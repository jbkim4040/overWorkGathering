package com.overWorkGathering.main.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkCollectionDtlReqDTO {
    private String userId;
    private String workDt;
    private String startTime;
    private String endTime;
    private String img;
    private int taxiPay;
    private String dinnerYn;
    private String taxiYn;
    private String name;

    @Builder
    public WorkCollectionDtlReqDTO(
            String userId,
            String workDt,
            String startTime,
            String endTime,
            String img,
            int taxiPay,
            String dinnerYn,
            String name,
            String taxiYn) {
        this.userId = userId;
        this.workDt = workDt;
        this.startTime = startTime;
        this.endTime = endTime;
        this.img = img;
        this.taxiPay = taxiPay;
        this.dinnerYn = dinnerYn;
        this.name = name;
        this.taxiYn = taxiYn;
    }
}
