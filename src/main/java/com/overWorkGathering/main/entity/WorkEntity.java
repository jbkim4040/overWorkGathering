package com.overWorkGathering.main.entity;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import com.overWorkGathering.main.DTO.UserDTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(DoublePK.class)
@Table(name = "TB_WORK_HIS")
public class WorkEntity {
	
	@Id
	@Column(name = "USER_ID")
	private String userId;
	
	@Id
	@Column(name = "WORK_DT")
	private String workDt;
	
	@Column(name = "START_TIME")
	private String startTime;
	
	@Column(name = "END_TIME")
	private String endTime;
	
	@Column(name = "IMG")
	private String img;
	
	@Column(name = "TAXI_PAY")
	private String taxiPay;
	
	@Column(name = "DINNER_YN")
	private String dinnerYn;
	
	@Column(name = "TAXI_YN")
	private String taxiYn;
	
}
