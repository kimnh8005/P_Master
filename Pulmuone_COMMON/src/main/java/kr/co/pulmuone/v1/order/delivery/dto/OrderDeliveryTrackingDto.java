package kr.co.pulmuone.v1.order.delivery.dto;



import com.fasterxml.jackson.annotation.JsonAlias;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "송장추적 리스트 DTO")
public class OrderDeliveryTrackingDto {

    @ApiModelProperty(value = "화물상태코드")
    private String trackingStatusCode;

    @ApiModelProperty(value = "화물상태명")
    private String trackingStatusName;

    @ApiModelProperty(value = "처리일자")
    private String scanDate;

    @ApiModelProperty(value = "처리시간")
    private String scanTime;

    @ApiModelProperty(value = "처리점소코드")
    private String processingShopCode;

    @ApiModelProperty(value = "처리점소명")
    private String processingShopName;

    @ApiModelProperty(value = "처리점소전화번호")
    private String processingShopTelephone;

    @ApiModelProperty(value = "상대점소코드")
    private String partnerShopCode;

    @ApiModelProperty(value = "상대점소명")
    private String partnerShopName;

    @ApiModelProperty(value = "상대점소전화번호")
    private String partnerShopTelephone;

    @ApiModelProperty(value = "처리사원번호")
    private String processingEmployeeNumber;

    @ApiModelProperty(value = "처리사원명")
    private String processingEmployeeName;

    @ApiModelProperty(value = "처리사원전화번호")
    private String processingEmployeeTelephone;

    @ApiModelProperty(value = "점소명")
    private String shopName;

    @ApiModelProperty(value = "스캔구분값")
    private String scanTypeName;

    @ApiModelProperty(value = "연락처")
    private String telephone;
}
