package kr.co.pulmuone.v1.goods.item.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.goods.item.dto.vo.MasterItemListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "마스터 품목 리스트 검색 결과 response dto")
public class MasterItemListResponseDto {

    /*
     * 마스터 품목 리스트 검색 결과 response dto
     */

    private List<MasterItemListVo> rows;
    private long total;

}
