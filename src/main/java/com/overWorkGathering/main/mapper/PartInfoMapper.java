package com.overWorkGathering.main.mapper;

import com.overWorkGathering.main.DTO.PartInfoDTO;
import com.overWorkGathering.main.entity.PartInfoEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PartInfoMapper {
    PartInfoEntity toPartInfoEntity(PartInfoDTO partInfoDTO);
    PartInfoDTO toPartInfoDTO(PartInfoEntity partInfoEntity);

    List<PartInfoEntity> toPartINfoEntityList(List<PartInfoDTO> partInfoDTOList);
    List<PartInfoDTO> toPartINfoDTOList(List<PartInfoEntity> partInfoEntityList);
}
