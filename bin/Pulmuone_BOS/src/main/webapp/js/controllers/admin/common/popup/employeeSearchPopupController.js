﻿/**-----------------------------------------------------------------------------
 * description    : BOS 계정 선택
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.14 	박승현          최초생성
 * @
 *
**/
"use strict";

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

var initializedSeachCond = false;

$(document).ready(function() {
	fnInitialize(); // Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID : "employeeSearchPopup",
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

		let query = { page : 1,
			          pageSize : PAGE_SIZE,
			          filterLength : fnSearchData(data).length,
			          filter : { filters : fnSearchData(data) }
		};
		initializedSeachCond = true;
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
			            { title : 'No'		, width:'50px',attributes:{ style:'text-align:center' },template:"<span class='row-number'></span>"}
			          , { field : "loginId", title : "ID", width : "80px", attributes : {style : "text-align:center"} }
			          , { field : "userName", title : "이름", width : "50px", attributes : {style : "text-align:center"} }
			          , { field : "email", title : "이메일", width : "120px", attributes : {style : "text-align:center"} }
			          , { field : "mobile", title : "휴대폰번호", width : "90px", attributes : {style : "text-align:center"},
			              template : function(dataItem){
			                  let encodeMobile = kendo.htmlEncode(dataItem.mobile);
			                  return fnPhoneNumberHyphen(encodeMobile);
			              }
			            }
			          , { field : "organizationName", title : "조직정보", width : "120px", attributes : {style : "text-align:center"} }
			          ,{ title : "관리", width: "60px", attributes:{ style:'text-align:center;'  , class:'forbiz-cell-readonly' }
							,command: [ { name: 'cEdit', text: '선택', imageClass: "k-i-add", className: "f-grid-add k-margin5", iconClass: "k-icon", click: fnSelectEmployee,
									click: function(e) {
										 e.preventDefault();
								            var tr = $(e.target).closest("tr"); // get the current table row (tr)
								            var data = this.dataItem(tr);
								            fnSelectEmployee(data);
									}
							}]
						}
			          , { field:'adminTypeName'		,hidden:true}
			          , { field:'adminType'			,hidden:true}
			          , { field:'clientName'		,hidden:true}
			          , { field:'userId'			,hidden:true}
			          , { field:'userStatusName'	,hidden:true}
			          , { field:'userStatus'		,hidden:true}
			          ]
			, requestStart : function(e) { // 검색이 한번도 실행이 안되었을 경우는 실행하지 않는다.(검색실행전 목록수 변경등에서 조회 방지)
				if (initializedSeachCond == false) e.preventDefault();
			}
		};

		aGrid = $("#aGrid").initializeKendoGrid(aGridOpt).cKendoGrid();
        aGrid.bind("dataBound", function(){
        	var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(row_num);
				row_num--;
			});

        	$('#countTotalSpan').text(aGridDs._total);
        });
	};

	function fnSelectEmployee(params){
		if(params){
			parent.POP_PARAM = params;
		}
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	}

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

        $('input:checkbox[name="userStatus"]:input[value="EMPLOYEE_STATUS.RESIGN"]').parent().remove();

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
	};

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	// 기본 설정
	function fnDefaultSet(){
	    $('[data-id="fnDateBtn3"]').mousedown();
	    $("input[name=adminType]").eq(1).prop("checked", true).trigger("change"); // 관리자유형 전체선택
	    $("input[name=userStatus]").eq(1).prop("checked", true).trigger("change"); // 회원상태 전체선택
	    $("input[name=userStatus]").eq(3).prop("checked", true).trigger("change"); // 회원상태 전체선택
	};


	// 콜백
	function fnBizCallback(id, data) {
	};

	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -------------------------------

	$scope.fnSearch = function() { fnSearch(); }; /* 조회 */
	$scope.fnClear = function() { fnClear(); }; /* 초기화 */

	//------------------------------- Html 버튼 바인딩  End -------------------------------

});
// document ready - END
