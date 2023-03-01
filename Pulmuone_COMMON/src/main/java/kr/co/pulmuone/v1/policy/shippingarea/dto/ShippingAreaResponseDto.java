package kr.co.pulmuone.v1.policy.shippingarea.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.policy.shippingarea.dto.vo.ShippingAreaResultVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "ShippingAreaResponseDto")
public class ShippingAreaResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "도서산간/배송불가 업로드 정보")
    private ShippingAreaResultVo rows;
}
