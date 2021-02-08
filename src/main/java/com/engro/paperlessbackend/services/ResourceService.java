package com.engro.paperlessbackend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engro.paperlessbackend.dao.ResourceDao;
import com.engro.paperlessbackend.entities.Resource;

@Component
public class ResourceService {

	@Autowired
	ResourceDao resourceDao;

	public List<Resource> getAllResources() {
		return resourceDao.getAllResources();
	}

}
