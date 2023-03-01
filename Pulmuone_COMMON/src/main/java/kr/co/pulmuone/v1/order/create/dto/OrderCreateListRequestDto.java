package kr.co.pulmuone.v1.order.create.dto;


import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 생성 관련 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문 생성 내역 Dto")
public class OrderCreateListRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "기간검색 시작일")
	private String startDate;

	@ApiModelProperty(value = "기간검색 종료일")
	private String endDate;

	@ApiModelProperty(value = "생성구분")
	private String orderCreateType;

	@ApiModelProperty(value = "상태")
	private String createStatus;

	@ApiModelProperty(value = "상품코드 순으로 정렬")
	private String sort;

	@ApiModelProperty(value = "상품코드검색 타입")
	private String keywordType;

	@ApiModelProperty(value = "상품코드검색값")
	private String keywordVal;

	@ApiModelProperty(value = "주문생성정보 PK")
	private String odCreateInfoId;

	@ApiModelProperty(value = "주문리스트 검색용 ,로 구분")
	private String odid;

	@ApiModelProperty(value = "주문번호 리스트")
	List<String> findOdIdList;
}
