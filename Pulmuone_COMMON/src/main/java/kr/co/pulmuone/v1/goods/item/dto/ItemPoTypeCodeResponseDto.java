package kr.co.pulmuone.v1.goods.item.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoTypeCodeVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "해당 공급업체 PK 값에 해당하는 발주유형 코드 목록 조회 response dto")
public class ItemPoTypeCodeResponseDto {

    /*
     * 해당 공급업체 PK 값에 해당하는 발주유형 코드 목록 조회 response dto
     */

    private List<ItemPoTypeCodeVo> rows;

}
