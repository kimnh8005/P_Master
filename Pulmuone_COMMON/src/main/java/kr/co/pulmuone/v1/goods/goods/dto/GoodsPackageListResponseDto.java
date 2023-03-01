package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsPackageListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "묶음 상품리스트 Response")
public class GoodsPackageListResponseDto {

	/*
     * 올가 할인 연동 리스트 검색 결과 response dto
     */

    private List<GoodsPackageListVo> rows;
    private long total;
}
