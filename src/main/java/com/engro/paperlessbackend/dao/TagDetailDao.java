package com.engro.paperlessbackend.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engro.paperlessbackend.entities.TagDetail;
import com.engro.paperlessbackend.entities.TemplateStructure;
import com.engro.paperlessbackend.repositories.TagDetailRepository;

@Component
public class TagDetailDao {

	private static Logger logger = LoggerFactory.getLogger(TagDetailDao.class);

	@Autowired
	TagDetailRepository tagDetailRepository;

	public TagDetail addNewTagDetail(TagDetail tagDetail) {
		TagDetail detail = null;
		try {
			detail = tagDetailRepository.save(tagDetail);
			logger.debug("[Successful]");
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
		}
		return detail;
	}

	public List<TagDetail> getTagDetailByTagId(String tagId) {
		List<TagDetail> tagDetails = null;
		try {
			tagDetails = tagDetailRepository.findByTagId(tagId);
			logger.debug("[Successful]");
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
		}
		return tagDetails;
	}

	public List<TagDetail> getTagDetailByTagIdAndDataType(String tagId, String tagDataType) {
		List<TagDetail> tagDetails = null;
		try {
			tagDetails = tagDetailRepository.findByTagIdAndTagDataType(tagId, tagDataType);
			logger.debug("[Successful]");
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
		}
		return tagDetails;
	}

	public List<TagDetail> getTagDetailByField(Integer fieldId) {
		List<TagDetail> tagDetails = null;
		try {
			TemplateStructure ts = new TemplateStructure();
			ts.setFieldId(fieldId);
			tagDetails = tagDetailRepository.findByTemplateField(ts);
			logger.debug("[Successful]");
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
		}
		return tagDetails;
	}

	public TagDetail updateTagDetail(TagDetail tagDetail) {
		TagDetail detail = null;
		try {
			detail = tagDetailRepository.save(tagDetail);
			logger.debug("[Successful]");
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
		}
		return detail;
	}

	public List<TagDetail> getTagDetailsByTemplateField(TemplateStructure templateField) {
		List<TagDetail> tagDetails = null;
		try {
			tagDetails = tagDetailRepository.findByTemplateField(templateField);
			logger.info("[SUCCESSFUL]");
		} catch (Exception e) {
			logger.error("[EXCEPTION] - [{}]", e);
		}
		return tagDetails;
	}

	public boolean deleteTagDetailsByTemplateField(TemplateStructure templateField) {

		try {
			tagDetailRepository.deleteByTemplateField(templateField);
			logger.info("[SUCCESSFUL]");
		} catch (Exception e) {
			logger.error("[EXCEPTION] - [{}]", e);
			return false;
		}
		return true;
	}

}
