/**-----------------------------------------------------------------------------
 * system            : 엑셀 다운로드 사유 등록 팝업
 * @
 * @ 수정일            수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.03     강윤경          최초생성
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
            PG_ID  : "excelDownloadReasonPopup",
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){
        fnInitButton();	// Initialize Button  ---------------------------------

    };

    //--------------------------------- Button Start---------------------------------

    // 버튼 초기화
    function fnInitButton(){
        $("#fnSave").kendoButton();
    };

    // 팝업 닫기
    function fnClose(params){
        if(params){
            parent.POP_PARAM = params;
        }
        parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
    };

    // 조회
    function fnSave(){
    	var downloadReason = $("#downloadReason").val();

    	var url  = '/admin/comn/popup/addExcelDownReason';
		var cbId = 'insert';
		var data = { "excelDownloadType" : pageParam.excelDownloadType
					,"downloadReason" 	 : downloadReason
					}

		if( downloadReason.length == 0 ){
			fnKendoMessage({message : '다운로드 사유를 입력해 주세요.'});
			return;
		}
		fnAjax({
			url     : url,
			params  : data,
			success :
				function(){
					fnClose(pageParam.excelDownloadType);
				},
			isAction : 'batch'
		});
    };

    //--------------------------------- Button End---------------------------------



    //------------------------------- Html 버튼 바인딩  Start -------------------------------

    $scope.fnSave = function() { fnSave(); }; /* 저장 */
    //------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
