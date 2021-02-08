package com.engro.paperlessbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.engro.paperlessbackend.entities.Role;
import com.engro.paperlessbackend.entities.RoleResource;
import com.engro.paperlessbackend.entityid.RoleResourceId;

@Repository
public interface RoleResourceRepository extends JpaRepository<RoleResource, RoleResourceId> {

	List<RoleResource> findByRole(Role role);

	@Modifying(clearAutomatically = true)
	void deleteByRole(Role role);

}
