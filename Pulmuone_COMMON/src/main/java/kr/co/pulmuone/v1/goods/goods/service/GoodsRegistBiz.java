package kr.co.pulmuone.v1.goods.goods.service;

import java.util.List;
import java.util.Map;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistCategoryRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistItemWarehouseRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsPackageCalcListVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsPackageGoodsMappingVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsPriceInfoResultVo;

public interface GoodsRegistBiz {
	//마스터품목 내역
	ApiResult<?> getItemDetail(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception;

	//묶음상품 정보
	List<GoodsPackageGoodsMappingVo> goodsPackageGoodsMappingList(String ilGoodsId, String imageSortYn) throws Exception;

	//상품정보 제공고시(품목내역)
	ApiResult<?> getItemSpecValueList(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception;

	//상품 영양정보(품목내역)
	ApiResult<?> getItemNutritionList(String ilItemCode) throws Exception;

	//상품 인증정보(품목내역)
	ApiResult<?> getItemCertificationList(String ilItemCode) throws Exception;

	//상품 상세 이미지(품목내역)
	ApiResult<?> getItemImageList(String ilItemCode) throws Exception;

	//마스터 품목 가격정보
	ApiResult<?> itemPriceList(String ilItemCode) throws Exception;

	//상품 등록
	ApiResult<?> addGoods(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception;

	//상품 수정
	ApiResult<?> modifyGoods(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception;

	//배송 유형에 따른 출고처
	ApiResult<?> itemWarehouseList(GoodsRegistItemWarehouseRequestDto goodsRegistItemWarehouseRequestDto) throws Exception;

	//출고처에 따른 배송 정책
	ApiResult<?> itemWarehouseShippingTemplateList(GoodsRegistItemWarehouseRequestDto goodsRegistItemWarehouseRequestDto) throws Exception;

	//상품 마스터 정보
	ApiResult<?> goodsDetail(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception;

	//상품 중복 확인(품목코드, 출고처 기준)
	ApiResult<?> duplicateGoods(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception;

	//전시/몰인몰 카테고리
	ApiResult<?> getDisplayCategoryList(GoodsRegistCategoryRequestDto goodsRegistCategoryRequestDto) throws Exception;

	//묶음 상품 > 기준상품, 묶음 상품 정보 불러오기
	ApiResult<?> getGoodsList(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception;

	//묶음 상품 > 상품정보 제공고시, 상품 영양정보 불러오기
	ApiResult<?> getGoodsInfo(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception;

	//묶음 상품 > 묶음 상품 리스트 조합의 배송 불가 지역, 반품 가능 기간 산출
	ApiResult<?> etcAssemble(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception;

	//묶음 상품 > 기본할인 등록 > 가격 계산 상품 리스트
	ApiResult<?> goodsPackageCalcList(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception;

	//묶음 상품 > 기본할인 등록 > 가격 계산 상품 리스트
	List<GoodsPackageCalcListVo> getGoodsPackageCalcList(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception;

	//일반상품 > 판매 정보 > 일일 판매 옵션 설정 > 예약판매 > 주문수집I/F일, 출고예정일, 도착예정일 산출
	ApiResult<?> goodsReservationDateCalcList(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception;

	//일반상품 > 판매 정보 > 일일 판매 옵션 설정 > 예약판매 > 주문수집I/F일 배송패턴에 따른 요일 날짜 제한
	ApiResult<?> orderIfDateLimitList(String urWarehouseId) throws Exception;

	//품목 판매불가에 따른 상품 판매상태 판매중지 처리
	ApiResult<?> updateGoodsStopSale(String ilItemCode) throws Exception;

	//묶음 상품 > 해당 구성 상품으로 구성된 묶음상품 정보가 있는지 체크
	ApiResult<?> goodsPackageExistChk(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception;

	///상품 할인 > 승인완료, 반려, 요청철회 이후 처리(승인 관리 메뉴에서 호출해서 처리)
	MessageCommEnum goodsDiscountApprAfterProc(String ilGoodsDiscountApprId, String discountType) throws Exception;

	//모든 상품 > 상품 승인완료(APPR_STAT.APPROVED) 이후 처리(승인 관리 메뉴에서 호출해서 처리)
	MessageCommEnum goodsApprAfterProc(String ilGoodsApprId, String apprKindTp) throws Exception;

	//품목 승인 요청시 상품 판매 대기 상태로 변경
	int updateGoodsSaleStatusToWaitByItemAppr(String ilItemCode) throws Exception;

	//품목 수정 승인시 상품의 판매 상태 원상 복귀
	int updateGoodsSaleStatusToBackByItemAppr(String ilItemCode) throws Exception;

	//품목가격 변동에 의한 상품 가격 수정 업데이트 프로시저 호출
	void spGoodsPriceUpdateWhenItemPriceChanges(String ilItemCode) throws Exception;

	//품목가격 변동에 의한 묶음상품 가격 수정 업데이트 프로시저 호출
	void spPackageGoodsPriceUpdateWhenItemPriceChanges() throws Exception;

	//상품등록에 의한 가격 수정 업데이트 프로시저 호출
	void spGoodsPriceUpdateWhenGoodsRegist(String ilGoodsId) throws Exception;

	//할인기간수정, 할인삭제에 따른 가격정보 > 판매 가격정보 새로고침 처리
	ApiResult<?> goodsPriceRefresh(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception;

	int addGoodsMasterChangeLog(String tranMode, String ilGoodsId, String createId, Map<String, String> beforeGoodsDatas, Map<String, String> afterGoodsDatas, String createDate) throws BaseException, Exception;

	//풀무원샵 상품코드 정보가 있는지 체크
	ApiResult<?> goodsCodeExistChk(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception;

	List<GoodsPriceInfoResultVo> getGoodsPriceList(Long ilGoodsId, String dateTime) throws Exception;

}
