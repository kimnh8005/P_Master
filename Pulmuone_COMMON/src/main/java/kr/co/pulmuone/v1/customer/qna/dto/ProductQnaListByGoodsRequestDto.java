package kr.co.pulmuone.v1.customer.qna.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductQnaListByGoodsRequestDto extends MallBaseRequestPageDto {
    @ApiModelProperty(value = "발급회원코드", hidden = true)
    private Long urUserId;

    @ApiModelProperty(value = "상품 PK", required = true)
    private Long ilGoodsId;

    @ApiModelProperty(value = "나의 문의 여부")
    private String myQnaYn;
}
