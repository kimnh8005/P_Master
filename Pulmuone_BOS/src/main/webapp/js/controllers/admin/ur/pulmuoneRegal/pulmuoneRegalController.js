/**-----------------------------------------------------------------------------
 * description    : 법인 정보
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.23		손진구          최초생성
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
			PG_ID : "pulmuoneRegal",
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
		$("input:radio[name='employeeBenefitsApply']").eq(0).prop("checked", true);
	};
	//--------------------------------- Button End---------------------------------

	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid() {

		aGridDs = fnGetPagingDataSource({
			url : "/admin/user/employee/getPulmuoneRegalList",
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
			          , { field : "erpRegalCode", title : "법인코드", width : "100px", attributes : {style : "text-align:center"} }
			          , { field : "erpRegalName", title : "법인명", width : "300px", attributes : {style : "text-align:center"} }
			          , { field : "employeeDiscountYn", title : "임직원 혜택적용", width : "100px", attributes : {style : "text-align:center"},
			              template : "#= (employeeDiscountYn == 'Y') ? '예' : '아니오' #" }
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
	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	// 옵션 초기화
	function fnInitOptionBox() {

        // 검색조건
	    let searchConditionDropDown =  fnKendoDropDownList(
	    {
	      id : "searchCondition",
	      data : [ {"CODE" : "ERP_REGAL_NAME", "NAME" : "법인명"},
                   {"CODE" : "ERP_REGAL_CODE", "NAME" : "법인코드"}
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

        // 임직원 혜택적용
        fnTagMkRadio({
            id : "employeeBenefitsApply",
            tagId : "employeeBenefitsApply",
            data : [
                { "CODE" : "", "NAME" : "전체" },
                { "CODE" : "Y", "NAME" : "예" },
                { "CODE" : "N", "NAME" : "아니오" }
            ],
            chkVal : "",
            style : {}
        });

	    // 입력제한
	    fnInputValidationForHangulAlphabetNumber("findKeyword"); // 검색어
	}

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -------------------------------

	$scope.fnSearch = function() { fnSearch(); }; /* 조회 */
	$scope.fnClear = function() { fnClear(); }; /* 초기화 */

	//------------------------------- Html 버튼 바인딩  End -------------------------------

});
// document ready - END
