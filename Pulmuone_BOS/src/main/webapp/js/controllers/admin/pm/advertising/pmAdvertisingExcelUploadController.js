/**-----------------------------------------------------------------------------
 * description 		 : 프로모션관리 > 외부광고 코드관리 > 외부광고 엑셀 업로드
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.05.07		이원호          최초생성
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
			PG_ID  : 'pmAdvertisingExcelUpload',
			callback : fnUI
		});
	}

	function fnUI(){
		fnInitButton();	// Initialize Button ---------------------------------
		fnDefaultSet();
		fnInitKendoUpload();
	}

	// --------------------------------- Button
	// Start---------------------------------
	function fnInitButton(){
		$('#fnExcelUpload', '#fnSampleDownload', '#fnDel').kendoButton();
	}

	function fnDefaultSet(){
	    fnClearExcelUploadInfo();
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
//            $("#fileInfoDiv").text(e.files[0].name);
            fnExcelUploadDone(e.files[0].name);
            // --------------------------------------------------------------------
            // 확장자 2중 체크 위치
            // --------------------------------------------------------------------
            // var imageExtension = e.files[0]['extension'].toLowerCase();
            // 전역변수에 선언한 허용 확장자와 비교해서 처리
            // itemMgmController.js 의 allowedImageExtensionList 참조

            //  켄도 이미지 업로드 확장자 검사
            if(!validateExtension(e)) {
            fnKendoMessage({
                message : '등록이 불가능한 양식입<BR>니다. 업로드 양식을 확인<BR>해 주세요.',
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

        fnKendoMessage({
            message:'업로드를 하시겠습니까?',
            type : "confirm" ,
            ok : function(){ fnExcelUpload(gFile, gFileTagId) }
        });
    }

    // NOTE 파일 업로드 이벤트
    function fnExcelUpload(file, fileTagId) {

        var formData = new FormData();
        formData.append('bannerImage', file);


        $.ajax({
            url         : '/admin/promotion/advertising/addAdvertisingExternalExcelUpload'
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
                    if(data.code == '0000'){
                        localMessage = "업로드가 완료 되었습니다.";
                    } else {
                        localMessage = "엑셀 업로드 실패<BR>엑셀 입력 양식을 확인해<BR>주세요.";
                    }

                    fnClearExcelUploadInfo();

                    fnKendoMessage({
                        message : localMessage,
                        ok : function(e) {}
                      });
                    }
        });
    }
    // # 파일업로드 End
    // ##########################################################################

    function fnSampleDownload(){
        fnKendoMessage({
            message:'샘플양식을 다운로드 <BR>하시겠습니까?',
            type : "confirm" ,
            ok : function(){
                document.location.href = "/contents/excelsample/promotion/외부광고코드관리_샘플.xlsx";
            }
        });

    }

    function fnDel(){
        fnClearExcelUploadInfo();
    }

    function fnExcelUploadDone(title){
        // UI change after Done
        $('#fileUpload__title').val(title);
        $('#fileUpload_message').hide();
        $('#fnDel').show();
    }

    function fnClearExcelUploadInfo(){
        // excel data clear
        gFile = "";
        $("#fileInfoDiv").empty();

        // UI Init
        $('#fileUpload__title').val("");
        $('#fileUpload_message').show();
        $('#fnDel').hide();
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
    /** Button delete */
	$scope.fnDel = function(){	 fnDel();	};

	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END
