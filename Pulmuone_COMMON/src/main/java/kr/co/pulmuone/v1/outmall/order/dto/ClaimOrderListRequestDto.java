package kr.co.pulmuone.v1.outmall.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "외부몰 클레임 주문리스트 검색조건 Request Dto")
public class ClaimOrderListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "검색유형")
    private String selectConditionType;

    //단일 조건
    @ApiModelProperty(value = "단일조건 검색 유형")
    private String singleSearchType;

    @ApiModelProperty(value = "단일조건 검색 값")
    private String singleSearchValue;

    //복수 조건
    @ApiModelProperty(value = "외부몰 그룹 구분유형")
    private String outMallType;

    @ApiModelProperty(value = "외부몰 그룹 Filter")
    private String outMallFilterAll;

    @ApiModelProperty(value = "외부몰 그룹 Filter")
    private String outMallFilterVendor;

    @ApiModelProperty(value = "외부몰 그룹 Filter")
    private String outMallFilterDirectMng;

    @ApiModelProperty(value = "외부몰 그룹 Filter")
    private String outMallFilterDirectBuy;

    @ApiModelProperty(value = "외부몰 그룹 조회 조건 List")
    private List<String> shopIdList;

    @ApiModelProperty(value = "기간 검색 유형")
    private String searchDateType;

    @ApiModelProperty(value = "시작일자")
    private String startDate;

    @ApiModelProperty(value = "종료일자")
    private String endDate;

    @ApiModelProperty(value = "CS 상태")
    private String orderCsFilter;

    @ApiModelProperty(value = "CS 상태 리스트")
    private List<String> orderCsList;

    @ApiModelProperty(value = "주문상태")
    private String orderStatusFilter;

    @ApiModelProperty(value = "주문미생성여부")
    private String orderStatusNotFilter;

    @ApiModelProperty(value = "주문상태 리스트")
    private List<String> orderStatusList;

    @ApiModelProperty(value = "관리자 아이디 리스트")
    private List<String> adminIdList;

    @ApiModelProperty(value = "관리자 아이디")
    private String adminSearchValue;

    @ApiModelProperty(value = "처리상태")
    private String processCodeFilter;

    @ApiModelProperty(value = "처리상태 리스트")
    private List<String> processCodeList;

    @ApiModelProperty(value = "복수 검색어 조회 유형")
    private String multiSearchType;

    @ApiModelProperty(value = "복수 검색어 값")
    private String multiSearchValue;
}