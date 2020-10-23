package com.cos.instagram.web;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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

	@GetMapping("/auth/pwChange")
	public String pwChange() {
		log.info("/auth/pwChange 진입");
		return "/auth/pwChange";
	}

	//비밀번호 확인 처리 요청
	@PostMapping("/checkPassword")
	public String checkPassword(String password, HttpSession session) throws Exception {

		log.info("비밀번호 확인 요청 발생!!");
		String result = null;

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		JoinReqDto dbJoinReqDto = (JoinReqDto)session.getAttribute("login");
		log.info("DB 회원의 비밀번호 : " + dbJoinReqDto.getPassword());
		log.info("폼에서 받아온 비밀번호 : " + password);

		if(encoder.matches(password, dbJoinReqDto.getPassword())) {
			result = "pwConfirmOK";
		} else {
			result = "pwConfirmNO";
		}
		return result;
	}

	//비밀번호 변경 요청
	@PostMapping(value = "/auth/pwChange", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String pwChange(JoinReqDto joinReqDto, HttpSession session) throws Exception{
		log.info("비밀번호 변경 요청 발생~!!",joinReqDto.toString());

		userService.비밀번호변경(joinReqDto);

		//비밀번호 성공 시 다시 로그인 세션 객체에 담음
		JoinReqDto modifyUser = new JoinReqDto();
		modifyUser.setUsername(joinReqDto.getUsername());


		return "redirect:/auth/loginForm";
	}
}


