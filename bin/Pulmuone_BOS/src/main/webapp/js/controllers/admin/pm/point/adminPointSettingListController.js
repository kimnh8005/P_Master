/**-----------------------------------------------------------------------------
 * description 		 : 관리자 지급 한도 설정 이력 조회 팝업
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.10.06		안치열          최초생성
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
			PG_ID  : 'adminPointSettingList',
			callback : fnUI
		});

		$('#CLICKED_MENU_ID').val( CLICKED_MENU_ID );

	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();

		fnInitSetting();

		fnSearch();

	}

	//--------------------------------- Button Start---------------------------------

	//-- 초기화 버튼 -----------------
	function fnClear() {
		$('#searchForm').formClear(true);

		$(".date-controller button").each(function() {
			$(this).attr("fb-btn-active", false);
		})
		fnInitSetting();
	}

	function fnInitButton(){
		$('#fnSearch, #fnGrantAuthEmployeeSearch, #fnClear').kendoButton();
	}

	function fnInitSetting(){
		$("#amountIndividual").attr("disabled", true);

		$(".date-controller button").each(function() {
			$(this).attr("fb-btn-active", false);
		})
		$("button[data-id='fnDateBtn3']").attr("fb-btn-active", true);

		var today = fnGetToday();

		$("#startCreateDate").data("kendoDatePicker").value(fnGetDayMinus(today, 6));
		$("#endCreateDate").data("kendoDatePicker").value(today);
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
		aGridDs.query(query);
	}

	function numberFormat(inputNumber) {
	   return inputNumber.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}


	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url		: '/admin/promotion/adminPointSetting/getAdminPointSettingList'
			, pageSize : PAGE_SIZE
            , requestEnd: function(e) {
            }
		});
		aGridOpt = {
			dataSource: aGridDs
			, pageable  : {
	               pageSizes   : [20, 30, 50],
	               buttonCount : 10
	             }
	        , navigatable: true
	        , scrollable : true
			,columns   : [
				{ field:'pmPointAdminSettingId'		,hidden:true}
				,{ field:'no'		,title : 'No'				, width:'70px',attributes:{ style:'text-align:center' }, template:"<span class='row-number'></span>"}
				,{ field:'adminName'		,title : '관리자'				, width:'150px',attributes:{ style:'text-align:center' },
					template : function(dataItem){
                	    let returnValue;
                        returnValue = dataItem.adminName + '<br>' + '(' + dataItem.loginId + ') ';
                        return returnValue;
			          }
				}
				,{ field:'roleName'		,title : '역할그룹'			, width:'150px',attributes:{ style:'text-align:left' }}
				,{ field:'amount'		,title : '적립금 설정/기본 유효기간 설정'	, width:'250px',attributes:{ style:'text-align:left' },
					template : function(dataItem){
                	    let returnValue;
                        returnValue = numberFormat(dataItem.amount) + '원 / 지급일로 부터 ' + dataItem.validityDay + '일 ';
                        return returnValue;
			          }
				}
				,{ field:'amountIndividual'		,title : '관리자별 설정'	, width:'150px',attributes:{ style:'text-align:center' },
					template : function(dataItem){
                	    let returnValue;
                	    if(dataItem.settingType == 'GROUP'){
                	    	returnValue = '제한없음';
                	    }else{
                	    	returnValue = '관리자별 : ' + numberFormat(dataItem.amountIndividual) + ' 원';
                	    }
                        return returnValue;
			          }
				}
				,{ field:'createInfo'		,title : '수정일자'	, width:'150px',attributes:{ style:'text-align:left' }}
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


	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

		// 역할그룹 조회
		fnKendoDropDownList({
			id    : 'roleGroup',
			url : "/admin/promotion/adminPointSetting/getRoleGroupList",
			params : {},
			textField :"roleName",
			valueField : "stRoleTpId",
			blank : "전체"
		});

		//================데이트 피커===============
        fnKendoDatePicker({
			id    : 'startCreateDate',
			format: 'yyyy-MM-dd',
			defVal: fnGetDayMinus(fnGetToday(),6),
            defType : 'oneWeek'
		});
		fnKendoDatePicker({
			id    : 'endCreateDate',
			format: 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startCreateDate',
			btnEndId : 'endCreateDate',
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
	$scope.fnClear = function( ) {	fnClear();	};


	$scope.fnGrantAuthEmployeeSearch = function( ) {	fnGrantAuthEmployeeSearch();	};



	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
