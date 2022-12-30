package com.overWorkGathering.main.repository;

import com.overWorkGathering.main.entity.PartInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartInfoRepository  extends JpaRepository<PartInfoEntity, String> {
    PartInfoEntity findByPart(String part);
}
