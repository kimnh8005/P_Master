﻿/**-----------------------------------------------------------------------------
 * system			: 우선할인 & 즉사할인 등록 팝업
 * @
 * @ 수정일		   수정자		  수정내용
 * @ ------------------------------------------------------
 * @ 2020.08.28	   손진구		  최초생성
 * @
 * **/
"use strict";

var paramData = parent.POP_PARAM["parameter"]; // 파라미터
var viewModel; // viewModel
var apprAdminGridDs, apprAdminGridOpt, apprAdminGrid;

$(document).ready(function() {

	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });

		fnPageInfo({
			PG_ID  : "priorityAndImmediatelyDiscountPopup",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){

		fnInitButton();
		fnViewModelInit();
		fnInitOptionBox();

		setTimeout( function(){
			fnDefaultValue();
		}, 100);

	};

	//--------------------------------- Button Start---------------------------------

	// 버튼 초기화
	function fnInitButton(){
		$("#fnSave, #fnAddDiscount").kendoButton();
	};

	//--------------------------------- Button End---------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){

		viewModel.fnGetApprovalStatus(); // 승인상태 공통코드 조회
		viewModel.fnGetDiscountMethodType(); // 할인유형 공통코드 조회
		viewModel.set("discountStartHourDropdownData", fbMakeTimeArr("hour"));
		viewModel.set("discountStartMinuteDropdownData", fbMakeTimeArr("min"));
		viewModel.set("discountEndHourDropdownData", fbMakeTimeArr("hour"));
		viewModel.set("discountEndMinuteDropdownData", fbMakeTimeArr("min"));
	};

	//---------------Initialize Option Box End ------------------------------------------------


	//-------------------------------  Common Function start -------------------------------

	// viewModel 초기화
	function fnViewModelInit(){
		viewModel = new kendo.data.ObservableObject({
			isAddDiscountEnabled : true,	// 할인추가 버튼 Enabled
			goodsPackageInfo : {
				goodsId : 0,				// 상품 ID
				discountTypeCode : "",		// 우선, 즉시 등 할인 유형 코드
				standardPrice : 0,			// 품목 원가
				recommendedPrice : 0,		// 품목 정상가
				taxYn : "N",				// 과세구분
				standardRowCount : 0,		// 임직원 기본할인 정보 상품구성 갯수
			},
			visibleGoodsPackageSaveBtn : false,	//저장 버튼
			apprDivVisible : false,
			goodsDiscountList : new kendo.data.DataSource({ // 할인 리스트
				schema: {
					model: {
						id: "goodsDiscountId",
						fields: {
							goodsId : { type: "number", editable: false }, // 상품 ID
							goodsDiscountApprId : { type: "number", editable: false }, // 상품할인승인 ID
							goodsDiscountId : { type: "number", editable: false }, // 상품할인 ID
							apprReqInfo : { type: "string", validation: { required: true } }, // 승인요청자정보
							apprReqNm : { type: "string", validation: { required: true } }, // 승인요청자명
							apprReqUserId : { type: "string", validation: { required: true } }, // 승인요청자 ID
							apprReqUserLoginId : { type: "string", validation: { required: true } }, // 승인요청자 LOGIN ID
							apprInfo : { type: "string", validation: { required: true } }, // 승인관리자정보
							apprSubNm : { type: "string", validation: { required: true } }, // 1차승인자명
							apprSubUserId : { type: "string", validation: { required: true } }, // 1차승인자 ID
							apprSubUserLoginId : { type: "string", validation: { required: true } }, // 1차승인자 LOGIN ID
							apprNm : { type: "string", validation: { required: true } }, // 2차승인자명
							apprUserId : { type: "string", validation: { required: true } }, // 2차승인자 ID
							apprUserLoginId : { type: "string", validation: { required: true } }, // 2차승인자 LOGIN ID
							discountTypeCode : { type: "string", validation: { required: true } }, // 상품할인코드
							approvalStatusCode : { type: "string", editable: false }, // 상품할인 승인상태코드
							discountStartDate : { type: "string", validation: { required: true } }, // 상품할인 시작일자
							discountStartDateOld : { type: "string" }, // 전 상품할인 시작일자
							discountStartHour : { type: "string", validation: { required: true } }, // 상품할인 시작시간
							discountStartHourOld : { type: "string" }, // 전 상품할인 시작시간
							discountStartMinute : { type: "string", validation: { required: true } }, // 상품할인 시작분
							discountStartMinuteOld : { type: "string" }, // 전 상품할인 시작분
							discountStartDateTime : { type: "string" }, // 상품할인 Full 시작일자
							discountEndDate : { type: "string", validation: { required: true } }, // 상품할인 종료일자
							discountEndDateOld : { type: "string" }, // 전 상품할인 종료일자
							discountEndDateOriginal : { type: "string" }, // 원본 상품할인 종료일자
							discountEndHour : { type: "string", validation: { required: true } }, // 상품할인 종료시간
							discountEndHourOld : { type: "string" }, // 전 상품할인 종료시간
							discountEndHourOriginal : { type: "string" }, // 원본 상품할인 종료시간
							discountEndMinute : { type: "string", validation: { required: true } }, // 상품할인 종료분
							discountEndMinuteOld : { type: "string" }, // 전 상품할인 종료분
							discountEndMinuteOriginal : { type: "string" }, // 원본 상품할인 종료분
							discountEndDateTime : { type: "string" }, // 상품할인 Full 종료일자
							discountMethodTypeCode : { type: "string", validation: { required: true } }, // 상품할인 유형코드
							recommendedPrice : { type: "number", editable: false }, // 품목 정상가
							standardPrice : { type: "number", editable: false }, // 품목 원가
							discountRatio : { type: "number", validation: { required: true } }, // 할인율
							discountRatioOld : { type: "number" }, // 전 할인율
							isDiscountRatioEnabled : { type: "boolean", editable: false }, // 할인율 Disabled
							discountAmount : { type: "number", editable: false }, // 할인액
							discountSalePrice : { type: "number", validation: { required: true } }, // 할인 판매가
							discountSalePriceOld : { type: "number" }, // 전 할인 판매가
							isDiscountSalePriceEnabled : { type: "boolean", editable: false }, // 할인 판매가 Enabled
							marginRate : { type: "number", editable: false } // 마진율
						}
					}
				}
			}),
			discountStartHourDropdownData : [], // 상품할인 시작시간 Dropdown 데이터
			discountStartMinuteDropdownData : [], // 상품할인 시작분 Dropdown 데이터
			discountEndHourDropdownData : [], // 상품할인 종료시간 Dropdown 데이터
			discountEndMinuteDropdownData : [], // 상품할인 종료분 Dropdown 데이터
			fnGetCodeName : function(code, dataName){ // 코드명 가져오기
				let dataList = viewModel.get(dataName);
				let codeName = "";

				for (let i = 0, dataListCount = dataList.length; i < dataListCount; i++) {
					if( dataList[i].CODE == code ) {
						codeName =  dataList[i].NAME;
						break;
					}
				}

				return codeName;
			},
			approvalStatusDropdownData : [], // 승인상태 Dropdown 데이터
			fnGetApprovalStatus : function(){ // 승인상태 공통코드 조회
				fnAjax({
					url	 : "/admin/comn/getCodeList",
					method : "GET",
					async : false,
					params  : {"stCommonCodeMasterCode" : "APPR_STAT", "useYn" : "Y"},
					success : function( data ){
						viewModel.set("approvalStatusDropdownData", data.rows);
					},
					error : function(xhr, status, strError){
						fnKendoMessage({ message : xhr.responseText });
					},
					isAction : "select"
				});
			},
			discountMethodTypeDropdownData : [],// 할인유형 Dropdown 데이터
			fnGetDiscountMethodType : function(){ // 할인유형 공통코드 조회
				fnAjax({
					url	 : "/admin/comn/getCodeList",
					method : "GET",
					async : false,
					params  : {"stCommonCodeMasterCode" : "GOODS_DISCOUNT_METHOD_TP", "useYn" : "Y"},
					success : function( data ){
						viewModel.set("discountMethodTypeDropdownData", data.rows);
					},
					error : function(xhr, status, strError){
						fnKendoMessage({ message : xhr.responseText });
					},
					isAction : "select"
				});
			},
			fnGetgoodsPackageInfoAndGoodsDiscountList : function(){ // 품목정보 및 상품할인리스트 조회
				viewModel.set("goodsPackageInfo", paramData.goodsPackageInfo);

				if( paramData.goodsDiscountList != null ){
					viewModel.goodsDiscountList.data(paramData.goodsDiscountList);
					viewModel.fnGoodsDiscountListSetting();
				}

				if( paramData.goodsDiscountApprList != null && paramData.goodsDiscountApprList.length > 0){
					viewModel.fnApprAdminInit();
					for (var i = 0; i < paramData.goodsDiscountApprList.length; i++) {
						apprAdminGridDs.add(paramData.goodsDiscountApprList[i]);
					}
				}
			},
			fnGoodsDiscountListSetting : function(){ // 상품할인리스트 설정
				let goodsDiscountList = viewModel.goodsDiscountList.data();

				goodsDiscountList.forEach(function(element, index)
				{
					viewModel.fnGoodsDiscountListValueSetting(index);

					// 이미 임시저장된 할인에 대해서 계산관련 경고 메세지를 또 띄우지 않기 위해서 변경처리
					//viewModel.fnDiscountCalculation(index);
					let dataItem = viewModel.goodsDiscountList.at(index);

					dataItem.isDiscountRatioEnabled = true;
					dataItem.isDiscountSalePriceEnabled = false;

					let rowGroupNum = dataItem.rowGroupNum;
					let rowAllNum = dataItem.rowCount+1;

					if(rowGroupNum != rowAllNum){	//합계 행은 계산하지 않는다.
						let discountAmount = dataItem.recommendedTotalPrice * (dataItem.discountRatio / 100);
						let discountSalePrice = (dataItem.recommendedTotalPrice - discountAmount) - ((dataItem.recommendedTotalPrice - discountAmount) % 10);

						dataItem.discountRatioOld = dataItem.discountRatio;
						dataItem.discountAmount = discountAmount;
						dataItem.discountSalePrice = discountSalePrice;
						dataItem.discountSalePriceOld = discountSalePrice;
					}
					// 이미 임시저장된 할인에 대해서 계산관련 경고 메세지를 또 띄우지 않기 위해서 변경처리
				});

				viewModel.fnAddDiscountButtonEnableSetting(false);
			},
			fnGoodsDiscountListValueSetting : function(rowIndex){ // 상품할인리스트 값 셋팅

				viewModel.fnSetDiscountStartDateOld(rowIndex, false);
				viewModel.fnSetDiscountEndDateOld(rowIndex, false);
				//viewModel.fnSetDiscountEndDateOriginal(rowIndex);
				viewModel.fnSetDiscountDefaultPrice(rowIndex, false);
			},
			fnSetDiscountDefaultPrice : function(rowIndex, syncAllow){ // 할인율, 할인판매가, 품목원가, 품목정상가 셋팅
				let dataItem = viewModel.goodsDiscountList.at(rowIndex);

				dataItem.discountRatioOld = dataItem.discountRatio;
				dataItem.discountSalePriceOld = dataItem.discountSalePrice;
				dataItem.standardPrice = dataItem.standardPrice;
				dataItem.recommendedPrice = dataItem.recommendedPrice;

				if(syncAllow){
					viewModel.goodsDiscountList.sync();
				}
			},
			fnGetRowDatePicker : function(rowIndex, objectName){ // datePicker 객체 가져오기
				return $("#goodsDiscountTbody tr").eq(rowIndex).find("[name=" + objectName + "]").data("kendoDatePicker");
			},
			fnDiscountStartDateChange : function(e){ // 시작일 변경
				e.preventDefault();

				let rowIndex = viewModel.goodsDiscountList.indexOf( e.data );
				let dataItem = viewModel.goodsDiscountList.at(rowIndex);

				// min, max 오버시
				if( e.sender._old == null && e.sender._oldText.indexOf("year-month-day") < 0 ){
					let discountStartDateAttr = viewModel.fnGetRowDatePicker(rowIndex, "discountStartDate");
					let discountStartDateMinDate = kendo.toString(discountStartDateAttr.min(), "yyyy-MM-dd");
					let discountStartDateMaxDate = kendo.toString(discountStartDateAttr.max(), "yyyy-MM-dd");
					fnKendoMessage({ message : "시작일은 " + discountStartDateMinDate + " ~ " + discountStartDateMaxDate + " 만 가능합니다." });
					viewModel.fnDiscountStartDateRecovery(rowIndex);
					return;
				}

				// 빈값 일 경우
				if( e.sender._old == null && e.sender._oldText.indexOf("year-month-day") >= 0 ){
					fnKendoMessage({ message : "시작일은 필수 입니다." });
					viewModel.fnDiscountStartDateRecovery(rowIndex);
					return;
				}

				dataItem.discountStartDate = kendo.toString(kendo.parseDate(dataItem.discountStartDate), "yyyy-MM-dd");
				viewModel.fnDiscountStartDateTimeCheckResultByDateChange( rowIndex );
			},
			fnDiscountStartHourChange : function(e){ // 시작시간 변경
				e.preventDefault();

				let rowIndex = viewModel.goodsDiscountList.indexOf( e.data );
				viewModel.fnDiscountStartDateTimeCheckResultByDateChange( rowIndex );
			},
			fnDiscountStartMinuteChange : function(e){ // 시작분 변경
				e.preventDefault();

				let rowIndex = viewModel.goodsDiscountList.indexOf( e.data );
				viewModel.fnDiscountStartDateTimeCheckResultByDateChange( rowIndex );
			},
			fnDiscountEndDateChange : function(e){ // 종료일 변경
				e.preventDefault();

				let rowIndex = viewModel.goodsDiscountList.indexOf( e.data );
				let dataItem = viewModel.goodsDiscountList.at(rowIndex);

				// min, max 오버시
				if( e.sender._old == null && e.sender._oldText.indexOf("year-month-day") < 0 ){
					let discountEndDateAttr = viewModel.fnGetRowDatePicker(rowIndex, "discountEndDate");
					let discountEndDateMinDate = kendo.toString(discountEndDateAttr.min(), "yyyy-MM-dd");
					let discountEndDateMaxDate = kendo.toString(discountEndDateAttr.max(), "yyyy-MM-dd");
					fnKendoMessage({ message : "종료일은 " + discountEndDateMinDate + " ~ " + discountEndDateMaxDate + " 만 가능합니다." });
					viewModel.fnDiscountEndDateRecovery(rowIndex);
					return;
				}

				// 빈값 일 경우
				if( e.sender._old == null && e.sender._oldText.indexOf("year-month-day") >= 0 ){
					fnKendoMessage({ message : "종료일은 필수 입니다." });
					viewModel.fnDiscountEndDateRecovery(rowIndex);
					return;
				}

				dataItem.discountEndDate = kendo.toString(kendo.parseDate(dataItem.discountEndDate), "yyyy-MM-dd");
				viewModel.fnDiscountEndDateTimeCheckResultByDateChange(rowIndex);
			},
			fnDiscountEndHourChange : function(e){ // 종료시간 변경
				e.preventDefault();

				let rowIndex = viewModel.goodsDiscountList.indexOf( e.data );
				viewModel.fnDiscountEndDateTimeCheckResultByDateChange(rowIndex);
			},
			fnDiscountEndMinuteChange : function(e){ // 종료분 변경
				e.preventDefault();

				let rowIndex = viewModel.goodsDiscountList.indexOf( e.data );
				viewModel.fnDiscountEndDateTimeCheckResultByDateChange(rowIndex);
			},
			fnDiscountMethodTypeChange : function(e){ // 할인유형 변경
				e.preventDefault();

				let rowIndex = viewModel.goodsDiscountList.indexOf( e.data );
				viewModel.fnDiscountRatioAndDiscountSalePriceInit(rowIndex);
			},
			fnDiscountRatioChange : function(e){ // 할인율 변경
				e.preventDefault();

				viewModel.fnDiscountRateBaseDiscountCalculation(e.data, true);
				viewModel.fnDiscountSalePriceSumCalculation();
			},
			fnDiscountSalePriceSumCalculation : function(){	//할인율 변경시 임직원 할인가 합계 계산
				//임직원 할인가 합계 계산
				let recommendedPriceSum = 0;		//정상가 총합 초기화
				let goodsQuantitySum = 0;			//구성수량 총합 초기화
				let recommendedTotalPriceSum = 0;	//정상가총액의 총합 초기화
				let discountSalePriceSum = 0;		//임직원 할인가 총합 초기화

				let goodsDiscountList = viewModel.goodsDiscountList.data();

				if(goodsDiscountList.length > 0) {
					for(let i=0, rowCount=goodsDiscountList.length; i < rowCount; i++){
						if(goodsDiscountList[i].goodsDiscountId == null && i != goodsDiscountList.length-1) {		//신규 추가된 임직원 할인 합계(마지막 합계라인은 계산에서 제외)
							recommendedPriceSum += goodsDiscountList[i].recommendedPrice;
							goodsQuantitySum += goodsDiscountList[i].goodsQuantity;
							recommendedTotalPriceSum += goodsDiscountList[i].recommendedTotalPrice;
							discountSalePriceSum += goodsDiscountList[i].discountSalePrice;
						}
					}

					let goodsDiscountListLastSum = goodsDiscountList[goodsDiscountList.length-1];					//신규 추가된 마지막 합계행을 지정

					goodsDiscountListLastSum.recommendedPrice = recommendedPriceSum;
					goodsDiscountListLastSum.goodsQuantity = goodsQuantitySum;
					goodsDiscountListLastSum.recommendedTotalPrice = recommendedTotalPriceSum;
					goodsDiscountListLastSum.discountSalePrice = discountSalePriceSum;

					viewModel.goodsDiscountList.sync();
				}
			},
			fnDiscountSalePriceChange : function(e){ // 판매가 변경
				e.preventDefault();

				viewModel.fnDiscountSalePriceBaseDiscountCalculation(e.data);
			},
			fnGetDiscountStartMinDate : function( rowIndex ){ // 시작일 min 가져오기
				let standardRowCount = viewModel.goodsPackageInfo.standardRowCount;		//임직원 기본할인 정보 상품구성 갯수
				let prevDataItem = viewModel.goodsDiscountList.at(rowIndex - standardRowCount - 1);
				let dataItem = viewModel.goodsDiscountList.at(rowIndex);

				if( prevDataItem != undefined ){
					let prevDiscountEndDateTime = kendo.parseDate( (prevDataItem.discountEndDate + " " + prevDataItem.discountEndHour + ":" + prevDataItem.discountEndMinute), "yyyy-MM-dd HH:mm" );
					prevDiscountEndDateTime.setMinutes(prevDiscountEndDateTime.getMinutes() + 1);
					return kendo.parseDate( kendo.toString(prevDiscountEndDateTime, "yyyy-MM-dd"), "yyyy-MM-dd" );
				}else{
					return kendo.parseDate( fnGetToday(), "yyyy-MM-dd" );
				}
			},
			fnGetDiscountStartMaxDate : function( rowIndex ){ // 시작일 max 가져오기
				let dataItem = viewModel.goodsDiscountList.at(rowIndex);
				return kendo.parseDate( dataItem.discountEndDate, "yyyy-MM-dd" );
			},
			fnDiscountStartDateMinSetting : function( rowIndex ){ // 시작일 최소 선택가능 일자 설정
				let discountStartDateAttr = viewModel.fnGetRowDatePicker(rowIndex, "discountStartDate");
				discountStartDateAttr.min( viewModel.fnGetDiscountStartMinDate(rowIndex) );
			},
			fnDiscountStartDateMaxSetting : function( rowIndex ){ // 시작일 최대 선택가능 일자 설정
				let discountStartDateAttr = viewModel.fnGetRowDatePicker(rowIndex, "discountStartDate");
				discountStartDateAttr.max( viewModel.fnGetDiscountStartMaxDate( rowIndex ) );
			},
			fnDiscountStartDateTimeCheck : function( rowIndex ){ // 시작일 일자, 시간, 분 체크
				let standardRowCount = viewModel.goodsPackageInfo.standardRowCount;		//임직원 기본할인 정보 상품구성 갯수
				let prevDataItem = viewModel.goodsDiscountList.at(rowIndex - standardRowCount - 1);
				let dataItem = viewModel.goodsDiscountList.at(rowIndex);
				let nowDateTime = kendo.parseDate( fnGetToday("yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
				let startDateTime = kendo.parseDate((dataItem.discountStartDate + " " + dataItem.discountStartHour + ":" + dataItem.discountStartMinute), "yyyy-MM-dd HH:mm");
				let endDateTime = kendo.parseDate((dataItem.discountEndDate + " " + dataItem.discountEndHour + ":" + dataItem.discountEndMinute), "yyyy-MM-dd HH:mm");

				if( dataItem.approvalStatusCode == "APPR_STAT.NONE" ){
					if( prevDataItem == undefined ){
						if( nowDateTime.getTime() >= startDateTime.getTime() ){
							fnKendoMessage({ message : "시작일은 현재 이후 시간만 등록 가능합니다." });
							return false;
						}else if( startDateTime.getTime() >= endDateTime.getTime() ){
							fnKendoMessage({ message : "현재행사 종료일보다 시작일이 크거나 같습니다." });
							return false;
						}else{
							return true;
						}
					}else{
						let prevDiscountEndDateTime = kendo.parseDate( (prevDataItem.discountEndDate + " " + prevDataItem.discountEndHour + ":" + prevDataItem.discountEndMinute), "yyyy-MM-dd HH:mm" );

						if( prevDiscountEndDateTime.getTime() > startDateTime.getTime() ){
							fnKendoMessage({ message : "이전행사 종료일보다 시작일이 작습니다." });
							return false;
						}else if( startDateTime.getTime() >= endDateTime.getTime() ){
							fnKendoMessage({ message : "현재행사 종료일보다 시작일이 크거나 같습니다." });
							return false;
						}else{
							return true;
						}
					}
				}else{
					return true;
				}

			},
			fnDiscountStartDateTimeCheckResultByDateChange : function(rowIndex){ // 시작일 일자, 시간, 분 체크 결과에 따른 시작일 정보 변경

				if( viewModel.fnDiscountStartDateTimeCheck(rowIndex) ){
					viewModel.fnSetDiscountStartDateOld(rowIndex, true);
					viewModel.fnDiscountEndDateMinSetting(rowIndex);
				}else{
					viewModel.fnDiscountStartDateRecovery(rowIndex);
				}
			},
			fnDiscountStartDateRecovery : function( rowIndex ){ // 시작일 복구
				let dataItem = viewModel.goodsDiscountList.at(rowIndex);

				dataItem.discountStartDate = dataItem.discountStartDateOld;
				dataItem.discountStartHour = dataItem.discountStartHourOld;
				dataItem.discountStartMinute = dataItem.discountStartMinuteOld;

				viewModel.goodsDiscountList.sync();
			},
			fnSetDiscountStartDateOld : function(rowIndex, syncAllow){ // 시작일 old 셋팅
				let dataItem = viewModel.goodsDiscountList.at(rowIndex);

				dataItem.discountStartDateOld = dataItem.discountStartDate;
				dataItem.discountStartHourOld = dataItem.discountStartHour;
				dataItem.discountStartMinuteOld = dataItem.discountStartMinute;

				if(syncAllow){
					viewModel.goodsDiscountList.sync();
				}
			},
			fnGetDiscountEndMinDate : function( rowIndex ){ // 종료일 min 가져오기
				let dataItem = viewModel.goodsDiscountList.at(rowIndex);
				let nowDateTime = kendo.parseDate( fnGetToday("yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
				let startDateTime = kendo.parseDate((dataItem.discountStartDate + " " + dataItem.discountStartHour + ":" + dataItem.discountStartMinute), "yyyy-MM-dd HH:mm");

				if( dataItem.approvalStatusCode == "APPR_STAT.NONE" ){
					return kendo.parseDate( dataItem.discountStartDate, "yyyy-MM-dd" );
				}else if( dataItem.approvalStatusCode == "APPR_STAT.APPROVED" ){
					if( startDateTime.getTime() > nowDateTime.getTime() ){ // 행사가 시작 안되었을 경우
						return kendo.parseDate( dataItem.discountStartDate, "yyyy-MM-dd" );
					}else{ // 행사가 시작 되었을 경우
						return kendo.parseDate( fnGetToday(), "yyyy-MM-dd" );
					}
				}
			},
			fnGetDiscountEndMaxDate : function( rowIndex ){ // 종료일 max 가져오기
				let dataItem = viewModel.goodsDiscountList.at(rowIndex);

				if( dataItem.approvalStatusCode == "APPR_STAT.APPROVED" ){
					return kendo.parseDate( dataItem.discountEndDateOriginal, "yyyy-MM-dd" );
				}else{
					return kendo.parseDate( "2090-12-31", "yyyy-MM-dd");
				}
			},
			fnDiscountEndDateMinSetting : function( rowIndex ){ // 종료일 최소 선택가능 일자 설정
				let discountEndDateAttr = viewModel.fnGetRowDatePicker(rowIndex, "discountEndDate");

				discountEndDateAttr.min( viewModel.fnGetDiscountEndMinDate(rowIndex) );
			},
			fnDiscountEndDateMaxSetting : function( rowIndex ){ // 종료일 최대 선택가능 일자 설정
				let discountEndDateAttr = viewModel.fnGetRowDatePicker(rowIndex, "discountEndDate");

				discountEndDateAttr.max( viewModel.fnGetDiscountEndMaxDate(rowIndex) );
			},
			fnDiscountEndDateUpdateCheck : function(rowIndex){	//기간수정 대상 종료일 일자, 시간, 분 체크
				let dataItem = viewModel.goodsDiscountList.at(rowIndex);
				let standardRowCount = viewModel.goodsPackageInfo.standardRowCount;		//임직원 기본할인 정보 상품구성 갯수
				let nextDataItem = viewModel.goodsDiscountList.at(rowIndex+standardRowCount+1);

				let nextStartDateTime = null

				let nowDateTime = kendo.parseDate( fnGetToday("yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
				let startDateTime = kendo.parseDate((dataItem.discountStartDate + " " + dataItem.discountStartHour + ":" + dataItem.discountStartMinute), "yyyy-MM-dd HH:mm");
				let endDateTime = kendo.parseDate((dataItem.discountEndDate + " " + dataItem.discountEndHour + ":" + dataItem.discountEndMinute), "yyyy-MM-dd HH:mm");
				let originalEndDateTime = kendo.parseDate((dataItem.discountEndDateOriginal + " " + dataItem.discountEndHourOriginal + ":" + dataItem.discountEndMinuteOriginal), "yyyy-MM-dd HH:mm");
				if(nextDataItem != undefined) {
					nextStartDateTime = kendo.parseDate((nextDataItem.discountStartDate + " " + nextDataItem.discountStartHour + ":" + nextDataItem.discountStartMinute), "yyyy-MM-dd HH:mm");
				}

				if( dataItem.approvalStatusCode == "APPR_STAT.APPROVED" ){ // 승인상태 승인완료
					if( nowDateTime.getTime() > endDateTime.getTime() ){
						fnKendoMessage({ message : "종료일이 현재시간보다 작습니다." });
						return false;
					}else if( endDateTime.getTime() > originalEndDateTime.getTime() ){
						fnKendoMessage({ message : "등록한 종료일 이전 기간으로 수정하실 수 있습니다" });
						return false;
					}else if(nextDataItem != undefined && nextStartDateTime.getTime() <= endDateTime.getTime()){
						fnKendoMessage({ message : "다음 시작일 이전으로만 등록 가능합니다" });
						return false;
					}else{
						return true;
					}
				} else if( dataItem.approvalStatusCode == "APPR_STAT.NONE" ){ // 승인상태 승인대기
					if( startDateTime.getTime() > endDateTime.getTime() ){
						fnKendoMessage({ message : "종료일이 시작일보다 작습니다." });
						return false;
					}else{
						return true;
					}
				}
			},
			fnDiscountEndDateTimeCheck : function( rowIndex ){ // 종료일 일자, 시간, 분 체크
				let dataItem = viewModel.goodsDiscountList.at(rowIndex);
				let startDateTime = kendo.parseDate((dataItem.discountStartDate + " " + dataItem.discountStartHour + ":" + dataItem.discountStartMinute), "yyyy-MM-dd HH:mm");
				let endDateTime = kendo.parseDate((dataItem.discountEndDate + " " + dataItem.discountEndHour + ":" + dataItem.discountEndMinute), "yyyy-MM-dd HH:mm");

				if( dataItem.approvalStatusCode == "APPR_STAT.APPROVED" ){ // 승인상태 승인완료
					let nowDateTime = kendo.parseDate( fnGetToday("yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
					let originalEndDateTime = kendo.parseDate((dataItem.discountEndDateOriginal + " " + dataItem.discountEndHourOriginal + ":" + dataItem.discountEndMinuteOriginal), "yyyy-MM-dd HH:mm");

					if( startDateTime.getTime() > nowDateTime.getTime() ){ // 행사가 시작 안되었을 경우
						if( startDateTime.getTime() >= endDateTime.getTime() ){
							fnKendoMessage({ message : "종료일이 시작일보다 작거나 같습니다." });
							return false;
						}else if( endDateTime.getTime() > originalEndDateTime.getTime() ){
							fnKendoMessage({ message : "종료일이 원본 종료일보다 큽니다." });
							return false;
						}else{
							return true;
						}
					}else{ // 행사가 시작되었을 경우
						if( endDateTime.getTime() <= nowDateTime.getTime()){	// 이미 기간이 지난 할인 내역의 경우 Validation 체크에서 제외처리 한다.
							return true;
						}
						else {
							return true;
//							if( nowDateTime.getTime() > endDateTime.getTime() ){
//								fnKendoMessage({ message : "종료일이 현재시간보다 작습니다." });
//								return false;
//							}else if( endDateTime.getTime() > originalEndDateTime.getTime() ){
//								fnKendoMessage({ message : "등록한 종료일 이전 기간으로 수정하실 수 있습니다" });
//								return false;
//							}else{
//								return true;
//							}
						}
					}

				}else if( dataItem.approvalStatusCode == "APPR_STAT.NONE" ){ // 승인상태 승인대기
					if( startDateTime.getTime() >= endDateTime.getTime() ){
						fnKendoMessage({ message : "종료일이 시작일보다 작거나 같습니다." });
						return false;
					}else{
						return true;
					}
				}
			},
			fnDiscountEndDateTimeCheckResultByDateChange : function(rowIndex){ // 종료일 일자, 시간, 분 체크 결과에 따른 종료일 정보 변경

				if( viewModel.fnDiscountEndDateTimeCheck(rowIndex) ){
					viewModel.fnSetDiscountEndDateOld(rowIndex, true);
					viewModel.fnNextDiscountStartDateMinSetting(rowIndex);
				}else{
					viewModel.fnDiscountEndDateRecovery(rowIndex);
				}
			},
			fnDiscountEndDateRecovery : function( rowIndex ){ // 종료일 복구
				let dataItem = viewModel.goodsDiscountList.at(rowIndex);

				dataItem.discountEndDate = dataItem.discountEndDateOld;
				dataItem.discountEndHour = dataItem.discountEndHourOld;
				dataItem.discountEndMinute = dataItem.discountEndMinuteOld;

				viewModel.goodsDiscountList.sync();
			},
			fnSetDiscountEndDateOld : function(rowIndex, syncAllow){ // 종료일 old 셋팅
				let dataItem = viewModel.goodsDiscountList.at(rowIndex);

				dataItem.discountEndDateOld = dataItem.discountEndDate;
				dataItem.discountEndHourOld = dataItem.discountEndHour;
				dataItem.discountEndMinuteOld = dataItem.discountEndMinute;

				if(syncAllow){
					viewModel.goodsDiscountList.sync();
				}
			},
			fnSetDiscountEndDateOriginal : function(rowIndex){ // 종료일 Original 셋팅
				let dataItem = viewModel.goodsDiscountList.at(rowIndex);

				dataItem.discountEndDateOriginal = dataItem.discountEndDate;
				dataItem.discountEndHourOriginal = dataItem.discountEndHour;
				dataItem.discountEndMinuteOriginal = dataItem.discountEndMinute;

				viewModel.goodsDiscountList.sync();
			},
			fnNextDiscountStartDateMinSetting : function(rowIndex){ // 다음 행사에 승인대기인 행사가 있을 경우 시작일 min 설정
				let baseLength = paramData.goodsPackageBaseDiscountEmployeeList.length;		// 해당 묶음상품의 구성상품 갯수+합계행 갯수
				let nextDataItem = viewModel.goodsDiscountList.at(rowIndex + baseLength);	// baseLength만큼 더해야 다음행으로 처리 가능

				if( nextDataItem != undefined && nextDataItem.approvalStatusCode == "APPR_STAT.NONE" ){
					viewModel.fnDiscountStartDateMinSetting(rowIndex + baseLength);
				}
			},
			fnDiscountRatioAndDiscountSalePriceInit : function(rowIndex){ // 할인율, 판매가 초기화
				let dataItem = viewModel.goodsDiscountList.at(rowIndex);

				dataItem.discountRatio = 0;
				dataItem.discountRatioOld = 0;
				dataItem.discountSalePrice = dataItem.recommendedTotalPrice;
				dataItem.discountSalePriceOld = dataItem.recommendedTotalPrice;

				viewModel.fnDiscountCalculation(rowIndex);

				viewModel.goodsDiscountList.sync();
			},
			fnDiscountCalculation : function(rowIndex){ // 할인유형에 따른 할인계산(임직원 할인은 무조건 정률할인)
				let dataItem = viewModel.goodsDiscountList.at(rowIndex);

				dataItem.isDiscountRatioEnabled = true;
				dataItem.isDiscountSalePriceEnabled = false;

				viewModel.fnDiscountRateBaseDiscountCalculation(dataItem, false);
			},
			fnDiscountRateBaseDiscountCalculation : function(dataItem, syncAllow){ // 할인율 기준 할인계산

				if( dataItem.discountRatio == null || dataItem.discountRatio > 99 ){
					setTimeout(function() {
						fnKendoMessage({ message : "할인율은 100%이하로 설정해야 합니다." });
					});
					dataItem.discountRatio = dataItem.discountRatioOld;
					viewModel.goodsDiscountList.sync();
					return;
				}

				let rowGroupNum = dataItem.rowGroupNum;
				let rowAllNum = dataItem.rowCount+1;

				if(rowGroupNum != rowAllNum){	//합계 행은 계산하지 않는다.
					let discountSalePrice = dataItem.recommendedPrice - (dataItem.recommendedPrice * dataItem.discountRatio / 100);
					discountSalePrice = discountSalePrice - (discountSalePrice % 10);

					let totalDiscountSalePrice = discountSalePrice * dataItem.goodsQuantity;

					let discountAmount = dataItem.recommendedPrice - discountSalePrice

					dataItem.discountRatioOld = dataItem.discountRatio;
					dataItem.discountAmount = discountAmount;
					dataItem.discountSalePrice = totalDiscountSalePrice;
					dataItem.discountSalePriceOld = totalDiscountSalePrice;
				}
				viewModel.fnMarginCalculation(dataItem, syncAllow);
			},
			fnMarginCalculation : function(dataItem, syncAllow){ // 마진율 계산
				let marginRate = 0;
				let taxRate = dataItem.taxYn == "Y" ? 1.1 : 1;

				marginRate = Math.floor( (dataItem.discountSalePrice - dataItem.standardPrice * taxRate) / dataItem.discountSalePrice * 100 );
				dataItem.marginRate = marginRate;

				if(syncAllow){
					viewModel.goodsDiscountList.sync();
				}

				if( dataItem.approvalStatusCode == "APPR_STAT.NONE" ){
					let messageText = "";

					if( dataItem.discountRatio > 50 ){
						messageText += dataItem.goodsName + "상품의 할인율이 50%를 초과하였습니다.";
					}

					if( messageText != "" ){
						messageText += "<br>";
					}

					if( dataItem.marginRate < 16 ){
						messageText += dataItem.goodsName + "상품의 마진율이 16% 미만입니다.";
					}

					if( messageText != "" ){
						setTimeout(function() {
							fnKendoMessage({ message : messageText });
						});
					}
				}
			},
			fnAddDiscountButtonEnableSetting : function(syncAllow){ // 할인추가 enable 설정
				let goodsDiscountList = viewModel.goodsDiscountList.data();
				let isAddDiscountEnabled = true;

				for(let i=0, rowCount=goodsDiscountList.length; i < rowCount; i++){
					if( goodsDiscountList[i].approvalStatusCode != "APPR_STAT.APPROVED" ){
						isAddDiscountEnabled = false;
						break;
					}
				}

				viewModel.set("isAddDiscountEnabled", isAddDiscountEnabled);
				if(syncAllow){
					viewModel.goodsDiscountList.sync();
				}
			},
			fnGetDiscountStartDateTime : function(){ // 시작일자 가져오기
				let rowIndex = viewModel.goodsDiscountList.total() - 1;
				let dataItem = viewModel.goodsDiscountList.at(rowIndex);

				if( dataItem != undefined ){
					let endDateTime = kendo.parseDate((dataItem.discountEndDate + " " + dataItem.discountEndHour + ":" + dataItem.discountEndMinute), "yyyy-MM-dd HH:mm");
					endDateTime.setMinutes(endDateTime.getMinutes() + 60);

					return endDateTime;
				}else{
					let nowDateTime = kendo.parseDate( fnGetToday("yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
					nowDateTime.setMinutes(nowDateTime.getMinutes() + 60);

					return nowDateTime;
				}
			},
			fnAddDiscount : function(e){ // 할인 추가
				e.preventDefault();

				let discountStartDateTime = viewModel.fnGetDiscountStartDateTime();
				let discountEndDate = fnGetDayAdd(kendo.toString(discountStartDateTime, "yyyy-MM-dd"), 7, "yyyy-MM-dd");
				let discountMethodTypeCode = "GOODS_DISCOUNT_METHOD_TP.FIXED_PRICE";

				if(viewModel.goodsPackageInfo.discountTypeCode == "GOODS_DISCOUNT_TP.EMPLOYEE"){	//임직원 할인은 정률할인으로 처리
					discountMethodTypeCode = "GOODS_DISCOUNT_METHOD_TP.FIXED_RATE";
				}

				//console.log("paramData.goodsPackageBaseDiscountEmployeeList", paramData.goodsPackageBaseDiscountEmployeeList);

				if(paramData.goodsPackageBaseDiscountEmployeeList.length > 0) {
					let recommendedPriceSum = 0;		//정상가 총합 초기화
					let goodsQuantitySum = 0;			//구성수량 총합 초기화
					let recommendedTotalPriceSum = 0;	//정상가총액의 총합 초기화
					let discountSalePriceSum = 0;		//임직원 할인가 총합 초기화
					let goodsDiscountListLength = 0;	//현재 저장된 할인 리스트 갯수

					if(viewModel.goodsDiscountList.data()){
						goodsDiscountListLength = viewModel.goodsDiscountList.data().length;
					}

					for(var i=0; i < paramData.goodsPackageBaseDiscountEmployeeList.length; i++) {
						var goodsPackageBaseDiscountEmployeeList = paramData.goodsPackageBaseDiscountEmployeeList[i];

						recommendedPriceSum += goodsPackageBaseDiscountEmployeeList.recommendedPrice;
						goodsQuantitySum += goodsPackageBaseDiscountEmployeeList.goodsQuantity;
						recommendedTotalPriceSum += goodsPackageBaseDiscountEmployeeList.recommendedTotalPrice;
						discountSalePriceSum += goodsPackageBaseDiscountEmployeeList.discountSalePrice;

						viewModel.goodsDiscountList.add({
							goodsId : viewModel.goodsPackageInfo.goodsId,
							goodsDiscountId : null,
							ilGoodsPackageGoodsMappingId : goodsPackageBaseDiscountEmployeeList.ilGoodsPackageGoodsMappingId,
							rowNum : goodsDiscountListLength+goodsPackageBaseDiscountEmployeeList.rowNum,
							rowGroupNum : goodsPackageBaseDiscountEmployeeList.rowGroupNum,
							rowCount : goodsPackageBaseDiscountEmployeeList.rowCount,
							approvalStatusCode : "APPR_STAT.NONE",
							discountStartDate : kendo.toString(discountStartDateTime, "yyyy-MM-dd"),
							discountStartDateOld : kendo.toString(discountStartDateTime, "yyyy-MM-dd"),
							discountStartHour : kendo.toString(discountStartDateTime, "HH"),
							discountStartHourOld : kendo.toString(discountStartDateTime, "HH"),
							discountStartMinute : kendo.toString(discountStartDateTime, "mm"),
							discountStartMinuteOld : kendo.toString(discountStartDateTime, "mm"),
							discountEndDate : discountEndDate,
							discountEndDateOld : discountEndDate,
							discountEndDateOriginal : discountEndDate,
							discountEndHour : "23",
							discountEndHourOld : "23",
							discountEndHourOriginal : "23",
							discountEndMinute : "59",
							discountEndMinuteOld : "59",
							discountEndMinuteOriginal : "59",
							goodsName : goodsPackageBaseDiscountEmployeeList.goodsName,
							discountTypeCode : viewModel.goodsPackageInfo.discountTypeCode,
							discountMethodTypeCode : discountMethodTypeCode,
							standardPrice : goodsPackageBaseDiscountEmployeeList.standardPrice,
							recommendedPrice : goodsPackageBaseDiscountEmployeeList.recommendedPrice,
							goodsQuantity : goodsPackageBaseDiscountEmployeeList.goodsQuantity,
							recommendedTotalPrice : goodsPackageBaseDiscountEmployeeList.recommendedTotalPrice,
							discountRatio : goodsPackageBaseDiscountEmployeeList.discountRatio,
							discountRatioOld : goodsPackageBaseDiscountEmployeeList.discountRatio,
							isDiscountRatioEnabled : true,
							discountAmount : 0,
							discountSalePrice : goodsPackageBaseDiscountEmployeeList.discountSalePrice,
							discountSalePriceOld : goodsPackageBaseDiscountEmployeeList.discountSalePrice,
							isDiscountSalePriceEnabled : false,
							marginRate : 0
						});

						//let rowIndex = viewModel.goodsDiscountList.total() - 1;

						viewModel.fnDiscountCalculation(i);
						viewModel.fnAddDiscountButtonEnableSetting(false);
						viewModel.fnApprAdminInit();	//승인관리자 정보 Grid
					}
					viewModel.goodsDiscountList.sync();
					viewModel.set("visibleGoodsPackageSaveBtn", true);	//저장 Button Visible

					$("#goodsDiscountTbody tr.k-footer-template th").css('font-size', '13px');
				}
			},
			fnDiscountEndDateUpdateRow : function(e){	//기간수정(즉시할인, 진행중인 할인내역 종료일 업데이트)
				e.preventDefault();

				let rowIndex = viewModel.goodsDiscountList.indexOf( e.data );

				let message = "종료일이 바로 수정 반영 됩니다. 진행하시겠습니까?";

				if(!viewModel.fnDiscountEndDateUpdateCheck(rowIndex)){
					return false;
				}

				fnKendoMessage({
					type	: "confirm",
					message : message,
					ok	  : function(){

//						console.log("e.data : ", e.data);
//						return;

						let discountEndDate = e.data.discountEndDate + " " + e.data.discountEndHour + ":" + e.data.discountEndMinute;

						if( e.data.goodsDiscountId != null && e.data.goodsDiscountId > 0 ){
							fnAjax({
								url	 : "/admin/goods/discount/discountEndDateUpdate",
								params  : { goodsDiscountId : e.data.goodsDiscountId, discountEndDate : discountEndDate },
								method : "GET",
								contentType : "application/json;charset=UTF-8",
								success : function( data ){
									viewModel.set("visibleGoodsPackageSaveBtn", false);	//저장 Button Visible
									viewModel.set("isAddDiscountEnabled", true);		// 승인 대기 상태에서 삭제 됐으면 추가 등록 버튼 다시 활성화 처리

									let rowIndex = viewModel.goodsDiscountList.indexOf( e.data );

//									console.log("rowIndex : ", rowIndex);
//									console.log("discountEndDate : ", discountEndDate);

									let discountEndDateAttr = viewModel.fnGetRowDatePicker(rowIndex, "discountEndDate");
									discountEndDateAttr.max(discountEndDate);

									viewModel.fnGoodsPackageBaseDiscountEndDateUpdate(data, "dateUpdate");				//묶음 기본 할인 날짜 업데이트 및 할인 내역 리스트 동기화

									if(data){
										fnKendoMessage({ message : "수정일이 반영되었습니다." });
									}
									//console.log("e.data : ",  e.data);
								},
								error : function(xhr, status, strError){
									fnKendoMessage({ message : xhr.responseText });
								},
								isAction : "update"
							});
						}
					},
					cancel  : function(){

					}
				});
			},
			fnDiscountDeleteRow : function(e){ // ROW 할인 삭제
				e.preventDefault();

				let message = "삭제 시 진행예정인 승인완료건이 자동 폐기됩니다. 진행하시겠습니까?";

				if(e.data.approvalStatusCode == "APPR_STAT.NONE") {
					message = "삭제하시겠습니까?";
				}
				else if(e.data.approvalStatusCode == "APPR_STAT.REQUEST"){
					message = "삭제 시 요청철회됩니다. 진행하시겠습니까?";
				}

				fnKendoMessage({
					type	: "confirm",
					message : message,
					ok	  : function(){
						viewModel.fnApprClear();																//승인관리자 정보 삭제
						viewModel.set("apprDivVisible", false);
						viewModel.set("visibleGoodsPackageSaveBtn", false);

						if( e.data.goodsDiscountApprId != null && e.data.goodsDiscountApprId > 0 ){
							viewModel.fnDiscountDelete(e.data);
						}else{
							viewModel.fnGoodsDiscountDatasourceRemove(e.data);
							viewModel.fnGoodsPackageBaseDiscountEndDateUpdate(e.data, "delete");				//묶음 기본 할인 날짜 업데이트 및 할인 내역 리스트 동기화
						}
					},
					cancel  : function(){

					}
				});
			},
			fnDiscountDelete : function( goodsDiscountInfo ){ // 할인 삭제
				fnAjax({
					url	 : "/admin/goods/discount/deleteGoodsDiscount",
					params  : {
								goodsId : goodsDiscountInfo.goodsId
							,	goodsDiscountApprId : goodsDiscountInfo.goodsDiscountApprId
							,	goodsDiscountId : goodsDiscountInfo.goodsDiscountId
							,	discountTypeCode : goodsDiscountInfo.discountTypeCode
							},
					method : "GET",
					contentType : "application/json;charset=UTF-8",
					success : function( data ){
						viewModel.fnGoodsDiscountDatasourceRemove(goodsDiscountInfo);
						viewModel.fnGoodsPackageBaseDiscountEndDateUpdate(data, "delete");				//묶음 기본 할인 날짜 업데이트 및 할인 내역 리스트 동기화
					},
					fail : function(data, result) {
						fnKendoMessage({
							message : result.message,
							ok : function(e) {
								if(result.code != "3000"){
									viewModel.fnGoodsDiscountDatasourceRemove(goodsDiscountInfo);					//삭제 처리한 행 화면에서 삭제
									viewModel.fnGoodsPackageBaseDiscountEndDateUpdate(data, "delete");				//묶음 기본 할인 날짜 업데이트 및 할인 내역 리스트 동기화
								}
							}
						});
					},
					error : function(xhr, status, strError){
						fnKendoMessage({ message : xhr.responseText });
					},
					isAction : "update"
				});
			},
			fnGoodsPackageBaseDiscountEndDateUpdate : function ( data, mode ){
				let goodsDiscountList = viewModel.goodsDiscountList.data();		//할인내역 마스터 리스트
				let goodsDiscountApprList = $("#apprGrid").data("kendoGrid");
				let params = {};

				if(goodsDiscountApprList != undefined){
					goodsDiscountApprList = goodsDiscountApprList.dataSource.data()
				}

				//console.log("goodsDiscountList : ", goodsDiscountList);

				if(goodsDiscountList.length > 0){

					let standardRowCount = viewModel.goodsPackageInfo.standardRowCount+1;
					let goodsDiscountListLength = goodsDiscountList.length;

//					console.log("goodsDiscountListLength : ", goodsDiscountListLength);
//					console.log("standardRowCount : ", standardRowCount);

					for(let i = (goodsDiscountListLength-1); i >= (goodsDiscountListLength-standardRowCount); i--) {
						//console.log("i : ", i);
						if(goodsDiscountList[i].approvalStatusCode == "APPR_STAT.NONE") {			//승인 대기 상태로 아직 저장하지 않은 상태의 할인 내역도 삭제 처리 한다
							viewModel.goodsDiscountList.remove(goodsDiscountList[i]);				// viewModel 에서 삭제
							viewModel.set("isAddDiscountEnabled", true);							// 승인 대기 상태에서 삭제 됐으면 추가 등록 버튼 다시 활성화 처리
						}
					}
				}
				viewModel.goodsDiscountList.sync();

				if(goodsDiscountList.length > 0){
					for(let i = (goodsDiscountList.length-1); i >= 0; i--) {
						//console.log("goodsDiscountList : ", goodsDiscountList);
						goodsDiscountList[i].discountStartDateTime = goodsDiscountList[i].discountStartDate + " " + goodsDiscountList[i].discountStartHour + ":" + goodsDiscountList[i].discountStartMinute;

						if(data.goodsDiscountId != null && goodsDiscountList[i].goodsDiscountId == data.goodsDiscountId) {		//삭제 처리한 Proc에서 가져온 이전행의 할인내역ID와 같다면
							//종료일자 관련 항목 업데이트
							goodsDiscountList[i].discountEndDate = data.discountEndDate;				// 상품할인 종료일자
							goodsDiscountList[i].discountEndDateOld  = data.discountEndDate;			// 전 상품할인 종료일자
							goodsDiscountList[i].discountEndDateOriginal  = data.discountEndDate;	// 원본 상품할인 종료일자
							goodsDiscountList[i].discountEndHour = data.discountEndHour;				// 상품할인 종료시간
							goodsDiscountList[i].discountEndHourOld = data.discountEndHour;			// 전 상품할인 종료시간
							goodsDiscountList[i].discountEndHourOriginal = data.discountEndHour;		// 원본 상품할인 종료시간
							goodsDiscountList[i].discountEndMinute = data.discountEndMinute;			// 상품할인 종료분
							goodsDiscountList[i].discountEndMinuteOld = data.discountEndMinute;		// 전 상품할인 종료분
							goodsDiscountList[i].discountEndMinuteOriginal = data.discountEndMinute;	// 원본 상품할인 종료분
							goodsDiscountList[i].discountEndDateTime = data.discountEndDateTime;		// 상품할인 Full 종료일자
						}
						else{

							let discountEndDateTime = goodsDiscountList[i].discountEndDate + " " + goodsDiscountList[i].discountEndHour + ":" + goodsDiscountList[i].discountEndMinute;
							let discountEndDateTimeOriginal = goodsDiscountList[i].discountEndDateOriginal + " " + goodsDiscountList[i].discountEndHourOriginal + ":" + goodsDiscountList[i].discountEndMinuteOriginal;

							if(mode == "delete" && discountEndDateTime != discountEndDateTimeOriginal) {	//항목 삭제 MODE 이고 discountEndDateOriginal과 discountEndDate가 맞지 안다면 원복 처리
								//종료일자 관련 항목 업데이트
								goodsDiscountList[i].discountEndDate = goodsDiscountList[i].discountEndDateOriginal;				// 상품할인 종료일자
								goodsDiscountList[i].discountEndDateOld  = goodsDiscountList[i].discountEndDateOriginal;			// 전 상품할인 종료일자
								goodsDiscountList[i].discountEndHour = goodsDiscountList[i].discountEndHourOriginal;				// 상품할인 종료시간
								goodsDiscountList[i].discountEndHourOld = goodsDiscountList[i].discountEndHourOriginal;				// 전 상품할인 종료시간
								goodsDiscountList[i].discountEndMinute = goodsDiscountList[i].discountEndMinuteOriginal;			// 상품할인 종료분
								goodsDiscountList[i].discountEndMinuteOld = goodsDiscountList[i].discountEndMinuteOriginal;			// 전 상품할인 종료분
								goodsDiscountList[i].discountEndDateTime = discountEndDateTimeOriginal;								// 상품할인 Full 종료일자
							}
							else {
								//console.log("goodsDiscountList["+i+"].discountEndDate : ", goodsDiscountList[i].discountEndDate);

								goodsDiscountList[i].discountEndDateOld  = goodsDiscountList[i].discountEndDate;			// 전 상품할인 종료일자
								goodsDiscountList[i].discountEndDateOriginal  = goodsDiscountList[i].discountEndDate;		// 원본 상품할인 종료일자
								goodsDiscountList[i].discountEndHourOld = goodsDiscountList[i].discountEndHour;				// 전 상품할인 종료시간
								goodsDiscountList[i].discountEndHourOriginal = goodsDiscountList[i].discountEndHour;		// 원본 상품할인 종료시간
								goodsDiscountList[i].discountEndMinuteOld = goodsDiscountList[i].discountEndMinute;			// 전 상품할인 종료분
								goodsDiscountList[i].discountEndMinuteOriginal = goodsDiscountList[i].discountEndMinute;	// 원본 상품할인 종료분
								goodsDiscountList[i].discountEndDateTime = discountEndDateTime;								// 상품할인 Full 종료일자
							}
						}

//						if(element.approvalStatusCode == "APPR_STAT.NONE") {			//승인 대기 상태로 아직 저장하지 않은 상태의 할인 내역도 삭제 처리 한다
//							viewModel.goodsDiscountList.remove(goodsDiscountList[index]); 	// viewModel 에서 삭제
//							viewModel.set("isAddDiscountEnabled", true);					// 승인 대기 상태에서 삭제 됐으면 추가 등록 버튼 다시 활성화 처리
//						}
					}

					//올바른 Visible 및 rowspan을 위해서 rowGroupNum, rowNum 초기화
					let rowGroupNum = 0;
					goodsDiscountList.forEach(function(element, index)
					{
						element.rowNum = index+1;
						let rowGroupDetailNum = index%(viewModel.goodsPackageInfo.standardRowCount+1);

//						element.rowGroupNum = rowGroupNum;
//
//						if(rowGroupDetailNum == viewModel.goodsPackageInfo.standardRowCount) {
//							rowGroupNum ++;
//						}
					});
				}
				viewModel.goodsDiscountList.sync();

				params = {
					'goodsDiscountList' : goodsDiscountList
				,	'goodsDiscountApprList' : goodsDiscountApprList
				}

				parent.POP_PARAM = params;
			},
			fnGoodsDiscountDatasourceRemove : function( goodsDiscountInfo ){ // datasource 삭제 및 할인추가 버튼 제어
				let goodsDiscountList = viewModel.goodsDiscountList.data();
				let selGoodsDiscountId = goodsDiscountInfo.goodsDiscountId;

				if(goodsDiscountList.length > 0) {
					for(let i=goodsDiscountList.length-1; i >= 0; i--){
						if(goodsDiscountList[i].goodsDiscountId == selGoodsDiscountId){		// 삭제버튼 클릭한 할인ID 그룹만 삭제(신규 생성은 goodsDisocuntId가 null임)
							viewModel.goodsDiscountList.remove(goodsDiscountList[i]); 		// viewModel 에서 삭제
						}
					}
				}
				viewModel.fnAddDiscountButtonEnableSetting(true);

				viewModel.set("visibleGoodsPackageSaveBtn", false);	//저장 Button Visible
			},
			fnSave : function(e){ // 저장
				e.preventDefault();

				if( viewModel.fnSaveValid() ){
					let goodsDiscountList = viewModel.goodsDiscountList.data();

					/*console.log("goodsDiscountList : ", goodsDiscountList);
					return;*/

					goodsDiscountList.forEach(function(element, index)
					{
						element.discountStartDateTime = element.discountStartDate + " " + element.discountStartHour + ":" + element.discountStartMinute;

						let discountEndDateTime = element.discountEndDate + " " + element.discountEndHour + ":" + element.discountEndMinute;
						let discountEndDateTimeOriginal = element.discountEndDateOriginal + " " + element.discountEndHourOriginal + ":" + element.discountEndMinuteOriginal;

//						console.log("element.discountEndDate : ", element.discountEndDate);

						/*if(discountEndDateTime != discountEndDateTimeOriginal && element.discountEndDateOriginal != undefined) {	//신규행 저장시 기간수정 항목에 대한 종료일을 변경 했다면 Original 날짜들로 원복 처리 한다.
							//종료일자 관련 항목 업데이트
							element.discountEndDate = element.discountEndDateOriginal;				// 상품할인 종료일자
							element.discountEndDateOld  = element.discountEndDateOriginal;			// 전 상품할인 종료일자
							element.discountEndDateOriginal  = element.discountEndDateOriginal;		// 원본 상품할인 종료일자
							element.discountEndHour = element.discountEndHourOriginal;				// 상품할인 종료시간
							element.discountEndHourOld = element.discountEndHourOriginal;			// 전 상품할인 종료시간
							element.discountEndHourOriginal = element.discountEndHourOriginal;		// 원본 상품할인 종료시간
							element.discountEndMinute = element.discountEndMinuteOriginal;			// 상품할인 종료분
							element.discountEndMinuteOld = element.discountEndMinuteOriginal;		// 전 상품할인 종료분
							element.discountEndMinuteOriginal = element.discountEndMinuteOriginal;	// 원본 상품할인 종료분
							element.discountEndDateTime = discountEndDateTimeOriginal;				// 상품할인 Full 종료일자
						}
						else {*/
							//종료일자 관련 항목 업데이트
							element.discountEndDate = element.discountEndDate;				// 상품할인 종료일자
							element.discountEndDateOld  = element.discountEndDate;			// 전 상품할인 종료일자
							element.discountEndDateOriginal  = element.discountEndDate;		// 원본 상품할인 종료일자
							element.discountEndHour = element.discountEndHour;				// 상품할인 종료시간
							element.discountEndHourOld = element.discountEndHour;			// 전 상품할인 종료시간
							element.discountEndHourOriginal = element.discountEndHour;		// 원본 상품할인 종료시간
							element.discountEndMinute = element.discountEndMinute;			// 상품할인 종료분
							element.discountEndMinuteOld = element.discountEndMinute;		// 전 상품할인 종료분
							element.discountEndMinuteOriginal = element.discountEndMinute;	// 원본 상품할인 종료분
							element.discountEndDateTime = discountEndDateTime;				// 상품할인 Full 종료일자
//						}
					});

//					console.log("goodsDiscountList : ", goodsDiscountList);

					let params = {
						'goodsDiscountList' : goodsDiscountList
					,	'goodsDiscountApprList' : $("#apprGrid").data("kendoGrid").dataSource.data()
					}

					parent.POP_PARAM = params;
					parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
				}
			},
			fnSaveValid : function(){ // 데이터 검증
				let goodsDiscountList = viewModel.goodsDiscountList.data();
				let goodsDiscountLegnth = goodsDiscountList.length - 1; //합계라인은 항목에 포함시키지 않는다.

				if( goodsDiscountLegnth <= 0 ){
					fnKendoMessage({ message : "저장할 값이 없습니다." });
					return false;
				}

				for(let i=0, rowCount=goodsDiscountLegnth; i < rowCount; i++){
					if( goodsDiscountList[i].discountRatio == null ){
						fnKendoMessage({ message : "할인율 값이 없습니다." });
						return false;
					}

					if( goodsDiscountList[i].discountRatio > 99 ){
						fnKendoMessage({ message : "할인율은 100%이하로 설정해야 합니다." });
						return false;
					}

					if( goodsDiscountList[i].discountSalePrice == null ){
						fnKendoMessage({ message : "판매가 값이 없습니다." });
						return false;
					}

					if( !viewModel.fnDiscountStartDateTimeCheck(i) ){
						return false;
					}

					if( !viewModel.fnDiscountEndDateUpdateCheck(i) ){
						return false;
					}
				}

				if (apprAdminGridDs.data().length == 0) {
					fnKendoMessage({ message : "승인관리자를 지정해 주세요." });
					return false;
				}

				return true;
			},
			fnApprAdminInit : function(){	// 승인관리자 정보 Grid
				apprAdminGridDs =  new kendo.data.DataSource();

				apprAdminGridOpt = {
					dataSource : apprAdminGridDs,
					editable : false,
					noRecordMsg: '승인관리자를 선택해 주세요.',
					columns : [{
						field : 'apprAdminInfo',
						title : '승인관리자정보',
						width : '100px',
						attributes : {style : 'text-align:center'}
						},{
						field : 'adminTypeName',
						title : '계정유형',
						width : '100px',
						attributes : {style : 'text-align:center'}
						},{
							field : 'apprUserName',
							title : '관리자이름/아이디',
							width : '100px',
							attributes : {style : 'text-align:center'},
							template : function(dataItem){
								let returnValue;
								returnValue = dataItem.apprUserName + '/' + dataItem.apprLoginId;
								return returnValue;
							}
						},{
							field : 'organizationName',
							title : '조직/거래처 정보',
							width : '100px',
							attributes : {style : 'text-align:center'}
						},{
							field : 'teamLeaderYn',
							title : '조직장여부',
							width : '80px',
							attributes : {style : 'text-align:center'}
						},{
							field : 'userStatusName',
							title : 'BOS 계정상태',
							width : '80px',
							attributes : {style : 'text-align:center'}
						},{
							field : 'grantUserName',
							title : '권한위임정보<BR/>(이름/ID)',
							width : '100px',
							attributes : {style : 'text-align:center'},
							template : function(dataItem){
								let returnValue;
								if(dataItem.grantAuthYn == 'Y'){
									returnValue = dataItem.grantUserName + '/' + dataItem.grantLoginId;
								}else{
									returnValue = '';
								}
								return returnValue;
							}
						},{
							field : 'userStatusName',
							title : '권한위임기간',
							width : '150px',
							attributes : {style : 'text-align:left'},
							template : function(dataItem){
								let returnValue;
								if(dataItem.grantAuthYn == 'Y'){
									returnValue = dataItem.grantAuthStartDt + '~' + dataItem.grantAuthEndDt;
								}else{
									returnValue = '';
								}
								return returnValue;
							}
						},{
							field : 'grantUserStatusName',
							title : '권한위임자<BR/>BOS 계정상태',
							width : '100px',
							attributes : {style : 'text-align:left'},
							template : function(dataItem){
								let returnValue;
								if(dataItem.grantAuthYn == 'Y'){
									returnValue = dataItem.grantUserStatusName;
								}else{
									returnValue = '';
								}
								return returnValue;
							}
						},{
							field:'addCoverageId', hidden:true
						},{
							field:'includeYn', hidden:true
						}
					]
				};

				apprAdminGrid = $('#apprGrid').initializeKendoGrid(apprAdminGridOpt).cKendoGrid();
				viewModel.set("apprDivVisible", true);
				viewModel.set("visibleGoodsPackageSaveBtn", true);
			},
			fnApprAdmin : function() {
				var param = {'taskCode' : 'APPR_KIND_TP.GOODS_DISCOUNT'};
				fnKendoPopup({
					id		: 'approvalManagerSearchPopup',
					title	: '승인관리자 선택',
					src		: '#/approvalManagerSearchPopup',
					param	: param,
					width	: '1300px',
					heigh	: '800px',
					scrollable : "yes",
					success: function( stMenuId, data ){

						if(data && !fnIsEmpty(data) && data.authManager2nd){
							$('#apprGrid').gridClear(true);

							var authManager1 = data.authManager1st;
							var authManager2 = data.authManager2nd;

							if(authManager1.apprUserId != undefined){							//1차 승인관리자가 미지정이라면
								var objManager1 = new Object();

								objManager1["apprAdminInfo"] = "1차 승인관리자";
								objManager1["adminTypeName"] = authManager1.adminTypeName;
								objManager1["apprUserName"] = authManager1.apprUserName;
								objManager1["apprKindTp"] = authManager1.apprKindType;
								objManager1["apprManagerTp"] = authManager1.apprManagerType
								objManager1["apprUserId"] = authManager1.apprUserId;
								objManager1["apprLoginId"] = authManager1.apprLoginId;
								objManager1["organizationName"] = authManager1.organizationName;
								objManager1["userStatusName"] = authManager1.userStatusName;
								objManager1["teamLeaderYn"] = authManager1.teamLeaderYn;
								objManager1["grantAuthYn"] = authManager1.grantAuthYn;
								objManager1["grantUserName"] = authManager1.grantUserName;
								objManager1["grantLoginId"] = authManager1.grantLoginId;
								objManager1["grantAuthStartDt"] = authManager1.grantAuthStartDt;
								objManager1["grantAuthEndDt"] = authManager1.grantAuthEndDt;
								objManager1["grantUserStatusName"] = authManager1.grantUserStatusName;
								apprAdminGridDs.add(objManager1);
							}

							var objManager2 = new Object();

							objManager2["apprAdminInfo"] = "2차 승인관리자";
							objManager2["adminTypeName"] = authManager2.adminTypeName;
							objManager2["apprUserName"] = authManager2.apprUserName;
							objManager2["apprKindTp"] = authManager2.apprKindType;
							objManager2["apprManagerTp"] = authManager2.apprManagerType
							objManager2["apprUserId"] = authManager2.apprUserId;
							objManager2["apprLoginId"] = authManager2.apprLoginId;
							objManager2["organizationName"] = authManager2.organizationName;
							objManager2["userStatusName"] = authManager2.userStatusName;
							objManager2["teamLeaderYn"] = authManager2.teamLeaderYn;
							objManager2["grantAuthYn"] = authManager2.grantAuthYn;
							objManager2["grantUserName"] = authManager2.grantUserName;
							objManager2["grantLoginId"] = authManager2.grantLoginId;
							objManager2["grantAuthStartDt"] = authManager2.grantAuthStartDt;
							objManager2["grantAuthEndDt"] = authManager2.grantAuthEndDt;
							objManager2["grantUserStatusName"] = authManager2.grantUserStatusName;
							apprAdminGridDs.add(objManager2);
						}
					}
				});
			},
			fnApprClear : function() {
				let goodsDiscountApprList = $("#apprGrid").data("kendoGrid");
				if(goodsDiscountApprList != undefined){
					$('#apprGrid').gridClear(true);
					$('#apprSubUserId').val('');
					$('#apprUserId').val('');
				}
			}
		});

		kendo.bind($("#inputForm"), viewModel);
	};

	// 기본값 셋팅
	function fnDefaultValue(){
		// 파라미터 체크
		if( paramData == undefined || paramData.goodsPackageInfo == undefined ){
			fnKendoMessage({ message : "할인 조회를 위한 필수값이 없습니다.", ok : fnClose });
		}else{
			viewModel.fnGetgoodsPackageInfoAndGoodsDiscountList();
		}
	};

	// 팝업 닫기
	function fnClose(){
		parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
	};

	//-------------------------------  Common Function end -------------------------------

});
