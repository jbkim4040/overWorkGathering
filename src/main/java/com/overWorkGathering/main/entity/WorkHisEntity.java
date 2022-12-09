package com.overWorkGathering.main.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(DoublePK.class)
@Table(name = "TB_WORK_HIS")
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
	
	@Column(name = "TAXI_RECEIPT_IMG")
	private String taxiReceiptImg;
	
	@Column(name = "TAXI_PAY")
	private String taxiPay;
	
	@Column(name = "DINNER_YN")
	private String dinnerYn;
	
	@Column(name = "TAXI_YN")
	private String taxiYn;

	@Column(name = "REMARKS")
	private String remarks;
	
}
