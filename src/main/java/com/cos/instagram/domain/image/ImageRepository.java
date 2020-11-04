package com.cos.instagram.domain.image;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepository extends JpaRepository<Image, Integer> {

	List<Image> findByUserId(int userId);

	// 내가 팔로우 하지 않은 사람들의 이미지들(최대 20개)
	@Query(value = "select * from image where userId in (select id from user where id != ?1 and id not in (select toUserId from follow where fromUserId = ?1)) limit 20", nativeQuery = true)
	List<Image> mNonFollowImage(int loginUserId);

	// feed 화면에 띄울 게시물 검색
	@Query(value = "select * from image where userId = ?1 or userId in (select toUserId from follow where fromUserId = ?1) order by createDate desc", nativeQuery = true)
	List<Image> mFeeds(int loginUserId);

	// 팔로우한사람이 없으면 전체게시글을 모두 보여주기위한 쿼리
	@Query(value = "select * from image order by createDate desc", nativeQuery = true)
	List<Image> mAllFeeds(int loginUserId);

	// 태그로 검색하는 부분
	@Query(value = "select * from image where id in (select imageId from tag where name=?1)", nativeQuery = true)
	List<Image> mFeeds(String tag);

	// 단독 게시물 조회하는 부분
	@Query(value = "select * from image where id = ?1", nativeQuery = true)
	List<Image> mBoardImage(int imageId);

}
