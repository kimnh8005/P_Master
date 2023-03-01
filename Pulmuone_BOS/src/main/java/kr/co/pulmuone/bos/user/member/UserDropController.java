package kr.co.pulmuone.bos.user.member;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.bos.user.member.service.UserDropBosService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserDropListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserDropListResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20200612		   	천혜현            최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class UserDropController {

    @Autowired
    private UserDropBosService userDropBosService;

    @Autowired(required = true)
    private HttpServletRequest request;

    /**
     * 탈퇴회원 리스트조회
     *
     * @param dto
     * @return GetUserDropListResponseDto
     * @throws Exception exception
     */
    @PostMapping(value = "/admin/ur/UserDrop/getUserDropList")
    @ApiOperation(value = "탈퇴회원 목록 조회", httpMethod = "POST", notes = "탈퇴회원 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ApiResult.class),
            @ApiResponse(code = 901, message = "" +
                    "0000 - 성공 \n" +
                    "9999 - 관리자에게 문의하세요."
            )
    })
    public ApiResult<?> getUserDropList(GetUserDropListRequestDto dto) throws Exception {

        return ApiResult.success(userDropBosService.getUserDropList(
                                                        (GetUserDropListRequestDto) BindUtil.convertRequestToObject(
                                                        request, GetUserDropListRequestDto.class)));
    }


}



