/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 주문관리 > 주문상세레스트
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.26		강윤경          최초생성
 * @ 2020.11.30		김승우					수정
 * **/
"use strict";

var PAGE_SIZE = 50;
var viewModel, orderGridDs, orderGridOpt, orderGrid;
var pageParam = fnGetPageParam();
var gFilePath;
var gExcelMakeChecker;

// 판매처그룹.직관리 (외부몰)
var sellersGroupDirectMng = "SELLERS_GROUP.DIRECT_MNG";
var sellersGroupVendor		= "SELLERS_GROUP.VENDOR";
// cs 관리 조회처리
var csParamData;
if(defaultActivateTab == undefined){
	var defaultActivateTab;
}else{
	var csData;
	if(csData != undefined){
		$('#urUserId').val(csData.urUserId);
		csParamData = {"urUserId" : csData.urUserId};
	}
}

if(newURL == 'orderDetailList'){
	var defaultActivateTab = '';
	csParamData = undefined;
	$('#urUserId').val('');
}

$(document).ready(function() {

	importScript("/js/service/od/order/orderCommDetailGridColumns.js", function (){
		importScript("/js/service/od/order/orderCommSearch.js", function (){
			importScript("/js/service/od/order/orderMgmFunction.js", function (){
				fnInitialize();	//Initialize Page Call ---------------------------------
			});
		});
	});

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : true });
		fnPageInfo({
			PG_ID  : "orderDetailList",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){

		fnTranslate();	// 다국어 변환
		fnInitButton();

		fnInitOptionBox();
		fnViewModelInit();
		fnDefaultSet();
		fnInitGrid();
		// fnSearch();
		//orderSubmitUtil.search();
	};

	//--------------------------------- Button Start---------------------------------
	// 버튼 초기화
	function fnInitButton(){
		$("#fnSearch,  #fnClear").kendoButton();
	};
	//--------------------------------- Button End-----------------------------------

	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid(){

		var form = $("#searchForm");

		// form.formClear(false);
		var data = form.formSerialize(true);

		if($('input[name=selectConditionType]:checked').length > 0) {
			data['selectConditionType'] = $('input[name=selectConditionType]:checked').val();
		}


		orderGridDs = fnGetPagingDataSource({
			url      : "/admin/order/getOrderDetailList",
			pageSize : PAGE_SIZE,
			filterLength: fnSearchData(data).length,
			filter: {
				filters: fnSearchData(data)
			}
		});

		orderGridOpt = {
			dataSource: orderGridDs,
			pageable  : { pageSizes: [20, 30, 50], buttonCount : 10 },
			navigatable : true,
			height : 755,
			scrollable : true,
			columns : orderDetailGridUtil.orderDetailList()
		};

		orderGrid = $('#orderGrid').initializeKendoGrid( orderGridOpt ).cKendoGrid();


		orderGridEventUtil.noView();
		orderGridEventUtil.click();
	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){

		//var searchData = orderSearchUtil.searchDataMutil();
		var searchDataSingle = orderSearchUtil.searchDataSingle();
		//고객검색
		// var searchDataForBuyer;
		//고객검색
		let searchDataForCustomer;
		//연락처검색
		let searchContactData;
		//주문검색
		let searchDataForOrder ;
		//상품검색
		let searchDataForGoods ;

		if(csParamData != undefined){ //cs관리
			if(csParamData.urUserId != undefined && csParamData.urUserId != ''){
				searchDataForCustomer = orderSearchUtil.csSearchDataForCustomerMulti();
				searchContactData = orderSearchUtil.csSearchDataForContactMulti();
				searchDataForOrder = orderSearchUtil.csSearchDataDetailForOrderMutil();
				searchDataForGoods = orderSearchUtil.csSearchDataDetailForGoodsMutil();
			}else{
				searchDataForCustomer = orderSearchUtil.searchDataForCustomerMulti();
				searchContactData = orderSearchUtil.searchDataForContactMulti();
				searchDataForOrder = orderSearchUtil.searchDataDetailForOrderMutil();
				searchDataForGoods = orderSearchUtil.searchDataDetailForGoodsMutil();
			}
		}else{
			searchDataForCustomer = orderSearchUtil.searchDataForCustomerMulti();
			searchContactData = orderSearchUtil.csSearchDataForContactMulti();
			searchDataForOrder = orderSearchUtil.searchDataDetailForOrderMutil();
			searchDataForGoods = orderSearchUtil.searchDataDetailForGoodsMutil();
		}

		var dateSearchData = [
            { "CODE" : "CREATE_DATE", "NAME" : "주문일자" },
            { "CODE" : "PAY_DATE", "NAME" : "결제완료일자" },
			{ "CODE" : "ORDER_IF_DATE", "NAME" : "주문 I/F" },
			{ "CODE" : "SHIPPING_DATE", "NAME" : "출고예정일" },
			{ "CODE" : "DELIVERY_DATE", "NAME" : "도착예정일" },
			{ "CODE" : "DELIVERY_ING_DATE", "NAME" : "배송중" },
			{ "CODE" : "BUY_FINALIZED_DATE", "NAME" : "구매확정" },
			{ "CODE" : "CANCEL_COMPLETE_DATE", "NAME" : "취소완료" },
			{ "CODE" : "RETURN_COMPLETE_DATE", "NAME" : "반품완료" },
			{ "CODE" : "SETTLE_DATE", "NAME" : "정산일자" },
        ];

		mutilSearchCommonUtil.default();//단일조건,복수조건 radio
		orderSearchUtil.searchTypeKeyword(null,searchDataSingle); //dropDown 공통
		orderSearchUtil.searchTypeKeywordForOrderDetail("searchMultiTypeForCustomer",searchDataForCustomer); //dropDown 공통
		orderSearchUtil.searchTypeKeywordForOrderDetail("searchMultiTypeForContact",searchContactData); //dropDown 공통
		orderSearchUtil.searchTypeKeywordForOrderDetail("searchMultiTypeForOrder",searchDataForOrder); //dropDown 공통
		orderSearchUtil.searchTypeKeywordForOrderDetail("searchMultiTypeForGoods",searchDataForGoods); //dropDown 공통
		orderSearchUtil.dateSearch(dateSearchData,-7); //기간검색
		orderSearchUtil.dateMutilSearch(dateSearchData,-7); //기간검색

		// 엑셀 양식 유형 - 엑셀다운로드 양식을 위한 공통
		orderSearchUtil.excelDownList();

		 // 판매처그룹
		searchCommonUtil.getDropDownCommCd("sellersGroup", "NAME", "CODE", "판매처 그룹 전체", "SELLERS_GROUP", "Y", "", "", "");

		//주문상태
		orderSearchUtil.getCheckBoxUrl(orderOptionUtil.orderStatus("orderState", "O"));

		//클레임
		orderSearchUtil.getCheckBoxUrl(orderOptionUtil.orderStatus("claimState", "C"));

		//결제수단
		orderSearchUtil.getCheckBoxCommCd(orderOptionUtil.paymentMethod);

		//주문자유형
		orderSearchUtil.getCheckBoxCommCd(orderOptionUtil.buyerType);

		//주문유형
		orderSearchUtil.getCheckBoxCommCd(orderOptionUtil.orderType);

		//유형
		orderSearchUtil.getCheckBoxCommCd(orderOptionUtil.agentType);

		// 배송유형
		orderSearchUtil.getCheckBoxCommCd(orderOptionUtil.delivType);

		// 송장없음
		orderSearchUtil.getCheckBoxLocalData(orderOptionUtil.trackingNoYn);

		// 정산일 없음
		orderSearchUtil.getCheckBoxLocalData(orderOptionUtil.settleInfoYn);

		// 취소주문제외
		orderSearchUtil.getCheckBoxLocalData(orderOptionUtil.orderCntYn);

		// 녹즙,내맘대로배송
		orderSearchUtil.getCheckBoxLocalData(orderOptionUtil.selectGreenjuice);

		// 공급업체
		const SUPPLIER_ID = "supplierId";
		searchCommonUtil.getDropDownUrl(SUPPLIER_ID, SUPPLIER_ID, "/admin/comn/getDropDownSupplierList", "supplierName", "supplierId", "공급업체 전체");

		// 출고처그룹
		searchCommonUtil.getDropDownCommCd("warehouseGroup", "NAME", "CODE", "출고처 그룹 전체", "WAREHOUSE_GROUP", "Y");

		const $warehouseGroup = $("#warehouseGroup").data("kendoDropDownList");

		if( $warehouseGroup ) {
			$warehouseGroup.bind("change", function (e) {
				const warehouseGroupCode = this.value();

				fnAjax({
					url     : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
					method : "GET",
					params : { "warehouseGroupCode" : warehouseGroupCode },
					success : function( data ){
						let warehouseId = $("#warehouseId").data("kendoDropDownList");
						warehouseId.setDataSource(data.rows);
					},
					error : function(xhr, status, strError){
						fnKendoMessage({ message : xhr.responseText });
					},
					isAction : "select"
				});
			});
		}

		// 출고처그룹 별 출고처
		const WAREHOSE_ID = "warehouseId";
		searchCommonUtil.getDropDownUrl(WAREHOSE_ID, WAREHOSE_ID, "/admin/comn/getDropDownWarehouseGroupByWarehouseList", "warehouseName", "warehouseId", "출고처 선택", "", "");

		//searchCommonUtil.getCheckBoxCommCd("discountTypeCode", "DISCOUNT_TYPE", "Y");	//할인유형
		//searchCommonUtil.dateSearch(); //defVal, defType 사용 필요

		$("input[name=sellersGroup]").each(function() {
			$(this).bind("change", onPurchaseTargetType);
		});

		fbCheckboxChange();

		$('#findKeywordForBuyer').on('keydown',function(e){
			if (e.keyCode == 13) {
				if ($.trim($(this).val()) == ""){
					fnKendoMessage({message: "검색어를 입력하세요."});
				} else {
					orderSubmitUtil.search();
				}
				return false;
			}
		});

		$('#findKeywordForOrder').on('keydown',function(e){
			if (e.keyCode == 13) {
				if ($.trim($(this).val()) == ""){
					fnKendoMessage({message: "검색어를 입력하세요."});
				} else {
					orderSubmitUtil.search();
				}
				return false;
			}
		});

		$('#findKeywordForGoods').on('keydown',function(e){
			if (e.keyCode == 13) {
				if ($.trim($(this).val()) == ""){
					fnKendoMessage({message: "검색어를 입력하세요."});
				} else {
					orderSubmitUtil.search();
				}
				return false;
			}
		});
	};


	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ----------------------------------

    // viewModel 초기화
    function fnViewModelInit(){};

    // 기본값 설정
    function fnDefaultSet(){};

	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	orderSubmitUtil.search();	};

	/** Common Clear*/
	$scope.fnClear =function(){	 orderSubmitUtil.orderDetailListClear();	};

    $scope.fnExcelMake = function(){
        var grid = $("#orderGrid").data("kendoGrid");
        var gridDs = grid.dataSource;

        if(gridDs._pristineData.length <= 0){
            fnKendoMessage({ message : "조회 후 다운로드 가능합니다." });
            return;
        }

        var psExcelTemplateId = stringUtil.getString($("input[name='psExcelTemplateId']").val(), "0");
        var privacyIncludeYn = stringUtil.getString($("#privacyIncludeYn_"+psExcelTemplateId).val(), "N");

        if (privacyIncludeYn == "Y") {
            fnKendoPopup({
                id: "excelDownloadReasonPopup",
                title: "엑셀 다운로드 사유",
                src: "#/excelDownloadReasonPopup",
                param: {excelDownloadType: "EXCEL_DOWN_TP.ORDER"},
                width: "700px",
                height: "300px",
                success: function (id, response) {
                    if (response == 'EXCEL_DOWN_TP.ORDER') {
                        var form = $("#searchForm");
                        var data = form.formSerialize(true);

                        if ($("#psExcelTemplateId").length > 0) {
                            if ($("#psExcelTemplateId").val().length < 1) {
                                fnKendoMessage({message: "다운로드 양식을 선택해주세요."});
                                return;
                            }
                            data['psExcelTemplateId'] = $("#psExcelTemplateId").val();
                        }

                        var selectConditionType = $('input[name=selectConditionType]:checked').val();

                        if ($('input[name=selectConditionType]:checked').length > 0) {
                            data['selectConditionType'] = selectConditionType;
                        }
                        fnCallExcelMake(data);
                    }
                }
            });
        } else {
            var form = $("#searchForm");
            var data = form.formSerialize(true);

            if ($("#psExcelTemplateId").length > 0) {
                if ($("#psExcelTemplateId").val().length < 1) {
                    fnKendoMessage({message: "다운로드 양식을 선택해주세요."});
                    return;
                }
                data['psExcelTemplateId'] = $("#psExcelTemplateId").val();
            }

            var selectConditionType = $('input[name=selectConditionType]:checked').val();

            if ($('input[name=selectConditionType]:checked').length > 0) {
                data['selectConditionType'] = selectConditionType;
            }
            fnCallExcelMake(data);
        }

    }

    function fnCallExcelMake(requestData){
        // 엑셀다운로드 버튼 활성화
        $("#btnExcelMake").html("엑셀 파일생성중.");
        $("#btnExcelMake").prop('disabled', true);
        $("#btnExcelDownload").prop('disabled', true);

        fnAjax({
            url     : '/admin/order/getOrderDetailExcelListMake',
            params  : requestData,
            contentType : "application/json",
            success :
                function( responseData ){
                    var obj = responseData.dataDownloadTypeResult;
                    var stExcelDownloadAsyncId;
                    for (var i = 0; i < obj.length; i++) {
                        var arrData = obj[i].split('___');
                        stExcelDownloadAsyncId = arrData[0];
                        gFilePath = arrData[1].replace(/\\/ig,"/");
                    }

                    // 조회
                    gExcelMakeChecker = setInterval(function() {
                        if($("#btnExcelMake").html() == "엑셀 파일생성중.."){
                            $("#btnExcelMake").html("엑셀 파일생성중...");
                        }else{
                            $("#btnExcelMake").html("엑셀 파일생성중..");
                        }
                        fnExcelFileReady(stExcelDownloadAsyncId);
                    }, 3000);
                },
            isAction : 'select'
        });
    }

    function fnExcelFileReady(stExcelDownloadAsyncId){
        fnAjax({
            url     : '/admin/order/getExcelDownloadAsyncUseYn',
            params : {"stExcelDownloadAsyncId" : stExcelDownloadAsyncId},
            contentType : "application/json",
            success :
                function( responseData ){
                    if(responseData.rows == "Y") {
                        // 엑셀다운로드 버튼 활성화
                        $("#btnExcelMake").prop('disabled', false);
                        $("#btnExcelDownload").prop('disabled', false);
                        $("#btnExcelMake").html("엑셀 파일생성요청");

                        // 중지
                        clearInterval(gExcelMakeChecker);
                    } else if(responseData.rows == "E") {
                        // 엑셀다운로드 버튼 활성화
                        $("#btnExcelMake").prop('disabled', false);
                        $("#btnExcelDownload").prop('disabled', true);
                        $("#btnExcelMake").html("엑셀 파일생성요청");

                        // 중지
                        clearInterval(gExcelMakeChecker);
                        fnKendoMessage({ message : "엑셀 파일 생성중 오류가 발생하였습니다. 관리자에게 문의해 주세요." });
                    }
                    return;
                },
            isAction : 'select'
        });
    }

//	$scope.fnExcelDownload =function(btnObj){
//		$("input[name='psExcelTemplateId']").val($("#psExcelTemplateId").val());
//		orderSubmitUtil.excelExportDown('/admin/order/getOrderDetailExcelList', btnObj);
//	};

    /** Button fnExcelDownload */
    $scope.fnExcelDownload = function( ) {
        var filePath = gFilePath.substring(0, gFilePath.lastIndexOf("/"));
        var fileName = gFilePath.substring(gFilePath.lastIndexOf("/")+1);
        var opt = {
            filePath: filePath,
            physicalFileName: fileName,
            originalFileName: fileName
        }
        console.log("@@@@@ ", opt);

        var event = window.event;
        var targetElement = event.target || event. srcElement;

        fnDownloadPublicFileCallback(opt, function(){
            // fnDeletePublicFile(opt); // 다운로드 완료된 파일 삭제
            // targetElement.style.display = "none"; // 다운로드 버튼 hidden 처리
        });
    };

    // fnExcelDownload() 콜백 함수
    function fnDownloadPublicFileCallback(opt, fnCallback) {  // Public 저장소의 파일 다운로드
        var filePath = opt.filePath; // 다운로드할 파일의 하위 경로
        var physicalFileName = opt.physicalFileName; // 업로드시 저장된 물리적 파일명
        var originalFileName = opt.originalFileName; // 원본 파일명 또는 다운로드시 지정할 파일명

        // 파일 다운로드시 Response Header 에 인코딩된 파일명을 지정하는 Key 값
        var downloadFileNameResponseHeaderKey = 'file-name';
        var downloadUrl = '/comn/fileDownload'; // 파일 다운로드 API
        downloadUrl += '?' + 'filePath' + '=' + filePath;
        downloadUrl += '&' + 'physicalFileName' + '=' + physicalFileName;
        downloadUrl += '&' + 'originalFileName' + '=' + originalFileName;

        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {  // 요청에 대한 콜백
            fnOpenLoading();

            if (xhr.readyState === xhr.DONE) { // 요청 완료시
                // 다운로드 완료시 서버에서 전달한 파일명
                var downloadFileName = decodeURIComponent( //
                    decodeURI( //
                        xhr.getResponseHeader(downloadFileNameResponseHeaderKey) //
                    ) //
                        .replace(/\+/g, " ") // 파일명에서 공백이 + 로 표시 : " " 으로 replace 처리
                );
                if( xhr.status === 200 || xhr.status === 201 ) { // 다운로드 성공
                    if (typeof window.navigator.msSaveBlob !== 'undefined') { // IE 사용시
                        window.navigator.msSaveBlob(xhr.response, downloadFileName);
                    } else {
                        var a = document.createElement('a');
                        a.href = window.URL.createObjectURL(xhr.response);
                        a.download = downloadFileName;
                        a.style.display = 'none';
                        document.body.appendChild(a);
                        a.click();
                        fnCloseLoading();
                    }
                    fnCallback(); // callback 추가
                } else { // 다운로드 실패
                    var reader = new FileReader();
                    reader.addEventListener('loadend', (e) => {
                        var errorMessage = JSON.parse(e.srcElement['result']);
                        console.log(errorMessage);
                    });
                    reader.readAsText(xhr.response);

                    fnCloseLoading();
                }
            }
        } // xhr.onreadystatechange end

        xhr.open('GET', downloadUrl);
        xhr.responseType = 'blob';
        xhr.send();
    }

	$scope.fnTotalCountView =function(){
		orderSubmitUtil.totalCountView();
	};


	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	//fnInputValidationForAlphabetNumberLineBreakComma("codeSearch");
	// 입력가능 : 한글 & 영어(대,소) & 숫자 & 특수문자  []~!@#$%^&*()_+|<>?:{}
	// fnInputValidationByRegexp("findKeyword", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);
	fnInputValidationByRegexp("findKeywordForAddr", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);
	fnInputValidationByRegexp("findKeywordForCustomer", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);
	fnInputValidationByRegexp("findKeywordForOrder", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);
	fnInputValidationByRegexp("findKeywordForGoods", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);
	//------------------------------- Validation End -------------------------------------
}); // document ready - END
