package com.overWorkGathering.main.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@IdClass(DoublePK.class)
@Table(name = "WORK")
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
