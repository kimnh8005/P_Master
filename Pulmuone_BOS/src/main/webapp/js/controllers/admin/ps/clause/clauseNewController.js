/**-----------------------------------------------------------------------------
 * description 		 : 정책관리 - 약관관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2016.12.26		최봉석          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var bGridDs, bGridOpt, bGrid;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'clauseNew',
			callback : fnUI
		});

	}

	function fnUI(){
		fnDefaultValue();

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		$('#content').focus();
	}

	//--------------------------------- Button Start---------------------------------
	function fnDefaultValue(){
		$('#psClauseGroupCd').val(parent.POP_PARAM["parameter"].psClauseGroupCd);
		$('#clauseGroupName').val(parent.POP_PARAM["parameter"].clauseGroupName);
		$('#content').val(fnTagConvert(parent.POP_PARAM["parameter"].originContent));
		$('#clauseInfo').val(fnTagConvert(parent.POP_PARAM["parameter"].originClauseInfo));
		$('#originContent').val(parent.POP_PARAM["parameter"].originContent);
		$('#originClauseInfo').val(parent.POP_PARAM["parameter"].originClauseInfo);
		$('#psClauseId').val(parent.POP_PARAM["parameter"].psClauseId);
		$('#inputPsClauseGroupCd').val(parent.POP_PARAM["parameter"].psClauseGroupCd);

		var data = $('#searchForm').formSerialize(true);
		fnAjax({
			url     : '/admin/policy/clause/getClauseModifyView',
			params  : data,
			success :
				function( data ){
					$("#createUserInfo").html(fnNvl(data.rows.createUserInfo));
					$("#modifyUserInfo").html(fnNvl(data.rows.modifyUserInfo));
					var mandatory = document.getElementById("mandatory");

					if(data.rows.mandatoryYn == 'Y'){
						$('input:checkbox[name="mandatoryYn"]').prop("checked", true);
						$("#mandatoryYn").val('Y');
						mandatory.style.display = '';
				    	$("#clauseInfo").attr("required", true);
					}else{

						$("input:checkbox[name='mandatoryYn']").prop('checked', false);
						$("#mandatoryYn").val('N');
						mandatory.style.display = 'none';
				    	$("#clauseInfo").attr("required", false);
					}

					fnBizCallback('select', data);
				},
			isAction : 'select'
		});
	}

	function fnInitButton(){
		$('#fnSearch, #fnNew,#fnSave,  #fnClear, #fnClose').kendoButton();
		$('#fnDel').kendoButton({ enable: false });
	}

	function fnSearch(){
		var data = $('#searchForm').formSerialize(true);
		aGridDs.read(data);
	}

	function fnClear(){
		$('#inputForm').formClear(true);

		$('#psClauseGroupCd').val(parent.POP_PARAM["parameter"].psClauseGroupCd);
		$('#clauseGroupName').val(parent.POP_PARAM["parameter"].clauseGroupName);

		$('input:checkbox[id="mandatoryYn"]').is(":checked");
		var originContent = $('#originContent').val();
		var originclauseInfo = $('#originClauseInfo').val();
		$('#content').val(originContent);
		$('#clauseInfo').val();
	}
	function fnSave(){
		var url  = '/admin/policy/clause/addClause';
		var cbId = 'insert';

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
	function fnClose(){
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	}

	//--------------------------------- Button End---------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

		fnTagMkRadioYN({id: "intputActive" , tagId : "USE_YN",chkVal: 'Y'});

		fnKendoEditor( {id : 'content'} );
		fnKendoEditor( {id : 'clauseInfo'} );

		let defaultDate = new Date()
		defaultDate.setDate(defaultDate.getDate() + 1)

		fnKendoDatePicker({
		    id    : 'executeDate',
		    format: 'yyyy-MM-dd',
		    defVal: defaultDate,
		    min: defaultDate,
		});

		fnTagMkChkBox({
            id : "mandatoryYn",
            data : [ { "CODE" : "", "NAME" : "필수동의 알림여부" } ],
            tagId : "mandatoryYn",
            change : function(e){
            	var chk = $('input:checkbox[id="mandatoryYn_0"]').is(":checked");
        		var mandatory = document.getElementById("mandatory");
        	    if(chk==true){
        	    	mandatory.style.display = '';
        	    	$("#mandatoryYn_0").val('Y');
        	    	$("#clauseInfo").attr("required", true);
        	    }else{
        	    	mandatory.style.display = 'none';
        	    	$("#mandatoryYn_0").val('N');
        	    	$("#clauseInfo").attr("required", false);
        	    }
            }
        });
	}

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	function inputFocus(){
		$('#input1').focus();
	}
	function condiFocus(){
		$('#condition1').focus();
	}

		/**
	 * 콜백합수
	 */
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
				$('#inputForm').bindingForm(data, 'rows', true);
				break;
			case 'insert':
				if(data.rows =="DUP_DATA"){
					fnKendoMessage({message : fnGetLangData({key :"370",nullMsg :'중복입니다.' })  ,ok :inputFocus});
				}else{
					fnKendoMessage({message : fnGetLangData({key :"369",nullMsg :'저장되었습니다.' }) ,ok :fnClose});
				}
				break;
			case 'save':
				fnKendoMessage({message : fnGetLangData({key :"368",nullMsg :'저장되었습니다.' }), ok : fnClose});
				break;
			case 'update':
				fnUpdateGrid(data,$("#aGrid"),"rows");
				fnKendoMessage({message : fnGetLangData({key :"367",nullMsg :'수정되었습니다.' }) });
				break;
			case 'delete':
				aGridDs.remove(data);
				fnNew();
				fnKendoMessage({message : fnGetLangData({key :"366",nullMsg :'삭제되었습니다.' })});
				break;

		}
		if(data.rows.mandatoryYn == 'Y'){
			$('input:checkbox[name="mandatoryYn"]').prop("checked", true);
			$("#mandatoryYn").val('Y');
			mandatory.style.display = '';
		}else{
			$("input:checkbox[name='mandatoryYn']").prop('checked', false);
			$("#mandatoryYn").val('N');
			mandatory.style.display = 'none';
		}
		let defaultDate = new Date()
		defaultDate.setDate(defaultDate.getDate() + 1)

		$("#executeDate").data("kendoDatePicker").value(defaultDate);
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common New*/
	$scope.fnNew = function( ){	fnNew();};
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	/** Common Delete*/
	$scope.fnDel = function(){	 fnDel();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------


}); // document ready - END
