/**-----------------------------------------------------------------------------
 * description 	: 배송정책 설정 > 도서산간/배송불가 권역 관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.09.24		남기승          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var publicStorageUrl = fnGetPublicStorageUrl();
var gAdminSearchValue = "";
var allowedExcelExtensionList = ['.xls','.xlsx'];
var gFileTagId;
var gFile;


$(document).ready(function() {

    fnInitialize();	// Initialize Page Call ---------------------------------

    // Initialize PageR
    function fnInitialize(){
        $scope.$emit('fnIsMenu', { flag : 'true' });

        fnPageInfo({
            PG_ID  : 'policyShippingArea',
            callback : fnUI
        });
    }

    function fnUI(){
        fnInitButton();	// Initialize Button ---------------------------------
        fnInitGrid();	// Initialize Grid ------------------------------------
        fnInitOptionBox();// Initialize Option Box
        fnDefaultSet();
        fnInitKendoUpload();
        fnSearch();
    }

    // --------------------------------- Button Start---------------------------------
    function fnInitButton(){
        $('#fnSearch, #fnClear, #fnDeliverySearch, #fnExcelUpload, #fnSampleDownload').kendoButton();
    }

    // 엑셀 적용내역 조회
    function fnSearch(){
        var data = $('#searchForm').formSerialize(true);

        var query = {
            page         : 1,
            pageSize     : PAGE_SIZE,
            filterLength : fnSearchData(data).length,
            filter :  {
                filters : fnSearchData(data)
            }
        };
        aGridDs.query( query );
    }

    // 초기화
    function fnClear(){
        $('#searchForm').formClear(true);
        fnDefaultSet();
    }

    // 옵션 초기화
    function fnInitOptionBox(){
        // 엑셀등록 권역 선택
        fnKendoDropDownList({
            id  : 'selectShippingArea',
            tagId : 'selectShippingArea',
            url   : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "UNDELIVERABLE_TP", "useYn" :"Y"},
            textField :"NAME",
            valueField : "CODE",
            value : "",
            blank : "권역선택"
        });

        // 엑셀적용내역 조회 권역 선택
        fnKendoDropDownList({
            id  : 'undeliverableTp',
            tagId : 'undeliverableTp',
            url   : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "UNDELIVERABLE_TP", "useYn" :"Y"},
            textField :"NAME",
            valueField : "CODE",
            value : "",
            blank : "전체"
        });
    };

    // 기본 설정
    function fnDefaultSet(){
        //$("#createStartDate").data("kendoDatePicker").value(fnGetToday());
        //$("#createEndDate").data("kendoDatePicker").value(fnGetToday());
        gAdminSearchValue = "";
    };


    // ##########################################################################
    // # 파일업로드 Start
    // ##########################################################################
    // ==========================================================================
    // # 파일업로드-업로드시 사용할 kendoUpload 컴포넌트 초기화
    // ==========================================================================
    function fnInitKendoUpload() {
        var uploadFileTagIdList = ['excelFile'];

        var selectFunction = function(e) {
            if (e.files && e.files[0]) {
                // 엑셀 파일 선택시
                $("#fileInfoDiv").empty();

                $("#fileInfoDiv").text(e.files[0].name);        // 열기 버튼 옆에 선택한 엑셀파일명 노출

                //  켄도 이미지 업로드 확장자 검사
                if(!validateExtension(e)) {
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

                //    fnExcelUpload(gFile, gFileTagId);
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
    }

    // ==========================================================================
    // # 파일업로드-validateExtension
    // ==========================================================================
    function validateExtension(e) {

        var allowedExt = '';
        var ext = e.files[0].extension;
        var $el = e.sender.element;

        if( !$el.length ) return;

        if( $el[0].accept && $el[0].accept.length ) {
            // 공백 제거
            allowedExt = $el[0].accept.replace(/\s/g, '');
            allowedExt = allowedExt.split(',');
        } else {
            allowedExt = allowedExcelExtensionList;
        }

        return allowedExt.includes(ext);
    };

    // ==========================================================================
    // # 파일업로드-처리
    // ==========================================================================
    function fnExcelUploadRun(){

        // validation check
        if(fnValidationCheck() == false) return;

        fnExcelUpload(gFile, gFileTagId);
    }

    // NOTE 파일 업로드 이벤트
    function fnExcelUpload(file, fileTagId) {

        var formData = new FormData();
        formData.append('bannerImage', file);
        formData.append('storageType', 'public'); // storageType 지정
        formData.append('domain', 'ps');          // domain 지정 - 전시

        var originalFileName  = '';
        var fullPath          = '';

        $.ajax({
            url         : '/comn/fileUpload'
            , data        : formData
            , type        : 'POST'
            , contentType : false
            , processData : false
            , async       : false
            , success     : function(data) {
                data = data.data;

                // ----------------------------------------------------
                // 업로드파일 정보 Set
                // ----------------------------------------------------

                originalFileName = data['addFile'][0].originalFileName;
                fullPath         = data['addFile'][0].serverSubPath + data['addFile'][0].physicalFileName;

                $("#fileInfoDiv").text(originalFileName);        // 열기 버튼 옆에 선택한 엑셀파일명 노출

                fnSuccessExcelUpload(file, fullPath);
            }
        });
    }

    // Excel 업로드시 실행
    function fnSuccessExcelUpload(file, fullPath){
        var formData = new FormData();
        formData.append('keyword', $("#insertKeyword").val());
        formData.append('undeliverableTp', $("#selectShippingArea").val());
        formData.append('bannerImage', file);                   // Excel 파일 체크위한 전달.
        formData.append('storageFileNm', fullPath);                  // NAS 저장 경로.

        $.ajax({
            url         : '/admin/policy/addShippingAreaExcelUpload'
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
                var opt = {
                    successCnt : data.data.successCount,
                    failCnt : data.data.failCount,
                    deleteCnt : data.data.deleteCount
                }

                if(data.code == 'FILE_ERROR'
                    || data.code == 'EXCEL_TRANSFORM_FAIL'
                    || data.code == 'EXCEL_UPLOAD_NONE'
                    || data.code == 'DUPLICATE_KEYWORD'){
                    localMessage = data.message;
                }else{
                    localMessage = "[결과] : " + data.message + "<BR>" +
                        " [총 건수] : " + data.data.totalCount + "<BR>" +
                        " [성공 건수] : " + data.data.successCount + "<BR>" +
                        " [실패 건수] : " + data.data.failCount + "<BR>" +
                        " [삭제 건수] : " + data.data.deleteCount;

                    if (data.code != "0000"){
                        //localMessage += "<BR>" +" [실패 메세지] : " + data.data.failMessage;
                    }
                }

                fnSearch();
                gFile = "";
                $("#fileInfoDiv").empty();
                $("#insertKeyword").val("");

                $("#fileInfoDiv").html("<span style='color:red'>※ 배송불가 업로드 시 신규우편번호 5자리만 등록 가능합니다.</span>");

                fnKendoMessage({
                    message : localMessage,
                    ok : function(e) {}
                });
                // 업로드 내역 보여주기 위한 작업
                getShippingAreaInfo(data.data.psShippingAreaExcelInfoId, opt);
            }
        });
    }

    // # 파일업로드 End
    // ##########################################################################

    // 샘플 엑셀 다운로드
    function fnSampleDownload(){
        document.location.href = "/contents/excelsample/policy/shippingAreaExcelUploadSample.xlsx"
    }

    // 업로드 이후 상세내역
    function getShippingAreaInfo(psShippingAreaExcelInfoId, opt) {

        var formData = new FormData();
        formData.append('psShippingAreaExcelInfoId', psShippingAreaExcelInfoId);

        $.ajax({
            url           : '/admin/policy/getUploadShippingAreaInfo'
            , data        : formData
            , type        : 'POST'
            , contentType : false
            , processData : false
            , async       : false
            , beforeSend : function(xhr) {
                xhr.setRequestHeader('authMenuID', CLICKED_MENU_ID);
            }
            , success :
                function( data ){
                    let infoData = data.data;
                    let html = "";
                    html += "<tr>";
                    html += "<th scope='row' style='text-align: center;'>권역</th>";
                    html += "<th scope='row' style='text-align: center;'>업로드</th>";
                    html += "<th scope='row' style='text-align: center;'>등록상태</th>";
                    html += "<th scope='row' style='text-align: center;'>업로드파일</th>";
                    html += "<th scope='row' style='text-align: center;'>관리</th>";
                    html += "</tr>";
                    html += "<tr>";
                    html += "<td style='text-align: center;'>" + infoData.undeliverableNm + "</td>";
                    var totalCnt = parseInt(opt.successCnt) + parseInt(opt.deleteCnt);
                    html += "<td style='text-align: center;'>성공 : " + totalCnt + " 건 / 실패 : "+ parseInt(opt.failCnt)  + " 건</td>";
                    if(totalCnt > 0) {
                        html += "<td style='text-align: center;'>" + infoData.uploadStatusCdNm + "</td>";
                    } else {
                        html += "<td style='text-align: center;'>등록실패</td>";
                    }
                    html += "<td style='text-align: center;'><button type='button' class='k-button k-button-icontext btn-s k-grid-업로드파일다운로드' kind='btnUploadExcelDown'>다운로드</button></td>";
                    if(infoData.failCnt > 0) {
                        html += "<td style='text-align: center;'><button type='button' class='k-button k-button-icontext btn-s k-grid-업로드실패내역다운로드' kind='btnUploadFailDown'>업데이트 실패내역</button></td>";
                    } else {
                        html += "<td style='text-align: center;'>-</td>";
                    }


                    html += "</tr>";
                    $("#uploadContent").html(html);

                    btnEventBind(psShippingAreaExcelInfoId, infoData.fileNm, infoData.storageFileNm);


                }

        });
    }

    function btnEventBind(psShippingAreaExcelInfoId, fileNm, storageFileNm){
        // 업로드 엑셀 파일명 클릭시 다운로드 처리 이벤트.
        $("button[kind=btnUploadExcelDown]").on("click", function() {
            var filePath = storageFileNm.substring(0, storageFileNm.lastIndexOf("/"));
            var fileName = storageFileNm.substring(storageFileNm.lastIndexOf("/")+1);
            var opt = {
                filePath : filePath,
                physicalFileName: fileName,
                originalFileName: fileNm
            }

            fnDownloadPublicFile(opt);
        });

        // 업로드 실패내역
        $("button[kind=btnUploadFailDown]").on("click", function() {
            let data = { "psShippingAreaExcelInfoId" : psShippingAreaExcelInfoId };
            fnExcelDownload('/admin/policy/getShippingAreaFailExcelDownload', data);
        });

    }

    function fnValidationCheck(){
        var selectShippingArea = $("#selectShippingArea").val();
        if(selectShippingArea == undefined || selectShippingArea == ''){
            fnKendoMessage({
                message : "권역을 선택해 주세요.",
                ok : function(e) {
                }
            });
            return false;
        }

        if(gFile == undefined || gFile == ""){
            fnKendoMessage({
                message : "엑셀파일을 등록해주세요.",
                ok : function(e) {
                }
            });
            return false;
        }

        if($("#insertKeyword").val() == null || $("#insertKeyword").val() == ""){
            fnKendoMessage({
                message : "키워드를 등록해주세요.",
                ok : function(e) {
                }
            });
            return false;
        }
    }

    // --------------------------------- Button End---------------------------------

    function fnDeliverySearch() {
        fnKendoPopup({
            id     : 'policyShippingAreaPopup',
            title  : '도서산간/배송불가 권역관리 조회',
            src    : '#/policyShippingAreaPopup',
            param  : '',
            width  : '1000px',
            height : '700px',
            success: function( id, data ){
                fnSearch();
            }
        });
    }



    // ------------------------------- Grid Start -------------------------------
    function fnInitGrid(){
        aGridDs = fnGetPagingDataSource({
            url      : "/admin/policy/getShippingAreaExcelInfoList",
            pageSize : PAGE_SIZE
        });
        aGridOpt = {
            dataSource: aGridDs
            ,  pageable  : {
                pageSizes: [20, 30, 50],
                buttonCount : 10
            }
            ,navigatable: true
            ,columns   : [
                { field : "no", title : "No", width : "50px", attributes : { style:'text-align:center' }, template: "<span class='row-number'></span>" }
                , { field : "psShippingAreaExcelInfoId", hidden : true }
                , { field : "storageFileNm", hidden : true }
                , { field : "undeliverableNm", title: "권역", width : "100px", attributes : {style : "text-align:center"}}
                , { field : "totalCnt", title : "적용 건수", width : "150px", attributes : {style : "text-align:center" },
                    template : function(dataItem){
                        return "총 " + dataItem.successCnt + " 건 ( 대체배송 " + dataItem.alternateCnt + " 건 )";
                    }
                }
                , { field : "keyword", title : "키워드 등록", width : "200px", attributes : { style : "text-align:center" }}
                , { field : "createNm", title : "등록자", width : "100px", attributes : { style : "text-align:center" }}
                , { field : "createDt", title : "등록일자", width : "100px", attributes : { style : "text-align:center" }}
                , { field : "uploadStatus", title : "업로드", width : "150px", attributes: { style : "text-align:center"},
                    template : function(dataItem){
                        return "성공 : " + dataItem.successCnt + " / 실패 : " + dataItem.failCnt + " 건";
                    }
                }
                , { field : "management", title : "관리", width : "200px", attributes : { style : "text-align:center" },
                    template : function(dataItem){
                        let rtnValue = "";
                        // 우선순위 : 일괄삭제 / 성공내역 다운로드 / 실패내역 다운로드 / 원본 다운로드
                        // 다운로드 권한이 있고 도서산간(1권역)/제주(2권역)외 일괄삭제 버튼 노출
                        if (fnIsProgramAuth("EXCELDOWN")) {
                            // 도서산간(1구역), 제주(2구역) 이 아니거나, 성공 건수가 0건인 경우 삭제버튼 노출
                            if((dataItem.undeliverableTp != "UNDELIVERABLE_TP.ISLAND" && dataItem.undeliverableTp != "UNDELIVERABLE_TP.JEJU") || dataItem.successCnt == 0){
                                rtnValue += '&nbsp;&nbsp;<button type="button" class="k-button k-button-icontext btn-red btn-s k-grid-일괄삭제" kind="btnAllDelete">일괄삭제</button>';
                            }
                            rtnValue += '&nbsp;&nbsp;<button type="button" class="k-button k-button-icontext btn-blue btn-s k-grid-업로드내역다운로드" kind="btnSuccessExcelDown">성공내역 다운로드</button>';
                            // 실패내역이 있는경우에만 버튼 노출
                            if(dataItem.failCnt > 0){
                                rtnValue += '&nbsp;&nbsp;<button type="button" class="k-button k-button-icontext btn-blue btn-s k-grid-취소내역다운로드" kind="btnFailExcelDown">실패내역 다운로드</button>';
                            }
                            rtnValue += '&nbsp;&nbsp;<button type="button" class="k-button k-button-icontext btn-blue btn-s k-grid-원본다운로드" kind="btnOriginalExcelDown">원본 다운로드</button>';
                        }
                        return rtnValue;
                    }
                }
            ]
        };

        aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

        aGrid.bind("dataBound", function() {
            var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
            $("#aGrid tbody > tr .row-number").each(function(index){
                $(this).html(row_num);
                row_num--;
            });

            $('#totalCnt').text(aGridDs._total);
        });

        // 성공내역 다운로드 버튼 이벤트.
        $('#aGrid').on("click", "button[kind=btnSuccessExcelDown]", function(e) {
            e.preventDefault();
            let dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));
            let data = { "psShippingAreaExcelInfoId" : dataItem.psShippingAreaExcelInfoId, "failType" : $(this).data('failtype')};
            fnExcelDownload('/admin/policy/getShippingAreaInfoExcelDownload', data);
        });

        // 실패내역 다운로드 버튼 이벤트.
        $('#aGrid').on("click", "button[kind=btnFailExcelDown]", function(e) {
            e.preventDefault();
            let dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));
            let data = { "psShippingAreaExcelInfoId" : dataItem.psShippingAreaExcelInfoId };
            fnExcelDownload('/admin/policy/getShippingAreaFailExcelDownload', data);
        });

        // 원본 다운로드 버튼 이벤트.
        $('#aGrid').on("click", "button[kind=btnOriginalExcelDown]", function(e) {
            e.preventDefault();
            let dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));
            var filePath = dataItem.storageFileNm.substring(0, dataItem.storageFileNm.lastIndexOf("/"));
            var fileName = dataItem.storageFileNm.substring(dataItem.storageFileNm.lastIndexOf("/")+1);
            var opt = {
                filePath : filePath,
                physicalFileName: fileName,
                originalFileName: dataItem.fileNm
            }

            fnDownloadPublicFile(opt);
        });

        // 일괄 삭제버튼 이벤트.
        $('#aGrid').on("click", "button[kind=btnAllDelete]", function(e) {
            e.preventDefault();
            let dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));
            var filePath = dataItem.storageFileNm.substring(0, dataItem.storageFileNm.lastIndexOf("/"));
            var fileName = dataItem.storageFileNm.substring(dataItem.storageFileNm.lastIndexOf("/")+1);

            fnKendoMessage({message : '삭제하시겠습니까?', type : "confirm", ok : function(){
                    fnAjax({
                        url     : '/admin/policy/delShippingAreaInfo',
                        contentType : "application/json",
                        params  : {psShippingAreaExcelInfoId : dataItem.psShippingAreaExcelInfoId},
                        success :
                            function( data ){
                                fnKendoMessage({message : '삭제되었습니다.'});
                                aGridDs.query();
                            },
                        isAction : 'delete'
                    });
                }
            });

            var opt = {
                filePath: filePath, // 다운로드할 파일의 하위 경로
                physicalFileName: fileName, // 업로드시 저장된 물리적 파일명
            }
            // 원본파일 삭제
            fnDeletePublicFile(opt);
        });
    }
    // ------------------------------- Grid End -------------------------------

    // ------------------------------- Html 버튼 바인딩 Start
    // -------------------------------


    /** Common Search */
    $scope.fnSearch = function(){	fnSearch();	};
    /** Common Clear */
    $scope.fnClear = function(){	 fnClear();	};
    /** Button fnEmployeeSearchPopup */
    // $scope.fnEmployeeSearchPopup = function(){	 fnEmployeeSearchPopup();};

    /** Button excelSelect */
    $scope.fnBtnExcelSelect = function(fileType) {$('#' + fileType).trigger('click');};
    /** Button excelUpload */
    $scope.fnExcelUploadRun = function(){ fnExcelUploadRun();};

    $scope.fnDeliverySearch = function(){ fnDeliverySearch();};


    $scope.fnSampleDownload = function() { fnSampleDownload();};
    // ------------------------------- Html 버튼 바인딩 End
    // -------------------------------

}); // document ready - END
