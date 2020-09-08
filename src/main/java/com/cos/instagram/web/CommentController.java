package com.cos.instagram.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.instagram.service.CommentService;
import com.cos.instagram.web.dto.CommentRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class CommentController {

	private final CommentService commentService;
	
	@PostMapping("/comment")
	public ResponseEntity<?> comment(CommentRespDto commentRespDto) {
		System.out.println("commentRespDto : "+commentRespDto);
		commentService.댓글쓰기(commentRespDto);
		return new ResponseEntity<String>("ok", HttpStatus.CREATED);
	}
	
	@DeleteMapping("/comment/{id}")
	public ResponseEntity<?> commentDelete(@PathVariable int id) {
		commentService.댓글삭제(id);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
}



