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
@Table(name = "tb_part_info")
public class PartInfoEntity {

	@Id
	@Column(name = "PART_CD")
	private String partCd;

	@Column(name = "PART_NM")
	private String partNm;

	@Column(name = "PART_LEADER")
	private String partLeader;
}
