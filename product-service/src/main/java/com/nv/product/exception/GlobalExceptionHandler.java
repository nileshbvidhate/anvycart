package com.nv.product.exception;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.NoHandlerFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////// HTTP
	////////////////////////////////////// Exceptions
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Occurs when API is called with unsupported HTTP method
	// Example: calling POST on GET endpoint
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpServletRequest request) {

		return buildErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, "HTTP method not supported for this endpoint",
				request.getRequestURI());
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////// URL
	////////////////////////////////////// Exceptions
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Occurs when requested URL mapping does not exist
	// Example: wrong endpoint path
	// Requires spring.mvc.throw-exception-if-no-handler-found=true
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ErrorResponse> handleNoHandlerFound(NoHandlerFoundException ex, HttpServletRequest request) {

		return buildErrorResponse(HttpStatus.NOT_FOUND, "Endpoint not found", request.getRequestURI());
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////// JSON
	////////////////////////////////////// Exceptions
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// if json body is missing or malformed for request
	// @RequestBody with no JSON → HttpMessageNotReadableException
	// @RequestBody with invalid JSON → HttpMessageNotReadableException
	// @RequestBody with JSON that violates @Valid → MethodArgumentNotValidException
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpServletRequest request) {

		String message = "Request body is missing or malformed";

		return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////// Headers
	////////////////////////////////////// Exceptions
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// for @RequestHeader annotation if header is missing
	// for ex. @RequestHeader Long userId; if userId is not sent in request header
	@ExceptionHandler(MissingRequestHeaderException.class)
	public ResponseEntity<ErrorResponse> handleMissingRequestHeader(MissingRequestHeaderException ex,
			HttpServletRequest request) {

		String message = "Required header '" + ex.getHeaderName() + "' is missing";

		return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////// Type
	////////////////////////////////////// Exceptions
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// For @RequestHeader, @RequestParam and @PathVariable
	// If value cannot be converted to required type then it throws
	// For Ex. @RequestHeader Long userId; if converion fails throws exception
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			HttpServletRequest request) {

		String message = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s", ex.getValue(),
				ex.getName(), ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");

		return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////// Validation
	////////////////////////////////////// Exceptions
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// @RequestBody + @Valid -> MethodArgumentNotValidException
	// when we use @RequestBody and @Valid fails then it will throw the
	// MethodArgumentNotValidException Exception Object
	// Without custom handler, Spring returns default error format (HTML Format),
	// we convert it into our standard ErrorResponse JSON format
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		// Extract the first error message
		String errorMessage = ex.getBindingResult().getFieldErrors().stream()
				.map(err -> err.getField() + ": " + err.getDefaultMessage()).findFirst() // gives only first filed error
				.orElse("Validation failed");

		// gives list of errors of fields
		// List<String> errors = ex.getBindingResult()
		// .getFieldErrors()
		// .stream()
		// .map(err -> err.getField() + ": " + err.getDefaultMessage())
		// .collect(Collectors.toList());

		return buildErrorResponse(HttpStatus.BAD_REQUEST, errorMessage, request.getRequestURI());
	}

	// @RequestHeader @RequestParam and @PathVariable -> throws
	// ConstraintViolationException
	// @NotBlank, @NotNull, @NotEmpty, @Size, @Min, @Max, @Pattern
	// throw ConstraintViolationException when used with
	// @RequestHeader, @RequestParam, and @PathVariable
	// These same annotations throw MethodArgumentNotValidException
	// when used inside @RequestBody DTO with @Valid
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
			HttpServletRequest request) {

		String message = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).findFirst()
				.orElse("Invalid request");

		return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////// Database
	////////////////////////////////////// Exceptions
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Spring translates SQL constraint errors into DataIntegrityViolationException
	// This occurs when database constraints are violated such as:
	// - Unique key violation (duplicate record)
	// - Foreign key violation
	// - Not null constraint violation
	// - Check constraint violation
	// We return 409 CONFLICT because data already exists or violates DB rule
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex,
			HttpServletRequest request) {

		String message = "Database constraint violation";

		return buildErrorResponse(HttpStatus.CONFLICT, message, request.getRequestURI());
	}

	// Low level JDBC exception when constraint is violated directly at SQL layer
	// Occurs when insert/update violates primary key, unique key or foreign key
	// Sometimes not translated by Spring into DataIntegrityViolationException
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleSQLIntegrityConstraintViolation(
			SQLIntegrityConstraintViolationException ex, HttpServletRequest request) {

		return buildErrorResponse(HttpStatus.CONFLICT, "Database constraint violated", request.getRequestURI());
	}

	// Thrown when delete/update is attempted on non-existing row
	// Example: deleteById(id) when record does not exist
	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<ErrorResponse> handleEmptyResultDataAccess(EmptyResultDataAccessException ex,
			HttpServletRequest request) {

		return buildErrorResponse(HttpStatus.NOT_FOUND, "Resource not found", request.getRequestURI());
	}

	// Occurs when transaction fails during commit or rollback
	// Mostly caused by validation failure or constraint violation during flush
	@ExceptionHandler(TransactionSystemException.class)
	public ResponseEntity<ErrorResponse> handleTransactionSystem(TransactionSystemException ex,
			HttpServletRequest request) {

		return buildErrorResponse(HttpStatus.BAD_REQUEST, "Transaction failed due to invalid data",
				request.getRequestURI());
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////// Spring Data JPA
	////////////////////////////////////// Exceptions
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Thrown by JPA when entity is not found in database
	// Common when using getReferenceById() or lazy loading proxy
	// Example: fetching entity which does not exist in DB
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request) {

		return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////// WebClient
	////////////////////////////////////// Exceptions
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ExceptionHandler(WebClientResponseException.class)
	public ResponseEntity<ErrorResponse> handleWebClientResponseException(WebClientResponseException ex,
			HttpServletRequest request) {

		return buildErrorResponse(HttpStatus.BAD_GATEWAY, "Inventory service error: " + ex.getStatusCode(),
				request.getRequestURI());
	}

	@ExceptionHandler(WebClientRequestException.class)
	public ResponseEntity<ErrorResponse> handleWebClientRequestException(WebClientRequestException ex,
			HttpServletRequest request) {

		return buildErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, "Product service is unavailable",
				request.getRequestURI());
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////// Business (Custom)
	////////////////////////////////////// Exceptions
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ExceptionHandler(CustomAccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleCustomAccessDenied(CustomAccessDeniedException ex,
			HttpServletRequest request) {

		return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex,
			HttpServletRequest request) {

		return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleResourceAlreadyExists(ResourceAlreadyExistsException ex,
			HttpServletRequest request) {

		return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI());
	}

	// Thrown when business rule is violated by client input
	// Example:
	// - Image does not belong to product
	// - Trying to update resource with invalid relationship
	// - Operation not allowed in current state
	// This is a logical error, not a system failure
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
			HttpServletRequest request) {

		return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////// Generic
	////////////////////////////////////// Exceptions
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {

		return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong", request.getRequestURI());
	}

	// reusable builder method
	private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message, String path) {

		ErrorResponse error = new ErrorResponse();
		error.setTimestamp(LocalDateTime.now());
		error.setStatus(status.value());
		error.setError(status.name());
		error.setMessage(message);
		error.setPath(path);

		return ResponseEntity.status(status).body(error);
	}

}
