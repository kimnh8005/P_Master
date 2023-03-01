package kr.co.pulmuone.bos.user.company;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.user.company.dto.BusinessInformationRequestDto;
import kr.co.pulmuone.v1.user.company.dto.vo.BusinessInformationVo;
import kr.co.pulmuone.v1.user.company.service.UserCompanyBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* <PRE>
* Forbiz Korea
* 회사 Controller
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 10. 26.               손진구          최초작성
* =======================================================================
* </PRE>
*/

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserCompnayController {
    private final UserCompanyBiz userCompanyBiz;

    @ApiOperation(value = "사업자정보 관리 조회")
    @GetMapping(value = "/admin/user/company/getBizInfo")
    @ApiResponses(value = { @ApiResponse(code = 900, message = "response data ", response = BusinessInformationVo.class) })
    public ApiResult<?> getBizInfo() {
        return userCompanyBiz.getBizInfo();
    }

    @ApiOperation(value = "사업자정보 관리 등록")
    @PostMapping(value = "/admin/user/company/addBizInfo")
    @ApiResponses(value = { @ApiResponse(code = 900, message = "관리자에게 문의하세요.") })
    public ApiResult<?> addBizInfo(BusinessInformationRequestDto businessInformationRequestDto) {

        try
        {
            businessInformationRequestDto.setAddFileList(BindUtil.convertJsonArrayToDtoList(businessInformationRequestDto.getAddFile(), FileVo.class));

            return userCompanyBiz.addBizInfo(businessInformationRequestDto);
        } catch(Exception e) {
            log.error("UserCompnayController.addBizInfo : {}", e);
            return ApiResult.fail();
        }
    }

    @ApiOperation(value = "사업자정보 관리 수정")
    @PostMapping(value = "/admin/user/company/putBizInfo")
    @ApiResponses(value = { @ApiResponse(code = 900, message = "관리자에게 문의하세요.") })
    public ApiResult<?> putBizInfo(BusinessInformationRequestDto businessInformationRequestDto) {

        try
        {
            if( StringUtils.isNotEmpty( businessInformationRequestDto.getAddFile() ) ) {
                businessInformationRequestDto.setAddFileList(BindUtil.convertJsonArrayToDtoList(businessInformationRequestDto.getAddFile(), FileVo.class));
            }

            return userCompanyBiz.putBizInfo(businessInformationRequestDto);
        } catch(Exception e) {
            log.error("UserCompnayController.putBizInfo : {}", e);
            return ApiResult.fail();
        }
    }
}