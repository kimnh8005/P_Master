/**-----------------------------------------------------------------------------
 * system            : ITSM 계정
 * @
 * @ 수정일            수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.05.31     이원호          최초생성
 * @
 * **/
"use strict";

var pageParam = parent.POP_PARAM["parameter"];

$(document).ready(function() {
    fnInitialize();	//Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : false });

        fnPageInfo({
            PG_ID  : "itgcPopup",
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){
        fnInitButton();	// Initialize Button  ---------------------------------
        fnPreventSubmit();
    };

    //--------------------------------- Button Start---------------------------------

    // 버튼 초기화
    function fnInitButton(){
        $("#fnSave").kendoButton();
    };

    // 팝업 닫기
    function fnClose(){
        parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
    };

    // 조회
    function fnSave(){
    	var itsmId = $("#itsmId").val();
		if( itsmId.length == 0 ){
            fnKendoMessage({message : 'ITSM 계정을 입력해 주세요.'});
            return;
        }

        let data = {
            itsmId : itsmId
        };
        parent.POP_PARAM = data;

        fnClose();
    };
    //--------------------------------- Button End---------------------------------

    //------------------------------- Html 버튼 바인딩  Start -------------------------------

    $scope.fnSave = function() { fnSave(); }; /* 저장 */

    //------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
