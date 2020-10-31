package com.cos.instagram.domain.chatting;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
	@Id
	@GeneratedValue
	private Long id;

	private Long projectId;

	// @ManyToOne
	// @JoinColumn(name ="account_id")
	// private Account sender;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String sender;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String message;

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime sendDateTime;
}