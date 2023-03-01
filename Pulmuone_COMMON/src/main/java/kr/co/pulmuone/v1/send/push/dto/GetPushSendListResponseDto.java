package kr.co.pulmuone.v1.send.push.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.send.push.dto.vo.GetPushSendListResultVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel(description = "모바일 푸시발송이력 조회 Response")
public class GetPushSendListResponseDto extends BaseResponseDto{

    @ApiModelProperty(value = "모바일 푸시발송이력 조회 리스트")
	private	List<GetPushSendListResultVo>	rows;

    @ApiModelProperty(value = "모바일 푸시발송이력 조회 총 카운트")
	private	int	total;

    @Builder
	public GetPushSendListResponseDto(int total, List<GetPushSendListResultVo> rows) {
		this.total = total;
		this.rows = rows;
	}

}
