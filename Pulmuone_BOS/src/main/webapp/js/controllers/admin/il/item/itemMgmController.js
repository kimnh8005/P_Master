/*******************************************************************************************************************************************************************************************************
 * --------------- description : 품목/재고 관리 - 마스터 품목 등록 @ ----------------------------
 ******************************************************************************************************************************************************************************************************/
'use strict';

var addAvailableNutritionCodeMap; // 조회된 등록가능 영양정보 코드 List 를 { "영양코드" : "영양코드명" , ... } 형식으로 변환한 객체
var addAvailableNutritionUnitMap; // 조회된 등록가능 영양정보 코드 List 를 { "영양코드" : "영양코드 단위" , ... } 형식으로 변환한 객체
var addAvailableNutritionPercentMap; // 영양소 기준치 % 사용하는 영양코드 Map : ( "영양코드" : "영양코드명" 코드 )

var specDescriptionPrefix = '*  '; // 상품정보 제공고시 부가 설명 Prefix
var publicUrlPath; // public 저장소를 경유하지 않고 API 서버로 저장소 접근시 url 경로 ( CORS 회피용 )
var workindEditorId; // 상품 상세 기본 정보와 주요 정보 Editor 중 이미지 첨부를 클릭한 에디터 Id

var itemImageUploadMaxLimit = 1048576; // 상품 이미지 첨부 가능 최대 용량 ( 단위 : byte )
var allowedImageExtensionList = ['.jpg', '.jpeg', '.gif']; // 업로드 가능한 이미지 확장자 목록
var priceEndApplyDate = '2999-12-31'; // 가격 적용 종료일

var foodLegalTypeCode = "PFF";       // 풀무원 식품 법인코드
var orgaLegalTypeCode = "OGH";       // 올가 법인코드
var foodmerceLegalTypeCode = "FDM";  // 푸드머스 법인코드
var lohasLegalTypeCode = "PGS";      // 건강생활 법인코드

var noneSupplierCode = ""; // BOS 공급업체 "미지정" 코드값
var fddSupplierCode = "4"; // BOS 공급업체 중 FDD ( "풀무원 녹즙1" ) 의 코드값
var pdmSupplierCode = "5"; // BOS 공급업체 중 PDM ( "풀무원 녹즙2" ) 의 코드값

var sizeUnitDirectInputCode = "UNIT_TYPE.DIRECT_INPUT"; // 용량(중량) 단위 Drowdown 의 "직접입력" 코드값
var packageUnitDirectInputCode = "UNIT_TYPE.DIRECT_INPUT"; // 포장구성 단위 Drowdown 의 "직접입력" 코드값

var previousMasterItem;  // 이전 마스터 품목 유형 선택값

var viewModel;  // Kendo viewModel

var apprAdminGridDs, apprAdminGridOpt, apprAdminGrid;

var apprKindTp;
var foodTemp;
var orgaTemp;

$(document).ready(function() {

	let loadDate = new Date();
	let loadDateTime = loadDate.oFormat("yyyy-MM-dd HH:mm:ss");

	/*
     * Kendo UI MVVM Pattern 적용
     */
    var viewModelOriginal = {

            /* 기본 정보 */
            isAllDisabled : true, // 최초 화면 조회시 화면 전체 항목 비활성화 여부
            isNewInsert : true, // 새로운 품목 등록 flag : isNotNewInsert 과 반대값
            isNotNewInsert : false, // 기존 품목 수정 flag : 마스터 품목 리스트에서 품목 조회후 "품목 수정" 버튼 클릭 / 페이지 이동시만 true

            // 품목 유형 / 품목 코드 / ERP 품목 연동 여부
            masterItemType : 'MASTER_ITEM_TYPE.COMMON', // 마스터 품목 유형 : '공통' 기본 선택
            erpItemCode : '', // ERP 품목코드
            bosItemCode : '', // BOS 품목코드
            isErpItemLink : true, // ERP 품목 연동여부 flag : 연동시 true, 미연동시 false
            isNotErpItemLink : false, // ERP 품목 미연동여부 flag: 연동시 false, 미연동시 true
            onMasterItemTypeChange : function(e) { // 마스터 품목 유형 change Event

                if( e != null ) { // event 객체가 전달된 경우 : 사용자에 의한 마스터 품목 유형 변경 => 전체 초기화 함수 호출

                    fnKendoMessage({
                        type : "confirm",
                        message : "품목유형 변경으로 인해 정보가 초기화됩니다.<br>계속하시겠습니까?",
                        ok : function() {
                            // 마스터 품목 유형 변경시 화면 전체 초기화 => fnClear 함수 내에서 다시 마스터 품목 유형 change Event 수동 호출
                            fnClear();
                        },
                        cancel : function() {
                            viewModel.set('masterItemType', previousMasterItem); // 이전 마스터 품목 유형 값으로 원복
                            viewModel.onMasterItemTypeChange(null); // 수동으로 마스터 품목 유형 change Event 호출

                            // 데이터 마이그레이션 관련 로직 Start : 추후 삭제 예정
                            fnShowMigrationDataButton();
                            // 데이터 마이그레이션 관련 로직 End

                        }
                    });

                } else { // 마스터 품목 유형 change Event 수동 호출

                    $(".incorporeal-star").addClass("req-star-th")
                    switch (this.masterItemType) {

                    case 'MASTER_ITEM_TYPE.COMMON': // 공통 품목 선택시 : ERP 품목 연동여부 '예/아니오' 선택 가능

                        this.set('isErpItemLinkYnDisabled', false); // ERP 품목 연동여부 활성화, ERP 연동 여부는 별도 변경하지 않음
                        this.set('showRentalPriceVisible', false);
                        this.set('showPriceApproval', true);		// 가격승인대상여부 활성화 여부
                        this.set('showAddWarehouse', true);	// 출고처 등록 UI 활성화 여부
                        break;

                    case 'MASTER_ITEM_TYPE.SHOP_ONLY': // 매장 전용 선택시 : ERP 품목 연동여부 '예' 만 가능

                        this.set('erpItemLinkYn', 'Y');
                        this.set('isErpItemLinkYnDisabled', true); // ERP 품목 연동여부 비활성화
                        this.set('showRentalPriceVisible', false);
                        this.set('showPriceApproval', true);		// 가격승인대상여부 활성화 여부
                        this.set('showAddWarehouse', false);	// 출고처 등록 UI 활성화 여부
                        break;

                    case 'MASTER_ITEM_TYPE.INCORPOREITY': // 무형, 렌탈 품목 선택시 : ERP 품목 연동여부 '아니오' 만 가능
                    	this.set('erpItemLinkYn', 'N');
                    	this.set('isErpItemLinkYnDisabled', true); // ERP 품목 연동여부 비활성화
                    	this.set('showRentalPriceVisible', false);
                    	this.set('showPriceApproval', true);		// 가격승인대상여부 활성화 여부
                    	this.set('showAddWarehouse', true);	// 출고처 등록 UI 활성화 여부
                        $(".incorporeal-star").removeClass("req-star-th")

                    	break;
                    case 'MASTER_ITEM_TYPE.RENTAL':

                        this.set('erpItemLinkYn', 'N');
                        this.set('isErpItemLinkYnDisabled', true); // ERP 품목 연동여부 비활성화
                        this.set('showPriceApproval', false);		// 가격승인대상여부 활성화 여부
                        this.set('showItemPrice', false);			// 온라인 통합가격정보 비활성화
                        this.set('isRetalEditDisabled', false);
                        this.set('showRentalPriceVisible', true);
                        this.set('showAddWarehouse', true);	// 출고처 등록 UI 활성화 여부
                        break;

                    }  // switch end

                    previousMasterItem = viewModel.get('masterItemType'); // 마스터 품목 유형 select 에서 변경 이전의 선택 값 별도 저장

                    this.onErpItemLinkYnChange(null); // 수동으로 ERP 품목 연동여부 change event 호출

                    // 데이터 마이그레이션 관련 로직 Start : 추후 삭제 예정
                    fnShowMigrationDataButton();
                    // 데이터 마이그레이션 관련 로직 End

                }

            },
            erpItemLinkYn : 'Y', // ERP 품목 연동여부 : 기본 선택값 '연동'
            isErpItemLinkYnDisabled : false, // ERP 품목 연동여부 활성화 / 비활성화 여부
            isRetalEditDisabled : true, // 렌탈 품목 입력 활성화 여부
            onErpItemLinkYnChange : function(e) { // ERP 품목 연동여부 Change 이벤트 : event 객체가 null 인 경우 스크립트에 의한 수동 호출

                if( e != null ) {

                    // e.sender._cascadedValue 는 dropdown 의 현재 선택값 : 'Y' or 'N'
                    var previousErpItemLinkYn = ( e.sender._cascadedValue == 'Y' ? 'N' : 'Y' ); // 이전 ERP 품목 연동 여부

                    fnKendoMessage({
                        type : "confirm",
                        message : "입력하신 품목정보가 초기화 됩니다.<br>계속하시겠습니까?",
                        ok : function() {
                            fnClear();   // ERP 품목 연동여부 변경시 화면 전체 초기화
                        },
                        cancel : function() {
                            viewModel.set('erpItemLinkYn', previousErpItemLinkYn); // 이전 ERP 품목 연동 여부 값으로 원복
                            viewModel.onErpItemLinkYnChange(null); // 수동으로 ERP 품목 연동여부 change event 호출

                            // 데이터 마이그레이션 관련 로직 Start : 추후 삭제 예정
                            fnShowMigrationDataButton();
                            // 데이터 마이그레이션 관련 로직 End

                        }
                    });

                }

                switch (this.erpItemLinkYn) { // erpItemLinkYn 의 'Y', 'N' 값 => isErpItemLink, isNotErpItemLink 의 true, false 로 변환

                case 'Y': // ERP 연동 품목 선택시

                    this.set('isErpItemLink', true);
                    this.set('isNotErpItemLink', false);

                    // $('#itemGroupTh').addClass("must"); // 상품군 필수입력 class 지정

                    this.set('bosSupplier', ''); // 공급업체 '미지정' 으로 세팅
                    this.set('isSupplierDisabled', true); // 공급업체 비활성화
                    this.set('showPriceApproval', true);		// 가격승인대상여부 활성화 여부
                    break;

                case 'N': // ERP 미연동 품목 선택시

                	this.set("approvalTarget", "승인");
                    this.set('isErpItemLink', false);
                    this.set('isNotErpItemLink', true);
                    if(viewModel.get("masterItemType") == "MASTER_ITEM_TYPE.RENTAL") {
                    	// 렌탈 상품이면 온라인 통합가격정도 비활성화
                    	this.set('showItemPrice', false);
                    	this.set('isRetalEditDisabled', false);
                    	this.set('showRentalPriceVisible', true);
                    	this.set('showPriceApproval', false);		// 가격승인대상여부 활성화 여부
                    }else {
                    	// 렌탈 상품아니면 온라인 통합가격정도 활성화
                    	this.set('showItemPrice', true);
                    	this.set('isRetalEditDisabled', true);
                    	this.set('showRentalPriceVisible', false);
                    	this.set('showPriceApproval', true);		// 가격승인대상여부 활성화 여부
                    }

                    this.set('showItemPriceAddBtn', true);
                    this.set('isBosBrandDisabled', false);

                    // $('#itemGroupTh').removeClass("must"); // 상품군 필수입력 class 지정 해제

                    this.set('isSupplierDisabled', false); // 공급업체 비활성화 해제
                    this.set('isAllDisabled', false); // 화면 전체 잠금 해제

                    // ERP 미연동 선택시 가격정보 수동 입력 가능하도록 가격정보 행 Visible
//                    this.set('bosPriceApplyStartDate' , fnGetToday()); // BOS 가격 적용 시작일 : 현재 일자 세팅
                    this.set('bosStandardPrice' , '');
                    this.set('bosRecommendedPrice' , '');
                    this.set('priceApplyStartDate' , ''); // BOS 가격 적용 시작일 : 현재 일자 세팅
                    this.set('standardPrice' , '');
                    this.set('recommendedPrice' , '');

                    this.set('showItemPriceRow', true); // 가격정보 행 Visible
                    this.set('noItemPriceInfo', false); // 가격정보 없을때 보여주는 값

                    fnGetAddAvailableNutritionCodeList("", true); // 등록 가능한 모든 영양정보 분류코드 리스트 조회

                    this.set('bosSupplier', ''); // 공급업체 '미지정' 으로 세팅 : 기존 선택값 초기화
                    break;

                }

                // 데이터 마이그레이션 관련 로직 Start : 추후 삭제 예정
                fnShowMigrationDataButton();
                // 데이터 마이그레이션 관련 로직 End

            },
            itemNameInCopy : '', // 마스터품목정보 복사 출력명
            erpItemName : '', // ERP 품목명
            erpItemBarCode : '', // ERP 품목 바코드
            bosItemBarCode : '', // BOS 품목 바코드
            o2oExposureType : '', // 매장 품목유형 ( ERP 연동 품목 중 올가 ERP 전용 )
            o2oExposureTypeName : '', // 매장 품목유형명 ( 화면에 출력 )
            productType : '', // 상품 판매유형 ( ERP 연동 품목 중 건강생활 ERP 전용 )
            hasO2oExposureType : false, // 매장 품목유형 데이터 존재 여부 : 올가 ERP 전용
            hasProductType : false, // 상품 판매유형 : 건강생활 ERP 전용
            itemName : '', // 마스터 품목명

            // ERP 카테고리
            erpCategoryLevel1Id : '', // ERP 대카테고리
            erpCategoryLevel2Id : '', // ERP 중카테고리
            erpCategoryLevel3Id : '', // ERP 소카테고리
            erpCategoryLevel4Id : '', // ERP 세부카테고리

            // 표준 카테고리
            bosCategoryStandardFirstId : '', // 표준 카테고리 대분류 ID
            bosCategoryStandardSecondId : '', // 표준 카테고리 중분류 ID
            bosCategoryStandardThirdId : '', // 표준 카테고리 소분류 ID
            bosCategoryStandardFourthId : '', // 표준 카테고리 세분류 ID

            erpItemWidth : '',				// ERP 품목 가로
            erpItemDepth : '',				// ERP 품목 세로
            erpItemHeight : '',				// ERP 품목 높이

            /* 상세 정보 */
            legalType : '', // ERP 법인코드
            organizationType : '', // ERP 온라인 통합몰 구분값
            erpSupplierName : '', // ERP 에서의 공급업체명
            bosSupplier : '', // BOS 공급업체
            isSupplierDisabled : true, // 공급업체 비활성화 여부
            onBosSupplierChange : function(e) { // 공급업체 Dropdown Change 이벤트

                var previousBosSupplier = this.bosSupplier; // 공급업체 select 에서 변경 이전값
                var selecetdBosSupplier = e.dataItem.urSupplierId; // 공급업체 select 에서 변경 이후값

                if( previousBosSupplier == noneSupplierCode ) {  // 공급업체 "미지정" 에서 다른 공급업체로 변경시 : 바로 출고처, 브랜드 목록 조회

                    /* 출고처 조회 : 변경한 공급업체 ID 값으로 조회 */
                    fnGetItemWarehouseList(selecetdBosSupplier);

                    /* 변경한 공급업체 ID 로 브랜드 리스트 호출 */
                    fnKendoDropDownList({
                        id : 'bosBrand',
                        url : "/admin/ur/brand/searchBrandList",
                        params : {
                            searchUrSupplierId : selecetdBosSupplier,  // 변경한 공급업체 ID 값
                            searchUseYn : "Y"
                        },
                        textField : "brandName",
                        valueField : "urBrandId",
                        blank : '선택'
                    });

                    viewModel.set('isBosBrandDisabled', false); // BOS 브랜드 비활성화 해제

                    $("#warehouseGroup").data("kendoDropDownList").enable(true);  // 출고처 관련 항목 모두 활성화
                    $("#warehouse").data("kendoDropDownList").enable(true);
                    $('#fnAddWarehouse').attr("disabled", false);

                    // PDM 그룹코드 처리(공급업체가 PDM일 경우에 노출 및 활성화)
                    if( selecetdBosSupplier == pdmSupplierCode ) { // 공급업체가 PDM ( "풀무원 녹즙2" ) 인 경우
                        viewModel.set('isPdmGroupCodeVisible', true ); // PDM 그룹코드 Visible 처리
                        viewModel.set('isPdmGroupCodeDisabled', false ); // PDM 그룹코드 활성화
                    } else {
                        viewModel.set('isPdmGroupCodeVisible', false ); // PDM 그룹코드 Visible 해제
                        viewModel.set('isPdmGroupCodeDisabled', true ); // PDM 그룹코드 비활성화
                        viewModel.set('pdmGroupCode', '' ); // 기존 PDM 그룹코드 값 초기화
                    }

                    viewModel.setSpecFieldModifiedMessage(null); // 상품정보 제공고시 상세 메시지 출력 이벤트 수동 호출

                } else {

                    fnKendoMessage({
                        type : "confirm",
                        message : "공급업체 변경시 관련 정보가 초기화 됩니다.<br>변경하시겠습니까?",
                        ok : function() {

                            if( viewModel.get('bosSupplier') == noneSupplierCode ) { // 공급업체 "미지정" 으로 변경시

                                fnKendoDropDownList({ // 출고처 그룹 데이터 초기화
                                    id : 'warehouseGroup',
                                    data : {},
                                    textField : "NAME",
                                    valueField : "CODE",
                                    blank : '출고처 그룹'
                                })

                                fnKendoDropDownList({ // 출고처 데이터 초기화
                                    id : 'warehouse',
                                    data : {},
                                    textField : "warehouseName",
                                    valueField : "urSupplierWarehouseId",
                                    blank : '출고처 선택'
                                })

                                fnKendoDropDownList({
                                	 id : 'bosBrand',
                                     url : "/admin/ur/brand/searchBrandList",
                                     params : {
                                         searchUrSupplierId : '',  // 변경한 공급업체 ID 값
                                         searchUseYn : "Y"
                                     },
                                     textField : "brandName",
                                     valueField : "urBrandId",
                                     blank : '선택'

                                });

                                viewModel.set('bosBrand', '');
                                viewModel.set('isBosBrandDisabled', false); // BOS 브랜드 비활성화
                                viewModel.set('selectedWarehouseGroupCode', '');
                                viewModel.set('selectedUrSupplierWarehouseId', '');
                                viewModel.set('itemWarehouseList', []); // 기존 해당 품목코드의 출고처 목록 초기화
                                viewModel.set('noItemWarehouseInfo', true); // 출고처 없을때 노출

                                $("#warehouseGroup").data("kendoDropDownList").enable(false);  // 출고처 관련 항목 모두 비활성화
                                $("#warehouse").data("kendoDropDownList").enable(false);
                                $('#fnAddWarehouse').attr("disabled", true);

                                viewModel.setSpecFieldModifiedMessage(null); // 상품정보 제공고시 상세 메시지 출력 이벤트 수동 호출

                            } else {

                                /* 출고처 조회 : 변경한 공급업체 ID 값으로 조회 */
                                fnGetItemWarehouseList(selecetdBosSupplier);

                                /* 변경한 공급업체 ID 로 브랜드 리스트 호출 */
                                fnKendoDropDownList({
                                    id : 'bosBrand',
                                    url : "/admin/ur/brand/searchBrandList",
                                    params : {
                                        searchUrSupplierId : selecetdBosSupplier,  // 변경한 공급업체 ID 값
                                        searchUseYn : "Y"
                                    },
                                    textField : "brandName",
                                    valueField : "urBrandId",
                                    blank : '선택'
                                });

                                viewModel.set('isBosBrandDisabled', false); // BOS 브랜드 비활성화 해제
                                viewModel.set('bosBrand', ''); // 공급업체 수정시 기존 브랜드 선택값 초기화

                                $("#warehouseGroup").data("kendoDropDownList").enable(true);  // 출고처 관련 항목 모두 활성화
                                $("#warehouse").data("kendoDropDownList").enable(true);
                                $('#fnAddWarehouse').attr("disabled", false);

                                viewModel.setSpecFieldModifiedMessage(null); // 상품정보 제공고시 상세 메시지 출력 이벤트 수동 호출

                            }

                            // PDM 그룹코드 처리(공급업체가 PDM일 경우에 노출 및 활성화)
                            if( viewModel.get('bosSupplier') == pdmSupplierCode ) { // 공급업체가 PDM ( "풀무원 녹즙2" ) 인 경우
                                viewModel.set('isPdmGroupCodeVisible', true ); // PDM 그룹코드 Visible 처리
                                viewModel.set('isPdmGroupCodeDisabled', false ); // PDM 그룹코드 활성화
                            } else {
                                viewModel.set('isPdmGroupCodeVisible', false ); // PDM 그룹코드 Visible 해제
                                viewModel.set('isPdmGroupCodeDisabled', true ); // PDM 그룹코드 비활성화
                                viewModel.set('pdmGroupCode', '' ); // 기존 PDM 그룹코드 값 초기화
                            }

                        },
                        cancel : function() {
                            viewModel.set('bosSupplier', previousBosSupplier); // 이전 선택값으로 원복
                        }
                    });

                }

            },
            erpBrand : '', // ERP 브랜드
            bosBrand : '', // BOS 브랜드
            dpBrand : '', // 전시 브랜드
            eatsslimBrandCodeList : [], // '잇슬림' 브랜드 코드 목록
            onBosBrandSelect : function(e) { // BOS 브랜드 Select 이벤트

/*
                if( viewModel.get('bosSupplier') == pdmSupplierCode ) { // 공급업체가 PDM ( "풀무원 녹즙2" ) 인 경우

                	var isEatsslimBrand = false;  // '잇슬림' 브랜드 여부 : 기본값 false ( '잇슬림' 브랜드 아님 )

                    for( var i = viewModel.get('eatsslimBrandCodeList').length - 1 ; i >= 0 ; i -- ) {

                        if( e.dataItem.urBrandId == viewModel.get('eatsslimBrandCodeList')[i] ) { // '잇슬림' 브랜드 목록에 있는 경우
                            isEatsslimBrand = true;
                            break;
                        }

                    }

                    if( isEatsslimBrand ) { // '잇슬림' 브랜드인 경우

                        viewModel.set('isPdmGroupCodeDisabled', false); // PDM 그룹코드 입력 가능 ( 비활성화 해제 )

                    } else { // '잇슬림' 브랜드가 아닌 경우

                        viewModel.set('isPdmGroupCodeDisabled', true); // PDM 그룹코드 입력 불가능 ( 비활성화 )
                        viewModel.set('pdmGroupCode', ''); // 기존 PDM 그룹 코드 입력값 초기화

                    }
                }
*/
            },

            isBosBrandDisabled : true, // BOS 브랜드 비활성화 여부
            erpItemGroup : '', // ERP 상품군
            erpItemGroupDisplayName : '', // ERP 상품군 출력명
            bosItemGroup : '', // BOS 상품군
            isItemGroupDisabled : true, // 상품군 비활성화 여부 : 풀무원건강생활 ERP 품목인 경우 최초 등록시만 수정 가능
            erpStorageMethod : '', // ERP 보관방법
            bosStorageMethod : '', // BOS 보관방법
            erpOriginName : '', // ERP 원산지
            erpOriginDetailName : '', // ERP 원산지 상세 (해외일 경우에만)
            bosOriginType : '', // BOS 원산지 - 국가
            bosOriginDetail : '', // BOS 원산지 상세 - 지역, 해외, 직접입력
            onBosOriginChange : function(e) { // BOS 원산지 Change 이벤트

                switch( this.bosOriginType ) {

                case 'ORIGIN_TYPE.DOMESTIC' : // '국내' 선택시

                    this.set('isBosOriginDetailDirectInputVisible', false); // 원산지 직접입력 Input Visible 해제
                    this.set('isBosOriginDetailOverseasVisible', false); // BOS 원산지 상세 '해외' Dropdown Visible 해제

                    break;

                case 'ORIGIN_TYPE.OVERSEAS' : // '해외' 선택시

                    this.set('isBosOriginDetailDirectInputVisible', false); // 원산지 직접입력 Input Visible 해제
                    this.set('isBosOriginDetailOverseasVisible', true); // BOS 원산지 상세 '해외' Dropdown Visible 처리

                    break;

                case 'ORIGIN_TYPE.ETC' : // '기타' 선택시

                    this.set('isBosOriginDetailDirectInputVisible', true); // 원산지 직접입력 Input Visible 처리
                    this.set('isBosOriginDetailOverseasVisible', false); // BOS 원산지 상세 '해외' Dropdown Visible 해제

                    if( e != null ) {  // onBosOriginChange 수동 호출이 아닌 경우 원산지 상세 Input 에 Foucs
                        $('#bosOriginDetailDirectInputValue').focus();
                    }

                    break;

                }

            },
            isBosOriginDetailOverseasVisible : false , // BOS 원산지 상세 Dropdown Visible 여부
            bosOriginDetailDirectInputValue : '', // BOS 원산지 상세 직접입력 Input 의 값
            isBosOriginDetailDirectInputVisible : false, // BOS 원산지 상세 직접입력 input Visible 여부
            erpDistributionPeriod : '', // ERP 유통기간
            bosDistributionPeriod : '', // BOS 유통기간
            boxWidth : '', // BOS 박스 가로
            boxDepth : '', // BOS 박스 세로
            boxHeight : '', // BOS 박스 높이
            erpBoxWidth : '', // ERP 박스 가로
            erpBoxDepth : '',// ERP 박스 세로
            erpBoxHeight : '', // ERP 박스 높이
            bosPiecesPerBox : '', // BOS 박스 입수량
            erpPiecesPerBox : '', // ERP 박스 입수량
            unitOfMeasurement : '', // UOM/OMS ( 측정단위 )
            sizePerPackage : '', // 포장단위별 용량
            sizeUnit : '', // 용량(중량) 단위
            onSizeUnitChange : function(e) { // 용량(중량) 단위 change event

                if( this.sizeUnit == sizeUnitDirectInputCode ) {  // '직접입력' 선택시
                    this.set('isSizeUnitDirectInputVisible', true); // 용량(중량) 단위 직접입력 Input Visible 처리

                    if( e != null ) {  // onSizeUnitChange 수동호출이 아닌 경우 용량(중량) 단위 직접입력 Input 에 foucs
                        $('#sizeUnitDirectInputValue').focus();
                    }

                } else {
                    this.set('isSizeUnitDirectInputVisible', false); // 용량(중량) 단위 직접입력 Input Visible 해제
                }

            },
            sizeUnitDirectInputValue : '', // 용량(중량) 단위 직접입력 값
            isSizeUnitDirectInputVisible : false, // 용량(중량) 단위 직접입력 input Visible 여부

            quantityPerPackage : '', // 포장 구성수량
            packageUnit : '', // 포장 구성단위
            onPackageUnitChange : function(e) {

                if( this.packageUnit == packageUnitDirectInputCode ) {  // '직접입력' 선택시
                    this.set('isPackageUnitDirectInputVisible', true); // 포장 구성단위 직접입력 Input Visible 처리

                    if( e != null ) {  // onPackageUnitChange 수동호출이 아닌 경우 포장 구성단위 직접입력 Input 에 foucs
                        $('#packageUnitDirectInputValue').focus();
                    }
                } else {
                    this.set('isPackageUnitDirectInputVisible', false); // 포장 구성단위 직접입력 Input Visible 해제
                }

            },
            packageUnitDirectInputValue : '', // 포장 구성단위 직접입력 값
            isPackageUnitDirectInputVisible : false, // 포장 구성단위 직접입력 input Visible 여부
            pdmGroupCode : '', // PDM 그룹코드
            isPdmGroupCodeVisible : false, // PDM 그룹코드 Visible 여부
            isPdmGroupCodeDisabled : false, // PDM 그룹코드 비활성화 여부

            /* 판매 정보 */
            taxYn : '', // 과세구분
            erpTaxInvoiceType : '', // ERP 연동 품목 검색 데이터 중 세금계산서 발행구분
            erpTaxType : '', // ERP 과세구분 (과세[영업사용], 서울-면세일반, 본점-과세일반, 면세[영업사용], NULL)

            rentalFeePerMonth : '',
            rentalDueMonth : '',
            rentalDeposit : '',
            rentalInstallFee : '',
            rentalRegistFee : '',
            rentalTotalPrice : '',
            showItemPriceRow : false, // 통합가격정보의 가격 행 Visible 여부
            noItemPriceInfo : true,   // 가격정보가 없을때 Visible 여부

//            showFoodmerceErpItemPrice : false, // 푸드머스 ERP 연동 품목 가격 정보 행 Visible 여부
//            showLohasProductsErpItemPrice : false, // 건강생활 ERP 중 제품 연동 품목 가격 정보 행 Visible 여부
//            showLohasGoodsErpItemPrice : false, // 건강생활 ERP 중 상품 연동 품목 가격 정보 행 Visible 여부
//            showErpLinkItemPrice : false, // 기타 ERP 연동 품목 가격 정보 행 Visible 여부

            showRentalPriceVisible : false, // 렌탈품목 가격정보
            showItemPrice : false, // 가격정보행 Visiable 여부
            showPriceApproval: true, // 가격승인대상 여부 Visiable 여부
            showItemPriceAddBtn : false, // 가격추가버튼 Visiable 여부
            itemPriceScheduleList : [], // 가격정보 List

            approvalStatusCode : '',
            approvalStatusCodeName : '',
            priceApplyStartDate : '',
            priceApplyEndDate : '',
            standardPrice : '',
            recommendedPrice : '',
            priceRatio : '',
            approval1st : '',
            approvalConfirm : '',
            approvalTarget : '',
            erpPriceApplyStartDate : '', // ERP 연동 품목인 경우 출력할 가격 적용 시작일
            erpPriceApplyEndDate : '', // ERP 연동 품목인 경우 출력할 가격 적용 종료일
            erpStandardPrice : '', // ERP API 품목조회 인터페이스로 조회한 ERP 표준단가, 원가
            erpRecommendedPrice : '', // ERP API 품목조회 인터페이스로 조회한 ERP 권장소비자가, 정상가

            bosPriceApplyStartDate : '', // ERP 미연동 품목인 경우 출력할 가격 적용 시작일
            bosPriceApplyEndDate : '', // ERP 미연동 품목인 경우 출력할 가격 적용 종료일
            bosStandardPrice : '', // BOS 상에 저장할 표준단가, 원가
            bosRecommendedPrice : '', // BOS 상에 저장할 권장소비자가, 정상가
            rentalInfoChange : function(e) {
            	var rentalFeePerMonth = viewModel.get("rentalFeePerMonth");
            	var rentalDueMonth = viewModel.get("rentalDueMonth");
            	var rentalDeposit = viewModel.get("rentalDeposit");
            	var rentalInstallFee = viewModel.get("rentalInstallFee");
            	var rentalRegistFee = viewModel.get("rentalRegistFee");

            	if(rentalFeePerMonth == null || rentalFeePerMonth == "") {
            		rentalFeePerMonth = 0;
            	}else {
            		rentalFeePerMonth = parseInt(rentalFeePerMonth);
            	}

            	if(rentalDueMonth == null || rentalDueMonth == "") {
            		rentalDueMonth = 0;
            	}else {
            		rentalDueMonth = parseInt(rentalDueMonth);
            	}

            	if(rentalDeposit == null || rentalDeposit == "") {
            		rentalDeposit = 0;
            	}else {
            		rentalDeposit = parseInt(rentalDeposit);
            	}

            	if(rentalInstallFee == null || rentalInstallFee == "") {
            		rentalInstallFee = 0;
            	}else {
            		rentalInstallFee = parseInt(rentalInstallFee);
            	}

            	if(rentalRegistFee == null || rentalRegistFee == "") {
            		rentalRegistFee = 0;
            	}else {
            		rentalRegistFee = parseInt(rentalRegistFee);
            	}

            	var rentalTotalPrice = rentalFeePerMonth;

            	if(rentalDueMonth > 0) {
            		rentalTotalPrice = rentalFeePerMonth * rentalDueMonth;
            	}

            	rentalTotalPrice = rentalTotalPrice + (rentalDeposit + rentalInstallFee + rentalRegistFee);

            	viewModel.set("rentalTotalPrice", rentalTotalPrice);
            },
            onErpPriceChange : function(e) { // ERP 연동 품목에서 원가, 정산가 input 가격 change event

                // 정상가가 원가보다 작은 값으로 입력될 경우
                if( parseInt( viewModel.get('erpStandardPrice') ) > parseInt( viewModel.get('erpRecommendedPrice') ) ) {

                    fnKendoMessage({
                        message : '정상가는 원가보다 낮게 입력할 수 없습니다.<br>가격정보를 확인해주세요',
                        ok : function focusValue() {

                            viewModel.set( 'erpStandardPrice', '' );
                            viewModel.set( 'erpRecommendedPrice', '' );

                        }
                    });

                }

            },

            onErpRecommendedPriceChange : function(e) { // 원가가 고정값인 경우 정상가 input 가격 change event

                // 정상가가 원가보다 작은 값으로 입력될 경우
//                if( parseInt( viewModel.get('erpStandardPrice') ) > parseInt( viewModel.get('erpRecommendedPrice') ) ) {
//
//                    fnKendoMessage({
//                        message : '정상가는 원가보다 낮게 입력할 수 없습니다.<br>가격정보를 확인해주세요',
//                        ok : function focusValue() {
//
//                            // 정상가 기존 입력값 모두 초기화
//                            viewModel.set( 'erpRecommendedPrice', '' );
//
//                        }
//                    });
//
//                }

            },

            onBosPriceChange : function(e) { // ERP 미연동 품목에서 원가, 정산가 input 가격 change event

                // 정상가가 원가보다 작은 값으로 입력될 경우
//                if( parseInt( this.bosStandardPrice ) > parseInt(this.bosRecommendedPrice) ) {
//
//                    fnKendoMessage({
//                        message : '정상가는 원가보다 낮게 입력할 수 없습니다.<br>가격정보를 확인해주세요',
//                        ok : function focusValue() {
//
//                            viewModel.set( 'bosStandardPrice', '' );
//                            viewModel.set( 'bosRecommendedPrice', '' );
//
//                        }
//                    });
//
//                }

            },

            /* 영양 정보 */
            erpNutritionApiObj : {}, // ERP 품목 검색 후 영양정보 API 로 조회된 ERP 연동 품목의 영양 정보
            nutritionDisplayYn : '', // 영양정보 표시여부
            isNutritionInputAreaVisible : true, // 영양정보 입력영역 Visible 여부
            isNutritionInputAreaNotVisible : false, // 영양정보 입력영역 Not Visible 여부 : isNutritionInputAreaVisible 와 반댓값
            onNutritionDisplayYnChange : function(e) { // 영양정보 표시여부 change 이벤트 Listener : event 객체가 null 인 경우 스크립트에 의한 수동 호출

                switch (this.nutritionDisplayYn) { // nutritionDisplayYn 의 'Y', 'N' 값

                case 'Y': // 영양정보 표시여부 '노출' 로 변경시

                    this.set('isNutritionInputAreaVisible', true);
                    this.set('isNutritionInputAreaNotVisible', false);

                    break;

                case 'N': // 영양정보 표시여부 '노출 안함' 으로 변경시

                    this.set('isNutritionInputAreaVisible', false);
                    this.set('isNutritionInputAreaNotVisible', true);

                    break;

                }

            },
            nutritionDisplayDefalut : '상품 상세 이미지 참조', // 영양정보 기본정보 ( 영양정보 표시여부 '노출안함' 선택시 노출되는 항목 )

            erpNutritionAnalysisMessage : '', // ERP 영양분석단위 메시지
            showErpNutritionAnalysisMessage : false, // ERP 영양분석단위 메시지 출력 여부
            erpNutritionEtc : '', // ERP 영양정보 조회 API 로 조회한 기타 정보
            nutritionQuantityPerOnce : '', // BOS 영양분석 단위 (1회 분량)
            nutritionQuantityTotal : '', // BOS 영양분석 단위 (총분량)
            nutritionEtc : '', // BOS 영양성분 기타

            addAvailableNutritionCodeList: [], // 등록 가능 영양정보 분류코드 Select 에 출력할 코드 목록
            selectedAddAvailableNutritionCode : '', // 등록 가능 영양정보 분류코드 Select 에서 선택한 값
            itemNutritionDetailList : [], // 해당 품목코드의 영양정보 세부 항목 목록 : nutritionDetailList-row-template 에 바인딩

            // 영양정보 세부항목 추가 버튼 이벤트
            fnAddAvailableNutritionCode : function(e) {

                // 등록 가능한 영양정보 분류코드 Select 에서 선택한 분류 코드값 : calories, protein , ...
                var selectedNutritionCode = this.get('selectedAddAvailableNutritionCode');

                if (selectedNutritionCode == '' || viewModel.get('addAvailableNutritionCodeList').length == 0 ) {
                    valueCheck('등록 가능한 영양정보 분류 코드가 없습니다.', '');
                    return;
                }

                if (fnValidationCheck('addNutritionDetail', null) == false) { // Validation 체크 : 이미 추가한 항목인 경우 return
                    return;
                }

                addNutritionDetailRow(selectedNutritionCode); // 해당 영양정보 분류코드로 Row 추가

            },

            // 영양정보 세부항목 삭제 버튼 이벤트
            fnRemoveNutritionDetailRow : function(e) {
                var index = this.get("itemNutritionDetailList").indexOf(e.data); // e.data : "삭제" 버튼을 클릭한 행의 영양정보 세부항목
                this.get("itemNutritionDetailList").splice(index, 1); // viewModel 에서 삭제

                // 삭제한 영양정보 세부항목의 코드를 등록 가능 영양정보 분류코드에 다시 추가
                this.get('addAvailableNutritionCodeList').push({
                    nutritionCode : e.data['nutritionCode'], // 영앙정보 분류코드
                    nutritionName : e.data['nutritionCodeName'], // 영양정보 분류명
                    nutritionUnit : e.data['nutritionUnit'], // 영양정보 분류단위
                    nutritionPercentYn : e.data['nutritionPercentYn']  // 영양소 기준치 (%) 사용여부
                });

                // 등록 가능한 영양정보 분류코드 목록에 한 건만 데이터 존재시 해당 데이터 선택
                if( this.get('addAvailableNutritionCodeList').length == 1 ) {
                    this.set('selectedAddAvailableNutritionCode', e.data['nutritionCode'] );
                }

            },

            // 영양정보 검색 Autocomplete 에서 선택한 영양정보명 ( 열량, 콜레스테롤, ... )
            selectedNutritionName: '',

            // 영양정보 검색 Autocomplete Change 이벤트 : 영양코드명 선택시 해당 코드로 등록 가능 영양정보 분류코드 Select 의 값 변경
            onNutritionAutoCompleteChange: function(e) {

                for( var i = viewModel.get('addAvailableNutritionCodeList').length - 1 ; i >= 0 ; i -- ) {

                    if( viewModel.get('addAvailableNutritionCodeList')[i]['nutritionName'] == this.selectedNutritionName ) {

                        viewModel.set( 'selectedAddAvailableNutritionCode', viewModel.get('addAvailableNutritionCodeList')[i]['nutritionCode'] );
                        break;

                    }
                }
            },

            /* 상품 인증정보 */
            selectedIlCertificationId : '', // 상품 인증정보 Select 에서 선택한 상품 인증정보 ID
            itemCertificationCode : [], // 상품 인증정보 코드 목록
            itemCertificationList : [], // 화면에 출력되는 품목별 상품 인증정보 목록

            itemCertificationCodeMap : {}, // 상품 인증정보 코드 Map : { 상품 인증정보 ID : { 상품 인증정보 객체 } , ...  }
            itemCertificationValueMap : {}, // 품목별 상품 인증정보 세부 항목 Map : { 상품 인증정보 ID : { 품목별 상품 인증정보 세수 항목 객체 } , ... }

            itemCertificationMaxLimit : 3, // 품목별 상품 인증정보 최대 등록가능 개수
            itemCertificationCount : 0, // 화면에 출력된 상품 인증정보 개수 => itemCertificationList 의 데이터 건수

            // 상품 인증정보 세부항목 추가 이벤트
            fnAddItemCertification : function(e) {

                // 상품 인증정보 Select 에서 선택한 상품 인증정보 ID
                var selectedIlCertificationId = this.get('selectedIlCertificationId');

                if (selectedIlCertificationId) { // 유효한 상품 인증정보 ID 선택시

                    // Validation 체크 : 이미 추가한 항목이거나 이미 최대 등록가능 건수인 경우 return
                    if (fnValidationCheck('addItemCertification', null) == false) {
                        return;
                    }

                    var itemCertificationCodeObj = this.get('itemCertificationCodeMap')[selectedIlCertificationId];
                    var itemCertificationValueObj = this.get('itemCertificationValueMap')[selectedIlCertificationId];

                    // 선택한 상품 인증정보 ID 의 인증정보명
                    var certificationName = itemCertificationCodeObj['certificationName'];

                    // 해당 상품인증정보의 인증 이미지 URL
                    var certificationImageSrc = publicUrlPath + itemCertificationCodeObj['imagePath'] + itemCertificationCodeObj['imageName'];

                    // 화면에 출력할 인증정보 메시지
                    var certificationDescription = '';

                    if (itemCertificationValueObj) { // 품목별 상품 인증정보 상세항목 존재시 : 해당 항목의 상세정보 우선

                        certificationDescription = itemCertificationValueObj['certificationDescription'];

                    } else if (itemCertificationCodeObj) { // 상품인증정보 코드 정보 존재시 : 해당 코드의 기본 메시지를 화면에 출력

                        certificationDescription = itemCertificationCodeObj['defaultCertificationDescription'];

                    }

                    this.get('itemCertificationList').push({
                        ilCertificationId : selectedIlCertificationId,
                        certificationName : certificationName,
                        imageSrc : certificationImageSrc,
                        certificationDescription : certificationDescription
                    });

                    // 화면에 출력된 상품 인증정보 데이터 건수 갱신
                    this.set('itemCertificationCount', this.get("itemCertificationList").length);

                }

            },

            fnRemoveAllItemCertification : function(e) { // 화면상에 출력된 상품 인증정보 모두 삭제

                fnKendoMessage({
                    type : "confirm",
                    message : "현재 품목의 상품 인증정보를 모두 삭제하시겠습니까?<br>실제 삭제는 저장시 반영됩니다.",
                    ok : function() {
                        viewModel.set('itemCertificationList', []); // viewModel 내 itemCertificationList 데이터 초기화
                        viewModel.set('itemCertificationCount', 0); // 화면에 출력된 상품 인증정보 데이터 건수 0 으로 초기화
                    },
                    cancel : function() {
                        return;
                    }
                });

            },

            fnRemoveItemCertification : function(e) { // 삭제 버튼을 클릭한 행의 상품 인증정보 삭제

                var index = this.get("itemCertificationList").indexOf(e.data); // e.data : "삭제" 버튼을 클릭한 행의 상품 인증정보 세부항목
                this.get("itemCertificationList").splice(index, 1); // viewModel 에서 삭제

                // 화면에 출력된 상품 인증정보 데이터 건수 갱신
                this.set('itemCertificationCount', this.get("itemCertificationList").length);

            },

            /* 상품정보 제공고시 */
            selectedItemSpecMasterId : '', // 상품정보 제공고시 상품군 Select 에서 선택한 분류 코드 값 : 품목 등록시 해당 선택값이 저장됨
            itemSpecValueList : [], // 상품정보 제공고시 상세 항목 목록 : spec-row-template 에 바인딩
            itemSpecMap : {}, // 상품정보제공고시분류 코드 Map : { 상품정보제공고시분류 코드 : [ { 상품정보 제공고시 상세항목 } , ... ] , ... } 형식의 Map
            itemSpecValueMap : {}, // 품목별 상품정보제공고시 세부 항목 Map : { 상품정보제공고시분류 코드 : { 품목별 상품정보제공고시 상세 항목 } } 형식의 Map
            specDefalutValue : '상품 상세 이미지 참조', //
            setSpecFieldModifiedMessage : function(e) {  // 상품정보 제공고시 상세 메시지에 수정된 메시지 출력

                //  공급업체 미선택 or ( ERP 유통기간 미조회 && BOS 유통 기간 미입력 ) or 상품군 미선택시는 기존 출력된 상세 메시지를 모두 기본 메시지로 원복
                if( ! viewModel.get('bosSupplier')
                        || ( ! viewModel.get('erpDistributionPeriod') && ! viewModel.get('bosDistributionPeriod') )
                        || ! viewModel.get('selectedItemSpecMasterId') ) {

                    // 화면상에 출력된 상품정보 제공고시 목록에서 상품정보제공고시항목 코드 SPEC_FIELD_CD 유무 확인
                    if( viewModel.get('itemSpecValueList') && viewModel.get('itemSpecValueList').length > 1 ) {

                        for( var i = viewModel.get('itemSpecValueList').length - 1 ; i >= 0 ; i -- ) {

                            var item = viewModel.get('itemSpecValueList')[i];

                            // SPEC_FIELD_CD 존재하고 직접입력 체크박스가 미체크 상태인 경우
                            if( item['specFieldCode']  && item['directInputChecked'] == false ) {
                                item['basicValue'] = item['detailMessage'];  // 해당 상품정보 제공고시 항목에 최초 기본 메시지 세팅
                            }

                        }

                        viewModel.trigger("change", {
                            field : "itemSpecValueList"
                        });

                    }

                } else {  //

                    // 화면상에 출력된 상품정보 제공고시 목록에서 상품정보제공고시항목 코드 SPEC_FIELD_CD 유무 확인
                    if( viewModel.get('itemSpecValueList') && viewModel.get('itemSpecValueList').length > 1 ) {

                        for( var i = viewModel.get('itemSpecValueList').length - 1 ; i >= 0 ; i -- ) {

                            var item = viewModel.get('itemSpecValueList')[i];

                            // SPEC_FIELD_CD 존재하고 해당 항목 수정 가능 && 직접입력 체크박스가 미체크 상태인 경우
                            if( item['specFieldCode'] && item['canModified'] == true && item['directInputChecked'] == false ) {

                                // 해당 상품정보 제공고시 항목 코드로 수정된 상세 메시지 조회
                                // 관련 Data 는 fnGetItemSpecFieldDetailMessage 내에서 viewModel 의 값 직접 참조
                                var itemSpecModifiedMessage = fnGetItemSpecFieldDetailMessage( item['specFieldCode'] );

                                // 해당 상품정보 제공고시 항목에 조회된 메시지 세팅
                                item['basicValue'] = itemSpecModifiedMessage;

                            }

                        }

                        viewModel.trigger("change", {
                            field : "itemSpecValueList"
                        });

                    }

                }

            },

            // 상품정보 제공고시 상품군 Select 값 변경시 화면에 해당 상세 항목 출력
            onSpecGroupChange : function(e) {

                // 상품정보 제공고시 상품군 Select 에서 선택된 상품정보제공고시 분류 PK
                var selectedItemSpecMasterId = this.get('selectedItemSpecMasterId');

                if (selectedItemSpecMasterId) { // 유효한 상품정보제공고시 분류 PK 선택시

                    // 현재 선택된 상품정보제공고시 분류 PK 에 해당하는 상세항목 목록을 itemSpecValueList 에 추가
                    var itemSpecValueListOfChosenItemSpecMasterId = this.get('itemSpecMap')[selectedItemSpecMasterId];

                    if( itemSpecValueListOfChosenItemSpecMasterId ) {
                        viewModel.set('itemSpecValueList', itemSpecValueListOfChosenItemSpecMasterId);

                    } else {
                        viewModel.set('itemSpecValueList', []);

                    }

                } else { // '선택' 선택시

                    viewModel.set('itemSpecValueList', []);

                }

                viewModel.trigger("change", {
                    field : "itemSpecValueList"
                });

                // 스크립트상의 수동 호출인 경우 ( 예 : 마스터 품목 정보 복사시 ) 호출하지 않음
                if( e != null ) {
                    viewModel.setSpecFieldModifiedMessage(null);  // 상품정보 제공고시 상세 메시지 출력 이벤트 수동 호출
                }

            },

            // 상품정보 제공고시 각 상세항목의 '직접입력' 체크박스 클릭 이벤트
            fnCheckSpecDirectInput : function(e) {

                var index = viewModel.get("itemSpecValueList").indexOf(e.data); // 체크박스 클릭한 행의 index
                var itemSpecValueObj = viewModel.get('itemSpecValueList[' + index + ']'); // 체크박스 클릭한 행의 viewModel Data

                // 중요!! 하단 if 문의 itemSpecValueObj['directInputChecked'] 값은 클릭 이전의 값
                if (itemSpecValueObj['directInputChecked'] == false) { // 직접입력' 체크박스 클릭시 : 해당 행의 Value Input 입력 활성화

                    viewModel.set('itemSpecValueList[' + index + '].isValueDisabled', false);

                } else { // '직접입력' 체크박스 클릭 해제시 : 해당 행의 Value Input 입력 비활성화

                    viewModel.set('itemSpecValueList[' + index + '].isValueDisabled', true);

                    // SPEC_FIELD_CD 존재 && 해당 항목 수정 가능한 경우
                    if( itemSpecValueObj['specFieldCode'] && itemSpecValueObj['canModified'] == true ) {

                        //  공급업체 미선택 or ( ERP 유통기간 미조회 && BOS 유통 기간 미입력 ) or 상품군 미선택시는 기존 출력된 상세 메시지를 기본 메시지로 원복
                        if( ! viewModel.get('bosSupplier')
                                || ( ! viewModel.get('erpDistributionPeriod') && ! viewModel.get('bosDistributionPeriod') )
                                || ! viewModel.get('selectedItemSpecMasterId') ) {

                            // 해당 상품정보 제공고시 항목에 최초 기본 메시지 세팅
                            viewModel.set('itemSpecValueList[' + index + '].basicValue', itemSpecValueObj['detailMessage']);

                        } else {

                            // 해당 상품정보 제공고시 항목 코드로 수정된 상세 메시지 조회
                            // 관련 Data 는 fnGetItemSpecFieldDetailMessage 내에서 viewModel 의 값 직접 참조
                            var itemSpecModifiedMessage = fnGetItemSpecFieldDetailMessage( itemSpecValueObj['specFieldCode'] );

                            // 해당 상품정보 제공고시 항목에 조회된 메시지 세팅
                            viewModel.set('itemSpecValueList[' + index + '].basicValue', itemSpecModifiedMessage);

                        }

                    }

                }

            },

            /* 배송 / 발주 정보 */
            erpPoType : '', // ERP API 로 조회한 연동 품목 데이터의 발주유형
            erpPoTypeDisplayName : '', // ERP API 로 조회한 연동 품목 데이터의 발주유형 화면상의 출력명 : 발주유형 미조회시 '없음' 출력
            selectedBosPoType : '', // BOS 발주유형 Select 에서 선택한 발주유형
            itemPoTypeCodeList : [], // BOS 발주유형 코드 목록
            poTypeDetailInformationPopupVisible : false, 	// 발주유형 상세정보 popup 버튼 visible 여부
            erpPoTp : '', // 발주유형
//            onBosPoTypeChange : function(e) {  // BOS 발주유형 Select change 이벤트
//
//                for( var i = viewModel.get('itemPoTypeCodeList').length - 1 ; i >= 0 ; i -- ) {
//
//                    // 사용자가 선택한 발주유형 코드의  발주일 설정값 품목별 상이 여부 값이 true 인 경우
//                    if( viewModel.get('itemPoTypeCodeList')[i]['poTypeCode'] == viewModel.get('selectedBosPoType') ) {
//
//                    	this.erpPoTp = viewModel.get('itemPoTypeCodeList')[i]['erpPoTp'];
//
//                        if(viewModel.get('itemPoTypeCodeList')[i]['poPerItemYn'] == true ) {
//
//                            this.set( 'poTypeDetailInformationPopupVisible', true ); // 발주유형 상세정보 popup 버튼 Visible
//
//                        } else {
//
//                            this.set( 'poTypeDetailInformationPopupVisible', false ); // 발주유형 상세정보 popup 버튼 Visible 해제
//
//                        }
//
//                        break;
//                    }
//
//                }
//
//            },

            erpCanPoYn : true, // ERP API 로 조회한 ERP 연동 품목 데이터에서 ERP 발주가능여부
            erpCanPoYnMessage : '', // ERP 발주가능여부 출력 메시지
            hasErpCanPoYn : false, //  ERP 발주가능여부 메시지 화면상에 노출 여부 : 올가 ERP 인 경우에만 출력됨

            islandShippingYn : false, // 도서산간지역 (1권역) 배송여부 : true 시 배송 불가
            jejuShippingYn : false, // 제주지역 (2권역) 배송여부 : true 시 배송 불가

            extinctionYn : 'N', // 단종여부
            returnPeriod : '', // 반품 가능기간

            // 반품 가능 기간 계산 event : 표준 카테고리, 보관 방법 변경시 call
            onCalculateReturnPeriod : function(e) {

                // 화면에서 표준카테고리 or 보관방법 미선택시, 또는 최하위 카테고리까지 선택하지 않은 경우 반품 가능기간 '' 으로 출력 / return
                // 표준 카테고리와 보관방법은 저장시 필수 선택값 : 반품 가능기간은 반드시 선택됨
                if( ! this.get('bosStorageMethod') || ! checkCategoryStandardId() ) {

                    viewModel.set('returnPeriod', '');
                    return;

                }

                fnAjax({
                    url : "/admin/item/master/register/getReturnPeriod",
                    method : 'GET',
                    params : {
                        ilCategoryStandardId : checkCategoryStandardId(),  // 표준 카테고리 PK
                        storageMethodType : this.get('bosStorageMethod')   // 보관방법
                    },
                    isAction : 'select',
                    success : function(data, status, xhr) {

                        viewModel.set('returnPeriod', data['returnPeriodMessage']);  // 반품 가능기간 메시지 표시

                    }
                });

            },

            /* 출고처 정보 */
            showAddWarehouse: true, // 출고처 등록 Visiable 여부
            selectedWarehouseGroupCode : '', // 출고처 그룹 Select 에서 선택된 출고처 코드 그룹 값
            selectedUrSupplierWarehouseId : '', // 출고처 Select 에서 선택된 공급업체-출고처 PK 값
            itemWarehouseMap : {}, // ( 출고처 그룹 코드 : [ { 출고처 정보 } , ... ] , ... ) 형식의 Map
            itemWarehouseList : [], // 해당 품목코드의 출고처 목록 : warehouse-row-template 에 바인딩
            noItemWarehouseInfo : true, //출고처 없을때 노출
            // 출고처 추가 버튼 이벤트
            fnAddWarehouse : function(e) { //
                if( ! this.get('selectedWarehouseGroupCode') || ! this.get('selectedUrSupplierWarehouseId') ) {
                    return;
                }

                // 출고처 그룹 Select 에서 선택된 출고처 그룹 코드
                var selectedWarehouseGroupCode = this.get('selectedWarehouseGroupCode');

                // 출고처 Select 에서 선택한 공급업체-출고처 PK 값
                var selectedUrSupplierWarehouseId = this.get('selectedUrSupplierWarehouseId');

                if (fnValidationCheck('addWarehouse', null) == false) { // Validation 체크 : 이미 추가한 항목인 경우 return
                    return;
                }

                // 현재 선택된 출고처 그룹 코드에 해당하는 출고처 목록
                var warehouseListOfChosenWarehouseGroupCode = this.get('itemWarehouseMap')[selectedWarehouseGroupCode];

                if( warehouseListOfChosenWarehouseGroupCode == undefined ) {
                    return;
                }

                var storeCnt = 0;
                for(var i = 0; i < this.get('itemWarehouseList').length ; i ++) {
                	if(this.get("itemWarehouseList")[i].storeYn == "Y") {
                		storeCnt++;
                	}
                }

                // 출고처 Select 에서 선택한 공급업체-출고처 PK 값에 해당하는 출고처 정보를 viewModel 내 itemWarehouseList 에 push
                for (var i = warehouseListOfChosenWarehouseGroupCode.length - 1; i >= 0; i--) {

                	// 렌탈 품목일 경우 매장(가맹점) 출고처 추가 안됨
                	if(viewModel.get("masterItemType") == "MASTER_ITEM_TYPE.RENTAL" && warehouseListOfChosenWarehouseGroupCode[i]['urSupplierWarehouseId'] == selectedUrSupplierWarehouseId) {
                		if(warehouseListOfChosenWarehouseGroupCode[i]['storeYn'] == "Y") {
                			valueCheck('매장(가맹점) 출고처를 출고처로 등록하실 수 없습니다.', 'warehouse');
                            return;
                		}
                	}

                	// ERP 연동 품목인 경우
                    if (this.get('isErpItemLink') == true && warehouseListOfChosenWarehouseGroupCode[i]['urSupplierWarehouseId'] == selectedUrSupplierWarehouseId) {

                        // 추가된 출고지가 없는 미선택 상태에서 매장(가맹점)으로 되어 있는 출고처를 추가 실행 시 alert 표시 후 실행 중지
                        if( this.get('itemWarehouseList').length == 0 && warehouseListOfChosenWarehouseGroupCode[i]['storeYn'] == "Y"  ) {

                            // 단, 공급업체가 풀무원 녹즙1, 풀무원 녹즙2 일 경우 추가 허용, 그 외에는 alert 표시 후 실행 중지
                            if( this.get('bosSupplier') != fddSupplierCode && this.get('bosSupplier') != pdmSupplierCode ) {

                                valueCheck('매장(가맹점) 출고처를 최초 출고처로 등록하실 수 없습니다.', 'warehouse');
                                return;

                            }

                        }
                        viewModel.set('noItemWarehouseInfo', false);            // 출고처 빈영역 감추기.

                        if(storeCnt >= 1 && warehouseListOfChosenWarehouseGroupCode[i].storeYn == "Y") {
                        	valueCheck('매장(가맹점) 출고처를 1개이상 등록하실 수 없습니다.', 'warehouse');
                   		 	return;
                        }

                        // 마스터 품목 등록 화면에서 출고처 추가시 기본적으로 모두 삭제 가능
                        warehouseListOfChosenWarehouseGroupCode[i]['canDeleted'] = true;

                        this.get('itemWarehouseList').push(warehouseListOfChosenWarehouseGroupCode[i]);
                    	if(warehouseListOfChosenWarehouseGroupCode[i].stockOrderYn != "Y") { // 재고 발주 미연동일 경우 발주유형 select box 노출하지 않는다.
                        	$("#itemWarehousePoTpIdTemplateList"+warehouseListOfChosenWarehouseGroupCode[i].urWarehouseId).hide();
                    	}
                    	else {
                            if(viewModel.get("bosSupplier") == "2" && (viewModel.get("erpPoTypeDisplayName").indexOf("센터(R2)") != -1)) {
                            	fnKendoDropDownList({
        							id			: "itemWarehousePoTpIdTemplateList"+warehouseListOfChosenWarehouseGroupCode[i].urWarehouseId,
        							url			: "/admin/item/master/register/getItemPoTypeCode",
        							params		: {urSupplierId : viewModel.get("bosSupplier"), erpPoType : viewModel.get("erpPoTypeDisplayName")},
        							textField	:"poTypeName",
        							valueField	: "poTypeCode",
        							blank : '선택'
        						});
                            } else {
                            	$("#itemWarehousePoTpIdTemplateList"+warehouseListOfChosenWarehouseGroupCode[i].urWarehouseId).show();

                            	fnKendoDropDownList({
        							id			: "itemWarehousePoTpIdTemplateList"+warehouseListOfChosenWarehouseGroupCode[i].urWarehouseId,
        							url			: "/admin/item/master/register/getItemPoTypeCode",
        							params		: {urSupplierId : viewModel.get("bosSupplier")},
        							textField	:"poTypeName",
        							valueField	: "poTypeCode",
        							blank : '선택'
        						});
                            }
                    	}

                        // 용인물류 출고처 일때 매장 출고처 포함 set
                        if(warehouseListOfChosenWarehouseGroupCode[i]['yonginWarehouseYn'] == 'Y'){
                        	addStoreWareHouse(true);
                        }

                        break;

                    // ERP 미연동 품목인 경우
                    } else if(this.get('isErpItemLink') == false && warehouseListOfChosenWarehouseGroupCode[i]['urSupplierWarehouseId'] == selectedUrSupplierWarehouseId) {

                        // ERP 미연동 품목은 매장(가맹점)으로 되어 있는 출고처를 추가 실행 시 alert 표시 후 실행 중지
                        if( warehouseListOfChosenWarehouseGroupCode[i]['storeYn'] == "Y"  ) {

                            valueCheck('매장(가맹점) 출고처는 등록하실 수 없습니다.', 'warehouse');
                            return;

                        }

                        // 마스터 품목 등록 화면에서 출고처 추가시 기본적으로 모두 삭제 가능
                        warehouseListOfChosenWarehouseGroupCode[i]['canDeleted'] = true;
                        this.get('itemWarehouseList').push(warehouseListOfChosenWarehouseGroupCode[i]);

						if(warehouseListOfChosenWarehouseGroupCode[i].stockOrderYn != "Y") { // 재고 발주 미연동일 경우 발주유형 select box 노출하지 않는다.
							$("#itemWarehousePoTpIdTemplateList"+warehouseListOfChosenWarehouseGroupCode[i].urWarehouseId).hide();
						}
						else {
	                        if(viewModel.get("bosSupplier") == "2" && (viewModel.get("erpPoTypeDisplayName").indexOf("센터(R2)") != -1)) {
	                        	fnKendoDropDownList({
	                        			id			: "itemWarehousePoTpIdTemplateList"+warehouseListOfChosenWarehouseGroupCode[i].urWarehouseId,
										url			: "/admin/item/master/register/getItemPoTypeCode",
										params		: {urSupplierId : viewModel.get("bosSupplier"), erpPoType : viewModel.get("erpPoTypeDisplayName")},
										textField	:"poTypeName",
										valueField	: "poTypeCode",
										blank : '선택'
	                        	});

							} else {
								$("#itemWarehousePoTpIdTemplateList"+warehouseListOfChosenWarehouseGroupCode[i].urWarehouseId).show();

								fnKendoDropDownList({
									id			: "itemWarehousePoTpIdTemplateList"+warehouseListOfChosenWarehouseGroupCode[i].urWarehouseId,
									url			: "/admin/item/master/register/getItemPoTypeCode",
									params		: {urSupplierId : viewModel.get("bosSupplier")},
									textField	:"poTypeName",
									valueField	: "poTypeCode",
									blank : '선택'
								});
							}
						}
                        break;

                    }

                }

            },

            // 출고처 삭제 버튼 이벤트
            fnRemoveWarehouseRow : function(e) {
                var index = this.get("itemWarehouseList").indexOf(e.data); // e.data : "삭제" 버튼을 클릭한 행의 영양정보 세부항목
                this.get("itemWarehouseList").splice(index, 1); // viewModel 에서 삭제

                // 출고처 목록 전체 삭제시 빈영역 노출처리
                if(this.get("itemWarehouseList").length == 0){
                    viewModel.set('noItemWarehouseInfo', true);
                }
            },

            /* 매장별 재고 정보 */
            visibleStoreStockInfo : false, // 매장별 재고정보 visible

            /* 상품 이미지 */
            itemImageList : [], // 상품 이미지 영역에 출력되는 이미지 정보 목록
            itemImageFileList : [],  //상품 이미지 파일 목록

            itemImageCount : 0, // 상품 이미지 영역에 추가된 이미지 개수
            itemImageMaxLimit : 10, // 상품 이미지 최대 등록가능 개수

            fnRemoveItemImage : function(e) { // 상품 이미지 thumbnail 내 "X" 클릭 이벤트 : 추가된 상품 이미지 삭제

                for (var i = this.get("itemImageList").length - 1; i >= 0; i--) {
                    if (this.get("itemImageList")[i]['imagePath'] == e.data["imagePath"]) {
                        this.get("itemImageList").splice(i, 1); // viewModel 에서 삭제
                    }
                }

                var itemImageFileList = this.get('itemImageFileList');

                for (var i = itemImageFileList.length - 1; i >= 0; i--) {
                    if (itemImageFileList[i]['name'] == e.data['imagePath']) {
                        itemImageFileList.splice(i, 1); // 삭제한 파일명에 해당하는 file 객체 삭제
                    }
                }

                viewModel.set('itemImageCount', viewModel.get('itemImageList').length); // 추가된 상품 이미지 개수 갱신

                fnAddItemImageArea(); // 삭제시 항상 기존 상품 추가 영역 삭제 / 마지막 목록에 상품 추가 영역 새로 append

            },
            itemImageUploadResultList : [], // 상품 이미지 업로드 결과 Data : 품목 등록시 사용
            videoUrl : '', // 동영상 URL
            videoAutoplayYn : false, // 비디오 자동재생 유무 ( 체크시 : 자동재생 )

            // 메모
            etcDescription : '',

            /* 추후 작업 / 확인 필요 항목들 */
            erpStockIfYn : false, // ERP 재고연동여부

            // 승인 관련
            ilItemApprId : '',
            itemApprovalStatusViewVisible : false,
            itemApprList : [],
            apprKindTp : '',
            itemStatusTp : '',


    };

    // viewModelOriginal 을 deepClone 한 객체를 viewModel 로 세팅
    viewModel = kendo.observable(deepClone(viewModelOriginal));

    fnInitialize(); // Initialize Page Call
    publicUrlPath = fnGetPublicUrlPath(); // Public 저장소 Root Url 조회

    // 데이터 마이그레이션 관련 로직 Start : 추후 삭제 예정
    fnShowMigrationDataButton();
    // 데이터 마이그레이션 관련 로직 End

    // Initialize PageR
    function fnInitialize() {
        $scope.$emit('fnIsMenu', {
            flag : 'true'
        });

        fnPageInfo({
            PG_ID : 'itemMgm',
            callback : fnUI
        });
    };

    /***************************************************************************
     * 화면 UI 구성
     ***************************************************************************/

    function fnUI() {

        fnTranslate(); // comm.lang.js 안에 있는 공통함수 다국어

        fnInitButton(); // Initialize Button

        fnInitItemMgm();

        initUI();

		getGoodsDetailInfoEditorTemplate();

		fnApproInit();
        fnApprAdminInit();

        if(PG_SESSION.companyType == "COMPANY_TYPE.CLIENT" && PG_SESSION.clientType == "CLIENT_TYPE.CLIENT") {
              $("#inputForm input").attr("disabled", true);
        }

        fnItemAuthInputAllow();

    };

    function fnInitButton() {
        $('#fnItemUpdateHistorySearch, #fnMasterItemSearch, #fnPoTypeDetailInformationPopup').kendoButton();
    };

    function initUI() {

        kendo.bind($("#view"), viewModel);

        // viewModel 이 화면에 바인딩된 후에 kendoSortable 을 적용해야 함
        // 영양정보 상세항목에 sortable 적용
        $("#nutritionDetailContainer").kendoSortable({
            filter : "tr",
            cursor : "move",
            ignore: "input",  // 해당 row 내 input 은 sortable 이벤트에서 무시, 그래야 입력 가능함
            placeholder: function(element) {
                return element.clone().addClass("k-state-hover").css("opacity", 0.7);
            },
            hint: function(element) { // Customize the hint.

                var table = $('<table class="k-grid k-widget"></table>');

                table.append(element.clone()); // Append the dragged element.
                table.css("opacity", 0.7);

                return table; // Return the hint element.
            },
            change : function(e) {

                // remove the item that has changed its order
                var item = viewModel.get('itemNutritionDetailList').splice(e.oldIndex, 1)[0];

                // add the item back using the newIndex
                viewModel.get('itemNutritionDetailList').splice(e.newIndex, 0, item);

                viewModel.trigger("change", {
                    field : "itemNutritionDetailList"
                });

            }
        });

        // 상품 이미지에 sortable 적용
        $("#itemImageContainer").kendoSortable({
            filter : ".itemImage",
            cursor : "move",
            placeholder : function(element) {
                return element.clone().css("opacity", 0.1);
            },
            hint : function(element) {
                return element.clone().css("width", element.width()).removeClass("k-state-selected");
            },
            change : function(e) {

                // remove the item that has changed its order
                var item = viewModel.get('itemImageList').splice(e.oldIndex, 1)[0];

                // add the item back using the newIndex
                viewModel.get('itemImageList').splice(e.newIndex, 0, item);
                viewModel.trigger("change", {
                    field : "itemImageList"
                });

                fnAddItemImageArea();  // 사용자가 이미지 순서 변경시 기존 상품 이미지 추가 영역 삭제 / 이미지 영역 마지막에 append

            }
        });

        fnAddItemImageArea();  // 최초 화면 조회시 상품 이미지 추가 영역 append

    }

    // 상품 이미지 추가 Html 을 상품 이미지 목록이 매핑되는 itemImageContainer 의 마지막에 동적 추가
    function fnAddItemImageArea() {

        // 상품 이미지 최대 등록 개수 이상인 경우 return
        if( viewModel.get('itemImageList').length >= viewModel.get('itemImageMaxLimit') ) {
            return;
        }

        $('#itemImageAdd').remove();  // 기존 추가된 상품 이미지 추가 Html 제거

        var itemImageAddHtml = '<span id="itemImageAdd" class="itemImageAdd" ';
        itemImageAddHtml += 'style="float: left; width: 150px; height: 150px; padding: 10px; margin-right: 10px; margin-bottom: 10px; position: relative; border-color: #a9a9a9; border-style: dashed; border-width: 5px" ';
        itemImageAddHtml += '> <span class="k-icon k-i-plus" '
        itemImageAddHtml += 'style="width: 100%; height: 100%; font-size: 75px; color: #a9a9a9;" ';
        itemImageAddHtml += '> </span></span>';

        $("#itemImageContainer").append(itemImageAddHtml);
        // $("#divContainer").append(itemImageAddHtml);

        $("#itemImageAdd").on("click", function(event) {
            $('#uploadItemImage').trigger('click'); // 상품 이미지 파일 input Tag 클릭 이벤트 호출
        });

    }

    /***************************************************************************
     * 품목 등록 관련 funtion
     ***************************************************************************/

    function fnSave() { // 품목 등록

        if (fnValidationCheck('save', null) == false) { // 저장 전 validation 체크
            return;
        };

        fnKendoMessage({
            type : "confirm",
            message : "저장하시겠습니까?",
            ok : function() {

                if (viewModel.get('itemImageFileList').length > 0) { // 상품 이미지 File 목록 존재시
                    var itemImageUploadResultList = fnUploadItemImage(); // 상품 이미지 업로드 / 결과 return

                    viewModel.set('itemImageUploadResultList', itemImageUploadResultList);
                    viewModel.set('itemImageFileList', []); // 기존 이미지 파일 저장 목록 초기화
                }

                fnAddItem(); // 품목 등록

            },
            cancel : function() {
                return;
            }
        });

    };

    function fnUploadItemImage() { // 품목 정보 등록 전 품목 이미지 업로드

        var formData = new FormData();
        var itemImageFileList = viewModel.get('itemImageFileList');

        for (var i = 0; i < itemImageFileList.length; i++) {
            // itemImage01, itemImage02, ... 형식으로 formData 에 이미지 file 객체 append
            formData.append('itemImage' + ('0' + (i + 1)).slice(-2), itemImageFileList[i]);
        }

        formData.append('storageType', 'public'); // storageType 지정
        formData.append('domain', 'il'); // domain 지정

        var itemImageUploadResultList; // 상품 이미지 업로드 결과 목록
        viewModel.set('itemImageUploadResultList', []);

        $.ajax({
            url : '/comn/fileUpload',
            data : formData,
            type : 'POST',
            contentType : false,
            processData : false,
            async : false,
            success : function(data) {
                data = data.data;
                itemImageUploadResultList = data['addFile'];
            }
        });

        return itemImageUploadResultList;

    }

    function fnAddItem() { // 실제 품목 등록 API 호출

        var addItemParam = {

            /* 기본 정보 */
            // 마스터 품목 유형
            itemType : viewModel.get("masterItemType"),

            // ERP 품목 연동여부
            erpLinkIfYn : (viewModel.get("isErpItemLink") == true ? true : false),

            // 품목코드 : ERP 연동 상품인 경우 ERP 품목코드, 아닌 경우 '' => 미연동 상품은 품목 등록시 품목코드 생성
            ilItemCode : (viewModel.get("isErpItemLink") == true ? viewModel.get("erpItemCode") : ''),

            // ERP 품목명
            erpItemName : viewModel.get("erpItemName"),

            // 마스터 품목명
            itemName : $.trim( viewModel.get("itemName") ),

            // BOS 품목 바코드
            itemBarcode : viewModel.get("bosItemBarCode"),

            // ERP 품목 바코드
            erpItemBarcode : viewModel.get("erpItemBarCode"),

            // 매장 품목 유형 ( ERP 연동 품목 중 올가 ERP 전용 )
            erpO2oExposureType : (viewModel.get("hasO2oExposureType") == true ? viewModel.get("o2oExposureType") : ''),

            // 상품 판매 유형 ( ERP 연동 품목 중 건강생활 ERP 전용 )
            erpProductType : (viewModel.get("hasProductType") == true ? viewModel.get("productType") : ''),

            // ERP 법인 코드
            erpLegalTypeCode : (viewModel.get("isErpItemLink") == true ? viewModel.get("legalType") : ""),

            // ERP 재고연동여부
            erpStockIfYn : viewModel.get("erpStockIfYn"),

            // ERP 카테고리
            erpCategoryLevel1Id : viewModel.get("erpCategoryLevel1Id"),
            erpCategoryLevel2Id : viewModel.get("erpCategoryLevel2Id"),
            erpCategoryLevel3Id : viewModel.get("erpCategoryLevel3Id"),
            erpCategoryLevel4Id : viewModel.get("erpCategoryLevel4Id"),

            // 표준 카테고리 PK
            ilCategoryStandardId : checkCategoryStandardId(),

            /* 상세 정보 */

            // 공급업체 PK
            urSupplierId : viewModel.get("bosSupplier"),

            // ERP 생산처
            erpSupplierName : viewModel.get("erpSupplierName"),

            // ERP 브랜드
            erpBrandName : viewModel.get("erpBrand"),

            // BOS 브랜드 PK
            urBrandId : viewModel.get("bosBrand"),

            // 전시 브랜드 PK
            dpBrandId : viewModel.get("dpBrand"),

            // ERP 상품군
            erpItemGroup : viewModel.get("erpItemGroup"),

            // BOS 상품군
            bosItemGroup : viewModel.get("bosItemGroup"),

            // ERP 보관방법
            erpStorageMethod : viewModel.get("erpStorageMethod"),

            // BOS 보관방법
            storageMethodType : viewModel.get("bosStorageMethod"),

            // ERP 원산지
            erpOriginName : viewModel.get("erpOriginName"),

            // ERP 원산지 상세 (해외일 경우에만)
            erpOriginDetailName : viewModel.get("erpOriginDetailName"),

            // BOS 원산지
            originType : viewModel.get("bosOriginType"),

            // BOS 원산지 상세
            originDetail : fnGetOriginDetail(),

            // BOS 유통기간 : ERP 연동품목인 경우 최초 등록시 0 입력
            distributionPeriod : viewModel.get("bosDistributionPeriod"),

            // ERP 유통기간
            erpDistributionPeriod : viewModel.get("erpDistributionPeriod"),

            // BOS 박스 가로
            boxWidth : viewModel.get("boxWidth"),

            // BOS 박스 세로
            boxDepth : viewModel.get("boxDepth"),

            // BOS 박스 높이
            boxHeight : viewModel.get("boxHeight"),

            // ERP 박스 가로
            erpBoxWidth : viewModel.get("erpBoxWidth"),

            // ERP 박스 세로
            erpBoxDepth : viewModel.get("erpBoxDepth"),

            // ERP 박스 높이
            erpBoxHeight : viewModel.get("erpBoxHeight"),

            // ERP 품목 가로
            erpItemWidth : viewModel.get("erpItemWidth"),

            // ERP 품목 세로
            erpItemDepth : viewModel.get("erpItemDepth"),

            // ERP 품목 높이
            erpItemHeight : viewModel.get("erpItemHeight"),

            // BOS 박스 입수량
            piecesPerBox : viewModel.get("bosPiecesPerBox"),

            // ERP 박스 입수량
            erpPiecesPerBox : viewModel.get("erpPiecesPerBox"),

            // UOM/OMS
            unitOfMeasurement : viewModel.get("unitOfMeasurement"),

            // 포장단위별 용량
            sizePerPackage : viewModel.get("sizePerPackage"),

            // 용량(중량) 단위
            sizeUnit : viewModel.get("sizeUnit"),

            // 용량(중량) 단위 기타 : 용량(중량) 단위 Dropdown 에서 "직접입력" 선택시 sizeUnitDirectInputValue 의 값 지정, 아닌 경우 '' 지정
            sizeUnitEtc : ( viewModel.get("sizeUnit") == sizeUnitDirectInputCode ? viewModel.get("sizeUnitDirectInputValue") : '' ),

            // 포장 구성수량
            quantityPerPackage : viewModel.get("quantityPerPackage"),

            // 포장 구성단위
            packageUnit :  viewModel.get("packageUnit"),

            // 포장 구성단위 기타 : 포장 구성단위 Dropdown 에서 "직접입력" 선택시 packageUnitDirectInputValue 의 값 지정, 아닌 경우 '' 지정
            packageUnitEtc : ( viewModel.get("packageUnit") == packageUnitDirectInputCode ? viewModel.get("packageUnitDirectInputValue") : '' ),

            // PDM 그룹코드 : 공급업체가 PDM ( "풀무원 녹즙2" ) 인 경우에만 해당 값 전송
            pdmGroupCode : ( viewModel.get('bosSupplier') == pdmSupplierCode ? viewModel.get("pdmGroupCode") : '' ),

            /* 판매 정보 */
            // 과세 구분
            taxYn : (viewModel.get("taxYn") == 'Y' ? true : false),

            // ERP 과세 구분 : ERP 연동 품목인 경우에만 과세 구분과 동일한 값을 전송
            erpTaxYn : (viewModel.get("isErpItemLink") == true ? (viewModel.get("taxYn") == 'Y' ? true : false) : null),

            // 가격 적용 시작일
//            priceApplyStartDate : (viewModel.get("isErpItemLink") == true ? viewModel.get('erpPriceApplyStartDate') : viewModel.get('bosPriceApplyStartDate') ),
            priceApplyStartDate : viewModel.get('priceApplyStartDate'),

            // 원가
//            standardPrice : (viewModel.get("isErpItemLink") == true ? viewModel.get('erpStandardPrice') : viewModel.get('bosStandardPrice') ),
            standardPrice : viewModel.get('standardPrice'),
            // 정상가
//            recommendedPrice : (viewModel.get("isErpItemLink") == true ? viewModel.get('erpRecommendedPrice') : viewModel.get('bosRecommendedPrice') ),
            recommendedPrice : viewModel.get('recommendedPrice'),

            /* 상품정보 제공고시 */
            ilSpecMasterId : viewModel.get('selectedItemSpecMasterId'), // 상품정보제공고시분류 PK

            /* 영양 정보 */
            // 영양정보 표시여부
            nutritionDisplayYn : (viewModel.get("nutritionDisplayYn") == 'Y' ? true : false),

            // 영양정보 기본정보 ( 영양정보 표시여부 값이 '노출 안함' 일때 노출되는 항목 ) => 영양정보 표시여부 '노출안함' 인 경우에만 저장
            nutritionDisplayDefalut : (viewModel.get("nutritionDisplayYn") == 'N' ? ($.trim(viewModel.get("nutritionDisplayDefalut")) == '' ? '상품 상세 이미지 참조' : viewModel.get("nutritionDisplayDefalut")) : ''),

            // BOS 영양분석 단위 (1회 분량) => 영양정보 표시여부 '노출' 인 경우에만 저장
            nutritionQuantityPerOnce : (viewModel.get("nutritionDisplayYn") == 'Y' ? viewModel.get("nutritionQuantityPerOnce") : ''),

            // BOS 영양분석 단위 (총분량) => 영양정보 표시여부 '노출' 인 경우에만 저장
            nutritionQuantityTotal : (viewModel.get("nutritionDisplayYn") == 'Y' ? viewModel.get("nutritionQuantityTotal") : ''),

            // 영양성분 기타 => 영양정보 표시여부 '노출' 인 경우에만 저장
            nutritionEtc : (viewModel.get("nutritionDisplayYn") == 'Y' ? viewModel.get("nutritionEtc") : ''),

            // ERP 영양분석단위 (1회 분량) : API 에서 미조회 / ERP 미연동 품목인 경우 ''
            erpNutritionQtyPerOnce : (viewModel.get("erpNutritionApiObj.erpServingsize") ? viewModel.get("erpNutritionApiObj.erpServingsize") : ''),

            // ERP 영양분석단위 (총분량) : API 에서 미조회 / ERP 미연동 품목인 경우 ''
            erpNutritionQtyTotal : (viewModel.get("erpNutritionApiObj.erpServingContainer") ? viewModel.get("erpNutritionApiObj.erpServingContainer") : ''),

            // ERP 영양성분 기타 : API 에서 미조회 / ERP 미연동 품목인 경우 ''
            erpNutritionEtc : (viewModel.get("erpNutritionApiObj.erpNutritionEtc") ? viewModel.get("erpNutritionApiObj.erpNutritionEtc") : ''),

            // 상품 상세 기본 정보
            basicDescription : $('#basicDescription').data("kendoEditor").value(),

            // 상품 상세 주요 정보
            detaillDescription : $('#detaillDescription').data("kendoEditor").value(),

            // 메모
            etcDescription : viewModel.get('etcDescription'),

            // 상품정보 제공고시
            addItemSpecValueList : fnAddItemSpecValueList(),

            // 영양정보 상세항목 목록
            addItemNutritionDetailList : fnAddItemNutritionDetailList(),

            // 영양정보 상세항목 정렬 순서
            itemNutritionDetailOrderList : fnGetNutritionDetailOrder(),

            // 상품 인증정보 목록
            addItemCertificationList : fnAddItemCertificationList(),

            /* 배송 / 발주 정보 */
            // ERP 발주정보
            erpPoType : viewModel.get("erpPoType"),

            // ERP 발주가능여부 : ERP 연동 품목인 경우에만 전송
            erpCanPoYn : (viewModel.get("isErpItemLink") == true ? viewModel.get('erpCanPoYn') : null),

            // BOS 발주유형 : ERP 연동 품목인 경우에만 전송
            ilPoTypeId : (viewModel.get("isErpItemLink") == true ? viewModel.get('selectedBosPoType') : null),

            /*
             * 배송불가지역 Enum UndeliverableAreaTypes 참조 : 화면에서 체크한 값을 반대로 전송함
             */

            // 도서산간지역 (1권역) 배송 불가 여부 : true 인 경우 배송 가능
            islandShippingYn : viewModel.get('islandShippingYn'),

            // 제주지역 (2권역) 배송 불가 여부 : true 인 경우 배송 가능
            jejuShippingYn : viewModel.get('jejuShippingYn'),

            // 단종 여부
            extinctionYn : (viewModel.get('extinctionYn') == 'Y' ? true : false),

            // 출고처 목록
            addItemWarehouseList : fnAddItemWarehouseList(),

            // 상품 이미지 업로드 결과 목록
            itemImageUploadResultList : viewModel.get("itemImageUploadResultList"),

            // 상품 대표 이미지명 : 상품 이미지 영역에서 첫번째 이미지의 data-id ( 파일명 )
            representativeImageName : $("#itemImageContainer > .itemImage").attr('data-id'),

            // 상품 이미지 정렬 순서 목록 : 저장시 품목 이미지의 정렬 순서로 지정
            itemImageOrderList : fnGetItemImageOrder(),

            // 동영상 URL
            videoUrl : viewModel.get('videoUrl'),

            // 비디오 자동재생 유무 ( true : 자동재생 )
            videoAutoplayYn : viewModel.get('videoAutoplayYn'),

            // 렌탈료 / 월
            rentalFeePerMonth : viewModel.get("rentalFeePerMonth"),

            // 의무사용기간 / 월
            rentalDueMonth : viewModel.get("rentalDueMonth"),

            // 계약금
            rentalDeposit : viewModel.get("rentalDeposit"),

            // 설치비
            rentalInstallFee : viewModel.get("rentalInstallFee"),

            // 등록비
            rentalRegistFee : viewModel.get("rentalRegistFee"),

            // 총 렌탈료
            rentalTotalPrice : viewModel.get("rentalTotalPrice"),

            // 반품기간
            returnPeriod : viewModel.get('returnPeriod'),

        	// 발주 가능여부
            erpCanPoYn : viewModel.get('erpCanPoYn'),

            /* 승인관리자 정보 시작 */
			itemApprList							: viewModel.get("itemApprList"), 														//승인관리자 정보
			loadDateTime							: loadDateTime,														//페이지 Load 시간
			apprKindTp								: viewModel.get("apprKindTp"),
			itemStatusTp							: viewModel.get("itemStatusTp"),
			/* 승인관리자 정보 끝 */
        }

        if(viewModel.get("masterItemType") == "MASTER_ITEM_TYPE.RENTAL") {
        	addItemParam.priceApplyStartDate = fnGetToday();
        	// 렌탈상품 품목가격 : 월 렌탈료 * 개월수 -> 월 렌탈료로 변경
        	addItemParam.standardPrice = viewModel.get("rentalFeePerMonth");
        	addItemParam.recommendedPrice = viewModel.get("rentalFeePerMonth");
        }

        fnAjax({
            url : "/admin/item/master/register/addItem",
            params : addItemParam,
            contentType : 'application/json',
            isAction : 'insert',
            success : function(data) {

                if (viewModel.get("isErpItemLink") == false) { // ERP 미연동 품목 등록시, BOS 자체에서 품목 코드 생성 후 반환
                    viewModel.set('bosItemCode', data["ilItemCode"]); // 등록된 품목 코드 viewModel 에 세팅

                    fnKendoMessage({
                        message : '품목코드 ' + data["ilItemCode"] + ' 가 생성되었습니다.',
                        ok : function focusValue() {
                            var option = new Object();

                            option.url = "#/item";   // 마스터 품목 리스트 화면 URL
                            option.menuId = 721;     // 마스터 품목 리스트 메뉴 ID
                            option.data = {          // 마스터 품목 리스트 화면으로 전달할 Data
                                    ilItemCode : data["ilItemCode"]
                            };
                            fnGoPage(option);
                        }
                    });

                } else {

                    fnKendoMessage({
                        message : '품목코드 ' + viewModel.get('erpItemCode') + ' 가 생성되었습니다.',
                        ok : function focusValue() {
                            var option = new Object();

                            option.url = "#/item";   // 마스터 품목 리스트 화면 URL
                            option.menuId = 721;     // 마스터 품목 리스트 메뉴 ID
                            option.data = {          // 마스터 품목 리스트 화면으로 전달할 Data
                                    ilItemCode : viewModel.get('erpItemCode')
                            };
                            fnGoPage(option);
                        }
                    });

                }

            },
            error : function(xhr, status, strError) {
                fnKendoMessage({
                    message : xhr.responseText
                });
            },

        });

    }

    // 영양정보 정렬 순서 반환 : [ "영양정보 코드", .. ] 형식의 배열 반환, 배열의 index 가 정렬 순서
    function fnGetNutritionDetailOrder() {

        var nutritionCodeArray = [];

        if( viewModel.get('nutritionDisplayYn') == 'Y' ) {   // 영양정보 표시여부 '노출' 선택시에만 영양정보 정렬 순서 전송

            var nutritionDetailArray = $("#nutritionDetailContainer tr");

            for (var i = 0; i < nutritionDetailArray.length; i++) {
                var nutritionCode = $("#nutritionDetailContainer tr:nth-child(" + (i + 1) + ")").attr('data-id');
                nutritionCodeArray.push(nutritionCode);
            }

        }

        return nutritionCodeArray;

    }

    // 상품 이미지 정렬 순서 반환 : [ "파일명", .. ] 형식의 배열 반환, 배열의 index 가 정렬 순서
    function fnGetItemImageOrder() {

        var itemImageArray = $("#itemImageContainer .itemImage");

        var itemImageFileNameArray = [];

        for (var i = 0; i < itemImageArray.length; i++) {
            var filePath = $("#itemImageContainer .itemImage:nth-child(" + (i + 1) + ")").attr('data-id');
            itemImageFileNameArray.push(filePath);
        }

        return itemImageFileNameArray;

    }

    function fnGetOriginDetail() { // 원산지 Dropdown 에서 선택한 값에 따라 품목 등록시 저장할 원산지 상세 값 return

        switch( viewModel.get('bosOriginType') ) { // 원산지 Dropdown 에서 선택한 값

        case 'ORIGIN_TYPE.DOMESTIC' : // '국내' 선택시

            return '';  // 원산지 상세 값 저장하지 않음

        case 'ORIGIN_TYPE.OVERSEAS' : // '해외' 선택시

            // fnKendoDropDownList 함수로 생성한 dropdown 이 코드 값이 아닌 코드 객체를 주는 case 발생 : type 체크 로직 추가
            if( typeof viewModel.get('bosOriginDetailOverseas') == "string"  ) {

                return viewModel.get('bosOriginDetailOverseas');  // 원산지 상세 '해외' 원산지 Dropdown 의 값 저장

            } else {

                return viewModel.get('bosOriginDetailOverseas')['CODE'];

            }

        case 'ORIGIN_TYPE.ETC' : // '기타' 선택시

            return viewModel.get('bosOriginDetailDirectInputValue');  // 원산지 상세 '직접입력' Input 의 값 저장

        }

    }

    function fnAddItemSpecValueList() { // 저장할 상품정보 제공고시 목록 return

        var addItemSpecValueList = [];

        if (viewModel.get('itemSpecValueList') != null && viewModel.get('itemSpecValueList').length > 0) {

            for (var i = 0; i < viewModel.get('itemSpecValueList').length; i++) {

                var itemSpecValueInfo = viewModel.get('itemSpecValueList')[i];

                var directYn = null; // 상품정보 제공고시 직접 입력 여부 : 최초 기본값 null ( 직접 입력하지 않음  )

                // 직접입력 체크박스 Visible 상태 && 값 체크했을 경우에만 true 값 지정, 그 외의 경우에는 null 값 유지
                if( itemSpecValueInfo['showDirectInputCheckBox'] == true && itemSpecValueInfo['directInputChecked'] == true ) {
                    directYn = true;
                }

                addItemSpecValueList.push({
                    ilSpecFieldId : itemSpecValueInfo['ilSpecFieldId'], // 상품정보제공고시 상세 항목 코드
                    specFieldValue : itemSpecValueInfo['basicValue'], // 상품정보제공고시 상세 항목 정보
                    directYn : directYn // 상품정보제공고시 직접 입력 여부
                });

            }

        }

        return addItemSpecValueList;

    }

    function fnAddItemCertificationList() { // 저장할 상품 인증정보 목록 return

        var addItemCertificationList = [];

        if (viewModel.get('itemCertificationList') != null && viewModel.get('itemCertificationList').length > 0) {

            for (var i = 0; i < viewModel.get('itemCertificationList').length; i++) {

                var itemCertificationInfo = viewModel.get('itemCertificationList')[i]; // 화면상에 추가된 상품 인증정보
                var ilCertificationId = itemCertificationInfo['ilCertificationId']; // 상품 인증정보 ID

                // 해당 상품인증정보 ID 의 기본 인증 메시지
                var defaultCertificationDescription = viewModel.get('itemCertificationCodeMap')[ilCertificationId]['defaultCertificationDescription'];

                // 저장할 상품 인증정보 상세항목
                var certificationDescription = '';

                // 인증 메시지 : 화면상의  메시지가 기본 인증 메시지와 같은 경우 '', 같지 않은 경우 화면상의 메시지 저장
                if (itemCertificationInfo['certificationDescription'] != defaultCertificationDescription) {
                    certificationDescription = itemCertificationInfo['certificationDescription'];
                }

                addItemCertificationList.push({
                    ilCertificationId : itemCertificationInfo['ilCertificationId'],
                    certificationDescription : certificationDescription,
                });

            }

        }

        return addItemCertificationList;

    }

    function fnAddItemWarehouseList() { // 저장할 출고처 목록 return

        var itemWarehouseList = [];

        if (viewModel.get('itemWarehouseList') != null && viewModel.get('itemWarehouseList').length > 0) {

            viewModel.set('noItemWarehouseInfo', false);    // 출고처가 있는경우 출고처 빈영역 제거

            for (var i = 0; i < viewModel.get('itemWarehouseList').length; i++) {

                var warehouseInfo = viewModel.get('itemWarehouseList')[i];
                var preOrderYnFlag = false;
                var preOrderTpVal = "";
                var unlimitStockFlag = true;

                var ilPoTpIdVal = "";
                if(undefined == warehouseInfo['itemWarehousePoTpIdTemplateList']) {
                	ilPoTpIdVal = "";
                }else {
                	if(undefined == warehouseInfo['itemWarehousePoTpIdTemplateList'].poTypeCode) {
                    	ilPoTpIdVal = warehouseInfo['itemWarehousePoTpIdTemplateList'];
    				}else {
    					ilPoTpIdVal = warehouseInfo['itemWarehousePoTpIdTemplateList'].poTypeCode;
    				}
                }

                if(ilPoTpIdVal == "") { // 발주유형 없을경우
                    ilPoTpIdVal = null;
                }
                else { // 발주유형 있을 경우
                    // 선주문 여부 추가 - 20210105
                    if (undefined == warehouseInfo['itemWarehousePoTpIdTemplateList'].poTypeCode) { // 발주유형 변경이 없었으면 이전값 셋팅
                		preOrderYnFlag = warehouseInfo['preOrderYn'];
    					preOrderTpVal = warehouseInfo['preOrderTp'];
                    }
                    else {
                    	if (undefined != warehouseInfo['ilPoTpId']
                    			&& (warehouseInfo['itemWarehousePoTpIdTemplateList'].poTypeCode == warehouseInfo['ilPoTpId'])) { // 기존 변경건 중 새로 선택한 발주유형이 이전 발주유형값과 같을 경우(발주유형이 변경되지 않았음) 이전값 셋팅
		            		preOrderYnFlag = warehouseInfo['preOrderYn'];
							preOrderTpVal = warehouseInfo['preOrderTp'];
                    	}
                    	else { // 출고처 추가에 의한 발주유형 신규 또는 기존 출고처의 발주유형 변경시
                        	if(viewModel.get("bosSupplier") == "1") { //풀무원식품
                        		if (undefined != warehouseInfo.itemWarehousePoTpIdTemplateList
                        				&& undefined != warehouseInfo.itemWarehousePoTpIdTemplateList.erpPoTp
                        				&& (warehouseInfo.itemWarehousePoTpIdTemplateList.erpPoTp == 'ERP_PO_TP.PD1'
                        					|| warehouseInfo.itemWarehousePoTpIdTemplateList.erpPoTp == 'ERP_PO_TP.PD2'
                        					|| warehouseInfo.itemWarehousePoTpIdTemplateList.erpPoTp == 'ERP_PO_TP.YJ')) { // PD1, PD2, 양지의 경우 선주문 불가
                            		preOrderYnFlag = false;
                					preOrderTpVal = "PRE_ORDER_TP.LIMITED_ALLOWED";
                        		}
                        		else { // PD1, PD2, 양지 이외의 경우 선주문 가능
                            		preOrderYnFlag = true;
                					preOrderTpVal = "PRE_ORDER_TP.UNLIMITED_ALLOWED";
                        		}
                        	}
                        	else if(viewModel.get("bosSupplier") == "2") { // 올가홀푸드
                        		for( var k = viewModel.get('itemPoTypeCodeList').length - 1 ; k >= 0 ; k -- ) {
                            		// 사용자가 선택한 발주유형 코드의 발주일 설정값 품목별 상이 여부 값이 true 인 경우
                            		if( viewModel.get('itemPoTypeCodeList')[k]['poTypeCode'] == ilPoTpIdVal) {
                            			viewModel.set("erpPoTp", viewModel.get('itemPoTypeCodeList')[k]['erpPoTp']);
                            			break;
                            		}
                        		}
                        		// 공급처가 올가인 경우 발주유형이 R1 일 때 선주문 가능.
                        		if(viewModel.get("erpPoTp") == "ERP_PO_TP.R1" ) {
                        			if(viewModel.get('erpCategoryLevel1Id') != "채소" && viewModel.get('erpCategoryLevel1Id') != "") {
                        				preOrderYnFlag = true;

                        				if(viewModel.get('erpCategoryLevel1Id') == "과일") {
                        					preOrderTpVal = "PRE_ORDER_TP.UNLIMITED_ALLOWED";
                        				}else {
                        					preOrderTpVal = "PRE_ORDER_TP.LIMITED_ALLOWED";
                        				}
                        			}
                        			else {
                        				preOrderYnFlag = false;
                    					preOrderTpVal = "PRE_ORDER_TP.LIMITED_ALLOWED";
                        			}
                        		}
                        		else {
                    				preOrderYnFlag = false;
                					preOrderTpVal = "PRE_ORDER_TP.LIMITED_ALLOWED";
                        		}

                        	}
                        	else {
                        		preOrderYnFlag = false;
            					preOrderTpVal = "PRE_ORDER_TP.LIMITED_ALLOWED";
                        	}
                        }
                    }
                }

                itemWarehouseList.push({
                    urSupplierWarehouseId : warehouseInfo['urSupplierWarehouseId'], //  공급처-출고처 PK
                    preOrderYn : preOrderYnFlag, 									//  선주문 여부
                    preOrderTp : preOrderTpVal, 									//  선주문 유형코드
                    unlimitStockYn : unlimitStockFlag,								//  무제한 재고 여부
                    ilPoTpId  : ilPoTpIdVal,
                    erpPoType : viewModel.get("erpPoType")
                });

            }
        }

        return itemWarehouseList;

    }

    function fnAddItemNutritionDetailList() { // 저장할 상품 영양정보 상세항목 목록 return

        var itemNutritionDetailList = [];

        if( viewModel.get('nutritionDisplayYn') == 'Y' ) {  // 영양정보 표시여부 '노출' 선택시에만 영양정보 상세항목 목록 저장

            for (var i = 0; i < viewModel.get('itemNutritionDetailList').length; i++) {

                var itemNutritionDetailInfo = viewModel.get('itemNutritionDetailList')[i];

                // 품목코드는 백엔드 서비스단에서 세팅
                itemNutritionDetailList.push({
                    nutritionCode : itemNutritionDetailInfo['nutritionCode'], // 영양정보 분류코드 PK
                    nutritionCodeName : itemNutritionDetailInfo['nutritionCodeName'], // 영양정보 분류코드명
                    erpNutritionQuantity : itemNutritionDetailInfo['erpNutritionQuantity'], // ERP 영양성분량
                    erpNutritionPercent : itemNutritionDetailInfo['erpNutritionPercent'], // ERP 영양성분 기준치대비 함량
                    nutritionQuantity : itemNutritionDetailInfo['nutritionQuantity'], // BOS 영양성분량
                    nutritionPercent : itemNutritionDetailInfo['nutritionPercent'], // BOS 영양성분 기준치대비 함량
                    nutritionUnit : itemNutritionDetailInfo['nutritionUnit'], // 영양성분 단위
                });
            }

        }

        return itemNutritionDetailList;

    }

    /***************************************************************************
     * 팝업 호출
     ***************************************************************************/

    function fnErpItemSearchPopup() { // ERP 품목 검색 팝업 호출

        // 기존에 이미 조회된 ERP 품목 코드가 있는 경우
        if( viewModel.get('erpItemCode') && viewModel.get('erpItemCode').length != 0 ) {

            fnKendoMessage({
                type : 'confirm',
                message : '품목변경으로 품목정보 연관정보가 초기화 됩니다.<br>계속하시겠습니까?',
                ok : function() {
                    // 화면 전체 초기화 => fnClear 함수 내에서 다시 마스터 품목 유형 change Event 수동 호출
                    fnClear();
                    setTimeout(openErpItemSearchPopup(), 3000);

                },
                cancel : function() {
                    return;
                },
            });

        } else {
            openErpItemSearchPopup();

        }

    };

    // 템플릿 초기셋팅
    function initTemplate() {
        fnTagMkRadio({
            id: 'templateSpan',
            tagId: 'template',
            data: [
                {"CODE": "FOOD", "NAME": "풀무원"},
                {"CODE": "ORGA", "NAME": "올가"}
            ],
            chkVal: 'FOOD',
            change: function (e) {
                const value = e.target.value.trim();

                if(value == "FOOD")
                    $('#basicDescription').data("kendoEditor").value(foodTemp);
                else if(value == "ORGA")
                    $('#basicDescription').data("kendoEditor").value(orgaTemp);
            }
        });

        $("#dpBrand").change(function () {

            $("input:radio[name='template']:radio[value='FOOD']").prop('checked', true);
            $('#basicDescription').data("kendoEditor").value(foodTemp);
            $("input[name=template]").attr("disabled", false);

            if($(this).val() == '1' || $(this).val() == '14' || $(this).val() == '15') {
                $("input:radio[name='template']:radio[value='ORGA']").prop('checked', true);
                $('#basicDescription').data("kendoEditor").value(orgaTemp);
                $("input[name=template]").attr("disabled", true);
            }

        });
    }

    // 상품상세정보 에디터 템플릿 가져오기
    function getGoodsDetailInfoEditorTemplate() {

        initTemplate();

        let params = new Object();
        params.psGroupType = "7.GOODS_TEMP";

        fnAjax({
            url     : '/admin/system/shopconfig/getGroupShopConfigList',
            params  : params,
            async	: false,
            success :
                function( data ){
                    for(var i=0; i < data.rows.length; i++){
                        if(data.rows[i].psKey == "FOOD")
                            foodTemp = data.rows[i].comment;
                        else if(data.rows[i].psKey == "ORGA")
                            orgaTemp = data.rows[i].comment;
                    }

                    $('#basicDescription').data("kendoEditor").value(foodTemp); // 상품상세 기본정보
                },
            isAction : 'select'
        });
    }

    //  실제 ERP 품목 검색 팝업 호출
    function openErpItemSearchPopup() {

        fnKendoPopup({
            id : 'erpItemSearchPopup',
            title : 'ERP 품목 검색',
            src : '#/erpItemSearchPopup',
            width : '1200px',
            height : '850px',
            param : {
                masterItemType : viewModel.get("masterItemType")
            },
            success : function(id, data) {
                if(data['masterItem']) { // BOS 상에 등록된 마스터 품목 데이터 값이 넘어온 경우 : 마스터 품목 수정 화면으로 이동

                    var option = new Object();

                    option.url = "layout.html#/itemMgmModify";   // 마스터 품목 수정 화면 URL
                    option.menuId = 789;   // 마스터 품목 수정 화면 메뉴 ID
                    option.data = {        // 마스터 품목 수정 화면으로 전달할 Data
                        ilItemCode : data['masterItem']['ilItemCode'],
                        isErpItemLink : data['masterItem']['erpLinkIfYn'],
                        masterItemType : data['masterItem']['itemType']
                    };

                    fnGoPage(option);
                    return;

                }

                if(!data['erpItemNo']) { // ERP 품목코드 값이 없는 경우 return
                    return;
                }

                viewModel.set('isAllDisabled', false); // 화면 전체 잠금 해제

                // 선택한 ERP 연동 품목 정보를 viewModel 에 세팅
                fnSetErpItemLink(data);

                // 조회된 ERP 연동 품목 코드로 ERP 영양정보 API 호출
                fnGetErpNutritionApi(data['erpItemNo']);

                /* 상품 영양정보 Data 조회 */
                // ERP 연동 품목인 경우 IL_NUTRITION 테이블에 등록된 모든 코드 조회
                // 품목코드는 넘기지 않음 : IL_ITEM_NUTRITION 테이블의 데이터는 무시
                // erpLinkIfYn 값 true (ERP 연동) : BOS 상에 등록된 영양정보 세부항목도 조회하지 않음
                fnGetAddAvailableNutritionCodeList("", true); // 등록 가능한 모든 영양정보 분류코드 리스트 조회

                // 조회된 ERP 연동 품목 코드로 ERP 가격정보 API 호출
                fnGetErpPriceApi(data['erpItemNo']);

                /* 출고처 조회 : API 로 조회한 공급업체 값으로 조회 */
                var bosSupplierCode = data['bosSupplier'];
                var erpTaxInvoiceType = data['erpTaxInvoiceType'];      // 세금계산서 구분/
                var legalType = data['legalType'];                      // 법인코드

                // 건강생활이면서 세금계산서 구분값이 '시식/시음/기타'인 경우 공급업체 리스트 노출처리
                if( legalType == "PGS" && erpTaxInvoiceType == "시식/시음/기타"){
                    fnGetwarehouseUndefinedItem();
                } else {
                    fnGetItemWarehouseList(bosSupplierCode);
                }

                /* BOS 발주유형 코드 조회 : API 로 조회한 공급업체 값으로 조회  */
                fnAjax({
                    url : "/admin/item/master/register/getItemPoTypeCode",
                    method : 'GET',
                    params : {
                        urSupplierId : bosSupplierCode,
                        erpPoType : data['erpPoType']
                    },
                    success : function(data, status, xhr) {
                        if( data['rows'] && data['rows'].length > 0 ) {

                            viewModel.set('itemPoTypeCodeList', data['rows']);

//                            fnKendoDropDownList({
//                                id : 'bosPoType', // BOS 발주 유형
//                                data : data['itemPoTypeCodeList'],
//                                valueField : 'poTypeCode',
//                                textField : 'poTypeName',
//                                blank : '선택'
//
//                            });

                        }
                    }
                });

                // 데이터 마이그레이션 관련 로직 Start : 추후 삭제 예정
                fnShowMigrationDataButton();
                // 데이터 마이그레이션 관련 로직 End

            }
        });

    }

    function fnMasterItemSearchPopup() { // 마스터 품목 검색 팝업 호출

        // ERP 품목 연동여부 '연동 품목' 선택시에는 반드시 ERP 품목 검색 팝업에서 ERP 품목 코드 조회 후에 마스터 품목 검색 팝업 호출 가능
        // ERP 품목코드 erpItemCode 값은 수정 불가 : '' 아니면 조회된 값만 세팅 가능
        if (viewModel.get("erpItemLinkYn") == 'Y' && viewModel.get("erpItemCode") == '') {
            valueCheck('ERP 연동 품목은 먼저 ERP 품목 코드를 조회해야 합니다.', 'fnErpItemSearchPopup');
            return;
        }

        fnKendoPopup({
            id : 'fnMasterItemSearchPopup',
            title : '마스터 품목 검색',
            src : '#/masterItemSearchPopup',
            width : '1500px',
            height : '900px',
            param : {
                masterItemType : viewModel.get("masterItemType")
            },
            success : function(id, data) {

                if(!data['ilItemCode']) { // 품목코드 값이 없는 경우 return
                    return;
                }

                fnCopyMasterItemInfo(data); // 마스터 품목 조회

            }
        });
    };

    function fnPoTypeDetailInformationPopup() { // 발주 상세정보 팝업 호출

        fnKendoPopup({
            id : 'fnPoTypeDetailInformationPopup',
            title : '발주 상세정보',
            src : '#/poTypeDetailInformationPopup',
            width : '750px',
            height : '400px',
            param : {
                erpItemCode : viewModel.get("erpItemCode"),
                erpItemName : viewModel.get("erpItemName")
            }
        });

    }

    /***************************************************************************
     *  ERP API 호출 / 마스터 품목 정보 조회 및 복사
     ***************************************************************************/

    // 마스터 품목 검색 팝업에서 선택한 품목 코드로 마스터 품목 정보 조회 / 복사
    function fnCopyMasterItemInfo(data) {

        var ilItemCode = data['ilItemCode']; // 품목 코드
        var erpLinkIfYn = viewModel.get('isErpItemLink'); // 마스터 품목 등록 화면에서 선택한 ERP 연동 여부

        fnAjax({ // 마스터 품목 조회
            url : "/admin/item/master/register/getMasterItem",
            method : 'GET',
            params : {
                ilItemCode : ilItemCode,
                isErpItemLink : erpLinkIfYn
            },
            success : function(data, status, xhr) {

                if (erpLinkIfYn == true) { // ERP 품목 연동 여부 '연동' 선택 상태에서 마스터 품목 복사시

                    var copyData = data['getErpLinkMasterItemVo']; // 조회된 연동 품목 data

                    // 조회된 연동 품목 Data 복사
                    viewModel.set('itemName', copyData['itemName']); // 마스터 품목명
                    viewModel.set('bosCategoryStandardFirstId', copyData['bosCategoryStandardFirstId']); // 표준카테고리 대분류 ID
                    viewModel.set('bosCategoryStandardSecondId', copyData['bosCategoryStandardSecondId']); // 표준카테고리 소분류 ID
                    viewModel.set('bosCategoryStandardThirdId', copyData['bosCategoryStandardThirdId']); // 표준카테고리 중분류 ID
                    viewModel.set('bosCategoryStandardFourthId', copyData['bosCategoryStandardFourthId']); // 표준카테고리 세분류 ID
                    viewModel.set('bosOriginType', copyData['originType']); // BOS 원산지
                    viewModel.onBosOriginChange(null); // BOS 원산지 Change 이벤트 수동 호출

                    switch( copyData['originType'] ) { // 마스터품목 복사시 조회된  BOS 원산지 값

                    case 'ORIGIN_TYPE.DOMESTIC' : // '국내' : 로직 없음

                        break;

                    case 'ORIGIN_TYPE.OVERSEAS' : // '해외' 선택시

                        // 원산지 상세 '해외' 원산지 Dropdown 과 viewModel 에 복사한 원산지 상세 값 세팅
                        viewModel.set('bosOriginDetailOverseas', copyData['originDetail']);
                        break;

                    case 'ORIGIN_TYPE.ETC' : // '기타' 선택시

                        // 원산지 상세 '직접입력' input 과 viewModel 에 복사한 원산지 상세 값 세팅
                        viewModel.set('bosOriginDetailDirectInputValue', copyData['originDetail']);

                        break;

                    }
                    viewModel.set('sizePerPackage', copyData['sizePerPackage']); // 포장단위별 용량
                    viewModel.set('sizeUnit', copyData['sizeUnit']); // 용량(중량) 단위

                    if( copyData['sizeUnit'] == sizeUnitDirectInputCode ) {
                        viewModel.set('isSizeUnitDirectInputVisible', true); // 용량(중량) 단위 직접입력 Input Visible 처리
                        viewModel.set('sizeUnitDirectInputValue', copyData['sizeUnitEtc']); // 용량(중량) 단위 기타
                    }

                    viewModel.set('quantityPerPackage', copyData['quantityPerPackage']); // 포장 구성수량
                    viewModel.set('packageUnit', copyData['packageUnit']); // 포장 구성단위

                    if( copyData['packageUnit'] == packageUnitDirectInputCode ) {
                        viewModel.set('isPackageUnitDirectInputVisible', true); // 포장 구성단위 직접입력 Input Visible 처리
                        viewModel.set('packageUnitDirectInputValue', copyData['packageUnitEtc']); // 포장 구성단위 단위 기타
                    }

                    viewModel.set('nutritionDisplayYn', (copyData['nutritionDisplayYn'] == true ? 'Y' : 'N')); // 영양정보 표시여부
                    viewModel.onNutritionDisplayYnChange(null); // 영양정보 표시여부 change 이벤트 수동 호출

                    viewModel.set('videoUrl', copyData['videoUrl']); // 동영상 url
                    viewModel.set('videoAutoplayYn', copyData['videoAutoplayYn']); // 동영상 자동재생 여부

                    /* 상품상세 기본 / 주요정보 */
                    $('#basicDescription').data("kendoEditor").value(copyData['basicDescription']); // 상품상세 기본정보
                    $('#detaillDescription').data("kendoEditor").value(copyData['detaillDescription']); // 상품상세 주요정보

                    /* 상품정보 제공고시 분류 / 상세 항목 조회 : 원본 ERP 연동 품목의 품목코드로 상품정보 제공고시 상세 항목 복사 */
                    // 원본 ERP 연동 품목의 상품정보 제공고시 항목 코드를 같이 넘김
                    fnGetItemSpecList(ilItemCode, copyData['ilSpecMasterId']);

                    /* 상품 인증정보 조회 : 원본 ERP 연동 품목의 품목코드로 상품 인증정보 복사 */
                    fnGetItemCertificationList(ilItemCode);

                    /* 해당 품목코드로 등록된 품목 이미지 목록 조회 */
                    fnGetItemImageList(ilItemCode);

                    setTimeout(fnAddItemImageArea(), 500);

                } else { // ERP 품목 연동 여부 '미연동' 선택 상태에서 마스터 품목 복사시

                    var copyData = data['getErpNotLinkMasterItemVo']; // 조회된 미연동 품목 data

                    fnKendoDropDownList({ // 조회된 공급업체 ID 로 브랜드 리스트 호출
                        id : 'bosBrand',
                        url : "/admin/ur/brand/searchBrandList",
                        params : {
                            searchUrSupplierId : copyData['urSupplierId'],
                            searchUseYn : "Y"
                        },
                        textField : "brandName",
                        valueField : "urBrandId",
                        blank : '선택'
                    });

                    // 조회된 미연동 품목 Data 복사
                    viewModel.set('itemName', copyData['itemName']); // 마스터 품목명
                    viewModel.set('bosCategoryStandardFirstId', copyData['bosCategoryStandardFirstId']); // 표준카테고리 대분류 ID
                    viewModel.set('bosCategoryStandardSecondId', copyData['bosCategoryStandardSecondId']); // 표준카테고리 소분류 ID
                    viewModel.set('bosCategoryStandardThirdId', copyData['bosCategoryStandardThirdId']); // 표준카테고리 중분류 ID
                    viewModel.set('bosCategoryStandardFourthId', copyData['bosCategoryStandardFourthId']); // 표준카테고리 세분류 ID
                    viewModel.set('bosSupplier', copyData['urSupplierId']); // BOS 공급업체
                    // PDM 그룹코드 처리(공급업체가 PDM일 경우에 노출 및 활성화)
                    if( viewModel.get('bosSupplier') == pdmSupplierCode ) { // 공급업체가 PDM ( "풀무원 녹즙2" ) 인 경우
                        viewModel.set('isPdmGroupCodeVisible', true ); // PDM 그룹코드 Visible 처리
                        viewModel.set('isPdmGroupCodeDisabled', false ); // PDM 그룹코드 활성화
                    } else {
                        viewModel.set('isPdmGroupCodeVisible', false ); // PDM 그룹코드 Visible 해제
                        viewModel.set('isPdmGroupCodeDisabled', true ); // PDM 그룹코드 비활성화
                        viewModel.set('pdmGroupCode', '' ); // 기존 PDM 그룹코드 값 초기화
                    }

                    viewModel.set('bosBrand', copyData['urBrandId']); // BOS 브랜드
                    viewModel.set('dpBrand', copyData['dpBrandId']); // 전시 브랜드
                    viewModel.set('isBosBrandDisabled', false); // BOS 브랜드 수정 가능하도록 비활성화 해제
                    viewModel.set('bosItemGroup', copyData['bosItemGroup']); // BOS 상품군
                    viewModel.set('bosDistributionPeriod', copyData['distributionPeriod']); // BOS 유통기간
                    viewModel.set('unitOfMeasurement', copyData['unitOfMeasurement']); // UOM / OMS

                    // BOS 원산지 / 원산지 상세 값 복사
                    viewModel.set('bosOriginType', copyData['originType']); // BOS 원산지
                    viewModel.onBosOriginChange(null); // BOS 원산지 Change 이벤트 수동 호출

                    switch( copyData['originType'] ) { // 마스터품목 복사시 조회된  BOS 원산지 값

                    case 'ORIGIN_TYPE.DOMESTIC' : // '국내' : 로직 없음

                        break;

                    case 'ORIGIN_TYPE.OVERSEAS' : // '해외' 선택시

                        // 원산지 상세 '해외' 원산지 Dropdown 과 viewModel 에 복사한 원산지 상세 값 세팅
                        viewModel.set('bosOriginDetailOverseas', copyData['originDetail']);
                        break;

                    case 'ORIGIN_TYPE.ETC' : // '기타' 선택시

                        // 원산지 상세 '직접입력' input 과 viewModel 에 복사한 원산지 상세 값 세팅
                        viewModel.set('bosOriginDetailDirectInputValue', copyData['originDetail']);

                        break;

                    }

                    // BOS 원산지 상세 값 별도 세팅
                    viewModel.set('bosOriginDetail', copyData['originDetail']);

                    viewModel.set('bosStorageMethod', copyData['storageMethodType']); // BOS 보관방법
                    viewModel.set('boxWidth', copyData['boxWidth']); // BOS 박스 가로
                    viewModel.set('boxDepth', copyData['boxDepth']); // BOS 박스 세로
                    viewModel.set('boxHeight', copyData['boxHeight']); // BOS 박스 높이
                    viewModel.set('bosPiecesPerBox', copyData['piecesPerBox']); // BOS 박스 입수량
                    viewModel.set('sizePerPackage', copyData['sizePerPackage']); // 포장단위별 용량
                    viewModel.set('sizeUnit', copyData['sizeUnit']); // 용량단위

                    if( copyData['sizeUnit'] == sizeUnitDirectInputCode ) {
                        viewModel.set('isSizeUnitDirectInputVisible', true); // 용량(중량) 단위 직접입력 Input Visible 처리
                        viewModel.set('sizeUnitDirectInputValue', copyData['sizeUnitEtc']); // 용량(중량) 단위 기타
                    }

                    viewModel.set('quantityPerPackage', copyData['quantityPerPackage']); // 포장 구성 수량
                    viewModel.set('packageUnit', copyData['packageUnit']); // 포장 구성 단위

                    if( copyData['packageUnit'] == packageUnitDirectInputCode ) {
                        viewModel.set('isPackageUnitDirectInputVisible', true); // 포장 구성단위 직접입력 Input Visible 처리
                        viewModel.set('packageUnitDirectInputValue', copyData['packageUnitEtc']); // 포장 구성단위 단위 기타
                    }

                    viewModel.set('taxYn', copyData['taxYn']); // BOS 과세구분

                    /* 상품 영양정보 */
                    viewModel.set('nutritionDisplayYn', (copyData['nutritionDisplayYn'] == true ? 'Y' : 'N')); // 영양정보 표시여부
                    viewModel.onNutritionDisplayYnChange(null); // 영양정보 표시여부 change 이벤트 수동 호출

                    if (copyData['nutritionDisplayDefalut']) { // 영양정보 기본 정보 조회시
                        viewModel.set('nutritionDisplayDefalut', copyData['nutritionDisplayDefalut']); // 영양정보 기본정보 ( NUTRITION_DISP_Y 값이 N 일때 노출되는 항목 )
                    }
                    viewModel.set('nutritionQuantityPerOnce', copyData['nutritionQuantityPerOnce']); // 영양분석 단위 (1회 분량)
                    viewModel.set('nutritionQuantityTotal', copyData['nutritionQuantityTotal']); // 영양분석 단위 (총분량)
                    viewModel.set('nutritionEtc', copyData['nutritionEtc']); // 영양성분 기타

                    viewModel.set('videoUrl', copyData['videoUrl']); // 동영상 url
                    viewModel.set('videoAutoplayYn', copyData['videoAutoplayYn']); // 동영상 자동재생 여부

                    viewModel.set('erpNutritionApiObj', {}); // ERP 미연동 품목이므로, 기조회된 ERP 영양정보 데이터 초기화

                    /* 기출력된 ERP 영양분석 단위 메시지 초기화 */
                    viewModel.set('erpNutritionEtc', ''); // ERP 영양성분 기타 : 초기화
                    viewModel.set('erpNutritionAnalysisMessage', ''); // ERP 영양분석 단위 메시지 : 초기화
                    viewModel.set('showErpNutritionAnalysisMessage', false); // visible 처리 해제

                    /* 기출력된 영양정보 상세항목 tr 모두 삭제 , viewModel Data 초기화 */
                    viewModel.set('itemNutritionDetailList', []);

                    /* 출고처 : 미연동 품목 Data 복사시 가져온 BOS 공급업체 PK 값으로 조회 */
                    fnGetItemWarehouseList(viewModel.get('bosSupplier'));

                    /* 상품상세 기본 / 주요정보 */
                    $('#basicDescription').data("kendoEditor").value(copyData['basicDescription']); // 상품상세 기본정보
                    $('#detaillDescription').data("kendoEditor").value(copyData['detaillDescription']); // 상품상세 주요정보

                    /* 상품정보 제공고시 분류 / 상세 항목 조회 : 원본 ERP 미연동 품목의 품목코드로 상품정보 제공고시 상세 항목 복사 */
                    // 원본 ERP 미연동 품목의 상품정보 제공고시 항목 코드를 같이 넘김
                    fnGetItemSpecList(ilItemCode, copyData['ilSpecMasterId']);

                    /* 상품 인증정보 조회 : 원본 ERP 미연동 품목의 품목코드로 상품 인증정보 복사 */
                    fnGetItemCertificationList(ilItemCode);

                    /* 영양정보 상세 항목 복사 위한 데이터 조회 */
                    // ERP 미연동 품목인 경우 복사 원본 품목 코드로 등록 가능한 IL_NUTRITION 테이블의 코드 조회 후 복사함
                    // 품목 코드 넘김 : 복사 원본 품목 코드로 기등록된 IL_ITEM_NUTRITION 테이블의 데이터도 같이 조회 후 복사함
                    // erpLinkIfYn 값 false : 해당 품목 코드로 BOS 상에 등록된 영양정보 세부항목을 같이 조회 후 반환
                    fnGetAddAvailableNutritionCodeList(ilItemCode, erpLinkIfYn); // 해당 품목코드로 등록 가능한 영양정보 분류코드 리스트 조회

                    /* 해당 품목코드로 등록된 품목 이미지 목록 조회 */
                    fnGetItemImageList(ilItemCode);

                    // 미연동 품목 정보 복사 후 세팅된 표준 카테고리 PK 와 보관방법으로 반품 가능기간 조회 / 출력
                    viewModel.onCalculateReturnPeriod(null);

                    setTimeout(fnAddItemImageArea(), 500);

                }

            },
            isAction : 'select'

        });

    }

    // ( ERP 연동 품목 ) ERP 품목 검색 팝업에서 선택한 ERP 연동 품목 Data 를 viewModel 에 세팅
    function fnSetErpItemLink(data) {

        viewModel.set('erpItemLinkYn', 'Y'); // ERP 연동 여부 : 'Y' 세팅
        viewModel.onErpItemLinkYnChange(null); // ERP 품목 연동/미연동 여부 flag 세팅

        // ERP 품목 검색 팝업에서 공급업체 미분류 품목은 조회 불가 => 공급업체 ID 는 항상 조회됨
        fnKendoDropDownList({ // 조회된 공급업체 ID 로 브랜드 리스트 호출
            id : 'bosBrand',
            url : "/admin/ur/brand/searchBrandList",
            params : {
                searchUrSupplierId : data['bosSupplier'],  // 공급업체 ID
                searchUseYn : "Y"
            },
            textField : "brandName",
            valueField : "urBrandId",
            blank : '선택'
        });

        viewModel.set('isSupplierDisabled', true); // 공급업체 Dropdown 비활성화
        viewModel.set('isBosBrandDisabled', false); // BOS 브랜드 활성화

        // PDM 그룹코드 관련
        if( data['bosSupplier'] == pdmSupplierCode ) { // 공급업체가 PDM ( "풀무원 녹즙2" ) 인 경우

            viewModel.set('isPdmGroupCodeVisible', true ); // PDM 그룹코드 Visible 처리
            viewModel.set('isPdmGroupCodeDisabled', false ); // PDM 그룹코드 비활성화 : 브랜드 '잇슬림' 선택시 활성화, '베이비밀' 선택시 비활성화

        } else {

            viewModel.set('isPdmGroupCodeVisible', false ); // PDM 그룹코드 Visible 해제
            viewModel.set('isPdmGroupCodeDisabled', true ); // PDM 그룹코드 비활성화
            viewModel.set('pdmGroupCode', '' ); // 기존 PDM 그룹코드 값 초기화

        }

        viewModel.set('legalType', data['legalType']); // ERP 법인코드
        viewModel.set('organizationType', data['organizationType']); // ERP 온라인 통합몰 구분값
        viewModel.set('erpCategoryLevel1Id', data['erpCategoryLevel1Id']); // ERP 대카테고리
        viewModel.set('erpCategoryLevel2Id', data['erpCategoryLevel2Id']); // ERP 중카테고리
        viewModel.set('erpCategoryLevel3Id', data['erpCategoryLevel3Id']); // ERP 소카테고리
        viewModel.set('erpCategoryLevel4Id', data['erpCategoryLevel4Id']); // ERP 세카테고리
        viewModel.set('erpItemName', data['erpItemName']); // ERP 품목명
        viewModel.set('itemName', data['erpItemName']); // 마스터 품목명
        viewModel.set('erpItemCode', data['erpItemNo']); // ERP 품목코드
        viewModel.set('erpItemBarCode', data['erpItemBarcode']); // ERP 품목바코드
        viewModel.set('bosItemBarCode', data['erpItemBarcode']); // ERP 연동시 ERP 품목바코드 -> BOS 바코드 대입.
        if(! data['erpSupplierName']){
            viewModel.set('erpSupplierName', '정보없음'); // ERP 에서의 생산처
        } else {
            viewModel.set('erpSupplierName', data['erpSupplierName']); // ERP 에서의 생산처
        }


        viewModel.set('bosSupplier', data['bosSupplier']); // BOS 공급업체
        viewModel.set('erpTaxInvoiceType', data['erpTaxInvoiceType']); // 세금계산서 발행구분
        viewModel.set('erpTaxType', data['erpTaxType']); // ERP 과세구분 (과세[영업사용],서울-면세일반,본점-과세일반,면세[영업사용],NULL)
        viewModel.set('erpBrand', data['erpBrandName']); // ERP 브랜드
        viewModel.set('erpItemGroup', data['erpItemGroup']); // ERP 상품군

        viewModel.set("erpItemWidth", data['erpItemWidth']);	// ERP 품목 가로
        viewModel.set("erpItemDepth", data['erpItemDepth']);	// ERP 품목 세로
        viewModel.set("erpItemHeight", data['erpItemHeight']);	// ERP 품목 높이

        // ERP품목별 수집정보가 없을 경우 정보없음으로 일괄 표시
        if( ! data['erpItemGroup'] ) {
            viewModel.set('erpItemGroupDisplayName', '정보없음'); // ERP 상품군 출력명 '정보없음' 으로 출력
        } else {
            viewModel.set('erpItemGroupDisplayName', data['erpItemGroup']); // ERP 상품군 출력
        }

        // ERP 온도구분 (상온, 실온, 냉장, 냉동…) => 보관방법??
        viewModel.set('erpStorageMethod', data['erpStorageMethod']);
        viewModel.set('erpOriginName', data['erpOriginName']); // ERP 원산지
        viewModel.set('erpOriginDetailName', data['erpOriginDetailName']); // ERP 원산지 상세 (해외일 경우에만)

        // ERP 온도구분에 매칭되는 BOS 보관방법 코드 선택
        viewModel.set('bosStorageMethod', data['bosStorageTypeCode']);

        viewModel.set('erpDistributionPeriod', data['erpDistributionPeriod']); // ERP 유통기간
        viewModel.set('bosDistributionPeriod', data['erpDistributionPeriod']); // BOS 유통기간
        viewModel.set('erpBoxWidth', data['erpBoxWidth']); // ERP 박스_가로 (소수점 1자리)
        viewModel.set('erpBoxDepth', data['erpBoxDepth']); // ERP 박스_세로 (소수점 1자리)
        viewModel.set('erpBoxHeight', data['erpBoxHeight']); // ERP 박스_높이 (소수점 1자리)
        viewModel.set('erpPiecesPerBox', data['erpPiecesPerBox']); // ERP 박스 입수량

        if(data['erpBoxWidth'] != null) {
        	viewModel.set('boxWidth', data['erpBoxWidth']); // BOS 박스_가로 (소수점 1자리)
        }
        if(data['erpBoxDepth'] != null) {
        	viewModel.set('boxDepth', data['erpBoxDepth']); // BOS 박스_세로 (소수점 1자리)
        }
        if(data['erpBoxHeight'] != null) {
        	viewModel.set('boxHeight', data['erpBoxHeight']); // BOS 박스_높이 (소수점 1자리)
        }

        viewModel.set('bosPiecesPerBox', data['erpPiecesPerBox']); // BOS 박스 입수량
        viewModel.set('unitOfMeasurement', data['erpUnitOfMeasurement']); // ERP UOM ( 측정단위 )
        viewModel.set('taxYn', (data['hasTax'] == true ? 'Y' : 'N')); // ERP 과세구분 (과세/면세)
        viewModel.set('erpPoType', data['erpPoType']); // ERP API 로 조회한 연동 품목 데이터의 발주유형

        // ERP API 로 조회한 연동 품목 데이터의 발주유형 출력명 : 발주유형 미조회시 '없음' 출력
        viewModel.set('erpPoTypeDisplayName', ( data['erpPoType'] ? data['erpPoType'] : '없음' ) );
        viewModel.set('erpCanPoYn', data['erpCanPoYn']); // ERP API 로 조회한 ERP 연동 품목 데이터에서 ERP 발주가능여부

        viewModel.set('hasErpCanPoYn', false);  // 올가 ERP 발주가능여부 메시지 : 최초 Visible 처리 해제

        if( data['legalType'] == orgaLegalTypeCode ) { // 올가 ERP 인 경우 : 매장품목유형, 발주가능여부 표시

            // 매장품목유형
            viewModel.set('o2oExposureType', data['o2oExposureType']);
            viewModel.set('o2oExposureTypeName', data['o2oExposureTypeName']);

            viewModel.set('hasO2oExposureType', true);  // 매장품목유형 Visible 처리

			// 공급업체가 올가홀푸드인 ERP 연동상품의 경우, 품목유형이 공통/매장전용이면 매장별 재고정보 Visible 처리 S
            if (viewModel.get('masterItemType') == 'MASTER_ITEM_TYPE.COMMON' || viewModel.get('masterItemType') == 'MASTER_ITEM_TYPE.SHOP_ONLY')
            	viewModel.set('visibleStoreStockInfo', true);
			// 공급업체가 올가홀푸드인 ERP 연동상품의 경우, 품목유형이 공통/매장전용이면 매장별 재고정보 Visible 처리 E

            // 발주가능여부
            if (data['erpCanPoYn'] == true) { // 올가 ERP 발주가능여부 출력 메시지 세팅
                viewModel.set('erpCanPoYnMessage', '예'); // ERP API 로 조회한 ERP 연동 품목 데이터에서 ERP 발주가능여부
            } else {
                viewModel.set('erpCanPoYnMessage', '아니오'); // ERP API 로 조회한 ERP 연동 품목 데이터에서 ERP 발주가능여부
            }

            viewModel.set('hasErpCanPoYn', true);  // 발주가능여부 Visible 처리

        }

        if( data['legalType'] == lohasLegalTypeCode ) { // 건강생활 ERP 인 경우 : 상품판매유형 표시

            viewModel.set('productType', data['productType']);
            viewModel.set('hasProductType', true);

        }

    }

    // ( ERP 연동 품목 ) ERP 품목 검색 팝업에서 선택한 ERP 연동 품목 코드로 ERP 영양정보 API 호출
    function fnGetErpNutritionApi(erpItemCode) {

        if (viewModel.get('isErpItemLink') == true) { // ERP 연동 품목 여부 체크

            fnAjax({
                url : "/admin/item/master/register/getErpNutritionApi",
                method : 'GET',
                params : {
                    ilItemCode : erpItemCode
                },
                async : false,
                isAction : 'select',
                success : function(data) { // 영양정보 1건 조회시

                    viewModel.set('erpNutritionApiObj', data); // 조회된 ERP 연동 품목의 영양정보 Data 를 전역변수로 세팅

                    // 화면상에 출력할 ERP 영양분석단위 메시지 생성 / viewModel 에 세팅
                    fnGenerateErpNutritionAnalysisMessage(data);

                    // API 로 조회된 기타 정보 viewModel 에 세팅
                    viewModel.set('erpNutritionEtc', (data['erpNutritionEtc'] ? data['erpNutritionEtc'] : ''));

                },
                error : function(xhr, status, strError) {  // 영양정보 미조회 or 2건 이상 조회시

                    viewModel.set('erpNutritionApiObj', {}); // 조회된 영양정보 Data 없음 : 빈 객체 세팅
                    fnGenerateErpNutritionAnalysisMessage({}); // ERP 영양분석단위 메시지 : 모두 '' 으로 세팅
                    viewModel.set('erpNutritionEtc', ''); // 기타 정보 : '' 으로 세팅

                },
            });

        }

    }

    // ( ERP 연동 품목 ) 가격정보 API 로 해당 ERP 연동 품목의 가격정보 조회
    function fnGetErpPriceApi(erpItemCode) {

        if (viewModel.get('isErpItemLink') == true) { // ERP 연동 품목 여부 체크

            fnAjax({
                url : "/admin/item/master/register/getErpPriceApi",
                method : 'GET',
                params : {
                    ilItemCode : erpItemCode,
                    erpSalesType : '정상'
                },
                isAction : 'select',
                success : function(data) { // 가격정보 조회시

                	viewModel.set('showItemPrice', true); // 가격정보 행 Vidible 처리
                	viewModel.set( 'showItemPriceAddBtn' , true );
//                    viewModel.set('showItemPriceRow', true); // 가격정보 행 Vidible 처리
                    viewModel.set('noItemPriceInfo', false); // 가격정보 없을 때 (최초) invisible 처리

                    if(data['erpIfPriceSearchResponseDtoList'] && data['erpIfPriceSearchResponseDtoList'].length > 0 ) {

                        // 올가 ERP : 가격적용 시작일로 오름차순 정렬된 여러 건 조회 => 첫번째 행인 현재 적용중인 가격 정보를 사용
                        // 기타 ERP : 단건 조회
                        var erpPriceApiObj = data['erpIfPriceSearchResponseDtoList'][0];

                        var today = fnGetToday();  // 현재 일자 ( yyyy-MM-dd 포맷 )

                        // 가격적용 시작일 : null 가능
                        var priceApplyStartDate = ( erpPriceApiObj['erpPriceApplyStartDate'] ? fnFormatDate( erpPriceApiObj['erpPriceApplyStartDate'], 'yyyy-MM-dd') : null );

                        // 정보 수정 일시
                        var updateDate = fnFormatDate( erpPriceApiObj['updateDate'], 'yyyy-MM-dd');

                        // CASE 1 : ERP API 에서 가격 정보 시작일 미조회시 => updateDate ( 정보 업데이트 일시 ) 를 가격적용 시작일로 사용
                        if( ! erpPriceApiObj['erpPriceApplyStartDate'] ) {

                            viewModel.set('erpPriceApplyStartDate', updateDate);

                        // CASE 2 : 가격 정보 시작일이 미래인 경우 => 등록시점의 현재 날짜를 시작일로 사용
                        } else if ( today < priceApplyStartDate  ) {

                            viewModel.set('erpPriceApplyStartDate', today);  // 현재 일자로 세팅

                        // CASE 3 : 가격 정보 시작일이 과거 또는 현재 일자인 경우 => 조회된 가격 정보 시작일을 사용
                        } else if ( today >= priceApplyStartDate ) {

                                viewModel.set('erpPriceApplyStartDate', priceApplyStartDate);

                        }

                    	if( viewModel.get('legalType') == foodLegalTypeCode ) {  // 풀무원식품
                    		viewModel.set( 'showItemPriceAddBtn' , false );
                    		viewModel.set("approvalTarget" , "미승인");
                    	} else if( viewModel.get('legalType') == orgaLegalTypeCode ) {  // 올가 ERP
                    		viewModel.set( 'showItemPriceAddBtn' , false );
                    		viewModel.set("approvalTarget" , "미승인");
                    	} else if( viewModel.get('legalType') == foodmerceLegalTypeCode ) {  // 푸드머스 ERP
                    		viewModel.set( 'showItemPriceAddBtn' , true );
                    		viewModel.set("approvalTarget" , "승인");
                    	} else if( viewModel.get('legalType') == lohasLegalTypeCode ) {  // 건강생활 ERP
                    		if( viewModel.get('productType') == '제품' ) {
                        		viewModel.set("approvalTarget" , "승인");
                    			viewModel.set( 'showItemPriceAddBtn' , true );
                    		} else if( viewModel.get('productType') == '상품' ) {
                        		viewModel.set("approvalTarget" , "승인");
                    			viewModel.set( 'showItemPriceAddBtn' , true );
                    		} else {
                        		viewModel.set( 'showItemPriceAddBtn' , false );
                        		viewModel.set("approvalTarget" , "품목등록 불가");
                    		}
                    	} else { // 기타 ERP 연동 품목인 경우
                    		viewModel.set( 'showItemPriceAddBtn' , false );
                    		viewModel.set("approvalTarget" , "품목등록 불가");
                    	}

                        // ERP 가격정보 API 에서 조회된 원가 / 정상가 viewModel 에 세팅 : 미조회시 '' 세팅
                        viewModel.set('erpStandardPrice', ( erpPriceApiObj['erpStandardPrice'] ? erpPriceApiObj['erpStandardPrice'] : ( erpPriceApiObj['erpStandardPrice'] == 0 ? 0 : '') ) );
                        viewModel.set('erpRecommendedPrice', ( erpPriceApiObj['erpRecommendedPrice'] ? erpPriceApiObj['erpRecommendedPrice'] : '' ) );

//                        if( viewModel.get('legalType') == foodmerceLegalTypeCode ) { // 푸드머스 ERP
//                        	viewModel.set("approvalTarget", "승인");
//                            viewModel.set( 'showFoodmerceErpItemPrice' , true );   // 푸드머스 ERP 가격정보 행 출력
//
//                        } else if( viewModel.get('legalType') == lohasLegalTypeCode ) {  // 건강생활 ERP
//                        	viewModel.set("approvalTarget", "승인");
//                            if( viewModel.get('productType') == '제품' ) {
//
//                                viewModel.set( 'showLohasProductsErpItemPrice' , true );   // 건강생활 ERP 중 제품 가격정보 행 출력
//
//                            } else if( viewModel.get('productType') == '상품' ) {
//
//                                viewModel.set( 'showLohasGoodsErpItemPrice' , true );   // 건강생활 ERP 중 상품 가격정보 행 출력
//
//                            }
//
//                        } else { // 기타 ERP 연동 품목인 경우
//                        	viewModel.set("approvalTarget", "미승인");
//                            viewModel.set( 'showErpLinkItemPrice' , true );   // 기타 ERP 연동 품목 가격정보 행 출력
//
//                        }

                        if(viewModel.get("showItemPriceAddBtn") == false) {
                        	viewModel.set("showItemPriceRow", true);
                        	viewModel.set("approvalStatusCode", "APPR_STAT.NONE");
                    		viewModel.set("approvalStatusCodeName", "승인대기");
                    		viewModel.set("priceApplyStartDate", viewModel.get("erpPriceApplyStartDate"));
                    		viewModel.set("priceApplyEndDate", priceEndApplyDate);
                    		viewModel.set("standardPrice", ( erpPriceApiObj['erpStandardPrice'] ? erpPriceApiObj['erpStandardPrice'] : ( erpPriceApiObj['erpStandardPrice'] == 0 ? 0 : '') ));
                    		viewModel.set("recommendedPrice", ( erpPriceApiObj['erpRecommendedPrice'] ? erpPriceApiObj['erpRecommendedPrice'] : '' ));

                    		var standardPrice = viewModel.get("standardPrice");
                    		var recommendedPrice = viewModel.get("recommendedPrice");
                    		var priceRatio = 0;
                    		var taxYn = viewModel.get("isErpItemLink") == true ? viewModel.get("erpTaxYn") : viewModel.get("taxYn");
                            var ratioCal = 0;
                            if(taxYn == true) {
                        		ratioCal = 1.1;
                        	}else {
                        		ratioCal = 1.0;
                        	}
                    		if(standardPrice != undefined && standardPrice != ""  && recommendedPrice != undefined && recommendedPrice != "") {
                    			priceRatio = Math.floor( (recommendedPrice - (standardPrice * ratioCal)) / recommendedPrice * 100);
                            }


                    		viewModel.set("priceRatio", priceRatio + "%");
                    		viewModel.set("approval1st", '');
                    		viewModel.set("approvalConfirm", '');

                        }



                    }

                },
                error : function(xhr, status, strError) { // 가격정보 미조회시

                    viewModel.set('erpPriceApplyStartDate', '');
                    viewModel.set('erpStandardPrice', '');
                    viewModel.set('erpRecommendedPrice', '');

                },
            });

        }

    }

    /***************************************************************************
     * 품목 정보 조회 function : 상품정보 제공고시 분류 / 세부 항목 조회
     ***************************************************************************/

    // 상품정보 제공고시 분류 / 세부 항목 조회
    // 최초 화면 조회시에는 상품정보 제공고시 상세 항목 전체 조회 => ilItemCode, ilSpecMasterId 모두 null
    // ilItemCode 는 마스터 품목 복사시에만 인자로 전달됨
    // ilSpecMasterId 는 마스터 품목 복사 후 해당 상품정보 제공고시 화면상에서 선택시 사용함
    function fnGetItemSpecList(ilItemCode, ilSpecMasterId) {

        fnAjax({
            url : "/admin/item/master/register/getItemSpecList",
            method : 'GET',
            params : {
                ilItemCode : ilItemCode
                , ilItemApprId : null
            },
            success : function(data, status, xhr) {

                // 품목별 상품정보제공고시 세부 항목 조회 목록 존재시 : 해당 데이터는 마스터 품목 복사시에만 조회됨
                var itemSpecValueMap = {};

                if (data['itemSpecValueList'] && data['itemSpecValueList'].length > 0) {

                    data['itemSpecValueList'].forEach(function(item, index) {
                        itemSpecValueMap[item['ilSpecFieldId']] = item;
                    });

                    // viewModel 에 품목별 상품정보제공고시 세부 항목 Setting
                    viewModel.set('itemSpecValueMap', itemSpecValueMap);

                }

                // 상품정보 제공고시 분류 / 항목 조회 목록 : 항상 조회됨
                var itemSpecMap = {};

                if (data['itemSpecList'] && data['itemSpecList'].length > 0) {

                    data['itemSpecList'].forEach(function(item, index) {

                        // 상품정보제공고시 항목 코드 존재 && 해당 항목 수정 가능한 경우 : '직접입력' 체크박스 Visible 처리, 입력 가능 처리
                        if (item['specFieldCode'] && item['canModified'] == true) {
                            item['showDirectInputCheckBox'] = true;
                            item['isValueDisabled'] = true;

                        // 상품정보제공고시 항목 코드 존재 && 해당 항목 수정 불가능한 경우 : '직접입력' 체크박스 Visible 처리, 입력 비활성화 처리
                        } else if(item['specFieldCode'] && item['canModified'] == false) {
                            item['showDirectInputCheckBox'] = false;
                            item['isValueDisabled'] = true;

                        } else {
                            item['showDirectInputCheckBox'] = false;
                            item['isValueDisabled'] = false;
                        }

                        if (item['specDescription']) { // 상품정보제공고시 부가설명 존재시 : specDescriptionPrefix 를 앞에 붙임
                            item['specDescription'] = specDescriptionPrefix + item['specDescription'];
                        }

                        item['directInputChecked'] = false; // '직접입력' 체크박스 : 최초 값은 미체크 상태

                        // ERP 미연동 품목 && 수정 가능한 항목인 경우
                        if( viewModel.get('isErpItemLink') == false && item['canModified'] == true ) {
                            item['directInputChecked'] = true; // '직접입력' 체크 상태로 최초 노출
                            item['isValueDisabled'] = false;  // 상품정보제공고시 입력 Input 활성화
                        }

                        if (!item['basicValue']) { // 상세항목의 기본값 미지정시 viewModel 의 specDefalutValue 의 값으로 세팅
                            item['basicValue'] = viewModel.get('specDefalutValue');
                        }

                        if (item['detailMessage']) { // 상세항목의 코드별 세부 메시지 존재시 해당 항목의 값으로 세팅
                            item['basicValue'] = item['detailMessage'];
                        }

                        // 상품정보 제공고시 상품군 Select 값 변경시 필터링이 편리하도록 Map ( Javascript Object ) 으로 변환
                        if (!itemSpecMap[item['ilspecMasterId']]) {

                            // Map 에 추가되지 않은 상품정보제공고시 분류 코드 인 경우 : ( 상품정보제공고시 분류 코드 : 빈 배열 ) 생성 후 추가
                            itemSpecMap[item['ilspecMasterId']] = [];
                            itemSpecMap[item['ilspecMasterId']].push(item);

                        } else {

                            // 이미 추가된 상품정보제공고시 분류 코드 : 해당 배열에 분류 상세항목 추가
                            itemSpecMap[item['ilspecMasterId']].push(item);

                        }

                    });

                    // 해당 품목코드로 조회된 품목별 상품정보 제공고시 세부 항목 존재시 : 마스터 품목 복사시에만 조회됨
                    if ( checkDataEmpty(itemSpecValueMap) == false && ilSpecMasterId != null ) {

                        // itemSpecMap[ilSpecMasterId] : 마스터 품목으로 복사한 원본 품목의 상품정보 제공고시 상품군에 해당하는 코드 목록
                        // => 최종적으로 원본 품목의 상품정보 제공고시 상세 항목의 값을 복사하여 화면에 출력함
                        itemSpecMap[ilSpecMasterId].forEach(function(item, index) {

                            var itemSpecValueObj = viewModel.get('itemSpecValueMap')[item['ilSpecFieldId']];

                            // 원본 품목의 해당 상품정보 제공고시 상세 항목의 직접 입력 여부가 true 인 경우
                            if( itemSpecValueObj && itemSpecValueObj['directYn'] && itemSpecValueObj['directYn'] == true ) {

                                // '직접입력' 체크박스 Visible 처리 / 체크 상태로 지정
                                item['showDirectInputCheckBox'] = true;
                                item['directInputChecked'] = true;

                                // 상품정보제공고시 입력 Input 활성화
                                item['isValueDisabled'] = false;

                            }

                            // 품목별 상품정보 제공고시의 해당 항목에 별도 입력값 존재시
                            if (itemSpecValueObj && itemSpecValueObj['specFieldValue']) {

                                // 품목별 상품정보 제공고시 세부 항목의 값을 복사
                                item['basicValue'] = itemSpecValueObj['specFieldValue'];

                            }

                        });

                    }

                    // viewModel 에 상품정보 제공고시 분류 / 항목 Map 추가
                    viewModel.set('itemSpecMap', itemSpecMap);

                    // 인자로 받은 상품정보 제공고시 분류코드를 viewModel 에 세팅 : 상품정보 제공고시의 상품군이 해당 코드로 선택됨
                    viewModel.set('selectedItemSpecMasterId', ilSpecMasterId);
                    viewModel.onSpecGroupChange(null); // 상품정보 제공고시의 상품군 change 이벤트 수동 호출

                }

            },
            isAction : 'select',
        });

    }

    // 해당 상품정보제공고시 항목 코드의 수정 메시지 조회
    function fnGetItemSpecFieldDetailMessage(specFieldCode) {

        var itemSpecModifiedMessage;

        // 유통기간 : ERP 연동여부에 따라 ERP 또는 BOS 유통기간 분기
        var distributionPeriod = ( viewModel.get('isErpItemLink') == true ? viewModel.get('erpDistributionPeriod') : viewModel.get('bosDistributionPeriod') );

        var paramObj = {
            specFieldCode : specFieldCode,
            urSupplierId : viewModel.get('bosSupplier'),
            distributionPeriod : distributionPeriod
        };

        fnAjax({
            url : "/admin/item/master/register/getItemSpecFieldDetailMessage",
            method : 'POST',
            params : paramObj,
            isAction : 'select',
            async : false,
            success : function(data, status, xhr) {

                if( data['itemSpecModifiedMessage'] ) {
                    itemSpecModifiedMessage = data['itemSpecModifiedMessage'];
                }

            }
        });

        return itemSpecModifiedMessage;

    }

    /***************************************************************************
     * 품목 정보 조회 function : 상품 인증정보
     ***************************************************************************/

    function fnGetItemCertificationList(ilItemCode) { // 품목코드로 상품 인증정보 조회

        fnAjax({
            url : "/admin/item/master/register/getItemCertificationList",
            method : 'GET',
            params : {
                ilItemCode : ilItemCode
                , ilItemApprId : null
            },
            success : function(data) {

                if (data['itemCertificationList'] && data['itemCertificationList'].length > 0) {

                    // 사용하기 편리하도록 Javascript Object (Map) 으로 세팅
                    var itemCertificationValueMap = {};

                    for (var i = 0; i < data['itemCertificationList'].length; i++) {
                        // { 상품 인증정보 ID : { 품목별 상품 인증정보 세수 항목 객체 } , ... } 형식으로 세팅
                        itemCertificationValueMap[data['itemCertificationList'][i]['ilCertificationId']] = data['itemCertificationList'][i];
                    }

                    viewModel.set('itemCertificationValueMap', itemCertificationValueMap);

                    viewModel.set('itemCertificationList', []);  // 화면에 기존 출력된 상품 인증정보 목록 초기화

                    // 마스터 품목 복사를 위해 조회한 상품 인증정보 데이터를 viewModel 의 itemCertificationList 에 추가
                    for (var i = 0; i < data['itemCertificationList'].length; i++) {

                        var ilCertificationId = data['itemCertificationList'][i]['ilCertificationId'];

                        var itemCertificationCodeObj = viewModel.get('itemCertificationCodeMap')[ilCertificationId];
                        var itemCertificationValueObj = viewModel.get('itemCertificationValueMap')[ilCertificationId];

                        // 해당 상품 인증정보 ID 의 인증정보명
                        var certificationName = itemCertificationCodeObj['certificationName'];

                        // 해당 상품인증정보의 인증 이미지 URL
                        var certificationImageSrc = publicUrlPath + itemCertificationCodeObj['imagePath'] + itemCertificationCodeObj['imageName'];

                        // 화면에 출력할 인증정보 메시지
                        var certificationDescription = '';

                        // 품목별 상품 인증정보에 입력된 상세항목 존재시 : 해당 항목의 상세정보를 화면에 출력
                        if (itemCertificationValueObj['certificationDescription'] && itemCertificationValueObj['certificationDescription'].length > 0) {

                            certificationDescription = itemCertificationValueObj['certificationDescription'];

                        } else { // 해당 코드의 기본 메시지를 화면에 출력

                            certificationDescription = itemCertificationCodeObj['defaultCertificationDescription'];

                        }

                        viewModel.get('itemCertificationList').push({
                            ilCertificationId : ilCertificationId,
                            certificationName : certificationName,
                            imageSrc : certificationImageSrc,
                            certificationDescription : certificationDescription
                        });

                        // 화면에 출력된 상품 인증정보 데이터 건수 갱신
                        viewModel.set('itemCertificationCount', viewModel.get("itemCertificationList").length);

                    }

                    viewModel.trigger("change", {
                        field : "itemCertificationList"
                    });

                }

            }, // end of success
            isAction : 'select'
        });
    }

    /***************************************************************************
     * 품목 정보 조회 function : 영양 정보
     ***************************************************************************/

    // ERP 연동 API 로 영양정보 조회 후 화면상에 출력할 ERP 영양분석단위 메시지 생성
    function fnGenerateErpNutritionAnalysisMessage(erpNutritionApiObj) {

        var servingSize = (erpNutritionApiObj['erpServingsize'] ? erpNutritionApiObj['erpServingsize'] : ''); // 1회 제공량
        var servingContainer = (erpNutritionApiObj['erpServingContainer'] ? erpNutritionApiObj['erpServingContainer'] : ''); // 총 제공량

        var message;

        if (servingSize && servingContainer) { // 1회 제공량, 총 제공량 모두 유효한 값 조회된 경우

            message = '1회 분량   ' + servingSize + '     총 ' + servingContainer + ' ';

            viewModel.set('erpNutritionAnalysisMessage', message); // viewModel 에 메시지 세팅
            viewModel.set('showErpNutritionAnalysisMessage', true); // visible 처리

            // BOS 1회 분량, 총 제공량 input 영역에 동일한 값 표시
            viewModel.set('nutritionQuantityPerOnce', servingSize); // BOS 1회 분량
            viewModel.set('nutritionQuantityTotal', servingContainer); // BOS 총 제공량

        } else if( servingSize ) { // 1회 제공량만 유효한 값 조회시

            message = '1회 분량   ' + servingSize;

            viewModel.set('erpNutritionAnalysisMessage', message); // viewModel 에 메시지 세팅
            viewModel.set('showErpNutritionAnalysisMessage', true); // visible 처리

            // BOS 1회 분량 input 영역에 동일한 값 표시
            viewModel.set('nutritionQuantityPerOnce', servingSize); // BOS 1회 분량

        } else if( servingContainer ) { // 총 제공량만 유효한 값 조회시

            message = '총   ' + servingContainer;

            viewModel.set('erpNutritionAnalysisMessage', message); // viewModel 에 메시지 세팅
            viewModel.set('showErpNutritionAnalysisMessage', true); // visible 처리

            // BOS 총 제공량 input 영역에 동일한 값 표시
            viewModel.set('nutritionQuantityTotal', servingContainer); // BOS 총 제공량

        } else {

            viewModel.set('erpNutritionAnalysisMessage', ''); // viewModel 에 메시지 세팅
            viewModel.set('showErpNutritionAnalysisMessage', false); // visible 처리 해제

            // BOS 1회 분량, 총 제공량 input 영역 값 초기화
            viewModel.set('nutritionQuantityPerOnce', ''); // BOS 1회 분량
            viewModel.set('nutritionQuantityTotal', ''); // BOS 총 제공량

        }

    }

    /* 마스터 품목 복사시 상품 영양정보 Data 조회 */

    // 1. ERP 연동 품목인 경우 => IL_NUTRITION 테이블에 등록된 모든 코드 조회
    // 품목코드는 넘기지 않음 : IL_ITEM_NUTRITION 테이블의 데이터는 무시
    // erpLinkIfYn 값 true (ERP 연동) : BOS 상에 등록된 영양정보 세부항목도 조회하지 않음

    // 2. ERP 미연동 품목인 경우 => 복사 원본 품목 코드로 등록 가능한 IL_NUTRITION 테이블의 코드 조회 후 복사함
    // 품목 코드 넘김 : 복사 원본 품목 코드로 기등록된 IL_ITEM_NUTRITION 테이블의 데이터도 같이 조회 후 복사함
    // erpLinkIfYn 값 false (ERP 미연동) : 해당 품목 코드로 BOS 상에 등록된 영양정보 세부항목을 같이 조회 후 반환

    function fnGetAddAvailableNutritionCodeList(ilItemCode, isErpItemLink) {   // 등록 가능한 영양정보 분류코드 리스트 조회

        fnAjax({
            url : "/admin/item/master/register/getAddAvailableNutritionCodeList",
            method : 'GET',
            params : {
                ilItemCode : ilItemCode,
                isErpItemLink : isErpItemLink
                , ilItemApprId : null
            },
            async: false,
            success : function(data) {

                // 등록 가능한 영양정보 코드 조회시
                if (data['addAvailableNutritionCodeList'] && data['addAvailableNutritionCodeList'].length > 0) {

                    // 등록 가능한 영양정보 분류코드 목록을 viewModel 에 등록
                    viewModel.set( 'addAvailableNutritionCodeList', data['addAvailableNutritionCodeList'] );

                    fnKendoDropDownList({ // 등록 가능한 영양정보 분류코드 리스트
                        id : 'addAvailableNutritionCode',
                        tagId : 'addAvailableNutritionCode',
                        data : viewModel.get('addAvailableNutritionCodeList'),
                        textField : "nutritionName",
                        valueField : "nutritionCode"
                    });

                    viewModel.trigger("change", {  // viewModel 에서 itemNutritionDetailList 관련 refresh
                        field : "addAvailableNutritionCodeList"
                    });

                    // 최초 조회시 첫 번째 영양코드 자동 선택
                    viewModel.set('selectedAddAvailableNutritionCode', viewModel.get('addAvailableNutritionCodeList')[0]['nutritionCode'] );

                    // 기존 세팅된 영양정보 분류 코드 Map 초기화
                    addAvailableNutritionCodeMap = {}; // "영양코드" : "영양코드명" 코드 Map
                    addAvailableNutritionUnitMap = {}; // "영양코드" : "영양코드 단위" 코드 Map
                    addAvailableNutritionPercentMap = {}; // 영양소 기준치 % 사용하는 영양코드 Map : ( "영양코드" : "영양코드명" 코드 )

                    data['addAvailableNutritionCodeList'].forEach(function(item, index) {

                        // 접근하기 편리하도록 화면상에서 코드 List => Javascript 객체 ( Map 형식 ) 으로 변환
                        addAvailableNutritionCodeMap[item['nutritionCode']] = item['nutritionName']; // { "영양코드" : "영양코드명" ... }
                        addAvailableNutritionUnitMap[item['nutritionCode']] = item['nutritionUnit']; // { "영양코드" : "영양코드 단위" ... }

                        if (item['nutritionPercentYn'] == true) { // 영양소 기준치 % 사용하는 영양코드를 별도로 취합
                            addAvailableNutritionPercentMap[item['nutritionCode']] = item['nutritionName'];
                        }

                    });

                    /*
                     * ERP 연동 상품 : API 로 조회된 정보가 영양정보 상세항목에 모두 추가됨
                     *
                     */
                    if (viewModel.get('isErpItemLink') == true) {
                        fnSetErpLinkItemNutritionDetail();
                    }

                    /*
                     * ERP 미연동 상품 :  복사 원본 품목 코드로 기동록된 영양정보 세부항목 있는 경우 viewModel 에 세팅
                     *
                     * => 이 때는 addNutritionDetailRow 메서드 사용하지 않고 별도 로직으로 구현함
                     *
                     * 이 때 addAvailableNutritionCodeMap, addAvailableNutritionUnitMap, addAvailableNutritionPercentMap 의
                     *
                     * 정보가 먼저 세팅되어야 함
                     */
                    if (data['itemNutritionDetailList'] && data['itemNutritionDetailList'].length > 0) {
                       fnSetErpNotLinkItemNutritionDetail(data['itemNutritionDetailList']);
                    }
                }
            },
            isAction : 'select'
        });
    }


    // ERP 연동 품목의 영양정보 출력 : ERP API 로 조회된 영양 정보가 영양정보 상세항목 화면에 모두 출력
    function fnSetErpLinkItemNutritionDetail() {

//        viewModel.set('nutritionDisplayYn', 'Y');  // 최초 영양정보 상세 노출
//        viewModel.onNutritionDisplayYnChange(null);

        // viewModel.get('erpNutritionApiObj') : ERP API 를 통해서 해당 ERP 연동 상품의 품목 코드로 조회된 정보 객체
        for ( var nutritionCode in viewModel.get('erpNutritionApiObj') ) { // 각 영양정보 코드별 반복문 실시

            if( viewModel.get('erpNutritionApiObj.' + nutritionCode ) ) { // ERP API 로 조회된 해당 영양코드의 값 존재시

                // API 로 조회된 정보 내에 BOS 상의 영양정보 분류코드와 일치하는 속성 존재시
                if (addAvailableNutritionCodeMap[nutritionCode] && typeof addAvailableNutritionCodeMap[nutritionCode] === 'string') {
                    addNutritionDetailRow(nutritionCode);
                }

            }
        }

        // 영양정보 분류코드 Select 의 첫번째 행으로 선택되도록 viewModel 에 세팅
        if( viewModel.get('addAvailableNutritionCodeList')[0] ) {
            viewModel.set('selectedAddAvailableNutritionCode', viewModel.get('addAvailableNutritionCodeList')[0]['nutritionCode']);
        }

        viewModel.trigger("change", {  // viewModel 에서 itemNutritionDetailList 관련 refresh
            field : "itemNutritionDetailList"
        });

    }

    // ERP 미연동 품목의 영양정보 출력 : 마스터 품목 복사로 조회한 ERP 미연동 품목의 영양정보 상세항목을 화면에 모두 출력
    function fnSetErpNotLinkItemNutritionDetail(itemNutritionDetailList) {

        viewModel.set('itemNutritionDetailList', []); // 기존 영양정보 세부항목 초기화

        for (var i = 0; i < itemNutritionDetailList.length; i++) {

            var nutritionDetailInfo = itemNutritionDetailList[i];
            var nutritionCode = nutritionDetailInfo['nutritionCode'];

            // 분류코드명 : 영양정보 분류코드 Map 을 조회하여 세팅
            var nutritionCodeName = addAvailableNutritionCodeMap[nutritionCode];
            var nutritionUnit = (addAvailableNutritionUnitMap[nutritionCode] ? addAvailableNutritionUnitMap[nutritionCode] : ''); //단위 미등록시는 '' 출력

            var nutritionQuantity = nutritionDetailInfo['nutritionQuantity']; // 복사한 BOS 영양성분량
            var nutritionPercent = nutritionDetailInfo['nutritionPercent']; // 복사한 BOS 영양성분 기준치대비 함량

            // BOS 에서 영양소 기준치 % 사용여부 : addAvailableNutritionUnitMap 에 등록된 경우 true
            var nutritionPercentYn = (addAvailableNutritionPercentMap[nutritionCode] ? true : false);

            viewModel.get('itemNutritionDetailList').push({
                canDeleted : true, // 삭제 버튼 노출 여부 : ERP 미연동 품목은 영양정보 상세항목 모두 삭제 가능
                nutritionCodeName : nutritionCodeName, // 영양정보 분류코드명
                nutritionCode : nutritionCode, // 영양정보 분류코드
                erpNutritionQuantity : '', // ERP 영양성분량 : 미연동 상품인 경우 '' 출력
                nutritionQuantity : nutritionQuantity, // BOS 영양성분량
                nutritionUnit : nutritionUnit, // BOS 영양성분량 단위
                erpNutritionPercent : '', // ERP 영양성분 기준치대비 함량 : 미연동 상품인 경우 '' 출력
                nutritionPercent : nutritionPercent, // BOS 영양성분 기준치대비 함량
                nutritionPercentYn : nutritionPercentYn, // BOS 에서 영양소 기준치 % 사용여부
            });

            // 상품 영양정보 항목에 추가된 항목은 등록 가능한 영양정보 분류코드 목록에서 삭제
            for( var j = viewModel.get('addAvailableNutritionCodeList').length - 1 ; j >= 0 ; j -- ) {

                if( viewModel.get('addAvailableNutritionCodeList')[j]['nutritionCode'] == nutritionCode ) {
                    viewModel.get("addAvailableNutritionCodeList").splice(j, 1);
                    break;
                }

            }

            // 영양정보 분류코드 Select 의 첫번째 행으로 선택되도록 viewModel 에 세팅
            if( viewModel.get('addAvailableNutritionCodeList')[0] ) {
                viewModel.set('selectedAddAvailableNutritionCode', viewModel.get('addAvailableNutritionCodeList')[0]['nutritionCode']);
            }

            viewModel.trigger("change", {  // viewModel 에서 itemNutritionDetailList 관련 refresh
                field : "itemNutritionDetailList"
            });

        } // end of for

    }

    // 인자로 받은 영양분류 코드로 상품 영양정보 항목에 Row 추가
    function addNutritionDetailRow(nutritionCode) {

        // 분류코드명 : 영양정보 분류코드 Map 을 조회하여 세팅
        var nutritionCodeName = addAvailableNutritionCodeMap[nutritionCode];
        var nutritionUnit = (addAvailableNutritionUnitMap[nutritionCode] ? addAvailableNutritionUnitMap[nutritionCode] : ''); // 단위 미등록시는 '' 출력

        // BOS 에서 영양소 기준치 % 사용여부 : addAvailableNutritionPercentMap 에 등록된 영양코드인 경우 true
        var nutritionPercentYn = (addAvailableNutritionPercentMap[nutritionCode] ? true : false);

        // API 를 통해 조회한 해당 품목코드의 ERP 영양정보 값 ( 미연동 품목 / 해당 영양분류코드로 데이터 없는 경우 '' 출력 )
        var erpNutritionQuantity = (viewModel.get('erpNutritionApiObj.' + nutritionCode) ? viewModel.get('erpNutritionApiObj.' + nutritionCode) : '');

        // API 를 통해 조회한 ERP 영양소 기준치 % 값
        var erpNutritionPercent = "";

        // 영양정보 API 에서 기준치대비 함량 값은 'calories' => 'caloriesPercent' 와 같이 key 로 세팅
        // 영양소 기준치 % 사용하고 인자로 받은 nutritionCode 의 Key 가 API 로 조회된 Data 내에 있는 경우
        if ( nutritionPercentYn == true && viewModel.get('erpNutritionApiObj')[nutritionCode] ) {
            erpNutritionPercent = (viewModel.get('erpNutritionApiObj.' + nutritionCode + 'Percent') ? viewModel.get('erpNutritionApiObj.' + nutritionCode + 'Percent') : '');
        }

        var nutritionQuantity = ""; // 영양성분량 : 영양 최초 항목 추가시 기본값 ""
        var nutritionPercent = ""; // 영양성분 기준치대비 함량 : 최초 항목 추가시 기본값 ""

        // ERP 연동 품목 && 영양정보 API 로 조회된 영양 정보에 해당 영양분류코드의 ERP 값 존재시 기본값으로 세팅
        if (viewModel.get('erpItemLinkYn') == 'Y' && checkDataEmpty(viewModel.get('erpNutritionApiObj')) == false) {

            // 영양성분량, 영양성분 기준치대비 함량 : ERP 값을 기본값으로 세팅
            nutritionQuantity = erpNutritionQuantity;
            nutritionPercent = erpNutritionPercent;

        }

        var canDeleted = true;  // 영양정보 세부항목 삭제 버튼 노출 여부 : 기본값 true ( 삭제 버튼 노출 )

        // ERP API 에서 영양성분량 or 영양성분 기준치대비 함량 조회시 : ERP 연동 항목이므로 삭제 불가
        if( viewModel.get('erpNutritionApiObj')[nutritionCode]
         || viewModel.get('erpNutritionApiObj')[nutritionCode] == 0
         || viewModel.get('erpNutritionApiObj')[nutritionCode + 'Percent']
         || viewModel.get('erpNutritionApiObj')[nutritionCode + 'Percent'] == 0  ) {
            canDeleted = false;
        }

        // viewModel 에 선택한 영양정보 코드로 해당 Data 추가
        viewModel.get('itemNutritionDetailList').push({
            canDeleted : canDeleted, // 삭제 가능 여부 :  ERP 연동 항목인 경우 삭제 불가능
            nutritionCodeName : nutritionCodeName, // 영양정보 분류코드명
            nutritionCode : nutritionCode, // 영양정보 분류코드
            erpNutritionQuantity : erpNutritionQuantity, // ERP 영양성분량
            nutritionQuantity : nutritionQuantity, // BOS 영양성분량
            nutritionUnit : nutritionUnit, // BOS 영양성분량 단위
            erpNutritionPercent : (nutritionPercentYn == true ? erpNutritionPercent : ''), // ERP 영양성분 기준치대비 함량
            nutritionPercent : (nutritionPercentYn == true ? nutritionPercent : ''), // BOS 영양성분 기준치대비 함량
            nutritionPercentYn : nutritionPercentYn, // BOS 에서 영양소 기준치 % 사용여부,

        });

        // 상품 영양정보 항목에 추가된 항목은 등록 가능한 영양정보 분류코드 목록에서 삭제
        for( var i = viewModel.get('addAvailableNutritionCodeList').length - 1 ; i >= 0 ; i -- ) {

            if( viewModel.get('addAvailableNutritionCodeList')[i]['nutritionCode'] == nutritionCode ) {
                viewModel.get("addAvailableNutritionCodeList").splice(i, 1);
                break;
            }

        }

        // 영양정보 분류코드 Select 의 첫번째 행으로 선택되도록 viewModel 에 세팅
        if( viewModel.get('addAvailableNutritionCodeList')[0] ) {
            viewModel.set('selectedAddAvailableNutritionCode', viewModel.get('addAvailableNutritionCodeList')[0]['nutritionCode']);
        }

    }

    /***************************************************************************
     * 조회 function : 출고처
     ***************************************************************************/
    function fnGetItemWarehouseList(urSupplierId) { // 공급업체 PK 로 출고처 그룹, 출고처 정보 조회

        fnAjax({ // 공급업체 PK 로 출고처 그룹, 출고처 정보 조회
            url : "/admin/item/master/register/getItemWarehouseCode",
            method : 'GET',
            params : {
                urSupplierId : urSupplierId
                , masterItemType : viewModel.get("masterItemType")

            },
            isAction : 'select',
            success : function(data, status, xhr) {

                // 출고처 그룹 코드 목록 조회시
                if (data['warehouseGroupCodeList'] && data['warehouseGroupCodeList'].length > 0) {

                    viewModel.set('itemWarehouseList', []); // 기존 해당 품목코드의 출고처 목록 초기화

                    fnKendoDropDownList({ // 출고처 그룹 Select
                        id : 'warehouseGroup',
                        data : data['warehouseGroupCodeList'],
                        textField : "NAME",
                        valueField : "CODE",
                        blank : '출고처 그룹',
                    }).bind("change", onWarehouseGroupChange); // 출고처 그룹 Select 의 change 이벤트

                    viewModel.set('selectedWarehouseGroupCode', ''); // 기존 선택된 출고처 그룹 값 초기화

                    /*
                     * 출고처 목록 조회시
                     */
                    if (data['itemWarehouseCodeList'] && data['itemWarehouseCodeList'].length > 0) {

                        var itemWarehouseMap = {};

                        data['itemWarehouseCodeList'].forEach(function(item, index) {

                            // 출고처 그룹 Select 값 변경시 출고처 Select 필터링이 편리하도록 Map ( Javascript Object ) 으로 변환
                            if (!itemWarehouseMap[item['warehouseGroupCode']]) {

                                // Map 에 추가되지 않은 출고처 그룹 코드인 경우 : ( 출고처 그룹 코드 : 빈 배열 ) 생성 후 추가
                                itemWarehouseMap[item['warehouseGroupCode']] = [];
                                itemWarehouseMap[item['warehouseGroupCode']].push(item);

                            } else {

                                // 이미 추가된 출고처 그룹 코드 : 해당 배열에 출고처 추가
                                itemWarehouseMap[item['warehouseGroupCode']].push(item);

                            }

                        });

                        // viewModel 에 출고처 정보 Map 추가
                        viewModel.set('itemWarehouseMap', itemWarehouseMap);

                        fnKendoDropDownList({ // 기존 출고처 dropdown Data 초기화
                            id : 'warehouse',
                            data : {},
                            textField : "warehouseName",
                            valueField : "urSupplierWarehouseId",
                            blank : '출고처 선택'
                        });

                        viewModel.set('selectedUrSupplierWarehouseId', ''); // 기존 선택된 출고처 값 초기화

                        // 마스터 품목 유형이 매장일때 기본 매장 출고처 기본 SET
                        if(viewModel.get('masterItemType') == 'MASTER_ITEM_TYPE.SHOP_ONLY'){
                        	addStoreWareHouse(false);
                        }
                    }

                }

            }
        });
    }

    /***************************************************************************
     * 조회 function : 건강생활 시식/증정/기타인 품목에 대한 출고처 리스트 출력
     ***************************************************************************/
    function fnGetwarehouseUndefinedItem(){
        fnKendoDropDownList({
            id : 'bosSupplier', // BOS 공급업체
            data : [ {
                "urSupplierId" : "4",
                "supplierName" : "풀무원녹즙(FDD)"
            }, {
                "urSupplierId" : "5",
                "supplierName" : "풀무원녹즙(PDM)"
            }, {
                "urSupplierId" : "6",
                "supplierName" : "씨에이에프"
            }, {
                "urSupplierId" : "7",
                "supplierName" : "풀무원건강생활"
            } ],
            valueField : 'urSupplierId',
            textField : 'supplierName',
            blank : '미지정'
        });
        viewModel.set('isSupplierDisabled', false); // 공급업체 비활성화 해제
    }

    function addStoreWareHouse(canDeleted) {
    	var storeWarehouseData = null;
    	$.each(viewModel.get('itemWarehouseMap'), function (key, list){
    		if(typeof list != "string" && list['length'] > 0){
        		$.each(list, function (index, warehouse) {
        			if(warehouse['storeWarehouseYn'] == 'Y'){
        				storeWarehouseData = warehouse;
        			}
        		});
    		}
    	});
    	if(storeWarehouseData != null){
    		storeWarehouseData['canDeleted'] = canDeleted;
    		viewModel.get('itemWarehouseList').push(storeWarehouseData);
            $("#itemWarehousePoTpIdTemplateList"+storeWarehouseData.urWarehouseId).hide();
    	}
    }

    function onWarehouseGroupChange(e) { // 출고처 그룹 변경시 출고처 목록 Filtering 이벤트

        if( e.sender.value() ) {  // 등록된 출고처 그룹 선택시

            // e.sender.value() : Select 에서 선택된 출고처 그룹의 코드
            // 변경된 출고처 그룹 코드를 selectedUrSupplierWarehouseId 에 세팅
            viewModel.set('selectedUrSupplierWarehouseId', e.sender.value());

            fnKendoDropDownList({ //  변경된 출고처 그룹 코드에 해당하는 출고처 목록을 출고처 Select 에 세팅
                id : 'warehouse',
                data : viewModel.get('itemWarehouseMap')[e.sender.value()],
                textField : "warehouseName",
                valueField : "urSupplierWarehouseId",
                blank : '출고처 선택'
            })

        } else { // blank ( '출고처 그룹' ) 선택시 : 출고처 dropdown Data 초기화

            fnKendoDropDownList({ // 출고처
                id : 'warehouse',
                data : {},
                textField : "warehouseName",
                valueField : "urSupplierWarehouseId",
                blank : '출고처 선택'
            });

        }

    }

    function onWarehouseChange(e) { // 출고처 Select 변경시 이벤트
        viewModel.set('selectedUrSupplierWarehouseId', e.sender.value()); // e.sender.value() : Select 에서 선택된 출고처의 코드
    }

    /***************************************************************************
     * 품목 정보 조회 function : 품목 이미지
     ***************************************************************************/

    // 해당 품목코드로 등록된 품목 이미지 목록 조회
    function fnGetItemImageList(ilItemCode) {

        var addImageCount = 0; // 화면상에 추가된 이미지 개수

        fnAjax({
            url : "/admin/item/master/register/getItemImageList",
            method : 'GET',
            params : {
                ilItemCode : ilItemCode
                , ilItemApprId : null
            },
            async: false,
            success : function(data) {

                if (data['itemImageList'] && data['itemImageList'].length > 0) {

                    // 마스터 품목 복사시 기존 이미지 목록 초기화
                    viewModel.set('itemImageList', [] );
                    viewModel.set( 'itemImageFileList', [] );

                    var xhrArray = [];

                    // 1차 반복문 : 이미지 파일 다운로드용 XMLHttpRequest 배열 생성
                    for (var i = 0; i < data['itemImageList'].length; i++) {

                        var imageSrcUrl = publicUrlPath + data['itemImageList'][i]['imagePath']; // 품목 이미지 url
                        var imagePhysicalName = data['itemImageList'][i]['imagePhysicalName']; //
                        var sort = data['itemImageList'][i]['sort']; // 정렬순서

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
                                // itemImageFileList 에 push 시에는 원본 이미지의 정렬 순서와 상관없음
                                viewModel.get('itemImageFileList').push(file);

                                // 해당 정렬 순서의 index 에 이미지 정보 추가
                                viewModel.get('itemImageList').splice(this.sort, 0, {
                                    basicYn : '', // 대표 이미지 여부
                                    imagePath : this.fileName, //
                                    imageOriginalName : this.fileName, //
                                    sort : this.sort, // 정렬순서
                                    imageSrc : blobUrl, // 상품 이미지 url
                                });

                                viewModel.get('itemImageList').sort(function(x, y) {
                                    return x.sort == y.sort ? 0 : (x.sort > y.sort ? 1 : -1);
                                });

                                viewModel.trigger("change", {
                                    field : "itemImageList"
                                });

                                addImageCount++;

                                // 등록된 상품 이미지 개수 갱신
                                viewModel.set('itemImageCount', viewModel.get('itemImageList').length);

                                // 이미지 최대 등록가능 건수보다 복사할 총 이미지 개수가 작고 마지막 이미지 추가시 : 상품 추가 영역 append
                                if( viewModel.get('itemImageMaxLimit') > xhrArray.length && addImageCount == xhrArray.length) {
                                    fnAddItemImageArea();
                                }

                            } else { // 404 등 에러 발생시

                                // 등록된 상품 이미지 개수 갱신
                                viewModel.set('itemImageCount', viewModel.get('itemImageList').length);

                                // 상품 이미지 등록 영역 추가
                                fnAddItemImageArea();

                            }

                        }

                        xhr.send();
                    }

                }

            }, // end of success
            isAction : 'select'
        });

    }

    /***************************************************************************
     * 기타 function
     ***************************************************************************/

    // validation 체크 : 유효하지 않은 경우 false 반환
    function fnValidationCheck(functionName, data) {

        switch (functionName) {

        case 'save': // 품목 저장

            // ERP 미연동 품목인 경우 : 마스터 품목명, 품목바코드 입력 여부 체크
            if ( viewModel.get('isErpItemLink') == false ) {

                if( ! viewModel.get('itemName') || $.trim( viewModel.get('itemName') ).length == 0 ) {
                    valueCheck('마스터 품목명 값을 입력하지 않았습니다.', 'itemName');
                    return false;
                }

            } else { // ERP 연동 품목 : 마스터 폼목명 입력 여부 체크

                if( ! viewModel.get('itemName') || $.trim( viewModel.get('itemName') ).length == 0 ) {
                    valueCheck('마스터 품목으로 등록할 ERP 품목코드를 검색해주세요.', 'itemName');
                    $('#erpItemLinkYn').data("kendoDropDownList").focus();
                    return false;
                }

            }

            if (checkCategoryStandardId() == false) { // 선택 가능한 하위 카테고리까지 선택하지 않은 경우
                valueCheck('선택 가능한 하위 표준 카테고리가 존재합니다.<br>최종 분류 선택시 저장이 가능합니다.', 'categoryStandardDepth4');
                return false;
            }

            if (checkCategoryStandardId() == null) { // 표준 카테고리 지정 여부 체크
                valueCheck('표준 카테고리를 선택하지 않았습니다.', 'categoryStandardDepth1');
                return false;
            }

            if (viewModel.get("bosSupplier") == '') { // 공급업체
                valueCheck('공급업체를 선택하지 않았습니다.', 'bosSupplier');
                return false;
            }

            if (viewModel.get("bosBrand") == '') { // BOS 브랜드
                valueCheck('표준 브랜드를 선택하지 않았습니다.', 'bosBrand');
                return false;
            }

            if (viewModel.get("dpBrand") == '') { // 전시 브랜드
                valueCheck('전시 브랜드를 선택하지 않았습니다.', 'dpBrand');
                return false;
            }

            if (viewModel.get("bosStorageMethod") == '') { // BOS 보관방법
                valueCheck('보관방법을 선택하지 않았습니다.', 'bosStorageMethod');
                return false;
            }

            var bosStorageMethodIndex = $('#bosStorageMethod').data('kendoDropDownList').select();
            if(bosStorageMethodIndex == 0) {
            	valueCheck('보관방법을 선택하지 않았습니다.', 'bosStorageMethod');
                return false;
            }

            // PDM 그룹코드 활성화 시 체크
            if(viewModel.get("isPdmGroupCodeVisible") == true) {
            	if(viewModel.get("pdmGroupCode") == "") {
            		valueCheck('PDM 그룹코드를 입력하지 않았습니다.', 'pdmGroupCode');
                    return false;
            	}
            }

            // ERP 미연동 품목인 경우 BOS 유통기간 체크 : 숫자형만 인정
            if (viewModel.get('isErpItemLink') == false && $.isNumeric(viewModel.get("bosDistributionPeriod")) == false) {
                valueCheck('유통기간을 입력하지 않았습니다.', 'bosDistributionPeriod');
                return false;
            }

            // ERP 미연동 품목인 경우 : 박스 체적 / 박스 입수량 입력 여부 체크
            if ( viewModel.get('isErpItemLink') == false && viewModel.get("masterItemType") !== "MASTER_ITEM_TYPE.INCORPOREITY") {

                if( ! viewModel.get('boxWidth') ) {
                    valueCheck('박스 체적 가로 값을 입력하지 않았습니다.', 'boxWidth');
                    return false;
                }

                if( ! viewModel.get('boxDepth') ) {
                    valueCheck('박스 체적 세로 값을 입력하지 않았습니다.', 'boxDepth');
                    return false;
                }

                if( ! viewModel.get('boxHeight') ) {
                    valueCheck('박스 체적 높이 값을 입력하지 않았습니다.', 'boxHeight');
                    return false;
                }

                if( ! viewModel.get('bosPiecesPerBox') ) {
                    valueCheck('박스 입수량 값을 입력하지 않았습니다.', 'bosPiecesPerBox');
                    return false;
                }

            }

            if ($.isNumeric(viewModel.get("sizePerPackage")) == false && viewModel.get("masterItemType") !== "MASTER_ITEM_TYPE.INCORPOREITY") { // 포장단위별 용량 : 숫자형만 인정
                valueCheck('포장단위별 용량을 입력하지 않았습니다.', 'sizePerPackage');
                return false;
            }

            if (viewModel.get("sizeUnit") == '' && viewModel.get("masterItemType") !== "MASTER_ITEM_TYPE.INCORPOREITY") { // 용량(중량) 단위
                valueCheck('용량(중량) 단위를 선택하지 않았습니다.', 'sizeUnit');
                return false;
            }

            if(viewModel.get("sizeUnit") == 'UNIT_TYPE.DIRECT_INPUT') {
            	if(viewModel.get("sizeUnitDirectInputValue") == "") {
            		valueCheck('용량(중량) 단위를 입력하지 않았습니다.', 'sizeUnitDirectInputValue');
                    return false;
            	}
            }

            if (!viewModel.get('selectedItemSpecMasterId')) { // 상품정보 제공고시
                valueCheck('상품정보 제공고시의 상품군을 선택하지 않았습니다.', 'specGroup');
                return false;
            }

            // 화면상의 상품정보 제공고시 목록
            var itemSpecValueList = viewModel.get('itemSpecValueList');

            for (var i = itemSpecValueList.length - 1; i >= 0; i--) { // 화면상의 상품정보 제공고시 목록

                if (! itemSpecValueList[i]['basicValue']) {
                    valueCheck('상품정보 제공고시를 입력하지 않은 항목이 있습니다.', '');
                    return false;
                }

            }


            if( viewModel.get('nutritionDisplayYn') == 'Y' ) {   // 영양정보 표시여부 '노출' 선택시

                // 화면상의 영양정보 세부항목 목록
                var itemNutritionDetailList = viewModel.get('itemNutritionDetailList');

                for (var i = itemNutritionDetailList.length - 1; i >= 0; i--) { // 화면상의 영양정보 세부항목 목록

                    // float 여부 체크
                    if (isNaN(parseFloat(itemNutritionDetailList[i]['nutritionQuantity']))) {
                        valueCheck('영양성분 값을 미입력했거나, 정확하지 않은 항목이 있습니다.', '');
                        return false;
                    }

                    // 영양소 기준치 % 사용시 : 영양소 기준치 % 값 float 여부 체크
                    if (itemNutritionDetailList[i]['nutritionPercentYn'] == true && isNaN(parseFloat(itemNutritionDetailList[i]['nutritionPercent']))) {
                        valueCheck('영양성분 기준치 % 값을 미입력했거나, 정확하지 않은 항목이 있습니다.', '');
                        return false;
                    }

                }

                // 추가된 영양정보 세부항목 없고 영양성분 기타 값도 미입력시 : alert 처리 / return
                if( itemNutritionDetailList.length == 0 && ( ! viewModel.get('nutritionEtc') || viewModel.get('nutritionEtc').trim() == '' ) ) {
                    valueCheck('영양정보 노출 설정 시 1개 이상의 영양정보가 입력되어야 합니다', 'nutritionEtc');
                    return false;
                }

            }
            else {
                // 영양정보 노출 안함 설정 > 노출 메시지가 미입력시 : alert 처리 / return
                if( viewModel.get('nutritionDisplayDefalut') == null || $.trim( viewModel.get('nutritionDisplayDefalut') ) == '' ) {
                    valueCheck('영양정보 노출 안함 설정 시 노출 메시지가 입력되어야 합니다.<br>노출 메시지를 확인해주세요.', 'nutritionDisplayDefalut');
                    return false;
                }
            }

            if( viewModel.get("isErpItemLink") == true ) {  // ERP 연동 품목의 가격정보 조회 여부 체크

//                if( ! viewModel.get('erpPriceApplyStartDate') ) {
//                    valueCheck('해당 ERP 연동 품목의 가격 정보 적용 시작일이 지정되지 않았습니다.', '');
//                    return false;
//                }
//
//                if( (viewModel.get('erpStandardPrice') != 0 && !viewModel.get('erpStandardPrice')) ) {
//                    valueCheck('해당 ERP 연동 품목의 원가 Data 가 없습니다.', 'erpStandardPrice');
//                    return false;
//                }
//
//                if( ! viewModel.get('erpRecommendedPrice') ) {
//                    valueCheck('해당 ERP 연동 품목의 정상가 Data 가 없습니다.', 'erpRecommendedPrice');
//                    return false;
//                }

                if( ! viewModel.get('priceApplyStartDate') ) {
                    valueCheck('해당 ERP 연동 품목의 가격 정보 적용 시작일이 지정되지 않았습니다.', '');
                    return false;
                }

                if( (viewModel.get('standardPrice') != 0 && !viewModel.get('standardPrice')) ) {
                    valueCheck('해당 ERP 연동 품목의 원가 Data 가 없습니다.', 'standardPrice');
                    return false;
                }

                if( ! viewModel.get('recommendedPrice') ) {
                    valueCheck('해당 ERP 연동 품목의 정상가 Data 가 없습니다.', 'recommendedPrice');
                    return false;
                }


            } else {  // ERP 미연동 품목의 가격정보 입력 여부 체크

//                if( ! viewModel.get('bosPriceApplyStartDate') ) {
//                    valueCheck('가격정보 적용 시작일이 정확히 입력되지 않았습니다.<br>가격정보를 확인해주세요.', 'bosPriceApplyStartDate');
//                    return false;
//                }
//
//                if( ! viewModel.get('bosStandardPrice') ) {
//                    valueCheck('원가가 입력되지 않았습니다.<br>가격정보를 확인해주세요.', 'bosStandardPrice');
//                    return false;
//                }
//
//                if( ! viewModel.get('bosRecommendedPrice') ) {
//                    valueCheck('정상가가 입력되지 않았습니다.<br>가격정보를 확인해주세요.', 'bosRecommendedPrice');
//                    return false;
//                }

            	if(viewModel.get("masterItemType") != "MASTER_ITEM_TYPE.RENTAL" ) {
            		if( ! viewModel.get('priceApplyStartDate') ) {
                        valueCheck('가격 정보 시작일이 지정되지 않았습니다.', 'fnItemPriceAddPopup');
                        return false;
                    }

                    if( (viewModel.get('standardPrice') != 0 && !viewModel.get('standardPrice')) ) {
                        valueCheck('원가가 입력되지 않았습니다.', 'fnItemPriceAddPopup');
                        return false;
                    }

                    if( ! viewModel.get('recommendedPrice') ) {
                        valueCheck('정상가가 입력되지 않았습니다.', 'fnItemPriceAddPopup');
                        return false;
                    }
            	}


            }

            if( viewModel.get('itemWarehouseList').length == 0 ) {
                valueCheck('추가된 출고처가 없습니다.', 'warehouse');
                return false;
            }
            else {
            	for (var i = 0; i < viewModel.get('itemWarehouseList').length; i++) {
                    var warehouseInfo = viewModel.get('itemWarehouseList')[i];

                    if (warehouseInfo.stockOrderYn == 'Y') {
                        if(undefined == warehouseInfo['itemWarehousePoTpIdTemplateList'] || null == warehouseInfo['itemWarehousePoTpIdTemplateList']) {
                            valueCheck(warehouseInfo.warehouseName+' 출고처의 발주유형(BOS)이 선택되지 않았습니다.', 'itemWarehousePoTpIdTemplateList'+warehouseInfo.urWarehouseId);
                            return false;
                        }
            		}
            	}
            }

            if( viewModel.get('itemImageList').length == 0 ) {
                valueCheck('추가된 상품 이미지가 없습니다.', 'itemImageAdd');
                $('input[name=shippingYn]:first').get(0).focus();
                return false;
            }

            // 렌탈 품목인경우 렌탈가격정보 Validation 추가.
            if(viewModel.get("masterItemType") == "MASTER_ITEM_TYPE.RENTAL" ) {
            	if(viewModel.get("rentalFeePerMonth") == "") {
                    valueCheck('렌탈료 / 월 금액을 입력하여 주세요.', 'rentalFeePerMonth');
                    return false;
            	}

            	if(viewModel.get("rentalDueMonth") == "") {
                    valueCheck('의무사용기간 / 월 을 입력하여 주세요.', 'rentalDueMonth');
                    return false;
            	}

            }

            // 승인요청처리 체크
            let approvalChkVal = $('input:checkbox[name="approvalCheckbox"]').is(":checked");	//승인요청 처리 여부
			let itemApprList = [];

			if(approvalChkVal) {
				itemApprList = $("#apprGrid").data("kendoGrid").dataSource.data()
			}

            if(approvalChkVal) {
            	if(itemApprList.length == 0) {
            		valueCheck('승인관리자 정보를 선택하여 주세요.', '');
                    return false;
            	}else {
            		viewModel.set("itemApprList", itemApprList);
            	}
            }

            viewModel.set("itemStatusTp", "ITEM_STATUS_TP.SAVE");
            break;

        case 'addNutritionDetail': // 영양정보 세부항목 추가 버튼 클릭

            var selectedNutritionCode = viewModel.get('selectedAddAvailableNutritionCode'); // 선택된 영양정보 분류코드
            var itemNutritionDetailList = viewModel.get('itemNutritionDetailList'); // 화면상의 영양정보 세부항목 목록

            for (var i = itemNutritionDetailList.length - 1; i >= 0; i--) { // itemNutritionDetailList 에 이미 추가된 영양정보 코드인 경우

                if (itemNutritionDetailList[i]['nutritionCode'] == selectedNutritionCode) {
                    valueCheck('이미 추가한 영양정보 항목입니다.', 'addAvailableNutritionCode');
                    return false;
                }

            }

            break;

        case 'addItemCertification': // 상품 인증정보 추가 버튼 클릭

            // 상품 인증정보 Select 에서 선택한 상품 인증정보 ID
            var selectedIlCertificationId = viewModel.get('selectedIlCertificationId');

            // 품목별 상품 인증정보 최대 등록가능 건수
            var itemCertificationMaxLimit = viewModel.get('itemCertificationMaxLimit');

            // 화면상에 출력된 출고처 목록
            var itemCertificationList = viewModel.get('itemCertificationList');

            // 이미 최대 등록가능 건수 도달시
            if (itemCertificationList.length >= itemCertificationMaxLimit) {
                valueCheck('상품 인증정보는 품목별 최대 ' + itemCertificationMaxLimit + ' 건까지 등록 가능합니다.', 'itemCertification');
                return false;
            }

            // 상품 인증정보 Select 에서 선택한 상품 인증정보 ID 에 해당하는 인증 정보가 itemCertificationList 에 이미 있는지 확인
            for (var i = itemCertificationList.length - 1; i >= 0; i--) {

                if (itemCertificationList[i]['ilCertificationId'] == selectedIlCertificationId) { // 이미 추가된 상품 인증정보 ID 값인 경우
                    valueCheck('이미 추가한 상품 인증정보 항목입니다.', 'itemCertification');
                    return false;
                }

            }

            break;

        case 'addWarehouse': // 출고처 추가 버튼 클릭

            // 출고처 그룹 Select 에서 선택된 출고처 그룹 코드
            var selectedWarehouseGroupCode = viewModel.get('selectedWarehouseGroupCode');

            // 출고처 Select 에서 선택한 공급업체-출고처 PK 값
            var selectedUrSupplierWarehouseId = viewModel.get('selectedUrSupplierWarehouseId');

            // 화면상에 출력된 출고처 목록
            var itemWarehouseList = viewModel.get('itemWarehouseList');

            // 출고처 Select 에서 선택한 공급업체-출고처 PK 값에 해당하는 출고처 정보가 itemWarehouseList 에 이미 있는지 확인
            for (var i = itemWarehouseList.length - 1; i >= 0; i--) {

                if (itemWarehouseList[i]['urSupplierWarehouseId'] == selectedUrSupplierWarehouseId) { // 이미 추가된 공급업체-출고처 PK 값인 경우
                    valueCheck('이미 추가한 출고처 항목입니다.', 'warehouse');
                    return false;
                }

            }

            break;

        } // end of switch

    }

    function fnClear() { // 전체 화면 초기화

        // 상품 영양정보 데이터 초기화
        viewModel.set('itemNutritionDetailList', []);

        // 상품 이미지 데이터 초기화
        viewModel.set('itemImageList', []);
        viewModel.set('itemImageFileList', []); // 기존 상품 이미지 파일 목록 초기화

        // 초기화 이전에 사용자가 선택한 마스터 품목 유형 값
        var beforeMasterItemType = viewModel.get('masterItemType');

        // 초기화 이전에 사용자가 선택한 ERP 품목 연동여부
        var beforeErpItemLinkYn = viewModel.get('erpItemLinkYn');

        /* 영양정보 상세 항목에 적용된 Kendo Sortable 강제 초기화 */
        $('#nutritionDetailContainer').remove();   // Kendo sortable 적용한 영양정보 html 태그 제거

        var nutritionDetailContainerTag = '<tbody';
        nutritionDetailContainerTag += ' id="nutritionDetailContainer"';
        nutritionDetailContainerTag += ' data-template="nutritionDetailList-row-template"';
        nutritionDetailContainerTag += ' data-bind="source: itemNutritionDetailList"';
        nutritionDetailContainerTag += ' ></tbody>';

        // 새로운 태그 값 생성 후 beforeNutritionDetailContainer 뒤에 태그 붙이기
        $('#beforeNutritionDetailContainer').after(nutritionDetailContainerTag);

        /* 상품 이미지 목록에 적용된 Kendo Sortable 강제 초기화 */
        $('#itemImageContainer').remove();   // Kendo sortable 적용한 상품 이미지 html 태그 제거

        var itemImageContainerTag = '<span';
        itemImageContainerTag += ' id="itemImageContainer"';
        itemImageContainerTag += ' style="display: flex; flex-wrap: wrap; justify-content: left;"';
        itemImageContainerTag += ' data-template="itemImage-row-template"';
        itemImageContainerTag += ' data-bind="source: itemImageList"';
        itemImageContainerTag += ' ></span>';

        // 새로운 태그 값 생성 후 beforeNutritionDetailContainer 뒤에 태그 붙이기
        $('#itemImageContainer').after(itemImageContainerTag);

        kendo.unbind($("#view"));  // 기존 viewModel binding 해제

        // viewModelOriginal 을 deepClone 한 객체로 viewModel 초기화
        viewModel = {};
        viewModel = kendo.observable( deepClone(viewModelOriginal) );

        // 상품 이미지 목록 tag 내 itemImageContainer span 추가
        var htmlText = '<span id="itemImageContainer" ';
        htmlText += 'style="display: flex; flex-wrap: wrap; justify-content: left;" ';
        htmlText += 'data-template="itemImage-row-template" ';
        htmlText += 'data-bind="source: itemImageList" ';
        htmlText += '></span>';

        $('#divContainer').append(htmlText);

        // initUI 함수 호출 : 내부에서 새로운 viewModel 을 html 화면에 바인딩, kendo sortable 등 적용
        initUI();

        viewModel.set('masterItemType', beforeMasterItemType); // 초기화 이전에 선택한 마스터 품목 유형 값 세팅
        viewModel.set('erpItemLinkYn', beforeErpItemLinkYn); // 초기화 이전에 선택한 ERP 품목 연동여부 값 세팅

        viewModel.onMasterItemTypeChange(null);  // 마스터 품목 유형 change Event 수동 호출
        viewModel.onErpItemLinkYnChange(null); // ERP 품목 연동여부 change Event 수동 호출

        setTimeout(fnInitItemMgm(), 1000); // Kendo Component 초기화

        // 데이터 마이그레이션 관련 로직 Start : 추후 삭제 예정
        fnShowMigrationDataButton();
        // 데이터 마이그레이션 관련 로직 End

    };

    /*
     * 메시지 팝업 호출 함수
     */
    function valueCheck(nullMsg, id) {
        fnKendoMessage({
            message : nullMsg,
            ok : function focusValue() {
            	var $obj = $('#' + id);
            	if($obj.data("kendoDropDownList")){
            		$obj.data("kendoDropDownList").focus();
            	} else if ($obj.data("kendoNumericTextBox")){
            		$obj.data("kendoNumericTextBox").focus();
            	} else {
            		$('#' + id).focus();
            	}
            }
        });

        return false;
    };

    // data 객체의 empty 여부 체크
    function checkDataEmpty(data) {
        return Object.keys(data).length === 0 && data.constructor === Object;
    }

    // 현재 선택된 표준 카테고리 Select 값 중 가장 마지막 값을 반환
    function checkCategoryStandardId() {

        var categoryStandardIdArray = [ 'bosCategoryStandardFirstId', 'bosCategoryStandardSecondId', 'bosCategoryStandardThirdId', 'bosCategoryStandardFourthId' ];
        var categoryDropdownIdArray = [ 'categoryStandardDepth1', 'categoryStandardDepth2', 'categoryStandardDepth3', 'categoryStandardDepth4' ];

        for (var i = categoryStandardIdArray.length - 1; i >= 0; i--) { // 카테고리 ID 배열을 역순으로 순환

            // 해당 카테고리 ID 의 dropDown 에서 유효한 값 선택시
            if (viewModel.get(categoryStandardIdArray[i]) != null && $.trim( viewModel.get(categoryStandardIdArray[i]) ) != '') {

                if( i == categoryStandardIdArray.length - 1 ) {   // 마지막 세분류 카테고리에서 유효한 값 선택시 : 해당 선택값을 바로 리턴
                    return viewModel.get(categoryStandardIdArray[i]);
                }

                // 현재 선택된 카테고리의 다음 카테고리에 선택 가능한 값 존재시
                if( $("#" + categoryDropdownIdArray[i+1] ).data("kendoDropDownList").dataSource._data.length > 0 ) {
                    return false;  // 하위 카테고리까지 선택해야 함

                } else {
                    return viewModel.get(categoryStandardIdArray[i]);

                }

            }

        }

    }

    // 자바스크립트 객체 deepCopy ( deepClone ) 메서드
    function deepClone(obj) {
        if(obj === null || typeof obj !== 'object') {
          return obj;
        }

        const result = Array.isArray(obj) ? [] : {};

        for(let key of Object.keys(obj)) {
          result[key] = deepClone(obj[key])
        }

        return result;
    }

    function fnInitItemMgm() { // Kendo Component 초기화

        // 숫자만 입력 허용 : 코드성 데이터이므로 0 으로 시작 허용
        fnInputValidationForNumber('bosItemBarCode'); // BOS 품목 바코드
        fnInputValidationForNumber('pdmGroupCode'); // PDM 그룹코드

// [HGRM-7736] 영양성분 기타 값 입력시 특수문자 입력제한 해제
//        fnInputValidationLimitSpecialCharacter('nutritionEtc');  // BOS 영양성분 기타 : 특수문자 입력 제한

        fnAjax({ // 마스터 품목 유형 Data 조회
            url : "/admin/comn/getCodeList",
            method : 'GET',
            params : {
                "stCommonCodeMasterCode" : "MASTER_ITEM_TYPE",
                "useYn" : "Y"
            },
            async : false,
            success : function(data, status, xhr) {

                data['rows'].forEach(function(item, index) {
                    if (item['CODE'] == "MASTER_ITEM_TYPE.ALL") {
                        data['rows'].splice(index, 1); // 공통 코드 중 '전체'는 제외
                    }
                });

                fnKendoDropDownList({
                    id : 'masterItemType', // ERP 품목 연동 여부
                    data : data['rows'],
                    valueField : 'CODE',
                    textField : 'NAME'
                });

            },
            isAction : 'select'

        });

        fnKendoDropDownList({
            id : 'erpItemLinkYn', // ERP 품목 연동 여부
            data : [ {
                code : 'Y',
                name : '연동 품목'
            }, {
                code : 'N',
                name : '미연동 품목'
            } ],
            valueField : 'code',
            textField : 'name'
        });

        // 기존 표준카테고리 dropdown 존재시 destroy
        if( $("#categoryStandardDepth1").data("kendoDropDownList") ) {
            $("#categoryStandardDepth1").data("kendoDropDownList").destroy();
        }

        if( $("#categoryStandardDepth2").data("kendoDropDownList") ) {
            $("#categoryStandardDepth2").data("kendoDropDownList").destroy();
        }

        if( $("#categoryStandardDepth3").data("kendoDropDownList") ) {
            $("#categoryStandardDepth3").data("kendoDropDownList").destroy();
        }

        if( $("#categoryStandardDepth4").data("kendoDropDownList") ) {
            $("#categoryStandardDepth4").data("kendoDropDownList").destroy();
        }

        // 표준카테고리 시작
        fnKendoDropDownList({
            id : "categoryStandardDepth1",
            tagId : "categoryStandardDepth1",
            url : "/admin/comn/getDropDownCategoryStandardList",
            params : { "depth" : "1" },
            textField : "categoryName",
            valueField : "categoryId",
            blank : "대분류",
            async : false
        });

        // 표준카테고리 중분류
        fnKendoDropDownList({
            id : "categoryStandardDepth2",
            tagId : "categoryStandardDepth2",
            url : "/admin/comn/getDropDownCategoryStandardList",
            textField : "categoryName",
            valueField : "categoryId",
            blank : "중분류",
            async : false,
            cscdId : "categoryStandardDepth1",
            cscdField : "categoryId"
        });

        // 표준카테고리 소분류
        fnKendoDropDownList({
            id : "categoryStandardDepth3",
            tagId : "categoryStandardDepth3",
            url : "/admin/comn/getDropDownCategoryStandardList",
            textField : "categoryName",
            valueField : "categoryId",
            blank : "소분류",
            async : false,
            cscdId : "categoryStandardDepth2",
            cscdField : "categoryId"
        });

        // 표준카테고리 세분류
        fnKendoDropDownList({
            id : "categoryStandardDepth4",
            tagId : "categoryStandardDepth4",
            url : "/admin/comn/getDropDownCategoryStandardList",
            textField : "categoryName",
            valueField : "categoryId",
            blank : "세분류",
            async : false,
            cscdId : "categoryStandardDepth3",
            cscdField : "categoryId"
        });
        // 표준카테고리 끝

        fnAjax({ // 공급업체 조회
            url : "/admin/ur/urCompany/getSupplierCompanyList",
            method : 'GET',
            success : function(data, status, xhr) {

                fnKendoDropDownList({ // BOS 공급업체
                    id : 'bosSupplier',
                    data : data['rows'],
                    textField : "supplierName",
                    valueField :"urSupplierId",
                    blank : "미지정"
                })

            },
            isAction : 'select'

        });

//        fnKendoDropDownList({
//            id : 'bosBrand', // BOS 브랜드 : 최초 화면 조회시에는 출력할 Data 없음
//            data : [ {
//                code : '',
//                name : '선택'
//            } ],
//            valueField : 'code',
//            textField : 'name'
//        });

        fnKendoDropDownList({
        	id : 'bosBrand',
            url : "/admin/ur/brand/searchBrandList",
            params : {
                searchUrSupplierId : '',  // 변경한 공급업체 ID 값
                searchUseYn : "Y"
            },
            textField : "brandName",
            valueField : "urBrandId",
            blank : '선택'
        });

        fnKendoDropDownList({
            id : 'dpBrand', 	// 전시 브랜드
            url : "/admin/ur/brand/searchDisplayBrandList",
            tagId : 'dpBrand',
            autoBind : true,
            valueField : 'dpBrandId',
            textField : 'dpBrandName',
            chkVal : '',
            style : {},
            blank : '선택',
            params      : {"useYn" :"Y"}
        });

        fnKendoDropDownList({
            id : 'bosStorageMethod', // 보관방법
            url : "/admin/comn/getCodeList",
            tagId : 'bosStorageMethod',
            autoBind : true,
            valueField : 'CODE',
            textField : 'NAME',
            chkVal : '',
            style : {},
            blank : '선택',
            params : {
                "stCommonCodeMasterCode" : "ERP_STORAGE_TYPE",
                "useYn" : "Y"
            }
        });

        fnKendoDropDownList({
            id : 'bosOriginType', // BOS 원산지
            url : "/admin/comn/getCodeList",
            tagId : 'bosOriginType',
            autoBind : true,
            valueField : 'CODE',
            textField : 'NAME',
            style : {},
            params : {
                "stCommonCodeMasterCode" : "ORIGIN_TYPE",
                "useYn" : "Y"
            },
        });

        viewModel.set('bosOriginType', 'ORIGIN_TYPE.DOMESTIC'); // 기본값 '국내' 선택

        fnAjax({   // BOS 원산지 상세 '해외' 원산지 Dropdown
            url : "/admin/comn/getCodeList",
            method : 'GET',
            params : {
                "stCommonCodeMasterCode" : "OVERSEAS",
                "useYn" : "Y",
                "orderBy" : "NAME"
            },
            async : false,
            isAction : 'select',
            success : function(data, status, xhr) {

                fnKendoDropDownList({
                    id : 'bosOriginDetailOverseas', // BOS 원산지 상세 '해외'
                    data : data['rows'],
                    valueField : 'CODE',
                    textField : 'NAME'
                });

                if( data['rows'] && data['rows'][0] ) {
                    viewModel.set('bosOriginDetailOverseas', data['rows'][0]['CODE']); // 첫 번째 Data 선택
                }

            },

        });

        fnAjax({
            url : "/admin/comn/getCodeList",
            method : 'GET',
            params : {
                "stCommonCodeMasterCode" : "UNIT_TYPE",
                "useYn" : "Y"
            },
            async : false,
            success : function(data, status, xhr) {

                fnKendoDropDownList({ // 용량(중량) 단위
                    id : 'sizeUnit',
                    data : data['rows'],
                    valueField : 'CODE',
                    textField : 'NAME',
                    blank : "단위 선택"
                })

                fnKendoDropDownList({
                    id : 'packageUnit', // 포장 구성 단위
                    data : data['rows'],
                    valueField : 'CODE',
                    textField : 'NAME',
                    blank : "단위 선택"
                })

            },
            isAction : 'select'
        });

        fnKendoDropDownList({
            id : 'taxYn', // 과세 구분
            data : [ {
                "CODE" : "Y",
                "NAME" : "과세"
            }, {
                "CODE" : "N",
                "NAME" : "면세"
            } ],
            valueField : 'CODE',
            textField : 'NAME',

        });

        viewModel.set('taxYn', 'Y');  // 마스터 품목 등록시 과세구분 기본 선택값 : '과세'

        fnKendoDatePicker({   // BOS 가격 적용 시작일
            id: "bosPriceApplyStartDate",
            format: "yyyy-MM-dd",
            min : fnGetToday(),
            change : function(e) {
            }
        });

        viewModel.set('bosPriceApplyStartDate', fnGetToday() );   // BOS 가격 적용 시작일 : 현재 일자 세팅

        fnKendoDropDownList({
            id : 'nutritionDisplayYn', // 영양정보 표시여부
            data : [ {
                CODE : 'Y',
                NAME : '노출'
            }, {
                CODE : 'N',
                NAME : '노출 안함'
            } ],
            valueField : 'CODE',
            textField : 'NAME'
        });


//        if( viewModel.get('isErpItemLink') == true ) {  // ERP 연동
//			viewModel.set('nutritionDisplayYn', 'Y'); // 영양정보 표시여부 기본값 '노출' 선택
//        } else { // ERP 미연동
//        	viewModel.set('nutritionDisplayYn', 'N'); // 영양정보 표시여부
//            viewModel.onNutritionDisplayYnChange(null); // 영양정보 표시여부 change 이벤트 수동 호출
//        }
		viewModel.set('nutritionDisplayYn', 'N'); // 영양정보 표시여부
		viewModel.onNutritionDisplayYnChange(null); // 영양정보 표시여부 change 이벤트 수동 호출

        if( viewModel.get('isErpItemLink') == true ) {  // 최초 ERP 연동 선택시에만 호출

            fnKendoDropDownList({ // 등록 가능한 영양정보 분류코드 리스트 : 최초 ERP 연동 선택 / 화면 조회시는 데이터 없음
                id : 'addAvailableNutritionCode',
                data : {},
                textField : "NAME",
                valueField : "CODE",
                blank : '선택'
            });

        }

        fnAjax({
            url : "/admin/item/master/register/getItemSpecCode",
            method : 'GET',
            params : {},
            async : false,
            success : function(data, status, xhr) {

                fnKendoDropDownList({ // 상품정보 제공고시 : 상품군
                    id : 'specGroup',
                    data : data['itemSpecCodeList'],
                    valueField : 'CODE',
                    textField : 'NAME',
                    blank : "선택"
                });

                fnGetItemSpecList(null, null); // 상품정보 제공고시 상세 항목 전체 조회

            },
            isAction : 'select'
        });

        fnAjax({
            url : "/admin/item/master/register/getItemCertificationCode",
            method : 'GET',
            params : {},
            async : false,
            success : function(data, status, xhr) {

                fnKendoDropDownList({ // 상품 인증정보 선택 Select
                    id : 'itemCertification',
                    data : data['itemCertificationCodeList'],
                    valueField : 'ilCertificationId',
                    textField : 'certificationName',
                    blank : '선택'
                });

                viewModel.set('itemCertificationCode', data['itemCertificationCodeList']);

                // viewModel 내 기존 선택된 상품인증정보 id 초기화
                viewModel.set('selectedIlCertificationId', '');

                // 상품 인증정보 선택 Select 값 변경시 필터링이 편리하도록 Map ( Javascript Object ) 으로 변환
                var itemCertificationCodeMap = {};

                for (var i = 0; i < data['itemCertificationCodeList'].length; i++) {
                    itemCertificationCodeMap[data['itemCertificationCodeList'][i]['ilCertificationId']] = data['itemCertificationCodeList'][i];
                }

                viewModel.set('itemCertificationCodeMap', itemCertificationCodeMap);

            },
            isAction : 'select'
        });

        fnKendoDropDownList({
            id : 'authenticationInformationSelect', // 인증정보 선택
            data : [ {
                code : '',
                name : '유기농산물'
            } ],
            valueField : 'code',
            textField : 'name'
        });

        fnKendoDropDownList({
            id : 'bosPoType', // BOS 발주 유형 : 최초 화면 조회시에는 출력되는 데이터 없음
            data : [],
            valueField : 'CODE',
            textField : 'NAME',
            blank : '선택'

        });

        $('#shippingYn').html(''); // 배송 불가지역 기존 html 초기화

        fnTagMkChkBox({ // 배송 불가 지역
            id : 'shippingYn',
            tagId : 'shippingYn',
            data : [ {
                "CODE" : 'islandShippingYn',
                "NAME" : "1권역(도서산간)"
            }, {
                "CODE" : 'jejuShippingYn',
                "NAME" : "2권역(제주)"
            } ],
            chkVal : '',
            style : {},
            change : function(e) {

                // 1권역, 2권역 체크박스 상태 변경값을 viewModel 에 반영
                // 화면에서는 배송불가 여부를 따지므로 값 반대로 세팅
                viewModel.set('islandShippingYn', ! $("input[name=shippingYn]:eq(0)").is(":checked"))
                viewModel.set('jejuShippingYn', ! $("input[name=shippingYn]:eq(1)").is(":checked"))

            }
        });

        // 최초 화면 조회시에는 1권역, 2권역 모두 체크 해제 (배송 가능) / viewModel 에는 반대로 세팅
        $("input[name=shippingYn]:eq(0)").prop('checked', false);
        viewModel.set('islandShippingYn', true);

        $("input[name=shippingYn]:eq(1)").prop('checked', false);
        viewModel.set('jejuShippingYn', true);

        fnKendoDropDownList({ // 단종여부
            id : 'extinctionYn',
            data : [ {
	              CODE : 'N',
	              NAME : '판매'
	          }, {
	              CODE : 'Y',
	              NAME : '판매불가'
	          } ],
        });

        var dropdownlist = $("#extinctionYn").data("kendoDropDownList").enable(false);

//        fnTagMkRadio({
//            id : 'extinctionYn', // 단종여부
//            data : [ {
//                CODE : 'N',
//                NAME : '아니오'
//            }, {
//                CODE : 'Y',
//                NAME : '예'
//            } ],
//            tagId : 'extinctionYn',
//            chkVal : 'N'
//        });

        // 단종여부 Radio 비활성화 처리
        $("#extinctionYn input:radio").attr('disabled', true);

        fnKendoDropDownList({ // 출고처 그룹
            id : 'warehouseGroup',
            data : {},
            textField : "NAME",
            valueField : "CODE",
            blank : '출고처 그룹'
        })

        fnKendoDropDownList({ // 출고처
            id : 'warehouse',
            data : {},
            textField : "warehouseName",
            valueField : "urSupplierWarehouseId",
            blank : '출고처 선택'
        })

        $("#warehouseGroup").data("kendoDropDownList").enable(false);  // 출고처 관련 Dropdown 최초 모두 비활성화
        $("#warehouse").data("kendoDropDownList").enable(false);

        fnItemDescriptionKendoEditor({ // 상품 상세 기본정보 Editor
            id : 'basicDescription',
        });

        fnItemDescriptionKendoEditor({ // 상품 상세 주요정보 Editor
            id : 'detaillDescription',
        });

        var tooltip = $("#tooltipSpan").kendoTooltip({ // 도움말 toolTip
            filter : "span",
            width : 300,
            position : "center",
            content: kendo.template($("#tooltip-template").html()),
            animation : {
                open : {
                    effects : "zoom",
                    duration : 150
                }
            }
        }).data("kendoTooltip");

        // 상품 이미지 첨부 File Tag 강제 초기화
        $('#uploadItemImageForm').html('');

        var htmlText = '<input type="file" ';
        htmlText += 'id="uploadItemImage" ';
        htmlText += 'name="uploadItemImage" ';
        htmlText += 'accept=".jpg, .jpeg, .gif" ';
        htmlText += '>';

        $('#uploadItemImageForm').append(htmlText);

        fnKendoUpload({ // 상품 이미지 첨부 File Tag 를 kendoUpload 로 초기화
            id : "uploadItemImage",
            select : function(e) {

                if (e.files && e.files[0]) { // 이미지 파일 선택시

                    if (itemImageUploadMaxLimit < e.files[0].size) { // 상품 이미지 업로드 용량 체크
                        fnKendoMessage({
                            message : '이미지 업로드 허용 최대 용량은 ' + parseInt(itemImageUploadMaxLimit / 1024) + ' kb 입니다.',
                            ok : function(e) {}
                        });
                        return;
                    }

                    var imageExtension = e.files[0]['extension'].toLowerCase();

                    // 업로드 가능한 이미지 확장자 목록에 포함되어 있는지 확인
                    if( allowedImageExtensionList.indexOf(imageExtension) < 0 ) {
                        fnKendoMessage({
                            message : '업로드 가능한 이미지 확장자가 아닙니다.',
                            ok : function(e) {}
                        });
                        return;
                    }

                    var itemImageFileList = viewModel.get('itemImageFileList');

                    for (var i = itemImageFileList.length - 1; i >= 0; i--) {
                        if (itemImageFileList[i]['name'] == e.files[0]['name']) { // 파일명 중복 체크
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

                        viewModel.get('itemImageFileList').push(file); // 추가된 상품 이미지 File 객체를 전역변수에 추가

                        viewModel.get('itemImageList').push({
                            basicYn : '', // 대표 이미지 여부 : 저장시 Backend 에서 지정됨
                            imagePath : file['name'], // local 에서 최초 추가한 이미지 : id 역할을 하는 imagePath 로 원본 파일명을 그대로 사용
                            imageOriginalName : file['name'], // 원본 File 명
                            sort : '', // 정렬순서
                            imageSrc : itemImageScr, // 상품 이미지 url
                        });

                        // 등록된 상품 이미지 개수 갱신
                        viewModel.set('itemImageCount', viewModel.get('itemImageList').length);

                        // 상품 이미지 최대 등록가능 개수 도달시 : 상품 이미지 추가 영역 숨김 처리
                        if (viewModel.get('itemImageList').length >= viewModel.get('itemImageMaxLimit')) {
                            $("#itemImageAdd").hide();
                        }

                    };

                    reader.readAsDataURL(e.files[0].rawFile);
                }
            }
        });

        fnKendoUpload({ // 상품 상세 기본 정보 / 주요정보 이미지 첨부 File Tag 를 kendoUpload 로 초기화
            id : "uploadImageOfEditor",
            select : function(e) {

                if (e.files && e.files[0]) { // 이미지 파일 선택시

                    // UPLOAD_IMAGE_SIZE : 이미지 업로드 / 첨부 최대 용량 ( 단위 : byte )
                    if (UPLOAD_IMAGE_SIZE < e.files[0].size) { // 용량 체크
                        fnKendoMessage({
                            message : '이미지 업로드 허용 최대 용량은 ' + parseInt(UPLOAD_IMAGE_SIZE / 1048576) + ' MB 입니다.',
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

        fnAjax({ // PDM 그룹코드 관련 : '잇슬림' 브랜드 코드 목록 조회
            url : "/admin/st/code/getCodeList",
            params : {
                "stCommonCodeMasterId" : "PDM_CATEGORY_DISP",
                "useYn" : "Y"
            },
            async : false,
            isAction : 'select',
            success : function(data, status, xhr) {

                if(data['rows'] && data['rows'].length > 0 ) {

                    data['rows'].forEach(function(item, index) {
                        viewModel.get('eatsslimBrandCodeList').push(item['commonCode']);  // '잇슬림' 브랜드 코드 취합
                    });

                } else {
                    viewModel.set('eatsslimBrandCodeList', []);
                }

            },

        });

    };

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
                    value : '<img src="' + imageSrcUrl + '" />'
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
            textAreaHtml += '></textarea>';

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
            //            pdf : {
            //                title : "상품상세 기본정보",
            //                fileName : "상품상세 기본정보.pdf",
            //                paperSize : 'A4',
            //                margin : {
            //                    bottom : '20 mm',
            //                    left : '20 mm',
            //                    right : '20 mm',
            //                    top : '20 mm'
            //                }
            //            },
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
            },
            resizable : true
        });

        $('<br/>').insertAfter($('.k-i-create-table').closest('li'));

    }

    // ------------------------------- Html 버튼 바인딩 Start
    // -------------------------------

    /** Common */
    $scope.fnSave = function() {
        fnSave();
    };

    $scope.fnErpItemSearchPopup = function() { // ERP 품목 검색 팝업
        fnErpItemSearchPopup();
    };

    $scope.fnMasterItemSearchPopup = function() { // 마스터 품목 검색 팝업
        fnMasterItemSearchPopup();
    };

    $scope.fnPoTypeDetailInformationPopup = function() { // 발주 상세정보 팝업
        fnPoTypeDetailInformationPopup();
    }

    $scope.fnAddAvailableNutritionCode = function() { // 등록 가능한 영양정보 세부항목 화면상에 추가 : 실제 저장은 아님
        fnAddAvailableNutritionCode();
    }

    $scope.fnRemoveNutritionDetailRow = function(rowId) { // 화면상에 추가된 영양정보 세부항목 삭제 : 실제 삭제는 아님
        fnRemoveNutritionDetailRow(rowId);
    }

    $scope.fnDecimalValidationByNumberOfDigits = function(event, limitCountUpperDecimalPoint, limitCountUnderDecimalPoint) {  // 영양정보 input 소수점 validation 체크
        setTimeout(() => fnDecimalValidationByNumberOfDigits(event, limitCountUpperDecimalPoint, limitCountUnderDecimalPoint), 1000);
    }

    // 데이터 마이그레이션 관련 로직 Start : 추후 삭제 예정
    $scope.fnSetMigrationData = function(event, limitCountUpperDecimalPoint, limitCountUnderDecimalPoint) {  // 영양정보 input 소수점 validation 체크
        fnSetMigrationData();
    }

    $scope.fnBosPoTypeChange = function(obj) {
    	fnBosPoTypeChange(obj);
    }

    $scope.fnItemPriceAddPopup = function(obj) {
    	fnItemPriceAddPopup(obj);
    }


    function fnBosPoTypeChange(obj) {

    	var poTpArray = $(obj).attr("id").split("itemWarehousePoTpIdTemplateList");

//    	for( var i = viewModel.get('itemPoTypeCodeList').length - 1 ; i >= 0 ; i -- ) {
//    		// 사용자가 선택한 발주유형 코드의 발주일 설정값 품목별 상이 여부 값이 true 인 경우
//    		if( viewModel.get('itemPoTypeCodeList')[i]['poTypeCode'] == $(obj).val()) {
//
//    			viewModel.set("erpPoTp", viewModel.get('itemPoTypeCodeList')[i]['erpPoTp']);
//
//    			$(obj).val(viewModel.get('itemPoTypeCodeList')[i]['poTypeCode']);
//
//    			if(viewModel.get('itemPoTypeCodeList')[i]['poPerItemYn'] == true ) {
//    				$("#poTypeDetailPopup"+poTpArray[1]).show(); 				// 발주유형 상세정보 popup 버튼 Visible
//    			} else {
//    				$("#poTypeDetailPopup"+poTpArray[1]).hide(); 				// 발주유형 상세정보 popup 버튼 Visible 해제
//    			}
//    			break;
//    		}
//    	}
    }


    function fnShowMigrationDataButton() {  // 데이터 마이그레이션 버튼 show / hide

        // ERP 품목 연동 여부 "연동 품목" 선택 && 마스터 품목유형 "공통" && ERP 품목 검색 후 ERP 품목코드 존재시
        if( viewModel.get('isErpItemLink') == true && viewModel.get('masterItemType') == 'MASTER_ITEM_TYPE.COMMON'//
            && viewModel.get('erpItemCode') && viewModel.get('erpItemCode').length > 0
        ) {

            $('#fnSetMigrationData').show(); // 해당 버튼 show

        } else {

            $('#fnSetMigrationData').hide();  // 해당 버튼 hide

        }
    }

    function fnItemPriceAddPopup( popupObj ) { // 가격 정보 전체 이력보기 팝업 호출

    	if (fnIsEmpty(viewModel.get('bosSupplier'))) {
            fnKendoMessage({
                message : '공급업체가 선택되지 않았습니다.'
            });
            return;
    	}

    	var popupId = $(popupObj).attr('id'); // 해당 가격정보 이력보기 팝업의 ID

        var priceParam = {};
        var standardPrice = viewModel.get("isErpItemLink") == true ? viewModel.get("erpStandardPrice") : viewModel.get('bosStandardPrice');
        var recommendedPrice = viewModel.get("isErpItemLink") == true ? viewModel.get("erpRecommendedPrice") : viewModel.get('bosRecommendedPrice');

        if(viewModel.get("standardPrice") != "") {
        	standardPrice = viewModel.get("standardPrice");
        }

        if(viewModel.get("recommendedPrice") != "") {
        	recommendedPrice = viewModel.get("recommendedPrice");
        }

        var taxYn = viewModel.get("isErpItemLink") == true ? viewModel.get("erpTaxYn") : viewModel.get("taxYn");
        var ratioCal = 0;
        if(taxYn == true) {
    		ratioCal = 1.1;
    	}else {
    		ratioCal = 1.0;
    	}
        var ratio = 0;
        if(standardPrice != undefined && standardPrice != ""  && recommendedPrice != undefined && recommendedPrice != "") {
        	ratio = Math.floor( (recommendedPrice - (standardPrice * ratioCal)) / recommendedPrice * 100);
        }


        if(viewModel.get('erpPriceApplyStartDate') == "") {
        	viewModel.set('erpPriceApplyStartDate', kendo.parseDate( fnGetToday("yyyy-MM-dd"), "yyyy-MM-dd"));
        }
        if(viewModel.get('bosPriceApplyStartDate') == "") {
        	viewModel.set('bosPriceApplyStartDate', kendo.parseDate( fnGetToday("yyyy-MM-dd"), "yyyy-MM-dd"));
        }
    	priceParam = {
        		ilItemPriceId : "",
        		ilItemCode : viewModel.get("isErpItemLink") == true ? viewModel.get("erpItemCode") : viewModel.get('bosItemCode'),
        		priceApplyStartDate : viewModel.get("isErpItemLink") == true ? viewModel.get("erpPriceApplyStartDate") : viewModel.get('bosPriceApplyStartDate'),
        		priceApplyEndDate : "2999-12-31",
        		standardPrice : standardPrice,
        	    recommendedPrice : recommendedPrice,
        	    priceRatio : ratio,
        	    approvalStatusCode : "APPR_STAT.NONE",
        	    approvalStatusCodeName : "승인대기",
        	    approval1st : "",
        	    approvalConfirm : ""
        };

        var priceOrigList = [];
        priceOrigList.push(priceParam);
        viewModel.set("itemPriceScheduleList", priceOrigList);

        var priceList = viewModel.get("itemPriceScheduleList");

        let params = {};
        params.itemInfo = {
        		ilItemCd : viewModel.get("isErpItemLink") == true ? viewModel.get("erpItemCode") : viewModel.get('bosItemCode') ,	 // 상품 ID
        		itemPriceList : priceList,
        		isErpItemLink: viewModel.get('isErpItemLink'),
        		erpLegalTypeCode : viewModel.get('legalType'),
        		productType : viewModel.get('productType'),
        		taxYn : viewModel.get("isErpItemLink") == true ? viewModel.get("erpTaxYn") : viewModel.get("taxYn"),
        		newInit : true,
        		standardPriceOrig : standardPrice,
        	    recommendedPriceOrig : recommendedPrice,
        	    urSupplierId : viewModel.get('bosSupplier'),
        };

        fnKendoPopup({
            id : popupId,
            title : 'BOS 가격정보',
            src : '#/itemPriceAddPopup',
            width : '1600px',
            height : '500px',
            param : params,
            success		: function( id, data ){
//            	var ilItemCode = viewModel.get("isErpItemLink") == true ? viewModel.get("erpItemCode") : viewModel.get('bosItemCode') ;


            	if(data.length > 0) {
            		viewModel.set("showItemPriceRow", true);

            		viewModel.set("approvalStatusCode", data[0].approvalStatusCode);
            		viewModel.set("approvalStatusCodeName", data[0].approvalStatusCodeName);
            		viewModel.set("priceApplyStartDate", data[0].priceApplyStartDate);
            		viewModel.set("priceApplyEndDate", data[0].priceApplyEndDate);
            		viewModel.set("priceApplyEndDate", data[0].priceApplyEndDate);
            		viewModel.set("standardPrice", data[0].standardPrice);
            		viewModel.set("recommendedPrice", data[0].recommendedPrice);
            		viewModel.set("priceRatio", data[0].priceRatio + "%");
            		viewModel.set("approval1st", data[0].approval1st);
            		viewModel.set("approvalConfirm", data[0].approvalConfirm);

            	}



            }
        });
    }

    function fnSetMigrationData() { // 데이터 마이그레이션 버튼 클릭시 화면에 마이그레이션 데이터 setting

        fnKendoMessage({
            type : "confirm",
            message : "해당 품목의 마이그레이션 데이터를 화면상에 출력합니다.<br>계속하시겠습니까?",
            ok : function() {

                fnAjax({
                    url : "/admin/item/master/migration/getMigrationItem",
                    method : 'GET',
                    params : {
                        ilItemCode : viewModel.get('erpItemCode') // ERP 연동 품목코드
                    },
                    isAction : 'select',
                    success : function(data, status, xhr) {

                        // 해당 품목 코드의 품목 정보 마이그레이션 데이터 미조회시
                        if( ! data['migrationItemVo'] ) {

                            fnKendoMessage({
                                message : '해당 품목의 마이그레이션 데이터가 존재하지 않습니다.',
                                ok : function focusValue() {
                                    return;
                                }
                            });

                        }

                        /*
                         * 해당 품목 코드의 품목 정보 마이그레이션 데이터 조회시
                         */
                        if( data['migrationItemVo'] ) {

                            var migrationItemData = data['migrationItemVo'];

                            if( migrationItemData['itemName'] ) {
                                viewModel.set('itemName', migrationItemData['itemName']);  // 마스터 품목명
                            }

                            if( migrationItemData['bosCategoryStandardFirstId'] ) {
                                viewModel.set('bosCategoryStandardFirstId', migrationItemData['bosCategoryStandardFirstId']); // 표준카테고리 대분류 ID
                            }

                            if( migrationItemData['bosCategoryStandardSecondId'] ) {
                                viewModel.set('bosCategoryStandardSecondId', migrationItemData['bosCategoryStandardSecondId']); // 표준카테고리 소분류 ID

                            }

                            if( migrationItemData['bosCategoryStandardThirdId'] ) {
                                viewModel.set('bosCategoryStandardThirdId', migrationItemData['bosCategoryStandardThirdId']); // 표준카테고리 중분류 ID

                            }

                            if( migrationItemData['bosCategoryStandardFourthId'] ) {
                                viewModel.set('bosCategoryStandardFourthId', migrationItemData['bosCategoryStandardFourthId']); // 표준카테고리 세분류 ID
                            }

                            if( migrationItemData['storageMethodType'] ) {
                                viewModel.set('bosStorageMethod', migrationItemData['storageMethodType']);  // 보관방법
                            }

                            if( migrationItemData['etcDescription'] ) {
                                viewModel.set('etcDescription', migrationItemData['etcDescription']); // 기타 정보
                            }

                            if( migrationItemData['nutritionQuantityPerOnce'] ) { // 영양분석 단위 (1회 분량)
                                viewModel.set('nutritionQuantityPerOnce', migrationItemData['nutritionQuantityPerOnce']);
                            }

                            if( migrationItemData['nutritionQuantityTotal'] ) { // 영양분석 단위 (총분량)
                                viewModel.set('nutritionQuantityTotal', migrationItemData['nutritionQuantityTotal']);
                            }

                            if( migrationItemData['nutritionEtc'] ) { // 영양성분 기타
                                viewModel.set('nutritionEtc', migrationItemData['nutritionEtc']);
                            }

                        }  // 품목 정보 마이그레이션 if 문 end

                       /*
                        * 해당 품목 코드의 상품정보 제공고시 정보 마이그레이션 Data 조회시
                        */
                       if (data['migrationItemSpecVoList'] && data['migrationItemSpecVoList'].length > 0) {

                           var itemSpecValueMap = {};

                           data['migrationItemSpecVoList'].forEach(function(item, index) {
                               itemSpecValueMap[item['ilSpecFieldId']] = item;
                           });

                           // viewModel 에 해당 품목 코드의 상품정보 제공고시 정보 마이그레이션 Data 세부 항목 Setting
                           viewModel.set('itemSpecValueMap', itemSpecValueMap);

                           // 해당 품목 코드의 품목 정보 마이그레이션 데이터 조회 && 데이터 내 상품정보제공고시 ID 존재시
                           if( data['migrationItemVo'] && data['migrationItemVo']['ilSpecMasterId'] ) {

                               var ilSpecMasterId = data['migrationItemVo']['ilSpecMasterId'];

                               // 상품정보 제공고시 분류 / 항목 조회 목록 : ERP 품목 검색 완료시 선조회됨
                               var itemSpecMap = viewModel.get('itemSpecMap');

                               // 해당 품목코드로 조회된 품목별 상품정보 제공고시 정보 마이그레이션 Data 세부 항목 존재시
                               if ( checkDataEmpty(itemSpecValueMap) == false ) {

                                   // itemSpecMap[ilSpecMasterId] : 마스터 품목으로 복사한 원본 품목의 상품정보 제공고시 상품군에 해당하는 코드 목록
                                   // => 최종적으로 원본 품목의 상품정보 제공고시 상세 항목의 값을 복사하여 화면에 출력함
                                   itemSpecMap[ilSpecMasterId].forEach(function(item, index) {

                                       var itemSpecValueObj = viewModel.get('itemSpecValueMap')[item['ilSpecFieldId']];

                                       // 품목별 상품정보 제공고시의 해당 항목에 별도 입력값 존재시
                                       if (itemSpecValueObj && itemSpecValueObj['specFieldValue']) {

                                           // 품목별 상품정보 제공고시 세부 항목의 값을 복사
                                           item['basicValue'] = itemSpecValueObj['specFieldValue'];

                                       }

                                   });

                               }

                               // 인자로 받은 상품정보 제공고시 분류코드를 viewModel 에 세팅 : 상품정보 제공고시의 상품군이 해당 코드로 선택됨
                               viewModel.set('selectedItemSpecMasterId', ilSpecMasterId);
                               viewModel.onSpecGroupChange(null); // 상품정보 제공고시의 상품군 change 이벤트 수동 호출

                           }

                       }  // 상품정보 제공고시 정보 마이그레이션 if 문 end

                       /*
                        * 해당 품목 코드의 영양 정보 마이그레이션 Data 조회시
                        */
                       if( data['migrationItemNutritionVoList'] && data['migrationItemNutritionVoList'].length > 0) {

                           // 화면에 기존 출력된 ERP 영양정보 목록 존재시 : 등록 가능 영양정보 분류코드에 다시 추가
                           if( viewModel.get('itemNutritionDetailList') && viewModel.get('itemNutritionDetailList').length > 0 ) {

                               for( var i = viewModel.get('itemNutritionDetailList').length - 1 ; i >= 0 ; i-- ) {

                                   viewModel.get('addAvailableNutritionCodeList').push({
                                       nutritionCode : viewModel.get('itemNutritionDetailList')[i]['nutritionCode'], // 영앙정보 분류코드
                                       nutritionName : viewModel.get('itemNutritionDetailList')[i]['nutritionCodeName'], // 영양정보 분류명
                                       nutritionUnit : viewModel.get('itemNutritionDetailList')[i]['nutritionUnit'], // 영양정보 분류단위
                                       nutritionPercentYn : viewModel.get('itemNutritionDetailList')[i]['nutritionPercentYn']  // 영양소 기준치 (%) 사용여부
                                   });

                               }

                           }

                           viewModel.set('itemNutritionDetailList', []); // 기존 조회된 영양정보 세부항목 초기화

                           var migrationItemNutritionList = data['migrationItemNutritionVoList'];

                           // 해당 품목 코드의 영양 정보 마이그레이션 Data 를 itemNutritionDetailList 에 추가
                           for (var i = 0; i < migrationItemNutritionList.length; i++) {

                               var nutritionDetailInfo = migrationItemNutritionList[i];
                               var nutritionCode = nutritionDetailInfo['nutritionCode'];

                               // 분류코드명 : 영양정보 분류코드 Map 을 조회하여 세팅
                               var nutritionCodeName = addAvailableNutritionCodeMap[nutritionCode];
                               var nutritionUnit = (addAvailableNutritionUnitMap[nutritionCode] ? addAvailableNutritionUnitMap[nutritionCode] : ''); //단위 미등록시는 '' 출력

                               // BOS 에서 영양소 기준치 % 사용여부 : addAvailableNutritionUnitMap 에 등록된 경우 true
                               var nutritionPercentYn = (addAvailableNutritionPercentMap[nutritionCode] ? true : false);

                               // ERP 영양정보 API 를 통해 조회한 해당 품목코드의 ERP 영양정보 값 ( 해당 영양분류코드로 데이터 없는 경우 '' 출력 )
                               var erpNutritionQuantity = (viewModel.get('erpNutritionApiObj.' + nutritionCode) ? viewModel.get('erpNutritionApiObj.' + nutritionCode) : '');

                               // ERP 영양정보 API 를 통해 조회한 ERP 영양소 기준치 % 값
                               var erpNutritionPercent = "";

                               // 영양정보 API 에서 기준치대비 함량 값은 'calories' => 'caloriesPercent' 와 같이 key 로 세팅
                               // 영양소 기준치 % 사용하고 인자로 받은 nutritionCode 의 Key 가 API 로 조회된 Data 내에 있는 경우
                               if ( nutritionPercentYn == true && viewModel.get('erpNutritionApiObj')[nutritionCode] ) {
                                   erpNutritionPercent = (viewModel.get('erpNutritionApiObj.' + nutritionCode + 'Percent') ? viewModel.get('erpNutritionApiObj.' + nutritionCode + 'Percent') : '');
                               }

                               viewModel.get('itemNutritionDetailList').push({
                                   canDeleted : true, // 삭제 버튼 노출 여부 : 영양정보 마이그레이션 상세항목은 모두 삭제 가능
                                   nutritionCodeName : nutritionCodeName, // 영양정보 분류코드명
                                   nutritionCode : nutritionCode, // 영양정보 분류코드
                                   erpNutritionQuantity : erpNutritionQuantity, // ERP 영양성분량
                                   nutritionQuantity : nutritionDetailInfo['nutritionQuantity'], // BOS 영양성분량
                                   nutritionUnit : nutritionUnit, // BOS 영양성분량 단위
                                   erpNutritionPercent : erpNutritionPercent, // ERP 영양성분 기준치대비 함량
                                   nutritionPercent : nutritionDetailInfo['nutritionPercent'], // BOS 영양성분 기준치대비 함량
                                   nutritionPercentYn : nutritionPercentYn, // BOS 에서 영양소 기준치 % 사용여부
                               });

                               // 상품 영양정보 항목에 추가된 항목은 등록 가능한 영양정보 분류코드 목록에서 삭제
                               for( var j = viewModel.get('addAvailableNutritionCodeList').length - 1 ; j >= 0 ; j -- ) {

                                   if( viewModel.get('addAvailableNutritionCodeList')[j]['nutritionCode'] == nutritionCode ) {
                                       viewModel.get("addAvailableNutritionCodeList").splice(j, 1);
                                   }

                               }

                               // 영양정보 분류코드 Select 의 첫번째 행으로 선택되도록 viewModel 에 세팅
                               if( viewModel.get('addAvailableNutritionCodeList')[0] ) {
                                   viewModel.set('selectedAddAvailableNutritionCode', viewModel.get('addAvailableNutritionCodeList')[0]['nutritionCode']);
                               }

                           }

                       } // 영양 정보 마이그레이션 if 문 end

                    }
                });

            },
            cancel : function() {
            }
        });

    }
    // 데이터 마이그레이션 관련 로직 End

    function fnApproInit(){
        //승인관리자 정보
        $('#approvalCheckbox').prop("checked",true);
        $('#apprDiv').show();

        //승인 요청처리 여부
        $("#approvalCheckbox").click(function(){
              if($("#approvalCheckbox").prop("checked") == true){
                    $('#approvalCheckbox').prop("checked",true);
                    $('#apprDiv').show();
              }else{
                    $('#approvalCheckbox').prop("checked",false);
                    $('#apprDiv').hide();
              }
        });
    }

 // 승인관리자 그리드 초기화
    function fnApprClear(){
          $('#apprGrid').gridClear(true);
          $('#apprSubUserId').val('');
          $('#apprUserId').val('');
    }

    //승인관리자 정보 Grid
    function fnApprAdminInit(){
          apprAdminGridDs =  new kendo.data.DataSource();

          apprAdminGridOpt = {
                dataSource : apprAdminGridDs,
                editable : false,
                noRecordMsg: '승인관리자를 선택해 주세요.',
                columns : [{
                      field : 'apprAdminInfo',
                      title : '승인관리자정보',
                      width : '100px',
                      attributes : {style : 'text-align:center'}
                      },{
                      field : 'adminTypeName',
                      title : '계정유형',
                      width : '100px',
                      attributes : {style : 'text-align:center'}
                      },{
                            field : 'apprUserName',
                            title : '관리자이름/아이디',
                            width : '100px',
                            attributes : {style : 'text-align:center'},
                            template : function(dataItem){
                                  let returnValue;
                                  returnValue = dataItem.apprUserName + '/' + dataItem.apprLoginId;
                                  return returnValue;
                            }
                      },{
                            field : 'organizationName',
                            title : '조직/거래처 정보',
                            width : '100px',
                            attributes : {style : 'text-align:center'}
                      },{
                            field : 'teamLeaderYn',
                            title : '조직장여부',
                            width : '80px',
                            attributes : {style : 'text-align:center'}
                      },{
                            field : 'userStatusName',
                            title : 'BOS 계정상태',
                            width : '80px',
                            attributes : {style : 'text-align:center'}
                      },{
                            field : 'grantUserName',
                            title : '권한위임정보<BR/>(이름/ID)',
                            width : '100px',
                            attributes : {style : 'text-align:center'},
                            template : function(dataItem){
                                  let returnValue;
                                  if(dataItem.grantAuthYn == 'Y'){
                                        returnValue = dataItem.grantUserName + '/' + dataItem.grantLoginId;
                                  }else{
                                        returnValue = '';
                                  }
                                  return returnValue;
                            }
                      },{
                            field : 'userStatusName',
                            title : '권한위임기간',
                            width : '150px',
                            attributes : {style : 'text-align:left'},
                            template : function(dataItem){
                                  let returnValue;
                                  if(dataItem.grantAuthYn == 'Y'){
                                        returnValue = dataItem.grantAuthStartDt + '~' + dataItem.grantAuthEndDt;
                                  }else{
                                        returnValue = '';
                                  }
                                  return returnValue;
                            }
                      },{
                            field : 'grantUserStatusName',
                            title : '권한위임자<BR/>BOS 계정상태',
                            width : '100px',
                            attributes : {style : 'text-align:left'},
                            template : function(dataItem){
                                  let returnValue;
                                  if(dataItem.grantAuthYn == 'Y'){
                                        returnValue = dataItem.grantUserStatusName;
                                  }else{
                                        returnValue = '';
                                  }
                                  return returnValue;
                            }
                      },{
                            field:'addCoverageId', hidden:true
                      },{
                            field:'includeYn', hidden:true
                      }
                ]
          };

          apprAdminGrid = $('#apprGrid').initializeKendoGrid(apprAdminGridOpt).cKendoGrid();
    }

    function fnItemAuthInputAllow() {
		let companyType = PG_SESSION.companyType		// 회사타입
		let clientType = PG_SESSION.clientType;			// 거래처타입

		if(companyType == "COMPANY_TYPE.HEADQUARTERS") {
			apprKindTp = "APPR_KIND_TP.ITEM_REGIST";
		}
		else if(companyType == "COMPANY_TYPE.CLIENT" && clientType == "CLIENT_TYPE.CLIENT") {
			apprKindTp = "APPR_KIND_TP.ITEM_CLIENT";
//			$("#inputForm input").attr("disabled", true);
//			$("#inputForm button").attr("disabled", true);
//
//			$(".clientAllow").attr("disabled", false);
//			fnInitSaleStatusRadio();
		}

		viewModel.set("apprKindTp", apprKindTp);
	}


    // 승인관리자 선택 팝업 호출
    function fnApprAdmin(){
          var param = {'taskCode' : apprKindTp };
          fnKendoPopup({
                id          : 'approvalManagerSearchPopup',
                title : '승인관리자 선택',
                src         : '#/approvalManagerSearchPopup',
                param : param,
                width : '1300px',
                heigh : '800px',
                scrollable : "yes",
                success: function( stMenuId, data ){

                      if(data && !fnIsEmpty(data) && data.authManager2nd){
                            $('#apprGrid').gridClear(true);

                            var authManager1 = data.authManager1st;
                            var authManager2 = data.authManager2nd;

                            if(authManager1.apprUserId != undefined){                                     //1차 승인관리자가 미지정이라면
                                  var objManager1 = new Object();

                                  objManager1["apprAdminInfo"] = "1차 승인관리자";
                                  objManager1["adminTypeName"] = authManager1.adminTypeName;
                                  objManager1["apprUserName"] = authManager1.apprUserName;
                                  objManager1["apprKindTp"] = authManager1.apprKindType;
                                  objManager1["apprManagerTp"] = authManager1.apprManagerType
                                  objManager1["apprUserId"] = authManager1.apprUserId;
                                  objManager1["apprLoginId"] = authManager1.apprLoginId;
                                  objManager1["organizationName"] = authManager1.organizationName;
                                  objManager1["userStatusName"] = authManager1.userStatusName;
                                  objManager1["teamLeaderYn"] = authManager1.teamLeaderYn;
                                  objManager1["grantAuthYn"] = authManager1.grantAuthYn;
                                  objManager1["grantUserName"] = authManager1.grantUserName;
                                  objManager1["grantLoginId"] = authManager1.grantLoginId;
                                  objManager1["grantAuthStartDt"] = authManager1.grantAuthStartDt;
                                  objManager1["grantAuthEndDt"] = authManager1.grantAuthEndDt;
                                  objManager1["grantUserStatusName"] = authManager1.grantUserStatusName;
                                  apprAdminGridDs.add(objManager1);
                            }

                            var objManager2 = new Object();

                            objManager2["apprAdminInfo"] = "2차 승인관리자";
                            objManager2["adminTypeName"] = authManager2.adminTypeName;
                            objManager2["apprUserName"] = authManager2.apprUserName;
                            objManager2["apprKindTp"] = authManager2.apprKindType;
                            objManager2["apprManagerTp"] = authManager2.apprManagerType
                            objManager2["apprUserId"] = authManager2.apprUserId;
                            objManager2["apprLoginId"] = authManager2.apprLoginId;
                            objManager2["organizationName"] = authManager2.organizationName;
                            objManager2["userStatusName"] = authManager2.userStatusName;
                            objManager2["teamLeaderYn"] = authManager2.teamLeaderYn;
                            objManager2["grantAuthYn"] = authManager2.grantAuthYn;
                            objManager2["grantUserName"] = authManager2.grantUserName;
                            objManager2["grantLoginId"] = authManager2.grantLoginId;
                            objManager2["grantAuthStartDt"] = authManager2.grantAuthStartDt;
                            objManager2["grantAuthEndDt"] = authManager2.grantAuthEndDt;
                            objManager2["grantUserStatusName"] = authManager2.grantUserStatusName;
                            apprAdminGridDs.add(objManager2);
                      }
                }
          });
    }


    function fnApprCancel() {
        if( viewModel.ilItemApprId == null && viewModel.ilItemApprId == undefined){
              fnKendoMessage({ message : "승인 내역에 문제가 있습니다. 관리자에게 문의하세요." });
              return;
        }

        let params = {};
        let url = "";

        if(PG_SESSION.companyType == "COMPANY_TYPE.HEADQUARTERS"){
              url = "/admin/approval/goods/putCancelRequestApprovalGoodsRegist";
        }
        else if(PG_SESSION.companyType == "COMPANY_TYPE.CLIENT" && PG_SESSION.clientType == "CLIENT_TYPE.CLIENT"){
              url = "/admin/approval/goods/putCancelRequestApprovalGoodsClient";
        }

        params.ilItemApprIdList = [];
        params.ilItemApprIdList[0] = viewModel.ilItmApprId;

        fnKendoMessage({
              type : "confirm",
              message : "요청철회 하시겠습니까?",
              ok : function() {
                    fnAjax({
                          url               : url,
                          params            : params,
                          contentType : "application/json",
                          success           : function( data ){
                                fnKendoMessage({  message : "요청철회가 완료되었습니다."
                                      , ok : function(){
                                            window.location.reload(true);
                                      }
                                });
                          },
                          fail : function(data, code){
                                fnKendoMessage({
                                      message : code.message,
                                      ok : function(e) {
                                            window.location.reload(true);
                                      }
                                });
                          },
                          error       : function(xhr, status, strError){
                                fnKendoMessage({ message : xhr.responseText });
                          },
                          isAction : "update"
                    });
              }
        });
    }
    // ------------------------------- Html 버튼 바인딩 End


    // -------------------------------
    $scope.fnApprAdmin = function(){fnApprAdmin();};                        //승인관리자 지증
    $scope.fnApprClear = function(){fnApprClear();};                        //승인관리자 초기화
    $scope.fnApprCancel = function(){fnApprCancel();};                      //요청철회
    $scope.fnApprDetailPopup = function() {fnApprDetailPopup();};     //승인내역 상세보기 팝업
}); // document ready - END
