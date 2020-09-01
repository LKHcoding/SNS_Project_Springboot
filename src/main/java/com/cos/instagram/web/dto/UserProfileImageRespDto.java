package com.cos.instagram.web.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.ManyToOne;

import com.cos.instagram.domain.tag.Tag;
import com.cos.instagram.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileImageRespDto {
	private int id;
	private String imageUrl;
	private int likeCount;
	private int commentCount;
}
