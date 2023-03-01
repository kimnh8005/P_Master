package kr.co.pulmuone.v1.api.eatsslim.dto;

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
@ApiModel(description = "잇슬림 스케줄 배송일자 변경 API Request Dto")
public class EatsslimOrderUpdateInfoRequestDto {

    @ApiModelProperty(value = "접속서버 IP")
    private String currentIp;

    @ApiModelProperty(value = "인증키")
    private String authKey;

    @ApiModelProperty(value = "userID")
    private String userID;

    @ApiModelProperty(value = "userId")
    private String userId;

    @ApiModelProperty(value = "userNm")
    private String userNm;

    @ApiModelProperty(value = "API ID")
    private String apiId;

    @ApiModelProperty(value = "사이트약어")
    private String siteCd;

    @ApiModelProperty(value = "CRUD 구분")
    private String mode;

    @ApiModelProperty(value = "CRUD의 Seq")
    private String modeSeq;

    @ApiModelProperty(value = "Service Code (API Code)")
    private String serviceCd;

    @ApiModelProperty(value = "destsiteCD")
    private String destsiteCD;

    @ApiModelProperty(value = "insertTypeTxt")
    private String insertTypeTxt;

    @ApiModelProperty(value = "activeFlgTxt")
    private String activeFlgTxt;

    @ApiModelProperty(value = "증정상태")
    private String giftTypeTxt;

    @ApiModelProperty(value = "잇슬림 주문번호")
    private String orderNumTxt;

    @ApiModelProperty(value = "상품 그룹코드")
    private String groupCodeTxt;

    @ApiModelProperty(value = "우편번호")
    private String zipCodeTxt;

    @ApiModelProperty(value = "주소")
    private String addressTxt;

    @ApiModelProperty(value = "상세주소")
    private String addressDetailTxt;

    @ApiModelProperty(value = "잇슬림 주문상품 고유ID(PK)")
    private String idTxt;

    @ApiModelProperty(value = "서브주문번호")
    private String oSubNumTxt;

    @ApiModelProperty(value = "변경할 도착예정일")
    private String stdateTxt;

    @ApiModelProperty(value = "상품그룹코드")
    private String gubunCodeTxt;

    @ApiModelProperty(value = "주문수량")
    private String orderCntTxt;

    @ApiModelProperty(value = "가맹점코드")
    private String agencyIdTxt;
}