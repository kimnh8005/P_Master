package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.exhibit.dto.GiftListResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@ApiModel(description = "주문서 정보 조회 응답 DTO")
public class GiftPageInfoResponseDto {

    @ApiModelProperty(value = "증정 행사 여부")
    private Boolean isGift;

    @ApiModelProperty(value = "증정 행사 정보")
    private List<GiftListResponseDto> gift;

}
