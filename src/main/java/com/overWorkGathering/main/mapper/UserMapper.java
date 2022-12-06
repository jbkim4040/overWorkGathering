package com.overWorkGathering.main.mapper;

import com.overWorkGathering.main.DTO.UserDTO;
import com.overWorkGathering.main.entity.UserInfoEntity;

import java.util.List;

//import org.apache.ibatis.annotations.Mapper;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toUserDTO (UserInfoEntity userInfoEntity);

    UserInfoEntity toUserEntity (UserDTO userDTO);

	List<UserDTO> toUserDTOList(List<UserInfoEntity> userInfoEntityList);
}
