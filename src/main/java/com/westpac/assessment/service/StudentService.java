package com.westpac.assessment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.westpac.assessment.model.Student;
import com.westpac.assessment.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository repository;

    public List<Student> getStudents() {
        return repository.findAllWithBooks();
    }
}
