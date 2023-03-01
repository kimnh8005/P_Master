package kr.co.pulmuone.v1.policy.shippingarea.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.policy.shippingarea.dto.vo.ShippingAreaResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ShippingAreaListResponseDto")
public class ShippingAreaListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "도서산간/배송불가 업로드 목록")
    private List<ShippingAreaResultVo> rows;

    @ApiModelProperty(value = "도서산간/배송불가 업로드 카운트 수")
    private int total;

}
