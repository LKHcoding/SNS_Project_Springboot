package com.cos.instagram.domain.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.instagram.web.dto.UserProfileRespDto;

// JpaRepository가 extends 되면 @Repository 필요 없음. IoC자동으로 됨.
public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByUsername(String username);
}
