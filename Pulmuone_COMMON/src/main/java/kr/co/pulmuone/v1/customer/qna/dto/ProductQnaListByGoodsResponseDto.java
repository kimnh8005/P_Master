package kr.co.pulmuone.v1.customer.qna.dto;

import kr.co.pulmuone.v1.customer.qna.dto.vo.ProductQnaListByGoodsVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class ProductQnaListByGoodsResponseDto {
    private int total;
    private List<ProductQnaListByGoodsVo> rows;
}
