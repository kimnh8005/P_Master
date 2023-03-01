/**-----------------------------------------------------------------------------
 * description 		 : 기초설정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.08.20		강윤경          최초생성
 * @
 * **/
'use strict';


$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'psUserBasic',
			callback : fnUI
		});
	}


	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitMaskTextBox();

		fnSearch();
	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnSave').kendoButton();
	}
	function fnSearch(){

		fnAjax({
			url     : '/admin/ur/psUserBasic/getPsUserBasic',
			params  : null,
			success :
				function( data ){
					fnBizCallback('select', data);
				},
			isAction : 'batch'
		});
	}


	//뒤로
	function fnBack(){
		history.back();
	}

	//저장
	function fnSave(){
//		var url  = '/admin/st/basic/putEnvironmentEnvVal';
		var url  = '/admin/ur/psUserBasic/putPsUserBasic';
		var cbId = 'insert';

		var data = $('#inputForm').formSerialize(true);

		if(data.rtnValid ){
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

	//--------------------------------- Button End---------------------------------

	function fnInitMaskTextBox() {
		$('#urLoginFailCount, #urPwCycleDay')
		.forbizMaskTextBox({fn:'onlyNum'});
	}

	//-------------------------------  Common Function start -------------------------------

	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding

				if(data.getEnvironmentListResultVo[0].environmentKey == 'UR_PW_CYCLE_DAY') {
					$("#urPwCycleDay").val(data.getEnvironmentListResultVo[0].environmentValue);
					$("#urPwCycleDayStEnvId").val(data.getEnvironmentListResultVo[0].stEnvId);
					$("#urLoginFailCount").val(data.getEnvironmentListResultVo[1].environmentValue);
					$("#urLoginFailCountStEnvId").val(data.getEnvironmentListResultVo[1].stEnvId);
				} else {
					$("#urPwCycleDay").val(data.getEnvironmentListResultVo[1].environmentValue);
					$("#urPwCycleDayStEnvId").val(data.getEnvironmentListResultVo[1].stEnvId);
					$("#urLoginFailCount").val(data.getEnvironmentListResultVo[0].environmentValue);
					$("#urLoginFailCountStEnvId").val(data.getEnvironmentListResultVo[0].stEnvId);
				}
				break;

			case 'insert':
				fnKendoMessage({
					message:"저장이 완료되었습니다.",
					ok:function(){
						fnSearch();
						fnClose();
					}
				});
				break;
		}
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common New*/
	$scope.fnBack = function( ){	fnBack();};
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//fnInputValidationForHangulSpace("reasonName");

}); // document ready - END
