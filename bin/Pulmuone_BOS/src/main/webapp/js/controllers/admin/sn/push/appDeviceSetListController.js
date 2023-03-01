/**-----------------------------------------------------------------------------
 * system            : APP 설치 회원
 * @
 * @ 수정일            수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.11     안치열          최초생성
 * @
 * **/
"use strict";

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
    fnInitialize(); //Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : "true" });

        fnPageInfo({
            PG_ID  : "appDeviceSetList",
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){

        fnInitButton(); //Initialize Button  ---------------------------------
        fnInitOptionBox();//Initialize Option Box ------------------------------------
        fnInitGrid();   //Initialize Grid ------------------------------------
        fnSearch();

        //탭 변경 이벤트
        fbTabChange();			// fbCommonController.js - fbTabChange 이벤트 호출
    };

	//--------------------------------- Button Start---------------------------------
    // 버튼 초기화
	function fnInitButton(){
		$("#fnSearch, #fnClear, #fnSelectUserPushSend, #fnSearchUserPushSend, #fnImageUpload").kendoButton();
	};

    // 조회
    function fnSearch(){
        if( fnSearchValidation() == false ){
            return;
        }

        let data = $("#searchForm").formSerialize(true);

        let query = {
                    page         : 1,
                    pageSize     : PAGE_SIZE,
                    filterLength : fnSearchData(data).length,
                    filter       : { filters : fnSearchData(data) }
        };

        aGridDs.query(query);
    };

    // 조회 검증
    function fnSearchValidation(){
        let searchClassificationTextareaTrimReplaceValue = $.trim($("#searchClassificationTextareaValue").val()).replace(/\n/g, "");

        if( $("#joinDateStart").val() == "" && $("#joinDateEnd").val() != ""){
            return fnAlertMessage("가입일 시작일 또는 종료일을 입력해주세요.", "joinDateStart");
        }

        if( $("#joinDateStart").val() != "" && $("#joinDateEnd").val() == ""){
            return fnAlertMessage("가입일 시작일 또는 종료일을 입력해주세요.", "joinDateEnd");
        }

        if( $("#joinDateStart").val() > $("#joinDateEnd").val()){
            return fnAlertMessage("가입일 시작일을 종료일보다 뒤로 설정할 수 없습니다.", "joinDateStart");
        }

        if( $("#lastVisitDateStart").val() == "" && $("#lastVisitDateEnd").val() != ""){
            return fnAlertMessage("최근방문일자 시작일 또는 종료일을 입력해주세요.", "lastVisitDateStart");
        }

        if( $("#lastVisitDateStart").val() != "" && $("#lastVisitDateEnd").val() == ""){
            return fnAlertMessage("최근방문일자 시작일 또는 종료일을 입력해주세요.", "lastVisitDateEnd");
        }

        if( $("#lastVisitDateStart").val() > $("#lastVisitDateEnd").val()){
            return fnAlertMessage("최근방문일자 시작일을 종료일보다 뒤로 설정할 수 없습니다.", "lastVisitDateStart");
        }


        return true;
    };

    //--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid(){

        aGridDs = fnGetPagingDataSource({
            url      : "/admin/ur/userDevice/getBuyerDeviceList",
            pageSize : PAGE_SIZE
        });

        aGridOpt = {
            dataSource : aGridDs,
            pageable : { pageSizes: [20, 30, 50], buttonCount : 10 },
            navigatable: true,
            columns   : [
                          {field : 'chk', title : "체크박스", width:"36px", attributes : {style:"text-align:center"}
                           , template : "<input type='checkbox' class='aGridCheckbox' id='aGridCheckbox' name='aGridCheckbox' />"
                           , headerTemplate : "<input type='checkbox' id='checkBoxAll' name='aGridCheckbox' />"
                           , filterable: false
                          }
                        , {field : 'no', title : 'No.', width : '60px', attributes : {style : 'text-align:center'},template:"<span class='row-number'></span>"}
                        , {field : 'deviceInfoId', hidden:true}
                        , {field : 'userTypeName', title : '회원유형', width : '60px', attributes : {style : 'text-align:center'}}
                        , {field : 'userLevelName', title : '회원등급', width : '60px', attributes : {style : 'text-align:center'}}
                        , {field : 'userStatus', 	 title : '회원상태', width : '60px', attributes : {style : 'text-align:center'}}
                        , {field : 'userName', title : '회원명', width : '80px', attributes : {style : 'text-align:center'}}
                        , {field : 'userId', title : '회원ID', width : '80px', attributes : {style : 'text-align:center'}}
                        , {field : 'mobile', title : '휴대폰', width : '120px', attributes : {style : 'text-align:center'}
	                        , template: function(dataItem) {
								var mobile = kendo.htmlEncode(dataItem.mobile);
								return fnPhoneNumberHyphen(mobile);
		                    }
                        }
                        , {field : 'email', title : 'EMAIL', width : '150px', attributes : {style : 'text-align:center'}}
                        , {field : 'joinDate', title : '가입일자', width : '80px', attributes : {style : 'text-align:center'}}
                        , {field : 'lastVisitDate', title : '최근방문일자', width : '80px', attributes : {style : 'text-align:center'}}
                        , {field : 'deviceType', hidden:true}
                        , {field : 'deviceTypeName', title : '플랫폼 유형', width : '60px', attributes : {style : 'text-align:center'}}
                        , {field : 'reception', 	 title : 'PUSH<BR>수신여부', width : '60px', attributes : {style : 'text-align:center'}, template : kendo.template($("#reception").html())}
                        , {field : 'nightReception', title : '야간<BR>수신여부', width : '60px', attributes : {style : 'text-align:center'}, template : kendo.template($("#nightReception").html())}
            ]
        };

        aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

        aGrid.bind('dataBound', function(e){
        	var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(row_num);
				row_num--;
			});

			//total count
            $('#countTotalSpan').text(aGridDs._total);

            $('#checkBoxAll').prop('checked', false);

            fnSetAllCheckbox('aGridCheckbox', 'checkBoxAll', true);

            $('input[type=checkbox]').on("change", function (){
                fnSetAllCheckbox('aGridCheckbox', 'checkBoxAll', true);
            });
        });
    };
    //------------------------------- Grid End -------------------------------

    //-------------------------------  Common Function start -------------------------------

    // 옵션 초기화
    function fnInitOptionBox() {
        //-------------------------------  메인화면 Function start -------------------------------

    	// 발송플랫폼유형 라디오
		fnTagMkRadio({
				id    :  "sendGroup",
	            data  : [ { "CODE" : "APP_OS_TYPE.ALL", "NAME":"전체"}
                        , { "CODE" : "APP_OS_TYPE.IOS", "NAME":"iOS"}
                        , { "CODE" : "APP_OS_TYPE.ANDROID", "NAME":"Android"} ],
				tagId : "sendGroup",
				chkVal: 'APP_OS_TYPE.ALL',
				style : {},
				change : function(e){
					let sendGroupValue = $("#sendGroup").getRadioVal();

					if( sendGroupValue == "APP_OS_TYPE.ALL" ){
					    $("#pushTitleAndroidView").show();
						$("#pushTitleIosView").show();
					}else if( sendGroupValue == "APP_OS_TYPE.ANDROID" ){
						$("#pushTitleAndroidView").show();
						$("#pushTitleIosView").hide();
					}else if( sendGroupValue == "APP_OS_TYPE.IOS" ){
						$("#pushTitleAndroidView").hide();
						$("#pushTitleIosView").show();
					}
				}
		});

    	fnTagMkRadio({
	        id: 'selectConditionType',
	        tagId: 'selectConditionType',
	        chkVal: 'singleSection',
	        tab: true,
	        data: [{
	            CODE: "singleSection",
	            NAME: "단일조건 검색",
	            TAB_CONTENT_NAME: "singleSection"
	        }, {
	            CODE: "multiSection",
	            NAME: "복수조건 검색",
	            TAB_CONTENT_NAME: "multiSection"
	        }],
	    });
	    //[공통] 탭 변경 이벤트
	    fbTabChange();

	    //전체회원 단일조건
		fnKendoDropDownList({
			id    : 'condiType',
			data  : [{ "CODE" : "userName" , "NAME":"회원명"},
				     { "CODE" : "loginId" , "NAME":"회원ID"}
					]
		});

        // 회원유형
    	fnKendoDropDownList({
    		id  : 'userType',
    		data  : [
    			{"CODE":"NORMAL"	,"NAME":"일반"},
    			{"CODE":"EMPLOYEE"	,"NAME":"임직원"}
    		],
    		valueField : "CODE",
    		textField :"NAME",
    		blank : "전체"
    	});
        // 회원등급
        fnKendoDropDownList({
            id  : "userLevel",
            url : "/admin/comn/getUserGroupList",
            params: {},
            valueField : "groupId",
            textField :"groupName",
            blank : "전체"
        });

        // 가입일 시작
        fnKendoDatePicker({
            id: "joinDateStart",
            format: "yyyy-MM-dd"
        });

        // 가입일 종료
        fnKendoDatePicker({
            id: "joinDateEnd",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "joinDateStart",
            btnEndId: "joinDateEnd"
        });

        // 최근방문일자 시작
        fnKendoDatePicker({
            id: "lastVisitDateStart",
            format: "yyyy-MM-dd"
        });

        // 최근방문일자 종료
        fnKendoDatePicker({
            id: "lastVisitDateEnd",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "lastVisitDateStart",
            btnEndId: "lastVisitDateEnd"
        });

        // 기기타입
        fnTagMkRadio({
            id    : "deviceType",
            tagId : "deviceType",
            url   : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "APP_OS_TYPE", "useYn" :"Y"},
            async : false,
            beforeData : [{"CODE":"", "NAME":"전체"}],
            chkVal : "",
            style : {},
            change : function(e){}
        });

        // PUSH 수신여부
        fnTagMkRadio({
            id : "pushReception",
            tagId : "pushReception",
            data : [
                { "CODE" : "", "NAME" : "전체" },
                { "CODE" : "Y", "NAME" : "수신" },
                { "CODE" : "N", "NAME" : "거부" }
            ],
            chkVal : "",
            style : {}
        });

        // 야간 수신여부
        fnTagMkRadio({
            id : "nightPushReception",
            tagId : "nightPushReception",
            data : [
                { "CODE" : "", "NAME" : "전체" },
                { "CODE" : "Y", "NAME" : "수신" },
                { "CODE" : "N", "NAME" : "거부" }
            ],
            chkVal : "",
            style : {}
        });

        // PUSH발송 click
        $(".modify-form header").on("click", function(){
            $(".modify-form header").next().toggleClass("active");
        });

        //-------------------------------  메인화면 Function end -------------------------------

        //-------------------------------  PUSH발송 Function start -----------------------------
        // 발송대상옵션
        fnKendoDropDownList({
            id : 'sendTargetOption',
            data : [
                    {"CODE":"1", "NAME":"PUSH 수신동의자"},
                    {"CODE":"2", "NAME":"전체 회원"}
            ],
            valueField : "CODE",
            textField :"NAME"
        });

        // 광고/공지타입
        fnKendoDropDownList({
            id  : 'advertAndNoticeType',
            url : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "PUSH_SEND_TYPE"},
            blank : "선택"
        });

        // 발송구분
        fnTagMkRadio({
            id: "sendClassification",
            tagId : "sendClassification",
            data  : [
                      { "CODE" : "N" , "NAME" : "즉시"}
                    , { "CODE" : "Y" , "NAME" : "예약"}
                    ],
            chkVal: "N",
            change : function(e){

                if($("#sendClassification").getRadioVal() == "N"){
                    $("#reservationDateViewControl").hide();
                    $("#reservationDate").attr("required" , false);
                }else{
                    $("#reservationDateViewControl").show();
                    $("#reservationDate").attr("required" , true);
                }
            }
        });

        // 발송구분 예약날짜
        $("#reservationDate").kendoDateTimePicker({
            format : "yyyy-MM-dd HH:mm",
            value : fnGetToday("yyyy-MM-dd HH:mm"),
            max : fnGetDayAdd(fnGetToday(), 6, 'yyyy-MM-dd') + " 23:59",
            min : fnGetToday("yyyy-MM-dd HH:mm")
        });

        // 푸시발송타입
        fnTagMkRadio({
            id    : "pushSendType",
            tagId : "pushSendType",
            url   : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "APP_PUSH_SEND_TYPE", "useYn" :"Y"},
            async : false,
            chkVal : "APP_PUSH_SEND_TYPE.TEXT_IMAGE",
            change : function(e){
                let pushSendTypeValue = $('#pushSendType').getRadioVal();
                if( pushSendTypeValue == "APP_PUSH_SEND_TYPE.TEXT_IMAGE"){
                    $("#imageViewControl").show();
                }else{
                    $("#imageViewControl").hide();
                }
            }
        });

        // 파일첨부
        fnKendoUpload({
            id     : "imageFile",
            select : function(e){
                let f = e.files[0];
                let ext = f.extension.substring(1, f.extension.length);

                if($.inArray(ext.toLowerCase(), ["gif","ico","jpeg","jpg","png"]) == -1){
                    fnKendoMessage({message : "이미지파일만 첨부가능합니다."});
                    e.preventDefault();
                }else{
                    if (typeof(window.FileReader) == "undefined"){
                        $("#basicImage").attr("src", e.sender.element.val());
                    } else {
                        if(f){
                            let reader = new FileReader();

                            reader.onload = function (ele) {
                                $("#basicImage").attr("src", ele.target.result);
                            };

                            reader.readAsDataURL(f.rawFile);

                        }
                    }
                }
            },
            localization : "파일첨부"
        });

        // 변경
        $("#imageChange").on("click", function() {
            $("#imageFile").trigger("click");
        });

        // 삭제
        $("#imageDelete").on("click", function(e){
            e.preventDefault();
            $("#basicImage").attr("src","/contents/images/noimg.png");
            $("#imageFile").data("kendoUpload").clearAllFiles();
        });

        // 회원상태
        fnKendoDropDownList({
			id  : 'userStatus',
			url : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "BUYER_STATUS", "useYn" :"Y"},
			blank : "전체"
		});

        //-------------------------------  PUSH발송 Function end -------------------------------
    };

    // 이미지 파일첨부 버튼 클릭
    function fnImageUpload(){
        $("#imageFile").trigger("click");
    };


    // 첨부파일 유무
    function fnImageYn(){
        if( $("#imageFile").data("kendoUpload").getFiles().length == 0 ){
            return false;
        }else{
            return true;
        }
    };

    // Alert 메세지
    function fnAlertMessage(msg, id){
        fnKendoMessage({ message : msg, ok : function focusValue(){
            $("#" + id).focus();
        }});

        return false;
    };

    // 검색제어
    function fnSearchControl(){
        let searchClassificationValueYn = $("#searchClassification").val() != "" ? true : false;

        fnSearchClassificationControl( $("#searchClassification").val() );
        //fnSearchConditionsControl( searchClassificationValueYn );
    };

    //검색구분 제어
    function fnSearchClassificationControl(searchClassificationValue){

        // 검색구분이 전체일 경우
        if(searchClassificationValue == ""){
            $("#searchClassificationTextValue, #searchClassificationTextareaValue").val("");
            $("#searchClassificationTextValue").css("display", "");
            $("#searchClassificationTextareaValue").css("display", "none");
            $("#searchClassificationTextValue, #searchClassificationTextareaValue").attr("disabled", true);
        } // 검색구분이 회원ID, 회원명 일 경우
        else if(searchClassificationValue == "USER_NAME" || searchClassificationValue == "LOGIN_ID"){
            $("#searchClassificationTextValue").val("");
            $("#searchClassificationTextValue").css("display", "none");
            $("#searchClassificationTextValue").attr("disabled", true);
            $("#searchClassificationTextareaValue").css("display", "");
            $("#searchClassificationTextareaValue").attr("disabled", false);
        } // 검색구분이 모바일, 이메일 일 경우
        else if(searchClassificationValue == "MOBILE" || searchClassificationValue == "MAIL"){
            $("#searchClassificationTextareaValue").val("");
            $("#searchClassificationTextValue").css("display", "");
            $("#searchClassificationTextValue").attr("disabled", false);
            $("#searchClassificationTextareaValue").css("display", "none");
            $("#searchClassificationTextareaValue").attr("disabled", true);
        }
    };

    // 검색조건 제어
    function fnSearchConditionsControl(disableYn){
        $("#userType").data("kendoDropDownList").readonly( disableYn );
        $("#userLevel").data("kendoDropDownList").readonly( disableYn );
        $("#joinDateStart").data("kendoDatePicker").enable( !disableYn );
        $("#joinDateEnd").data("kendoDatePicker").enable( !disableYn );
        $("#lastVisitDateStart").data("kendoDatePicker").enable( !disableYn );
        $("#lastVisitDateEnd").data("kendoDatePicker").enable( !disableYn );
        $(".set-btn-type6").attr("disabled" , disableYn );
        $("input:radio[name=deviceType]").attr("disabled", disableYn );
        $("input:radio[name=pushReception]").attr("disabled", disableYn );
    };

    // 초기화 버튼 클릭
    function fnClear() {
        $("#searchForm").formClear(true);
        $(".set-btn-type6").attr("fb-btn-active" , false );
        $("input:radio[name='deviceType']").eq(0).prop("checked", true);
        $("input:radio[name='pushReception']").eq(0).prop("checked", true);
        //fnSearchControl();
    };

    function fnSelectUserPushSend(){
        let url  = "/admin/sn/push/addPushIssueSelect";
        let inputFormValidator = $("#inputForm").kendoValidator().data("kendoValidator");
        let checkRows  = aGrid.tbody.find("input[type=checkbox]:checked").closest("tr");
        let userDataArray = fnGetUserDataList( checkRows );

        if(checkRows.length <= 0){
            fnKendoMessage({message : "회원을 선택해주세요."});
            return;
        }

        if( $("#pushSendType").getRadioVal() == "APP_PUSH_SEND_TYPE.TEXT_IMAGE" && fnImageYn() == false ){
            fnKendoMessage({message : "이미지 파일첨부를 해주세요."});
            return;
        }

        if( userDataArray.length <= 0 ){
            fnKendoMessage({message : "발송가능한 회원이 없습니다."});
            return;
        }

        if( inputFormValidator.validate() ){
            fnKendoMessage({
                type    : "confirm",
                message : "푸시메시지를 발송하시겠습니까?",
                ok      : function(e){
                              fnInputFormSubmit(url, userDataArray, "addPushIssueSelect");
                },
                cancel  : function(e){  }
            });
        }
    };

    function fnSearchUserPushSend(){
        let url  = "/admin/sn/push/addPushIssueSearch";
        let inputFormValidator = $("#inputForm").kendoValidator().data("kendoValidator");
        let allRows  = aGrid.tbody.find('input[type=checkbox]').closest('tr');
        let userDataArray = fnGetUserDataList( allRows );

        if( allRows.length <= 0 ){
            fnKendoMessage({message : "검색된 회원이 없습니다."});
            return;
        }

        if( $("#pushSendType").getRadioVal() == "APP_PUSH_SEND_TYPE.TEXT_IMAGE" && fnImageYn() == false ){
            fnKendoMessage({message : "이미지 파일첨부를 해주세요."});
            return;
        }

        if( userDataArray.length <= 0 ){
            fnKendoMessage({message : "발송가능한 회원이 없습니다."});
            return;
        }

        if( inputFormValidator.validate() ){
            fnKendoMessage({
                type    : 'confirm',
                message : "푸시메시지를 발송하시겠습니까?",
                ok      : function(e){
                              fnInputFormSubmit(url, userDataArray, "addPushIssueSearch");
                },
                cancel  : function(e){  }
            });
        }
    };

    // PUSH발송 submit
    function fnInputFormSubmit(url, userDataArray, cbId){
        let inputFormData = $("#inputForm").formSerialize(true);

        inputFormData['buyerDevice'] = kendo.stringify( userDataArray );

        if( fnImageYn() ){
            fnAjaxSubmit({
                form    : 'inputForm',
                fileUrl : '/fileUpload',
                url     : url,
                storageType : "public", // 추가
                domain : "ur", 			// 추가
                params  : inputFormData,
                success : function( successData ){
                            fnBizCallback(cbId, successData);
                },
                isAction : 'insert'
            });
        }else{
            fnAjax({
                url     : url,
                params  : inputFormData,
                success : function( successData ){
                            fnBizCallback(cbId, successData);
                },
                isAction : 'insert'
            });
        }
    };

    // 회원 목록 가져오기
    function fnGetUserDataList(rowArray){
        let userDataArray = new Array();

        for(let i = 0, rowLength = rowArray.length; i < rowLength; i++){
            let rowData = aGrid.dataItem( $(rowArray[i]) );
            let userData = {};

            if( $("#sendTargetOption").val() == "1" && rowData.reception == "N" ){
                continue;
            }

            userData.userId = rowData.userId;
            userData.deviceType = rowData.deviceType;
            userData.deviceInfoId = rowData.deviceInfoId;
            userDataArray.push(userData);
        }

        return userDataArray;
    };

    /**
     * 콜백합수
     */
    function fnBizCallback(id, data) {
        switch(id){
            case 'addPushIssueSelect':
                fnKendoMessage({message : "발송하였습니다."});
                $("input[name='aGridCheckbox']:enabled").prop("checked", false);
                break;
            case 'addPushIssueSearch':
                fnKendoMessage({message : "발송하였습니다."});
                $("input[name='aGridCheckbox']:enabled").prop("checked", false);
                break;
        }
    };

    //-------------------------------  Common Function end -------------------------------

    //------------------------------- Html 버튼 바인딩  Start -------------------------------

    $scope.fnSearch = function() { fnSearch(); }; /* Common Search */
    $scope.fnClear = function() { fnClear(); }; /* Common Clear */
    $scope.fnSelectUserPushSend = function() { fnSelectUserPushSend(); }; /* 선택회원 PUSH 발송 */
    $scope.fnSearchUserPushSend = function() { fnSearchUserPushSend(); }; /* 검색회원 PUSH 발송 */
    $scope.fnImageUpload =function(){ fnImageUpload(); }; 				 /* 이미지 파일첨부 버튼 */

    //------------------------------- Html 버튼 바인딩  End -------------------------------
}); // document ready - END
﻿﻿


