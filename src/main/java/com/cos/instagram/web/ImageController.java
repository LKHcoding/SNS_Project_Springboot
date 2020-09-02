package com.cos.instagram.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.instagram.config.auth.LoginUserAnnotation;
import com.cos.instagram.config.auth.dto.LoginUser;
import com.cos.instagram.service.ImageService;
import com.cos.instagram.web.dto.ImageReqDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ImageController {

	private final ImageService imageService;
	
	@GetMapping({"", "/", "/image/feed"})
	public String feed(
			@LoginUserAnnotation LoginUser loginUser) {
		System.out.println("loginUser : "+loginUser);
		return "image/feed";
	}
	
	@GetMapping("/image/uploadForm")
	public String imageUploadForm() {
		return "image/image-upload";
	}
	
	@PostMapping("/image")
	public String imageUpload(
			@LoginUserAnnotation LoginUser loginUser,
			ImageReqDto imageReqDto) {
		
		imageService.사진업로드(imageReqDto, loginUser.getId());
		
		return "redirect:/";
	}
	
	@GetMapping("/image/explore")
	public String imageExplore() {
		return "image/explore";
	}
}




