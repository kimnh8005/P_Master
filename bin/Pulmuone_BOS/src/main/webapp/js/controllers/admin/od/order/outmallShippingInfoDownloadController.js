/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 배송관리 > 배송정보 내역 다운로드
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.20		천혜현          최초생성
 * @
 * **/
"use strict";

var PAGE_SIZE = 20;
var downloadGridDs, downloadGridOpt, downloadGrid;
var downloadTrackNumHistGridDs, downloadTrackNumHistGridOpt, downloadTrackNumHistGrid;
var pageParam = fnGetPageParam();

// 판매처그룹.직관리 (외부몰)
var sellersGroupDirectMng = "SELLERS_GROUP.DIRECT_MNG";
var sellersGroupVendor		= "SELLERS_GROUP.VENDOR";
$(document).ready(function() {
    importScript("/js/service/od/order/orderCommSearch.js", function (){
        fnInitialize();
    });

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : true });
		fnPageInfo({
			PG_ID  : "outmallShippingInfoDownload",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){
		fnInitButton();
		fnInitGrid();
        fnInitTrackNumHistGrid();
		fnInitOptionBox();
		fnBindEvents();
	};

	//--------------------------------- Button Start---------------------------------

	// 버튼 초기화
	function fnInitButton(){
		$("#fnExcelDownload, #fnGetDownloadHist, #fnSearch, #fnClear, #fnTrackNumHistSearch").kendoButton();
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});
	};

    // 초기화
    function fnClear() {
        //$("#searchForm").formClear(true);
        $("#downloadForm").formClear(true);
        $(".date-controller button").each(function() {
			$(this).attr("fb-btn-active", false);
		})
        $("#downloadForm").find('button[data-id="fnDateBtn3"]').attr("fb-btn-active", true);
        //$("#omSellersId").html("").hide();
        $("#popupOmSellersId").html("").hide();
    };

    // 조회
    function fnSearch(){
        let data = $("#downloadForm").formSerialize(true);

        let query = { page : 1,
                      pageSize : PAGE_SIZE,
                      filterLength : fnSearchData(data).length,
                      filter : { filters : fnSearchData(data) }
        };
        downloadGridDs.query(query);
    };

    // 송장등록이력 조회
    function fnTrackNumHistSearch(){

        let data = $("#searchTrackNumHistForm").formSerialize(true);

        let query = { page : 1,
            pageSize : PAGE_SIZE,
            filterLength : fnSearchData(data).length,
            filter : { filters : fnSearchData(data) }
        };
        downloadTrackNumHistGridDs.query(query);
    };

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid(){
		downloadGridDs = fnGetEditPagingDataSource({
            url      	  : "/admin/outmall/order/getOutMallShippingExceldownHist",
            pageSize 	  : PAGE_SIZE
        });

		downloadGridOpt = {
            dataSource : downloadGridDs,
            pageable : { pageSizes: [20, 30, 50], buttonCount : 10 },
            navigatable: true,
            columns   : [
			               { field: 'rowNumber'				, title: 'No'					, width: '30px'		, attributes : { style: 'text-align:center' }
			               			, template: function (dataItem){ return fnKendoGridPagenation(downloadGrid.dataSource,dataItem); }}
			            , { field : "sellersGroup"			, title : "판매처"				, width : "40px"	, attributes : {style : "text-align:center"}}
			            , { field : "period"				, title : "기간"					, width : "40px"	, attributes : {style : "text-align:center"}
						            , template : function (dataItem){
				            			return dataItem.periodType + '</br>' + dataItem.startDt + ' ~ '+ dataItem.endDt;
				            		}}
			            , { field : "warehouseNm"			, title : "출고처"				, width : "40px"	, attributes : {style : "text-align:center"}}
			            , { field : "outmallType"			, title : "수집몰"				, width : "40px"	, attributes : {style : "text-align:center"}}
			            , { field : "userInfo"				, title : "관리자"				, width : "40px"	, attributes : {style : "text-align:center"}}
			            , { field : "downloadDt"			, title : "다운로드일시"			, width : "40px"	, attributes : {style : "text-align:center"}}
            ],
        };

    	downloadGrid = $("#downloadGrid").initializeKendoGrid( downloadGridOpt ).cKendoGrid();
    	downloadGrid.bind("dataBound", function() {
            $('#totalCnt').text(downloadGridDs._total);
        });

	};

    // 송장등록이력 그리드 초기화
    function fnInitTrackNumHistGrid(){
        downloadTrackNumHistGridDs = fnGetEditPagingDataSource({
            url      	  : "/admin/outmall/order/getOutMallTrackingNumberHist",
            pageSize 	  : PAGE_SIZE
        });

        downloadTrackNumHistGridOpt = {
            dataSource : downloadTrackNumHistGridDs,
            pageable : { pageSizes: [20, 30, 50], buttonCount : 10 },
            navigatable: true,
            columns   : [
                { field : "sendEndDt"			, title : "송장등록일시"			, width : "40px"	, attributes : {style : "text-align:center"}}
                , { field : "shippingCompNm"	, title : "출고처"				, width : "40px"	, attributes : {style : "text-align:center"}}
                , { field : "updateCnt"			, title : "등록라인수"			, width : "40px"	, attributes : {style : "text-align:center"}}
            ],
        };

        downloadTrackNumHistGrid = $("#trackingNumberGrid").initializeKendoGrid( downloadTrackNumHistGridOpt ).cKendoGrid();
        downloadTrackNumHistGrid.bind("dataBound", function() {
            $('#pageTotalText').text(downloadTrackNumHistGridDs._total);
        });

    };

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------처------------------------

	// 옵션 초기화
	function fnInitOptionBox(){

		// --------------- 목록
		searchCommonUtil.getDropDownCommCd("searchSellersGroupCd", "NAME", "CODE", "판매처 그룹 전체", "SELLERS_GROUP", "Y", "OUTMALL", "", ""); // 판매처

		// 기간 검색
		fnKendoDropDownList({
			id  : 'searchPeriodType',
			data  : [
				{"CODE":"DI_DT"			,"NAME":"배송중 일자"},
				{"CODE":"SHIPPING_DT"	,"NAME":"출고예정일"},
				{"CODE":"DELIVERY_DT"	,"NAME":"도착예정일"}
			],
			valueField 	: "CODE",
			textField 	: "NAME"
		});

		// 송장등록이력 기간 검색
		fnKendoDropDownList({
			id  : 'searchTrackNumHistPeriodType',
			data  : [
				{"CODE":"DI_DT"			,"NAME":"배송중 일자"},
				{"CODE":"SHIPPING_DT"	,"NAME":"출고예정일"},
				{"CODE":"DELIVERY_DT"	,"NAME":"도착예정일"}
			],
			valueField 	: "CODE",
			textField 	: "NAME"
		});

        var hourList = new Array() ;

        for(var i = 0; i<=23; i++){
            // 객체 생성
            var data = new Object() ;

            data.CODE = (i < 10) ? "0" + i : i.toString();
            data.NAME = (i < 10) ? "0" + i : i.toString();

            // 리스트에 생성된 객체 삽입
            hourList.push(data) ;
        }

        // String 형태로 변환
        //var jsonHourData = JSON.stringify(hourList) ;
        //console.log(jsonHourData) ;

        // 시작 시각 검색
        fnKendoDropDownList({
            id  : 'searchStHour',
            data  : hourList,
            valueField 	: "CODE",
            textField 	: "NAME"
        });

        // 종료 시각 검색
        fnKendoDropDownList({
            id  : 'searchEdHour',
            data  : hourList,
            valueField 	: "CODE",
            textField 	: "NAME",
            value : "23"
        });

        // 송장등록이력 시작 시각 검색
        fnKendoDropDownList({
            id  : 'startHour',
            data  : hourList,
            valueField 	: "CODE",
            textField 	: "NAME"
        });

        // 송장등록이력 종료 시각 검색
        fnKendoDropDownList({
            id  : 'endHour',
            data  : hourList,
            valueField 	: "CODE",
            textField 	: "NAME",
            value : "23"
        });

        // 수집몰 구분
		fnKendoDropDownList({
			id  : 'outmallType',
			data  : [
				{"CODE":"E"		,"NAME":"이지어드민"},
				{"CODE":"S"		,"NAME":"사방넷"}
			],
			valueField 	: "CODE",
			textField 	: "NAME",
			blank 		: "선택"
		});

        // 이지어드민 구분
        fnKendoDropDownList({
            id  : 'ezadminType',
            data  : [
                {"CODE":"API"		,"NAME":"API"},
                {"CODE":"EXCEL"		,"NAME":"엑셀 업로드"}
            ],
            valueField 	: "CODE",
            textField 	: "NAME",
            blank 		: "전체"
        });

        var dropdownlist = $("#ezadminType").data("kendoDropDownList");
        dropdownlist.wrapper.hide();

		// 기간검색
        fnKendoDatePicker({
            id: "searchStartDate",
            format: "yyyy-MM-dd",
            btnStartId: "searchStartDate",
            btnEndId: "searchEndDate",
            defVal: fnGetDayAdd(fnGetToday(),-6),
            defType : 'oneWeek',
            change : function(e) {
                fnDateValidation(e, "start", "searchStartDate", "searchEndDate", -6);
                fnValidateCal(e)
            }
        });

        // 기간검색
        fnKendoDatePicker({
            id: "searchEndDate",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "searchStartDate",
            btnEndId: "searchEndDate",
            defVal: fnGetToday(),
            defType : 'oneWeek',
            minusCheck: true,
            nextDate: false,
            change : function(e) {
                fnDateValidation(e, "end", "searchStartDate", "searchEndDate");
                fnValidateCal(e)
            }
        });


        // 출고처그룹
        fnKendoDropDownList({
            id : "warehouseGroup",
            tagId : "warehouseGroup",
            url : "/admin/comn/getCodeList",
            params : { "stCommonCodeMasterCode" : "WAREHOUSE_GROUP", "useYn" : "Y" },
            blank : "출고처 그룹 전체",
            async : false
        }).bind("change", function(e){
        	fnWareHouseGroupChange("list");
        });

        // 출고처그룹 별 출고처
        fnKendoDropDownList({
            id : "warehouseId",
            tagId : "warehouseId",
            url : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
            textField :"warehouseName",
            valueField : "warehouseId",
            blank : "출고처 선택",
            params : { "warehouseGroupCode" : "" },
        });

        // 송장등록이력 출고처그룹
        fnKendoDropDownList({
            id : "trackNumHistWarehouseGroup",
            tagId : "trackNumHistWarehouseGroup",
            url : "/admin/comn/getCodeList",
            params : { "stCommonCodeMasterCode" : "WAREHOUSE_GROUP", "useYn" : "Y" },
            blank : "출고처 그룹 전체",
            async : false
        }).bind("change", function(e){
            fnTrackNumHistWareHouseGroupChange();
        });

        // 송장등록이력 출고처그룹 별 출고처
        fnKendoDropDownList({
            id : "trackNumHistWarehouseId",
            tagId : "trackNumHistWarehouseId",
            url : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
            textField :"warehouseName",
            valueField : "warehouseId",
            blank : "출고처 선택",
            params : { "warehouseGroupCode" : "" },
        });
        // --------------- 목록

        // --------------- 팝업
        searchCommonUtil.getDropDownCommCd("popupSellersGroupCd", "NAME", "CODE", "전체", "SELLERS_GROUP", "Y", "OUTMALL", "", ""); // 판매처

		// 수집몰 구분
		fnKendoDropDownList({
			id  : 'popupOutmallType',
			data  : [
				{"CODE":"E"		,"NAME":"이지어드민"},
				{"CODE":"S"		,"NAME":"사방넷"}
			],
			valueField 	: "CODE",
			textField 	: "NAME",
			blank 		: "전체"
		});

		// 기간검색
        fnKendoDatePicker({
            id: "popupSearchStartDate",
            format: "yyyy-MM-dd",
            btnStartId: "popupSearchStartDate",
            btnEndId: "popupSearchEndDate",
            defVal: fnGetDayAdd(fnGetToday(),-7),
            defType : 'oneWeek',
            change : function(e) {
                fnDateValidation(e, "start", "popupSearchStartDate", "popupSearchEndDate");
                fnValidateCal(e)
            }
        });

        // 기간검색
        fnKendoDatePicker({
            id: "popupSearchEndDate",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "popupSearchStartDate",
            btnEndId: "popupSearchEndDate",
            defVal: fnGetToday(),
            defType : 'oneWeek',
            minusCheck: true,
            nextDate: false,
            change : function(e) {
                fnDateValidation(e, "end", "popupSearchStartDate", "popupSearchEndDate");
                fnValidateCal(e)
            }
        });


        // 출고처그룹
        fnKendoDropDownList({
            id : "popupWarehouseGroup",
            tagId : "popupWarehouseGroup",
            url : "/admin/comn/getCodeList",
            params : { "stCommonCodeMasterCode" : "WAREHOUSE_GROUP", "useYn" : "Y" },
            blank : "출고처 그룹 전체",
            async : false
        }).bind("change", function(e){
        	fnWareHouseGroupChange("popup");
        });

        // 출고처그룹 별 출고처
        fnKendoDropDownList({
            id : "popupWarehouseId",
            tagId : "popupWarehouseId",
            url : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
            textField :"warehouseName",
            valueField : "warehouseId",
            blank : "출고처 선택",
            params : { "warehouseGroupCode" : "" },
        });
        // --------------- 팝업

        // 시작일
        fnKendoDatePicker({
            id: "startDate",
            format: "yyyy-MM-dd",
            btnStartId: "startDate",
            btnEndId: "endDate",
            defVal : fnGetDayAdd(fnGetToday(),-30),
            defType : 'oneMonth',
            change : function(e) {
                fnDateValidation(e, "start", "startDate", "endDate", -30);
                fnValidateCal(e)
            }
        });
        // 종료일
        fnKendoDatePicker({
            id: "endDate",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "startDate",
            btnEndId: "endDate",
            defVal : fnGetToday(),
            defType : 'oneMonth',
            minusCheck: true,
            nextDate: false,
            change : function(e) {
                fnDateValidation(e, "end", "startDate", "endDate");
                fnValidateCal(e)
            }
        });


        $("input[name=searchSellersGroupCd]").each(function() {
            $(this).bind("change", onPurchaseTargetType);
        });
        $("input[name=popupSellersGroupCd]").each(function() {
            $(this).bind("change", popOnPurchaseTargetType);
        });

        fbCheckboxChange();
	};

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------
    function fnWareHouseGroupChange(type) {
    	let warehouseGroup = $("#warehouseGroup").val();

    	if(type == "popup"){
    		warehouseGroup = $("#popupWarehouseGroup").val();
    	}

    	fnAjax({
            url     : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
            method : "GET",
            params : { "warehouseGroupCode" : warehouseGroup },

            success : function( data ){
                let warehouseId = $("#warehouseId").data("kendoDropDownList");
                if(type == "popup"){
                	warehouseId = $("#popupWarehouseId").data("kendoDropDownList");
            	}
                warehouseId.setDataSource(data.rows);
            },
            error : function(xhr, status, strError){
                fnKendoMessage({ message : xhr.responseText });
            },
            isAction : "select"
        });
    };

    function fnTrackNumHistWareHouseGroupChange() {
       	let warehouseGroup = $("#trackNumHistWarehouseGroup").val();

    	fnAjax({
            url     : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
            method : "GET",
            params : { "warehouseGroupCode" : warehouseGroup },

            success : function( data ){
                let warehouseId = $("#trackNumHistWarehouseId").data("kendoDropDownList");
                warehouseId.setDataSource(data.rows);
            },
            error : function(xhr, status, strError){
                fnKendoMessage({ message : xhr.responseText });
            },
            isAction : "select"
        });
    };

    // 엑셀 다운로드
    function fnOutmallShippingInfoDownload(btnObj){

    	var data;
        data = $('#searchForm').formSerialize(true);
        if(data.outmallType == ''){
        	fnKendoMessage({ message: '수집몰 구분을 선택해주세요.'});
        	return false;
        }else{
        	fnExcelDownload('/admin/outmall/order/getOutmallShippingInfoDownload', data, btnObj);
        }

    }

    // 다운로드 내역
    function fnGetDownloadHist(){
    	fnClear();
    	$("#downloadGrid").data("kendoGrid").dataSource.data([]);
    	fnKendoInputPoup({height: "auto", width: "1200px", title: {nullMsg: '다운로드 내역'}});

    }

    function fnBindEvents(){

        // 수집몰 구분 Change
        $('[name="outmallType"]').on('change', function(e) {

            var dropdownlist = $("#ezadminType").data("kendoDropDownList");
            dropdownlist.value(-1); // dropdownlist reset

            // 이지어드민일 경우 이지어드민 구분값 노출
            if($(this).val() == 'E'){
                dropdownlist.wrapper.show();
            }else{
                dropdownlist.wrapper.hide();
            }

        });
    }
	//-------------------------------  Common Function end -------------------------------
	//------------------------------- Html 버튼 바인딩  Start -------------------------------
    $scope.fnSearch = function() { fnSearch(); };
    $scope.fnClear = function() { fnClear(); };
    $scope.fnTrackNumHistSearch = function() { fnTrackNumHistSearch(); };
	$scope.fnOutmallShippingInfoDownload = function( btnObj) {	fnOutmallShippingInfoDownload(btnObj);	};
	$scope.fnGetDownloadHist = function() {	fnGetDownloadHist();	};


	//------------------------------- Html 버튼 바인딩  End -------------------------------
}); // document ready - END
