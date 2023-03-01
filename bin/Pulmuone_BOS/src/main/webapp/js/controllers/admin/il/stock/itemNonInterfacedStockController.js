/**-----------------------------------------------------------------------------
 * description 		 : 재고 미연동 품목리스트
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.08		박영후          최초생성
 * @ 2020.12.10		이성준          기능추가
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var pageParam = fnGetPageParam();													//GET Parameter 받기
var paramIlItemCode = pageParam.paramIlItemCode;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'itemNonInterfacedStock',
			callback : fnUI
		});
	}

	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		//탭 변경 이벤트
        fbTabChange();	// fbCommonController.js - fbTabChange 이벤트 호출

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		if(paramIlItemCode != undefined) {
			$("#itemCodes").val(paramIlItemCode);
			fnSearch();
		}

		//fnInitMaskTextBox(); (HGRM-3505_재고수정 팝업 공통화_임상건)

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		//$('#fnSearch, #fnSave, #fnClear, #fnExcelDown').kendoButton(); (HGRM-3505_재고수정 팝업 공통화_임상건)
		$('#fnSearch, #fnClear, #fnExcelDown').kendoButton(); // (HGRM-3505_재고수정 팝업 공통화_임상건)
	}
	function fnSearch(){

		var data = $('#searchForm').formSerialize(true);

		data.itemName = data.itemName.trim();
		data.itemCodes = data.itemCodes.trim();

		if($("input[name=selectConditionType]:checked").val() == "codeSearch") {
			if(data.itemCodes == "") {
				$("#itemCodes").val("");
                fnKendoMessage({ message : "최소 1글자 이상 입력해야 됩니다." });
				return;
			}
		}

		var query = {
					page         : 1,
					pageSize     : PAGE_SIZE,
					filterLength : fnSearchData(data).length,
					filter :  {
						filters : fnSearchData(data)
					}
		};
		aGridDs.query( query );
	}
	function valueCheck(key, nullMsg, ID){
		fnKendoMessage({ message : fnGetLangData({ key : key, nullMsg : nullMsg}), ok : function focusValue(){
			$('#'+ID).focus();
		}});
		return false;
	}
	function fnClear(){
		if($("input[name=selectConditionType]:checked").val() == "codeSearch") {
			$("#itemCodes").val("");
		}

		if($("input[name=selectConditionType]:checked").val() == "condSearch") {
			$('#searchForm').formClear(true);
			$("input:radio[name='selectConditionType']:radio[value='condSearch']").prop('checked', true); // 선택하기
		}

		// 최초 조회시에는 상품 판매상태 '전체' 체크
        $('input[name=saleStatus]').each(function(idx, element) {
            $(element).prop('checked', true);
        });

        // 최초 조회시에는 상품유형 '전체' 체크
        $('input[name=goodsType]').each(function(idx, element) {
            $(element).prop('checked', true);
        });

        // 최초 조회시에는 보관방법 '전체' 체크
        $('input[name=keepMethod]').each(function(idx, element) {
            $(element).prop('checked', true);
        });
	}

	//엑셀 다운로드
	function fnExcelDown(){

		var data = $('#searchForm').formSerialize(true);

		fnExcelDownload('/admin/goods/stock/getStockNonErpExportExcel', data);

	}

	//재고수정
	/*  (HGRM-3505_재고수정 팝업 공통화_임상건)
	function fnSave(){

		let url  = '/admin/goods/stock/putStockNonErp';
		let cbId= 'update';

		var data = $('#inputForm').formSerialize(true);

		if(data.popStockCnt == ''){
		   data.popStockCnt = 0;
		}

		if( data.rtnValid ){
			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback(cbId, data);
					},
					isAction : 'update'
			});
		}
	}

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}
	*/
	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : '/admin/goods/stock/getStockNonErpList',
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 10
			}
			,navigatable: true
			,columns   : [
				 { field:'ilItemWarehouseId', hidden: true}
				,{ title : 'No.'			,width:'50px'	,attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
				,{ field:'ilItemCd'			,title : '품목코드/<br/>품목바코드'  , width:'100px'  ,attributes:{ style:'text-align:center' }
											,template: function(dataItem) {
											if(dataItem.ilItemCd != "" && dataItem.barcode != ""){
												var html =	"<a kind='itemDetlInfo' style='color:blue;cursor:pointer'>" +
												            kendo.htmlEncode(dataItem.ilItemCd) + "/<br/>" +
												            kendo.htmlEncode(dataItem.barcode) + "</a>";
											}else {
												var html =	"<a kind='itemDetlInfo' style='color:blue;cursor:pointer'>" +
												            kendo.htmlEncode(dataItem.ilItemCd) + "/-</a>";
											}
												return html;
											}}
				,{ field:'goodsTpNm'	    ,title : '상품유형/<br/>상품코드'   , width:'80px'	,attributes:{ style:'text-align:center' }
											,template: function(dataItem) {
											if(dataItem.goodsTpNm != "" && dataItem.ilGoodsId != ""){
												var html =	"<a kind='goodsDetlInfo' style='color:blue;cursor:pointer'>" +
												            kendo.htmlEncode(dataItem.goodsTpNm) + "/<br/>" +
												            kendo.htmlEncode(dataItem.ilGoodsId) + "</a>";
											}else if(dataItem.goodsTpNm != "" && dataItem.ilGoodsId == ""){
												var html =	"<a kind='goodsDetlInfo' style='color:blue;cursor:pointer'>" +
												            kendo.htmlEncode(dataItem.goodsTpNm) + "/-</a>";
											}else if(dataItem.goodsTpNm == "" && dataItem.ilGoodsId != ""){
												var html =	"<a kind='goodsDetlInfo' style='color:blue;cursor:pointer'>" +
												            "-/" + kendo.htmlEncode(dataItem.ilGoodsId) + "</a>";
											}else{
												var html =	"";
											}
												return html;
											}}
				,{ field:'saleStatusNm'	    ,title : '상품<br/>판매상태'        , width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'itemNm'			,title : '마스터품목명'		        , width:'250px'	,attributes:{ style:'text-align:left'   }}
				,{ field:'supplierNm'		,title : '공급업체'		        , width:'100px'	,attributes:{ style:'text-align:center' }}
				,{ field:'ctgryStdNm'		,title : '표준카테고리<br/>(대분류)'	, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'storageMethodTpNm',title : '보관<br/>방법'	        , width:'60px'	,attributes:{ style:'text-align:center' }}
				,{ field:'warehouseNm'		,title : '출고처'			        , width:'100px'	,attributes:{ style:'text-align:center' }}
				,{ title : '재고'			    , width:'60px'	,attributes:{ style:'text-align:center' }
											,template: function(dataItem) {
												let name = '';

												if(dataItem.unlimitStockYn == 'Y'){
													name = '무제한';
												}else{
													name = dataItem.notIfStockCnt
												}

												var html =	kendo.htmlEncode(name);
												return html;
											}}
				,{ title : '관리'	 , width:'80px', attributes:{ style:'text-align:center', class:'forbiz-cell-readonly' }
								 ,template: function(dataItem) {
										var html = '';

										if(fnIsProgramAuth("STOCK_CHANGE")){
										  if(dataItem.goodsTp == 'GOODS_TYPE.PACKAGE'){
											html = "";
										  }else{
											html = "<a role='button' class='k-button k-button-icontext' kind='fnStockClick' href='#'>상세보기</a>";
										  }
										}

										return html;
								 }}
		  ]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
        aGrid.bind("dataBound", function() {
        	let rowNum = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);

			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(rowNum);
				rowNum--;
			});
			//total count
            $('#countTotalSpan').text(aGridDs._total);
        });
	}

	//품목상세보기
    $('#aGrid').on("click", "a[kind=itemDetlInfo]", function(e) {
    	e.preventDefault();

		var dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));

	    var option = new Object();

	    option.url = "#/itemMgmModify";  // 마스터 품목 수정 화면 URL
	    option.menuId = 789;             // 마스터 품목 수정 화면 메뉴 ID
	    option.data = {                  // 마스터 품목 수정 화면으로 전달할 Data
	            ilItemCode : dataItem.ilItemCd,
	            isErpItemLink : dataItem.erpLinkIfYn,
	            masterItemType : dataItem.itemTp
	    };

	    option.target = '_blank';
    	fnGoNewPage(option);

    });

    //상품상세보기
    $('#aGrid').on("click", "a[kind=goodsDetlInfo]", function(e) {
    	e.preventDefault();

		var dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));

	    var option = new Object();

        let goodsTp = dataItem.goodsTp;

        option.data = { ilGoodsId : dataItem.ilGoodsId };

        switch(goodsTp){
            case "GOODS_TYPE.ADDITIONAL" : // 추가
                option.url = "#/goodsAdditional";
                option.menuId = 865;
                break;
            case "GOODS_TYPE.DAILY" : // 일일
                option.url = "#/goodsDaily";
                option.menuId = 1;
                break;
            case "GOODS_TYPE.DISPOSAL" : // 폐기임박
                option.url = "#/goodsDisposal";
                option.menuId = 921;
                break;
			case "GOODS_TYPE.GIFT" : // 증정
				option.url = "#/goodsAdditional";
				option.menuId = 865;
				break;
			case "GOODS_TYPE.GIFT_FOOD_MARKETING" : // 식품마케팅증정
				option.url = "#/goodsAdditional";
				option.menuId = 865;
				break;
            case "GOODS_TYPE.INCORPOREITY" : // 무형
                option.url = "#/goodsIncorporeal";
                break;
            case "GOODS_TYPE.NORMAL" : // 일반
                option.url = "#/goodsMgm";
                option.menuId = 98;
                break;
            case "GOODS_TYPE.PACKAGE" : // 묶음
                option.url = "#/goodsPackage";
                option.menuId = 768;
                break;
            case "GOODS_TYPE.RENTAL" : // 렌탈
                option.url = "#/goodsRental";
                break;
            case "GOODS_TYPE.SHOP_ONLY" : // 매장전용
                option.url = "#/goodsShopOnly";
                break;
        };

	    option.target = '_blank';
    	fnGoNewPage(option);
    });

	//재고수정 팝업
    /* (HGRM-3505_재고수정 팝업 공통화_임상건)
	function fnStockClick(e){
		e.preventDefault();

		var dataItem = this.dataItem($(e.currentTarget).closest("tr"));

		$('#popIlItemCd').val(dataItem.ilItemCd);
		$('#popItemName').val(dataItem.itemNm);
        $('#popIlItemWarehouseId').val(dataItem.ilItemWarehouseId);

		if(dataItem.unlimitStockYn == 'Y'){
			$("input:radio[name='popUnlimitStockYn']:radio[value='Y']").prop('checked', true);
			$('#popStockCnt').attr('disabled', true);
		}else{
			$("input:radio[name='popUnlimitStockYn']:radio[value='N']").prop('checked', true);
			$('#popStockCnt').attr('disabled', false);
			$('#popStockCnt').val(dataItem.notIfStockCnt);
		}

		fnKendoInputPoup({height:"250px" ,width:"600px",title:{nullMsg :'재고수정'} });
	};
	*/

    //재고수정 팝업 (HGRM-3505_재고수정 팝업 공통화_임상건) 시작
    $('#aGrid').on("click", "a[kind=fnStockClick]", function(e) {
    	e.preventDefault();
    	var dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));

    	let params = {};

    	params.ilItemCd = dataItem.ilItemCd;
    	params.itemNm = dataItem.itemNm;
    	params.ilItemWarehouseId = dataItem.ilItemWarehouseId;
    	params.unlimitStockYn = dataItem.unlimitStockYn;
    	params.notIfStockCnt = dataItem.notIfStockCnt;

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
					aGridDs.query();
				}
			}
		});

    });
  //재고수정 팝업 (HGRM-3505_재고수정 팝업 공통화_임상건) 끝
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start -------------------------------
    /* (HGRM-3505_재고수정 팝업 공통화_임상건)
	function fnInitMaskTextBox() {
		$('#popStockCnt').forbizMaskTextBox({fn:'onlyNum'});
	}
	*/
	//---------------Initialize Option Box Start -------------------------------
	function fnInitOptionBox(){

		fnTagMkRadio({
            id: 'selectConditionType',
            tagId: 'selectConditionType',
            chkVal: 'codeSearch',
            tab: true,
            data: [{
                "CODE": "codeSearch",
                "NAME": "단일조건 검색",
                "TAB_CONTENT_NAME": "singleSection"
            }, {
                "CODE": "condSearch",
                "NAME": "복수조건 검색",
                "TAB_CONTENT_NAME": "multiSection"
            }],
        });

		// 재고운영형태
		fnTagMkRadio({
			id    : 'stockOperType',
			tagId : 'stockOperType',
			chkVal: '',
			data  : [   { "CODE" : ""  , "NAME":'전체'  },
						{ "CODE" : "Y" , "NAME":'무제한' },
						{ "CODE" : "N" , "NAME":'한정재고'}
					],
			style : {}
		});

		// 재고관리여부(재고운영형태랑 같은것임)
		/* (HGRM-3505_재고수정 팝업 공통화_임상건)
		fnTagMkRadio({
			id    : 'popUnlimitStockYn',
			tagId : 'popUnlimitStockYn',
			chkVal: '',
			data  : [
						{ "CODE" : "Y" , "NAME":'무제한' },
						{ "CODE" : "N" , "NAME":'재고관리'}
					],
			style : {}
		});
		*/

		// 표준카테고리 대분류
        fnKendoDropDownList({
            id : "bigCategory",
            tagId : "bigCategory",
            url : "/admin/comn/getDropDownCategoryStandardList",
            params : { "depth" : "1" },
            textField : "categoryName",
            valueField : "categoryId",
            blank : "전체",
            async : false
        });

		// 판매상태
        fnTagMkChkBox({
            id: "saleStatus",
            tagId: "saleStatus",
            url: "/admin/comn/getCodeList",
            params: { "stCommonCodeMasterCode": "SALE_STATUS", "useYn": "Y" },
            beforeData: [{ "CODE": "ALL", "NAME": "전체" }],
            async: false
        });

        // 상품유형
        fnTagMkChkBox({
            id : "goodsType",
            tagId : "goodsType",
            url : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "GOODS_TYPE", "useYn" : "Y", "attr1" : "STOCK"},
            beforeData : [
				{ "CODE" : "ALL", "NAME" : "전체" }
				, { "CODE" : "NO_GOODS", "NAME" : "상품 미생성 품목" }
            ],
            async : false
        });

        //보관방법
        fnTagMkChkBox({
			id    : 'keepMethod',
			tagId : 'keepMethod',
			url : "/admin/comn/getCodeList",
			chkVal: '',
			style : {},
			params : {"stCommonCodeMasterCode" : "ERP_STORAGE_TYPE", "useYn" :"Y"},
			beforeData: [{ "CODE": "ALL", "NAME": "전체" }],
            async : false
		});

		// 공급업체 리스트 조회
		fnKendoDropDownList({
			id    : 'urSupplierId',
//			url : "/admin/ur/urCompany/getSupplierCompanyList",
            url : "/admin/comn/getDropDownAuthSupplierList",
			tagId : 'urSupplierId',
			textField :"supplierName",
			valueField : "supplierId",
			blank : "전체",
			async : false
		});

		// 출고처그룹
        fnKendoDropDownList({
            id : "warehouseGroup",
            tagId : "warehouseGroup",
            url : "/admin/comn/getCodeList",
            params : { "stCommonCodeMasterCode" : "WAREHOUSE_GROUP", "useYn" : "Y" },
            blank : "출고처 그룹 전체",
            async : false
        });


        // 출고처그룹 별 출고처
        fnKendoDropDownList({
            id : "warehouseGroupByWarehouseId",
            tagId : "warehouseGroupByWarehouseId",
//            url : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
            url : "/admin/comn/getDropDownAuthWarehouseGroupByWarehouseList",
            textField :"warehouseName",
            valueField : "warehouseId",
            blank : "출고처 전체",
            async : false,
            params : { "warehouseGroupCode" : "" }
        //  cscdId     : "warehouseGroup",
        //  cscdField  : "warehouseGroupCode"
        });

		$('#saleStatus').bind("change", onCheckboxWithTotalChange);

		// 상품 판매상태 '전체' 체크
		$('input[name=saleStatus]').each(function(idx, element) {
		    $(element).prop('checked', true);
		});

		$('#goodsType').bind("change", onCheckboxWithTotalChange);

		// 상품유형'전체' 체크
		$('input[name=goodsType]').each(function(idx, element) {
		    $(element).prop('checked', true);
		});

		$('#keepMethod').bind("change", onCheckboxWithTotalChange);

		// 보관방법'전체' 체크
		$('input[name=keepMethod]').each(function(idx, element) {
		    $(element).prop('checked', true);
		});

		//$('#popUnlimitStockYn').bind("change", onRadioChange); (HGRM-3505_재고수정 팝업 공통화_임상건)

		$('#warehouseGroup').bind("change", fnWareHouseGroupChange);

		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

	}
	//---------------Initialize Option Box End ------------------------------------------------

	//-------------------------------  Common Function start -------------------------------

	// 체크박스 change event
	function onCheckboxWithTotalChange(e) {

        // 첫번째 체크박스가 '전체' 체크박스라 가정함
        var totalCheckedValue = $("input[name=" + e.target.name + "]:eq(0)").attr('value');

        if( e.target.value == totalCheckedValue ) {  // '전체' 체크 or 체크 해제시

            if( $("input[name=" + e.target.name + "]:eq(0)").is(":checked") ) {  // '전체' 체크시

                $("input[name=" + e.target.name + "]:gt(0)").each(function(idx, element){
                    $(element).prop('checked', true);  // 나머지 모두 체크
                });

            } else { // '전체' 체크 해제시

                $("input[name=" + e.target.name + "]:gt(0)").each(function(idx, element){
                    $(element).prop('checked', false);  // 나머지 모두 체크 해제
                });

            }

        } else { // 나머지 체크 박스 중 하나를 체크 or 체크 해재시

            var allChecked = true; // 나머지 모두 체크 상태인지 flag

            $("input[name=" + e.target.name + "]:gt(0)").each(function(idx, element) {
                if( $(element).prop('checked') == false ) {
                    allChecked = false;  // 하나라도 체크 해제된 상태가 있는 경우 flag 값 false
                }
            });

            if( allChecked ) { // 나머지 모두 체크 상태인 경우
                $("input[name=" + e.target.name + "]:eq(0)").prop('checked', true);  // 나머지 모두 '전체' 체크
            } else {
                $("input[name=" + e.target.name + "]:eq(0)").prop('checked', false);  // 나머지 모두 '전체' 체크 해제
            }

        }
    }

	//라디오 버튼 change event
	/* (HGRM-3505_재고수정 팝업 공통화_임상건)
	function onRadioChange(e){
		if(e.target.value == 'Y'){
		   $('#popStockCnt').val('');
		   $('#popStockCnt').attr('disabled', true);
		}else{
		   $('#popStockCnt').attr('disabled', false);
		}
	}
	*/

	//출고처 그룹 이벤트
	function fnWareHouseGroupChange() {
    	fnAjax({
//            url     : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
            url : "/admin/comn/getDropDownAuthWarehouseGroupByWarehouseList",
            method : "GET",
            params : { "warehouseGroupCode" : $("#warehouseGroup").val() },

            success : function( data ){
                let warehouseGroupByWarehouseId = $("#warehouseGroupByWarehouseId").data("kendoDropDownList");
                warehouseGroupByWarehouseId.setDataSource(data.rows);
            },
            error : function(xhr, status, strError){
                fnKendoMessage({ message : xhr.responseText });
            },
            isAction : "select"
        });
    }

	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
				$('#inputForm').bindingForm(data, 'rows', true);
				break;
			case 'update':
				aGridDs.query();
				fnKendoMessage({message : '수정되었습니다.'});
				fnClose();
				break;
		}
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common Save*/
	// $scope.fnSave = function(){	 fnSave();}; (HGRM-3505_재고수정 팝업 공통화_임상건)
	/** Common ExcelDown*/
	$scope.fnExcelDown = function(){ fnExcelDown();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
