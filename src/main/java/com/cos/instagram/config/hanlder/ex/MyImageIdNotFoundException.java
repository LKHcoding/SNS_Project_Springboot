package com.cos.instagram.config.hanlder.ex;

public class MyImageIdNotFoundException extends RuntimeException{
	
	private String message;
	
	public MyImageIdNotFoundException() {
		this.message = "해당 이미지의 id를 찾을 수 없습니다.";
	}
	
	public MyImageIdNotFoundException(String message) {	
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
}
