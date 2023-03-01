package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 현금영수증 발급내역 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 05. 18.            천혜현        최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "현금영수증 발급내역 Request Dto")
public class CashReceiptIssuedListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "기간검색 유형")
    private String dateSearch;

    @ApiModelProperty(value = "기간검색 시작일")
    private String startDate;

    @ApiModelProperty(value = "기간검색 종료일")
    private String endDate;

    @ApiModelProperty(value = "현금영수증 발급구분")
    private String cashReceiptType;

    @ApiModelProperty(value = "PG구분")
    private String pgType;

    @ApiModelProperty(value = "발급여부")
    private String issueType;

    @ApiModelProperty(value = "결제수단")
    private String payType;

    @ApiModelProperty(value = "검색어 유형")
    private String searchType;

    @ApiModelProperty(value = "검색어")
    private String searchWord;

    @ApiModelProperty(value = "CS 사용자 조회 ID")
    private String urUserId;

}

