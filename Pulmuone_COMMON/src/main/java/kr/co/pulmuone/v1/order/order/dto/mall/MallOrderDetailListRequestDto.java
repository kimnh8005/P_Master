package kr.co.pulmuone.v1.order.order.dto.mall;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문상세 리스트 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 12.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */

@Builder
@Getter
@ToString
@ApiModel(description = "Mall 주문상세 리스트 Request Dto")
public class MallOrderDetailListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "단일조건 _ 복수조건 검색")
	private String selectConditionType;

	@ApiModelProperty(value = "주문상세구분")
	private String orderDetailType;

    @ApiModelProperty(value = "판매처 그룹")
    private String sellersGroup;

    @ApiModelProperty(value = "판매처")
    private String omSellersId;

    @ApiModelProperty(value = "판매처 리스트")
    private List<String> omSellersIdList;

    @ApiModelProperty(value = "기간검색유형")
    private String dateSearchType;

    @ApiModelProperty(value = "기간검색 시작일자")
    private String dateSearchStart;

    @ApiModelProperty(value = "기간검색 종료일자")
    private String dateSearchEnd;

    @ApiModelProperty(value = "주문상태")
    private String orderState;

    @ApiModelProperty(value = "주문상태 리스트")
    private List<String> orderStateList;

    @ApiModelProperty(value = "클레임상태")
    private String claimState;

    @ApiModelProperty(value = "클레임상태 리스트")
    private List<String> claimStateList;

    @ApiModelProperty(value = "결제수단")
    private String paymentMethodCode;

    @ApiModelProperty(value = "결제수단 리스트")
    private List<String> paymentMethodCodeList;

    @ApiModelProperty(value = "주문자유형")
    private String buyerTypeCode;

    @ApiModelProperty(value = "주문자유형 리스트")
    private List<String> buyerTypeCodeList;

    @ApiModelProperty(value = "유형")
    private String agentTypeCode;

    @ApiModelProperty(value = "유형 리스트")
    private List<String> agentTypeCodeList;

    @ApiModelProperty(value = "복수조건 검색조건")
	private String searchMultiType;

	@ApiModelProperty(value = "복수조건 검색어")
	private String findKeyword;

    @ApiModelProperty(value = "단일조건 검색조건")
	private String searchSingleType;

	@ApiModelProperty(value = "단일조건 검색어")
	private String codeSearch;

    @ApiModelProperty(value = "단일조건 검색어 리스트")
    private ArrayList<String> codeSearchList;

	@ApiModelProperty(value = "반품 단일조건 주문상태")
	private String orderStateSingle;

    @ApiModelProperty(value = "반품 단일조건 리스트")
    private List<String> orderStateSingleList;

    @ApiModelProperty(value = "공급업체")
    private String supplierId;

    @ApiModelProperty(value = "출고처그룹")
    private String warehouseGroup;

    @ApiModelProperty(value = "출고처ID")
    private String warehouseId;

    @ApiModelProperty(value = "엑셀 양식")
    private String psExcelTemplateId;
}