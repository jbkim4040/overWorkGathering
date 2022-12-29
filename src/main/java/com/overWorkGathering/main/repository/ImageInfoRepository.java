package com.overWorkGathering.main.repository;

import com.overWorkGathering.main.entity.ImageInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageInfoRepository extends JpaRepository<ImageInfoEntity, String> {

    List<ImageInfoEntity> findAllByPart(String part);

    List<ImageInfoEntity> findAllByUserId(String UserId);
}
