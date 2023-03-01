/**-----------------------------------------------------------------------------
 * description 		 : 정기배송관리 > 신청 정보 변경
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 20201.02.25	김명진          최초생성
 * **/
"use strict";

var paramData = parent.POP_PARAM['parameter'];
var odRegularReqId = paramData.odRegularReqId;
var changeType = paramData.changeType;
var goodsCycleTermTp, goodsCycleTp, weekCd;
var prevGoodsCycleTermTp, prevGoodsCycleTp, prevWeekCd;

$(document).ready(function() {
	importScript("/js/service/comm/searchCommItem.js");
	importScript("/js/service/od/order/orderCommSearch.js");

	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({
			PG_ID  : "regularReqInfoPopup",
			callback : fnUI
		});

		$("#" + changeType).closest("tr").show();
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
//		$("#fnSave, #fnSelect").kendoButton();
	};
	//--------------------------------- Button End-----------------------------------

	//------------------------------- Grid Start -------------------------------
	// 데이터 Set
	function initInputFormMain(){
		fnAjax({
            url     : '/admin/order/getOrderRegularReqDetailBuyer',
            params	: {'odRegularReqId' : odRegularReqId},
            success :
                function( data ){
            		goodsCycleTermTp = data.goodsCycleTermTp;
            		goodsCycleTp = data.goodsCycleTp;
            		weekCd = data.weekCd;
            		prevGoodsCycleTermTp = data.goodsCycleTermTp;
            		prevGoodsCycleTp = data.goodsCycleTp;
            		prevWeekCd = data.weekCd;
            		$('#reqRoundInfo').text(data.reqRound + " / 총 " + data.totCnt + " ( " + (data.totCnt - data.reqRound) + " 회차 남음 )");
            		$("#goodsCycleTermTp").siblings("span").find("span.k-input").text(data.goodsCycleTermTpNm);
            		$('#goodsCycleTermTp').val(data.goodsCycleTermTp);
            		$("#goodsCycleTp").siblings("span").find("span.k-input").text(data.goodsCycleTpNm);
            		$('#goodsCycleTp').val(data.goodsCycleTp);
            		$('#nextReqRound').text(data.arriveDt + " 도착 예정");
            		$('input[name=weekCd]').each(function() {
            			if($(this).val() == data.weekCd) {
            				$(this).prop('checked', true);
            			}
            		});
                },
            isAction : 'select'
        });
	}

	function getChangeTp() {
		if(changeType == "goodsCycleTermTp") {
			return 'GOODS_CYCLE_TERM_TP';
		}
		else if(changeType == "goodsCycleTp") {
			return 'GOODS_CYCLE_TP';
		}
		else {
			return 'WEEK_CD';
		}
	}

	/** 회차 변경 정보 조회 */
	function changeReqRoundInfo() {
		var params = {
				odRegularReqId   : odRegularReqId,
				goodsCycleTermTp : goodsCycleTermTp,
				goodsCycleTp     : goodsCycleTp,
				weekCd           : weekCd,
				changeTp         : getChangeTp()
		};

		fnAjax({
            url     : '/admin/order/getOrderRegularReqDetailChangeBuyerInfo',
            params	: params,
            success :
                function( data ){
            	console.log(data);
	            	if(data.RETURN_CODE == "0000") {
	            		if(changeType == "weekCd") {
	            			$('#nextReqRound').text(data.nextDeliveryDt + " 도착 예정" + ( weekCd != data.weekCd ? ' (출고처 휴일 적용 됨)' : '' ));
	            		}
	            		$(".reqInfo #fnSave").prop("disabled", false);
	            	}
	            	else {
	            		$(".reqInfo #fnSave").prop("disabled", true);
	            	}
                },
            fail     :
            	function() {
            		$(".reqInfo #fnSave").prop("disabled", true);
            	},
            isAction : 'select'
        });
	}
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	// 옵션 초기화
	function fnInitOptionBox() {

		// 정기배송 신청 기간
		searchCommonUtil.getDropDownCommCd("goodsCycleTermTp", "NAME", "CODE", "신청 기간 선택", "REGULAR_CYCLE_TERM_TYPE", "Y");
		// 정기배송 신청 주기
		searchCommonUtil.getDropDownCommCd("goodsCycleTp", "NAME", "CODE", "신청 주기 선택", "REGULAR_CYCLE_TYPE", "Y");
		// 정기배송 신청 요일
		orderSearchUtil.getRadioCommData(orderOptionUtil.regularWeekCd);
	};

	//이벤트 바인딩
	function bindEvents() {
		$('#ng-view').on("change", "#goodsCycleTermTp", function() {
			if($(this).val() != "") {
				goodsCycleTermTp = $(this).val();
				changeReqRoundInfo();
			}
		});
		$('#ng-view').on("change", "#goodsCycleTp", function() {
			if($(this).val() != "") {
				goodsCycleTp = $(this).val();
				changeReqRoundInfo();
			}
		});
		$('#ng-view').on("change", "input[name=weekCd]", function() {
			weekCd = $(this).val();
			changeReqRoundInfo();
		});
	};

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ---------------------------------
    //닫기
    function fnClose(){
        parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
    };

    //저장
    function fnSave() {
    	var changeTp = getChangeTp();

    	if(changeTp == 'GOODS_CYCLE_TERM_TP') {
    		if(goodsCycleTermTp == prevGoodsCycleTermTp) {
    			fnKendoMessage({message: '동일한 기간 정보로 변경할 수 없습니다.'});
    			return false;
    		}
    	}
    	if(changeTp == 'GOODS_CYCLE_TP') {
    		if(goodsCycleTp == prevGoodsCycleTp) {
    			fnKendoMessage({message: '동일한 주기 정보로 변경할 수 없습니다.'});
    			return false;
    		}
    	}
    	if(changeTp == 'WEEK_CD') {
    		if(weekCd == prevWeekCd) {
    			fnKendoMessage({message: '동일한 요일 정보로 변경할 수 없습니다.'});
    			return false;
    		}
    	}

		var params = {
				odRegularReqId   : odRegularReqId,
				goodsCycleTermTp : goodsCycleTermTp,
				goodsCycleTp     : goodsCycleTp,
				weekCd           : weekCd,
				changeTp         : changeTp
		};

		fnAjax({
            url     : '/admin/order/putOrderRegularReqDetailChangeBuyerInfo',
            params	: params,
            success :
                function( data ){
            		fnKendoMessage({message: '신청정보가 변경 되었습니다.'});
            		fnClose();
                },
            isAction : 'update'
        });
	}
	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	$scope.fnSave = function( ) {	fnSave();	};
	$scope.fnClose = function(){ fnClose(); };
	$scope.fnSelect = function() {};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------
	//------------------------------- Validation End -------------------------------------
}); // document ready - END
