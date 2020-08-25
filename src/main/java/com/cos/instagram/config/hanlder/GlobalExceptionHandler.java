package com.cos.instagram.config.hanlder;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.instagram.config.hanlder.ex.MyUsernameNotFoundException;
import com.cos.instagram.util.Script;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value=MyUsernameNotFoundException.class)
	public String myUsernameNotFoundException(Exception e) {
		return Script.alert("아이디 혹은 패스워드가 잘못되었습니다.");
	}
}
