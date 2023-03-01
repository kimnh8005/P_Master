/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 주문 상세 > 수취정보 변경내역
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.18		김승우          최초생성
 * @ 2021.02.09		최윤지			추가작성
 * **/
"use strict";

//var PAGE_SIZE = 20;
var orderShippingZoneGridDs, orderShippingZoneGridOpt, orderShippingZoneGrid;
var orderShippingZoneHistGridDs, orderShippingZoneHistGridOpt, orderShippingZoneHistGrid;
//var pageParam = fnGetPageParam();
var paramData = parent.POP_PARAM['parameter'];

$(document).ready(function() {

	fnInitialize();

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({
			PG_ID  : "receiverHistPopup",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){

		fnInitGrid();

	};

	//--------------------------------- Button Start---------------------------------
	// 버튼 초기화
	function fnInitButton(){
		$("#fnSearch,  #fnClear").kendoButton();
	};
	//--------------------------------- Button End-----------------------------------

	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid(){
		initOrderShippingZoneGrid();
		initOrderShippingZoneHistGrid();
	};

	//수취정보
	function initOrderShippingZoneGrid() {
	  orderShippingZoneGridDs = fnGetDataSource({
			url      : "/admin/order/getOrderShippingZoneByOdShippingZoneId?odShippingZoneId="+paramData.odShippingZoneId
		});

	  orderShippingZoneGridOpt = {
				dataSource: orderShippingZoneGridDs,
				//navigatable : true,
				//scrollable : true,
				columns : [
							{ field : "odShippingZoneId", title : "배송번호"	, width: "50px", attributes : {style : "text-align: center"}},
							{ field : "deliveryType", title : "배송방법"	, width: "50px", attributes : {style : "text-align: center"}},
							{ field : "recvNm"	, title : "받는분", width: "80px", attributes : {style : "text-align: center"}},
							{ field : "recvHp"	, title : "휴대폰", width: "120px", attributes : {style : "text-align: center"},
								template: function (row) {
											let str = "";
											let recvHp = row.recvHp.replace(/\-/g,'') == "" ? "01000000000" : row.recvHp.replace(/\-/g,''); // 휴대폰번호 하이픈(-) 제거;
											if(recvHp.length < 12) {
												str = fnPhoneNumberHyphen(recvHp);
											} else if(12 <= recvHp.length){
												str = recvHp.slice(0,4) + '-' + recvHp.slice(4,(recvHp.length)-4) + '-' + recvHp.slice((recvHp.length)-4,recvHp.length);
											}
											return str;
								}

							},
							{ field : "recvZipCd", title : "우편번호"	, width: "70px", attributes : {style : "text-align: center"}},
							{ field : "recvAddr1", title : "주소1"		, width: "230px", attributes : {style : "text-align: center"}},
							{ field : "recvAddr2", title : "주소2"		, width: "210px", attributes : {style : "text-align: center"}},
							{ field : "deliveryMsg", title : "배송요청사항"	, width: "100px", attributes : {style : "text-align: center"},
								template : function(row){
									let str = "-";
									if (row.deliveryMsg) {
										str = row.deliveryMsg;
									}
									return str;
								}
							},
							{ field : "doorMsgCdName"			, title : "배송출입정보"			, width: "70px"		, attributes : {style : "text-align:center;"}
								, template : function(dataItem){
									let returnVal;
									if(!dataItem.doorMsgCd){
										returnVal = "-";
									}else{
										if(dataItem.doorMsgCd == "ACCESS_INFORMATION.FRONT_DOOR_PASSWORD"){ // 공동현관 비밀번호 입력 -> 비밀번호도 출력
											returnVal = dataItem.doorMsgCdName + "<br/>" + dataItem.doorMsg;
										}
										else if(dataItem.doorMsgCd == "ACCESS_INFORMATION.ETC"){ // 기타/직접입력
											returnVal = dataItem.doorMsg;
										} else{ // 그외
											returnVal = dataItem.doorMsgCdName;
										}
									}
									return returnVal;
								}
							},
							  { field : "createDt"			, title : "등록일자"				, width: "50px"		, attributes : {style : "text-align:center;"}}
							, { field : "deliveryAreaNm"		, title : "권역정보"				, width: "50px"		, attributes : {style : "text-align:center;"}}
							, { field : "deliveryDt"			, title : "적용일자"				, width: "50px"		, attributes : {style : "text-align:center;"}}
							, { field : "recvBldNo"			, hidden : true}
						]
			};

	  orderShippingZoneGrid = $('#orderShippingZoneGrid').initializeKendoGrid( orderShippingZoneGridOpt ).cKendoGrid();

	  orderShippingZoneGrid.bind("dataBound", function(e){
			 //Hide show columns
		    var columns = e.sender._data[0];
		    var grid = $('#orderShippingZoneGrid').data('kendoGrid');

		    if(columns.deliveryType != '일일배송'){ // 일일배송인경우 권역정보, 적용일자 출력
		       	grid.hideColumn(10);
		       	grid.hideColumn(11);
		       	$('#orderShippingZoneGrid').find('table').css('min-width','100%');
	       }

		});

	  orderShippingZoneGridDs.query();
	}

	//변경 정보
	function initOrderShippingZoneHistGrid() {
		orderShippingZoneHistGridDs = fnGetDataSource({
			url      : "/admin/order/getOrderShippingZoneHistList?odShippingZoneId="+paramData.odShippingZoneId +"&odOrderDetlId="+paramData.odOrderDetlId
		});

		orderShippingZoneHistGridOpt = {
				dataSource: orderShippingZoneHistGridDs,
				navigatable : true,
				height : 500,
				scrollable : true,
				columns : [
							{ field : "odShippingZoneId", title : "배송번호"	, width: "50px", attributes : {style : "text-align: center"}},
							{ field : "deliveryType", title : "배송방법"	, width: "50px", attributes : {style : "text-align: center"}},
							{ field : "recvNm"	, title : "받는분",  width: "80px", attributes : {style : "text-align: center"}},
							{ field : "recvHp"	, title : "휴대폰",  width: "120px", attributes : {style : "text-align: center"},
								template: function (row) {
											let str = "";
											let recvHp = row.recvHp.replace(/\-/g,'') == "" ? "01000000000" : row.recvHp.replace(/\-/g,''); // 휴대폰번호 하이픈(-) 제거;
											if(recvHp.length < 12) {
												str = fnPhoneNumberHyphen(recvHp);
											} else if(12 <= recvHp.length){
												str = recvHp.slice(0,4) + '-' + recvHp.slice(4,(recvHp.length)-4) + '-' + recvHp.slice((recvHp.length)-4,recvHp.length);
											}
											return str;
								}

							},
							{ field : "recvZipCd", title : "우편번호"	, width: "70px", attributes : {style : "text-align: center"}},
							{ field : "recvAddr1", title : "주소1"		, width: "230px", attributes : {style : "text-align: center"}},
							{ field : "recvAddr2", title : "주소2"		, width: "210px", attributes : {style : "text-align: center"}},
							{ field : "deliveryMsg", title : "배송요청사항"	, width: "100px", attributes : {style : "text-align: center"},
								template : function(row){
									let str = "-";
									if (row.deliveryMsg) {
										str = row.deliveryMsg;
									}
									return str;
								}
							},
							{ field : "doorMsgCdName"			, title : "배송출입정보"			, width: "70px"		, attributes : {style : "text-align:center;"}
								, template : function(dataItem){
									let returnVal;
									if(!dataItem.doorMsgCd){
										returnVal = "-";
									}else{
										if(dataItem.doorMsgCd == "ACCESS_INFORMATION.FRONT_DOOR_PASSWORD"){ // 공동현관 비밀번호 입력 -> 비밀번호도 출력
											returnVal = dataItem.doorMsgCdName + "<br/>" + dataItem.doorMsg;
										}
										else if(dataItem.doorMsgCd == "ACCESS_INFORMATION.ETC"){ // 기타/직접입력
											returnVal = dataItem.doorMsg;
										} else{ // 그외
											returnVal = dataItem.doorMsgCdName;
										}
									}
									return returnVal;
								}
							},
							{ field : "createDt"			, title : "등록일자"				, width: "50px"		, attributes : {style : "text-align:center;"}}
							, { field : "deliveryAreaNm"		, title : "권역정보"				, width: "50px"		, attributes : {style : "text-align:center;"}}
							, { field : "recvBldNo"			, hidden : true}
						]
			};

	 orderShippingZoneHistGrid = $('#orderShippingZoneHistGrid').initializeKendoGrid( orderShippingZoneHistGridOpt ).cKendoGrid();

	 orderShippingZoneHistGrid.bind("dataBound", function(e){
			 //Hide show columns
		    var columns = e.sender._data[0];
		    var grid = $('#orderShippingZoneHistGrid').data('kendoGrid');

		    if(columns.deliveryType != '일일배송'){ // 일일배송인경우 권역정보, 적용일자 출력
		       	grid.hideColumn(10);
		       	grid.hideColumn(11);
		       	$('#orderShippingZoneHistGrid').find('table').css('min-width','100%');
	       }

		});

	 orderShippingZoneHistGridDs.query();
	}
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){};

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ----------------------------------

    // 기본값 설정
    function fnDefaultSet(){};

    function fnSearch() {
      fnSearchAGrid();
      fnSearchBGrid();
    }

    function fnClear() {}

    function fnSearchAGrid() {
      var data = {"categoryType":""}; //url 호출 위해 임시

      var query = {
          page: 1,
          pageSize: 1,
          filterLength: fnSearchData(data).length,
          filter: {
              filters: fnSearchData(data)
          }
      };

      aGridDs.query(query);
    }

    function fnSearchBGrid() {
      var data = {"categoryType":""}; //url 호출 위해 임시

      var query = {
          page: 1,
          pageSize: PAGE_SIZE,
          filterLength: fnSearchData(data).length,
          filter: {
              filters: fnSearchData(data)
          }
      };

      bGridDs.query(query);
    }


	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};

	/** Common Clear*/
	$scope.fnClear =function(){	fnClear(); };

	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	// fnInputValidationForAlphabetNumberLineBreakComma("code");

	//------------------------------- Validation End -------------------------------------

}); // document ready - END

