/**-----------------------------------------------------------------------------
 * description 		 : 외부몰관리 > 외부몰주문관리 > 외부몰 주문 엑셀업로드
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.19		이원호          최초생성
 * @
 * **/
'use strict';

// ----------------------------------------------------------------------------
// 파일업로드 - EXCEL
// ----------------------------------------------------------------------------
var gFileTagId;
var gFile;

$(document).ready(function() {

	fnInitialize();	// Initialize Page Call ---------------------------------

	// Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'omOrderUpload',
			callback : fnUI
		});
	}

	function fnUI(){
		fnInitButton();	// Initialize Button ---------------------------------
		fnInitKendoUpload();
		fnSearch();
	}

	// --------------------------------- Button
	// Start---------------------------------
	function fnInitButton(){
		$('#fnExcelUpload, #fnSampleDownload').kendoButton();
	}

	function fnSearch(){
        fnAjax({
            url     : '/admin/outmall/order/getSellersList',
            method  : 'GET',
            success :
                function( data ){
                    let sellersHtml = "<table role='grid' tabindex='-1' data-role='selectable' class='k-selectable'>";
                    sellersHtml += "<colgroup>";
                    sellersHtml += "   <col style='width:200px'>";
                    sellersHtml += "   <col style='width:100px'>";
                    sellersHtml += "</colgroup>";
                    sellersHtml += "<tbody role='rowgroup'>";

                    for(let vo of data.rows){
                        sellersHtml += "<tr>";
                        sellersHtml += "<td style='text-align:center'>" + vo.NAME + "</td>";
                        sellersHtml += "<td style='text-align:center'>" + vo.CODE + "</td>";
                        sellersHtml += "</tr>";
                    }
                    sellersHtml += "</tbody>";
                    sellersHtml += "</table>";
                    $("#sellersContent").html(sellersHtml);
                },
            isAction : 'select'
        });
	}

      // ##########################################################################
      // # 파일업로드 Start
      // ##########################################################################
      // ==========================================================================
      // # 파일업로드-업로드시 사용할 kendoUpload 컴포넌트 초기화
      // ==========================================================================
      function fnInitKendoUpload() {
        var uploadFileTagIdList = ['excelFile'];

        var selectFunction = function(e) {
          if (e.files && e.files[0]) {
            // 엑셀 파일 선택시
            $("#fileInfoDiv").text(e.files[0].name);
            // --------------------------------------------------------------------
            // 확장자 2중 체크 위치
            // --------------------------------------------------------------------
            // var imageExtension = e.files[0]['extension'].toLowerCase();
            // 전역변수에 선언한 허용 확장자와 비교해서 처리
            // itemMgmController.js 의 allowedImageExtensionList 참조

            //  켄도 이미지 업로드 확장자 검사
            if(!validateExtension(e)) {
              fnKendoMessage({
                message : '허용되지 않는 확장자 입니다.',
                ok : function(e) {}
              });
              return;
            }

            gFileTagId = e.sender.element[0].id;
            let reader = new FileReader();

            reader.onload = function(ele) {
                var itemImageScr = ele.target.result; // FileReader 로 생성한 상품 이미지 url
                gFile = e.files[0].rawFile; // kendoUpload 로 가져온 상품 이미지 file 객체

//              fnExcelUpload(file, fileTagId);
            };

            reader.readAsDataURL(e.files[0].rawFile);

          } // End of if (e.files && e.files[0])
        } // End of var selectFunction = function(e)

        for (var i = 0; i < uploadFileTagIdList.length; i++) {
          fnKendoUpload({
              id : uploadFileTagIdList[i]
            , select : selectFunction
          });
        } // End of for (var i = 0; i < uploadFileTagIdList.length; i++)
      }

    // ==========================================================================
    // # 파일업로드-validateExtension
    // ==========================================================================
    function validateExtension(e) {

        var allowedExt = '';
        var ext = e.files[0].extension;
        var $el = e.sender.element;

        if( !$el.length ) return;

        if( $el[0].accept && $el[0].accept.length ) {
          // 공백 제거
          allowedExt = $el[0].accept.replace(/\s/g, '');
          allowedExt = allowedExt.split(',');
        } else {
          allowedExt = allowedImageExtensionList;
        }

        return allowedExt.includes(ext);
    };

    // ==========================================================================
    // # 파일업로드-처리
    // ==========================================================================
    function fnExcelUploadRun(){
        if(gFile == undefined || gFile == ""){
            fnKendoMessage({
                message : "엑셀파일을 등록해주세요.",
                ok : function(e) {
                }
            });
            return;
        }
        fnExcelUpload(gFile, gFileTagId);
    }

    // NOTE 파일 업로드 이벤트
    function fnExcelUpload(file, fileTagId) {

        var formData = new FormData();
        formData.append('bannerImage', file);


        $.ajax({
            url         : '/admin/outmall/order/addOutMallExcelUpload'
            , data        : formData
            , type        : 'POST'
            , contentType : false
            , processData : false
            , async       : false
            , beforeSend : function(xhr) {
                xhr.setRequestHeader('authMenuID', CLICKED_MENU_ID);
            }
            , success     : function(data) {
                    let localMessage = "";
                    if(data.code == 'FILE_ERROR' || data.code == 'EXCEL_TRANSFORM_FAIL' || data.code == 'EXCEL_UPLOAD_NONE'){
                        localMessage = data.message;
                    }else{
                        localMessage = "[결과] : " + data.message + "<BR>" +
                                        " [총 건수] : " + data.data.totalCount + "<BR>" +
                                        " [성공 건수] : " + data.data.successCount + "<BR>" +
                                        " [실패 건수] : " + data.data.failCount;

                        if (data.code != "0000"){
                            //localMessage += "<BR>" +" [실패 메세지] : " + data.data.failMessage;
                        }
                    }
                    gFile = "";
                    $("#fileInfoDiv").empty();


                    fnKendoMessage({
                        message : localMessage,
                        ok : function(e) {}
                      });
                      }
        });
    }
    // # 파일업로드 End
    // ##########################################################################

    function fnSampleDownload(target){
    	if(target == 'E') {
    		document.location.href = "/contents/excelsample/outmall/이지어드민_주문_엑셀업로드_샘플.xlsx"
    	}
    	else if(target == 'S') {
    		document.location.href = "/contents/excelsample/outmall/사방넷_주문_엑셀업로드_샘플.xlsx"
    	}
    }

	// --------------------------------- Button
	// End---------------------------------


	// ------------------------------- Html 버튼 바인딩 Start
	// -------------------------------

	/** Button excelSelect */
	$scope.fnBtnExcelSelect = function(fileType) {$('#' + fileType).trigger('click');};
	/** Button excelUpload */
	$scope.fnExcelUploadRun = function(){	 fnExcelUploadRun();	};
	/** Button SampleDownload */
	$scope.fnSampleDownload = function(target) { fnSampleDownload(target); };

	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END
