﻿/**-----------------------------------------------------------------------------
 * description 		 : 재고 기한 이력관리 팝업
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.09		박영후          최초생성
 * @ 2020.08.10		강윤경          수정
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
			PG_ID  : 'stockDeadlineHist',
			callback : fnUI
		});

	}

	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitMaskTextBox();  // Initialize Input Mask ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnClear').kendoButton();
	}
	//조회
	function fnSearch(){
		if( $('#startDate').val() == "" && $('#endDate').val() != ""){
			fnKendoMessage({ message : "시작일 또는 종료일을 입력해주세요.."});
			return;
		}
		if( $('#startDate').val() != "" && $('#endDate').val() == ""){
			fnKendoMessage({ message : "시작일 또는 종료일을 입력해주세요."});
			return;
		}

		if( $('#startDate').val() > $('#endDate').val()){
			fnKendoMessage({ message : "시작일을 종료일보다 뒤로 설정할 수 없습니다."});
			return;
		}

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
	//초기화
	function fnClear(){
		$('#searchForm').formClear(true);

		setDefault();

		var warehouseDropdown = $("#warehouse").data("kendoDropDownList");
		warehouseDropdown.dataSource.data([]);
		warehouseDropdown.enable(false);
	}
	//닫기
	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}
	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){

		aGridDs = fnGetPagingDataSource({
			url      : '/admin/il/stock/getStockDeadlineHistList',
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 10
			}
			, navigatable: true
			, sellectable: false
			,columns   : [
				 { field:'no'				,title : 'No.'			, width:'50px'	,attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		,title : '등록일자'		, width:'100px'	,attributes:{ style:'text-align:center' }}
				,{ field:'compName'			,title : '공급<br/>업체'	, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'warehouseName'	,title : '출고처'			, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'histTp'			,title : '타입'			, width:'50px'	,attributes:{ style:'text-align:center' }, template: "#=(histTp=='HIST_TP.INSERT') ? '신규' : '수정'#"}
				,{ field:'distributionPeriod'		,title : '유통<br/>기간'	, width:'60px'	,attributes:{ style:'text-align:center' }}
				,{ field:'imminent'			,title : '임박<br/>기준'	, width:'80px'	,attributes:{ style:'text-align:center' }
					,template:  function(dataItem){
	                    return dataItem.imminent + "일" ;
					}
				}
				,{ field:'delivery'			,title : '출고<br/>기준'	, width:'80px'	,attributes:{ style:'text-align:center' }
					,template:  function(dataItem){
	                    return dataItem.delivery + "일" ;
					}
				}
				,{ field:'targetStockRatio'	,title : '목표<br/>재고'	, width:'80px'	,attributes:{ style:'text-align:center' }
					,template:  function(dataItem){
	                    return dataItem.targetStockRatio + "%" ;
					}
				}
				,{ field:'basicYn'	,title : '기본<br/>설정'	, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'createName'		,title : '관리자정보'		, width:'60px'	,attributes:{ style:'text-align:center' }
					,template:  function(dataItem){
                        return dataItem.createName + "(" + dataItem.loginId + ")" ;
					}
                }
			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
        aGrid.bind("dataBound", function() {
			//total count
            $('#countTotalSpan').text(aGridDs._total);
        });
	}

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitMaskTextBox() {
		$('#searchDistributionPeriod, #searchDistributionPeriodStart, #searchDistributionPeriodEnd, #searchTargetStockRatio, #searchDate').forbizMaskTextBox({fn:'onlyNum'});
	}

	function setDefault() {
		$("#searchForm input[type=radio][name=searchExpirePeriod]:eq(0)").trigger('click');
		setDefaultDatePicker();
	}

	// 데이트피커 컨트롤러 초기화 함수
	function setDefaultDatePicker() {
		$(".date-controller button").each(function() {
			$(this).attr("fb-btn-active", false);
		})

		$("button[data-id='fnDateBtn3']").attr("fb-btn-active", true);

		var today = fnGetToday();

		$("#createDateStart").val(fnGetDayMinus(today, 1));
		$("#createDateEnd").val(fnGetDayMinus(today, 1));
	}

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		//공급업체
		fnKendoDropDownList({
			id    : 'searchUrSupplierId',
			url : "/admin/comn/getDropDownSupplierList",
			tagId : 'searchUrSupplierId',
			textField :"supplierName",
			valueField : "supplierId",
			blank : "전체"
		});

		//출고처
		fnKendoDropDownList({
			id    : 'searchUrWarehouseId',
			url : "/admin/comn/getDropDownWarehouseList",
			tagId : 'searchUrWarehouseId',
			chkVal: '',
			style : {},
			blank : "전체",
			textField :"warehouseName",
			valueField : "warehouseId",
		/*
		 공급업체랑 별개로 출고처 전체 노출 변경 적용 20201113
			autoBind : false,
			cscdId     : 'searchUrSupplierId',
			cscdField  : 'supplierId'
			*/
		});


		//검색 타입
        fnKendoDropDownList({
            id : 'searchType', // ERP 품목 연동 여부
            data : [ {
                code : 'imminent',
                name : '임박기준'
            }, {
                code : 'delivery',
                name : '출고기준'
            } ],
            valueField : 'code',
            textField : 'name'
        });

        //유통기간
		$("#searchForm input[type=radio][name=searchExpirePeriod]:eq(0)").on("click", function() {
			$("#searchDistributionPeriod").prop("disabled", false);
			$("#searchDistributionPeriodStart, #searchDistributionPeriodEnd").prop("disabled", true);
			$("#searchDistributionPeriodStart, #searchDistributionPeriodEnd").val("");
		});
		$("#searchForm input[type=radio][name=searchExpirePeriod]:eq(1)").on("click", function() {
			$("#searchDistributionPeriod").prop("disabled", true);
			$("#searchDistributionPeriodStart, #searchDistributionPeriodEnd").prop("disabled", false);
			$("#searchDistributionPeriod").val("");
		});

		//달력
		fnKendoDatePicker({
			id    : 'startDate',
			format: 'yyyy-MM-dd',
			defVal : fnGetDayMinus(fnGetToday(),6),
			defType : 'oneWeek'
		});
		fnKendoDatePicker({
			id    : 'endDate',
			format: 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startDate',
			btnEndId : 'endDate',
			defVal : fnGetToday(),
			defType : 'oneWeek',
			change: function(e){
	        	   if ($('#startDate').val() == "" ) {
	                   return valueCheck("6495", "시작일을 선택해주세요.", 'endDate');
	               }
				}
		});

		function onSupplierCompany(e) {
			var targetId = "searchUrWarehouseId";
			var sourceId = $(e.sender.element).attr("id");

			var warehouseDropDown = $("#" + targetId).data("kendoDropDownList");

			if (e.sender.value() != "") {
				warehouseDropDown.enable(true);

	        	var dropDownUrl = warehouseDropDown.dataSource.options.transport.read.url;

	        	fnAjax({
	        		url     : dropDownUrl,
	        		method	: 'GET',
	        		params  : {"supplierId": e.sender.value()},
	        		success :
	        			function( data ){
	        			warehouseDropDown.dataSource.data(data.rows);
	        			},
	        		isAction : 'batch'
	        	});
	        }
	        else {
	        	warehouseDropDown.dataSource.data([]);
	        	warehouseDropDown.enable(false);
	        }
		}



	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){

	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
