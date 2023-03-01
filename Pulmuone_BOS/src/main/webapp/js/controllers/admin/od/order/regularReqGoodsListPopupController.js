/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 수취 정보 변경
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.15		김승우          최초생성
 * @ 2021.02.02		최윤지			추가작성
 * **/
"use strict";

var paramData = parent.POP_PARAM['parameter'];
var odRegularReqId = paramData.odRegularReqId;
var ilGoodsIdList = paramData.ilGoodsIdList;
var inputMenuId = paramData.inputMenuId;
$(document).ready(function() {
//	importScript("/js/service/od/order/orderCommSearch.js");
//	importScript("/js/service/od/order/regularReqPopup.js");

	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({
			PG_ID  : "regularReqGoodsListPopup",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){
		fnInitButton();
		fnInitOptionBox();
		initInputFormMain();
		bindEvents();
		fbTabChange();
	};

	//--------------------------------- Button Start---------------------------------
	// 버튼 초기화
	function fnInitButton(){
		$("#fnSave, #fnSelect").kendoButton();
	};
	//--------------------------------- Button End-----------------------------------

	//------------------------------- Grid Start -------------------------------
	// 데이터 Set
	function initInputFormMain(){

		initRegularShippingExpect();	// 배송예정내역조회
		initRegularShippingSkip();		// 건너뛰기내역조회
		initRegularShippingHistory();	// 배송내역조회
	}

	// 배송예정내역조회
	function initRegularShippingExpect() {

		fnAjax({
            url     : '/admin/order/getOrderRegularReqDetailShippingExpect',
            params	: {'odRegularReqId' : odRegularReqId},
            success :
                function( data ){
            		makeHtmlReqRoundGoodsList('shippingListArea', data, 0);
                },
            isAction : 'select'
        });
	}

	// 건너뛰기내역조회
	function initRegularShippingSkip() {

		fnAjax({
			url     : '/admin/order/getOrderRegularReqDetailShippingSkip',
			params	: {'odRegularReqId' : odRegularReqId},
			success :
				function( data ){
        		makeHtmlReqRoundGoodsList('skipListArea', data, 1);
			},
			isAction : 'select'
		});
	}

	// 배송내역조회
	function initRegularShippingHistory() {

		fnAjax({
			url     : '/admin/order/getOrderRegularReqDetailShippingHistory',
			params	: {'odRegularReqId' : odRegularReqId},
			success :
				function( data ){
				makeHtmlShippingGoodsList(data);
			},
			isAction : 'select'
		});
	}

	/** 회차별 상품 정보 HTML 생성 */
	function makeHtmlReqRoundGoodsList(id, data, tabIdx) {
		var divObj = $("#" + id);
		if(data.total < 1) {
			var noneTemplate = $("#shippingSkipTableNoneTemplate").html();
			if(id == "skipListArea") noneTemplate = $("#shippingSkipTableNoneTemplateSkip").html();
			divObj.find("div.table-wrapper").html(noneTemplate);
			return false;
		}

		var htmlStr = "";
		for(var i=0; i<data.total; i++) {

			var tableTemplate = $("#shippingSkipTableTemplate").html();
			var allCheckboxTemplate = $("#shippingSkipTableCheckboxTemplate").html();

			var dataRow = data.rows[i];
			var title = dataRow.reqRound + " 회차 / " + dataRow.arriveDt + " (" + dataRow.weekCdNm + ")";
			if(dataRow.odid != null) {
				title += " *주문번호 : " + dataRow.odid;
			}

			var tbodyInfo = "";
			// 상품 리스트
			var goodsList = dataRow.goodsList;
			var goodsCnt = 0;
			var checkCnt = 0;
			if(goodsList != null) {
				for(var j=0; j<goodsList.length; j++) {

					var goodsItem = goodsList[j];
					var tbodyTemplate = $("#shippingSkipContentsTemplate").html();
					var checkboxTemplate = $("#shippingSkipContentsCheckboxTemplate").html();
					if(tabIdx == 0) {
						if(goodsItem.reqDetailStatusCd == 'REGULAR_DETL_STATUS_CD.APPLY' && goodsItem.statusChgPsbYn == 'Y') {
							goodsCnt++;
							checkboxTemplate = checkboxTemplate.replace("{odRegularResultDetlId}", goodsItem.odRegularResultDetlId)
															   .replace("{odRegularResultId}"    , goodsItem.odRegularResultId)
															   .replace("{odRegularReqId}"       , goodsItem.odRegularReqId)
															   .replace(/{ilGoodsId}/ig          , goodsItem.ilGoodsId);
						}
						else {
							checkboxTemplate = "";
						}

						for(var k=0; k<ilGoodsIdList.length; k++) {
//							if(ilGoodsIdList[k] == goodsItem.ilGoodsId && goodsItem.reqDetailStatusCd == 'REGULAR_DETL_STATUS_CD.APPLY') {
							if(ilGoodsIdList[k] == goodsItem.ilGoodsId && dataRow.nextRound) {
								checkCnt++;
								checkboxTemplate = checkboxTemplate.replace("{checked}", "checked");
							}
						}
					}
					else {
						if(goodsItem.statusChgPsbYn == 'Y') {
							checkboxTemplate = checkboxTemplate.replace("{odRegularResultDetlId}", goodsItem.odRegularResultDetlId)
								.replace("{odRegularResultId}"    , goodsItem.odRegularResultId)
								.replace("{odRegularReqId}"       , goodsItem.odRegularReqId)
								.replace(/{ilGoodsId}/ig          , goodsItem.ilGoodsId);
						}
						else {
							checkboxTemplate = "";
						}
					}

					var goodsInfo = goodsItem.ilItemCd + "<br />" + goodsItem.itemBarcode;
					tbodyTemplate = tbodyTemplate.replace("{checkboxArea}", checkboxTemplate)
												 .replace("{goodsInfo}"   , goodsInfo)
												 .replace("{goodsNm}"     , goodsItem.goodsNm)
												 .replace("{statusCd}"    , goodsItem.reqDetailStatusCdNm);
					tbodyInfo += tbodyTemplate;
				}
			}
			if(tabIdx == 0) {
				if(goodsCnt < 1) {
					allCheckboxTemplate = "";
				}
				if(goodsCnt == checkCnt) {
					allCheckboxTemplate = allCheckboxTemplate.replace("{checked}", "checked");
				}
				else {
					allCheckboxTemplate = allCheckboxTemplate.replace("{checked}", "");
				}
			}

			tableTemplate = tableTemplate.replace(/{title}/ig        , title)
										 .replace("{tbodyInfo}"      , tbodyInfo)
										 .replace("{allCheckboxArea}", allCheckboxTemplate);
			htmlStr += tableTemplate;
		}

		divObj.find("div.table-wrapper").html(htmlStr);

//		for(var k=0; k<ilGoodsIdList.length; k++) {
//			divObj.find(".ilGoodsId_" + ilGoodsIdList[i]).prop("checked", true);
//		}

		btnDisabledCheck();
	}

	/** 배송내역 정보 HTML 생성 */
	function makeHtmlShippingGoodsList(data) {
		var divObj = $("#shippingHististArea");
		if(data.total < 1) {
			var noneTemplate = $("#shippingSkipTableNoneTemplate").html();
			divObj.find("div.table-wrapper").html(noneTemplate);
			return false;
		}

		var htmlStr = "";

		for(var i=0; i<data.total; i++) {

			var tableTemplate = $("#shippingTableTemplate").html();

			var dataRow = data.rows[i];
			var title = dataRow.reqRound + " 회차 / " + dataRow.arriveDt + " (" + dataRow.weekCdNm + ")";

			if(dataRow.odid != null) {
				title += " *주문번호 : <a href='/layout.html#/orderMgm?inputMenuId="+inputMenuId+"&odid="+dataRow.odid+"' target='_blank'>" + dataRow.odid + "</a>";
			}

			var tbodyInfo = "";
			// 상품 리스트
			var goodsList = dataRow.goodsList;
			if(goodsList != null) {
				for(var j=0; j<goodsList.length; j++) {

					var goodsItem = goodsList[j];
					var tbodyTemplate = $("#shippingContentsTemplate").html();
					var goodsInfo = goodsItem.ilItemCd + "<br />" + goodsItem.itemBarcode;
					tbodyTemplate = tbodyTemplate.replace("{goodsInfo}"   , goodsInfo)
												 .replace("{goodsNm}"     , goodsItem.goodsNm)
												 .replace("{statusCd}"    , goodsItem.statusNm);
					tbodyInfo += tbodyTemplate;
				}
			}

			tableTemplate = tableTemplate.replace(/{title}/ig        , title)
										 .replace("{tbodyInfo}"      , tbodyInfo);
			htmlStr += tableTemplate;
		}

		divObj.find("div.table-wrapper").html(htmlStr);
	}

	/** 버튼 disabled 체크 */
	function btnDisabledCheck() {
		if($("#shippingListArea .table-wrapper tbody input[type=checkbox]:checked").length < 1) {
			$("#skipBtn").prop("disabled", true);
		}
		else {
			$("#skipBtn").prop("disabled", false);
		}
		if($("#skipListArea .table-wrapper tbody input[type=checkbox]:checked").length < 1) {
			$("#skipCancelBtn").prop("disabled", true);
		}
		else {
			$("#skipCancelBtn").prop("disabled", false);
		}
	}
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	// 옵션 초기화
	function fnInitOptionBox() {
	};

	//이벤트 바인딩
	function bindEvents() {
		// 테이블 각각의 체크박스 전체 체크
		$('#ng-view').on("click", ".table-wrapper thead input[type=checkbox]", function() {
			$(this).closest("thead").siblings("tbody").find("input[type=checkbox]").prop("checked", $(this).is(":checked"));
			btnDisabledCheck();
		});
		// 테이블 각각의 체크박스 체크 시 전체 체크박스 체크
		$('#ng-view').on("click", ".table-wrapper tbody input[type=checkbox]", function() {
			var checkedSize = $(this).closest("tbody").find("input[type=checkbox]:checked").length;
			var checkboxSize = $(this).closest("tbody").find("input[type=checkbox]").length;
			$(this).closest("tbody").siblings("thead").find("input[type=checkbox]").prop("checked", checkedSize == checkboxSize);
			btnDisabledCheck();
		});
	};

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ---------------------------------
    //닫기
    function fnClose(){
        parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
    };

    //저장
    function fnSave(id, url, msg) {

    	var params = {
    			odRegularReqId : odRegularReqId
    	};
    	params.reqInfoList = new Array();

    	$("#" + id + " table tbody input[name=rowCheckbox]:checked").each(function(index) {
    		var data = new Object();
    		data.odRegularResultId = $(this).data('odregularresultid');
    		data.odRegularResultDetlId = $(this).data('odregularresultdetlid');
    		data.odRegularReqId = $(this).data('odregularreqId');
    		params.reqInfoList.push(data);
    	});

		fnAjax({
			url         : url,
			params	    : params,
			contentType : 'application/json',
			success     :
				function( data ){
				fnKendoMessage({message: msg});
				initInputFormMain();
			},
			isAction : 'update'
		});
	}
	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------]
    /** 건너뛰기 */
    $scope.skip = function () {
    	fnSave('shippingListArea', '/admin/order/getOrderRegularReqDetailGoodsSkip', '선택하신 상품이 건너뛰기 되었습니다.');
    };

    /** 건너뛰기 철회 */
    $scope.skipCancel = function () {
    	fnSave('skipListArea', '/admin/order/getOrderRegularReqDetailGoodsSkipCancel', '선택하신 상품이 건너뛰기가 철회 되었습니다.');
    };
	$scope.fnSave = function( ) {	fnSave();	};
	$scope.fnClose = function(){ fnClose(); };
	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------
	//------------------------------- Validation End -------------------------------------
}); // document ready - END
