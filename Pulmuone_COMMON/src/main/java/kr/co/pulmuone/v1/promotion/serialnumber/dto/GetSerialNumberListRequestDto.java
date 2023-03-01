package kr.co.pulmuone.v1.promotion.serialnumber.dto;

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
@ApiModel(description = " GetClauseRequestDto")
public class GetSerialNumberListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "검색조건")
	private String serialNumberSearchCondition;

	@ApiModelProperty(value = "조회 데이터")
	private String inputSearchValue;

	@ApiModelProperty(value = "이용권 사용여부")
	private String serialNumberUseCondition;

	@ApiModelProperty(value = "사용일시 조회 시작 일자")
	private String startUseDate;

	@ApiModelProperty(value = "사용일시 조회 종료 일자")
	private String endUseDate;

	@ApiModelProperty(value = "조회 데이터 리스트")
	private List<String> searchValueList;

	@ApiModelProperty(value = "조회 PK (쿠폰 || 포인트)")
	private String parentId;

	@ApiModelProperty(value = "이용권 타입")
	private String useType;

	@ApiModelProperty(value = "이용권다운로드 엑셀 구분")
	private String ticketDownYn;

	@ApiModelProperty(value = "쿠폰/적립금명")
	private String displayName;

	@ApiModelProperty(value = "난수번호 타입")
	private String serialNumberList;

	@ApiModelProperty(value = "난수번호 타입")
	private String serialNumberTp;

}