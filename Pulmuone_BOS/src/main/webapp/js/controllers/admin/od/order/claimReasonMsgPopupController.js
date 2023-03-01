/**-----------------------------------------------------------------------------
 * description 		 : 주문상세관리>클레임정보>클레임상품정보 첨부파일보기 팝업
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.02.10		최윤지			최초생성
 * @
 * **/
'use strict';

var paramData ;
var publicStorageUrl = fnGetPublicStorageUrl();

if (parent.POP_PARAM['parameter']) {
	paramData = parent.POP_PARAM['parameter'];
}


$(document).ready(function() {

    fnInitialize();

    function fnInitialize() {
        $scope.$emit('fnIsMenu', { flag : false });

        fnPageInfo({
              PG_ID    : 'claimReasonMsgPopup'
            , callback : fnUI
        });
    }

    function fnUI(){

        fnInitButton(); //Initialize Button  ---------------------------------
        fnInitList();
    }

	//--------------------------------- Button Start---------------------------------

    function fnInitButton() {
        $('#fnClose').kendoButton();
    }

    //첨부파일 리스트
    function fnInitList(){
    	fnAjax({
			url		: '/admin/order/getOrderClaimAttc',
			params	: {odClaimId : paramData.odClaimId},
			isAction : 'select',
			success	:
				function( data ){
					var imageHtml = "";
					if(data.rows.length>0){
						imageHtml += "			<div style='position:relative; display: flex; flex-direction: row; flex-wrap: wrap; justify-content: left;'>";
						for(var i=0; i < data.rows.length; i++){

							var imageUrl = publicStorageUrl + data.rows[i].uploadPath + data.rows[i].uploadNm;
							imageHtml += "      <span style='width:150px; height:150px; margin:15px; border-color:#a9a9a9; border-style:solid; border-width:1px'>";
							imageHtml += "          <img src='"+ imageUrl +"' style='max-width: 100%; max-height: 100%;' onclick='$scope.fnShowImage(\""+imageUrl+"\")'>";
							imageHtml += "      </span>";
						}
						imageHtml += "			</div>";

						$("#imageContent").html(imageHtml);
					}
					else{
						$("#imageContent").html("");
					}
				}
		});
    }

	// 닫기
	function fnClose(params) {
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	}
	//--------------------------------- Button End---------------------------------


	// 이미지 팝업 호출
	function fnShowImage(imageUrl){

		fnKendoPopup({
			id      : 'feedbackPopup'
			, title   : '첨부파일보기'
			, src     : '#/feedbackPopup'
			, width   : '600px'
			, height  : '600px'
			, param   : { "LOGO_URL" : imageUrl }
			, success : function (id, data) {
			}
		});
	}

    //------------------------------- Html 버튼 바인딩  Start -------------------------------
    /** Common Close */
    $scope.fnClose = function() { fnClose(); };

	$scope.fnShowImage = function(url){ fnShowImage(url); };
    //------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END


