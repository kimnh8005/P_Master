/**-----------------------------------------------------------------------------
 * description 		 : 회원 탈퇴사유 신규등록/수정 팝업
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.10.21     최윤지          최초생성
 * **/
'use strict';

var paramData = parent.POP_PARAM["parameter"]; // 파라미터

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'moveReasonPopup',
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){

		fnInitButton();	  //Initialize Button  ---------------------------------
		fnInitOptionBox();//Initialize Option Box ----------------------------
		fnSearch();

	};

	//--------------------------------- Button Start---------------------------------
    //버튼 초기화
    function fnInitButton(){
        $('#fnSave, #fnClose').kendoButton();
    };

	//회원 탈퇴사유 상세조회
	function fnSearch(){
	    if( paramData.urMoveReasonId != "" ){
	        fnAjax({
	            url     : '/admin/user/movereason/getMoveReason',
	            params  : {urMoveReasonId : paramData.urMoveReasonId},
	            success :
	                function( data ){
	                    $('#inputForm').bindingForm(data, "moveReasonResultVo", true);
	                },
	            isAction : 'select'
	        });
	    }
	};

	// 팝업 닫기
    function fnClose(){
        parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
    };

	//저장
	function fnSave(){

	    var data = $('#inputForm').formSerialize(true);

		var url  = '/admin/user/movereason/addMoveReason';
		var cbId = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/user/movereason/putMoveReason';
			cbId= 'update';
		}


		if( data.rtnValid ){
			if(data.useYn == '') {
				fnKendoMessage({message:"사용여부를 선택해주세요."});
				return;
			}
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
	};

	//--------------------------------- Button End---------------------------------

	//------------------------------- Grid Start -------------------------------

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	// 옵션 초기화
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

		// 사용여부 라디오버튼
		fnTagMkRadio({
			id    :  'useYn',
			tagId : 'useYn',
			data  : [	{ "CODE" : "Y" , "NAME":"예"}
					, 	{ "CODE" : "N" , "NAME":"아니요"}],
			chkVal: "",
			style : {},
            async : false

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

	fnInputValidationForHangulSpace("reasonName");

}); // document ready - END
