package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealPatternListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "식단 패턴 MealPatternListResponseDto")
public class MealPatternListResponseDto {

	 private long total;

	 private List<MealPatternListVo> rows;
}
