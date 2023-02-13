package com.overWorkGathering.main.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
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
    private String remarks;

    private String part;

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
            String taxiYn,
            String remarks,
            String part) {
        this.userId = userId;
        this.workDt = workDt;
        this.startTime = startTime;
        this.endTime = endTime;
        this.img = img;
        this.taxiPay = taxiPay;
        this.dinnerYn = dinnerYn;
        this.name = name;
        this.taxiYn = taxiYn;
        this.remarks = remarks;
        this.part = part;
    }
}
