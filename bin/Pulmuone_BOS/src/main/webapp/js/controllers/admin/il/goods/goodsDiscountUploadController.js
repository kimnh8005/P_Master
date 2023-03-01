/**-----------------------------------------------------------------------------
 * description 		 : 상품 할인 일괄 업로드
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.18     정형진			퍼블수정 및 기능개발
 * @
 * **/

'use strict';
var pageParam = fnGetPageParam(); // GET 방식으로 전달된 parameter 객체
var aGridOpt, aGrid;
var apprAdminGridDs, apprAdminGridOpt, apprAdminGrid;

$(document).ready(function() {

    // Initialize Page Call
	fnInitialize();

    // sheetJs 스크립트 추가
    let myScript = document.createElement("script");
    myScript.setAttribute("src", "js/lib/sheetJs/xlsx.full.min.js");
    document.body.appendChild(myScript);

	// Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', {
			flag : 'true'
		});

		fnPageInfo({
			PG_ID : 'goodsDiscountUpload',
			callback : fnUI
		});
	};

	function fnUI() {
		fnTranslate();		// 다국어 변환 -----------------------------------------
		fnInitButton();		// Initialize Button  ---------------------------------
		fnInitGrid();		// Initialize Grid ------------------------------------
		fnApprAdminInit();	// 승인관리자 정보 Grid----------------------------------
		fnInitOptionBox();	// Initialize Option Box ------------------------------

	}

	// 버튼 초기화
	function fnInitButton(){
		$("#fnClear, #fnDisExcelUpload, #fnSamepleFormDownload").kendoButton();
	};

	// 그리드 초기화
	function fnInitGrid(){

        aGridOpt = {
            columns   : [
                          {field : '상품코드'	    , title : '상품코드'	, width : '60px', attributes : {style : 'text-align:center'}}
                        , {field : '할인시작일'	, title : '할인시작일'	, width : '60px', attributes : {style : 'text-align:center'}}
                        , {field : '할인종료일'	    , title : '할인종료일'	    , width : '40px', attributes : {style : 'text-align:center'}}
                        , {field : '할인구분'	    , title : '할인구분'	    , width : '40px', attributes : {style : 'text-align:center'}}
                        , {field : '할인유형'	    , title : '할인유형'	    , width : '40px', attributes : {style : 'text-align:center'}}
                        , {field : '할인율'	    , title : '할인율'	    , width : '40px', attributes : {style : 'text-align:center'}}
                        , {field : 'FILE_NM'	    , title : '파일명'	, width : '40px', attributes : {style : 'text-align:center'}, hidden:true}
            ]
        };

        aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
    };

    //승인 관리자 Grid 초기화
    function fnApprAdminInit(){
    	apprAdminGridDs =  new kendo.data.DataSource();

		apprAdminGridOpt = {
			dataSource : apprAdminGridDs,
			editable : false,
			noRecordMsg: '승인관리자를 선택해 주세요.',
			columns : [{
				field : 'apprAdminInfo',
				title : '승인관리자정보',
				width : '100px',
				attributes : {style : 'text-align:center'}
				},{
				field : 'adminTypeName',
				title : '계정유형',
				width : '100px',
				attributes : {style : 'text-align:center'}
				},{
					field : 'apprUserName',
					title : '관리자이름/아이디',
					width : '100px',
					attributes : {style : 'text-align:center'},
					template : function(dataItem){
						let returnValue;
						returnValue = dataItem.apprUserName + '/' + dataItem.apprLoginId;
						return returnValue;
					}
				},{
					field : 'organizationName',
					title : '조직/거래처 정보',
					width : '100px',
					attributes : {style : 'text-align:center'}
				},{
					field : 'teamLeaderYn',
					title : '조직장여부',
					width : '80px',
					attributes : {style : 'text-align:center'}
				},{
					field : 'userStatusName',
					title : 'BOS 계정상태',
					width : '80px',
					attributes : {style : 'text-align:center'}
				},{
					field : 'grantUserName',
					title : '권한위임정보<BR/>(이름/ID)',
					width : '100px',
					attributes : {style : 'text-align:center'},
					template : function(dataItem){
						let returnValue;
						if(dataItem.grantAuthYn == 'Y'){
							returnValue = dataItem.grantUserName + '/' + dataItem.grantLoginId;
						}else{
							returnValue = '';
						}
						return returnValue;
					}
				},{
					field : 'userStatusName',
					title : '권한위임기간',
					width : '150px',
					attributes : {style : 'text-align:left'},
					template : function(dataItem){
						let returnValue;
						if(dataItem.grantAuthYn == 'Y'){
							returnValue = dataItem.grantAuthStartDt + '~' + dataItem.grantAuthEndDt;
						}else{
							returnValue = '';
						}
						return returnValue;
					}
				},{
					field : 'grantUserStatusName',
					title : '권한위임자<BR/>BOS 계정상태',
					width : '100px',
					attributes : {style : 'text-align:left'},
					template : function(dataItem){
						let returnValue;
						if(dataItem.grantAuthYn == 'Y'){
							returnValue = dataItem.grantUserStatusName;
						}else{
							returnValue = '';
						}
						return returnValue;
					}
				},{
					field:'addCoverageId', hidden:true
				},{
					field:'includeYn', hidden:true
				}
			]
		};

		apprAdminGrid = $('#apprGrid').initializeKendoGrid(apprAdminGridOpt).cKendoGrid();
	}

	// 업로드 버튼 클릭
    function fnDisExcelUpload(){

    	fileClear();

        $("#uploadFile").trigger("click");
    };

    function fnInitOptionBox() {
    	// 업로드 팝업
	    $("#uploadPopup").kendoWindow({
            width : 800,
            height : 500,
            title : "상품 할인 일괄 업로드",
            visible : false,
            modal : true
        });


        // 업로드 등록 삭제
        $("#uploadDelete").on("click", function(e){
            e.preventDefault();

            fileClear();

            $("#uploadViewControl").hide();
            $("#uploadLink").val("");
            $("#aGrid").data("kendoGrid").dataSource.data( [] );
        });

	    // 업로드  링크 클릭
        $("#uploadLink").on("click", function(e){
            $("#uploadPopup").data("kendoWindow").center().open();
        });


    }

    function fileClear() {
    	var agent = navigator.userAgent.toLowerCase();
        if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ){
            // ie 일때 input[type=file] init.
            $("#uploadFile").replaceWith( $("#uploadFile").clone(true) );
        } else {
            //other browser 일때 input[type=file] init.
            $("#uploadFile").val("");
        }


    }
    // 보내기
	function fnAddExcelSubmit(){
        let url  = "/admin/goods/discount/addExcelUpload";
        let inputFormValidator = $("#excelUploadForm").kendoValidator().data("kendoValidator");

        if($("#uploadLink").val() == "") {
        	fnKendoMessage({
                message : '파일을 업로드해주세요.',
                ok : function focusValue() {
                	return;
                }
            });
        }
        else if (apprAdminGridDs.data().length == 0) {
			fnKendoMessage({ message : "승인관리자를 지정해 주세요." });
			 return;
		}
        else if( inputFormValidator.validate() ){
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

    // 샘플다운로드 버튼 클릭
	function fnSamepleFormDownload(){

		document.location.href = "/contents/excelsample/goodsdiscount/sampleGoodsPriceDiscount.xlsx"
//	    var opt = {filePath: "/contents/images/",
//        		physicalFileName: "sampleGoodsPriceDiscount.xlsx",
//        		originalFileName: "sampleGoodsPriceDiscount.xlsx"
//        }
//        fnDownloadPublicFile(opt);
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
			var excelData = XLSX.utils.sheet_to_json(ws, {raw:false,dateNF:'yyyy-mm-dd hh:mm:ss'});
			excelData.splice(0,1);
			$("#aGrid").data("kendoGrid").dataSource.data(excelData);
			$("#uploadViewControl").show();
			$("#uploadLink").val(fileName);
        };

//        reader.onload = function() {
//            var fileData = reader.result;
//            var wb = XLSX.read(fileData, {
//                type : 'binary'
//            });
//
//            wb.SheetNames.forEach(function(sheetName) {
//
//                var excelData = XLSX.utils.sheet_to_json(wb.Sheets[sheetName]);
//
//                excelData.splice(0,1);
//
//                $("#aGrid").data("kendoGrid").dataSource.data(excelData);
//                $("#uploadDelete").show();
//                $("#uploadLink").text(fileName);
//
//            })
//        };

        reader.readAsBinaryString(input.files[0]);
    };

    // 엑셀 업로드 submit
    function fnInputFormSubmit(url, cbId){

    	let uploadList = fnGetDataList();
        let inputFormData = {
        	uploadList : uploadList
        ,	goodsDiscountApproList : $("#apprGrid").data("kendoGrid").dataSource.data()
        };

        fnAjax({
            url     : url,
            params  : inputFormData,
            contentType : 'application/json',
            success : function( successData ){
            	fnBizCallback(cbId, successData);
            },
            isAction : 'insert'
        })
    };

    // 엑셀 목록 가져오기
    function fnGetDataList(){
        let DataArray = [];
        let aGridData = $("#aGrid").data("kendoGrid").dataSource.data();
        let aGridCnt = aGridData.length;
        let FILE_NM = $("#uploadLink").text();

        if( aGridCnt > 0 ){
            for(let i = 0; i < aGridCnt; i++){
                let orgData = {};

                orgData.goodsId      			= aGridData[i].상품코드.trim();
                orgData.discountStartDate  		= aGridData[i].할인시작일.trim();
                orgData.discountEndDate    		= aGridData[i].할인종료일.trim();
                orgData.discountTypeCode      	= aGridData[i].할인구분.trim();
                orgData.discountMethodTypeCode  = aGridData[i].할인유형.trim();
                orgData.discountVal      		= aGridData[i].할인율.trim();
                orgData.fileNm        			= FILE_NM;

                DataArray[i] = orgData;
            }
        }

        return DataArray;
    };

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

    //확인후 url이동
    function fnClose() {
      var option = new Object();

      option.url = "#/goodsDiscountUploadList";// ERP 재고 엑셀 업로드 내역 화면 URL
      option.menuId = 1018;// ERP 재고 엑셀 업로드 내역 화면 메뉴 ID

      fnGoPage(option);
    }

	//승인관리자 지정 팝업 호출
	function fnApprAdmin() {
		var param = {'taskCode' : 'APPR_KIND_TP.GOODS_DISCOUNT'};
		fnKendoPopup({
			id		: 'approvalManagerSearchPopup',
			title	: '승인관리자 선택',
			src		: '#/approvalManagerSearchPopup',
			param	: param,
			width	: '1300px',
			heigh	: '800px',
			scrollable : "yes",
			success: function( stMenuId, data ){

				if(data && !fnIsEmpty(data) && data.authManager2nd){
					$('#apprGrid').gridClear(true);

					var authManager1 = data.authManager1st;
					var authManager2 = data.authManager2nd;

					if(authManager1.apprUserId != undefined){							//1차 승인관리자가 미지정이라면
						var objManager1 = new Object();

						objManager1["apprAdminInfo"] = "1차 승인관리자";
						objManager1["adminTypeName"] = authManager1.adminTypeName;
						objManager1["apprUserName"] = authManager1.apprUserName;
						objManager1["apprKindTp"] = authManager1.apprKindType;
						objManager1["apprManagerTp"] = authManager1.apprManagerType
						objManager1["apprUserId"] = authManager1.apprUserId;
						objManager1["apprLoginId"] = authManager1.apprLoginId;
						objManager1["organizationName"] = authManager1.organizationName;
						objManager1["userStatusName"] = authManager1.userStatusName;
						objManager1["teamLeaderYn"] = authManager1.teamLeaderYn;
						objManager1["grantAuthYn"] = authManager1.grantAuthYn;
						objManager1["grantUserName"] = authManager1.grantUserName;
						objManager1["grantLoginId"] = authManager1.grantLoginId;
						objManager1["grantAuthStartDt"] = authManager1.grantAuthStartDt;
						objManager1["grantAuthEndDt"] = authManager1.grantAuthEndDt;
						objManager1["grantUserStatusName"] = authManager1.grantUserStatusName;
						apprAdminGridDs.add(objManager1);
					}

					var objManager2 = new Object();

					objManager2["apprAdminInfo"] = "2차 승인관리자";
					objManager2["adminTypeName"] = authManager2.adminTypeName;
					objManager2["apprUserName"] = authManager2.apprUserName;
					objManager2["apprKindTp"] = authManager2.apprKindType;
					objManager2["apprManagerTp"] = authManager2.apprManagerType
					objManager2["apprUserId"] = authManager2.apprUserId;
					objManager2["apprLoginId"] = authManager2.apprLoginId;
					objManager2["organizationName"] = authManager2.organizationName;
					objManager2["userStatusName"] = authManager2.userStatusName;
					objManager2["teamLeaderYn"] = authManager2.teamLeaderYn;
					objManager2["grantAuthYn"] = authManager2.grantAuthYn;
					objManager2["grantUserName"] = authManager2.grantUserName;
					objManager2["grantLoginId"] = authManager2.grantLoginId;
					objManager2["grantAuthStartDt"] = authManager2.grantAuthStartDt;
					objManager2["grantAuthEndDt"] = authManager2.grantAuthEndDt;
					objManager2["grantUserStatusName"] = authManager2.grantUserStatusName;
					apprAdminGridDs.add(objManager2);
				}
			}
		});
	}

	function fnApprClear() {
		let goodsDiscountApprList = $("#apprGrid").data("kendoGrid");
		if(goodsDiscountApprList != undefined){
			$('#apprGrid').gridClear(true);
			$('#apprSubUserId').val('');
			$('#apprUserId').val('');
		}
	}

	//-------------------------------  Common Function end -------------------------------

	$scope.fnClear =function(){ fnClear(); }; // 초기화
	$scope.fnDisExcelUpload =function(){ fnDisExcelUpload(); }; // 업로드 파일첨부 버튼
	$scope.fnSamepleFormDownload =function(){ fnSamepleFormDownload(); }; // 샘플다운로드 버튼
	$scope.fnAddExcelSubmit =function(){ fnAddExcelSubmit(); }; // 보내기 버튼
	$scope.fnExcelUpload = function(event) { excelExport(event);} // 엑셀 업로드 버튼
	$scope.fnApprAdmin = function(){ fnApprAdmin(); } // 승인관리자 지정 팝업 버튼
	$scope.fnApprClear = function(){ fnApprClear(); } // 승인관리자 초기화 팝업 버튼

});

