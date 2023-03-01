package kr.co.pulmuone.v1.policy.shippingtemplate.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.vo.ShippingConditionVo;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.vo.ShippingTemplateVo;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.vo.ShippingWarehouseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "배송정책 설정 Response")
public class ShippingTemplateManagementResponseDto{

    @ApiModelProperty(value = "배송비 탬플릿")
    private ShippingTemplateVo  shippingTemplateInfo;

    @ApiModelProperty(value = "배송비 출고처 정보 리스트")
    private List<ShippingWarehouseVo>  shippingWarehouseList;

    @ApiModelProperty(value = "배송비 조건 정보 리스트")
    private List<ShippingConditionVo>  shippingConditionList;

    @ApiModelProperty(value = "상품 배송정책 사용유무")
    private Boolean goodsShippingTemplateUseYn;

    @ApiModelProperty(value = "다른 배송정책 출고처 기본여부")
    private Boolean otherShippingWarehouseBasicYn;

}
