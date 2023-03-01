/**-----------------------------------------------------------------------------
 * description 		 : 주문생성 > 일일상품 옵션 변경
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.07.06		천혜현          최초생성
 * **/
"use strict";

var PAGE_SIZE = 20;
var viewModel, gridDs, gridOpt, grid;
var pageParam = fnGetPageParam();
var paramData ;
var goodsDailyCycleList = [];
var goodsDailyCycleTypeData = []; 		// 배송주기 라디오버튼 data
var goodsDailyCycleTypeChkVal; 			// 배송주기 라디오버튼 default
var goodsDailyCycleTermTypeData = []; 	// 배송기간 셀렉트박스 data
var goodsBulkTypeData = []; 			// 세트수량 셀렉트박스 data
var goodsDailyBulkYnChkVal;				// 배송유형 라디오버튼 default
var goodsDailyAllergyYnChkVal; 			// 식단유형 라디오버튼 default
if(parent.POP_PARAM['parameter']){
	paramData = parent.POP_PARAM['parameter'];
}

$(document).ready(function() {

	fnInitialize();	//Initialize Page Call ---------------------------------

	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({
			PG_ID  : "dailyGoodsOptionChangePopup",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){
		fnInitGrid();
	};

	//--------------------------------- Button Start---------------------------------
	//--------------------------------- Button End-----------------------------------

	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid(){

		// 상품명
		$(".title__subTitle").html(paramData.goodsName);

		if(paramData.goodsDailyType == "GOODS_DAILY_TP.GREENJUICE"){
			$(".goodsDailyBulkYn").css("display","none");
			$(".goodsBulkType").css("display","none");
			$(".goodsDailyAllergyYn").css("display","none");
		}else if(paramData.goodsDailyType == "GOODS_DAILY_TP.EATSSLIM"){
			// 배송유형, 식단유형, 배송요일, 세트수량
			$(".goodsDailyBulkYn").css("display","none");
			$(".goodsDailyAllergyYn").css("display","none");
			$(".goodsDailyCycleGreenJuiceWeekType").css("display","none");
			$(".goodsBulkType").css("display","none");
		}else if(paramData.goodsDailyType == "GOODS_DAILY_TP.BABYMEAL"){
			$(".goodsBulkType").css("display","none");
		}

		var data = new Object();
		data.odOrderDetlId = paramData.odOrderDetlId;
		data.urWarehouseId = paramData.urWarehouseId;
		data.ilGoodsId = paramData.ilGoodsId;
		data.orderCnt = paramData.orderCnt;
		data.recvBldNo = paramData.recvBldNo;
		data.zipCode = paramData.zipCode;
		data.goodsDailyCycleType = paramData.goodsCycleTpCd;
		data.weekCode = paramData.weekCode;

		fnAjax({
			url     : '/admin/order/create/getGoodsDailyCycleList',
			params  : {"ilGoodsId":paramData.ilGoodsId,"recvBldNo":paramData.recvBldNo,"zipCode":paramData.zipCode},
			success :
				function( data ){

					goodsDailyCycleList = data;

					if(data != null){
						let goodsDailyCycleTypeList = [];
						for(let i=0; i < goodsDailyCycleList.length; i++){
							let goodsDailyCycleTypeObj = new Object();
							goodsDailyCycleTypeObj.CODE = goodsDailyCycleList[i].goodsDailyCycleType;
							goodsDailyCycleTypeObj.NAME = goodsDailyCycleList[i].goodsDailyCycleTypeName;
							goodsDailyCycleTypeList[i] = goodsDailyCycleTypeObj;
						}
						goodsDailyCycleTypeData = goodsDailyCycleTypeList;

						// default setting
						goodsDailyCycleTypeChkVal = paramData.goodsDailyCycleType || goodsDailyCycleList[0].goodsDailyCycleType;
						goodsDailyCycleTermTypeData = fnSettingGoodsDailyCycleTermType(goodsDailyCycleList[0].term);
						goodsBulkTypeData = fnSettingGoodsDailyBulkType(goodsDailyCycleList[0].goodsDailyBulk);
						goodsDailyBulkYnChkVal = paramData.goodsDailyBulkYn == "Y" ? "Y" : "N";
						goodsDailyAllergyYnChkVal = paramData.goodsDailyAllergyYn == "Y" ? "Y" : "N";
					}

					fnInitOptionBox();
					fnBindEvents();
					fnInitOptionData(goodsDailyCycleList);

				},
			isAction : 'batch'
		});

	}

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){
		// 배송주기
		fnTagMkRadio({
			id: "goodsDailyCycleType",
			data:  goodsDailyCycleTypeData,
			tagId: "goodsDailyCycleType",
			chkVal: goodsDailyCycleTypeChkVal
		});

		var goodsDailyCycleGreenJuiceWeekTypeData = [
			{ "CODE" : "WEEK_CD.MON" , "NAME":"월"},
			{ "CODE" : "WEEK_CD.TUE" , "NAME":"화"},
			{ "CODE" : "WEEK_CD.WED" , "NAME":"수"},
			{ "CODE" : "WEEK_CD.THU" , "NAME":"목"},
			{ "CODE" : "WEEK_CD.FRI" , "NAME":"금"}
		];
		//배송요일
		fnTagMkChkBox({
			id    : 'goodsDailyCycleGreenJuiceWeekType',
			data  : goodsDailyCycleGreenJuiceWeekTypeData,
			tagId : 'goodsDailyCycleGreenJuiceWeekType',
			chkVal : 'WEEK_CD.MON'
		});

		//배송기간
		fnKendoDropDownList({
			id    : 'goodsDailyCycleTermType',
			data  : goodsDailyCycleTermTypeData,
			textField :"NAME",
			valueField : "CODE"
		});

		// 배송유형
		fnTagMkRadio({
			id: "goodsDailyBulkYn",
			data:  [
				{ "CODE": "N", "NAME": "일일배송" },
				{ "CODE": "Y", "NAME": "일괄배송" }],
			tagId: "goodsDailyBulkYn",
			chkVal: goodsDailyBulkYnChkVal
		});

		// 식단유형
		fnTagMkRadio({
			id: "goodsDailyAllergyYn",
			data:  [
				{ "CODE": "N", "NAME": "일반식단" },
				{ "CODE": "Y", "NAME": "알러지대체식단" }],
			tagId: "goodsDailyAllergyYn",
			chkVal: goodsDailyAllergyYnChkVal
		});

		//세트수량
		fnKendoDropDownList({
			id    : 'goodsBulkType',
			data  : goodsBulkTypeData,
			textField :"NAME",
			valueField : "CODE"
		});
	}

	function fnBindEvents(){

		// 배송주기 변경
		$('[name="goodsDailyCycleType"]').on('change', function(e, isFirst) {
			fnBindEventClear();
			let goodsCycleType = ($(this).filter(':checked').val());

			// 배송기간
			let termList = goodsDailyCycleList.find(x => x.goodsDailyCycleType === goodsCycleType).term;
			goodsDailyCycleTermTypeData = fnSettingGoodsDailyCycleTermType(termList);
			let dataSource = new kendo.data.DataSource({
				data: goodsDailyCycleTermTypeData
			});

			let index = isFirst ? dataSource.options.data.findIndex(v => paramData.goodsDailyCycleTermType === v.CODE) : 0;
			$("#goodsDailyCycleTermType").data("kendoDropDownList").setDataSource(dataSource);
			$("#goodsDailyCycleTermType").data("kendoDropDownList").select(index);

			// 배송요일
			switch (goodsCycleType) {
				case "GOODS_CYCLE_TP.1DAY_PER_WEEK":
					$("input[value='WEEK_CD.MON']").prop('checked', true);
					break;
				case "GOODS_CYCLE_TP.3DAYS_PER_WEEK": // 월/수/금 고정
					$("input[value='WEEK_CD.MON']").prop('checked', true);
					$("input[value='WEEK_CD.TUE']").prop('disabled', true);
					$("input[value='WEEK_CD.WED']").prop('checked', true);
					$("input[value='WEEK_CD.THU']").prop('disabled', true);
					$("input[value='WEEK_CD.FRI']").prop('checked', true);
					$("input[value='WEEK_CD.MON'],input[value='WEEK_CD.WED'],input[value='WEEK_CD.FRI']").on("click", fnBlockClickEvent);
					break;
				case "GOODS_CYCLE_TP.4DAYS_PER_WEEK": // 월/화/수/목 고정
					$("input[value='WEEK_CD.MON']").prop('checked', true);
					$("input[value='WEEK_CD.TUE']").prop('checked', true);
					$("input[value='WEEK_CD.WED']").prop('checked', true);
					$("input[value='WEEK_CD.THU']").prop('checked', true);
					$("input[value='WEEK_CD.FRI']").prop('disabled', true);
					$("input[value='WEEK_CD.MON'],input[value='WEEK_CD.TUE'],input[value='WEEK_CD.WED'],input[value='WEEK_CD.THU']").on("click", fnBlockClickEvent);
					break;
				case "GOODS_CYCLE_TP.5DAYS_PER_WEEK": // 월/화/수/목/금 고정
					$("input[value='WEEK_CD.MON']").prop('checked', true);
					$("input[value='WEEK_CD.TUE']").prop('checked', true);
					$("input[value='WEEK_CD.WED']").prop('checked', true);
					$("input[value='WEEK_CD.THU']").prop('checked', true);
					$("input[value='WEEK_CD.FRI']").prop('checked', true);
					$("input[value='WEEK_CD.MON'],input[value='WEEK_CD.TUE'],input[value='WEEK_CD.WED'],input[value='WEEK_CD.THU'],input[value='WEEK_CD.FRI']").on("click", fnBlockClickEvent);
					break;
				case "GOODS_CYCLE_TP.6DAYS_PER_WEEK": // 월/화/수/목/금 고정
					$("input[value='WEEK_CD.MON']").prop('checked', true);
					$("input[value='WEEK_CD.TUE']").prop('checked', true);
					$("input[value='WEEK_CD.WED']").prop('checked', true);
					$("input[value='WEEK_CD.THU']").prop('checked', true);
					$("input[value='WEEK_CD.FRI']").prop('checked', true);
					$("input[value='WEEK_CD.MON'],input[value='WEEK_CD.TUE'],input[value='WEEK_CD.WED'],input[value='WEEK_CD.THU'],input[value='WEEK_CD.FRI']").on("click", fnBlockClickEvent);
					break;
				case "GOODS_CYCLE_TP.7DAYS_PER_WEEK": // 월/화/수/목/금 고정
					$("input[value='WEEK_CD.MON']").prop('checked', true);
					$("input[value='WEEK_CD.TUE']").prop('checked', true);
					$("input[value='WEEK_CD.WED']").prop('checked', true);
					$("input[value='WEEK_CD.THU']").prop('checked', true);
					$("input[value='WEEK_CD.FRI']").prop('checked', true);
					$("input[value='WEEK_CD.MON'],input[value='WEEK_CD.TUE'],input[value='WEEK_CD.WED'],input[value='WEEK_CD.THU'],input[value='WEEK_CD.FRI']").on("click", fnBlockClickEvent);
					break;
			}

		});

		$("input[name='goodsDailyCycleGreenJuiceWeekType']").on('click', function(e) {
			if($('input[name="goodsDailyCycleType"]').filter(':checked').val() == "GOODS_CYCLE_TP.1DAY_PER_WEEK"){
				$("input[name='goodsDailyCycleGreenJuiceWeekType']").not(this).prop('checked', false);
			}
		});


		// 베이비밀 -> 배송유형 변경
		$('[name="goodsDailyBulkYn"]').on('change', function(e) {
			let goodsDailyBulkYn = ($(this).filter(':checked').val());
			if(goodsDailyBulkYn == 'N'){
				$(".goodsDailyCycleType").css("display","");		// 배송주기
				$(".goodsDailyCycleTermType").css("display","");	// 배송기간
				$(".goodsDailyCycleGreenJuiceWeekType").css("display","");	// 배송요일
				$(".goodsBulkType").css("display","none");					// 세트수량
			}else{
				$(".goodsDailyCycleType").css("display","none");		// 배송주기
				$(".goodsDailyCycleTermType").css("display","none");	// 배송기간
				$(".goodsDailyCycleGreenJuiceWeekType").css("display","none");	// 배송요일
				$(".goodsBulkType").css("display","");					// 세트수량
			}
		});
	}

	function fnBlockClickEvent(e){
		e.preventDefault();
	}

	function fnBindEventClear(){
		$("input[name='goodsDailyCycleGreenJuiceWeekType']").prop('disabled', false);
		$("input[name='goodsDailyCycleGreenJuiceWeekType']").prop('checked', false);
		$("input[name='goodsDailyCycleGreenJuiceWeekType']").off("click", fnBlockClickEvent);
	}

	function fnSettingGoodsDailyCycleTermType(termList){
		let goodsDailyCycleTermTypeList = [];

		for(let i=0; i < termList.length; i++){
			let goodsDailyCycleTermTypeObj = new Object();
			goodsDailyCycleTermTypeObj.CODE = termList[i].goodsDailyCycleTermType;
			goodsDailyCycleTermTypeObj.NAME = termList[i].goodsDailyCycleTermTypeName;
			goodsDailyCycleTermTypeList[i] = goodsDailyCycleTermTypeObj;
		}

		return goodsDailyCycleTermTypeList;
	}

	function fnSettingGoodsDailyBulkType(bulkTypeList){
		let goodsDailyBulkTypeList = [];

		for(let i=0; i < bulkTypeList.length; i++){
			let goodsDailyBulkTypeObj = new Object();
			goodsDailyBulkTypeObj.CODE = bulkTypeList[i].goodsBulkType;
			goodsDailyBulkTypeObj.NAME = bulkTypeList[i].goodsBulkTypeName;
			goodsDailyBulkTypeList[i] = goodsDailyBulkTypeObj;
		}

		return goodsDailyBulkTypeList;
	}

	function fnInitOptionData(goodsDailyCycleList){
		$('[name="goodsDailyCycleType"]').filter(':checked').trigger("change", true);
		$('[name="goodsDailyBulkYn"]').filter(':checked').trigger("change", true);

		if(goodsDailyCycleList != null && goodsDailyCycleList != undefined){
			// 베이비밀 -> 식단유형-알러지대체식단이 불가능한 상품일 경우
			if(goodsDailyCycleList[0].goodsDailyAllergyYn == 'N') {
				$('input:radio[name="goodsDailyAllergyYn"]:input[value="Y"]').prop('disabled',true);
			}
		}

		// 베이비밀 -> 스토어배송권역이 택배만 가능한 경우
		if(paramData.storeDeliveryIntervalType == "STORE_DELIVERY_INTERVAL.PARCEL"){
			$('input:radio[name="goodsDailyBulkYn"]:input[value="N"]').prop('disabled',true);
		}
	}

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ----------------------------------

    // viewModel 초기화
    function fnViewModelInit(){};

    // 기본값 설정
    function fnDefaultSet(){};

    function fnSearch() {
    }

    function fnClear() {}

    function fnClose(){
    	parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
    }

    function fnSave(){

    	let dailyGoodsOptionChangeData = $('#dailyGoodsOptionChangeForm').formSerialize(true);
		parent.POP_PARAM["parameter"].dailyGoodsOptionChangeData = dailyGoodsOptionChangeData;

		fnKendoMessage({
			message: "변경되었습니다.", ok: function () {
				fnClose();
			}
		});

    }


	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};

	/** Common Clear*/
	$scope.fnClear =function(){	fnClear(); };
	$scope.fnSave =function(){	fnSave(); };
	$scope.fnClose = function( ){  fnClose();};

	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	//------------------------------- Validation End -------------------------------------
}); // document ready - END
