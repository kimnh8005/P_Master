package kr.co.pulmuone.v1.goods.item.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfPriceSearchResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "해당 EPR 연동 품목의 품목 가격정보 ( 올가 행사가격 ) 목록 조회 response dto")
public class ErpIfPriceListResponseDto {

    /*
     * 해당 EPR 연동 품목의 품목 가격정보 ( 올가 행사가격 ) 목록 조회 response dto
     */

    // ERP API 가격조회 인터페이스로 조회한 품목 가격정보 목록
    List<ErpIfPriceSearchResponseDto> erpIfPriceSearchResponseDtoList;

}
