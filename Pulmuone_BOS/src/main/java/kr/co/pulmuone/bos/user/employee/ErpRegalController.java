package kr.co.pulmuone.bos.user.employee;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.user.employee.dto.ErpRegalRequestDto;
import kr.co.pulmuone.v1.user.employee.service.ErpRegalBiz;
import lombok.RequiredArgsConstructor;

/**
* <PRE>
* Forbiz Korea
* ERP 법인정보 Controller
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
public class ErpRegalController {
    private final ErpRegalBiz erpRegalBiz;

    @ApiOperation(value = "ERP 법인 정보 조회")
    @PostMapping(value = "/admin/user/employee/getPulmuoneRegalList")
    public ApiResult<?> getPulmuoneRegalList(HttpServletRequest request, ErpRegalRequestDto erpRegalRequestDto) throws Exception{

        return erpRegalBiz.getPulmuoneRegalList(BindUtil.bindDto(request, ErpRegalRequestDto.class));
    }

    @ApiOperation(value = "ERP 법인 정보 전체 조회")
    @GetMapping(value= "/admin/user/employee/getPulmuoneRegalListWithoutPaging")
    public ApiResult<?> getPulmuoneRegalListWithoutPaging() {

        return erpRegalBiz.getPulmuoneRegalListWithoutPaging();
    }
}