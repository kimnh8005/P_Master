package kr.co.pulmuone.v1.batch.goods.stock;

 import java.time.LocalDate;
 import java.time.temporal.ChronoUnit;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Map;
 import java.util.function.Function;
 import java.util.stream.Collectors;

 import org.apache.commons.lang.builder.ToStringBuilder;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;

 import kr.co.pulmuone.v1.batch.goods.stock.dto.vo.ErpLinkItemVo;
 import kr.co.pulmuone.v1.batch.goods.stock.dto.vo.ItemErpStockResultVo;
 import kr.co.pulmuone.v1.batch.goods.stock.dto.vo.ItemErpStockVo;
 import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfStockSearchRequestDto;
 import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfStockSearchResponseDto;
 import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
 import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
 import kr.co.pulmuone.v1.comm.enums.StockEnums;
 import kr.co.pulmuone.v1.comm.exception.BaseException;
 import kr.co.pulmuone.v1.comm.mappers.batch.master.goods.stock.BatchItemErpStockMapper;
 import lombok.RequiredArgsConstructor;
 import lombok.extern.slf4j.Slf4j;

 /**
  * <PRE>
  * Forbiz Korea
  * ERP 재고수량 조회 API 조회 후 BOS 재고 계산 배치 Service
  * </PRE>
  *
  * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 10. 14.               박주형         최초작성
 * =======================================================================
  * </PRE>
  */
 @Slf4j
 @Service
 @RequiredArgsConstructor
 public class BatchGoodsStockService {

     //재고수량 조회 완료 인터페이스 ID
     private static final String STOCK_SEARCH_FLAG_INTERFACE_ID = "IF_STOCK_FLAG";

     @Autowired
     private ErpApiExchangeService erpApiExchangeService;

     @Autowired
     private BatchItemErpStockMapper batchItemErpStockMapper;

     /*
      * ERP 재고조회 API 의 해당 shiFroOrgId 에 해당하는 재고 배치 Job 실행
      *
      * @param List<ErpIfStockSearchResponseDto> erpStockSearchList : 해당 shiFroOrgId 로 ERP 재고조회 API 를 통해 수집한 재고 Data
      *
      * @param String shiFroOrgId : ERP 재고조회 API 의 해당 출고처 ID
      *
      * @param long batchId : 배치 Job ID
      *
      */
     protected void runGoodsStockJobByShiFroOrgId(List<ErpIfStockSearchResponseDto> erpStockSearchList, String shiFroOrgId, long batchId) throws BaseException {

         /*
          * ERP 재고조회 API 의 해당 shiFroOrgId 에 해당하는 BOS 상의 출고처 ID ( UR_WAREHOUSE_ID ) 조회
          */
         String urWarehouseIdStr = getUrWarehouseIdByShiFroOrgId(shiFroOrgId);

         if (urWarehouseIdStr == null) { // BOS 상에 등록된 출고처 ID 미조회시
             throw new BaseException(StockEnums.StockBatchExceptionMessage.NOT_REGISTERED_UR_WAREHOUSE_ID);
         }

         /*
          * 해당 출고처의 공급업체 / 출고처 PK 에 대응하는 BOS 상의 모든 연동품목 목록 조회
          */
         List<ErpLinkItemVo> registeredErpLinkItemList = getErpLinkItemList(shiFroOrgId);

         if (registeredErpLinkItemList.isEmpty()) { // 해당 출고처에 대응하는 연동 품목 미등록시 return
             return;
         }

         /*
          * 해당 출고처의 ERP 재고 Data Filtering 쉽게 하기 위해 품목코드를 key 로 가지는 Map 형식으로 반환
          */
		Map<String, ErpIfStockSearchResponseDto> erpStockSearchMap = erpStockSearchList.stream() //
				.collect(Collectors.toMap(ErpIfStockSearchResponseDto::getErpItemNo, Function.identity(), (v1, v2) -> v1));

         ErpIfStockSearchResponseDto registeredErpLinkItemDto = null; // BOS 상에 등록된 연동 품목에 대응되는 ERP 재고 Data 객체

         /*
          * BOS 상에 해당 출고처로 등록된 연동 품목 반복문 시작
          */
         for (ErpLinkItemVo erpLinkItemVo : registeredErpLinkItemList) {

             // ERP API 재고 조회 Map 에서 해당 품목정보 존재시
             if (erpStockSearchMap.containsKey(erpLinkItemVo.getIlItemCode())) {

                 // BOS 상에 등록된 해당 연동 품목에 대응되는 ERP 재고 Data 객체
                 registeredErpLinkItemDto = erpStockSearchMap.get(erpLinkItemVo.getIlItemCode());

                 /*
                  * 재고 유형 / 재고 수량 setting
                  */

                 // 전일재고 (전일 마감수량) 존재시
                 if (registeredErpLinkItemDto.getStockClosingCount() != null) {

                     // IL_ITEM_ERP_STOCK 에 저장할 Vo 생성
                     ItemErpStockVo itemErpStockVo = ItemErpStockVo.builder() //
                             .ilItemWarehouseId(erpLinkItemVo.getIlItemWarehouseId()) // BOS 상에 등록된 품목-출고처 PK
                             .baseDate(registeredErpLinkItemDto.getInputDate()) // 기준일자
                             .createId(batchId) // 등록자 ID : 해당 배치 ID
                             .build();

                     itemErpStockVo.setStockType(StockEnums.ErpStockType.ERP_STOCK_CLOSED.getCode()); // 해당 재고유형
                     itemErpStockVo.setStockQuantity(registeredErpLinkItemDto.getStockClosingCount()); // 해당 재고수량

                     addItemErpStock(itemErpStockVo); // IL_ITEM_ERP_STOCK 테이블에 저장
                     addItemErpStockHistory(itemErpStockVo.getIlItemErpStockId()); // IL_ITEM_ERP_STOCK_HISTORY 테이블에 저장

                 }

                 // 입고예정수량 존재시
                 /*
                 if (registeredErpLinkItemDto.getStockScheduledCount() != null) {

                     // IL_ITEM_ERP_STOCK 에 저장할 Vo 생성
                     ItemErpStockVo itemErpStockVo = ItemErpStockVo.builder() //
                             .ilItemWarehouseId(erpLinkItemVo.getIlItemWarehouseId()) // BOS 상에 등록된 품목-출고처 PK
                             .baseDate(registeredErpLinkItemDto.getInputDate()) // 기준일자
                             .createId(batchId) // 등록자 ID : 해당 배치 ID
                             .build();

                     itemErpStockVo.setStockType(StockEnums.ErpStockType.ERP_STOCK_SCHEDULED.getCode()); // 해당 재고유형
                     itemErpStockVo.setStockQuantity(registeredErpLinkItemDto.getStockScheduledCount()); // 해당 재고수량

                     addItemErpStock(itemErpStockVo); // IL_ITEM_ERP_STOCK 테이블에 저장
                     addItemErpStockHistory(itemErpStockVo.getIlItemErpStockId()); // IL_ITEM_ERP_STOCK_HISTORY 테이블에 저장
                 }*/

                 // 입고확정량 존재시
                 /*
                 if (registeredErpLinkItemDto.getStockConfirmedCount() != null) {

                     // IL_ITEM_ERP_STOCK 에 저장할 Vo 생성
                     ItemErpStockVo itemErpStockVo = ItemErpStockVo.builder() //
                             .ilItemWarehouseId(erpLinkItemVo.getIlItemWarehouseId()) // BOS 상에 등록된 품목-출고처 PK
                             .baseDate(registeredErpLinkItemDto.getInputDate()) // 기준일자
                             .createId(batchId) // 등록자 ID : 해당 배치 ID
                             .build();

                     itemErpStockVo.setStockType(StockEnums.ErpStockType.ERP_STOCK_CONFIRMED.getCode()); // 해당 재고유형
                     itemErpStockVo.setStockQuantity(registeredErpLinkItemDto.getStockConfirmedCount()); // 해당 재고수량

                     addItemErpStock(itemErpStockVo); // IL_ITEM_ERP_STOCK 테이블에 저장
                     addItemErpStockHistory(itemErpStockVo.getIlItemErpStockId()); // IL_ITEM_ERP_STOCK_HISTORY 테이블에 저장

                 }*/

                 // 오프라인 물류수량 존재시
                 /*
                 if (registeredErpLinkItemDto.getWmsCount() != null) {

                     // IL_ITEM_ERP_STOCK 에 저장할 Vo 생성
                     ItemErpStockVo itemErpStockVo = ItemErpStockVo.builder() //
                             .ilItemWarehouseId(erpLinkItemVo.getIlItemWarehouseId()) // BOS 상에 등록된 품목-출고처 PK
                             .baseDate(registeredErpLinkItemDto.getInputDate()) // 기준일자
                             .createId(batchId) // 등록자 ID : 해당 배치 ID
                             .build();

                     itemErpStockVo.setStockType(StockEnums.ErpStockType.ERP_STOCK_OFFLINE.getCode()); // 해당 재고유형
                     itemErpStockVo.setStockQuantity(registeredErpLinkItemDto.getWmsCount()); // 해당 재고수량

                     addItemErpStock(itemErpStockVo); // IL_ITEM_ERP_STOCK 테이블에 저장
                     addItemErpStockHistory(itemErpStockVo.getIlItemErpStockId()); // IL_ITEM_ERP_STOCK_HISTORY 테이블에 저장

                 }*/
             }

         } // BOS 상에 해당 출고처로 등록된 연동 품목 반복문 끝

         /*
          * IL_ITEM_ERP_STOCK 테이블에 저장 완료 후 기존 변수 초기화
          */
         if (erpStockSearchList != null)
             erpStockSearchList.clear();

         if (erpStockSearchMap != null)
             erpStockSearchMap.clear();

         if (registeredErpLinkItemList != null)
             registeredErpLinkItemList.clear();
     }


     /*
      * ERP 재고조회 API 의 해당 shiFroOrgId 에 해당하는 재고 배치 Job 실행(올가전용)
      *
      * @param List<ErpIfStockSearchResponseDto> erpStockSearchList : 해당 shiFroOrgId 로 ERP 재고조회 API 를 통해 수집한 재고 Data
      *
      * @param String shiFroOrgId : ERP 재고조회 API 의 해당 출고처 ID
      *
      * @param long batchId : 배치 Job ID
      *
      */
     protected void runOrgaGoodsStockJobByShiFroOrgId(List<ErpIfStockSearchResponseDto> erpStockSearchList, String shiFroOrgId, long batchId) throws BaseException {

         /*
          * ERP 재고조회 API 의 해당 shiFroOrgId 에 해당하는 BOS 상의 출고처 ID ( UR_WAREHOUSE_ID ) 조회
          */
         String urWarehouseIdStr = getUrWarehouseIdByShiFroOrgId(shiFroOrgId);

         if (urWarehouseIdStr == null) { // BOS 상에 등록된 출고처 ID 미조회시
             throw new BaseException(StockEnums.StockBatchExceptionMessage.NOT_REGISTERED_UR_WAREHOUSE_ID);
         }

         /*
          * 해당 출고처의 공급업체 / 출고처 PK 에 대응하는 BOS 상의 모든 연동품목 목록 조회
          */
         List<ErpLinkItemVo> registeredErpLinkItemList = getErpLinkItemList(shiFroOrgId);

         if (registeredErpLinkItemList.isEmpty()) { // 해당 출고처에 대응하는 연동 품목 미등록시 return
             return;
         }

         /*
          * 해당 출고처의 ERP 재고 Data Filtering 쉽게 하기 위해 품목코드를 key 로 가지는 Map 형식으로 반환
          */
         Map<String, ErpIfStockSearchResponseDto> erpStockSearchMap = erpStockSearchList.stream() //
                 .collect(Collectors.toMap(ErpIfStockSearchResponseDto::getErpItemNo, Function.identity(), (v1, v2) -> v1));

         ErpIfStockSearchResponseDto registeredErpLinkItemDto = null; // BOS 상에 등록된 연동 품목에 대응되는 ERP 재고 Data 객체

         /*
          * BOS 상에 해당 출고처로 등록된 연동 품목 반복문 시작
          */
         for (ErpLinkItemVo erpLinkItemVo : registeredErpLinkItemList) {

             // ERP API 재고 조회 Map 에서 해당 품목정보 존재시
             if (erpStockSearchMap.containsKey(erpLinkItemVo.getIlItemCode())) {

                 // BOS 상에 등록된 해당 연동 품목에 대응되는 ERP 재고 Data 객체
                 registeredErpLinkItemDto = erpStockSearchMap.get(erpLinkItemVo.getIlItemCode());

                 /*
                  * 재고 유형 / 재고 수량 setting
                  */
                 // 입고확정량 존재시
                if (registeredErpLinkItemDto.getStockConfirmedCount() != null) {

                     // IL_ITEM_ERP_STOCK 에 저장할 Vo 생성
                     ItemErpStockVo itemErpStockVo = ItemErpStockVo.builder() //
                             .ilItemWarehouseId(erpLinkItemVo.getIlItemWarehouseId()) // BOS 상에 등록된 품목-출고처 PK
                             .baseDate(registeredErpLinkItemDto.getInputDate()) // 기준일자
                             .createId(batchId) // 등록자 ID : 해당 배치 ID
                             .build();

                     itemErpStockVo.setStockType(StockEnums.ErpStockType.ERP_STOCK_CONFIRMED.getCode()); // 해당 재고유형
                     itemErpStockVo.setStockQuantity(registeredErpLinkItemDto.getStockConfirmedCount()); // 해당 재고수량

                     int count = getIlItemErpStockCount(itemErpStockVo);//품목 연동재고 건수 조회

                     // insert / update 분기처리
                     if(count == 0) {
                        addItemErpStock(itemErpStockVo); // IL_ITEM_ERP_STOCK 테이블에 저장
                        addItemErpStockHistory(itemErpStockVo.getIlItemErpStockId()); // IL_ITEM_ERP_STOCK_HISTORY 테이블에 저장
                     }else {
                        putItemErpStock(itemErpStockVo); // IL_ITEM_ERP_STOCK 테이블에 수정
                        putItemErpStockHistory(itemErpStockVo); // IL_ITEM_ERP_STOCK_HISTORY 테이블에 수정
                     }
                }

               // 오프라인 물류수량 존재시
               if (registeredErpLinkItemDto.getWmsCount() != null) {

                 // IL_ITEM_ERP_STOCK 에 저장할 Vo 생성
                 ItemErpStockVo itemErpStockVo = ItemErpStockVo.builder() //
                         .ilItemWarehouseId(erpLinkItemVo.getIlItemWarehouseId()) // BOS 상에 등록된 품목-출고처 PK
                         .baseDate(registeredErpLinkItemDto.getInputDate()) // 기준일자
                         .createId(batchId) // 등록자 ID : 해당 배치 ID
                         .build();

                 itemErpStockVo.setStockType(StockEnums.ErpStockType.ERP_STOCK_OFFLINE.getCode()); // 해당 재고유형
                 itemErpStockVo.setStockQuantity(registeredErpLinkItemDto.getWmsCount()); // 해당 재고수량

                 int count = getIlItemErpStockCount(itemErpStockVo);//품목 연동재고 건수 조회

                 // insert / update 분기처리
                 if(count == 0) {
                    addItemErpStock(itemErpStockVo); // IL_ITEM_ERP_STOCK 테이블에 저장
                    addItemErpStockHistory(itemErpStockVo.getIlItemErpStockId()); // IL_ITEM_ERP_STOCK_HISTORY 테이블에 저장
                 }else {
                    putItemErpStock(itemErpStockVo); // IL_ITEM_ERP_STOCK 테이블에 수정
                    putItemErpStockHistory(itemErpStockVo); // IL_ITEM_ERP_STOCK_HISTORY 테이블에 수정
                 }
               }

            }

         } // BOS 상에 해당 출고처로 등록된 연동 품목 반복문 끝

         /*
          * IL_ITEM_ERP_STOCK 테이블에 저장 완료 후 기존 변수 초기화
          */
         if (erpStockSearchList != null)
             erpStockSearchList.clear();

         if (erpStockSearchMap != null)
             erpStockSearchMap.clear();

         if (registeredErpLinkItemList != null)
             registeredErpLinkItemList.clear();
     }


     /*
      * 재고수량 조회 확인
      */
     protected void runGoodsStockSearchFlagJob(List<ErpIfStockSearchResponseDto> erpStockSearchList, String shiFroOrgId, long batchId) throws BaseException {

         List<ErpIfStockSearchRequestDto> erpItmNoList = new ArrayList<>();

         /*
          * ERP 재고조회 API 의 해당 shiFroOrgId 에 해당하는 BOS 상의 출고처 ID ( UR_WAREHOUSE_ID ) 조회
          */
         String urWarehouseIdStr = getUrWarehouseIdByShiFroOrgId(shiFroOrgId);

         if (urWarehouseIdStr == null) { // BOS 상에 등록된 출고처 ID 미조회시
             throw new BaseException(StockEnums.StockBatchExceptionMessage.NOT_REGISTERED_UR_WAREHOUSE_ID);
         }

         /*
          * 해당 출고처의 공급업체 / 출고처 PK 에 대응하는 BOS 상의 모든 연동품목 목록 조회
          */
         List<ErpLinkItemVo> registeredErpLinkItemList = getErpLinkItemList(shiFroOrgId);

         if (registeredErpLinkItemList.isEmpty()) { // 해당 출고처에 대응하는 연동 품목 미등록시 return
             return;
         }

         /*
          * 해당 출고처의 ERP 재고 Data Filtering 쉽게 하기 위해 품목코드를 key 로 가지는 Map 형식으로 반환
          */
         Map<String, ErpIfStockSearchResponseDto> erpStockSearchMap = erpStockSearchList.stream() //
                 .collect(Collectors.toMap(ErpIfStockSearchResponseDto::getErpItemNo, Function.identity(), (v1, v2) -> v1));


         /*
          * BOS 상에 해당 출고처로 등록된 연동 품목 반복문 시작
          */
         for (ErpLinkItemVo erpLinkItemVo : registeredErpLinkItemList) {

             // ERP API 재고 조회 Map 에서 해당 품목정보 존재시
             if (erpStockSearchMap.containsKey(erpLinkItemVo.getIlItemCode())) {

                 //재고수량 조회완료 목록
                 ErpIfStockSearchRequestDto dto = new ErpIfStockSearchRequestDto();

                 dto.setErpItmNo(erpLinkItemVo.getIlItemCode());

                 erpItmNoList.add(dto);

             }

         } // BOS 상에 해당 출고처로 등록된 연동 품목 반복문 끝

         try {
              //재고수량 조회 완료 Flag처리
                 if(!erpItmNoList.isEmpty()) {
                 putStockSearchFlag(erpItmNoList);
                 }
         } catch (Exception e) {
             throw new BaseException(e.getMessage());
         }

         /*
          * IL_ITEM_ERP_STOCK 테이블에 저장 완료 후 기존 변수 초기화
          */
         if (erpStockSearchList != null)
             erpStockSearchList.clear();

         if (erpStockSearchMap != null)
             erpStockSearchMap.clear();

         if (registeredErpLinkItemList != null)
             registeredErpLinkItemList.clear();
     }

     /**
      * 재고수량 조회 완료 Flag
      * ERP 연동 후 결과값 return
      * @return
      */
     protected BaseApiResponseVo putStockSearchFlag(List<ErpIfStockSearchRequestDto> erpItmNoList) throws Exception {
         // baseApiResponseVo : restTemplate 으로 해당 API 를 PUT 방식으로 호출 후 받은 응답 결과 객체
         BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.put(erpItmNoList, STOCK_SEARCH_FLAG_INTERFACE_ID);

         log.info("baseApiResponseVo:" + ToStringBuilder.reflectionToString(baseApiResponseVo));

         if (!baseApiResponseVo.isSuccess()) { // API 호출 오류 시
             log.error(" API Call Failure");
         }

         return baseApiResponseVo;
     }

     /**
      * 재고수량 조회 API 의 shiFroOrgId 출고처 ID 에 해당하는 BOS 상의 출고처 ID 조회
      *
      * @param String shiFroOrgId : 재고수량 조회 API 의 shiFroOrgId 출고처 ID
      * @return String : BOS 상에 등록된 해당 출고처 ID
      */
     protected String getUrWarehouseIdByShiFroOrgId(String shiFroOrgId) {

         return batchItemErpStockMapper.getUrWarehouseIdByShiFroOrgId(shiFroOrgId);

     }



     /**
      * 재고수량 조회 API 의 해당 shiFroOrgId 출고처 ID 에 해당하고 BOS 상에 등록된 모든 ERP 연동품목 조회
      *
      * @param String shiFroOrgId : 재고수량 조회 API 의 shiFroOrgId 출고처 ID
      * @return List<ErpLinkItemVo> : 재고수량 조회 API 의 해당 shiFroOrgId 출고처 ID 에 해당하고 BOS 상에 등록된 모든 ERP 연동품목 목록
      */
     protected List<ErpLinkItemVo> getErpLinkItemList(String shiFroOrgId) {

         return batchItemErpStockMapper.getErpLinkItemList(shiFroOrgId);

     }

     /**
      * IL_ITEM_ERP_STOCK 테이블에 ERP 재고수량 조회 Data 저장
      *
      * @param ItemErpStockVo : 재고 유형 / 수량 저장 Vo
      * @return int
      */
     protected long addItemErpStock(ItemErpStockVo itemErpStockVo) {
         return batchItemErpStockMapper.addItemErpStock(itemErpStockVo);

     }

     /**
      * IL_ITEM_ERP_STOCK_HISTORY 테이블에 ERP 재고수량 조회 Data 저장
      *
      * @param ItemErpStockVo : 재고 유형 / 수량 저장 Vo
      * @return int
      */
     protected int addItemErpStockHistory(long ilItemErpStockIdd) {
         return batchItemErpStockMapper.addItemErpStockHistory(ilItemErpStockIdd);

     }

     /**
      * IL_ITEM_ERP_STOCK 테이블에 재고타입 및 예정일 저장
      */
     protected void runExcelUploadCal() {

         List<ItemErpStockResultVo> resultList = null;

         List<ItemErpStockVo> uploadList = getIlItemStockExpr();

         for(ItemErpStockVo vo : uploadList) {

             LocalDate now = LocalDate.now();//현재일짜

             int year  = Integer.parseInt(vo.getExpirationDate().substring(0, 4));
             int month = Integer.parseInt(vo.getExpirationDate().substring(5, 7));
             int day   = Integer.parseInt(vo.getExpirationDate().substring(8, 10));

             LocalDate endDate = LocalDate.of(year, month, day);

             long calDateDays = ChronoUnit.DAYS.between(now, endDate);

             vo.setRestDay(calDateDays); //남은 일수 set

             resultList = getStockExprCalList(vo);//품목 유통기한별 재고 계산된 목록 조회

             for(ItemErpStockResultVo resultVo : resultList) {

                 putExcelUpload(resultVo); //엑셀 업로드 재고타입 수정

                 putIlItemWarehouse(resultVo); //품목별 출고처 관리 수정

                 int subYear  = Integer.parseInt(resultVo.getExpirationDate().substring(0, 4));
                 int subMonth = Integer.parseInt(resultVo.getExpirationDate().substring(5, 7));
                 int subDay   = Integer.parseInt(resultVo.getExpirationDate().substring(8, 10));

                 LocalDate expirationDate = LocalDate.of(subYear, subMonth, subDay);//유통기한

                 LocalDate scheduleDate = expirationDate.minusDays((resultVo.getDelivery()));//유통기한에서 출고기준일 빼기

                 resultVo.setStockType(StockEnums.ErpStockType.ERP_STOCK_DISCARD.getCode()); //재고타입은 폐기예정
                 resultVo.setScheduleDate(scheduleDate.toString());//예정일

                 addErpStock(resultVo); //품목 연동제고에 저장
                 addItemErpStockHistory(resultVo.getIlItemErpStockId()); // IL_ITEM_ERP_STOCK_HISTORY 테이블에 저장
             }

         }

     }

     /**
      * @Desc 품목 유통기한별 재고 조회
      * @param String
      * @return List<ItemErpStockVo>
      */
     protected List<ItemErpStockVo> getIlItemStockExpr() {
         return batchItemErpStockMapper.getIlItemStockExpr();
     }

     /**
     * @Desc 품목 연동재고  > 품목 재고 저장(Procedure 호출)
     * @param
     * @return int
     */
     protected int spItemStockCaculatedPrepare() {
         return batchItemErpStockMapper.spItemStockCaculatedPrepare();
     }

      /**
         * @Desc  품목 유통기한별 재고 계산된 목록 조회
         * @param ItemErpStockVo
         * @return List
         */
        protected List<ItemErpStockResultVo> getStockExprCalList(ItemErpStockVo vo) {
            return batchItemErpStockMapper.getStockExprCalList(vo);
        }


       /**
      * @Desc 품목 유통기한별 재고 재고타입 수정
      * @param ItemErpStockResultVo
      * @return int
      */
      protected int putExcelUpload(ItemErpStockResultVo vo) {
          return batchItemErpStockMapper.putExcelUpload(vo);
      }

      /**
       * @Desc 품목별 출고처 관리 수정
       * @param ItemErpStockResultVo
       * @return int
       */
       protected int putIlItemWarehouse(ItemErpStockResultVo vo) {
           return batchItemErpStockMapper.putIlItemWarehouse(vo);
       }

      /**
       * @Desc ERP 재고 엑셀 업로드 - 폐기예정으로 저장
       * @param ItemErpStockResultVo
       * @return int
       */
      protected long addErpStock(ItemErpStockResultVo vo) {
          return batchItemErpStockMapper.addErpStock(vo);
      }

      /**
       * @Desc 품목 연동재고 건수 조회
       * @param ItemErpStockVo
       * @return int
       */
      protected int getIlItemErpStockCount(ItemErpStockVo vo) {
           return batchItemErpStockMapper.getIlItemErpStockCount(vo);
      }

      /**
       * @Desc 품목 연동재고 수정
       * @param ItemErpStockVo
       * @return int
       */
      protected int putItemErpStock(ItemErpStockVo vo) {
           return batchItemErpStockMapper.putItemErpStock(vo);
      }

      /**
       * @Desc 품목 연동재고이력 수정
       * @param ItemErpStockVo
       * @return int
       */
      protected int putItemErpStockHistory(ItemErpStockVo vo) {
           return batchItemErpStockMapper.putItemErpStockHistory(vo);
      }

 }
