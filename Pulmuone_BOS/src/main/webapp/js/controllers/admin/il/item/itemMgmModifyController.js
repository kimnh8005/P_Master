/*******************************************************************************************************************************************************************************************************
 * --------------- description : 품목/재고 관리 - 마스터 품목 수정 @ ----------------------------
 ******************************************************************************************************************************************************************************************************/
'use strict';

var pageParam = fnGetPageParam(); // GET 방식으로 전달된 parameter 객체

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

var sizeUnitDirectInputCode = "UNIT_TYPE.DIRECT_INPUT"; // 용량(중량) 단위 Dropdown 의 "직접입력" 코드값
var packageUnitDirectInputCode = "UNIT_TYPE.DIRECT_INPUT"; // 포장구성 단위 Dropdown 의 "직접입력" 코드값

var apprAdminGridDs, apprAdminGridOpt, apprAdminGrid;

var viewModel;  // Kendo viewModel
var apprKindTp;
$(document).ready(function() {

	let loadDate = new Date();
	let loadDateTime = loadDate.oFormat("yyyy-MM-dd HH:mm:ss");

    // 마스터 품목 유형, ERP 연동 여부, 품목 코드 중 하나라도 전달되지 않은 경우 : 마스터 품목 리스트 화면으로 이동
    if( !pageParam || !pageParam['masterItemType'] || !pageParam['isErpItemLink'] || !pageParam['ilItemCode'] ) {

        fnKendoMessage({
            message : '필수 파라미터가 전달되지 않았습니다.<br>마스터 품목 리스트 화면으로 이동합니다.',
            ok : function focusValue() {

                var option = new Object();

                option.url = "#/item";   // 마스터 품목 리스트 화면 URL
                option.menuId = 721;     // 마스터 품목 리스트 메뉴 ID

                fnGoPage(option);
            }
        });
        return;

    }

    /*
     * Kendo UI MVVM Pattern 적용
     */
    var viewModelOriginal = {

            /* 기본 정보 */
            // 품목 유형 / 품목 코드 / ERP 품목 연동 여부
            masterItemType : pageParam['masterItemType'], // 마스터 품목 유형
            erpItemCode : ( pageParam['isErpItemLink'] == 'true' ? pageParam['ilItemCode'] : ''  ), // ERP 품목코드
            bosItemCode : ( pageParam['isErpItemLink'] == 'false' ? pageParam['ilItemCode'] : ''  ), // BOS 품목코드
            isErpItemLink : ( pageParam['isErpItemLink'] == 'true' ? true : false  ), // ERP 품목 연동여부 flag : 연동시 true, 미연동시 false
            isNotErpItemLink : ( pageParam['isErpItemLink'] == 'true' ? false : true )  , // ERP 품목 미연동여부 flag: 연동시 false, 미연동시 true
            erpItemLinkYn : ( pageParam['isErpItemLink'] == 'true' ? 'Y' : 'N'  ), // ERP 품목 연동여부 : 기본 선택값 '연동'
            registerInfo : '', // 등록정보
            recentUpdateInfoByUser : '', // 사용자에 의한 최근 업데이트 내역
            recentUpdateInfoBySystem : '', // 시스템에 의한 최근 업데이트 내역
            erpItemName : '', // ERP 품목명
            erpItemBarCode : '', // ERP 품목 바코드
            bosItemBarCode : '', // BOS 품목 바코드
            o2oExposureType : '', // 매장 품목유형 ( ERP 연동 품목 중 올가 ERP 전용 )
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

            /* 상세 정보 */
            erpLegalTypeCode : '', // ERP 법인코드
            bosSupplier : '', // BOS 공급업체
            erpSupplierName : '',  // ERP 생산처
            erpBrand : '', // ERP 브랜드
            bosBrand : '', // BOS 브랜드
            dpBrand : '', // 전시 브랜드
            eatsslimBrandCodeList : [], // '잇슬림' 브랜드 코드 목록
            onBosBrandSelect : function(e) { // BOS 브랜드 Select 이벤트

                if( viewModel.get('bosSupplier') == pdmSupplierCode ) { // 공급업체가 PDM ( "풀무원 녹즙2" ) 인 경우

                    var isEatsslimBrand = false;  // '잇슬림' 브랜드 여부 : 기본값 false ( '잇슬림' 브랜드 아님 )

                    for( var i = viewModel.get('eatsslimBrandCodeList').length - 1 ; i >= 0 ; i -- ) {

                        if( e.dataItem.urBrandId == viewModel.get('eatsslimBrandCodeList')[i] ) { // '잇슬림' 브랜드 목록에 있는 경우
                            isEatsslimBrand = true;
                            break;
                        }

                    }

                    viewModel.set('isPdmGroupCodeDisabled', false); // PDM 그룹코드 입력 가능 ( 비활성화 해제 )

//                    if( isEatsslimBrand ) { // '잇슬림' 브랜드인 경우
//
//                        viewModel.set('isPdmGroupCodeDisabled', false); // PDM 그룹코드 입력 가능 ( 비활성화 해제 )
//
//                    } else { // '잇슬림' 브랜드가 아닌 경우
//
//                        viewModel.set('isPdmGroupCodeDisabled', true); // PDM 그룹코드 입력 불가능 ( 비활성화 )
//                        viewModel.set('pdmGroupCode', ''); // 기존 PDM 그룹 코드 입력값 초기화
//
//                    }

                }

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

                    if( e != null ) {  // onBosOriginChange 수동 호출이 아닌 경우 원산지 상세 Input 에 Focus
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
            erpTaxYn : '', // ERP 과세구분
            erpTaxInvoiceType : '', // ERP 연동 품목 검색 데이터 중 세금계산서 발행구분
            erpTaxType : '', // ERP 과세구분 (과세[영업사용], 서울-면세일반, 본점-과세일반, 면세[영업사용], NULL)

            showItemPriceAddRow : false, // ERP 미연동 품목 : 통합가격정보의 가격 입력 행 Visible 여부

            newPriceApplyStartDate : '', // 가격 적용 시작일 신규 입력값
            newPriceApplyEndDate : '', // 가격 적용 종료일 신규 입력값 ( 사용하지 않음 )
            newStandardPrice : '', // 표준단가, 원가 신규 입력값
            newRecommendedPrice : '', // 권장소비자가, 정상가 신규 입력값
            isRetalEditDisabled : true,
            rentalFeePerMonth : '',
            rentalFeePerMonthOrig : '',
            rentalDueMonth : '',
            rentalDeposit : '',
            rentalInstallFee : '',
            rentalRegistFee : '',
            rentalTotalPrice : '',
            rentalInfoChange : function(e) {
            	fnSetRentalTotalPrice();
            },
            onNewPriceChange : function(e) { // 원가, 정상가 input 가격 change event

                // 정상가가 원가보다 작은 값으로 입력될 경우
                if( parseInt( this.newStandardPrice ) > parseInt(this.newRecommendedPrice) ) {

                    fnKendoMessage({
                        message : '정상가는 원가보다 낮게 입력할 수 없습니다.<br>가격정보를 확인해주세요',
                        ok : function focusValue() {

                            // 정상가 / 원가 기존 입력값 모두 초기화
                            viewModel.set( 'newStandardPrice', '' );
                            viewModel.set( 'newRecommendedPrice', '' );

                        }
                    });

                }

            },

            onErpNewRecommendedPriceChange : function(e) { // 원가가 고정값인 경우 정상가 input 가격 change event

                // 정상가가 원가보다 작은 값으로 입력될 경우
                if( parseInt( this.newStandardPrice ) > parseInt(this.newRecommendedPrice) ) {

                    fnKendoMessage({
                        message : '정상가는 원가보다 낮게 입력할 수 없습니다.<br>가격정보를 확인해주세요',
                        ok : function focusValue() {

                            // 정상가 기존 입력값 모두 초기화
                            viewModel.set( 'newRecommendedPrice', '' );

                        }
                    });

                }

            },

            // 조회된 품목의 가격정보 현재/예정 목록 변수명 ( "orgaErpItemPriceScheduleList" ~ "erpNotLinkItemPriceScheduleList" )
            // 가격정보 삭제 / 신규 가격정보 추가시 해당 변수명 사용
            currentPriceScheduleListName : '',

            showRentalPriceVisible : false, // 렌탈품목 가격정보
            showItemPrice : false,
            itemPriceScheduleList : [],
            itemPriceScheduleAfterList : [],
            approvalTarget : '',
            /*
            orgaErpItemPriceScheduleList : [],   // 올가 ERP 가격정보 현재/예정 목록
            showOrgaErpItemPrice : false,       // 올가 ERP 가격정보 visible 여부

            foodmerceErpItemPriceScheduleList : [],  // 푸드머스 ERP 가격정보 현재/예정 목록
            showFoodmerceErpItemPrice : false,       // 푸드머스 ERP 가격정보 visible 여부

            lohasProductsErpItemPriceScheduleList : [],  // 건강생활 ERP 중 제품 가격정보 현재/예정 목록
            showLohasProductsErpItemPrice : false,       // 건강생활 ERP 중 제품 가격정보 visible 여부

            lohasGoodsErpItemPriceScheduleList : [],  // 건강생활 ERP 중 상품 가격정보 현재/예정 목록
            showLohasGoodsErpItemPrice : false,       // 건강생활 ERP 중 상품 가격정보 visible 여부

            erpLinkItemPriceScheduleList : [],    // 기타 ERP 연동 품목의 가격정보 현재/예정 목록
            showErpLinkItemPrice : false,        //  기타 ERP 연동 품목의 가격정보 visible 여부

            erpNotLinkItemPriceScheduleList : [], // ERP 미연동 품목의 가격정보 현재/예정 목록
			*/

            itemDiscountPriceList : [],    // ERP 품목 DISCOUNT 가격정보 목록
            showItemDiscountPrice : false,     // ERP 품목 DISCOUNT 가격정보 visible 여부

            showItemPriceAddBtn : false,

            priceApplyStartDateListToDelete : [],  // 해당 품목 가격정보에서 삭제할 가격의 시작일자 목록
            fnRemovePriceHistory : function(e) { // 가격정보 현재/예정 목록에서 해당 가격정보 삭제

                fnKendoMessage({
                    message : '삭제하시겠습니까?',
                    type    : "confirm",
                    ok : function focusValue() {

                        // viewModel 에 등록된 현재 가격정보 현재/예정 목록 변수명
                        var currentPriceScheduleListName = viewModel.get('currentPriceScheduleListName');

                        var index = viewModel.get(currentPriceScheduleListName).indexOf(e.data); // e.data : "삭제" 버튼을 클릭한 행의 영양정보 세부항목
                        viewModel.get(currentPriceScheduleListName).splice(index, 1); // viewModel 에서 삭제

                        viewModel.get('priceApplyStartDateListToDelete').push(e.data['priceApplyStartDate']);

                    },
                    cancel : function(e){}


                });

            },
            fnRemoveNewPrice : function(e) { //

                fnKendoMessage({
                    message : '삭제하시겠습니까?',
                    ok : function focusValue() {

                        viewModel.set( 'newPriceApplyStartDate', '' ); // 기존 가격 입력값 모두 초기화
                        viewModel.set( 'newPriceApplyEndDate', '' );
                        viewModel.set( 'newStandardPrice', '' );
                        viewModel.set( 'newRecommendedPrice', '' );

                        viewModel.set('showItemPriceAddRow', false);  // 가격정보 신규 입력 행 visible 해제

                    }
                });

            },

            /* 영양 정보 */
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
                var selectedItemSpecMasterId = viewModel.get('selectedItemSpecMasterId');

                if (selectedItemSpecMasterId) { // 유효한 상품정보제공고시 분류 PK 선택시

                    // 현재 선택된 상품정보제공고시 분류 PK 에 해당하는 상세항목 목록을 itemSpecValueList 에 추가
                    var itemSpecValueListOfChosenItemSpecMasterId = viewModel.get('itemSpecMap')[selectedItemSpecMasterId];

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

                viewModel.setSpecFieldModifiedMessage(null);  // 상품정보 제공고시 상세 메시지 출력 이벤트 수동 호출

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
            selectedBosPoType : '', // ERP 연동 품목에서만 조회됨 : 발주유형 Select 에서 선택한 발주유형
            itemPoTypeCodeList : [], // BOS 발주유형 코드 목록
            poTypeDetailInformationPopupVisible : false, // 발주유형 상세정보 popup 버튼 visible 여부
            erpPoTp : '', // 발주유형
            ilPoTpId : '',
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
                    	if(data['returnPeriodMessage'] == "null 일") {
                    		data['returnPeriodMessage'] = "없음";
                    	}
                        viewModel.set('returnPeriod', data['returnPeriodMessage']);  // 반품 가능기간 메시지 표시

                    }
                });

            },

            /* 출고처 정보 */
            showAddWarehouse: pageParam['masterItemType'] == 'MASTER_ITEM_TYPE.SHOP_ONLY' ? false : true, // 출고처 등록 Visiable 여부
            selectedWarehouseGroupCode : '', // 출고처 그룹 Select 에서 선택된 출고처 코드 그룹 값
            selectedUrSupplierWarehouseId : '', // 출고처 Select 에서 선택된 공급업체-출고처 PK 값
            itemWarehouseMap : {}, // ( 출고처 그룹 코드 : [ { 출고처 정보 } , ... ] , ... ) 형식의 Map
            itemWarehouseList : [], // 해당 품목코드의 출고처 목록 : warehouse-row-template 에 바인딩
            itemWarehouseDeleteList : [], // 기존 품목코드 삭제 할 경우 바인딩.
            noItemWarehouseInfo : true, // 출고처 없을 경우 빈영역 보여주기 위해 추가.

            // 출고처 추가 버튼 이벤트
            fnAddWarehouse : function(e) { //

                if( ! this.get('selectedWarehouseGroupCode') || ! this.get('selectedUrSupplierWarehouseId' ) ) {
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

                    viewModel.set('noItemWarehouseInfo', false);            // 출고처 빈영역 감추기.

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

                        if(storeCnt >= 1 && warehouseListOfChosenWarehouseGroupCode[i].storeYn == "Y") {
                        	valueCheck('매장(가맹점) 출고처를 1개이상 등록하실 수 없습니다.', 'warehouse');
                   		 	return;
                        }


                        this.get('itemWarehouseList').push(warehouseListOfChosenWarehouseGroupCode[i]);
                        var itemWarehouseListLength = 0;

                        if(this.get('itemWarehouseList').length > 0	) {
                        	itemWarehouseListLength = this.get('itemWarehouseList').length-1;
                        }

                		if(warehouseListOfChosenWarehouseGroupCode[i].stockOrderYn != "Y") { // 재고 발주 미연동일 경우 발주유형 select box 노출하지 않는다.
							$("#itemWarehousePoTpIdTemplateList"+warehouseListOfChosenWarehouseGroupCode[i].urWarehouseId).hide();
                        }
                        else {
                            if(viewModel.get("bosSupplier") == "2" && (viewModel.get("erpPoTypeDisplayName").indexOf("센터(R2)") != -1)) {
                        		fnKendoDropDownList({
        							id			: "itemWarehousePoTpIdTemplateList"+warehouseListOfChosenWarehouseGroupCode[i].urWarehouseId,
        							url			: "/admin/item/master/register/getItemPoTypeCode",
        							params		: {urSupplierId : viewModel.get("bosSupplier"), selectType : "all"},
        							textField	:"poTypeName",
        							valueField	: "poTypeCode",
        							blank : '선택'
        						});

                        		viewModel.set("itemWarehouseList["+itemWarehouseListLength+"].itemWarehousePoTpIdTemplateList", viewModel.get('ilPoTpId'));
        						fnBosPoPerItemVisible(warehouseListOfChosenWarehouseGroupCode[i].urWarehouseId, viewModel.get('ilPoTpId'));

        						$("#itemWarehousePoTpIdTemplateList"+warehouseListOfChosenWarehouseGroupCode[i].urWarehouseId).data("kendoDropDownList").enable(false);
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
                        	addStoreWareHouse();
                        }

                        break;

                    // ERP 미연동 품목인 경우
                    } else if(this.get('isErpItemLink') == false && warehouseListOfChosenWarehouseGroupCode[i]['urSupplierWarehouseId'] == selectedUrSupplierWarehouseId) {

                        // ERP 미연동 품목은 매장(가맹점)으로 되어 있는 출고처를 추가 실행 시 alert 표시 후 실행 중지
                        if( warehouseListOfChosenWarehouseGroupCode[i]['storeYn'] == "Y"  ) {

                            valueCheck('매장(가맹점) 출고처는 등록하실 수 없습니다.', 'warehouse');
                            return;

                        }
                        this.get('itemWarehouseList').push(warehouseListOfChosenWarehouseGroupCode[i]);

                		if(warehouseListOfChosenWarehouseGroupCode[i].stockOrderYn != "Y") { // 재고 발주 미연동일 경우 발주유형 select box 노출하지 않는다.
							$("#itemWarehousePoTpIdTemplateList"+warehouseListOfChosenWarehouseGroupCode[i].urWarehouseId).hide();
                		}
                		else {
                            if(viewModel.get("bosSupplier") == "2" && (viewModel.get("erpPoTypeDisplayName").indexOf("센터(R2)") != -1)) {
                            	fnKendoDropDownList({
        							id			: "itemWarehousePoTpIdTemplateList"+warehouseListOfChosenWarehouseGroupCode[i].urWarehouseId,
        							url			: "/admin/item/master/register/getItemPoTypeCode",
        							params		: {urSupplierId : viewModel.get("bosSupplier")},
        							textField	:"poTypeName",
        							valueField	: "poTypeCode",
        							blank : '선택'
        						});

                        		viewModel.set("itemWarehouseList["+itemWarehouseListLength+"].itemWarehousePoTpIdTemplateList", viewModel.get('ilPoTpId'));
        						fnBosPoPerItemVisible(warehouseListOfChosenWarehouseGroupCode[i].urWarehouseId, viewModel.get('ilPoTpId'));

        						$("#itemWarehousePoTpIdTemplateList"+warehouseListOfChosenWarehouseGroupCode[i].urWarehouseId).data("kendoDropDownList").enable(false);
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


                if(this.get("itemWarehouseList")[index].updateFlag == "UPDATE")	 {
                	this.get('itemWarehouseDeleteList').push(this.get("itemWarehouseList")[index]);
                }

                this.get("itemWarehouseList").splice(index, 1); // viewModel 에서 삭제

                // 출고처 목록 전체 삭제시 빈영역 노출처리
                if(this.get("itemWarehouseList").length == 0){
                    viewModel.set('noItemWarehouseInfo', true);
                }
            },

            /* 매장별 재고 정보 */
            visibleStoreStockInfo : false, // 매장별 재고정보 visible
            noStoreWarehouseInfo: false, //매장별 재고 정보 Visiable 여부
            itemStoreInfoList : [], // 매장별 재고 정보 목록

            /* 상품 이미지 */
            itemImageList : [], // 상품 이미지 영역에 출력되는 이미지 정보 목록
            itemImageFileList : [],  //상품 이미지 파일 목록 : 마스터 품목 수정 화면에서는 화면에서 사용자가 이미지 추가시에만 itemImageFileList 에 파일 추가됨

            // 사용자가 삭제한 이미지 파일명 목록
            itemImageNameListToDelete : [],
            imageSortOrderChanged : false,  // 이미지 정렬 순서 변경 여부 : 사용자가 한번이라도 이미지 정렬 순서 변경시 true

            itemImageCount : '', // 상품 이미지 영역에 추가된 이미지 개수
            itemImageMaxLimit : 10, // 상품 이미지 최대 등록가능 개수

            fnRemoveItemImage : function(e) { // 상품 이미지 thumbnail 내 "X" 클릭 이벤트 : 추가된 상품 이미지 삭제

                for (var i = this.get("itemImageList").length - 1; i >= 0; i--) {
                    if (this.get("itemImageList")[i]['imagePath'] == e.data["imagePath"]) {
                        this.get("itemImageList").splice(i, 1); // viewModel 에서 삭제
                    }
                }

                viewModel.set('itemImageCount', viewModel.get('itemImageList').length); // 추가된 상품 이미지 개수 갱신

                var itemImageFileList = this.get('itemImageFileList'); // 화면에서 사용자가 추가한 상품 이미지 파일 목록
                var isRegisteredImage = true;  // 삭제한 이미지가 기존 등록된 이미지이면 true, 화면에서 새로 추가한 이미지로 등록 전이면 false

                for (var i = itemImageFileList.length - 1; i >= 0; i--) {
                    if (itemImageFileList[i]['name'] == e.data['imagePath']) {
                        itemImageFileList.splice(i, 1); // 삭제한 파일명에 해당하는 file 객체 삭제
                        isRegisteredImage = false;  // itemImageFileList 에는 화면에서 새로 추가한 파일만 존재 : 기등록된 이미지 아님
                    }
                }

                if( isRegisteredImage ) { // 기존 등록된 이미지를 사용자가 삭제한 경우 삭제 대상 목록에 추가
                    this.itemImageNameListToDelete.push(e.data['imagePath']);
                }

                fnAddItemImageArea(); // 삭제시 항상 기존 상품 추가 영역 삭제 / 마지막 목록에 상품 추가 영역 새로 append

            },
            itemImageUploadResultList : [], // 상품 이미지 업로드 결과 Data : 품목 등록시 사용
            videoUrl : '', // 동영상 URL
            videoAutoplayYn : false, // 비디오 자동재생 유무 ( 체크시 : 자동재생 )

            // 메모
            etcDescription : '',

            /* 추후 작업 / 확인 필요 항목들 */
            erpStockIfYn : false, // ERP 재고연동여부

            // 승인 관련 로직
            itemApprovalStatusViewVisible : false,									// 승인 상태 > 전체 내역 Visible
            approvalStatusNotification : '',										// 승인 상태 > 저장불가 사유
            visibleItemRegistApprDetailPopupButton	: false,						// 승인 상태 > 품목등록 승인내역상세 Button Visible
            visibleItemClientApprDetailPopupButton	: false,						// 승인 상태 > 거래처 승인 요청 승인내역상세 Button Visible
            visibleGoodsRegistApprDetailPopupButton	: false,						// 승인 상태 > 상품등록 승인내역상세 Button Visible
            visibleGoodsClientApprDetailPopupButton	: false,						// 승인 상태 > 거래처 상품 수정 승인 요청 승인내역상세 Button Visible
			itemRegistApprovalDeniedVisible		: false,							// 승인 상태 > 품목등록 반려 사유 Visible
			apprCancelBtnVisible				: false,							// 요청철회 Button Visible
			saveBtnVisible						: true,								// 상품 저장 Button Visible
			itemRegistItemApprId				: '',								// 품목등록 승인 ID
			itemRegistApprReqUserId				: '',								// 품목등록 승인 요청자 ID
			itemRegistApprStat					: '',								// 품목등록 승인상태
			itemRegistApprStatName				: '',								// 품목등록 승인상태명
			itemRegistApprStatusCmnt			: '',								// 품목등록 승인상태 변경 코멘트
			itemClientItemApprId				: '',								// 거래처 품목수정 승인 ID
			itemClientApprReqUserId				: '',								// 거래처 품목수정 승인 요청자 ID
			itemClientApprStat					: '',								// 거래처 품목수정 승인상태
			itemClientApprStatName				: '',								// 거래처 품목수정 승인상태명
			itemClientApprStatusCmnt			: '',								// 거래처 품목수정 승인상태 변경 코멘트
			goodsRegistApprStat					: '',								// 상품등록 승인상태
			goodsRegistApprStatName				: '',								// 상품등록 승인상태명
			goodsClientApprStat					: '',								// 거래처 상품수정 승인상태
			goodsClientApprStatName				: '',								// 거래처 상품수정 승인상태명
			itemApprList 						: [],

            setLabelColor: function(value) {
                return value.changedSpecValue ? '#ff0000' : '';
            }
    };

    // viewModelOriginal 을 deepClone 한 객체를 viewModel 로 세팅
    viewModel = kendo.observable(deepClone(viewModelOriginal));

    fnInitialize(); // Initialize Page Call
    publicUrlPath = fnGetPublicUrlPath(); // Public 저장소 Root Url 조회

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

    function fnUI() {

        fnTranslate(); // comm.lang.js 안에 있는 공통함수 다국어

        fnInitButton(); // Initialize Button

        fnInitItemMgm();

        initUI();

    	fnApproInitialize();

    	getMasterItem(pageParam['ilItemCode']); // 해당 품목코드의 마스터 품목 정보 조회
    };

    function fnInitButton() {
        $('#fnItemUpdateHistorySearch, #fnMasterItemSearch, #fnPoTypeDetailInformationPopup').kendoButton();
    };

    function initUI() {

        kendo.bind($("#view"), viewModel);

        viewModel.set('itemImageCount', viewModel.get('itemImageList').length); // 등록된 상품 이미지 개수 viewModel 에 세팅

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

                viewModel.set('imageSortOrderChanged', true); // 한번이라도 이미지 정렬 순서 변경시 해당 flag 값 true 로 변경

                fnAddItemImageArea();

            }

        });

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

        $("#itemImageAdd").on("click", function(event) {
            $('#uploadItemImage').trigger('click'); // 상품 이미지 파일 input Tag 클릭 이벤트 호출
        });

    }

    // 해당 품목코드의 마스터 품목 정보 조회
    function getMasterItem(ilItemCode) {

    	fnAjax({
            url : "/admin/item/master/modify/getMasterItem",
            method : 'GET',
            params : {
                ilItemCode : ilItemCode
                , ilItemApprId : checkApprovalView()
            },
            success : function(data, status, xhr) {
                fnShowMasterItemInfo(data);
                fnApprHtml(data.rows); // 승인관련 화면 제어
                fnItemApprDetail(data.rows, pageParam['ilItemApprId']) //pageParam.apprKindType = ITEM_CLIENT일 경우 master 품목 승인 정보매핑

            }
        });
    }

//    function getMasterItemApprDetail(ilItemCode) {
//        importScript('/js/service/il/item/itemApprovalComm.js', function() {
//            fnItemApprDetail(pageParam['ilItemCode']);
//        });
//    }

    // 조회된 마스터 품목 정보를 viewModel 에 세팅 / 화면에 출력
    function fnShowMasterItemInfo(data) {
        var item = data['rows'];
        //////////////////////////////////////////////////////////////////////////////////////////////////
        /*
         * ERP 연동 / 미연동 공통 항목 세팅
         */
        //////////////////////////////////////////////////////////////////////////////////////////////////
        fnKendoDropDownList({ // 조회된 공급업체 ID 로 브랜드 리스트 호출
            id : 'bosBrand',
            url : "/admin/ur/brand/searchBrandList",
            params : {
                searchUrSupplierId : item['urSupplierId'],  // 공급업체 ID
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

        viewModel.set('isBosBrandDisabled', false); // BOS 브랜드 Dropdown 활성화

        fnAjax({  // 조회된 공급업체 ID 로 BOS 발주유형 코드 조회
            url : "/admin/item/master/register/getItemPoTypeCode",
            method : 'GET',
            params : {
                urSupplierId : item['urSupplierId'],
                selectType : "all"
            },
            success : function(data, status, xhr) {
                if( data['rows'] && data['rows'].length > 0 ) {

                    viewModel.set('itemPoTypeCodeList', data['rows']);

//                    fnKendoDropDownList({
//                        id : 'bosPoType', // BOS 발주 유형
//                        data : data['itemPoTypeCodeList'],
//                        valueField : 'poTypeCode',
//                        textField : 'poTypeName',
//                        blank : '선택'
//                    });
//
//                   viewModel.set('selectedBosPoType', item['ilPoTypeId']);   // 해당 품목의 발주유형 코드 세팅
//                   viewModel.onBosPoTypeChange(null); // 발주유형 change 이벤트 수동 호출
                }
            }
        });

        // 등록정보
        viewModel.set( 'registerInfo', item['createDate'] + ' / ' + item['createUserName'] + ' ( ' + item['createUserId'] + ' )' );

        // 사용자에 의한 최근 업데이트
        if( item['recentModifyDate'] && item['recentModifyUserId'] && item['recentModifyUserName'] ) { // 수정일자 / 수정자 ID / 수정자 이름 모두 존재시
            viewModel.set( 'recentUpdateInfoByUser', item['recentModifyDate'] + ' / ' + item['recentModifyUserName'] + ' ( ' + item['recentModifyUserId'] + ' )');
        } else if( item['recentModifyDate'] ) { // 수정일자만 존재시
            viewModel.set( 'recentUpdateInfoByUser', item['recentModifyDate'] + ' / ' + '시스템' );
        } else {
            viewModel.set( 'recentUpdateInfoByUser', '' );
        }

        viewModel.set('itemName', item['itemName']); // 마스터 품목명
        viewModel.set('erpStockIfYn', item['erpStockIfYn']); // ERP 재고 연동여부
        viewModel.set('bosItemBarCode', item['itemBarcode']); // BOS 품목바코드 : 미연동품목인 경우에만 출력됨
        viewModel.set('bosCategoryStandardFirstId', item['bosCategoryStandardFirstId']); // 표준카테고리 대분류 ID
        viewModel.set('bosCategoryStandardSecondId', item['bosCategoryStandardSecondId']); // 표준카테고리 소분류 ID
        viewModel.set('bosCategoryStandardThirdId', item['bosCategoryStandardThirdId']); // 표준카테고리 중분류 ID
        viewModel.set('bosCategoryStandardFourthId', item['bosCategoryStandardFourthId']); // 표준카테고리 세분류 ID
        viewModel.set('bosSupplier', item['urSupplierId']); // BOS 공급업체
        viewModel.set('bosBrand', item['urBrandId']); // BOS 브랜드
        viewModel.set('dpBrand', item['dpBrandId']); // 전시 브랜드
        viewModel.set('bosItemGroup', item['bosItemGroup']); // BOS 상품군
        viewModel.set('bosStorageMethod', item['storageMethodType']); // BOS 보관방법

        viewModel.set('bosOriginType', item['originType']); // BOS 원산지
        viewModel.onBosOriginChange(null); // BOS 원산지 Change 이벤트 수동 호출

        console.log(viewModel);

        // 렌탈 품목일 경우 렌탈료 정보
        if(viewModel.get("masterItemType") == "MASTER_ITEM_TYPE.RENTAL" ) {
        	viewModel.set('rentalFeePerMonth', item['rentalFeePerMonth']);  	// 렌탈료(월)
        	viewModel.set('rentalFeePerMonthOrig', item['rentalFeePerMonth']);  // 렌탈료(월)
        	viewModel.set('rentalDueMonth', item['rentalDueMonth']);  			// 의무사용기간(월)
        	viewModel.set('rentalDeposit', item['rentalDeposit']);  			// 계약기간
        	viewModel.set('rentalInstallFee', item['rentalInstallFee']);  		// 설치비
        	viewModel.set('rentalRegistFee', item['rentalRegistFee']);  		// 등록비
        	viewModel.set('isRetalEditDisabled', false);

        	fnSetRentalTotalPrice(); // 총 렌탈가격 - rentalTotalPrice 가격을 viewModel에 설정한다.
        }else {
        	viewModel.set('isRetalEditDisabled', true);

        	if (viewModel.get("masterItemType") == "MASTER_ITEM_TYPE.INCORPOREITY") { // 무형 품목의 경우 비필수값 처리
                $(".incorporeal-star").removeClass("req-star-th")
        	}
        }

        switch( item['originType'] ) { // 마스터품목 복사시 조회된  BOS 원산지 값

        case 'ORIGIN_TYPE.DOMESTIC' : // '국내' : 로직 없음

            break;

        case 'ORIGIN_TYPE.OVERSEAS' : // '해외' 선택시

            // 원산지 상세 '해외' 원산지 Dropdown 과 viewModel 에 복사한 원산지 상세 값 세팅
            viewModel.set('bosOriginDetailOverseas', item['originDetail']);
            break;

        case 'ORIGIN_TYPE.ETC' : // '기타' 선택시

            // 원산지 상세 '직접입력' input 과 viewModel 에 복사한 원산지 상세 값 세팅
            viewModel.set('bosOriginDetailDirectInputValue', item['originDetail']);

            break;

        }

        viewModel.set('bosDistributionPeriod', item['distributionPeriod']); // 유통기간

        viewModel.set('boxWidth', item['boxWidth']); // 박스 가로
        viewModel.set('boxDepth', item['boxDepth']); // 박스 세로
        viewModel.set('boxHeight', item['boxHeight']); // 박스 높이

        viewModel.set('bosPiecesPerBox', item['piecesPerBox']); // 박스 입수량
        viewModel.set('unitOfMeasurement', item['unitOfMeasurement']); // UOM/OMS ( 측정단위 )

        viewModel.set('sizePerPackage', item['sizePerPackage']); // 포장단위별 용량
        viewModel.set('sizeUnit', item['sizeUnit']); // 용량(중량) 단위

        if( item['sizeUnit'] == sizeUnitDirectInputCode ) {
            viewModel.set('isSizeUnitDirectInputVisible', true); // 용량(중량) 단위 직접입력 Input Visible 처리
            viewModel.set('sizeUnitDirectInputValue', item['sizeUnitEtc']); // 용량(중량) 단위 기타
        }

        viewModel.set('quantityPerPackage', item['quantityPerPackage']); // 포장 구성수량
        viewModel.set('packageUnit', item['packageUnit']); // 포장 구성단위

        if( item['packageUnit'] == packageUnitDirectInputCode ) {
            viewModel.set('isPackageUnitDirectInputVisible', true); // 포장 구성단위 직접입력 Input Visible 처리
            viewModel.set('packageUnitDirectInputValue', item['packageUnitEtc']); // 포장 구성단위 단위 기타
        }

        viewModel.set('pdmGroupCode', item['pdmGroupCode'] ); // PDM 그룹코드

        // PDM 그룹코드 관련
        if( item['urSupplierId'] == pdmSupplierCode ) { // 공급업체가 PDM ( "풀무원 녹즙2" ) 인 경우

            viewModel.set('isPdmGroupCodeVisible', true); // PDM 그룹코드 Visible 처리

//            var isEatsslimBrand = false;  // '잇슬림' 브랜드 여부 : 기본값 false ( '잇슬림' 브랜드 아님 )
//
//            // '잇슬림' 브랜드 목록 eatsslimBrandCodeList : fnInitItemMgm 함수에서 먼저 세팅됨
//            for( var i = viewModel.get('eatsslimBrandCodeList').length - 1 ; i >= 0 ; i -- ) {
//
//                if( item['urBrandId'] == viewModel.get('eatsslimBrandCodeList')[i] ) { // '잇슬림' 브랜드 목록에 있는 경우
//                    isEatsslimBrand = true;
//                }
//
//            }
//
//            if( isEatsslimBrand ) { // '잇슬림' 브랜드인 경우
//
//                viewModel.set('isPdmGroupCodeDisabled', false); // PDM 그룹코드 입력 가능 ( 비활성화 해제 )
//
//            } else { // '잇슬림' 브랜드가 아닌 경우
//
//                viewModel.set('isPdmGroupCodeDisabled', true); // PDM 그룹코드 입력 불가능 ( 비활성화 )
//                viewModel.set('pdmGroupCode', ''); // 기존 PDM 그룹 코드 입력값 초기화
//
//            }

        } else {

            viewModel.set('isPdmGroupCodeVisible', false ); // PDM 그룹코드 Visible 해제
            viewModel.set('isPdmGroupCodeDisabled', true ); // PDM 그룹코드 비활성화
            viewModel.set('pdmGroupCode', '' ); // 기존 PDM 그룹코드 값 초기화

        }

        viewModel.set('taxYn', ( item['taxYn'] == true ? 'Y' : 'N' ) ); // 과세구분 (과세/면세)

        viewModel.set('nutritionDisplayYn', (item['nutritionDisplayYn'] == true ? 'Y' : 'N')); // 영양정보 표시여부
        viewModel.onNutritionDisplayYnChange(null); // 영양정보 표시여부 change 이벤트 수동 호출

        if (item['nutritionDisplayDefalut']) { // 영양정보 기본 정보 조회시
            viewModel.set('nutritionDisplayDefalut', item['nutritionDisplayDefalut']); // 영양정보 기본정보 ( NUTRITION_DISP_Y 값이 N 일때 노출되는 항목 )
        }
        viewModel.set('nutritionQuantityPerOnce', item['nutritionQuantityPerOnce']); // 영양분석 단위 (1회 분량)
        viewModel.set('nutritionQuantityTotal', item['nutritionQuantityTotal']); // 영양분석 단위 (총분량)
        viewModel.set('nutritionEtc', item['nutritionEtc']); // 영양성분 기타

        // 배송불가 지역
        var undeliverableAreaInfo = data['undeliverableAreaResponseDto'];
        if( data['undeliverableAreaResponseDto'] ) {

            var undeliverableAreaInfo = data['undeliverableAreaResponseDto'];

            // 1권역 배송가능 여부 ( true : 배송 가능 )
            var islandShippingYn = undeliverableAreaInfo['islandShippingYn'];

            // 2권역 배송가능 여부 ( true : 배송 가능 )
            var jejuShippingYn = undeliverableAreaInfo['jejuShippingYn'];

            // 1권역 / 2권역 배송가능 여부 viewModel 에 세팅
            viewModel.set('islandShippingYn', islandShippingYn);
            viewModel.set('jejuShippingYn', jejuShippingYn);

            // 1권역 / 2권역 배송불가여부 체크박스 값 세팅 : 화면에서는 배송 불가 여부이므로 값 반대로 세팅
            $("input[name=shippingYn]:eq(0)").prop('checked', !islandShippingYn);
            $("input[name=shippingYn]:eq(1)").prop('checked', !jejuShippingYn);

        }

        viewModel.set('extinctionYn', ( item['extinctionYn'] == true ? 'Y' : 'N' ) ); // 단종여부

        // 조회된 단종 여부 값으로 단종여부 Radio 값 세팅
        $("input[name=extinctionYn][value=" + viewModel.get('extinctionYn') + "]").attr("checked", true);

        viewModel.set('videoUrl', item['videoUrl']); // 동영상 url
        viewModel.set('videoAutoplayYn', item['videoAutoplayYn']); // 동영상 자동재생 여부

        /* 상품상세 기본 / 주요정보 */
        $('#basicDescription').data("kendoEditor").value(item['basicDescription']); // 상품상세 기본정보
        $('#detaillDescription').data("kendoEditor").value(item['detaillDescription']); // 상품상세 주요정보
        viewModel.set('etcDescription', item['etcDescription']); // 메모

        //////////////////////////////////////////////////////////////////////////////////////////////////
        /*
         * ERP 연동 품목 전용 정보 화면에 출력
         */
        //////////////////////////////////////////////////////////////////////////////////////////////////
        if( viewModel.get('isErpItemLink') == true ) {

            viewModel.set('erpItemName', item['erpItemName']); // ERP 품목명
            viewModel.set('erpItemBarCode', item['erpItemBarcode']); // ERP 품목바코드
            //viewModel.set('bosItemBarCode', item['erpItemBarcode']); // BOS 품목바코드
            viewModel.set('erpCategoryLevel1Id', item['erpCategoryLevel1Id']); // ERP 대카테고리
            viewModel.set('erpCategoryLevel2Id', item['erpCategoryLevel2Id']); // ERP 중카테고리
            viewModel.set('erpCategoryLevel3Id', item['erpCategoryLevel3Id']); // ERP 소카테고리
            viewModel.set('erpCategoryLevel4Id', item['erpCategoryLevel4Id']); // ERP 세카테고리
            viewModel.set('erpBrand', item['erpBrandName']); // ERP 브랜드
            viewModel.set('erpItemGroup', item['erpItemGroup']); // ERP 상품군
            viewModel.set('erpLegalTypeCode', item['erpLegalTypeCode']); // ERP 법인코드
            if( ! item['erpSupplierName']){
                viewModel.set('erpSupplierName', '정보없음'); // ERP 상품군 출력명 '정보없음' 으로 출력
            } else {
                viewModel.set('erpSupplierName', item['erpSupplierName']); // ERP 생산처
            }

            if( ! item['erpItemGroup'] ) {
                viewModel.set('erpItemGroupDisplayName', '정보없음'); // ERP 상품군 출력명 '정보없음' 으로 출력
            } else {
                viewModel.set('erpItemGroupDisplayName', item['erpItemGroup']); // ERP 상품군 출력
            }

            // ERP 온도구분 (상온, 실온, 냉장, 냉동…) => 보관방법??
            viewModel.set('erpStorageMethod', item['erpStorageMethod']);
            viewModel.set('erpOriginName', item['erpOriginName']); // ERP 원산지
            viewModel.set('erpDistributionPeriod', item['erpDistributionPeriod']); // ERP 유통기간
            viewModel.set('bosDistributionPeriod', item['erpDistributionPeriod']); // BOS 유통기간
            viewModel.set('erpBoxWidth', item['erpBoxWidth']); // ERP 박스_가로 (소수점 1자리)
            viewModel.set('erpBoxDepth', item['erpBoxDepth']); // ERP 박스_세로 (소수점 1자리)
            viewModel.set('erpBoxHeight', item['erpBoxHeight']); // ERP 박스_높이 (소수점 1자리)
            viewModel.set('erpPiecesPerBox', item['erpPiecesPerBox']); // ERP 박스 입수량
            viewModel.set('boxWidth', item['erpBoxWidth']);
            viewModel.set('boxDepth', item['erpBoxDepth']);
            viewModel.set('boxHeight', item['erpBoxHeight']);

            viewModel.set('bosPiecesPerBox', item['erpPiecesPerBox']); // BOS 박스 입수량
            /*
             * 화면상에 출력할 ERP 영양분석단위 메시지 생성 / 유효한 값인 경우 viewModel 에 세팅
             */
            fnGenerateErpNutritionAnalysisMessage(item);

            viewModel.set('erpTaxYn', item['erpTaxYn']); // 최초 품목 생성시 ERP API 로 조회한 연동 품목 데이터의 과세구분
            viewModel.set('erpPoType', item['erpPoType']); // 최초 품목 생성시 ERP API 로 조회한 연동 품목 데이터의 발주유형

            // ERP API 로 조회한 연동 품목 데이터의 발주유형 출력명 : 발주유형 미조회시 '없음' 출력
            viewModel.set('erpPoTypeDisplayName', ( item['erpPoType'] ? item['erpPoType'] : '없음' ) );

            if( item['erpLegalTypeCode'] == orgaLegalTypeCode ) {   // 올가 ERP 인 경우 : 매장품목유형, 발주가능여부 표시

                // 매장품목유형
                viewModel.set('o2oExposureType', item['erpO2oExposureType']);
                viewModel.set('hasO2oExposureType', true);   // 매장품목유형 Visible 처리

    			// 공급업체가 올가홀푸드인 ERP 연동상품의 경우, 품목유형이 공통/매장전용이면 매장별 재고정보 Visible 처리 S
                if (viewModel.get('masterItemType') == 'MASTER_ITEM_TYPE.COMMON' || viewModel.get('masterItemType') == 'MASTER_ITEM_TYPE.SHOP_ONLY') {
                	viewModel.set('visibleStoreStockInfo', true);
                	viewModel.set('noStoreWarehouseInfo', true);
                    /* 매장별 재고 정보 */
                    if(item['storeWarehouseYn'] == 'Y'){
                    	fnGetItemStoreInfo(item['ilItemCode']);
                    }
                }
    			// 공급업체가 올가홀푸드인 ERP 연동상품의 경우, 품목유형이 공통/매장전용이면 매장별 재고정보 Visible 처리 E

                // 발주가능여부
                viewModel.set('erpCanPoYn', item['erpCanPoYn']); // 최초 품목 생성시 ERP API 로 조회한 ERP 연동 품목 데이터에서 ERP 발주가능여부

                if (item['erpCanPoYn'] == true) { // ERP 발주가능여부 출력 메시지 세팅
                    viewModel.set('erpCanPoYnMessage', '예'); // ERP API 로 조회한 ERP 연동 품목 데이터에서 ERP 발주가능여부
                } else {
                    viewModel.set('erpCanPoYnMessage', '아니오'); // ERP API 로 조회한 ERP 연동 품목 데이터에서 ERP 발주가능여부
                }

                viewModel.set('hasErpCanPoYn', true);   // 올가 ERP 발주가능여부 메시지 Visible 처리

            }

            if( item['erpLegalTypeCode'] == lohasLegalTypeCode ) { // 건강생활 ERP 인 경우 : 상품판매유형 표시

                viewModel.set('productType', item['erpProductType']);
                viewModel.set('hasProductType', true);

            }

        }

        //////////////////////////////////////////////////////////////////////////////////////////////////
        /*
         * 기타 항목 정보 조회
         */
        //////////////////////////////////////////////////////////////////////////////////////////////////

        /* 해당 품목의 표준 카테고리, 보관방법에 따른 반품 가능여부, 반품 기간 조회 */
        viewModel.onCalculateReturnPeriod(null);

        /* 해당 품목코드로 가격 정보 조회 */
        fnGetItemPriceList(item['ilItemCode']);

        /* 해당 품목코드로 ERP 할인 가격 정보 조회 */
        fnGetItemErpPriceList(item['ilItemCode']);

        /* 상품정보 제공고시 분류 / 상세 항목 조회 : 해당 품목의 품목코드로 상품정보 제공고시 상세 항목 조회 */
        // 해당 품목의 상품정보 제공고시 항목 코드를 같이 넘김
        fnGetItemSpecList(item['ilItemCode'], item['ilSpecMasterId']);

        /* 상품 영양정보 조회 : 해당 품목의 품목 코드로 등록 가능한 영양정보 분류코드 목록 / 해당 품목의 영양정보 상세 목록 조회  */
        fnGetAddAvailableNutritionCodeList( item['ilItemCode'], false );

        /* 상품 인증정보 조회 : 해당 품목의 품목코드로 상품 인증정보 조회 */
        fnGetItemCertificationList(item['ilItemCode']);

        /* 출고처 코드 정보 조회 : 해당 품목의 공급업체 PK 로 출고처 코드 정보 조회 */
        fnGetItemWarehouseCodeList(item['urSupplierId']);

        /* 출고처 정보 조회 : 해당 품목의 출고처 조회 */
        setTimeout(() => fnGetItemWarehouseList(item['ilItemCode']), 1000);

        /* 해당 품목코드로 등록된 품목 이미지 목록 조회 */
        fnGetItemImageList(item['ilItemCode']);

        setTimeout(fnAddItemImageArea(), 1000);

    }

    function fnGetItemStoreInfo(ilItemCode) { // 해당 품목코드로 매장별 재고 정보 조회
    	fnAjax({
            url : "/admin/item/store/getStoreList",
            params : {
                ilItemCode : ilItemCode,  //품목 코드
            },
            isAction : 'select',
            success : function(data, status, xhr) {
            	viewModel.set('noStoreWarehouseInfo', false);
            	viewModel.set('itemStoreInfoList', data);
            }
        });
    }

    function fnStoreScheduleModal (urStoreId, storeName) {
    	fnKendoPopup({
            id : 'itemStoreSchedule',
            title : storeName + ' 스케쥴 정보',
            src : '#/itemStoreSchedule',
            width : '1000px',
            height : '800px',
            param : {
            	urStoreId : urStoreId,
            }
        });
    }

    function fnItemStorePriceLogModal(urStoreId, storeName) {
    	fnKendoPopup({
            id : 'itemStorePriceLog',
            title : 'O2O 매장 판매가 정보 <' + storeName + '>',
            src : '#/itemStorePriceLog',
            width : '1000px',
            height : '800px',
            param : {
            	ilItemCd : (viewModel.get("isErpItemLink") == true ? viewModel.get("erpItemCode") : viewModel.get('bosItemCode')),
            	urStoreId : urStoreId
            }
        });
    }

    function fnGetItemPriceList(ilItemCode) { // 해당 품목코드로 가격 정보 조회

        fnAjax({
            url : "/admin/item/master/modify/getItemPriceSchedule",
            method : 'GET',
            params : {
                ilItemCode : ilItemCode,  //품목 코드
            },
            isAction : 'select',
            success : function(data, status, xhr) {


                if( data['rows'] && data['rows'].length > 0 ) {

                	if(viewModel.get("masterItemType") == "MASTER_ITEM_TYPE.RENTAL") {
                		viewModel.set( 'showItemPrice' , false );
                		viewModel.set( 'showRentalPriceVisible' , true );
                	}else {
                    	// 현재가격 및 예정가격 셋팅 - 화면 노출용
                    	viewModel.set( 'itemPriceScheduleList' , data['rows'] );
                    	// 현재가격 및 예정가격 셋팅 - Popup 편집용
                    	viewModel.set( 'itemPriceScheduleAfterList' , data['rowsPopup']);
                        viewModel.set( 'showItemPrice' , true );   // 올가 ERP 가격정보 행 출력
                        viewModel.set( 'showRentalPriceVisible' , false ); // 렌탈 가격정보 활성화 여부

//                        if( viewModel.get('isErpItemLink') == true ) { // ERP 연동 품목인 경우
//                        	if( viewModel.get('erpLegalTypeCode') == orgaLegalTypeCode ) {  // 올가 ERP
//                        		viewModel.set( 'showItemPriceAddBtn' , false );
//                        		viewModel.set("approvalTarget" , "미승인");
//                        	}else if( viewModel.get('erpLegalTypeCode') == foodmerceLegalTypeCode ) {  // 푸드머스 ERP
//                        		viewModel.set( 'showItemPriceAddBtn' , true );
//                        		viewModel.set("approvalTarget" , "승인");
//                        	}else if( viewModel.get('erpLegalTypeCode') == lohasLegalTypeCode ) {  // 건강생활 ERP
//                        		viewModel.set("approvalTarget" , "승인");
//                        		if( viewModel.get('productType') == '제품' ) {
//                        			viewModel.set( 'showItemPriceAddBtn' , true );
//                        		}else if( viewModel.get('productType') == '상품' ) {
//                        			viewModel.set( 'showItemPriceAddBtn' , true );
//                        		}
//                        	}else { // 기타 ERP 연동 품목인 경우
//                        		viewModel.set( 'showItemPriceAddBtn' , false );
//                        		viewModel.set("approvalTarget" , "미승인");
//                        	}
//                        }else {
//                        	// ERP 미연동 품목인 경우
//                        	viewModel.set( 'showItemPriceAddBtn' , true );
//                        	viewModel.set("approvalTarget" , "승인");
//                        }
                	}

                	if( viewModel.get('isErpItemLink') == true ) { // ERP 연동 품목인 경우
                    	if( viewModel.get('erpLegalTypeCode') == foodLegalTypeCode ) {  // 풀무원식품
                    		viewModel.set( 'showItemPriceAddBtn' , false );
                    		viewModel.set("approvalTarget" , "미승인");
                    	} else if( viewModel.get('erpLegalTypeCode') == orgaLegalTypeCode ) {  // 올가 ERP
                    		viewModel.set( 'showItemPriceAddBtn' , false );
                    		viewModel.set("approvalTarget" , "미승인");
                    	} else if( viewModel.get('erpLegalTypeCode') == foodmerceLegalTypeCode ) {  // 푸드머스 ERP
                    		viewModel.set( 'showItemPriceAddBtn' , true );
                    		viewModel.set("approvalTarget" , "승인");
                    	} else if( viewModel.get('erpLegalTypeCode') == lohasLegalTypeCode ) {  // 건강생활 ERP
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
                    } else {
                    	// ERP 미연동 품목인 경우
                    	viewModel.set( 'showItemPriceAddBtn' , true );
                    	viewModel.set("approvalTarget" , "승인");
                    }

                    /*
                    if( viewModel.get('isErpItemLink') == true ) { // ERP 연동 품목인 경우

                        if( viewModel.get('erpLegalTypeCode') == orgaLegalTypeCode ) {  // 올가 ERP

                            // 해당 가격정보 현재/예정 목록을 담을 변수명
                            viewModel.set( 'currentPriceScheduleListName', "orgaErpItemPriceScheduleList" );

                            viewModel.set( 'orgaErpItemPriceScheduleList' , data['rows'] );
                            viewModel.set( 'showOrgaErpItemPrice' , true );   // 올가 ERP 가격정보 행 출력

                        } else if( viewModel.get('erpLegalTypeCode') == foodmerceLegalTypeCode ) { // 푸드머스 ERP

                            // 해당 가격정보 현재/예정 목록을 담을 변수명
                            viewModel.set( 'currentPriceScheduleListName', "foodmerceErpItemPriceScheduleList" );

                            viewModel.set( 'foodmerceErpItemPriceScheduleList', data['rows'] );
                            viewModel.set( 'showFoodmerceErpItemPrice' , true );   // 푸드머스 ERP 가격정보 행 출력

                        } else if( viewModel.get('erpLegalTypeCode') == lohasLegalTypeCode ) {  // 건강생활 ERP

                            if( viewModel.get('productType') == '제품' ) {

                                // 해당 가격정보 현재/예정 목록을 담을 변수명
                                viewModel.set( 'currentPriceScheduleListName', "lohasProductsErpItemPriceScheduleList" );

                                viewModel.set( 'lohasProductsErpItemPriceScheduleList' , data['rows'] );
                                viewModel.set( 'showLohasProductsErpItemPrice' , true );   // 건강생활 ERP 중 제품 가격정보 행 출력

                            } else if( viewModel.get('productType') == '상품' ) {

                                // 해당 가격정보 현재/예정 목록을 담을 변수명
                                viewModel.set( 'currentPriceScheduleListName', "lohasGoodsErpItemPriceScheduleList" );

                                viewModel.set( 'lohasGoodsErpItemPriceScheduleList' , data['rows'] );
                                viewModel.set( 'showLohasGoodsErpItemPrice' , true );   // 건강생활 ERP 중 상품 가격정보 행 출력

                            }

                        } else { // 기타 ERP 연동 품목인 경우

                            // 해당 가격정보 현재/예정 목록을 담을 변수명
                            viewModel.set( 'currentPriceScheduleListName', "erpLinkItemPriceScheduleList" );

                            viewModel.set( 'erpLinkItemPriceScheduleList', data['rows'] );
                            viewModel.set( 'showErpLinkItemPrice' , true );   // 기타 ERP 연동 품목 가격정보 행 출력

                        }

                    } else { // ERP 미연동 품목인 경우

                        // 해당 가격정보 현재/예정 목록을 담을 변수명
                        viewModel.set( 'currentPriceScheduleListName', "erpNotLinkItemPriceScheduleList" );

                        viewModel.set( 'erpNotLinkItemPriceScheduleList', data['rows'] );

                    }
                    */

                }

            }
        });
    }

    function fnGetItemErpPriceList(ilItemCode) { // 해당 품목코드로 가격 정보 조회

        fnAjax({
            url : "/admin/item/master/modify/getItemErpPriceSchedule",
            method : 'GET',
            params : {
                ilItemCode : ilItemCode,  //품목 코드
            },
            isAction : 'select',
            success : function(data, status, xhr) {

                if( data['rows'] && data['rows'].length > 0 ) {

                	viewModel.set("itemDiscountPriceList", data['rows']);
                	viewModel.set("showItemDiscountPrice", true);
                }

            }
        });
    }



    // 화면상에 출력할 ERP 영양분석단위 메시지 생성 / 유효한 값인 경우 viewModel 에 세팅
    function fnGenerateErpNutritionAnalysisMessage(item) {

        var servingSize = (item['erpNutritionQtyPerOnce'] ? item['erpNutritionQtyPerOnce'] : ( item['erpNutritionQtyPerOnce'] == 0 ? 0 : '')); // 1회 제공량
        var servingContainer = (item['erpNutritionQtyTotal'] ? item['erpNutritionQtyTotal'] : ( item['erpNutritionQtyTotal'] == 0 ? 0 : '')); // 총 제공량

        var message;

        if (servingSize && servingContainer) { // 1회 제공량, 총 제공량 모두 유효한 값 조회된 경우

            message = '1회 분량   ' + servingSize + '     총 ' + servingContainer + ' ';

            viewModel.set('erpNutritionAnalysisMessage', message); // viewModel 에 메시지 세팅
            viewModel.set('showErpNutritionAnalysisMessage', true); // visible 처리

        } else if( servingSize ) { // 1회 제공량만 유효한 값 조회시

            message = '1회 분량   ' + servingSize;

            viewModel.set('erpNutritionAnalysisMessage', message); // viewModel 에 메시지 세팅
            viewModel.set('showErpNutritionAnalysisMessage', true); // visible 처리

        } else if( servingContainer ) { // 총 제공량만 유효한 값 조회시

            message = '총   ' + servingContainer;

            viewModel.set('erpNutritionAnalysisMessage', message); // viewModel 에 메시지 세팅
            viewModel.set('showErpNutritionAnalysisMessage', true); // visible 처리

        } else {

            viewModel.set('erpNutritionAnalysisMessage', ''); // viewModel 에 메시지 세팅
            viewModel.set('showErpNutritionAnalysisMessage', false); // visible 처리 해제

        }

        viewModel.set('erpNutritionQtyPerOnce', item['erpNutritionQtyPerOnce']); // ERP 영양분석단위 (1회분량)
        viewModel.set('erpNutritionQtyTotal', item['erpNutritionQtyTotal']); // ERP 영양분석단위 (총분량)
        viewModel.set('erpNutritionEtc', item['erpNutritionEtc']); // ERP 영양성분 기타

    }

    function fnUpdate() { // 품목 정보 수정

        if (fnValidationCheck('update', null) == false) { // 저장 전 validation 체크
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

                fnUpdateItem(); // 품목 정보 수정

            },
            cancel : function() {
                return;
            }
        });

    };

    function fnUploadItemImage() { // 상품 이미지 업로드

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
            var fileName = $("#itemImageContainer .itemImage:nth-child(" + (i + 1) + ")").attr('data-id');
            itemImageFileNameArray.push(fileName);
        }

        return itemImageFileNameArray;

    }

    function fnUpdateItem() { // 품목 정보 수정

        var updateItemParam = {

            /* 기본 정보 */

            // 품목코드
            ilItemCode : (viewModel.get("isErpItemLink") == true ? viewModel.get("erpItemCode") : viewModel.get('bosItemCode')),

            // 마스터 품목 유형
            itemType : viewModel.get("masterItemType"),

            // ERP 품목 연동여부
            erpLinkIfYn : (viewModel.get("isErpItemLink") == true ? true : false),

            erpStockIfYn : viewModel.get("erpStockIfYn"),

            urSupplierId : viewModel.get("bosSupplier"),
            // BOS 품목 바코드
            itemBarcode : viewModel.get("bosItemBarCode"),

            // 마스터 품목명
            itemName : $.trim( viewModel.get("itemName") ),

            // 표준 카테고리 PK
            ilCategoryStandardId : checkCategoryStandardId(),

            /* 상세 정보 */

            // BOS 브랜드 PK
            urBrandId : viewModel.get("bosBrand"),

            // 전시 브랜드 PK
            dpBrandId : viewModel.get("dpBrand"),

            // BOS 상품군
            bosItemGroup : viewModel.get("bosItemGroup"),

            // BOS 보관방법
            storageMethodType : viewModel.get("bosStorageMethod"),

            // BOS 원산지
            originType : viewModel.get("bosOriginType"),

            // BOS 원산지 상세
            originDetail : fnGetOriginDetail(),

            // BOS 유통기간
            distributionPeriod : viewModel.get("bosDistributionPeriod"),

            // BOS 박스 가로
            boxWidth : viewModel.get("boxWidth"),

            // BOS 박스 세로
            boxDepth : viewModel.get("boxDepth"),

            // BOS 박스 높이
            boxHeight : viewModel.get("boxHeight"),

            // BOS 박스 입수량
            piecesPerBox : viewModel.get("bosPiecesPerBox"),

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

            /* 배송 / 발주 정보 */

            // BOS 발주유형 : ERP 연동 품목인 경우에만 전송
            ilPoTypeId : (viewModel.get("isErpItemLink") == true ? viewModel.get('selectedBosPoType') : null),

            /*
             * 배송불가지역 Enum UndeliverableAreaTypes 참조 : 화면에서 체크한 값을 반대로 전송함
             */

            // 1도서산간지역 (1권역) 배송 불가 여부
            islandShippingYn : viewModel.get('islandShippingYn'),

            // 2제주지역 (2권역) 배송 불가 여부
            jejuShippingYn : viewModel.get('jejuShippingYn'),

            // 단종 여부
            extinctionYn : (viewModel.get('extinctionYn') == 'Y' ? true : false),

            // 동영상 URL
            videoUrl : viewModel.get('videoUrl'),

            // 비디오 자동재생 유무 ( true : 자동재생 )
            videoAutoplayYn : viewModel.get('videoAutoplayYn'),

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

            // 출고처 목록
            addItemWarehouseList : fnAddItemWarehouseList(),

            delItemWarehouseList : viewModel.get("itemWarehouseDeleteList"),

            // 상품 이미지 업로드 결과 목록
            itemImageUploadResultList : viewModel.get("itemImageUploadResultList"),

            // 상품 대표 이미지명 : 상품 이미지 영역에서 첫번째 이미지의 data-id ( 파일명 )
            representativeImageName : $("#itemImageContainer > .itemImage").attr('data-id'),

            // 삭제할 상품 이미지 파일명 목록
            itemImageNameListToDelete : viewModel.get("itemImageNameListToDelete"),

            // 상품 이미지 정렬 순서 변경 여부
            imageSortOrderChanged : viewModel.get('imageSortOrderChanged'),

            // 상품 이미지 정렬 순서 목록 : 저장시 품목 이미지의 정렬 순서로 지정
            itemImageOrderList : fnGetItemImageOrder(),

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

        	// 발주가능여부
            erpCanPoYn : viewModel.get('erpCanPoYn'),

            /* 승인관리자 정보 시작 */
			itemApprList							: viewModel.get("itemApprList"), 														//승인관리자 정보
			loadDateTime							: loadDateTime,														//페이지 Load 시간
			apprKindTp								: viewModel.get("apprKindTp"),
			itemStatusTp							: viewModel.get("itemStatusTp"),
			/* 승인관리자 정보 끝 */

        }

        // 렌탈 품목 렌탈료가 변경 되었을 경우 추가
        if(viewModel.get("masterItemType") == "MASTER_ITEM_TYPE.RENTAL") {
        	if(viewModel.get("rentalFeePerMonth") != viewModel.get("rentalFeePerMonthOrig")) {
            	//가격 정보 신규 입력 여부 : true 인 경우에만 신규 가격정보 저장됨
                updateItemParam['newPriceYn'] = true;

                // 신규 입력 가격 정보의 적용 시작일
                updateItemParam['priceApplyStartDate'] = fnGetToday();

                // 신규 입력 원가
                updateItemParam['standardPrice'] = viewModel.get('rentalFeePerMonth');

                // 신규 입력 정상가
                updateItemParam['recommendedPrice'] = viewModel.get('rentalFeePerMonth');
        	}
        }

        /* 가격정보 세팅 */
        // Validation 체크 성공 / 가격정보 신규 입력행 Visible 상태인 경우 가격정보 저장
        if( viewModel.get('showItemPriceAddRow') == true ) {

            // 가격 정보 신규 입력 여부 : true 인 경우에만 신규 가격정보 저장됨
            updateItemParam['newPriceYn'] = true;

            // 신규 입력 가격 정보의 적용 시작일
            updateItemParam['priceApplyStartDate'] = kendo.toString(viewModel.get('newPriceApplyStartDate'), "yyyy-MM-dd");

            // 신규 입력 원가
            updateItemParam['standardPrice'] = viewModel.get('newStandardPrice');

            // 신규 입력 정상가
            updateItemParam['recommendedPrice'] = viewModel.get('newRecommendedPrice');

        }

        // 해당 품목 가격정보에서 삭제할 가격의 시작일자 목록
        updateItemParam['priceApplyStartDateListToDelete'] = viewModel.get('priceApplyStartDateListToDelete');

        fnAjax({
            url : "/admin/item/master/modify/modifyItem",
            params : updateItemParam,
            contentType : 'application/json',
            isAction : 'insert',
            success : function(data) {

                fnKendoMessage({
                    message : '품목코드 ' + (viewModel.get("isErpItemLink") == true ? viewModel.get("erpItemCode") : viewModel.get('bosItemCode')) + ' 의 정보가 수정되었습니다.',
                    ok : function focusValue() {
                        var option = new Object();

                    	viewModel.set('rentalFeePerMonthOrig', viewModel.get('rentalFeePerMonth'));  // 렌탈료(월)

                        option.url = "#/itemMgmModify";   	// 마스터 품목 수정 화면 URL
//                        option.menuId = 721;     			// 마스터 품목 수정 메뉴 ID
                        option.data = {          			// 마스터 품목 수정 화면으로 전달할 Data
                        		ilItemCode : (viewModel.get("isErpItemLink") == true ? viewModel.get("erpItemCode") : viewModel.get('bosItemCode')),
                	            isErpItemLink : viewModel.get("isErpItemLink"),
                	            masterItemType : viewModel.get('masterItemType')
                        };

//                        location.href = successUrl;
//                        fnGoPage(option);
                        location.reload();
                    }
                });

            },
            fail : function(data, resultcode) {
            	if (resultcode.messageEnum == 'CLINET_APPR_REQUEST') { // 승인요청후 페이지 갱신
					fnKendoMessage({
						message : resultcode.message
						, ok : function(e) {
		                    location.reload();
						}
					});
            	}
            },
            error : function(xhr, status, strError) {
                fnKendoMessage({
                     message : xhr.responseText,
                     ok : function focusValue() {
                    	 location.reload();
                     }
                });
            },

        });

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
                var unlimitStockFlag = warehouseInfo['unlimitStockYn'];
                var updateFlag = warehouseInfo['updateFlag'];

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

                if(unlimitStockFlag == undefined) {
                	unlimitStockFlag = true;
                }
                if(updateFlag == undefined) {
                	updateFlag = "INSERT";
                }

                itemWarehouseList.push({
                	ilItemWarehouseId : warehouseInfo['ilItemWarehouseId'],
                    urSupplierWarehouseId : warehouseInfo['urSupplierWarehouseId'], //  공급처-출고처 PK
                    preOrderYn : preOrderYnFlag, 									//  선주문 여부
                    preOrderTp : preOrderTpVal,										//  선주문 유형코드
                    unlimitStockYn : unlimitStockFlag,								//  무제한 재고 여부
                    updateFlag : updateFlag,											//  신규 여부
                    ilPoTpId  : ilPoTpIdVal
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

    // 상품정보 제공고시 분류 / 세부 항목 조회
    function fnGetItemSpecList(ilItemCode, ilSpecMasterId) {

    	fnAjax({
            url : "/admin/item/master/register/getItemSpecList",
            method : 'GET',
            params : {
                ilItemCode : ilItemCode
                , ilItemApprId : checkApprovalView()
            },
            success : function(data, status, xhr) {

                // 품목별 상품정보제공고시 세부 항목 조회 목록 존재시
                var itemSpecValueMap = {};

                if (data['changedSpecMaster'] == true) $('#labelSpecMaster').addClass('approvalChanged'); // 상품정보 제공고시 변경여부

                if (data['itemSpecValueList'] && data['itemSpecValueList'].length > 0) {

                    data['itemSpecValueList'].forEach(function(item, index) {
                        itemSpecValueMap[item['ilSpecFieldId']] = item;
                    });

                    // viewModel 에 품목별 상품정보제공고시 세부 항목 Setting
                    viewModel.set('itemSpecValueMap', itemSpecValueMap);

                }

                // 상품정보 제공고시 분류 / 항목 조회 목록
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
                            if (itemSpecValueObj == null)
                            	return;

                            // 원본 품목의 해당 상품정보 제공고시 상세 항목의 직접 입력 여부가 true 인 경우
                            if (itemSpecValueObj['directYn'] && itemSpecValueObj['directYn'] == true) {

                            	// '직접입력' 체크박스 Visible 처리 / 체크 상태로 지정
                                item['showDirectInputCheckBox'] = true; //
                                item['directInputChecked'] = true; //

                                // 상품정보제공고시 입력 Input 활성화
                                item['isValueDisabled'] = false;

                            }
                            else {
                                item['directInputChecked'] = false;
                            }

                            // 품목별 상품정보 제공고시의 해당 항목에 별도 입력값 존재시
                            if (itemSpecValueObj['specFieldValue']) {

                                // 품목별 상품정보 제공고시 세부 항목의 값을 복사
                                item['basicValue'] = itemSpecValueObj['specFieldValue'];

                            }
                                // 품목별 상품정보 제공고시 세부 항목의 값을 복사
                                item['changedSpecValue'] = itemSpecValueObj['changedSpecValue'];

                        });

                    }

                    if(!fnIsEmpty(data) && data.changedSpecMaster == true) {
                        $('#labelSpecMaster').addClass('approvalChanged'); // 상품정보 제공고시 변경여부
                    }

                    // viewModel 에 상품정보 제공고시 분류 / 항목 Map 추가
                    viewModel.set('itemSpecMap', itemSpecMap);

                    // 인자로 받은 상품정보 제공고시 분류코드를 viewModel 에 세팅 : 상품정보 제공고시의 상품군이 해당 코드로 선택됨
                    viewModel.set('selectedItemSpecMasterId', ilSpecMasterId);

                    // 해당 상품정보제공고시 분류 PK 에 해당하는 상세항목 목록을 itemSpecValueList 에 추가
                    var itemSpecValueListOfChosenItemSpecMasterId = itemSpecMap[ilSpecMasterId];
                    viewModel.set('itemSpecValueList', itemSpecValueListOfChosenItemSpecMasterId);

                    viewModel.trigger("change", {
                        field : "itemSpecValueList"
                    });

                }

            },
            isAction : 'select',
        });

    }

    // 해당 상품정보제공고시 항목 코드의 수정 메시지 조회
    function fnGetItemSpecFieldDetailMessage(specFieldCode) {

        var itemSpecModifiedMessage;

        // 유통기간 : ERP 연동여부에 따라 ERP 또는 BOS 유통기간 분기
        var distributionPeriod = ( viewModel.get('isErpItemLink') == true ? viewModel.get('erpDistributionPeriod') : viewModel.get('bosDistributionPeriod') )

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

    function fnPoTypeDetailInformationPopup() { // 발주 상세정보 팝업 호출

        fnKendoPopup({
            id : 'fnPoTypeDetailInformationPopup',
            title : '발주 상세정보',
            src : '#/poTypeDetailInformationPopup',
            width : '750px',
            height : '400px',
            param : {
                erpItemCode : viewModel.get("erpItemCode"),
                erpItemName : viewModel.get("erpItemName"),
                ilPoTpId : viewModel.get("ilPoTpId"),
            }
        });

    }

    function fnItemPriceHistoryPopup( popupObj ) { // 가격 정보 전체 이력보기 팝업 호출

        var popupId = $(popupObj).attr('id'); // 해당 가격정보 이력보기 팝업의 ID

        fnKendoPopup({
            id : popupId,
            title : '온라인 통합 가격정보 내역',
            src : '#/itemPriceHistoryPopup',
            width : '1000px',
            height : '500px',
            param : {
                ilItemCode : ( viewModel.get("isErpItemLink") == true ? viewModel.get("erpItemCode") : viewModel.get('bosItemCode') ),
            }
        });
    }

    function fnItemDiscountPriceHistoryPopup( popupObj ) { // 가격 정보 전체 이력보기 팝업 호출

        var popupId = $(popupObj).attr('id'); // 해당 가격정보 이력보기 팝업의 ID

        fnKendoPopup({
            id : popupId,
            title : '가격정보 전체 이력',
            src : '#/itemPriceDiscountHistoryPopup',
            width : '1000px',
            height : '500px',
            param : {
                ilItemCode : ( viewModel.get("isErpItemLink") == true ? viewModel.get("erpItemCode") : viewModel.get('bosItemCode') ),
            }
        });
    }

    function fnItemPriceAddPopup( popupObj ) { // 가격 정보 추가 등록 팝업


        var popupId = $(popupObj).attr('id'); // 해당 가격정보 이력보기 팝업의 ID

        var priceList = viewModel.get("itemPriceScheduleAfterList");

        let params = {};
        params.itemInfo = {
        		ilItemCd : viewModel.get("isErpItemLink") == true ? viewModel.get("erpItemCode") : viewModel.get('bosItemCode') ,	 // 상품 ID
        		itemPriceList : priceList,
        		isErpItemLink: viewModel.get('isErpItemLink'),
        		erpLegalTypeCode : viewModel.get('erpLegalTypeCode'),
        		productType : viewModel.get('productType'),
        		taxYn : viewModel.get("isErpItemLink") == true ? viewModel.get("erpTaxYn") : viewModel.get("taxYn"),
        		newInit : false,
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

            	var ilItemCode = viewModel.get("isErpItemLink") == true ? viewModel.get("erpItemCode") : viewModel.get('bosItemCode') ;
            	fnGetItemPriceList(ilItemCode);
            }
        });
    }

    function addNutritionDetailRow(nutritionCode) { // 인자로 받은 영양분류 코드로 상품 영양정보 항목에 Row 추가

        // 분류코드명 : 영양정보 분류코드 Map 을 조회하여 세팅
        var nutritionCodeName = addAvailableNutritionCodeMap[nutritionCode];
        var nutritionUnit = (addAvailableNutritionUnitMap[nutritionCode] ? addAvailableNutritionUnitMap[nutritionCode] : ''); // 단위 미등록시는 '' 출력

        // BOS 에서 영양소 기준치 % 사용여부 : addAvailableNutritionPercentMap 에 등록된 영양코드인 경우 true
        var nutritionPercentYn = (addAvailableNutritionPercentMap[nutritionCode] ? true : false);

        // viewModel 에 선택한 영양정보 코드로 해당 Data 추가
        viewModel.get('itemNutritionDetailList').push({
            canDeleted : true, // 삭제 버튼 노출 여부
            nutritionCodeName : nutritionCodeName, // 영양정보 분류코드명
            nutritionCode : nutritionCode, // 영양정보 분류코드
            erpNutritionQuantity : '', // ERP 영양성분량
            nutritionQuantity : '', // BOS 영양성분량 : 영양 최초 항목 추가시 기본값 ""
            nutritionUnit : nutritionUnit, // BOS 영양성분량 단위
            erpNutritionPercent : '', // ERP 영양성분 기준치대비 함량
            nutritionPercent : '', // BOS 영양성분 기준치대비 함량 : 최초 항목 추가시 기본값 ""
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

    function fnGetItemWarehouseCodeList(urSupplierId) { // 공급업체 PK 로 출고처 그룹, 출고처 정보 조회

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

                        // 첫 번째 출고처 그룹으로 선택 세팅 후 해당 출고처 그룹 코드의 출고처 목록을 출고처 Select 에 세팅
                        var firstWarehouseGroupCode = data['warehouseGroupCodeList'][0]['CODE'];
                        var supplierListOfFirstWarehouseGroupCode = viewModel.get('itemWarehouseMap')[firstWarehouseGroupCode];

                        fnKendoDropDownList({ // 기존 출고처 dropdown Data 초기화
                            id : 'warehouse',
                            data : {},
                            textField : "warehouseName",
                            valueField : "urSupplierWarehouseId",
                            blank : '출고처 선택',
                        });

                        viewModel.set('selectedUrSupplierWarehouseId', ''); // 기존 선택된 출고처 값 초기화

                    }

                }

            }
        });
    }

    function fnGetItemWarehouseList(ilItemCode) { // 해당 품목 코드로 출고처 정보 조회

        fnAjax({
            url : "/admin/item/master/register/getItemWarehouseList",
            method : 'GET',
            params : {
                ilItemCode : ilItemCode
            },
            isAction : 'select',
            success : function(data, status, xhr) {

                /*
                 * 해당 품목의 출고처 정보 조회시
                 */
                if (data['itemWarehouseList'] && data['itemWarehouseList'].length > 0) {

                    viewModel.set('noItemWarehouseInfo', false);

                    viewModel.set('itemWarehouseList', data['itemWarehouseList']);

                    for(var i=0; i < data.itemWarehouseList.length; i++){

                		if(data.itemWarehouseList[i].stockOrderYn != "Y") { // 재고 발주 미연동일 경우 발주유형 select box 노출하지 않는다.
                    		viewModel.set("itemWarehouseList["+i+"].itemWarehousePoTpIdTemplateList", data.itemWarehouseList[i].ilPoTpId);
                    		$("#itemWarehousePoTpIdTemplateList"+data.itemWarehouseList[i].urWarehouseId).hide();
                		}
                		else {
                            if(viewModel.get("bosSupplier") == "2" && (viewModel.get("erpPoTypeDisplayName").indexOf("센터(R2)") != -1)) {
                            	fnKendoDropDownList({
        							id			: "itemWarehousePoTpIdTemplateList"+data.itemWarehouseList[i].urWarehouseId,
        							url			: "/admin/item/master/register/getItemPoTypeCode",
        							params		: {urSupplierId : viewModel.get("bosSupplier"), selectType : "all"},
        							textField	:"poTypeName",
        							valueField	: "poTypeCode",
        							blank : '선택'
        						});

                        		viewModel.set("ilPoTpId", data.itemWarehouseList[i].ilPoTpId);
                        		viewModel.set("itemWarehouseList["+i+"].itemWarehousePoTpIdTemplateList", data.itemWarehouseList[i].ilPoTpId);
        						fnBosPoPerItemVisible(data.itemWarehouseList[i].urWarehouseId, data.itemWarehouseList[i].ilPoTpId);

        						$("#itemWarehousePoTpIdTemplateList"+data.itemWarehouseList[i].urWarehouseId).data("kendoDropDownList").enable(false);
                        	} else {
                        		$("#itemWarehousePoTpIdTemplateList"+data.itemWarehouseList[i].urWarehouseId).show();

                        		fnKendoDropDownList({
        							id			: "itemWarehousePoTpIdTemplateList"+data.itemWarehouseList[i].urWarehouseId,
        							url			: "/admin/item/master/register/getItemPoTypeCode",
        							params		: {urSupplierId : viewModel.get("bosSupplier")},
        							textField	:"poTypeName",
        							valueField	: "poTypeCode",
        							blank : '선택'
        						});
        						viewModel.set("itemWarehouseList["+i+"].itemWarehousePoTpIdTemplateList", data.itemWarehouseList[i].ilPoTpId);
        						fnBosPoPerItemVisible(data.itemWarehouseList[i].urWarehouseId, data.itemWarehouseList[i].ilPoTpId);

        						if(
        							(checkApprovalView() != null) // 승인에서 넘어온 경우
        							|| PG_SESSION.companyType == "COMPANY_TYPE.CLIENT" // 거래처의 경우
        						) { // 발주유형(BOS)를 변경할 수 없음.
            						$("#itemWarehousePoTpIdTemplateList"+data.itemWarehouseList[i].urWarehouseId).data("kendoDropDownList").enable(false);
        						}

                        	}
                		}
					}

                }

            }
        });


    }

    function addStoreWareHouse() {
    	var storeWarehouseData = null;

    	var isStoreWarehouse = viewModel.get('itemWarehouseList').find(function (e) {
    		return e.storeWarehouseYn == 'Y'
		});

    	if(!isStoreWarehouse){
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
	    		storeWarehouseData['canDeleted'] = true;
	    		viewModel.get('itemWarehouseList').push(storeWarehouseData);
	            $("#itemWarehousePoTpIdTemplateList"+storeWarehouseData.urWarehouseId).hide();
	    	}
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

    // 등록 가능한 영양정보 분류코드 리스트 / 해당 품목의 영양정보 상세 목록 조회
    function fnGetAddAvailableNutritionCodeList(ilItemCode, isErpItemLink) {

    	fnAjax({
            url : "/admin/item/master/register/getAddAvailableNutritionCodeList",
            method : 'GET',
            params : {
                ilItemCode : ilItemCode,
                isErpItemLink : isErpItemLink
                , ilItemApprId : checkApprovalView()
            },
            async: false,
            isAction : 'select',
            success : function(data) {

                // 등록 가능한 영양정보 코드 조회시
                if (data['addAvailableNutritionCodeList'] && data['addAvailableNutritionCodeList'].length > 0) {

                    // 등록 가능한 영양정보 분류코드 목록을 viewModel 에 등록
                    viewModel.set( 'addAvailableNutritionCodeList', data['addAvailableNutritionCodeList'] );

                    fnKendoDropDownList({ // 등록 가능한 영양정보 분류코드 리스트
                        id : 'addAvailableNutritionCode',
                        data : viewModel.get('addAvailableNutritionCodeList'),
                        textField : "nutritionName",
                        valueField : "nutritionCode"
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
                     * 해당 품목 코드로 기동록된 영양정보 세부항목 있는 경우 viewModel 에 세팅
                     *
                     * 이 때 addAvailableNutritionCodeMap, addAvailableNutritionUnitMap, addAvailableNutritionPercentMap 의
                     *
                     * 정보가 먼저 세팅되어야 함
                     */
                    if (data['itemNutritionDetailList'] && data['itemNutritionDetailList'].length > 0) {
                        fnSetItemNutritionDetail(data['itemNutritionDetailList']);
                    }
                    if(!fnIsEmpty(data) && data.changedItemNutrition == true) {
                        $('#labelItemNutrition').addClass('approvalChanged'); // 상품영양정보 변경여부
                    }
                }
            }
        });
    }

    function fnSetItemNutritionDetail(itemNutritionDetailList) { // 해당 품목의 영양정보 상세항목 화면에 출력

        viewModel.set('itemNutritionDetailList', []); // 기존 영양정보 세부항목 초기화

        for (var i = 0; i < itemNutritionDetailList.length; i++) {

            var nutritionDetailInfo = itemNutritionDetailList[i];

            var nutritionCode = nutritionDetailInfo['nutritionCode'];

            // 분류코드명 : 영양정보 분류코드 Map 을 조회하여 세팅
            var nutritionCodeName = addAvailableNutritionCodeMap[nutritionCode];
            var nutritionUnit = (addAvailableNutritionUnitMap[nutritionCode] ? addAvailableNutritionUnitMap[nutritionCode] : ''); //단위 미등록시는 '' 출력
            var erpNutritionQuantity = nutritionDetailInfo['erpNutritionQuantity'];
            var erpNutritionPercent = nutritionDetailInfo['erpNutritionPercent'];
            var nutritionQuantity = nutritionDetailInfo['nutritionQuantity']; // 복사한 BOS 영양성분량
            var nutritionPercent = nutritionDetailInfo['nutritionPercent']; // 복사한 BOS 영양성분 기준치대비 함량
            var changedNutritionCode = nutritionDetailInfo['changedNutritionCode']; // 영양정보 수정여부

            // 영양정보 세부항목 삭제 버튼 노출 여부 : ERP 연동 항목으로 관련 데이터 존재시 삭제 불가
            var canDeleted = nutritionDetailInfo['canDeleted'];

            // BOS 에서 영양소 기준치 % 사용여부 : addAvailableNutritionUnitMap 에 등록된 경우 true
            var nutritionPercentYn = (addAvailableNutritionPercentMap[nutritionCode] ? true : false);

            viewModel.get('itemNutritionDetailList').push({
                canDeleted : canDeleted, // 삭제 버튼 노출 여부 : ERP 연동 항목인 경우 삭제 불가
                nutritionCodeName : nutritionCodeName, // 영양정보 분류코드명
                nutritionCode : nutritionCode, // 영양정보 분류코드
                erpNutritionQuantity : erpNutritionQuantity, // ERP 영양성분량
                nutritionQuantity : nutritionQuantity, // BOS 영양성분량
                nutritionUnit : nutritionUnit, // BOS 영양성분량 단위
                erpNutritionPercent : erpNutritionPercent, // ERP 영양성분 기준치대비 함량
                nutritionPercent : nutritionPercent, // BOS 영양성분 기준치대비 함량
                nutritionPercentYn : nutritionPercentYn, // BOS 에서 영양소 기준치 % 사용여부
                changedNutritionCode : changedNutritionCode, // 영양성분 수정여부
            });

            // 상품 영양정보 항목에 추가된 항목은 등록 가능한 영양정보 분류코드 목록에서 삭제
            for( var j = viewModel.get('addAvailableNutritionCodeList').length - 1 ; j >= 0 ; j-- ) {

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

    function fnGetItemCertificationList(ilItemCode) { // 품목코드로 상품 인증정보 조회

    	fnAjax({
            url : "/admin/item/master/register/getItemCertificationList",
            method : 'GET',
            params : {
                ilItemCode : ilItemCode
                , ilItemApprId : checkApprovalView()
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

                    // 마스터 품목 복사를 위해 조회한 상품 인증정보 데이터를 viewModel 의 itemCertificationList 에 추가
                    for (var i = 0; i < data['itemCertificationList'].length; i++) {

                        var ilCertificationId = data['itemCertificationList'][i]['ilCertificationId'];

                        var itemCertificationCodeObj = viewModel.get('itemCertificationCodeMap')[ilCertificationId];
                        var itemCertificationValueObj = viewModel.get('itemCertificationValueMap')[ilCertificationId];

                        // 해당 상품 인증정보 ID 의 인증정보명
                        var certificationName = itemCertificationCodeObj['certificationName'];

                        // 해당 상품인증정보의 인증 이미지 URL
                        var certificationImageSrc = publicUrlPath + itemCertificationCodeObj['imagePath'] + itemCertificationCodeObj['imageName'];

                        console.log("certificationImageSrc : ", certificationImageSrc);

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

                        if (data['changedItemCertification'] == true) $('#labelItemCertification').addClass('approvalChanged');       // 상품 인증정보 변경여부
                        if (data['changedItemCertification'] == true) $('#labelItemCertificationSelect').addClass('approvalChanged'); // 인증정보 선택 변경여부

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

    // 해당 품목코드로 등록된 품목 이미지 목록 조회
    function fnGetItemImageList(ilItemCode) {

    	var addImageCount = 0; // 화면상에 추가된 이미지 개수

    	fnAjax({
            url : "/admin/item/master/register/getItemImageList",
            method : 'GET',
            params : {
                ilItemCode : ilItemCode
                , ilItemApprId : checkApprovalView()
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
                                //// viewModel.get('itemImageFileList').push(file);

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
                    if(!fnIsEmpty(data) && data.changedItemImage == true) {
                        $('#labelItemDetailImage').addClass('approvalChanged'); // 상품 상세이미지 변경여부
                        $('#labelItemImage').addClass('approvalChanged'); // 상품이미지 변경여부
                    }
               }

            }, // end of success
            isAction : 'select'
        });

    }

    // validation 체크 : 유효하지 않은 경우 false 반환
    function fnValidationCheck(functionName, data) {

        switch (functionName) {

        case 'update': // 품목 정보 수정

            // ERP 미연동 품목인 경우 : 마스터 품목명, 품목바코드 입력 여부 체크
            if ( viewModel.get('isErpItemLink') == false ) {

                if( ! viewModel.get('itemName') || $.trim( viewModel.get('itemName') ).length == 0 ) {
                    valueCheck('마스터 품목명 값을 입력하지 않았습니다.', 'itemName');
                    return false;
                }

            } else { // ERP 연동 품목 : 마스터 폼목명 입력 여부 체크

                if( ! viewModel.get('itemName') || $.trim( viewModel.get('itemName') ).length == 0 ) {
                    valueCheck('마스터 품목명 값을 입력하지 않았습니다.', 'itemName');
                    return false;
                }

            }

            if (checkCategoryStandardId() == false) { // 선택 가능한 하위 카테고리까지 선택하지 않은 경우
                valueCheck('선택 가능한 하위 표준 카테고리가 존재합니다.<br>최종 분류 선택시 저장이 가능합니다.', 'categoryStandardDepth4');
                return false;
            }

            if (checkCategoryStandardId() == null) { // 표준 카테고리 지정 여부 체크
                valueCheck('표준 카테고리를 선택하지 않았습니다.', 'categoryStandardDepth4');
                return false;
            }

            if (viewModel.get("bosSupplier") == '') { // 공급업체
                valueCheck('공급업체를 선택하지 않았습니다.', 'bosSupplier');
                return false;
            }

            if (viewModel.get("bosBrand") == '') { // BOS 브랜드
                valueCheck('브랜드를 선택하지 않았습니다.', 'bosBrand');
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

            if( parseInt( viewModel.get('newStandardPrice') ) > parseInt( viewModel.get('newRecommendedPrice') ) ) {
                valueCheck('정상가는 원가보다 낮게 입력할 수 없습니다.<br>가격정보를 확인해주세요.', '');
                return false;
            }

            if( viewModel.get('showItemPriceAddRow') == true ) {  // 가격 정보 입력 행이 visible 상태인 경우

                if( viewModel.get('newPriceApplyStartDate') == null || $.trim( viewModel.get('newPriceApplyStartDate') ) == '' ) {
                    valueCheck('가격정보 적용 시작일이 정확히 입력되지 않았습니다.<br>가격정보를 확인해주세요.', '');
                    return false;
                }

                if( viewModel.get('newStandardPrice') == null || $.trim( viewModel.get('newStandardPrice') ) == '' ) {
                    valueCheck('원가가 입력되지 않았습니다.<br>가격정보를 확인해주세요.', '');
                    return false;
                }

                if( viewModel.get('newRecommendedPrice') == null || $.trim( viewModel.get('newRecommendedPrice') ) == '' ) {
                    valueCheck('정상가가 입력되지 않았습니다.<br>가격정보를 확인해주세요.', '');
                    return false;
                }

            }

            if( viewModel.get('nutritionDisplayYn') == 'Y' ) {   // 영양정보 표시여부 '노출' 선택시

                // 화면상의 영양정보 세부항목 목록
                var itemNutritionDetailList = viewModel.get('itemNutritionDetailList');

                for (var i = itemNutritionDetailList.length - 1; i >= 0; i--) { // 화면상의 영양정보 세부항목 목록

                    // float 여부 체크 : 추후 Validaiont 체크 로직 보완 예정
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
            if(approvalChkVal) {
    			let itemApprList = [];
				itemApprList = $("#apprGrid").data("kendoGrid").dataSource.data()

				if(itemApprList.length == 0) {
            		valueCheck('승인관리자 정보를 선택하여 주세요.', '');
                    return false;
            	}else {
            		viewModel.set("itemApprList", itemApprList);
            	}
            }

//            viewModel.set("itemStatusTp", "ITEM_STATUS_TP.SAVE");

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

        // 상품 이미지 데이터 초기화
        viewModel.set('itemImageList', []);
        viewModel.set('itemImageFileList', []); // 기존 상품 이미지 파일 목록 초기화

        // 초기화 이전에 사용자가 선택한 마스터 품목 유형 값
        var beforeMasterItemType = viewModel.get('masterItemType');

        // 초기화 이전에 사용자가 선택한 ERP 품목 연동여부
        var beforeErpItemLinkYn = viewModel.get('erpItemLinkYn');

        kendo.unbind($("#view"));  // 기존 viewModel binding 해제

        // viewModelOriginal 을 deepClone 한 객체로 viewModel 초기화
        viewModel = {};
        viewModel = kendo.observable( deepClone(viewModelOriginal) );

        // 영양정보 상세 항목에 적용된 Kendo Sortable 강제 초기화
        $('#nutritionDetailContainer').html('');

        // 상품 이미지 목록에 적용된 Kendo Sortable 강제 초기화
        $('#divContainer').html('');

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

        setTimeout(fnInitItemMgm(), 500); // Kendo Component 초기화

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

        console.log("################# checkCateGory############");

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

        // 표준카테고리 시작
        fnKendoDropDownList({
            id : "categoryStandardDepth1",
            tagId : "categoryStandardDepth1",
            url : "/admin/comn/getDropDownCategoryStandardList",
            params : { "depth" : "1" },
            textField : "categoryName",
            valueField : "categoryId",
            blank : "대분류",
            async : false,
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
            cscdField : "categoryId",
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
            cscdField : "categoryId",
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
            cscdField : "categoryId",
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

        fnKendoDropDownList({
            id : 'bosBrand', // BOS 브랜드 : 최초 화면 조회시에는 출력할 Data 없음
            data : [ {
                code : '',
                name : '선택'
            } ],
            valueField : 'code',
            textField : 'name'
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

        // viewModel.set('bosOriginType', 'ORIGIN_TYPE.DOMESTIC'); // 기본값 '국내' 선택

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

        fnKendoDatePicker({   // BOS 가격 적용 시작일
            id: "newPriceApplyStartDate",
            format: "yyyy-MM-dd",
            min : fnGetDayAdd( fnGetToday(), 1 ),  // 현재일자 기준 D + 1 일부터 선택 가능
            change : function(e) {
            }
        });

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

        viewModel.set('nutritionDisplayYn', 'Y'); // 영양정보 표시여부 기본값 '노출' 선택

        fnKendoDropDownList({ // 등록 가능한 영양정보 분류코드 리스트 : 최초 화면 조회시는 데이터 없음
            id : 'addAvailableNutritionCode',
            data : {},
            textField : "NAME",
            valueField : "CODE",
            blank : '선택'
        });

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

                // fnGetItemSpecList(null, null); // 상품정보 제공고시 상세 항목 전체 조회

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
                // 화면에서는 배송불가 여부를 따지므로 viewModel 에 값을 반대로 세팅
                viewModel.set('islandShippingYn', !$("input[name=shippingYn]:eq(0)").is(":checked"))
                viewModel.set('jejuShippingYn', !$("input[name=shippingYn]:eq(1)").is(":checked"))

            }
        });

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
//            change : function(e) {
//
//                // 단종여부 Radio 체크된 값 변경시 viewModel 에 세팅
//                viewModel.set('extinctionYn', $("input[name=extinctionYn]:checked").val());
//
//            }
//        });

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

    function fnItemUpdateHistorySearch() {
        let option = {};
        option.url = "#/itemChangeLogList";
        option.data = {"paramIlItemCode": pageParam['ilItemCode']};
        option.target = "itemChangeLogList";

        fnGoNewPage(option);
    }

    function fnShowItemPriceAddRow(newPriceRowId) {  // 가격 정보 신규 입력 항목 Visible 처리

        if( viewModel.get('showItemPriceAddRow') == true ) {

            fnKendoMessage({
                message : '이미 추가되었습니다.',
            });

            return;
        }

        var currentPriceScheduleListName = viewModel.get('currentPriceScheduleListName');

        // 조회된 가격정보 중 가장 마지막 가격 정보 row 데이터
        var previousPriceRow = viewModel.get(currentPriceScheduleListName)[ viewModel.get(currentPriceScheduleListName).length - 1 ];

        var previousPriceApplyStartDate = previousPriceRow['priceApplyStartDate'];   // 가장 마지막 가격 정보 row 데이터의 가격 적용 시작일
        var today = fnGetToday();  // 현재 일자
        var priceApplyStartMinDate = '';  // 가격적용 시작일 최소값

        if( previousPriceApplyStartDate <= today ) {  // 가장 마지막 가격적용 시작일이 현재 날짜보다 같거나 작은 경우

            priceApplyStartMinDate = fnGetDayAdd( today, 1 );  // 현재일자 기준 D + 1 부터 가격적용 시작일 지정 가능

        } else {  // 가장 마지막 가격적용 시작일이 미래 날짜 : 가격 적용 예정 데이터 존재

            priceApplyStartMinDate = fnGetDayAdd( previousPriceApplyStartDate, 1 );  //가장 마지막 가격 적용 예정 시작일 기준 D+1 부터 시작일 지정 가능

        }

        switch(newPriceRowId) {

        case 'foodmerce-erp-item-price-row' :  // 푸드머스 ERP

            fnKendoDatePicker({   // 푸드머스 ERP 가격 적용 시작일
                id: "newFoodmerceErpItemPriceApplyStartDate",
                format: "yyyy-MM-dd",
                min : priceApplyStartMinDate,  // priceApplyStartMinDate 부터 가격 적용 시작일로 지정 가능
                change : function(e) {
                }
            });

            viewModel.set('newPriceApplyStartDate', priceApplyStartMinDate); // 가격 적용 시작일
            viewModel.set('newPriceApplyEndDate', '');
            viewModel.set('newStandardPrice', previousPriceRow['standardPrice']); // 가장 마지막 가격 정보 row 의 원가 데이터
            viewModel.set('newRecommendedPrice', '');

            break;


        case 'lohas-products-erp-item-price-row' : // 건강생활 ERP 중 제품

            fnKendoDatePicker({   // 건강생활 ERP 중 제품 품목 가격 적용 시작일
                id: "lohasProductsErpItemPriceApplyStartDate",
                format: "yyyy-MM-dd",
                min : priceApplyStartMinDate,  // priceApplyStartMinDate 부터 가격 적용 시작일로 지정 가능
                change : function(e) {
                }
            });

            viewModel.set('newPriceApplyStartDate', priceApplyStartMinDate);   // 가격 적용 시작일
            viewModel.set('newPriceApplyEndDate', '');
            viewModel.set('newStandardPrice', '');
            viewModel.set('newRecommendedPrice', '');

            break;

        case 'lohas-goods-erp-item-price-row' : // 건강생활 ERP 중 상품

            fnKendoDatePicker({   // 건강생활 ERP 중 상품 품목 가격 적용 시작일
                id: "lohasGoodsErpItemPriceApplyStartDate",
                format: "yyyy-MM-dd",
                min : priceApplyStartMinDate,  // priceApplyStartMinDate 부터 가격 적용 시작일로 지정 가능
                change : function(e) {
                }
            });

            viewModel.set('newPriceApplyStartDate', priceApplyStartMinDate); // 가격 적용 시작일
            viewModel.set('newPriceApplyEndDate', '');
            viewModel.set('newStandardPrice', previousPriceRow['standardPrice']); // 가장 마지막 가격 정보 row 의 원가 데이터
            viewModel.set('newRecommendedPrice', '');

            break;

        case 'erp-not-link-new-price-row' : // ERP 미연동 품목

            fnKendoDatePicker({   // ERP 미연동 품목 가격 적용 시작일
                id: "erpNotLinkItemPriceApplyStartDate",
                format: "yyyy-MM-dd",
                min : priceApplyStartMinDate,  // priceApplyStartMinDate 부터 가격 적용 시작일로 지정 가능
                change : function(e) {
                }
            });

            viewModel.set('newPriceApplyStartDate', priceApplyStartMinDate);   // 가격 적용 시작일
            viewModel.set('newPriceApplyEndDate', '');
            viewModel.set('newStandardPrice', '');
            viewModel.set('newRecommendedPrice', '');

            break;

        }

        viewModel.set('showItemPriceAddRow', true);

    }

    function fnBosPoTypeChange(obj) {

    	var poTpArray = $(obj).attr("id").split("itemWarehousePoTpIdTemplateList");

    	for( var i = viewModel.get('itemPoTypeCodeList').length - 1 ; i >= 0 ; i -- ) {
    		// 사용자가 선택한 발주유형 코드의 발주일 설정값 품목별 상이 여부 값이 true 인 경우
    		if( viewModel.get('itemPoTypeCodeList')[i]['poTypeCode'] == $(obj).val()) {

    			$(obj).val(viewModel.get('itemPoTypeCodeList')[i]['poTypeCode']);

    			if(viewModel.get('itemPoTypeCodeList')[i]['poPerItemYn'] == true ) {
    				if(viewModel.get('itemPoTypeCodeList')[i]['templateYn'] == "N") {
    					$("#poTypeDetailPopup"+warehouseId).show(); 				// 발주유형 상세정보 popup 버튼 Visible
    				}else {
    					$("#poTypeDetailPopup"+warehouseId).hide(); 				// 발주유형 상세정보 popup 버튼 Visible 해제
    				}
    			} else {
    				$("#poTypeDetailPopup"+poTpArray[1]).hide(); 				// 발주유형 상세정보 popup 버튼 Visible 해제
    			}
    			break;
    		}
    	}
    }

    function fnBosPoPerItemVisible(warehouseId, poTpId) {


    	if(poTpId != null) {
    		for( var i = viewModel.get('itemPoTypeCodeList').length - 1 ; i >= 0 ; i -- ) {
        		// 사용자가 선택한 발주유형 코드의 발주일 설정값 품목별 상이 여부 값이 true 인 경우
        		if( viewModel.get('itemPoTypeCodeList')[i]['poTypeCode'] == poTpId ) {
        			if(viewModel.get('itemPoTypeCodeList')[i]['poPerItemYn'] == true ) {
        				if(viewModel.get('itemPoTypeCodeList')[i]['templateYn'] == "N") {
        					$("#poTypeDetailPopup"+warehouseId).show(); 				// 발주유형 상세정보 popup 버튼 Visible
        				}else {
        					$("#poTypeDetailPopup"+warehouseId).hide(); 				// 발주유형 상세정보 popup 버튼 Visible 해제
        				}
        			} else {
        				$("#poTypeDetailPopup"+warehouseId).hide(); 				// 발주유형 상세정보 popup 버튼 Visible 해제
        			}
        			break;
        		}
        	}
    	}
    }

	function fnApproInitialize(){
		fnApproInit();
		fnApprAdminInit();

		if(PG_SESSION.companyType == "COMPANY_TYPE.CLIENT") {
			$("#inputForm input").attr("disabled", true);
		}
	}

    function fnApproInit(){
		//승인관리자 정보
//		$('#approvalCheckbox').prop("checked",true);
//		$('#apprDiv').show();

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

	// 승인관리자 선택 팝업 호출
	function fnApprAdmin(){
//		console.log("### apprKindTp  ===>"+ apprKindTp);
		var param = {'taskCode' : apprKindTp };

		console.log("#### param --->"+ JSON.stringify(param));
		fnKendoPopup({
			id		: 'approvalManagerSearchPopup',
			title	: '승인관리자 선택',
			src		: '#/approvalManagerSearchPopup',
			param	: param,
			width	: '1300px',
			heigh	: '800px',
			scrollable : "yes",
			success: function( stMenuId, data ){

				if(data && !fnIsEmpty(data) && data.authManager2nd){
					$('#apprGrid').gridClear(true);

					var authManager1 = data.authManager1st;
					var authManager2 = data.authManager2nd;

					if(authManager1.apprUserId != undefined){							//1차 승인관리자가 미지정이라면
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

	function fnApprCancel() {
		let params = {};
		params.ilItemApprIdList = [];
		let url = "";

		if(PG_SESSION.companyType == "COMPANY_TYPE.HEADQUARTERS"){
			if( viewModel.itemRegistItemApprId == null && viewModel.itemRegistItemApprId == undefined){
				fnKendoMessage({ message : "승인 내역에 문제가 있습니다. 관리자에게 문의하세요." });
				return;
			}

			params.ilItemApprIdList[0] = viewModel.itemRegistItemApprId;
			url = "/admin/approval/item/putCancelRequestApprovalItemRegist";
		}
		else if(PG_SESSION.companyType == "COMPANY_TYPE.CLIENT"){
			if( viewModel.itemClientItemApprId == null && viewModel.itemClientItemApprId == undefined){
				fnKendoMessage({ message : "승인 내역에 문제가 있습니다. 관리자에게 문의하세요." });
				return;
			}

			params.ilItemApprIdList[0] = viewModel.itemClientItemApprId;
			url = "/admin/approval/item/putCancelRequestApprovalItemClient";
		}

		fnKendoMessage({
			type : "confirm",
			message : "요청철회 하시겠습니까?",
			ok : function() {
				fnAjax({
					url			: url,
					params		: params,
					contentType	: "application/json",
					success		: function( data ){
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
					error		: function(xhr, status, strError){
						fnKendoMessage({ message : xhr.responseText });
					},
					isAction : "update"
				});
			}
		});
	}

	function fnItemRegistApprDetailPopup(){
		if( viewModel.itemRegistItemApprId == null && viewModel.itemRegistItemApprId == undefined){
			fnKendoMessage({ message : "승인 내역에 문제가 있습니다. 관리자에게 문의하세요." });
			return;
		}

//		fnKendoPopup({
//			id		: 'itemApprovalDetailPopup',
//			title	: "품목등록 승인내역",
//			src		: '#/approvalHistoryPopup',
//			param	: { "taskCode" : "APPR_KIND_TP.ITEM_REGIST", "taskPk" : viewModel.itemRegistItemApprId},
//			width	: '680px',
//			height	: '585px',
//			success	: function( id, data ){
//			}
//		});

		let companyType = PG_SESSION.companyType;		// 회사타입
		if (companyType == "COMPANY_TYPE.HEADQUARTERS") {
			var option = new Object();
			if (
				(viewModel.get("itemRegistApprReqUserId") == PG_SESSION.userId) // 품목등록 승인 요청자이면서
				&& (viewModel.get("itemRegistApprStat") != 'APPR_STAT.APPROVED') // 품목등록 승인완료 상태가 아니면
			)
				option.url = "#/approvalItemRegistRequest"; // 품목등록 승인요청 URL
			else
				option.url = "#/approvalItemRegistAccept"; // 품목등록 승인관리 URL

			option.target = "_blank";
			option.data = { // 승인 화면으로 전달할 Data
				ilItemCode : (viewModel.get("isErpItemLink") == true ? viewModel.get("erpItemCode") : viewModel.get('bosItemCode'))
			};
			fnGoNewPage(option);
		}
	}

	function fnItemClientApprDetailPopup() {
		if (viewModel.itemClientItemApprId == null && viewModel.itemClientItemApprId == undefined) {
			fnKendoMessage({ message : "승인 내역에 문제가 있습니다. 관리자에게 문의하세요." });
			return;
		}

//		fnKendoPopup({
//		id		: 'itemApprovalDetailPopup',
//		title	: "거래처 품목수정 승인내역",
//		src		: '#/approvalHistoryPopup',
//		param	: { "taskCode" : "APPR_KIND_TP.ITEM_CLIENT", "taskPk" : viewModel.itemClientItemApprId},
//		width	: '680px',
//		height	: '585px',
//		success	: function( id, data ){
//		}
//	});

		let companyType = PG_SESSION.companyType;		// 회사타입
		var option = new Object();
		if (companyType == "COMPANY_TYPE.HEADQUARTERS")
			option.url = "#/approvalItemClientAccept"; // 거래처 품목 승인관리 URL
		else
			option.url = "#/approvalItemClientRequest"; // 거래처 품목 승인요청 URL

		option.target = "_blank";
		option.data = { // 승인 화면으로 전달할 Data
			ilItemCode : (viewModel.get("isErpItemLink") == true ? viewModel.get("erpItemCode") : viewModel.get('bosItemCode'))
		};
		fnGoNewPage(option);
	}

	function fnGoodsRegistApprDetailPopup(){
		if (fnIsEmpty(viewModel.goodsRegistApprStat)) {
			fnKendoMessage({ message : "승인 내역에 문제가 있습니다. 관리자에게 문의하세요." });
			return;
		}

		let companyType = PG_SESSION.companyType;		// 회사타입
		if (companyType == "COMPANY_TYPE.HEADQUARTERS") {
			var option = new Object();
			option.url = "#/approvalGoodsRegistAccept"; // 상품등록 승인관리 URL
			option.target = "_blank";
			option.data = { // 승인 화면으로 전달할 Data
				ilItemCode : (viewModel.get("isErpItemLink") == true ? viewModel.get("erpItemCode") : viewModel.get('bosItemCode'))
			};
			fnGoNewPage(option);
		}
	}

	function fnGoodsClientApprDetailPopup() {
		if (fnIsEmpty(viewModel.goodsClientApprStat)) {
			fnKendoMessage({ message : "승인 내역에 문제가 있습니다. 관리자에게 문의하세요." });
			return;
		}

		let companyType = PG_SESSION.companyType;		// 회사타입
		var option = new Object();
		if (companyType == "COMPANY_TYPE.HEADQUARTERS")
			option.url = "#/approvalGoodsClientAccept"; // 거래처 상품 승인관리 URL
		else
			option.url = "#/approvalGoodsClientRequest"; // 거래처 상품 승인요청 URL

		option.target = "_blank";
		option.data = { // 승인 화면으로 전달할 Data
			ilItemCode : (viewModel.get("isErpItemLink") == true ? viewModel.get("erpItemCode") : viewModel.get('bosItemCode'))
		};
		fnGoNewPage(option);
	}

	function fnApprHtml(itemData) {

        // 승인 상태 정보
        viewModel.set("itemRegistItemApprId", itemData.itemRegistItemApprId); // 품목등록 승인 ID
        viewModel.set("itemRegistApprReqUserId", itemData.itemRegistApprReqUserId); // 품목등록 승인요청자 ID
        viewModel.set("itemRegistApprStat", itemData.itemRegistApprStat); // 품목등록 상태
        viewModel.set("itemRegistApprStatName", itemData.itemRegistApprStatName); // 품목등록 상태명
        viewModel.set("itemRegistApprStatusCmnt", itemData.itemRegistApprStatusCmnt); // 품목등록 상태변경 코멘트
        viewModel.set("itemClientItemApprId", itemData.itemClientItemApprId); // 거래처 품목수정 ID
        viewModel.set("itemClientApprReqUserId", itemData.itemClientApprReqUserId); // 거래처 품목수정요청자 ID
        viewModel.set("itemClientApprStat", itemData.itemClientApprStat); // 거래처 품목수정 상태
        viewModel.set("itemClientApprStatName", itemData.itemClientApprStatName); // 거래처 품목수정 상태명
        viewModel.set("itemClientApprStatusCmnt", itemData.itemClientApprStatusCmnt); // 거래처 품목수정 상태변경 코멘트
        viewModel.set("goodsRegistApprStat", itemData.goodsRegistApprStat); // 상품드옭 상태
        viewModel.set("goodsRegistApprStatName", itemData.goodsRegistApprStatName); // 상품등록 상태명
        viewModel.set("goodsClientApprStat", itemData.goodsClientApprStat); // 거래처 상품수정 상태
        viewModel.set("goodsClientApprStatName", itemData.goodsClientApprStatName); // 거래처 상품수정 상태명
		// 승인관련 화면 초기값 설정
		viewModel.set("itemApprovalStatusViewVisible", true);
		viewModel.set("visibleItemRegistApprDetailPopupButton", false);
		viewModel.set("itemRegistApprovalDeniedVisible", false);
		viewModel.set("visibleItemClientApprDetailPopupButton", false);
		viewModel.set("visibleGoodsRegistApprDetailPopupButton", false);
		viewModel.set("visibleGoodsClientApprDetailPopupButton", false);
		viewModel.set("saveBtnVisible", false);
		viewModel.set("apprCancelBtnVisible", false);
		$('#apprChkDiv').hide();
		$('#approvalCheckbox').prop("checked", false);
		$('#apprDiv').hide();

		if (checkApprovalView() != null) { // 거래처 승인리스트에서 넘어온 경우
			viewModel.set("itemApprovalStatusViewVisible", false);
			fnKendoMessage({ message : "승인요청 중인 품목정보 입니다.<BR>해당 정보는 수정이 불가하며, 승인완료 후 반영됩니다." });
		}
		else {
			if (PG_SESSION.companyType == "COMPANY_TYPE.HEADQUARTERS") { // 임직원인 경우
				// 품목등록 관련 S
				if (!fnIsEmpty(itemData.itemRegistApprStat))
					viewModel.set("visibleItemRegistApprDetailPopupButton", true); // 품목등록 승인 상세 팝업 버튼 노출

				if (itemData.itemRegistApprStat == 'APPR_STAT.DENIED')
					viewModel.set("itemRegistApprovalDeniedVisible", true); // 반려 사유 노출

				if (itemData.itemRegistApprStat == "APPR_STAT.REQUEST" && itemData.itemRegistApprReqUserId == PG_SESSION.userId) // 품목등록 요청상태에서 요청자일 경우
					viewModel.set("apprCancelBtnVisible", true); // 품목 등록 승인철회 버튼 노출
				// 품목등록 관련 E

				// 거래처 품목수정 관련 S
				if (itemData.itemClientApprStat == "APPR_STAT.REQUEST" || itemData.itemClientApprStat == "APPR_STAT.SUB_APPROVED") // 거래처 품목 수정 상세 팝업 버튼 노출 조건
					viewModel.set("visibleItemClientApprDetailPopupButton", true); // 거래처 품목수정 상세 팝업 버튼 노출
				else
					viewModel.set("itemClientApprStatName", ''); // 임직원의 경우 진행중인 승인건이 아니면 거래처 품목수정 상태명을 노출하지 않는다.
				// 거래처 품목수정 관련 E

				// 상품등록 관련 S
				if (itemData.goodsRegistApprStat == "APPR_STAT.REQUEST" || itemData.goodsRegistApprStat == "APPR_STAT.SUB_APPROVED") // 상품등록 상세 팝업 버튼 노출 조건
					viewModel.set("visibleGoodsRegistApprDetailPopupButton", true); // 상품등록 승인 상세 팝업 버튼 노출
				else
					viewModel.set("goodsRegistApprStatName", ''); // 승인건이 아니면 상품등록 상태명을 노출하지 않는다.
				// 상품등록 관련 E

				// 거래처 상품수정 관련 S
				if (itemData.goodsClientApprStat == "APPR_STAT.REQUEST" || itemData.goodsClientApprStat == "APPR_STAT.SUB_APPROVED") // 거래처 상품 수정 상세 팝업 버튼 노출 조건
					viewModel.set("visibleGoodsClientApprDetailPopupButton", true); // 거래처 상품수정 상세 팝업 버튼 노출
				else
					viewModel.set("goodsClientApprStatName", ''); // 승인건이 아니면 상품등록 상태명을 노출하지 않는다.
				// 거래처 상품수정 관련 E

				// 임직원인 경우, 저장버튼 및 승인관리자 지정 화면 처리 S
				if (
					!(itemData.itemRegistApprStat == "APPR_STAT.REQUEST" || itemData.itemRegistApprStat == "APPR_STAT.SUB_APPROVED") // 품목등록 승인 처리중이 아니고
					&& !(itemData.itemClientApprStat == "APPR_STAT.REQUEST" || itemData.itemClientApprStat == "APPR_STAT.SUB_APPROVED") // 거래처 품목수정 승인중이 아니고
					&& !(itemData.goodsRegistApprStat == "APPR_STAT.REQUEST" || itemData.goodsRegistApprStat == "APPR_STAT.SUB_APPROVED") // 상품등록 승인중이 아닐 경우
					&& !(itemData.goodsClientApprStat == "APPR_STAT.REQUEST" || itemData.goodsClientApprStat == "APPR_STAT.SUB_APPROVED") // 거래처 상품수정 승인중이 아닐 경우
				) {
					viewModel.set("saveBtnVisible", true); // 저장버튼 노출
					if (itemData.itemStatusTp == "ITEM_STATUS_TP.SAVE") { // 품목등록 완료가 아닐 경우 품목등록 승인 필요
						$('#apprChkDiv').show(); // 승인체크박스 노출
						$('#approvalCheckbox').prop("checked", true); // 승인체크박스 설정
						$('#apprDiv').show(); // 승인관리자 영역 노출
					}
				}
				else {
					if (itemData.itemRegistApprStat == "APPR_STAT.REQUEST" || itemData.itemRegistApprStat == "APPR_STAT.SUB_APPROVED") {
						viewModel.set("approvalStatusNotification", "*품목 등록 승인 처리중입니다. 해당 상태에서는 상세 정보 수정이 불가합니다.");
						fnKendoMessage({ message : "품목 등록 승인 처리중입니다.<BR>해당 상태에서는 상세 정보 수정이 불가합니다." });
					}
					else if (itemData.itemClientApprStat == "APPR_STAT.REQUEST" || itemData.itemClientApprStat == "APPR_STAT.SUB_APPROVED") {
						viewModel.set("approvalStatusNotification", "*거래처 품목 수정 승인 처리중입니다. 해당 상태에서는 상세 정보 수정이 불가합니다.");
						fnKendoMessage({ message : "거래처 품목 수정 승인 처리중입니다.<BR>해당 상태에서는 상세 정보 수정이 불가합니다." });
					}
					else if (itemData.goodsRegistApprStat == "APPR_STAT.REQUEST" || itemData.goodsRegistApprStat == "APPR_STAT.SUB_APPROVED") {
						viewModel.set("approvalStatusNotification", "*상품 등록 승인 처리중입니다. 해당 상태에서는 상세 정보 수정이 불가합니다.");
						fnKendoMessage({ message : "상품 등록 승인 처리중입니다.<BR>해당 상태에서는 상세 정보 수정이 불가합니다." });
					}
					else if (itemData.goodsClientApprStat == "APPR_STAT.REQUEST" || itemData.goodsClientApprStat == "APPR_STAT.SUB_APPROVED") {
						viewModel.set("approvalStatusNotification", "*거래처 상품 수정 승인 처리중입니다. 해당 상태에서는 상세 정보 수정이 불가합니다.");
						fnKendoMessage({ message : "거래처 상품 수정 승인 처리중입니다.<BR>해당 상태에서는 상세 정보 수정이 불가합니다." });
					}
				}
				// 임직원인 경우, 저장버튼 및 승인관리자 지정 화면 처리 E
			}
			else if (PG_SESSION.companyType == "COMPANY_TYPE.CLIENT") { // 거래처인 경우
				if (itemData.itemStatusTp == "ITEM_STATUS_TP.REGISTER") { // 거래처인 경우 품목 등록이 완료된 상태에서만 수정 가능
					// 거래처 품목수정 관련 S
					if (!fnIsEmpty(itemData.itemClientApprStat) && itemData.itemClientApprReqUserId == PG_SESSION.userId) // 거래처 품목 수정 요청자가 본인일 경우
						viewModel.set("visibleItemClientApprDetailPopupButton", true); // 거래처 품목수정 상세 팝업 버튼 노출

					if (itemData.itemClientApprStat == "APPR_STAT.REQUEST" && itemData.itemClientApprReqUserId == PG_SESSION.userId) // 거래처 품목수정 요청상태에서 요청자일 경우
						viewModel.set("apprCancelBtnVisible", true); // 승인철회 버튼 노출
					// 거래처 품목수정 관련 E

					// 상품등록 관련 S
					if (!(itemData.goodsRegistApprStat == "APPR_STAT.REQUEST" || itemData.goodsRegistApprStat == "APPR_STAT.SUB_APPROVED"))
						viewModel.set("goodsRegistApprStatName", ''); // 승인건이 아니면 상품등록 상태명을 노출하지 않는다.
					// 상품등록 관련 E

					// 거래처 상품수정 관련 S
					if (!(itemData.goodsClientApprStat == "APPR_STAT.REQUEST" || itemData.goodsClientApprStat == "APPR_STAT.SUB_APPROVED"))
						viewModel.set("goodsClientApprStatName", ''); // 승인건이 아니면 상품등록 상태명을 노출하지 않는다.
					// 거래처 상품수정 관련 E

					// 거래처인 경우, 저장버튼 및 승인관리자 지정 화면 처리 S
					if (
						!(itemData.itemClientApprStat == "APPR_STAT.REQUEST" || itemData.itemClientApprStat == "APPR_STAT.SUB_APPROVED") // 거래처 품목수정 승인 처리중이 아닐 경우
						&& !(itemData.goodsRegistApprStat == "APPR_STAT.REQUEST" || itemData.goodsRegistApprStat == "APPR_STAT.SUB_APPROVED") // 상품등록 승인중이 아닐 경우
						&& !(itemData.goodsClientApprStat == "APPR_STAT.REQUEST" || itemData.goodsClientApprStat == "APPR_STAT.SUB_APPROVED") // 거래처 상품수정 승인 처리중이 아닐 경우
					) {
						viewModel.set("saveBtnVisible", true); // 저장버튼 노출
						$('#apprChkDiv').show(); // 승인체크박스 노출
						$('#approvalCheckbox').prop("checked", true); // 승인체크박스 설정
						$("#approvalCheckbox").attr("disabled", true); // 승인체크박스 설정 수정불가처리 - 항상 승인을 요청해야됨.
						$('#apprDiv').show(); // 승인관리자 영역 노출
					}
					else {
						if (itemData.itemClientApprStat == "APPR_STAT.REQUEST" || itemData.itemClientApprStat == "APPR_STAT.SUB_APPROVED") {
							viewModel.set("approvalStatusNotification", "*거래처 품목 수정 승인 처리중입니다. 해당 상태에서는 상세 정보 수정이 불가합니다.");
							fnKendoMessage({ message : "거래처 품목 수정 승인 처리중입니다.<BR>해당 상태에서는 상세 정보 수정이 불가합니다." });
						}
						else if (itemData.goodsRegistApprStat == "APPR_STAT.REQUEST" || itemData.goodsRegistApprStat == "APPR_STAT.SUB_APPROVED") {
							viewModel.set("approvalStatusNotification", "*상품 등록 승인 처리중입니다. 해당 상태에서는 상세 정보 수정이 불가합니다.");
							fnKendoMessage({ message : "상품 등록 승인 처리중입니다.<BR>해당 상태에서는 상세 정보 수정이 불가합니다." });
						}
						else if (itemData.goodsClientApprStat == "APPR_STAT.REQUEST" || itemData.goodsClientApprStat == "APPR_STAT.SUB_APPROVED") {
							viewModel.set("approvalStatusNotification", "*거래처 상품 수정 승인 처리중입니다. 해당 상태에서는 상세 정보 수정이 불가합니다.");
							fnKendoMessage({ message : "거래처 상품 수정 승인 처리중입니다.<BR>해당 상태에서는 상세 정보 수정이 불가합니다." });
						}
					}
					// 거래처인 경우, 저장버튼 및 승인관리자 지정 화면 처리 E
				}
				else {
					viewModel.set("approvalStatusNotification", "*품목 등록 승인 처리중입니다. 해당 상태에서는 상세 정보 수정이 불가합니다.");
					fnKendoMessage({ message : "품목 등록 승인 처리중입니다.<BR>해당 상태에서는 상세 정보 수정이 불가합니다." });
				}
			}
		}

		fnItemAuthInputAllow();
	}

	function fnItemAuthInputAllow() {
		let companyType = PG_SESSION.companyType;		// 회사타입
		let clientType = PG_SESSION.clientType;			// 거래처타입

		if (checkApprovalView() != null) { // 거래처 승인리스트에서 넘어온 경우
			if(companyType == "COMPANY_TYPE.HEADQUARTERS") {
				apprKindTp = "APPR_KIND_TP.ITEM_REGIST";
			}
			else if(companyType == "COMPANY_TYPE.CLIENT") {
				apprKindTp = "APPR_KIND_TP.ITEM_CLIENT";
			}

			$("#searchForm input").attr("disabled", true);
			$("#searchForm button").attr("disabled", true);
			$("#searchForm textarea").attr("disabled", true);

			setTimeout(() => fnInitClientDisabledSetting(), 800);
		}
		else if(companyType == "COMPANY_TYPE.HEADQUARTERS") {
			apprKindTp = "APPR_KIND_TP.ITEM_REGIST";
		}
		else if(companyType == "COMPANY_TYPE.CLIENT") {
			apprKindTp = "APPR_KIND_TP.ITEM_CLIENT";
			$("#searchForm input").attr("disabled", true);
			$("#searchForm button").attr("disabled", true);

			setTimeout(() => fnInitClientDisabledSetting(), 800);
		}

		viewModel.set("apprKindTp", apprKindTp);
	}

	function fnInitClientDisabledSetting() {

		// 표준 카테고리
		$("#categoryStandardDepth1").data("kendoDropDownList").enable(false);
		$("#categoryStandardDepth2").data("kendoDropDownList").enable(false);
		$("#categoryStandardDepth3").data("kendoDropDownList").enable(false);
		$("#categoryStandardDepth4").data("kendoDropDownList").enable(false);

		$("#taxYn").data("kendoDropDownList").enable(false); // 과세 구분
		$("#extinctionYn").data("kendoDropDownList").enable(false); // 판매 허용여부(단종)

		if (checkApprovalView() != null) { // 거래처 승인리스트에서 넘어온 경우 수정 불가
			$("#bosBrand").data("kendoDropDownList").enable(false); // 표준브랜드
			$("#dpBrand").data("kendoDropDownList").enable(false); // 전시브랜드
			$("#bosStorageMethod").data("kendoDropDownList").enable(false); // 보관방법
			$("#bosOriginType").data("kendoDropDownList").enable(false); // 원산지
			$("#bosOriginDetailOverseas").data("kendoDropDownList").enable(false); // 원산지 해외
			$("#sizeUnit").data("kendoDropDownList").enable(false); // 용량(중량) 단위
			$("#packageUnit").data("kendoDropDownList").enable(false); // 포장구성 단위
			$("#specGroup").data("kendoDropDownList").enable(false); // 상품정보 제공고시 상품군
			$(".fnCheckSpecDirectInput").attr("disabled", true); // 상품정보 제공고시 직접입력
			$(".specFieldValue").attr("disabled", true); // 상품정보 제공고시 상세
			$("#nutritionDisplayYn").data("kendoDropDownList").enable(false); // 상품 영양정보 표시여부
			$("#addAvailableNutritionCode").data("kendoDropDownList").enable(false); // 신규분류 드랍다운
			$(".nutritionQuantity").attr("disabled", true); // 분류 상세 영양성분
			$(".nutritionPercent").attr("disabled", true); // 분류 상세 영양소 기준치
			$("#itemCertification").data("kendoDropDownList").enable(false); // 상품 인증정보 선택
			$("#uploadItemImage").attr("disabled", true); // 상품이미지
			$("#basicDescription").data("kendoEditor").body.contentEditable = false; // 상품 상세 기본 정보
			$("#detaillDescription").data("kendoEditor").body.contentEditable = false; //상품 상세 주요 정보
			$('.k-editor-toolbar').hide(); // kendoEditor toolbar 비활성화
			$(".poTypeDetailPopup").attr("disabled", true); // 상품이미지
			$(".fnRemoveWarehouseRow").attr("disabled", true); // 상품이미지
		}
		else {
			$(".clientAllow").attr("disabled", false);

			$("#fnAddAvailableNutritionCode").attr("disabled", false);
			$(".fnRemoveNutritionDetailRow").attr("disabled", false);
			$("#fnAddItemCertification").attr("disabled", false);
			$("#fnRemoveAllItemCertification").attr("disabled", false);
//			$("#approvalCheckbox").attr("disabled", false);
//			$('#approvalCheckbox').prop("checked",true);
//			$("#approvalCheckbox").attr("disabled", true);
//			$("#fnUpdate").attr("disabled", false);
			$("#memo").attr("disabled", true);

			if (viewModel.get('isErpItemLink') == true) { // 연동 품목일 경우 clientAllow에 대한 예외처리
				$("#unitOfMeasurement").attr("disabled", true);
				// 연동품목의 경우 거래처 사용자가 영양정보관련 노출/미노출 이외의 설정 불가 S
				$("#nutritionQuantityPerOnce").attr("disabled", true); // 1회 분량
				$("#nutritionQuantityTotal").attr("disabled", true); // 총 분량
				$("#addAvailableNutritionCode").data("kendoDropDownList").enable(false); // 신규분류 드랍다운
				$("#nutritionCodeAutoComplete").attr("disabled", true); // 영양 정보 검색
				$("#fnAddAvailableNutritionCode").attr("disabled", true); // 신규분류 추가 버튼
				$(".fnRemoveNutritionDetailRow").attr("disabled", true); // 분류 상세 삭제 버튼
				$(".nutritionQuantity").attr("disabled", true); // 분류 상세 영양성분
				$(".nutritionPercent").attr("disabled", true); // 분류 상세 영양소 기준치
				$("#nutritionEtc").attr("disabled", true); // 영양성분 기타
				// 연동품목의 경우 거래처 사용자가 영양정보관련 노출/미노출 이외의 설정 불가 E
			}
		}
	}

	// 품목승인내역에서 넘어온 화면인지 확인하는 함수.
	// return값이 null일 경우 일반 품목화면, 그렇지 않으면(승인 요청 번호) 품목승인내역에서 넘어온 화면
	function checkApprovalView() {
    	var ilItemApprId = null;
    	if (pageParam.apprKindType != undefined && pageParam.apprKindType == "APPR_KIND_TP.ITEM_CLIENT") { // 승인관리에서 페이치 호출한 경우, 승인변경 내역 정보를 얻어오기위해 승인아이디 설정
    		if (pageParam.ilItemApprId != undefined)
    			ilItemApprId = pageParam.ilItemApprId;
		}

    	return ilItemApprId;
	}

	function fnItemApprDetail(itemDate, ilItemApprId) {
		if (checkApprovalView() != null) {
			$('#apprDiv').hide();
			$('#apprChkDiv').hide();

			// 변경된 데이터 항목 red 처리 S
			if (itemDate.changedStdBrand == true) $('#labelStdBrand').addClass('approvalChanged'); // 표준브랜드 변경여부
			if (itemDate.changedDpBrand == true) $('#labelDpBrand').addClass('approvalChanged'); // 전시브랜드 변경여부
			if (itemDate.changedItemGroup == true) $('#labelItemGroup').addClass('approvalChanged'); // 상품군 변경여부
			if (itemDate.changedStorageTp == true) $('#labelStorageTp').addClass('approvalChanged'); // 보관방법 변경여부
			if (itemDate.changedOrigin == true) $('#labelOrigin').addClass('approvalChanged'); // 원산지 변경여부
			if (itemDate.changedDistributionPeriod == true) $('#labelDistributionPeriod').addClass('approvalChanged'); // 유통기간 변경여부
			if (itemDate.changedBoxVolume == true) $('#labelBoxVolume').addClass('approvalChanged'); // 박스체적 변경여부
			if (itemDate.changedPiecesPerBox == true) $('#labelPiecesPerBox').addClass('approvalChanged'); // 박스입수량/UOM 변경여부
			if (itemDate.changedSizePerPackage == true) $('#labelSizePerPackage').addClass('approvalChanged'); // 포장단위별용량 변경여부
			if (itemDate.changedSizeUnit == true) $('#labelSizeUnit').addClass('approvalChanged'); // 용량(중량)단위 변경여부
			if (itemDate.changedQtyPerPackage == true) $('#labelQtyPerPackage').addClass('approvalChanged'); // 포장구성수량 변경여부
			if (itemDate.changedPackageUnit == true) $('#labelPackageUnit').addClass('approvalChanged'); // 포장구성단위 변경여부
			if (itemDate.changedSpecMaster == true) $('#labelSpecMaster').addClass('approvalChanged'); // 상품정보 제공고시 변경여부
			if (itemDate.changedItemNutrition == true) $('#labelItemNutrition').addClass('approvalChanged'); // 상품영양정보 변경여부
			if (itemDate.changedItemNutritionDisplayYn == true) $('#labelItemNutritionDisplayYn').addClass('approvalChanged'); // 상품영양정보 영양정보 표시여부 변경여부
			if (itemDate.changedItemNutritionQuantity == true) $('#labelItemNutritionQuantity').addClass('approvalChanged'); // 상품영양정보 영양분석 단위 변경여부
			if (itemDate.changedItemNutritionEtc == true) $('#labelItemNutritionEtc').addClass('approvalChanged'); // 상품영양정보 영양성분 기타 변경여부
			if (itemDate.changedItemCertification == true) $('#labelItemCertification').addClass('approvalChanged'); // 상품인증정보 변경여부
			if (itemDate.changedItemImage == true) $('#labelItemImage').addClass('approvalChanged'); // 상품이미지 변경여부
			if (itemDate.changedVideoInfo == true) {
			    $('#labelItemDetailImage').addClass('approvalChanged'); //상품 상세 이미지 변경여부
                $('#labelVideoInfo').addClass('approvalChanged'); // 동영상정보 변경여부
            }
			if (itemDate.changedBasicDesc == true) $('#labelBasicDesc').addClass('approvalChanged'); // 상품상세 기본정보 변경여부
			if (itemDate.changedDetlDesc == true) $('#labelDetlDesc').addClass('approvalChanged'); // 상품상세 주요정보 변경여부
			// 변경된 데이터 항목 red 처리 E
		}
	}

    function fnSetRentalTotalPrice() {
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
    }

    function fnStoreStockInfoVisible(visible){
    	if(visible) {
    		$("#storeStockInfoUp").show();
    		$("#storeStockInfoDown").hide();
    		$("#storeStockInfoList").show();
    	}
    	else{
    		$("#storeStockInfoUp").hide();
    		$("#storeStockInfoDown").show();
    		$("#storeStockInfoList").hide();
    	}
    }

    // ------------------------------- Html 버튼 바인딩 Start
    // -------------------------------

    /** Common */
    $scope.fnUpdate = function() {
        fnUpdate();
    };

    $scope.fnItemUpdateHistorySearch = function() {
        fnItemUpdateHistorySearch();
    }

    $scope.fnShowItemPriceAddRow = function(newPriceRowId) { // 가격 정보 신규 입력 항목 Visible 처리
        fnShowItemPriceAddRow(newPriceRowId);
    }

    $scope.fnItemPriceHistoryPopup = function(popupObj) { // 가격 정보 전체 이력보기 팝업
        fnItemPriceHistoryPopup(popupObj);
    }

    $scope.fnItemDiscountPriceHistoryPopup = function(popupObj) { // 가격 Item Discount 이력보기 팝업
    	fnItemDiscountPriceHistoryPopup(popupObj);
    }

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
        setTimeout(() => fnDecimalValidationByNumberOfDigits(event, limitCountUpperDecimalPoint, limitCountUnderDecimalPoint), 0);
    }
    $scope.fnBosPoTypeChange = function(obj) {
    	fnBosPoTypeChange(obj);
    }

    $scope.fnItemPriceAddPopup = function(obj) {
        // 품목 가격 승인, 상품 할인 승인 중복 체크
        fnAjax({
            url     : "/admin/approval/auth/checkDuplicatePriceApproval",
            method : "GET",
            async : false,
            params  : {"taskCode" : "APPR_KIND_TP.ITEM_PRICE", "itemCode" : viewModel.get("isErpItemLink") == true ? viewModel.get("erpItemCode") : viewModel.get('bosItemCode')},
            success : function(data){
                 fnItemPriceAddPopup(obj);
            },
            isAction : "select"
        });
    }

    $scope.fnApprAdmin = function(){fnApprAdmin();};				//승인관리자 지증
	$scope.fnApprClear = function(){fnApprClear();};				//승인관리자 초기화
	$scope.fnApprCancel = function(){fnApprCancel();};				//요청철회
	$scope.fnItemRegistApprDetailPopup = function() {fnItemRegistApprDetailPopup();};	//품목등록 승인내역 상세보기 팝업
	$scope.fnItemClientApprDetailPopup = function() {fnItemClientApprDetailPopup();};	//거래처 품목 수정 승인내역 상세보기 팝업
	$scope.fnGoodsRegistApprDetailPopup = function() {fnGoodsRegistApprDetailPopup();};	//상품등록 승인내역 상세보기 팝업
	$scope.fnGoodsClientApprDetailPopup = function() {fnGoodsClientApprDetailPopup();};	//거래처 상품 수정 승인내역 상세보기 팝업

    $scope.fnStoreScheduleModal = function(urStoreId, storeName) {
    	fnStoreScheduleModal(urStoreId, storeName);
    }

    $scope.fnItemStorePriceLogModal = function(urStoreId, storeName){
    	fnItemStorePriceLogModal(urStoreId, storeName);
	}

    $scope.fnStoreStockInfoVisible = function (visible) {
    	fnStoreStockInfoVisible(visible);
    }

    $scope.fnStoreScheduleModal = function(urStoreId, storeName) {
    	fnStoreScheduleModal(urStoreId, storeName);
    }

    // ------------------------------- Html 버튼 바인딩 End
    // -------------------------------

}); // document ready - END
