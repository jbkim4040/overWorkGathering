package com.overWorkGathering.main.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PrssRsltDTO {
    private String prssYn;
    private String prssMsg;
}
