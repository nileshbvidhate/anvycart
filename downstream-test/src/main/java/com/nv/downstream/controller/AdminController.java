package com.nv.downstream.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@GetMapping("/dashboard")
	public String adminDashboard(@RequestHeader("x-user-role") String role) {

		return "Admin access granted. Role = " + role;
	}

	@GetMapping("/greet")
	public String greeting() {

		return "WELCOME- ADMIN";
	}
}

//@Valid
//Used to trigger Bean Validation on an object argument (mostly @RequestBody DTO).
//It validates all constraint annotations present inside the DTO fields.
//Example: @RequestBody @Valid CreateInventoryRequest request
//Simply: //@Valid validates the object fields.
//Triggers validation of an object

//@Validated
//Used at class or method level to enable validation for method parameters
//such as @PathVariable, @RequestParam, and for validation groups.
//Without @Validated, method parameter constraints will NOT be validated.
//@Validated enables method-level validation and supports validation groups.
//Activates method parameter validation + supports groups

//@Validated is NOT meant to be used on method parameters like @RequestBody DTO
//@Validated works on:
//1. Class level
//2. Method level
//to enable validation
//
//For DTO object validation, we must use @Valid instead of @Validated.

//@Validated(Group.class) is used when:
//You want DIFFERENT validation rules for DIFFERENT operations
//Example:
//- Create API
//- Update API
//
//Some fields required only during Create
//Some fields required only during Update

//@Valid does NOT support validation groups
//It always validates ALL constraints
//
//@Validated(Group.class) validates ONLY constraints belonging to that group
/*
 * public class DTO {
 * 
 * @NotNull(groups = CreateGroup.class) private Long id;
 * 
 * @NotBlank private String name; }
 */

//@Valid DTO dto; will validates both id and name
//@Validated(CreateGroup.class) DTO dto; validates only id + name that belong to CreateGroup
//Only constraints that belong to CreateGroup are validated
//@NotNull(groups = CreateGroup.class)  --> VALIDATED
//@NotBlank (no group specified)       --> NOT validated

//Summary
//@Validated at class level is used to enable validation of method parameters like @PathVariable, @RequestParam, and @RequestHeader.
//It is NOT required for @RequestBody @Valid DTO validation.
//@Validated enables method-level validation in Spring, while @Valid triggers object validation.
//RequestBody works with @Valid without needing @Validated.

//Note: For controllers we can use @Validated and class level only not the method level because...
//Controllers are already proxied by Spring MVC, so adding another proxy layer is not guaranteed.

//But method level work in service layer sometimes
