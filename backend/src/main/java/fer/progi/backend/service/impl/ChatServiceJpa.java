package fer.progi.backend.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fer.progi.backend.dao.ChatGroupRepository;
import fer.progi.backend.dao.ChatMessageRepository;
import fer.progi.backend.domain.ChatGroup;
import fer.progi.backend.domain.ChatMessage;

@Service
public class ChatServiceJpa {
	
	@Autowired
	private ChatMessageRepository messageRepository;
	
	@Autowired
	private ChatGroupRepository groupRepository;

    public ChatMessage saveMessage(ChatMessage message) {
        return messageRepository.save(message);
    }

	public List<ChatMessage> getMessagesBetweenUsers(String posiljatelj, String primatelj) {
		List<ChatMessage> listaPoruka = new ArrayList<>();
		listaPoruka.addAll(messageRepository.findMessagesBetweenUsers(primatelj, posiljatelj));
		
		
		return listaPoruka;
	}

	public void kreirajGrupu(List<String> clanovi, String imeGrupe) {
		ChatGroup chatGrupa = new ChatGroup();
		chatGrupa.setClanovi(clanovi);
		chatGrupa.setImeGrupe(imeGrupe);
		
		groupRepository.save(chatGrupa);
		
	}

	public List<String> getImenaGrupa(String email) {
		List<String> listaImenaGrupa = new ArrayList<>();
 		for(ChatGroup g :groupRepository.findAll()) {
 			if(g.getClanovi().contains(email)) {
 				listaImenaGrupa.add(g.getImeGrupe());
 			}
		}
		return listaImenaGrupa;
	}

	public List<ChatMessage> getMessagesForGroup(String imeGrupe) {
		return messageRepository.findByImeGrupe(imeGrupe);
	}
	
	public List<String> getMembersEmails(String imeGrupe) {
		return groupRepository.findByImeGrupe(imeGrupe).getClanovi();
		
		
	}

}
