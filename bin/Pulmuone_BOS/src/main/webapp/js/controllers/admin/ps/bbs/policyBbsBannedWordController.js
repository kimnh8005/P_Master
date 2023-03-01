/**-----------------------------------------------------------------------------
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.09.16		박승현          최초생성
 * @
 * **/
'use strict';

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'policyBbsBannedWord',
			callback : fnUI
		});

	}

	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitOptionBox();

	}

	//--------------------------------- Button Start---------------------------------
	function fnInitButton(){
		$('#fnSave').kendoButton();
	}
	function fnSave(){
		let paramData = $("#policyBbsBannedWordInputForm").formSerialize(true);
        if( paramData.rtnValid && fnInputValidation() ){
        	fnAjax({
                 url     : "/admin/policy/bbs/putPolicyBbsBannedWord",
                 params  : paramData,
                 success : function( successData ){
                     fnKendoMessage({message : "저장이 완료되었습니다."});
                 },
                 isAction : "insert"
             });
        }
	}

	// 입력값 검증
	function fnInputValidation(){

        if(fnIsEmpty($("#mallBannedWord").val())){
            fnKendoMessage({ message : "쇼핑몰 금칙어 오류",
                             ok : function(e) { $("#mallBannedWord").focus(); }
            });
            return false;
        }
        if(fnIsEmpty($("#bosBannedWord").val())){
        	fnKendoMessage({ message : "BOS 금칙어 오류",
        		ok : function(e) { $("#bosBannedWord").focus(); }
        	});
        	return false;
        }
	    return true;
	};

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
		function fnDefaultSearch(){
			fnAjax({
				url     : '/admin/policy/bbs/getPolicyBbsBannedWord',
				params	: {"ST_SHOP_ID" :'1'},
				success :
					function( data, resultcode ){
						if (data.RETURN_CODE == '0000' && data.RETURN_MSG != '' && data.RETURN_MSG != null ) {
							fnKendoMessage({
					            message : fnGetLangData({
					                key : data.RETURN_CODE,
					                nullMsg : data.RETURN_MSG
					            })
					        });
					    }
						$('input:radio[name="mallUseYn"]:input[value="' + data.mallUseYn + '"]').prop("checked", true);
						$('input:radio[name="bosUseYn"]:input[value="' + data.bosUseYn + '"]').prop("checked", true);
						$("#mallBannedWord").val(data.mallBannedWord);
						$("#bosBannedWord").val(data.bosBannedWord);
					}
			});
		}

		fnTagMkRadio({
			id    :  'mallUseYn',
			tagId : 'mallUseYn',
			data : [
                { "CODE" : "Y", "NAME" : "예" },
                { "CODE" : "N", "NAME" : "아니오" }
            ],
			chkVal: 'Y',
			style : {}
		});
		fnTagMkRadio({
			id    :  'bosUseYn',
			tagId : 'bosUseYn',
			data : [
				{ "CODE" : "Y", "NAME" : "예" },
				{ "CODE" : "N", "NAME" : "아니오" }
				],
				chkVal: 'Y',
				style : {}
		});

		fnDefaultSearch();

	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------


	var _URL = window.URL || window.webkitURL;

	function inputFocus(){

	};

	function condiFocus(){

	};

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------

	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
