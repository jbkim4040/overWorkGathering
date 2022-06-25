package com.overWorkGathering.main.repository;

import com.overWorkGathering.main.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
	UserEntity findByUserIdAndPw(String userId, String pw);
	UserEntity findByUserId(String userId);
}
