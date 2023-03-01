/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 클레임 상세팝업 (녹즙)
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.08		최윤지          최초생성
 * @ 2021.04.27	 	최윤지			수정
 * **/
var datePicker = null;
var dayOn = [];
var deliveryList = [];
var dawnDeliveryList = [];
var shippingListDeliveryDtPicker = null;
var dayOff = [];
//'취소 or 반품' 신청 시 클레임정보 > [사유(쇼핑몰 클레임 사유)]가 '기타(고객센터)'로 기본값 설정
//"쇼핑몰클레임사유_bos클레임사유(대)_bos클레임사유(중)_bos클레임사유(귀책처)"
var defaultValuePsClaimMallId = "0_1_9_23_41";
var claimMgmGreenJuiceViewUtil = {
	//클레임 주문상태에 따라 show(), hide()
	claimShow : function (putOrderStatusCd) {
		$('.inputForm__section.toggleSection.reShippingInfo').hide();	// 재배송정보 영역 숨김
		$('.inputForm__section.shippingInfo').hide();					// 배송정보 영역 숨김
		$('#fnNext').hide();
		// -> 재배송
		if(putOrderStatusCd == 'EC'){
			$('.inputForm__section.shippingInfo').show();				// 배송정보 영역 노출
			$('.inputForm__section.payInfo').hide(); //결제정보
			$('.inputForm__section.refundInfo').hide(); //환불정보
			$('#fnSave').hide();
			$('#fnNext').show();
			// -> 취소요청 OR 취소완료 OR 반품완료 OR 배송완료 OR CS환불
		}
	}
};

var claimMgmGreenJuiceFunctionUtil = {
	//--> 클레임정보
	initClaimInfo: function(customerReasonInfo, orderGoodList, paramData) {
		//사유-상세사유
		if(paramData.orderStatusCd == 'CA' || paramData.orderStatusCd == 'RA'){
			$('#claimReasonMsg').val(orderGoodList[0].claimReasonMsg).attr('disabled', true);
			$("#psClaimMallId").data("kendoDropDownList").value(orderGoodList[0].psClaimMallId);
			$("#psClaimMallId").data("kendoDropDownList").enable( false );
		} else if(paramData.orderStatusCd == 'RI' || paramData.orderStatusCd == 'EC'){
			$("#psClaimMallId").data("kendoDropDownList").value(orderGoodList[0].psClaimMallId);
        }

		//BOS 클레임 사유
		const $target = $("#startBosClaimReasonListArea .claimMgm__bosClaimReasonList");
		$target.empty();

		let tpl = $('#bosClaimReasonListItem').html();
		let tplObj = null;
		let orderStatusChangeList = new Array();
		for(let i=0; i<orderGoodList.length; i++){

			orderStatusChangeList.push(orderGoodList[i].orderStatusNm); //주문상태 리스트
			tplObj = $(tpl);
			tplObj.find(".claimMgm__goodsNm").text("상품명 : "+orderGoodList[i].goodsNm); //상품명
			$target.append(tplObj);
			//let odOrderId = paramData.odOrderId;
			//let urSupplierId = "2";
			tplObj.find("input[name=lclaimCtgryId]").attr({"id" : "lclaimCtgryId_"+i, "data-ilgoodsid" : orderGoodList[i].ilGoodsId});
			tplObj.find("input[name=mclaimCtgryId]").attr("id", "mclaimCtgryId_"+i);
			tplObj.find("input[name=sclaimCtgryId]").attr("id", "sclaimCtgryId_"+i);
			tplObj.find(".targetType").attr("id", "targetType_"+i);
			tplObj.find(".supplierName").attr("id", "supplierName_"+i);
			tplObj.find(".psClaimBosId").attr("id", "psClaimBosId_"+i);
			tplObj.find(".psClaimBosSupplyId").attr("id", "psClaimBosSupplyId_"+i);
			tplObj.find(".odOrderDetlId").text(orderGoodList[i].odOrderDetlId).attr("id", "odOrderDetlId_"+i);
			tplObj.find(".bosClaimResultView").attr("id", "bosClaimResultView_"+i);
			tplObj.attr("data-odorderdetlid", orderGoodList[i].odOrderDetlId);
		}

		let orderStatusChangeData = new Set(orderStatusChangeList);
		const uniqueOrderStatusChange = [...orderStatusChangeData];
		//주문상태 (중복제거)
		if(paramData.putOrderStatusCd == 'CS'){ //CS환불이면 주문상태변경X, 선택된 상품에 판매가 일부 환불(적립금 or PG 환불)
			$(".claimMgm__odStatusChange").text(paramData.putOrderStatus);
		} else if(paramData.putOrderStatusCd == 'CC' && paramData.orderStatusCd == 'CA'){
			$(".claimMgm__odStatusChange").html('<span id="odStatusCA" name="" class="radios-wrapper fb__custom__radio"></span>');
			claimOrderStatusTypeUtil.initOdStatusCAType();
		} else if (paramData.orderStatusCd == 'RA'){ //반품요청이면 주문상태 라디오버튼 활성화 (반품완료, 반품승인, 취소거부)
			$(".claimMgm__odStatusChange").html('<span id="odStatusRA" name="" class="radios-wrapper fb__custom__radio"></span>');
			claimOrderStatusTypeUtil.initOdStatusRAType();

		}else if (paramData.orderStatusCd == 'RF'){ //반품보류이면 주문상태 라디오버튼 활성화 (반품완료, 배송완료)
			$(".claimMgm__odStatusChange").html('<span id="odStatusRF" name="" class="radios-wrapper fb__custom__radio"></span>');
			claimOrderStatusTypeUtil.initOdStatusRFType();

		}else if (paramData.putOrderStatusCd == 'DI' ){ //배송중이면 반품승인, 반품완료
			$(".claimMgm__odStatusChange").text(uniqueOrderStatusChange+" ➜ "+paramData.putOrderStatus);

		}else if (paramData.orderStatusCd == 'RI') { //반품승인이면 반품완료, 반품보류
			$(".claimMgm__odStatusChange").html('<span id="odStatusRI" name="" class="radios-wrapper fb__custom__radio"></span>');
			claimOrderStatusTypeUtil.initOdStatusRIType();
		}else {
			$(".claimMgm__odStatusChange").text(uniqueOrderStatusChange+" ➜ "+paramData.putOrderStatus);
		}

		//BOS클레임사유 초기화
		$(".claimMgm__bosClaimReasonList").children().hide();
		$(".claimMgm__bosClaimReasonList").children().eq(0).show();
		$('.bosClaimReasonList__listItem').children().eq(0).hide();

		//"상품별 사유 선택" 체크박스 체크
		if(orderGoodList.length > 1 ) {
			$("#eachGoodsDiv").show();
			$("#eachGoodsReasonSelect").click(function(){
				let chk = $(this).is(":checked");
				if(chk){
					for (let i = 0; i < orderGoodList.length; i++) {

						var lCtgry = $(LClaimCtgryDropDownList[0].element).data("kendoDropDownList");
						var mCtgry = $(MClaimCtgryDropDownList[0].element).data("kendoDropDownList");
						var slCtgry = $(SClaimCtgryDropDownList[0].element).data("kendoDropDownList");

						var lCtgryList = $(LClaimCtgryDropDownList[i].element).data("kendoDropDownList");
						var mCtgryList = $(MClaimCtgryDropDownList[i].element).data("kendoDropDownList");
						var sCtgryList = $(SClaimCtgryDropDownList[i].element).data("kendoDropDownList");

						// 클레임사유(대)
						lCtgryList.value(lCtgry.value());
						lCtgryList.trigger("change");
						// 클레임사유(중)
						mCtgryList.value(mCtgry.value());
						mCtgryList.trigger("change");
						// 클레임사유(소)
						sCtgryList.value(slCtgry.value());
						sCtgryList.trigger("change");
					}

					$(".claimMgm__bosClaimReasonList").children().show();
					$('.bosClaimReasonList__listItem').children().eq(0).show();
				}else{
					$(".claimMgm__bosClaimReasonList").children().hide();
					$(".claimMgm__bosClaimReasonList").children().eq(0).show();
					$('.bosClaimReasonList__listItem').children().eq(0).hide();
				}
			});
		} else {
			$("#eachGoodsDiv").hide();
		}

	},
	//-->결제정보
	initPaymentInfo : function(paymentInfo, accountInfo){
		$("#inputFormRefundBank").hide();
		$(".claimMgm__paidPrice").text(fnNumberWithCommas(paymentInfo.paidPrice)); //주문금액
		//$(".claimMgm__payTpNm").text(fnNumberWithCommas(paymentInfo.payTpNm)); //결제수단
		$(".claimMgm__payTpNm").text(paymentInfo.refundPayTpNm); //결제수단
		$(".claimMgm__refundType").text(paymentInfo.payTpNm);
		$(".claimMgm__paymentPrice").text(fnNumberWithCommas(paymentInfo.paymentPrice)); //결제금액
		$(".claimMgm__pointPrice").text(fnNumberWithCommas(paymentInfo.pointPrice)); //적립금
		$(".claimMgm__odPaymentMasterId").text(paymentInfo.odPaymentMasterId); //odPaymentMasterId

		if(paymentInfo.payTp == "PAY_TP.VIRTUAL_BANK") {
			$("#inputFormRefundBank").show();
			$("#holderName").val(accountInfo.accountHolder);
			$("#accountNumber").val(accountInfo.accountNumber);
			claimMgmEventUtil.bankCodeDropDownList(accountInfo.bankCd);
		}
	},
	//-> 환불정보
	initRefundInfo : function(priceInfo, goodsCouponList, cartCouponList, orderGoodList, paramData){

		$("#tbodyRefundGoodsArea").empty();
		//환불정보 > 상품정보
		let refundGoodsObj = new Object();

		let refundGoodsTplObj = $($("#divRefundGoodsArea").html());
		var refundgoodsTplStr = "";
		if(fnNvl(priceInfo.goodsPriceList) != '') {
			var goodsInfo = new Object();
			for(var i=0; i<priceInfo.goodsPriceList.length; i++) {
				if(fnNvl(goodsInfo[priceInfo.goodsPriceList[i].ilGoodsId]) != "") {
					goodsInfo[priceInfo.goodsPriceList[i].ilGoodsId] = {
						"goodsNm" : priceInfo.goodsPriceList[i].goodsNm,
						"claimCnt" : goodsInfo[priceInfo.goodsPriceList[i].ilGoodsId].claimCnt + priceInfo.goodsPriceList[i].claimCnt,
						"paidPrice" : goodsInfo[priceInfo.goodsPriceList[i].ilGoodsId].paidPrice + priceInfo.goodsPriceList[i].paidPrice,
					};
				}
				else {
					goodsInfo[priceInfo.goodsPriceList[i].ilGoodsId] = {
						"goodsNm" : priceInfo.goodsPriceList[i].goodsNm,
						"claimCnt" : priceInfo.goodsPriceList[i].claimCnt,
						"paidPrice" : priceInfo.goodsPriceList[i].paidPrice,
					};
				}
			}

			for(var key in goodsInfo) {
				refundGoodsTplObj.find(".refundGoods_goodsNm").text(goodsInfo[key].goodsNm);
				refundGoodsTplObj.find(".refundGoods_cnt").text(fnNumberWithCommas(goodsInfo[key].claimCnt));
				refundGoodsTplObj.find(".refundGoods_refundPrice").text(fnNumberWithCommas(goodsInfo[key].paidPrice));
				refundgoodsTplStr += refundGoodsTplObj.html();
			}
			$("#tbodyRefundGoodsArea").append(refundgoodsTplStr);
		}
		else {
			$("#tbodyRefundGoodsArea").append($("#divRefundGoodsNoneArea").html());
		}

		//상품쿠폰 리스트 template
		const $target = $("#startGoodsCouponListArea .claimMgm__goodsCouponList");
		var goodsCouponPrice = 0;
		$target.empty();

		let tpl = $('#goodsCouponListItem').html();
		let tplObj = null;
		if(fnNvl(goodsCouponList) != '') {
			for (let i = 0; i < goodsCouponList.length; i++) {
				goodsCouponPrice += goodsCouponList[i].discountPrice;
				tplObj = $(tpl);
				tplObj.find(".claimMgm__goodsCouponName").text(goodsCouponList[i].pmCouponNm).attr("id", "goodsCouponName_" + i); 	//상품 쿠폰 > 쿠폰명
				tplObj.find(".claimMgm__goodsDiscountDetail").text(goodsCouponList[i].discountDetl).attr("id", "goodsDiscountDetail_" + i); 	//상품 쿠폰 > 할인상세
				// hidden
				tplObj.find("input[name=odClaimDetlDiscountIdGoods]").text(goodsCouponList[i].odClaimDetlDiscountId).attr("id", "odClaimDetlDiscountIdGoods_" + i);
				tplObj.find("input[name=odClaimDetlIdGoods]").text(goodsCouponList[i].odClaimDetlId).attr("id", "odClaimDetlIdGoods_" + i);
				tplObj.find("input[name=discountTpGoods]").text(goodsCouponList[i].discountTp).attr("id", "discountTpGoods_" + i);
				tplObj.find("input[name=pmCouponIssueIdGoods]").text(goodsCouponList[i].pmCouponIssueId).attr("id", "pmCouponIssueIdGoods_" + i);
				tplObj.find("input[name=discountPriceGoods]").text(goodsCouponList[i].discountPrice).attr("id", "discountPriceGoods_" + i);
				tplObj.find("input[name=psEmplDiscMasterIdGoods]").text(goodsCouponList[i].psEmplDiscMasterId).attr("id", "psEmplDiscMasterIdGoods_" + i);
				tplObj.find("input[name=urBrandIdGoods]").text(goodsCouponList[i].urBrandId).attr("id", "urBrandIdGoods_" + i);
				tplObj.find("input[name=odOrderDetlDiscountIdGoods]").text(goodsCouponList[i].odOrderDetlDiscountId).attr("id", "odOrderDetlDiscountIdGoods_" + i);
				tplObj.find("input[name=odOrderDetlIdGoods]").text(goodsCouponList[i].odOrderDetlId).attr("id", "odOrderDetlIdGoods_" + i);

				$target.append(tplObj);
			}
		}

		//장바구니쿠폰 리스트 template
		const $targetCart = $("#startCartCouponListArea .claimMgm__cartCouponList");
		$targetCart.empty();

		let tplCart = $('#cartCouponListItem').html();
		let tplObjCart = null;
		var cartCouponPrice = 0;
		if(fnNvl(cartCouponList) != '') {
			for (let i = 0; i < cartCouponList.length; i++) {
				cartCouponPrice += cartCouponList[i].discountPrice;
				tplObjCart = $(tplCart);
				tplObjCart.find(".claimMgm__cartCouponName").text(cartCouponList[i].pmCouponNm).attr("id", "cartCouponName_" + i); 	//장바구니 쿠폰 > 쿠폰명
				tplObjCart.find(".claimMgm__cartDiscountDetail").text(cartCouponList[i].discountDetl).attr("id", "cartDiscountDetail_" + i); 	//장바구니 쿠폰 > 할인상세
				// hidden
				tplObjCart.find("input[name=odClaimDetlDiscountIdCart]").text(cartCouponList[i].odClaimDetlDiscountId).attr("id", "odClaimDetlDiscountIdCart_" + i);
				tplObjCart.find("input[name=odClaimDetlIdCart]").text(cartCouponList[i].odClaimDetlId).attr("id", "odClaimDetlIdCart_" + i);
				tplObjCart.find("input[name=discountTpCart]").text(cartCouponList[i].discountTp).attr("id", "discountTpCart_" + i);
				tplObjCart.find("input[name=pmCouponIssueIdCart]").text(cartCouponList[i].pmCouponIssueId).attr("id", "pmCouponIssueIdCart_" + i);
				tplObjCart.find("input[name=discountPriceCart]").text(cartCouponList[i].discountPrice).attr("id", "discountPriceCart_" + i);
				tplObjCart.find("input[name=psEmplDiscMasterIdCart]").text(cartCouponList[i].psEmplDiscMasterId).attr("id", "psEmplDiscMasterIdCart_" + i);
				tplObjCart.find("input[name=urBrandIdCart]").text(cartCouponList[i].urBrandId).attr("id", "urBrandIdCart_" + i);
				tplObjCart.find("input[name=odOrderDetlDiscountIdCart]").text(cartCouponList[i].odOrderDetlDiscountId).attr("id", "odOrderDetlDiscountIdCart_" + i);
				tplObjCart.find("input[name=odOrderDetlIdCart]").text(cartCouponList[i].odOrderDetlId).attr("id", "odOrderDetlIdCart_" + i);

				$targetCart.append(tplObjCart);
			}
		}

		// 배송비 쿠폰 정보가 존재할 경우
		if(fnNvl(priceInfo.deliveryCouponList) != '') {
			let tplShipping = $('#shippingCouponListItem').html();
			const $targetShipping = $("#shippingCouponList");
			let tplObjShipping = null;
			$targetShipping.empty();
			for(let i=0; i<priceInfo.deliveryCouponList.length; i++){
				tplObjShipping = $(tplShipping);
				tplObjShipping.find(".claimMgm__shippingCouponName").text(priceInfo.deliveryCouponList[i].pmCouponNm);
				tplObjShipping.find(".claimMgm__shippingDiscountDetail").text(priceInfo.deliveryCouponList[i].pmCouponBenefit);
				$targetShipping.append(tplObjShipping);
			}
		}

		$(".claimMgm__goodsPrice").text(fnNumberWithCommas(priceInfo.goodsPrice)); //주문상품금액(판매가합계)
		//$(".claimMgm__refundType").text(priceInfo.refundType == 'D' ? "원결제 내역" : "무통장입금"); //환불수단 "C" : 무통장입금
		//$(".claimMgm__refundType").text(paymentInfo.payTpNm); //환불수단 "C" : 무통장입금
		/** 2021-03-21 추가 START */
		let tagStr = "&minus;";
		//let goodsCouponPrice = priceInfo.goodsCouponPrice;
		if(priceInfo.goodsCouponPrice == 0) {
			tagStr = "";
		}
		$(".claimMgm__goodsCouponPrice").text(fnNumberWithCommas(priceInfo.goodsCouponPrice)).parent().siblings("span.price-prefix").html(tagStr); 	//상품 쿠폰 (원)
		tagStr = "&minus;";
		//let cartCouponPrice = priceInfo.cartCouponPrice;
		if(priceInfo.cartCouponPrice == 0) {
			tagStr = "";
		}
		$(".claimMgm__cartCouponPrice").text(fnNumberWithCommas(priceInfo.cartCouponPrice)).parent().siblings("span.price-prefix").html(tagStr); //장바구니 쿠폰 (원)
		//$("#shippingPrice").val(fnNumberWithCommas(priceInfo.shippingPrice)); //배송비
		tagStr = "&plus;";
		var shippingPrice = priceInfo.shippingPrice;
		if(shippingPrice < 0) {
			tagStr = "&minus;";
			shippingPrice = shippingPrice * -1;
		}
		else if(shippingPrice == 0) {
			tagStr = "";
		}
		if(fnNvl(priceInfo.addPaymentShippingPrice) != '') {
			tagStr = "&minus;";
			shippingPrice = priceInfo.addPaymentShippingPrice;

			var paidShippingPrice = priceInfo.addPaymentShippingPrice;
			priceInfo.remainPaymentPrice -= paidShippingPrice;
			if(priceInfo.remainPaymentPrice < 0) {
				paidShippingPrice = Math.abs(paidShippingPrice);
				priceInfo.remainPaymentPrice = 0;
			}
			else {
				paidShippingPrice = 0;
			}
			if(paidShippingPrice > 0) {
				priceInfo.remainPointPrice -= paidShippingPrice;
			}
			if(priceInfo.remainPointPrice < 0) {
				priceInfo.remainPointPrice = 0;
			}
		}
		$("#shippingPrice").attr("data-val", priceInfo.shippingPrice).text(fnNumberWithCommas(shippingPrice)); //배송비
		$("#shippingPrice").siblings("span.shipping").html(tagStr);

		/** 2021-06-15 임직원지원금 추가 : 임직원지원금이 > 0 임직원지원금 영역 노출 (클레임수량 변경 시 임직원 지원금 변경)*/
		var employeeDiscountArr = new Array();
		for(let i=0; i < orderGoodList.length; i++) {
			employeeDiscountArr.push(orderGoodList[i].employeeDiscountPrice);
		}
		if(employeeDiscountArr.some(elem => elem > 0)) {
			$("#employeeDiscountDiv").show();
			$(".claimMgm__employeeDiscountPrice").text(fnNumberWithCommas(priceInfo.directPrice)).attr('data-orgval', priceInfo.directPrice); // 임직원 지원금
		} else {
			$("#employeeDiscountDiv").hide();
		}

		/** 2021-03-21 추가 END */
		//$("#shippingPrice").text(fnNumberWithCommas(priceInfo.shippingPrice)); //배송비
		//$(".claimMgm__refundPrice").text("1000"); //환불금액
		$(".claimMgm__refundPrice").text(fnNumberWithCommas(priceInfo.refundPrice)); //환불금액
		$(".claimMgm__remainPaymentPrice").text(fnNumberWithCommas(priceInfo.remainPaymentPrice)); //잔여 결재 금액
		$(".claimMgm__refundPointPrice").text(fnNumberWithCommas(priceInfo.refundPointPrice)); //환불 적립금
		$(".claimMgm__remainPointPrice").text(fnNumberWithCommas(priceInfo.remainPointPrice)); //잔여 적립금
		//$(".claimMgm__refundTotalPrice").text("1000"); //환불 금액 합계
		let addPaymentShippingPrice = priceInfo.addPaymentShippingPrice;
		let addPaymentShippingPriceYn = priceInfo.addPaymentShippingPriceYn;
		if(addPaymentShippingPriceYn == 'Y') {
			$(".claimMgm__refundTotalPrice").text("-" + fnNumberWithCommas(addPaymentShippingPrice)); //환불 금액 합계
		}
		else {
			$(".claimMgm__refundTotalPrice").text(fnNumberWithCommas(priceInfo.refundTotalPrice)); //환불 금액 합계
		}

		//반품,취소일시 > 추가결제 발생할 경우
		if((paramData.orderStatusCd == 'RI' || paramData.orderStatusCd == 'RF')){ // 현 주문상태가 반품승인, 반품보류일 때
			$('#addPaymentView').hide(); //추가결제 버튼 div hide
		} else{
			if(addPaymentShippingPriceYn == 'Y') {// 환불금액합계가 마이너스일 때
				$('#addPaymentView').show(); //추가결제 버튼 div show
			}else {
				$('#addPaymentView').hide();
			}
		}
	},
	//--> 배송정보 (녹즙)initGreenJuiceShippingList
	initGreenJuiceShippingList : function (orderGoodsList){

		$("#tbodyShippingInfoArea").empty();

		if(fnNvl(orderGoodsList) == "") {
			$("#tbodyShippingInfoArea").html($("#returnsDeliveryInfoNoneTmpl").html());
			return false;
		}

		let shippingInfoTplObj = $("#divShippingInfoArea").clone();

		//체크박스 좌(1), 우(2)
		// let chkHtml1 = '<input type="checkbox" name="select1" class="shippingInfo_chk_1" />';
		// let chkHtml2 = '<input type="checkbox" name="select2" class="shippingInfo_chk_2" />';

		$("input[name=selectAll1]").click(function(){
			if($("input[name=selectAll1]").prop("checked")) {
				$("input[name=select1]").prop("checked",true);
				} else {
				$("input[name=select1]").prop("checked",false);
				}
		});

		$("input[name=selectAll2]").click(function(){
			if($("input[name=selectAll2]").prop("checked")) {
				$("input[name=select2]").prop("checked",true);
			} else {
				$("input[name=select2]").prop("checked",false);
			}
		});

		let returnsDeliveryTmpl = $($("#returnsDeliveryInfoTmpl").html());
		var returnsDeliveryHtml = "";
		for (var i = 0; i < orderGoodsList.length; i++) {
			let num = (i % 2 + 1);
			let checkBox = '<input type="checkbox" name="select' + num + '" class="shippingInfo_chk_' + num + '" data-index="' + i +
																																	'" data-odorderdetlid="' + orderGoodsList[i].odOrderDetlId +					// 주문상세PK
																																	'" data-odorderdetldailyschid="' + orderGoodsList[i].odOrderDetlDailySchId +	// 주문상세일일스케쥴PK
																																	'" data-urwarehouseid="' + orderGoodsList[i].urWarehouseId +					// 출고처PK
																																	'" data-ilgoodsid="' + orderGoodsList[i].ilGoodsId +							// 상품PK
																																	'" data-goodsdeliverytype="' + orderGoodsList[i].goodsDeliveryType +			// 배송타입
																																	'" data-goodscycletp="' + orderGoodsList[i].goodsCycleTp +						// 배송주기
																																	'" data-weekdaycd="' + orderGoodsList[i].weekDayCd +							// 요일코드
																																	'" data-odorderdetlseq="' + orderGoodsList[i].odOrderDetlSeq +					// 주문상세순번
																																	'"/>';
			let goodsClaimInfo = '<input id="goodsNmCnt' + i + '" name="goodsNmCnt1" className="fb__custom__select left-input" style="width : 50px;" type="text">';
			returnsDeliveryTmpl.find(".shippingInfo_chk_" + num).html(checkBox);
			returnsDeliveryTmpl.find(".shippingInfo_date_" + num).html('<span class="deliveryDate" data-deliverydt="' + orderGoodsList[i].deliveryDt + '">' + orderGoodsList[i].deliveryDtNm + " (" + orderGoodsList[i].weekDayNm + ")</span>");
			returnsDeliveryTmpl.find(".shippingInfo_goodsNm_cnt_" + num).html('<span class="goodsNm">' + orderGoodsList[i].goodsNm + "</span> / 수량 " + goodsClaimInfo);
			if (i % 2 == 1) {
				returnsDeliveryHtml += returnsDeliveryTmpl.html();
				returnsDeliveryTmpl = $($("#returnsDeliveryInfoTmpl").html());
			}
		}
		if (orderGoodsList.length % 2 > 0) {
			returnsDeliveryHtml += returnsDeliveryTmpl.html();
		}
		$("#tbodyShippingInfoArea").html(returnsDeliveryHtml);

		for (var i = 0; i < orderGoodsList.length; i++) {
			var codeArr = new Array();
			for (var j = 0; j < orderGoodsList[i].claimCnt; j++) {
				codeArr.push({
					"CODE": j + 1,
					"NAME": j + 1
				});
			}
			fnKendoDropDownList({
				id: 'goodsNmCnt' + i,
				data: codeArr,
				value: orderGoodsList[i].claimCnt,
				dataBound: function () {
					$("#goodsNmCnt" + i).data("kendoDropDownList").enable(false);
				}
			});
		}
	},
	//--> 재배송정보 (재배송, 녹즙)
	initReShippingList : function (goodSchList, arrivalDtList){

		$("#tbodyReShippingArea").empty();

		let reShippingInfoTplObj = $($("#divReShippingArea").html());

		for(var i=0; i<goodSchList.length; i++) {

			var deliveryDt = goodSchList[i].deliveryDt.replace(/[^0-9]{0,}/ig, '');
			deliveryDt = deliveryDt.substr(0, 4) + "-" + deliveryDt.substr(4, 2) + "-" + deliveryDt.substr(6);

			let reDateHtml = "도착예정일 : ";
			reDateHtml += '<div class="datepicker-wrapper">' +
				'<input class="shippingListDeliveryDtPicker" id="shippingListDeliveryDtPicker__' + i + '" type="text" class="fb__datePicker--input" autocomplete="off" ' +
															'data-odorderdetldailyschid="' + goodSchList[i].odOrderDetlDailySchId + '" ' +
															'data-odorderdetlid="' + goodSchList[i].odOrderDetlId + '" ' +
															'data-claimcnt="' + goodSchList[i].claimCnt + '" ' +
															'data-validate-title="도착 예정일 (리스트)" ' +
															'data-defaultdate="' + deliveryDt + '" ' +
															'data-odorderdetlseq="' + goodSchList[i].odOrderDetlSeq + '" ' +
															'value="' + deliveryDt + '">' +
				'</div>';

			// for(let i=0; i<rows.length; i++){

			reShippingInfoTplObj.find(".reshipping_date").html(goodSchList[i].deliveryDtNm);
			reShippingInfoTplObj.find(".reshipping_goodsNm_cnt").html(goodSchList[i].goodsNm + '/' + goodSchList[i].claimCnt);
			reShippingInfoTplObj.find(".reshipping_reDate").html(reDateHtml);
			//SET END
			$("#tbodyReShippingArea").append(reShippingInfoTplObj.html());
		}

		$("#tbodyReShippingArea input[type=text]").each(function(index) {
			let odOrderDetlId = $(this).data('odorderdetlid');
			for(var i=0; i<arrivalDtList.length; i++) {
				if(odOrderDetlId == arrivalDtList[i].odOrderDetlId) {
					pickerDate1(arrivalDtList[i].dateList, "shippingListDeliveryDtPicker__" + index); // picker [추후수정예정]
				}
			}
		});
		//let scheduleDelvDateList = data.scheduleDelvDateList == null ? paramData.deliveryDt : data.scheduleDelvDateList;
		// let scheduleDelvDateList =  ["2021-05-07", "2021-05-12", "2021-05-14", "2021-05-21", "2021-05-26", "2021-05-28", "2021-05-31"]; // [추후수정예정] 재배송일자-도착예정일 리스트
		//
		// pickerDate1(scheduleDelvDateList, "shippingListDeliveryDtPicker__0"); // picker [추후수정예정]
		//재배송일자-도착예정일  datePicker
		function pickerDate1(dayOn, picker) {

			shippingListDeliveryDtPicker = new FbDatePicker(picker, {
				dayOff: dayOff,
				dayOn: dayOn,
				beforeShowDay: function(date) {
					if(dayOn != [] && dayOn != undefined){
						if(this.isDayOn(date)){
							return [true, "dayOn"];
						}else{
							return [false, ""];
						}
					}else{
						if( this.isDayOff(date)) {
							return [false, "dayOff", "휴일"];
						} else if( this.isDisabledDate(date) ) {
							return [false, ""];
						} else {
							return [true, ""];
						}
					}
				},
				onChange: function(e) {
					const context = this;
					onChangeDatePicker.call(context, e);
				}
			})
		};


		function onChangeDatePicker(e, type, sId, eId) {
			const self = this;
			let date = e.target.value;
			let $target = $(e.target);
			let sd = sId ? $("#" + sId) : null;
			let ed = eId ? $("#" + eId) : null;

			//변경일 노출일자 설정
			if(type = 'end'){

			}

			if( !fnIsValidDate(date) ) {
				fnKendoMessage({message: "올바른 날짜가 아닙니다."});
				$target.val("");
				return false;
			}

			date = new Date(date);

			if( self.isDayOffOrDisabled(date) ) {
				fnKendoMessage({message: "해당 날짜를 선택하실 수 없습니다."});
				$target.val("");
				return false;
			}

			if( sd && ed ) {
				let startDate = sd.val();
				let endDate = ed.val();
				if( !startDate || !endDate ) return;
				if( fnValidateDatePicker(startDate, endDate) ) return;

				if( type === "start" ) {
					fnKendoMessage({message: "시작일을 확인해주세요."});
				} else {
					fnKendoMessage({message: "종료일을 확인해주세요."});
				}

				$target.val("");
			}
		}

		$('.inputForm__section.goodsInfo').hide();						// 상품정보 영역 숨김
		$('.inputForm__section.shippingInfo').hide();					// 배송정보 영역 숨김
		$('.inputForm__section.toggleSection.reShippingInfo').show();	// 재배송정보 영역 노출
		$('#fnSave').show();											// 변경버튼 노출
		$('#fnNext').hide();											// 다음버튼 숨김
	},
	clear: function() {
		var form = $("#searchForm");
		form.formClear(true);

		$(".date-controller button").attr("fb-btn-active", false);
	},
}

var claimMgmGreenJuiceEventUtil = {
	//클레임 구분
	initClaimType : function(changeFunc, claimStatusCd) {
		$('#refundGoodsInfoDiv').hide();
		// 클레임 상태 코드가 재배송일 경우 라디오 버튼 생성하지 않음
		if(claimStatusCd == "EC") {
			$("#claimType").text("재배송");
			return;
		}
		$('.inputForm__section.shippingInfo').hide(); //배송정보 hide
		fnTagMkRadio({
			id: 'claimType',
			tagId: 'claimType',
			chkVal: 'C', //추후수정예정
			data: [{
				CODE: "C", //추후수정예정
				NAME: "취소",
			},{
				CODE: "R", //추후수정예정
				NAME: "반품",
			}],
			change: function(e) {
				if(typeof(changeFunc) === 'function') {
					changeFunc(e);
				}
				else {
					const value = e.target.value;
					if( value == 'C' ) {
						$('.inputForm__section.shippingInfo').hide(); //배송정보 hide
						$('#refundGoodsInfoDiv').hide();
					} else {
						$('.inputForm__section.shippingInfo').show(); //배송정보 show
						$('#refundGoodsInfoDiv').show();
					}
				}
			}
		});
	},
	//사유
	psClaimMallId : function(paramData, claimReasonData) {
		//신청할 클레임상태 별 사유 목록
		var searchReasonTypeList = new Array();
		const putOrderStatusCd = paramData.putOrderStatusCd;
		if(putOrderStatusCd.charAt(0) == 'C') { //취소
			searchReasonTypeList.push('C');
			searchReasonTypeList.push('A');
		} else if(putOrderStatusCd.charAt(0) == 'R') { //반품
			searchReasonTypeList.push('R');
			searchReasonTypeList.push('A');
		}

		fnAjax({
			url     : "/admin/order/claim/getOrderClaimReasonList",
			params  : {searchReasonTypeList : searchReasonTypeList},
			contentType: "application/json",
			success : function( data ){

				// 취소요청 / 반품요청일 시
				if(paramData.orderStatusCd == 'CA' || paramData.orderStatusCd == 'RA' ||
					paramData.orderStatusCd == 'RI' || paramData.orderStatusCd == 'EC'){
					var reasonArr = new Array();
					for(var i=0; i<data.rows.length; i++) {
						let psClaimMallId = data.rows[i].psClaimMallId;
						data.rows[i].psClaimMallId = psClaimMallId;
						reasonArr.push(data.rows[i]);
					}

					fnKendoDropDownList({
						id         : 'psClaimMallId',
						blank      : '선택해주세요.',
						data       : reasonArr,
						valueField : "psClaimMallId",
						textField  : "reasonMessage"
					});

				} else {
					//그 외
					if(data.rows != undefined && data.rows.length > 0) {
						var reasonArr = new Array();
						for(var i=0; i<data.rows.length; i++) {
							let psClaimMallId = data.rows[i].psClaimMallId + "_" + data.rows[i].lclaimCtgryId + "_" + data.rows[i].mclaimCtgryId + "_" + data.rows[i].sclaimCtgryId + "_" + data.rows[i].psClaimBosId;
							data.rows[i].psClaimMallId = psClaimMallId;
							reasonArr.push(data.rows[i]);
						}

						if(putOrderStatusCd.charAt(0) == 'C' || putOrderStatusCd.charAt(0) == 'R' || putOrderStatusCd.charAt(0) == 'E') {
                            defaultClaimMallId = '';
                        }

						fnKendoDropDownList({
							id         : 'psClaimMallId',
							blank      : '선택해주세요.',
							data       : reasonArr,
							valueField : "psClaimMallId", //숫자 0,1,2...
							textField  : "reasonMessage",
							value        : defaultValuePsClaimMallId,
							dataBound : function() {
								var reasonArr = defaultValuePsClaimMallId.split("_");
								//fnBosClaimReasonList(reasonArr);

								$('#ng-view').on('change', '#psClaimMallId', function() {
									if(fnNvl($("#bosClaimReasonListDiv").css("display")) == "" || $("#bosClaimReasonListDiv").css("display") == "none") {
										return false;
									}
									reasonArr = $(this).val().split("_");
									fnBosClaimReasonList(reasonArr);
								});


							}
						});
					}
				}

				function fnBosClaimReasonList(reasonArr) {
					var selectFlag = (reasonArr.length < 4);
					var orderGoodList = $("#claimMgmPopupOrderGoodsGrid").data("kendoGrid").dataSource.data();

					for (let i = 0; i < orderGoodList.length; i++) {
						// 클레임사유(대)
						var lCtgryList = $(LClaimCtgryDropDownList[i].element).data("kendoDropDownList");
						var mCtgryList = $(MClaimCtgryDropDownList[i].element).data("kendoDropDownList");
						var sCtgryList = $(SClaimCtgryDropDownList[i].element).data("kendoDropDownList");

						if (!selectFlag) {
							lCtgryList.value(reasonArr[1]);
							lCtgryList.trigger("change");
							// 클레임사유(중)
							mCtgryList.value(reasonArr[2]);
							mCtgryList.trigger("change");
							// 클레임사유(소)
							sCtgryList.value(reasonArr[3]);
							setTimeout(function () {
								sCtgryList.trigger("change");
							}, 1000)

						} else {
							lCtgryList.value('');
							lCtgryList.trigger("change");
						}
					}
				}
			},
			isAction : "select"
		});
	},
	//은행명
	bankCodeDropDownList : function(bankcd){
		var bankCodeDropDownList = fnKendoDropDownList({
			id         : 'bankCode',
			url        : "/admin/comn/getCodeList",
			autoBind   : true,
			blank      : '선택',
			value      : bankcd,
			params : {"stCommonCodeMasterCode" : "BANK_CODE", "useYn" :"Y"},
		});
		bankCodeDropDownList.bind('change', function(e){
			//select 때문에 -1처리
			var index = this.select()-1;
			var param = this.dataSource.data()[index];
			$("#bankName").val(param.NAME);
			isValidBankAccount = false;
			$("#validBankAccountDiv").text('');
		});

		$("#accountNumber, #holderName").bind('blur', function(e){
			isValidBankAccount = false;
			$("#validBankAccountDiv").text('');
		});
	},
}

var claimRegisterGreenJuiceFnUtil = {
	//클레임상태, 클레임상태구분 변경
	changeClaimStatus : function (data, paramData) {
		// 재배송일 경우
		if(paramData.putOrderStatusCd == 'EC'){
			data.claimStatusCd = 'EC'; //재배송
			data.claimStatusTp = 'CLAIM_STATUS_TP.RETURN_DELIVERY';
		}
		// 재배송이 아닐 경우
		else {
			data.claimStatusCd = 'CC'; //취소완료
			data.claimStatusTp = 'CLAIM_STATUS_TP.CANCEL';
			// 클레임 구분이 반품일 경우
			if(data.claimType == "R") {
				data.claimStatusCd = 'RC';
				data.claimStatusTp = 'CLAIM_STATUS_TP.RETURN';
			}
		}
	},

	//추가결제 실행 시 클레임상태 변경
	addPaymentExecution : function (data, paramData, odAddPaymentReqInfoId, payTp, refundPrice) {
		// 클레임 상태가 취소요청, 취소완료, 반품승인, 반품완료가 아닐 경우 추가결제 불가
		if(paramData.putOrderStatusCd != 'CA' && paramData.putOrderStatusCd != 'CC' && paramData.putOrderStatusCd != 'RI' && paramData.putOrderStatusCd != 'RC') {
			fnKendoMessage({message: "클레임 상태를 확인 해주세요."});
			return false;
		}
		//-> 추가결제 > 비인증결제, 가상계좌발급
		if(data.addPayment != 'directPay'){
			data.odAddPaymentReqInfoId = odAddPaymentReqInfoId;
			// 결제 정보 번호 채번이 되지 않았고, 추가 결제를 해야 함
			if(fnNvl(odAddPaymentReqInfoId) == "" || (odAddPaymentReqInfoId < 1 && refundPrice < 0)) {
				fnKendoMessage({message: "추가 결제를 진행 해주세요."});
				return false;
			}
			// 비인증 결제 일 때
			if(payTp == 'card') {
				//클레임상태가 취소요청, 취소완료인 경우
				if(paramData.putOrderStatusCd == 'CA' || paramData.putOrderStatusCd == 'CC') {
					data.claimStatusCd = 'CC'; //취소완료
				}
				//클레임상태가 반품승인, 반품완료인 경우
				else if(paramData.putOrderStatusCd == 'RI' || paramData.putOrderStatusCd == 'RC') {
					data.claimStatusCd = 'RI'; //반품승인
				}
			}
			// 가상계좌 일 떄
			else {
				if(paramData.putOrderStatusCd == 'CA' || paramData.putOrderStatusCd == 'CC') {
					data.claimStatusCd = 'CA'; //취소요청
				}
				else if(paramData.putOrderStatusCd == 'RI' || paramData.putOrderStatusCd == 'RC') {
					data.claimStatusCd = 'RA'; //반품요청
				}
			}
			//-> 추가결제 > 직접결제
		} else if (data.addPayment == 'directPay') {
			//고객이 직접 추가결제 진행
			//직접결제 선택 후 변경 시 취소요청으로 변경
			//고객이 추가결제 완료 시 취소완료, 자동환불대기로 상태변경
			if(paramData.putOrderStatusCd == 'CA' || paramData.putOrderStatusCd == 'CC') {
				data.claimStatusCd = 'CA'; //취소요청
			}
			else if(paramData.putOrderStatusCd == 'RI' || paramData.putOrderStatusCd == 'RC'){
				data.claimStatusCd = 'RA' // 반품요청
			}
		}
		return true;
	}

}
var claimInfoGreenJuiceRegisterUtil = {
	removeComma : function (str){
		if(fnNvl(str) == '') { return 0; }
		n = parseInt(str.replace(/,/g,""));
		return n;
	},
	isEmptyNum : function ( val ){
		if(val == null || typeof(val) == "undefined" || $.trim(val) == "") {
			return 0;
		}
		return val;
	},
	isEmptyStrjg  : function ( val ){
		if(val == null || typeof(val) == "undefined" || $.trim(val) == "") {
			return 0;
		}
		return val;
	},
	//--> OD_CLAIM (MASTER SET)
	odClaimSetting : function(data, paramData, partCancelYn) {
		data.odid = paramData.odid; 												//주문번호 ODID
		data.odOrderId = paramData.odOrderId; 										//주문pk
		data.odClaimId = paramData.odClaimId == null ? 0 : paramData.odClaimId; 	//주문클레임pk
		//추가 직접결제 필드 추가예정)
		data.claimReasonCd =$('#psClaimMallId').val().split("_")[0]; 								//클레임사유코드
		data.claimReasonMsg = $('#claimReasonMsg').val();							//클레임상세사유
		data.targetTp = data.targetTp == '구매자 귀책' ? 'B' : 'S'; 					//귀책
		data.returnsYn = data.withDraw;								//반품회수여부
		//data.refundType = $(".claimMgm__refundType").text() == '원결제 내역' ? 'D' : 'C';				//D: 원결제 내역 C : 무통장입금 (환불정보에서 환불수단 값)
		data.refundType = 'D';
		// 부분취소 불가일 경우 무통장 입금
		if(partCancelYn == 'N') {
			data.refundType = 'C';
		}
		data.goodsNm = $("#claimMgmPopupOrderGoodsGrid").data("kendoGrid")._data[0].goodsNm;
	},
	//--> BOS클레임 사유 SET
	odClaimCtgrySetting: function(data, paramData) {
		let goodsCheckFlag = $("#eachGoodsReasonSelect").is(":checked");
		var sellerFaultFlag = false;
		for(var i=0; i<data.goodsInfoList.length; i++) {
			var odOrderDetlId = data.goodsInfoList[i].odOrderDetlId;
			var liItem = null;
			// 상품별 BOS클레임 사유 체크 했을 경우
			if(goodsCheckFlag) {
				liItem = $("#bosClaimReasonList li[data-odorderdetlid=" + odOrderDetlId + "]");
			}
			// 상품별 BOS클레임 사유 체크하지 않았을 경우
			else {
				liItem = $("#bosClaimReasonList li:eq(0)");
			}
			data.goodsInfoList[i].psClaimBosId = liItem.find('span.psClaimBosId').text();
			data.goodsInfoList[i].psClaimBosSupplyId = liItem.find('span.psClaimBosSupplyId').text();
			data.goodsInfoList[i].bosClaimLargeId = liItem.find("input[name=lclaimCtgryId]").val();
			data.goodsInfoList[i].bosClaimMiddleId = liItem.find("input[name=mclaimCtgryId]").val();
			data.goodsInfoList[i].bosClaimSmallId = liItem.find("input[name=sclaimCtgryId]").val();
			if(liItem.find("span.targetType").text() == "판매자 귀책") {
				sellerFaultFlag = true;
			}
		}
		data.targetTp = "B";
		if(sellerFaultFlag) {
			data.targetTp = "S";
		}
		data.psClaimMallId = $("#psClaimMallId").val().split("_")[0];
	},
	//--> 결제-환불정보
	paymentInfo : function(data) {
		let arrayPayment = [
			$('.claimMgm__goodsPrice').text(),
			$('.claimMgm__goodsCouponPrice').text(),
			$('.claimMgm__cartCouponPrice').text(),
			$('#shippingPrice').text(),
			$('.claimMgm__refundPrice').text(),
			$('.claimMgm__refundPointPrice').text(),
			$('.claimMgm__remainPaymentPrice').text()
		];

		let arrayPaymentOfNumber = arrayPayment.map(item => parseInt(item.replace(/,/g,""))); // String to Int

		data.goodsPrice = arrayPaymentOfNumber[0]; 				//상품금액	 (값이 없으면 0으로 셋팅 해주세요)
		data.goodsCouponPrice =  arrayPaymentOfNumber[1];		//상품쿠폰금액 (값이 없으면 0으로 셋팅 해주세요)
		data.cartCouponPrice = arrayPaymentOfNumber[2];			//장바구니쿠폰금액 (값이 없으면 0으로 셋팅 해주세요)
		data.shippingPrice = arrayPaymentOfNumber[3];			//배송비	 (값이 없으면 0으로 셋팅 해주세요)

		if(data.putOrderStatusCd == 'CS') {
			//결제금액환불일 때
			if (data.odStatusCS == 'paymentPriceRefund') {
				data.refundPrice = $("#refundPriceCS").val().replace(",", "");	//환불금액	 (값이 없으면 0으로 셋팅 해주세요)
				data.refundPointPrice = 0;						//환불적립금(값이 없으면 0으로 셋팅 해주세요)
			}
			//적립금환불일때
			else {
				data.refundPrice = 0;								//환불금액	 (값이 없으면 0으로 셋팅 해주세요)
				data.refundPointPrice = $("#refundPriceCS").val().replace(",", "");	//환불적립금(값이 없으면 0으로 셋팅 해주세요)
			}
		} else {
			data.refundPrice = arrayPaymentOfNumber[4];				//환불금액	 (값이 없으면 0으로 셋팅 해주세요)
			data.refundPointPrice = arrayPaymentOfNumber[5];		//환불적립금(값이 없으면 0으로 셋팅 해주세요)
		}
		data.remaindPrice = arrayPaymentOfNumber[6];			//환불잔여금액	 (값이 없으면 0으로 셋팅 해주세요)
		data.odPaymentMasterId ='0'; 							//추가결제 결제마스터 PK 	 (값이 없으면 0으로 셋팅 해주세요)
	},

	//--> 주문회원정보 및 택배사 반품접수 관련 필요변수
	userInfo : function(data, paramData) {
		data.urUserId = paramData.urUserId;				//회원ID
		data.guestCi = paramData.guestCi;				//비회원CI
		data.urEmployeeCd = '';							//임직원사번 : UR_EMPLOYEE.UR_EMPLOYEE_CD
	},

	//-->주문클레임 환불계좌관리[OD_CLAIM_ACCOUNT]
	claimAccountInfo : function(data) {
		data.odClaimAccountId = data.odClaimAccountId == null ? '0' : data.odClaimAccountId; //주문클레임 환불계좌 PK (값이 없으면 0으로 셋팅 해주세요)
		data.bankCd = data.bankCode; 					//은행코드'BANK_CODE.WOORI'
		data.accountHolder = data.holderName; 			//예금주
		data.accountNumber = data.accountNumber; 		//계좌번호 '10021112345667'
	},
	//-->주문상세 [OD_CLAIM_DETL]
	goodsInfoList : function(data, paramData) {

		var goodsInfoList = new Array();
		var claimGoodsDatas = $("#claimMgmPopupOrderGoodsGrid").data("kendoGrid").dataSource.data();

		for(var i = 0 ; i < claimGoodsDatas.length; i++){
			let goodOrgData = {...claimGoodsDatas[i]};
			if(goodOrgData.claimCnt == 0) {
				continue;
			}
			//let gridData = {...claimGoodsDatas[i]};
			goodOrgData.odClaimDetlId = goodOrgData.odClaimDetlId == null ? '0' : goodOrgData.odClaimDetlId; //주문클레임 상세 PK
			//추가 >
			goodOrgData.orderStatusCd = paramData.orderStatusCd; 	//주문클레임 상세 PK
			goodOrgData.claimGoodsYn ='Y';
			goodOrgData.psClaimBosSupplyId = $('#psClaimBosSupplyId_'+i).text();
			goodOrgData.psClaimBosId = $("#psClaimBosId_"+i).text();
			goodOrgData.bosClaimLargeId =  claimInfoGreenJuiceRegisterUtil.removeComma($("#lclaimCtgryId_"+i).val()); //클레임사유(대)
			goodOrgData.bosClaimMiddleId = claimInfoGreenJuiceRegisterUtil.removeComma($("#mclaimCtgryId_"+i).val()); //클레임사유(중)
			goodOrgData.bosClaimSmallId = claimInfoGreenJuiceRegisterUtil.removeComma($("#sclaimCtgryId_"+i).val()); //클레임사유(귀책처)
			goodOrgData.ilGoodsShippingTemplateId = goodOrgData.ilShippingTmplId;				//상품명 : IL_GOODS.GOODS_NM

			// 상품 상세 클레임수량 정보가 존재할 경우
			if(fnNvl(data.goodDetlCntList) != "") {
				for(var j=0; j<data.goodDetlCntList.length; j++) {
					if(goodOrgData.odOrderDetlId == data.goodDetlCntList[j].odOrderDetlId) {
						goodOrgData.claimCnt = data.goodDetlCntList[j].claimCnt;
					}
				}
			}

			goodsInfoList.push(goodOrgData);
		}
		return goodsInfoList;
	},

	//-->상품쿠폰목록정보
	goodsCouponInfoList : function(data, paramData) {
		var goodsCouponInfoList = new Array();
		for(var i = 0 ; i < $('input[name=odOrderDetlIdGoods]').length ; i++){
			let goodsCouponOrgData = {};
			goodsCouponOrgData.odClaimDetlDiscountId = data.odClaimDetlDiscountId == null ? '0' : $('#odClaimDetlDiscountIdGoods_'+i).text(); //주문클레임 상세 할인 PK
			goodsCouponOrgData.odOrderId = paramData.odOrderId;																//주문 PK
			goodsCouponOrgData.odClaimId = paramData.odClaimId == null ? '0' : paramData.odClaimId;	//주문 클레임 PK
			goodsCouponOrgData.odClaimDetlId = paramData.odClaimDetlId == null ? '0' :  $('#odClaimDetlIdGoods_'+i).text();	//주문클레임 상세 PK
			goodsCouponOrgData.discountTp = $('#discountTpGoods_'+i).text();													//상품할인 유형 공통코드(GOODS_DISCOUNT_TP - PRIORITY:우선할인, ERP_EVENT:올가할인, IMMEDIATE:즉시할인)
			goodsCouponOrgData.pmCouponIssueId = $('#pmCouponIssueIdGoods_'+i).text();										//쿠폰 PK : PM_COUPON_ISSUE.PM_COUPON_ISSUE_ID
			goodsCouponOrgData.pmCouponNm = $('#goodsCouponName_'+i).text();												//쿠폰명
			goodsCouponOrgData.discountPrice = $('#discountPriceGoods_'+i).text();											//할인금액
			goodsCouponOrgData.psEmplDiscMasterId = $('#psEmplDiscMasterIdGoods_'+i).text();									//임직원 혜택그룹 : PS_EMPL_DISC_MASTER.PS_EMPL_DISC_MASTER_ID
			goodsCouponOrgData.urBrandId = $('#urBrandIdGoods_'+i).text();													//임직원 혜택 표준 브랜드
			goodsCouponOrgData.odOrderDetlDiscountId = $('#odClaimDetlDiscountIdGoods_'+i).text();							//주문상세 할인PK
			goodsCouponOrgData.odOrderDetlId = $('#odOrderDetlIdGoods_'+i).text();											//주문상세 PK

			goodsCouponInfoList.push(goodsCouponOrgData);
		}
		return goodsCouponInfoList;
	},

	//-->장바구니쿠폰목록정보
	cartCouponInfoList : function(data, paramData) {
		var cartCouponInfoList = new Array();
		for(var i = 0 ; i < $('input[name=odOrderDetlIdCart]').length ; i++){
			let cartCouponOrgData = {};
			cartCouponOrgData.odClaimDetlDiscountId = data.odClaimDetlDiscountId == null ? '0' : $('#odClaimDetlDiscountIdCart_'+i).text(); //주문클레임 상세 할인 PK
			cartCouponOrgData.odOrderId = paramData.odOrderId;																//주문 PK
			cartCouponOrgData.odClaimId = paramData.odClaimId == null ? '0' : paramData.odClaimId;	//주문 클레임 PK
			cartCouponOrgData.odClaimDetlId = paramData.odClaimDetlId == null ? '0' :  $('#odClaimDetlIdCart_'+i).text();	//주문클레임 상세 PK
			cartCouponOrgData.discountTp = $('#discountTpCart_'+i).text();													//상품할인 유형 공통코드(GOODS_DISCOUNT_TP - PRIORITY:우선할인, ERP_EVENT:올가할인, IMMEDIATE:즉시할인)
			cartCouponOrgData.pmCouponIssueId = $('#pmCouponIssueIdCart_'+i).text();										//쿠폰 PK : PM_COUPON_ISSUE.PM_COUPON_ISSUE_ID
			cartCouponOrgData.pmCouponNm = $('#goodsCouponName_'+i).text();												//쿠폰명
			cartCouponOrgData.discountPrice = $('#discountPriceCart_'+i).text();											//할인금액
			cartCouponOrgData.psEmplDiscMasterId = $('#psEmplDiscMasterIdCart_'+i).text();									//임직원 혜택그룹 : PS_EMPL_DISC_MASTER.PS_EMPL_DISC_MASTER_ID
			cartCouponOrgData.urBrandId = $('#urBrandIdCart_'+i).text();													//임직원 혜택 표준 브랜드
			cartCouponOrgData.odOrderDetlDiscountId = $('#odClaimDetlDiscountIdCart_'+i).text();							//주문상세 할인PK
			cartCouponOrgData.odOrderDetlId = $('#odOrderDetlIdCart_'+i).text();											//주문상세 PK

			cartCouponInfoList.push(cartCouponOrgData);
		}
		return cartCouponInfoList;
	},
}