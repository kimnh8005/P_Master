/**-----------------------------------------------------------------------------
 * description    : BOS 계정관리 등록 & 수정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.27		손진구          최초생성
 * @ 2020.12.16		ykk           ERP 연동 v1.44 반영
 * @
 * **/
"use strict";

var viewModel;
var pageParam = parent.POP_PARAM["parameter"]; // 파라미터

$(document).ready(function() {
	fnInitialize(); // Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize() {
		$scope.$emit("fnIsMenu", {
			flag : false
		});

		fnPageInfo({
			PG_ID : "employeeMgm",
			callback : fnUI
		});

	};

	// 초기 UI 셋팅
	function fnUI() {
		fnInitButton(); // Initialize Button  ---------------------------------
		fnInitOptionBox(); //Initialize Option Box ------------------------------------
		fnPreventSubmit(); // enter 입력방지 공통
		fnViewModelSetting(); // Model 셋팅
        fnDefaultValue(); // 등록 && 수정에 따른 값 셋팅
        // setValidate();  //validation 설정
	};

	//--------------------------------- Button Start---------------------------------
	// 버튼 초기화
	function fnInitButton() {
		$("#fnHeadquartersPasswordPush, #fnUserIdDuplicateConfirm, #fnSave").kendoButton();
	};

    function fnSave() {
        if( viewModel.get("newCreate") ){
            fnSaveProcess("");
        } else {
            if (viewModel.get("sAuthUpdateYn") || viewModel.employeeInfo.get("isAuthListChanged") === 'Y'){
                fnKendoPopup({
                    id: "itgcPopup",
                    title: "ITSM 계정",
                    src: "#/itgcPopup",
                    param: {},
                    width: "450px",
                    height: "150px",
                    success: function(id, data) {
                        if( data.itsmId == undefined ){
                            fnKendoMessage({message : 'ITSM 계정을 입력해 주세요.'});
                            return;
                        }
                        fnSaveProcess(data);
                    }
                });
            } else {
                fnSaveProcess("");
            }
        }
    }

	// 저장
	function fnSaveProcess(data) {
	    let paramData = $("#inputForm").formSerialize(true);

	    if( paramData.rtnValid ){
	        if( fnSaveValid() ){
	            let url = "";

	            if( viewModel.get("newCreate") ){
	                url = "/admin/user/employee/addEmployeeInfo";

	                // 등록 - ITSM 계정
                    if( viewModel.employeeInfo.adminType == "COMPANY_TYPE.HEADQUARTERS" ){
                        if($("#itsmId").val() == ""){
                            fnKendoMessage({message : 'ITSM 계정을 입력해 주세요.'});
                            return;
                        }
                        viewModel.employeeInfo.set("itsmId", $("#itsmId").val());
                    } else {
                        if($("#clientItsmId").val() == ""){
                            fnKendoMessage({message : 'ITSM 계정을 입력해 주세요.'});
                            return;
                        }
                        viewModel.employeeInfo.set("itsmId", $("#clientItsmId").val());
                    }
	            }else{
	                url = "/admin/user/employee/putEmployeeInfo";

                    // 수정 - ITSM 계정
                    if (viewModel.get("sAuthUpdateYn") || viewModel.employeeInfo.get("isAuthListChanged") === 'Y'){
                        viewModel.employeeInfo.set("itsmId", data.itsmId);
                    }

                    // 수정 - ITSM 계정 - 개인정보 접근권한
                    if(viewModel.sPersonalInfoAccessYn != $("input[name=personalInfoAccessYn]:checked").val()){
                        if($("#privacyItsmId").val() == ""){
                            fnKendoMessage({message : 'ITSM 계정을 입력해 주세요.'});
                            return;
                        }
                        viewModel.employeeInfo.set("privacyItsmId", $("#privacyItsmId").val());
                    }
	            }

	            viewModel.employeeInfo.set("grantAuthDateStart", $("#grantAuthDateStart").val());
	            viewModel.employeeInfo.set("grantAuthDateEnd", $("#grantAuthDateEnd").val());

                fnAjax({
                    url     : url,
                    params  : viewModel.get("employeeInfo"),
                    contentType : "application/json",
                    success : function( data ){
                        if( viewModel.get("newCreate") ){
                            fnKendoMessage({ message : "저장이 완료되었습니다.", ok : fnClose });
                        }else{
                            fnKendoMessage({ message : "저장이 완료되었습니다.", ok : fnClose });
                        }
                    },
                    error : function(xhr, status, strError){
                        fnKendoMessage({ message : xhr.responseText });
                    },
                    isAction : "insert"
                });
	        }
	    }

	};

	// 팝업 닫기
	function fnClose(){
	    parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
	};

	//--------------------------------- Button End---------------------------------


	//---------------Initialize Option Box Start ------------------------------------------------
	// 옵션 초기화
	function fnInitOptionBox() {

        $("#tabstrip").kendoTabStrip({ animation: false }).data("kendoTabStrip").activateTab( $("#defaultInfoTab") ); // TAB 설정

	    // 관리자 유형
        fnKendoDropDownList({
            id    : "adminType",
            url : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "COMPANY_TYPE", "useYn" :"Y"}
        });

        // 회원상태
        fnTagMkRadio({
            id    : "userStatus",
            tagId : "userStatus",
            url   : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "EMPLOYEE_STATUS", "useYn" :"Y"},
            async : false
        });

        // 회원상태 속성추가
        $("input[name=userStatus]").each(function() {

            if( $(this).val() == "EMPLOYEE_STATUS.RESIGN" || $(this).val() == "EMPLOYEE_STATUS.ADMINISTRATIVE_LEAVE" ){
                $(this).attr("data-bind", "checked: employeeInfo.userStatus, visible: userStatusVisible");
                $("label[for=" + $(this).attr("id") + "]").attr("data-bind", "visible: userStatusVisible");
            }else{
                $(this).attr("data-bind", "checked: employeeInfo.userStatus");
            }

            if( $(this).val() != "EMPLOYEE_STATUS.NORMAL" && $(this).val() != "EMPLOYEE_STATUS.STOP" ){
                $(this).attr("disabled", true);
            }
        });

        // 조직장 여부
        fnTagMkRadio({
            id    : "teamLeaderYn",
            tagId : "teamLeaderYn",
            data  : [ { "CODE" : "Y", "NAME":"예"}
                    , { "CODE" : "N", "NAME":"아니오"} ],
            style : {},
            async : false
        });

        // 조직장 여부 속성 추가
        $("input[name=teamLeaderYn]").each(function() {
            $(this).attr("data-bind", "checked: employeeInfo.teamLeaderYn");
        });

        // 개인정보 열람권한
        fnTagMkRadio({
            id    : "personalInfoAccessYn",
            tagId : "personalInfoAccessYn",
            data  : [ { "CODE" : "N", "NAME":"권한없음"}
                    , { "CODE" : "Y", "NAME":"레벨1(평문)"}
                    , { "CODE" : "M", "NAME":"레벨2(마스킹)"}],
            style : {},
            async : false
        });

        // 개인정보 열람권한 속성추가
        $("input[name=personalInfoAccessYn]").each(function() {
            $(this).attr("data-bind", "checked: employeeInfo.personalInfoAccessYn");
        });

        // 권한 위임기간 시작
        fnKendoDatePicker({
            id: "grantAuthDateStart",
            format: "yyyy-MM-dd",
            btnStartId: "grantAuthDateStart",
            btnEndId: "grantAuthDateEnd",
            change : function() {
                fnStartCalChange("grantAuthDateStart", "grantAuthDateEnd");
            }
        });

        // 권한 위임기간 종료
        fnKendoDatePicker({
            id: "grantAuthDateEnd",
            format: "yyyy-MM-dd",
            btnStartId: "grantAuthDateStart",
            btnEndId: "grantAuthDateEnd",
            change : function() {
                fnEndCalChange("grantAuthDateStart", "grantAuthDateEnd");
            }
        });

        // 위임 중지
        fnTagMkChkBox({
            id : "grantAuthStop",
            data : [ { "CODE" : "N", "NAME" : "위임 중지" } ],
            tagId : "grantAuthStop",
            async : false
        });

        // 개인정보 열람권한 속성추가
        $("input[name=grantAuthStop]").each(function() {
            $(this).attr("data-bind", "checked: grantAuthStopCheck, events: { change: fnGrantAuthStopCheck}");
        });

        // 역할명
        fnKendoDropDownList({
            id : "roleName",
            url : "/admin/system/auth/getRoleTypeList",
            params : {},
            textField :"roleTypeName",
            valueField : "roleTypeId",
            blank : "역할선택"
        });

        // 공급업체 선택
        fnKendoDropDownList({
            id : "authSupplier",
            url : "/admin/ur/urCompany/getSupplierCompanyList",
            params : {},
            textField :"supplierName",
            valueField : "urSupplierId",
            blank : "선택 안함(전체권한)"
        });

        // 출고처그룹 선택
        fnKendoDropDownList({
            id : "warehouseGroup",
            tagId : "warehouseGroup",
            url : "/admin/comn/getCodeList",
            params : { "stCommonCodeMasterCode" : "WAREHOUSE_GROUP", "useYn" : "Y" },
            blank : "출고처 그룹 전체",
        }).bind("change", fnWareHouseGroupChange);

        // 출고처 선택
        fnKendoDropDownList({
            id : "authWarehouse",
            url : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
            params : { "warehouseGroupCode" : "" },
            textField :"warehouseName",
            valueField : "warehouseId",
            blank : "선택 안함(전체권한)"
        });

        // 입력 제어
        fnInputValidationForHangulAlphabetNumber("employeeNumber"); // 사번
        fnInputValidationForNumber("mobile"); // 휴대폰번호
        fnInputValidationForLowAlphabetNumberPoint("loginId"); // 아이디
        fnInputValidationForHangulAlphabetNumber("userName"); // 이름
        fnInputValidationForAlphabetNumberSpecialCharacters("email"); // 이메일 주소
        fnInputValidationForHangulAlphabetNumber("regalName"); // 법인정보

	};

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

    // 등록 or 수정에 따른 기본값 셋팅
    function fnDefaultValue(){
        if( pageParam.employeeNumber != undefined && pageParam.employeeNumber != "" ){ // 수정
            viewModel.set("newCreate", false);
            fnEmployeeInfo( pageParam.employeeNumber );
        }else if( pageParam.erpEmployeeNumber != undefined && pageParam.erpEmployeeNumber != "" ){ // 임직원 정보 등록
            viewModel.set("newCreate", true);

            //ITSM 아이디 활성화여부 - 신규등록시 화면설정
            $("#employeeNumberViewTd").attr("colspan","1");
            $("#itsmTh").css("display","");
            $("#itsmTd").css("display","");

            $("#clientNameTd").attr("colspan","1");
            $("#clientItsmTh").css("display","");
            $("#clientItsmTd").css("display","");

            fnEmployeeInfo( pageParam.erpEmployeeNumber );
        }else{
            viewModel.set("newCreate", true);

            //ITSM 아이디 활성화여부 - 신규등록시 화면설정
            $("#employeeNumberViewTd").attr("colspan","1");
            $("#itsmTh").css("display","");
            $("#itsmTd").css("display","");

            $("#clientNameTd").attr("colspan","1");
            $("#clientItsmTh").css("display","");
            $("#clientItsmTd").css("display","");
        }
    };

    // Model 셋팅
    function fnViewModelSetting(){
        viewModel = new kendo.data.ObservableObject({

            employeeInfo : { // 관리자 Info
                adminType : "COMPANY_TYPE.HEADQUARTERS", // 관리자유형
                employeeNumber : "", // 사번
                loginId : "", // 사용자 ID
                userId : null, // 회원 ID
                companyId : null, // 회사 ID
                clientName : "", // 거래처명
                firstJoinDate : "", // 최초 가입일시
                lastVisitDate : "", // 최근 접속일시
                userStatus : "EMPLOYEE_STATUS.NORMAL", // 회원상태
                userName : "", // 이름
                email : "", // 이메일 주소
                regalName : "", // 법인정보명
                regalCode : "", // 법인정보코드
                mobile : "", // 휴대폰 번호
                positionName : "", // 직책 정보
                organizationName : "", // 조직 정보명
                organizationCode : "", // 조직 정보코드
                teamLeaderYn : "", // 조직장 여부
                personalInfoAccessYn : "", // 개인정보 열람권한
                accessAuthGrantDate : "", // 열람권한 부여일시
                grantAuthEmployeeName : "", // 권한 위임정보 담당자명
                grantAuthEmployeeNumber : "", // 권한 위임정보 담당자 사번
                grantAuthDateStart : "", // 권한 위임기간 시작일자
                grantAuthDateEnd : "", // 권한 위임기간 종료일자
                grantAuthStopYn : "N", // 권한 위임 중지 여부
                authSupplierId : null, // 공급업체 Id
                authWarehouseGroupId : null, // 공급업체 Id
                authWarehouseId : null, // 공급업체 Id
                authSupplierDate : "", // 공급업체 권한 부여일시
                authSupplierUserName : "", // 공급업체 권한 부여 수정자
                authSupplierUserLoginId : "", // 공급업체 권한 부여 수정자 로그인 아이디
                erpUserStatus : "", // ERP 임직원 재직상태
                roleList : [], // 역할 List
                authSupplierIdList : [], // 공급업체 권한 List
                authWarehouseIdList : [], // 출고처 권한 List
                isAuthListChanged : "N"// 공급업체/출고처 권한 변경여부
            },
            headquarters : true, // 본사 유무
            newCreate : false, // 신규등록 유무
            adminTypeDisabled : false, // 관리자 유형 DropDown Disable
            employeeNumberDisabled : false, // 사번 입력 Disabled
            employeeNumberSearchButtonVisible : true, // 사번 검색 버튼 Visible
            loginIdDisabled : true, // 아이디 입력 Disabled
            loginIdDuplicateConfirmVisible : false, // 아이디 중복확인 버튼 Visible
            loginIdDuplicateConfirm : false, // 아이디 중복확인 실행 유무
            passwordPushVisible : false, // 비밀번호 재발송 버튼 Visible
            userNameDisabled : true, // 이름 입력  Disabled
            userNameButtonVisible : false, // 이름 변경 버튼 Visible
            emailDisabled : true, // 이메일 입력  Disabled
            emailButtonVisible : false, // 이메일 변경 버튼 Visible
            emailDuplicateConfirmButtonVisible : false, // 이메일 중복확인 버튼 Visible
            emailDuplicateConfirm : true, // 이메일 중복확인 유무
            regalNameDisabled : true, // 법인정보 입력 제어
            regalNameButtonVisible : false, // 법인정보 변경 버튼 Visible
            mobileDisabled : true, // 휴대폰 입력 제어
            mobileButtonVisible : false, // 휴대폰 변경 버튼 Visible
            roleNameDisabled : false, // 역할명 DropDown Disabled
            roleAddVisible : true, // 역할 추가 버튼 Visible
            adminAuthPageMoveButtonVisible : true, // 관리자 권한관리 바로가기 버튼 Visible
            grantAuthEmployeeSearchButtonDisabled : false, // 담당자 검색 버튼 Disabled
            grantAuthDateStartDisabled : false, // 권한 위임기간 시작일 Disabled
            grantAuthDateEndDisabled : false, // 권한 위임기간 종료일 Disabled
            authSupplierDisabled : false, // 공급업체 선택 Disabled
            authWarehouseGroupDisabled : false, // 출고처그룹 선택 Disabled
            authWarehouseDisabled : false, // 출고처 선택 Disabled
            grantAuthStopCheck : false, // 위임 중지 Check
            userStatusVisible : true, // 회원상태 Visible

            sPersonalInfoAccessYn : "", // 개인정보접근권한 - 조회정보
            sAuthUpdateYn : false, // 역할정보 수정여부

            fnNewCreateEmployeeViewSetting : function(){ // 등록화면 셋팅
                // 데이터
                viewModel.employeeInfo.set("employeeNumber", "");
                viewModel.employeeInfo.set("loginId", "");
                viewModel.employeeInfo.set("userId", null);
                viewModel.employeeInfo.set("companyId", null);
                viewModel.employeeInfo.set("clientName", "");
                viewModel.employeeInfo.set("userStatus", "EMPLOYEE_STATUS.NORMAL");
                viewModel.employeeInfo.set("userName", "");
                viewModel.employeeInfo.set("email", "");
                viewModel.employeeInfo.set("regalName", "");
                viewModel.employeeInfo.set("regalCode", "");
                viewModel.employeeInfo.set("mobile", "");
                viewModel.employeeInfo.set("positionName", "");
                viewModel.employeeInfo.set("organizationName", "");
                viewModel.employeeInfo.set("organizationCode", "");
                viewModel.employeeInfo.set("teamLeaderYn", viewModel.headquarters ? "" : "N" );
                viewModel.employeeInfo.set("personalInfoAccessYn", "");
                viewModel.employeeInfo.set("grantAuthEmployeeName", "");
                viewModel.employeeInfo.set("grantAuthEmployeeNumber", "");
                viewModel.employeeInfo.set("grantAuthDateStart", "");
                viewModel.employeeInfo.set("grantAuthDateEnd", "");
                viewModel.employeeInfo.set("grantAuthStopYn", "N");
                viewModel.employeeInfo.set("authSupplierId", null);
                viewModel.employeeInfo.set("authWarehouseGroupId", null);
                viewModel.employeeInfo.set("authWarehouseId", null);
                viewModel.employeeInfo.set("erpUserStatus", "");
                viewModel.employeeInfo.set("roleList", []);
                viewModel.employeeInfo.set("authSupplierIdList", []);
                viewModel.employeeInfo.set("authWarehouseIdList", []);
                viewModel.employeeInfo.set("isAuthListChanged", "N");

                // 화면 제어
                viewModel.set("loginIdDisabled", viewModel.headquarters);
                viewModel.set("loginIdDuplicateConfirmVisible", !viewModel.headquarters);
                viewModel.set("userNameDisabled", viewModel.headquarters);
                viewModel.set("emailDisabled", viewModel.headquarters);
                viewModel.set("emailDuplicateConfirmButtonVisible", !viewModel.headquarters);
                viewModel.set("regalNameDisabled", viewModel.headquarters);
                viewModel.set("mobileDisabled", viewModel.headquarters);
                viewModel.set("grantAuthStopCheck", false);
                viewModel.set("grantAuthEmployeeSearchButtonDisabled", !viewModel.headquarters);
                viewModel.set("grantAuthDateStartDisabled", !viewModel.headquarters);
                viewModel.set("grantAuthDateEndDisabled", !viewModel.headquarters);
                viewModel.set("authSupplierDisabled", !viewModel.headquarters);
                viewModel.set("authWarehouseGroupDisabled", !viewModel.headquarters);
                viewModel.set("authWarehouseDisabled", !viewModel.headquarters);
                viewModel.set("userStatusVisible", viewModel.headquarters);

                fnPlaceholderSettion();

                // 검증
                viewModel.set("loginIdDuplicateConfirm", false);
                viewModel.set("emailDuplicateConfirm", viewModel.headquarters);

                viewModel.fnRequiredChange();
            },
            fnModifyEmployeeViewSetting : function(){ // 수정화면 셋팅
                // 화면 제어
                viewModel.set("adminTypeDisabled", true);
                viewModel.set("employeeNumberSearchButtonVisible", false);
                viewModel.set("employeeNumberDisabled", true);
                viewModel.set("loginIdDisabled", true);
                viewModel.set("loginIdDuplicateConfirmVisible", false );
                viewModel.set("passwordPushVisible", true );
                viewModel.set("userNameDisabled", true);
                viewModel.set("userNameButtonVisible", !viewModel.headquarters);
                viewModel.set("emailDisabled", true);
                viewModel.set("emailButtonVisible", !viewModel.headquarters);
                viewModel.set("emailDuplicateConfirmButtonVisible", !viewModel.headquarters);
                viewModel.set("regalNameDisabled", true );
                viewModel.set("regalNameButtonVisible", !viewModel.headquarters);
                viewModel.set("mobileDisabled",  true);
                viewModel.set("mobileButtonVisible", viewModel.fnMobileButtonVisibleChange());
                viewModel.set("grantAuthStopCheck",  viewModel.employeeInfo.grantAuthStopYn == "N" ? false : true);
                viewModel.set("roleAddButtonDisabled", false );
                viewModel.set("adminAuthPageMoveButtonDisabled", false );
                viewModel.set("grantAuthEmployeeSearchButtonDisabled", false );
                viewModel.set("grantAuthDateStartDisabled", false );
                viewModel.set("grantAuthDateEndDisabled", false );
                viewModel.set("authSupplierDisabled", false );
                viewModel.set("authWarehouseGroupDisabled", false );
                viewModel.set("authWarehouseDisabled", false );
                viewModel.set("userStatusVisible", viewModel.headquarters);

                fnUserStatusRadioSetting();

                // 검증
                viewModel.set("loginIdDuplicateConfirm", true);
                viewModel.set("emailDuplicateConfirm", true );

                viewModel.fnRequiredChange();

                if(fnIsProgramAuth("VIEW_AUTH")){
                    $('.viewAuth').show();
                } else {
                    $('.viewAuth').hide();
                }

                if(fnIsProgramAuth("DELETE_AUTH")){
                    $('.deleteAuth').show();
                } else {
                    $('.deleteAuth').hide();
                }
            },
            fnAdminTypeChange : function(){ // 관리자유형 변경

                if( viewModel.employeeInfo.adminType == "COMPANY_TYPE.HEADQUARTERS" ){
                    $("#employeeNumberView").show();
                    $("#positionView").show();
                    $("#teamLeaderYnView").show();
                    $("#clientChoiceView").hide();
                    $("#grantAuthAndAuthSupplierView").show();
                    viewModel.set("headquarters", true);
                }else{
                    $("#employeeNumberView").hide();
                    $("#positionView").hide();
                    $("#teamLeaderYnView").hide();
                    $("#clientChoiceView").show();
                    $("#grantAuthAndAuthSupplierView").hide();
                    viewModel.set("headquarters", false);
                }

                if( viewModel.newCreate ){
                    viewModel.fnNewCreateEmployeeViewSetting();
                }
            },
            fnEmployeeNumberSearch : function(){ // 사번 검색
                fnEmployeeInfo( viewModel.employeeInfo.get("employeeNumber") );
            },
            fnClientSearchPopup : function(){ // 거래처 검색 팝업
                fnKendoPopup({
                    id     : "clientSearchPopup",
                    title  : "거래처 검색",
                    src    : "#/clientSearchPopup",
                    param  : {},
                    width  : "700px",
                    height : "600px",
                    success: function( id, data ){

                        if( data.companyId != undefined ){
                            viewModel.employeeInfo.set("companyId", data.companyId);
                            viewModel.employeeInfo.set("clientName", data.clientName);
                        }else{
                            viewModel.employeeInfo.set("companyId", "");
                            viewModel.employeeInfo.set("clientName", "");
                        }
                    }
                });
            },
            fnLoginIdChange : function(){ // 아이디 변경
                if( viewModel.headquarters == false && viewModel.newCreate == true ){
                    viewModel.set("loginIdDuplicateConfirm", false);
                    viewModel.employeeInfo.set("employeeNumber", "");
                }
            },
            fnLoginIdDuplicateConfirm : function(){ // 아이디 중복확인
                if( fnClientLoginIdValid() == false ){
                    return;
                }

                fnAjax({
                    url     : "/admin/user/employee/getUserInfo",
                    method : "GET",
                    params  : { loginId : viewModel.employeeInfo.loginId },
                    success : function( data ){
                        if( data.userInfo != null ){
                            fnKendoMessage({
                                message : "이미 존재하는 아이디 입니다.",
                                ok      : function(){ $("#loginId").focus(); }
                            });
                        }else{
                            fnKendoMessage({ message : "등록 가능한 아이디 입니다." });
                            viewModel.employeeInfo.set("employeeNumber", viewModel.employeeInfo.get("loginId"));
                            viewModel.set("loginIdDuplicateConfirm", true);
                        }
                    },
                    isAction : "select"
                });
            },
            fnPasswordPush : function(){ // 비밀번호 재발송
                let param = {};

                param.mobile = fnPhoneNumberHyphen( kendo.htmlEncode(viewModel.employeeInfo.mobile) );
                param.mail = viewModel.employeeInfo.email;
                param.urUserId = viewModel.employeeInfo.userId;
                param.userType = 'EMPLOYEE';
                param.loginId = viewModel.employeeInfo.loginId;

                fnKendoPopup({
                    id: "pwClearPopup",
                    title: "PW 재발송",
                    param: { "data" : param},
                    src: "#/pwClearPopup",
                    width: "660px",
                    height: "350px",
                    success: function(id, data) {

                    }
                });
            },
            fnMobileDisabledChange : function(){ // 휴대폰 번호 Disabled 변경
                viewModel.set("mobileDisabled", false);
            },
            fnUserNameDisabledChange : function(){ // 이름 Disabled 변경
                viewModel.set("userNameDisabled", false);
            },
            fnEmailDisabledChange : function(){ // 이메일 Disabled 변경
                viewModel.set("emailDisabled", false);
            },
            fnEmailChange : function(){ // 이메일 변경
                viewModel.set("emailDuplicateConfirm", false);
            },
            fnEmailDuplicateConfirm : function(){ // 이메일 중복검사
                if( !fnEmailValid() ){
                    return;
                }

                fnAjax({
                    url     : "/admin/user/employee/getEmailDuplocateCheck",
                    params  : { email : viewModel.employeeInfo.get("email") },
                    success : function( data ){
                        if( data.emailDuplocate ){
                            fnKendoMessage({
                                message : "이미 등록된 이메일 입니다.",
                                ok      : function(){
                                    viewModel.set("emailDuplicateConfirm", false);
                                    $("#email").focus();
                                }
                            });
                        }else{
                            fnKendoMessage({
                                message : "등록 가능한 이메일 입니다.",
                                ok      : function(){
                                    viewModel.set("emailDuplicateConfirm", true);
                                    $("#email").focus();
                                }
                            });
                        }
                    },
                    isAction : "select"
                });
            },
            fnRegalNameDisabledChange : function(){ // 법인정보 Disabled 변경
                viewModel.set("regalNameDisabled", false);
            },
            fnRoleAdd : function(){ // 권한설정 역할 추가

                let roleNameDorpDown = $("#roleName").data("kendoDropDownList");
                let dataItem = roleNameDorpDown.dataItem();

                if( !$("#roleName").val() ){
                    fnKendoMessage({ message : "선택된 권한설정이 없습니다." });
                    return;
                }

                fnRoleListAdd( dataItem );
            },
            fnAdminAuthPageMove : function(){ // 관리자 권한관리 바로가기
                let option = {};
                option.url = "#/authUser";
                option.menuId = 68;
                option.target = "authUser";

                fnGoNewPage(option);
            },
            fnAuthView : function(e){ // 역할 권한보기
                let dataItem = e.data;
                let option = {};
                option.url = "#/authUser";
                option.menuId = 68;
                option.target = "authUser";
                option.data = {
                    authUserMappingId : dataItem.authUserMappingId
                  , roleTypeId : dataItem.roleTypeId
                  , userId : dataItem.userId
                };

                fnGoNewPage(option);
            },
            fnAuthDeleteRow : function(e) { // 역할 삭제

                fnKendoMessage({
                    type    : "confirm",
                    message : "삭제하시겠습니까?",
                    ok      : function(){
                        let dataItem = e.data;
                        let roleList = viewModel.employeeInfo.get("roleList");
                        let index = roleList.indexOf(dataItem);

                        roleList.splice(index, 1);

                        // 역할 편집여부
                        viewModel.set("sAuthUpdateYn", true);
                    },
                    cancel  : function(){

                    }
                });

            },
            fnGrantAuthStopCheck : function(e){ // 위임 중지 체크 클릭
                if( viewModel.grantAuthStopCheck ){
                    viewModel.employeeInfo.set("grantAuthStopYn", "Y");
                    viewModel.set("grantAuthDateStartDisabled", true );
                    viewModel.set("grantAuthDateEndDisabled", true );
                }else{
                    viewModel.employeeInfo.set("grantAuthStopYn", "N");
                    viewModel.set("grantAuthDateStartDisabled", false );
                    viewModel.set("grantAuthDateEndDisabled", false );
                }
            },
            fnGrantAuthEmployeeSearch : function(){ // 담당자 검색 팝업
                fnKendoPopup({
                    id     : "grantAuthEmployeeSearchPopup",
                    title  : "담당자 검색",
                    src    : "#/grantAuthEmployeeSearchPopup",
                    param  : {},
                    width  : "700px",
                    height : "600px",
                    success: function( id, data ){

                        if( data.employeeNumber != undefined ){
                            viewModel.employeeInfo.set("grantAuthEmployeeNumber", data.employeeNumber);
                            viewModel.employeeInfo.set("grantAuthEmployeeName", data.employeeName);
                        }else{
                            viewModel.employeeInfo.set("grantAuthEmployeeNumber", "");
                            viewModel.employeeInfo.set("grantAuthEmployeeName", "");
                        }
                    }
                });
            },
            fnRequiredChange : function(){ // 필수 속성 변경
                if( viewModel.headquarters ){
                    $("#employeeNumber, #authSupplier, #organizationName, #regalName").attr("required");
                    $("#clientName").removeAttr("required");
                    $("#regalNameTh").addClass("req-star-front");
                }else{
                    $("#employeeNumber, #authSupplier, #organizationName, #regalName").removeAttr("required");
                    $("#clientName").attr("required");
                    $("#regalNameTh").removeClass("req-star-front");
                }
            },
            fnMobileButtonVisibleChange : function(){ // 모바일 버튼 Visible 변경
                // 본사 일 경우 로그인한 사람의 개인정보열람권한이 있어야만 휴대폰 번호 변경 가능
                if( viewModel.headquarters ){
                    if( PG_SESSION.personalInformationAccessYn != undefined && PG_SESSION.personalInformationAccessYn == "Y" ){
                        return true;
                    }else{
                        return false;
                    }
                }else{
                    return true;
                }
            }
            , fnAddAuthSupplierCard : function(){ // 공급업체 권한 추가
                let authSupplierDorpDown = $("#authSupplier").data("kendoDropDownList");
                let dataItem = authSupplierDorpDown.dataItem();

                if( !$("#authSupplier").val() ){
        			fnKendoMessage({message:'공급업체를 선택해주세요.'});
                    return;
                }
                addSupplierItem(dataItem);
            }
            , fnDeleteAuthSupplierCard : function(e) { // 설정된 공급업체 권한 삭제
				let dataItem = e.data;
				let authSupplierIdList = viewModel.employeeInfo.get("authSupplierIdList");
				let index = authSupplierIdList.indexOf(dataItem);
				authSupplierIdList.splice(index, 1);
				viewModel.employeeInfo.set("isAuthListChanged", "Y");
            }
            , fnAddAuthWarehouseCard : function(){ // 출고처 권한 추가
                let authWarehouseDorpDown = $("#authWarehouse").data("kendoDropDownList");
                let dataItem = authWarehouseDorpDown.dataItem();

                if( !$("#authWarehouse").val() ){
        			fnKendoMessage({message:'출고처를 선택해주세요.'});
                    return;
                }
                addWarehouseItem(dataItem);
            }
            , fnDeleteAuthWarehouseCard : function(e) { // 설정된 출고처 권한 삭제
				let dataItem = e.data;
				let authWarehouseIdList = viewModel.employeeInfo.get("authWarehouseIdList");
				let index = authWarehouseIdList.indexOf(dataItem);
				authWarehouseIdList.splice(index, 1);
				viewModel.employeeInfo.set("isAuthListChanged", "Y");
            }
        });

        kendo.bind($("#inputForm"), viewModel);
    };

    // 입력창 placeholder 설정
    function fnPlaceholderSettion(){
        if( viewModel.headquarters ){
            $("#userName").removeAttr("placeholder");
            $("#email").removeAttr("placeholder");
            $("#regalName").removeAttr("placeholder");
            $("#mobile").removeAttr("placeholder");
        }else{
            $("#userName").attr("placeholder", "이름 입력");
            $("#email").attr("placeholder", "이메일 입력");
            $("#regalName").attr("placeholder", "법인정보 입력");
            $("#mobile").attr("placeholder", "휴대폰 입력");
        }
    };

    // 수정 셋팅
    function fnModifySetting( data ){
    	var employeeInfo = data.employeeInfo;
    	var authRoleList = data.authRoleList;
    	var authSupplierList = data.authSupplierList; // 공급업체 권한
    	var authWarehouseList = data.authWarehouseList; // 출고처 권한

    	viewModel.set("employeeInfo", employeeInfo);
        viewModel.employeeInfo.set("roleList", authRoleList);
        viewModel.fnAdminTypeChange();
        viewModel.fnModifyEmployeeViewSetting();
        viewModel.set("sPersonalInfoAccessYn", employeeInfo.personalInfoAccessYn);

        $("#itsmTh").css("display","none");
        $("#itsmTd").css("display","none");
        $("#employeeNumberViewTd").attr("colspan","3");

        $("#clientItsmTh").css("display","none");
        $("#clientItsmTd").css("display","none");
        $("#clientNameTd").attr("colspan","3");

        // 기 설정된 공급업체 권한
        viewModel.employeeInfo.set("authSupplierIdList", authSupplierList);

        // 기 설정된 출고처 권한
        viewModel.employeeInfo.set("authWarehouseIdList", authWarehouseList);

		viewModel.employeeInfo.set("isAuthListChanged", "N"); // 공급업체/출고처 권한 설정변경 여부 초기화

		// 상품권한 최근수정 정보 설정
		var strAuthSupplierChangeInfo = '';
		if (employeeInfo.authSupplierDate != null && employeeInfo.authSupplierDate != '')
			strAuthSupplierChangeInfo = employeeInfo.authSupplierDate + ' / ' + employeeInfo.authSupplierUserName + '(' + employeeInfo.authSupplierUserLoginId + ')'
		$("#authSupplierChangeInfo").text(strAuthSupplierChangeInfo);

		// ITSM 아이디 활성화여부 - 개인정보 접근권한 - 수정시 활성화
        $("input[name=personalInfoAccessYn]").change(function() {
            if(viewModel.sPersonalInfoAccessYn != $("input[name=personalInfoAccessYn]:checked").val()){
                $("#privacyItsmTr").css("display","");
            } else {
                $("#privacyItsmTr").css("display","none");
            }
        });
    };

    // 회원 상태 Radio 제어
    function fnUserStatusRadioSetting(){
        /*
    	if( viewModel.employeeInfo.get("userStatus") != "EMPLOYEE_STATUS.NORMAL"
                && viewModel.employeeInfo.get("userStatus") != "EMPLOYEE_STATUS.STOP" )
        {
            $("input[name=userStatus]").attr("disabled", true);
        }*/

    	//회원상태가 휴직이고 ERP 상태가 정상일 경우 상태 수정 가능
        if( viewModel.employeeInfo.get("userStatus") == "EMPLOYEE_STATUS.TEMPORARY_STOP"
        	|| viewModel.employeeInfo.get("userStatus") == "EMPLOYEE_STATUS.RESIGN"
            || (viewModel.employeeInfo.get("userStatus") == "EMPLOYEE_STATUS.ADMINISTRATIVE_LEAVE"
        		&& viewModel.employeeInfo.get("erpUserStatus") != "EMPLOYEE_STATUS.NORMAL"))
        {
        	$("input[name=userStatus]").attr("disabled", true);
        }
    };

    // 회원정보 조회
    function fnEmployeeInfo( employeeNumber ){
        if( employeeNumber == undefined || employeeNumber == "" ){
            fnKendoMessage({ message : "사번은 필수입니다."});
            return;
        }

        fnAjax({
            url     : "/admin/user/employee/getEmployeeInfo",
            method : "GET",
            params  : { employeeNumber : employeeNumber },
            success : function( data ){
                if( viewModel.get("newCreate") ){
                    if( data.employeeInfo == null ){
                        fnErpEmployeeInfo( employeeNumber );
                    }else{
                        fnKendoMessage({
                            type    : "confirm",
                            message : "이미 등록 되어있는 계정입니다. 등록정보를 불러올까요?",
                            ok      : function(){
                                viewModel.set("newCreate", false);
                                fnModifySetting( data );
                            },
                            cancel  : function(){
                                viewModel.fnNewCreateEmployeeViewSetting();
                                $("#employeeNumber").focus();
                            }
                        });
                    }
                }else{
                    fnModifySetting( data );
                }
            },
            isAction : "select"
        });
    };

    // ERP 회원정보 조회
    function fnErpEmployeeInfo( employeeNumber ){
        if( employeeNumber == undefined || employeeNumber == "" ){
            fnKendoMessage({ message : "사번은 필수입니다."});
            return;
        }

        fnAjax({
            url     : "/admin/user/employee/getErpEmployeeInfo",
            method : "GET",
            params  : { employeeNumber : employeeNumber },
            success : function( data ){
                if( data.erpEmployeeInfo == null ){
                    fnKendoMessage({
                        message : "입력하신 정보가 존재하지 않습니다.",
                        ok      : function(){
                            viewModel.fnNewCreateEmployeeViewSetting();
                            $("#employeeNumber").focus();
                        }
                    });
                }else{
                    viewModel.set("employeeInfo", data.erpEmployeeInfo);
                    viewModel.employeeInfo.set("roleList", []);
                    fnEmployeeDefaultRoleAdd();
                    viewModel.employeeInfo.set("authSupplierIdList", []);
                    viewModel.employeeInfo.set("authWarehouseIdList", []);
    				viewModel.employeeInfo.set("isAuthListChanged", "N");
                }
            },
            isAction : "select"
        });
    };

    // 입력값 검증
    function fnSaveValid(){
        // 조직장 여부
        if( viewModel.get("headquarters") && ( viewModel.employeeInfo.get("teamLeaderYn") == null || viewModel.employeeInfo.get("teamLeaderYn") == "" )){
            fnKendoMessage({
                message : "<p>" + "<span style='color: red;font-size: 18pt;font-weight: bolder;'>[조직장 여부]</span>는 필수 입니다.<p>"
            });

            return false;
        }

        // 개인정보 열람권한
        if( viewModel.employeeInfo.get("personalInfoAccessYn") == null || viewModel.employeeInfo.get("personalInfoAccessYn") == "" ){
            fnKendoMessage({
                message : "<p>" + "<span style='color: red;font-size: 18pt;font-weight: bolder;'>[개인정보 열람권한]</span>은 필수 입니다.<p>"
            });

            return false;
        }

        // 사번
        if( viewModel.get("headquarters") && viewModel.get("employeeNumber") != viewModel.get("loginId") ){
            fnKendoMessage({ message : "사번과 아이디가 다릅니다." });
            return false;
        }

        // 아이디
        if( !viewModel.get("headquarters") && !fnClientLoginIdValid() ){
            return false;
        }

        // 아이디 중복확인
        if( !viewModel.get("headquarters") && !viewModel.get("loginIdDuplicateConfirm") ){
            fnKendoMessage({ message : "아이디 중복확인은 필수 입니다." });
            return false;
        }

        // 이메일
        if( !fnEmailValid() ){
            return false;
        }

        // 이메일 중복확인
        if( !viewModel.get("headquarters") && !viewModel.get("emailDuplicateConfirm") ){
            fnKendoMessage({ message : "이메일 중복확인은 필수 입니다." });
            return false;
        }

        // 휴대폰번호
        if( !fnMobileValid() ){
            return false;
        }

        // 권한 위임 정보
        if( viewModel.get("headquarters") && !fnGrantAuthEmployeeValid() ){
            return false;
        }

        // 권한설정 체크
        if( viewModel.employeeInfo.get("roleList") == null || viewModel.employeeInfo.get("roleList").length == 0 ){
            fnKendoMessage({ message : "최소 1개이상의 역할그룹이 존재해야 합니다." });
            return false;
        }

        return true;
    };

    // 아이디 검증
    function fnClientLoginIdValid(){
        let loginId = viewModel.employeeInfo.get("loginId");
        let number = loginId.search(/[0-9]/g);
        let alphabet = loginId.search(/[a-zA-Z]/ig);
        let point = loginId.search(/[.]/ig);

        if( loginId.length < 6 || loginId.length > 15 ){
            fnKendoMessage({message : "아이디는 6~15자리의 영문소문자, 숫자와 특수기호(.)만 사용가능합니다."});
            return false;
        } else if( number >= 0 && alphabet < 0 && point < 0 ){
            fnKendoMessage({message : "아이디는 숫자로 만들 수 없습니다."});
            return false;
        }else {
            return true;
        }
    };

    // 이메일 검증
    function fnEmailValid(){
        if( fnValidateEmail( viewModel.employeeInfo.get("email") ) ){
            return true;
        }else{
            fnKendoMessage({
                message : "잘못된 이메일 입니다.",
                ok      : function(){
                    $("#email").focus();
                }
            });
            return false;
        }
    };

    // 휴대폰번호 검증
    function fnMobileValid(){
        if( fnValidatePhone( viewModel.employeeInfo.get("mobile") ) ){
            return true;
        }else{
            fnKendoMessage({
                message : "잘못된 휴대폰번호 입니다.",
                ok      : function(){
                    $("#mobile").focus();
                }
            });
            return false;
        }
    };

    // 권한 위임 정보 검증
    function fnGrantAuthEmployeeValid(){
        if( !viewModel.employeeInfo.get("grantAuthEmployeeNumber")
                && !viewModel.employeeInfo.get("grantAuthDateStart")
                && !viewModel.employeeInfo.get("grantAuthDateEnd") )
        {
            return true;
        }else if( viewModel.employeeInfo.get("grantAuthEmployeeNumber")
                      && viewModel.employeeInfo.get("grantAuthDateStart")
                      && viewModel.employeeInfo.get("grantAuthDateEnd") )
        {
            return true;
        }else
        {
            fnKendoMessage({ message : "담당자 또는 권한 위임기간 중 입력하지 않으신 값이 있습니다." });
            return false;
        }
    };

    // 권한설정 사용자권한 매핑 삭제
    function fnDelAuthUserMapping( dataItem ){
        let param = {
             roleTypeId : dataItem.roleTypeId
           , userId : dataItem.userId
        };

        fnAjax({
            url     : "/admin/user/employee/delAuthUserMapping",
            params  : param,
            success : function( data ){

            },
            isAction : "delete"
        });
    };

    // 본사 사번에 해당하는 기본 권한역할 추가
    function fnEmployeeDefaultRoleAdd(){
        let roleNameDorpDown = $("#roleName").data("kendoDropDownList");
        const items = roleNameDorpDown.items();

        for(var i=0, rowCnt=items.length; i < rowCnt; i++){
            const rowItem = roleNameDorpDown.dataItem(i);

            if( rowItem.erpOrganizationCode == viewModel.employeeInfo.get("organizationCode")
                    && rowItem.erpRegalCode == viewModel.employeeInfo.get("regalCode") )
            {
                fnRoleListAdd(rowItem);
                break;
            }
        }
    };

    // 권한설정 리스트 역할 추가
    function fnRoleListAdd( dataItem ){
        let roleList = viewModel.employeeInfo.get("roleList");
        let duplRoleTypeId = roleList.filter(function(roleInfo){
            return roleInfo.roleTypeId == dataItem.roleTypeId;
        });

        if ( roleList == null ){
            roleList = [];
        }

        if( Object.keys(duplRoleTypeId).length > 0 ){
            fnKendoMessage({ message : "동일한 역할이 있습니다." });
            return;
        }

        roleList.push({
            authUserMappingId : null
          , roleTypeId : dataItem.roleTypeId
          , userId : dataItem.userId
          , roleTypeName : dataItem.roleTypeName
        });

        if(fnIsProgramAuth("VIEW_AUTH")){
            $('.viewAuth').show();
        } else {
            $('.viewAuth').hide();
        }

        if(fnIsProgramAuth("DELETE_AUTH")){
            $('.deleteAuth').show();
        } else {
            $('.deleteAuth').hide();
        }

        // 역할 편집여부
        viewModel.set("sAuthUpdateYn", true);

    };

    function fnWareHouseGroupChange() {
    	fnAjax({
            url     : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
            method : "GET",
            params : { "warehouseGroupCode" : $("#warehouseGroup").val() },

            success : function( data ){
                let authWarehouse = $("#authWarehouse").data("kendoDropDownList");
                authWarehouse.setDataSource(data.rows);
            },
            error : function(xhr, status, strError){
                fnKendoMessage({ message : xhr.responseText });
            },
            isAction : "select"
        });
    };

	// 공급업체 추가
	function addSupplierItem(dataItem) {
        let authSupplierIdList = viewModel.employeeInfo.get("authSupplierIdList");
        let duplAuthSupplierId = authSupplierIdList.filter(function(authSupplierInfo){
            return authSupplierInfo.authId == dataItem.urSupplierId;
        });

        if ( authSupplierIdList == null ){
        	authSupplierIdList = [];
        }

        if( Object.keys(duplAuthSupplierId).length > 0 ){
            fnKendoMessage({ message : "이미 선택된 공급업체입니다." });
            return;
        }

        //중복된 값이 없을 경우
		viewModel.employeeInfo.set("isAuthListChanged", "Y");
        authSupplierIdList.push({
        	authId : dataItem.urSupplierId
        	, authName : dataItem.supplierName
        	, authIdTp : 'S'
        });
	}

	// 출고처 추가
	function addWarehouseItem(dataItem) {
        let authWarehouseIdList = viewModel.employeeInfo.get("authWarehouseIdList");
        let duplAuthWarehouseId = authWarehouseIdList.filter(function(authWarehouseInfo){
            return authWarehouseInfo.authId == dataItem.warehouseId;
        });

        if ( authWarehouseIdList == null ){
        	authWarehouseIdList = [];
        }

        if( Object.keys(duplAuthWarehouseId).length > 0 ){
            fnKendoMessage({ message : "이미 선택된 출고처입니다." });
            return;
        }

		//중복된 값이 없을 경우
		viewModel.employeeInfo.set("isAuthListChanged", "Y");
        authWarehouseIdList.push({
        	authId : dataItem.warehouseId
        	, authName : dataItem.warehouseName
        	, authIdTp : 'W'
        });
	}
	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -------------------------------

	$scope.fnSave = function() { fnSave(); }; // 저장

	//------------------------------- Html 버튼 바인딩  End -------------------------------

});
// document ready - END
