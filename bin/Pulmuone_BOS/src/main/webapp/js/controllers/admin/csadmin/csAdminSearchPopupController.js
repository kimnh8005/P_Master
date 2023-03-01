/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : CS관리 회원조회
 *
 *
 * @ 수정일          수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2020.12.23    안치열        최초생성
 * @
 ******************************************************************************/
'use strict';



var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var paramData ;
if(parent.POP_PARAM['parameter']){

	paramData = parent.POP_PARAM['parameter'];
	paramData.menuType = 'MENU_TYPE.PAGE';
}

if(paramData.csFindKeyword != undefined){
	$("#csFindKeyword").val(paramData.csFindKeyword);
}

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'csAdminSearchPopup',
			callback : fnUI
		});

	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnSearch();
	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch,  #fnClear, #fnClose').kendoButton();
	}

	function fnSearch(){

		var data;
		data = $('#searchForm').formSerialize(true);
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

		aGridDs = fnGetPagingDataSource({
			url      : '/admin/ur/buyer/getBuyerList',
			pageSize : PAGE_SIZE
		});

		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 5
			}
			,navigatable: true
			,scrollable: true
			,columns   : [
				 { field:'no'				, title : 'No'			, width:'80px'	, attributes:{ style:'text-align:center' }, template:"<span class='row-number'></span>"}
				,{ field:'userType'			, title: '회원유형'		, width: '80px'	, attributes:{ style: 'text-align:center' }, template: "#=(employeeYn=='Y') ? '임직원' : '일반'#"}
				,{ field:'userName'			, title : '회원명'		, width:'90px'	, attributes:{ style:'text-align:center' }}
				,{ field:'loginId'			, title : '회원ID'		, width:'90px'	, attributes:{ style:'text-align:center' }}
				,{ field:'mobile'			, title : '휴대폰'		, width:'150px'	, attributes:{ style:'text-align:center' }}
				,{ field:'mail'				, title : 'EMAIL'		, width:'150px'	, attributes:{ style:'text-align:center' }}
				,{ field:'groupName'		, title : '회원등급'		, width:'90px'	, attributes:{ style:'text-align:center' }}
				,{ field:'status'			, title : '회원상태'		, width:'90px'	, attributes:{ style:'text-align:center' }}
				,{ field:'accumulateCount'	, title : '블랙리스트'		, width:'90px'	, attributes:{ style:'text-align:center' }}
				,{ title:  fnGetLangData({key :"660",nullMsg :'관리' }), width: "90px", attributes:{ style:'text-align:center;'  , class:'forbiz-cell-readonly' }
				,command: [ { name: 'cEdit',  text: '선택'		, imageClass: "k-i-add", className: "f-grid-add k-margin5", iconClass: "k-icon", click: fnSelectBuyer,
							click: function(e) {
								 e.preventDefault();
						            var tr = $(e.target).closest("tr"); // get the current table row (tr)
						            var data = this.dataItem(tr);

						            fnSelectBuyer(data);
							}
					}]
				}
				,{ field:'urUserId', hidden:true}
				,{ field:'userType', hidden:true}
				,{ field:'status', hidden:true}
			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

        aGrid.bind("dataBound", function(){
        	var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(row_num);
				row_num--;
			});

        	$('#countTotalSpan').text(aGridDs._total);
        });


		$("#aGrid").on("dblclick", "tbody>tr", function () {
				//fnGridClick();
		});

	}
	function fnSelectBuyer(param){

		fnAjax({
			url     : '/admin/ur/buyer/getBuyer',
			params  : {urUserId : param.urUserId},
			success :
				function( data ){
					fnClose(data.rows);

			}
		});

	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

		fnKendoDropDownList({
			id : 'csSearchSelect',
			data : [ {
				"CODE" : "SEARCH_TYPE.NAME",
				"NAME" : "회원명"
			}, {
				"CODE" : "SEARCH_TYPE.ID",
				"NAME" : "회원ID"
			},
			{
				"CODE" : "SEARCH_TYPE.MOBILE",
				"NAME" : "휴대폰번호"
			},
			{
				"CODE" : "SEARCH_TYPE.MAIL",
				"NAME" : "이메일"
			}
			],
			chkVal : 'SEARCH_TYPE.NAME'
		});


		//회원구분
		fnTagMkRadio({
			id    :  'csSearchBuyerType',
			tagId : 'csSearchBuyerType',
			data  : [
						{ "CODE" : "BUYER_TYPE.BUYER"	, "NAME":'회원' },
						{ "CODE" : "BUYER_TYPE.MOVE"	, "NAME":'휴면회원' }
					],
			chkVal: 'BUYER_TYPE.BUYER',
			style : {}
		});

	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	function inputFocus(){
		$('#input1').focus();
	};
		function condiFocus(){
		$('#condition1').focus();
	};

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};

	$scope.fnPopupButton = function(data){ fnPopupButton(data); };
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
