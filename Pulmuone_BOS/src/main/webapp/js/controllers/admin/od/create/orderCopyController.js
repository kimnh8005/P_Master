/**----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 주문 생성 > 주문 복사
 * @
 * @ 수정일			수정자          수정내용
 * @ --------------------------------------------------------------------------
 * @ 2021.02.23     이규한		최초 생성
 * **/

'use strict';

// 상품정보 그리드
var orderGoodsGrid, orderGoodsGridOpt
// 배송정보 그리드
var shippingGrid, shippingGridOpt;
// GET Parameter
var pageParam = fnGetPageParam();
// 주문 ID 전역변수
var s_odOrderId;
// 그리드 NoData 메세지
var noRecodeMessage = "데이터가 존재하지 않습니다.";

var orderCopySellersGroupCd = "";

var orderCopyOdOrderId = "";

var orderCopyOdid = "";

var checkOrderCopySellersGroupCd = "SELLERS_GROUP.MALL";

var orderCopySellersGroupMsg = "통합몰 주문이 아닌경우 변경 할 수 없습니다.";

// 결제 정보 Array(통합몰 회원주문용, 통합몰 비회원 주문용, 외부몰 주문용)
var payTpCdArr = new Array(), payTpCdArr1 = new Array(), payTpCdArr2 = new Array(), payTpCdArr3 = new Array();

// 상품정보 머지할 필드 목록
var orderGoodsMergeFieldArr			= ['shippingPrice', 'warehouseNm', 'trackingNo'];
// 상품정보 그룹할 필드 목록
var orderGoodsMergeGroupFieldArr 	= ['shippingPrice', 'warehouseNm', 'trackingNo'];

var isValidBankAccount = false;

var isHitokSwitch = null; //하이톡 스위치

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

	// Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', { flag : true });
		fnPageInfo({
			PG_ID  		: 'orderCopy',
			callback 	: fnUI
		});
	}

	// 화면 UI 초기화
	function fnUI() {
		fnTranslate();		// 다국어 변환
		fnInitButton();		// 버튼 초기화
		fnInitGrid();		// Initialize Grid
		fnInitOptionBox();	// Initialize Option Box
		fnInitPopups();		// 팝업관련 초기화
		fnBindEvents();		// 이벤트 바인딩
	}

	//--------------------------------- Button Start-------------------------------------------
	// 버튼 초기화
	function fnInitButton() {
		$('#fnSearch, #fnClear, #fnSave, #fnToggleAreaM, #fnUserDetail, #fnImmediateDiscount').kendoButton();
		$('#fnSave').hide();
	}

	// 주문복사 주문내역 조회
	function fnSearch() {

		var url 		= "/admin/order/copy/getOrderCopyDetailInfo";
		var paramData 	= $('#searchForm').formSerialize(true);

		if (paramData.rtnValid) {
			// 조회 내용 초기화
			fnContentsClear();
			$('#saveOrderIfType').data("kendoDropDownList").select(0);
			$("#saveOrderIfType").data("kendoDropDownList").enable( true );
			$('#savePaymentType').data("kendoDropDownList").select(0);
			$("#savePaymentType").data("kendoDropDownList").enable( true );
			$("#copyTitle").text("주문 조회 결과");
			$('#fnNext').show();
			$('#fnSave').hide();

			fnAjax({
	            url     : url,
	            params  : paramData,
	            async   : true,
	            success : function(resultData) {
	            	fnBizCallback('search', resultData);
	            },
	            fail : function(resultData, resultCode){

	            	if(resultData != undefined && resultData != null){
						fnKendoMessage({
							message : resultData,
							ok : function(e) {
								fnBizCallback("fail", resultData);
							}
						});

					}else{
						fnKendoMessage({
							message : resultCode.message,
							ok : function(e) {
								fnBizCallback("fail", resultData);
							}
						});
					}
				},
	            isAction : 'select'
	        });
		}
	}

	// 초기화
	function fnClear() {
		// 검색 조건 초기화
		$('#searchForm').formClear(true);
		$('#saveOrderIfType').data("kendoDropDownList").select(0);
		orderSearchUtil.getDropDownComm(payTpCdArr, "savePaymentType");
		$('#savePaymentType').data("kendoDropDownList").select(0);

		// 컨텐츠 초기화
		fnContentsClear();
	}

	// 다음
	function fnNext() {

		let shippingDataList = shippingGrid.dataSource._data;
		let selectRowsOrder = $("#orderGoodsGrid").find("input[name=rowCheckbox]:checked").closest("tr");
		let selectedRowList = new Array();
		let selectedRowOrderData; // select한 row데이터
		var selectedRowOrderDetlIdList = [];
		for(let i = 0 ; i < selectRowsOrder.length ; i++){
			selectedRowOrderData = orderGoodsGrid.dataItem($(selectRowsOrder[i]));
			selectedRowOrderDetlIdList[i] = selectedRowOrderData.odOrderDetlId; //주문상세PK

			var selectObj = new Object();
			selectObj.odOrderDetlId 	= selectedRowOrderData.odOrderDetlId;
			selectObj.urWarehouseId 	= selectedRowOrderData.urWarehouseId;
			selectObj.isDawnDelivery 	= selectedRowOrderData.goodsDeliveryType == "GOODS_DELIVERY_TYPE.NORMAL" ? false : true;
			selectObj.ilGoodsId 		= selectedRowOrderData.ilGoodsId;
			selectObj.goodsName 		= selectedRowOrderData.goodsNm;
			selectObj.orderIfType 		= fnNvl($("#saveOrderIfType").val()); // 주문 연동 방법

			for(let j = 0 ; j < shippingDataList.length ; j++){ // 주문배송지 우편번호
				if(shippingDataList[j].odShippingZoneId == selectedRowOrderData.odShippingZoneId){
					selectObj.zipCode = shippingDataList[j].recvZipCd;
				}
			}
			selectedRowList.push(selectObj);
		}


		// 주문복사 유효성 체크
		fnAjax({
			url     : '/admin/order/copy/checkOrderCopyValidation',
			params  :  selectedRowList,
			contentType: "application/json",
			success : function(resultData) {

				if(resultData.result != 'SUCCESS'){

					if ($.trim(resultData.failMessage) != ""){
						fnKendoMessage({message : resultData.failMessage});
					}

				}else{
					//주문 상품의 출고처가 용인물류, 백암물류가 아니면 매출만연동 주문복사 불가능 체크
					fnAjax({
						url     : '/admin/order/copy/getOrderDetlGoodsWarehouseCode',
						params  : {"orderDetlId[]" : selectedRowOrderDetlIdList},
						success : function (responseData) {

							let warehouseFailMessage = "";
							if ($("#saveOrderIfType").val() == "ORDER_IF_TYPE.SAL_IF") {
								for(var k=0;k<responseData.length;k++) {
									if(responseData[k].warehouseCode != "WAREHOUSE_YONGIN_ID" && responseData[k].warehouseCode != "WAREHOUSE_BAEKAM_ID" ) { //용인물류, 백암물류
										warehouseFailMessage += "[" + responseData[k].goodsId + "] " + " 용인물류, 백암물류 출고처가 아니면 매출만연동 주문복사를 할 수 없습니다. <br/>";
									}
								}
							}

							if ($.trim(warehouseFailMessage) != ""){
								fnKendoMessage({message : warehouseFailMessage});
							} else {
								if (fnSaveValid()) {
									$("#saveOrderIfType").data("kendoDropDownList").enable(false);
									$("#savePaymentType").data("kendoDropDownList").enable(false);
									$("#copyTitle").text("주문 상세 내역");

									$("#checkBoxAll").attr("disabled", true);
									$("input[name='rowCheckbox']").attr("disabled", true);
									$('#fnNext').hide();
									$('#fnSave').show();
									$("button[kind=shippingListEdit]").hide();

									$(document).off("click").on("click", "div[name=ifDayChange]", function () {
										return false;
									});
								}
							}

						},
						fail : function(responseData, resultCode){
							fnKendoMessage({
								message : resultCode.message,
								ok : function(e) {
									fnBizCallback("fail", responseData);
								}
							});
						},
						isAction : 'select'
					})
				}
			},
			fail : function(resultData, resultCode){
				fnKendoMessage({
					message : resultCode.message,
					ok : function(e) {
						fnBizCallback("fail", resultData);
					}
				});
			},
			isAction : 'select'
		})

	}

	// 저장
	function fnSave() {
		// 정합성 체크
		if (fnSaveValid()) {
			let paramData 	= $('#saveForm').formSerialize(true);
			let saveParam = new Object();
			saveParam.odOrderId			= s_odOrderId;					//주문PK
			saveParam.odid				= $("#odid").text();					//주문PK
			saveParam.orderIfType 		= paramData.saveOrderIfType;	//주문 연동 방법
			saveParam.paymentType 		= paramData.savePaymentType;	//결제 정보
			saveParam.orderPrice		= $("#paymentPrice").text().replace(/,/ig, '').replace(/ 원/ig, '');
			saveParam.sellersGroupCd	= orderCopySellersGroupCd;
			var selectRowsOrder = $("#orderGoodsGrid").find("input[name=rowCheckbox]:checked").closest("tr");
			var selectedRowOrderData; // select한 row데이터
			let orderGoodsList = new Array();
			var odOrderDetlIds = new Array();
			var repGoodsNm = "";
			var repIlGoodsId = "";
			for(var i = 0 ; i < selectRowsOrder.length ; i++){
				selectedRowOrderData = orderGoodsGrid.dataItem($(selectRowsOrder[i]));
				let goodOrgData = {};

				goodOrgData.orderCnt			= selectedRowOrderData.orderCnt;				//주문수량
				goodOrgData.odOrderDetlId		= selectedRowOrderData.odOrderDetlId;		//주문상세PK
				goodOrgData.odOrderDetlDepthId	= selectedRowOrderData.odOrderDetlDepthId; // 주문상세뎁스
				goodOrgData.orderIfDt			= selectedRowOrderData.orderIfDt;
				goodOrgData.shippingDt			= selectedRowOrderData.shippingDt;
				goodOrgData.deliveryDt			= selectedRowOrderData.deliveryDt;

				orderGoodsList.push(goodOrgData);
				odOrderDetlIds.push(selectedRowOrderData.odOrderDetlId);

				if (i <= 0){
					repGoodsNm		= selectedRowOrderData.goodsNm;
					repIlGoodsId	= selectedRowOrderData.ilGoodsId;
				}
			}
			saveParam.orderGoodsList = orderGoodsList;	//상품 및 주문갯수
			saveParam.repGoodsNm = repGoodsNm;
			saveParam.repIlGoodsId = repIlGoodsId;

			var shippingDatas = $("#shippingGrid").data("kendoGrid").dataSource.data();
			let orderShippingList = new Array();

			for (var i = 0; i < shippingDatas.length; i++) {
				let shippingZoneInfo = {};
				shippingZoneInfo.odShippingZoneId = shippingDatas[i].odShippingZoneId;	//주문 배송지 PK
				shippingZoneInfo.odOrderId = s_odOrderId;					//주문 PK
				shippingZoneInfo.recvNm = shippingDatas[i].orgRecvNm;			//수령인 명
				shippingZoneInfo.recvHp = shippingDatas[i].orgRecvHp;			//수령인 핸드폰
				shippingZoneInfo.recvTel = shippingDatas[i].recvTel;		//수령인 연락처
				shippingZoneInfo.recvMail = shippingDatas[i].recvMail;		//수령인 이메일
				shippingZoneInfo.recvZipCd = shippingDatas[i].recvZipCd;	//수령인 우편번호
				shippingZoneInfo.recvAddr1 = shippingDatas[i].orgRecvAddr1;	//수령인 주소1
				shippingZoneInfo.recvAddr2 = shippingDatas[i].orgRecvAddr2;	//수령인 주소2
				shippingZoneInfo.recvBldNo = shippingDatas[i].recvBldNo;	//건물번호
				shippingZoneInfo.deliveryMsg = shippingDatas[i].deliveryMsg;//배송요청사항
				shippingZoneInfo.doorMsgCd = shippingDatas[i].doorMsgCd;	//출입정보타입
				shippingZoneInfo.doorMsg = shippingDatas[i].doorMsg;		//배송출입 현관 비밀번호
				orderShippingList.push(shippingZoneInfo);
			}
			saveParam.orderShippingList = orderShippingList;	//주문 복사 배송지 목록

			let orderShippingPriceList = new Array();
			let freeShippingPriceYn = fnNvl($("input[name=freeShippingPriceYn]:checked").val()) == "" ? "N" : $("input[name=freeShippingPriceYn]:checked").val();
			let totalShippingPrice = 0;	//전체 배송비

			for (var i = 0; i < shippingDatas.length; i++) {
				let shippingPriceInfo = {};
				shippingPriceInfo.odShippingPriceId = shippingDatas[i].odShippingPriceId;	//주문 배송비 PK
				shippingPriceInfo.ilShippingTmplId = shippingDatas[i].ilShippingTmplId;		//주문 배송정책 PK
				if(freeShippingPriceYn == "Y") {
					shippingPriceInfo.shippingPrice =  0;			//주문 배송비
					totalShippingPrice = totalShippingPrice + shippingDatas[i].shippingPrice;
				} else {
					shippingPriceInfo.shippingPrice =  shippingDatas[i].shippingPrice;			//주문 배송비
				}
				shippingPriceInfo.shippingDiscountPrice = shippingDatas[i].shippingDiscountPrice;	//배송비 할인금액
				orderShippingPriceList.push(shippingPriceInfo);
			}
			saveParam.orderShippingPriceList = orderShippingPriceList;	//주문 복사 배송비 목록
			console.log("orderPrice / totalShippingPrice = " + saveParam.orderPrice + " / " + totalShippingPrice);
			if(freeShippingPriceYn == "Y") {
				saveParam.orderPrice = saveParam.orderPrice - totalShippingPrice;
				console.log("change_orderPrice / totalShippingPrice = " + saveParam.orderPrice + " / " + totalShippingPrice);
			}
			if(paramData.savePaymentType == "PAY_TP.VIRTUAL_BANK") {
				saveParam.bankCode = fnNvl($('#bankCode').data('kendoDropDownList').value()); //BANK_CODE.KB
				saveParam.accountNumber = fnNvl($("#accountNumber").val()); //'환불계좌번호'
				saveParam.holderName = fnNvl($("#holderName").val()); //'예금주'
				console.log(">>> 환불계좌 : " + saveParam.bankCode + " / " + saveParam.accountNumber+ " / " + saveParam.holderName);
				if(saveParam.bankCode == "" || saveParam.accountNumber == "" || saveParam.holderName == "") {
					// fnKendoMessage({message : "가상계좌 선택시 환불계좌 정보를 모두 등록해주세요."});
					// return false;
				} else {
					if(!isValidBankAccount) {
						fnKendoMessage({message:"환불 계좌 정보 유효성 확인 후 진행해 주세요."});
						return false;
					}
				}
				let pgBankCd = fnNvl($('#pgBankCd').data('kendoDropDownList').value()); //BANK_CODE.KB
				if(pgBankCd == "") {
					fnKendoMessage({message : "가상계좌 입금 은행을 선택해 주세요."});
					return false;
				}
				let receiptType = $('[name="receiptType"]:checked').val();	// 현금영수증 선택
				if(receiptType == "CASH_RECEIPT.DEDUCTION" || receiptType == "CASH_RECEIPT.PROOF") {
					let regNumber = fnNvl($("#regNumber").val()); //사업자번호 or 휴대폰번호
					if(regNumber == "") {
						fnKendoMessage({message : "현금영수증 발급정보를 입력해주세요."});
						return false;
					}
				}
			}
			saveParam.freeShippingPriceYn = fnNvl($("input[name=freeShippingPriceYn]:checked").val()) == "" ? "N" : $("input[name=freeShippingPriceYn]:checked").val();

			if (paramData.savePaymentType == "PAY_TP.CARD") {
				fnKendoPopup({
					id     : 'nonCardBankBookCopyPopup',
					title  : '신용카드 비인증 결제',
					src    : '#/nonCardBankBookCopyPopup',
					param  : saveParam,
					width  : '500px',
					height : '550px',
					scrollable : "no",
					success: function(stMenuId, data) {

						if (fnIsEmpty(data.result)) return false;

						if (data.result == 'SUCCESS') {
							fnKendoMessage({message : "주문번호 : " + param.odid + " 주문이 생성되었습니다."});
						} else {
							fnKendoMessage({message : data.message});
						}
					}
				});
			} else {
				let url	= "/admin/order/copy/saveOrderCopyDetailInfo";

				fnKendoMessage({
					type    : "confirm"
					, message : "주문복사 하시겠습니까?"
					, ok      : function(e){
							        fnAjax({
							            url     : url,
							            params  : saveParam,
							            contentType: "application/json",
							            async   : true,
							            success : function(resultData) {
							            	fnBizCallback('save', resultData);
							            },
							            fail : function(resultData, resultCode){
							            	fnKendoMessage({
							                    message : resultCode.message,
							                    ok : function(e) {
							                    	fnBizCallback("fail", resultData);
							                    }
							                });
										},
							            isAction : 'insert'
							        })
					}
					, cancel  : function(e){
					}
				});
			}

		}
		//$('[name="rowCheckbox"]').prop("required", false);
	}

	// 영역 보이기 / 숨기기
	// btnDom - 버튼 dom 객체, startNo - table 에서 숨기기 시작하는 tr 번호 (default = 2 로 두번째 tr 부터 숨기기 시작한다), excpetionArea - 적용 예외 영역명
	function fnToggleArea(btnDom, startNo, excpetionArea) {

		var $btn = $(btnDom);
		var $table = $btn.closest("table");

		if ($btn.attr('data-flag') == "SHOW") {  // 테이블 tr 노출 상태에서 클릭시
			$btn.attr('data-flag',"HIDE"); // data-flag rkqt
			$btn.attr('class', 'k-icon k-i-arrow-chevron-down'); // "∨" 모양의 kendo icon 으로 전환

			if (excpetionArea) {
				$table.find("tbody").find("tr").not("[" + excpetionArea + "]").each(function(i) {
					if ((i + 1) >= startNo)
						$(this).hide();
				});
			} else {
				$table.find("tbody").find("tr:nth-child(n+" + startNo + ")").hide();
			}
		} else {   // 테이블 tr 숨김 상태에서 클릭시
			$btn.attr('data-flag',"SHOW");
			$btn.attr('class', 'k-icon k-i-arrow-chevron-up'); // "∧" 모양의 kendo icon 으로 전환

			if (excpetionArea) {
				$table.find("tbody").find("tr").not("[" + excpetionArea + "]").each(function(i) {
					if ((i + 1) >= startNo) $(this).show();
				});
			} else {
				$table.find("tbody").find("tr:nth-child(n+" + startNo + ")").show();
			}
		}
	}

	// 회원정보상세
	function fnUserDetail() {
		let urUserId = $('#urUserId').text();
 		fnKendoPopup({
            id		: 'buyerPopup',
            title	: fnGetLangData({ nullMsg: '회원상세' }),
            param	: { "urUserId": urUserId },
            src		: '#/buyerPopup',
            width	: '1200px',
            height	: '640px',
            success	: function(id, data) {}
        });
	}

	// 즉시 할인 내역
	function fnImmediateDiscount() {
		let param = {};
		param.odOrderId = s_odOrderId;
		orderPopupUtil.open("discountHistPopup", param);
	}
	//--------------------------------- Button End---------------------------------------------


	//------------------------------- Grid Start ----------------------------------------------
	// 그리드 초기화
	function fnInitGrid() {
		// 주문자정보 영역
		initBuyerForm();
		// 상품정보 그리드
		initOrderGoodsGrid();
		// 배송정보 그리드
		initOrderShippingGrid();
		// 결제정보 영역
		initPayForm();
	}

	// 주문자 정보 영역 초기화
	function initBuyerForm() {
		$('#buyerDiv td div').each(function (index, item) {
			item.id != 'userDiv' ? $(item).text('') : $(item).find("div").text('');
		});
	}

	// 상품정보 그리드 초기화
	function initOrderGoodsGrid() {
		// 상품정보 그리드 기본옵션
		orderGoodsGridOpt = {
			navigatable	: true,
			scrollable	: true,
			editable	: true,
			columns 	: orderMgmGridUtil.orderCopyGoodsList()
		};
		orderGoodsGrid = $('#orderGoodsGrid').initializeKendoGrid(orderGoodsGridOpt).cKendoGrid();


		orderGoodsGrid.bind("dataBound", function(e) {

			$( '#orderGoodsGrid' ).find("tr").css("height", "30px");

			fnMergeGridRows('orderGoodsGrid', orderGoodsMergeFieldArr, orderGoodsMergeGroupFieldArr);
		});
	}

	// 배송정보 그리드 초기화
	function initOrderShippingGrid() {
		// 배송정보 그리드 기본옵션
		shippingGridOpt = {
			navigatable	: true,
			scrollable 	: true,
			columns 	: orderMgmGridUtil.orderCopyshippingList()
		};
		shippingGrid = $('#shippingGrid').initializeKendoGrid( shippingGridOpt ).cKendoGrid();
	}

	// 결제 정보 영역 초기화
	function initPayForm() {
		 $('#payDiv td div span').text('');
		$('#virtualBankInfoDiv').hide();
		$("input[name='freeShippingPriceYn']").prop("checked", false);
	}

	// 컨텐츠 초기화
	function fnContentsClear() {
		s_odOrderId = null;	// 주문PK 전역변수 초기화

		initBuyerForm();
		$('#orderGoodsGrid').gridClear(true)
		$('#shippingGrid').gridClear(true)
		initPayForm();
	}

	// 화면 데이터 셋팅
	function fnSetDataView(paramData) {

		fnSetSavePaymentType(paramData);	// 결제정보 DropDown 셋팅
		s_odOrderId = paramData.odOrderId;	// 주문PK
		fnSetBuyerDataView(paramData);		// 주문자 정보
		fnSetGoodsDataView(paramData);		// 상품 정보

		$("#checkBoxAll").prop('checked', true);
		$('input:checkbox[name="rowCheckbox"]').each(function() {
		      this.checked = true; //checked 처리
		 });

		// 일일상품 스케쥴버튼 비노출
		$("#orderScheduleChangeBtn").css('display','none');

		fnSetShippingDataView(paramData);	// 배송 정보
		fnSetPayDataView(paramData);		// 결제 정보

	}

	// 결제정보 DropDown 셋팅
	function fnSetSavePaymentType(paramData) {
		// 외부몰 주문
		if (!fnIsEmpty(paramData.orderBuyerDto.outmallId)) {
			orderSearchUtil.getDropDownComm(payTpCdArr3, "savePaymentType");
		} else {
			// 통합몰 비회원 주문
			if (paramData.buyerTypeCd == 'BUYER_TYPE.GUEST') {
				orderSearchUtil.getDropDownComm(payTpCdArr2, "savePaymentType");
			// 통합몰 회원 주문
			} else {
				orderSearchUtil.getDropDownComm(payTpCdArr1, "savePaymentType");
			}
		}
		// 결제방식 change Event (savePaymentType 라디오박스가 동적으로 생성되므로 생성된 이후에 change 이벤트 재생성)
		$('input[name="savePaymentType"]').on('change', function(e) {
			console.log(">>> savePaymentType : " + this.value);
			if(this.value == 'PAY_TP.VIRTUAL_BANK') {
				$('#virtualBankInfoDiv').show();
			} else {
				$('#virtualBankInfoDiv').hide();
			}
		});
	}

	// 주문자 정보 데이터 셋팅
	function fnSetBuyerDataView(paramData) {
		if (paramData.orderBuyerDto.collectionMallId == null || paramData.orderBuyerDto.collectionMallId == '')
			$("#outCollectId").text(stringUtil.getString(paramData.orderBuyerDto.outmallId, ""));
		else
			$("#outCollectId").text(paramData.orderBuyerDto.outmallId + " (" + paramData.orderBuyerDto.collectionMallId + ")");

		if (paramData.orderBuyerDto.guestCi != '')
			$("#buyerInfo").text(paramData.orderBuyerDto.buyerNm + " / 비회원");
		else
			$("#buyerInfo").text(paramData.orderBuyerDto.buyerNm + " / " + paramData.orderBuyerDto.loginId + " / " + paramData.orderBuyerDto.urGroupNm);

		if (paramData.orderBuyerDto.sellersGroupNm == paramData.orderBuyerDto.sellersNm)
			$("#sellersInfo").text(paramData.orderBuyerDto.sellersGroupNm);
		else
			$("#sellersInfo").text(paramData.orderBuyerDto.sellersGroupNm + " (" + paramData.orderBuyerDto.sellersNm + ")");

		$("#orderPayDtInfo").text(paramData.orderBuyerDto.createDt + " / " + paramData.orderBuyerDto.approvalDt);

		var totalPayPrice = Number(paramData.orderDetailPayDetlList[0].paymentPrice) + Number(paramData.orderDetailPayDetlList[0].pointPrice);



		orderCopySellersGroupCd = paramData.orderBuyerDto.sellersGroupCd;

		orderCopyOdOrderId = paramData.orderBuyerDto.odOrderId;

		orderCopyOdid = paramData.orderBuyerDto.odid;


		var payinfo = "" + fnNumberWithCommas(totalPayPrice);
		payinfo += " = [" + paramData.orderDetailPayList[0].payType;
		payinfo += "] " + fnNumberWithCommas(paramData.orderDetailPayDetlList[0].paymentPrice);
		if (stringUtil.getInt(paramData.orderDetailPayDetlList[0].pointPrice, 0) > 0){
			payinfo += " + " + fnNumberWithCommas(paramData.orderDetailPayDetlList[0].pointPrice);
		}


		$("#orderPayInfo").text(payinfo)
				;

		$.each(Object.keys(paramData.orderBuyerDto), function (index, key) {
			if (key == 'buyerHp') {
				$('#' + key).text(fnPhoneNumberHyphen(paramData.orderBuyerDto[key]));
			} else {
				$('#' + key).text(paramData.orderBuyerDto[key]);
			}
		});
	}

	// 상품 정보 데이터 셋팅
	function fnSetGoodsDataView(paramData) {
		for (var i=0; i<paramData.orderDetailGoodsList.length; i++) {
			paramData.orderDetailGoodsList[i].rowNum = i;
			paramData.orderDetailGoodsList[i].orgOrderCnt = paramData.orderDetailGoodsList[i].orderCnt;
		}
		orderGoodsGrid.dataSource.data(paramData.orderDetailGoodsList);
	}

	// 배송 정보 데이터 셋팅
	function fnSetShippingDataView(paramData) {
		shippingGrid.dataSource.data(paramData.orderDetailShippingZoneList);
	}

	// 결제 정보 데이터 셋팅
	function fnSetPayDataView(paramData) {
		var couponPrice = 0;
		// 결제상세 정보
		if (paramData.orderDetailPayDetlList[0] != null) {
			$.each(Object.keys(paramData.orderDetailPayDetlList[0]), function (index, key) {
				if (key == "salePrice" || key == "shippingPrice" || key == "discountCouponPrice" || key == "paymentTargetPrice" || key == "pointPrice" || key == "paymentPrice")
					$('#' + key).html(fnNumberWithCommas(paramData.orderDetailPayDetlList[0][key])+"<span> 원</span>");
				else
					$('#' + key).html(paramData.orderDetailPayDetlList[0][key]);
			});
			//$("#payTargetPrice").html(fnNumberWithCommas(Number(paramData.orderDetailPayDetlList[0]["paidPrice"]) + Number(paramData.orderDetailPayDetlList[0]["shippingPrice"]))+"<span> 원</span>");	//결제대상금액
		}

		var orderPayDt = "";

		// 결제 정보
		if (paramData.orderDetailPayList[0] != null) {
			var orderDetailPayInfo = paramData.orderDetailPayList[0];
			if (orderDetailPayInfo.createDt == null) {
				if (orderDetailPayInfo.approvalDt != null) {
					orderPayDt = "<span>결제일 : </span>"+orderDetailPayInfo.approvalDt;
				}
			} else {
				if (orderDetailPayInfo.approvalDt == null) {
					orderPayDt = "<span>주문일 : </span>"+orderDetailPayInfo.createDt;
				} else {
					orderPayDt = "<span>주문일 : </span>"+orderDetailPayInfo.createDt+"<br><span>결제일 : </span>"+orderDetailPayInfo.approvalDt;
				}
			}

			$('#pgService').html(orderDetailPayInfo.pgService == null ? "-" : orderDetailPayInfo.pgService);
			$('#payType').html(orderDetailPayInfo.payType);
			$('#payWay').html(orderDetailPayInfo.payInfo);
			$('#orderStatus').html(orderDetailPayInfo.orderStatus);
			$('#orderPayDt').html(orderPayDt);
			$('#payPrice').html(fnNumberWithCommas(orderDetailPayInfo.paidPrice)+"<span> 원</span>");
		}

		$('#bankCode').data("kendoDropDownList").select(0);
		$('#accountNumber').val('');
		$('#holderName').val('');
		if (fnIsEmpty(paramData.refundBankResult)) return false;
		console.log(">>> paramData.refundBankResult : " + paramData.refundBankResult.bankCode + " / " + paramData.refundBankResult.accountNumber + " / " + paramData.refundBankResult.holderName);
		$('#bankCode').data('kendoDropDownList').value(paramData.refundBankResult.bankCode);
		$('#accountNumber').val(paramData.refundBankResult.accountNumber);
		$('#holderName').val(paramData.refundBankResult.holderName);
	}

	//결제 상세정보 셋팅
	function fnSetPaymentDetailInfo(paramData) {
		$('#salePrice').html(fnNumberWithCommas(paramData.salePrice)+"<span> 원</span>");			//총상품금액
		$('#shippingPrice').html(fnNumberWithCommas(paramData.shippingPrice)+"<span> 원</span>");	//총배송비
		//$('#directPrice').html(fnNumberWithCommas(paramData.directPrice)+"<span> 원</span>");		//즉시할인
		$('#discountCouponPrice').html(fnNumberWithCommas(paramData.discountCouponPrice)+"<span> 원</span>");		//쿠폰할인
		$("#paymentTargetPrice").html(fnNumberWithCommas(Number(paramData.paidPrice) + Number(paramData.shippingPrice))+"<span> 원</span>");	//결제대상금액
		$('#pointPrice').html(fnNumberWithCommas(paramData.pointPrice)+"<span> 원</span>");			//적립금사용
		$('#paymentPrice').html(fnNumberWithCommas(paramData.paymentPrice)+"<span> 원</span>");		//실결제금액
	}

	// 결제 상세정보 를 리셋 시킨다.
	function fnPaymentDetailReset() {
		$('#salePrice').html("<span> 0원</span>");			//총상품금액
		$('#shippingPrice').html("<span> 0원</span>");		//총배송비
		//$('#directPrice').html("<span> 0원</span>");		//즉시할인
		$('#discountCouponPrice').html("<span> 0원</span>");	//쿠폰할인
		$("#paymentTargetPrice").html("<span> 0원</span>");	//결제대상금액
		$('#pointPrice').html("<span> 0원</span>");			//적립금할인
		$('#paymentPrice').html("<span> 0원</span>");		//실결제금액
	}

	//주문수량 변경을 할 때 또는 체크박스를 선택을 할 때
	function fnOrderCntChange(paramData) {

		paramData.sellersGroupCd = orderCopySellersGroupCd;
		paramData.odOrderId = orderCopyOdOrderId;
		paramData.odid = orderCopyOdid;


		fnAjax({
            url     : "/admin/order/copy/getOrderCopyCntChangeInfo",
            params  : paramData,
            contentType: "application/json",
            async   : true,
            success : function(resultData) {
            	fnBizCallback('cntChange', resultData);
            },
            fail : function(resultData, resultCode){
            	fnKendoMessage({
                    message : resultCode.message,
                    ok : function(e) {
                    	fnBizCallback("fail", resultData);
                    }
                });
			},
            isAction : 'select'
        });
	}
	//-------------------------------  Grid End  ----------------------------------------------


	//---------------Initialize Option Box Start ----------------------------------------------
	function fnInitOptionBox() {
		orderSearchUtil.getDropDownComm(orderSearchUtil.searchDataSingle(), "searchOrderType");	// 조회구분
		//fnInputValidationForAlphabetNumberSpecialCharacters('conditionValue');									// 조회구분 값

		searchCommonUtil.getDropDownCommCd("saveOrderIfType", "NAME", "CODE", " 주문 연동 방법 선택 ", "ORDER_IF_TYPE", "Y", "");	// 주문 연동 방법

		// 결제정보
		fnAjax({
            url     : "/admin/comn/getCodeList",
            params  : {"stCommonCodeMasterCode" : "PAY_TP", "useYn" : "Y", "attr1" : "OrderCopy"},
            async   : false,
            method	: 'GET',
            success : function(resultData) {
            	fnBizCallback('payTp', resultData);
            },
            fail : function(resultData, resultCode){
            	fnKendoMessage({
                    message : resultCode.message,
                    ok : function(e) {
                    	fnBizCallback("fail", resultData);
                    }
                });
			},
            isAction : 'select'
        });

		var bankCodeDropDownList = fnKendoDropDownList({
			id         : 'bankCode',
			url        : "/admin/comn/getCodeList",
			autoBind   : true,
			blank      : '선택',
			params : {"stCommonCodeMasterCode" : "BANK_CODE", "useYn" :"Y"},
		});
		bankCodeDropDownList.bind('change', function(e){
			//select 때문에 -1처리
			var index = this.select()-1;
			var param = this.dataSource.data()[index];
			$("#bankName").val(param.NAME);
		});
		fnKendoDropDownList({
			id : "receiptIssueNoType",
			data : [
				{"CODE" : "mobile"		, "NAME" : "휴대폰번호"},
				{"CODE" : "businessNo"	, "NAME" : "사업자번호"},
			],
			valueField : "CODE",
			textField : "NAME"
		});
		fnTagMkRadio({
			id    : 'receiptType',
			data  : [
				{ "CODE" : "CASH_RECEIPT.DEDUCTION"	, "NAME":"소득공제용"},
				{ "CODE" : "CASH_RECEIPT.PROOF" 	, "NAME":"지출증빙용"},
				{ "CODE" : "CASH_RECEIPT.NONE" 	, "NAME":"미발행"}
			],
			tagId : 'receiptType',
			chkVal : 'CASH_RECEIPT.DEDUCTION',
			change : function(e){
				var dropdownlist = $("#receiptIssueNoType").data("kendoDropDownList");

				if($("#receiptType").getRadioVal() == "CASH_RECEIPT.DEDUCTION"){
					dropdownlist.select(dropdownlist.ul.children().eq(0));
				}else{
					dropdownlist.select(dropdownlist.ul.children().eq(1));
				}
				if($("#receiptType").getRadioVal() == "CASH_RECEIPT.NONE") {
					dropdownlist.enable(false);
					$('#regNumber').prop('disabled', true);
				} else {
					dropdownlist.enable(true);
					$('#regNumber').prop('disabled', false);
				}
			}
		});
		fnKendoDropDownList({
			id          : 'pgBankCd'
			, tagId       : 'pgBankCd'
			, url         : '/admin/comn/payment/getPgBankCodeList'
			, params : {"psCode" : "PG_SERVICE.INICIS", "psPgCode" :"PAY_TP.VIRTUAL_BANK"}
			, isDupUrl    : 'Y'
			, textField   : 'bankNmCdNm'
			, valueField  : 'bankNmCd'
			, blank       : '은행 선택'
		});
	}
	//---------------Initialize Option Box End ------------------------------------------------


	//---------------Initialize Bind Events Start ---------------------------------------------
	//팝업초기화
	function fnInitPopups() {
		// 상품정보 > 상품코드, 상품명 클릭
		$('#ng-view').on("click", "div.popGoodsClick", function(e) {
			e.preventDefault();
			let dataItems = orderGoodsGrid.dataItem($(this).closest('tr'));
			let ilGoodsId = dataItems.ilGoodsId;

			// 묶음상품
			if(dataItems.goodsTpCd == 'GOODS_TYPE.PACKAGE'){
				if(dataItems.goodsNm.indexOf('내맘대로') > 0){
					let serverUrlObj = fnGetServerUrl();
					window.open(serverUrlObj.mallUrl+"/freeOrder" ,"_blank");
				}else{
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
						//orderMgmFunctionUtil.initShippingGrid(s_odOrderId);
						//orderMgmFunctionUtil.initOrderHistoryGrid(s_odOrderId);
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
			selectedRowData.orderCopyYn = "Y";
			selectedRowData.goodsDeliveryType = selectedRowData.dawnDeliveryYn == "Y" ? "GOODS_DELIVERY_TYPE.DAWN": "GOODS_DELIVERY_TYPE.NORMAL";
			selectedRowData.odOrderDetlId = selectedRowData.odOrderDetlId;

	    	orderPopupUtil.open("interfaceDayChangePopup", selectedRowData, function(){

				var shippingDt	= parent.POP_PARAM["parameter"].shippingDt;
				var deliveryDt	= parent.POP_PARAM["parameter"].deliveryDt;
				var orderIfDt	= parent.POP_PARAM["parameter"].orderIfDt;
				var orderIfDawnYn = parent.POP_PARAM["parameter"].orderIfDawnYn;

				var arrCheced = [];
				var checkedDataList = orderGoodsGrid.dataSource._data;
				for(var i = 0 ; i < checkedDataList.length ; i++){
						var rowChecked = $("#orderGoodsGrid").find("input[name=rowCheckbox]:eq("+i+")").is(":checked");
						arrCheced[i] = rowChecked;
				}



				if (stringUtil.getString(parent.POP_PARAM["parameter"].allChangeYn, "N") == "Y"){
					var urWarehouseId = selectedRowData.urWarehouseId;
					var orderGoodsDataList = orderGoodsGrid.dataSource._data;
					for(var i = 0 ; i < orderGoodsDataList.length ; i++){
						if (urWarehouseId == orderGoodsDataList[i].urWarehouseId){
							orderGoodsDataList[i].shippingDt	= shippingDt;
							orderGoodsDataList[i].deliveryDt	= deliveryDt;
							orderGoodsDataList[i].orderIfDt		= orderIfDt;
							orderGoodsDataList[i].orderIfDawnYn	= orderIfDawnYn;
						}
					}
				} else {
					selectedRowData.shippingDt	= shippingDt;
					selectedRowData.deliveryDt	= deliveryDt;
					selectedRowData.orderIfDt	= orderIfDt;
					selectedRowData.orderIfDawnYn	= orderIfDawnYn;
				}

				$('#orderGoodsGrid').data('kendoGrid').refresh();
				for(var i = 0 ; i < checkedDataList.length ; i++){
					$("#orderGoodsGrid").find("input[name=rowCheckbox]:eq("+i+")").prop("checked", arrCheced[i]);
				}



				//initOrderGoodsGrid();
			});
	    });

		// 상품 정보 > 송장번호(배송추적 팝업)
	    $('#ng-view').on("click","div[name=trackingNoSearch]",function(e){
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
	    		params.logisticsCd 		= selectedRowData.logisticscd;
	    		params.trackingNo 		= selectedRowData.trackingNo;
				console.log("logisticsCd : ", params.logisticsCd);
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

		// 상품정보 > 증정품 내역 팝업
		$('#ng-view').on("click", "#orderGiftListPopupBtn", function(e) {
			let param = {};
			param.odOrderId = s_odOrderId;
			orderPopupUtil.open("orderGiftListPopup", param, null);
		});


		// 배송정보 > 배송정보 > 수취 정보 변경 팝업
		$('#shippingGrid').on("click", "button[kind=shippingListEdit]", function(e) {
			e.preventDefault();
			let dataItem = shippingGrid.dataItem($(e.currentTarget).closest('tr'));
			/*let initGrid = { //success 함수
					initGridSucess : function initShippingGrid(){
						orderMgmFunctionUtil.initOrderCopyShippingGrid(s_odOrderId);
					}
			};
			orderPopupUtil.open("receiverMgmPopup", dataItem, initGrid.initGridSucess);
			*/
			let orderShippingPriceList = new Array();
			var shippingDatas = $("#shippingGrid").data("kendoGrid").dataSource.data();
			for (var i = 0; i < shippingDatas.length; i++) {
				let shippingPriceInfo = {};
				shippingPriceInfo.odShippingPriceId = shippingDatas[i].odShippingPriceId;	//주문 배송비 PK
				shippingPriceInfo.ilShippingTmplId = shippingDatas[i].ilShippingTmplId;		//주문 배송정책 PK
				shippingPriceInfo.shippingPrice = shippingDatas[i].shippingPrice;			//주문 배송비
				shippingPriceInfo.shippingDiscountPrice = shippingDatas[i].shippingDiscountPrice;	//배송비 할인금액

				orderShippingPriceList.push(shippingPriceInfo);
			}

			var trNum = $(this).closest('tr').prevAll().length;

			dataItem.orderCopyYn = "Y";

			orderPopupUtil.open("receiverMgmPopup", dataItem, function(){
				var deliveryData	= parent.POP_PARAM["parameter"].deliveryData;


				if (deliveryData != undefined && deliveryData != "undefined") {




					var shippingDatas = $("#shippingGrid").data("kendoGrid").dataSource.data();
					let orderShippingList = new Array();
					for (var i = 0; i < shippingDatas.length; i++) {
						let shippingZoneInfo = {};

						if (i == trNum){

							shippingZoneInfo.odShippingZoneId 	= $.trim(deliveryData.odShippingZoneId);	;	//주문 배송지 PK
							shippingZoneInfo.odOrderId			= s_odOrderId;					//주문 PK
							shippingZoneInfo.deliveryType		= $.trim(deliveryData.deliveryType);		// 배송타입
							shippingZoneInfo.recvNm				= $.trim(deliveryData.receiverName);			//수령인 명
							shippingZoneInfo.orgRecvNm			= $.trim(deliveryData.receiverName);			//수령인 명
							shippingZoneInfo.recvHp				= $.trim(""+deliveryData.receiverMobile);			//수령인 핸드폰
							shippingZoneInfo.orgRecvHp			= $.trim(""+deliveryData.receiverMobile);			//수령인 핸드폰
							shippingZoneInfo.recvTel			= "";		//수령인 연락처
							shippingZoneInfo.orgRecvTel			= "";		//수령인 연락처
							shippingZoneInfo.recvMail			= "";		//수령인 이메일
							shippingZoneInfo.orgRecvMail		= "";		//수령인 이메일
							shippingZoneInfo.recvZipCd			= $.trim(deliveryData.receiverZipCode);	//수령인 우편번호
							shippingZoneInfo.recvAddr1			= $.trim(deliveryData.receiverAddress1);	//수령인 주소1
							shippingZoneInfo.orgRecvAddr1		= $.trim(deliveryData.receiverAddress1);	//수령인 주소1
							shippingZoneInfo.recvAddr2			= $.trim(deliveryData.receiverAddress2);	//수령인 주소2
							shippingZoneInfo.orgRecvAddr2		= $.trim(deliveryData.receiverAddress2);	//수령인 주소2
							shippingZoneInfo.recvBldNo			= $.trim(deliveryData.buildingCode);	//건물번호
							shippingZoneInfo.deliveryMsg		= $.trim(deliveryData.shippingComment);//배송요청사항
							shippingZoneInfo.doorMsgCd			= $.trim(deliveryData.accessInformationType);	//출입정보타입
							shippingZoneInfo.doorMsgCdName		= $.trim(deliveryData.doorMsgCdName);
							shippingZoneInfo.doorMsg			= $.trim(deliveryData.accessInformationPassword);		//배송출입 현관 비밀번호
							shippingZoneInfo.urUserId			= $.trim(deliveryData.urUserId);
							shippingZoneInfo.odOrderDetlId		= $.trim(deliveryData.odOrderDetlId);

						} else {
							shippingZoneInfo.odShippingZoneId = shippingDatas[i].odShippingZoneId;	//주문 배송지 PK
							shippingZoneInfo.odOrderId		= s_odOrderId;					//주문 PK
							shippingZoneInfo.deliveryType	= shippingDatas[i].deliveryType;					//배송타입
							shippingZoneInfo.recvNm			= shippingDatas[i].orgRecvNm;			//수령인 명
							shippingZoneInfo.orgRecvNm		= shippingDatas[i].orgRecvNm;			//수령인 명
							shippingZoneInfo.recvHp			= shippingDatas[i].orgRecvHp;			//수령인 핸드폰
							shippingZoneInfo.orgRecvHp		= shippingDatas[i].orgRecvHp;			//수령인 핸드폰
							shippingZoneInfo.recvTel		= shippingDatas[i].recvTel;		//수령인 연락처
							shippingZoneInfo.orgRecvTel		= shippingDatas[i].recvTel;		//수령인 연락처
							shippingZoneInfo.recvMail		= shippingDatas[i].recvMail;		//수령인 이메일
							shippingZoneInfo.orgRecvMail	= shippingDatas[i].recvMail;		//수령인 이메일
							shippingZoneInfo.recvZipCd		= shippingDatas[i].recvZipCd;	//수령인 우편번호
							shippingZoneInfo.recvAddr1		= shippingDatas[i].orgRecvAddr1;	//수령인 주소1
							shippingZoneInfo.orgRecvAddr1	= shippingDatas[i].orgRecvAddr1;	//수령인 주소1
							shippingZoneInfo.recvAddr2		= shippingDatas[i].orgRecvAddr2;	//수령인 주소2
							shippingZoneInfo.orgRecvAddr2	= shippingDatas[i].orgRecvAddr2;	//수령인 주소2
							shippingZoneInfo.recvBldNo		= shippingDatas[i].recvBldNo;	//건물번호
							shippingZoneInfo.deliveryMsg	= shippingDatas[i].deliveryMsg;//배송요청사항
							shippingZoneInfo.doorMsgCd		= shippingDatas[i].doorMsgCd;	//출입정보타입
							shippingZoneInfo.doorMsg		= shippingDatas[i].doorMsg;		//배송출입 현관 비밀번호
							shippingZoneInfo.doorMsgCdName	= shippingDatas[i].doorMsgCdName;
						}
						shippingZoneInfo.odShippingPriceId 		= orderShippingPriceList[i].odShippingPriceId;	//주문 배송비 PK
						shippingZoneInfo.ilShippingTmplId	 	= orderShippingPriceList[i].ilShippingTmplId;		//주문 배송정책 PK
						shippingZoneInfo.shippingPrice 			= orderShippingPriceList[i].shippingPrice;			//주문 배송비
						shippingZoneInfo.shippingDiscountPrice 	= orderShippingPriceList[i].shippingDiscountPrice;	//배송비 할인금액
						orderShippingList.push(shippingZoneInfo);
					}


					$("#shippingGrid").data("kendoGrid").dataSource.data(orderShippingList);
				}
				//fnSearch();
			});
		});

		//  배송정보 > 배송정보 > 수취 정보 변경 내역 팝업
		$('#shippingGrid').on("click", "button[kind=shippingHistList]", function(e) {
			e.preventDefault();
			let dataItem = shippingGrid.dataItem($(e.currentTarget).closest('tr'));
			let param = {};
			param.odShippingZoneId = dataItem.odShippingZoneId;
			param.urUserId = dataItem.urUserId;
			param.odOrderId = s_odOrderId; // 주문번호 > 변경이력 팝업에 사용
			orderPopupUtil.open("receiverHistPopup", param, null);
		});
	}; //end fnInitPopups();

	function fnBindEvents() {

		//주문수량 변경시
		$('#ng-view').on('change', 'div[data-role=grid] input[data-role=dropdownlist]', function (e) {
			let paramData= {};
			var dataItem = orderGoodsGrid.dataItem($(this).closest('tr'));	//주문수량 변경된 데이터
			let orderCnt = $(this).val();
			var dataCnt = 0;
			var selectRowsOrder = $("#orderGoodsGrid").find("input[name=rowCheckbox]:checked").closest("tr");
			var selectedRowOrderData; // select한 row데이터
			var orderCntChangList = new Array();


			if (selectRowsOrder.length == 0) {
				$(this).val(dataItem.orgOrderCnt);
				fnKendoMessage({message: "주문 복사할 상품을 선택 해야 합니다."});
			} else {
	    		for(var i = 0 ; i < selectRowsOrder.length ; i++){
	    			selectedRowOrderData = orderGoodsGrid.dataItem($(selectRowsOrder[i]));

	    			let goodOrgData = {};

	    			if (dataItem.odOrderDetlId == selectedRowOrderData.odOrderDetlId) {
	    				dataCnt = 1;
	    				goodOrgData.orderCnt = Number(orderCnt);
	    			} else {
	    				goodOrgData.orderCnt = selectedRowOrderData.orderCnt;
	    			}
					goodOrgData.odOrderDetlId = selectedRowOrderData.odOrderDetlId;
					orderCntChangList.push(goodOrgData);
	    		}
	    		if (dataCnt == 0) {
	    			$(this).val(dataItem.orgOrderCnt);
	    			fnKendoMessage({message: "주문 복사할 상품을 선택 해야 합니다."});
	    		} else {
	    			paramData.orderCntChangList = orderCntChangList;
	    			fnOrderCntChange(paramData);
	    		}
			}
		});

		// 상품코드/상품명 클릭
		$('#ng-view').on('click', '.popGoodsClick', function(e) {
			e.preventDefault();
			let dataItems = orderGoodsGrid.dataItem($(this).closest('tr'));
			let ilGoodsId = dataItems.ilGoodsId;
			window.open("#/goodsMgm?ilGoodsId="+ilGoodsId,"_blank");
		});

		//상품전체 체크박스 클릭
		$('#ng-view').on("click","input[name=checkBoxAll]",function(e){
			//if (orderCopySellersGroupCd == checkOrderCopySellersGroupCd) {
				if ($('input:checkbox[name="checkBoxAll"]').is(":checked")) {
					$('input:checkbox[name="rowCheckbox"]').each(function () {
						this.checked = true; //checked 처리
					});
					let paramData = {};
					var selectRowsOrder = $("#orderGoodsGrid").find("input[name=rowCheckbox]:checked").closest("tr");
					var selectedRowOrderData; // select한 row데이터
					var orderCntChangList = new Array();

					for (var i = 0; i < selectRowsOrder.length; i++) {
						selectedRowOrderData = orderGoodsGrid.dataItem($(selectRowsOrder[i]));

						let goodOrgData = {};
						goodOrgData.orderCnt = selectedRowOrderData.orderCnt;
						goodOrgData.odOrderDetlId = selectedRowOrderData.odOrderDetlId;
						orderCntChangList.push(goodOrgData);
					}
					paramData.orderCntChangList = orderCntChangList;
					fnOrderCntChange(paramData);
				} else {
					$('input:checkbox[name="rowCheckbox"]').each(function () {
						this.checked = false; //checked 처리
					});

					fnPaymentDetailReset();
				}
			/*} else {
				$('input:checkbox[name="checkBoxAll"]').prop("checked", true);
				fnKendoMessage({message : orderCopySellersGroupMsg});
			}*/
		});

        // 상품 체크박스 클릭에 의한 주문상태변경버튼 활성화
	    $('#ng-view').on("click","input[name=rowCheckbox]",function(e){
			//if (orderCopySellersGroupCd == checkOrderCopySellersGroupCd) {
				let paramData= {};
				var selectRowsOrder = $("#orderGoodsGrid").find("input[name=rowCheckbox]:checked").closest("tr");
				var selectedRowOrderData; // select한 row데이터
				var orderCntChangList = new Array();

				if (selectRowsOrder.length == 0) {
					fnPaymentDetailReset();
				} else {
					for(var i = 0 ; i < selectRowsOrder.length ; i++){
						selectedRowOrderData = orderGoodsGrid.dataItem($(selectRowsOrder[i]));

						let goodOrgData = {};
						goodOrgData.orderCnt = selectedRowOrderData.orderCnt;
						goodOrgData.odOrderDetlId = selectedRowOrderData.odOrderDetlId;
						orderCntChangList.push(goodOrgData);
					}
					paramData.orderCntChangList = orderCntChangList;
					fnOrderCntChange(paramData);
				}

				if ($('input:checkbox[name="rowCheckbox"]').length == selectRowsOrder.length) {
					$("#checkBoxAll").prop('checked', true);
				} else {
					$("#checkBoxAll").prop('checked', false);
				}
			/*} else {
				$(this).prop("checked", true);
				fnKendoMessage({message : orderCopySellersGroupMsg});
			}*/
	    });
	}

	//---------------Initialize Bind Events End -----------------------------------------------


	//------------------------------- Validation Start ----------------------------------------
	// 저장 Validation
	function fnSaveValid() {
		// 주문정보 존재 여부 체크
		if (orderGoodsGrid.dataSource.data().length < 1) {
			fnKendoMessage({message : "복사할 주문정보가 존재하지 않습니다."});
			return false;
		}
		// 주문정보 유효성 체크(체크가능 상품)
		if ($('[name="rowCheckbox"]').length < 1) {
			fnKendoMessage({message : "복사 가능한 상품정보가 존재하지 않습니다."});
			return false;
		}

        let nowDateTime = fnGetToday();
        let returnNow = false;

		$.each(orderGoodsGrid.dataSource.data(), function(index, item) {
		   if(item.shippingDt == nowDateTime && $('input[name="savePaymentType"]').val() == "PAY_TP.VIRTUAL_BANK") {
               fnKendoMessage({message : "당일출고 주문은 가상계좌 결제가 불가능합니다. <br> 출고예정일 혹은 결제방식을 변경해 주세요."});
               returnNow = true;
		       return false;
           }
        });

		if(returnNow) {
		    return false;
        }

		// 체크한 상품 존재여부 체크
		$('[name="rowCheckbox"]').prop("required", true);
		var paramData 	= $('#saveForm').formSerialize(true);
		return paramData.rtnValid;
	}
	//------------------------------- Validation End ------------------------------------------


	//-------------------------------  Common Function start ----------------------------------
	function fnBizCallback(id, data) {
    	switch (id) {
		case 'search' :	// 조회

			let orderEmployeeDiscountCnt = stringUtil.getInt(data.orderEmployeeDiscountCnt, 0);

			if (orderEmployeeDiscountCnt > 0){
				fnKendoMessage({
					message : "임직원 지원금 사용 주문은 복사 불가능합니다."
				});
				return;
			}

			if(data.urStoreId != ''){
				fnKendoMessage({message : "매장배송/픽업 주문은 복사 불가능합니다."});
				return;
			}

			if (data.orderDetailGoodsList == null || data.orderDetailGoodsList.length < 1) {
				fnKendoMessage({
	        		message : "주문정보가 존재하지 않습니다."
	        	});
				$('#orderGoodsGrid').setNoRecords(noRecodeMessage);
				$('#shippingGrid').setNoRecords(noRecodeMessage);
				return false;
			}

			// 화면 데이터 셋팅
			fnSetDataView(data);

			// 결제 관련 정보 다시 조회 필요가 없으면 주석 처리 해주세요
			if ($('input:checkbox[name="checkBoxAll"]').is(":checked")) {
				$('input:checkbox[name="rowCheckbox"]').each(function() {
					this.checked = true; //checked 처리
				});
				let paramData= {};
				var selectRowsOrder = $("#orderGoodsGrid").find("input[name=rowCheckbox]:checked").closest("tr");
				var selectedRowOrderData; // select한 row데이터
				var orderCntChangList = new Array();

				for(var i = 0 ; i < selectRowsOrder.length ; i++){
	    			selectedRowOrderData = orderGoodsGrid.dataItem($(selectRowsOrder[i]));

	    			let goodOrgData = {};
	    			goodOrgData.orderCnt = selectedRowOrderData.orderCnt;
					goodOrgData.odOrderDetlId = selectedRowOrderData.odOrderDetlId;
					orderCntChangList.push(goodOrgData);
	    		}
				paramData.orderCntChangList = orderCntChangList;
				fnOrderCntChange(paramData);
			}
            break;

		case 'payTp' :	// 결제정보 공통코드 조회
			payTpCdArr = data.rows;
			payTpCdArr.unshift( { 'CODE' : '' , 'NAME' : '결제 정보 선택' } );

			$.each(payTpCdArr, function (index, payTpCd) {
				if (payTpCd.CODE == '' || payTpCd.CODE == 'PAY_TP.CARD' || payTpCd.CODE == 'PAY_TP.VIRTUAL_BANK') {
					payTpCdArr1.push(payTpCd);	// 통합몰 회원 주문용
					payTpCdArr2.push(payTpCd);	// 통합몰 비회원 주문용
					payTpCdArr3.push(payTpCd);	// 외부몰 주문용
				}
				if (payTpCd.CODE == 'PAY_TP.DIRECT') payTpCdArr1.push(payTpCd);		// 통합몰 회원 주문용
				if (payTpCd.CODE == 'PAY_TP.COLLECTION') payTpCdArr3.push(payTpCd);	// 외부몰 주문용
			});

			orderSearchUtil.getDropDownComm(payTpCdArr, "savePaymentType");
			break;
		case 'cntChange' :	//수량변경
			//결제 상세정보 변경 셋팅
			fnSetPaymentDetailInfo(data);
			break;
		case 'save' :	// 저장
			if (data.result == "SUCCESS") {
				let paramData 	= $('#saveForm').formSerialize(true);
				let param = new Object();
				param.odPaymentMasterId = data.odPaymentMasterId;
				param.orderPrice = data.paymentPrice;

				// 가상계좌
				//console.log(">>> " + paramData.savePaymentType);
				if (paramData.savePaymentType == 'PAY_TP.VIRTUAL_BANK') {
					param.odid = data.odid;
					param.odOrderId = data.odOrderId;
					param.pgBankCd = fnNvl($('#pgBankCd').data('kendoDropDownList').value());
					fnVirtualBankPay(param);
				} else {
					fnKendoMessage({ message : "[" + data.odid + "]로 주문복사가 완료 되었습니다.", ok : function(e) { fnClear(); }});
				}
			} else {
				fnKendoMessage({ message : data.errMsg });
			}
			break;

		case 'fail' :	// 오류
			break;
		}
	}

	// 그리드 Rowspan 셋팅 (gridId : 그리드 Div ID, mergeColArr : 머지할 data-field 목록, groupByColArr : Group By 할 data-field 목록)
	function fnMergeGridRows(gridId, mergeColArr, groupByColArr) {

		// 데이터 1건 이하인 경우 : rowSpan 불필요
		if ($('#' + gridId).data("kendoGrid").dataSource.data().length <= 1) return false;
		// 머지할 목록 없는 경우 체크
		if (fnIsEmpty(mergeColArr)) return false;

		let dataArr = $('#' + gridId).data("kendoGrid").dataSource.data();

		let rowArr		= new Array();	// rowSpan 셋팅할 row 목록
		let rowSpanArr	= new Array();	// rowSpan값 목록
		let rowSpan		= 1;			// 최초 rowSpan 셋팅값

		for (let i=0; i<dataArr.length; i++) {
			if (i == 0) {
				rowArr.push(i);
			} else {
				// Group By할 목록이 있는 경우
				if (!fnIsEmpty(groupByColArr)) {
					let newGroupFlag = false;
					for (let j=0; j<groupByColArr.length; j++) {
						if (dataArr[i-1][groupByColArr[j]] != dataArr[i][groupByColArr[j]]) {
							newGroupFlag = true;
							break;
						}
					}

					if (newGroupFlag) {
						rowSpanArr.push(rowSpan);
						rowArr.push(i);
						rowSpan = 1;
					} else {
						rowSpan++;
					}
					// 없는 경우 해당 Cell 목록 전체 머지
				} else {
					rowSpan++;
				}
			}

			if ((i+1) == dataArr.length) {
				rowSpanArr.push(rowSpan);
			}
		}

		$('#' + gridId + '>.k-grid-content>table').each(function (index, item) {
			$('#' + gridId + '> .k-grid-header > .k-grid-header-wrap > table').find('th').each(function (tdIndex, tdInfo) {
				try {
					let targetField = $(this).context.dataset.field;
					let removeCount = 1;

					$(item).find('tr').each(function (row, trInfo) {
						let targetTd	= $(this).find('td:nth-child(' + (tdIndex+1) + ')');
						if (mergeColArr.indexOf(targetField) != -1) {
							let targetIndex = rowArr.indexOf(row);
							targetIndex != -1 ? targetTd.prop('rowspan', rowSpanArr[targetIndex]) : targetTd.hide();
						} else {
							targetTd.prop('rowspan', 1);
						}
					});
				} catch(e) {
					console.log('fnMergeGridRows Error : ' + e);
				}
			});
		});
	}

	// 가상계좌 입금
	function fnVirtualBankPay(param) {
		let receiptType = $('[name="receiptType"]:checked').val();	// 현금영수증 선택
		if(receiptType == "CASH_RECEIPT.DEDUCTION") {	//소득공제용
			param.flgCash = "1";
		} else if(receiptType == "CASH_RECEIPT.PROOF") {	//지출증빙용
			param.flgCash = "2";
		} else {
			param.flgCash = "0";
		}
		param.cashReceiptNumber = fnNvl($("#regNumber").val()); //사업자번호 or 휴대폰번호

		param.orderCreateTp = "ORDER_COPY";	//주문복사-가상계좌

		let url		= "/admin/order/create/addBankBookOrderCreate";

		fnAjax({
			url     : url,
			params  : param,
			async   : true,
			method	: 'POST',
			success : function(resultData) {
				console.log("> fnVirtualBankPay : " + resultData);
				if(resultData.result == 'SUCCESS') {
					fnKendoMessage({
						//message : "엑셀업로드한 주문이 생성되었습니다.",
						message : "가상계좌 주문번호 : " + resultData.odid + " 주문이 생성되었습니다.",
						ok : function(e) {
							location.reload();
						}
					});
				} else {
					fnKendoMessage({
						message : "가상계좌 주문번호 : " + resultData.odid + " 주문, 가상계좌 실패 : " + resultData.message,
						ok : function(e) {
							//location.reload();
						}
					});
				}
				//$('[name="orderCreateTp"]').change();
			},
			isAction : 'select'
		});
	}

	//계좌 정보 유효성 확인
	function fnValidBankAccount(){
		var data = $('#inputFormRefundBank').formSerialize(false);
		var regExp = /^\d+-?\d+$/;
		if(!regExp.test(data.accountNumber)) {
			fnKendoMessage({message:"환불 계좌번호 형식을 확인해주세요."});
			return false;
		}
		if($("#bankCode").val() == ""){
			fnKendoMessage({message:"환불 은행명을 선택해주세요."});
			return false;
		}
		if($("#holderName").val() ==""){
			fnKendoMessage({message:"환불 예금주를 입력해주세요."});
			return false;
		}
		fnAjax({
			url     : '/admin/ur/userRefund/isValidationBankAccountNumber',
			params  : data,
			success :
				function( data ){
					if(data == true){
						$("#validBankAccountDiv").text("계좌인증 성공");
						isValidBankAccount = true;
					}else{
						$("#validBankAccountDiv").text("계좌인증 실패");
						isValidBankAccount = false;
					}
				},
			isAction : 'select'
		});
	};
	//-------------------------------  Common Function end ------------------------------------


	//------------------------------- Html 버튼 바인딩  Start --------------------------------------
	/** Common Search */
	$scope.fnSearch 			= function() { fnSearch(); };
	/** Common Clear */
	$scope.fnClear 				= function() { fnClear(); };
	/** Common Next */
	$scope.fnNext				= function() { fnNext(); }
	/** Common Save */
	$scope.fnSave 				= function() { fnSave(); };
	/** 영역 보이기 / 숨기기 */
	$scope.fnToggleArea 		= function(btnDom, startNo, excpetionArea) { fnToggleArea(btnDom, startNo, excpetionArea); };
	/** 회원정보 상세 */
	$scope.fnUserDetail 		= function() { fnUserDetail(); };
	/** 즉시 할인 내역 */
	$scope.fnImmediateDiscount 	= function() { fnImmediateDiscount(); };
	/** 계좌정보 유효성 확인*/
	$scope.fnValidBankAccount = function( ) {	fnValidBankAccount();};
	//------------------------------- Html 버튼 바인딩  End ----------------------------------------
}); // document ready - END
