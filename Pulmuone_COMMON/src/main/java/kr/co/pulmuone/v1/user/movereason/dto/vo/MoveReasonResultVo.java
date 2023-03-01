package kr.co.pulmuone.v1.user.movereason.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "회원 탈퇴사유 Result Vo")
public class MoveReasonResultVo {

	@ApiModelProperty(value = "인덱스")
	private String urMoveReasonId;

	@ApiModelProperty(value = "사유상세설명")
	private String reasonName;

	@ApiModelProperty(value = "사용유무 (Y.사용 N.미사용)")
	private String useYn;


}
