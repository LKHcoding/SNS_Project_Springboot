package com.cos.instagram.config.hanlder.ex;

public class MyImageDeleteException extends RuntimeException {
	private String message;

	public MyImageDeleteException() {
		this.message = "게시물 작성자만 글을 지울 수 있습니다.";
	}

	public MyImageDeleteException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
