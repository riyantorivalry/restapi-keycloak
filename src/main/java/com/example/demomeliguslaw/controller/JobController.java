package com.example.demomeliguslaw.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demomeliguslaw.entity.Job;
import com.example.demomeliguslaw.service.JobService;


@RestController
@RequestMapping("/jobs")
public class JobController {
	@Autowired
	private JobService jobService;

	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Object> create(@RequestBody Job job, Principal principal){
		Job entity = jobService.create(job);

		return new ResponseEntity<>(entity, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> getAll(@RequestParam (value = "sort", required = false) String sort,@RequestParam (value = "orderby", required = false) String orderby,Principal principal){
		List<Job> entities = jobService.getAll(sort, orderby);
		if(null==entities) {
			return new ResponseEntity<>(entities, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(entities, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> create(@PathVariable("id") Integer id, Principal principal){
		Job entity = jobService.getById(id);
		if(null == entity) {
			return  new ResponseEntity<>(entity, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(entity, HttpStatus.OK);
	}

}
