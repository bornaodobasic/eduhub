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
    const [messageTimestamps, setMessageTimestamps] = useState({});
    const [senderNames, setSenderNames] = useState({});

    useEffect(() => {
        fetch('/api/user/emailOnly')
            .then(response => response.text())
            .then(email => {
                setCurrentUserEmail(email.trim());
                const ws = new WebSocket(`ws://localhost:8080/chat2?email=${encodeURIComponent(email.trim())}`);
                setSocket(ws);
    
                ws.onmessage = event => {
                    console.log('Primljeni podaci putem WebSocketa:', event.data);
                    try {
                        const message = typeof event.data === 'string' && event.data.startsWith('{')
                            ? JSON.parse(event.data)
                            : { sadrzaj: event.data }; // Pretpostavka za slučaj kada nije JSON
    
                        setMessages(prevMessages => [...prevMessages, message]);

                    } catch (error) {
                        console.error('Greška pri parsiranju podataka:', error, event.data);
                    }
                };
                ws.onclose = () => console.log('WebSocket closed.');
            });

        return () => {
            if (socket) socket.close();
        };
    }, []);

    useEffect(() => {
        if (recipientType === 'nastavnik') {
            fetch('/api/nastavnik')
                .then(response => response.json())
                .then(data => setRecipients(data))
                .catch(error => console.error('Error fetching nastavnici:', error));
        } else if (recipientType === 'ucenik') {
            fetch('/api/nastavnik/ucenici')
                .then(response => response.json())
                .then(data => setRecipients(data))
                .catch(error => console.error('Error fetching ucenici:', error));
        } else if (recipientType === 'grupe') {
            fetch('/api/chat/grupe')
                .then(response => response.json())
                .then(data => setRecipients(data))
                .catch(error => console.error('Error fetching grupe:', error));
        } else {
            setRecipients([]); // Reset recipients if no type is selected
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
        fetch(`/api/chat/groupMessages/${group}`)
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
        console.log('Slanje poruke:', message);
        if (currentUserEmail && !senderNames[currentUserEmail]) {
            console.log(`Dodavanje imena pošiljatelja za ${currentUserEmail}`);
            // Pretpostavljamo da već imate način da dohvatite ime korisnika na temelju emaila
            fetch(`/api/chat/getUserName/${currentUserEmail}`)
                .then(response => response.text())
                .then(name => {
                    setSenderNames(prev => ({
                        ...prev,
                        [currentUserEmail]: name
                    }));
                })
                .catch(error => console.error('Greška pri dohvaćanju imena pošiljatelja:', error));
        }
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
            .then(data => {
                setGroups(data);
                if (recipientType === 'grupe') setRecipients(data); 
            })
            .catch(error => console.error('Error loading groups:', error));
    };

    const toggleTimestamp = index => {
        setMessageTimestamps(prev => ({
            ...prev,
            [index]: !prev[index]
        }));
    };

    const formatTime = (timestamp) => {
        console.log("Raw timestamp:", timestamp);
        try {
            let date;
            
            if (Array.isArray(timestamp)) {
                date = new Date(timestamp[0], timestamp[1] - 1, timestamp[2], timestamp[3], timestamp[4], timestamp[5], timestamp[6] / 1000000); 
            } 
            else if (typeof timestamp === 'string') {
                date = new Date(timestamp);
            }
            
            if (isNaN(date)) throw new Error("Invalid Date");
    
            return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        } catch (error) {
            console.error("Error formatting time:", error);
            return "Invalid Time";
        }
    };

    return (
        <div className="chat-container">
            <div className="chat-main">
            {selectedRecipient && (
                <div className="selected-recipient">
                    <h3>
                        {recipientType === 'grupe'
                            ? selectedRecipient
                            : recipients.find(r => r.email === selectedRecipient)?.ime || ""}
                    </h3>
                </div>
                )}
                 <div className="chat-messages">
                    {messages.map((msg, index) => (
                        <div
                            key={index}
                            className={`message ${msg.posiljatelj === currentUserEmail ? 'sent' : 'received'}`}
                            onClick={() =>
                                setMessages(prevMessages =>
                                    prevMessages.map((m, i) =>
                                            i === index ? { ...m, showTime: !m.showTime } : m
                                    )       
                                )
                            }
                        >
                            <p>
                                {recipientType === 'grupe' && msg.posiljatelj !== currentUserEmail ? (
                                    <strong>{senderNames[msg.posiljatelj].name || msg.posiljatelj}: </strong> // Display sender name if it's a group message
                                ) : (
                                    msg.posiljatelj === currentUserEmail ? 'Vi: ' : ''
                                    )}
                                {msg.sadrzaj}
                            </p>
                            {msg.showTime && <small className="message-time">{formatTime(msg.oznakaVremena)}</small>}
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
                        <option value="nastavnik">Nastavnika</option>
                        <option value="ucenik">Učenika</option>
                        <option value="grupe">Grupu</option>
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
                            <option key={recipient.email || recipient} value={recipient.email || recipient}>
                                {recipient.ime || recipient}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="group-controls">
                    <button
                        onClick={() => {
                            setRecipientType('ucenik');
                            setShowCreateGroup(prev => !prev);
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
                        <br/>
                        <h4>Odaberite članove:</h4>
                        <br/>
                        <ul>
                            {recipients.map(recipient => (
                                <li key={recipient.email} style={{display: 'flex', alignItems: 'center', gap: '10px'}}>
                                    <input
                                        type="checkbox"
                                        value={recipient.email}
                                        onChange={e => {
                                            if (e.target.checked) {
                                                setGroupMembers(prev => [...prev, recipient.email]);
                                            } else {
                                                setGroupMembers(prev =>
                                                    prev.filter(member => member !== recipient.email)
                                                );
                                            }
                                        }}
                                    />
                                    <span>{recipient.ime}</span>
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