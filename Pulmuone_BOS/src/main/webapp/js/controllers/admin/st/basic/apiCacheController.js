/**-----------------------------------------------------------------------------
 * description 		 : API데이타캐쉬관리
 * @
 * @ ------------------------------------------------------
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.05		김경민          최초생성
 * @
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
			PG_ID  : 'apiCache',
			callback : fnUI
		});
		//$('div#ng-view').css('width', '1500px');
	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnPreventSubmit();

		fnSearch();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew,#fnSave,  #fnClear').kendoButton();
	}



	function fnSearch(){
		var data = $('#searchForm').formSerialize(true);
//		console.dir("===>"+JSON.stringify(data));
		var query = {
				page         : 1,
				pageSize     : PAGE_SIZE,
				filterLength : fnSearchData(data).length,
				filter :  {
					filters : fnSearchData(data)
				}
		};
		aGridDs.query(query);
	}


	function fnClear(){
		$('#searchForm').formClear(true);
	}
	function fnNew(){
		aGrid.clearSelection();
		$('#inputForm').formClear(true);
		inputFocus();
	}
	function fnSave(){
		var changeCnt =0;

		var insertData = fnEGridDsExtract('aGrid', 'insert');
		var result = fnCheckValidaion(insertData);
		if( !result.success ){
			fnKendoMessage({message :  result.message });
			return;
		}
		var updateData = fnEGridDsExtract('aGrid', 'update');
		result = fnCheckValidaion(updateData);
		if( !result.success ){
			fnKendoMessage({message :  result.message });
			return;
		}
		var deleteData = fnEGridDsExtract('aGrid', 'delete');
		changeCnt = insertData.length + updateData.length +deleteData.length ;

		if(changeCnt ==0){
			fnKendoMessage({message :  fnGetLangData({key :"4355",nullMsg :'데이터 변경사항이 없습니다.' }) });
			return ;
		}else{
			var data = {"insertData" : kendo.stringify(insertData)
						,"updateData" : kendo.stringify(updateData)
						,"deleteData" : kendo.stringify(deleteData)
			};

			fnAjax({
				url     : '/admin/st/apiCache/saveApiCache',
				params  : data,
				success :
					function( data ){
						fnBizCallback("save", data);
					},
				error : function (xhr){
					fnKendoMessage({message : fnGetLangData({nullMsg :'' })});
				},
				isAction : 'batch'
			});
		}
	}

	function fnCheckValidaion(list){
		var result = {success: true, message: ''};
		$.each(list, function (i, data){
			if(data.apiUrl.indexOf(" ") >= 0){
				result.success = false;
				result.message = "`" + data.apiUrl + "` 에 공백문자가 포함되어 있습니다.";
				return result;
			}

			if(data.apiUrl.length == 0){
				result.success = false;
				result.message = "API URL은  필수 입력입니다.";
				return result;
			}
            var apiUrlArr = data.apiUrl.split(",");
            for(i = 0 ; i < apiUrlArr.length; i++){
                if(apiUrlArr[i].length > 255){
                    result.success = false;
                    result.message = "API URL은 최대 255자까지 입력 가능합니다.";
                    return result;
                }
            }

			if(data.casheFilePath.indexOf(" ") >= 0){
				result.success = false;
				result.message = "`" + data.casheFilePath + "` 에 공백문자가 포함되어 있습니다.";
				return result;
			}

			if(data.casheFilePath.length == 0){
				result.success = false;
				result.message = "cache 파일 경로는 필수 입력입니다.";
				return result;
			}

			var casheFilePathArr = data.casheFilePath.split(",");
			for(i = 0 ; i < casheFilePathArr.length; i++){
				if(casheFilePathArr[i].length > 255){
					result.success = false;
					result.message = "동의어는 최대 255자까지 입력 가능합니다.";
					return result;
				}
			}
		})
		return result;

	}

	function fnClose(){

	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetEditPagingDataSource({
			url      : '/admin/st/apiCache/getApiCacheList',
			pageSize       : PAGE_SIZE,
			model_id     : 'stApiCacheId',  //model_id 는 pk역할을 해서 수정모드에서 활성화가 안된다.
			model_fields : {
				 no			     : { editable: false , type: 'string', validation: { required: false }}
		     	,apiUrl	         : { editable: true	 , type: 'string', validation: { required: true , maxLength:"255"}}
				,casheFilePath	 : { editable: true	 , type: 'string', validation: { required: true , maxLength:"255"}}
		        ,casheData       : { editable: false , type: 'string', validation: { required: false }}
		        ,casheTime       : { editable: false , type: 'string', validation: { required: false }}
		        ,memo            : { editable: true  , type: 'string', validation: { required: false , maxLength:"255"}}
				,useYn			 : { editable: true	 , type: 'string', validation: { required: true }	, defaultValue : 'Y' }
				,goodorbad       : { editable: false , type: 'string', validation: { required: false }}
			}
		});

		aGridOpt =
		{
			dataSource: aGridDs,
			toolbar   :
						fnIsProgramAuth("ADD") && !fnIsProgramAuth("SAVE_DELETE") ? [{ name: 'create', text: '신규', className: "btn-point btn-s"},
								{ name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }]:
							fnIsProgramAuth("SAVE_DELETE") && !fnIsProgramAuth("ADD") ? [{ name: 'cSave',  text: '저장', imageClass: "k-i-update", className: 'k-custom-save btn-point btn-s'},
									{ name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }]:
								fnIsProgramAuth("ADD") && fnIsProgramAuth("SAVE_DELETE") ? [{ name: 'create', text: '신규', className: "btn-point btn-s"},
										{ name: 'cSave',  text: '저장', imageClass: "k-i-update", className: 'k-custom-save btn-point btn-s'},
										{ name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }]:
									[{ name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }],
			pageable :
			{
				 refresh: true
				,pageSizes: [20, 30, 50]
				,buttonCount: 5
			},
			//editable  : true,
			editable:{confirmation: function(model) {
		        return '삭제하시겠습니까?'
		    }},
			//height: 550,
			columns   :
			[
				{ field:'no'			 ,title : 'No'		              , width:'30px',attributes:{ style:'text-align:center'} ,locked: true, lockable: false }
				,{ field:'apiUrl'        ,title : 'cache 적용 url'		  , width:'200px',attributes:{ style:'text-align:left' }, validation: { required: true , maxLength:"255"}}
				,{ field:'casheFilePath' ,title : 'cache 파일 경로'		  , width:'200px',attributes:{ style:'text-align:left' }, validation: { required: true , maxLength:"255"}}
				,{ field:'casheData'     ,title : 'api url response json' , width:'200px',attributes:{ style:'text-align:left;white-space: nowrap;' }}
				,{ field:'casheTime'     ,title : 'cache 시간'             , width:'200px',attributes:{ style:'text-align:center', class:'forbiz-cell-readonly'  }}
				,{ field:'memo'          ,title : '메모'                   , width:'200px',attributes:{ style:'text-align:left' }, validation: { required: false , maxLength:"255"}}
				,{ field:'useYn'         ,title : '사용유무'		          , width:'75px',attributes:{ style:'text-align:center' }
				,template : "#=(useYn=='Y')?fnGetLangData({nullMsg :'예' }):fnGetLangData({nullMsg :'아니오' })#"
				,editor: function(container, options) {
					var input = $("<input id='psGrpDropDown' value='"+options.model.useYn+"'   selected='selected' />");
					input.appendTo(container);
					fnKendoDropDownList({
						id    : 'psGrpDropDown',
						data  : [
							{"CODE":"Y"	,"NAME":"예"},
							{"CODE":"N"	,"NAME":"아니오"}
						],
						textField :"NAME",
						valueField : "CODE",
						value :	options.model.useYn
					});

					$('#psGrpDropDown').val(options.model.useYn);
					//alert($('#psGrpDropDown').val())
					$('#psGrpDropDown').unbind('change').on('change', function(){
						var dataItem = aGrid.dataItem($(this).closest('tr'));
						var shopDropDownList =$('#psGrpDropDown').data('kendoDropDownList');
						dataItem.set('useYn', shopDropDownList.value());
						dataItem.set('CODE', shopDropDownList.value());
						dataItem.set('NAME', shopDropDownList.text());
					});

					}
			    }
				,{ command: [{name:'destroy'	,text:'삭제', className: 'btn-red btn-s',   visible: function() { return fnIsProgramAuth("SAVE_DELETE") }}]		,title : '관리'        , width: '120px', attributes:{ style:'text-align:center'  , class:'forbiz-cell-readonly' }}
				,{ field:'stApiCacheId'   	,hidden: true }

			],
			forbizEditCustm : fnEditCustm
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
		aGrid.bind('dataBound', function(){
			$('#totalCnt').text(aGridDs._total);
		});

		$(".k-custom-save").click(function(e){
			e.preventDefault();
			fnSave();
		});

		$(".k-grid-toolbar").addClass("k-align-with-pager-sizes");
	}
    function columnTemplateFunction(dataItem) {
        var input = '<input class="dropDownTemplate"/>'

        return input
    };
	function fnGridClick(){
		var map = aGrid.dataItem(aGrid.select());

		$('#inputForm').bindingForm( {'rows':map},'rows', true);
	};
	//Initialize End End
	function fnEditCustm(e){
		return;
	}
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		fnTagMkRadio({
			id    :  'useYn',
			tagId : 'useYn',
			chkVal: '',
			data  : [   { "CODE" : ""	, "NAME":'전체' },
						{ "CODE" : "Y"	, "NAME":'예' },
						{ "CODE" : "N"	, "NAME":'아니오' }
					],
			style : {}
		});
		//fnTagMkRadioYN({id: "useYn" , tagId : "useYn",chkVal: '전체'});
		$('#depth').forbizMaskTextBox({fn:'onlyNum'});
		$('#sort').forbizMaskTextBox({fn:'onlyNum'});
	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

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
				//form data binding
				$('#searchForm').bindingForm(data, 'rows', true);
				break;
			case 'save':
				fnSearch();
				 fnKendoMessage({message : '저장되었습니다.'});
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
	/** Common Delete*/
	$scope.fnDel = function(){	 fnDel();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
