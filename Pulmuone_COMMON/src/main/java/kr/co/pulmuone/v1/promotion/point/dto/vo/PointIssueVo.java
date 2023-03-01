package kr.co.pulmuone.v1.promotion.point.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@ApiModel(description = "PointIsuueVo 포인트 발급 정보")
public class PointIssueVo {

	@ApiModelProperty(value = "적립금 발급 고유값")
	private Long pmPointIssueId;

	@ApiModelProperty(value = "적립금 고유값")
	private Long pmPointId;

	@ApiModelProperty(value = "회원 고유값")
	private Long urUserId;

}
