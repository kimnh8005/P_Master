package kr.co.pulmuone.v1.policy.shoppingsetting.dto;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;

import kr.co.pulmuone.v1.policy.shoppingsetting.dto.vo.GetPolicyShopSettingListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
@ApiModel(description = " GetShopConfigListResponseDto")
public class PutPolicyShopSettingResponseDto extends BaseResponseDto {

    private List<GetPolicyShopSettingListResultVo> rows = new ArrayList<GetPolicyShopSettingListResultVo>();
    private int total;

}
