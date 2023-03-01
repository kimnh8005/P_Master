/**-----------------------------------------------------------------------------
 * description 		 : 메뉴관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2017.01.10		최봉석          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var bGridDs, bGridOpt, bGrid;
var cGridDs, cGridOpt, cGrid;
var dGridDs, dGridOpt, dGrid;
var aGridSelectedRow, bGridSelectedRow;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'menuUrl',
			callback : fnUI
		});
	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnTranslate();	// 다국어 변환--------------------------------------------

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew,#fnSave,#fnClear, #fnDataMoveRtoL,#fnDataMoveLtoR').kendoButton();
		$('#fnDel').kendoButton({ enable: false });
	}
	function fnSearch(){
		$('#bGrid').gridClear();
		$('#cGrid').gridClear();
		$('#dGrid').gridClear();

		var query = {
			page         : 1,
			pageSize     : PAGE_SIZE
		};
		aGridDs.query( query );
	}
	function fnClear(){
		$('#searchForm').formClear(true);
	}
	function fnNew(){
		bGrid.clearSelection();
		inputFocus();
		$('input:radio[name="menuType"]:input[value="01"]').prop("checked", true);
		var map = aGrid.dataItem(aGrid.select());
		if(map){
			$('#inputMENU_GROUP').data("kendoDropDownList").value(map.ST_MENU_GRP_ID);
		}
		fnKendoInputPoup({height:"335px" ,width:"500px",title:{key :"5979",nullMsg :'메뉴관리 등록' } });
	}
	function fnSave(){
		var map = bGrid.dataItem(bGrid.select());

		var insertData = kendo.stringify(fnEGridDsExtract('dGrid', 'insert'));
		var deleteData = kendo.stringify(fnEGridDsExtract('dGrid', 'delete'));

		var data = {"insertData" : insertData, "deleteData" : deleteData, "stProgramAuthId" : map.stProgramAuthId};

		fnAjax({
				url     : '/admin/st/menu/saveMenuAssignUrl',
				params  : data,
				success :
					function( data ){
						fnBizCallback("save", data);
					},
				isAction : 'batch'
		});
	}
	function fnClose(){

	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){

		aGridDs = fnGetPagingDataSource({
			url      : 	'/admin/st/pgm/getProgramNameList',
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: PAGE_SIZE,
				buttonCount : 5,
				responsive : false
			}, filterable: {
					mode: "row"
			}
			,navigatable: true
			,columns   : [
				{ field:'programId'	,title : '프로그램 ID'		, width:'40%'	,attributes:{ style:'text-align:left' } , filterable: {	cell: {	showOperators: false,  enabled: true}}},
				{ field:'programName'	,title : '프로그램명'		, width:'50%'	,attributes:{ style:'text-align:left' } , filterable: {	cell: {	showOperators: false,  enabled: true}}}
			]
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		$("#aGrid").on("click", "tbody>tr", function () {
				fnGridClick();
		});


		bGridDs = fnGetDataSource({
			url      : '/admin/st/pgm/getProgramAuthUseList',
			pageSize : PAGE_SIZE
		});

		bGridOpt = {
			dataSource: bGridDs
			,navigatable: true
			,columns   : [
				{ field:'programAuthCode'	,title : '권한 코드'		, width:'40%'	,attributes:{ style:'text-align:left' }},
				{ field:'programAuthCodeName'	,title : '권한 코드 명'		, width:'60%'	,attributes:{ style:'text-align:left' }}
			]
		};
		bGrid = $('#bGrid').initializeKendoGrid( bGridOpt ).cKendoGrid();

		$("#bGrid").on("click", "tbody>tr", function () {
				fnBGridClick();
		});

		cGridDs = fnGetDataSource({
			url      : '/admin/st/menu/getMenuUrlList'
		});
		cGridOpt = {
			dataSource: cGridDs
			,filterable: {
				mode: "row"

			}
			,navigatable: true
			,scrollable: true
			,columns   : [
				{ field:'CHK'			,title : { key : '4346'		, nullMsg :'체크박스'}		, width:'40px',attributes:{ style:'text-align:center' }
					,template : "<input type='checkbox' class='cGridCheckbox' name='CGRID'/>"
					,headerTemplate : "<input type='checkbox' id='checkBoxAll1' />"
					, filterable: false
				}
				,{ field:'url'	,title : '시스템 URL'		, width:'60%'	,attributes:{ style:'text-align:left' }, filterable: { cell: {delay: 99999999, showOperators: false, operator: "contains"}}}
				,{ field:'urlName'	,title : '시스템 URL 명'		, width:'30%'	,attributes:{ style:'text-align:left' }, filterable: {	cell: {delay: 99999999, showOperators: false, operator: "contains" }}}
				,{ field:'stMenuUrlId', hidden:true}
			]
			,noRecordMsg : fnGetLangData({key : '5920', nullMsg : '검색결과가 없습니다.' })
		};
		cGrid = $('#cGrid').initializeKendoGrid( cGridOpt ).cKendoGrid();

		cGrid.bind("dataBound", function(){
			if(cGrid.dataSource && cGrid.dataSource._view.length > 0){
				fnSetAllCheckbox('cGridCheckbox','checkBoxAll1');
				$('input[name=CGRID]').on("change", function (){
					fnSetAllCheckbox('cGridCheckbox','checkBoxAll1');
				});
			}
		});
		$(cGrid.tbody).on("click", "td", function (e) {
				var row = $(this).closest("tr");
				var rowIdx = $("tr", cGrid.tbody).index(row);
				var colIdx = $("td", row).index(this);
				if(colIdx>0){
					fnCGridClick($(e.target).closest('tr'));
				}
			});

		dGridDs = fnGetEditDataSource({
			url      :'/admin/st/menu/getMenuAssignUrlList',
			model_id     : 'stProgramAuthUrlMappingId',
			model_fields : {
				stProgramAuthUrlMappingId	: { editable: false	, type: 'number', validation: { required: false  }}
				,stMenuUrlId 		: { editable: false	, type: 'number', validation: { required: true  }}
				,url		: { editable: false	, type: 'string', validation: { required: true  } }
				,urlName		: { editable: false	, type: 'string', validation: { required: true  } }
			}
		});
		dGridOpt = {
			dataSource: dGridDs
			,navigatable: true
			,scrollable: true
			,columns   : [
					{ field:'CHK'			,title : { key : '4346'		, nullMsg :'체크박스'}		, width:'40px',attributes:{ style:'text-align:center' }
					,template : "<input type='checkbox' class='dGridCheckbox' name='DGRID'/>"
					,headerTemplate : "<input type='checkbox' id='checkBoxAll2' />"
					,filterable: false
					}
				,{ field:'url'	,title : '시스템 URL'		, width:'60%'	,attributes:{ style:'text-align:left' }}
				,{ field:'urlName'	,title : '시스템 URL 명'		, width:'30%'	,attributes:{ style:'text-align:left' }}
			]
			,noRecordMsg : fnGetLangData({key : '5920', nullMsg : '검색결과가 없습니다.' })
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
		$(dGrid.tbody).on("click", "td", function (e) {
			var row = $(this).closest("tr");
			var rowIdx = $("tr", dGrid.tbody).index(row);
			var colIdx = $("td", row).index(this);
			if(colIdx>0){
				fnDGridClick($(e.target).closest('tr'));
			}
		});

		fnSearch();

	}

	function fnGridClick(){
		let iscGridChanged = $("#cGrid").data('kendoGrid').dataSource.hasChanges();
		let isdGridChanged = $("#dGrid").data('kendoGrid').dataSource.hasChanges();

		if(iscGridChanged || isdGridChanged) {
			fnKendoMessage({message: '변경된 데이터가 있습니다 저장하지 않고 나가시겠습니까?', type: "confirm", ok: fnMoveaGrid, cancel:function(){
				aGrid.select("tr:eq("+ (aGridSelectedRow+1) + ")");
			}
			})
		} else {
			fnMoveaGrid();
		}
	}

	function fnMoveaGrid() {
		var aMap= aGrid.dataItem(aGrid.select());
		bGridDs.read({"stProgramId":aMap.stProgramId});

		cGridDs.cancelChanges();
		dGridDs.cancelChanges();
		cGridDs.data([]);
		dGridDs.data([]);

		aGridSelectedRow = aGrid.select().index()
	}

	function fnBGridClick(){

		let iscGridChanged = cGridDs.hasChanges();
		let isdGridChanged = dGridDs.hasChanges();

		if(iscGridChanged || isdGridChanged) {
			fnKendoMessage({message: '변경된 데이터가 있습니다 저장하지 않고 나가시겠습니까?', type: "confirm", ok: fnMovebGrid, cancel:function(){
				bGrid.select("tr:eq("+ (bGridSelectedRow+2) + ")");
			}
			})
		} else {
			fnMovebGrid();
		}
	}

	function fnMovebGrid() {
		var bMap= bGrid.dataItem(bGrid.select());
		var data = {stProgramAuthId :bMap.stProgramAuthId};
		cGridDs.read(data);
		dGridDs.read(data);
		bGridSelectedRow = bGrid.select().index()
	}

	function fnCGridClick(param){
		var clickRowCheckBox = param.find('input[type=checkbox]');
		if(clickRowCheckBox.prop('checked')){
			clickRowCheckBox.prop('checked', false);
		}else{
			clickRowCheckBox.prop('checked', true);
		}
		fnSetAllCheckbox('cGridCheckbox','checkBoxAll1');
	}

	function fnDGridClick(param){
		var clickRowCheckBox = param.find('input[type=checkbox]');
		if(clickRowCheckBox.prop('checked')){
			clickRowCheckBox.prop('checked', false);
		}else{
			clickRowCheckBox.prop('checked', true);
		}
		fnSetAllCheckbox('dGridCheckbox','checkBoxAll2');
	}
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	function inputFocus(){
		$('#input2').focus();
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
				//form data binding
				fnKendoInputPoup({height:"335px" ,width:"500px",title:{key :"5980",nullMsg :'메뉴관리 수정' } });
				break;
			case 'insert':
				if(data.rows =="DUP_DATA"){
					fnKendoMessage({message : fnGetLangData({key :"370",nullMsg :'중복입니다.' })  ,ok :inputFocus});
				}else{
					cGridDs.insert(data.rows);
					fnKendoMessage({message : fnGetLangData({key :"369",nullMsg :'입력되었습니다.' }) ,ok :fnNew});
				}
				break;
			case 'save':
				fnKendoMessage({message : '저장되었습니다.' });
				fnMovebGrid();
				break;

		}
	}

	function fnDataMoveLtoR(){
		var selectRows 	= cGrid.tbody.find('input[class=cGridCheckbox]:checked').closest('tr');
		if(selectRows){
			for(var i =0; i< selectRows.length;i++){
				var cMap = cGrid.dataItem($(selectRows[i]));
				var insertData = {"url": cMap.url ,"urlName": cMap.urlName, "stMenuUrlId": cMap.stMenuUrlId};
				dGridDs.add(insertData);
			}
			for(var i =0; i< selectRows.length;i++){
				cGrid.removeRow($(selectRows[i]));
			}
		}
	};

	function fnDataMoveRtoL(){
		var selectRows 	= dGrid.tbody.find('input[class=dGridCheckbox]:checked').closest('tr');
		if(selectRows){
			for(var i=0; i< selectRows.length; i++){
				var dMap = dGrid.dataItem($(selectRows[i]));
				var insertData = {"url": dMap.url ,"urlName": dMap.urlName, "stMenuUrlId": dMap.stMenuUrlId};
				cGridDs.add(insertData);
			}
			for(var i=0; i< selectRows.length; i++){
				dGrid.removeRow($(selectRows[i]));
			}
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

	$scope.fnDataMoveLtoR = function( ) {fnDataMoveLtoR();};
	$scope.fnDataMoveRtoL = function( ) {fnDataMoveRtoL();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
