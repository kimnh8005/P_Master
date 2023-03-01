package kr.co.pulmuone.v1.comm.api.dto.basic;

import kr.co.pulmuone.v1.comm.ErpApiServiceTestConfig;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.util.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErpIfGoodsOrgaPoResponseDtoTest extends ErpApiServiceTestConfig {

	/*
     * ERP API 품목정보 조회 인터페이스 TestCase
     */

    @Test
    void ERP_API_올가공급업체_발주정보조회() {

        String interfaceId = "IF_PURCHASESCH_SRCH"; // ERP API 의 품목정보 조회 인터페이스 ID
        String erpItemNo = "0932436"; // 품목 번호

        Map<String, String> parameterMap = new HashMap<>();

        parameterMap.put("itmNo", erpItemNo);

        List<ErpIfGoodsOrgaPoResponseDto> eachPageDtoList = null; // 각 페이지별 품목 dto 목록
        List<ErpIfGoodsOrgaPoResponseDto> erpItemApiList = new ArrayList<>(); // 전체 취합된 품목 dto 목록

        // 최초 1페이지 조회
        // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
        BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.get(parameterMap, interfaceId);

        eachPageDtoList = baseApiResponseVo.deserialize(ErpIfGoodsOrgaPoResponseDto.class); // baseApiResponseVo => List<T> 역직렬화
        erpItemApiList.addAll(eachPageDtoList);

        if (baseApiResponseVo.getTotalPage() != null && baseApiResponseVo.getTotalPage() > 1) { // 전체 페이지 수가 1 보다 큰 경우

        	for (int page = 2; page <= baseApiResponseVo.getTotalPage(); page++) { // 2 페이지부터 전체 페이지 수까지 추가 조회

        		parameterMap.put("page", String.valueOf(page)); // 페이지 조회조건 추가

                baseApiResponseVo = erpApiExchangeService.get(parameterMap, interfaceId);
                eachPageDtoList = baseApiResponseVo.deserialize(ErpIfGoodsOrgaPoResponseDto.class); // 각 페이지별 dto 변환
                erpItemApiList.addAll(eachPageDtoList); // 전체 품목 dto 목록에 취합

        	}
        }

        System.out.println("### json Result --->"+ JsonUtil.serializeWithPrettyPrinting(erpItemApiList));

        // 품목 코드 일치 여부 확인
        Assertions.assertTrue(erpItemApiList.size() > 0);

    }
}
