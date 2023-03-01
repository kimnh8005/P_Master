/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 배송 요일 변경
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.10		김승우          최초생성
 * **/
"use strict";

var paramData;
if(parent.POP_PARAM['parameter']){
	paramData = parent.POP_PARAM['parameter'];
}

var scheduleDay = fnGetDayAdd( fnGetToday(), 4 );

$(document).ready(function() {
	const $deliveryDayWeek = $(".deliveryDayWeek__list");

	// 남은 배송회차x수량 계산해서 수정!!!!
	const maxCount = 10;
	const minCount = 0;
	let addAmmount = 1;
	let mode = "insert";

	//Initialize Page Call ---------------------------------
	fnInitialize();

	//Initialize Page
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({
			PG_ID  : "deliveryDayWeekPopup",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){

		fnDefaultSet();
	    bindEvents();

	    if( mode === "update" ) {
	    	addAmmount = 2;
	    }
	};

	function fnDefaultSet(){

		// 배송 요일, 수량 기본 세팅
		// 변경 적용일 수정!!!!

		$(".deliveryDayWeek__date").html(scheduleDay);
		$(".deliveryDayWeek__day").html(getInputDayLabel(scheduleDay));

		let shippingDayList = paramData.goodsDailyCycleTermDays.split('/');

		// 주 3회 배달 가능 가맹점일때 -> 화목 disabled 처리!!!
		$deliveryDayWeek.find('.deliveryDayWeek__listItem').each(function(){
			for(let shippingDay of shippingDayList){
				if($(this).find('.deliveryDayWeek__list__day').text() == shippingDay){
					$(this).find('.deliveryDayWeek__check').prop('checked',true);

					// 수량 수정!!
					$(this).find('.counter__count').val(paramData.orderCnt);

					disableCounter($(this), false);
				}
			}
		});

		if(paramData.scheduleDeliveryInterval == "STORE_DELIVERY_INTERVAL.TWO_DAYS"){
			$('.deliveryDayWeek__check').eq(1).prop("disabled", true);
			$('.deliveryDayWeek__check').eq(3).prop("disabled", true);
		}
	}

	function bindEvents() {

		// 체크박스 이벤트
		$(".deliveryDayWeek__check").on("change", function(e) {

			const self = $(this);
		    const _checked =  self.is(":checked");
		    const $listItem = self.closest(".deliveryDayWeek__listItem");
		    const _disabled = !_checked;

		    disableCounter($listItem, _disabled);
	    })


	    // plus, minus 이벤트
	    $(".counter__btn").on("click", function(e) {
	    	e.stopPropagation();
	    	const self = $(this);
	    	const $count = self.parent().find(".counter__count");
	    	const _type = self.data("type");
	    	let countVal = Number($count.val());

	    	if( _type === "plus" ) {

		        if( countVal >= maxCount ) return;
		        countVal = countVal + addAmmount;

	    	} else if( _type === "minus" ) {

		        if( countVal <= minCount ) return;
		        countVal = countVal - addAmmount;

		        if( countVal === minCount ) {
		          self.closest(".deliveryDayWeek__listItem").find(".deliveryDayWeek__check").prop("checked", false);
		          disableCounter(self.closest(".deliveryDayWeek__listItem"), true);
		        }

	    	}
	     	$count.val( countVal );
	    })

	    // counter__count 입력 이벤트
	    $(".counter__count").on("keyup", function(e) {
	    	const regex = /[^0-9]/g;
	    	const self = $(this);
	    	const value = self.val();

	    	if( regex.test(value) ) {
	    		fnKendoMessage({message: "숫자만 입력할 수 있습니다."});
		        self.val(0);
		        self.focus();
	    	}

	    	if( value.length > 1 && value.startsWith("0") ) {
	    		self.val(value.replace("0", ""));
	    	}

	    	if( Number(value) > maxCount ) {
	    		fnKendoMessage({message: "최대 개수 이상으로 입력할 수 없습니다. 최대 갯수 : " + maxCount});
		        self.val(0);
	    	} else if( Number(value) < minCount ) {
	    		fnKendoMessage({message: "최소 개수 이하로 입력할 수 없습니다. 최대 갯수 : " + maxCount});
		        self.val(0);
	    	}
	    })

	    // counter__count blur 이벤트
	    $(".counter__count").on("blur", function(e) {
	    	const self = $(this);
	    	const value = self.val();

	    	if( !value.length ) {
		        self.val(0);
		        return;
	    	}
	    })
	}

	function fnSave(){

		var allValueCheck = true;
		$deliveryDayWeek.find('.deliveryDayWeek__listItem').each(function(){
			if($(this).find('.deliveryDayWeek__check').prop("checked") && parseInt($(this).find('.counter__count').val()) == 0){
				allValueCheck = false;
			}
		});

		if(allValueCheck == false || $(".deliveryDayWeek__check:checked").length == 0){
			fnKendoMessage({message: "배송요일을 선택 해주세요."});
			return;
		}

		// 배송 요일변경 API 호출
		let scheduleChangeForm = $("#scheduleChangeForm").formSerialize(true);

		var value = "";
		var totalCnt = "";
		$('[name="deliveryDayOfWeekCnt"]').each(function() {
			if ($(this).val() > 0) value += $(this).val() + "∀";
			totalCnt = Number(totalCnt) + Number($(this).val());
		});
		if (value == null) value = "";
		value = value.substring(0, value.length - 1);

		if(totalCnt > paramData.saleSeq) {
			fnKendoMessage({message: "남은 수량 확인 바랍니다."});
			return;
		}

		scheduleChangeForm.deliveryDayOfWeekCnt = value;

		scheduleChangeForm.odOrderDetlId = paramData.odOrderDetlId;

		scheduleChangeForm.changeDate = scheduleDay;

		fnAjax({
			url     : '/admin/order/schedule/putScheduleArrivalDay',
			params  : scheduleChangeForm,
			async   : true,
			success :
				function( resultData ){
					fnBizCallback('scheduleArrival', resultData);
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

	//-------------------------------  Common Function start ----------------------------------
	function disableCounter($el, isDisable = true) {
		$el.find(".counter__btn, .counter__count").each(function() {
			const self = $(this);
			self.prop("disabled", isDisable);

			if(self.hasClass("counter__count") && isDisable) {
				self.val(0);
			}

		});
	}

	function getInputDayLabel(scheduleDay) {

	    var week = new Array('(일)', '(월)', '(화)', '(수)', '(목)', '(금)', '(토)');

	    var today = new Date(scheduleDay).getDay();
	    var todayLabel = week[today];

	    return todayLabel;
	}

	function fnBizCallback(id, result) {
    	switch (id) {
		case 'scheduleArrival' :			// 상품 도착일 변경(저장)
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

}); // document ready - END
