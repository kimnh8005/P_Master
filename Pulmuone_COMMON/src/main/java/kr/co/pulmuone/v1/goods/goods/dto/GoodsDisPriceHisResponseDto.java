package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsDisPriceHisVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품 할인 업데이트 리스트 Response")
public class GoodsDisPriceHisResponseDto {

	/*
     * 상품 할인 업데이트 리스트 검색 결과 response dto
     */

    private List<GoodsDisPriceHisVo> rows;
    private long total;
}
