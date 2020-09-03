package com.cos.instagram.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.instagram.config.auth.LoginUserAnnotation;
import com.cos.instagram.config.auth.dto.LoginUser;
import com.cos.instagram.domain.image.Image;
import com.cos.instagram.service.ImageService;
import com.cos.instagram.web.dto.ImageReqDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ImageController {

	private final ImageService imageService;
	
	@GetMapping({"", "/", "/image/feed"})
	public String feed(
			@LoginUserAnnotation LoginUser loginUser,
			Model model) {
		System.out.println("loginUser : "+loginUser);
		model.addAttribute("images", imageService.피드사진(loginUser.getId()));
		return "image/feed";
	}
	
	@GetMapping("/test/image/feed")
	public @ResponseBody List<Image> testFeed(
			@LoginUserAnnotation LoginUser loginUser) {
		return imageService.피드사진(loginUser.getId());
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
	public String imageExplore(@LoginUserAnnotation LoginUser loginUser, Model model) {
		model.addAttribute("images", imageService.인기사진(loginUser.getId()));
		return "image/explore";
		
	}
}




