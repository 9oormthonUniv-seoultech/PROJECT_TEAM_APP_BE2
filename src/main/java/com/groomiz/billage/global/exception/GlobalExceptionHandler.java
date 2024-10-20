

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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groomiz.billage.auth.document.LoginExceptionDocs;
import com.groomiz.billage.auth.exception.AuthException;
import com.groomiz.billage.global.dto.ErrorReason;
import com.groomiz.billage.global.dto.ErrorResponse;
import com.groomiz.billage.member.exception.MemberErrorCode;
import com.groomiz.billage.member.exception.MemberException;
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

		if (ex.getCause().getCause() instanceof DateTimeParseException) {
			String message = ex.getCause().getMessage();

			ReservationErrorCode errorCode = null;

			if (message.contains("LocalDate:")) {
				errorCode = ReservationErrorCode.INVALID_RESERVATION_DATE;
			}
			else if (message.contains("LocalTime:")) {
				errorCode = ReservationErrorCode.INVALID_RESERVATION_TIME;
			}
			else if (message.contains("DateValidator:")) {
				errorCode = ReservationErrorCode.PAST_DATE_RESERVATION; // 새 에러 코드 추가
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

	@ExceptionHandler(AuthException.class)
	public ResponseEntity<ErrorResponse> handleAuthException(AuthException ex, HttpServletRequest request) {
		ErrorReason reason = ex.getErrorCode().getErrorReason();
		ErrorResponse errorResponse =
			new ErrorResponse(ex.getErrorCode().getErrorReason(), request.getRequestURL().toString());

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
		if (firstErrorMessage.equals("INVALID_PHONE_NUMBER")) {
			MemberErrorCode invalidPhoneNumberErrorCode = MemberErrorCode.INVALID_PHONE_NUMBER;
			MemberException memberException = new MemberException(invalidPhoneNumberErrorCode);

			ErrorReason reason = memberException.getErrorReason();
			ErrorResponse errorResponse =
				new ErrorResponse(memberException.getErrorReason(), url);

			return ResponseEntity.status(HttpStatus.valueOf(reason.getStatus()))
				.body(errorResponse);
		}

		// 예약 날짜가 과거인 경우
		if (firstErrorMessage.equals("PAST_DATE")) {
			ReservationErrorCode errorCode = ReservationErrorCode.PAST_DATE_RESERVATION;
			ReservationException reservationException = new ReservationException(errorCode);

			ErrorReason reason = reservationException.getErrorReason();
			ErrorResponse errorResponse = new ErrorResponse(reason, url);

			return ResponseEntity.status(HttpStatus.valueOf(reason.getStatus()))
				.body(errorResponse);
		}

		// 예약 날짜가 한 달 이후인 경우
		if (firstErrorMessage.equals("FUTURE_DATE")) {
			ReservationErrorCode errorCode = ReservationErrorCode.FUTURE_DATE_LIMIT_EXCEEDED;
			ReservationException reservationException = new ReservationException(errorCode);

			ErrorReason reason = reservationException.getErrorReason();
			ErrorResponse errorResponse = new ErrorResponse(reason, url);

			return ResponseEntity.status(HttpStatus.valueOf(reason.getStatus()))
				.body(errorResponse);
		}

		// 인원이 음수인 경우
		if (firstErrorMessage.equals("NEGATIVE_PARTICIPANTS")) {
			ReservationErrorCode errorCode = ReservationErrorCode.NEGATIVE_PARTICIPANTS;
			ReservationException reservationException = new ReservationException(errorCode);

			ErrorReason reason = reservationException.getErrorReason();
			ErrorResponse errorResponse = new ErrorResponse(reason, url);

			return ResponseEntity.status(HttpStatus.valueOf(reason.getStatus()))
				.body(errorResponse);
		}

		// 인원이 최대 인원을 초과하는 경우
		if (firstErrorMessage.equals("EXCEED_MAX_PARTICIPANTS")) {
			ReservationErrorCode errorCode = ReservationErrorCode.EXCEED_MAX_PARTICIPANTS;
			ReservationException reservationException = new ReservationException(errorCode);

			ErrorReason reason = reservationException.getErrorReason();
			ErrorResponse errorResponse = new ErrorResponse(reason, url);

			return ResponseEntity.status(HttpStatus.valueOf(reason.getStatus()))
				.body(errorResponse);
		}

		// 이메일 형식 잘못 입력한 경우
		if (firstErrorMessage.equals("INVALID_EMAIL")) {
			MemberErrorCode errorCode = MemberErrorCode.INVALID_EMAIL;
			MemberException memberException = new MemberException(errorCode);

			ErrorReason reason = memberException.getErrorReason();
			ErrorResponse errorResponse = new ErrorResponse(reason, url);

			return ResponseEntity.status(HttpStatus.valueOf(reason.getStatus()))
				.body(errorResponse);
		}

		// 비밀번호 형식 잘못 입력한 경우
		if (firstErrorMessage.equals("INVALID_PASSWORD")) {
			MemberErrorCode errorCode = MemberErrorCode.INVALID_PASSWORD_FORMAT;
			MemberException memberException = new MemberException(errorCode);

			ErrorReason reason = memberException.getErrorReason();
			ErrorResponse errorResponse = new ErrorResponse(reason, url);

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
	protected ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) throws IOException {
		HttpServletRequest httpServletRequest;

		// RequestContextHolder를 통해 HttpServletRequest를 안전하게 가져옵니다.
		if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes) {
			httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		} else {
			// 만약 RequestContextHolder가 ServletRequestAttributes의 인스턴스가 아닌 경우
			// 기본 HttpServletRequest 객체를 사용합니다.
			httpServletRequest = request;
		}

		String url = httpServletRequest.getRequestURL().toString();

		log.error("INTERNAL_SERVER_ERROR", ex);
		GlobalErrorCode internalServerError = GlobalErrorCode.INTERNAL_SERVER_ERROR;
		ErrorResponse errorResponse = new ErrorResponse(
			internalServerError.getStatus(),
			internalServerError.getCode(),
			internalServerError.getReason(),
			url
		);

		return ResponseEntity.status(HttpStatus.valueOf(internalServerError.getStatus()))
			.body(errorResponse);
	}

}