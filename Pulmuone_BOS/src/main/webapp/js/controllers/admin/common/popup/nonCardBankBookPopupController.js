/**-----------------------------------------------------------------------------
 * description 		 : 비인증 신용카드 추가결제 팝업
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.13		강상국          최초생성
 * @
 * **/
'use strict';
var pageParam = fnGetPageParam(); // GET 방식으로 전달된 parameter 객체
var paramData = parent.POP_PARAM["parameter"]; // 파라미터

var PAGE_SIZE = 20;
var itemGridOpt, itemGrid, itemGridDs;

$(document).ready(function() {

	// Initialize Page Call
	fnInitialize();

	// Initialize PageR
	function fnInitialize() {
        $scope.$emit("fnIsMenu", { flag : false });

        fnPageInfo({
            PG_ID  : "nonCardBankBookClaimPopup",
            callback : fnUI
        });

		$("#orderPrice").val(paramData.orderPrice);
		$("#odid").val(paramData.odid);
		$("#odPaymentMasterId").val(paramData.odPaymentMasterId);


	};

	//전체화면 구성
	function fnUI() {
		fnTranslate();		// comm.lang.js 안에 있는 공통함수 다국어
		fnInitButton();		// Initialize Button
		fnInitCompont();	// 부가정보, 할부기간

		setTimeout(function(){
			$("#addInfoVal").val("");
			$("#cardPass").val("");
		}, 10)
	}

	function fnInitButton() {
		$('#fnCardSave').kendoButton();
	};

	function fnInitCompont() {
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
/*
		$('#cardPayPopup').kendoWindow({
			visible: false,
			modal: true
		});
*/
	}

	$("#addInfoSel").change(
		function() {
			if ($("#addInfoSel").val() == 'BIRTH') { $("#addInfoVal").val(''); $("#validText").text('*생년월일 YYMMDD 숫자만 입력'); }
			else if ($("#addInfoSel").val() == 'BSNUM') { $("#addInfoVal").val(''); $("#validText").text('*사업자번호 3188101744 숫자만 으로 입력'); }
			else $("#validText").text('');
		}
	);

	//신용카드 결제
	function fnCardSave() {
		if(fnValidationCheck() == false) return;

		// 결제버튼 비활성화
		$("#fnCardSave").attr('disabled',true);

		let param = $('#cardPayForm').formSerialize(true);
		param['odid'] = paramData.odid;
		// var claimUrl 	= "/admin/order/claim/addCardPayOrderCreate";s
		var createUrl 	= "/admin/order/create/cardPayOrderCreate";

		fnKendoMessage({
			type    : "confirm"
			, message : "결제 하시겠습니까?"
			, ok      : function(e){
					        fnAjax({
					            url     : createUrl,//fnNvl(paramData.type) ==	'CREATE' ? createUrl : claimUrl,
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

		if ($("#planPeriod").val() == "") {
            fnKendoMessage({message : "할부기간을 선택 하세요.", ok : function() { $("#planPeriod").focus(); }});
            return false;
		}
	};


	//콜백 함수
    function fnBizCallback(id, data) {
        switch(id){
            case 'cardInsert':
            	var result = data.result;

            	let resultObj = new Object();

            	resultObj.result = data;
            	if (result == 'SUCCESS') {
            		result = "결제 되었습니다.";
//            		fnSave(data.inicisNonAuthenticationCartPay); //(카드)
            	} else {
					result = data.message;
            	}
            	parent.POP_PARAM = resultObj;
        		fnKendoMessage({
        			message : result
        			, ok : function (e) {
        				//parent.fnSearch();
        				parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
				    }
        		});
        }
    };

	$scope.fnCardSave = function() { fnCardSave(); }

});
