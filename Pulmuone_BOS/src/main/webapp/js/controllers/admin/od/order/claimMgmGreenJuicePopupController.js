/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 클레임 신청 (녹즙)
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.01.05		김승우          최초생성
 * @ 2021.04.27	 	최윤지			수정
 * **/
"use strict";

var PAGE_SIZE = 20;
var viewModel, gridDs, gridOpt, grid;
var claimMgmPopupOrderGoodsGridDs, claimMgmPopupOrderGoodsGridOpt, claimMgmPopupOrderGoodsGrid;
var pageParam = fnGetPageParam();
var paramData = parent.POP_PARAM['parameter'];
var claimReasonData;
var groupIdx = 0;
var LClaimCtgryDropDownList = [];
var MClaimCtgryDropDownList = [];
var SClaimCtgryDropDownList = [];
var sClaimCtgryDropDownListData = [];
var isValidBankAccount = true;
var recommendedPriceArr = new Array();
var partCancelYn = "Y";

$(document).ready(function() {
	importScript("/js/service/od/claim/claimMgmGreenJuiceFunction.js");
	importScript("/js/service/od/claim/claimMgmPopups.js");
	importScript("/js/controllers/admin/ps/claim/searchCtgryComm.js");
	importScript("/js/service/od/claim/claimMgmGreenJuiceGridColumns.js");
	importScript("/js/service/od/order/orderCommSearch.js");

	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({
			PG_ID  : "claimMgmGreenJuicePopup",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){
		setTimeout(function() {
			fnDefaultSet();
			fnInitButton();

			fnInitGrid();
			initOrderGoodList();
			fnInitMaskTextBox();
			fnInitOptionBox();
			bindEvents(); //(이미지 - 추후삭제예정)
		}, 200);
	};

	//--------------------------------- Button Start---------------------------------
	// 버튼 초기화
	function fnInitButton(){
		$("#fnSave, #fnDelCoverageGoods, #fnValidBankAccount, #fnNext").kendoButton();
	};

	function valueCheck(key, nullMsg, ID){
		fnKendoMessage({ message : fnGetLangData({ key : key, nullMsg : nullMsg}), ok : function focusValue(){
				$('#'+ID).focus();
			}});
		return false;
	};
	function fnInitMaskTextBox() {
		$('#refundPriceCS').forbizMaskTextBox({fn:'money'});
		$('#shippingPrice').forbizMaskTextBox({fn:'money'});
	}
	//--------------------------------- Button End-----------------------------------

	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid(){

		let form = {};
		form.odClaimId = paramData.odClaimId; //주문pk
		form.odOrderId = paramData.odOrderId; //주문클레임pk
		form.orderStatusCd = paramData.orderStatusCd; //주문클레임pk
		form.goodSearchList = setGridGoodsDataInit(paramData.goodSearchList); //주문상세pk
		form.putOrderStatusCd = paramData.putOrderStatusCd; // 변경 클레임 주문 상태
		form.claimStatusCd = paramData.putOrderStatusCd; // 클레임상태코드
		form.claimType = (fnNvl($("input[name=claimType]:checked").val()) == "" ? "C" : $("input[name=claimType]:checked").val());
		form.odOrderDetlDailySchSeq = paramData.odOrderDetlDailySchSeq;
		form.undeliveredClaimYn = paramData.undeliveredClaimYn == "Y" ? "Y" : "N";

		let putOrderStatusCd = paramData.putOrderStatusCd; // 변경 클레임 주문 상태
		// 변경 클레임 상태가 재배송일 경우 클레임 구분은 반품
		if(putOrderStatusCd == "EC") {
			form.claimType = "R";
		}

		fnAjax({
			url     : "/admin/order/claim/getOrderGreenJuiceClaimView",
			params  : form,
			contentType: "application/json",
			success : function( data ){

				// 카드부분취소가능여부
				partCancelYn = data.paymentInfo.partCancelYn;

				//클레임 주문상태에 따라 show(), hide()
				claimMgmGreenJuiceViewUtil.claimShow(putOrderStatusCd);

				//결제정보 폼
				initPaymentInfo(data.paymentInfo, data.accountInfo);

				//환불정보 폼
				initRefundInfo(data.priceInfo, data.goodsCouponList, data.cartCouponList, data.orderGoodList);

				//배송정보
				initGreenJuiceShippingList();

				//상품정보 그리드
				claimMgmPopupOrderGoodsGrid.dataSource.data(data.orderGoodList);

				//클레임정보
				initClaimInfo(data.customerReasonInfo, data.orderGoodList);

				// 재배송일 경우 배송정보도 조회
				if(putOrderStatusCd == "EC") {
					setTimeout(function() {
						refreshShippingList(form);
					}, 500);
				}
			},
			isAction : "select"
		});
	};

	//Default Setting
	function fnDefaultSet() {
		//Default Set
		// $('#cancelRejectDiv').hide(); //취소거부 구분 div hide (취소요청 -> 취소완료 / 취소거부)
		// $('#approveSelectDiv').hide(); //승인요청처리 div hide (CS환불 > 환불금액 10000원 이상)
	}

	//클레임 상세 > 클레임 정보
	function initClaimInfo(customerReasonInfo, orderGoodList) {
		claimMgmGreenJuiceFunctionUtil.initClaimInfo(customerReasonInfo, orderGoodList, paramData);

		//bos클레임사유
		for(let i=0; i<orderGoodList.length; i++){
			fnDropDownListInit(i);
		}

		for(let i=0; i<orderGoodList.length; i++){

			setTimeout(function(){
				MClaimCtgryDropDownList[i].enable(false);
				SClaimCtgryDropDownList[i].enable(false);

				LClaimCtgryDropDownList[i].bind('change', function(e) {
					if(0 === this.selectedIndex) {
						MClaimCtgryDropDownList[i].enable(false);
						SClaimCtgryDropDownList[i].enable(false);
					}
					else {
						MClaimCtgryDropDownList[i].enable(true);
					}

					$("#targetType_"+i).val("");
				});

				MClaimCtgryDropDownList[i].bind('change', function(e) {
					if(0 === this.selectedIndex) SClaimCtgryDropDownList[i].enable(false);
					else SClaimCtgryDropDownList[i].enable(true);

					$("#targetType_"+i).val("");
				});

				SClaimCtgryDropDownList[i].unbind("change").bind('change', function(e){
					var psClaimCtgryId = this.value();
					var claimName = this.text();
					var iid = $(this)[0].element[0].id.replace("sclaimCtgryId_", ""); // id ="abcd_"+i 값
					var sClaimData = sClaimCtgryDropDownListData[i];

					if ($("#eachGoodsReasonSelect").is(":checked") == false){
						iid = 0;
					}
					if (sClaimData != undefined) {
						for (var j = 0; j < sClaimData.length; j++) {
							if (sClaimData[j].psClaimCtgryId == psClaimCtgryId) {
								let claimNamedata = $('#inputForm').formSerialize(true);
								$('.bosClaimResultView').show();

								$("#psClaimBosId_" + iid).text(sClaimData[j].psClaimBosId); //psClaimBosId
								$("#psClaimBosSupplyId_" + iid).text(sClaimData[j].psClaimBosSupplyId); //psClaimBosSupplyId
								$("#targetType_" + iid).text(sClaimData[j].targetTypeName);
//    	            	 $("#odOrderDetlId_"+ii).text(sClaimData[j].odOrderDetlId);
								if (claimNamedata.withDraw == 'N') {
									if ($.trim(sClaimData[j].nclaimName) != "") {
										$("#supplierName_" + iid).text(sClaimData[j].supplierName + ' : ' + sClaimData[j].nclaimName);
									}
								} else {
									if ($.trim(sClaimData[j].yclaimName) != "") {
										$("#supplierName_" + iid).text(sClaimData[j].supplierName + ' : ' + sClaimData[j].yclaimName);
									}
								}
							}
						}
					}
				});
				let bosClaimLargeId		= orderGoodList[i].bosClaimLargeId;
				let bosClaimMiddleId	= orderGoodList[i].bosClaimMiddleId;
				let bosClaimSmallId		= orderGoodList[i].bosClaimSmallId;

				var lCtgryList = $(LClaimCtgryDropDownList[i].element).data("kendoDropDownList");
				var mCtgryList = $(MClaimCtgryDropDownList[i].element).data("kendoDropDownList");
				var sCtgryList = $(SClaimCtgryDropDownList[i].element).data("kendoDropDownList");

				lCtgryList.value(bosClaimLargeId);
				lCtgryList.trigger("change");

				mCtgryList.value(bosClaimMiddleId);
				mCtgryList.trigger("change");

				sCtgryList.value(bosClaimSmallId);

				setTimeout(function () {
					sCtgryList.trigger("change");
				}, 700)


			}, 500)
		}


	};

	//클레임 상세 > 상품정보
	function initOrderGoodList(){
		claimMgmPopupOrderGoodsGridOpt = {
			//dataSource: {data: [{…}, {…}]}//{data: orderList} ,3
			navigatable : true,
			scrollable : true,
			editable : false,
			columns : orderMgmGreenJuiceGridUtil.claimGreenjuiceMgmPopupOrderGoodsList()
		};

		claimMgmPopupOrderGoodsGrid = $('#claimMgmPopupOrderGoodsGrid').initializeKendoGrid( claimMgmPopupOrderGoodsGridOpt ).cKendoGrid();
		//claimMgmGreenJuiceFunctionUtil.initOrderGoodList()
	};

	//클레임 상세 > 결제정보
	function initPaymentInfo(paymentInfo, accountInfo){
		claimMgmGreenJuiceFunctionUtil.initPaymentInfo(paymentInfo, accountInfo);
	};

	//클레임 상세 > 배송정보
	function initGreenJuiceShippingList(){
		claimMgmGreenJuiceFunctionUtil.initGreenJuiceShippingList();
	};

	//클레임 상세 > 재배송정보 (재배송만)
	function initReShippingList(){
		claimMgmGreenJuiceFunctionUtil.initReShippingList();
	};

	//클레임 상세 > 환불정보 (쿠폰, 장바구니 포함)
	function initRefundInfo(priceInfo, goodsCouponList, cartCouponList, orderGoodList){
		claimMgmGreenJuiceFunctionUtil.initRefundInfo(priceInfo, goodsCouponList, cartCouponList, orderGoodList, paramData);
	};

	//-------------------------------  Grid End  -------------------------------
	//-------------------------------- fn Start --------------------------------------
	function validClaimReason() {
		let validFlag = true;
		// BOS 클레임 사유 > 상품별 사유 버튼 클릭 했을 경우
		let liArea = $("li.bosClaimReasonList__listItem");
		if($("#eachGoodsReasonSelect").is(":checked")) {
			liArea.each(function() {
				if(	$(this).find("input[name=lclaimCtgryId]").val() == "" ||
					$(this).find("input[name=mclaimCtgryId]").val() == "" ||
					$(this).find("input[name=sclaimCtgryId]").val() == "") {
					fnKendoMessage({message:$(this).find("span.claimMgm__goodsNm").text() + "<br/>BOS 클레임 사유를 선택 해주세요."});
					validFlag = false;
					return false;
				}
			});
		}
		// BOS 클레임 사유 > 상품별 사유 버튼 클릭 하지 않았을 경우
		else {
			if(	liArea.eq(0).find("input[name=lclaimCtgryId]").val() == "" ||
				liArea.eq(0).find("input[name=mclaimCtgryId]").val() == "" ||
				liArea.eq(0).find("input[name=sclaimCtgryId]").val() == "") {
				fnKendoMessage({message:"BOS 클레임 사유를 선택 해주세요."});
				validFlag = false;
			}
		}

		return validFlag;
	}

	// 상품 유효성 체크
	function validGoodsInfo() {
		// 선택 가능 상품이 없을 경우
		if($("#tbodyShippingInfoArea input[type=checkbox]").length < 1) {
			fnKendoMessage({message:"반품가능 상품이 존재하지 않습니다."});
			return false;
		}
		// 상품을 선택하지 않았을 경우
		if($("#tbodyShippingInfoArea input[type=checkbox]:checked").length < 1) {
			fnKendoMessage({message:"배송정보를 선택해주세요."});
			return false;
		}
		return true;
	}

	// 도착예정일 유효성 체크
	function validArrivalDtInfo() {
		var validFlag = true;
		$("#tbodyReShippingArea input[type=text]").each(function() {
			var defaultDate = $(this).data('defaultdate');
			if($(this).val() == defaultDate) {
				fnKendoMessage({message:"도착예정일을 변경 해주세요."});
				validFlag = false;
				return false;
			}
		});
		return validFlag;
	}

	//변경(저장)
	function fnSave(odAddPaymentReqInfoId, payTp) {

		var data = $('#inputForm').formSerialize(true);
		if( data.rtnValid ){

			// 미출정보 PK
			data.ifUnreleasedInfoId = paramData.ifUnreleasedInfoId;
			data.undeliveredClaimYn = paramData.undeliveredClaimYn == "Y" ? "Y" : "N";

			var claimGoodsDatas = $("#claimMgmPopupOrderGoodsGrid").data("kendoGrid").dataSource.data();
			if(fnNvl(claimGoodsDatas) == '' || claimGoodsDatas.length < 1) {
				fnKendoMessage({message:"상품이 존재하지 않습니다."});
				return false;
			}

			if(fnNvl($("#bosClaimReasonListDiv").css("display")) != "" && $("#bosClaimReasonListDiv").css("display") != "none") {
				let claimReasonValid = validClaimReason(data);
				if(!claimReasonValid) {
					return false;
				}
			}

			let putOrderStatusCd = paramData.putOrderStatusCd;
			let claimType = (fnNvl($("input[name=claimType]:checked").val()) == "" ? "C" : $("input[name=claimType]:checked").val());
			// 재배송이 아닐 경우
			if(putOrderStatusCd != "EC") {
				// 클레임 구분이 반품일 경우
				if (claimType == "R") {
					let goodsValid = validGoodsInfo();
					if (!goodsValid) {
						return false;
					}
					// 반품일 경우 스케쥴 정보 Set
					let schFormData = setReturnsGoodsParam();
					// 스케쥴 목록 얻기
					data.goodSchList = schFormData.goodSchList;
					// 상품 상세 별 클레임 수량 정보
					data.goodDetlCntList = schFormData.goodSearchList;
				}

				//--> 상품정보 [OD_CLAIM_DETL]
				data.goodsInfoList = claimInfoGreenJuiceRegisterUtil.goodsInfoList(data, paramData);
			}
			// 재배송일 경우
			else {
				let arrivalDtValid = validArrivalDtInfo();
				if(!arrivalDtValid) {
					return false;
				}
				claimType = "R";
				let schFormData = setExchangeGoodsParam();
				// 재배송 스케쥴 정보 Set
				data.goodSchList = schFormData.goodSchList;
				// 상품 정보
				data.goodsInfoList = schFormData.goodsInfoList;
			}

			let refundPrice = $(".claimMgm__refundTotalPrice").text().replace(/,/ig, '');
			var url = "/admin/order/claim/addAddPaymentAfterOrderClaimGreenJuiceRegister";

			data.putOrderStatusCd = putOrderStatusCd;
			data.orderStatusCd = paramData.orderStatusCd;
			data.claimType = claimType;

			//--> OD_CLAIM SET
			claimInfoGreenJuiceRegisterUtil.odClaimSetting(data, paramData, partCancelYn);

			//--> BOS클레임 사유 SET
			claimInfoGreenJuiceRegisterUtil.odClaimCtgrySetting(data, paramData);

			//--> 결제-환불정보
			claimInfoGreenJuiceRegisterUtil.paymentInfo(data);

			//-->상품쿠폰목록정보
			data.goodsCouponInfoList = claimInfoGreenJuiceRegisterUtil.goodsCouponInfoList(data, paramData);

			//-->장바구니쿠폰목록정보
			data.cartCouponInfoList = claimInfoGreenJuiceRegisterUtil.cartCouponInfoList(data, paramData);

			//-->클레임상태, 클레임상태구분 변경
			claimRegisterGreenJuiceFnUtil.changeClaimStatus(data, paramData);

			//-->주문회원 정보 및 택배사 반품접수 관련 필요 변수
			claimInfoGreenJuiceRegisterUtil.userInfo(data,paramData);

			//주문클레임 환불계좌관리[OD_CLAIM_ACCOUNT]
			claimInfoGreenJuiceRegisterUtil.claimAccountInfo(data);

			fnAjax({
				url     : url,
				params  : data,
				contentType: "application/json",
				success :
					function( data ){
						if(fnNvl(data) != "") {
							if(data.orderRegistrationResult != "SUCCESS") {
								fnKendoMessage({
									message:data.message
								});
							}
							else {
								fnKendoMessage({
									message:"변경되었습니다.",
									ok:function(){
										paramData.successDo = true;
										fnClose();
									}
								});
							}
						}
					},
				error :
					function (xhr, status, strError) {
						let resultData = xhr.responseJSON;
						fnKendoMessage({
							message: resultData.message
						});
					},
				isAction : 'batch'
			});
		}
	};

	//닫기
	function fnClose(){
		parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
	};

	// 추가 결제용 파라미터 만들기
	function getAddPaymentPriceParam() {
		let refundPrice = $(".claimMgm__refundTotalPrice").text().replace(/,/ig, '');
		var odOrderDetlIds = new Array();
		for(var i=0; i<paramData.goodSearchList.length; i++) {
			odOrderDetlIds.push(paramData.goodSearchList[i].odOrderDetlId);
		}
		let param = {
			'orderPrice' : Math.abs(new Number(refundPrice)), //parseInt($('#shippingPrice').val().replace(/,/g,"")), //[추후변경]배송비(추가결제금액)
			'odid' : paramData.odid,
			'odOrderDetlIds' : odOrderDetlIds
		};
		return param;
	}

	// 환불정보 > 추가결제 시 버튼선택 (비인증결제, 가상계좌발급, 직접결제)
	//--> 비인증결제
	$('#ng-view').on("click", "#fnCardPay", function(e) {
		let param = getAddPaymentPriceParam();
		claimMgmPopupUtil.open("nonCardBankBookClaimPopup", param, null);
	});

	//--> 가상계좌발급
	$('#ng-view').on("click", "#fnBankBook", function(e) {
		let param = getAddPaymentPriceParam();
		var url = "/admin/order/claim/addBankBookOrderCreate";
		fnKendoMessage({
			type    : "confirm"
			, message : "무통장입금을 하시겠습니까?"
			, ok      : function(e){
				fnAjax({
					url     : url,
					params  : param,
					contentType: "application/json",
					success : function( data ){
						var result = data.orderRegistrationResult;
						var successFalg = false;
						if (result == 'SUCCESS') {
							result = "가상계좌가 채번 되었습니다.";
							successFalg = true;
							fnSave(data.odAddPaymentReqInfoId, 'virtualAccount');
						} else {
							result = "아래와 같은 사유로 결제가 실패 되었습니다.<br/>" + data.message;
						}
						fnKendoMessage({
							message : result
							, ok : function (e) {
								if(successFalg) {
									//fnClose();
								}
							}
						});
					},
					isAction : 'insert'
				})
			}
			, cancel  : function(e){
				$('#fnBankBook').prop('checked', false);
			}
		});
	});

	// 행 삭제
	function removeRow(e) {
		var $target = $(e.target);
		var $row = $target.closest("tr");

		$("#claimMgmPopupOrderGoodsGrid").data("kendoGrid").removeRow($row);
	};

	//계좌 정보 유효성 확인
	function fnValidBankAccount(){
		var data = $('#inputFormRefundBank').formSerialize(false);
		var regExp = /^\d+-?\d+$/;
//		if(!regExp.test(data.accountNumber)) {
//			return valueCheck("", "계좌번호 형식을 확인해주세요.", '');
//		}
//		if($("#bankCode").val() == ""){
//			return valueCheck("", "은행명을 선택해주세요.", '');
//		}
//		if($("#holderName").val() ==""){
//			return valueCheck("", "예금주를 입력해주세요.", '');
//		}
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

	function setRefundData(data) { // 환불 데이터 정보 Set

		initRefundInfo(data.priceInfo, data.goodsCouponList, data.cartCouponList, data.orderGoodList);
	}
	//---------------Initialize Option Box Start ------------------------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){
		initDatePicker();
		//사유
		claimMgmGreenJuiceEventUtil.psClaimMallId(paramData, claimReasonData);
		//클레임 구분
		claimMgmGreenJuiceEventUtil.initClaimType(changeClaimType, paramData.putOrderStatusCd);
	};

	// 클레임구분 변경 시 이벤트 처리
	function changeClaimType(e) {
		const value = e.target.value;

		let form = {};
		form.odClaimId = paramData.odClaimId; //주문pk
		form.odOrderId = paramData.odOrderId; //주문클레임pk
		form.orderStatusCd = paramData.orderStatusCd; //주문클레임pk
		form.claimStatusCd = paramData.putOrderStatusCd; // 변경 클레임 주문 상태
		form.claimType = (fnNvl($("input[name=claimType]:checked").val()) == "" ? "C" : $("input[name=claimType]:checked").val());
		form.goodSearchList = setGridGoodsDataInit(paramData.goodSearchList); //주문상세pk
		form.odOrderDetlDailySchSeq = paramData.odOrderDetlDailySchSeq;

		// 취소 일때
		if( value == 'C' ) {
			$("input[name=selectAll1]").prop("checked", false);
			$("input[name=selectAll2]").prop("checked", false);
			$('.inputForm__section.shippingInfo').hide(); //배송정보 hide
			$('#refundGoodsInfoDiv').hide();

			refreshGoodsGrid(form);
		}
		// 반품 일때
		else {
			$('.inputForm__section.shippingInfo').show(); //배송정보 show
			$('#refundGoodsInfoDiv').show();

			refreshGoodsGrid(form);

			// 배송정보 조회
			refreshShippingList(form);
		}
	}

	// 배송정보 리프레시
	function refreshShippingList(form) {
		// 반품일때 해당 녹즙의 배송완료 여부가 'Y'인 주문건 가져오기
		fnAjax({
			url     : "/admin/order/claim/getOrderGreenJuiceClaimReturnsScheduleView",
			params  : form,
			contentType: "application/json",
			success : function( data ){
				claimMgmGreenJuiceFunctionUtil.initGreenJuiceShippingList(data.orderGoodList);
			},
			isAction : "select"
		});
	}

	//BOS 클레임사유 드롭다운리스트
	function fnDropDownListInit(i){
		LClaimCtgryDropDownList[i] = searchCtgryCommonUtil.LClaimCtgryDropDownListById(i);
		MClaimCtgryDropDownList[i] = searchCtgryCommonUtil.MClaimCtgryDropDownListById(i);
		SClaimCtgryDropDownList[i] = searchCtgryCommonUtil.SClaimCtgryDropDownListById(i);

	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ----------------------------------
	// 그리드 초기 상품 목록 Set 추가
	function setGridGoodsDataInit(goodsList) {
		let goodSearchList = new Array();

		for(let i=0; i<goodsList.length; i++) {
			let claimGoodsData = goodsList[i];
			let goodOrgData = {};
			goodOrgData.odOrderId = claimGoodsData.odOrderId;
			goodOrgData.odOrderDetlId = claimGoodsData.odOrderDetlId;
			goodOrgData.odClaimDetlId = claimGoodsData.odClaimDetlId;
			goodOrgData.claimCnt = 0;
			goodSearchList.push(goodOrgData);
		}
		return goodSearchList;
	}
	// 그리드 상품 목록 Set 추가
	function setGridGoodsData(data) {
		let goodSearchList = new Array();
		let claimGoodsDatas = $("#claimMgmPopupOrderGoodsGrid").data("kendoGrid").dataSource.data();

		for(let i=0; i<claimGoodsDatas.length; i++) {
			let claimGoodsData = claimGoodsDatas[i];
			let goodOrgData = {};
			goodOrgData.odOrderId = claimGoodsData.odOrderId;
			goodOrgData.odOrderDetlId = claimGoodsData.odOrderDetlId;
			goodOrgData.odClaimDetlId = claimGoodsData.odClaimDetlId;
			goodOrgData.claimCnt = 0;
			goodSearchList.push(goodOrgData);
		}
		//goodOrgData.urWarehouseId = dataItem.urWarehouseId;
		data.goodSearchList = goodSearchList;
	}

	// 상품 그리드 새로고침
	function refreshGoodsGrid(param) {
		fnAjax({
			url     : "/admin/order/claim/getOrderGreenJuiceClaimView",
			params  : param,
			contentType: "application/json",
			success : function( data ){

				//상품정보 그리드
				claimMgmPopupOrderGoodsGrid.dataSource.data(data.orderGoodList);

				//환불정보 폼
				initRefundInfo(data.priceInfo, data.goodsCouponList, data.cartCouponList, data.orderGoodList);

				if(LClaimCtgryDropDownList.length < 1) {
					//클레임정보
					initClaimInfo(data.customerReasonInfo, data.orderGoodList);
				}
			},
			isAction : "select"
		});
	}

	// 반품 상품 신청 정보 SET
	function setReturnsGoodsParam() {
		let form = {};
		form.odClaimId = paramData.odClaimId; //주문pk
		form.odOrderId = paramData.odOrderId; //주문클레임pk
		form.orderStatusCd = paramData.orderStatusCd; //주문클레임pk
		form.claimStatusCd = paramData.putOrderStatusCd; // 변경 클레임 주문 상태
		form.claimType = (fnNvl($("input[name=claimType]:checked").val()) == "" ? "C" : $("input[name=claimType]:checked").val());
		if(paramData.putOrderStatusCd == "EC") {
			form.claimType = "R";
		}

		let goodSearchList = new Array();
		let odOrderDetlObj = new Object();
		let goodSchList = new Array();
		$("#tbodyShippingInfoArea input[type=checkbox]:checked").each(function() {
			let claimCnt = new Number($("#goodsNmCnt" +  $(this).data('index')).val());
			let odOrderDetlId = $(this).data('odorderdetlid');
			let odOrderDetlSeq = $(this).data('odorderdetlseq');
			let goodSchData = {};
			goodSchData.odOrderDetlDailySchId = $(this).data('odorderdetldailyschid');
			goodSchData.odOrderDetlId = odOrderDetlId;
			goodSchData.deliveryDt = $(this).parents("td").next().find("span.deliveryDate").data('deliverydt');
			goodSchData.deliveryDtNm = $(this).parents("td").next().find("span.deliveryDate").text();
			goodSchData.goodsNm = $(this).parents("td").next().next().find("span.goodsNm").text();
			goodSchData.claimCnt = claimCnt;
			goodSchData.odOrderDetlSeq = odOrderDetlSeq;
			goodSchList.push(goodSchData);

			if(fnNvl(odOrderDetlObj[odOrderDetlId]) == '') {
				var objData = {
					claimCnt 			: claimCnt,
					urWarehouseId 		: $(this).data('urwarehouseid'),
					ilGoodsId 			: $(this).data('ilgoodsid'),
					goodsDeliveryType 	: $(this).data('goodsdeliverytype'),
					goodsCycleTp 		: $(this).data('goodscycletp'),
					weekDayCd 			: $(this).data('weekdaycd')
				};
				odOrderDetlObj[odOrderDetlId] = objData;
			}
			else {
				odOrderDetlObj[odOrderDetlId].claimCnt += claimCnt;
			}
		});
		for (var key in odOrderDetlObj) {
			let goodSearchData = {};
			goodSearchData.odOrderDetlId 		= key;
			goodSearchData.claimCnt 			= odOrderDetlObj[key].claimCnt;
			goodSearchData.urWarehouseId 		= odOrderDetlObj[key].urWarehouseId;
			goodSearchData.ilGoodsId 			= odOrderDetlObj[key].ilGoodsId;
			goodSearchData.goodsDeliveryType 	= odOrderDetlObj[key].goodsDeliveryType;
			goodSearchData.goodsCycleTp 		= odOrderDetlObj[key].goodsCycleTp;
			goodSearchData.weekDayCd 			= odOrderDetlObj[key].weekDayCd;
			goodSearchList.push(goodSearchData);
		}

		// 상품 상세 정보 리스트
		form.goodSearchList = goodSearchList;
		// 상품 스케쥴 리스트
		form.goodSchList = goodSchList;

		return form;
	}

	// 재배송 스케쥴 정보 설정
	function setExchangeGoodsParam() {
		let form = {};
		let goodSearchList = new Array();
		let odOrderDetlObj = new Object();
		let odOrderDetlSeqObj = new Object();
		let goodSchList = new Array();
		$("#tbodyReShippingArea input[type=text]").each(function() {
			var odOrderDetlId = $(this).data('odorderdetlid');
			var odOrderDetlSeq = $(this).data('odorderdetlseq');
			var claimCnt = $(this).data('claimcnt');
			var schInfo = {
				odOrderDetlDailySchId 	: $(this).data('odorderdetldailyschid'),
				odOrderDetlId 			: odOrderDetlId,
				claimCnt 				: claimCnt,
				deliveryDt 				: $(this).val()
			};
			goodSchList.push(schInfo);

			if(fnNvl(odOrderDetlObj[odOrderDetlId]) == '') {
				odOrderDetlObj[odOrderDetlId] = claimCnt;
				odOrderDetlSeqObj[odOrderDetlId] = odOrderDetlSeq;
			}
			else {
				odOrderDetlObj[odOrderDetlId] += claimCnt;
			}
		});
		for (var key in odOrderDetlObj) {
			let goodSearchData = {};
			goodSearchData.odOrderDetlId	= key;
			goodSearchData.odOrderDetlSeq	= odOrderDetlSeqObj[key];
			goodSearchData.claimCnt 		= odOrderDetlObj[key];
			goodSearchList.push(goodSearchData);
		}

		// 상품 상세 정보 리스트
		form.goodsInfoList = goodSearchList;
		// 상품 스케쥴 리스트
		form.goodSchList = goodSchList;

		return form;
	}

	// 배송 정보 영역 리프레시
	function refreshShippingArea() {
		var form = setReturnsGoodsParam();
		fnAjax({
			url     : "/admin/order/claim/getOrderGreenJuiceClaimView",
			params  : form,
			contentType: "application/json",
			success : function( data ){
				//환불정보 폼
				initRefundInfo(data.priceInfo, data.goodsCouponList, data.cartCouponList, data.orderGoodList);
			},
			isAction : "select"
		});
	}

	// 다음 버튼 클릭 시
	function fnNext() {
		// 선택 배송정보 Set
		let schFormData = setReturnsGoodsParam();
		// 스케쥴 목록 얻기
		if(fnNvl(schFormData.goodSchList) == "") {
			fnKendoMessage({message: "배송 정보를 선택해주세요."});
			return false;
		}
		// 선택한 스케쥴 목록으로 재배송 일자 정보 조회
		fnAjax({
			url     : "/admin/order/claim/getOrderGreenJuiceClaimExchangeScheduleView",
			params  : schFormData,
			contentType: "application/json",
			success : function( data ){
				if(fnNvl(data.arrivalDtList) != "") {
					claimMgmGreenJuiceFunctionUtil.initReShippingList(schFormData.goodSchList, data.arrivalDtList);
				}
				else {
					fnKendoMessage({message: "선택 가능한 도착 예정일 정보가 없습니다."});
					return false;
				}
			},
			isAction : "select"
		});
	}
	//-------------------------------  Common Function end -------------------------------

	//-------------------------------  events Start  -------------------------------
	function bindEvents() {
		// 배송정보 체크박스 변경 시 이벤트 처리
		$('#ng-view').on("change", ".shippingInfo input[type=checkbox]", function() {
			var targetNm = "";
			var targetIndex = 0;
			if($(this).attr("name").indexOf("All") < 0) {
				targetNm = $(this).attr("name");
				targetIndex = $(this).data("index");
			}
			$("#tbodyShippingInfoArea input[type=checkbox]").each(function() {
				$("#goodsNmCnt" + $(this).data('index')).data("kendoDropDownList").enable($(this).is(":checked"));
			});
			if(fnNvl(targetNm) != "") {
				var allNum = (targetIndex % 2) + 1;
				$("input[name=selectAll" + allNum + "]").prop("checked", ($("input[name=select" + allNum + "]:checked").length == $("input[name=select" + allNum + "]").length));
			}
			refreshShippingArea();
		});

		$('#ng-view').on("change", "#tbodyShippingInfoArea input[data-role=dropdownlist]", function() {
			refreshShippingArea();
		});
	}

	function bindListChangeEvent(id, handler) {
		const $kendoList = $("#" + id).data("kendoDropDownList");

		$kendoList.bind("change", function(e) {
			handler.call(this, e);
		})
	}
	//-------------------------------  events End  -------------------------------

	//------------------------------- Calendar Start -------------------------------------
	function initDatePicker() {
//		datePicker = new FbDatePicker("datepicker", {
//			disableDay: disableDay,
//			dayOff: dayOff,
//		})
	}
	//------------------------------- Calendar End -------------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};

	/** Common Clear*/
	$scope.fnClear =function(){	fnClear(); };
	$scope.fnSave = function() {	fnSave();	}; //변경(저장)
	$scope.fnNext = function() {	fnNext();	}; //다음
	$scope.fnClose = function(){ fnClose(); };
	/**CS환불 > 계좌정보 유효성 확인*/
	$scope.fnValidBankAccount = function( ) {	fnValidBankAccount();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------
	// fnInputValidationForAlphabetNumberLineBreakComma("code");
	fnCheckLength("detailReason", 255);
	fnInputValidationForNumber("shippingFee");
	fnInputValidationForNumber("phone-prefix");
	fnInputValidationForNumber("phone1");
	fnInputValidationForNumber("phone2");
	//------------------------------- Validation End -------------------------------------

}); // document ready - END
