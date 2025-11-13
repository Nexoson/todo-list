
package com.max.todo_list.config;

import com.max.todo_list.dto.RestResult;
import com.max.todo_list.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理类
 *
 * @author huangxun
 * @date 2025-03-22 18:13:28
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {
    /**
     * 处理业务异常
     *
     * @param exception 业务异常对象
     * @return 返回包含异常信息的RestResult对象
     */
    @ExceptionHandler(BusinessException.class)
    public RestResult<Object> handleBusinessException(BusinessException exception) {
        // 记录异常信息到日志
        log.error(exception.getMessage(), exception);
        // 返回包含异常代码和消息的RestResult对象
        return new RestResult<>(exception.getCode(), exception.getMsg());
    }
    // 使用@ExceptionHandler注解来处理所有类型的Throwable异常
    @ExceptionHandler(Throwable.class)
    // 定义一个处理异常的方法，该方法接收一个Throwable类型的参数
    public ResponseEntity<String> handleException(Throwable throwable) {
        // 使用日志记录器log记录异常信息，包括异常消息和堆栈跟踪
        log.error(throwable.getMessage(), throwable);
        // 返回一个新的ResponseEntity对象，包含错误信息和HTTP状态码500（内部服务器错误）
        return new ResponseEntity<>("服务器内部错误", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        log.error("请求方法不支持: ", exception);
        return new ResponseEntity<>("请求方法不支持", HttpStatus.METHOD_NOT_ALLOWED);
    }
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<String> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
        log.error("媒体类型不支持: ", exception);
        return new ResponseEntity<>("媒体类型不支持", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<String> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException exception) {
        log.error("媒体类型无法接受: ", exception);
        return new ResponseEntity<>("媒体类型无法接受", HttpStatus.NOT_ACCEPTABLE);
    }
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<String> handleMissingPathVariableException(MissingPathVariableException exception) {
        log.error("路径变量缺失: ", exception);
        return new ResponseEntity<>("路径变量缺失", HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        log.error("请求参数缺失: ", exception);
        return new ResponseEntity<>("请求参数缺失", HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("参数验证失败: ", exception);
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        log.error("参数类型不匹配: ", exception);
        return new ResponseEntity<>("参数类型不匹配", HttpStatus.BAD_REQUEST);
    }
}
