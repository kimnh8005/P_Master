package kr.co.pulmuone.v1.goods.discount.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.goods.discount.dto.vo.GoodsDiscountUploadListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품 할인 일괄 업로드  리스트 Response")
public class GoodsDiscountUploadReponseDto {

	/*
     * 상품 할인 업데이트 리스트 검색 결과 response dto
     */

    private List<GoodsDiscountUploadListVo> rows;
    private long total;
}
