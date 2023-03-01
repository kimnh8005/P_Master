package kr.co.pulmuone.v1.api.Integratederp.order.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfDeliveryLineResponseDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfDeliveryRequestDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfDeliveryResponseDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfDlvConditionRequestDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfMissRequestDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfMissResponseDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfSalesRequestDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfSalesResponseDto;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfGoodsUpdateRequestDto;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.util.JsonUtil;

/**
 * <PRE>
 * Forbiz Korea
 * 주문|취소 ERP 연동
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200923   	강윤경     최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class OrderErpService {

	private static final Logger log = LoggerFactory.getLogger(OrderErpService.class);

    //ERP API 에서 가격 정보 조회 인터페이스 ID
    private static final String DELIVERY_SEARCH_INTERFACE_ID = "IF_DLV_SRCH";	//송장조회
    private static final String DELIVERY_FLAG_INTERFACE_ID = "IF_DLV_FLAG";	//송장조회 완료
    private static final String MISS_SEARCH_INTERFACE_ID = "IF_MIS_SRCH";	//미출조회
    private static final String MISS_FLAG_INTERFACE_ID = "IF_MIS_FLAG";	//미출조회 완료
    private static final String SALES_SEARCH_INTERFACE_ID = "IF_SAL_SRCH";	//매출 확정 내역 조회
    private static final String SALES_FLAG_INTERFACE_ID = "IF_SAL_FLAG";	//매출 확정 내역 조회 완료

    @Autowired
    ErpApiExchangeService erpApiExchangeService;


	/**
     * 주문|취소 입력 ERP 연동 IF_CUSTORD_INP
     * ERP 연동 후 결과값 return
	 * @return
	 */
	protected List<ErpIfDeliveryResponseDto> addIfCustordInpByErp() throws Exception {
		//주문 입력 - 용인출고 택배 주문 (풀무원식품, 올가, 푸드머스)

		//주문 입력 - 용인출고 새벽 주문 (풀무원식품, 푸드머스)

		//주문 입력 - CJ물류 출고 주문 (풀무원식품, 푸드머스)

		//주문 입력 - 베이비밀

		//주문 입력 - FD


        return null;
	}



	/**
     * 송장 조회 ERP 연동 IF_DLV_SRCH
     * ERP 연동 후 결과값 return
	 * @return
	 */
	protected List<ErpIfDeliveryResponseDto> getIfDlvSrchByErp(String ordNum) throws Exception {

		//Erp 데이터 param
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("ordNum", ordNum);

        // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
        BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.get(parameterMap, DELIVERY_SEARCH_INTERFACE_ID);

        // baseApiResponseVo => List<T> 역직렬화
        List<ErpIfDeliveryResponseDto> erpIfDeliveryResponseDto = baseApiResponseVo.deserialize(ErpIfDeliveryResponseDto.class);

        int totalPageCount = baseApiResponseVo.getTotalPage(); // 전체 페이지 수

        if (totalPageCount > 1) { // 전체 페이지 수가 1 보다 큰 경우
            for (int page = 2; page <= totalPageCount; page++) { // 2 페이지부터 전체 페이지 수까지 추가 조회
                parameterMap.put("page", String.valueOf(page)); // 페이지 조회조건 추가
                //배치데이터 받기
                baseApiResponseVo = erpApiExchangeService.get(parameterMap, DELIVERY_SEARCH_INTERFACE_ID);
                erpIfDeliveryResponseDto.addAll(baseApiResponseVo.deserialize(ErpIfDeliveryResponseDto.class));
            }
        }

        return erpIfDeliveryResponseDto;
	}


	/**
     * 송장 조회 완료 ERP 연동 IF_DLV_FLAG
     * ERP 연동 후 결과값 return
	 * @return
	 */
	protected BaseApiResponseVo putIfDlvFlagByErp(List<ErpIfDeliveryRequestDto> erpIfDeliveryRequestDto) throws Exception {
		// baseApiResponseVo : restTemplate 으로 해당 API 를 PUT 방식으로 호출 후 받은 응답 결과 객체
	    BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.put(erpIfDeliveryRequestDto, DELIVERY_FLAG_INTERFACE_ID);

	    log.info("baseApiResponseVo:" + ToStringBuilder.reflectionToString(baseApiResponseVo));

	    if (!baseApiResponseVo.isSuccess()) { // API 호출 오류 시
	        // 별도 확인 필요 : BOS 상에 품목 등록은 성공했으나, ERP API 상의 품목 조회 완료 업데이트 API 호출 실패 case
	        log.error(" API Call Failure");
	    }

        return baseApiResponseVo;
	}



	/**
     * 미출 조회 ERP 연동 IF_MIS_SRCH
     * ERP 연동 후 결과값 return
	 * @return
	 */
	protected List<ErpIfMissResponseDto> getIfMisSrchByErp(String ordNum) throws Exception {

		//Erp 데이터 param
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("ordNum", ordNum);

        // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
        BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.get(parameterMap, MISS_SEARCH_INTERFACE_ID);

        // baseApiResponseVo => List<T> 역직렬화
        List<ErpIfMissResponseDto> erpIfMissResponseDto = baseApiResponseVo.deserialize(ErpIfMissResponseDto.class);

        int totalPageCount = baseApiResponseVo.getTotalPage(); // 전체 페이지 수

        if (totalPageCount > 1) { // 전체 페이지 수가 1 보다 큰 경우
            for (int page = 2; page <= totalPageCount; page++) { // 2 페이지부터 전체 페이지 수까지 추가 조회
                parameterMap.put("page", String.valueOf(page)); // 페이지 조회조건 추가
                //배치데이터 받기
                baseApiResponseVo = erpApiExchangeService.get(parameterMap, MISS_SEARCH_INTERFACE_ID);
                erpIfMissResponseDto.addAll(baseApiResponseVo.deserialize(ErpIfMissResponseDto.class));
            }
        }

        return erpIfMissResponseDto;
	}





	/**
     * 미출 조회 완료 ERP 연동 IF_MIS_FLAG
     * ERP 연동 후 결과값 return
	 * @return
	 */
	protected BaseApiResponseVo putIfMisFlagByErp(List<ErpIfMissRequestDto> erpIfMissRequestDto) throws Exception {
		// baseApiResponseVo : restTemplate 으로 해당 API 를 PUT 방식으로 호출 후 받은 응답 결과 객체
	    BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.put(erpIfMissRequestDto, MISS_FLAG_INTERFACE_ID);

	    log.info("baseApiResponseVo:" + ToStringBuilder.reflectionToString(baseApiResponseVo));

	    if (!baseApiResponseVo.isSuccess()) { // API 호출 오류 시
	        // 별도 확인 필요 : BOS 상에 품목 등록은 성공했으나, ERP API 상의 품목 조회 완료 업데이트 API 호출 실패 case
	        log.error(" API Call Failure");
	    }

        return baseApiResponseVo;
	}






	/**
     * 매출 확정 내역 조회 ERP 연동 IF_SAL_SRCH
     * ERP 연동 후 결과값 return
	 * @return
	 */
	protected List<ErpIfSalesResponseDto> getIfSalSrchByErp(String ordNum) throws Exception {

		//Erp 데이터 param
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("ordNum", ordNum);

        // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
        BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.get(parameterMap, SALES_SEARCH_INTERFACE_ID);

        // baseApiResponseVo => List<T> 역직렬화
        List<ErpIfSalesResponseDto> erpIfSalesResponseDto = baseApiResponseVo.deserialize(ErpIfSalesResponseDto.class);

        int totalPageCount = baseApiResponseVo.getTotalPage(); // 전체 페이지 수

        if (totalPageCount > 1) { // 전체 페이지 수가 1 보다 큰 경우
            for (int page = 2; page <= totalPageCount; page++) { // 2 페이지부터 전체 페이지 수까지 추가 조회
                parameterMap.put("page", String.valueOf(page)); // 페이지 조회조건 추가
                //배치데이터 받기
                baseApiResponseVo = erpApiExchangeService.get(parameterMap, SALES_SEARCH_INTERFACE_ID);
                erpIfSalesResponseDto.addAll(baseApiResponseVo.deserialize(ErpIfSalesResponseDto.class));
            }
        }

        return erpIfSalesResponseDto;
	}






	/**
     * 매출 확정 내역 조회 완료 ERP 연동 IF_MIS_FLAG
     * ERP 연동 후 결과값 return
	 * @return
	 */
	protected BaseApiResponseVo putIfSalFlagByErp(List<ErpIfSalesRequestDto> erpIfSalesRequestDto) throws Exception {
		// baseApiResponseVo : restTemplate 으로 해당 API 를 PUT 방식으로 호출 후 받은 응답 결과 객체
	    BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.put(erpIfSalesRequestDto, SALES_FLAG_INTERFACE_ID);

	    log.info("baseApiResponseVo:" + ToStringBuilder.reflectionToString(baseApiResponseVo));

	    if (!baseApiResponseVo.isSuccess()) { // API 호출 오류 시
	        // 별도 확인 필요 : BOS 상에 품목 등록은 성공했으나, ERP API 상의 품목 조회 완료 업데이트 API 호출 실패 case
	        log.error(" API Call Failure");
	    }

        return baseApiResponseVo;
	}



}
