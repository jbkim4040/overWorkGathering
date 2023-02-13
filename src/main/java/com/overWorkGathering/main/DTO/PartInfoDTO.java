package com.overWorkGathering.main.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PartInfoDTO {

	private String partNm;
	private String partCd;
	private String partLeader;

	@Builder
	public PartInfoDTO(
			String partCd,
			String partNm,
			String partLeader) {
		this.partCd = partCd;
		this.partNm = partNm;
		this.partLeader = partLeader;
	}
}
