package com.cos.instagram.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.cos.instagram.config.auth.PrincipalDetails;

@Controller
public class ImageController {

	@GetMapping({"", "/", "/image/feed"})
	public String feed(@AuthenticationPrincipal PrincipalDetails principal) {
		System.out.println(principal.getUser());
		return "image/feed";
	}
}
