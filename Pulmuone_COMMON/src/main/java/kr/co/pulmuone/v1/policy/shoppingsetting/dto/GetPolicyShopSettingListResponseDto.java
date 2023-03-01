package kr.co.pulmuone.v1.policy.shoppingsetting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.policy.shoppingsetting.dto.vo.GetPolicyShopSettingListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@ToString
@ApiModel(description = "GetAuthMenuListResponseDto")
public class GetPolicyShopSettingListResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "리스트")
	private List<GetPolicyShopSettingListResultVo> rows;

}
