﻿/**-----------------------------------------------------------------------------
 * description 		 : 재고 기한관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.07		박영후          최초생성
 * @
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
			PG_ID  : 'itemStockExpirationDate',
			callback : fnUI
		});

	}

	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitMaskTextBox();  // Initialize Input Mask ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnSearch();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew,#fnSave,  #fnClear, #fnHistory').kendoButton();
	}
	function fnSearch(){

		var data = $('#searchForm').formSerialize(true);
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
		$('#searchForm').formClear(true);

		var warehouseDropdown = $("#searchWarehouse").data("kendoDropDownList");
		warehouseDropdown.dataSource.data([]);
		warehouseDropdown.enable(false);
	}
	function fnNew(){
		$('#inputForm').formClear(true);

		var warehouseDropdown = $("#warehouse").data("kendoDropDownList");
		warehouseDropdown.dataSource.data([]);
		warehouseDropdown.enable(false);

		fnKendoInputPoup({height:"200px" ,width:"600px", title:{ nullMsg :'재고 기한 설정' } });
	}
	function fnSave(){
		if ($("input[name='regFormRadio']:checked").val() != "D")  // 파일 등록일 경우
			alert("직접등록만 가능합니다.");

		var url  = '/admin/policy/shipArea/addBackCountry';
		var cbId = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/policy/shipArea/putBackCountry';
			cbId= 'update';
		}
		var data = $('#inputForm').formSerialize(true);

		if( data.rtnValid ){
			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback(cbId, data);
					},
					isAction : 'batch'
			});
		}
	}
	function fnDelRow(e){
		e.preventDefault();

		var dataItem = this.dataItem($(e.currentTarget).closest("tr"));

		fnKendoMessage({message : '삭제하시겠습니까?', type : "confirm", ok : function(){
			fnAjax({
				url     : '/admin/ps/shipArea/delBackCountry',
				params  : {zipCodeCsv : dataItem.zipCode},
				success :
					function( data ){
						fnBizCallback("delete",data);
					},
				isAction : 'select'
			});
		}});
	}

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	function fnHistory() {
		fnKendoPopup({
            id: 'goodsStockHistoryPopup',
            title: "이력관리",
            //param: {},
            src: '#/goodsStockExpirationHist',
            width: '1400px',
            height: '800px',
            success: function(id, data) {
                //fnSearch();
            }
        });
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : '/admin/policy/origin/getOriginList',
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50, 100],
				buttonCount : 10
			}
			,navigatable: true
//			        ,height:550
			,columns   : [
				{ field:'rowNumber'			,title : 'No.'							, width:'50px'		,attributes:{ style:'text-align:center' }}
				,{ field:'islandYn'			,title : '공급업체'						, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'originCode'		,title : '출고처'							, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		,title : '유통기간'							, width:'60px'	,attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		,title : '임박기준'			, width:'100px'	,attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		,title : '출고기준'			, width:'100px'	,attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		,title : '목표재고'					, width:'60px'	,attributes:{ style:'text-align:center' }}
				,{ command: { text: "수정", click: fnPut }, title : '관리'			, width:'80px' ,attributes:{ style:'text-align:center', class:'forbiz-cell-readonly' }}
			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
        aGrid.bind("dataBound", function() {
			//total count
            $('#countTotalSpan').text(aGridDs._total);
        });
	}

	function fnPut(e){
		var dataItem = this.dataItem($(e.currentTarget).closest("tr"));

		fnAjax({
			url     : '/admin/policy/shipArea/getBackCountry',
			params  : {zipCode : dataItem.rowNumber},
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'select'
		});
	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Input Mask Start ------------------------------------------------
	function fnInitMaskTextBox() {
		$('#searchExpireStartDate, #searchExpireEndDate, #searchApproachPoint, #searchOutPoint, #searchTargetStcok, #aaa, #bbb, #ccc, #ddd, #eee, #fff')
		.forbizMaskTextBox({fn:'onlyNum'});
	}
	//---------------Initialize Input Mask End ------------------------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

		// 팝업 공급업체 리스트 조회
		fnKendoDropDownList({
			id    : 'supplierCompany',
			tagId : 'supplierCompany',
			//params : {},
			autoBind : false,
			textField :"supplierName",
			valueField : "urSupplierId",
			blank : "전체"
		}).bind("change", onSupplierCompany);

		// 팝업 출고처
		fnKendoDropDownList({
			id    : 'warehouse',
			url : "/admin/ur/urCompany/getWarehouseList",
			tagId : 'warehouse',
			//params : {"supplierCode": ""},
			chkVal: '',
			style : {},
			blank : "전체",
			textField :"warehouseName",
			valueField : "urSupplierWarehouseId",
			autoBind : false
			//cscdId     : 'searchSupplierCompany',
			//cscdField  : 'urSupplierId'
		}).enable(false);


		// 검색 공급업체 리스트 조회
		fnKendoDropDownList({
			id    : 'searchSupplierCompany',
			url : "/admin/ur/urCompany/getSupplierCompanyList",
			tagId : 'searchApplyCompany',
			//params : {},
			textField :"supplierName",
			valueField : "urSupplierId",
			blank : "전체"
		}).bind("change", onSupplierCompany)
			.dataSource.bind("requestEnd", function() {  // 팝업 공급업체 dropdown 데이타 설정
				$("#supplierCompany").data("kendoDropDownList").setDataSource(this);
			});

		// 검색 출고처
		fnKendoDropDownList({
			id    : 'searchWarehouse',
			url : "/admin/ur/urCompany/getWarehouseList",
			tagId : 'searchWarehouse',
			//params : {"supplierCode": ""},
			chkVal: '',
			style : {},
			blank : "전체",
			textField :"warehouseName",
			valueField : "urSupplierWarehouseId",
			autoBind : false
			//cscdId     : 'searchSupplierCompany',
			//cscdField  : 'urSupplierId'
		}).enable(false);


		fnKendoDropDownList({
			id    : 'searchApproachOverBelow',
			data  : [
				{"CODE":"BELOW"	,"NAME":"이하"},
				{"CODE":"OVER"	,"NAME":"이상"}
			],
			textField :"NAME",
			valueField : "CODE",
			value : "ALL"
		});


		fnKendoDropDownList({
			id    : 'searchOutPointOverBelow',
			data  : [
				{"CODE":"BELOW"	,"NAME":"이하"},
				{"CODE":"OVER"	,"NAME":"이상"}
			],
			textField :"NAME",
			valueField : "CODE",
			value : "ALL"
		});


		fnKendoDropDownList({
			id    : 'searchTargetStcokOverBelow',
			data  : [
				{"CODE":"BELOW"	,"NAME":"이하"},
				{"CODE":"OVER"	,"NAME":"이상"}
			],
			textField :"NAME",
			valueField : "CODE",
			value : "ALL"
		});


		fnKendoDatePicker({
			id    : 'searchExpireDate',
			format: 'yyyy-MM-dd'
		});

		$("#searchForm input[type=radio][name=expireDateRadio]:eq(0)").on("click", function() {
			$("#searchExpireDate").data("kendoDatePicker").enable(true);
			$("#searchExpireStartDate, #searchExpireEndDate").prop("disabled", true);
		});
		$("#searchForm input[type=radio][name=expireDateRadio]:eq(1)").on("click", function() {
			$("#searchExpireDate").data("kendoDatePicker").enable(false);
			$("#searchExpireStartDate, #searchExpireEndDate").prop("disabled", false);
		});


		$("#inputForm input[type=radio][name=dateradio]:eq(0)").on("click", function() {
			$("#ddd").prop("disabled", false);
			$("#eee, #fff").prop("disabled", true);
		});
		$("#inputForm input[type=radio][name=dateradio]:eq(1)").on("click", function() {
			$("#ddd").prop("disabled", true);
			$("#eee, #fff").prop("disabled", false);
		});


		function onSupplierCompany(e) {
			var targetId = "";
			var sourceId = $(e.sender.element).attr("id");

			if (sourceId == "supplierCompany")
				targetId = "warehouse";
			else
				targetId = "searchWarehouse";

			var warehouseDropDown = $("#" + targetId).data("kendoDropDownList");

			if (e.sender.value() != "") {
				warehouseDropDown.enable(true);

	        	var dropDownUrl = warehouseDropDown.dataSource.options.transport.read.url;

	        	fnAjax({
	        		url     : dropDownUrl,
	        		method	: 'GET',
	        		params  : {"supplierCode": e.sender.value()},
	        		success :
	        			function( data ){
	        			warehouseDropDown.dataSource.data(data.rows);
	        			},
	        		isAction : 'batch'
	        	});
	        }
	        else {
	        	warehouseDropDown.dataSource.data([]);
	        	warehouseDropDown.enable(false);
	        }
		}


		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
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
				$('#inputForm').bindingForm(data, 'rows', true);
				fnKendoInputPoup({height:"200px" ,width:"600px",title:{nullMsg :'재고 기한 설정'} });
				break;
			case 'insert':
				if(data.rows =="DUP_DATA"){
					fnKendoMessage({message : '중복입니다.'});
				}else{
					fnSearch();
					$('#kendoPopup').data('kendoWindow').close();
					$('#searchForm').formClear(true);
					fnKendoMessage({message : '저장되었습니다.'});
				}
				break;
//			case 'save':
//				fnKendoMessage({message : '저장되었습니다.'});
//				break;
			case 'update':
				aGridDs.query();
				fnKendoMessage({message : '수정되었습니다.'});
				fnClose();
				break;
			case 'delete':
				aGridDs.query();
				fnClose();
				fnKendoMessage({message : '삭제되었습니다.'});
				break;

		}
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common New*/
	$scope.fnNew = function( ){	fnNew();};
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	/** Common Delete*/
	$scope.fnDel = function(){	 fnDel();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	/** 이력관리 */
	$scope.fnHistory = function( ){  fnHistory();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
