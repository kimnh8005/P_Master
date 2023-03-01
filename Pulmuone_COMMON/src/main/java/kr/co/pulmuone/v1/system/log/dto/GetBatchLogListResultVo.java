package kr.co.pulmuone.v1.system.log.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetBatchLogListResultVo")
public class GetBatchLogListResultVo {

	@ApiModelProperty(value = "배치명")
	String batchName;

	@ApiModelProperty(value = "설명")
	String description;

	@ApiModelProperty(value = "실행Class")
	String jobClassFullPath;

	@ApiModelProperty(value = "스케쥴")
	String schedule;

	@ApiModelProperty(value = "배치시작시간")
	String startTime;

	@ApiModelProperty(value = "배치종료시간")
	String endTime;

	@ApiModelProperty(value = "배치결과")
	String status;

	@ApiModelProperty(value = "오류메시지")
	String errorMessage;

}
