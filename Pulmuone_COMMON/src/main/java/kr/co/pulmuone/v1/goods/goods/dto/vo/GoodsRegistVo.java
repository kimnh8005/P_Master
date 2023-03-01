package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(description = "상품등록/수정 Vo")
public class GoodsRegistVo {

	//품목마스터관련
	@ApiModelProperty(value="품목등록일자")
	String createDate;

	@ApiModelProperty(value="등록자")
	@UserMaskingUserName
	String userName;

	@ApiModelProperty(value="등록자아이디")
	String itemCreateLoginId;

	@ApiModelProperty(value="최근수정일")
	String modifyDate;

	@ApiModelProperty(value="최근수정자아이디")
	String modifyUserId;

	@ApiModelProperty(value="최근수정자명")
	@UserMaskingUserName
	String modifyUserName;

	@ApiModelProperty(value="마스터품목유형")
	String itemTpName;

	@ApiModelProperty(value="ERP품목연동여부")
	String erpIfYn;

	@ApiModelProperty(value="ERP품목명")
	String erpItemName;

	@ApiModelProperty(value="표준 카테고리 ID")
	String ilCategoryStadardId;

	@ApiModelProperty(value="표준 카테고리 명")
	String categoryStandardName;

	@ApiModelProperty(value="ERP카테고리명")
	String erpCategoryName;

	@ApiModelProperty(value="ERP대분류")
	String erpCategoryLevel1Id;

	@ApiModelProperty(value="마스터품목코드")
	String ilItemCode;

	@ApiModelProperty(value="마스터품목바코드")
	String itemBarcode;

	@ApiModelProperty(value="상품등록일자")
	String itemCreateDate;

	@ApiModelProperty(value="마스터품목명")
	String masterItemName;

	@ApiModelProperty(value="포장단위별용량")
	Double sizePerPackage;

	@ApiModelProperty(value="용량단위")
	String sizeUnit;

	@ApiModelProperty(value="용량단위명")
	String sizeUnitName;

	@ApiModelProperty(value="포장구성수량")
	int qtyPerPackage;

	@ApiModelProperty(value="포장구성단위")
	String packageUnit;

	@ApiModelProperty(value="포장구성단위명")
	String packageUnitName;

	@ApiModelProperty(value="PDM그룹코드")
	String pdmGroupCode;

	@ApiModelProperty(value="공급업체")
	String supplierName;

	@ApiModelProperty(value="표준 브랜드")
	String brandName;

	@ApiModelProperty(value="전시 브랜드")
	String dpBrandName;

	@ApiModelProperty(value="상품군")
	String itemGroup;

	@ApiModelProperty(value="보관방법 CODE")
	String storageMethodType;

	@ApiModelProperty(value="보관방법")
	String storageMethodName;

	@ApiModelProperty(value="원산지(해외/국내/기타)")//추후ENUM으로변경처리해야함
	String originType;

	@ApiModelProperty(value="원산지상세(해외:국가코드,기타:입력)")
	String originDetail;

	@ApiModelProperty(value="유통기간")
	int distributionPeriod;

	@ApiModelProperty(value="박스체적(가로)")
	String boxWidth;

	@ApiModelProperty(value="박스체적(세로)")
	String boxDepth;

	@ApiModelProperty(value="박스체적(높이)")
	String boxHeight;

	@ApiModelProperty(value="박스입수량")
	int pcsPerBox;

	@ApiModelProperty(value="uom")
	String uom;

	@ApiModelProperty(value="상품상세기본정보")
	String basicDescription;

	@ApiModelProperty(value="상품상세주요정보")
	String detailDescription;

	@ApiModelProperty(value="마스터품목메모")
	String etcDescription;

	@ApiModelProperty(value="공급업체ID")
	String urSupplierId;

	@ApiModelProperty(value="브랜드 ID")
	String urBrandId;

	@ApiModelProperty(value="발주가능여부(올가ERP)")
	String erpCanPoYn;

	@ApiModelProperty(value="배송불가지역코드")
	String undeliverableAreaType;

	@ApiModelProperty(value="배송불가지역명")
	String undeliverableAreaTypeName;

	@ApiModelProperty(value="매장전용품목유형")
	String erpO2oExposureType;

	@ApiModelProperty(value="건강생활상품유형")
	String erpProductType;

	@ApiModelProperty(value="과세구분")
	String taxYn;

	@ApiModelProperty(value="과세구분명")
	String taxYnName;

	@ApiModelProperty(value="매장(가맹점)여부")
	String storeYn;

	@ApiModelProperty(value="ERP 상품군")
	String erpItemGroup;

	@ApiModelProperty(value="ERP 보관정보")
	String erpStorageMethod;

	@ApiModelProperty(value="ERP 원산지")
	String erpOriginName;

	@ApiModelProperty(value="ERP 유통기간")
	String erpDistributionPeriod;

	@ApiModelProperty(value="ERP 박스 가로")
	String erpBoxWidth;

	@ApiModelProperty(value="ERP 박스 세로")
	String erpBoxDepth;

	@ApiModelProperty(value="ERP 박스 높이")
	String erpBoxHeight;

	@ApiModelProperty(value="ERP 박스입수량")
	String erpPcsPerBox;

	@ApiModelProperty(value="단품표시중량")
	String itemDispWeight;

	@ApiModelProperty(value="단품실중량")
	String itemRealWeight;

	@ApiModelProperty(value="발주유형 ERP")
	String erpPoType;

	@ApiModelProperty(value="ERP 공급업체")
	String erpSupplierName;

	@ApiModelProperty(value="발주유형명")
	String poTypeName;

	@ApiModelProperty(value="품목별 상이 여부(Y:품목별 상이)")
	String poPerItemYn;

	@ApiModelProperty(value="몰인몰 카테고리 분류값")
	String mallinmallCategoryId;

	@ApiModelProperty(value="묶음상품 기준상품ID")
	String baseGoodsId;

	@ApiModelProperty(value="공급처 CODE")
	String supplierCode;

	@ApiModelProperty(value="단종여부")
	String extinctionYn;

	@ApiModelProperty(value="렌탈료/월")
	Integer rentalFeePerMonth;

	@ApiModelProperty(value="렌탈 의무사용기간(월)")
	Integer rentalDueMonth;

	@ApiModelProperty(value="렌탈 계약금")
	Integer rentalDeposit;

	@ApiModelProperty(value="렌탈 설치비")
	Integer rentalInstallFee;

	@ApiModelProperty(value="렌탈 등록비")
	Integer rentalRegistFee;

	//상품정보제공고시관련
	@ApiModelProperty(value="상품정보제공고시마스터ID")
	String ilSpecMasterId;

	@ApiModelProperty(value="상품정보제공고시마스터명")
	String specMasterName;

	@ApiModelProperty(value="상품정보제공고시항목ID")
	String ilSpecFieldId;

	@ApiModelProperty(value="상품정보제공고시 CODE")
	String specFieldCd;

	@ApiModelProperty(value="상품정보제공고시항목명")
	String specFieldName;

	@ApiModelProperty(value="상품정보제공고시항목설명")
	String specFieldValue;

	@ApiModelProperty(value="유통기한에 따른 출고일")
	int delivery;

	@ApiModelProperty(value="직접입력 여부")
	String directYn;

	//상품영양정보관련
	@ApiModelProperty(value="영양정보표시여부")
	String nutritionDisplayYn;

	@ApiModelProperty(value="영양정보기본정보(nutritionDisplayYn값이N일때노출되는항목)")
	String nutritionDisplayDefault;

	@ApiModelProperty(value = "영양성분량")
	String nutritionQuantity;

	@ApiModelProperty(value = "영양성분코드")
	String nutritionCode;

	@ApiModelProperty(value="영양분석단위(1회분량)")
	String nutritionQuantityPerOnce;

	@ApiModelProperty(value="영양분석단위(총분량)")
	String nutritionQuantityTotal;

	@ApiModelProperty(value="영양성분기타")
	String nutritionEtc;

	@ApiModelProperty(value="품목영양정보마스터ID")
	String ilItemNutritionId;

	@ApiModelProperty(value="영양성분명")
	String nutritionName;

	@ApiModelProperty(value="ERP영양성분")
	String erpNutritionQty;

	@ApiModelProperty(value="ERP영양성분기준치")
	Double erpNutritionPercent;

	@ApiModelProperty(value="영양성분")
	Double nutritionQty;

	@ApiModelProperty(value="영양성분기준치")
	Double nutritionPercent;

	@ApiModelProperty(value="영양성분단위")
	String nutritionUnit;

	@ApiModelProperty(value = "영양소 기준치 사용여부(Y:사용)")
	String nutritionPercentYn;

	@ApiModelProperty(value = "그룹순번")
	String groupSequence;

	@ApiModelProperty(value = "그룹내 최대 레코드번호")
	private String maxNumber;

	//상품인증정보관련
	@ApiModelProperty(value="상품인증정보ID")
	String ilItemCertificationId;

	@ApiModelProperty(value="인증정보ID")
	String ilCertificationId;

	@ApiModelProperty(value="인증정보이미지경로")
	String imagePath;

	@ApiModelProperty(value="인증정보이미지파일명")
	String imageName;

	@ApiModelProperty(value="인증정보이미지원본파일명")
	String imageOriginalName;

	@ApiModelProperty(value="인증정보명")
	String certificationName;

	@ApiModelProperty(value="기본인증정보설명")
	String defaultCertificationDesc;

	@ApiModelProperty(value="상품인증정보설명")
	String certificationDesc;

	//상품이미지
	@ApiModelProperty(value="상품이미지아이디")
	String ilGoodsImageId;

	@ApiModelProperty(value="상품인증정보설명")
	String itemImageName;

	@ApiModelProperty(value="동영상정보")
	String videoUrl;

	@ApiModelProperty(value="비디오 자동재생 유무")
	String videoAutoplayYn;

	//상품마스터
	@ApiModelProperty(value="상품ID")
	String ilGoodsId;

	@ApiModelProperty(value="출고처ID")
	String urWarehouseId;

	@ApiModelProperty(value="출고처그룹ID")
	String warehouseGrpCd;

	@ApiModelProperty(value="상품유형")
	String goodsType;

	@ApiModelProperty(value="상품유형명")
	String goodsTypeName;

	@ApiModelProperty(value="판매유형")
	String saleType;

	@ApiModelProperty(value="선물하기 허용")
	String presentYn;

	@ApiModelProperty(value="녹즙_클렌즈 옵션사용 체크박스")
	boolean greenJuiceCleanseOpt;

	@ApiModelProperty(value="녹즙_클렌즈 옵션사용")
	String greenJuiceCleanseOptYn;

	@ApiModelProperty(value="상품명")
	String goodsName;

	@ApiModelProperty(value="표장용량구성정보노출여부(Y:노출)")
	String packageUnitDisplayYn;

	@ApiModelProperty(value="용량정보 직접입력(묶음상품에서 사용)")
	String packageUnitDesc;

	@ApiModelProperty(value="프로모션명")
	String promotionName;

	@ApiModelProperty(value="프로모션시작일")
	String promotionNameStartDate;

	@ApiModelProperty(value="프로모션종료일")
	String promotionNameEndDate;

	@ApiModelProperty(value="상품설명")
	String goodsDesc;

	@ApiModelProperty(value="검색키워드")
	String searchKeyword;

	@ApiModelProperty(value="상품구매유형공통코드")
	String[] purchaseTargetType;

	@ApiModelProperty(value="회원구매여부(Y:회원구매가능)")
	String purchaseMemberYn;

	@ApiModelProperty(value="임직원구매여부(Y:구매가능)")
	String purchaseEmployeeYn;

	@ApiModelProperty(value="비회원구매여부(Y:비회원구매가능)")
	String purchaseNonmemberYn;

	@ApiModelProperty(value="판매허용범위 공통코드")
	String[] goodsDisplayType;

	@ApiModelProperty(value="WEB PC전시여부(Y:전시)")
	String displayWebPcYn;

	@ApiModelProperty(value="WEB MOBILE전시여부(Y:전시)")
	String displayWebMobileYn;

	@ApiModelProperty(value="APP 전시여부(Y:전시)")
	String displayAppYn;

	@ApiModelProperty(value="판매시작일")
	String saleStartDate;

	@ApiModelProperty(value="판매시작일")
	String saleEndDate;

	@ApiModelProperty(value="전시여부(Y:전시)")
	String displayYn;

	@ApiModelProperty(value="판매상태공통코드(SALE_STATUS)")
	String saleStatus;

	@ApiModelProperty(value="상품 외부몰 판매 상태 공통코드(GOODS_OUTMALL_SALE_STAT)")
	String goodsOutmallSaleStatus;

	@ApiModelProperty(value="단위별용량정보자동표기(Y:자동표기)")
	String autoDisplaySizeYn;

	@ApiModelProperty(value="단위별용량정보(자동표기안함일때사용)")
	String sizeEtc;

	@ApiModelProperty(value="매장판매여부(Y:매장판매)")
	String saleShopYn;

	@ApiModelProperty(value="개별쿠폰적용여부(Y:적용)")
	String couponUseYn;

	@ApiModelProperty(value="최소구매수량")
	String limitMinimumCnt;

	@ApiModelProperty(value="최대구매수량유형공통코드(PURCHASE_LIMIT_MAX_TP)")
	String limitMaximumType;

	@ApiModelProperty(value="최대구매수량산정기간")
	String limitMaximumDuration;

	@ApiModelProperty(value="최대구매수량")
	String limitMaximumCnt;

	@ApiModelProperty(value="MD추천(Y:추천)")
	String mdRecommendYn;

	@ApiModelProperty(value="상품메모")
	String goodsMemo;

	@ApiModelProperty(value="상세하단공지1이미지URL")
	String noticeBelow1ImageUrl;

	@ApiModelProperty(value="상세하단공지1시작일")
	String noticeBelow1StartDate;

	@ApiModelProperty(value="상세하단공지2시작일")
	String noticeBelow2StartDate;

	@ApiModelProperty(value="상세하단공지2이미지URL")
	String noticeBelow2ImageUrl;

	@ApiModelProperty(value="상세하단공지1종료일")
	String noticeBelow1EndDate;

	@ApiModelProperty(value="상세하단공지2종료일")
	String noticeBelow2EndDate;

	@ApiModelProperty(value = "정상가")
	private int recommendedPrice;

	@ApiModelProperty(value = "판매가")
	private int salePrice;

	@ApiModelProperty(value = "원가")
	private int standardPrice;

	@ApiModelProperty(value = "구성수량")
	private int purchaseQuanity;

	@ApiModelProperty(value = "정상가총액")
	private int recommendedTotalPrice;

	@ApiModelProperty(value = "구성수량", required = false)
	private int goodsQuantity;

	@ApiModelProperty(value = "원가 총액", required = false)
	private int standardTotalPrice;

	@ApiModelProperty(value = "75x75크기 이미지")
	private String size75Image;

	@ApiModelProperty(value = "180x180크기 이미지")
	private String size180Image;

	@ApiModelProperty(value = "(묶음상품)상품이미지형식", required = false)
	private String goodsPackageImageType;

	@ApiModelProperty(value = "(묶음상품)상품상세 기본정보 직접등록 여부(Y:직접등록)", required = false)
	private String goodsPackageBasicDescYn;

	@ApiModelProperty(value = "(묶음상품)상품상세 기본정보", required = false)
	private String goodsPackageBasicDesc;

	@ApiModelProperty(value = "(묶음상품)동영상 URL", required = false)
	private String goodsPackageVideoUrl;

	@ApiModelProperty(value = "(묶음상품)비디오 자동재생 유무", required = false)
	private String goodsPackageVideoAutoplayYn;

	@ApiModelProperty(value = "일일상품 유형", required = false)
	private String goodsDailyType;

	@ApiModelProperty(value = "상품 승인 ID", required = false)
	private String ilGoodsApprId;

	@ApiModelProperty(value = "상품 승인상태", required = false)
	private String apprStat;

	@ApiModelProperty(value = "상품 승인상태명", required = false)
	private String apprStatName;

	@ApiModelProperty(value = "상태 변경 메세지(반려 사유)", required = false)
	private String statusCmnt;

	@ApiModelProperty(value = "상품등록 승인 ID", required = false)
	private String goodsRegistGoodsApprId;

	@ApiModelProperty(value = "상품등록 승인 요청자 ID", required = false)
	private String goodsRegistApprReqUserId;

	@ApiModelProperty(value = "상품등록 승인상태", required = false)
	private String goodsRegistApprStat;

	@ApiModelProperty(value = "상품등록 승인상태명", required = false)
	private String goodsRegistApprStatName;

	@ApiModelProperty(value = "상품등록 승인상태 변경 코멘트", required = false)
	private String goodsRegistApprStatusCmnt;

	@ApiModelProperty(value = "거래처 상품수정 승인 ID", required = false)
	private String goodsClientGoodsApprId;

	@ApiModelProperty(value = "거래처 상품수정 승인 요청자 ID", required = false)
	private String goodsClientApprReqUserId;

	@ApiModelProperty(value = "거래처 상품수정 승인상태", required = false)
	private String goodsClientApprStat;

	@ApiModelProperty(value = "거래처 상품수정 승인상태명", required = false)
	private String goodsClientApprStatName;

	@ApiModelProperty(value = "거래처 상품수정 승인상태 변경 코멘트", required = false)
	private String goodsClientApprStatusCmnt;

	@ApiModelProperty(value = "거래처 품목수정 승인 ID", required = false)
	private String itemClientItemApprId;

	@ApiModelProperty(value = "거래처 품목수정 승인 요청자 ID", required = false)
	private String itemClientApprReqUserId;

	@ApiModelProperty(value = "거래처 품목수정 승인상태", required = false)
	private String itemClientApprStat;

	@ApiModelProperty(value = "거래처 품목수정 승인상태명", required = false)
	private String itemClientApprStatName;

	@ApiModelProperty(value = "거래처 품목수정 승인상태 변경 코멘트", required = false)
	private String itemClientApprStatusCmnt;

	@ApiModelProperty(value = "(증정품)증정 형태", required = false)
	private String giftGoodsOwnerType;

	@ApiModelProperty(value = "(일일상품)알러지 식단 포함여부", required = false)
	private String goodsDailyAllergyYn;

	@ApiModelProperty(value = "(일일상품)일괄배달 허용여부", required = false)
	private String goodsDailyBulkYn;

	@ApiModelProperty(value="등록자ID")
	private String createId;

	@ApiModelProperty(value="등록자 USER ID")
	private String createUserId;

	@ApiModelProperty(value="등록자 이름")
	private String createUserName;

	@ApiModelProperty(value="상품 등록일")
	private String goodsCreateDate;

	@ApiModelProperty(value="수정자ID")
	private String modifyId;

	@ApiModelProperty(value="기본정보 > 최근 수정일")
	private String confirmStatus;

	@ApiModelProperty(value="공급업체 ID")
	private String urCompanyId;

	@ApiModelProperty(value = "거래처 상품수정 전시상태")
	private boolean goodsClientDispYn;

	@ApiModelProperty(value = "거래처 상품수정 판매 시작기간")
	private boolean godosClientSaleStartDt;

	@ApiModelProperty(value = "거래처 상품수정 판매 종료기간")
	private boolean goodsClientSaleEndDt;

	@ApiModelProperty(value = "거래처 상품수정 판매상태")
	private boolean goodsClientSaleStatus;

}
