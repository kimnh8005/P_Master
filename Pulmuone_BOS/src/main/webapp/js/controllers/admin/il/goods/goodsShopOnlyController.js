/*******************************************************************************************************************************************************************************************************
 * --------------- description : 상품 관리 - 매장전용 상품 등록 @ ----------------------------
 ******************************************************************************************************************************************************************************************************/
'use strict';


var viewModel;																		//viewModel 전역변수
var firstIlGoodsDetailData = {};
var pageParam = fnGetPageParam();													//GET Parameter 받기
var ilGoodsId = pageParam.ilGoodsId;												//상품 ID
var ilItemCode, urWarehouseId;														//품목코드, 출고처 ID
var urSupplierId, supplierCode, urBrandId;											//공급처ID, 공급처Code, 브랜드 ID
var erpItemLinkYn;																	//ERP 품목연동 여부
var ilItemWarehouseId, stockOrderYn;												//추가상품 재고 상세보기 호출 파라미터 관련
var CUR_SERVER_URL = fnGetServerUrl().mallUrl;										// 상품상세 미리보기 위한 URL

var ilGoodsDisplayCategoryList = null;												//저장된 전시 카테고리 리스트
var ilGoodsMallinmallCategoryList = null;											//저장된 몰인몰 카테고리 리스트

var exhibitGiftGridDs, exhibitGiftGridOpt, exhibitGiftGrid;		//혜택/구매정보 > 증정 행사 Grid
var supplierName;																	//기본정보 > 몰인몰 카테고리 Display관련 전역변수
var noticeBelowImageUploadMaxLimit = 512000;										//첨부파일 최대 용량
// PJH Start
var allowedImageExtensionList = ['.jpg', '.jpeg', '.gif']; // 업로드 가능한 이미지 확장자 목록
var promotionNameMaxByteLength = 12;  // 프로모션 상품명 최대 입력 가능 길이 ( 단위 : byte )
var searchKeywordMaxCount = 20;  // 검색 키워드 최대 등록 가능 건수
var oldSearchKeywordValue;  // 검색 키워드 이전 입력값
// PJH End

var undeliverableAreaType															//혜택/구매정보 > 추가상품 > 상품검색 팝업 고정 파라미터 값(공급처 ID, 배송불가지역 ID)

var publicStorageUrl = fnGetPublicStorageUrl();										//이미지 BASE URL

var mallinmallCategoryId = null														//브랜드가 잇슬림, 베이비밀에 해당하는 카테고리 정보

/* 카테고리 관련 변수 */
var ctgryGrid1, ctgryGrid2, ctgryGrid3, ctgryGrid4;
var ctgryGridDs1, ctgryGridDs2, ctgryGridDs3, ctgryGridDs4;
var ctgryGridOpt, ctgryGrid, ctgryGridDs;

//몰인몰 카테고리 관련 변수
var mallInMallCtgryGrid1, mallInMallCtgryGrid2, mallInMallCtgryGrid3, mallInMallCtgryGrid4;
var mallInMallCtgryGridDs1, mallInMallCtgryGridDs2, mallInMallCtgryGridDs3, mallInMallCtgryGridDs4;
var mallInMallCtgryGridOpt, mallInMallCtgryGrid, mallInMallCtgryGridDs;

//HGRM-1646 초기화 및 시간달력포맷 공통화 수정

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

	var promoLastDate = new Date(2099,11,31,23,59,0);
	var promoLastDateTime = promoLastDate.oFormat(FULL_DATE_FORMAT);

	var defaultStartTime =  fnGetToday(FULL_DATE_FORMAT);
	var defaultEndTime = "2999-12-31 23:59";
	var noticeEndTime =  fnGetDayAdd(defaultStartTime, 1, FULL_DATE_FORMAT);

    let nowDate = fnGetToday();

    var cacheData = {};

	//fnInitialize(); // Initialize Page Call
	//전시카테고리, 몰인몰 카테고리 공통 Func
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

	// Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', {
			flag : 'true'
		});

		fnPageInfo({
			PG_ID : 'goodsShopOnly',
			callback : fnDuplicateGoods
		});
	};

	/**
	 * functions 2020.10.06
	 */

	function setCacheData() {
		cacheData = !isEmpty(viewModel.ilGoodsDetail) ?  Object.assign(cacheData, viewModel.ilGoodsDetail) : { };
	}

	//데이터 비었는지 체크하는 함수
	function isNotValidData(data) {
		if( data === undefined || typeof data === "undefined" || data === null || data === "" ) return true;
		else return false;
	}

	function isEmpty(obj){
		return Object.keys(obj).length === 0;
	}

	//시간 비교 // 숫자 또는 UTCString만
	function compareTime(a, b) {
		return Number(a) - Number(b);
	}

	function isEqualTime(a, b) {
		return compareTime(a, b) === 0;
	}

	function isBiggerTime(a, b) {
		return compareTime(a, b) > 0;
	}

	function isSmallerTime(a, b) {
		return compareTime(a, b) < 0;
	}

	/**/

	//동일한 품목ID 출고처ID, 상품유형을 가진 상품이 있다면 등록 제한
	function fnDuplicateGoods(){
		ilItemCode = pageParam.ilItemCode;
		urWarehouseId = pageParam.urWarehouseId;
		let paramGoodsType = "GOODS_TYPE.SHOP_ONLY";

		if(ilItemCode != undefined && ilItemCode != ""){	//상품 등록이라면
			fnAjax({
				url		: '/admin/goods/regist/duplicateGoods',
				params	: {ilItemCode : ilItemCode, urWarehouseId : urWarehouseId, goodsType : paramGoodsType},
				async	: false,
				contentType : 'application/json',
				isAction : 'select',
				success	: function(data){
					//console.log("duplicateGoods data", data);
					if(data.ilGoodsId){
						fnKendoMessage({ message : data.returnMessage, ok : function (){
							location.href = "/layout.html#/goodsShopOnly?ilGoodsId="+data.ilGoodsId;
						}});
						return;
					}
					else{
						fnViewModelSetting();		// viewModel 세팅
						fnInitButton();				// Initialize Button
						fnInitGoodsShopOnly();			// Kendo Component 초기화
						init();
						fnExhibitGiftGrid();		// 혜택/구매정보 > 증정 행사 Grid
						fnDefaultValue();			// 등록 or 수정에 따른 기본 세팅
						setCacheData();
						fnGoodsAuthInputAllow()
					}
				}
			});
		}

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
							message : "해당 상품은 매장전용 상품이 아닙니다.",
							ok : function (){
								history.back();
								return;
						}});
					}
					else if(data.ilGoodsId == null) {
						fnKendoMessage({
							message : "회원님의 공급업체나 출고처 상품이 아닙니다.",
							ok : function (){
								location.href = "#/goods";
								return;
						}});
					}
					else{
						fnViewModelSetting();		// viewModel 세팅
						fnInitButton();				// Initialize Button
						fnInitGoodsShopOnly();			// Kendo Component 초기화
						init();
						fnExhibitGiftGrid();		// 혜택/구매정보 > 증정 행사 Grid
						fnDefaultValue();			// 등록 or 수정에 따른 기본 세팅
						setCacheData();
						fnGoodsAuthInputAllow()
					}
				}
			});
		}
	}

	function fnDefaultValue() {
		if(ilGoodsId != undefined && ilGoodsId != ""){	//상품 수정이라면
			viewModel.set("pageMode", "modify");
			var itemKeyList = fnGoodsDetail(ilGoodsId);
			ilItemCode = itemKeyList[0].ilItemCode;
			urWarehouseId = itemKeyList[0].urWarehouseId;
			var goodsShippingTemplateList = itemKeyList[0].goodsShippingTemplateList;
		}
		else{
			viewModel.set("pageMode", "create");
			ilItemCode = pageParam.ilItemCode;
			urWarehouseId = pageParam.urWarehouseId;
			var goodsShippingTemplateList = "";
		}

		//기본정보 > 전시 카테고리 > 선택내역 리스트
		fnInitCategoryGrid();
		//기본정보 > 전시 카테고리
		fnInitCtgry();

		//기본정보 > 몰인몰 카테고리 > 선택내역 리스트
		fnInitMallInMallCategoryGrid();
		//기본정보 > 몰인몰 카테고리(공급업체가 올가, 풀무원녹즙2일 경우에만)
		fnInitMallInMallCtgry();

		fnSaveCategoryList(ilGoodsDisplayCategoryList, ilGoodsMallinmallCategoryList);	//저장된 카테고리 리스트 불러오기
		fnItemPriceList(ilItemCode);						// 가격정보 > 마스터 품목 가격정보
		fnOrderIfDateLimit(urWarehouseId);					// 판매정보 > 판매유형 > 예약 판매 옵션 설정 > 주문수집I/F일 배송패턴에 따른 요일 제한
		fnMasterItemAll(goodsShippingTemplateList);					// 마스터 품목 모든 정보 가져오기
		fnInitSaleStatusRadio();
	}

	var ifOrderDatesToDisable = [];
	var ifOrderDatesCutofftime = "00:00:00";

	function isPassedIFfOrderDatesCutofftime (dataItem) {
		let reservationEndDateTime = dataItem.reservationEndHour + ':' + dataItem.reservationEndMinute + ':00';

		if (reservationEndDateTime < ifOrderDatesCutofftime) { // 예약주문 가능 종료일의 마감시간이 주문마감시간보다 작으면 당일부터
			return false;
		} else { // 예약주문 가능 종료일의 마감시간이 주문마감시간 이후면 다음날 부터
			return true;
		}

	}

	function fnOrderIfDateLimit(urWarehouseId){
		if(urWarehouseId != null || urWarehouseId != ""){
			fnAjax({
				url			: '/admin/goods/regist/orderIfDateLimitList',
				params		: {urWarehouseId : viewModel.ilGoodsDetail.urWarehouseId},
				isAction	: 'select',
				success		: function(data, status, xhr){
					if(data) {
						if (data.ifOrderDatesToDisableList) {
							ifOrderDatesToDisable = data.ifOrderDatesToDisableList;
						}

						if (data.cutoffTime) {
							ifOrderDatesCutofftime = data.cutoffTime;
						}
					}
				},
				error		: function(xhr, status, strError) {
					fnKendoMessage({
						message : xhr.responseText
					});
				}
			});
		}

		//주문수집I/F일 입력창 Disabled
		$("#goodsReservationOptionTable").find("[name='orderIfDate']").each(function() {
			$(this).attr("disabled", true);
		});
	}

	function fnViewModelSetting(){

		viewModel = new kendo.data.ObservableObject({															//Kendo UI MVVM Pattern 적용

			pageMode							: 'create',
			isMallInMallVisible					: false,														//몰인몰 카테고리 Visible

			isOrderKindNonErp					: false,														//발주 가능여부(올가ERP) Visible
			isOrderKindErp						: false,														//발주 가능여부(올가ERP) Visible
			discountBtnVisible					: false,														//모든 할인 > 할인설정 버튼 Visaible
			/* 기본정보(마스터 품목) Start*/
			// 모든 내역을 마스터 품목 정보에서 가져옴(fnMasterItemAll 에서 호출)
			/* 기본정보(마스터 품목) End*/

			/* ilGoodsDetail Datas Start!!!! */
			ilGoodsDetail : {
				/* 기본 정보 Start */
				visibleUpdateHistory				: false,													// 최근 수정일 > 업데이트 내역 버튼 Visible
				visibleDownPopup					: false,													// 상세이미지 다운로드 Visible
				ilItemCode							: ilItemCode,												// 품목 Code
				urWarehouseId						: urWarehouseId,											// 출고처 ID
				goodsType							: 'GOODS_TYPE.SHOP_ONLY',									// 상품 유형
				goodsTypeName						: '매장전용',												// 코드에 따라서 불러오도록 변경 해야 함
				ilGoodsId							: '',														// 상품코드
				goodsCreateDate						: '',														// 상품 등록일자
				confirmStatus						: '',														// 최근 수정일
				mdRecommendYn						: 'N',														// MD추천 노출여부 DropwonList
				goodsName							: '',														// 상품명
				packageUnitDisplayYn				: 'Y',														// 상품명 > 포장용량 구성정보 > 노출 여부 > 기본설정값
				promotionName						: '',														// 프로모션 상품명

				promotionNameStartDate				: todayDate,												// 기본정보 > 프로모션 시작 기간, type : dateObj
				promotionNameEndDate				: lastDate,
				promotionNameStartYear				: '',														// 기본정보 > 프로모션 시작일
				promotionNameStartHour				: todayDate.oFormat("HH"),									// 기본정보 > 프로모션 시작 시간
				promotionNameStartMinute			: todayDate.oFormat("mm"),									// 기본정보 > 프로모션 시작 분
				promotionNameEndYear				: '',														// 기본정보 > 프로모션 종료일
				promotionNameEndHour				: lastDate.oFormat("HH"),									// 기본정보 > 프로모션 종료 시간
				promotionNameEndMinute				: lastDate.oFormat("mm"),									// 기본정보 > 프로모션 종료 분

				goodsDesc							: '',																					// 상품설명
				searchKeyword						: '',																				// 키워드 입력

				displayCategoryDiv					: 'MALL_DIV.PULMUONE',										//전시 카테고리
				displayCategoryList					: [],														//전시 카테고리 리스트
				mallInMallCategoryList				: [],														//몰인몰 카테고리 리스트
				/* 기본정보 End */

				/* 상세 정보 Start */
				// 모든 내역을 마스터 품목 정보에서 가져옴(fnMasterItemAll 에서 호출)
				/* 상세 정보 End */

				/* 판매/전시 Start */
				purchaseTargetType					: ['ALL'		//구매 허용 범위 > 기본설정값
					,'PURCHASE_TARGET_TP.MEMBER'
					,'PURCHASE_TARGET_TP.EMPLOYEE'
					,'PURCHASE_TARGET_TP.NONMEMBER'],
				goodsDisplayType					: ['ALL'							//판매허용범위 (PC/Mobile) > 기본설정값
					,'GOODS_DISPLAY_TYPE.APP'
					,'GOODS_DISPLAY_TYPE.WEB_MOBILE'
					,'GOODS_DISPLAY_TYPE.WEB_PC'],
				displayYn								: 'Y',						//전시 상태 > 전시(기본설정)

				saleStartDate						: todayDate,	//type : dataObj																									// 판매 기간 > 시작 기간
				saleEndDate							: lastDate,																											// 판매 기간 >  종료 기간
				saleStartYear						: todayDate.oFormat("yyyy-MM-dd"),		// 판매 기간 >  시작일
				saleStartHour						: todayDate.oFormat("HH"),	//kendo.toString(kendo.parseDate(defaultStartTime), "HH"),		// 판매 기간 >  시작 시간
				saleStartMinute						: todayDate.oFormat("mm"),	//kendo.toString(kendo.parseDate(defaultStartTime), "mm"),		// 판매 기간 >  시작 분
				saleEndYear							: lastDate.oFormat("yyyy-MM-dd"),			// 판매 기간 >  종료일
				saleEndHour							: lastDate.oFormat("HH"),							// 판매 기간 >  종료 시간
				saleEndMinute						: lastDate.oFormat("mm"),							// 판매 기간 >  종료 분

				saleStatus							: 'SALE_STATUS.SAVE',				//판매 상태 > 저장(기본설정)
				extinctionYn						: 'N',								//품목의 판매 허용여부 상태 값(단종 여부 확인)
				isSaleStatus						: true,								//판매 상태 등록시에는 변경 불가
				stockOperationForm					: '',								//재고운영형태
				stockQuantity						: 0,								//재고수량
				goodsStockInfo						: [],								//재고관련 정보
				stockQuantityName					: '',								//재고수량 제목
				visibleStockChangeButton			: false,							//재고운영형태 버튼 Visible
				visibleStockDetailPopupButton		: false,							//전일마감재고 상세보기 버튼 Visible
				goodsOutmallSaleStatus				: 'GOODS_OUTMALL_SALE_STAT.NONE',	//외부몰 판매 상태
				/* 판매/전시 End */

				/* 가격 정보 Start */
				/* 과세구분 항목은 품목 정보에서 가져옴(fnMasterItemAll 에서 호출) */
				taxYn								: '',							// 과세구분
				goodsPrice							: [],							// 판매 가격정보
				isVisibleGoodsPriceInfoNodata		: true,							// 판매 가격정보 Nodata Visible
				isVisibleGoodsPriceInfo				: false,						// 판매 가격정보 Data Visible

				goodsDiscountNodataVisible			: true,							// 행사/할인 내역 > 등록 페이지 Nodata Visible
				goodsDiscountPriorityList			: [],							// 행사/할인 내역 > 우선할인
				goodsDiscountPriorityApproList		: [],							// 행사/할인 내역 > 우선할인 > 승인 관리자 정보
				isGoodsDiscountPriorityListNoDataTbody	: false,					// 행사/할인 내역 > 우선할인 > Nodata 항목
				isGoodsDiscountPriorityListTbody		: false,					// 행사/할인 내역 > 우선할인 > Data 항목
				discountDetailPriorityBtnVisible		: false,					// 행사/할인 내역 > 우선할인 > 상세내역 버튼 Visible

				isGoodsDiscountImmediateListNoDataTbody	: false,					// 행사/할인 내역 > 즉시할인 > Nodata 항목
				isGoodsDiscountImmediateListTbody		: false,					// 행사/할인 내역 > 즉시할인 > Data 항목
				discountDetailImmediateBtnVisible		: false,					// 행사/할인 내역 > 우선할인 > 상세내역 버튼 Visible

				goodsDiscountErpEventList			: [],							// 행사/할인 내역 > ERP행사
				isGoodsDiscountErpEventListNoDataTbody	: false,					// 행사/할인 내역 > ERP행사 > Nodata 항목
				isGoodsDiscountErpEventListTbody		: false,					// 행사/할인 내역 > ERP행사 > Data 항목

				goodsDiscountImmediateList			: [],							// 행사/할인 내역 > 즉시할인
				goodsDiscountImmediateApproList		: [],							// 행사/할인 내역 > 즉시할인 > 승인 관리자 정보
				itemPriceList						: [],							// 마스터 품목 가격정보

				autoDisplaySizeYn					: 'Y',							// 단위별 용량정보 > 자동표기 여부 radio buttion
				sizeEtc								: '',							// 단위별 용량정보 > 자동표기안함 일때 textbox
				isAutoSizeValue						: true,							// 단위별 용량정보 > 자동표기안함 일때 textbox visible
				isSizeEtcPlaceholder				: '',							// 단위별 용량정보 > 자동표기안함 일때 기본 표기값
				/* 가격 정보 End */

				/* 임직원 할인 정보 Start */
				goodsEmployeePrice				: [],								// 임직원 할인 가격정보
				goodsEmployeePriceNoDataVisible : true,								// 임직원 할인 가격정보 Nodata Tbody Visible
				goodsEmployeePriceDataVisible	: false,							// 임직원 할인 가격정보 Data Tbody Visible

				goodsBaseDiscountEmployeeList		: [],							// 임직원 기본할인 정보
				goodsBaseDiscountEmployeeListNoDataVisible : true,					// 임직원 기본할인 정보 > Nodata Tbody Visible
				goodsBaseDiscountEmployeeListDataVisible : false,					// 임직원 기본할인 정보 > Data Tbody Visible

				goodsDiscountEmployeeList			: [],							// 임직원 개별할인 정보
				goodsDiscountEmployeeApproList		: [],							// 임직원 개별할인 정보 > 승인 관리자 정보
				goodsDiscountEmployeeListNoDataVisible	: true,						// 임직원 개별할인 정보 > Nodata Tbody Visible
				goodsDiscountEmployeeListInsertTbodyVisible	: false,				// 임직원 개별할인 정보 > 초기 할인설정 Tbody Visible
				goodsDiscountEmployeeListDataVisible	: false,					// 임직원 개별할인 정보 > Data Tbody Visible

				visibleGoodsDiscountEmployeeButton	: false,						// 임직원 할인 가격정보 > 임직원할인 설정 Button Visible
				/* 임직원 할인 정보 End */

				/* 판매 정보 Start */
				saleType								: 'SALE_TYPE.SHOP',			// 판매유형 > 일반판매(기본설정)
//				goodsReservationOptionList				: [],							// 판매유형(예약판매) > 예약 판매 옵션 설정 세부 항목 목록, goodsReservationOption-row-template 사용
				visibleGoodsReservationAllDel			: true,							// 예약 판매 옵션 설정 일정 초기화 버튼 Visible

				isReservOptionAllCheck					: false,						// 판매유형(예약판매) > 예약판매 옵션 설정 전체 선택/해제 체크박스 > 기본값
				saleShopYn								: 'Y',							// 판매정보 > 판매유형 > 매장판매(매장전용 상품은 무조건 Y로 처리)

				/* 판매 정보 End */

				/* 배송/발주 정보 Start */
				itemWarehouseList						: [],							//배송 유형에 따른 출고처 리스트

				//매장별 재고정보
				tiemStoreInfo							: [],						//매장별 재고정보 리스트
				tiemStoreInfoNoDataTbody				: true,						//매장별 재고정보 NoData
				tiemStoreInfoDataTbody					: false,					//매장별 재고정보 Data
				//발주 가능여부(올가ERP), 배송 불가 지역, 반품 가능 기간은 품목 정보에서 가져옴(fnMasterItemAll 에서 호출)
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

				/* 상품정보 제공고시 Start*/
				// 모든 내역을 마스터 품목 정보에서 가져옴(fnMasterItemAll 에서 호출)
				/* 상품정보 제공고시 End*/

				/* 상품 영양정보 Start*/
				// 모든 내역을 마스터 품목 정보에서 가져옴(fnMasterItemAll 에서 호출)
				/* 상품 영양정보 End*/

				/* 상품 인증정보 Start*/
				// 모든 내역을 마스터 품목 정보에서 가져옴(fnMasterItemAll 에서 호출)
				/* 상품 인증정보 End*/

				/* 상품 상세 이미지 Start*/
				// 모든 내역을 마스터 품목 정보에서 가져옴(fnMasterItemAll 에서 호출)
				/* 상품 상세 이미지 End*/

				/* 상품 상세 기본 정보 Start*/
				// 모든 내역을 마스터 품목 정보에서 가져옴(fnMasterItemAll 에서 호출)
				/* 상품 상세 기본 정보 End*/

				/* 상품 상세 주요 정보 Start*/
				// 모든 내역을 마스터 품목 정보에서 가져옴(fnMasterItemAll 에서 호출)
				/* 상품 상세 주요 정보 End*/

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

				/* 기타 정보 Start*/
				// 모든 내역을 마스터 품목 정보에서 가져옴(fnMasterItemAll 에서 호출)
				/* 기타 정보 End*/

				/* 상품 메모 Start */
				goodsMemo							: '',
				/* 상품 메모 End */

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

				promotionNameGoodsNameSumLength     : 0                             // 프로모션명 + 상품명 길이
			},
			/* ilGoodsDetail Datas End!!!! */
			fnNumberComma : function(e) {
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
			fnGoodsUpdateHistory					: function(e) {
				fnKendoMessage({ message : "상품정보 업데이트내역은 개발중 입니다." });
			},

			fnStockHistory							: function(e) {
				fnKendoMessage({ message : "상품정보 재고관리는 개발중 입니다." });
			},

			goodsReservationOptionList				: new kendo.data.DataSource({	// 판매유형(예약판매) > 예약 판매 옵션 설정 세부 항목 목록, goodsReservationOption-row-template 사용
				schema: {
					model: {
						id: "ilGoodsReserveOptionId",
						fields: {
							ilGoodsReserveOptionId :	{ type: "number", editable: false },
							saleSequance :				{ type: "string", validation: { required: true } },
							reservationStartDate :		{ type: "string", validation: { required: true } },
							reservationStartDateOld :	{ type: "string" },
							reservationStartHour :		{ type: "string", validation: { required: true } },
							reservationStartHourOld :	{ type: "string" },
							reservationStartMinute :	{ type: "string", validation: { required: true } },
							reservationStartMinuteOld :	{ type: "string" },
							reservationEndDate :		{ type: "string", validation: { required: true } },
							reservationEndDateOld :		{ type: "string" },
							reservationEndHour :		{ type: "string", validation: { required: true } },
							reservationEndHourOld :		{ type: "string" },
							reservationEndMinute :		{ type: "string", validation: { required: true } },
							reservationEndMinuteOld :	{ type: "string" },
							stockQuantity :				{ type: "number", validation: { required: true } },
							orderIfDate :				{ type: "string", validation: { required: true } },
							releaseDate :				{ type: "string", validation: { required: true } },
							arriveDate :				{ type: "string", validation: { required: true } }
						}
					}
				}
			}),

			deleteGoodsReservationOptionList		: [],	//예약 판매 옵션 설정 리스트에서 삭제한 항목 Log

			/* 상품공지 상세 하단 공지 파일관련 Start */
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
			/* 상품공지 상세 하단 공지 파일관련 End */

			/* click, function, change 등 이벤트 및 함수 */
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
			//판매/전시 > 재고운영형태 > 변경 팝업
			fnStockChange : function(e){
				e.preventDefault();

				let goodsStockInfo = viewModel.ilGoodsDetail.get("goodsStockInfo");
				let params = {};

				if(goodsStockInfo && goodsStockInfo.stockOrderYn == "Y" && goodsStockInfo.preOrderYn == "Y") {	//재고발주 여부가 'Y'이고 선주문여부가 'Y'이면 품목별 재고 리스트 재고운영형태 팝업 호출

					params.ilItemCd				= goodsStockInfo.ilItemCode;
					params.itemNm				= goodsStockInfo.itemName;
					params.warehouseNm			= goodsStockInfo.warehouseName;
					params.supplierCd			= goodsStockInfo.supplierCode;
					params.supplierNm			= goodsStockInfo.supplierName;
					params.erpCtgryLv1Id		= goodsStockInfo.erpCtgryLv1Id;
					params.preOrderYn			= goodsStockInfo.preOrderYn;
					params.ilItemWarehouseId	= goodsStockInfo.ilItemWarehouseId;

					fnKendoPopup({
						id			: "itemStockInfoEditPopup",
						title		: "선주문 여부",  // 해당되는 Title 명 작성
						width		: "525px",
						height		: "200px",
						scrollable	: "yes",
						src			: "#/itemStockInfoEditPopup",
						param		: params,

						success		: function( id, data ){
							if(data.parameter == undefined){
								if(data.popPreOrderYn == "Y") {
									viewModel.ilGoodsDetail.set("stockOperationForm", data.stockOperationForm);
								}
								else if(goodsStockInfo.stockOrderYn == "Y" && data.popPreOrderYn == "N") {
									viewModel.ilGoodsDetail.set("stockOperationForm", "한정재고");
								}
								else {
									viewModel.ilGoodsDetail.set("stockOperationForm", "");
								}
								goodsStockInfo.preOrderYn = data.popPreOrderYn;
							}
						}
					});
				}
				else if(goodsStockInfo && goodsStockInfo.stockOrderYn == "N") {	//재고발주 여부가 'N'라면 재고 미연동 품목 리스트 재고수정 팝업 호출

					params.ilItemCd				= goodsStockInfo.ilItemCode;
					params.itemNm				= goodsStockInfo.itemName;
					params.ilItemWarehouseId	= goodsStockInfo.ilItemWarehouseId;
					params.unlimitStockYn		= goodsStockInfo.unlimitStockYn;
					if(goodsStockInfo.unlimitStockYn == "Y") {
						params.notIfStockCnt	= 0;
					}
					else if(goodsStockInfo.unlimitStockYn == "N"){
						params.notIfStockCnt	= viewModel.ilGoodsDetail.get("stockQuantity");
					}

					fnKendoPopup({
						id			: "itemNonInterfacedStockUpdatePopup",
						title		: "재고수정",  // 해당되는 Title 명 작성
						width		: "700px",
						height		: "250px",
						scrollable	: "yes",
						src			: "#/itemNonInterfacedStockUpdatePopup",
						param		: params,
						success		: function( id, data ){
							if(data.parameter == undefined){
								viewModel.ilGoodsDetail.set("stockQuantity", data.popStockCntText);
								viewModel.ilGoodsDetail.set("stockOperationForm", data.stockOperationForm);

								goodsStockInfo.unlimitStockYn = data.popUnlimitStockYn;
							}
						}
					});
				}
			},
			//판매/전시 > 전일마감재고 > 재고 상세보기 팝업
			fnStockDetailPopup : function(){
				let goodsStockInfo = viewModel.ilGoodsDetail.get('goodsStockInfo');

				//console.log("goodsStockInfo : ", goodsStockInfo);

				if(goodsStockInfo) {
					fnKendoPopup({
						id: 'goodsStockMgm',
						title: '재고 상세 정보',
						param: {
							"ilItemWarehouseId":goodsStockInfo.ilItemWarehouseId
						,	"baseDt":goodsStockInfo.baseDate
						},
						src: '#/goodsStockMgm',
						width: '1100px',
						height: '800px',
						success: function(id, data) {
						}
					});
				}
				else {
					fnKendoMessage({ message : "재고관련 정보가 존재하지 않습니다. 관리자에게 문의하세요." });
				}
			},

			onGoodsDisplayType : function(e){
				e.preventDefault();

				//전체 선택 및 요소 체크박스에 따른 이벤트 처리
				fnAllChkEvent(e);
			},

			// 단위별 용량정보 > 자동표기 여부에 따른 자동표기 여부
			onAutoDisplaySizeYnChange			: function(e) {
				switch(viewModel.ilGoodsDetail.autoDisplaySizeYn) {
					case 'Y' :
						viewModel.ilGoodsDetail.set('isAutoSizeValue', true);
						viewModel.ilGoodsDetail.set('isSizeEtc', false);
						break;
					case 'N' :
						viewModel.ilGoodsDetail.set('isAutoSizeValue', false);
						viewModel.ilGoodsDetail.set('isSizeEtc', true);
						break;
				}
			},
			// 판매유형 > 예약 판매 클릭 시 (임시)
			onSaleTypeClick						: function(e) {
				if(e.target.value == 'SALE_TYPE.RESERVATION') {
					e.preventDefault();
					alert("예약판매는 개발중입니다.");
				}
			},

			// 판매유형 > 예약 판매 옵션 설정
			onSaleTypeChange 					: function(e) {
				if(e.target.value == "SALE_TYPE.SHOP") {
					e.preventDefault();

					fnKendoMessage({
						type : "confirm",
						message : "판매유형 변경 시 고객장바구니 정보 및 예약판매 중지됩니다.<BR/>진행하시겠습니까?",
						ok : function() {
							viewModel.ilGoodsDetail.set('saleType', e.target.value);
							viewModel.ilGoodsDetail.set('isSaleType', false);
							$("#saleTypeTh").attr("rowspan",1);
						}
					});
				}
				else if (e.target.value == "SALE_TYPE.REGULAR") {
					e.preventDefault();

					fnKendoMessage({
						type : "confirm",
						message : "판매유형 변경 시 고객장바구니 정보 및 예약판매 중지됩니다.<BR/>진행하시겠습니까?",
						ok : function() {
							viewModel.ilGoodsDetail.set('saleType', e.target.value);
							viewModel.ilGoodsDetail.set('isSaleType', false);
							$("#saleTypeTh").attr("rowspan",1);
						}
					});
				}
				else if (e.target.value == "SALE_TYPE.RESERVATION") {
					viewModel.ilGoodsDetail.set('saleType', e.target.value);
					viewModel.ilGoodsDetail.set('isSaleType', true);
					$("#saleTypeTh").attr("rowspan",2);
				}
			},
			// 판매유형(예약판매) > 예약판매 옵션 설정 전체 선택/해제 체크박스
			reservOptionAllChange : function(e) {
				if(viewModel.ilGoodsDetail.get('isReservOptionAllCheck')){
					if(viewModel.goodsReservationOptionList.total() > 0){
						for(var i=0; i <= viewModel.goodsReservationOptionList.total() - 1; i++){
							let dataItem = viewModel.goodsReservationOptionList.at(i);
							dataItem.goodsReservationOptionChk = false;
						}
						viewModel.goodsReservationOptionList.sync();
					}
				}
				else{
					if(viewModel.goodsReservationOptionList.total() > 0){
						for(var i=0; i <= viewModel.goodsReservationOptionList.total() - 1; i++){
							let dataItem = viewModel.goodsReservationOptionList.at(i);
							if(dataItem.optnDelAllow){
								dataItem.goodsReservationOptionChk = true;
							}
						}
						viewModel.goodsReservationOptionList.sync();
					}
				}
			},

			// 판매유형(예약판매) > 예약판매 옵션 설정 > 리스트 체크박스 해제/선택에 따른 전체 체크박스 제어
			reservOptionChange : function(e) {
				var checkedCnt = 0;
				if(viewModel.goodsReservationOptionList.total() > 0){
					for(var i=0; i <= viewModel.goodsReservationOptionList.total() - 1; i++){
						let dataItem = viewModel.goodsReservationOptionList.at(i);
						if(dataItem.goodsReservationOptionChk == true) {
							checkedCnt++;
						}
					}

					if(viewModel.goodsReservationOptionList.total() == checkedCnt) {
						viewModel.ilGoodsDetail.set('isReservOptionAllCheck', true);
					}
					else{
						viewModel.ilGoodsDetail.set('isReservOptionAllCheck', false);
					}
				}
			},

			//판매유형(예약판매) > 예약판매 옵션설정 > 옵션 추가 버튼 이벤트
			fnAddGoodsReservationOption : function(e) {
				e.preventDefault();
				var goodsReservationOptionChk = false;
				var saleSequance = 1;
				var reservationStartDate = "";//fnGetDayAdd(fnGetToday(),1);
				var reservationStartHour = "00";
				var reservationStartMinute = "00";

				var reservationEndDate = "";//fnGetDayAdd(fnGetToday(),1);
				var reservationEndHour = "23";
				var reservationEndMinute = "59";

				var stockQuantity = "";
				var orderIfDate = "";
				var releaseDate = "";
				var arriveDate = "";

				viewModel.goodsReservationOptionList.add({
					goodsReservationOptionChk : goodsReservationOptionChk,			//체크박스
					ilGoodsReserveOptionId : null,									//예약 판매 옵션 ID
					saleSequance : saleSequance,									//회차
					optnDelAllow : true,											//삭제가능여부
					reservationStartDate : reservationStartDate,					//예약 주문 가능 기간 시작일
					reservationStartDateOld : reservationStartDate,					//예약 주문 가능 기간 시작일(이전 값)
					reservationStartHour : reservationStartHour,					//예약 주문 가능 기간 시작 시간
					reservationStartHourOld : reservationStartHour,					//예약 주문 가능 기간 시작 시간(이전 값)
					reservationStartMinute : reservationStartMinute,				//예약 주문 가능 기간 시작 분
					reservationStartMinuteOld : reservationStartMinute,				//예약 주문 가능 기간 시작 분(이전 값)
					reservationEndDate : reservationEndDate,						//예약 주문 가능 기간 종료일
					reservationEndDateOld : reservationEndDate,						//예약 주문 가능 기간 종료일(이전 값)
					reservationEndHour : reservationEndHour,						//예약 주문 가능 기간 종료 시간
					reservationEndHourOld : reservationEndHour,						//예약 주문 가능 기간 종료 시간(이전 값)
					reservationEndMinute : reservationEndMinute,					//예약 주문 가능 기간 종료 분
					reservationEndMinuteOld : reservationEndMinute,					//예약 주문 가능 기간 종료 분(이전 값)
					stockQuantity : stockQuantity,									//주문재고
					orderIfDate : orderIfDate,										//주문수집 I/F일
					orderIfDateOld : orderIfDate,									//주문수집 I/F일(이전 값)
					releaseDate : releaseDate,										//출고예정일
					arriveDate : arriveDate											//도착예정일
				});

				if(viewModel.goodsReservationOptionList.total() > 0){								//번호 다시 매기기
					for(var i=0; i <= viewModel.goodsReservationOptionList.total() - 1; i++){
						let dataItem = viewModel.goodsReservationOptionList.at(i);
						dataItem.saleSequance = i+1;
					}
					viewModel.goodsReservationOptionList.sync();
				}

				//새로 행이 추가되면 새로 추가된 행은 무조건 false이므로 전체 체크박스를 false로 변경
				viewModel.ilGoodsDetail.set('isReservOptionAllCheck', false);

				//주문수집I/F일 입력창 Disabled
				$("#goodsReservationOptionTable").find("[name='orderIfDate']").each(function() {
					$(this).attr("disabled", true);
				});
			},

			// 판매유형(예약판매) > 예약판매 옵션설정 > 옵션 삭제 버튼 이벤트(체크박스 선택된 것만 삭제)
			fnDelGoodsReservationOption : function(e) {
				if(viewModel.goodsReservationOptionList.total() == 0) {
					alert("추가된 옵션이 없습니다.");
					return;
				}

				var goodsReservationOptionChkNum = 0;
				if(viewModel.goodsReservationOptionList.total() > 0) {
					for(var i=0; i <= viewModel.goodsReservationOptionList.total() - 1; i++){
						let dataItem = viewModel.goodsReservationOptionList.at(i);

						if(dataItem.goodsReservationOptionChk == true){
							goodsReservationOptionChkNum++;
						}
					}
				}

				if(goodsReservationOptionChkNum == 0){
					alert("적어도 한개 이상 삭제할 옵션을 선택해 주세요.");
					return;
				}

				fnKendoMessage({
					type : "confirm",
					message : "진행중이거나 종료된 주문가능기간 회차를 제외한 회차정보만 삭제처리됩니다.<BR/>삭제하시겠습니까?",
					ok : function() {
						let goodsReservationOptionList = viewModel.goodsReservationOptionList.data();

						if(goodsReservationOptionList.length > 0) {
							for(let i=goodsReservationOptionList.length-1; i >= 0; i--){
								if(goodsReservationOptionList[i].goodsReservationOptionChk == true){				//체크된 것만 삭제
									var goodsReservationOptionInfo = viewModel.goodsReservationOptionList.data()[i];

									viewModel.deleteGoodsReservationOptionList.push({
										ilGoodsReserveOptionId : goodsReservationOptionInfo['ilGoodsReserveOptionId'],			//삭제할 예약판매옵션설정 ID
									});

									viewModel.goodsReservationOptionList.remove(goodsReservationOptionInfo); 	// viewModel 에서 삭제
								}
							}
						}

						if(viewModel.goodsReservationOptionList.total() > 0){								//번호 다시 매기기
							for(var i=0; i <= viewModel.goodsReservationOptionList.total() - 1; i++){
								let dataItem = viewModel.goodsReservationOptionList.at(i);
								dataItem.saleSequance = i+1;
							}
							viewModel.goodsReservationOptionList.sync();
						}

						if(viewModel.goodsReservationOptionList.total() == 0){	//전체 삭제 시 전체 선택 체크박스 false
							viewModel.ilGoodsDetail.set('isReservOptionAllCheck', false);
						}
					},
					cancel : function() {
						return;
					}
				});
			},

			// 판매유형(예약판매) > 예약판매 옵션설정 > 일정 초기화 버튼 이벤트
			fnGoodsReservationAllDel : function(e){
				if(viewModel.goodsReservationOptionList.total() == 0){
					fnKendoMessage({ message : "추가된 옵션이 없습니다." });
					return;
				}

				fnKendoMessage({
					type : "confirm",
					message : "일정을 초기화하시겠습니까?",
					ok : function() {
						viewModel.goodsReservationOptionList.data([]);														// dataSource 초기화
					},
					cancel : function() {
						return;
					}
				});
			},
			fnGetRowDatePicker : function(rowIndex, objectName){ // datePicker 객체 가져오기
				return $("#goodsReservationOptionTable tr").eq(rowIndex).find("[name=" + objectName + "]").data("kendoDatePicker");
			},
			// 판매유형(예약판매) > 예약판매 옵션설정 > 예약 주문 가능 기간 날짜 제한
			fnReservationStartMinDate : function(rowIndex){
				return kendo.parseDate( "2020-01-01", "yyyy-MM-dd" );
			},
			fnReservationStartMaxDate : function(rowIndex){
				let dataItem = viewModel.goodsReservationOptionList.at(rowIndex);
				let reservationStartDate = dataItem.reservationStartDate;
				let reservationEndDate = dataItem.reservationEndDate;

				if(reservationStartDate == "" || reservationEndDate == "") {
					return kendo.parseDate( "2999-12-31", "yyyy-MM-dd" );
				}
				else if(reservationEndDate != "") {
					return kendo.parseDate( reservationEndDate, "yyyy-MM-dd" );
				}
			},
			fnReservationStartDateChange : function(e){
				e.preventDefault();

				let rowIndex = viewModel.goodsReservationOptionList.indexOf( e.data );
				let dataItem = viewModel.goodsReservationOptionList.at(rowIndex);

				if( e.sender._old == null && e.sender._oldText.indexOf("year-month-day") >= 0 ){
					dataItem.reservationStartDate = dataItem.reservationStartDateOld;
					dataItem.reservationStartHour = dataItem.reservationStartHourOld;
					dataItem.reservationStartMinute = dataItem.reservationStartMinuteOld;
					viewModel.goodsReservationOptionList.sync();

					fnKendoMessage({ message : "시작일은 필수 입니다." });
					return;
				}

				dataItem.reservationStartDate = kendo.toString(kendo.parseDate(dataItem.reservationStartDate), "yyyy-MM-dd");
				dataItem.reservationEndDate = kendo.toString(kendo.parseDate(dataItem.reservationEndDate), "yyyy-MM-dd");

				let startDateTime = kendo.parseDate((dataItem.reservationStartDate + " " + dataItem.reservationStartHour + ":" + dataItem.reservationStartMinute), "yyyy-MM-dd HH:mm");
				let endDateTime = kendo.parseDate((dataItem.reservationEndDate + " " + dataItem.reservationEndHour + ":" + dataItem.reservationEndMinute), "yyyy-MM-dd HH:mm");

				if( endDateTime != undefined && startDateTime.getTime() >= endDateTime.getTime() ){
					dataItem.reservationStartDate = dataItem.reservationStartDateOld;
					dataItem.reservationStartHour = dataItem.reservationStartHourOld;
					dataItem.reservationStartMinute = dataItem.reservationStartMinuteOld;
					viewModel.goodsReservationOptionList.sync();
					fnKendoMessage({ message : "예약 주문 가능 기간을 정확히 입력해주세요" });
					return;
				}

				let reservationEndDateAttr = viewModel.fnGetRowDatePicker(rowIndex, "reservationEndDate");
				reservationEndDateAttr.min( viewModel.fnReservationEndMinDate(rowIndex) );
			},
			fnOrderIfMinDate : function(rowIndex) {	//주문수집 I/F 일자 min 가져오기
				let dataItem = viewModel.goodsReservationOptionList.at(rowIndex);
				let orderIfDate = dataItem.orderIfDate;

				if(orderIfDate == "") {
					return kendo.parseDate( "2020-01-01", "yyyy-MM-dd" );
				}
				else if(orderIfDate != "") {
					return kendo.parseDate( orderIfDate, "yyyy-MM-dd" );
				}
			},
			fnReservationEndMinDate : function(rowIndex){ // 종료일 min 가져오기
				let dataItem = viewModel.goodsReservationOptionList.at(rowIndex);
				let reservationStartDate = dataItem.reservationStartDate;
				let reservationEndDate = dataItem.reservationEndDate;

				if(reservationStartDate == "") {
					return kendo.parseDate( "2020-01-01", "yyyy-MM-dd" );
				}
				else if(reservationStartDate != "") {
					return kendo.parseDate( reservationStartDate, "yyyy-MM-dd" );
				}
			},
			fnReservationEndMaxDate : function(rowIndex){ // 종료일 max 가져오기
				return kendo.parseDate( "2099-12-31", "yyyy-MM-dd");
			},
			fnReservationEndDateChange : function(e){ // 종료일 변경
				e.preventDefault();

				let rowIndex = viewModel.goodsReservationOptionList.indexOf( e.data );
				let dataItem = viewModel.goodsReservationOptionList.at(rowIndex);

				if( e.sender._old == null && e.sender._oldText.indexOf("year-month-day") >= 0 ){
					dataItem.reservationEndDate = dataItem.reservationEndDateOld;
					dataItem.reservationEndHour = dataItem.reservationEndHourOld;
					dataItem.reservationEndMinute = dataItem.reservationEndMinuteOld;
					viewModel.goodsReservationOptionList.sync();

					fnKendoMessage({ message : "종료일은 필수 입니다." });
					return;
				}

				dataItem.reservationStartDate = kendo.toString(kendo.parseDate(dataItem.reservationStartDate), "yyyy-MM-dd");
				dataItem.reservationEndDate = kendo.toString(kendo.parseDate(dataItem.reservationEndDate), "yyyy-MM-dd");

				let startDateTime = kendo.parseDate((dataItem.reservationStartDate + " " + dataItem.reservationStartHour + ":" + dataItem.reservationStartMinute), "yyyy-MM-dd HH:mm");
				let endDateTime = kendo.parseDate((dataItem.reservationEndDate + " " + dataItem.reservationEndHour + ":" + dataItem.reservationEndMinute), "yyyy-MM-dd HH:mm");

                if (startDateTime != undefined && startDateTime.getTime() >= endDateTime.getTime()) {
                    dataItem.reservationEndDate = dataItem.reservationEndDateOld;
                    dataItem.reservationEndHour = dataItem.reservationEndHourOld;
                    dataItem.reservationEndMinute = dataItem.reservationEndMinuteOld;
                    viewModel.goodsReservationOptionList.sync();
                    fnKendoMessage({message: "예약 주문 가능 기간을 정확히 입력해주세요"});
                    return;
                }

                // 예약주문기간이 도래한 경우
                if (dataItem.reservationStartDateOld <= nowDate) {
                    // 기존 종료일 이후로 변경 못함.
                    if (dataItem.reservationEndDate > dataItem.reservationEndDateOld) {
                        dataItem.reservationEndDate = dataItem.reservationEndDateOld;
                        dataItem.reservationEndHour = dataItem.reservationEndHourOld;
                        dataItem.reservationEndMinute = dataItem.reservationEndMinuteOld;
                        viewModel.goodsReservationOptionList.sync();
                        fnKendoMessage({message: "예약 주문 가능 기간 종료일을 기존 종료일 이후로 변경할 수 없습니다."});
                        return;
                    }
                }

				let reservationStartDateAttr = viewModel.fnGetRowDatePicker(rowIndex, "reservationStartDate");
				reservationStartDateAttr.max( viewModel.fnReservationStartMaxDate(rowIndex) );

				let orderIfDateAttr = viewModel.fnGetRowDatePicker(rowIndex, "orderIfDate");
				orderIfDateAttr.min( viewModel.fnOrderIfMinDate(rowIndex) );

                // 기존 저장된 시작일이 없고 현재일보다 시작일이 큰 경우에만 변경 (예약주문기간이 도래하지 않음)
                if (dataItem.reservationStartDateOld > nowDate) {
                    $("#ifCalc-btn-id" + dataItem.saleSequance).attr("class", "btn-point");
                }
            },
            fnOrderIfDateAllDisable: function () {
                //주문수집I/F일 입력창 Disabled
                $("#goodsReservationOptionTable").find("[name='orderIfDate']").each(function () {
                    $(this).attr("disabled", true);
                });
            },
            fnAllOrderIfDateCalc: function (e) {	//주문수집I/F일자 전체 계산
                if (viewModel.goodsReservationOptionList.total() == 0) {
                    fnKendoMessage({message: "추가된 옵션이 없습니다."});
                    return;
                }

				var reservationEndDateNullChkNum = 0;
				if(viewModel.goodsReservationOptionList.total() > 0) {
					for(var i=0; i <= viewModel.goodsReservationOptionList.total() - 1; i++){
						let dataItem = viewModel.goodsReservationOptionList.at(i);

						if(dataItem.reservationEndDate == ""){
							reservationEndDateNullChkNum++;
						}
					}

					if(reservationEndDateNullChkNum > 0) {
						fnKendoMessage({ message : "주문가능 기간 및 수집일을 입력해주세요" });
						return;
					}

					let patternStandardDateArray = [];

					for(var i=0; i <= viewModel.goodsReservationOptionList.total() - 1; i++){
						let dataItem = viewModel.goodsReservationOptionList.at(i);
						let reservationStartDate = dataItem.reservationStartDate;
						let reservationEndDate = dataItem.reservationEndDate;

						if (isPassedIFfOrderDatesCutofftime(dataItem)) { // 예약주문 가능 종료일의 마감시간이 주문마감시간 이후면 다음날 부터
							patternStandardDateArray[i] = fnGetDayAdd(dataItem.reservationEndDate,1);
						} else { // 예약주문 가능 종료일의 마감시간이 주문마감시간 이전이면 당일부터
							patternStandardDateArray[i] = dataItem.reservationEndDate;
						}
					}

					let params = {
						"urWarehouseId" : viewModel.ilGoodsDetail.urWarehouseId,	//상품ID
						"patternStandardDateArray" : patternStandardDateArray		//예약주문 가능 기간 종료일+1
					};

					fnAjax({
						url			: '/admin/goods/regist/goodsReservationDateCalcList',
						params		: params,
						contentType : 'application/json',
						isAction	: 'select',
						success		: function(data, status, xhr){
							if(data) {
								for(var i in data){
									let dataItem = viewModel.goodsReservationOptionList.at(i);

                                    dataItem.reservationEndDateOld = dataItem.reservationEndDate;
                                    if ((dataItem.orderIfDateOld == '') || (dataItem.orderIfDateOld == data[i].orderIfDate)) {

                                        dataItem.orderIfDate = data[i].orderIfDate;
                                        dataItem.orderIfDateOld = data[i].orderIfDate;
                                        dataItem.releaseDate = data[i].releaseDate;
                                        dataItem.arriveDate = data[i].arriveDate;
                                    }
                                }
                                viewModel.goodsReservationOptionList.sync();
                                viewModel.fnOrderIfDateAllDisable();

                                for (var i in data) {
                                    let dataItem = viewModel.goodsReservationOptionList.at(i);
                                    if (dataItem.reservationStartDate > nowDate) {
                                        $("#ifCalc-btn-id" + dataItem.saleSequance).attr("class", "btn-point");
                                    } else {
                                        $("#ifCalc-btn-id" + dataItem.saleSequance).attr("class", "btn-ligntgray");
                                    }
                                }
                            }
                        },
                        error: function (xhr, status, strError) {
                            fnKendoMessage({
                                message: xhr.responseText
                            });
                        }
                    });
                }
            },

			fnOrderIfDateCalc : function(e) {		//주문수집I/F 일자 계산
				e.preventDefault();

                let rowIndex = viewModel.goodsReservationOptionList.indexOf(e.data);
                let dataItem = viewModel.goodsReservationOptionList.at(rowIndex);
                let reservationStartDate = dataItem.reservationStartDate;
                let reservationEndDate = dataItem.reservationEndDate;
                let optnDelAllow = dataItem.optnDelAllow;		// 삭제여부 추가
                let patternStandardDateArray = [];

                if (isPassedIFfOrderDatesCutofftime(dataItem)) { // 예약주문 가능 종료일의 마감시간이 주문마감시간 이후면 다음날 부터
                    patternStandardDateArray[0] = fnGetDayAdd(reservationEndDate, 1);
                } else { // 예약주문 가능 종료일의 마감시간이 주문마감시간 이전이면 당일부터
                    patternStandardDateArray[0] = reservationEndDate;
                }

				if(reservationStartDate != null && reservationStartDate != '' && reservationEndDate != null && reservationEndDate != '') {
					let params = {
						"urWarehouseId" : viewModel.ilGoodsDetail.urWarehouseId,	//상품ID
						"patternStandardDateArray" : patternStandardDateArray		//예약주문 가능 기간 종료일+1
					};

					fnAjax({
						url			: '/admin/goods/regist/goodsReservationDateCalcList',
						params		: params,
						contentType : 'application/json',
						isAction	: 'select',
						success		: function(data, status, xhr){
							if(data && data[0].orderIfDate != null) {
								dataItem.reservationEndDateOld = dataItem.reservationEndDate;

								dataItem.orderIfDate = data[0].orderIfDate;
								dataItem.orderIfDateOld = data[0].orderIfDate;
								dataItem.releaseDate = data[0].releaseDate;
								dataItem.arriveDate = data[0].arriveDate;

                                viewModel.fnOrderIfDateAllDisable();
                                if (!optnDelAllow) {
                                    $("#ifCalc-btn-id" + dataItem.saleSequance).attr("class", "btn-lightgray");
                                } else {
                                    $("#ifCalc-btn-id" + dataItem.saleSequance).attr("class", "btn-point");
                                }
                            } else {
                                dataItem.orderIfDate = "";
                                dataItem.orderIfDateOld = "";
                                dataItem.releaseDate = "";
                                dataItem.arriveDate = "";

								viewModel.fnOrderIfDateAllDisable();

								fnKendoMessage({
									message : "배송패턴이 존재하지 않습니다. 관리자에게 문의해 주세요."
								});
							}
							viewModel.goodsReservationOptionList.sync();
						},
						error		: function(xhr, status, strError) {
							fnKendoMessage({
								message : xhr.responseText
							});
						}
					});
				}
				else {
					fnKendoMessage({ message : "주문가능 기간 및 수집일을 입력해주세요" });
					return;
				}
			},

			fnAllReleaseAndArriveDateCalc : function(e) {	//출고예정일, 도착예정일 전체 계산
				if(viewModel.goodsReservationOptionList.total() == 0) {
					fnKendoMessage({ message : "추가된 옵션이 없습니다." });
					return;
				}

				var reservationEndDateNullChkNum = 0;
				if(viewModel.goodsReservationOptionList.total() > 0) {
					for(var i=0; i <= viewModel.goodsReservationOptionList.total() - 1; i++){
						let dataItem = viewModel.goodsReservationOptionList.at(i);

						if(dataItem.orderIfDate == ""){
							reservationEndDateNullChkNum++;
						}
					}

					if(reservationEndDateNullChkNum > 0) {
						fnKendoMessage({ message : "주문가능 기간 및 수집일을 입력해주세요" });
						return;
					}

					let patternStandardDateArray = [];

					for(var i=0; i <= viewModel.goodsReservationOptionList.total() - 1; i++){
						let dataItem = viewModel.goodsReservationOptionList.at(i);
						let reservationStartDate = dataItem.reservationStartDate;
						let orderIfDate = dataItem.orderIfDate;
						patternStandardDateArray[i] = orderIfDate;
					}

					let params = {
						"urWarehouseId" : viewModel.ilGoodsDetail.urWarehouseId,	//상품ID
						"patternStandardDateArray" : patternStandardDateArray		//예약주문 가능 기간 종료일+1
					};

					fnAjax({
						url			: '/admin/goods/regist/goodsReservationDateCalcList',
						params		: params,
						contentType : 'application/json',
						isAction	: 'select',
						success		: function(data, status, xhr){
							if(data) {
								for(var i in data){
									let dataItem = viewModel.goodsReservationOptionList.at(i);

									dataItem.orderIfDate = data[i].orderIfDate;
									dataItem.orderIfDateOld = data[i].orderIfDate;
									dataItem.releaseDate = data[i].releaseDate;
									dataItem.arriveDate = data[i].arriveDate;
								}

								viewModel.goodsReservationOptionList.sync();
								viewModel.fnOrderIfDateAllDisable();

								for(var i in data){
									let dataItem = viewModel.goodsReservationOptionList.at(i);
									$("#raaCalc-btn-id"+dataItem.saleSequance).attr("class", "btn-ligntgray");
								}
							}
						},
						error		: function(xhr, status, strError) {
							fnKendoMessage({
								message : xhr.responseText
							});
						}
					});
				}
			},
			fnOrderIfDateOpen : function(e) {			//주문수집 I/F일 DatePicker Open할때
				$("#goodsReservationOptionTable").find("[name='orderIfDate']").each(function() {
					$(this).data("kendoDatePicker").setOptions({	//요일에 해당하는 날짜 모두 제한 걸기
						 disableDates: ifOrderDatesToDisable
					})
				})
			},
			fnReleaseAndArriveDateCalc : function(e) {	//출고예정일, 도착예정일 계산
				e.preventDefault();

				let rowIndex = viewModel.goodsReservationOptionList.indexOf( e.data );
				let dataItem = viewModel.goodsReservationOptionList.at(rowIndex);
				let reservationStartDate = dataItem.reservationStartDate;
				let orderIfDate = dataItem.orderIfDate;
				let patternStandardDateArray = [];
				patternStandardDateArray[0] = orderIfDate;

				if(orderIfDate != "") {
					let params = {
						"urWarehouseId" : viewModel.ilGoodsDetail.urWarehouseId,	//상품ID
						"patternStandardDateArray" : patternStandardDateArray		//주문수집I/F일자
					};

					fnAjax({
						url			: '/admin/goods/regist/goodsReservationDateCalcList',
						params		: params,
						contentType : 'application/json',
						isAction	: 'select',
						success		: function(data, status, xhr){
							if(data) {
								dataItem.orderIfDate = data[0].orderIfDate;
								dataItem.orderIfDateOld = data[0].orderIfDate;
								dataItem.releaseDate = data[0].releaseDate;
								dataItem.arriveDate = data[0].arriveDate;

								viewModel.goodsReservationOptionList.sync();
								viewModel.fnOrderIfDateAllDisable();

								$("#raaCalc-btn-id"+dataItem.saleSequance).attr("class", "btn-ligntgray");
							}
						},
						error		: function(xhr, status, strError) {
							fnKendoMessage({
								message : xhr.responseText
							});
						}
					});
				}
				else {
					fnKendoMessage({ message : "주문가능 기간 및 수집일을 입력해주세요" });
					return;
				}
			},
			fnOrderIfDateChange : function(e) {		//주문수집I/F일자 변경 처리
				e.preventDefault();

				let rowIndex = viewModel.goodsReservationOptionList.indexOf( e.data );
				let dataItem = viewModel.goodsReservationOptionList.at(rowIndex);

				if( e.sender._old == null && e.sender._oldText.indexOf("year-month-day") >= 0 ){
					dataItem.orderIfDate = "";
					viewModel.goodsReservationOptionList.sync();

					fnKendoMessage({ message : "주문가능 기간 및 수집일을 입력해주세요" });
					return;
				}

				dataItem.orderIfDate = kendo.toString(kendo.parseDate(dataItem.orderIfDate), "yyyy-MM-dd");
				dataItem.orderIfDate = kendo.toString(kendo.parseDate(dataItem.orderIfDate), "yyyy-MM-dd");

				$("#raaCalc-btn-id"+dataItem.saleSequance).attr("class", "btn-point");
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
			fnExhibitGiftDetail : function(rowData) {
				console.log("rowData", rowData);

				let option = {};
				option.data = { exhibitTp : 'EXHIBIT_TP.GIFT', evExhibitId : rowData.evExhibitId, mode : 'update' };
				option.url = "#/exhibitMgm";
				option.menuId = 959;
				option.target = '_blank';

				fnGoNewPage(option);
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

				//이미지가 삭제되면 노출기간 설정 필수조건 삭제
				/* HGRM-1646 초기화 및 시간달력포맷 공통화 수정
				$("#noticeBelow1StartDate").removeAttr("required");
				$("#noticeBelow1EndDate").removeAttr("required");
				*/
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

				//이미지가 삭제되면 노출기간 설정 필수조건 삭제
				/* HGRM-1646 초기화 및 시간달력포맷 공통화 수정
				$("#noticeBelow2StartDate").removeAttr("required");
				$("#noticeBelow2EndDate").removeAttr("required");
				*/
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
				// let startDateTime = kendo.parseDate(viewModel.fnGetDateFull(type+"Start"), "yyyy-MM-dd HH:mm");
				// let endDateTime = kendo.parseDate(viewModel.fnGetDateFull(type+"End"), "yyyy-MM-dd HH:mm");

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
//						if( todayDate.getTime() > startDateObj.getTime() ) {
//							fnKendoMessage({ message : "오늘 날짜 이후를 선택해주세요.", ok : function() {
//								$("#" + id).focus();
//							}});
//
//							return false;
//						}

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
//					if( todayDate.getTime() > endDateObj.getTime() ) {
//						fnKendoMessage({ message : "오늘 날짜 이후를 선택해주세요.", ok : function() {
//							$("#" + id).focus();
//						}});
//
//						return false;
//					}

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

				// 	if( startDateTime.getTime() > endDateTime.getTime() ){
				// 		fnKendoMessage({ message : "시작일은 종료일보다 이전 날짜이어야 합니다" });

				// 		if(type == 'promotionName') {

				// 			viewModel.fnPromotionDateReset();

				// 		} else if(type == 'sale') {

				// 			viewModel.fnSaleDateReset();

				// 		} else if(type == 'noticeBelow1') {

				// 			viewModel.fnNoticeBelow1DateReset();

				// 		} else if(type == 'noticeBelow2') {

				// 			viewModel.fnNoticeBelow2DateReset();

				// 		}
				// 		return false;
				// }
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
					// if((e.sender.element.context.id).indexOf('promotionName') > -1) {
					// 	viewModel.fnPromotionDateReset();
					// } else if((e.sender.element.context.id).indexOf('sale') > -1) {
					// 	viewModel.fnSaleDateReset();
					// } else if((e.sender.element.context.id).indexOf('noticeBelow1') > -1) {
					// 	viewModel.fnNoticeBelow1DateReset();
					// } else if((e.sender.element.context.id).indexOf('noticeBelow2') > -1) {
					// 	viewModel.fnNoticeBelow2DateReset();
					// }
					return;
				}


				//변경된 데이터 set
				// viewModel.ilGoodsDetail.set(id, kendo.toString(kendo.parseDate(viewModel.ilGoodsDetail.get(id)), "yyyy-MM-dd"));
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
				// fnStartCalChange('saleStartYear', 'saleEndYear');
				// fnEndCalChange('saleStartYear', 'saleEndYear');
			},
            // 상품 공지 > 상세 상단공지 시작일/종료일 초기화
			fnNoticeBelow1DateReset : function() {
				//수정 데이터로 초기화 일 경우

				const currentStartDate = kendo.parseDate(viewModel.ilGoodsDetail.get("noticeBelow1StartDate"));
				const currentEndDate = kendo.parseDate(viewModel.ilGoodsDetail.get("noticeBelow1EndDate"));

				if( currentStartDate ) {
					viewModel.ilGoodsDetail.set('noticeBelow1StartYear',kendo.toString(currentStartDate, "yyyy-MM-dd") );
					viewModel.ilGoodsDetail.set('noticeBelow1StartHour', kendo.toString(currentStartDate, "HH"));
					viewModel.ilGoodsDetail.set('noticeBelow1StartMinute', kendo.toString(currentStartDate, "mm"));
				} else {
					viewModel.ilGoodsDetail.set('noticeBelow1StartYear', "");
					viewModel.ilGoodsDetail.set('noticeBelow1StartHour', "00");
					viewModel.ilGoodsDetail.set('noticeBelow1StartMinute', "00");
				}

				if( currentEndDate ) {
					viewModel.ilGoodsDetail.set('noticeBelow1EndYear', kendo.toString(currentEndDate, "yyyy-MM-dd") );
					viewModel.ilGoodsDetail.set('noticeBelow1EndHour', kendo.toString(currentEndDate, "HH"));
					viewModel.ilGoodsDetail.set('noticeBelow1EndMinute', kendo.toString(currentEndDate, "mm"));
				} else {
					viewModel.ilGoodsDetail.set('noticeBelow1EndYear', "");
					viewModel.ilGoodsDetail.set('noticeBelow1EndHour', "00");
					viewModel.ilGoodsDetail.set('noticeBelow1EndMinute', "00");
				}
			},
			// 상품 공지 > 상세 하단공지 시작일/종료일 초기화
			fnNoticeBelow2DateReset : function() {

				const currentStartDate = kendo.parseDate(viewModel.ilGoodsDetail.get("noticeBelow2StartDate"));
				const currentEndDate = kendo.parseDate(viewModel.ilGoodsDetail.get("noticeBelow2EndDate"));

				if( currentStartDate ) {
					viewModel.ilGoodsDetail.set('noticeBelow2StartYear',kendo.toString(currentStartDate, "yyyy-MM-dd") );
					viewModel.ilGoodsDetail.set('noticeBelow2StartHour', kendo.toString(currentStartDate, "HH"));
					viewModel.ilGoodsDetail.set('noticeBelow2StartMinute', kendo.toString(currentStartDate, "mm"));
				} else {
					viewModel.ilGoodsDetail.set('noticeBelow2StartYear', "");
					viewModel.ilGoodsDetail.set('noticeBelow2StartHour', "00");
					viewModel.ilGoodsDetail.set('noticeBelow2StartMinute', "00");
				}

				if( currentEndDate ) {
					viewModel.ilGoodsDetail.set('noticeBelow2EndYear', kendo.toString(currentEndDate, "yyyy-MM-dd") );
					viewModel.ilGoodsDetail.set('noticeBelow2EndHour', kendo.toString(currentEndDate, "HH"));
					viewModel.ilGoodsDetail.set('noticeBelow2EndMinute', kendo.toString(currentEndDate, "mm"));
				} else {
					viewModel.ilGoodsDetail.set('noticeBelow2EndYear', "");
					viewModel.ilGoodsDetail.set('noticeBelow2EndHour', "00");
					viewModel.ilGoodsDetail.set('noticeBelow2EndMinute', "00");
				}
			}
		});
	} //fnViewModelSetting 끝

	function fnOrderIfDateClick(rowIndex){
		let dataItem = viewModel.goodsReservationOptionList.at(rowIndex);
		console.log("dataItem", dataItem);
	}

	function init(){
		kendo.bind($("#inputForm"), viewModel);
	}



	// 저장된 카테고리 정보 가져오기
	function fnSaveCategoryList(ilGoodsDisplayCategoryList, ilGoodsMallinmallCategoryList){

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

	// 저장된 상품 정보 가져오기
	function fnGoodsDetail(ilGoodsId){
		var itemKeyList = [];

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
						//history.back();
					}
					else{
						firstIlGoodsDetailData = data.ilGoodsDetail;
						viewModel.set("ilGoodsDetail", data.ilGoodsDetail);																//상품 마스터 기본 정보 내역
						//console.log("data.ilGoodsDetail : " + JSON.stringify(data.ilGoodsDetail));

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

						//가격정보 > 단위별 용량 정보 > 자동표기 여부
						if(data.ilGoodsDetail.autoDisplaySizeYn == "N") {
							viewModel.ilGoodsDetail.set('isSizeEtc', true);
						}

						if(data.goodsPrice.length > 0){
							viewModel.ilGoodsDetail.set("isVisibleGoodsPriceInfoNodata", false);
							viewModel.ilGoodsDetail.set("isVisibleGoodsPriceInfo", true);
							viewModel.ilGoodsDetail.set("goodsPrice", data.goodsPrice);											//가격 정보 > 판매 가격정보
						}
						else{
							viewModel.ilGoodsDetail.set("isVisibleGoodsPriceInfoNodata", true);
							viewModel.ilGoodsDetail.set("isVisibleGoodsPriceInfo", false);
							viewModel.ilGoodsDetail.set("goodsPrice", []);														//가격 정보 > 판매 가격정보
						}

						viewModel.ilGoodsDetail.set("goodsDiscountNodataVisible", false);										//가격 정보 > 행사/할인내역 > 전체 Nodata Visible

						if(data.goodsDiscountPriorityList.length > 0){
							viewModel.ilGoodsDetail.set("goodsDiscountPriorityList", data.goodsDiscountPriorityList);				//가격 정보 > 행사/할인 내역 > 우선할인
							viewModel.ilGoodsDetail.set("isGoodsDiscountPriorityListNoDataTbody", false);							//가격 정보 > 행사/할인 내역 > 우선할인 > Nodata 항목
							viewModel.ilGoodsDetail.set("isGoodsDiscountPriorityListTbody", true);									//가격 정보 > 행사/할인 내역 > 우선할인 > Data 항목
						}
						else{
							viewModel.ilGoodsDetail.set("goodsDiscountPriorityList", []);											//가격 정보 > 행사/할인 내역 > 우선할인
							viewModel.ilGoodsDetail.set("isGoodsDiscountPriorityListNoDataTbody", true);							//가격 정보 > 행사/할인 내역 > 우선할인 > Nodata 항목
							viewModel.ilGoodsDetail.set("isGoodsDiscountPriorityListTbody", false);									//가격 정보 > 행사/할인 내역 > 우선할인 > Data 항목
						}
						//console.log("data.goodsDiscountPriorityList : ", data.goodsDiscountPriorityList);
						//console.log("viewModel.ilGoodsDetail.goodsDiscountPriorityList : " + JSON.stringify(viewModel.ilGoodsDetail.goodsDiscountPriorityList));

						if(data.goodsDiscountErpEventList.length > 0){
							viewModel.ilGoodsDetail.set("goodsDiscountErpEventList", data.goodsDiscountErpEventList);				//가격 정보 > 행사/할인 내역 > ERP행사
							viewModel.ilGoodsDetail.set("isGoodsDiscountErpEventListNoDataTbody", false);							//가격 정보 > 행사/할인 내역 > ERP행사 > Nodata 항목
							viewModel.ilGoodsDetail.set("isGoodsDiscountErpEventListTbody", true);									//가격 정보 > 행사/할인 내역 > ERP행사 > Data 항목
						}
						else {
							viewModel.ilGoodsDetail.set("goodsDiscountErpEventList", []);											//가격 정보 > 행사/할인 내역 > ERP행사
							viewModel.ilGoodsDetail.set("isGoodsDiscountErpEventListNoDataTbody", true);							//가격 정보 > 행사/할인 내역 > ERP행사 > Nodata 항목
							viewModel.ilGoodsDetail.set("isGoodsDiscountErpEventListTbody", false);									//가격 정보 > 행사/할인 내역 > ERP행사 > Data 항목
						}

						if(data.goodsDiscountImmediateList.length > 0){
							viewModel.ilGoodsDetail.set("goodsDiscountImmediateList", data.goodsDiscountImmediateList);				//가격 정보 > 행사/할인 내역 > 즉시할인
							viewModel.ilGoodsDetail.set("isGoodsDiscountImmediateListNoDataTbody", false);							//가격 정보 > 행사/할인 내역 > 즉시할인 > Nodata 항목
							viewModel.ilGoodsDetail.set("isGoodsDiscountImmediateListTbody", false);									//가격 정보 > 행사/할인 내역 > 즉시할인 > Data 항목
						}
						else{
							viewModel.ilGoodsDetail.set("goodsDiscountImmediateList", []);											//가격 정보 > 행사/할인 내역 > 즉시할인
							viewModel.ilGoodsDetail.set("isGoodsDiscountImmediateListNoDataTbody", false);							//가격 정보 > 행사/할인 내역 > 즉시할인 > Nodata 항목
							viewModel.ilGoodsDetail.set("isGoodsDiscountImmediateListTbody", false);								//가격 정보 > 행사/할인 내역 > 즉시할인 > Data 항목
						}

						//현재 임직원 할인 가격정보에 대한 테이블이 없으므로 무조건 visible 처리
						if(data.goodsEmployeePrice.length > 0){
							viewModel.ilGoodsDetail.set("goodsEmployeePrice", data.goodsEmployeePrice);							//임직원 할인정보 > 임직원 할인 가격정보
							viewModel.ilGoodsDetail.set("goodsEmployeePriceNoDataVisible", false);								//임직원 할인정보 > 임직원 할인 가격정보 Nodata Visible
							viewModel.ilGoodsDetail.set("goodsEmployeePriceDataVisible", true);									//임직원 할인정보 > 임직원 할인 가격정보 Nodata Visible
						}
						else {
							viewModel.ilGoodsDetail.set("goodsEmployeePrice", []);												//임직원 할인정보 > 임직원 할인 가격정보
							viewModel.ilGoodsDetail.set("goodsEmployeePriceNoDataVisible", true);								//임직원 할인정보 > 임직원 할인 가격정보 Nodata Visible
							viewModel.ilGoodsDetail.set("goodsEmployeePriceDataVisible", false);								//임직원 할인정보 > 임직원 할인 가격정보 Nodata Visible
						}

						if(data.goodsDiscountEmployeeList.length > 0){
							viewModel.ilGoodsDetail.set("goodsDiscountEmployeeList", data.goodsDiscountEmployeeList);				//임직원 할인정보 > 임직원 개별할인 정보
							viewModel.ilGoodsDetail.set("goodsDiscountEmployeeListNoDataVisible", false);							//임직원 할인정보 > 임직원 개별할인 정보 > Nodata Visible
							viewModel.ilGoodsDetail.set("goodsDiscountEmployeeListInsertTbodyVisible", false);						//임직원 할인정보 > 임직원 개별할인 정보 > 초기 Insert Visible
							viewModel.ilGoodsDetail.set("goodsDiscountEmployeeListDataVisible", true);								//임직원 할인정보 > 임직원 개별할인 정보 > Data Visible
						}
						else{
							viewModel.ilGoodsDetail.set("goodsDiscountEmployeeList", []);											//임직원 할인정보 > 임직원 개별할인 정보
							viewModel.ilGoodsDetail.set("goodsDiscountEmployeeListNoDataVisible", false);							//임직원 할인정보 > 임직원 개별할인 정보 > Nodata Visible
							viewModel.ilGoodsDetail.set("goodsDiscountEmployeeListInsertTbodyVisible", true);						//임직원 할인정보 > 임직원 개별할인 정보 > 초기 Insert Visible
							viewModel.ilGoodsDetail.set("goodsDiscountEmployeeListDataVisible", false);								//임직원 할인정보 > 임직원 개별할인 정보 > Data Visible
						}

						viewModel.ilGoodsDetail.set("visibleGoodsReservationAllDel", false);					//예약 판매 옵션 설정 > 일정 초기화 버튼 Visible

						if(data.goodsReservationOptionList){
							for(var i=0; i < data.goodsReservationOptionList.length; i++){
								var goodsReservationOption = data.goodsReservationOptionList[i];

								viewModel.goodsReservationOptionList.add({
									ilGoodsReserveOptionId : goodsReservationOption.ilGoodsReserveOptionId,		//ID
									goodsReservationOptionChk : false,											//체크박스
									optnDelAllow : goodsReservationOption.optnDelAllow,							//삭제가능여부
									saleSequance : goodsReservationOption.saleSequance,							//회차
									reservationStartDate : goodsReservationOption.reservationStartDate,			//예약 주문 가능 기간 시작일
									reservationStartDateOld : goodsReservationOption.reservationStartDate,		//예약 주문 가능 기간 시작일(이전 값)
									reservationStartHour : goodsReservationOption.reservationStartHour,			//예약 주문 가능 기간 시작 시간
									reservationStartHourOld : goodsReservationOption.reservationStartHour,		//예약 주문 가능 기간 시작 시간(이전 값)
									reservationStartMinute : goodsReservationOption.reservationStartMinute,		//예약 주문 가능 기간 시작 분
									reservationStartMinuteOld : goodsReservationOption.reservationStartMinute,	//예약 주문 가능 기간 시작 분(이전 값)
									reservationEndDate : goodsReservationOption.reservationEndDate,				//예약 주문 가능 기간 종료일
									reservationEndDateOld : goodsReservationOption.reservationEndDate,			//예약 주문 가능 기간 종료일(이전 값)
									reservationEndHour : goodsReservationOption.reservationEndHour,				//예약 주문 가능 기간 종료 시간
									reservationEndHourOld : goodsReservationOption.reservationEndHour,			//예약 주문 가능 기간 종료 시간(이전 값)
									reservationEndMinute : goodsReservationOption.reservationEndMinute,			//예약 주문 가능 기간 종료 분
									reservationEndMinuteOld : goodsReservationOption.reservationEndMinute,		//예약 주문 가능 기간 종료 분(이전 값)
									stockQuantity : goodsReservationOption.stockQuantity,						//주문재고
									orderIfDate : goodsReservationOption.orderIfDate,							//주문수집 I/F일
									orderIfDateOld : goodsReservationOption.orderIfDate,						//주문수집 I/F일(이전 값)
									releaseDate : goodsReservationOption.releaseDate,							//출고예정일
									arriveDate : goodsReservationOption.arriveDate								//도착예정일
								});

                                if (!goodsReservationOption.optnDelAllow) {
                                    $("#fnOrderIfDateCalc-id" + goodsReservationOption.saleSequance).attr("class", "btn-ligntgray");
                                    $("#raaCalc-btn-id" + goodsReservationOption.saleSequance).attr("class", "btn-ligntgray");
                                }
                            }
                        }

						//viewModel.goodsReservationOptionList.data(data.goodsReservationOptionList);								//판매정보 > 판매유형(예약판매) > 예약판매 옵션설정 리스트 내역
						//console.log("viewModel.goodsReservationOptionList", viewModel.goodsReservationOptionList);

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

						//판매정보 > 판매 유형 Visible
						switch(viewModel.ilGoodsDetail.saleType) {
							case 'SALE_TYPE.SHOP' :
								viewModel.ilGoodsDetail.set('isSaleType', false);
								$("#saleTypeTh").attr("rowspan",1);
								break;
							case 'SALE_TYPE.REGULAR' :
								viewModel.ilGoodsDetail.set('isSaleType', false);
								$("#saleTypeTh").attr("rowspan",1);
								break;
							case 'SALE_TYPE.RESERVATION' :
								viewModel.ilGoodsDetail.set('isSaleType', true);
								$("#saleTypeTh").attr("rowspan",2);
								break;
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

						//console.log("data.exhibitGiftList : ", data.exhibitGiftList);
						//혜택/구매 정보 > 증정행사
						if(data.exhibitGiftList) {
							for(var i=0; i < data.exhibitGiftList.length; i++){
								exhibitGiftGridDs.insert(i, data.exhibitGiftList[i]);
							}
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

						// 승인정보 관련 화면 제어(goodsApprovalComm.js)
//						fnApprHtml(data.ilGoodsDetail);
						//상품 수정 상세 화면 진입시 추가 매핑
						fnGoodsApprDetail(pageParam.ilGoodsApprId);

						// PJH Start
						if( data.ilGoodsDetail.noticeBelow1ImageUrl ) {  // 상세 상단공지 ImageUrl 조회시
						    viewModel.set('showNoticeBelow1Date', true); // 상세 상단공지 노출기간 Visible 처리

						    //HGRM-1646 초기화 및 시간달력포맷 공통화 수정
						  //   fnStartCalChange('noticeBelow1StartYear', 'noticeBelow1EndYear');
							// fnEndCalChange('noticeBelow1StartYear', 'noticeBelow1EndYear');
						}

                        if( data.ilGoodsDetail.noticeBelow2ImageUrl ) {  // 상세 하단공지 ImageUrl 조회시
                            viewModel.set('showNoticeBelow2Date', true); // 상세 하단공지 노출기간 Visible 처리

                            //HGRM-1646 초기화 및 시간달력포맷 공통화 수정
						  //   fnStartCalChange('noticeBelow2StartYear', 'noticeBelow2EndYear');
							// fnEndCalChange('noticeBelow2StartYear', 'noticeBelow2EndYear');
                        }

						// PJH End
                        var goodsShippingTemplateList = [];

                        //console.log("data.goodsShippingTemplateList", data.goodsShippingTemplateList);

                        if(data.goodsShippingTemplateList){
                        	goodsShippingTemplateList = data.goodsShippingTemplateList;
                        }

						itemKeyList.push({
							ilItemCode : data.ilGoodsDetail.ilItemCode
						,	urWarehouseId : data.ilGoodsDetail.urWarehouseId
						,	goodsShippingTemplateList : goodsShippingTemplateList
						});

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
		return itemKeyList;
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

	// 마스터 품목 리스트에서 선택된 마스터 품목 모든 정보 가져오기
	function fnMasterItemAll(goodsShippingTemplateList){
		fnAjax({
			url		: '/admin/goods/regist/getItemDetail',
			params	: {ilItemCode : ilItemCode, urWarehouseId : urWarehouseId},
			isAction : 'select',
			success	:
				function( data ){
					$("#itemCreateDate").html(data.row.createDate + " / " + data.row.userName + "(" + data.row.itemCreateLoginId + ")");	//품목등록일자

					var itemModifyDate = "";

					if(data.row.modifyDate && data.row.modifyUserName && data.row.modifyUserId){
						itemModifyDate = data.row.modifyDate + " / " + data.row.modifyUserName + "(" + data.row.modifyUserId + ")";
					}
					else if(data.row.modifyDate){
						itemModifyDate = data.row.modifyDate + " / 시스템";
					}

					$("#itemModifyDate").html(itemModifyDate);																				//최근 수정일
					$("#masterItemType").html(data.row.itemTpName);																			//마스터 품목 유형
					$("#masterItemCode").html(data.row.ilItemCode);																			//마스터 품목 코드
					$("#masterItemBarCode").html(data.row.itemBarcode);																		//마스터 품목 바코드
					$("#masterItemName").html(data.row.masterItemName);																		//마스터 품목명

					var packageInfo = data.row.sizePerPackage + " " + data.row.sizeUnitName;

					if(data.row.qtyPerPackage > 0 && data.row.packageUnitName != null){
						packageInfo += " x " + data.row.qtyPerPackage + data.row.packageUnitName;
					}

					$("#itemSizePerPackage").html("포장용량 구성정보 : " + packageInfo)	//용량표시
					$("#categoryStandardName").html(data.row.categoryStandardName);															//표준카테고리 명

					erpItemLinkYn = data.row.erpIfYn;
					switch (data.row.erpIfYn){
						case 'Y' :
							$("#erpItemLinkYn").html("예");																					//ERP 품목연동 여부
							$("#erpItemName").html(data.row.erpItemName);																	//ERP 품목명
							$("#erpCategoryName").html(data.row.erpCategoryName);															//ERP 카테고리명
							$("shopOnlyItemName").html();																					//매장전용품목유형, 건강생활상품유형
							$("#erpCtgry").html();																							//ERP 카테고리

							break;
						case 'N' :
							$("#erpItemLinkYn").html("아니오");
							$("#erpItemName").html("");
							$("#shopOnlyItemName").html("");
							$("#erpCtgry").html("");

							break;
					}

					$("#erpO2oExposureType").html(data.row.erpO2oExposureType);																//매장전용품목유형
					$("#erpProductType").html(data.row.erpProductType);																		//건강생활상품유형

					/* 상세 정보 항목 Start */
					$("#supplierName").html(data.row.supplierName);																			//공급업체
					supplierName = data.row.supplierName;																					//공급업체명 젼역변수(몰인몰 카테고리 Display 관련)

					var itemGroup = data.row.erpIfYn == 'Y' ? data.row.erpItemGroup : data.row.itemGroup;
					var distributionPeriod = data.row.erpIfYn == 'Y' ? data.row.erpDistributionPeriod : data.row.distributionPeriod;

					let erpBoxWidth = data.row.erpBoxWidth != "" ? Number(data.row.erpBoxWidth) : data.row.erpBoxWidth;
					let erpBoxDepth = data.row.erpBoxDepth != "" ? Number(data.row.erpBoxDepth) : data.row.erpBoxDepth;
					let erpBoxHeight = data.row.erpBoxHeight != "" ? Number(data.row.erpBoxHeight) : data.row.erpBoxHeight;

					let boxWidth = data.row.boxWidth != "" ? Number(data.row.boxWidth) : data.row.boxWidth;
					let boxDepth = data.row.boxDepth != "" ? Number(data.row.boxDepth) : data.row.boxDepth;
					let boxHeight = data.row.boxHeight != "" ? Number(data.row.boxHeight) : data.row.boxHeight

					var boxVolume = data.row.erpIfYn == 'Y' ? erpBoxWidth+" x "+erpBoxDepth+" x "+erpBoxHeight : boxWidth+" x "+boxDepth+" x "+boxHeight;

					var pcsPerBox = data.row.erpIfYn == 'Y' ? data.row.erpPcsPerBox : data.row.pcsPerBox;
					var uom = data.row.uom == null ? '' :  "/" + data.row.uom;
					var originDetailText = ( ! data.row.originDetail ? '' : " / " + data.row.originDetail );
					var qtyPerPackage = data.row.qtyPerPackage == 0 ? '' : data.row.qtyPerPackage;

					$("#brandName").html(data.row.brandName);																				//표준 브랜드
					$("#dpBrandName").html(data.row.dpBrandName);																			//전시 브랜드

					$("#itemGroup").html(itemGroup);																						//상품군
					$("#storageMethodName").html(data.row.storageMethodName);																//보관방법
					$("#originTypeValue").html(data.row.originType+originDetailText);														//원산지
					$("#distributionPeriod").html(distributionPeriod);																		//유통기간
					$("#boxVolume").html(boxVolume);																						//박스체적
					$("#pcsPerBox").html(pcsPerBox+uom);																					//박스입수량 + UOM

					$("#sizePerPackage").html(data.row.sizePerPackage);																		//포장단위별 용량
					$("#sizeUnitName").html(data.row.sizeUnitName);																			//포장용량(중량) 단위

					var pdmGroupCode = data.row.pdmGroupCode == 0 ? '' : data.row.pdmGroupCode;

					$("#qtyPerPackage").html(qtyPerPackage);																				//구성 정보 용량
					$("#packageUnitName").html(data.row.packageUnitName);																	//구성 정보 단위
					$("#pdmGroupCode").html(pdmGroupCode);																					//PDM 그룹코드
					/* 상세 정보 항목 End */

					var taxYnText = data.row.taxYn == "Y" ? '과세' : '면세';
					$("#taxYn").html(taxYnText);
					viewModel.ilGoodsDetail.taxYn = data.row.taxYn;																			//과세구분(할인설정 세팅을 위한 기초 데이터)

					//기본 정보 > 몰인몰 카테고리 > Visible 여부
					urSupplierId = data.row.urSupplierId;
					urBrandId = data.row.urBrandId;
					supplierCode = data.row.supplierCode;

					fnItemWarehouseList(goodsShippingTemplateList);		// 배송/발주 정보 > 배송유형 > 출고처에 따른 배송유형별 리스트

					if(supplierCode == "OG"){																								//공급처가 올가홀푸드이면
						viewModel.set("isMallInMallVisible", true);
					}
					else if(supplierCode == "DM" && data.row.mallinmallCategoryId){															//공급처가 풀무원 녹즙(PDM) 이고 브랜드가 잇슬림이면
						viewModel.set("isMallInMallVisible", true);
					}

					//판매/전시 시작
					if(data.goodsStockInfo){
						viewModel.ilGoodsDetail.set('stockOperationForm', data.goodsStockInfo.stockOperationForm + data.goodsStockInfo.stockOperationFormSub);
						viewModel.ilGoodsDetail.set('stockQuantity', data.goodsStockInfo.stockQuantity);
						viewModel.ilGoodsDetail.set('goodsStockInfo', data.goodsStockInfo);

						if(data.goodsStockInfo.stockOrderYn == "Y") {
							viewModel.ilGoodsDetail.set('stockQuantityName', '전일마감재고');
							viewModel.ilGoodsDetail.set('visibleStockDetailPopupButton', true);
							if (data.goodsStockInfo.preOrderYn == "N") {
								viewModel.ilGoodsDetail.set('visibleStockChangeButton', false);
							} else if (data.goodsStockInfo.preOrderYn == "Y") {
								viewModel.ilGoodsDetail.set('visibleStockChangeButton', true);
							}
						}
						else if(data.goodsStockInfo.stockOrderYn == "N") {
							viewModel.ilGoodsDetail.set('stockQuantityName', '재고');
							viewModel.ilGoodsDetail.set('visibleStockDetailPopupButton', false);
						}
					}
					//판매/전시 끝

					//가격 정보 > 단위별 용량 정보 기본 자동표기 설정
					switch(viewModel.ilGoodsDetail.autoDisplaySizeYn) {
						case 'Y' :
							viewModel.ilGoodsDetail.set('isAutoSizeValue', true);
							viewModel.ilGoodsDetail.set('isSizeEtc', false);
							break;
						case 'N' :
							viewModel.ilGoodsDetail.set('isAutoSizeValue', false);
							viewModel.ilGoodsDetail.set('isSizeEtc', true);
							break;
					}

					//단위별 용량정보 > 자동 표기일 경우 값 계산 처리 Start
					var autoSizeValueText = "";
					var goodsPriceSalePrice = 0;
					var itemPriceListRecommendedPrice = 0;

					if(viewModel.ilGoodsDetail.get("goodsPrice").length > 0){
						goodsPriceSalePrice = viewModel.ilGoodsDetail.get("goodsPrice")[0].salePrice;						//판매 가격정보 > 판매가
					}

					if(viewModel.ilGoodsDetail.get("itemPriceList")){
						itemPriceListRecommendedPrice = viewModel.ilGoodsDetail.get("itemPriceList")[0].recommendedPrice;	//마스터 품목 가격정보 > 정상가
					}

					if(goodsPriceSalePrice > 0 || itemPriceListRecommendedPrice > 0){
						var packagePrice = "";
						if(goodsPriceSalePrice > 0) {					//판매 가격정보 > 판매가가 존재한다면
							packagePrice = goodsPriceSalePrice;
						}
						else{
							packagePrice = itemPriceListRecommendedPrice;
						}

						autoSizeValueText = "판매가 / (포장단위별 용량) * (패키지 수량) = " + kendo.format("{0:\#\#,\#}", Number(packagePrice)) + "원 / " + data.row.sizePerPackage + data.row.sizeUnitName;
						if(data.row.qtyPerPackage > 0 && data.row.packageUnitName != null){
							autoSizeValueText += " * " + data.row.qtyPerPackage + data.row.packageUnitName;
						}

						var qtyPerPackage = data.row.qtyPerPackage == 0 ? 1 : data.row.qtyPerPackage;
						var pagkagePerPrice = 0
						if(data.row.sizePerPackage > 0 && data.row.sizePerPackage <= 500){
							pagkagePerPrice = Math.ceil(packagePrice / (data.row.sizePerPackage * qtyPerPackage) * 10);
							autoSizeValueText += " = 10" + data.row.sizeUnitName + " 당 " + kendo.format("{0:\#\#,\#}", Number(pagkagePerPrice)) + "원";
							viewModel.ilGoodsDetail.set("isSizeEtcPlaceholder", "10" + data.row.sizeUnitName + " 당 " + kendo.format("{0:\#\#,\#}", Number(pagkagePerPrice)) + "원");
						}
						else if(data.row.sizePerPackage > 0 && data.row.sizePerPackage > 500){
							pagkagePerPrice = Math.ceil(packagePrice / (data.row.sizePerPackage * qtyPerPackage) * 100);
							autoSizeValueText += " = 100" + data.row.sizeUnitName + " 당 " + kendo.format("{0:\#\#,\#}", Number(pagkagePerPrice)) + "원";
							viewModel.ilGoodsDetail.set("isSizeEtcPlaceholder", "100" + data.row.sizeUnitName + " 당 " + kendo.format("{0:\#\#,\#}", Number(pagkagePerPrice)) + "원");
						}
						else {
							viewModel.ilGoodsDetail.set("isSizeEtcPlaceholder", "");
						}
					}

					viewModel.ilGoodsDetail.set("autoSizeValue", autoSizeValueText);
					//단위별 용량정보 > 자동 표기일 경우 값 계산 처리 End

					/* 임직원 할인 정보 Start */
					if(data.goodsBaseDiscountEmployeeList != null){
						viewModel.ilGoodsDetail.set('goodsBaseDiscountEmployeeList', []);			//임직원 할인 정보 > 임직원 기본할인 정보

						var discountStartDateTime = data.goodsBaseDiscountEmployeeList.discountStartDateTime;
						if(viewModel.ilGoodsDetail.goodsCreateDate != null && viewModel.ilGoodsDetail.goodsCreateDate != ""){
							discountStartDateTime = kendo.toString(kendo.parseDate(viewModel.ilGoodsDetail.goodsCreateDate), "yyyy-MM-dd HH:mm");
						}
						else{
							discountStartDateTime = data.goodsBaseDiscountEmployeeList.discountStartDateTime;
						}

						viewModel.ilGoodsDetail.get('goodsBaseDiscountEmployeeList').push({
							discountTypeCodeName	: "기본",
							ilItemPriceId			: data.goodsBaseDiscountEmployeeList.ilItemPriceId,
							discountStartDateTime	: discountStartDateTime,
							discountEndDateTime		: data.goodsBaseDiscountEmployeeList.discountEndDateTime,
							standardPrice			: data.goodsBaseDiscountEmployeeList.standardPrice,
							recommendedPrice		: data.goodsBaseDiscountEmployeeList.recommendedPrice,
							discountRatio			: data.goodsBaseDiscountEmployeeList.discountRatio,
							discountSalePrice		: data.goodsBaseDiscountEmployeeList.discountSalePrice,
							marginRate				: data.goodsBaseDiscountEmployeeList.marginRate
						});

						viewModel.ilGoodsDetail.set("goodsBaseDiscountEmployeeListNoDataVisible", false);
						viewModel.ilGoodsDetail.set("goodsBaseDiscountEmployeeListDataVisible", true);
					}
					else {
						viewModel.ilGoodsDetail.set('goodsBaseDiscountEmployeeList', []);			//임직원 할인 정보 > 임직원 기본할인 정보
						viewModel.ilGoodsDetail.set("goodsBaseDiscountEmployeeListNoDataVisible", true);
						viewModel.ilGoodsDetail.set("goodsBaseDiscountEmployeeListDataVisible", false);
					}
					/* 임직원 할인 정보 End */

					//판매 정보 > 판매 유형 > 매장판매
					if(data.row.supplierCode == "OG" && data.row.storeYn == "Y"){															//공급처가 올가홀푸드이고, 해당 출고처의 매장(가맹점)여부가 Y이면
						viewModel.ilGoodsDetail.set("saleShopYn", "Y");
						$("input[name=saleShopYn]:eq(0)").prop('checked', true);
						viewModel.ilGoodsDetail.set("tiemStoreInfoNoDataTbody", false);
						viewModel.ilGoodsDetail.set("tiemStoreInfoDataTbody", true);
						viewModel.ilGoodsDetail.set("tiemStoreInfo", data.itemStoreList);
					}
					else{
						viewModel.ilGoodsDetail.set("saleShopYn", 'N');
						$("input[name=saleShopYn]:eq(0)").prop('checked', false);
					}

					//배송/발주 정보 > 발주 가능여부(올가 ERP) Visible 여부
					if(data.row.erpIfYn == "Y" && data.row.supplierCode == "OG"){															//공급처가 올가홀푸드이고, ERP 연동 품목 이면
						viewModel.set("isOrderKindNonErp", false);
						viewModel.set("isOrderKindErp", true);
					}
					else{
						viewModel.set("isOrderKindNonErp", true);
						viewModel.set("isOrderKindErp", false);
					}

					//배송/발주 정보 시작
					switch (data.row.erpCanPoYn){																							//발주 가능여부 (올가ERP)
						case 'Y' :
							$("#erpCanPoYn").html("예");
							break;
						case 'N' :
							$("#erpCanPoYn").html("아니오");
							break;
					}

					$("#undeliverableAreaTypeName").html(data.row.undeliverableAreaTypeName);												//배송 불가 지역

					if(data.row.erpIfYn == "Y"){
						var poTypeName = data.row.erpPoType;

						if(data.row.poTypeName){
							poTypeName += " / " + data.row.poTypeName;
						}

						if(data.row.poTypeName && data.row.poPerItemYn == "Y"){
							poTypeName += " <button type='button' class='btn-point btn-s' onclick='$scope.fnPoTypeDetailInformationPopup()'>상세보기</button>";
						}
					}

					$("#orderKind").html(poTypeName);								//발주 유형

					//표준 카테고리와 보관방법에 따른 반품 가능 기간 정보 불러오기
					fnAjax({
						url : "/admin/item/master/register/getReturnPeriod",
						method : 'GET',
						params : {
							ilCategoryStandardId : data.row.ilCategoryStadardId,	// 표준 카테고리 PK
							storageMethodType : data.row.storageMethodType			// 보관방법
						},
						isAction : 'select',
						success : function(data, status, xhr) {
							$("#returnPeriod").html(data['returnPeriodMessage']);															//반품 가능 기간
						}
					});
					//배송/발주 정보 항목 끝

					//혜택/구매 정보
					undeliverableAreaType = data.row.undeliverableAreaType																	//추가상품검색 팝업 고정 파라미터 값(배송불가지역 ID)

					$("#basicDescription").html(data.row.basicDescription);																	//상품 상세 기본 정보
					$("#detailDescription").html(data.row.detailDescription);																//상품 상세 주요 정보
					$("#etcDescription").html(data.row.etcDescription);																		//마스터 품목 메모

					if(data.row.ilItemCode.length > 0){
						//마스터 품목 상품정보 제공고시
						var specMasterName = data.row.specMasterName;

						fnAjax({
							url		: '/admin/goods/regist/getItemSpecValueList',
							params	: {
								ilItemCode : ilItemCode
							,	urSupplierId: urSupplierId
							,	urWarehouseId : urWarehouseId
							,	distributionPeriod : distributionPeriod
							},
							isAction : 'select',
							success	:
								function( data ){
									var itemSpecValueHtml = "";

									itemSpecValueHtml += "<table class='datatable v-type'>";
									itemSpecValueHtml += "		<colgroup>";
									itemSpecValueHtml += "			<col style='width: 15%'>";
									itemSpecValueHtml += "			<col style='width: 85%'>";
									itemSpecValueHtml += "		</colgroup>";
									itemSpecValueHtml += "		<tr>";
									itemSpecValueHtml += "			<th scope='row' colspan='2' class='active' style='text-align: left;'><label style='font-size: 20px;'>상품정보 제공고시</label></th>";
									itemSpecValueHtml += "		</tr>";
									itemSpecValueHtml += "		<tr>";
									itemSpecValueHtml += "			<th scope='row'><label>상품군</label></th>";
									itemSpecValueHtml += "			<td>"+specMasterName+"</td>";
									itemSpecValueHtml += "		</tr>";

									if(data.rows.length > 0){

										for(var i=0; i < data.rows.length; i++){
											itemSpecValueHtml += "	<tr>";
											itemSpecValueHtml += "		<th scope='row'><label>"+data.rows[i].specFieldName+"</label></th>";
											itemSpecValueHtml += "		<td>"+data.rows[i].specFieldValue+"</td>";
											itemSpecValueHtml += "	</tr>";
										}
									}
									else{
										$("#itemSpecValueContent").html("");
									}

									itemSpecValueHtml += "</table>";

									$("#itemSpecValueContent").html(itemSpecValueHtml);
								}
						});


						//마스터 품목 상품 영양정보
						var nutritionDisplayYnText = "";
						if(data.row.nutritionDisplayYn == "Y"){
							nutritionDisplayYnText = "정보 노출";
						}

						var nutritionQuantityText = "";

						if(data.row.nutritionQuantityPerOnce != "" && data.row.nutritionQuantityTotal != ""){
							nutritionQuantityText = "1회 분량 "+data.row.nutritionQuantityPerOnce+" 총"+data.row.nutritionQuantityTotal+"회분";
						}

						var nutritionEtcText = data.row.nutritionEtc;
						var itemNutritionHtml = "";

						if(data.row.nutritionDisplayYn == "Y"){		//상품 영양정보 > 영양정보 표시여부가 '정보 노출'일 경우에만 표시
							fnAjax({
								url		: '/admin/goods/regist/getItemNutritionList',
								params	: {ilItemCode : ilItemCode},
								isAction : 'select',
								success	:
									function( data ){
										var nutritionLength = data.rows.length;
										var trLength = Math.ceil(nutritionLength / 2);

										itemNutritionHtml += "<table class='datatable v-type'>";
										itemNutritionHtml += "	<colgroup>";
										itemNutritionHtml += "		<col style='width: 15%'>";
										itemNutritionHtml += "		<col style='width: 17%'>";
										itemNutritionHtml += "		<col style='width: 16%'>";
										itemNutritionHtml += "		<col style='width: 15%'>";
										itemNutritionHtml += "		<col style='width: 17%'>";
										itemNutritionHtml += "		<col style='width: 20%'>";
										itemNutritionHtml += "	</colgroup>";
										itemNutritionHtml += "	<tr>";
										itemNutritionHtml += "		<th scope='row' colspan='6' class='active' style='text-align: left;'><label style='font-size: 20px;'>상품 영양정보</label></th>";
										itemNutritionHtml += "	</tr>";

										if(nutritionLength > 0){

											itemNutritionHtml += "	<tr>";
											itemNutritionHtml += "		<th scope='row'><label>영양정보 표시여부</label></th>";
											itemNutritionHtml += "		<td colspan='5'>"+nutritionDisplayYnText+"</td>";
											itemNutritionHtml += "	</tr>";
											itemNutritionHtml += "	<tr>";
											itemNutritionHtml += "		<th scope='row'><label>영양분석단위</label></th>";
											itemNutritionHtml += "		<td colspan='5'>"+nutritionQuantityText+"</td>";
											itemNutritionHtml += "	</tr>";

											var nutritionNameText = "";
											var nutritionQtyText = "";
											var nutritionPercentText = "";
											var k = 0;

											for(var i=0; i < trLength; i++){
												itemNutritionHtml += "<tr>";

												for(var j=1; j < 3; j++){
													k = (i+j+i)-1;
													if(k <= nutritionLength-1){

														nutritionNameText = data.rows[k].nutritionName == null ? '' : data.rows[k].nutritionName;
														nutritionQtyText = data.rows[k].nutritionQty == null ? '' : data.rows[k].nutritionQty+" "+data.rows[k].nutritionUnit;
														nutritionPercentText = data.rows[k].nutritionPercent == null ? '' : "영양성분기준치 "+data.rows[k].nutritionPercent+" %";

														itemNutritionHtml += "	<th scope='row'><label>"+nutritionNameText+"</label></th>";
														itemNutritionHtml += "	<td style='text-align:center'>"+nutritionQtyText+"</td>";
														itemNutritionHtml += "	<td style='text-align:center'>"+nutritionPercentText+"</td>";
													}
													else{
														itemNutritionHtml += "	<th scope='row'><label></label></th>";
														itemNutritionHtml += "	<td style='text-align:center'></td>";
														itemNutritionHtml += "	<td style='text-align:center'></td>";
													}
												}
												itemNutritionHtml += "</tr>";
											}

											itemNutritionHtml += "<tr>";
											itemNutritionHtml += "	<th scope='row'><label>영양성분 기타</label></th>";
											itemNutritionHtml += "	<td colspan='5'><div style='white-space:pre;'>"+nutritionEtcText+"</div></td>";
											itemNutritionHtml += "</tr>";
										}
										else{
											$("#itemNutritionValueContent").html("");
										}

										itemNutritionHtml += "</table>";

										$("#itemNutritionValueContent").html(itemNutritionHtml);
									}
							});
						}
						else{
							itemNutritionHtml += "<table class='datatable v-type'>";
							itemNutritionHtml += "	<colgroup>";
							itemNutritionHtml += "		<col style='width: 16%'>";
							itemNutritionHtml += "		<col style='width: 84%'>";
							itemNutritionHtml += "	</colgroup>";
							itemNutritionHtml += "	<tr>";
							itemNutritionHtml += "		<th scope='row' colspan='2' class='active' style='text-align: left;'><label style='font-size: 20px;'>상품 영양정보</label></th>";
							itemNutritionHtml += "	</tr>";
							itemNutritionHtml += "	<tr>";
							itemNutritionHtml += "		<th scope='row'><label>영양정보 표시여부</label></th>";
							itemNutritionHtml += "		<td>"+data.row.nutritionDisplayDefault+"</td>";
							itemNutritionHtml += "	</tr>";
							itemNutritionHtml += "	</table>";

							$("#itemNutritionValueContent").html(itemNutritionHtml);
						}

						//상품 인증정보
						fnAjax({
							url		: '/admin/goods/regist/getItemCertificationList',
							params	: {ilItemCode : ilItemCode},
							isAction : 'select',
							success	:
								function( data ){
									var itemCertificationHtml = "";
									itemCertificationHtml += "<table class='datatable v-type'>";
									itemCertificationHtml += "	<colgroup>";
									itemCertificationHtml += "		<col style='width: 140px;'>";
									itemCertificationHtml += "		<col style='width: 15%'>";
									itemCertificationHtml += "		<col>";
									itemCertificationHtml += "	</colgroup>";
									itemCertificationHtml += "	<tr>";
									itemCertificationHtml += "		<th scope='row' colspan='3' class='active' style='text-align: left;'><label style='font-size: 20px;'>상품 인증정보</label></th>";
									itemCertificationHtml += "	</tr>";

									if(data.rows.length>0){
										for(var i=0; i < data.rows.length; i++){
											itemCertificationHtml += "	<tr>";
											// PJH Start
											itemCertificationHtml += "		<th scope='row' style='padding: 5px;'><img src='" + publicStorageUrl + data.rows[i].imagePath + data.rows[i].imageName + "' style='width:130px;height:130px;'></th>";
											// PJH End
											itemCertificationHtml += "		<th scope='row'>"+data.rows[i].certificationName+"</th>";
											itemCertificationHtml += "		<td>"+data.rows[i].certificationDesc+"</td>";
											itemCertificationHtml += "	</tr>";
										}

										$("#itemCertificationContent").html(itemCertificationHtml);
									}
									else{
										$("#itemCertificationContent").html("");
									}

									itemCertificationHtml += "</table>";

									$("#itemCertificationContent").html(itemCertificationHtml);
								}
						});

						//상품 상세 이미지
						//마스터 품목 > 상품 이미지 > 동영상 URL
						var videoUrlText = data.row.videoUrl == null ? '' : data.row.videoUrl;

						//마스터 품목 > 상품 이미지 > 동영상 URL > 자동재생 여부
						var videoAutoplay = data.row.videoAutoplayYn == "Y" ? "checked" : "";

						fnAjax({
							url		: '/admin/goods/regist/getItemImageList',
							params	: {ilItemCode : ilItemCode},
							isAction : 'select',
							success	:
								function( data ){
									var itemImageHtml = "";
									if(data.rows.length>0){
										itemImageHtml += "<table class='datatable v-type'>";
										itemImageHtml += "	<colgroup>";
										itemImageHtml += "		<col style='width: 15%'>";
										itemImageHtml += "		<col style='width: 85%'>";
										itemImageHtml += "	</colgroup>";
										itemImageHtml += "	<tr>";
										itemImageHtml += "		<th scope='row' colspan='2' class='active' style='text-align: left;'><label style='font-size: 20px;'>상품 상세 이미지</label></th>";
										itemImageHtml += "	</tr>";
										itemImageHtml += "	<tr>";
                                        // PJH Start
                                        itemImageHtml += "      <th scope='row' class='must'><label>상품 이미지 ( " + data.rows.length + " / 10 )</label></th>";
										// PJH End
										itemImageHtml += "		<td>";
										itemImageHtml += "			<div style='position:relative; display: flex; flex-direction: row; flex-wrap: wrap; justify-content: left;'>";
										for(var i=0; i < data.rows.length; i++){
										    // PJH Start
										    if(i == 0) {

                                                itemImageHtml += "      <span style='width:150px; height:150px; margin-bottom:10px; margin-right:10px; border-color:red; border-style:solid; border-width:3px'>";
                                                itemImageHtml += "          <span style='display:block; font-size: 20px; width:100px; height:25px; position:absolute; top:3px; left:3px; background:white; overflow:hidden;'";
                                                itemImageHtml += "          >대표 이미지</span>";
                                                itemImageHtml += "          <span>";
                                                itemImageHtml += "              <img src='" + publicStorageUrl + data.rows[i].itemImageName + "' style='max-width: 100%; max-height: 100%;'>";
                                                itemImageHtml += "          </span>";
                                                itemImageHtml += "      </span>";

										    } else {

	                                            itemImageHtml += "      <span style='width:150px; height:150px; margin-bottom:10px; margin-right:10px; border-color:#a9a9a9; border-style:solid; border-width:1px'>";
	                                            itemImageHtml += "          <img src='" + publicStorageUrl + data.rows[i].itemImageName + "' style='max-width: 100%; max-height: 100%;'>";
	                                            itemImageHtml += "      </span>";

										    }
										    // PJH End
										}
										itemImageHtml += "			</div>";
										itemImageHtml += "		</td>";
										itemImageHtml += "	</tr>";
										itemImageHtml += "	<tr>";
										itemImageHtml += "		<th scope='row'><label>동영상 정보&nbsp;&nbsp;&nbsp;<input type='checkbox' class='checkbox-wrapper fb__custom__checkbox' "+videoAutoplay+" disabled>&nbsp;자동재생</label></th>";
										itemImageHtml += "		<td>"+videoUrlText+"</td>";
										itemImageHtml += "	</tr>";
										itemImageHtml += "</table>";

										$("#itemImageContent").html(itemImageHtml);
									}
									else{
										$("#itemImageContent").html("");
									}
								}
						});

						$("#videoUrl").html(data.row.videoUrl);																						//상품 상세 이미지 > 동영상 정보
					}

					// 승인정보 관련 화면 제어(goodsApprovalComm.js) S
					fnSetItemApprData(data.row); // 품목승인정보 설정
					// 승인정보 관련 화면 제어(goodsApprovalComm.js) E
				}
		});
	} //fnMasterItemAll 끝

	//가격 정보 > 마스터 품목 가격정보
	function fnItemPriceList(ilItemCode){
		fnAjax({
			url : "/admin/goods/regist/itemPriceList",
			params : {
				ilItemCode : ilItemCode
			},
			isAction : 'select',
			success : function(data, status, xhr) {
				if(data != null){
					viewModel.ilGoodsDetail.set("itemPriceList", data.itemPriceList);										//가격 정보 > 마스터 품목 가격정보
				}
			}
		});
	}

	//배송/발주 정보 > 배송유형 > 출고처에 따른 배송유형별 리스트
	function fnItemWarehouseList(goodsShippingTemplateList){

		let storeYn = "Y";
		if(supplierCode == "FD" || supplierCode == "DM") {	//공급처가 풀무원 녹즙(FDD / PDM)이면 매장/가맹점 출고처는 나오지 말아야함(HGRM-5964)
			storeYn = "N";
		}

		fnAjax({
			url : "/admin/goods/regist/itemWarehouseList",
			params : {
				ilItemCode : ilItemCode
			,	urWarehouseId : urWarehouseId
			,	storeYn : storeYn
			},
			isAction : 'select',
			success : function(data, status, xhr) {
				fnWarehouseShippingTemplateList(goodsShippingTemplateList, data);
			}
		});

	}

	// Initialize Button
	function fnInitButton() {
		$('#fnGoodsUpdateHistorySearch, #fnMasterGoodsSearch, #fnOrderDetailInformationPopup, #fnItemDetailImageOrderChange, #fnItemDetailImageChangeComplete, #fnCtgrySelect, #fnMallInMallCtgrySelect, #fnNoticeBelow1File, #fnNoticeBelow2File').kendoButton();
	};

	// Kendo Component 초기화
	function fnInitGoodsShopOnly() {

	    // PJH Start
		//[HGRM-2379][S]상품명에 특수문자 입력불가
		//fnInputValidationLimitSpecialCharacter('goodsName');  // 상품명 : 특수문자 입력 제한
		fnInputValidationByRegexp("goodsName", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);
		//[HGRM-2379][E]상품명에 특수문자 입력불가
		//fnInputValidationLimitSpecialCharacter('goodsDesc');  // 상품설명 : 특수문자 입력 제한
		fnInputValidationByRegexp("goodsDesc", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);
        fnPromotionNameValidationCheck(); // 프로모션 상품명 Validation 체크 Listener 등록
        fnSearchKeywordValidationCheck(); // 검색 키워드 Validation 체크 Listener 등록

        fnToggleArea($('#basicInfoOfMasterItem'));  // 기본 정보(마스터 품목) => 최초 닫힘 상태로 처리
        fnToggleArea($('#detailInfo'));  // 상세 정보 => 최초 닫힘 상태로 처리
        // PJH End

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
				"CODE" : "Y",
				"NAME" : "노출함"
			}, {
				"CODE" : "N",
				"NAME" : "노출안함"
			} ],
			tagId : 'packageUnitDisplayYn'
		});

		//기본 정보 > 상품명 > 포장용량 구성정보 > 노출 여부 > 속성 추가
		$("input[name=packageUnitDisplayYn]").each(function() {
			$(this).attr("data-bind", "checked: ilGoodsDetail.packageUnitDisplayYn");
		});

		/* HGRM-1646 초기화 및 시간달력포맷 공통화 수정
		//기본 정보 > 프로모션 상품명 시작날짜
		fnkendoDateTimePicker({
			id : 'promotionNameStartDate',
			format : 'yyyy-MM-dd HH:mm'
		});

		//기본정보 > 프로모션 상품명 종료날짜
		fnkendoDateTimePicker({
			id : 'promotionNameEndDate',
			format : 'yyyy-MM-dd HH:mm'
		});
		*/
		//기본 정보 > 프로모션 상품명 시작 년도
		fnKendoDatePicker({
			id : 'promotionNameStartYear',
			format : 'yyyy-MM-dd',
//			defVal : todayDate,
//			max: lastDate,
			change: function(e){

				viewModel.fnDateChange(e, "promotionNameStartYear");
				// fnStartCalChange('promotionNameStartYear', 'promotionNameEndYear');
			}
		});

		$("#promotionNameStartYear").data("kendoDatePicker").unbind("blur");

		//기본정보 > 프로모션 상품명 종료 년도
		fnKendoDatePicker({
			id : 'promotionNameEndYear',
			format : 'yyyy-MM-dd',
//			min: todayDate,
//			max: lastDate,
			change: function(e){


				viewModel.fnDateChange(e, "promotionNameEndYear");
				// fnEndCalChange('promotionNameStartYear', 'promotionNameEndYear');
			}
		});

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

		/* HGRM-1646 초기화 및 시간달력포맷 공통화 수정
		//판매/전시 > 판매기간 시작날짜
		fnkendoDateTimePicker({
			id : 'saleStartDate',
			format : 'yyyy-MM-dd HH:mm'
		});

		//판매/전시 > 판매기간 종료날짜
		fnkendoDateTimePicker({
			id : 'saleEndDate',
			format : 'yyyy-MM-dd HH:mm'
		});
		*/
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

		//가격정보 > 단위별 용량정보
		fnTagMkRadio({
			id : 'autoDisplaySizeYn',
			data : [ {
				"CODE" : "Y",
				"NAME" : "자동표기"
			}, {
				"CODE" : "N",
				"NAME" : "자동 표기안함"
			} ],
			tagId : 'autoDisplaySizeYn'
		});

		//가격정보 > 단위별 용량정보 > 자동표기 > 속성 추가
		$("input[name=autoDisplaySizeYn]").each(function() {
			$(this).attr("data-bind", "checked: ilGoodsDetail.autoDisplaySizeYn, events: {change: onAutoDisplaySizeYnChange}");
		});

		//판매 정보 > 판매유형
		fnTagMkRadio({
			id : 'saleType',
			data : [ {
				"CODE" : "SALE_TYPE.SHOP",
				"NAME" : "매장판매"
			} ],
			tagId : 'saleType',
		});
		//판매 정보 > 판매유형 > 속성 추가
		$("input[name=saleType]").each(function() {
			$(this).attr("data-bind", "checked: ilGoodsDetail.saleType, events: {click:onSaleTypeClick, click: onSaleTypeChange}"); // onSaleTypeClick : 예약판매 클릭 시 (임시)
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
			buyCount.push({"code":i	,"name":i+"개"})
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

						//이미지가 저장되면 노출기간설정을 필수값으로 변경
						/*HGRM-1646 초기화 및 시간달력포맷 공통화 수정
						$("#noticeBelow1StartDate").attr("required","required");
						$("#noticeBelow1EndDate").attr("required","required");
						*/
						$(".noticeBelow1StartDate").attr("required","required");
						$(".noticeBelow1EndDate").attr("required","required");


						// PJH Start
						viewModel.set('showNoticeBelow1Date', true); // // 상세 상단공지 노출기간 Visible 처리
						// PJH End

						//HGRM-1646 초기화 및 시간달력포맷 공통화 수정
						// fnStartCalChange('noticeBelow1StartYear', 'noticeBelow1EndYear');
						// fnEndCalChange('noticeBelow1StartYear', 'noticeBelow1EndYear');

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

		/* HGRM-1646 초기화 및 시간달력포맷 공통화 수정
		//상품공지 > 상세 상단공지 노출기간 시작날짜
		fnkendoDateTimePicker({
			id : 'noticeBelow1StartDate',
			format : 'yyyy-MM-dd HH:mm'
		});

		//상품공지 > 상세 상단공지 노출기간 종료날짜
		fnkendoDateTimePicker({
			id : 'noticeBelow1EndDate',
			format : 'yyyy-MM-dd HH:mm'
		});

		//상품공지 > 상세 하단공지 노출기간 시작날짜
		fnkendoDateTimePicker({
			id : 'noticeBelow2StartDate',
			format : 'yyyy-MM-dd HH:mm'
		});

		//상품공지 > 상세 하단공지 노출기간 종료날짜
		fnkendoDateTimePicker({
			id : 'noticeBelow2EndDate',
			format : 'yyyy-MM-dd HH:mm'
		});
		*/

		//상품공지 > 상세 상단공지 노출기간 시작 년도
		fnKendoDatePicker({
			id : 'noticeBelow1StartYear',
			format : 'yyyy-MM-dd',
//			defVal : todayDate,
			//max: viewModel.ilGoodsDetail.get("noticeBelow1EndYear"),
			change: function(e){
				// fnStartCalChange('noticeBelow1StartYear', 'noticeBelow1EndYear');
				viewModel.fnDateChange(e, "noticeBelow1StartYear");
			}
		});

		//console.log("1year:",viewModel.ilGoodsDetail.get("noticeBelow1StartYear"))

		//상품공지 > 상세 상단공지 노출기간 종료 년도
		fnKendoDatePicker({
			id : 'noticeBelow1EndYear',
			format : 'yyyy-MM-dd',
		//	min: viewModel.ilGoodsDetail.get("noticeBelow1StartYear"),
//			max: lastDate,
			change: function(e){
				// fnEndCalChange('noticeBelow1StartYear', 'noticeBelow1EndYear');
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
				// fnStartCalChange('noticeBelow2StartYear', 'noticeBelow2EndYear');
				viewModel.fnDateChange(e, "noticeBelow2StartYear");
			}
		});


		//console.log("2year:",viewModel.ilGoodsDetail.get("noticeBelow2StartYear"))

		//상품공지 > 상세 하단공지 노출기간 종료 년도
		fnKendoDatePicker({
			id : 'noticeBelow2EndYear',
			format : 'yyyy-MM-dd',
		//	min: viewModel.ilGoodsDetail.get("noticeBelow2StartYear"),
//			max: lastDate,
			change: function(e){
				// fnEndCalChange('noticeBelow2StartYear', 'noticeBelow2EndYear');
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
						/*
						$("#noticeBelow2StartDate").attr("required","required");
						$("#noticeBelow2EndDate").attr("required","required");
						 */
						$(".noticeBelow2StartDate").attr("required","required");
						$(".noticeBelow2EndDate").attr("required","required");

                        // PJH Start
                        viewModel.set('showNoticeBelow2Date', true); // // 상세 하단공지 노출기간 Visible 처리
                        // PJH End

                        //HGRM-1646 초기화 및 시간달력포맷 공통화 수정
					    // fnStartCalChange('noticeBelow2StartYear', 'noticeBelow2EndYear');
						// fnEndCalChange('noticeBelow2StartYear', 'noticeBelow2EndYear');

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
	};

	//가격 정보 > 판매 가격정보 > 변경이력 보기
	function fnGoodsPricePopup(priceKind) {
		if(ilGoodsId != ""){
			let params = {};
			params.ilGoodsId = ilGoodsId;
			params.priceKind = priceKind;

			var GoodsPricePopupName = null;
			if(priceKind == "price"){
				GoodsPricePopupName = "판매정보 상세내역";
			}
			else if (priceKind == "employeePrice"){
				GoodsPricePopupName = "임직원 할인 가격정보 내역";
			}

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

	//가격정보 > 행사/할인 내역 > 상세내역 팝업 호출
	function fnGoodsDiscountDetailPopup(discountTypeCode, discountTypeCodeName){
		if(ilGoodsId != ""){
			let params = {};
			params.ilGoodsId = ilGoodsId;

			/* HGRM-2325 - dgyoun : 할인유형코드 파라미터 추가 */
			if (discountTypeCode != undefined && discountTypeCode != 'undefined') {
			  params.discountType = discountTypeCode;
			}
			else {
			  params.discountType = '';
			}

			console.log("discountTypeCode : ", discountTypeCode);

			var GoodsPricePopupName = "";
			if(discountTypeCode != "" && discountTypeCode != undefined){
				params.discountTypeCode = discountTypeCode;
				GoodsPricePopupName = discountTypeCodeName + " 정보 내역";
			}
			else{
				params.discountTypeCode = "";
			}

			if(discountTypeCode == "GOODS_DISCOUNT_TP.EMPLOYEE") {
				GoodsPricePopupName = "임직원 개별할인 정보 내역";
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

	//가격정보 > 마스터 품목 가격정보 > 상세내역 팝업 호출
	function fnItemPricePopup() {
		if(ilItemCode != null){
			let params = {};

			params.ilItemCd = ilItemCode;

			fnKendoPopup({
				id			: "itemPricePopup",
				title		: "품목 기본 판매 정보 내역",  // 해당되는 Title 명 작성
				width		: "1700px",
				height		: "800px",
				scrollable	: "yes",
				src			: "#/itemPricePopup",
				param		: params,
				success		: function( id, data ){
				}
			});
		}
	}

	// 혜택/구매정보 > 증정 행사 Grid > 추후 kendo template로 대체 처리할 예정
	function fnExhibitGiftGrid() {
		exhibitGiftGridDs = fnGetEditDataSource({
			model_id : 'ilGoodsId',
		});

		//Columns 구성 내역
		var presentationEventColumns = new Array;

		exhibitGiftGridOpt = {
			dataSource : exhibitGiftGridDs,
			columns : [
				{ field : "giftTypeName"	, title : "증정조건"	, width : "10%"	, attributes : {style : "text-align:center"} }
			,	{ field : "title"			, title : "증정행사 명"	, width : "50%"	, attributes : {style : "text-align:left"} }
			,	{ field : "startDate"		, title : "행사기간"	, width : "30%"	, attributes : {style : "text-align:center"}
						,template : function(dataItem){
							let dateTime = "";
							if(dataItem.alwaysYn == "Y") {
								dateTime = "상시진행";
							}
							else {
								dateTime = dataItem.startDate + " ~ " + dataItem.endDate;
							}
							return dateTime;
						}
				}
			,	{ command : [ { name : "행사 상세보기",
						click : function(e) {
							e.preventDefault();
							let row = $(e.target).closest("tr");
							let rowData = this.dataItem(row);

							viewModel.fnExhibitGiftDetail(rowData);
						}
					} ], title : "상세보기", width : "10%", attributes : { style : "text-align:center" }, locked: true
				}
			],
			noRecordMsg : '행사가 없습니다'
		};
		exhibitGiftGrid = $('#exhibitGiftGrid').initializeKendoGrid(exhibitGiftGridOpt).cKendoGrid();
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
				data = data.data
				noticeBelow2ImageUploadResultList = data['addFile'];
			}
		});

		return noticeBelow2ImageUploadResultList;
	}

	function fnSave() {

		/* 풀무원샵 상품코드 */
		let goodsCheckFlag		= true;
		let goodsCodeList		= new Array();
		let checkGoodsCodeList	= new Array();
		let goodsCodeCnt		= $("input[name='goodsCode']").length;

		fnPromotionGoodsNameValidationCheck();  // 프로모션명 + 상품명 30자 체크

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

		if( data.rtnValid ){
			fnKendoMessage({
				type : "confirm",
				message : "저장하시겠습니까?",
				ok : function() {

					//판매정보 > 판매유형(예약판매) > 예약 판매 옵션 설정 날짜 형식 변경 처리
					if(viewModel.goodsReservationOptionList.total() > 0){
						var goodsReservationOptionList = [];

						for(var i=0; i < viewModel.goodsReservationOptionList.total(); i++){
							var goodsReservationOptionInfo = viewModel.goodsReservationOptionList.data()[i];

							var reservationStartDateTime = kendo.toString(goodsReservationOptionInfo.reservationStartDate, "yyyy-MM-dd") + ' ' + goodsReservationOptionInfo.reservationStartHour + ":" + goodsReservationOptionInfo.reservationStartMinute;
							var reservationEndDateTime = kendo.toString(goodsReservationOptionInfo.reservationEndDate, "yyyy-MM-dd") + ' ' + goodsReservationOptionInfo.reservationEndHour + ":" + goodsReservationOptionInfo.reservationEndMinute;

							goodsReservationOptionList.push({
								saleSequance : i+1,																		//회차
								ilGoodsReserveOptionId : goodsReservationOptionInfo['ilGoodsReserveOptionId'],			//예약판매옵션설정 ID
								reservationStartDate : reservationStartDateTime,										//예약 주문 가능 기간 시작일
								reservationEndDate : reservationEndDateTime,											//예약 주문 가능 기간 종료일
								stockQuantity : goodsReservationOptionInfo['stockQuantity'],							//주문재고
								orderIfDate : kendo.toString(goodsReservationOptionInfo['orderIfDate'], "yyyy-MM-dd"),	//주문수집 I/F일
								releaseDate : kendo.toString(goodsReservationOptionInfo['releaseDate'], "yyyy-MM-dd"),	//출고예정일
								arriveDate : kendo.toString(goodsReservationOptionInfo['arriveDate'], "yyyy-MM-dd")		//도착예정일
							});
						}
					}

					//console.log("fnSave > goodsDiscountImmediateList", viewModel.ilGoodsDetail.get("goodsDiscountImmediateList"));

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

					//fixme: 로직 점검 필요. 랜더링 문제로 object 리턴하면서 정상적인 저장이 되지 않아 임시로 처리함. 2020.10.05
					var limitMaximumCount = viewModel.ilGoodsDetail.get("limitMaximumCnt.code") == undefined ?
						viewModel.ilGoodsDetail.get("limitMaximumCnt") : viewModel.ilGoodsDetail.get("limitMaximumCnt.code");

					let approvalChkVal = $('input:checkbox[name="approvalCheckbox"]').is(":checked");	//승인요청 처리 여부
					let goodsApprList = [];

					if(approvalChkVal) {
						goodsApprList = $("#apprGrid").data("kendoGrid").dataSource.data()
					}

					var addGoodsParam = {
							ilGoodsId								: ilGoodsId
						,	ilItemCode								: ilItemCode														//품목코드
						,	urWarehouseId							: urWarehouseId														//출고처 ID
						,	urSupplierId							: urSupplierId														//공급사 ID
						,	urBrandId								: urBrandId															//브랜드 ID
						,	goodsType								: viewModel.ilGoodsDetail.get("goodsType")							//상품 유형
						,	erpItemLinkYn							: erpItemLinkYn
						/* 기본 정보 시작 */
						,	goodsName								: viewModel.ilGoodsDetail.get("goodsName")														//상품명
						,	packageUnitDisplayYn					: viewModel.ilGoodsDetail.get("packageUnitDisplayYn")											//표장용량 구성정보 노출여부(Y:노출)
						,	mdRecommendYn							: viewModel.ilGoodsDetail.get("mdRecommendYn")													//MD추천 노출여부
						,	promotionName							: viewModel.ilGoodsDetail.get("promotionName")													//프로모션 상품명
						,	promotionNameStartDate					: kendo.toString(viewModel.fnGetDateFull("promotionNameStart"), "yyyy-MM-dd HH:mm")				// 프로모션 시작일
						,	promotionNameEndDate					: kendo.toString(viewModel.fnGetDateFull("promotionNameEnd"), "yyyy-MM-dd HH:mm")				// 프로모션 종료일
						,	goodsDesc								: viewModel.ilGoodsDetail.get("goodsDesc")														//상품설명
						,	searchKeyword							: viewModel.ilGoodsDetail.get("searchKeyword")													//키워드 입력
						,	displayCategoryList						: $("#goodsDisplayCategoryGrid").data("kendoGrid").dataSource.data()							//상품 전시 카테고리
						,	mallInMallCategoryList					: $("#mallInMallCategoryGrid").data("kendoGrid").dataSource.data()								//상품 몰인몰 카테고리
						/* 기본 정보 끝 */

						/* 판매/전시 시작 */
						,	purchaseTargetType						: viewModel.ilGoodsDetail.get("purchaseTargetType")								//구매허용
						,	goodsDisplayType						: viewModel.ilGoodsDetail.get("goodsDisplayType")								//판매허용범위 (PC/Mobile)

						,	displayYn								: viewModel.ilGoodsDetail.get("displayYn")										//전시 상태
						/* HGRM-1646 초기화 및 시간달력포맷 공통화 수정
						,	saleStartDate							: kendo.toString(viewModel.ilGoodsDetail.get("saleStartDate"), "yyyy-MM-dd")	//판매 기간 시작일
						,	saleEndDate								: kendo.toString(viewModel.ilGoodsDetail.get("saleEndDate"), "yyyy-MM-dd")		//판매 기간 종료일
						*/
						,	saleStartDate							: kendo.toString(viewModel.fnGetDateFull("saleStart"), "yyyy-MM-dd HH:mm")		//판매 기간 시작일
						,	saleEndDate								: kendo.toString(viewModel.fnGetDateFull("saleEnd"), "yyyy-MM-dd HH:mm")		//판매 기간 종료일
						,	saleStatus								: viewModel.ilGoodsDetail.get("saleStatus")										//판매 상태
						,	goodsOutmallSaleStatus					: viewModel.ilGoodsDetail.get("goodsOutmallSaleStatus")							//외부몰 판매 상태
						/* 판매/전시 끝 */

						/* 가격정보 시작*/
						,	autoDisplaySizeYn						: viewModel.ilGoodsDetail.get("autoDisplaySizeYn")								//단위별 용량정보
						,	goodsDiscountPriorityList				: viewModel.ilGoodsDetail.get("goodsDiscountPriorityList")						//행사/할인 내역 > 우선할인 내역
						,	goodsDiscountPriorityApproList			: viewModel.ilGoodsDetail.get("goodsDiscountPriorityApproList")					//행사/할인 내역 > 우선할인 내역 > 승인 관리자 정보
						,	goodsDiscountImmediateList				: viewModel.ilGoodsDetail.get("goodsDiscountImmediateList")						//행사/할인 내역 > 즉시할인 내역
						,	goodsDiscountImmediateApproList			: viewModel.ilGoodsDetail.get("goodsDiscountImmediateApproList")				//행사/할인 내역 > 즉시할인 내역 > 승인 관리자 정보
						,	sizeEtc									: viewModel.ilGoodsDetail.get("sizeEtc")										//단위별 용량정보 > 자동표기안함 시에 입력값
						,	taxYn									: viewModel.ilGoodsDetail.taxYn
						/* 가격정보 끝*/

						/* 임직원 할인 정보 시작 */
						,	goodsDiscountEmployeeList				: viewModel.ilGoodsDetail.get("goodsDiscountEmployeeList")					//임직원 개별할인 정보 내역
						,	goodsDiscountEmployeeApproList			: viewModel.ilGoodsDetail.get("goodsDiscountEmployeeApproList")				//임직원 개별할인 정보 내역 > 승인 관리자
						/* 임직원 할인 정보 끝 */

						/* 판매정보 시작*/
						,	saleType								: viewModel.ilGoodsDetail.get("saleType")										//판매유형
						,	saleShopYn								: "Y"																			//매장판매
						,	goodsReservationOptionList				: goodsReservationOptionList													//예약판매옵션 설정 리스트
						,	deleteGoodsReservationOptionList		: viewModel.get("deleteGoodsReservationOptionList")								//삭제할 예약판매옵션 설정 리스트
						/* 판매정보 끝*/

						/* 배송/발주 정보 시작 */
						,	itemWarehouseShippingTemplateList		: viewModel.ilGoodsDetail.get('itemWarehouseList')								//배송 정책 리스트
						/* 배송/발주 정보 끝 */

						/* 혜택/구매 정보 시작*/
						,	goodsAdditionalGoodsMappingList			: viewModel.ilGoodsDetail.get("goodsAdditionalGoodsMappingList")				//추가상품 리스트
						,	couponUseYn								: viewModel.ilGoodsDetail.get("couponUseYn")									//혜택 설정 > 쿠폰사용허용
						,	limitMinimumCnt							: viewModel.ilGoodsDetail.get("limitMinimumCnt")								//최소구매 수량기준
						,	limitMaximumType						: viewModel.ilGoodsDetail.get("limitMaximumType")								//최대 구매 기준
						,	limitMaximumDuration					: viewModel.ilGoodsDetail.get("limitMaximumDuration")							//최대 구매 기간
						,	limitMaximumCnt							: limitMaximumCount																//최대 구매 수량
						/* 혜택/구매 정보 끝*/

						/* 추천상품 등록 시작*/
						,	goodsRecommendList						: viewModel.ilGoodsDetail.get("goodsRecommendList")								//추천상품 리스트
						/* 추천상품 등록 끝*/

						/* 상품공지 시작*/
						,	noticeBelow1ImageUploadResultList		: viewModel.get("noticeBelow1ImageUploadResultList")								//상세 상단공지 이미지 Upload 정보
						,	noticeBelow1StartDate					: kendo.toString(viewModel.fnGetDateFull("noticeBelow1Start"),"yyyy-MM-dd HH:mm")	//상세 상단공지 시작일
						,	noticeBelow1EndDate						: kendo.toString(viewModel.fnGetDateFull("noticeBelow1End"),"yyyy-MM-dd HH:mm")		//상세 상단공지 종료일
						,	noticeBelow2ImageUploadResultList		: viewModel.get("noticeBelow2ImageUploadResultList")								//상세 하단공지 이미지 Upload 정보
						,	noticeBelow2StartDate					: kendo.toString(viewModel.fnGetDateFull("noticeBelow2Start"),"yyyy-MM-dd HH:mm")	//상세 하단공지 시작일
						,	noticeBelow2EndDate						: kendo.toString(viewModel.fnGetDateFull("noticeBelow2End"),"yyyy-MM-dd HH:mm")		//상세 하단공지 종료일
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


//					console.log("addGoodsParam", addGoodsParam);
//					return;

					var pageMode = viewModel.get("pageMode");
					var url = pageMode == "create" ? "/admin/goods/regist/addGoods" : "/admin/goods/regist/modifyGoods";
					var modeMessage = pageMode == "create" ? "생성" : "수정";
					var paramDataSort = pageMode == "create" ? "" : "?paramDataSort=MODIFY_DATE";

					fnAjax({
						url : url,
						params : addGoodsParam,
						contentType : 'application/json',
						isAction : 'insert',
						success : function(data, result) {
							fnKendoMessage({
								message : '상품명 ' + data["goodsName"] + ' (이)가 '+modeMessage+'되었습니다.',
								ok : function() {
									//var successUrl = "/layout.html#/goods"+paramDataSort;
									//var successUrl = "/layout.html#/goods?paramIlGoodsId="+data['ilGoodsId'];
									//location.href = successUrl;
									if(pageMode == "create") {
										var successUrl = "/layout.html#/goodsShopOnly?ilGoodsId="+data['ilGoodsId'];
										location.href = successUrl;
									}
									else{
										window.location.reload(true);
									}
								}
							});
						},
						error : function(xhr, status, strError, data, result) {
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
												var successUrl = "/layout.html#/goodsShopOnly?ilGoodsId="+data['ilGoodsId'];
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

        $targetObject.keyup(validationCheckFunction);    // keyup 이벤트에 listener 적용
        $targetObject.focusout(validationCheckFunction); // 포커스아웃 이벤트의 경우에도 동일하게 적용

    }

	/**
	 * 프로모션 + 상품명 30자 제한 Validation Check
	 */
	function fnPromotionGoodsNameValidationCheck() {

		var $promotionName = $("#promotionName");   // 프로모션명
		var $goodsName = $("#goodsName");           // 상품명

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

    /**
     * 검색 키워드 입력 Validation Check
     */
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

        $targetObject.keyup(validationCheckFunction);    // keyup 이벤트에 listener 적용
        $targetObject.focusout(validationCheckFunction); // 포커스아웃 이벤트의 경우에도 동일하게 적용

        $targetObject.keypress(function(event){ // keypress 이벤트에 특수문자 입력방지 로직 적용
            return regexpFilter.test(event.key) ? false : true;
        });

    }

	/**
     * 영역 보이기 / 숨기기
     * @param btnDom - 버튼 dom 객체
     * @param startNo - table 에서 숨기기 시작하는 tr 번호 (default = 2 로 두번째 tr 부터 숨기기 시작한다)
     * @param excpetionArea - 적용 예외 영역명
     */
    function fnToggleArea(btnDom, startNo = 2, excpetionArea) {

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
	// PJH End

	// ------------------------------------------------------------------------
	// 상품상세 미리보기 링크
	// ------------------------------------------------------------------------
	function fnBtnGoodsPreview() {
		var mallUrl = CUR_SERVER_URL + "/shop/goodsView?goods=" + ilGoodsId+"&preview=Y";
		window.open(mallUrl);
	}
	// ------------------------------- Html 버튼 바인딩 Start
	/** Common */
	$scope.fnSave = function() {
		fnSave();
	};

	$scope.fnErpGoodsSearchPopup = function() { // ERP 상품 검색 팝업
		fnErpGoodsSearchPopup();
	};

	$scope.fnMasterGoodsSearchPopup = function() { // 마스터 상품 검색 팝업
		fnMasterGoodsSearchPopup();
	};

	$scope.fnCtgrySelect = function( ){				//전시 카테고리 분류 선택
		fnCtgrySelect();
	};

	$scope.fnMallInMallCtgrySelect = function( ){	//몰인몰 카테고리 분류 선택
		fnMallInMallCtgrySelect();
	};

	$scope.fnNoticeBelow1File = function( ){		//상품공지 > 상세 상단공지 첨부파일
		fnNoticeBelow1File();
	};

	$scope.fnNoticeBelow2File = function( ){		//상품공지 > 상세 상단공지 첨부파일
		fnNoticeBelow2File();
	};

	$scope.fnGoodsPricePopup = function(priceKind){		//가격 정보 > 판매 가격정보 > 변경이력 보기
		fnGoodsPricePopup(priceKind);
	};

	$scope.fnGoodsDiscountDetailPopup = function(discountTypeCode, discountTypeCodeName){		//가격 정보 > 행사/할인 내역 > 상세내역
		fnGoodsDiscountDetailPopup(discountTypeCode, discountTypeCodeName);
	};

	$scope.fnGoodsDiscountPopup = function(discountTypeCode, discountTypeCodeName){		//가격 정보 > 행사/할인 내역 > 할인설정
		// 품목 가격 승인, 상품 할인 승인 중복 체크
		fnAjax({
			url     : "/admin/approval/auth/checkDuplicatePriceApproval",
			method : "GET",
			async : false,
			params  : {"taskCode" : "APPR_KIND_TP.GOODS_DISCOUNT", "itemCode" : ilItemCode},
			success : function(data){
				fnGoodsDiscountPopup(discountTypeCode, discountTypeCodeName);
			},
			isAction : "select"
		});
	};

	$scope.fnItemPricePopup = function( ){		//가격정보 > 마스터 품목 가격정보 > 상세내역 팝업 호출
		fnItemPricePopup();
	};

	$scope.fnPoTypeDetailInformationPopup = function(){									//배송/발주 정보 > 발주 유형 > 상세보기
		fnPoTypeDetailInformationPopup();
	};

	$scope.fnOrderIfDateClick = function(rowIndex){
		fnOrderIfDateClick(rowIndex);
	}

	// PJH Start
	/** 영역 보이기 / 숨기기 */
    $scope.fnToggleArea = function( btnDom, startNo, excpetionArea ){
        fnToggleArea(btnDom, startNo, excpetionArea);
    };
	// PJH End
    $scope.fnStoreStockInfoVisible = function (visible) {
    	fnStoreStockInfoVisible(visible);
    }

    $scope.fnStoreScheduleModal = function(urStoreId, storeName) {
    	fnStoreScheduleModal(urStoreId, storeName);
    }

    $scope.fnItemStorePriceLogModal = function(ilItemCd, urStoreId, storeName){
    	fnItemStorePriceLogModal(ilItemCd, urStoreId, storeName);
	}
	// 상품상세 미리보기
	$scope.fnBtnGoodsPreview = function () {
		fnBtnGoodsPreview();
	};
    // ------------------------------- Html 버튼 바인딩 End
}); // document ready - END
