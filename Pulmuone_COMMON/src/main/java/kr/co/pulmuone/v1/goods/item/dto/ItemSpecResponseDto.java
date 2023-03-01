package kr.co.pulmuone.v1.goods.item.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemSpecValueVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "상품정보 제공고시 response dto")
public class ItemSpecResponseDto {

    /*
     * 상품정보 제공고시 response dto
     */

    List<ItemSpecDto> itemSpecList; // 상품정보 제공고시 분류 / 항목 조회 목록

    List<ItemSpecValueVo> itemSpecValueList; // 품목별 상품정보제공고시 세부 항목 조회 목록

    /*
     * 상품정보 제공고시 상세 메시지 관련
     */
    String itemSpecModifiedMessage; // 화면에 출력할 값으로 치환된 상품정보 제공고시 상세 메시지

    private boolean changedSpecMaster; // 상품정보 제공고시 변경 확인

}
