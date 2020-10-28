package com.cos.instagram.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cos.instagram.config.auth.LoginUserAnnotation;
import com.cos.instagram.config.auth.dto.LoginUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatController {

	// LKH chat pagecontroller부분
	@GetMapping("/chat")
	public String Chatting(@LoginUserAnnotation LoginUser loginUser, Model model) {
		/*
		 * model.addAttribute("followingList", followService.팔로잉리스트(loginUser.getId(),
		 * pageUserId));
		 */
		return "chat/direct-message";
	}

	// test
	@GetMapping("/chatTemplate")
	public String ChatTemplate(@LoginUserAnnotation LoginUser loginUser, Model model) {
		/*
		 * model.addAttribute("followingList", followService.팔로잉리스트(loginUser.getId(),
		 * pageUserId));
		 */
		return "chat/chatTemplate";
	}
}
