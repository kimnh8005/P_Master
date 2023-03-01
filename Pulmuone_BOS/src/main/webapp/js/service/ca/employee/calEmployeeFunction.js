/**-----------------------------------------------------------------------------
 * description 		 : 임직원 관련 공통 함수
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.03		이명수   최초생성
 * **/

var calEmployeeGridEventUtil = {
    gridInit: function(gridUrl, grilColumns){
        var form = $("#searchForm");

        // form.formClear(false);
        var data = form.formSerialize(true);

        if($('input[name=selectConditionType]:checked').length > 0) {
            data['selectConditionType'] = $('input[name=selectConditionType]:checked').val();
        }
        defaultGridDs = fnGetPagingDataSource({
            url      : gridUrl,
            pageSize : PAGE_SIZE,
            filterLength: fnSearchData(data).length,
            filter: {
                filters: fnSearchData(data)
            }
        });

        defaultGridOpt = {
            dataSource: defaultGridDs,
            pageable  : { pageSizes: [20, 30, 50], buttonCount : 10 },
            navigatable : true,
            scrollable : true,
            columns : grilColumns
        };

        defaultGrid = $('#defaultGrid').initializeKendoGrid( defaultGridOpt ).cKendoGrid();

        defaultGrid.bind("dataBound", function(e) {
            $('#countTotalSpan').text( kendo.toString(defaultGridDs._total, "n0") );

            console.log(defaultGridDs);
            if($("#emSumPrice").length > 0) {
            	$("#emSumPrice").text(kendo.toString(defaultGridDs._pristineData[0].totalSalePrice, "n0"));
            }

            if(defaultGridDs._data[0] != undefined){
                $('#totalTaxablePrice').text( kendo.toString(defaultGridDs._data[0].totalTaxablePrice, "n0") );
            }else{
                $('#totalTaxablePrice').text( 0 );
            }

            let rowNum = defaultGridDs._total - ((defaultGridDs._page - 1) * defaultGridDs._pageSize);
            $("#defaultGrid tbody > tr .row-number").each(function(index){
                $(this).html(rowNum);
                rowNum--;
            });
        });
    },
    ckeckBoxAllClick: function(){
        $("#checkBoxAll").on("click", function(index){

            if( $("#checkBoxAll").prop("checked") ){

                $("input[name=rowCheckbox]").prop("checked", true);
            }else{

                $("input[name=rowCheckbox]").prop("checked", false);
            }
        });
    },
    checkBoxClick: function(){
        defaultGrid.element.on("click", "[name=rowCheckbox]" , function(e){
            if( e.target.checked ){
                if( $("[name=rowCheckbox]").length == $("[name=rowCheckbox]:checked").length ){
                    $("#checkBoxAll").prop("checked", true);
                }
            }else{
                $("#checkBoxAll").prop("checked", false);
            }
        });
    },
    click: function(){
        $($("#defaultGrid").data("kendoGrid").tbody).on("click", "[kind]", function(e) {

            e.preventDefault();

            var grid = $("#defaultGrid").data("kendoGrid");
            var row = $(this).closest("tr");
            var colIdx = $("td", row).index(this);
            var rowData = grid.dataItem(row);
            var param = {
            		ouId : rowData.ouId,
            		sessionId : rowData.sessionId,
            		settleMonth : rowData.settleMonth
            };
            var paramStr = "ouId=" + rowData.ouId + "&dataSearchStart=" + rowData.startDt.substring(0, 10) + "&dateSearchEnd=" + rowData.endDt.substring(0, 10) + "&confirmYn=" + rowData.confirmYn;

            switch (e.currentTarget.attributes.kind.value) {
                case "EXCEL_LIMIT_USAGE_AMOUNT" :
                    //alert("한도사용액 엑셀다운로드")
                    fnExcelDownload('/admin/calculate/employee/getEmployeeLimitUsePriceExcelList', param);
                    break;
                case "EXCEL_SETTLEMENT_SUPPORT_FUNDS" :
                    //alert("지원금정산 엑셀다운로드")
                    fnExcelDownload('/admin/calculate/employee/getEmployeeSupportPriceExcelList', param);
                    break;
                case "EMPLOYEE_USE_DETAIL" :
                    //alert("상세보기")
		            window.open("#/calEmployeeUse?" + paramStr,"_blank");
                    break;
            }
        });
    }
}