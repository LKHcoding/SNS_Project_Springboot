package com.cos.instagram.web;

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
import com.cos.instagram.domain.user.User;
import com.cos.instagram.service.UserService;
import com.cos.instagram.web.dto.UserProfileRespDto;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/user/{id}")
	public String profile(@PathVariable int id, @LoginUserAnnotation LoginUser loginUser, Model model) {
		UserProfileRespDto userProfileRespDto = userService.회원프로필(id, loginUser);
		model.addAttribute("respDto", userProfileRespDto);
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

	@GetMapping("user/board/{imageid}")
	public String FeedComment() {
		return "user/board";
	}
}
