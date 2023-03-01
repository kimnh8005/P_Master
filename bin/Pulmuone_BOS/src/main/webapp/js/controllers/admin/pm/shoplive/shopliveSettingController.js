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
			PG_ID  : 'shopliveSetting',
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
		// $("#roleGroup").data("kendoDropDownList").enable( true );
		fnKendoInputPoup({height:"250px" ,width:"850px", title:{ nullMsg :'편성 컨텐츠 등록' } } );

	}

	function fnSave(){
		var url  = '/admin/pm/shoplive/addShopliveInfo';
		var cbId = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/pm/shoplive/putShopliveInfo';
			cbId= 'update';
		}

		if($('#settingType_0').prop("checked")){
			$('#amountIndividual').val('_');
		}

		//삭제 구분 'N'
		$('#delYn').val('N');

		var data = $('#inputForm').formSerialize(true);
		data.startDt          = data.startDe + '' + data.startHour + '' + data.startMin + '00'; // 시작일시
		data.endDt            = data.endDe   + '' + data.endHour   + '' + data.endMin   + '59'; // 종료일시

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

	function fnSearchRemoteLive() {
		let campaignKey = $('#campaignKey').val();
		fnAjax({
			url     : '/admin/pm/shoplive/getRemoteShopliveInfo',
			params  : {campaignKey : campaignKey},
			success :
				function( data ){
					fnBizCallback("remote",data);
				},
			isAction : 'select'
		});
	}

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	// 수정팝업 호출
	function fnPutAdminSettingPoint(evShopliveId){

		$('#inputForm').formClear(true);
		fnAjax({
			url     : '/admin/pm/shoplive/selectShopliveInfo',
			params  : {evShopliveId : evShopliveId},
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'select'
		});

	}

	function fnRemoveAdminSettingPoint(data){
    	fnKendoMessage({message:'[' + data.title + '] 편성 컨텐츠를 삭제하시겠습니까?', type : "confirm" ,ok :function(){ fnDelApply(data)} });
    }

	// 삭제처리
	function fnDelApply(data){
		fnAjax({
			url     : '/admin/pm/shoplive/delShopliveInfo',
			params  : {evShopliveId : data.evShopliveId, delYn : 'Y'},
			success :
				function( data ){
					fnBizCallback('delete', data);
				},
				isAction : 'delete'
		});

	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetDataSource({
			url		: '/admin/pm/shoplive/selectShopliveList'
		});
		aGridOpt = {
			dataSource: aGridDs
			,navigatable: true
			,noRecordMsg: '등록된 정보가 존재하지 않습니다.'
			//,height:550
			,columns   : [
				{ field:'no'		,title : 'No'				, width:'50px',attributes:{ style:'text-align:center' }, template:"<span class='row-number'></span>"}
				,{ field:'evShopliveId' ,title : '방송ID' , width:'50px',attributes:{ style:'text-align:center' }}
				,{ field:'title'		,title : '방송제목'			, width:'250px',attributes:{ style:'text-align:left' }}
				,{ field:'startDate'	,title : '시작일자'			, width:'100px',attributes:{ style:'text-align:center' }}
				,{ field:'endDate'		,title : '종료일자'			, width:'100px',attributes:{ style:'text-align:center' }}
				,{ command: [{ text: '링크 URL 복사', visible: function() { return fnIsProgramAuth("SAVE_DELETE") }, className:'btn-s btn-gray marginR10 marginL10',
						click: function(e) {
							e.preventDefault();
							var tr = $(e.target).closest("tr"); // get the current table row (tr)
							var data = this.dataItem(tr);

							const tempElem = document.createElement('textarea');
							tempElem.value = "javascript:window.openShoplive('" + data.evShopliveId + "');";
							document.body.appendChild(tempElem);

							tempElem.select();
							document.execCommand('copy');
							document.body.removeChild(tempElem);
					} },
					{ text: '수정', visible: function() { return fnIsProgramAuth("SAVE_DELETE") }, className:'btn-s btn-white marginR10 marginL10',
						click: function(e) {

							e.preventDefault();
							var tr = $(e.target).closest("tr"); // get the current table row (tr)
							var data = this.dataItem(tr);

							fnPutAdminSettingPoint(data.evShopliveId);
					} },
					{ text: '삭제', visible: function() { return fnIsProgramAuth("SAVE_DELETE") }, className:'k-button k-button-icontext btn-red btn-s k-grid-delete marginR10 marginL10',
						click: function(e) {

							e.preventDefault();
							var tr = $(e.target).closest("tr"); // get the current table row (tr)
							var data = this.dataItem(tr);

							fnRemoveAdminSettingPoint(data);
					} }
					]
				, title: '관리', width: "150px", attributes:{ style:'text-align:center'  , class:'forbiz-cell-readonly #:#' }}   // EXECUTE_TYPE 별 버튼 제어 처리 확인 필요
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

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

		// // 역할그룹 조회
		// fnKendoDropDownList({
		// 	id    : 'roleGroup',
		// 	url : "/admin/promotion/adminPointSetting/getRoleGroupList",
		// 	params : {},
		// 	textField :"roleName",
		// 	valueField : "stRoleTpId",
		// 	blank : "선택해주세요"
		// });
		// ------------------------------------------------------------------------
		// 진행기간-시작일자/종료일자 : [공통]
		// ------------------------------------------------------------------------
		fnKendoDatePicker({
			id      : 'startDe'
			, format  : 'yyyy-MM-dd'
			, change  : fnOnChangeStartDe
			//, defVal  : /gbTodayDe
		});

		fnKendoDatePicker({
			id        : 'endDe'
			, format    : 'yyyy-MM-dd'
			//, btnStyle  : true
			, btnStartId: 'startDe'
			, btnEndId  : 'endDe'
			//, defVal    : '2999-12-31'
			, defType   : 'oneWeek'
			, minusCheck: true
			, nextDate  : true
			, change  : function(e) {
				// --------------------------------------------------------
				// 체험단 일자 체크
				// --------------------------------------------------------
				if (gbEventTp == 'EVENT_TP.EXPERIENCE') {
					// 체험단인 경우만 실행
					// ------------------------------------------------------
					// 당첨자선정기간 종료일자와 선후 체크
					// ------------------------------------------------------
					fnOnChangeDateTimePickerCustom( e, 'start', 'endDe', 'endHour', 'endMin', 'selectEndDe', 'selectEndHour', 'selectEndMin', '진행기간종료일시', '당첨자선정기간종료일시');
					// ------------------------------------------------------
					// 지정한 시작일자에 Set
					// ------------------------------------------------------
					fnSetStartPickerNextMinute('end', 'selectStart');
				}
				// --------------------------------------------------------
				// 일자 선후체크
				// --------------------------------------------------------
				fnOnChangeEndDe(e);
			}
		});
		fbMakeTimePicker('#startHour', 'start', 'hour', fnOnChangeStartDe);
		fbMakeTimePicker('#startMin' , 'start', 'min' , fnOnChangeStartDe);
		fbMakeTimePicker('#endHour'  , 'end'  , 'hour', fnOnChangeEndDe);
		fbMakeTimePicker('#endMin'   , 'end'  , 'min' , fnOnChangeEndDe);
		// 종료 시/분 기본값 Set
		$('#endHour').data('kendoDropDownList').select(23);
		$('#endMin').data('kendoDropDownList').select(59);
		// 일자 선후 체크
		function fnOnChangeStartDe(e) {
			fnOnChangeDateTimePicker( e, 'start', 'startDe', 'startHour', 'startMin',  'endDe', 'endHour', 'endMin' );
		}
		function fnOnChangeEndDe(e) {
			fnOnChangeDateTimePicker( e, 'end'  , 'startDe', 'startHour', 'startMin',  'endDe', 'endHour', 'endMin' );
		}
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
				fnKendoInputPoup({height:"260px" ,width:"850px", title:{ nullMsg :'편성 컨텐츠 수정' } } );

				var createInfo;
				createInfo = data.rows.createDt + ' / ' + data.rows.createNm + '(' + data.rows.createId + ')';
				data.rows.createInfo = createInfo;

				var modifyInfo;
				modifyInfo = data.rows.modifyDt + ' / ' + data.rows.modifyNm + '(' + data.rows.modifyId + ')';
				if(data.rows.modifyDt != null){
					data.rows.modifyInfo = modifyInfo;
				}
				// ------------------------------------------------------------------------
				// 진행기간
				// ------------------------------------------------------------------------
				// 진행기간-시작
				var startDtStr = '';
				var startDe    = '';
				var startHour;
				var startMin;
				//startDtStr  = (((eventInfo.startDt).replace(/-/gi, '')).replace(/:/gi, '')).replace(/ /gi, '');
				startDtStr  = ((data.rows.startDt).replace(/:/gi, '')).replace(/ /gi, '');

				if (startDtStr != null && startDtStr != '') {
					if (startDtStr.length >= 10) {
						startDe = startDtStr.substring(0, 10);
						$('#startDe').val(startDe);
						// --------------------------------------------------------------------
						// 날짜 선후비교를 위한 kendoDatePicker 값 Set
						// --------------------------------------------------------------------
						const $datePicker = $('#startDe').data('kendoDatePicker');
						if( $datePicker ) {
							$datePicker.value(startDe);
						}
					}
					if (startDtStr.length >= 12) {
						startHour = Number(startDtStr.substring(10, 12));
						$('#startHour').data('kendoDropDownList').select(startHour);
					}
					if (startDtStr.length >= 14) {
						startMin = Number(startDtStr.substring(12, 14));
						$('#startMin').data('kendoDropDownList').select(startMin);
					}
				}

				// 진행기간-종료
				var endDtStr = '';
				var endDe    = '';
				var endHour;
				var endMin;
				endDtStr  = ((data.rows.endDt).replace(/:/gi, '')).replace(/ /gi, '');
				if (endDtStr != null && endDtStr != '') {
					if (endDtStr.length >= 10) {
						endDe = endDtStr.substring(0, 10);
						$('#endDe').val(endDe);
						// --------------------------------------------------------------------
						// 날짜 선후비교를 위한 kendoDatePicker 값 Set
						// --------------------------------------------------------------------
						const $datePicker = $('#endDe').data('kendoDatePicker');
						if( $datePicker ) {
							$datePicker.value(endDe);
						}
					}
					if (endDtStr.length >= 12) {
						endHour = Number(endDtStr.substring(10, 12));
						$('#endHour').data('kendoDropDownList').select(endHour);
					}
					if (endDtStr.length >= 14) {
						endMin = Number(endDtStr.substring(12, 14));
						$('#endMin').data('kendoDropDownList').select(endMin);
					}
				}


				$('#inputForm').bindingForm(data, 'rows', true);
				break;
			case 'remote':

				if(data.rows === undefined || data.rows.campaignStatus === undefined || data.rows.campaignStatus == null || data.rows.campaignStatus === "NOT_EXIST") {
					fnKendoMessage({ message : "방송을 조회할 수 없습니다.<br/>연동코드가 잘못되었거나 방송중에는 정보를 가져올 수 없습니다."});
					break;
				}
				// ------------------------------------------------------------------------
				// 진행기간
				// ------------------------------------------------------------------------
				// 진행기간-시작
				var startDtStr = '';
				var startDe    = '';
				var startHour;
				var startMin;
				//startDtStr  = (((eventInfo.startDt).replace(/-/gi, '')).replace(/:/gi, '')).replace(/ /gi, '');
				startDtStr  = ((data.rows.startDt).replace(/:/gi, '')).replace(/ /gi, '');

				if (startDtStr != null && startDtStr != '') {
					if (startDtStr.length >= 10) {
						startDe = startDtStr.substring(0, 10);
						$('#startDe').val(startDe);
						// --------------------------------------------------------------------
						// 날짜 선후비교를 위한 kendoDatePicker 값 Set
						// --------------------------------------------------------------------
						const $datePicker = $('#startDe').data('kendoDatePicker');
						if( $datePicker ) {
							$datePicker.value(startDe);
						}
					}
					if (startDtStr.length >= 12) {
						startHour = Number(startDtStr.substring(10, 12));
						$('#startHour').data('kendoDropDownList').select(startHour);
					}
					if (startDtStr.length >= 14) {
						startMin = Number(startDtStr.substring(12, 14));
						$('#startMin').data('kendoDropDownList').select(startMin);
					}
				}

				// 진행기간-종료
				var endDtStr = '';
				var endDe    = '';
				var endHour;
				var endMin;
				endDtStr  = ((data.rows.endDt).replace(/:/gi, '')).replace(/ /gi, '');
				if (endDtStr != null && endDtStr != '') {
					if (endDtStr.length >= 10) {
						endDe = endDtStr.substring(0, 10);
						$('#endDe').val(endDe);
						// --------------------------------------------------------------------
						// 날짜 선후비교를 위한 kendoDatePicker 값 Set
						// --------------------------------------------------------------------
						const $datePicker = $('#endDe').data('kendoDatePicker');
						if( $datePicker ) {
							$datePicker.value(endDe);
						}
					}
					if (endDtStr.length >= 12) {
						endHour = Number(endDtStr.substring(10, 12));
						$('#endHour').data('kendoDropDownList').select(endHour);
					}
					if (endDtStr.length >= 14) {
						endMin = Number(endDtStr.substring(12, 14));
						$('#endMin').data('kendoDropDownList').select(endMin);
					}
				}
				$('#title').val(data.rows.title);

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

	$scope.fnSearchRemoteLive = function(){ fnSearchRemoteLive(); };
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
