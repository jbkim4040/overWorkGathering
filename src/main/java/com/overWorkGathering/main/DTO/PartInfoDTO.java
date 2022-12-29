package com.overWorkGathering.main.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PartInfoDTO {

	private String part;
	private String partLeader;

	@Builder
	public PartInfoDTO(
			String part,
			String partLeader) {
		this.part = part;
		this.partLeader = partLeader;
	}
}
