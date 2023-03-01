package kr.co.pulmuone.v1.system.shopconfig.dto.basic;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.shopconfig.dto.vo.GetSystemShopConfigListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = " GetSystemShopConfigListResponseDto")
public class GetSystemShopConfigListResponseDto extends BaseResponseDto {

    private List<GetSystemShopConfigListResultVo> rows = new ArrayList<GetSystemShopConfigListResultVo>();
    private long total;

}
