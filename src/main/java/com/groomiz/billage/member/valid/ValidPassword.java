package com.groomiz.billage.member.valid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

	String message() default "비밀번호는 영문 8~16자이고 특수문자가 1개 이상 포함되어야 합니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}