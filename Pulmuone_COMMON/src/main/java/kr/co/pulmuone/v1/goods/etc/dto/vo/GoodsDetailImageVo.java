package kr.co.pulmuone.v1.goods.etc.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품상세 이미지 관리 Vo")
public class GoodsDetailImageVo {

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "상품상세정보 수정여부")
    private String chgGoodsDetlYn;

    @ApiModelProperty(value = "상품고시정보 수정여부")
    private String chgSpceYn;

    @ApiModelProperty(value = "이미지 생성여부")
    private String imgGenYn;

    @ApiModelProperty(value = "품목별 상품군 사용유무")
    private boolean chkImgGenYn;

    @ApiModelProperty(value = "등록자")
    private String createId;

    @ApiModelProperty(value = "상품정보제공고시항목 PK")
    private String specificsFieldId;


}
