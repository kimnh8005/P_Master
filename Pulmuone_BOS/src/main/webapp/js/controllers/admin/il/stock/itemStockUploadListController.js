﻿/**-----------------------------------------------------------------------------
 * description 		 : ERP 재고 엑셀 업로드 내역
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.23		이성준          최초생성
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
			PG_ID  : 'itemStockUploadList',
			callback : fnUI
		});
	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch,#fnClear').kendoButton();
	}
	function fnSearch(){

        if ($('#startCreateDate').val() != "" && $('#endCreateDate').val() == "") {
            return valueCheck("", "종료일을 입력해주세요.", 'endCreateDate');
        }
        if ($('#startCreateDate').val() > $('#endCreateDate').val()) {
            return valueCheck("6410", "시작일을 종료일보다 뒤로 설정할 수 없습니다.", 'START_CREATED');
        }

		var data;
		data = $('#searchForm').formSerialize(true);
		$('#inputForm').formClear(false);
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

		//날짜버튼 초기화
		$('[data-id="fnDateBtn3"]').mousedown();
	}
	function fnGetDetail(e){
		e.preventDefault();
		var dataItem = aGrid.dataItem( $(e.currentTarget).closest('tr') );

		var option = new Object();

		option.url = "#/itemStockUploadDetlList";// ERP 재고 엑셀 업로드 상세내역 URL
		option.menuId = 930; // ERP 재고 엑셀 업로드 상세내역 메뉴 ID
		option.data = {ilStockExcelUploadLogId:dataItem.ilStockExcelUploadLogId, createDt:dataItem.createDt};//param
		option.target = '_blank';

		fnGoNewPage(option);
	}

	function fnExcelDown(e){

		e.preventDefault();

		var dataItem = aGrid.dataItem( $(e.currentTarget).closest('tr') );

		$('#ilStockExcelUploadLogId').val(dataItem.ilStockExcelUploadLogId);

		var data = $('#searchForm').formSerialize(true);

		fnExcelDownload('/admin/goods/stock/exportExcelUploadList', data);

	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : '/admin/goods/stock/getStockUploadList'
			,pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 10
			}
			,navigatable: true
			,columns   : [
				     { field:'ilStockExcelUploadLogId', hidden:true}
				    ,{ title:'No.'		  ,width:'20px'	   , attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
					,{ field:'createDt'	  ,title : '등록일자<br>관리자' , width:'90px'	,attributes:{ style:'text-align:center' }
						, template : function(data) {
							return data.createDt + '<br/>' + data.userNm + '(' + data.loginId + ')';
						}
					}
					,{ field:'successCnt' ,title : '정상'	   , width:'90px'	,attributes:{ style:'text-align:center' }}
					,{ field:'failCnt'	  ,title : '실패'	   , width:'90px'	,attributes:{ style:'text-align:center' }}
					,{ command:[{text: "상세보기", click: fnGetDetail},{text: " 실패내역 다운로드", click: fnExcelDown}], title: '관리', width: "90px", attributes:{ style:'text-align:center'}}
			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		aGrid.bind("dataBound", function(){
			let rowNum = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);

			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(rowNum);
				rowNum--;
			});
			$("#countTotalSpan").text(aGridDs._total);
		});
	}
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
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
				$('#searchForm').bindingForm(data, 'rows', true);
				break;
			case 'detail':
				//form data binding
				$('#searchForm').bindingForm(data, 'rows', true);
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
