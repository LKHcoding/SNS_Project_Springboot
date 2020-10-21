package com.cos.instagram.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.instagram.domain.noti.Noti;
import com.cos.instagram.domain.noti.NotiRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NotiService {
	private final NotiRepository notiRepository;
	

	HttpServletRequest req;
	
	@Autowired
	private HttpSession session;
	/*
	 * private HttpSession session = req.getSession();
	 */	
	
	@Transactional(readOnly = true)
	public List<Noti> 알림리스트(int loginUserId){
		List<Noti> Noti = notiRepository.findByToUserId(loginUserId);
		session.setAttribute("staticNoti", Noti);
		return Noti;
	}
	
}
