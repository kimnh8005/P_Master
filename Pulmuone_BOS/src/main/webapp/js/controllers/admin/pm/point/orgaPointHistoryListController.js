/**-----------------------------------------------------------------------------
 * description 		 : 올가 이관 적립금 내역
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.06.02		남기승          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var detailGridDs, detailGridOpt, detailGrid;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'orgaPointHistoryList',
			callback : fnUI
		});
	}

	function fnUI(){
		fnInitButton();	//Initialize Button  ---------------------------------
		fnInitGrid();	//Initialize Grid ------------------------------------
		fnInitOptionBox();
//		fnPointDetailHistoryInit();
//		fnSearch();
		setDefaultDatePicker();
	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnClear, #fnExcel').kendoButton();
	}

	function fnSearch(){

		var data;
		data = $('#searchForm').formSerialize(true);
		var query = {
                page         : 1
              , pageSize     : PAGE_SIZE
              , filterLength : fnSearchData(data).length
              , filter :  {
                    filters : fnSearchData(data)
                }
		};
		aGridDs.query( query );

		fnGetTotalSumValue();
	}

	function numberFormat(inputNumber) {
	   return inputNumber.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}

	function fnClear() {
		$('#searchForm').formClear(true);
		//$('#aGrid').gridClear(true);
		setDefaultDatePicker();
	}

	function setDefaultDatePicker() {
		$(".date-controller button").each(function() {
			$(this).attr("fb-btn-active", false);
		})

		$("button[data-id='fnDateBtn3']").attr("fb-btn-active", true);

		var today = fnGetToday();

		$("#startDate").data("kendoDatePicker").value(fnGetDayMinus(today, 6));
		$("#endDate").data("kendoDatePicker").value(today);
	}

    function valueCheck(key, nullMsg, ID) {
        fnKendoMessage({
            message: fnGetLangData({ key: key, nullMsg: nullMsg }),
            ok: function focusValue() {
                $('#' + ID).focus();
            }
        });

        return false;
    }


	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------



	function fnInitGrid(){


		aGridDs = fnGetPagingDataSource({
			url		: '/admin/promotion/pointHistory/getOrgaPointHistoryList',
			pageSize: PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			, pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 5
			}
			, navigatable: true
			, scrollable: true
			, columns : [
				{ field:'no'					, title : 'No'			, width:'50px', attributes:{ style:'text-align:center' }, template:"<span class='row-number'></span>"}
				, { field:'pmPointUsedId'		, hidden:true}
				, { field:'urUserName'			, title : '회원명'		, width:'150px', attributes:{ style:'text-align:center' }}
				, { field:'urUserId'			, title : '회원ID'		, width:'150px', attributes:{ style:'text-align:center' }}
				, { field:'paymentTypeName'		, title : '구분'			, width:'150px', attributes:{ style:'text-align:center' }}
				, { field:'orgaMemberNo'		, title : '올가 회원번호'	, width:'150px', attributes:{ style:'text-align:center' }}
				, { field:'pointTypeName'		, title : '적립금 설정'	, width:'150px', attributes:{ style:'text-align:center' }}
				, { field:'amount'				, title : '내역'			, width:'150px', attributes:{ style:'text-align:center' },
					template : function(dataItem){
						let returnValue;
						if(dataItem.paymentType == 'POINT_PAYMENT_TP.PROVISION'){
							if(dataItem.validityDay == ''){
								returnValue = numberFormat(dataItem.amount) + '원';
							} else {
								returnValue = numberFormat(dataItem.amount) + "원 (" + dataItem.validityDay + "일)";
							}
						} else {
							returnValue = numberFormat(dataItem.amount) + '원';
						}
						return returnValue;
					}
				}
				, { field:'pointDetailType'		, hidden:true}
				, { field:'pointDetailTypeName'	, title : '상세구분'		, width:'150px', attributes:{ style:'text-align:center' }}
				, { field:'createDate'			, title : '지급/차감 일자'	, width:'150px', attributes:{ style:'text-align:center' }}
				, { field:'calculateDay'		, title : '유효기간'		, width:'150px', attributes:{ style:'text-align:center' },
					template : function(dataItem) {
						return dataItem.expirationDt;
					}
				}
				, { field:'pointUsedMsg'		, title : '사유'			, width:'150px', attributes:{ style:'text-align:center' }}
			]
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		aGrid.bind("dataBound", function(){
			var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(row_num);
				row_num--;
			});

	        $('#totalCnt').text(aGridDs._total);
		});
	}

	function fnGetTotalSumValue(){

		var data = $('#searchForm').formSerialize(true);
		fnAjax({
			url     : '/admin/promotion/pointHistory/getTotalOrgaPointHistory',
			params  : data,
			success :
				function( data ){

					if(data.totalIssuePoint != null){
						$('#totalIssuePoint').val(numberFormat(data.totalIssuePoint) + '원');
					}else{
						$('#totalIssuePoint').val('');
					}
					if(data.totalUsePoint != null){
						$('#totalUsePoint').val(numberFormat(data.totalUsePoint) + '원');
					}else{
						$('#totalUsePoint').val('');
					}
					if(data.totalExpirationPoint != null){
						$('#totalExpirationPoint').val(numberFormat(data.totalExpirationPoint) + '원');
					}else{
						$('#totalExpirationPoint').val('');
					}
					if(data.totalMonthExpirationPoint != null){
						$('#totalMonthExpirationPoint').val(numberFormat(data.totalMonthExpirationPoint) + '원');
					}else{
						$('#totalMonthExpirationPoint').val('');
					}
				},
			isAction : 'select'
		});
	}

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

    	//================ 상세구분 ===============
    	fnKendoDropDownList({
    			id  : 'searchPointDetailType',
    	        data : [ { "CODE" : "POINT_PROCESS_TP.DPOT"	, "NAME":"적립 올가 포인트 이전" }],
    	        textField :"NAME",
    			valueField : "CODE"
    	});
		$("#searchPointDetailType").attr("disabled", true);
    	//================ 구분 ===============
		fnTagMkRadio({
			id    :  'searchPaymentType',
			tagId : 'searchPaymentType',
			chkVal: 'POINT_PAYMENT_TP.PROVISION',			// 선택값
			data  : [   { "CODE" : ""	, "NAME":'전체'},
						{ "CODE" : "POINT_PAYMENT_TP.PROVISION"	, "NAME":'지급' },
						{ "CODE" : "POINT_PAYMENT_TP.DEDUCTION"	, "NAME":'차감' }
			]
		});

		// 구분 항목 전체, 차감 비활성화 처리
		$("input:radio[name='searchPaymentType']:radio[value='']").prop('disabled', true); // 선택하기
		$("input:radio[name='searchPaymentType']:radio[value='POINT_PAYMENT_TP.DEDUCTION']").prop('disabled', true); // 선택하기

		//================데이트 피커===============
        fnKendoDatePicker({
			id    : 'startDate',
			format: 'yyyy-MM-dd',
			defVal: fnGetDayMinus(fnGetToday(),6),
            defType : 'oneWeek'
		});

		fnKendoDatePicker({
			id    : 'endDate',
			format: 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startDate',
			btnEndId : 'endDate',
			defVal: fnGetToday(),
            defType : 'oneWeek'
		});
	}


	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------
	// 엑셀 다운로드
    function fnExcel(){
    	var data = $('#searchForm').formSerialize(true);
		fnExcelDownload('/admin/promotion/pointHistory/getOrgaPointHistoryListExportExcel', data);

    }
	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear = function( ){	fnClear();};
	/** Common Excel*/
	$scope.fnExcel = function(){	 fnExcel();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
