package com.proyekakhir.manajemendokumen.core.controller;

import com.proyekakhir.manajemendokumen.core.exception.AlurKerjaException;
import com.proyekakhir.manajemendokumen.core.response.CommonRs;
import org.camunda.bpm.engine.BadUserRequestException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class BaseController {

    public ResponseEntity<Object> success(Object data) {
        return new ResponseEntity<>(new CommonRs(HttpStatus.OK.value(), "success", data), HttpStatus.OK);
    }

    public ResponseEntity<Object> error(Object data) {
        return new ResponseEntity<>(new CommonRs(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error", data),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Object> badRequest(Object data) {
        return new ResponseEntity<>(new CommonRs(HttpStatus.BAD_REQUEST.value(), "bad_request", data),
                HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Object> notFound(Object data) {
        return new ResponseEntity<>(new CommonRs(HttpStatus.NOT_FOUND.value(), "not_found", data),
                HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Object> forbidden(Object data) {
        return new ResponseEntity<>(new CommonRs(HttpStatus.FORBIDDEN.value(), "forbidden", data),
                HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<Object> success(Object data, String message) {
        return new ResponseEntity<>(new CommonRs(HttpStatus.OK.value(), message, data), HttpStatus.OK);
    }

    public ResponseEntity<Object> badRequest(Object data, String message) {
        return new ResponseEntity<>(new CommonRs(HttpStatus.BAD_REQUEST.value(), message, data),
                HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Object> forbidden(Object data, String message) {
        return new ResponseEntity<>(new CommonRs(HttpStatus.FORBIDDEN.value(), message, data),
                HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<Object> notFound(Object data, String message) {
        return new ResponseEntity<>(new CommonRs(HttpStatus.NOT_FOUND.value(), message, data),
                HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Object> error(HttpStatus httpStatus, Object message) {
        return new ResponseEntity<>(new CommonRs(httpStatus.value(), "error", message), httpStatus);
    }

    public ResponseEntity<Resource> okDownload(String filename, String mediaType, byte[] data) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(data.length)
                .contentType(MediaType.parseMediaType(mediaType))
                .body(new ByteArrayResource(data));
    }

    public Pageable pageFromRequest(int page, int size, String sort, Boolean asc) {
        return ObjectUtils.isEmpty(sort) ?
                PageRequest.of(page, size) :
                PageRequest.of(page, size, Sort.by(getSortBy(sort, asc)));
    }

    private Sort.Order getSortBy(String sort, Boolean asc) {
        return Boolean.TRUE.equals(asc) ?
                Sort.Order.asc(sort) :
                Sort.Order.desc(sort);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();
        HashMap<String, String> errors = new HashMap<>();
        for(FieldError field: fieldErrorList){
            errors.put(field.getField(), field.getDefaultMessage());
        }
        return badRequest(errors, ex.getMessage());
    }

    @ExceptionHandler({BadUserRequestException.class})
    public ResponseEntity<Object> badUserRequestException(BadUserRequestException ex){
        return badRequest(ex.getMessage());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<Object> httpMessageNotReadableException(HttpMessageNotReadableException ex){
        return badRequest(ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> constraintViolationException(ConstraintViolationException ex){
        return badRequest(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> illegalArgumentException(IllegalArgumentException ex){
        return badRequest(ex.getMessage());
    }

    @ExceptionHandler({AlurKerjaException.class})
    public ResponseEntity<Object> alurKerjaExceptionHandler(HttpServletRequest request,
                                                       AlurKerjaException ex){

        String lang = request.getAttribute("lang").toString();
        ResourceBundle message = ResourceBundle.getBundle("messages", lang.equals("id") ? Locale.ROOT : Locale.ENGLISH);

        String text = "";
        try {
            text = message.getString(ex.getMessage());
            if (ex.getValues() != null){
                int i = 0;
                for (String value : ex.getValues()) {
                    text = text.replace("{" + i + "}", value);
                    i++;
                }
            }
        }
        catch (Exception e){
            text = ex.getMessage();
        }


        if (ex.getErrorCode() == 400)
            return badRequest(null, text);
        if (ex.getErrorCode() == 404)
            return notFound(null, text);
        return error(HttpStatus.EXPECTATION_FAILED, text);
    }
}
