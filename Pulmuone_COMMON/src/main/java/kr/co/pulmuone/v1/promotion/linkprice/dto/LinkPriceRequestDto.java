package kr.co.pulmuone.v1.promotion.linkprice.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LinkPriceRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "결제/입금기간 검색시작일자")
    private String startDt;

	@ApiModelProperty(value = "결제/입금기간 검색종료일자")
	private String endDt;

	@ApiModelProperty(value = "상태구분")
	private String statusGubun;

	@ApiModelProperty(value = "정산대상")
	private String calculateObjectType;

	@ApiModelProperty(value = "다운로드유형")
	private String downType;
}
