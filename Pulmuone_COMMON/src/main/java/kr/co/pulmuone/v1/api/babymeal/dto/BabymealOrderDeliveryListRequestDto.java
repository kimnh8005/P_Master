package kr.co.pulmuone.v1.api.babymeal.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "베이비밀 주문정보, 배송 스케쥴정보 조회 API Request Dto")
public class BabymealOrderDeliveryListRequestDto {

    @ApiModelProperty(value = "접속서버 IP")
    private String currentIp;

    @ApiModelProperty(value = "인증키")
    private String authKey;

    @ApiModelProperty(value = "USERID")
    private String userID;

    @ApiModelProperty(value = "APIID")
    private String apiId;

    @ApiModelProperty(value = "companyID")
    private String companyID;

    @ApiModelProperty(value = "사이트약어")
    private String siteCd;

    @ApiModelProperty(value = "주문번호 (ODID)")
    private String shopOrderNo;

    @ApiModelProperty(value = "주문 상세 SEQ")
    private String podSeq;

    @ApiModelProperty(value = "Service Code (API Code)")
    private String serviceCd;
}