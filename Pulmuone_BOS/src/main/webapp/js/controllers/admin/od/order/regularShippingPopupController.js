/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 수취 정보 변경
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.15		김승우          최초생성
 * @ 2021.02.02		최윤지			추가작성
 * **/
"use strict";

var paramData = parent.POP_PARAM['parameter'];

var deliveryDtPicker = null;
var dayOff = [];

$(document).ready(function() {
	importScript("/js/service/od/order/orderCommSearch.js");
	importScript("/js/service/od/order/regularReqPopup.js");

	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({
			PG_ID  : "regularShippingPopup",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){
		fnInitButton();
		fnInitOptionBox();
		initInputFormMain();
		initShippingZoneList();
		bindEvents();
		fbTabChange();
	};

	//--------------------------------- Button Start---------------------------------
	// 버튼 초기화
	function fnInitButton(){
		$("#fnSave, #fnSelect").kendoButton();
	};
	//--------------------------------- Button End-----------------------------------

	//------------------------------- Grid Start -------------------------------
	// 데이터 Set
	function initInputFormMain(){

		$('#deliveryMsgCd').data("kendoDropDownList").enable( true );
		$('#phonePrefix').data("kendoDropDownList").enable( true ); //phonePrefix

		fnAjax({
			url     : '/admin/order/getOrderRegularReqDetailShippingZonePopup',
			params  : {'odRegularReqId' : paramData.odRegularReqId},
			async	: false,
			success :
				function( data ){
					var recvHp = fnNvl(data.recvHp).replace(/-/g, '');
					if(recvHp.length > 0) {
						let hp1 = recvHp.substring(3, (recvHp.length - 4));
						let hp2 = recvHp.substring(recvHp.length - 4);
						$('#recvHp1').val(hp1);
						$('#recvHp2').val(hp2);
					}
					//데이터 넣기
					$('#recvNm').val(data.recvNm);
					$('#recvZipCd').val(data.recvZipCd);
					$('#recvAddr1').val(data.recvAddr1);
					$('#recvAddr2').val(data.recvAddr2);
					$('#deliveryMsg').val(data.deliveryMsg);
					$('#doorMsgCd').val(data.doorMsgCd);
					$('#doorEtc').val(data.doorEtc);
					$('#doorMsg').val(data.doorMsg);
				},
			isAction : 'select'
		});
	}

	// 배송지 목록 리스트 조회
	function initShippingZoneList(){
		fnAjax({
	        url     : '/admin/order/getShippingZoneList',
	        params  : {urUserId : paramData.urUserId},
	        success :
	            function( data ){
	        		shippingList(data); // 배송지 목록
		        }
	    });
	};

	// 배송지 목록
	function shippingList(data){

		const $target = $("#startShippingListArea .shippingList__list");
		$target.empty();

		let tpl = $('#shippingListItem').html();
		let tplObj = null;

		// 배송지 목록 데이터
		let rows = data.rows;

		if(rows.length < 1) {
			let noneTpl = $('#shippingListItemNone').html();
			$target.append(noneTpl);
			return false;
		}

		for(let i=0; i<rows.length; i++){

			let delivery = data.rows[i].delivery;

			tplObj = $(tpl);
			tplObj.find(".shippingList__userName").html(rows[i].receiverName).attr("id", "shippingList__userName__"+rows[i].urShippingAddrId);
			tplObj.find(".shippingList__zipCode").html("["+rows[i].receiverZipCode+"]").attr("id", "shippingList__zipCode__"+rows[i].urShippingAddrId);
			tplObj.find(".shippingList__detailAddr").html(rows[i].receiverAddress).attr("id", "shippingList__detailAddr__"+rows[i].urShippingAddrId);
			tplObj.find(".shippingList__detailAddr1").html(rows[i].receiverAddress1).attr("id", "shippingList__detailAddr1__"+rows[i].urShippingAddrId);
			tplObj.find(".shippingList__detailAddr2").html(rows[i].receiverAddress2).attr("id", "shippingList__detailAddr2__"+rows[i].urShippingAddrId);
			tplObj.find(".shippingList__phoneNum").html(rows[i].receiverMobile).attr("id", "shippingList__phoneNum__"+rows[i].urShippingAddrId);
			tplObj.find(".shippingList__buildingCode").html(rows[i].buildingCode).attr("id", "shippingList__buildingCode__"+rows[i].urShippingAddrId);
			tplObj.find(".shipInfo__content__shipping").html(rows[i].shippingCmnt).attr("id", "shipInfo__content__shipping__"+rows[i].urShippingAddrId);

			tplObj.find("#select").attr("onClick", "$scope.fnSelect("+rows[i].urShippingAddrId+")");

			//'기본 배송지'인 경우
			if(rows[i].defaultYn == 'Y'){
				tplObj.find(".shippingList__isDefaultLocation.show").html("[기본 배송지]");
			} else {
				tplObj.find(".shippingList__isDefaultLocation.show").html("");
			}

			//'공동현관 비밀번호 입력'인 경우
			if(rows[i].accessInformationType == 'ACCESS_INFORMATION.FRONT_DOOR_PASSWORD'){
				tplObj.find(".shipInfo__content").html(rows[i].accessInformationTypeName).attr("id", "shipInfo__content__"+rows[i].urShippingAddrId);
				tplObj.find(".shipInfo__type").html(rows[i].accessInformationType).attr("id", "shipInfo__type__"+rows[i].urShippingAddrId);
				tplObj.find(".shipInfo__password").html(rows[i].accessInformationPassword).attr("id", "shipInfo__password__"+rows[i].urShippingAddrId);
			} else {
				tplObj.find(".shipInfo__content").html(rows[i].accessInformationTypeName).attr("id", "shipInfo__content__"+rows[i].urShippingAddrId);
				tplObj.find(".shipInfo__type").html(rows[i].accessInformationType).attr("id", "shipInfo__type__"+rows[i].urShippingAddrId);
				tplObj.find(".shipInfo__password").html("").attr("id", "shipInfo__password__"+rows[i].urShippingAddrId);
			}

			$target.append(tplObj);
		}
	}

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	// 옵션 초기화
	function fnInitOptionBox() {
		//핸드폰 앞자리 선택
		regularReqPopupListUtil.phonePrefix("phonePrefix", paramData.phonePrefix);
		//배송요청사항 공통코드 (드롭다운리스트)
		regularReqPopupListUtil.deliveryMsgCd("deliveryMsgCd", paramData.deliveryMsgCd);
		//배송출입정보 공통코드 (라디오버튼)
		regularReqPopupListUtil.doorMsgCd("doorMsgCd", paramData.doorMsgCd);
	};

	//주소찾기 팝업
	function fnAddressPopup() { //우편번호, 주소1, 주소2(상세주소)
		fnDaumPostcode("recvZipCd", "recvAddr1", "recvAddr2","");
	};

	//이벤트 바인딩
	function bindEvents() {
		$("#recvHp1").on("keyup", function(e) {
			var self = $(this);
			if( self.val().trim().length === 4 ) {
				$("#recvHp2").focus();
			}
		})
	};

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ---------------------------------
    // 배송지목록 > 선택버튼
    function fnSelect(id){

		var odRegularReqShippingZoneId = paramData.odRegularReqShippingZoneId;
		var odRegularReqId = paramData.odRegularReqId;

    	var receiverName = $("#shippingList__userName__"+id).text();
    	var receiverMobile = $("#shippingList__phoneNum__"+id).text();
    	var receiverZipCode = $("#shippingList__zipCode__"+id).text();
    	var receiverAddress1 = $("#shippingList__detailAddr1__"+id).text();
    	var receiverAddress2 = $("#shippingList__detailAddr2__"+id).text();
    	var buildingCode = $("#shippingList__buildingCode__"+id).text();
    	var shippingComment = $("#shipInfo__content__shipping__"+id).text();
    	var accessInformationType = $("#shipInfo__type__"+id).text();
    	var accessInformationPassword = $("#shipInfo__password__"+id).text();

    	let data = {
    				odRegularReqShippingZoneId : odRegularReqShippingZoneId,
    				odRegularReqId : odRegularReqId,
	    			urShippingAddrId : id,
	    			urUserId : paramData.urUserId,
	    			recvNm : receiverName,
	    			recvHp:receiverMobile,
	    			recvZipCd: receiverZipCode,
	    			recvAddr1:receiverAddress1,
	    			recvAddr2:receiverAddress2,
	    			recvBldNo: buildingCode,
	    			deliveryMsg:shippingComment,
	    			doorMsgCd:accessInformationType,
	    			doorMsg:accessInformationPassword
		};

		fnKendoMessage({message : '선택한 주소로 변경하시겠습니까?', type : "confirm", ok : function(){
            fnAjax({
                url     : '/admin/order/putOrderRegularReqShippingZone',
                params	: data,
                success :
                    function( data ){
	                	fnKendoMessage({
	    					message:"변경되었습니다.",
	    					ok:function(){
	    						fnClose();
	    					}
	    				});
                    },
                isAction : 'update'
            });
        }});
    }

    //닫기
    function fnClose(){
        parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
    };

    //저장
    function fnSave() {

 		var url  = '/admin/order/putOrderRegularReqShippingZone'; // 수정팝업 > 저장 url
		var data = $('#inputFormMain').formSerialize(true);

		if( data.rtnValid ){
			var recvHp = data["phonePrefix"] + data.recvHp1 + data.recvHp2;
			delete data["phonePrefix"];
			delete data.recvHp1;
			delete data.recvHp2;
			data.recvHp = recvHp;

			var odRegularReqShippingZoneId = paramData.odRegularReqShippingZoneId;
			var odRegularReqId = paramData.odRegularReqId;

			data['odRegularReqShippingZoneId'] = odRegularReqShippingZoneId;
			data['odRegularReqId'] = odRegularReqId;

			if(data.doorMsgCd == "ACCESS_INFORMATION.FRONT_DOOR_PASSWORD") { //공동현관 비밀번호
				delete data.doorEtc;
				delete data.doorEtc;
			} else if(data.doorMsgCd == "ACCESS_INFORMATION.ETC"){ // 기타
				delete data.doorMsg;
			} else {
				delete data.doorEtc;
				delete data.doorMsg;
			}

			if(data.deliveryMsgCd != 'DELIVERY_MSG_TYPE.INPUT') {
				data.deliveryMsg = '';
			}

			fnAjax({
                url     : url,
                params  : data,
                success :
                    function( data ){

	                	fnKendoMessage({
	    					message:"변경되었습니다.",
	    					ok:function(){
	    						fnClose();
	    					}
	    				});
                    },
                    isAction : 'batch'
            });
		}
	}
	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	$scope.fnSave = function( ) {	fnSave();	};
	$scope.fnClose = function(){ fnClose(); };
	$scope.fnAddressPopup = function() { fnAddressPopup(); };
	$scope.fnSelect = function(id) { fnSelect(id);};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------
	fnInputValidationForNumber("phone1");
	fnInputValidationForNumber("phone2");
	//------------------------------- Validation End -------------------------------------
}); // document ready - END
