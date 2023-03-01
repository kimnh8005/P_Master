/*******************************************************************************************************************************************************************************************************
 * --------------- description : 마스터 품목 관리 - 발주 상세정보 팝업 @ ----------------------------
 ******************************************************************************************************************************************************************************************************/
'use strict';

var PAGE_SIZE = 10;
var aGridDs, aGridOpt, aGrid;

var pageParam = parent.POP_PARAM["parameter"];
var viewModel;
var erpItemCode = pageParam["erpItemCode"]; // 해당 ERP 연동 품목의 품목 코드
var erpItemName = pageParam['erpItemName']; // 해당 ERP 연동 품목의 품목명

$(document).ready(function() {
    fnInitialize(); //Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize() {

        $scope.$emit('fnIsMenu', {
            flag : false
        });

        fnPageInfo({
            PG_ID : 'poTypeDetailInformationPopup',
            callback : fnUI
        });

    }

    function fnUI() {

        fnTranslate(); // 다국어 변환--------------------------------------------

        fnInitButton(); //Initialize Button  ---------------------------------
        fnViewModelInit();
        fnSetParamValue();

        fnInitGrid(); //Initialize Grid ------------------------------------

        $('#erpItemCode').html(erpItemCode); // 품목코드 출력
        $('#erpItemName').html(erpItemName); // 품목명 출력

        fnSearch();

    }

    function fnViewModelInit() {
		viewModel = new kendo.data.ObservableObject({
            searchInfo : { // 조회조건
            	ilPoTpId : "", 	// 발주유형Key
            },
        });

		kendo.bind($("#searchForm"), viewModel);
	};

	// 파라미터 셋팅
    function fnSetParamValue(){

        if( pageParam != undefined ){
            if( pageParam.ilPoTpId != undefined ){
                viewModel.searchInfo.set("ilPoTpId", pageParam.ilPoTpId);

            }
        }

    };


    //--------------------------------- Button Start---------------------------------

    function fnInitButton() {
        $('#fnClose').kendoButton();
    }

    function fnSearch() {}

    function fnClose() {
        parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
    }

    //--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------
    var weekHanArray = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
    function fnInitGrid() {

        aGridDs = fnGetPagingDataSource({
            url : '/admin/item/master/getPoTpDetailInfoList',
            pageSize : PAGE_SIZE,
        });

        aGridOpt = {
            dataSource : aGridDs,
            pageable : false,
            editable : false,
            scrollable : true,
            navigatable : true,
            sellectable : false,
            noRecordMsg : '발주 정보는 해당 API 확인 후 개발 예정입니다.',
            columns : [
            		{field : 'weekDay', title : '발주 가능일', width : '50%', attributes : { style : 'text-align:center' }
    	            },
            		{field : 'scheduledWeek', title : '입고 예정', width : '50%', attributes : { style : 'text-align:center'},
                        template: function(dataItem) {

                        	var weekIndex = weekHanArray.indexOf(dataItem.weekDay) + parseInt(dataItem.scheduledWeek);
                        	if(weekIndex >=7) {
                        		weekIndex = weekIndex % 7;
    						}

                        	if(parseInt(dataItem.scheduledWeek) >= 7) {
                        		return "차주  " +  weekHanArray[weekIndex];
                        	}else {
                        		return weekHanArray[weekIndex];
                        	}
    	              }
            		}
            ]

        };

        aGrid = $('#aGrid').initializeKendoGrid(aGridOpt).cKendoGrid();

        var data = $('#searchForm').formSerialize(true);

        const	_pageSize = aGrid && aGrid.dataSource ? aGrid.dataSource.pageSize() : PAGE_SIZE;

        var query = {
    			page : 1,
    			pageSize : _pageSize,
    			filterLength : fnSearchData(data).length,
    			filter : {
    				filters : fnSearchData(data)
    			}
    	};

    	aGridDs.query(query);

    }

    //-------------------------------  Grid End  -------------------------------

    //------------------------------- Html 버튼 바인딩  Start -------------------------------
    /** Common Close*/
    $scope.fnClose = function() {
        fnClose();
    };
    //------------------------------- Html 버튼 바인딩  End -------------------------------
});