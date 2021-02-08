package com.engro.paperlessbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.engro.paperlessbackend.entities.Group;
import com.engro.paperlessbackend.entities.UserGroup;
import com.engro.paperlessbackend.entities.Users;
import com.engro.paperlessbackend.entityid.UserGroupId;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, UserGroupId> {

	@Modifying(clearAutomatically = true)
	void deleteByGroupId(Integer groupId);

	@Modifying(clearAutomatically = true)
	void deleteByUserId(String userId);

	List<UserGroup> findByUser(Users user);

	List<UserGroup> findByGroup(Group group);
}
