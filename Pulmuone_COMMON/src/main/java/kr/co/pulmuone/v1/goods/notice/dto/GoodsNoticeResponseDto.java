package kr.co.pulmuone.v1.goods.notice.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.notice.dto.vo.GoodsNoticeVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoodsNoticeResponseDto {

    @ApiModelProperty(value = "상품 공통 공지 구분 코드")
    private String goodsNoticeType;

    @ApiModelProperty(value = "공지제목")
    private String noticeName;

    @ApiModelProperty(value = "공지 상세")
    private String detailDescription;

    public GoodsNoticeResponseDto(GoodsNoticeVo vo) {
        this.goodsNoticeType = vo.getGoodsNoticeTp();
        this.noticeName = vo.getNoticeNm();
        this.detailDescription = vo.getDetlDesc();
    }
}
