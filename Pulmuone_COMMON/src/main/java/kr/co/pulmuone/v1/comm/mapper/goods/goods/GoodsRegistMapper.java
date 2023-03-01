package kr.co.pulmuone.v1.comm.mapper.goods.goods;

import kr.co.pulmuone.v1.goods.goods.dto.*;
import kr.co.pulmuone.v1.goods.goods.dto.vo.*;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealPatternListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface GoodsRegistMapper {

	//마스터 품목 내역
	GoodsRegistVo getItemDetail(GoodsRegistRequestDto goodsRegistRequestDto);

	//상품정보 제공고시(품목내역)
	List<GoodsRegistVo> getItemSpecValueList(GoodsRegistRequestDto getGoodsMgmRequestDt);

	//상품 영양정보(품목내역)
	List<GoodsRegistVo> getItemNutritionList(String ilItemCode);

	//상품 인증정보(품목내역)
	List<GoodsRegistVo> getItemCertificationList(String ilItemCode);

	//상품 상세 이미지(품목내역)
	List<GoodsRegistVo> getItemImageList(String ilItemCode);

	//상품 등록
	int addGoods(GoodsRegistRequestDto goodsRegistRequestDto);

	//상품 수정
	int modifyGoods(GoodsRegistRequestDto goodsRegistRequestDto);

	//상품 수정 일자 업데이트
	int modifyDateGoods(@Param("ilGoodsId") long ilGoodsId, @Param("modifyId") long modifyId);

	//배송 유형에 따른 출고처
	List<GoodsRegistItemWarehouseVo> itemWarehouseList(GoodsRegistItemWarehouseRequestDto goodsRegistItemWarehouseRequestDto);

	//배송 유형에 따른 출고처(일일상품의 경우, 가맹점 배송만 처리)
	List<GoodsRegistItemWarehouseVo> dailyGoodsItemWarehouseList(GoodsRegistItemWarehouseRequestDto goodsRegistItemWarehouseRequestDto);

	//출고처에 따른 배송 정책
	List<GoodsRegistItemWarehouseVo> itemWarehouseShippingTemplateList(GoodsRegistItemWarehouseRequestDto goodsRegistItemWarehouseRequestDto);

	//전시, 몰인몰 카테고리 저장
	int addGoodsCategory(GoodsRegistCategoryRequestDto goodsRegistCategoryRequestDto);

	//예약판매 옵션설정 저장
	int addGoodsReservationOption(GoodsRegistReservationOptionDto goodsRegistReservationOptionDto);

	//예약판매 옵션설정 수정
	int updateGoodsReservationOption(GoodsRegistReservationOptionDto goodsRegistReservationOptionDto);

	//예약 판매 옵션 설정 회차 순서 변경(1순위 : 도착예정일, 2순위 출고예정일, 3순위 : 주문수집I/F일, 4순위 : 예약주문가능기간 종료일)
	int updateGoodsReservationOptionSaleSeq(String ilGoodsId);

	//배송정책 저장
	int addGoodsShippingTemplate(GoodsRegistShippingTemplateDto goodsRegistShippingTemplateDto);

	//추가 상품 저장
	int addGoodsAdditionalGoodsMapping(GoodsRegistAdditionalGoodsDto goodsAdditionalGoodsMappingListDto);

	//추천 상품 저장
	int addGoodsRecommend(GoodsRegistAdditionalGoodsDto goodsRecommendDto);

	//상품 마스터 정보
	GoodsRegistVo goodsDetail(GoodsRegistRequestDto goodsRegistRequestDto);

	//상품 중복 확인(품목코드, 출고처 기준)
	GoodsRegistVo duplicateGoods(GoodsRegistRequestDto goodsRegistRequestDto);

	//예약판매옵션설정
	List<GoodsRegistReserveOptionVo> goodsReservationOptionList(GoodsRegistRequestDto goodsRegistRequestDto);

	//추가상품
	List<GoodsRegistAdditionalGoodsVo> goodsAdditionalGoodsMappingList(GoodsRegistRequestDto goodsRegistRequestDto);

	//추천상품
	List<GoodsRegistAdditionalGoodsVo> goodsRecommendList(GoodsRegistRequestDto goodsRegistRequestDto);

	//배송정책
	List<GoodsRegistShippingTemplateVo> goodsShippingTemplateList(GoodsRegistRequestDto goodsRegistRequestDto);

	//저장된 배송정책인지 체크
	int goodsShippingTemplateChkNum(GoodsRegistShippingTemplateDto goodsRegistShippingTemplateDto);

	//카테고리 삭제
	int deleteGoodsCategory(GoodsRegistRequestDto goodsRegistRequestDto);

	// 현재 유효한 예약판매옵션 삭제시 주문수량 체크
	int getGoodsReservationOptionOrderCount(@Param("delReservationOptionList") List<GoodsRegistReservationOptionDto> delReservationOptionList);

	//예약판매옵션설정 삭제
	int deleteGoodsReservationOption(@Param("delReservationOptionList") List<GoodsRegistReservationOptionDto> delReservationOptionList, @Param("createId") long createId);

	//배송 정책 수정
	int updateGoodsShippingTemplate(GoodsRegistShippingTemplateDto goodsRegistShippingTemplateDto);

	//추가상품 삭제
	int deleteGoodsAdditionalGoodsMapping(GoodsRegistRequestDto goodsRegistRequestDto);

	//추천상품 삭제
	int deleteGoodsRecommend(GoodsRegistRequestDto goodsRegistRequestDto);

	//가격정보 > 판매 가격정보
	List<GoodsPriceInfoResultVo> goodsPrice(@Param("ilGoodsId") String ilGoodsId,@Param("taxYn") String taxYn) throws Exception;

	List<GoodsPriceInfoResultVo> getGoodsPriceList(@Param("ilGoodsId") Long ilGoodsId, @Param("dateTime") String dateTime) throws Exception;

	//임직원 할인 정보 > 임직원 할인 가격정보
	List<GoodsPriceInfoResultVo> goodsEmployeePrice(@Param("ilItemCode") String ilItemCode,@Param("ilGoodsId") String ilGoodsId,@Param("taxYn") String taxYn) throws Exception;

	//가격정보 > 행사/할인 내역
	List<GoodsPriceInfoResultVo> goodsDiscountList(@Param("ilGoodsId") String ilGoodsId,@Param("discountTypeCode") String discountTypeCode) throws Exception;

	//가격정보 > 행사/할인 내역
	List<GoodsPriceInfoResultVo> goodsDiscountErpEventList(@Param("ilGoodsId") String ilGoodsId,@Param("discountTypeCode") String discountTypeCode) throws Exception;

	//묶음상품 > 가격정보 > 묶음상품 기본 힐인가
	List<GoodsPriceInfoResultVo> goodsPackageDiscountList(@Param("ilGoodsId") String ilGoodsId,@Param("discountTypeCode") String discountTypeCode) throws Exception;

	//가격정보 > 마스터 품목 가격정보
	List<GoodsPriceInfoResultVo> itemPriceList(String ilItemCode) throws Exception;

	//가격정보 > 행사/할인 내역 > 할인 내역 시작일이 유효한지 Validation check
	int isValidStartTimeAddGoodsDiscount(GoodsDiscountRequestDto goodsDiscountRequestDto) throws Exception;

	//가격정보 > 행사/할인 내역 > 할인 내역 저장 가능한지 Validation check
	int isValidAddGoodsDiscount(GoodsDiscountRequestDto goodsDiscountRequestDto) throws Exception;

	//가격정보 > 행사/할인 내역 > 우선할인,즉시 할인 할인설정 승인 요청
	int addGoodsDiscountAppr(GoodsDiscountApprVo goodsDiscountApprVo) throws Exception;

	//가격정보 > 행사/할인 내역 > 우선할인,즉시 할인 할인설정 승인 요청 히스토리
	int addGoodsDiscountApprStatusHistory(GoodsDiscountApprVo goodsDiscountApprVo) throws Exception;

	//가격정보 > 행사/할인 내역 > 우선할인,즉시할인 승인완료 후 할인내역 추가
	int addGoodsDiscountByAppr(GoodsDiscountApprVo goodsDiscountApprVo) throws Exception;

	//묶음상품 > 가격정보 > 행사/할인 내역 > 할인 승인완료 후 묶음상품 개별품목 할인정보 승인 추가
	int addGoodsPackageItemFixedDiscountPriceByAppr(GoodsDiscountApprVo goodsDiscountApprVo) throws Exception;

	//상품 할인 승인 테이블에 상품할인ID UPDATE
	int updateGoodsDiscountAppr(GoodsDiscountApprVo goodsDiscountApprVo) throws Exception;

	//가격정보 > 행사/할인 내역 > 우선할인,즉시 할인 할인설정 승인 요청 저장
	int addGoodsDiscount(GoodsDiscountRequestDto goodsDiscountRequestDto) throws Exception;

	//가격정보 > 행사/할인 내역 > 우선할인,즉시 할인 할인설정 승인 요청 수정
	int modifyGoodsDiscount(GoodsDiscountRequestDto goodsDiscountRequestDto) throws Exception;

	//묶음 상품 > 기준상품, 묶음 상품 정보 불러오기
	List<GoodsRegistVo> getGoodsList(GoodsRegistRequestDto goodsRegistRequestDto);

	//묶음 상품 > 상품정보 제공고시 불러오기
	List<GoodsRegistVo> getGoodsInfoAnnounceList(GoodsRegistRequestDto goodsRegistRequestDto);

	//묶음 상품 > 상품 영양정보 불러오기
	List<GoodsRegistVo> getGoodsNutritionList(GoodsRegistRequestDto goodsRegistRequestDto);

	//묶음 상품 > 이미지 등록
	int addGoodsImage(GoodsImageVo addGoodsImageVo);

	//묶음 상품 > 묶음상품, 증정품 목록 저장
	int addPackageGoodsMapping(GoodsRegistPackageGoodsMappingDto goodsRegistPackageGoodsMappingDto);

	//묶음 상품 > 묶음상품 원본 가격 입력
	int addGoodsPackagePriceOrig(GoodsRegistPackageGoodsPriceDto goodsRegistPackageGoodsItemPriceMappingDto);

	//묶음 상품 > 묶음상품 구성 정보 리스트
	List<GoodsPackageGoodsMappingVo> goodsPackageGoodsMappingList(@Param("ilGoodsId") String ilGoodsId, @Param("imageSortYn") String imageSortYn) throws Exception;

	//묶음상품 개별품목 고정가 할인가격 저장
	int addGoodsPackageItemFixedDiscountPrice(GoodsRegistPackageGoodsPriceDto goodsPackageItemFixedDiscountPrice);

	//묶음상품 개별품목 할인정보 승인 저장
	int addGoodsPackageItemFixedDiscountPriceAppr(GoodsPackageItemFixedDiscountPriceApprVo goodsPackageItemFixedDiscountPriceApprVo);

	//묶음 상품 > 상품 이미지 리스트
	List<GoodsRegistImageVo> goodsImageList(String ilGoodsId) throws Exception;

	//묶음 상품 > 묶음상품 전용 이미지 삭제
	int delGoodsImage(String ilGoodsId);

	//일일 상품 > 일일 판매 옵션 설정 > 식단 주기 입력
	int addGoodsDailyCycle(GoodsDailyCycleBulkVo addGoodsDailyCycleVo);

	//일일 상품 > 일일 판매 옵션 설정 > 일괄 배달 설정 입력
	int addGoodsDailyBulk(GoodsDailyCycleBulkVo addGoodsDailyBulkVo);

	//일일 상품 > 일일 판매 옵션 설정 > 식단 주기 리스트
	List<GoodsDailyCycleBulkVo> goodsDailyCycleList(String ilGoodsId) throws Exception;

	//일일 상품 > 일일 판매 옵션 설정 > 일괄 배달 설정 리스트
	List<GoodsDailyCycleBulkVo> goodsDailyBulkList(String ilGoodsId) throws Exception;

	//일일상품 > 일일 판매 옵션 설정 > 식단주기 삭제
	int delGoodsDailyCycle(String ilGoodsId);

	//일일상품 > 일일 판매 옵션 설정 > 일괄 배달 설정 삭제
	int delGoodsDailyBulk(String ilGoodsId);

	//묶음 상품 > 기본할인 등록 > 가격 계산 상품 리스트
	List<GoodsPackageCalcListVo> goodsPackageCalcList(GoodsRegistRequestDto goodsRegistRequestDto);

	//묶음 상품 > 묶음 상품 가격정보 저장(Procedure 호출)
	int spGoodsPriceUpdateWhenPackageGoodsChanges(GoodsRegistRequestDto goodsRegistRequestDto);

	//일반상품 > 상품 가격정보 저장/Update, 품목 가격정보 Update(프로시져 호출)
	void spGoodsPriceUpdateWhenItemPriceChanges(@Param("ilItemCode") String ilItemCode, @Param("inDebugFlag") boolean inDebugFlag);

	//묶음상품 > 상품 가격정보 저장/Update, 품목 가격정보 Update(프로시져 호출)
	void spPackageGoodsPriceUpdateWhenItemPriceChanges(@Param("inDebugFlag") boolean inDebugFlag);

	//일반상품 저장시 기본 상품 가격정보 생성(프로시져 호출)
	void spGoodsPriceUpdateWhenGoodsRegist(@Param("ilGoodsId") String ilGoodsId, @Param("inDebugFlag") boolean inDebugFlag);

	//묶음 상품 > 묶음상품 기본 판매가 > 기존 종료일 변경
	int updateGoodsDiscountEndTime(@Param("ilGoodsId") String ilGoodsId,@Param("newGoodsDiscountStartTime") String newGoodsDiscountStartTime);

	//묶음 상품 > 묶음상품 기본 판매가 승인 > 기존 종료일 변경
	int updateGoodsDiscountApprEndTime(@Param("ilGoodsId") String ilGoodsId,@Param("newGoodsDiscountStartTime") String newGoodsDiscountStartTime);

	//일반상품 > 임직원 할인 정보 > 임직원 기본할인 정보
	GoodsPriceInfoResultVo goodsBaseDiscountEmployeeList(String ilItemCode) throws Exception;

	//묶음상품 > 임직원 할인 정보 > 임직원 할인 가격정보
	List<GoodsPriceInfoResultVo> goodsPackageEmployeePriceList(String ilGoodsId);

	//묶음상품 > 임직원 할인 정보 > 임직원 기본할인 정보
	List<GoodsPriceInfoResultVo> goodsPackageBaseDiscountEmployeeList(String ilGoodsId) throws Exception;

	//묶음상품 > 임직원 할인 정보 > 임직원 개별할인 정보
	List<GoodsPriceInfoResultVo> goodsPackageDiscountEmployeeList(String ilGoodsId) throws Exception;

	//일반상품 > 판매 정보 > 일일 판매 옵션 설정 > 예약판매 > 주문수집I/F일, 출고예정일, 도착예정일 산출
	GoodsRegistReserveOptionVo goodsReservationDateCalc(GoodsRegistRequestDto goodsRegistRequestDto);

	//일반상품 > 판매 정보 > 일일 판매 옵션 설정 > 예약판매 > 주문수집I/F일 배송패턴에 따른 요일 날짜 제한
	List<GoodsRegistReserveOptionVo> orderIfDateLimitList(String urWarehouseId) throws Exception;

	//일반, 일일상품 > 혜택/구매 정보 > 증정행사 정보
	List<ExhibitGiftResultVo> exhibitGiftList(String ilGoodsId) throws Exception;

	//판매/전시 > 재고운영형태, 전일마감재고 정보
	GoodsRegistStockInfoVo goodsStockInfo(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception;

	//묶음상품 > 상품 이미지 > 개별상품 대표이미지 정렬 순서 UPDATE
	int updateGoodsPackageGoodsMappingImageOrderUpdate(@Param("targetGoodsId") String targetGoodsId, @Param("imageSortSeq") int imageSortSeq, @Param("ilGoodsId") String ilGoodsId);

	//품목 판매불가에 따른 상품 판매중지 대상 찾기
	List<GoodsRegistVo> itemExtinctionGoodsStopSaleList(String ilItemCode) throws Exception;

	//품목 판매불가에 따른 상품 판매상태 판매중지 처리
	int updateGoodsStopSale(@Param("ilGoodsIds") List<String> ilGoodsIds, @Param("saleStatus") String saleStatus);

	//묶음 상품 > 해당 구성 상품으로 구성된 묶음상품 정보가 있는지 체크 // HGRM-4246 기존 묶음상품 구성 체크 로직 변경 / 2021.01.31 이명수
	GoodsRegistVo goodsPackageExistChk(Map<String, Object> params) throws Exception;

	//체크된 묶음 상품의 구성 상품과 동일한 수량이 있는지 체크
	int goodsPackageQuantityExistChk(GoodsRegistPackageGoodsMappingDto goodsRegistPackageGoodsMappingDto);

	//상품 변경 이력 등록
	int addGoodsChangeLog(GoodsChangeLogVo goodsChangeLogVo);

	//상품 승인 내역 저장
	int addGoodsAppr(GoodsRegistApprVo goodsRegistApprVo);

	//상품 승인 상태 이력 저장
	int addGoodsApprStatusHistory(GoodsRegistApprVo goodsRegistApprVo);

	//상품 승인 내역 업데이트(1,2차 승인자 변경)
	int updateGoodsAppr(GoodsRegistApprVo goodsRegistApprVo);

	//상품 승인 내역 확인, 승인 내역 존재시 요청 자격 확인
	GoodsRegistApprVo goodsApprInfo(@Param("userId") String userId, @Param("ilGoodsId") String ilGoodsId, @Param("ilGoodsApprId") String ilGoodsApprId, @Param("apprKindTp") String apprKindTp) throws Exception;

	//상품 승인 완료시 상품 판매상태, 상품 정보 변경
	int updateGoodsApprovedSaleStatus(GoodsRegistApprVo goodsRegistApprVo);

	//상품 승인 요청시 상품 판매상태 변경
	int updateGoodsRequestSaleStatus(GoodsRegistApprVo goodsRegistApprVo);

	//상품 승인 상태 항목
	List<GoodsApprovalResultVo> goodsApprStatusList(String ilGoodsId) throws Exception;

	//품목 승인 요청시 상품 판매 대기 상태로 변경
	int updateGoodsSaleStatusToWaitByItemAppr(@Param("ilItemCode") String ilItemCode) throws Exception;

	//품목 승인 요청시 상태 변경내역 저장
	int addGoodsChangeLogUpdateByItemAppr(@Param("goodsChangeLogVo") GoodsChangeLogVo goodsChangeLogVo, @Param("ilItemCode") String ilItemCode);

	//품목 수정 승인시 상품의 판매 상태 원상 복귀
	int updateGoodsSaleStatusToBackByItemAppr(@Param("ilItemCode") String ilItemCode) throws Exception;

	//품목 수정 승인시 상품의 판매 상태 원상 복귀 변경내역 저장
	int addGoodsChangeLogBackByItemAppr(@Param("goodsChangeLogVo") GoodsChangeLogVo goodsChangeLogVo, @Param("ilItemCode") String ilItemCode);

	//상품 할인 승인 내역 확인, 승인 내역 존재시 요청 자격 확인
	GoodsDiscountApprVo goodsDiscountApprInfo(@Param("ilGoodsId") String ilGoodsId, @Param("ilGoodsDiscountApprId") String ilGoodsDiscountApprId,@Param("discountType") String discountType) throws Exception;

	// 품목코드에 의한 상품검색
	int getGoodsCountByItemCode(@Param("ilItemCode") String ilItemCode);

	// 묶음 구성상품 선물하기 허용 여부
	List<String> getPresentYnsByPackageGoodsId(@Param("ilGoodsId") String ilGoodsId);

	// 선물하기 허용 여부 update
	void updateGoodsPresentYn(@Param("ilGoodsId") String ilGoodsId, @Param("presentYn") String presentYn);

	// 식단 스케쥴 정보 조회
	List<MealPatternListVo> getMealScheduleList(@Param("ilGoodsId") String ilGoodsId);
}
