package kr.co.pulmuone.v1.goods.item.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemDiscountVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "해당 품목의 ERP 가격정보조회")
public class ItemDiscountResponseDto {

	/*
     * 해당 품목의 가격정보 조회 response dto
     */

    // 해당 품목의 가격정보 전체 이력 목록
    private List<ItemDiscountVo> rows;

    // 해당 품목의 가격정보 전체 이력 개수
    private long total;
}
