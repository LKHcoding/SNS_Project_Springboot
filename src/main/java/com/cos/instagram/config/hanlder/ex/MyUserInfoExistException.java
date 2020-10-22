package com.cos.instagram.config.hanlder.ex;

public class MyUserInfoExistException extends RuntimeException {
	private String message;

	public MyUserInfoExistException() {
		this.message = "이미 존재하는 Email 또는 사용자 이름 입니다.";
	}

	public MyUserInfoExistException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
