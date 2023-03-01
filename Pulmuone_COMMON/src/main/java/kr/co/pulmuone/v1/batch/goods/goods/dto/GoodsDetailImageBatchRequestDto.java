package kr.co.pulmuone.v1.batch.goods.goods.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품 상세 이미지 배치 정보")
public class GoodsDetailImageBatchRequestDto {

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "이미지 파일 명")
    private String pcImgNm ;

}
