/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 클레임 상세팝업
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.08		최윤지          최초생성
 * **/
var datePicker = null;
var dayOn = [];
var deliveryList = [];
var dawnDeliveryList = [];
var outmallOrderYn = "N";

//'취소 or 반품' 신청 시 클레임정보 > [사유(쇼핑몰 클레임 사유)]가 '기타(고객센터)'로 기본값 설정
//"쇼핑몰클레임사유_bos클레임사유(대)_bos클레임사유(중)_bos클레임사유(귀책처)"
var defaultValuePsClaimMallId = "0_1_9_23_41";
var claimMgmViewUtil = {
	//클레임 주문상태에 따라 show(), hide()
	claimShow : function (putOrderStatusCd) {
		// -> 재배송
		if(putOrderStatusCd == 'EC'){
			$('.inputForm__section.releaseInfo').show();//출고정보
			$('.inputForm__section.payInfo').hide(); //결제정보
			$('.inputForm__section.refundInfo').hide(); //환불정보
			$('.inputForm__section.toggleSection.addressInfo1').hide(); //반품수거주소정보
			$('.inputForm__section.toggleSection.addressInfo2').hide(); //반품회수주소정보
			$('.inputForm__section.toggleSection.shipInfo').hide(); //배송정보

		// -> 취소요청 OR 취소완료 OR 반품완료 OR 배송완료 OR CS환불
		}else if(putOrderStatusCd == 'CA' || putOrderStatusCd == 'CC' || putOrderStatusCd == 'RF' ||
				 putOrderStatusCd == 'DC' || putOrderStatusCd == 'CS' || putOrderStatusCd == 'RC'){
			$('.inputForm__section.toggleSection.addressInfo1').hide(); //반품수거주소정보
			$('.inputForm__section.toggleSection.addressInfo2').hide(); //반품회수주소정보
			$('.inputForm__section.toggleSection.shipInfo').hide(); //배송정보

		// -> 반품요청 OR 반품승인 OR 반품완료 OR 반품보류
		}else if(putOrderStatusCd == 'RA' || putOrderStatusCd == 'RI'){
			$('.inputForm__section.toggleSection.shipInfo').hide(); //배송정보

		} else {
			console.log('추가예정...');
			$('.inputForm__section.toggleSection.addressInfo1').hide(); //반품수거주소정보
			$('.inputForm__section.toggleSection.addressInfo2').hide(); //반품회수주소정보
			$('.inputForm__section.toggleSection.shipInfo').hide(); //배송정보
		}
	},
	claimPriorityUndeliveredShow : function (orderStatusCd, putOrderStatusCd) {
		$('#divPriorityUndelivered').hide();
		$("input:checkbox[name='priorityUndelivered']").prop('checked', false);

		// 취소완료 일때
		if(orderStatusCd == 'CA' && putOrderStatusCd == "CC"){
			$('#divPriorityUndelivered').show();
			$("input:checkbox[name='priorityUndelivered']").prop('checked', true);
		}
		// 반품완료 또는 재배송일떄
		else if(putOrderStatusCd == "RC" || putOrderStatusCd == "EC"){
			$('#divPriorityUndelivered').show();
		}
	}
};

var claimMgmFunctionUtil = {
	//--> 클레임정보
	initClaimInfo: function(customerReasonInfo, orderGoodList, paramData) {
		// -> 재배송
		if(paramData.putOrderStatusCd == 'EC') {
			$('#withDrawDiv').hide(); //회수여부 div
			$('#returnImgArea').hide(); //반품이미지 div
		// -> 취소요청
		}else if(paramData.putOrderStatusCd == 'CA'){
			$('#withDrawDiv').hide(); //회수여부 div
			$('#reShippingTypeDiv').hide(); //재배송구분
			$('#returnImgArea').hide(); //반품이미지 div
		// -> 반품완료
		}else if(paramData.putOrderStatusCd == 'RC'){
			$('#reShippingTypeDiv').hide(); //재배송구분 div
		// -> CS환불
		}else if(paramData.putOrderStatusCd == 'CS'){
			$('#withDrawDiv').hide(); //회수여부 div
			$('#reShippingTypeDiv').hide(); //재배송구분 div
			$('#bosClaimReasonListDiv').show(); //BOS클레임사유 div
			$('#returnImgArea').hide(); //반품이미지 div
		// -> 취소완료
		}else if(paramData.putOrderStatusCd == 'CC'){
			$('#withDrawDiv').hide(); //회수여부 div
			$('#reShippingTypeDiv').hide(); //재배송구분 div
			$('#returnImgArea').hide(); //반품이미지 div
		// -> 취소요청 (현 주문상태)
		}else if(paramData.orderStatusCd == 'CA'){
			$('#withDrawDiv').hide(); //회수여부 div
			$('#reShippingTypeDiv').hide(); //재배송구분 div
			$('#returnImgArea').hide(); //반품이미지 div
		// -> 반품승인 (현 주문상태)
		}else if(paramData.putOrderStatusCd == 'RI'){
			$('#reShippingTypeDiv').hide(); //재배송구분 div
		// -> 반품요청 (현 주문상태)
		}else if(paramData.orderStatusCd == 'RA'){
			$('#reShippingTypeDiv').hide(); //재배송구분 div
		// -> 반품보류 (현 주문상태)
		}else if(paramData.orderStatusCd == 'RF'){
			$('#withDrawDiv').hide(); //회수여부 div
			$('#reShippingTypeDiv').hide(); //재배송구분 div
			$('#reasonDiv').hide(); //사유 div
			$('#reasonDetlDiv').hide(); //상세사유 div
			$('#bosClaimReasonListDiv').hide(); //BOS클레임사유 div
		}else{
		}

		//사유-상세사유 (취소요청 or 반품요청 or 반품승인)
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
			$('#cancelRejectDiv').hide();
			//취소거부구분 setting
			$("#withDrawDiv").hide(); //회수여부 hide
			$("#reShippingTypeDiv").hide(); //재배송 구분
		} else if (paramData.orderStatusCd == 'RA'){ //반품요청이면 주문상태 라디오버튼 활성화 (반품완료, 반품승인, 취소거부)
			$(".claimMgm__odStatusChange").html('<span id="odStatusRA" name="" class="radios-wrapper fb__custom__radio"></span>');
			claimOrderStatusTypeUtil.initOdStatusRAType();
			$('#odStatusRA_1').parents().eq(0).hide(); //반품승인 라디오버튼 hide
			$("#cancelRejectDiv").hide(); //취소거부 구분 hide
			$("#reShippingTypeDiv").hide(); //재배송 구분 hide

		}else if (paramData.orderStatusCd == 'RF'){ //반품보류이면 주문상태 라디오버튼 활성화 (반품완료, 배송완료)
			$(".claimMgm__odStatusChange").html('<span id="odStatusRF" name="" class="radios-wrapper fb__custom__radio"></span>');
			claimOrderStatusTypeUtil.initOdStatusRFType();
			$("#cancelRejectDiv").hide(); //취소거부 구분 hide
			$("#reShippingTypeDiv").hide(); //재배송 구분 hide

		}else if (paramData.putOrderStatusCd == 'DI' ){ //배송중이면 반품승인, 반품완료
			$(".claimMgm__odStatusChange").text(uniqueOrderStatusChange+" ➜ "+paramData.putOrderStatus);
			$("#cancelRejectDiv").hide(); //취소거부 구분 hide
			$("#reShippingTypeDiv").hide(); //재배송 구분 hide

		}else if (paramData.orderStatusCd == 'RI') { //반품승인이면 반품완료, (반품보류 : specOut)
			$(".claimMgm__odStatusChange").text(paramData.orderStatus+" ➜ "+paramData.putOrderStatus);
			// $('#withDraw_0').attr('checked', 'unchecked'); //회수여부 > 회수 체크 default
			$('#withDraw_0').prop('disabled', true); //회수여부 > 회수 체크 default
			// $('#withDraw_1').attr('checked', 'checked');
			$("#cancelRejectDiv").hide(); //취소거부 구분 hide
			$("#reShippingTypeDiv").hide(); //재배송 구분 hide
		}else {
			$(".claimMgm__odStatusChange").text(uniqueOrderStatusChange+" ➜ "+paramData.putOrderStatus);
			$("#cancelRejectDiv").hide(); //취소거부 구분 hide
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
					var lCtgry = $(LClaimCtgryDropDownList[0].element).data("kendoDropDownList");
					var mCtgry = $(MClaimCtgryDropDownList[0].element).data("kendoDropDownList");
					var slCtgry = $(SClaimCtgryDropDownList[0].element).data("kendoDropDownList");

					for (let i = 0; i < orderGoodList.length; i++) {

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
	//--> 클레임정보 [bos클레임사유 only]
	initClaimInfoForBosClaimReason: function(orderGoodList, paramData) {

		//사유-상세사유
		$('#claimReasonMsg').val(orderGoodList[0].claimReasonMsg).attr('disabled', true);
		$("#psClaimMallId").data("kendoDropDownList").value(orderGoodList[0].psClaimMallId);
		$("#psClaimMallId").data("kendoDropDownList").enable( false );

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
  //-->상품정보
  initOrderGoodList : function (){
//		let rowNum = 1;
//		claimMgmPopupOrderGoodsGrid.bind("dataBound", function(e){
//			 $("#claimMgmPopupOrderGoodsGrid tbody > tr").each(function(index, item){
//				 $(item).find(".row-number").html(rowNum);
//				 rowNum++;
//				// $(item).find("")
//			 });
//
//		});

  },
  //-->결제정보
  initPaymentInfo : function(paymentInfo, accountInfo, paramData){
		$("#inputFormRefundBank").hide();
		$("#fnValidBankAccount").show();
		$(".claimMgm__paidPrice").text(fnNumberWithCommas(paymentInfo.paidPrice)); //주문금액
		//$(".claimMgm__payTpNm").text(fnNumberWithCommas(paymentInfo.payTpNm)); //결제수단
		$(".claimMgm__payTpNm").text(paymentInfo.refundPayTpNm); //결제수단
		$(".claimMgm__refundType").text(paymentInfo.payTpNm);
		$(".claimMgm__paymentPrice").text(fnNumberWithCommas(paymentInfo.paymentPrice)); //결제금액
		$(".claimMgm__pointPrice").text(fnNumberWithCommas(paymentInfo.pointPrice)); //적립금
		$(".claimMgm__odPaymentMasterId").text(paymentInfo.odPaymentMasterId); //odPaymentMasterId

	  	//var dropdownlist = null;
		if(paymentInfo.payTp == "PAY_TP.VIRTUAL_BANK") {
			$("#inputFormRefundBank").show();
			claimMgmEventUtil.bankCodeDropDownList(accountInfo.bankCd);
			//dropdownlist = $("#bankCode").data("kendoDropDownList");
			if(fnNvl(accountInfo.accountHolder) != '') {
				$("#holderName").val(accountInfo.accountHolder).data({'accountholder': fnNvl(accountInfo.accountHolder), 'bankCd' : fnNvl(accountInfo.bankCd)});
				$("#accountNumber").val(accountInfo.accountNumber).data('accountnumber', fnNvl(accountInfo.accountNumber));
				//dropdownlist.readonly();
				$("#fnValidBankAccount").hide();
				isValidBankAccount = true;
			}
		}
		if(paymentInfo.payTp == "PAY_TP.COLLECTION") {
			outmallOrderYn = "Y";
		}
  },
	//-->CS결제정보
	initCSPaymentInfo : function(paymentInfo, accountInfo, paramData){
		$("#fnValidBankAccount").show();
		$(".claimMgm__paidPrice").text(fnNumberWithCommas(paymentInfo.paidPrice)); //주문금액
		$(".claimMgm__payTpNm").text(paymentInfo.refundPayTpNm); //결제수단
		$(".claimMgm__refundType").text(paymentInfo.payTpNm);
		$(".claimMgm__paymentPrice").text(fnNumberWithCommas(paymentInfo.paymentPrice)); //결제금액
		$(".claimMgm__pointPrice").text(fnNumberWithCommas(paymentInfo.pointPrice)); //적립금
		$(".claimMgm__odPaymentMasterId").text(paymentInfo.odPaymentMasterId); //odPaymentMasterId
		//var dropdownlist = null;
		claimMgmEventUtil.bankCodeDropDownList(accountInfo.bankCd);
		if(fnNvl(accountInfo.accountHolder) != '') {
			$("#holderName").val(accountInfo.accountHolder).data({'accountholder': fnNvl(accountInfo.accountHolder), 'bankCd' : fnNvl(accountInfo.bankCd)});
			$("#accountNumber").val(accountInfo.accountNumber).data('accountnumber', fnNvl(accountInfo.accountNumber));
			//dropdownlist.readonly();
			$("#fnValidBankAccount").hide();
			isValidBankAccount = true;
		}
		if(paymentInfo.payTp == "PAY_TP.COLLECTION") {
			outmallOrderYn = "Y";
		}
	},
	//-> CS환불정보
	initCSRefundInfo : function(priceInfo){
		$(".claimMgm__refundType").text("무통장 입금");

		$(".claimMgm__goodsPrice").text(fnNumberWithCommas(priceInfo.goodsSalePriceSum)); //주문상품금액(판매가합계)
		$(".claimMgm__refundPrice").text(fnNumberWithCommas(priceInfo.refundRegPrice)).attr('data-orgval', priceInfo.refundRegPrice); //환불금액
		$(".claimMgm__remainPaymentPrice").text(fnNumberWithCommas(priceInfo.remainPaymentPrice)).attr('data-orgval', priceInfo.remainPaymentPrice); //잔여 결재 금액
		$(".claimMgm__refundPointPrice").text(fnNumberWithCommas(priceInfo.refundPointPrice)).attr('data-orgval', priceInfo.refundPointPrice); //환불 적립금
		$(".claimMgm__remainPointPrice").text(fnNumberWithCommas(priceInfo.remainPointPrice)).attr('data-orgval', priceInfo.remainPointPrice); //잔여 적립금
		$(".claimMgm__refundTotalPrice").text(fnNumberWithCommas(priceInfo.refundTotalPrice)).attr('data-orgval', priceInfo.refundTotalPrice); //환불 금액 합계

		$('#csRefundDiv').show();
		$('#refundPriceCS').val(0); // 환불금액
		$('#couponCartDiv').hide(); //상품,장바구니쿠폰
		$('#shipPriceDiv').hide(); //배송비

		//승인요청처리 checkbox
		// if(priceInfo.refundPrice >= 10000) { //10000원 이상 시
		// 	$('#approveSelectDiv').show();
		// } else {
		// 	$('#approveSelectDiv').hide();
		// }
	},
  //-> 환불정보
  initRefundInfo : function(priceInfo, goodsCouponList, cartCouponList, orderGoodList, paramData){
		// $('#inputFormRefundBank').hide();
		$('#csRefundDiv').hide();

		//상품쿠폰 리스트 template
		const $target = $("#startGoodsCouponListArea .claimMgm__goodsCouponList");
		var goodsCouponPrice = 0;
			$target.empty();

			let tpl = $('#goodsCouponListItem').html();
			let tplObj = null;

			for(let i=0; i<goodsCouponList.length; i++){
				goodsCouponPrice += goodsCouponList[i].discountPrice;
				tplObj = $(tpl);
				tplObj.find(".claimMgm__goodsCouponName").text(goodsCouponList[i].pmCouponNm).attr("id", "goodsCouponName_"+i); 	//상품 쿠폰 > 쿠폰명
				tplObj.find(".claimMgm__goodsDiscountDetail").text(goodsCouponList[i].discountDetl).attr("id", "goodsDiscountDetail_"+i); 	//상품 쿠폰 > 할인상세
				// hidden
				tplObj.find("input[name=odClaimDetlDiscountIdGoods]").text(goodsCouponList[i].odClaimDetlDiscountId).attr("id", "odClaimDetlDiscountIdGoods_"+i);
				tplObj.find("input[name=odClaimDetlIdGoods]").text(goodsCouponList[i].odClaimDetlId).attr("id", "odClaimDetlIdGoods_"+i);
				tplObj.find("input[name=discountTpGoods]").text(goodsCouponList[i].discountTp).attr("id", "discountTpGoods_"+i);
				tplObj.find("input[name=pmCouponIssueIdGoods]").text(goodsCouponList[i].pmCouponIssueId).attr("id", "pmCouponIssueIdGoods_"+i);
				tplObj.find("input[name=discountPriceGoods]").text(goodsCouponList[i].discountPrice).attr("id", "discountPriceGoods_"+i);
				tplObj.find("input[name=psEmplDiscMasterIdGoods]").text(goodsCouponList[i].psEmplDiscMasterId).attr("id", "psEmplDiscMasterIdGoods_"+i);
				tplObj.find("input[name=urBrandIdGoods]").text(goodsCouponList[i].urBrandId).attr("id", "urBrandIdGoods_"+i);
				tplObj.find("input[name=odOrderDetlDiscountIdGoods]").text(goodsCouponList[i].odOrderDetlDiscountId).attr("id", "odOrderDetlDiscountIdGoods_"+i);
				tplObj.find("input[name=odOrderDetlIdGoods]").text(goodsCouponList[i].odOrderDetlId).attr("id", "odOrderDetlIdGoods_"+i);

				$target.append(tplObj);
			}

		//장바구니쿠폰 리스트 template
		const $targetCart = $("#startCartCouponListArea .claimMgm__cartCouponList");
		$targetCart.empty();

		let tplCart = $('#cartCouponListItem').html();
		let tplObjCart = null;
		var cartCouponPrice = 0;
		for(let i=0; i<cartCouponList.length; i++){
			cartCouponPrice += cartCouponList[i].discountPrice;
			tplObjCart = $(tplCart);
			tplObjCart.find(".claimMgm__cartCouponName").text(cartCouponList[i].pmCouponNm).attr("id", "cartCouponName_"+i); 	//장바구니 쿠폰 > 쿠폰명
			tplObjCart.find(".claimMgm__cartDiscountDetail").text(cartCouponList[i].discountDetl).attr("id", "cartDiscountDetail_"+i); 	//장바구니 쿠폰 > 할인상세
			// hidden
			tplObjCart.find("input[name=odClaimDetlDiscountIdCart]").text(cartCouponList[i].odClaimDetlDiscountId).attr("id", "odClaimDetlDiscountIdCart_"+i);
			tplObjCart.find("input[name=odClaimDetlIdCart]").text(cartCouponList[i].odClaimDetlId).attr("id", "odClaimDetlIdCart_"+i);
			tplObjCart.find("input[name=discountTpCart]").text(cartCouponList[i].discountTp).attr("id", "discountTpCart_"+i);
			tplObjCart.find("input[name=pmCouponIssueIdCart]").text(cartCouponList[i].pmCouponIssueId).attr("id", "pmCouponIssueIdCart_"+i);
			tplObjCart.find("input[name=discountPriceCart]").text(cartCouponList[i].discountPrice).attr("id", "discountPriceCart_"+i);
			tplObjCart.find("input[name=psEmplDiscMasterIdCart]").text(cartCouponList[i].psEmplDiscMasterId).attr("id", "psEmplDiscMasterIdCart_"+i);
			tplObjCart.find("input[name=urBrandIdCart]").text(cartCouponList[i].urBrandId).attr("id", "urBrandIdCart_"+i);
			tplObjCart.find("input[name=odOrderDetlDiscountIdCart]").text(cartCouponList[i].odOrderDetlDiscountId).attr("id", "odOrderDetlDiscountIdCart_"+i);
			tplObjCart.find("input[name=odOrderDetlIdCart]").text(cartCouponList[i].odOrderDetlId).attr("id", "odOrderDetlIdCart_"+i);

			$targetCart.append(tplObjCart);
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


		$(".claimMgm__goodsPrice").text(fnNumberWithCommas(priceInfo.goodsSalePriceSum)); //주문상품금액(판매가합계)
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
	  let addPaymentShippingPriceYn = priceInfo.addPaymentShippingPriceYn;
		if(shippingPrice < 0) {
			tagStr = "&minus;";
			shippingPrice = Math.abs(shippingPrice);
		}
		else if(shippingPrice == 0) {
			tagStr = "";
		}
		if(fnNvl(priceInfo.addPaymentShippingPrice) != '') {
			tagStr = "&minus;";
			shippingPrice = priceInfo.addPaymentShippingPrice;
		}
		$("#shippingPrice").attr("data-val", priceInfo.shippingPrice).text(fnNumberWithCommas(shippingPrice)); //배송비
		$("#shippingPrice").siblings("span.shipping").html(tagStr);

		/** 2021-06-14 임직원지원금 추가 : 임직원지원금이 > 0 임직원지원금 영역 노출 (클레임수량 변경 시 임직원 지원금 변경)*/
		var employeeDiscountArr = new Array();
		for(let i=0; i < orderGoodList.length; i++) {
			employeeDiscountArr.push(orderGoodList[i].employeeDiscountPrice);
		}
		if(employeeDiscountArr.some(elem => elem > 0)) {
			$("#employeeDiscountDiv").show();
			$(".claimMgm__employeeDiscountPrice").text(fnNumberWithCommas(priceInfo.employeePrice)).attr('data-orgval', priceInfo.employeePrice); // 임직원 지원금
		} else {
			$("#employeeDiscountDiv").hide();
		}
		/** 2021-03-21 추가 END */
		//$("#shippingPrice").text(fnNumberWithCommas(priceInfo.shippingPrice)); //배송비
		//$(".claimMgm__refundPrice").text("1000"); //환불금액
		$(".claimMgm__refundPrice").text(fnNumberWithCommas(priceInfo.refundRegPrice)).attr('data-orgval', priceInfo.refundRegPrice); //환불금액
		$(".claimMgm__remainPaymentPrice").text(fnNumberWithCommas(priceInfo.remainPaymentPrice)).attr('data-orgval', priceInfo.remainPaymentPrice); //잔여 결재 금액
		$(".claimMgm__refundPointPrice").text(fnNumberWithCommas(priceInfo.refundPointPrice)).attr('data-orgval', priceInfo.refundPointPrice); //환불 적립금
		$(".claimMgm__remainPointPrice").text(fnNumberWithCommas(priceInfo.remainPointPrice)).attr('data-orgval', priceInfo.remainPointPrice); //잔여 적립금
		//$(".claimMgm__refundTotalPrice").text("1000"); //환불 금액 합계
		let addPaymentShippingPrice = priceInfo.addPaymentShippingPrice;
		$(".claimMgm__refundTotalPrice").text(fnNumberWithCommas(priceInfo.refundTotalPrice)).attr('data-orgval', priceInfo.refundTotalPrice); //환불 금액 합계
		if(addPaymentShippingPriceYn == 'Y') {
			// $("#refundNotiText").text('추가 결제 금액 합계 :');
			if(paramData.odClaimId == 0) {
				$('#addPaymentView').show(); //추가결제 버튼 div show
			}
			$(".addPaymentPriceInfo").show();
			$(".claimMgm__addPaymentTotalPrice").text(fnNumberWithCommas(addPaymentShippingPrice)).attr({'data-orgval': addPaymentShippingPrice, 'data-addpaymentcnt': priceInfo.addPaymentCnt}); //추가 배송비 결제 금액
		}
		else {
			// $("#refundNotiText").text('환불 금액 합계 :');
			if(paramData.odClaimId == 0) {
				$('#addPaymentView').hide(); //추가결제 버튼 div hide
			}
			$(".addPaymentPriceInfo").hide();
			$(".claimMgm__addPaymentTotalPrice").text(0).attr({'data-orgval': 0, 'data-addpaymentcnt': priceInfo.addPaymentCnt});
		}

		if(paramData.odClaimId > 0) {
			//반품,취소일시 > 추가결제 발생할 경우
			if (paramData.orderStatusCd == 'RI') { // 현 주문상태가 반품승인, 반품보류일 때
				$('#addPaymentView').hide(); //추가결제 버튼 div hide
			} else {
				if (addPaymentShippingPriceYn == 'Y') {
					if (priceInfo.addPaymentCnt < 1 && addPaymentShippingPrice > 0) {
						if (fnNvl(priceInfo.addPaymentTp) == '' || (priceInfo.addPaymentTp == 'ADD_PAYMENT_TP.VIRTUAL' || priceInfo.addPaymentTp == 'ADD_PAYMENT_TP.DIRECT_PAY')) {
							//$(".claimMgm__addPaymentTotalPrice").text("-" + fnNumberWithCommas(addPaymentShippingPrice)).attr('data-orgval', addPaymentShippingPrice); //환불 금액 합계
							$('#addPaymentView').show(); //추가결제 버튼 div show
						}
						else {
							$('#addPaymentView').hide(); //추가결제 버튼 div show
						}
					}
					else {
						$('#addPaymentView').hide(); //추가결제 버튼 div show
					}
				} else {
					$('#addPaymentView').hide();
				}
			}
		}
  },
  //-> 반품 이미지 정보
  initReturnsImg : function(attcInfoList)  {
		// 반품 이미지 정보가 있을 경우
		if(fnNvl(attcInfoList) != '') {
			var imgStr = "";
			for(var i=0; i<attcInfoList.length; i++) {
				let attrInfo = attcInfoList[i];
				imgStr += "<img src='" + publicStorageUrl + attrInfo.uploadPath + attrInfo.uploadNm + "' style='width:150px;height:150px;margin-right:10px;' onclick='$scope.fnShowImage(\""+publicStorageUrl + attrInfo.uploadPath + attrInfo.uploadNm+"\")'/>";
			}
			$("#returnImgArea .complex-condition").html(imgStr);
			$("#returnImgArea").show();
		}
		else {
			$("#returnImgArea").hide();
		}
  },
  //-> 반품상품 수거 주소정보(보내는 분)
  initSendShippingInfo : function(sendShippingInfo) {
	  if (sendShippingInfo != null) {
	  	  let recvHp = fnNvl(sendShippingInfo.recvHp) == "" ? "01000000000" : sendShippingInfo.recvHp.replace(/\-/g,''); // 휴대폰번호 하이픈(-) 제거

		  $("input[name=claimMgm__recvNm]").val(sendShippingInfo.recvNm == "" ? 0 : sendShippingInfo.recvNm); //이름
		  $("input[name=claimMgm__recvZipCd]").val(sendShippingInfo.recvZipCd == "" ? 0 : sendShippingInfo.recvZipCd); //우편번호
		  if(recvHp.length < 12) {
		  	if(recvHp.indexOf('02') == 0) {
				  $("input[name=claimMgm__phonePrefix]").val(recvHp.slice(0,2) == "" ? 0 : recvHp.slice(0,2)); //prefix
				  $("input[name=claimMgm__phone1]").val(recvHp.slice(2,(recvHp.length)-4) == "" ? 0 : recvHp.slice(2,(recvHp.length)-4)); //phone1
				  $('input[name=claimMgm__phone2]').val(recvHp.slice((recvHp.length)-4,recvHp.length) == "" ? 0 : recvHp.slice((recvHp.length)-4,recvHp.length)); //phone2
			} else {
		  		  $("input[name=claimMgm__phonePrefix]").val(recvHp.slice(0,3) == "" ? 0 : recvHp.slice(0,3)); //prefix
				  $("input[name=claimMgm__phone1]").val(recvHp.slice(3,(recvHp.length)-4) == "" ? 0 : recvHp.slice(3,(recvHp.length)-4)); //phone1
				  $('input[name=claimMgm__phone2]').val(recvHp.slice((recvHp.length)-4,recvHp.length) == "" ? 0 : recvHp.slice((recvHp.length)-4,recvHp.length)); //phone2
			}
		  } else if(12 <= recvHp.length) {
				  $("input[name=claimMgm__phonePrefix]").val(recvHp.slice(0,4) == "" ? 0 : recvHp.slice(0,4)); //prefix
				  $("input[name=claimMgm__phone1]").val(recvHp.slice(4,(recvHp.length)-4) == "" ? 0 : recvHp.slice(4,(recvHp.length)-4)); //phone1
				  $('input[name=claimMgm__phone2]').val(recvHp.slice((recvHp.length)-4,recvHp.length) == "" ? 0 : recvHp.slice((recvHp.length)-4,recvHp.length)); //phone2
		  }
		  $("input[name=claimMgm__recvAddr1]").val(sendShippingInfo.recvAddr1 == "" ? 0 : sendShippingInfo.recvAddr1); //주소
		  $("input[name=claimMgm__recvAddr2]").val(sendShippingInfo.recvAddr2 == "" ? 0 : sendShippingInfo.recvAddr2); //상세주소
		  //배송요청사항
		  if(sendShippingInfo.deliveryMsg == "부재 시 경비실에 맡겨주세요." ||
				  sendShippingInfo.deliveryMsg == "부재 시 문 앞에 놓아주세요" ||
				  sendShippingInfo.deliveryMsg == "부재 시 휴대폰으로 연락바랍니다." ||
				  sendShippingInfo.deliveryMsg == "배송 전 연락바랍니다."){
				$("input[name=claimMgm__deliveryMsg]").data('kendoDropDownList').value(sendShippingInfo.deliveryMsg == null ? 0 : sendShippingInfo.deliveryMsg);
				$("input[name=claimMgm__deliveryMsgEtc]").css("display","none");
		  }else{
				$("input[name=claimMgm__deliveryMsgEtc]").css("display","");
				$("input[name=claimMgm__deliveryMsgEtc]").val(sendShippingInfo.deliveryMsg);
		  }
		  //출입정보 (공동현관비밀번호, 기타)
		  if(sendShippingInfo.doorMsgCd == "ACCESS_INFORMATION.FRONT_DOOR_PASSWORD" || sendShippingInfo.doorMsgCd == "ACCESS_INFORMATION.ETC"){
				$("input[name=claimMgm__doorMsg]").css("display","");
				$("input[name=claimMgm__doorMsg]").val(sendShippingInfo.doorMsg);
		  }else{
				$("input[name=claimMgm__doorMsg]").css("display","none");
		  }
		  $("input[name=claimMgm__doorMsgCd]").data('kendoDropDownList').value(sendShippingInfo.doorMsgCd == null ? 0 : sendShippingInfo.doorMsgCd);
	  }
  },
  //-> 반품회수 주소정보 (받는 분)
  initOrderClaimShippingInfoList : function(receShippingInfoList, paramData) {
	  const $target = $("#startClaimShippingListArea .claimShippingList__list");
	  $target.empty();

	  let tpl = $('#claimShippingListItem').html();
	  let tplObj = null;

	  if (receShippingInfoList != null && receShippingInfoList.length > 0) {
		  for(let i=0; i<receShippingInfoList.length; i++){
				if(receShippingInfoList[i].storeYn == "Y") {
					continue;
				}
				tplObj = $(tpl);
				let hpArr = [];
				let recvHp = fnNvl(receShippingInfoList[i].recvHp) == "" ? "01000000000" : receShippingInfoList[i].recvHp.replace(/\-/g,''); // 휴대폰번호 하이픈(-) 제거
				tplObj.find("input[name=claimMgm__recvNmList]").val(receShippingInfoList[i].recvNm); //이름
				if(recvHp.length < 12) {
					if(recvHp.indexOf("02") == 0){
						tplObj.find("input[name=claimMgm__phonePrefixList]").val(recvHp.slice(0,2) == "" ? 0 : recvHp.slice(0,2)); //prefix
						tplObj.find("input[name=claimMgm__phone1List]").val(recvHp.slice(2,(recvHp.length)-4) == "" ? 0 : recvHp.slice(2,(recvHp.length)-4)); //phone1
						tplObj.find('input[name=claimMgm__phone2List]').val(recvHp.slice((recvHp.length)-4,recvHp.length) == "" ? 0 : recvHp.slice((recvHp.length)-4,recvHp.length)); //phone2
					} else {
						 tplObj.find("input[name=claimMgm__phonePrefixList]").val(recvHp.slice(0,3) == "" ? 0 : recvHp.slice(0,3)); //prefix
						 tplObj.find("input[name=claimMgm__phone1List]").val(recvHp.slice(3,(recvHp.length)-4) == "" ? 0 : recvHp.slice(3,(recvHp.length)-4)); //phone1
						 tplObj.find('input[name=claimMgm__phone2List]').val(recvHp.slice((recvHp.length)-4,recvHp.length) == "" ? 0 : recvHp.slice((recvHp.length)-4,recvHp.length)); //phone2
					}
			   } else if(12 <= recvHp.length) {
						  tplObj.find("input[name=claimMgm__phonePrefixList]").val(recvHp.slice(0,4) == "" ? 0 : recvHp.slice(0,4)); //prefix
						  tplObj.find("input[name=claimMgm__phone1List]").val(recvHp.slice(4,(recvHp.length)-4) == "" ? 0 : recvHp.slice(4,(recvHp.length)-4)); //phone1
						  tplObj.find('input[name=claimMgm__phone2List]').val(recvHp.slice((recvHp.length)-4,recvHp.length) == "" ? 0 : recvHp.slice((recvHp.length)-4,recvHp.length)); //phone2
			   }
				tplObj.find("input[name=claimMgm__recvZipCdList]").val(receShippingInfoList[i].recvZipCd).attr("id", "claimMgm__zipCodeList_"+receShippingInfoList[i].urWarehouseId); //우편번호
				tplObj.find("input[name=claimMgm__recvAddr1List]").val(receShippingInfoList[i].recvAddr1).attr("id", "claimMgm__recvAddr1List_"+receShippingInfoList[i].urWarehouseId); //주소
				tplObj.find("input[name=claimMgm__recvAddr2List]").val(receShippingInfoList[i].recvAddr2).attr("id", "claimMgm__recvAddr2List_"+receShippingInfoList[i].urWarehouseId); //상세주소
				tplObj.find("input[name=odClaimShippingZoneIdList]").val(receShippingInfoList[i].odClaimShippingZoneId).attr("id", "odClaimShippingZoneIdList_"+receShippingInfoList[i].urWarehouseId); //상세주소
				tplObj.find("input[name=urWarehouseIdList]").val(receShippingInfoList[i].urWarehouseId).attr("id", "urWarehouseIdList_"+receShippingInfoList[i].urWarehouseId); //상세주소
				tplObj.find("input[name=odOrderDetlIdList]").val(receShippingInfoList[i].odOrderDetlId).attr("id", "odOrderDetlIdList_"+receShippingInfoList[i].urWarehouseId); //상세주소
				tplObj.find("#fnAddressPopup2").attr("onClick", "$scope.fnAddressPopup2("+receShippingInfoList[i].urWarehouseId+")"); //출고처pk
				$target.append(tplObj);
		  }
	  } else {
		  tplObj = $(tpl);
		  tplObj.find("input[name=claimMgm__recvNmList]").val(''); //이름
		  tplObj.find("input[name=claimMgm__phonePrefixList]").val(''); //prefix
		  tplObj.find("input[name=claimMgm__phone1List]").val(''); //phone1
		  tplObj.find('input[name=claimMgm__phone2List]').val(''); //phone2
		  tplObj.find("input[name=claimMgm__recvZipCdList]").val(''); //우편번호
		  tplObj.find("input[name=claimMgm__recvAddr1List]").val(''); //주소
		  tplObj.find("input[name=claimMgm__recvAddr2List]").val(''); //상세주소
		  tplObj.find("input[name=odClaimShippingZoneIdList]").val(''); //상세주소
		  tplObj.find("input[name=urWarehouseIdList]").val(''); //상세주소
		  tplObj.find("input[name=odOrderDetlIdList]").val(''); //상세주소
		  tplObj.find("#fnAddressPopup2").attr("onClick", "$scope.fnAddressPopup2()"); //출고처pk
		  $target.append(tplObj);
	  }
  },

  //-> 출고 정보
  initOrderClaimReleaseList : function(deliveryInfoList, warehouseId) {
	dayOn = [];

	let tpl = $('#reShippingTmpl').html();
	var htmlStr = "";

	if(deliveryInfoList != null && deliveryInfoList.length > 0) {

		for(var i=0; i<deliveryInfoList.length; i++) {

			var deliveryInfo = deliveryInfoList[i];
			var tplTemp = tpl.replace(/\{warehouseId}/ig, deliveryInfo.urWarehouseId)
								.replace(/\{warehouseNm}/ig, deliveryInfo.urWarehouseNm)
								.replace(/\{index}/ig, i);
			var deliveryInfoArr = deliveryInfo.deliveryInfoList.arrivalScheduleList;

			for(var j=0; j<5; j++) {
				var dateInfo = deliveryInfoArr[j];
				var orderDateRegexStr = new RegExp("\\{orderDate" + j + "}", "ig");
				var forwardRegexStr = new RegExp("\\{forwardIndex" + j + "}", "ig");
				var forwardDateRegexStr = new RegExp("\\{forwardingScheduledDate" + j + "}", "ig");
				var arriveRegexStr = new RegExp("\\{arrivalIndex" + j + "}", "ig");
				var arriveDateRegexStr = new RegExp("\\{arrivalScheduledDate" + j + "}", "ig");
				if(dateInfo != null) {
					tplTemp = tplTemp.replace(forwardRegexStr, j)
										.replace(orderDateRegexStr, dateInfo.orderDate)
										.replace(forwardDateRegexStr, dateInfo.forwardingScheduledDate)
										.replace(arriveRegexStr, j)
										.replace(arriveDateRegexStr, dateInfo.arrivalScheduledDate)
										;
				}
				else {
					tplTemp = tplTemp.replace(forwardRegexStr, j)
										.replace(forwardDateRegexStr, '-')
										.replace(arriveRegexStr, j)
										.replace(arriveDateRegexStr, '-')
										;
				}
			}

			htmlStr += tplTemp;
		}
		$("#reShippingTbody").html(htmlStr);
		claimMgmEventUtil.initDatePicker();
	}
  },

  clear: function() {
      var form = $("#searchForm");
      form.formClear(true);

      $(".date-controller button").attr("fb-btn-active", false);
  },
  setClaimAddShippingPrice: function(addPaymentList) {

		var claimGoodsDatas = $("#claimMgmPopupOrderGoodsGrid").data("kendoGrid").dataSource.data();
		let putData = new Object();
		let claimGoodsList = new Array();
		if(fnNvl(addPaymentList) == '') {
			for(var i=0; i<claimGoodsDatas.length; i++) {
				let claimGoodsItem = claimGoodsDatas[i];
				claimGoodsItem.addPaymentShippingPrice = 0;
				claimGoodsList.push(claimGoodsItem);
			}
		}
		else {
			for(var i=0; i<claimGoodsDatas.length; i++) {
				let claimGoodsItem = claimGoodsDatas[i];
				for(var j=0; j<addPaymentList.length; j++) {
					var addPaymentItem = addPaymentList[j];
					if(claimGoodsItem.reShipping != true &&
						addPaymentItem.urWarehouseId == claimGoodsItem.urWarehouseId &&
						addPaymentItem.ilShippingTmplId == claimGoodsItem.ilShippingTmplId &&
						putData[claimGoodsItem.urWarehouseId + '_' + claimGoodsItem.ilShippingTmplId] == undefined) {
						claimGoodsItem.addPaymentShippingPrice = addPaymentItem.addPaymentShippingPrice;
						putData[claimGoodsItem.urWarehouseId + '_' + claimGoodsItem.ilShippingTmplId] = addPaymentItem.addPaymentShippingPrice;
					}
				}
				claimGoodsList.push(claimGoodsItem);
			}
		}
		return claimGoodsList;
  }
}

var claimMgmEventUtil = {
	//회수여부
	initWithDrawType : function(paramData, odClaimInfo, changeEvent) {
		fnTagMkRadio({
			id: 'withDraw',
			tagId: 'withDraw',
			chkVal: fnNvl(odClaimInfo.returnsYn) != '' ? odClaimInfo.returnsYn : 'N',
			data: [{
					CODE: "N",
					NAME: "회수 안함",
			},{
					CODE: "Y",
					NAME: "회수",
			}],
			change: function(e) {
				const value = e.target.value;
				$('#divPriorityUndelivered').hide();
				$("input:checkbox[name='priorityUndelivered']").prop('checked', false);
				if (value == 'N') {
					$('.inputForm__section.toggleSection.addressInfo1').hide();
					$('.inputForm__section.toggleSection.addressInfo2').hide();
					$('#divPriorityUndelivered').show();
					if (paramData.orderStatusCd == 'DI' || paramData.orderStatusCd == 'DC' || paramData.orderStatusCd == 'BF') {
						$(".claimMgm__odStatusChange").text(paramData.orderStatus + " ➜ " + '반품완료');
					} else {
						$("#odStatusRA_0").prop('checked', true);
						$('#odStatusRA_0').parents().eq(0).show(); //반품완료 라디오버튼 show
						$('#odStatusRA_1').parents().eq(0).hide(); //반품승인 라디오버튼 hide
					}
				} else { // 'Y'
					$('.inputForm__section.toggleSection.addressInfo1').show();
					$('.inputForm__section.toggleSection.addressInfo2').show();
					if (paramData.orderStatusCd == 'DI' || paramData.orderStatusCd == 'DC' || paramData.orderStatusCd == 'BF') {
						$(".claimMgm__odStatusChange").text(paramData.orderStatus + " ➜ " + '반품승인');
					} else {
						if(paramData.orderStatusCd == 'RI'){
							$('#divPriorityUndelivered').show();
						}
						$("#odStatusRA_1").prop('checked', true);
						$('#odStatusRA_1').parents().eq(0).show(); //반품승인 라디오버튼 show
						$('#odStatusRA_0').parents().eq(0).hide(); //반품완료 라디오버튼 hide
					}
				}
				if (typeof changeEvent == 'function') {
					changeEvent(value);
				}
			}
		});
	},
	// 재배송 & 대체상품 라디오 태그
	initReShippingType : function(changeEvent) {
		fnTagMkRadio({
			id: 'reShippingType',
			tagId: 'reShippingType',
			chkVal: 'R',
			data: [{
					CODE: "R",
					NAME: "재배송",
			},{
					CODE: "S",
					NAME: "대체상품",
			}],
			change: function(e) {
				const value = e.target.value;
				if(typeof changeEvent == 'function') {
					changeEvent(value);
				}
				else {
					if( value == 'S' ) {
						$('.fnAddCoverageGoods').show(); //상품조회팝업 버튼 show
					} else {
						$('.fnAddCoverageGoods').hide(); //상품조회팝업 버튼 hide
					}
				}
			}
		});
	},
	//택배사
	psShipppingCompId : function() {
		 fnKendoDropDownList({
	  			id         : 'psShippingCompId',
	  			url        : "/admin/policy/shippingcomp/getDropDownPolicyShippingCompList",
	  			params	   : {},
	  			blank      : '선택해주세요.',
	  			valueField : "psShippingCompId",
	  			textField  : "shippingCompNm"
	  		});
	},
	//사유
	csPsClaimMallId : function(paramData) {
		//신청할 클레임상태 별 사유 목록
		var searchReasonTypeList = new Array();

		fnAjax({
			url     : "/admin/order/claim/getOrderClaimReasonList",
			params  : {searchReasonTypeList : searchReasonTypeList},
			contentType: "application/json",
			success : function( data ){

				if(data.rows != undefined && data.rows.length > 0) {
					var reasonArr = new Array();
					var defaultClaimMallId = defaultValuePsClaimMallId;
					for(var i=0; i<data.rows.length; i++) {
						let psClaimMallId = data.rows[i].psClaimMallId + "_" + data.rows[i].lclaimCtgryId + "_" + data.rows[i].mclaimCtgryId + "_" + data.rows[i].sclaimCtgryId + "_" + data.rows[i].psClaimBosId;
						if(data.rows[i].psClaimMallId == 0) {
							defaultClaimMallId = psClaimMallId;
						}
						var psClaimBosIds = data.rows[i].psClaimBosVal;
						if(paramData.undeliveredClaimYn == "Y" && psClaimBosIds != null) {
							defaultClaimMallId = psClaimBosIds;
						}
						data.rows[i].psClaimMallId = psClaimMallId;
						reasonArr.push(data.rows[i]);
					}

					defaultClaimMallId = '';
					fnKendoDropDownList({
						id         : 'psClaimMallId',
						blank      : '선택해주세요.',
						data       : reasonArr,
						valueField : "psClaimMallId", //숫자 0,1,2...
						textField  : "reasonMessage",
						value      : defaultClaimMallId,
						dataBound : function() {
							var reasonArr = defaultClaimMallId.split("_");
							setTimeout(function() {
								fnBosClaimReasonList(reasonArr);
							}, 1000);

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

				function fnBosClaimReasonList(reasonArr) {
					var selectFlag = (reasonArr.length < 4);
					var orderGoodList = $("#claimMgmPopupOrderGoodsGrid").data("kendoGrid").dataSource.data();
					for (let i = 0; i < orderGoodList.length; i++) {
						if(fnNvl(LClaimCtgryDropDownList[i]) == "") {
							continue;
						}
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
					if (data.rows != undefined && data.rows.length > 0) {
						var reasonArr = new Array();
						var defaultClaimMallId = defaultValuePsClaimMallId;
						for (var i = 0; i < data.rows.length; i++) {
							let psClaimMallId = data.rows[i].psClaimMallId + "_" + data.rows[i].lclaimCtgryId + "_" + data.rows[i].mclaimCtgryId + "_" + data.rows[i].sclaimCtgryId + "_" + data.rows[i].psClaimBosId;
							if (data.rows[i].psClaimMallId == 0) {
								defaultClaimMallId = psClaimMallId;
							}
							var psClaimBosIds = data.rows[i].psClaimBosVal;
							if (paramData.undeliveredClaimYn == "Y" && psClaimBosIds != null) {
								defaultClaimMallId = psClaimBosIds;
							}
							data.rows[i].psClaimMallId = psClaimMallId;
							reasonArr.push(data.rows[i]);
						}

						if(putOrderStatusCd.charAt(0) == 'C' || putOrderStatusCd.charAt(0) == 'R' || putOrderStatusCd.charAt(0) == 'E') {
							defaultClaimMallId = '';
						}

						fnKendoDropDownList({
							id: 'psClaimMallId',
							blank: '선택해주세요.',
							data: reasonArr,
							valueField: "psClaimMallId", //숫자 0,1,2...
							textField: "reasonMessage",
							value: defaultClaimMallId,
							dataBound: function () {
								var reasonArr = defaultClaimMallId.split("_");

								setTimeout(function () {
									fnBosClaimReasonList(reasonArr);
								}, 1000);

								$('#ng-view').on('change', '#psClaimMallId', function () {
									if (fnNvl($("#bosClaimReasonListDiv").css("display")) == "" || $("#bosClaimReasonListDiv").css("display") == "none") {
										return false;
									}
									reasonArr = $(this).val().split("_");
									fnBosClaimReasonList(reasonArr);
								});
							}
						});
						//	}
					}

					function fnBosClaimReasonList(reasonArr) {
						var selectFlag = (reasonArr.length < 4);
						var orderGoodList = $("#claimMgmPopupOrderGoodsGrid").data("kendoGrid").dataSource.data();
						for (let i = 0; i < orderGoodList.length; i++) {
							if(fnNvl(LClaimCtgryDropDownList[i]) == "") {
								continue;
							}

							// 클레임사유(대)
							var lCtgryList = $(LClaimCtgryDropDownList[i].element).data("kendoDropDownList");
							var mCtgryList = $(MClaimCtgryDropDownList[i].element).data("kendoDropDownList");

							if (!selectFlag) {
								lCtgryList.value(reasonArr[1]);
								lCtgryList.trigger("change");
								// 클레임사유(중)
								mCtgryList.value(reasonArr[2]);
								mCtgryList.trigger("change");

								setTimeout(function () {
									var sCtgryList = $(SClaimCtgryDropDownList[i].element).data("kendoDropDownList");
									// 클레임사유(소)
									sCtgryList.value(reasonArr[3]);
									sCtgryList.trigger("change");
								}, 1000)

							} else {
								lCtgryList.value('');
								lCtgryList.trigger("change");
							}
						}
					}
				}
	 		},
 			isAction : "select"
 		});
	},
	psClaimMallIdForBosClaimReason : function(paramData, claimReasonData) {
		//신청할 클레임상태 별 사유 목록
		var searchReasonTypeList = new Array();

		fnAjax({
			url     : "/admin/order/claim/getOrderClaimReasonList",
			params  : {searchReasonTypeList : searchReasonTypeList},
			contentType: "application/json",
			success : function( data ){
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
			},
			isAction : "select"
		});
	},
	//배송요청사항
	deliveryMsgCdDropDownList :function() {
		var deliveryMsgCdDropDownList = fnKendoDropDownList({
				id  : 'claimMgm__deliveryMsg',
				data  : [
					{"CODE":"부재 시 경비실에 맡겨주세요."		,"NAME":"부재 시 경비실에 맡겨주세요."},
					{"CODE":"부재 시 문 앞에 놓아주세요"		,"NAME":"부재 시 문 앞에 놓아주세요"},
					{"CODE":"부재 시 휴대폰으로 연락바랍니다."	,"NAME":"부재 시 휴대폰으로 연락바랍니다."},
					{"CODE":"배송 전 연락바랍니다."			,"NAME":"배송 전 연락바랍니다."},
					{"CODE":"직접입력"						,"NAME":"직접입력"}
				],
				textField  :"NAME",
				valueField : "CODE",
				blank : "선택"
			});
		 deliveryMsgCdDropDownList.bind('change',function(e){
				if($("#claimMgm__deliveryMsg").val() == '직접입력'){
					 $("#claimMgm__deliveryMsgEtc").css("display","");
					 $("#claimMgm__deliveryMsgEtc").val('');
				}else{
					 $("#claimMgm__deliveryMsgEtc").css("display","none");
					 $("#claimMgm__deliveryMsgEtc").val('');
				}
			});
	},
	//출입정보
	doorMsgCdDropDownList : function() {
		var doorMsgCdDropDownList = fnKendoDropDownList({
			id         : 'claimMgm__doorMsgCd',
			url        : "/admin/comn/getCodeList",
			autoBind   : true,
			blank      : '선택',
			params : {"stCommonCodeMasterCode" : "ACCESS_INFORMATION", "useYn" :"Y"},
		});
		doorMsgCdDropDownList.bind('change', function(e){
			if($("#claimMgm__doorMsgCd").val() == 'ACCESS_INFORMATION.FRONT_DOOR_PASSWORD' || $("#claimMgm__doorMsgCd").val() == 'ACCESS_INFORMATION.ETC' ){
				$("#claimMgm__doorMsg").css("display","");
				$("#claimMgm__doorMsg").val('');
			}else{
				$("#claimMgm__doorMsg").css("display","none");
				$("#claimMgm__doorMsg").val('');
			}
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
				if(fnNvl(param) != "") {
					$("#bankName").val(param.NAME);
				}
				//isValidBankAccount = false;
				$("#validBankAccountDiv").text('');
		});

		// $("#accountNumber, #holderName").bind('blur', function(e){
		// 		isValidBankAccount = false;
		// 		$("#validBankAccountDiv").text('');
		// });
	},
	//출고지시일 날짜
	initDatePicker : function() {
		$("#reShippingTbody .fb__datePicker--input").each(function(index) {
			var arr = new Array();

			var tr = $(this).closest("tr");
			tr.find("td").each(function() {
				var selectDt = $(this).children("div").attr("value");
				if(selectDt !== undefined) {
					arr.push(selectDt);
				}
			});

			new FbDatePicker("datepicker" + index, {
				 dayOn : arr
				,onChange: function(e) {
					var date = e.target.value;

					tr.find("td").each(function(index) {
						var selectDt = $(this).children("div").attr("value");
						if(selectDt == date) {
							var next = tr.next();
							var arriveDt = next.find("td:eq(" + index + ")").children("div").attr("value");
							next.find("input[name=orderIfDate]").val(arriveDt);
						}
					});
				}
			})
		});
//		datePicker = new FbDatePicker("datepicker", {
//			dayOn : dayOn
//			,onChange: function(e) {
//				var date = e.target.value;
//				for(var i = 0 ; i < arrivalScheduleList.length ; i ++){
//					if(arrivalScheduleList[i].forwardingScheduledDate == date){
//						$("#orderIfDate").val(arrivalScheduleList[i].arrivalScheduledDate);
//					}
//				}
//			}
//		})
	}

}

var claimOrderStatusTypeUtil ={
	// 주문상태 취소요청 시 태그
	initOdStatusCAType : function() {
		fnTagMkRadio({
			id: 'odStatusCA',
			tagId: 'odStatusCA',
			chkVal: 'CC',
			data: [{
					CODE: "CC",
					NAME: "취소요청 ➜ 취소완료",
			},{
					CODE: "CE",
					NAME: "취소거부",
			}],
			change: function(e) {
				const value = e.target.value;
				$('#divPriorityUndelivered').hide();
				$("input:checkbox[name='priorityUndelivered']").prop('checked', false);
				if( value == 'CC' ) {
					$('#cancelRejectDiv').hide(); //취소거부 구분 div hide
					$('#reasonDiv').show(); // 사유 div show
					$('#reasonDetlDiv').show(); // 상세사유 div show
					$('#bosClaimReasonListDiv').show(); // BOS클레임사유 div show
					$('.inputForm__section.goodsInfo').show(); //상품정보 show
					$('.inputForm__section.payInfo').show(); //결제정보 show
					$('.inputForm__section.refundInfo').show(); //환불정보 show
					$('#divPriorityUndelivered').show();
					$("input:checkbox[name='priorityUndelivered']").prop('checked', true);
				} else { // 'CE'
					$('#cancelRejectDiv').show(); //취소거부 구분 div show
					$('#reasonDiv').hide(); // 사유 div hide
					$('#reasonDetlDiv').hide(); // 상세사유 div hide
					$('#bosClaimReasonListDiv').hide(); // BOS클레임사유 div hide
					$('.inputForm__section.goodsInfo').hide(); //상품정보 hide
					$('.inputForm__section.payInfo').hide(); //결제정보 hide
					$('.inputForm__section.refundInfo').hide(); //환불정보 hide

					$("#psShippingCompId").data("kendoDropDownList").enable( false );

					$('input[name=cancelRejectType]').change(function(){
						if($(this).val() == 'rejectReasonMsg'){
							$('#psShippingCompId').data("kendoDropDownList").value( "" );
							$('#trackingNo').val("");
							$('#rejectReasonMsg').attr("disabled", false);
							$('#trackingNo').attr("disabled", true);
							$("#psShippingCompId").data("kendoDropDownList").enable( false );
						} else {
							$('#rejectReasonMsg').val("");
							$('#rejectReasonMsg').attr("disabled", true);
							$('#psShippingCompId').attr("disabled", false);
							$('#trackingNo').attr("disabled", false);
							$("#psShippingCompId").data("kendoDropDownList").enable( true );
						}
					});
				}
			}
		});
	},
	// 주문상태 취소요청 -> 취소거부 태그
	initOdStatusCEType : function(){
		fnTagMkRadio({
			id: 'odStatusCE',
			tagId: 'odStatusCE',
			chkVal: 'Y',
			data: [{
				CODE: "rejectReasonMsg",
				NAME: "거부사유",
			},{
				CODE: "shipping",
				NAME: "택배사",
			}],
		});
	},
	// 주문상태 반품요청 시 태그
	initOdStatusRAType : function() {
		fnTagMkRadio({
			id: 'odStatusRA',
			tagId: 'odStatusRA',
			chkVal: 'RC',
			data: [
				{
				CODE: "RC",
				NAME: "반품요청 ➜ 반품완료",
			},{
				CODE: "RI",
				NAME: "반품요청 ➜ 반품승인",
			},{
				CODE: "RE",
				NAME: "반품거부",
				}
			],
			change: function(e) {
				const value = e.target.value;
				$('#divPriorityUndelivered').hide();
				$("input:checkbox[name='priorityUndelivered']").prop('checked', false);

				if(value == 'RC'){
					$('#divPriorityUndelivered').show();
				}

				if( value == 'RE' ) {
					$('#refundReject').show(); //반품거부 사유입력란 show
					$('#withDrawDiv').hide(); // 회수여부 div hide
					$('#reasonDiv').hide(); // 사유 div hide
					$('#reasonDetlDiv').hide(); // 상세사유 div hide
					$('#bosClaimReasonListDiv').hide(); // BOS클레임사유 div hide
					$('.inputForm__section.goodsInfo').hide(); //상품정보 hide
					$('.inputForm__section.payInfo').hide(); //결제정보 hide
					$('.inputForm__section.refundInfo').hide(); //환불정보 hide
					$('.inputForm__section.toggleSection.addressInfo1').hide(); //반품수거주소정보 hide
					$('.inputForm__section.toggleSection.addressInfo2').hide(); //반품회수주소정보 hide

				} else {
					$('#refundReject').hide(); //반품거부 사유입력란 hide
					$('#withDrawDiv').show(); // 회수여부 div show
					$('#reasonDiv').show(); // 사유 div show
					$('#reasonDetlDiv').show(); // 상세사유 div show
					$('#bosClaimReasonListDiv').show(); // BOS클레임사유 div show
					$('.inputForm__section.goodsInfo').show(); //상품정보 show
					$('.inputForm__section.payInfo').show(); //결제정보 show
					$('.inputForm__section.refundInfo').show(); //환불정보 show
					$('.inputForm__section.toggleSection.addressInfo1').show(); //반품수거주소정보 show
					$('.inputForm__section.toggleSection.addressInfo2').show(); //반품회수주소정보 show
				}
			}
		});
	},
	// 주문상태 반품보류 시 태그
	initOdStatusRFType : function() {
		fnTagMkRadio({
			id: 'odStatusRF',
			tagId: 'odStatusRF',
			chkVal: 'RC',
			data: [{
					CODE: "RC",
					NAME: "반품보류 ➜ 반품완료",
			},{
					CODE: "DC",
					NAME: "반품보류 ➜ 배송완료",
			}],
			change: function(e) {
				const value = e.target.value;
				$('#divPriorityUndelivered').hide();
				$("input:checkbox[name='priorityUndelivered']").prop('checked', false);

				if(value == 'RC'){
					$('#divPriorityUndelivered').show();
				}
			}
		});
	},
	//주문상태 CS환불 시 태그
	initOdStatusCSType : function(changeEvent) {
		fnTagMkRadio({
			id: 'csRefundTp',
			tagId: 'csRefundTp',
			chkVal: 'CS_REFUND_TP.PAYMENT_PRICE_REFUND',
			data: [
				{
				CODE: "CS_REFUND_TP.PAYMENT_PRICE_REFUND",
				NAME: "예치금 환불",
			},{
				CODE: "CS_REFUND_TP.POINT_PRICE_REFUND",
				NAME: "적립금 환불",
			}
			],
			change: function(e) {
				const value = e.target.value;
				if(typeof changeEvent == 'function') {
					changeEvent(value);
				}
				else {
					$("#refundPriceCS").val(0);
				}
			}
		});
	}
}

var claimRegisterUtil = {
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
		}
}

var claimRegisterFnUtil = {

		//클레임상태, 클레임상태구분 변경
		changeClaimStatus : function (data, paramData) {
			//반품요청 -> 반품승인, 반품완료, 반품거부
			if(paramData.orderStatusCd == 'RA') {
				if(data.odStatusRA == 'RI') {//반품승인
					data.claimStatusCd = 'RI';
				}else if(data.odStatusRA == 'RC'){ //반품완료
					data.claimStatusCd = 'RC';
				} else if(data.odStatusRA == 'RE'){ //반품거부
					data.claimStatusCd = 'RE'; //반품거부
				}
				data.claimStatusTp = 'CLAIM_STATUS_TP.RETURN';
			}
			//반품승인 -> 반품완료, (반품보류 : specOut)
			if(paramData.orderStatusCd == 'RI'){
				data.claimStatusCd = 'RC';
				data.claimStatusTp = 'CLAIM_STATUS_TP.RETURN';
			}

			//반품보류 -> 반품완료, 배송완료
			if(paramData.orderStatusCd == 'RF'){
				if(data.odStatusRF == 'RC'){ //반품완료
					data.claimStatusCd = 'RC';
				} else {
					data.claimStatusCd = 'DC'; //배송완료
				}
				data.claimStatusTp = 'CLAIM_STATUS_TP.RETURN';
			}

			//배송중, 배송완료, 구매확정 -> 반품승인
			if(paramData.orderStatusCd == 'DI' || paramData.orderStatusCd == 'DC' || paramData.orderStatusCd == 'BF'){
				if(data.withDraw == 'N') {
					data.claimStatusCd = 'RC'; //반품완료
				} else {
					data.claimStatusCd = 'RI'; //반품승인
				}
				data.claimStatusTp = 'CLAIM_STATUS_TP.RETURN';
			}

			//배송준비중 -> 취소요청
			if(paramData.orderStatusCd == 'DR'){
				data.claimStatusCd = 'CA'; //취소요청
				data.claimStatusTp = 'CLAIM_STATUS_TP.CANCEL';
				if(paramData.undeliveredClaimYn == "Y") data.claimStatusCd = 'CC'; //취소완료
			}

			//취소요청 -> 취소완료, 취소거부
			if(paramData.orderStatusCd == 'CA') {
				if(data.odStatusCA == 'CC') {
					data.claimStatusCd = 'CC'; //취소완료
				} else {
					data.claimStatusCd = 'CE' //취소거부 -> 거부사유(배송준비중) / 택배사(배송중)
				}
				data.claimStatusTp = 'CLAIM_STATUS_TP.CANCEL';
			}

			//결제완료 -> 취소완료
			if(paramData.orderStatusCd == 'IC') {
				data.claimStatusCd = 'CC'; //취소완료
				data.claimStatusTp = 'CLAIM_STATUS_TP.CANCEL';
			}
			// 결제완료, 배송준비중, 배송중, 배송완료, 구매확정 -> 재배송
			if(paramData.putOrderStatusCd == 'EC'){
				data.claimStatusCd = 'EC'; //재배송
				data.claimStatusTp = 'CLAIM_STATUS_TP.RETURN_DELIVERY';
			}
		},

		//추가결제 실행 시 클레임상태 변경
		addPaymentExecution : function (data, paramData, odAddPaymentReqInfoId, payTp, addPaymentPrice) {
			if(data.claimStatusCd == 'CE' || data.claimStatusCd == 'RE') {
				return true;
			}
			// 클레임 상태가 취소요청, 취소완료, 반품요청, 반품승인, 반품완료가 아닐 경우 추가결제 불가
			if(data.claimStatusCd != 'CA' && data.claimStatusCd != 'CC' && data.claimStatusCd != 'RI' && data.claimStatusCd != 'RC') {
				fnKendoMessage({message: "클레임 상태를 확인 해주세요."});
				return false;
			}
			// 체크된 결제방법이 존재하지 않을 경우
			if($("input[name=addPayment]:checked").size() < 1 && $('#addPaymentView').css('display') != 'none') {
				fnKendoMessage({message: "추가 결제 방식을 선택 해주세요."});
				return false;
			}
			data.directPaymentYn = 'N';	// 직접결제여부 Y
			data.addPaymentTp = ''; // 추가결제방법
			//-> 추가결제 > 비인증결제, 가상계좌발급
			if(data.addPayment != 'directPay'){
				data.odAddPaymentReqInfoId = odAddPaymentReqInfoId;
				// 결제 정보 번호 채번이 되지 않았고, 추가 결제를 해야 함
				if((fnNvl(odAddPaymentReqInfoId) == "" || (odAddPaymentReqInfoId < 1 && addPaymentPrice > 0)) && data.addPayment == 'cardPay') {
					fnKendoMessage({message: "추가 결제를 진행 해주세요."});
					return false;
				}
				// 비인증 결제 일 때
				if(payTp == 'card') {
					//클레임상태가 취소요청, 취소완료인 경우
					if(data.claimStatusCd == 'CA' || data.claimStatusCd == 'CC') {
						data.claimStatusCd = 'CC'; //취소완료
						// 주문 상태가 배송준비중 일 경우
						if(paramData.orderStatusCd == 'DR') {
							data.claimStatusCd = 'CA'; //취소요청
						}
					}
					//클레임상태가 반품승인, 반품완료인 경우
					else if(data.claimStatusCd == 'RI' || data.claimStatusCd == 'RC') {
						data.claimStatusCd = 'RI'; //반품승인
					}
					data.addPaymentTp = 'ADD_PAYMENT_TP.CARD'; // 비인증결제
				}
				// 가상계좌 일 떄
				else {
					if(data.claimStatusCd == 'CA' || data.claimStatusCd == 'CC') {
						data.claimStatusCd = 'CA'; //취소요청
					}
					else if(data.claimStatusCd == 'RI' || data.claimStatusCd == 'RC') {
						data.claimStatusCd = 'RA'; //반품요청
					}
					data.addPaymentTp = 'ADD_PAYMENT_TP.VIRTUAL'; // 가상계좌
				}
				//-> 추가결제 > 직접결제
			} else if (data.addPayment == 'directPay') {
		    	//고객이 직접 추가결제 진행
				//직접결제 선택 후 변경 시 취소요청으로 변경
				//고객이 추가결제 완료 시 취소완료, 자동환불대기로 상태변경
				if(data.claimStatusCd == 'CA' || data.claimStatusCd == 'CC') {
					data.claimStatusCd = 'CA'; //취소요청
				}
				else if(data.claimStatusCd == 'RI' || data.claimStatusCd == 'RC'){
					data.claimStatusCd = 'RA' // 반품요청
				}
				data.directPaymentYn = 'Y';	// 직접결제여부 Y
				data.addPaymentTp = 'ADD_PAYMENT_TP.DIRECT_PAY'; // 직접결제
			}
			return true;
		}

}
var claimInfoRegisterUtil = {

	//--> OD_CLAIM (MASTER SET)
	odClaimSetting : function(data, paramData, partCancelYn) {
		data.odid = paramData.odid; 												//주문번호 ODID
		data.odOrderId = paramData.odOrderId; 										//주문pk
		data.odClaimId = paramData.odClaimId == null ? 0 : paramData.odClaimId; 	//주문클레임pk
		//삭제 data.orderStatusCd = paramData.orderStatusCd; 						//주문상태코드
		//삭제 data.refundStatusCd = ''; 											//환불상태
																					//추가 직접결제 필드 추가예정)
		data.claimReasonCd =$('#psClaimMallId').val().split("_")[0]; 								//클레임사유코드
		data.claimReasonMsg = $('#claimReasonMsg').val();							//클레임상세사유
		data.targetTp = fnNvl($('#targetType_0').text(), '').indexOf('구매자') > -1 ? 'B' : 'S'; 					//귀책
		//삭제 data.redeliveryType = data.reShippingType; 							//재배송구분 재배송 : R, 대체상품 : S
		if(data.refundReject == '') {
			data.rejectReasonMsg = data.rejectReasonMsg == null ? "" : data.rejectReasonMsg; //거부사유 (취소)
		} else if(data.rejectReasonMsg == '') {
			data.rejectReasonMsg = data.refundReject == null ? "" : data.refundReject; //거부사유 (반품)
		}
		data.returnsYn = data.withDraw;								//반품회수여부
		data.refundType = 'D';//$(".claimMgm__refundType").text() == '원결제 내역' ? 'D' : 'C';				//D: 원결제 내역 C : 무통장입금 (환불정보에서 환불수단 값), P : 부분취소불가
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

	//--> <추후 하드코딩 삭제 및 소스 수정> 첨부파일 [OD_CLAIM_ATTC]
	claimAttcList : function(data) {
		data.odClaimAttcId= '0';						//주문클레임 파일 PK (값이 없으면 0으로 셋팅 해주세요)
		data.originNm= '20200128file.png'; 				//업로드 원본 파일명
		data.uploadNm = 'claim_202001281735_file.png'; 	//업로드 파일명
		data.uploadPath ='c:/file/down/claim/20210128/';//업로드 경로
	},

	//--> <추후 하드코딩 삭제 및 소스 수정>  주문상세 송장번호 변수 [OD_TRACKING_NUMBER]
	claimTrackingInfo : function(data) {
		data.psShippingCompId = data.psShippingCompId == null ? "" : $('#psShippingCompId').val();	//택배사코드 (주문상세 정보)
		data.trackingNo = data.trackingNo == null ? "" :  $('#trackingNo').val();	//송장번호   (주문상세 정보)
	},
	csRefundInfo : function(data) {

		let csRefundTp = $("input[name=odStatusCS]:checked").val();
		data.csRefundTp = "CS_REFUND_TP.PAYMENT_PRICE_REFUND";		// 결제금액 환불
		data.csRefundPrice = Number($("#refundPriceCS").val().replace(/\,/g, ''));
		data.csRefundPointPrice = 0;
		if(csRefundTp == 'pointPriceRefund') {
			data.csRefundTp = "CS_REFUND_TP.POINT_PRICE_REFUND";	// 적립금 환불
			data.csRefundPrice = 0;
			data.csRefundPointPrice = Number($("#refundPriceCS").val().replace(/\,/g, ''));
		}
		data.csRefundApproveCd = "APPR_STAT.APPROVED";	// 승인
		// 금액정보가 10000원 이상일 경우
		if(Number($("#refundPriceCS").val().replace(/\,/g, '')) >= 10000) {
			// 승인요청 처리 체크 되어 있을 경우
			if($("#approveSelect").is(":checked")) {
				data.csRefundApproveCd = "APPR_STAT.REQUEST";	// 승인요청
			}
			else {
				data.csRefundApproveCd = "APPR_STAT.SAVE";	// 저장
			}
		}

		let targetGrid    = $('#apprGrid').data('kendoGrid');
		let targetGridDs  = targetGrid.dataSource;
		let targetGridArr = targetGridDs.data();

		// 승인자정보 Set
		if (targetGridArr.length == 1) {
			// 2차승인자만 있는 경우
			if (fnIsEmpty(targetGridArr[0].apprUserId) == false) {
				data.apprUserId = targetGridArr[0].apprUserId;
			}
		}
		else if (targetGridArr.length == 2) {
			// 1차승인자/2차승인자 모두있는 경우
			// 1차승인자
			if (fnIsEmpty(targetGridArr[0].apprUserId) == false) {
				data.apprSubUserId = targetGridArr[0].apprUserId;
			}
			// 2차승인자
			if (fnIsEmpty(targetGridArr[1].apprUserId) == false) {
				data.apprUserId = targetGridArr[1].apprUserId;
			}
		}
	},

	//-->주문클레임 받는 배송지 [OD_CLAIM_SHIPPING_ZONE]
	recvShippingList : function(paramData){
		var recvShippingList = new Array();
		// 상품 목록별 출고처 정보 매핑
		var claimGoodsDatas = $("#claimMgmPopupOrderGoodsGrid").data("kendoGrid").dataSource.data();
		for(var i = 0 ; i < claimGoodsDatas.length; i++) {
			let goodOrgData = {...claimGoodsDatas[i]};
			$("#startClaimShippingListArea .claimShippingList__list > .claimShippingList__listItem").each(function() {
				let warehouseId = parseInt($(this).find('input[name=urWarehouseIdList]').val().replace(/,/g,""));
				if(goodOrgData.urWarehouseId == warehouseId) {
					let recvOrgData = {};
					recvOrgData.ilGoodsId = goodOrgData.ilGoodsId;
					recvOrgData.odClaimShippingZoneId = parseInt($(this).find('input[name=odClaimShippingZoneIdList]').val().replace(/,/g,"")) == null ? 0 :parseInt($(this).find('input[name=odClaimShippingZoneIdList]').val().replace(/,/g,""));	//주문클레임 배송지 PK (값이 없으면 0으로 셋팅 해주세요)
					recvOrgData.odClaimId = paramData.odClaimId == null ? 0 : paramData.odClaimId;//주문클레임PK
					recvOrgData.odClaimDetlId = goodOrgData.odClaimDetlId== null ? '0' : goodOrgData.odClaimDetlId;			//주문클레임상세PK
					recvOrgData.urWarehouseId = warehouseId;	//출고처 PK
					recvOrgData.odOrderDetlId = goodOrgData.odOrderDetlId== null ? '0' : goodOrgData.odOrderDetlId;		//주문 상세 PK
					recvOrgData.recvNm = $(this).find('input[name=claimMgm__recvNmList]').val(); 	 			//수령인명
					recvOrgData.recvHp =  $(this).find('input[name=claimMgm__phonePrefixList]').val()+$(this).find('input[name=claimMgm__phonePrefixList]').val()+$(this).find('input[name=claimMgm__phone2List]').val();    //수령인핸드폰
					// if($(this).find('input[name=claimMgm__phonePrefixList]').val().length > 3) {
					// 	recvOrgData.recvHp = $(this).find('input[name=claimMgm__phonePrefixList]').val()+'-'+
					// 						 $(this).find('input[name=claimMgm__phonePrefixList]').val()+'-'+
					// 						 $(this).find('input[name=claimMgm__phone2List]').val();
					// } else {
					// 	recvOrgData.recvHp = $(this).find('input[name=claimMgm__phonePrefixList]').val()+
					// 						 $(this).find('input[name=claimMgm__phonePrefixList]').val()+
					// 						 $(this).find('input[name=claimMgm__phone2List]').val();
					// }
					recvOrgData.recvHp1 = $(this).find('input[name=claimMgm__phonePrefixList]').val();				//수령인핸드폰1
					recvOrgData.recvHp2 = $(this).find('input[name=claimMgm__phone1List]').val();					//수령인핸드폰2
					recvOrgData.recvHp3 = $(this).find('input[name=claimMgm__phone2List]').val();			//수령인핸드폰3
					// recvOrgData.recvTel = ''; 		//수령인연락처 없으면 공백
					recvOrgData.recvZipCd = $(this).find('input[name=claimMgm__recvZipCdList]').val();  			//수령인우편번호
					recvOrgData.recvAddr1 = $(this).find('input[name=claimMgm__recvAddr1List]').val();  	//수령인주소1
					recvOrgData.recvAddr2 = $(this).find('input[name=claimMgm__recvAddr2List]').val(); 		//수령인주소2
					// recvOrgData.recvBldNo = '111'; 				//[하드코딩]건물번호
					// recvOrgData.deliveryMsg = '문앞에 놔두세요';//[하드코딩- 삭제예정]배송요청사항
					// recvOrgData.doorMsgCd = "ACCESS_INFORMATION.FRONT_DOOR_PASSWORD"; 			//[하드코딩- 삭제예정]출입정보타입 공통코드 (DOOR_MSG_CD)
					// recvOrgData.doorMsg = '12345*'; 			//[하드코딩- 삭제예정]배송출입 현관 비밀번호
					recvShippingList.push(recvOrgData);
				}
			});
		}

		return recvShippingList;
	},

	//-->주문클레임 보내는 배송지 [OD_CLAIM_SEND_SHIPPING_ZONE]
	sendShippingInfo : function(data){
		data.odClaimSendShippingZoneId = data.odClaimSendShippingZoneId == null ? '0' : data.odClaimSendShippingZoneId;	//주문클레임 보내는 배송지 PK (값이 없으면 0으로 셋팅 해주세요)
		data.sendRecvNm= $('input[name=claimMgm__recvNm]').val(); 				//수령인명
		// if($('input[name=claimMgm__phonePrefix]').val().length > 3) {
		// 	data.sendRecvHp   = $('input[name=claimMgm__phonePrefix]').val() + '-'+
		// 						$('input[name=claimMgm__phone1]').val()+'-'+
		// 						$('input[name=claimMgm__phone2]').val();
		// } else {
		// 	data.sendRecvHp =  $('input[name=claimMgm__phonePrefix]').val()+$('input[name=claimMgm__phone1]').val()+$('input[name=claimMgm__phone2]').val();
		// }
		data.sendRecvHp =  $('input[name=claimMgm__phonePrefix]').val()+$('input[name=claimMgm__phone1]').val()+$('input[name=claimMgm__phone2]').val();	//수령인핸드폰
		data.sendRecvHp1 = $('input[name=claimMgm__phonePrefix]').val();  				//수령인핸드폰1
		data.sendRecvHp2=  $('input[name=claimMgm__phone1]').val(); 					//수령인핸드폰2
		data.sendRecvHp3 = $('input[name=claimMgm__phone2]').val(); 	 				//수령인핸드폰3
		data.sendRecvTel= '0222521114'; //[하드코딩]수령인연락처
		data.sendRecvZipCd= $('input[name=claimMgm__recvZipCd]').val(); 				//수령인우편번호
		data.sendRecvAddr1 = $('input[name=claimMgm__recvAddr1]').val(); 				//수령인주소1
		data.sendRecvAddr2= $('input[name=claimMgm__recvAddr2]').val(); 				//수령인주소2
		data.sendRecvBldNo= '222'; 		//[하드코딩]건물번호
		data.sendDeliveryMsg = $('input[name=claimMgm__deliveryMsg]').val() == '직접입력' ? $('input[name=claimMgm__deliveryMsgEtc]').val() : $('input[name=claimMgm__deliveryMsg]').val();			//배송요청사항 직접입력(구분하기)
		data.sendDoorMsgCd= $('input[name=claimMgm__doorMsgCd]').val(); 				//출입정보타입 공통코드 (DOOR_MSG_CD)
		data.sendDoorMsg = $('input[name=claimMgm__doorMsgCd]').val() == "ACCESS_INFORMATION.FRONT_DOOR_PASSWORD" || "ACCESS_INFORMATION.ETC" ? $('input[name=claimMgm__doorMsg]').val() : null; 	//배송출입 현관 비밀번호
	},

	//-->주문상세 [OD_CLAIM_DETL]
	goodsInfoList : function(data, paramData) {

		var goodsInfoList = new Array();
		var claimGoodsDatas = $("#claimMgmPopupOrderGoodsGrid").data("kendoGrid").dataSource.data();

//		for(var i = 0 ; i < $('#claimMgmPopupOrderGoodsGrid').data("kendoGrid")._data.length; i++){
		for(var i = 0 ; i < claimGoodsDatas.length; i++){
			let goodOrgData = {...claimGoodsDatas[i]};
			if(goodOrgData.odOrderDetlSeq == 0 && data.putOrderStatusCd != 'CS') {
				continue;
			}
			//let gridData = {...claimGoodsDatas[i]};
			goodOrgData.odClaimDetlId = goodOrgData.odClaimDetlId == null ? '0' : goodOrgData.odClaimDetlId; //주문클레임 상세 PK
			//추가 >
			goodOrgData.orderStatusCd = paramData.orderStatusCd; 	//주문클레임 상세 PK
			goodOrgData.claimGoodsYn ='Y';
			//추가 >
			if(fnNvl($("#bosClaimReasonListDiv").css("display")) != "" && $("#bosClaimReasonListDiv").css("display") != "none") {
				goodOrgData.redeliveryType = data.reShippingType; 		//재배송구분 재배송 : R, 대체상품 : S
			}
//			goodOrgData.odOrderDetlId = gridData.odOrderDetlId;		//주문상세 PK '1005'
//			goodOrgData.odOrderDetlSeq = gridData.odOrderDetlSeq;						//주문상세 순번(라인번호) 주문번호에 대한 순번
//			goodOrgData.odOrderDetlStepId = gridData.odOrderDetlStepId;				//[하드코딩-추가]주문상세 정렬 키
//			goodOrgData.odOrderDetlParentId = gridData.odOrderDetlParentId;					//[하드코딩-추가]주문상세 부모 id
//			goodOrgData.claimCnt = gridData.claimCnt;								//클레임처리수량
			if(data.putOrderStatusCd == 'CS') {
				goodOrgData.claimCnt = 0;
			}
			goodOrgData.psClaimBosSupplyId = $('#psClaimBosSupplyId_'+i).text();
			goodOrgData.psClaimBosId = $("#psClaimBosId_"+i).text();
			goodOrgData.bosClaimLargeId =  claimRegisterUtil.removeComma($("#lclaimCtgryId_"+i).val()); //클레임사유(대)
			goodOrgData.bosClaimMiddleId = claimRegisterUtil.removeComma($("#mclaimCtgryId_"+i).val()); //클레임사유(중)
			goodOrgData.bosClaimSmallId = claimRegisterUtil.removeComma($("#sclaimCtgryId_"+i).val()); //클레임사유(귀책처)
			// goodOrgData.addPaymentShippingPrice = gridData.addPaymentShippingPrice;			//추가결제배송비
			goodOrgData.ilGoodsShippingTemplateId = goodOrgData.ilShippingTmplId;				//상품명 : IL_GOODS.GOODS_NM
//			goodOrgData.goodsNm = gridData.goodsNm;				//상품명 : IL_GOODS.GOODS_NM
//			goodOrgData.salePrice = gridData.recommendedPrice;		//판매가
//			goodOrgData.psShippingCompId = gridData.psShippingCompId;					//!!!!!택배사 설정 PK
//			goodOrgData.trackingNo = gridData.trackingNo;				//!!!!!개별송장번호
//			goodOrgData.shippingDt = gridData.shippingDt;		 			//!!!!!출고예정일 일자
//			goodOrgData.deliveryDt = gridData.deliveryDt;					//!!!!!도착예정일 일자
//			goodOrgData.orderIfDt = gridData.orderIfDt;			//!!!!!주문I/F일 일자
//			goodOrgData.urWarehouseId = gridData.urWarehouseId;		// 출고처ID(출고처PK)
//			goodOrgData.ilGoodsId = gridData.ilGoodsId;				// 상품ID(상품PK)
//			goodOrgData.goodsTpCd = gridData.goodsTpCd;				// 상품유형코드
//			goodOrgData.storageTypeCd = gridData.storageTypeCd;		//상품보관방법 ilItem.storageMethodTp 공통코드(erpStorageType) cool: 냉장, frozen: 냉동, room: 실온, ordinary: 상온, etc: 기타, notDefined: 미정
//			goodOrgData.goodsDailyTp = gridData.goodsDailyTp;      	//일일상품 유형(goodsDailyTp : greenjuice/babymeal/eatsslim )
//			goodOrgData.orderStatusDeliTp = gridData.orderStatusDeliTp; 	//주문상태 배송유형 공통코드: orderStatusDeliTp
//			goodOrgData.itemBarcode = gridData.itemBarcode;       					//품목바코드 : ilItem.itemBarcode
//			goodOrgData.ilItemCd = gridData.ilItemCd;          						//품목코드 pk : ilItem.ilItemCd
//			goodOrgData.orderCnt = gridData.orderCnt;							// 주문수량
//			goodOrgData.cancelCnt = gridData.cancelCnt;							//주문취소수량
//			goodOrgData.goodsCycleTp = gridData.goodsCycleTp;					// 일일 배송주기코드
//			goodOrgData.targetOdOrderDetlId = gridData.targetOdOrderDetlId;
//			goodOrgData.reShipping = gridData.reShipping;
			var goodsNm = new String(goodOrgData.goodsNm);
			if(goodsNm.indexOf("<button") > -1) {
				goodOrgData.odOrderDetlSeq = 0;
				goodOrgData.goodsNm = goodsNm.substr(0, goodsNm.indexOf("<button"));
			}

			goodsInfoList.push(goodOrgData);
		}
		return goodsInfoList;
	},

	//-->클레임BOS사유 - 주문상세 [OD_CLAIM_DETL]
	goodsInfoListForBosClaimReason : function(data, paramData) {

		var goodsInfoList = new Array();
		var claimGoodsDatas = $("#claimMgmPopupOrderGoodsGrid").data("kendoGrid").dataSource.data();
		let goodsCheckFlag = $("#eachGoodsReasonSelect").is(":checked");

		for(var i = 0 ; i < claimGoodsDatas.length; i++){
			let goodOrgData = {...claimGoodsDatas[i]};
			if(goodsCheckFlag) {

				if(goodOrgData.odOrderDetlSeq == 0) {
					continue;
				}
				goodOrgData.odClaimDetlId = goodOrgData.odClaimDetlId == null ? '0' : goodOrgData.odClaimDetlId; //주문클레임 상세 PK
				//추가 >
				goodOrgData.orderStatusCd = paramData.orderStatusCd; 	//주문클레임 상세 PK
				goodOrgData.claimGoodsYn ='Y';
				//추가 >
				if(fnNvl($("#bosClaimReasonListDiv").css("display")) != "" && $("#bosClaimReasonListDiv").css("display") != "none") {
					goodOrgData.redeliveryType = data.reShippingType; 		//재배송구분 재배송 : R, 대체상품 : S
				}
				if(data.putOrderStatusCd == 'CS') {
					goodOrgData.claimCnt = 0;
				}
				goodOrgData.psClaimBosSupplyId = $('#psClaimBosSupplyId_'+i).text();
				goodOrgData.psClaimBosId = $("#psClaimBosId_"+i).text();
				goodOrgData.bosClaimLargeId =  claimRegisterUtil.removeComma($("#lclaimCtgryId_"+i).val()); //클레임사유(대)
				goodOrgData.bosClaimMiddleId = claimRegisterUtil.removeComma($("#mclaimCtgryId_"+i).val()); //클레임사유(중)
				goodOrgData.bosClaimSmallId = claimRegisterUtil.removeComma($("#sclaimCtgryId_"+i).val()); //클레임사유(귀책처)
				goodOrgData.ilGoodsShippingTemplateId = goodOrgData.ilShippingTmplId;				//상품명 : IL_GOODS.GOODS_NM
				goodOrgData.claimStatusCd = paramData.orderStatusCd; 								// 현 클레임상태 그대로 (only ForBosClaimReason)

				//처리이력메세지 (only ForBosClaimReason)
				var histMsg = claimGoodsDatas[0].bosClaimLargeNm + ' > ' + claimGoodsDatas[0].bosClaimMiddleNm + ' > ' + claimGoodsDatas[0].bosClaimSmallNm
				histMsg	+= ' ➜ ' + $("#lclaimCtgryId_"+i).closest('span')[0].innerText + ' > ' + $("#mclaimCtgryId_"+i).closest('span')[0].innerText + ' > ' + $("#sclaimCtgryId_"+i).closest('span')[0].innerText;

				goodOrgData.histMsg = histMsg;

				var goodsNm = new String(goodOrgData.goodsNm);
				if(goodsNm.indexOf("<button") > -1) {
					goodOrgData.odOrderDetlSeq = 0;
					goodOrgData.goodsNm = goodsNm.substr(0, goodsNm.indexOf("<button"));
				}

				goodsInfoList.push(goodOrgData);

			}
			// 상품별 BOS클레임 사유 체크하지 않았을 경우
			else {

				if(goodOrgData.odOrderDetlSeq == 0) {
					continue;
				}
				goodOrgData.odClaimDetlId = goodOrgData.odClaimDetlId == null ? '0' : goodOrgData.odClaimDetlId; //주문클레임 상세 PK
				//추가 >
				goodOrgData.orderStatusCd = paramData.orderStatusCd; 	//주문클레임 상세 PK
				goodOrgData.claimGoodsYn ='Y';
				//추가 >
				if(fnNvl($("#bosClaimReasonListDiv").css("display")) != "" && $("#bosClaimReasonListDiv").css("display") != "none") {
					goodOrgData.redeliveryType = data.reShippingType; 		//재배송구분 재배송 : R, 대체상품 : S
				}
				if(data.putOrderStatusCd == 'CS') {
					goodOrgData.claimCnt = 0;
				}
				goodOrgData.psClaimBosSupplyId = $('#psClaimBosSupplyId_'+0).text();
				goodOrgData.psClaimBosId = $("#psClaimBosId_"+0).text();
				goodOrgData.bosClaimLargeId =  claimRegisterUtil.removeComma($("#lclaimCtgryId_"+0).val()); //클레임사유(대)
				goodOrgData.bosClaimMiddleId = claimRegisterUtil.removeComma($("#mclaimCtgryId_"+0).val()); //클레임사유(중)
				goodOrgData.bosClaimSmallId = claimRegisterUtil.removeComma($("#sclaimCtgryId_"+0).val()); //클레임사유(귀책처)
				goodOrgData.ilGoodsShippingTemplateId = goodOrgData.ilShippingTmplId;				//상품명 : IL_GOODS.GOODS_NM
				goodOrgData.claimStatusCd = paramData.orderStatusCd; 								// 현 클레임상태 그대로 (only ForBosClaimReason)

				//처리이력메세지 (only ForBosClaimReason)
				var histMsg = claimGoodsDatas[0].bosClaimLargeNm + ' > ' + claimGoodsDatas[0].bosClaimMiddleNm + ' > ' + claimGoodsDatas[0].bosClaimSmallNm
				histMsg	+= ' ➜ ' + $("#lclaimCtgryId_"+0).closest('span')[0].innerText + ' > ' + $("#mclaimCtgryId_"+0).closest('span')[0].innerText + ' > ' + $("#sclaimCtgryId_"+0).closest('span')[0].innerText;

				goodOrgData.histMsg = histMsg;

				var goodsNm = new String(goodOrgData.goodsNm);
				if(goodsNm.indexOf("<button") > -1) {
					goodOrgData.odOrderDetlSeq = 0;
					goodOrgData.goodsNm = goodsNm.substr(0, goodsNm.indexOf("<button"));
				}

				goodsInfoList.push(goodOrgData);
			}
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
	//-->출고정보
	releaseList : function(data) {

		var shippingRowCnt			= $("#reShippingTbody tr.reShippingRowClass").length;
		var shippingRowChceckedCnt	= $("#reShippingTbody input[type='radio']:checked").length;

		if(shippingRowCnt != shippingRowChceckedCnt){
			fnKendoMessage({message: "출고지시일을 선택해주세요."});
			return false;
		}

		var reDeliveryType = $("input[name=reShippingType]:checked").val();
		var goodsList = data.goodsInfoList;
		var newGoodsList = new Array();
		for(var i=0; i<goodsList.length; i++) {
			if(goodsList[i].claimCnt < 1) {
				continue;
			}
			if(goodsList[i].reShipping != true) {
				goodsList[i].redeliveryIndex = i;
				newGoodsList.push(goodsList[i]);
				// 재배송일경우 동일 상품으로 1로우 추가
				if(reDeliveryType == 'R') {
					var addGoodsInfo = {...goodsList[i]};
					addGoodsInfo.odOrderDetlId = 0;
					addGoodsInfo.redeliveryType = '';
					addGoodsInfo.orderCnt = goodsList[i].claimCnt; // goodsList[i].orderCnt - goodsList[i].cancelCnt - goodsList[i].claimCnt;
					addGoodsInfo.orgOrderCnt = goodsList[i].claimCnt;
					addGoodsInfo.reShipping = true;
					addGoodsInfo.odOrderDetlDepthId = 3;
					if(goodsList[i].odOrderDetlDepthId == 3) {
						addGoodsInfo.odOrderDetlParentId = goodsList[i].odOrderDetlParentId;
					}
					else {
						addGoodsInfo.odOrderDetlParentId = goodsList[i].odOrderDetlId;
					}
					newGoodsList.push(addGoodsInfo);
					goodsList[i].redeliveryType = reDeliveryType;
				}
				// 대체배송일 경우
				// targetOdOrderDetlId가 동일한 상품은 redeliveryType 'R'
				// 대체배송 선택하지 않은 상품의 경우는 redeliveryType 'S'
				else {
					goodsList[i].redeliveryType = reDeliveryType;
					var matchCnt = 0;
					let odOrderDetlId = goodsList[i].odOrderDetlId;
					for(var j=(i + 1); j<goodsList.length; j++) {
						if(goodsList[j].reShipping == true &&
							odOrderDetlId == goodsList[j].targetOdOrderDetlId) {
							goodsList[j].redeliveryType = reDeliveryType;
							goodsList[j].reShipping = true;
							goodsList[j].odOrderDetlId = 0;
							goodsList[j].odOrderDetlDepthId = 3;
							goodsList[j].redeliveryIndex = i;
							if(goodsList[i].odOrderDetlDepthId == 3) {
								goodsList[j].odOrderDetlParentId = goodsList[i].odOrderDetlParentId;
							}
							else {
								goodsList[j].odOrderDetlParentId = goodsList[i].odOrderDetlId;
							}
							newGoodsList.push(goodsList[j]);
							matchCnt++;
						}
					}
					// 매치되는 상품이 없을 경우 배송타입 - 'R' 처리
					if(matchCnt < 1) {
						fnKendoMessage({message: "(" + goodsList[i].goodsNm + ") 상품의 대체 상품을 선택 해주세요."});
						return false;
					}
				}
			}
		}

		data.goodsInfoList = newGoodsList;
		goodsList = data.goodsInfoList;

		var dateValidFlag = true;
		$("#reShippingTbody > tr").each(function() {
			var index = $(this).find("input[type=radio]:checked").val();
			var urWarehouseId = 0;
			var orderDt = '';
			var shippingDt = '';
			var deliveryDt = '';
			if(index == 'typing'){
				shippingDt = $(this).find("table thead > tr:eq(1) input[type=text]").val();
				orderDt = shippingDt;
				deliveryDt = $(this).find("table thead > tr:eq(2) input[type=text]").val();
			}
			else {
				orderDt	= $(this).find("table thead > tr:eq(1) #forwardingScheduledDate_" + index).data("orderdate");
				shippingDt = $(this).find("table thead > tr:eq(1) #forwardingScheduledDate_" + index).attr("value");
				deliveryDt = $(this).find("table thead > tr:eq(2) #arrivalScheduledDate_" + index).attr("value");
			}
			urWarehouseId = $(this).find("table thead > tr:eq(1) input[type=text]").data("warehouseid");
			for(var j=0; j<goodsList.length; j++) {
				if(goodsList[j].odOrderDetlId == 0 && goodsList[j].urWarehouseId == urWarehouseId) {
					data.goodsInfoList[j].orderIfDt = orderDt;
					data.goodsInfoList[j].shippingDt = shippingDt;
					data.goodsInfoList[j].deliveryDt = deliveryDt;
				}
			}
			if(fnNvl(shippingDt) == '' || fnNvl(deliveryDt) == '' || fnNvl(shippingDt).replace(/\-/g, '') == '' || fnNvl(deliveryDt).replace(/\-/g, '') == '') {
				fnKendoMessage({message: "출고지시일을 선택해주세요."});
				dateValidFlag = false;
				return false;
			}
		});

		if(!dateValidFlag) {
			return false;
		}

		return true;
		// 출고지시일 날짜 직접 선택
//		if(index == 'typing'){
//			data.shippingDt = $("#datepicker").val();
//			data.deliveryDt = data.orderIfDate;
//		}else{
//			data.shippingDt =  $("#forwardingScheduledDate_"+index).attr("value");
//			data.deliveryDt =  $("#arrivalScheduledDate_"+index).attr("value");
//		}
//		return true;
	},
	// CS 상태 및 CS 환불 금액 정보 Set
	csStatusPriceInfo: function(data) {
		data.claimStatusCd = 'CS'; //CS환불
		data.claimStatusTp = 'CLAIM_STATUS_TP.CS_REFUND';
		var csRefundPrice = Number($("#refundPriceCS").val().replace(/\,/g, ''));
		if (csRefundPrice >= 10000) {
			// if($('#approveSelect').is(":checked")){
				data.csRefundApproveCd = 'CS_REFUND_APPR_CD.REQUEST';
			// } else {
			// 	data.csRefundApproveCd = 'CS_REFUND_APPR_CD.SAVE';
			// }
		}
		else {
			data.csRefundApproveCd = 'CS_REFUND_APPR_CD.APPROVED';
		}

		let targetGrid    = $('#apprGrid').data('kendoGrid');
		let targetGridDs  = targetGrid.dataSource;
		let targetGridArr = targetGridDs.data();

		// 승인자정보 Set
		if (csRefundPrice >= 10000) {
			if (targetGridArr.length == 1) {
				// 2차승인자만 있는 경우
				if (fnIsEmpty(targetGridArr[0].apprUserId) == false) {
					data.apprUserId = targetGridArr[0].apprUserId;
				}
			} else if (targetGridArr.length == 2) {
				// 1차승인자/2차승인자 모두있는 경우
				// 1차승인자
				if (fnIsEmpty(targetGridArr[0].apprUserId) == false) {
					data.apprSubUserId = targetGridArr[0].apprUserId;
				}
				// 2차승인자
				if (fnIsEmpty(targetGridArr[1].apprUserId) == false) {
					data.apprUserId = targetGridArr[1].apprUserId;
				}
			}
		}

		data.refundPrice = csRefundPrice;
		data.psClaimMallId = data.claimReasonCd;
	}
}

var zipCodeUtil = {
	fnDaumPostCode: function(postcode, address, detailAddress,buildingCode) {
		new daum.Postcode({
			oncomplete: function(data) {
				// 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

				// 각 주소의 노출 규칙에 따라 주소를 조합한다.
				// 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
				var addr = ''; // 주소 변수
				var extraAddr = ''; // 참고항목 변수

				//도로명 주소가 빈값일 경우 지번주소로 저장
				if (data.roadAddress !== '') {
					addr = data.roadAddress;
				} else {
					addr = data.jibunAddress;
				}

				// 도로명 주소가 빈값이 아닌 경우 참고항목을 조합
				if(data.roadAddress !== ''){
					// 법정동명이 있을 경우 추가한다. (법정리는 제외)
					// 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
					if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
						extraAddr += data.bname;
					}
					// 건물명이 있고, 공동주택일 경우 추가한다.코스트코코리아 양재점
					if(data.buildingName !== '' && data.apartment === 'Y'){
						extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
					}
					// 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
					if(extraAddr !== ''){
						extraAddr = ' (' + extraAddr + ')';
					}

					// 주소변수에 참고항목 추가
					if(extraAddr !== ''){
						addr += extraAddr;
					}
				}

				// 우편번호와 주소 정보를 해당 필드에 넣는다.
				document.getElementById(postcode).value = data.zonecode;
				document.getElementById(address).value = addr;
				if (buildingCode ==null ||buildingCode ==undefined || buildingCode =="" ){
					buildingCode = "buildingCode";
				}
				else
				{
					document.getElementById(buildingCode).value = data.buildingCode;
				}

				$("#" + postcode).change();
				// 커서를 상세주소 필드로 이동한다.
				document.getElementById(detailAddress).focus();
			}
		}).open();
	}
};

var claimSearchUtil = {
  // 기간 검색
  dateSearch: function() {
      fnKendoDatePicker({
        id: 'startDate',
        format: 'yyyy-MM-dd',

      });

      fnKendoDatePicker({
          id: 'endDate',
          format: 'yyyy-MM-dd',
          btnStyle: true,
          btnStartId: 'startDate',
          btnEndId: 'endDate',
          nextDate: true,
          minusCheck: true,
      });
  },
  // 단일조건 조건
  searchCondition: function() {
    fnKendoDropDownList(
      {
          id : "searchType",
          data : [
                  {"CODE" : "orderNo", "NAME" : "주문번호"},
                  {"CODE" : "orderName", "NAME" : "주문자명"},
                  {"CODE" : "orderId", "NAME" : "주문자ID"},
                  {"CODE" : "", "NAME" : "상품코드"},
                  {"CODE" : "", "NAME" : "품목코드"},
                  {"CODE" : "", "NAME" : "품목바코드"},
          ],
          valueField : "CODE",
          textField : "NAME",
          blank : "선택해주세요."
      });
  },
  // 복수조건/단일조건 탭 선택
  tabSelector: function() {
    fnTagMkRadio({
      id: 'selectConditionType',
      tagId: 'selectConditionType',
      chkVal: 'multiSection',
      data: [{
          CODE: "multiSection",
          NAME: "복수조건 검색",
      },{
          CODE: "singleSection",
          NAME: "단일조건 검색",
      }],
      change: function(e) {
        const $tabs = $("tr[data-tab]");
        const tab = $(e.target).val();

        $tabs.hide();
        $("[data-tab="+ tab +"]").show();
      }
    });
  }
}

var claimFncs = {
  getCode: function(id) {
    let input = $("#" + id).val();
    let query = "";

    if( input && input.length ) {

      input = input.split(",");

      query = input.filter(function(i){
        return i && i.length;
      }).join(",");
    }

    return query;
  },
}