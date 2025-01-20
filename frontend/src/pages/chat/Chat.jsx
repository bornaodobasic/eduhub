import React, { useState, useEffect } from 'react';
import './Chat.css';

const Chat = () => {
    const [currentUserEmail, setCurrentUserEmail] = useState('');
    const [recipients, setRecipients] = useState([]);
    const [selectedRecipient, setSelectedRecipient] = useState('');
    const [messages, setMessages] = useState([]);
    const [messageInput, setMessageInput] = useState('');
    const [socket, setSocket] = useState(null);
    const [groups, setGroups] = useState([]);
    const [showCreateGroup, setShowCreateGroup] = useState(false);
    const [groupName, setGroupName] = useState('');
    const [groupMembers, setGroupMembers] = useState([]);
    const [recipientType, setRecipientType] = useState('');
    const [showGroups, setShowGroups] = useState(false);

    useEffect(() => {
        fetch('/api/user/emailOnly')
            .then(response => response.text())
            .then(email => {
                setCurrentUserEmail(email.trim());
                const ws = new WebSocket(`ws://localhost:8080/ws/chat?email=${encodeURIComponent(email.trim())}`);
                setSocket(ws);

                ws.onmessage = event => {
                    const message = JSON.parse(event.data);
                    setMessages(prevMessages => [...prevMessages, message]);
                };

                ws.onclose = () => console.log('WebSocket closed.');
            });

        return () => {
            if (socket) socket.close();
        };
    }, []);

    useEffect(() => {
        if (recipientType === 'nastavnik') {
            fetch('/api/ucenik/nastavnici')
                .then(response => response.json())
                .then(data => setRecipients(data))
                .catch(error => console.error('Error fetching nastavnici:', error));
        } else if (recipientType === 'ucenik') {
            fetch('/api/ucenik')
                .then(response => response.json())
                .then(data => setRecipients(data))
                .catch(error => console.error('Error fetching ucenici:', error));
        } else if (recipientType === 'grupe') {
            fetch('/api/chat/grupe')
                .then(response => response.json())
                .then(data => setRecipients(data))
                .catch(error => console.error('Error fetching grupe:', error));
        } else {
            setRecipients([]);
        }
    }, [recipientType]);

    useEffect(() => {
        if (showGroups) {
            loadGroups();
        }
    }, [showGroups]);

    useEffect(() => {
        if (recipientType !== 'grupe' && selectedRecipient) {
            fetchMessages(currentUserEmail, selectedRecipient);
        }
    }, [selectedRecipient, recipientType]);

    useEffect(() => {
        if (recipientType === 'grupe' && selectedRecipient) {
            fetchGroupMessages(selectedRecipient);
        }
    }, [selectedRecipient, recipientType]);

    const fetchMessages = (korisnik1, korisnik2) => {
        fetch(`/api/chat/messages?korisnik1=${korisnik1}&korisnik2=${korisnik2}`)
            .then(response => response.json())
            .then(data => setMessages(data))
            .catch(error => console.error('Error fetching messages:', error));
    };

    const fetchGroupMessages = group => {
        fetch(`/api/chat/messagesGrupa?grupaNaziv=${group}`)
            .then(response => response.json())
            .then(data => setMessages(data))
            .catch(error => console.error('Error fetching group messages:', error));
    };

    const sendMessage = () => {
        if (!selectedRecipient || !messageInput) {
            alert('Please select a recipient and enter a message.');
            return;
        }

        const message = {
            posiljatelj: currentUserEmail,
            primatelj: selectedRecipient,
            sadrzaj: messageInput,
            oznakaVremena: new Date().toISOString()
        };

        if (socket) socket.send(JSON.stringify(message));
        setMessages(prevMessages => [...prevMessages, message]);
        setMessageInput('');
    };

    const createGroup = () => {
        if (!groupName || groupMembers.length === 0) {
            alert('Please provide a group name and select members.');
            return;
        }

        const groupData = {
            imeGrupe: groupName,
            clanovi: groupMembers
        };

        fetch('/api/chat/kreirajGrupu', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(groupData)
        })
            .then(response => {
                if (response.ok) alert('Group created successfully!');
                else alert('Error creating group.');
            })
            .catch(error => console.error('Error creating group:', error));

        setShowCreateGroup(false);
        setGroupName('');
        setGroupMembers([]);
    };

    const loadGroups = () => {
        fetch('/api/chat/grupe')
            .then(response => response.json())
            .then(data => setGroups(data));
    };

    return (
        <div className="chat-container">
            <div className="chat-main">
                <div className="chat-messages">
                    {messages.map((msg, index) => (
                        <div
                            key={index}
                            className={`message ${
                                msg.posiljatelj === currentUserEmail ? 'sent' : 'received'
                            }`}
                        >
                            <p>{msg.sadrzaj}</p>
                        </div>
                    ))}
                </div>
                <div className="chat-input">
                    <textarea
                        value={messageInput}
                        onChange={e => setMessageInput(e.target.value)}
                        placeholder="Unesite poruku..."
                    />
                    <button onClick={sendMessage}>Pošaljite</button>
                </div>
            </div>

            <div className="chat-sidebar">
                <div className="form-group">
                    <h3>Poruka za:</h3>
                    <select
                        value={recipientType}
                        onChange={e => {
                            setRecipientType(e.target.value);
                            setSelectedRecipient('');
                        }}
                    >
                        <option value="">--Odaberite--</option>
                        <option value="nastavnik">Nastavnici</option>
                        <option value="ucenik">Učenici</option>
                        <option value="grupe">Grupe</option>
                    </select>
                </div>

                <div className="form-group">
                    <h4>Odaberi primatelja:</h4>
                    <select
                        value={selectedRecipient}
                        onChange={e => setSelectedRecipient(e.target.value)}
                    >
                        <option value="">--Odaberite--</option>
                        {recipients.map(recipient => (
                            <option key={recipient} value={recipient}>
                                {recipient}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="group-controls">
                    <button
                        onClick={() => {
                            setRecipientType('ucenik');
                            setShowCreateGroup(true);
                        }}
                    >
                        Kreiraj grupu
                    </button>
                    <button
                        onClick={() => {
                            loadGroups();
                            setShowGroups(!showGroups);
                        }}
                    >
                        Moje grupe
                    </button>
                </div>

                {showCreateGroup && (
                    <div className="create-group-container">
                        <h3>Kreiraj novu grupu</h3>
                        <input
                            type="text"
                            value={groupName}
                            onChange={e => setGroupName(e.target.value)}
                            placeholder="Unesite ime grupe"
                        />
                        <h4>Odaberite članove:</h4>
                        <ul>
                            {recipients.map(recipient => (
                                <li key={recipient}>
                                    <input
                                        type="checkbox"
                                        value={recipient}
                                        onChange={e => {
                                            if (e.target.checked) {
                                                setGroupMembers(prev => [...prev, recipient]);
                                            } else {
                                                setGroupMembers(prev =>
                                                    prev.filter(member => member !== recipient)
                                                );
                                            }
                                        }}
                                    />
                                    {recipient}
                                </li>
                            ))}
                        </ul>
                        <button onClick={createGroup}>Spremi grupu</button>
                    </div>
                )}

                {showGroups && (
                    <div className="group-list-container">
                        <ul>
                            {groups.map(group => (
                                <li key={group}>{group}</li>
                            ))}
                        </ul>
                    </div>
                )}
            </div>
        </div>
    );
};

export default Chat;