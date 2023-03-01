/**-----------------------------------------------------------------------------
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2017.01.13		최봉석          최초생성
 * @ 2020.10.28     최성현          리펙토링
 * @ 2020.11.27     강윤경          현행화
 * @
 * **/
'use strict';

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//쇼핑몰로고(메인)
    fnKendoUpload({
        id : "SHOP_LOGO_IMAGE_FILE",
        select : function(e) {
        	fnFilecheck(e, "SHOP_LOGO_IMAGE_FILE", "shopImageFileName");
        },
        localization : "파일검색"
    });

	//쇼핑몰로고(상세)
    fnKendoUpload({
        id : "SHOP_LOGO_DETAIL_IMAGE_FILE",
        select : function(e) {
        	fnFilecheck(e, "SHOP_LOGO_DETAIL_IMAGE_FILE", "shopDetailImageFileName");
        },
        localization : "파일검색"
    });

    //favicon
    $("#FAVICON_IMAGE_FILE").change(function(e) {
    	var _URL = window.URL || window.webkitURL;
    	var file, img;
        if ((file = this.files[0])) {
             img = new Image();
             var objectUrl = _URL.createObjectURL(file);
             img.onload = function () {
            	 if(this.width > 50 || this.height > 50 ) {
            		 fnKendoMessage({
                     	message : "이미지 사이즈는 50px*50px까지 첨부가능합니다"
                     });
            		 $("#FAVICON_IMAGE_FILE").val("")
            	} else {
            		fnFilecheck(e.target, "FAVICON_IMAGE_FILE", "faviconImageFileName");
            	}
             };
             img.src = objectUrl;
        }

    } );

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'shopSetting',
			callback : fnUI
		});

	}

	// 화면 UI 초기화
	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitOptionBox();

	}

	//--------------------------------- Button Start---------------------------------

	// 버튼 초기화
	function fnInitButton(){
		$('#fnSave').kendoButton();
	}

	//파일 확장자 체크
	function fnFilecheck(e, id, fileNameId){
		let f = e.files[0];
        let ext = f.name.substring(f.name.lastIndexOf(".") + 1);;
        let avaibleExt =  ["ico"];

        if(id == 'SHOP_LOGO_IMAGE_FILE' || id == 'SHOP_LOGO_DETAIL_IMAGE_FILE') avaibleExt =  ["png"]

        if ($.inArray(ext.toLowerCase(), avaibleExt) == -1 ){
            fnKendoMessage({
            	message : avaibleExt + " 파일만 첨부가능합니다."
            });
            e.preventDefault();
            return false;
        } else {
            $("#"+fileNameId).val(f.name);
        }
	}

	function fnSearch(){

	}

	function fnDefaultSearch(){
		fnAjax({
			url     : '/admin/policy/shopsetting/getShopSettingList',
			params	: {"stShopId" :'1'},
			success :
				function( data ){
					for(var i =0; i <data.rows.length ; i++){

						if($('#'+data.rows[i].psKey).attr('type') == "textarea"){
							$('#'+data.rows[i].psKey).data("key",data.rows[i].ID);
							$('#'+data.rows[i].psKey).val(data.rows[i].psValue);
						}else{
							$('input[name='+data.rows[i].psKey+']').data("key",data.rows[i].ID);
							if($('input[name='+data.rows[i].psKey+']').attr('type') != "file"){
								if($('input[name='+data.rows[i].psKey+']').data('role') == "dropdownlist"){
									$('input[name='+data.rows[i].psKey+']').data('kendoDropDownList').value(data.rows[i].psValue);
								}else if($('input[name='+data.rows[i].psKey+']').attr('type') == "radio"){
									$('input[type=radio][name='+data.rows[i].psKey+'][value='+data.rows[i].psValue+']').prop('checked',true);
								}else if($('input[name='+data.rows[i].psKey+']').attr('type') == "checkbox" && data.rows[i].psValue == "Y"){
									$('input[name='+data.rows[i].psKey+']').prop("checked", true);
								}else if($('input[name='+data.rows[i].psKey+']') && data.rows[i].psValue == "Y"){
									$('input[name='+data.rows[i].psKey+'_CONDI]').prop("checked", true);
									$('input[name='+data.rows[i].psKey+']').val("Y");
								}
								else{
									$('input[name='+data.rows[i].psKey+']').val(data.rows[i].psValue);
								}
								if(data.rows[i].psKey == "SHOP_LOGO_IMAGE_FILE_ORIG_NAME") {
									$('#shopImageFileName').val(data.rows[i].psValue);
								}
								if(data.rows[i].psKey == "SHOP_LOGO_DETAIL_IMAGE_FILE_ORIG_NAME") {
									$('#shopDetailImageFileName').val(data.rows[i].psValue);
								}
								if(data.rows[i].psKey == "FAVICON_IMAGE_FILE_ORIG_NAME") {
									$('#faviconImageFileName').val(data.rows[i].psValue);
								}
							}
						}
					}
				}
		});
	}

	function fnClear(){
		$('#searchForm').formClear(true);
	}

	function fnNew(){
	}

	function fnSave(){
		let serializedData = $("#shopSettingInputForm").formSerialize(true);
		let updateRequestDtoList = new Array();
		let inputFormData = new Object();
		let checkPayment = 0;

		$.each(serializedData, function(index, item) {
			//체크박스 파라미터 on->Y로 치환
			let psKeyArray = index.split('_');

			if(psKeyArray[psKeyArray.length-1] == 'PC'||psKeyArray[psKeyArray.length-1] == 'MOBILE'||psKeyArray[psKeyArray.length-1] == 'APP') {

				if(item == 'on') {
					item = 'Y';
				} else if(item == 'Y') {
					checkPayment = 1;
				}
			}

			if(index != 'rtnValid'){

				let jsonObject = new Object();

				jsonObject.psKey = index;
				jsonObject.psValue = item;

				updateRequestDtoList.push(jsonObject);
			}
		})


		if(checkPayment === 0) {
			fnKendoMessage({
				message : "결제수단 채널을 1개 이상 선택해주세요."
			});
			return;
		}

		inputFormData['updateData'] = kendo.stringify( updateRequestDtoList );
		let data = updateRequestDtoList;

		fnAjaxSubmit({
			form : 'shopSettingInputForm',
            fileUrl : '/fileUpload',
            storageType : "public",
            domain : "ur",
			url     : '/admin/policy/shopsetting/putShopSetting',
			params  : inputFormData,
			success :
				function( data ){
					fnBizCallback("update", data);
				},
			isAction : 'update'
		});
	}
	function fnDel(){

	}
	function fnClose(){

	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){

	}
	function fnGridClick(){

	};
	//Initialize End End
	function fnEditCustm(e){
		return;
	}
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

		fnDefaultSearch();

		var sevenDate = [
            {"CODE":"1"	    ,"NAME": '1일'},
			{"CODE":"2"	    ,"NAME": '2일'},
			{"CODE":"3"	    ,"NAME": '3일'},
			{"CODE":"4"	    ,"NAME": '4일'},
			{"CODE":"5"	    ,"NAME": '5일'},
			{"CODE":"6"	    ,"NAME": '6일'},
			{"CODE":"7"	    ,"NAME": '7일'},
			{"CODE":"8"	    ,"NAME": '8일'},
			{"CODE":"9"		,"NAME": '9일'},
			{"CODE":"10"	,"NAME": '10일'},
			{"CODE":"11"	,"NAME": '11일'},
			{"CODE":"12"	,"NAME": '12일'},
			{"CODE":"13"	,"NAME": '13일'},
			{"CODE":"14"	,"NAME": '14일'},
			{"CODE":"15"	,"NAME": '15일'},
			{"CODE":"16"	,"NAME": '16일'},
			{"CODE":"17"	,"NAME": '17일'},
			{"CODE":"18"	,"NAME": '18일'},
			{"CODE":"19"	,"NAME": '19일'},
			{"CODE":"20"	,"NAME": '20일'},
			{"CODE":"21"	,"NAME": '21일'},
			{"CODE":"22"	,"NAME": '22일'},
			{"CODE":"23"	,"NAME": '23일'},
			{"CODE":"24"	,"NAME": '24일'},
			{"CODE":"25"	,"NAME": '25일'},
			{"CODE":"26"	,"NAME": '26일'},
			{"CODE":"27"	,"NAME": '27일'},
			{"CODE":"28"	,"NAME": '28일'},
			{"CODE":"29"	,"NAME": '29일'},
			{"CODE":"30"	,"NAME": '30일'}
		];

		fnKendoDropDownList({ id : 'OD_CART_MAINTENANCE_PERIOD'	, data : sevenDate, value : "7" });
		fnKendoDropDownList({ id : 'OD_SHIPPING_AUTO_END_DAY'	, data : sevenDate, value : "7" });
		fnKendoDropDownList({ id : 'OD_SELL_CONFIRM_DAY'		, data : sevenDate, value : "14" });


		//신용카드 결제수단 설정 체크박스
		fnTagMkChkBox({
            id : "CREDIT_CARD_PAYMENT_PC_CONDI",
            data : [ { "CODE" : "", "NAME" : "PC" } ],
            tagId : "CREDIT_CARD_PAYMENT_PC_CONDI",
            change : function(e){
                if( $("input:checkbox[name=CREDIT_CARD_PAYMENT_PC_CONDI]").is(":checked") ){
                    $("#CREDIT_CARD_PAYMENT_PC").val("Y");
                }else{
                    $("#CREDIT_CARD_PAYMENT_PC").val("N");
                }
            }
        });

		fnTagMkChkBox({
            id : "CREDIT_CARD_PAYMENT_MOBILE_CONDI",
            data : [ { "CODE" : "", "NAME" : "MOBILE" } ],
            tagId : "CREDIT_CARD_PAYMENT_MOBILE_CONDI",
            change : function(e){
                if( $("input:checkbox[name=CREDIT_CARD_PAYMENT_MOBILE_CONDI]").is(":checked") ){
                    $("#CREDIT_CARD_PAYMENT_MOBILE").val("Y");
                }else{
                    $("#CREDIT_CARD_PAYMENT_MOBILE").val("N");
                }
            }
        });

		fnTagMkChkBox({
            id : "CREDIT_CARD_PAYMENT_APP_CONDI",
            data : [ { "CODE" : "", "NAME" : "APP" } ],
            tagId : "CREDIT_CARD_PAYMENT_APP_CONDI",
            change : function(e){
                if( $("input:checkbox[name=CREDIT_CARD_PAYMENT_APP_CONDI]").is(":checked") ){
                    $("#CREDIT_CARD_PAYMENT_APP").val("Y");
                }else{
                    $("#CREDIT_CARD_PAYMENT_APP").val("N");
                }
            }
        });

		//실시간 계좌 이체 결제수단 체크박스
		fnTagMkChkBox({
            id : "REAL_TIME_ACCOUNT_WIRE_PC_CONDI",
            data : [ { "CODE" : "", "NAME" : "PC" } ],
            tagId : "REAL_TIME_ACCOUNT_WIRE_PC_CONDI",
            change : function(e){
                if( $("input:checkbox[name=REAL_TIME_ACCOUNT_WIRE_PC_CONDI]").is(":checked") ){
                    $("#REAL_TIME_ACCOUNT_WIRE_PC").val("Y");
                }else{
                    $("#REAL_TIME_ACCOUNT_WIRE_PC").val("N");
                }
            }
        });

		fnTagMkChkBox({
            id : "REAL_TIME_ACCOUNT_WIRE_MOBILE_CONDI",
            data : [ { "CODE" : "", "NAME" : "MOBILE" } ],
            tagId : "REAL_TIME_ACCOUNT_WIRE_MOBILE_CONDI",
            change : function(e){
                if( $("input:checkbox[name=REAL_TIME_ACCOUNT_WIRE_MOBILE_CONDI]").is(":checked") ){
                    $("#REAL_TIME_ACCOUNT_WIRE_MOBILE").val("Y");
                }else{
                    $("#REAL_TIME_ACCOUNT_WIRE_MOBILE").val("N");
                }
            }
        });

		fnTagMkChkBox({
            id : "REAL_TIME_ACCOUNT_WIRE_APP_CONDI",
            data : [ { "CODE" : "", "NAME" : "APP" } ],
            tagId : "REAL_TIME_ACCOUNT_WIRE_APP_CONDI",
            change : function(e){
                if( $("input:checkbox[name=REAL_TIME_ACCOUNT_WIRE_APP_CONDI]").is(":checked") ){
                    $("#REAL_TIME_ACCOUNT_WIRE_APP").val("Y");
                }else{
                    $("#REAL_TIME_ACCOUNT_WIRE_APP").val("N");
                }
            }
        });

		//가상계좌 결제수단 체크박스
		fnTagMkChkBox({
            id : "VIRTUAL_ACCOUNT_WIRE_PC_CONDI",
            data : [ { "CODE" : "", "NAME" : "PC" } ],
            tagId : "VIRTUAL_ACCOUNT_WIRE_PC_CONDI",
            change : function(e){
                if( $("input:checkbox[name=VIRTUAL_ACCOUNT_WIRE_PC_CONDI]").is(":checked") ){
                    $("#VIRTUAL_ACCOUNT_WIRE_PC").val("Y");
                }else{
                    $("#VIRTUAL_ACCOUNT_WIRE_PC").val("N");
                }
            }
        });

		fnTagMkChkBox({
            id : "VIRTUAL_ACCOUNT_WIRE_MOBILE_CONDI",
            data : [ { "CODE" : "", "NAME" : "MOBILE" } ],
            tagId : "VIRTUAL_ACCOUNT_WIRE_MOBILE_CONDI",
            change : function(e){
                if( $("input:checkbox[name=VIRTUAL_ACCOUNT_WIRE_MOBILE_CONDI]").is(":checked") ){
                    $("#VIRTUAL_ACCOUNT_WIRE_MOBILE").val("Y");
                }else{
                    $("#VIRTUAL_ACCOUNT_WIRE_MOBILE").val("N");
                }
            }
        });

		fnTagMkChkBox({
            id : "VIRTUAL_ACCOUNT_WIRE_APP_CONDI",
            data : [ { "CODE" : "", "NAME" : "APP" } ],
            tagId : "VIRTUAL_ACCOUNT_WIRE_APP_CONDI",
            change : function(e){
                if( $("input:checkbox[name=VIRTUAL_ACCOUNT_WIRE_APP_CONDI]").is(":checked") ){
                    $("#VIRTUAL_ACCOUNT_WIRE_APP").val("Y");
                }else{
                    $("#VIRTUAL_ACCOUNT_WIRE_APP").val("N");
                }
            }
        });

		//휴대폰 소액결제 결제수단 체크박스
		fnTagMkChkBox({
            id : "MOBILE_PAYMENT_PC_CONDI",
            data : [ { "CODE" : "", "NAME" : "PC" } ],
            tagId : "MOBILE_PAYMENT_PC_CONDI",
            change : function(e){
                if( $("input:checkbox[name=MOBILE_PAYMENT_PC_CONDI]").is(":checked") ){
                    $("#MOBILE_PAYMENT_PC").val("Y");
                }else{
                    $("#MOBILE_PAYMENT_PC").val("N");
                }
            }
        });

		fnTagMkChkBox({
            id : "MOBILE_PAYMENT_MOBILE_CONDI",
            data : [ { "CODE" : "", "NAME" : "MOBILE" } ],
            tagId : "MOBILE_PAYMENT_MOBILE_CONDI",
            change : function(e){
                if( $("input:checkbox[name=MOBILE_PAYMENT_MOBILE_CONDI]").is(":checked") ){
                    $("#MOBILE_PAYMENT_MOBILE").val("Y");
                }else{
                    $("#MOBILE_PAYMENT_MOBILE").val("N");
                }
            }
        });

		fnTagMkChkBox({
            id : "MOBILE_PAYMENT_APP_CONDI",
            data : [ { "CODE" : "", "NAME" : "APP" } ],
            tagId : "MOBILE_PAYMENT_APP_CONDI",
            change : function(e){
                if( $("input:checkbox[name=MOBILE_PAYMENT_APP_CONDI]").is(":checked") ){
                    $("#MOBILE_PAYMENT_APP").val("Y");
                }else{
                    $("#MOBILE_PAYMENT_APP").val("N");
                }
            }
        });

		//간편결제_카카오 결제수단 체크박스
		fnTagMkChkBox({
            id : "KAKAO_PAYMENT_PC_CONDI",
            data : [ { "CODE" : "", "NAME" : "PC" } ],
            tagId : "KAKAO_PAYMENT_PC_CONDI",
            change : function(e){
                if( $("input:checkbox[name=KAKAO_PAYMENT_PC_CONDI]").is(":checked") ){
                    $("#KAKAO_PAYMENT_PC").val("Y");
                }else{
                    $("#KAKAO_PAYMENT_PC").val("N");
                }
            }
        });

		fnTagMkChkBox({
            id : "KAKAO_PAYMENT_MOBILE_CONDI",
            data : [ { "CODE" : "", "NAME" : "MOBILE" } ],
            tagId : "KAKAO_PAYMENT_MOBILE_CONDI",
            change : function(e){
                if( $("input:checkbox[name=KAKAO_PAYMENT_MOBILE_CONDI]").is(":checked") ){
                    $("#KAKAO_PAYMENT_MOBILE").val("Y");
                }else{
                    $("#KAKAO_PAYMENT_MOBILE").val("N");
                }
            }
        });

		fnTagMkChkBox({
            id : "KAKAO_PAYMENT_APP_CONDI",
            data : [ { "CODE" : "", "NAME" : "APP" } ],
            tagId : "KAKAO_PAYMENT_APP_CONDI",
            change : function(e){
                if( $("input:checkbox[name=KAKAO_PAYMENT_APP_CONDI]").is(":checked") ){
                    $("#KAKAO_PAYMENT_APP").val("Y");
                }else{
                    $("#KAKAO_PAYMENT_APP").val("N");
                }
            }
        });

		//간편결제_페이코 결제수단 체크박스
		fnTagMkChkBox({
            id : "PAYCO_PAYMENT_PC_CONDI",
            data : [ { "CODE" : "", "NAME" : "PC" } ],
            tagId : "PAYCO_PAYMENT_PC_CONDI",
            change : function(e){
                if( $("input:checkbox[name=PAYCO_PAYMENT_PC_CONDI]").is(":checked") ){
                    $("#PAYCO_PAYMENT_PC").val("Y");
                }else{
                    $("#PAYCO_PAYMENT_PC").val("N");
                }
            }
        });

		fnTagMkChkBox({
            id : "PAYCO_PAYMENT_MOBILE_CONDI",
            data : [ { "CODE" : "", "NAME" : "MOBILE" } ],
            tagId : "PAYCO_PAYMENT_MOBILE_CONDI",
            change : function(e){
                if( $("input:checkbox[name=PAYCO_PAYMENT_MOBILE_CONDI]").is(":checked") ){
                    $("#PAYCO_PAYMENT_MOBILE").val("Y");
                }else{
                    $("#PAYCO_PAYMENT_MOBILE").val("N");
                }
            }
        });

		fnTagMkChkBox({
            id : "PAYCO_PAYMENT_APP_CONDI",
            data : [ { "CODE" : "", "NAME" : "APP" } ],
            tagId : "PAYCO_PAYMENT_APP_CONDI",
            change : function(e){
                if( $("input:checkbox[name=PAYCO_PAYMENT_APP_CONDI]").is(":checked") ){
                    $("#PAYCO_PAYMENT_APP").val("Y");
                }else{
                    $("#PAYCO_PAYMENT_APP").val("N");
                }
            }
        });
	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------




	$(".checkBoxCondition").change(function() {
		if($(this).is(":checked")) {
			$(this).val("Y");
		}
	})

	/*
	$('.imgFileName').change(function () {
		var file = $(this)[0].files[0];

		var img = new Image();
		var imgwidth = 0;
		var imgheight = 0;
		var maxwidth = 100;
		var maxheight = 100;

		img.src = _URL.createObjectURL(file);

		let imageExtension = file.type.split("/")[1];

		if(imageExtension != "gif" && imageExtension != "jpg" && imageExtension != "jpeg") {
			$('#FAVICON_IMAGE_FILE').val("");
			fnKendoMessage({
				message : "이미지는 gif, jpg, jpeg만 첨부가능합니다."
			});
			return false;
		}
		img.onerror = function() {
			$('#FAVICON_IMAGE_FILE').val("");
			fnKendoMessage({
				message : "이미지 업로드에 실패했습니다."
			});
			return false;
		}
		img.onload = function() {
			imgwidth = this.naturalWidth;
			imgheight = this.naturalHeight;

			if(imgwidth <= maxwidth && imgheight <= maxheight){
				$("#imageFileName").val(file.name)
			}else{
				$('#FAVICON_IMAGE_FILE').val("");
				fnKendoMessage({
					message : "이미지 사이즈는 50px*50px까지 첨부가능합니다."
				});

			}
		};
	});*/

	// 쇼핑몰 Favicon 첨부
//	var _URL = window.URL || window.webkitURL;
//    fnKendoUpload({
//        id : "FAVICON_IMAGE_FILE",
//
//        select : function(e) {
//        	var file = e.files && e.files[0];
//
//
//            let f = e.files[0];
//            let ext = f.extension.substring(1, f.extension.length);
//
//            if ($.inArray(ext.toLowerCase(), [ "jpg", "gif" ]) == -1 ){
//                fnKendoMessage({
//                                  message : "jpg, gif 파일만 첨부가능합니다."
//                });
//                e.preventDefault();
//                return false;
//          } else {
//                if (e.files && e.files[0]) {
//                	var img = new Image();
//
////        			img.src = _URL.createObjectURL(file);
//        			img.onload = function() {
//        				alert(this.width);
//        				$('#faviconImage').attr('src', ele.target.result);
//                    	$('.k-upload-sync').addClass("k-upload-empty");
//                    	$(".cs_bbs_attc_pcMain").html($(".cs_bbs_attc_pcMain").prev().find("span.k-file-name")[0].title);
//                    	$(".k-upload-empty").css("width", "12%");
//        			}
//                }
//            }
//        	if($('#faviconImage')[0].naturalWidth > 50 || $('#faviconImage')[0].naturalHeight > 50) {
//        		fnKendoMessage({
//        			message : "이미지 사이즈는 50px*50px까지 첨부가능합니다."
//        		});
//        		e.preventDefault();
//                return false;
//        	} else {
//
//        	}
//        },
//        localization : "파일첨부"
//    });

	function inputFocus(){

	};

	function condiFocus(){

	};

	/**
	 * 콜백합수
	 */
	function fnBizCallback( id, data ){
		switch(id){
			case 'update':
				fnUpdateGrid(data,$("#aGrid"),"rows");
				fnKendoMessage({message : "저장이 완료되었습니다"});
				fnDefaultSearch();
				break;
		}
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------

	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
