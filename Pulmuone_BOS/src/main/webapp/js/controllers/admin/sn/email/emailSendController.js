/**-----------------------------------------------------------------------------
 * description 		 : Mail / SMS 설정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.06.17		오영민          최초생성
 * @
 * **/
'use strict';

var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'emailSend',
			callback : fnUI
		});

		$('#CLICKED_MENU_ID').val( CLICKED_MENU_ID );

	}

	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnSearch();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnNew').kendoButton();
	}
	function fnSearch(){
		var data;
		data = $('#searchForm').formSerialize(true);
		aGridDs.read(data);
	}
	function fnNew(e){
		fnKendoPopup({
			id     : 'emailSendAdd',
			title  : 'Mail / SMS 설정',
			src    : '#/emailSendAdd',
			width  : '1030px',
			height : '750px',
			scrollable : 'yes',
			param  : {XXXX : 'X'},
			success: function( id, data ){
				fnSearch();
			}
		});

	}


	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetDataSource({
			url      : '/admin/sn/email/getEmailSendList'
		});
		aGridOpt = {
			dataSource: aGridDs
			,navigatable: true
			//,height:550
			,columns   : [
				{ field:'snAutoSendId'		,hidden:true}
				,{ field:'templateCode'		,hidden:true}
				,{ field:'templateName'		,hidden:true}
				,{ field:'mailBody'		,hidden:true}
				,{ field:'smsBody'			,hidden:true}
				,{ field:'snAutoSendId'		,title : 'NO'				, width:'80px',attributes:{ style:'text-align:center' }}
				,{ field:'templateCode'		,title : '템플릿 ID'			, width:'450px',attributes:{ style:'text-align:left' }}
				,{ field:'templateName'		,title : 'Email / SMS 템플릿 제목'	, width:'450px',attributes:{ style:'text-align:left' }}
				,{ field:'mailSendYn'		,title : 'Email 자동발송여부'		, width:'140px',attributes:{ style:'text-align:center' }
					,template: kendo.template($("#mailSendTpl").html())
					}
				,{ field:'smsSendYn'		,title : 'SMS 자동발송여부'		, width:'140px',attributes:{ style:'text-align:center' }
					,template: kendo.template($("#smsSendTpl").html())}
				,{ command: { text: "삭제", className: "btn-red btn-s", click: fnDel }, title : '관리', width:'100px' ,attributes:{ style:'text-align:center', class:'forbiz-cell-readonly' }}
//				,{ title:  fnGetLangData({key :"660",nullMsg :'관리' }), width: "100px", attributes:{ style:'text-align:center'  , class:'forbiz-cell-readonly' }
//					, command: { text: "삭제", className: "btn-red btn-s" }, imageClass: "k-i-delete", className: "k-custom-delete", iconClass: "k-icon", click: fnDel }
			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		$("#aGrid").on("click", "tbody>tr", function () {
				fnGridClick();
		});
	}
	function fnDel(e){
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

	}

	function fnGridClick(){

		var dataItem = aGrid.dataItem(aGrid.select());
		var param  = {'snAutoSendId' : dataItem.snAutoSendId};

		fnKendoPopup({
			id     : 'emailSendPut',
			title  : fnGetLangData({key :"5755",nullMsg :'자동메일/SMS설정' }),
			src    : '#/emailSendPut',
			//param  : option.data,
			param  : param,
			width  : '1030px',
			height : '750px',
			success: function( id, data ){
				fnSearch();
			}
		});
	}
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	/**
	 * 콜백합수
	 */
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
				$('#inputForm').bindingForm(data, 'rows', true);
				break;
			case 'delete':
				fnKendoMessage({message : '삭제되었습니다.'});
				fnSearch();
				break;
		}
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common New*/
	$scope.fnNew = function( ){	fnNew();};
	/** Common Delete*/
	$scope.fnDel = function(){	 fnDel();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
