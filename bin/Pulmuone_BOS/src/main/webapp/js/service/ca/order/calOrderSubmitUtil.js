/**-----------------------------------------------------------------------------
 * description 		 : 주문정산 관련 Submit
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.05		이명수          최초생성
 * **/
var calOrderSubmitUtil = {
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
    clear: function() {
        var form = $("#searchForm");
        form.formClear(true);
        form.find("input[type='checkbox']").prop("checked", true);
        calOrderSearchUtil.getCalculateMonth();

        $("#dateSearchStart").data("kendoDatePicker").value( fnGetDayAdd(fnGetToday(), -30) ); // 시작 기본날짜
        $("#dateSearchEnd").data("kendoDatePicker").value( fnGetToday() ); // 종료 기본날짜
        $('[data-id="fnDateBtnC"]').mousedown();
    },
    confirmProc: function(){
        alert("확정완료");
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