package kr.co.pulmuone.v1.goods.item.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemImageVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "해당 품목의 품목코드로 등록된 품목 이미지 조회 response dto")
public class ItemImageResponseDto {

    /*
     * 해당 품목의 품목코드로 등록된 품목 이미지 조회 response dto
     */
    private List<ItemImageVo> itemImageList;
    private boolean changedItemImage;       // 상품이미지 변경여부
}
