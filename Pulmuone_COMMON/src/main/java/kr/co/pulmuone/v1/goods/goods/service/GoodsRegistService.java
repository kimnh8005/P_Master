package kr.co.pulmuone.v1.goods.goods.service;

import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.mapper.goods.goods.GoodsCodeMapper;
import kr.co.pulmuone.v1.comm.mapper.goods.goods.GoodsRegistMapper;
import kr.co.pulmuone.v1.comm.mapper.goods.price.GoodsPriceMapper;
import kr.co.pulmuone.v1.goods.goods.dto.*;
import kr.co.pulmuone.v1.goods.goods.dto.vo.*;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealPatternListVo;
import kr.co.pulmuone.v1.goods.price.dto.vo.GoodsVo;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
* <PRE>
* 상품 등록/수정 Service
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  0.1    2020. 10. 13.               임상건         최초작성
* =======================================================================
* </PRE>
*/
@Service
@RequiredArgsConstructor
public class GoodsRegistService {

	@Autowired
	private final GoodsRegistMapper goodsRegistMapper;

	@Autowired
	private final GoodsCodeMapper goodsCodeMapper;

	@Autowired
	private final GoodsPriceMapper goodsPriceMapper;

	/**
	* @Desc 마스터품목 내역
	* @param goodsRegistRequestDto
	* @return GoodsRegistVo
	*/
	protected GoodsRegistVo getItemDetail(GoodsRegistRequestDto goodsRegistRequestDto) {
		return goodsRegistMapper.getItemDetail(goodsRegistRequestDto);
	}

	/**
	* @Desc 상품정보 제공고시(품목내역)
	* @param goodsRegistRequestDto
	* @return List<GoodsRegistVo>
	*/
	protected List<GoodsRegistVo> getItemSpecValueList(GoodsRegistRequestDto goodsRegistRequestDto) {
		return goodsRegistMapper.getItemSpecValueList(goodsRegistRequestDto);
	}

	/**
	* @Desc 상품 영양정보(품목내역)
	* @param String
	* @return List<GoodsRegistVo>
	*/
	protected List<GoodsRegistVo> getItemNutritionList(String ilItemCode) {
		return goodsRegistMapper.getItemNutritionList(ilItemCode);
	}

	/**
	* @Desc 상품 인증정보(품목내역)
	* @param String
	* @return List<GoodsRegistVo>
	*/
	protected List<GoodsRegistVo> getItemCertificationList(String ilItemCode) {
		return goodsRegistMapper.getItemCertificationList(ilItemCode);
	}

	/**
	* @Desc 상품 상세 이미지(품목내역)
	* @param String
	* @return List<GoodsRegistVo>
	*/
	protected List<GoodsRegistVo> getItemImageList(String ilItemCode) {
		return goodsRegistMapper.getItemImageList(ilItemCode);
	}

	/**
	* @Desc 상품 등록
	* @param GoodsRegistRequestDto
	* @return int
	*/
	protected int addGoods(GoodsRegistRequestDto goodsRegistRequestDto) {
		return goodsRegistMapper.addGoods(goodsRegistRequestDto);
	}

	/**
	* @Desc 상품 수정
	* @param GoodsRegistRequestDto
	* @return int
	*/
	protected int modifyGoods(GoodsRegistRequestDto goodsRegistRequestDto) {
		return goodsRegistMapper.modifyGoods(goodsRegistRequestDto);
	}

	/**
	 * @Desc 상품 수정 일자 업데이트
	 * @param GoodsRegistRequestDto
	 * @return int
	 */
	protected int modifyDateGoods(long ilGoodsId, long createId) {
		return goodsRegistMapper.modifyDateGoods(ilGoodsId, createId);
	}

	/**
	* @Desc 카테고리 등록
	* @param GoodsRegistCategoryRequestDto
	* @return int
	*/
	protected int addGoodsCategory(GoodsRegistCategoryRequestDto goodsRegistCategoryRequestDto) {
		return goodsRegistMapper.addGoodsCategory(goodsRegistCategoryRequestDto);
	}

	/**
	* @Desc 카테고리 삭제
	* @param GoodsRegistRequestDto
	* @return int
	*/
	protected int deleteGoodsCategory(GoodsRegistRequestDto goodsRegistRequestDto) {
		return goodsRegistMapper.deleteGoodsCategory(goodsRegistRequestDto);
	}

	/**
	* @Desc 배송정책 등록
	* @param
	* @return int
	*/
	protected int addGoodsShippingTemplate(GoodsRegistShippingTemplateDto goodsRegistShippingTemplateDto) {
		return goodsRegistMapper.addGoodsShippingTemplate(goodsRegistShippingTemplateDto);
	}

	/**
	* @Desc 예약판매옵션설정 등록
	* @param GoodsRegistReservationOptionDto
	* @return int
	*/
	protected int addGoodsReservationOption(GoodsRegistReservationOptionDto goodsRegistReservationOptionDto) {
		return goodsRegistMapper.addGoodsReservationOption(goodsRegistReservationOptionDto);
	}

	/**
	* @Desc 예약판매옵션설정 수정
	* @param GoodsRegistReservationOptionDto
	* @return int
	*/
	protected int updateGoodsReservationOption(GoodsRegistReservationOptionDto goodsRegistReservationOptionDto) {
		return goodsRegistMapper.updateGoodsReservationOption(goodsRegistReservationOptionDto);
	}

	/**
	* @Desc 예약판매옵션설정 회차 순번 수정
	* @param String
	* @return int
	*/
	protected int updateGoodsReservationOptionSaleSeq(String ilGoodsId) {
		return goodsRegistMapper.updateGoodsReservationOptionSaleSeq(ilGoodsId);
	}


	/**
	 * @Desc 유효한 예약판매옵션설정 삭제시 주문수량 체크
	 * @param delReservationOptionList
	 * @return int
	 */
	protected int getGoodsReservationOptionOrderCount(List<GoodsRegistReservationOptionDto> delReservationOptionList) {
		return goodsRegistMapper.getGoodsReservationOptionOrderCount(delReservationOptionList);
	}

	/**
	* @Desc 예약판매옵션설정 삭제
	* @param GoodsRegistReservationOptionDto
	* @return int
	*/
	protected int deleteGoodsReservationOption(List<GoodsRegistReservationOptionDto> delReservationOptionList, long modifyId) {
		return goodsRegistMapper.deleteGoodsReservationOption(delReservationOptionList, modifyId);
	}

	/**
	* @Desc 추가 상품 저장
	* @param GoodsRegistAdditionalGoodsDto
	* @return int
	*/
	protected int addGoodsAdditionalGoodsMapping(GoodsRegistAdditionalGoodsDto goodsAdditionalGoodsMappingListDto) {
		return goodsRegistMapper.addGoodsAdditionalGoodsMapping(goodsAdditionalGoodsMappingListDto);
	}

	/**
	* @Desc 추천 상품 저장
	* @param GoodsRegistAdditionalGoodsDto
	* @return int
	*/
	protected int addGoodsRecommend(GoodsRegistAdditionalGoodsDto goodsRecommendDto) {
		return goodsRegistMapper.addGoodsRecommend(goodsRecommendDto);
	}

	/**
	* @Desc 배송정책
	* @param GoodsRegistRequestDto
	* @return List<GoodsRegistShippingTemplateVo>
	*/
	protected List<GoodsRegistShippingTemplateVo> goodsShippingTemplateList(GoodsRegistRequestDto goodsRegistRequestDto) {
		return goodsRegistMapper.goodsShippingTemplateList(goodsRegistRequestDto);
	}

	/**
	* @Desc 저장된 배송정책인지 체크
	* @param GoodsRegistRequestDto
	* @return int
	*/
	protected int goodsShippingTemplateChkNum(GoodsRegistShippingTemplateDto goodsRegistShippingTemplateDto) {
		return goodsRegistMapper.goodsShippingTemplateChkNum(goodsRegistShippingTemplateDto);
	}

	/**
	* @Desc 배송정책 수정
	* @param GoodsRegistRequestDto
	* @return int
	*/
	protected int updateGoodsShippingTemplate(GoodsRegistShippingTemplateDto goodsRegistShippingTemplateDto) {
		return goodsRegistMapper.updateGoodsShippingTemplate(goodsRegistShippingTemplateDto);
	}

	/**
	* @Desc 추가상품 삭제
	* @param GoodsRegistRequestDto
	* @return int
	*/
	protected int deleteGoodsAdditionalGoodsMapping(GoodsRegistRequestDto goodsRegistRequestDto) {
		return goodsRegistMapper.deleteGoodsAdditionalGoodsMapping(goodsRegistRequestDto);
	}

	/**
	* @Desc 추천상품 삭제
	* @param GoodsRegistRequestDto
	* @return int
	*/
	protected int deleteGoodsRecommend(GoodsRegistRequestDto goodsRegistRequestDto) {
		return goodsRegistMapper.deleteGoodsRecommend(goodsRegistRequestDto);
	}

	/**
	* @Desc 배송 유형에 따른 출고처
	* @param GoodsRegistItemWarehouseRequestDto
	* @return List<GoodsRegistItemWarehouseVo>
	*/
	protected List<GoodsRegistItemWarehouseVo> itemWarehouseList(GoodsRegistItemWarehouseRequestDto goodsRegistItemWarehouseRequestDto) {
		return goodsRegistMapper.itemWarehouseList(goodsRegistItemWarehouseRequestDto);
	}

	/**
	* @Desc 배송 유형에 따른 출고처(일일상품의 경우, 가맹점 배송만 처리)
	* @param GoodsRegistItemWarehouseRequestDto
	* @return List<GoodsRegistItemWarehouseVo>
	*/
	protected List<GoodsRegistItemWarehouseVo> dailyGoodsItemWarehouseList(GoodsRegistItemWarehouseRequestDto goodsRegistItemWarehouseRequestDto) {
		return goodsRegistMapper.dailyGoodsItemWarehouseList(goodsRegistItemWarehouseRequestDto);
	}

	/**
	* @Desc 출고처에 따른 배송 정책
	* @param GoodsRegistItemWarehouseRequestDto
	* @return List<GoodsRegistItemWarehouseVo>
	*/
	protected List<GoodsRegistItemWarehouseVo> itemWarehouseShippingTemplateList(GoodsRegistItemWarehouseRequestDto goodsRegistItemWarehouseRequestDto) {
		return goodsRegistMapper.itemWarehouseShippingTemplateList(goodsRegistItemWarehouseRequestDto);
	}

	/**
	* @Desc 상품 마스터 정보
	* @param GoodsRegistRequestDto
	* @return GoodsRegistVo
	*/
	protected GoodsRegistVo goodsDetail(GoodsRegistRequestDto goodsRegistRequestDto) {
		return goodsRegistMapper.goodsDetail(goodsRegistRequestDto);
	}

	/**
	* @Desc 예약판매옵션설정
	* @param GoodsRegistRequestDto
	* @return List<GoodsRegistReserveOptionVo>
	*/
	protected List<GoodsRegistReserveOptionVo> goodsReservationOptionList(GoodsRegistRequestDto goodsRegistRequestDto) {
		return goodsRegistMapper.goodsReservationOptionList(goodsRegistRequestDto);
	}

	/**
	* @Desc 추가상품
	* @param GoodsRegistRequestDto
	* @return List<GoodsRegistAdditionalGoodsVo>
	*/
	protected List<GoodsRegistAdditionalGoodsVo> goodsAdditionalGoodsMappingList(GoodsRegistRequestDto goodsRegistRequestDto) {
		return goodsRegistMapper.goodsAdditionalGoodsMappingList(goodsRegistRequestDto);
	}

	/**
	* @Desc 추천상품
	* @param GoodsRegistRequestDto
	* @return List<GoodsRegistAdditionalGoodsVo>
	*/
	protected List<GoodsRegistAdditionalGoodsVo> goodsRecommendList(GoodsRegistRequestDto goodsRegistRequestDto) {
		return goodsRegistMapper.goodsRecommendList(goodsRegistRequestDto);
	}

	/**
	* @Desc 상품 중복 확인(품목코드, 출고처 기준)
	* @param GoodsRegistRequestDto
	* @return List<GoodsRegistAdditionalGoodsVo>
	*/
	protected GoodsRegistVo duplicateGoods(GoodsRegistRequestDto goodsRegistRequestDto) {
		return goodsRegistMapper.duplicateGoods(goodsRegistRequestDto);
	}

	/**
	 * 가격정보 > 판매 가격정보
	 *
	 * @param	ilGoodsId, taxYn
	 * @return	GoodsPriceResultVo
	 * @throws	Exception
	 */
	protected List<GoodsPriceInfoResultVo> goodsPrice(String ilGoodsId, String taxYn) throws Exception{
		return goodsRegistMapper.goodsPrice(ilGoodsId, taxYn);
	}

	/**
	 * 가격정보 > 판매 가격정보 - 대상일시
	 *
	 * @param	ilGoodsId Long
	 * @param	taxYn String
	 * @return	List<GoodsPriceInfoResultVo>
	 * @throws	Exception
	 */
	protected List<GoodsPriceInfoResultVo> getGoodsPriceList(Long ilGoodsId, String dateTime) throws Exception {
		return goodsRegistMapper.getGoodsPriceList(ilGoodsId, dateTime);
	}

	/**
	 * 임직원 할인 정보 > 임직원 할인 가격정보
	 *
	 * @param	ilGoodsId, taxYn
	 * @return	GoodsPriceResultVo
	 * @throws	Exception
	 */
	protected List<GoodsPriceInfoResultVo> goodsEmployeePrice(String ilItemCode, String ilGoodsId, String taxYn) throws Exception{
		return goodsRegistMapper.goodsEmployeePrice(ilItemCode, ilGoodsId, taxYn);
	}

	/**
	 * 가격정보 > 행사/할인 내역
	 *
	 * @param	ilGoodsId, discountTypeCode
	 * @return	GoodsPriceResultVo
	 * @throws	Exception
	 */
	protected List<GoodsPriceInfoResultVo> goodsDiscountList(String ilGoodsId, String discountTypeCode) throws Exception{
		if(discountTypeCode.equals(GoodsEnums.GoodsDiscountType.ERP_EVENT.getCode())) {
			return goodsRegistMapper.goodsDiscountErpEventList(ilGoodsId, discountTypeCode);
		}
		else{
			return goodsRegistMapper.goodsDiscountList(ilGoodsId, discountTypeCode);
		}
	}


	/**
	 * 묶음상품 > 가격정보 > 묶음상품 기본 힐인가
	 *
	 * @param	ilGoodsId, discountTypeCode
	 * @return	GoodsPriceResultVo
	 * @throws	Exception
	 */
	protected List<GoodsPriceInfoResultVo> goodsPackageDiscountList(String ilGoodsId, String discountTypeCode) throws Exception{
		return goodsRegistMapper.goodsPackageDiscountList(ilGoodsId, discountTypeCode);
	}


	/**
	 * 가격정보 > 마스터 품목 가격정보
	 *
	 * @param	ilItemCode
	 * @return	GoodsPriceResultVo
	 * @throws	Exception
	 */
	protected List<GoodsPriceInfoResultVo> itemPriceList(String ilItemCode) throws Exception{
		return goodsRegistMapper.itemPriceList(ilItemCode);
	}

	/**
	 * 가격정보 > 행사/할인 내역 > 우선할인,즉시 할인 할인설정 승인 요청
	 *
	 * @param	goodsDiscountRequestDto
	 * @return
	 * @throws	Exception
	 */
	protected int addGoodsDiscountAppr(GoodsDiscountApprVo goodsDiscountApprVo) throws Exception {
		return goodsRegistMapper.addGoodsDiscountAppr(goodsDiscountApprVo);
	}

	/**
	 * 가격정보 > 행사/할인 내역 > 우선할인,즉시 할인 할인설정 승인 요청 히스토리
	 *
	 * @param	goodsDiscountRequestDto
	 * @return
	 * @throws	Exception
	 */
	protected int addGoodsDiscountApprStatusHistory(GoodsDiscountApprVo goodsDiscountApprVo) throws Exception {
		return goodsRegistMapper.addGoodsDiscountApprStatusHistory(goodsDiscountApprVo);
	}

	/**
	 * 가격정보 > 행사/할인 내역 > 우선할인,즉시할인 승인완료 후  할인내역 추가
	 *
	 * @param	String
	 * @return
	 * @throws	Exception
	 */
	protected int addGoodsDiscountByAppr(GoodsDiscountApprVo goodsDiscountApprVo) throws Exception {
		return goodsRegistMapper.addGoodsDiscountByAppr(goodsDiscountApprVo);
	}

	/**
	 * 묶음상품 > 가격정보 > 행사/할인 내역 > 할인 승인완료 후 묶음상품 개별품목 할인정보 승인 추가
	 *
	 * @param	String
	 * @return
	 * @throws	Exception
	 */
	protected int addGoodsPackageItemFixedDiscountPriceByAppr(GoodsDiscountApprVo goodsDiscountApprVo) throws Exception {
		return goodsRegistMapper.addGoodsPackageItemFixedDiscountPriceByAppr(goodsDiscountApprVo);
	}

	/**
	 * 상품 할인 승인 테이블에 상품할인ID UPDATE
	 *
	 * @param	GoodsDiscountApprVo
	 * @return
	 * @throws	Exception
	 */
	protected int updateGoodsDiscountAppr(GoodsDiscountApprVo goodsDiscountApprVo) throws Exception {
		return goodsRegistMapper.updateGoodsDiscountAppr(goodsDiscountApprVo);
	}

	/**
	 * 가격정보 > 행사/할인 내역 > 할인 내역 시작일이 유효한지 Validation check
	 * 할인 내역 저장시 시작일이 현재시각 이후인지 확인
	 *
	 * @param	goodsDiscountRequestDto
	 * @return	int
	 * @throws	Exception
	 */
	protected int isValidStartTimeAddGoodsDiscount(GoodsDiscountRequestDto goodsDiscountRequestDto) throws Exception {
		return goodsRegistMapper.isValidStartTimeAddGoodsDiscount(goodsDiscountRequestDto);
	}

	/**
	 * 가격정보 > 행사/할인 내역 > 할인 내역 저장 가능한지 Validation check
	 * 할인 내역 저장시 시작일 또는 종료일이 겹치는 항목이 존재하는지 확인
	 *
	 * @param	goodsDiscountRequestDto
	 * @return	int
	 * @throws	Exception
	 */
	protected int isValidAddGoodsDiscount(GoodsDiscountRequestDto goodsDiscountRequestDto) throws Exception {
		return goodsRegistMapper.isValidAddGoodsDiscount(goodsDiscountRequestDto);
	}


	/**
	 * 가격정보 > 행사/할인 내역 > 우선할인,즉시 할인 할인설정 승인 요청 저장
	 *
	 * @param	goodsDiscountRequestDto
	 * @return
	 * @throws	Exception
	 */
	protected int addGoodsDiscount(GoodsDiscountRequestDto goodsDiscountRequestDto) throws Exception {
		return goodsRegistMapper.addGoodsDiscount(goodsDiscountRequestDto);
	}

	/**
	 * 가격정보 > 행사/할인 내역 > 우선할인,즉시 할인 할인설정 승인 요청 수정
	 *
	 * @param	goodsDiscountRequestDto
	 * @return
	 * @throws	Exception
	 */
	protected int modifyGoodsDiscount(GoodsDiscountRequestDto goodsDiscountRequestDto) throws Exception {
		return goodsRegistMapper.modifyGoodsDiscount(goodsDiscountRequestDto);
	}

	/**
	* @Desc 묶음 상품 > 기준상품, 묶음 상품 정보 불러오기
	* @param GoodsRegistRequestDto
	* @return GoodsRegistVo
	*/
	protected List<GoodsRegistVo> getGoodsList(GoodsRegistRequestDto goodsRegistRequestDto) {
		return goodsRegistMapper.getGoodsList(goodsRegistRequestDto);
	}

	/**
	* @Desc 묶음 상품 > 상품정보 제공고시 불러오기
	* @param GoodsRegistRequestDto
	* @return GoodsRegistVo
	*/
	protected List<GoodsRegistVo> getGoodsInfoAnnounceList(GoodsRegistRequestDto goodsRegistRequestDto) {
		return goodsRegistMapper.getGoodsInfoAnnounceList(goodsRegistRequestDto);
	}

	/**
	* @Desc 묶음 상품 > 상품 영양정보 불러오기
	* @param GoodsRegistRequestDto
	* @return GoodsRegistVo
	*/
	protected List<GoodsRegistVo> getGoodsNutritionList(GoodsRegistRequestDto goodsRegistRequestDto) {
		return goodsRegistMapper.getGoodsNutritionList(goodsRegistRequestDto);
	}

	/**
	* @Desc 묶음 상품 이미지 등록
	* @param GoodsImageVo
	* @return int
	*/
	protected int addGoodsImage(GoodsImageVo addGoodsImageVo) {
		return goodsRegistMapper.addGoodsImage(addGoodsImageVo);
	}

	/**
	* @Desc 묶음 상품, 증정품 목록 저장
	* @param GoodsRegistPackageGoodsMappingDto
	* @return int
	*/
	protected int addPackageGoodsMapping(GoodsRegistPackageGoodsMappingDto goodsRegistPackageGoodsMappingDto) {
		return goodsRegistMapper.addPackageGoodsMapping(goodsRegistPackageGoodsMappingDto);
	}

	/**
	* @Desc 묶음상품 원본 가격 입력
	* @param GoodsRegistPackageGoodsPriceDto
	* @return int
	*/
	protected int addGoodsPackagePriceOrig(GoodsRegistPackageGoodsPriceDto goodsRegistPackageGoodsItemPriceMappingDto) {
		return goodsRegistMapper.addGoodsPackagePriceOrig(goodsRegistPackageGoodsItemPriceMappingDto);
	}

	/**
	* @Desc 묶음 상품 > 묶음상품 구성 정보 리스트
	* @param String
	* @return GoodsPackageGoodsMappingVo
	 * @throws Exception
	*/
	protected List<GoodsPackageGoodsMappingVo> goodsPackageGoodsMappingList(String ilGoodsId, String imageSortYn) throws Exception {
		return goodsRegistMapper.goodsPackageGoodsMappingList(ilGoodsId, imageSortYn);
	}

	/**
	* @Desc 묶음상품 개별품목 고정가 할인가격 저장
	* @param GoodsRegistPackageGoodsPriceDto
	* @return int
	*/
	protected int addGoodsPackageItemFixedDiscountPrice(GoodsRegistPackageGoodsPriceDto goodsPackageItemFixedDiscountPrice) {
		return goodsRegistMapper.addGoodsPackageItemFixedDiscountPrice(goodsPackageItemFixedDiscountPrice);
	}

	/**
	 * @Desc 묶음상품 개별품목 할인정보 승인 저장
	 * @param GoodsPackageItemFixedDiscountPriceApprVo
	 * @return int
	 */
	protected int addGoodsPackageItemFixedDiscountPriceAppr(GoodsPackageItemFixedDiscountPriceApprVo goodsPackageItemFixedDiscountPriceApprVo){
		return goodsRegistMapper.addGoodsPackageItemFixedDiscountPriceAppr(goodsPackageItemFixedDiscountPriceApprVo);
	}

	/**
	* @Desc 묶음 상품 > 묶음상품 전용 이미지 리스트
	* @param String
	* @return GoodsRegistImageVo
	* @throws Exception
	*/
	protected List<GoodsRegistImageVo> goodsImageList(String ilGoodsId) throws Exception {
		return goodsRegistMapper.goodsImageList(ilGoodsId);
	}

	/**
	* @Desc 묶음 상품 > 묶음상품 전용 이미지 삭제
	* @param String
	* @return int
	* @throws Exception
	*/
	protected int delGoodsImage(String ilGoodsId) {
		return goodsRegistMapper.delGoodsImage(ilGoodsId);
	}

	/**
	* @Desc 일일 상품 > 일일 판매 옵션 설정 > 식단 주기 입력
	* @param GoodsDailyCycleBulkVo
	* @return int
	*/
	protected int addGoodsDailyCycle(GoodsDailyCycleBulkVo addGoodsDailyCycleVo) {
		return goodsRegistMapper.addGoodsDailyCycle(addGoodsDailyCycleVo);
	}

	/**
	* @Desc 일일 상품 > 일일 판매 옵션 설정 > 일괄 배달 설정 입력
	* @param GoodsDailyCycleBulkVo
	* @return int
	*/
	protected int addGoodsDailyBulk(GoodsDailyCycleBulkVo addGoodsDailyBulkVo) {
		return goodsRegistMapper.addGoodsDailyBulk(addGoodsDailyBulkVo);
	}

	/**
	* @Desc 일일 상품 > 일일 판매 옵션 설정 > 식단 주기 리스트
	* @param String
	* @return GoodsDailyCycleBulkVo
	*/
	protected List<GoodsDailyCycleBulkVo> goodsDailyCycleList(String ilGoodsId) throws Exception {
		return goodsRegistMapper.goodsDailyCycleList(ilGoodsId);
	}

	/**
	* @Desc 일일 상품 > 일일 판매 옵션 설정 > 식단 주기 리스트
	* @param String
	* @return GoodsDailyCycleBulkVo
	*/
	protected List<GoodsDailyCycleBulkVo> goodsDailyBulkList(String ilGoodsId) throws Exception {
		return goodsRegistMapper.goodsDailyBulkList(ilGoodsId);
	}

	/**
	* @Desc 일일상품 > 일일 판매 옵션 설정 > 식단주기 삭제
	* @param String
	* @return int
	* @throws Exception
	*/
	protected int delGoodsDailyCycle(String ilGoodsId) {
		return goodsRegistMapper.delGoodsDailyCycle(ilGoodsId);
	}

	/**
	* @Desc 일일상품 > 일일 판매 옵션 설정 > 일괄 배달 설정 삭제
	* @param String
	* @return int
	* @throws Exception
	*/
	protected int delGoodsDailyBulk(String ilGoodsId) {
		return goodsRegistMapper.delGoodsDailyBulk(ilGoodsId);
	}

	/**
	* @Desc 묶음 상품 > 기본할인 등록 > 가격 계산 상품 리스트
	* @param GoodsRegistRequestDto
	* @return GoodsRegistVo
	*/
	protected List<GoodsPackageCalcListVo> goodsPackageCalcList(GoodsRegistRequestDto goodsRegistRequestDto) {
		return goodsRegistMapper.goodsPackageCalcList(goodsRegistRequestDto);
	}

	/**
	* @Desc 묶음 상품 > 묶음 상품 가격정보 저장(Procedure 호출)
	* @param GoodsRegistRequestDto
	* @return int
	*/
	protected int spGoodsPriceUpdateWhenPackageGoodsChanges(GoodsRegistRequestDto goodsRegistRequestDto) {
		return goodsRegistMapper.spGoodsPriceUpdateWhenPackageGoodsChanges(goodsRegistRequestDto);
	}

	/**
	* @Desc 일반상품 > 상품 가격정보 저장/Update, 품목 가격정보 Update(프로시져 호출)
	* @param String
	* @return void
	*/
	protected void spGoodsPriceUpdateWhenItemPriceChanges(String ilItemCode) throws Exception {

		if (ilItemCode == null || "".equals(ilItemCode)) { // validation check
			return;
		}

		if (goodsRegistMapper.getGoodsCountByItemCode(ilItemCode) > 0) { // 해당 품목코드의 상품이 있을 경우에만 프로시저 호출
			boolean isDebugFlag = false;
			goodsRegistMapper.spGoodsPriceUpdateWhenItemPriceChanges(ilItemCode, isDebugFlag);
		}
	}

	/**
	* @Desc 묶음상품 > 상품 가격정보 저장/Update, 품목 가격정보 Update(프로시져 호출)
	* @param
	* @return void
	*/
	protected void spPackageGoodsPriceUpdateWhenItemPriceChanges() {
		boolean isDebugFlag = false;
		goodsRegistMapper.spPackageGoodsPriceUpdateWhenItemPriceChanges(isDebugFlag);
	}

	/**
	* @Desc 일반상품 저장시 기본 상품 가격정보 생성(프로시져 호출)
	* @param String
	* @return void
	*/
	protected void spGoodsPriceUpdateWhenGoodsRegist(String ilGoodsId) {
		boolean isDebugFlag = false;
		goodsRegistMapper.spGoodsPriceUpdateWhenGoodsRegist(ilGoodsId, isDebugFlag);
	}

	/**
	* @Desc 묶음 상품 > 묶음상품 기본 판매가 > 기존 종료일 변경
	* @param String
	* @return int
	*/
	protected int updateGoodsDiscountEndTime(String ilGoodsId, String newGoodsDiscountStartTime) {
		return goodsRegistMapper.updateGoodsDiscountEndTime(ilGoodsId, newGoodsDiscountStartTime);
	}

	/**
	 * @Desc 묶음 상품 > 묶음상품 기본 판매가 승인 > 기존 종료일 변경
	 * @param String
	 * @return int
	 */
	protected int updateGoodsDiscountApprEndTime(String ilGoodsId, String newGoodsDiscountStartTime) {
		return goodsRegistMapper.updateGoodsDiscountApprEndTime(ilGoodsId, newGoodsDiscountStartTime);
	}

	/**
	 * 묶음상품 > 임직원 할인 정보 > 임직원 할인 가격정보
	 *
	 * @param	ilGoodsId
	 * @return	GoodsPriceResultVo
	 * @throws	Exception
	 */
	protected List<GoodsPriceInfoResultVo> goodsPackageEmployeePriceList(String ilGoodsId) {
		return goodsRegistMapper.goodsPackageEmployeePriceList(ilGoodsId);
	}

	/**
	 * 일반상품 > 임직원 할인 정보 > 임직원 기본할인 정보
	 *
	 * @param	ilItemCode
	 * @return	GoodsPriceResultVo
	 * @throws	Exception
	 */
	protected GoodsPriceInfoResultVo goodsBaseDiscountEmployeeList(String ilItemCode) throws Exception{
		return goodsRegistMapper.goodsBaseDiscountEmployeeList(ilItemCode);
	}

	/**
	 * 묶음상품 > 임직원 할인 정보 > 임직원 기본할인 정보
	 *
	 * @param	ilGoodsId
	 * @return	GoodsPriceResultVo
	 * @throws	Exception
	 */
	protected List<GoodsPriceInfoResultVo> goodsPackageBaseDiscountEmployeeList(String ilGoodsId) throws Exception{
		return goodsRegistMapper.goodsPackageBaseDiscountEmployeeList(ilGoodsId);
	}

	/**
	 * 묶음상품 > 임직원 할인 정보 > 임직원 개별할인 정보
	 *
	 * @param	ilGoodsId
	 * @return	GoodsPriceResultVo
	 * @throws	Exception
	 */
	protected List<GoodsPriceInfoResultVo> goodsPackageDiscountEmployeeList(String ilGoodsId) throws Exception{
		return goodsRegistMapper.goodsPackageDiscountEmployeeList(ilGoodsId);
	}

	/**
	* @Desc 일반상품 > 판매 정보 > 일일 판매 옵션 설정 > 예약판매 > 주문수집I/F일, 출고예정일, 도착예정일 산출
	* @param GoodsRegistRequestDto
	* @return GoodsRegistReserveOptionVo
	*/
	protected GoodsRegistReserveOptionVo goodsReservationDateCalc(GoodsRegistRequestDto goodsRegistRequestDto) {
		return goodsRegistMapper.goodsReservationDateCalc(goodsRegistRequestDto);
	}

	/**
	 * @Desc  일반상품 > 판매 정보 > 일일 판매 옵션 설정 > 예약판매 > 주문수집I/F일 배송패턴에 따른 요일 날짜 제한
	 * @param String
	 * @return goodsRegistResponseDto
	 * @throws Exception
	 */
	protected List<GoodsRegistReserveOptionVo> orderIfDateLimitList(String urWarehouseId) throws Exception {
		return goodsRegistMapper.orderIfDateLimitList(urWarehouseId);
	}

	/**
	 * @Desc  일반, 일일상품 > 혜택/구매 정보 > 증정행사 정보
	 * @param String
	 * @return goodsRegistResponseDto
	 * @throws Exception
	 */
	protected List<ExhibitGiftResultVo> exhibitGiftList(String ilGoodsId) throws Exception {
		return goodsRegistMapper.exhibitGiftList(ilGoodsId);
	}

	/**
	 * @Desc  판매/전시 > 재고운영형태, 전일마감재고 정보
	 * @param goodsRegistRequestDto
	 * @return goodsRegistResponseDto
	 * @throws Exception
	 */
	protected GoodsRegistStockInfoVo goodsStockInfo(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception{
		return goodsRegistMapper.goodsStockInfo(goodsRegistRequestDto);
	}

	/**
	 * @Desc  묶음상품 > 상품 이미지 > 개별상품 대표이미지 정렬 순서 UPDATE
	 * @param String
	 * @return int
	 * @throws Exception
	 */
	protected int updateGoodsPackageGoodsMappingImageOrderUpdate(String targetGoodsId, int imageSortSeq, String ilGoodsId) {
		return goodsRegistMapper.updateGoodsPackageGoodsMappingImageOrderUpdate(targetGoodsId, imageSortSeq, ilGoodsId);
	}

	/**
	 * @Desc  품목 판매불가에 따른 상품 판매중지 대상 찾기
	 * @param String
	 * @return List<GoodsRegistVo>
	 * @throws Exception
	 */
	protected List<GoodsRegistVo> itemExtinctionGoodsStopSaleList(String ilItemCode) throws Exception {
		return goodsRegistMapper.itemExtinctionGoodsStopSaleList(ilItemCode);
	}

	/**
	* @Desc 품목 판매불가에 따른 상품 판매상태 판매중지 처리
	* @param List<String>
	* @return int
	*/
	protected int updateGoodsStopSale(List<String> ilGoodsIds, String saleStatus) {
		return goodsRegistMapper.updateGoodsStopSale(ilGoodsIds, saleStatus);
	}

	/**
	* @Desc 품목 판매불가에 따른 상품 판매상태 판매중지 처리
	* @param String[]
	* @return GoodsRegistVo
	* // HGRM-4246 기존 묶음상품 구성 체크 로직 변경 / 2021.01.31 이명수
	*/
	protected GoodsRegistVo goodsPackageExistChk(Map<String, Object> params) throws Exception {
		return goodsRegistMapper.goodsPackageExistChk(params);
	}

	/**
	* @Desc 체크된 묶음 상품의 구성 상품과 동일한 수량이 있는지 체크
	* @param GoodsRegistPackageGoodsMappingDto
	* @return int
	*/
	protected int goodsPackageQuantityExistChk(GoodsRegistPackageGoodsMappingDto goodsRegistPackageGoodsMappingDto) throws Exception {
		return goodsRegistMapper.goodsPackageQuantityExistChk(goodsRegistPackageGoodsMappingDto);
	}

	/**
	* @Desc 상품 변경내역 저장
	* @param GoodsRegistRequestDto
	* @return int
	*/
	protected int addGoodsChangeLog(GoodsChangeLogVo goodsChangeLogVo) {
		return goodsRegistMapper.addGoodsChangeLog(goodsChangeLogVo);
	}

	/**
	* @Desc  상품 승인 내역 저장
	* @param GoodsRegistApprVo
	* @return int
	*/
	protected int addGoodsAppr(GoodsRegistApprVo goodsRegistApprVo) {
		return goodsRegistMapper.addGoodsAppr(goodsRegistApprVo);
	}

	/**
	* @Desc  상품 승인 상태 이력 저장
	* @param GoodsRegistApprVo
	* @return int
	*/
	protected int addGoodsApprStatusHistory(GoodsRegistApprVo goodsRegistApprVo) {
		return goodsRegistMapper.addGoodsApprStatusHistory(goodsRegistApprVo);
	}

	/**
	* @Desc  상품 승인 내역 수정(1,2차 승인자 변경)
	* @param GoodsRegistApprVo
	* @return int
	*/
	protected int updateGoodsAppr(GoodsRegistApprVo goodsRegistApprVo) {
		return goodsRegistMapper.updateGoodsAppr(goodsRegistApprVo);
	}

	/**
	* @Desc  상품 승인 내역 확인, 승인 내역 존재시 요청 자격 확인
	* @param String
	* @return GoodsRegistApprVo
	*/
	protected GoodsRegistApprVo goodsApprInfo(String userId, String ilGoodsId, String ilGoodsApprid, String apprKindTp)  throws Exception {
		return goodsRegistMapper.goodsApprInfo(userId, ilGoodsId, ilGoodsApprid, apprKindTp);
	}

	/**
	* @Desc  상품 승인 완료시 상품 판매상태, 상품 정보 변경
	* @param String
	* @return int
	*/
	protected int updateGoodsApprovedSaleStatus(GoodsRegistApprVo goodsRegistApprVo) {
		return goodsRegistMapper.updateGoodsApprovedSaleStatus(goodsRegistApprVo);
	}

	/**
	* @Desc  상품 승인 요청시 상품 판매상태 변경
	* @param String
	* @return int
	*/
	protected int updateGoodsRequestSaleStatus(GoodsRegistApprVo goodsRegistApprVo) {
		return goodsRegistMapper.updateGoodsRequestSaleStatus(goodsRegistApprVo);
	}

	/**
	* @Desc  상품 승인 상태 항목
	* @param String
	* @return GoodsApprovalResultVo
	*/
	protected List<GoodsApprovalResultVo> goodsApprStatusList(String ilGoodsId) throws Exception {
		return goodsRegistMapper.goodsApprStatusList(ilGoodsId);
	}


	/**
	* @Desc  품목 승인 요청시 상품 판매 대기 상태로 변경
	* @param String
	* @return int
	*/
	protected int updateGoodsSaleStatusToWaitByItemAppr(String ilItemCode) throws Exception {
		return goodsRegistMapper.updateGoodsSaleStatusToWaitByItemAppr(ilItemCode);
	}

	/**
	 * @Desc 품목 승인 요청시 상품 판매 대기 상태 변경내역 저장
	 * @param goodsChangeLogVo
	 * @return int
	 */
	protected int addGoodsChangeLogUpdateByItemAppr(GoodsChangeLogVo goodsChangeLogVo, String ilItemCode) {
		return goodsRegistMapper.addGoodsChangeLogUpdateByItemAppr(goodsChangeLogVo, ilItemCode);
	}

	/**
	* @Desc  품목 수정 승인시 상품의 판매 상태 원상 복귀
	* @param String
	* @return int
	*/
	protected int updateGoodsSaleStatusToBackByItemAppr(String ilItemCode) throws Exception {
		return goodsRegistMapper.updateGoodsSaleStatusToBackByItemAppr(ilItemCode);
	}

	/**
	 * @Desc 품목 수정 승인시 상품의 판매 상태 원상 복귀 변경내역 저장
	 * @param goodsChangeLogVo
	 * @return int
	 */
	protected int addGoodsChangeLogBackByItemAppr(GoodsChangeLogVo goodsChangeLogVo, String ilItemCode) {
		return goodsRegistMapper.addGoodsChangeLogBackByItemAppr(goodsChangeLogVo, ilItemCode);
	}

	/**
	 * @Desc  품목 승인에 의한 판매상태 변경 시 상품의 판매 상태 변경 처리(묶음상품 제외)
	 * @param String
	 * @return int
	 */
	protected GoodsDiscountApprVo goodsDiscountApprInfo(String ilGoodsId, String ilGoodsDiscountApprId, String discountType) throws Exception {
		return goodsRegistMapper.goodsDiscountApprInfo(ilGoodsId, ilGoodsDiscountApprId, discountType);
	}

	/**
	 * 풀무원샵 상품코드 조회
	 * @param ilGoodsId
	 */
	protected List<GoodsCodeVo> getGoodsCodeList(String ilGoodsId) {
		return goodsCodeMapper.getGoodsCodeList(ilGoodsId);
	}

	/**
	 * 식단 스케쥴 조회
	 * @param ilGoodsId
	 */
	protected List<MealPatternListVo> getMealScheduleList(String ilGoodsId) {
		return goodsRegistMapper.getMealScheduleList(ilGoodsId);
	}

	/**
	 * 풀무원샵 상품코드 저장
	 * @param ilGoodsId
	 * @param goodsCodeList
	 * @param createId
	 */
	protected void setGoodsCodeList(String ilGoodsId, List<GoodsCodeVo> goodsCodeList, String createId) {
		if(goodsCodeList != null) {
			if (!goodsCodeList.isEmpty()) {
				goodsCodeMapper.delAllGoodsCode(ilGoodsId);
				goodsCodeMapper.addGoodsCode(ilGoodsId, goodsCodeList, createId);
			}
		}
    }

	/**
	 * 풀무원샵 상품코드 정보가 있는지 체크
	 * @param goodsCodeVoList
	 * @return
	 * @throws Exception
	 */
	protected List<GoodsCodeVo> goodsCodeExistChk(List<GoodsCodeVo> goodsCodeList) throws Exception {
		return goodsCodeMapper.goodsCodeExistChk(goodsCodeList);
	}

	protected List<GoodsVo> getPackageGoodsListByMappingGoods(String ilGoodsIid) throws Exception {
			return goodsPriceMapper.getPackageGoodsListByMappingGoods(ilGoodsIid);
	}

	/**
	 * 묶음 구성상품 선물하기 허용 여부
	 * @param ilGoodsId
	 * @return
	 */
	protected List<String> getPresentYnsByPackageGoodsId(String ilGoodsId) {
		return goodsRegistMapper.getPresentYnsByPackageGoodsId(ilGoodsId);
	}

	/**
	 * 선물하기 허용 여부 update
	 * @param ilGoodsId
	 * @param presentYn
	 */
	protected void updateGoodsPresentYn(String ilGoodsId, String presentYn) {
		goodsRegistMapper.updateGoodsPresentYn(ilGoodsId, presentYn);
	}
}
