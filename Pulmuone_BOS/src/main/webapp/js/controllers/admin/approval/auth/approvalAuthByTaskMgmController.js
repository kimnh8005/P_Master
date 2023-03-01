/**-----------------------------------------------------------------------------
 * description 		 : 업무별 승인관리자 정보
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.14 	박승현          최초생성
 * @
 * **/
"use strict";

var paramData = parent.POP_PARAM["parameter"]; // 파라미터
var viewModel; // viewModel

$(document).ready(function() {

    fnInitialize();	//Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : false });

        fnPageInfo({
            PG_ID  : "approvalAuthByTaskMgm",
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){

        fnInitButton();	// Initialize Button  ---------------------------------
        fnViewModelInit();
        fnDefaultValue();
    };

    //--------------------------------- Button Start---------------------------------

    // 버튼 초기화
    function fnInitButton(){
        $("#fnSave").kendoButton();
        $("#fnEmployeeSearchPopup").kendoButton();
    };

    // 팝업 닫기
    function fnClose(){
    	parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
    };

    // 저장
    function fnSave(){
        let data = $("#inputForm").formSerialize(true);
        if( data.rtnValid ){
            if( fnSaveValid() ){
                let url = "";
                var paramData = viewModel.get("approvalAuthByTaskInfo");
                url = "/admin/approval/auth/putApprovalAuthByTaskInfo";
                fnAjax({
                    url     : url,
                    params  : paramData,
    	            contentType : "application/json",
                    success : function( data ){
                        fnKendoMessage({  message : "저장 완료되었습니다."
                                        , ok : function(){
                                            parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
                                          }
                                       });
                    },
                    error : function(xhr, status, strError){
                        fnKendoMessage({ message : xhr.responseText });
                    },
                    isAction : "insert"
                });
            }
        }
    };

    //--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------
    //------------------------------- Grid End -------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    //---------------Initialize Option Box End ------------------------------------------------


    //-------------------------------  Common Function start -------------------------------
    // viewModel 초기화
    function fnViewModelInit(){
        viewModel = new kendo.data.ObservableObject({
        	approvalAuthByTaskInfo : { // 업무별 승인관리자 정보 탬플릿
        		taskCode : null, // 승인권한관리 대상 업무 코드

        		isAuthManager1stNoDataTbody	: true,							//업무별 1차 승인관리자 목록 > NoData Tbody Visible
				isAuthManager1stTbody		: false,						//업무별 1차 승인관리자 목록 > Data Tbody Visible
				authManager1stList			: [],							//업무별 1차 승인관리자 목록 > 업무별 1차 승인관리자 목록, authManager1st-row-template 사용

				isAuthManager2ndNoDataTbody	: true,							//업무별 2차 승인관리자 목록 > NoData Tbody Visible
				isAuthManager2ndTbody		: false,						//업무별 2차 승인관리자 목록 > Data Tbody Visible
				authManager2ndList			: []							//업무별 2차 승인관리자 목록 > 업무별 1차 승인관리자 목록, authManager2nd-row-template 사용
            },
            newCreate : true, // 신규 여부
            fnGetApprovalAuthByTaskInfo : function( dataItem ){ // 업무별 승인관리자 상세정보 조회
            	fnAjax({
                	url     : "/admin/approval/auth/getApprovalAuthByTaskInfo",
                    params  : dataItem,
                    method : "GET",
                    success : function( data ){
                    	viewModel.set("approvalAuthByTaskInfo", data);

                    	viewModel.approvalAuthByTaskInfo.set("authManager1stList", data.authManager1stList);	//업무별 1차 승인관리자 목록

						if(data.authManager1stList.length > 0){
							viewModel.approvalAuthByTaskInfo.set("isAuthManager1stNoDataTbody", false);
							viewModel.approvalAuthByTaskInfo.set("isAuthManager1stTbody", true);
						}
						else{
							viewModel.approvalAuthByTaskInfo.set("isAuthManager1stNoDataTbody", true);
							viewModel.approvalAuthByTaskInfo.set("isAuthManager1stTbody", false);
						}

						viewModel.approvalAuthByTaskInfo.set("authManager2ndList", data.authManager2ndList);	//업무별 2차 승인관리자 목록

						if(data.authManager2ndList.length > 0){
							viewModel.approvalAuthByTaskInfo.set("isAuthManager2ndNoDataTbody", false);
							viewModel.approvalAuthByTaskInfo.set("isAuthManager2ndTbody", true);
						}
						else{
							viewModel.approvalAuthByTaskInfo.set("isAuthManager2ndNoDataTbody", true);
							viewModel.approvalAuthByTaskInfo.set("isAuthManager2ndTbody", false);
						}

						if(fnIsProgramAuth('DELETE')) {
							$('.fnRemoveAuthManager2nd').show();
							$('.fnRemoveAuthManager1st').show();
						} else {
							$('.fnRemoveAuthManager2nd').hide();
							$('.fnRemoveAuthManager1st').hide();
						}

                    },
                    isAction : "select"
                });
            },
            //1차 승인관리자 삭제 버튼 이벤트
            fnRemoveAuthManager1st : function(e) {
            	fnKendoMessage({
					type : "confirm",
					message : "1차 승인관리자를 삭제하시겠습니까?",
					ok : function() {
						var index = viewModel.approvalAuthByTaskInfo.get("authManager1stList").indexOf(e.data);		// e.data : "삭제" 버튼을 클릭한 행의 승인관리자 세부항목
						viewModel.approvalAuthByTaskInfo.get("authManager1stList").splice(index, 1);				// viewModel 에서 삭제

						if(viewModel.approvalAuthByTaskInfo.get("authManager1stList").length == 0){				//모든 1차 승인관리자 리스트를 다 삭제하면 NoData Tbody를 보여준다.
							viewModel.approvalAuthByTaskInfo.set("isAuthManager1stNoDataTbody", true)				//1차 승인관리자 > NoData Tbody Visible
							viewModel.approvalAuthByTaskInfo.set("isAuthManager1stTbody", false)					//1차 승인관리자 > Data Tbody Visible
						}
					},
					cancel : function() {
						return;
					}
				});
			},
			//2차 승인관리자 삭제 버튼 이벤트
			fnRemoveAuthManager2nd : function(e) {
				fnKendoMessage({
					type : "confirm",
					message : "최종 승인관리자를 삭제하시겠습니까?",
					ok : function() {
						var index = viewModel.approvalAuthByTaskInfo.get("authManager2ndList").indexOf(e.data);		// e.data : "삭제" 버튼을 클릭한 행의 승인관리자 세부항목
						viewModel.approvalAuthByTaskInfo.get("authManager2ndList").splice(index, 1);				// viewModel 에서 삭제

						if(viewModel.approvalAuthByTaskInfo.get("authManager2ndList").length == 0){				//모든 2차 승인관리자 리스트를 다 삭제하면 NoData Tbody를 보여준다.
							viewModel.approvalAuthByTaskInfo.set("isAuthManager2ndNoDataTbody", true)				//2차 승인관리자 > NoData Tbody Visible
							viewModel.approvalAuthByTaskInfo.set("isAuthManager2ndTbody", false)					//2차 승인관리자 > Data Tbody Visible
						}
					},
					cancel : function() {
						return;
					}
				});
			}
        });
        kendo.bind($("#inputForm"), viewModel);
    };
    function fnEmployeeSearchPopup(apprManagerType){
		fnKendoPopup({
			id     : 'employeeSearchPopup',
			title  : 'BOS 계정 선택',
			src    : '#/employeeSearchPopup',
			width  : '1050px',
			height : '800px',
            scrollable : "yes",
			success: function( stMenuId, data ){
				if(!fnIsEmpty(data.userId)){
					var authManagerList = viewModel.approvalAuthByTaskInfo.get('authManager2ndList');
					if(apprManagerType == "APPR_MANAGER_TP.FIRST"){
						authManagerList =  viewModel.approvalAuthByTaskInfo.get('authManager1stList');
						viewModel.approvalAuthByTaskInfo.set("isAuthManager1stNoDataTbody", false)			//승인관리자 > NoData Tbody Visible
						viewModel.approvalAuthByTaskInfo.set("isAuthManager1stTbody", true)					//승인관리자 > Data Tbody Visible
					}else{
						viewModel.approvalAuthByTaskInfo.set("isAuthManager2ndNoDataTbody", false)			//승인관리자 > NoData Tbody Visible
						viewModel.approvalAuthByTaskInfo.set("isAuthManager2ndTbody", true)					//승인관리자 > Data Tbody Visible
					}

					var authManagerListLength = authManagerList.length;

					var organizationName = data.organizationName;
					/*if(data.adminType = "COMPANY_TYPE.CLIENT"){
						organizationName = data.clientName;
					}*/
					var isDuplicateUser = false;
					for(var i=0; i < viewModel.approvalAuthByTaskInfo.get('authManager1stList').length; i++){
						if(data.userId == viewModel.approvalAuthByTaskInfo.get('authManager1stList')[i].apprUserId){	//이미 들어가 있는 승인관리자를 체크
							isDuplicateUser = true;
							break;
						}
					}
					for(var i=0; i < viewModel.approvalAuthByTaskInfo.get('authManager2ndList').length; i++){
						if(data.userId == viewModel.approvalAuthByTaskInfo.get('authManager2ndList')[i].apprUserId){	//이미 들어가 있는 승인관리자를 체크
							isDuplicateUser = true;
							break;
						}
					}
					if(!isDuplicateUser){
						authManagerList.push({
							apprUserId : data.userId,				//승인관리자
							adminTypeName : data.adminTypeName,		//유형
							apprLoginId : data.loginId,				//승인관리자ID
							apprUserName : data.userName,			//승인관리자명
							organizationName : organizationName,	//조직/거래처 정보
							userStatusName : data.userStatusName,	//BOS 계정상태
							userStatus : data.userStatus,	//BOS 계정상태
							sort : 999,								//순번
						});
					}else{
						fnKendoMessage({ message : "중복된 승인관리자입니다." });
					}
				}else{
//					fnKendoMessage({ message : "올바르지 않은 관리자입니다." });
				}
			}
		});
	}

    // 기본값 셋팅
    function fnDefaultValue(){
        if( !paramData.taskCode ){
            viewModel.set("newCreate", true);
        }else{
            viewModel.set("newCreate", false);
            viewModel.fnGetApprovalAuthByTaskInfo( paramData );
        }
        if(fnIsProgramAuth("DELETE")) {
        	$('#fnRemoveAuthManager2nd').show();
		} else {
			$('#fnRemoveAuthManager2nd').hide();
		}
    };

    // 입력값 검증
    function fnSaveValid(){

		var authManagerList = viewModel.approvalAuthByTaskInfo.get('authManager1stList');
		for(var i=0; i < authManagerList.length; i++){
			if(fnIsEmpty(authManagerList[i].apprUserId)){
				fnKendoMessage({ message : "저장할 값이 없습니다." });
				return false;
			}
		}

		var checkValid = true;
		var authManagerNormalCnt = 0;
		authManagerList = viewModel.approvalAuthByTaskInfo.get('authManager2ndList');
		if(!authManagerList || !authManagerList.length){
			checkValid = false;
		}
		for(var i=0; i < authManagerList.length; i++){
			if(fnIsEmpty(authManagerList[i].apprUserId)){
				checkValid = false;
				break;
			}
			if("EMPLOYEE_STATUS.NORMAL" == authManagerList[i].userStatus
				|| "EMPLOYEE_STATUS.TEMPORARY_STOP" == authManagerList[i].userStatus
					){
				authManagerNormalCnt++;
			}
		}
		if(!checkValid || authManagerNormalCnt < 1 ){
			fnKendoMessage({ message : "최종승인관리자는 1명이상 등록되거나 정상상태이어야 합니다" });
			return false;
		}

        return true;
    };

    //-------------------------------  Common Function end -------------------------------


    //------------------------------- Html 버튼 바인딩  Start -------------------------------

    $scope.fnSave = function(){ fnSave(); }; // 저장
    $scope.fnClose = function(){ fnClose(); }; // 닫기
	$scope.fnEmployeeSearchPopup = function(apprManagerType){	 fnEmployeeSearchPopup(apprManagerType);};

    //------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
