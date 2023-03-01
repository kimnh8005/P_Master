/**-----------------------------------------------------------------------------
 * description 		 : BOS 접근가능 IP 설정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.06.29		오영민          최초생성
 * @ 2020.10.21     최성현          리펙토링
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
$(document).ready(function() {
    fnInitialize();	//Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit('fnIsMenu', { flag : 'true' });
        fnPageInfo({
            PG_ID  : 'accessibleIp',
            callback : fnUI
        });
    }

    //화면 UI 초기화
    function fnUI(){

        fnTranslate();	// 다국어 변환--------------------------------------------

        fnInitButton();	//Initialize Button  ---------------------------------

        fnInitGrid();	//Initialize Grid ------------------------------------

        fnInitOptionBox();//Initialize Option Box ------------------------------------

        fnSearch();

        bindFormEvent();

    }

    //--------------------------------- Button Start---------------------------------
    // 버튼 초기화
    function fnInitButton(){
        $('#fnSearch, #fnNew, #fnSave,  #fnClear').kendoButton();
    }

    //조회
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
        aGridDs.query(query);
    }

    function onSubmit(e) {
        e.preventDefault();
    }

    function bindFormEvent(){
        $("#searchForm").on("submit", onSubmit);
    }

    //초기화
    function fnClear(){
        $('#searchForm').formClear(true);
    }

    //신규 추가
    function fnNew(){
        aGrid.clearSelection();
        $('#inputForm').formClear(true);
        inputFocus();
    }

    //저장
    function fnSave(){
        var changeCnt =0;
        var insertData = fnEGridDsExtract('aGrid', 'insert');
        var updateData = fnEGridDsExtract('aGrid', 'update');
        var deleteData = fnEGridDsExtract('aGrid', 'delete');
        changeCnt = insertData.length + updateData.length +deleteData.length ;

        for(let i=0; i<insertData.length; i++){
            if(insertData[i].ipAddress==""){
                fnKendoMessage({message : aGridOpt.columns[0].title + " 필수 값을 입력해주세요."});
                return;
            } else if(insertData[i].startApplyDate=="") {
                fnKendoMessage({message : aGridOpt.columns[1].title + " 필수 값을 입력해주세요."});
                return;
            } else if(insertData[i].endApplyDate=="") {
                fnKendoMessage({message : aGridOpt.columns[2].title + " 필수 값을 입력해주세요."});
                return;
            }
        }
        for(let i=0; i<updateData.length; i++){
            if(updateData[i].ipAddress==""){
                fnKendoMessage({message : aGridOpt.columns[0].title + " 필수 값을 입력해주세요."});
                return;
            } else if(updateData[i].startApplyDate=="") {
                fnKendoMessage({message : aGridOpt.columns[1].title + " 필수 값을 입력해주세요."});
                return;
            } else if(updateData[i].endApplyDate=="") {
                fnKendoMessage({message : aGridOpt.columns[2].title + " 필수 값을 입력해주세요."});
                return;
            }
        }

        if(changeCnt ==0){
            fnKendoMessage({message :  '데이터 변경사항이 없습니다.'});
            return ;
        }else{
            var data = {"insertData" : kendo.stringify(insertData)
                ,"updateData" : kendo.stringify(updateData)
                ,"deleteData" : kendo.stringify(deleteData)
            };
            fnAjax({
                url     : '/admin/policy/accessibleIp/savePolicyAccessibleIp',
                params  : data,
                refreshGrid : aGridDs,
                success :
                    function( data ){
                        fnBizCallback("save", data);
                    },
                isAction : 'save'
            });
        }

    }
    //--------------------------------- Button End---------------------------------


    //------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid(){
        aGridDs = fnGetEditPagingDataSource({
            url      : '/admin/policy/accessibleIp/getPolicyAccessibleIpList',
            pageSize       : PAGE_SIZE,
            model_id     : 'psAccessibleIpId',
            model_fields : {
                ipAddress			: { editable: true	, type: 'string'
                    , validation: { required: true
                        ,ipAddressvalidation: function (input) {
                            console.log('ipAddressvalidation', input);
                            if (input.is("[name='ipAddress']") && input.val() != "") {
                                input.attr("data-ipAddressvalidation-msg", "유효한 IP 값이 아닙니다.");
                                return fnValidateIPaddress(input.val());
                            }
                            return true;
                        }
                    }
                }
                ,startApplyDate		: { editable: true	, type: 'string'
                }
                ,endApplyDate		: { editable: true	, type: 'string'
                }
            }
        });

        aGridOpt = {
            dataSource: aGridDs,
            toolbar   :
            fnIsProgramAuth("ADD") && !fnIsProgramAuth("SAVE_DELETE") ? [{ name: 'create', text: '신규 추가', className: "btn-point btn-m"}]:
            fnIsProgramAuth("SAVE_DELETE") && !fnIsProgramAuth("ADD") ? [{ name: 'cSave',  text: '저장', imageClass: "k-i-update", className: "k-custom-save btn-point btn-m", iconClass: "k-icon" }]:
            fnIsProgramAuth("ADD") && fnIsProgramAuth("SAVE_DELETE") ? [{ name: 'create', text: '신규 추가', className: "btn-point btn-m"},
                                                                                          { name: 'cSave',  text: '저장', imageClass: "k-i-update", className: "k-custom-save btn-point btn-m", iconClass: "k-icon" }]:[],
            pageable :	{
                refresh: true
                ,pageSizes: [20, 30, 50]
                ,buttonCount: 10
            },
            editable:{confirmation: function(model) {
                    return '삭제하시겠습니까?\n저장버튼을 클릭해야 반영이 완료됩니다.'
                }},
            //height: 550,
            columns   : [
                { field:'ipAddress'				,title : 'IP 입력'		, width:'160px',attributes:{"class" : "#=(psAccessibleIpId)?'existRow':''#", style:'text-align:center', required: "required" } }
                ,{ field:'startApplyDate'		,title : '시작 적용 기간'		, width:'160px',attributes:{"class" : "#=(psAccessibleIpId)?'existRow':''#", style:'text-align:center' }
                    ,editor: dateTimeEditor, format:"{0:yyyy-MM-dd HH:mm}"
                }
                ,{ field:'endApplyDate'			,title : '종료 적용 기간'		, width:'160px',attributes:{"class" : "#=(psAccessibleIpId)?'existRow':''#", style:'text-align:center' }
                    ,editor: dateTimeEditor, format:"{0:yyyy-MM-dd HH:mm}"
                }
                ,{ command: [{name:'destroy'	,text:'삭제', className: 'btn-red btn-s',   visible: function() { return fnIsProgramAuth("SAVE_DELETE") }}], title : '관리'        	, width: '120px', attributes:{ style:'text-align:center'  , class:"forbiz-cell-readonly #=(psAccessibleIpId)?'existRow':''#" }}
                ,{ field:'psAccessibleIpId'  	,hidden: true }
            ]
        };
        aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

        $(".k-custom-save").click(function(e){
            e.preventDefault();
            fnSave();
        });

        $(".k-grid-toolbar").addClass("k-align-with-pager-sizes");

        function dateTimeEditor(container, options) {
            let minDate;
            let maxDate;
            if(options.field == 'startApplyDate') {
                minDate = new Date(1900, 0, 1)
                if(options.model.endApplyDate != "") {
                    maxDate = options.model.endApplyDate
                }
            } else {
                maxDate = new Date(2100, 0, 1)
                if(options.model.startApplyDate != "") {
                    minDate = options.model.startApplyDate
                }
            }
            $('<input required class="sedate"  data-text-field="' + options.field + '" data-value-field="' + options.field + '" data-bind="value:' + options.field + '" data-format="' + options.format + '"/>')
                .appendTo(container)
                .kendoDateTimePicker({
                    format: "0:yyyy-MM-dd HH:mm"
                    ,min : minDate
                    ,max : maxDate
                    ,change: function(e){

                        if(options.field == 'startApplyDate'){
                            options.model.startApplyDate = kendo.toString(this._oldText, '0:yyyy-MM-dd HH:mm');
                            options.model.dirty = true;
                        }else if(options.field == 'endApplyDate'){
                            options.model.endApplyDate = kendo.toString(this._oldText, '0:yyyy-MM-dd HH:mm');
                            options.model.dirty = true;
                        }

                    }
                });

            $(".sedate").attr("readonly", "readonly");
        }
        // 총 개수
        aGrid.bind("dataBound", function () {
            //total count
            $('#countTotalSpan').text(kendo.toString(aGridDs._total, "n0"));
        });

    }
    //-------------------------------  Grid End  -------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------
    function fnInitOptionBox(){
    }
    //---------------Initialize Option Box End ------------------------------------------------
    //-------------------------------  Common Function start -------------------------------
    function fnValidateIPaddress(ipAddress){
        return /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(ipAddress);
    }

    function inputFocus(){
        $('#input1').focus();
    };

    /**
     * 콜백합수
     */
    function fnBizCallback( id, data ){
        switch(id){
            case 'save':
                fnSearch();
                fnKendoMessage({message : '저장되었습니다.'});
                break;
        }
    }

    //-------------------------------  Common Function end -------------------------------


    //------------------------------- Html 버튼 바인딩  Start -------------------------------
    /** Common Search*/
    $scope.fnSearch = function( ) {	fnSearch();	}; // 조회
    /** Common Clear*/
    $scope.fnClear =function(){	 fnClear();	}; // 초기화
    /** Common New*/
    $scope.fnNew = function( ){	fnNew();}; // 생성
    /** Common Save*/
    $scope.fnSave = function(){	 fnSave();}; // 저장
    /** Common Delete*/
    $scope.fnDel = function(){	 fnDel();}; // 삭제
    //------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
