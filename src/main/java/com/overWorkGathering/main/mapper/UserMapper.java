package com.overWorkGathering.main.mapper;

import com.overWorkGathering.main.DTO.UserDTO;
import com.overWorkGathering.main.entity.UserEntity;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper {
    UserDTO toUserDTO (UserEntity userEntity);

    UserEntity toUserEntity (UserDTO userDTO);

	List<UserDTO> toUserDTOList(List<UserEntity> userEntityList);
}
