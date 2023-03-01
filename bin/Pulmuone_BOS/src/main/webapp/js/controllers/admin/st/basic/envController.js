/**-----------------------------------------------------------------------------
 * system 			 : 시스템 환경설정
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
			PG_ID  : 'env',
			callback : fnUI
		});

		//$('div#ng-view').css('width', '1500px');
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

		for(let i=0; i<insertData.length; i++){
			if(insertData[i].environmentKey=="" || insertData[i].environmentName=="" || insertData[i].environmentValue==""){
				fnKendoMessage({message : "필수 데이터를 입력해주세요."});
				return;
			}
		}
		for(let i=0; i<updateData.length; i++){
			if(updateData[i].environmentKey=="" || updateData[i].environmentName=="" || updateData[i].environmentValue==""){
				fnKendoMessage({message : "필수 데이터를 입력해주세요."});
				return;
			}
		}

		changeCnt = insertData.length + updateData.length +deleteData.length ;

		if(changeCnt ==0){
			fnKendoMessage({message : "데이터 변경사항이 없습니다."});
			return ;
		}else{
			var data = {"insertData" : kendo.stringify(insertData)
						,"updateData" : kendo.stringify(updateData)
						,"deleteData" : kendo.stringify(deleteData)
			};
		}

		fnAjax({
			url     : '/admin/st/basic/saveEnvironment',
			params  : data,
			success :
				function( data ){
					fnBizCallback("save", data);
				},
			isAction : 'batch'
		});

	}
	function fnDel(){

	}

	function fnClose(){

	}

	//--------------------------------- Button End---------------------------------

	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){

		aGridDs = fnGetEditPagingDataSource({
			url      : '/admin/st/basic/getEnvironmentList',
			pageSize       : PAGE_SIZE,
			model_id     : 'environmentKey',
			model_fields : {
				environmentKey		: { editable: true	, type: 'string', validation: { required: true
					, stringValidation: function (input) {
                        if (input.is("[name='environmentKey']") && input.val() != "") {
                            input.attr("data-stringValidation-msg", '영문대문자, 숫자, 언더스코어"_"만 등록하실 수 있습니다');
                            return !validateFormat.typeNotUpperEnglishNumberUnderBar.test(input.val());
                        }
                    return true;
					}}}
				,environmentName 		: { editable: true	, type: 'string', validation: { required: true, maxLength:"20"
					, stringValidation: function (input) {
	                    if (input.is("[name='environmentName']") && input.val() != "") {
	                        input.attr("data-stringValidation-msg", '한글만 등록하실 수 있습니다');
	                        return !validateFormat.typeNotKorean.test(input.val());
	                    }
                    return true;
				}}}
				,environmentValue		: { editable: true	, type: 'string', validation: { required: true, maxLength:"255"}}
				,comment			: { editable: true	, type: 'string'}
			}
		});

		aGridOpt = {
			dataSource: aGridDs
			,navigatable: true
			,toolbar   :
				fnIsProgramAuth("ADD") && !fnIsProgramAuth("SAVE_DELETE") ? [{ name: 'create', text: '신규', className: "btn-point btn-s"},
		 							             											  { name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }]:
				fnIsProgramAuth("SAVE_DELETE") && !fnIsProgramAuth("ADD") ? [{ name: 'cSave',  text: '저장', imageClass: "k-i-update", className: 'k-custom-save btn-point btn-s'},
																							  { name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }]:
				fnIsProgramAuth("ADD") && fnIsProgramAuth("SAVE_DELETE") ? [{ name: 'create', text: '신규', className: "btn-point btn-s"},
																							 { name: 'cSave',  text: '저장', imageClass: "k-i-update", className: 'k-custom-save btn-point btn-s'},
																							 { name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }]:
				[{ name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }]
			,pageable :	{
				refresh: true
				,pageSizes: [20, 30, 50]
				,buttonCount: 10
			}
			, editable:{confirmation: function(model) {
		        return model.environmentName + ' 삭제하시겠습니까?'
		    }}
			//,height:550
			,columns   : [
				{ title : 'No', width:'60px', attributes:{ style:'text-align:center' }, template: function (dataItem){return fnKendoGridPagenation(aGrid.dataSource,dataItem);} }
				,{ field:'environmentKey',title : '설정키'	, width:'250px',attributes:{ style:'text-align:center' }}
				,{ field:'environmentName'	,title : '설정명'	, width:'250px',attributes:{ style:'text-align:center' }}
				,{ field:'environmentValue'	,title : '설정값'	, width:'250px',attributes:{ style:'text-align:center' }}
				,{ field:'comment'			,title : '설명'	, width:'400px',attributes:{ style:'text-align:center' }}
				,{ command: [{name:'destroy'	,text:'삭제', className: 'btn-red btn-s',   visible: function() { return fnIsProgramAuth("SAVE_DELETE") }}]			,title : ' '	, width:'120px', attributes:{ style:'text-align:center', class:'forbiz-cell-readonly' }}
			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
		aGrid.bind('dataBound', function(){
			$('#totalCnt').text(aGridDs._total);
		});

		$(".k-custom-save").click(function(e){
			e.preventDefault();
			fnSave();
		});

		$("#aGrid").on("click", "tbody>tr", function () {
				fnGridClick();
		});





		//최근 업데이트 일자에 날짜 추가
		const year = '2020';
		const month = '7';
		const date = '22';
		const hour = '14';
		const minute = '25';
		const second = '30';

		const dateString = year + '-' + month + '-' + date;
		const timeString = hour + ':' + minute + ':' + second;

		$('.update-date__date').html(dateString);
		$('.update-date__time').html(timeString);

		//최근 업데이트 일자 hide
		//$('.update-date').hide();

		//툴바 hide
		//$('.toolbar-wrapper').hide();

		$(".k-grid-toolbar").addClass("k-align-with-pager-sizes");
	}
	function fnGridClick(){
		var map = aGrid.dataItem(aGrid.select());
		$('#inputForm').bindingForm( {'rows':map},'rows', true);
	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
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
				fnKendoMessage({message : "저장되었습니다."});
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
