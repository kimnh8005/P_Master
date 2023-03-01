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
			PG_ID  : 'clauseView',
			callback : fnUI
		});
	}

	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		$('#content').focus();

		fnDefaultValue();

	}

	//--------------------------------- Button Start---------------------------------
	function fnDefaultValue(){
		$('#psClauseGroupCd').val(parent.POP_PARAM["parameter"].psClauseGroupCd);
		$('#clauseGroupName').val(parent.POP_PARAM["parameter"].clauseGroupName);
		$('#psClauseId').val(parent.POP_PARAM["parameter"].psClauseId);

		var data = $('#inputForm').formSerialize(true);
		fnAjax({
			url     : '/admin/policy/clause/getClauseModifyView',
			params  : data,
			success :
				function( data ){
					$("#createUserInfo").html(fnNvl(data.rows.createUserInfo));
					$("#modifyUserInfo").html(fnNvl(data.rows.modifyUserInfo));
					$("#executeDate").html(fnNvl(data.rows.clauseDescription));
					//$("#mandatoryYn").html(fnNvl(data.rows.mandatoryYn));
					var mandatory = document.getElementById("mandatory");

					if(data.rows.mandatoryYn == 'Y'){
						$('input:checkbox[name="mandatoryYn"]').prop("checked", true);
						$("#mandatoryYn").val('Y');
						mandatory.style.display = '';
					}else{

						$("input:checkbox[name='mandatoryYn']").prop('checked', false);
						$("#mandatoryYn").val('N');
						mandatory.style.display = 'none';
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
		mandatory.style.display = 'none';
	}

	function fnSave(){
		var url  = '/admin/policy/clause/putClause';
		var cbId = 'update';

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
		}else{

		}
	}

	function fnClose(){
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){

	}
	function fnGridClick(){
		var map = aGrid.dataItem(aGrid.select());

		bGridDs.read( {ST_SHOP_ID:map.ST_SHOP_ID,PS_CLAUSE_GRP_ID:map.PS_CLAUSE_GRP_ID});
	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		let presentDate = new Date();

		fnTagMkRadioYN({id: "intputActive" , tagId : "USE_YN",chkVal: 'Y'});

		fnKendoEditor( {id : 'content'} );
		fnKendoEditor( {id : 'clauseInfo'} );


		fnKendoDatePicker({
		    id    : 'executeDate',
		    format: 'yyyy-MM-dd',
		    btnStartId : 'startDate',
		    btnEndId : 'executeDate',
		    change: function(e){
		    	 fnEndCalChange_new('startDate', 'executeDate');
			}
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

	function fnEndCalChange_new( sId, eId ) {
		/*var sd = $('#' + sId).data('kendoDatePicker');
		var ed = $('#' + eId).data('kendoDatePicker');
	    var startDate = fnGetToday();
	    startDate = new Date(startDate);
	    startDate.setDate(startDate.getDate());
	    ed.min(startDate);
*/
		var sd = $('#' + sId).data('kendoDatePicker');
		var ed = $('#' + eId).data('kendoDatePicker');
	    var startDate = fnGetToday();
	    var endDate   = fnGetToday();

	    if (startDate) {
	        startDate = new Date(startDate);
	        startDate.setDate(startDate.getDate());
	        ed.min(startDate);
	    } else if (endDate) {
	        sd.max(new Date(endDate));
	    } else {
	        endDate = new Date();
	        sd.max(endDate);
	        ed.min(endDate);
	    }

		var sd = $('#' + sId).data('kendoDatePicker');
		var ed = $('#' + eId).data('kendoDatePicker');
		var endDate   = ed.value();
		var startDate = fnGetToday();

	    if (endDate) {
	        endDate = new Date(endDate);
	        endDate.setDate(endDate.getDate());
	        sd.max(endDate);
	    } else if (startDate) {
	        ed.min(new Date(startDate));
	    } else {
	        endDate = new Date();
	        sd.max(endDate);
	        ed.min(endDate);
	    }
	}

	function inputFocus(){
		$('#input1').focus();
	};
		function condiFocus(){
		$('#condition1').focus();
	};

	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				$('#inputForm').bindingForm(data, 'rows', true);
				if(data.rows.modifyUserInfo) {
					$("#modifyUserInfoTitle").html("최근업데이트&nbsp;");
				}
				break;
			case 'update':
				fnKendoMessage({message : fnGetLangData({key :"367",nullMsg :'수정되었습니다.' }), ok : fnClose });
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
