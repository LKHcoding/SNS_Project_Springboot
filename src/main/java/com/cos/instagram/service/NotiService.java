package com.cos.instagram.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.instagram.domain.noti.Noti;
import com.cos.instagram.domain.noti.NotiRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NotiService {
	private final NotiRepository notiRepository;
	
	@Transactional(readOnly = true)
	public List<Noti> 알림리스트(int loginUserId){
		return notiRepository.findByToUserId(loginUserId);
	}
}
