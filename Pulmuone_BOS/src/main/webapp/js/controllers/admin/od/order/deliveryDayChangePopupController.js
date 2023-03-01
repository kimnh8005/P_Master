/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 건너뛰기
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.10		김승우          최초생성
 * **/
"use strict";

var pageParam = fnGetPageParam();
var startPicker = null;
var endPicker = null;
var changePicker = null;
var dayOff = [];
var paramData;
if(parent.POP_PARAM['parameter']){
	paramData = parent.POP_PARAM['parameter'];
}

var scheduleDay = fnGetDayAdd( fnGetToday(), 4 );
var goodsInfo 				= paramData.goodsInfo;
var scheduleDelvDateList 	= paramData.scheduleDelvDateList;

// 변경일선택에서 선택가능한 일자 설정!!!!
var disableDay = [];
goodsInfo.scheduleDeliveryInterval == 'STORE_DELIVERY_INTERVAL.EVERY' ? disableDay = [0,6] : disableDay = [0,1,2,4,6];

// 일정 선택 가능한 일자 설정!!!!!!!!!!
// 일정 선택에서 선택가능한 일자
var dayOn = [];
$.each(scheduleDelvDateList, function(idx){
	new Date(scheduleDelvDateList[idx].orgDelvDate) > new Date(scheduleDay) ? dayOn.push(scheduleDelvDateList[idx].orgDelvDate) : "";
});

$(document).ready(function() {
	//녹즙명
	$(".deliveryDay__supplier").html(goodsInfo.goodsNm);
	//마지막 배송일자
	$(".deliveryDay__date").html(goodsInfo.lastDeliveryDate);
	//남은 수량
	$(".deliveryDay__leftCount").html(goodsInfo.saleSeq);
  //Initialize Page Call ---------------------------------
	fnInitialize();

	//Initialize Page
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({
			PG_ID  : "deliveryDayChangePopup",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){

		fnInitButton();
		fnInitOptionBox();

		// bindEvents();
	};

	function bindEvents() { }

	//--------------------------------- Button Start---------------------------------

	function fnInitButton() { }

	//--------------------------------- Button End-----------------------------------

	//------------------------------- Grid Start -------------------------------
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 배송가능 여부 체크
	function fnIsDeliverableDate(date) {
		return dayOn.includes(date.oFormat("yyyy-MM-dd"));
	}

	function fnInitOptionBox() {

		// 출고처휴일&공통휴일에 따른 공휴일 설정
		fnGetWarehouseHolidayList();
		fnGetHolidayList();

		initDatePickers();

	}

	function initDatePickers() {

		startPicker = new FbDatePicker("startPicker", {
			disableDay: disableDay,
			dayOff: dayOff,
			dayOn:dayOn,
			onSelect: onSelectStartPicker,
			onChange: function(e) {
				const context = this;
				onChangeDatePicker.call(context, e, "start", "startPicker", "endPicker");
			}
		})


		endPicker = new FbDatePicker("endPicker", {
			disableDay: disableDay,
			dayOff: dayOff,
			dayOn:dayOn,
			onSelect: onSelectEndPicker,
			onChange: function(e) {
		        const context = this;
		        onChangeDatePicker.call(context, e, "end", "startPicker", "endPicker");
			}
		})

		// 시작일  default값 설정!!!!!!!!
		$("#startPicker").val(scheduleDay);

		changePicker = new FbDatePicker("changePicker", {
			disableDay: disableDay,
			dayOff: dayOff,
			beforeShowDay: function(date) {

				 if( this.isDayOff(date)) {
		             return [false, "dayOff", "휴일"];
		         } else if( this.isDisabledDate(date) ) {
		             return [false, ""];
		         } else {
		        	 // 시작일보다 이전 날짜 변경일에서 비활성화 처리!!!
		        	if((date - new Date(scheduleDay)) > 0) {
		    			return [true, ""];
		    		} else {
		    			return [false, ""];
		    		}
		         }

			},
			onChange: function(e) {
		        const context = this;
		        onChangeDatePicker.call(context, e);
			}
		})


	}
	//---------------Initialize Option Box End ------------------------------------------------

	//-------------------------------  Events start -------------------------------------
	// 데이트피커 날짜 변경 이벤트
	function onChangeDatePicker(e, type, sId, eId) {
	    const self = this;
	    let date = e.target.value;
	    let changeDateId = e.target.id;
	    let $target = $(e.target);
	    let sd = sId ? $("#" + sId) : null;
	    let ed = eId ? $("#" + eId) : null;

	    if( !fnIsValidDate(date) ) {
	    	fnKendoMessage({message: "올바른 날짜가 아닙니다."});
	    	$target.val("");
	    	return;
	    }

	    date = new Date(date);

	    if(changeDateId != "changePicker"){
		    if(!fnIsDeliverableDate(date)) {
		    	fnKendoMessage({message: "변경 불가능한 일자 입니다."});
		    	$target.val("");
		    	return;
		    }
	    }

	    if( sd && ed ) {
	    	let startDate = sd.val();
	    	let endDate = ed.val();
	    	if( !startDate || !endDate ) return;
	    	if( fnValidateDatePicker(startDate, endDate) ) return;

		    if( type === "start" ) {
		    	fnKendoMessage({message: "시작일을 확인해주세요."});
		    } else {
		    	fnKendoMessage({message: "종료일을 확인해주세요."});
		    }

		    $target.val("");
	    }
	}

	function onSelectStartPicker(date, inst) {
		if( !fnIsValidDate(date) ) return;

	    date = new Date(date);
	    let endDate = $("#endPicker").val();

	    if( !endDate ) return;

	    endDate = new Date(endDate);
	    if( date.getTime() > endDate.getTime() ) {
	    	fnKendoMessage({message: "시작일을 확인해주세요."});
	    	$("#" + inst.id).datepicker("setDate", "");
	    	return;
	    }
	}

	function onSelectEndPicker(date, inst) {
		if( !fnIsValidDate(date) ) return;

		date = new Date(date);
		let startDate = $("#startPicker").val();

		if( !startDate ) return;

	    startDate = new Date(startDate);
	    if( date.getTime() < startDate.getTime() ) {
	    	fnKendoMessage({message: "종료일을 확인해주세요."});
	    	$("#" + inst.id).datepicker("setDate", "");
	    	return;
	    }
	}

	function fnGetWarehouseHolidayList(){

		// 출고처 휴일 조회
		fnAjax({
			url     : '/admin/ur/warehouse/getWarehouseHolidayListById',
			params  : {'urWarehouseId' : paramData.urWarehouseId},
			async   : false,
			success :
				function( data ){
					for(var i=0 ; i < data.rows.length ; i++){
						dayOff.push(data.rows[i].holiday);
					}
				},
			isAction : 'batch'
		});
	}

	function fnGetHolidayList(){

		// 공통 휴일 조회
		fnAjax({
			url     : '/admin/policy/holiday/getAllHolidayList',
			async   : false,
			success :
				function( data ){
					for(var i=0 ; i < data.rows.length ; i++){
						dayOff.push(data.rows[i].holidayDate);
					}
				},
			isAction : 'batch'
		});
	}

	function fnSave(){
		var endDate = $("#endPicker").val();
		var changeDate = $("#changePicker").val();

		endDate = new Date(endDate);
		changeDate = new Date(changeDate);

	    if (fnIsEmpty($("#startPicker").val()) || fnIsEmpty($("#endPicker").val()) || fnIsEmpty($("#changePicker").val()))
    		fnKendoMessage({message: "날짜를 확인해주세요."});
	    else if (endDate >= changeDate)
	    	fnKendoMessage({message: "변경 불가능한 날짜 입니다."});
	    else{

		    var scheduleData = {};

		    let params	= new Object();
		    let param = new Object();
			let scheduleUpdateList	= new Array();

			params.changeDate = $("#changePicker").val();
			params.odOrderDetlId = goodsInfo.odOrderDetlId;

			//선택 일자(시작)
			param.delvDate = $("#startPicker").val();
			scheduleUpdateList.push(param);

			//선택 일자(종료)
			param = new Object();
			param.delvDate = $("#endPicker").val();
			scheduleUpdateList.push(param);

			params.scheduleUpdateListData = kendo.stringify(scheduleUpdateList);

			fnAjax({
				url     : '/admin/order/schedule/putScheduleSkip',
				params  : params,
				async   : true,
				success :
					function( resultData ){
						fnBizCallback('scheduleSkip', resultData);
					},
	            fail : function(resultData, resultCode) {
	            	fnKendoMessage({
	                    message : resultCode.message,
	                    ok : function(e) {
	                    	fnBizCallback("fail", resultData);
	                    }
	                });
				},
				isAction : 'insert'
			});
	    }
	}
	//-------------------------------  Events End -------------------------------------

	//-------------------------------  Common Function start ----------------------------------
	// Biz Callback
	function fnBizCallback(id, result) {
    	switch (id) {
		case 'scheduleSkip' :			// 상품 도착일 변경(저장)
			if (result.code == "0000") {
				fnKendoMessage({message : "스케줄이 변경되었습니다.", ok : fnClose });
			} else {
				fnKendoMessage({message : result.message});
			}
			break;


		case 'fail' :	// 오류
			break;
		}
	}

	// 팝업 닫기
	function fnClose(){
		parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
	};

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	$scope.fnSave = function() { fnSave();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------
	//------------------------------- Validation End -------------------------------------
}); // document ready - END
