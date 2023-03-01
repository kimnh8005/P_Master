/**-----------------------------------------------------------------------------
 * description    : 임직원 정보
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.22		손진구          최초생성
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
			PG_ID : "pulmuoneEmployee",
			callback : fnUI
		});

	};

	// 초기 UI 셋팅
	function fnUI() {

		fnInitButton(); // Initialize Button  ---------------------------------
		fnInitGrid(); // Initialize Grid ------------------------------------
		fnInitOptionBox(); //Initialize Option Box ------------------------------------
		fnPreventSubmit(); // enter 입력방지 공통
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

		aGridDs.query(query);
	};

	// 초기화
	function fnClear() {
		$("#searchForm").formClear(true);
		$("#findKeyword").val("").attr("disabled", true);
	};
	//--------------------------------- Button End---------------------------------

	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid() {

		aGridDs = fnGetPagingDataSource({
			url : "/admin/user/employee/getPulmuoneEmployeeList",
			pageSize : PAGE_SIZE,
			requestEnd : function(e){
				if( e.response.data.lastUpdateDate != null ){
				    $("#lastUpdateDate").text( e.response.data.lastUpdateDate );
				}else{
				    $("#lastUpdateDate").text( "" );
				}
			}
		});

		aGridOpt = {
			dataSource : aGridDs,
			pageable : {
			    pageSizes: [20, 30, 50],
				buttonCount : 10
			},
			navigatable : true,
			columns : [
			            {title : "NO", width : "50px", attributes : {style : "text-align:center"}, template : "<span class='row-number'></span>" }
			          , { field : "erpEmployeeNumber", title : "사번", width : "70px", attributes : {style : "text-align:center"} }
			          , { field : "erpEmployeeName", title : "직원명", width : "50px", attributes : {style : "text-align:center"} }
			          , { field : "erpRegalName", title : "법인정보", width : "140px", attributes : {style : "text-align:left"} }
			          , { field : "erpOrganizationName", title : "조직", width : "200px", attributes : {style : "text-align:center"} }
			          , { field : "erpPositionName", title : "직책", width : "60px", attributes : {style : "text-align:center"} }
			          , { field : "erpEmail", title : "이메일", width : "130px", attributes : {style : "text-align:center"} }
			          , { field : "erpCellPhone", title : "휴대폰", width : "90px", attributes : {style : "text-align:center"} }
			          , { field : "erpStatusName", title : "상태(ERP)", width : "50px", attributes : {style : "text-align:center"} }
			          , { field : "statusName", title : "상태(BOS)", width : "50px", attributes : {style : "text-align:center"} }
			          , { field : "accountManagement", title : "계정 관리", width : "90px", attributes : {style : "text-align:center"},
			              template: function(dataItem) {
			                  if( !dataItem.status ){
			                      let newCreateOption = dataItem.erpStatusName == "퇴사" ? "disabled" : "kind='newCreate'";

			                      return "<a href='#' role='button' class='k-button k-button-icontext' " + newCreateOption + ">신규 생성</a>";
			                  }else{
			                      return "생성완료";
			                  }
			              }
			            }
			          ]
		};

		aGrid = $("#aGrid").initializeKendoGrid(aGridOpt).cKendoGrid();

		aGrid.bind("dataBound", function(){
		   let rowNum = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);

		   $("#aGrid tbody > tr .row-number").each(function(index){
		      $(this).html(rowNum);
		      rowNum--;
		   });

		   $("#countTotalSpan").text( kendo.toString(aGridDs._total, "n0") );
		});

		// 신규생성 버튼 클릭
        $('#aGrid').on("click", "a[kind=newCreate]", function(e) {
            e.preventDefault();

            let dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));
            let params = {};
            params.erpEmployeeNumber = dataItem.erpEmployeeNumber;

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

        });
	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	// 옵션 초기화
	function fnInitOptionBox() {

        // 검색조건
	    let searchConditionDropDown =  fnKendoDropDownList(
	    {
	      id : "searchCondition",
	      data : [ {"CODE" : "EMPLOYEE_NAME", "NAME" : "직원명"},
                   {"CODE" : "EMPLOYEE_NUMBER", "NAME" : "사번"},
                   {"CODE" : "REGAL_NAME", "NAME" : "법인정보"}
                 ],
          valueField : "CODE",
          textField : "NAME",
          blank : "전체"
        });

        // 검색구분 DropDown 변경 이벤트
	    searchConditionDropDown.bind("change", function() {
            if( $("#searchCondition").val() == "" ){
                $("#findKeyword").val("").attr("disabled", true);
            }else{
                $("#findKeyword").attr("disabled", false);
            }
        });

	    // 입력제한
	    fnInputValidationForHangulAlphabetNumber("findKeyword"); // 검색어
	}

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	// ERP 임직원정보 연동
	function fnGetErpEmployee(){

        fnKendoMessage({
            type    : "confirm",
            message : "실행 할 경우 관리자 회원정보에 영향을 줄 수 있습니다. 실행하시겠습니까?",
            ok      : function(){
                fnAjax({
                    url     : "/admin/user/employee/getErpEmployeeAndErpOrganization",
                    method : "GET",
                    params  : {},
                    success : function( data ){
                        fnSearch();
                    },
                    isAction : "select"
                });
            },
            cancel  : function(){

            }
        });
	};

	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -------------------------------

	$scope.fnSearch = function() { fnSearch(); }; /* 조회 */
	$scope.fnClear = function() { fnClear(); }; /* 초기화 */
	$scope.fnGetErpEmployee = function() { fnGetErpEmployee(); }; /* ERP 임직원정보 연동 */

	//------------------------------- Html 버튼 바인딩  End -------------------------------

});
// document ready - END
