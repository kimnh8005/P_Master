package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "해당 상품정보제공고시 항목 코드의 수정 메시지 조회 request dto")
public class ItemSpecFieldModifiedMessageRequestDto {

    /*
     * 해당 상품정보제공고시 항목 코드의 수정 메시지 조회 request dto
     */

    private String specFieldCode; // 상품정보 제공고시 항목 코드

    // 제조연월일/유통기한 또는 품질유지기한 ( SpecFieldCode.SPEC_FIELD_01 ) 관련 필드 Start

    /*
     * 마스터 품목 등록 / 수정시 : 화면에서 선택한 공급업체 PK, 유통기한 값
     */
    private String urSupplierId; // 공급업체 PK

    private int distributionPeriod; // 유통기한

    // 제조연월일/유통기한 또는 품질유지기한 ( SpecFieldCode.SPEC_FIELD_01 ) 관련 필드 End

}
