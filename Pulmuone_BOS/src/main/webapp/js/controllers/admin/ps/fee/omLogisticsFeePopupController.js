
'use strict';

var paramData = parent.POP_PARAM["parameter"]; // 파라미터
var PAGE_SIZE = 5;
var UPDATE_BEFORE_START_DT = ""; // 수정 전 시작일자
var bGridDs, bGridOpt, bGrid;

$(document).ready(function() {

    importScript("/js/service/comm/searchCommItem.js", function (){
        fnInitialize();
    });

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit('fnIsMenu', { flag : false });

        fnPageInfo({
            PG_ID  : 'omLogisticsFeePopup',
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){

        fnInitButton();	  //Initialize Button  ---------------------------------
        fnInitOptionBox();//Initialize Option Box ----------------------------
        fnSearch();
        fnInitGrid();

    };

    //--------------------------------- Button Start---------------------------------
    //버튼 초기화
    function fnInitButton(){
        $('#fnSave, #fnClose').kendoButton();
    };

    function fnReadonlyCall() {
       $("#urWarehouseId").data("kendoDropDownList").readonly();
       $("#urSupplierId").data("kendoDropDownList").readonly();
    }

    function fnSearch(){
        $('#createInfoDiv').hide();
        UPDATE_BEFORE_START_DT = paramData.startDt.replace(/\-/g,'');
        if( paramData.omLogisticsFeeId != undefined && paramData.omLogisticsFeeId != "" ){
            fnAjax({
                url     : '/admin/ps/fee/getOmLogisticsFee',
                params  : {omLogisticsFeeId : paramData.omLogisticsFeeId},
                success :
                    function( data ){
                        $('#omLogisticsFeeForm').bindingForm(data, "row", true);
                        $('#createInfoDiv').show();

                        setTimeout(fnReadonlyCall,1000);

                        setTimeout(callGrid,1000);
                    },
                isAction : 'select'
            });
        }
    };

    function callGrid() {
        var param = $('#omLogisticsFeeForm').formSerialize(true);

        var query = {
            page: 1,
            pageSize: PAGE_SIZE,
            filterLength: fnSearchData(param).length,
            filter: {
                filters: fnSearchData(param)
            }
        };
        bGridDs.query(query);
    }


    function fnInitGrid(){
        bGridDs = fnGetPagingDataSource({
            url      : '/admin/ps/fee/getOmLogisticsFeeHistList',
            pageSize : PAGE_SIZE
        });

        bGridOpt = {
            dataSource: bGridDs
            ,   pageable  : {
                buttonCount : 10
               , responsive: false
            }
            ,   navigatable: true
            ,   columns   :  [
                { field : "rowNumber"				, title : "No"				, width: "20px"		    , attributes : {style : "text-align:center;"}
                    , template: function (dataItem){ return fnKendoGridPagenation(bGrid.dataSource, dataItem)}}
                , { field : "startDt"			    , title : "시작일자"			, width: "60px"		    , attributes : {style : "text-align:center;"} }
                , { field : "calcType"				, title : "정산방식"			, width: "60px"		    , attributes : {style : "text-align:center;"}
                    , template: function(dataItem){ return dataItem.calcType == "S" ? "판매가정산" : "공급가정산" ;}}
                , { field : "fee"	                , title : "수수료"			, width: "40px"		    , attributes : {style : "text-align:right;"}
                    , template: function(dataItem){ return dataItem.fee + "%"; }}
            ]
        };
        bGrid = $('#bGrid').initializeKendoGrid( bGridOpt ).cKendoGrid();

        bGrid.bind("dataBound", function() {
            //total count
            $('#totalCnt2').text(bGrid._total);
        })

    };

    // 팝업 닫기
    function fnClose(){
        parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
    };

    //저장
    function fnSave(){

        var data = $('#omLogisticsFeeForm').formSerialize(true);

        // 신규화면
        var url = '/admin/ps/fee/addOmLogisticsFee';
        var cbId = 'insert';

        var isInsertOrUpdate = false;
        // 수정화면
        if(OPER_TP_CODE == 'U'){
            // 이전 시작일자랑 수정 시작일자가 같은 경우는 수정
            if(UPDATE_BEFORE_START_DT == data.startDt) isInsertOrUpdate = true;
            // 이전 시작일자가 오늘일자보다 작거나 같을 경우는 저장
            else if(UPDATE_BEFORE_START_DT <= fnGetToday("yyyyMMdd")) isInsertOrUpdate = false;
            // 수정 시작일자가 오늘일자보다 큰 경우는 수정
            else if(data.startDt > fnGetToday("yyyyMMdd")) isInsertOrUpdate = true;
            // 나머지 경우는 전부 새로 저장
        }

        if(isInsertOrUpdate) {
            url = '/admin/ps/fee/putOmLogisticsFee';
            cbId = 'update';
        }

        if( data.rtnValid ) {
            fnAjax({
                url: url,
                params: data,
                contentType: "application/json",
                success:
                    function (data) {
                        fnBizCallback(cbId, data);
                    },
                isAction: 'insert'
            });
        }
    };

    //--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------

    //-------------------------------  Grid End  -------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------
    // 옵션 초기화
    function fnInitOptionBox(){

        // 촐고처
        //searchCommonUtil.getDropDownWarehouseStlmnList("urWarehouseId", "urWarehouseId", "선택해주세요.");
        fnKendoDropDownList({
            id : "urWarehouseId",
            tagId : "urWarehouseId",
            url : "/admin/comn/getDropDownWarehouseStlmnList",
            textField : "warehouseName",
            valueField : "warehouseId",
            blank : "선택해주세요.",
            async : false
        });

        // 공급업체
     //   searchCommonUtil.getSellersSupplier("urSupplierId", "urSupplierId", "선택해주세요.");
        fnKendoDropDownList({
            id: "urSupplierId",
            tagId: "urSupplierId",
            url: "/admin/comn/getDropDownSupplierListByWarehouseId",
            textField: "supplierName",
            valueField: "supplierId",
            blank: "선택해주세요.",
            async: false,
            cscdId: "urWarehouseId",
            cscdField: 'warehouseId'
        });




        // 정산방식
        fnKendoDropDownList({
            id: "calcType",
            data: [
                { "CODE" : "", "NAME" : "정산 방식" }
                , { "CODE" : "S", "NAME" : "판매가 정산" }
                , { "CODE" : "B", "NAME" : "공급가 정산" }
            ],
            valueField: "CODE",
            textField: "NAME"
        });

        // 시작일자
        fnKendoDatePicker({
            id: "startDt",
            format: "yyyy-MM-dd"
        });
    };

    //---------------Initialize Option Box End ------------------------------------------------
    //-------------------------------  Common Function start -------------------------------

    /**
     * 콜백합수
     */
    function fnBizCallback( id, data ){
        switch(id){
            case 'insert':
                fnKendoMessage({
                    message:"저장이 완료되었습니다.",
                    ok:function(){
                        fnClose();
                    }
                });
                break;
            case 'update':
                UPDATE_BEFORE_START_DT = "";
                fnKendoMessage({
                    message:"저장이 완료되었습니다.",
                    ok:function(){
                        fnClose();
                    }
                });
                break;
        }
    };

    //-------------------------------  Common Function end -------------------------------

    //------------------------------- Html 버튼 바인딩  Start -------------------------------
    /** Common Save*/
    $scope.fnSave = function(){	 fnSave();};   // 저장
    /** Common Close*/
    $scope.fnClose = function(){ fnClose(); }; // 닫기
    //------------------------------- Html 버튼 바인딩  End -------------------------------

    fnInputValidationByRegexp("fee", /[^0-9]/g);

}); // document ready - END
