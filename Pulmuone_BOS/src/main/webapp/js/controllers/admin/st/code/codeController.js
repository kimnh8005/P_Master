/**-----------------------------------------------------------------------------
 * description 		 : 시스템관리 - 공통코드
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2016.12.09		최봉석          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var bGridDs, bGridOpt, bGrid;

$(document).ready(function() {
	//Initialize Page Call
	fnInitialize();

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'code',
			callback : fnUI,
		});
	}

	function fnUI(){
		//------------------------------- Tab ---------------------------------
		fnInitButton();
		//------------------------------- Initialize Button  ---------------------------------
		fnInitGrid();
		//------------------------------- Initialize Grid ---------------------------------
		fnTranslate();	// 다국어 변환

		fnInitOptionBox();

		fnSearch();
	}

	//Initialize Button Start
	function fnInitButton(){
		$('#fnSearch, #fnNew,#fnSave,  #fnClear, #fnClose').kendoButton();
		$('#fnDel').kendoButton({ enable: false });
	}

	function fnInitOptionBox(){
		fnKendoDropDownList({
			id    : 'conditionType',
			data  : [
				{"CODE":"COMMON_MASTER_NAME"	,"NAME":"마스터코드명"},
				{"CODE":"COMMON_MASTER_CODE"	,"NAME":"마스터코드값"}
			],
			textField :"NAME",
			valueField : "CODE"
		});
	}

	function fnClear(){
		$('#searchForm').formClear(true);
	}
	//Initialize Button End

	//Initialize Grid Start
	function fnInitGrid(){

		//------------------------------- Left Grid Start -------------------------------
		aGridDs = fnGetPagingDataSource({
			url      : "/admin/st/code/getCodeMasterNameList",
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs,
			pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 2,
				responsive: false
			},
			height: 800,
			scrollable: true,
//			frAtEvent :fnGridClick,
			columns   : [
				{ field: 'commonMasterName', title: '마스터코드명'	, width: '300px', attributes:{ style:'text-align:center' }}
			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
		aGrid.select("tr:eq(0)");

		$("#aGrid").on("click", "tr", function () {
			fnGridClick();
		});

//		var data = {"useYn" : "Y"};

		//------------------------------- Left Grid End -------------------------------

		//------------------------------- Right Grid Start -------------------------------
		bGridDs = fnGetEditDataSource({
			url      : "/admin/st/code/getCodeList",
			model_id     : 'stCommonCodeId',
			model_fields : {
				commonMasterCode		: { editable: false	, type: 'string', validation: { required: true  }	, defaultValue :fnGetCommonMasterCode  }
				,stCommonCodeId 		: { editable: false	, type: 'string', validation: { required: true  }}
				,commonCode 			: { editable: true	, type: 'string'
					, validation: { required: true
						, pattern : "[a-zA-Z0-9_]+"
						, validationMessage : "영문대문자, 특수문자 _만 입력가능합니다."
					}
				}
				,dictionaryMasterName	: { editable: true	, type: 'string', validation: { required: true }}
				,gbDictionaryMasterId 	: { editable: true	, type: 'number', validation: { required: true }}
				,sort					: { editable: true	, type: 'number', validation: { required: true, min: 1, max: 99, maxlength:2}}
				,useYn					: { editable: false	, type: 'string', validation: { required: true  }	, defaultValue : 'Y' }
				,comment				: { editable: true	, type: 'string', validation: { required: false }}
				,attribute1				: { editable: true	, type: 'string', validation: { required: false }}
				,attribute2				: { editable: true	, type: 'string', validation: { required: false }}
				,attribute3				: { editable: true	, type: 'string', validation: { required: false }}
				,stCommonCodeMasterId	: { editable: true	, type: 'number', validation: { required: true }    , defaultValue :fnGetCommonMasterId}
			}
		});

		bGridOpt = {
			dataSource: bGridDs,
			toolbar   :
				fnIsProgramAuth("ADD") && !fnIsProgramAuth("SAVE_DELETE") ? [{ name: 'create', text: '신규', className: "btn-point btn-s"},
						{ name: 'cancel', text: '취소', className: "btn-white btn-s" }]:
					fnIsProgramAuth("SAVE_DELETE") && !fnIsProgramAuth("ADD") ? [{ name: 'cSave',  text: '저장', imageClass: "k-i-update", className: 'k-custom-save btn-point btn-s'},
							{ name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }]:
						fnIsProgramAuth("ADD") && fnIsProgramAuth("SAVE_DELETE") ? [{ name: 'create', text: '신규', className: "btn-point btn-s"},
								{ name: 'cSave',  text: '저장', imageClass: "k-i-update", className: 'k-custom-save btn-point btn-s'},
								{ name: 'cancel', text: '취소', className: "btn-white btn-s" }]:
							[{ name: 'cancel', text: '취소', className: "btn-white btn-s" }],
			editable:{confirmation: function(model) {
		        return model.dictionaryMasterName+' 삭제하겠습니까?'
		    }},
			height: 800,
			scrollable: true,
			columns   : [
							{ field: 'commonMasterCode'		, title: '마스터코드값'	, width: '140px', attributes:{ style:'text-align:center' }}
							,{ field: 'stCommonCodeId'		, title: '코드'		, width: '140Px' , attributes:{ style:'text-align:center' }}
							,{ field: 'commonCode'			, title: '공통코드'	, width: '110px', attributes:{ style:'text-align:center' }}
							,{ field: 'dictionaryMasterName'		, title: '코드명'		, width: '110px', attributes:{ style:'text-align:center' }
								,editor: function(container, options) {
									fnKendoPopup({
										id     : 'SAMPLE_POP',
										title  : fnGetLangData({key :"4620",nullMsg :'표준용어 팝업' }),
										width  : '820px',
										height : '585px',
										src    : '#/dicMstPopup',
										param  : { "MENU_TYPE" : "word"},
										success: function( id, data ){
											if(data.id){
												var dataItem = bGrid.dataItem(bGrid.select());
												dataItem.set('dictionaryMasterName' ,data.baseName);
												dataItem.set('gbDictionaryMasterId' ,data.id);
											}
										}
									});
								}
							}
							,{ field: 'sort'			, title: '정렬'		, width: '60px', attributes:{ style:'text-align:center'  }}
							,{ field: 'useYn'			, title: '사용여부'	, width: '60px', attributes:{ style:'text-align:center'}, template : '<input class="someInputChk" type="checkbox" name="useYn" #= useYn == "Y" ? checked="checked" : "" #/>'}
							,{ command: [{name:'destroy'	,text:'삭제', className: 'k-button k-button-icontext btn-red btn-s k-grid-delete',   visible: function() { return fnIsProgramAuth("SAVE_DELETE") }}], title: ' '    	, width: '100px', attributes:{ style:'text-align:center'  , class:'forbiz-cell-readonly' }}
							,{ field: 'comment'			, title: '설명'		, width: '150px', attributes:{ style:'text-align:center' }}
							,{ field: 'attribute1'		, title: '추가1'		, width: '100px', attributes:{ style:'text-align:center' }}
							,{ field: 'attribute2'		, title: '추가2'		, width: '100px', attributes:{ style:'text-align:center' }}
							,{ field: 'attribute3'		, title: '추가3'		, width: '100px', attributes:{ style:'text-align:center' }, lockable: false}
						],
			forbizEditCustm : fnEditCustm
//			, cellClose: function(e) {
//				var cellIndex = e.sender._lastCellIndex;
//				if(cellIndex == 2) {
//					e.model.commonCode	= e.model.commonCode.replace(/[^a-z^A-Z^_]/g, "");
//				}
//			}
		};
		bGrid = $('#bGrid').initializeKendoGrid( bGridOpt ).cKendoGrid();

		bGrid.bind('dataBound', function(){
			$('.someInputChk').unbind('change').on('change', function(e){
				var dataItem = bGrid.dataItem($(e.target).closest('tr'));
				dataItem.useYn = $(this).is(':checked') ? 'Y' : 'N';
				dataItem.dirty = true;
			});
		});

		$(".k-custom-save").click(function(e){
			e.preventDefault();
			$scope.fnSave();
		});


		//------------------------------- Right Grid End -------------------------------

	}

	//Initialize End End
	function fnEditCustm(e){
		console.log(e);
		e.container.find("input[name='commonCode']").attr('maxlength', '30');
		e.container.find("input[name='sort']").attr('maxlength', '2');
		e.container.find("input[name='comment']").attr('maxlength', '255');
		e.container.find("input[name='attribute1']").attr('maxlength', '255');
		e.container.find("input[name='attribute2']").attr('maxlength', '255');
		e.container.find("input[name='attribute3']").attr('maxlength', '255');
	}
	function fnGetCommonMasterCode(){
		return aGrid.dataItem(aGrid.select()).commonMasterCode;
	}
	function fnGetCommonMasterId(){
		return aGrid.dataItem(aGrid.select()).stCommonCodeMasterId;
	}
	function fnGridClick(){
		var map = aGrid.dataItem(aGrid.select());
		var data = {"stCommonCodeMasterId":map.stCommonCodeMasterId};
		bGridDs.read(data);
	}
	function fnSearch(){
		var data = $('#searchForm').formSerialize(true);
		if( data.rtnValid ){
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
	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};

	$scope.fnSave = function(){
        let inputFormValidator = $("#bGrid").kendoValidator().data("kendoValidator");
        if(!inputFormValidator.validate()){
			fnKendoMessage({message : '필수값을 입력해주세요.'});
			return ;
        }

        var dataRows = bGridDs.data();
        if (dataRows.length <= 0) {
			fnKendoMessage({message : '입력된 데이터가 없습니다.'});
			return ;
        }
        else {
        	for (var i=0; i<dataRows.length; i++) {
        		if (dataRows[i].gbDictionaryMasterId == "") {
        			fnKendoMessage({message : '필수값을 입력해주세요.'});
        			return ;
        		}
        	}
        }

		var changeCnt = 0;
		var insertData = fnEGridDsExtract('bGrid', 'insert');
		var updateData = fnEGridDsExtract('bGrid', 'update');
		var deleteData = fnEGridDsExtract('bGrid', 'delete');
		changeCnt = insertData.length + updateData.length + deleteData.length ;
		if(changeCnt ==0){
			fnKendoMessage({message :  fnGetLangData({key :"4355",nullMsg :'데이터 변경사항이 없습니다.' }) });
			return ;
		}else{
			var data = {"insertData" : kendo.stringify(insertData)
						,"updateData" : kendo.stringify(updateData)
						,"deleteData" : kendo.stringify(deleteData)
			};
			fnAjax({
				url     : '/admin/st/code/saveCode',
				params  : data,
				success :
					function( data ){
						fnBizCallback("save", data);
					},
				isAction : 'batch'
			});
		}
	};
	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data){
		switch(id){
			case 'select':
				$('#iBrandDivForm').bindingForm(data, 'rows', true);
				break;
			case 'insert':
				fnKendoMessage({message : fnGetLangData({key :"com.msg.insert.sucess",nullMsg :'입력되었습니다.' })});
				break;
			case 'save':
			    fnKendoMessage({message : '저장되었습니다.' });
				fnGridClick(data);
				break;
			case 'update':
				fnKendoMessage({message : fnGetLangData({key :"367",nullMsg :'수정되었습니다.' }) });
				$('#fnSearch').trigger('click');
				break;
			case 'delete':
				aGridDs.remove(data);
				fnKendoMessage({message : fnGetLangData({key :"366",nullMsg :'삭제되었습니다.' })});
				break;

		}
	}
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();};
	/* component set ready */
}); // document ready - END

