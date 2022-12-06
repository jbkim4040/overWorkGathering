package com.overWorkGathering.main.mapper;

import com.overWorkGathering.main.DTO.WorkDTO;
import com.overWorkGathering.main.entity.WorkHisEntity;

//import org.apache.ibatis.annotations.Mapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkMapper {
    List<WorkDTO> toWorkDTOList(List<WorkHisEntity> workHisEntityList);
    WorkDTO toWorkDTO(WorkHisEntity workHisEntity);

    List<WorkHisEntity> toWorkEntityList(List<WorkDTO> workDTOList);
    WorkHisEntity toWorkEntity(WorkDTO workDTO);
}
