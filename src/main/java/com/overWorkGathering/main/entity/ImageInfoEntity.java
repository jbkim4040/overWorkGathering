package com.overWorkGathering.main.entity;

import lombok.*;
import org.apache.commons.net.ntp.TimeStamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Table(name = "tb_user_info")
public class ImageInfoEntity {

    @Id
    @Column(name = "IMAGE_ID")
    private String imageId;

    @Column(name = "ORG_IMAGE_NAME")
    private String orgImageName;

    @Column(name = "ORG_IMAGE_EXT")
    private String orgImageExt;

    @Column(name = "WORK_DT")
    private String workDt;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "PART")
    private String part;

    @Column(name = "CREATE_DTTM")
    private TimeStamp createDttm;
}
