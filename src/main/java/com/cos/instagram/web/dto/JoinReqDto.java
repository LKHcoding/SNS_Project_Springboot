package com.cos.instagram.web.dto;

import com.cos.instagram.domain.user.User;
import com.cos.instagram.domain.user.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinReqDto {
	private String email;
	private String name;
	private String username;
	private String password;
	
	public User toEntity() {
		return User.builder()
				.email(email)
				.name(name)
				.username(username)
				.password(password)
				.role(UserRole.USER)
				.build();
	}
}
