/**-----------------------------------------------------------------------------
 * system 			 : 출고처검색
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.08		안치열          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;


$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'warehousePopup',
			callback : fnUI
		});


		$('#ng-view').on("click", "#warehouseGroup_0" ,function(index){
			if($("#warehouseGroup_0").prop("checked")==true)
				$('input[name^=warehouseGroup]').prop("checked",true);
			else
				$('input[name^=warehouseGroup]').prop("checked",false);
		});

		$('#ng-view').on("click", "#warehouseGroup_1" ,function(index){
			if($("#warehouseGroup_1").prop("checked")==true){
				if($("#warehouseGroup_2").prop("checked")==true){
					$("#warehouseGroup_0").prop("checked",true);
				}else{
					$("#warehouseGroup_0").prop("checked",false);
				}
			}else{
				$('#warehouseGroup_0').prop("checked",false);
			}
		});


		$('#ng-view').on("click", "#warehouseGroup_2" ,function(index){
			if($("#warehouseGroup_2").prop("checked")==true){
				if($("#warehouseGroup_1").prop("checked")==true){
					$("#warehouseGroup_0").prop("checked",true);
				}else{
					$("#warehouseGroup_0").prop("checked",false);
				}
			}else{
				$('#warehouseGroup_0').prop("checked",false);
			}
		});


		// 파라미터 설정하기 --------------------------------------------------------------

		// ------------------------------------------------------------------------
	}

	function chkInit(){

		$('input[name^=warehouseGroup]').prop("checked",true);
	}

	function fnUI(){


		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		chkInit();

		fnSearch();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnClear').kendoButton();
	}

	function fnSearch(){
		$('#inputForm').formClear(false);
		$('#supplierCode').val(parent.POP_PARAM["parameter"].supplierCode);
		var data = $('#searchForm').formSerialize(true);

		var query = {
				page         : 1,
				pageSize     : PAGE_SIZE,
				filterLength : fnSearchData(data).length,
				filter :  {
					filters : fnSearchData(data)
				}
		};
		aGridDs.query(query);
	}
	function fnClear(){
		$('#searchForm').formClear(false);
		$("span#condiActive input:radio").eq(0).click();	//radio init
        chkInit();

	}


	function fnClose(params){

		if(params){
			parent.POP_PARAM = params;
		}
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		//$('#supplierCode').val(parent.POP_PARAM["parameter"].supplierCode);

		var data = $('#searchForm').formSerialize(true);

		aGridDs = fnGetPagingDataSource({
			url      : '/admin/ur/urCompany/getWarehouseList'
			//,params  : data
			,pageSize       : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,pageable :	{
				pageSizes: [20, 30, 50],
				buttonCount: 10,
				responsive: false
			}
			,navigatable: true
			,scrollable: true
			,height:380
			,columns   : [
				{ field:'no'		,title : 'No'	, width:'40px',attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
				,{ field:'warehouseGroupName'		,title : '출고처그룹'	, width:'80px',attributes:{ style:'text-align:center' }}
				,{ field:'warehouseName'	,title : '출고처명'	, width:'80px',attributes:{ style:'text-align:center; underline;color:blue;' }}
				,{ field:'supplierCompanyName'		,title : '공급업체명'	, width:'80px',attributes:{ style:'text-align:center' }}
				//,{ field:'workingDay'		,title : '출고가능일'	, width:'100px',attributes:{ style:'text-align:center' }}
				,{ field:'urSupplierId', hidden:true}
				,{ command: [{ text: '선택', click: fnSelectClient,
							click: function(e) {

					            e.preventDefault();
					            var tr = $(e.target).closest("tr"); // get the current table row (tr)
					            var data = this.dataItem(tr);

					            fnSelectClient(data);
							}
					}]
				, title: '', width: "60px", attributes:{ style:'text-align:center'  , class:'forbiz-cell-readonly #:#' }}

			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		aGrid.bind("dataBound", function() {

		    var row_num = aGridDs._total - ( ( aGridDs._page - 1 ) * aGridDs._pageSize );

		    $("#aGrid tbody > tr .row-number").each(function(index){
	            $(this).html(row_num);
	            row_num--;
		    });


		});

		$("#aGrid").on("click", "tbody>tr>td", function () {
			var index = $(this).index();

			// 출고처명 선택
//			if(index == 2){
//				fnWarehouseNameClick();
//			}
		});

		aGrid.bind("dataBound", function() {
            $('#totalCnt').text(aGridDs._total);
        });
	}
	function fnSelectClient(param){

		fnClose(param);
	};

	function fnWarehouseNameClick(){
//		var aMap = aGrid.dataItem(aGrid.select());
//		fnAjax({
//			url     : '/admin/ur/warehouse/getWarehouse',
//			params  : {urWarehouseId : aMap.urWarehouseId},
//			success :
//				function( data ){
//					//fnBizCallback("select",data);
//					alert('출고처 상세 팝업 호출 처리');
//				},
//			isAction : 'select'
//		});


		var aMap = aGrid.dataItem(aGrid.select());

		var params = {};
		params.urWarehouseId = aMap.urWarehouseId;

		fnKendoPopup({
			id     : 'warehouseDetailPopup',
			title  : '출고처 수정',
			src    : '#/warehouseDetailPopup',
			param  : params,
			width  : '600px',
			height : '1200px',
			success: function( id, data ){
				fnSearch();
			}
		});
	}
	//-------------------------------  Grid End  -------------------------------
	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){


		fnKendoDropDownList({
			id    : "warehouseGroup",
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "WAREHOUSE_GROUP", "useYn" :"Y"},
			tagId : 'warehouseGroupCode',
			blank : "전체"

		});


	}
	//---------------Initialize Option Box End ------------------------------------------------



	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
