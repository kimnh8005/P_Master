/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 관리자공지사항상세
 *
 * @ 실제 처리는 noticePopupController.js, noticePopup.html
 *
 * @ 수정일        수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.04.16    dgyoun        최초생성
 * @
 ******************************************************************************/
'use strict';

var pageParam = fnGetPageParam();


$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });
		$("#lnb, #lnb-closeBtn").css({"display": "none"});

		fnPageInfo({
			PG_ID  : 'noticePopupView',
			callback : fnUI
		});
	}

	function fnUI(){

	   	fnDefaultValue();

	   	fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------
	}

	//--------------------------------- Button Start---------------------------------

	function fnDefaultValue(){
		// 게시글 상세
		fnAjax({
			url     : 'admin/cs/notice/getNotice',
			params  : {csCompanyBbsId : pageParam.csCompanyBbsId, viewMode : 'Y'},
			success :
				function( data ){
					// 게시글 정보
					$("#inputOrgBbsId").val(data.rows.csCompanyBbsId);
					$("#spanTitle").html(data.rows.title);

					$("#spanContent").html(fnTagConvert(data.rows.content));

					// 첨부파일 정보
					if(data.rowsFile != null && data.rowsFile != null){
						var fileList = "";
						fileList = fileList + data.rowsFile.physicalAttached+"\n";
						$("#inputOrgBbsAttcId").val(data.rowsFile.csCompanyBbsAttachId);
						$("span#V_CS_BBS_ATTC").html(fileList);
						//$('#attcDown').attr('href', "/comn/getView?FILE_NAME="+data.rowsFile.realAttached+"&ORG_FILE_NAME="+data.rowsFile.physicalAttached+"&SUB_PATH=CS//compBbs//1//");
						$('#attcDown').attr('href', "javascript:$scope.attchFileDownload('" + data.rowsFile.filePath + "', '" + data.rowsFile.realAttached + "', '" + data.rowsFile.physicalAttached + "')");
						$('#attcDown').css("display" ,"");
					}else{
						$("span#V_CS_BBS_ATTC").html('첨부한 파일이 없습니다.');
						$('#attcDown').css("display" ,"");
					}
				},
			isAction : 'batch'
		});
		// 이전글, 다음글 정보
		fnAjax({
			url     : '/admin/cs/notice/getNoticePreNextList',
			params  : {csCompanyBbsId : pageParam.csCompanyBbsId},
			success :
				function( data ){
					for(var i=0; i<data.rows.length; i++){
						if(data.rows[i].preNextType == 'PRE') {
							$("#prevBbs").html(data.rows[i].title);
							let prevCsCompanyBbsId = data.rows[i].csCompanyBbsId

							$('#prevBbs').on('click', function(e){
								e.preventDefault();
								fnGoDetail(prevCsCompanyBbsId);
							});
						} else if(data.rows[i].preNextType == 'NEXT') {
							$("#nextBbs").html(data.rows[i].title);
							let nextCsCompanyBbsId = data.rows[i].csCompanyBbsId

							$('#nextBbs').on('click', function(e){
								e.preventDefault();
								fnGoDetail(nextCsCompanyBbsId);
							});
						}
					}
				},
			isAction : 'batch'
		});
	}

	function fnInitButton(){
	}

    // 상세화면 이동
	function fnGoDetail(csCompanyBbsId){
		var option = new Object();
    	option.url = "#/noticePopupView";
    	option.menuId = 200;
    	option.data = {
    		csCompanyBbsId	:csCompanyBbsId,
		};
    	$scope.$emit('goPage', option);
    }

	//--------------------------------- Button End---------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

	}
	//---------------Initialize Option Box End ------------------------------------------------

	//-------------------------------  Common Function start -------------------------------

     /**
	 * 콜백합수
	 */
	function fnBizCallback( id, data ){
	}


	/**
	 * 첨부파일 다운로드
	 */
	function attchFileDownload(filePath, physicalFileName, originalFileName) {
		var opt = {
				filePath: filePath,
				physicalFileName: physicalFileName,
			    originalFileName: originalFileName
		}

		fnDownloadPublicFile(opt);
	}


    //-------------------------------  Common Function end -------------------------------


    //------------------------------- Html 버튼 바인딩  Start -------------------------------
    /** Common GoDetail*/
    $scope.fnGoDetail = function(csCompanyBbsId){  fnGoDetail(csCompanyBbsId);};
    /** Common GoDetail*/
    $scope.attchFileDownload = function(filePath, physicalFileName, originalFileName){  attchFileDownload(filePath, physicalFileName, originalFileName);};

    //------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END