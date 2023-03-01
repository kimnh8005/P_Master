package kr.co.pulmuone.v1.comm.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class CommonControllerExceptionHandler {

    /*
     * BOS Exception 예제
     */
    @ExceptionHandler(BosCustomException.class)
    public Map<String, Object> bosCustomException(BosCustomException e) {
        log.error("BosCustomException", e);
        Map<String, Object> map = new HashMap<>();

//        map.put("RETURN_CODE", e.getReturnCode());
//        map.put("RETURN_MSG", e.getReturnMessage());
        map.put("code", e.getCode());
        map.put("message", e.getMessage());

        return map;
    }

	/*
	 * 공통 BaseException
	 */
	@ExceptionHandler(BaseException.class)
	public ApiResult<?> CommonException(BaseException e) {
		log.error(e.getMessageEnum().getMessage());
		return ApiResult.result(e.getMessageEnum());
	}

	/*
	 * 공통 Exception
	 */
	@ExceptionHandler(Exception.class)
	public ApiResult<?> CommonException(Exception e) {
		log.error("Exception", e);
		return ApiResult.result(BaseEnums.Default.EXCEPTION_ISSUED);
	}

	/*
	 * Mall Exception 예제
	 */
	@ExceptionHandler(MallCustomException.class)
	public ModelAndView MallCustom(MallCustomException e) {
		log.error("MallCustomException", e);
		ModelAndView mv = new ModelAndView();
		mv.addObject("Return_Code", "코드정의");
		mv.addObject("Return_Msg", e.getMessage());
		mv.setViewName("exception");
		return mv;
	}

	 /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생W
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
   	public ApiResult<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
		return ApiResult.result(BaseEnums.Default.EXCEPTION_HTTP);
    }


    /**
     * @ModelAttribut 으로 binding error 발생시 BindException
     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     */
    @ExceptionHandler(BindException.class)
   	public ApiResult<?> handleBindException(BindException e) {
		log.error("handleBindException", e);
		return ApiResult.result(BaseEnums.Default.EXCEPTION_BIND);
    }


    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
   	public ApiResult<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException", e);
		return ApiResult.result(BaseEnums.Default.EXCEPTION_ENUM);
    }


	/* 필요한 Exception 추가 */


}
