/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 클레임 신청
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.01.05		김승우          최초생성
 * **/
"use strict";

var PAGE_SIZE = 20;
var viewModel, gridDs, gridOpt, grid;
var claimMgmPopupOrderGoodsGridDs, claimMgmPopupOrderGoodsGridOpt, claimMgmPopupOrderGoodsGrid;
var apprAdminGridDs, apprAdminGridOpt, apprAdminGrid;	// 승인 그리드
var claimMgmPopupOrderUndeliveredGridOpt, claimMgmPopupOrderUndeliveredGrid;
var pageParam = fnGetPageParam();
var paramData = parent.POP_PARAM['parameter'];
var claimReasonData;
var groupIdx = 0;
var LClaimCtgryDropDownList = [];
var MClaimCtgryDropDownList = [];
var SClaimCtgryDropDownList = [];
var sClaimCtgryDropDownListData = [];
var isValidBankAccount = false;
var recommendedPriceArr = new Array();
var publicStorageUrl = fnGetPublicStorageUrl();
var partCancelYn = "Y";
var isRefundFlag = false;


// FIXME 이미지 임시 데이터
const imageData = [
	{
		src: "https://images.unsplash.com/photo-1601162908554-d4f69c1d5f1a?ixid=MXwxMjA3fDB8MHx0b3BpYy1mZWVkfDF8NnNNVmpUTFNrZVF8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60",
		title: "1번 이미지"
	},
	{
		src: "https://images.unsplash.com/photo-1585874313804-91a5274fe74f?ixid=MXwxMjA3fDB8MHx0b3BpYy1mZWVkfDd8cVBZc0R6dkpPWWN8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60",
		title: "2번 이미지"
	},
	{
		src: "https://images.unsplash.com/photo-1609176873016-237393c2e4ff?ixid=MXwxMjA3fDB8MHx0b3BpYy1mZWVkfDEyfHFQWXNEenZKT1ljfHxlbnwwfHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60",
		title: "3번 이미지"
	},
	{
		src: "https://images.unsplash.com/photo-1609590561239-78d7198d0190?ixid=MXwxMjA3fDB8MHx0b3BpYy1mZWVkfDl8UzRNS0xBc0JCNzR8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60",
		title: "4번 이미지"
	},
]

$(document).ready(function() {

	importScript("/js/service/od/claim/claimMgmFunction.js", function (){
		importScript("/js/service/od/claim/claimMgmPopups.js", function (){
			importScript("/js/controllers/admin/ps/claim/searchCtgryComm.js", function (){
				importScript("/js/service/od/order/orderMgmGridColumns.js", function (){
					importScript("/js/service/od/order/orderCommSearch.js", function (){
						fnInitialize();
					});
				});
			});
		});
	});

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({
			PG_ID  : "claimMgmPopup",
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
			initOrderClaimApprList(); // 승인관리자 그리드 초기화
			initOrderUndeliveredList();
			fnInitMaskTextBox();
			fnInitOptionBox();
			renderImageList(imageData); //(이미지 - 추후삭제예정)
			bindEvents(); //(이미지 - 추후삭제예정)
		}, 300);
	};

	//--------------------------------- Button Start---------------------------------
	// 버튼 초기화
	function fnInitButton(){
		$("#fnSave, #fnDelCoverageGoods, #fnValidBankAccount").kendoButton();
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

		isRefundFlag = paramData.isRefundFlag;

		let form = {};
		form.odOrderId = paramData.odOrderId; //주문pk
		form.odClaimId = paramData.odClaimId; //주문클레임pk
		form.orderStatusCd = paramData.orderStatusCd; //주문클레임pk
		form.goodSearchList = paramData.goodSearchList; //주문상세pk
		form.claimStatusCd = paramData.putOrderStatusCd; // 변경 클레임 주문 상태
		form.undeliveredClaimYn = paramData.undeliveredClaimYn == "Y" ? "Y" : "N";
		setClaimStatusTp(form);

		let oriOrderStatusCd = paramData.oriOrderStatusCd;
		let orderStatusCd = paramData.orderStatusCd; //주문상태코드
		let putOrderStatusCd = paramData.putOrderStatusCd; // 변경 -> 클레임주문상태
		let btnOrderStatusCd = paramData.btnOrderStatusCd; // 버튼 상태코드

		fnAjax({
	       	url     : "/admin/order/claim/getOrderClaimView",
	       	params  : form,
	       	contentType: "application/json",
	 		success : function( data ){

					//상품정보 그리드
					claimMgmPopupOrderGoodsGrid.dataSource.data(data.orderGoodList);

					//클레임정보
					initClaimInfo(data.customerReasonInfo, data.orderGoodList);

	       			// 주문상태, 클레임상태에 따른 선미출 체크박스 표시 여부
					claimMgmViewUtil.claimPriorityUndeliveredShow(orderStatusCd, putOrderStatusCd);

	 				//클레임 주문상태에 따라 show(), hide()
	 				claimMgmViewUtil.claimShow(putOrderStatusCd);

	 				// 카드부분취소가능여부
					partCancelYn = data.paymentInfo.partCancelYn;

	 				// -> 재배송
	 				if(putOrderStatusCd == 'EC'){
	 					console.log('재배송...');
	 					//출고정보 리스트
	 					initOrderClaimReleaseList(data.deliveryInfoList);

	 					// if(fnNvl(data.orderGoodList) != '') {
						// 	makePaidPriceInfo(data.orderGoodList[0].odOrderDetlId);
						// }
						if(fnNvl(data.orderGoodList) != '') {
							let orderGoodListOrderDetlId = data.orderGoodList[0].odOrderDetlId;

							for(let i = 0;i< data.orderGoodList.length;i++){
								if(data.orderGoodList[i].odOrderDetlSeq != 0){
									orderGoodListOrderDetlId = data.orderGoodList[i].odOrderDetlId;
									break;
								}
							}

							makePaidPriceInfo(orderGoodListOrderDetlId);
						}
	 				// -> 취소요청 OR 취소완료 OR 반품완료 OR 배송완료 OR CS환불
	 				}else if(putOrderStatusCd == 'CA' || putOrderStatusCd == 'CC' || putOrderStatusCd == 'RF' ||
	 						 putOrderStatusCd == 'DC' || putOrderStatusCd == 'CS'){
	 					//결제정보 폼
		 				initPaymentInfo(data.paymentInfo, data.accountInfo, paramData);
		 				//환불정보 폼
		 				initRefundInfo(data.priceInfo, data.goodsCouponList, data.cartCouponList, data.orderGoodList);

		 			// -> 반품요청 OR 반품승인 OR 반품완료 OR 반품보류
	 				}else if(putOrderStatusCd == 'RA' || putOrderStatusCd == 'RI' || putOrderStatusCd == 'RC'){
	 					//반품상품 수거 주소정보 (보내는 분)
		 				initSendShippingInfo(data.sendShippingInfo);
		 				//반품정보 > 반품회수 주소정보 (받는 분 - 리스트)
		 				initOrderClaimShippingInfoList(data.receShippingInfoList);
		 				//결제정보 폼
		 				initPaymentInfo(data.paymentInfo, data.accountInfo, paramData);
		 				//환불정보 폼
		 				initRefundInfo(data.priceInfo, data.goodsCouponList, data.cartCouponList, data.orderGoodList);
						//반품이미지 정보 Set
						initReturnsImg(data.attcInfoList);

	 				} else {
	 					console.log('추가예정...');
	 				}
	 				if(fnNvl(data.priceInfo.addPaymentTp) != '') {
	 					var addPayment = '';
	 					if(data.priceInfo.addPaymentTp == 'ADD_PAYMENT_TP.CARD') {
							addPayment = 'cardPay';
						}
	 					else if(data.priceInfo.addPaymentTp == 'ADD_PAYMENT_TP.VIRTUAL') {
							addPayment = 'bankBook';
						}
	 					else if(data.priceInfo.addPaymentTp == 'ADD_PAYMENT_TP.DIRECT_PAY') {
							addPayment = 'directPay';
						}
	 					$("input[name=addPayment][value=" + addPayment + "]").prop("checked", true);
					}

					if(data.undeliveredList != null && data.undeliveredList != ""){
						//미출정보 그리드
						claimMgmPopupOrderUndeliveredGrid.dataSource.data(data.undeliveredList);
					}

					initReturnsYn(data.odClaimInfo);
			},
	 		isAction : "select"
 		});
	};

	//Default Setting
	function fnDefaultSet() {
		//Default Set
		$('#cancelRejectDiv').hide(); //취소거부 구분 div hide (취소요청 -> 취소완료 / 취소거부)
		$('#approveSelectDiv').hide(); //승인요청처리 div hide (CS환불 > 환불금액 10000원 이상)
		$('.approvalManagerDiv').hide();	// 승인관리자 div hide (CS환불 > 환불금액 10000원 이상)
	}

	// 클레임상태타입 Set
	function setClaimStatusTp(form) {
		if(paramData.orderStatusCd == 'RA' || paramData.orderStatusCd == 'RI' || paramData.orderStatusCd == 'RF' || paramData.orderStatusCd == 'DI' || paramData.orderStatusCd == 'DC' || paramData.orderStatusCd == 'BF') {
			form.claimStatusTp = 'CLAIM_STATUS_TP.RETURN';
		}
		//배송준비중 -> 취소요청
		else if(paramData.orderStatusCd == 'DR' || paramData.orderStatusCd == 'CA' || paramData.orderStatusCd == 'IC'){
			form.claimStatusTp = 'CLAIM_STATUS_TP.CANCEL';
		}
		// 결제완료, 배송준비중, 배송중, 배송완료, 구매확정 -> 재배송
		else if(paramData.putOrderStatusCd == 'EC'){
			form.claimStatusTp = 'CLAIM_STATUS_TP.RETURN_DELIVERY';
		}
	}

	//클레임 상세 > 클레임 정보
	function initClaimInfo(customerReasonInfo, orderGoodList) {
		claimMgmFunctionUtil.initClaimInfo(customerReasonInfo, orderGoodList, paramData);

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

					// if ($("#eachGoodsReasonSelect").is(":checked") == false){
					// 	iid = 0;
					// }
					if (sClaimData != undefined) {
						for (var j = 0; j < sClaimData.length; j++) {
							if (sClaimData[j].psClaimCtgryId == psClaimCtgryId) {
								let claimNamedata = $('#inputForm').formSerialize(true);
								$('.bosClaimResultView').show();

								$("#psClaimBosId_" + i).text(sClaimData[j].psClaimBosId); //psClaimBosId
								$("#psClaimBosSupplyId_" + i).text(sClaimData[j].psClaimBosSupplyId); //psClaimBosSupplyId
								$("#targetType_" + i).text(sClaimData[j].targetTypeName);

								if (claimNamedata.withDraw == 'N') {
									if ($.trim(sClaimData[j].nclaimName) != "") {
										$("#supplierName_" + i).text(sClaimData[j].supplierName + ' : ' + sClaimData[j].nclaimName);
									}
								} else {
									if ($.trim(sClaimData[j].yclaimName) != "") {
										$("#supplierName_" + i).text(sClaimData[j].supplierName + ' : ' + sClaimData[j].yclaimName);
									}
								}

								if (paramData.putOrderStatusCd == 'RC' || paramData.putOrderStatusCd == 'CA' || paramData.putOrderStatusCd == 'CC') {
									var targetTp = "S";
									if (sClaimData[0].targetTypeName == "구매자 귀책") {
										targetTp = "B";
									}

									let form = {};
									form.odClaimId = paramData.odClaimId; //주문pk
									form.odOrderId = paramData.odOrderId; //주문클레임pk
									form.orderStatusCd = paramData.orderStatusCd; //주문클레임pk
									form.claimStatusCd = paramData.putOrderStatusCd; // 변경 클레임 주문 상태
									//form.goodSearchList = paramData.goodSearchList; //주문상세pk
									form.undeliveredClaimYn = paramData.undeliveredClaimYn == "Y" ? "Y" : "N";
									setGridGoodsData(form);
									form.targetTp = targetTp;
									form.recvZipCd = $("#zipCode").val();
									form.returnsYn = $("input[name=withDraw]:checked").val();
									setClaimStatusTp(form);

									setTimeout(function() {
										refreshGoodsGrid(form);
									}, 500);
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

				setTimeout(function() {
					lCtgryList.value(bosClaimLargeId);
					lCtgryList.trigger("change");

					mCtgryList.value(bosClaimMiddleId);
					mCtgryList.trigger("change");

					var sCtgryList = $(SClaimCtgryDropDownList[i].element).data("kendoDropDownList");
					sCtgryList.value(bosClaimSmallId);
					setTimeout(function () {
						sCtgryList.trigger("change");
					}, 300);
				}, 400);

			}, 500)
		}


	};

	//클레임 상세 > 상품정보
	function initOrderGoodList(){
		claimMgmPopupOrderGoodsGridOpt = {
			//dataSource: {data: [{…}, {…}]}//{data: orderList} ,3
			navigatable : true,
			scrollable : true,
			editable : true,
			columns : orderMgmGridUtil.claimMgmPopupOrderGoodsList()
		};

		claimMgmPopupOrderGoodsGrid = $('#claimMgmPopupOrderGoodsGrid').initializeKendoGrid( claimMgmPopupOrderGoodsGridOpt ).cKendoGrid();
	};

	//클레임 상세 > 승인 관리자 초기화
	function initOrderClaimApprList(){
		apprAdminGridOpt = {
			dataSource  : apprAdminGridDs
			, editable    : false
			, noRecordMsg : '승인관리자를 선택해 주세요.'
			, columns : orderMgmGridUtil.orderClaimApprManageList()
		};

		apprAdminGrid = $('#apprGrid').initializeKendoGrid( apprAdminGridOpt ).cKendoGrid();
	};

	//클레임 상세 > 미출정보
	function initOrderUndeliveredList(){
		claimMgmPopupOrderUndeliveredGridOpt = {
			navigatable : true,
			scrollable : true,
			columns : orderMgmGridUtil.claimMgmPopupOrderUndeliveredList()
		};

		claimMgmPopupOrderUndeliveredGrid = $('#claimMgmPopupOrderUndeliveredGrid').initializeKendoGrid( claimMgmPopupOrderUndeliveredGridOpt ).cKendoGrid();
	};

	//클레임 상세 > 결제정보
	function initPaymentInfo(paymentInfo, accountInfo, paramData){
		claimMgmFunctionUtil.initPaymentInfo(paymentInfo, accountInfo, paramData);
	};

	//클레임 상세 > 환불정보 (쿠폰, 장바구니 포함)
	function initRefundInfo(priceInfo, goodsCouponList, cartCouponList, orderGoodList){
		claimMgmFunctionUtil.initRefundInfo(priceInfo, goodsCouponList, cartCouponList, orderGoodList, paramData);
		// 추가 배송비 정보가 존재 할 경우
		claimMgmPopupOrderGoodsGrid.dataSource.data(claimMgmFunctionUtil.setClaimAddShippingPrice(priceInfo.addPaymentList));
	};

	//클레임 상세 > 반품이미지 정보 Set
	function initReturnsImg(attcInfoList){
		claimMgmFunctionUtil.initReturnsImg(attcInfoList);
	};

	//클레임 상세 > 회수여부 정보 Set
	function initReturnsYn(odClaimInfo){
		//회수여부 태그
		claimMgmEventUtil.initWithDrawType(paramData, odClaimInfo, function(changeVal) {
			if(SClaimCtgryDropDownList != null) {
				var count = 0;
				for(var i=0; i<SClaimCtgryDropDownList.length; i++) {
					setTimeout(function () {
						var sCtgryList = $(SClaimCtgryDropDownList[count].element).data("kendoDropDownList");
						sCtgryList.trigger("change");
						count++;
					}, 500);
				}
			}
			else {
				$("#zipCode").change();
			}
		});

		if(paramData.putOrderStatusCd == "RC" && odClaimInfo.returnsYn != null) {
			$("#withDraw input[type=radio]").removeAttr("checked");
			setTimeout(function() {
				$("#withDraw input[type=radio][value=" + odClaimInfo.returnsYn + "]").prop("checked", true).attr("checked", "checked").change();
				if(paramData.orderStatusCd != 'RA') {
					$('#withDraw_0').prop('disabled', true); //회수여부 > 회수 체크 default
				}
			}, 1000);
		}

		if(paramData.putOrderStatusCd == "RC" && fnNvl(odClaimInfo.returnsYn) != '') {
			if (odClaimInfo.returnsYn == 'N') {
				$('.inputForm__section.toggleSection.addressInfo1').hide();
				$('.inputForm__section.toggleSection.addressInfo2').hide();
			} else { // 'Y'
				$('.inputForm__section.toggleSection.addressInfo1').show();
				$('.inputForm__section.toggleSection.addressInfo2').show();
			}
		}
	};

	//클레임 상세 > 반품상품 수거 주소정보 (보내는 분) 수정
	function initSendShippingInfo(sendShippingInfo){
		claimMgmFunctionUtil.initSendShippingInfo(sendShippingInfo);
	};

	//클레임 상세 > 반품회수 주소정보 (받는 분)
	function initOrderClaimShippingInfoList(receShippingInfoList){
		claimMgmFunctionUtil.initOrderClaimShippingInfoList(receShippingInfoList, paramData);
	};

	//클레임 상세 > 출고정보
	function initOrderClaimReleaseList(deliveryInfoList){
		claimMgmFunctionUtil.initOrderClaimReleaseList(deliveryInfoList, paramData);
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

	function validCsRefundPrice() {
		if(fnNvl($("#claimReasonMsg").val()) == "") {
			fnKendoMessage({message:"상세 사유를 입력 해주세요."});
			return false;
		}
		if(fnNvl($("#refundPriceCS").val()) == "" || $("#refundPriceCS").val() < 1) {
			fnKendoMessage({message:"CS환불 금액을 입력 해주세요."});
			return false;
		}
		if($("#approveSelect").is(":checked")) {
			let targetGrid    = $('#apprGrid').data('kendoGrid');
			let targetGridDs  = targetGrid.dataSource;
			let targetGridArr = targetGridDs.data();

			if (targetGridArr == undefined || targetGridArr == null || targetGridArr.length < 1) {
				fnKendoMessage({message:"승인관리자를 지정해주세요."});
				return false;
			}
		}
		return true;
	}

	function validRejectReason(claimStatusCd) {
		if(claimStatusCd == "CE") {
			// 클레임 상태코드가 취소 거부 이고, 거부 사유 체크 했을 경우
			if($("input[name=cancelRejectType]:checked").val() == 'rejectReasonMsg') {
				if(fnNvl($("#rejectReasonMsg").val()) == '') {
					fnKendoMessage({message:"취소거부 사유를 입력 해주세요."});
					return false;
				}
				if($("#rejectReasonMsg").val().length > 60) {
					fnKendoMessage({message:"취소거부 사유는 최대 " + $("#rejectReasonMsg").attr("maxlength") + "자 까지 입력 가능합니다."});
					$("#rejectReasonMsg").val($("#rejectReasonMsg").val().substr(0, 60));
					return false;
				}
			}
			else {
				if(fnNvl($("#psShippingCompId").val() == '')) {
					fnKendoMessage({message:"택배사를 선택 해주세요."});
					return false;
				}
				if(fnNvl($("#trackingNo").val()) == '') {
					fnKendoMessage({message:"송장번호를 입력 해주세요."});
					return false;
				}
			}
		}
		else if(claimStatusCd == "RE") {
			if(fnNvl($("#refundReject").val()) == '') {
				fnKendoMessage({message:"반품거부 사유를 입력 해주세요."});
				return false;
			}
			if($("#refundReject").val().length > 60) {
				fnKendoMessage({message:"반품거부 사유는 최대 " + $("#refundReject").attr("maxlength") + "자 까지 입력 가능합니다."});
				$("#refundReject").val($("#refundReject").val().substr(0, 60));
				return false;
			}
		}
		return true;
	}

	//변경(저장)
    function fnSave(odAddPaymentReqInfoId, payTp) {
		if($("#fnSave").hasClass("on_click")) {
			return false;
		}
		$("#fnSave").addClass("on_click");
		setTimeout(function() {
			$("#fnSave").removeClass("on_click");
		}, 2000);

    	var data = $('#inputForm').formSerialize(true);
			if( data.rtnValid ){

				// 미출정보 PK
				data.ifUnreleasedInfoId = paramData.ifUnreleasedInfoId;
				data.undeliveredClaimYn = paramData.undeliveredClaimYn == "Y" ? "Y" : "N";

				//'취소거부' <- BOS클레임사유 필수선택 x
				if(fnNvl($("#bosClaimReasonListDiv").css("display")) != "" && $("#bosClaimReasonListDiv").css("display") != "none" && data.odStatusCA != 'CE') {
					let claimReasonValid = validClaimReason(data);
					if(!claimReasonValid) {
						return false;
					}
				}

				// 환불 정보가 보여지고, 계좌 정보가 보여질 경우 체크
				if(fnNvl($(".refundInfo").css('display')) != '' && $(".refundInfo").css('display') != 'none' && fnNvl($("#inputFormRefundBank").css('display')) != '' && $("#inputFormRefundBank").css('display') != 'none') {
					if(!isValidBankAccount) {
						fnKendoMessage({message:"계좌 정보 유효성 확인 후 변경합니다."});
						return false;
					}
				}

				let putOrderStatusCd = paramData.putOrderStatusCd;
				let addPaymentPrice = $(".claimMgm__addPaymentTotalPrice").text().replace(/,/ig, '');
				let addPaymentCnt = $(".claimMgm__addPaymentTotalPrice").data('addpaymentcnt');
				var url = "/admin/order/claim/addOrderClaimRegister";

				data.putOrderStatusCd = putOrderStatusCd;
				data.orderStatusCd = paramData.orderStatusCd;
				// 직접결제여부는 N으로 설정
				data.directPaymentYn = 'N';

				//--> OD_CLAIM SET
				claimInfoRegisterUtil.odClaimSetting(data, paramData, partCancelYn);

				//--> 상품정보 [OD_CLAIM_DETL]
				data.goodsInfoList = claimInfoRegisterUtil.goodsInfoList(data, paramData);

				//--> BOS클레임 사유 SET
				claimInfoRegisterUtil.odClaimCtgrySetting(data, paramData);

				//--> 결제-환불정보
				claimInfoRegisterUtil.paymentInfo(data);

				//-->상품쿠폰목록정보
				data.goodsCouponInfoList = claimInfoRegisterUtil.goodsCouponInfoList(data, paramData);

				//-->장바구니쿠폰목록정보
				data.cartCouponInfoList = claimInfoRegisterUtil.cartCouponInfoList(data, paramData);

				//-->클레임상태, 클레임상태구분 변경
				claimRegisterFnUtil.changeClaimStatus(data, paramData);

				//-->주문회원 정보 및 택배사 반품접수 관련 필요 변수
				claimInfoRegisterUtil.userInfo(data,paramData);

				//주문클레임 환불계좌관리[OD_CLAIM_ACCOUNT]
				claimInfoRegisterUtil.claimAccountInfo(data);

				// 클레임 상태가 취소 / 반품 거부 상태 일 경우
				if(data.claimStatusCd == "CE" || data.claimStatusCd == "RE") {
					// 취소거부 / 반품 거부 사유 체크
					if(!validRejectReason(data.claimStatusCd)) {
						return false;
					}
				}
				// -> 재배송
				if(putOrderStatusCd == 'EC'){
                    console.log("재배송 register...");
					//출고정보
					var validFlag = claimInfoRegisterUtil.releaseList(data);
					if(!validFlag) {
						return false;
					}
				// -> 취소요청 OR 취소완료 OR 반품완료 OR 배송완료 OR CS환불
				}else if(putOrderStatusCd == 'CA' || putOrderStatusCd == 'CC' || putOrderStatusCd == 'RF' ||
						 putOrderStatusCd == 'DC' || putOrderStatusCd == 'CS'){

					//추가결제 실행 시 클레임상태 변경
					if((addPaymentCnt < 1 && addPaymentPrice > 0)) {
						// 추가결제 방법이 가상계좌일경우 가상계좌번호 채번
						if(data.addPayment == 'bankBook') {
							let returnObj = getVirtualAccount();
							if(!returnObj.result) {
								return false;
							}
							odAddPaymentReqInfoId = returnObj.odAddPaymentReqInfoId;
							payTp = returnObj.payTp;
						}
						let validRefund = claimRegisterFnUtil.addPaymentExecution(data, paramData, odAddPaymentReqInfoId, payTp, addPaymentPrice);
						if(!validRefund) {
							return false;
						}
						// 추가 결제 정보가 존재할 경우
						if(odAddPaymentReqInfoId > 0) {
							// 추가 결제 금액이 존재할 경우 url 변경 - 추가 결제 정보를 Set 해주기 위해
							url = "/admin/order/claim/addAddPaymentAfterOrderClaimRegister";
						}
					}
					//주문상세 송장번호 변수 [OD_TRACKING_NUMBER]
					claimInfoRegisterUtil.claimTrackingInfo(data);

					if(putOrderStatusCd == 'CS') {
						var validFlag = validCsRefundPrice();
						if(!validFlag) {
							return false;
						}

						// CS환불정보 Set
						claimInfoRegisterUtil.csRefundInfo(data);
					}
				// -> 반품요청 OR 반품승인 OR 반품완료 OR 반품보류
				}else if(putOrderStatusCd == 'RA' || putOrderStatusCd == 'RI' || putOrderStatusCd == 'RC'){
					console.log("반품요청 OR 반품승인 OR 반품완료 OR 반품보류 register...");

					//주문클레임 받는 배송지 [OD_CLAIM_SHIPPING_ZONE]
					data.recvShippingList = claimInfoRegisterUtil.recvShippingList(paramData);
					//주문클레임 보내는 배송지 [OD_CLAIM_SEND_SHIPPING_ZONE]
					claimInfoRegisterUtil.sendShippingInfo(data);
					if((addPaymentCnt < 1 && addPaymentPrice > 0) && !(data.orderStatusCd == 'RI' && data.claimStatusCd == 'RC')) {
						// 추가결제 방법이 가상계좌일경우 가상계좌번호 채번
						if(data.addPayment == 'bankBook') {
							let returnObj = getVirtualAccount();
							if(!returnObj.result) {
								return false;
							}
							odAddPaymentReqInfoId = returnObj.odAddPaymentReqInfoId;
							payTp = returnObj.payTp;
						}
						//추가결제 실행 시 클레임상태 변경
						let validRefund = claimRegisterFnUtil.addPaymentExecution(data, paramData, odAddPaymentReqInfoId, payTp, addPaymentPrice);
						if (!validRefund) {
							return false;
						}
						// 추가 결제 정보가 존재할 경우
						if (odAddPaymentReqInfoId > 0) {
							// 추가 결제 금액이 존재할 경우 url 변경 - 추가 결제 정보를 Set 해주기 위해
							url = "/admin/order/claim/addAddPaymentAfterOrderClaimRegister";
						}
					}

					//첨부파일 [OD_CLAIM_ATTC]
					claimInfoRegisterUtil.claimAttcList(data);

					$('.inputForm__section.toggleSection.shipInfo').hide(); //배송정보
				} else {
				}
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
		let addPaymentPrice = $(".claimMgm__addPaymentTotalPrice").text().replace(/,/ig, '');
		var odOrderDetlIds = new Array();
		for(var i=0; i<paramData.goodSearchList.length; i++) {
			odOrderDetlIds.push(paramData.goodSearchList[i].odOrderDetlId);
		}
		let param = {
			'orderPrice' : Math.abs(new Number(addPaymentPrice)), //parseInt($('#shippingPrice').val().replace(/,/g,"")), //[추후변경]배송비(추가결제금액)
			'odid' : paramData.odid,
			'odClaimId' : paramData.odClaimId,
			'odOrderDetlIds' : odOrderDetlIds
		};
		return param;
	}

    // 환불정보 > 추가결제 시 버튼선택 (비인증결제, 가상계좌발급, 직접결제)
    //--> 비인증결제
	$('#ng-view').on("click", "#fnCardPay", function(e) {
		$('#fnDirectPay').removeClass("click_radio");
		if(fnNvl($("#bosClaimReasonListDiv").css("display")) != "" && $("#bosClaimReasonListDiv").css("display") != "none") {
			let claimReasonValid = validClaimReason();
			if(!claimReasonValid) {
				return false;
			}
		}
		let param = getAddPaymentPriceParam();
    	claimMgmPopupUtil.open("nonCardBankBookClaimPopup", param, null);
	});

    //--> 직접결제
	$('#ng-view').on("click", "#fnDirectPay", function(e) {
		if($(this).hasClass("click_radio")) {
			$(this).removeClass("click_radio");
			$(this).prop("checked", false);
		}
		else {
			$(this).addClass("click_radio");
		}
	});

	//--> 가상계좌번호 채번
	function getVirtualAccount() {
		let param = getAddPaymentPriceParam();
		var returnObj = {
			odAddPaymentReqInfoId 	: 0,
			result					: true,
			payTp					: 'virtualAccount'
		};
		fnAjax({
			url     : "/admin/order/claim/addBankBookOrderCreate",
			params  : param,
			async	: false,
			contentType: "application/json",
			success : function( data ){
				var result = data.orderRegistrationResult;
				if (result == 'SUCCESS') {
					returnObj.odAddPaymentReqInfoId = data.odAddPaymentReqInfoId;
				} else {
					fnKendoMessage({message : "아래와 같은 사유로 결제가 실패 되었습니다.<br/>" + data.message});
					returnObj.result = false;
				}
			},
			isAction : 'insert'
		});
		return returnObj;
	}

    //--> 가상계좌발급
	$('#ng-view').on("click", "#fnBankBook", function(e) {
		$('#fnDirectPay').removeClass("click_radio");
		// if(fnNvl($("#bosClaimReasonListDiv").css("display")) != "" && $("#bosClaimReasonListDiv").css("display") != "none") {
		// 	let claimReasonValid = validClaimReason();
		// 	if(!claimReasonValid) {
		// 		return false;
		// 	}
		// }
		// let param = getAddPaymentPriceParam();
		// var url = "/admin/order/claim/addBankBookOrderCreate";
		// fnKendoMessage({
		// 	type    : "confirm"
		// 	, message : "무통장입금을 하시겠습니까?"
		// 	, ok      : function(e){
		// 			        fnAjax({
		// 			            url     : url,
		// 			            params  : param,
		// 						contentType: "application/json",
		// 			            success : function( data ){
		// 			            	var result = data.orderRegistrationResult;
		// 			            	var successFalg = false;
		// 			            	if (result == 'SUCCESS') {
		// 			            		result = "가상계좌가 채번 되었습니다.";
		// 								successFalg = true;
		// 			            		fnSave(data.odAddPaymentReqInfoId, 'virtualAccount');
		// 			            	} else {
		// 			            		result = "아래와 같은 사유로 결제가 실패 되었습니다.<br/>" + data.message;
		// 			            	}
		// 			        		fnKendoMessage({
		// 			        			message : result
		// 			        			, ok : function (e) {
		// 			        				if(successFalg) {
		// 			        					fnClose();
		// 									}
		// 							    }
		// 			        		});
		// 			            },
		// 			            isAction : 'insert'
		// 			        })
		// 	}
		// 	, cancel  : function(e){
		// 		$('#fnBankBook').prop('checked', false);
		// 	}
		// });
	});


	// 상품정보 > 대체상품 선택 시 상품조회팝업 -> 클릭
	function fnAddCoverageGoods(id, obj){
		var selectRows	= $(obj).closest('tr');
		var selectedRowData = claimMgmPopupOrderGoodsGrid.dataItem($(selectRows));

		var params = {};
		//params.goodsType = "";
		params.odOrderDetlId = id;
		params.recommendedPrice = selectedRowData.recommendedPrice; //판매가
		params.orderPrice = selectedRowData.orderPrice; //주문금액
		params.goodsCouponPrice = selectedRowData.goodsCouponPrice; //쿠폰할인
		params.paidPrice = selectedRowData.paidPrice; //결제금액
		params.saleStatus = "SALE_STATUS.ON_SALE";	// 판매상태

		params.goodsType = "GOODS_TYPE.NORMAL,GOODS_TYPE.DISPOSAL";	// 상품유형(복수 검색시 , 로 구분)
	//	if(couponTypeValue == "COUPON_TYPE.GOODS" && paymentTYpeValue == "PAYMENT_TYPE.GOODS_DETAIL"){
	//		params.selectType = "single";
	//	}else{
			params.selectType = "multi";
			params.searchChoiceBtn = "Y";
	//	}
		params.columnNameHidden = false;
		params.columnAreaShippingDeliveryYnHidden = false;
		params.columnDpBrandNameHidden = false;
		params.columnStardardPriceHidden = false;
		params.columnRecommendedPriceHidden = false;
		params.columnSalePriceHidden = false;
		params.columnSaleStatusCodeNameHidden = false;
		params.columnGoodsDisplyYnHidden = false;

		fnKendoPopup({
			id         : "goodsSearchPopup",
            title      : "상품조회",  // 해당되는 Title 명 작성
            width      : "1700px",
            height     : "800px",
            scrollable : "yes",
            src        : "#/goodsSearchPopup",
            param      : params,
			success: function(id, dataItem) {

				let claimGoodsDs = $("#claimMgmPopupOrderGoodsGrid").data("kendoGrid");
				var claimGoodsDatas = $("#claimMgmPopupOrderGoodsGrid").data("kendoGrid").dataSource.data();

				var claimGoodsArr = new Array();
				var insertIndex = claimGoodsDs.dataSource._view.length;
				for(var i=0; i<claimGoodsDatas.length; i++) {
					if(claimGoodsDatas[i].odOrderDetlId == selectedRowData.odOrderDetlId) {
						insertIndex = i;
					}
					claimGoodsArr.push(claimGoodsDatas[i].ilGoodsId);
				}
				insertIndex++;

				var newRowObject = new Array();
				var isGoodsPackValid = true;

				for(var i=0; i<dataItem.length; i++) {
					var rowData = dataItem[i];
					if(rowData.goodsTypeCode == 'GOODS_TYPE.PACKAGE') {
						isGoodsPackValid = false;
						continue;
					}

					if(!claimGoodsArr.includes(rowData.goodsId)) {
						let newRow = {
							"odOrderDetlSeq" : "대체배송",
							"orderStatusNm": "결제완료",
							"goodsTpNm": rowData.goodsTypeName,
							"ilItemCd" : rowData.itemCode,
							"itemBarcode" : rowData.itemBarcode,
							"ilGoodsId": rowData.goodsId,
							"goodsNm": rowData.goodsName + "<button id='fnDelCoverageGoods' type='button' class='btn-s btn-white margin10' onclick='$scope.fnDelCoverageGoods(this);'>삭제</button>",
							"storageTypeNm": rowData.storageMethodTypeName,
							"goodsTpCd": rowData.goodsTypeCode,
							"claimCnt": selectedRowData.claimCnt,
							"standardPrice": rowData.standardPrice,
							"recommendedPrice": rowData.recommendedPrice, // 정상가
							"salePrice": rowData.salePrice, // 판매가 (통합몰기준 나머지)
							"orderPrice": 0, //주문금액
							"goodsCouponPrice": 0, //쿠폰할인
							"ilShippingTmplId": rowData.ilShippingTmplId,
							"paidPrice": 0, //결제금액
							"urWarehouseId": rowData.warehouseId,
							"warehouseNm": rowData.warehouseName,
							"storageTypeCd": rowData.storageMethodTypeCode,
							"targetOdOrderDetlId" : selectedRowData.odOrderDetlId,
							"odOrderDetlParentId" : selectedRowData.odOrderDetlParentId,
							"reShipping": true
						};
						// 주문상세 Depth Id가 3보다 작을 경우 원 주문 상세 PK가 Parent Pk로
						if(rowData.odOrderDetlDepthId < 3) {
							newRow.odOrderDetlParentId = selectedRowData.odOrderDetlId;
						}
						newRowObject.push(newRow);
					}
				}
				if(!isGoodsPackValid) {
					fnKendoMessage({message : "묶음 구성 상품을 추가해주세요."});
					return false;
				}
				if(newRowObject.length > 0) {
					for(var i=0; i<newRowObject.length; i++) {
						var objData = newRowObject[i];
						claimGoodsDs.dataSource.insert((insertIndex + i), objData);
					}
					makePaidPriceInfo(selectedRowData.odOrderDetlId);
				}
			}
		});
	};

	/** 재배송일 경우 대체 상품 금액 계산 */
	function makePaidPriceInfo(odOrderDetlId, claimCnt) {

		var claimGoodsDatas = $("#claimMgmPopupOrderGoodsGrid").data("kendoGrid").dataSource.data();
		var goodsInfoList = new Array();

		for(var i=0; i<claimGoodsDatas.length; i++) {
			var obj = new Object();
			var objData = claimGoodsDatas[i];

			if(objData.reShipping != true && objData.targetOdOrderDetlId == odOrderDetlId) {
				claimCnt = $(claimGoodsDatas[i]).data();
			}
			else if(fnNvl(claimCnt) == "" && objData.odOrderDetlId == odOrderDetlId) {
				claimCnt = objData.claimCnt;
			}
			if(objData.reShipping != true) {
				continue;
			}
			obj.odOrderDetlId		= objData.targetOdOrderDetlId;
			obj.ilGoodsId			= objData.ilGoodsId;
			obj.urWarehouseId 		= objData.urWarehouseId;
			obj.warehouseNm 		= objData.warehouseNm;
			obj.goodsTpCd 			= objData.goodsTpCd;
			obj.recommendedPrice 	= objData.recommendedPrice;
			obj.salePrice 			= objData.salePrice;
			obj.claimCnt 			= objData.claimCnt;
			obj.orderPrice 			= objData.orderPrice;
			obj.discountPrice 		= objData.discountPrice;
			obj.paidPrice 			= objData.paidPrice;
			goodsInfoList.push(obj);
		}

		var param = {
				'odOrderId' : paramData.odOrderId,
				'odOrderDetlId' : odOrderDetlId,
				'claimCnt' : claimCnt
		};

		param.goodsInfoList = goodsInfoList;

		fnAjax({
			url     : "/admin/order/claim/getOrderClaimReShippingGoodsPriceInfo",
			params  : param,
			contentType : 'application/json',
			success : function( data ){
				// 선택 상품 목록이 없을 경우
				if(fnNvl(data.goodsInfoList) == '') {
					let orderPaymentInfo = data.orderPaymentInfo;
					for(var i=0; i<claimGoodsDatas.length; i++) {
						if(claimGoodsDatas[i].odOrderDetlId == orderPaymentInfo.odOrderDetlId) {
							claimGoodsDatas[i].orderPrice = orderPaymentInfo.orderPrice;
							claimGoodsDatas[i].couponPrice = orderPaymentInfo.discountPrice;
							claimGoodsDatas[i].cartCouponPrice = orderPaymentInfo.cartCouponPrice;
							claimGoodsDatas[i].goodsCouponPrice = orderPaymentInfo.goodsCouponPrice;
							claimGoodsDatas[i].paidPrice = orderPaymentInfo.paidPrice;
							claimGoodsDatas[i].totSalePrice = orderPaymentInfo.orderPrice;
							claimGoodsDatas[i].recommendedPrice = orderPaymentInfo.recommendedPrice;
							claimGoodsDatas[i].salePrice = orderPaymentInfo.salePrice;
						}
					}
					claimMgmPopupOrderGoodsGrid.dataSource.data(claimGoodsDatas);
					// 대체배송 일 경우 출고처 정보 리프레시
					if($("input[name=reShippingType]:checked").val() == 'S') {
						// 출고처 정보 리프레시
						let form = {};
						form.odClaimId = paramData.odClaimId; //주문클레임pk
						form.odOrderId = paramData.odOrderId; //주문pk
						form.orderStatusCd = paramData.orderStatusCd; //
						form.claimStatusCd =  paramData.putOrderStatusCd; // 변경 클레임 주문 상태
						form.putOrderStatusCd = paramData.putOrderStatusCd; // 변경 클레임 주문 상태
						form.undeliveredClaimYn = paramData.undeliveredClaimYn == "Y" ? "Y" : "N";
						form.targetTp = ($("#bosClaimReasonList > li:eq(0) .targetType").text().indexOf('구매자') < 0 ? 'S' : 'B');
						form.recvZipCd = $("#zipCode").val();
						form.returnsYn = $("input[name=withDraw]:checked").val();
						setClaimStatusTp(form);
						setGridGoodsData(form);
						refreshGoodsGrid(form);
					}
				}
				// 선택 상품 목록이 있을 경우
				else if(data.goodsInfoList != null && data.goodsInfoList.length > 0) {
					var chgPaymentInfo = data.orderPaymentInfo;
					var resDatas = data.goodsInfoList;
					for(var i=0; i<resDatas.length; i++) {
						var resData = resDatas[i];
						for(var j=0; j<claimGoodsDatas.length; j++) {
							var objData = claimGoodsDatas[j];
							if(objData.odOrderDetlId == odOrderDetlId) {
								claimGoodsDatas[j].orderPrice = chgPaymentInfo.orderPrice;
								claimGoodsDatas[j].discountPrice = chgPaymentInfo.discountPrice;
								claimGoodsDatas[j].couponPrice = chgPaymentInfo.discountPrice;
								claimGoodsDatas[j].paidPrice = chgPaymentInfo.paidPrice;
								claimGoodsDatas[j].totSalePrice = chgPaymentInfo.orderPrice;
								claimGoodsDatas[j].recommendedPrice = chgPaymentInfo.recommendedPrice;
								claimGoodsDatas[j].salePrice = chgPaymentInfo.salePrice;
							}
							if(	resData.odOrderDetlId == objData.targetOdOrderDetlId &&
								resData.ilGoodsId == objData.ilGoodsId &&
								objData.reShipping == true) {
								claimGoodsDatas[j].orderPrice = resData.orderPrice;
								claimGoodsDatas[j].goodsCouponPrice = resData.discountPrice;
								claimGoodsDatas[j].couponPrice = resData.discountPrice;
								claimGoodsDatas[j].paidPrice = resData.paidPrice;
								claimGoodsDatas[j].totSalePrice = resData.orderPrice;
								claimGoodsDatas[j].recommendedPrice = resData.recommendedPrice;
								claimGoodsDatas[j].salePrice = resData.salePrice;
								claimGoodsDatas[j].discountRate = resData.discountRate;
								claimGoodsDatas[j].goodsDeliveryType = resData.goodsDeliveryType;
								claimGoodsDatas[j].orderStatusDeliTp = resData.orderStatusDeliTp;
							}
						}
					}
					claimMgmPopupOrderGoodsGrid.dataSource.data(claimGoodsDatas);
					claimMgmFunctionUtil.initOrderClaimReleaseList(data.deliveryInfoList);
				}
			},
			isAction : "select"
		});

		claimMgmPopupOrderGoodsGrid.bind("dataBound", function() {
			// 재배송일경우
			if($("input[name=reShippingType]:checked").val() == 'R') {
				$('.fnAddCoverageGoods').hide();
			}
			// 대체배송일 경우
			else {
				$('.fnAddCoverageGoods').show();
			}
		});
	}

	//상품정보 > 대체상품 선택 후 row 삭제
	function fnDelCoverageGoods(obj){
		var dataRow = $(obj).closest('tr');
		var claimGoodsDs = $("#claimMgmPopupOrderGoodsGrid").data("kendoGrid").dataSource;
		fnKendoMessage({
			message: "선택한 상품을 제거하시겠습니까?",
			type: "confirm",
			ok: function (e) {
				//removeRow(event);
				var data = claimMgmPopupOrderGoodsGrid.dataItem($(dataRow));
				claimGoodsDs.remove(data);
				makePaidPriceInfo(data.targetOdOrderDetlId);
				return true;
			},
			cancel: function (e) {
				$('.fnAddCoverageGoods').show();
				return false;
			}
		})
	};

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

	//CS환불 금액 입력 완료 후 검사
	function fnPriceValid(val) {
		if ($("input:radio[id='odStatusCS_0']").is(':checked')) {
			if (Number($("#spanRefundPrice").text().replace(",", "")) < Number(val)) {
				fnKendoMessage({message : "CS환불금액은 결제금액(" + $("#spanRefundPrice").text() + "원)이하로 입력 해주셔야 합니다.", ok : function() { $("#refundPriceCS").val($("#spanRefundPrice").text()); $("#refundPriceCS").focus(); }});
				return false;
			}
		}
		if ($("input:radio[id='odStatusCS_1']").is(':checked')) {
			if (Number($("#spanRefundPointPrice").text().replace(",", "")) < val) {
				fnKendoMessage({message : "CS환불적립금은 환불적립금액(" + $("#spanRefundPointPrice").text() + "원) 이하로 입력 해주셔야 합니다.", ok : function() { $("#refundPriceCS").val($("#spanRefundPointPrice").text()); $("#refundPriceCS").focus(); }});
				return false;
			}
		}
		if(Number(val) >= 10000) { //10000원 이상 시
			var targetDiv = '#approveSelectDiv';
			$('#approveSelectDiv').show();
			if($("#approveSelect").is(":checked")) {
				$('.approvalManagerDiv').show();
				targetDiv = ".approvalManagerDiv";
			}
			$("#document").animate({scrollTop:$(targetDiv).offset().top}, 400);
		} else {
			$('#approveSelectDiv').hide();
			$('.approvalManagerDiv').hide();
		}
	};

	// ==========================================================================
	// # 승인 담당자 지정
	// ==========================================================================
	function fnSetManagerByTaskAppr() {

		var dataItem  = {};
		var param     = {'taskCode' : 'APPR_KIND_TP.CS_REFUND' };

		fnKendoPopup({
			id          : 'approvalManagerSearchPopup'
			, title       : '승인관리자 선택'
			, src         : '#/approvalManagerSearchPopup'
			, param       : param
			, width       : '1600px'
			, height      : '800px'
			, scrollable  : 'yes'
			, success     : function( stMenuId, data ) {
				console.log('# stMenuId :: ', stMenuId);
				console.log('# 승인자팝업결과 :: ', JSON.stringify(data));
				if(data && !fnIsEmpty(data) && data.authManager2nd) {

					// 승인 담당자 정보 Set
					//fnApprovelSet(data);
				}
			}
		});
	}

	function setRefundData(data) { // 환불 데이터 정보 Set

		initRefundInfo(data.priceInfo, data.goodsCouponList, data.cartCouponList, data.orderGoodList);
	  }
	//---------------Initialize Option Box Start ------------------------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){
		initPopup();
		initDatePicker();
		//택배사
		claimMgmEventUtil.psShipppingCompId();
		//사유
		claimMgmEventUtil.psClaimMallId(paramData, claimReasonData);
    	//배송요청사항
		claimMgmEventUtil.deliveryMsgCdDropDownList();
		//출입정보
		claimMgmEventUtil.doorMsgCdDropDownList();
		//은행명
		//claimMgmEventUtil.bankCodeDropDownList();
		//재배송 & 대체상품 라디오 태그
		claimMgmEventUtil.initReShippingType(function(changeVal) {
			reShippingRadioChange(changeVal);
		});

		//--> 주문상태 별 태그
		//취소요청 시 태그
		claimOrderStatusTypeUtil.initOdStatusCAType();
		//취소요청 -> 취소거부 태그
		claimOrderStatusTypeUtil.initOdStatusCEType();
		//반품요청 시 태그
		claimOrderStatusTypeUtil.initOdStatusRAType();
		//반품보류 시 태그
		claimOrderStatusTypeUtil.initOdStatusRFType();
		//CS환불 시 태그
		claimOrderStatusTypeUtil.initOdStatusCSType(function(changeVal) {
			statusCsRadioChange();
		});

		fnInputValidationForNumber('trackingNo');

		if(!isRefundFlag) {
			setTimeout(function () {
				$("#fnSave").show();
			}, 2000);
		}
	};

	// 상품 그리드 새로고침
	function refreshGoodsGrid(param) {
		fnAjax({
	       	url     : "/admin/order/claim/getOrderClaimView",
	       	params  : param,
	       	contentType: "application/json",
	 		success : function( data ){
 				//환불정보 폼
 				initRefundInfo(data.priceInfo, data.goodsCouponList, data.cartCouponList, data.orderGoodList);
 				// 재배송일 경우
 				if(param.putOrderStatusCd == 'EC') {
					//출고정보 리스트
					initOrderClaimReleaseList(data.deliveryInfoList);
				}
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
		// 그리드 상품 목록 Set 추가
		function setGridGoodsData(data) {
			let goodSearchList = new Array();
			let claimGoodsDatas = $("#claimMgmPopupOrderGoodsGrid").data("kendoGrid").dataSource.data();

			for(let i=0; i<claimGoodsDatas.length; i++) {
				let claimGoodsData = claimGoodsDatas[i];
				if(claimGoodsData.odOrderDetlSeq == 0) {
					continue;
				}
				if(claimGoodsData.reShipping != true) {
					let goodOrgData = {};
					goodOrgData.odOrderId = claimGoodsData.odOrderId;
					goodOrgData.odOrderDetlId = claimGoodsData.odOrderDetlId;
					goodOrgData.odClaimDetlId = claimGoodsData.odClaimDetlId;
					goodOrgData.claimCnt = claimGoodsData.claimCnt;
					goodOrgData.cancelCnt = claimGoodsData.cancelCnt;
					goodOrgData.orderCnt = claimGoodsData.orderCnt;
					goodSearchList.push(goodOrgData);
				}
			}
			//goodOrgData.urWarehouseId = dataItem.urWarehouseId;
			data.goodSearchList = goodSearchList;
		}

		//반품상품 수거 주소정보(보내는 분) > 주소찾기
		function fnAddressPopup() {
			zipCodeUtil.fnDaumPostCode("zipCode", "claimMgm__recvAddr1", "claimMgm__recvAddr2","");
		}
		//반품회수 주소정보 (받는 분) > 주소찾기
		function fnAddressPopup2(id) {
			fnDaumPostcode("claimMgm__zipCodeList_"+id, "claimMgm__recvAddr1List_"+id, "claimMgm__recvAddr2List_"+id,"");
		}

		// 이미지 보기 팝업창 초기화
		function initPopup() {
			$("#imageModal").kendoWindow({
				visible: false,
				modal: true
			});

			$("#imageModal").data("kendoWindow").bind("close", function(e) {
				const modal = $(e.sender.element);
				const $img = modal.find("#imagePreview");
				$img.attr("src", "");
			})
		}

		// 이미지 보기 팝업 열기
		function openPopup() {
			var opt = {
				id: "imageModal",
				width: 500,
				height: 500,
				title: { nullMsg: "이미지 보기" },
			}

			fnKendoInputPoup(opt);
		}

		// 이미지 리스트 그리기
		function renderImageList(data) {
			var htmlString = "";
			var $imageList = $("#imageList");

			htmlString = data.map(function(image) {
				return makeImageItem(image);
			}).join("");

			$imageList.html(htmlString);
		}

		// 이미지 리스트 아이템 그리기
		function makeImageItem(image) {
			return `
				<li>
					<a href="#">
						<figure>
							<img src="${image.src}" alt="${image.title}">
						</figure>
					</a>
				</li>
			`;
		}


	//-------------------------------  Common Function end -------------------------------

	//-------------------------------  events Start  -------------------------------
	function bindEvents() {
		//$("#imageList").on("click", "a", onClickImage);
		// 사유 변경 시

		// 승인 요청 처리 체크박스 변경 시
		$('#ng-view').on("change", "#approveSelect", function() {
			if($(this).is(":checked")) {
				$(".approvalManagerDiv").show();
				$("#document").animate({scrollTop:$(".approvalManagerDiv").offset().top}, 400);
			}
			else {
				$(".approvalManagerDiv").hide();
			}
		});

		// CS 환불 금액 입력 시
		$('#ng-view').on("keyup", "#refundPriceCS", function() {
			var refundTarget = 'spanRefundPrice';
			var remaindTarget = 'claimMgm__remainPaymentPrice';
			if($("input[name=odStatusCS]:checked").val() != 'paymentPriceRefund') {
				refundTarget = 'spanRefundPointPrice';
				remaindTarget = 'claimMgm__remainPointPrice';
			}
			let orgVal = $('#' + refundTarget).data('orgval');
			if(orgVal < 1 || $(this).val() > orgVal) {
				$("#" + refundTarget).text(fnNumberWithCommas(orgVal));
				$('.' + remaindTarget).text(fnNumberWithCommas($('.' + remaindTarget).data('orgval')));
				$('.claimMgm__refundTotalPrice').text(fnNumberWithCommas($('.claimMgm__refundTotalPrice').data('orgval')));
				return false;
			}
			let remainVal = $('.' + remaindTarget).data('orgval') + orgVal - $(this).val();
			$("#" + refundTarget).text(fnNumberWithCommas($(this).val()));
			$('.' + remaindTarget).text(fnNumberWithCommas(remainVal));
			$('.claimMgm__refundTotalPrice').text(fnNumberWithCommas($(this).val()));
		});

		$('#ng-view').on('change', 'div[data-role=grid] input[data-role=dropdownlist]', function (e) {
			var dataItem = claimMgmPopupOrderGoodsGrid.dataItem($(this).closest('tr'));
			let claimCnt = $(this).val();
			let odOrderDetlId = dataItem.odOrderDetlId;
			let claimData= {};
			claimData.odOrderId = dataItem.odOrderId; //주문pk
			claimData.odClaimId = dataItem.odClaimId; //주문클레임pk
			claimData.putOrderStatusCd = parent.POP_PARAM.parameter.putOrderStatusCd;	//주문상태코드
			claimData.orderStatusCd = parent.POP_PARAM.parameter.orderStatusCd;	//주문상태코드
			claimData.claimStatusCd = parent.POP_PARAM.parameter.putOrderStatusCd;
			//claimData.orderStatusCd = dataItem.orderStatusCd;	//주문상태코드
			claimData.targetTp = ($("#bosClaimReasonList > li:eq(0) span.targetType").text().indexOf('구매자') < 0 ? 'S' : 'B');			// 귀책구분 B: 구매자, S: 판매자
			claimData.goodsChange = 1; 			// 수량 변경 (조회 : 0)

			//상품목록에서 데이터를 가져와서 셋팅합니다.
			let goodSearchList = new Array();
			let goodsClaimList = new Array();
			var claimGoodsDatas = $("#claimMgmPopupOrderGoodsGrid").data("kendoGrid").dataSource.data();

			for(var i=0; i<claimGoodsDatas.length; i++) {
				var claimGoodsData = claimGoodsDatas[i];
				if(claimGoodsData.odOrderDetlSeq == 0) {
					continue;
				}
				if(claimGoodsData.reShipping != true) {
					let goodOrgData = {};
					if(claimGoodsData.odOrderDetlId == dataItem.odOrderDetlId) {
						goodOrgData.odOrderId = dataItem.odOrderId;
						goodOrgData.odOrderDetlId = dataItem.odOrderDetlId;
						goodOrgData.odClaimDetlId = dataItem.odClaimDetlId;
						goodOrgData.claimCnt = claimCnt;
						goodOrgData.cancelCnt = dataItem.cancelCnt;
						goodOrgData.orderCnt = dataItem.orderCnt;
					}
					else {
						goodOrgData.odOrderId = claimGoodsData.odOrderId;
						goodOrgData.odOrderDetlId = claimGoodsData.odOrderDetlId;
						goodOrgData.odClaimDetlId = claimGoodsData.odClaimDetlId;
						goodOrgData.claimCnt = claimGoodsData.claimCnt;
						goodOrgData.cancelCnt = claimGoodsData.cancelCnt;
						goodOrgData.orderCnt = claimGoodsData.orderCnt;
					}
					goodSearchList.push(goodOrgData);
				}
			}
			//goodOrgData.urWarehouseId = dataItem.urWarehouseId;
			claimData.goodSearchList = goodSearchList;
			claimData.recvZipCd = $("#zipCode").val();
			claimData.returnsYn = $("input[name=withDraw]:checked").val();
			setClaimStatusTp(claimData);

	 		var shopDropDownList = $('#targetTypeDropDown').data('kendoDropDownList');
			dataItem.set('claimCnt', shopDropDownList.text());
			dataItem.set('claimCnt', shopDropDownList.value());

//			claimMgmPopupOrderGoodsGrid.dataSource.data(goodsClaimList);
			//ajax (클레임수량 변경 -> 결제정보, 환불정보 데이터 변경)

			// 수량 변경한 row가 대체 상품이 아닐 경우에
			if(dataItem.reShipping != true) {
				fnAjax({
			       url     : "/admin/order/claim/getOrderClaimGoodsAmountInfo",
			       params  : claimData,
			       contentType: "application/json",
			       success : function( data ){
			    	   if(paramData.putOrderStatusCd == 'EC') {
						   // 재배송일 경우 출고처 정보 업데이트
						   if($("input[name=reShippingType]:checked").val() == 'R') {
							   claimMgmFunctionUtil.initOrderClaimReleaseList(data.deliveryInfoList);
						   }
			    		   makePaidPriceInfo(dataItem.odOrderDetlId, claimCnt);
			    	   }
			    	   else {
			    		   setRefundData(data);
			    	   }
			 		},
		 			isAction : "select"
		 		});
			}
		});

		$('#ng-view').on("change", "#zipCode", function() {
			let form = {};
			form.odClaimId = paramData.odClaimId; //주문클레임pk
			form.odOrderId = paramData.odOrderId; //주문pk
			form.orderStatusCd = paramData.orderStatusCd; //
			form.claimStatusCd =  paramData.putOrderStatusCd; // 변경 클레임 주문 상태
			form.putOrderStatusCd = paramData.putOrderStatusCd; // 변경 클레임 주문 상태
			form.undeliveredClaimYn = paramData.undeliveredClaimYn == "Y" ? "Y" : "N";
			form.targetTp = ($("#bosClaimReasonList > li:eq(0) .targetType").text().indexOf('구매자') < 0 ? 'S' : 'B');
			form.recvZipCd = $("#zipCode").val();
			form.returnsYn = $("input[name=withDraw]:checked").val();
			setClaimStatusTp(form);
			setGridGoodsData(form);

			refreshGoodsGrid(form);
		});

		$('#ng-view').on('change', '#bankCode', function (e) {
			validAccountInfo();
		});

		$('#ng-view').on("blur", "#holderName, #accountNumber", function() {
			validAccountInfo();
		});
	}

	// 계좌정보 유효성 체크
	function validAccountInfo() {
		let bankCd = $("#holderName").data('bankCd');
		if(bankCd != ''){
			if($("#bankCode").val() != bankCd) {
				isValidBankAccount = false;
				$("#fnValidBankAccount").show();
				return false;
			}
		}
		else {
			isValidBankAccount = false;
			$("#fnValidBankAccount").show();
			return false;
		}
		let chgHolderName = fnNvl($("#holderName").data('accountholder'));
		if(chgHolderName != ''){
			if($("#holderName").val() != chgHolderName) {
				isValidBankAccount = false;
				$("#fnValidBankAccount").show();
				return false;
			}
		}
		else {
			isValidBankAccount = false;
			$("#fnValidBankAccount").show();
			return false;
		}
		let chgAccountNumber = fnNvl($("#accountNumber").data('accountnumber'));
		if(chgAccountNumber != ''){
			if($("#accountNumber").val() != chgAccountNumber) {
				isValidBankAccount = false;
				$("#fnValidBankAccount").show();
				return false;
			}
		}
		else {
			isValidBankAccount = false;
			$("#fnValidBankAccount").show();
			return false;
		}
		isValidBankAccount = true;
		$("#fnValidBankAccount").hide();
	}

	function bindListChangeEvent(id, handler) {
		const $kendoList = $("#" + id).data("kendoDropDownList");

		$kendoList.bind("change", function(e) {
			handler.call(this, e);
		})
	}

	function fnAddCoverage(){
		fnKendoPopup({
			id			: "goodsSearchPopup",
			title		: "상품 검색",  // 해당되는 Title 명 작성
			width		: "1700px",
			height		: "800px",
			scrollable	: "yes",
			src			: "#/goodsSearchPopup",
			param		: params,
			success	: function( id, data ){
				if (data.length == undefined) {
				} else if (data.length > 0 ) {
					var gridDataSource = $("#aGrid").data("kendoGrid").dataSource;
		    		var insertRowData = gridDataSource.view()[$("#clickIdx").val()];
		    		insertRowData.set("itemCode", 		data[0].itemCode);
		    		insertRowData.set("itemBarcode", 	data[0].itemBarcode);
		    		insertRowData.set("goodsId", 		data[0].goodsId);
		    		insertRowData.set("goodsName", 		data[0].itemName);
		    		insertRowData.set("storageMethodTypeName", 	data[0].storageMethodTypeName);
		    		insertRowData.set("recommendedPrice",	data[0].recommendedPrice);
		    		$("#clickIdx").val('');
				}
			}
		});
	}

	// 이미지 클릭 이벤트
	function onClickImage(e) {
		e.preventDefault();

		const $img = $(this).find("img");
		const src = $img[0].src;
		const $imagePreview = $("#imagePreview");

		if( src ) {
			$imagePreview.attr("src", src);
		}

		openPopup();
	}

	// 재배송 라디오 변경 이벤트
	function reShippingRadioChange(val) {
		if(val == 'S') {
			$('.fnAddCoverageGoods').show(); //상품조회팝업 버튼 show
		}
		else {
			var dataRow = $("#claimMgmPopupOrderGoodsGrid tbody > tr");
			var claimGoodsDs = $("#claimMgmPopupOrderGoodsGrid").data("kendoGrid").dataSource;
			var dataArr = new Array();
			for(var i=0; i<dataRow.length; i++) {
				var data = claimMgmPopupOrderGoodsGrid.dataItem(dataRow[i]);
				if(data.reShipping == true) {
					dataArr.push(data);
				}
			}
			if(dataArr.length > 0) {
				for(var i=0; i<dataArr.length; i++) {
					claimGoodsDs.remove(dataArr[i]);
				}
				fnInitGrid();

				claimMgmPopupOrderGoodsGrid.bind("dataBound", function() {
					$('.fnAddCoverageGoods').hide(); //상품조회팝업 버튼 hide
				});
			}
			else {
				$('.fnAddCoverageGoods').hide(); //상품조회팝업 버튼 hide
			}
		}
	}

	// CS 환불정보 라디오 변경 이벤트
	function statusCsRadioChange() {
		$("#spanRefundPrice").text(fnNumberWithCommas($("#spanRefundPrice").data('orgval')));
		$("#spanRefundPointPrice").text(fnNumberWithCommas($("#spanRefundPointPrice").data('orgval')));
		$(".claimMgm__remainPaymentPrice").text(fnNumberWithCommas($(".claimMgm__remainPaymentPrice").data('orgval')));
		$(".claimMgm__remainPointPrice").text(fnNumberWithCommas($(".claimMgm__remainPointPrice").data('orgval')));
		$(".claimMgm__refundTotalPrice").text(fnNumberWithCommas($(".claimMgm__refundTotalPrice").data('orgval')));
		$("#refundPriceCS").val(0);
		$('#approveSelectDiv').hide();
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

	// 이미지 팝업 호출
	function fnShowImage(imageUrl){

		fnKendoPopup({
			id      : 'feedbackPopup'
			, title   : '첨부파일보기'
			, src     : '#/feedbackPopup'
			, width   : '600px'
			, height  : '600px'
			, param   : { "LOGO_URL" : imageUrl }
			, success : function (id, data) {
			}
		});
	}

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};

	/** Common Clear*/
	$scope.fnClear =function(){	fnClear(); };
	$scope.fnSave = function(inicisNonAuthenticationCartPay, virtualAccountData) {	fnSave(inicisNonAuthenticationCartPay, virtualAccountData);	}; //변경(저장)
	$scope.fnClose = function(){ fnClose(); };
	/**대체상품 추가-삭제*/
	$scope.fnAddCoverageGoods = function(id, obj){fnAddCoverageGoods(id, obj);};
	$scope.fnDelCoverageGoods = function(id){fnDelCoverageGoods(id);};
	/**CS환불 > 계좌정보 유효성 확인*/
	$scope.fnValidBankAccount = function( ) {	fnValidBankAccount();};
	$scope.fnAddressPopup = function() { fnAddressPopup(); };
	$scope.fnAddressPopup2 = function(id) { fnAddressPopup2(id); };
	$scope.fnPriceValid = function(val) { fnPriceValid(val); }
	$scope.fnShowImage = function(url){ fnShowImage(url); };

	$scope.fnApprovalRequest = function() { fnSetManagerByTaskAppr(); };
	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------
	// fnInputValidationForAlphabetNumberLineBreakComma("code");
	fnCheckLength("detailReason", 255);
	fnInputValidationForNumber("shippingFee");
	fnInputValidationForNumber("phone-prefix");
	fnInputValidationForNumber("phone1");
	fnInputValidationForNumber("phone2");
	fnInputValidationForNumber("accountNumber");
	fnCheckLength("accountNumber", 14);
	//------------------------------- Validation End -------------------------------------

}); // document ready - END
