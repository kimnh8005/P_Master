package kr.co.pulmuone.v1.comm.api.dto.basic;

import kr.co.pulmuone.v1.comm.ErpApiServiceTestConfig;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ErpIfStockSearchResponseDtoTest extends ErpApiServiceTestConfig {

    /*
     * ERP API 재고수량 조회 인터페이스 TestCase
     */

    @Test
    void ERP_API_재고수량조회_정상() {

        String interfaceId = "IF_STOCK_SRCH"; // ERP API 의 재고 수량 조회 인터페이스 ID
        String erpItemNo = "0933264"; // 품목 번호

        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("erpItmNo", erpItemNo);

        // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
        BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.get(parameterMap, interfaceId);

        // baseApiResponseVo => List<T> 역직렬화
        List<ErpIfStockSearchResponseDto> dtoList = baseApiResponseVo.deserialize(ErpIfStockSearchResponseDto.class);

        // 품목 코드 일치 여부 확인
        Assertions.assertTrue(dtoList.stream().allMatch(i -> i.getErpItemNo().equals(erpItemNo)));

    }

}
