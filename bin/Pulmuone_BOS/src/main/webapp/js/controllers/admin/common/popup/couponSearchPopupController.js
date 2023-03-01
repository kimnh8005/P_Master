﻿/**-----------------------------------------------------------------------------
 * description 		 : 쿠폰목록 조회 팝업
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.19		안치열          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var rejectPmCouponId;
$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'couponSearchPopup',
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
		$('#fnSearch, #fnClear, #fnSave').kendoButton();

	}
	function fnSearch(){
		$('#inputForm').formClear(true);
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
			url      : '/admin/comn/popup/getCouponList',
			pageSize : PAGE_SIZE
		});

		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [5, 20, 30, 50],
				buttonCount : 5
			}
			,navigatable: true
			,scrollable: true
			,columns   : [
				 { field : "chk", headerTemplate : '<input type="checkbox" id="checkBoxAll" />',
                         template : '<input type="checkbox" name="couponGridChk" class="k-checkbox" />',
                         width : "60px", attributes : {style : "text-align:center;"}
                       }
				,{ field:'no'			,title : 'No'		, width:'70px',attributes:{ style:'text-align:center' },template:"<span class='row-number'></span>"}
				,{ field:'couponTypeName'		,title : '종류(발급방법)'		, width:'130px',attributes:{ style:'text-align:center' }}
				,{ field:'displayCouponName'			,title : '전시쿠폰명'		, width:'200px',attributes:{ style:'text-align:left;text-decoration: underline;color:blue;' }}
				,{ field:'bosCouponName'	,title : '관리자 쿠폰명'	, width:'200px',attributes:{ style:'text-align:left;text-decoration: underline;color:blue;' }}
				,{ field:'issueDate'	,title : '발급기간'	, width:'220px',attributes:{ style:'text-align:center' }}
				,{ field:'validityDate'	,title : '유효기간'	, width:'220px',attributes:{ style:'text-align:center' }}
				,{ field:'discountTypeName'	,title : '할인방식'	, width:'100px',attributes:{ style:'text-align:center' }}
				,{ field:'discountValue'	,title : '할인상세'	, width:'200px',attributes:{ style:'text-align:center' }}
				,{ title:'관리', width: "150px", attributes:{ style:'text-align:center;'  , class:'forbiz-cell-readonly' }
					,command: [ { text: '선택'		, imageClass: "k-i-add", className: "f-grid-add k-margin5", iconClass: "k-icon",
								   click: function(e) {  e.preventDefault();
												            var tr = $(e.target).closest("tr");
												            var data = this.dataItem(tr);
												            fnSelectCoupon(data);}
								}
					]
				}
				,{ field:'pmCouponId'		, hidden:'true'}
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

        $("#checkBoxAll").on("click", function(index){
            if( $("#checkBoxAll").prop("checked") ){
                $("input[name=couponGridChk]").prop("checked", true);
            }else{
                $("input[name=couponGridChk]").prop("checked", false);
            }
        });

		$(aGrid.tbody).on("click", "td", function (e) {
			var row = $(this).closest("tr");
			var rowIdx = $("tr", aGrid.tbody).index(row);
			var colIdx = $("td", row).index(this);
			if(colIdx == 3 || colIdx == 4){
				fnGridClick(e);
			}
		});

	}

	function fnGridClick(e){
		var dataItem = aGrid.dataItem($(e.target).closest('tr'));
		var sData = $('#searchForm').formSerialize(true);

		fnKendoPopup({
			id     : 'cpnMgmPut',
			title  : '쿠폰등록/수정',fnGridClick
			src    : '#/cpnMgmPut',
			param  : {pmCouponId :dataItem.pmCouponId },
			width  : '1300px',
			height : '1400px',
			success: function( id, data ){
				fnSearch();
			}
		});
	}




	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

		fnTagMkRadio({
			id: "searchCouponType" ,
			tagId : 'searchCouponType',
			url : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "COUPON_TYPE", "useYn" :"Y"},
			async : false,
			beforeData : [
							{"CODE":"", "NAME":"전체"},
						],
			chkVal: '',
			style : {}
		});


		fnKendoDatePicker({
			id    : 'startCreateDate',
			format: 'yyyy-MM-dd'
		});
		fnKendoDatePicker({
			id    : 'endCreateDate',
			format: 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startCreateDate',
			btnEndId : 'endCreateDate',
		    change: function(e){
	    	   if ($('#startCreateDate').val() == "" ) {
	               return valueCheck("6495", "시작일을 선택해주세요.", 'endCreateDate');
	           }
			}
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


	//개별 쿠폰 선택 버튼 선택
	function fnSelectCoupon(data){

		fnClose(data);
	}


	//등록 버튼 클릭
	function fnSave(){

		let params = [];

        if( $("input[name=couponGridChk]:checked").length == 0 ){
            fnKendoMessage({ message : "선택된 쿠폰이 없습니다." });
            return;
        }

        let selectRows  = aGrid.tbody.find("input[name=couponGridChk]:checked").closest("tr");

        for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){
            let dataItem = aGrid.dataItem($(selectRows[i]));
            params[i] = dataItem;
        }

        parent.POP_PARAM = params;
        parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();

	}



	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};



	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
