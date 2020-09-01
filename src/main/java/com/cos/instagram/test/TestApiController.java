package com.cos.instagram.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.instagram.domain.follow.Follow;
import com.cos.instagram.domain.follow.FollowRepository;
import com.cos.instagram.domain.image.Image;
import com.cos.instagram.domain.image.ImageRepository;
import com.cos.instagram.domain.like.Likes;
import com.cos.instagram.domain.like.LikesRepository;
import com.cos.instagram.domain.tag.Tag;
import com.cos.instagram.domain.tag.TagRepository;
import com.cos.instagram.domain.user.User;
import com.cos.instagram.domain.user.UserRepository;
import com.cos.instagram.domain.user.UserRole;

@RestController
public class TestApiController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private TagRepository tagRepository;
	
	@Autowired
	private FollowRepository followRepository;
	
	@Autowired
	private LikesRepository likeRepository;
	
	@PostMapping("/test/api/join")
	public User join(@RequestBody User user) {
		System.out.println("/test/api/join");
		user.setRole(UserRole.USER); // USER
		
		User userEntity = userRepository.save(user);
		return userEntity;
	}
	
	@PostMapping("/test/api/image/{caption}")
	public String image(@PathVariable String caption) {
		User userEntity = userRepository.findById(1).get();
		
		Image image = Image.builder()
				.location("외국")
				.caption(caption)
				.user(userEntity)
				.build();
		
		Image imageEntity = imageRepository.save(image);
		
		List<Tag> tags = new ArrayList<>();
		Tag tag1 = Tag.builder()
				.name("#외국")
				.image(imageEntity)
				.build();
		Tag tag2 = Tag.builder()
				.name("#여행")
				.image(imageEntity)
				.build();
		tags.add(tag1);
		tags.add(tag2);
		
		tagRepository.saveAll(tags);
		
		return "Image Insert 잘됨";
	}
	
	@GetMapping("/test/api/image/list")
	public List<Image> imageList(){
		return imageRepository.findAll();
	}
	
	@GetMapping("/test/api/tag/list")
	public List<Tag> tagList(){
		return tagRepository.findAll();
	}
	
	@GetMapping("/test/api/follow/{fromUserId}/{toUserId}")
	public String follow(
			@PathVariable int fromUserId, @PathVariable int toUserId) {
		
		User fromUserEntity = userRepository.findById(fromUserId).get();
		User toUserEntity = userRepository.findById(toUserId).get();
		
		Follow follow = Follow.builder()
				.fromUser(fromUserEntity)
				.toUser(toUserEntity)
				.build();
		followRepository.save(follow);
		// http://localhost:8080/test/api/follow/1/2
		return fromUserEntity.getUsername()+"이 "
		+toUserEntity.getUsername()+"을 팔로우 하였습니다.";
	}
	
	@GetMapping("/test/api/image/{imageId}/like")
	public String imageLike(@PathVariable int imageId) {
		Image imageEntity = imageRepository.findById(imageId).get();
		User userEntity = userRepository.findById(1).get();
		Likes like = Likes.builder()
				.image(imageEntity)
				.user(userEntity)
				.build();
		likeRepository.save(like);
		return "좋아요 완료";
	}
}



