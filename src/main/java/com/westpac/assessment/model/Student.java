package com.westpac.assessment.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
public class Student {
    @Id
    private Long id;
    private String name;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY) // default lazy
    @JsonManagedReference
    private List<Book> books;
}
