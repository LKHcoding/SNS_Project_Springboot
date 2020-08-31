package com.cos.instagram.config.hanlder.ex;

public class MyUsernameNotFoundException extends RuntimeException{
	
	private String message;
	
	public MyUsernameNotFoundException() {
		this.message = "해당 유저네임을 찾을 수 없습니다.";
	}
	
	public MyUsernameNotFoundException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}
