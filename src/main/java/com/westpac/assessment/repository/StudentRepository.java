package com.westpac.assessment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.westpac.assessment.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("""
            select distinct s
            from Student s
            left join fetch s.books
            """)
    List<Student> findAllWithBooks();
}
