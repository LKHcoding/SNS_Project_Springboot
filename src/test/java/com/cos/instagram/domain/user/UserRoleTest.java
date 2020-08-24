package com.cos.instagram.domain.user;

import org.junit.jupiter.api.Test;

public class UserRoleTest {
	
	@Test
	public void 유저롤_테스트() {
		System.out.println(UserRole.ADMIN.getKey());
		System.out.println(UserRole.ADMIN);
		User user = User.builder()
				.role(UserRole.ADMIN)
				.build();
		System.out.println(user.getRole());
	}
}
