/**-----------------------------------------------------------------------------
 * description 		 : 묶음상품 등록
 * @
 * @ 수정일			수정자		  수정내용
 * @ ------------------------------------------------------
 * @ 2020.08.06		박영후		  최초생성
 * @
 * **/
'use strict';

var viewModel;												//viewModel 전역변수
var firstIlGoodsDetailData={};
var pageParam = fnGetPageParam();							//GET Parameter 받기
var ilGoodsId = pageParam.ilGoodsId;						//상품 ID
var ilItemCode;
var urSupplierId, supplierCode, urWarehouseId, undeliverableAreaType;
var ilItemWarehouseId, stockOrderYn;										//추가상품 재고 상세보기 호출 파라미터 관련
var CUR_SERVER_URL = fnGetServerUrl().mallUrl;										// 상품상세 미리보기 위한 URL

var allowedImageExtensionList = ['.jpg', '.jpeg', '.gif'];	// 업로드 가능한 이미지 확장자 목록
var promotionNameMaxByteLength = 12;						// 프로모션 상품명 최대 입력 가능 길이 ( 단위 : byte )
var searchKeywordMaxCount = 20;								// 검색 키워드 최대 등록 가능 건수
var oldSearchKeywordValue;									// 검색 키워드 이전 입력값

var	aGridOpt, aGrid, aGridDs,	// 묶음상품선택 그리드
	bGridOpt, bGrid, bGridDs,	// 증정품선택 그리드
	cGridOpt, cGrid, cGridDs,	// 묶음상품 판매가 입력 그리드
	dGridOpt, dGrid, dGridDs,	// 묶음상품 구성목록 그리드
	eGridOpt, eGrid, eGridDs	// 증정품 구성목록 그리드

var	goodsPackageGoodsMappingGridOpt, goodsPackageGoodsMappingGrid, goodsPackageGoodsMappingGridDs	//묶음상품 구성 정보 그리드

/* 카테고리 관련 변수 */
var ctgryGrid1, ctgryGrid2, ctgryGrid3, ctgryGrid4;
var ctgryGridDs1, ctgryGridDs2, ctgryGridDs3, ctgryGridDs4;
var ctgryGridOpt, ctgryGrid, ctgryGridDs;

//몰인몰 카테고리 관련 변수
var mallInMallCtgryGrid1, mallInMallCtgryGrid2, mallInMallCtgryGrid3, mallInMallCtgryGrid4;
var mallInMallCtgryGridDs1, mallInMallCtgryGridDs2, mallInMallCtgryGridDs3, mallInMallCtgryGridDs4;
var mallInMallCtgryGridOpt, mallInMallCtgryGrid, mallInMallCtgryGridDs;

var publicStorageUrl;	// Public 저장소 Root Url
var workindEditorId;	// 상품 상세 기본 정보와 주요 정보 Editor 중 이미지 첨부를 클릭한 에디터 Id

var packageImageUploadMaxLimit = 1024000;				// 상품 이미지 첨부 가능 최대 용량 ( 단위 : byte )
var noticeBelowImageUploadMaxLimit = 512000;			//첨부파일 최대 용량

var ilGoodsDisplayCategoryList = null;
var ilGoodsMallinmallCategoryList = null;
var baseRowCount = 0;									//묶음상품 구성 갯수
var gblAlertMessage = "";

var noticeBelow1ImageDelete = false;														//상세 상단공지 이미지 X(삭제)버튼
var noticeBelow2ImageDelete = false;														//상세 하단공지 이미지 X(삭제)버튼


$(document).ready(function() {
	//오늘 날짜
	const FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm";

	var todayDate = new Date();
	todayDate.setHours(0,0,0,0);
	var todayDateTime = todayDate.oFormat(FULL_DATE_FORMAT);

	var lastDate = new Date(2999,11,31,23,59,0);
	var lastDateTime = lastDate.oFormat(FULL_DATE_FORMAT);

	var defaultStartTime =  fnGetToday(FULL_DATE_FORMAT);
	var defaultEndTime = "2999-12-31 23:59";
	var noticeEndTime =  fnGetDayAdd(defaultStartTime, 1, FULL_DATE_FORMAT);

	var cacheData = {};

	var totalsalePricePerUnit = 0;		// 묶음상품 판매가(개당)의 총 합
	var totalSalePriceGoods = 0;		// 묶음상품 판매가총액의 합
	var totalDiscountPricePerGoods = 0;	//묶음상품 총 할인액(수량 포함)

	var saleRateSumAvg = 0;			// 묶음상품 구성목록 합계 할인율

	var amount = [];  // 수량 데이터 수정
	for (var i = 1; i <= 200; i++)		// 99 -> 200 수량 변경
		amount.push(i);

	// #################### viewModel start ####################
	viewModel = new kendo.data.ObservableObject({

		pageMode							: 'create',														//등록/수정 구분
		baseGoodsPopupButtonVisible			: true,															//묶음상품 기본설정 > 기준 상품 선택 > 선택 button Visible
		goodsPackageBaseCreateVisible		: true,															//등록 pageMode 시 묶음상품 기본설정 Visible
		goodsPackageBaseModifyVisible		: false,														//수정 pageMode 시 묶음상품 기본설정 Visible
		isGoodsDisplayVisible				: false,														//기본정보 > 전시 카테고리 Visible
		isMallInMallVisible					: false,														//기본정보 > 몰인몰 카테고리 Visible
		discountBtnVisible					: false,														//모든 할인 > 할인설정 버튼 Visaible

		// 기본 정보
		ilGoodsDetail				: {
			/* 묶음상품 기본설정 Start */
			baseGoodsId							: '',														// 기준 상품 코드
			ilItemCode							: '',														// 품목 코드
			urWarehouseId						: '',														// 출고처 ID
			warehouseGrpCd						: '',														// 출고처 그룹 ID
			supplierId							: '',														// 공급처ID
			warehouseId							: '',														// 출고처 ID
			mallinmallCategoryId				: '',														// 브랜드가 베이비밀, 잇슬림에 해당하는 카테고리 ID
			itemBarcode							: '',														// 품목 바코드
			goodsCreateDate						: '',														// 상품등록일자
			confirmStatus						: '',														// 상품수정일자, 수정자
			mdRecommendYn						: 'N',														// MD추천 노출여부 DropwonList
			goodsType							: 'GOODS_TYPE.PACKAGE',										// 상품 유형
			totalSalePriceGoods					: 0,
			/* 묶음상품 기본설정 End */

			/* 기본 정보 Start */
			visibleUpdateHistory				: false,													//최근 수정일 > 업데이트 내역 버튼 Visible
			visibleDownPopup					: false,													// 상세이미지 다운로드 Visible
			goodsName							: '',														// 상품명
			packageUnitDisplayYn				: 'N',														// 상품명 > 포장용량 구성정보 > 노출 여부 > 기본설정값
			packageUnitDesc						: '',														// 용량정보 입력 > 노출함 > 텍스트 value
			isPackageUnitDisplay				: false,													// 용량정보 입력 > 노출함 > 텍스트 Box Visible

			promotionName						: '',														// 프로모션 상품명
			promotionNameStartDate				: todayDate,												// 기본정보 > 프로모션 시작 기간, type : dateObj
			promotionNameEndDate				: lastDate,
			promotionNameStartYear				: '',														// 기본정보 > 프로모션 시작일
			promotionNameStartHour				: todayDate.oFormat("HH"),									// 기본정보 > 프로모션 시작 시간
			promotionNameStartMinute			: todayDate.oFormat("mm"),									// 기본정보 > 프로모션 시작 분
			promotionNameEndYear				: '',														// 기본정보 > 프로모션 종료일
			promotionNameEndHour				: lastDate.oFormat("HH"),									// 기본정보 > 프로모션 종료 시간
			promotionNameEndMinute				: lastDate.oFormat("mm"),									// 기본정보 > 프로모션 종료 분

			goodsDesc							: '',														// 상품설명
			searchKeyword						: '',														// 키워드 입력
			categoryStandardName				: '',														// 기준 상품 표준 카테고리

			displayCategoryDiv					: 'MALL_DIV.PULMUONE',										// 전시 카테고리
			displayCategoryList					: [],														// 전시 카테고리 리스트
			mallInMallCategoryList				: [],														// 몰인몰 카테고리 리스트
			/* 기본 정보 End */

			/* 판매/전시 Start */
			purchaseTargetType					: ['ALL'													//구매 허용 범위 > 기본설정값
				,'PURCHASE_TARGET_TP.MEMBER'
				,'PURCHASE_TARGET_TP.EMPLOYEE'
				,'PURCHASE_TARGET_TP.NONMEMBER'],
			goodsDisplayType					: ['ALL'													//판매허용범위 (PC/Mobile) > 기본설정값
				,'GOODS_DISPLAY_TYPE.APP'
				,'GOODS_DISPLAY_TYPE.WEB_MOBILE'
				,'GOODS_DISPLAY_TYPE.WEB_PC'],
			displayYn							: 'Y',														//전시 상태 > 전시(기본설정)

			saleStartDate						: todayDate,												// 판매 기간 > 시작 기간
			saleEndDate							: lastDate,													// 판매 기간 >  종료 기간
			saleStartYear						: todayDate.oFormat("yyyy-MM-dd"),							// 판매 기간 >  시작일
			saleStartHour						: todayDate.oFormat("HH"),									// 판매 기간 >  시작 시간
			saleStartMinute						: todayDate.oFormat("mm"),									// 판매 기간 >  시작 분
			saleEndYear							: lastDate.oFormat("yyyy-MM-dd"),							// 판매 기간 >  종료일
			saleEndHour							: lastDate.oFormat("HH"),									// 판매 기간 >  종료 시간
			saleEndMinute						: lastDate.oFormat("mm"),									// 판매 기간 >  종료 분

			saleStatus							: 'SALE_STATUS.SAVE',				// 판매 상태 > 저장(기본설정)
			extinctionYn						: 'N',								//품목의 판매 허용여부 상태 값(단종 여부 확인)
			isSaleStatus						: true,								// 판매 상태 등록시에는 변경 불가
			stockOperationForm					: '',								//재고운영형태
			stockQuantity						: 0,								//전일마감재고
			goodsStockInfo						: [],								//재고관련 정보

			goodsOutmallSaleStatus				: 'GOODS_OUTMALL_SALE_STAT.NONE',							// 외부몰 판매 상태
			/* 판매/전시 End */

			/* 가격 정보 Start */
			taxYn									: '',							// 과세구분(기준상품 품목 정보에서 호출)
			goodsPrice								: [],							// 판매 가격정보
			visibleGoodsPackagePriceInfoNodata		: true,							//가격정보 > 판매 가격정보 > NoData
			visibleGoodsPackagePriceInfo			: false,						//가격정보 > 판매 가격정보 > 리스트

			isGoodsDiscountPriorityListNoDataTbody	: true,							// 행사/할인 내역 > 우선할인 > Nodata 항목
			isGoodsDiscountPriorityListTbody		: false,						// 행사/할인 내역 > 우선할인 > Data 항목
			visibleGoodsDiscountPriorityButton		: false,						// 행사/할인 내역 > 우선할인 > 우선할인 설정 버튼

			isGoodsDiscountImmediateListNoDataTbody	: true,							// 행사/할인 내역 > 즉시할인 > Nodata 항목
			isGoodsDiscountImmediateListTbody		: false,						// 행사/할인 내역 > 즉시할인 > Data 항목
			visibleGoodsDiscountImmediateButton		: false,						// 행사/할인 내역 > 즉시할인 > 즉시할인 설정 버튼

			visibleGoodsPackagePriceListNoDataTbody	: true,							// 묶음상품 기본 판매가 > Nodata 항목
			visibleGoodsPackagePriceListTbody		: false,						// 묶음상품 기본 판매가 > Data 항목
			visibleGoodsPackagePriceButton			: false,						// 묶음상품 기본 판매가 > 기본할인 설정 버튼

			goodsDiscountPriorityList				: [],							// 행사/할인 내역 > 우선할인
			goodsDiscountPriorityApproList			: [],							// 행사/할인 내역 > 우선할인 > 승인 관리자 정보
			goodsDiscountPriorityCalcList			: [],							// 행사/할인 내역 > 우선할인 > 가격계산 정보
			goodsDiscountImmediateList				: [],							// 행사/할인 내역 > 즉시할인
			goodsDiscountImmediateApproList		    : [],							// 행사/할인 내역 > 즉시할인 > 승인 관리자 정보
			goodsDiscountImmediateCalcList			: [],							// 행사/할인 내역 > 즉시할인 > 가격계산 정보
			goodsPackagePriceList					: [],							// 묶음상품 기본 판매가 > List
			goodsPackagePriceApproList				: [],							// 묶음상품 기본 판매가 > 승인 관리자 정보
			goodsPackageCalcList					: [],							// 묶음상품 기본 판매가, 행사/할인내역 > 가격계산 Grid 리스트
			/* 가격 정보 End */

			/* 임직원 할인 정보 Start */
			goodsPackageEmployeePrice				: [],							// 임직원 할인 가격정보
			goodsPackageEmployeePriceListNoDataVisible : true,						// 임직원 할인 가격정보 Nodata Tbody Visible
			goodsPackageEmployeePriceListDataVisible	: false,					// 임직원 할인 가격정보 Data Tbody Visible

			goodsPackageBaseDiscountEmployeeList				: [],			// 임직원 기본할인 정보
			goodsPackageBaseDiscountEmployeeListNoDataVisible	: true,			// 임직원 기본 할인 정보 Nodata Tbody Visible
			goodsPackageBaseDiscountEmployeeListDataVisible		: false,		// 임직원 기본 할인 정보 Data Tbody Visible

			goodsPackageDiscountEmployeeList			: [],					// 임직원 개별할인 정보
			goodsPackageDiscountEmployeeApproList		: [],					// 임직원 개별할인 정보 > 승인 관리자 정보
			goodsPackageDiscountEmployeeListNoDataVisible	: true,				// 임직원 개별할인 정보 > Nodata Tbody Visible
			goodsPackageDiscountEmployeeListDataVisible	: false,				// 임직원 개별할인 정보 > Data Tbody Visible
			visibleGoodsPackageDiscountEmployeeButton : false,					// 임직원 개별할인 할인설정 Button Visible
			/* 임직원 할인 정보 End */

			/* 판매 정보 Start */
			saleType								: 'SALE_TYPE.NORMAL',			// 판매유형 > 일반판매(일반판매만)
			presentYn								: 'NA',						//선물하기 허용 여부
			/* 판매 정보 End */

			/* 배송/발주 정보 Start */
			itemWarehouseList						: [],							// 배송 유형에 따른 출고처 리스트
			undeliverableAreaType					: '',							// 배송 불가 지역
			//발주 유형 개발 전
			//발주 가능여부(올가ERP), 배송 불가 지역, 반품 가능 기간은 품목 정보에서 가져옴
			/* 배송/발주 정보 End */

			/* 혜택/구매 정보 Start */
			isGoodsAdditionalGoodsMappingNoDataTbody	: true,							//추가상품 > NoData Tbody Visible
			isGoodsAdditionalGoodsMappingTbody		: false,							//추가상품 > Data Tbody Visible
			goodsAdditionalGoodsMappingList			: [],								//추가상품 > 추가 상품 목록, goodsAdditionalGoodsMapping-row-template 사용

			couponUseYn								: 'Y',								//혜택 설정 > 쿠폰 사용 허용

			//HGRM-1708 최소 구매 제한 설정 범위 1->''
			limitMinimumCnt							: '1',								//구매 제한 설정 > 최소 구매 > 선택(기본설정)
			limitMaximumType						: 'PURCHASE_LIMIT_MAX_TP.UNLIMIT',	//구매 제한 설정 > 최대 구매 > 제한없음(기본설정)
			isLimitMaximumType						: true,								//구매 제한 설정 > 최대 구매 > Disabled TRUE(판매/전시 > 비회원 체크시에)
			limitMaximumDuration					: '7',								//구매 제한 설정 > 최대 구매 > 기간 > 7일(기본설정)
			//start HGRM-1708  최대 구매 제한 설정 범위 1->''
			limitMaximumCnt							: '',								//구매 제한 설정 > 최대 구매 > 갯수 > 선택(기본설정)
			/* 혜택/구매 정보 End */

			/* 추천상품 등록 Start */
			goodsRecommendList						: [],								//추천상품 등록 > 추천상품 리스트
			isGoodsRecommendNoDataTbody				: true,								//추천상품 등록 > 추천상품 리스트 NoData Tobody Visible
			isGoodsRecommendTbody					: false,							//추천상품 등록 > 추천상품 리스트 Data Tobody Visible
			/* 추천상품 등록 End */

			/* 상품 이미지 Start*/
			goodsPackageImageType					: 'GOODS_PACKAGE_IMG_TP.MIXED',		//(묶음상품)상품이미지형식(묶음상품 전용, 묶음/개별상품 조합/개별상품 전용)
			goodsPackageVideoAutoplayYn				: 'Y',								//동영상 정보 자동재생 여부
			goodsPackageVideoUrl					: '',								//동영상 정보 URL
			/* 상품 이미지 End*/

			/* 상품 상세 기본 정보 Start*/
			goodsPackageBasicDescYn						: 'M',								//(묶음상품)상품상세 기본정보 직접등록 여부(Y:묶음상품전용, M:묶음상품전용+구성상품상세, N:구성상품상세)
			/* 상품 상세 기본 정보 End*/

			/* 상품 공지 Start */
			noticeBelow1StartDate				: '',								// 상품 공지 > 프로모션 시작 기간
			noticeBelow1EndDate					: '',								// 상품 공지 > 프로모션 종료 기간

			noticeBelow1StartYear				: todayDate.oFormat("yyyy-MM-dd"),	// 상품 공지 > 상세 상단공지 노출기간 설정 시작일
			noticeBelow1StartHour				: todayDate.oFormat("HH"),			// 상품 공지 > 프로모션 상세 상단공지 노출기간 설정 시작시간
			noticeBelow1StartMinute				: todayDate.oFormat("mm"),			// 상품 공지 > 프로모션 상세 상단공지 노출기간 설정 시작분
			noticeBelow1EndYear					: lastDate.oFormat("yyyy-MM-dd"),	// 상품 공지 > 상세 상단공지 노출기간 설정 종료일
			noticeBelow1EndHour					: lastDate.oFormat("HH"),			// 상품 공지 > 상세 상단공지 노출기간 설정 종료 시간
			noticeBelow1EndMinute				: lastDate.oFormat("mm"),			// 상품 공지 > 상세 상단공지 노출기간 설정 종료 분

			noticeBelow2StartDate				: '',								// 상품 공지 > 프로모션 시작 기간
			noticeBelow2EndDate					: '',								// 상품 공지 > 프로모션 종료 기간

			noticeBelow2StartYear				: todayDate.oFormat("yyyy-MM-dd"),	// 상품 공지 > 상세 하단공지 노출기간 설정 시작일
			noticeBelow2StartHour				: todayDate.oFormat("HH"),			// 상품 공지 > 프로모션 상세 하단공지 노출기간 설정 시작시간
			noticeBelow2StartMinute				: todayDate.oFormat("mm"),			// 상품 공지 > 프로모션 상세 하단공지 노출기간 설정 시작분
			noticeBelow2EndYear					: lastDate.oFormat("yyyy-MM-dd"),	// 상품 공지 > 상세 하단공지 노출기간 설정 종료일
			noticeBelow2EndHour					: lastDate.oFormat("HH"),			// 상품 공지 > 상세 하단공지 노출기간 설정 종료 시간
			noticeBelow2EndMinute				: lastDate.oFormat("mm"),			// 상품 공지 > 상세 하단공지 노출기간 설정 종료 분
			/* 상품 공지 End */

			/* 기타 정보 Start */
			goodsMemo							: '',
			/* 기타 정보 End */

			/* 승인 상태 Start */
			goodsApprovalStatusViewVisible		: false,							// 승인 상태 > 전체 내역 Visible
            approvalStatusNotification			: '',								// 승인 상태 > 저장불가 사유
            visibleGoodsRegistApprDetailPopupButton	: false,						// 승인 상태 > 상품등록 승인내역상세 Button Visible
            visibleItemClientApprDetailPopupButton	: false,						// 승인 상태 > 거래처 품목 승인 요청 승인내역상세 Button Visible
            visibleGoodsClientApprDetailPopupButton	: false,						// 승인 상태 > 거래처 상품 승인 요청 승인내역상세 Button Visible
			goodsApprovalDeniedVisible			: false,							// 승인 상태 > 반려 사유 Visible
			apprCancelBtnVisible				: false,							// 요청철회 Button Visible
			saveBtnVisible						: true,								// 상품 저장 Button Visible
			itemClientApprVisible				: true,								// 거래처 품목 승인 요청 Visible
			goodsClientApprVisible				: true,								// 거래처 상품 승인 요청 Visible
			goodsRegistGoodsApprId				: '',								// 상품등록 승인 ID
			goodsRegistApprReqUserId			: '',								// 상품등록 승인요청자 ID
			goodsRegistApprStat					: '',								// 상품등록 승인상태
			goodsRegistApprStatName				: '',								// 상품등록 승인상태명
			goodsRegistApprStatusCmnt			: '',								// 상품등록 승인상태 변경 코멘트
			goodsClientGoodsApprId				: '',								// 거래처 상품수정 승인 ID
			goodsClientApprReqUserId			: '',								// 거래처 상품수정 승인요청자 ID
			goodsClientApprStat					: '',								// 거래처 상품수정 승인상태
			goodsClientApprStatName				: '',								// 거래처 상품수정 승인상태명
			goodsClientApprStatusCmnt			: '',								// 거래처 상품수정 승인상태 변경 코멘트
			itemClientItemApprId				: '',								// 거래처 품목수정 승인 ID
			itemClientApprReqUserId				: '',								// 거래처 품목수정 승인요청자 ID
			itemClientApprStat					: '',								// 거래처 품목수정 승인상태
			itemClientApprStatName				: '',								// 거래처 품목수정 승인상태명
			itemClientApprStatusCmnt			: '',								// 거래처 품목수정 승인상태 변경 코멘트
			/* 승인 상태 End*/

			promotionNameGoodsNameSumLength		: 0								// 프로모션명 + 상품명 길이
		},

		/* ########## 상품 이미지 start ########## */
		onlyPackageImageMaxLimit : 10,				// 묶음 상품 전용 이미지 최대 등록가능 개수
		mixedImageMaxLimit : 1,						// 묶음/개별 상품 조합시에 최대 등록가능 개수

		packageImageList : [],						// 묶음 상품 이미지 영역에 출력되는 이미지 정보 목록(묶음상품 전용)
		packageImageFileList : [],					// 묶음 상품 이미지 파일 목록(묶음상품 전용) : 묶음상품 수정 화면에서는 화면에서 사용자가 이미지 추가시에만 packageImageFileList 에 파일 추가됨
		packageImageUploadResultList : [],			// 묶음 상품 이미지 업로드 결과 Data(묶음상품 전용) : 등록/수정 시 사용
		packageImageCount : '',						// 묶음 상품 이미지 영역에 추가된 이미지 개수
		packageImageMaxLimit : 1,					// 묶음 상품 이미지 최대 등록가능 개수
		packageImageNameListToDelete : [],			// 묶음 상품 삭제한 이미지 파일명 목록(수정시에만 사용)

		packageImageMixList : [],					// 묶음 상품 이미지 영역에 출력되는 이미지 정보 목록(묶음/개별상품 조합)
		packageImageMixFileList : [],				// 묶음 상품 이미지 파일 목록(묶음/개별상품 조합) : 묶음상품 수정 화면에서는 화면에서 사용자가 이미지 추가시에만 packageImageFileList 에 파일 추가됨
		packageImageMixUploadResultList : [],		// 묶음 상품 이미지 업로드 결과 Data(묶음/개별상품 조합) : 등록/수정 시 사용

		imageSortOrderChanged : false,				// 묶음 상품 전용 이미지 순서 변경 여부
		goodsImageSortOrderChanged : false,			// 상품별 대표 이미지 순서 변경 여부

		goodsImageList : [],						// 개별 상품 이미지 영역에 출력되는 이미지 정보 목록
		goodsImageUploadResultList : [],			// 상품 이미지 업로드 결과 Data : 상품 등록시 사용
		/* ########## 상품 이미지 end ########## */

		/* 상품공지 파일관련 Start */
		noticeBelow1ImageList : [],											//상세 상단공지 첨부파일
		noticeBelow1ImageFileList : [],										//상세 상단공지 첨부파일 목록
		noticeBelow1ImageUploadResultList : [],								//상세 상단공지 첨부파일 업로드 결과 Data : 품목 등록시 사용

		noticeBelow2ImageList : [],											//상세 하단공지 첨부파일
		noticeBelow2ImageFileList : [],										//상세 하단공지 첨부파일 목록
		noticeBelow2ImageUploadResultList : [],								//상세 하단공지 첨부파일 업로드 결과 Data : 품목 등록시 사용

		// PJH Start
		showNoticeBelow1Date : false, // 상세 상단공지 노출기간 Visible 여부 : 최초 숨김 처리
		showNoticeBelow2Date : false, // 상세 하단공지 노출기간 Visible 여부 : 최초 숨김 처리
		// PJH End
		/* 상품공지 파일관련 End */

		/* click, function, change 등 이벤트 및 함수 Start */
		fnNumberComma : function(e) {
			let val = e.target.value;
			// 판매가가 정상가를 초과할 수 없음
			if(parseInt(val.replaceAll(',','')) > parseInt(e.data.recommendedPrice)) {
				fnKendoMessage({ message : "판매가는 정상가를 초과할 수 없습니다." });
				const beforeData = viewModel.ilGoodsDetail.goodsAdditionalGoodsMappingList.filter(gagml => gagml.ilItemCode == e.data.ilItemCode)[0];
				if(beforeData.tempSalePrice !== undefined) e.target.value = beforeData.tempSalePrice;
				else e.target.value = null;
			} else {
				if(val != "" && val != null) {
					e.target.value = fnNumberWithCommas(val);
					const currentData = viewModel.ilGoodsDetail.goodsAdditionalGoodsMappingList.filter(gagml => gagml.ilItemCode == e.data.ilItemCode)[0];
					currentData.set("tempSalePrice", e.target.value);
				}
			}
		},
		// 기본정보 > 용량정보 입력 > 노출여부에 따른 텍스트 박스 Visible
		onPackageUnitDisplayYn			: function(e) {
			switch(viewModel.ilGoodsDetail.packageUnitDisplayYn) {
				case 'Y' :
					viewModel.ilGoodsDetail.set('isPackageUnitDisplay', true);
					break;
				case 'N' :
					viewModel.ilGoodsDetail.set('packageUnitDesc', "");
					viewModel.ilGoodsDetail.set('isPackageUnitDisplay', false);
					break;
			}
		},

		onPurchaseTargetType : function(e){
			e.preventDefault();

			//전체 선택 및 요소 체크박스에 따른 이벤트 처리
			fnAllChkEvent(e);

			//판매/전시 > 구매 허용 범위 > 비회원 체크 여부에 따른 혜택/구매정보 > 구매 제한 설정 > 최대 구매 제한 설정
			if(viewModel.ilGoodsDetail.purchaseTargetType.length > 0){
				var isLimitMaximum = false;
				for(var i=0; i < viewModel.ilGoodsDetail.purchaseTargetType.length; i++){
					if(viewModel.ilGoodsDetail.purchaseTargetType[i] == "PURCHASE_TARGET_TP.NONMEMBER"){
						isLimitMaximum = true;
					}
				}

				if(isLimitMaximum){
					viewModel.ilGoodsDetail.set('limitMaximumType', 'PURCHASE_LIMIT_MAX_TP.UNLIMIT');
					viewModel.ilGoodsDetail.set('isLimitMaximumType', true);
					viewModel.ilGoodsDetail.set('isMaximumSaleDay', false);
					viewModel.ilGoodsDetail.set('isMaximumSaleQuantity', false);
				}
				else{
					viewModel.ilGoodsDetail.set('isLimitMaximumType', false);
				}
			}
		},

		onGoodsDisplayType : function(e){	//판매 허용 범위(PC/Mobile)
			e.preventDefault();

			//전체 선택 및 요소 체크박스에 따른 이벤트 처리
			fnAllChkEvent(e);
		},

		// 추가상품 삭제 버튼 이벤트
		fnRemoveGoodsAdditionalGoodsMapping : function(e) {
			fnKendoMessage({
				type : "confirm",
				message : "추가상품 등록을 삭제하시겠습니까?",
				ok : function() {
					var index = viewModel.ilGoodsDetail.get("goodsAdditionalGoodsMappingList").indexOf(e.data);	// e.data : "삭제" 버튼을 클릭한 행의 추가상품 세부항목
					viewModel.ilGoodsDetail.get("goodsAdditionalGoodsMappingList").splice(index, 1);				// viewModel 에서 삭제

					if(viewModel.ilGoodsDetail.get("goodsAdditionalGoodsMappingList").length == 0){				//모든 추가 상품 리스트를 다 삭제하면 NoData Tbody를 보여준다.
						viewModel.ilGoodsDetail.set("isGoodsAdditionalGoodsMappingNoDataTbody", true)				//추가상품 > NoData Tbody Visible
						viewModel.ilGoodsDetail.set("isGoodsAdditionalGoodsMappingTbody", false)					//추가상품 > Data Tbody Visible
					}
				},
				cancel : function() {
					return;
				}
			});
		},

		// 추천상품 삭제
		fnRemoveGoodsRecommend : function(e) {
			fnKendoMessage({
				type : "confirm",
				message : "추천상품 등록을 삭제하시겠습니까?",
				ok : function() {
					var index = viewModel.ilGoodsDetail.get("goodsRecommendList").indexOf(e.data);	// e.data : "삭제" 버튼을 클릭한 행의 추천상품 세부항목
					viewModel.ilGoodsDetail.get("goodsRecommendList").splice(index, 1);				// viewModel 에서 삭제

					if(viewModel.ilGoodsDetail.get("goodsRecommendList").length == 0){				//모든 추천 상품 리스트를 다 삭제하면 NoData Tbody를 보여준다.
						viewModel.ilGoodsDetail.set("isGoodsRecommendNoDataTbody", true)			//추천상품 > NoData Tbody Visible
						viewModel.ilGoodsDetail.set("isGoodsRecommendTbody", false)					//추천상품 > Data Tbody Visible
					}
				},
				cancel : function() {
					return;
				}
			});
		},

		//구매 제한 설정 > 최대 구매 > 옵션값에 따른 DropDownList Visible
		onLimitMaximumTypeChange		: function(e) {
			switch(viewModel.ilGoodsDetail.limitMaximumType) {
				case 'PURCHASE_LIMIT_MAX_TP.1DAY' :
					viewModel.ilGoodsDetail.set('isMaximumSaleDay', false);
					viewModel.ilGoodsDetail.set('isMaximumSaleQuantity', true);
					break;
				case 'PURCHASE_LIMIT_MAX_TP.DURATION' :
					viewModel.ilGoodsDetail.set('isMaximumSaleDay', true);
					viewModel.ilGoodsDetail.set('isMaximumSaleQuantity', true);
					break;
				case 'PURCHASE_LIMIT_MAX_TP.UNLIMIT' :
					viewModel.ilGoodsDetail.set('isMaximumSaleDay', false);
					viewModel.ilGoodsDetail.set('isMaximumSaleQuantity', false);
					break;
			}
		},
		//구매 제한 설정 > 최소 구매, 최대 구매 값 비교
		onLimitCntCompare			: function(e) {
			let limitMinimumCnt = viewModel.ilGoodsDetail.get("limitMinimumCnt");
			let limitMaximumCnt = viewModel.ilGoodsDetail.get("limitMaximumCnt");

			if(limitMaximumCnt != "" && limitMaximumCnt != null && limitMinimumCnt > limitMaximumCnt){
				fnKendoMessage({ message : "최소 구매 수량보다 최대 구매 수량이 커야 합니다.", ok : function() {
					viewModel.ilGoodsDetail.set("limitMaximumCnt", "");
				}});
			}
		},
		fnRemovePackageImage : function(e) {	// 묶음상품 전용 상품 이미지 thumbnail 내 "X" 클릭 이벤트 : 추가된 상품 이미지 삭제
			for (var i = this.get("packageImageList").length - 1; i >= 0; i--) {
				if (this.get("packageImageList")[i]['imagePath'] == e.data["imagePath"]) {
					this.get("packageImageList").splice(i, 1); // viewModel 에서 삭제
				}
			}
			viewModel.set('packageImageCount', viewModel.get('packageImageList').length);	// 추가된 상품 이미지 개수 갱신

			var packageImageFileList = this.get('packageImageFileList');					// 화면에서 사용자가 추가한 묶음 상품 이미지 파일 목록
			var isRegisteredImage = true;													// 삭제한 이미지가 기존 등록된 이미지이면 true, 화면에서 새로 추가한 이미지로 등록 전이면 false

			for (var i = packageImageFileList.length - 1; i >= 0; i--) {
				if (packageImageFileList[i]['name'] == e.data['imagePath']) {
					packageImageFileList.splice(i, 1);	// 삭제한 파일명에 해당하는 file 객체 삭제
					isRegisteredImage = false;			// packageImageFileList에는 화면에서 새로 추가한 파일만 존재 : 기등록된 이미지 아님
				}
			}

			if( isRegisteredImage ) { // 기존 등록된 이미지를 사용자가 삭제한 경우 삭제 대상 목록에 추가
				this.packageImageNameListToDelete.push(e.data['imagePath']);
			}

			// 추가된 상품 이미지 개수가 최대 등록가능 개수보다 작은 경우 : 상품 이미지 추가 영역 Visible 처리
			if (viewModel.get('packageImageList') && viewModel.get('onlyPackageImageMaxLimit') //
					&& viewModel.get('packageImageList').length < viewModel.get('onlyPackageImageMaxLimit')) {
				$("#packageImageAdd").show();
				fnAddPackageImageArea("GOODS_PACKAGE_IMG_TP.PACKAGE_GOODS");
			}
		},

		fnRemovePackageImageMix : function(e) {	// 묶음/개별상품 조합 상품 이미지 thumbnail 내 "X" 클릭 이벤트 : 추가된 상품 이미지 삭제
			for (var i = this.get("packageImageMixList").length - 1; i >= 0; i--) {
				if (this.get("packageImageMixList")[i]['imagePath'] == e.data["imagePath"]) {
					this.get("packageImageMixList").splice(i, 1); // viewModel 에서 삭제
				}
			}
			viewModel.set('packageImageCount', viewModel.get('packageImageMixList').length);	// 추가된 상품 이미지 개수 갱신

			var packageImageMixFileList = this.get('packageImageMixFileList');					// 화면에서 사용자가 추가한 묶음 상품 이미지 파일 목록
			var isRegisteredImage = true;														// 삭제한 이미지가 기존 등록된 이미지이면 true, 화면에서 새로 추가한 이미지로 등록 전이면 false

			for (var i = packageImageMixFileList.length - 1; i >= 0; i--) {
				if (packageImageMixFileList[i]['name'] == e.data['imagePath']) {
					packageImageMixFileList.splice(i, 1);	// 삭제한 파일명에 해당하는 file 객체 삭제
					isRegisteredImage = false;			// packageImageFileList에는 화면에서 새로 추가한 파일만 존재 : 기등록된 이미지 아님
				}
			}

			if( isRegisteredImage ) { // 기존 등록된 이미지를 사용자가 삭제한 경우 삭제 대상 목록에 추가
				this.packageImageNameListToDelete.push(e.data['imagePath']);
			}

//			console.log("viewModel.get('packageImageMixList').length : ", viewModel.get('packageImageMixList').length);
//			console.log("viewModel.get('mixedImageMaxLimit') : ", viewModel.get('mixedImageMaxLimit'));

			// 추가된 상품 이미지 개수가 최대 등록가능 개수보다 작은 경우 : 상품 이미지 추가 영역 Visible 처리
			if (viewModel.get('packageImageMixList') && viewModel.get('mixedImageMaxLimit') //
					&& viewModel.get('packageImageMixList').length < viewModel.get('mixedImageMaxLimit')) {
				$("#packageImageAdd").show();
				fnAddPackageImageArea("GOODS_PACKAGE_IMG_TP.MIXED");
			}
		},

		//상품 상세 기본 정보 radio
		goodsPackageBasicDescYnChange : function(e) {
			switch(viewModel.ilGoodsDetail.goodsPackageBasicDescYn) {
				case 'Y' :
					$(".k-editor-widget").css("display", "");
					break;
				case 'M' :
					$(".k-editor-widget").css("display", "");
					break;
				case 'N' :
					$(".k-editor-widget").css("display", "none");
					//$('#goodsPackageBasicDesc').data("kendoEditor").value("");
					break;
			}
		},

		//상세 상단공지 첨부파일 thumbnail 내 "X" 클릭 이벤트 : 이미지 삭제
		fnRemoveNoticeBelow1Image : function(e) {

			for (var i = viewModel.get("noticeBelow1ImageList").length - 1; i >= 0; i--) {
				if (viewModel.get("noticeBelow1ImageList")[i]['imageName'] == e.data["imageName"]) {
					viewModel.get("noticeBelow1ImageList").splice(i, 1); // viewModel 에서 삭제
				}
			}

			var noticeBelow1ImageFileList = viewModel.get('noticeBelow1ImageFileList');

			for (var i = noticeBelow1ImageFileList.length - 1; i >= 0; i--) {
				if (noticeBelow1ImageFileList[i]['name'] == e.data['imageName']) {
					noticeBelow1ImageFileList.splice(i, 1); // 삭제한 파일명에 해당하는 file 객체 삭제
				}
			}

			var noticeBelow1ImageUploadResultList = viewModel.get('noticeBelow1ImageUploadResultList');

			for (var i = noticeBelow1ImageUploadResultList.length - 1; i >= 0; i--) {
				noticeBelow1ImageUploadResultList.splice(i, 1); // 삭제한 파일명에 해당하는 file 객체 삭제
			}
			$(".noticeBelow1StartDate").removeAttr("required");
			$(".noticeBelow1EndDate").removeAttr("required");

			// PJH Start
			viewModel.set('showNoticeBelow1Date', false);  // 첨부파일 삭제시 상세 상단공지 노출기간 숨김 처리
			// PJH End

			noticeBelow1ImageDelete = true;
			viewModel.ilGoodsDetail.set('noticeBelow1StartYear', '');
			viewModel.ilGoodsDetail.set('noticeBelow1StartHour', '');
			viewModel.ilGoodsDetail.set('noticeBelow1StartMinute', '');
			viewModel.ilGoodsDetail.set('noticeBelow1EndYear', '');
			viewModel.ilGoodsDetail.set('noticeBelow1EndHour', '');
			viewModel.ilGoodsDetail.set('noticeBelow1EndMinute', '');
		},
		//상세 하단공지 첨부파일 thumbnail 내 "X" 클릭 이벤트 : 이미지 삭제
		fnRemoveNoticeBelow2Image : function(e) {

			for (var i = viewModel.get("noticeBelow2ImageList").length - 1; i >= 0; i--) {
				if (viewModel.get("noticeBelow2ImageList")[i]['imageNameSecond'] == e.data["imageNameSecond"]) {
					viewModel.get("noticeBelow2ImageList").splice(i, 1); // viewModel 에서 삭제
				}
			}

			var noticeBelow2ImageFileList = viewModel.get('noticeBelow2ImageFileList');

			for (var i = noticeBelow2ImageFileList.length - 1; i >= 0; i--) {
				if (noticeBelow2ImageFileList[i]['name'] == e.data['imageNameSecond']) {
					noticeBelow2ImageFileList.splice(i, 1); // 삭제한 파일명에 해당하는 file 객체 삭제
				}
			}

			var noticeBelow2ImageUploadResultList = viewModel.get('noticeBelow2ImageUploadResultList');

			for (var i = noticeBelow2ImageUploadResultList.length - 1; i >= 0; i--) {
				noticeBelow2ImageUploadResultList.splice(i, 1); // 삭제한 파일명에 해당하는 file 객체 삭제
			}

			$(".noticeBelow2StartDate").removeAttr("required");
			$(".noticeBelow2EndDate").removeAttr("required");

			// PJH Start
			viewModel.set('showNoticeBelow2Date', false);  // 첨부파일 삭제시 상세 하단공지 노출기간 숨김 처리
			// PJH End

			noticeBelow2ImageDelete = true;
			viewModel.ilGoodsDetail.set('noticeBelow2StartYear', '');
			viewModel.ilGoodsDetail.set('noticeBelow2StartHour', '');
			viewModel.ilGoodsDetail.set('noticeBelow2StartMinute', '');
			viewModel.ilGoodsDetail.set('noticeBelow2EndYear', '');
			viewModel.ilGoodsDetail.set('noticeBelow2EndHour', '');
			viewModel.ilGoodsDetail.set('noticeBelow2EndMinute', '');
		},
		//HGRM-1646 초기화 및 시간달력포맷 공통화 수정
		// 시 Dropdown 데이터
		hourDropdownData : function () {

			let hour = [];
			for(var i = 0; i < 24; i++) {
				hour.push({"CODE":('00'+ i).slice(-2),"NAME":('00'+ i).slice(-2)})
			}
			return hour
		},
		// 분 Dropdown 데이터
		MinuteDropdownData : function () {

			let minute = [];
			for(var i = 0; i < 60; i++) {
				minute.push({"CODE":('00'+ i).slice(-2),"NAME":('00'+ i).slice(-2)})
			}
			return minute
		},
		// 날짜 시작일 종료일 유효성 체크
		fnCheckDateValidation : function(type, isStartEnd, mode, id) {

			//시작일 종료일 체크
			let startDateObj = "";
			let endDateObj = "";

			const _value = viewModel.ilGoodsDetail.get(id);

			let _startYear = viewModel.ilGoodsDetail.get(type + "StartYear");// || cacheData[type + "StartYear"];
			let _startHour = viewModel.ilGoodsDetail.get(type + "StartHour");// || cacheData[type + "StartHour"];
			let _startMinute = viewModel.ilGoodsDetail.get(type + "StartMinute");// || cacheData[type + "StartMinute"];

			let _endYear = viewModel.ilGoodsDetail.get(type + "EndYear");// || cacheData[type + "EndYear"];
			let _endHour = viewModel.ilGoodsDetail.get(type + "EndHour");// || cacheData[type + "EndHour"];
			let _endMinute = viewModel.ilGoodsDetail.get(type + "EndMinute");// || cacheData[type + "EndMinute"];

			if( isNotValidData(_value) ) return;

			startDateObj = new Date(_startYear + " " + _startHour + ":" + _startMinute);
			endDateObj = new Date(_endYear + " " + _endHour + ":" + _endMinute);

			//시작
			if( isStartEnd === "start" ) {
				//오늘 날짜와 비교
//				if( todayDate.getTime() > startDateObj.getTime() ) {
//					fnKendoMessage({ message : "오늘 날짜 이후를 선택해주세요.", ok : function() {
//						$("#" + id).focus();
//					}});
//
//					return false;
//				}

				//종료 날짜 없을 경우
				if( isNotValidData(_endYear) || !new Date(_endYear) instanceof Date ) return true;


				if( startDateObj.getTime() > endDateObj.getTime() ) {
					fnKendoMessage({ message : "종료 날짜 이전을 선택해주세요.", ok : function() {
						$("#" + id).focus();
					}});

					return false;
				}

				return true;
			} else {
				//오늘 날짜와 비교
//				if( todayDate.getTime() > endDateObj.getTime() ) {
//					fnKendoMessage({ message : "오늘 날짜 이후를 선택해주세요.", ok : function() {
//						$("#" + id).focus();
//					}});
//
//					return false;
//				}

				//시작 날짜 없을 경우
				if( isNotValidData(_startYear) || !new Date(_startYear) instanceof Date ) return true;


				if( startDateObj.getTime() > endDateObj.getTime() ) {
					fnKendoMessage({ message : "시작 날짜 이후를 선택해주세요.", ok : function() {
						$("#" + id).focus();
					}});

					return false;
				}

				return true;
			}
		},

		//날짜  받기
		fnGetDateFull : function(type) {
			let dateYear = viewModel.ilGoodsDetail.get(type+"Year");
			let dateHour = viewModel.ilGoodsDetail.get(type+"Hour");
			let dateMinute = viewModel.ilGoodsDetail.get(type+"Minute");

			var returnDate = null;

			if(dateYear != "" && dateYear != null){
				returnDate = dateYear + " " + dateHour + ":" + dateMinute;
			}

			return returnDate;

		},

		fnPromotionNameChange : function(e){
			e.preventDefault();
			if(viewModel.ilGoodsDetail.promotionName.length == 0){
				viewModel.fnPromotionDateReset();
			}
		},

		// 시간 분 변경 체크
		fnHourMinuteChange : function(e){
			e.preventDefault();

			let type = "";
			if((e.sender.element.context.id).indexOf('promotionName') > -1) {
				type = 'promotionName';
			} else if((e.sender.element.context.id).indexOf('sale') > -1) {
				type = 'sale';
			} else if((e.sender.element.context.id).indexOf('noticeBelow1') > -1) {
				type = 'noticeBelow1';
			} else if((e.sender.element.context.id).indexOf('noticeBelow2') > -1) {
				type = 'noticeBelow2';
			}

			const _id = e.sender.element.context.id;

			if( isNotValidData(_id) ) return;

			let _isStartEnd = "";
			let _mode = "";

			if( !!_id.match(/end/i) ) {
				_isStartEnd = "end";
			} else if ( !!_id.match(/start/i) ) {
				_isStartEnd = "start";
			} else {
				return;
			}

			if( !!_id.match(/year/i) ) {
				_mode = "year";
			} else if ( !!_id.match(/hour/i) ) {
				_mode = "hour";
			} else if( !!_id.match(/minute/i) ) {
				_mode = "minute"
			} else {
				return;
			}

			if( isNotValidData(_isStartEnd) ||  isNotValidData(_mode) ) return;

			const isSuccess = viewModel.fnCheckDateValidation(type, _isStartEnd, _mode, _id);

			//검증 실패 시 기존 데이터 복원
			if( !isSuccess ) {
				var bindDataValue = cacheData[_id];
				if(!bindDataValue){
					var _firstSearchId = type + _isStartEnd.charAt(0).toUpperCase() + _isStartEnd.slice(1) + "Date";
					if(firstIlGoodsDetailData[_firstSearchId]){
						bindDataValue = kendo.toString(kendo.parseDate(firstIlGoodsDetailData[_firstSearchId]), _mode=="hour" ? "HH" : "mm");
					} else {
						bindDataValue = "";
					}
				}
				viewModel.ilGoodsDetail.set(_id, bindDataValue);
			} else {
				//검증 성공 시 기존 데이터 새로운 값으로 세팅
				cacheData[_id] = viewModel.ilGoodsDetail.get(_id);
			}

			return;
		},

		// 기본정보 > 프로모션 시작일/종료일 변경
		fnDateChange : function(e, id){
			e.preventDefault();

			const _sender = e.sender;

			const formatedDate = fnFormatDate(_sender.element.val(), _sender.options.format);

			if( !isNotValidData(formatedDate) ) {
				//_old값 직접 할당
				_sender.value(formatedDate);
			} else {
				//포맷이 이상하므로 에러 발생
				const _cacheDate = cacheData[id] ? cacheData[id] : kendo.toString(todayDate, "yyyy-MM-dd");
				_sender.value(cacheData[id]);
				fnKendoMessage({message : "올바른 날짜를 입력해주세요.", ok : function() {
					viewModel.ilGoodsDetail[id] = cacheData[id];
					$("#" + id).focus();
				}});
				return;
			}

			//이상한 포맷일경우 _old가 없다.
			if( e.sender._old == null) {
				//여기 타면 안됨
				fnKendoMessage({ message : "기간을 정확히 입력해주세요." , ok : function(){
					viewModel.ilGoodsDetail[id] = cacheData[id];
					$("#" + id).focus();

					return;
				}});

				return;
			}

			//변경된 데이터 set
			viewModel.ilGoodsDetail.set(id, kendo.toString(kendo.parseDate(formatedDate), "yyyy-MM-dd"));

			//날짜 시작일 종료일 유효성 체크
			let type = "";
			if((id).indexOf('promotionName') > -1) {
				type = 'promotionName';
			} else if((id).indexOf('sale') > -1) {
				type = 'sale';
			} else if((id).indexOf('noticeBelow1') > -1) {
				type = 'noticeBelow1';
			} else if((id).indexOf('noticeBelow2') > -1) {
				type = 'noticeBelow2';
			}


			let _isStartEnd = "";

			if( !!id.match(/end/i) ) {
				_isStartEnd = "end";
			} else if ( !!id.match(/start/i) ) {
				_isStartEnd = "start";
			} else {
				return;
			}

			if( isNotValidData(_isStartEnd)  ) return;

			const isSuccess = viewModel.fnCheckDateValidation(type, _isStartEnd, "year", id);

			//검증 실패 시 기존 데이터 복원
			if( !isSuccess ) {
				var bindDataValue = cacheData[id];
				if(!bindDataValue){
					var _firstSearchId = type + _isStartEnd.charAt(0).toUpperCase() + _isStartEnd.slice(1) + "Date";
					if(firstIlGoodsDetailData[_firstSearchId]){
						bindDataValue = kendo.toString(kendo.parseDate(firstIlGoodsDetailData[_firstSearchId]), "yyyy-MM-dd");
					} else {
						bindDataValue = "";
					}
				}
				viewModel.ilGoodsDetail.set(id, bindDataValue);
			} else {
				//검증 성공 시 기존 데이터 새로운 값으로 세팅
				cacheData[id] = viewModel.ilGoodsDetail.get(id);
			}

			return;
		},
		// 기본정보 > 프로모션 시작일/종료일 초기화
		fnPromotionDateReset : function() {
			//수정 데이터로 초기화 일 경우
			viewModel.ilGoodsDetail.set('promotionName', '');
			viewModel.ilGoodsDetail.set('promotionNameStartYear', '');
			viewModel.ilGoodsDetail.set('promotionNameStartHour', todayDate.oFormat("HH"));
			viewModel.ilGoodsDetail.set('promotionNameStartMinute', todayDate.oFormat("mm"));
			viewModel.ilGoodsDetail.set('promotionNameEndYear', '');
			viewModel.ilGoodsDetail.set('promotionNameEndHour', lastDate.oFormat("HH"));
			viewModel.ilGoodsDetail.set('promotionNameEndMinute', lastDate.oFormat("mm"));
		},

		// 판매전시 > 판매기간 시작일/종료일 초기화
		fnSaleDateReset : function() {
			//수정 데이터로 초기화 일 경우
			viewModel.ilGoodsDetail.set('saleStartYear',kendo.toString(kendo.parseDate(viewModel.ilGoodsDetail.get("saleStartDate")), "yyyy-MM-dd") );
			viewModel.ilGoodsDetail.set('saleStartHour', kendo.toString(kendo.parseDate(viewModel.ilGoodsDetail.get("saleStartDate")), "HH"));
			viewModel.ilGoodsDetail.set('saleStartMinute', kendo.toString(kendo.parseDate(viewModel.ilGoodsDetail.get("saleStartDate")), "mm"));
			viewModel.ilGoodsDetail.set('saleEndYear', kendo.toString(kendo.parseDate(viewModel.ilGoodsDetail.get("saleEndDate")), "yyyy-MM-dd") );
			viewModel.ilGoodsDetail.set('saleEndHour', kendo.toString(kendo.parseDate(viewModel.ilGoodsDetail.get("saleEndDate")), "HH"));
			viewModel.ilGoodsDetail.set('saleEndMinute', kendo.toString(kendo.parseDate(viewModel.ilGoodsDetail.get("saleEndDate")), "mm"));
		}
		/* click, function, change 등 이벤트 및 함수 End */
	});
	// #################### viewModel end ####################

	//fnInitialize();	//Initialize Page Call ---------------------------------
	importScript("/js/service/il/goods/goodsCategoryComm.js", function() {
		importScript("/js/service/il/goods/goodsDetailComm.js", function() {
			//추천상품 등록 HTML 컨텐츠 가져오기
			getContentsHtmlByPath('/contents/views/admin/il/goods/goodsPartRecommend.html', function (){
				importScript('/js/service/il/goods/goodsPartRecommendComm.js', function() {
					//상품 승인관련 HTML 컨텐츠 가져오기
					getContentsHtmlByPath('/contents/views/admin/il/goods/goodsApproval.html', function (){
						importScript('/js/service/il/goods/goodsApprovalComm.js', function() {
							fnInitialize();			// 상품 등록시 중복 체크(동일 품목ID, 동일 출고처로는 상품 등록 되지 말아야 함)
						});
					}, $('#approval-view'));
				});
			}, $('#goodsRecommend-view'));
		});
	});
	publicStorageUrl = fnGetPublicUrlPath(); // Public 저장소 Root Url 조회

	function fnGetPublicUrlPath() { // public 저장소를 경유하지 않고 API 서버로 저장소 접근시 url 경로 조회 ( CORS 회피용 )

		var publicUrlPath;

		fnAjax({
			url : "/comn/getPublicStorageUrl",
			method : 'GET',
			async: false,
			success : function(data, status, xhr) {
				publicUrlPath = data['publicUrlPath'] + '/';
			},
			isAction : 'select'
		});

		return publicUrlPath;
	}
	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'goodsPackage',
			callback : fnDuplicateGoods
		});
	}

	function fnUI(){
		importScript("/js/service/il/goods/goodsCategoryComm.js", function() {
			importScript("/js/service/il/goods/goodsDetailComm.js", function() {
				//추천상품 등록 HTML 컨텐츠 가져오기
				getContentsHtmlByPath('/contents/views/admin/il/goods/goodsPartRecommend.html', function (){
					importScript('/js/service/il/goods/goodsPartRecommendComm.js', function() {
						//상품 승인관련 HTML 컨텐츠 가져오기
						getContentsHtmlByPath('/contents/views/admin/il/goods/goodsApproval.html', function (){
							importScript('/js/service/il/goods/goodsApprovalComm.js', function() {
								fnDuplicateGoods();			// 상품 등록시 중복 체크(동일 품목ID, 동일 출고처로는 상품 등록 되지 말아야 함)
							});
						}, $('#approval-view'));
					});
				}, $('#goodsRecommend-view'));
			});
		});
	}

	//--------------------------------- Button Start---------------------------------
	function fnInitButton(){
		$('#fnSave,  #fnGoodsAssemble').kendoButton();
	}
	//--------------------------------- Button End---------------------------------

	//--------------------------------- Etc Function Start-------------------------------
	//동일한 품목ID 출고처ID, 상품유형을 가진 상품이 있다면 등록 제한
	function fnDuplicateGoods(){
		let paramGoodsType = "GOODS_TYPE.PACKAGE";

		if(ilGoodsId != undefined && ilGoodsId != ""){
			fnAjax({
				url		: '/admin/goods/regist/duplicateGoods',
				params	: {ilGoodsId : ilGoodsId},
				async	: false,
				contentType : 'application/json',
				isAction : 'select',
				success	: function(data){
					if(data.ilGoodsId && data.goodsType != paramGoodsType){
						fnKendoMessage({
							message : "해당 상품은 묶음 상품이 아닙니다.",
							ok : function (){
								history.back();
								return;
						}});
					}
					else if(data.ilGoodsId == null) {
						fnKendoMessage({
							message : "존재하지 않는 상품(마스터품목) 정보입니다.",
							ok : function (){
								location.href = "#/goods";
								return;
						}});
					}
					else{
						fnInitButton();				//Initialize Button
						fnInitOptionBox();			//Initialize Option Box
						fnInitUI();
						fnPageMode();				//등록 or 수정에 따른 기본 세팅
						//fnInitSaleStatusRadio();	//판매/전시 > 판매 상태 > 상태 설정
						fnGoodsSaleStatus();		//판매/전시 > 판매 상태 > 묶음 구성상품에 따른 상태 변경
						fnGoodsAuthInputAllow();
					}
				}
			});
		}
		else{
			fnInitButton();				//Initialize Button
			fnInitOptionBox();			//Initialize Option Box
			fnInitUI();
			fnPageMode();				//등록 or 수정에 따른 기본 세팅
			//fnInitSaleStatusRadio();	//판매/전시 > 판매 상태 > 상태 설정
			fnGoodsSaleStatus();		//판매/전시 > 판매 상태 > 묶음 구성상품에 따른 상태 변경
			fnGoodsAuthInputAllow();
		}
	}

	// 저장된 상품 정보 가져오기
	function fnGoodsDetail(ilGoodsId){

		if(ilGoodsId == undefined && ilGoodsId == ""){	//상품 ID가 존재하지 않는다면
			fnKendoMessage({ message : "정상적인 수정 경로가 아닙니다."});
			history.back();
			return;
		}
		else{
			fnAjax({
				url		: '/admin/goods/regist/goodsDetail',
				params	: {ilGoodsId : ilGoodsId},
				async	: false,
				contentType : 'application/json',
				isAction : 'select',
				success	: function(data){
					if(data.ilGoodsDetail == null){
						fnKendoMessage({ message : "정상적인 상품이 아닙니다."});
						history.back();
					}
					else{
						firstIlGoodsDetailData = data.ilGoodsDetail;
						viewModel.set("ilGoodsDetail", data.ilGoodsDetail);																//상품 마스터 기본 정보 내역

						//카테고리 공통 Func을 위한 전역변수
						ilItemCode = data.ilGoodsDetail.ilItemCode;
						urSupplierId = data.ilGoodsDetail.urSupplierId;
						urWarehouseId = data.ilGoodsDetail.urWarehouseId;

						viewModel.ilGoodsDetail.set("visibleUpdateHistory", true);														//기본정보 > 최근 수정일 업데이트내역 버튼 Visible True
						viewModel.ilGoodsDetail.set("visibleDownPopup", true);															//상세이미지 다운로드 Visible True

						if(data.ilGoodsDetail.goodsCreateDate && data.ilGoodsDetail.createUserName && data.ilGoodsDetail.createUserId){
							viewModel.ilGoodsDetail.set("goodsCreateDate", data.ilGoodsDetail.goodsCreateDate + "<BR/>" + data.ilGoodsDetail.createUserName + "(" + data.ilGoodsDetail.createUserId + ")");
						} else if(data.ilGoodsDetail.goodsCreateDate){
							viewModel.ilGoodsDetail.set("goodsCreateDate", data.ilGoodsDetail.goodsCreateDate + "<BR/>시스템");
						}

						if(data.ilGoodsDetail.modifyDate && data.ilGoodsDetail.modifyUserName && data.ilGoodsDetail.modifyUserId){
							viewModel.ilGoodsDetail.set("confirmStatus", data.ilGoodsDetail.modifyDate + "<BR/>" + data.ilGoodsDetail.modifyUserName + "(" + data.ilGoodsDetail.modifyUserId + ")");
						} else if(data.ilGoodsDetail.modifyDate){
							viewModel.ilGoodsDetail.set("confirmStatus", data.ilGoodsDetail.modifyDate + "<BR/>시스템");
						}

						//묶음상품 기본설정 > 묶음 조합 설정 > 묶음상품 구성 정보
						// 선물하기 설정 S
						var hasNotAvailablePresentGoods = false;
						var hasNoPresentGoods = false;
						// 선물하기 설정 E
						if(data.goodsPackageGoodsMappingList != null){
							for(var i=0; i < data.goodsPackageGoodsMappingList.length; i++){
								goodsPackageGoodsMappingGridDs.insert(i, data.goodsPackageGoodsMappingList[i]);
								baseRowCount = data.goodsPackageGoodsMappingList.length;

								if(data.goodsPackageGoodsMappingList[i].extinctionYn == "Y") {
									viewModel.ilGoodsDetail.set("extinctionYn", "Y");
								}

								// 선물하기 설정 S
								if (data.goodsPackageGoodsMappingList[i].goodsType != 'GOODS_TYPE.GIFT' && data.goodsPackageGoodsMappingList[i].goodsType != 'GOODS_TYPE.GIFT_FOOD_MARKETING') { // 구성품이 증정품이 아닌 경우
									if (data.goodsPackageGoodsMappingList[i].presentYn == "NA") {
										hasNotAvailablePresentGoods = true;
									}
									else if (data.goodsPackageGoodsMappingList[i].presentYn == "N") {
										hasNoPresentGoods = true;
									}
								}
								// 선물하기 설정 E
							}

							//상품 구분 rowspan 처리(공통 rowspan 함수 사용)
							mergeGridRows(
								'goodsPackageGoodsMappingGrid' // div 로 지정한 그리드 ID
								, ['goodsPackageType'] // 그리드에서 셀 머지할 컬럼들의 data-field 목록
								, ['goodsPackageType'] // group by 할 컬럼들의 data-field 목록
							);
						}

						// 선물하기 설정 S
						if (hasNotAvailablePresentGoods) {
							$("#presentYnTh").attr("hidden", true);
							$("#presentYnTd").attr("hidden", true);
							viewModel.ilGoodsDetail.set("presentYn", "NA"); // 구성품중에 N이 있는 경우, NA로 강제 설정.
						}
						else if (hasNoPresentGoods) {
							$("#presentYnTh").attr("hidden", false);
							$("#presentYnTd").attr("hidden", false);
							$("#presentYn").data('kendoDropDownList').enable(false);
							viewModel.ilGoodsDetail.set("presentYn", "N"); // 구성품중에 N이 있는 경우, N으로 강제 설정.
						}
						else {
							$("#presentYnTh").attr("hidden", false);
							$("#presentYnTd").attr("hidden", false);
							$("#presentYn").data('kendoDropDownList').enable(true);
						}
						// 선물하기 설정 E

						//기본정보 > 용량정보 입력 > 노출여부 > 텍스트 박스 Visible
						switch(viewModel.ilGoodsDetail.packageUnitDisplayYn) {
							case 'Y' :
								viewModel.ilGoodsDetail.set('isPackageUnitDisplay', true);
								break;
							case 'N' :
								viewModel.ilGoodsDetail.set('packageUnitDesc', "");
								viewModel.ilGoodsDetail.set('isPackageUnitDisplay', false);
								break;
						}

						//HGRM-1646 초기화 및 시간달력포맷 공통화 수정
						//기본정보 > 프로모션 상품 시작일
						if( !isNotValidData(data.ilGoodsDetail.promotionNameStartDate) ) {
							var promotionNameStartDate = kendo.parseDate(data.ilGoodsDetail.promotionNameStartDate);

							viewModel.ilGoodsDetail.set('promotionNameStartYear',kendo.toString(promotionNameStartDate, "yyyy-MM-dd") );
							viewModel.ilGoodsDetail.set('promotionNameStartHour', kendo.toString(promotionNameStartDate, "HH"));
							viewModel.ilGoodsDetail.set('promotionNameStartMinute', kendo.toString(promotionNameStartDate, "mm"));
						}
						else{
							viewModel.ilGoodsDetail.set('promotionNameStartYear', '');
							viewModel.ilGoodsDetail.set('promotionNameStartHour', todayDate.oFormat("HH"));
							viewModel.ilGoodsDetail.set('promotionNameStartMinute', todayDate.oFormat("mm"));
						}
						//기본정보 > 프로모션 상품 종료일
						if( !isNotValidData(data.ilGoodsDetail.promotionNameEndDate) ) {
							var promotionNameEndDate = kendo.parseDate(data.ilGoodsDetail.promotionNameEndDate);

							viewModel.ilGoodsDetail.set('promotionNameEndYear',kendo.toString(promotionNameEndDate, "yyyy-MM-dd") );
							viewModel.ilGoodsDetail.set('promotionNameEndHour', kendo.toString(promotionNameEndDate, "HH"));
							viewModel.ilGoodsDetail.set('promotionNameEndMinute', kendo.toString(promotionNameEndDate, "mm"));
						}
						else{
							viewModel.ilGoodsDetail.set('promotionNameEndYear', '');
							viewModel.ilGoodsDetail.set('promotionNameEndHour', lastDate.oFormat("HH"));
							viewModel.ilGoodsDetail.set('promotionNameEndMinute', lastDate.oFormat("mm"));
						}

						//판매전시 > 판매기간 시작일
						if( !isNotValidData(data.ilGoodsDetail.saleStartDate) ) {
							var saleStartDate = kendo.parseDate(data.ilGoodsDetail.saleStartDate);

							viewModel.ilGoodsDetail.set('saleStartYear',kendo.toString(saleStartDate, "yyyy-MM-dd") );
							viewModel.ilGoodsDetail.set('saleStartHour', kendo.toString(saleStartDate, "HH"));
							viewModel.ilGoodsDetail.set('saleStartMinute', kendo.toString(saleStartDate, "mm"));
						}
						//판매전시 > 판매기간 종료일
						if( !isNotValidData(data.ilGoodsDetail.saleEndDate) ) {
							var saleEndDate = kendo.parseDate(data.ilGoodsDetail.saleEndDate);

							viewModel.ilGoodsDetail.set('saleEndYear',kendo.toString(saleEndDate, "yyyy-MM-dd") );
							viewModel.ilGoodsDetail.set('saleEndHour', kendo.toString(saleEndDate, "HH"));
							viewModel.ilGoodsDetail.set('saleEndMinute', kendo.toString(saleEndDate, "mm"));
						}
						//상품공지 > 상세 상단공지 시작일
						if( !isNotValidData(data.ilGoodsDetail.noticeBelow1StartDate) ) {
							var noticeBelow1StartDate = kendo.parseDate(data.ilGoodsDetail.noticeBelow1StartDate);
							viewModel.ilGoodsDetail.set('noticeBelow1StartYear',kendo.toString(noticeBelow1StartDate, "yyyy-MM-dd") );
							viewModel.ilGoodsDetail.set('noticeBelow1StartHour', kendo.toString(noticeBelow1StartDate, "HH"));
							viewModel.ilGoodsDetail.set('noticeBelow1StartMinute', kendo.toString(noticeBelow1StartDate, "mm"));
						}
						//상품공지 > 상세 상단공지 종료일
						if( !isNotValidData(data.ilGoodsDetail.noticeBelow1EndDate) ) {
							var noticeBelow1EndDate = kendo.parseDate(data.ilGoodsDetail.noticeBelow1EndDate);

							viewModel.ilGoodsDetail.set('noticeBelow1EndYear',kendo.toString(noticeBelow1EndDate, "yyyy-MM-dd") );
							viewModel.ilGoodsDetail.set('noticeBelow1EndHour', kendo.toString(noticeBelow1EndDate, "HH"));
							viewModel.ilGoodsDetail.set('noticeBelow1EndMinute', kendo.toString(noticeBelow1EndDate, "mm"));
						}
						//상품공지 > 상세 하단공지 시작일
						if( !isNotValidData(data.ilGoodsDetail.noticeBelow2StartDate) ) {
							var noticeBelow2StartDate = kendo.parseDate(data.ilGoodsDetail.noticeBelow2StartDate);

							viewModel.ilGoodsDetail.set('noticeBelow2StartYear',kendo.toString(noticeBelow2StartDate, "yyyy-MM-dd") );
							viewModel.ilGoodsDetail.set('noticeBelow2StartHour', kendo.toString(noticeBelow2StartDate, "HH"));
							viewModel.ilGoodsDetail.set('noticeBelow2StartMinute', kendo.toString(noticeBelow2StartDate, "mm"));
						}
						//상품공지 > 상세 하단공지 종료일
						if( !isNotValidData(data.ilGoodsDetail.noticeBelow2EndDate) ) {
							var noticeBelow2EndDate = kendo.parseDate(data.ilGoodsDetail.noticeBelow2EndDate);

							viewModel.ilGoodsDetail.set('noticeBelow2EndYear',kendo.toString(noticeBelow2EndDate, "yyyy-MM-dd") );
							viewModel.ilGoodsDetail.set('noticeBelow2EndHour', kendo.toString(noticeBelow2EndDate, "HH"));
							viewModel.ilGoodsDetail.set('noticeBelow2EndMinute', kendo.toString(noticeBelow2EndDate, "mm"));
						}

						ilGoodsDisplayCategoryList = data.ilGoodsDisplayCategoryList;
						ilGoodsMallinmallCategoryList = data.ilGoodsMallinmallCategoryList;

						// #### 전시카테고리, 몰인몰 카테고리 출력 Start ####
						viewModel.set("isGoodsDisplayVisible", true);
						//기본정보 > 전시 카테고리 > 선택내역 리스트
						fnInitCategoryGrid();
						//기본정보 > 전시 카테고리
						fnInitCtgry();

						//추후에 사용예정이라 주석처리 HGRM-4280
						if(data.ilGoodsDetail.urSupplierId == "2"){																	//공급처가 올가홀푸드이면
							viewModel.set("isMallInMallVisible", true);
						}
						else if(data.ilGoodsDetail.urSupplierId == "5" && data.ilGoodsDetail.mallinmallCategoryId){					//공급처가 풀무원 녹즙(PDM) 이고 브랜드가 잇슬림이면
							viewModel.set("isMallInMallVisible", true);
						}

						//기본정보 > 몰인몰 카테고리 > 선택내역 리스트
						fnInitMallInMallCategoryGrid();
						//기본정보 > 몰인몰 카테고리(공급업체가 올가, 풀무원녹즙2일 경우에만)
						fnInitMallInMallCtgry();

						fnLoadCategoryList(ilGoodsDisplayCategoryList, ilGoodsMallinmallCategoryList);	//저장된 카테고리 리스트 불러오기
						// #### 전시카테고리, 몰인몰 카테고리 출력 End ####

						if(data.goodsPrice.length > 0){
							viewModel.ilGoodsDetail.set("goodsPrice", data.goodsPrice);											//가격 정보 > 판매 가격정보
							viewModel.ilGoodsDetail.set("visibleGoodsPackagePriceInfoNodata", false);							//가격 정보 > 판매 가격정보 > NoData 항목
							viewModel.ilGoodsDetail.set("visibleGoodsPackagePriceInfo", true);									//가격 정보 > 판매 가격정보 > Data 항목
						}
						else{
							viewModel.ilGoodsDetail.set("goodsPrice", []);														//가격 정보 > 판매 가격정보
							viewModel.ilGoodsDetail.set("visibleGoodsPackagePriceInfoNodata", true);							//가격 정보 > 판매 가격정보 > NoData 항목
							viewModel.ilGoodsDetail.set("visibleGoodsPackagePriceInfo", false);									//가격 정보 > 판매 가격정보 > Data 항목
						}

						if(data.goodsDiscountPriorityList.length > 0){
							viewModel.ilGoodsDetail.set("goodsDiscountPriorityList", data.goodsDiscountPriorityList);			//가격 정보 > 행사/할인 내역 > 우선할인
							viewModel.ilGoodsDetail.set("isGoodsDiscountPriorityListNoDataTbody", false);						//가격 정보 > 행사/할인 내역 > 우선할인 > Nodata 항목
							viewModel.ilGoodsDetail.set("isGoodsDiscountPriorityListTbody", true);								//가격 정보 > 행사/할인 내역 > 우선할인 > Data 항목
						}
						else{
							viewModel.ilGoodsDetail.set("goodsDiscountPriorityList", []);										//가격 정보 > 행사/할인 내역 > 우선할인
							viewModel.ilGoodsDetail.set("isGoodsDiscountPriorityListNoDataTbody", true);						//가격 정보 > 행사/할인 내역 > 우선할인 > Nodata 항목
							viewModel.ilGoodsDetail.set("isGoodsDiscountPriorityListTbody", false);								//가격 정보 > 행사/할인 내역 > 우선할인 > Data 항목
						}
						viewModel.ilGoodsDetail.set("visibleGoodsDiscountPriorityButton", true);								// 행사/할인 내역 > 우선할인 > 우선할인 설정 버튼

						if(data.goodsDiscountImmediateList.length > 0){
							viewModel.ilGoodsDetail.set("goodsDiscountImmediateList", data.goodsDiscountImmediateList);			//가격 정보 > 행사/할인 내역 > 즉시할인
							viewModel.ilGoodsDetail.set("isGoodsDiscountImmediateListNoDataTbody", false);						//가격 정보 > 행사/할인 내역 > 즉시할인 > Nodata 항목
							viewModel.ilGoodsDetail.set("isGoodsDiscountImmediateListTbody", true);								//가격 정보 > 행사/할인 내역 > 즉시할인 > Data 항목
						}
						else{
							viewModel.ilGoodsDetail.set("goodsDiscountImmediateList", []);										//가격 정보 > 행사/할인 내역 > 즉시할인
							viewModel.ilGoodsDetail.set("isGoodsDiscountImmediateListNoDataTbody", true);						//가격 정보 > 행사/할인 내역 > 즉시할인 > Nodata 항목
							viewModel.ilGoodsDetail.set("isGoodsDiscountImmediateListTbody", false);							//가격 정보 > 행사/할인 내역 > 즉시할인 > Data 항목
						}
						viewModel.ilGoodsDetail.set("visibleGoodsDiscountImmediateButton", true)								//가격 정보 > 행사/할인 내역 > 즉시할인 > 즉시할인 설정 버튼

						if(data.goodsPackagePriceList.length > 0){
							viewModel.ilGoodsDetail.set("goodsPackagePriceList", data.goodsPackagePriceList);					//가격 정보 > 묶음상품 기본 힐인가
							viewModel.ilGoodsDetail.set("visibleGoodsPackagePriceListNoDataTbody", false);
							viewModel.ilGoodsDetail.set("visibleGoodsPackagePriceListTbody", true);
						}
						else{
							viewModel.ilGoodsDetail.set("goodsPackagePriceList", []);											//가격 정보 > 묶음상품 기본 힐인가
							viewModel.ilGoodsDetail.set("visibleGoodsPackagePriceListNoDataTbody", true);
							viewModel.ilGoodsDetail.set("visibleGoodsPackagePriceListTbody", false);
						}
						viewModel.ilGoodsDetail.set("visibleGoodsPackagePriceButton", true);

						/* 임직원 할인 정보 시작 */
						if(data.goodsPackageEmployeePriceList.length > 0){					//임직원 할인 정보 > 임직원 할인 가격정보
							viewModel.ilGoodsDetail.set("goodsPackageEmployeePriceList", data.goodsPackageEmployeePriceList);
							viewModel.ilGoodsDetail.set("goodsPackageEmployeePriceListNoDataVisible", false);
							viewModel.ilGoodsDetail.set("goodsPackageEmployeePriceListDataVisible", true);
						}
						else {
							viewModel.ilGoodsDetail.set("goodsPackageEmployeePriceList", []);
							viewModel.ilGoodsDetail.set("goodsPackageEmployeePriceListNoDataVisible", true);
							viewModel.ilGoodsDetail.set("goodsPackageEmployeePriceListDataVisible", false);
						}

						if(data.goodsPackageBaseDiscountEmployeeList.length > 0){		//임직원 할인 정보 > 임직원 기본할인 정보
							viewModel.ilGoodsDetail.set("goodsPackageBaseDiscountEmployeeList", data.goodsPackageBaseDiscountEmployeeList);
							viewModel.ilGoodsDetail.set("goodsPackageBaseDiscountEmployeeListNoDataVisible", false);
							viewModel.ilGoodsDetail.set("goodsPackageBaseDiscountEmployeeListDataVisible", true);
						}
						viewModel.ilGoodsDetail.set("visibleGoodsPackageDiscountEmployeeButton", true);

						if(data.goodsPackageDiscountEmployeeList.length > 0) {		//임직원 개별할인 정보
							viewModel.ilGoodsDetail.set("goodsPackageDiscountEmployeeList", data.goodsPackageDiscountEmployeeList);
							viewModel.ilGoodsDetail.set("goodsPackageDiscountEmployeeListNoDataVisible", false);
							viewModel.ilGoodsDetail.set("goodsPackageDiscountEmployeeListDataVisible", true);
						}
						else {
							viewModel.ilGoodsDetail.set("goodsPackageDiscountEmployeeList", []);
							viewModel.ilGoodsDetail.set("goodsPackageDiscountEmployeeListNoDataVisible", true);
							viewModel.ilGoodsDetail.set("goodsPackageDiscountEmployeeListDataVisible", false);
						}
						/* 임직원 할인 정보 끝 */

						viewModel.ilGoodsDetail.set("goodsAdditionalGoodsMappingList", data.goodsAdditionalGoodsMappingList);	//혜택/구매 정보 > 추가 상품 > 저장된 추가 상품 리스트 내역

						if(data.goodsAdditionalGoodsMappingList.length > 0){
							viewModel.ilGoodsDetail.set("isGoodsAdditionalGoodsMappingNoDataTbody", false);
							viewModel.ilGoodsDetail.set("isGoodsAdditionalGoodsMappingTbody", true);
						}
						else{
							viewModel.ilGoodsDetail.set("isGoodsAdditionalGoodsMappingNoDataTbody", true);
							viewModel.ilGoodsDetail.set("isGoodsAdditionalGoodsMappingTbody", false);
						}

						//추천상품 등록 > 추천상품 리스트
						viewModel.ilGoodsDetail.set("goodsRecommendList", data.goodsRecommendList);

						if(data.goodsRecommendList.length > 0){
							viewModel.ilGoodsDetail.set("isGoodsRecommendNoDataTbody", false);
							viewModel.ilGoodsDetail.set("isGoodsRecommendTbody", true);
						}
						else{
							viewModel.ilGoodsDetail.set("isGoodsRecommendNoDataTbody", true);
							viewModel.ilGoodsDetail.set("isGoodsRecommendTbody", false);
						}

						//혜택/구매 정보 > 구매 제한 설정 > 최대 구매 설정값에 따른 Visible
						switch(viewModel.ilGoodsDetail.limitMaximumType) {
							case 'PURCHASE_LIMIT_MAX_TP.1DAY' :
								viewModel.ilGoodsDetail.set('isMaximumSaleDay', false);
								viewModel.ilGoodsDetail.set('isMaximumSaleQuantity', true);
								break;
							case 'PURCHASE_LIMIT_MAX_TP.DURATION' :
								viewModel.ilGoodsDetail.set('isMaximumSaleDay', true);
								viewModel.ilGoodsDetail.set('isMaximumSaleQuantity', true);
								break;
							case 'PURCHASE_LIMIT_MAX_TP.UNLIMIT' :
								viewModel.ilGoodsDetail.set('isMaximumSaleDay', false);
								viewModel.ilGoodsDetail.set('isMaximumSaleQuantity', false);
								break;
						}

						//판매/전시 > 구매 허용 범위 > 비회원이 check 상태면
						if(data.ilGoodsDetail.purchaseNonmemberYn == "Y") {
							viewModel.ilGoodsDetail.set('limitMaximumType', 'PURCHASE_LIMIT_MAX_TP.UNLIMIT');
							viewModel.ilGoodsDetail.set('isLimitMaximumType', true);
							viewModel.ilGoodsDetail.set('isMaximumSaleDay', false);
							viewModel.ilGoodsDetail.set('isMaximumSaleQuantity', false);
						}

						//판매/전시 > 판매 상태 > 수정시에 disabled를 false로 변경
						viewModel.ilGoodsDetail.set('isSaleStatus', false);

						var goodsPackageGoodsMappingDatas = goodsPackageGoodsMappingGrid.dataSource.data().slice();		// 묶음상품 선택 그리드의 데이타
						var allPackageIlGoodsIds = [];

						//console.log("goodsPackageGoodsMappingDatas", goodsPackageGoodsMappingDatas);

						//상품 이미지 > 이미지 유형 RADIO 버튼에 따른 화면 조정
						fnPackageImageList(viewModel.ilGoodsDetail.goodsPackageImageType);

						//상품 이미지 > 묶음상품 전용 이미지 리스트
						fnGoodsImageList(data.goodsPackageImageList);

						if(goodsPackageGoodsMappingDatas.length > 0){
							for (var i = 0; i < goodsPackageGoodsMappingDatas.length; i++){
								allPackageIlGoodsIds.push(goodsPackageGoodsMappingDatas[i].targetGoodsId);
							}

							fnGoodsEtcAssemble(allPackageIlGoodsIds);		// 배송/발주 정보 > 배송 불가지역, 반품가능 기간 처리
							fnGoodsPackageInfo(allPackageIlGoodsIds);		// 상품정보 제공 고시, 상품 영양정보
						}

						if(data.goodsImageList && data.goodsImageList.length > 0) {
							for (var i = 0; i < data.goodsImageList.length; i++){
								if(data.goodsImageList[i].goodsType != "GOODS_TYPE.GIFT" && data.goodsImageList[i].goodsType != "GOODS_TYPE.GIFT_FOOD_MARKETING"){	//증정품은 상품 이미지에 포함되지 않음, 개별상품 이미지 저장순서대로 입력처리
									//묶음 구성 상품에 따른 상품별 대표 이미지 리스트
									viewModel.get('goodsImageList').push({
										  goodsId: data.goodsImageList[i].targetGoodsId
										, imageSrc: publicStorageUrl + data.goodsImageList[i].size180Image
									});
								}
							}
						}

						//상품 상세 기본 정보
						$('#goodsPackageBasicDesc').data("kendoEditor").value(viewModel.ilGoodsDetail.goodsPackageBasicDesc);

						switch(viewModel.ilGoodsDetail.goodsPackageBasicDescYn) {
							case 'Y' :
								$(".k-editor-widget").css("display", "");
								break;
							case 'M' :
								$(".k-editor-widget").css("display", "");
								break;
							case 'N' :
								$(".k-editor-widget").css("display", "none");
								//$('#goodsPackageBasicDesc').data("kendoEditor").value("");
								break;
						}

						//상품 공지 > 상세 상단공지 이미지 Upload Info
						var noticeBelow1ImageUrl = data.ilGoodsDetail.noticeBelow1ImageUrl;

						if(noticeBelow1ImageUrl != null){
							//상품 공지 > 상세 상단공지 이미지
							viewModel.get('noticeBelow1ImageList').push({
								imageName : data.ilGoodsDetail.noticeBelow1ImageUrl, // local 에서 최초 추가한 이미지 : id 역할을 하는 imageName 으로 원본 파일명을 그대로 사용
								imageOriginalName : data.ilGoodsDetail.noticeBelow1ImageUrl, // 원본 File 명
								sort : '', // 정렬순서
								imageSrc : publicStorageUrl+data.ilGoodsDetail.noticeBelow1ImageUrl // 상품 이미지 url
							});

							var noticeBelow1ImageUrlExtensionSplit = noticeBelow1ImageUrl.split('.');
							var noticeBelow1ImageUrlFileSplit = noticeBelow1ImageUrl.split('/');
							var fileExtension1 = noticeBelow1ImageUrlExtensionSplit[noticeBelow1ImageUrlExtensionSplit.length-1];
							var physicalFileName1 = noticeBelow1ImageUrlFileSplit[noticeBelow1ImageUrlFileSplit.length-1];
							var serverSubPath1 = noticeBelow1ImageUrl.replace(physicalFileName1, "");

							viewModel.get('noticeBelow1ImageUploadResultList').push({
								fieldName : "noticeBelow1Image01",
								originalFileName : "",
								fileExtension : fileExtension1,
								serverSubPath : serverSubPath1,
								physicalFileName : physicalFileName1,
								contentType : "",
								fileSize : ""
							});
						}

						//상품 공지 > 상세 하단공지 이미지 Upload Info
						var noticeBelow2ImageUrl = data.ilGoodsDetail.noticeBelow2ImageUrl;

						if(noticeBelow2ImageUrl != null){
							//상품 공지 > 상세 하단공지 이미지
							viewModel.get('noticeBelow2ImageList').push({
								imageNameSecond : data.ilGoodsDetail.noticeBelow2ImageUrl, // local 에서 최초 추가한 이미지 : id 역할을 하는 imageName 으로 원본 파일명을 그대로 사용
								imageOriginalName : data.ilGoodsDetail.noticeBelow2ImageUrl, // 원본 File 명
								sort : '', // 정렬순서
								imageSrcSecond : publicStorageUrl+data.ilGoodsDetail.noticeBelow2ImageUrl, // 상품 이미지 url
							});

							var noticeBelow2ImageUrlExtensionSplit = noticeBelow2ImageUrl.split('.');
							var noticeBelow2ImageUrlFileSplit = noticeBelow2ImageUrl.split('/');
							var fileExtension2 = noticeBelow2ImageUrlExtensionSplit[noticeBelow2ImageUrlExtensionSplit.length-1];
							var physicalFileName2 = noticeBelow2ImageUrlFileSplit[noticeBelow2ImageUrlFileSplit.length-1];
							var serverSubPath2 = noticeBelow2ImageUrl.replace(physicalFileName2, "");

							viewModel.get('noticeBelow2ImageUploadResultList').push({
								fieldName : "noticeBelow2Image01",
								originalFileName : "",
								fileExtension : fileExtension2,
								serverSubPath : serverSubPath2,
								physicalFileName : physicalFileName2,
								contentType : "",
								fileSize : ""
							});
						}

						// PJH Start
						if( data.ilGoodsDetail.noticeBelow1ImageUrl ) {  // 상세 상단공지 ImageUrl 조회시
							viewModel.set('showNoticeBelow1Date', true); // 상세 상단공지 노출기간 Visible 처리
						}

						if( data.ilGoodsDetail.noticeBelow2ImageUrl ) {  // 상세 하단공지 ImageUrl 조회시
							viewModel.set('showNoticeBelow2Date', true); // 상세 하단공지 노출기간 Visible 처리
						}

						// PJH End
						if(data.goodsShippingTemplateList){
							fnItemWarehouseList(data.goodsShippingTemplateList);		// 배송/발주 정보 > 배송유형 > 출고처에 따른 배송유형별 리스트
						}

						viewModel.ilGoodsDetail.set("ilItemCode", data.ilGoodsDetail.ilItemCode);
						viewModel.ilGoodsDetail.set("itemBarcode", data.ilGoodsDetail.itemBarcode);
						viewModel.ilGoodsDetail.set("urWarehouseId", data.ilGoodsDetail.urWarehouseId);
						viewModel.ilGoodsDetail.set("warehouseGrpCd", data.ilGoodsDetail.warehouseGrpCd);

						//상품 수정 상세 화면 진입시 추가 매핑
						fnGoodsApprDetail(pageParam.ilGoodsApprId);

						/* 풀무원샵 상품코드 시작 */
						if(data.goodsCodeList.length > 0){
							for(var i=0; i < data.goodsCodeList.length; i++) {
								if (i != 0){
									$(".js__add-goods-code").trigger("click");
								}
							}
							for(var i=0; i < data.goodsCodeList.length; i++) {
								var goodsCode = data.goodsCodeList[i].goodsNo;
								$("input[name='goodsCode']:eq("+i+")").val(goodsCode);
								$("input[name='goodsCode']:eq("+i+")").data("checkYn", "N")
							}
						}
						/* 풀무원샵 상품코드 종료 */

						// 상품상세 미리보기 버튼 비노출 처리
						if(data.ilGoodsDetail.displayYn == "N") {
							$('#btnGoodsPreview').attr('disabled', true);       // 상품상세 미리보기 버튼:비활성
						} else {
							$('#btnGoodsPreview').attr('disabled', false);      // 상품상세 미리보기 버튼: 활성
						}
					}
				}
			});
		}
	}

	// 저장된 카테고리 정보 가져오기
	function fnLoadCategoryList(ilGoodsDisplayCategoryList, ilGoodsMallinmallCategoryList){

		//기본정보 > 저장된 전시카테고리 리스트 내역
		if(ilGoodsDisplayCategoryList){
			if( ilGoodsDisplayCategoryList.length > 0 ){
				$('#goodsDisplayCategoryGridArea').show();
			}
			for (var i = 0; i < ilGoodsDisplayCategoryList.length; i++) {
				ctgryGridDs.add(ilGoodsDisplayCategoryList[i]);
			}
		}

		//기본정보 > 저장된 몰인몰카테고리 리스트 내역
		if( ilGoodsMallinmallCategoryList){
			if( ilGoodsMallinmallCategoryList.length > 0 ){
				$('#mallInMallCategoryGridArea').show();
			}
			for (var i = 0; i < ilGoodsMallinmallCategoryList.length; i++) {
				mallInMallCtgryGridDs.add(ilGoodsMallinmallCategoryList[i]);
			}
		}
	}

	//전체 선택 및 요소 체크박스에 따른 이벤트 처리
	function fnAllChkEvent(e){
		if( e.target.value == "ALL" ){
			if( $("input[name=" + e.target.name + "]:eq(0)").is(":checked") ){
				$("input[name=" + e.target.name + "]:gt(0)").each(function(idx){
					if( viewModel.ilGoodsDetail.get(e.target.name).indexOf($(this).val()) < 0 ){
						viewModel.ilGoodsDetail.get(e.target.name).push($(this).val());
					}
				});
			}
			else{
				$("input[name=" + e.target.name + "]:gt(0)").each(function(idx){
					viewModel.ilGoodsDetail.get(e.target.name).remove($(this).val());
				});
			}
		}
		else{
			if( !$("#" + e.target.id).is(":checked") && $("input[name=" + e.target.name + "]:eq(0)").is(":checked") ){
				viewModel.ilGoodsDetail.get(e.target.name).remove($("input[name=" + e.target.name + "]:eq(0)").val());
			}
			else if( $("#" + e.target.id).is(":checked") && ($("input[name=" + e.target.name + "]").length - 1) == viewModel.ilGoodsDetail.get(e.target.name).length ){
				viewModel.ilGoodsDetail.get(e.target.name).push($("input[name=" + e.target.name + "]:eq(0)").val());
			}
		}
	}

	//프로모션 상품명 Validation Check
	function fnPromotionNameValidationCheck() {

		var $targetObject = $("#promotionName");
		var maxLength = promotionNameMaxByteLength; // 최대 byte 길이

		var validationCheckFunction = function(){  // keyup, focusout 이벤트 listener
			var tempValue = $targetObject.val();

			if(tempValue.length > 0) {
				if( parseInt( promoByteCheck($targetObject) ) > parseInt( maxLength ) ) { //
					var limitLength = getCharIndexLessThanMaxByte($targetObject, maxLength);
					$(this).val( tempValue.substring( 0, limitLength ) ); // limitLength 의 바로 전 index 까지만 출력
				}
			}
		};

		$targetObject.keyup(validationCheckFunction);	// keyup 이벤트에 listener 적용
		$targetObject.focusout(validationCheckFunction); // 포커스아웃 이벤트의 경우에도 동일하게 적용
	}

	// 프로모션 + 상품명 30자 제한 Validation Check
	function fnPromotionGoodsNameValidationCheck() {

		var $promotionName = $("#promotionName");	// 프로모션명
		var $goodsName = $("#goodsName");			// 상품명

		var totalLength = 0;

		// 프로모션명이 있는경우 해당 byte값을 totalLength에 추가한다. 최대 12byte
		if($promotionName.val().length > 0) {
			totalLength += parseInt( promoByteCheck($promotionName));
		}

		// 상품명이 있는경우 해당 byte값을 totalLength 에 추가한다.
		if($goodsName.val().length > 0) {
			totalLength += parseInt( promoByteCheck($goodsName));
		}
		viewModel.ilGoodsDetail.set('promotionNameGoodsNameSumLength', totalLength);
	}


	//검색 키워드 입력 Validation Check
	function fnSearchKeywordValidationCheck() {

		var $targetObject = $("#searchKeyword");
		var regexpFilter = /[\{\}\[\]\/?.;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi; // 쉼표 제외한 특수 문자 입력 제외
		var separator = ','; // 키워드 구분자 : regexpFilter 에 포함되면 안됨
		var maxCount = searchKeywordMaxCount; // 최대 등록 가능 건수 : 입력값을 키워드 구분자로 split 한 배열의 길이와 비교함

		var validationCheckFunction = function(){  // keyup, focusout 이벤트 listener
			var tempValue = $targetObject.val();

			if(tempValue.length > 0) {
				if(tempValue.match(regexpFilter)) {  // 쉼표 제외한 특수문자 입력 방지
					$(this).val(tempValue.replace(regexpFilter, ""));
				}

				if( tempValue.charAt(0) == separator ) { // 입력값은 키워드 구분자로 시작할 수 없음
					$(this).val(tempValue.slice( 1 ));   // 첫번째 입력값이 키워드 구분자인 경우 제거
				}

				if( tempValue.indexOf( separator + separator ) >= 0 ) { // 키워드 구분자 연속 입력 방지
					$(this).val(tempValue.replace(separator + separator, separator));
				}

				var keywordArray = tempValue.split(separator); // 키워드 구분자로 split

				if( keywordArray.length > maxCount ) { // 키워드 최대 등록 가능 건수 초과시
					$(this).val( oldSearchKeywordValue ); // 이전 입력값으로 원복

					fnKendoMessage({
						message : '키워드는 ' + maxCount + '건까지 등록 가능합니다.'
					});
				} else {
					oldSearchKeywordValue = tempValue;
				}

			}
		};

		$targetObject.keyup(validationCheckFunction);	// keyup 이벤트에 listener 적용
		$targetObject.focusout(validationCheckFunction); // 포커스아웃 이벤트의 경우에도 동일하게 적용

		$targetObject.keypress(function(event){ // keypress 이벤트에 특수문자 입력방지 로직 적용
			return regexpFilter.test(event.key) ? false : true;
		});
	}

	//상품공지 > 상세 상단공지 > 파일선택 버튼 이벤트
	function fnNoticeBelow1File() {
		$('#uploadNoticeBelow1Image').trigger('click');
	}

	//상품공지 > 상세 하단공지 > 파일선택 버튼 이벤트
	function fnNoticeBelow2File() {
		$('#uploadNoticeBelow2Image').trigger('click');
	}

	/**
	 * 상세 상단공지 이미지 업로드
	 **/
	function fnUploadNoticeBelow1Image() {

		var formData = new FormData();
		var noticeBelow1ImageFileList = viewModel.get('noticeBelow1ImageFileList');

		for (var i = 0; i < noticeBelow1ImageFileList.length; i++) {
			// noticeBelow1Image01, noticeBelow1Image02, ... 형식으로 formData 에 이미지 file 객체 append, 현재는 단건만 저장됨!!!!
			formData.append('noticeBelow1Image' + ('0' + (i + 1)).slice(-2), noticeBelow1ImageFileList[i]);
		}

		formData.append('storageType', 'public'); // storageType 지정
		formData.append('domain', 'il'); // domain 지정

		var noticeBelow1ImageUploadResultList; // 상품 이미지 업로드 결과 목록

		$.ajax({
			url : '/comn/fileUpload',
			data : formData,
			type : 'POST',
			contentType : false,
			processData : false,
			async : false,
			success : function(data) {
				data = data.data;

				noticeBelow1ImageUploadResultList = data['addFile'];
			}
		});

		return noticeBelow1ImageUploadResultList;
	}

	/**
	 * 상세 하단공지 이미지 업로드
	 **/
	function fnUploadNoticeBelow2Image() {

		var formData = new FormData();
		var noticeBelow2ImageFileList = viewModel.get('noticeBelow2ImageFileList');

		for (var i = 0; i < noticeBelow2ImageFileList.length; i++) {
			// noticeBelow2Image01, noticeBelow2Image02, ... 형식으로 formData 에 이미지 file 객체 append, 현재는 단건만 저장됨!!!!
			formData.append('noticeBelow2Image' + ('0' + (i + 1)).slice(-2), noticeBelow2ImageFileList[i]);
		}

		formData.append('storageType', 'public'); // storageType 지정
		formData.append('domain', 'il'); // domain 지정

		var noticeBelow2ImageUploadResultList; // 상품 이미지 업로드 결과 목록

		$.ajax({
			url : '/comn/fileUpload',
			data : formData,
			type : 'POST',
			contentType : false,
			processData : false,
			async : false,
			success : function(data) {
				data = data.data;
				noticeBelow2ImageUploadResultList = data['addFile'];
			}
		});

		return noticeBelow2ImageUploadResultList;
	}

	//데이터 비었는지 체크하는 함수
	function isNotValidData(data) {
		if( data === undefined || typeof data === "undefined" || data === null || data === "" ) return true;
		else return false;
	}

	function isEmpty(obj){
		return Object.keys(obj).length === 0;
	}

	/**
	 * 프로모션 상품명 Validation Check
	 */
	function fnPromotionNameValidationCheck() {

		var $targetObject = $("#promotionName");
		var maxLength = promotionNameMaxByteLength; // 최대 byte 길이

		var validationCheckFunction = function(){  // keyup, focusout 이벤트 listener
			var tempValue = $targetObject.val();

			if(tempValue.length > 0) {

				if( parseInt( promoByteCheck($targetObject) ) > parseInt( maxLength ) ) { //

					var limitLength = getCharIndexLessThanMaxByte($targetObject, maxLength);
					$(this).val( tempValue.substring( 0, limitLength ) ); // limitLength 의 바로 전 index 까지만 출력
/*

					fnKendoMessage({
						message : "프로모션 상품명은 " + maxLength + " 바이트까지 입력 가능합니다."
					});
*/

				}
			}
		};

		$targetObject.keyup(validationCheckFunction);	// keyup 이벤트에 listener 적용
		$targetObject.focusout(validationCheckFunction); // 포커스아웃 이벤트의 경우에도 동일하게 적용

	}

	/**
	 * 바이트수 반환
	 *
	 * @param el : tag jquery object
	 * @returns {Number}
	 */
	function promoByteCheck(el){
		var codeByte = 0;
		for (var idx = 0; idx < el.val().length; idx++) {
			var oneChar = escape(el.val().charAt(idx));
			if ( oneChar.length == 1 ) {
				codeByte ++;
			} else if (oneChar.indexOf("%u") != -1) {
				codeByte += 2;
			} else if (oneChar.indexOf("%") != -1) {
				codeByte ++;
			}
		}
		return codeByte;
	}

	/**
	 * 최대 바이트 수에 해당하는 문자열 내 char index 반환
	 *
	 * => 해당 함수의 반환값은 substring 함수 내에서 사용
	 *
	 * @param el : tag jquery object
	 * @param maxByteLength : 최대 바이트 수
	 * @returns {Number}
	 */
	function getCharIndexLessThanMaxByte(el, maxByteLength){
		var codeByte = 0;

		for (var idx = 0; idx < el.val().length; idx++) {
			var oneChar = escape(el.val().charAt(idx));
			if ( oneChar.length == 1 ) {
				codeByte ++;
			} else if (oneChar.indexOf("%u") != -1) {
				codeByte += 2;
			} else if (oneChar.indexOf("%") != -1) {
				codeByte ++;
			}

			if( codeByte == maxByteLength ){  // 해당 char index 까지는 포함해야 하므로 +1 하여 반환
				return idx + 1;
			} else if( codeByte > maxByteLength ) {
				return idx;
			}
		}
	}

	//배송/발주 정보 > 배송유형 > 출고처에 따른 배송유형별 리스트
	function fnItemWarehouseList(goodsShippingTemplateList){
		fnAjax({
			url : "/admin/goods/regist/itemWarehouseList",
			params : {
				ilItemCode : viewModel.ilGoodsDetail.ilItemCode
			,	urWarehouseId : viewModel.ilGoodsDetail.urWarehouseId
			,	storeYn : 'N'
			},
			isAction : 'select',
			success : function(data, status, xhr) {
				fnWarehouseShippingTemplateList(goodsShippingTemplateList, data);
			}
		});
	}

	function fnGoodsPackageInfo(allPackageIlGoodsIds) {
		// 상품정보제공고시, 상품영양정보등 을 조회
		var url  = '/admin/goods/regist/getGoodsInfo';
		var cbId = 'goodsInfo';

		var data = {
			ilGoodsIds : allPackageIlGoodsIds.join(",")
		}

		fnAjax({
			url	 : url,
			params  : data,
			success :
				function( data ){
					fnBizCallback(cbId, data);
				},
				isAction : 'batch'
		});

		// 상품 이미지 출력
//		for (var i in newAddGoodsDataItems) {
//			viewModel.get('goodsImageList').push({
//				  goodsId: newAddGoodsDataItems[i].ilGoodsId
//				, imageSrc: publicStorageUrl + newAddGoodsDataItems[i].size180Image
//			});
//		}
	}

	function fnMallInMallCtgryDisplayAllow(indivisualGoodsDatas){	//묶음 상품 조합(증정품 제외)에 따른 몰인몰 카테고리 노출여부 처리
		var mallInMallCtgryDisplayAllowNum = 0;					// 몰인몰 카테고리 노출여부

		if(indivisualGoodsDatas){
			for (var i = 0; i < indivisualGoodsDatas.length; i++){
				//공급처가 올가홀푸드이거나 공급처가 풀무원 녹즙(PDM) 이고 브랜드가 잇슬림, 베이비밀이면
				if(indivisualGoodsDatas[i].supplierCode == "OG" || (indivisualGoodsDatas[i].supplierCode == "DM" && indivisualGoodsDatas[i].mallinmallCategoryId)){
					mallInMallCtgryDisplayAllowNum = mallInMallCtgryDisplayAllowNum + 0;
				}
				else{
					mallInMallCtgryDisplayAllowNum = mallInMallCtgryDisplayAllowNum + 1;
				}
			}

			//console.log("mallInMallCtgryDisplayAllowNum : ", mallInMallCtgryDisplayAllowNum);

			if(mallInMallCtgryDisplayAllowNum == 0){	//모든 조합이 몰인몰 카테고리 노출 되는 조건이라면
				if(!viewModel.isMallInMallVisible){		//몰인몰 카테고리가 노출되지 않은 상태일때만 초기화 처리를 한다.
					mallInMallCtgryGridDs.data([]);
					$("#mallInMallCategory1").empty();
					$("#mallInMallCategory2").empty();
					$("#mallInMallCategory3").empty();
					$("#mallInMallCategory4").empty();
					$("#selectMallInMallCtgoryText-addon").hide();
					$("#selectMallInMallCtgoryText").text("전시 카테고리를 선택한 후 ‘카테고리 추가’ 버튼을 선택해 주세요.");;
					$("#mallInMallCategoryGridArea").hide();
					viewModel.set("isMallInMallVisible", true);
					fnInitMallInMallCategoryGrid();
					//기본정보 > 몰인몰 카테고리
					fnInitMallInMallCtgry();
				}
			}
			else{
				if(mallInMallCtgryGridDs) {
					mallInMallCtgryGridDs.data([]);
					$("#mallInMallCategory1").empty();
					$("#mallInMallCategory2").empty();
					$("#mallInMallCategory3").empty();
					$("#mallInMallCategory4").empty();
					$("#selectMallInMallCtgoryText-addon").hide();
					$("#selectMallInMallCtgoryText").text("전시 카테고리를 선택한 후 ‘카테고리 추가’ 버튼을 선택해 주세요.");;
					$("#mallInMallCategoryGridArea").hide();
					viewModel.set("isMallInMallVisible", false);
				}
			}
		}
	}

	function fnGoodsEtcAssemble(allPackageIlGoodsIds){	//묶음 상품 리스트 조합의 배송 불가 지역, 반품 가능 기간 산출

		var data = { ilGoodsIds : allPackageIlGoodsIds.join(",") }

		fnAjax({
			url : "/admin/goods/regist/etcAssemble",
			params : data,
			isAction : 'select',
			success : function(data, status, xhr) {
				$("#undeliverableAreaTypeName").text(data.undeliverableAreaTypeAssembleCodeName);
				$("#returnPeriod").text(data.assembleReturnPeriodValue);
				undeliverableAreaType = data.undeliverableAreaTypeAssembleCode;
			}
		});
	}

	function fnGoodsPackageResetConfirm(){		//기준 상품정보 제외하고 모든 내역을 초기화 처리 함

		fnKendoMessage({
			type : "confirm",
			message : "초기화 실행 시 기준상품을 제외한 구성정보가 초기화 됩니다. 진행하시겠습니까?",
			ok : function() {
				fnGoodsPackageReset();
			}
		});
	}

	function fnGoodsPackageReset(){		//기준 상품정보 제외하고 모든 내역을 초기화 처리 함
		var selectRows 	= aGrid.tbody.find('input[name=purchaseQuanity]').closest('tr');

		if(selectRows){
			for(var i=0; i< selectRows.length;i++){
				if(i != 0){
					aGrid.removeRow($(selectRows[i]));
				}
			}
		}

		bGridDs.data([]);

		$("#undeliverableAreaTypeName").text("");		//배송/발주 정보 > 배송 불가 지역 초기화
		$("#returnPeriod").text("");					//배송/발주 정보 > 반품 가능 기간 초기화

		calculateGoodsPriceRate();
	}

	function fnSave(){

		/* 풀무원샵 상품코드 */
		let goodsCheckFlag		= true;
		let goodsCodeList		= new Array();
		let checkGoodsCodeList	= new Array();
		let goodsCodeCnt		= $("input[name='goodsCode']").length;

		fnPromotionGoodsNameValidationCheck();			// 프로모션명 + 상품명 30자 체크

		if(goodsCodeCnt > 0) {
			$("input[name='goodsCode']").each(function () {
				let that = $(this);
				let thisVal = that.val();
				let checkYn	= that.data("checkYn");
				let i;
				for (i in goodsCodeList) {
					if (goodsCodeList[i].goodsNo == thisVal) {
						goodsCheckFlag = false;
						return false;
					}
				}
				if (goodsCheckFlag == true && thisVal != "") {
					let goodsNo = {};
					goodsNo.goodsNo = thisVal;
					goodsCodeList.push(goodsNo);
				}
				if (checkYn != "N" && thisVal != "") {
					let goodsNo = {};
					goodsNo.goodsNo = thisVal;
					checkGoodsCodeList.push(goodsNo);
				}
			});

			fnAjax({
				url : "/admin/goods/regist/goodsCodeExistChk",
				params : {goodsCodeList: checkGoodsCodeList},
				contentType : 'application/json',
				isAction : 'select',
				async: false,
				success : function(data, result) {
					if (data.goodsCodeList != null && data.goodsCodeList.length > 0){
						goodsCheckFlag = false;
					}
				},
				error : function(xhr, status, strError, data, result) {
					fnKendoMessage({
						//message : xhr.responseText
						message : '오류가 발생하였습니다. 관리자에게 문의해 주세요.'
					});
				}
			});

			if (goodsCheckFlag != true) {
				fnKendoMessage({
					message: "이미 등록 된 풀무원샵 상품코드 입니다.",
					ok: function focusValue() {
						$("input[name='goodsCode']:eq(0)").focus();
					}
				});
				return;
			}
		}

		if(fnValidationCheck() == false)
			return;

		var data = $('#inputForm').formSerialize(true);

		if( !data.rtnValid )
			return;

		fnKendoMessage({
			type : "confirm",
			message : "저장하시겠습니까?",
			ok : function() {
				if (viewModel.get('noticeBelow1ImageFileList').length > 0) {																		// 상세 상단공지 이미지 존재시
					var noticeBelow1ImageUploadResultList = fnUploadNoticeBelow1Image();															// 상세 상단공지 이미지 / 결과 return
					viewModel.set('noticeBelow1ImageUploadResultList', noticeBelow1ImageUploadResultList);
					viewModel.set('noticeBelow1ImageFileList', []);																					// 기존 상세 상단공지 이미지 초기화
				}

				if (viewModel.get('noticeBelow2ImageFileList').length > 0) {																		// 상세 하단공지 이미지 존재시
					var noticeBelow2ImageUploadResultList = fnUploadNoticeBelow2Image();															// 상세 하단공지 이미지 / 결과 return
					viewModel.set('noticeBelow2ImageUploadResultList', noticeBelow2ImageUploadResultList);
					viewModel.set('noticeBelow2ImageFileList', []);																					// 기존 상세 하단공지 이미지 초기화
				}

				//console.log("packageImageFileList", viewModel.packageImageFileList);

				var goodsPackageImageType = $("#goodsPackageImageType").find("input[type=radio]:checked").val();
				var	packageImageOrderList = [];

				if(goodsPackageImageType == "GOODS_PACKAGE_IMG_TP.MIXED"){
					if(viewModel.get('packageImageMixFileList').length > 0){
						var packageImageUploadResultList = fnUploadPackageImage(goodsPackageImageType);
						packageImageOrderList = fnGetImageOrder('#mixPackageImageContainer')				//묶음상품 이미지 순서별 파일명
						viewModel.set('packageImageUploadResultList', packageImageUploadResultList);
					}

					if(viewModel.get('packageImageList').length > 0){										//기존 묶음상품 전용 이미지 파일들이 있다면
						var packageImageList = viewModel.get('packageImageList');

						for (var i = packageImageList.length - 1; i >= 0; i--) {
							viewModel.get("packageImageNameListToDelete").push(packageImageList[i]['imagePath']);	//기존 묶음상품 전용 이미지 파일들을 삭제 리스트에 추가한다.
						}
					}
				}
				else if(goodsPackageImageType == "GOODS_PACKAGE_IMG_TP.PACKAGE_GOODS"){
					if(viewModel.get('packageImageFileList').length > 0){
						var packageImageUploadResultList = fnUploadPackageImage(goodsPackageImageType);
						packageImageOrderList = fnGetImageOrder('#packageImageContainer')										//묶음상품 이미지 순서별 파일명
						viewModel.set('packageImageUploadResultList', packageImageUploadResultList);
					}

					if(viewModel.get('packageImageMixList').length > 0){										//기존 묶음상품 전용 이미지 파일들이 있다면
						var packageImageMixList = viewModel.get('packageImageMixList');

						for (var i = packageImageMixList.length - 1; i >= 0; i--) {
							viewModel.get("packageImageNameListToDelete").push(packageImageMixList[i]['imagePath']);	//기존 묶음상품 전용 이미지 파일들을 삭제 리스트에 추가한다.
						}
					}
				}

				//console.log("viewModel.get('packageImageUploadResultList') : ", viewModel.get('packageImageUploadResultList'));
				//return;

				viewModel.set('packageImageFileList', []); // 기존 이미지 파일 저장 목록(묶음상품 전용) 초기화
				viewModel.set('packageImageMixFileList', []); // 기존 이미지 파일 저장 목록(묶음/개별상품 조합) 초기화

				var assemblePackageGoodsList = null;
				var assembleGiftGoodsList = null;
				var packageSaleType = null;
				var displayCategoryList = ctgryGrid.dataSource.data();
				var mallInMallCategoryList = null;
				if(mallInMallCtgryGridDs){
					mallInMallCategoryList = mallInMallCtgryGrid.dataSource.data();
				}

				if(viewModel.pageMode == "create") {
					assemblePackageGoodsList = dGrid.dataSource.data();	//묶음상품 구성목록 Grid
					assembleGiftGoodsList = eGrid.dataSource.data();	//증정품 구성목록 Grid
					packageSaleType = cGrid.dataSource.at(0).saleType;	//묶음상품 판매가 입력 > 할인유형(최초 묶음상품 기본 판매가 생성시 사용)
				}

				let representativeImageName = null;

				if(viewModel.ilGoodsDetail.goodsPackageImageType == "GOODS_PACKAGE_IMG_TP.PACKAGE_GOODS") {
					representativeImageName = $("#packageImageContainer > .goodsImage").attr('data-id');
				}
				else if(viewModel.ilGoodsDetail.goodsPackageImageType == "GOODS_PACKAGE_IMG_TP.MIXED") {
					representativeImageName = $("#mixPackageImageContainer > .goodsImage").attr('data-id');
				}

				let approvalChkVal = $('input:checkbox[name="approvalCheckbox"]').is(":checked");	//승인요청 처리 여부
				let goodsApprList = [];

				if(approvalChkVal) {
					goodsApprList = $("#apprGrid").data("kendoGrid").dataSource.data()
				}


				var addGoodsParam = {
						ilGoodsId								: ilGoodsId
					,	pageMode								: viewModel.pageMode
					/* 묶음상품 기본설정 시작 */
					,	ilItemCode								: viewModel.ilGoodsDetail.get("ilItemCode")			//기준상품 아이톰 CODE
					,	urWarehouseId							: viewModel.ilGoodsDetail.get("urWarehouseId")		//기준상품 출고처 ID
					,	urSupplierId							: viewModel.ilGoodsDetail.get("urSupplierId")		//기준상품 공급사 ID
					,	goodsType								: viewModel.ilGoodsDetail.get("goodsType")			//상품 유형
					,	mdRecommendYn							: viewModel.ilGoodsDetail.get("mdRecommendYn")		//MD추천 노출여부
					,	packageSaleType							: packageSaleType									//묶음상품 판매가 입력 > 할인유형(최초 묶음상품 기본 판매가 생성시 사용)
					,	assemblePackageGoodsList				: assemblePackageGoodsList							//묶음상품 구성목록 Grid
					,	assembleGiftGoodsList					: assembleGiftGoodsList								//증정품 구성목록 Grid
					,	totalSalePriceGoods						: totalSalePriceGoods								//묶음상품 판매가의 합계(최초 묶음상품 기본 판매가 생성시 사용)
					,	saleRate								: saleRateSumAvg									//묶음상품 구성목록 합계 할인율
					/* 묶음상품 기본설정 끝 */

					/* 기본 정보 시작 */
					,	goodsName								: viewModel.ilGoodsDetail.get("goodsName")														//상품명
					,	packageUnitDisplayYn					: viewModel.ilGoodsDetail.get("packageUnitDisplayYn")											//용량정보 입력 노출여부(Y:노출)
					,	packageUnitDesc							: viewModel.ilGoodsDetail.get("packageUnitDesc")												//용량정보 노출함 텍스트
					,	promotionName							: viewModel.ilGoodsDetail.get("promotionName")													//프로모션 상품명
					,	promotionNameStartDate					: kendo.toString(viewModel.fnGetDateFull("promotionNameStart"), "yyyy-MM-dd HH:mm")				//프로모션 시작일
					,	promotionNameEndDate					: kendo.toString(viewModel.fnGetDateFull("promotionNameEnd"), "yyyy-MM-dd HH:mm")				//프로모션 종료일
					,	goodsDesc								: viewModel.ilGoodsDetail.get("goodsDesc")														//상품설명
					,	searchKeyword							: viewModel.ilGoodsDetail.get("searchKeyword")													//키워드 입력
					,	displayCategoryList						: displayCategoryList//$("#goodsDisplayCategoryGrid").data("kendoGrid").dataSource.data()							//상품 전시 카테고리
					,	mallInMallCategoryList					: mallInMallCategoryList//$("#mallInMallCategoryGrid").data("kendoGrid").dataSource.data()								//상품 몰인몰 카테고리
					/* 기본 정보 끝 */

					/* 판매/전시 시작 */
					,	purchaseTargetType						: viewModel.ilGoodsDetail.get("purchaseTargetType")								//구매 허용 범위
					,	goodsDisplayType						: viewModel.ilGoodsDetail.get("goodsDisplayType")								//판매 허용 범위 (PC/Mobile)
					,	displayYn								: viewModel.ilGoodsDetail.get("displayYn")										//전시 상태
					,	saleStartDate							: kendo.toString(viewModel.fnGetDateFull("saleStart"), "yyyy-MM-dd HH:mm")		//판매 기간 시작일
					,	saleEndDate								: kendo.toString(viewModel.fnGetDateFull("saleEnd"), "yyyy-MM-dd HH:mm")		//판매 기간 종료일
					,	saleStatus								: viewModel.ilGoodsDetail.get("saleStatus")										//판매 상태
					,	goodsOutmallSaleStatus					: viewModel.ilGoodsDetail.get("goodsOutmallSaleStatus")							//외부몰 판매 상태
					/* 판매/전시 끝 */

					/* 가격정보 시작*/
					,	taxYn									: viewModel.ilGoodsDetail.taxYn													//과세구분
					,	goodsDiscountPriorityList				: viewModel.ilGoodsDetail.get("goodsDiscountPriorityList")						//행사/할인 내역 > 우선할인 내역
					,	goodsDiscountPriorityApproList			: viewModel.ilGoodsDetail.get("goodsDiscountPriorityApproList")					//행사/할인 내역 > 우선할인 내역 > 승인 관리자 정보
					,	goodsDiscountPriorityCalcList			: viewModel.ilGoodsDetail.get("goodsDiscountPriorityCalcList")					//행사/할인 내역 > 우선할인 > 가격계산 정보
					,	goodsDiscountImmediateList				: viewModel.ilGoodsDetail.get("goodsDiscountImmediateList")						//행사/할인 내역 > 즉시할인 내역
					,	goodsDiscountImmediateApproList			: viewModel.ilGoodsDetail.get("goodsDiscountImmediateApproList")				//행사/할인 내역 > 즉시할인 내역 > 승인 관리자 정보
					,	goodsDiscountImmediateCalcList			: viewModel.ilGoodsDetail.get("goodsDiscountImmediateCalcList")					//행사/할인 내역 > 즉시할인 > 가격계산 정보
					,	goodsPackagePriceList					: viewModel.ilGoodsDetail.get("goodsPackagePriceList")							//묶음상품 기본 판매가 > List
					,	goodsPackagePriceApproList				: viewModel.ilGoodsDetail.get("goodsPackagePriceApproList")						//묶음상품 기본 판매가 > 승인 관리자 정보
					,	goodsPackageCalcList					: viewModel.ilGoodsDetail.get("goodsPackageCalcList")							//묶음상품 기본 판매가, 행사/할인 내역 > 가격계산 Grid 리스트
					/* 가격정보 끝*/

					/* 임직원 할인 정보 시작 */
					,	goodsPackageDiscountEmployeeList		: viewModel.ilGoodsDetail.get("goodsPackageDiscountEmployeeList")				//임직원 개별할인 정보 내역
					,	goodsPackageDiscountEmployeeApproList	: viewModel.ilGoodsDetail.get("goodsPackageDiscountEmployeeApproList")			//임직원 개별할인 정보 내역 > 승인 관리자 정보
					/* 임직원 할인 정보 끝 */

					/* 판매정보 시작*/
					,	saleType								: viewModel.ilGoodsDetail.get("saleType")										//판매유형
					,	presentYn								: viewModel.ilGoodsDetail.get("presentYn")										//선물하기 허용 여부
					/* 판매정보 끝*/

					/* 배송/발주 정보 시작 */
					,	itemWarehouseShippingTemplateList		: viewModel.ilGoodsDetail.get('itemWarehouseList')								//배송 정책
					/* 배송/발주 정보 끝 */

					/* 혜택/구매 정보 시작*/
					,	goodsAdditionalGoodsMappingList			: viewModel.ilGoodsDetail.get("goodsAdditionalGoodsMappingList")				//추가상품 리스트
					//	증정 행사 정보 추후 추가될 예정
					,	couponUseYn								: viewModel.ilGoodsDetail.get("couponUseYn")									//혜택 설정 > 쿠폰사용허용
					,	limitMinimumCnt							: viewModel.ilGoodsDetail.get("limitMinimumCnt")								//최소구매 수량기준
					,	limitMaximumType						: viewModel.ilGoodsDetail.get("limitMaximumType")								//최대 구매 기준
					,	limitMaximumDuration					: viewModel.ilGoodsDetail.get("limitMaximumDuration")							//최대 구매 기간
					,	limitMaximumCnt							: viewModel.ilGoodsDetail.get("limitMaximumCnt")								//최대 구매 수량
					/* 혜택/구매 정보 끝*/

					/* 추천상품 등록 시작*/
					,	goodsRecommendList						: viewModel.ilGoodsDetail.get("goodsRecommendList")								//추천상품 리스트
					/* 추천상품 등록 끝*/

					/* 상품 이미지 시작 */
					,	goodsPackageImageType					: viewModel.ilGoodsDetail.get("goodsPackageImageType")							//묶음상품 이미지 형식
					,	packageImageUploadResultList			: viewModel.get("packageImageUploadResultList")									//묶음상품 이미지 리스트
					,	representativeImageName 				: representativeImageName														//묶음상품 이미지 첫번째 파일명(대표이미지명)
					,	packageImageOrderList					: packageImageOrderList															//묶음상품 이미지 순서별 파일명
					,	packageImageNameListToDelete			: viewModel.get("packageImageNameListToDelete")									//묶음상품 이미지 삭제할 이미지 파일명
					,	imageSortOrderChanged					: viewModel.get('imageSortOrderChanged')										//묶음상품 이미지 순서 변경 여부

					,	goodsImageSortOrderChanged				: viewModel.get('goodsImageSortOrderChanged')									//상품별 대표이미지 순서 변경 여부
					,	goodsImageOrderList						: fnGetImageOrder('#goodsImageContainer')										//상품별 대표이미지 리스트

					,	goodsPackageVideoAutoplayYn				: viewModel.ilGoodsDetail.get("goodsPackageVideoAutoplayYn") ? "Y" : "N"		//동영상 정보 자동재생 여부
					,	goodsPackageVideoUrl					: viewModel.ilGoodsDetail.get("goodsPackageVideoUrl")							//동영상 정보 URL
					/* 상품 이미지 끝 */

					/* 상품 상세 기본 정보 시작 */
					,	goodsPackageBasicDescYn						: viewModel.ilGoodsDetail.get("goodsPackageBasicDescYn")					//상품 상세 기본 정보 직접등록 여부
					,	goodsPackageBasicDesc						: $('#goodsPackageBasicDesc').data("kendoEditor").value()					//상품 상세 기본 정보
					/* 상품 상세 기본 정보 끝 */

					/* 상품공지 시작*/
					,	noticeBelow1ImageUploadResultList		: viewModel.get("noticeBelow1ImageUploadResultList")								//상세 상단공지 이미지 Upload 정보
					,	noticeBelow1StartDate					: kendo.toString(viewModel.fnGetDateFull("noticeBelow1Start"),"yyyy-MM-dd HH:mm")	//상세 상단공지 시작일
					,	noticeBelow1EndDate						: kendo.toString(viewModel.fnGetDateFull("noticeBelow1End"),"yyyy-MM-dd HH:mm")		//상세 상단공지 종료일

					,	noticeBelow2ImageUploadResultList		: viewModel.get("noticeBelow2ImageUploadResultList")								//상세 하단공지 이미지 Upload 정보
					,	noticeBelow2StartDate					: kendo.toString(viewModel.fnGetDateFull("noticeBelow2Start"),"yyyy-MM-dd HH:mm")	//상세 하단공지 시작일
					,	noticeBelow2EndDate						: kendo.toString(viewModel.fnGetDateFull("noticeBelow2End"),"yyyy-MM-dd HH:mm")	//상세 하단공지 종료일
					/* 상품공지 끝*/

					/* 기타정보 시작*/
					,	goodsMemo								: viewModel.ilGoodsDetail.get("goodsMemo")							//상품 메모
					/* 기타정보 끝*/

					/* 승인관리자 정보 시작 */
					,	goodsApprList							: goodsApprList														//승인관리자 정보
					,	loadDateTime							: loadDateTime														//페이지 Load 시간
					/* 승인관리자 정보 끝 */

					/* 풀무원샵 상품코드 시작 */
					,	goodsCodeList							: goodsCodeList
					/* 풀무원샵 상품코드 종료 */

				}

//				console.log("addGoodsParam", addGoodsParam);
//				return;

				var pageMode = viewModel.get("pageMode");
				var url = pageMode == "create" ? "/admin/goods/regist/addGoods" : "/admin/goods/regist/modifyGoods";
				var modeMessage = pageMode == "create" ? "생성" : "수정";
				var paramDataSort = pageMode == "create" ? "" : "?paramDataSort=MODIFY_DATE";

				fnAjax({
					url : url,
					params : addGoodsParam,
					contentType : 'application/json',
					isAction : 'insert',
					success : function(data) {

						fnKendoMessage({
							message : '상품명 ' + data["goodsName"] + ' (이)가 '+modeMessage+'되었습니다.',
							ok : function() {
								//var successUrl = "/layout.html#/goods"+paramDataSort;
								//var successUrl = "/layout.html#/goods?paramIlGoodsId="+data['ilGoodsId'];
								//location.href = successUrl;
								if(pageMode == "create") {
									var successUrl = "/layout.html#/goodsPackage?ilGoodsId="+data['ilGoodsId'];
									location.href = successUrl;
//									window.history.replaceState(null, '', successUrl);
//									window.location.reload(true);
								}
								else{
									window.location.reload(true);
								}
							}
						});
					},
					error : function(xhr, status, strError) {
						fnKendoMessage({
							//message : xhr.responseText
							message : '오류가 발생하였습니다. 관리자에게 문의해 주세요.'
						});
					}
					, fail : function(data, code) {
						switch (code.code) {
							case 'LOCAL_DEFINE_INVALID_GOODS_DISCOUNT_PERIOD': //기존에 등록된 할인기간과 겹칩니다.
							case 'LOCAL_DEFINE_INVALID_GOODS_DISCOUNT_START_TIME': //할인 시작일은 현재 이후만 가능합니다.
								fnKendoMessage({
									message : code.message,
									ok : function(e) {
										window.location.reload(true);
									}
								});
							break;

							default :
								fnKendoMessage({
									message : code.message
									, ok : function(e) {
										if(pageMode == "create") {
											var successUrl = "/layout.html#/goodsPackage?ilGoodsId="+data['ilGoodsId'];
											location.href = successUrl;
										}
										else{
											window.location.reload(true);
										}
									}
								});
							break;
						}
					}
				});
			},
			cancel : function() {
				return;
			}
		});
	}

	function fnUploadPackageImage(goodsPackageImageType) { // 상품 이미지 업로드

		var formData = new FormData();
		var packageImageFileList = null;

		if(goodsPackageImageType == "GOODS_PACKAGE_IMG_TP.MIXED"){
			packageImageFileList = viewModel.get('packageImageMixFileList');
		}
		else if(goodsPackageImageType == "GOODS_PACKAGE_IMG_TP.PACKAGE_GOODS"){
			packageImageFileList = viewModel.get('packageImageFileList');
		}


		// 상품 이미지 저장
		for (var i = 0; i < packageImageFileList.length; i++) {
			// goodsImage01, goodsImage02, ... 형식으로 formData 에 이미지 file 객체 append
			formData.append('packageImage' + ('0' + (i + 1)).slice(-2), packageImageFileList[i]);
		}

		formData.append('storageType', 'public'); // storageType 지정
		formData.append('domain', 'il'); // domain 지정

		var packageImageUploadResultList; // 상품 이미지 업로드 결과 목록

		$.ajax({
			url : '/comn/fileUpload',
			data : formData,
			type : 'POST',
			contentType : false,
			processData : false,
			async : false,
			success : function(data) {
				data = data.data;
				packageImageUploadResultList = data['addFile'];
			}
		});

		return packageImageUploadResultList;
	}

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	//가격 정보 > 판매 가격정보 > 변경이력 보기
	function fnGoodsPackagePricePopup(priceKind) {
		if(ilGoodsId != ""){
			let params = {};
			params.ilGoodsId = ilGoodsId;
			params.priceKind = priceKind;

			var GoodsPricePopupName = "판매정보 상세내역";

			fnKendoPopup({
				id			: "goodsPricePopup",
				title		: GoodsPricePopupName, // 해당되는 Title 명 작성
				width		: "1700px",
				height		: "800px",
				scrollable	: "yes",
				src			: "#/goodsPricePopup",
				param		: params,
				success		: function( id, data ){
				}
			});
		}
	}

	//가격정보 > 행사/할인 내역, 묶음상품 기본 할인 > 상세내역 팝업 호출
	function fnGoodsPackageDiscountDetailPopup(discountTypeCode, discountTypeCodeName){
		if(ilGoodsId != ""){
			let params = {};
			params.ilGoodsId = ilGoodsId;
			params.goodsType = viewModel.ilGoodsDetail.goodsType;

			/* HGRM-2325 - dgyoun : 할인유형코드 파라미터 추가 */
			if (discountTypeCode != undefined && discountTypeCode != 'undefined') {
			  params.discountType = discountTypeCode;
			}
			else {
			  params.discountType = '';
			}

			var GoodsPricePopupName = "";
			if(discountTypeCode != "" && discountTypeCode != undefined){
				params.discountTypeCode = discountTypeCode;
				GoodsPricePopupName = discountTypeCodeName + " 정보 내역";
			}
			else{
				params.discountTypeCode = "";
			}

			fnKendoPopup({
				id			: "goodsDiscountPopup",
				title		: GoodsPricePopupName, // 해당되는 Title 명 작성
				width		: "1700px",
				height		: "800px",
				scrollable	: "yes",
				src			: "#/goodsDiscountPopup",
				param		: params,
				success		: function( id, data ){
				}
			});
		}
	}

	//임직원 할인 정보 > 임직원 개별할인 정보 > 변경이력 보기
	function fnGoodsPackageEmployeeDiscountHistoryPopup(historyKind) {
		if(ilGoodsId != ""){
			let params = {};
			params.ilGoodsId = ilGoodsId;
			params.baseRowCount = baseRowCount;		//개별할인 그룹별 기본갯수(묶음 구성 상품 갯수)
			params.historyKind = historyKind;		//개별할인 or 할인 가격정보

			fnKendoPopup({
				id			: "goodsPackageEmployeeDiscountHistoryPopup",
				title		: "임직원 개별 할인 정보 내역", // 해당되는 Title 명 작성
				width		: "1700px",
				height		: "800px",
				scrollable	: "yes",
				src			: "#/goodsPackageEmployeeDiscountHistoryPopup",
				param		: params,
				success		: function( id, data ){
				}
			});
		}
	}

	//가격 정보 > 행사할인내역, 묶음상품 기본 판매가, 임직원 할인 정보 > 임직원 개별할인 정보 설정 팝업
	function fnGoodsPackageDiscountPopup(discountTypeCode, discountTypeCodeName) {
		if(ilGoodsId != ""){
			let params = {};
			let recommendedPrice = 0;
			let standardPrice = 0;
			let standardRowCount = 0;

			if(viewModel.ilGoodsDetail.goodsPrice){
				recommendedPrice = viewModel.ilGoodsDetail.goodsPrice[0].recommendedPrice;
				standardPrice = viewModel.ilGoodsDetail.goodsPrice[0].standardPrice;
			}

			if(viewModel.ilGoodsDetail.goodsPackageBaseDiscountEmployeeList){
				standardRowCount = viewModel.ilGoodsDetail.goodsPackageBaseDiscountEmployeeList[0].rowCount;
			}

			params.goodsPackageInfo = {
				goodsId : ilGoodsId,							// 상품 ID
				goodsType : viewModel.ilGoodsDetail.goodsType,	// 상품 유형
				discountTypeCode : discountTypeCode,			// 우선, 즉시 등 할인 유형 코드
				taxYn : viewModel.ilGoodsDetail.taxYn,			// 과세구분
				recommendedPrice : recommendedPrice,			// 정상가 총액
				standardPrice : standardPrice,					// 원가 총액
				standardRowCount : standardRowCount				// 임직원 기본할인 정보 상품구성 갯수
			};

			var goodsPackageDiscountPopupName = "";
			if(discountTypeCode != "" && discountTypeCode != "undefined"){
				params.discountTypeCode = discountTypeCode;
				goodsPackageDiscountPopupName = discountTypeCodeName + " 등록";
			}

			var goodsDiscountList = [];
			var goodsPackageCalcList = [];
			var goodsPackageBaseDiscountEmployeeList = [];
			var goodsDiscountApprList = [];
			var requestIlGoodsDiscountId = "";
			var url = "goodsPackageDiscountPopup";
			if(discountTypeCode == "GOODS_DISCOUNT_TP.PRIORITY"){
				goodsDiscountList = viewModel.ilGoodsDetail.get('goodsDiscountPriorityList');
				goodsDiscountApprList = viewModel.ilGoodsDetail.get('goodsDiscountPriorityApproList');
				goodsPackageCalcList = viewModel.ilGoodsDetail.get('goodsDiscountPriorityCalcList');
			}
			else if(discountTypeCode == "GOODS_DISCOUNT_TP.IMMEDIATE"){
				goodsDiscountList = viewModel.ilGoodsDetail.get('goodsDiscountImmediateList');
				goodsDiscountApprList = viewModel.ilGoodsDetail.get('goodsDiscountImmediateApproList');
				goodsPackageCalcList = viewModel.ilGoodsDetail.get('goodsDiscountImmediateCalcList');
			}
			else if(discountTypeCode == "GOODS_DISCOUNT_TP.PACKAGE"){
				goodsDiscountList = viewModel.ilGoodsDetail.get('goodsPackagePriceList');
				goodsDiscountApprList = viewModel.ilGoodsDetail.get('goodsPackagePriceApproList');
				goodsPackageCalcList = viewModel.ilGoodsDetail.get('goodsPackageCalcList');
			}
			else if(discountTypeCode == "GOODS_DISCOUNT_TP.EMPLOYEE"){
				goodsPackageBaseDiscountEmployeeList = viewModel.ilGoodsDetail.get('goodsPackageBaseDiscountEmployeeList');
				goodsDiscountList = viewModel.ilGoodsDetail.get('goodsPackageDiscountEmployeeList');
				goodsDiscountApprList = viewModel.ilGoodsDetail.get('goodsPackageDiscountEmployeeApproList');
				url = "goodsPackageEmployeeDiscountPopup";
			}

			params.goodsDiscountList = goodsDiscountList;
			params.goodsDiscountApprList = goodsDiscountApprList;
			params.goodsPackageCalcList = goodsPackageCalcList;
			params.goodsPackageBaseDiscountEmployeeList = goodsPackageBaseDiscountEmployeeList;

			fnKendoPopup({
				id			: url,
				title		: goodsPackageDiscountPopupName, // 해당되는 Title 명 작성
				width		: "1750px",
				height		: "600px",
				scrollable	: "yes",
				src			: "#/"+url,
				param		: params,
				success		: function( id, data ){
					if(data.parameter == undefined){

						if(goodsDiscountList){
							for (var i = goodsDiscountList.length - 1; i >= 0; i--) {
								goodsDiscountList.splice(i, 1);
							}
						}

						var discountMethodTypeCodeName = "";

//						console.log("data.goodsDiscountList : ", data.goodsDiscountList);
//						console.log("goodsPackageBaseDiscountEmployeeList", goodsPackageBaseDiscountEmployeeList);
						let rowCountNum = 1;

						if(data.goodsDiscountList.length > 1){
							rowCountNum = 2;
						}

						if(data.goodsDiscountApprList != null && data.goodsDiscountList.length > 0
							&& data.goodsDiscountApprList.length > 0 && data.goodsDiscountApprList.length > 0){	//할인 승인 관리자 정보 저장
							if(discountTypeCode == "GOODS_DISCOUNT_TP.PRIORITY"){
								viewModel.ilGoodsDetail.set('goodsDiscountPriorityApproList', data.goodsDiscountApprList);
								viewModel.ilGoodsDetail.set("goodsDiscountPriorityCalcList", data.goodsPackageCalcList);
							}
							else if(discountTypeCode == "GOODS_DISCOUNT_TP.IMMEDIATE"){
								viewModel.ilGoodsDetail.set('goodsDiscountImmediateApproList', data.goodsDiscountApprList);
								viewModel.ilGoodsDetail.set("goodsDiscountImmediateCalcList", data.goodsPackageCalcList);
							}
							else if(discountTypeCode == "GOODS_DISCOUNT_TP.PACKAGE"){
								viewModel.ilGoodsDetail.set('goodsPackagePriceApproList', data.goodsDiscountApprList);
								viewModel.ilGoodsDetail.set("goodsPackageCalcList", data.goodsPackageCalcList);
							}
							else if(discountTypeCode == "GOODS_DISCOUNT_TP.EMPLOYEE"){
								viewModel.ilGoodsDetail.set('goodsPackageDiscountEmployeeApproList', data.goodsDiscountApprList);
							}
						}
						else {
							if(discountTypeCode == "GOODS_DISCOUNT_TP.PRIORITY"){
								viewModel.ilGoodsDetail.set('goodsDiscountPriorityApproList', []);
								viewModel.ilGoodsDetail.set("goodsDiscountPriorityCalcList", []);
							}
							else if(discountTypeCode == "GOODS_DISCOUNT_TP.IMMEDIATE"){
								viewModel.ilGoodsDetail.set('goodsDiscountImmediateApproList', []);
								viewModel.ilGoodsDetail.set("goodsDiscountImmediateCalcList", []);
							}
							else if(discountTypeCode == "GOODS_DISCOUNT_TP.PACKAGE"){
								viewModel.ilGoodsDetail.set('goodsPackagePriceApproList', []);
								viewModel.ilGoodsDetail.set("goodsPackageCalcList", []);
							}
							else if(discountTypeCode == "GOODS_DISCOUNT_TP.EMPLOYEE"){
								viewModel.ilGoodsDetail.set('goodsPackageDiscountEmployeeApproList', []);
							}
						}

						if(discountTypeCode == "GOODS_DISCOUNT_TP.PRIORITY"){
							if(data.goodsDiscountList.length > 0) {
								viewModel.ilGoodsDetail.set("isGoodsDiscountPriorityListNoDataTbody", false);						//가격 정보 > 행사/할인 내역 > 우선할인 > Nodata Visible
								viewModel.ilGoodsDetail.set("isGoodsDiscountPriorityListTbody", true);								//가격 정보 > 행사/할인 내역 > 우선할인 > Data Visible
							}
							else{
								viewModel.ilGoodsDetail.set("isGoodsDiscountPriorityListNoDataTbody", true);						//가격 정보 > 행사/할인 내역 > 우선할인 > Nodata Visible
								viewModel.ilGoodsDetail.set("isGoodsDiscountPriorityListTbody", false);								//가격 정보 > 행사/할인 내역 > 우선할인 > Data Visible
							}
						}
						else if(discountTypeCode == "GOODS_DISCOUNT_TP.IMMEDIATE"){
							if(data.goodsDiscountList.length > 0) {
								viewModel.ilGoodsDetail.set("isGoodsDiscountImmediateListNoDataTbody", false);						//가격 정보 > 행사/할인 내역 > 즉시할인 > Nodata Visible
								viewModel.ilGoodsDetail.set("isGoodsDiscountImmediateListTbody", true);								//가격 정보 > 행사/할인 내역 > 즉시할인 > Data Visible
							}
							else {
								viewModel.ilGoodsDetail.set("isGoodsDiscountImmediateListNoDataTbody", true);						//가격 정보 > 행사/할인 내역 > 즉시할인 > Nodata Visible
								viewModel.ilGoodsDetail.set("isGoodsDiscountImmediateListTbody", false);							//가격 정보 > 행사/할인 내역 > 즉시할인 > Data Visible
							}
						}
						else if(discountTypeCode == "GOODS_DISCOUNT_TP.EMPLOYEE"){
							if(data.goodsDiscountList.length > 0) {
								viewModel.ilGoodsDetail.set("goodsPackageDiscountEmployeeListNoDataVisible", false);				//임직원 할인 정보 > 임직원 개별할인 정보 > Nodata Visible
								viewModel.ilGoodsDetail.set("goodsPackageDiscountEmployeeListDataVisible", true);					//임직원 할인 정보 > 임직원 개별할인 정보 > Data Visible
							}
							else {
								viewModel.ilGoodsDetail.set("goodsPackageDiscountEmployeeListNoDataVisible", true);					//임직원 할인 정보 > 임직원 개별할인 정보 > Nodata Visible
								viewModel.ilGoodsDetail.set("goodsPackageDiscountEmployeeListDataVisible", false);					//임직원 할인 정보 > 임직원 개별할인 정보 > Data Visible
							}
						}
						//묶음상품 기본 판매가의 경우에는 저장시에 무조건 하나의 ROW가 생성되므로 NoDataTbody 항목을 보여줄 필요가 없음.

						if(data.goodsDiscountList.length > 0 ){
							data.goodsDiscountList.forEach(function(element, index, array){
								if(element.discountMethodTypeCode == "GOODS_DISCOUNT_METHOD_TP.FIXED_PRICE"){
									discountMethodTypeCodeName = "고정가할인";
								}
								else if(element.discountMethodTypeCode == "GOODS_DISCOUNT_METHOD_TP.FIXED_RATE"){
									discountMethodTypeCodeName = "정률할인";
								}

								var trListVisible = true;
								let rowNum = index+1;
								let displayAllow = ";";				//rowspan에 따른 구분, 시작일, 종료일 display 여부

								if(rowNum > 1){
									displayAllow = "none;";
								}

								if(discountTypeCode == "GOODS_DISCOUNT_TP.EMPLOYEE") { //임직원 개별할인 정보라면
									if(index > ((standardRowCount+1) * 2) - 1){	//((기본 구성품 갯수 + 합계 행) * 2) -1
										trListVisible = false;
									}
								}
								else {
									if(index > 1){
										trListVisible = false;
									}
								}

								var approvalStatusCodeName = "승인요청";
								if(element.approvalStatusCodeName != null) {
									approvalStatusCodeName = element.approvalStatusCodeName;
								}

								let apprReqInfo = PG_SESSION.loginName + "(" + PG_SESSION.loginId + ")";
								let apprReqNm = PG_SESSION.loginName;
								let apprReqUserId = PG_SESSION.userId;
								let apprReqUserLoginId = PG_SESSION.loginId;
								let apprInfo = null;

								let apprSubNm = null;
								let apprSubUserId = null;
								let apprSubUserLoginId = null;
								let apprNm = null;
								let apprUserId = null;
								let apprUserLoginId = null;

								if(data.goodsDiscountApprList != null && data.goodsDiscountApprList.length > 1){
									for(var i=0; i < data.goodsDiscountApprList.length; i++){
										if(i == 0){
											apprInfo = data.goodsDiscountApprList[i].apprUserName + "(" + data.goodsDiscountApprList[i].apprLoginId + ")";
											apprSubNm = data.goodsDiscountApprList[i].apprUserName;
											apprSubUserId = data.goodsDiscountApprList[i].apprUserId;
											apprSubUserLoginId = data.goodsDiscountApprList[i].apprLoginId;
										}
										if(i == 1){
											apprInfo = apprInfo + "</BR>" + data.goodsDiscountApprList[i].apprUserName + "(" + data.goodsDiscountApprList[i].apprLoginId + ")";
											apprNm = data.goodsDiscountApprList[i].apprUserName;
											apprUserId = data.goodsDiscountApprList[i].apprUserId;
											apprUserLoginId = data.goodsDiscountApprList[i].apprLoginId;
										}
									}
								}

								if(data.goodsDiscountApprList != null && data.goodsDiscountApprList.length == 1){
									for(var i=0; i < data.goodsDiscountApprList.length; i++){
										apprInfo = data.goodsDiscountApprList[i].apprUserName + "(" + data.goodsDiscountApprList[i].apprLoginId + ")";
										apprNm = data.goodsDiscountApprList[i].apprUserName;
										apprUserId = data.goodsDiscountApprList[i].apprUserId;
										apprUserLoginId = data.goodsDiscountApprList[i].apprLoginId;
									}
								}

								if(element.approvalStatusCode != "APPR_STAT.NONE"){
									apprReqInfo = element.apprReqInfo;
									apprReqNm = element.apprReqNm;
									apprReqUserId = element.apprReqUserId;
									apprReqUserLoginId = element.apprReqUserLoginId;
									apprInfo = element.apprInfo;
									apprSubNm = element.apprSubNm;
									apprSubUserId = element.apprSubUserId;
									apprSubUserLoginId = element.apprSubUserLoginId;
									apprNm = element.apprNm;
									apprUserId = element.apprUserId;
									apprUserLoginId = element.apprUserLoginId;
								}

								if(discountTypeCode == "GOODS_DISCOUNT_TP.EMPLOYEE") {
									goodsDiscountList.push({
										trListVisible					: trListVisible,
										goodsId							: ilGoodsId,
										rowNum							: element.rowNum,
										rowGroupNum						: element.rowGroupNum,
										rowCount						: element.rowCount,
										ilGoodsPackageGoodsMappingId	: element.ilGoodsPackageGoodsMappingId,
										regTypeName						: "시스템",
										goodsDiscountApprId				: element.goodsDiscountApprId,
										goodsDiscountId					: element.goodsDiscountId,
										apprReqInfo						: apprReqInfo,
										apprReqNm						: apprReqNm,
										apprReqUserId					: apprReqUserId,
										apprReqUserLoginId				: apprReqUserLoginId,
										apprInfo						: apprInfo,
										apprSubNm						: apprSubNm,
										apprSubUserId					: apprSubUserId,
										apprSubUserLoginId				: apprSubUserLoginId,
										apprNm							: apprNm,
										apprUserId						: apprUserId,
										apprUserLoginId					: apprUserLoginId,
										discountTypeCode				: discountTypeCode,
										discountTypeCodeName			: "개별",
										approvalStatusCode				: element.approvalStatusCode,
										approvalStatusCodeName			: approvalStatusCodeName,
										discountStartDateTime			: element.discountStartDateTime,
										discountStartDate				: element.discountStartDate,
										discountStartHour				: element.discountStartHour,
										discountStartMinute				: element.discountStartMinute,

										discountEndDateTime				: element.discountEndDateTime,
										discountEndDate					: element.discountEndDate,
										discountEndDateOriginal			: element.discountEndDate,
										discountEndHour					: element.discountEndHour,
										discountEndHourOriginal			: element.discountEndHour,
										discountEndMinute				: element.discountEndMinute,
										discountEndMinuteOriginal		: element.discountEndMinute,

										discountMethodTypeCode			: element.discountMethodTypeCode,
										discountMethodTypeCodeName		: discountMethodTypeCodeName,

										goodsName						: element.goodsName,
										goodsQuantity					: element.goodsQuantity,

										recommendedPrice				: element.recommendedPrice,
										recommendedTotalPrice			: element.recommendedTotalPrice,
										salePrice						: element.salePrice,
										standardPrice					: element.standardPrice,
										discountAmount					: element.discountAmount,
										marginRate						: element.marginRate,
										discountRatio					: kendo.parseFloat(element.discountRatio),
										discountSalePrice				: element.discountSalePrice
									});
								}
								else {
									goodsDiscountList.push({
										trListVisible				: trListVisible,
										rowNum						: rowNum,
										displayAllow				: displayAllow,
										rowCountNum					: rowCountNum,
										goodsId						: ilGoodsId,
										regTypeName					: "시스템",
										goodsDiscountApprId			: element.goodsDiscountApprId,
										goodsDiscountId				: element.goodsDiscountId,
										apprReqInfo					: apprReqInfo,
										apprReqNm					: apprReqNm,
										apprReqUserId				: apprReqUserId,
										apprReqUserLoginId			: apprReqUserLoginId,
										apprInfo					: apprInfo,
										apprSubNm					: apprSubNm,
										apprSubUserId				: apprSubUserId,
										apprSubUserLoginId			: apprSubUserLoginId,
										apprNm						: apprNm,
										apprUserId					: apprUserId,
										apprUserLoginId				: apprUserLoginId,
										discountTypeCode			: discountTypeCode,
										discountTypeCodeName		: discountTypeCodeName,
										approvalStatusCode			: element.approvalStatusCode,
										approvalStatusCodeName		: approvalStatusCodeName,
										discountStartDateTime		: element.discountStartDateTime,
										discountStartDate			: element.discountStartDate,
										discountStartHour			: element.discountStartHour,
										discountStartMinute			: element.discountStartMinute,

										discountEndDateTime			: element.discountEndDateTime,
										discountEndDate				: element.discountEndDate,
										discountEndDateOriginal		: element.discountEndDate,
										discountEndHour				: element.discountEndHour,
										discountEndHourOriginal		: element.discountEndHour,
										discountEndMinute			: element.discountEndMinute,
										discountEndMinuteOriginal	: element.discountEndMinute,

										discountMethodTypeCode		: element.discountMethodTypeCode,
										discountMethodTypeCodeName	: discountMethodTypeCodeName,

										recommendedPrice			: element.recommendedPrice,
										salePrice					: element.salePrice,
										standardPrice				: element.standardPrice,
										discountAmount				: element.discountAmount,
										marginRate					: element.marginRate,
										approveName					: "나승인",
										discountRatio				: kendo.parseFloat(element.discountRatio),
										discountSalePrice			: element.discountSalePrice
									});
								}
							});
						}

						//모든 할인 설정 이후에 판매 가격 정보 Refresh 처리
						fnGoodsPriceRefresh();
					}
				}
			});
		}
	}
	//--------------------------------- Etc Function End-------------------------------

	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		// ##### 기본설정 #####
		fnInitAgrid();  // 묶음상품선택 그리드
		fnInitBgrid();  // 증정품선택 그리드
		fnInitCgrid();  // 묶음상품 판매가 입력 그리드
		fnInitDgrid();  // 묶음상품 구성목록 그리드
		fnInitEgrid();  // 증정품 구성목록 그리드
	}

	// 기본설정 > 묶음상품 선택 그리드
	function fnInitAgrid(){
		aGridDs = fnGetEditDataSource({
			//url : '',
			model_id : 'ilGoodsId'
//			model_fields : {
//				ilGoodsId :			{ editable : false, type : 'number' },
//				goodsBase :			{ editable : false, type : 'string' },
//				itemCodeBarcode :	{ editable : false, type : 'string' },
//				goodsTypeName :		{ editable : false, type : 'string' },
//				goodsName :			{ editable : false, type : 'string' },
//				taxYnName :			{ editable : false, type : 'string' },
//				purchaseQuanity :	{ editable : false, type : 'number' }
//			}
		});
		// 상품 추가/삭제/수정 시에 발생하는 모든 작업을 처리
		aGridDs.bind("change", function(e) {
			if (e.action == "add" || e.action == "remove" || e.action == "itemchange") {
				var dataRow = e.items[0];

				if (e.action == "remove") {
					removeGoodsInfo(dataRow.ilGoodsId);  // 상품 관련 정보 삭제

					var goodsImageList = viewModel.get("goodsImageList");

					for ( var i in goodsImageList) {  // 상품 이미지 삭제
						if (goodsImageList[i].goodsId == dataRow.ilGoodsId)
							goodsImageList.splice(i, 1);
					}
				} else {
					// 묶음상품선택 그리드(aGrid) 의 add, itemChange 된 레코드의 정상가 총액 계산 = 정상가(개당) * 구성수량
					dataRow.recommendedTotalPrice = dataRow.recommendedPrice * dataRow.purchaseQuanity;

					if (e.action == "add") {  // 추가일 경우 마진율 계산

/*
						if (dataRow.taxYn == "Y")  // 과세일 경우 계산 = (정상가 - 원가 * 1.1) / 정상가 * 100
							dataRow.marginRate = Math.round( (dataRow.recommendedPrice - dataRow.standardPrice * 1.1) / dataRow.recommendedPrice * 100 );
						else  // 면세일 경우 계산 = (정상가 - 원가) / 정상가 * 100
							dataRow.marginRate = Math.round( (dataRow.recommendedPrice - dataRow.standardPrice) / dataRow.recommendedPrice * 100 );
*/
						if (dataRow.taxYn == "Y") { // 과세일 경우 계산 = (정상가 - 원가 * 1.1) / 정상가 * 100
							dataRow.marginRate = Math.floor( (dataRow.recommendedPrice - dataRow.standardPrice * 1.1) / dataRow.recommendedPrice * 100 );
						}
						else { // 면세일 경우 계산 = (정상가 - 원가) / 정상가 * 100
							dataRow.marginRate = Math.floor( (dataRow.recommendedPrice - dataRow.standardPrice) / dataRow.recommendedPrice * 100 );
						}
						dataRow.origMarginRate = dataRow.marginRate;
					}
				}

				calculateGoodsPriceRate();  // 묶음상품 판매가 입력 그리드(cGgrid) 에 가격총액 계산학고 할인율,할인액,묶음상품판매가, 마진율 게산하고 묶은상품 구성목록 그리드(dGrid) 초기화
			}
		});

		aGridOpt = {
			dataSource: aGridDs
			,autoBind:false
			,navigatable: true
			,scrollable: false
			,selectable: false
			//,height:280
			,columns   : [
				 { field:'goodsBase'				,title : '상품구분'					, width:'50px'	,attributes:{ style:'text-align:center' }}
				,{ field:'itemCodeBarcode'			,title : '마스터품목코드<br>품목바코드'		, width:'110px'	,attributes:{ style:'text-align:center' }
													, template: "#: ilItemCode # <br> #: itemBarcode #" }
				,{ field:'ilGoodsId'				,title : '상품코드'					, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'goodsTypeName'			,title : '상품유형'					, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'goodsName'				,title : '상품명'						, width:'200px'	,attributes:{ style:'text-align:left' }
													,template: '<table style="border:none;"><colgroup><col style="width: 30%"><col style="width: 70%"></colgroup><tr><td style="border:none;"><img src="#: publicStorageUrl + size75Image #" style="vertical-align:middle;"></td><td style="border:none;">#: goodsName #</td></tr></table>' }
				,{ field:'taxYn'					,title : '과세코드'					, hidden:true}
				,{ field:'taxYnName'				,title : '과세구분'					, width:'60px'	,attributes:{ style:'text-align:center' }}
				,{ field:'recommendedPrice'			,title : '정상가<br>(개당)'				, width:'80px'	,attributes:{ style:'text-align:right' } ,format: "{0:n0}"}
				,{ field:'salePrice'				,title : '판매가<br>(개당)'				, width:'80px'	,attributes:{ style:'text-align:right' } ,format: "{0:n0}"}
				,{ field:'purchaseQuanity'			,title : '구성수량'					, width:'64px'	,attributes:{ style:'text-align:center' }, template: "<input name='purchaseQuanity' data-bind='value: purchaseQuanity' style='width:50pt'>"}
				,{ field:'recommendedTotalPrice'	,title : '정상가총액'					, width:'80px'	,attributes:{ style:'text-align:right' } ,format: "{0:n0}"}
//				,{ field:'marginRate'				,title : '마진율<br>(정상가 기준)'		, width:'100px'	,attributes:{ style:'text-align:center' } ,format: "{0:n0} %", hidden:true}
				,{ field:'origMarginRate'			,title : '마진율<br>(정상가 기준)'		, width:'100px'	,attributes:{ style:'text-align:center' } ,format: "{0:n0} %"}
				,{ command: [{ text: "삭제", visible: function(dataItem) { return !dataItem.goodsBase }, click: fnDelAgridRow }]
				,title : '관리'						, width:'80px' ,attributes:{ style:'text-align:center', class:'forbiz-cell-readonly' }}
			]
			,noRecordMsg: "-"
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		aGrid.bind("dataBound", function(){
			aGrid.tbody.find('tr').each(function () {
				// 구성수량
				$(this).find("input[name=purchaseQuanity]").kendoDropDownList({
					  dataSource: amount,
					  change: function(e) {
						  var tdElement = $(e.sender.element).closest("td");

						  tdElement.prepend('<span class="k-dirty"></span>');
						  tdElement.addClass("k-dirty-cell");
					  }
				});

			  var model =  aGrid.dataItem(this);
			  kendo.bind(this,model);
			});
		});

		function fnDelAgridRow(e) {
			e.preventDefault();
			e.stopPropagation();

			var model = aGrid.dataItem($(e.currentTarget).closest("tr"));

			fnKendoMessage({message : '삭제하시겠습니까?', type : "confirm", ok : function(){
				var trDom = $(e.currentTarget).closest("tr");

				var dataItem = aGrid.dataItem(trDom);

				aGrid.removeRow(trDom);

				var indivisualGoodsDatas = aGrid.dataSource.data().slice();		// 묶음상품 선택 그리드의 데이타
				fnMallInMallCtgryDisplayAllow(indivisualGoodsDatas);			//몰인몰 카테고리 노출 여부
				fnCheckAndSetPresentYn();
			}});
		}
	}

	// 기본설정 > 증정품선택 그리드
	function fnInitBgrid(){
		bGridDs = fnGetEditDataSource({
			model_id : 'ilGoodsId'
		});

		bGridDs.bind("change", function(e) {
			var dataRow = e.items[0];

			if (e.action == "add" || e.action == "remove" || e.action == "itemchange") {
				// 묶음상품선택 그리드(aGrid) 의 add, itemChange 된 레코드의 정상가 총액 계산 = 정상가(개당) * 구성수량
				dataRow.recommendedTotalPrice = dataRow.recommendedPrice * dataRow.purchaseQuanity;

				if (e.action == "add") {  // 추가일 경우 마진율 계산
					if (dataRow.taxYn == "Y")  // 과세일 경우 계산 = (정상가 - 원가 * 1.1) / 정상가 * 100
						dataRow.marginRate = Math.floor( (dataRow.recommendedPrice - dataRow.standardPrice * 1.1) / dataRow.recommendedPrice * 100 );
					else  // 면세일 경우 계산 = (정상가 - 원가) / 정상가 * 100
						dataRow.marginRate = Math.floor( (dataRow.recommendedPrice - dataRow.standardPrice) / dataRow.recommendedPrice * 100 );
				}

				calculateGoodsPriceRate();  // 묶음상품 판매가 입력 그리드(cGgrid) 에 가격총액 계산학고 할인율,할인액,묶음상품판매가, 마진율 게산하고 묶은상품 구성목록 그리드(dGrid) 초기화
			}
		});

		bGridOpt = {
			dataSource: bGridDs
			,autoBind:false
			,navigatable: true
			,scrollable: false
			,selectable: false
			,columns   : [
				{ field:'itemCodeBarcode'			,title : '마스터품목코드<br>품목바코드'		, width:'110px'	,attributes:{ style:'text-align:center' }
													, template: "#: ilItemCode # <br> #: itemBarcode #" }
				,{ field:'ilGoodsId'				,title : '상품코드'					, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'goodsTypeName'			,title : '상품유형'					, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'goodsName'				,title : '상품명'						, width:'200px'	,attributes:{ style:'text-align:left' }
													,template: '<img src="#: publicStorageUrl + size75Image #" style="vertical-align:middle; padding-right:12px">#: goodsName #' }
				,{ field:'taxYnName'				,title : '과세구분'					, width:'60px'	,attributes:{ style:'text-align:center' }}
				,{ field:'recommendedPrice'			,title : '정상가<br>(개당)'				, width:'80px'	,attributes:{ style:'text-align:right' } ,format: "{0:n0}"}
				,{ field:'salePrice'				,title : '판매가<br>(개당)'				, width:'80px'	,attributes:{ style:'text-align:right' } ,format: "{0:n0}"}
				,{ field:'purchaseQuanity'			,title : '구성수량'					, width:'64px'	,attributes:{ style:'text-align:center' }, template: "<input name='purchaseQuanity' data-bind='value: purchaseQuanity' style='width:50pt'>"}
				,{ field:'recommendedTotalPrice'	,title : '정상가총액'					, width:'80px'	,attributes:{ style:'text-align:right' } ,format: "{0:n0}"}
				,{ field:'marginRate'				,title : '마진율<br>(정상가 기준)'		, width:'100px'	,attributes:{ style:'text-align:center' } ,format: "{0:n0} %"}
				,{ command: [{ text: "삭제", visible: function(dataItem) { return !dataItem.goodsBase }, click: fnDelBgridRow }]
				,title : '관리'						, width:'80px' ,attributes:{ style:'text-align:center', class:'forbiz-cell-readonly' }}
			]
			,noRecordMsg: "-"
		};
		bGrid = $('#bGrid').initializeKendoGrid( bGridOpt ).cKendoGrid();

		bGrid.bind("dataBound", function(){
			bGrid.tbody.find('tr').each(function () {
				// 구성수량
				$(this).find("input[name=purchaseQuanity]").kendoDropDownList({
					dataSource: amount,
					change: function(e) {
						var tdElement = $(e.sender.element).closest("td");
						tdElement.prepend('<span class="k-dirty"></span>');
						tdElement.addClass("k-dirty-cell");
					}
				});
				var model =  bGrid.dataItem(this);
				kendo.bind(this,model);
			});
		});

		function fnDelBgridRow(e) {
			e.preventDefault();
			e.stopPropagation();

			var model = bGrid.dataItem($(e.currentTarget).closest("tr"));

			fnKendoMessage({message : '삭제하시겠습니까?', type : "confirm", ok : function(){
				var trDom = $(e.currentTarget).closest("tr");
				var dataItem = bGrid.dataItem(trDom);

				bGrid.removeRow(trDom);
				//eGridDs.remove(dataItem);
			}});
		}
	}

	// 기본설정 > 묶음상품 판매가 입력 그리드
	function fnInitCgrid(){
		cGridOpt = {
			 pageable  : false
			,navigatable: true
			,scrollable: false
			//,height:280
			,selectable: false
			,columns   : [
				 { field:'saleType'						,title : '할인유형'			, width:'80px'	,attributes:{ style:'text-align:center'} ,template: "<input id='saleType' name='saleType' data-bind='value: saleType'>"}
				,{ field:'recommendedPackageTotalPrice'	,title : '정상가 총액'			, width:'80px'	,attributes:{ style:'text-align:right' } ,format: "{0:n0}"}
				,{ field:'saleTotalPrice'				,title : '판매가 총액'			, width:'80px'	,attributes:{ style:'text-align:right' } ,format: "{0:n0}"}
				,{ field:'saleRate'						,title : '할인율 계산'			, width:'80px'	,attributes:{ style:'text-align:center' } ,template: "<input id='saleRate' name='saleRate' maxlength='2' data-bind='value: saleRate' disabled> %"}
				,{ field:'discountPrice'				,title : '할인액'				, width:'80px'	,attributes:{ style:'text-align:right' } ,format: "{0:n0}"}
				,{ field:'goodsPackageSalePrice'		,title : '묶음상품 예상 판매가'	, width:'80px'	,attributes:{ style:'text-align:center' } ,template: "<input id='goodsPackageSalePrice' name='goodsPackageSalePrice' data-bind='value: goodsPackageSalePrice' text-align='right'>"}
				,{ field:'marginRate'					,title : '예상 마진율'			, width:'80px'	,attributes:{ style:'text-align:center' } ,format: "{0:n0} %"}
			]
			,noRecordMsg: "-"
		};
		cGrid = $('#cGrid').initializeKendoGrid( cGridOpt ).cKendoGrid();

		cGrid.bind("dataBound", function(){
			cGrid.tbody.find('tr').each(function () {
				$("#saleType").kendoDropDownList({
					dataSource: [
						{"CODE":"GOODS_DISCOUNT_METHOD_TP.FIXED_PRICE"	,"NAME":"고정가할인"},
						{"CODE":"GOODS_DISCOUNT_METHOD_TP.FIXED_RATE"	,"NAME":"정률할인"}
					],
					dataTextField: "NAME",
					dataValueField: "CODE",
					change: function(e) {
						convertSaleType(this.value());
					}
				});

				$("#saleType").data("kendoDropDownList").enable(false);

				// 할인율 input
				$("#saleRate").kendoNumericTextBox({
					format: "n0"
					, min: 0
					, max: 99
					, spinners: false
					, change: function(e) {
						if ( aGridDs.data().length == 1 && Number(aGrid.dataSource.at(0).get("purchaseQuanity")) < 2 ) {
							setTimeout(function() {
								fnKendoMessage({
									message : '상품을 먼저 선택해 주십시오.',
									ok : function(e) {
										cGrid.dataSource.at(0).set("saleRate", "");
									}
								});
							});
							return true;
						}
						var baseTaxYn = aGrid.dataSource.at(0).taxYn;														//기준 상품의 과세구분
						var marginRate = 0																					//마진율
						var dataItem = cGrid.dataSource.at(0);

						var recommendedPackageTotalPrice = cGrid.dataSource.at(0).recommendedPackageTotalPrice * 1;			// 정상가 총액
						var standardPackageTotalPrice = cGrid.dataSource.at(0).standardTotalPrice;							// 총액
						var inputSaleRate = this.value() * 1;																// 입력한 할인율


						// 할인액 계산 시작 - 각 상품의 낱개별 할인 기준으로 한다.
						var aGridDatas = aGrid.dataSource.data().slice();													// 묶음상품 선택 그리드의 데이타
						var resultDiscountPrice = 0;
						for (var i = 0; i < aGridDatas.length; i++){
							var discountPrice = aGridDatas[i].recommendedPrice * inputSaleRate / 100;						// 개별 할인액 계산
							var restDiscountPrice = discountPrice%10;														// 원단위 계산
							discountPrice = parseInt(discountPrice/10)*10;													// 개별 할인액 원단위 절사
							if (restDiscountPrice > 0)																		// 절사된 원단위 할인이 있으면 10원 올림 처리
								discountPrice += 10;

							resultDiscountPrice += discountPrice * aGridDatas[i].purchaseQuanity;							// 개별 할인액 * 수량
						}
						var resultGoodsPackageSalePrice = recommendedPackageTotalPrice - resultDiscountPrice;				// 묶음상품 판매가 계산
						// 할인액 계산 종료

/*
						var resultGoodsPackageSalePrice = recommendedPackageTotalPrice - resultDiscountPrice;				// 묶음상품 판매가 계산

//						console.log("recommendedPackageTotalPrice", recommendedPackageTotalPrice);
//						console.log("resultDiscountPrice", resultDiscountPrice);

						var goodsPackageTextSalePrice = "" + resultGoodsPackageSalePrice;									// 계산된 묶음상품 판매가를 문자열로 데이타로 전환
						var priceWonDigitValue = goodsPackageTextSalePrice.charAt(goodsPackageTextSalePrice.length - 1);	// 묶음상품 판매가 원단위 값

						if (priceWonDigitValue != '0') {	// 묶음상품 판매가가 원단위로 산출될 경우 원단위 절사
							resultGoodsPackageSalePrice = resultGoodsPackageSalePrice - priceWonDigitValue * 1;				// 묶음상품 판매가 원단위 절사
							resultDiscountPrice = recommendedPackageTotalPrice - resultGoodsPackageSalePrice;				// 할인액 다시 계산
						}
*/

//						console.log("resultGoodsPackageSalePrice", resultGoodsPackageSalePrice);
//						console.log("resultDiscountPrice", resultDiscountPrice);

/*
						if(baseTaxYn == "Y"){	// 과세일 경우 계산 = (정상가 - 묶음상품 판매가 * 1.1) / 정상가 * 100
							marginRate = Math.round( (recommendedPackageTotalPrice - resultGoodsPackageSalePrice * 1.1) / recommendedPackageTotalPrice * 100 );
						}
						else{	// 면세일 경우 계산 = (정상가 - 묶음상품 판매가 / 정상가 * 100
							marginRate = Math.round( (recommendedPackageTotalPrice - resultGoodsPackageSalePrice) / recommendedPackageTotalPrice * 100 );
						}
*/
						if(baseTaxYn == "Y"){	// 과세일 경우 계산 = (묶음상품 판매가 - 원가총액 * 1.1) / 묶음상품 판매가 * 100
							marginRate = Math.floor( (resultGoodsPackageSalePrice - standardPackageTotalPrice * 1.1) / resultGoodsPackageSalePrice * 100 );
						}
						else{	// 면세일 경우 계산 = (묶음상품 판매가 - 원가총액 ) / 묶음상품 판매가 * 100
							marginRate = Math.floor( (resultGoodsPackageSalePrice - standardPackageTotalPrice) / resultGoodsPackageSalePrice * 100 );
						}

						dataItem.set("discountPrice", resultDiscountPrice);													// 할인액 설정
						dataItem.set("goodsPackageSalePrice", resultGoodsPackageSalePrice);									// 묶음상품 판매가 설정
						dataItem.set("marginRate", marginRate);																// 마진율 설정

						$(this.element.context).blur();
					}
				}).enable(false);
				$("#saleRate").closest(".k-widget.k-numerictextbox").css("background-color", "#c0c0c0");

				// 묶음 상품 판매가 input
				$("#goodsPackageSalePrice").kendoNumericTextBox({
					format: "n0"
					, spinners: false
					, change: function(e) {
						if ( aGridDs.data().length == 1 && Number(aGrid.dataSource.at(0).get("purchaseQuanity")) < 2 ) {
							setTimeout(function() {
								fnKendoMessage({
									message : '상품을 먼저 선택해 주십시오.',
									ok : function(e) {
										cGrid.dataSource.at(0).set("goodsPackageSalePrice", "");
									}
								});
							});
							return true;
						}
						var baseTaxYn = aGrid.dataSource.at(0).taxYn;													//기준 상품의 과세구분
						var recommendedPackageTotalPrice = cGrid.dataSource.at(0).recommendedPackageTotalPrice * 1;		// 정상가 총액
						var standardPackageTotalPrice = cGrid.dataSource.at(0).standardTotalPrice;						// 총액
						var goodsPackageInputSalePrice = Math.floor((this.value() * 1)/10)*10;							// 입력한 묶음상품 판매가

						if (goodsPackageInputSalePrice <= 0) {
							setTimeout(function() {
								fnKendoMessage({
									message : '묶음상품 판매가를 입력해주세요 (10원이상)',
									ok : function(e) {
										cGrid.dataSource.at(0).set("goodsPackageSalePrice", "");
										$("#goodsPackageSalePrice").data("kendoNumericTextBox").focus();
									}
								});
							});
							return;
						} else if (goodsPackageInputSalePrice > recommendedPackageTotalPrice) {
							setTimeout(function() {
								fnKendoMessage({
									message : '정상가 총액을 초과하는 금액을 입력할 수 없습니다.',
									ok : function(e) {
										cGrid.dataSource.at(0).set("goodsPackageSalePrice", "");
										$("#goodsPackageSalePrice").data("kendoNumericTextBox").focus();
									}
								});
							});
							return;
						} else {
							var dataItem = cGrid.dataSource.at(0);

							var resultSaleRate =  Math.floor(100 - (goodsPackageInputSalePrice / recommendedPackageTotalPrice * 100));	// 할인율 계산 (소수점은 절사)
							var discountPrice = recommendedPackageTotalPrice - goodsPackageInputSalePrice;								// 할인액 계산
							var marginRate = 0																							//마진율

/*
							if(baseTaxYn == "Y"){	// 과세일 경우 계산 = (정상가 - 묶음상품 판매가 * 1.1) / 정상가 * 100
								marginRate = Math.round( (recommendedPackageTotalPrice - goodsPackageInputSalePrice * 1.1) / recommendedPackageTotalPrice * 100 );
							}
							else{	// 면세일 경우 계산 = (정상가 - 묶음상품 판매가) / 정상가 * 100
								marginRate = Math.round( (recommendedPackageTotalPrice - goodsPackageInputSalePrice) / recommendedPackageTotalPrice * 100 );
							}
*/
							if(baseTaxYn == "Y"){	// 과세일 경우 계산 = (묶음상품 판매가 - 원가총액 * 1.1) / 묶음상품 판매가 * 100
								marginRate = Math.floor( (goodsPackageInputSalePrice - standardPackageTotalPrice * 1.1) / goodsPackageInputSalePrice * 100 );
							}
							else{	// 면세일 경우 계산 = (묶음상품 판매가 - 원가총액 ) / 묶음상품 판매가 * 100
								marginRate = Math.floor( (goodsPackageInputSalePrice - standardPackageTotalPrice) / goodsPackageInputSalePrice * 100 );
							}

							cGrid.dataSource.at(0).set("goodsPackageSalePrice", goodsPackageInputSalePrice);
							dataItem.set("saleRate", resultSaleRate);			// 할인율 설정
							dataItem.set("discountPrice", discountPrice);		// 할인액 설정
							dataItem.set("marginRate", marginRate);				// 마진율 설정

							$(this.element.context).blur();
						}
					}
				});

				var model =  cGrid.dataItem(this);

				//convertSaleType(model.saleType);

				kendo.bind(this,model);
			});
		});  // cGrid.bind("dataBound", function()

		// 할인유형 변경시 할인률과 묶음상품 할인가 input box (비)활성화 전환
		function convertSaleType(saleTypeValue) {
			if (saleTypeValue == "GOODS_DISCOUNT_METHOD_TP.FIXED_PRICE") {
//				$("#saleRate").prop("disabled", true);
				$("#saleRate").data("kendoNumericTextBox").enable(false);
				$("#saleRate").closest(".k-widget.k-numerictextbox").css("background-color", "#c0c0c0");
				$("#goodsPackageSalePrice").data("kendoNumericTextBox").enable(true);
				$("#goodsPackageSalePrice").closest(".k-widget.k-numerictextbox").css("background-color", "");
			}
			else {
//				$("#saleRate").prop("disabled", false);
				$("#saleRate").data("kendoNumericTextBox").enable(true);
				$("#saleRate").closest(".k-widget.k-numerictextbox").css("background-color", "");
				$("#goodsPackageSalePrice").data("kendoNumericTextBox").enable(false);
				$("#goodsPackageSalePrice").closest(".k-widget.k-numerictextbox").css("background-color", "#c0c0c0");
			}

//			dGridDs.data([]);;	// 묶음상품 구성목록 초기화
//			eGridDs.data([]);;	// 증정품 구성목록 초기화
		}

		var row = {
			saleType: 'GOODS_DISCOUNT_METHOD_TP.FIXED_PRICE',
			recommendedPackageTotalPrice: 0,
			saleTotalPrice: 0,
			salePrice: 0,
			marginRate: 0
		}

		setTimeout(function() {
			cGrid.dataSource.add(row);
		});
	}  // function fnInitCgrid()

	// 기본설정 > 묶음상품 구성목록 그리드
	function fnInitDgrid(){
		dGridDs = fnGetGroupDataSource({
			model_id : 'ilGoodsId',
			aggregate_fields: [	{ field: "recommendedPrice", aggregate: "sum" },
								{ field: "salePrice", aggregate: "sum" },
								{ field: "goodsQuantity", aggregate: "sum" },
								{ field: "recommendedTotalPrice", aggregate: "sum" },
								{ field: "saleTotalPrice", aggregate: "sum" },
								{ field: "discountPricePerGoods", aggregate: "sum" },
								{ field: "saleRate", aggregate: "average" },
								//{ field: "salePricePerUnit", aggregate: "sum" },
								{ field: "salePricePerQuantity", aggregate: "sum" },
								{ field: "marginRate", aggregate: "average" }	]
		});
		dGridDs.bind("change", function(e) {
			if (e.action == "itemchange") {

			}
		});

		dGridOpt = {
			dataSource: dGridDs,
			 pageable  : false
			,autoBind:false
			,navigatable: true
			,scrollable: false
			,columns   : [
				 { field:'itemCodeBarcode'			,title : '마스터품목코드<br>품목바코드'		, width:'10%'	,attributes:{ style:'text-align:center' }
				 									,footerTemplate: "합계 (묶음상품 판매가)"
				 									,footerAttributes: { style:'text-align:center' }
				 									,template: "#: ilItemCode # <br> #: itemBarcode #" }
				,{ field:'ilGoodsId'				,title : '상품코드'					, width:'6%'	,attributes:{ style:'text-align:center' }}
				,{ field:'goodsTypeName'			,title : '상품유형'					, width:'6%'	,attributes:{ style:'text-align:center' }
													,footerAttributes: { style:'text-align:center' } }
				,{ field:'goodsName'				,title : '상품명'						, width:'12%'	,attributes:{ style:'text-align:left' }}
				,{ field:'taxYnName'				,title : '과세구분'					, width:'6%'	,attributes:{ style:'text-align:center' }}
				,{ field:'recommendedPrice'			,title : '정상가<br>(개당)'				, width:'6%'	,attributes:{ style:'text-align:right' }
													,format: "{0:n0}"
													,footerTemplate: "#: kendo.toString(sum, 'n0') #"
													,footerAttributes: { style:'text-align:right' } }
				,{ field:'salePrice'				,title : '판매가<br>(개당)'				, width:'6%'	,attributes:{ style:'text-align:right' }
													,format: "{0:n0}"
													,footerTemplate: "#: kendo.toString(sum, 'n0') #"
													,footerAttributes: { style:'text-align:right' } }
				,{ field:'goodsQuantity'				,title : '구성수량'					, width:'6%'	,attributes:{ style:'text-align:center' }
													,format: "{0:n0}"
													,footerTemplate: "#: kendo.toString(sum, 'n0') #"
													,footerAttributes: { style:'text-align:center' } }
				,{ field:'recommendedTotalPrice'	,title : '정상가총액'					, width:'6%'	,attributes:{ style:'text-align:right' }
													,format: "{0:n0}"
													,footerTemplate: "#: kendo.toString(sum, 'n0') #"
													,footerAttributes: { style:'text-align:right' } }
				,{ field:'saleTotalPrice'			,title : '판매가총액'					, width:'6%'	,attributes:{ style:'text-align:right' }
													,format: "{0:n0}"
													,footerTemplate: "#: kendo.toString(sum, 'n0') #"
													,footerAttributes: { style:'text-align:right' } }

				,{ field:'discountPricePerGoods'	,title : '할인액(개당)'					, width:'6%'
													,format: "{0:n0}"
													,attributes: {class:'discountPricePerGoods-cell', style:'text-align:right'}
													,footerTemplate: "#: kendo.toString(sum, 'n0') #"
													,footerAttributes: { style:'text-align:right' }
													}

				,{ field:'saleRate'					,title : '할인율'						, width:'6%'
													,template: "#: saleRate # %"
													,attributes: {class:'saleRate-cell', style:'text-align:center'}
													,footerTemplate: "#: kendo.toString(average, 'n0') # %"
													,footerAttributes: { style:'text-align:center' } }

				,{ field:'salePricePerUnit'			,title : '묶음상품<br>판매가(개당)'		, width:'6%'	,attributes:{ style:'text-align:center' }}

				,{ field:'salePricePerQuantity'		,title : '묶음상품<br>판매가'		, width:'6%'
													,format: "{0:n0}"
													,attributes: {class:'salePricePerQuantity-cell', style:'text-align:right'}
													,footerTemplate: "#: kendo.toString(sum, 'n0') #"
													,footerAttributes: { style:'text-align:right; font-size: 20px;' } }

				,{ field:'marginRate'				,title : '마진율'						, width:'6%'
													,attributes: {class:'marginRate-cell', style:'text-align:center'}
													,template: "#: marginRate # %"
													,footerTemplate: "#: kendo.toString(average, 'n0') # %"
													,footerAttributes: { style:'text-align:center' } }
			]
			,noRecordMsg: "-"
		};
		dGrid = $('#dGrid').initializeKendoGrid( dGridOpt ).cKendoGrid();

//		dGrid.bind("dataBound", function(){
//			var rows = this.tbody.children();
//			var dataItems = this.dataSource.view();
//
//			for (var i = 0; i < dataItems.length; i++)
//			  kendo.bind(rows[i], dataItems[i]);
//		});
	}

	// 기본설정 > 증정품 구성목록 그리드
	function fnInitEgrid(){
		eGridDs = fnGetEditDataSource({
			model_id : 'ilGoodsId'
		});

		eGridOpt = {
			dataSource: eGridDs
			,autoBind:false
			,navigatable: true
			,scrollable: false
			,selectable: false
			,columns   : [
				{ field:'itemCodeBarcode'			,title : '마스터품목코드<br>품목바코드'		, width:'110px'	,attributes:{ style:'text-align:center' }
													, template: "#: ilItemCode # <br> #: itemBarcode #" }
				,{ field:'ilGoodsId'				,title : '상품코드'					, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'goodsTypeName'			,title : '상품유형'					, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'goodsName'				,title : '상품명'						, width:'200px'	,attributes:{ style:'text-align:left' }
													,template: '<img src="#: publicStorageUrl + size75Image #" style="vertical-align:middle; padding-right:12px">#: goodsName #' }
				,{ field:'taxYnName'				,title : '과세구분'					, width:'60px'	,attributes:{ style:'text-align:center' }}
				,{ field:'recommendedPrice'			,title : '정상가<br>(개당)'				, width:'80px'	,attributes:{ style:'text-align:right' } ,format: "{0:n0}"}
				,{ field:'salePrice'				,title : '판매가<br>(개당)'				, width:'80px'	,attributes:{ style:'text-align:right' } ,format: "{0:n0}"}
				,{ field:'purchaseQuanity'			,title : '구성수량'						, width:'64px'	,attributes:{ style:'text-align:center' }, format: "{0:n0}"}
				,{ field:'recommendedTotalPrice'	,title : '정상가총액'					, width:'80px'	,attributes:{ style:'text-align:right' } ,format: "{0:n0}"}
				,{ field:'marginRate'				,title : '마진율<br>(정상가 기준)'		, width:'100px'	,attributes:{ style:'text-align:center' } ,format: "{0:n0} %"}
			]
			,noRecordMsg: "-"
		};
		eGrid = $('#eGrid').initializeKendoGrid( eGridOpt ).cKendoGrid();

		eGrid.bind("dataBound", function(){
		});
	}
	//goodsPackageGoodsMappingList

	// 묶음 상품 수정 > 묶음 조합 설정 > 묶음상품 구성 정보 Grid
	function fnInitGoodsPackageGoodsMappingGrid(){
		goodsPackageGoodsMappingGridDs = fnGetEditDataSource({
//			url : '',
			model_id : 'ilGoodsId'
//			model_fields : {
//				ilGoodsId :			{ editable : false, type : 'number' },
//				goodsBase :			{ editable : false, type : 'string' },
//				itemCodeBarcode :	{ editable : false, type : 'string' },
//				goodsTypeName :		{ editable : false, type : 'string' },
//				goodsName :			{ editable : false, type : 'string' },
//				taxYnName :			{ editable : false, type : 'string' },
//				purchaseQuanity :	{ editable : false, type : 'number' }
//			}
		});

		goodsPackageGoodsMappingGridOpt = {
			dataSource: goodsPackageGoodsMappingGridDs
			,autoBind:false
			,navigatable: true
			,scrollable: false
			,selectable: false
			//,height:280
			,columns   : [
				 { field:'goodsPackageType'			,title : '상품구분'					, width:'50px'	,attributes:{ style:'text-align:center' }}
				,{ field:'itemCodeBarcode'			,title : '품목코드<br>품목바코드'		, width:'110px'	,attributes:{ style:'text-align:center' }
					, template : function(result){
						if (result.itemBarcode == undefined || result.itemBarcode == '') {
							return result.ilItemCode;
						}
						else {
							return result.ilItemCode + "<BR>" + result.itemBarcode;
						}
					}
				 }
				,{ field:'targetGoodsId'			,title : '상품코드'					, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'goodsType'				,title : '상품유형코드'				, hidden:true}
				,{ field:'goodsTypeName'			,title : '상품유형'					, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'goodsName'				,title : '상품명'						, width:'200px'	,attributes:{ style:'text-align:left; text-decoration: underline;color:blue;' }}
				,{ field:'goodsQuantity'			,title : '구성수량'					, width:'64px'	,attributes:{ style:'text-align:center' }}
				,{ field:'taxYn'					,title : '과세코드'					, hidden:true}
				,{ field:'taxName'					,title : '과세구분'					, width:'60px'	,attributes:{ style:'text-align:center' }}
				,{ field:'standardPrice'			,title : '원가'						, width:'64px'	,attributes:{ style:'text-align:right' },format: "{0:n0}"}
				,{ field:'recommendedPrice'			,title : '정상가'						, width:'64px'	,attributes:{ style:'text-align:right' },format: "{0:n0}"}
				,{ field:'saleStatusCode'	,title : '판매상태코드'					, hidden:true}
				,{ field:'saleStatus'	,title : '판매상태'					, width:'64px'	,attributes:{ style:'text-align:center' }}
				,{ field:'displayYn'		,title : '전시상태'					, width:'64px'	,attributes:{ style:'text-align:center' }}
				,{ title : '출고가능수량'				, width:'64px'	,attributes:{ style:'text-align:center' }
					, template : function(result){
						if (result.stockOrderYn == "Y") {	// 재고발주여부YN
							if (result.goodsType == "GOODS_TYPE.DISPOSAL") return "D+0 : " + result.d0OrderDiscardQty;	// 폐기상품
							else return "D+0 : " + result.d0OrderQty + "<BR>" + "D+1 : " + result.d1OrderQty;
						} else {
							if (result.unlimitStockYn == "Y") {	// 재고 무제한
								return "재고무제한";
							} else {
								return "한정재고" + "<BR>" + result.notIfStockCnt;
							}
						}
					}
				}
				, {
					title: '재고상세', width: '85px', attributes: {style: 'text-align:center'}
					, template: function (result) {
						var html = "";
						//if (fnIsProgramAuth("EDIT_STOCK")) {	// 권한 확인 필요
							if (result.stockOrderYn == "Y") {
								html = "<a role='button' class='k-button k-button-icontext' kind='detailInfoBtn' href='#'>상세<BR>보기</a>";
							} else {
								html = "";
							}
						//}
						return html;
					}
				}
			]
			,noRecordMsg: "-"
		};
		goodsPackageGoodsMappingGrid = $('#goodsPackageGoodsMappingGrid').initializeKendoGrid( goodsPackageGoodsMappingGridOpt ).cKendoGrid();

		goodsPackageGoodsMappingGrid.bind("dataBound", function(){
		});

		$(goodsPackageGoodsMappingGrid.tbody).on("click", "td", function (e) {
			var row = $(this).closest("tr");
			var rowIdx = $("tr", goodsPackageGoodsMappingGrid.tbody).index(row);
			var colIdx = $("td", row).index(this);
			if(colIdx == 5){
				fnGoodsPackge(e);
			}
		});

		$(goodsPackageGoodsMappingGrid.tbody).on("click", "a[kind=detailInfoBtn]", function (e) {
			e.preventDefault();
			var dataItem = goodsPackageGoodsMappingGrid.dataItem($(e.currentTarget).closest("tr"));
			var nowDate = fnGetToday();

			fnKendoPopup({
				id: 'goodsStockMgm',
				title: '재고 상세 정보',
				param: { "ilItemCd":dataItem.ilItemCode, "ilItemWarehouseId":dataItem.ilItemWarehouseId, "baseDt":nowDate },
				src: '#/goodsStockMgm',
				width: '1100px',
				height: '800px'
			});
		});
	}

	/**
	 * 묶음 구성상품 판매상태에 따라 묶음 상품 판매상태 수정 가능 범위 조정
	 */
	function fnGoodsSaleStatus() {
		var mySaleStatus = viewModel.ilGoodsDetail.get("saleStatus");
		let saleStatuses = new Set();
		$("#goodsPackageGoodsMappingGrid tbody tr").each(function(index){
			saleStatuses.add(goodsPackageGoodsMappingGrid.dataItem('tr:eq('+index+')').saleStatusCode);
		});

		let statusForAbled = new Array();
		if (viewModel.get("pageMode") == 'create') { // 상품 등록시
			statusForAbled.push('SALE_STATUS.SAVE');
		}
		else if (mySaleStatus == 'SALE_STATUS.SAVE') {
//			statusForAbled.push('SALE_STATUS.SAVE', 'SALE_STATUS.WAIT');
			statusForAbled.push('SALE_STATUS.SAVE'); // 승인프로세스 적용시, 승인완료시 판매대기로 전환
		}
		else if (mySaleStatus == 'SALE_STATUS.WAIT' || mySaleStatus == 'SALE_STATUS.ON_SALE') {
			statusForAbled.push('SALE_STATUS.WAIT', 'SALE_STATUS.ON_SALE', 'SALE_STATUS.OUT_OF_STOCK_BY_MANAGER', 'SALE_STATUS.STOP_SALE');
		}
		else if (mySaleStatus == 'SALE_STATUS.OUT_OF_STOCK_BY_SYSTEM') {
			statusForAbled.push('SALE_STATUS.STOP_SALE', 'SALE_STATUS.OUT_OF_STOCK_BY_SYSTEM', 'SALE_STATUS.OUT_OF_STOCK_BY_MANAGER');
		}
		else if (mySaleStatus == 'SALE_STATUS.OUT_OF_STOCK_BY_MANAGER' || mySaleStatus == 'SALE_STATUS.STOP_SALE') {
			// 구성품의 판매상태가 일시품절(시스템), 일시품절(관리자), 판매중지가 존재할 경우 판매대기 또는 판매중으로 변경할 수 없다.
			if (saleStatuses.has('SALE_STATUS.OUT_OF_STOCK_BY_SYSTEM') || saleStatuses.has('SALE_STATUS.OUT_OF_STOCK_BY_MANAGER') || saleStatuses.has('SALE_STATUS.STOP_SALE')) {
				statusForAbled.push('SALE_STATUS.OUT_OF_STOCK_BY_MANAGER', 'SALE_STATUS.STOP_SALE');
			}
			else {
				statusForAbled.push('SALE_STATUS.WAIT', 'SALE_STATUS.ON_SALE', 'SALE_STATUS.OUT_OF_STOCK_BY_MANAGER', 'SALE_STATUS.STOP_SALE');
			}
		}
		else {
			// Do Nothing
		}

		$("input[name=saleStatus]").attr('disabled', true);
		$.each(statusForAbled, function(i, v){
			$("input[name=saleStatus][value='"+v+"']").attr('disabled', false);
		});
	}

	function fnGoodsPackge(e){
		var dataItem = goodsPackageGoodsMappingGrid.dataItem($(e.target).closest('tr'));
		let option = {};

		if (dataItem.goodsType == "GOODS_TYPE.NORMAL"){
			option.url = "#/goodsMgm";
			option.menuId = 768;
			option.target = "goodsMgm";
			option.data = {
					ilGoodsId : dataItem.targetGoodsId
			};
		}
		else if (dataItem.goodsType == "GOODS_TYPE.DISPOSAL"){
			option.url = "#/goodsDisposal";
			option.menuId = 921;
			option.target = "goodsDisposal";
			option.data = {
					ilGoodsId : dataItem.targetGoodsId
			};
		}
		else if (dataItem.goodsType == "GOODS_TYPE.GIFT" || dataItem.goodsType == "GOODS_TYPE.GIFT_FOOD_MARKETING"){
			option.url = "#/goodsAdditional";
			option.menuId = 865;
			option.target = "goodsAdditional";
			option.data = {
					ilGoodsId : dataItem.targetGoodsId
			};
		}
		fnGoNewPage(option);
	}
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

		fnInputValidationByRegexp("goodsName", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);	//상품명에 글자제한
		//fnInputValidationLimitSpecialCharacter('goodsDesc');																	// 상품설명 : 특수문자 입력 제한
		fnInputValidationByRegexp("goodsDesc", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);
		fnPromotionNameValidationCheck();																						// 프로모션 상품명 Validation 체크 Listener 등록
		fnSearchKeywordValidationCheck();																						// 검색 키워드 Validation 체크 Listener 등록

		//기본정보 > MD추천 > 노출여부 DropdownList
		fnKendoDropDownList({
			id : 'mdRecommendYn',
			data : [ {
				code : 'Y',
				name : '노출함'
			}, {
				code : 'N',
				name : '노출안함'
			} ],
			valueField : 'code',
			textField : 'name'
		});

		//기본 정보 > 상품명 > 포장용량 구성정보 > 노출 여부
		fnTagMkRadio({
			id : 'packageUnitDisplayYn',
			data : [ {
				"CODE" : "N",
				"NAME" : "노출안함"
			}, {
				"CODE" : "Y",
				"NAME" : "노출함"
			}],
			tagId : 'packageUnitDisplayYn'
		});

		//기본 정보 > 상품명 > 포장용량 구성정보 > 노출 여부 > 속성 추가
		$("input[name=packageUnitDisplayYn]").each(function() {
			$(this).attr("data-bind", "checked: ilGoodsDetail.packageUnitDisplayYn, events: {change: onPackageUnitDisplayYn}");
		});

		//기본 정보 > 프로모션 상품명 시작날짜
		fnKendoDatePicker({
			id : 'promotionNameStartYear',
			format : 'yyyy-MM-dd',
//			defVal : todayDate,
//			max: lastDate,
			change: function(e){
				viewModel.fnDateChange(e, "promotionNameStartYear");
			}
		});
		$("#promotionNameStartYear").data("kendoDatePicker").unbind("blur");

		//기본정보 > 프로모션 상품명 종료날짜
		fnKendoDatePicker({
			id : 'promotionNameEndYear',
			format : 'yyyy-MM-dd',
//			min: todayDate,
//			max: lastDate,
			change: function(e){
				viewModel.fnDateChange(e, "promotionNameEndYear");
			}
		});


		// ##### 기본정보 end #####

		//판매/전시 > 구매 허용 범위
		fnTagMkChkBox({
			id : 'purchaseTargetType',
			data : [ {
				"CODE" : "ALL",
				"NAME" : "전체"
			}, {
				"CODE" : "PURCHASE_TARGET_TP.MEMBER",
				"NAME" : "일반"
			}, {
				"CODE" : "PURCHASE_TARGET_TP.EMPLOYEE",
				"NAME" : "임직원"
			}, {
				"CODE" : "PURCHASE_TARGET_TP.NONMEMBER",
				"NAME" : "비회원"
			} ],
			tagId : 'purchaseTargetType'
		});

		//판매/전시 > 구매 허용 범위 > 속성 추가
		$("input[name=purchaseTargetType]").each(function() {
			$(this).attr("data-bind", "checked: ilGoodsDetail.purchaseTargetType, events: {change: onPurchaseTargetType}");
		});

		//판매/전시 > 판매허용범위 (PC/Mobile)
		fnTagMkChkBox({
			id : 'goodsDisplayType',
			url : "/admin/comn/getCodeList",
			tagId : 'goodsDisplayType',
			async : false,
			style : {},
			params : {"stCommonCodeMasterCode" : "GOODS_DISPLAY_TYPE", "useYn" :"Y"},
			beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }],
			change : function(e) {
			}
		});
		//판매/전시 > 전시 (PC/Mobile) > 속성 추가
		$("input[name=goodsDisplayType]").each(function() {
			$(this).attr("data-bind", "checked: ilGoodsDetail.goodsDisplayType, events: {change: onGoodsDisplayType}");
		});

		//판매/전시 > 판매기간 시작 년도
		fnKendoDatePicker({
			id : 'saleStartYear',
			format : 'yyyy-MM-dd',
//			defVal : todayDate,
//			max: lastDate,
			change: function(e){
				// fnStartCalChange('saleStartYear', 'saleEndYear');


				viewModel.fnDateChange(e, "saleStartYear");
			}
		});

		//판매/전시 > 판매기간 종료 년도
		fnKendoDatePicker({
			id : 'saleEndYear',
			format : 'yyyy-MM-dd',
//			min: todayDate,
//			max: lastDate,
			change: function(e){
				// fnEndCalChange('saleStartYear', 'saleEndYear');



				viewModel.fnDateChange(e, "saleEndYear");
			}
		});

		//판매/전시 > 전시 상태
		fnTagMkRadio({
			id : 'displayYn',
			data : [ {
				"CODE" : "Y",
				"NAME" : "전시"
			}, {
				"CODE" : "N",
				"NAME" : "미전시"
			} ],
			tagId : 'displayYn'
		});
		//판매/전시 > 전시 상태 > 속성 추가
		$("input[name=displayYn]").each(function() {
			$(this).attr("data-bind", "checked: ilGoodsDetail.displayYn");
			$(this).attr("required", "required");
			$(this).addClass("clientAllow");
		});

		//판매/전시 > 판매 상태
		fnTagMkRadio({
			id : 'saleStatus',
			url : "/admin/comn/getCodeList",
			tagId : 'saleStatus',
			async : false,
			style : {},
			params : {"stCommonCodeMasterCode" : "SALE_STATUS", "useYn" :"Y"},
			change : function(e) {
			}
		});
		//판매/전시 > 판매 상태 > 속성 추가
		$("input[name=saleStatus]").each(function() {
			$(this).attr("data-bind", "checked: ilGoodsDetail.saleStatus");
		});

		//판매/전시 > 외부몰 판매 상태
		fnTagMkRadio({
			id : 'goodsOutmallSaleStatus',
			url : "/admin/comn/getCodeList",
			tagId : 'goodsOutmallSaleStatus',
			async : false,
			style : {},
			params : {"stCommonCodeMasterCode" : "GOODS_OUTMALL_SALE_STAT", "useYn" :"Y"},
			change : function(e) {
			}
		});
		//판매/전시 > 외부몰 판매 상태 > 속성 추가
		$("input[name=goodsOutmallSaleStatus]").each(function() {
			$(this).attr("data-bind", "checked: ilGoodsDetail.goodsOutmallSaleStatus");
		});

		//판매 정보 > 판매 유형
		fnTagMkRadio({
			id : 'saleType',
			data : [ {
				"CODE" : "SALE_TYPE.NORMAL",
				"NAME" : "일반판매"
			} ],
			tagId : 'saleType',
		});
		//판매 정보 > 판매 유형 > 속성 추가
		$("input[name=saleType]").each(function() {
			$(this).attr("data-bind", "checked: ilGoodsDetail.saleType"); // onSaleTypeClick : 예약판매 클릭 시 (임시)
		});

		fnKendoDropDownList({
			id : "presentYn",
			tagId : "presentYn",
			data : [
				{ "CODE" : "Y", "NAME" : "Y" },
				{ "CODE" : "N", "NAME" : "N" }
			]
		});

		//혜택 설정 > 쿠폰 사용 허용
		fnTagMkRadio({
			id : 'couponUseYn',
			data : [ {
				"CODE" : "Y",
				"NAME" : "허용"
			}, {
				"CODE" : "N",
				"NAME" : "허용 안함"
			} ],
			tagId : 'couponUseYn'
		});
		//혜택 설정 > 쿠폰 사용 허용 > 속성 추가
		$("input[name=couponUseYn]").each(function() {
			$(this).attr("data-bind", "checked: ilGoodsDetail.couponUseYn, disabled: isCouponUseYn");
		});

		//HGRM-1708 최소/최대 구매 제한 설정 범위 1~99개 처리
		var buyCount = [];
		for(var i = 1; i < 100; i++) {
			buyCount.push({"code":i	,"name":i+" SET"})
		}

		//HGRM-1708 최소/최대 구매 제한 설정 범위 1~99개 처리
		//구매 제한 설정 > 최소 구매
		fnKendoDropDownList({
			id : 'limitMinimumCnt',
			data : buyCount,
			valueField : 'code',
			textField : 'name'
//			, blank : "선택"
		});

		//구매 제한 설정 > 최대 구매
		fnTagMkRadio({
			id : 'limitMaximumType',
			url : "/admin/comn/getCodeList",
			tagId : 'limitMaximumType',
			async : false,
			style : {},
			params : {"stCommonCodeMasterCode" : "PURCHASE_LIMIT_MAX_TP", "useYn" :"Y"},
			change : function(e) {
			}
		});
		//구매 제한 설정 > 최대 구매 > 속성 추가
		$("input[name=limitMaximumType]").each(function() {
			$(this).attr("data-bind", "checked: ilGoodsDetail.limitMaximumType, events: {change: onLimitMaximumTypeChange}, disabled: ilGoodsDetail.isLimitMaximumType");
		});

		//구매 제한 설정 > 최대 구매 > 기간
		fnKendoDropDownList({
			id : 'limitMaximumDuration',
			data : [ {
				code : '1',
				name : '1일'
			}, {
				code : '7',
				name : '7일'
			}, {
				code : '15',
				name : '15일'
			}, {
				code : '30',
				name : '1개월'
			}, {
				code : '90',
				name : '3개월'
			}, {
				code : '180',
				name : '6개월'
			}, {
				code : '365',
				name : '1년'
			}],
			valueField : 'code',
			textField : 'name'
		});

		//HGRM-1708 최소/최대 구매 제한 설정 범위 1~99개 처리
		//구매 제한 설정 > 최대 구매 > 갯수
		fnKendoDropDownList({
			id : 'limitMaximumCnt',
			data : buyCount,
			valueField : 'code',
			textField : 'name',
			blank : "선택"
		});

		// 상품 이미지 radio
		fnTagMkRadio({
			id	:  'goodsPackageImageType',
			tagId : 'goodsPackageImageType',
			url   : "/admin/comn/getCodeList",
			async : false,
			params : {"stCommonCodeMasterCode" : "GOODS_PACKAGE_IMG_TP", "useYn" :"Y"},
			style : {},
			change: function(e) {
				var goodsPackageImageType = e.target.value;
				fnPackageImageList(goodsPackageImageType);	// 상품이미지 타입 radio 버튼에 따른 화면 변경
			}
		});
		//구매 제한 설정 > 최대 구매 > 속성 추가
		$("input[name=goodsPackageImageType]").each(function() {
			$(this).attr("data-bind", "checked: ilGoodsDetail.goodsPackageImageType");
		});

		// 상품 상세 기본정보 radio
		fnTagMkRadio({
			id	:  'goodsPackageBasicDescYn',
			tagId : 'goodsPackageBasicDescYn',
			//chkVal: 'DIRECT',
			data  : [   { "CODE" : "Y"	, "NAME":'묶음상품 전용' },
						{ "CODE" : "M"	, "NAME":'묶음상품 전용 + 구성상품 상세' },
						{ "CODE" : "N"	, "NAME":'구성상품 상세' }
					],
			style : {}
		});
		//상품 상세 기본 정보
		$("input[name=goodsPackageBasicDescYn]").each(function() {
			$(this).attr("data-bind", "checked: ilGoodsDetail.goodsPackageBasicDescYn, events: {change: goodsPackageBasicDescYnChange}");
		});

		//상품공지 > 상세 상단공지 > 첨부파일 저장
		$('#uploadNoticeBelow1ImageForm').html('');
		var htmlText = '<input type="file" id="uploadNoticeBelow1Image" name="uploadNoticeBelow1Image" accept=".jpg, .jpeg, .gif">';

		$('#uploadNoticeBelow1ImageForm').append(htmlText);

		fnKendoUpload({
			id : "uploadNoticeBelow1Image",
			select : function(e) {

				if (e.files && e.files[0]) { // 이미지 파일 선택시

					if (noticeBelowImageUploadMaxLimit < e.files[0].size) { // 상세 상단공지 이미지 업로드 용량 체크
						fnKendoMessage({
							message : '이미지 업로드 허용 최대 용량은 ' + parseInt(noticeBelowImageUploadMaxLimit / 1024) + ' kb 입니다.',
							ok : function(e) {}
						});
						return;
					}

					// PJH Start
					var imageExtension = e.files[0]['extension'].toLowerCase();

					// 업로드 가능한 이미지 확장자 목록에 포함되어 있는지 확인
					if( allowedImageExtensionList.indexOf(imageExtension) < 0 ) {
						fnKendoMessage({
							message : '업로드 가능한 이미지 확장자가 아닙니다.',
							ok : function(e) {}
						});
						return;
					}
					// PJH End

					var noticeBelow1ImageFileList = viewModel.get('noticeBelow1ImageFileList');

					for (var i = noticeBelow1ImageFileList.length - 1; i >= 0; i--) {
						if (noticeBelow1ImageFileList[i]['name'] == e.files[0]['name']) { // 파일명 중복 체크
							fnKendoMessage({
								message : '이미지 파일명이 중복됩니다.',
								ok : function(e) {}
							});
							return;
						}
					}

					let reader = new FileReader();

					reader.onload = function(ele) {
						var itemImageScr = ele.target.result; // FileReader 로 생성한 상품 이미지 url
						var file = e.files[0].rawFile; // kendoUpload 로 가져온 상품 이미지 file 객체

						//기존 파일 삭제 처리
						viewModel.get("noticeBelow1ImageList").splice(0, 1);
						viewModel.get('noticeBelow1ImageFileList').splice(0, 1);

						viewModel.get('noticeBelow1ImageFileList').push(file); // 추가된 상품 이미지 File 객체를 전역변수에 추가

						viewModel.get('noticeBelow1ImageList').push({
							imageName : file['name'], // local 에서 최초 추가한 이미지 : id 역할을 하는 imageName 으로 원본 파일명을 그대로 사용
							imageOriginalName : file['name'], // 원본 File 명
							sort : '', // 정렬순서
							imageSrc : itemImageScr, // 상품 이미지 url
						});

						$(".noticeBelow1StartDate").attr("required","required");
						$(".noticeBelow1EndDate").attr("required","required");

						// PJH Start
						viewModel.set('showNoticeBelow1Date', true); // // 상세 상단공지 노출기간 Visible 처리
						// PJH End

						if (noticeBelow1ImageDelete) {
							viewModel.ilGoodsDetail.set('noticeBelow1StartYear',kendo.toString(defaultStartTime, "yyyy-MM-dd") );
							viewModel.ilGoodsDetail.set('noticeBelow1StartHour', "00");
							viewModel.ilGoodsDetail.set('noticeBelow1StartMinute', "00");
							viewModel.ilGoodsDetail.set('noticeBelow1EndYear', "2999-12-31");
							viewModel.ilGoodsDetail.set('noticeBelow1EndHour', "23");
							viewModel.ilGoodsDetail.set('noticeBelow1EndMinute', "59");
						}
					};

					reader.readAsDataURL(e.files[0].rawFile);
				}
			}
		});

		//상품공지 > 상세 상단공지 노출기간 시작 년도
		fnKendoDatePicker({
			id : 'noticeBelow1StartYear',
			format : 'yyyy-MM-dd',
//			defVal : todayDate,
			//max: viewModel.ilGoodsDetail.get("noticeBelow1EndYear"),
			change: function(e){
				viewModel.fnDateChange(e, "noticeBelow1StartYear");
			}
		});

		//상품공지 > 상세 상단공지 노출기간 종료 년도
		fnKendoDatePicker({
			id : 'noticeBelow1EndYear',
			format : 'yyyy-MM-dd',
		//	min: viewModel.ilGoodsDetail.get("noticeBelow1StartYear"),
//			max: lastDate,
			change: function(e){
				viewModel.fnDateChange(e, "noticeBelow1EndYear");
			}
		});

		//상품공지 > 상세 하단공지 노출기간 시작 년도
		fnKendoDatePicker({
			id : 'noticeBelow2StartYear',
			format : 'yyyy-MM-dd',
//			defVal : todayDate,
			//max: viewModel.ilGoodsDetail.get("noticeBelow2EndYear"),
			change: function(e){
				viewModel.fnDateChange(e, "noticeBelow2StartYear");
			}
		});

		//상품공지 > 상세 하단공지 노출기간 종료 년도
		fnKendoDatePicker({
			id : 'noticeBelow2EndYear',
			format : 'yyyy-MM-dd',
		//	min: viewModel.ilGoodsDetail.get("noticeBelow2StartYear"),
//			max: lastDate,
			change: function(e){
				viewModel.fnDateChange(e, "noticeBelow2EndYear");
			}
		});

		//상품공지 > 상세 하단공지 > 첨부파일 저장
		$('#uploadNoticeBelow2ImageForm').html('');
		var htmlText = '<input type="file" id="uploadNoticeBelow2Image" name="uploadNoticeBelow2Image" accept=".jpg, .jpeg, .gif">';

		$('#uploadNoticeBelow2ImageForm').append(htmlText);

		fnKendoUpload({ // 상품 이미지 첨부 File Tag 를 kendoUpload 로 초기화
			id : "uploadNoticeBelow2Image",
			select : function(e) {

				if (e.files && e.files[0]) { // 이미지 파일 선택시

					if (noticeBelowImageUploadMaxLimit < e.files[0].size) { // 상품 이미지 업로드 용량 체크
						fnKendoMessage({
							message : '이미지 업로드 허용 최대 용량은 ' + parseInt(noticeBelowImageUploadMaxLimit / 1024) + ' kb 입니다.',
							ok : function(e) {}
						});
						return;
					}

					// PJH Start
					var imageExtension = e.files[0]['extension'].toLowerCase();

					// 업로드 가능한 이미지 확장자 목록에 포함되어 있는지 확인
					if( allowedImageExtensionList.indexOf(imageExtension) < 0 ) {
						fnKendoMessage({
							message : '업로드 가능한 이미지 확장자가 아닙니다.',
							ok : function(e) {}
						});
						return;
					}
					// PJH End

					var noticeBelow2ImageFileList = viewModel.get('noticeBelow2ImageFileList');

					for (var i = noticeBelow2ImageFileList.length - 1; i >= 0; i--) {
						if (noticeBelow2ImageFileList[i]['name'] == e.files[0]['name']) { // 파일명 중복 체크
							fnKendoMessage({
								message : '이미지 파일명이 중복됩니다.',
								ok : function(e) {}
							});
							return;
						}
					}

					let reader = new FileReader();

					reader.onload = function(ele) {
						var itemImageScr = ele.target.result; // FileReader 로 생성한 상품 이미지 url
						var file = e.files[0].rawFile; // kendoUpload 로 가져온 상품 이미지 file 객체

						//기존 파일 삭제 처리
						viewModel.get("noticeBelow2ImageList").splice(0, 1);
						viewModel.get('noticeBelow2ImageFileList').splice(0, 1);

						viewModel.get('noticeBelow2ImageFileList').push(file); // 추가된 상품 이미지 File 객체를 전역변수에 추가

						viewModel.get('noticeBelow2ImageList').push({
							imageNameSecond : file['name'], // local 에서 최초 추가한 이미지 : id 역할을 하는 imageName 으로 원본 파일명을 그대로 사용
							imageOriginalName : file['name'], // 원본 File 명
							sort : '', // 정렬순서
							imageSrcSecond : itemImageScr, // 상품 이미지 url
						});

						//이미지가 저장되면 노출기간설정을 필수값으로 변경
						$(".noticeBelow2StartDate").attr("required","required");
						$(".noticeBelow2EndDate").attr("required","required");

						// PJH Start
						viewModel.set('showNoticeBelow2Date', true); // // 상세 하단공지 노출기간 Visible 처리
						// PJH End

						if (noticeBelow2ImageDelete) {
							viewModel.ilGoodsDetail.set('noticeBelow2StartYear',kendo.toString(defaultStartTime, "yyyy-MM-dd") );
							viewModel.ilGoodsDetail.set('noticeBelow2StartHour', "00");
							viewModel.ilGoodsDetail.set('noticeBelow2StartMinute', "00");
							viewModel.ilGoodsDetail.set('noticeBelow2EndYear', "2999-12-31");
							viewModel.ilGoodsDetail.set('noticeBelow2EndHour', "23");
							viewModel.ilGoodsDetail.set('noticeBelow2EndMinute', "59");
						}
					};

					reader.readAsDataURL(e.files[0].rawFile);
				}
			}
		});

//		$('#kendoPopup').kendoWindow({
//			visible: false,
//			modal: true
//		});
	}
	//---------------Initialize Option Box End ------------------------------------------------

	//---------------Initialize UI Start ------------------------------------------------
	function fnInitUI() {

		//기본정보 > 용량정보 입력 > 한글, 영문대소문자, 숫자 허용
		//fnInputValidationForHangulAlphabetNumber('packageUnitDesc');
		fnInputValidationByRegexp("packageUnitDesc", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);

		kendo.bind($("#view"), viewModel);

		// ########## 이미지 start ##########
		// 등록된 상품 이미지 개수 viewModel 에 세팅
		viewModel.set('packageImageCount', viewModel.get('packageImageList').length*1 + viewModel.get('goodsImageList').length*1);

		// viewModel 이 화면에 바인딩된 후에 kendoSortable 을 적용해야 함
		$("#packageImageContainer").kendoSortable({		//묶음상품 전용 이미지 Drag로 이미지 순서 변경
			filter : ".goodsImage",
			cursor : "move",
			placeholder : function(element) {
				return element.clone().css("opacity", 0.1);
			},
			hint : function(element) {
				return element.clone().css("width", element.width()).removeClass("k-state-selected");
			},

			change : function(e) {

				// remove the item that has changed its order
				var item = viewModel.get('packageImageList').splice(e.oldIndex, 1)[0];

				// add the item back using the newIndex
				viewModel.get('packageImageList').splice(e.newIndex, 0, item);
				viewModel.trigger("change", {
					field : "packageImageList"
				});

				viewModel.set('imageSortOrderChanged', true); // 한번이라도 이미지 정렬 순서 변경시 해당 flag 값 true 로 변경
			}
		});

		$("#goodsImageContainer").kendoSortable({		//개별상품 대표 이미지 Drag로 이미지 순서 변경
			filter : ".goodsImage",
			cursor : "move",
			placeholder : function(element) {
				return element.clone().css("opacity", 0.1);
			},
			hint : function(element) {
				return element.clone().css("width", element.width()).removeClass("k-state-selected");
			},

			change : function(e) {

				// remove the item that has changed its order
				var item = viewModel.get('goodsImageList').splice(e.oldIndex, 1)[0];

				// add the item back using the newIndex
				viewModel.get('goodsImageList').splice(e.newIndex, 0, item);
				viewModel.trigger("change", {
					field : "goodsImageList"
				});

				viewModel.set('goodsImageSortOrderChanged', true); // 한번이라도 이미지 정렬 순서 변경시 해당 flag 값 true 로 변경
			}
		});

		// 묶음 상품 이미지 첨부 File Tag 강제 초기화
		$('#uploadPackageImageForm').html('');

		var htmlText = '<input type="file" ';
		htmlText += 'id="uploadPackageImage" ';
		htmlText += 'name="uploadPackageImage" ';
		htmlText += 'accept=".jpg, .jpeg, .gif" ';
		htmlText += '>';

		$('#uploadPackageImageForm').append(htmlText);

		fnKendoUpload({ // 묶음 상품 이미지 첨부 File Tag 를 kendoUpload 로 초기화
			id : "uploadPackageImage",
			select : function(e) {

				if (e.files && e.files[0]) { // 이미지 파일 선택시

					if (packageImageUploadMaxLimit < e.files[0].size) { // 상품 이미지 업로드 용량 체크
						fnKendoMessage({
							message : '이미지 업로드 허용 최대 용량은 ' + Math.round(packageImageUploadMaxLimit / 1024) + ' kb 입니다.',
							ok : function(e) {}
						});
						return;
					}

					var packageImageFileList = viewModel.get('packageImageFileList');

					for (var i = packageImageFileList.length - 1; i >= 0; i--) {
						if (packageImageFileList[i]['name'] == e.files[0]['name']) { // 파일명 중복 체크
							fnKendoMessage({
								message : '이미지 파일명이 중복됩니다.',
								ok : function(e) {}
							});
							return;
						}
					}

					let reader = new FileReader();

					reader.onload = function(ele) {
						var goodsImageScr = ele.target.result; // FileReader 로 생성한 상품 이미지 url
						var file = e.files[0].rawFile; // kendoUpload 로 가져온 상품 이미지 file 객체

						viewModel.get('packageImageFileList').push(file); // 추가된 상품 이미지 File 객체를 전역변수에 추가

						viewModel.get('packageImageList').push({
							basicYn : '', // 대표 이미지 여부 : 저장시 Backend 에서 지정됨
							imagePath : file['name'], // local 에서 최초 추가한 이미지 : id 역할을 하는 imageName 으로 원본 파일명을 그대로 사용
							imageOriginalName : file['name'], // 원본 File 명
							sort : '', // 정렬순서
							imageSrc : goodsImageScr, // 상품 이미지 url
						});

						// 상품 이미지 종류
						var goodsPackageImageType = $("#goodsPackageImageType").find("input[type=radio]:checked").val();

						// 등록된 상품 이미지 개수 갱신
						viewModel.set('packageImageCount', viewModel.get('packageImageList').length);

						// 이미지 최대 등록가능 개수 도달하거나 개별상품 전용 일때는 상품 이미지 추가 영역 숨김 처리
						if ( ( goodsPackageImageType == "GOODS_PACKAGE_IMG_TP.PACKAGE_GOODS" && viewModel.get('packageImageList').length >= viewModel.get('onlyPackageImageMaxLimit') ) ||
									goodsPackageImageType == "GOODS_PACKAGE_IMG_TP.NORMAL_GOODS" )
						{
							$("#packageImageAdd").hide();
						}

					};

					reader.readAsDataURL(e.files[0].rawFile);
				}
			}
		});

		//묶음/개별상품 조합 이미지 첨부파일
		$('#uploadPackageImageMixForm').html('');

		var htmlText = '<input type="file" ';
		htmlText += 'id="uploadPackageImageMix" ';
		htmlText += 'name="uploadPackageImageMix" ';
		htmlText += 'accept=".jpg, .jpeg, .gif" ';
		htmlText += '>';

		$('#uploadPackageImageMixForm').append(htmlText);

		fnKendoUpload({ // 묶음 상품 이미지 첨부 File Tag 를 kendoUpload 로 초기화
			id : "uploadPackageImageMix",
			select : function(e) {

				if (e.files && e.files[0]) { // 이미지 파일 선택시

					if (packageImageUploadMaxLimit < e.files[0].size) { // 상품 이미지 업로드 용량 체크
						fnKendoMessage({
							message : '이미지 업로드 허용 최대 용량은 ' + Math.round(packageImageUploadMaxLimit / 1024) + ' kb 입니다.',
							ok : function(e) {}
						});
						return;
					}

					var packageImageMixFileList = viewModel.get('packageImageMixFileList');

					for (var i = packageImageMixFileList.length - 1; i >= 0; i--) {
						if (packageImageMixFileList[i]['name'] == e.files[0]['name']) { // 파일명 중복 체크
							fnKendoMessage({
								message : '이미지 파일명이 중복됩니다.',
								ok : function(e) {}
							});
							return;
						}
					}

					let reader = new FileReader();

					reader.onload = function(ele) {
						var goodsImageScr = ele.target.result; // FileReader 로 생성한 상품 이미지 url
						var file = e.files[0].rawFile; // kendoUpload 로 가져온 상품 이미지 file 객체

						viewModel.get('packageImageMixFileList').push(file); // 추가된 상품 이미지 File 객체를 전역변수에 추가

						viewModel.get('packageImageMixList').push({
							basicYn : '', // 대표 이미지 여부 : 저장시 Backend 에서 지정됨
							imagePath : file['name'], // local 에서 최초 추가한 이미지 : id 역할을 하는 imageName 으로 원본 파일명을 그대로 사용
							imageOriginalName : file['name'], // 원본 File 명
							sort : '', // 정렬순서
							imageSrc : goodsImageScr, // 상품 이미지 url
						});

						// 상품 이미지 종류
						var goodsPackageImageType = $("#goodsPackageImageType").find("input[type=radio]:checked").val();

						// 등록된 상품 이미지 개수 갱신
						viewModel.set('packageImageCount', viewModel.get('packageImageMixList').length);

//						console.log("goodsPackageImageType : ", goodsPackageImageType);
//						console.log("viewModel.get('packageImageMixList').length : ", viewModel.get('packageImageMixList').length);
//						console.log("viewModel.get('mixedImageMaxLimit') : ", viewModel.get('mixedImageMaxLimit'));

						// 이미지 최대 등록가능 개수 도달하거나 개별상품 전용 일때는 상품 이미지 추가 영역 숨김 처리
						if (	( goodsPackageImageType == "GOODS_PACKAGE_IMG_TP.MIXED" && viewModel.get('packageImageMixList').length >= viewModel.get('mixedImageMaxLimit') )  ||
									goodsPackageImageType == "GOODS_PACKAGE_IMG_TP.NORMAL_GOODS" )
						{
							$("#packageImageAdd").hide();
						}

					};

					reader.readAsDataURL(e.files[0].rawFile);
				}
			}
		});

		//ADD 버튼 영역 호출
		var goodsPackageImageType = $("#goodsPackageImageType").find("input[type=radio]:checked").val();
		fnAddPackageImageArea(goodsPackageImageType);
		// ########## 이미지 end ##########

		// ########## 편집기 start ##########
		fnItemDescriptionKendoEditor({ // 상품 상세 기본정보 Editor
			id : 'goodsPackageBasicDesc',
		});

		fnKendoUpload({ // 상품 상세 기본 정보 / 주요정보 이미지 첨부 File Tag 를 kendoUpload 로 초기화
			id : "uploadImageOfEditor",
			select : function(e) {

				if (e.files && e.files[0]) { // 이미지 파일 선택시

					// UPLOAD_IMAGE_SIZE : 이미지 업로드 / 첨부 최대 용량 ( 단위 : byte )
					if (UPLOAD_IMAGE_SIZE < e.files[0].size) { // 용량 체크
						fnKendoMessage({
							message : '이미지 업로드 허용 최대 용량은 ' + Math.round(UPLOAD_IMAGE_SIZE / 1048576) + ' MB 입니다.',
							ok : function(e) {}
						});
						return;
					}

					let reader = new FileReader();

					reader.onload = function(ele) {
						fnUploadImageOfEditor(); // 선택한 이미지 파일 업로드 함수 호출
					};

					reader.readAsDataURL(e.files[0].rawFile);
				}
			}
		});

		function fnUploadImageOfEditor(opt) { // Editor 에 첨부된 이미지 Upload 후 반환된 url 을 Editor 내에 img 태그로 추가

			var formData = $('#uploadImageOfEditorForm').formSerialize(true);

			fnAjaxSubmit({
				form : "uploadImageOfEditorForm",
				fileUrl : "/fileUpload",
				method : 'GET',
				url : '/comn/getPublicStorageUrl',
				storageType : "public",
				domain : "il",
				params : formData,
				success : function(result) {

					var uploadResult = result['addFile'][0];
					var serverSubPath = uploadResult['serverSubPath'];
					var physicalFileName = uploadResult['physicalFileName'];
					var imageSrcUrl = result['publicStorageUrl'] + serverSubPath + physicalFileName; // 업로드 이미지 url

					var editor = $('#' + workindEditorId).data("kendoEditor"); // 이미지 첨부할 Editor
					editor.exec('inserthtml', {
						value : '<img src="' + imageSrcUrl + '"/>'
					});

				},
				isAction : 'insert'
			});

		}

		function fnItemDescriptionKendoEditor(opt) { // 상품 상세 기본정보 / 주요 정보 Editor

			if  ( $('#' + opt.id).data("kendoEditor") ) { // 기존에 이미 Editor 로 생성되어 있는 경우 초기화

				$('#' + opt.id + 'Td').html('');  // 해당 editor TextArea 를 가지고 있는 td 내의 html 을 강제 초기화

				var textAreaHtml = '<textarea id="' + opt.id + '" ';
				textAreaHtml += 'style="width: 100%; height: 400px" ';
				textAreaHtml += '></textarea>"'

				$('#' + opt.id + 'Td').append(textAreaHtml);  // 새로운 editor TextArea 를 추가

			}

			$('#' + opt.id).kendoEditor({
				tools : [ {
					name : 'viewHtml',
					tooltip : 'HTML 소스보기'
				},
				// { name : 'pdf', tooltip : 'PDF 저장' },  // PDF 저장시 한글 깨짐 현상 있어 주석 처리
				//------------------- 구분선 ----------------------
				{
					name : 'bold',
					tooltip : '진하게'
				}, {
					name : 'italic',
					tooltip : '이탤릭'
				}, {
					name : 'underline',
					tooltip : '밑줄'
				}, {
					name : 'strikethrough',
					tooltip : '취소선'
				},
				//------------------- 구분선 ----------------------
				{
					name : 'foreColor',
					tooltip : '글자색상'
				}, {
					name : 'backColor',
					tooltip : '배경색상'
				},
				//------------------- 구분선 ----------------------
				{
					name : 'justifyLeft',
					tooltip : '왼쪽 정렬'
				}, {
					name : 'justifyCenter',
					tooltip : '가운데 정렬'
				}, {
					name : 'justifyRight',
					tooltip : '오른쪽 정렬'
				}, {
					name : 'justifyFull',
					tooltip : '양쪽 맞춤'
				},
				//------------------- 구분선 ----------------------
				{
					name : 'insertUnorderedList',
					tooltip : '글머리기호'
				}, {
					name : 'insertOrderedList',
					tooltip : '번호매기기'
				}, {
					name : 'indent',
					tooltip : '들여쓰기'
				}, {
					name : 'outdent',
					tooltip : '내어쓰기'
				},
				//------------------- 구분선 ----------------------
				{
					name : 'createLink',
					tooltip : '하이퍼링크 연결'
				}, {
					name : 'unlink',
					tooltip : '하이퍼링크 제거'
				}, {
					name : 'insertImage',
					tooltip : '이미지 URL 첨부'
				}, {
					name : 'file-image',
					tooltip : '이미지 파일 첨부',
					exec : function(e) {
						e.preventDefault();
						workindEditorId = opt.id; // 이미지 첨부할 Editor 의 id 를 전역변수에 저장
						$('#uploadImageOfEditor').trigger('click'); // 파일 input Tag 클릭 이벤트 호출

					},
				},
				//------------------- 구분선 ----------------------
				{
					name : 'subscript',
					tooltip : '아래 첨자'
				}, {
					name : 'superscript',
					tooltip : '위 첨자'
				},
				//------------------- 구분선 ----------------------
				{ name : 'tableWizard'    , tooltip : '표 수정' },
				{
					name : 'createTable',
					tooltip : '표 만들기'
				}, {
					name : 'addRowAbove',
					tooltip : '위 행 추가'
				}, {
					name : 'addRowBelow',
					tooltip : '아래 행 추가'
				}, {
					name : 'addColumnLeft',
					tooltip : '왼쪽 열 추가'
				}, {
					name : 'addColumnRight',
					tooltip : '오른쪽 열 추가'
				}, {
					name : 'deleteRow',
					tooltip : '행 삭제'
				}, {
					name : 'deleteColumn',
					tooltip : '열 삭제'
				},
				{ name : 'mergeCellsHorizontally'   , tooltip : '수평으로 셀 병합' },
		        { name : 'mergeCellsVertically'   , tooltip : '수직으로 셀 병합' },
		        { name : 'splitCellHorizontally'   , tooltip : '수평으로 셀 분할' },
		        { name : 'splitCellVertically'   , tooltip : '수직으로 셀 분할' },
				//------------------- 구분선 ----------------------
				'formatting', 'fontName', {
					name : 'fontSize',
					items : [ {
						text : '8px',
						value : '8px'
					}, {
						text : '9px',
						value : '9px'
					}, {
						text : '10px',
						value : '10px'
					}, {
						text : '11px',
						value : '11px'
					}, {
						text : '12px',
						value : '12px'
					}, {
						text : '13px',
						value : '13px'
					}, {
						text : '14px',
						value : '14px'
					}, {
						text : '16px',
						value : '16px'
					}, {
						text : '18px',
						value : '18px'
					}, {
						text : '20px',
						value : '20px'
					}, {
						text : '22px',
						value : '22px'
					}, {
						text : '24px',
						value : '24px'
					}, {
						text : '26px',
						value : '26px'
					}, {
						text : '28px',
						value : '28px'
					}, {
						text : '36px',
						value : '36px'
					}, {
						text : '48px',
						value : '48px'
					}, {
						text : '72px',
						value : '72px'
					}, ]
				}
				//  ,'print'
				],
				//			pdf : {
				//				title : "상품상세 기본정보",
				//				fileName : "상품상세 기본정보.pdf",
				//				paperSize : 'A4',
				//				margin : {
				//					bottom : '20 mm',
				//					left : '20 mm',
				//					right : '20 mm',
				//					top : '20 mm'
				//				}
				//			},
				messages : {
					formatting : '포맷',
					formatBlock : '포맷을 선택하세요.',
					fontNameInherit : '폰트',
					fontName : '글자 폰트를 선택하세요.',
					fontSizeInherit : '글자크기',
					fontSize : '글자 크기를 선택하세요.',
					//------------------- 구분선 ----------------------
					print : '출력',
					//------------------- 구분선 ----------------------

					//------------------- 구분선 ----------------------
					imageWebAddress : '웹 주소',
					imageAltText : '대체 문구', //Alternate text
					//------------------- 구분선 ----------------------
					fileWebAddress : '웹 주소',
					fileTitle : '링크 문구',
					//------------------- 구분선 ----------------------
					linkWebAddress : '웹 주소',
					linkText : '선택 문구',
					linkToolTip : '풍선 도움말',
					linkOpenInNewWindow : '새 창에서 열기', //Open link in new window
					//------------------- 구분선 ----------------------
					dialogInsert : '적용',
					dialogUpdate : 'Update',
					dialogCancel : '닫기',
				//-----------------------------
				}
			});  // function fnItemDescriptionKendoEditor(opt) // 상품 상세 기본정보 / 주요 정보 Editor

			$('<br/>').insertAfter($('.k-i-create-table').closest('li'));

		}  // function fnItemDescriptionKendoEditor(opt) { // 상품 상세 기본정보 / 주요 정보 Editor
		// ########## 편집기 end ##########
	}  // function fnInitUI()

	//---------------Initialize UI End ------------------------------------------------

	//---------------PageMode Start ------------------------------------------------
	function fnPageMode(){
		if(ilGoodsId != undefined && ilGoodsId != ""){	//상품 수정이라면
			viewModel.set("pageMode", "modify");
			viewModel.set("baseGoodsPopupButtonVisible", false);
			viewModel.set("goodsPackageBaseCreateVisible", false);
			viewModel.set("goodsPackageBaseModifyVisible", true);

			//묶음상품 기본설정 > 묶음 조합 설정 > 묶음상품 구성 정보 Grid
			fnInitGoodsPackageGoodsMappingGrid();

			//상품 상세정보
			fnGoodsDetail(ilGoodsId);
		}
		else{
			viewModel.set("pageMode", "create");
			viewModel.set("goodsPackageBaseCreateVisible", true);
			viewModel.set("goodsPackageBaseModifyVisible", false);
			fnInitGrid();												//Initialize Grid
		}
		// 승인정보 관련 화면 제어(goodsApprovalComm.js) S
		fnApprHtml();
		// 승인정보 관련 화면 제어(goodsApprovalComm.js) E
	}
	//---------------PageMode End ------------------------------------------------

	//-------------------------------  Common Function start -------------------------------
	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){

		switch(id){
			case 'baseGoods':  // 기준 상품 선택시
				var dataRows = data.rows;

//				console.log("기준상품 dataRows : ", dataRows);
//				console.log("기준상품 dataRows[0].mallinmallCategoryId : ", dataRows[0].mallinmallCategoryId);

				if (!dataRows || dataRows.length == 0) {
					alert("데이타가 존재하지 않습니다.");
					return;
				}

				// ##### 묶음 상품 선택 그리드(aGrid)에 레코드 추가 start #####
				if (aGridDs.data().length > 0) {
					removeGoodsInfo($("#baseGoodsId").val());  // 상품 관련 정보 삭제
					aGridDs.remove(aGridDs.get($("#baseGoodsId").val()));  // 묶음 상품 선택 그리드에서 레코드 삭제
				}

				viewModel.ilGoodsDetail.set("ilItemCode", dataRows[0].ilItemCode);
				viewModel.ilGoodsDetail.set("urWarehouseId", dataRows[0].urWarehouseId);
				viewModel.ilGoodsDetail.set("urSupplierId", dataRows[0].urSupplierId);
				viewModel.ilGoodsDetail.set("urBrandId", dataRows[0].urBrandId);
				viewModel.ilGoodsDetail.set("mallinmallCategoryId", dataRows[0].mallinmallCategoryId);
				viewModel.ilGoodsDetail.set("itemBarcode", dataRows[0].itemBarcode);
				viewModel.ilGoodsDetail.set("baseGoodsId", dataRows[0].ilGoodsId);

				urSupplierId = dataRows[0].urSupplierId;
				supplierCode = dataRows[0].supplierCode;
				urWarehouseId = dataRows[0].urWarehouseId;

				ilItemCode = dataRows[0].ilItemCode;
				urWarehouseId = dataRows[0].urWarehouseId;

				viewModel.ilGoodsDetail.set("categoryStandardName", dataRows[0].categoryStandardName);
				//viewModel.ilGoodsDetail.set("undeliverableAreaType", dataRows[0].undeliverableAreaType);

				viewModel.ilGoodsDetail.set("taxYn", dataRows[0].taxYn);
				viewModel.ilGoodsDetail.set("taxYnName", dataRows[0].taxYnName);

				dataRows[0].baseGoodsYn = "Y";
				dataRows[0].goodsBase = "기준상품";

				aGridDs.insert(0, dataRows[0]);  // 묶음 상품 선택 그리드에  insert
				// ##### 묶음 상품 선택 그리드(aGrid)에 레코드 추가 end #####

				// #### 전시카테고리, 몰인몰 카테고리 출력 Start ####
				//전시카테고리 초기화, 몰인몰 카테고리 초기화
				if(ctgryGridDs) {
					ctgryGridDs.data([]);
					$("#goodsDisplayCategory1").empty();
					$("#goodsDisplayCategory2").empty();
					$("#goodsDisplayCategory3").empty();
					$("#goodsDisplayCategory4").empty();
					$("#selectCtgoryText-addon").hide();
					$("#selectCtgoryText").text("전시 카테고리를 선택한 후 ‘카테고리 추가’ 버튼을 선택해 주세요.");
					$("#goodsDisplayCategoryGridArea").hide();
				}

				if(mallInMallCtgryGridDs) {
					mallInMallCtgryGridDs.data([]);
					$("#mallInMallCategory1").empty();
					$("#mallInMallCategory2").empty();
					$("#mallInMallCategory3").empty();
					$("#mallInMallCategory4").empty();
					$("#selectMallInMallCtgoryText-addon").hide();
					$("#selectMallInMallCtgoryText").text("전시 카테고리를 선택한 후 ‘카테고리 추가’ 버튼을 선택해 주세요.");;
					$("#mallInMallCategoryGridArea").hide();
				}
				//전시카테고리 초기화, 몰인몰 카테고리 초기화

				viewModel.set("isGoodsDisplayVisible", true);
				//기본정보 > 전시 카테고리 > 선택내역 리스트
				fnInitCategoryGrid();
				//기본정보 > 전시 카테고리
				fnInitCtgry();


				//기본정보 > 몰인몰 카테고리 > 선택내역 리스트
				//추후에 사용예정이라 주석처리 HGRM-4280

				if(dataRows[0].supplierCode == "OG"){																		//공급처가 올가홀푸드이면
					viewModel.set("isMallInMallVisible", true);
					fnInitMallInMallCategoryGrid();
					//기본정보 > 몰인몰 카테고리(공급업체가 올가, 풀무원녹즙2일 경우에만)
					fnInitMallInMallCtgry();
				}
				else if(dataRows[0].supplierCode == "DM" && dataRows[0].mallinmallCategoryId){								//공급처가 풀무원 녹즙(PDM) 이고 브랜드가 잇슬림이면
					viewModel.set("isMallInMallVisible", true);
					fnInitMallInMallCategoryGrid();
					//기본정보 > 몰인몰 카테고리(공급업체가 올가, 풀무원녹즙2일 경우에만)
					fnInitMallInMallCtgry();
				}
				else{
					viewModel.set("isMallInMallVisible", false);
				}
				// #### 전시카테고리, 몰인몰 카테고리 출력 End ####

				//배송/발주 정보 > 배송 유형 리스트 불러오기
				fnItemWarehouseList();

				// 기준 상품 이미지 추가/수정
				var goodsImageList = viewModel.get('goodsImageList');
				if (goodsImageList.length == 0) {  // 선택한 기준 상품이 없으면 새로 추가
					viewModel.get('goodsImageList').push({
						  goodsId: dataRows[0].ilGoodsId
						, imageSrc: publicStorageUrl + dataRows[0].size180Image
					});
				} else {  // 이미 선택한 기준 상품이 존재하면 수정
					goodsImageList[0].goodsId = dataRows[0].ilGoodsId;
					goodsImageList[0].imageSrc = publicStorageUrl + dataRows[0].size180Image;
				}

				//기준 상품정보 제외하고 모든 내역을 초기화 처리 함
				fnGoodsPackageReset();

				// 상품정보 제공고시, 상품 영양정보 출력
				//기준 상품 선택시에 상품정보 제공고시, 상품 영양정보를 초기화
				$("#goodsInfoList").html('<tr noDataRow><td colspan="2" style="text-align:center">-</td></tr>');
				$("#goodsNutritionList").html('<tr noDataRow><td colspan="6" style="text-align:center">-</td></tr>');

				var url  = '/admin/goods/regist/getGoodsInfo';
				var cbId = 'goodsInfo';

				var data = {
					ilGoodsIds : dataRows[0].ilGoodsId
				}
				fnAjax({
					url	 : url,
					params  : data,
					async	: false,
					success :
						function( data ){
							fnBizCallback(cbId, data);
						},
						isAction : 'batch'
				});

				fnCheckAndSetPresentYn();
				break;
			case 'goodsList':  // 개별상품 선택시
				var dataRows = data.rows;

				var newAddGoodsIds = [];  // 새로 추가하게될 상품 id 목록
				var newAddGoodsDataItems = [];  // 새로 추가하게될 상품 dataItem 목록
				var legacyGoodsDateItems = [];  // 기존에 이미 선택한 상품의 dataItem 목록

				var baseGoodsWarehouseId = (aGrid.dataSource.data().slice())[0].urWarehouseId;
				var diffWarehouse = false;
				if (baseGoodsWarehouseId == undefined || baseGoodsWarehouseId == null || baseGoodsWarehouseId == '') {
					return;
				}

				//console.log("묶음상품 선택 Data", dataRows);

				// ##### 묶음 상품 선택 그리드(aGrid)에 레코드 추가 start #####
				for (var i in dataRows) {
					if (dataRows[i].urWarehouseId != baseGoodsWarehouseId) { // 기준상품과 출고처가 같은지 확인
						diffWarehouse = true;
						continue;
					}

					//console.log("묶음 상품 Data", dataRows[i]);
					dataRows[i].baseGoodsYn = "N";

					if (!aGridDs.get(dataRows[i].ilGoodsId)) {  // 기존에 선택하지 않은 상품만
						aGridDs.add(dataRows[i]);  // 묶음상품선택 그리드에 레코드 추가

						newAddGoodsDataItems.push(dataRows[i]);
						newAddGoodsIds.push(dataRows[i].ilGoodsId);
					} else {  // 이미 선택했던 상품의 경우
						legacyGoodsDateItems.push(dataRows[i]);
					}
				}
				// ##### 묶음 상품 선택 그리드(aGrid)에 레코드 추가 end #####

				// 배송 불가 지역, 반품 가능 기간 조합, 몰인몰 카테고리 노출여부를 위한 상품 IDS 가져오기
				var indivisualGoodsDatas = aGrid.dataSource.data().slice();		// 묶음상품 선택 그리드의 데이타
				var allPackageIlGoodsIds = [];


				//console.log("indivisualGoodsDatas : ", indivisualGoodsDatas);

				for (var i = 0; i < indivisualGoodsDatas.length; i++){
					allPackageIlGoodsIds.push(indivisualGoodsDatas[i].ilGoodsId);
				}

				fnMallInMallCtgryDisplayAllow(indivisualGoodsDatas);	//몰인몰 카테고리 노출 여부
				fnGoodsEtcAssemble(allPackageIlGoodsIds);				//묶음 상품 내역 조합의 배송 불가 지역, 반품 가능 기간 산출

				// 새로 추가된 상품에 대해서만 부가정보(상품정보제공고시, 상품영양정보등, 상품 이미지등...)들을 조회한다
				if (newAddGoodsIds.length > 0) {

					// 상품정보제공고시, 상품영양정보등 을 조회
					var url  = '/admin/goods/regist/getGoodsInfo';
					var cbId = 'goodsInfo';

					var data = {
						ilGoodsIds : newAddGoodsIds.join(",")
					}

					fnAjax({
						url	 : url,
						params  : data,
						success :
							function( data ){
								fnBizCallback(cbId, data);
							},
							isAction : 'batch'
					});

					// 상품 이미지 출력
					for (var i in newAddGoodsDataItems) {
						viewModel.get('goodsImageList').push({
							  goodsId: newAddGoodsDataItems[i].ilGoodsId
							, imageSrc: publicStorageUrl + newAddGoodsDataItems[i].size180Image
						});
					}
				}

				if (diffWarehouse) {
					fnKendoMessage({message : '출고처가 다른 상품이 있어 제거되었습니다.'});
				}
				fnCheckAndSetPresentYn();
				break;

			case 'goodsGiftList':  // 증정품 선택시
				var dataRows = data.rows;

				var newAddGoodsIds = [];  // 새로 추가하게될 상품 id 목록
				var newAddGoodsDataItems = [];  // 새로 추가하게될 상품 dataItem 목록
				var legacyGoodsDateItems = [];  // 기존에 이미 선택한 상품의 dataItem 목록

				var baseGoodsWarehouseId = (aGrid.dataSource.data().slice())[0].urWarehouseId;
				var diffWarehouse = false;
				if (baseGoodsWarehouseId == undefined || baseGoodsWarehouseId == null || baseGoodsWarehouseId == '') {
					return;
				}

				// ##### 증정품 선택 그리드(bGrid)에 레코드 추가 start #####
				for (var i in dataRows) {
					if (dataRows[i].urWarehouseId != baseGoodsWarehouseId) { // 기준상품과 출고처가 같은지 확인
						diffWarehouse = true;
						continue;
					}

					dataRows[i].salePrice = 0;					// 증정품 판매가는 0원임

					if (!bGridDs.get(dataRows[i].ilGoodsId)) {	// 기존에 선택하지 않은 상품만 조회
						bGridDs.add(dataRows[i]);  // 묶음상품선택 그리드에 레코드 추가

						newAddGoodsDataItems.push(dataRows[i]);
						newAddGoodsIds.push(dataRows[i].ilGoodsId);
					}
					else {	// 이미 선택했던 상품의 경우
						legacyGoodsDateItems.push(dataRows[i]);
					}
				}
				// ##### 증정품 선택 그리드(aGrid)에 레코드 추가 end #####
				if (diffWarehouse) {
					fnKendoMessage({message : '출고처가 다른 상품이 있어 제거되었습니다.'});
				}
				break;
			case 'goodsInfo':  // 상품별 기타 정보 조회시
				var html = "";

				// 상품정보 제공고시 출력
				if (data.goodsInfoAnnounceList.length > 0) {
					$("#goodsInfoList tr[noDataRow]").remove();
					for ( var i in data.goodsInfoAnnounceList) {
						var template = kendo.template($("#goodsInfo-row-template").html());
						html += template(data.goodsInfoAnnounceList[i]);
					}
					$("#goodsInfoList").append(html);

					var $btn = $($('#goodsInfoListTable'));
					var $table = $btn.closest("table");

					for(var i in data.goodsInfoAnnounceList){
						$("#goodsInfoListTable").closest("table").find("tr[goodsinfotogglearea"+data.goodsInfoAnnounceList[i].ilGoodsId+"]").hide();	//소제목을 close 한 상태로 Load
					}
				}

				// 상품 영양정보 출력
				if (data.goodsNutritionList.length > 0) {
					$("#goodsNutritionList tr[noDataRow]").remove();
					html = "";
					for ( var i in data.goodsNutritionList) {
						var template = kendo.template($("#goodsNutritionInfo-row-template").html());
						html += template(data.goodsNutritionList[i]);
					}
					$("#goodsNutritionList").append(html);

					for(var i in data.goodsNutritionList){
						$("#goodsNutritionListTable").closest("table").find("tr[goodsnutritioninfotogglearea"+data.goodsNutritionList[i].ilGoodsId+"]").hide(); //소제목을 close 한 상태로 Load
					}
				}

				break;
			case 'save':
				fnKendoMessage({message : '저장되었습니다.'});
				break;
		}  // switch(id)
	}  // function fnBizCallback( id, data )

	function fnCheckAndSetPresentYn() {
		const set = new Set(aGrid.dataSource.data().map(g => {return g.presentYn}));
		const presentYn = [...set];

		if(presentYn.length == 1 && presentYn.includes("NA")) {	// 모두 미대상
			$("#presentYnTh").attr("hidden", true);
			$("#presentYnTd").attr("hidden", true);
			viewModel.ilGoodsDetail.set("presentYn", "NA");
		} else if(presentYn.length == 1 && presentYn.includes("Y")) {	// 모두 허용
			$("#presentYnTh").attr("hidden", false);
			$("#presentYnTd").attr("hidden", false);
			$("#presentYn").data('kendoDropDownList').enable(true);
			viewModel.ilGoodsDetail.set("presentYn", "Y");
		} else {	// 비허용
			$("#presentYnTh").attr("hidden", false);
			$("#presentYnTd").attr("hidden", false);
			$("#presentYn").data('kendoDropDownList').enable(false);
			viewModel.ilGoodsDetail.set("presentYn", "N");
		}

	}
	//해당 구성 상품으로 구성된 묶음상품 정보가 있는지 체크
	function fnGoodsPackageExistChk() {

		var ilGoodsIds = [];
		var aGridDatas = aGrid.dataSource.data().slice();
		var bGridDatas = bGrid.dataSource.data().slice();
		var existChkVal = false;
		let selectPackageGoodsList = [];
		let nCnt = 0;

		if (aGridDatas.length > 0){
			for (var i = 0; i < aGridDatas.length; i++){
//				console.log(aGridDatas[i]);
//				console.log(aGridDatas[i].goodsBase);
					ilGoodsIds[nCnt] = aGridDatas[i].ilGoodsId;
					selectPackageGoodsList.push({
						"targetGoodsId": aGridDatas[i].ilGoodsId,
						"goodsQuantity": Number(aGridDatas[i].purchaseQuanity),
						"goodsType": aGridDatas[i].goodsType
					});
					nCnt++;
			}
			//증정도 중복 조건에 포함 처리
			for (var i = 0; i < bGridDatas.length; i++){
				ilGoodsIds[nCnt] = bGridDatas[i].ilGoodsId;
				selectPackageGoodsList.push({
					"targetGoodsId": bGridDatas[i].ilGoodsId,
					"goodsQuantity": Number(bGridDatas[i].purchaseQuanity),
					"goodsType": bGridDatas[i].goodsType
				});
			}

			var param = {selectPackageGoodsList : selectPackageGoodsList, ilGoodsIds: ilGoodsIds}; // HGRM-4246 기존 묶음상품 구성 체크 로직 변경 / 2021.01.31 이명수

			fnAjax({
				url		: '/admin/goods/regist/goodsPackageExistChk',
				params	: param,
				contentType : 'application/json',
				isAction : 'select',
				async	: false,
				success	:
					function( data ){
						if(data.ilGoodsId != null) {
							let message = "해당 상품구성정보가 이미 존재합니다.<br/>상품정보:{"+data.ilGoodsId+"}{"+data.goodsName+"}";

							fnKendoMessage({
								message : message
							});
							existChkVal = true;
						}
					}
			});
		}

		return existChkVal;
	}

	/**
	 * 상품 조합
	 */
	function fnGoodsAssemble() {
		//console.log("Number(aGrid.dataSource.at(0).get(\"purchaseQuanity\")) : " + Number(aGrid.dataSource.at(0).get("purchaseQuanity")))
		// HGRM-4246 기존 묶음상품 구성 체크 로직 변경 / 2021.01.31 이명수
		if (!checkBaseGoods()){
			return true;
		}

		if ( aGridDs.data().length == 1) {
			if(Number(aGrid.dataSource.at(0).get("purchaseQuanity")) == 1 && bGridDs.data().length == 0) {
				fnKendoMessage({
					message : '동일 상품으로만 묶음상품 구성 시 구성수량을 최소 2개 이상으로 등록하시거나<BR>증정품을 하나 이상 선택해 주세요.',
					ok : function(e) {
					}
				});

				return true;
			}
		}

		//해당 구성 상품으로 구성된 묶음상품 정보가 있는지 체크
		var existChkVal = fnGoodsPackageExistChk();

		if(existChkVal) {
			return true;
		}

		totalsalePricePerUnit = 0;		// 묶음상품 판매가(개당)의 총 합을 초기화
		totalSalePriceGoods = 0;		// 묶음상품 판매가총액의 합을 초기화
		totalDiscountPricePerGoods = 0;	//묶음상품 할인액총액의 합을 초기화

		var saleType = $("#cGrid input[name=saleType]").val();  // 할인유형
		var saleRate = String(cGrid.dataSource.at(0).get("saleRate"));

		if( (saleType == "GOODS_DISCOUNT_METHOD_TP.FIXED_RATE" && (saleRate == "null" || saleRate == "")) || (saleType == "GOODS_DISCOUNT_METHOD_TP.FIXED_PRICE" && cGrid.dataSource.at(0).get("goodsPackageSalePrice") === "") ) {

			var alertMessage = "";

			if (saleType == "GOODS_DISCOUNT_METHOD_TP.FIXED_PRICE")  // 고정할인일 경우
				alertMessage = "묶음상품 예상 판매가를 입력해 주세요.";
			else
				alertMessage = "할인율을 입력해 주십시오.";

			fnKendoMessage({
				message : alertMessage,
				ok : function(e) {
					if (saleType == "GOODS_DISCOUNT_METHOD_TP.FIXED_PRICE")  // 고정할인일 경우
						$("#goodsPackageSalePrice").data("kendoNumericTextBox").focus();
					else
						$("#saleRate").data("kendoNumericTextBox").focus();
				}
			});
			return true;
		}

		var columns = dGrid.getOptions().columns;
		var columnIndex = 0;  // 묶음상품 판매가(개당) 칼럼에 대한 columne index

		for ( var i in columns) {
			columns[i].headerAttributes.style = "text-align:center; vertical-align:middle;";

			if (columns[i].field == "salePricePerUnit") {
				columnIndex = i;
			}
		}

		dGridDs.data([]);

		if (saleType == "GOODS_DISCOUNT_METHOD_TP.FIXED_PRICE") { // 고정할인일 경우
			columns[columnIndex].template = "<input name='salePricePerUnit' value='#: salePricePerUnit #' style='text-align:right; width:50pt'>";
		}
		else {
			columns[columnIndex].template = "#: kendo.toString(salePricePerUnit, 'n0') #";
		}
		dGrid.setOptions({columns: columns});

		var indivisualGoodsDatas = aGrid.dataSource.data().slice();  // 묶음상품 선택 그리드의 데이타

		//console.log("indivisualGoodsDatas", indivisualGoodsDatas);

		var recommendedAllGoodsPrice = 0;									// 묶음 상품 선택 그리드의 정상가 총액의 합
		var purchaseAllQuanity = 0;											// 묶음 상품 선택 그리드의 구성 수량의 합
		var discountPrice = cGrid.dataSource.at(0).get("discountPrice");	// 묶음상품판매가 입력 그리드(cGrid)에서 입력한 상품 전체 할인액
		var saleRate = cGrid.dataSource.at(0).get("saleRate");				// 묶음상품판매가 입력 그리드(cGrid)에서 입력한 할인율
		var goodsPackageSalePrice = cGrid.dataSource.at(0).get("goodsPackageSalePrice");  // 묶음상품 판매가 입력 그리드에서 입력한 묶음상품 판매가(묶음상품 전체 판매가)
		let taxRate = 0;

		for (var i = 0; i < indivisualGoodsDatas.length; i++){
			recommendedAllGoodsPrice += indivisualGoodsDatas[i].recommendedTotalPrice;
			purchaseAllQuanity += Math.round(indivisualGoodsDatas[i].purchaseQuanity);
		}

		for (var i = 0; i < indivisualGoodsDatas.length; i++) {
			indivisualGoodsDatas[i].saleTotalPrice = indivisualGoodsDatas[i].salePrice * indivisualGoodsDatas[i].purchaseQuanity;	// 판매가 총액
			indivisualGoodsDatas[i].goodsQuantity = Math.round(indivisualGoodsDatas[i].purchaseQuanity);							// 구성수량
			indivisualGoodsDatas[i].saleRate = 0;																					// 할인율
			indivisualGoodsDatas[i].salePricePerUnit = 0;																			// 묶음상품 판매가(개당)

			let salePricePerUnit = 0;		//묶음상품 판매가(개당)
			let saleRateUnit = 0;			//할인율
			let discountPricePerGoods = 0;	//할인액(개당)
			let marginRate = 0;				//마진율
			let taxRate = indivisualGoodsDatas[i].taxYn == "Y" ? 1.1 : 1;	//과세수치

			if (saleType == "GOODS_DISCOUNT_METHOD_TP.FIXED_PRICE") {	// 고정가할인인 경우

				// 할인액(개당) 계산 ( 사용자가 묶음상품판매가 입력 그리드에서 입력해서 결정된 할인액 * 해당 상품의 정상가 총액 / 전체 상품 정상가총액의 합 / 구성수량)
				//indivisualGoodsDatas[i].discountPricePerGoods = Math.round(discountPrice * indivisualGoodsDatas[i].recommendedTotalPrice / recommendedAllGoodsPrice / indivisualGoodsDatas[i].goodsQuantity);

				// 할인액(개당) 계산 ( 묶음상품 판매가 입력 그리드의 할인액 / 묶음 상품 선택의 전체 구성 수량), 원 단위 절사
				//indivisualGoodsDatas[i].discountPricePerGoods = Math.floor((discountPrice / purchaseAllQuanity)/10) * 10;

/*
				// 할인액(개당) 계산 : 원단위 절사(해당 상품의 정상가 * (할인율/100))
				indivisualGoodsDatas[i].discountPricePerGoods = Math.floor(indivisualGoodsDatas[i].recommendedPrice * (saleRate/100)/10) * 10;

				// 할인율 계산 (소수점은 반올림)
				indivisualGoodsDatas[i].saleRate = Math.round(indivisualGoodsDatas[i].discountPricePerGoods / indivisualGoodsDatas[i].recommendedPrice * 100);
*/

				// 할인액 계산 시작 - 각 상품의 낱개별 할인 기준으로 한다.
//				var discountPrice = indivisualGoodsDatas[i].recommendedPrice * saleRate / 100;								// 개별 할인액 계산
//				var restDiscountPrice = discountPrice%10;																	// 원단위 계산
//				discountPrice = parseInt(discountPrice/10)*10;																// 개별 할인액 원단위 절사
//				if (restDiscountPrice > 0)																					// 절사된 원단위 할인이 있으면 10원 올림 처리
//					discountPrice += 10;
//
//				indivisualGoodsDatas[i].discountPricePerGoods = discountPrice;																					// 개별 할인액
//				indivisualGoodsDatas[i].saleRate = Math.round(indivisualGoodsDatas[i].discountPricePerGoods / indivisualGoodsDatas[i].recommendedPrice * 100);	// 개별 할인율

				//묶음상품 판매가(개당)
				salePricePerUnit = indivisualGoodsDatas[i].recommendedPrice - (indivisualGoodsDatas[i].recommendedPrice * saleRate / 100);
				salePricePerUnit = salePricePerUnit - (salePricePerUnit % 10);
				//할인액(개당)
				discountPricePerGoods = indivisualGoodsDatas[i].recommendedPrice - salePricePerUnit
				//할인율
				saleRateUnit = discountPricePerGoods / indivisualGoodsDatas[i].recommendedPrice * 100;
				saleRateUnit = saleRateUnit.toFixed(1);

			} else {	//정률할인인 경우
/*
				// 할인액(개당) 계산 : 원단위 절사(해당 상품의 정상가 * (할인율/100)) * 해당 상품의 구성수량
				indivisualGoodsDatas[i].discountPricePerGoods = Math.floor(indivisualGoodsDatas[i].recommendedPrice * (saleRate/100)/10) * 10;

				// 할인율
				indivisualGoodsDatas[i].saleRate = saleRate;
*/
				// 할인액 계산 시작 - 각 상품의 낱개별 할인 기준으로 한다.
//				var discountPrice = indivisualGoodsDatas[i].recommendedPrice * saleRate / 100;								// 개별 할인액 계산
//				var restDiscountPrice = discountPrice%10;																	// 원단위 계산
//				discountPrice = parseInt(discountPrice/10)*10;																// 개별 할인액 원단위 절사
//				if (restDiscountPrice > 0)																					// 절사된 원단위 할인이 있으면 10원 올림 처리
//					discountPrice += 10;
//
//				indivisualGoodsDatas[i].discountPricePerGoods = discountPrice;												// 개별 할인액
//				indivisualGoodsDatas[i].saleRate = saleRate;																// 개별 할인율
				// 할인액 계산 종료

				//묶음상품 판매가(개당)
				salePricePerUnit = indivisualGoodsDatas[i].recommendedPrice - (indivisualGoodsDatas[i].recommendedPrice * saleRate / 100);
				salePricePerUnit = salePricePerUnit - (salePricePerUnit % 10);
				//할인액(개당)
				discountPricePerGoods = indivisualGoodsDatas[i].recommendedPrice - salePricePerUnit
				//할인율
				saleRateUnit = saleRate;
			}

			//그리드 항목에 입력
			indivisualGoodsDatas[i].salePricePerUnit = salePricePerUnit;
			indivisualGoodsDatas[i].salePricePerQuantity = salePricePerUnit * indivisualGoodsDatas[i].goodsQuantity;
			indivisualGoodsDatas[i].discountPricePerGoods = discountPricePerGoods;
			indivisualGoodsDatas[i].saleRate = saleRateUnit;

			//마진율 계산
			taxRate = indivisualGoodsDatas[i].taxYn == "Y" ? 1.1 : 1;

			//마진율 계산
			marginRate = Math.floor( (salePricePerUnit - indivisualGoodsDatas[i].standardPrice * taxRate) / salePricePerUnit * 100 );
			indivisualGoodsDatas[i].marginRate = marginRate;
/*
			if (indivisualGoodsDatas[i].taxYn == "Y")  // 과세일 경우 계산 = (정상가 - 묶음상품 판매가(개당) * 1.1) / 정상가 * 100
				marginRate = Math.round( (indivisualGoodsDatas[i].recommendedPrice - indivisualGoodsDatas[i].salePricePerUnit * 1.1) / indivisualGoodsDatas[i].recommendedPrice * 100 );
			else  // 면세일 경우 계산 = (정상가 - 원가) / 정상가 * 100
				marginRate = Math.round( (indivisualGoodsDatas[i].recommendedPrice - indivisualGoodsDatas[i].salePricePerUnit) / indivisualGoodsDatas[i].recommendedPrice * 100 );
*/
/*
			if (indivisualGoodsDatas[i].taxYn == "Y") { // 과세일 경우 계산 = (묶음상품 판매가(개당) - 원가 * 1.1) / 묶음상품 판매가(개당) * 100
				marginRate = Math.round( (indivisualGoodsDatas[i].salePricePerUnit - indivisualGoodsDatas[i].standardPrice * 1.1) / indivisualGoodsDatas[i].salePricePerUnit * 100 );
			}
			else { // 면세일 경우 계산 = (묶음상품 판매가(개당) - 원가) / 묶음상품 판매가(개당) * 100
				marginRate = Math.round( (indivisualGoodsDatas[i].salePricePerUnit - indivisualGoodsDatas[i].standardPrice) / indivisualGoodsDatas[i].salePricePerUnit * 100 );
			}
*/
			// 묶음상품 판매가(개당) 의 총합
			totalsalePricePerUnit += Math.round(indivisualGoodsDatas[i].salePricePerUnit);
			//판매가총액(구성 수량을 곱한) 합계
			totalSalePriceGoods += Math.round(indivisualGoodsDatas[i].salePricePerUnit) * indivisualGoodsDatas[i].goodsQuantity;
			// 할인액총액(구성 수량을 곱한) 합계
			totalDiscountPricePerGoods += discountPricePerGoods * indivisualGoodsDatas[i].goodsQuantity;
		}

		viewModel.ilGoodsDetail.set("totalSalePriceGoods", totalSalePriceGoods);

		//종합 마진율 계산
		taxRate = indivisualGoodsDatas[0].taxYn == "Y" ? 1.1 : 1;
//		var totalMarginRate = Math.round( (cGrid.dataSource.at(0).get("recommendedPackageTotalPrice") - totalSalePriceGoods * taxRate) / cGrid.dataSource.at(0).get("recommendedPackageTotalPrice") * 100 );
		var totalMarginRate = Math.floor( (totalSalePriceGoods - cGrid.dataSource.at(0).get("standardTotalPrice") * taxRate) / totalSalePriceGoods * 100 ); // (묶음상품 판매가총액 - 원가총액 * taxRate) / 묶음상품 판매가총액 * 100

		columns[14].footerTemplate = "#: kendo.toString("+totalMarginRate+", 'n0') + ' %' #";
		//cGrid.dataSource.at(0).set("marginRate", totalMarginRate);					//묶음상품 판매가 계산 > 예상 마진율 업데이트
		cGrid.dataSource.at(0).set("goodsPackageSalePrice", totalSalePriceGoods);		//묶음상품 판매가 계산 > 묶음상품 예상 판매가 업데이트
		cGrid.dataSource.at(0).set("discountPrice", totalDiscountPricePerGoods);		//묶음상품 판매가 계산 > 할인액 업데이트


		// 묶음상품 판매가(개당)의 총합 column에 판매가총액(구성 수량을 곱한) 합계가 들어가야 하기 때문에 변경 처리
		var html = kendo.toString(totalSalePriceGoods, "n0");
//		var html = "<span style='color:#0000FF'>" + kendo.toString(totalSalePriceGoods, "n0") + "</span>";
		var diffSalePrice = totalSalePriceGoods - goodsPackageSalePrice; // 묶음상품 판매가 입력 그리드에서 입력한 묶음상품 판매가(묶음상품 전체 판매가)와 묶음상품 구성목록 그리드에서 입력한 모든 묶음상품 판매가(개당) 합과의 차액

/*
		if (diffSalePrice > 0){
			html += "<span style='color:#0000FF'>+" + kendo.toString(diffSalePrice, "n0") + "</span>";
		}
		else if(diffSalePrice == 0){
			html += "<span style='color:#0000FF'>" + kendo.toString(diffSalePrice, "n0") + "</span>";
		}
		else if (diffSalePrice < 0){
			html += "<span style='color:#FF0000'>" + kendo.toString(diffSalePrice, "n0") + "</span>";
		}
*/

		dGrid.setOptions({columns: columns});
		dGridDs.data(indivisualGoodsDatas);

		//$("#dGrid tr.k-footer-template td:eq(12)").html(html);								//묶음상품 판매가 합계 Cell

		if (saleType == "GOODS_DISCOUNT_METHOD_TP.FIXED_PRICE") {  // 고정할인일 경우 묶음상품 판매가 kendoNumericTextBox 설정
			dGrid.tbody.find("tr").each(function() {
				var inputDom = $(this).find("input[name=salePricePerUnit]");

				fnInputDomValidationForNumber(inputDom);

				var dataItem = dGrid.dataItem(this);

				$(inputDom).kendoNumericTextBox({
					format: "n0"
					, spinners: false
					, change: function(e) {
						const $numericTextBox = this.element.data("kendoNumericTextBox");
						if (this.value() <= 9) {
							setTimeout(function() {
								fnKendoMessage({
									message : '묶음상품 예약 판매가를 입력해주세요 (10원이상)',
									recommendedPrice : Math.floor((dataItem.recommendedPrice * 1)/10)*10,
									dom : inputDom,
									ok : function(e) {
										$numericTextBox.focus();
										$numericTextBox.value(Math.floor((dataItem.recommendedPrice * 1)/10)*10);
										$numericTextBox.trigger('change');
									}
								});
							});
							return;
						}

						if (this.value() > dataItem.recommendedPrice) {
							setTimeout(function() {
								fnKendoMessage({
									message : '정상가(개당)을 초과하는 금액을 입력할 수 없습니다.',
									recommendedPrice : Math.floor((dataItem.recommendedPrice * 1)/10)*10,
									dom : inputDom,
									ok : function(e) {
										$numericTextBox.focus();
										$numericTextBox.value(Math.floor((dataItem.recommendedPrice * 1)/10)*10);
										$numericTextBox.trigger('change');
									}
								});
							});
							return;
						}
						//						dataItem.salePricePerUnit = this.value();
						dataItem.salePricePerUnit = Math.floor((this.value() * 1)/10)*10; // 원단위 절사
						this.value(dataItem.salePricePerUnit); // 원단위 절사된 값으로 재설정

						dataItem.salePricePerQuantity = dataItem.salePricePerUnit * dataItem.goodsQuantity;
						dataItem.discountPricePerGoods = dataItem.recommendedPrice - dataItem.salePricePerUnit;				//할인액(개당) 다시 계산
//						dataItem.saleRate = Math.round(100 - (dataItem.salePricePerUnit/dataItem.recommendedPrice*100));	//할인율 다시 계산
						let saleRate = 100 - (dataItem.salePricePerUnit/dataItem.recommendedPrice*100);						//할인율 다시 계산 - 절사
						dataItem.saleRate = saleRate.toFixed(1);
/*
						if (dataItem.taxYn == "Y") { // 과세일 경우 계산 = (정상가 - 묶음상품 판매가(개당) * 1.1) / 정상가 * 100
							dataItem.marginRate = Math.round( (dataItem.recommendedPrice - dataItem.salePricePerUnit * 1.1) / dataItem.recommendedPrice * 100 );
						}
						else { // 면세일 경우 계산 = (정상가 - 묶음상품 판매가(개당)) / 정상가 * 100
							dataItem.marginRate = Math.round( (dataItem.recommendedPrice - dataItem.salePricePerUnit) / dataItem.recommendedPrice * 100 );
						}
*/
						if (dataItem.taxYn == "Y") { // 과세일 경우 계산 = (묶음상품 판매가(개당) - 원가(개당) * 1.1) / 묶음상품 판매가(개당) * 100
							dataItem.marginRate = Math.floor( (dataItem.salePricePerUnit - dataItem.standardPrice * 1.1) / dataItem.salePricePerUnit * 100 );
						}
						else { // 과세일 경우 계산 = (묶음상품 판매가(개당) - 원가(개당)) / 묶음상품 판매가(개당) * 100
							dataItem.marginRate = Math.floor( (dataItem.salePricePerUnit - dataItem.standardPrice) / dataItem.salePricePerUnit * 100 );
						}

						var indexId = $("#dGrid").data("kendoGrid").dataSource.indexOf(dataItem);	//현재 Grid Row ID

						//Grid에 수치 적용
						$("#dGrid tr td.discountPricePerGoods-cell:eq("+indexId+")").html(kendo.toString(dataItem.discountPricePerGoods, "n0"));
						$("#dGrid tr td.saleRate-cell:eq("+indexId+")").html(dataItem.saleRate + " %");
						$("#dGrid tr td.salePricePerQuantity-cell:eq("+indexId+")").html(kendo.toString(dataItem.salePricePerQuantity, "n0"));
						$("#dGrid tr td.marginRate-cell:eq("+indexId+")").html(dataItem.marginRate + " %");

						var arryAllInputValues = $("#dGrid input[name=salePricePerUnit]").map(function() {
							return $(this).val();
						}).toArray();

						var packageGoodsResultDatas = dGrid.dataSource.data().slice();
						var salePricePerUnitSum = 0;
						var discountPricePerGoodsSum = 0;
						var discountPriceGoodsSum = 0;
						var saleRateSum = 0;
						var marginRateAll = 0;

						if(arryAllInputValues.length > 0){
							for(var i=0; i < arryAllInputValues.length; i++){
								salePricePerUnitSum += Math.round(arryAllInputValues[i]) * packageGoodsResultDatas[i].goodsQuantity;								// 묶음상품 판매가(개당) * 구성수량의 합
								discountPricePerGoodsSum += Math.round(packageGoodsResultDatas[i].discountPricePerGoods);											// 할인액(개당)의 합
								discountPriceGoodsSum  += Math.round(packageGoodsResultDatas[i].discountPricePerGoods * packageGoodsResultDatas[i].goodsQuantity);	// 할인액의 합
								saleRateSum += packageGoodsResultDatas[i].saleRate;																					// 할인율 합
							}
						}

						var recommendedPackageTotalPrice = cGrid.dataSource.at(0).get("recommendedPackageTotalPrice");					// 정상가 총액

						saleRateSumAvg = 100 - (salePricePerUnitSum/recommendedPackageTotalPrice*100);									// 할인율 AVG(합계 금액 기준)
						saleRateSumAvg = saleRateSumAvg.toFixed(1);
//						saleRateSumAvg = Math.round(saleRateSum / arryAllInputValues.length);											// 할인율 AVG(항목 SUM 기준)

						//마진율은 기준상품 면세/과세 여부에 따라서 계산해야 하므로 합계 금액 기준으로만 처리 해야 함.
						var standardTaxYn = aGrid.dataSource.at(0).get("taxYn");
/*
						if (standardTaxYn == "Y") { // 과세일 경우 계산 = (정상가 총액 - 묶음상품 판매가 총액 * 1.1) / 정상가 총액 * 100
							marginRateAll = Math.round( (recommendedPackageTotalPrice - salePricePerUnitSum * 1.1) / recommendedPackageTotalPrice * 100 );
						}
						else { // 면세일 경우 계산 = (정상가 총액 - 묶음상품 판매가 총액)) / 정상가 총액 * 100
							marginRateAll = Math.round( (recommendedPackageTotalPrice - salePricePerUnitSum) / recommendedPackageTotalPrice * 100 );
						}
*/
						if (standardTaxYn == "Y") { // (묶음상품 판매가총액 - 원가총액 * 1.1) / 판매가총액 * 100
							marginRateAll = Math.round( (salePricePerUnitSum - cGrid.dataSource.at(0).get("standardTotalPrice") * 1.1) / salePricePerUnitSum * 100 );
						}
						else { // (묶음상품 판매가총액 - 원가총액) / 판매가총액 * 100
							marginRateAll = Math.round( (salePricePerUnitSum - cGrid.dataSource.at(0).get("standardTotalPrice")) / salePricePerUnitSum * 100 );
						}

						// 묶음상품 판매가(개당) 입력한 모든 값들의 합
						var resultValue = arryAllInputValues.reduce(function(accumulator, currentValue) {
							return accumulator * 1 + currentValue * 1;
						});

//						var html = kendo.toString(salePricePerUnitSum, "n0") + "<br>";
						var html = kendo.toString(salePricePerUnitSum, "n0");

						var goodsPackageSalePrice = cGrid.dataSource.at(0).get("goodsPackageSalePrice");  // 묶음상품 판매가 입력 그리드에서 입력한 묶음상품 판매가(묶음상품 전체 판매가)
						var diffSalePrice = salePricePerUnitSum - goodsPackageSalePrice; // 묶음상품 판매가 입력 그리드에서 입력한 묶음상품 판매가(묶음상품 전체 판매가)와 묶음상품 구성목록 그리드에서 입력한 모든 묶음상품 판매가(개당) 합과의 차액
						totalSalePriceGoods = salePricePerUnitSum;
						viewModel.ilGoodsDetail.set("totalSalePriceGoods",  salePricePerUnitSum);

/*
						if (diffSalePrice > 0){
							html += "<span style='color:#0000FF'>+" + kendo.toString(diffSalePrice, "n0") + "</span>";
						}
						else if(diffSalePrice == 0){
							html += "<span style='color:#0000FF'>" + kendo.toString(diffSalePrice, "n0") + "</span>";
						}
						else if (diffSalePrice < 0){
							html += "<span style='color:#FF0000'>" + kendo.toString(diffSalePrice, "n0") + "</span>";
						}
*/

						$("#dGrid tr.k-footer-template td:eq(10)").html(kendo.toString(discountPricePerGoodsSum, "n0"));	//할인액(개당) 합계 Cell
						$("#dGrid tr.k-footer-template td:eq(11)").html(kendo.toString(saleRateSumAvg, "n0") + " %");		//할인율합계 AVG Cell
						$("#dGrid tr.k-footer-template td:eq(13)").html(html);												//묶음상품 판매가 합계 Cell
						$("#dGrid tr.k-footer-template td:eq(14)").html(kendo.toString(marginRateAll, "n0") + " %");		//마진율합계 AVG Cell

						var tdElement = $(e.sender.element).closest("td");

						tdElement.prepend('<span class="k-dirty"></span>');
						tdElement.addClass("k-dirty-cell");

						$(this.element.context).blur();
						cGrid.dataSource.at(0).set("saleRate", Math.floor(saleRateSumAvg));				//묶음상품 판매가 입력 > 할인율 업데이트
						cGrid.dataSource.at(0).set("discountPrice", discountPriceGoodsSum);				//묶음상품 판매가 입력 > 할인액 업데이트
						cGrid.dataSource.at(0).set("marginRate", marginRateAll);						//묶음상품 판매가 입력 > 마진율 업데이트
						cGrid.dataSource.at(0).set("goodsPackageSalePrice", salePricePerUnitSum);		//묶음상품 판매가 입력 > 묶음상품 판매가 업데이트

						$("#goodsPackageSalePrice").data("kendoNumericTextBox").enable(false);
						$("#goodsPackageSalePrice").closest(".k-widget.k-numerictextbox").css("background-color", "#c0c0c0");
					}
				});
			});
		}

		//증정품 구성목록 입력 처리 시작
		eGridDs.data([]);		//증정품 구성목록 그리드 초기화

		if(bGrid.dataSource.data().length > 0){	//증정품을 하나라도 선택 했다면
			eGridDs.data(bGrid.dataSource.data().slice());
		}
		//증정품 구성목록 입력 처리 끝

		$("#goodsPackageSalePrice").data("kendoNumericTextBox").enable(false);
		$("#goodsPackageSalePrice").closest(".k-widget.k-numerictextbox").css("background-color", "#c0c0c0");

	}  // function fnGoodsAssemble()

	// 묶음 상품 이미지 추가 Html 을 상품 이미지 목록이 매핑되는 packageImageContainer 의 마지막에 동적 추가
	function fnAddPackageImageArea(goodsPackageImageType) {
		// 상품 이미지 최대 등록 개수 이상인 경우 return
		if(goodsPackageImageType == "GOODS_PACKAGE_IMG_TP.MIXED") {
			if( viewModel.get('packageImageMixList').length >= viewModel.get('packageImageMaxLimit') ) {
				return;
			}
		}
		else{
			if( viewModel.get('packageImageList').length >= viewModel.get('packageImageMaxLimit') ) {
				return;
			}
		}

		$('#packageImageAdd').remove();  // 기존 추가된 상품 이미지 추가 Html 제거

		var packageImageAddHtml = '<span id="packageImageAdd" class="goodsImageAdd" ';
		packageImageAddHtml += 'style="float: left; width: 150px; height: 150px; padding: 10px; margin-right: 10px; margin-bottom: 10px; position: relative; border-color: #a9a9a9; border-style: dashed; border-width: 5px" ';
		packageImageAddHtml += '> <span class="k-icon k-i-plus" '
		packageImageAddHtml += 'style="width: 100%; height: 100%; font-size: 75px; color: #a9a9a9;" ';
		packageImageAddHtml += '> </span></span>';

		$("#divContainer").append(packageImageAddHtml);

		//console.log("goodsPackageImageType : ", goodsPackageImageType);

		if(goodsPackageImageType == "GOODS_PACKAGE_IMG_TP.MIXED") {
			$("#packageImageAdd").on("click", function(event) {
				$('#uploadPackageImageMix').trigger('click'); // 상품 이미지 파일 input Tag 클릭 이벤트 호출
			});
		}
		else {
			$("#packageImageAdd").on("click", function(event) {
				$('#uploadPackageImage').trigger('click'); // 상품 이미지 파일 input Tag 클릭 이벤트 호출
			});
		}
	}


	// 상품공지 상세 하단공지 1 이미지 추가 Html 을 상품 이미지 목록이 매핑되는 divDetailBottomNotcie1Container 의 마지막에 동적 추가
	function fnAddDetailBottomNotcie1ImageArea() {

		$('#detailBottomNotcie1ImageAdd').remove();  // 기존 추가된 상품 이미지 추가 Html 제거

		var detailBottomNotcie1ImageAddHtml = '<span id="detailBottomNotcie1ImageAdd" class="goodsImageAdd" ';
		detailBottomNotcie1ImageAddHtml += 'style="float: left; width: 150px; height: 150px; padding: 10px; margin-right: 10px; margin-bottom: 10px; position: relative; border-color: #a9a9a9; border-style: dashed; border-width: 5px" ';
		detailBottomNotcie1ImageAddHtml += '> <span class="k-icon k-i-plus" '
		detailBottomNotcie1ImageAddHtml += 'style="width: 100%; height: 100%; font-size: 75px; color: #a9a9a9;" ';
		detailBottomNotcie1ImageAddHtml += '> </span></span>';

		// $("#detailBottomNotcie1ImageContainer").append(detailBottomNotcie1ImageAddHtml);
		$("#divDetailBottomNotcie1Container").append(detailBottomNotcie1ImageAddHtml);

		$("#detailBottomNotcie1ImageAdd").on("click", function(event) {
			$('#uploadDetailBottomNotcie1Image').trigger('click'); // 이미지 파일 input Tag 클릭 이벤트 호출
		});
	}


	// 상품공지 상세 하단공지 2 이미지 추가 Html 을 상품 이미지 목록이 매핑되는 divDetailBottomNotcie2Container 의 마지막에 동적 추가
	function fnAddDetailBottomNotcie2ImageArea() {

		$('#detailBottomNotcie2ImageAdd').remove();  // 기존 추가된 상품 이미지 추가 Html 제거

		var detailBottomNotcie2ImageAddHtml = '<span id="detailBottomNotcie2ImageAdd" class="goodsImageAdd" ';
		detailBottomNotcie2ImageAddHtml += 'style="float: left; width: 150px; height: 150px; padding: 10px; margin-right: 10px; margin-bottom: 10px; position: relative; border-color: #a9a9a9; border-style: dashed; border-width: 5px" ';
		detailBottomNotcie2ImageAddHtml += '> <span class="k-icon k-i-plus" '
		detailBottomNotcie2ImageAddHtml += 'style="width: 100%; height: 100%; font-size: 75px; color: #a9a9a9;" ';
		detailBottomNotcie2ImageAddHtml += '> </span></span>';

		// $("#detailBottomNotcie2ImageContainer").append(detailBottomNotcie2ImageAddHtml);
		$("#divDetailBottomNotcie2Container").append(detailBottomNotcie2ImageAddHtml);

		$("#detailBottomNotcie2ImageAdd").on("click", function(event) {
			$('#uploadDetailBottomNotcie2Image').trigger('click'); // 이미지 파일 input Tag 클릭 이벤트 호출
		});
	}

//	function fnSetPackageGoodsPrice(inputDom) {
//		var $inputDom = $(inputDom);
//
//		fnInputDomValidationForNumber(inputDom);
//
//		var dataItem = dGrid.dataItem($inputDom.closest("tr"));
//
//		inputDom.value = dataItem.get("salePricePerUnit");
//
//		$inputDom.on("change", function(event) {
//			dataItem.set("salePricePerUnit", this.value);
//			//console.log("##### ", $(inputDom).val());
//			//$(inputDom).val(dataItem.get("salePricePerUnit"));
//		});
//	}


	/**
	 * 영역 보이기 / 숨기기
	 * @param btnDom - 버튼 dom 객체
	 * @param startNo - table 에서 숨기기 시작하는 tr 번호 (default = 2 로 두번째 tr 부터 숨기기 시작한다)
	 * @param excpetionArea - 적용 예외 영역명
	 */
	function fnToggleArea(btnDom, startNo, excpetionArea) {

		var $btn = $(btnDom);
		var $table = $btn.closest("table");

		if ($btn.attr('data-flag') == "SHOW") {  // 테이블 tr 노출 상태에서 클릭시

			$btn.attr('data-flag',"HIDE"); // data-flag rkqt
			$btn.attr('class', 'k-icon k-i-arrow-chevron-down'); // "∨" 모양의 kendo icon 으로 전환

			if (excpetionArea) {
				$table.find("tbody").find("tr").not("[" + excpetionArea + "]").each(function(i) {
					if ((i + 1) >= startNo)
						$(this).hide();
				});
			} else
				$table.find("tbody").find("tr:nth-child(n+" + startNo + ")").hide();

		} else {   // 테이블 tr 숨김 상태에서 클릭시

			$btn.attr('data-flag',"SHOW");
			$btn.attr('class', 'k-icon k-i-arrow-chevron-up'); // "∧" 모양의 kendo icon 으로 전환

			if (excpetionArea) {
				$table.find("tbody").find("tr").not("[" + excpetionArea + "]").each(function(i) {
					if ((i + 1) >= startNo)
						$(this).show();
				});
			} else
				$table.find("tbody").find("tr:nth-child(n+" + startNo + ")").show();
		}
	}

	/**
	 * 상품정보 제공고시에서 상품 보이기 / 숨기기
	 * @param btnDom 버튼 dom 객체
	 * @param toggleAreaName display toggle 할 영역이름 (tr 에 지정 - (예) <tr toggleAreaName>)
	 */
	function fnToggleNameArea(btnDom, toggleAreaName) {
		var $btn = $(btnDom);

		$btn.closest("table").find("tr[" + toggleAreaName + "]").toggle();

		if ($btn.attr('data-flag') == "SHOW") {  // 테이블 tr 노출 상태에서 클릭시
			$btn.attr('data-flag',"HIDE"); // data-flag rkqt
			$btn.attr('class', 'k-icon k-i-arrow-chevron-down'); // "∨" 모양의 kendo icon 으로 전환
		}
		else {
			$btn.attr('data-flag',"SHOW");
			$btn.attr('class', 'k-icon k-i-arrow-chevron-up'); // "∧" 모양의 kendo icon 으로 전환
		}

//		if ($btn.html() == "∧")
//			$btn.html("∨");
//		else
//			$btn.html("∧");
	}


	/**
	 * 검색(기준상품 추가 팝업)
	 */
	function fnAddBaseGoodsPopup() {

		// ######### 개발용 임시 start ############
//		var url  = '/admin/goods/regist/getGoodsList';
//		var cbId = 'baseGoods';
//
//		var data = { ilGoodsIds : "15540" }
//
//		fnAjax({
//			url	 : url,
//			params  : data,
//			async	: false,
//			success :
//				function( data ){
//					fnBizCallback(cbId, data);
//				},
//				isAction : 'batch'
//		});
		// ######### 개발용 임시 end ############


		// ######### 개발 완료 후 팝업을 여는 실제 적용할 소스 start ############
		 // 상품검색 팝업
		var params = {};
		params.selectType = "single"; // 그리드 체크박스 타입 ( single : 하나만 선택됨, multi : 여러개 선택됨 )
		params.goodsType = "GOODS_TYPE.NORMAL,GOODS_TYPE.DISPOSAL";	// 상품유형(복수 검색시 , 로 구분)
		params.chkGoodsOverlapYn = "Y";

		let goodsIds = [];
		let aGridData = aGrid.dataSource.data().slice();

		for (var i = 0; i < aGridData.length; i++) {  // 정상가총액, 판매가 총액, 원가 총액 계산
			goodsIds.push(aGridData[i].id);
		}
		params.goodsIds = goodsIds;

		params.goodsCallType = "basePackageGoods";
		fnKendoPopup({
			id		 : "goodsSearchPopup",
			title	  : "기준 상품 선택",
			width	  : "1590px",
			height	 : "800px",
			minWidth : "1000px",
			maxWidth : "1590px",
			scrollable : "yes",
			src		: "#/goodsSearchPopup",
			param	  : params,
			success	: function( id, data ){

				if ($.trim(gblAlertMessage) != "") {
					fnKendoMessage({message: gblAlertMessage});
				}


				if (data.length > 0) {
					var url  = '/admin/goods/regist/getGoodsList';
					var cbId = 'baseGoods';

					var data = { ilGoodsIds : data[0].goodsId }

					fnAjax({
						url	 : url,
						params  : data,
						async	: false,
						success :
							function( data ){
								fnBizCallback(cbId, data);
							},
							isAction : 'batch'
					});
				}
			}
		});
		// ######### 개발 완료 후 팝업을 여는 실제 적용할 소스 end ############
	}

	/**
	 * 묶음상품 추가 팝업
	 **/
	function fnAddGoodsPopup() {
		var baseGoodsSelFlag = checkBaseGoods();		//기준 상품 등록 여부

		if (!baseGoodsSelFlag)
			return false;

		// ######### 개발용 임시 start ############
//		var url  = '/admin/goods/regist/getGoodsList';
//		var cbId = 'goodsList';
//
//		var data = { ilGoodsIds : "15649, 15623" }
//
//		fnAjax({
//			url	 : url,
//			params  : data,
//			async	: false,
//			success :
//				function( data ){
//					fnBizCallback(cbId, data);
//				},
//				isAction : 'batch'
//		});
		// ######### 개발용 임시 end ############


		// ######### 개발 완료 후 팝업을 여는 실제 적용할 소스 start ############
		var params = {};
		params.selectType = "multi"; // 그리드 체크박스 타입 ( single : 하나만 선택됨, multi : 여러개 선택됨 )
		params.goodsType = "GOODS_TYPE.NORMAL,GOODS_TYPE.DISPOSAL"; // 상품유형(복수 검색시 , 로 구분)
        params.goodsCallType = "packageGoods";
		params.supplierId = viewModel.ilGoodsDetail.urSupplierId;
		params.warehouseId = viewModel.ilGoodsDetail.urWarehouseId;
		params.warehouseGrpCd = viewModel.ilGoodsDetail.warehouseGrpCd;
		params.chkGoodsOverlapYn = "Y";

		let goodsIds = [];
		let aGridData = aGrid.dataSource.data().slice();

		for (var i = 0; i < aGridData.length; i++) {  // 정상가총액, 판매가 총액, 원가 총액 계산
			goodsIds.push(aGridData[i].id);
		}
		params.goodsIds = goodsIds;


		fnKendoPopup({
			id		 : "goodsSearchPopup",
			title	  : "묶음 상품 선택",
			width	  : "1590px",
			height	 : "800px",
			minWidth : "1000px",
			maxWidth : "1590px",
			scrollable : "yes",
			src		: "#/goodsSearchPopup",
			param	  : params,
			success	: function( id, data ){
				//console.log("############ data; ", data);

				if ($.trim(gblAlertMessage) != "" && data.length > 1) {
					fnKendoMessage({message: gblAlertMessage});
				}
				if (data.length > 0) {
					var goodsIds = [];

					for (var i = 0; i < data.length; i++)
						goodsIds.push(data[i].goodsId);

					var url  = '/admin/goods/regist/getGoodsList';
					var cbId = 'goodsList';

					var data = { ilGoodsIds : goodsIds.join(",") }

					//console.log("묶음상품 선택 Data", data);

					fnAjax({
						url	 : url,
						params  : data,
						async	: false,
						success :
							function( data ){
								//console.log("묶음상품 선택 Data", data);
								fnBizCallback(cbId, data);
							},
							isAction : 'batch'
					});
				}
			}
		});
		// ######### 개발 완료 후 팝업을 여는 실제 적용할 소스 end ############

	}

	/**
	 * 증정품 추가 팝업
	 **/
	function fnAddGoodsGiftSearchPopup() {
		var baseGoodsSelFlag = checkBaseGoods();		//기준 상품 등록 여부

		if (!baseGoodsSelFlag)
			return false;

		// ######### 개발용 임시 start ############
//		var url  = '/admin/goods/regist/getGoodsList';
//		var cbId = 'goodsGiftList';
//
//		var data = { ilGoodsIds : "10000046" }
//
//		fnAjax({
//			url	 : url,
//			params  : data,
//			async	: false,
//			success :
//				function( data ){
//					fnBizCallback(cbId, data);
//				},
//				isAction : 'batch'
//		});
		// ######### 개발용 임시 end ############

		var params = {};
		params.selectType = "multi"; // 그리드 체크박스 타입 ( single : 하나만 선택됨, multi : 여러개 선택됨 )
		params.goodsType = "GOODS_TYPE.GIFT,GOODS_TYPE.GIFT_FOOD_MARKETING";	// 상품유형(복수 검색시 , 로 구분)
		params.supplierId = viewModel.ilGoodsDetail.urSupplierId;
		params.warehouseId = viewModel.ilGoodsDetail.urWarehouseId;
		params.goodsCallType = "giftGoods";
		fnKendoPopup({
			id			: "goodsSearchPopup",
			title		: "증정품 선택",
			width		: "1590px",
			height		: "800px",
			minWidth : "1000px",
			maxWidth : "1590px",
			scrollable	: "yes",
			src			: "#/goodsSearchPopup",
			param		: params,
			success		: function( id, data ){
				//console.log("############ data; ", data);
				if (data.length > 0) {
					var goodsIds = [];

					for (var i = 0; i < data.length; i++){
						goodsIds.push(data[i].goodsId);
					}

					var url  = '/admin/goods/regist/getGoodsList';
					var cbId = 'goodsGiftList';
					var data = { ilGoodsIds : goodsIds.join(",") }
					//console.log("증정품 선택 Data", data);

					fnAjax({
						url	 : url,
						params  : data,
						async	: false,
						success :
							function( data ){
								fnBizCallback(cbId, data);
							},
							isAction : 'batch'
					});
				}
			}
		});
	}


	/**
	 * 기준상품 선택 여부 검사
	 */
	function checkBaseGoods() {
		if (!$("#baseGoodsId").val()) {
			fnKendoMessage({message: '<span style="color: red;font-size: 18pt;font-weight: bolder;">[기준 상품] </span>을 선택해 주십시오.'});

			return false;
		} else {
			return true;
		}
	}


	 /**
	  * 상품 관련 정보 삭제
	  * @param goodsId 상품 아이디
	  */
	 function removeGoodsInfo(goodsId) {
		$("tr[goodsInfo_goodsId=" + goodsId + "]").remove();  // 상품정보 제공고시 삭제
		$("tr[goodsNutrition_goodsId=" + goodsId + "]").remove();  // 상품영양정보 삭제

		if ($("#goodsInfoList tr").length == 0)
			$("#goodsInfoList").html('<tr noDataRow><td colspan="2" style="text-align:center">-</td></tr>');
		if ($("#goodsNutritionList tr").length == 0)
			$("#goodsNutritionList").html('<tr noDataRow><td colspan="6" style="text-align:center">-</td></tr>');
	 }


	// 상품 이미지 정렬 순서 반환 : [ "파일명", .. ] 형식의 배열 반환, 배열의 index 가 정렬 순서
	function fnGetImageOrder(containerId) {
		var packageImageArray = $(containerId+" .goodsImage");
		var packageImageFileNameArray = [];

		for (var i = 0; i < packageImageArray.length; i++) {
			var fileName = $(containerId+" .goodsImage:nth-child(" + (i + 1) + ")").attr('data-id');
			packageImageFileNameArray.push(fileName);
		}

		return packageImageFileNameArray;
	}


	// 묶음상품 판매가 입력 그리드(cGgrid) 에 가격총액 계산학고 할인율,할인액,묶음상품판매가, 마진율 게산하고 묶은상품 구성목록 그리드(dGrid) 초기화
	function calculateGoodsPriceRate() {

		var aGridData = aGrid.dataSource.data().slice();
		var recommendedPackageTotalPrice = 0;  // 모든 선택 상품 정상가 총액
		var saleTotalPrice = 0; // 모든 선택 상품 판매가 총액
		var standardTotalPrice = 0; // 모든 선택 상품 원가 총액
		var taxYn = null;  // 과제구분(Y/N)

		for (var i = 0; i < aGridData.length; i++) {  // 정상가총액, 판매가 총액, 원가 총액 계산
			recommendedPackageTotalPrice += aGridData[i].recommendedTotalPrice;  // 정상가 총액
			saleTotalPrice += (aGridData[i].salePrice * aGridData[i].purchaseQuanity);  // 판매가 총액
			standardTotalPrice += (aGridData[i].standardPrice * aGridData[i].purchaseQuanity);  // 원가 총액

			if (i == 0)  // 기준상품 과세구분
				taxYn = aGridData[i].taxYn;
		}

		var dataItem = cGrid.dataSource.at(0);

		// 마진율 계산
		if (taxYn == "Y")  // 과세일 경우 계산 = (정상가 - 원가 * 1.1) / 정상가 * 100
			dataItem.set("marginRate", Math.round( (recommendedPackageTotalPrice - standardTotalPrice * 1.1) / recommendedPackageTotalPrice * 100 ));
		else  // 면세일 경우 계산 = (정상가 - 원가) / 정상가 * 100
			dataItem.set("marginRate", Math.round( (recommendedPackageTotalPrice - standardTotalPrice) / recommendedPackageTotalPrice * 100 ));

		dataItem.set("recommendedPackageTotalPrice", recommendedPackageTotalPrice);  // 정상가 총액
		dataItem.set("saleTotalPrice", saleTotalPrice);  // 판매가 총액
		dataItem.set("standardTotalPrice", standardTotalPrice);  // 원가 총액
		dataItem.set("goodsPackageSalePrice", recommendedPackageTotalPrice);		//묶음상품 예상판매가를 정상가 총액으로 입력
		dataItem.set("saleRate", "0");			// 할인율은 정상가 이므로 0
		dataItem.set("discountPrice", "0");		// 할인액은 정상가 이므로 0

		dataItem.set("marginRateAll", "");
		if ($("#goodsPackageImageType").find("input[type=radio]:checked").val() == "GOODS_PACKAGE_IMG_TP.NORMAL_GOODS") {  // 상품 상세 이미지 타입이 개별상품 전용일 경우
			viewModel.set("packageImageCount", aGridData.length);  // 상품 상세 이미지 카운트 수
			viewModel.set("packageImageMaxLimit", aGridData.length);  // 상품 상세 이미지 최대 수
		}

		dGrid.dataSource.data([]);	// 묶음상품 구성목록 초기화
		eGrid.dataSource.data([]);	// 증정품 구성목록 초기화
		$("#dGrid tr.k-footer-template td:eq(13)").html("0 %"); // 2021.01.31 이명수 묶음상품 구성목록 마진율 합계 초기화
	}

	/*
	 * Kendo Grid 전용 rowSpan 메서드
	 *
	 * @param gridId : div 로 지정한 그리드 ID, 해당 div 내 table 태그를 찾아감
	 * @param mergeColumns : 그리드에서 셀 머지할 컬럼들의 data-field 목록
	 * @param groupByColumns : group by 할 컬럼들의 data-field 목록, 해당 group 내에서만 셀 머지가 일어남
	 *
	 */
	function mergeGridRows(gridId, mergeColumns, groupByColumns) {

		if( $('#' + gridId + ' > table > tbody > tr').length <= 1 ) { // 데이터 1건 이하인 경우 : rowSpan 불필요하므로 return
			return;
		}

		var groupByColumnIndexArray = [];  // group by 할 컬럼들의 th 헤더 내 column index 목록
		var tdArray = [];  // 해당 컬럼의 모든 td 배열, 개수 / 순서는 그리드 내 tr 개수 / 순서와 같음
		var groupNoArray = [];  // 파라미터로 전달된 groupByColumns 에 따라 계산된 그룹번호 배열, 같은 그룹인 경우 그룹번호 같음, 개수 / 순서는 tdArray 와 같음

		var groupNo;  // 각 tr 별 그룹번호, 같은 그룹인 경우 그룹번호 같음
		var beforeTr = null; // 이전 tr
		var beforeTd = null; // 이전 td
		var rowspan = null; // rowspan 할 개수, 1 인경우 rowspan 하지 않음

		var thRow = $('#' + gridId + ' > table > thead > tr')[0];  // 해당 그리드의 th 헤더 row

		// 셀 머지시 group by 할 컬럼들의 data-field 목록이 Array 형태의 파라미터로 전달시
		if( groupByColumns && Array.isArray(groupByColumns) && groupByColumns.length > 0 ) {
			$(thRow).children('th').each(function (thIndex, th) {  // thead 의 th 반복문 시작 : table 내 각 컬럼별로 반복
				// groupByColumns => groupByColumnIndexArray 로 변환
				if( groupByColumns.includes( $(th).attr('data-field') ) ) {
					groupByColumnIndexArray.push(thIndex);
				}
			});
		} // if 문 끝

		$('#' + gridId + ' > table > tbody > tr').each(function() {  // tbody 내 tr 반복문 시작
			beforeTr = $(this).prev();  // 이전 tr

			if( beforeTr.length == 0 ) {  // 첫번째 tr 인 경우 : 이전 tr 없음
				groupNo = 0;  // 그룹번호는 0 부터 시작
				groupNoArray.push(groupNo); // 첫번째 tr 의 그룹번호 push

			} else {
				var sameGroupFlag = true;  // 이전 tr 과 비교하여 같은 그룹인지 여부 flag, 기본값 true

				for( var i in groupByColumnIndexArray ) {
					var groupByColumnIndex = groupByColumnIndexArray[i];  // groupByColumns 로 전달된 각 column 의 index

					// 이전 tr 과 현재 tr 비교하여 group by 기준 컬럼의 html 값이 하나라도 다른 경우 flag 값 false ( 같은 그룹 아님 )
					if( $(this).children().eq(groupByColumnIndex).html() != $(beforeTr).children().eq(groupByColumnIndex).html() ) {
						sameGroupFlag = false;
					}
				}

				if( ! sameGroupFlag ) {  // 이전 tr 의 값과 비교하여 같은 그룹이 아닌 경우 : groupNo 1 증가시킴
					groupNo++;
				}

				groupNoArray.push(groupNo); // 해당 tr 의 그룹번호 push

			}

		});	// tbody 내 tr 반복문 끝

		$(thRow).children('th').each(function (thIndex, th) {  // thead 의 th 반복문 시작 : table 내 각 컬럼별로 반복

			if( ! mergeColumns.includes( $(th).attr('data-field') ) ) {
				return true;   // mergeColumns 에 포함되지 않은 컬럼인 경우 continue
			}

			tdArray = [];  // 값 초기화
			beforeTd = null;
			rowspan = null;

			var colIdx = $("th", thRow).index(this);  // 해당 컬럼 index

			$('#' + gridId + ' > table > tbody > tr').each(function() {  // tbody 내 tr 반복문 시작
				var td = $(this).children().eq(colIdx);
				tdArray.push(td);
			});  // tbody 내 tr 반복문 끝

			for( var i in tdArray ) {  // 해당 컬럼의 td 배열 반복문 시작
				var td = tdArray[i];

				if ( i > 0 && groupNoArray[i-1] == groupNoArray[i] && $(td).html() == $(beforeTd).html() ) {

					rowspan = $(beforeTd).attr("rowSpan");

					if ( rowspan == null || rowspan == undefined ) {
						$(beforeTd).attr("rowSpan", 1);
						rowspan = $(beforeTd).attr("rowSpan");
					}

					rowspan = Number(rowspan) + 1;

					$(beforeTd).attr("rowSpan",rowspan);
					$(td).hide(); // .remove(); // do your action for the old cell here

				} else {
					beforeTd = td;
				}

				beforeTd = ( beforeTd == null || beforeTd == undefined ) ? td : beforeTd; // set the that if not already set

			}  // 해당 컬럼의 td 배열 반복문 끝
		});  // thead 의 th 반복문 끝
	}

	function fnPackageImageList(goodsPackageImageType){
//		viewModel.set('packageImageList', []);		// 묶음 상품 이미지 영역에 출력되는 이미지 정보 목록
//		viewModel.set('packageImageFileList', []);	// 묶음 상품 이미지 파일 목록
		viewModel.set('packageImageCount', 0);		// 묶음 상품 이미지 영역에 추가된 이미지 개수

		//console.log("fnPackageImageList > goodsPackageImageType : ", goodsPackageImageType);

		$("#packageImageContainer").hide();
		$("#mixPackageImageContainer").hide();
		$("#goodsImageContainerDiv").hide();
		$("#goodsImageContainer").hide();
		$("#packageImageAdd").hide();

		if (goodsPackageImageType == "GOODS_PACKAGE_IMG_TP.PACKAGE_GOODS") {
			$("#packageImageContainer").show();
			$("#mixPackageImageContainer").hide();
			viewModel.set('packageImageCount', viewModel.get("packageImageList").length);
			viewModel.set("packageImageMaxLimit", viewModel.get("onlyPackageImageMaxLimit"));
			fnAddPackageImageArea(goodsPackageImageType);
		} else if (goodsPackageImageType == "GOODS_PACKAGE_IMG_TP.MIXED") {
			$("#packageImageContainer").hide();
			$("#mixPackageImageContainer").show();
			viewModel.set('packageImageCount', viewModel.get("packageImageMixList").length);
			viewModel.set("packageImageMaxLimit", viewModel.get("mixedImageMaxLimit"));
			fnAddPackageImageArea(goodsPackageImageType);
			$("#goodsImageContainer").show();
			$("#goodsImageContainerDiv").show();
		} else if (goodsPackageImageType == "GOODS_PACKAGE_IMG_TP.NORMAL_GOODS") {
			$("#goodsImageContainerDiv").hide();
			$("#goodsImageContainer").show();
			if(viewModel.pageMode == "create"){
				viewModel.set("packageImageCount", aGridDs.data().length);
				viewModel.set("packageImageMaxLimit", aGridDs.data().length);
			}
			else{
				var goodsPackageGoodsMappingDatas = goodsPackageGoodsMappingGrid.dataSource.data().slice();		// 묶음상품 선택 그리드의 데이타
				var imageCount = 0;
				if(goodsPackageGoodsMappingDatas.length > 0){
					for (var i = 0; i < goodsPackageGoodsMappingDatas.length; i++){
						if(goodsPackageGoodsMappingDatas[i].goodsType != "GOODS_TYPE.GIFT" && goodsPackageGoodsMappingDatas[i].goodsType != "GOODS_TYPE.GIFT_FOOD_MARKETING"){	//증정품은 상품 이미지에 포함되지 않음
							imageCount++;
						}
					}
				}

				viewModel.set("packageImageCount", imageCount);
				viewModel.set("packageImageMaxLimit", imageCount);
			}
		}
	}


	function fnGoodsImageList(goodsPackageImageList){
		var addImageCount = 0;
		var goodsPackageImageType = $("#goodsPackageImageType").find("input[type=radio]:checked").val();

		if (goodsPackageImageList && goodsPackageImageList.length > 0) {

			// 마스터 품목 복사시 기존 이미지 목록 초기화
			viewModel.set('packageImageList', [] );
			viewModel.set('packageImageFileList', [] );
			viewModel.set('packageImageMixList', [] );
			viewModel.set('packageImageMixFileList', [] );

			var xhrArray = [];

			// 1차 반복문 : 이미지 파일 다운로드용 XMLHttpRequest 배열 생성
			for (var i = 0; i < goodsPackageImageList.length; i++) {
				var imageSrcUrl = publicStorageUrl + goodsPackageImageList[i]['imagePath']; // 품목 이미지 url
				var imagePhysicalName = goodsPackageImageList[i]['imagePhysicalName']; //
				var sort = goodsPackageImageList[i]['sort']; // 정렬순서

				var xhr = new XMLHttpRequest();
				xhr.open("GET", imageSrcUrl);
				xhr.responseType = "blob";
				xhr.fileName = imagePhysicalName; // 마스터 품목 복사로 조회한 원본 이미지의 물리적 파일명
				xhr.sort = sort;

				xhrArray.push(xhr);
			}

			// 2차 반복문 : XMLHttpRequest 전송 후 품목 이미지 다운로드
			for (var i = 0; i < xhrArray.length; i++) {

				var xhr = xhrArray[i];

				xhr.onload = function(e) {

					if (this.status == 200) {
						var blob = this.response;
						var blobUrl = window.URL.createObjectURL(blob);

						var file = new File([ blob ], this.fileName, {
							type : blob.type,
							lastModified : new Date()
						});

						// blob => file 로 변환한 상품 이미지 객체를 전역변수에 추가
						// packageImageFileList 에 push 시에는 원본 이미지의 정렬 순서와 상관없음
						//// viewModel.get('packageImageFileList').push(file);

						if(goodsPackageImageType == "GOODS_PACKAGE_IMG_TP.MIXED"){
							// 해당 정렬 순서의 index 에 이미지 정보 추가
							viewModel.get('packageImageMixList').splice(this.sort, 0, {
								basicYn : '', // 대표 이미지 여부
								imagePath : this.fileName, //
								imageOriginalName : this.fileName, //
								sort : this.sort, // 정렬순서
								imageSrc : blobUrl, // 상품 이미지 url
							});

							viewModel.get('packageImageMixList').sort(function(x, y) {
								return x.sort == y.sort ? 0 : (x.sort > y.sort ? 1 : -1);
							});

							viewModel.trigger("change", {
								field : "packageImageList"
							});
						}
						else if(goodsPackageImageType == "GOODS_PACKAGE_IMG_TP.PACKAGE_GOODS"){
							// 해당 정렬 순서의 index 에 이미지 정보 추가
							viewModel.get('packageImageList').splice(this.sort, 0, {
								basicYn : '', // 대표 이미지 여부
								imagePath : this.fileName, //
								imageOriginalName : this.fileName, //
								sort : this.sort, // 정렬순서
								imageSrc : blobUrl, // 상품 이미지 url
							});

							viewModel.get('packageImageList').sort(function(x, y) {
								return x.sort == y.sort ? 0 : (x.sort > y.sort ? 1 : -1);
							});

							viewModel.trigger("change", {
								field : "packageImageList"
							});
						}

						addImageCount++;

						// 등록된 상품 이미지 개수 갱신
						if(goodsPackageImageType == "GOODS_PACKAGE_IMG_TP.MIXED"){
							viewModel.set('packageImageCount', viewModel.get('packageImageMixList').length);

							if( viewModel.get('packageImageMixList').length >= viewModel.get('packageImageMaxLimit')) {
								$("#packageImageAdd").hide();
							}
						}
						else{
							viewModel.set('packageImageCount', viewModel.get('packageImageList').length);

							if( viewModel.get('packageImageList').length >= viewModel.get('packageImageMaxLimit')) {
								$("#packageImageAdd").hide();
							}
						}

						// 이미지 최대 등록가능 건수보다 복사할 총 이미지 개수가 작고 마지막 이미지 추가시 : 상품 추가 영역 append
						if( viewModel.get('packageImageMaxLimit') > xhrArray.length && addImageCount == xhrArray.length) {
							fnAddPackageImageArea(goodsPackageImageType);
						}

					} else { // 404 등 에러 발생시
						// 등록된 상품 이미지 개수 갱신
						viewModel.set('packageImageCount', viewModel.get('packageImageList').length);

						// 상품 이미지 등록 영역 추가
						fnAddPackageImageArea(goodsPackageImageType);

					}

				}

				xhr.send();
			}

		}
	}

	// ------------------------------------------------------------------------
	// 상품상세 미리보기 링크
	// ------------------------------------------------------------------------
	function fnBtnGoodsPreview() {
		var mallUrl = CUR_SERVER_URL + "/shop/goodsView?goods=" + ilGoodsId+"&preview=Y";
		window.open(mallUrl);
	}
	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	//$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	//$scope.fnClear =function(){	 fnClear();	};
	/** Common New*/
	//$scope.fnNew = function( ){	fnNew();};
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	/** Common Delete*/
	//$scope.fnDel = function(){	 fnDel();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	/** 전시 카테고리 분류 선택 */
	$scope.fnCtgrySelect = function( ){ fnCtgrySelect(); };
	/** 몰인몰 카테고리 분류 선택 */
	$scope.fnMallInMallCtgrySelect = function( ){ fnMallInMallCtgrySelect(); };
	/** 상품 조합*/
	$scope.fnGoodsAssemble = function( ){  fnGoodsAssemble();};
	/** 영역 보이기 / 숨기기 */
	$scope.fnToggleArea = function( btnDom, startNo, excpetionArea ){  fnToggleArea(btnDom, startNo, excpetionArea);};
	/** 상품정보 제공고시에서 상품 보이기 / 숨기기 */
	$scope.fnToggleNameArea = function( btnDom, toggleAreaName ){  fnToggleNameArea(btnDom, toggleAreaName);};
	/** 검색(대표상품 선택) */
	$scope.fnAddBaseGoodsPopup = function( ) { fnAddBaseGoodsPopup(); };
	/** 상품추가 */
	$scope.fnAddGoodsPopup = function( ) { fnAddGoodsPopup(); };
	/** 증정품 추가 */
	$scope.fnAddGoodsGiftSearchPopup = function () { fnAddGoodsGiftSearchPopup(); };
	/** 초기화 Confirm */
	$scope.fnGoodsPackageResetConfirm = function () { fnGoodsPackageResetConfirm(); }
	/** 초기화 */
	$scope.fnGoodsPackageReset = function () { fnGoodsPackageReset(); }
	//상품공지 > 상세 상단공지 첨부파일
	$scope.fnNoticeBelow1File = function( ){ fnNoticeBelow1File(); };
	//상품공지 > 상세 하단공지 첨부파일
	$scope.fnNoticeBelow2File = function( ){ fnNoticeBelow2File(); };

	$scope.fnGoodsPackagePricePopup = function(priceKind){		//가격 정보 > 판매 가격정보 > 변경이력 보기
		fnGoodsPackagePricePopup(priceKind);
	};

	$scope.fnGoodsPackageDiscountDetailPopup = function(discountTypeCode, discountTypeCodeName){	//가격 정보 > 행사/할인 내역 > 상세내역
		fnGoodsPackageDiscountDetailPopup(discountTypeCode, discountTypeCodeName);
	};

	$scope.fnGoodsPackageDiscountPopup = function(discountTypeCode, discountTypeCodeName){			//가격 정보 > 행사/할인 내역 > 할인설정
		fnGoodsPackageDiscountPopup(discountTypeCode, discountTypeCodeName);
	};

	$scope.fnGoodsPackageEmployeeDiscountHistoryPopup = function(historyKind){			//임직원 할인 정보 > 임직원 개별할인 정보 > 변경이력 보기
		fnGoodsPackageEmployeeDiscountHistoryPopup(historyKind);
	};

	// 상품 상세 이미지 다운로드 팝업
	$scope.fnOpenDownloadPopup = function() {
		const goodsName = viewModel.ilGoodsDetail.goodsName;

		// 팝업 옵션
		const popupOption = {
                id: "goodsDetailImagePopup",
                title: "이미지 다운로드",
//                src: "#/inputMulti30",
                src: "#/goodsDetailImagePopup",
                param: {
                	ilGoodsId: ilGoodsId,
                	goodsName: goodsName,
                },
                width: "1200px",
                height: "1200px",
                success: function (id, data) {
                	console.log(id, data);
                },
                key: "4355",
                nullMsg: "이미지 다운로드",
        };

		fnKendoPopup(popupOption);
	}

	// 상품상세 미리보기
	$scope.fnBtnGoodsPreview = function () {
		fnBtnGoodsPreview();
	};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
