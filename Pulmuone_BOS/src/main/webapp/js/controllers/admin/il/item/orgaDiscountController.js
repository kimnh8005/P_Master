/*******************************************************************************************************************************************************************************************************
 * -------------------------------------------------------- description : 올가 할인연동 내역 - 올가 할인연동 리스트 * @ @ 수정일 수정자 수정내용 @ ------------------------------------------------------ * @ 2020.11.12 정형진 최초생성 @
 ******************************************************************************************************************************************************************************************************/
'use strict';
var pageParam = fnGetPageParam(); // GET 방식으로 전달된 parameter 객체

var PAGE_SIZE = 20;
var itemGridOpt, itemGrid, itemGridDs;

$(document).ready(function() {

    // Initialize Page Call
	fnInitialize();

    //Initialize PageR
    function fnInitialize() {
        $scope.$emit("fnIsMenu", { flag: true });

        fnPageInfo({
            PG_ID: "orgaDiscount",
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

//        setDate(); 				// 날짜 셋팅

        fnItemGrid();			// 올가할인 내역 Grid
    };

	function fnInitButton() {
		$('#fnSearch01 , #fnClear01, #fnSearch02 , #fnClear02 , #EXCEL_DN').kendoButton();
	};

	function fnInitCtgry() {
        fnTagMkRadio({
            id: 'selectConditionType',
            tagId: 'selectConditionType',
            chkVal: 'codeSearch',
            tab: true,
            data: [{
                CODE: "codeSearch",
                NAME: "코드 검색",
                TAB_CONTENT_NAME: "singleSection"
            }, {
                CODE: "condSearch",
                NAME: "조건 검색",
                TAB_CONTENT_NAME: "multiSection"
            }],
        });
    }

  //켄도 datepicker 만드는 함수
    function fnInitDate() {
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
    }


	//날짜 설정 함수
    function setDate(_day = 7) {
        //오늘 날짜
        const todayDate = new Date();

        if (!isNaN(_day)) {
            _day = _day * 24 * 60 * 60 * 1000;
            const startTime = new Date(todayDate - _day);
            $("#startDate").val(startTime.format("yyyy-MM-dd"));
            $("#endDate").val(todayDate.format("yyyy-MM-dd"));
        } else {
        	$("#startDate").val("");
        	$("#endDate").val("");
        }
    }


  //마스터 품목 리스트 Grid
	function fnItemGrid() {

		itemGridDs = fnGetEditPagingDataSource({
			url : '/admin/item/master/getOrgaDisList',
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
				{ field : 'ilItemCd',  title : '마스터 품묵코드', width : '180px', attributes : { style : 'text-align:center' }}
			,	{ field : 'itemBarcode', title : '품목 바코드', width : '180px', attributes : { style : 'text-align:center' } }
            ,   { field : 'itemNm', title : '마스터 품목명', attributes : { style : 'text-align:left' } }
            ,   { field : 'discountTpNm', title : '행사구분', width : '130px', attributes : { style : 'text-align:center' } }
            ,	{ field : 'discountStartDt', title : '할인 시작일', width : '140px', attributes : { style : 'text-align:center' } }
			,	{ field : 'discountEndDt', title : '할인 종료일', width : '140px', attributes : { style : 'text-align:center' } }
			,	{ field : 'discountSalePrice', title : '행사가', width : '120px', attributes : { style : 'text-align:center' } , format: "{0:n0}", locked: true }

            ]
		};

		itemGrid = $('#itemGrid').initializeKendoGrid(itemGridOpt).cKendoGrid();

	};

	//엑셀 다운로드
	function fnExcelExport() {
		var data = $('#searchForm').formSerialize(true);

		fnExcelDownload('/admin/item/master/orgaDisExportExcel', data);
	}

	//검색
	function fnSearch() {
		var data = $('#searchForm').formSerialize(true);

		var itemNameLengthChk = false;

		if($("input[name=selectConditionType]:checked").val() == "codeSearch") {
           	if($.trim($('#itemCodes').val()).length >= 0 && $.trim($('#itemCodes').val()).length < 2 ) {
        		itemNameLengthChk = true;
        			fnKendoMessage({
        				message : '코드 검색은 2글자 이상 입력해야 합니다.',
        				ok : function() {
        					return;
        				}
        			});
           	} else {
           		data['itemCodes'] = $.trim($('#itemCodes').val());
           	}
		}


		if(!itemNameLengthChk) {
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
		}

	};

	//검색조건 초기화
	function fnClear() {

		if($("input[name=selectConditionType]:checked").val() == "codeSearch") {
			$('#searchForm').formClear(true);
		}

		if($("input[name=selectConditionType]:checked").val() == "condSearch") {
//			setDate();
			 $('[data-id="fnDateBtnC"]').mousedown();
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
