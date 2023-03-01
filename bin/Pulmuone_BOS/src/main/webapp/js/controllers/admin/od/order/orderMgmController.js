/*******************************************************************************************************************************************************************************************************
 * --------------- description : 상품 관리 - 일반상품 등록 @ ----------------------------
 ******************************************************************************************************************************************************************************************************/
'use strict';
var pageParam = fnGetPageParam();													//GET Parameter 받기
var odid = pageParam.odid;
var odOrderId = pageParam.odOrderId;												//주문 ID
// 상품/결제 정보 & 주문자 정보 & 배송 정보 > 상품정보 그리드
var orderGoodsGrid, orderGoodsGridDs, orderGoodsGridOpt,orderGoodsOrderGridOpt, orderGoodsShippingGridOpt;
// 상품/결제 정보 > 쿠폰 할인 정보 그리드
var couponDiscountGrid, couponDiscountGridDs, couponDiscountGridOpt;
// 처리이력 그리드
var orderHistoryGrid, orderHistoryGridDs, orderHistoryGridOpt;
// 배송 정보 > 배송정보 그리드
var shippingGrid, shippingGridDs, shippingGridOpt;
// 클레임 정보 > 클레임 상품 정보 그리드
var claimGoodsGrid, claimGoodsGridDs, claimGoodsGridOpt;
// 클레임 정보 > 클레임 회수 정보 그리드
var claimReturnShippingGrid, claimReturnShippingGridDs, claimReturnShippingGridOpt;
// 선물하기 조회 정보
var orderPresentInfo;

var tabStrip = null;
var gridSelect1 = null;
// 상품/결제 정보 내 체크박스ID
var checkedRowId = [];
// 배송정보 내 체크박스ID
var checkedRowId2 = [];
// 클레임 정보 내 체크박스ID
var checkedClaimRowId = [];
var serverUrlObj;

var isHitokSwitch = null; //하이톡 스위치
var profilesActive = null; //프로필 조회

$(document).ready(function() {
	//공통 스크립트 import

	importScript("/js/service/od/order/orderMgmGridColumns.js", function (){
		importScript("/js/service/od/order/orderMgmFunction.js", function (){
			importScript("/js/service/od/order/orderMgmPopups.js", function (){
				importScript("/js/service/od/order/orderCommSearch.js", function (){
					fnInitialize();
				});
			});
		});
	});

	// Initialize Page Call
	function fnInitialize() {
		$scope.$emit('fnIsMenu', {
			flag : 'true'
		});
		fnPageInfo({
			PG_ID : 'orderMgm',
			callback : fnUI
		});
	};
	// Initialize Calback
	function fnUI() {
		serverUrlObj = fnGetServerUrl();
		initTabStrip(); //tab 처리
		fnInitGrids();
		//fnInitOptionBox();
		orderStatusChangeUtil.fnPutOrderStatusBtnClear();//버튼초기화
		fnInitPopups();
		fnInitConsultButton(); //주문상담내용버튼
		initOrderMgmButtons(); // 주문 상태 처리 버튼 생성
		bindEvents(); // 이벤트 바인딩
	};
	//------------------------------- Grid Start -------------------------------
	function fnInitGrids(){


		$("#"+pageParam.orderMenuId).addClass("active");


		$(".tab-cont.k-content.k-state-active").find("[name='putOrderStatusBtnArea']").find("button").attr("disabled",true);
		//--상품/결제 정보

		isHitokSwitch = initHitokSwitch(); //하이톡 스위치 여부 조회 (Sync : false)
		profilesActive = initProfilesActive(); //프로필 조회

		if(odid.indexOf(" ") > -1) {
			odid = odid.substring(0, odid.indexOf(' '));
		}

		//--주문자 정보
		odOrderId = initOrdererForm(); // 주문자 정보 > 주문자 정보

		if (odOrderId == "" || odOrderId <= 0){
			fnKendoMessage({message : '주문번호를 확인해주세요.'  ,ok : function(){
					$(".k-dialog-buttongroup > button").trigger("click");
					$("#"+pageParam.orderMenuId).trigger("click");
				}});

			return false;
		}

		if (initOrderGoodsGrid() <= 0) {
			fnKendoMessage({message : '주문번호를 확인해주세요.'  ,ok : function(){
					$(".k-dialog-buttongroup > button").trigger("click");
					$("#"+pageParam.orderMenuId).trigger("click");
				}});

			return false;
		}

		initPayForm(); // 상품/결제 정보 > 결제 정보 > 결제상세정보 / 결제정보 / 환불정보
		initCouponDiscountGrid(); // 상품/결제 정보 > 쿠폰 할인 정보
		initOrderConsultForm(); // 상품/결제 정보 > 주문 상담 내용
		initOrderHistoryGrid(); // 상품/결제 정보 >  처리이력
		//--클레임 회수 정보
		initClaimReturnShippingGrid(); // 클레임 정보 > 클레임 회수 정보
		//--배송 정보
		initShippingGrid(); // 배송 정보 > 배송 정보

		//--클레임 정보
		initClaimGoodsGrid(); // 클레임 정보 > 클레임 상품 정보



		orderDetailGridEventUtil.ckeckBoxAllClick();
		orderDetailGridEventUtil.checkBoxClick();
		orderDetailGridEventUtil.ckeckBoxPackageClick();

		$('.rowAllCheckbox').prop("checked", false);
	}

	//하이톡 <--> FD-PHI 스위치 여부 (하이톡 스위치)
	function initHitokSwitch() {
		return orderMgmFunctionUtil.initHitokSwitch();
	}

	//프로필 조회
	function initProfilesActive() {
		return orderMgmFunctionUtil.initProfilesActive();
	}

	// 상품/결제 정보 > 상품정보 그리드
	function initOrderGoodsGrid() {
		var dataCount = orderMgmFunctionUtil.initOrderGoodsGrid(odOrderId);

		$("#orderGoodsGrid div.k-grid-content table tr").each(function(i){
			var that = $(this);
			let dataItems = orderGoodsGrid.dataItem($(this).closest('tr'));
			if(dataItems == undefined) {return false;}
			that.attr("data-group", stringUtil.getInt(dataItems.odOrderDetlParentId, 0));

			if (dataItems.odOrderDetlSeq != 0 && stringUtil.getInt(dataItems.claimAbleCnt, 0) == 0){
				that.addClass("trDisabled");
			}
		})


		$("#orderGoodsGrid div.k-grid-content table tr").off("hover");
		$("#orderGoodsGrid div.k-grid-content table tr").off('mouseenter mouseleave');

		$('#orderGoodsGrid tr').hover(function() {
			$('tr[data-group="'+$(this).data('group')+ '"]').toggleClass('hover');
		});
		orderStatusChangeUtil.fnPutOrderStatusBtnClear();
		return dataCount;
	}

	// 결제 정보 > 결제상세정보 / 결제정보 / 환불정보
	function initPayForm(){
		orderMgmFunctionUtil.payInfoList(odOrderId);
	}

	// 처리 이력 그리드
	function initOrderHistoryGrid() {
		orderMgmFunctionUtil.initOrderHistoryGrid(odOrderId);
	}

	// 주문자 정보
	function initOrdererForm(){
		return orderMgmFunctionUtil.initOrdererForm(odid);
	}

	// 배송정보 그리드
	function initShippingGrid(){
		orderMgmFunctionUtil.initShippingGrid(odOrderId);
	};

	// 클레임 상품 정보 그리드
	function initClaimGoodsGrid() {
		orderMgmFunctionUtil.initClaimGoodsGrid(odOrderId, checkedClaimRowId, gridSelect1);
	}

	// 쿠폰 할인 정보 그리드
	function initCouponDiscountGrid() {
		orderMgmFunctionUtil.initCouponDiscountGrid(odOrderId);
	}

	// 주문 상담 내용 결과 (조회)
	function initOrderConsultForm(){
		$('#modifyNm').text(PG_SESSION.loginName);
		$('#modifyId').text(PG_SESSION.loginId);
		orderMgmFunctionUtil.initOrderConsultForm(odOrderId);
	}

	function initClaimReturnShippingGrid() {
		orderMgmFunctionUtil.initClaimReturnShippingGrid(odOrderId);
	}

	//-------------------------------  Grid End  -------------------------------

	//-------------------------------  optionBox Start  -------------------------------
	//주문상담저장버튼
	function fnInitConsultButton(){
		$('#fnSave, #fnUpdate, #fnDel').kendoButton();
	};
	//주문상담 (저장)
	function fnSave(){
		var url  = '/admin/order/addOrderDetailConsult?odOrderId='+odOrderId; // 주문상담내용 url
		let odConsultMsg = $.trim($("#odConsultMsg").val());
		if (odConsultMsg == ""){
			fnKendoMessage({message: '주문 상담 내용을 입력해주세요.'});
			return false;
		} else {
			var data = $('#inputConsultForm').formSerialize(true);
			if (data.rtnValid) {
				fnAjax({
					url: url,
					params: data,
					success:
						function (data) {
							fnKendoMessage({message: '저장되었습니다.'});
							$("#odConsultMsg").val("");
							initOrderConsultForm();
						},
					isAction: 'batch'
				});
			}
		}
	};

	//주문상담 (수정)
	function fnUpdate(id) {
		fnKendoMessage({message : '수정하시겠습니까?', type : "confirm", ok : function(){
					$("#fnUpdate_"+id).hide();
					$("#fnDel_"+id).hide();
					$("#fnSave_list_"+id).show();
					$("#odConsultMsg_"+id).css('background-color', '#FFFFFF').prop('disabled', false);
				}});
	};

	//주문상담 (삭제)
	function fnDel(odConsultId) {
		fnKendoMessage({message : '삭제하시겠습니까?', type : "confirm", ok : function(){
				fnAjax({
					url     : '/admin/order/delOrderDetailConsult?odConsultId='+odConsultId,
					success :
						function( data ){
							fnKendoMessage({message : '삭제되었습니다.'});
							initOrderConsultForm();
						},
					isAction : 'delete'
				});
			}});
	};

	//주문상담 리스트 (수정 -> 저장)
	function fnSaveList(id){
		var url  = '/admin/order/putOrderDetailConsult';
		let odConsultMsg = $.trim($("#odConsultMsg_"+id).val());
		if (odConsultMsg == ""){
			fnKendoMessage({message: '주문 상담 내용을 입력해주세요.'});
			return false;
		} else {
			var data = $('#inputConsultForm').formSerialize(true);
			if (data.rtnValid) {
				fnAjax({
					url: url,
					params	: {odConsultId : id, odConsultMsg : odConsultMsg},
					success:
						function (data) {
							fnKendoMessage({message: '저장되었습니다.'});
							initOrderConsultForm();
						},
					isAction: 'update'
				});
			}
		}
	};

	//선물정보 > 저장
	function fnPresentSave() {
		var data = $('#presentForm').formSerialize(true);
		data.odOrderId = odOrderId;
		data.presentReceiveHp = data.presentReceiveHp_prefix +  data.presentReceiveHp_phone1 + data.presentReceiveHp_phone2;
		delete data.presentReceiveHp_prefix;
		delete data.presentReceiveHp_phone1;
		delete data.presentReceiveHp_phone2;

		fnKendoMessage({message : '수정하시겠습니까?', type : "confirm", ok : function(){
			if (data.rtnValid) {
				fnAjax({
					url: '/admin/order/putOrderPresentInfo',
					params	: data,
					success:
						function (data) {
							fnKendoMessage({message: '수정되었습니다.'});
							initOrderPresentInfo(odOrderId);
						},
					isAction: 'update'
				});
			}
		}});
	};

	//선물정보 > 메세지 재발송
	function fnPresentMsgSend() {
		var data = orderPresentInfo;
		fnKendoMessage({
			message: data.presentReceiveNm+' / '+ data.presentReceiveHp +'로 '+'</br> 선물메세지를 보내시겠습니까?', type: "confirm", ok: function () {
				fnAjax({
					url: '/admin/order/reSendMessage?odid=' + odid,
					success:
						function (data) {
							fnKendoMessage({message: '선물메세지를 보냈습니다.'});
							initOrderPresentInfo(odOrderId);
						},
					isAction: 'update'
				});
			}
		});
	};
	//-------------------------------  Grid End  -------------------------------

	//-------------------------------  optionBox Start  -------------------------------

	function fnInitOptionBox() {
	}
	//-------------------------------  optionBox End  -------------------------------

	//-------------------------------  events Start  -------------------------------
	//팝업초기화
	function fnInitPopups() {
		// 상품정보 > 상품코드, 상품명 클릭
		$('#ng-view').on("click", "div.popGoodsClick", function(e) {
			e.preventDefault();
			let dataItems = orderGoodsGrid.dataItem($(this).closest('tr'));
			let ilGoodsId = dataItems.ilGoodsId;
			let evExhibitId = stringUtil.getInt(dataItems.evExhibitId, 0);


			if(dataItems.goodsTpCd == 'GOODS_TYPE.PACKAGE'){
				if(dataItems.goodsNm.indexOf('내맘대로') > 0) {
					//let serverUrlObj = fnGetServerUrl();
					window.open(serverUrlObj.mallUrl + "/freeOrder", "_blank");
				} else if (evExhibitId > 0){
					window.open("#/exhibitMgm?exhibitTp=EXHIBIT_TP.SELECT&evExhibitId="+ evExhibitId +"&mode=update" ,"_blank");
				}else{
					//window.open(serverUrlObj.mallUrl+"/shop/goodsView?goods="+ilGoodsId ,"_blank");
					window.open("#/goodsPackage?ilGoodsId="+ilGoodsId,"_blank");
				}
				// 추가&증정 상품
			}else if(dataItems.goodsTpCd == 'GOODS_TYPE.ADDITIONAL' || dataItems.goodsTpCd == 'GOODS_TYPE.GIFT' || dataItems.goodsTpCd == 'GOODS_TYPE.GIFT_FOOD_MARKETING'){
				window.open("#/goodsAdditional?ilGoodsId="+ilGoodsId,"_blank");
				// 일일 상품
			}else if(dataItems.goodsTpCd == 'GOODS_TYPE.DAILY'){
				window.open("#/goodsDaily?ilGoodsId="+ilGoodsId,"_blank");
				// 폐기임박
			}else if(dataItems.goodsTpCd == 'GOODS_TYPE.DISPOSAL'){
				window.open("#/goodsDisposal?ilGoodsId="+ilGoodsId,"_blank");
				// 무형 상품
			}else if(dataItems.goodsTpCd == 'GOODS_TYPE.INCORPOREITY'){
				window.open("#/goodsIncorporeal?ilGoodsId="+ilGoodsId,"_blank");
				// 렌탈 상품
			}else if(dataItems.goodsTpCd == 'GOODS_TYPE.RENTAL'){
				window.open("#/goodsRental?ilGoodsId="+ilGoodsId,"_blank");
				// 매장전용 상품
			}else if(dataItems.goodsTpCd == 'GOODS_TYPE.SHOP_ONLY'){
				window.open("#/goodsShopOnly?ilGoodsId="+ilGoodsId,"_blank");
				// 그외 상품
			}else{
				window.open("#/goodsMgm?ilGoodsId="+ilGoodsId,"_blank");
			}
			return false;
		});

		$('#ng-view').on("click", "div.popGoodsNmClick", function(e) {
			e.preventDefault();
			let dataItems = orderGoodsGrid.dataItem($(this).closest('tr'));
			let ilGoodsId = dataItems.ilGoodsId;
			let evExhibitId = stringUtil.getInt(dataItems.evExhibitId, 0);


			if(dataItems.goodsTpCd == 'GOODS_TYPE.PACKAGE'){
				if(dataItems.goodsNm.indexOf('내맘대로') > 0) {
					//let serverUrlObj = fnGetServerUrl();
					window.open(serverUrlObj.mallUrl + "/freeOrder", "_blank");
				}else if (evExhibitId > 0){
					window.open(serverUrlObj.mallUrl+"/pickingOutView?select="+evExhibitId ,"_blank");
				}else{
					window.open(serverUrlObj.mallUrl+"/shop/goodsView?goods="+ilGoodsId ,"_blank");
					//window.open("#/goodsPackage?ilGoodsId="+ilGoodsId,"_blank");
				}
				// 추가&증정 상품
			}else if(dataItems.goodsTpCd == 'GOODS_TYPE.ADDITIONAL' || dataItems.goodsTpCd == 'GOODS_TYPE.GIFT' || dataItems.goodsTpCd == 'GOODS_TYPE.GIFT_FOOD_MARKETING'){
				//window.open("#/goodsAdditional?ilGoodsId="+ilGoodsId,"_blank");
				//window.open(serverUrlObj.mallUrl+"/shop/goodsView?goods="+ilGoodsId ,"_blank");
				// 일일 상품
			}else if(dataItems.goodsTpCd == 'GOODS_TYPE.DAILY'){
				window.open(serverUrlObj.mallUrl+"/shop/goodsView?goods="+ilGoodsId ,"_blank");
				//window.open("#/goodsDaily?ilGoodsId="+ilGoodsId,"_blank");
				// 그외 상품
			}else{
				window.open(serverUrlObj.mallUrl+"/shop/goodsView?goods="+ilGoodsId ,"_blank");
				//window.open("#/goodsMgm?ilGoodsId="+ilGoodsId,"_blank");
			}
			return false;
		});

		$('#ng-view').on("click", "div.popIlItemCdClick", function(e) {
			e.preventDefault();
			let dataItems = orderGoodsGrid.dataItem($(this).closest('tr'));


			var option = new Object();
			option.url = "#/itemMgmModify";  // 마스터 품목 수정 화면 URL
			option.target = "itemMgmModify";
			option.data = {                  // 마스터 품목 수정 화면으로 전달할 Data
				ilItemCode : dataItems.ilItemCd,
				isErpItemLink : (dataItems.erpIfYn == 'Y' ? true : false),
				masterItemType : dataItems.itemTp
			};

			fnGoNewPage(option);

			//window.open("#/itemMgmModify?ilItemCode="+ilItemCd,"_blank");
		});



		// 상품정보 > 묶음상품
		$('#ng-view').on("click", ".orderPackageMore", function(e) {
			let that = $(this);
			let trObj = that.closest('tr');
			var selectedRowData = orderGoodsGrid.dataItem(trObj);

			let parentId = selectedRowData.odOrderDetlId;
			let obj		= that.closest("div[data-role='grid']").find("input[data-detailid='"+parentId+"'].rowCheckbox");
			let btnObj = trObj.find("button.orderPackageMore");

			let dataView = stringUtil.getString(btnObj.data("view"), "N");

			if (dataView == "Y"){
				btnObj.text("+ 더보기");
				btnObj.data("view", "N");
				obj.closest("tr").hide();
			} else {
				btnObj.text("- 접기");
				btnObj.data("view", "Y");
				obj.closest("tr").show();
			}
		});
		$('#ng-view').on("hover", "div.k-grid-content table tr", function(e) {
			$('tr[data-group="'+$(this).data('group')+ '"]').toggleClass('hover');
		});



		// 상품정보 > 스케쥴관리 팝업
		$('#ng-view').on("click", "#orderScheduleChangeBtn", function(e) {
			var selectedRowData = orderGoodsGrid.dataItem($(this).closest('tr'));
			let param = {};
			param.odOrderDetlId = selectedRowData.odOrderDetlId;
			param.urWarehouseId = selectedRowData.urWarehouseId;
			let initGrid = { //success 함수
				initGridSucess : function initShippingGrid(){
					orderMgmFunctionUtil.initShippingGrid(odOrderId);
					orderMgmFunctionUtil.initOrderHistoryGrid(odOrderId);
				}
			};
			orderPopupUtil.open("scheduleMgmPopup", param, initGrid.initGridSucess);
		});

		// 상품정보 > 주문I/F변경 팝업
		$('#ng-view').on("click","div[name=ifDayChange]",function(){
			let gridSelect = $(this).closest("div[data-role=grid]").attr("id");
			var selectedRowData = "";
			if(gridSelect != "claimGoodsGrid") {
				selectedRowData = orderGoodsGrid.dataItem($(this).closest('tr'));
			} else  {
				selectedRowData = claimGoodsGrid.dataItem($(this).closest('tr'));
			}
			var shipingDataList = shippingGrid.dataSource._data;
			for(var i = 0 ; i < shipingDataList.length ; i++){ // 주문배송지 우편번호,건물번호
				if(shipingDataList[i].odShippingZoneId == selectedRowData.odShippingZoneId){
					selectedRowData.zipCode = shipingDataList[i].recvZipCd;
					selectedRowData.recvBldNo = shipingDataList[i].recvBldNo;
				}
			}
			var weekCode = "";
			if (selectedRowData.monCnt > 0){ weekCode = "WEEK_CD.MON"; }
			if (selectedRowData.tueCnt > 0){ weekCode = "WEEK_CD.TUE"; }
			if (selectedRowData.wedCnt > 0){ weekCode = "WEEK_CD.WED"; }
			if (selectedRowData.thuCnt > 0){ weekCode = "WEEK_CD.THU"; }
			if (selectedRowData.friCnt > 0){ weekCode = "WEEK_CD.FRI"; }
			selectedRowData.weekCode = weekCode;
			orderPopupUtil.open("interfaceDayChangePopup", selectedRowData, function(){
				initOrderGoodsGrid();
				initShippingGrid();
				initOrderHistoryGrid();
			});
		});

		// 상품/결제정보 & 클레임정보 >  택배사 / 송장번호(배송추적 팝업)
		$('#ng-view').on("click","div[name=trackingNoSearchForOrderClaim]",function(e){
			let gridSelect = $(this).closest("div[data-role=grid]").attr("id");
			var selectedRowData = "";
			if(gridSelect != "claimGoodsGrid") {
				selectedRowData = orderGoodsGrid.dataItem($(this).closest('tr'));
			} else  {
				selectedRowData = claimGoodsGrid.dataItem($(this).closest('tr'));
			}

			if (selectedRowData.trackingYn == 'Y') {
				let url 	= '/admin/order/delivery/getDeliveryTrackingList';
				let params	= new Object();
				params.logisticsCd 		= selectedRowData.logisticsCd;
				params.trackingNo 		= selectedRowData.trackingNo;

				fnAjax({
					url     : url,
					params  : params,
					async   : true,
					success : function(resultData) {
						if (resultData.responseCode != 0) {
							fnKendoMessage({message: resultData.responseMessage});
							return false;
						}

						if (fnIsEmpty(resultData.tracking) || resultData.tracking.length < 1) {
							fnKendoMessage({message : "배송정보가 존재하지 않습니다."});
							return false;
						}

						let paramObj = new Object();
						paramObj.shippingCompNm		= selectedRowData.shippingCompNm;	// 택배사명
						paramObj.trackingNo			= selectedRowData.trackingNo;		// 송장번호
						paramObj.psShippingCompId 	= selectedRowData.psShippingCompId;	// 택배사코드
						paramObj.trackingArr		= resultData.tracking;				// 배송상태 타임라인 리스트
						paramObj.iframeYn			= "N";								// Iframe여부
						orderPopupUtil.open("deliverySearchPopup", paramObj);
					},
					fail : function(resultData, resultCode) {
						fnKendoMessage({message : resultCode.message});
					},
					isAction : 'select'
				});
			} else {
				e.preventDefault();
				if (fnIsEmpty(selectedRowData.trackingUrl)) {
					fnKendoMessage({message : "택배사 배송추적 URL정보가 존재하지 않습니다."});
					return false;
				}

				let paramObj = new Object();
				paramObj.httpRequestTp		= selectedRowData.httpRequestTp;	// HTTP 전송방법
				paramObj.trackingUrl		= selectedRowData.trackingUrl;		// 송장추적 URL
				paramObj.trackingNo 		= selectedRowData.trackingNo;		// 송장번호
				paramObj.invoiceParam		= selectedRowData.invoiceParam;		// 송장파라미터 명
				paramObj.iframeYn			= "Y";								// Iframe여부
				orderPopupUtil.open("deliverySearchPopup", paramObj);
			}
		});

		// 배송 정보 > 송장번호(배송추적 팝업)
		$('#ng-view').on("click","div[name=trackingNoSearch]",function(e){

			var shippingInfo = $("#"+this.id).data();
			var trackingYn = shippingInfo.trackingYn;
			var shippingCompId = shippingInfo.shippingCompId;
			var trackingNo = $("#"+this.id).text();
			var shippingCompNm = shippingInfo.shippingCompNm;
			var httpRequestTp = shippingInfo.httpRequestTp;
			var trackingUrl = shippingInfo.trackingUrl;
			var invoiceParam = shippingInfo.invoiceParam;
			var logisticsCd = shippingInfo.logisticscd;

			if(this.id == "trackingNoSearch") {
				shippingInfo = $(this).closest('div[data-tracking-url]').data();
				trackingNo = $(this).closest('div[data-tracking-url]').text();
				trackingYn = shippingInfo.trackingYn;
				shippingCompId = shippingInfo.shippingCompId;
				shippingCompNm = shippingInfo.shippingCompNm;
				httpRequestTp = shippingInfo.httpRequestTp;
				trackingUrl = shippingInfo.trackingUrl;
				invoiceParam = shippingInfo.invoiceParam;
				logisticsCd = shippingInfo.logisticscd;

			}

			let areaId = $(this).closest('div[data-role=grid]').attr("id");
			var url = "/admin/order/delivery/getDeliveryTrackingList";
			// 클레임 회수 정보 일 경우 URL 변경
			if(areaId == 'claimReturnShippingGrid') {
				url = '/admin/order/delivery/getOrderClaimReturnDeliveryTrackingList'
			}

			if (trackingYn == 'Y') {
				let params	= new Object();

				params.logisticsCd 		= logisticsCd; // psShippingCompId
				params.trackingNo 		= trackingNo; // trackingNo

				fnAjax({
					url     : url,
					params  : params,
					async   : true,
					success : function(resultData) {
						if (resultData.responseCode != 0) {
							fnKendoMessage({message: resultData.responseMessage});
							return false;
						}

						if (fnIsEmpty(resultData.tracking) || resultData.tracking.length < 1) {
							fnKendoMessage({message : "배송정보가 존재하지 않습니다."});
							return false;
						}

						let paramObj = new Object();
						paramObj.shippingCompNm		= shippingCompNm;			// shippingCompNm (택배사명)
						paramObj.trackingNo			= trackingNo;			// trackingNo (송장번호)
						paramObj.psShippingCompId 	= shippingCompId;			// psShippingCompId (택배사코드)
						paramObj.trackingArr		= resultData.tracking;				// 배송상태 타임라인 리스트
						paramObj.iframeYn			= "N";								// Iframe여부
						orderPopupUtil.open("deliverySearchPopup", paramObj);
					},
					fail : function(resultData, resultCode) {
						fnKendoMessage({message : resultCode.message});
					},
					isAction : 'select'
				});
			} else {
				e.preventDefault();
				if (fnIsEmpty(shippingInfo.trackingUrl)) {
					fnKendoMessage({message : "택배사 배송추적 URL정보가 존재하지 않습니다."});
					return false;
				}
				let paramObj = new Object();
				paramObj.httpRequestTp		= httpRequestTp;			// httpRequestTp (HTTP 전송방법)
				paramObj.trackingUrl		= trackingUrl;			// trackingUrl (송장추적 URL)
				paramObj.trackingNo 		= trackingNo;			// trackingNo (송장번호)
				paramObj.invoiceParam		= invoiceParam;			// invoiceParam (송장파라미터 명)
				paramObj.iframeYn			= "Y";								// Iframe여부
				orderPopupUtil.open("deliverySearchPopup", paramObj);
			}
		});

		// 상품정보 > 증정품 내역 팝업
		$('#ng-view').on("click", "#orderGiftListPopupBtn", function(e) {
			let param = {};
			param.odOrderId = odOrderId;
			orderPopupUtil.open("orderGiftListPopup", param, null);
		});


		// 클레임상품정보 > 첨부파일보기 팝업
		$('#ng-view').on("click", "#orderClaimAttcBtn", function(e) {
			var selectedRowData = claimGoodsGrid.dataItem($(this).closest('tr'));
			let param = {};
			param.odClaimId = selectedRowData.odClaimId;
			let initGrid = { //success 함수
				initGridSucess : function initShippingGrid(){
					orderMgmFunctionUtil.initShippingGrid(odOrderId);
				}
			};
			orderPopupUtil.open("claimReasonMsgPopup", param, initGrid.initGridSucess);
		});

		// 결제정보 > 즉시 할인 내역
		$('#ng-view').on("click", "#immediateDiscountBtn", function(e) {
			let param = {};
			param.odOrderId = odOrderId;
			orderPopupUtil.open("discountHistPopup", param, null);
		});

		// 주문자정보 > 회원정보상세 버튼
		$('#ng-view').on("click", "#userDetailBtn", function(e) {
			let param = {};
			param.urUserId =$('#urUserId').text();
			orderPopupUtil.open("buyerPopup", param, null);
		});

		// 배송정보 > 배송정보 > 수취 정보 변경 팝업
		$('#shippingGrid').on("click", "button[kind=shippingListEdit]", function(e) {
			e.preventDefault();
			let dataItem = shippingGrid.dataItem($(e.currentTarget).closest('tr'));

			orderPopupUtil.open("receiverMgmPopup", dataItem, function(){
				initOrderGoodsGrid();
				initShippingGrid();
				initOrderHistoryGrid();
			});
		});

		//  배송정보 > 배송정보 > 수취 정보 변경 내역 팝업
		$('#shippingGrid').on("click", "button[kind=shippingHistList]", function(e) {
			e.preventDefault();
			let dataItem = shippingGrid.dataItem($(e.currentTarget).closest('tr'));
			let param = {};
			param.odShippingZoneId = dataItem.odShippingZoneId;
			param.urUserId = dataItem.urUserId;
			param.odOrderId = odOrderId; // 주문번호 > 변경이력 팝업에 사용
			param.odOrderDetlId = dataItem.odOrderDetlId;
			orderPopupUtil.open("receiverHistPopup", param, null);
		});

	}; //end fnInitPopups();

	//bindEvents()
	function bindEvents() {
		// 테이블 접기 버튼
		$(".table-toggleBtn").on("click", function(e) {
			const $table = $(this).closest('table');

			$table.toggleClass('fold');
		});




		// 상품 체크박스 클릭에 의한 주문상태변경버튼 활성화
		$('#ng-view').on("click","input[name=rowCheckbox]",function(e){
			let gridSelect = $(this).closest("div[data-role=grid]").attr("id");
			let checkboxSize = $(this).closest("tbody").find("input[type=checkbox]").length;
			let checkedCheckboxSize = $(this).closest("tbody").find("input[type=checkbox]:checked").length;

			if(checkboxSize == checkedCheckboxSize) {
				$(this).closest("div[data-role=grid]").find("div.k-grid-header input[type=checkbox]").prop("checked", true);
			}
			else {
				$(this).closest("div[data-role=grid]").find("div.k-grid-header input[type=checkbox]").prop("checked", false);
			}

			let selectRow = $(this).closest("tr");
			if(gridSelect != "claimGoodsGrid") {
				let gridData = orderGoodsGrid.dataItem($(selectRow));
				if(gridData.orderStatusCode == "IR") {
					orderDetailGridEventUtil.fnVirtualAccountCheckBox($(this).is(":checked"));
				}
				else {
					orderDetailGridEventUtil.orderStatusChange(gridSelect);
				}
			}
			else {
				orderDetailGridEventUtil.orderStatusChange(gridSelect);
			}
		});

		$('#ng-view').on("click", ".k-i-arrow-chevron-up", function(e){
			return false;
		});


		// 클레임정보 > 클레임상품정보 > 클레임 번호 클릭 시
		$('#ng-view').on("click", "div.odClaimIdClick", function(e) {
			e.preventDefault();
			var rowspan = $(e.currentTarget).closest('td').attr('rowspan');
			var rowIndex = $(e.currentTarget).closest('tr').index();

			var _data = claimGoodsGrid.dataSource.data();
			var _dataArr = [];

			var goodSearchList = new Array();

			if(rowspan == undefined || rowspan == "") {
				_dataArr = claimGoodsGrid.dataItem($(e.currentTarget).closest('tr'));
				var goodOrgData = {};
				goodOrgData.claimCnt = _dataArr.cancelCnt;
				goodOrgData.odOrderId = odOrderId;
				goodOrgData.odOrderDetlId = _dataArr.odOrderDetlId;
				goodOrgData.odClaimDetlId = _dataArr.odClaimDetlId;
				goodOrgData.urWarehouseId = _dataArr.urWarehouseId;
				goodOrgData.claimGoodsYn = "N";

				goodSearchList.push(goodOrgData);

			} else {
				_dataArr = _data.slice(rowIndex, rowIndex + Number(rowspan));

				for(var i=0; i < _dataArr.length; i++) {
					var goodOrgData = {};
					goodOrgData.claimCnt = _dataArr[i].cancelCnt;
					goodOrgData.odOrderId = odOrderId;
					goodOrgData.odOrderDetlId = _dataArr[i].odOrderDetlId;
					goodOrgData.odClaimDetlId = _dataArr[i].odClaimDetlId;
					goodOrgData.urWarehouseId = _dataArr[i].urWarehouseId;
					goodOrgData.claimGoodsYn = "N";

					goodSearchList.push(goodOrgData);
				}
			}

			var isSubmit = false;
			var selectedRowClaimData = claimGoodsGrid.dataItem($(e.currentTarget).closest('tr'));
			//클레임상세팝업 parameter set
			let putOrderStatusInfo = {};
			let claimData = {};
			claimData.odid = $('#odid').val(); //odid
			claimData.urUserId = $('#urUserId').text(); //urUserId
			claimData.guestCi = $('#guestCi').text(); //guestCi
			claimData.goodSearchList = goodSearchList;
			claimData.orderStatusCd = selectedRowClaimData.claimStatusCode;
			claimData.orderStatus = selectedRowClaimData.claimStatus;
			claimData.odOrderId = odOrderId; //주문pk
			claimData.odClaimId = selectedRowClaimData.odClaimId; //주문클레임pk
			claimData.goodsChange = 0;
			claimData.successDo = false;

			fnKendoPopup({
				id: 'claimMgmBosClaimReasonPopup',
				title: 'BOS 클레임 사유 변경',
				param: claimData,
				src: '#/claimMgmBosClaimReasonPopup',
				width: '810px',
				height: '300px',
				success: function(id, data) {
					if(data.parameter != undefined){
						if(data.parameter.successDo == true) {
							odid = data.parameter.odid;
							fnInitGrids();
						} else {
							return false;
						}
					} else {
						return false;
					}
				}
			});
			//window.open("#/itemMgmModify?ilItemCode="+ilItemCd,"_blank");
		});

		// 클레임정보 > 클레임상품정보 > 쇼핑몰클레임사유 내 [클레임상세]버튼
		$('#claimGoodsGrid').on("click", "button[kind=claimDetlBtn]", function(e) {
			e.preventDefault();

			var rowspan = $(e.currentTarget).closest('td').attr('rowspan');
			var rowIndex = $(e.currentTarget).closest('tr').index();

			var _data = claimGoodsGrid.dataSource.data();
			var _dataArr = [];

			var goodSearchList = new Array();

			if(rowspan == undefined || rowspan == "") {
				_dataArr = claimGoodsGrid.dataItem($(e.currentTarget).closest('tr'));
					var goodOrgData = {};
					goodOrgData.claimCnt = _dataArr.cancelCnt;
					goodOrgData.odOrderId = odOrderId;
					goodOrgData.odOrderDetlId = _dataArr.odOrderDetlId;
					goodOrgData.odClaimDetlId = _dataArr.odClaimDetlId;
					goodOrgData.urWarehouseId = _dataArr.urWarehouseId;
					goodOrgData.claimGoodsYn = "N";

					goodSearchList.push(goodOrgData);

			} else {
				_dataArr = _data.slice(rowIndex, rowIndex + Number(rowspan));

				for(var i=0; i < _dataArr.length; i++) {
					var goodOrgData = {};
					goodOrgData.claimCnt = _dataArr[i].cancelCnt;
					goodOrgData.odOrderId = odOrderId;
					goodOrgData.odOrderDetlId = _dataArr[i].odOrderDetlId;
					goodOrgData.odClaimDetlId = _dataArr[i].odClaimDetlId;
					goodOrgData.urWarehouseId = _dataArr[i].urWarehouseId;
					goodOrgData.claimGoodsYn = "N";

					goodSearchList.push(goodOrgData);
				}
			}

			var isSubmit = false;
			var selectedRowClaimData = claimGoodsGrid.dataItem($(e.currentTarget).closest('tr'));
			var json = JSON.parse(selectedRowClaimData.bosJson);
			// 변경할 주문상태값
			var btnOrderStatusCd = $('button[name='+json[1].rows[0].actionRows[0].actionExecId+']').val();
			var btnOrderStatus = $('button[name='+json[1].rows[0].actionRows[0].actionExecId+']').html();

			//클레임상세팝업 parameter set
			let putOrderStatusInfo = {};
			let claimData = {};
			claimData.odid = $('#odid').val(); //odid
			claimData.urUserId = $('#urUserId').text(); //urUserId
			claimData.guestCi = $('#guestCi').text(); //guestCi

			claimData.goodSearchList = goodSearchList;

			claimData.orderStatusCd = selectedRowClaimData.claimStatusCode;
			claimData.orderStatus = selectedRowClaimData.claimStatus;
			putOrderStatusInfo = orderStatusChangeUtil.fnPutOrderStatus(selectedRowClaimData.claimStatusCode, btnOrderStatusCd);

			claimData.putOrderStatusCd = putOrderStatusInfo.putOrderStatusCd; //주문상태코드(변경)
			if(putOrderStatusInfo.putOrderStatus == ""){
				claimData.putOrderStatus = btnOrderStatus; //주문상태코드명(변경) 변화 X
			}else{
				claimData.putOrderStatus = putOrderStatusInfo.putOrderStatus; //주문상태코드명(변경)
			}
			//end

			claimData.odOrderId = odOrderId; //주문pk
			claimData.odClaimId = selectedRowClaimData.odClaimId; //주문클레임pk
			claimData.goodsChange = 0;
			claimData.btnOrderStatusCd = btnOrderStatusCd;
			claimData.successDo = false;

			fnKendoPopup({
				id: 'claimMgmPopup',
				title: '클레임 상세',
				param: claimData,
				src: '#/claimMgmPopup',
				width: '1800px',
				height: '1500px',
				success: function(id, data) {
					if(data.parameter != undefined){
						if(data.parameter.successDo == true) {
							odid = data.parameter.odid;
							fnInitGrids();
						} else {
							return false;
						}
					} else {
						return false;
					}
				}
			});

		});

		// 주문 상태 변경
		$('#ng-view').on("click", "#putOrderStatusBtnArea > button",function(){

			var isSubmit = false;
			let tapSelect = $(this).parents().eq(4).attr("id"); //tap id 찾기
			let checkbox = $("#"+tapSelect).find('input[name=rowCheckbox]:checked');
			// 변경할 주문상태값
			var btnOrderStatusCd = $(this).val();
			var btnOrderStatus = $(this).text();
			var selectedRowClaimData; // select한 row데이터
			var selectRowsClaim = checkbox.closest('tr'); //탭별 선택한 데이터

			//클레임상세팝업 parameter set
			let putOrderStatusInfo = {};
			let claimData = {};
			claimData.odid = $('#odid').val(); //odid
			claimData.urUserId = $('#urUserId').text(); //urUserId
			claimData.guestCi = $('#guestCi').text(); //guestCi
			var goodSearchList = new Array();
			for(var i = 0 ; i < selectRowsClaim.length ; i++){
				let goodOrgData = {};
				if(tapSelect != "tabstrip-2") {
					selectedRowClaimData = orderGoodsGrid.dataItem($(selectRowsClaim[i]));
					goodOrgData.claimCnt = selectedRowClaimData.claimAbleCnt;
				} else  {
					selectedRowClaimData = claimGoodsGrid.dataItem($(selectRowsClaim[i]));
					goodOrgData.claimCnt = selectedRowClaimData.cancelCnt;
				}
				if(btnOrderStatusCd != 'CS' && goodOrgData.claimCnt < 1) {
					continue;
				}
				goodOrgData.odOrderId = odOrderId;
				goodOrgData.odOrderDetlId = selectedRowClaimData.odOrderDetlId;
				goodOrgData.odClaimDetlId = selectedRowClaimData.odClaimDetlId;
				goodOrgData.urWarehouseId = selectedRowClaimData.urWarehouseId;
				goodOrgData.claimGoodsYn = "N";

				goodOrgData.goodsTpCd = selectedRowClaimData.goodsTpCd;
				goodOrgData.goodsDailyTp = selectedRowClaimData.goodsDailyTp;
				goodOrgData.odOrderDetlParentId	= selectedRowClaimData.odOrderDetlParentId;
				goodOrgData.promotionTp	= selectedRowClaimData.promotionTp;
				goodOrgData.orderStatusCd	= selectedRowClaimData.orderStatusCode;
				goodOrgData.ilGoodsId = selectedRowClaimData.ilGoodsId;
				goodOrgData.odOrderDetlSeq = selectedRowClaimData.odOrderDetlSeq;

				goodSearchList.push(goodOrgData);
			}
			claimData.goodSearchList = goodSearchList;

			claimData.oriOrderStatusCd = selectedRowClaimData.orderStatusCode;

			if(tapSelect != "tabstrip-2") {
				claimData.orderStatusCd = selectedRowClaimData.orderStatusCode;
				claimData.orderStatus = selectedRowClaimData.orderStatus;
				putOrderStatusInfo = orderStatusChangeUtil.fnPutOrderStatus(selectedRowClaimData.orderStatusCode, btnOrderStatusCd);
			} else {
				claimData.orderStatusCd = selectedRowClaimData.claimStatusCode;
				claimData.orderStatus = selectedRowClaimData.claimStatus;
				putOrderStatusInfo = orderStatusChangeUtil.fnPutOrderStatus(selectedRowClaimData.claimStatusCode, btnOrderStatusCd);
			}
			claimData.putOrderStatusCd = putOrderStatusInfo.putOrderStatusCd; //주문상태코드(변경)
			if(putOrderStatusInfo.putOrderStatus == ""){
				claimData.putOrderStatus = btnOrderStatus; //주문상태코드명(변경) 변화 X
			}else{
				claimData.putOrderStatus = putOrderStatusInfo.putOrderStatus; //주문상태코드명(변경)
			}
			//end

			claimData.odOrderId = odOrderId; //주문pk
			claimData.odClaimId = selectedRowClaimData.odClaimId; //주문클레임pk
			claimData.goodsChange = 0;
			claimData.btnOrderStatusCd = btnOrderStatusCd; //버튼주문상태코드(변경-하드코딩) 'CS'
			claimData.btnOrderStatus = btnOrderStatus; //버튼주문상태코드명(변경-하드코딩) 'CS환불'
			claimData.successDo = false;

			/*************************************************************************************************/
			/* 배송준비중/배송중/구매확정 */
			// 현재 주문상태값
			var orderStatusCd = "";
			var detlIdList = [];
			var detlOrderStatusCdList = [];

			let params = {};
			params.detlIdList           = [];
			params.psShippingCompIdList = [];
			params.trackingNoList       = [];
			params.successDo			= false;

			if( selectRowsClaim.length > 0){

				for(var i = 0 ; i < selectRowsClaim.length ; i++){
					if(tapSelect != "tabstrip-2") {
						selectedRowClaimData = orderGoodsGrid.dataItem($(selectRowsClaim[i]));
					} else  {
						selectedRowClaimData = claimGoodsGrid.dataItem($(selectRowsClaim[i]));
					}

					params.detlIdList[i] = selectedRowClaimData.odOrderDetlId;

					if ("DI" == btnOrderStatusCd) { // 배송중으로 변경시
						let psShippingCompId    = $.trim(selectedRowClaimData.psShippingCompId);
						let trackingNo          = $.trim(selectedRowClaimData.trackingNo);
						if (psShippingCompId != "" && trackingNo != "") {
							params.psShippingCompIdList[i]  = selectedRowClaimData.psShippingCompId;
							params.trackingNoList[i]        = selectedRowClaimData.trackingNo;
						}

						params.goodsDeliveryType = selectedRowClaimData.goodsDeliveryType;
					}
				}
			}
			params.statusCd = btnOrderStatusCd;

			// 상태값 업데이트
			if (btnOrderStatusCd == 'DR'
				|| btnOrderStatusCd == 'BF'
				|| btnOrderStatusCd == 'DC') {
				fnKendoMessage({message : $.trim(btnOrderStatus)+' 상태로 변경하시겠습니까?', type : "confirm", ok : function(){
						fnAjax({
							url: '/admin/order/status/putOrderDetailStatus',
							params: params,
							success:
								function (data) {
									if(data.message != "" && data.message != undefined) {
										fnKendoMessage({
											message:data.message
										});
									}else{
										var msg = "[총 건수] : " + data.totalCount + "<BR>" +
											" [성공 건수] : " + data.successCount + "<BR>" +
											" [실패 건수] : " + data.failCount + "<BR><BR>" ;
										fnKendoMessage({ message : msg + "저장이 완료되었습니다.", ok : fnInitGrids() });
									}
								},
							isAction: 'select'
						});
					}});

				// 송장번호 입력 팝업 처리 후 상태값 업데이트
			} else if (btnOrderStatusCd == 'DI') {

				fnKendoPopup({
					id: "deliveryInfoPopup",
					title: "배송정보",
					src: "#/deliveryInfoPopup",
					param: params,
					width: "500px",
					height: "400px",
					minWidth: 500,
					success: function (id, data) {
						if(data.parameter != undefined){
							if(data.parameter.successDo == true) {
								fnInitGrids();
							} else {
								return false;
							}
						} else {
							return false;
						}
					}
				});
			} else if (btnOrderStatusCd == 'CS') {
				fnKendoPopup({
					id: 'claimMgmCSPopup',
					title: '클레임 상세',
					param: claimData,
					src: '#/claimMgmCSPopup',
					width: '1800px',
					height: '1500px',
					success: function(id, data) {
						if(data.parameter != undefined){
							if(data.parameter.successDo == true) {
								odid = data.parameter.odid;
								fnInitGrids();
							} else {
								return false;
							}
						} else {
							return false;
						}
					}
				});
			} else if(btnOrderStatusCd == 'C' && claimData.orderStatusCd == "IR") {
				fnKendoMessage({message : '입금 전 취소 처리를 진행하시겠습니까?', type : "confirm", ok : function(){
					claimData.targetTp = 'S';
					claimData.claimStatusCd = 'IB';
					claimData.claimStatusTp = 'CLAIM_STATUS_TP.CANCEL';
					claimData.goodsInfoList = claimData.goodSearchList;
					claimData.psClaimMallId = 0;

					fnAjax({
						url: '/admin/order/claim/getOrderClaimView',
						params: claimData,
						contentType: "application/json",
						success:
							function (data) {
								if(fnNvl(data.goodsCouponList) != "") {
									claimData.goodsCouponInfoList = data.goodsCouponList;
								}
								if(fnNvl(data.cartCouponList) != "") {
									claimData.cartCouponInfoList = data.cartCouponList;
								}

								let claimGoodsInfoList = new Array();
								data.orderGoodList.forEach(function(el){

									// 묶음상품 대표상품 제외
									if(el.odOrderDetlSeq != 0){
										claimGoodsInfoList.push(el);
									}
								});

								claimData.goodsInfoList = claimGoodsInfoList;

								fnAjax({
									url: '/admin/order/claim/addOrderClaimRegister',
									params: claimData,
									contentType: "application/json",
									success:
										function (data) {
											fnKendoMessage({message: '입금전 취소 처리가 완료 되었습니다.', ok : function () {
												window.location.reload(true);
											}});
										},
									isAction: 'batch'
								});
							},
						isAction: 'batch'
					});
				}});
			} else {
				var i = 0;
				var scheduleGoodsCnt = 0;
				var icCount = 0;
				if(goodSearchList.length > 1){
					while (i < goodSearchList.length) {
						if (goodSearchList[i].goodsTpCd == 'GOODS_TYPE.DAILY' && goodSearchList[i].goodsDailyTp == 'GOODS_DAILY_TP.GREENJUICE'
							&& goodSearchList[i].promotionTp != 'CART_PROMOTION_TP.GREENJUICE_SELECT') {
							// fnKendoMessage({message: '녹즙 일일배송 상품은 단품만 반품처리 가능합니다.'});
							// return false;
							// break;
							scheduleGoodsCnt++;
						}
						if(goodSearchList[i].orderStatusCd == "IC") {
							icCount++;
						}
						i++;
					}
					if(scheduleGoodsCnt > 0 && goodSearchList.length != icCount) {
						fnKendoMessage({message: '녹즙 일일배송 상품은 단품만 반품처리 가능합니다.'});
						return false;
					}
				}
				if((!isHitokSwitch || profilesActive == 'ver') //하이톡 IF 이거나 canary서버일 경우
					&& selectedRowClaimData.goodsTpCd  == 'GOODS_TYPE.DAILY' 
					&& selectedRowClaimData.goodsDailyTp == 'GOODS_DAILY_TP.GREENJUICE' 
					&& selectedRowClaimData.batchExecFl == 'Y'){
					fnKendoPopup({
						id: 'claimMgmGreenJuicePopup',
						title: '클레임 상세',
						param: claimData,
						src: '#/claimMgmGreenJuicePopup',
						width: '1800px',
						height: '1500px',
						success: function(id, data) {
							if(data.parameter != undefined){
								if(data.parameter.successDo == true) {
									odid = data.parameter.odid;
									fnInitGrids();
								} else {
									return false;
								}
							} else {
								return false;
							}
						}
					});
				} else {
					if(selectedRowClaimData.goodsTpCd  == 'GOODS_TYPE.DAILY' && selectedRowClaimData.goodsDailyTp == 'GOODS_DAILY_TP.GREENJUICE' && btnOrderStatusCd == 'EC') {
						fnKendoMessage({message: '녹즙 일일배송 상품은 배송 전 재배송 처리가 불가능합니다.'});
						return false;
					}
					//if((btnOrderStatusCd == "R" && tapSelect == "tabstrip-2") || (btnOrderStatusCd == "C" && tapSelect == "tabstrip-2")) {
					if(btnOrderStatusCd == "R" && tapSelect == "tabstrip-2") {

						let checkbox = $("#tabstrip-2").find('input[name=rowCheckbox]:checked:eq(0)');
						let tr = checkbox.closest('tr');
						let trData = claimGoodsGrid.dataItem($(tr));
						let odClaimId = trData.odClaimId;
						let claimDataList = $("#claimGoodsGrid").data("kendoGrid").dataSource.data();
						for(var i=0; i<claimDataList.length; i++) {
							if(odClaimId == claimDataList[i].odClaimId) {
								tr = $("#tabstrip-2 #claimGoodsGrid table[role=grid]").find('tr:eq(' + i + ')');
								break;
							}
						}
						var rowspan = $(tr).find("td:eq(3)").attr('rowspan');
						var rowIndex = tr.index();

						var _data = claimGoodsGrid.dataSource.data();
						var _dataArr = [];

						goodSearchList = new Array();

						if(rowspan == undefined || rowspan == "") {
							_dataArr = claimGoodsGrid.dataItem(tr);
							var goodOrgData = {};
							goodOrgData.claimCnt = _dataArr.cancelCnt;
							goodOrgData.odOrderId = odOrderId;
							goodOrgData.odOrderDetlId = _dataArr.odOrderDetlId;
							goodOrgData.odClaimDetlId = _dataArr.odClaimDetlId;
							goodOrgData.urWarehouseId = _dataArr.urWarehouseId;
							goodOrgData.claimCnt = _dataArr.cancelCnt;
							goodOrgData.claimGoodsYn = "N";

							goodSearchList.push(goodOrgData);

						} else {
							_dataArr = _data.slice(rowIndex, rowIndex + Number(rowspan));

							for(var i=0; i < _dataArr.length; i++) {
								var goodOrgData = {};
								goodOrgData.claimCnt = _dataArr[i].cancelCnt;
								goodOrgData.odOrderId = odOrderId;
								goodOrgData.odOrderDetlId = _dataArr[i].odOrderDetlId;
								goodOrgData.odClaimDetlId = _dataArr[i].odClaimDetlId;
								goodOrgData.urWarehouseId = _dataArr[i].urWarehouseId;
								goodOrgData.claimCnt = _dataArr[i].cancelCnt;
								goodOrgData.claimGoodsYn = "N";

								goodSearchList.push(goodOrgData);
							}
						}
						claimData.goodSearchList = goodSearchList;
					}
					fnKendoPopup({
						id: 'claimMgmPopup',
						title: '클레임 상세',
						param: claimData,
						src: '#/claimMgmPopup',
						width: '1800px',
						height: '1500px',
						success: function(id, data) {
							if(data.parameter != undefined){
								if(data.parameter.successDo == true) {
									odid = data.parameter.odid;
									fnInitGrids();
								} else {
									return false;
								}
							} else {
								return false;
							}
						}
					});
				}
			}
		});

		// 선물정보 > 수신자 Keyup
		$('#presentReceiveNm').on('keyup', function(e) {
			let presentReceiveNmLength = $(this).val().length;
			if(presentReceiveNmLength > 20) {
				fnKendoMessage({message : '수신자 정보를 확인해주세요.'});
				$('#presentReceiveNm').val($(this).val().substring(0,20))
			}
		});

		// 선물정보 >  연락처 Keyup
		$("#presentReceiveHp_prefix").on("keyup", function(e) {
			var self = $(this);
			if( self.val().trim().length === 3 ) {
				$("#presentReceiveHp_phone1").focus();
			}
		})
		$("#presentReceiveHp_phone1").on("keyup", function(e) {
			var self = $(this);
			if( self.val().trim().length === 4 ) {
				$("#presentReceiveHp_phone2").focus();
			}
		})

		// 선물정보 > 메세지 Keyup
		$('#presentCardMsg').on('keyup', function(e) {
			let presentCardMsgLength = $(this).val().length;
			if(presentCardMsgLength > 50) {
				fnKendoMessage({message : '최대 50자까지 입력가능합니다.'});
				$('#presentCardMsg').val($(this).val().substring(0,50))
			}
		});

	}//end bindEvents();

	////////////////주문상태변경 --------------

	//주문 상태변경 버튼 초기화


	//-------------------------------  events End  -------------------------------

	//-------------------------------  Common Function start ----------------------------------

	function initTabStrip() {
		tabStrip = $("#tabstrip").kendoTabStrip({
			animation: false ,
			activate : function(e) {
				var thisId = $(e.item).attr('id');
			}
		}).data("kendoTabStrip");
		tabStrip.activateTab( $('#defaultTab') );
	}

	// 주문 상태 처리 버튼 생성
	function initOrderMgmButtons() {
		const template = $("#orderButton-template").html();
		const $btnConts = $(".tab-cont__btnCont");

		$btnConts.each(function() {
			$(this).html(template);
		});

		// 매장배송 정보 조회
		fnAjax({
			url     : '/admin/order/getOrderShopStoreInfo',
			params  : {odOrderId : odOrderId},
			async   : false,
			success :
				function( data ){
					if(data.rows != null) {
						let str = data.rows.urStoreNm + ' - ' + data.rows.deliveryDt + ' ' + data.rows.deliveryType ;
							str += " ( " + data.rows.storeScheduleNo + " ) 회차" + " ( " + data.rows.storeStartTime + " ~ " + data.rows.storeEndTime + " )";

							$('#orderShopStore').css("display", "");
							$('#orderShopStore').val(str);
					}
				},
			isAction : 'select'
		});
	}

	function fnBizCallback(id, data) {
		switch(id){
			case 'select':
				$('#inputForm').data("data", data);
				$('#inputForm').bindingForm(data, 'rows', true);
				break;

		}
	}

	//-------------------------------  Common Function end ------------------------------------


	// ------------------------------- Html 버튼 바인딩 Start
	/** Common Save*/
	$scope.fnSave = function() { fnSave();};
	$scope.fnSaveList = function(id) { fnSaveList(id);};
	$scope.fnUpdate = function(id, msg){ fnUpdate(id, msg);}; //수정
	$scope.fnDel = function(id){   fnDel(id);}; // 삭제
	$scope.fnPresentSave = function(){ fnPresentSave();}; // 선물정보 > 저장
	$scope.fnPresentMsgSend = function(){ fnPresentMsgSend();}; // 선물정보 > 메세지 재발송

	$scope.fnChangeStatus = function(obj) {
		orderSubmitUtil.changeStatus('/admin/order/status/putOrderDetailStatus', $(obj).data('msg'), $(obj).data('stat'));
	};

	// ------------------------------- Html 버튼 바인딩 End
}); // document ready - END
