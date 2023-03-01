/**-----------------------------------------------------------------------------
 * description       : 탈퇴사유 분류설정
 * @
 * @ 수정일            수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.06.19     오영민          최초생성
 * @ 2020.08.19     강윤경          화면설계서 부분 반영
 * @ 2020.10.21     최윤지          New 변경
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
    fnInitialize(); //Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit('fnIsMenu', { flag : 'true' });

        fnPageInfo({
            PG_ID  : 'moveReason',
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){

        fnInitButton(); //Initialize Button  ---------------------------------
        fnInitGrid();   //Initialize Grid ------------------------------------
        fnSearch();

    };

    //--------------------------------- Button Start---------------------------------
    // 버튼 초기화
    function fnInitButton(){
        $('#fnSearch, #fnNew, #fnSave').kendoButton();
    };

    // 조회
    function fnSearch(){
        $('#inputForm').formClear(true);

        var data;
        data = $('#searchForm').formSerialize(true);
        var query = {
                    page         : 1,
                    pageSize     : PAGE_SIZE,
                    filterLength : fnSearchData(data).length,
                    filter :  {
                        filters : fnSearchData(data)
                    }
        };
        aGridDs.query( query );
        //  aGridDs.read(data);
    };

    //신규등록
    function fnNew(){
        fnMoveReasonPopup();
    };

    //탈퇴사유 팝업
    function fnMoveReasonPopup(params){
        if( params == undefined ){
            params = { urMoveReasonId : "" };
        }

        fnKendoPopup({
            id     : 'moveReasonPopup',
            title  : '탈퇴사유 등록/수정',
            src    : '#/moveReasonPopup',
            param  : params,
            width  : '600px',
            height : '305px',
            success: function( id, data ){
                fnSearch();
            }
        });
    };

    //--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid(){
        aGridDs = fnGetPagingDataSource({
            url      : '/admin/user/movereason/getMoveReasonList',
            pageSize : PAGE_SIZE
        });

        aGridOpt = {
                dataSource: aGridDs
            ,   pageable  : {
                pageSizes: [20, 30, 50],
                buttonCount : 10
            }
            ,   navigatable: true
            ,   columns   : [
                     { field:'rnum' ,hidden:true}
                    ,{ field:'urMoveReasonId'   ,title : '사유코드'     , width:'30px'  ,attributes:{ style:'text-align:center' }}
                    ,{ field:'reasonName'       ,title : '탈퇴사유 명'       , width:'150px' ,attributes:{ style:'text-align:center' }}
                    ,{ field:'useYn'            ,title : '사용여부'     , width:'30px'  ,attributes:{ style:'text-align:center' }
                        ,template : "#=(useYn=='Y')?'예':'아니요'#"}
                    , { field : 'edit', title : "관리", width : "30px", attributes : {style : "text-align:center"},
                        template : function(dataItem){
                            return "<a href='#' role='button' class='k-button k-button-icontext' kind='modify'>수정</a>";
                        }
                      }
                ]
            };
        aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

        //총 건수
        aGrid.bind("dataBound", function() {
            //total count
            $('#countTotalSpan').text(aGridDs._total);
        });

        // 그리드 Row 클릭
        $($("#aGrid").data("kendoGrid").tbody).on("click", "[kind]", function(e) {
            e.preventDefault();
            let dataItem = aGrid.dataItem($(e.target).closest('tr'));
            let param = {};
            param.urMoveReasonId = dataItem.urMoveReasonId;

            fnMoveReasonPopup(param);
        });
    };

    //-------------------------------  Grid End  -------------------------------

    //------------------------------- Html 버튼 바인딩  Start -------------------------------
    /** Common Search*/
    $scope.fnSearch = function( ) { fnSearch(); }; // 조회
    /** Common New*/
    $scope.fnNew = function( ){ fnNew();}; // 신규
    //------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END