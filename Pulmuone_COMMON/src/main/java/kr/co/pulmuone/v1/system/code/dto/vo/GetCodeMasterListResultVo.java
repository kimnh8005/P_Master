package kr.co.pulmuone.v1.system.code.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetCodeMasterListResultVo")
public class GetCodeMasterListResultVo {

	@ApiModelProperty(value = "번호")
	private int no;

	@ApiModelProperty(value = "공통 코드 마스터 시퀀스 아이디")
	private String stComnCodeMstId;

	@ApiModelProperty(value = "공통 마스터명")
	private String commonMasterName;

	@ApiModelProperty(value = "공통 마스터 코드")
	private String commonMasterCode;

	@ApiModelProperty(value = "설명")
	private String comment;

	@ApiModelProperty(value = "사용여부")
	private String useYn;
}
