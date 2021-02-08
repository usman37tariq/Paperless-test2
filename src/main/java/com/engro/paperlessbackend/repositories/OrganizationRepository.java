package com.engro.paperlessbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.engro.paperlessbackend.entities.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, String> {

}
