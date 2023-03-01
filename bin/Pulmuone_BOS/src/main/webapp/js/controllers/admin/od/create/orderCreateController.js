/**----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 주문 생성 > 주문 생성
 * @
 * @ 수정일			수정자		수정내용
 * @ --------------------------------------------------------------------------
 * @ 2021.04.07     이규한		최초 생성
 * **/

'use strict';

// 상품정보 그리드
var orderGoodsGrid, orderGoodsGridOpt;
// 주문정보 그리드
var orderGrid, orderGridOpt;
// GET Parameter
var pageParam = fnGetPageParam();
// 그리드 NoData 메세지
var noRecodeMessage = "데이터가 존재하지 않습니다.";
// 주문자 정보(배송지, 적립금) 전역변수
var buyerBaseInfo	= new Object();
// 주문생성 엑셀업로드 Data
var orderCreateExcelDataArr = new Array();
// 배송타입 : 일반배송 고정
var deliveryType 	= 'DELIVERY_TYPE.NORMAL';
// 카트타입 : 일반 고정
var cartType 		= 'CART_TYPE.NORMAL';
// 판매유형 : 일반판매 고정
var saleType 		= 'SALE_TYPE.NORMAL';
// 엑셀 업로드 주문생성 타입 (개별주문) 고정
var orderType		= 'INDIVIDUAL';
// 상품정보 머지할 필드 목록
var orderGoodsMergeFieldArr			= ['shippingPrice', 'deliveryDt','orderIfDt'];
// 상품정보 그룹할 필드 목록
var orderGoodsMergeGroupFieldArr 	= ['deliveryType', 'shippingIndex'];
// // 주문정보 머지할 필드 목록
// var orderMergeFieldArr				= ['recvNm', 'recvHp', 'recvZipCd', 'recvAddr1', 'recvAddr2'];
// // 주문정보 그룹할 필드 목록
// var orderMergeGroupFieldArr 		= ['recvNm', 'recvHp', 'recvZipCd', 'recvAddr1', 'recvAddr2'];
// 주문정보 그룹화 할 키 리스트
var orderDeepGroupingKeys = ['recvNm', 'recvHp', 'recvZipCd', 'recvAddr1', 'recvAddr2'];
var orderGroupingKeys = ['recvNm', 'recvHp', 'recvZipCd', 'recvAddr1', 'recvAddr2', 'urWarehouseId', 'ilShippingTmplId', 'shippingPrice'];
// 주문정보 그룹화 하기 위한 데이터 변환 함수 리스트
var orderGroupingValueTransformations  = {
	recvHp: function(value) {
		if(!value) return value;

		return value.toString().replace(/\-/g, "");
	},
	shippingPrice: function(value) {
		if(!value) return value;

		return value.toString().replace(/\,/g, "");
	}
};
// 배송요청사항 목록
var deliveryMsgArr	= [
	{ "CODE" : ""	, "NAME":'선택'					},
	{ "CODE" : "1"	, "NAME":'부재 시 경비실에 맡겨주세요.'	},
	{ "CODE" : "2"	, "NAME":'부재 시 문 앞에 놓아주세요'		},
	{ "CODE" : "3"	, "NAME":'부재 시 휴대폰으로 연락바랍니다.'},
	{ "CODE" : "4"	, "NAME":'배송 전 연락바랍니다.' 		},
	{ "CODE" : "5"	, "NAME":'직접입력' 				}
];
// PROD 여부
var isProdServerYn;

var isValidBankAccount = false;

$(document).ready(function() {

	// 공통 스크립트 import
	importScript("/js/service/od/order/orderMgmGridColumns.js", function() {
		importScript("/js/service/od/order/orderMgmFunction.js", function() {
			importScript("/js/service/od/order/orderMgmPopups.js", function() {
				importScript("/js/service/od/order/orderCommSearch.js", function() {
					fnInitialize();
				});
			});
		});
	});

	// Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', { flag : true });
		fnPageInfo({
			PG_ID  		: 'orderCreate',
			callback 	: fnUI
		});
	}

	// 화면 UI 초기화
	function fnUI() {
		fnTranslate();		// 다국어 변환
		fnInitButton();		// 버튼 초기화
		fnInitServerInfo();	// 서버정보조회
		fnInitGrid();		// Initialize Grid
		fnInitOptionBox();	// Initialize Option Box
		fnOrderCreateBindEvents();		// 이벤트 바인딩
		fnSetDefault();		// 화면 기본 셋팅
	}
	//--------------------------------- Button Start-------------------------------------------
	// 버튼 초기화
	function fnInitButton() {
		$('#fnSaveTop, #fnExcelUpload, #fnExcelUploadDelete, #fnExcelUploadConfirm, #fnSampleExcelDownload, #fnSearchBuyer, #fnSearchShippingList, #fnSearchAddress, #fnSearchGoods,#fnDeleteGoods, #fnUseCoupon, #fnUseAllPointPrice, #fnSaveBottom' ).kendoButton();

		// 상품검색,삭제 버튼 비활성화
		$("#fnSearchGoods").attr('disabled',true);
		$("#fnDeleteGoods").attr('disabled',true);
	}

	// 저장 버튼 클릭
	function fnSave() {
		if (!fnSaveValid()) return false;

		fnKendoMessage({
			type    : "confirm",
			message : "주문을 생성 하시겠습니까?",
			ok      : function(e){
				// 저장버튼 비활성화
				$("#fnSaveBottom").attr('disabled',true);

				let createTp = $('[name="orderCreateTp"]').filter(':checked').val();

				// 회원,비화원
				if (createTp == 'MEMBER' || createTp == 'NON_MEMBER') {
					let url			= "/admin/order/create/createOrderPayment";
					let param 		=  new Object();

					// 주문자 정보
					param.urUserId 			= fnNvl($("#urUserId").val());		// 주문자ID
					param.urGroupId 		= fnNvl($("#urGroupId").val());		// 주문자 그룹ID
					param.userName 			= fnNvl($("#buyerName").val());		// 주문자명
					param.userMobile		= fnNvl($('#buyerHp1').data('kendoDropDownList').value()) + fnNvl($('#buyerHp2').val()) + fnNvl($('#buyerHp3').val());	// 주문자 휴대폰 번호
					param.userEmail			= fnNvl($("#buyerEmail1").val()) + '@' + fnNvl($("#buyerEmail2").val());	// 주문자 이메일

					// 배송지 정보
					param.receiverName		= fnNvl($('#recvName').val());		// 배송지 수령인명
					param.receiverMobile	= fnNvl($('#recvHp1').data('kendoDropDownList').value()) + fnNvl($('#recvHp2').val()) + fnNvl($('#recvHp3').val());	// 배송지 휴대폰 번호
					param.receiverZipCode	= fnNvl($('#recvZipCode').val());	// 배송지 우편번호
					param.buildingCode		= fnNvl($('#buildingCode').val());	// 배송지 건물관리 번호
					param.receiverAddress1	= fnNvl($('#recvAddr1').val());		// 배송지 주소1
					param.receiverAddress2	= fnNvl($('#recvAddr2').val());		// 배송지 주소2
					param.bosTp = 'BOS';										// BOS 타입

					// 배송 요청사항
					let shippingComment = '';
                    if($('#recvDeliveryMsgCd').data('kendoDropDownList').text() == '직접입력') {
                        shippingComment = $('#recvDeliveryMsgEtc').val();
                    } else if($('#recvDeliveryMsgCd').data('kendoDropDownList').text() == '선택'){
                        shippingComment = '';
                    } else {
                        shippingComment = $('#recvDeliveryMsgCd').data('kendoDropDownList').text();
                    }
                    param.shippingComment             = shippingComment;
					param.accessInformationType		= $('#recvDoorMsgCd').data('kendoDropDownList').value();	// 배송 출입정보 코드
					param.accessInformationPassword	= fnNvl($('#recvDoorMsgCdNm').val());						// 배송 출입정보 비밀번호

					let gridArr 	= orderGoodsGrid.dataSource.data();
					let cartArr 	= new Array();
					let deliveryArr = new Array();

					let freeShippingPriceYn = fnNvl($("input[name=freeShippingPriceYn]:checked").val()) == "" ? "N" : $("input[name=freeShippingPriceYn]:checked").val();

					let totalPrice  = 0;					// 총 결제금액
					for (let i=0; i<gridArr.length; i++) {
						cartArr.push(gridArr[i].spCartId);
						totalPrice += Number(fnUncomma(gridArr[i].paidPrice));
						/*if (i==0 || (gridArr[i-1].deliveryType != gridArr[i].deliveryType || gridArr[i-1].shippingIndex != gridArr[i].shippingIndex)) {
                            let scheduledItem = new Object();
                            scheduledItem.arrivalScheduledDate 	= gridArr[i].deliveryDt;
                            scheduledItem.dawnDeliveryYn 		= 'N';
                            deliveryArr.push(scheduledItem);
                            totalPrice += Number(fnUncomma(gridArr[i].shippingPrice));
                        }
                        */
						if (i==0 || (gridArr[i-1].deliveryType != gridArr[i].deliveryType || gridArr[i-1].shippingIndex != gridArr[i].shippingIndex)) {
							let scheduledItem = new Object();
							scheduledItem.arrivalScheduledDate 	= gridArr[i].deliveryDt;
							scheduledItem.dawnDeliveryYn 		= gridArr[i].orderIfDawnYn == undefined ? gridArr[i].dawnDeliveryYn : gridArr[i].orderIfDawnYn;
							deliveryArr.push(scheduledItem);
							if(freeShippingPriceYn == "N") {	totalPrice += Number(fnUncomma(gridArr[i].shippingPrice));	}
						}

						// let scheduledItem = new Object();
						// scheduledItem.arrivalScheduledDate 	= gridArr[i].deliveryDt;
						// scheduledItem.dawnDeliveryYn 		= gridArr[i].orderIfDawnYn == undefined ? gridArr[i].dawnDeliveryYn : gridArr[i].orderIfDawnYn;
						// deliveryArr.push(scheduledItem);

					}

					param.spCartIdListData 			= kendo.stringify(cartArr);			// 그리드 장바구니PK 리스트
					param.arrivalScheduledListData 	= kendo.stringify(deliveryArr);		// 배송 스케줄 변경 정보 리스트

					param.useGoodsCouponListData	= fnNvl($("#useGoodsCouponList").val());		// 상품 쿠폰 사용 리스트
					param.useShippingCouponListData	= fnNvl($("#useShippingCouponListData").val());	// 배송 쿠폰 사용 리스트
					param.useCartPmCouponIssueId	= fnNvl($("#useCartPmCouponIssueId").val());	// 장바구니 쿠폰 사용 발급 PK
					param.usePoint					= fnIsEmpty($("#usePointPrice").val()) ? 0 : fnUncomma($("#usePointPrice").val());	// 사용 적립금

					// 쿠폰, 적립금 사용에 따른 결제방식 무료 체크
					// 실제 결제 금액 = 총 결제금액 - 쿠폰 사용금액 - 적립금 사용금액
					totalPrice -= fnIsEmpty($('#discountPrice').val()) ? 0 : Number(fnUncomma($('#discountPrice').val()));
					totalPrice -= fnIsEmpty($('#usePointPrice').val()) ? 0 : Number(fnUncomma($('#usePointPrice').val()));

					// 실 결제금액 0원인 경우, 결제방식 무료 결제
					param.psPayCd = totalPrice == 0 ? 'PAY_TP.FREE' : $('[name="payTp"]:checked').val();	// 결제 정보 PK
					param.freeShippingPriceYn = fnNvl($("input[name=freeShippingPriceYn]:checked").val()) == "" ? "N" : $("input[name=freeShippingPriceYn]:checked").val();

					if(param.psPayCd == "PAY_TP.VIRTUAL_BANK") {
						param.bankCode = fnNvl($('#bankCode').data('kendoDropDownList').value()); //BANK_CODE.KB
						param.accountNumber = fnNvl($("#accountNumber").val()); //'환불계좌번호'
						param.holderName = fnNvl($("#holderName").val()); //'예금주'
						//console.log(">>> 환불계좌 : " + param.bankCode + " / " + param.accountNumber+ " / " + param.holderName);
					}

					fnAjax({
						url     : url,
						params  : param,
						async   : true,
						method	: 'POST',
						success : function(resultData) {
							resultData.psPayCd = param.psPayCd;
							fnBizCallback('save', resultData);
						},
						fail : function(resultData, resultCode){
							fnKendoMessage({
								message : resultCode.message,
								ok : function(e) {
									fnBizCallback("fail", resultCode);
								}
							});
						},
						isAction : 'insert'
					});

					// 엑셀 업로드
				} else if (createTp == 'EXCEL_UPLOAD') {
					let url			= "/admin/order/create/addOrderCreate";
					let param 		=  new Object();

					let gridArr 	= orderGrid.dataSource.data();
					let saveArr		= new Array();
					for (let i=0; i<gridArr.length; i++) {
						let item = new Object();
						item.recvNm					= fnNvl(gridArr[i].recvNm);
						item.recvHp  				= fnNvl(gridArr[i].recvHp);
						item.recvZipCd   			= fnNvl(gridArr[i].recvZipCd);
						item.recvAddr1   			= fnNvl(gridArr[i].recvAddr1);
						item.recvAddr2  			= fnNvl(gridArr[i].recvAddr2);
						item.ilItemCd    			= fnNvl(gridArr[i].ilItemCd);
						item.itemCode    			= fnNvl(gridArr[i].ilItemCd);
						item.itemBarcode 			= fnNvl(gridArr[i].itemBarcode);
						item.goodsId     			= fnNvl(gridArr[i].ilGoodsId);
						item.ilGoodsId     			= fnNvl(gridArr[i].ilGoodsId);
						item.goodsName   			= fnNvl(gridArr[i].goodsName);
						item.storageMethodTypeCode  = fnNvl(gridArr[i].storageMethodTypeCode);
						item.storageMethodTypeName	= fnNvl(gridArr[i].storageMethodTypeName);
						item.orderCnt        		= fnNvl(gridArr[i].orderCnt);
						item.recommendedPrice       = fnNvlForNumber(gridArr[i].recommendedPrice);
						item.salePrice        		= fnNvlForNumber(gridArr[i].salePrice);
						item.orgSalePrice        	= fnNvlForNumber(gridArr[i].orgSalePrice);
						item.orderAmt        		= fnNvlForNumber(gridArr[i].orderAmt);
						item.grpShippingId        	= fnNvl(gridArr[i].grpShippingId);
						item.ilShippingTmplId       = fnNvl(gridArr[i].ilShippingTmplId);
						item.urWarehouseId        	= fnNvl(gridArr[i].urWarehouseId);
						item.goodsTp        		= fnNvl(gridArr[i].goodsTp);
						saveArr.push(item);
					}

					param.insert	= kendo.stringify(saveArr);								// 엑셀 업로드 주문 목록
					param.orderType	= orderType;											// 개별주문 생성 (INDIVIDUAL)
					param.buyerNm	= fnNvl($("#buyerName").val());							// 주문자명
					param.buyerHp	= fnNvl($('#buyerHp1').data('kendoDropDownList').value()) + fnNvl($('#buyerHp2').val()) + fnNvl($('#buyerHp3').val());	// 주문자 휴대폰 번호
					param.buyerMail	= fnNvl($("#buyerEmail1").val()) + '@' + fnNvl($("#buyerEmail2").val());	// 주문자 이메일
					param.psPayCd 	= $('[name="payTp"]:checked').val();					// 결제 정보 PK
					param.freeShippingPriceYn = fnNvl($("input[name=freeShippingPriceYn]:checked").val()) == "" ? "N" : $("input[name=freeShippingPriceYn]:checked").val();

					if(param.psPayCd == "PAY_TP.VIRTUAL_BANK") {
						param.bankCode = fnNvl($('#bankCode').data('kendoDropDownList').value()); //BANK_CODE.KB
						param.accountNumber = fnNvl($("#accountNumber").val()); //'환불계좌번호'
						param.holderName = fnNvl($("#holderName").val()); //'예금주'
						//console.log(">>> 환불계좌 : " + param.bankCode + " / " + param.accountNumber+ " / " + param.holderName);
					}

					fnAjax({
						url     : url,
						params  : param,
						/*async   : false,
                        method	: "POST",*/
						success : function(resultData) {

							resultData.psPayCd = param.psPayCd;
							fnBizCallback('save', resultData);
						},
						fail : function(resultData, resultCode){
							fnKendoMessage({
								message : resultCode.message,
								ok : function(e) {
									fnBizCallback("fail", resultCode);
								}
							});
						},
						isAction : 'insert'
					});
				}
			},
			cancel  : function(e){}
		});
	}

	// 엑셀 업로드 파일선택 버튼 클릭
	function fnExcelUpload() {
		fileClear();						// 파일초기화
		$("#uploadFile").trigger("click");	// fnExcelExport(e) 호출
	}

	// 엑셀 업로드 파일선택 버튼 클릭시 파일버튼 - 엑셀 업로드 (SheetJs)
	function fnExcelExport(e) {
		// Excel Data => Javascript Object 로 변환
		let f = e.target.files[0];
		let fileLength = f.name.length;
		let lastDot = f.name.lastIndexOf('.');
		let ext = f.name.substring(lastDot+1, fileLength); // 확장자

		if ($.inArray(ext.toLowerCase(), [ "xls", "xlsx" ]) == -1) {
			fnKendoMessage({
				message : "파일형식을 확인 해주세요."
			});
			e.preventDefault();
			return false;
		} else {
			$("#uploadViewControl").show();
			$("#uploadLink").text(f.name);
		}
	}

	// 엑셀 업로드 파일첨부 삭제 버튼 클릭
	function fnExcelUploadDelete(e) {
		e.preventDefault();
		fileClear();
		$("#uploadViewControl").hide();
		$("#uploadLink").text("");
	}

	// 업로드 버튼 클릭
	function fnExcelUploadConfirm() {
		let url	= "/admin/order/create/addBosCreateExcelUpload";

		if (fnIsEmpty($("#uploadFile").val())) {
			fnKendoMessage({message:'업로드할 엑셀파일을 선택해주세요.'});
		} else {
			fnKendoMessage({
				type    : "confirm",
				message : "업로드를 하시겠습니까?",
				ok      : function(e){fnExcelUploadSubmit(url, "excelUpload");},
				cancel  : function(e){}
			});
		}
	}

	// Sample 다운로드 버튼
	function fnSampleExcelDownload() {
		document.location.href = "/contents/excelsample/ordercreate/sampleOrderCreate.xlsx";
	}

	// 영역 보이기 / 숨기기
	// btnDom - 버튼 dom 객체, startNo - table 에서 숨기기 시작하는 tr 번호 (default = 2 로 두번째 tr 부터 숨기기 시작한다), excpetionArea - 적용 예외 영역명
	function fnToggleArea(btnDom, startNo, excpetionArea) {

		let $btn = $(btnDom);
		let $table = $btn.closest("table");

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

	// 회원 검색 버튼 클릭
	function fnSearchBuyer() {
		fnKendoPopup({
			id     : 'cpnMgmIssueList',
			title  : '회원 검색',
			src    : '#/cpnMgmIssueList',
			width  : '1100px',
			height : '900px',
			param  : {"pmCouponId" : '', "orderCreateYn" : "Y"},
			success: function(id, data) {
				// 선택한 주문자 정보 x
				if (fnIsEmpty(data.rows)) return false;
				// 주문자 2명이상 선택시
				if (data.rows.length != 1) {
					fnKendoMessage({message : "주문자는 한명만 선택 할 수 있습니다."});
					return false;
				}

				let buyerInfo = data.rows[0];
				let buyerHp = fnPhoneNumberHyphen(fnNvl(buyerInfo.orgMobile)).split("-");

				if (buyerHp.length != 3) {
					fnKendoMessage({message : "휴대폰 정보가 유효하지 않습니다. 확인하시기 바랍니다."});
					return false;
				}

				// 상품정보 그리드 초기화
				//$('#orderGoodsGrid').gridClear(true);	// 상품정보 그리드 초기화
				fnClearPayData();						// 결제정보 초기화

				// 주문자명
				$('#buyerName').val(fnNvl(buyerInfo.orgUserName));
				// 주문자ID
				$("#urUserId").val(fnNvl(buyerInfo.urUserId));
				// 회원 그룹 PK
				$("#urGroupId").val(fnNvl(buyerInfo.urGroupId));
				// 휴대폰번호
				let dropdownlist = $("#dropdownlist").data("kendoDropDownList");
				$('#buyerHp1').data('kendoDropDownList').value(buyerHp[0]);
				$('#buyerHp2').val(buyerHp[1]);
				$('#buyerHp3').val(buyerHp[2]);

				// 이메일
				let buyerEmail = fnNvl(buyerInfo.orgEmail).split("@");
				if (buyerEmail.length == 2) {
					$("#buyerEmail1").val(buyerEmail[0]);
					$("#buyerEmail2").val(buyerEmail[1]);
				}

				fnSearchBuyerBaseInfo(buyerInfo.urUserId);
			}
		});
	}

	// 배송지 목록 버튼 클릭
	function fnSearchShippingList() {
		if (fnIsEmpty($('#urUserId').val())) {
			fnKendoMessage({message : "회원정보를 선택해 주시기 바랍니다."});
			return false;
		}

		fnKendoPopup({
			id     : 'buyerShoppingAddrListPopup',
			title  : '수취 정보 변경',
			src    : '#/buyerShoppingAddrListPopup',
			width  : '600px',
			height : '850px',
			param  : {urUserId : fnNvl($('#urUserId').val())},
			success: function(id, data) {
				fnSetBuyerAddressInfo(data);
			}
		});
	}

	// 주소 찾기 버튼 클릭
	function fnSearchAddress() {
		fnDaumPostcode("recvZipCode", "recvAddr1", "recvAddr2","buildingCode");
	}

	// 상품 검색 버튼 클릭
	function fnSearchGoods() {
		let params = new Object();


		params.goodsType = "";
		params.selectType = "multi";
		params.searchChoiceBtn = "Y";
		params.columnNameHidden = false;
		params.columnAreaShippingDeliveryYnHidden = false;
		params.columnDpBrandNameHidden = false;
		params.columnStardardPriceHidden = false;
		params.columnRecommendedPriceHidden = false;
		params.columnSalePriceHidden = false;
		params.columnSaleStatusCodeNameHidden = false;
		params.columnGoodsDisplyYnHidden = false;
		params.orderCreateYn = "Y"

		fnKendoPopup({
			id			: "goodsSearchPopup",
			title		: "상품 검색",		// 해당되는 Title 명 작성
			width		: "1700px",
			height		: "800px",
			src			: "#/goodsSearchPopup",
			param		: params,
			success		: function(id, data) {
				if (!fnIsEmpty(data) && data.length > 0) {

					// 임직원전용상품 구매 불가
					for(var i=0 ; i<data.length ; i++){
						if(data[i].purchaseEmployeeYn == "Y" && data[i].purchaseMemberYn == "N" && data[i].purchaseNonmemberYn == "N"){
							fnKendoMessage({message : "임직원전용 상품은 주문할 수 없습니다."});
							return false;
						}
					}

					// 비회원일 경우 일일상품, 회원전용상품 주문 생성 불가
					if($('[name="orderCreateTp"]').filter(':checked').val() == 'NON_MEMBER'){
						for(var i=0 ; i<data.length ; i++){
							if(data[i].goodsTypeCode == "GOODS_TYPE.DAILY"){
								fnKendoMessage({message : "비회원은 일일상품을 주문할 수 없습니다."});
								return false;
							}
							if(data[i].purchaseNonmemberYn == "N"){
								fnKendoMessage({message : "비회원은 회원전용상품을 주문할 수 없습니다."});
								return false;
							}
						}
					}

					// 주문생성  장바구니 정보 생성
					fnAddCartInfo(data);
				}
			}
		});
	}

	// 상품 삭제 버튼 클릭
	function fnDeleteGoods() {

		let selectRowsGoods = $("#orderGoodsGrid").find("input[name=rowCheckbox]:checked").closest("tr");
		let selectSpCartIdList = new Array();
		let selectSpCartAddGoodsIdList = new Array();

		if (selectRowsGoods.length == 0) {
			fnKendoMessage({message : "삭제하려는 상품을 선택해주세요."});
			return false;
		} else {
			for(let i = 0 ; i < selectRowsGoods.length ; i++){
				let selectedRowGoodsData = new Object();
				selectedRowGoodsData = orderGoodsGrid.dataItem($(selectRowsGoods[i]));

				// 추가상품
				if(selectedRowGoodsData.spCartAddGoodsId != undefined && selectedRowGoodsData.spCartAddGoodsId > 0){
					selectSpCartAddGoodsIdList.push(selectedRowGoodsData.spCartAddGoodsId);

					// 일반상품
				}else{
					selectSpCartIdList.push(selectedRowGoodsData.spCartId);
				}
			}
		}

		let params = new Object();

		params.spCartId = selectSpCartIdList;
		params.urUserId = $("#urUserId").val();
		params.spCartAddGoodsId = selectSpCartAddGoodsIdList;

		fnAjax({
			url     : '/admin/order/create/delCartAndAddGoods',
			params  : params,
			async   : true,
			contentType: "application/json",
			success : function(resultData) {

				// 그리드에서 상품 삭제
				for(let i = 0 ; i < selectRowsGoods.length ; i++){
					orderGoodsGrid.removeRow($(selectRowsGoods[i]));
				}

				$("#checkBoxAll").prop('checked', false);

				// 장바구니 상품 조회
				fnGetCartInfo();
			},
			isAction : 'select'
		});
	}

	// 쿠폰 사용 버튼 클릭
	function fnUseCoupon() {
		let gridArr 	= orderGoodsGrid.dataSource.data();

		if (fnIsEmpty($('#urUserId').val())) {
			fnKendoMessage({message : "회원정보를 선택해 주시기 바랍니다."});
			return false;
		}

		if (fnIsEmpty(gridArr) || gridArr.length < 1) {
			fnKendoMessage({message : "상품정보를 선택해 주시기 바랍니다."});
			return false;
		}

		if (fnIsEmpty($('#couponCount').text()) || Number(fnUncomma($('#couponCount').text())) < 1) {
			fnKendoMessage({message : "사용가능한 쿠폰정보가 없습니다."});
			return false;
		}

		let cartArr 	= new Array();
		let deliveryArr = new Array();
		/*    	for (let i=0; i<gridArr.length; i++) {
                    cartArr.push(gridArr[i].spCartId);
                    if (i==0 || (gridArr[i-1].deliveryType != gridArr[i].deliveryType || gridArr[i-1].shippingIndex != gridArr[i].shippingIndex)) {
                        let scheduledItem = new Object();
                        scheduledItem.arrivalScheduledDate 	= gridArr[i].deliveryDt;
                        deliveryArr.push(scheduledItem);
                    }
                }*/

		for (let i=0; i<gridArr.length; i++) {
			cartArr.push(gridArr[i].spCartId);
			let scheduledItem = new Object();

			scheduledItem.arrivalScheduledDate 	= gridArr[i].deliveryDt;
			scheduledItem.dawnDeliveryYn 		= 'N';
			deliveryArr.push(scheduledItem);
		}

		let param = new Object();
		param.urUserId 					= $("#urUserId").val();				// 주문자ID
		param.spCartIdListData 			= kendo.stringify(cartArr);			// 그리드 장바구니PK 리스트
		param.arrivalScheduledListData 	= kendo.stringify(deliveryArr);		// 배송 스케줄 변경 정보 리스트

		fnKendoPopup({
			id: "orderCouponPopup",
			title: "쿠폰 적용",
			src: "#/orderCouponPopup",
			param: param,
			width: "400px",
			height: "550px",
			success: function(id, data) {
				if (fnIsEmpty(data)) return false;
				let cartCouponDiscount 		= fnIsEmpty(data.cartCouponDiscount) ? 0 : data.cartCouponDiscount;
				let goodsCouponDiscount 	= fnIsEmpty(data.goodsCouponDiscount) ? 0 : data.goodsCouponDiscount;
				let shippingCouponDiscount 	= fnIsEmpty(data.shippingCouponDiscount) ? 0 : data.shippingCouponDiscount;
				let discountPrice = Number(cartCouponDiscount) + Number(goodsCouponDiscount) + Number(shippingCouponDiscount);
				$('#discountPrice').val(fnNumberWithCommas(discountPrice));	// 쿠폰 사용 금액
				$('#usePointPrice').val('');								// 적립금 사용 금액

				// 상품 쿠폰
				if (!fnIsEmpty(data.goodsCoupon) && data.goodsCoupon.length > 0) {
					let goodsCouponArr = new Array();
					for (let i=0; i<data.goodsCoupon.length; i++) {
						let item = data.goodsCoupon[i];

						let itemObj = new Object();
						itemObj.spCartId 		= item.coupon.spCartId;
						itemObj.pmCouponIssueId = item.coupon.pmCouponIssueId;
						goodsCouponArr.push(itemObj);
					}
					$('#useGoodsCouponList').val(kendo.stringify(goodsCouponArr));
				}

				// 배송비 쿠폰
				if (!fnIsEmpty(data.shippingCoupon) && data.shippingCoupon.length > 0) {
					let shippingCouponArr = new Array();
					for (let i=0; i<data.shippingCoupon.length; i++) {
						let item = data.shippingCoupon[i];

						let itemObj = new Object();
						itemObj.shippingIndex 		= item.shippingIndex;
						itemObj.pmCouponIssueId 	= item.coupon.pmCouponIssueId;
						shippingCouponArr.push(itemObj);
					}
					$('#useShippingCouponListData').val(kendo.stringify(shippingCouponArr));
				}

				// 장바구니 쿠폰
				if (!fnIsEmpty(data.cartCoupon)) {
					$('#useCartPmCouponIssueId').val(data.cartCoupon.pmCouponIssueId);
				}
			}
		});
	}

	// 적립금 전체 적용 버튼 클릭
	function fnUseAllPointPrice() {
		let gridArr 	= orderGoodsGrid.dataSource.data();

		if (fnIsEmpty($('#urUserId').val())) {
			fnKendoMessage({message : "회원정보를 선택해 주시기 바랍니다."});
			return false;
		}

		if (fnIsEmpty(gridArr) || gridArr.length < 1) {
			fnKendoMessage({message : "상품정보를 선택해 주시기 바랍니다."});
			return false;
		}

		if (fnIsEmpty($('#pointPrice').text()) || Number(fnUncomma($('#pointPrice').text())) < 1) {
			fnKendoMessage({message : "사용가능한 적립금이 없습니다."});
			return false;
		}

		let freeShippingPriceYn = fnNvl($("input[name=freeShippingPriceYn]:checked").val()) == "" ? "N" : $("input[name=freeShippingPriceYn]:checked").val();

		// 총 결제금액
		let totalPrice  = 0;
		for (let i=0; i<gridArr.length; i++) {
			totalPrice += Number(fnUncomma(gridArr[i].paidPrice));
			if (i==0 || (gridArr[i-1].deliveryType != gridArr[i].deliveryType || gridArr[i-1].shippingIndex != gridArr[i].shippingIndex)) {
				if(freeShippingPriceYn == "N") {	totalPrice += Number(fnUncomma(gridArr[i].shippingPrice));	}
			}
		}

		// 총 결제금액 - 쿠폰 사용금액
		totalPrice -= fnIsEmpty($('#discountPrice').val()) ? 0 : Number(fnUncomma($('#discountPrice').val()));
		let pointPrice = fnIsEmpty($('#pointPrice').text()) ? 0 : Number(fnUncomma($('#pointPrice').text()));
		$('#usePointPrice').val(pointPrice >= totalPrice ? fnNumberWithCommas(totalPrice) : fnNumberWithCommas(pointPrice));
	}
	//--------------------------------- Button End---------------------------------------------


	//------------------------------- Grid Start ----------------------------------------------
	// 서버정보 조회
	function fnInitServerInfo() {
		fnAjax({
			url     : "/admin/order/create/getServerInfo",
			params  : '',
			async   : false,
			method	: 'POST',
			success : function(resultData) {
				isProdServerYn = "Y";
				if(fnNvl(resultData) != '') {
					isProdServerYn = resultData.isProdServerYn;
				}
			},
			isAction : 'select'
		});
	}
	// 그리드 초기화
	function fnInitGrid() {
		// 상품정보 그리드
		fnInitOrderGoodsGrid();
		// 주문정보 그리드
		fnInitOrderGrid();
	}

	// 상품정보 그리드 초기화
	function fnInitOrderGoodsGrid() {
		// 상품정보 그리드 기본옵션
		orderGoodsGridOpt = {
			navigatable	: true,
			scrollable	: true,
			editable	: {
				confirmation:false
			},
			columns 	: orderMgmGridUtil.orderCreateGoodsList()
		};
		orderGoodsGrid = $('#orderGoodsGrid').initializeKendoGrid(orderGoodsGridOpt).cKendoGrid();

		orderGoodsGrid.bind("dataBound", function(e) {
			fnMergeGridRows('orderGoodsGrid', orderGoodsMergeFieldArr, orderGoodsMergeGroupFieldArr);
		});

		// 상품정보 그리드 change
		$("#orderGoodsGrid").on("change", "tbody > tr > td", function(e) {
			// 주문수량 변경시 장바구니 정보 수정
			let field 		= e.currentTarget.dataset.field;
			if (field === "orderCnt") {
			    var paramObj = new Object();
			    //paramObj.orderCnt = $(this).find('input').val();
                var selectedRowData = "";
                selectedRowData = orderGoodsGrid.dataItem($(this).closest('tr'));

                if ($(e.target).val() > selectedRowData.stockQty)
                    paramObj.orderCnt = selectedRowData.stockQty;
                else
                    paramObj.orderCnt = $(e.target).val();

			    fnPutCartInfo(paramObj);
            }
		});
	}

	// 주문정보 그리드 초기화
	function fnInitOrderGrid() {
		// 배송정보 그리드 기본옵션
		orderGridOpt = {
			navigatable	: true,
			scrollable 	: true,
			columns 	: orderMgmGridUtil.orderCreateList()
		};
		orderGrid = $('#orderGrid').initializeKendoGrid(orderGridOpt).cKendoGrid();

		orderGrid.bind("dataBound", function(e) {
			//fnMergeGridRows('orderGrid', orderMergeFieldArr, orderMergeGroupFieldArr);
			fnOrderMergeGridRows('orderGrid', orderDeepGroupingKeys, orderGroupingKeys, orderGroupingValueTransformations);
			$('#countTotalSpan').text( kendo.toString($("#orderGrid").find("td[data-field=recvNm]:visible").length, "n0") );
		});
	}

	/**
	 * 주문정보 그룹 별 데이터 키 조회(DOM 데이터 기준)
	 *
	 * @param {array} order 주문정보
	 * @param {array} keys 그룹화 할 키
	 * @param {object} transformations 그룹화 데이터 변환 필요한 함수
	 * @returns {string}
	 */
	function getRefChainValue(refs, keys, transformation) {
		const values = [];

		for(const key of keys) {
			for(const ref of refs) {
				const field = ref.dataset || ref.dataset.field;
				let value = null;

				if(!field) break;
				if(key === ref.dataset.field) {
					value = transformation[key] ? transformation[key](ref.textContent) : ref.textContent;
					values.push(value);

					break;
				}
			}
		}

		return values.join("-");
	}

	/**
	 * 주문정보 그룹 별 데이터 키 조회
	 *
	 * @param {array} order 주문정보
	 * @param {array} keys 그룹화 할 키
	 * @param {object} transformations 그룹화 데이터 변환 필요한 함수
	 * @returns {string}
	 */
	function getOrderChainValue(order, keys, transformations) {
		const values = [];

		for(const key of keys) {
			const value = transformations[key] ? transformations[key](order[key]) : order[key];

			values.push(value);
		}

		return values.join("-");
	}

	/**
	 * 주문정보 그룹화
	 *
	 * @param {array} orders 주문정보
	 * @param {array} chainKeys 그룹화 할 키 리스트
	 * @param {object} valueTransformations 그룹화 데이터 변환 필요한 함수
	 * @param {{
	 * 	deep: boolean
	 * }} options - deep: 모든 데이터 일치
	 * @returns {object}
	 */
	function makeOrdersGrouping(orders, chainKeys, valueTransformations, options) {
		if(options && options.deep) {
			const a = orders.reduce(function(acc, value, index, array) {
				const groupingKeyValues = [];
				let groupingKey = null;

				for(const key of chainKeys) {
					groupingKeyValues.push(value[key]);
				}

				groupingKey = groupingKeyValues.join('-');

				if(acc[groupingKey]) acc[groupingKey].push(value);
				else acc[groupingKey] = [value];

				return acc;
			}, {});

			return a;
		}
		else {
			return chainKeys.reduce(function(acc, value, index, array) {
				const compareGroupKeys = chainKeys.slice(0, index+1);

				for(const order of orders) {
					const compareValues = getOrderChainValue(order, compareGroupKeys, valueTransformations);
					const sameValues = orders.filter(v => getOrderChainValue(v, compareGroupKeys, valueTransformations) === compareValues);

					acc[compareValues] = sameValues;
				}

				return acc;
			}, {});
		}
	}

	/**
	 *
	 * @param {array} orders
	 * @param {array} groupingKeys
	 * @param {object} valueTransformations
	 * @param {{
	 * 	deep: boolean
	 * }} options - deep: 모든 데이터 일치
	 */
	function mergeOrderGrid(orders, groupingKeys, valueTransformations, options) {
		if(!options) options = {};

		const ordersGrouping = makeOrdersGrouping(JSON.parse(JSON.stringify(orders)), groupingKeys, valueTransformations, {deep: options.deep});
		const tableTR = document.querySelectorAll('#' + 'orderGrid' + '>.k-grid-content>table tr');

		for(let i = 0, maxCnt = tableTR.length; i < maxCnt; i++) {
			const tr = tableTR[i];
			const tdRefs = tr.querySelectorAll("td");
			const prevTR = tableTR[i-1];
			const prevTDRefs = prevTR ? prevTR.querySelectorAll("td") : null;

			if(options.deep) {
				for(let j = 0, maxCntJ = tdRefs.length; j < maxCntJ; j++) {
					// current
					const td = tdRefs[j];
					const value = td ? td.textContent : null;
					const field = td.dataset ? td.dataset.field : null;

					// prev
					const prevTD = prevTR ? prevTR.querySelectorAll("td")[j] : null;
					const prevValue = prevTD ? prevTD.textContent : null;

					// compare
					const compareKeyIndex = groupingKeys.findIndex(v => v === field);
					const compareGroupingKeys = groupingKeys;

					// current prev grouping data
					const groupingValue = getRefChainValue(tdRefs, compareGroupingKeys, valueTransformations);
					const prevGroupingValue = prevTDRefs ? getRefChainValue(prevTDRefs, compareGroupingKeys, valueTransformations) : null;

					if(-1 < compareKeyIndex) {

						// 이전 데이터랑 같은 그룹이랑 비교 필요
						if(prevGroupingValue === groupingValue && prevValue === value) {
							td.style.display = "none";
						}
						else {
							td.style.display = "table-cell";
							td.setAttribute("rowspan", (ordersGrouping[groupingValue] || []).length);
						}
					}
				}
			}
			else {
				for(let j = 0, maxCntJ = tdRefs.length; j < maxCntJ; j++) {
					// current
					const td = tdRefs[j];
					const value = td ? td.textContent : null;
					const field = td.dataset ? td.dataset.field : null;

					// prev
					const prevTD = prevTR ? prevTR.querySelectorAll("td")[j] : null;
					const prevValue = prevTD ? prevTD.textContent : null;

					// compare
					const compareKeyIndex = groupingKeys.findIndex(v => v === field);
					const compareGroupingKeys = groupingKeys.slice(0, compareKeyIndex+1);

					// current prev grouping data
					const groupingValue = getRefChainValue(tdRefs, compareGroupingKeys, valueTransformations);
					const prevGroupingValue = prevTDRefs ? getRefChainValue(prevTDRefs, compareGroupingKeys, valueTransformations) : null;

					if(-1 < compareKeyIndex) {

						// 이전 데이터랑 같은 그룹이랑 비교 필요
						if(prevGroupingValue === groupingValue && prevValue === value) td.style.display = "none";
						else td.setAttribute("rowspan", (ordersGrouping[groupingValue] || []).length);
					}
				}
			}
		}
	}

	function fnOrderMergeGridRows(gridId, deepGroupingKeys, groupingKeys, valueTransformations) {
		// 머지할 목록 없는 경우 체크
		if(fnIsEmpty(deepGroupingKeys) || fnIsEmpty(groupingKeys)) return false;

		const orders = $('#' + gridId).data("kendoGrid").dataSource.data();

		// 데이터 1건 이상인 경우만 처리
		if(orders && orders.length) {
			mergeOrderGrid(orders, groupingKeys, valueTransformations);
			mergeOrderGrid(orders, deepGroupingKeys, valueTransformations, {deep: true});
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
	//-------------------------------  Grid End  ----------------------------------------------


	//---------------Initialize Option Box Start ----------------------------------------------
	function fnInitOptionBox() {
		// 주문생성 구분
		//if(isProdServerYn == "N") {
			fnTagMkRadio(orderOptionUtil.orderCreateTp);
		// }
		// else {
		// 	fnTagMkRadio(orderOptionUtil.prodOrderCreateTp);
		// }

		// 회원검색 버튼
		let targetObj = $('[name="orderCreateTp"][value="MEMBER"]').closest("label");
		$('<button type="button" id="fnSearchBuyer" name="fnSearchBuyer" class="btn-point btn-s marginL5" onclick="$scope.fnSearchBuyer();">회원 검색</button>').appendTo(targetObj);

		// 휴대폰 번호 앞자리
		orderPopupListUtil.phonePrefix("buyerHp1", '');
		orderPopupListUtil.phonePrefix("recvHp1", '');

		// 주소입력 구분
		fnTagMkRadio(orderOptionUtil.addressCreateTp);

		// 이메일 도메인
		searchCommonUtil.getDropDownCommCd("buyerEmailDomain", "NAME", "CODE", "선택", "EMAIL_DOMAIN", "Y");

		// 배송 요청사항
		orderSearchUtil.getDropDownComm(deliveryMsgArr, "recvDeliveryMsgCd");

		// 배송 출입정보
		searchCommonUtil.getDropDownCommCd("recvDoorMsgCd", "NAME", "CODE", "선택", "ACCESS_INFORMATION", "Y");

		// 결제방식(회원/비회원)
		fnTagMkRadio(orderOptionUtil.orderPayTp1);

		// 입력제한
		fnInputValidationForAlphabetHangul('buyerName');	// 주문자명
		fnInputValidationForNumber('buyerHp2'); 			// 주문자 휴대폰번호2
		fnInputValidationForNumber('buyerHp3'); 			// 주문자 휴대폰번호3

		fnInputValidationForAlphabetNumberSpecialCharacters('buyerEmail1');	// 주문자 이메일1
		fnInputValidationForAlphabetNumberSpecialCharacters('buyerEmail2');	// 주문자 이메일2

		fnInputValidationForHangulAlphabetNumberSpace('recvName');		// 배송지 수령인명
		fnInputValidationForNumber('recvHp2'); 							// 배송지 휴대폰번호2
		fnInputValidationForNumber('recvHp3'); 							// 배송지 휴대폰번호3

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
	function fnOrderCreateBindEvents() {
		// 주문생성 구분 Change
		$('[name="orderCreateTp"]').on('change', function(e) {

			buyerBaseInfo = new Object();			// 주문자 정보 초기화
			$('#orderGoodsGrid').gridClear(true);	// 상품정보 그리드 초기화
			$('#orderGrid').gridClear(true);		// 주문정보 그리드 초기화
			fnClearPayData();						// 결제정보 초기화
			$('#fnExcelUploadDelete').click();		// 첨부파일 삭제

			switch ($(this).filter(':checked').val()) {
				case 'MEMBER':			// 회원
					$('#fnSearchBuyer').prop('disabled', false);		// 회원 검색 활성화
					$('#fnExcelUpload').prop('disabled', true);			// 엑셀 업로드 파일선택 비활성화
					fnClearBuyerData('buyerInfo', false);				// 주문자 정보 초기화, 비활성화
					$('#excelUploadBtnDiv').hide();						// 엑셀 업로드 버튼 그룹 Hide
					$('#recvInfoDiv').show();							// 수취 정보 영역 Show
					$('#goodsInfoDiv').show();							// 상품 정보 영역 Show
					$('#orderInfoDiv').hide();							// 주문 정보 영역 Hide
					$('[name="recvAddressCreateTp"]:eq(0)').prop('checked', true).change();		// 회원 기본 주소
					$('#recvAddressCreateTpDiv').show();				// 주소입력 구분 Show
					fnTagMkRadio(orderOptionUtil.orderPayTp1);			// 결제방식(회원/비회원) 라디오 셋팅
					fnShowPayInfo(true);								// 결제정보(쿠폰할인/적립금 사용) Show
					$('#fnExcelUploadDelete').click();					// 첨부파일 삭제 클릭
					break;

				case 'NON_MEMBER':		// 비회원
					$('#fnSearchBuyer').prop('disabled', true);			// 회원 검색 비활성화
					$('#fnExcelUpload').prop('disabled', true);			// 엑셀 업로드 파일선택 비활성화
					fnClearBuyerData('buyerInfo', true);				// 주문자 정보 초기화, 활성화
					$("#buyerEmailDomain").data("kendoDropDownList").trigger("change");	// 이메일 주소 도메인
					$('#excelUploadBtnDiv').hide();						// 엑셀 업로드 버튼 그룹 Hide
					$('#recvInfoDiv').show();							// 수취 정보 영역 Show
					$('#goodsInfoDiv').show();							// 상품 정보 영역 Show
					$('#orderInfoDiv').hide();							// 주문 정보 영역 Hide
					$('[name="recvAddressCreateTp"]:eq(1)').prop('checked', true).change();		// 직접 입력
					$('#recvAddressCreateTpDiv').hide();				// 주소입력 구분 hide
					fnTagMkRadio(orderOptionUtil.orderPayTp1);			// 결제방식(회원/비회원) 라디오 셋팅
					fnShowPayInfo(false);								// 결제정보(쿠폰할인/적립금 사용) Hide
					$('#fnExcelUploadDelete').click();					// 첨부파일 삭제 클릭
					break;

				case 'EXCEL_UPLOAD':	// 엑셀 업로드
					$('#fnSearchBuyer').prop('disabled', true);			// 회원 검색 비활성화
					$('#fnExcelUpload').prop('disabled', false);		// 엑셀 업로드 파일선택 활성화
					fnClearBuyerData('buyerInfo', true);				// 주문자 정보 초기화, 활성화
					$("#buyerEmailDomain").data("kendoDropDownList").trigger("change");	// 이메일 주소 도메인
					$('#excelUploadBtnDiv').show();						// 엑셀 업로드 버튼 그룹 Show
					$('#recvInfoDiv').hide();							// 수취 정보 영역 Hide
					$('#goodsInfoDiv').hide();							// 상품 정보 영역 Hide
					$('#orderInfoDiv').show();							// 주문 정보 영역 Show
					$('[name="recvAddressCreateTp"]:eq(1)').prop('checked', true).change();		// 직접 입력
					$('#recvAddressCreateTpDiv').hide();				// 주소입력 구분 hide
					fnTagMkRadio(orderOptionUtil.orderPayTp2);			// 결제방식(엑셀 업로드) 라디오 셋팅
					fnShowPayInfo(false);								// 결제정보(쿠폰할인/적립금 사용) Hide
					break;
			}
			// 결제방식 change Event (payTp 라디오박스가 동적으로 생성되므로 생성된 이후에 change 이벤트 재생성)
			$('input[name="payTp"]').on('change', function(e) {
				//console.log(">>> payTp : " + $('input[name="orderCreateTp"]:checked').val() + " / "+ $(this).filter(':checked').val());
				if($(this).filter(':checked').val() == 'PAY_TP.VIRTUAL_BANK') {
					$('#virtualBankInfoDiv').show();
				} else {
					$('#virtualBankInfoDiv').hide();
				}
			});
			//주문생성 구분 change 에 따라 결제방식 change 이벤트 발생
			$('[name="payTp"]:eq(0)').change();
		});

		$('input[name="freeShippingPriceYn"]').on('change', function(e) {
			//배송비 제외 체크시 적립금 사용 금액을 0 으로 초기화
			var totalPrice = fnIsEmpty($('#usePointPrice').val()) ? 0 : Number(fnUncomma($('#usePointPrice').val()));
			if(totalPrice > 0) {
				$('#usePointPrice').val('0');
			}
		});

		// 이메일 Change
		$('#buyerEmailDomain').data('kendoDropDownList').bind('change', function(e) {
			fnIsEmpty(this.value()) || this.value() == 'EMAIL_DOMAIN.DIRECT' ? $("#buyerEmail2").val('') : $("#buyerEmail2").val(this.text());
			this.value() == 'EMAIL_DOMAIN.DIRECT' ? $('#buyerEmail2').prop('disabled', false) : $('#buyerEmail2').prop('disabled', true);
		});

		// 주소입력 구분 Change
		$('[name="recvAddressCreateTp"]').on('change', function(e) {
			switch ($(this).filter(':checked').val()) {
				case 'DEFAULT_ADDRESS':			// 회원 기본 주소
					$('#fnSearchShippingList').prop('disabled', true);	// 배송지 목록 버튼 비활성화
					fnClearBuyerData('addressInfoDiv', false);			// 주문자 주소 정보 초기화, 비활성화
					fnSetBuyerAddressInfo(buyerBaseInfo);				// 주문자 주소 정보 셋팅
					break;

				case 'DIRECT':					// 직접입력
					$('#fnSearchShippingList').prop('disabled', false);	// 배송지 목록 버튼 활성화
					fnClearBuyerData('addressInfoDiv', true);			// 주문자 주소 정보 초기화, 비활성화
					$("#recvDeliveryMsgCd").data("kendoDropDownList").trigger("change");
					$("#recvDoorMsgCd").data("kendoDropDownList").trigger("change");
					break;
			}
		});

		// 배송 요청사항
		$("#recvDeliveryMsgCd").data("kendoDropDownList").bind("change", function(e) {
			// 직접입력
			if (this.text() == '직접입력') {
				$('#recvDeliveryMsgEtc').prop('disabled', false);
			} else {
				$('#recvDeliveryMsgEtc').prop('disabled', true);
			}
			$('#recvDeliveryMsgEtc').val('');
		});

		// 배송 출입정보
		$("#recvDoorMsgCd").data("kendoDropDownList").bind("change", function(e) {
			// 공동현관 비밀번호 입력 or 기타
			if (this.value() == 'ACCESS_INFORMATION.FRONT_DOOR_PASSWORD' || this.value() == 'ACCESS_INFORMATION.ETC') {
				$('#recvDoorMsgCdNm').prop('disabled', false);
			} else {
				$('#recvDoorMsgCdNm').prop('disabled', true);
			}
			$('#recvDoorMsgCdNm').val('');
		});

		// 쿠폰금액, 적립금 숫자입력
		fnValidateNum('discountPrice');
		fnValidateNum('usePointPrice');

		// 주소(우편번호, 주소1) 변경시
		$('#recvZipCode, #recvAddr1').on('change', function() {
			fnSetBuyerPayInfo(buyerBaseInfo);		// 주문자 결제 정보 초기화
			fnGetCouponInfo();						// 쿠폰정보 조회
		});

		// 쿠폰할인 Keyup
		$('#discountPrice').on('keyup', function(e) {
			$(this).val(fnNumberWithCommas(fnUncomma($(this).val())));
		});

		// 적립금 사용 Keyup
		$('#usePointPrice').on('keyup', function(e) {
			$(this).val(fnNumberWithCommas(fnUncomma($(this).val())));
			if (Number(fnUncomma($(this).val())) > Number(fnUncomma($('#pointPrice').text()))) $(this).val($('#pointPrice').text());
		});


		// 상품정보 > 주문I/F변경 팝업
		$('#ng-view').on("click","div[name=ifDayChange]",function(){
			let gridSelect = $(this).closest("div[data-role=grid]").attr("id");
			var selectedRowData = "";
			selectedRowData = orderGoodsGrid.dataItem($(this).closest('tr'));
			selectedRowData.orderCreateYn = 'Y';
			selectedRowData.recvBldNo = fnNvl($('#buildingCode').val());
			selectedRowData.zipCode = fnNvl($('#recvZipCode').val());
			selectedRowData.goodsDeliveryType = selectedRowData.dawnDeliveryYn == "Y" ? "GOODS_DELIVERY_TYPE.DAWN": "GOODS_DELIVERY_TYPE.NORMAL";
			selectedRowData.isMember = $('[name="orderCreateTp"]').filter(':checked').val() == 'MEMBER' ? true : false; //회원/비회원 여부

			// 상품정보중에서 해당 출고처인 상품 List 세팅
			let goodsList = new Array();
			let orderGoodsGridData = orderGoodsGrid.dataSource._data;
			let urWarehouseId = selectedRowData.urWarehouseId;

			// 일일상품은 동일출고처가 있어도 같이 주문I/F 변경 안함
			if(selectedRowData.deliveryType == "DELIVERY_TYPE.DAILY"){
				let goodsObj = new Object();
				goodsObj.urWarehouseId 	= selectedRowData.urWarehouseId;
				goodsObj.ilGoodsId 		= selectedRowData.ilGoodsId;
				goodsObj.orderCnt 		= selectedRowData.orderCnt;
				goodsObj.goodsDailyCycleType = selectedRowData.goodsDailyCycleType;
				if(selectedRowData.goodsDailyCycleType == "GOODS_CYCLE_TP.1DAY_PER_WEEK"){ // 주1일만 weekCode 전달
					goodsObj.weekCode 	= selectedRowData.goodsDailyCycleGreenJuiceWeekType[0];
				}
				goodsList.push(goodsObj);
			}else{
				for(let i = 0 ; i < orderGoodsGridData.length ; i++){
					if (urWarehouseId == orderGoodsGridData[i].urWarehouseId){
						let goodsObj = new Object();
						goodsObj.urWarehouseId 	= orderGoodsGridData[i].urWarehouseId;
						goodsObj.ilGoodsId 		= orderGoodsGridData[i].ilGoodsId;
						goodsObj.orderCnt 		= orderGoodsGridData[i].orderCnt;
						goodsList.push(goodsObj);
					}
				}
			}
			selectedRowData.goodsList = goodsList;

			orderPopupUtil.open("interfaceDayChangePopup", selectedRowData, function(){

				var shippingDt					= parent.POP_PARAM["parameter"].shippingDt;
				var deliveryDt					= parent.POP_PARAM["parameter"].deliveryDt;
				var orderIfDt					= parent.POP_PARAM["parameter"].orderIfDt;
				var orderIfDawnYn 				= parent.POP_PARAM["parameter"].orderIfDawnYn == "Y" ? "Y" : "N";
				var goodsDeliveryDateMap 		= parent.POP_PARAM["parameter"].goodsDeliveryDateMap;
				var goodsDawnDeliveryDateMap 	= parent.POP_PARAM["parameter"].goodsDawnDeliveryDateMap;

				var arrCheced = [];
				var checkedDataList = orderGoodsGrid.dataSource._data;
				for(var i = 0 ; i < checkedDataList.length ; i++){
					var rowChecked = $("#orderGoodsGrid").find("input[name=rowCheckbox]:eq("+i+")").is(":checked");
					arrCheced[i] = rowChecked;
				}


				if (stringUtil.getString(parent.POP_PARAM["parameter"].allChangeYn, "N") == "Y"){
					var urWarehouseId = selectedRowData.urWarehouseId;
					var orderGoodsDataList = orderGoodsGrid.dataSource._data;

					if(selectedRowData.deliveryType == "DELIVERY_TYPE.DAILY"){
						selectedRowData.shippingDt		= shippingDt;
						selectedRowData.deliveryDt		= deliveryDt;
						selectedRowData.orderIfDt		= orderIfDt;
						selectedRowData.orderIfDawnYn	= orderIfDawnYn;
						//일반
						if(orderIfDawnYn == 'N'){
							selectedRowData.stockQty = goodsDeliveryDateMap[selectedRowData.ilGoodsId].find(x => x.orderDate === orderIfDt).stock;
							//새벽
						}else{
							selectedRowData.stockQty = goodsDawnDeliveryDateMap[selectedRowData.ilGoodsId].find(x => x.orderDate === orderIfDt).stock;
						}
					}else{
						for(var i = 0 ; i < orderGoodsDataList.length ; i++){
							if (urWarehouseId == orderGoodsDataList[i].urWarehouseId){
								orderGoodsDataList[i].shippingDt	= shippingDt;
								orderGoodsDataList[i].deliveryDt	= deliveryDt;
								orderGoodsDataList[i].orderIfDt		= orderIfDt;
								orderGoodsDataList[i].orderIfDawnYn	= orderIfDawnYn;

								//일반
								if(orderIfDawnYn == 'N'){
									orderGoodsDataList[i].stockQty = goodsDeliveryDateMap[orderGoodsDataList[i].ilGoodsId].find(x => x.orderDate === orderIfDt).stock;
									//새벽
								}else{
									orderGoodsDataList[i].stockQty = goodsDawnDeliveryDateMap[orderGoodsDataList[i].ilGoodsId].find(x => x.orderDate === orderIfDt).stock;
								}
							}
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
			});
		});


		// 상품 정보 그리드 내 상품선택 버튼 활성화(HGRM-8980)
		var observer = new MutationObserver(function(mutations) {
			mutations.forEach(function(mutation) {
				$('#orderGoodsGrid').gridClear(true);	// 상품정보 그리드 초기화
				if($('#buildingCode').val()){
					$("#fnSearchGoods").attr('disabled',false);
					$("#fnDeleteGoods").attr('disabled',false);
				}else{
					$("#fnSearchGoods").attr('disabled',true);
					$("#fnDeleteGoods").attr('disabled',true);
				}
			});
		});
		var config = { attributes: true};
		observer.observe(document.getElementById('buildingCode'), config);


		//품목 재고 상세보기 팝업
		$('#ng-view').on("click", "a[kind=detailInfoBtn]", function(e) {
			e.preventDefault();
			var dataItem = orderGoodsGrid.dataItem($(this).closest('tr'));

			var nowDate = fnGetToday();
			var popupOption;
			if('GOODS_TYPE.PACKAGE' == dataItem.goodsType) {
				popupOption = {
					id: 'packageGoodsStockMgm',
					title: '묶음상품 재고 상세 정보',
					param: { "goodsId": dataItem.ilGoodsId },
					src: '#/packageGoodsStockMgm',
					width: '1100px',
					height: '800px',
					success: function(id, data) {
					}
				}
			} else {
				popupOption = {
					id: 'goodsStockMgm',
					title: '재고 상세 정보',
					// param: { "ilItemCd":dataItem.itemCd, "itemBarcode":dataItem.itemBarcode, "itemNm":dataItem.itemNm,
					// 	"preOrderTpNm":dataItem.preOrderTpNm, "ilItemStockId":dataItem.ilItemStockId,
					// 	"stockScheduledCount":dataItem.stockScheduledCount,"ilItemWarehouseId":dataItem.ilItemWarehouseId,"baseDt":nowDate
					// },
					param: {"ilItemWarehouseId":dataItem.ilItemWarehouseId,"baseDt":nowDate
					},
					src: '#/goodsStockMgm',
					width: '1100px',
					height: '800px',
					success: function(id, data) {
						//fnSearch();
					}
				}
			}
			fnKendoPopup(popupOption)

		});

		// 일일상품 옵션변경 팝업
		$('#ng-view').on("click", "button[id=dailyGoodsOptionChangeBtn]", function(e) {
			e.preventDefault();
			let selectedRowData = "";
			selectedRowData = orderGoodsGrid.dataItem($(this).closest('tr'));
			selectedRowData.recvBldNo = fnNvl($('#buildingCode').val());
			selectedRowData.zipCode = fnNvl($('#recvZipCode').val());
			selectedRowData.goodsDeliveryType = selectedRowData.dawnDeliveryYn == "Y" ? "GOODS_DELIVERY_TYPE.DAWN": "GOODS_DELIVERY_TYPE.NORMAL";
			//selectedRowData.isMember = $('[name="orderCreateTp"]').filter(':checked').val() == 'MEMBER' ? true : false; //회원/비회원 여부
			selectedRowData.goodsName = selectedRowData.goodsNm.replace(/\$\{(.*.)\}/g, "$1").split('\nㄴ')[0];

			orderPopupUtil.open("dailyGoodsOptionChangePopup", selectedRowData, function(){

				var dailyGoodsOptionChangeData	= parent.POP_PARAM["parameter"].dailyGoodsOptionChangeData;

				if(dailyGoodsOptionChangeData != undefined){
					fnPutCartInfo(dailyGoodsOptionChangeData,selectedRowData);
				}

			});
		});


		// 추가상품 변경 팝업
		$('#ng-view').on("click", "button[id=additionalGoodsChangeBtn]", function(e) {
			e.preventDefault();
			let selectedRowData = "";
			selectedRowData = orderGoodsGrid.dataItem($(this).closest('tr'));
			selectedRowData.goodsName = selectedRowData.goodsNm.replace(/\$\{(.*.)\}/g, "$1").split('\nㄴ')[0];

			orderPopupUtil.open("additionalGoodsChangePopup", selectedRowData, function(){

				var additionalGoodsOptionChangeData	= parent.POP_PARAM["parameter"].additionalGoodsOptionChangeData;

				if(additionalGoodsOptionChangeData != undefined){
					fnPutCartInfo(additionalGoodsOptionChangeData,selectedRowData);
				}

			});
		});


		// 상품전체 체크박스 클릭
		$('#ng-view').on("click","input[name=checkBoxAll]",function(e){
			if ($('input:checkbox[name="checkBoxAll"]').is(":checked")) {
				$('input:checkbox[name="rowCheckbox"]').each(function () {
					this.checked = true; //checked 처리
				});
			} else {
				$('input:checkbox[name="rowCheckbox"]').each(function () {
					this.checked = false; //checked 처리
				});
			}
		});

		// 상품 체크박스 클릭
		$('#ng-view').on("click","input[name=rowCheckbox]",function(e){
			var selectRowsOrder = $("#orderGoodsGrid").find("input[name=rowCheckbox]:checked").closest("tr");

			if ($('input:checkbox[name="rowCheckbox"]').length == selectRowsOrder.length) {
				$("#checkBoxAll").prop('checked', true);
			} else {
				$("#checkBoxAll").prop('checked', false);
			}
		});

		$('#ng-view').on("click","input[name=copyOrdererNameHp]",function(e){
			if ($('input:checkbox[name="copyOrdererNameHp"]').is(":checked")) {
				$('#recvName').val($('#buyerName').val());
				if(!fnIsEmpty($('#buyerHp1').data('kendoDropDownList').value())){
                    $('#recvHp1').data('kendoDropDownList').value($('#buyerHp1').val());
                }

				$('#recvHp2').val($('#buyerHp2').val());
				$('#recvHp3').val($('#buyerHp3').val());
			} else {
				$('#recvName').val("");
                $('#recvHp1').data("kendoDropDownList").select(0);
				$('#recvHp2').val("");
				$('#recvHp3').val("");
			}
		});

	}
	//---------------Initialize Bind Events End -----------------------------------------------


	//-------------------------------  Common Service start -----------------------------------
	// 엑셀 업로드 submit
	function fnExcelUploadSubmit(url, cbId) {
		let form = $('#excelForm')[0];
		let paramData = new FormData(form);
		/*
                $.ajax({
                    url     	: url,
                    data  		: paramData,
                    type        : 'POST',
                    processData	: false,
                    contentType	: false,
                    async       : false,
                    success 	: function(successData) {
                        fnBizCallback(cbId, successData);
                    }
                });
        */
		$.ajax({
			url     	: url,
			data  		: paramData,
			type        : 'POST',
			processData	: false,
			contentType	: false,
			async       : false,
			beforeSend : function(xhr, settings) {
				xhr.setRequestHeader('authMenuID', CLICKED_MENU_ID);
			},
			success 	: function(resultData) {

				if(resultData.data.failCount > 0){
					fnBizCallback('excelUploadFail', resultData);
				}else{
					fnBizCallback(cbId, resultData);
				}
			}
		});


	}

	// 주문생성 주문자 정보(배송지, 적립금) 조회
	function fnSearchBuyerBaseInfo(paramUrUserId) {
		// 주문자 정보(배송지, 적립금) 전역변수 초기화
		buyerBaseInfo = new Object();

		let url		= "/admin/order/create/getBuyerBaseInfo";
		let param 	=  new Object();
		param.urUserId = paramUrUserId;

		fnAjax({
			url     : url,
			params  : param,
			async   : true,
			method	: 'GET',
			success : function(resultData) {
				fnBizCallback('buyerBaseInfo', resultData);
			},
			isAction : 'select'
		});
	}

	// 쿠폰정보 조회
	function fnGetCouponInfo() {

		let gridArr 	= orderGoodsGrid.dataSource.data();
		if (fnIsEmpty($("#urUserId").val()) || fnIsEmpty($("#recvZipCode").val()) || fnIsEmpty(gridArr) || gridArr.length < 1) return false;

		let url		= "/admin/order/create/getCouponPageInfo";
		let param 	=  new Object();

		let cartArr 	= new Array();
		let deliveryArr = new Array();
		for (let i=0; i<gridArr.length; i++) {
			cartArr.push(gridArr[i].spCartId);
			//let scheduledItem = new Object();


			// scheduledItem.arrivalScheduledDate 	= gridArr[i].deliveryDt;
			// scheduledItem.dawnDeliveryYn 		= gridArr[i].orderIfDawnYn == undefined ? gridArr[i].dawnDeliveryYn : gridArr[i].orderIfDawnYn;
			// deliveryArr.push(scheduledItem);


			if (i==0 || (gridArr[i-1].deliveryType != gridArr[i].deliveryType || gridArr[i-1].shippingIndex != gridArr[i].shippingIndex)) {
				let scheduledItem = new Object();
				scheduledItem.arrivalScheduledDate 	= gridArr[i].deliveryDt;
				scheduledItem.dawnDeliveryYn 		= gridArr[i].orderIfDawnYn == undefined ? gridArr[i].dawnDeliveryYn : gridArr[i].orderIfDawnYn;
				deliveryArr.push(scheduledItem);
			}
		}


		param.urUserId 					= fnNvl($("#urUserId").val());		// 회원PK
		param.receiverZipCode 			= fnNvl($('#recvZipCode').val());	// 우편번호
		param.buildingCode 				= fnNvl($('#buildingCode').val());	// 건물관리 번호
		param.spCartIdListData 			= kendo.stringify(cartArr);			// 그리드 장바구니PK 리스트
		param.arrivalScheduledListData 	= kendo.stringify(deliveryArr);		// 배송 스케줄 변경 정보 리스트

		fnAjax({
			url     : url,
			params  : param,
			async   : false,
			method	: 'POST',
			success : function(resultData) {
				fnBizCallback('couponInfo', resultData);
			},
			isAction : 'select'
		});

	}

	// 주문생성  장바구니 정보 생성
	function fnAddCartInfo(paramArr) {

		if (!fnIsEmpty(paramArr) && paramArr.length > 0) {

			let url		= "/admin/order/create/addCartInfo";
			let param 	=  new Object();
			let gridArr = orderGoodsGrid.dataSource.data();
			let goodsArr = new Array();

			let alertFlag		= false;
			let goodsTypeFlag	= true;
			let saleTypeFlag	= true;
			let checkGoodsFlag	= true;
			let overLapGoodsNm	= "";
			let overLapCnt	 	= 0;

			if (!fnIsEmpty(gridArr) && gridArr.length > 0) {

				let gridIlGoodsIdList = new Array();
				for (let i=0; i<gridArr.length; i++) {
					gridIlGoodsIdList.push(gridArr[i].ilGoodsId);
				}

				for(let j=0; j<paramArr.length; j++) {
					if(gridIlGoodsIdList.includes(paramArr[j].goodsId)){
						alertFlag		= true;
						checkGoodsFlag	= false;
						if (overLapGoodsNm == ""){
							overLapGoodsNm = paramArr[j].goodsName;
						}
						overLapCnt++;
					}else if(paramArr[j].goodsTypeCode != "GOODS_TYPE.NORMAL" && paramArr[j].goodsTypeCode != "GOODS_TYPE.DISPOSAL" && paramArr[j].goodsTypeCode !=  "GOODS_TYPE.PACKAGE"
						&& paramArr[j].goodsTypeCode !=  "GOODS_TYPE.GIFT"&& paramArr[j].goodsTypeCode != "GOODS_TYPE.GIFT_FOOD_MARKETING" && paramArr[j].goodsTypeCode !=  "GOODS_TYPE.DAILY"){
						alertFlag		= true;
						checkGoodsFlag	= false;
						goodsTypeFlag	= false;
					}else if(paramArr[j].saleTypeCode == "SALE_TYPE.RESERVATION" && paramArr[j].saleTypeCode == "SALE_TYPE.SHOP"){
						alertFlag		= true;
						checkGoodsFlag	= false;
						saleTypeFlag	= false;
					}else{
						paramArr[j].ilGoodsId 	= paramArr[j].goodsId		// 상품ID
						paramArr[j].goodsNm		= paramArr[j].goodsName		// 상품명

						let item = new Object();
						item.ilGoodsId 	= paramArr[j].goodsId;	// 상품PK
						item.qty		= 1;					// 수량
						goodsArr.push(item);
					}
				}

			}else{
				for(let i=0; i<paramArr.length; i++) {
					paramArr[i].ilGoodsId 	= paramArr[i].goodsId		// 상품ID
					paramArr[i].goodsNm		= paramArr[i].goodsName		// 상품명

					let item = new Object();
					item.ilGoodsId 	= paramArr[i].goodsId;	// 상품PK
					item.qty		= 1;					// 수량
					goodsArr.push(item);
				}
			}

			param.urUserId			= fnNvl($("#urUserId").val());		// 회원PK
			param.deliveryType		= deliveryType;						// 배송타입 : 일일배송 고정
			param.addGoodsListData 	= kendo.stringify(goodsArr);		// 상품정보List
			param.receiverZipCode	= fnNvl($('#recvZipCode').val());	// 우편번호
			param.buildingCode		= fnNvl($('#buildingCode').val());	// 건물관리 번호
			param.cartType			= cartType;							// 카트타입 : 일반 고정


			if (!fnIsEmpty(gridArr) && gridArr.length > 0) {
				let cartArr = new Array();
				for (let i=0; i<gridArr.length; i++) {
					cartArr.push(gridArr[i].spCartId);
					paramArr.push(gridArr[i]);
				}
				param.spCartIdListData 	= kendo.stringify(cartArr);		// 그리드 장바구니PKList
			}

			kendo.ui.progress($('#orderGoodsGrid'), true);

			if (alertFlag == true){
				if (overLapGoodsNm != ""){
					let msg = "동일한 상품이 등록 되어있습니다.<br/>" + "상품명 : " + overLapGoodsNm;
					if (overLapCnt > 1){
						msg += " 외 "+ overLapCnt + "건";
					}
					fnKendoMessage({message : msg});
				}
				if (goodsTypeFlag == false){
					fnKendoMessage({message : "일반, 폐기임박, 묶음, 일일 상품유형만 추가 가능합니다."});
				}
				if (saleTypeFlag == false){
					fnKendoMessage({message : "일반, 일일, 비매품 판매유형만 추가 가능합니다."});
				}
			}

			fnAjax({
				url     : url,
				params  : param,
				async   : true,
				method	: 'POST',
				success : function(resultData) {
					let result = new Object();

					let failMessageList = resultData.failMessageList;
					let orderCreateCartList = resultData.orderCreateCartList.data;
					result.goodsArr			= paramArr;
					result.resultData 		= orderCreateCartList;
					result.failMessageList	= failMessageList;

					fnBizCallback('addCartInfo', result);
				},
				fail : function(resultData, resultCode) {
					kendo.ui.progress($('#orderGoodsGrid'), false);
					fnKendoMessage({
						message : resultCode.message,
						ok : function(e) {
							fnBizCallback("fail", resultCode);
						}
					});
				},
				isAction : 'select'
			});
		}
	}

	// 주문생성  장바구니 정보 변경
	function fnPutCartInfo(paramObj,selectedRowData) {

		let url		= "/admin/order/create/putCartInfo";
		let param 	=  new Object();

		let gridArr 	= orderGoodsGrid.dataSource.data();
		let cartArr 	= new Array();
		let paramArr	= new Array();
		for (let i=0; i<gridArr.length; i++) {
			cartArr.push(gridArr[i].spCartId);
			paramArr.push(gridArr[i]);
		}
		param.spCartIdListData 	= kendo.stringify(cartArr);		// 그리드 장바구니PKList

		// 선택데이터 정보
		//let dataItems 	= orderGoodsGrid.dataItem(orderGoodsGrid.select());
		let dataItems = selectedRowData == undefined ? orderGoodsGrid.dataItem(orderGoodsGrid.select()) : selectedRowData;

		param.spCartId 			= dataItems.spCartId;				// 장바구니PK
		param.qty 				= paramObj.orderCnt == undefined? dataItems.orderCnt :paramObj.orderCnt;    			// 주문수량
		param.spCartIdListData 	= kendo.stringify(cartArr);			// 그리드 장바구니PKList
		param.urUserId			= fnNvl($("#urUserId").val());		// 회원PK
		param.ilGoodsId 		= dataItems.ilGoodsId;				// 상품PK
		param.deliveryType		= dataItems.deliveryType;// deliveryType;						// 배송타입 : 일일배송 고정
		//param.saleType			= saleType;							// 판매유형 : 일반판매 고정
		param.receiverZipCode	= fnNvl($('#recvZipCode').val());	// 우편번호
		param.buildingCode		= fnNvl($('#buildingCode').val());	// 건물관리 번호
		param.cartType			= cartType;							// 카트타입 : 일반 고정
		param.deliveryDt		= dataItems.deliveryDt;				// 도착예정일

		// 일일상품
		if(dataItems.deliveryType == 'DELIVERY_TYPE.DAILY'){
			// 옵션변경 팝업에서 변경
			if(paramObj.goodsDailyCycleType != undefined){
				param.goodsDailyCycleType 					= paramObj.goodsDailyCycleType;
				param.goodsDailyCycleGreenJuiceWeekType 	= paramObj.goodsDailyCycleGreenJuiceWeekType.split("∀");
				param.goodsDailyCycleTermType 				= paramObj.goodsDailyCycleTermType;
				param.goodsBulkType 						= paramObj.goodsBulkType;
				param.goodsDailyBulkYn 						= paramObj.goodsDailyBulkYn;
				param.goodsDailyAllergyYn 					= paramObj.goodsDailyAllergyYn;

				// 본 화면에서 수량변경
			}else{
				param.goodsDailyCycleType 					= dataItems.goodsDailyCycleType;
				param.goodsDailyCycleGreenJuiceWeekType 	= dataItems.goodsDailyCycleGreenJuiceWeekType;
				param.goodsDailyCycleTermType 				= dataItems.goodsDailyCycleTermType;
				param.goodsBulkType 						= dataItems.goodsBulkType;
				param.goodsDailyBulkYn 						= dataItems.goodsDailyBulkYn;
				param.goodsDailyAllergyYn 					= dataItems.goodsDailyAllergyYn;
			}
		}

		// 옵션변경 팝업에서 추가상품 추가할 경우
		if(dataItems.additionalGoodsOptionChangeData != undefined){
			let additionalGoodsList = new Array();

			// 옵션변경 팝업에서 변경
			if(paramObj.goodsType == 'GOODS_TYPE.ADDITIONAL'){
				let additionalGoodsObj = new Object();
				additionalGoodsObj.ilGoodsId = paramObj.additionalGoods;
				additionalGoodsObj.qty = 1;	// 팝업에서 추가상품 추가시 수량 1로 고정
				additionalGoodsList.push(additionalGoodsObj);
			}
			param.addGoodsList = additionalGoodsList;
		}

		// 그리드에서 추가상품 수정할 경우
		if(dataItems.goodsType == 'GOODS_TYPE.ADDITIONAL'){
			let additionalGoodsList = new Array();

			let additionalGoodsObj = new Object();
			additionalGoodsObj.ilGoodsId = dataItems.ilGoodsId;
			additionalGoodsObj.qty = paramObj.orderCnt == undefined? dataItems.orderCnt :paramObj.orderCnt;    			// 주문수량
			additionalGoodsList.push(additionalGoodsObj);

			let trElements = $('#orderGoodsGrid tbody tr');

			// 본상품 수량은 원래 그대로 유지
			if(trElements && trElements.length) {
				for(let i = 0, maxCnt = trElements.length; i < maxCnt; i++) {
					const tdElements = $('#orderGoodsGrid tbody tr')[i];
					const spCartIdElement = $(tdElements).find("td[data-field='spCartId']");
					const goodsTypeElement = $(tdElements).find("td[data-field='goodsType']");
					const orderCntElement = $(tdElements).find("td[data-field='orderCnt']");

					if(spCartIdElement && goodsTypeElement && orderCntElement && dataItems.spCartId == spCartIdElement.text() && 'GOODS_TYPE.ADDITIONAL' != goodsTypeElement.text()) {
						param.qty = orderCntElement.text();
						break;
					}
				}
			}

			param.addGoodsList = additionalGoodsList;
		}

		kendo.ui.progress($('#orderGoodsGrid'), true);

		fnAjax({
			url     : url,
			params  : param,
			async   : true,
			contentType: "application/json",
			success : function(resultData) {

				let result = new Object();
				result.goodsArr		= paramArr;
				result.resultData 	= resultData;
				fnBizCallback('putCartInfo', result);
			},
			fail : function(resultData, resultCode) {
				kendo.ui.progress($('#orderGoodsGrid'), false);
				fnKendoMessage({
					message : resultCode.message,
					ok : function(e) {
						fnBizCallback("fail", resultCode);
					}
				});
			},
			isAction : 'select'
		});

	}

	function fnGetCartInfo(){
		let orderGoodsGridData = orderGoodsGrid.dataSource._data;
		if(orderGoodsGridData.length > 0){
			let params = new Object();
			let spCartIds = new Array();
			for(let i = 0 ; i < orderGoodsGridData.length ; i++){
				spCartIds.push(orderGoodsGridData[i].spCartId);
			}
			params.spCartId 		= spCartIds;
			params.urUserId 		= fnNvl($("#urUserId").val());
			params.deliveryType		= deliveryType;						// 배송타입 : 일반배송 고정
			params.receiverZipCode	= fnNvl($('#recvZipCode').val());	// 우편번호
			params.buildingCode		= fnNvl($('#buildingCode').val());	// 건물관리 번호
			params.cartType			= cartType;							// 카트타입 : 일반 고정

			fnAjax({
				url     : '/admin/order/create/getCartInfo',
				params  : params,
				async   : true,
				contentType: "application/json",
				success : function(resultData) {
					fnBizCallback('getCartInfo', resultData);
				},
				isAction : 'select'
			});

		}

	}

	// 엑셀 업로드 가상계좌 입금
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

		let createTp = $('[name="orderCreateTp"]').filter(':checked').val();
		param.orderCreateTp = createTp;

		let url		= "/admin/order/create/addBankBookOrderCreate";

		fnAjax({
            url     : url,
            params  : param,
            /*async   : true,
            method	: 'POST',*/
            success : function(resultData) {
				console.log("> fnVirtualBankPay : " + resultData);
				if(resultData.result == 'SUCCESS') {
					fnKendoMessage({
						//message : "엑셀업로드한 주문이 생성되었습니다.",
						message : "주문번호 : " + param.odid + " 주문이 생성되었습니다.",
						ok : function(e) {
							location.reload();
						}
					});
				} else {
					fnKendoMessage({
						message : "주문번호 : " + param.odid + " 주문, 가상계좌 실패 : " + resultData.message,
						ok : function(e) {
							$("#fnSaveBottom").attr('disabled',false);
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
	//-------------------------------  Common Service End -------------------------------------


	//-------------------------------  Common Function start ----------------------------------
	// 화면 기본 셋팅
	function fnSetDefault() {
		// 주문생성 구분 -> 회원
		$('[name="orderCreateTp"]:eq(0)').change();
		// 주소입력 구분 -> 회원 기본 주소
		$('[name="orderCreateTp"]:eq(0)').change();
		// 결제 방식 -> 직접 결제
		$('[name="orderCreateTp"]:eq(0)').change();

		$("input[name='freeShippingPriceYn']").prop("checked", false);
	}

	// 서비스 CallBack
	function fnBizCallback(id, result) {
		switch (id) {
			case 'buyerBaseInfo' :				// 주문자 기본정보(배송지, 적립금) 조회
				if (fnIsEmpty(result)) return false;
				buyerBaseInfo = result;					// 주문자 정보(배송지, 적립금) 전역변수 셋팅
				fnSetBuyerAddressInfo(buyerBaseInfo);	// 주문자 주소 정보 셋팅
				fnSetBuyerPayInfo(buyerBaseInfo);		// 주문자 결제 정보 셋팅
				break;

			case 'addCartInfo' :				// 주문생성  장바구니 정보 생성
				//if (fnIsEmpty(result.resultData)) return false;
				let params = new Object();



				params.goodsArr	= result.goodsArr;
				params.cartArr 	= result.resultData;

				if (params.cartArr.length > 0){
					fnSetOrderGoodsGrid(params);			// 상품정보 그리드 데이터 셋팅
					fnSetBuyerPayInfo(buyerBaseInfo);		// 주문자 결제 정보 초기화
					fnGetCouponInfo();						// 쿠폰정보 조회
				}

				let failMessage = "";
				for(var i=0;i<result.failMessageList.length;i++){
					if (stringUtil.getString(result.failMessageList[i], "") != "") {
						failMessage += result.failMessageList[i] + "<br/>";
					}
				}
				if ($.trim(failMessage) != ""){
					fnKendoMessage({message : failMessage});
				}/* else {
				fnSetOrderGoodsGrid(params);			// 상품정보 그리드 데이터 셋팅
				fnSetBuyerPayInfo(buyerBaseInfo);		// 주문자 결제 정보 초기화
				fnGetCouponInfo();						// 쿠폰정보 조회
			}*/
				kendo.ui.progress($('#orderGoodsGrid'), false);
				break;

			case 'putCartInfo' :				// 주문생성  장바구니 정보 변경
				if (fnIsEmpty(result.resultData) || fnIsEmpty(result.resultData.data) || result.resultData.data.length < 1) return false;
				let param = new Object();
				param.goodsArr	= result.goodsArr;
				param.cartArr 	= result.resultData.data;
				fnSetOrderGoodsGrid(param);				// 상품정보 그리드 데이터 셋팅
				fnSetBuyerPayInfo(buyerBaseInfo);		// 주문자 결제 정보 초기화
				fnGetCouponInfo();						// 쿠폰정보 조회
				break;

			case 'getCartInfo' :				// 주문생성  장바구니 정보 조회
				if (fnIsEmpty(result)) return false;
				$('#orderGoodsGrid').gridClear(true);	// 상품정보 그리드 초기화

				let paramGetCartInfo = new Object();
				paramGetCartInfo.goodsArr	= result;
				paramGetCartInfo.cartArr 	= result;

				fnSetOrderGoodsGrid(paramGetCartInfo);				// 상품정보 그리드 데이터 셋팅
				fnSetBuyerPayInfo(buyerBaseInfo);		// 주문자 결제 정보 초기화
				fnGetCouponInfo();						// 쿠폰정보 조회
				break;

			case 'couponInfo' :					// 쿠폰정보 조회
				let availableCouponCnt = fnIsEmpty(result.availableCouponCnt) ? 0 : result.availableCouponCnt;
				$('#couponCount').text(availableCouponCnt);	// 사용가능 쿠폰 개수 셋팅
				break;

			case 'excelUpload' :				// 엑셀 업로드
				if (fnIsEmpty(result) || fnIsEmpty(result.data) || fnIsEmpty(result.data.successList) || result.data.successList.length < 1) return false;
				let orderGridArr = new Array();
				orderGridArr = result.data.successList.concat(result.data.failList);

				// 그룹화에 따른 데이터 정렬
				orderGridArr.sort(function(a, b) {
					// 전체 그룹화 부터 우선순위로 정렬
					for(let key of orderDeepGroupingKeys) {
						let order = 0;

						if("string" === typeof a[key]) order = a[key].localeCompare(b[key]);
						else order = a[key] - b[key];

						if(0 !== order) return order;
					}

					// 전체 그룹화 데이터 정렬 변환 없을 시 일반 그룹화 기준으로 데이터 정렬
					for(let key of orderGroupingKeys) {
						let order = 0;

						if("string" === typeof a[key]) order = a[key].localeCompare(b[key]);
						else order = a[key] - b[key];

						if(0 !== order) return order;
					}

					return 0;
				}).map(function(v, i) {	// row number 배열 순서대로 변경
					v.rnum = i + 1;

					return v;
				});
				// orderDeepGroupingKeys

				fnSetOrderGrid(orderGridArr);	// 주문정보 그리드 데이터 셋팅
				break;

			case 'save' :	// 저장
				let createTp = $('[name="orderCreateTp"]').filter(':checked').val();

				var option = new Object();

				option.url = "#/orderCreate";
				option.menuId = 1290;
				//option.data = {};

				// 회원,비화원
				if (createTp == 'MEMBER' || createTp == 'NON_MEMBER') {
					let param = new Object();
					param.odid 				= result.odid;
					param.odPaymentMasterId = result.odPaymentMasterId;
					param.orderPrice 		= result.paymentPrice;
					if (result.psPayCd == 'PAY_TP.CARD') {
						param.type 				= 'CREATE';
						fnCardPay(param);
					} else if(result.psPayCd == 'PAY_TP.VIRTUAL_BANK') {
						param.odOrderId = result.odOrderId;
						param.pgBankCd = fnNvl($('#pgBankCd').data('kendoDropDownList').value());
						fnVirtualBankPay(param);
					} else {
						//fnKendoMessage({message : "주문번호 : " + result.odid + " 주문이 생성되었습니다."});
						fnKendoMessage({message : "주문번호 : " + result.odid + " 주문이 생성되었습니다.", ok :function(){
								location.reload();
								//fnGoPage(option);
							}});
						//$('[name="orderCreateTp"]').change();
					}
					// 엑셀 업로드
				} else {

					if (result.result == "SUCCESS") {

						let param = new Object();
						param.odPaymentMasterId = result.odPaymentMasterId;
						param.orderPrice = result.paymentPrice;

						// 신용카드
						if (result.psPayCd == 'PAY_TP.CARD') {
							let odid = fnNvl(result.odid).split(".");
							param.odid = odid[0];
							param.odids = fnNvl(result.odid);
							fnCardPay(param);
							// 가상계좌
						} else {
							param.odid = result.odid;
							param.odOrderId = result.odOrderId;
							param.pgBankCd = fnNvl($('#pgBankCd').data('kendoDropDownList').value());
							fnVirtualBankPay(param);
						}
					} else {

						let failMessage = "";
						if (stringUtil.getString(result.failMessage, "") != "") {

							let failMessageList = result.failMessage.split('∀');
							for(let i=0 ; i<failMessageList.length ; i++){
								failMessage += failMessageList[i] + "<br/>";
							}
						}else{
							failMessage = result.failMessage;
						}

						fnKendoMessage({message : failMessage, ok :function(){
								location.reload();
							}});
					}
				}

				break;

			case 'fail' :	// 오류
				console.log(result);
				break;

			case 'excelUploadFail' : // 엑셀업로드 실패
				fnKendoMessage({message : result.data.failMessage});
		}
	}

	// 파일 초기화
	function fileClear() {
		var agent = navigator.userAgent.toLowerCase();
		// IE
		if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ) {
			$("#uploadFile").replaceWith($("#uploadFile").clone(true));
			// OTHER
		} else {
			$("#uploadFile").val("");
		}
	}

	// 주문자 정보 초기화 (paramTrId : 초기화 행ID, paramEnable : 활성화여부(true/false)
	function fnClearBuyerData(paramTrId, paramEnable) {
		$('#' + paramTrId + ' :input').each(function (index, item) {
			if (item.id != 'recvZipCode' && item.id != 'recvAddr1') {
				!fnIsEmpty($(this).data("kendoDropDownList")) ? $(this).data("kendoDropDownList").enable(paramEnable) : $(this).prop('disabled', !paramEnable);
			}
			!fnIsEmpty($(this).data("kendoDropDownList")) ? $(this).data("kendoDropDownList").select(0) : $(this).val('');
		});
		fnSetBuyerPayInfo(buyerBaseInfo);		// 주문자 결제 정보 초기화
	}

	// 결제 정보 초기화
	function fnClearPayData() {
		$('#discountPrice').val('');	// 쿠폰 할인 금액
		$('#couponCount').text('');		// 쿠폰 개수
		$('#pointPrice').text('');		// 보유 적립금
		$('#usePointPrice').val('');	// 사용 적립금

		$('#useGoodsCouponList').val('');			// 상품 쿠폰 사용 목록
		$('#useShippingCouponListData').val('');	// 배송비 쿠폰 사용 목록
		$('#useCartPmCouponIssueId').val('');		// 장바구니 쿠폰 사용 목록
	}

	// 결제 정보 Show/Hide
	function fnShowPayInfo(param) {
		param ? $('#payDiv2').show() : $('#payDiv2').hide();
		param ? $('#payDiv3').show() : $('#payDiv3').hide();
	}

	// 주문자 주소 정보 셋팅
	function fnSetBuyerAddressInfo(paramInfo) {
		if (fnIsEmpty(paramInfo)) return false;

		// 회원 기본 주소 선택시

		$('#recvName').val(fnNvl(paramInfo.receiverName));		// 받는분

		// 휴대폰 번호
		if (fnValidatePhone(fnNvl(paramInfo.receiverMobile))) {
			let receiverMobile = paramInfo.receiverMobile.replace(/(\d{3})(\d{3,4})(\d{4})/g, '$1-$2-$3').split('-');
			$('#recvHp1').data('kendoDropDownList').value(receiverMobile[0]);
			$('#recvHp2').val(receiverMobile[1]);
			$('#recvHp3').val(receiverMobile[2]);
		}

		$('#recvZipCode').val(fnNvl(paramInfo.receiverZipCode));	// 우편번호
		$('#recvAddr1').val(fnNvl(paramInfo.receiverAddress1));		// 주소1
		$('#recvAddr2').val(fnNvl(paramInfo.receiverAddress2));		// 주소2
		$('#buildingCode').val(fnNvl(paramInfo.buildingCode));		// 건물관리 번호

		// 배송 요청사항
		if (fnIsEmpty(paramInfo.shippingComment)) {
			$('#recvDeliveryMsgCd').data('kendoDropDownList').value('');							// 배송 요청사항
		} else {
			let deliveryMsgTypeList = $('#recvDeliveryMsgCd').data('kendoDropDownList').dataSource._pristineData;
			let targetIndex = deliveryMsgTypeList.findIndex(deliveryMsg=>deliveryMsg.NAME == paramInfo.shippingComment);

			if (targetIndex != -1) {
				$('#recvDeliveryMsgCd').data('kendoDropDownList').text(paramInfo.shippingComment);	// 배송 요청사항
			} else {
				$('#recvDeliveryMsgCd').data('kendoDropDownList').text('직접입력');					// 배송 요청사항 (직접입력)
				$('#recvDeliveryMsgEtc').val(fnNvl(paramInfo.shippingComment));						// 배송 요청사항 Etc
			}
		}

		// 배송 출입정보
		$('#recvDoorMsgCd').data('kendoDropDownList').value(paramInfo.accessInformationType);	// 배송 출입정보 코드
		$('#recvDoorMsgCdNm').val(fnNvl(paramInfo.accessInformationPassword));					// 배송 출입정보 비밀번호

	}

	// 주문자 결제 정보 셋팅
	function fnSetBuyerPayInfo(paramInfo) {
		if (fnIsEmpty(paramInfo)) return false;
		let availablePoint = fnIsEmpty(paramInfo.availablePoint) ? 0 : fnNumberWithCommas(paramInfo.availablePoint);
		$('#pointPrice').text(availablePoint);		// 적립금 셋팅
		availablePoint == 0 ? $('#usePointPrice').prop('disabled', true) : $('#usePointPrice').prop('disabled', false);
		$('#usePointPrice').val('0');

		$('#bankCode').data("kendoDropDownList").select(0);
		$('#accountNumber').val('');
		$('#holderName').val('');
		if (fnIsEmpty(paramInfo.refundBankResult)) return false;
    	console.log(">>> paramInfo.refundBankResult : " + paramInfo.refundBankResult.bankCode + " / " + paramInfo.refundBankResult.accountNumber + " / " + paramInfo.refundBankResult.holderName);
		$('#bankCode').data('kendoDropDownList').value(paramInfo.refundBankResult.bankCode);
		$('#accountNumber').val(paramInfo.refundBankResult.accountNumber);
		$('#holderName').val(paramInfo.refundBankResult.holderName);
    }

	// 상품정보 그리드 셋팅
	function fnSetOrderGoodsGrid(param) {

		if (fnIsEmpty(param.goodsArr) || param.goodsArr.length < 1 || fnIsEmpty(param.cartArr) || param.cartArr.length < 1) return false;

		let orderGoodsArr = new Array();
		let no = 1;

		// var gridArr = orderGoodsGrid.dataSource.data();
		// if (!fnIsEmpty(gridArr) && gridArr.length > 0) {
		// 	for (let i=0; i<gridArr.length; i++) {
		// 		let rowItem	= gridArr[i];
		// 		rowItem.no = no++;
		// 		orderGood msArr.push(rowItem);
		// 	}
		// }



		let alertFlag		= false;
		let goodsTypeFlag	= true;
		var overLapGoodsNm	= "";
		let overLapCnt	 	= 0;

		for (let ii=0; ii<param.cartArr.length; ii++) {
			let checkGoodsFlag	= true;
			let targetSpCartId	= param.cartArr[ii].spCartId;
			let targetGoodsId	= param.cartArr[ii].ilGoodsId;

			for (let j=ii+1; j<param.cartArr.length; j++) {
				let rowItem	= param.cartArr[j];

				if (rowItem.ilGoodsId == targetGoodsId){
					alertFlag		= true;
					checkGoodsFlag	= false;
					if (overLapGoodsNm == ""){
						overLapGoodsNm = rowItem.goodsNm;
					}
					overLapCnt++;
				}
			}

			// if (!fnIsEmpty(gridArr) && gridArr.length > 0) {
			// 	for (let i=0; i<gridArr.length; i++) {
			//         let rowItem	= gridArr[i];
			//
			// 	    if (rowItem.ilGoodsId == targetGoodsId){
			// 			alertFlag		= true;
			// 			checkGoodsFlag	= false;
			// 			if (overLapGoodsNm == ""){
			// 				overLapGoodsNm = rowItem.goodsNm;
			// 			}
			// 			overLapCnt++;
			// 		}
			// 	}
			// }

			/**
			 * 일반, 폐기임박, 묶음 상품
			 *
			 ADDITIONAL("GOODS_TYPE.ADDITIONAL", "추가"),
			 DAILY("GOODS_TYPE.DAILY", "일일"),
			 DISPOSAL("GOODS_TYPE.DISPOSAL", "폐기임박"),
			 GIFT("GOODS_TYPE.GIFT", "증정"),
			 INCORPOREITY("GOODS_TYPE.INCORPOREITY", "무형"),
			 NORMAL("GOODS_TYPE.NORMAL", "일반"),
			 PACKAGE("GOODS_TYPE.PACKAGE", "묶음"),
			 RENTAL("GOODS_TYPE.RENTAL", "렌탈"),
			 SHOP_ONLY("GOODS_TYPE.SHOP_ONLY", "매장전용")
			 *
			 */


			if (param.cartArr[ii].goodsType != "GOODS_TYPE.NORMAL"
				&& param.cartArr[ii].goodsType != "GOODS_TYPE.DISPOSAL"
				&& param.cartArr[ii].goodsType != "GOODS_TYPE.PACKAGE"
				&& param.cartArr[ii].goodsType != "GOODS_TYPE.GIFT"
				&& param.cartArr[ii].goodsType != "GOODS_TYPE.GIFT_FOOD_MARKETING"
				&& param.cartArr[ii].goodsType != "GOODS_TYPE.DAILY"){

				alertFlag		= true;
				checkGoodsFlag	= false;
				goodsTypeFlag	= false;
			}



			if (checkGoodsFlag == true) {

				let targetIndex = param.goodsArr.findIndex(goods => goods.spCartId == targetSpCartId) != -1
					? param.goodsArr.findIndex(goods => goods.spCartId == targetSpCartId)
					: param.goodsArr.findIndex(goods => goods.ilGoodsId == targetGoodsId);


				let rowItem = fnSetOrderGoodsItem(param.cartArr[ii], param.goodsArr[targetIndex]);

				rowItem.no = no++;
				orderGoodsArr.push(rowItem);

				//추가상품 있을 경우
				if(param.cartArr[ii].additionalGoodsExistYn == 'Y' && param.cartArr[ii].additionalGoods.length > 0){
					let rowItemList = fnSetOrderGoodsItemForAdditionalGoods(param.cartArr[ii], param.goodsArr[targetIndex]);
					for(let i = 0; i<rowItemList.length; i++){
						rowItemList[i].no = no++;
						orderGoodsArr.push(rowItemList[i]);
					}
				}
			}
		}

		orderGoodsGrid.dataSource.data(orderGoodsArr);
		kendo.ui.progress($('#orderGoodsGrid'), false);

		if (alertFlag == true){
			if (overLapGoodsNm != ""){
				let msg = "동일한 상품이 등록 되어있습니다.<br/>" + "상품명 : " + overLapGoodsNm;
				if (overLapCnt > 1){
					msg += " 외 "+ overLapCnt + "건";
				}
				fnKendoMessage({message : msg});
				return;
			}
			if (goodsTypeFlag == false){
				fnKendoMessage({message : "일반, 폐기임박, 묶음, 일일 상품유형만 추가 가능합니다."});
				return;
			}

		}


	}

	// 상품정보 그리드 로우 데이터 셋팅
	function fnSetOrderGoodsItem(paramCartItem, paramGoodsItem) {

		let goodsNm = "${" + paramCartItem.goodsNm + "}";

		//일일상품
		if(paramCartItem.goodsType == "GOODS_TYPE.DAILY"){

			// 녹즙
			if(paramCartItem.goodsDailyType == "GOODS_DAILY_TP.GREENJUICE"){
				goodsNm += "\n"+"ㄴ 배송주기:" + paramCartItem.goodsDailyCycleTypeName;
				goodsNm += " / 배송요일:" + paramCartItem.goodsDailyCycleGreenJuiceWeekTypeName.join("/");
				goodsNm += " / 배송기간:" + paramCartItem.goodsDailyCycleTermTypeName;

				// 잇슬림
			}else if(paramCartItem.goodsDailyType == "GOODS_DAILY_TP.EATSSLIM"){
				goodsNm += "\n"+"ㄴ 배송주기:" + paramCartItem.goodsDailyCycleTypeName;
				goodsNm += " / 배송요일:" + paramCartItem.goodsDailyWeekText;
				goodsNm += " / 배송기간:" + paramCartItem.goodsDailyCycleTermTypeName;

				// 베이비밀
			}else if(paramCartItem.goodsDailyType == "GOODS_DAILY_TP.BABYMEAL"){
				goodsNm += "\n"+"ㄴ 배송유형:" + (paramCartItem.goodsDailyBulkYn == 'Y' ? "일괄배송" : "일일배송");
				if(paramCartItem.goodsDailyBulkYn == 'Y'){
					goodsNm += " / 세트수량:" + paramCartItem.goodsBulkTypeName;
				}else{
					goodsNm += " / 배송주기:" + paramCartItem.goodsDailyCycleTypeName;
					goodsNm += " / 배송기간:" + paramCartItem.goodsDailyCycleTermTypeName;
				}
				goodsNm += " / 식단유형:" + (paramCartItem.goodsDailyAllergyYn == 'Y' ? "알러지대체식단" : "일반식단");
			}
		}

		let goodsItem = new Object();
		goodsItem.ilGoodsId						= fnNvl(paramGoodsItem.ilGoodsId);						// 상품ID
		goodsItem.goodsNm						= goodsNm;												// 상품명
		goodsItem.recommendedPrice				= paramGoodsItem.recommendedPrice;						// 정상가
		goodsItem.salePrice						= paramGoodsItem.salePrice;								// 판매가
		goodsItem.stockQty						= paramCartItem.stockQty;								// 재고수량
		goodsItem.orderCnt						= paramCartItem.qty;									// 수량
		goodsItem.paidPrice						= paramCartItem.paymentPrice;							// 결제금액
		goodsItem.shippingPrice					= paramCartItem.shippingRecommendedPrice;				// 배송비
		goodsItem.deliveryDt 					= fnNvl(paramCartItem.deliveryDt);	// 도착 예정일
		goodsItem.choiceArrivalScheduledDateList= fnNvl(paramCartItem.choiceArrivalScheduledDateList);	// 도착 예정일 선택일
		goodsItem.deliveryType					= fnNvl(paramCartItem.deliveryType);					// 배송타입
		goodsItem.shippingIndex					= fnNvl(paramCartItem.shippingIndex);					// 배송정책 index
		goodsItem.spCartId						= fnNvl(paramCartItem.spCartId);						// 장바구니 PK
		goodsItem.orderIfDt						= fnNvl(paramCartItem.orderIfDt);						// I/F 일자
		goodsItem.shippingDt					= fnNvl(paramCartItem.shippingDt);						// 출고 예정일
		goodsItem.urWarehouseId					= fnNvl(paramCartItem.urWarehouseId);					// 출고처 PK
		goodsItem.warehouseNm					= fnNvl(paramCartItem.warehouseNm);						// 출고처명
		goodsItem.goodsType						= fnNvl(paramCartItem.goodsType);						// 상품유형
		goodsItem.orderCopyYn					= "Y";
		goodsItem.dawnDeliveryYn				= paramCartItem.dawnDeliveryYn;							// 새벽배송여부
		goodsItem.ilItemWarehouseId				= paramCartItem.ilItemWarehouseId;						// 품목별 출고처PK
		goodsItem.unlimitStockYn				= paramCartItem.unlimitStockYn;						// 재고 무제한여부
		goodsItem.notIfStockCnt					= paramCartItem.notIfStockCnt;						// 미연동재고 수량
		goodsItem.goodsDailyType				= paramCartItem.goodsDailyType;						// 일일상품타입
		goodsItem.goodsDailyCycleType 					= paramCartItem.goodsDailyCycleType;				// 일일상품 배송주기
		goodsItem.goodsDailyCycleGreenJuiceWeekType 	= paramCartItem.goodsDailyCycleGreenJuiceWeekType;	// 일일상품 배송요일
		goodsItem.goodsDailyCycleTermType 				= paramCartItem.goodsDailyCycleTermType;			// 일일상품 배송기간
		goodsItem.goodsBulkType 						= paramCartItem.goodsBulkType;						// 일일상품 세트수량
		goodsItem.goodsDailyBulkYn 						= paramCartItem.goodsDailyBulkYn;					// 일일상품 배송유형
		goodsItem.goodsDailyAllergyYn 					= paramCartItem.goodsDailyAllergyYn;				// 일일상품 알러지식단여부
		goodsItem.storeDeliveryIntervalType 			= paramCartItem.storeDeliveryIntervalType;			// 배송가능한 스토어배송권역정보(없거나 택배만 가능일 경우 -> 베이비밀 일괄배송 처리위해)
		goodsItem.additionalGoodsExistYn 				= paramCartItem.additionalGoodsExistYn;				// 추가구성상품 존재여부

		return goodsItem;
	}

	// 상품정보 그리드 로우 데이터 셋팅(추가상품 전용)
	function fnSetOrderGoodsItemForAdditionalGoods(paramCartItem, paramGoodsItem) {

		let additionalGoodsList = new Array();
		let additionalGoodsParam = paramCartItem.additionalGoods;

		for(let i = 0; i <  additionalGoodsParam.length ; i++){
			let goodsItem = new Object();

			goodsItem.ilGoodsId						= additionalGoodsParam[i].ilGoodsId;								// 상품ID
			goodsItem.goodsNm						= "&nbsp;&nbsp;&nbsp;&nbsp;ㄴ "+additionalGoodsParam[i].goodsName;	// 상품명
			goodsItem.recommendedPrice				= additionalGoodsParam[i].recommendedPrice;							// 정상가
			goodsItem.salePrice						= additionalGoodsParam[i].salePrice;								// 판매가
			goodsItem.stockQty						= additionalGoodsParam[i].stockQty;									// 재고수량
			goodsItem.orderCnt						= additionalGoodsParam[i].qty;										// 수량
			goodsItem.paidPrice						= additionalGoodsParam[i].paymentPrice;								// 결제금액
			goodsItem.shippingPrice					= paramCartItem.shippingRecommendedPrice;							// 배송비
			goodsItem.deliveryDt 					= fnNvl(paramCartItem.deliveryDt);						// 도착 예정일
			goodsItem.choiceArrivalScheduledDateList= fnNvl(paramCartItem.choiceArrivalScheduledDateList);	// 도착 예정일 선택일
			goodsItem.deliveryType					= fnNvl(paramCartItem.deliveryType);					// 배송타입
			goodsItem.shippingIndex					= fnNvl(paramCartItem.shippingIndex);					// 배송정책 index
			goodsItem.spCartId						= fnNvl(paramCartItem.spCartId);						// 장바구니 PK
			goodsItem.orderIfDt						= fnNvl(paramCartItem.orderIfDt);						// I/F 일자
			goodsItem.shippingDt					= fnNvl(paramCartItem.shippingDt);						// 출고 예정일
			goodsItem.urWarehouseId					= fnNvl(paramCartItem.urWarehouseId);					// 출고처 PK
			goodsItem.warehouseNm					= fnNvl(paramCartItem.warehouseNm);						// 출고처명
			goodsItem.goodsType						= "GOODS_TYPE.ADDITIONAL";								// 상품유형
			goodsItem.orderCopyYn					= "Y";
			goodsItem.dawnDeliveryYn				= paramCartItem.dawnDeliveryYn;							// 새벽배송여부
			goodsItem.ilItemWarehouseId				= additionalGoodsParam[i].ilItemWarehouseId;						// 품목별 출고처PK
			goodsItem.unlimitStockYn				= additionalGoodsParam[i].unlimitStockYn;							// 재고 무제한여부
			goodsItem.notIfStockCnt					= additionalGoodsParam[i].notIfStockCnt;							// 미연동재고 수량
			goodsItem.spCartAddGoodsId				= additionalGoodsParam[i].spCartAddGoodsId;				// 장바구니 추가 구성상품 PK

			additionalGoodsList.push(goodsItem);
		}

		return additionalGoodsList;
	}

	// 주문정보 그리드 셋팅
	function fnSetOrderGrid(paramArr) {
		if (fnIsEmpty(paramArr) || paramArr.length < 1) return false;

		for (let i=0; i<paramArr.length; i++) {
			paramArr[i].no			= paramArr[i].rnum;						// 순번
			paramArr[i].goodsNm 	= paramArr[i].goodsName;				// 상품명
			paramArr[i].storageType	= paramArr[i].storageMethodTypeName;	// 보관방법
			paramArr[i].orderPrice	= paramArr[i].orderAmt;					// 주문금액
		}
		orderGrid.dataSource.data(paramArr);
	}

	// 콤마 제거
	function fnUncomma(str) {
		str = String(str);
		return str.replace(/[^\d]+/g, '');
	}

	// 신용카드(비인증결제)
	function fnCardPay(param) {
		fnKendoPopup({
			id     : 'nonCardBankBookPopup',
			title  : '신용카드 비인증 결제',
			src    : '#/nonCardBankBookPopup',
			param  : param,
			width  : '500px',
			height : '550px',
			scrollable : "no",
			success: function(stMenuId, data) {
				var option = new Object();

				option.url = "#/orderCreate";
				option.menuId = 1290;
				if (data.hasOwnProperty("result") == true) {

					let result = data.result;
					if (result.result == "SUCCESS") {
						fnKendoMessage({message : "주문번호 : " + param.odid + " 주문이 생성되었습니다.", ok :function(){
								location.reload();
							}});
					} else {
						if ($.trim(stringUtil.getString(result.failMessage, "")) != "") {
							fnKendoMessage({message: result.failMessage, ok: function () {}});
						}
						$("#fnSaveBottom").attr('disabled',false);
					}
				} else {
					fnKendoMessage({message : "결제가 취소되었습니다.", ok :function(){
							$("#fnSaveBottom").attr('disabled',false);
						}});
				}
				return false;
			}
		});
	}

	function fnNvlForNumber( argv1 ){
		if( argv1 != null && argv1 != "" && argv1 != undefined ){
			return argv1;
		}else{
			return 0;
		}
	}
	//-------------------------------  Common Function end ------------------------------------


	//------------------------------- Validation Start ----------------------------------------
	// 저장 Validation
	function fnSaveValid() {
		// 주문자명
		if (fnIsEmpty($.trim($('#buyerName').val()))) {
			fnKendoMessage({ message : "주문자명은 필수 입니다."});
			return false;
		}

		// 주문자 휴대폰 번호
		if (fnIsEmpty($('#buyerHp1').data('kendoDropDownList').value()) || fnIsEmpty($.trim($('#buyerHp2').val())) || fnIsEmpty($.trim($('#buyerHp3').val()))) {
			fnKendoMessage({ message : "주문자 휴대폰 번호를 입력해 주시기 바랍니다."});
			return false;
		}

		let buyerHp = fnNvl($('#buyerHp1').data('kendoDropDownList').value()) + fnNvl($('#buyerHp2').val()) + fnNvl($('#buyerHp3').val()) + '';
		if (!fnValidatePhone(buyerHp)) {
			fnKendoMessage({ message : "주문자 휴대폰 번호가 유효하지 않습니다."});
			return false;
		}

		// 주문자 이메일
		if (fnIsEmpty($.trim($('#buyerEmail1').val())) || fnIsEmpty($.trim($('#buyerEmail2').val()))) {
			fnKendoMessage({ message : "주문자 이메일 주소를 입력해 주시기 바랍니다."});
			return false;
		}

		let buyerEmail = fnNvl($('#buyerEmail1').val()) + '@' + fnNvl($('#buyerEmail2').val()) + '';
		if (!fnValidateEmail(buyerEmail)) {
			fnKendoMessage({ message : "주문자 이메일 주소가 유효하지 않습니다."});
			return false;
		}

		let createTp = $('[name="orderCreateTp"]').filter(':checked').val();

		// 회원,비화원
		if (createTp == 'MEMBER' || createTp == 'NON_MEMBER') {
			// 받는분
			if (fnIsEmpty($('#recvName').val()) || fnIsEmpty($('#recvName').val())) {
				fnKendoMessage({ message : "받는분명은 필수 입니다."});
				return false;
			}

			// 배송지 주소1
			if (fnIsEmpty($('#recvZipCode').val()) || fnIsEmpty($('#recvAddr1').val())) {
				fnKendoMessage({ message : "배송지 주소를 선택해 주시기 바랍니다."});
				return false;
			}

			// 배송지 주소2
			if (fnIsEmpty($.trim($('#recvAddr2').val()))) {
				fnKendoMessage({ message : "배송지 상세 주소를 입력해 주시기 바랍니다."});
				return false;
			}

			// 배송지 휴대폰 번호
			if (fnIsEmpty($('#recvHp1').data('kendoDropDownList').value()) || fnIsEmpty($.trim($('#recvHp2').val())) || fnIsEmpty($.trim($('#recvHp3').val()))) {
				fnKendoMessage({ message : "배송지 휴대폰 번호를 입력해 주시기 바랍니다."});
				return false;
			}

			let recvHp = fnNvl($('#recvHp1').data('kendoDropDownList').value()) + fnNvl($('#recvHp2').val()) + fnNvl($('#recvHp3').val()) + '';
			if (!fnValidatePhone(recvHp)) {
				fnKendoMessage({ message : "배송지 휴대폰 번호가 유효하지 않습니다."});
				return false;
			}

			// 배송 출입정보
			if (fnIsEmpty($.trim($('#recvDoorMsgCd').val()))) {
				fnKendoMessage({ message : "배송 출입정보를 입력해 주시기 바랍니다."});
				return false;
			}

			let gridArr 	= orderGoodsGrid.dataSource.data();
			if (fnIsEmpty(gridArr) || gridArr.length < 1) {
				fnKendoMessage({ message : "주문 생성할 상품을 선택해 주시기 바랍니다."});
				return false;
			}
		} else if (createTp == 'EXCEL_UPLOAD') {
			let gridArr 	= orderGrid.dataSource.data();
			if (fnIsEmpty(gridArr) || gridArr.length < 1) {
				fnKendoMessage({ message : "주문 생성할 엑셀을 업로드 해주시기 바랍니다."});
				return false;
			}
		}

		let gridArr 	= orderGoodsGrid.dataSource.data();
		let cartArr 	= new Array();
		let deliveryArr = new Array();
		let totalPrice  = 0;					// 총 결제금액
		let nowDateTime = fnGetToday();

		for (let i=0; i<gridArr.length; i++) {
			if($('[name="payTp"]:checked').val() == "PAY_TP.VIRTUAL_BANK" && gridArr[i].shippingDt == nowDateTime) {
				fnKendoMessage({ message : "당일출고 주문은 가상계좌 결제가 불가능합니다. <br> 출고예정일 혹은 결제방식을 변경해 주세요."});
				return false;
			}

			cartArr.push(gridArr[i].spCartId);
			totalPrice += Number(fnUncomma(gridArr[i].paidPrice));
			if (i==0 || (gridArr[i-1].deliveryType != gridArr[i].deliveryType || gridArr[i-1].shippingIndex != gridArr[i].shippingIndex)) {
				let scheduledItem = new Object();
				scheduledItem.arrivalScheduledDate 	= gridArr[i].deliveryDt;
				scheduledItem.dawnDeliveryYn 		= gridArr[i].orderIfDawnYn == undefined ? gridArr[i].dawnDeliveryYn : gridArr[i].orderIfDawnYn;
				deliveryArr.push(scheduledItem);
				totalPrice += Number(fnUncomma(gridArr[i].shippingPrice));
			}
		}

		// 쿠폰, 적립금 사용에 따른 결제방식 무료 체크
		// 실제 결제 금액 = 총 결제금액 - 쿠폰 사용금액 - 적립금 사용금액
		totalPrice -= fnIsEmpty($('#discountPrice').val()) ? 0 : Number(fnUncomma($('#discountPrice').val()));
		totalPrice -= fnIsEmpty($('#usePointPrice').val()) ? 0 : Number(fnUncomma($('#usePointPrice').val()));
		let psPayCd = totalPrice == 0 ? 'PAY_TP.FREE' : $('[name="payTp"]:checked').val();	// 결제 정보 PK
		if(psPayCd == "PAY_TP.VIRTUAL_BANK") {
			let bankCode = fnNvl($('#bankCode').data('kendoDropDownList').value()); //BANK_CODE.KB
			let accountNumber = fnNvl($("#accountNumber").val()); //'환불계좌번호'
			let holderName = fnNvl($("#holderName").val()); //'예금주'
			console.log(">>> 환불계좌 : " + bankCode + " / " + accountNumber+ " / " + holderName);
			if(bankCode == "" || accountNumber == "" || holderName == "") {
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

		// 추가 상품 체크 - 증정품만 존재시 주문 불가
		/*
		let gridArr	= orderGoodsGrid.dataSource.data();
		let giftCnt	= 0;
		let gridCnt	= gridArr.length;
		for (let i=0; i<gridCnt; i++) {
			if (gridArr[i].goodsType == "GOODS_TYPE.GIFT" || gridArr[i].goodsType == "GOODS_TYPE.GIFT_FOOD_MARKETING"){
				giftCnt++;
			}
		}

		if(giftCnt == gridCnt){
			fnKendoMessage({ message : "증정 상품유형만으로는 주문 할 수 없습니다."});
			return false;
		}
		 */
		return true;
	}
	//------------------------------- Validation End ------------------------------------------


	//------------------------------- Html 버튼 바인딩  Start --------------------------------------
	/** Common Save */
	$scope.fnSave 				= function() { fnSave(); };
	/** 영역 보이기 / 숨기기 */
	$scope.fnToggleArea 		= function(btnDom, startNo, excpetionArea) { fnToggleArea(btnDom, startNo, excpetionArea); };
	/** 엑셀 업로드 파일선택 버튼 */
	$scope.fnExcelUpload 		= function() { fnExcelUpload(); };
	/** 엑셀 업로드 파일선택 버튼 클릭시 파일버튼 */
	$scope.fnExcelExport 		= function(event) { fnExcelExport(event); };
	/** 엑셀 업로드 파일첨부 삭제 버튼 */
	$scope.fnExcelUploadDelete	= function(event) { fnExcelUploadDelete(event); };
	/** 업로드 버튼 */
	$scope.fnExcelUploadConfirm	= function() { fnExcelUploadConfirm(); };
	/** Sample 다운로드 버튼 */
	$scope.fnSampleExcelDownload = function() { fnSampleExcelDownload(); };
	/** 회원 검색 버튼*/
	$scope.fnSearchBuyer 		= function() { fnSearchBuyer(); };
	/** 배송지 목록 버튼 */
	$scope.fnSearchShippingList = function() { fnSearchShippingList(); };
	/** 주소 찾기 버튼 */
	$scope.fnSearchAddress 		= function() { fnSearchAddress(); };
	/** 상품 검색 버튼 */
	$scope.fnSearchGoods 		= function() { fnSearchGoods(); };
	/** 상품 삭제 버튼 */
	$scope.fnDeleteGoods 		= function() { fnDeleteGoods(); };
	/** 쿠폰 사용 버튼 */
	$scope.fnUseCoupon 			= function() { fnUseCoupon(); };
	/** 적립금 사용 버튼 */
	$scope.fnUseAllPointPrice 	= function() { fnUseAllPointPrice(); };
	/** 계좌정보 유효성 확인*/
	$scope.fnValidBankAccount = function( ) {	fnValidBankAccount();};
	//------------------------------- Html 버튼 바인딩  End ----------------------------------------
}); // document ready - END