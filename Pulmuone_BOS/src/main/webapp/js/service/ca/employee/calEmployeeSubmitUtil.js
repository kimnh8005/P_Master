/**-----------------------------------------------------------------------------
 * description 		 : 정산관리 > 임직원정산 > 임직원 지원 정산
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.03		이명수          최초생성
 * **/
var calEmployeeSubmitUtil = {
    search: function() {
        if( !fnInputValidate() ) return;

        var grid = $("#defaultGrid").data("kendoGrid");
        var gridDs = grid.dataSource;

        var form = $("#searchForm");

        // form.formClear(false);
        var data = form.formSerialize(true);



        //data = {"categoryType":""}; //url 호출 위해 임시
        const _pageSize = grid && grid.dataSource ? grid.dataSource.pageSize() : PAGE_SIZE;

        var query = {
            page: 1,
            pageSize: _pageSize,
            filterLength: fnSearchData(data).length,
            filter: {
                filters: fnSearchData(data)
            }
        };

        gridDs.query(query);
    },
    searchUse: function() {
        if( !fnInputValidateUse() ) return;

        var grid = $("#defaultGrid").data("kendoGrid");
        var gridDs = grid.dataSource;

        var form = $("#searchForm");

        // form.formClear(false);
        var data = form.formSerialize(true);



        //data = {"categoryType":""}; //url 호출 위해 임시
        const _pageSize = grid && grid.dataSource ? grid.dataSource.pageSize() : PAGE_SIZE;

        var query = {
            page: 1,
            pageSize: _pageSize,
            filterLength: fnSearchData(data).length,
            filter: {
                filters: fnSearchData(data)
            }
        };

        gridDs.query(query);
    },
    clear: function() {
        var form = $("#searchForm");
        form.formClear(true);
        form.find("input[type='checkbox']").prop("checked", true);
        calEmployeeSearchUtil.getCalculateMonth();

        $("#dateSearchStart").data("kendoDatePicker").value( fnGetDayAdd(fnGetToday(), -30) ); // 시작 기본날짜
        $("#dateSearchEnd").data("kendoDatePicker").value( fnGetToday() ); // 종료 기본날짜
        $('[data-id="fnDateBtnC"]').mousedown();
    },
    confirmProc: function(){
        let selectRows  = $("#defaultGrid").find("input[name=rowCheckbox]:checked").closest("tr");

        if( selectRows.length == 0 ){
            fnKendoMessage({ message : "변경할 내역을 선택해주세요" });
            return;
        }

        fnKendoMessage({
            type    : "confirm",
            message : "임직원 지원금 정산을 확정 완료하시겠습니까?",
            ok      : function(){

                let params = {};
                params.settleMonth = [];
                params.ouId = [];
                params.sessionId = [];

                for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){

                    let dataItem = defaultGrid.dataItem($(selectRows[i]));

                    params.settleMonth[i] = dataItem["settleMonth"];
                    params.ouId[i] = dataItem["ouId"];
                    params.sessionId[i] = dataItem["sessionId"];

                }

                fnAjax({
                    url     : "/admin/calculate/employee/putCalculateConfirmProc",
                    params  : params,
                    success : function( data ){
                        fnKendoMessage({ message : "저장이 완료되었습니다.", ok : calEmployeeSubmitUtil.search() });
                    },
                    error : function(xhr, status, strError){
                        fnKendoMessage({ message : xhr.responseText });
                    },
                    isAction : "update"
                });
            },
            cancel  : function(){

            }
        });
    },
    excelDownload: function(url){

        var grid = $("#defaultGrid").data("kendoGrid");
        var gridDs = grid.dataSource;

        if(gridDs._pristineData.length <= 0){
            fnKendoMessage({ message : "조회 후 다운로드 가능합니다." });
            return;
        }
        var form = $("#searchForm");
        var data = form.formSerialize(true);

        fnExcelDownload(url, data);
    }
}