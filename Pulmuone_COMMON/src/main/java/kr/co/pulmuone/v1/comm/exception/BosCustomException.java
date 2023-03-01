package kr.co.pulmuone.v1.comm.exception;

import lombok.ToString;

@ToString
public class BosCustomException extends RuntimeException {

    private static final long serialVersionUID = 2324140157267040123L;

//    private final String RETURN_CODE; // 에러코드 : 생성자를 통해 초기화
//    private final String RETURN_MSG; // 에러 메시지 : 생성자를 통해 초기화
    private final String code; // 에러코드 : 생성자를 통해 초기화
    private final String message; // 에러 메시지 : 생성자를 통해 초기화

    /*
     * 생성자 ( 에러 코드, 에러 메시지 )
     */
    public BosCustomException(String returnCode, String returnMsg) {
        super(returnMsg);
//        this.RETURN_CODE = returnCode;
//        this.RETURN_MSG = returnMsg;
        this.code = returnCode;
        this.message = returnMsg;
    }

    /*
     * 생성자 ( 에러 코드, 에러 메시지, 발생한 예외 )
     */
    public BosCustomException(String returnCode, String returnMsg, Throwable e) {
        super(returnMsg, e);
//        this.RETURN_CODE = returnCode;
//        this.RETURN_MSG = returnMsg;
        this.code = returnCode;
        this.message = returnMsg;
    }
/*
    public String getReturnCode() {
        return this.RETURN_CODE;
    }

    public String getReturnMessage() {
        return this.RETURN_MSG;
    }
*/
    public String getCode() {
      return this.code;
  }

  public String getMessage() {
      return this.message;
  }

}
