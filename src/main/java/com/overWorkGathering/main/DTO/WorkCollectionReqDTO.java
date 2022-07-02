package com.overWorkGathering.main.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkCollectionReqDTO {
	
	private String workDt;
	private String dinnerPay;
	private String taxiPay;
	private String other;
	
	@Builder
	public WorkCollectionReqDTO(
			String workDt, 
			String dinnerPay, 
			String taxiPay, 
			String other) {
		this.workDt = workDt;
		this.dinnerPay = dinnerPay;
		this.taxiPay = taxiPay;
		this.other = other;

	}
}
