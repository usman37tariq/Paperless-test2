package com.engro.paperlessbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.engro.paperlessbackend.entities.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

	@Query(value = "SELECT * FROM (SELECT * FROM tbl_group WHERE group_id != ?)temp WHERE group_name = ?", nativeQuery = true)
	List<Group> isGroupNameAleardyExists(Integer id, String name);

	@Query(value = "SELECT * FROM tbl_group WHERE group_id IN (SELECT group_id_fk FROM tbl_user_group WHERE user_id_fk = ?)", nativeQuery = true)
	List<Group> getGroupsByUserId(String userId);

	List<Group> findAllByOrderByNameAsc();
}
