package com.westpac.assessment.model;

import java.util.Comparator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

@Entity
@Data
public class Employee implements Comparable<Employee> {
    @Id
    @SequenceGenerator(
            name = "employee_id_seq",
            sequenceName = "employee_id_sequence",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_id_seq")
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    private String department;

    @Override
    public int compareTo(Employee other) {
        if (other == null) {
            return 1;
        }
        return Comparator
                .comparing(Employee::getLastName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER))
                .thenComparing(Employee::getFirstName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER))
                .thenComparing(Employee::getId, Comparator.nullsLast(Long::compareTo))
                .compare(this, other);
    }
}
