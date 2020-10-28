package com.cos.instagram.domain.user;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

// JpaRepository가 extends 되면 @Repository 필요 없음. IoC자동으로 됨.
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByUsername(String username);

	@Query(value = "select u.*, (select true from follow where fromUserId = ?2 and toUserId = u.id) as matpal from follow f inner join user u on f.toUserId = u.id and f.fromUserId = ?1", nativeQuery = true)
	List<User> mFollowingUser(int pageUserId, int loginUserId);

	@Query(value = "select u.*,(select true from follow where fromUserId = ?2 and toUserId = u.id) as matpal from follow f inner join user u on f.fromUserId = u.id and f.toUserId = ?1", nativeQuery = true)
	List<User> mFollowerUser(int pageUserId, int loginUserId);

	// 사이드 랜덤 추천
	@Query(value = "select * from user where id not in(?1) order by rand() limit  5", nativeQuery = true)
	List<User> mRecommendationImage(int loginUserId);

	// 회원가입시 이메일, username 중복체크하는 부분
	@Query(value = "select * from user where email = ?1 or username = ?2", nativeQuery = true)
	List<User> 중복체크(String email, String username);

	// 비밀번호 변경
	@Modifying(clearAutomatically = true)
	@Transactional  // delete 사용시
	@Query(value = "update user set password = ?2 where username = ?1", nativeQuery = true)
	int modifyPassword(String username, String password);

}
