package com.cos.instagram.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.instagram.domain.chatting.Chatting;
import com.cos.instagram.domain.chatting.ChattingRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatService {
	private final ChattingRepository chattingRepository;

	@Transactional
	public void ChattingSave(int fromUserId, int toUserId, String message) {
		int result = chattingRepository.mChatSave(fromUserId, toUserId, message);
		System.out.println("대화 저장 : " + result);
	}

	@Transactional
	public List<Chatting> ChattingLoad(int fromUserId, int toUserId) {
		List<Chatting> result = chattingRepository.mChatList(fromUserId, toUserId);
		System.out.println("불러온대화몇개? : " + result.size());
		return result;

	}
}
