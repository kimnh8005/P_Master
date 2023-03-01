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
$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'menu',
			callback : fnUI
		});
		//$('div#ng-view').css('width', '1500px');
	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnTranslate();	// 다국어 변환--------------------------------------------

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew,  #fnClear').kendoButton();
		$('#fnDel,#fnSave').kendoButton({ enable: false });
	}
	function fnSearch(){
		$('#inputForm').formClear(false);
		$('#bGrid').gridClear();
		aGridDs.read({"useYn" :"Y"});
	}
	function fnClear(){
		$('#searchForm').formClear(true);
	}
	function fnNew(){
		bGrid.clearSelection();
		$('#inputForm').formClear(true);
		inputFocus();
		$('input:radio[name="menuType"]:input[value="MENU_TYPE.PAGE"]').prop("checked", true);
		$("input[name='popYn']").attr("disabled", false);
		$(".comSearchPop-program").attr("disabled", false);
		var map = aGrid.dataItem(aGrid.select());
		if(map){
			$('#inputMenuGroup').data("kendoDropDownList").value(map.stMenuGroupId);
			//$('#inputMENU_GROUP').data("kendoDropDownList").value(map.ST_MENU_GRP_ID);
		}
		fnKendoInputPoup({height:"700px" ,width:"700px",title:{key :"5979",nullMsg :'메뉴정보' } });
	}
	function fnSave(){
		var url  = '/admin/st/menu/addMenu';
		var cbId = 'insert';
		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/st/menu/putMenu';
			cbId= 'update'
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
					isAction : 'batch'
			});
		}else{

		}
	}
	function fnDel(){
		fnKendoMessage({message:fnGetLangData({key :"4489",nullMsg :'삭제 하시겠습니까?' }), type : "confirm" ,ok :fnDelApply  });
	}
	function fnDelApply(){
		var url  = '/admin/st/menu/delMenu';
		var cbId = 'delete';
		var data = $('#inputForm').formSerialize(true);
		var map = bGrid.dataItem(bGrid.select());
		if( data.rtnValid ){
			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback(cbId, map);
					},
				isAction : 'delete'
			});
		}else{

		}
	}
	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetDataSource({
			url      : '/admin/st/menu/getMenuGroupNameList'
		});
		aGridOpt = {
			dataSource: aGridDs
			,navigatable: true
			,columns   : [
				{ field:'menuGroupName'	,title : '메뉴그룹명'		, width:'100%'	,attributes:{ style:'text-align:left' }}
			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		$("#aGrid").on("click", "tbody>tr", function () {
				fnGridClick();
		});


		bGridDs = fnGetPagingDataSource({
			url      : '/admin/st/menu/getMenuList',
			pageSize : PAGE_SIZE,
			filter :  {
				filters: [
					{ field: "stMenuGroupId", operator: "eq", value: function(e){
					var aMap= aGrid.dataItem(aGrid.select());
					if(aMap){
						return aMap.stMenuGroupId;
					}else{
						return null;
					}
					}}
				]
				}
		});
		bGridOpt = {
			dataSource: bGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 5
			}
			,filterable: {
					mode: "row"
			}
			,navigatable: true
			,columns   : [
				{ field:'stMenuId'	,title : '메뉴아이디'	, width:'100px',attributes:{ style:'text-align:center' }
					, filterable: {	cell: {	showOperators: false,  enabled: false}}}
				,{ field:'treeMenuName'	,title : '메뉴명'		, width:'20%'	,attributes:{ style:'text-align:left' }
					, template: kendo.template($("#menuNameTpl").html())
					, filterable: {	cell: {	showOperators: false, enabled: false}}}
				,{ field:'menuType'	,title : '메뉴타입'		, width:'100px'	,attributes:{ style:'text-align:center' }
					, template : "#=menuTypeName #"
					, filterable: {	  cell: {
						template: function (args) {
						args.element.kendoDropDownList({
							dataSource : {
									serverFiltering: true,
									transport: {
										read: {
											url     : "/admin/comn/getCodeList"
										},
										parameterMap: function (data, type) {
											return {"stCommonCodeMasterCode" : "MENU_TYPE", "useYn" :"Y"};
										}
									},
									schema: {
							            data  : function(response) {
						        			return response.data.rows
							            }
									}
							},
							dataTextField: "NAME",
							dataValueField: "CODE",
							valuePrimitive: true,
							optionLabel : "All"
						});
					},
					showOperators: false
			}}}
				,{ field:'url'				,title : 'URL'		, width:'15%'	,attributes:{ style:'text-align:left' }
					, filterable: {	cell: {	showOperators: false,  enabled: false}}}
				,{ field:'parentMenuName'		,title : '상위메뉴명'		, width:'150px'	,attributes:{ style:'text-align:left' }
					, filterable: {	cell: {	showOperators: false,  enabled: false}}}
				,{ field:'sort'				,title : '정렬'		, width:'100px'	,attributes:{ style:'text-align:center' }
					, filterable: {	cell: {	showOperators: false,  enabled: false}}}
				,{ field:'popYn'			,title : { key : '4335'	, nullMsg :'팝업여부'}		, width:'100px'	,attributes:{ style:'text-align:center' },template : "#=(popYn=='Y')?fnGetLangData({key :359,nullMsg :'예' }):fnGetLangData({key :360,nullMsg :'아니오' })#"
					, filterable: {	 cell: {
						template: function (args) {
						args.element.kendoDropDownList({
							dataSource :[
								{ CODE:"Y",NAME:fnGetLangData({key:"359",nullMsg:"예"}) },
								{ CODE:"N",NAME:fnGetLangData({key:"360",nullMsg:"아니오"}) }
							],
							dataTextField: "NAME",
							dataValueField: "CODE",
							valuePrimitive: true,
							optionLabel : "All"
						});
					},
					showOperators: false}}}
				,{ field:'useYn'		,title : '사용여부'		, width:'100px'	,attributes:{ style:'text-align:center' } ,template : "#=(useYn=='Y')?'예' :'아니오' #"
					, filterable: {	 cell: {
						template: function (args) {
						args.element.kendoDropDownList({
							dataSource :[
								{ CODE:"Y",NAME:"예"},
								{ CODE:"N",NAME:"아니오"}
							],
							dataTextField: "NAME",
							dataValueField: "CODE",
							valuePrimitive: true,
							optionLabel : "All"
						});
					},
					showOperators: false}}}
					,{ field:'addAuthorizationCheckData'	 ,hidden : true}
					,{ field:'comment'				 ,hidden : true}
					,{ field:'stMenuGroupId'				 ,hidden : true}
			]
		};
		bGrid = $('#bGrid').initializeKendoGrid( bGridOpt ).cKendoGrid();

		$("#bGrid").on("click", "tbody>tr", function () {
				fnRGridClick();
		});
		fnSearch();

	}


	function fnGridClick(){
//		bGridDs.fetch(function(){
//			var aMap= aGrid.dataItem(aGrid.select());
//			if(!aMap){
//				return;
//			}
//			var data = {"stMenuGroupId":aMap.stMenuGroupId};
//			bGridDs.filter( fnSearchData(data) );
//		});
		var aMap= aGrid.dataItem(aGrid.select());
		if(!aMap){
			return;
		}
		var data = {"stMenuGroupId":aMap.stMenuGroupId};
		bGridDs.filter( fnSearchData(data) );
	};

	function fnRGridClick(){

		var bMap = bGrid.dataItem(bGrid.select());
		fnAjax({
			url     : '/admin/st/menu/getMenu',
			params  : {stMenuId : bMap.stMenuId},
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'select'
		});
	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});
		//fnTagMkRadioYN({id: "condiActive" , tagId : "USE_YN",chkVal: 'Y'});
		fnTagMkRadioYN({id: "intputActive" , tagId : "useYn",chkVal: 'Y'});
		fnTagMkRadioYN({id: "intputPopupYN" , tagId : "popYn",chkVal: 'N'});
		fnKendoDropDownList({
			id    : 'inputBusinessType',
			url : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "1", "useYn" :"Y"},
			textField :"NAME",
			valueField : "CODE"
		});

		fnTagMkRadio({
			id    : 'menuType',
			tagId : 'menuType',
			url : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "MENU_TYPE", "useYn" :"Y"},
			/*textField :"NAME",
			valueField : "CODE"
			,tagId : "menuType"
			,chkVal :'01'*/
			async : false,
			chkVal: 'MENU_TYPE.PAGE',
			style : {}
		});

		$('input[name=menuType]').on("change",  function () {

			if($(this).val() =='MENU_TYPE.FOLDER'){
				$('#programId').val('0');
				$('#programName').val("폴더");
				$(".comSearchPop-program").attr("disabled", true);
				$('.upper_folder_th').removeClass('must');
				$('.upper_folder').removeAttr('required');
				$('.upper_folder_search_button').addClass('k-state-disabled');
				$('.upper_folder_search_button').attr('disabled', 'disabled');
				$('#searchParentMenuId').val('0');
				$("input[name='popYn']").eq(1).prop("checked", true);
				$("input[name='popYn']").attr("disabled", true);
			}else{
				$('#programId').val('');
				$('#programName').val('');
				$(".comSearchPop-program").attr("disabled", false);
				$('.upper_folder_th').addClass('must');
				$('.upper_folder').attr('required', 'required');
				$('.upper_folder_search_button').removeClass('k-state-disabled');
				$('.upper_folder_search_button').removeAttr('disabled', 'disabled');
				$('#searchParentMenuId').val('');
				$("input[name='popYn']").eq(0).prop("checked", true);
				$("input[name='popYn']").attr("disabled", false);
			}

			$("#sort").val("");
			$("input[name='parentMenuName']").val("");
		});
		fnKendoDropDownList({
			id    : 'inputMenuGroup',
			url : "/admin/st/menu/getMenuGroupNamePopList",
			params : {"useYn" :"Y"},
			textField :"menuGroupName",
			valueField : "stMenuGroupId"
		});

		$('#inputMenuGroup').data('kendoDropDownList').bind('change' ,function(e){
			$('#searchParentMenuId').val('');
			$('#input4').val('');
		});

		/*fnAjax({
				url     : '/biz/st/menu/getsValueList',
				success :
						function( data ){
							fnKendoDropDownList({
								id    : 'SCH_BIZ_TYPE',
								data  : data.bizTypeList,
								textField :"NAME",
								valueField : "CODE",
								blank : "All"
							});

							fnKendoDropDownList({
								id    : 'inputBusinessType',
								data  : data.bizTypeList,
								textField :"NAME",
								valueField : "CODE"
							});

							fnKendoDropDownList({
								id    : 'SCH_MENU_GROUP',
								data  : data.menuGroupList,
								textField :"MENU_GRP_NAME",
								valueField : "ST_MENU_GRP_ID",
								blank : "All"
							});

							fnKendoDropDownList({
								id    : 'inputMenuGroup',
								data  : data.menuGroupList,
								textField :"MENU_GRP_NAME",
								valueField : "ST_MENU_GRP_ID"
							});
							$('#inputMenuGroup').data('kendoDropDownList').bind('change' ,function(e){
								$('#searchParentMenuId').val('');
								$('#input4').val('');
							});
							fnTagMkRadio({
									id    :  "menuType"
									,data  : data.menuTypeList
									,tagId : "menuType"
									,chkVal :'01'
									,style : {}
							});
							$('input[name=menuType]').on("change",  function () {
								if($(this).val() =='02'){
									$('#programId').val('0');
									$('#programName').val('폴더');
								}else{
									$('#programId').val('');
									$('#programName').val('');
								}
							});

						},
					isAction : 'select'
		});*/
		$('#sort').forbizMaskTextBox({fn:'onlyNum'});
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
				var menuType = data.rows.menuType;

				if( menuType === "MENU_TYPE.PAGE" ) {
					$(".comSearchPop-program").attr("disabled", false);
					$("input[name='popYn']").attr("disabled", false);
					$('.upper_folder_th').addClass('must');
 					$('.upper_folder').attr('required', true);
				} else if ( menuType === "MENU_TYPE.FOLDER" ) {
					$(".comSearchPop-program").attr("disabled", true);
					$("input[name='popYn']").attr("disabled", true);
					$('.upper_folder_th').removeClass('must');
 					$('.upper_folder').removeAttr('required');
				}


				$('#inputForm').bindingForm(data, 'rows', true);
				fnKendoInputPoup({height:"700px" ,width:"700px",title:{key :"5980",nullMsg :'메뉴정보' } });
				break;
			case 'insert':
				fnKendoMessage({
					message:"등록되었습니다.",
					ok:function(){
						fnSearch();
						fnClose();
					}
				});
				break;
			case 'update':
				fnKendoMessage({
					message:"수정되었습니다.",
					ok:function(){
						fnSearch();
						fnClose();
					}
				});
				break;
			case 'delete':
				fnKendoMessage({message : '삭제되었습니다.',
					ok:function(){
						fnSearch();
						fnClose();
						}
				});
				break;
		}
	}

	function fnMenuPopupButton( param ){
		fnKendoPopup({
			id     : 'MENU_POP',
			title  : '상위메뉴관리',
			src    : '#/pMenuPopup',
			width  : '1000px',
			height : '650px',
			resizable: false,
			minWidth: 950,
			param  : { "stMenuGroupId" : $('#inputMenuGroup').val()},
			success: function( stMenuId, data ){
				if(data.stMenuId){
					$('#input4').val(data.menuName);
					$('#searchParentMenuId').val(data.stMenuId);
				/*	$('#inputBusinessType').data('kendoDropDownList').value(data.businessType);
					$('#inputMenuGroup').data('kendoDropDownList').value(data.stMenuGroupId);*/
				}
			}
		});
	};
function fnPopupButton( param ){

	fnKendoPopup({
		id     : 'SAMPLE_POP',
		title  : '프로그램',
		src    : '#/pgmPopup',
		width  : '950px',
		height : '650px',
		minWidth: 850,
		success: function( id, data ){
			if(param =="search"){
				if(data.programId){
					$('#condition3').val(data.stProgramId);
				}
			}else{
				if(data.programId){
					$('#programId').val(data.stProgramId);
					$('#programName').val(data.programName);
				}
			}
		}
	});
};
	function fnPopupDicButton( param ){
	fnKendoPopup({
			id     : 'SAMPLE_POP',
			title  : fnGetLangData({key :"4620",nullMsg :'표준용어 팝업' }),
			width  : '900px',
			height : '600px',
			src    : '#/dicMstPopup',
			param  : { "MENU_TYPE" : "word"},
			success: function( id, data ){
				if(data.id){
					$('#input6').val(data.id);
					$('#baseName').val(data.baseName);
				}
			}
		});
};

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

	$scope.fnPopupButton = function( param ){ fnPopupButton(param); };

	$scope.fnMenuPopupButton = function( param ){ fnMenuPopupButton(param); };

	$scope.fnPopupDicButton = function( param ){ fnPopupDicButton(param); };

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
