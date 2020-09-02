package com.cos.instagram.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.instagram.config.auth.LoginUserAnnotation;
import com.cos.instagram.config.auth.dto.LoginUser;
import com.cos.instagram.service.FollowService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class FollowController {

	private final FollowService followService;
	
	@GetMapping("/follow/followingList/{userId}")
	public String followingList(@PathVariable int userId) {
		return "follow/following-list";
	}
	
	@GetMapping("/follow/followerList/{userId}")
	public String followerList(@PathVariable int userId) {
		return "follow/follower-list";
	}
	
	@PostMapping("/follow/{id}")
	public ResponseEntity<?> follow(@PathVariable int id,
			@LoginUserAnnotation LoginUser loginUser) {
		
		followService.팔로우(loginUser.getId(), id);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
	
	@DeleteMapping("/follow/{id}")
	public ResponseEntity<?> unFollow(@PathVariable int id,
			@LoginUserAnnotation LoginUser loginUser) {
		
		followService.팔로우취소(loginUser.getId(), id);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
}
