package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.framework.dto.UploadFileDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsCodeVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsDailyCycleBulkVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품 등록/수정 Request")
public class GoodsRegistRequestDto extends BaseRequestPageDto implements Cloneable {

	//마스터품목 내역 관련 Request
	@ApiModelProperty(value = "품목코드", required = true)
	private String ilItemCode;

	@ApiModelProperty(value = "공급사 ID", required = true)
	private String urSupplierId;

	@ApiModelProperty(value = "출고처 ID", required = true)
	private String urWarehouseId;

	@ApiModelProperty(value = "브랜드 ID", required = true)
	private String urBrandId;

	@ApiModelProperty(value = "상품 유형 ID", required = true)
	private String goodsType;

	@ApiModelProperty(value = "ERP 품목연동 여부", required = true)
	private String erpItemLinkYn;

	@ApiModelProperty(value = "상품 PK")
	private String ilGoodsIds[];

	/* 기본 정보 시작 */
	//상품 마스터
	@ApiModelProperty(value = "상품 ID", required = true)
	private String ilGoodsId;

	@ApiModelProperty(value = "등록 or 수정 상태", required = false)
	private String pageMode;

	@ApiModelProperty(value = "상품명", required = true)
	private String goodsName;

	@ApiModelProperty(value = "표장용량 구성정보 노출여부", required = false)
	private String packageUnitDisplayYn;

	@ApiModelProperty(value = "표장용량 구성정보 노출 내용", required = false)
	private String packageUnitDesc;

	@ApiModelProperty(value = "MD추천 노출여부", required = false)
	private String mdRecommendYn;

	@ApiModelProperty(value = "프로모션 상품명", required = false)
	private String promotionName;

	@ApiModelProperty(value = "프로모션 상품명 시작일", required = false)
	private String promotionNameStartDate;

	@ApiModelProperty(value = "프로모션 상품명 종료일", required = false)
	private String promotionNameEndDate;

	@ApiModelProperty(value = "상품설명", required = false)
	private String goodsDesc;

	@ApiModelProperty(value = "키워드 입력", required = false)
	private String searchKeyword;

	@ApiModelProperty(value = "카테고리 구분자(전시/몰인몰)", required = false)
	private String mallDiv;

	@ApiModelProperty(value = "전시카테고리 설정 리스트")
	private List<GoodsRegistCategoryRequestDto> displayCategoryList;

	@ApiModelProperty(value = "몰인몰카테고리 설정 리스트")
	private List<GoodsRegistCategoryRequestDto> mallInMallCategoryList;

	@ApiModelProperty(value = "상품 카테고리 ID")
	private String ilGoodsCtgryId;
	/* 기본 정보 끝 */

	/* 묶음상품 기본설정 시작*/
	@ApiModelProperty(value = "묶음상품 선택목록")
	private List<GoodsRegistPackageGoodsMappingDto> selectPackageGoodsList;

	@ApiModelProperty(value = "묶음상품 구성목록")
	private List<GoodsRegistPackageGoodsMappingDto> assemblePackageGoodsList;

	@ApiModelProperty(value = "증정품 구성목록")
	private List<GoodsRegistPackageGoodsMappingDto> assembleGiftGoodsList;

	@ApiModelProperty(value = "묶음상품 판매가 입력 > 할인유형(최초 묶음상품 기본 할인가 생성시 사용)")
	private String packageSaleType;

	@ApiModelProperty(value = "묶음상품 판매가 입력 > 묶음상품 판매가의 합계(최초 묶음상품 기본 할인가 생성시 사용)")
	private int totalSalePriceGoods;

	@ApiModelProperty(value = "할인율")
	private double saleRate;

	@ApiModelProperty(value = "묶음상품 가격 증정품 포함여부")
	private boolean isGiftGoodsYn = false;
	/* 묶음상품 기본설정 끝*/

	/* 상세정보 시작 */
	@ApiModelProperty(value = "유통기간")
	private int distributionPeriod;
	/* 상세정보 끝 */

	/* 판매/전시 시작 */
	@ApiModelProperty(value = "구매허용", required = false)
	private String[] purchaseTargetType;

	@ApiModelProperty(value = "회원 구매여부", required = false)
	private String purchaseMemberYn;

	@ApiModelProperty(value = "임직원 구매여부", required = false)
	private String purchaseEmployeeYn;

	@ApiModelProperty(value = "비회원 구매여부", required = false)
	private String purchaseNonmemberYn;

	@ApiModelProperty(value = "판매허용범위 (PC/Mobile)", required = false)
	private String[] goodsDisplayType;

	@ApiModelProperty(value = "WEB PC 전시여부", required = false)
	private String displayWebPcYn;

	@ApiModelProperty(value = "WEB MOBILE 전시여부", required = false)
	private String displayWebMobileYn;

	@ApiModelProperty(value = "APP 전시여부", required = false)
	private String displayAppYn;

	@ApiModelProperty(value = "전시 상태", required = true)
	private String displayYn;

	@ApiModelProperty(value = "판매 기간 시작일", required = false)
	private String saleStartDate;

	@ApiModelProperty(value = "판매 기간 종료일", required = false)
	private String saleEndDate;

	@ApiModelProperty(value = "판매 상태", required = false)
	private String saleStatus;

	@ApiModelProperty(value = "외부몰 판매 상태", required = false)
	private String goodsOutmallSaleStatus;
	/* 판매/전시 끝 */

	/* 가격정보 시작*/

	@ApiModelProperty(value = "과세구분", required = false)
	private String taxYn;

	@ApiModelProperty(value = "행사/할인 내역 > 우선할인 리스트", required = false)
	private List<GoodsDiscountRequestDto> goodsDiscountPriorityList;

	@ApiModelProperty(value = "행사/할인 내역 > 우선할인 승인 관리자", required = false)
	private List<GoodsRegistApprRequestDto> goodsDiscountPriorityApproList;

	@ApiModelProperty(value = "행사/할인 내역 > 즉시할인 리스트", required = false)
	private List<GoodsDiscountRequestDto> goodsDiscountImmediateList;

	@ApiModelProperty(value = "행사/할인 내역 > 즉시할인 승인 관리자", required = false)
	private List<GoodsRegistApprRequestDto> goodsDiscountImmediateApproList;

	@ApiModelProperty(value = "묶음상품 기본 할인가 > 리스트", required = false)
	private List<GoodsDiscountRequestDto> goodsPackagePriceList;

	@ApiModelProperty(value = "묶음상품 기본 할인가 > 즉시할인 승인 관리자", required = false)
	private List<GoodsRegistApprRequestDto> goodsPackagePriceApproList;

	@ApiModelProperty(value = "묶음상품 > 가격 정보 > 즉시 할인 > 개별품목 고정가 할인가격 리스트", required = false)
	private List<GoodsRegistPackageGoodsPriceDto> goodsDiscountImmediateCalcList;

	@ApiModelProperty(value = "묶음상품 > 가격 정보 > 우선 할인 > 개별품목 고정가 할인가격 리스트(현재 사용하지 않음)", required = false)
	private List<GoodsRegistPackageGoodsPriceDto> goodsDiscountPriorityCalcList;

	@ApiModelProperty(value = "묶음상품 > 가격 정보 > 묶음상품 기본 판매가 > 개별품목 고정가 할인가격 리스트", required = false)
	private List<GoodsRegistPackageGoodsPriceDto> goodsPackageCalcList;

	@ApiModelProperty(value = "단위별 용량정보", required = false)
	private String autoDisplaySizeYn;

	@ApiModelProperty(value = "단위별 용량정보 > 자동표기안함 시에 입력값", required = false)
	private String sizeEtc;

	@ApiModelProperty(value = "할인액", required = false)
	private int discountPrice;

	@ApiModelProperty(value = "정상가 총액", required = false)
	private int recommendedAllGoodsPrice;

	@ApiModelProperty(value = "할인구분", required = false)
	private String discountTypeCode;
	/* 가격정보 끝*/

	/* 임직원 할인 정보 시작 */
	@ApiModelProperty(value = "일반상품 > 임직원 할인 정보 > 임직원 개별할인 정보 리스트", required = false)
	private List<GoodsDiscountRequestDto> goodsDiscountEmployeeList;

	@ApiModelProperty(value = "일반상품 > 임직원 할인 정보 > > 임직원 개별할인 승인 관리자", required = false)
	private List<GoodsRegistApprRequestDto> goodsDiscountEmployeeApproList;

	@ApiModelProperty(value = "묶음상품 > 임직원 할인 정보 > 임직원 개별할인 정보 리스트", required = false)
	private List<GoodsDiscountRequestDto> goodsPackageDiscountEmployeeList;

	@ApiModelProperty(value = "묶음상품 > 임직원 할인 정보 > > 임직원 개별할인 승인 관리자", required = false)
	private List<GoodsRegistApprRequestDto> goodsPackageDiscountEmployeeApproList;
	/* 임직원 할인 정보 끝 */


	/* 판매정보 시작*/
	@ApiModelProperty(value = "판매유형", required = false)
	private String saleType;

	@ApiModelProperty(value = "녹즙_클렌즈 옵션사용 체크박스", required = false)
	private boolean greenJuiceCleanseOpt;

	@ApiModelProperty(value = "녹즙_클렌즈 옵션사용", required = false)
	private String greenJuiceCleanseOptYn;

	@ApiModelProperty(value = "매장판매", required = false)
	private String saleShopYn;

	@ApiModelProperty(value = "부등호", required = false)
	private String inequalitySign;

	@ApiModelProperty(value = "출고예정일,도착예정일 계산을 위한 기준날짜(예약주문가능기간종료일, 주문수집I/F일)", required = false)
	private String patternStandardDate;

	@ApiModelProperty(value = "출고예정일,도착예정일 계산을 위한 기준날짜(예약주문가능기간종료일, 주문수집I/F일) Array", required = false)
	private String[] patternStandardDateArray;

	@ApiModelProperty(value = "예약판매옵션설정 리스트")
	private List<GoodsRegistReservationOptionDto> goodsReservationOptionList;

	@ApiModelProperty(value = "삭제처리 할 예약판매옵션설정 리스트")
	private List<GoodsRegistReservationOptionDto> deleteGoodsReservationOptionList;

	//	일일 상품 관련항목 시작
	@ApiModelProperty(value = "일일상품 유형", required = false)
	private String goodsDailyType;

	@ApiModelProperty(value = "알러지 식단 포함여부")
	private String goodsDailyAllergyYn;

	@ApiModelProperty(value = "주7일 식단")
	private String[] goodsCycleType7Day;

	@ApiModelProperty(value = "주7일 식단 주차")
	private String[] goodsCycleTermType7Day;

	@ApiModelProperty(value = "주6일 식단")
	private String[] goodsCycleType6Day;

	@ApiModelProperty(value = "주6일 식단 주차")
	private String[] goodsCycleTermType6Day;

	@ApiModelProperty(value = "주5일 식단")
	private String[] goodsCycleType5Day;

	@ApiModelProperty(value = "주5일 식단 주차")
	private String[] goodsCycleTermType5Day;

	@ApiModelProperty(value = "주3일 식단")
	private String[] goodsCycleType3Day;

	@ApiModelProperty(value = "주3일 식단 주차")
	private String[] goodsCycleTermType3Day;

	@ApiModelProperty(value = "주3일 식단(잇슬림)")
	private String[] goodsCycleTypeEatslim3Day;

	@ApiModelProperty(value = "주3일 식단 주차(잇슬림)")
	private String[] goodsCycleTermTypeEatslim3Day;

	@ApiModelProperty(value = "일괄 배달 설정 허용 여부")
	private String goodsDailyBulkYn;

	@ApiModelProperty(value = "일괄 배달 설정 세트")
	private String[] goodsBulkType;

	@ApiModelProperty(value = "식단주기 List")
	private List<GoodsDailyCycleBulkVo> goodsDailyCycleList;

	@ApiModelProperty(value = "일괄배달 설정 List")
	private List<GoodsDailyCycleBulkVo> goodsDailyBulkList;

	@ApiModelProperty(value = "선물하기 허용 여부")
	private String presentYn;
	//	일일 상품 관련항목 끝
	/* 판매정보 끝*/

	/* 배송/발주 정보 시작 */
	@ApiModelProperty(value = "배송/발주 정보 > 선택한 배송정책 리스트", required = false)
	private List<GoodsRegistShippingTemplateDto> itemWarehouseShippingTemplateList;
	/* 배송/발주 정보 끝 */

	/* 혜택/구매 정보 시작*/
	@ApiModelProperty(value = "추가 상품 리스트")
	private List<GoodsRegistAdditionalGoodsDto> goodsAdditionalGoodsMappingList;

	@ApiModelProperty(value = "혜택 설정 > 쿠폰사용허용", required = false)
	private String couponUseYn;

	@ApiModelProperty(value = "최소구매 수량기준", required = false)
	private String limitMinimumCnt;

	@ApiModelProperty(value = "최대 구매 기준", required = false)
	private String limitMaximumType;

	@ApiModelProperty(value = "최대 구매 기간", required = false)
	private String limitMaximumDuration;

	@ApiModelProperty(value = "최대 구매 수량", required = false)
	private String limitMaximumCnt;
	/* 혜택/구매 정보 끝*/

	/* 추천상품 등록 시작 */
	@ApiModelProperty(value = "추천 상품 리스트")
	private List<GoodsRegistAdditionalGoodsDto> goodsRecommendList;
	/* 추천상품 등록 끝 */

	/* 상품 이미지 시작 */
	@ApiModelProperty(value = "(묶음상품)상품이미지형식", required = false)
	private String goodsPackageImageType;

	@ApiModelProperty(value = "묶음 상품 전용 이미지 정렬 순서", required = false)
	private List<String> packageImageOrderList;

	@ApiModelProperty(value = "묶음 상품 전용 이미지 목록", required = false)
	List<UploadFileDto> packageImageUploadResultList;

	@ApiModelProperty(value = "(묶음상품)동영상 URL", required = false)
	private String goodsPackageVideoUrl;

	@ApiModelProperty(value = "(묶음상품)비디오 자동재생 유무", required = false)
	private String goodsPackageVideoAutoplayYn;

	@ApiModelProperty(value = "상품 이미지를 저장할 최상위 저장 디렉토리 경로", required = false)
	private String imageRootStoragePath;

	@ApiModelProperty(value = "상품 이미지 첫번째 파일명(대표이미지명)", required = false)
	private String representativeImageName;

	@ApiModelProperty(value= "상품 이미지 삭제할 파일명", required = false)
	private List<String> packageImageNameListToDelete;

	@ApiModelProperty(value= "묶음 상품 전용 이미지 순서 변경 여부", required = false)
	private boolean imageSortOrderChanged;

	@ApiModelProperty(value = "상품별 대표이미지 정렬 순서", required = false)
	private List<String> goodsImageOrderList;

	@ApiModelProperty(value= "상품별 대표 이미지 순서 변경 여부", required = false)
	private boolean goodsImageSortOrderChanged;


	/* 상품 이미지 끝 */

	/* 상품 상세 기본 정보 시작 */
	@ApiModelProperty(value = "(묶음상품)상품상세 기본정보 직접등록 여부", required = false)
	private String goodsPackageBasicDescYn;

	@ApiModelProperty(value = "(묶음상품)상품상세 기본정보", required = false)
	private String goodsPackageBasicDesc;
	/* 상품 상세 기본 정보 끝 */

	/* 상품공지 시작*/
	@ApiModelProperty(value = "상세 하단공지1 이미지 파일 업로드 정보", required = false)
	List<UploadFileDto> noticeBelow1ImageUploadResultList;

	@ApiModelProperty(value = "상세 하단공지1 이미지 URL", required = false)
	private String noticeBelow1ImageUrl;

	@ApiModelProperty(value = "상세 하단공지1 시작일", required = false)
	private String noticeBelow1StartDate;

	@ApiModelProperty(value = "상세 하단공지1 종료일", required = false)
	private String noticeBelow1EndDate;

	@ApiModelProperty(value = "상세 하단공지2 이미지 파일 업로드 정보", required = false)
	List<UploadFileDto> noticeBelow2ImageUploadResultList;

	@ApiModelProperty(value = "상세 하단공지2 이미지 URL", required = false)
	private String noticeBelow2ImageUrl;

	@ApiModelProperty(value = "상세 하단공지2 시작일", required = false)
	private String noticeBelow2StartDate;

	@ApiModelProperty(value = "상세 하단공지2 종료일", required = false)
	private String noticeBelow2EndDate;
	/* 상품공지 끝*/

	/* 기타정보 시작*/
	@ApiModelProperty(value = "상품 메모", required = false)
	private String goodsMemo;
	/* 기타정보 끝*/

	/* 승인관리자 정보 시작 */
	@ApiModelProperty(value = "승인 관리자 정보", required = false)
	private List<GoodsRegistApprRequestDto> goodsApprList;

	@ApiModelProperty(value = "승인 종류", required = false)
	private String apprKindTp;

	@ApiModelProperty(value = "페이지 Load 시간", required = false)
	private String loadDateTime;
	/* 승인관리자 정보 끝 */

	@ApiModelProperty(value = "등록자", required = false)
	private String createId;

	@ApiModelProperty(value = "수정자", required = false)
	private String modifyId;

	@ApiModelProperty(value = "프로시져 Debug Flag", required = false)
	private boolean inDebugFlag;

	/* 권한 관련 시작 */
    @ApiModelProperty(value = "접근권한 공급업체 ID 리스트")
    private List<String> listAuthSupplierId;

	@ApiModelProperty(value = "출고처 권한 IDS")
	private List<String> listAuthWarehouseId;
	/* 권한 관련 시작 */

	@ApiModelProperty(value = "거래처 품목 승인 상태", required = false)
	private String itemClientApprStat;

	@ApiModelProperty(value = "거래처 상품 승인 상태", required = false)
	private String goodsClientApprStat;

	/* 풀무원샵 상품코드 시작 */
	@ApiModelProperty(value = "풀무원샵 상품코드")
	private List<GoodsCodeVo> goodsCodeList;
	/* 풀무원샵 상품코드 종료 */

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
