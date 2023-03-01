package kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "이지어드민 API 변경 정보")
public class EZAdminInfoDto{

	@ApiModelProperty(value = "이지어드민정보PK")
	private long ifEasyadminInfoId;

	@ApiModelProperty(value = "배치시작일자")
	private String batchStartDt;

	@ApiModelProperty(value = "배치종료일자")
	private String batchEndDt;

	@ApiModelProperty(value = "배치처리시간(초단위)")
	private long batchExecTime;

	@ApiModelProperty(value = "정상주문건수")
	private int succCnt;

	@ApiModelProperty(value = "실패주문건수")
	private int failCnt;

	@ApiModelProperty(value = "배치연동상태")
	private String syncCd;

	@ApiModelProperty(value = "처리상태")
	private String processCd;

	@ApiModelProperty(value = "주문 생성 건수")
	private int orderCreateCnt;
}
