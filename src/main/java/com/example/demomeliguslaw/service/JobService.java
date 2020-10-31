package com.example.demomeliguslaw.service;

import java.util.List;

import com.example.demomeliguslaw.entity.Job;

public interface JobService {
	Job getById(Integer id);
	Job create(Job job);
	List<Job> getAll(String sort, String orderby);
}
