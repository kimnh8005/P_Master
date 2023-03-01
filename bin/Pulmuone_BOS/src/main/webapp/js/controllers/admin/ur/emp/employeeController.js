/**-----------------------------------------------------------------------------
 * description    : BOS 계정관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.24		손진구          최초생성
 * @ 2020.11.16      최성현          개인정보 열람 권한 설정 노출
 * @
 * **/
"use strict";

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
	fnInitialize(); // Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize() {
		$scope.$emit("fnIsMenu", {
			flag : "true"
		});

		fnPageInfo({
			PG_ID : "employee",
			callback : fnUI
		});

	};

	// 초기 UI 셋팅
	function fnUI() {

		fnInitButton(); // Initialize Button  ---------------------------------
		fnInitGrid(); // Initialize Grid ------------------------------------
		fnInitOptionBox(); //Initialize Option Box ------------------------------------
		fnPreventSubmit(); // enter 입력방지 공통
		fnDefaultSet();
	};

	//--------------------------------- Button Start---------------------------------
	// 버튼 초기화
	function fnInitButton() {
		$("#fnSearch, #fnClear").kendoButton();

	};

	// 조회
	function fnSearch() {
	    let data = $("#searchForm").formSerialize(true);

		let createDateStart	= $.trim($("#createDateStart").val());
		let createDateEnd	= $.trim($("#createDateEnd").val());

		if(createDateStart != "" && createDateEnd == ""){
			fnKendoMessage({message : "종료일을 선택해주세요", ok : function() {
					$("#createDateEnd").focus();
			}});
			return false;
		}
		if(createDateStart == "" && createDateEnd != ""){
			fnKendoMessage({message : "시작일을 선택해주세요", ok : function() {
					$("#createDateEnd").focus();
				}});
			return false;
		}


		//달력 시작일 종료일 체크
		if($("#createDateStart").val() > $("#createDateEnd").val()) {
				fnKendoMessage({message : "시작일을 종료일보다 뒤로 설정할 수 없습니다.", ok : function() {
					$("#createDateEnd").focus();
				}});
			return false;
		}

		let query = { page : 1,
			          pageSize : PAGE_SIZE,
			          filterLength : fnSearchData(data).length,
			          filter : { filters : fnSearchData(data) }
		};

		aGridDs.query(query);
	};

	// 초기화
	function fnClear() {
		$("#searchForm").formClear(true);
		fnDefaultSet();
	};
	//--------------------------------- Button End---------------------------------

	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid() {

		aGridDs = fnGetPagingDataSource({
			url : "/admin/user/employee/getEmployeeList",
			pageSize : PAGE_SIZE
		});

		aGridOpt = {
			dataSource : aGridDs,
			pageable : { pageSizes: [20, 30, 50], buttonCount : 10 },
			navigatable : true,
			columns : [
			            { field : "adminTypeName", title : "관리자<BR>유형", width : "50px", attributes : {style : "text-align:center"} }
			          , { field : "loginId", title : "ID", width : "80px", attributes : {style : "text-align:center"} }
			          , { field : "userName", title : "이름", width : "50px", attributes : {style : "text-align:center"} }
			          , { field : "email", title : "이메일", width : "120px", attributes : {style : "text-align:center"} }
			          , { field : "mobile", title : "휴대폰번호", width : "90px", attributes : {style : "text-align:center"},
			              template : function(dataItem){
			                  let encodeMobile = kendo.htmlEncode(dataItem.mobile);
			                  return fnPhoneNumberHyphen(encodeMobile);
			              }
			            }
			          , { field : "userStatusName", title : "회원상태", width : "60px", attributes : {style : "text-align:center"} }
			          , { field : "personalInfoAccessYn", title : "개인정보<BR>열람권한", width : "70px", attributes : {style : "text-align:center"},
			              template : function(dataItem){
			          			if(dataItem.personalInfoAccessYn == "N") {
									return "권한없음";
								} else if(dataItem.personalInfoAccessYn == "Y") {
			          				return "레벨1"
								} else if(dataItem.personalInfoAccessYn == "M") {
			          				return "레벨2"
								} else {
			          				return ""
								}

			              }
			            }
			          , { field : "createDateAndLastVisitDate", title : "등록(가입)일<BR>최근 접속일", width : "160px", attributes : {style : "text-align:center"},
			              template : function(dataItem){
			                  return dataItem.createDate + "<BR>" + dataItem.lastVisitDate;
			              }
			            }
			          , { field : "management", title : "관리", width : "90px", attributes : {style : "text-align:center"},
			              template : function(dataItem){
			          			let btn = "";
			          			if(fnIsProgramAuth("RESEND_PW")) {
									btn += "<a href='#' role='button' class='k-button k-button-icontext item-command-btn' kind='passwordPush' >PW재발송</a>"+"<BR>";
								}
							  	if(fnIsProgramAuth("SAVE")) {
									btn += "<a href='#' role='button' class='k-button k-button-icontext item-command-btn' kind='modify' >수정</a>";
							  	}
			                  return btn;
			              }
			            }
			          ]
		};

		aGrid = $("#aGrid").initializeKendoGrid(aGridOpt).cKendoGrid();
        aGrid.bind("dataBound", function() {
			//total count
            $('#countTotalSpan').text(aGridDs._total);
        });

		// PW재발송 버튼 클릭
        $('#aGrid').on("click", "a[kind=passwordPush]", function(e) {
            e.preventDefault();

            let dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));
            let param = {};

            param.mobile = fnPhoneNumberHyphen( kendo.htmlEncode(dataItem.mobile) );
            param.mail = dataItem.email;
            param.urUserId = dataItem.userId;
            param.userType = 'EMPLOYEE';
            param.loginId = dataItem.loginId;

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
        });

        // 수정 버튼 클릭
        $('#aGrid').on("click", "a[kind=modify]", function(e) {
            e.preventDefault();

            let dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));
            let param = {};
            param.employeeNumber = dataItem.employeeNumber;

            fnGoEmployeePopup( param );
        });
	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	// 옵션 초기화
	function fnInitOptionBox() {

        // 검색조건
	    fnKendoDropDownList(
	    {
	      id : "searchCondition",
	      data : [ {"CODE" : "ID", "NAME" : "아이디"}
                 , {"CODE" : "NAME", "NAME" : "이름"}
                 , {"CODE" : "EMAIL", "NAME" : "이메일"}
                 , {"CODE" : "MOBILE", "NAME" : "휴대폰번호"}
                 ],
          valueField : "CODE",
          textField : "NAME"
        });

        // 관리자 유형
        fnTagMkChkBox({
            id : "adminType",
            url : "/admin/comn/getCodeList",
            tagId : "adminType",
            async : false,
            style : {},
            params : {"stCommonCodeMasterCode" : "COMPANY_TYPE", "useYn" :"Y"},
            beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }]
        });

        // 회원상태
        fnTagMkChkBox({
            id : "userStatus",
            url : "/admin/comn/getCodeList",
            tagId : "userStatus",
            async : false,
            style : {},
            params : {"stCommonCodeMasterCode" : "EMPLOYEE_STATUS", "useYn" :"Y"},
            beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }]
        });

        fbCheckboxChange();

        // 등록(가입)일 시작
        fnKendoDatePicker({
            id: "createDateStart",
            format: "yyyy-MM-dd",
            btnStartId: "createDateStart",
            btnEndId: "createDateEnd",
            change : function(e) {
                fnStartCalChange("createDateStart", "createDateEnd");
            }
        });

        // 등록(가입)일 종료
        fnKendoDatePicker({
            id: "createDateEnd",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "createDateStart",
						btnEndId: "createDateEnd",
						defType: "oneWeek",
            change : function(e) {
                fnEndCalChange("createDateStart", "createDateEnd");
            }
        });

        // 검색기간 초기화 버튼 클릭
        // $('[data-id="fnDateBtnC"]').on("click", function(){
        //     $('[data-id="fnDateBtn3"]').mousedown();
        // });
	};

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	// 기본 설정
	function fnDefaultSet(){
	    $('[data-id="fnDateBtn3"]').mousedown();
	    $("input[name=adminType]").eq(0).prop("checked", true).trigger("change"); // 관리자유형 전체선택
	    $("input[name=userStatus]").eq(0).prop("checked", true).trigger("change"); // 회원상태 전체선택
	};

	// 신규등록
	function fnNewAdd(){
	    fnGoEmployeePopup();
	};

    // BOS 계정관리 상세화면 팝업
    function fnGoEmployeePopup( params ){
        if( params == undefined ){
            params = { employeeNumber : "" };
        }

        fnKendoPopup({
            id     : "employeeMgm",
            title  : "BOS 계정관리",
            src    : "#/employeeMgm",
            param  : params,
            width  : "1150px",
            height : "900px",
            success: function( id, data ){

            }
        });
    };

	// 콜백
	function fnBizCallback(id, data) {
	};

	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -------------------------------

	$scope.fnSearch = function() { fnSearch(); }; /* 조회 */
	$scope.fnClear = function() { fnClear(); }; /* 초기화 */
	$scope.fnNewAdd = function() { fnNewAdd(); }; /* 신규등록 */

	//------------------------------- Html 버튼 바인딩  End -------------------------------

});
// document ready - END
