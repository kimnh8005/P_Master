/**-----------------------------------------------------------------------------
 * description 		 : 제품 상세 정보 팝업
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.08		박영후          최초생성
 * @ 2020.12.03		이성준          기능추가
 * @ 2020.12.29		이성준          기능추가
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var paramIlItemCd = "";
var paramItemBarcode = "";
var paramItemNm = "";
var paramPreOrderTpNm = "";
var paramIlItemStockId = "";
var paramStockScheduledCount = "";
var paramIlItemWarehouseId = "";
var paramBaseDt = "";
var paramUrWarehouseId = "";
var paramUrSupplierId  = "";

$(document).ready(function() {

	//품목 재고리스트에서 넘어온 param
	paramIlItemWarehouseId   = parent.POP_PARAM["parameter"].ilItemWarehouseId;
	paramBaseDt              = parent.POP_PARAM["parameter"].baseDt;

	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'goodsStockMgm',
			callback : fnUI
		});

        $('#curStockDeadLineBaseDt').text(paramBaseDt);

	}

	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		//fnInitMaskTextBox();  // Initialize Input Mask ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnSearch();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnConfirm, #fnSave, #fnClear').kendoButton();
	}

	function fnSearch(){
		var url  = '/admin/goods/stock/getStockInfo';
		var cbId = 'search';
		var data = $('#inputForm').formSerialize(true);

		data.ilItemWarehouseId   = paramIlItemWarehouseId;  //품목별 출고처 관리ID
		data.baseDt              = paramBaseDt;  //기준일

		fnAjax({
			url     : url,
			params  : data,
			success :
				function( data ){
					fnBizCallback(cbId, data);
				},
				isAction : 'select'
		});
	}

	function valueCheck(key, nullMsg, ID){
		fnKendoMessage({ message : fnGetLangData({ key : key, nullMsg : nullMsg}), ok : function focusValue(){
			$('#'+ID).focus();
		}});
		return false;
	}
	function fnClear(){
		$('#searchForm').formClear(true);
	}

	function fnConfirm(){
		fnKendoMessage({message:'변경된 출고기준은 익일부터 적용됩니다. 진행하시겠습니까?', type : "confirm" ,ok : function(){ fnSave() } });
	}

	function fnSave(){
		var url  = '/admin/goods/stock/putStockDeadlineInfo';
		var cbId= 'update';

		var data = $('#inputForm').formSerialize(true);

		data.popIlStockDeadlineId = $('#popIlStockDeadlineId').val();
		data.urWarehouseId = paramUrWarehouseId;
		data.urSupplierId  = paramUrSupplierId;

		if(data.urSupplierId != null && data.urSupplierId != ''){
			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback(cbId, data);
					},
					isAction : 'update'
			});
		}
	}

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetGroupDataSource({
			url : '/admin/goods/stock/getStockDetailList?ilItemWarehouseId='+paramIlItemWarehouseId+'&baseDt='+paramBaseDt
		});
		aGridOpt = {
			dataSource: aGridDs
			, pageable  : false
			, autoBind : true
			, scrollable : true
			, navigatable: true
			, sellectable: false
			, sortable: true
	        ,height:260
			,columns   : [
				{ field:'restDistributionPeriod' ,title : '잔여 유통기간',  width:'110px' ,attributes:{ style:'text-align:center' }
												 ,template: function(dataItem) {
														var fontColor = "";

														if(dataItem.stockTp == 'STOCK_EXPR_TP.IMMINENT'){//임박
														   fontColor = "blue";
														}else if(dataItem.stockTp == 'STOCK_EXPR_TP.NORMAL'){//정상
														   fontColor = "black";
														}else{
														   fontColor = "red";//폐기
														}

														var html =	"<font color='"+fontColor+"'>" + kendo.htmlEncode(dataItem.restDistributionPeriod) + "</font>";
														return html;
												 }}
			   ,{ field:'expirationDt'           ,title : '유통기한',     width:'170px' ,attributes:{ style:'text-align:center' }
												 ,template: function(dataItem) {
														var fontColor = "";

														if(dataItem.stockTp == 'STOCK_EXPR_TP.IMMINENT'){//임박
														   fontColor = "blue";
														}else if(dataItem.stockTp == 'STOCK_EXPR_TP.NORMAL'){//정상
														   fontColor = "black";
														}else{
														   fontColor = "red";//폐기
														}

														var html =	"<font color='"+fontColor+"'>" + kendo.htmlEncode(dataItem.expirationDt) + "</font>";
														return html;
												 }}
			   ,{ field:'stockNm'			     ,title : '구분'	 ,     width:'80px'  ,attributes:{ style:'text-align:center' }
												 ,template: function(dataItem) {
													var fontColor = "";

													if(dataItem.stockTp == 'STOCK_EXPR_TP.IMMINENT'){//임박
													   fontColor = "blue";
													}else if(dataItem.stockTp == 'STOCK_EXPR_TP.NORMAL'){//정상
													   fontColor = "black";
													}else{
													   fontColor = "red";//폐기
													}

													var html =	"<font color='"+fontColor+"'>" + kendo.htmlEncode(dataItem.stockNm) + "</font>";
													return html;
											     }}
			   ,{ field:'stockQty'			     ,title : '재고'	 ,     width:'90px'  ,attributes:{ style:'text-align:center' }
			                                     ,template: function(dataItem) {
														var fontColor = "";

														if(dataItem.stockTp == 'STOCK_EXPR_TP.IMMINENT'){ //임박
														   fontColor = "blue";
														}else if(dataItem.stockTp == 'STOCK_EXPR_TP.NORMAL'){//정상
														   fontColor = "black";
														}else{
														   fontColor = "red";//폐기
														}

														var html =	"<font color='"+fontColor+"'>" + kendo.htmlEncode(dataItem.stockQty) + "</font>";
														return html;
												     }}
			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
        aGrid.bind("dataBound", function() {

        });


	}

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

		//출고기준관리 조회
		let params = {};
		params.ilItemWarehouseId = paramIlItemWarehouseId;

		fnKendoDropDownList({
			id    : 'popIlStockDeadlineId',
			url : "/admin/goods/stock/getStockDeadlineDropDownList",
			tagId : 'popIlStockDeadlineId',
			params : params,
			textField :"name",
			valueField : "ilStockDeadlineId",
			blank : ""
		});

	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	// 재고 조정 > 조정 수량 keyup 이벤트 핸들러
	function fnKeyupControlCount(e) {
		getResultCount(e);
	}

	// 재고 조정 > 조정 수량 focusout 이벤트 핸들러
	function fnBlurControlCount(e) {
		getResultCount(e);
	}

	function getResultCount(e) {
		const regex = /[^0-9\-]/gim;
		const $target = $(e.target);

		let value = $target.val();

		if(value.match(regex)) {
			value = value.replace(regex, '');
			$target.val(value);
			fnShowValidateMessage($target);
			return;
		}

		if(!value.length) {
			value = 0;
		}

		// 입력된 값이 숫자가 아닐 경우 '123-'와 같은 케이스
		if(value.length > 1 && isNaN(value)) {
			$target.val('');
			fnShowValidateMessage($target, ALERT_MESSAGES.NOT_NUM);
			return;
		}

		const controlCount = Number(value),
			idPrefix = $target.data('target'),
			$ifCount = $('#' + idPrefix + '-if-count'),
			$resultCount = $('#' + idPrefix + '-result-count');

		if(!idPrefix || !$ifCount.length || !$resultCount.length) return;

		const ifCount = $ifCount.text();

		// controlCount === '-'인 경우
		if(isNaN(ifCount)) return;
		if(isNaN(controlCount)) {
			if(e.type === 'blur') {
				$target.val(0);
			}
			return;
		}

		const resultCount = calculateResultCount(Number(ifCount), controlCount);
		$resultCount.text(resultCount);
	}

	function fnSaveStockCount(e) {
		if (Number.isNaN(parseInt($('#confirmed-result-count').text())) || Number.isNaN(parseInt($('#expected-result-count').text()))) {
			fnKendoMessage({
				message : '잘못된 형식의 값이 입력되었습니다.',
				ok : function() {
					return;
				}
			});
			return;
		}
		else if (parseInt($('#confirmed-result-count').text()) > parseInt($('#expected-result-count').text())) {
			fnKendoMessage({
				message : '입고확정수량은 입고예정수량보다 많을 수 없습니다.',
				ok : function() {
					return;
				}
			});
			return;
		}

		fnKendoMessage({message:'변경된 조정 재고 수량을 적용하시겠습니까?', type : "confirm" ,ok : function(){ fnSaveStockAdjustCount() } });
	}

	function fnSaveStockAdjustCount() {
		var url  = '/admin/goods/stock/putStockAdjustCount';
		var cbId= 'putStockAdjustCount';

		var data = {};
		data.ilItemWarehouseId = paramIlItemWarehouseId;
		data.baseDt = paramBaseDt;
		if ($('#ended-control-count').val() == '')
			data.d0StockClosedAdjQty = 0;
		else
			data.d0StockClosedAdjQty = $('#ended-control-count').val();

		if ($('#expected-control-count').val() == '')
			data.d0StockScheduledAdjQty = 0;
		else
			data.d0StockScheduledAdjQty = $('#expected-control-count').val();

		if ($('#confirmed-control-count').val() == '')
			data.d0StockConfirmedAdjQty = 0;
		else
			data.d0StockConfirmedAdjQty = $('#confirmed-control-count').val();

		if(data.ilItemWarehouseId != null && data.baseDt != ''){
			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback(cbId, data);
					},
					isAction : 'update'
			});
		}
	}


	// 반영 수량 계산
	function calculateResultCount(ifCount, controlCount) {
		return parseInt(ifCount) + parseInt(controlCount);
	}
	//---------------fnInitEvents End ------------------------------------------------


	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
				//$('#inputForm').bindingForm(data, 'rows', true);
				break;

			case 'search':
				 $('#inputForm').bindingForm(data, 'stockListResultVo', true);
				 var detailData = data.stockListResultVo;

				 $("#warehouseNm").html(detailData.warehouseNm);
				 $("#ilItemCd").html(detailData.ilItemCode);
				 $("#itemBarcode").html(detailData.barcode);
				 $("#itemNm").html(detailData.itemNm);
				 $("#preOrderTpNm").html(detailData.preOrderNm);
				 
				 var d0ScheduleQty = 0;
				 var d0ScheduleAccQty = detailData.orderQtyList[0].d0StockScheduledQuantity;
				 var d0ConfirmAccQty = detailData.d0StockConfirmedQty;
				 
				 if(detailData.preOrderType == "PRE_ORDER_TP.LIMITED_ALLOWED") {
				 	if(detailData.d0StockConfirmedQty > 0 || detailData.d0StockConfirmedAdjQty > 0) {
						d0ScheduleAccQty = 0;
					}
				 }
				 
				 d0ScheduleAccQty = d0ScheduleAccQty + detailData.d0StockScheduledAdjQty;
				 d0ConfirmAccQty =  d0ConfirmAccQty + detailData.d0StockConfirmedAdjQty;
				 
				 if(d0ConfirmAccQty > d0ScheduleAccQty) {
					d0ScheduleQty = 0;
				 } else {
					d0ScheduleQty = d0ScheduleAccQty - d0ConfirmAccQty;
				 }

				 $("#d0stockQuantity").html(detailData.d0stockQuantity+'<br/>('+d0ScheduleQty+')');
				 $("#d1stockQuantity").html(detailData.d1stockQuantity+'<br/>('+detailData.orderQtyList[0].d1StockScheduledQuantity+')');
				 $("#d2stockQuantity").html(detailData.d2stockQuantity+'<br/>('+detailData.orderQtyList[0].d2StockScheduledQuantity+')');
				 $("#d3stockQuantity").html(detailData.d3stockQuantity+'<br/>('+detailData.orderQtyList[0].d3StockScheduledQuantity+')');
				 $("#d4stockQuantity").html(detailData.d4stockQuantity+'<br/>('+detailData.orderQtyList[0].d4StockScheduledQuantity+')');
				 $("#d5stockQuantity").html(detailData.d5stockQuantity+'<br/>('+detailData.orderQtyList[0].d5StockScheduledQuantity+')');
				 $("#d6stockQuantity").html(detailData.d6stockQuantity+'<br/>('+detailData.orderQtyList[0].d6StockScheduledQuantity+')');
				 $("#d7stockQuantity").html(detailData.d7stockQuantity+'<br/>('+detailData.orderQtyList[0].d7StockScheduledQuantity+')');
				 $("#d8stockQuantity").html(detailData.d8stockQuantity+'<br/>('+detailData.orderQtyList[0].d8StockScheduledQuantity+')');
				 $("#d9stockQuantity").html(detailData.d9stockQuantity+'<br/>('+detailData.orderQtyList[0].d9StockScheduledQuantity+')');
				 $("#d10stockQuantity").html(detailData.d10stockQuantity+'<br/>('+detailData.orderQtyList[0].d10StockScheduledQuantity+')');
				 $("#d11stockQuantity").html(detailData.d11stockQuantity+'<br/>('+detailData.orderQtyList[0].d11StockScheduledQuantity+')');
				 $("#d12stockQuantity").html(detailData.d12stockQuantity+'<br/>('+detailData.orderQtyList[0].d12StockScheduledQuantity+')');
				 $("#d13stockQuantity").html(detailData.d13stockQuantity+'<br/>('+detailData.orderQtyList[0].d13StockScheduledQuantity+')');
				 $("#d14stockQuantity").html(detailData.d14stockQuantity+'<br/>('+detailData.orderQtyList[0].d14StockScheduledQuantity+')');
				 $("#d15stockQuantity").html(detailData.d15stockQuantity+'<br/>('+detailData.orderQtyList[0].d15StockScheduledQuantity+')');

				 $("#d0orderQuantity").html(detailData.orderQtyList[0].d0Quantity+'<br/>('+detailData.d0orderQuantity+')');
				 $("#d1orderQuantity").html(detailData.orderQtyList[0].d1Quantity+'<br/>('+detailData.d1orderQuantity+')');
				 $("#d2orderQuantity").html(detailData.orderQtyList[0].d2Quantity+'<br/>('+detailData.d2orderQuantity+')');
				 $("#d3orderQuantity").html(detailData.orderQtyList[0].d3Quantity+'<br/>('+detailData.d3orderQuantity+')');
				 $("#d4orderQuantity").html(detailData.orderQtyList[0].d4Quantity+'<br/>('+detailData.d4orderQuantity+')');
				 $("#d5orderQuantity").html(detailData.orderQtyList[0].d5Quantity+'<br/>('+detailData.d5orderQuantity+')');
				 $("#d6orderQuantity").html(detailData.orderQtyList[0].d6Quantity+'<br/>('+detailData.d6orderQuantity+')');
				 $("#d7orderQuantity").html(detailData.orderQtyList[0].d7Quantity+'<br/>('+detailData.d7orderQuantity+')');
				 $("#d8orderQuantity").html(detailData.orderQtyList[0].d8Quantity+'<br/>('+detailData.d8orderQuantity+')');
				 $("#d9orderQuantity").html(detailData.orderQtyList[0].d9Quantity+'<br/>('+detailData.d9orderQuantity+')');
				 $("#d10orderQuantity").html(detailData.orderQtyList[0].d10Quantity+'<br/>('+detailData.d10orderQuantity+')');
				 $("#d11orderQuantity").html(detailData.orderQtyList[0].d11Quantity+'<br/>('+detailData.d11orderQuantity+')');
				 $("#d12orderQuantity").html(detailData.orderQtyList[0].d12Quantity+'<br/>('+detailData.d12orderQuantity+')');
				 $("#d13orderQuantity").html(detailData.orderQtyList[0].d13Quantity+'<br/>('+detailData.d13orderQuantity+')');
				 $("#d14orderQuantity").html(detailData.orderQtyList[0].d14Quantity+'<br/>('+detailData.d14orderQuantity+')');
				 $("#d15orderQuantity").html(detailData.orderQtyList[0].d15Quantity+'<br/>('+detailData.d15orderQuantity+')');

				 if (detailData.goodsDisposalCnt > 0) { // 폐기임박상품이 존재하면 폐기임박 재고 정보 노출
					 $("#discardStockArea").show();
					 $("#d0stockDiscardQuantity").html(detailData.d0stockDiscardQuantity);
					 $("#d0orderDiscardQuantity").html(detailData.orderQtyList[0].d0DiscardQuantity+'<br/>('+detailData.d0orderDiscardQuantity+')');
				 }

	        	 $('#curStockDeadLine').text(detailData.name);

	        	 // 재고조정 초기값 설정
	        	 $('#ended-if-count').text(detailData.d0StockClosedQty); // 전일마감재고 I/F수량
	        	 $('#ended-control-count').val(detailData.d0StockClosedAdjQty); // 전일마감재고 조정수량
	        	 $('#ended-result-count').text(detailData.d0StockClosedQty + detailData.d0StockClosedAdjQty); // 전일마감재고 반영수량
	        	 $('#expected-if-count').text(detailData.orderQtyList[0].d0StockScheduledQuantity); // D0입고예정 I/F수량
	        	 $('#expected-control-count').val(detailData.d0StockScheduledAdjQty); // D0입고예정 조정수량
	        	 $('#expected-result-count').text(detailData.orderQtyList[0].d0StockScheduledQuantity + detailData.d0StockScheduledAdjQty); // D0입고예정 반영수량
	        	 $('#confirmed-if-count').text(detailData.d0StockConfirmedQty); // D0입고확정 I/F수량
	        	 $('#confirmed-control-count').val(detailData.d0StockConfirmedAdjQty); // 입고확정 조정수량
	        	 $('#confirmed-result-count').text(detailData.d0StockConfirmedQty + detailData.d0StockConfirmedAdjQty); // 입고확정 반영수량

				 var totalSum = detailData.d0StockClosedQty + detailData.d0StockClosedAdjQty;
//				 if (detailData.stockQtyList[0] != undefined && detailData.stockQtyList[0].totalSum != undefined) {
//					 totalSum = detailData.stockQtyList[0].totalSum;
//				 }
				 $("#totalSum").html(totalSum);

	        	 break;

			case 'update':
				aGridDs.query();
				fnKendoMessage({message : '수정되었습니다.'});
				fnInitOptionBox();
				break;

			case 'putStockAdjustCount' :
				fnSearch();
				fnKendoMessage({message : '저장되었습니다.'});
				break;
		}
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common Confirm*/
	$scope.fnConfirm = function(){ fnConfirm(); };
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};

	$scope.fnKeyupControlCount = function(e) { fnKeyupControlCount(e); };

	$scope.fnBlurControlCount = function(e) { fnBlurControlCount(e); };

	$scope.fnSaveStockCount = function(e) { fnSaveStockCount(e); };
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
