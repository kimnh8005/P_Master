package kr.co.pulmuone.v1.calculate.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.*;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 상품 정산 조회 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 05.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "상품 정산 조회 Request Dto")
public class CalGoodsListRequestDto extends BaseRequestPageDto {


    @ApiModelProperty(value = "기간 조건")
    private String dateSearchType;

    @ApiModelProperty(value = "기간 시작일")
    private String dateSearchStart;

    @ApiModelProperty(value = "기간 종료일")
    private String dateSearchEnd;

    @ApiModelProperty(value = "판매처")
    private String sellersGroup;

    @ApiModelProperty(value = "판매처")
    private String omSellersId;

    @ApiModelProperty(value = "판매처 리스트")
    private List<String> omSellersIdList;

    @ApiModelProperty(value = "구분")
    private String salesGubun;

    @ApiModelProperty(value = "통합 ERP I/F 여부")
    private String erpSendYn;

    @ApiModelProperty(value = "상품유형")
    private String goodsType;

    @ApiModelProperty(value = "결제수단")
    private String paymentMethodCode;

    @ApiModelProperty(value = "공급업체")
    private String supplierId;

    @ApiModelProperty(value = "출고처 그룹")
    private String warehouseGroup;

    @ApiModelProperty(value = "출고처")
    private String warehouseId;

    @ApiModelProperty(value = "매장PK")
    private String urStoreId;

    @ApiModelProperty(value = "검색조건")
    private String searchMultiType;

    @ApiModelProperty(value = "검색어")
    private String findKeyword;


    @ApiModelProperty(value = "구분 체크 값")
    private List<String> salesGubunList;

    @ApiModelProperty(value = "상품유형 체크 값")
    private List<String> goodsTypeList;

    @ApiModelProperty(value = "결제수단 체크 값")
    private List<String> paymentMethodCodeList;


    @ApiModelProperty(value = "엑셀여부")
    private String excelYn;

}
