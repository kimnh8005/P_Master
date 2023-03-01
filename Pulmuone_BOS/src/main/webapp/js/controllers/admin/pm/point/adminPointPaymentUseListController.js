/**-----------------------------------------------------------------------------
 * description 		 : 관리자 적립금 지급/차감 내역
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.10.12		안치열          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//관리자 별 설정 라디오버튼 제어
/*	$("input[name=settingType]").change(function() {
		var chkValue = $(this).val();
		if(chkValue == 'GROUP'){
			$("#amountIndividual").attr("disabled", true);
		}else{
			$("#amountIndividual").attr("disabled", false);
		}
	});*/

	$('#createInfoDiv').hide();

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'adminPointPaymentUseList',
			callback : fnUI
		});

		$('#CLICKED_MENU_ID').val( CLICKED_MENU_ID );

	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();

	//	fnInitSetting();

		fnSearch();

		setDefaultDatePicker();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnClear, #fnExcel, #fnGrantAuthEmployeeSearch').kendoButton();
	}


	function fnSearch(){

		if($('#condiValue').val() != ""){
			if($('#grantAuthEmployeeNumber').val() != "" || $('#roleGroup').val() != ""
				||$('#searchPointType').val() || $('#pointUsedMsg').val()){
				return valueCheck("", "아이디 또는 회원명 검색 시 다른 조회항목과 함께 조회할 수 없습니다.", 'condiType');
			}
		}


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
	}


	function numberFormat(inputNumber) {
	   return inputNumber.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}

	function fnClear() {
		$('#searchForm').formClear(true);
		//$('#aGrid').gridClear(true);
		setDefaultDatePicker()
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


	 function fnGrantAuthEmployeeSearch(){ // 담당자 검색 팝업

         fnKendoPopup({
             id     : "grantAuthEmployeeSearchPopup",
             title  : "담당자 검색",
             src    : "#/grantAuthEmployeeSearchPopup",
             param  : {},
             width  : "700px",
             height : "600px",
             success: function( id, data ){

                 if( data.employeeNumber != undefined ){
                	 $('#grantAuthEmployeeNumber').val(data.employeeNumber);
                	 $('#grantAuthEmployeeName').val(data.employeeName);
                 }else{
                	 $('#grantAuthEmployeeNumber').val('');
                	 $('#grantAuthEmployeeName').val('');
                 }
             }
         });
     }

	function fnExcel() {
		var data = $('#searchForm').formSerialize(true);
		fnExcelDownload('/admin/promotion/adminPointPaymentUse/adminPointPaymentUseListExportExcel', data);
	}




	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url		: '/admin/promotion/adminPointPaymentUse/getAdminPointPaymentUseList',
			pageSize: PAGE_SIZE,
			requestEnd: function(e) {
                 //$('#countTotalSpan').text(kendo.toString(e.response.total, "n0"));
             }
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 5
			}
			,navigatable: true
			,scrollable: true
//			,height:400
			,columns   : [
				{ field:'no'		,title : 'No'				, width:'50px',attributes:{ style:'text-align:center' }, template:"<span class='row-number'></span>"}
				,{ field:'pmPointId'		,hidden:true}
				,{ field:'pointDetailType'		,hidden:true}
				,{ field:'pointDetailTypeName'		,title : '상세구분'				, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'erpOrganizationName'		,title : '조직명'				, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'adminUserName'		,title : '처리자명'				, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'adminUserId'		,title : '처리자ID'				, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'urUserName'		,title : '회원명'				, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'urUserId'		,title : '회원ID'				, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'paymentTypeName'		,title : '구분'				, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'issueVal'		,title : '내역'				, width:'150px',attributes:{ style:'text-align:center' },
					template : function(dataItem){
                	    let returnValue;
                	    if(dataItem.paymentType == 'POINT_PAYMENT_TP.PROVISION'){
                	    	returnValue = dataItem.issueValue + "원(" + dataItem.validityDay + "일)";
                	    }else{
                	    	returnValue = dataItem.issueValue + '원';
                	    }
                        return returnValue;
			          }
				}
				,{ field:'pointUseDate'		,title : '지급/차감 일자'				, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'pointUsedMsg'		,title : '비고'				, width:'150px',attributes:{ style:'text-align:center' }}
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

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

		// 역할그룹 조회
		fnKendoDropDownList({
			id    : 'roleGroup',
			url : "/admin/promotion/adminPointSetting/getRoleGroupList",
			params : {},
			textField :"roleName",
			valueField : "erpOrganizationCd",
			blank : "선택해주세요"
		});

    	//================ 상세구분 ===============
    	fnKendoDropDownList({
    			id  : 'searchPointType',
    	        url : "/admin/comn/getCodeList",
    	        params : {"stCommonCodeMasterCode" : "POINT_ADMIN_ISSUE_TP", "useYn" :"Y"},
    	        textField :"NAME",
    			valueField : "CODE",
    			blank : "전체"
    	});

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



	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear = function( ){	fnClear();};
	/** Common Excel*/
	$scope.fnExcel = function(){	 fnExcel();};

	$scope.fnGrantAuthEmployeeSearch = function(){	 fnGrantAuthEmployeeSearch();};

	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//마스터코드값 입력제한 - 숫자 & -
	/*fnInputValidationByRegexp("amount", /[^0-9]/g);
	fnInputValidationByRegexp("validityDay", /[^0-9]/g);
	fnInputValidationByRegexp("amountIndividual", /[^0-9]/g);*/

}); // document ready - END
