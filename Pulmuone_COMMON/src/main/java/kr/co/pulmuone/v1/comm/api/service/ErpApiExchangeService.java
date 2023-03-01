package kr.co.pulmuone.v1.comm.api.service;

import java.util.List;
import java.util.Map;

import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;

public interface ErpApiExchangeService {

    /*
     * ERP API 대상 GET 방식의 데이터 조회 ( restTemplate 활용 )
     */
    public BaseApiResponseVo exchange(Map<String, String> parameterMap, String interfaceId);

    /*
     * 상단 exchange 메서드를 추후 get 으로 변경하고 기존 exchange 메서드는 삭제 예정
     *
     * 현재 기존 exchange 메서드를 사용하는 곳이 많아 동일한 내용으로 get 메서드 우선 등록
     */

    /*
     * ERP API 대상 GET 방식의 데이터 조회 ( restTemplate 활용 )
     */
    public BaseApiResponseVo get(Map<String, String> parameterMap, String interfaceId);

    /*
     * ERP API 대상 POST 방식의 데이터 조회 ( restTemplate 활용 )
     */
    public BaseApiResponseVo post(List<?> paramList, String interfaceId);

    /*
     * ERP API 대상 POST 방식의 데이터 조회 ( restTemplate 활용 )
     */
    public BaseApiResponseVo post(Object obj, String interfaceId);

    /*
     * ERP API 대상 PUT 방식의 데이터 조회 ( restTemplate 활용 )
     */
    public BaseApiResponseVo put(List<?> paramList, String interfaceId);

    /*
     * ERP API 대상 PUT 방식의 데이터 조회 ( restTemplate 활용 )
     */
    public BaseApiResponseVo put(Object obj, String interfaceId);
}
