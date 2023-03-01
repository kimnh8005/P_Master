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
@ApiModel(description = "잇슬림 주문조회 API Request Dto")
public class EatsslimOrderInfoRequestDto {

    @ApiModelProperty(value = "접속서버 IP")
    private String currentIp;

    @ApiModelProperty(value = "인증키")
    private String authKey;

    @ApiModelProperty(value = "USERID")
    private String userID;

    @ApiModelProperty(value = "API ID")
    private String apiId;

    @ApiModelProperty(value = "사이트약어")
    private String siteCd;

    @ApiModelProperty(value = "CRUD 구분")
    private String mode;

    @ApiModelProperty(value = "CRUD의 Seq (R = 00:상세조건검색, 01통합검색, 12:주문서정보조회 화면의 주문목록-그리드)")
    private String modeSeq;

    @ApiModelProperty(value = "Service Code (API Code)")
    private String serviceCd;

    @ApiModelProperty(value = "주문번호 (ODID)")
    private String outOrderNum;

    @ApiModelProperty(value = "상품세트ID")
    private String gooodsSetId;
}