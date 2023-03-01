// 정기배송 신청 상세 팝업 util
var makePhonePrefix = function() {
  var data = [];

  for( var i = 0; i < 10; i++ ) {
    data.push({
      CODE : "01" + i,
      NAME : "01" + i,
    });
  }

  return data;
}

// FIXME
var regularReqPopupListUtil = {

	//휴대폰번호
	phonePrefix: function(id, paramPhonePrefix) {
	    return fnKendoDropDownList({
				id: id,
				data: makePhonePrefix(),
				valueField : "CODE",
	            textField : "NAME",
	            value: paramPhonePrefix,
				blank: "선택",
			})
	  }
	,
	// 배송요청사항
	deliveryMsgCd: function(id, paramDeliveryMsgCd) {

	    var dropdownList = fnKendoDropDownList({
			id  : 'deliveryMsgCd',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "DELIVERY_MSG_TYPE", "useYn" :"Y"},
			textField :"NAME",
			valueField : "CODE",
			value : paramDeliveryMsgCd,
			blank : "선택해주세요.",
		});

	    dropdownList.bind('dataBound', function(e){
	    	var deliveryMsg = parent.POP_PARAM.parameter.deliveryMsg;

	    	const target = this.value();
	    	const targetElement = e.sender.element;
	    	const $targetDetailLabel = targetElement.closest('.form__element').find('.deliveryMsgDiv');

	    	if(target == 'DELIVERY_MSG_TYPE.INPUT') {
	    		//$('#deliveryMsg').val(deliveryMsg);
	    		$targetDetailLabel.show();
	    	} else {
	    		$targetDetailLabel.hide();
	    	}
	    });

	    dropdownList.bind('change', function(e){
			const value = this.value();
			const element = e.sender.element;
			const $datailLabel = element.closest('.form__element').find('.deliveryMsgDiv');

			//배송요청사항 > 직접입력 클릭 시
			if(value == 'DELIVERY_MSG_TYPE.INPUT'){
				$datailLabel.show();
				$datailLabel.find("input").focus();
			} else {
				$datailLabel.val('').hide();
			}
		});
	  }
	,
	//배송 출입 정보
	doorMsgCd: function(id, paramDoorMsgCd) {

		return fnTagMkRadio({
			id: "doorMsgCd" ,
			tagId : 'doorMsgCd',
			url : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "ACCESS_INFORMATION", "useYn" :"Y"},
			textField :"NAME",
			valueField : "CODE",
			async : false,
			chkVal: paramDoorMsgCd,
			style : {},
			success: function() {
				var doorMsg = parent.POP_PARAM.parameter.doorMsg;
				var doorEtc = parent.POP_PARAM.parameter.doorEtc;

				//공동현관 비밀번호
				var $doorMsgInput = $('<input id="doorMsg" name="doorMsg" type="text" class="comm-input width100 entrance__detail" style="margin-top: 15px;" placeholder="공동현관 비밀번호는 배송을 위한 출입 목적으로만 사용합니다.">');
				var $doorMsgLabel = $('#doorMsgCd_1').closest('label');
				//기타
				var $etcInput = $('<input id="doorEtc" name="doorEtc" type="text" class="comm-input width100 entrance__detail" style="margin-top: 15px;">');
				var $etcLabel = $('#doorMsgCd_4').closest('label');

				$doorMsgLabel.append($doorMsgInput);
				$etcLabel.append($etcInput);

				if(paramDoorMsgCd == 'ACCESS_INFORMATION.FRONT_DOOR_PASSWORD'){
					$('#doorMsg').val(doorMsg);
					$doorMsgInput.show();
					$etcInput.hide();
				} else if(paramDoorMsgCd == 'ACCESS_INFORMATION.ETC'){
					$('#doorEtc').val(doorEtc);
					$etcInput.show();
					$doorMsgInput.hide();
				} else {
					$doorMsgInput.hide();
					$etcInput.hide();
				}
			},
			change: function(e) {
				const value = e.target.value;

				//공동현관 비밀번호
				if( value == 'ACCESS_INFORMATION.FRONT_DOOR_PASSWORD' ) {
					$('#doorMsg').show();
					$('#doorEtc').hide();
				} else if ( value == 'ACCESS_INFORMATION.ETC' ) {
					$('#doorMsg').hide();
					$('#doorEtc').show();
				} else {
					$('#doorEtc, #doorMsg').hide();
				}

			}
		});
		}
}