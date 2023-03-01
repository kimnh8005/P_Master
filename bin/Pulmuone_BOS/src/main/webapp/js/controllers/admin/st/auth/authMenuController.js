/**-----------------------------------------------------------------------------
 * description 		 : 권한관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2017.01.10		최봉석          최초생성
 * @
 * **/
'use strict';

var aGridDs, aGridOpt, aGrid;
var bGridDs, bGridOpt, bGrid;
var cGridDs, cGridOpt, cGrid;
var dGridDs, dGridOpt, dGrid;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'authMenu',
			callback : fnUI
		});

	}

	function fnUI(){

		fnTranslate();	// comm.lang.js 안에 있는 공통함수 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		setGridHeaderScrollWidth(); // 그리드 스크롤 너비만큼 패딩 적용

	}

	//--------------------------------- Button Start---------------------------------
	function fnInitButton(){
		$('#fnSave').kendoButton();
	}

	function fnSearch(){
		var data;
		data = $('#searchForm').formSerialize(true);
		data["USE_YN"] ='Y';
		aGridDs.read(data);
	}

    function fnSave() {

        fnKendoPopup({
            id: "itgcPopup",
            title: "ITSM 계정",
            src: "#/itgcPopup",
            param: {},
            width: "450px",
            height: "150px",
            success: function(id, data) {
                if( data.itsmId == undefined ){
                    fnKendoMessage({message : 'ITSM 계정을 입력해 주세요.'});
                    return;
                }
                fnSaveProcess(data);
            }
        });

	}

	function fnSaveProcess(data){
		var aMap = aGrid.dataItem(aGrid.select());
		var cMap = cGrid.dataItem(cGrid.select());

		var selectRows 	= dGrid.tbody.find('input[class=dGridCheckbox]:checked').closest('tr');
		var insertArray = new Array();
		for(var i =0; i< selectRows.length;i++){
			var dataRow = dGrid.dataItem($(selectRows[i]));
			if(dataRow.stRoleMenuAuthMappingId == null){
				insertArray.push(dataRow);
			}
		}

		var selectDelRows = dGrid.tbody.find('input[class=dGridCheckbox]:not(:checked)').closest('tr');
		var deleteArray = new Array();
		for(var i =0; i< selectDelRows.length;i++){
			var dataRow = dGrid.dataItem($(selectDelRows[i]));
			if(dataRow.stRoleMenuAuthMappingId != null){
				deleteArray.push(dataRow);
			}
		}

		var insertData = kendo.stringify(insertArray);
		var deleteData = kendo.stringify(deleteArray);

		var data = {
				"stRoleTypeId" : aMap.stRoleTypeId
				,"stMenuId" : cMap.stMenuId
				,"menuName" : cMap.menuName
				,"insertData" : insertData
				,"deleteData" : deleteData
				,"itsmId" : data.itsmId
				,"roleName" : aMap.roleName
		};

		fnAjax({
			url     : '/admin/st/auth/saveRoleMenuAuth',
			params  : data,
			success :
				function( data ){
					fnBizCallback("save", data);
				},
			isAction : 'batch'
		});

	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){

		//------------------------------- 왼쪽그리드 S -------------------------------

		aGridDs = fnGetPagingDataSource({
			url      :  "/admin/st/auth/getRoleListWithoutPaging"
		});

		aGridOpt = {
			dataSource: aGridDs
			,navigatable: true
			,height:700
			,scrollable: true
			,columns   : [
				{ field:'roleName'	,title : '역할명'		, width:'135px',attributes:{ style:'text-align:left' }}
				,{ field:'stRoleTypeId', hidden:true}
			]
			,bindStatus : 'rebind'
			,frAtEvent  : fnAGridClick
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
		//------------------------------- 왼쪽그리드 E -------------------------------
		$("#aGrid").on("click", "tbody>tr", function () {
			fnAGridClick();
		});

		//------------------------------- 왼쪽그리드 S -------------------------------

		bGridDs = fnGetDataSource({
			url      : '/admin/st/menu/getRoleTypeMenuGroupNameList'
		});

		bGridOpt = {
			dataSource: bGridDs
			,navigatable: true
			,height:700
			,scrollable: true
			,columns   : [
				{ field:'menuGroupName'	,title : '메뉴그룹명'	, width:'160px',attributes:{ style:'text-align:left' }, template: function (dataItem){
					var str =  dataItem.menuGroupName;
					if(dataItem.authMenuCount > 0){
						str += " (" + dataItem.authMenuCount + "개)";
					}
					return str;
				}}
			]
			,noRecordMsg : '역할그룹을 선택해주세요.'
		};
		bGrid = $('#bGrid').initializeKendoGrid( bGridOpt ).cKendoGrid();
		$("#bGrid").on("click", "tbody>tr", function () {
			fnBGridClick();
		});
		//------------------------------- 왼쪽그리드 E -------------------------------

		//------------------------------- 왼쪽그리드 S -------------------------------

		cGridDs = fnGetDataSource({
			 url      : '/admin/st/auth/getAuthMenuList'
		});
		cGridOpt = {
			dataSource: cGridDs
			,navigatable: true
			,height:700
			,scrollable: true
			,columns   : [
/*				{ field:'treeMenuName'	,title : '메뉴'	, width:'300px',attributes:{ style:'text-align:left' }, template: kendo.template($("#menuNameTpl").html()) }*/
				{ field:'menuName'	,title : '메뉴명'		, width:'200px'	,attributes:{ style:'text-align:left' }, template: kendo.template($("#menuNameTpl").html())
					, filterable: {	cell: {	showOperators: false}}}
				,{ field: 'popYn'   , title: '팝업'   	, width: '65px',attributes:{ style:'text-align:center' }}
				,{ field: 'menuTypeName'   , title: '타입'   	, width: '65px',attributes:{ style:'text-align:center' }}
				,{ field:'stMenuId', hidden:true}
				,{ field:'stRoleTypeId', hidden:true}
			]
			,noRecordMsg : '메뉴그룹을 선택해주세요.'
		};
		cGrid = $('#cGrid').initializeKendoGrid( cGridOpt ).cKendoGrid();

		$("#cGrid").on("click", "tbody>tr", function () {
			fnCGridClick();
		});

		dGridDs = fnGetEditDataSource({
			url       : '/admin/st/auth/getRoleMenuAuthList',
			model_id     : 'stRoleMenuAuthMappingId',
			model_fields : {
				stRoleMenuAuthMappingId	: { editable: false	, type: 'number', validation: { required: false  }}
				,stProgramAuthId : { editable: false	, type: 'number', validation: { required: false  }}
				,programAuthCode : { editable: false	, type: 'string', validation: { required: false  }}
				,programAuthCodeName : { editable: false	, type: 'string', validation: { required: false }}
			}
		});
		dGridOpt = {
			dataSource: dGridDs
			,scrollable: true
			,columns   : [
					{ field:'CHK'			,title : { key : '4346'		, nullMsg :'체크박스'}		, width:'40px',attributes:{ style:'text-align:center' }
					,template : "<input type='checkbox' class='dGridCheckbox' name='DGRID' #= stRoleMenuAuthMappingId !=null ? checked='checked' : '' #/>"
					,headerTemplate : "<input type='checkbox' id='checkBoxAll2' />"
					,filterable: false
					}
				,{ field:'programAuthCode' ,title : '권한코드'		, width:'40%'	,attributes:{ style:'text-align:left' }}
				,{ field:'programAuthCodeName' ,title : '권한코드 명'		, width:'60%'	,attributes:{ style:'text-align:left' }}
			]
			,noRecordMsg : '메뉴를 선택해주세요'
		};
		dGrid = $('#dGrid').initializeKendoGrid( dGridOpt ).cKendoGrid();

		dGrid.bind("dataBound", function(){
			if(dGrid.dataSource && dGrid.dataSource._view.length > 0){
				fnSetAllCheckbox('dGridCheckbox','checkBoxAll2');
				$('input[name=DGRID]').on("change", function (){
					fnSetAllCheckbox('dGridCheckbox','checkBoxAll2');
				});
			}
		});

		//------------------------------- 왼쪽그리드 E -------------------------------

		fnSearch();
	}

	function fnAGridClick(){
		var aMap = aGrid.dataItem(aGrid.select());
		var data = {stRoleTypeId :aMap.stRoleTypeId};
		bGridDs.read(data);
		cGridDs.data([]);
		dGridDs.data([]);
	};

	function fnBGridClick(){
		var aMap = aGrid.dataItem(aGrid.select());
		var bMap = bGrid.dataItem(bGrid.select());
		if(!aMap) return;
		if(!bMap) return;
		var data = {stRoleTypeId :aMap.stRoleTypeId ,stMenuGroupId : bMap.stMenuGroupId };
		cGridDs.read(data);
		dGridDs.data([]);
	};

	function fnCGridClick(){
		var cMap = cGrid.dataItem(cGrid.select());
		if(!cMap) return;
		var data = {stMenuId : cMap.stMenuId, stRoleTypeId : cMap.stRoleTypeId};
		dGridDs.read(data);
	}
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------


	function fnMenuPopupButton( param ){
		fnKendoPopup({
			id     : 'MENU_POP',
			title  : '메뉴관리',
			src    : '#/stmn0021',
			success: function( id, data ){
				if(param =="search"){
					if(data.MENU_ID){
						$('#condition1').val(data.MENU_ID);
					}
				}else{
					if(data.MENU_ID){
						$('#input4').val(data.MENU_ID);
					}
				}
			}
		});
	};
	function fnPopupButton( param ){
		fnKendoPopup({
			id     : 'SAMPLE_POP',
			title  : '역할팝업',
			src    : '#/stat0031',
			success: function( id, data ){
				if(param =="search"){
					if(data.ST_ROLE_TYPE_ID){
						$('#condition2').val(data.ST_ROLE_TYPE_ID);
					}
				}else{
					if(data.ST_ROLE_TYPE_ID){
						$('#input2').val(data.ST_ROLE_TYPE_ID);
					}
				}
			}
		});
	};

		/**
	 * 콜백합수
	 */
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
				$('#searchForm').bindingForm(data, 'rows', true);
				break;
			case 'save':
			    fnCGridClick();
				fnKendoMessage({message:"저장되었습니다."});
				break;

		}
	}

	function fnRoleMenuAuthListExcelDownload () { // 엑셀다운로드

        var aMap = aGrid.dataItem(aGrid.select());

        //엑셀다운로드 양식을 위한 공통
		if( fnIsEmpty(aMap.stRoleTypeId) ){
    		fnKendoMessage({ message : "역할 그룹명을 선택해주세요."});
    		return false;
    	}

		fnExcelDownload('/admin/st/auth/getRoleMenuAuthListExportExcel', aMap.stRoleTypeId);
    }

	// Resize Scrollbar function & event
	var resizingTimer = null;

	window.addEventListener("resize", function(e) {

		if( resizingTimer ) {
			clearTimeout(resizingTimer);
		}

		resizingTimer = setTimeout(function(){

			setGridHeaderScrollWidth();
			clearTimeout(resizingTimer);

		}, 200);
	})

	function setGridHeaderScrollWidth() {
		var scrollBarWidth = fbGetScrollBarWidth();

			$(".k-grid-header").css("padding-right", scrollBarWidth -1 );
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};

	$scope.fnRoleMenuAuthListExcelDownload = function(){ fnRoleMenuAuthListExcelDownload();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
