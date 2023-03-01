package kr.co.pulmuone.v1.user.movereason.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.user.movereason.dto.vo.MoveReasonResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "회원탈퇴사유 Responsedto")
public class MoveReasonResponseDto {

	@ApiModelProperty(value = "결과값 row")
	private	List<MoveReasonResultVo> rows;

	@ApiModelProperty(value = "총 count")
	private long total;

	@ApiModelProperty(value = "탈퇴사유 설정 Vo")
	private MoveReasonResultVo moveReasonResultVo;


}
