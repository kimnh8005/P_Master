/**-----------------------------------------------------------------------------
 * description 		 : 연동 재고 내역관리

 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.08		박영후          최초생성
 * @ 2020.12.07		이성준          기능추가
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'itemInterfacedStock',
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

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnClear, #fnExcelDown').kendoButton();
	}

	function fnSearch(){

		if( $('#startCreateDate').val() == "" && $('#endCreateDate').val() != ""){
			fnKendoMessage({ message : "시작일 또는 종료일을 입력해주세요.."});
			return;
		}
		if( $('#startCreateDate').val() != "" && $('#endCreateDate').val() == ""){
			fnKendoMessage({ message : "시작일 또는 종료일을 입력해주세요."});
			return;
		}

		if( $('#startCreateDate').val() > $('#endCreateDate').val()){
			fnKendoMessage({ message : "시작일을 종료일보다 뒤로 설정할 수 없습니다."});
			return;
		}

		var data = $('#searchForm').formSerialize(true);

		data.itemName = data.itemName.trim();//공백제거
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

		//날짜버튼 초기화
		$('[data-id="fnDateBtn3"]').mousedown();

	}

	//엑셀다운로드
    function fnExcelDown(){

	   var data = $('#searchForm').formSerialize(true);

	   fnExcelDownload('/admin/goods/stock/getStockExprExportExcel', data);

    }

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : '/admin/goods/stock/getStockExprList',
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
				 { title : 'No.'			,width:'50px'	,attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
				,{ field:'baseDt'			,title : '연동일자'		      , width:'90px'	,attributes:{ style:'text-align:center' }}
				,{ field:'ilItemCd'			,title : '품목코드/<br/>품목바코드', width:'90px'	,attributes:{ style:'text-align:center' }
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
				,{ field:'itemNm'		    ,title : '품목명'			, width:'200px'	,attributes:{ style:'text-align:left' }
											,template: function(dataItem) {
												var html =	"<a kind='itemDetlInfo' style='color:blue;cursor:pointer'>" +
												            kendo.htmlEncode(dataItem.itemNm) + "</a>";
												return html;
											}}
				,{ field:'supplierNm'		,title : '공급업체'		, width:'90px'	,attributes:{ style:'text-align:center' }}
				,{ field:'storageMethodTpNm',title : '보관방법'		, width:'60px'	,attributes:{ style:'text-align:center' }}
				,{ field:'warehouseNm'		,title : '출고처'			, width:'90px'	,attributes:{ style:'text-align:center' }}
				,{ field:'expirationDt'		,title : '유통기한'		, width:'120px'	,attributes:{ style:'text-align:center' }
											,template: function(dataItem) {
												var html =	kendo.htmlEncode(dataItem.expirationDt) + "(" + kendo.htmlEncode(dataItem.restDistributionPeriod) + ")";
												return html;
											}}
				,{ field:'stockQty'		    ,title : '재고'			, width:'50px'	,attributes:{ style:'text-align:center' }}
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

	}

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
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
            }]
        });

		// 공급업체 리스트 조회
		fnKendoDropDownList({
			id    : 'urSupplierId',
//			url : "/admin/ur/urCompany/getSupplierCompanyList",
            url : "/admin/comn/getDropDownAuthSupplierList",
			tagId : 'urSupplierId',
			textField :"supplierName",
			valueField : "supplierId",
			blank : "전체"
		});

		// 출고처
		fnKendoDropDownList({
			id    : 'urWarehouseId',
//			url : "/admin/ur/urCompany/getWarehouseList",
            url : "/admin/comn/getDropDownAuthWarehouseGroupByWarehouseList",
			tagId : 'urWarehouseId',
			chkVal: '',
			params : {"stockOrderYn": "Y", "warehouseGroupCode" : ""},
			style : {},
			blank : "전체",
			textField :"warehouseName",
			valueField : "warehouseId"
		});

		//달력
		fnKendoDatePicker({
			id    : 'startCreateDate',
			format: 'yyyy-MM-dd',
			defVal : fnGetDayMinus(fnGetToday(),6),
			defType : 'oneWeek'
		});

		fnKendoDatePicker({
			id    : 'endCreateDate',
			format: 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startCreateDate',
			btnEndId : 'endCreateDate',
			defVal : fnGetToday(),
			defType : 'oneWeek',
			change: function(e){
	        	   if ($('#startCreateDate').val() == "" ) {
	                   return valueCheck("6495", "시작일을 선택해주세요.", 'endCreateDate');
	               }
				}
		});
	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
				//$('#inputForm').bindingForm(data, 'rows', true);
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
	/** Common ExcelDown*/
	$scope.fnExcelDown = function(){ fnExcelDown();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
