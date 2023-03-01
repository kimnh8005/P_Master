/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 클레임 신청 (CS환불)
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.01.05		김승우          최초생성
 * @ 2021.06.03	 	최윤지			수정
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
			PG_ID  : "claimMgmCSPopup",
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
			fnInitMaskTextBox();
			fnInitOptionBox();
			bindEvents(); //(이미지 - 추후삭제예정)
		}, 200);
	};

	//--------------------------------- Button Start---------------------------------
	// 버튼 초기화
	function fnInitButton(){
		$("#fnSave, #fnValidBankAccount").kendoButton();
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
		form.goodSearchList = paramData.goodSearchList; //주문상세pk
		form.claimStatusCd = paramData.putOrderStatusCd; // 변경 클레임 주문 상태
		form.undeliveredClaimYn = paramData.undeliveredClaimYn == "Y" ? "Y" : "N";
		setClaimStatusTp(form);

		let putOrderStatusCd = paramData.putOrderStatusCd; // 변경 -> 클레임주문상태


		fnAjax({
	       	url     : "/admin/order/claim/getOrderClaimView",
	       	params  : form,
	       	contentType: "application/json",
	 		success : function( data ){

	 				//클레임 주문상태에 따라 show(), hide()
	 				claimMgmViewUtil.claimShow(putOrderStatusCd);

					//결제정보 폼
					initCSPaymentInfo(data.paymentInfo, data.accountInfo, paramData);

					//환불정보 폼
					initRefundInfo(data.priceInfo, data.goodsCouponList, data.cartCouponList);

	 				//상품정보 그리드
	 				claimMgmPopupOrderGoodsGrid.dataSource.data(data.orderGoodList);

					//클레임정보
					initClaimInfo(data.customerReasonInfo, data.orderGoodList);
			},
	 		isAction : "select"
 		});
	};

	//Default Setting
	function fnDefaultSet() {
		//Default Set
		$('#cancelRejectDiv').hide(); //취소거부 구분 div hide (취소요청 -> 취소완료 / 취소거부)
		// $('#approveSelectDiv').hide(); //승인요청처리 div hide (CS환불 > 환불금액 10000원 이상)
		$('.approvalManagerDiv').hide();	// 승인관리자 div hide (CS환불 > 환불금액 10000원 이상)

		$("#toDateDiv").val(fnGetToday()); //CS 환불 적립금 유효 시작일 셋팅
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

								if (paramData.putOrderStatusCd == 'RC') {
									var targetTp = "S";
									if (sClaimData[j].targetTypeName == "구매자 귀책") {
										targetTp = "B";
									}

									let form = {};
									form.odClaimId = paramData.odClaimId; //주문pk
									form.odOrderId = paramData.odOrderId; //주문클레임pk
									form.orderStatusCd = paramData.orderStatusCd; //주문클레임pk
									form.claimStatusCd = paramData.putOrderStatusCd; // 변경 클레임 주문 상태
									//form.goodSearchList = paramData.goodSearchList; //주문상세pk
									setGridGoodsData(form);
									form.targetTp = targetTp;
									form.recvZipCd = $("#zipCode").val();
									form.returnsYn = $("input[name=withDraw]:checked").val();
									setClaimStatusTp(form);

									refreshGoodsGrid(form);
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
			editable : true,
			columns : orderMgmGridUtil.claimMgmCSPopupOrderGoodsList()
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

	//클레임 상세 > 결제정보
	function initCSPaymentInfo(paymentInfo, accountInfo, paramData){
		claimMgmFunctionUtil.initCSPaymentInfo(paymentInfo, accountInfo, paramData);
	};

	//클레임 상세 > 환불정보 (쿠폰, 장바구니 포함)
	function initRefundInfo(priceInfo, goodsCouponList, cartCouponList){
		// claimMgmFunctionUtil.initRefundInfo(priceInfo, goodsCouponList, cartCouponList, paramData);
		claimMgmFunctionUtil.initCSRefundInfo(priceInfo, goodsCouponList, cartCouponList, paramData);
	};
	//-------------------------------  Grid End  -------------------------------
	//-------------------------------- fn Start --------------------------------------
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
	function validClaimReason() {
		let validFlag = true;

		if(fnNvl($("#psClaimMallId").val()) == "") {
			fnKendoMessage({message:"사유를 선택 해주세요."});
			return false;
		}

		if(fnNvl($("#claimReasonMsg").val()) == "") {
			fnKendoMessage({message:"상세사유를 입력 해주세요."});
			return false;
		}

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

	//변경(저장)
    function fnSave() {
    	var data = $('#inputForm').formSerialize(true);
		if( data.rtnValid ){
			// TODO 추후 예치금 계약 완료되면 삭제 예정
			// if($("input[name=csRefundTp]:checked").val() == 'CS_REFUND_TP.PAYMENT_PRICE_REFUND') {
			// 	fnKendoMessage({message:"CS환불 > 예치금 환불 개발 진행 중 입니다."});
			// 	return false;
			// }

			if(!validClaimReason()) {
				return false;
			}

			let refundPrice = Number($("#refundPriceCS").val().replace(/\,/g, ''));
			if(refundPrice < 1) {
				fnKendoMessage({message:"CS환불 금액을 입력 해주세요."});
				return false;
			}

			if($("input[name=csRefundTp]:checked").val() == 'CS_REFUND_TP.PAYMENT_PRICE_REFUND' && !isValidBankAccount) {
				fnKendoMessage({message:"계좌정보 유효성확인을 해주세요."});
				return false;
			}

			if($("input[name=csRefundTp]:checked").val() == 'CS_REFUND_TP.POINT_PRICE_REFUND' && $('#pointPaymentAmount').val() == '') {
				fnKendoMessage({message:"CS환불 적립금 유효일을 입력 해주세요."});
				return false;
			}

			//적립금 환불 시 유효일 셋팅
			if($("input[name=csRefundTp]:checked").val() == 'CS_REFUND_TP.POINT_PRICE_REFUND') {
				data.csPointValidityDay = $('#pointPaymentAmount').val();
			}

			if(refundPrice >= 10000) {
				// if($('#approveSelect').is(":checked")){
					let targetGrid    = $('#apprGrid').data('kendoGrid');
					let targetGridDs  = targetGrid.dataSource;
					let targetGridArr = targetGridDs.data();
					if(targetGridArr.length < 1) {
						fnKendoMessage({message:"승인관리자를 지정 해주세요."});
						return false;
					}
				// }
			}

			data.putOrderStatusCd = "CS";

			//--> OD_CLAIM SET
			claimInfoRegisterUtil.odClaimSetting(data, paramData, partCancelYn);

			//--> 상품정보 [OD_CLAIM_DETL]
			data.goodsInfoList = claimInfoRegisterUtil.goodsInfoList(data, paramData);

			//-> CS 상태 및 CS 환불 금액 정보 Set
			claimInfoRegisterUtil.csStatusPriceInfo(data);

			//주문클레임 환불계좌관리[OD_CLAIM_ACCOUNT]
			claimInfoRegisterUtil.claimAccountInfo(data);

			fnAjax({
				url     : "/admin/order/claim/addOrderCSRefundRegister",
				params  : data,
				contentType: "application/json",
				success :
					function( data , resultcode){
						if(fnNvl(resultcode) != "") {
							if(resultcode.code != "0000") {
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

				var apprSubUserId;
				var apprUserId
				if(data && !fnIsEmpty(data) && data.authManager2nd){
					var authManager1 = data.authManager1st;
					apprSubUserId = authManager1.apprUserId;
					var authManager2 = data.authManager2nd;
					apprUserId = authManager2.apprUserId;

					var apprList = new Array();
					if(authManager1.adminTypeName != undefined){
						apprList.push(
							{
								'apprManagerType'		: authManager1.apprManagerType,
								'apprAdminInfo'			: (authManager1.apprManagerType == 'APPR_MANAGER_TP.FIRST' ? '1차 승인관리자' : '최종 승인관리자'),
								'adminTypeName'			: authManager1.adminTypeName,
								'apprUserName'			: authManager1.apprUserName,
								'apprLoginId'			: authManager1.apprLoginId,
								'organizationName'		: authManager1.organizationName,
								'userStatusName'		: authManager1.userStatusName,
								'teamLeaderYn'			: authManager1.teamLeaderYn,
								'grantAuthYn'			: authManager1.grantAuthYn,
								'grantUserName'			: authManager1.grantUserName,
								'grantLoginId'			: authManager1.grantLoginId,
								'grantAuthStartDt'		: authManager1.grantAuthStartDt,
								'grantAuthEndDt'		: authManager1.grantAuthEndDt,
								'grantUserStatusName'	: authManager1.grantUserStatusName,
								'apprUserId'			: authManager1.apprUserId
							}
						);
					}

					if(authManager2 != undefined){
						apprList.push(
							{
								'apprManagerType'		: authManager2.apprManagerType,
								'apprAdminInfo'			: (authManager2.apprManagerType == 'APPR_MANAGER_TP.FIRST' ? '1차 승인관리자' : '최종 승인관리자'),
								'adminTypeName'			: authManager2.adminTypeName,
								'apprUserName'			: authManager2.apprUserName,
								'apprLoginId'			: authManager2.apprLoginId,
								'organizationName'		: authManager2.organizationName,
								'userStatusName'		: authManager2.userStatusName,
								'teamLeaderYn'			: authManager2.teamLeaderYn,
								'grantAuthYn'			: authManager2.grantAuthYn,
								'grantUserName'			: authManager2.grantUserName,
								'grantLoginId'			: authManager2.grantLoginId,
								'grantAuthStartDt'		: authManager2.grantAuthStartDt,
								'grantAuthEndDt'		: authManager2.grantAuthEndDt,
								'grantUserStatusName'	: authManager2.grantUserStatusName,
								'apprUserId'			: authManager2.apprUserId
							}
						);
					}
					$("#apprGrid").data("kendoGrid").dataSource.data(apprList);
				}
			}
		});
	}
	//---------------Initialize Option Box Start ------------------------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){
		//사유
		claimMgmEventUtil.csPsClaimMallId(paramData, claimReasonData);
		//은행명
		claimMgmEventUtil.bankCodeDropDownList();

		//--> 주문상태 별 태그
		//CS환불 시 태그
		setTimeout(function() {
			claimOrderStatusTypeUtil.initOdStatusCSType(function(changeVal) {
				statusCsRadioChange(changeVal);
				//fnInputCSRefundPrice();
			});
			// 외부몰 주문일 경우 적립금 라디오 버튼 선택 불가 처리
			if(outmallOrderYn == "Y") {
				// $("#fnSave").hide();
				// $("#refundPaymentDiv").show();
				$("input[name=csRefundTp]:eq(1)").parents("label").hide();
			}
		}, 800);

		fnKendoDatePicker({
			id          : 'pointPaymentAmountToDate',
			format		: 'yyyy-MM-dd',
			min 		: fnGetToday(),
			change      : function(e){
				dateTimeChk('pointPaymentAmountToDate','pointPaymentAmountToDate','유효일날짜보기')

				//유효일자 변경 시 유효일 노출
				var sDate = new Date(fnGetToday());
				var eDate = new Date(e.sender._oldText);
				var diff = Math.abs(eDate.getTime() - sDate.getTime());
				diff = Math.ceil(diff / (1000 * 3600 * 24));
				$("#pointPaymentAmount").val(diff);
			}
		});
	};

	// 상품 그리드 새로고침
	function refreshGoodsGrid(param) {
		fnAjax({
	       	url     : "/admin/order/claim/getOrderClaimView",
	       	params  : param,
	       	contentType: "application/json",
	 		success : function( data ){
 				//환불정보 폼
 				initRefundInfo(data.priceInfo, data.goodsCouponList, data.cartCouponList);
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

	//-------------------------------  Common Function end -------------------------------

	//-------------------------------  events Start  -------------------------------
	//관리자 지급 유효일 변경 시 일자 노출
	$("input[name=pointPaymentAmount]").keyup(function() {
		var today = fnGetToday();
		$("#pointPaymentAmountToDate").data("kendoDatePicker").value(fnGetDayAdd(today, this.value*1));
	});

	function bindEvents() {

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
	}

	$('#ng-view').on('change', '#bankCode', function (e) {
		validAccountInfo();
	});

	$('#ng-view').on("blur", "#holderName, #accountNumber", function() {
		validAccountInfo();
	});

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

	// CS 환불정보 라디오 변경 이벤트
	function statusCsRadioChange(changeVal) {
		if(changeVal == 'CS_REFUND_TP.PAYMENT_PRICE_REFUND') {
			$("#pointPaymentAmountDiv").hide(); //적립금 CS 환불은 유지.

			$("#inputFormRefundBank").show();
			// TODO 추후 예치금 계약 완료되면 삭제 예정
			// $("#fnSave").hide();
			// $("#refundPaymentDiv").show();
			// $("#document").animate({scrollTop:$("#refundPaymentDiv").offset().top}, 400);
		}
		else {
			$("#pointPaymentAmountDiv").show(); //적립금 CS 환불은 유지.

			$("#inputFormRefundBank").hide();
			// TODO 추후 예치금 계약 완료되면 삭제 예정
			// $("#fnSave").show();
			// $("#refundPaymentDiv").hide();
		}
		// $("#spanRefundPrice").text(fnNumberWithCommas($("#spanRefundPrice").data('orgval')));
		// $("#spanRefundPointPrice").text(fnNumberWithCommas($("#spanRefundPointPrice").data('orgval')));
		// $(".claimMgm__remainPaymentPrice").text(fnNumberWithCommas($(".claimMgm__remainPaymentPrice").data('orgval')));
		// $(".claimMgm__remainPointPrice").text(fnNumberWithCommas($(".claimMgm__remainPointPrice").data('orgval')));
		// $(".claimMgm__refundTotalPrice").text(fnNumberWithCommas($(".claimMgm__refundTotalPrice").data('orgval')));
		// $("#refundPriceCS").val(0);
		// $('#approveSelectDiv').hide();
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

	// CS환불금액 SUM 처리
	function setSumCSRefundPrice() {
		var sum = 0;
		$('.comm-input.num_only').each(function(idx, ele){
			let value = isNaN(Number(this.value)) ? 0 : Number(this.value);
			sum += value;
		});
		$('#refundPriceCS').val(fnNumberWithCommas(sum)); // 환불정보 > CS 환불금액 입력

		if(sum >= 10000) { //10000원 이상 시
			var targetDiv = '#approvalManagerDiv';
			// $('#approveSelectDiv').show();
			if($('.approvalManagerDiv').css('display') != 'block') {
				// if ($("#approveSelect").is(":checked")) {
					$('.approvalManagerDiv').show();
					targetDiv = ".approvalManagerDiv";
				// }
				$("#document").animate({scrollTop: $(targetDiv).offset().top}, 400);
			}
		}
		else {
			// $('#approveSelectDiv').hide();
			$('.approvalManagerDiv').hide();
		}
	}

	// 그리드에서 CS환불 금액 입력 시
	function fnInputCSRefundPrice(obj) {

		let dataRow = $(obj).closest('tr');
		let data = claimMgmPopupOrderGoodsGrid.dataItem($(dataRow));
		let paidPrice = data.paidPrice;
		var thisValue = isNaN(Number($(obj).val())) ? 0 : Number($(obj).val());
		$(obj).val(thisValue);
		data.csRefundPrice = thisValue;

		if(paidPrice < thisValue) {
			fnKendoMessage(
			{
				type : "confirm",
				message: "CS 환불금액이 결제금액을 초과하였습니다.</br>현재 입력한 금액으로 진행하시겠습니까?",
				cancel: function () {
					$(obj).val(paidPrice);
					data.csRefundPrice = paidPrice;
					setSumCSRefundPrice();
				},
				ok: function () {
					setSumCSRefundPrice();
				}
			});
		}
		else {
			setSumCSRefundPrice();
		}
	}
	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};

	/** Common Clear*/
	$scope.fnClear =function(){	fnClear(); };
	$scope.fnSave = function() {	fnSave();	}; //변경(저장)
	$scope.fnClose = function(){ fnClose(); };
	/**대체상품 추가-삭제*/
	/**CS환불 > 계좌정보 유효성 확인*/
	$scope.fnValidBankAccount = function( ) {	fnValidBankAccount();};
	$scope.fnInputCSRefundPrice = function(obj) { fnInputCSRefundPrice(obj); }
	$scope.fnShowImage = function(url){ fnShowImage(url); };

	$scope.fnApprovalRequest = function() { fnSetManagerByTaskAppr(); };

	/** 승인-초기화 */
	$scope.fnApprovalInit = function(){
		// 그리드 비우기
		$("#apprGrid").data("kendoGrid").destroy();
		$("#apprGrid").empty();
		initOrderClaimApprList();
	};
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
	fnInputValidationByRegexp("pointPaymentAmount", /[^0-9]/g);
	//------------------------------- Validation End -------------------------------------

}); // document ready - END
