/**-----------------------------------------------------------------------------
 * description 		 : 주문생성내역 조회
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.13		강상국          최초생성
 * @
 * **/
'use strict';
var pageParam = fnGetPageParam(); // GET 방식으로 전달된 parameter 객체

var PAGE_SIZE = 20;
var itemGridOpt, itemGrid, itemGridDs;

$(document).ready(function() {

	importScript("/js/service/od/order/orderCommSearch.js", function (){
		fnInitialize();
	});

	// Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', {
			flag : 'true'
		});

		fnPageInfo({
			PG_ID : 'orderCreateList',
			callback : fnUI
		});
	};

	//전체화면 구성
	function fnUI() {
		fnTranslate();		// comm.lang.js 안에 있는 공통함수 다국어
		fnInitButton();		// Initialize Button
		fnInitCompont();	// 상품 할인 변경 검색조건 Initialize
        fnItemGrid();		// 주문생성내역 Grid
        fnSearch();
	}

	function fnInitButton() {
		$('#fnSearch , #fnClear , #EXCEL_DN').kendoButton();
	};

	function fnInitCompont() {
        // 생성구분
        fnTagMkRadio({
            id : "orderCreateTypeRadio",
            tagId : "orderCreateTypeRadio",
            data  : [
    					{ "CODE" : ""  , "NAME" : "전체" },
            			{ "CODE" : "T" , "NAME" : "단일주문생성" },
            			{ "CODE" : "S" , "NAME" : "개별주문생성" }
                    ],
            style : {}
        });
        $("input:radio[name='orderCreateTypeRadio']:radio[value='']").attr("checked", true);

        // 상태
        fnTagMkRadio({
            id : "createStatusRadio",
            tagId : "createStatusRadio",
            data  : [
    					{ "CODE" : ""  , "NAME" : "전체" },
            			{ "CODE" : "E" , "NAME" : "생성완료" },
            			{ "CODE" : "W" , "NAME" : "대기중" },
            			{ "CODE" : "C" , "NAME" : "입금대기중" }
                    ],
            style : {}
        });
        $("input:radio[name='createStatusRadio']:radio[value='']").attr("checked", true);

        // 검색어
		fnKendoDropDownList({
	        id: "keywordType",
	        data: [
	            { CODE: "buyerNm", NAME: "주문자명" },
	            { CODE: "buyerHp", NAME: "연락처" },
	            { CODE: "goodsNm",  NAME: "상품명" }
	        ],
	        valueField: "CODE",
	        textField: "NAME"

	    });

		//등록일/수정일 시작날짜
		fnKendoDatePicker({
			id : 'startDate',
			format : 'yyyy-MM-dd',
			btnStartId: "startDate",
			btnEndId: "endDate",
			defVal: fnGetDayAdd(fnGetToday(),-6),
			defType : 'oneWeek',
			change : function(e) {
				fnDateValidation(e, "start", "startDate", "endDate", -6);
				fnValidateCal(e)
			}

		});

		//등록일/수정일 종료날짜
		fnKendoDatePicker({
			id : 'endDate',
			format : 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startDate',
			btnEndId : 'endDate',
			defVal: fnGetToday(),
			defType : 'oneWeek',
			minusCheck: true,
			nextDate: false,
			change : function(e) {
				fnDateValidation(e, "end", "startDate", "endDate");
				fnValidateCal(e)
			}
		});

		//부가정보 선택
		fnKendoDropDownList({
			id    : 'addInfoSel',
			data  : [
				{"CODE":""		, "NAME":"선택해주세요"},
				{"CODE":"BIRTH"	, "NAME":"생년월일"},
				{"CODE":"BSNUM"	, "NAME":"사업자번호"}
			],
			textField :"NAME",
			valueField : "CODE",
			value : ""
		});

		//할부기간
		fnKendoDropDownList({
			id    : 'planPeriod',
			data  : [
				{"CODE":"00", "NAME":"일시불"},
				{"CODE":"02", "NAME":"2"},
				{"CODE":"03", "NAME":"3"},
				{"CODE":"04", "NAME":"4"},
				{"CODE":"05", "NAME":"5"},
				{"CODE":"06", "NAME":"6"},
				{"CODE":"07", "NAME":"7"},
				{"CODE":"08", "NAME":"8"},
				{"CODE":"09", "NAME":"9"},
				{"CODE":"10", "NAME":"10"},
				{"CODE":"11", "NAME":"11"},
				{"CODE":"12", "NAME":"12"},
				{"CODE":"13", "NAME":"13"},
				{"CODE":"14", "NAME":"14"},
				{"CODE":"15", "NAME":"15"},
				{"CODE":"16", "NAME":"16"},
				{"CODE":"17", "NAME":"17"},
				{"CODE":"18", "NAME":"18"},
				{"CODE":"19", "NAME":"19"},
				{"CODE":"20", "NAME":"20"},
				{"CODE":"21", "NAME":"21"},
				{"CODE":"22", "NAME":"22"},
				{"CODE":"23", "NAME":"23"},
				{"CODE":"24", "NAME":"24"}
			],
			textField :"NAME",
			valueField : "CODE",
			value : ""
		});


		$('#cardPayPopup').kendoWindow({
			visible: false,
			modal: true
		});

		$('#bankBookPopup').kendoWindow({
			visible: false,
			modal: true
		});
	}

	//주문생성 리스트 Grid
	function fnItemGrid() {
		itemGridDs = fnGetEditPagingDataSource({
			url : '/admin/order/create/getOrderCreateList',
			pageSize : PAGE_SIZE
		});

		itemGridOpt = {
			dataSource : itemGridDs,
			pageable : {
				pageSizes : [ 20, 30, 50 ],
				buttonCount : 5
			},
			navigatable: true,
			columns : [
				{ field : 'rownum',  title : 'No', width : '40px', attributes : { style : 'text-align:center' }}
			,	{ field : 'buyerNm',  title : '주문자명', width : '100px', attributes : { style : 'text-align:center' }}
			,	{ field : 'createDt', title : '등록일', width : '80px', attributes : { style : 'text-align:center' }, format: "{0:yyyy-MM-dd}" }
            ,   { field : 'createType', title : '생성구분', width : '80px', attributes : { style : 'text-align:center' }
		    		, template : function(dataItem){
		    			return dataItem.createType == "T" ? "단일주문생성" : "개별주문생성";
		    		}
            	}
            ,   { field : 'successOrderCnt', title : '주문건수/<BR>(주문번호기준)', width : '100px', attributes : { style : 'text-align:right' },format: "{0:n0}" }
            ,	{ field : 'successOrderDetlCnt', title : '품목건수/<BR>(주문상세번호기준)', width : '100px', attributes : { style : 'text-align:right' },format: "{0:n0}" }
			,	{ field : 'orderPrice', title : '주문금액', width : '100px', attributes : { style : 'text-align:right' },format: "{0:n0}" }
			,	{ field : 'createStatus', title : '상태', width : '80px', attributes : { style : 'text-align:center' }
		    		, template : function(dataItem){
		    			var createStatusNm = "";
		    			if (dataItem.createStatus == "E") createStatusNm = "생성완료";
		    			else if (dataItem.createStatus == "W") createStatusNm = "대기중";
		    			else if (dataItem.createStatus == "C") createStatusNm = "입금대기중";
		    			return createStatusNm;
		    		}
				}
			,	{ field : 'odid', title : 'oidi', width : '100px', attributes : { style : 'text-align:center' }, hidden:true}
			,	{ field : 'odCreateInfoId', title : 'odCreateInfoId', width : '100px', attributes : { style : 'text-align:center' }, hidden:true}
			,	{ field : 'odPaymentMasterId', title : 'odPaymentMasterId', width : '100px', attributes : { style : 'text-align:center' }, hidden:true}
			,	{ field : 'orderPaymentType', title : 'orderPaymentType', width : '100px', attributes : { style : 'text-align:center' }, hidden:true}
    		, 	{ title : '관리'	, width:'200px', attributes:{ style:'text-align:center;' , class:'forbiz-cell-readonly' }
					, command:
					[
						{ text: '주문조회 ' , imageClass: "k-i-search", className: "f-grid-search k-margin5", iconClass: "k-icon"
							, click :
								function(e) {
									e.preventDefault();
									var tr = $(e.target).closest("tr");
									var data = this.dataItem(tr);
									fnOrderSearch(data.odid);
								}
							, visible :
								function(dataItem) {
									return dataItem.createStatus == "E";
								}
        				}
						,{ text: '무통장입금' , imageClass: "k-i-search", className: "f-grid-search k-margin5", iconClass: "k-icon",
							click:
								function(e) {
									e.preventDefault();
									var tr = $(e.target).closest("tr");
									var data = this.dataItem(tr);
									fnBankBook(data.orderPrice, data.odid, data.odPaymentMasterId);
								}
							, visible:
								function(dataItem) {
									return dataItem.createStatus == "W" || dataItem.createStatus == "C";
								}
						}
						,{ text: '신용카드' , imageClass: "k-i-search", className: "f-grid-search k-margin5", iconClass: "k-icon"
							, click :
								function(e) {
									e.preventDefault();
									var tr = $(e.target).closest("tr");
									var data = this.dataItem(tr);
									fnCardPay(data.orderPrice, data.odid, data.odPaymentMasterId);
								}
							, visible :
								function(dataItem) {
									return dataItem.createStatus == "W" || dataItem.createStatus == "C";
								}
        				}
						, { text: '삭제 ' , imageClass: "k-i-search", className: "f-grid-search k-margin5", iconClass: "k-icon"
							, click :
								function(e) {
									e.preventDefault();
									var grid = $("#itemGrid").data("kendoGrid");
									var tr = $(e.target).closest("tr");
									var rowIdx = $("tr", grid.tbody).index(tr);

									$("#clickIdx").val(rowIdx);

									fnDelete(rowIdx);
								}
							, visible :
								function(dataItem) {
									return dataItem.createStatus == "W" || dataItem.createStatus == "C";
								}
        				}
        			]
    			}

            ]
		};

		itemGrid = $('#itemGrid').initializeKendoGrid(itemGridOpt).cKendoGrid();

		itemGrid.bind("dataBound", function() {
            $('#pageTotalText').text( kendo.toString(itemGridDs._total, "n0") );
        });
	};

	//검색
	function fnSearch() {
		$("#orderCreateType").val($("input[name='orderCreateTypeRadio']:checked").val());
		$("#createStatus").val($("input[name='createStatusRadio']:checked").val());
		var data = $('#searchForm').formSerialize(true);

		const	_pageSize = itemGrid && itemGrid.dataSource ? itemGrid.dataSource.pageSize() : PAGE_SIZE;

		var query = {
			page : 1,
			pageSize : _pageSize,
			filterLength : fnSearchData(data).length,
			filter : {
				filters : fnSearchData(data)
			}
		};

		itemGridDs.query(query);
	};

	//주문조회
	function fnOrderSearch(odid) {
		let DataArray = fnOdIdArray();

        //주문조회 화면 주소 연결 예정
	};

	//odid 배열로 담기
	function fnOdIdArray() {
		let DataArray = [];
		let odidArray = odid.split(",");
        for(let i = 0; i < odidArray.length; i++){
            let orgData = {};
            orgData.oidi = odidArray[i];
            DataArray[i] = orgData;
        }
        return DataArray;
	};

	//무통장입금(가상계좌)
	function fnBankBook(orderPrice, odid, odPaymentMasterId) {
		$("#orderPrice").val(orderPrice);
		$("#odid").val(odid);
		$("#odPaymentMasterId").val(odPaymentMasterId);

		let param = $('#cardPayForm').formSerialize(true);
		param['odid'] = $("#odid").val();

		var url = "/admin/order/create/addBankBookOrderCreate";
		fnKendoMessage({
			type    : "confirm"
			, message : "무통장입금을 하시겠습니까?"
			, ok      : function(e){
					        fnAjax({
					            url     : url,
					            params  : param,
					            success : function( successData ){
					            	fnBizCallback("bankInsert", successData);
					            },
					            isAction : 'insert'
					        })
			}
			, cancel  : function(e){
			}
		});
	};

	//신용카드(비인증결제)
	function fnCardPay(orderPrice, odid, odPaymentMasterId){
		//$('#cardPayForm').formClear(true);
		//$("#orderPrice").val(orderPrice);
		//$("#odid").val(odid);
		//$("#odPaymentMasterId").val(odPaymentMasterId);
		//fnKendoInputPoup({id:"cardPayPopup", height:"350px", width:"700px", title:{ nullMsg :'신용카드(비인증결제)' } });

		var param  = {'orderPrice' : orderPrice, 'odid' : odid, 'odPaymentMasterId' : odPaymentMasterId };
		fnKendoPopup({
			id     : 'nonCardBankBookPopup',
			title  : '비인증신용카드결제',
			src    : '#/nonCardBankBookPopup',
			param  : param,
			width  : '500px',
			height : '550px',
			scrollable : "no",
			success: function( stMenuId, data ){
				fnSearch();
			}
		});
	};

	$("#addInfoSel").change(
		function() {
			if ($("#addInfoSel").val() == 'BIRTH') $("#validText").text('*생년월일 YYMMDD 숫자만 입력');
			else if ($("#addInfoSel").val() == 'BSNUM') $("#validText").text('*사업자번호 3188101744 숫자만 으로 입력');
			else $("#validText").text('');
		}
	);

	//신용카드 결제
	function fnCardSave() {
		if(fnValidationCheck() == false) return;

		let param = $('#cardPayForm').formSerialize(true);
		param['odid'] = $("#odid").val();
		var url = "/admin/order/create/addCardPayOrderCreate";
		fnKendoMessage({
			type    : "confirm"
			, message : "결제 하시겠습니까?"
			, ok      : function(e){
					        fnAjax({
					            url     : url,
					            params  : param,
					            success : function( successData ){
					            	fnBizCallback("cardInsert", successData);
					            },
					            isAction : 'insert'
					        })
			}
			, cancel  : function(e){
			}
		});
	};

	//신용카드 결제 필수체크
	function fnValidationCheck() {
		if ($("#cardNo").val() == "") {
			fnKendoMessage({message : "카드번호를 입력해야 합니다.", ok : function() { $("#cardNo").focus(); }});
            return false;
		} else {
	        if (!checkNum($("#cardNo"))) {
	        	fnKendoMessage({message : "카드번호는 숫자만 입력 가능합니다.", ok : function() { $("#cardNo").focus(); }});
	            return false;
	        }
		}

		if ($("#cardNumYy").val() == "") {
            fnKendoMessage({message : "유효기간 년도를 입력해야 합니다.", ok : function() { $("#cardNumYy").focus(); }});
            return false;
		} else {
	        if (!checkNum($("#cardNumYy"))) {
	        	fnKendoMessage({message : "유효기간 년도는 숫자만 입력 가능합니다.", ok : function() { $("#cardNumYy").focus(); }});
	            return false;
	        }
		}

		if ($("#cardNumMm").val() == "") {
            fnKendoMessage({message : "유효기간 월을 입력해야 합니다.", ok : function() { $("#cardNumMm").focus(); }});
            return false;
		} else {
	        if (!checkNum($("#cardNumMm"))) {
	        	fnKendoMessage({message : "유효기간 월은 숫자만 입력 가능합니다.", ok : function() { $("#cardNumMm").focus(); }});
	            return false;
	        }
		}

		if ($("#addInfoSel").val() == "") {
            fnKendoMessage({message : "부가서비스 구분을 선택 하세요.", ok : function() { $("#addInfoSel").focus(); }});
            return false;
		}

		if ($("#addInfoVal").val() == "") {
            fnKendoMessage({message : "부가서비스 주민번호 또는 사업자번호를 입력해야 합니다.", ok : function() { $("#addInfoVal").focus(); }});
            return false;
		} else {
	        if (!checkNum($("#addInfoVal"))) {
	        	fnKendoMessage({message : "부가서비스는 숫자만 입력 가능합니다.", ok : function() { $("#addInfoVal").focus(); }});
	            return false;
	        }
		}

		if ($("#cardPass").val() == "") {
            fnKendoMessage({message : "비밀번호 앞자리 2자리를 입력해야 합니다.", ok : function() { $("#cardPass").focus(); }});
            return false;
		} else {
	        if (!checkNum($("#cardPass"))) {
	        	fnKendoMessage({message : "비밀번호는 숫자만 입력 가능합니다.", ok : function() { $("#cardPass").focus(); }});
	            return false;
	        }
		}

		if ($("#planPeriod").val() == "") {
            fnKendoMessage({message : "할부기간을 선택 하세요.", ok : function() { $("#planPeriod").focus(); }});
            return false;
		}
	};

	// 팝업창 닫기
	function fnPopUpClose(){
		var kendoWindow =$('#cardPayPopup').data('kendoWindow');
		kendoWindow.close();
	};

	//삭제
	function fnDelete(idx) {
		var gridDataSource = $("#itemGrid").data("kendoGrid").dataSource;
		var data = gridDataSource.view()[idx];

		$("#odCreateInfoId").val(data.odCreateInfoId);
		$("#odid").val(data.odid);

		let param = $('#searchForm').formSerialize(true);
		var url = "/admin/order/create/deleteOrderCreate";

		fnKendoMessage({
			type    : "confirm"
			, message : "삭제 하시겠습니까?"
			, ok      : function(e){
					        fnAjax({
					            url     : url,
					            params  : param,
					            success : function( successData ){
					            	fnBizCallback("delete", successData);
					            },
					            isAction : 'delete'
					        })
			}
			, cancel  : function(e){
			}
		});
	};

	//콜백 함수
    function fnBizCallback(id, data) {
        switch(id){
            case 'delete':
            	var result = data.orderRegistrationResult;
            	if (result == 'SUCCESS') {
            		result = "삭제 되었습니다.";
            		fnKendoMessage({
            			message : result
            			, ok : function (e) {
            				fnSearch();
					    }
            		});
            	}
                break;
            case 'cardInsert':
            	var result = data.result;
            	if (result == 'SUCCESS') {
            		result = "결제 되었습니다.";
            	} else {
            		result = "아래와 같은 사유로 결제가 실패 되었습니다.<br/>" + data.message;
            	}
        		fnKendoMessage({
        			message : result
        			, ok : function (e) {
        				fnSearch();
        				fnPopUpClose();
				    }
        		});
            case 'bankInsert':
            	var result = data.result;
            	if (result == 'SUCCESS') {
            		result = "가상계좌가 채번 되었습니다.";
            	} else {
            		result = "아래와 같은 사유로 결제가 실패 되었습니다.<br/>" + data.message;
            	}
        		fnKendoMessage({
        			message : result
        			, ok : function (e) {
        				fnSearch();
        				fnPopUpClose();
				    }
        		});
        }
    };

	function fnClear() {
		$('#searchForm').formClear(true);
	};

	function fnTest() {
		let claimData= {};

		claimData.odOrderId = '21148';			//주문 PK
		claimData.goodsChange = 1;				//조회구분 (전체 : 0, 상품갯수변경 : 1)

		var goodSearchList = new Array();
		let goodOrgData = {};

		goodOrgData.odOrderDetlId = '21176';
		goodOrgData.claimCnt = 1;
		goodOrgData.cancelCnt = 4;
		goodOrgData.orderCnt = 10;
		goodSearchList.push(goodOrgData);

		goodOrgData = {};
		goodOrgData.odOrderDetlId = '21177';
		goodOrgData.claimCnt = 2;
		goodOrgData.cancelCnt = 6;
		goodOrgData.orderCnt = 0;
		goodSearchList.push(goodOrgData);

		goodOrgData = {};
		goodOrgData.odOrderDetlId = '21178';
		goodOrgData.claimCnt = 3;
		goodOrgData.cancelCnt = 7;
		goodOrgData.orderCnt = 1;
		goodSearchList.push(goodOrgData);

		goodOrgData = {};
		goodOrgData.odOrderDetlId = '21179';
		goodOrgData.claimCnt = 4;
		goodOrgData.cancelCnt = 8;
		goodOrgData.orderCnt = 1;
		goodSearchList.push(goodOrgData);

		goodOrgData = {};
		goodOrgData.odOrderDetlId = '21180';
		goodOrgData.claimCnt = 5;
		goodOrgData.cancelCnt = 9;
		goodOrgData.orderCnt = 1;
		goodSearchList.push(goodOrgData);

		claimData.goodSearchList = goodSearchList;
	    fnAjax({
            //url     : "/admin/order/claim/addOrderClaimRegister",
	    	url     : "/order/claim/getOrderCanCelList",
            params  : claimData,
            contentType: "application/json",
			success : function( data ){
				console.log('성공');
            },
			error : function(xhr, status, strError) {
				console.log('오류');
				fnKendoMessage({
					//message : xhr.responseText
					message : '오류가 발생하였습니다. 관리자에게 문의해 주세요.'
				});
			},
            isAction : "insert"
        })
	};

	$scope.fnSearch = function() { fnSearch(); };
	$scope.fnClear = function() { fnClear(); };
	$scope.fnCardSave = function() { fnCardSave(); }
	$scope.fnTest = function() { fnTest(); }

});
