package kr.co.pulmuone.v1.base.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetShopInfoRequestDto")
public class GetShopInfoRequestDto extends BaseRequestDto{

}
