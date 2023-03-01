package kr.co.pulmuone.v1.goods.item.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemCertificationCodeVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "상품 인증정보 코드 조회 response dto")
public class ItemCertificationCodeResponseDto {

    /*
     * 상품 인증정보 코드 조회 response dto
     */
    List<ItemCertificationCodeVo> itemCertificationCodeList;

}
