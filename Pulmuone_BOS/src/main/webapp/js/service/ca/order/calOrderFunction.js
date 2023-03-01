/**-----------------------------------------------------------------------------
 * description 		 : 주문정산 관련 공통 함수
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.05		이명수   최초생성
 * **/

var calOrderGridEventUtil = {
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
            height : 755,
            scrollable : true,
            columns : grilColumns
        };

        defaultGrid = $('#defaultGrid').initializeKendoGrid( defaultGridOpt ).cKendoGrid();

        defaultGrid.bind("dataBound", function(e) {
            $('#countTotalSpan').text( kendo.toString(defaultGridDs._total, "n0") );

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

            switch (e.currentTarget.attributes.kind.value) {
                case "1" :
                    alert("한도사용액 엑셀다운로드")
                    break;
                case "2" :
                    alert("지원금정산 엑셀다운로드")
                    break;
                case "3" :
                    alert("상세보기")
                    break;
            }
        });
    }
}