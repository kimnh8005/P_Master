package kr.co.pulmuone.v1.goods.item.dto;

import java.util.ArrayList;
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
public class ItemChangeLogListRequestDto extends BaseRequestPageDto {
	// 조회조건
	@ApiModelProperty(value = "검색조건")
	private String searchCondition;

	@ApiModelProperty(value = "단일_복수조건검색")
	private String selectConditionType;

	@ApiModelProperty(value = "")
	private String ilItemCodeStrFlag;

	@ApiModelProperty(value = "품목 검색 조건")
	private String ilItemCodeKind;

	@ApiModelProperty(value = "품목코드Array", required = false)
	private ArrayList<String> ilItemCodeArray;

	@ApiModelProperty(value = "품목 CODE")
	private String ilItemCode;

	@ApiModelProperty(value = "ERP 연동 여부")
	private String erpLinkIfYn;

	@ApiModelProperty(value = "품목명")
	private String itemName;

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
