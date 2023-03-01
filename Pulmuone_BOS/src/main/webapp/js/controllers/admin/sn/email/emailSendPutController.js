/**-----------------------------------------------------------------------------
 * description 		 : Mail / SMS 설정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.06.17		오영민          최초생성
 * @
 * **/
'use strict';

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });
		fnPageInfo({
			PG_ID  : 'emailSendPut',
			callback : fnUI
		});

	}

	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		initUI();
	}

	//--------------------------------- Button Start---------------------------------


	function fnInitButton(){
		$('#fnSave').kendoButton();

	}

	function fnClose(){
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	}

    function fnDefaultValue(){

        var data = {
        		'snAutoSendId' : parent.POP_PARAM["parameter"].snAutoSendId
        };

        var url  = '/admin/sn/email/getEmailSend';
        fnAjax({
            url     : url,
            params  : data,
            success :
                function( data ){
                    fnBizCallback('select', data);
                },
            isAction : 'select'
        });
    }

	function fnSave(){

		var url  = '/admin/sn/email/putEmailSend';
		var cbId = 'update';

		var data = $('#inputFormMain').formSerialize(true);
		var templateName = $("#templateName").val();
//		if(fnChkVal(templateName)){
//			fnKendoMessage({
//					message : "숫자,영문대소문자,한글.특수문자만 허용 합니다"
//			});
//			return;
//		}

		if( data.rtnValid ){

			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback(cbId, data);
					},
				isAction : cbId
			});
		}

	}


	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------


	function fnInitOptionBox(){


		fnTagMkRadio({
			id    :  "smsSendYn",
			data  : [	{ "CODE" : "Y" , "NAME":"발송"}
					, 	{ "CODE" : "N" , "NAME":"발송안함"}],
			tagId : "smsSendYn",
			chkVal: 'Y',
			style : {},
			change: function() {
				var chk = $('input:radio[id="smsSendYn_0"]').is(":checked");

				if(chk==true){
					$("#smsBody").attr("required", true);
				}else{
					$("#smsBody").attr("required", false);
				}
			}
		});
		fnTagMkRadio({
			id    :  "mailSendYn",
			data  : [	{ "CODE" : "Y" , "NAME":"발송"}
					, 	{ "CODE" : "N" , "NAME":"발송안함"}],
			tagId : "mailSendYn",
			chkVal: 'Y',
			style : {},
			change: function() {
				var chk = $('input:radio[id="mailSendYn_0"]').is(":checked");

				if(chk==true){
					$("#mailTitle").attr("required", true);
					$("#mailBody").attr("required", true);
				}else{
					$("#mailTitle").attr("required", false);
					$("#mailBody").attr("required", false);
				}
			}
		});

		//textarea로 수정
		//fnKendoEditor( {id : 'mailBody'} );

	}

	function fnChkVal(param){
		var check1 = /[^가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9!@#$%^*()\-_=+\\\|\[\]{};:\'",.<>\/?\\s$]/gi;
		if(!check1.test(param)) {
			return false;
		}
		return true;
	}

	function initUI(){
			fnDefaultValue();
	}

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	/**
	 * 콜백합수
	 */
	function fnBizCallback( id, data ){
		switch(id){
			case 'update':
				fnKendoMessage({
					message:"수정되었습니다.",
					ok      : function(e){
						fnClose();
						parent.fnSearch();
					}
				});
				break;
            case 'select':
                //form data binding
                $('#inputFormMain').bindingForm(data, 'rows', true);
				break;
		}
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END

