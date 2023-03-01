package kr.co.pulmuone.v1.search.searcher.dto;

import kr.co.pulmuone.v1.comm.enums.GoodsEnums.DeviceType;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.search.searcher.constants.SortCode;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreGoodsSearchRequestDto
{

	/**
	 * 검색 키워드
	 */
	private String keyword;

	/**
	 * 상품코드 리스트
	 */
	private List<Long> goodsIdList;

	/**
	 * 전시 대카테고리 코드
	 */
	private String lev1CategoryId;

	/**
	 * 전시 중카테고리 코드
	 */
	private String lev2CategoryId;

	/**
	 * 전시 소카테고리 코드
	 */
	private String lev3CategoryId;

	/**
	 * 몰 코드
	 */
	private String mallId;

	/**
	 * 브랜드 코드 리스트
	 */
	private List<String> brandIdList;

	/**
	 * 혜택유형코드 리스트
	 */
	private List<String> benefitTypeIdList;

	/**
	 * 인증유형코드 리스트
	 */
	private List<String> certificationTypeIdList;

	/**
	 * 배송유형코드 리스트
	 */
	private List<String> deliveryTypeIdList;

	/**
	 * 보관방법코드 리스트
	 */
	private List<String> storageMethodIdList;

	/**
	 * 최소 가격
	 */
	private int minimumPrice = 0;

	/**
	 * 최대 가격
	 */
	private int maximumPrice = 0;

	/**
	 * 일시품절상품 제외 여부
	 */
	private boolean excludeSoldOutGoods;

	/**
	 * 검색어 저장여부
	 */
	@Setter
	private String isSaveKeyword;

	/**
	 * 첫검색 여부
	 */
	private boolean isFirstSearch;

	/**
	 * offset 에서 시작하여 반환할 문서 개수
	 */
	private int limit = 50;

	/**
	 * 리스팅 페이지
	 */
	private int page = 1;

	/**
	 * 정렬코드
	 */
	private SortCode sortCode = SortCode.NEW;

	/**
	 * 구매허용범위
	 */
	private String purchaseTargetType;


	/**
	 * 전시 허용 범위;
	 * PC/Mobile/App
	 */
	@Setter
	private DeviceType deviceType;


	/**
	 * 임직원 로그인 여부
	 */
	private boolean isEmployee;

	public void setIsEmployee(boolean isEmployee) {
		this.isEmployee = isEmployee;
		setSortCodeEmployee();
	}

	/**
	 * 할인 상품만 조회
	 */
	private boolean isDiscountGoods;

	/**
	 * 최소 할인 비율
	 */
	private int minimumDiscountRate = 0;

	/**
	 * 최대 할인 비율
	 */
	private int maximumDiscountRate = 0;

	/**
	 * 매장 ID
	 */
	private String storeId;

	/**
	 * 디바이스 타입 조회
	 *
	 * @return
	 */
	public DeviceType getDeviceType() {
		if(this.deviceType == null) {
			return DeviceUtil.getGoodsEnumDeviceTypeByUserDevice();
		} else {
			return this.deviceType;
		}
	}

	/**
	 * fetch 할 시작 지점
	 */
	public int offset()
	{
		if(this.page > 1){
			return (this.page - 1) * this.limit;
		}
		return 0;
	}



	/**
	 * 가격 범위 파라미터 값이 있는지 여부
	 *
	 * @return
	 */
	public boolean hasPriceFilterValue()
	{
		return this.maximumPrice > 0 && this.minimumPrice <= this.maximumPrice;
	}

	/**
	 * 키워드 값이 있는지 여부
	 *
	 * @return
	 */
	public boolean hasKeyword()
	{
		return StringUtil.isNotEmpty(this.keyword);
	}

	/**
	 * 몰ID 값이 있는지 여부
	 *
	 * @return
	 */
	public boolean hasMallId()
	{
		return StringUtil.isNotEmpty(this.mallId);
	}

	/**
	 * lev1 카테고리 값이 있는지 여부
	 *
	 * @return
	 */
	public boolean hasLev1CategoryId()
	{
		return StringUtil.isNotEmpty(this.lev1CategoryId);
	}

	/**
	 * lev2 카테고리 값이 있는지 여부
	 *
	 * @return
	 */
	public boolean hasLev2CategoryId()
	{
		return StringUtil.isNotEmpty(this.lev2CategoryId);
	}

	/**
	 * lev3 카테고리 값이 있는지 여부
	 *
	 * @return
	 */
	public boolean hasLev3CategoryId()
	{
		return StringUtil.isNotEmpty(this.lev3CategoryId);
	}

	/**
	 * 브랜드ID 값이 있는지 여부
	 *
	 * @return
	 */
	public boolean hasBrandIdList()
	{
		return CollectionUtils.isNotEmpty((this.getBrandIdList()));
	}

	/**
	 * 배송유형ID 값이 있는지 여부
	 *
	 * @return
	 */
	public boolean hasDeliveryTypeIdList()
	{
		return CollectionUtils.isNotEmpty((this.getDeliveryTypeIdList()));
	}

	/**
	 * 혜택유형ID 값이 있는지 여부
	 *
	 * @return
	 */
	public boolean hasBenefitTypeIdList()
	{
		return CollectionUtils.isNotEmpty((this.getBenefitTypeIdList()));
	}

	/**
	 * 보관방법ID 값이 있는지 여부
	 *
	 * @return
	 */
	public boolean hasStorageMethodIdList()
	{
		return CollectionUtils.isNotEmpty((this.getStorageMethodIdList()));
	}

	/**
	 * 인증유형ID 값이 있는지 여부
	 *
	 * @return
	 */
	public boolean hasCertificationTypeIdList()
	{
		return CollectionUtils.isNotEmpty((this.getCertificationTypeIdList()));
	}

	/**
	 * 상품ID 값이 있는지 여부
	 *
	 * @return
	 */
	public boolean hasGoodsIdList()
	{
		return CollectionUtils.isNotEmpty((this.getGoodsIdList()));
	}

	/**
	 * 전시 채널 값이 있는지 여부 (전시 허용 범위)
	 * @return
	 */
	public boolean hasDisplayChannel() {
		return StringUtil.isNotEmpty(this.getDeviceType());
	}

	/**
	 * 구매허용범위 값이 있는지 여부
	 * @return
	 */
	public boolean hasPurchaseTargetType() {
		return StringUtil.isNotEmpty(this.purchaseTargetType);
	}

	/**
	 * 검색어 디코딩
	 * @throws UnsupportedEncodingException
	 */
	public void decodeKeyword() throws UnsupportedEncodingException {
		this.keyword = URLDecoder.decode(this.keyword, "UTF-8");
	}

	/**
	 * 할인 범위 파라미터 값이 있는지 여부
	 *
	 * @return
	 */
	public boolean hasDiscountRateFilterValue() {
		return this.maximumDiscountRate > 0 && this.minimumDiscountRate <= this.maximumDiscountRate;
	}

	/**
	 * 매장 ID 값이 있는지 여부
	 *
	 * @return
	 */
	public boolean hasStoreId()
	{
		return StringUtil.isNotEmpty(this.storeId);
	}

	@Builder
	public StoreGoodsSearchRequestDto(String keyword, String lev1CategoryId, String lev2CategoryId, String lev3CategoryId, String mallId
			, List<String> brandIdList, List<String> benefitTypeIdList, List<String> deliveryTypeIdList, List<String> storageMethodIdList
			, List<String> certificationTypeIdList, Integer minimumPrice, Integer maximumPrice, Integer page, Integer limit
			, Boolean excludeSoldOutGoods, Boolean isFirstSearch, SortCode sortCode, List<Long> goodsIdList, DeviceType deviceType
			, Boolean isEmployee, String purchaseTargetType, Boolean isDiscountGoods, Integer minimumDiscountRate, Integer maximumDiscountRate, String storeId)
	{

		this.keyword = keyword;
		this.lev1CategoryId = lev1CategoryId;
		this.lev2CategoryId = lev2CategoryId;
		this.lev3CategoryId = lev3CategoryId;
		this.mallId = mallId;
		this.brandIdList = brandIdList;
		this.benefitTypeIdList = benefitTypeIdList;
		this.deliveryTypeIdList = deliveryTypeIdList;
		this.storageMethodIdList = storageMethodIdList;
		this.certificationTypeIdList = certificationTypeIdList;
		this.minimumPrice = minimumPrice == null ? this.minimumPrice : minimumPrice;
		this.maximumPrice = maximumPrice == null ? this.maximumPrice : maximumPrice;
		this.excludeSoldOutGoods = excludeSoldOutGoods == null ? false : excludeSoldOutGoods;
		this.isFirstSearch = isFirstSearch == null ? false : isFirstSearch;
		this.limit = limit == null ? this.limit : limit;
		this.page = page == null ? this.page : page;
		this.goodsIdList = goodsIdList;
		this.isDiscountGoods = isDiscountGoods == null ? false : isDiscountGoods;
		this.minimumDiscountRate = minimumDiscountRate == null ? this.minimumDiscountRate : minimumDiscountRate;
		this.maximumDiscountRate = maximumDiscountRate == null ? this.maximumDiscountRate : maximumDiscountRate;

		this.deviceType = deviceType;

		this.isEmployee = isEmployee == null ? false : isEmployee;
		this.sortCode = sortCode == null ? this.sortCode : sortCode;

		setSortCodeEmployee();

		this.purchaseTargetType = purchaseTargetType;
		this.storeId = storeId;
	}

	private void setSortCodeEmployee() {
		if( this.isEmployee ) {
			if( SortCode.LOW_PRICE == this.sortCode ) this.sortCode = SortCode.EMPLOYEE_LOW_PRICE;
			if( SortCode.HIGH_PRICE == this.sortCode ) this.sortCode = SortCode.EMPLOYEE_HIGH_PRICE;
			if( SortCode.HIGH_DISCOUNT_RATE == this.sortCode ) this.sortCode = SortCode.HIGH_EMPLOYEE_DISCOUNT_RATE;
		}
	}
}
