package kr.co.pulmuone.v1.system.basic.dto.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetUrGroupResultResultVo implements Serializable {
	private static final long serialVersionUID = 6776497017308089547L;
	
	@ApiModelProperty(value = "그룹 아이디")
	private String groupId;
	@ApiModelProperty(value = "그룹 마스터 아이디")
	private String urGroupMasterId;
	@ApiModelProperty(value = "그룹 레벨")
	private String groupLevelType;
	@ApiModelProperty(value = "그룹명")
	private String groupName;
	@ApiModelProperty(value = "구매금액From")
	private String purchaseAmountFrom;
	@ApiModelProperty(value = "구매수량From")
	private String purchaseCountFrom;
	@ApiModelProperty(value = "계산 기간")
	private String calculatePeriod;
	@ApiModelProperty(value = "기본값")
	private String defaultYn;

}
