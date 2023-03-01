/**-----------------------------------------------------------------------------
 * description 		 : 통합문의관리 이미지 팝업
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.07     안치열          최초생성
 * @
 * **/
'use strict';


var paramData ;

if (parent.POP_PARAM['parameter']) {
	paramData = parent.POP_PARAM['parameter'];
}


$(document).ready(function() {

    fnInitialize();

    function fnInitialize() {
        $scope.$emit('fnIsMenu', { flag : false });

        fnPageInfo({
              PG_ID    : 'customerQnaPopup'
            , callback : fnUI
        });
    }

    function fnUI(){

        fnInitButton(); //Initialize Button  ---------------------------------
    }

	//--------------------------------- Button Start---------------------------------

    function fnInitButton() {
        $('#fnClose').kendoButton();
        $("#image").attr("src", paramData.LOGO_URL);
    }

	// 닫기버튼 함수
	function fnClose(params) {
    	parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	}
	//--------------------------------- Button End---------------------------------


    //------------------------------- Html 버튼 바인딩  Start -------------------------------
    $scope.fnClose = function() { fnClose(); };
    //------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END


