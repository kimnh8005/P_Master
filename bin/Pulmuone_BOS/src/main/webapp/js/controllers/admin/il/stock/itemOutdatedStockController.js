﻿/**-----------------------------------------------------------------------------
 * description 		 : 임박/폐기 예정 품목리스트
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.06		박영후          최초생성
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
			PG_ID  : 'itemOutdatedStock',
			callback : fnUI
		});

	}

	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnSearch();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnClear').kendoButton();
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
		$("#zipCode").attr("disabled", false);

		$('#inputForm').formClear(true);
		fnKendoInputPoup({height:"200px" ,width:"600px", title:{ nullMsg :'그룹추가' } });
	}
	function fnSave(){
	}

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : '/admin/ps/shipArea/getBackCountryList',
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
				{ field:'rowNumber'			,title : 'No.'					, width:'50px'		,attributes:{ style:'text-align:center' }}
				,{ field:'zipCode'			,title : '품목코드'				, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'zipCode'			,title : '마스터품목명'				, width:'100px'	,attributes:{ style:'text-align:center' }}
				,{ field:'jejuYn'			,title : '공급업체'				, width:'100px'	,attributes:{ style:'text-align:center' }}
				,{ field:'islandYn'			,title : '출고처'					, width:'100px'	,attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		,title : '브랜드'					, width:'100px'	,attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		,title : '유통기간'				 				,attributes:{ style:'text-align:center' },
					 columns: [ {field: "jejuYn", width: 60 ,attributes:{ style:'text-align:center' }}
					 			,{ command: { text: "상세보기", click: viewDetail }, title : '상세보기'			, width:'110px' ,attributes:{ style:'text-align:center', class:'forbiz-cell-readonly' }}]	}
				,{ field:'createDate'		,title : '정상<br>재고'			, width:'100px'	,attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		,title : '임박기준'				, width:'100px'	,attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		,title : '임박<br>재고'			, width:'100px'	,attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		,title : '출고기준'				, width:'100px'	,attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		,title : '출고기한<br>초과재고'		, width:'100px'	,attributes:{ style:'text-align:center' }}
				,{ command: { text: "임시", click: null }, title : '폐기임박<br>상품상태'			, width:'80px' ,attributes:{ style:'text-align:center', class:'forbiz-cell-readonly' }}
			]
			,change : fnGridClick
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
        aGrid.bind("dataBound", function() {
			//total count
            $('#countTotalSpan').text(aGridDs._total);
        });

		$('#aGrid thead tr:eq(1)').hide();

		function viewDetail(e) {
			var dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));

			fnKendoPopup({
			    id: 'goodsStockMgm',
			    title: '재고 상세 정보',
			    param: { "zipCode":  dataItem.zipCode },
			    src: '#/goodsStockMgm',
			    width: '1100px',
			    height: '800px',
			    success: function(id, data) {
			        //fnSearch();
			    }
			});
		}
	}

	function fnGridClick(){
		$("#zipCode").attr("disabled", true);

		var aMap = aGrid.dataItem(aGrid.select());

		fnAjax({
			url     : '/admin/ps/shipArea/getBackCountry',
			params  : {zipCode : aMap.zipCode},
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'select'
		});
	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

		fnKendoDropDownList({
			id    : 'searchExpireDateDropDown',
			data  : [
				{"CODE":"BELOW"		,"NAME":"이하"},
				{"CODE":"OVER"	,"NAME":"이상"}
			],
			textField :"NAME",
			valueField : "CODE",
			value : "ALL"
		});


		// 공급업체 리스트 조회
		fnKendoDropDownList({
			id    : 'searchSupplierCompany',
			url : "/admin/ur/urCompany/getSupplierCompanyList",
			tagId : 'searchApplyCompany',
			//params : {},
			textField :"supplierName",
			valueField : "urSupplierId",
			blank : "전체"
		}).bind("change", onSearchSupplierCompany);

		// 출고처
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


		fnTagMkRadio({
			id    :  'searchStockTypeRadio',
			tagId : 'searchStockTypeRadio',
			chkVal: 'ALL',
			data  : [   { "CODE" : "ALL"		, "NAME":'전체' },
						{ "CODE" : "C"			, "NAME":'임박' },
						{ "CODE" : "OPE"		, "NAME":'출고기한 초과' }
					],
			style : {}
		});

		fnTagMkChkBox({
			id    :  'searchOutdateGoodsSatus',
			data  : [   { "CODE" : "NT"		, "NAME":'미생성' },
						{ "CODE" : "SL"		, "NAME":'판매중' },
						{ "CODE" : "SO"		, "NAME":'일시품절' }
			],
			tagId : 'searchOutdateGoodsSatus',
			chkVal: '',
			style : {}
		});


		function onSearchSupplierCompany(e) {
			var searchWarehouseDropDown = $("#searchWarehouse").data("kendoDropDownList");

			if (e.sender.value() != "") {
	        	searchWarehouseDropDown.enable(true);

	        	var dropDownUrl = searchWarehouseDropDown.dataSource.options.transport.read.url;

	        	fnAjax({
	        		url     : dropDownUrl,
	        		method	: 'GET',
	        		params  : {"supplierCode": e.sender.value()},
	        		success :
	        			function( data ){
							searchWarehouseDropDown.dataSource.data(data.rows);
	        			},
	        		isAction : 'batch'
	        	});
	        }
	        else {
	        	searchWarehouseDropDown.dataSource.data([]);
	        	searchWarehouseDropDown.enable(false);
	        }
		}
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

//				OPER_TP_CODE = 'U';
//
//				var detailData = data.rows;
//
//				$("#zipCode").val(detailData.zipCode);
//
//				if (detailData.jejuYn == "Y")
//					$("#areaType_0").prop("checked", "checked");
//				else
//					$("#areaType_0").removeAttr("checked");
//				if (detailData.islandYn == "Y")
//					$("#areaType_1").prop("checked", "checked");
//				else
//					$("#areaType_1").removeAttr("checked");
//
//				$("#regFormRadio_0").click();
//				fnKendoInputPoup({height:"200px" ,width:"600px",title:{nullMsg :'원산지 등록'} });
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
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
