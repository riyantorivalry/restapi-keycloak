package com.example.demomeliguslaw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demomeliguslaw.entity.Job;

public interface JobRepository extends JpaRepository<Job, Integer> {

	//	List<Job> findAllSortRole(@Param("sort") String sort);

	//	@Query(value = "FROM JOB ORDER BY :orderby desc")
	//	List<Job> findAllDesc(@Param("orderby") String orderby);
	//	@Query(value = "FROM JOB ORDER BY :orderby asc")
	//	List<Job> findAllAsc(@Param("orderby") String orderby);

	List<Job> findByOrderByRoleAsc();
	List<Job> findByOrderByRoleDesc();
	List<Job> findByOrderBySalaryAsc();
	List<Job> findByOrderBySalaryDesc();
}
