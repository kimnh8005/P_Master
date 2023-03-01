/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 클레임 사유별 현황 통계
 * @
 * @
 * @ 수정일        수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.07.27    이원호        최초생성
 * @
 ******************************************************************************/
'use strict';

var gridDs, gridOpt, grid;
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
            PG_ID     : 'claimStatics'
          , callback  : fnUI
        });
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

    // 출고처그룹 초기화에 따른 촐고처 전체 조회
    fnWareHouseGroupChange(null);
    // 판매처그룹 초기화에 따른 판매처 전체 조회
    fnSellerGroupChange(null);
    // 브랜드유형 초기화(미선택)에 따른 표준브랜드/전시브랜드 숨김
    $('#urBrandIdDiv').hide();
    $('#dpBrandIdDiv').hide();
    // 판매채널유형 : 전체선택
    $("input[name=agentTypeCd]:checkbox").prop("checked", true);
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
  }

    // ==========================================================================
    // # 기본 설정
    // ==========================================================================
    function fnDefaultSet() {
    };

  // ==========================================================================
  // # 초기화 - 옵션박스
  // ==========================================================================
  function fnInitOptionBox() {

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
    // 공급업체 [콤보]
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : 'urSupplierId'
      , tagId       : 'urSupplierId'
      , url         : '/admin/comn/getDropDownSupplierList'
      //, params      : {'stCommonCodeMasterCode' : '', 'useYn' : 'Y'}
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
    });
    // 표준카테고리 중분류
    fnKendoDropDownList({
        id          : 'categoryStandardDepth2'
      , tagId       : 'categoryStandardDepth2'
      , url         : '/admin/comn/getDropDownCategoryStandardList'
      , textField   : 'categoryName'
      , valueField  : 'categoryId'
      , blank       : '중분류'
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
    });
    // 전시카테고리 중분류
    fnKendoDropDownList({
        id          : 'categoryDepth2'
      , tagId       : 'categoryDepth2'
      , url         : '/admin/comn/getDropDownCategoryList'
      , textField   : 'categoryName'
      , valueField  : 'categoryId'
      , blank       : '중분류'
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
      , isDupUrl    : 'Y'
      , style       : {}
      , beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
      , success     : function() {
                        // 전체선택
                        $("input[name=goodsTpCd]:checkbox").prop("checked", true);
                      }
    });

  }

  // ==========================================================================
  // # 초기화 - 이벤트
  // ==========================================================================
  function fnInitEvent() {

    // ------------------------------------------------------------------------
    // 체크박스변경(전체선택관련)
    // ------------------------------------------------------------------------
    fbCheckboxChange();

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
      let brandTp = $('#brandTp').val();
      if (brandTp == 'GIFT_TARGET_BRAND_TP.STANDARD') {
        $('#urBrandIdDiv').show();
        $('#dpBrandIdDiv').hide();
        $("#dpBrandId").data("kendoDropDownList").enable(true);
      }
      else if (brandTp == 'GIFT_TARGET_BRAND_TP.DISPLAY') {
        $('#urBrandIdDiv').hide();
        $('#dpBrandIdDiv').show();
        $("#urBrandId").data("kendoDropDownList").enable(true);
      }
      else {
        $('#urBrandIdDiv').hide();
        $('#dpBrandIdDiv').hide();
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
    let startDe         = $('#startDe').val();
    let endDe           = $('#endDe').val();
    if (fnIsEmpty(startDe)) {
      fnMessage('', '<font color="#FF1A1A">[기간검색 시작일자]</font> 필수 입력입니다.', 'startDe');
      return;
    }
    if (fnIsEmpty(endDe)) {
      fnMessage('', '<font color="#FF1A1A">[기간검색 종료일자]</font> 필수 입력입니다.', 'endDe');
      return;
    }

    // ------------------------------------------------------------------------
    // 그리드초기화
    // ------------------------------------------------------------------------
    fnInitGrid(gbCtgryTp);

    // 리스트 조회
    //$('#searchForm').formClear(false);
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
        url      : '/admin/statics/claim/getClaimReasonStaticsList'
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
                { field:'bosClaimLargeName', title: '취소사유<BR>(대)', width:60 , attributes:{ style:'text-align:center' }}
                , { field:'bosClaimMiddleName', title: '취소사유<BR>(중)', width: 60 , attributes:{ style:'text-align:center' }}
                , { field:'bosClaimSmallName', title: '귀책처', width: 60 , attributes:{ style:'text-align:center' }}
                , { field:'targetName', title: '귀책구분', width: 60 , attributes:{ style:'text-align:center' }, format: '{0:\#\#,\#}'}
                , { title   : '취소'
                    , columns : [
                        { field:'cancelCompletePrice', title: '취소<BR>금액', width: 60 , attributes:{ style:'text-align:center'  }, format: '{0:\#\#,\#}'}
                        , { field:'cancelCompleteCount', title: '취소<BR>건수', width: 60 , attributes:{ style:'text-align:center'  }, format: '{0:\#\#,\#}'}
                        , { field:'cancelClaimCount', title: '취소<BR>수량', width: 60 , attributes:{ style:'text-align:center'  }, format: '{0:\#\#,\#}'}
                    ]
                }
                , { title   : '반품'
                    , columns : [
                        { field:'returnCompletePrice', title: '반품<BR>금액', width: 60 , attributes:{ style:'text-align:center'  }, format: '{0:\#\#,\#}'}
                        , { field:'returnCompleteCount', title: '반품<BR>건수', width: 60 , attributes:{ style:'text-align:center'  }, format: '{0:\#\#,\#}'}
                        , { field:'returnClaimCount', title: '반품<BR>수량', width: 60 , attributes:{ style:'text-align:center'  }, format: '{0:\#\#,\#}'}
                    ]
                }
                , { title   : '재배송'
                    , columns : [
                        { field:'exchangeCompletePrice', title: '재배송<BR>금액', width: 60 , attributes:{ style:'text-align:center'  }, format: '{0:\#\#,\#}'}
                        , { field:'exchangeCompleteCount', title: '재배송<BR>건수', width: 60 , attributes:{ style:'text-align:center'  }, format: '{0:\#\#,\#}'}
                        , { field:'exchangeClaimCount', title: '재배송<BR>수량', width: 60 , attributes:{ style:'text-align:center'  }, format: '{0:\#\#,\#}'}
                    ]
                }
          ]
    };

    grid = $('#grid').initializeKendoGrid( gridOpt ).cKendoGrid();

    // ------------------------------------------------------------------------
    // dataBound
    // ------------------------------------------------------------------------
    grid.bind('dataBound', function(e) {
        $('#totalCnt').text(gridDs._total);
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

    function fnGetCheckBoxText(id){
	    var value = "";
        $('form[id=searchForm] :checkbox[name='+ id +']:checked').each(function() {
            value += $(this).closest('label').find('span').text() + ","
        });
        if (value == null)
            value = "";
        value = value.substring(0, value.length - 1);
        return value;
	}

  // ==========================================================================
  // # 엑셀다운로드-참여자목록/당첨자목록
  // ==========================================================================
  function fnExcelDown(gubn, keyVal) {
    let startDe         = $('#startDe').val();
    let endDe           = $('#endDe').val();
    if (fnIsEmpty(startDe)) {
        fnMessage('', '<font color="#FF1A1A">[기간검색 시작일자]</font> 필수 입력입니다.', 'startDe');
        return;
    }
    if (fnIsEmpty(endDe)) {
        fnMessage('', '<font color="#FF1A1A">[기간검색 종료일자]</font> 필수 입력입니다.', 'endDe');
        return;
    }

    // 세션 체크
    if (PG_SESSION == null || PG_SESSION == '' || PG_SESSION == undefined || PG_SESSION == 'undefined') {
      fnKendoMessage({ message : "세션이 종료되었습니다. \n다시 로그인 해 주십시오.", ok : function () {
        location.href = "/admVerify.html";
      }});
      return false;
    }

    // ------------------------------------------------------------------------
    // 검색조건정보 생성
    // ------------------------------------------------------------------------
    let inputData   = $('#searchForm').formSerialize(true);
    inputData.supplierName = $('#urSupplierId').data('kendoDropDownList').text();
    inputData.urWarehouseGrpCdName = $('#urWarehouseGrpCd').data('kendoDropDownList').text();
    inputData.urWarehouseIdName = $('#urWarehouseId').data('kendoDropDownList').text();
    inputData.sellersGroupCdName = $('#sellersGroupCd').data('kendoDropDownList').text();
    inputData.omSellersIdName = $('#omSellersId').data('kendoDropDownList').text();
    inputData.brandTpName = $('#brandTp').data('kendoDropDownList').text();
    inputData.urBrandIdName = $('#urBrandId').data('kendoDropDownList').text();
    inputData.agentTypeCdName = fnGetCheckBoxText('agentTypeCd');
    inputData.buyerTypeCdName = fnGetCheckBoxText('buyerTypeCd');
    inputData.storageMethodTpName = $('#storageMethodTp').data('kendoDropDownList').text();
    inputData.ctgryTpName = $('#ctgryTp').data('kendoDropDownList').text();
    let ctgryTp = $('#ctgryTp').val();
    if (ctgryTp == 'S') {
        inputData.ctgryStdIdDepth1Name = $('#categoryStandardDepth1').data('kendoDropDownList').text();
        inputData.ctgryStdIdDepth2Name = $('#categoryStandardDepth2').data('kendoDropDownList').text();
        inputData.ctgryStdIdDepth3Name = $('#categoryStandardDepth3').data('kendoDropDownList').text();
        inputData.ctgryStdIdDepth4Name = $('#categoryStandardDepth4').data('kendoDropDownList').text();
    } else if (ctgryTp == 'D') {
        inputData.ctgryIdDepth1Name = $('#categoryDepth1').data('kendoDropDownList').text();
        inputData.ctgryIdDepth2Name = $('#categoryDepth2').data('kendoDropDownList').text();
        inputData.ctgryIdDepth3Name = $('#categoryDepth3').data('kendoDropDownList').text();
        inputData.ctgryIdDepth4Name = $('#categoryDepth4').data('kendoDropDownList').text();
    }
    inputData.goodsTpCdName = fnGetCheckBoxText('goodsTpCd');

    let url         = '/admin/statics/claim/getClaimReasonStaticsExcelDownload';
    let confirmMsg  = '엑셀파일을 다운로드 하시겠습니까?';

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
    /** 엑셀 다운로드 */
    $scope.fnBtnExcelDown   = function() { fnExcelDown();};

}); // document ready - END
