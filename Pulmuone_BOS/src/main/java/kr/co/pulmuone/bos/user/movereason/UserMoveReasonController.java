package kr.co.pulmuone.bos.user.movereason;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.user.movereason.dto.MoveReasonRequestDto;
import kr.co.pulmuone.v1.user.movereason.dto.MoveReasonResponseDto;
import kr.co.pulmuone.v1.user.movereason.service.UserMoveReasonBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* <PRE>
* Forbiz Korea
* 회원 탈퇴사유 설정
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 07. 17.    박영후            최초작성
*  1.0    2020. 10. 20.    최윤지            NEW 변경
* =======================================================================
* </PRE>
*/
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserMoveReasonController {
    private final UserMoveReasonBiz userMoveReasonBiz;


    @ApiOperation(value = "회원탈퇴 사유 설정 목록 조회")
    @PostMapping(value = "/admin/user/movereason/getMoveReasonList")
    @ApiResponses(value = {
        @ApiResponse(code = 900, message = "response data", response = MoveReasonResponseDto.class)
    })
    public ApiResult<?> getMoveReasonList(HttpServletRequest request, MoveReasonRequestDto moveReasonRequestDto) throws Exception{
        return userMoveReasonBiz.getMoveReasonList(BindUtil.bindDto(request, MoveReasonRequestDto.class));
    }


    @ApiOperation(value = "회원탈퇴 사유 설정 상세조회")
    @PostMapping(value = "/admin/user/movereason/getMoveReason")
    @ApiResponses(value = {
        @ApiResponse(code = 900, message = "response data", response = MoveReasonResponseDto.class)
    })
    public ApiResult<?> getMoveReason(MoveReasonRequestDto moveReasonRequestDto) {
        return userMoveReasonBiz.getMoveReason(moveReasonRequestDto);
    }


    @ApiOperation(value = "회원탈퇴 사유 설정 등록")
    @PostMapping(value = "/admin/user/movereason/addMoveReason")
    @ApiResponses(value = {
        @ApiResponse(code = 900, message = "request data", response = MoveReasonRequestDto.class),
        @ApiResponse(code = 901, message = "" + "DUPLICATE_DATA - 중복된 데이터가 존재합니다.")
    })
    public ApiResult<?> addMoveReason(MoveReasonRequestDto moveReasonRequestDto) {
        try
        {
            return userMoveReasonBiz.addMoveReason(moveReasonRequestDto);
        } catch(Exception e) {
            log.error("UserMoveReasonController.addMoveReason : {}", e);
            return ApiResult.fail();
        }
    }


    @ApiOperation(value = "회원탈퇴 사유 설정 수정")
    @PostMapping(value = "/admin/user/movereason/putMoveReason")
    @ApiResponses(value = {
        @ApiResponse(code = 900, message = "request data", response = MoveReasonRequestDto.class),
        @ApiResponse(code = 901, message = "" + "DUPLICATE_DATA - 중복된 데이터가 존재합니다.")
    })
    public ApiResult<?> putMoveReason(MoveReasonRequestDto moveReasonRequestDto) {
        try
        {
            return userMoveReasonBiz.putMoveReason(moveReasonRequestDto);
        } catch(Exception e) {
             log.error("UserMoveReasonController.putMoveReason : {}", e);
            return ApiResult.fail();
        }
    }
}