/**-----------------------------------------------------------------------------
 * description 		 : 관리자 지급 한도 설정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.10.05		안치열          최초생성
 * @
 * **/
'use strict';

var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//관리자 별 설정 라디오버튼 제어
	$("input[name=settingType]").change(function() {
		var chkValue = $(this).val();
		if(chkValue == 'GROUP'){
			$("#amountIndividual").attr("disabled", true);
		}else{
			$("#amountIndividual").attr("disabled", false);
		}
	});

	$('#createInfoDiv').hide();

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'adminPointSetting',
			callback : fnUI
		});

		$('#CLICKED_MENU_ID').val( CLICKED_MENU_ID );

	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();

		fnInitSetting();

		fnSearch();

//		if(aGrid.dataSource._data.length == 0){
//			$("#aGrid tbody > tr > td").html('등록된 정보가 존재하지 않습니다.');
//		}

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnNew, #fnSave, #fnHistoryView').kendoButton();
	}

	function fnInitSetting(){
		$("#amountIndividual").attr("disabled", true);
	}

	function fnSearch(){
		var data;
		data = $('#searchForm').formSerialize(true);
		aGridDs.read(data);

	}

	function fnNew(e){
		$('#inputForm').formClear(true);
		$("#amountIndividual").attr("disabled", true);
		$('#createInfoDiv').hide();
		$("#roleGroup").data("kendoDropDownList").enable( true );
		fnKendoInputPoup({height:"300px" ,width:"650px", title:{ nullMsg :'관리자 적립금 월 지급한도 설정' } } );

	}

	function fnSave(){
		var url  = '/admin/promotion/adminPointSetting/addAdminPointSetting';
		var cbId = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/promotion/adminPointSetting/putAdminPointSetting';
			cbId= 'update';
		}

		if($('#settingType_0').prop("checked")){
			$('#amountIndividual').val('_');
		}

		//삭제 구분 'N'
		$('#delYn').val('N');

		var data = $('#inputForm').formSerialize(true);

		data.amount = data.amount.replace(',', '');
		data.amountIndividual = data.amountIndividual.replace(',', '');

		// 관리자 별 설정
		if($('#settingType_0').prop("checked")){
			$('#amountIndividual').val('');
			data.amountIndividual = null;
		}else{

			// 관리자 지급한도 설정 체크
			if(Number(data.amount) < Number(data.amountIndividual)){
				fnKendoMessage({message:'관리자별 제한 금액이 적립금 설정금액보다 큽니다.'});
				return;
			}

		}



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
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	// 수정팝업 호출
	function fnPutAdminSettingPoint(pmPointAdminSettingId){

		$('#inputForm').formClear(true);
		fnAjax({
			url     : '/admin/promotion/adminPointSetting/getAdminPointSettingDetail',
			params  : {pmPointAdminSettingId : pmPointAdminSettingId},
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'select'
		});

	}

	function fnRemoveAdminSettingPoint(data){
    	fnKendoMessage({message:data.roleName + ' 역할그룹을 삭제하시겠습니까?', type : "confirm" ,ok :function(){ fnDelApply(data)} });
    }

	// 삭제처리
	function fnDelApply(data){
		fnAjax({
			url     : '/admin/promotion/adminPointSetting/removeAdminPointSetting',
			params  : {pmPointAdminSettingId : data.pmPointAdminSettingId, delYn : 'Y'},
			success :
				function( data ){
					fnBizCallback('delete', data);
				},
				isAction : 'delete'
		});

	}

	function numberFormat(inputNumber) {
	   return inputNumber.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}


	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetDataSource({
			url		: '/admin/promotion/adminPointSetting/getAdminPointSetting'
		});
		aGridOpt = {
			dataSource: aGridDs
			,navigatable: true
			,noRecordMsg: '등록된 정보가 존재하지 않습니다.'
			//,height:550
			,columns   : [
				{ field:'no'		,title : 'No'				, width:'50px',attributes:{ style:'text-align:center' }, template:"<span class='row-number'></span>"}
				,{ field:'pmPointAdminSettingId'		,hidden:true}
				,{ field:'roleName'		,title : '역할그룹'						, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'amount'		,title : '월간 최대 지급가능 적립금'			, width:'100px',attributes:{ style:'text-align:left' },
					template : function(dataItem){
                	    let returnValue;
                        returnValue = numberFormat(dataItem.amount) + '원';
                        return returnValue;
			          }
				}
				,{ field:'validityDay'		,title : '최대 설정가능 유효기간'			, width:'100px',attributes:{ style:'text-align:left' },
					template : function(dataItem){
                	    let returnValue;
                        returnValue = '지급일로 부터 ' + dataItem.validityDay + '일';
                        return returnValue;
			          }
					}
				,{ field:'amountIndividual'		,title : '관리자별 제한'	, width:'150px',attributes:{ style:'text-align:left' },
					template : function(dataItem){
                	    let returnValue;
                	    if(dataItem.settingType == 'GROUP'){
                	    	returnValue = '제한없음';
                	    }else{
                	    	returnValue = '관리자별 : ' + numberFormat(dataItem.amountIndividual) + ' 원';
                	    }
                        return returnValue;
			          }
				}
				,   { command: [{ text: '수정', visible: function() { return fnIsProgramAuth("SAVE_DELETE") }, className:'btn-s btn-white',
					click: function(e) {

		            e.preventDefault();
		            var tr = $(e.target).closest("tr"); // get the current table row (tr)
		            var data = this.dataItem(tr);

		            fnPutAdminSettingPoint(data.pmPointAdminSettingId);
					} },
					{ text: '삭제', visible: function() { return fnIsProgramAuth("SAVE_DELETE") }, className:'k-button k-button-icontext btn-red btn-s k-grid-delete',
						click: function(e) {

			            e.preventDefault();
			            var tr = $(e.target).closest("tr"); // get the current table row (tr)
			            var data = this.dataItem(tr);

			            fnRemoveAdminSettingPoint(data);
						} }
					]
				, title: '관리', width: "100px", attributes:{ style:'text-align:center'  , class:'forbiz-cell-readonly #:#' }}   // EXECUTE_TYPE 별 버튼 제어 처리 확인 필요
			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

//		if(aGrid.dataSource._data.length == 0){
//			$("#aGrid tbody > tr > td").html('등록된 정보가 존재하지 않습니다.');
//		}

		aGrid.bind("dataBound", function(){
        	var row_num = aGrid._data.length;
			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(index+1);
				//row_num--;
			});

        });


	}
	/*function fnDel(e){
		e.preventDefault();
		var dataItem = aGrid.dataItem($(e.currentTarget).closest('tr'));
		var data = {'snAutoSendId' : dataItem.snAutoSendId};
		fnKendoMessage({
			type    : 'confirm',
			message : "삭제하시겠습니까?",
			ok      : function(e){
						var url  = '/admin/sn/email/delEmailSend';
						fnAjax({
								url     : url,
								params  : data,
								success :
									function( data ){
											var cbId = 'delete';
											fnBizCallback(cbId, dataItem);
									},
								isAction : 'delete'
							});
			},
			cancel  : function(e){  }
		});

	}*/

	// 관리자 적립금 지급 한도 이력 보기
	function fnHistoryView(){

		fnKendoPopup({
			id     : 'adminPointSettingList',
			title  : '관리자 적립금 월 지급한도 수정내역',
			src    : '#/adminPointSettingList',
			width  : '1400px',
			height : '1200px',
			param  : { },
			success: function( id, data ){
				/*if(data[0] != undefined){
					$('#dawnDeliveryPatternId').val(data[0]);
					$('#dawnDeliveryPatternName').val(data[1]);
				}*/
			}
		});
	}

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

		// 역할그룹 조회
		fnKendoDropDownList({
			id    : 'roleGroup',
			url : "/admin/promotion/adminPointSetting/getRoleGroupList",
			params : {},
			textField :"roleName",
			valueField : "stRoleTpId",
			blank : "선택해주세요"
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
				$('#createInfoDiv').show();
				fnKendoInputPoup({height:"330px" ,width:"550px", title:{ nullMsg :'관리자 적립금 한도 수정' } } );
				data.rows.amount = numberFormat(data.rows.amount);


				if(data.rows.settingType == 'GROUP'){
					$("#settingType_0").prop("checked",true);
					$("#amountIndividual").attr("disabled", true);
				}else{
					$("#settingType_1").prop("checked",true);
					$("#amountIndividual").attr("disabled", false);
					data.rows.amountIndividual = numberFormat(data.rows.amountIndividual);
				}

				var createInfo;
				createInfo = data.rows.createDate + ' ' + data.rows.createName + '(' + data.rows.createLoginId + ')';
				data.rows.createInfo = createInfo;

				var modifyInfo;
				modifyInfo = data.rows.modifyDate + ' ' + data.rows.modifyName + '(' + data.rows.modifyLoginId + ')';
				if(data.rows.modifyDate != null){
					data.rows.modifyInfo = modifyInfo;
				}

				$('#inputForm').bindingForm(data, 'rows', true);

				$("#roleGroup").data("kendoDropDownList").enable( false );
				break;
			case 'insert':
			case 'update':
				fnKendoMessage({
						message:"저장되었습니다.",
						ok:function(){
							fnSearch();
							fnClose();
						}
				});
				break;
			case 'delete':
				fnKendoMessage({
						message:"삭제되었습니다.",
						ok:function(){
							fnSearch();
						}
				});

		}
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common New*/
	$scope.fnNew = function( ){	fnNew();};
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	/** Common HistoryView*/
	$scope.fnHistoryView = function(){	 fnHistoryView();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//마스터코드값 입력제한 - 숫자 & -
	fnInputValidationByRegexp("amount", /[^0-9]/g);
	fnInputValidationByRegexp("validityDay", /[^0-9]/g);
	fnInputValidationByRegexp("amountIndividual", /[^0-9]/g);

}); // document ready - END
