package kr.co.pulmuone.bos.store.template;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.send.template.dto.*;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;
import lombok.RequiredArgsConstructor;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * <PRE>
 * Email, SMS 발송
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :    작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200616                 오영민             최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
@RequiredArgsConstructor
public class StoreTemplateController {

    @Autowired(required=true)
    private HttpServletRequest request;

    @Autowired
    private SendTemplateBiz sendTemplateBiz;

    /**
     * Email, SMS 템플릿 설정 메인페이지 조회
     * @param getEmailSendListRequestDto
     * @return GetEmailSendListResponseDto
     * @throws Exception
     */
    @PostMapping(value = "/admin/sn/email/getEmailSendList")
    @ApiOperation(value = "Email, SMS 템플릿 설정", httpMethod = "POST", notes = "Email 템플릿 설정페이지 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GetEmailSendListResponseDto.class),
    })
    public ApiResult<?> getEmailSendList(GetEmailSendListRequestDto getEmailSendListRequestDto) throws Exception{
        return sendTemplateBiz.getEmailSendList((GetEmailSendListRequestDto) BindUtil.convertRequestToObject(request, GetEmailSendListRequestDto.class));
    }

    /**
     * Email, SMS 템플릿 설정 상세조회
     * @param snAutoSendId
     * @return GetEmailSendResponseDto
     * @throws Exception
     */
    @PostMapping(value = "/admin/sn/email/getEmailSend")
    @ApiOperation(value = "Email, SMS 템플릿 설정 상세", httpMethod = "POST", notes = "Email 템플릿 설정페이지 상세조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GetEmailSendResponseDto.class),
    })
    public ApiResult<?> getEmailSend(Long snAutoSendId) throws Exception{
        return sendTemplateBiz.getEmailSend(snAutoSendId);
    }

    /**
     * Email 템플릿 설정 등록
     * @param dto
     * @return AddEmailSendResponseDto
     * @throws Exception
     */
    @PostMapping(value = "/admin/sn/email/addEmailSend")
    @ApiOperation(value = "Email, SMS 설정 추가")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "SUCCESS"),
            @ApiResponse(code = 901, message = "" +
                    "DUPLICATE_DATA - 중복된 데이터가 존재합니다. \n" +
                    "MANDATORY_MISSING - 필수값을 입력해주세요. \n" +
                    "FAIL - 관리자에게 문의하세요."
            )
    })
    public ApiResult<?> addEmailSend(AddEmailSendRequestDto dto) throws Exception {
        return sendTemplateBiz.addEmailSend(dto);
    }

    /**
     * Email 템플릿 설정 수정
     * @param dto
     * @return PutEmailSendResponseDto
     * @throws Exception
     */
    @PostMapping(value = "/admin/sn/email/putEmailSend")
    @ApiOperation(value = "Email, SMS 설정 수정")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "SUCCESS"),
            @ApiResponse(code = 901, message = "" +
                    "DUPLICATE_DATA - 중복된 데이터가 존재합니다. \n" +
                    "MANDATORY_MISSING - 필수값을 입력해주세요. \n" +
                    "FAIL - 관리자에게 문의하세요."
            )
    })
    public ApiResult<?> putEmailSend(PutEmailSendRequestDto dto) throws Exception {
        return sendTemplateBiz.putEmailSend(dto);
    }

    /**
     * Email 템플릿 설정 삭제
     * @param snAutoSendId
     * @return DelEmailSendResponseDto
     * @throws Exception
     */
    @PostMapping(value = "/admin/sn/email/delEmailSend")
    @ApiOperation(value = "Email, SMS 템플릿 설정 삭제")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "SUCCESS"),
    })
    public ApiResult<?> delEmailSend(Long snAutoSendId) throws Exception {
        return sendTemplateBiz.delEmailSend(snAutoSendId);
    }
}