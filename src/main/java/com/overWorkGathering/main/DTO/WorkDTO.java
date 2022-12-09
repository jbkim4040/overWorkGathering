package com.overWorkGathering.main.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkDTO {
	private String userId;
	private String workDt;
	private String startTime;
	private String endTime;
	private String taxiReceiptImg;
	private String taxiPay;
	private String dinnerYn;
	private String taxiYn;
	private String remarks;
	
	@Builder
	public WorkDTO(
			String userId, 
			String workDt, 
			String startTime, 
			String endTime,
			String taxiReceiptImg,
			String taxiPay,
			String dinnerYn,
			String taxiYn,
			String remarks) {
		this.userId = userId;
		this.workDt = workDt;
		this.startTime = startTime;
		this.endTime = endTime;
		this.taxiReceiptImg = taxiReceiptImg;
		this.taxiPay = taxiPay;
		this.dinnerYn = dinnerYn;
		this.taxiYn = taxiYn;
		this.remarks = remarks;
	}
}
