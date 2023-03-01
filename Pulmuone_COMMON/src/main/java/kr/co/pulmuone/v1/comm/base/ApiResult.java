package kr.co.pulmuone.v1.comm.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(description = "API 결과 클래스")
public class ApiResult<T> {

    // bos
/*
	@ApiModelProperty(notes = "데이터")
    private T rows;

    @ApiModelProperty(notes = "코드")
    @JsonProperty("RETURN_CODE")
    private String returncode = BaseEnums.Default.SUCCESS.getCode();

    @ApiModelProperty(notes = "메시지")
    @JsonProperty("RETURN_MSG")
    private String returnmsg;
*/

    // mall
    @ApiModelProperty(notes = "데이터")
    private T data;

    @ApiModelProperty(notes = "코드")
    private String code = BaseEnums.Default.SUCCESS.getCode();

    @ApiModelProperty(notes = "메시지")
    private String message;

    @ApiModelProperty(notes = "메시지 EnumClass", hidden = true)
    private MessageCommEnum messageEnum;

    /**
     * 결과 코드
     * @param code
     * @return
     */
    private ApiResult<T> code(String code) {
        this.code = code;
//        this.returncode = code;
        return this;
    }

    /**
     * 결과 메시지
     * @param message
     * @return
     */
    private ApiResult<T> message(String message) {
        this.message = message;
//        this.returnmsg = message;
        return this;
    }

    /**
     * 결과 메시지
     * @param messageEnum
     * @return
     */
    private ApiResult<T> messageEnum(MessageCommEnum messageEnum) {
        this.messageEnum = messageEnum;
        return this;
    }

    public static <T> ApiResult<T> ofNullable(T data) {
        if(data != null) {
            return success(data);
        } else {
            return fail(null, BaseEnums.Default.FAIL);
        }
    }

    public static <T> ApiResult<T> result(T data, MessageCommEnum enums) {
        return with(data).code(enums.getCode()).message(enums.getMessage()).messageEnum(enums);
    }

    public static ApiResult<?> result(MessageCommEnum enums) {
        return with(null).code(enums.getCode()).message(enums.getMessage()).messageEnum(enums);
    }

    /**
     * 성공 객체 생성
     * @return
     */
    public static ApiResult<?> success() {
        return with(null).code(BaseEnums.Default.SUCCESS.getCode()).message(BaseEnums.Default.SUCCESS.getMessage()).messageEnum(BaseEnums.Default.SUCCESS);
    }

    /**
     * 성공 객체 생성
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> success(T data) {
        return with(data).code(BaseEnums.Default.SUCCESS.getCode()).message(BaseEnums.Default.SUCCESS.getMessage()).messageEnum(BaseEnums.Default.SUCCESS);
    }

    /**
     * 실패 객체 생성
     * @param
     * @return
     */
    public static ApiResult<?> fail() {

        return result(BaseEnums.Default.FAIL);
    }

    /**
     * 실패 객체 생성
     * @param
     * @return
     */
    private static <T> ApiResult<T> fail(T data, MessageCommEnum enums) {
        return with(data).code(enums.getCode()).message(enums.getMessage()).messageEnum(enums);
    }

//    /**
//     * 실패 객체 생성
//     * @param enums
//     * @return
//     */
//    public static ApiResult<?> fail(MessageCommEnum enums) {
//
//        return fail(enums.getCode(), enums.getMessage()).messageEnum(enums);
//    }

//    /**
//     * 실패 객체 생성
//     * @param code
//     * @param message
//     * @return
//     */
//    private static ApiResult<?> fail(String code, String message) {
//        return with(null).code(code).message(message);
//    }


    /**
     * 객체 생성
     * @param data
     * @param <T>
     * @return
     */
    private static <T> ApiResult<T> with(T data) {
        ApiResult<T> response = new ApiResult<>();
        response.data = data;
//        response.rows = data;
        return response;
    }

}
