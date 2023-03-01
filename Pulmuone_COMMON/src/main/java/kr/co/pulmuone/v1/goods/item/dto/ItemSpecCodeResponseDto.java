package kr.co.pulmuone.v1.goods.item.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "상품정보 제공고시 코드 목록 response dto")
public class ItemSpecCodeResponseDto {

    /*
     * 상품정보 제공고시 코드 목록 response dto
     */

    private List<GetCodeListResultVo> itemSpecCodeList;

}
