package kr.co.pulmuone.v1.calculate.collation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.*;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * PG 거래 내역 대사 조회 Request Dto
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
@ApiModel(description = "PG 거래 내역 대사 조회 Request Dto")
public class CalPgListRequestDto extends BaseRequestPageDto {


    @ApiModelProperty(value = "기간 조건")
    private String dateSearchType;

    @ApiModelProperty(value = "기간 시작일")
    private String dateSearchStart;

    @ApiModelProperty(value = "기간 종료일")
    private String dateSearchEnd;

    @ApiModelProperty(value = "판매처")
    private String sellersGroup;

    @ApiModelProperty(value = "구분")
    private String salesDeliveryGubun;

    @ApiModelProperty(value = "물류비 정산여부")
    private String logisticsCost;

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

    @ApiModelProperty(value = "PG 거래내역 업로드 정보PK")
	private Long odPgCompareUploadInfoId;


    @ApiModelProperty(value = "구분 체크 값")
    private List<String> salesDeliveryGubunList;

    @ApiModelProperty(value = "성공 회수")
	private int successCnt;

    @ApiModelProperty(value = "실패 회수")
	private int failCnt;

    @ApiModelProperty(value = "엑셀업로드 구분")
    private String pgUploadGubun;

    @ApiModelProperty(value = "관리자번호")
    private String grantAuthEmployeeNumber;


    @ApiModelProperty(value = "엑셀업로드 구분")
    private String findPgGubn;


}
