package com.cos.instagram.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.instagram.domain.image.Image;
import com.cos.instagram.domain.image.ImageRepository;
import com.cos.instagram.domain.like.Likes;
import com.cos.instagram.domain.tag.Tag;
import com.cos.instagram.domain.tag.TagRepository;
import com.cos.instagram.domain.user.User;
import com.cos.instagram.domain.user.UserRepository;
import com.cos.instagram.util.Utils;
import com.cos.instagram.web.dto.ImageReqDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {

	private final ImageRepository imageRepository;
	private final TagRepository tagRepository;
	private final UserRepository userRepository;
	
	@Transactional(readOnly = true)
	public List<Image> 피드사진(int loginUserId){
		List<Image> images = imageRepository.mFeeds(loginUserId);
		for (Image image : images) {
			image.setLikeCount(image.getLikes().size());
			for (Likes like : image.getLikes()) {
				if(like.getUser().getId() == loginUserId) {
					image.setLikeState(true);
				}
			}
		}
		return images;
	}
	
	@Transactional(readOnly = true)
	public List<Image> 인기사진(int loginUserId) {
		return imageRepository.mNonFollowImage(loginUserId);
	}
	
	
	@Value("${file.path}")
	private String uploadFolder;
	
	@Transactional
	public void 사진업로드(ImageReqDto imageReqDto, int userId) {
		User userEntity = userRepository.findById(userId).
				orElseThrow(null);
		
		UUID uuid = UUID.randomUUID();
		String imageFilename = 
				uuid+"_"+imageReqDto.getFile().getOriginalFilename();
		Path imageFilepath = Paths.get(uploadFolder+imageFilename);
		try {
			Files.write(imageFilepath, imageReqDto.getFile().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 1. Image 저장
		Image image = imageReqDto.toEntity(imageFilename, userEntity);
		Image imageEntity = imageRepository.save(image);
		
		// 2. Tag 저장
		List<String> tagNames = Utils.tagParse(imageReqDto.getTags());
		for (String name : tagNames) {
			Tag tag = Tag.builder()
					.image(imageEntity)
					.name(name)
					.build();
			tagRepository.save(tag);
		}
	}
}




