package com.overWorkGathering.main.repository;

import com.overWorkGathering.main.entity.UserInfoEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserInfoEntity, String> {
	UserInfoEntity findByUserIdAndPw(String userId, String pw);
	UserInfoEntity findByUserId(String userId);
	List<UserInfoEntity> findAllByPart(String part);
}
