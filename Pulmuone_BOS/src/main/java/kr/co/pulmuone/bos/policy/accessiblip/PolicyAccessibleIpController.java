package kr.co.pulmuone.bos.policy.accessiblip;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.policy.accessibleip.dto.basic.*;
import kr.co.pulmuone.v1.policy.accessibleip.service.PolicyAccessibleIpBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;


/**
 * <PRE>
 * Forbiz Korea
 * BOS 접근 IP 설정 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20201020    최성현                 최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class PolicyAccessibleIpController {

    @Autowired
    private PolicyAccessibleIpBiz policyAccessibleIpBiz;

    @ApiOperation(value = "BOS 접근가능 IP설정 리스트")
    @PostMapping(value = "/admin/policy/accessibleIp/getPolicyAccessibleIpList")
    public ApiResult<?> getAccessibleIpList(HttpServletRequest request, GetPolicyAccessibleIpListRequestDto getPolicyAccessibleIpListRequestDto) throws Exception{
            return policyAccessibleIpBiz.getPolicyAccessibleIpList(BindUtil.bindDto(request, GetPolicyAccessibleIpListRequestDto.class));

    }


    @ApiOperation(value = "BOS 접근가능 IP설정 생성, 수정, 삭제")
    @RequestMapping(value = "/admin/policy/accessibleIp/savePolicyAccessibleIp")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "request data", response = SavePolicyAccessibleIpRequestDto.class),
            @ApiResponse(code = 901, message = "" +
                    "DUPLICATE_DATA_DO_REFRESH - 중복된 데이터가 존재합니다."
            )
    })
    public ApiResult<?> saveAccessibleIp(SavePolicyAccessibleIpRequestDto savePolicyAccessibleIpRequestDto) throws Exception{


            System.out.println(savePolicyAccessibleIpRequestDto);
            //binding data
            savePolicyAccessibleIpRequestDto.setInsertRequestDtoList(BindUtil.convertJsonArrayToDtoList(savePolicyAccessibleIpRequestDto.getInsertData(), SavePolicyAccessibleIpRequestSaveDto.class));
            savePolicyAccessibleIpRequestDto.setUpdateRequestDtoList(BindUtil.convertJsonArrayToDtoList(savePolicyAccessibleIpRequestDto.getUpdateData(), SavePolicyAccessibleIpRequestSaveDto.class));
            savePolicyAccessibleIpRequestDto.setDeleteRequestDtoList(BindUtil.convertJsonArrayToDtoList(savePolicyAccessibleIpRequestDto.getDeleteData(), SavePolicyAccessibleIpRequestSaveDto.class));

            return policyAccessibleIpBiz.savePolicyAccessibleIp(savePolicyAccessibleIpRequestDto);

    }

}

