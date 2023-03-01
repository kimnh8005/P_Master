package kr.co.pulmuone.v1.comm.api.dto.basic;

import kr.co.pulmuone.v1.comm.ErpApiServiceTestConfig;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ErpIfGoodSearchResponseDtoTest extends ErpApiServiceTestConfig {

    /*
     * ERP API 품목정보 조회 인터페이스 TestCase
     */

    @Test
    void ERP_API_품목정보조회_정상() {

        String interfaceId = "IF_GOODS_SRCH"; // ERP API 의 품목정보 조회 인터페이스 ID
        String erpItemNo = "0059048"; // 품목 번호

        Map<String, String> parameterMap = new HashMap<>();

        parameterMap.put("itmNo", erpItemNo);

        // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
        BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.get(parameterMap, interfaceId);

        // baseApiResponseVo => List<T> 역직렬화
        List<ErpIfGoodSearchResponseDto> dtoList = baseApiResponseVo.deserialize(ErpIfGoodSearchResponseDto.class);

        // 품목 코드 일치 여부 확인
        Assertions.assertTrue(dtoList.stream().allMatch(i -> i.getErpItemNo().equals(erpItemNo)));

    }

}
