/**-----------------------------------------------------------------------------
 * description 		 : 약관그룹관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2017.01.13		최봉석          최초생성
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
			PG_ID  : 'caluseGrp',
			callback : fnUI
		});

	}

	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnSearch();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew,#fnSave,  #fnClear').kendoButton();
		$('#fnDel').kendoButton({ enable: false });
	}
	function fnSearch(){
		var data = $('#searchForm').formSerialize(true);
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
		var updateData = fnEGridDsExtract('aGrid', 'update');
		var deleteData = fnEGridDsExtract('aGrid', 'delete');
		changeCnt = insertData.length + updateData.length +deleteData.length ;

		if(changeCnt ==0){
			fnKendoMessage({message :  fnGetLangData({key :"4355",nullMsg :'데이터 변경사항이 없습니다.' }) });
			return ;
		}else{
			var data = {"insertData" : kendo.stringify(insertData)
						,"updateData" : kendo.stringify(updateData)
			};
			fnAjax({
				url     : '/admin/policy/clause/saveClauseGroup',
				params  : data,
				success :
					function( data ){
						fnBizCallback("save", data);
					},
				isAction : 'batch'
			});
		}

		return false;
	}
	function fnDel(){
		fnKendoMessage({message:fnGetLangData({key :"4489",nullMsg :'삭제 하시겠습니까?' }), type : "confirm" ,ok :fnDelApply  });
	}
	function fnDelApply(){
		var url  = '/biz/ps/caluse/delCaluseGrp';
		var cbId = 'delete';
		var data = $('#inputForm').formSerialize(true);
		var map = aGrid.dataItem(aGrid.select());
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

	}

	//--------------------------------- Button End---------------------------------

	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){

		//------------------------------- Right Grid Start -------------------------------
		aGridDs = fnGetEditPagingDataSource({
			url      : "/admin/policy/clause/getClauseGroupList",
			pageSize       : PAGE_SIZE,
			model_id     : 'psClauseGroupCd',
			model_fields : {
				mandatoryYn		: { editable: false	, type: 'string', validation: { required: false }	, defaultValue : 'N' }
				, sort				: { editable: true	, type: 'string', validation: { required: false
					, stringValidation: function (input) {
                        if (input.is("[name='sort']") && input.val() != "") {
                            input.attr("data-stringValidation-msg", '숫자만 등록하실 수 있습니다');
                            return !validateFormat.typeNotNumber.test(input.val());
                        }
                    return true;
					}}}
				, psClauseGroupCd	: { editable: true	, type: 'string', validation: { required: true
					, stringValidation: function (input) {
                        if (input.is("[name='psClauseGroupCd']") && input.val() != "") {
                            input.attr("data-stringValidation-msg", '영문대소문자, 특수문자"_"만 등록하실 수 있습니다');
                            return !validateFormat.typeNotEnglishUnderBar.test(input.val());
                        }
                    return true;
					}}}
				, clauseGroupName	: { editable: true	, type: 'string', validation: { required: true, maxLength:"100"
					, stringValidation: function (input) {
                        if (input.is("[name='clauseGroupName']") && input.val() != "") {
                            input.attr("data-stringValidation-msg", '한글, 영문대소문자, 숫자만 등록하실 수 있습니다');
                            return !validateFormat.typeNotLetterSpaceNumber.test(input.val());
                        }
                    return true;
					}}}
				, clauseTitle		: { editable: true	, type: 'string', validation: { required: true, maxLength:"100"
					, stringValidation: function (input) {
                        if (input.is("[name='clauseTitle']") && input.val() != "") {
                            input.attr("data-stringValidation-msg", '한글, 영문대소문자, 숫자만 등록하실 수 있습니다');
                            return !validateFormat.typeNotLetterSpaceNumber.test(input.val());
                        }
                    return true;
					}}}
				, useYn				: { editable: false	, type: 'string', validation: { required: false  }	, defaultValue : 'Y' }
				, existYn		: { editable: false	, type: 'string', validation: { required: false }}
			}
		});

		aGridOpt = {
			dataSource: aGridDs,
			toolbar   : fnIsProgramAuth("SAVE") ? [
				{ name: 'create', text: '신규 약관추가' },
				{ name: 'cSave',  text: '저장', imageClass: "k-i-update", className: "k-custom-save", iconClass: "k-icon" },
				{ name: 'cancel', text: '취소' }
			] : [],
			pageable :	{
				refresh: true
				,pageSizes : false
				,pageSizes: [20, 30, 50]
				,buttonCount: 5
			},
			editable  : true,
			columns   : [
				{ field: 'sort'			,title: '순번'		,width: '90px'		,attributes:{ "class" : "#=(existYn)?'existRow':''#", style:'text-align:center' }}
				,{ field: 'psClauseGroupCd'	,title : 'Code'			,width:'150px'		,attributes:{ style:'text-align:left', class:"forbiz-cell-readonly #=(existYn)?'existRow':''#" }, headerAttributes: {"class":"req-star-th"}}
				,{ field: 'clauseGroupName'	,title : '약관그룹명'	,width:'410px'		,attributes:{ "class" : "#=(existYn)?'existRow':''#", style:'text-align:left' }, headerAttributes: {"class":"req-star-th"}}
				,{ field: 'clauseTitle'		,title : '약관제목'		,width:'210px'		,attributes:{ "class" : "#=(existYn)?'existRow':''#", style:'text-align:left' }, headerAttributes: {"class":"req-star-th"}}
				,{ field: 'mandatoryYn'		,title : '약관필수 동의여부'		,width:'90px'		,attributes:{ "class" : "#=(existYn)?'existRow':''#", style:'text-align:center' }
					, template : '<input class="mandatoryInputChk" type="checkbox" name="mandatoryYn" #= mandatoryYn == "Y" ? checked="checked" : "" #/>'
				}
				,{ field: 'useYn'			,title: '사용여부'		,width: '90px'		,attributes:{ "class" : "#=(existYn)?'existRow':''#", style:'text-align:center' }
					, template : '<input class="someInputChk" type="checkbox" name="useYn" #= useYn == "Y" ? checked="checked" : "" #/>'
				}
				,{ field: 'existYn', hidden: true}
			],
			forbizEditCustm : fnEditCustm
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
		aGrid.bind('dataBound', function(){
			$('.someInputChk').unbind('change').on('change', function(e){
				var dataItem = aGrid.dataItem($(e.target).closest('tr'));
				dataItem.useYn = $(this).is(':checked') ? 'Y' : 'N';
				dataItem.dirty = true;
			});

			$('.mandatoryInputChk').unbind('change').on('change', function(e){
				var dataItem = aGrid.dataItem($(e.target).closest('tr'));
				dataItem.mandatoryYn = $(this).is(':checked') ? 'Y' : 'N';
				dataItem.dirty = true;
			});
		});

		$(".k-custom-save").click(function(e){
			e.preventDefault();
			fnSave();
			//fnSave();
		});

        $(".k-grid-toolbar").addClass("k-align-with-pager-sizes");
	}
	function fnGridClick(){
		var map = aGrid.dataItem(aGrid.select());
		$('#inputForm').bindingForm( {'rows':map},'rows', true);
	};
	//Initialize End End
	function fnEditCustm(e){
		//console.log(e);
	}
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
//		fnShopKendoDropDownList({id:"ST_SHOP_ID"});
//		fnTagMkRadioYN({id: "intputActive" , tagId : "USE_YN",chkVal: 'Y'});
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
			case 'insert':
				if(data.rows =="DUP_DATA"){
					fnKendoMessage({message : fnGetLangData({key :"370",nullMsg :'중복입니다.' })  ,ok :inputFocus});
				}else{
					aGridDs.insert(data.rows);;
					fnKendoMessage({message : fnGetLangData({key :"369",nullMsg :'입력되었습니다.' }) ,ok :fnNew});
				}
				break;
			case 'save':
				fnKendoMessage({message : '저장되었습니다.', ok :fnSearch});
				break;
			case 'update':
				fnUpdateGrid(data,$("#aGrid"),"rows");
				fnKendoMessage({message : fnGetLangData({key :"367",nullMsg :'수정되었습니다.' }) });
				break;
			case 'delete':
				aGridDs.remove(data);
				fnNew();
				//aGridDs.total = aGridDs.total-1;
				fnKendoMessage({message : fnGetLangData({key :"366",nullMsg :'삭제되었습니다.' })});
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

