/**-----------------------------------------------------------------------------
 * description 		 : 공지사항 상세 < 거래처 공지/문의 < 거래처 관리
 * description 		 : 공지사항(입점사 또는 공급사) 상세 < 공지/문의
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2017.02.21		김아란          최초생성
 * @
 * **/
'use strict';

var pageParam = fnGetPageParam();

// ----------------------------------------------------------------------------
// noticePopupViewController 에서 진입 여부 확인
// ----------------------------------------------------------------------------
var gbPageTp = '';

try {
  if (gbPageTp == undefined) {
    //console.log('# gbPageTp is Null');
  }
  else {
    if (gbPageId == 'noticePopupView') {
      gbPageTp = gbPageId;
    }
    else {
      //console.log('# gbPageTp is Not noticeView');
    }
  }
}
catch (error) {
  //console.log('# gbPageTp is Null 2');
}

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'noticePopup',
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

					$("#spanCompanyBbsType").html(data.rows.companyBbsTypeName);
					$("#spanNotificationYn").html(data.rows.notificationYn);


					if(data.rows.popupYn == 'Y') {
						$("#spanPopupYn").html(data.rows.popupYn);
						$("#spanPopupDisplayDate").html('(' + data.rows.popupDisplayStartDate + ' ~ ' + data.rows.popupDisplayEndDate + ')');
						$("#spanPopupCoordinate").html('상단부터 : ' + data.rows.popupCoordinateY + 'px,&nbsp;좌측부터 : ' + data.rows.popupCoordinateX + 'px');
						$("#spanPopupDisplayTodayYn").html(data.rows.popupDisplayTodayYn);
						$(".showPopup").show();
						$(".hidePopup").hide();
					} else {
						$("#spanHidePopupYn").html(data.rows.popupYn);
						$(".showPopup").hide();
						$(".hidePopup").show();
					}

					$("#spanUseYn").html(data.rows.useYn);

					$("#spanCreateId").html(data.rows.createName);
					$("#spanModifyId").html(data.rows.modifyName);
					$("#spanCreateDate").html(data.rows.createDate);
					$("#spanModifyDate").html(data.rows.modifyDate);

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

					// ------------------------------------------------------------------
					// 버튼 노출/숨김 처리
					// ------------------------------------------------------------------
					fnButtonDisplay();
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
		$('#fnMod, #fnDel, #fnList').kendoButton();
		var ssRoleId = PG_SESSION.roleId;
		if(ssRoleId == '10' || ssRoleId == '11'){
			$("#fnMod").css("display", "none");
			$("#fnDel").css("display", "none");
		}
	}
	function fnNew(){

	}
	function fnMod(){
		var option = new Object();
    	option.url = "#/noticeAdd";
    	option.menuId = 349;
    	option.data = {
    					editMode		:"U",
    					csCompanyBbsId	:pageParam.csCompanyBbsId,
    					companyBbsType	:pageParam.companyBbsType,
    					useYn			:pageParam.useYn,
    					popupYn			:pageParam.popupYn,
    					conditionType	:pageParam.conditionType,
    					conditionValue	:pageParam.conditionValue,
    					startCreateDate	:pageParam.startCreateDate,
    					endCreateDate	:pageParam.endCreateDate
        				};
    	$scope.$emit('goPage', option);
	}
    function fnDel(){
    	fnKendoMessage({message:'해당 게시물을 삭제하시겠습니까?', type : "confirm" ,ok :fnDelApply  });
    }
    function fnDelApply(){
    	var url  = '/admin/cs/notice/delNotice';
    	var cbId = 'delete';
    	var dataArray = new Array();
    	dataArray.push({"csCompanyBbsId":pageParam.csCompanyBbsId});

    	fnAjax({
			url     : url,
			params  : {deleteData :kendo.stringify(dataArray)},
			success :
				function( data ){
				fnBizCallback(cbId, data);
			},
			isAction : 'delete'
		});
    }
    // 목록화면 이동
    function fnList(){
    	var option = new Object();
    	option.url = "#/notice";
    	option.menuId = 179;
    	option.data = {
		    			companyBbsType	:pageParam.companyBbsType,
						useYn			:pageParam.useYn,
						popupYn			:pageParam.popupYn,
						conditionType	:pageParam.conditionType,
						conditionValue	:pageParam.conditionValue,
						startCreateDate	:pageParam.startCreateDate,
						endCreateDate	:pageParam.endCreateDate
        				};
    	$scope.$emit('goPage', option);
    }
    // 상세화면 이동
	function fnGoDetail(csCompanyBbsId){
		var option = new Object();
    	option.url = "#/noticePopup";
    	option.menuId = 200;
    	option.data = { csCompanyBbsId	:csCompanyBbsId,
		    			companyBbsType	:pageParam.companyBbsType,
						useYn			:pageParam.useYn,
						popupYn			:pageParam.popupYn,
						conditionType	:pageParam.conditionType,
						conditionValue	:pageParam.conditionValue,
						startCreateDate	:pageParam.startCreateDate,
						endCreateDate	:pageParam.endCreateDate
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
		switch(id){
			case 'delete':
                fnKendoMessage({
                    message : '삭제되었습니다.'
                    , ok : function() { $('#fnList').trigger('click'); }
                });
				break;
		}
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

		console.log("@@@@@@@@@@@@@ ", opt);

		fnDownloadPublicFile(opt);
	}

	// ==========================================================================
	// # 버튼 노출/숨김 제어
	// ==========================================================================
	function fnButtonDisplay() {
    // 대시보드 버튼 제어 처리 영역
     if (gbPageTp == 'noticePopupView') {
       // 대시보드에서 호출 : 읽기모드
       $('#buttonSpan').hide();
     }
     else {
       $('#buttonSpan').show();
     }
	}

    //-------------------------------  Common Function end -------------------------------


    //------------------------------- Html 버튼 바인딩  Start -------------------------------
    /** Common Clear*/
    $scope.fnClear =function(){	 fnClear();};
    /** Common New*/
    $scope.fnNew = function( ){	fnNew();};
    /** Common Mod */
    $scope.fnMod = function( ){  fnMod();};
    /** Common Delete*/
    $scope.fnDel = function(){	 fnDel();};
    /** Common List*/
    $scope.fnList = function( ){  fnList();};
    /** Common GoDetail*/
    $scope.fnGoDetail = function(csCompanyBbsId){  fnGoDetail(csCompanyBbsId);};
    /** Common GoDetail*/
    $scope.attchFileDownload = function(filePath, physicalFileName, originalFileName){  attchFileDownload(filePath, physicalFileName, originalFileName);};

    //------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
