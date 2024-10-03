package com.groomiz.billage.classroom.document;

import com.groomiz.billage.classroom.exception.ClassroomErrorCode;
import com.groomiz.billage.global.anotation.ExceptionDoc;
import com.groomiz.billage.global.anotation.ExplainError;
import com.groomiz.billage.global.exception.GlobalCodeException;
import com.groomiz.billage.global.interfaces.SwaggerExampleExceptions;

@ExceptionDoc
public class ClassroomInfoExceptionDocs implements SwaggerExampleExceptions {

	@ExplainError
	public GlobalCodeException 강의실_존재하지_않음 = new GlobalCodeException(ClassroomErrorCode.CLASSROOM_NOT_FOUND);
}
