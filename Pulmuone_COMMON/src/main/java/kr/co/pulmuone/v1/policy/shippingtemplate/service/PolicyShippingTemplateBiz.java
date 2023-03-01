package kr.co.pulmuone.v1.policy.shippingtemplate.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.ShippingTemplateManagementRequestDto;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.ShippingTemplateRequestDto;

public interface PolicyShippingTemplateBiz {

    ApiResult<?> getShippingTemplateList(ShippingTemplateRequestDto shippingTemplateRequestDto) throws Exception;
    ApiResult<?> getShippingTemplate(ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto);
    ApiResult<?> addShippingTemplate(ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto) throws Exception;
    ApiResult<?> putShippingTemplate(ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto) throws Exception;
    ApiResult<?> getGoodsShippingTemplateYn(ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto);
    ApiResult<?> getShippingWarehouseBasicYnCheck(ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto);
}
