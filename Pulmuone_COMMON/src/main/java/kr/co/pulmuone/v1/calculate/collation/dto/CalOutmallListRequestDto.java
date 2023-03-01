package kr.co.pulmuone.v1.calculate.collation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.*;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 외부몰 주문 대사 조회 Request Dto
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
@ApiModel(description = "외부몰 주문 대사 조회 Request Dto")
public class CalOutmallListRequestDto extends BaseRequestPageDto {


    @ApiModelProperty(value = "기간 조건")
    private String dateSearchType;

    @ApiModelProperty(value = "기간 시작일")
    private String dateSearchStart;

    @ApiModelProperty(value = "기간 종료일")
    private String dateSearchEnd;

    @ApiModelProperty(value = "사용자 ID")
    private String grantAuthEmployeeNumber;

    @ApiModelProperty(value = "판매처")
    private String sellersGroup;

    @ApiModelProperty(value = "판매처")
    private String omSellersId;

    @ApiModelProperty(value = "판매처 리스트")
    private List<String> omSellersIdList;

    @ApiModelProperty(value = "매칭여부")
    private String findOutmallMatchingType;

    @ApiModelProperty(value = "검색어 조건")
    private String outmallSearchMultiType;

    @ApiModelProperty(value = "검색어 키워드")
    private String findKeyword;

    @ApiModelProperty(value = "공급업체")
    private String supplierId;

    @ApiModelProperty(value = "출고처 그룹")
    private String warehouseGroup;

    @ApiModelProperty(value = "출고처")
    private String warehouseId;

    @ApiModelProperty(value = "주문번호 조회")
    private String findOdid;

    @ApiModelProperty(value = "원본 파일명")
    private String originNm;

    @ApiModelProperty(value = "등록자ID")
	private Long createId;

    @ApiModelProperty(value = "외부몰 주문 업로드 상세ID")
	private Long odOutMallCompareUploadInfoId;


    @ApiModelProperty(value = "구분 체크 값")
    private List<String> salesDeliveryGubunList;

    @ApiModelProperty(value = "성공 회수")
	private int successCnt;

    @ApiModelProperty(value = "실패 회수")
	private int failCnt;

    @ApiModelProperty(value = "엑셀여부")
    private String excelYn;

}
