package com.westpac.assessment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
public class Book {
    @Id
    private Long id;
    private String title;

    @ManyToOne
    @JsonBackReference
    private Student student;
}
