/**-----------------------------------------------------------------------------
 * system 			 : BOS 사유 관리 팝업
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.20		천혜현          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 10;
var aGridDs, aGridOpt, aGrid;
var bGridDs, bGridOpt, bGrid;
var cGridDs, cGridOpt, cGrid;

$(document).ready(function() {
	importScript("/js/controllers/admin/ps/claim/policyClaimCtgryPopupGridColumns.js", function (){
		fnInitialize();
	});

	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });
		fnPageInfo({ PG_ID  : 'policyClaimCtgryPopup', callback : fnUI});
	}

	function fnUI(){
		fnInitButton();
		fnInitGrid();
		fnInitOptionBox();
		fnSearch('aTab');
	}
	//--------------------------------- Button Start---------------------------------
	function fnInitButton(){
		$('#fnSearch, #fnNew, #fnSave,  #fnClear').kendoButton();
	}
	function fnSearch(param){
		var data = new Object();

		data.categoryCode = fnGetGridCategoryCode(param);

		var query = {
				page         : 1,
				pageSize     : PAGE_SIZE,
				filterLength : fnSearchData(data).length,
				filter :  {
					filters : fnSearchData(data)
				}
		};

		if(param == 'aTab'){
			aGridDs.query(query);
		}else if(param == 'bTab'){
			bGridDs.query(query);
		}else{
			cGridDs.query(query);
		}

	}
	function fnSave(param){

		var changeCnt = 0;
		var insertData = fnEGridDsExtract(param, 'insert');
		var updateData = fnEGridDsExtract(param, 'update');
		var deleteData = fnEGridDsExtract(param, 'delete');
		var isSubmit = true;

		for(let i=0; i<insertData.length; i++){
			if(insertData[i].claimName=="" ){
				fnKendoMessage({message : "클레임 사유를 입력해주세요."});
				isSubmit = false;
				return;
			}
		}

		// 귀책처일 경우
		if(param == 'cGrid'){
			if(insertData.length > 0){
				for(let i=0; i<insertData.length; i++){
					if(insertData[i].targetTypeName=="" ){
						fnKendoMessage({message : "귀책 유형을 선택해주세요."});
						isSubmit = false;
						return;
					}
				}
			}

			if(updateData.length > 0){
				for(let i=0; i<updateData.length; i++){
					if(updateData[i].targetTypeCode == "" ){
						fnKendoMessage({message : "귀책 유형을 선택해주세요."});
						isSubmit = false;
						return;
					}
				}
			}
		}

		changeCnt = insertData.length + updateData.length +deleteData.length ;

		if(changeCnt == 0){
			fnKendoMessage({message :  fnGetLangData({key :"4355",nullMsg :'데이터 변경사항이 없습니다.' }) });
			isSubmit = false;
			return ;
		}else{
			var data = {"insertData" : kendo.stringify(insertData)
						,"updateData" : kendo.stringify(updateData)
						,"deleteData" : kendo.stringify(deleteData)
			};
		}

		data.categoryCode = fnGetGridCategoryCode(param);

		if(isSubmit){
			fnAjax({
				url     : '/admin/policy/claim/savePsClaimCtgry',
				params  : data,
				success : function( data ){
					fnBizCallback("save", param);
				},
				fail : function(data, resultcode){
                	fnKendoMessage({
                        message : resultcode.message,
                        ok : function(e) {
                        	fnBizCallback("fail", data);
                        }
                    });
				},
				isAction : 'batch'
			});
		}
	}
	function fnClose(){
		parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
	}

	function fnGetGridCategoryCode(param){
		if(param == 'aGrid' || param == 'aTab'){
			return "10";
		}else if(param == 'bGrid' || param == 'bTab'){
			return "20";
		}else{
			return "30";
		}
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitaGrid(){
		aGridDs = fnGetEditPagingDataSource({
			url      	  : '/admin/policy/claim/getPsClaimCtgryList',
			pageSize      : PAGE_SIZE,
			model_id      : 'claimName',
			model_fields  : { claimName : gridUtil.claimNameOption()}
		});

		aGridOpt = {
			dataSource	 : aGridDs
			,pageable	 : { buttonCount: 10 , responsive: false}
			,toolbar     : gridUtil.toolbar()
			,navigatable : true
			,columns     : gridUtil.aList()
			,editable    : {confirmation: function(model) { return '삭제하시겠습니까?'}}
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		$('#aGrid').find('div.k-grid-toolbar').css({'right' : 0});
		$(".k-custom-save").click(function(e){
			e.preventDefault();
			if($(this).parent('div').parent('div').attr('id') == 'aGrid'){
				fnSave('aGrid');
			}
		});
	}

	function fnInitbGrid(){
		bGridDs = fnGetEditPagingDataSource({
			url      	  : '/admin/policy/claim/getPsClaimCtgryList',
			pageSize      : PAGE_SIZE,
			model_id      : 'claimName',
			model_fields  : { claimName : gridUtil.claimNameOption()}
		});

		bGridOpt = {
			dataSource	 : bGridDs
			,pageable	 : { buttonCount: 10 , responsive: false}
			,toolbar     : gridUtil.toolbar()
			,navigatable : true
			,columns     : gridUtil.bList()
			,editable    : {confirmation: function(model) { return '삭제하시겠습니까?'}}
		};
		bGrid = $('#bGrid').initializeKendoGrid( bGridOpt ).cKendoGrid();
		$('#bGrid').find('div.k-grid-toolbar').css({'right' : 0});
		$(".k-custom-save").click(function(e){
			e.preventDefault();
			if($(this).parent('div').parent('div').attr('id') == 'bGrid'){
				fnSave('bGrid');
			}
		});
	}

	function fnInitcGrid(){
		cGridDs = fnGetEditPagingDataSource({
			url      	  : '/admin/policy/claim/getPsClaimCtgryList',
			pageSize      : PAGE_SIZE,
			model_id      : 'claimName',
			model_fields  : { claimName : gridUtil.claimNameBySCtgryOption()
				,targetType: gridUtil.targetTypeOption()
			}
		});

		cGridOpt = {
			dataSource	 : cGridDs
			,pageable	 : { buttonCount: 10 , responsive: false}
			,toolbar     : gridUtil.toolbar()
			,navigatable : true
			,columns     : gridUtil.cList()
			,editable    : {confirmation: function(model) { return '삭제하시겠습니까?'}}
		};
		cGrid = $('#cGrid').initializeKendoGrid( cGridOpt ).cKendoGrid();
		$('#cGrid').find('div.k-grid-toolbar').css({'right' : 0});
		$(".k-custom-save").click(function(e){
			e.preventDefault();
			if($(this).parent('div').parent('div').attr('id') == 'cGrid'){
				fnSave('cGrid');
			}
		});
	}

	function fnInitGrid(){
		fnInitaGrid(); // 클레임 사유(대) 그리드
		fnInitbGrid(); // 클레임 사유(중) 그리드
		fnInitcGrid(); // 귀책처 그리드
	}
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		var tabStrip = $("#tabstrip").kendoTabStrip({
			animation: false,
			activate : function(e) {
				var thisId = $(e.item).attr('id');
				fnSearch(thisId);
		}}).data("kendoTabStrip");
		tabStrip.activateTab( $('#aTab') );

		$(".k-item.k-state-default").css("width","215px");
	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
				$('#searchForm').bindingForm(data, 'rows', true);
				break;
			case 'save':
				fnKendoMessage({message : fnGetLangData({nullMsg :'저장되었습니다.' }) ,ok :function(e){
					if(data == 'aGrid'){
						fnSearch('aTab');
					}else if(data == 'bGrid'){
						fnSearch('bTab');
					}else{
						fnSearch('cTab');
					}
				}});;
				break;
			case 'fail':
				$(".k-grid-cancel-changes").click();
				break;
		}
	}
	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function(param) {	fnSearch(param);};
	/** Common Save*/
	$scope.fnSave = function(param){ fnSave(param);};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
