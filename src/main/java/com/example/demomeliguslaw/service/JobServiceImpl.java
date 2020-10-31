package com.example.demomeliguslaw.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demomeliguslaw.entity.Job;
import com.example.demomeliguslaw.repository.JobRepository;

@Service
public class JobServiceImpl implements JobService{
	@Autowired
	private JobRepository jobRepository;

	@Override
	public Job getById(Integer id) {
		// TODO Auto-generated method stub

		return jobRepository.findById(id).get();
	}

	@Override
	public Job create(Job job) {
		Job entity = jobRepository.save(job);

		return entity;
	}

	@Override
	public List<Job> getAll(String sort, String orderby) {
		List<Job> data = new ArrayList<>();
		if(StringUtils.isEmpty(sort)&&StringUtils.isEmpty(orderby)) {
			data = jobRepository.findAll();
		}else {
			if(sort.equalsIgnoreCase("desc")) {
				if(orderby.equalsIgnoreCase("role")) {
					data = jobRepository.findByOrderByRoleDesc();
				}else if(orderby.equalsIgnoreCase("salary")) {
					data = jobRepository.findByOrderBySalaryDesc();
				}
			}else if(sort.equalsIgnoreCase("asc")){
				if(orderby.equalsIgnoreCase("role")) {
					data = jobRepository.findByOrderByRoleAsc();
				}else if(orderby.equalsIgnoreCase("salary")) {
					data = jobRepository.findByOrderBySalaryAsc();
				}
			}
		}
		return data;
	}
}
