package kr.co.pulmuone.v1.statics.pm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@ToString
@ApiModel(description = "PromotionStaticsRequestDto")
public class PromotionStaticsRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "기준기간 조회년")
    private String findYear;

    @ApiModelProperty(value = "기준기간 조회월")
    private String findMonth;

    @ApiModelProperty(value = "조회 시작일자")
    private String searchDateStart;

    @ApiModelProperty(value = "조회 종료일자")
    private String searchDateEnd;

    @ApiModelProperty(value = "조회 분담조직정보")
    private String searchOrganization;

    @ApiModelProperty(value = "조회조건정보", required = false)
    private String searchInfo;

    @ApiModelProperty(value = "쿠폰번호 조회값")
    private String searchCouponId;

    @ApiModelProperty(value = "기간검색 구분")
    private String searchTp;

    @ApiModelProperty(value = "쿠폰종류 구분")
    private String searchCouponStatus;

    @ApiModelProperty(value = "발급목적 구분")
    private String searchIssuedType;

    @ApiModelProperty(value = "쿠폰명 구분")
    private String searchSelect;

    @ApiModelProperty(value = "재발행 포함/제외 구분")
    private String searchReissue;

    @ApiModelProperty(value = "쿠폰명 조회")
    private String searchCouponName;

    @ApiModelProperty(value = "외부광고구분 대분류(매체)")
    private String source;

    @ApiModelProperty(value = "외부광고구분 중분류(구좌)")
    private String medium;

    @ApiModelProperty(value = "외부광고구분 소분류(캠페인)")
    private String campaign;

    @ApiModelProperty(value = "외부광고구분 세분류(콘텐츠)")
    private String content;

    @ApiModelProperty(value = "외부광고코드 조회")
    private String searchPmAdExternalCd;

    @ApiModelProperty(value = "내부광고코드 구분")
    private String searchType;

    @ApiModelProperty(value = "페이지 코드")
    private String pageCd;

    @ApiModelProperty(value = "카테고리 코드")
    private String contentCd;

    @ApiModelProperty(value = "카테고리 코드 KeyIn")
    private String searchContentNm;

    @ApiModelProperty(value = "내부광고 코드")
    private String searchPmAdInternalPageCd;


    @ApiModelProperty(value = "쿠폰번호 조회값 Array")
    private ArrayList<String> searchCouponIdArray;
}
