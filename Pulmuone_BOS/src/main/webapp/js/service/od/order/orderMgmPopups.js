// 주문 상세 팝업 util
var orderPopupUtil = {
  // fnkendoinputpoup options
  options: {
	  nonCardBankBookPopup: {
			id     : 'nonCardBankBookPopup',
			title  : '비인증신용카드결제',
			src    : '#/nonCardBankBookPopup',
			param  : null,
			width  : '500px',
			height : '550px',
			scrollable : "no",
			success: function(id, data){
				console.log(id, data);
			}
    },
    discountHistPopup: {
        id: "discountHistPopup",
        title: "즉시 할인 내역",
        src: "#/discountHistPopup",
        param: null,
        width: "900px",
        height: "400px",
        minWidth: 500,
        success: function(id, data) {
          console.log(id, data);
        }
      },
      buyerPopup: {
          id: 'buyerPopup',
          title: '회원상세',
          param: null,
          src: '#/buyerPopup',
          width: '1200px',
          height: '640px',
          success: function(id, data) {
        	  console.log(id, data);
          }
      },
      scheduleMgmPopup: {
          id: "scheduleMgmPopup",
          title: "스케줄 관리",
          src: "#/scheduleMgmPopup",
          param: null,
          width: '600px',
		  height: '850px',
          activate: function(args) {
            console.log(args);

            var elem = args.sender.element;
            var iframe = elem.find("iframe")[0];

            var iframeDocument = iframe.contentWindow.document;
            var iframeBody = iframeDocument.body;
            iframeBody.id = "scheduleMgmPopup__body";
          },
          success: function(id, data) {
            console.log(id, data);
          }
      },
      claimReasonMsgPopup: {
          id: 'claimReasonMsgPopup',
          title: '클레임사유 첨부파일',
          param: null,
          src: '#/claimReasonMsgPopup',
          width: '745px',
          height: '650px',
          success: function(id, data) {
        	  console.log(id, data);
          }
      },
      deliveryInfoPopup: {
        id: "deliveryInfoPopup",
        title: "배송정보",
        src: "#/deliveryInfoPopup",
        param: null,
        width: "500px",
        height: "400px",
        minWidth: 500,
        success: function(id, data) {
          console.log(id, data);
        }
      },
      deliveryDayChangePopup: {
        id: "deliveryDayChangePopup",
        title: "배송일정변경",
        src: "#/deliveryDayChangePopup",
        param: null,
        width: "500px",
        height: "450px",
        minWidth: 500,
        success: function(id, data) {
          console.log(id, data);
        }
      },
      interfaceDayChangePopup: {
        id: "interfaceDayChangePopup",
        title: "주문 I/F 변경",
        src: "#/interfaceDayChangePopup",
        param: null,
        width: "1200px",
        height: "350px",
        success: function(id, data) {
          console.log(id, data);
        }
      },
      deliverySearchPopup: {
        id: "deliverySearchPopup",
        title: "배송 추적",
        src: "#/deliverySearchPopup",
        param: null,
        width: "800",
        height: "900",
        minWidth: 700,
        success: function(id, data) {
          console.log(id, data);
        }
      },
      receiverMgmPopup: {
        id: "receiverMgmPopup",
        title: "수취정보 변경",
        src: "#/receiverMgmPopup",
        param: null,
        width: "600px",
        height: "850px",
        success: function(id, data) {
        	console.log(id, data);
        }
      },
      receiverHistPopup: {
        id: "receiverHistPopup",
        title: "배송정보 변경 내역",
        src: "#/receiverHistPopup",
        param: null,
        width: "1800px",
        height: "850px",
        success: function(id, data) {
          console.log(id, data);
        }
      },
      claimMgmPopup: {
        id: "claimMgmPopup",
        title: "클레임 상세",
        src: "#/claimMgmPopup",
        param: null,
        width: "1800px",
        height: "1500px",
        resizable: false,
        success: function(id, data) {
          console.log(id, data);
        }
      },
      orderGiftListPopup: {
          id: "orderGiftListPopup",
          title: "증정품 내역",
          src: "#/orderGiftListPopup",
          param: null,
          width: "500px",
          height: "400px",
          minWidth: 500,
          success: function(id, data) {
              console.log(id, data);
          }
      },
      csRefundAccountInfoPopup: {
          id: "csRefundAccountInfoPopup",
          title: "환불계좌정보",
          src: "#/csRefundAccountInfoPopup",
          param: null,
          width: "500px",
          height: "180px",
          minWidth: 500,
          success: function(id, data) {
              console.log(id, data);
          }
      },
      dailyGoodsOptionChangePopup: {
          id: "dailyGoodsOptionChangePopup",
          title: "옵션변경",
          src: "#/dailyGoodsOptionChangePopup",
          param: null,
          width: "600px",
          height: "400px",
          success: function(id, data) {
              console.log(id, data);
          }
      },
      additionalGoodsChangePopup: {
          id: "additionalGoodsChangePopup",
          title: "옵션변경",
          src: "#/additionalGoodsChangePopup",
          param: null,
          width: "500px",
          height: "200px",
          success: function(id, data) {
              console.log(id, data);
          }
      },
  },
  open: function(id, param, success) {
    const opt =  $.extend({}, this.options[id]);

    if( param ) {
      opt.param = param;
    }

    opt.success = success;

    fnKendoPopup(opt);
  }
}

var makePhonePrefix = function(paramPhonePrefix) {
  var data = [];
  for( var i = 0; i < 10; i++ ) {
    data.push({
      CODE : "01" + i,
      NAME : "01" + i
    });
  }
   data.push({
       CODE : paramPhonePrefix,
       NAME : paramPhonePrefix
   });

  data = [...new Set(data.map(JSON.stringify))].map(JSON.parse);
  return data;
}
var deliveryMsgList = function() {
    var data = [];

    data = [
        {"CODE":"부재 시 경비실에 맡겨주세요."		,"NAME":"부재 시 경비실에 맡겨주세요."},
        {"CODE":"부재 시 문 앞에 놓아주세요"		,"NAME":"부재 시 문 앞에 놓아주세요"},
        {"CODE":"부재 시 휴대폰으로 연락바랍니다."	,"NAME":"부재 시 휴대폰으로 연락바랍니다."},
        {"CODE":"배송 전 연락바랍니다."			,"NAME":"배송 전 연락바랍니다."},
        {"CODE":"직접입력"						,"NAME":"직접입력"}
    ];
    return data;
}
// FIXME
var orderPopupListUtil = {
//휴대폰번호
phonePrefix: function(id, paramPhonePrefix) {
    return fnKendoDropDownList({
			id: id,
			data: makePhonePrefix(paramPhonePrefix),
			valueField : "CODE",
            textField : "NAME",
            value: paramPhonePrefix,
			blank: "선택",
		})
  }
,
// 배송요청사항
deliveryMsg: function(paramDeliveryMsg) {
    var dropdownList = fnKendoDropDownList({
		id  : 'deliveryMsg',
		data  : deliveryMsgList(),
		textField :"NAME",
		valueField : "CODE",
		blank : "선택해주세요.",
	});
    dropdownList.bind('change',function(e){
		if($("#deliveryMsg").val() == '직접입력'){
            $("#deliveryMsgEtc").css("display","");
            if(paramDeliveryMsg != null){
                if (paramDeliveryMsg.indexOf(deliveryMsgList()[0].CODE) || paramDeliveryMsg.indexOf(deliveryMsgList()[1].CODE) || paramDeliveryMsg.indexOf(deliveryMsgList()[2].CODE)
                    || paramDeliveryMsg == deliveryMsgList()[3].CODE){
                    $("#deliveryMsgEtc").val('');
                } else {
                    $("#deliveryMsg").data('kendoDropDownList').value("직접입력");
                    $("#deliveryMsgEtc").css("display", "");
                    $("#deliveryMsgEtc").val(paramData.deliveryMsg);
                }
            } else {
                $("#deliveryMsgEtc").val('');
            }
		}else{
			 $("#deliveryMsgEtc").css("display","none");
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
			var $doorMsgInput = $('<input id="doorMsg" name="doorMsg" type="text" class="comm-input width100 entrance__detail" style="margin-top: 15px;" placeholder="공동현관 비밀번호는 배송을 위한 출입 목적으로만 사용합니다." maxlength= "50">');
			var $doorMsgLabel = $('#doorMsgCd_0').closest('label');
			//기타
			var $etcInput = $('<input id="doorEtc" name="doorEtc" type="text" class="comm-input width100 entrance__detail" style="margin-top: 15px;" maxlength= "50">');
			var $etcLabel = $('#doorMsgCd_4').closest('label');

			$doorMsgLabel.append($doorMsgInput);
			$etcLabel.append($etcInput);

			if(paramDoorMsgCd == 'ACCESS_INFORMATION.FRONT_DOOR_PASSWORD'){
				$('#doorMsg').val(doorMsg);
				$doorMsgInput.show();
				$etcInput.hide();
			} else if(paramDoorMsgCd == 'ACCESS_INFORMATION.ETC'){
				$('#doorEtc').val(doorMsg);
				$etcInput.show();
				$doorMsgInput.hide();
			} else {
				$doorMsgInput.hide();
				$etcInput.hide();
			}
		},
		change: function(e) {
		    if( $(e.target).attr('type') !== 'radio' ) {
		        e.stopPropagation();
                return false;
            }

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