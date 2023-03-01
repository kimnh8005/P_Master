package kr.co.pulmuone.bos.user.employee;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.user.employee.dto.ErpOrganizationRequestDto;
import kr.co.pulmuone.v1.user.employee.service.ErpOrganizationBiz;
import lombok.RequiredArgsConstructor;

/**
* <PRE>
* Forbiz Korea
* ERP 조직정보 Controller
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 9. 16.                손진구          최초작성
* =======================================================================
* </PRE>
*/
@RestController
@RequiredArgsConstructor
public class ErpOrganizationController {
    private final ErpOrganizationBiz erpOrganizationBiz;

    @ApiOperation(value = "ERP 조직정보 조회")
    @GetMapping(value = "/admin/user/employee/getPulmuoneOrganizationList")
    public ApiResult<?> getPulmuoneOrganizationList(HttpServletRequest request, ErpOrganizationRequestDto erpOrganizationRequestDto) throws Exception{

        if( StringUtils.isEmpty(erpOrganizationRequestDto.getErpRegalCode()) ) {
            erpOrganizationRequestDto = BindUtil.bindDto(request, ErpOrganizationRequestDto.class);
        }

        return erpOrganizationBiz.getPulmuoneOrganizationList(erpOrganizationRequestDto);
    }
}