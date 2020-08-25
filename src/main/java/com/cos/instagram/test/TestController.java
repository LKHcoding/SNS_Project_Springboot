package com.cos.instagram.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.instagram.config.hanlder.ex.MyUsernameNotFoundException;

@Controller
public class TestController {
	
	@GetMapping("/test/login")
	public String test1() {
		return "auth/login";
	}
	
	@GetMapping("/test/join")
	public String test2() {
		return "auth/join";
	}
	
	@GetMapping("/test/following")
	public String test3() {
		return "follow/following";
	}
	
	@GetMapping("/test/explore")
	public String test4() {
		return "image/explore";
	}
	
	@GetMapping("/test/feed")
	public String test5() {
		return "image/feed";
	}
	
	@GetMapping("/test/imageUpload")
	public String test6() {
		return "image/image-upload";
	}
	
	@GetMapping("/test/profileEdit")
	public String test7() {
		return "user/profile-edit";
	}
	
	@GetMapping("/test/profile")
	public String test8() {
		return "user/profile";
	}
	
	@GetMapping("/test/username/{username}")
	public @ResponseBody String test9(@PathVariable String username) {
		if(!username.equals("ssar")) {
			throw new MyUsernameNotFoundException("유저네임 못찾음");
		}
		
		return "username test";
	}
}
