package com.overWorkGathering.main.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "PERSISTENT_LOGIN")
public class PersistentLoginsEntity {
    @Id
    private String series;
    private String username;
    private String token;
    private Date lastUsed;

}
