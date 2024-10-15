package com.groomiz.billage.global.exception;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.groomiz.billage.auth.document.LoginExceptionDocs;
import com.groomiz.billage.global.dto.ErrorReason;
import com.groomiz.billage.global.dto.ErrorResponse;
import com.groomiz.billage.member.entity.College;
import com.groomiz.billage.member.entity.Major;
import com.groomiz.billage.member.exception.MemberErrorCode;
import com.groomiz.billage.member.exception.MemberException;
import com.groomiz.billage.reservation.entity.ReservationPurpose;
import com.groomiz.billage.reservation.entity.ReservationStatusType;
import com.groomiz.billage.reservation.entity.ReservationType;
import com.groomiz.billage.reservation.exception.AdminReservationErrorCode;
import com.groomiz.billage.reservation.exception.AdminReservationException;
import com.groomiz.billage.reservation.exception.ReservationErrorCode;
import com.groomiz.billage.reservation.exception.ReservationException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	public GlobalExceptionHandler(LoginExceptionDocs loginExceptionDocs) {
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
		Exception ex, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		ServletWebRequest servletWebRequest = (ServletWebRequest) request;
		HttpServletRequest httpServletRequest = servletWebRequest.getRequest();
		String url = httpServletRequest.getRequestURL().toString();

		HttpStatus httpStatus = (HttpStatus) status;
		ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase(),
			ex.getMessage(), url);
		return super.handleExceptionInternal(ex, errorResponse, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		ServletWebRequest servletWebRequest = (ServletWebRequest)request;
		HttpServletRequest httpServletRequest = servletWebRequest.getRequest();
		String url = httpServletRequest.getRequestURL().toString();

		// 존재하지 않는 enum 값 예외 처리
		if (ex.getCause() instanceof ValueInstantiationException) {
			Class<?> rawClass = ((ValueInstantiationException)ex.getCause()).getType().getRawClass();

			BaseErrorCode errorCode = null;
			GlobalCodeException exception = null;

			if (rawClass.equals(ReservationPurpose.class)) {
				errorCode = ReservationErrorCode.INVALID_RESERVATION_PURPOSE;
				exception = new ReservationException((ReservationErrorCode)errorCode);
			} else if (rawClass.equals(ReservationStatusType.class)) {
				errorCode = ReservationErrorCode.INVALID_RESERVATION_STATUS_TYPE;
				exception = new ReservationException((ReservationErrorCode)errorCode);
			} else if (rawClass.equals(ReservationType.class)) {
				errorCode = AdminReservationErrorCode.INVALID_RESERVATION_TYPE;
				exception = new AdminReservationException((AdminReservationErrorCode)errorCode);
			} else if (rawClass.equals(College.class)) {
				errorCode = MemberErrorCode.INVALID_COLLEGE_ENUM;
				exception = new MemberException((MemberErrorCode)errorCode);
			} else if (rawClass.equals(Major.class)) {
				errorCode = MemberErrorCode.INVALID_MAJOR_ENUM;
				exception = new MemberException((MemberErrorCode)errorCode);
			} else {
				errorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR;
				exception = new GlobalCodeException(errorCode);
			}

			ErrorReason reason = exception.getErrorReason();
			ErrorResponse errorResponse =
				new ErrorResponse(reason, url);

			return ResponseEntity.status(HttpStatus.valueOf(reason.getStatus()))
				.body(errorResponse);
		}

		// 날짜, 시간 예외 처리
		if (ex.getCause().getCause() instanceof DateTimeParseException) {
			String message = ex.getCause().getMessage();

			ReservationErrorCode errorCode = null;

			if (message.contains("LocalDate:")) {
				errorCode = ReservationErrorCode.INVALID_RESERVATION_DATE;
			}
			else if (message.contains("LocalTime:")) {
				errorCode = ReservationErrorCode.INVALID_RESERVATION_TIME;
			}
			else {
				return super.handleHttpMessageNotReadable(ex, headers, status, request);
			}

			ReservationException reservationException = new ReservationException(errorCode);

			ErrorReason reason = reservationException.getErrorReason();
			ErrorResponse errorResponse =
				new ErrorResponse(reservationException.getErrorReason(), url);

			return ResponseEntity.status(HttpStatus.valueOf(reason.getStatus()))
				.body(errorResponse);
		}

		return super.handleHttpMessageNotReadable(ex, headers, status, request);
	}

	@ExceptionHandler(MemberException.class)
	public ResponseEntity<ErrorResponse> handleMemberException(MemberException ex, HttpServletRequest request) {

		ErrorReason reason = ex.getErrorReason();
		ErrorResponse errorResponse =
			new ErrorResponse(ex.getErrorReason(), request.getRequestURL().toString());

		return ResponseEntity.status(HttpStatus.valueOf(reason.getStatus()))
			.body(errorResponse);
	}

	@ExceptionHandler(ReservationException.class)
	public ResponseEntity<ErrorResponse> handleReservationException(ReservationException ex, HttpServletRequest request) {

		ErrorReason reason = ex.getErrorReason();
		ErrorResponse errorResponse =
			new ErrorResponse(ex.getErrorReason(), request.getRequestURL().toString());

		return ResponseEntity.status(HttpStatus.valueOf(reason.getStatus()))
			.body(errorResponse);
	}

	@ExceptionHandler(AdminReservationException.class)
	public ResponseEntity<ErrorResponse> handleAdminReservationException(ReservationException ex, HttpServletRequest request) {

		log.info("AdminReservationException: {}", ex.getMessage());
		ErrorReason reason = ex.getErrorReason();
		ErrorResponse errorResponse =
			new ErrorResponse(ex.getErrorReason(), request.getRequestURL().toString());

		return ResponseEntity.status(HttpStatus.valueOf(reason.getStatus()))
			.body(errorResponse);
	}

	//주로 요청 본문이 유효성 검사를 통과하지 못할 때 발생합니다 (예: @Valid 어노테이션 사용 시) MethodArgumentNotValidException 예외를 처리하는 메서드
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request) {

		List<FieldError> errors = ex.getBindingResult().getFieldErrors();
		ServletWebRequest servletWebRequest = (ServletWebRequest)request;
		HttpServletRequest httpServletRequest = servletWebRequest.getRequest();
		String url = httpServletRequest.getRequestURL().toString();

		String firstErrorMessage = errors.getFirst().getDefaultMessage();

		// 전화번호 형식 에러
		MemberErrorCode invalidPhoneNumberErrorCode = MemberErrorCode.INVALID_PHONE_NUMBER;

		if (firstErrorMessage.equals(invalidPhoneNumberErrorCode.toString())) {
			MemberException memberException = new MemberException(invalidPhoneNumberErrorCode);

			ErrorReason reason = memberException.getErrorReason();
			ErrorResponse errorResponse =
				new ErrorResponse(memberException.getErrorReason(), url);

			return ResponseEntity.status(HttpStatus.valueOf(reason.getStatus()))
				.body(errorResponse);
		}

		Map<String, Object> fieldAndErrorMessages =
			errors.stream()
				.collect(
					Collectors.toMap(
						FieldError::getField, FieldError::getDefaultMessage));

		String errorsToJsonString = null;
		try {
			errorsToJsonString = new ObjectMapper().writeValueAsString(fieldAndErrorMessages);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		HttpStatus httpStatus = (HttpStatus)status;
		ErrorResponse errorResponse =
			new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase(), errorsToJsonString, url);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	/** Request Param Validation 예외 처리
	 * 유효성 검사 제약 조건이 위반되었을 때 발생합니다. (예: @NotNull, @Size, @Email 어노테이션 사용 시)
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> constraintViolationExceptionHandler(
		ConstraintViolationException ex, HttpServletRequest request) {
		Map<String, Object> bindingErrors = new HashMap<>(); // 유효성 검사 실패 필드와 해당 오류 메시지를 저장하기 위한 맵을 생성
		// 예외 객체에서 유효성 검사 위반 항목들을 가져옴.
		ex.getConstraintViolations()
			.forEach(
				constraintViolation -> {
					//위반된 속성의 경로를 가져옵니다. 이 경로는 문자열로 변환되어 점(.)을 기준으로 분할됩니다
					List<String> propertyPath =
						List.of(
							constraintViolation
								.getPropertyPath()
								.toString()
								.split("\\."));
					// 마지막 요소를 추출하여 실제 필드 이름을 가져옵니다. 예를 들어, 경로가 user.address.street라면 street가 추출됩니다.
					String path =
						propertyPath.stream()
							.skip(propertyPath.size() - 1L)
							.findFirst()
							.orElse(null);
					//위반된 필드 이름과 해당 오류 메시지를 맵에 저장
					bindingErrors.put(path, constraintViolation.getMessage());
				});

		ErrorReason errorReason =
			ErrorReason.builder()
				.code("BAD_REQUEST")
				.status(400)
				.reason(bindingErrors.toString())
				.build();

		ErrorResponse errorResponse =
			new ErrorResponse(errorReason, request.getRequestURL().toString());
		return ResponseEntity.status(HttpStatus.valueOf(errorReason.getStatus()))
			.body(errorResponse);
	}

	//TODO: 이 경우 디코에 알림 가도록 구성해도 좋겠다.
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request)
		throws IOException {
		ServletWebRequest servletWebRequest = (ServletWebRequest)request;
		HttpServletRequest httpServletRequest = servletWebRequest.getRequest(); // 예외가 발생한 URL과 같은 요청에 대한 세부 정보를 추출
		String url = httpServletRequest.getRequestURL().toString();

		log.error("INTERNAL_SERVER_ERROR", ex);
		GlobalErrorCode internalServerError = GlobalErrorCode.INTERNAL_SERVER_ERROR;
		ErrorResponse errorResponse =
			new ErrorResponse(
				internalServerError.getStatus(),
				internalServerError.getCode(),
				internalServerError.getReason(),
				url);

		return ResponseEntity.status(HttpStatus.valueOf(internalServerError.getStatus()))
			.body(errorResponse);
	}
}



