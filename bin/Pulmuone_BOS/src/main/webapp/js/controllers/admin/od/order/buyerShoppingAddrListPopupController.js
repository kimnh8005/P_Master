/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 배송지 목록
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.04.09		이원호          최초생성
 *
**/
"use strict";

var paramData = parent.POP_PARAM['parameter'];

$(document).ready(function() {

	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({
			PG_ID  : "buyerShoppingAddrListPopup",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){
		fnInitButton();
		fnInitOptionBox();
		initInputFormMain();
	};

	//--------------------------------- Button Start---------------------------------
	// 버튼 초기화
	function fnInitButton(){
		$("#fnSelect").kendoButton();
	};
	//--------------------------------- Button End-----------------------------------

	//------------------------------- Grid Start -------------------------------
	//수정
	function initInputFormMain(){
		//비회원인 경우 배송지목록 탭 비노출
		if(paramData.urUserId == 0 ) {
			$('#startShippingListArea').hide();
		} else {
			initShippingZoneList();
		}
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

		for(let i=0; i<rows.length; i++){
			tplObj = $(tpl);
			tplObj.find(".shippingList__userName").html(rows[i].receiverName).attr("id", "shippingList__userName__"+rows[i].urShippingAddrId);
			tplObj.find(".shippingList__zipCode").html(rows[i].receiverZipCode).attr("id", "shippingList__zipCode__"+rows[i].urShippingAddrId);
			tplObj.find(".shippingList__detailAddr").html(rows[i].receiverAddress).attr("id", "shippingList__detailAddr__"+rows[i].urShippingAddrId);
			tplObj.find(".shippingList__detailAddr1").html(rows[i].receiverAddress1).attr("id", "shippingList__detailAddr1__"+rows[i].urShippingAddrId);
			tplObj.find(".shippingList__detailAddr2").html(rows[i].receiverAddress2).attr("id", "shippingList__detailAddr2__"+rows[i].urShippingAddrId);
			tplObj.find(".shippingList__phoneNum").html(rows[i].receiverMobile).attr("id", "shippingList__phoneNum__"+rows[i].urShippingAddrId);
			tplObj.find(".shippingList__buildingCode").html(rows[i].buildingCode).attr("id", "shippingList__buildingCode__"+rows[i].urShippingAddrId);
			tplObj.find(".shipInfo__content__shipping").html(rows[i].shippingCmnt).attr("id", "shipInfo__content__shipping__"+rows[i].urShippingAddrId);
			tplObj.find(".dawnDelivery").attr("id", "dawnDelivery__"+rows[i].urShippingAddrId);
			tplObj.find(".dailyDelivery").attr("id", "dailyDelivery__"+rows[i].urShippingAddrId);
			tplObj.find(".storeDelivery").attr("id", "storeDelivery__"+rows[i].urShippingAddrId);
			tplObj.find(".shippingCompDelivery").attr("id", "shippingCompDelivery__"+rows[i].urShippingAddrId);

			tplObj.find("#select").attr("onClick", "$scope.fnSelect("+rows[i].urShippingAddrId+")");

			//'기본 배송지'인 경우
			if(rows[i].defaultYn == 'Y'){
				tplObj.find(".shippingList__isDefaultLocation.show").html("[기본 배송지]");
			} else {
				tplObj.find(".shippingList__isDefaultLocation.show").html("");
			}

			//'공동현관 비밀번호 입력'인 경우
			if(rows[i].accessInformationType == 'ACCESS_INFORMATION.FRONT_DOOR_PASSWORD' || 'ACCESS_INFORMATION.ETC'){
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

		//배송가능주소 아이콘 노출
		for(let i=0; i<data.rows.length; i++){
			if(data.rows[i].delivery.dawnDelivery == true){//새벽배송가능
					$("#dawnDelivery__"+data.rows[i].urShippingAddrId).show();
			} else {
				$("#dawnDelivery__"+data.rows[i].urShippingAddrId).hide();
			}

			if(data.rows[i].delivery.dailyDelivery == true){ //일일배송가능여부
				$("#dailyDelivery__"+data.rows[i].urShippingAddrId).show();
			} else {
				$("#dailyDelivery__"+data.rows[i].urShippingAddrId).hide();
			}

			if(data.rows[i].delivery.storeDelivery == true){ //매장배송가능여부
				$("#storeDelivery__"+data.rows[i].urShippingAddrId).show();
				$("#storeDelivery__").val("매장배송 "+data.rows[i].delivery.storeName);
			} else {
				$("#storeDelivery__"+data.rows[i].urShippingAddrId).hide();
			}

			if(data.rows[i].delivery.shippingCompDelivery == true){ //택배배송가능여부
				if(data.rows[i].delivery.dawnDelivery == true){
					$("#dawnDelivery__"+data.rows[i].urShippingAddrId).show();
					$("#shippingCompDelivery__"+data.rows[i].urShippingAddrId).hide();
				} else {
					$("#shippingCompDelivery__"+data.rows[i].urShippingAddrId).show();
				}
			} else {
				$("#shippingCompDelivery__"+data.rows[i].urShippingAddrId).hide();
			}
		}
	}

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	// 옵션 초기화
	function fnInitOptionBox() {

	};

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ---------------------------------
    // 배송지목록 > 선택버튼
    function fnSelect(id){

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
	    			urShippingAddrId : id,
	    			receiverName : receiverName,
	    			receiverMobile:receiverMobile,
	    			receiverZipCode: receiverZipCode,
	    			receiverAddress1:receiverAddress1,
	    			receiverAddress2:receiverAddress2,
	    			buildingCode: buildingCode,
	    			shippingComment:shippingComment,
	    			accessInformationType:accessInformationType,
	    			accessInformationPassword:accessInformationPassword
    				}

        parent.POP_PARAM = data;
        fnClose();
    }

    //닫기
    function fnClose(){
        parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
    };

	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	$scope.fnClose = function(){ fnClose(); };
	$scope.fnSelect = function(id) { fnSelect(id);};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	//------------------------------- Validation End -------------------------------------
}); // document ready - END
