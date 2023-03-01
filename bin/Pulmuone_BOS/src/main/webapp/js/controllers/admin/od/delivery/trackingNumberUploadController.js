/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 배송 관리 > 일괄 송장 입력
 * @
 * @ 수정일			수정자          수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.12.23     이규한		퍼블수정 및 기능개발
 * **/

'use strict';

var PAGE_SIZE = 20;
var trackingGridDs, trackingGridOpt, trackingGrid;
var trackingUploadDataArr = new Array();
var gFileTagId;
var gFile;


$(document).ready(function() {

	// Initialize Page Call
	fnInitialize();

    // sheetJs 스크립트 추가
    let myScript = document.createElement("script");
    myScript.setAttribute("src", "js/lib/sheetJs/xlsx.full.min.js");
    document.body.appendChild(myScript);

	// Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', { flag : true });
		fnPageInfo({
			PG_ID  		: 'trackingNumberUpload',
			callback 	: fnUI
		});
	}

	// 화면 UI 초기화
	function fnUI() {
		fnTranslate();		// 다국어 변환
		fnInitButton();		// 버튼 초기화
		fnInitGrid();		// Initialize Grid
		fnInitOptionBox();	// Initialize Option Box
		fnSearch();			// 택배사 기본정보 조회
		fnInitKendoUpload();
	}

	//--------------------------------- Button Start-------------------------------------------
	// 버튼 초기화
	function fnInitButton() {
		$('#fnExcelUpload, #fnExcelUploadDelete, #fnExcelUploadConfirm, #fnSampleExcelDownload').kendoButton();
	}

	// 택배사 기본정보 조회
	function fnSearch() {
		var data = new Object();
		data.rtnValid 		= true;
		data.shippingCompNm = "";		// 택배사명
		data.useYn 			= "Y";		// 사용여부(Y/N)

		var query = {
				page         : 1,
				pageSize     : PAGE_SIZE,
				filterLength : fnSearchData(data).length,
				filter :  {
					filters : fnSearchData(data)
				}
		};
		trackingGridDs.query(query);
	}

	// 초기화
	function fnClear() {
		fnSearch();	// 택배사 기본정보 조회
	}

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

	// 업로드 파일첨부 버튼 클릭
	function fnExcelUpload(file, fileTagId) {

		var formData = new FormData();
		formData.append('bannerImage', file);
		formData.append("originNm",		$("#fileInfoDiv").text());
		//formData.append("sendSmsYn",		($("#sendSmsYn").is(":checked") == true) ? "Y" : "N");

		fnAjax({
			url         : '/admin/order/delivery/orderBulkTrackingNumberExcelUpload'
			, params        : formData
			, type        : 'POST'
			, contentType : false
			, processData : false
			, async       : false
			, success     : function(data, resultcode) {
				let localMessage = "";
				if(resultcode.code == 'FILE_ERROR' || resultcode.code == 'EXCEL_TRANSFORM_FAIL' || resultcode.code == 'EXCEL_UPLOAD_NONE'){
					localMessage = resultcode.message;
				}else{
					localMessage = "[결과] : " + resultcode.message + "<BR>" +
						" [총 건수] : " + data.totalCnt + "<BR>" +
						" [성공 건수] : " + data.successCnt + "<BR>" +
						" [실패 건수] : " + data.failureCnt;

					if (resultcode.code != "0000"){
						localMessage += "<BR>" +" [실패 메세지] : " + data.failMessage;
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

    // 업로드 파일첨부 삭제 버튼 클릭
    function fnExcelUploadDelete(e) {
    	e.preventDefault();
        fileClear();
        $("#uploadViewControl").hide();
        $("#uploadLink").text("");
    }

    // Sample 다운로드 버튼 클릭
	function fnSampleExcelDownload() {
		document.location.href = "/contents/excelsample/trackingNumber/sampleTrackingNumber.xlsx";
	}

    // 업로드 버튼 클릭
	function fnExcelUploadConfirm() {
        var url  				= "/admin/order/delivery/orderBulkTrackingNumberExcelUpload";
        var inputFormValidator 	= $("#inputForm").kendoValidator().data("kendoValidator");

        if (trackingUploadDataArr.length < 1) {
        	fnKendoMessage({message:'업로드할 엑셀파일을 선택해주세요.'});
        } else if (inputFormValidator.validate()) {
            fnKendoMessage({
                type    : "confirm",
                message : "업로드를 하시겠습니까?",
                ok      : function(e){
                			fnExcelUploadSubmit(url, "excelUpload");
							fileClear();
							$("#uploadViewControl").hide();
							$("#uploadLink").text("");
				},
                cancel  : function(e){}
            });
        }
	}
	//--------------------------------- Button End---------------------------------------------


	//------------------------------- Grid Start ----------------------------------------------
	// 그리드 초기화
	function fnInitGrid() {
		trackingGridDs = fnGetPagingDataSource({
			url      : "/admin/policy/shippingcomp/getPolicyShippingCompUseAllList",
			pageSize : 999999
		});

		trackingGridOpt = {
			dataSource	: trackingGridDs,

			columns   	: [
				  {							      title : 'No.'		, width:'30px'	,attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
				, { field : 'psShippingCompId'	, title : '택배사 코드'	, width:'50px'	,attributes:{ style:'text-align:center' }}
				, { field : 'shippingCompNm'	, title : '택배사 명'	, width:'100px'	,attributes:{ style:'text-align:center' }}
			]
		};
		trackingGrid = $('#trackingGrid').initializeKendoGrid( trackingGridOpt ).cKendoGrid();

		trackingGrid.bind("dataBound", function(){
			let rowNum = 1;

			$("#trackingGrid tbody > tr .row-number").each(function(index){
				$(this).html(rowNum);
				rowNum++;
			});

			$("#countTotalSpan").text(trackingGridDs._total);
		});

		$(trackingGrid.tbody).on("click", "td", function (e) {
			var row = $(this).closest("tr");
			var rowIdx = $("tr", trackingGrid.tbody).index(row);
			var colIdx = $("td", row).index(this);
			if (colIdx>0) {
				// TO-DO
			}
		});
	}
	//-------------------------------  Grid End  ----------------------------------------------


	//---------------Initialize Option Box Start ----------------------------------------------
	function fnInitOptionBox() {
        // 업로드 등록 삭제
        $("#uploadDelete").on("click", function(e){
            e.preventDefault();

            fileClear();
            $("#uploadViewControl").hide();
            $("#uploadLink").text("");
        });
	}
	//---------------Initialize Option Box End ------------------------------------------------


    //-------------------------------  Common Function start ----------------------------------
	// 콜백함수
    function fnBizCallback(id, data) {
    	switch (id) {
		case 'excelUpload' :	// 엑셀 업로드
        	var result = data.split("|"); //총건수|성공건수|실패건수
        	fnKendoMessage({message : "총 "+result[0]+"건</br>"+"정상 "+result[1]+"건 / "+"실패 "+result[2]+"건"});
            break;
		}
    };

	// 파일 초기화
    function fileClear() {
    	var agent = navigator.userAgent.toLowerCase();
    	// IE
        if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ) {
            $("#uploadFile").replaceWith( $("#uploadFile").clone(true) );
        // OTHER
        } else {
            $("#uploadFile").val("");
        }
        trackingUploadDataArr = new Array();
    }

    // 엑셀 업로드 submit
    function fnExcelUploadSubmit(url, cbId) {
        var paramData = new Object();
        paramData.upload 		= kendo.stringify(trackingUploadDataArr);
        paramData.originNm		= $("#uploadLink").text();
		//paramData.sendSmsYn		= ($("#sendSmsYn").is(":checked") == true) ? "Y" : "N";
        fnAjax({
            url     : url,
            params  : paramData,
            success : function(successData) {
            	fnBizCallback(cbId, successData);
            },
            isAction : 'insert'
        });
    }


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
	//-------------------------------  Common Function end ------------------------------------


	//------------------------------- Html 버튼 바인딩  Start --------------------------------------
	/** Common Search */
	$scope.fnSearch 			= function() { fnSearch(); };
	/** Common Clear */
	$scope.fnClear 				= function() { fnClear(); };
	/** 업로드 파일첨부 버튼 */
	$scope.fnExcelUpload 		= function() { fnExcelUpload(); };
	/** 업로드 파일첨부 삭제 버튼 */
	$scope.fnExcelUploadDelete	= function(event) { fnExcelUploadDelete(event); };
	/** 업로드 버튼 */
	$scope.fnExcelUploadConfirm	= function() { fnExcelUploadConfirm(); };
	/** Sample 다운로드 버튼 */
	$scope.fnSampleExcelDownload = function() { fnSampleExcelDownload(); };
	/** 파일첨부 클릭시 파일버튼 */
	$scope.fnExcelExport 		= function(event) { fnExcelExport(event); };

	$scope.fnExcelUploadRun = function(){	 fnExcelUploadRun();	};

	$scope.fnBtnExcelSelect = function(fileType) {$('#' + fileType).trigger('click');};
	//------------------------------- Html 버튼 바인딩  End ----------------------------------------

}); // document ready - END
