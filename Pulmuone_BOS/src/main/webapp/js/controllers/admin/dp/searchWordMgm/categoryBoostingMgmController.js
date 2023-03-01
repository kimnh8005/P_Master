/**-----------------------------------------------------------------------------
 * description 		 : 카테고리 부스팅 관리
 * @
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

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

	}
	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });
		fnPageInfo({
			PG_ID  : 'searchWordDicMgm',
			callback : fnUI
		});
		//$('div#ng-view').css('width', '1500px');
	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------


		fnSearch();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew,#fnSave,  #fnClear').kendoButton();
	}
	function fnSearchEngine(){
		 fnKendoMessage({message:'검색엔진 반영을 요청 하시겠습니까?', type : "confirm" , ok : function(){
			 fnAjax({
				 url     : '/admin/dp/categoryBoosting/callReflectionCategoryBoosting',
				 success :
					 function( data ){
					 fnBizCallback("engine");
				 },
				 isAction : 'insert'
			 });
		}});
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
		var insertChkData = fnEGridDsExtract('aGrid', 'insert');
		var updateData = fnEGridDsExtract('aGrid', 'update');
		var deleteData = fnEGridDsExtract('aGrid', 'delete');
		changeCnt = insertData.length + updateData.length +deleteData.length ;



		if(changeCnt ==0){
			fnKendoMessage({message :  fnGetLangData({nullMsg :'데이터 변경사항이 없습니다.' }) });
			return ;
		}else{
			var data = {"insertData" : kendo.stringify(insertData)
						,"updateData" : kendo.stringify(updateData)
						,"deleteData" : kendo.stringify(deleteData)
			};
//			console.dir("===>"+JSON.stringify(data));

			fnAjax({
				url     : '/admin/dp/categoryBoosting/saveCategoryBoosting',
				params  : data,
				success :
					function( data ){
						fnBizCallback("save", data);
					},
				isAction : 'batch'
			});
		}

	}

	function fnClose(){

	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetEditPagingDataSource({
			url      : '/admin/dp/categoryBoosting/getCategoryBoostingList',
			pageSize       : PAGE_SIZE,
			model_id     : 'dpCtgryBoostingId',  //model_id 는 pk역할을 해서 수정모드에서 활성화가 안된다.
			model_fields : {
				 no			        : { editable: false	, type: 'string', validation: { required: false }}
				,ilCtgryId       	: { editable: true	, type: 'string', validation: { required: true }}
				,searchWord			: { editable: true	, type: 'string', validation: { required: true , maxLength:"10"}}
				,boostingScore		: { editable: true	, type: 'string', validation: { required: true }, defaultValue:"1"}
				,useYn				: { editable: true	, type: 'string', validation: { required: true }, defaultValue:'Y'}
				,dpCtgryBoostingId  : { editable: false  , type: 'string', validation: { required: false }}
				,goodorbad       	: { editable: false  , type: 'string', validation: { required: false }}


			}
		});

		aGridOpt =
		{
			dataSource: aGridDs,
			toolbar   : fnIsProgramAuth("SAVE") ?
			[
				{ name: 'create', text: '신규' },
				{ name: 'cSave',  text: '저장', imageClass: "k-i-update", className: "k-custom-save", iconClass: "k-icon" },
				{ name: 'cancel', text: '취소' }
			] : [],
			pageable :
			{
				refresh: true
				,pageSizes : false
				,pageSizes: [20, 30, 50]
				,buttonCount: 5
			},
			//editable  : true,
			editable:{confirmation: function(model) {
				return '삭제하시겠습니까? \n저장버튼을 클릭해야 반영이 완료됩니다.'
		    }},
			//height: 550,
			columns   :
			[
				{ field:'no'			    ,title : 'No.'		, width:'30px',attributes:{ style:'text-align:center'} ,locked: true, lockable: false }
				,{ field:'ctgryName'	    ,title : '카테고리'	, width:'200px',attributes:{ style:'text-align:left' }
				  ,editor: function(container, options) {
					  var input = $("<input id='ilCtgryIdList2' value='"+options.model.ilCtgryId+"'   selected='selected' />");
						input.appendTo(container);
						fnKendoDropDownList({
							id  : 'ilCtgryIdList2',
							url	: "/admin/dp/categoryBoosting/getCategoryList",
							textField :"categoryName",
							valueField : "ilCategoryId",
							value :	options.model.ilCtgryId,
							blank : "선택"
						});
						$('#ilCtgryIdList2').unbind('change').on('change', function(){
							var dataItem = aGrid.dataItem($(this).closest('tr'));
							var shopDropDownList =$('#ilCtgryIdList2').data('kendoDropDownList');
							dataItem.set('ilCtgryId', shopDropDownList.value());
							dataItem.set('ctgryName', shopDropDownList.text());
						});
						}

				}
				,{ field:'ilCtgryId'  	,hidden: true }
				,{ field:'searchWord'		,title : '검색어'		, width:'200px',attributes:{ style:'text-align:left' }}
				,{ field:'boostingScore'	,title : '부스팅 점수'	, width:'200px',attributes:{ style:'text-align:center' }
//				  ,template : "#=(useYn=='Y')?fnGetLangData({nullMsg :'예' }):fnGetLangData({nullMsg :'아니오' })#"
				  ,editor: function(container, options) {
						var input = $("<input id='boostingScoreDownList' />");
						input.appendTo(container);
						fnKendoDropDownList({
							id    : 'boostingScoreDownList',
							data  : [
								{"CODE":"1"	,"NAME":"1점"},
								{"CODE":"2"	,"NAME":"2점"},
								{"CODE":"3"	,"NAME":"3점"},
								{"CODE":"4"	,"NAME":"4점"},
								{"CODE":"5"	,"NAME":"5점"},
								{"CODE":"6"	,"NAME":"6점"},
								{"CODE":"7"	,"NAME":"7점"},
								{"CODE":"8"	,"NAME":"8점"},
								{"CODE":"9"	,"NAME":"9점"},
								{"CODE":"10","NAME":"10점"},
								{"CODE":"11","NAME":"11점"},
								{"CODE":"12","NAME":"12점"},
								{"CODE":"13","NAME":"13점"},
								{"CODE":"14","NAME":"14점"},
								{"CODE":"15","NAME":"15점"}
							],
							textField :"NAME",
							valueField : "CODE",
    						value :options.model.boostingScore
						});
						$('#boostingScoreDownList').unbind('change').on('change', function(){
							var dataItem = aGrid.dataItem($(this).closest('tr'));
							var shopDropDownList =$('#boostingScoreDownList').data('kendoDropDownList');
							dataItem.set('boostingScore', shopDropDownList.value());
						});

						}

				}
				,{ field:'useYn'	,title : '사용여부'		, width:'75px',attributes:{ style:'text-align:center' }
				,template : "#=(useYn=='Y')?fnGetLangData({nullMsg :'예' }):fnGetLangData({nullMsg :'아니오' })#"
				,editor: function(container, options) {
//					console.dir("options" +options.model.useYn)
					var input = $("<input id='psGrpDropDown' value='"+options.model.useYn+"'   selected='selected' />");

					//console.dir("container" +JSON.stringify(container));
					input.appendTo(container);

					fnKendoDropDownList({
						id    : 'psGrpDropDown',
						data  : [
							{"CODE":"Y"	,"NAME":"예"},
							{"CODE":"N"	,"NAME":"아니오"}
						],
						textField :"NAME",
						valueField : "CODE",
						value :	options.model.useYn,
						blank : "선택"
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
				,{ command: [{name:'destroy'	,text:'삭제', visible: function() { return fnIsProgramAuth("DELETE") }}]			,title : '관리'        , width: '120px', attributes:{ style:'text-align:center'  , class:'forbiz-cell-readonly' }}
				,{ field:'dpCtgryBoostingId'   	,hidden: true }

			],
			cellClose:  function(e)
			{
				var cellIndex = e.sender._lastCellIndex;
				if(cellIndex == 1) {
					aGrid.refresh();
				}else if(cellIndex == 2) {
					aGrid.refresh();
				}else if(cellIndex == 3) {
					aGrid.refresh();
				}else if(cellIndex == 4) {
					aGrid.refresh();
				}
			},
			forbizEditCustm : fnEditCustm
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
		aGrid.bind('dataBound', function(){
			$('#totalCnt').text(aGridDs._total);
			$('.someInputChk').unbind('change').on('change', function(e){
				var dataItem = aGrid.dataItem($(e.target).closest('tr'));
				dataItem.useYn = $(this).is(':checked') ? 'Y' : 'N';
				dataItem.dirty = true;
			});
		});

		$(".k-custom-save").click(function(e){
			e.preventDefault();
			fnSave();
		});
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

		fnShopKendoDropDownList({id:"ilCtgryId", blank :"All"});
		fnKendoDropDownList({
			id  : 'ilCtgryId',
			url	: "/admin/dp/categoryBoosting/getCategoryList",
			//params : {"stCommonCodeMasterCode" : "7"},
			textField :"categoryName",
			valueField : "ilCategoryId",
			blank : "전체"
		});

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
				fnKendoMessage({message : '저장되었습니다'});
				break;
			case 'engine':
				fnKendoMessage({message :  '검색엔진 반영 요청을 완료했습니다.' });
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
	/** fnAnalTesth */
	$scope.fnSearchEngine = function( ) {	fnSearchEngine();	};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
