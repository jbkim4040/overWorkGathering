package com.overWorkGathering.main.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.net.ntp.TimeStamp;

@Data
@Builder
@AllArgsConstructor
public class ImageInfoDTO {

    private String imageId;
    private String orgImageName;
    private String orgImageExt;
    private String workDt;
    private String userId;
    private String part;
    private TimeStamp createDttm;

}
