package kr.co.pulmuone.v1.goods.item.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemCertificationListVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "품목별 상품 인증정보 목록 조회 response dto")
public class ItemCertificationListResponseDto {

    /*
     * 품목별 상품 인증정보 목록 조회 response dto
     */
    List<ItemCertificationListVo> itemCertificationList;

    /*
     * 상품 인증정보 변경여부
     */
    private boolean changedItemCertification;
}
