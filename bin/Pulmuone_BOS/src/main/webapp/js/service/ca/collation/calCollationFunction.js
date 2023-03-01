/**-----------------------------------------------------------------------------
 * description 		 : 주문정산 관련 공통 함수
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.05		이명수   최초생성
 * **/

var calCollationGridEventUtil = {
    gridInit: function(gridUrl, grilColumns){
        var form = $("#searchForm");

        // form.formClear(false);
        var data = form.formSerialize(true);
        if(pageParam.odOutMallCompareUploadInfoId != undefined){
            data.odOutMallCompareUploadInfoId = pageParam.odOutMallCompareUploadInfoId;
        }
        if(pageParam.odPgCompareUploadInfoId != undefined){
            data.odPgCompareUploadInfoId = pageParam.odPgCompareUploadInfoId;
        }
        if($('input[name=selectConditionType]:checked').length > 0) {
            data['selectConditionType'] = $('input[name=selectConditionType]:checked').val();
        }
        defaultGridDs = fnGetPagingDataSource({
            url      : gridUrl,
            pageSize : PAGE_SIZE,
            filterLength: fnSearchData(data).length,
            filter: {
                filters: fnSearchData(data)
            },
            requestEnd : function(e){
                        $("#totalAmt").text(kendo.toString(e.response.data.totalAmt, "n0"));
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
//        $("#defaultGrid").on("click", "tbody > tr > td", function(e) {
            e.preventDefault();

            var grid = $("#defaultGrid").data("kendoGrid");
            var row = $(this).closest("tr");
            var colIdx = $("td", row).index(this);
            var rowData = grid.dataItem(row);

//             window.open("#/calOutmallDetl");

            switch (e.currentTarget.attributes.kind.value) {
                case "DETAIL" :

                    window.open("#/calOutmallDetl?odOutMallCompareUploadInfoId=" + rowData.odOutMallCompareUploadInfoId);

                    break;
                case "EXCEL_FAIL" :
                    let data = { "odOutMallCompareUploadInfoId": rowData.odOutMallCompareUploadInfoId};

                    fnExcelDownload('/admin/calculate/collation/getCalOutmallUploadFailList', data);
                    break;
            }
        });
        $("#defaultGrid").on("click", "tbody > tr > td", function(e) {
            e.preventDefault();
            var row = $(this).closest("tr");
            var colIdx = $("td", row).index(this);
            if(colIdx == 3){
                window.open("#/calOutmallDetl");
            }
        });
    },


    calPgClick: function(){
            $($("#defaultGrid").data("kendoGrid").tbody).on("click", "[kind]", function(e) {
                e.preventDefault();

                var grid = $("#defaultGrid").data("kendoGrid");
                var row = $(this).closest("tr");
                var colIdx = $("td", row).index(this);
                var rowData = grid.dataItem(row);

                switch (e.currentTarget.attributes.kind.value) {
                    case "DETAIL" :

                        window.open("#/calPgDetl?odPgCompareUploadInfoId=" + rowData.odPgCompareUploadInfoId);

                        break;
                    case "EXCEL_FAIL" :
                        let data = { "odPgCompareUploadInfoId": rowData.odPgCompareUploadInfoId};

                        fnExcelDownload('/admin/calculate/collation/getCalPgUploadFailList', data);
                        break;
                }
            });
//            $("#defaultGrid").on("click", "tbody > tr > td", function(e) {
//                e.preventDefault();
//                var row = $(this).closest("tr");
//                var colIdx = $("td", row).index(this);
//                if(colIdx == 3){
//                    window.open("#/calPgDetl");
//                }
//            });
        }

//    ,
//    outmallDetailClick : function(){
//    	$("#defaultGrid").on("click", "tbody > tr > td", function(e) {
//			let fieldId = e.currentTarget.dataset.field;	//컬럼 정보
//			let dataItems = orderGrid.dataItem(defaultGrid.select()); //선택데이터 정보
//			let serverUrlObj = fnGetServerUrl();
//
//			//등록일자
//			if(fieldId === "createDt") {
//
//				window.open("#/calPgDetl);
//			}
//		});
//    }
}


var calCollationUploadUtil = {
    // ==========================================================================
    // # 파일업로드-처리
    // ==========================================================================
    fnExcelUploadRun: function(uploadUrl){
        if(gFile == undefined || gFile == ""){
            fnKendoMessage({
                message : "파일을 선택해주세요.",
                ok : function(e) {
                }
            });
            return;
        }
        calCollationUploadUtil.fnExcelUpload(gFile, gFileTagId, uploadUrl);
    },
    // NOTE 파일 업로드 이벤트
    fnExcelUpload: function (file, fileTagId, uploadUrl) {

        var formData = new FormData();
        formData.append('bannerImage', file);
//        formData.append('authMenuID', "1170");

        $.ajax({
            url         : uploadUrl
            , data        : formData
            , type        : 'POST'
            , contentType : false
            , processData : false
            , async       : false
            , beforeSend : function(xhr) {
                xhr.setRequestHeader('authMenuID', CLICKED_MENU_ID);
            }
            , success     : function(data) {
                let localMessage = "";
                if(data.code == 'FILE_ERROR' || data.code == 'EXCEL_TRANSFORM_FAIL' || data.code == 'EXCEL_UPLOAD_NONE'){
                    localMessage = data.message;
                }else{
                    localMessage = "[결과] : " + data.message + "<BR>" +
                        " [총 건수] : " + data.data.totalCount + "<BR>" +
                        " [성공 건수] : " + data.data.successCount + "<BR>" +
                        " [실패 건수] : " + data.data.failCount;

                    if (data.code != "0000"){
                        localMessage += "<BR>" +" [실패 메세지] : " + data.data.failMessage;
                    }
                }

                fnKendoMessage({
                    message : localMessage,
                    ok : function(e) {}
                });
            }
        });
    },
    // # 파일업로드 End
    // ##########################################################################
    uploadInit: function(){
        var uploadFileTagIdList = ['excelFile'];

        var selectFunction = function(e) {
            if (e.files && e.files[0]) {
                // 엑셀 파일 선택시
                $("#fileInfoDiv").text(e.files[0].name);
                // --------------------------------------------------------------------
                // 확장자 2중 체크 위치
                // --------------------------------------------------------------------
                // var imageExtension = e.files[0]['extension'].toLowerCase();
                // 전역변수에 선언한 허용 확장자와 비교해서 처리
                // itemMgmController.js 의 allowedImageExtensionList 참조

                //  켄도 이미지 업로드 확장자 검사
                if(!calCollationUploadUtil.validateExtension(e)) {
                    fnKendoMessage({
                        message : '허용되지 않는 확장자 입니다.',
                        ok : function(e) {}
                    });
                    return;
                }

                gFileTagId = e.sender.element[0].id;
                let reader = new FileReader();

                reader.onload = function(ele) {
                    var itemImageScr = ele.target.result; // FileReader 로 생성한 상품 이미지 url
                    gFile = e.files[0].rawFile; // kendoUpload 로 가져온 상품 이미지 file 객체

//              fnExcelUpload(file, fileTagId);
                };

                reader.readAsDataURL(e.files[0].rawFile);

            } // End of if (e.files && e.files[0])
        } // End of var selectFunction = function(e)

        for (var i = 0; i < uploadFileTagIdList.length; i++) {
            fnKendoUpload({
                id : uploadFileTagIdList[i]
                , select : selectFunction
            });
        } // End of for (var i = 0; i < uploadFileTagIdList.length; i++)
    },
    validateExtension: function(e){

        var allowedExt = '';
        var ext = e.files[0].extension;
        var $el = e.sender.element;

        if( !$el.length ) return;

        if( $el[0].accept && $el[0].accept.length ) {
            // 공백 제거
            allowedExt = $el[0].accept.replace(/\s/g, '');
            allowedExt = allowedExt.split(',');
        } else {
            allowedExt = allowedImageExtensionList;
        }

        return allowedExt.includes(ext);
    },
    sampleDownload: function(downloadUrl){
        document.location.href = downloadUrl
    }

}
