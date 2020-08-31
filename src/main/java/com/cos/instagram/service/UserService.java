package com.cos.instagram.service;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.instagram.config.auth.dto.LoginUser;
import com.cos.instagram.config.hanlder.ex.MyUserIdNotFoundException;
import com.cos.instagram.domain.image.Image;
import com.cos.instagram.domain.image.ImageRepository;
import com.cos.instagram.domain.user.User;
import com.cos.instagram.domain.user.UserRepository;
import com.cos.instagram.web.dto.JoinReqDto;
import com.cos.instagram.web.dto.UserProfileRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final ImageRepository imageRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Transactional
	public void 회원가입(JoinReqDto joinReqDto) {
		String encPassword = 
				bCryptPasswordEncoder.encode(joinReqDto.getPassword());
		joinReqDto.setPassword(encPassword);
		userRepository.save(joinReqDto.toEntity());
	}
	
	// 읽기 전용 트랜잭션
	// (1) 변경 감지 연산을 하지 않음.
	// (2) isolation(고립성)을 위해 Phantom read 문제가 일어나지 않음.
	@Transactional(readOnly = true)
	public UserProfileRespDto 회원프로필(int id, LoginUser loginUser) {
		
		int imageCount;
		int followerCount;
		int followingCount;
		
		User userEntity = userRepository.findById(id)
				.orElseThrow(new Supplier<MyUserIdNotFoundException>() {
					@Override
					public MyUserIdNotFoundException get() {
						return new MyUserIdNotFoundException();
					}
				});
		
		// 1. 이미지들과 전체 이미지 카운트
		List<Image> imagesEntity = imageRepository.findByUserId(id);
		imageCount = imagesEntity.size();
		
		// 2. 팔로우 수 (수정해야됨)
		followerCount = 50;
		followingCount = 100;
		
		// 3. 최종마무리
		UserProfileRespDto userProfileRespDto = 
				UserProfileRespDto.builder()
				.pageHost(id==loginUser.getId())
				.followerCount(followerCount)
				.followingCount(followingCount)
				.imageCount(imageCount)
				.user(userEntity)
				.images(imagesEntity)
				.build();
		
		return userProfileRespDto;
	}
}






