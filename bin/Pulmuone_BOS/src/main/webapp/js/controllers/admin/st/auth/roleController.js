/**-----------------------------------------------------------------------------
 * description 		 : 역할관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2017.01.10		최봉석          최초생성
 * @ 2020.12.07		강윤경          현행화
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
			PG_ID  : 'role',
			callback : fnUI
		});

	}

	function fnUI(){

		fnTranslate();	// comm.lang.js 안에 있는 공통함수 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnSearch();

		fnPreventSubmit();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew,#fnSave,  #fnClear').kendoButton();
	}
	function fnSearch(){
		$('#inputForm').formClear(false);
		var data;
		data = $('#searchForm').formSerialize(true);
		var query = {
					page         : 1,
					pageSize     : PAGE_SIZE,
					filterLength : fnSearchData(data).length,
					filter :  {
						filters : fnSearchData(data)
					}
		};
		aGridDs.query( query );

	}
	function fnClear(){
		$('#searchForm').formClear(true);
	}

	function fnNew(){
		aGrid.clearSelection();
		$('#inputForm').formClear(true);
		fnFormPopup();
	//	initPopupOptionBox();

		$("#inputSectorAllViewYn_1").prop("checked",true);
	}
	function fnFormPopup(){
		fnKendoInputPoup({height:'270px', width:'550px', title:{key : 'XXXX', nullMsg : '역할관리 정보'}});
		//$('#URL').focus();
	}


	function fnSave(){
		var url  = '/admin/st/auth/addRole';
		var cbId = 'insert';
		var isAction = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/st/auth/putRole';
			cbId= 'update';
			isAction = 'update';
		}

		var data = $('#inputForm').formSerialize(true);

		if( data.rtnValid ){
			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback(cbId, data);
					},
					isAction : isAction
			});
		}
	}

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();

	}


	// 조직검색 팝업 호출
	function fnDeptPopupButton(){

		fnKendoPopup({
			id     : 'deptPopup',
			title  : '조직검색',
			src    : '#/deptPopup',
			width  : '900px',
			height : '800px',
			param  : { },
			success: function( stMenuId, data ){
				$('#inputErpOrganizationName').val(data.erpOrganizationName);
				$('#inputErpRegalCode').val(data.erpRegalCode);
				$('#inputErpOrganizationCode').val(data.erpOrganizationCode);
			}
		});
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({  //fnGetDataSource 페이징이 없을때
			url      :  "/admin/st/auth/getRoleList"
    		,pageSize : PAGE_SIZE

		});

		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 5
			}
			,navigatable: true
			,columns   : [
				{ field:'stRoleTypeId'  ,title : 'No'      , width:'90px'	,attributes:{ style:'text-align:center' }}
				,{ field:'roleName'     ,title : '역할그룹명' , width:'200px'	,attributes:{ style:'text-align:center' }}
				,{ field:'erpOrganizationName'    ,title : '조직명' , width:'200px'	,attributes:{ style:'text-align:center' }}
				,{ field:'erpRegalName' ,id:'erpRegalName',title : '법인명' , width:'200px'	,attributes:{ style:'text-align:center' }}
				,{ field:'useYn'	   ,title :'사용여부'	  , width:'90px',attributes:{ style:'text-align:center' }
				,template : "#=(useYn=='Y')?fnGetLangData({key :359,nullMsg :'예' }):fnGetLangData({key :360,nullMsg :'아니오' })#"
				}
				,{ field : "management", title : "관리", 		width : "100px", attributes : { style:"text-align:center"},
                    command: { text : "복사"}
                  }

			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		$("#aGrid").on("click", "tbody>tr>td", function (e) {

			const dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));
			const row = $(this).closest("tr");
			const rowIdx = $("tr", aGrid).index(row);
			const colIdx = $("td", row).index(this);

			if(aGrid.options.columns[colIdx].field == "management") {

                e.preventDefault();
				fnGridClick('common', dataItem.stRoleTypeId);
			} else {
				var aMap = aGrid.dataItem(aGrid.select());
				fnGridClick('select', dataItem.stRoleTypeId );
			}
		});

		aGrid.bind("dataBound", function() {
			$("#countTotalSpan").text(aGridDs._total);
		})

		//fnSearch();
	}

	function fnGridClick(action, stRoleTypeId){
		//var aMap = aGrid.dataItem(aGrid.select());

		fnAjax({
			url     : '/admin/st/auth/getRole',
			params  : {'stRoleTypeId' : stRoleTypeId},
			success :
				function( data ){
					fnBizCallback(action ,data);
				},
			isAction : action
		});
	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

		fnTagMkRadio({
			id    :  "condiActive"
			,data  :  [
						{ "CODE" : "" , "NAME": "전체"}
						,	{ "CODE" : "Y" , "NAME": "예"}
						, 	{ "CODE" : "N" , "NAME": "아니오"}]
			,tagId : "useYn"
			,chkVal: ""
		});

		fnTagMkRadioYN({id: "intputActive" , tagId : "inputUseYn",chkVal: 'N'});

		fnTagMkRadio({
        			id    :  'inputSectorAllViewYn',
        			data  : [
        					   { "CODE" : "Y"	, "NAME":'예' },
        					   { "CODE" : "N"	, "NAME":'아니오' }
        					],
        			tagId : 'inputSectorAllViewYn',
        			chkVal: "N",
        			style : {}
        		});


		fnKendoDropDownList({
			id    : 'erpRegalCode',
			url : "/admin/user/employee/getPulmuoneRegalListWithoutPaging",
			textField :"erpRegalName",
			valueField : "erpRegalCode",
			blank: '전체'
		});

		fnKendoDropDownList({
			id    : 'erpOrganizationCode',
			url : "/admin/user/employee/getPulmuoneOrganizationList",
			//cscdId : 'erpRegalCode',
			//cscdField: 'erpRegalCode',
			textField :"erpOrganizationName",
			valueField : "erpOrganizationCode",
			blank: '전체'
		});

		/*
		 *
		fnKendoDropDownList({
			id    : 'erpOrganizationCode',
			blank: 'All'
		});
		$('#erpOrganizationCode').data('kendoDropDownList').enable(false);
		*/
	}

	function initPopupOptionBox() {
		fnKendoDropDownList({
			id    : 'inputErpRegalCode',
			url : "/admin/user/employee/getPulmuoneRegalListWithoutPaging",
			textField :"erpRegalName",
			valueField : "erpRegalCode",
			blank: '선택해주세요.'
		});


		$('#inputErpRegalCode').data('kendoDropDownList').bind('change' ,function(e){
			if($('#inputErpRegalCode').val() != '') {
				fnKendoDropDownList({
					id    : 'inputErpOrganizationCode',
					url : "/admin/user/employee/getPulmuoneOrganizationList",
					params : {"erpRegalCode" : $('#inputErpRegalCode').val()},
					textField :"erpOrganizationName",
					valueField : "erpOrganizationCode",
					blank: '선택해주세요.'
				});
				$('#inputErpOrganizationCode').data('kendoDropDownList').enable(true);
			} else {
				fnKendoDropDownList({
					id    : 'inputErpOrganizationCode',
					blank: '선택해주세요.'
				});
				$('#inputErpOrganizationCode').data('kendoDropDownList').enable(false);
			}
		});

		fnKendoDropDownList({
			id    : 'inputErpOrganizationCode',
			blank: '선택해주세요.'
		});

		$('#inputErpOrganizationCode').data('kendoDropDownList').enable(false);
	}

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//initPopupOptionBox();

				$('#inputForm').bindingForm(data, 'rows', true);
				/*
				fnKendoDropDownList({
					id    : 'inputErpOrganizationCode',
					url : "/admin/user/employee/getPulmuoneOrganizationList",
					params : {"erpRegalCode" : data.rows.inputErpRegalCode},
					textField :"erpOrganizationName",
					valueField : "erpOrganizationCode",
					value: data.rows.inputErpOrganizationCode,
					blank: '선택해주세요.'
				});
				if(data.rows.inputErpRegalCode) {
					$('#inputErpOrganizationCode').data('kendoDropDownList').enable(true);
				} else {
					$('#inputErpOrganizationCode').data('kendoDropDownList').enable(false);
				}*/

				if(data.rows.sectorAllViewYn == 'Y'){
					$("#inputSectorAllViewYn_0").prop("checked",true);
				}else{
					$("#inputSectorAllViewYn_1").prop("checked",true);
				}
				fnFormPopup();
				break;
			case 'common':
				aGrid.clearSelection();
				$('#inputForm').formClear(true);

				$('#inputErpOrganizationName').val(data.rows.inputErpOrganizationName);
				$('#inputErpRegalCode').val(data.rows.inputErpRegalCode);
				$('#inputErpOrganizationCode').val(data.rows.inputErpOrganizationCode);
				$('#action').val('COPY');
				$('#originStRoleTypeId').val(data.rows.inputStRoleTypeId);
				fnTagMkRadioYN({id: "intputActive" , tagId : "inputUseYn",chkVal: data.rows.inputUseYn});

				fnFormPopup();
				break;

			case 'insert':
				fnKendoMessage({message :"저장되었습니다.", ok :function(e){
					$('#searchForm').formClear(true);
					fnSearch();
					$('#kendoPopup').data('kendoWindow').close();
				}});
				break;
			case 'save':
				fnKendoMessage({message :"저장되었습니다."});
				break;
			case 'update':
				fnKendoMessage({message : "저장되었습니다.", ok: function(e) {
					$('#searchForm').formClear(true);
					fnSearch();
					$('#kendoPopup').data('kendoWindow').close();
				}});
				break;
			case 'delete':
				fnKendoMessage({ message :'삭제되었습니다.'
								,ok :function(e){
									fnSearch();
									fnClose();
									//$('#kendoPopup').data('kendoWindow').close();
								}});
				break;

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
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	//역할조직 조회
	$scope.fnDeptPopupButton = function( ){  fnDeptPopupButton();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
