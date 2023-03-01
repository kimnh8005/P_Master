package kr.co.pulmuone.v1.policy.shippingtemplate.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.vo.ShippingConditionVo;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.vo.ShippingWarehouseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "배송정책 등록 & 수정 Request")
public class ShippingTemplateManagementRequestDto extends BaseRequestDto {

    @ApiModelProperty(value = "배송정책 ID")
    private Long shippingTemplateId;

    @ApiModelProperty(value = "원본 배송정책 ID")
    private Long originalShippingTemplateId;

    @ApiModelProperty(value = "배송정책명")
    private String shippingTemplateName;

    @ApiModelProperty(value = "선/착불 구분")
    private String paymentMethodType;

    @ApiModelProperty(value = "합배송 여부")
    private String bundleYn;

    @ApiModelProperty(value = "조건배송비 구분코드")
    private String conditionTypeCode;

    @ApiModelProperty(value = "기본 배송비")
    private long shippingPrice;

    @ApiModelProperty(value = "클래임배송비")
    private long claimShippingPrice;

    @ApiModelProperty(value = "배송불가지역 공통코드(UNDELIVERABLE_AREA_TP)")
    private String undeliverableAreaTp;

    @ApiModelProperty(value = "지역별 배송 가능 여부")
    private String areaShippingDeliveryYn;

    @ApiModelProperty(value = "지역별 배송비 사용 여부")
    private String areaShippingYn;

    @ApiModelProperty(value = "제주 추가 배송비")
    private long jejuShippingPrice;

    @ApiModelProperty(value = "도서산간 추가 배송비")
    private long islandShippingPrice;

    @ApiModelProperty(value = "출고처코드")
    private Long warehouseId;

    @ApiModelProperty(value = "배송비 출고처 정보 리스트")
    private List<ShippingWarehouseVo>  shippingWarehouseList;

    @ApiModelProperty(value = "배송비 조건 정보 리스트")
    private List<ShippingConditionVo>  shippingConditionList;
}
