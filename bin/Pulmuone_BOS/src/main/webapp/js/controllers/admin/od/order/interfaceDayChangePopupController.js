/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 주문 I/F 변경
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.07		김승우          최초생성
 * **/
"use strict";

var PAGE_SIZE = 20;
var SOURCE_ID = "fbSource";
var datePicker = null;
var paramData;
if(parent.POP_PARAM['parameter']){
	paramData = parent.POP_PARAM['parameter'];
}

var dayOn = [];
var deliveryList = [];
var dawnDeliveryList = [];

var goodsDeliveryDateMap = new Object();
var goodsDawnDeliveryDateMap = new Object();

$(document).ready(function() {
	importScript("/js/service/od/odComm.js");

	// 달력 선택된 날짜 받을 변수
	var selectedDate = null;

	fnInitialize();	//Initialize Page Call ----------------------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({
			PG_ID  : "interfaceDayChangePopup",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){
		fnInitGrid();
		bindEvents();
	};

	//------------------------------- Grid Start ----------------------------------------
	// 그리드 초기화
	function fnInitGrid(){
		let url = '/admin/order/getIfDayList';

		// 주문 I/F 출고지시일 목록 조회 요청 파라미터 세팅
		var data = new Object();

		data.odOrderDetlId = paramData.odOrderDetlId;
		data.urWarehouseId = paramData.urWarehouseId;
		data.ilGoodsId = paramData.ilGoodsId;
		data.orderCnt = paramData.orderCnt;
		data.recvBldNo = paramData.recvBldNo;
		data.zipCode = paramData.zipCode;
		data.goodsDailyCycleType = paramData.goodsCycleTpCd;
		data.weekCode = paramData.weekCode;

		$('.orderIfPopup__ifDate').text(paramData.orderIfDt);

		// 주문생성일 경우
		if(paramData.orderCreateYn != undefined && paramData.orderCreateYn == 'Y'){
			url = '/admin/order/getIfDayListForOrderCreate';
			data.orderCreateYn = "Y";
			data.orderCreateGoodsList = paramData.goodsList;
			data.isMember = paramData.isMember;
		}

		fnAjax({
			url     : url,
			params  : data,
			contentType : 'application/json',
			success :
				function( data ){
					deliveryList = data.deliveryList;
					dawnDeliveryList = data.dawnDeliveryList;

					goodsDeliveryDateMap = data.goodsDeliveryDateMap;
					goodsDawnDeliveryDateMap = data.goodsDawnDeliveryDateMap;

					//재고가 없으면
					if(data.dawnDeliveryList == '' && data.deliveryList == ''){
						let noStock = "<td colspan='7' height='50px'><div style='text-align: center; font-size: 15px; font-weight : bold;'>변경 가능한 출고지시일이 없습니다.</div></td>";
						$("#arrivalScheduledDateList").append(noStock);
					} else {
						// 새벽배송 가능
						if(data.dawnDeliveryList.length > 0){
							$('.orderIfPopup__isDawn').css('display','inline');
							$('.labelTabs').css('display','');
							var dawnYn = paramData.goodsDeliveryType == "GOODS_DELIVERY_TYPE.DAWN" ? "Y" : "N";
							$("input[name='orderIfDawnYn'][value="+dawnYn+"]").trigger("click");
							$('#allChangeYnDiv').css('display', 'block');
							$('.btn-area').css('display', 'block');

						// 일반배송만 가능
						}else{
							fnViewModelSetting(deliveryList);
						}
					}

				},
			isAction : 'batch'
		});

		// 주문생성에서 진입했을 시 무조건 동일 출고처 모두 변경
		if(paramData.orderCreateYn != undefined && paramData.orderCreateYn == 'Y'){
			$("#allChangeYn").attr("disabled",true);
		}

	};
	//-------------------------------  Grid End  ----------------------------------------

	//-------------------------------  Common Function start ----------------------------

    // viewModel 세팅
    function fnViewModelSetting(paramList){

    	dayOn = [];

		$('#allChangeYnDiv').css('display', 'block');
		$('.btn-area').css('display', 'block');

		$('input[name=orderIfType]:not(:last)').prop('disabled', true);

    	if(paramList.length > 0){

    		// 출고지시일 datePicker 세팅
    		for(var i = 0 ; i < paramList.length ; i ++){
    			if( paramList[i] ){
    				dayOn.push(paramList[i].arrivalScheduledDate);
    				$('input[name=orderIfType][value='+i+']').prop('disabled', false);
    			}
    		}

    		// 주문I/F일
			var orderIfDate = "<td style='text-align: center;'><div class='infoTable__cell'>주문I/F일</div></td>";
			for(var i = 0 ; i < 5 ; i++){
				orderIfDate += "<td style='text-align: center;'>";
				if (paramList[i]) {
					orderIfDate += "<div class='infoTable__cell' id='orderIfDate_"+i+"' value='"+paramList[i].orderDate+"'>"+paramList[i].orderDate+"</div>";
				} else {
					orderIfDate += "<div class='infoTable__cell' id='orderIfDate_"+i+"' value='-'>-</div>";
				}
				orderIfDate += "</td>";
			}

			orderIfDate += '<td>';
			orderIfDate += '<div class="infoTable__cell">';
			orderIfDate += '<input class="comm-input disabled" type="text" id="orderIfDate" name="orderIfDate" disabled style="width: 100%;">';
			orderIfDate += '</div>';
			orderIfDate += '</td>';

			$("#orderIfDateList").append(orderIfDate);


    		// 출고예정일
    		var forwardingScheduledDate = "<td style='text-align: center;'><div class='infoTable__cell'>출고예정일</div></td>";
    		for(var i = 0 ; i < 5 ; i++){
    			forwardingScheduledDate += "<td style='text-align: center;'>";
    			if (paramList[i]) {
    				forwardingScheduledDate += "<div class='infoTable__cell' id='forwardingScheduledDate_"+i+"' value='"+paramList[i].forwardingScheduledDate+"'>"+paramList[i].forwardingScheduledDateStr+"</div>";
    			} else {
    				forwardingScheduledDate += "<div class='infoTable__cell' id='forwardingScheduledDate_"+i+"' value='-'>-</div>";
    			}
    			forwardingScheduledDate += "</td>";
    		}

    		forwardingScheduledDate += '<td>';
    		forwardingScheduledDate += '<div class="infoTable__cell">';
			forwardingScheduledDate += '<input class="comm-input disabled" type="text" id="forwardingScheduledDate" name="forwardingScheduledDate" disabled style="width: 100%;">';
    		forwardingScheduledDate += '</div>';
    		forwardingScheduledDate += '</div>';
    		forwardingScheduledDate += '</td>';

    		$("#forwardingScheduledDateList").append(forwardingScheduledDate);

    		// 도착예정일
    		var arrivalScheduledDate = "<td style='text-align: center;'><div class='infoTable__cell'>도착예정일</div></td>";
    		for(var i = 0 ; i < 5 ; i++){
    			arrivalScheduledDate += "<td style='text-align: center;'>";
    			if (paramList[i]) {
    				arrivalScheduledDate += "<div class='infoTable__cell' id='arrivalScheduledDate_"+i+"' value='"+paramList[i].arrivalScheduledDate+"'>"+paramList[i].arrivalScheduledDateStr+"</div>";
    			} else {
    				arrivalScheduledDate += "<div class='infoTable__cell' id='arrivalScheduledDate_"+i+"' value='-'>-</div>";
    			}
    			arrivalScheduledDate += "</td>";
    		}

    		arrivalScheduledDate += '<td>';
    		arrivalScheduledDate += '<div class="infoTable__cell">';
			arrivalScheduledDate += '<div class="datepicker-wrapper width100">';
			arrivalScheduledDate += '<input type="text" id="datepicker" class="fb__datePicker--input" autocomplete="off">';
    		arrivalScheduledDate += '</div>';
    		arrivalScheduledDate += '</td>';

    		$("#arrivalScheduledDateList").append(arrivalScheduledDate);
    		initDatePicker();
    		$('#orderIfDateList').hide();
    	}

    	//일일배송인경우 동일출고처 출고지시일 모두 변경 불가
    	if(paramData.goodsDeliveryType == "GOODS_DELIVERY_TYPE.DAILY" || paramData.orderStatusDeliTpCd == "ORDER_STATUS_DELI_TP.DAILY") {
			$("input:checkbox[name=allChangeYn]").val('N');
			$("input:checkbox[name=allChangeYn]").prop("checked",false);
    		$('#allChangeYnDiv').css('display', 'none');
		}

    };

    function fnClose(){
    	parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
    };

	//-------------------------------  Events start -------------------------------------

	function bindEvents() {

		// 일반,새벽 탭 클릭시
		$("input[name='orderIfDawnYn']").on("click", function(e) {

			$("#orderIfDateList").find('td').remove();
			$("#forwardingScheduledDateList").find('td').remove();
			$("#arrivalScheduledDateList").find('td').remove();

			//새벽 tab
			if(this.value == 'Y'){
				fnViewModelSetting(dawnDeliveryList)
			//일반 tab
			}else{
				fnViewModelSetting(deliveryList)
			}

		});

	}

	function fnCheckAllChangeYn(){
		//동일 출고처 출고지시일 모두 변경 체크했을 경우
		if($("input:checkbox[name=allChangeYn]").is(":checked") == true){
			if(fnNvl($('input[name=orderIfType]:checked').val()) == '' ||
				($('input[name=orderIfType]:checked').val() == 'typing' && $('#forwardingScheduledDate').val() == '')){
				fnKendoMessage({message: "출고지시일을 선택해주세요."});
				return;
			} else {
				fnKendoMessage({message:"동일한 배송방법, 출고처, 주문 I/F 주문도 함께 변경하시겠습니까?", type : "confirm" , ok : function(){
					$("input:checkbox[name=allChangeYn]").val('Y');
					fnPutOrderIfDate();
				},cancel : function(){
					$("input:checkbox[name=allChangeYn]").val('N');
					$("input:checkbox[name=allChangeYn]").prop("checked",false);
					fnPutOrderIfDate();
				}});
			}
		}else{
			fnPutOrderIfDate();
		}
	}


	function fnPutOrderIfDate(){
		var data = $('#orderIfInfoForm').formSerialize(true);
		var index = data.orderIfType;

		if(index == "" || (index == "typing" && data.forwardingScheduledDate == "")){
			fnKendoMessage({message: "출고지시일을 선택해주세요."});
			return;
		}

		// 출고지시일 날짜 직접 선택
		if(index == 'typing'){
			data.shippingDt = data.forwardingScheduledDate;
			data.deliveryDt = $("#datepicker").val();
			data.orderIfDt  = data.orderIfDate;
		}else {
			data.orderIfDt =  $("#orderIfDate_" + index).attr("value");
			data.shippingDt = $("#forwardingScheduledDate_" + index).attr("value");
			data.deliveryDt = $("#arrivalScheduledDate_" + index).attr("value");
		}

		if(data.allChangeYn == "") data.allChangeYn = "N";
		data.odOrderDetlId = paramData.odOrderDetlId;
		data.goodsTpCd = paramData.goodsTpCd;
		data.goodsDailyTp = paramData.goodsDailyTp;
		data.urWarehouseId = paramData.urWarehouseId;
		data.orderIfDawnYn = $("input:radio[name=orderIfDawnYn]:checked").val() == undefined ? '' : $("input:radio[name=orderIfDawnYn]:checked").val();
		data.odShippingZoneId = paramData.odShippingZoneId;
		data.promotionTp = paramData.promotionTp;

		if (stringUtil.getString(paramData.orderCopyYn, "N") == "Y") {
			parent.POP_PARAM["parameter"].allChangeYn	= data.allChangeYn;
			parent.POP_PARAM["parameter"].shippingDt	= data.shippingDt;
			parent.POP_PARAM["parameter"].deliveryDt	= data.deliveryDt;
			parent.POP_PARAM["parameter"].orderIfDt		= data.orderIfDt;
			parent.POP_PARAM["parameter"].orderIfDawnYn = data.orderIfDawnYn;
			parent.POP_PARAM["parameter"].goodsDeliveryDateMap = goodsDeliveryDateMap;
			parent.POP_PARAM["parameter"].goodsDawnDeliveryDateMap = goodsDawnDeliveryDateMap;

			fnKendoMessage({
				message: "변경되었습니다.", ok: function () {
					fnClose();
				}
			});
		} else {
			fnAjax({
				url: '/admin/order/putIfDay',
				params: data,
				success:
					function (data) {

						if (data.code == '0000') {
							fnKendoMessage({
								message: "변경되었습니다.", ok: function () {
									fnClose();
								}
							});
						} else {
							fnKendoMessage({message: data.message});
						}

					},
				error : function(data){
					fnKendoMessage({message: data.message});
				},
				isAction: 'batch'
			});
		}


	}

	//-------------------------------  Events End ----------------------------------------

	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start ----------------------------
	$scope.fnClose = function( ){  fnClose();};
	$scope.fnCheckAllChangeYn =function(){	fnCheckAllChangeYn(); };

	//------------------------------- Html 버튼 바인딩  End ------------------------------

	//------------------------------- Datepicker Start -------------------------------------

	function initDatePicker() {
		datePicker = new FbDatePicker("datepicker", {
			dayOn : dayOn
			,onChange: function(e) {
				var date = e.target.value;
				for(var i = 0 ; i < deliveryList.length ; i ++){
					if(deliveryList[i].arrivalScheduledDate == date){
						$("#orderIfDate").val(deliveryList[i].orderDate);
						$("#forwardingScheduledDate").val(deliveryList[i].forwardingScheduledDate);
					}
				}
			}
		})
	}
	//------------------------------- Datepicker End -------------------------------------

}); // document ready - END
