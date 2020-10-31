package com.cos.instagram.web;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.instagram.config.auth.LoginUserAnnotation;
import com.cos.instagram.config.auth.dto.LoginUser;
import com.cos.instagram.domain.user.User;
import com.cos.instagram.service.UserService;
import com.cos.instagram.web.dto.JoinReqDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AuthController {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	private final UserService userService;

	@GetMapping("/auth/loginForm")
	public String loginForm() {
		log.info("/auth/loginForm 진입");
		return "auth/loginForm";
	}

	@GetMapping("/auth/joinForm")
	public String joinForm() {
		log.info("/auth/joinForm 진입");
		return "auth/joinForm";
	}

	// 회원가입 처리하는 부분
	@PostMapping("/auth/join")
	public String join(JoinReqDto joinReqDto) {
		log.info(joinReqDto.toString());
		userService.회원가입(joinReqDto);
		return "redirect:/auth/loginForm";
	}

	// 비밀번호 변경 진입
	@GetMapping("/auth/pwChange")
	public void pwChange(String oldPassword, boolean passwordCK, @LoginUserAnnotation LoginUser loginUser, Model model) {
		log.info("/auth/pwChange 진입 ");
		User userEntity = userService.회원정보(loginUser);
		model.addAttribute("user", userEntity);

	}

	// 비밀번호 변경 요청
	@PostMapping("/auth/pwChange")
	public String pwChange(@LoginUserAnnotation LoginUser loginUser, CharSequence oldPassword, CharSequence newPassword,
			CharSequence newRePassword, boolean passwordCK, JoinReqDto joinReqDto, HttpSession session, Model model) {
		String result = null;
		log.info("비밀번호 변경 요청 발생!!", joinReqDto.toString());

		userService.비밀번호변경(loginUser, oldPassword, newPassword, newRePassword, passwordCK, joinReqDto);

		return "redirect:/auth/logout";
	}
}
