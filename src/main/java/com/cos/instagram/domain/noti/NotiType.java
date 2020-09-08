package com.cos.instagram.domain.noti;

import lombok.Getter;

@Getter
public enum NotiType {
	LIKE("좋아요"), COMMENT("댓글작성"), FOLLOW("팔로우");
	
	NotiType(String key) {
		this.key = key;
	}
	
	private String key;
}
