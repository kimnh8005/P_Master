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
@ApiModel(description = "베이비밀 스케줄 배송일자 변경 API Request Dto")
public class BabymealOrderUpdateInfoRequestDto {

    @ApiModelProperty(value = "접속서버 IP")
    private String currentIp;

    @ApiModelProperty(value = "인증키")
    private String authKey;

    @ApiModelProperty(value = "userID")
    private String userID;

    @ApiModelProperty(value = "userName")
    private String userName;

    @ApiModelProperty(value = "API ID")
    private String apiId;

    @ApiModelProperty(value = "사이트약어")
    private String siteCd;

    @ApiModelProperty(value = "Service Code (API Code)")
    private String serviceCd;

    @ApiModelProperty(value = "destsiteCD")
    private String destsiteCD;

    @ApiModelProperty(value = "언어")
    private String languageCD;

    @ApiModelProperty(value = "localeCD")
    private String localeCD;

    @ApiModelProperty(value = "clientDate")
    private String clientDate;

    @ApiModelProperty(value = "timezoneCD")
    private String timezoneCD;

    @ApiModelProperty(value = "currencyCD")
    private String currencyCD;

    @ApiModelProperty(value = "dateFormatCD")
    private String dateFormatCD;

    @ApiModelProperty(value = "amtFormatCD")
    private String amtFormatCD;

    @ApiModelProperty(value = "베이비밀 주문번호")
    private String orderNo;

    @ApiModelProperty(value = "배송타입")
    private String deliveryType;

    @ApiModelProperty(value = "spartnerDivCD")
    private String spartnerDivCD;

    @ApiModelProperty(value = "kindCd")
    private String kindCd;

    @ApiModelProperty(value = "ordAddr1Ji")
    private String ordAddr1Ji;

    @ApiModelProperty(value = "ordAddr2")
    private String ordAddr2;

    @ApiModelProperty(value = "deliverySeq")
    private String deliverySeq;

    @ApiModelProperty(value = "jisaCd")
    private String jisaCd;

    @ApiModelProperty(value = "activeFlg")
    private String activeFlg;

    @ApiModelProperty(value = "addDt")
    private String addDt;

    @ApiModelProperty(value = "giftTypeCd")
    private String giftTypeCd;

    @ApiModelProperty(value = "goodsGroupId")
    private String goodsGroupId;

    @ApiModelProperty(value = "deliveryDate")
    private String deliveryDate;

    @ApiModelProperty(value = "originDate")
    private String originDate;

    @ApiModelProperty(value = "giftChange")
    private String giftChange;

    @ApiModelProperty(value = "kind")
    private String kind;

    @ApiModelProperty(value = "giftType")
    private String giftType;

    @ApiModelProperty(value = "giftKey")
    private String giftKey;

    @ApiModelProperty(value = "ordZipCd")
    private String ordZipCd;

    @ApiModelProperty(value = "ordAddr1Do")
    private String ordAddr1Do;

    @ApiModelProperty(value = "addCnt")
    private String addCnt;
}