package kr.co.pulmuone.v1.customer.qna.dto;

import kr.co.pulmuone.v1.customer.qna.dto.vo.ProductQnaVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ProductQnaByGoodsResponseDto {
    private List<CodeInfoVo> list;
    private ProductQnaVo productQna;
}
