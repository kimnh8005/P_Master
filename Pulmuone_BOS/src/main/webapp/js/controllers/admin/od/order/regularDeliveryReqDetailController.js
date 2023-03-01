/**-----------------------------------------------------------------------------
 * description 		 : 정기배송주문 신청 내역
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.02.07		김명진          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var viewModel;
var pageParam = fnGetPageParam();												//GET Parameter 받기
var odRegularReqId = pageParam.odRegularReqId;									// 정기배송주문신청PK
var regularGoodsGridDs, regularGoodsGridOpt, regularGoodsGrid, shippingZoneDs;	// 정기배송 주문 신청 내역 상품 정보 그리드
var regularHistoryGridDs, regularHistoryGridOpt, regularHistoryGrid;			// 정기배송 주문 신청 내역 처리이력 정보 그리드
var regularStatusCd, goodsCycleTermTpNm, goodsCycleTerm, termExtensionCnt;
var inputMenuId = pageParam.inputMenuId;
$(document).ready(function() {
	importScript("/js/service/admin/od/order/regularDeliveryReqDetailGoodsListGridColumns.js");
	importScript("/js/service/od/order/orderCommSearch.js");

	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'regularDeliveryReqDetail',
			callback : fnUI
		});

	}

	// 화면 UI 초기화
	function fnUI(){

		fnTranslate();	// 다국어 변환
		fnInitButton();
		fnInitGrid();
		fnFormSearch();
		fnInitOptionBox();
		fnViewModelInit();
		fnDefaultSet();
		// 이벤트 바인딩
		bindEvents();

		//fnSearch();
		//orderSubmitUtil.search();
	};

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
	}
	//--------------------------------- Button End-----------------------------------

	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid(){

		$("#"+inputMenuId).addClass("active");

		// 상품그리드
		regularGoodsGridDs = fnGetEditDataSource({
			url          : '/admin/order/getOrderRegularReqDetailGoodsList?odRegularReqId=' + odRegularReqId,
			model_id     : 'regularDeliveryReqGoods',
			model_fields : regularDeliveryReqGoodsGridUtil.regularDeliveryReqGoodsModelFields()
		});

		regularGoodsGridOpt = {
			dataSource: regularGoodsGridDs,
			navigatable : true,
			height : 300,
			scrollable : true,
			columns : regularDeliveryReqGoodsGridUtil.regularDeliveryReqGoodsList(),
			editable : true
		};

		regularGoodsGrid = $('#regularGoodsGrid').initializeKendoGrid( regularGoodsGridOpt ).cKendoGrid();

		// 처리이력그리드
		regularHistoryGridDs = fnGetDataSource({
			url      : '/admin/order/getOrderRegularReqDetailHistoryList?odRegularReqId=' + odRegularReqId
		});
		regularHistoryGridOpt = {
			dataSource: regularHistoryGridDs,
			navigatable : true,
			height : 300,
			scrollable : true,
			columns : regularDeliveryReqHistoryGridUtil.regularDeliveryReqHistoryList()
		};

		regularHistoryGrid = $('#regularHistoryGrid').initializeKendoGrid( regularHistoryGridOpt ).cKendoGrid();

		regularHistoryGrid.bind("dataBound", function(){
			let rowNum = regularHistoryGridDs._data.length;

			$("#regularHistoryGrid tbody > tr .row-number").each(function(index){
				$(this).html(rowNum);
				rowNum--;
			});
		});
	}

	function fnFormSearch() {

		initRegularGoodsForm(); 		// 상품 그리드 초기화
		initRegularOrderBuyer();		// 신청정보 초기화
		initRegularOrderShippingZone();	// 배송정보 초기화
		initRegularOrderPayment();		// 결제정보 초기화
		initRegularOrderConsult();		// 상담내용 초기화
		initRegularOrderHistoryGrid();	// 처리이력 초기화
	}

	// 상품 그리드 초기화
	function initRegularGoodsForm() {

		//regularGoodsGridDs.query();
		regularGoodsGridDs.read();

	    // 그리드 전체선택 클릭
        $("#checkBoxAll").on("click", function(index){

            if( $("#checkBoxAll").prop("checked") ){

                $("input[name=rowCheckbox]").prop("checked", true);
            }else{

                $("input[name=rowCheckbox]").prop("checked", false);
            }
        });

        // 그리드 체크박스 클릭
        regularGoodsGrid.element.on("click", "[name=rowCheckbox]" , function(e){

            if( e.target.checked ){
                if( $("[name=rowCheckbox]").length == $("[name=rowCheckbox]:checked").length ){
                    $("#checkBoxAll").prop("checked", true);
                }
            }else{
                $("#checkBoxAll").prop("checked", false);
            }
        });
	}

	// 배송정보
	function initRegularOrderShippingZone() {

		fnAjax({
	        url     : '/admin/order/getOrderRegularReqDetailShippingZone',
	        params  : {'odRegularReqId' : odRegularReqId},
	        success :
	            function( data ){
	        		$("#odRegularReqShippingZoneId").text(data.odRegularReqShippingZoneId);
	        		$("#deliveryType").text(data.deliveryType);
	        		$("#recvNm").text(data.recvNm);
	        		$("#recvHp").text(fnPhoneNumberHyphen(data.recvHp));
	        		$("#recvZipCd").text(data.recvZipCd);
	        		$("#recvAddr1").text(data.recvAddr1);
	        		$("#recvAddr2").text(data.recvAddr2);
	        		$("#deliveryMsg").text(data.deliveryMsg);
	        		$("#doorMsg").text(data.doorMsgCdNm);
	        		$("#shippingCreateDt").text(data.createDt);
	        		shippingZoneDs = data;
		        },
		        isAction : 'select'
	    });
	}

	// 신청정보
	function initRegularOrderBuyer() {

		fnAjax({
	        url     : '/admin/order/getOrderRegularReqDetailBuyer',
	        params  : {'odRegularReqId' : odRegularReqId},
	        success :
	            function( data ){
	        		regularStatusCd = data.regularStatusCd;
	        		goodsCycleTermTpNm = data.goodsCycleTermTpNm;
	        		goodsCycleTerm = data.goodsCycleTerm;
	        		termExtensionCnt = data.termExtensionCnt;
	        		$("#reqId").text(data.reqId);
	        		$("#createDt").text(data.createDt);
	        		$("#reqRound").html(data.reqRound + " / 총 " + data.totCnt + (data.termExtensionCnt > 0 ? "<br/>(" + data.termExtensionCnt + "회 연장)" : ""));
	        		$("#goodsCycleTermTpNm").text(data.goodsCycleTermTpNm);
	        		$("#goodsCycleTpNm").text(data.goodsCycleTpNm);
	        		$("#arriveDt").text(data.weekCdNm + "요일");
	        		$("#buyerNm").text(data.buyerNm);
	        		$("#loginId").text(data.loginId);
	        		$("#urGroupNm").text(data.urGroupNm);
	        		$("#erpEmployeeYn").text(data.erpEmployeeYn != "N" ? "Y" : data.erpEmployeeYn);
	        		$("#buyerHp").text(fnPhoneNumberHyphen(data.buyerHp));
	        		$("#buyerMail").text(data.buyerMail);
	        		$("#regularStatusCdNm").text(data.regularStatusCdNm);
		        },
		        isAction : 'select'
	    });
	}

	// 결제정보
	function initRegularOrderPayment() {

		fnAjax({
			url     : '/admin/order/getOrderRegularReqDetailPayInfo',
			params  : {'odRegularReqId' : odRegularReqId},
			success :
				function( data ){
				regularOrderPaymentInfo(data);
			},
			isAction : 'select'
		});
	}

	// 결제정보 내용 리스트
	function regularOrderPaymentInfo(data) {

		var html = document.querySelector('#templatePayment').innerHTML;
		var resultHtml = "";
		for(var i=0; i<data.rows.length; i++){

			var title = "";
			var salePrice = 0;
			var shippingPrice = data.rows[i].shippingPrice;
			var discountPrice = 0;
			var addDiscountPrice = 0;
			var paidPrice = 0;
			if(data.rows[i].reqRoundYn == "N") {
				title = "결제 예정 금액";
				salePrice = data.rows[i].recommendedPrice;
				discountPrice = data.rows[i].recommendedPrice - data.rows[i].salePrice;
				addDiscountPrice = data.rows[i].discountPrice + data.rows[i].addDiscountPrice;
				paidPrice = data.rows[i].paidPrice;
			}
			else {
				title = "결제 완료 정보 (" + data.rows[i].reqRound + "회차)";
				salePrice = data.rows[i].salePrice;
				discountPrice = data.rows[i].discountPrice;
				addDiscountPrice = data.rows[i].addDiscountPrice;
				paidPrice = data.rows[i].paymentPrice;
			}

			resultHtml += html.replace("{title}"			, title)
							  .replace("{salePrice}"		, kendo.toString(kendo.parseInt(salePrice), "n0") + " 원")
							  .replace("{shippingPrice}"	, kendo.toString(kendo.parseInt(shippingPrice), "n0") + " 원")
							  .replace("{discountPrice}"	, kendo.toString(kendo.parseInt(discountPrice), "n0") + " 원")
							  .replace("{addDiscountPrice}"	, kendo.toString(kendo.parseInt(addDiscountPrice), "n0") + " 원")
							  .replace("{paidPrice}"		, kendo.toString(kendo.parseInt(paidPrice), "n0") + " 원");
		}

		if(resultHtml == "") {
			resultHtml = document.querySelector('#templatePaymentEmpty').innerHTML;
		}

		document.querySelector("#paymentInfo").innerHTML = resultHtml;
	}

	// 상담정보
	function initRegularOrderConsult() {

		$('#modifyNm').text(PG_SESSION.loginName);
		$('#modifyId').text(PG_SESSION.loginId);

		fnAjax({
			url     : '/admin/order/getOrderRegularReqDetailConsultList',
			params  : {'odRegularReqId' : odRegularReqId},
			success :
				function( data ){
				regularOrderCounsultInfo(data);
			},
			isAction : 'select'
		});
	}

	// 주문 상담 내용 리스트
	function regularOrderCounsultInfo(data) {

		var html = document.querySelector('#templateConsult').innerHTML;
		var resultHtml = "";
		for(var i=0; i<data.rows.length; i++){

			resultHtml += html.replace(/{userNm}/ig, data.rows[i].userNm)
							  .replace(/{loginId}/ig, data.rows[i].loginId)
							  .replace(/{createDt}/ig, data.rows[i].createDt)
							  .replace(/{odRegularReqMemoId}/ig, data.rows[i].odRegularReqMemoId)
							  .replace(/{memo}/ig, data.rows[i].memo)
							  .replace("{odRegularReqMemoIdText}", data.rows[i].odRegularReqMemoId)
							  .replace("{modifyIdList1}", data.rows[i].loginId)
							  .replace("{modifyIdList2}", data.rows[i].loginId)
							  .replace("{modifyIdList3}", data.rows[i].loginId)
							  .replace("{modifyIdList4}", data.rows[i].loginId)
							  .replace("{odRegularReqMemoIdUpdate2}", data.rows[i].odRegularReqMemoId)
							  .replace("{odRegularReqMemoIdDel2}", data.rows[i].odRegularReqMemoId)
							  .replace("{odRegularReqMemoIdSave2}", data.rows[i].odRegularReqMemoId)
			;
		}

		document.querySelector("#consultInfo").innerHTML = resultHtml;

		//신청상담내용 등록한 관리자 아닌경우 수정/삭제 버튼 hide
		for(var j=0; j < $('.consult__manager__id').length; j++){
			var loginId = $('.consult__manager__id')[j].getAttribute('name');
			if(PG_SESSION.loginId != loginId) {
				$("button[name='"+loginId+"']").hide();
			}
		}
	}

	// 처리 이력 그리드
	function initRegularOrderHistoryGrid() {

//		regularHistoryGridDs.query();
		regularHistoryGridDs.read();
	}
	//------------------------------- Grid End -------------------------------

	function fnSearch(){
	}

	function fnClear(){
		$('#searchForm').formClear(true);
	}
	//--------------------------------- Button End---------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	// 옵션 초기화
	function fnInitOptionBox(){
	}

	//---------------Initialize Option Box End ------------------------------------------------

	//-------------------------------  events Start  -------------------------------

	function bindEvents() {

		// 상품정보 > 상품코드, 상품명 클릭
		$('#ng-view').on("click", "div.popGoodsClick", function(e) {
			e.preventDefault();
			let dataItems = regularGoodsGrid.dataItem($(this).closest('tr'));
			let ilGoodsId = dataItems.ilGoodsId;
			window.open("#/goodsMgm?ilGoodsId="+ilGoodsId,"_blank");
		});

		// 테이블 접기 버튼
		$(".table-toggleBtn").on("click", function(e) {
			const $table = $(this).closest('table');
			$table.toggleClass('fold');
		});
	}
	//-------------------------------  events End  -------------------------------

	//-------------------------------  Common Function start ----------------------------------

    // viewModel 초기화
    function fnViewModelInit(){};

    // 기본값 설정
    function fnDefaultSet(){};

    // 회차별 상품정보 팝업
    function fnReqRoundGoodsPopup(params) {
		fnKendoPopup({
			id: 'regularReqGoodsListPopup',
			title: fnGetLangData({ nullMsg: '회차별 상품정보' }),
			param: params,
			src: '#/regularReqGoodsListPopup',
			width: '550px',
			height: '600px',
			success: function(id, data) {
				fnFormSearch();
			}
		});
    };

	/** 신청정보 변경 */
	function reqInfoChg(params) {
		if(regularStatusCd == 'REGULAR_STATUS_CD.CANCEL' || regularStatusCd == 'REGULAR_STATUS_CD.END') {
			fnKendoMessage({message: '이미 종료 된 정기배송주문건 입니다.'});
			return false;
		}
		fnKendoPopup({
			id: 'regularReqInfoPopup',
			title: fnGetLangData({ nullMsg: '신청 정보' }),
			param: params,
			src: '#/regularReqInfoPopup',
			width: '550px',
			height: '300px',
			success: function(id, data) {
				fnFormSearch();
			}
		});
	};
	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {};

	/** 추가 상품 삭제 */
	$scope.goodsDel = function(obj) {
		var dataRow = $(obj).closest('tr');
		regularGoodsGridDs.remove(regularGoodsGrid.dataItem($(dataRow)));
	};

	/** 회차별 상품 정보 */
	$scope.roundGoods = function () {
		var params = {
				'odRegularReqId' : odRegularReqId,
			     'inputMenuId' : inputMenuId
		};
		params.ilGoodsIdList = [];
		fnReqRoundGoodsPopup(params);
	};

	/** 건너뛰기 */
	$scope.skip = function () {
		var dataRows = $('#regularGoodsGrid tbody > tr input[type=checkbox]:checked').closest('tr');
		if(dataRows.length < 1) {
			fnKendoMessage({message: '선택한 상품이 없습니다.'});
			return false;
		}
		var params = {
				'odRegularReqId' : odRegularReqId,
				'inputMenuId': inputMenuId
		};
		params.ilGoodsIdList = [];
		for(var i=0; i<dataRows.length; i++) {
			var rowData = regularGoodsGrid.dataItem($(dataRows[i]));
			params.ilGoodsIdList[i] = rowData.ilGoodsId;
		}
		fnReqRoundGoodsPopup(params);
	};

	/** 상품검색 공통 */
	$scope.addGoods = function() {

		if(regularStatusCd == 'REGULAR_STATUS_CD.CANCEL' || regularStatusCd == 'REGULAR_STATUS_CD.END') {
			fnKendoMessage({message: '이미 종료 된 정기배송주문건 입니다.'});
			return false;
		}

		var params = {};
		params.selectType = "single";            	// 그리드 체크박스 타입 ( single : 하나만 선택됨, multi : 여러개 선택됨 )
		params.goodsType = "GOODS_TYPE.NORMAL";  	// 상품유형(복수 검색시 , 로 구분)
		params.saleStatus = "SALE_STATUS.ON_SALE";	// 판매상태
		params.isSaleTypes = true;					// 판매유형 활성화
		params.saleType = "SALE_TYPE.REGULAR";
		params.goodsCallType = "regularDeliveryReqDetail";
		fnKendoPopup({
			id          : "goodsSearchPopup"
		  , title       : "상품추가"
		  , width       : "1700px"
		  , height      : "800px"
		  , scrollable  : "yes"
		  , src         : "#/goodsSearchPopup"
		  , param       : params
		  , success     : function( id, data ) {

			  var item = data[0];
			  if(item === undefined) {
				  return false;
			  }
			  if(item.saleTypeCode != 'SALE_TYPE.REGULAR') {
				  fnKendoMessage({message: '정기배송 상품만 추가 가능합니다.'});
				  return false;
			  }
			  var dataRows = $('#regularGoodsGrid tbody > tr input[type=checkbox]').closest('tr');
			  for(var i=0; i<dataRows.length; i++) {
				  var rowData = regularGoodsGrid.dataItem($(dataRows[i]));
				  // 취소 상품이 아닐 경우
				  if(rowData.reqDetailStatusCd != 'REGULAR_DETL_STATUS_CD.CANCEL_SELLER' && rowData.reqDetailStatusCd != 'REGULAR_DETL_STATUS_CD.CANCEL_BUYER') {
					  if(rowData.ilGoodsId == item.goodsId) {
						  fnKendoMessage({message: '이미 신청된 상품 입니다.'});
						  return false;
					  }
				  }
			  }
			  var addItem = {
					  ilItemCd : item.itemCode,
					  itemBarcode : item.itemBarcode,
					  ilGoodsId : item.goodsId,
					  goodsNm : item.goodsName,
					  storageMethodTpNm : item.storageMethodTypeName,
					  orderCnt : 1,
					  reqDetailStatusCdNm : '신청',
					  goodsTpNm : item.goodsTypeName,
					  warehouseNm : item.warehouseName,
					  urWarehouseId : item.warehouseId,
					  shippingPrice : 0,
					  deliveryTmplNm : item.name,
					  recommendedPrice : item.recommendedPrice,
					  salePrice : item.salePrice,
					  addGoodsFlag : true
			  };
			  regularGoodsGridDs.insert(regularGoodsGridDs._view.length, addItem);
		  }
		});
	};

	/** 상품 추가 저장 */
	$scope.addGoodsSave = function() {

		  var dataRows = $('#regularGoodsGrid table[role=grid] > tbody > tr');
		  var newCnt = 0;
		  var countValid = true;
		  var params = {
				  'odRegularReqId' : odRegularReqId
		  };
		  params.ilGoodsIdList = [];
		  params.ilItemCdList = [];
		  params.goodsNmList = [];
		  params.orderCntList = [];

		  for(var i=0; i<dataRows.length; i++) {
			  var rowData = regularGoodsGrid.dataItem($(dataRows[i]));
			  if(rowData.addGoodsFlag == true) {
				  params.ilGoodsIdList[newCnt] = rowData.ilGoodsId;
				  params.ilItemCdList[newCnt] = rowData.ilItemCd;
				  params.goodsNmList[newCnt] = rowData.goodsNm;
				  params.orderCntList[newCnt] = rowData.orderCnt;
				  newCnt++;
				  if(rowData.orderCnt > 99 || rowData.orderCnt < 1) {
					  countValid = false;
				  }
			  }
		  }

		  if(!countValid) {
			  fnKendoMessage({message: '상품수량을 확인 해주세요.'});
			  return false;
		  }

		  if(newCnt < 1) {
			  fnKendoMessage({message: '추가된 상품이 없습니다.'});
			  return false;
		  }

		  fnAjax({
			  url: '/admin/order/addOrderRegularReqGoods',
			  params: params,
			  success:
				  function (data) {
				  fnKendoMessage({message: '상품이 추가 되었습니다.'});
				  fnFormSearch();
			  },
			  isAction: 'insert'
		  });
	}

	/** 신청기간 연장 */
	$scope.regularReqExtensionBtn = function () {

		if(regularStatusCd == 'REGULAR_STATUS_CD.CANCEL' || regularStatusCd == 'REGULAR_STATUS_CD.END') {
			fnKendoMessage({message: '이미 종료 된 정기배송주문건 입니다.'});
			return false;
		}

		let term = goodsCycleTerm * (termExtensionCnt + 2);
		fnKendoMessage({message:'신청기간을 연장 하시겠습니까?<br/>신청기간 : ' + goodsCycleTermTpNm + '<br />연장 후 신청기간: ' + term + ' 개월', type : "confirm" , ok : function(){

			var params = {
					'odRegularReqId' : odRegularReqId
			};
			fnAjax({
				url: '/admin/order/putOrderRegularGoodsCycleTermExtension',
				params: params,
				success:
					function (data) {
					fnFormSearch();
					fnKendoMessage({message: '기간이 연장 되었습니다.'});
				},
				isAction: 'update'
			});
		}});
	}

	/** 신청정보 기간 변경 */
	$scope.reqInfoTermChg = function() {
		var params = {'odRegularReqId' : odRegularReqId, 'changeType' : 'goodsCycleTermTp'};
		reqInfoChg(params);
	};

	/** 신청정보 주기 변경 */
	$scope.reqInfoCycleChg = function() {
		var params = {'odRegularReqId' : odRegularReqId, 'changeType' : 'goodsCycleTp'};
		reqInfoChg(params);
	};

	/** 신청정보 요일 변경 */
	$scope.reqInfoWeekChg = function() {
		var params = {'odRegularReqId' : odRegularReqId, 'changeType' : 'weekCd'};
		reqInfoChg(params);
	};

	/** 구독 해지 */
	$scope.expire = function () {
		var selectRows = $('#regularGoodsGrid tbody > tr input[name=rowCheckbox]:checked').closest('tr');

		if( selectRows.length < 1) {
			fnKendoMessage({message: '선택한 상품이 없습니다.'});
		}
		else {

			var dataRows = $('#regularGoodsGrid tbody > tr');
			var newGoodsCnt = 0;

			for(var i=0; i<dataRows.length; i++) {
				var rowData = regularGoodsGrid.dataItem($(dataRows[i]));
				if (rowData.addGoodsFlag == true) {
					newGoodsCnt++;
				}
			}
			if(newGoodsCnt > 0) {
				fnKendoMessage({message: '상품 추가 저장 후 가능합니다.'});
				return false;
			}

			fnKendoMessage({message:'구독해지 하시겠습니까?', type : "confirm" , ok : function(){

				let params = {
					'odRegularReqId' : odRegularReqId
				};
				params.ilGoodsIdList = [];

				for(var i = 0 ; i < selectRows.length ; i++){

	    			var selectedRowData = regularGoodsGrid.dataItem($(selectRows[i]));

					params.ilGoodsIdList[i] = selectedRowData["ilGoodsId"];
	    		}

				fnAjax({
					url: '/admin/order/putOrderRegularReqGoodsCancel',
					params: params,
					success:
						function (data) {
							fnKendoMessage({message: '구독해지 되었습니다.'});
							fnFormSearch();
						},
					isAction: 'batch'
				});
			}});
		}
	};

	/** 배송정보 변경 */
	$scope.fnShippingZoneChg = function () {

		let param = {};

		let recvHp = shippingZoneDs.recvHp.replace(/-/ig, '');
		let recvHpPrefix = recvHp.substring(0, 3);
		// let recvHp1 = recvHp.substring(3, recvHp.length - 4);
		// let recvHp2 = recvHp.substring(recvHp.length - 4);
		//inputFormMain "배송 정보" 탭
		param.odRegularReqId = shippingZoneDs.odRegularReqId; // 정기배송신청번호
		// param.odRegularReqShippingZoneId = shippingZoneDs.odRegularReqShippingZoneId; // 배송번호
		// param.recvNm = shippingZoneDs.recvNm; // 받는분
		param.phonePrefix = recvHpPrefix; // 휴대폰번호 prefix
		// param.recvHp1 = recvHp1; // 휴대폰번호1
		// param.recvHp2 = recvHp2; // 휴대폰번호2
		// param.recvZipCd = shippingZoneDs.recvZipCd; // 우편번호
		// param.recvAddr1 = shippingZoneDs.recvAddr1; // 주소1
		// param.recvAddr2 = shippingZoneDs.recvAddr2; // 주소2
		param.deliveryMsgCd = shippingZoneDs.deliveryMsgCd; // 배송요청사항코드
		// param.deliveryMsg = shippingZoneDs.deliveryMsg; // 배송요청사항메세지
		param.doorMsgCd = shippingZoneDs.doorMsgCd; // 배송출입정보코드
		// param.doorMsgCdNm = shippingZoneDs.doorMsgCdNm; // 배송출입정보메세지
		// param.doorMsg = shippingZoneDs.doorMsg; // 배송출입정보>공동현관비밀번호
		// param.doorEtc = shippingZoneDs.doorEtc; // 배송출입정보>기타/직접입력
		param.urUserId = shippingZoneDs.urUserId; // 회원번호

		fnKendoPopup({
			id: 'regularShippingPopup',
			title: fnGetLangData({ nullMsg: '배송 정보 변경' }),
			param: param,
			src: '#/regularShippingPopup',
			width: '600px',
			height: '850px',
			success: function(id, data) {
				initRegularOrderShippingZone();
				initRegularOrderHistoryGrid();
			}
		});
	};

	/** Common Clear*/
	$scope.fnClear =function(){};

	$scope.fnExcelDownload =function(){};

	 // 저장
	$scope.fnSave = function() {
		var url  = '/admin/order/addOrderRegularReqDetailConsult?odRegularReqId=' + odRegularReqId; // 정기배송신청상담내용 등록 url

		let memo = $.trim($("#memo").val());
		if (memo == ""){
			fnKendoMessage({message: '신청 상담 내용을 입력해주세요.'});
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
							$("#memo").val("");
							initRegularOrderConsult();
						},
					isAction: 'batch'
				});
			}
		}
	};

	//수정
	$scope.fnUpdate = function(id, obj){
		fnKendoMessage({message : '수정하시겠습니까?', type : "confirm", ok : function(){
					$("#fnUpdate_"+id).hide();
					$("#fnDel_"+id).hide();
					$("#fnConsultSave_"+id).show();
					$("#memo_"+id).css('background-color', '#FFFFFF').prop('disabled', false);
				}});
	};

	// 삭제
	$scope.fnDel = function(id){
		var url  = '/admin/order/delOrderRegularReqDetailConsult'; // 정기배송신청상담내용 삭제 url
		fnKendoMessage({message:'삭제 하시겠습니까?', type : "confirm" , ok : function(){
			fnAjax({
				url: url,
				params: {'odRegularReqMemoId' : id},
				success:
					function (data) {
						fnKendoMessage({message: '삭제되었습니다.'});
						initRegularOrderConsult();
					},
				isAction: 'batch'
			});
		}});
	};

	//신청상담 리스트 (수정 -> 저장)
	$scope.fnConsultSave = function(id){
		var url  = '/admin/order/putOrderRegularReqDetailConsult'; // 정기배송신청상담내용 수정 url
		let memo = $.trim($('#memo_'+id).val());
		if (memo == ""){
			fnKendoMessage({message: '신청 상담 내용을 입력해주세요.'});
			return false;
		} else {
			fnAjax({
				url: url,
				params: {'odRegularReqMemoId' : id, 'memo' : memo},
				success:
					function (data) {
						fnKendoMessage({message: '수정 되었습니다.'});
						initRegularOrderConsult();
					},
				isAction: 'batch'
			});
		}
	};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	fnInputValidationForAlphabetNumberLineBreakComma("codeSearch");

	//------------------------------- Validation End -------------------------------------

}); // document ready - END
