package kr.co.pulmuone.v1.goods.item.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.goods.item.dto.vo.AvailableNutritionVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemNutritionDetailVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "마스터 품목 관리 화면에서 상품 영양정보 코드 조회 & EPR 미연동 마스터 품목 복사시 영양정보 세부 항목 ResponseDto")
public class AvailableNutritionCodeResponseDto {

    /*
     * 마스터 품목 관리 화면에서 상품 영양정보 코드 조회 & EPR 미연동 마스터 품목 복사시 영양정보 세부 항목 ResponseDto
     */

    private List<AvailableNutritionVo> addAvailableNutritionCodeList; // 등록 가능한 영양정보 분류 코드 목록

    private List<ItemNutritionDetailVo> itemNutritionDetailList; // BOS 상에 해당 품목코드로 등록된 영양정보 세부 항목

    private boolean changedItemNutrition; // 상품영양정보 변경여부
}
