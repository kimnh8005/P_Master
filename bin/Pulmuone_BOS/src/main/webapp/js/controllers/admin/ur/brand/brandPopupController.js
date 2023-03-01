/**-----------------------------------------------------------------------------
 * description 		 : 시스템관리 - 프로그램관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.10     신성훈          최초생성
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
              PG_ID    : 'brandPopup'
            , callback : fnUI
        });
    }

    function fnUI(){
        fnTranslate();  // 다국어 변환--------------------------------------------

        fnInitButton(); //Initialize Button  ---------------------------------
    }

	//--------------------------------- Button Start---------------------------------

    function fnInitButton() {
        $('#fnClose').kendoButton();
        $("#logoImage").attr("src", paramData.LOGO_URL);
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


