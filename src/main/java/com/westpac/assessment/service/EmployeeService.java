package com.westpac.assessment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.westpac.assessment.model.Employee;
import com.westpac.assessment.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository repository;

    public Employee addEmployee(String firstName, String lastName, String email, String department) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        Employee employee = new Employee();
        employee.setFirstName(firstName.trim());
        employee.setLastName(lastName.trim());
        employee.setEmail(email.trim());
        employee.setDepartment(department != null ? department.trim() : null);

        return repository.save(employee);
    }

    // Demo-only helpers to show Comparable (natural order) vs Comparator (custom order)
    public List<Employee> demoComparableSort() {
        List<Employee> employees = demoEmployees();
        employees.sort(null); // uses Employee.compareTo (Comparable)
        return employees;
    }

    public List<Employee> demoComparatorSortByEmail() {
        List<Employee> employees = demoEmployees();
        employees.sort(Comparator.comparing(Employee::getEmail, String.CASE_INSENSITIVE_ORDER));
        return employees;
    }

    private List<Employee> demoEmployees() {
        List<Employee> employees = new ArrayList<>();

        Employee e1 = new Employee();
        e1.setId(3L);
        e1.setFirstName("Ava");
        e1.setLastName("Collins");
        e1.setEmail("ava.collins@example.com");
        e1.setDepartment("Engineering");

        Employee e2 = new Employee();
        e2.setId(1L);
        e2.setFirstName("Ben");
        e2.setLastName("Hart");
        e2.setEmail("ben.hart@example.com");
        e2.setDepartment("Finance");

        Employee e3 = new Employee();
        e3.setId(2L);
        e3.setFirstName("Chloe");
        e3.setLastName("Nguyen");
        e3.setEmail("chloe.nguyen@example.com");
        e3.setDepartment("Operations");

        employees.add(e1);
        employees.add(e2);
        employees.add(e3);

        return employees;
    }

    // Demo-only helpers to show @Transactional self-invocation behavior
    @Transactional
    public String demoSelfInvocationDirect() {
        String outer = currentTxName();
        String inner = demoRequiresNew();
        return "outer=" + outer + ", inner=" + inner;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String demoRequiresNew() {
        return currentTxName();
    }

    private String currentTxName() {
        return TransactionSynchronizationManager.getCurrentTransactionName();
    }
}
