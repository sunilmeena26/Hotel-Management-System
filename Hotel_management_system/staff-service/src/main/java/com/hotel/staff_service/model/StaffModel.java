package com.hotel.staff_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "staff_table")
public class StaffModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Employee code is required")
    private String code;

    @NotBlank(message = "Employee name is required")
    private String employeeName;

    @NotBlank(message = "Address is required")
    private String employeeAddress;

    @Positive(message = "Salary must be a positive number")
    private double salary;

    @Min(value = 18, message = "Employee must be at least 18 years old")
    @Max(value = 65, message = "Employee age must not exceed 65")
    private int age;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    @Column(nullable = false)
    private String phoneNumber;

    @NotNull
    private String aboutWork;
}