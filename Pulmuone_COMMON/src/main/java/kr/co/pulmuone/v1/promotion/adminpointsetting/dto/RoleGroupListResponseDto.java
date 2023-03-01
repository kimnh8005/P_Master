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
@ApiModel(description = "역할그룹 조회 ResponseDto")
public class RoleGroupListResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "역할그룹 리스트")
	private	List<AdminPointSettingVo> rows;

}
