package com.overWorkGathering.main.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


@Embeddable
@MappedSuperclass
@EntityListeners({AuditListener.class})
public class Audit {

    @JsonIgnore
    @Column(name="CREATED_BY", nullable = false, length = 50, updatable = false)
    protected String createdBy;
    @JsonIgnore
    @Column(name="CREATED_DATE", nullable = false, updatable = false)
    protected LocalDateTime createdDate;
    @JsonIgnore
    @Column(name="LAST_MODIFIED_BY", nullable = false, length = 50)
    protected String lastModifiedBy;
    @JsonIgnore
    @Column(name="LAST_MODIFIED_DATE", nullable = false)
    protected LocalDateTime lastModifiedDate;




}
