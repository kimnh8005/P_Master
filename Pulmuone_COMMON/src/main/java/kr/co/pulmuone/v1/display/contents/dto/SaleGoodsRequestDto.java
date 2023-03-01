package kr.co.pulmuone.v1.display.contents.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SaleGoodsRequestDto extends MallBaseRequestPageDto {

    @ApiModelProperty(value = "할인율 검색 구간 - 시작")
    private int discountRateStart;

    @ApiModelProperty(value = "할인율 검색 종료 - 종료")
    private int discountRateEnd;

    @ApiModelProperty(value = "일일배송 조회 구분")
    private String mallDiv;

    @ApiModelProperty(value = "카테고리 PK")
    private String ilCtgryId;

    @ApiModelProperty(value = "상품 정렬 코드")
    private String goodsSortCode;

}
