package com.overWorkGathering.main.mapper;

import com.overWorkGathering.main.DTO.WorkDTO;
import com.overWorkGathering.main.entity.WorkEntity;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface WorkMapper {
    List<WorkDTO> toWorkDTOList(List<WorkEntity> workEntityList);
    WorkDTO toWorkDTO(WorkEntity workEntity);

    List<WorkEntity> toWorkEntityList(List<WorkDTO> workDTOList);
    WorkEntity toWorkEntity(WorkDTO workDTO);
}
