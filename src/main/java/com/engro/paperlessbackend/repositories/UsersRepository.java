package com.engro.paperlessbackend.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.engro.paperlessbackend.entities.Role;
import com.engro.paperlessbackend.entities.Section;
import com.engro.paperlessbackend.entities.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {

	@Query(value = "SELECT * from users WHERE status = '1' AND user_id IN (SELECT user_id_fk FROM tbl_user_group WHERE group_id_fk = ?)", nativeQuery = true)
	List<Users> getUsersByGroupId(Integer groupId);

	List<Users> findByStatusOrderByUserNameAsc(String userStatusActive);

	List<Users> findByIdAndPassword(String id, String password);

//	@Modifying(clearAutomatically = true)
//	@Query(value = "UPDATE users SET status = 0, date_inactive = ? WHERE department = ?", nativeQuery = true)
//	void deleteUsersByDepartmentId(Timestamp timestamp, Integer departmentId);

	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE users SET status = 0, date_inactive = ? WHERE section = ?", nativeQuery = true)
	void deleteUsersBySectionId(Timestamp timestamp, Integer sectionId);

	List<Users> findBySectionAndStatus(Section section, String userStatusActive);

//	List<Users> findByDepartmentAndStatus(Department department, String userStatusActive);

	List<Users> findByUserRole(Role role);

	List<Users> findByEmailAndPassword(String email, String password);

	List<Users> findByEmail(String email);

	List<Users> findByEmailAndIdNot(String email, String id);
}
