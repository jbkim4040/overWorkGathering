package com.overWorkGathering.main.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(DoublePK.class)
@Table(name = "tb_work_his")
public class WorkHisEntity {
	
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
	
	@Column(name = "IMAGE_ID")
	private String imageId;

	@Column(name = "TAXI_PAY")
	@ColumnDefault("0")
	private int taxiPay;
	
	@Column(name = "DINNER_YN")
	private String dinnerYn;
	
	@Column(name = "TAXI_YN")
	private String taxiYn;

	@Column(name = "REMARKS")
	private String remarks;
	
}
