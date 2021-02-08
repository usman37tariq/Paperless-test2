package com.engro.paperlessbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.engro.paperlessbackend.entities.Resource;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Integer> {

}
