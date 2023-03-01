package kr.co.pulmuone.v1.store.shop.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.store.shop.dto.vo.ShopListVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class ShopListResponseDto {

    @ApiModelProperty(value = "개수")
    private int total;

    @ApiModelProperty(value = "매장리스트")
    private List<ShopListVo> list;

}
