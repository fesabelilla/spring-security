package com.example.spring_security.controller;

import com.example.spring_security.model.Student;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("management/api/v1/students")
public class StudentManagementController {

    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1, "Zahid"),
            new Student(2, "Hasan"),
            new Student(3, "Kabir")
    );

    @GetMapping
    public List<Student> getAllStudents() {
        System.out.println("getAllStudents");
        return STUDENTS;
    }

    @PostMapping
    public void registerNewStudent(@RequestBody Student student) {
        System.out.println("registerNewStudent");
        System.out.println(student);
    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudent(@PathVariable("studentId") Integer studentId) {
        System.out.println("deleteStudent");
        System.out.println(STUDENTS.get(studentId));
    }

    @PutMapping(path = "{studentId}")
    public void updateStudents(@PathVariable("studentId") Integer studentId, @RequestBody Student student) {
        System.out.println("updateStudents");
        System.out.println(String.format("%s %s", studentId, student));
    }
}

