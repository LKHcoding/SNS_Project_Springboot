package com.cos.instagram.domain.user;

import lombok.Getter;

@Getter
public enum UserRole {
	USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

	UserRole(String key) {
		this.key = key;
	}
	
	private String key;
}
