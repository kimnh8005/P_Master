package kr.co.pulmuone.v1.promotion.adminpointsetting.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.promotion.adminpointsetting.dto.vo.AdminPointSettingVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "관리자 적립금 한도 설정 조회 ResponseDto")
public class AdminPointSettingResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "관리자 적립금 한도 설정 조회 리스트")
	private	List<AdminPointSettingVo> rows;

	@ApiModelProperty(value = "관리자 적립금 한도 설정 조회 총 Count")
	private long total;

}
