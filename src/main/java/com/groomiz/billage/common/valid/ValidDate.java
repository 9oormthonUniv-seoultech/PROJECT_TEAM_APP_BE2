package com.groomiz.billage.common.valid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
public @interface ValidDate {
	String message() default "INVALID_DATE";
	Class[] groups() default {};
	Class[] payload() default {};
}
