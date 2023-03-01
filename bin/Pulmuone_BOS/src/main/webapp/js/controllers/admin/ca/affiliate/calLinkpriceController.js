/**-----------------------------------------------------------------------------
 * description 		 : 정산관리 > 링크프라이스 > 링크프라이스 내역조회
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.07.19		whseo          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {

	fnInitialize();	// Initialize Page Call ---------------------------------

	// Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'calLinkprice',
			callback : fnUI
		});
	}

	function fnUI(){
		fnInitButton();	// Initialize Button ---------------------------------
        fnInitGrid();	// Initialize Grid ------------------------------------
		fnInitOptionBox();// Initialize Option Box
		fnDefaultSet();
	}

    // 옵션 초기화
    function fnInitOptionBox(){
        // 기준기간 시작일/종료일 [날짜]
        fnKendoDatePicker({
            id          : 'startDt'
            , format      : 'yyyy-MM-dd'
            , btnStartId  : 'startDt'
            , btnEndId    : 'endDt'
            , defVal: fnGetDayMinus(fnGetToday(),30)
            , defType : 'oneMonth'
        });
        fnKendoDatePicker({
            id          : 'endDt'
            , format      : 'yyyy-MM-dd'
            , btnStyle    : true     //버튼 노출
            , btnStartId  : 'startDt'
            , btnEndId    : 'endDt'
            , defVal: fnGetToday()
            , defType : 'oneMonth'
        });

        fnTagMkRadio({
            id    : 'statusGubun',
            tagId : 'statusGubun',
            chkVal: '',
            data  : [
                { "CODE" : "ALL"	, "NAME":'전체' },
                { "CODE" : "ORDER"	, "NAME":'결제' },
                { "CODE" : "CLAIM"	, "NAME":'취소' }
            ],
            style : {}
        });
        fnTagMkRadio({
            id    : 'calculateObjectType',
            tagId : 'calculateObjectType',
            chkVal: '',
            data  : [
                { "CODE" : "ALL"	    ,   "NAME":'전체' },
                { "CODE" : "TARGET"	    ,   "NAME":'대상' },
                { "CODE" : "NON_TARGET",    "NAME":'비대상' }
            ],
            style : {}
        });
    };


	// --------------------------------- Button Start---------------------------------
	function fnInitButton(){
		$('#fnSearch', '#fnClear', '#fnExcelExport').kendoButton();
        $('#fnSearch', '#fnClear', '#fnExcelDetailExport').kendoButton();
	}
    function fnSearch(){
        $('#inputForm').formClear(false);
        var data = $('#searchForm').formSerialize(true);
        if(data.startDt.length == 8) {
            data.startDt = data.startDt.substr(0,4) + "-" + data.startDt.substr(4,2) + "-" + data.startDt.substr(6,2);
        }
        if(data.endDt.length == 8) {
            data.endDt = data.endDt.substr(0,4) + "-" + data.endDt.substr(4,2) + "-" + data.endDt.substr(6,2);
        }
        var query = {
            page         : 1,
            pageSize     : PAGE_SIZE,
            filterLength : fnSearchData(data).length,
            filter :  {
                filters : fnSearchData(data)
            }
        };
        aGridDs.query( query );
        // 불필요 제외..  박리나 확인.
        // try {
        //     fnTotalSearch();
        // } catch (e) {
        //     console.log(e);
        // }
    }
    function fnDefaultSet(){
        $("#statusGubun input:radio").eq(0).click();
        $("#calculateObjectType input:radio").eq(0).click();
        $('.k-pager-sizes').css('right', '315px');
        setDefaultDatePicker();
    }
    function setDefaultDatePicker() {
        var firstDay = fnGetMonthAdd(fnGetToday("yyyy-MM-01"),-1,'yyyy-MM-dd');
        var dayArray = firstDay.split("-");
        var lastDay = new Date(dayArray[0], dayArray[1], 0).oFormat("yyyy-MM-dd");
        $("#startDt").data("kendoDatePicker").value( firstDay ); // 시작 기본날짜
        $("#endDt").data("kendoDatePicker").value( lastDay ); // 종료 기본날짜
        $(".date-controller button[fb-btn-active=true]").attr("fb-btn-active", false);
    }

    // Total data 조회
    function fnTotalSearch(){
        var data = $('#searchForm').formSerialize(true);
        if(data.startDt.length == 8) {
            data.startDt = data.startDt.substr(0,4) + "-" + data.startDt.substr(4,2) + "-" + data.startDt.substr(6,2);
        }
        if(data.endDt.length == 8) {
            data.endDt = data.endDt.substr(0,4) + "-" + data.endDt.substr(4,2) + "-" + data.endDt.substr(6,2);
        }
        fnAjax({
            url     : '/admin/calculate/affiliate/getLinkPriceListTotal',
            params  : data,
            success :
                function( result ){
                    callBackTotalSearch(result);
                },
            isAction : 'select'
        });
    }

    // fnTotalSearch() 콜백 함수
    function callBackTotalSearch(result){
        console.log(result);
        if(result == null || result == undefined || result.totalResult == undefined) {
            return;
        }

        var obj = result.totalResult;
        var arrData = obj.split('___');
        if (arrData != null && arrData.length == 4) {
            console.log(arrData[0] + " , " + arrData[1] + " , " + arrData[2] + " , " + arrData[3]);
            $('#totOrdCnt').text(numberFormat(arrData[0]));
            $('#totOrdPrice').text(numberFormat(arrData[1]));
            $('#totCnclCnt').text(numberFormat(arrData[2]));
            $('#totCnclPrice').text(numberFormat(arrData[3]));
        }
    }

    // 천단위 (,) 로 표시
    function numberFormat(inputNumber) {
        if(inputNumber == null || inputNumber == undefined || inputNumber == '' || inputNumber == '0.0') {
            return '0';
        }
        return inputNumber.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }

    // --------------------------------- Button End---------------------------------

	// ------------------------------- Grid Start -------------------------------
    function fnInitGrid(){
        aGridDs = fnGetPagingDataSource({
            url      : "/admin/calculate/affiliate/getLinkPriceList",
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
                { title : 'No',width:'50px',attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
                ,{ field: 'ordNo'	,title : '주문번호'		, width:'150px',attributes:{ style:'text-align:center' }}
                ,{ field: 'lpinfo'	,title : 'LPINFO'		, width:'150px',attributes:{ style:'text-align:center' }}
                ,{ field: 'paidDt'	,title : '결제/입금일'		, width:'100px',attributes:{ style:'text-align:center' }}
                ,{ field: 'ordPrice', title: '총결제금액', width: '80px', attributes: {style: 'text-align:center'},
                    template: function (dataItem) {
                        return numberFormat(dataItem.ordTotalPrice);
                    }
                }
                ,{ field: 'ordPrice', title: '취소금액', width: '80px', attributes: {style: 'text-align:center'},
                    template: function (dataItem) {
                        return numberFormat(dataItem.cnclTotalPrice);
                    }
                }
                ,{ field: 'ordPrice', title: '사용적립금', width: '80px', attributes: {style: 'text-align:center'},
                    template: function (dataItem) {
                        return numberFormat(dataItem.ordPointPrice);
                    }
                }
            ]
        };

        aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

//		$("#aGrid").on("click", "tbody>tr", function () {
//			fnGridClick();
//		});

        aGrid.bind("dataBound", function() {
            var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
            $("#aGrid tbody > tr .row-number").each(function(index){
                $(this).html(row_num);
                row_num--;
            });

            $('#totalCnt').text(aGridDs._total);
            console.log(">>> aGridDs._total : " + aGridDs._total);
        });

        $("#aGrid").on("click", "tbody>tr>td", function () {
            var index = $(this).index();
        });

    }

    // 엑셀 다운로드
    function fnExcelExport(downType) {
        var data = $('#searchForm').formSerialize(true);
        if(data.startDt.length == 8) {
            data.startDt = data.startDt.substr(0,4) + "-" + data.startDt.substr(4,2) + "-" + data.startDt.substr(6,2);
        }
        if(data.endDt.length == 8) {
            data.endDt = data.endDt.substr(0,4) + "-" + data.endDt.substr(4,2) + "-" + data.endDt.substr(6,2);
        }
        data.downType = downType;
        fnExcelDownload('/admin/calculate/affiliate/getLinkPriceListExcel', data);
    }
	// ------------------------------- Grid End -------------------------------

	// ------------------------------- Html 버튼 바인딩 Start -------------------------------

    /** Common Search */
    $scope.fnSearch = function( ) {	fnSearch(); };
    /** Common Clear */
    $scope.fnClear =function(){	    fnDefaultSet();	};
    /** Common ExcelDownload */
    $scope.fnExcelExport = function( ){	fnExcelExport("NOR");};
    $scope.fnExcelDetailExport = function( ){	fnExcelExport("DTL");};

	// ------------------------------- Html 버튼 바인딩 End -------------------------------

}); // document ready - END
