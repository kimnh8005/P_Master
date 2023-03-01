﻿/**-----------------------------------------------------------------------------
 * description 		 : 상품영양정보 관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.16		박영후          최초생성
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
			PG_ID  : 'itemNutrition',
			callback : fnUI
		});

	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitMaskTextBox();  // Initialize Input Mask ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnSearch();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew,#fnSave,  #fnClear').kendoButton();
		//$('#fnDel').kendoButton({ enable: false });
	}
	function fnSearch(){

		var query = {
				page         : 1,
				pageSize     : PAGE_SIZE
	    };

		aGridDs.read( query );
	}
	function fnClear(){
		$('#searchForm').formClear(true);
	}
	function fnNew(){
		$("#inputIlNutritionCodeTr").hide();
		$("#ilNutritionCode").prop("required", false);
		$('#inputForm').formClear(true);
		fnKendoInputPoup({height:"320px" ,width:"520px",title:{nullMsg :'분류 상세 정보'} });
	}
	function fnSave(){
		var url  = '/admin/goods/nutrition/addGoodsItemNutrition';
		var cbId = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/goods/nutrition/putGoodsNutrition';
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
	function fnDel(){
		fnKendoMessage({message : '삭제하시겠습니까?', type : "confirm", ok : function(){
			fnAjax({
				url     : '/admin/goods/nutrition/delGoodsItemNutrition',
				params  : {ilNutritionCode : $("#ilNutritionCode").val()},
				success :
					function( data ){
						fnBizCallback("delete",data);
					},
				isAction : 'delete'
			});
		}});
	}

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	function fnDown() {
//		var theForm = document.excelDownloadForm;
//		theForm.action = "/admin/goods/nutrition/exportExcel";
//		theForm.submit();
        var data = $('#excelDownloadForm').formSerialize(true);
        fnExcelDownload("/admin/goods/nutrition/exportExcel", data);
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : '/admin/goods/nutrition/getGoodsNutritionList',
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50, 100],
				buttonCount : 10
			}
			,navigatable: true
	        ,scrollable: true
			,height:700
			,columns   : [
				{ field:'sort'					,title : '노출순서'			, width:'50px'	,attributes:{ style:'text-align:center' }}
				,{ field:'ilNutritionCode'		,title : '분류코드'			, width:'200px'	,attributes:{ style:'text-align:center' }}
				,{ field:'nutritionName'		,title : '분류명'				, width:'200px'	,attributes:{ style:'text-align:center' }}
				,{ field:'nutritionUnit'		,title : '분류단위'			, width:'100px'	,attributes:{ style:'text-align:center' }}
			]
			,change: fnGridRowLick
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
        aGrid.bind("dataBound", function() {
			//total count
            $('#countTotalSpan').text(aGridDs._total);
        });
	}
	function fnGridRowLick(e){
		var aMap = aGrid.dataItem(aGrid.select());
		fnAjax({
			url     : '/admin/goods/nutrition/getGoodsNutrition',
			params  : {ilNutritionCode : aMap.ilNutritionCode},
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'delete'
		});
	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Input Mask Start ------------------------------------------------
	function fnInitMaskTextBox() {
		fnInputValidationForNumber("sort");
	}
	//---------------Initialize Input Mask End ------------------------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

		fnTagMkRadioYN({id: "nutritionPercentYn" , tagId : "nutritionPercentYn",chkVal: 'Y'});

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
				$("#inputIlNutritionCodeTr").show();
				$("#ilNutritionCode").prop("required", true);
				fnKendoInputPoup({height:"370px" ,width:"520px",title:{nullMsg :'분류 상세 정보'} });
				break;
			case 'insert':
				aGridDs.data([]);
				fnSearch();
				$('#kendoPopup').data('kendoWindow').close();
				fnKendoMessage({message : '저장되었습니다.'});
				break;
//			case 'save':
//				fnKendoMessage({message : '저장되었습니다.'});
//				break;
			case 'update':
				aGridDs.read();
				fnKendoMessage({message : '수정되었습니다.'});
				fnClose();
				break;
			case 'delete':
				aGridDs.read();
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
	/** Excel Down*/
	$scope.fnDown = function( ){  fnDown();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
