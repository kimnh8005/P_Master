/**-----------------------------------------------------------------------------
 * system 	       : ERP 재고 엑셀 업로드 상세내역
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.23		이성준          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var itemGridOpt, itemGrid, itemGridDs;
var pageParam = fnGetPageParam();

$(document).ready(function() {

	var ilStockExcelUploadLogId = pageParam.ilStockExcelUploadLogId;//상세보기 업로드ID param
	var createDt = pageParam.createDt;//등록일시 param

	$('#createDt').text(createDt);

	if(isNaN(ilStockExcelUploadLogId)){
		ilStockExcelUploadLogId = 0; //넘어온 값이 없으면 0
	}

    // Initialize Page Call
	fnInitialize();

    //Initialize PageR
    function fnInitialize() {
        $scope.$emit("fnIsMenu", { flag: true });

        fnPageInfo({
            PG_ID: "itemStockUploadDetlList",
            callback: fnUI
        });
    };

    function fnUI() {
    	fnTranslate();			// comm.lang.js 안에 있는 공통함수 다국어
    	fnInitButton();			// Initialize Button
    	fnInitCtgry();			// 라디오 , 체크박스 셋팅.
    	fnInitDate();			// 달력 컴포넌트 셋팅.

    	//탭 변경 이벤트
        fbTabChange();			// fbCommonController.js - fbTabChange 이벤트 호출
        setDate(); 				// 날짜 셋

        fnItemGrid();			// ERP 재고 엑셀 업로드 상세내역 Grid

        if(ilStockExcelUploadLogId != 0 ) { //0이 아니면 상세조회
           fnSearch();
        }
    };

	function fnInitButton() {
		$('#fnSearch01 , #fnClear01, #EXCEL_DN').kendoButton();
	};

	function fnInitCtgry() {
        fnTagMkRadio({
            id: 'selectConditionType',
            tagId: 'selectConditionType',
            chkVal: 'codeSearch',
            tab: true,
            data: [{
                CODE: "codeSearch",
                NAME: "단일조건 검색",
                TAB_CONTENT_NAME: "singleSection"
            }, {
                CODE: "condSearch",
                NAME: "복수조건 검색",
                TAB_CONTENT_NAME: "multiSection"
            }],
        });

		//상품 코드 검색
        fnKendoDropDownList({
       		id: "searchKind",
            data: [
            	{
            		CODE: "1",
            		NAME: "마스터 품목명",
            	},
            	{
            		CODE: "2",
            		NAME: "마스터 품목코드",
            	}
            ],
            valueField: "CODE",
            textField: "NAME",
        });
    }

    //켄도 datepicker 만드는 함수
    function fnInitDate() {
    	//
		fnKendoDatePicker({
			id : 'distributionPeriod',
			format : 'yyyy-MM-dd'
		});
    }

	//날짜 설정 함수
    function setDate(_day = 0) {
        //오늘 날짜
        const todayDate = new Date();

        if (!isNaN(_day)) {
            _day = _day * 24 * 60 * 60 * 1000;
            const startTime = new Date(todayDate - _day);
            $("#distributionPeriod").val(startTime.format("yyyy-MM-dd"));
        } else {
        	$("#distributionPeriod").val("");
        }
    }

    //ERP 재고 엑셀 업로드 상세내역 Grid
	function fnItemGrid() {

		itemGridDs = fnGetEditPagingDataSource({
			url : '/admin/goods/stock/getStockUploadDetlList',
			pageSize : PAGE_SIZE,
            requestEnd : function(e){
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
				{ title:'No.',           width:'100px',        attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
			,	{ field : 'ilItemCd',    title : '마스터 품목코드', width : '140px', attributes : { style : 'text-align:center' } }
            ,   { field : 'itemNm',      title : '마스터 품목명',  width : '230px', attributes : { style : 'text-align:left' } }
            ,   { field : 'expirationDt',title : '유통기한',     width : '160px', attributes : { style : 'text-align:center' } }
            ,   { field : 'stockQty',    title : '수량',        width : '160px', attributes : { style : 'text-align:center' } }
            ]
		};

		itemGrid = $('#itemGrid').initializeKendoGrid(itemGridOpt).cKendoGrid();

		itemGrid.bind("dataBound", function() {
			let rowNum = itemGridDs._total - ((itemGridDs._page - 1) * itemGridDs._pageSize);

			$("#itemGrid tbody > tr .row-number").each(function(index){
				$(this).html(rowNum);
				rowNum--;
			});
			//total count
            $('#pageTotalText').text(itemGridDs._total);

        });

	};

	//엑셀 다운로드
	function fnExcelExport() {
		var data = $('#searchForm').formSerialize(true);

		data.ilStockExcelUploadLogId = ilStockExcelUploadLogId;

		fnExcelDownload('/admin/goods/stock/getStockUploadDetlExportExcel', data);
	}

	//검색
	function fnSearch() {
		var data = $('#searchForm').formSerialize(true);

		data.ilStockExcelUploadLogId = ilStockExcelUploadLogId;
		data.itemCodes = data.itemCodes.trim();

		if(ilStockExcelUploadLogId == 0){
			fnKendoMessage({ message : "ERP 재고 엑셀 업로드 내역 상세보기 검색만 가능합니다." });
			return false;
		}

		//마스터품목명 필수체크
		if(data.selectConditionType == "condSearch"){
			if(data.itemName.trim() == ""){
				fnKendoMessage({message : "검색어는 필수 입니다"});
				return false;
			}else{
				data.itemName = data.itemName.trim();
			}
		}

		const _pageSize = itemGrid && itemGrid.dataSource ? itemGrid.dataSource.pageSize() : PAGE_SIZE;

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

	//검색조건 초기화
	function fnClear() {

		if($("input[name=selectConditionType]:checked").val() == "codeSearch") {
			$('#searchForm').formClear(true);
		}

		if($("input[name=selectConditionType]:checked").val() == "condSearch") {
			$('#itemName').val("");
            setDate();
		}

	};

	/** Common Search */
	$scope.fnSearch = function() {
		fnSearch();
	};

	/** Common Clear */
	$scope.fnClear = function() {
		fnClear();
	};

	/** Common Excel */
	$scope.fnExcelExport = function() {
		fnExcelExport();
	};


}); // document ready - END
