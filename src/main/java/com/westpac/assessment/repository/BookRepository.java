package com.westpac.assessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.westpac.assessment.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
