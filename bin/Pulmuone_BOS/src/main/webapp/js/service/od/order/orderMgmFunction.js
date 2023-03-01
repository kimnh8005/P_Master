/**-----------------------------------------------------------------------------
 * description 		 : 주문 상세 관리 관련 함수
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.25		강윤경   		최초생성
 * @ 2021.01.27		최윤지	  		추가작성
 * **/

var paymentUrEmployeeYn = 'N';
var payTypeStr = '';
var payTypeCode = '';
var orderMgmFunctionUtil = {

	// 상품 추가 정보
	goodNmAddTextView : function(row) {
		let addSubTxt = "";
		let depthId     = row.odOrderDetlDepthId;
		let blankStr    = "";

		if (depthId > 1){
			blankStr = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ㄴ ";
		}
		if (row.goodsTpCd == "GOODS_TYPE.DAILY" && row.promotionTp == "CART_PROMOTION_TP.GREENJUICE_SELECT"){

			addSubTxt += blankStr + "&nbsp;&nbsp; 배송기간 : " + row.goodsCycleTermTp;
			addSubTxt += "<br/>"+blankStr+"&nbsp;&nbsp; 배송주기 : " + row.goodsCycleTp;
			if (row.monCnt > 0){ addSubTxt += "<br/>"+blankStr+"&nbsp;&nbsp; 배송요일 : 월요일"; }
			if (row.tueCnt > 0){ addSubTxt += "<br/>"+blankStr+"&nbsp;&nbsp; 배송요일 : 화요일"; }
			if (row.wedCnt > 0){ addSubTxt += "<br/>"+blankStr+"&nbsp;&nbsp; 배송요일 : 수요일"; }
			if (row.thuCnt > 0){ addSubTxt += "<br/>"+blankStr+"&nbsp;&nbsp; 배송요일 : 목요일"; }
			if (row.friCnt > 0){ addSubTxt += "<br/>"+blankStr+"&nbsp;&nbsp; 배송요일 : 금요일"; }
			addSubTxt += (stringUtil.getString(row.schDeliveryDt, "") != "") ? "<br/>"+blankStr + "&nbsp;&nbsp; 배송일 : " + row.schDeliveryDt : "";

		} else if (row.goodsTpCd == "GOODS_TYPE.DAILY" && row.promotionTp == ""){
			let arrDay =  [];

			if (row.monCnt > 0){ arrDay.push("월"); }
			if (row.tueCnt > 0){ arrDay.push("화"); }
			if (row.wedCnt > 0){ arrDay.push("수"); }
			if (row.thuCnt > 0){ arrDay.push("목"); }
			if (row.friCnt > 0){ arrDay.push("금"); }

			addSubTxt += (stringUtil.getString(row.goodsCycleTermTp, "") != "") ? blankStr + "&nbsp;&nbsp;ㄴ 배송기간 : " + row.goodsCycleTermTp : "";
			addSubTxt += (stringUtil.getString(row.goodsCycleTp, "") != "") ? "<br/>"+blankStr+"&nbsp;&nbsp;ㄴ 배송주기 : " + row.goodsCycleTp : "";
			addSubTxt += (stringUtil.getString(arrDay.join(), "") != "") ? "<br/>"+blankStr+"&nbsp;&nbsp;ㄴ 배송요일 : " + arrDay.join() : "";
			addSubTxt += (stringUtil.getString(row.schDeliveryDt, "") != "") ? "<br/>"+blankStr + "&nbsp;&nbsp;ㄴ 배송일 : " + row.schDeliveryDt : "";

		}
		//베이비밀
		if(row.goodsDailyTp == "GOODS_DAILY_TP.BABYMEAL") {
			if (row.dailyBulkYn == 'Y') {//일괄배송일 경우
				addSubTxt += blankStr + "&nbsp;&nbsp;ㄴ 배송유형 : " + ((row.dailyBulkYn == "Y") ? "일괄배송" : "일일배송");
				addSubTxt += "<br/>" + blankStr + "&nbsp;&nbsp;ㄴ 식단유형 : " + ((row.allergyYn == "N") ? "일반식단" : "알러지대체식단");
				addSubTxt += "<br/>" + blankStr + "&nbsp;&nbsp;ㄴ 세트수량 : " + ((row.setCnt != "") ? row.setCnt : 0) + "세트";
			} else {
				addSubTxt += "<br/>" + blankStr + "&nbsp;&nbsp;ㄴ 알러지여부 : " + ((row.allergyYn == "Y") ? "예" : "아니오");
			}
		}
		return addSubTxt;
	},
	//-->상품정보 그리드
	initOrderGoodsGrid : function(odOrderId){
		orderGoodsGridDs = fnGetDataSource({
			async: false,
			url  : '/admin/order/getOrderDetailGoodsList?odOrderId='+odOrderId
		});
		//상품/결제&주문자정보 Tab
		orderGoodsGridOpt = {
			dataSource: orderGoodsGridDs,
			navigatable : true,
			scrollable : true,
			columns : orderMgmGridUtil.orderGoodsList()
		};
		/*
        orderGoodsOrderGridOpt = {
                dataSource: orderGoodsGridDs,
                navigatable : true,
                scrollable : true,
                columns : orderMgmGridUtil.orderGoodsOrderList()
        };
        */
		//배송정보 Tab
		/*orderGoodsShippingGridOpt = {
                dataSource: orderGoodsGridDs,
                navigatable : true,
                scrollable : true,
                columns : orderMgmGridUtil.orderGoodsShippingList()
        };*/

		//상품/결제정보 > 상품정보
		$('#orderGoodsGrid').empty();
		orderGoodsGrid = $('#orderGoodsGrid').initializeKendoGrid( orderGoodsGridOpt ).cKendoGrid();

		orderGoodsGrid.bind("dataBound", function(e) {
			//내맘대로 주문의 경우 구성 상품이 1개라도 배송준비중으로 변경 된 경우 주문I/F일자 변경 X
			if($( '#orderGoodsGrid' ).find('.greenSelectDiv').length > 0) {
				$(".ifDayChangeDiv").parent().attr('name', 'ifDayChangeNot');
				$(".ifDayChangeDiv").css('background', '');
				$(".ifDayChangeDiv").css('cursor', '');
			}

			//셀고정시 내용물에 따라 td height가 변경되어 높이값 지정을 주석처리
			//$( '#orderGoodsGrid' ).find("tr").css("height", "30px");
			mergeGridRows('orderGoodsGrid', ['shippingPrice', 'warehouseNm', 'trackingNo'],['shippingPrice', 'warehouseNm', 'trackingNo']);

		});
		orderGoodsGridDs.query();
		return orderGoodsGridDs._data.length;
	},
	//--> 클레임상품정보 그리드
	initClaimGoodsGrid : function(odOrderId, checkedClaimRowId, gridSelect1) {
		claimGoodsGridDs = fnGetDataSource({
			url      : '/admin/order/getOrderDetailClaimGoodsList?odOrderId='+odOrderId
		});
		claimGoodsGridOpt = {
			dataSource: claimGoodsGridDs,
			navigatable : true,
			scrollable : true,
			columns : orderMgmGridUtil.claimGoodsList()
		};
		claimGoodsGrid = $('#claimGoodsGrid').initializeKendoGrid( claimGoodsGridOpt ).cKendoGrid();

		claimGoodsGrid.bind("dataBound", function() {
			$( '#claimGoodsGrid' ).find("tr").css("height", "60px");

			mergeGridRows('claimGoodsGrid',['odClaimId', 'claimDt', 'reasonMsg', 'bosClaimLargeNm', 'shippingPrice', 'discountPrice', 'refundPointPrice', 'refundPrice', 'warehouseNm', 'trackingNo'],['odClaimId'] );

			// 접수일/처리일, 클레임번호, 쇼핑몰클레임사유, BOS클레임사유, 배송비 rowspan
			//mergeGridRows('claimGoodsGrid',['odClaimId','claimDt', 'reasonMsg', 'bosClaimLargeNm', 'shippingPrice', 'warehouseNm', 'trackingNo'],['odClaimId', 'claimDt', 'reasonMsg','bosClaimLargeNm', 'shippingPrice', 'claimStatus', 'warehouseNm', 'trackingNo'] );

		});

		claimGoodsGridDs.query();
	},
	//-->결제정보
	payInfoList : function(odOrderId) {
		fnAjax({
			url     : '/admin/order/getOrderDetailPayList',
			params  : {odOrderId : odOrderId},
			success :
				function( data ){

					$("#tbodyPaymentArea").empty();

					//결제상세정보
					let paymentDetailObj = new Object();
					paymentDetailObj.firstDiv = "주문/결제일자";
					paymentDetailObj.secondDiv = "총 주문금액";
					paymentDetailObj.thirdDiv = "총 상품금액";
					paymentDetailObj.fourthDiv = "배송비";
					paymentDetailObj.fifthDiv = "쿠폰할인";
					paymentDetailObj.sixthDiv = "즉시할인";
					paymentDetailObj.seventhDiv = "임직원"+"</br>"+"지원금";
					paymentDetailObj.eighthDiv = "결제"+"</br>"+"대상금액";
					paymentDetailObj.ninthDiv = "적립금사용";
					paymentDetailObj.tenthDiv = "최종"+"</br>"+"결제금액";
					paymentDetailObj.eleventhDiv = "결제상태";
					paymentDetailObj.twelfthDiv = "결제방법";
					paymentDetailObj.thirteenthDiv = "결제기관(PG)";

					//환불정보
					let paymentRefundObj = new Object();
					paymentRefundObj.firstDiv = "요청/환불일자";
					paymentRefundObj.secondDiv = "총 환불금액";
					paymentRefundObj.thirdDiv = "총 환불"+"</br>"+"상품금액";
					paymentRefundObj.fourthDiv = "배송비";
					paymentRefundObj.fifthDiv = "쿠폰할인"+"</br>"+"차감";
					paymentRefundObj.sixthDiv = "즉시할인";
					paymentRefundObj.seventhDiv = "임직원"+"</br>"+"지원금";
					paymentRefundObj.eighthDiv = "환불금액";
					paymentRefundObj.ninthDiv = "환불 적립금";
					paymentRefundObj.tenthDiv = "결제수단"+"</br>"+"환불금액";
					paymentRefundObj.eleventhDiv = "클레임상태";
					paymentRefundObj.twelfthDiv = "환불수단";
					paymentRefundObj.thirteenthDiv = "결제기관(PG)";

					//추가결제정보
					let addPaymentObj = new Object();
					addPaymentObj.firstDiv = "추가 결제 일자";
					addPaymentObj.secondDiv = "추가결제1";
					addPaymentObj.thirdDiv = "추가 결제금액";
					addPaymentObj.fourthDiv = "추가결제2";
					addPaymentObj.fifthDiv = "추가결제2";
					addPaymentObj.sixthDiv = "추가결제 상태";
					addPaymentObj.seventhDiv = "추가결제3";
					addPaymentObj.eighthDiv = "추가결제3";
					addPaymentObj.ninthDiv = "결제방법";
					addPaymentObj.tenthDiv = "추가결제4";
					addPaymentObj.eleventhDiv = "추가결제4";
					addPaymentObj.twelfthDiv = "결제기관(PG)";
					addPaymentObj.thirteenthDiv = "추가결제5";

					//추가환불정보
					let addRefundObj = new Object();
					addRefundObj.firstDiv = "추가 환불 일자";
					addRefundObj.secondDiv = "추가결제1";
					addRefundObj.thirdDiv = "환불금액";
					addRefundObj.fourthDiv = "추가결제2";
					addRefundObj.fifthDiv = "추가결제2";
					addRefundObj.sixthDiv = "클레임 상태";
					addRefundObj.seventhDiv = "추가결제3";
					addRefundObj.eighthDiv = "추가결제3";
					addRefundObj.ninthDiv = "환불방법";
					addRefundObj.tenthDiv = "추가결제4";
					addRefundObj.eleventhDiv = "추가결제4";
					addRefundObj.twelfthDiv = "결제기관(PG)";
					addRefundObj.thirteenthDiv = "추가결제5";

					let tpl = $("#divPaymentArea").clone();
					let tplObj = null;
					let addTplObj = null;

					//주문자정보 > 결제정보 [총결제금액] = [결제수단][실결제금액]+[적립금사용금액]
					var buyerPayment = '';
					buyerPayment += fnNumberWithCommas(stringUtil.getInt(data.payDetailList[0].paymentPrice) + stringUtil.getInt(data.payDetailList[0].pointPrice))+" 원";
					buyerPayment += ' = '+ '[ '+ data.payDetailList[0].payType + ' ] ';
					buyerPayment += fnNumberWithCommas(data.payDetailList[0].paymentPrice)+" 원";

					if(stringUtil.getInt(data.payDetailList[0].pointPrice) != 0) {
						buyerPayment += ' + ' +fnNumberWithCommas(data.payDetailList[0].pointPrice)+" 원";

					}

					//결제 한건에 주문번호가 여러개 존재하면 일괄결제금액으로 추가 노출
					if(stringUtil.getInt(data.payDetailList[0].paymentPriceMaster) != 0) {
						buyerPayment += "</br>" + "일괄결제금액 : " + fnNumberWithCommas(data.payDetailList[0].paymentPriceMaster) + " 원";
					}

					$('#buyerPayment').html(buyerPayment);

					// 결제상세정보
					let rows = data.payDetailList;
					for(let i=0; i<rows.length; i++){
						tplObj = tpl;
						payTypeStr = rows[i].payType;
						payTypeCode = rows[i].payTypeCd;
						//HEADER START
						//즉시할인내역 노출
						if((rows[i].directPrice > 0 || rows[i].discountEmployeePrice > 0)) {
							$('#immediateDiscountBtn').show();
						}else {
							$('#immediateDiscountBtn').hide();
						}
						tplObj.find(".firstDiv").html(paymentDetailObj.firstDiv);
						tplObj.find(".secondDiv").html(paymentDetailObj.secondDiv);
						tplObj.find(".thirdDiv").html(paymentDetailObj.thirdDiv);
						tplObj.find(".fourthDiv").html(paymentDetailObj.fourthDiv);
						tplObj.find(".fifthDiv").html(paymentDetailObj.fifthDiv);
						tplObj.find(".sixthDiv").html(paymentDetailObj.sixthDiv);
						tplObj.find(".seventhDiv").html(paymentDetailObj.seventhDiv);
						tplObj.find(".eighthDiv").html(paymentDetailObj.eighthDiv);
						tplObj.find(".ninthDiv").html(paymentDetailObj.ninthDiv);
						tplObj.find(".tenthDiv").html(paymentDetailObj.tenthDiv);
						tplObj.find(".eleventhDiv").html(paymentDetailObj.eleventhDiv);
						tplObj.find(".twelfthDiv").html(paymentDetailObj.twelfthDiv);
						tplObj.find(".thirteenthDiv").html(paymentDetailObj.thirteenthDiv);
						//HEADER END

						//SET START
						var shippingCouponPriceStr = rows[i].shippingCouponPrice != 0 ? '(할인 : '+ fnNumberWithCommas(rows[i].shippingCouponPrice) + ' 원)' : '';

						var approvalDtStr = "";
						if(rows[i].approvalDt != null){
							approvalDtStr = "<br><span>결제 : </span>"+stringUtil.getString(rows[i].approvalDt, "");
						}
						var payInfoStr = stringUtil.getString(rows[i].payInfo, "");
						var payType =  rows[i].payType != null ? stringUtil.getString(rows[i].payType, "") : "";
						var cardQuota =stringUtil.getString(rows[i].cardQuota, "");
						if(cardQuota != '') {
							if(cardQuota == '00') {
								cardQuota = ' / 일시불';
							} else {
								cardQuota = ' / '+rows[i].cardQuota + '개월';
							}
						} else {
							cardQuota = '';
						}

						var salesSlipUrlBtn = '';
						if(rows[i].salesSlipUrl != "") {
							//결제방법 - 신용카드, 간편결제(카카오페이, 페이코, 네이버페이, 삼성페이)
							if((rows[i].payTypeCd == 'PAY_TP.CARD' || rows[i].payTypeCd == 'PAY_TP.KAKAOPAY'
								|| rows[i].payTypeCd == 'PAY_TP.PAYCO' || rows[i].payTypeCd == 'PAY_TP.NAVERPAY'
								|| rows[i].payTypeCd == 'PAY_TP.SSPAY') && rows[i].status != '입금대기중') {
								salesSlipUrlBtn = "<br><button type='button' id='salesSlipUrlBtnLink1_"+i+"' name= 'salesSlipUrlBtnLink1' class='btn-point btn-s' onclick='' >"
								salesSlipUrlBtn += "<span>매출전표</span></button>";
								// 결제방법 - 실시간 계좌이체, 가상계좌
							} else if(rows[i].payTypeCd == 'PAY_TP.BANK' || rows[i].payTypeCd == 'PAY_TP.VIRTUAL_BANK') {
								salesSlipUrlBtn = "<br><button type='button' id='salesSlipUrlBtnLink1_"+i+"' name= 'salesSlipUrlBtnLink1' class='btn-point btn-s' onclick='' >"
								salesSlipUrlBtn += "<span>현금영수증</span></button>";
							}
						}

						tplObj.find(".dateFirst").html("<span>주문 : </span>"+rows[i].createDt+approvalDtStr);
						tplObj.find(".totalOrderPriceFirst").html(fnNumberWithCommas(rows[i].totalOrderPrice+"<span> 원</span>"));
						tplObj.find(".salePriceFirst").html(fnNumberWithCommas(rows[i].salePrice+"<span> 원</span>"));
						tplObj.find(".shippingPriceFirst").html(fnNumberWithCommas(rows[i].shippingPrice)+"<span> 원</span><br>"+ shippingCouponPriceStr);
						tplObj.find(".discountCouponPriceFirst").html(fnNumberWithCommas(rows[i].discountCouponPrice)+"<span> 원</span>");
						tplObj.find(".directPriceFirst").html(fnNumberWithCommas(rows[i].directPrice)+"<span> 원</span>");
						tplObj.find(".discountEmployeePriceFirst").html(fnNumberWithCommas(rows[i].discountEmployeePrice)+"<span> 원</span>");
						tplObj.find(".paymentTargetPriceFirst").html(fnNumberWithCommas(rows[i].paymentTargetPrice)+"<span> 원</span>");
						tplObj.find(".pointPriceFirst").html(fnNumberWithCommas(rows[i].pointPrice)+"<span> 원</span>");
						tplObj.find(".paymentPriceFirst").html(fnNumberWithCommas(rows[i].paymentPrice)+"<span> 원</span>");
						tplObj.find(".statusFirst").html(rows[i].status);
						tplObj.find(".payTypeFirst").html(payType + cardQuota + salesSlipUrlBtn);
						tplObj.find(".payInfoFirst").html(payInfoStr.replaceAll(' ', '<br>'));
						//SET END

						$("#tbodyPaymentArea").append(tplObj.find("tbody").html());

						$("#salesSlipUrlBtnLink1_"+i).on("click", function(){
							window.open(data.payDetailList[i].salesSlipUrl,"_blank");
						});
					}

					// 환불정보 + 추가결제/환불정보 데이터
					rows = data.refundList;
					for (let i = 0; i < rows.length; i++) {
						payTypeCode = rows[i].payTypeCd;

						//환불정보 (추가결제 X, 추가환불 X)
						if(rows[i].type != 'A' && rows[i].typeCd == 'ADD_REFUND_1') {
							tplObj = tpl;
							//HEADER START
							tplObj.find(".firstDiv").html(paymentRefundObj.firstDiv);
							tplObj.find(".secondDiv").html(paymentRefundObj.secondDiv);
							tplObj.find(".thirdDiv").html(paymentRefundObj.thirdDiv);
							tplObj.find(".fourthDiv").html(paymentRefundObj.fourthDiv);
							tplObj.find(".fifthDiv").html(paymentRefundObj.fifthDiv);
							tplObj.find(".sixthDiv").html(paymentRefundObj.sixthDiv);
							tplObj.find(".seventhDiv").html(paymentRefundObj.seventhDiv);
							tplObj.find(".eighthDiv").html(paymentRefundObj.eighthDiv);
							tplObj.find(".ninthDiv").html(paymentRefundObj.ninthDiv);
							tplObj.find(".tenthDiv").html(paymentRefundObj.tenthDiv);
							tplObj.find(".eleventhDiv").html(paymentRefundObj.eleventhDiv);
							tplObj.find(".twelfthDiv").html(paymentRefundObj.twelfthDiv);
							tplObj.find(".thirteenthDiv").html(paymentRefundObj.thirteenthDiv);
							//HEADER END

							//SET START
							var targetTpStr = rows[i].targetTp == 'B' ? '(구매자 부담)' : '(판매자 부담)';
							var approvalDtStr = "";
							if (rows[i].approvalDt != null) {
								approvalDtStr = "<br><span>환불 : </span>" + stringUtil.getString(rows[i].approvalDt, "");
							}

							var payInfoStr = stringUtil.getString(rows[i].payInfo, "");
							var payType =  rows[i].payType != null ? stringUtil.getString(rows[i].payType, "") : "";
							if(payType == '') {
								payType = payTypeStr;
							}
							var cardQuota =stringUtil.getString(rows[i].cardQuota, "");
							if(cardQuota != '') {
								if(cardQuota == '00') {
									cardQuota = ' / 일시불';
								} else {
									cardQuota = ' / '+rows[i].cardQuota + '개월';
								}
							} else {
								cardQuota = '';
							}

							var claimAccountStr = '';
							if(payTypeCode == 'PAY_TP.VIRTUAL_BANK'){
								claimAccountStr = '<br>'+ rows[i].bankNm + ' / <br>' + rows[i].accountNumber + ' / ' + rows[i].accountHolder;
							}

							var claimStatus = rows[i].claimStatus;
							if(fnNvl(rows[i].addPaymentTp) != '' && rows[i].addPaymentTp != 'ADD_PAYMENT_TP.CARD') {
								claimStatus += '<br/>(' + rows[i].addPaymentTpNm + ')';
							}

							tplObj.find(".dateFirst").html("<span>요청 : </span>" + rows[i].createDt + approvalDtStr);
							tplObj.find(".totalOrderPriceFirst").html(fnNumberWithCommas(rows[i].totalRefundPrice+"<span> 원</span>"));
							tplObj.find(".salePriceFirst").html(fnNumberWithCommas(rows[i].goodsPrice+"<span> 원</span>"));
							tplObj.find(".shippingPriceFirst").html(fnNumberWithCommas(rows[i].shippingPrice)+"<span> 원</span><br>"+ targetTpStr);
							tplObj.find(".discountCouponPriceFirst").html(fnNumberWithCommas(rows[i].discountCouponPrice)+"<span> 원</span>");
							tplObj.find(".directPriceFirst").html(fnNumberWithCommas(rows[i].directPrice)+"<span> 원</span>");
							tplObj.find(".discountEmployeePriceFirst").html(fnNumberWithCommas(rows[i].refundEmployeePrice) + "<span> 원</span>");
							tplObj.find(".paymentTargetPriceFirst").html(fnNumberWithCommas(rows[i].refundTargetPrice) + "<span> 원</span>");
							tplObj.find(".pointPriceFirst").html(fnNumberWithCommas(rows[i].refundPointPrice)+"<span> 원</span>");
							tplObj.find(".paymentPriceFirst").html(fnNumberWithCommas(rows[i].refundPrice)+"<span> 원</span>");
							tplObj.find(".statusFirst").html(claimStatus);
							tplObj.find(".payTypeFirst").html(payType+cardQuota);
							tplObj.find(".payInfoFirst").html((rows[i].refundType == 'C' ? '무통장입금' : '원결제수단') + claimAccountStr); // 'C' : 무통장입금
							//SET END

							//추가 결제 일자
							tplObj.find(".firstDiv").parents().eq(0).attr('colspan', ''); //추가 결제 일자
							tplObj.find(".dateFirst").parents().eq(1).attr('colspan', '');
							tplObj.find(".secondDiv").parents().eq(0).show();
							tplObj.find(".totalOrderPriceFirst").parents().eq(1).show();

							//추가 결제금액
							tplObj.find(".thirdDiv").parents().eq(0).attr('colspan', ''); //추가 결제금액
							tplObj.find(".salePriceFirst").parents().eq(1).attr('colspan', '');
							tplObj.find(".fourthDiv").parents().eq(0).show();
							tplObj.find(".shippingPriceFirst").parents().eq(1).show();
							tplObj.find(".fifthDiv").parents().eq(0).show();
							tplObj.find(".discountCouponPriceFirst").parents().eq(1).show();

							//추가결제 상태
							tplObj.find(".sixthDiv").parents().eq(0).attr('colspan', ''); //추가결제 상태
							tplObj.find(".directPriceFirst").parents().eq(1).attr('colspan', '');
							tplObj.find(".seventhDiv").parents().eq(0).show();
							tplObj.find(".discountEmployeePriceFirst").parents().eq(1).show();
							tplObj.find(".eighthDiv").parents().eq(0).show();
							tplObj.find(".paymentTargetPriceFirst").parents().eq(1).show();

							//결제방법
							tplObj.find(".ninthDiv").parents().eq(0).attr('colspan', ''); //결제방법
							tplObj.find(".pointPriceFirst").parents().eq(1).attr('colspan', '');
							tplObj.find(".tenthDiv").parents().eq(0).show();
							tplObj.find(".paymentPriceFirst").parents().eq(1).show();
							tplObj.find(".eleventhDiv").parents().eq(0).show();
							tplObj.find(".statusFirst").parents().eq(1).show();

							//결제기관(PG)
							tplObj.find(".twelfthDiv").parents().eq(0).attr('colspan', ''); //결제기관(PG)
							tplObj.find(".payTypeFirst").parents().eq(1).attr('colspan', '');
							tplObj.find(".thirteenthDiv").parents().eq(0).show();
							tplObj.find(".payInfoFirst").parents().eq(1).show();

							$("#tbodyPaymentArea").append(tplObj.find("tbody").html());

						// 추가결제정보
						} else if(rows[i].type == 'A' && rows[i].typeCd == 'ADD_REFUND_1') {

							addTplObj = tpl;
							//HEADER START
							addTplObj.find(".firstDiv").html(addPaymentObj.firstDiv);
							addTplObj.find(".thirdDiv").html(addPaymentObj.thirdDiv);
							addTplObj.find(".sixthDiv").html(addPaymentObj.sixthDiv);
							addTplObj.find(".ninthDiv").html(addPaymentObj.ninthDiv);
							addTplObj.find(".twelfthDiv").html(addPaymentObj.twelfthDiv);
							//HEADER END

							//SET START
							var payInfoStr = stringUtil.getString(rows[i].payInfo, "");
							var payType =  rows[i].payType != null ? stringUtil.getString(rows[i].payType, "") : "";
							var cardQuota =stringUtil.getString(rows[i].cardQuota, "");
							if(cardQuota != '') {
								if(cardQuota == '00') {
									cardQuota = ' / 일시불';
								} else {
									cardQuota = ' / '+rows[i].cardQuota + '개월';
								}
							} else {
								cardQuota = '';
							}
							var claimStatus = rows[i].claimStatus;
							if(payTypeCode == 'PAY_TP.VIRTUAL_BANK'){
								if(rows[i].status == "IR") {claimStatus += '<br/>(입금대기중)';}
								else if(rows[i].status == "IC") {claimStatus += '<br/>(결제완료)';}
							}

							addTplObj.find(".dateFirst").html("<span>결제 : </span>" + rows[i].createDt);
							addTplObj.find(".salePriceFirst").html(fnNumberWithCommas(rows[i].refundPrice) + "<span> 원</span>");
							addTplObj.find(".directPriceFirst").html(claimStatus);
							addTplObj.find(".pointPriceFirst").html(payType + cardQuota);
							addTplObj.find(".payTypeFirst").html(payInfoStr.replaceAll(' ', '<br>')); // 'C' : 무통장입금
							//SET END

							//추가 결제 일자
							addTplObj.find(".firstDiv").parents().eq(0).attr('colspan', '2'); //추가 결제 일자
							addTplObj.find(".dateFirst").parents().eq(1).attr('colspan', '2');
							addTplObj.find(".secondDiv").parents().eq(0).hide(); // 총 주문금액 hide
							addTplObj.find(".totalOrderPriceFirst").parents().eq(1).hide();

							//추가 결제금액
							addTplObj.find(".thirdDiv").parents().eq(0).attr('colspan', '3'); //추가 결제금액
							addTplObj.find(".salePriceFirst").parents().eq(1).attr('colspan', '3');
							addTplObj.find(".fourthDiv").parents().eq(0).hide();
							addTplObj.find(".shippingPriceFirst").parents().eq(1).hide();
							addTplObj.find(".fifthDiv").parents().eq(0).hide();
							addTplObj.find(".discountCouponPriceFirst").parents().eq(1).hide();

							//추가결제 상태
							addTplObj.find(".sixthDiv").parents().eq(0).attr('colspan', '3'); //추가결제 상태
							addTplObj.find(".directPriceFirst").parents().eq(1).attr('colspan', '3');
							addTplObj.find(".seventhDiv").parents().eq(0).hide();
							addTplObj.find(".discountEmployeePriceFirst").parents().eq(1).hide();
							addTplObj.find(".eighthDiv").parents().eq(0).hide();
							addTplObj.find(".paymentTargetPriceFirst").parents().eq(1).hide();

							//결제방법
							addTplObj.find(".ninthDiv").parents().eq(0).attr('colspan', '3'); //결제방법
							addTplObj.find(".pointPriceFirst").parents().eq(1).attr('colspan', '3');
							addTplObj.find(".tenthDiv").parents().eq(0).hide();
							addTplObj.find(".paymentPriceFirst").parents().eq(1).hide();
							addTplObj.find(".eleventhDiv").parents().eq(0).hide();
							addTplObj.find(".statusFirst").parents().eq(1).hide();

							//결제기관(PG)
							addTplObj.find(".twelfthDiv").parents().eq(0).attr('colspan', '2'); //결제기관(PG)
							addTplObj.find(".payTypeFirst").parents().eq(1).attr('colspan', '2');
							addTplObj.find(".thirteenthDiv").parents().eq(0).hide();
							addTplObj.find(".payInfoFirst").parents().eq(1).hide();

							$("#tbodyPaymentArea").append(addTplObj.find("tbody").html());

						// 추가환불정보
						} else if (rows[i].typeCd == 'ADD_REFUND_2') {
							addTplObj = tpl;
							//HEADER START
							addTplObj.find(".firstDiv").html(addRefundObj.firstDiv);
							addTplObj.find(".thirdDiv").html(addRefundObj.thirdDiv);
							addTplObj.find(".sixthDiv").html(addRefundObj.sixthDiv);
							addTplObj.find(".ninthDiv").html(addRefundObj.ninthDiv);
							addTplObj.find(".twelfthDiv").html(addRefundObj.twelfthDiv);
							//HEADER END

							//SET START
							var payInfoStr = stringUtil.getString(rows[i].payInfo, "");
							var payType =  rows[i].payType != null ? stringUtil.getString(rows[i].payType, "") : "";
							var cardQuota =stringUtil.getString(rows[i].cardQuota, "");
							if(cardQuota != '') {
								if(cardQuota == '00') {
									cardQuota = ' / 일시불';
								} else {
									cardQuota = ' / '+rows[i].cardQuota + '개월';
								}
							} else {
								cardQuota = '';
							}

							addTplObj.find(".dateFirst").html("<span>환불 : </span>" + rows[i].createDt);
							addTplObj.find(".salePriceFirst").html(fnNumberWithCommas(rows[i].refundPrice) + "<span> 원</span>");
							addTplObj.find(".directPriceFirst").html(rows[i].claimStatus);
							addTplObj.find(".pointPriceFirst").html(payType + cardQuota);
							addTplObj.find(".payTypeFirst").html(rows[i].refundType == 'C' ? '무통장입금' : '원결제수단'); // 'C' : 무통장입금
							//SET END

							//추가 결제 일자
							addTplObj.find(".firstDiv").parents().eq(0).attr('colspan', '2'); //추가 결제 일자
							addTplObj.find(".dateFirst").parents().eq(1).attr('colspan', '2');
							addTplObj.find(".secondDiv").parents().eq(0).hide();
							addTplObj.find(".totalOrderPriceFirst").parents().eq(1).hide();

							//추가 결제금액
							addTplObj.find(".thirdDiv").parents().eq(0).attr('colspan', '3'); //추가 결제금액
							addTplObj.find(".salePriceFirst").parents().eq(1).attr('colspan', '3');
							addTplObj.find(".fourthDiv").parents().eq(0).hide();
							addTplObj.find(".shippingPriceFirst").parents().eq(1).hide();
							addTplObj.find(".fifthDiv").parents().eq(0).hide();
							addTplObj.find(".discountCouponPriceFirst").parents().eq(1).hide();

							//추가결제 상태
							addTplObj.find(".sixthDiv").parents().eq(0).attr('colspan', '3'); //추가결제 상태
							addTplObj.find(".directPriceFirst").parents().eq(1).attr('colspan', '3');
							addTplObj.find(".seventhDiv").parents().eq(0).hide();
							addTplObj.find(".discountEmployeePriceFirst").parents().eq(1).hide();
							addTplObj.find(".eighthDiv").parents().eq(0).hide();
							addTplObj.find(".paymentTargetPriceFirst").parents().eq(1).hide();

							//결제방법
							addTplObj.find(".ninthDiv").parents().eq(0).attr('colspan', '3'); //결제방법
							addTplObj.find(".pointPriceFirst").parents().eq(1).attr('colspan', '3');
							addTplObj.find(".tenthDiv").parents().eq(0).hide();
							addTplObj.find(".paymentPriceFirst").parents().eq(1).hide();
							addTplObj.find(".eleventhDiv").parents().eq(0).hide();
							addTplObj.find(".statusFirst").parents().eq(1).hide();

							//결제기관(PG)
							addTplObj.find(".twelfthDiv").parents().eq(0).attr('colspan', '2'); //결제기관(PG)
							addTplObj.find(".payTypeFirst").parents().eq(1).attr('colspan', '2');
							addTplObj.find(".thirteenthDiv").parents().eq(0).hide();
							addTplObj.find(".payInfoFirst").parents().eq(1).hide();

							$("#tbodyPaymentArea").append(addTplObj.find("tbody").html());
						}

					}

				}
		});
	},
	//-->쿠폰 할인 정보 그리드
	initCouponDiscountGrid : function initCouponDiscountGrid(odOrderId) {
		couponDiscountGridDs = fnGetDataSource({
			url      : '/admin/order/getOrderDetailDiscountList?odOrderId='+odOrderId
		});
		couponDiscountGridOpt = {
			dataSource: couponDiscountGridDs,
			navigatable : true,
			scrollable : true,
			columns : orderMgmGridUtil.couponDiscountList()
		};
		couponDiscountGrid = $('#couponDiscountGrid').initializeKendoGrid( couponDiscountGridOpt ).cKendoGrid();

		couponDiscountGrid.bind("dataBound", function() {
			$( '#couponDiscountGrid' ).find("tr").css("height", "50px");
		});

		couponDiscountGridDs.query();
	},
	//-->주문자 정보
	initOrdererForm : function initOrdererForm(odid) {
		var odOrderId = "";
		fnAjax({
			url     : '/admin/order/getOrderBuyer',
			params  : {odid : odid},
			async   : false,
			success :
				function( data ){

					if(data.rows) {
						var obj = data.rows;
						var sellersNm = (($.trim(obj.sellersNm) != "")? " (" + $.trim(obj.sellersNm)+")" : "");
						var outmallId = (($.trim(obj.outmallId) != "")? $.trim(obj.outmallId): "");
						var collectionMallId = (($.trim(obj.collectionMallId) != "")? " (" + $.trim(obj.collectionMallId)+")" : "");
						var buyerNm = (obj.buyerNm != '')? obj.buyerNm+' / ' : '';
						var longinId = (obj.loginId != '')? obj.loginId+' / ' : '';
						var urEmployeeYn = (obj.urEmployeeYn == 'Y')? '임직원'+ ' / ' : '';
						var urGroupNm = (obj.urGroupId != 0) ? obj.urGroupNm : '비회원';
						var urUserId = obj.urUserId;
						odOrderId = obj.odOrderId
						var odidText = obj.odid;
						if ($.trim(obj.orderCopyOdid) != ""){
							odidText += " (주문복사 : <a href='javascript:goOrderDetailView(\""+$.trim(obj.orderCopyOdid)+"\")' style='color: blue'>" + $.trim(obj.orderCopyOdid) + "</a>)";

						}
						if($.trim(obj.reqId) != "") {
							odidText += " <a href='javascript:goOrderRegularDetailView(\""+$.trim(obj.odRegularReqId)+"\")' style='color: blue;font-weight: bold;'>[정기배송신청번호 : " + $.trim(obj.reqId) + "]</a>";
						}
						if($.trim(obj.giftYn) == "Y") {
							odidText = "선물하기 / " + obj.odid;
							// 선물 정보 조회
							$('#presentForm').show();
							initOrderPresentInfo(odOrderId);
						} else {
							$('#presentForm').hide();
						}
						$("#odid").val(obj.odid);
						$("#odidText").html(odidText);
						$('#urUserId').text(urUserId);

						if(obj.urGroupId != 0){ //회원일 때
							$("#buyerNm").html("<span id= 'userDetailBtn' style='color: blue; font-weight : bold; text-decoration: underline;  cursor: pointer;'>"+buyerNm + longinId + urEmployeeYn + urGroupNm+"</span>"); //주문자정보
						} else { //비회원일 때
							$("#buyerNm").html("<span style='font-weight : bold;'>"+buyerNm + urEmployeeYn + urGroupNm+"</span>"); //주문자정보
						}
						var sellersGroupNm = (obj.sellersGroupCd != "SELLERS_GROUP.MALL" ? obj.sellersGroupNm + sellersNm : obj.sellersGroupNm);

						sellersGroupNm = sellersGroupNm + " / " + obj.agentType;

						if(obj.outmallType == "") {
							if (obj.orderCopyYn === 'Y') {
								if (obj.orderCopySalIfYn === "Y") {
									sellersGroupNm = sellersGroupNm + " (주문복사/매출만연동)";
								} else {
									sellersGroupNm = sellersGroupNm + " (주문복사)";
								}
							} else if (obj.orderCreateYn === 'Y') {
								sellersGroupNm = sellersGroupNm + " (주문생성)";
							}
						} else {
							sellersGroupNm = sellersGroupNm + " (" + ((obj.outmallType == "E") ? "이지어드민" : "샤방넷") + ")";
						}
						var buyerHp = obj.buyerHp.replace(/\-/g,'') == "" ? "01000000000" : obj.buyerHp.replace(/\-/g,''); // 휴대폰번호 하이픈(-) 제거
						if(buyerHp.length < 12) {
							buyerHp = fnPhoneNumberHyphen(buyerHp);

						} else if(12 <= buyerHp.length){
							buyerHp = buyerHp.slice(0,4) + '-' + buyerHp.slice(4,(buyerHp.length)-4) + '-' + buyerHp.slice((buyerHp.length)-4,buyerHp.length);
						}
						$("#sellersGroupNm").text(sellersGroupNm);
						$("#buyerHp").text(buyerHp);
						$("#createDt").text(obj.createDt + (obj.approvalDt != '' ? ' / ' + obj.approvalDt : '')); //주문일시 / 결제일시
						$("#buyerMail").text(obj.buyerMail !=''? obj.buyerMail : '-'); //주문자 이메일
						$("#guestCi").text(obj.guestCi);

						var sellersUrl		= $.trim(obj.sellersUrl);
						var sellersAdminUrl = $.trim(obj.sellersAdminUrl);
						//외부몰주문번호 (판매처주문번호)
						if (sellersAdminUrl != "") { //외부몰관리자링크가 있는 경우
							var outMallIdHtml = ""
							outMallIdHtml += "<button type='button' style='float: right;' id='btnOutmallAdminLink' name='btnOutmallAdminLink' value='IB' class='btn-point btn-s btnOutmallAdminLink' onclick='' >"
							outMallIdHtml += "<span>"+obj.sellersNm+"<br>관리자페이지 이동</span></button>";
							$("#btnOutMallAdmin").html(outMallIdHtml);
							$("#outmallId").text(outmallId + collectionMallId);
							$('#outmallId').css('padding-top','10px');
							$(".btnOutmallAdminLink").on("click", function(){
								window.open(sellersAdminUrl,"_blank");
							});
						} else { //외부몰관리자링크가 없는 경우
							if(outmallId != "" && collectionMallId != "") {
								$('#outmallId').text(outmallId + collectionMallId);
							} else {
								$('#outmallId').text('-');
							}
						}

					}

				},
			isAction : 'select'
		});
		return odOrderId;
	},
	//-->배송지 정보 그리드
	initShippingGrid : function initShippingGrid(odOrderId) {
		shippingGridDs = fnGetDataSource({
			url      : "/admin/order/getOrderDetailShippingZoneList?odOrderId="+odOrderId
		});
		shippingGridOpt = {
			dataSource: shippingGridDs,
			navigatable : true,
			scrollable : true,
			columns : orderMgmGridUtil.shippingList()
		};
		shippingGrid = $('#shippingGrid').initializeKendoGrid( shippingGridOpt ).cKendoGrid();

		shippingGrid.bind("dataBound", function() {
			//일반배송 상품 출고처가 N개 이고, 주문 상품 중 1개라도 배송준비중으로 변경 된 경우, 배송정보 수정 불가 (수정 버튼 미노출)
			if($( '#shippingGrid' ).find('.normalDeliveryHideDiv').length > 0) {
				$(".normalDeliveryDiv").hide();
			}

			$( '#shippingGrid' ).find("tr").css("height", "50px");
			mergeGridRows('shippingGrid', ['recvNm', 'recvHp', 'recvZipCd', 'recvAddr1', 'recvAddr2', 'deliveryMsg', 'doorMsgCdName', 'management'],['deliveryTypeCode']);
		});
		shippingGridDs.query();

	},
	//-->주문복사 배송지 정보 그리드
	initOrderCopyShippingGrid : function initShippingGrid(odOrderId) {
		shippingGridDs = fnGetDataSource({
			url      : "/admin/order/copy/getOrderDetailCopyShippingZoneList?odOrderId="+odOrderId
		});
		shippingGridOpt = {
			dataSource: shippingGridDs,
			navigatable : true,
			scrollable : true,
			columns : orderMgmGridUtil.orderCopyshippingList()
		};
		shippingGrid = $('#shippingGrid').initializeKendoGrid( shippingGridOpt ).cKendoGrid();

		shippingGrid.bind("dataBound", function() {
			$( '#shippingGrid' ).find("tr").css("height", "50px");
		});

		shippingGridDs.query();
	},
	//--> 주문 상담 내용 리스트
	initOrderConsultForm : function initOrderConsultForm(odOrderId){
		fnAjax({
			url     : '/admin/order/getOrderDetailConsultList',
			params  : {odOrderId : odOrderId},
			success :
				function( data ){
					var html = document.querySelector('#templateConsult').innerHTML;
					var resultHtml = "";
					for(var i=0; i<data.rows.length; i++){
						resultHtml += html.replace("{modifyNm}", data.rows[i].modifyNm)
							.replace("{modifyId}", data.rows[i].modifyId)
							.replace("{modifyDt}", data.rows[i].modifyDt)
							.replace("{odConsultIdUpdate1}", data.rows[i].odConsultId)
							.replace("{odConsultMsgUpdate}", "")
							.replace("{odConsultIdDel1}", data.rows[i].odConsultId)
							.replace("{hidden_odConsultId}", data.rows[i].odConsultId)
							.replace("{odConsultMsgTextArea}", data.rows[i].odConsultMsg)
							.replace("{odConsultIdText}", data.rows[i].odConsultId)
							.replace("{modifyIdList1}", data.rows[i].modifyId)
							.replace("{modifyIdList2}", data.rows[i].modifyId)
							.replace("{modifyIdList3}", data.rows[i].modifyId)
							.replace("{modifyIdList4}", data.rows[i].modifyId)
						    .replace("{odConsultIdSave1}", data.rows[i].odConsultId)
						    .replace("{odConsultIdUpdate2}", data.rows[i].odConsultId)
						    .replace("{odConsultIdDel2}", data.rows[i].odConsultId)
						    .replace("{odConsultIdSave2}", data.rows[i].odConsultId)
						;

					}
					document.querySelector("#consultInfo").innerHTML = resultHtml;

					//주문상담내용 등록한 관리자 아닌경우 수정/삭제 버튼 hide
					for(var j=0; j < $('.consult__manager__id__list').length; j++){
						var modifyId = $('.consult__manager__id__list')[j].getAttribute('name');
						if(PG_SESSION.loginId != modifyId) {
							$("button[name='"+modifyId+"']").hide();
						}
					}
				},
			isAction : 'select'
		});
	},
	//--> 처리이력그리드
	initOrderHistoryGrid : function initOrderHistoryGrid(odOrderId){
		orderHistoryGridDs = fnGetDataSource({
			url      : '/admin/order/getOrderDetailHistoryList?odOrderId='+odOrderId
		});

		orderHistoryGridOpt = {
			dataSource: orderHistoryGridDs,
			navigatable : true,
			scrollable : true,
			columns : orderMgmGridUtil.orderHistoryList()
		};
		orderHistoryGrid = $('#orderHistoryGrid').initializeKendoGrid( orderHistoryGridOpt ).cKendoGrid();

		orderHistoryGrid.bind("dataBound", function() {
			$( '#orderHistoryGrid' ).find("tr").css("height", "50px");

			var rowNum = orderHistoryGridDs._view.length;
			$("#orderHistoryGrid tbody > tr .row-number").each(function(index){
				$(this).text(rowNum);
				rowNum--;
			});
		});

		orderHistoryGridDs.query();
	},
	//--> 클레임 회수 정보
	initClaimReturnShippingGrid : function initClaimReturnShippingGrid(odOrderId){
		claimReturnShippingGridDs = fnGetDataSource({
			url      : '/admin/order/getOrderDetailClaimCollectionList?odOrderId='+odOrderId
		});

		claimReturnShippingGridOpt = {
			dataSource: claimReturnShippingGridDs,
			navigatable : true,
			scrollable : true,
			columns : orderMgmGridUtil.claimReturnShippingList()
		};
		claimReturnShippingGrid = $('#claimReturnShippingGrid').initializeKendoGrid( claimReturnShippingGridOpt ).cKendoGrid();

		claimReturnShippingGrid.bind("dataBound", function() {
			$( '#claimReturnShippingGrid' ).find("tr").css("height", "50px");
		});

		claimReturnShippingGridDs.query();
	},
	//하이톡 스위치 여부
	initHitokSwitch : function initHitokSwitch() {
		var isHitok = null;
	    fnAjax({
			url     : '/admin/order/getHitokSwitch',
			params  : '',
			async   : false,
			success : function(resultData) {
                isHitok = resultData;
			},
			isAction : 'select'
		});

	    return isHitok;
	},
	//spring profiles
	initProfilesActive : function initProfilesActive() {
		var profilesActive = "";
		fnAjax({
			url     : '/admin/comn/getProfilesActive',
			params  : '',
			async   : false,
			success : function(resultData) {
				profilesActive = resultData;
			},
			isAction : 'select'
		});

		return profilesActive;
	},
	//그리드 겁색
	fnSearch : function(thisId) {
		//상품정보
		if(thisId == 'defaultTab') {
			this.fnInitOrderGoodsGrid();
		}
	},
	fnLengthCheck: function(selector) {

		// $(selector).on("keyup", function(e) {
		// 	console.log(e);
		// })
	}
}

var orderDetailGridEventUtil = {
	orderStatusChange : function(gridSelect) {
		var selectRows			= $("#"+gridSelect).find('input[name=rowCheckbox]:checked').closest('tr');
		let selectRowsLength	= selectRows.length;
		$(".tab-cont.k-content.k-state-active").find("[name='putOrderStatusBtnArea']").find("button").attr("disabled",true);
		if( selectRowsLength > 0){
			let selectedRowData;
			let arrStatus		= {};
			let arrOrderStatus	= {};
			let odClaimIdList = []; //클레임번호 arr
			let goodsTpCdList = []; // 상품유형 arr
			let goodsDailyTpList = []; // 일일상품유형 arr
			let cnt				= 0;
			let isDeactiveReDelvBtnForHitok = false; //하이톡 스위치

			for(var i = 0 ; i < selectRowsLength ; i++){
				if($(selectRows[i]).hasClass('trDisabled')) {
					continue;
				}
				var orderStatusCode = '';
				if(gridSelect != "claimGoodsGrid") {
					selectedRowData = orderGoodsGrid.dataItem($(selectRows[i]));
					orderStatusCode = selectedRowData.orderStatusCode;
				} else  {
					selectedRowData = claimGoodsGrid.dataItem($(selectRows[i]));
					orderStatusCode = selectedRowData.claimStatusCode;
				}

				//하이톡<-->FD-PHI 스위치 ON 상태이면 재배송버튼 비활성화 (하이톡 스위치)
				if(isHitokSwitch && selectedRowData.goodsDailyTp == 'GOODS_DAILY_TP.GREENJUICE') isDeactiveReDelvBtnForHitok = true;

				var json = JSON.parse(selectedRowData.bosJson);

				let arrOdClaimId    = {};
				arrOdClaimId.odClaimId = selectedRowData.odClaimId;
				odClaimIdList.push(arrOdClaimId);

				goodsTpCdList.push(selectedRowData.goodsTpCd);
				goodsDailyTpList.push(selectedRowData.goodsDailyTp);

				if (arrOrderStatus.hasOwnProperty(orderStatusCode) == true) {
					arrOrderStatus[orderStatusCode] = arrOrderStatus[orderStatusCode] + 1;
				} else {
					arrOrderStatus[orderStatusCode] = 1;
				}
				$(json).each(function(i, item){
					$(item.rows).each(function(j, item2){
						$(item2.actionRows).each(function(k, item3){
                        	if(item2.typeCd == 'BOS') {
							var actionFlag = true;
							var actionExecId = item3.actionExecId;
							var claimCntCheck = stringUtil.getString($("button[name='"+ actionExecId+"']").data("claimcntcheck"), "N");

							if (gridSelect != "claimGoodsGrid" && claimCntCheck == "Y"){
								if(selectedRowData.claimAbleCnt <= 0){
									actionFlag = false;
								}
							}
							if (actionFlag == true) {
								if (arrStatus.hasOwnProperty(actionExecId) == true) {

									if (gridSelect != "claimGoodsGrid" && claimCntCheck == "Y") {
										if (selectedRowData.claimAbleCnt > 0) {
											cnt = arrStatus[actionExecId];
											arrStatus[actionExecId] = cnt + 1;
										}
									} else {
										cnt = arrStatus[actionExecId];
										arrStatus[actionExecId] = cnt + 1;
									}
								} else {
									arrStatus[actionExecId] = 1;
								}
							} else {
								arrStatus[actionExecId] = 0;
							}
                          }
						});
					});
				});
			}
			for(var key in arrStatus){
				var disabledFlag = true;
				if (selectRowsLength == arrStatus[key]){
					disabledFlag = true;
					if (key == "putOrderStatusC") { // 취소인경우 결제완료, 배송준비중 분리 해야함
						if (Object.keys(arrOrderStatus).length > 1){
							disabledFlag = false;
						}
					}
					if(odClaimIdList[0].odClaimId != 0) {
						if(odClaimIdList[0].odClaimId != odClaimIdList[odClaimIdList.length-1].odClaimId){
							disabledFlag = false;
						}
					}

					// 선물하기는 받기 완료 상태일때만 배송준비중 가능
					if(disabledFlag && orderPresentInfo && orderPresentInfo.presentOrderStatus !='PRESENT_ORDER_STATUS.RECEIVE_COMPLET' && key == 'putOrderStatusDR'){
						disabledFlag = false;
					}

					if (disabledFlag == true) {
						$(".tab-cont.k-content.k-state-active").find(`[name='${key}']`).attr("disabled", false);

						//상품유형이 "일일"이고 공급업체가 풀무원녹즙(PDM)이면 재배송(EC)버튼 비활성화 처리
						if(goodsTpCdList.includes('GOODS_TYPE.DAILY') &&
							(goodsDailyTpList.includes('GOODS_DAILY_TP.BABYMEAL') || goodsDailyTpList.includes('GOODS_DAILY_TP.EATSSLIM'))) {
							$(".tab-cont.k-content.k-state-active").find(`[name='putOrderStatusEC']`).attr("disabled", true);
						}

					}

					//하이톡<-->FD-PHI 스위치 ON 상태이면 재배송버튼 비활성화 처리 (하이톡 스위치)
					if(isDeactiveReDelvBtnForHitok && key == "putOrderStatusEC") {
						$(".tab-cont.k-content.k-state-active").find(`[name='${key}']`).attr("disabled", true);
					}

					// 매장배송이면 재배송 버튼 비활성화
					if(selectedRowData.goodsDeliveryType == "GOODS_DELIVERY_TYPE.SHOP" && key == "putOrderStatusEC"){
						$(".tab-cont.k-content.k-state-active").find(`[name='${key}']`).attr("disabled", true);
					}
				}
			}
			// CS환불은 모든 상태에서 처리 가능 추가
			$(".tab-cont.k-content.k-state-active button[name=putOrderStatusCS]").removeAttr("disabled");
		}
	},
	fnVirtualAccountCheckBox : function (checkFlag) {
		$("#orderGoodsGrid input[name=rowCheckbox]").prop("checked", checkFlag);
		$("#orderGoodsGrid input.packageCheckbox").prop("checked", checkFlag);
		let checkLength = $("#orderGoodsGrid input[name=rowCheckbox]:checked").length;
		let checkBoxLength = $("#orderGoodsGrid input[name=rowCheckbox]").length;
		if(checkLength == checkBoxLength) {
			$("#orderGoodsGrid input[name=checkBoxAll]").prop("checked", true);
		}
		else {
			$("#orderGoodsGrid input[name=checkBoxAll]").prop("checked", false);
		}
		// 체크가 되었을경우 취소버튼 활성화
		if(checkFlag) {
			var claimAbleCnt = 0;
			$("#orderGoodsGrid input[name=rowCheckbox]:checked").each(function() {
				let tr = $(this).closest("tr");
				let gridData = orderGoodsGrid.dataItem($(tr));
				if(gridData.claimAbleCnt > 0) {
					claimAbleCnt++;
				}
			});
			// 클레임 가능 수량이 존재할 경우에만 취소버튼 활성화
			if(claimAbleCnt > 0) {
				$("#putOrderStatusBtnArea button[name=putOrderStatusC]").removeAttr("disabled");
			}
			else {
				$("#putOrderStatusBtnArea button[name=putOrderStatusC]").attr("disabled", true);
			}
		}
		else {
			$("#putOrderStatusBtnArea button[name=putOrderStatusC]").attr("disabled", true);
		}
	},
	fnCheckBoxAllClick : function(checkBoxAllId, rowCheckboxId) {
		$("#"+checkBoxAllId).off("click");
		$("#"+checkBoxAllId).on("click", function(index){ // 상품/결제정보

			if( $(checkBoxAllId).prop("checked") ){
				$("input[name='"+rowCheckboxId+"]").prop("checked", true);
			}else{
				$("input[name'"+rowCheckboxId+"]").prop("checked", false);
			}
			let gridSelect = $(this).parents().eq(6).attr("id");

			orderDetailGridEventUtil.orderStatusChange(gridSelect);
		});
	},
	ckeckBoxAllClick: function(){
		$("input.rowAllCheckbox").off("click");
		$("input.rowAllCheckbox").on("click", function(index){
			let that	= $(this);
			let obj		= that.closest("div[data-role='grid']").find(".rowCheckbox");
			let packObj	= that.closest("div[data-role='grid']").find(".packageCheckbox"); // 묶음상품
			if(that.is(":checked") == true){
				obj.prop("checked", true);
				packObj.prop("checked", true);
			}else{
				obj.prop("checked", false);
				packObj.prop("checked", false);
			}

			let gridSelect = that.parents().eq(6).attr("id");

			if(gridSelect != "claimGoodsGrid") {
				var rowTr = $("#orderGoodsGrid input[name=rowCheckbox]:eq(0)").closest("tr");
				let gridData = orderGoodsGrid.dataItem($(rowTr));
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
	},
	checkBoxClick: function(checkboxAllObj, checkboxObj){
		$("input.rowCheckbox").off("click");
		$("input.rowCheckbox").on("click", function(){
			let that		= $(this);
			let obj			= that.closest("div[data-role='grid']");

			let headerObj	= obj.find(".rowAllCheckbox");
			let targetObj	= obj.find(".rowCheckbox:checked");
			let rowObj		= obj.find(".rowCheckbox");

			if(targetObj.length == rowObj.length){
				headerObj.prop("checked", true);
			}else{
				headerObj.prop("checked", false);
			}

			let detailid		= that.data("detailid");
			let packObj			= that.closest("div[data-role='grid']").find("input[data-detailid='"+detailid+"'].rowCheckbox");
			let packTargetObj	= that.closest("div[data-role='grid']").find("input[data-detailid='"+detailid+"'].rowCheckbox:checked");

			if (packObj.length == packTargetObj.length){
				that.closest("div[data-role='grid']").find("input[data-detailid='"+detailid+"'].packageCheckbox").prop("checked", true);
			} else {
				that.closest("div[data-role='grid']").find("input[data-detailid='"+detailid+"'].packageCheckbox").prop("checked", false);
			}

		});

	},
	ckeckBoxPackageClick: function(){
		$("input.packageCheckbox").off("click");
		$("input.packageCheckbox").on("click", function(){

			let that		= $(this);
			let parentId	= that.data("detailid");
			let object 		= that.closest("div[data-role='grid']");
			let obj			= that.closest("div[data-role='grid']").find("input[data-detailid='"+parentId+"']");

			let headerObj	= object.find(".rowAllCheckbox");
			let targetObj	= {};
			let rowObj		= object.find(".rowCheckbox");

			if(that.is(":checked") == true){
				obj.prop("checked", true);
				targetObj = object.find(".rowCheckbox:checked");
			}else{
				obj.prop("checked", false);
				targetObj = object.find(".rowCheckbox:checked");
			}
			if(targetObj.length == rowObj.length){
				headerObj.prop("checked", true);
			}else{
				headerObj.prop("checked", false);
			}

			let gridSelect = that.parents().eq(6).attr("id");
			if(gridSelect != "claimGoodsGrid") {
				var rowTr = $(this).closest("tr");
				let gridData = orderGoodsGrid.dataItem($(rowTr));
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
	}
}

var orderStatusChangeUtil = {
	//주문 상태변경 버튼 초기화
	fnPutOrderStatusBtnClear : function() {
		$.each($("[name='putOrderStatusBtnArea']").find("button"), function(i, item){
			var that = $(this);
			that.attr("disabled",true);
		});
	},
	//현재 주문상태에 따라 주문상태변경 값 변경
	fnPutOrderStatus : function(orderStatusCd, btnOrderStatusCd){
		let putOrderStatusCd;
		let putOrderStatus;
		// 결제완료일때 취소 클릭시 -> 취소 완료
		if(orderStatusCd == 'IC' && btnOrderStatusCd == 'C'){
			return {putOrderStatusCd : 'CC', putOrderStatus : '취소완료'};
		}
		// 배송준비중일때 취소 클릭시 -> 취소요청
		if(orderStatusCd == 'DR' && btnOrderStatusCd == 'C'){
			return {putOrderStatusCd : 'CA', putOrderStatus : '취소요청'};
		}
//		//배송완료, 구매확정일때 반품 클릭시
//		//- > 회수?회수안함? (클레임상세팝업 내 해결)
//		if((orderStatusCd == 'BF') && btnOrderStatusCd == 'R'){
//			return {putOrderStatusCd : 'RI', putOrderStatus : '반품승인'};
//		}
		// 배송완료, 구매확정일때 CS환불 클릭시
		if((orderStatusCd == 'DC' || orderStatusCd == 'BF') && btnOrderStatusCd == 'CS'){
			return {putOrderStatusCd : 'CS', putOrderStatus : 'CS환불'};
		}
		// 배송중, 배송완료, 구매확정, 반품요청 일때 반품 클릭시
		if((orderStatusCd == 'DI' || orderStatusCd == 'DC' || orderStatusCd == 'BF' || orderStatusCd == 'RA') && btnOrderStatusCd == 'R'){
			return {putOrderStatusCd : 'RC', putOrderStatus : '반품완료'};
		}

		// 취소요청일때 취소승인 클릭시 -> 취소완료
		if(orderStatusCd == 'CA' && btnOrderStatusCd == 'C1'){
			return {putOrderStatusCd : 'CC', putOrderStatus : '취소완료'};
		}
		// 취소요청일때 취소거부 클릭시 -> 배송중
		if(orderStatusCd == 'CA' && btnOrderStatusCd == 'C2'){
			return {putOrderStatusCd : 'DI', putOrderStatus : '배송중'};
		}
		// 취소요청일때 취소 클릭시 -> 취소완료 OR 취소거부 라디오버튼 활성화(클레임상세팝업 내)
		if(orderStatusCd == 'CA' && btnOrderStatusCd == 'C'){
			return {putOrderStatusCd : 'CC', putOrderStatus : '취소완료'}
		}
		// 반품승인일때 반품 클릭시 - > 반품완료
		if(orderStatusCd == 'RI' && btnOrderStatusCd == 'R'){
			return {putOrderStatusCd : 'RC', putOrderStatus : '반품완료'};
		}
		// 반품보류일때 반품거부 클릭시 -> 배송완료
		if(orderStatusCd == 'RF' && btnOrderStatusCd == 'R1'){
			return {putOrderStatusCd : 'DC', putOrderStatus : '배송완료'};
		}
		// 반품보류일때 반품 클릭시 -> 반품완료
		if(orderStatusCd == 'RF' && btnOrderStatusCd == 'R'){
			return {putOrderStatusCd : 'RC', putOrderStatus : '반품완료'};
		}
		return {putOrderStatusCd : btnOrderStatusCd, putOrderStatus : ""};
	},
	//현재 클레임상태에 따라 주문상태값 변경
	fnClaimOrderStatus : function (orderStatusCd){
		let putOrderStatusCd;
		let putOrderStatus;
		//반품요청일때
		//- > 회수?회수안함? (클레임상세팝업 내 해결)
		if(orderStatusCd == 'RA'){
			return {putOrderStatusCd : 'RI', putOrderStatus : '반품승인'};
		}
		// 반품승인일때
		if(orderStatusCd == 'RI'){
			return {putOrderStatusCd : 'RC', putOrderStatus : '반품완료'};
		}
		// 반품보류일때
		if(orderStatusCd == 'RF'){
			return {putOrderStatusCd : 'RC', putOrderStatus : '반품완료'};
		}
		// CS환불일때
		if(orderStatusCd == 'CS'){
			return {putOrderStatusCd : 'CS', purOrderStatus : 'CS환불'};
		}
		// 취소완료일때
		if(orderStatusCd == 'CC'){
			return {putOrderStatusCd : 'CC', purOrderStatus : '취소완료'};
		}
		// 반품완료일때
		if(orderStatusCd == 'RC'){
			return {putOrderStatusCd : 'RC', purOrderStatus : '반품완료'};
		}
		return {putOrderStatusCd : orderStatusCd, putOrderStatus : ""};
	},
	//현재 클레임상태에 따라 클레임상태값 변경 (odClaimId가 있는 경우 [OD_CLAIM])
	fnPutClaimStatus : function(claimStatusCd, btnClaimStatusCd){
		let putclaimStatusCd;
		let putclaimStatus;

		// 반품요청일 때 반품 클릭 시 -> 반품완료
		if(claimStatusCd == 'RA' && btnClaimStatusCd == 'R'){
			return {putclaimStatusCd : 'RC', putclaimStatus : '반품완료'};
		}
		// 반품승인일 때 반품 클릭 시 -> 반품완료
		if(claimStatusCd == 'RI' && btnClaimStatusCd == 'R'){
			return {putclaimStatusCd : 'RC', putclaimStatus : '반품완료'};
		}
		// 반품보류일 때 반품 클릭 시 -> 반품완료
		if(claimStatusCd == 'RF' && btnClaimStatusCd == 'R'){
			return {putclaimStatusCd : 'RC', putclaimStatus : '반품완료'};
		}
		// 취소요청일 때 취소 클릭 시 -> 취소완료
		if(claimStatusCd == 'CA' && btnClaimStatusCd == 'C'){
			return {putclaimStatusCd : 'CC', putclaimStatus : '취소완료'};
		}
		return {putclaimStatusCd : claimStatusCd, putclaimStatus : ""};
	}

}
var orderGridEventUtil = {
	noView: function(addBindFnc){
		orderGrid.bind("dataBound", function(e) {

			$('#countTotalSpan').text( kendo.toString(orderGridDs._total, "n0") );

			let rowNum = orderGridDs._total - ((orderGridDs._page - 1) * orderGridDs._pageSize);
			$("#orderGrid tbody > tr .row-number").each(function(index){
				$(this).html(rowNum);
				rowNum--;
			});
			if (addBindFnc) addBindFnc();
		});
	},
	ckeckBoxAllClick: function(){
		$("#checkBoxAll").on("click", function(index){

			if( $("#checkBoxAll").prop("checked") ){

				$("input[name=rowCheckbox]").prop("checked", true);
			}else{

				$("input[name=rowCheckbox]").prop("checked", false);
			}
		});
	},
	checkBoxClick: function(){
		orderGrid.element.on("click", "[name=rowCheckbox]" , function(e){

			if( e.target.checked ){
				if( $("[name=rowCheckbox]").length == $("[name=rowCheckbox]:checked").length ){
					$("#checkBoxAll").prop("checked", true);
				}
			}else{
				$("#checkBoxAll").prop("checked", false);
			}
		});
	},
	click: function(){
		$("#orderGrid").on("click", "tbody > tr > td", function(e) {
			//e.preventDefault();
			let fieldId = e.currentTarget.dataset.field;	//컬럼 정보
			let dataItems = orderGrid.dataItem(orderGrid.select()); //선택데이터 정보
			let serverUrlObj = fnGetServerUrl();

			//주문상세 (임시 goodsId -> 주문번호 로 변경 필요)
			if(fieldId === "odid" || fieldId === "odOrderDetlSeq") {

				let odid = dataItems.odid
				//window.open("#/orderMgm?odOrderId="+odOrderId,"_blank");
				var orderMenuId = $("#lnbMenuList li").find("a[data-menu_depth='3'].active").attr("id");
				//localStorageUtil.put("orderMenuId", inputMenuId);

				window.open("#/orderMgm?orderMenuId="+ orderMenuId +"&odid="+odid ,"popForm_"+odid);
				//회원상세 (임시 1. brandName -> 주문자정보(urUserId)로 변경 필요 2.purchaseNonmemberYn -> 회원주문 조건 변경 필요 ) && dataItems.purchaseNonmemberYn === 'Y'
			} else if (fieldId === "buyerNm"  ) {
				//임시
				let urUserId = dataItems.urUserId;
				if( typeof urUserId != "undefined" && urUserId != null && urUserId != "" ) {
					if (fnIsProgramAuth("USER_POPUP_VIEW") == true) {
						fnKendoPopup({
							id: 'buyerPopup',
							title: fnGetLangData({ nullMsg: '회원상세' }),
							param: { "urUserId": urUserId },
							src: '#/buyerPopup',
							width: '1200px',
							height: '700px',
							success: function(id, data) {

							}
						});
					}
				}
			} else if (fieldId === "goodsNm"  ) {
				let ilGoodsId = dataItems.ilGoodsId;
				window.open(serverUrlObj.mallUrl+"/shop/goodsView?goods="+ilGoodsId ,"_blank");

			} else if(fieldId === "ilItemCd"){
				let ilItemCode = dataItems.ilItemCd;
				let isErpItemLink = dataItems.erpIfYn == 'Y' ? true : false;
				let masterItemType = dataItems.itemTp;
				window.open("#/itemMgmModify?ilItemCode="+ilItemCode+"&isErpItemLink="+isErpItemLink+"&masterItemType="+masterItemType , "_blank");

			} else if(fieldId === "ilGoodsId"){
				let ilGoodsId = dataItems.ilGoodsId;

				// 묶음상품
				if(dataItems.goodsTpCd == 'GOODS_TYPE.PACKAGE'){
					window.open("#/goodsPackage?ilGoodsId="+ilGoodsId,"_blank");
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
			} else if(fieldId === "odClaimId" || fieldId === "claimOdClaimId"){
				var isSubmit = false;
				var selectedRowClaimData = orderGrid.dataItem($(e.currentTarget).closest('tr'));
				var json = JSON.parse(selectedRowClaimData.bosJson);
				// 변경할 주문상태값

				console.log(json[1].rows)
				console.log(json[1].rows[0].actionRows)
				console.log(stringUtil.getString(json[1].rows[0].actionRows, "") )

				if(stringUtil.getString(json[1].rows[0].actionRows, "") != "") {

					var actionExecId = json[1].rows[0].actionRows[0].actionExecId;


					var btnOrderStatusCd = "";//$('button[name='+json[1].rows[0].actionRows[0].actionExecId+']').val();
					var btnOrderStatus = ""; //$('button[name='+json[1].rows[0].actionRows[0].actionExecId+']').html();
					var selectRowsClaim = selectedRowClaimData;//탭별 선택한 데이터

					if (actionExecId == "putOrderStatusDR") {
						btnOrderStatusCd = "DR";
						btnOrderStatus = "배송준비중";
					} else if (actionExecId == "putOrderStatusDI") {
						btnOrderStatusCd = "DI";
						btnOrderStatus = "배송중";
					} else if (actionExecId == "putOrderStatusBF") {
						btnOrderStatusCd = "BF";
						btnOrderStatus = "구매확정";
					} else if (actionExecId == "putOrderStatusC") {
						btnOrderStatusCd = "C";
						btnOrderStatus = "취소";
					} else if (actionExecId == "putOrderStatusEC") {
						btnOrderStatusCd = "EC";
						btnOrderStatus = "재배송";
					} else if (actionExecId == "putOrderStatusCS") {
						btnOrderStatusCd = "CS";
						btnOrderStatus = "CS환불";
					} else if (actionExecId == "putOrderStatusR") {
						btnOrderStatusCd = "R";
						btnOrderStatus = "반품";
					}

					//클레임상세팝업 parameter set
					let putOrderStatusInfo = {};
					let claimData = {};
					claimData.odid = selectedRowClaimData.odid; //odid
					claimData.urUserId = selectedRowClaimData.urUserId; //urUserId
					claimData.guestCi = selectedRowClaimData.guestCi;//guestCi
					var goodSearchList = new Array();

					let goodOrgData = {};
					goodOrgData.claimCnt = selectedRowClaimData.cancelCnt;
					goodOrgData.odOrderId = selectedRowClaimData.odOrderId;
					goodOrgData.odOrderDetlId = selectedRowClaimData.odOrderDetlId;
					goodOrgData.odClaimDetlId = selectedRowClaimData.odClaimDetlId;
					goodOrgData.urWarehouseId = selectedRowClaimData.urWarehouseId;
					goodOrgData.claimGoodsYn = "N";
					goodSearchList.push(goodOrgData);
					claimData.goodSearchList = goodSearchList;

					claimData.orderStatusCd = selectedRowClaimData.claimStatusCd;
					claimData.orderStatus = selectedRowClaimData.orderClaimStatus;
					putOrderStatusInfo = orderStatusChangeUtil.fnPutOrderStatus(selectedRowClaimData.claimStatusCd, btnOrderStatusCd);

					claimData.putOrderStatusCd = stringUtil.getString(putOrderStatusInfo.putOrderStatusCd, ""); //주문상태코드(변경)
					if (putOrderStatusInfo.putOrderStatus == "") {
						claimData.putOrderStatus = btnOrderStatus; //주문상태코드명(변경) 변화 X
					} else {
						claimData.putOrderStatus = putOrderStatusInfo.putOrderStatus; //주문상태코드명(변경)
					}
					//end

					claimData.odOrderId = selectedRowClaimData.odOrderId; //주문pk
					claimData.odClaimId = selectedRowClaimData.odClaimId; //주문클레임pk
					claimData.goodsChange = 0;
					claimData.btnOrderStatusCd = btnOrderStatusCd; //버튼주문상태코드(변경-하드코딩) 'CS'
					claimData.successDo = false;

					fnKendoPopup({
						id: 'claimMgmPopup',
						title: '클레임 상세',
						param: claimData,
						src: '#/claimMgmPopup',
						width: '1800px',
						height: '1500px',
						success: function (id, data) {
							if (data.parameter.successDo == true && data.parameter != undefined) {
								odid = data.parameter.odid;
								fnInitGrid();
							} else {
								return false;
							}
						}
					});
				} else {
					return false;
				}
			return false;
			}
		});
	}
}

// ==========================================================================
// # Kendo Grid 전용 rowSpan 메서드
//  - @param gridId : div 로 지정한 그리드 ID, 해당 div 내 table 태그를 찾아감
//  - @param mergeColumns : 그리드에서 셀 머지할 컬럼들의 data-field 목록
//  - @param groupByColumns : group by 할 컬럼들의 data-field 목록, 해당 group 내에서만 셀 머지가 일어남
// ==========================================================================
function mergeGridRows(gridId, mergeColumns, groupByColumns) {
	// 데이터 1건 이하인 경우 : rowSpan 불필요하므로 return
	if( $('#' + gridId + '  >div>table>tbody>tr').length <= 1 ) {
		return;
	}


	var groupByColumnIndexArray = [];   // group by 할 컬럼들의 th 헤더 내 column index 목록
	var tdArray = [];                   // 해당 컬럼의 모든 td 배열, 개수 / 순서는 그리드 내 tr 개수 / 순서와 같음
	var groupNoArray = [];              // 파라미터로 전달된 groupByColumns 에 따라 계산된 그룹번호 배열, 같은 그룹인 경우 그룹번호 같음, 개수 / 순서는 tdArray 와 같음

	var groupNo;            // 각 tr 별 그룹번호, 같은 그룹인 경우 그룹번호 같음
	var beforeTr = null;    // 이전 tr
	var beforeTd = null;    // 이전 td
	var rowspan = null;     // rowspan 할 개수, 1 인경우 rowspan 하지 않음

	// 해당 그리드의 th 헤더 row
	var thRow = $('#' + gridId + '>div>div>table>thead>tr')[0];

	// 셀 머지시 group by 할 컬럼들의 data-field 목록이 Array 형태의 파라미터로 전달시
	if( groupByColumns && Array.isArray(groupByColumns) && groupByColumns.length > 0 ) {

		$(thRow).children('th').each(function (thIndex, th) {  // thead 의 th 반복문 시작 : table 내 각 컬럼별로 반복
			// groupByColumns => groupByColumnIndexArray 로 변환
			if( groupByColumns.includes( $(th).attr('data-field') ) ) {
				groupByColumnIndexArray.push(thIndex);
			}
		});
	} // if 문 끝

	// ------------------------------------------------------------------------
	// tbody 내 tr 반복문 시작
	// ------------------------------------------------------------------------
	$('#' + gridId + ' >div>table>tbody>tr').each(function() {
		beforeTr = $(this).prev();        // 이전 tr
		// 첫번째 tr 인 경우 : 이전 tr 없음
		if( beforeTr.length == 0 ) {
			groupNo = 0;                    // 그룹번호는 0 부터 시작
			groupNoArray.push(groupNo);     // 첫번째 tr 의 그룹번호 push
		}
		else {
			var sameGroupFlag = true;       // 이전 tr 과 비교하여 같은 그룹인지 여부 flag, 기본값 true

			for( var i in groupByColumnIndexArray ) {
				var groupByColumnIndex = groupByColumnIndexArray[i];  // groupByColumns 로 전달된 각 column 의 index
				// 이전 tr 과 현재 tr 비교하여 group by 기준 컬럼의 html 값이 하나라도 다른 경우 flag 값 false ( 같은 그룹 아님 )
				if( $(this).children().eq(groupByColumnIndex).html() != $(beforeTr).children().eq(groupByColumnIndex).html() ) {
					sameGroupFlag = false;
				}
			}

			// 이전 tr 의 값과 비교하여 같은 그룹이 아닌 경우 : groupNo 1 증가시킴
			if( ! sameGroupFlag ) {
				groupNo++;
			}

			groupNoArray.push(groupNo); // 해당 tr 의 그룹번호 push
		}

	});
	// tbody 내 tr 반복문 끝
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// thead 의 th 반복문 시작 : table 내 각 컬럼별로 반복
	// ------------------------------------------------------------------------
	$(thRow).children('th').each(function (thIndex, th) {
		if( ! mergeColumns.includes( $(th).attr('data-field') ) ) {
			return true;    // mergeColumns 에 포함되지 않은 컬럼인 경우 continue
		}

		tdArray = [];  // 값 초기화
		beforeTd = null;
		rowspan = null;

		var colIdx = $("th", thRow).index(this);  // 해당 컬럼 index

		$('#' + gridId + ' >div>table>tbody>tr').each(function() {  // tbody 내 tr 반복문 시작
			var td = $(this).children().eq(colIdx);
			tdArray.push(td);
		});  // tbody 내 tr 반복문 끝

		// ----------------------------------------------------------------------
		// 해당 컬럼의 td 배열 반복문 시작
		// ----------------------------------------------------------------------
		for( var i in tdArray ) {
			var td = tdArray[i];

			if( (gridId == 'shippingGrid' && tdArray[i].prevObject[14].innerText == "DELIVERY_TYPE.DAILY") || //배송정보그리드 && 일일배송인 경우 rowspan X
				(gridId == 'orderGoodsGrid' && tdArray[i].prevObject[26].innerText == "N"))	  				  //주문상품정보그리드 && 합배송제외인 경우 rowspan X
			{
				beforeTd = td;
			}else {
				if ( i > 0 && groupNoArray[i-1] == groupNoArray[i] && $(td).html() == $(beforeTd).html() ) {
					rowspan = $(beforeTd).attr("rowSpan");

					if ( rowspan == null || rowspan == undefined ) {
						$(beforeTd).attr("rowSpan", 1);
						rowspan = $(beforeTd).attr("rowSpan");
					}

					rowspan = Number(rowspan) + 1;

					$(beforeTd).attr("rowSpan",rowspan);
					$(td).hide(); // .remove(); // do your action for the old cell here
				}
				else {
					beforeTd = td;
				}
			}

			beforeTd = ( beforeTd == null || beforeTd == undefined ) ? td : beforeTd; // set the that if not already set
		}
		// 해당 컬럼의 td 배열 반복문 끝
		// ----------------------------------------------------------------------
	});
	// thead 의 th 반복문 끝
	// ------------------------------------------------------------------------

}


function goOrderDetailView(odid){
	var orderMenuId = $("#lnbMenuList li").find("a[data-menu_depth='3'].active").attr("id");
	window.open("#/orderMgm?orderMenuId="+ orderMenuId +"&odid="+odid ,"popForm_"+odid);
}

function goOrderRegularDetailView(odRegularReqId) {
	var inputMenuId = $("#lnbMenuList li").find("a[data-menu_depth='3'].active").attr("id");
	//페이지 창 open
	window.open("#/regularDeliveryReqDetail?inputMenuId="+inputMenuId+"&odRegularReqId=" + odRegularReqId, "_blank");
}

function initOrderPresentInfo(odOrderId){

	fnAjax({
		url     : '/admin/order/getOrderPresentInfo',
		params  : {odOrderId : odOrderId},
		async   : false,
		success :
			function( data ){
				if(data != null) {
					orderPresentInfo = data;
					let presentReceiveHp = data.presentReceiveHp.replace(/\-/g,'') == "" ?
										   "01000000000" : data.presentReceiveHp.replace(/\-/g,''); // 휴대폰번호 하이픈(-) 제거
					let presentMsgSendCntStr = "메세지 재발송";

					// 선물하기 상태가 대기일때만 수정 가능
					if(data.presentOrderStatus == 'PRESENT_ORDER_STATUS.WAIT') {
						$("#presentReceiveNm").attr('disabled', false);
						$("#presentReceiveHp_prefix").attr('disabled', false);
						$("#presentReceiveHp_phone1").attr('disabled', false);
						$("#presentReceiveHp_phone2").attr('disabled', false);
						$("#presentCardMsg").attr('disabled', false);
						$("#fnPresentSave").attr('disabled', false);
						$("#fnPresentMsgSend").attr('disabled', false);
					} else {
						$("#presentReceiveNm").attr('disabled', true);
						$("#presentReceiveHp_prefix").attr('disabled', true);
						$("#presentReceiveHp_phone1").attr('disabled', true);
						$("#presentReceiveHp_phone2").attr('disabled', true);
						$("#presentCardMsg").attr('disabled', true);
						$("#fnPresentSave").attr('disabled', true);
						$("#fnPresentMsgSend").attr('disabled', true);
					}

					$('#presentReceiveHp_prefix').val(presentReceiveHp.slice(0,3));
					$('#presentReceiveHp_phone1').val(presentReceiveHp.slice(3,(presentReceiveHp.length)-4));
					$('#presentReceiveHp_phone2').val(presentReceiveHp.slice((presentReceiveHp.length)-4,presentReceiveHp.length));
					$('#presentReceiveNm').val(data.presentReceiveNm);
					$('#presentCardMsg').val(data.presentCardMsg);
					$('#fnPresentMsgSend').text(presentMsgSendCntStr);

				}
			},
		isAction : 'select'
	});
}