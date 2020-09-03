package com.cos.instagram.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LikesRepository extends JpaRepository<Likes, Integer>{

	@Modifying
	@Query(value = "INSERT INTO likes(imageId, userId) VALUES(?1, ?2)", nativeQuery = true)
	int mSave(int imageId, int loginUserId);
	
	@Modifying
	@Query(value = "DELETE FROM likes WHERE imageId = ?1 AND userId = ?2", nativeQuery = true)
	int mDelete(int imageId, int loginUserId);
}
