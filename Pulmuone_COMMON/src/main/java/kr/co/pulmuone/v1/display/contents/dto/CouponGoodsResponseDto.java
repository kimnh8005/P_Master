package kr.co.pulmuone.v1.display.contents.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponInfoByUserJoinVo;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import kr.co.pulmuone.v1.search.searcher.dto.SearchResultDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@ApiModel(description = "전시 신규회원 용 쿠폰 상품 정보 ResponseDto")
public class CouponGoodsResponseDto {

    @ApiModelProperty(value = "할인값")
    private String discountValue;

    @ApiModelProperty(value = "상품 정보")
    private GoodsSearchResultDto goods;

}
