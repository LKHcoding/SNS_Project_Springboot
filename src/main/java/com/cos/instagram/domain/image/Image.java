package com.cos.instagram.domain.image;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import com.cos.instagram.domain.tag.Tag;
import com.cos.instagram.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String location;
	private String caption;
	private String imageUrl;
	
	// Image를 select하면 한명의 User가 딸려옴.
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="userId") // 칼럼명
	private User user; // 타입은 User오브젝트의 PK의 타입
	
	// Image를 select하면 여러개의 Tag가 딸려옴.
	@OneToMany(mappedBy = "image", fetch = FetchType.LAZY) //연관관계 주인의 변수명을 적는다.
	@JsonIgnoreProperties({"image"}) //Jackson한테 내리는 명령
	private List<Tag> tags;
	
	@CreationTimestamp
	private Timestamp createDate;
}





