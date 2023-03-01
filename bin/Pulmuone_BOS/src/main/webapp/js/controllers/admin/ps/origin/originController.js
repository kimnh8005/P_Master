﻿﻿/**-----------------------------------------------------------------------------
 * description       : 원산지 목록
 * @
 * @ 수정일            수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.06.30     박영후          최초생성
 * @ 2020.10.28     최윤지          NEW 변경
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
            PG_ID  : 'psOrigin',
            callback : fnUI
        });

    }

    // 화면 UI 초기화
    function fnUI(){

        fnInitButton(); //Initialize Button  ---------------------------------
        fnInitGrid();   //Initialize Grid ------------------------------------
        fnInitMaskTextBox();  // Initialize Input Mask ------------------------------------
        fnInitOptionBox();//Initialize Option Box ------------------------------------
        fnSearch();

    }

    //--------------------------------- Button Start---------------------------------
    // 버튼 초기화
    function fnInitButton(){
        $('#fnSearch, #fnNew,#fnSave,  #fnClear, #fnExcelExport').kendoButton();
        //$('#fnDel').kendoButton({ enable: false });
    }
    // 조회
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

    function fnExcelExport(){
        var data = $('#searchForm').formSerialize(true);
        fnExcelDownload("/admin/policy/origin/getOriginListExportExcel", data);
    }

    // 초기화
    function fnClear(){
        $('#searchForm').formClear(true);
        $("input[name=originTypes]").eq(0).prop("checked", true).trigger("change"); // 원산지 구분 전체선택
    }
    // 팝업 초기화
    function fnClearInput() {
        var data = $('#inputForm').data("data");
        if (data)
            $('#inputForm').bindingForm(data, 'rows', true);
        else
            $('#inputForm').formClear(true);
    }

    // 신규
    function fnNew(){
        //aGrid.clearSelection();
        $("#originType").data("kendoDropDownList").enable(true);
        $("#originCode").attr("disabled", false);

        $('#inputForm').formClear(true);
        $('#inputForm').data("data", "");
        fnKendoInputPoup({height:"180px" ,width:"640px", title:{ nullMsg :'원산지 등록' } });
    }

    // 저장
    function fnSave(){
        var url  = '/admin/policy/origin/addOrigin';
        var cbId = 'insert';

        if( OPER_TP_CODE == 'U' ){
            url  = '/admin/policy/origin/putOrigin';
            cbId= 'update';
        }
        var data = $('#inputForm').formSerialize(true);

        if( data.rtnValid ){
            fnAjax({
                url     : url,
                params  : data,
                success :
                    function( data ){
                        fnBizCallback(cbId, data);
                    },
                    isAction : 'batch'
            });
        }
    }

    // 삭제
    function fnDel(e){
        e.preventDefault();
        e.stopPropagation();

        var dataItem = this.dataItem($(e.currentTarget).closest("tr"));

        fnKendoMessage({message : '삭제하시겠습니까?', type : "confirm", ok : function(){
            fnAjax({
                url     : '/admin/policy/origin/delOrigin',
                params  : {systemCommonCodeId : dataItem.systemCommonCodeId},
                success :
                    function( data ){
                        fnBizCallback("delete",data);
                    },
                isAction : 'delete'
            });
        }});
    }

    // 닫기
    function fnClose(){
        var kendoWindow =$('#kendoPopup').data('kendoWindow');
        kendoWindow.close();
    }

    //--------------------------------- Button End---------------------------------


    //------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid(){
        aGridDs = fnGetPagingDataSource({
            url      : '/admin/policy/origin/getOriginList',
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
                { field:'originType'            ,title : '원산지 구분'       , width:'100px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'originCode'           ,title : '원산지 코드'       , width:'200px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'originName'           ,title : '원산지명'     , width:'300px' ,attributes:{ style:'text-align:left' }}
                ,{ command: [{ text: "삭제", click: fnDel, className:"btn-red btn-s", visible: function() { return fnIsProgramAuth("SAVE")} }]
            		, title : '관리'           , width:'80px' ,attributes:{ style:'text-align:center', class:'forbiz-cell-readonly' }}
                ,{ field:'systemCommonCodeId'   ,hidden: true}
            ]
        };

        aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
            $("#aGrid").on("click", "tbody>tr", function () {
                fnGridClick();
        });

        // 총 건수
        aGrid.bind("dataBound", function() {
            //total count
            $('#countTotalSpan').text(aGridDs._total);
        });
    }

    // 그리드 클릭
    function fnGridClick(){
        $("#originType").data("kendoDropDownList").enable(false);
        $("#originCode").attr("disabled", true);

        var aMap = aGrid.dataItem(aGrid.select());
        fnAjax({
            url     : '/admin/policy/origin/getOrigin',
            params  : {systemCommonCodeId : aMap.systemCommonCodeId},
            success :
                function( data ){
                    fnBizCallback("select",data);
                },
            isAction : 'select'
        });
    };

    //-------------------------------  Grid End  -------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------
    function fnInitMaskTextBox() {
        $("#originCode").on("keyup", function () { this.value = this.value.toUpperCase() });
        fnInputValidationForAlphabetNumberUnderbar("originCode");
        fnInputValidationForHangulAlphabetNumber("originName");
    }

    //---------------Initialize Option Box Start ------------------------------------------------
    function fnInitOptionBox(){

        // 원산지 목록 체크박스
        fnTagMkChkBox({
            id    : "originTypes",
            url : "/admin/policy/origin/getOriginTypeList",
            tagId : 'originTypes',
            async : false,
            chkVal: '',
            style : {},
            textField :"originTypeName",
            valueField : "originTypeCode",
            beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }]
        });
        fbCheckboxChange();

        $("input[name=originTypes]").eq(0).prop("checked", true).trigger("change"); // 원산지 구분 전체선택

        // 검색어 입력 드롭다운 리스트
        fnKendoDropDownList({
            id    : 'condiType',
            data  : [
                {"CODE":"ORIGIN_NAME"   ,"NAME":"원산지명"},
                {"CODE":"ORIGIN_CODE"   ,"NAME":"원산지코드"}
            ],
            textField :"NAME",
            valueField : "CODE"
        });

        // 국외만 관리하므로 아래와 같이 드롭다운 수정함
        var originTypeData = [
            {"CODE":"OVERSEAS"      ,"NAME": '국외'},
        ];
        fnKendoDropDownList({ id : 'originType' , data : originTypeData });

        // 국내, 기타 관리시 아래 코드로 수정 필요
//      fnKendoDropDownList({
//          id    : 'originType',
//          url : "/admin/ps/origin/getOriginTypeList",
//          textField :"originTypeName",
//          valueField : "originTypeCode"
//      });


        $('#kendoPopup').kendoWindow({
            visible: false,
            modal: true
        });
    }
    //---------------Initialize Option Box End ------------------------------------------------
    //-------------------------------  Common Function start -------------------------------

    /**
    * 콜백합수
    */
    function fnBizCallback( id, data ){
        switch(id){
            case 'select':
                //form data binding
                $('#inputForm').data("data", data);
                $('#inputForm').bindingForm(data, 'rows', true);
                fnKendoInputPoup({height:"180px" ,width:"640px",title:{nullMsg :'원산지 수정'} });
                break;
            case 'insert':
                fnSearch();
                $('#kendoPopup').data('kendoWindow').close();
                fnKendoMessage({message : '저장되었습니다.'});
                break;
            case 'update':
                aGridDs.query();
                fnKendoMessage({message : '수정되었습니다.'});
                fnClose();
                break;
            case 'delete':
                aGridDs.query();
                fnClose();
                fnKendoMessage({message : '삭제되었습니다.'});
                break;

        }
    }

    // 표준용어 팝업
    function fnPopupDicButton(){
        fnKendoPopup({
                id     : 'DIC_POP',
                title  : '표준용어 팝업',
                width  : '900px',
                height : '600px',
                src    : '#/dicMstPopup',
                param  : { "MENU_TYPE" : "word"},
                success: function( id, data ){
                    if(data.id){
                        $('#gbDictionaryMasterId').val(data.id);
                        $('#originName').val(data.baseName);
                    }
                }
            });
    };
    //-------------------------------  Common Function end -------------------------------


    //------------------------------- Html 버튼 바인딩  Start -------------------------------
    /** Common Search*/
    $scope.fnSearch = function( ) { fnSearch(); }; // 조회
    /** Common Clear*/
    $scope.fnClear =function(){  fnClear(); }; // 초기화
    /** Common New*/
    $scope.fnNew = function( ){ fnNew();}; // 신규
    /** Common Save*/
    $scope.fnSave = function(){  fnSave();}; // 저장
    /** Common Delete*/
    $scope.fnDel = function(){   fnDel();}; // 삭제
    /** Common Close*/
    $scope.fnClose = function( ){  fnClose();}; // 닫기
    /** input Clear */
    $scope.fnClearInput = function( ){  fnClearInput();}; // 팝업 초기화
    /** dictionary poptup */
    $scope.fnPopupDicButton = function( ){  fnPopupDicButton();}; // 표준용어 팝업
    $scope.fnExcelExport = function() { fnExcelExport(); };
    //------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
