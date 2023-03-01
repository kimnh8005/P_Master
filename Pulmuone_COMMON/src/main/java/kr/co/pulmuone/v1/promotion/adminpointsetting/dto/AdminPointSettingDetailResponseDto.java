package kr.co.pulmuone.v1.promotion.adminpointsetting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.adminpointsetting.dto.vo.AdminPointSettingVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "관리자 적립금 지급 한도 설정 상세 조회 ResponseDto")
public class AdminPointSettingDetailResponseDto {

	@ApiModelProperty(value = "관리자 적립금 지급 한도 설정 상세 Vo")
	private	AdminPointSettingVo rows;

}
