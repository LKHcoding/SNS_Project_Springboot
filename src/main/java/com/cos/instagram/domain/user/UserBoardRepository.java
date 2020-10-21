package com.cos.instagram.domain.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cos.instagram.domain.image.Image;

public interface UserBoardRepository extends JpaRepository<Image, Integer> {
	// 특정유저 게시물 조회하는 부분
	@Query(value = "select * from image where userId = ?1", nativeQuery = true)
	List<Image> mUserBoard(int BoardUserid);
}
