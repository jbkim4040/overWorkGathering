package com.overWorkGathering.main.mapper;

import com.overWorkGathering.main.DTO.ImageInfoDTO;
import com.overWorkGathering.main.entity.ImageInfoEntity;

public interface ImageInfoMapper {

    ImageInfoEntity toImageEntity (ImageInfoDTO imageInfoDTO);

    ImageInfoDTO toImageInfoDTO (ImageInfoEntity imageInfoEntity);
}
