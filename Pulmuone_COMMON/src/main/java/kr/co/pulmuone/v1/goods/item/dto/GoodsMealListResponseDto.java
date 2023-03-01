package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.GoodsMealVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealContsVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "일일상품 식단 등록 내역 리스트 조회 response dto")
public class GoodsMealListResponseDto extends BaseResponseDto {

    private List<GoodsMealVo> rows;
    private long total;
}
