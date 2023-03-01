package kr.co.pulmuone.v1.goods.item.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemWarehouseCodeVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "출고처 코드 정보 reponse dto")
public class ItemWarehouseCodeReponseDto {

    /*
     * 출고처 코드 정보 reponse dto
     */
    List<ItemWarehouseCodeVo> itemWarehouseCodeList; // 해당 품목코드의 공급업체 PK 에 해당하는 출고처 그룹, 출고처 정보 목록

    List<GetCodeListResultVo> warehouseGroupCodeList; // 해당 품목코드의 공급업체 PK 에 해당하는 출고처 그룹 코드 목록

}
