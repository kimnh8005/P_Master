package kr.co.pulmuone.v1.user.movereason.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "회원탈퇴사유 Request dto")
public class MoveReasonRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "인덱스")
    private String urMoveReasonId;

	@ApiModelProperty(value = "사유상세설명")
	private String reasonName;

	@ApiModelProperty(value = "사용유무 (Y.사용 N.미사용)")
	private String useYn;

	@ApiModelProperty(value = "공통코드(사유: 1.활동->탈퇴)")
	private String moveType;

}
