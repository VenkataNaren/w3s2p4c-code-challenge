package com.westpac.assessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.westpac.assessment.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
