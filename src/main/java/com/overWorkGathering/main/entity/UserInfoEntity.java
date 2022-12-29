package com.overWorkGathering.main.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Table(name = "tb_user_info")
public class UserInfoEntity {

	@Id
	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "PW")
	private String pw;

	@Column(name = "NAME")
	private String name;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "PART")
	private String part;

	@Column(name = "PARTLEADER")
	private String partleader;

	@Column(name = "AUTH")
	private String auth;

	@Column(name = "PHONE")
	private String phone;

	@Column(name = "SALT")
	private String salt;

	@Column(name = "ACNT")
	private String acnt;
}
