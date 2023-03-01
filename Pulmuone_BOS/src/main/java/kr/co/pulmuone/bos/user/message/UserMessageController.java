package kr.co.pulmuone.bos.user.message;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.send.push.dto.*;
import kr.co.pulmuone.v1.send.push.service.SendPushBiz;
import kr.co.pulmuone.v1.user.login.dto.LoginResponseDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserMessageController {

    @Autowired
    private SendPushBiz sendPushBiz;

    @Autowired(required=true)
    private HttpServletRequest request;

    /**
     * @Desc 모바일 푸시발송이력 조회
     * @param GetPushSendListRequestDto
     * @return GetPushSendListResponseDto
     * @throws Exception
     */
    @PostMapping(value = "/admin/sn/push/getPushSendList")
    @ApiOperation(value = "모바일 푸시발송이력 조회")
    @ApiResponses(value = {@ApiResponse(code = 900, message = "response data", response = GetPushSendListResponseDto.class)})
    public ApiResult<?> getPushSendList(GetPushSendListRequestDto getPushSendListRequestDto) throws Exception{
        return ApiResult.success(sendPushBiz.getPushSendList((GetPushSendListRequestDto) BindUtil.convertRequestToObject(request, GetPushSendListRequestDto.class)));
    }

    /**
     * @Desc 선택회원 PUSH 발송
     * @param AddPushIssueSelectRequestDto
     * @return AddPushIssueSelectResponseDto
     * @throws Exception
     */
    @PostMapping(value = "/admin/sn/push/addPushIssueSelect")
    @ApiOperation(value = "선택회원 PUSH 발송")
    @ApiResponses(value = {@ApiResponse(code = 900, message = "response data", response = AddPushIssueSelectResponseDto.class)})
    public ApiResult<?> addPushIssueSelect(AddPushIssueSelectRequestDto dto)throws Exception{
        if( SendEnums.AppPushSendType.TEXT_IMAGE.getCode().equals(dto.getPushSendType()) ){
            dto.setAddFileList(BindUtil.convertJsonArrayToDtoList(dto.getAddFile(), FileVo.class));
        }

        dto.setBuyerDeviceList(BindUtil.convertJsonArrayToDtoList(dto.getBuyerDevice(), PushSendListRequestDto.class));

        return ApiResult.success(sendPushBiz.addPushIssueSelect(dto));
    }

    /**
     * @Desc 검색회원 PUSH 발송
     * @param AddPushIssueSearchRequestDto
     * @return AddPushIssueSearchResponseDto
     * @throws Exception
     */
    @PostMapping(value = "/admin/sn/push/addPushIssueSearch")
    @ApiOperation(value = "검색회원 PUSH 발송")
    @ApiResponses(value = {@ApiResponse(code = 900, message = "response data", response = AddPushIssueSearchResponseDto.class)})
    public ApiResult<?> addPushIssueSearch(@RequestBody AddPushIssueSearchRequestDto dto)throws Exception{
        if( SendEnums.AppPushSendType.TEXT_IMAGE.getCode().equals(dto.getPushSendType()) ){
            dto.setAddFileList(BindUtil.convertJsonArrayToDtoList(dto.getAddFile(), FileVo.class));
        }

        dto.setBuyerDeviceList(BindUtil.convertJsonArrayToDtoList(dto.getBuyerDevice(), PushSendListRequestDto.class));

        return ApiResult.success(sendPushBiz.addPushIssueSearch(dto));
    }

    /**
     * @Desc 모바일 푸시 발송(전체) PUSH 발송
     * @param dto
     * @return AddPushIssueAllResponseDto
     * @throws Exception
     */
    @PostMapping(value = "/admin/sn/push/addPushIssueAll")
    @ApiOperation(value = "모바일 푸시 발송(전체) PUSH 발송")
    @ApiResponses(value = {@ApiResponse(code = 900, message = "response data", response = AddPushIssueAllResponseDto.class)})
    public ApiResult<?> addPushIssueAll(AddPushIssueAllRequestDto dto)throws Exception{
        if(SendEnums.AppPushSendType.TEXT_IMAGE.getCode().equals(dto.getPushSendType()) ){
            dto.setAddFileList(BindUtil.convertJsonArrayToDtoList(dto.getAddFile(), FileVo.class));
        }

        if( "UPLOAD_USER".equals(dto.getSendGroup()) ) {
            dto.setUploadUserList(BindUtil.convertJsonArrayToDtoList(dto.getUploadUser(), PushSendListRequestDto.class));
        }

        return ApiResult.success(sendPushBiz.addPushIssueAll(dto));
    }

}
