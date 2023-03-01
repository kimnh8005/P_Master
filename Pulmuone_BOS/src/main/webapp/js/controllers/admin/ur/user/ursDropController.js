﻿/**-----------------------------------------------------------------------------
 * description 		 : 상점관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2017.01.13		최봉석          최초생성
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
			PG_ID  : 'ursDrop',
			callback : fnUI
		});

	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

	//	fnSearch();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnClear').kendoButton();
		//$('#fnDel').kendoButton({ enable: false });
	}
	function fnSearch(){

        if ($('#startCreateDate').val() > $('#endCreateDate').val()) {
            return valueCheck("6410", "시작일을 종료일보다 뒤로 설정할 수 없습니다.", 'START_CREATED');
        }

		$('#inputForm').formClear(false);
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

	function fnClear(){
		$('#searchForm').formClear(true);
		$(".set-btn-type6").attr("fb-btn-active" , false );
	}
	function valueCheck(key, nullMsg, ID){
		fnKendoMessage({ message : fnGetLangData({ key : key, nullMsg : nullMsg}), ok : function focusValue(){
			$('#'+ID).focus();
		}});

		return false;
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){

		aGridDs = fnGetPagingDataSource({
			url      : '/admin/ur/UserDrop/getUserDropList',
			pageSize : PAGE_SIZE
		});

		aGridOpt = {
			dataSource: aGridDs
			,navigatable: true
			,pageable  : {
				buttonCount : 10
				,pageSizes: [20, 30, 50]
			}
			//,height:550
			,columns   : [
					{title : 'No.'										, width:'90px'	,attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
					,{ field:'loginId'			,title : '회원ID'			, width:'150px'	,attributes:{ style:'text-align:center' }}
					,{ field:'status'			,title : '탈퇴시점 회원상태'	, width:'150px'	,attributes:{ style:'text-align:center' }}
					,{ field:'dropReasonName'	,title : '탈퇴구분'		, width:'150px'	,attributes:{ style:'text-align:center' }}
					,{ field:'comment'			,title : '탈퇴사유'		, width:'200px'	,attributes:{ style:'text-align:center' }}
					,{ field:'createDate'		,title : '탈퇴일'			, width:'150px'	,attributes:{ style:'text-align:center' }}
			]
			,scrollable: false
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
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});
		fnKendoDatePicker({
			id    : 'startCreateDate',
			format: 'yyyy-MM-dd'
			//,defVal : fnGetDayMinus(fnGetToday(),7)
		});
		fnKendoDatePicker({
			id    : 'endCreateDate',
			format: 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startCreateDate',
			btnEndId : 'endCreateDate'
			//,defVal : fnGetToday()
            ,change: function(e){
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
				$('#inputForm').bindingForm(data, 'rows', true);
				fnKendoInputPoup({height:"650px" ,width:"650px",title:{key :"5863",nullMsg :'탈퇴회원 상세' } });
				break;
			case 'insert':
				if(data.rows =="DUP_DATA"){
					fnKendoMessage({message : fnGetLangData({key :"370",nullMsg :'중복입니다.' })  ,ok :inputFocus});
				}else{
					aGridDs.insert(data.rows);;
					fnKendoMessage({message : fnGetLangData({key :"369",nullMsg :'입력되었습니다.' }) ,ok :fnNew});
				}
				break;
			case 'save':
				fnKendoMessage({message : fnGetLangData({key :"368",nullMsg :'저장되었습니다.' })});
				break;
			case 'update':
				fnUpdateGrid(data,$("#aGrid"),"rows");
				fnKendoMessage({message : fnGetLangData({key :"367",nullMsg :'수정되었습니다.' }) });
				break;
			case 'delete':
				aGridDs.remove(data);
				fnNew();
				//aGridDs.total = aGridDs.total-1;
				fnKendoMessage({message : fnGetLangData({key :"366",nullMsg :'삭제되었습니다.' })});
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
