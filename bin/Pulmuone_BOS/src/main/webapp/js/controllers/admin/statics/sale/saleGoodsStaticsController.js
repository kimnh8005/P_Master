/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 통계관리 상품별 매출통계
 * @
 * @
 * @ 수정일        수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.03.19    dgyoun        최초생성
 * @
 ******************************************************************************/
'use strict';


//var LAST_PAGE = null;   // 최종 페이지 (페이징 기억 관련)
//var PAGE_SIZE = 20;
var gbPageParam = '';   // 넘어온 페이지파라미터
var publicStorageUrl = fnGetPublicStorageUrl();
var gridDs, gridOpt, grid;

var gbSelectConditionType = 'singleSection';  // 단일/복수 (기본값:단일)
var gbCtgryTp = 'S';                          // 카테고리유형 (기본값:표준)

$(document).ready(function() {

  // ==========================================================================
  // # Initialize Page Call
  // ==========================================================================
  fnInitialize();

  // ==========================================================================
  // # Initialize PageR
  // ==========================================================================
  function fnInitialize() {

    // ------------------------------------------------------------------------
    // 화면기본설정
    // ------------------------------------------------------------------------
    $scope.$emit('fnIsMenu', { flag : 'true' });

    fnPageInfo({
        PG_ID     : 'saleGoodsStatics'
      , callback  : fnUI
    });

    // ------------------------------------------------------------------------
    // 페이지파라미터
    // ------------------------------------------------------------------------
    gbPageParam = fnGetPageParam();
  }

  // ==========================================================================
  // # fnUI
  // ==========================================================================
  function fnUI() {

    fnInitButton();           // Initialize Button ----------------------------

    fnInitOptionBox();        // Initialize Option Box ------------------------

    fnInitGrid();             // Initialize Grid ------------------------------

    fnInitEvent();            // Initialize Event -----------------------------

    fnDefaultSet();           // 기본설정 -------------------------------------

    //fnSearch();               // 조회 -----------------------------------------

  }

  // ==========================================================================
  // # 초기화 - 버튼
  // ==========================================================================
  function fnInitButton() {
    //$('#fnSearch, #fnConfirm, #fnClear, #fnClose, #fnExcelDownload, #fnShowImage').kendoButton();
  }

  // ==========================================================================
  // # 초기화 - 값
  // ==========================================================================
  function fnClear() {
    //console.log('# fnClear Start');
    // ------------------------------------------------------------------------
    // 조회조건 초기화
    // ------------------------------------------------------------------------
    $('#searchForm').formClear(true);

    // 단일/복수 Set (초기화 없이 선택되어있는 값 유지)
    $('input:radio[name="selectConditionType"]:radio[value="'+gbSelectConditionType+'"]').prop('checked',true);

    // 상품코드별합산 : 체크
    $('INPUT[name=goodsSumYn]').prop('checked', true);
    // 출고처그룹 초기화에 따른 촐고처 전체 조회
    fnWareHouseGroupChange(null);
    // 판매처그룹 초기화에 따른 판매처 전체 조회
    fnSellerGroupChange(null);
    // 브랜드유형 초기화(미선택)에 따른 표준브랜드/전시브랜드 숨김
    $('#urBrandIdDiv').hide();
    $('#dpBrandIdDiv').hide();
    // 판매채널유형 : 전체선택
    $("input[name=agentTypeCd]:checkbox").prop("checked", true);
    // 회원유형 : 전체선택
    $("input[name=buyerTypeCd]:checkbox").prop("checked", true);
    //카테고리유형 초기화(표준카테고리)에 따른 표준카테고리군노출/전시카테고리군숨김
    $('#ctgryStandardSpan').show();
    $('#ctgrySpan').hide();
    // 상품유형 : 전체선택
    $("input[name=goodsTpCd]:checkbox").prop("checked", true);

    // ------------------------------------------------------------------------
    // 그리드 초기화
    // ------------------------------------------------------------------------
    let ctgryTp = $('ctgryTp').val();
    fnInitGrid(ctgryTp);
    // 그리드 총건수
    $('#totalCnt').text(0);
    // 그리드 합산정보
    $('#totalInfo').text('');
  }

  // ==========================================================================
  // # 기본 설정
  // ==========================================================================
  function fnDefaultSet() {
    // ------------------------------------------------------------------------
    // 단일조건검색 영역 노출
    // ------------------------------------------------------------------------
    $('.singleTr').show();
  };

  // ==========================================================================
  // # 초기화 - 옵션박스
  // ==========================================================================
  function fnInitOptionBox() {

    // ------------------------------------------------------------------------
    // 팝업
    // ------------------------------------------------------------------------
    //$('#kendoPopup').kendoWindow({
    //    visible: false
    //  , modal: true
    //});

    // ------------------------------------------------------------------------
    // 단일/복수 [라디오] : 단일조건 검색/복수조건 검색
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id      : 'selectConditionType'
      , tagId   : 'selectConditionType'
      , tab     : true
      , data    : [ {
                      CODE            : 'singleSection'
                    , NAME            : '단일조건 검색'
                    }
                  , { CODE            : 'multiSection'
                    , NAME            : '복수조건 검색'
                    }
                  ]
      , chkVal  : 'singleSection'
      , change  : function (e) {
                    gbSelectConditionType = $('input[name=selectConditionType]:checked').val();
                  }
      , style   : {}
    });

    // ------------------------------------------------------------------------
    // 검색기준유형 [체크] : 주문일/결제일/매출일
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id      : 'searchTp'
      , tagId   : 'searchTp'
      , tab     : true
      , data    : [ {CODE : 'ODR' , NAME  : '주문일'}
                  , {CODE : 'PAY' , NAME  : '결제일'}
                  , {CODE : 'SAL' , NAME  : '매출일'}
                  ]
      , chkVal  : 'ODR'
      , change  : function (e) {}
      , style   : {}
    });

    // ------------------------------------------------------------------------
    // 시작일/종료일 [날짜]
    // ------------------------------------------------------------------------
    fnKendoDatePicker({
        id          : 'startDe'
      , format      : 'yyyy-MM-dd'
      , btnStartId  : 'startDe'
      , btnEndId    : 'endDe'
      , change      : fnOnChangeStartDt
    });
    fnKendoDatePicker({
        id          : 'endDe'
      , format      : 'yyyy-MM-dd'
      , btnStyle    : false     //버튼 숨김
      , btnStartId  : 'startDe'
      , btnEndId    : 'endDe'
      , change      : fnOnChangeEndDt
    });

    // ------------------------------------------------------------------------
    // 시작일/종료일 [시간]
    // ------------------------------------------------------------------------
    var hourList = new Array() ;

    for(var i = 0; i<=23; i++){
      // 객체 생성
      var data = new Object() ;

      data.CODE = (i < 10) ? "0" + i : i.toString();
      data.NAME = (i < 10) ? "0" + i : i.toString();

      // 리스트에 생성된 객체 삽입
      hourList.push(data) ;
    }


    // 시작 시각 검색
    fnKendoDropDownList({
      id  : 'searchStHour',
      data  : hourList,
      valueField 	: "CODE",
      textField 	: "NAME"
    });

    // 종료 시각 검색
    fnKendoDropDownList({
      id  : 'searchEdHour',
      data  : hourList,
      valueField 	: "CODE",
      textField 	: "NAME",
      value : "23"
    });

    function fnOnChangeStartDt(e) {
      fnOnChangeDatePicker(e, 'start', 'startDe', 'endDe');
    }
    function fnOnChangeEndDt(e) {
      fnOnChangeDatePicker(e, 'end', 'startDe', 'endDe');
    }

    // ------------------------------------------------------------------------
    // 코드검색유형 [콤보] : 상품코드/품목코드/품목바코드
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : 'goodsSearchTp'
      , tagId       : 'goodsSearchTp'
      , data        : [
                        { 'CODE' : 'GOODS_CODE'   , 'NAME' : '상품코드' }
                      , { 'CODE' : 'ITEM_CODE'    , 'NAME' : '품목코드' }
                      , { 'CODE' : 'ITEM_BARCODE' , 'NAME' : '품목바코드' }
                      ]
      , valueField  : 'CODE'
      , textField   : 'NAME'
    });

    // ------------------------------------------------------------------------
    // 상품코드합산여부 [체크]
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id        : 'goodsSumYn'
      , tagId     : 'goodsSumYn'
      , data      : [
                      { 'CODE' : 'Y'    , 'NAME' : '상품코드별 합산'    }
                    ]
      , chkVal    : 'Y'
      , style     : {}
    });

    // ------------------------------------------------------------------------
    // 푸드머스 보기 [체크]
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id        : 'foodmusYn'
      , tagId     : 'foodmusYn'
      , data      : [
                      { 'CODE' : 'Y'    , 'NAME' : '푸드머스 보기'    }
                    ]
      , chkVal    : ''
      , style     : {}
    });

    // ------------------------------------------------------------------------
    // 푸드머스 보기 [체크]
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id        : 'urFoodmusYn'
      , tagId     : 'urFoodmusYn'
      , data      : [
                      { 'CODE' : 'Y'    , 'NAME' : '푸드머스 보기'    }
                    ]
      , chkVal    : ''
      , style     : {}
    });

    // ------------------------------------------------------------------------
    // 푸드머스 보기 [체크]
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id        : 'dpFoodmusYn'
      , tagId     : 'dpFoodmusYn'
      , data      : [
                      { 'CODE' : 'Y'    , 'NAME' : '푸드머스 보기'    }
                    ]
      , chkVal    : ''
      , style     : {}
    });

    // ------------------------------------------------------------------------
    // 공급업체 [콤보]
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : 'urSupplierId'
      , tagId       : 'urSupplierId'
      , url         : '/admin/comn/getDropDownSupplierList'
      //, params      : {'stCommonCodeMasterCode' : '', 'useYn' : 'Y'}
      , async       : false
      , isDupUrl    : 'Y'
      , textField   : 'supplierName'
      , valueField  : 'supplierId'
      , style       : {}
      , blank       : '공급업체 전체'
    });

    // ------------------------------------------------------------------------
    // 출고처그룹 [콤보]
    // ------------------------------------------------------------------------
    // 출고처그룹
    fnKendoDropDownList({
        id      : 'urWarehouseGrpCd'
      , tagId   : 'urWarehouseGrpCd'
      , url     : '/admin/comn/getCodeList'
      , params  : { 'stCommonCodeMasterCode' : 'WAREHOUSE_GROUP', 'useYn' : 'Y' }
      , async   : false
      , isDupUrl: 'Y'
      , blank   : '출고처 그룹 전체'
    });

    // ------------------------------------------------------------------------
    // 출고처ID [콤보]
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : 'urWarehouseId'
      , tagId       : 'urWarehouseId'
      , url         : '/admin/comn/getDropDownWarehouseGroupByWarehouseList'
      , params      : { 'warehouseGroupCode' : '' }
      , async       : false
      , isDupUrl    : 'Y'
      , textField   : 'warehouseName'
      , valueField  : 'warehouseId'
      , blank       : '출고처 선택'
    });

    // ------------------------------------------------------------------------
    // 판매처그룹 [콤보]
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id        : 'sellersGroupCd'
      , tagId     : 'sellersGroupCd'
      , url       : '/admin/comn/getCodeList'
      , params    : { 'stCommonCodeMasterCode' : 'SELLERS_GROUP', 'useYn' : 'Y' }
      , blank     : '판매처 그룹 전체'
      , async     : false
      , isDupUrl  : 'Y'
    });

    // ------------------------------------------------------------------------
    // 판매처ID [콤보]
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : 'omSellersId'
      , tagId       : 'omSellersId'
      , url         : '/admin/comn/getDropDownSellersGroupBySellersList'
      , params      : { 'sellersGroupCd' : '' }
      , textField   : 'sellersNm'
      , valueField  : 'omSellersId'
      , blank       : '판매처 전체'
    });

    // ------------------------------------------------------------------------
    // 브랜드유형 [콤보]
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id      : 'brandTp'
      , tagId   : 'brandTp'
      , url     : '/admin/comn/getCodeList'
      , params  : { 'stCommonCodeMasterCode' : 'GIFT_TARGET_BRAND_TP', 'useYn' : 'Y' }
      , async   : false
      , isDupUrl: 'Y'
      , blank   : '브랜드 유형'
    });

    // ------------------------------------------------------------------------
    // *표준브랜드ID [콤보]
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : 'urBrandId'
      , tagId       : 'urBrandId'
      , url         : '/admin/ur/brand/searchBrandList'
      , params      : {'useYn' :'Y'}
      , autoBind    : true
      , valueField  : 'urBrandId'
      , textField   : 'brandName'
      //, chkVal      : brandId
      , style       : {}
      , blank       : '표준브랜드 선택'
    });

    // ------------------------------------------------------------------------
    // *전시브랜드ID [콤보]
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : 'dpBrandId'
      , tagId       : 'dpBrandId'
      , url         : '/admin/ur/brand/searchDisplayBrandList'
      , params      : {'useYn' :'Y'}
      , autoBind    : true
      , valueField  : 'dpBrandId'
      , textField   : 'dpBrandName'
      , async       : true
      , isDupUrl    : 'Y'
      //, chkVal      :brandId
      , style       : {}
      , blank       : '전시브랜드 선택'
    });

    // ------------------------------------------------------------------------
    // 판매채널유형 [체크] : 전체/PC/MOBILE/APP/관리자주문/외부몰
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id          : 'agentTypeCd'
      , tagId       : 'agentTypeCd'
      , url         : '/admin/comn/getCodeList'
      , params      : {'stCommonCodeMasterCode' : 'AGENT_TYPE', 'useYn' : 'Y'}
      , async       : false
      , isDupUrl    : 'Y'
      , style       : {}
      , beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
      , success     : function() {
                        // 전체선택
                        $("input[name=agentTypeCd]:checkbox").prop("checked", true);
                      }
    });

    // ------------------------------------------------------------------------
    // 회원유형 [콤보]
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id          : 'buyerTypeCd'
      , tagId       : 'buyerTypeCd'
      , url         : '/admin/comn/getCodeList'
      , params      : {'stCommonCodeMasterCode' : 'BUYER_TYPE', 'useYn' : 'Y'}
      , async       : false
      , isDupUrl    : 'Y'
      , style       : {}
      , beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
      , success     : function() {
                        // 전체선택
                        $("input[name=buyerTypeCd]:checkbox").prop("checked", true);
                      }
    });


    // ------------------------------------------------------------------------
    // 보관온도 [콤보]
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id      : 'storageMethodTp'
      , tagId   : 'storageMethodTp'
      , url     : '/admin/comn/getCodeList'
      , params  : { 'stCommonCodeMasterCode' : 'ERP_STORAGE_TYPE', 'useYn' : 'Y' }
      , async   : false
      , isDupUrl: 'Y'
      , blank   : '보관온도 전체'
    });

    // ------------------------------------------------------------------------
    // 카테고리유형 [콤보] : 표준카테고리/전시카테고리
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id      : 'ctgryTp'
      , tagId   : 'ctgryTp'
      , data    : [
                    {'CODE':'S' , 'NAME':'표준카테고리'}
                  , {'CODE':'D' , 'NAME':'전시카테고리'}
                  , {'CODE':'E' , 'NAME':'ERP 카테고리'}
                  ]
      , chkVal  : 'S'
    });

    // ------------------------------------------------------------------------
    // 표준카테고리 [콤보] : 대분류/중분류/소분류/세분류
    // ------------------------------------------------------------------------
    // 표준카테고리 대분류
    fnKendoDropDownList({
        id          : 'categoryStandardDepth1'
      , tagId       : 'categoryStandardDepth1'
      , url         : '/admin/comn/getDropDownCategoryStandardList'
      , params      : { 'depth' : '1' }
      , textField   : 'categoryName'
      , valueField  : 'categoryId'
      , blank       : '대분류'
      , async       : false
    });
    // 표준카테고리 중분류
    fnKendoDropDownList({
        id          : 'categoryStandardDepth2'
      , tagId       : 'categoryStandardDepth2'
      , url         : '/admin/comn/getDropDownCategoryStandardList'
      , textField   : 'categoryName'
      , valueField  : 'categoryId'
      , blank       : '중분류'
      , async       : false
      , cscdId      : 'categoryStandardDepth1'
      , cscdField   : 'categoryId'
    });
    // 표준카테고리 소분류
    fnKendoDropDownList({
        id          : 'categoryStandardDepth3'
      , tagId       : 'categoryStandardDepth3'
      , url         : '/admin/comn/getDropDownCategoryStandardList'
      , textField   : 'categoryName'
      , valueField  : 'categoryId'
      , blank       : '소분류'
      , async       : false
      , cscdId      : 'categoryStandardDepth2'
      , cscdField   : 'categoryId'
    });
    // 표준카테고리 세분류
    fnKendoDropDownList({
        id          : 'categoryStandardDepth4'
      , tagId       : 'categoryStandardDepth4'
      , url         : '/admin/comn/getDropDownCategoryStandardList'
      , textField   : 'categoryName'
      , valueField  : 'categoryId'
      , blank       : '세분류'
      , async       : false
      , cscdId      : 'categoryStandardDepth3'
      , cscdField   : 'categoryId'
    });
    // ------------------------------------------------------------------------
    // 전시카테고리 [콤보] : 대분류/중분류/소분류/세분류
    // ------------------------------------------------------------------------
    // 전시카테고리 대분류
    fnKendoDropDownList({
        id          : 'categoryDepth1'
      , tagId       : 'categoryDepth1'
      , url         : '/admin/comn/getDropDownCategoryList'
      , params      : { 'depth' : '1', 'mallDiv' : 'MALL_DIV.PULMUONE' }
      , textField   : 'categoryName'
      , valueField  : 'categoryId'
      , blank       : '대분류'
      , async       : false
    });
    // 전시카테고리 중분류
    fnKendoDropDownList({
        id          : 'categoryDepth2'
      , tagId       : 'categoryDepth2'
      , url         : '/admin/comn/getDropDownCategoryList'
      , textField   : 'categoryName'
      , valueField  : 'categoryId'
      , blank       : '중분류'
      , async       : false
      , cscdId      : 'categoryDepth1'
      , cscdField   : 'categoryId'
    });
    // 전시카테고리 소분류
    fnKendoDropDownList({
        id          : 'categoryDepth3'
      , tagId       : 'categoryDepth3'
      , url         : '/admin/comn/getDropDownCategoryList'
      , textField   : 'categoryName'
      , valueField  : 'categoryId'
      , blank       : '소분류'
      , async       : false
      , cscdId      : 'categoryDepth2'
      , cscdField   : 'categoryId'
    });
    // 전시카테고리 세분류
    fnKendoDropDownList({
        id          : 'categoryDepth4'
      , tagId       : 'categoryDepth4'
      , url         : '/admin/comn/getDropDownCategoryList'
      , textField   : 'categoryName'
      , valueField  : 'categoryId'
      , blank       : '세분류'
      , async       : false
      , cscdId      : 'categoryDepth3'
      , cscdField   : 'categoryId'
    });

    // ------------------------------------------------------------------------
    // 상품유형 [체크] : 전체/일반/폐기임박/증정/추가/묶음/일일/매장/렌탈/무형
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id          : 'goodsTpCd'
      , tagId       : 'goodsTpCd'
      , url         : '/admin/comn/getCodeList'
      , params      : {'stCommonCodeMasterCode' : 'GOODS_TYPE', 'useYn' : 'Y'}
      , async       : false
      , isDupUrl    : 'Y'
      , style       : {}
      , beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
      , success     : function() {
                        // 전체선택
                        $("input[name=goodsTpCd]:checkbox").prop("checked", true);
                      }
    });

  }


	$("#urFoodmusYn").click(function(){
		if($("#urFoodmusYn_0").prop("checked") == true){
			$("#urBrandId").data("kendoDropDownList").enable(false);
		}else{
			$("#urBrandId").data("kendoDropDownList").enable(true);
		}
	});

    $("#dpFoodmusYn").click(function(){
		if($("#dpFoodmusYn_0").prop("checked") == true){
			$("#dpBrandId").data("kendoDropDownList").enable(false);
		}else{
			$("#dpBrandId").data("kendoDropDownList").enable(true);
		}
	});
  // ==========================================================================
  // # 초기화 - 이벤트
  // ==========================================================================
  function fnInitEvent() {

    // ------------------------------------------------------------------------
    // 체크박스변경(전체선택관련)
    // ------------------------------------------------------------------------
    fbCheckboxChange();

    // ------------------------------------------------------------------------
    // 단일조건/복수조건 클릭 이벤트
    // ------------------------------------------------------------------------
    $('#selectConditionType').on('click', function (e) {
      //console.log('# 단일/복수 클릭');
      let selectConditionType = $('input[name=selectConditionType]:checked').val();

      if (selectConditionType == 'singleSection') {
        // 단일조건
        $('.singleTr').show();
        $('.multiTr').hide();
        // 카테고리유형 : 표준
        gbCtgryTp = 'S';
      }
      else {
        // 복수조건  multiSection
        $('.singleTr').hide();
        $('.multiTr').show();
        // 카테고리유형 : 선택된 값
        gbCtgryTp = $('#ctgryTp').val();
      }
      // 검색조건내용 초기화
      $('#searchInfo').val('');

    });

    // ------------------------------------------------------------------------
    // 출고처그룹 선택 이벤트
    // ------------------------------------------------------------------------
    $('#urWarehouseGrpCd').on('change', function (e) {
      //console.log('# 출고처 그룹 변경');
      fnWareHouseGroupChange(e);
    });

    // ------------------------------------------------------------------------
    // 판매처그룹 선택 이벤트 : bind 확인
    // ------------------------------------------------------------------------
    $('#sellersGroupCd').on('change', function (e) {
      //console.log('# 판매처 그룹 변경');
      fnSellerGroupChange(e);
    });

    // ------------------------------------------------------------------------
    // 브랜드유형 선택 이벤트
    // ------------------------------------------------------------------------
    $('#brandTp').on('change', function (e) {
      //console.log('# 브랜드유형 변경 :: ', $('#brandTp').val());
      let brandTp = $('#brandTp').val();
      if (brandTp == 'GIFT_TARGET_BRAND_TP.STANDARD') {
        $('#urBrandIdDiv').show();
        $('#dpBrandIdDiv').hide();
        $('#foodmusYnDiv').hide();
        $("input[name=foodmusYn]:checkbox").prop("checked", false);
        $("input[name=dpFoodmusYn]:checkbox").prop("checked", false);
        $("#dpBrandId").data("kendoDropDownList").enable(true);
      }
      else if (brandTp == 'GIFT_TARGET_BRAND_TP.DISPLAY') {
        $('#urBrandIdDiv').hide();
        $('#dpBrandIdDiv').show();
        $('#foodmusYnDiv').hide();
        $("input[name=foodmusYn]:checkbox").prop("checked", false);
        $("input[name=urFoodmusYn]:checkbox").prop("checked", false);
        $("#urBrandId").data("kendoDropDownList").enable(true);
      }
      else {
        $('#urBrandIdDiv').hide();
        $('#dpBrandIdDiv').hide();
        $('#foodmusYnDiv').show();
        $("input[name=dpFoodmusYn]:checkbox").prop("checked", false);
        $("input[name=urFoodmusYn]:checkbox").prop("checked", false);
        $("#urBrandId").data("kendoDropDownList").enable(true);
        $("#dpBrandId").data("kendoDropDownList").enable(true);
      }
    });

    // ------------------------------------------------------------------------
    // 카테고리유형 선택 이벤트
    // ------------------------------------------------------------------------
    $('#ctgryTp').on('change', function (e) {
      //console.log('카테고리유형 변경 :: ', $('#ctgryTp').val());
      let ctgryTp = $('#ctgryTp').val();
      gbCtgryTp = ctgryTp;

      if (ctgryTp == 'S') {
        $('#ctgryStandardSpan').show();
        $('#ctgrySpan').hide();
      }
      else if (ctgryTp == 'D') {
        $('#ctgryStandardSpan').hide();
        $('#ctgrySpan').show();
      }
      else if (ctgryTp == 'E') {
         $('#ctgryStandardSpan').hide();
         $('#ctgrySpan').hide();
       }
    });

  }


  // ==========================================================================
  // # 조회
  // ==========================================================================
  function fnSearch() {
    // ------------------------------------------------------------------------
    // Validation
    // ------------------------------------------------------------------------
    let selectConditionType = $('input[name=selectConditionType]:checked').val();

    let startDe         = $('#startDe').val();
    let endDe           = $('#endDe').val();

    if (fnIsEmpty(startDe)) {
      fnMessage('', '<font color="#FF1A1A">[기준기간 시작일자]</font> 필수 입력입니다.', 'startDe');
      return;
    }
    if (fnIsEmpty(endDe)) {
       fnMessage('', '<font color="#FF1A1A">[기준기간 종료일자]</font> 필수 입력입니다.', 'endDe');
       return;
    }

    if (selectConditionType == 'singleSection') {
      let keyword = $('#keyword').val();

      if (keyword == undefined || keyword == null || keyword == '') {
        fnMessage('', '<font color="#FF1A1A">[검색코드]</font> 필수 입력입니다.', 'keyword');
        return false;
      }
    }

    // ------------------------------------------------------------------------
    // 그리드초기화
    // ------------------------------------------------------------------------
    fnInitGrid(gbCtgryTp);

    // ------------------------------------------------------------------------
    // 리스트 조회
    // ------------------------------------------------------------------------
    var data;
    data = $('#searchForm').formSerialize(true);

    // ----------------------------------------------------------------------
    // 최종페이지 정보 Set (페이징 기억 관련)
    // ----------------------------------------------------------------------
    var query = {
      //page          : curPage
      //, pageSize      : PAGE_SIZE
        filterLength  : fnSearchData(data).length
      , filter        : {
                          filters : fnSearchData(data)
                        }
    };
    gridDs.query(query);
  }

  // ==========================================================================
  // # Tab 초기화
  //    - fbCommonController.js > const fbTabChange = function fbTabChange(_selected) 참조
  // ==========================================================================
  //function fnInitTabChange() {
  //  $('#ng-view').on("change", ".js__tab", function () {
  //    const $this = $(this);
  //    const _name = $this.attr("name");
  //    //js__tabs__wrapper안의 모든 탭 콘텐츠
  //    const $allTabContents = $("." + _name);
  //    //해당 탭의 콘텐츠
  //    let $tabContent = $("." + $this.data("tab-content"));
  //    console.log('# $tabContent :: ', $tabContent);
  //    //모든 탭 콘텐츠 비노출
  //    $allTabContents.hide();
  //    //해당 탭 콘텐츠 노출
  //    $tabContent.show();
  //  });
  //}

  // ==========================================================================
  // # 출고처그룹 변경 이벤트 처리
  // ==========================================================================
  function fnWareHouseGroupChange(e) {
    fnAjax({
        method    : 'GET'
      , url       : '/admin/comn/getDropDownWarehouseGroupByWarehouseList'
      , params    : { 'warehouseGroupCode' : $('#urWarehouseGrpCd').val() }
      , success   : function(data) {
                      let urWarehouseId = $('#urWarehouseId').data('kendoDropDownList');
                      urWarehouseId.setDataSource(data.rows);
                    }
      , error     : function(xhr, status, strError){
                      fnKendoMessage({ message : xhr.responseText });
                    }
      , isAction  : "select"
      });
  };

  // ==========================================================================
  // # 판매처그룹 변경 이벤트 처리
  // ==========================================================================
  function fnSellerGroupChange() {
    //console.log('# fnSellerGroupChange Start');
    let sellersGroupCd = $('#sellersGroupCd').val();

    fnAjax({
        method    : 'GET'
      , url       : '/admin/comn/getDropDownSellersGroupBySellersList'
      , params    : { 'sellersGroupCd' : sellersGroupCd }
      , success   : function( data ){
                      let sellerDetail = $('#omSellersId').data('kendoDropDownList');
                      sellerDetail.setDataSource(data.rows);
                    }
      , error     : function(xhr, status, strError){
                      fnKendoMessage({ message : xhr.responseText });
                    }
      , isAction  : 'select'
      });
  };


  // ------------------------------- Grid Start -------------------------------
  // ==========================================================================
  // # 그리드
  // ==========================================================================
  function fnInitGrid(ctgryTp){
    //console.log('# fnInitGrid Start :: [', ctgryTp, ']');
    // ------------------------------------------------------------------------
    // 그리드 호출
    // ------------------------------------------------------------------------
    gridDs = fnGetPagingDataSource({
        url      : '/admin/statics/sale/selectSaleGoodsStaticsList'
      //, pageSize : PAGE_SIZE
    });

    gridOpt  = {
        dataSource  : gridDs
      , noRecordMsg : '검색된 목록이 없습니다.'
      //, pageable    : {
      //                  pageSizes   : [20, 30, 50]
      //                , buttonCount : 10
      //                }
      , navigatable : true
      , scrollable  : false
      , height      : 'auto' // '120'
      , selectable  : true
      , editable    : false
      , resizable   : true
      , autobind    : false
      , columns     : [
                        {                           title: '순번'                       , width: 50 , attributes:{ style:'text-align:center' }
                                                                                        , template: '<span class="row-number"></span>'
                        }
                      , { field:'supplierNm'      , title: '공급업체'                   , width:120 , attributes:{ style:'text-align:center' }}
                      , { field:'sellersGroupCdNm', title: '판매처그룹'                 , width: 60 , attributes:{ style:'text-align:center' }}
                      , { field:'sellersNm'       , title: '판매처'                     , width: 80 , attributes:{ style:'text-align:center' }}
                      , { field:'buyerTypeNm'     , title: '회원유형'                  , width: 60 , attributes:{ style:'text-align:center' }}
                      , { field:'ctgryNm'         , title: '카테고리'                   , width:200 , attributes:{ style:'text-align:left'   }
                                                                                        , template  : function (dataItem) {
                                                                                        
                                                                                                        if (ctgryTp == 'S') {
                                                                                                          // 표준카테고리
                                                                                                          return dataItem.ctgryStdNm;
                                                                                                        }
                                                                                                        else {
                                                                                                          // 전시카테고리
                                                                                                          return dataItem.ctgryNm;
                                                                                                        }
                                                                                                      }
                        }
                      , { field:'goodsNm'         , title: '상품명'                     , width:200 , attributes:{ style:'text-align:left'   }}
                      , { field:'taxTypeNm'       , title: '과세구분'                  , width: 60 , attributes:{ style:'text-align:center' }}
                      , { field:'recommendedPrice', title: '정상가'                     , width: 70 , attributes:{ style:'text-align:right'  }, format: '{0:\#\#,\#}'}
                      , { field:'salePrice'       , title: '판매가'                     , width: 70 , attributes:{ style:'text-align:right'  }, format: '{0:\#\#,\#}'}
                      , { field:'goodsCnt'        , title: '주문상품<BR/>수량'          , width: 50 , attributes:{ style:'text-align:right'  }}
                      , { field:'taxPaidPrice'    , title: '총 매출금액<br/>(VAT포함)'  , width: 90 , attributes:{ style:'text-align:right'  }, format: '{0:\#\#,\#}'}
                      , { field:'nonTaxPaidPrice' , title: '총 매출금액<br/>(VAT별도)'  , width: 90 , attributes:{ style:'text-align:right'  }, format: '{0:\#\#,\#}'}
                      , { field:'ilGoodsId'       , title: '상품코드'                   , width: 80 , attributes:{ style:'text-align:center' }}
                      , { field:'ilItemCd'        , title: '품목코드'                   , width: 80 , attributes:{ style:'text-align:center' }}
                      , { field:'itemBarcode'     , title: '품목바코드'                 , width:100 , attributes:{ style:'text-align:center' }}
                      ]
    };

    grid = $('#grid').initializeKendoGrid( gridOpt ).cKendoGrid();

    // ------------------------------------------------------------------------
    // dataBound
    // ------------------------------------------------------------------------
    grid.bind('dataBound', function(e) {
      // ----------------------------------------------------------------------
      // NO 항목 및 전체건수 Set
      // ----------------------------------------------------------------------
      //console.log('# gridDs :: ', JSON.stringify(gridDs));
      //var row_num = gridDs._total - ((gridDs._page - 1) * gridDs._pageSize);
      let row_num = 1;    // 순번
      $('#grid tbody > tr .row-number').each(function(index){
        $(this).html(row_num);
        row_num++;
      });

      // ----------------------------------------------------------------------
      // 총판매금액(VAT포함) 합계금액
      // ----------------------------------------------------------------------
      let totalTaxPaidPrice     = 0;
      let totalNonTaxPaidPrice  = 0;
      var targetGrid    = $('#grid').data('kendoGrid');
      var targetGridDs  = targetGrid.dataSource;
      var targetGridArr = targetGridDs.data();

      for (var i = 0; i < targetGridArr.length; i++) {
        totalTaxPaidPrice     += targetGridArr[i].taxPaidPrice;
        totalNonTaxPaidPrice  += targetGridArr[i].nonTaxPaidPrice;
      }
      //console.log('# totalTaxPaidPrice    :: ', totalTaxPaidPrice);
      //console.log('# totalNonTaxPaidPrice :: ', totalNonTaxPaidPrice);
      let totalTaxPaidPriceStr = String(totalTaxPaidPrice);
      let totalTaxPaidPriceStrComma = totalTaxPaidPriceStr.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
      let totalNonTaxPaidPriceStr = String(totalNonTaxPaidPrice);
      let totalNonTaxPaidPriceStrComma = totalNonTaxPaidPriceStr.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');


      let totalInfoStr = ' | ' + '판매금액 합산 : ' + totalTaxPaidPriceStrComma + '원 (VAT 포함).  '
                                                    + totalNonTaxPaidPriceStrComma + '원 (VAT 별도)';
      $('#totalInfo').text(totalInfoStr);

      // ----------------------------------------------------------------------
      // 전체건수 Set
      // ----------------------------------------------------------------------
      $('#totalCnt').text(gridDs._total);

      // ----------------------------------------------------------------------
      // 엑셀다운로드 버튼 노출
      // ----------------------------------------------------------------------
      //if (gridDs._total > 0) {
      //  console.log('# gridDs._total :: ', gridDs._total);
      //  $('#btnExcelDown').show();
      //}
    });

  }
  // ------------------------------- Grid End ---------------------------------

  // ==========================================================================
  // # 콜백함수
  // ==========================================================================
  function fnBizCallback( id, data ){

    switch(id){
      case 'select' :
        // --------------------------------------------------------------------
        // 조회
        // --------------------------------------------------------------------

        break;
    }
  }


  // ==========================================================================
  // # 오류메시지처리
  // ==========================================================================
  function fnMessage(key, nullMsg, ID) {
    fnKendoMessage({
        message : fnGetLangData({ key : key, nullMsg : nullMsg})
      , ok      : function() {
                    if (ID != null && ID != '') {
                      $('#'+ID).focus();
                    }
                  }
    });
  }

  // ==========================================================================
  // # 조회조건 정보 문자열 생성
  // ==========================================================================
  function fnGenConditionInfo() {

    let infoStr = '';
    let tmpStr  = '';
    let selectConditionType = $('input[name=selectConditionType]:checked').val();

    // ------------------------------------------------------------------------
    // 자료갱신일
    // ------------------------------------------------------------------------
    let date = new Date();
    infoStr = '자료 갱신일: ' + date.oFormat('yyyy-MM-dd');

    // ------------------------------------------------------------------------
    // 검색기준 : 라디오
    // ------------------------------------------------------------------------
    $('input[name="searchTp"]:checked').each(function() {
      //var value = $(this).val();
      var text = $(this).closest('label').find('span').text();
      tmpStr = text;
    });
    if (fnIsEmpty(tmpStr) != true) {
      // 값이 존재하면
      tmpStr = '검색기준: ' + tmpStr;
    }
    infoStr += ' / ' + tmpStr;

    // ------------------------------------------------------------------------
    // 기준기간
    // ------------------------------------------------------------------------
    tmpStr = '';
    tmpStr = $('#startDe').val() + '~' + $('#endDe').val();

    if (fnIsEmpty($('#startDe').val()) != true || fnIsEmpty($('#endDe').val()) != true) {
      tmpStr = '조회기간: ' + tmpStr;
      if (fnIsEmpty(infoStr) == true) {
        infoStr += tmpStr;
      }
      else {
        infoStr += ' / ' + tmpStr;
      }
    }

    // ------------------------------------------------------------------------
    // 상품코드별합산여부 : 체크박스(단일)
    // ------------------------------------------------------------------------
    tmpStr = '';
    if($('input:checkbox[name=goodsSumYn]').is(':checked') == true) {
      tmpStr = '상품코드별 합산';
    }
    else {
      tmpStr = '상품코드별 미합산';
    }
    if (fnIsEmpty(infoStr) == true) {
      infoStr += tmpStr;
    }
    else {
      infoStr += ' / ' + tmpStr;
    }



    if (selectConditionType == 'singleSection') {
      // ----------------------------------------------------------------------
      // 단일조건
      // ----------------------------------------------------------------------

      // ----------------------------------------------------------------------
      // 코드검색유형
      // ----------------------------------------------------------------------
      tmpStr = '';
      tmpStr = $('#goodsSearchTp').data('kendoDropDownList').text();
      if (fnIsEmpty(infoStr) == true) {
        infoStr += tmpStr;
      }
      else {
        infoStr += ' / ' + tmpStr;
      }

      // ----------------------------------------------------------------------
      // 키워드
      // ----------------------------------------------------------------------
      tmpStr = '';
      let keyword = $('#keyword').val();
      keyword = keyword.replace(/(?:\r\n|\r|\n)/g, ',');
      keyword = keyword.replace(' ', '');
      infoStr += ': ' + keyword;

    }
    else {
      // ----------------------------------------------------------------------
      // 복수조건
      // ----------------------------------------------------------------------

      // ----------------------------------------------------------------------
      // 공급업체
      // ----------------------------------------------------------------------
      tmpStr = '';
      tmpStr = '공급업체: ' + $('#urSupplierId').data('kendoDropDownList').text();
      if (fnIsEmpty(infoStr) == true) {
        infoStr += tmpStr;
      }
      else {
        infoStr += ' / ' + tmpStr;
      }

      // ----------------------------------------------------------------------
      // 출고처그룹
      // ----------------------------------------------------------------------
      tmpStr = '';
      tmpStr = '출고처그룹: ' + $('#urWarehouseGrpCd').data('kendoDropDownList').text();
      if (fnIsEmpty(infoStr) == true) {
        infoStr += tmpStr;
      }
      else {
        infoStr += ' / ' + tmpStr;
      }

      // ----------------------------------------------------------------------
      // 출고처
      // ----------------------------------------------------------------------
      tmpStr = '';
      tmpStr = '출고처: ' + $('#urWarehouseId').data('kendoDropDownList').text();
      if (fnIsEmpty(infoStr) == true) {
        infoStr += tmpStr;
      }
      else {
        infoStr += ' / ' + tmpStr;
      }

      // ----------------------------------------------------------------------
      // 판매처그룹
      // ----------------------------------------------------------------------
      tmpStr = '';
      tmpStr = '판매처그룹: ' + $('#sellersGroupCd').data('kendoDropDownList').text();
      if (fnIsEmpty(infoStr) == true) {
        infoStr += tmpStr;
      }
      else {
        infoStr += ' / ' + tmpStr;
      }

      // ----------------------------------------------------------------------
      // 판매처
      // ----------------------------------------------------------------------
      tmpStr = '';
      tmpStr = '판매처: ' + $('#omSellersId').data('kendoDropDownList').text();
      if (fnIsEmpty(infoStr) == true) {
        infoStr += tmpStr;
      }
      else {
        infoStr += ' / ' + tmpStr;
      }

      // ----------------------------------------------------------------------
      // 브랜드유형
      // ----------------------------------------------------------------------
      tmpStr = '';

      let brandTp = $('#brandTp').data('kendoDropDownList').value();

      if (fnIsEmpty(brandTp) == true) {
        tmpStr = '브랜드: 전체';
      }
      else {
        tmpStr = $('#brandTp').data('kendoDropDownList').text();

        if (brandTp == 'GIFT_TARGET_BRAND_TP.STANDARD') {
          // ------------------------------------------------------------------
          // 표준브랜드
          // ------------------------------------------------------------------
          tmpStr += tmpStr + ': ' + $('#urBrandId').data('kendoDropDownList').text();
        }
        else {
          // ------------------------------------------------------------------
          // 전시브랜드
          // ------------------------------------------------------------------
          tmpStr += tmpStr + ': ' + $('#dpBrandId').data('kendoDropDownList').text();
        }
      }
      if (fnIsEmpty(infoStr) == true) {
        infoStr += tmpStr;
      }
      else {
        infoStr += ' / ' + tmpStr;
      }

      // ----------------------------------------------------------------------
      // 판매채널유형
      // ----------------------------------------------------------------------
      tmpStr = '';
      let i = 0;
      $('input[name="agentTypeCd"]:checked').each(function() {
        var id = $(this).val();
        var text = $(this).closest('label').find('span').text();
        if (id == 'ALL') {
          tmpStr = text;
          return false;
        }
        else {
          if (i == 0) {
            tmpStr += text;
          }
          else {
            tmpStr += ',' + text;
          }
        }
        i++;
      });
      if (fnIsEmpty(tmpStr) == true) {
        tmpStr = '판매처유형: 전체';
      }
      else {
        tmpStr = '판매처유형: ' + tmpStr
      }
      if (fnIsEmpty(infoStr) == true) {
        infoStr += tmpStr;
      }
      else {
        infoStr += ' / ' + tmpStr;
      }

      // ----------------------------------------------------------------------
      // 회원유형
      // ----------------------------------------------------------------------
      tmpStr = '';
      i = 0;
      $('input[name="buyerTypeCd"]:checked').each(function() {
        var id = $(this).val();
        var text = $(this).closest('label').find('span').text();
        if (id == 'ALL') {
          tmpStr = text;
          return false;
        }
        else {
          if (i == 0) {
            tmpStr += text;
          }
          else {
            tmpStr += ',' + text;
          }
        }
        i++;
      });
      if (fnIsEmpty(tmpStr) == true) {
        tmpStr = '회원유형: 전체';
      }
      else {
        tmpStr = '회원유형: ' + tmpStr
      }
      if (fnIsEmpty(infoStr) == true) {
        infoStr += tmpStr;
      }
      else {
        infoStr += ' / ' + tmpStr;
      }


      // ----------------------------------------------------------------------
      // 보관온도
      // ----------------------------------------------------------------------
      tmpStr = '';
      tmpStr = '보관온도: ' + $('#storageMethodTp').data('kendoDropDownList').text();
      if (fnIsEmpty(infoStr) == true) {
        infoStr += tmpStr;
      }
      else {
        infoStr += ' / ' + tmpStr;
      }

      // ----------------------------------------------------------------------
      // 카테고리유형
      // ----------------------------------------------------------------------
      tmpStr = '';
      tmpStr = '카테고리: ' + $('#ctgryTp').data('kendoDropDownList').text();
      if (fnIsEmpty(infoStr) == true) {
        infoStr += tmpStr;
      }
      else {
        infoStr += ' / ' + tmpStr;
      }

      // ----------------------------------------------------------------------
      // 상품유형
      // ----------------------------------------------------------------------
      tmpStr = '';
      i = 0;
      $('input[name="goodsTpCd"]:checked').each(function() {
        var id = $(this).val();
        var text = $(this).closest('label').find('span').text();
        if (id == 'ALL') {
          tmpStr = text;
          return false;
        }
        else {
          if (i == 0) {
            tmpStr += text;
          }
          else {
            tmpStr += ',' + text;
          }
        }
        i++;
      });
      if (fnIsEmpty(tmpStr) == true) {
        tmpStr = '상품유형: 전체';
      }
      else {
        tmpStr = '상품유형: ' + tmpStr
      }
      if (fnIsEmpty(infoStr) == true) {
        infoStr += tmpStr;
      }
      else {
        infoStr += ' / ' + tmpStr;
      }

      // ----------------------------------------------------------------------
      // 상품명
      // ----------------------------------------------------------------------
      tmpStr = '';
      let goodsNm = $('#goodsNm').val();
      if (fnIsEmpty(goodsNm) == false) {
        tmpStr = '상품명: ' + goodsNm;
        if (fnIsEmpty(infoStr) == false) {
          infoStr += tmpStr;
        }
        else {
          infoStr += ' / ' + tmpStr;
        }
      }
    }
    //console.log('# 검색조건 :: ', infoStr);
    // hidden에 Set
    $('#searchInfo').val(infoStr);
  }

  // ==========================================================================
  // # 엑셀다운로드-참여자목록/당첨자목록
  // ==========================================================================
  function fnExcelDownSaleGoodsStaticsList(gubn, keyVal) {
    //console.log('# fnExcelDownJoinList Start [', gubn, '][', keyVal, ']');
    // ------------------------------------------------------------------------
    // Validation
    // ------------------------------------------------------------------------
    let selectConditionType = $('input[name=selectConditionType]:checked').val();
    //console.log('# selectConditionType :: ', selectConditionType);

    if (selectConditionType == 'singleSection') {
      let keyword = $('#keyword').val();

      if (keyword == undefined || keyword == null || keyword == '') {
        fnMessage('', '<font color="#FF1A1A">[검색코드]</font> 필수 입력입니다.', 'keyword');
        return false;
      }
    }

    // 세션 체크
    //20210201  20210228  console.log('# PG_SESSION :: ', JSON.stringify(PG_SESSION));
    if (PG_SESSION == null || PG_SESSION == '' || PG_SESSION == undefined || PG_SESSION == 'undefined') {
      fnKendoMessage({ message : "세션이 종료되었습니다. \n다시 로그인 해 주십시오.", ok : function () {
        location.href = "/admVerify.html";
      }});
      return false;
    }

    // ------------------------------------------------------------------------
    // 검색조건정보 생성
    // ------------------------------------------------------------------------
    fnGenConditionInfo();

    let inputData   = $('#searchForm').formSerialize(true);
    let url         = '/admin/statics/sale/getExportExcelSaleGoodsStaticsList';
    let confirmMsg  = '엑셀파일을 다운로드 하시겠습니까?';

    //fnKendoMessage({
    //    message : fnGetLangData({key :"", nullMsg : confirmMsg })
    //  , type    : "confirm"
    //  , ok      : function() {
    //                fnExcelDownload(url, inputData);
    //              }
    //});

    // ------------------------------------------------------------------------
    // 세션체크
    // ------------------------------------------------------------------------
    fnAjax({
      url       :'/system/getSessionCheck'
    , method    : 'POST'
    //, success   : function(data, status, xhr) {
    , success   : function(data) {
                    if(data.session){
                      // ------------------------------------------------------
                      // 엑셀다운로드실행
                      // ------------------------------------------------------
                      var confirmMsg = '엑셀파일을 다운로드 하시겠습니까?';
                      //console.log('# inParam :: ', JSON.stringify(inParam));
                      //console.log('# inputData :: ', JSON.stringify(inputData));

                      fnKendoMessage({
                          message : fnGetLangData({key :"", nullMsg : confirmMsg })
                        , type    : "confirm"
                        , ok      : function() {
                                      fnExcelDownload(url, inputData);
                                    }
                        });
                    }
                    else {
                      fnKendoMessage({ message : "세션이 종료되었습니다. \n다시 로그인 해 주십시오.", ok : function () {
                        location.href = "/admVerify.html";
                      }});
                      return false;
                    }
                  }
    });

  }

  // ==========================================================================
  // ------------------------------- Html 버튼 바인딩  Start ------------------
  // ==========================================================================
  /** Common Search */
  $scope.fnBtnSearch      = function( )   { fnSearch();         };
  /** Common Clear */
  $scope.fnBtnClear       = function()    { fnClear();          };


  /** 판매현황 통계 엑셀 다운로드 */

  /** 상품별 판매현황 통계 엑셀 다운로드 */
  $scope.fnBtnExcelDownSaleGoodsStaticsList   = function() { fnExcelDownSaleGoodsStaticsList();};


}); // document ready - END
