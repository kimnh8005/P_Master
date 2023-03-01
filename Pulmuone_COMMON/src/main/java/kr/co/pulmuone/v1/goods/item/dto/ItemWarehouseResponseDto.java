package kr.co.pulmuone.v1.goods.item.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemWarehouseVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@ApiModel(description = "품목별 출고처 정보 조회 Response dto")
public class ItemWarehouseResponseDto {

    /*
     * 품목별 출고처 정보 조회 Response dto
     */

    private List<ItemWarehouseVo> itemWarehouseList;

}
