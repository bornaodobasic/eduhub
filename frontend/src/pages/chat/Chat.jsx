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

        fetch('/api/ucenik')
            .then(response => response.json())
            .then(data => setRecipients(data));

        return () => {
            if (socket) socket.close();
        };
    }, []);

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
            <h2>Chat</h2>
            <div className="form-group">
                <label htmlFor="recipient">Odaberi primatelja ili grupu:</label>
                <select
                    id="recipient"
                    value={selectedRecipient}
                    onChange={e => {
                        setSelectedRecipient(e.target.value);
                        if (e.target.value.includes('grupa')) {
                            fetchGroupMessages(e.target.value);
                        } else {
                            fetchMessages(currentUserEmail, e.target.value);
                        }
                    }}
                >
                    <option value="">--Odaberi primatelja ili grupu--</option>
                    {recipients.map(recipient => (
                        <option key={recipient} value={recipient}>{recipient}</option>
                    ))}
                </select>
            </div>
            <textarea
                value={messageInput}
                onChange={e => setMessageInput(e.target.value)}
                placeholder="Unesi poruku..."
            />
            <button onClick={sendMessage}>Pošaljite</button>

            <div className="chat-box">
                {messages.map((msg, index) => (
                    <div
                        key={index}
                        className={`message ${msg.posiljatelj === currentUserEmail ? 'sent' : 'received'}`}
                    >
                        {msg.posiljatelj === currentUserEmail ? `Ti: ${msg.sadrzaj}` : `${msg.posiljatelj}: ${msg.sadrzaj}`}
                    </div>
                ))}
            </div>

            <div className="group-controls">
                <button onClick={() => setShowCreateGroup(true)}>Kreiraj grupu</button>
                <button onClick={loadGroups}>Moje grupe</button>
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
                                            setGroupMembers(prev => prev.filter(member => member !== recipient));
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

            <div className="group-list-container">
                <h3>Moje grupe</h3>
                <ul>
                    {groups.map(group => (
                        <li key={group}>{group}</li>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default Chat;
