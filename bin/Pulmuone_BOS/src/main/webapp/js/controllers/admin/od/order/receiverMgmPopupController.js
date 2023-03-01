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
var shippingListDeliveryDtPicker = null;
var dayOff = [];
$(document).ready(function() {

	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({
			PG_ID  : "receiverMgmPopup",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){
		fnInitButton();
		fnInitOptionBox();
		initInputFormMain();
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
	//function
	function initHitokSwitch() {
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
	}

	function phonePrefix(id, paramPhonePrefix) {
    return fnKendoDropDownList({
			id: id,
			data: makePhonePrefix(paramPhonePrefix),
			valueField : "CODE",
            textField : "NAME",
            value: paramPhonePrefix,
			blank: "선택",
		})
  	}

  	function makePhonePrefix(paramPhonePrefix) {
	  var data = [];
	  for( var i = 0; i < 10; i++ ) {
		data.push({
		  CODE : "01" + i,
		  NAME : "01" + i
		});
	  }
	  
	  //지역번호 추가
	  data.push(
		   		{"CODE":"02"		,"NAME":"02"},
				{"CODE":"031"		,"NAME":"031"},
				{"CODE":"032"		,"NAME":"032"},
				{"CODE":"033"		,"NAME":"033"},
				{"CODE":"041"		,"NAME":"041"},
				{"CODE":"042"		,"NAME":"042"},
				{"CODE":"043"		,"NAME":"043"},
				{"CODE":"044"		,"NAME":"044"},
				{"CODE":"051"		,"NAME":"051"},
				{"CODE":"052"		,"NAME":"052"},
				{"CODE":"053"		,"NAME":"053"},
				{"CODE":"054"		,"NAME":"054"},
				{"CODE":"055"		,"NAME":"055"},
				{"CODE":"061"		,"NAME":"061"},
				{"CODE":"062"		,"NAME":"062"},
				{"CODE":"063"		,"NAME":"063"},
				{"CODE":"064"		,"NAME":"064"}
	   );
	  
	   data.push({
		   CODE : paramPhonePrefix,
		   NAME : paramPhonePrefix
	   });

	  data = [...new Set(data.map(JSON.stringify))].map(JSON.parse);
	  return data;
	}

	//수정
	function initInputFormMain(){
		//배송가능유형조회
		fnShippingAddressPossibleDeliveryInfo(paramData.recvZipCd, paramData.recvBldNo);

		//일일배송-녹즙인경우, 적용일자 노출
		if(!initHitokSwitch() && paramData.deliveryTypeCode == 'DELIVERY_TYPE.DAILY' && paramData.goodsDailyType == 'GOODS_DAILY_TP.GREENJUICE'){
			//ajax 배송가능일자 리스트 가져오기
			fnAjax({
		        url     : '/admin/order/schedule/getOrderDeliverableScheduleAddressList',
		        params  : {odOrderDetlId : paramData.odOrderDetlId},
		        success :
		            function( data ){

		        		let dayOn = data.scheduleDelvDateList;
		        		$('#deliveryDtPicker').val(data.scheduleDelvDateList[0]);
		        		pickerDate(dayOn);
			        }
		    });
			$('#deliveryDtDiv').show();
		} else {
			$('#deliveryDtDiv').hide();
			}

		let deliveryMsg = paramData.deliveryMsg;
		let deliveryMsgArr =
			["부재 시 경비실에 맡겨주세요.", "부재 시 문 앞에 놓아주세요", "부재 시 휴대폰으로 연락바랍니다.", "배송 전 연락바랍니다.", "직접입력"];

		if(deliveryMsg != null && deliveryMsg != "") {
			$("#deliveryMsgEtc").css("display", "none");

				if (deliveryMsg.indexOf(deliveryMsgArr[0]) == 0) {
					$("#deliveryMsg").data('kendoDropDownList').value(deliveryMsgArr[0]);
				} else if (deliveryMsg.indexOf(deliveryMsgArr[1]) == 0) {
					$("#deliveryMsg").data('kendoDropDownList').value(deliveryMsgArr[1]);
				}else if (deliveryMsg.indexOf(deliveryMsgArr[2]) == 0) {
					$("#deliveryMsg").data('kendoDropDownList').value(deliveryMsgArr[2]);
				}else if (deliveryMsg.indexOf(deliveryMsgArr[3]) == 0) {
					$("#deliveryMsg").data('kendoDropDownList').value(deliveryMsgArr[3]);
				}else {
					$("#deliveryMsg").data('kendoDropDownList').value("직접입력");
					$("#deliveryMsgEtc").css("display", "");
					$("#deliveryMsgEtc").val(paramData.deliveryMsg);
				}
		} else {
			$('#deliveryMsg').data("kendoDropDownList").enable( true );
		}

		//$('#deliveryMsg').data("kendoDropDownList").enable( true );

		//데이터 넣기

		$('#recvNm').val(paramData.orgRecvNm);
		let recvHp = paramData.orgRecvHp.replace(/\-/g,'') == "" ? "01000000000" : paramData.orgRecvHp.replace(/\-/g,''); // 휴대폰번호 하이픈(-) 제거
		if(recvHp.length < 12) {
			if(recvHp.indexOf('02') == 0) {
				phonePrefix("phonePrefix", recvHp.slice(0,2));
				$('#phonePrefix').data("kendoDropDownList").enable( true ); //phonePrefix
				$('#recvHp1').val(recvHp.slice(2,(recvHp.length)-4));
				$('#recvHp2').val(recvHp.slice((recvHp.length)-4,recvHp.length));
			} else {
				phonePrefix("phonePrefix", recvHp.slice(0,3));
				$('#phonePrefix').data("kendoDropDownList").enable( true ); //phonePrefix
				$('#recvHp1').val(recvHp.slice(3,(recvHp.length)-4));
				$('#recvHp2').val(recvHp.slice((recvHp.length)-4,recvHp.length));
			}

		} else if(12 <= recvHp.length){
			phonePrefix("phonePrefix", recvHp.slice(0,4));
			$('#phonePrefix').data("kendoDropDownList").enable( true ); //phonePrefix
			$('#recvHp1').val(recvHp.slice(4,(recvHp.length)-4));
			$('#recvHp2').val(recvHp.slice((recvHp.length)-4,recvHp.length));
		}
		$('#recvZipCd').val(paramData.recvZipCd);
		$('#recvAddr1').val(paramData.orgRecvAddr1);
		$('#recvAddr2').val(paramData.orgRecvAddr2);
		$('#recvBldNo').val(paramData.recvBldNo);
		$('#doorMsgCd').val(paramData.doorMsgCd);
		$('#doorMsgCdName').val(paramData.doorMsgCdName);

		//비회원인 경우 배송지목록 탭 비노출
		if(paramData.urUserId == 0 ) {
			$('#shippingListDiv').hide();
			$('#startShippingListArea').hide();
		} else {
			initShippingZoneList();
		}

	}

	//배송예정일 datePicker
    function pickerDate(dayOn) {

    	deliveryDtPicker = new FbDatePicker("deliveryDtPicker", {
            dayOff: dayOff,
            dayOn: dayOn,
            beforeShowDay: function(date) {
                if(dayOn != [] && dayOn != undefined){
                    if(this.isDayOn(date)){
                        return [true, "dayOn"];
                    }else{
                        return [false, ""];
                    }
                }else{
                    if( this.isDayOff(date)) {
                        return [false, "dayOff", "휴일"];
                    } else if( this.isDisabledDate(date) ) {
                        return [false, ""];
                    } else {
                        return [true, ""];
                    }
                }
            },
            onChange: function(e) {
                const context = this;
                onChangeDatePicker.call(context, e);
            }
        })
    };

  //배송예정일 datePicker
    function pickerDate1(dayOn, picker) {

    	shippingListDeliveryDtPicker = new FbDatePicker(picker, {
            dayOff: dayOff,
            dayOn: dayOn,
            beforeShowDay: function(date) {
                if(dayOn != [] && dayOn != undefined){
                    if(this.isDayOn(date)){
                        return [true, "dayOn"];
                    }else{
                        return [false, ""];
                    }
                }else{
                    if( this.isDayOff(date)) {
                        return [false, "dayOff", "휴일"];
                    } else if( this.isDisabledDate(date) ) {
                        return [false, ""];
                    } else {
                        return [true, ""];
                    }
                }
            },
            onChange: function(e) {
                const context = this;
                onChangeDatePicker.call(context, e);
            }
        })
    };

    function onChangeDatePicker(e, type, sId, eId) {
        const self = this;
        let date = e.target.value;
        let $target = $(e.target);
        let sd = sId ? $("#" + sId) : null;
        let ed = eId ? $("#" + eId) : null;

        //변경일 노출일자 설정
        if(type = 'end'){

        }

        if( !fnIsValidDate(date) ) {
            fnKendoMessage({message: "올바른 날짜가 아닙니다."});
            $target.val("");
            return false;
        }

        date = new Date(date);

        if( self.isDayOffOrDisabled(date) ) {
            fnKendoMessage({message: "해당 날짜를 선택하실 수 없습니다."});
            $target.val("");
            return false;
        }

        if( sd && ed ) {
            let startDate = sd.val();
            let endDate = ed.val();
            if( !startDate || !endDate ) return;
            if( fnValidateDatePicker(startDate, endDate) ) return;

            if( type === "start" ) {
                fnKendoMessage({message: "시작일을 확인해주세요."});
            } else {
                fnKendoMessage({message: "종료일을 확인해주세요."});
            }

            $target.val("");
        }
    }



	// 배송지 목록 리스트 조회
	function initShippingZoneList(){
		fnAjax({
	        url     : '/admin/order/getShippingZoneList',
	        params  : {urUserId : paramData.urUserId, odOrderDetlId : paramData.odOrderDetlId},
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
		let scheduleDelvDateList = data.scheduleDelvDateList == null ? paramData.deliveryDt : data.scheduleDelvDateList;
        //tpl = tpl.replace(/\{IDX\}/gi, i);
		// 배송지 목록 데이터
		let rows = data.rows;

		for(let i=0; i<rows.length; i++){

			let delivery = data.rows[i].delivery;
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
			if(data.scheduleDelvDateList != null && paramData.goodsDailyType == "GOODS_DAILY_TP.GREENJUICE"){
				tplObj.find(".shippingListDeliveryDtPicker").val(scheduleDelvDateList[0]).attr("id", "shippingListDeliveryDtPicker__"+rows[i].urShippingAddrId);
			}

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


			//일일배송 & 녹즙인 경우 -> 적용일자 노출
			if(( delivery.dailyDeliveryType == 'STORE_DELIVERABLE_ITEM.ALL' || delivery.dailyDeliveryType == 'STORE_DELIVERABLE_ITEM.FD') && scheduleDelvDateList != null && paramData.goodsDailyType == "GOODS_DAILY_TP.GREENJUICE"){
				tplObj.find("#shippingListDeliveryDateDiv").attr("hidden", false);
			} else {
				tplObj.find("#shippingListDeliveryDateDiv").attr("hidden", true);
			}
			$target.append(tplObj);
			//일일배송 & 녹즙인 경우 -> 적용일자 노출
			if(( delivery.dailyDeliveryType == 'STORE_DELIVERABLE_ITEM.ALL' || delivery.dailyDeliveryType == 'STORE_DELIVERABLE_ITEM.FD' ) && scheduleDelvDateList != null  && paramData.goodsDailyType == "GOODS_DAILY_TP.GREENJUICE"){
				pickerDate1(scheduleDelvDateList, "shippingListDeliveryDtPicker__"+rows[i].urShippingAddrId);
			}

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

			//일일배송 & 녹즙인 경우 -> 적용일자 노출
			if(data.rows[i].delivery.dailyDeliveryType == 'STORE_DELIVERABLE_ITEM.FD' || data.rows[i].delivery.dailyDeliveryType == 'STORE_DELIVERABLE_ITEM.ALL'){
				$("#shippingListDeliveryDateDiv").show();
			} else {
				$("#shippingListDeliveryDateDiv").hide();
			}
		}


	}

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	// 옵션 초기화
	function fnInitOptionBox() {
		//배송요청사항 공통코드 (드롭다운리스트)
		function deliveryMsgList(){
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

		function deliveryMsg (paramDeliveryMsg) {
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
		deliveryMsg(paramData.deliveryMsg);
		//배송출입정보 공통코드 (라디오버튼)
		function doorMsgCd(id, paramDoorMsgCd) {

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
		doorMsgCd("doorMsgCd", paramData.doorMsgCd);
	};

	//주소찾기 팝업
	function fnAddressPopup() { //우편번호, 주소1, 주소2(상세주소),
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
	            document.getElementById("recvZipCd").value = data.zonecode;
	            document.getElementById("recvAddr1").value = addr;
	            document.getElementById("recvBldNo").value = data.buildingCode;
	            // 커서를 상세주소 필드로 이동한다.
	            document.getElementById("recvAddr2").focus();

	        },
		onclose : function() {
				fnShippingAddressPossibleDeliveryInfo($('#recvZipCd').val(), $('#recvBldNo').val()); //배송가능유형조회
			}
	    }).open();
	};

	//배송가능유형조회
	function fnShippingAddressPossibleDeliveryInfo(zipCode, buildingCode) {
		fnAjax({
	        url     : '/admin/order/getShippingAddressPossibleDeliveryInfo',
	        params  : {zipCode : zipCode, buildingCode : buildingCode},
	        success :
	            function( data ){
	        		if(data.dawnDelivery == true){//새벽배송가능
						$("#dawnDelivery").show();
					} else {
						$("#dawnDelivery").hide();
					}

					if(data.dailyDelivery == true){ //일일배송가능여부
						$("#dailyDelivery").show();
					} else {
						$("#dailyDelivery").hide();
					}

					if(data.storeDelivery == true){ //매장배송가능여부
						$("#storeDelivery").show();
						$("#storeDelivery__").text("매장배송 "+data.storeName);
					} else {
						$("#storeDelivery").hide();
					}

					if(data.shippingCompDelivery == true){ //택배배송가능여부
						if(data.dawnDelivery == true){
							$("#dawnDelivery").show();
							$("#shippingCompDelivery").hide();
						} else {
							$("#shippingCompDelivery").show();
						}
					} else {
						$("#shippingCompDelivery").hide();
					}
		        }
	    });
	}


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

    	var receiverName = $("#shippingList__userName__"+id).text();
    	var receiverMobile = $("#shippingList__phoneNum__"+id).text();
    	var receiverZipCode = $("#shippingList__zipCode__"+id).text();
    	var receiverAddress1 = $("#shippingList__detailAddr1__"+id).text();
    	var receiverAddress2 = $("#shippingList__detailAddr2__"+id).text();
    	var buildingCode = $("#shippingList__buildingCode__"+id).text();
    	var doorMsgCdName = $("#shipInfo__content__"+id).text();
    	var shippingComment = $("#shipInfo__content__shipping__"+id).text();
    	var accessInformationType = $("#shipInfo__type__"+id).text();
    	var accessInformationPassword = $("#shipInfo__password__"+id).text();
    	var deliveryDt = $("#shippingListDeliveryDtPicker__"+id).val(); //배송적용일자

    	let data = {
	    			urShippingAddrId : id, urUserId : paramData.urUserId,
	    			odShippingZoneId : paramData.odShippingZoneId, odOrderId : paramData.odOrderId,
	    			receiverName : receiverName, receiverMobile:receiverMobile,
	    			receiverZipCode: receiverZipCode,receiverAddress1:receiverAddress1,
	    			receiverAddress2:receiverAddress2,buildingCode: buildingCode,
	    			shippingComment:shippingComment,accessInformationType:accessInformationType,
	    			accessInformationPassword:accessInformationPassword,
	    			deliveryType : paramData.deliveryTypeCode,
					deliveryDt : deliveryDt,
					goodsDailyType : paramData.goodsDailyType,
					promotionTp : paramData.promotionTp,
					odOrderDetlId : paramData.odOrderDetlId,
					doorMsgCdName: doorMsgCdName,
    				}
		let orderCopyYn = stringUtil.getString(paramData.orderCopyYn, "N");


		fnKendoMessage({message : '선택한 주소로 변경하시겠습니까?', type : "confirm", ok : function(){
			//일일배송-녹즙인경우 도착예정일 업데이트
	    	//if(deliveryDt != undefined){
			// if(paramData.deliveryTypeCode == 'DELIVERY_TYPE.DAILY' && paramData.goodsDailyType == 'GOODS_DAILY_TP.GREENJUICE'){
	    	// 	updateArrivalScheduledDate($("#shippingListDeliveryDtPicker__"+id).val());
	    	// }
			if (orderCopyYn == "Y"){
				parent.POP_PARAM["parameter"].deliveryData	= data;
				fnClose();
			} else {
				fnAjax({
					url: '/admin/ur/userShipping/putShippingAddressIntoOrderShippingZone',
					params: data,
					success:
						function (data) {

							if (data == 'CHANGE_DAWN_TO_NORMAL') {
								fnKendoMessage({
									message: "택배 배송으로 변경 됩니다.", ok: function () {
										fnClose();
									}
								});
							} else {
								fnKendoMessage({
									message: "변경되었습니다.", ok: function () {
										fnClose();
									}
								});
							}
						},
					isAction: 'update'
				});
			}

        }});
    }

    //닫기
    function fnClose(){
        parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
    };

    //저장
    function fnSave() {

	 		var url  = '/admin/order/putOrderDetailShippingZone'; // 수정팝업 > 저장 url
			var data = $('#inputFormMain').formSerialize(true);
			var msg = "변경하시겠습니까?";
			if( data.rtnValid ){

				let doorMsgCdStr = '<p>' + '<span style=\'color: red;font-size: 9pt;font-weight: bolder;\'>[' + '배송출입정보' + '] </span>' + '필수 입력입니다.' + '<p>';
				if(fnIsEmpty($('input[name=doorMsgCd]:checked').val())) { // 배송출입정보
					fnKendoMessage({message: doorMsgCdStr});
					return false;
				}

				if(data.doorMsgCd == "ACCESS_INFORMATION.FRONT_DOOR_PASSWORD") { //공동현관 비밀번호
					if(	$.trim(data.doorMsg) == "" ) {
						fnKendoMessage({message:"공동현관 비밀번호를 입력해주세요."});
						return false;
					}else {
						delete data.doorEtc;
					}
				} else if(data.doorMsgCd == "ACCESS_INFORMATION.ETC"){ // 기타
					if(	$.trim(data.doorEtc) == "" ) {
						fnKendoMessage({message:"배송 출입 정보를 입력해주세요."});
						return false;
					}else {
						data.doorMsg = data.doorEtc;
					}
				} else {
					delete data.doorMsg;
					delete data.doorEtc;
				}

				if($('#deliveryMsg').val() == '직접입력'){
					if($.trim(data.deliveryMsgEtc) == ""){
						fnKendoMessage({message:"배송 요청사항을 입력해주세요."});
						return false;
					}
					data.deliveryMsg = $('#deliveryMsgEtc').val();
				}else {
					data.deliveryMsg = $('#deliveryMsg').val();
				}
				if($('#deliveryDtDiv').is(':visible') == true && data.deliveryDtPicker == '') {
					fnKendoMessage({message: "적용일자를 선택해주세요."});
					return false;
				}
				data.recvHp = paramData.orgRecvHp.indexOf("-") < 0 ? data["phonePrefix"] + data.recvHp1 + data.recvHp2
															  	   : data["phonePrefix"] + '-' + data.recvHp1 + '-'+ data.recvHp2;
				data.odShippingZoneId = paramData.odShippingZoneId;
				data.odOrderId = paramData.odOrderId;
				data.deliveryType = paramData.deliveryTypeCode;
				data.deliveryDt =  data.deliveryDtPicker;
				data.goodsDailyType = paramData.goodsDailyType;
				data.promotionTp = paramData.promotionTp;
				data.odOrderDetlId = paramData.odOrderDetlId;


				if(paramData.deliveryTypeCode == 'DELIVERY_TYPE.DAILY' && paramData.goodsDailyType == 'GOODS_DAILY_TP.GREENJUICE'){
					if(paramData.deliveryDt == data.deliveryDtPicker){
						msg = data.recvAddr1+" "+data.recvAddr2+"(으)로 변경하시겠습니까?";
					} else {
						msg = data.recvAddr1+" "+data.recvAddr2+"<BR>"+
							  paramData.deliveryDt+"에서 "+data.deliveryDtPicker+"로 변경하시겠습니까?";
					}
            	}

				fnKendoMessage(
				{message : msg,
				    type : "confirm",
				      ok : function(){
							// if(paramData.deliveryTypeCode == 'DELIVERY_TYPE.DAILY' && paramData.goodsDailyType == 'GOODS_DAILY_TP.GREENJUICE'){
							// 	updateArrivalScheduledDate(data.deliveryDtPicker);
			            	// }


						  	let orderCopyYn = stringUtil.getString(paramData.orderCopyYn, "N");
						  	if (orderCopyYn == "Y"){

								let deliveryData = {
									urUserId : paramData.urUserId,
									odShippingZoneId : paramData.odShippingZoneId,
									odOrderId : paramData.odOrderId,
									receiverName : data.recvNm,
									receiverMobile: data.recvHp,
									receiverZipCode: data.recvZipCd,
									receiverAddress1: data.recvAddr1,
									receiverAddress2: data.recvAddr2,
									buildingCode: data.recvBldNo,
									shippingComment: data.deliveryMsg,
									accessInformationType: stringUtil.getString($("input[name='doorMsgCd']:checked").val(), "ACCESS_INFORMATION.ETC"),
									accessInformationPassword: $.trim($("#doorMsg").val()),
									deliveryType : paramData.deliveryTypeCode,
									deliveryDt : data.deliveryDt,
									goodsDailyType : paramData.goodsDailyType,
									promotionTp : paramData.promotionTp,
									odOrderDetlId : paramData.odOrderDetlId,
									doorMsgCdName: $("input[name='doorMsgCd']:checked").next().text()

								}
								parent.POP_PARAM["parameter"].deliveryData	= deliveryData;
								fnClose();
							} else {
								fnAjax({
									url: url,
									params: data,
									success:
										function (data) {

											if (data == 'CHANGE_DAWN_TO_NORMAL') {
												fnKendoMessage({
													message: "택배 배송으로 변경 됩니다.", ok: function () {
														fnClose();
													}
												});
											} else {
												fnKendoMessage({
													message: "변경되었습니다.", ok: function () {
														fnClose();
													}
												});
											}

										},
									isAction: 'batch'
								});
							}
				      	}});
			}
	}

    //일일배송-녹즙인 경우 도착예정일 업데이트
	function updateArrivalScheduledDate(deliveryDt){

		let data = {odOrderDetlId : paramData.odOrderDetlId, ilGoodsId : paramData.ilGoodsId, deliveryDt : deliveryDt};
		fnAjax({
            url     : "/admin/order/schedule/putOrderArrivalScheduledDate",
            params  : data,
            success :
                function( data ){
            		console.log("일일배송-녹즙 > 도착예정일 업데이트 완료");
                },
                isAction : 'batch'
        });
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
