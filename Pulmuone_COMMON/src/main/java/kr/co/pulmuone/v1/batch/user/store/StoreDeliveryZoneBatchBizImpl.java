package kr.co.pulmuone.v1.batch.user.store;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.batch.user.store.dto.ErpIfDeliveryZoneRequestDto;
import kr.co.pulmuone.v1.batch.user.store.dto.vo.StoreDeliveryAreaVo;
import kr.co.pulmuone.v1.batch.user.store.dto.vo.StoreDeliveryZoneVo;
import kr.co.pulmuone.v1.batch.user.store.dto.vo.StoreInfoVo;
import kr.co.pulmuone.v1.batch.user.store.dto.vo.StoreScheduleVo;
import kr.co.pulmuone.v1.batch.user.store.dto.vo.StoreUndeliveryDateVo;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.enums.StoreEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;

@Service
public class StoreDeliveryZoneBatchBizImpl implements StoreDeliveryZoneBatchBiz {

	@Autowired
    private StoreDeliveryZoneBatchService storeDeliveryZoneBatchService;

    @Autowired
    private ErpApiExchangeService erpApiExchangeService;

    // ERP API 의 매장정보 조회 인터페이스 ID
    private static final String STORE_SEARCH_INTERFACE_ID = "IF_STORE_SRCH";

	// ERP API 의 매장/가맹점 배송권역정보 조회 인터페이스 ID
    private static final String STORE_DELIVERY_ZONE_SEARCH_INTERFACE_ID = "IF_STORE_DELIVER_SRCH";

    // ERP API 의 매장주문시간 조회 인터페이스 ID
    private static final String STORE_ORDTIME_SEARCH_INTERFACE_ID = "IF_STORE_ORDTIME_SRCH";

	// ERP API 의 배송권역정보 조회 인터페이스 ID
    private static final String DELIVERY_ZONE_SEARCH_INTERFACE_ID = "IF_DLVZONE_SRCH";

    // ERP API 의 매장휴무일정보 조회 인터페이스 ID
    private static final String HOLIDAY_SEARCH_INTERFACE_ID = "IF_HOLIDAY_SRCH";


	/**
     * 매장 정보 저장
     */
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runStoreSetUp() throws BaseException {

		/*
         * 매장 Data  조회 / BOS 상에 등록된 연동 품목인 경우 UR_STORE 테이블에 저장
         */
        List<StoreInfoVo> storeSearchList = getStoreSearchList();
        storeDeliveryZoneBatchService.putStoreSearchJob(storeSearchList);

    }

    /**
     * 스토어(매장/가맹점) 매장배송관리정보 저장
     */
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runStoreDeliveryAreaSetUp() throws BaseException {

		/*
         * ERP 스토어(매장/가맹점) 매장배송관리정보  조회 / BOS 상에 등록된 연동 품목인 경우 UR_STORE_DELIVERY_AREA 테이블에 저장
         */
        List<StoreDeliveryZoneVo> storeDeliveryZoneSearchList = getStoreDeliveryZoneSearchList();
        storeDeliveryZoneBatchService.putStoreDeliveryZoneSearchJob(storeDeliveryZoneSearchList);

    }


	/**
     * 매장주문시간 저장
     */
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runStoreOrdtimeSetUp() throws BaseException {

		/*
         * 매장주문시간 Data  조회 / BOS 상에 등록된 연동 품목인 경우 UR_ST0RE_SCHEDULE 테이블에 저장
         */
        List<StoreScheduleVo> StoreOrdtimeSearchList = getStoreOrdtimeSearchList();
        storeDeliveryZoneBatchService.putStoreOrdtimeSearchJob(StoreOrdtimeSearchList);

    }

    /**
     * 배송권역 정보 저장
     */
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runDeliveryAreaSetUp() throws BaseException {

		/*
         * ERP 배송권역  Data  조회 / BOS 상에 등록된 연동 품목인 경우 UR_DELIVERY_AREA 테이블에 저장
         */
		getDeliveryZoneSearchList();

    }

	/**
     * 매장휴무일 정보 저장
     */
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runStoreUnDeliveryDateSetUp() throws BaseException {

		/*
         * 매장휴무일 Data  조회 / BOS 상에 등록된 연동 품목인 경우 UR_STORE_UNDELIVERDATE 테이블에 저장
         */
        List<StoreUndeliveryDateVo> StoreUnDeliveryDateSearchList = getStoreUnDeliveryDateSearchList();
        storeDeliveryZoneBatchService.putUnDeliveryDateSearchJob(StoreUnDeliveryDateSearchList);

    }


	/*
     * ERP 매장정보 조회 API IF 안된 Data 조회
     *
     * @return List<StoreDeliveryZoneVo> totalStoreDeliveryZoneList : ERP 매장정보 조회 API를 통해 수집한 매장정보 Data
     */
	private List<StoreInfoVo> getStoreSearchList() throws BaseException {
		List<StoreInfoVo> totalDeliveryZoneList = new ArrayList<>(); // 매장정보 Data 전체 취합할 ArrayList

        Map<String, String> parameterMap = new HashMap<>();

        BaseApiResponseVo baseApiResponseVo = null;
        List<StoreInfoVo> eachPageList = null;

        parameterMap.put("itfFlg", String.valueOf("N"));

        try {

            // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
            baseApiResponseVo = erpApiExchangeService.get(parameterMap, STORE_SEARCH_INTERFACE_ID);

            // baseApiResponseVo => List<T> 역직렬화
            eachPageList = baseApiResponseVo.deserialize(StoreInfoVo.class);

        } catch (Exception e) { // ERP API 통신 실패시 : BaseException 으로 변환 후 throw 처리
            throw new BaseException(e.getMessage());
        }

        totalDeliveryZoneList.addAll(eachPageList);

        int startPage = baseApiResponseVo.getCurrentPage(); // 최초 조회한 페이지 ( 1 페이지 )
        int totalPage = baseApiResponseVo.getTotalPage(); // 해당 검색조건으로 조회시 전체 페이지 수

        if (totalPage > 1) {

            // 최초 조회한 페이지의 다음 페이지부터 조회
            for (int page = startPage + 1; page <= totalPage; page++) {

                parameterMap.put("page", String.valueOf(page));

                try {

                    // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
                    baseApiResponseVo = erpApiExchangeService.get(parameterMap, STORE_SEARCH_INTERFACE_ID);

                    // baseApiResponseVo => List<T> 역직렬화
                    eachPageList = baseApiResponseVo.deserialize(StoreInfoVo.class);

                } catch (Exception e) { // ERP API 통신 실패시 : BaseException 으로 변환 후 throw 처리
                    throw new BaseException(e.getMessage());
                }

                totalDeliveryZoneList.addAll(eachPageList);

            }

        }

        return totalDeliveryZoneList;
	}


    /*
     * ERP 매장/가맹점 배송권역정보 조회 API IF 안된 Data 조회
     *
     * @return List<StoreDeliveryZoneVo> totalStoreDeliveryZoneList : ERP 매장/가맹점 배송권역정보 조회 API를 통해 수집한 매장/가맹점 배송권역정보 Data
     */
	private List<StoreDeliveryZoneVo> getStoreDeliveryZoneSearchList() throws BaseException {
		List<StoreDeliveryZoneVo> totalStoreDeliveryZoneList = new ArrayList<>(); // 매장/가맹점 배송권역정보 Data 전체 취합할 ArrayList

        Map<String, String> parameterMap = new HashMap<>();
//        parameterMap.put("inpDat", DateUtil.getCurrentDate()); //현재일 기준으로 조회 (API 검색조건에 추가)

        BaseApiResponseVo baseApiResponseVo = null;
        List<StoreDeliveryZoneVo> eachPageList = null;

        parameterMap.put("itfFlg", String.valueOf("N"));

        try {

            // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
            baseApiResponseVo = erpApiExchangeService.get(parameterMap, STORE_DELIVERY_ZONE_SEARCH_INTERFACE_ID);

            // baseApiResponseVo => List<T> 역직렬화
            eachPageList = baseApiResponseVo.deserialize(StoreDeliveryZoneVo.class);

        } catch (Exception e) { // ERP API 통신 실패시 : BaseException 으로 변환 후 throw 처리
            throw new BaseException(e.getMessage());
        }

        totalStoreDeliveryZoneList.addAll(eachPageList);

        int startPage = baseApiResponseVo.getCurrentPage(); // 최초 조회한 페이지 ( 1 페이지 )
        int totalPage = baseApiResponseVo.getTotalPage(); // 해당 검색조건으로 조회시 전체 페이지 수

        if (totalPage > 1) {

            // 최초 조회한 페이지의 다음 페이지부터 조회
            for (int page = startPage + 1; page <= totalPage; page++) {

                parameterMap.put("page", String.valueOf(page));

                try {

                    // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
                    baseApiResponseVo = erpApiExchangeService.get(parameterMap, STORE_DELIVERY_ZONE_SEARCH_INTERFACE_ID);

                    // baseApiResponseVo => List<T> 역직렬화
                    eachPageList = baseApiResponseVo.deserialize(StoreDeliveryZoneVo.class);

                } catch (Exception e) { // ERP API 통신 실패시 : BaseException 으로 변환 후 throw 처리
                    throw new BaseException(e.getMessage());
                }

                totalStoreDeliveryZoneList.addAll(eachPageList);

            }

        }

        return totalStoreDeliveryZoneList;
	}


    /*
     * ERP 매장시간 조회 API IF 안된 Data 조회
     *
     * @return List<StoreScheduleVo> totalStoreScheduleList : ERP 매장시간 API를 통해 수집한 매장시간 Data
     */
	private List<StoreScheduleVo> getStoreOrdtimeSearchList() throws BaseException {
		List<StoreScheduleVo> totalStoreScheduleList = new ArrayList<>(); // 매장시간 Data 전체 취합할 ArrayList

        Map<String, String> parameterMap = new HashMap<>();
//        parameterMap.put("inpDat", DateUtil.getCurrentDate()); //현재일 기준으로 조회 (API 검색조건에 추가)

        BaseApiResponseVo baseApiResponseVo = null;
        List<StoreScheduleVo> eachPageList = null;

        parameterMap.put("itfFlg", String.valueOf("N"));

        try {

            // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
            baseApiResponseVo = erpApiExchangeService.get(parameterMap, STORE_ORDTIME_SEARCH_INTERFACE_ID);

            // baseApiResponseVo => List<T> 역직렬화
            eachPageList = baseApiResponseVo.deserialize(StoreScheduleVo.class);

        } catch (Exception e) { // ERP API 통신 실패시 : BaseException 으로 변환 후 throw 처리
            throw new BaseException(e.getMessage());
        }

        totalStoreScheduleList.addAll(eachPageList);

        int startPage = baseApiResponseVo.getCurrentPage(); // 최초 조회한 페이지 ( 1 페이지 )
        int totalPage = baseApiResponseVo.getTotalPage(); // 해당 검색조건으로 조회시 전체 페이지 수

        if (totalPage > 1) {

            // 최초 조회한 페이지의 다음 페이지부터 조회
            for (int page = startPage + 1; page <= totalPage; page++) {

                parameterMap.put("page", String.valueOf(page));

                try {

                    // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
                    baseApiResponseVo = erpApiExchangeService.get(parameterMap, STORE_ORDTIME_SEARCH_INTERFACE_ID);

                    // baseApiResponseVo => List<T> 역직렬화
                    eachPageList = baseApiResponseVo.deserialize(StoreScheduleVo.class);

                } catch (Exception e) { // ERP API 통신 실패시 : BaseException 으로 변환 후 throw 처리
                    throw new BaseException(e.getMessage());
                }

                totalStoreScheduleList.addAll(eachPageList);

            }

        }

        return totalStoreScheduleList;
	}


    /*
     * ERP 배송권역 조회 API IF 안된 Data 조회
     *
     * @return List<storeDeliveryAreaVo> totalStoreDeliveryAreaList : ERP 배송권역조회 API를 통해 수집한 배송권역 Data
     */
	private void getDeliveryZoneSearchList() throws BaseException {
		List<StoreDeliveryAreaVo> totalDeliveryZoneList = new ArrayList<>(); // 배송권역 Data 전체 취합할 ArrayList

        Map<String, String> parameterMap = new HashMap<>();
//        parameterMap.put("inpDat", DateUtil.getCurrentDate()); //현재일 기준으로 조회 (API 검색조건에 추가)

        BaseApiResponseVo baseApiResponseVo = null;
        List<StoreDeliveryAreaVo> eachPageList = null;

        parameterMap.put("itfFlg", String.valueOf("N"));

        try {

            // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
            baseApiResponseVo = erpApiExchangeService.get(parameterMap, DELIVERY_ZONE_SEARCH_INTERFACE_ID);

            // baseApiResponseVo => List<T> 역직렬화
//            eachPageList = baseApiResponseVo.deserialize(StoreDeliveryAreaVo.class);

        } catch (Exception e) { // ERP API 통신 실패시 : BaseException 으로 변환 후 throw 처리
            throw new BaseException(e.getMessage());
        }

//        totalDeliveryZoneList.addAll(eachPageList);

        int startPage = baseApiResponseVo.getCurrentPage(); // 최초 조회한 페이지 ( 1 페이지 )
        int totalPage = baseApiResponseVo.getTotalPage(); // 해당 검색조건으로 조회시 전체 페이지 수
        boolean add = false;
        if (totalPage > 0) {

            // 최초 조회한 페이지의 다음 페이지부터 조회
            for (int page = startPage ; page <= totalPage; page++) {

                parameterMap.put("page", String.valueOf(page));

                try {

                    // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
                    baseApiResponseVo = erpApiExchangeService.get(parameterMap, DELIVERY_ZONE_SEARCH_INTERFACE_ID);

                    // baseApiResponseVo => List<T> 역직렬화
                    eachPageList = baseApiResponseVo.deserialize(StoreDeliveryAreaVo.class);

                } catch (Exception e) { // ERP API 통신 실패시 : BaseException 으로 변환 후 throw 처리
                    throw new BaseException(e.getMessage());
                }

                totalDeliveryZoneList.addAll(eachPageList);

                if(page%10 == 0) {
		            storeDeliveryZoneBatchService.putDeliveryZoneSearchJob(totalDeliveryZoneList);
		            totalDeliveryZoneList.clear();
		            add = true;
                }else {
                	add = false;
                }
            }

            if(!add) {
	            storeDeliveryZoneBatchService.putDeliveryZoneSearchJob(totalDeliveryZoneList);
            }

        }

        List<ErpIfDeliveryZoneRequestDto> shpCodeList = new ArrayList<>();
        ErpIfDeliveryZoneRequestDto dto = new ErpIfDeliveryZoneRequestDto();
        dto.setItfFlg("N");
        dto.setSrcSvr(StoreEnums.StoreDeliverySystemCode.ORGAOMS.getCode());
        shpCodeList.add(dto);
        
        dto = new ErpIfDeliveryZoneRequestDto();
        dto.setItfFlg("N");
        dto.setSrcSvr(StoreEnums.StoreDeliverySystemCode.HITOK.getCode());
        shpCodeList.add(dto);

        try {
        	if(totalPage > 0) {
	        	// 배송권역정보 조회 완료 ERP 연동 IF_DLVZONE_FLAG 'Y' 변경
				storeDeliveryZoneBatchService.putDeliveryZoneSearch(shpCodeList);
        	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	 /*
     * ERP 매장휴무일 조회 API IF 안된 Data 조회
     *
     * @return List<StoreUndeliveryDateVo> totalStoreDeliveryZoneList : ERP 매장휴무일 조회 API를 통해 수집한 매장휴무일 Data
     */
	private List<StoreUndeliveryDateVo> getStoreUnDeliveryDateSearchList() throws BaseException {
		List<StoreUndeliveryDateVo> totalUnDeliveryDateList = new ArrayList<>(); // 매장휴무일 Data 전체 취합할 ArrayList

        Map<String, String> parameterMap = new HashMap<>();

        BaseApiResponseVo baseApiResponseVo = null;
        List<StoreUndeliveryDateVo> eachPageList = null;

        parameterMap.put("itfFlg", String.valueOf("N"));

        try {

            // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
            baseApiResponseVo = erpApiExchangeService.get(parameterMap, HOLIDAY_SEARCH_INTERFACE_ID);

            // baseApiResponseVo => List<T> 역직렬화
            eachPageList = baseApiResponseVo.deserialize(StoreUndeliveryDateVo.class);

        } catch (Exception e) { // ERP API 통신 실패시 : BaseException 으로 변환 후 throw 처리
            throw new BaseException(e.getMessage());
        }

        totalUnDeliveryDateList.addAll(eachPageList);

        int startPage = baseApiResponseVo.getCurrentPage(); // 최초 조회한 페이지 ( 1 페이지 )
        int totalPage = baseApiResponseVo.getTotalPage(); // 해당 검색조건으로 조회시 전체 페이지 수

        if (totalPage > 1) {

            // 최초 조회한 페이지의 다음 페이지부터 조회
            for (int page = startPage + 1; page <= totalPage; page++) {

                parameterMap.put("page", String.valueOf(page));

                try {

                    // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
                    baseApiResponseVo = erpApiExchangeService.get(parameterMap, HOLIDAY_SEARCH_INTERFACE_ID);

                    // baseApiResponseVo => List<T> 역직렬화
                    eachPageList = baseApiResponseVo.deserialize(StoreUndeliveryDateVo.class);

                } catch (Exception e) { // ERP API 통신 실패시 : BaseException 으로 변환 후 throw 처리
                    throw new BaseException(e.getMessage());
                }

                totalUnDeliveryDateList.addAll(eachPageList);

            }

        }

        return totalUnDeliveryDateList;
	}


}
