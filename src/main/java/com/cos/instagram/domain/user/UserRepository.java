package com.cos.instagram.domain.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// JpaRepository가 extends 되면 @Repository 필요 없음. IoC자동으로 됨.
public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByUsername(String username);
	
	@Query(value = "select u.*, (select true from follow where fromUserId = ?2 and toUserId = u.id) as matpal from follow f inner join user u on f.toUserId = u.id and f.fromUserId = ?1", nativeQuery = true)
	List<User> mFollowingUser(int pageUserId, int loginUserId);
	
	@Query(value = "select u.*,(select true from follow where fromUserId = ?2 and toUserId = u.id) as matpal from follow f inner join user u on f.fromUserId = u.id and f.toUserId = ?1", nativeQuery = true)
	List<User> mFollowerUser(int pageUserId, int loginUserId);
}
