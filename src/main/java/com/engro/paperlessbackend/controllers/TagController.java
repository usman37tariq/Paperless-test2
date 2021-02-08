package com.engro.paperlessbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.engro.paperlessbackend.dao.TagDao;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/")
public class TagController {
	
	@Autowired TagDao tagDao;
	
	
	@ApiOperation(value = "Create Test Tag Numeric Type", tags = "Tag")
	@RequestMapping(method = RequestMethod.POST, value = "createTestTableForTagNumeric")
	public Object createTestTagNumeric(@RequestBody String tableName) {
		return tagDao.createTagTable(tableName, TagDao.TAG_TYPE_NUMERIC);
	}
	
	@ApiOperation(value = "Create Test Tag Text Type", tags = "Tag")
	@RequestMapping(method = RequestMethod.POST, value = "createTestTableForTagText")
	public Object createTestTagText(@RequestBody String tableName) {
		return tagDao.createTagTable(tableName, TagDao.TAG_TYPE_TEXT);
	}
}
