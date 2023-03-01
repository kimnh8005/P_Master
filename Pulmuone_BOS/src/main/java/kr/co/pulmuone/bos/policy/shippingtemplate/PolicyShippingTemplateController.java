package kr.co.pulmuone.bos.policy.shippingtemplate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.ShippingTemplateManagementRequestDto;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.ShippingTemplateRequestDto;
import kr.co.pulmuone.v1.policy.shippingtemplate.service.PolicyShippingTemplateBiz;
import lombok.RequiredArgsConstructor;

/**
* <PRE>
* Forbiz Korea
* 배송정책설정 Controller
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 9. 28.                손진구          최초작성
* =======================================================================
* </PRE>
*/
@RestController
@RequiredArgsConstructor
public class PolicyShippingTemplateController {
    private final PolicyShippingTemplateBiz policyShippingTemplateBiz;

    @ApiOperation(value = "배송정책 설정 조회")
    @PostMapping(value = "/admin/policy/shippingtemplate/getShippingTemplateList")
    public ApiResult<?> getShippingTemplateList(HttpServletRequest request, ShippingTemplateRequestDto shippingTemplateRequestDto) throws Exception{

        return policyShippingTemplateBiz.getShippingTemplateList(BindUtil.bindDto(request, ShippingTemplateRequestDto.class));
    }

    @ApiOperation(value = "배송정책 상세 정보 조회")
    @PostMapping(value = "/admin/policy/shippingtemplate/getShippingTemplate")
    public ApiResult<?> getShippingTemplate(@RequestBody ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto){
        return policyShippingTemplateBiz.getShippingTemplate(shippingTemplateManagementRequestDto);
    }

    @ApiOperation(value = "배송정책 추가")
    @PostMapping(value = "/admin/policy/shippingtemplate/addShippingTemplate")
    public ApiResult<?> addShippingTemplate(@RequestBody ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto) throws Exception{

        return policyShippingTemplateBiz.addShippingTemplate(shippingTemplateManagementRequestDto);
    }

    @ApiOperation(value = "배송정책 수정")
    @PostMapping(value = "/admin/policy/shippingtemplate/putShippingTemplate")
    public ApiResult<?> putShippingTemplate(@RequestBody ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto) throws Exception{

        return policyShippingTemplateBiz.putShippingTemplate(shippingTemplateManagementRequestDto);
    }

    @ApiOperation(value = "상품 배송비 관계 출고처 유무 체크")
    @PostMapping(value = "/admin/policy/shippingtemplate/getGoodsShippingTemplateYn")
    public ApiResult<?> getGoodsShippingTemplateYn(@RequestBody ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto){
        return policyShippingTemplateBiz.getGoodsShippingTemplateYn(shippingTemplateManagementRequestDto);
    }

    @ApiOperation(value = "배송정책 출고처 기본여부 체크")
    @PostMapping(value = "/admin/policy/shippingtemplate/getShippingWarehouseBasicYnCheck")
    public ApiResult<?> getShippingWarehouseBasicYnCheck(@RequestBody ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto){
        return policyShippingTemplateBiz.getShippingWarehouseBasicYnCheck(shippingTemplateManagementRequestDto);
    }
}