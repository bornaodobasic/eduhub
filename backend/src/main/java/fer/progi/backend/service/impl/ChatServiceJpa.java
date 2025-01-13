package fer.progi.backend.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fer.progi.backend.dao.ChatMessageRepository;
import fer.progi.backend.domain.ChatMessage;

@Service
public class ChatServiceJpa {
	
	@Autowired
	private ChatMessageRepository messageRepository;

    public ChatMessage saveMessage(ChatMessage message) {
        return messageRepository.save(message);
    }

    public List<ChatMessage> getMessages(String recipient, String groupId) {
        return messageRepository.findByPrimateljOrGrupaId(recipient, groupId);
    }

	public List<ChatMessage> getMessagesBetweenUsers(String posiljatelj, String primatelj) {
		List<ChatMessage> listaPoruka = new ArrayList<>();
		listaPoruka.addAll(messageRepository.findMessagesBetweenUsers(primatelj, posiljatelj));
//		listaPoruka.addAll(messageRepository.findMessagesBetweenUsers(primatelj, posiljatelj));
		
		
		return listaPoruka;
	}

}
