package kr.co.pulmuone.v1.goods.goods.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.goods.etc.dto.vo.GoodsDetailImageVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "상품 상세 이미지 다운로드 리스트 response")
public class GoodsDetailImageListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = " 리스트")
    List<GoodsDetailImageVo> goodsDetailImageVoList;

    @ApiModelProperty(value = " 리스트")
    List<GoodsDetailImageDto> goodsDetailImageDtoList;

    @ApiModelProperty(value = "상품 상세 이미지 다운로드 리스트")
    private	List<GoodsDetailImageDto> rows;

    @ApiModelProperty(value = "상품 상세 이미지 다운로드 리스트 카운트")
    private	long total;

}
