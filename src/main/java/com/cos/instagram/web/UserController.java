package com.cos.instagram.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cos.instagram.config.auth.LoginUserAnnotation;
import com.cos.instagram.config.auth.dto.LoginUser;
import com.cos.instagram.domain.image.Image;
import com.cos.instagram.domain.user.User;
import com.cos.instagram.service.FollowService;
import com.cos.instagram.service.UserService;
import com.cos.instagram.web.dto.UserProfileRespDto;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private FollowService followService;
	
	//팔로워,팔로우 모달창에 구현
	@GetMapping("/user/{pageUserId}")
	public String profile(@PathVariable int pageUserId, @LoginUserAnnotation LoginUser loginUser, Model model) {
		UserProfileRespDto userProfileRespDto = userService.회원프로필(pageUserId, loginUser);
		model.addAttribute("respDto", userProfileRespDto);
		model.addAttribute("followingList", followService.팔로잉리스트(loginUser.getId(),pageUserId));
		model.addAttribute("followerList", followService.팔로워리스트(loginUser.getId(),pageUserId));
		
		// 프로필 페이지에서 특정 회원의 게시물정보를 받아오기위해 추가한 부분
		List<Image> UserBoard = userService.특정유저게시물(id, loginUser.getId());
		model.addAttribute("board", UserBoard);
		return "user/profile";
	}

	@GetMapping("/user/profileEdit")
	public String profileEdit(@LoginUserAnnotation LoginUser loginUser, Model model) {
		User userEntity = userService.회원정보(loginUser);
		model.addAttribute("user", userEntity);
		return "user/profile-edit";
	}

	@PutMapping("/user")
	public ResponseEntity<?> userUpdate(User user) {
		userService.회원수정(user);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	// 원래는 put으로 하는게 맞는데 편하게 하기 위해
	@PostMapping("/user/profileUpload")
	public String userProfileUpload(@RequestParam("profileImage") MultipartFile file, int userId,
			@LoginUserAnnotation LoginUser loginUser) {
		if (userId == loginUser.getId()) {
			userService.프로필사진업로드(loginUser, file);
		}

		return "redirect:/user/" + userId;
	}
}
