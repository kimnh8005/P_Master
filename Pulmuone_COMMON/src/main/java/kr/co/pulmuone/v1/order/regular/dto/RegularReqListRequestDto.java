package kr.co.pulmuone.v1.order.regular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 신청 리스트 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 04.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 주문 신청 리스트 Request Dto")
public class RegularReqListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "단일조건 _ 복수조건 검색")
	private String selectConditionType;

    @ApiModelProperty(value = "신청일자 시작일자")
    private String dateSearchStart;

    @ApiModelProperty(value = "신청일자 종료일자")
    private String dateSearchEnd;

	@ApiModelProperty(value = "신청상세상태구분")
	private String regularReqDetailStatus;

    @ApiModelProperty(value = "신청상세상태구분 리스트")
    private List<String> regularReqDetailStatusList;

    @ApiModelProperty(value = "신청상태")
    private String regularReqStatus;

    @ApiModelProperty(value = "신청상태 리스트")
    private List<String> regularReqStatusList;

    @ApiModelProperty(value = "신청기간")
    private String regularTerm;

    @ApiModelProperty(value = "신청기간 리스트")
    private List<String> regularTermList;

    @ApiModelProperty(value = "회차")
    private String reqRound;

    @ApiModelProperty(value = "회차구분")
    private String reqRoundType;

    @ApiModelProperty(value = "유형")
    private String agentTypeCode;

    @ApiModelProperty(value = "유형 리스트")
    private List<String> agentTypeCodeList;

    @ApiModelProperty(value = "공급업체")
    private String supplierId;

    @ApiModelProperty(value = "출고처그룹")
    private String warehouseGroup;

    @ApiModelProperty(value = "출고처ID")
    private String warehouseId;

    @ApiModelProperty(value = "복수조건 검색조건")
	private String searchMultiType;

	@ApiModelProperty(value = "복수조건 검색어")
	private String findKeyword;

	@ApiModelProperty(value = "단일조건 검색어")
	private String codeSearch;

    @ApiModelProperty(value = "단일조건 검색어 리스트")
    private List<String> codeSearchList;

    @ApiModelProperty(value = "cs 사용자 조회 ID")
    private String urUserId;
}
