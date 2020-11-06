package com.cos.instagram.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.instagram.config.hanlder.ex.MyImageDeleteException;
import com.cos.instagram.domain.comment.Comment;
import com.cos.instagram.domain.follow.Follow;
import com.cos.instagram.domain.follow.FollowRepository;
import com.cos.instagram.domain.image.Image;
import com.cos.instagram.domain.image.ImageRepository;
import com.cos.instagram.domain.like.Likes;
import com.cos.instagram.domain.tag.Tag;
import com.cos.instagram.domain.tag.TagRepository;
import com.cos.instagram.domain.user.User;
import com.cos.instagram.domain.user.UserRepository;
import com.cos.instagram.util.Utils;
import com.cos.instagram.web.dto.ImageReqDto;
import com.cos.instagram.web.dto.UserProfileImageRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {
	@PersistenceContext
	private EntityManager em;
	private final ImageRepository imageRepository;
	private final TagRepository tagRepository;
	private final UserRepository userRepository;
	private final FollowRepository followRepository;

	@Transactional(readOnly = true)
	public List<Image> 피드사진(int loginUserId, String tag) {
		List<Image> images = null;
		if (tag == null || tag.equals("")) {
			images = imageRepository.mFeeds(loginUserId);
			if (images.size() == 0) {
				images = imageRepository.mAllFeeds(loginUserId);
			}
		} else {
			tag="%"+tag+"%";
			images = imageRepository.mFeeds(tag);
		}

		for (Image image : images) {
			image.setLikeCount(image.getLikes().size());

			// 좋아요 상태 여부 등록
			for (Likes like : image.getLikes()) {
				if (like.getUser().getId() == loginUserId) {
					image.setLikeState(true);
				}
			}
			// 댓글 주인 여부 등록
			for (Comment comment : image.getComments()) {
				if (comment.getUser().getId() == loginUserId) {
					comment.setCommentHost(true);
				}
			}
		}

		return images;
	}

	@Transactional
	public void 게시물삭제(int imageid, int ImageUserId, int loginuserid) throws MyImageDeleteException {

		if (ImageUserId != loginuserid) {
			throw new MyImageDeleteException("게시물 작성자만 글을 지울 수 있습니다.");
		} else {
			imageRepository.deleteById(imageid);
		}
	}

	@Transactional(readOnly = true)
	public List<Image> 단독게시물(int loginUserId, int imageId) {
		List<Image> boards = null;
		boards = imageRepository.mBoardImage(imageId);

		for (Image board : boards) {
			board.setLikeCount(board.getLikes().size());

			// 좋아요 상태 여부 등록
			for (Likes like : board.getLikes()) {
				if (like.getUser().getId() == loginUserId) {
					board.setLikeState(true);
				}
			}
			// 댓글 주인 여부 등록
			for (Comment comment : board.getComments()) {
				if (comment.getUser().getId() == loginUserId) {
					comment.setCommentHost(true);
				}
			}
		}
		return boards;
	}

	@Transactional(readOnly = true)
	public List<UserProfileImageRespDto> 인기사진(int loginUserId) {

		// LKH 나말고 다른 유저들의 ImageRespDto 정보 받아오기
		StringBuilder sb = new StringBuilder();
		sb.append("select im.id, im.imageUrl, im.userId, ");
		sb.append("(select count(*) from likes lk where lk.imageId = im.id) as likeCount, ");
		sb.append("(select count(*) from comment ct where ct.imageId = im.id) as commentCount ");
		sb.append("from image im where im.userId != ? ");
		String q = sb.toString();
		Query query = em.createNativeQuery(q, "UserProfileImageRespDtoMapping").setParameter(1, loginUserId);
		List<UserProfileImageRespDto> imagesEntity = query.getResultList();

		// LKH 내가 팔로우한 유저 정보 받아오기
		List<Follow> LoginUserFollowingList = followRepository.findByfromUserId(loginUserId);

		// LKH 내가 팔로우 한 유저인지 아닌지 찾아서 해당 부분을 삭제함
		for (Follow asdf : LoginUserFollowingList) {
			for (int i = 0; i < imagesEntity.size(); i++) {
				if (imagesEntity.get(i).getUserId() == asdf.getToUser().getId()) {
					imagesEntity.remove(i);
				}
			}
		}

		return imagesEntity;
		/* return imageRepository.mNonFollowImage(loginUserId); */
	}

	@Value("${file.path}")
	private String uploadFolder;

	@Transactional
	public void 사진업로드(ImageReqDto imageReqDto, int userId) {
		User userEntity = userRepository.findById(userId).orElseThrow(null);

		UUID uuid = UUID.randomUUID();
		String imageFilename = uuid + "_" + imageReqDto.getFile().getOriginalFilename();
		Path imageFilepath = Paths.get(uploadFolder + imageFilename);
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
			Tag tag = Tag.builder().image(imageEntity).name(name).build();
			tagRepository.save(tag);
		}
	}
}
