package com.cos.instagram.service;

import java.util.function.Supplier;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.instagram.domain.image.Image;
import com.cos.instagram.domain.image.ImageRepository;
import com.cos.instagram.domain.like.Likes;
import com.cos.instagram.domain.like.LikesRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LikesService {
	private final LikesRepository likesRepository;

	@Transactional
	public void 좋아요(int imageId, int loginUserId) {
		likesRepository.mSave(imageId, loginUserId);
	}
	
	@Transactional
	public void 싫어요(int imageId, int loginUserId) {
		likesRepository.mDelete(imageId, loginUserId);
	}
}
