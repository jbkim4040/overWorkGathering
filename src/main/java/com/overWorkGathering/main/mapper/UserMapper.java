package com.overWorkGathering.main.mapper;

import com.overWorkGathering.main.DTO.UserDTO;
import com.overWorkGathering.main.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserDTO toUserDTO (UserEntity userEntity);

    UserEntity toUserEntity (UserDTO userDTO);
}
