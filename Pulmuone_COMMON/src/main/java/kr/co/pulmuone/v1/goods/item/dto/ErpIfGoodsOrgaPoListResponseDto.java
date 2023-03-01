package kr.co.pulmuone.v1.goods.item.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfGoodsOrgaPoResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "품목코드로 올가공급처 발주유형 List API")
public class ErpIfGoodsOrgaPoListResponseDto {

    /*
     * 품목코드로 올가공급처 발주유형 List API
     */

    // ERP API 가격조회 인터페이스로 조회한 품목 가격정보 목록
    List<ErpIfGoodsOrgaPoResponseDto> erpIfGoodsOrgaPoListDto;

}
