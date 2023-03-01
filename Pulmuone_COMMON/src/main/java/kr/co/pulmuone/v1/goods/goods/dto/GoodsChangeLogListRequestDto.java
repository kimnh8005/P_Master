package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품 업데이트 내역 Request")
public class GoodsChangeLogListRequestDto extends BaseRequestPageDto {
	// 조회조건
	@ApiModelProperty(value = "검색조건")
	private String searchCondition;

	@ApiModelProperty(value = "단일_복수조건검색")
	private String selectConditionType;

	@ApiModelProperty(value = "검색어")
	private String findKeyword;

	@ApiModelProperty(value = "검색어 리스트")
	private List<String> findKeywordList;

	@ApiModelProperty(value = "검색어문자열여부")
	private String findKeywordStrFlag;

	@ApiModelProperty(value = "상품ID")
	private String ilGoodsId;

	@ApiModelProperty(value = "상품명")
	private String goodsName;

	@ApiModelProperty(value = "담당자 검색 조건")
	private String chargeType;

	@ApiModelProperty(value = "담당자명 or 담당자 LOGIN ID")
	private String charge;

	@ApiModelProperty(value = "기간검색 시작일자")
	private String dateSearchStart;

	@ApiModelProperty(value = "기간검색 종료일자")
	private String dateSearchEnd;

	@ApiModelProperty(value = "엑셀 양식")
	private String psExcelTemplateId;

	@ApiModelProperty(value = "업데이트 날짜")
	private String createDate;
}
