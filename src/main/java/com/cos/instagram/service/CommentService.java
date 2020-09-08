package com.cos.instagram.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.instagram.domain.comment.CommentRepository;
import com.cos.instagram.web.dto.CommentRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {
	private final CommentRepository commentRepository;
	
	@Transactional
	public void 댓글쓰기(CommentRespDto commentRespDto) {
		commentRepository.mSave( 
				commentRespDto.getUserId(), 
				commentRespDto.getImageId(), 
				commentRespDto.getContent());
	}
}
