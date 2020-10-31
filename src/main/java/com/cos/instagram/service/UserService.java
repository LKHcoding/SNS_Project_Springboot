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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cos.instagram.config.auth.dto.LoginUser;
import com.cos.instagram.config.hanlder.ex.MyUserIdNotFoundException;
import com.cos.instagram.config.hanlder.ex.MyUserInfoExistException;
import com.cos.instagram.domain.comment.Comment;
import com.cos.instagram.domain.follow.FollowRepository;
import com.cos.instagram.domain.image.Image;
import com.cos.instagram.domain.like.Likes;
import com.cos.instagram.domain.user.User;
import com.cos.instagram.domain.user.UserBoardRepository;
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

	private final UserBoardRepository userboardRepository;

	@Value("${file.path}")
	private String uploadFolder;

	@Transactional
	public void 프로필사진업로드(LoginUser loginUser, MultipartFile file) {
		UUID uuid = UUID.randomUUID();
		String imageFilename = uuid + "_" + file.getOriginalFilename();
		Path imageFilepath = Paths.get(uploadFolder + imageFilename);
		try {
			Files.write(imageFilepath, file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		User userEntity = userRepository.findById(loginUser.getId())
				.orElseThrow(new Supplier<MyUserIdNotFoundException>() {
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

	@Transactional(readOnly = false)
	public void 비밀번호변경(JoinReqDto joinReqDto) throws Exception {

		String username = joinReqDto.getUsername();

		System.out.println("비밀번호 변경 서비스 진입");

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

//			System.out.println("username: " + joinReqDto.getUsername());
		System.out.println("username: " + auth.getName());
		System.out.println("암호화 전 비밀번호: " + joinReqDto.getPassword());

		// 비밀번호를 암호화하여 joinReqDto객체에 다시 저장
		String newPassword = encoder.encode(joinReqDto.getPassword());
		joinReqDto.setPassword(newPassword);

		System.out.println("암호화 후 비밀번호  : " + joinReqDto.getPassword());
		// joinReqDto.setPassword(encPassword);

		userRepository.modifyPassword(username, newPassword);
		// userRepository.save(joinReqDto.toEntity());

	}

	@Transactional(readOnly = true)
	public User 회원정보(LoginUser loginUser) {
		return userRepository.findById(loginUser.getId()).orElseThrow(new Supplier<MyUserIdNotFoundException>() {
			@Override
			public MyUserIdNotFoundException get() {
				return new MyUserIdNotFoundException();
			}
		});
	}

	@Transactional
	public void 회원가입(JoinReqDto joinReqDto) throws MyUserInfoExistException {
		System.out.println("서비스 회원가입 들어옴");
		System.out.println(joinReqDto);

		// 이메일, 이름은 중복체크가 필요하다.
		List<User> DuplicateCheckList = userRepository.중복체크(joinReqDto.getEmail(), joinReqDto.getUsername());
		System.out.println(DuplicateCheckList.size());
		if (!(DuplicateCheckList.isEmpty())) {
			System.out.println("중복 이메일 또는 아이디가 있습니다.");
			throw new MyUserInfoExistException("이미 존재하는 Email 또는 사용자 이름 입니다.");
		} else {
			String encPassword = bCryptPasswordEncoder.encode(joinReqDto.getPassword());
			System.out.println("encPassword : " + encPassword);
			joinReqDto.setPassword(encPassword);
			userRepository.save(joinReqDto.toEntity());
		}
	}

	// 프로필 페이지에서 특정유저의 게시물정보를 모두 받아오기 위해 만든 부분
	@Transactional
	public List<Image> 특정유저게시물(int BoardUserid, int loginUserId) {
		List<Image> boards = null;
		boards = userboardRepository.mUserBoard(BoardUserid);
		for (Image board : boards) {
			board.setLikeCount(board.getLikes().size());

			// 좋아요 상태 여부 등록
			for (Likes like : board.getLikes()) {
				if (like.getUser().getId() == loginUserId) {
					board.setLikeState(true);
				}
			}
			// 댓글 주인 여부 등록
			for (Comment comment : board.getComments()) {
				if (comment.getUser().getId() == loginUserId) {
					comment.setCommentHost(true);
				}
			}
		}
		return boards;
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

		User userEntity = userRepository.findById(id).orElseThrow(new Supplier<MyUserIdNotFoundException>() {
			@Override
			public MyUserIdNotFoundException get() {
				return new MyUserIdNotFoundException();
			}
		});

		// 1. 이미지들과 전체 이미지 카운트(dto받기)
		StringBuilder sb = new StringBuilder();
		sb.append("select im.id, im.imageUrl, im.userId,");
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
		followState = followRepository.mFollowState(loginUser.getId(), id) == 1 ? true : false;

		// 4. 최종마무리
		UserProfileRespDto userProfileRespDto = UserProfileRespDto.builder().pageHost(id == loginUser.getId())
				.followState(followState).followerCount(followerCount).followingCount(followingCount)
				.imageCount(imageCount).user(userEntity).images(imagesEntity) // 수정완료(Dto만듬) (댓글수, 좋아요수)
				.build();

		return userProfileRespDto;
	}

	// feed에 회원 추천 기능
	@Transactional(readOnly = true)
	public List<User> 추천유저(int loginUserId) {
		return userRepository.mRecommendationImage(loginUserId);
	}

	// DM페이지에서 전체 회원 불러오기
	@Transactional(readOnly = true)
	public List<User> 전체회원(int loginUserId) {
		return userRepository.mAllUserList(loginUserId);
	}

	// DM페이지에서 특정 회원 검색하기
	@Transactional(readOnly = true)
	public List<User> DM회원검색(String username, int id) {
		username = "%" + username + "%";
		return userRepository.mSearchUserList(username, id);
	}

	// DM페이지에서 특정 회원 불러오기
	@Transactional(readOnly = true)
	public User 특정회원(int selectedUserId) {
		return userRepository.mSelectedUser(selectedUserId);
	}
}
