/**-----------------------------------------------------------------------------
 * system 	       : ERP 재고 엑셀 업로드
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.10		이성준          최초생성
 * @
 * **/
"use strict";
var aGridOpt, aGrid;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

    // sheetJs 스크립트 추가
    let myScript = document.createElement("script");
    myScript.setAttribute("src", "js/lib/sheetJs/xlsx.full.min.js");
    document.body.appendChild(myScript);

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : "true" });

		fnPageInfo({
			PG_ID  : 'itemStockUpload',
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){

		fnTranslate();	// 다국어 변환 --------------------------------------------
		fnInitButton();	// Initialize Button  ---------------------------------
		fnInitGrid(); // Initialize Grid ------------------------------------
		fnInitOptionBox(); // Initialize Option Box ------------------------------------

	};

	//--------------------------------- Button Start---------------------------------

	// 버튼 초기화
	function fnInitButton(){
		$("#fnClear, #fnErpExcelUpload, #fnSamepleFormDownload").kendoButton();
	};

	//--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------

	// 그리드 초기화
	function fnInitGrid(){

        aGridOpt = {
            columns   : [
            	          {field : '기준일자'	    , title : '기준일자'	    , width : '60px', attributes : {style : 'text-align:center'}}
            	        , {field : 'erp코드'	    , title : 'erp코드'	    , width : '60px', attributes : {style : 'text-align:center'}}
            	        , {field : '이샵코드'	    , title : '이샵코드'	    , width : '60px', attributes : {style : 'text-align:center'}}
            	        , {field : '제품명'	    , title : '제품명'	    , width : '60px', attributes : {style : 'text-align:center'}}
                        , {field : '재고유통기한'	, title : '재고유통기한'	, width : '60px', attributes : {style : 'text-align:center'}}
                        , {field : '재고수량'	    , title : '재고수량'	    , width : '40px', attributes : {style : 'text-align:center'}}
                        , {field : 'FILE_NM'	, title : '파일명'	    , width : '40px', attributes : {style : 'text-align:center'}, hidden:true}
            ]
        };

        aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
    };

	//-------------------------------  Grid End  -------------------------------

	//-------------------------------  Common Function start -------------------------------

	// 옵션 초기화
	function fnInitOptionBox() {
	    // 업로드 팝업
	    $("#uploadPopup").kendoWindow({
            width : 800,
            height : 500,
            title : "ERP 재고 업로드",
            visible : false,
            modal : true
        });

        // 업로드 등록 삭제
        $("#uploadDelete").on("click", function(e){
            e.preventDefault();
            $("#uploadViewControl").hide();
            $("#uploadLink").val("");
            $("#uploadFile").val("");
            $("#aGrid").data("kendoGrid").dataSource.data( [] );
        });

        // 업로드  링크 클릭
        $("#uploadLink").on("click", function(e){
            $("#uploadPopup").data("kendoWindow").center().open();
        });
	};

    // 업로드 버튼 클릭
    function fnErpExcelUpload(){
    	$("#uploadFile").trigger("click");
    };

	// 샘플다운로드 버튼 클릭
	function fnSamepleFormDownload(){

		var data = $('#searchForm').formSerialize(true);

		fnExcelDownload('/admin/goods/stock/getStockExprListExportExcel', data);
	};

	// 보내기
	function fnAddExcelSubmit(){
        let url  = "/admin/goods/stock/addExcelUpload";
        let inputFormValidator = $("#inputForm").kendoValidator().data("kendoValidator");

        let fileNm = $("#uploadLink").val();

        if(fileNm == ""){//파일유무 체크
        	fnKendoMessage({message : "엑셀 등록은 필수입니다."});
            return false;
        }

        if( fnDataValidation() == false ){
            return;
        }

        if( inputFormValidator.validate() ){
            fnKendoMessage({
                type    : "confirm",
                message : "업로드를 하시겠습니까?",
                ok      : function(e){
                              fnInputFormSubmit(url, "addExcelUpload");
                },
                cancel  : function(e){  }
            });
        }
	};

	// 데이터 검증
	function fnDataValidation() {
        if( $("#aGrid").data("kendoGrid").dataSource.data().length > 5000 ){
            fnKendoMessage({message : "업로드는 5000건 이상 업로드 하실 수 없습니다."});
            return false;
        }
       return true;
	};

    // 엑셀 업로드 submit
    function fnInputFormSubmit(url, cbId){
        let inputFormData = $("#inputForm").formSerialize(true);

        inputFormData['upload'] = kendo.stringify( fnGetDataList() );

        fnAjax({
            url     : url,
            params  : inputFormData,
            success : function( successData ){
            	fnBizCallback(cbId, successData);
            },
            isAction : 'insert'
        });
    };

    //날짜변환 함수(엑셀 날짜타입을 js에 맞게 변환)
    function excelSerialDateToJSDate (excelSerialDate) {
	  const daysBeforeUnixEpoch = 70 * 365 + 19;
	  const hour = 60 * 60 * 1000;
	  var date = new Date(Math.round((excelSerialDate - daysBeforeUnixEpoch) * 24 * hour) + 12 * hour);
	      date = getFormatDate(date);
	  return date;
    };

    //yyyyMMdd 포맷으로 반환
    function getFormatDate(date){
        var year = date.getFullYear();              //yyyy
        var month = (1 + date.getMonth());          //M
        month = month >= 10 ? month : '0' + month;  //month 두자리로 저장
        var day = date.getDate();                   //d
        day = day >= 10 ? day : '0' + day;          //day 두자리로 저장
        return  year + '' + month + '' + day;       //'-' 추가하여 yyyy-mm-dd 형태 생성 가능
    }

    // 엑셀 목록 가져오기
    function fnGetDataList(){
        let DataArray = [];
        let aGridData = $("#aGrid").data("kendoGrid").dataSource.data();

        let aGridCnt = aGridData.length;
        let FILE_NM = $("#uploadLink").val();

        if( aGridCnt > 0 ){
            for(let i = 0; i < aGridCnt; i++){
                let orgData = {};

                orgData.ilItemCd      = $.trim(aGridData[i].erp코드);
                orgData.expirationDt  = $.trim(aGridData[i].재고유통기한);
                orgData.stockQty      = $.trim(aGridData[i].재고수량);
                orgData.fileNm        = FILE_NM;

                DataArray[i] = orgData;
            }
        }

        return DataArray;
    };

    // 엑셀 업로드 (SheetJs)
    function excelExport(event) {

        // Excel Data => Javascript Object 로 변환
        var input = event.target;
        var reader = new FileReader();

        var fileName = event.target.files[0].name;

        reader.onload = function() {
            var fileData = reader.result;
            var wb = XLSX.read(fileData, {
                type : 'binary'
               ,cellText : false
               , cellDates : true
            });

			const wsname = wb.SheetNames[0]; // 첫번째 sheet 만 처리
			const ws = wb.Sheets[wsname];
			var excelData = XLSX.utils.sheet_to_json(ws, {raw:false,dateNF:'yyyy-mm-dd'});

			$("#aGrid").data("kendoGrid").dataSource.data(excelData);
			$("#uploadViewControl").show();
			$("#uploadLink").val(fileName);
        };

        reader.readAsBinaryString(input.files[0]);
    }

    //확인후 url이동
    function fnClose() {
      var option = new Object();

      option.url = "#/itemStockUploadList";// ERP 재고 엑셀 업로드 내역 화면 URL
      option.menuId = 339;// ERP 재고 엑셀 업로드 내역 화면 메뉴 ID

      fnGoPage(option);
    }

    /**
     * 콜백합수
     */
    function fnBizCallback(id, data) {
        switch(id){
            case 'addExcelUpload':

            	var result = data.split("|"); //총건수|성공건수|실패건수

            	fnKendoMessage({message : "총 "+result[0]+"건</br>"+"정상 "+result[1]+"건 / "+"실패 "+result[2]+"건", ok : fnClose});

                break;
        }
    };

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------

	$scope.fnClear =function(){ fnClear(); }; // 초기화
	$scope.fnErpExcelUpload =function(){ fnErpExcelUpload(); }; // 업로드 파일첨부 버튼
	$scope.fnSamepleFormDownload =function(){ fnSamepleFormDownload(); }; // 샘플다운로드 버튼
	$scope.fnAddExcelSubmit =function(){ fnAddExcelSubmit(); }; // 보내기 버튼
	$scope.fnExcelUpload = function(event) { excelExport(event);} // 엑셀 업로드 버튼

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
