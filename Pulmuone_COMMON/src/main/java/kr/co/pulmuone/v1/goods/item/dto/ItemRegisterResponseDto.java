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
@ApiModel(description = "마스터 품목 등록 response dto")
public class ItemRegisterResponseDto {

    /*
     * 마스터 품목 등록 response dto
     */

    // ERP 연동 품목 등록시 : ERP 품목 검색시 조회된 품목 코드 세팅
    // ERP 미연동 품목 등록시 : BOS 자체 규칙에 따라 새로 생성된 품목 코드 세팅
    private String ilItemCode;

}
