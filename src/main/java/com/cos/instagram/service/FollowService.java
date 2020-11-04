package com.cos.instagram.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.instagram.domain.follow.FollowRepository;
import com.cos.instagram.domain.noti.NotiRepository;
import com.cos.instagram.domain.noti.NotiType;
import com.cos.instagram.web.dto.FollowRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FollowService {
	
	@PersistenceContext
	private EntityManager em;
	private final FollowRepository followRepository;
	private final NotiRepository notiRepository;
	
	public List<FollowRespDto> 팔로잉리스트(int loginUserId, int pageUserId){
		// 첫번째 물음표 loginUserId, 두번째 물음표 pageUserId
		StringBuilder sb = new StringBuilder();
		sb.append("select u.id,u.username,u.name,u.profileImage, ");
		sb.append("if(u.id = ?, true, false) equalUserState,");
		sb.append("if((select true from follow where fromUserId = ? and toUserId = u.id), true, false) as followState ");
		sb.append("from follow f inner join user u on f.toUserId = u.id ");
		sb.append("and f.fromUserId = ? ");
		sb.append("and u.id != ? ");
		String q = sb.toString();
		System.out.println("팔로잉리스트 : "+q);
		Query query = em.createNativeQuery(q, "FollowRespDtoMapping")
				.setParameter(1, loginUserId)
				.setParameter(2, loginUserId)
				.setParameter(3, pageUserId)
				.setParameter(4, loginUserId);
		List<FollowRespDto> followListEntity = query.getResultList();
		return followListEntity;
	}
	
	public List<FollowRespDto> 팔로워리스트(int loginUserId, int pageUserId){
		// 첫번째 물음표 loginUserId, 두번째 물음표 pageUserId
		StringBuilder sb = new StringBuilder();
		sb.append("select u.id,u.username,u.name,u.profileImage, ");
		sb.append("if(u.id = ?, true, false) equalUserState,");
		sb.append("if((select true from follow where fromUserId = ? and toUserId = u.id), true, false) as followState ");
		sb.append("from follow f inner join user u on f.fromUserId = u.id ");
		sb.append("and f.toUserId = ? ");
		sb.append("and u.id != ? ");
		String q = sb.toString();
		
		Query query = em.createNativeQuery(q, "FollowRespDtoMapping")
				.setParameter(1, loginUserId)
				.setParameter(2, loginUserId)
				.setParameter(3, pageUserId)
				.setParameter(4, loginUserId);		
		List<FollowRespDto> followerListEntity = query.getResultList();
		return followerListEntity;
	}
	
	// 서비스단에서 롤백하려면 throw를 runtimeException으로 던져야됨.
	@Transactional
	public void 팔로우(int loginUserId, int pageUserId) {
		
		int result = followRepository.mFollow(loginUserId, pageUserId);
		
		notiRepository.mSave(loginUserId, pageUserId, NotiType.FOLLOW.name());
		System.out.println("팔로우 result : "+result);
	}
	
	@Transactional
	public void 팔로우취소(int loginUserId, int pageUserId) {	
		int result = followRepository.mUnFollow(loginUserId, pageUserId);
		System.out.println("팔로우취소 result : "+result);
	}
}
