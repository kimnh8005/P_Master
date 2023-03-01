/**-----------------------------------------------------------------------------
 * description 		 : 외부몰관리 > 외부몰주문관리 > 외부몰 주문 엑셀업로드 내역
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.20		이원호          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var publicStorageUrl = fnGetPublicStorageUrl();
var gAdminSearchValue = "";
var gFileTagId;
var gFile;


$(document).ready(function() {
	importScript("/js/service/od/ifday/ifDayUploadListGridColumns.js");

	fnInitialize();	// Initialize Page Call ---------------------------------

	// Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'ifDayChangeUpload',
			callback : fnUI
		});
	}

	function fnUI(){
		fnInitButton();	// Initialize Button ---------------------------------
		fnInitGrid();	// Initialize Grid ------------------------------------
		fnInitOptionBox();// Initialize Option Box
		fnDefaultSet();
		fnInitKendoUpload();
		fnSearch();
	}

	// --------------------------------- Button
	// Start---------------------------------
	function fnInitButton(){
		$('#fnSearch, #fnClear, #fnEmployeeSearchPopup, #fnExcelUpload, #fnSampleDownload').kendoButton();
	}

	function fnSearch(){
		var data = $('#searchForm').formSerialize(true);
		data.adminSearchValue = gAdminSearchValue;

		var query = {
					page         : 1,
					pageSize     : PAGE_SIZE,
					filterLength : fnSearchData(data).length,
					filter :  {
						filters : fnSearchData(data)
					}
		};
		aGridDs.query( query );
	}

	function fnClear(){
		$('#searchForm').formClear(true);
		fnDefaultSet();
	}

    function fnEmployeeSearchPopup(apprManagerType){

    	document.getElementById("searchAdmin").value = '';

        fnKendoPopup({
            id     : 'employeeSearchPopup',
            title  : 'BOS 계정 선택',
            src    : '#/employeeSearchPopup',
            width  : '1050px',
            height : '800px',
            scrollable : "yes",
            success: function( stMenuId, data ){
            	if(data.userId != undefined){
            		document.getElementById("searchAdmin").value = "[" + data.userId + "] " + data.userName;
            		gAdminSearchValue = data.userId;
            	}
            }
        });
    }

    // 옵션 초기화
	function fnInitOptionBox(){
	    // 연동 상태
		fnKendoDropDownList({
            id: "outMallType",
            data: [
                { "CODE" : "E", "NAME" : "이지어드민" }
                , { "CODE" : "S", "NAME" : "사방넷" }
            ],
            blank:'양식 전체'
        });

        // 관리자

        // 조회 시작 일자
        fnKendoDatePicker({
            id: "createStartDate",
            format: "yyyy-MM-dd",
            btnStartId: "createStartDate",
            btnEndId: "createEndDate",
            defVal : fnGetDayMinus(fnGetToday(),6),
            defType : 'oneWeek'
        });

        // 조회 종료 일자
        fnKendoDatePicker({
            id: "createEndDate",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "createStartDate",
            btnEndId: "createEndDate",
            defVal: fnGetToday(),
            defType : 'oneWeek'
        });

	};

	// 기본 설정
	function fnDefaultSet(){
		//$("#createStartDate").data("kendoDatePicker").value(fnGetToday());
        //$("#createEndDate").data("kendoDatePicker").value(fnGetToday());
        gAdminSearchValue = "";
	};


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
			url         : '/admin/order/ifDay/addIfDayExcelUpload'
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

				fnSearch();
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

	function fnSampleDownload(){
		document.location.href = "/contents/excelsample/order/interfaceDayChangeSample.xlsx"
	}



	// --------------------------------- Button
	// End---------------------------------


	// ------------------------------- Grid Start
	// -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : "/admin/order/ifDay/getIfDayExcelInfoList",
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 10
			}
			,navigatable: true
			,height: 100
			,columns   : ifDayUploadListGridUtil.orderUploadList()
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		aGrid.bind("dataBound", function() {
			var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(row_num);
				row_num--;
			});

        	$('#totalCnt').text(aGridDs._total);
        });

        // 실패내역 다운로드 버튼 이벤트.
        $('#aGrid').on("click", "button[kind=btnExcelDown]", function(e) {
            e.preventDefault();
            let btnObj = $(this);
            let dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));
			let failType = $(this).data("failtype");
            let data = { "ifIfDayExcelInfoId" : dataItem.ifIfDayExcelInfoId, "failType": failType};
            fnExcelDownload('/admin/order/ifDay/getIfDayFailExcelDownload', data, btnObj);
        });

		// 업로드 엑셀 파일명 클릭시 다운로드 처리 이벤트.
		$('#aGrid').on("click", "div.divExcelDownClick", function(e) {
			e.preventDefault();
			let btnObj = $(this);
			let dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));
			let failType = $(this).data("failtype");
			let data = { "ifIfDayExcelInfoId" : dataItem.ifIfDayExcelInfoId, "failType": failType};
			fnExcelDownload('/admin/order/ifDay/getIfDayFailExcelDownload', data, btnObj);
		});

	}

	// ------------------------------- Grid End -------------------------------

	// ------------------------------- Html 버튼 바인딩 Start
	// -------------------------------

	/** Common Search */
	$scope.fnSearch = function(){	fnSearch();	};
	/** Common Clear */
	$scope.fnClear = function(){	 fnClear();	};
	/** Button fnEmployeeSearchPopup */
	$scope.fnEmployeeSearchPopup = function(){	 fnEmployeeSearchPopup();};

	/** Button excelSelect */
	$scope.fnBtnExcelSelect = function(fileType) {$('#' + fileType).trigger('click');};
	/** Button excelUpload */
	$scope.fnExcelUploadRun = function(){	 fnExcelUploadRun();	};

	$scope.fnSampleDownload = function() { fnSampleDownload(); };
	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END
