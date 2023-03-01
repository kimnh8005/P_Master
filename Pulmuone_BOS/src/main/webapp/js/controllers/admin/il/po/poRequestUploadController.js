/**-----------------------------------------------------------------------------
 * description 		 : 행사발주관리 엑셀업로드
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.02.10     정형진			퍼블수정 및 기능개발
 * @
 * **/

'use strict';
var pageParam = fnGetPageParam(); // GET 방식으로 전달된 parameter 객체
var PAGE_SIZE = 20;
var aGridOpt, aGrid;
var itemGridOpt, itemGrid, itemGridDs;
var viewModel, sellerModel, searchModel;

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
			PG_ID : 'poRequestUpload',
			callback : fnUI
		});
	};

	function fnUI() {
		fnTranslate();			// comm.lang.js 안에 있는 공통함수 다국어

		fnInitButton();			// Initialize Button

		fnViewModelInit();

		fnGetSellerList();

		fnInitCompont();		// 행사발주 업로드 컴포넌트 Initialize

		fnInitUploadGrid();		// 업로드 엑셀 업로드 Grid Init

        fnItemGrid();			// 상품 할인 일괄업로드 상세 내역 Grid
	};


	function fnInitButton() {
		$('#fnSearch , #fnClear , #EXCEL_DN, #fnNew, #fnPoRequestExcelUpload, #fnSamepleFormDownload, #fnEmployeeSearchPopup').kendoButton();
	};

	// 그리드 초기화
	function fnInitUploadGrid(){
        aGridOpt = {
            columns   : [
                          {field : 'IL_GOODS_ID'	    , title : '상품코드'	, width : '60px', attributes : {style : 'text-align:center'}}
                        , {field : 'PO_EVENT_QTY'	, title : '행사발주수량'	, width : '60px', attributes : {style : 'text-align:center'}}
                        , {field : 'EVENT_START_DT'	    , title : '행사시작일'	    , width : '40px', attributes : {style : 'text-align:center'}}
                        , {field : 'EVENT_END_DT'	    , title : '행사종료일'	    , width : '40px', attributes : {style : 'text-align:center'}}
                        , {field : 'OM_SELLERS_ID'	    , title : '판매처코드'	    , width : '40px', attributes : {style : 'text-align:center'}}
                        , {field : 'FILE_NM'	    , title : '파일명'	, width : '40px', attributes : {style : 'text-align:center'}, hidden:true}
            ]
        };

        aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
    };

	function fnInitCompont() {
		// 업로드 팝업
	    $("#uploadPopup").kendoWindow({
            width : 800,
            height : 500,
            title : "상품 할인 일괄 업로드",
            visible : false,
            modal : true
        });

		//등록일/수정일 시작날짜
		fnKendoDatePicker({
			id : 'startDate',
			format : 'yyyy-MM-dd'
		});

		//등록일/수정일 종료날짜
		fnKendoDatePicker({
			id : 'endDate',
			format : 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startDate',
			btnEndId : 'endDate',
			change : function(e) {
				fnStartCalChange('startDate', 'endDate');
			}
		});

        // 업로드 등록 삭제
        $("#uploadDelete").on("click", function(e){
            e.preventDefault();

            fileClear();

            $("#uploadViewControl").hide();
            $("#uploadLink").text("");
            $("#aGrid").data("kendoGrid").dataSource.data( [] );
        });

	};

	function fnViewModelInit() {
		viewModel = new kendo.data.ObservableObject({
            searchInfo : { // 조회조건

            },
        });

		sellerModel = new kendo.data.ObservableObject({
            sellerInfo : { // 조회조건
            	sellerList : [],
            	isSellerListVisible : false,
            },
        });

		searchModel = new kendo.data.ObservableObject({
            searchInfo : { // 조회조건
				searchUserId : "" // 관리자 urUserId
				, searchUserNm : ""
				, startDate : "" // 등록일자 시작일
				, endDate : "" // 등록일자 종료일
            },
        });

		kendo.bind($("#excelUploadForm"), viewModel);
        kendo.bind($("#sellerForm"), sellerModel);
        kendo.bind($("#searchForm"), searchModel);
	};

	//행사발주 엑셀 업로드 Grid
	function fnItemGrid() {

		itemGridDs = fnGetEditPagingDataSource({
			url : '/admin/item/poRequest/getPoRequestUploadList',
			pageSize : PAGE_SIZE,
	           requestEnd : function(e) {
	        }
		});

		itemGridOpt = {
			dataSource : itemGridDs,
			pageable : {
				pageSizes : [ 20, 30, 50 ],
				buttonCount : 5
			},
			editable : false,
			navigatable: true,
			columns : [
				{ field : 'rownum',  title : 'No', width : '40px', attributes : { style : 'text-align:center' }}
			,	{ field : 'ilPoEventExcelUploadLogId',  title : 'ilPoEventExcelUploadLogId', width : '40px', attributes : { style : 'text-align:center' }, hidden:true}
			,	{ field : 'successCnt', title : '정상', width : '160px', attributes : { style : 'text-align:center' } ,
		          template : function(dataItem){
                      return dataItem.successCnt + "건";
		          },
                }
            ,   { field : 'failCnt', title : '실패', width : '200px', attributes : { style : 'text-align:center' } ,
		          template : function(dataItem){
                      return dataItem.failCnt + "건";
		          },
                }
            ,   { field : 'createId', title : '관리자', width : '160px', attributes : { style : 'text-align:center' } ,
		          template : function(dataItem){
                      return dataItem.userNm + "/ ( " + dataItem.loginId + " )";
		          },
                }
            ,	{ field : 'createDt', title : '등록일자', width : '80px', attributes : { style : 'text-align:center' } }
            ,   { field : 'failCnt', title : '관리', width : '100px', attributes : { style : 'text-align:center' },
        		template : function(dataItem){
        			if(dataItem.failCnt > 0) {
						return '<button type="button" class="k-button k-button-icontext k-grid-실패내역다운로드" kind="btnExcelDown">실패내역 다운로드 </button>';
        			}else {
        				return ''
        			}
        		}
      		}

            ]
		};

		itemGrid = $('#itemGrid').initializeKendoGrid(itemGridOpt).cKendoGrid();

		itemGrid.bind("dataBound", function() {
			$('#pageTotalText').text( kendo.toString(itemGridDs._total, "n0") );
		});

	    // 실패내역 다운로드 버튼 이벤트.
	    $('#itemGrid').on("click", "button[kind=btnExcelDown]", function(e) {
	        e.preventDefault();
	        let dataItem = itemGrid.dataItem($(e.currentTarget).closest("tr"));

	        $("#logId").val(dataItem.ilPoEventExcelUploadLogId);

	        var data = $('#searchForm').formSerialize(true);

			fnExcelDownload('/admin/item/poRequest/createPoRequestUplodFailList', data);

	    });

	};

	function fnGetSellerList() {
		fnAjax({
			url		: '/admin/comn/getDropDownSellersGroupBySellersList',
			method : "GET",
			params : {},
			async	: false,
			success	: function(data){

				sellerModel.sellerInfo.set("sellerList", data.rows);
			}
		});
	};

	function fnToggleArea() {

		if(!sellerModel.sellerInfo.get("isSellerListVisible")) {
			sellerModel.sellerInfo.set("isSellerListVisible", true);
		}else {
			sellerModel.sellerInfo.set("isSellerListVisible", false);
		}
	};

    // 샘플다운로드 버튼 클릭
	function fnSamepleFormDownload(){
		document.location.href = "/contents/excelsample/itemPo/samplePoEventRequest.xlsx"
	};

	// 업로드 버튼 클릭
    function fnPoRequestExcelUpload(){
    	fileClear();
        $("#uploadFile").trigger("click");
    };

	// 업로드 파일 클리어
    function fileClear() {
    	var agent = navigator.userAgent.toLowerCase();
        if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ){
            // ie 일때 input[type=file] init.
            $("#uploadFile").replaceWith( $("#uploadFile").clone(true) );
        } else {
            //other browser 일때 input[type=file] init.
            $("#uploadFile").val("");
        }
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

            excelData.splice(0,1);
			$("#aGrid").data("kendoGrid").dataSource.data(excelData);
            $("#uploadViewControl").show();
            $("#uploadLink").text(fileName);

/*
            wb.SheetNames.forEach(function(sheetName) {

                var excelData = XLSX.utils.sheet_to_json(wb.Sheets[sheetName]);

                excelData.splice(0,1);

                $("#aGrid").data("kendoGrid").dataSource.data(excelData);
                $("#uploadViewControl").show();
                $("#uploadLink").text(fileName);

            })
*/
        };

        reader.readAsBinaryString(input.files[0]);
    };

    // 보내기
	function fnAddExcelSubmit(){
        let url  = "/admin/item/poRequest/addPoRequestExcelUpload";
        let inputFormValidator = $("#excelUploadForm").kendoValidator().data("kendoValidator");

        let fileNm = $("#uploadLink").text();

        if(fileNm == ""){//파일유무 체크
        	fnKendoMessage({message : "엑셀 등록은 필수입니다."});
            return false;
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

	// 엑셀 업로드 submit
    function fnInputFormSubmit(url, cbId){
        let inputFormData = $("#excelUploadForm").formSerialize(true);

        inputFormData['upload'] = kendo.stringify( fnGetDataList() );

        fnAjax({
            url     : url,
            params  : inputFormData,
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
                let poEventData = {};

                poEventData.ilGoodsId      			= aGridData[i].IL_GOODS_ID;
                poEventData.poEventQty  			= aGridData[i].PO_EVENT_QTY;
                poEventData.eventStartDt      		= aGridData[i].EVENT_START_DT;
                poEventData.eventEndDt      		= aGridData[i].EVENT_END_DT;
                poEventData.omSellersId			 	= aGridData[i].OM_SELLERS_ID;
                poEventData.fileNm        			= FILE_NM;

                DataArray[i] = poEventData;
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

            	// 업로드 파일 삭제
            	fileClear();
                $("#uploadViewControl").hide();
                $("#uploadLink").text("");
                $("#aGrid").data("kendoGrid").dataSource.data( [] );

            	var result = data.split("|"); //총건수|성공건수|실패건수

            	fnKendoMessage({message : "총 "+result[0]+"건</br>"+"정상 "+result[1]+"건 / "+"실패 "+result[2]+"건", ok : fnClose});

                break;
        }
    };

    //확인후 url이동
    function fnClose() {
      var option = new Object();

//      option.url = "#/goodsDiscountUploadList";// ERP 재고 엑셀 업로드 내역 화면 URL
//      option.menuId = 1018;// ERP 재고 엑셀 업로드 내역 화면 메뉴 ID
//
//      fnGoPage(option);
    }

	function fnEmployeeSearchPopup(apprManagerType){
		fnKendoPopup({
			id     : 'employeeSearchPopup',
			title  : 'BOS 계정 선택',
			src    : '#/employeeSearchPopup',
			width  : '1050px',
			height : '800px',
            scrollable : "yes",
			success: function( stMenuId, data ){
				if(!fnIsEmpty(data.userId)){
					searchModel.searchInfo.set("searchUserId", data.userId);
					searchModel.searchInfo.set("searchUserNm", data.userName);
				}else{
//					fnKendoMessage({ message : "올바르지 않은 관리자입니다." });
				}
			}
		});
	};

    function fnSearch() {
		var data = $('#searchForm').formSerialize(true);

		 const	_pageSize = itemGrid && itemGrid.dataSource ? itemGrid.dataSource.pageSize() : PAGE_SIZE;

		var query = {
			page : 1,
			pageSize : _pageSize,
			filterLength : fnSearchData(data).length,
			filter : {
				filters : fnSearchData(data)
			}
		};

		itemGridDs.query(query);
	};

	function fnClear() {
		fnDefaultSet();

        $('[data-id="fnDateBtnC"]').mousedown(); // 날짜 버튼 초기화
	};

	function fnDefaultSet() {
		searchModel.searchInfo.set("searchUserId", "");
		searchModel.searchInfo.set("searchUserNm", "");
		searchModel.searchInfo.set("startDate", "");
		searchModel.searchInfo.set("endDate", "");
	};

	/** Common Search */
	$scope.fnSearch = function() {
		fnSearch();
	};

	/** Common Clear */
	$scope.fnClear = function() {
		fnClear();
	};

	$scope.fnToggleArea = function(){
        fnToggleArea();
    };

    $scope.fnSamepleFormDownload =function(){ fnSamepleFormDownload(); }; // 샘플다운로드 버튼
    $scope.fnPoRequestExcelUpload =function(){ fnPoRequestExcelUpload(); }; // 업로드 파일첨부 버튼
    $scope.fnExcelUpload = function(event) { excelExport(event);} // 엑셀 업로드 버튼
    $scope.fnAddExcelSubmit =function(){ fnAddExcelSubmit(); }; // 보내기 버튼
	$scope.fnEmployeeSearchPopup = function(apprManagerType){ fnEmployeeSearchPopup(apprManagerType); }; // 관리자 검색
});
