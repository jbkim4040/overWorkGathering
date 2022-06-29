package com.overWorkGathering.main.utils;

import javax.persistence.PrePersist;
import java.time.LocalDateTime;

public class AuditListener {

    @PrePersist
    public <T extends Audit> void setCreatedOn(T entity){
        String operatorId = "";
        String windowId = "";
        LocalDateTime now = LocalDateTime.now();


    }
}
