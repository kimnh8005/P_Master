package kr.co.pulmuone.v1.batch.user.store;

import kr.co.pulmuone.v1.batch.user.store.dto.ErpIfDeliveryZoneRequestDto;
import kr.co.pulmuone.v1.batch.user.store.dto.ErpIfStoreRequestDto;
import kr.co.pulmuone.v1.batch.user.store.dto.vo.*;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.StoreEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mappers.batch.master.user.store.StoreErpBatchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreDeliveryZoneBatchService {

	 private final StoreErpBatchMapper storeErpBatchMapper;

	 @Autowired
	 ErpApiExchangeService erpApiExchangeService;

	 // ERP API 의 매장정보 조회 완료 인터페이스 ID
	 private static final String STORE_FLAG_INTERFACE_ID = "IF_STORE_FLAG";

	 // ERP API 의 매장/가맹점 배송권역정보 조회 완료 인터페이스 ID
     private static final String STORE_DELIVERY_ZONE_FLAG_INTERFACE_ID = "IF_STORE_DELIVER_FLAG";

	 // ERP API 의 매장주문시간 정보 조회 완료 인터페이스 ID
     private static final String STORE_ORDTIME_FLAG_INTERFACE_ID = "IF_STORE_ORDTIME_FLAG";

	 // ERP API 의 배송권역정보 조회 완료 인터페이스 ID
     private static final String DELIVERY_ZONE_FLAG_INTERFACE_ID = "IF_DLVZONE_FLAG";

	 // ERP API 의 매장휴무일 정보 조회 완료 인터페이스 ID
     private static final String HOLLIDAY_FLAG_INTERFACE_ID = "IF_HOLIDAY_FLAG";

 	/**
      * 매장정보 갱신 Batch
 	 * @throws Exception
      *
      */
 	protected void putStoreSearchJob(List<StoreInfoVo> storeSearchList) throws BaseException {

 		List<ErpIfStoreRequestDto> shpCodeList = new ArrayList<>();


 		for(StoreInfoVo vo : storeSearchList) {

 			vo.setCreateId(Constants.BATCH_CREATE_ID);
 			if(StoreEnums.StoreDeliverySystemCode.HITOK.getCode().equals(vo.getSrcSvr()) ||
 					StoreEnums.StoreDeliverySystemCode.ORGAOMS.getCode().equals(vo.getSrcSvr())) {
 				// 하이톡 : 공급처 ID '4'[풀무원녹즙(FDD)] 로 설정
 				if(StoreEnums.StoreDeliverySystemCode.HITOK.getCode().equals(vo.getSrcSvr())) {
 					vo.setUrSupplierId(StoreEnums.StoreDeliverySystemCode.HITOK.getValue());
 					vo.setShpTyp(StoreEnums.StoreTypeCode.BRANCH.getCode());
 					vo.setShpCtg(StoreEnums.StoreCategoryTypeCode.FD.getCommonCode());
 				}
 				// ORGAOME : 공급처 ID '2'[올가홀푸드] 로 설정
 				if(StoreEnums.StoreDeliverySystemCode.ORGAOMS.getCode().equals(vo.getSrcSvr())) {
 					vo.setUrSupplierId(StoreEnums.StoreDeliverySystemCode.ORGAOMS.getValue());
 					vo.setShpTyp(StoreEnums.StoreTypeCode.DIRECT.getCode());
 					if(StoreEnums.StoreCategoryTypeCode.DIRECT.getCode().equals(vo.getShpCtg())) {
 						vo.setShpCtg(StoreEnums.StoreCategoryTypeCode.DIRECT.getCommonCode());
 					}else if(StoreEnums.StoreCategoryTypeCode.SIS.getCode().equals(vo.getShpCtg())) {
 						vo.setShpCtg(StoreEnums.StoreCategoryTypeCode.SIS.getCommonCode());
 					}else if(StoreEnums.StoreCategoryTypeCode.BY_ORGA.getCode().equals(vo.getShpCtg())) {
 						vo.setShpCtg(StoreEnums.StoreCategoryTypeCode.BY_ORGA.getCommonCode());
 					}
 				}

 				if(vo.getO2oTyp() == null) {
 					vo.setO2oTyp("Y");
 				}

 				if(vo.getShpSalSet() == null) {
 					vo.setShpSalSet(StoreEnums.StoreDeliverySystemCode.HITOK.getCode());
 				}

 				if(StoreEnums.StoreStatusSystemCode.WAIT.getCode().equals(vo.getShpSta())){
 					vo.setShpSta(StoreEnums.StoreStatusSystemCode.WAIT.getCommonCode());
 				}else if(StoreEnums.StoreStatusSystemCode.USE.getCode().equals(vo.getShpSta())){
 					vo.setShpSta(StoreEnums.StoreStatusSystemCode.USE.getCommonCode());
 				}else if(StoreEnums.StoreStatusSystemCode.NOT_USE.getCode().equals(vo.getShpSta())){
 					vo.setShpSta(StoreEnums.StoreStatusSystemCode.NOT_USE.getCommonCode());
 				}

 				if(vo.getUseYn() != null) {
	 				if(StoreEnums.UseType.USE.getCode().equals(vo.getUseYn())) {
	 					if(StoreEnums.UseType.NOT_USE.getCode().equals(vo.getUpdFlg()) || vo.getUpdFlg() == null) {
	 						addStoreInfo(vo); //매장정보 저장
	 					}else if(StoreEnums.UseType.USE.getCode().equals(vo.getUpdFlg())) {
	 						putStoreInfo(vo); //매장정보 수정
	 					}
	 				}else {
	 					putStoreNotUse(vo); //매장정보 사용여부 'N' 업데이트 처리
	 				}

	 				ErpIfStoreRequestDto dto = new ErpIfStoreRequestDto();
					dto.setShpCd(vo.getShpCd());
					dto.setItfFlg(vo.getItfFlg());

					shpCodeList.add(dto);
 				}
 			}
 		}

 		try {
 				if(shpCodeList.size()>0) {
		 			//매장정보 조회 완료 ERP 연동 IF_STORE_FLAG ITF_FLG 'Y' 변경
		 			putStoreSearch(shpCodeList);
 				}
 		} catch (Exception e) {
 			// TODO Auto-generated catch block
 			throw new BaseException(e.getMessage());
 		}
 	}


	/**
	 * 매장배송관리정보 갱신 Batch
	 * @throws Exception
	 *
	 */
	protected void putStoreDeliveryZoneSearchJob(List<StoreDeliveryZoneVo> storeDeliveryZoneSearchList) throws BaseException {

		List<ErpIfStoreRequestDto> shpCodeList = new ArrayList<>();
		for(StoreDeliveryZoneVo vo : storeDeliveryZoneSearchList) {

			vo.setCreateId(Constants.BATCH_CREATE_ID);

			if(StoreEnums.StoreDeliverySystemCode.HITOK.getCode().equals(vo.getSrcSvr()) ||
					vo.getSrcSvr().equals(StoreEnums.StoreDeliverySystemCode.ORGAOMS.getCode())) {

				if(StoreEnums.StoreApiDeliveryIntervalCode.EVERY.getCode().equals(vo.getDlvMthTyp())) {
					vo.setDlvMthTyp(StoreEnums.StoreApiDeliveryIntervalCode.EVERY.getCommonCode());
				}else if(StoreEnums.StoreApiDeliveryIntervalCode.TWO_DAYS.getCode().equals(vo.getDlvMthTyp())) {
					vo.setDlvMthTyp(StoreEnums.StoreApiDeliveryIntervalCode.TWO_DAYS.getCommonCode());
				}else if(StoreEnums.StoreApiDeliveryIntervalCode.NON_DELV.getCode().equals(vo.getDlvMthTyp())) {
					vo.setDlvMthTyp(StoreEnums.StoreApiDeliveryIntervalCode.NON_DELV.getCommonCode());
				}else if(StoreEnums.StoreApiDeliveryIntervalCode.PARCEL.getCode().equals(vo.getDlvMthTyp())) {
					vo.setDlvMthTyp(StoreEnums.StoreApiDeliveryIntervalCode.PARCEL.getCommonCode());
				}

				if(StoreEnums.StoreApiDeliveryTypeCode.DIRECT.getCode().equals(vo.getDlvTypCd())) {
					vo.setDlvTypCd(StoreEnums.StoreApiDeliveryTypeCode.DIRECT.getCommonCode());
				}else if(StoreEnums.StoreApiDeliveryTypeCode.HD_OD.getCode().equals(vo.getDlvTypCd())) {
					vo.setDlvTypCd(StoreEnums.StoreApiDeliveryTypeCode.HD_OD.getCommonCode());
				}else if(StoreEnums.StoreApiDeliveryTypeCode.HOME.getCode().equals(vo.getDlvTypCd())) {
					vo.setDlvTypCd(StoreEnums.StoreApiDeliveryTypeCode.HOME.getCommonCode());
				}else if(StoreEnums.StoreApiDeliveryTypeCode.OFFICE.getCode().equals(vo.getDlvTypCd())) {
					vo.setDlvTypCd(StoreEnums.StoreApiDeliveryTypeCode.OFFICE.getCommonCode());
				}else if(StoreEnums.StoreApiDeliveryTypeCode.PICKUP.getCode().equals(vo.getDlvTypCd())) {
					vo.setDlvTypCd(StoreEnums.StoreApiDeliveryTypeCode.PICKUP.getCommonCode());
				} else if(StoreEnums.StoreApiDeliveryTypeCode.ALL.getCode().equals(vo.getDlvTypCd())) {
					vo.setDlvTypCd(StoreEnums.StoreApiDeliveryTypeCode.ALL.getCommonCode());
				}

				if(StoreEnums.StoreDeliverySystemCode.HITOK.getCode().equals(vo.getSrcSvr())) {
					if(StoreEnums.StoreApiDeliverableItemCode.ALL.getCode().equals(vo.getDlvItmTyp())) {
						vo.setDlvItmTyp(StoreEnums.StoreApiDeliverableItemCode.ALL.getCommonCode());
					}else if(StoreEnums.StoreApiDeliverableItemCode.DM.getCode().equals(vo.getDlvItmTyp())) {
						vo.setDlvItmTyp(StoreEnums.StoreApiDeliverableItemCode.DM.getCommonCode());
					}else if(StoreEnums.StoreApiDeliverableItemCode.FD.getCode().equals(vo.getDlvItmTyp())) {
						vo.setDlvItmTyp(StoreEnums.StoreApiDeliverableItemCode.FD.getCommonCode());
					}else {
						vo.setDlvItmTyp(null);
					}
				}

				if(vo.getUseYn() != null) {
					if(StoreEnums.UseType.NOT_USE.getCode().equals(vo.getUseYn())) {
						putStoreDeliveryAreaNotUse(vo); // 배송관리 사용여부 'N' 업데이트 처리
					}else {

						if(StoreEnums.UseType.NOT_USE.getCode().equals(vo.getUpdFlg()) || vo.getUpdFlg() == null) {
	 						addStoreDeliveryArea(vo); //스토어(매장/가맹점) 배송관리 정보 저장
	 					}else if(StoreEnums.UseType.USE.getCode().equals(vo.getUpdFlg()) && StoreEnums.UseType.USE.getCode().equals(vo.getUseYn())) {
	 						putStoreDeliveryArea(vo); //스토어(매장/가맹점) 배송관리 정보  수정
	 					}
					}

					ErpIfStoreRequestDto dto = new ErpIfStoreRequestDto();			// shpCd dto
					dto.setShpCd(vo.getShpCd());
					dto.setItfFlg(vo.getItfFlg());
					dto.setDlvAreCd(vo.getDlvAreCd());

					shpCodeList.add(dto);
				}

			}
		}

		try {
				if(shpCodeList.size()>0) {
					// 매장배송관리정보 조회 완료 ERP 연동 IF_STORE_DELIVER_FLAG 'Y' 변경
					putStoreDeliveryZoneSearch(shpCodeList);
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new BaseException(e.getMessage());
		}

	}


	/**
	 * 매장시간 정보 갱신 Batch
	 * @throws Exception
	 *
	 */
	protected void putStoreOrdtimeSearchJob(List<StoreScheduleVo> StoreOrdtimeSearchList) throws BaseException {

		List<ErpIfStoreRequestDto> shpCodeList = new ArrayList<>();
		for(StoreScheduleVo vo : StoreOrdtimeSearchList) {

			if(StoreEnums.StoreDeliverySystemCode.HITOK.getCode().equals(vo.getSrcSvr()) ||
					vo.getSrcSvr().equals(StoreEnums.StoreDeliverySystemCode.ORGAOMS.getCode())) {

				if(vo.getUseYn() != null) {
					if(StoreEnums.UseType.NOT_USE.getCode().equals(vo.getUseYn())) {
						putStoreOrdtimeNotUse(vo); // 매장시간 정보 업데이트 처리
					}else {
						if(StoreEnums.UseType.NOT_USE.getCode().equals(vo.getUpdFlg()) || vo.getUpdFlg() == null) {
	 						addStoreOrdtime(vo); //매장시간 저장
						}else if(StoreEnums.UseType.USE.getCode().equals(vo.getUpdFlg()) && StoreEnums.UseType.USE.getCode().equals(vo.getUseYn())) {
	 						putStoreOrdtime(vo); //매장시간   수정
	 					}
					}

					ErpIfStoreRequestDto dto = new ErpIfStoreRequestDto();			// shpCd dto
					dto.setShpCd(vo.getShpCd());
					dto.setItfFlg(vo.getItfFlg());
					dto.setDlvSte(vo.getDlvSte());

					shpCodeList.add(dto);
				}
			}
		}

		try {
				if(shpCodeList.size()>0) {
					// 매장시간정보 조회 완료 ERP 연동 IF_STORE_ORDTIME_FLAG 'Y' 변경
					putStoreOrdtimeSearch(shpCodeList);
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new BaseException(e.getMessage());
		}

	}

	/**
     * 배송권역 갱신 Batch
	 * @throws Exception
     *
     */
	protected void putDeliveryZoneSearchJob(List<StoreDeliveryAreaVo> storeDeliveryZoneSearchList) throws BaseException {

		if(storeDeliveryZoneSearchList != null) {
			for(StoreDeliveryAreaVo vo : storeDeliveryZoneSearchList) {

				vo.setCreateId(Constants.BATCH_CREATE_ID);
				if(StoreEnums.StoreDeliverySystemCode.HITOK.getCode().equals(vo.getSrcSvr()) ||
						vo.getSrcSvr().equals(StoreEnums.StoreDeliverySystemCode.ORGAOMS.getCode())) {

					if(StoreEnums.UseType.NOT_USE.getCode().equals(vo.getUseYn())) {
						putDeliveryAreaNotUse(vo); // 배송권역 정보 업데이트 처리
					}else {
						addDeliveryArea(vo); //배송권역 정보 저장
					}

				}
			}
		}
	}



	/**
     * 매장휴무일 정보 갱신 Batch
	 * @throws Exception
     *
     */
	protected void putUnDeliveryDateSearchJob(List<StoreUndeliveryDateVo> storeDeliveryZoneSearchList) throws BaseException {

		List<ErpIfStoreRequestDto> shpCodeList = new ArrayList<>();
		for(StoreUndeliveryDateVo vo : storeDeliveryZoneSearchList) {

			vo.setCreateId(Constants.BATCH_CREATE_ID);
			if(StoreEnums.StoreDeliverySystemCode.HITOK.getCode().equals(vo.getSrcSvr()) ||
					vo.getSrcSvr().equals(StoreEnums.StoreDeliverySystemCode.ORGAOMS.getCode())) {
				if(vo.getUseYn() != null) {
					if(StoreEnums.UseType.NOT_USE.getCode().equals(vo.getUseYn())) {
						putUnDeliveryDateNotUse(vo); // 매장 휴무일 정보 업데이트 처리
					}else {
						if(StoreEnums.UseType.NOT_USE.getCode().equals(vo.getUpdFlg()) || vo.getUpdFlg() == null) {
							addUnDeliveryDate(vo); //매장 휴무일 정보 저장
						}else if(StoreEnums.UseType.USE.getCode().equals(vo.getUpdFlg()) && StoreEnums.UseType.USE.getCode().equals(vo.getUseYn())) {
	 						putUnDeliveryDateNotUse(vo); //매장 휴무일    수정
	 					}
					}

					ErpIfStoreRequestDto dto = new ErpIfStoreRequestDto();			// shpCd dto
					dto.setShpCd(vo.getShpCd());
					dto.setSchShiDat(vo.getSchShiDat());

					shpCodeList.add(dto);
				}

			}
		}

		try {
				if(shpCodeList.size()>0) {
					// 배송권역정보 조회 완료 ERP 연동 IF_DLVZONE_FLAG 'Y' 변경
					putStoreUnDeliveryDateSearch(shpCodeList);
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new BaseException(e.getMessage());
		}
	}


  	/**
  	 * @Desc  매장 정보 저장
  	 * @param StoreDeliveryAreaVo
  	 * @return int
  	 */
  	protected int addStoreInfo(StoreInfoVo vo) {
  		return storeErpBatchMapper.addStoreInfo(vo);
  	}

  	/**
  	 * @Desc  매장 정보 수정
  	 * @param StoreDeliveryAreaVo
  	 * @return int
  	 */
  	protected int putStoreInfo(StoreInfoVo vo) {
  		return storeErpBatchMapper.putStoreInfo(vo);
  	}

  	/**
  	 * @Desc  매장 정보 사용상태 변경
  	 * @param StoreDeliveryAreaVo
  	 * @return int
  	 */
  	protected int putStoreNotUse(StoreInfoVo vo) {
  		return storeErpBatchMapper.putStoreNotUse(vo);
  	}




  	/**
  	 * @Desc  스토어(매장/가맹점) 매장배송관리정보 저장
  	 * @param StoreDeliveryAreaVo
  	 * @return int
  	 */
  	protected int addStoreDeliveryArea(StoreDeliveryZoneVo vo) {
  		return storeErpBatchMapper.addStoreDeliveryArea(vo);
  	}

  	/**
  	 * @Desc  스토어(매장/가맹점) 매장배송관리정보 수정
  	 * @param StoreDeliveryAreaVo
  	 * @return int
  	 */
  	protected int putStoreDeliveryArea(StoreDeliveryZoneVo vo) {
  		return storeErpBatchMapper.putStoreDeliveryArea(vo);
  	}

  	/**
  	 * @Desc  스토어(매장/가맹점) 매장배송관리정보 사용상태 변경
  	 * @param StoreDeliveryAreaVo
  	 * @return int
  	 */
  	protected int putStoreDeliveryAreaNotUse(StoreDeliveryZoneVo vo) {
  		return storeErpBatchMapper.putStoreDeliveryAreaNotUse(vo);
  	}

  	/**
  	 * @Desc  매장주문시간 정보 저장
  	 * @param StoreScheduleVo
  	 * @return int
  	 */
  	protected int addStoreOrdtime(StoreScheduleVo vo) {
  		return storeErpBatchMapper.addStoreOrdtime(vo);
  	}


  	/**
  	 * @Desc  매장주문시간  수정
  	 * @param StoreScheduleVo
  	 * @return int
  	 */
  	protected int putStoreOrdtime(StoreScheduleVo vo) {
  		return storeErpBatchMapper.putStoreOrdtime(vo);
  	}


  	/**
  	 * @Desc  매장주문시간  사용상태 변경
  	 * @param StoreScheduleVo
  	 * @return int
  	 */
  	protected int putStoreOrdtimeNotUse(StoreScheduleVo vo) {
  		return storeErpBatchMapper.putStoreOrdtimeNotUse(vo);
  	}


  	/**
  	 * @Desc  배송권역 정보 저장
  	 * @param StoreDeliveryAreaVo
  	 * @return int
  	 */
  	protected int addDeliveryArea(StoreDeliveryAreaVo vo) {
  		return storeErpBatchMapper.addDeliveryArea(vo);
  	}


  	/**
  	 * @Desc  배송권역 정보 수정
  	 * @param StoreDeliveryAreaVo
  	 * @return int
  	 */
  	protected int putDeliveryArea(StoreDeliveryAreaVo vo) {
  		return storeErpBatchMapper.putDeliveryArea(vo);
  	}


  	/**
  	 * @Desc  배송권역 정보 사용상태 변경
  	 * @param StoreDeliveryAreaVo
  	 * @return int
  	 */
  	protected int putDeliveryAreaNotUse(StoreDeliveryAreaVo vo) {
  		return storeErpBatchMapper.putDeliveryAreaNotUse(vo);
  	}


  	/**
  	 * @Desc  매장휴무일 정보 저장
  	 * @param StoreUndeliveryDateVo
  	 * @return int
  	 */
  	protected int addUnDeliveryDate(StoreUndeliveryDateVo vo) {
  		return storeErpBatchMapper.addUnDeliveryDate(vo);
  	}



  	/**
  	 * @Desc  매장휴무일 정보 사용상태 변경
  	 * @param StoreUndeliveryDateVo
  	 * @return int
  	 */
  	protected int putUnDeliveryDateNotUse(StoreUndeliveryDateVo vo) {
  		return storeErpBatchMapper.putUnDeliveryDateNotUse(vo);
  	}

	/**
     * 매장정보 조회 완료 ERP 연동 IF_STORE_FLAG
     * ERP 연동 후 결과값 return
	 * @return
	 */
	protected BaseApiResponseVo putStoreSearch(List<ErpIfStoreRequestDto> shpCodeList) throws Exception {
		// baseApiResponseVo : restTemplate 으로 해당 API 를 PUT 방식으로 호출 후 받은 응답 결과 객체
	    BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.put(shpCodeList, STORE_FLAG_INTERFACE_ID);

	    log.info("baseApiResponseVo:" + ToStringBuilder.reflectionToString(baseApiResponseVo));

	    if (!baseApiResponseVo.isSuccess()) { // API 호출 오류 시
	        // 별도 확인 필요 : BOS 상에 품목 등록은 성공했으나, ERP API 상의 품목 조회 완료 업데이트 API 호출 실패 case
	        log.error(" API Call Failure");
	    }

        return baseApiResponseVo;
	}


	/**
     * 매장배송관리정보 조회 완료 ERP 연동 IF_STORE_DELIVER_FLAG
     * ERP 연동 후 결과값 return
	 * @return
	 */
	protected BaseApiResponseVo putStoreDeliveryZoneSearch(List<ErpIfStoreRequestDto> shpCodeList) throws Exception {
		// baseApiResponseVo : restTemplate 으로 해당 API 를 PUT 방식으로 호출 후 받은 응답 결과 객체
	    BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.put(shpCodeList, STORE_DELIVERY_ZONE_FLAG_INTERFACE_ID);

	    log.info("baseApiResponseVo:" + ToStringBuilder.reflectionToString(baseApiResponseVo));

	    if (!baseApiResponseVo.isSuccess()) { // API 호출 오류 시
	        // 별도 확인 필요 : BOS 상에 품목 등록은 성공했으나, ERP API 상의 품목 조회 완료 업데이트 API 호출 실패 case
	        log.error(" API Call Failure");
	    }

        return baseApiResponseVo;
	}


	/**
     * 매주문시간 정보 조회 완료 ERP 연동 IF_STORE_ORDTIME_SRCH
     * ERP 연동 후 결과값 return
	 * @return
	 */
	protected BaseApiResponseVo putStoreOrdtimeSearch(List<ErpIfStoreRequestDto> shpCodeList) throws Exception {
		// baseApiResponseVo : restTemplate 으로 해당 API 를 PUT 방식으로 호출 후 받은 응답 결과 객체
	    BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.put(shpCodeList, STORE_ORDTIME_FLAG_INTERFACE_ID);

	    log.info("baseApiResponseVo:" + ToStringBuilder.reflectionToString(baseApiResponseVo));

	    if (!baseApiResponseVo.isSuccess()) { // API 호출 오류 시
	        // 별도 확인 필요 : BOS 상에 품목 등록은 성공했으나, ERP API 상의 품목 조회 완료 업데이트 API 호출 실패 case
	        log.error(" API Call Failure");
	    }

        return baseApiResponseVo;
	}


	/**
     * 배송권역정보 조회 완료 ERP 연동 IF_DLVZONE_FLAG
     * ERP 연동 후 결과값 return
	 * @return
	 */
	protected BaseApiResponseVo putDeliveryZoneSearch(List<ErpIfDeliveryZoneRequestDto> shpCodeList) throws Exception {
		// baseApiResponseVo : restTemplate 으로 해당 API 를 PUT 방식으로 호출 후 받은 응답 결과 객체
	    BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.put(shpCodeList, DELIVERY_ZONE_FLAG_INTERFACE_ID);

	    log.info("baseApiResponseVo:" + ToStringBuilder.reflectionToString(baseApiResponseVo));

	    if (!baseApiResponseVo.isSuccess()) { // API 호출 오류 시
	        // 별도 확인 필요 : BOS 상에 품목 등록은 성공했으나, ERP API 상의 품목 조회 완료 업데이트 API 호출 실패 case
	        log.error(" API Call Failure");
	    }

        return baseApiResponseVo;
	}


	/**
     * 매장휴무일 정보 조회 완료 ERP 연동 IF_HOLIDAY_FLAG
     * ERP 연동 후 결과값 return
	 * @return
	 */
	protected BaseApiResponseVo putStoreUnDeliveryDateSearch(List<ErpIfStoreRequestDto> shpCodeList) throws Exception {
		// baseApiResponseVo : restTemplate 으로 해당 API 를 PUT 방식으로 호출 후 받은 응답 결과 객체
	    BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.put(shpCodeList, HOLLIDAY_FLAG_INTERFACE_ID);

	    log.info("baseApiResponseVo:" + ToStringBuilder.reflectionToString(baseApiResponseVo));

	    if (!baseApiResponseVo.isSuccess()) { // API 호출 오류 시
	        // 별도 확인 필요 : BOS 상에 품목 등록은 성공했으나, ERP API 상의 품목 조회 완료 업데이트 API 호출 실패 case
	        log.error(" API Call Failure");
	    }

        return baseApiResponseVo;
	}

}
