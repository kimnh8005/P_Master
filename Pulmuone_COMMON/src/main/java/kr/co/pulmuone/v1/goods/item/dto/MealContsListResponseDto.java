package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
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
@ApiModel(description = "식단 컨텐츠 조회결과 response dto")
public class MealContsListResponseDto {

    private List<MealContsVo> rows;
    private long total;
}
