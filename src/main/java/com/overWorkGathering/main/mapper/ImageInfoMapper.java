package com.overWorkGathering.main.mapper;

import com.overWorkGathering.main.DTO.ImageInfoDTO;
import com.overWorkGathering.main.entity.ImageInfoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageInfoMapper {

    ImageInfoEntity toImageEntity (ImageInfoDTO imageInfoDTO);

    ImageInfoDTO toImageInfoDTO (ImageInfoEntity imageInfoEntity);
}
