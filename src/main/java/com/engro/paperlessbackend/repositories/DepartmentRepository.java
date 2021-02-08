package com.engro.paperlessbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.engro.paperlessbackend.entities.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

	@Query(value = "SELECT * FROM (SELECT * FROM tbl_department WHERE department_id != ?)temp WHERE department_name = ? AND is_deleted = 0", nativeQuery = true)
	List<Department> isDepartmentNameExists(int id, String name);

	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE tbl_department SET is_deleted = 1 WHERE department_id = ?", nativeQuery = true)
	void deleteByDepartmentId(Integer departmentId);

	List<Department> findByIsDeletedOrderByNameAsc(Integer departmentIsNotDeleted);

}
