/**-----------------------------------------------------------------------------
 * description 		 : 승인관리자 선택
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.27 	박승현          최초생성
 * @
 * **/
"use strict";

var paramData = parent.POP_PARAM["parameter"]; // 파라미터
var viewModel; // viewModel
var subApprCount = 0;
//업무별 1차 승인관리자 미지정 등록
const unselectAuth1st = {
	"adminTypeName": "",
	"apprUserId": "-1",
	"apprUserInfo": "미지정",
	"apprUserName": "",
	"apprLoginId": "",
	"userStatus": "EMPLOYEE_STATUS.NORMAL",
	"organizationName": "",
	"userStatusName": "",
	"teamLeaderYn": "",
	"grantAuthYn": "N",
	"grantUserName": undefined,
	"grantLoginId": undefined,
	"grantAuthStartDt": undefined,
	"grantAuthEndDt": undefined,
	"grantUserStatusName": ""
};

$(document).ready(function() {

    fnInitialize();	//Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : false });

        fnPageInfo({
            PG_ID  : "approvalManagerSearchPopup",
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
            approvalAuthManagerHistoryByTaskInfo : { // 업무별 직전 승인관리자 이력
            	approvalSubUserId : null, // 직전 1차 승인관리자 SEQ
            	approvalUserId : null // 직전 최종 승인관리자 SEQ
            },
            newCreate : true, // 신규 여부
            fnGetApprovalAuthManagerHistoryByTaskInfo : function( dataItem ){ // 업무별 승인관리자 상세정보 조회
            	fnAjax({
            		url     : "/admin/approval/auth/getApprovalAuthManagerHistoryByTask",
            		params  : dataItem,
            		method : "GET",
            		success : function( data ){
            			viewModel.set("approvalAuthManagerHistoryByTaskInfo", data);
	            	},
	            	isAction : "select"
	            });

            },
            fnGetApprovalAuthByTaskInfo : function( dataItem ){ // 업무별 승인관리자 상세정보 조회
            	fnAjax({
                	url     : "/admin/approval/auth/getApprovalAuthByTaskInfo",
                    params  : dataItem,
                    method : "GET",
                    success : function( data ){
                    	viewModel.set("approvalAuthByTaskInfo", data);
                    	let same1stReqApprUserIndex = null;
						let same2stReqApprUserIndex = null;

                    	for(var i=0; i < data.authManager1stList.length; i++){
							if('EMPLOYEE_STATUS.NORMAL' == data.authManager1stList[i].userStatus
								|| 'EMPLOYEE_STATUS.TEMPORARY_STOP' == data.authManager1stList[i].userStatus
								|| (data.authManager1stList[i].grantAuthYn == 'Y' &&
										('EMPLOYEE_STATUS.NORMAL' == data.authManager1stList[i].grantUserStatus
										|| 'EMPLOYEE_STATUS.TEMPORARY_STOP' == data.authManager1stList[i].grantUserStatus)
									)
								) {
								data.authManager1stList[i].apprAuthYn = 'Y';
								data.authManager1stList[i].apprUserInfo = data.authManager1stList[i].apprUserName + "/" + data.authManager1stList[i].apprLoginId

								if (data.apprReqLoginId == data.authManager1stList[i].apprLoginId) {
									data.authManager1stList[i].apprAuthYn = 'N';
								}

							}else{
								data.authManager1stList[i].apprAuthYn = 'N';
								data.authManager1stList[i].apprUserInfo = data.authManager1stList[i].apprUserName + "/" + data.authManager1stList[i].apprLoginId
							}

							//[HGRM-8220] [기획전승인] 승인관리자 선택팝업 > 위임 중지, 위임기간 종료되었으나, 종료된 정보가 남아 있음, 수정처리 21-05-24 임상건
							if(data.authManager1stList[i].grantAuthYn == 'N'){
								data.authManager1stList[i].grantAuthStartDt = undefined;
								data.authManager1stList[i].grantAuthEndDt = undefined;
								data.authManager1stList[i].grantLoginId = undefined;
								data.authManager1stList[i].grantUserName = undefined;
								data.authManager1stList[i].grantUserStatus = undefined;
								data.authManager1stList[i].grantUserStatusName = undefined;
							}
						}

						data.authManager1stList.unshift(unselectAuth1st);

                    	viewModel.approvalAuthByTaskInfo.set("authManager1stList", data.authManager1stList);	//업무별 1차 승인관리자 목록

						if(data.authManager1stList.length > 0){
							viewModel.approvalAuthByTaskInfo.set("isAuthManager1stNoDataTbody", false);
							viewModel.approvalAuthByTaskInfo.set("isAuthManager1stTbody", true);
						}
						else{
							viewModel.approvalAuthByTaskInfo.set("isAuthManager1stNoDataTbody", true);
							viewModel.approvalAuthByTaskInfo.set("isAuthManager1stTbody", false);
						}

						for(var i=0; i < data.authManager2ndList.length; i++){
							if('EMPLOYEE_STATUS.NORMAL' == data.authManager2ndList[i].userStatus
								|| 'EMPLOYEE_STATUS.TEMPORARY_STOP' == data.authManager2ndList[i].userStatus
								|| (data.authManager2ndList[i].grantAuthYn == 'Y' &&
										('EMPLOYEE_STATUS.NORMAL' == data.authManager2ndList[i].grantUserStatus
										|| 'EMPLOYEE_STATUS.TEMPORARY_STOP' == data.authManager2ndList[i].grantUserStatus)
									)
								) {
								data.authManager2ndList[i].apprAuthYn = 'Y';

								if (data.apprReqLoginId == data.authManager2ndList[i].apprLoginId) {
									data.authManager2ndList[i].apprAuthYn = 'N';
								}
							}else{
								data.authManager2ndList[i].apprAuthYn = 'N';
							}

							//[HGRM-8220] [기획전승인] 승인관리자 선택팝업 > 위임 중지, 위임기간 종료되었으나, 종료된 정보가 남아 있음, 수정처리 21-05-24 임상건
							if(data.authManager2ndList[i].grantAuthYn == 'N'){
								data.authManager2ndList[i].grantAuthStartDt = undefined;
								data.authManager2ndList[i].grantAuthEndDt = undefined;
								data.authManager2ndList[i].grantLoginId = undefined;
								data.authManager2ndList[i].grantUserName = undefined;
								data.authManager2ndList[i].grantUserStatus = undefined;
								data.authManager2ndList[i].grantUserStatusName = undefined;
							}
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

                    	var approvalSubUserId = viewModel.approvalAuthManagerHistoryByTaskInfo.get('approvalSubUserId');
                    	var approvalUserId = viewModel.approvalAuthManagerHistoryByTaskInfo.get('approvalUserId');

						$("input:radio[name ='selSubApprManager']:input[value='-1']").attr("checked", true); //1차 승인자 default 선택

						data.authManager1stList.forEach((obj, idx) => {
							if (obj.apprAuthYn == "N")
								$('#authManager1stbody > tr:eq(' + (idx) + ')').css('color', 'rgb(222, 222, 222)');	//정지 사용자 disable 처리
							if (obj.apprAuthYn == "Y" && approvalSubUserId == obj.apprUserId)
								$("input:radio[name ='selSubApprManager']:input[value='" + approvalSubUserId + "']").attr("checked", true);	//이전 승인 이력 선택
						});

						data.authManager2ndList.forEach((obj, idx) => {
							if (obj.apprAuthYn == "N")
								$('#authManager2ndbody > tr:eq(' + (idx) + ')').css('color', 'rgb(222, 222, 222)');	//정지 사용자 disable 처리
							if (obj.apprAuthYn == "Y" && approvalUserId == obj.apprUserId)
								$("input:radio[name ='selApprManager']:input[value='" + approvalUserId + "']").attr("checked", true); //이전 승인 이력 선택
						});

                    },
                    isAction : "select"
                });
			}
        });
        kendo.bind($("#inputForm"), viewModel);
    };

    // 기본값 셋팅
    function fnDefaultValue(){
        if( !paramData.taskCode ){
            viewModel.set("newCreate", true);
        }else{
            viewModel.set("newCreate", false);
            viewModel.fnGetApprovalAuthManagerHistoryByTaskInfo( paramData );
            viewModel.fnGetApprovalAuthByTaskInfo( paramData );
        }
    };

    // 등록
    function fnSave(){

    	var apprSubManager = $(':radio[name="selSubApprManager"]:checked').val();
    	var apprManager = $(':radio[name="selApprManager"]:checked').val();

//		if((fnIsEmpty(apprSubManager)&&subApprCount>0) || fnIsEmpty(apprManager)){
    	/*
    	 * 2021.02.23 16시 긴급 요청 사항.
    	 * 1차 승인관리자는 선택사항으로 변동됨 (이전에는 등록된 1차 승인관리자 목록이 있으며, 승인가능한 상태의 관리자가 있는 경우에는 필수 였음)
    	 * 추후 각각 업무에 따라 선택, 혹은 이전처럼 부분필수 가 될 예정임. 추후 개발자는 변경 되는 방식에 따라 분기처리하시면 될 듯 합니다.
    	 */
    	if(fnIsEmpty(apprManager)){
			fnKendoMessage({ message : "승인관리자를 선택해주세요" });
			return false;
		}

		var subApprIndex = $("#inputForm").find(":radio[name='selSubApprManager']:checked").closest("tr").index();
		var apprIndex = $("#inputForm").find(":radio[name='selApprManager']:checked").closest("tr").index();

//		console.log("authManager1stList["+JSON.stringify(viewModel.approvalAuthByTaskInfo.get('authManager1stList')[subApprIndex])+"]");
//		console.log("authManager1stList["+JSON.stringify(viewModel.approvalAuthByTaskInfo.get('authManager2ndList')[apprIndex])+"]");

		var dataItem = {};

		if (apprSubManager == -1) dataItem.authManager1st = {}
		else dataItem.authManager1st = (viewModel.approvalAuthByTaskInfo.get('authManager1stList')[subApprIndex]);

		dataItem.authManager2nd = (viewModel.approvalAuthByTaskInfo.get('authManager2ndList')[apprIndex]);
		dataItem.isCompleteProcess = true;

		if(dataItem){
			parent.POP_PARAM = dataItem;
		}
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
    };

    //-------------------------------  Common Function end -------------------------------

    //------------------------------- Html 버튼 바인딩  Start -------------------------------

    $scope.fnSave = function(){ fnSave(); }; // 등록
    $scope.fnClose = function(){ fnClose(); }; // 닫기

    //------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
