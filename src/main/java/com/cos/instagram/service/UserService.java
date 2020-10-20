package com.cos.instagram.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cos.instagram.config.auth.dto.LoginUser;
import com.cos.instagram.config.hanlder.ex.MyUserIdNotFoundException;
import com.cos.instagram.domain.follow.FollowRepository;
import com.cos.instagram.domain.user.User;
import com.cos.instagram.domain.user.UserRepository;
import com.cos.instagram.web.dto.JoinReqDto;
import com.cos.instagram.web.dto.UserProfileImageRespDto;
import com.cos.instagram.web.dto.UserProfileRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	@PersistenceContext
	private EntityManager em;
	private final UserRepository userRepository;
	private final FollowRepository followRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Value("${file.path}")
	private String uploadFolder;

	@Transactional
	public void 프로필사진업로드(LoginUser loginUser, MultipartFile file) {
		UUID uuid = UUID.randomUUID();
		String imageFilename =
				uuid+"_"+file.getOriginalFilename();
		Path imageFilepath = Paths.get(uploadFolder+imageFilename);
		try {
			Files.write(imageFilepath, file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		User userEntity = userRepository.findById(loginUser.getId()).orElseThrow(new Supplier<MyUserIdNotFoundException>() {
			@Override
			public MyUserIdNotFoundException get() {
				return new MyUserIdNotFoundException();
			}
		});

		// 더티체킹
		userEntity.setProfileImage(imageFilename);
	}

	@Transactional
	public void 회원수정(User user) {
		// 더티 체킹
		User userEntity = userRepository.findById(user.getId()).orElseThrow(new Supplier<MyUserIdNotFoundException>() {
			@Override
			public MyUserIdNotFoundException get() {
				return new MyUserIdNotFoundException();
			}
		});
		userEntity.setName(user.getName());
		userEntity.setWebsite(user.getWebsite());
		userEntity.setBio(user.getBio());
		userEntity.setPhone(user.getPhone());
		userEntity.setGender(user.getGender());
	}

	@Transactional(readOnly = true)
	public User 회원정보(LoginUser loginUser) {
		return userRepository.findById(loginUser.getId())
				.orElseThrow(new Supplier<MyUserIdNotFoundException>() {
					@Override
					public MyUserIdNotFoundException get() {
						return new MyUserIdNotFoundException();
					}
				});
	}

	@Transactional
	public void 회원가입(JoinReqDto joinReqDto) {
		System.out.println("서비스 회원가입 들어옴");
		System.out.println(joinReqDto);
		String encPassword =
				bCryptPasswordEncoder.encode(joinReqDto.getPassword());
		System.out.println("encPassword : "+encPassword);
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
		boolean followState;

		User userEntity = userRepository.findById(id)
				.orElseThrow(new Supplier<MyUserIdNotFoundException>() {
					@Override
					public MyUserIdNotFoundException get() {
						return new MyUserIdNotFoundException();
					}
				});

		// 1. 이미지들과 전체 이미지 카운트(dto받기)
		StringBuilder sb = new StringBuilder();
		sb.append("select im.id, im.imageUrl, ");
		sb.append("(select count(*) from likes lk where lk.imageId = im.id) as likeCount, ");
		sb.append("(select count(*) from comment ct where ct.imageId = im.id) as commentCount ");
		sb.append("from image im where im.userId = ? ");
		String q = sb.toString();
		Query query = em.createNativeQuery(q, "UserProfileImageRespDtoMapping").setParameter(1, id);
		List<UserProfileImageRespDto> imagesEntity = query.getResultList();

		imageCount = imagesEntity.size();

		// 2. 팔로우 수
		followerCount = followRepository.mCountByFollower(id);
		followingCount = followRepository.mCountByFollowing(id);

		// 3. 팔로우 유무
		followState =
				followRepository.mFollowState(loginUser.getId(), id) == 1 ? true : false;

		// 4. 최종마무리
		UserProfileRespDto userProfileRespDto =
				UserProfileRespDto.builder()
				.pageHost(id==loginUser.getId())
				.followState(followState)
				.followerCount(followerCount)
				.followingCount(followingCount)
				.imageCount(imageCount)
				.user(userEntity)
				.images(imagesEntity) // 수정완료(Dto만듬) (댓글수, 좋아요수)
				.build();

		return userProfileRespDto;
	}

	// feed에 회원 추천 기능
	@Transactional(readOnly = true)
	public List<User> 추천유저(int loginUserId) {
		return userRepository.mRecommendationImage(loginUserId);
	}
}






