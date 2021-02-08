package com.engro.paperlessbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.engro.paperlessbackend.entities.TagDetail;
import com.engro.paperlessbackend.entities.TemplateStructure;

@Repository
public interface TagDetailRepository extends JpaRepository<TagDetail, String> {

	List<TagDetail> findByTagId(String tagId);

	List<TagDetail> findByTagIdAndTagDataType(String tagId, String tagDataType);

	List<TagDetail> findByTemplateField(TemplateStructure templateStructure);

	@Modifying(clearAutomatically = true)
	void deleteByTemplateField(TemplateStructure templateField);
}
