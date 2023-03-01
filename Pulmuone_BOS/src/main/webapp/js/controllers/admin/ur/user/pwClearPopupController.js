﻿﻿﻿
/**-----------------------------------------------------------------------------
 * system 			 : 사용자관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2017.01.10		최봉석          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

var paramData ;
if(parent.POP_PARAM['parameter']){
	paramData = parent.POP_PARAM['parameter'];
}

$(document).ready(function() {
    fnInitialize(); //Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize() {
        $scope.$emit('fnIsMenu', { flag: false });

        fnPageInfo({
            PG_ID: 'pwClearPopup',
            callback: fnUI
        });
        /*$('div#ng-view').css('width', '1300px');*/
    }

    function fnUI() {

        fnInitButton(); //Initialize Button  ---------------------------------

        fnInitGrid(); //Initialize Grid ------------------------------------

        fnInitOptionBox(); //Initialize Option Box ------------------------------------


    }

    //--------------------------------- Button Start---------------------------------
    function fnInitButton() {
        $('#fnSearch, #fnNew,#fnSave,  #fnClear,#fnPwdClear,#fnPwdClear2').kendoButton();
    }

	function fnClose(params){
		if(params){
			parent.POP_PARAM = params;
		}
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	}

    //--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------
    function fnInitGrid() {
    	$('#inputForm').bindingForm(paramData, 'data', true);
    }

    //-------------------------------  Grid End  -------------------------------

    //-------------------------------  Common Function start -------------------------------

    function fnInitOptionBox() {
		fnTagMkChkBox({
			id    : 'putPasswordType'
			,data  : [{ "CODE" : "SMS" 	 , "NAME":'SMS'}
					 ,{ "CODE" : "EMAIL" , "NAME":'EMAIL'}]
			,tagId : 'putPasswordType'
			,style : {}
		});

		$("input:checkbox[name='putPasswordType']").prop('checked', true);

    };

    function fnPwdClear2() {

    	var data = $('#inputForm').formSerialize(true);

    	if(data.putPasswordType == ""){
    		return fnKendoMessage({ message: fnGetLangData({ nullMsg: '임시 비밀번호 받는 곳을 체크해주세요.' })});
    	}

		fnAjax({
			url: '/admin/ur/user/putPasswordClear',
			params: data,
			success: function(data) {
				fnKendoMessage({ message: fnGetLangData({ nullMsg: '패스워드가 재발송되었습니다.' }), ok: fnClose });
			},
			isAction: 'update'
		});

    }

    /**
     * 콜백합수
     */
    function fnBizCallback(id, data) {
        switch (id) {
            case 'select':
                //form data binding
                $('#inputForm').bindingForm(data, 'rows', true);
                break;
            case 'insert':
                if (data.rows == "DUP_DATA") {
                    fnKendoMessage({ message: fnGetLangData({ key: "370", nullMsg: '중복입니다.' }), ok: inputFocus });
                } else {
                    aGridDs.insert(data.rows);
                    fnKendoMessage({ message: fnGetLangData({ key: "369", nullMsg: '입력되었습니다.' }), ok: fnNew });
                }
                break;
            case 'save':
                fnKendoMessage({ message: fnGetLangData({ key: "368", nullMsg: '저장되었습니다.' }) });
                break;
            case 'update':
                fnUpdateGrid(data, $("#aGrid"), "rows");
                fnKendoMessage({ message: fnGetLangData({ key: "367", nullMsg: '수정되었습니다.' }) });
                break;
            case 'delete':
                aGridDs.remove(data);
                fnNew();
                fnKendoMessage({ message: fnGetLangData({ key: "366", nullMsg: '삭제되었습니다.' }) });
                break;

        }
    }

    //-------------------------------  Common Function end -------------------------------


    //------------------------------- Html 버튼 바인딩  Start -------------------------------
    /** Common Search*/
    $scope.fnSearch = function() { fnSearch(); };
    /** Common Clear*/
    $scope.fnClear = function() { fnClear(); };
    /** Common New*/
    $scope.fnNew = function() { fnNew(); };
    /** Common Save*/
    $scope.fnSave = function() { fnSave(); };
    /** Common Delete*/
    $scope.fnDel = function() { fnDel(); };
    /** Common Close*/
    $scope.fnClose = function() { fnClose(); };

    $scope.fnPopupButton = function(data) { fnPopupButton(data); };

    $scope.fnPwdClear = function() { fnPwdClear(); };

    $scope.fnPwdClear2 = function() { fnPwdClear2(); };

    //------------------------------- Html 버튼 바인딩  End -------------------------------
    //
}); // document ready - END