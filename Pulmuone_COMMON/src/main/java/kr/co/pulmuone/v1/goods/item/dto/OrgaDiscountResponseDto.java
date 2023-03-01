package kr.co.pulmuone.v1.goods.item.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.goods.item.dto.vo.OrgaDiscountListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "올가 할인 연동내역 검색 결과 response dto")
public class OrgaDiscountResponseDto {

	 /*
     * 올가 할인 연동 리스트 검색 결과 response dto
     */

    private List<OrgaDiscountListVo> rows;
    private long total;
}
