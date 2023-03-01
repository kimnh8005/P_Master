﻿/**-----------------------------------------------------------------------------
 * description 		 : 전시관리 > QR코드관리
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.18		최윤지          최초생성
 * @
 * **/
'use strict';

var publicStorageUrl = null; // image url path
$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'qrCodeMgm',
			callback : fnUI
		});
	}

	function fnUI(){
		fnInitButton();	//Initialize Button  ---------------------------------
		fnInitPublicStorageUrl();
	}

	// 이미지 URL 조회
	function fnInitPublicStorageUrl() {
		publicStorageUrl = fnGetPublicStorageUrl();
	};

	// URL 글자수 제한 (500자)
	$('#qrCodeUrl').on('keyup', function() {
        if($(this).val().length > 500) {
            $(this).val($(this).val().substring(0, 500));
        }
    });

	function readAsDataURL(url) {
        return new Promise((resolve, reject) => {
            const xhr = new XMLHttpRequest();

            xhr.onload = function() {
                const reader = new FileReader();

                reader.onloadend = function() {
                    resolve(reader.result);
                }
                reader.readAsDataURL(xhr.response);
            };

            xhr.open("GET", url);
            xhr.responseType = "blob";
            xhr.send();
        });
    }

	//--------------------------------- Button Start---------------------------------

	//버튼 초기화
	function fnInitButton(){
		$('#fnAdd, #fnClear').kendoButton();
	}

	// 초기화
	function fnClear(){
		$('#inputForm').formClear(true);
	}

	// 생성
	function fnAdd(){

		var qrCodeUrl = $("#qrCodeUrl").val();

		if(qrCodeUrl == " " || qrCodeUrl == "") {
			alert("url을 입력해주세요");
			$("#qrCodeUrl").focus();
			return;
		}
		else{

			fnAjax({
            url     : "/admin/display/qrcode/addQrcode",
            params  : {"qrCodeUrl" : qrCodeUrl},
            success : function( data ){

            	readAsDataURL(publicStorageUrl + data.qrCodeFullSubStoragePath + data.qrCodeImageName).then((r) => {
            		//이미지 업로드
            		$("#qrCodeImage").prop("src", r);

            		$('#fnSave').unbind('click');
            		//이미지 저장 버튼 클릭 시 (다운로드)
                	$('#fnSave').click(function() {
                		 var fileName = data.qrCodeImageName;
                         var base64Array = r.split(",");
                         var mimeType = base64Array[0].match(/[^:\s*]\w+\/[\w-+\d.]+(?=[;| ])/)[0];
                         var base64 = atob(base64Array[1]);
                         var base64ArrBuff = new ArrayBuffer(base64.length);
                         var base64Arr = new Uint8Array(base64ArrBuff);

                         for (let i = 0; i < base64.length; i++) {
                             base64Arr[i] = base64.charCodeAt(i);
                         }

                         if(window.navigator.msSaveBlob) { // for IE and Edge
                             window.navigator.msSaveBlob(new Blob([base64Arr], {type: mimeType}), fileName);
                         } else {

                         var downloadATag = document.createElement("a");

                         downloadATag.href = r;
                         downloadATag.download = fileName;
                         downloadATag.click();
                         }

                	});

                	//이미지 삭제
                	qrCodeImageDelete(data);
                });

            }
        });
	  }
	}
	//--------------------------------- Button End---------------------------------
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	//이미지 삭제
	function qrCodeImageDelete(data){
		var filePath = data.qrCodeFilePath;
		var physicalFileName = data.qrCodeImageName;
		var opt = {
				filePath: filePath, // 다운로드할 파일의 하위 경로
				physicalFileName: physicalFileName, // 업로드시 저장된 물리적 파일명
		}
		fnDeletePublicFile(opt);
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnAdd = function( ) {	fnAdd();};
	$scope.fnClear = function( ) {	fnClear();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
