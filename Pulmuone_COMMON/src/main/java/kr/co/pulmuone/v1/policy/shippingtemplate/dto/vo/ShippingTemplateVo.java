package kr.co.pulmuone.v1.policy.shippingtemplate.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "배송비 탬플릿 Vo")
public class ShippingTemplateVo{

    // 목록 조회
    @ApiModelProperty(value = "배송정책 ID")
    private Long shippingTemplateId;

    @ApiModelProperty(value = "배송정책명")
    private String shippingTemplateName;

    @ApiModelProperty(value = "조건배송비 구분코드")
    private String conditionTypeCode;

    @ApiModelProperty(value = "조건배송비 구분명")
    private String conditionTypeName;

    @ApiModelProperty(value = "배송비")
    private long shippingPrice;

    @ApiModelProperty(value = "클래임 배송비")
    private long claimShippingPrice;

    @ApiModelProperty(value = "합배송 여부")
    private String bundleYn;

    @ApiModelProperty(value = "배송불가지역 공통코드(UNDELIVERABLE_AREA_TP)")
    private String undeliverableAreaTp;

    @ApiModelProperty(value = "지역별 배송가능 여부")
    private String areaShippingDeliveryYn;

    @ApiModelProperty(value = "지역별 배송비 사용 여부")
    private String areaShippingYn;

    @ApiModelProperty(value = "출고처 갯수")
    private int warehouseCount;

    @ApiModelProperty(value = "출고처 이름")
    private String warehouseName;

    @ApiModelProperty(value = "기본 유무")
    private String basicYn;

    // 등록 & 수정
    @ApiModelProperty(value = "원본 배송정책 ID")
    private Long originalShippingTemplateId;

    @ApiModelProperty(value = "선/착불 구분")
    private String paymentMethodType;

    @ApiModelProperty(value = "제주 추가 배송비")
    private long jejuShippingPrice;

    @ApiModelProperty(value = "도서산간 추가 배송비")
    private long islandShippingPrice;

    @ApiModelProperty(value = "지역별 배송비 사용 여부")
    private String shippingContractCode;

    @ApiModelProperty(value = "등록자ID")
    private String createId;
}
