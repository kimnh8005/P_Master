/**-----------------------------------------------------------------------------
 * description 		 : BOS 접근가능 IP 설정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.06.29		오영민          최초생성
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
			PG_ID  : 'holiday',
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
		$('#fnBack, #fnExcelPopup, #fnSave,  #fnClear, #fnGridAdd, #fnGridDel, #fnKendoPopup').kendoButton();
	}
	function fnKendoPopup(){
		fnKendoInputPoup({height:"410px" ,width:"550px",title:{key :"5967",nullMsg :'휴일 업로드' }});
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
//		$('#searchForm').formClear(true);
		fnSearch();
	}
	function fnNew(){
		aGrid.clearSelection();
		$('#inputForm').formClear(true);
		inputFocus();
	}
	function fnBack() {
		history.back();
	}
	function fnSave(){
		let data = {"insertHolidayData" : kendo.stringify(aGridDs._data)};
		fnAjax({
			url     : '/admin/policy/holiday/addHoliday',
			params  : data,
			success :
				function( data ){
					fnBizCallback("save", data);
				},
			isAction : 'save'
		});
	}

	function fnGridAdd() {
		let jsonObject = {};
		let inputDate = $('#dateInputArea').val().split('\n')

		for(let i=0; i < inputDate.length; i++) {
			if(!isValidDate(inputDate[i])){
				fnKendoMessage({message : '날짜 양식대로 입력해주세요.'});
				return;
			}
		}

		let data = inputDate;
		let targetGrid = $('#aGrid').data('kendoGrid');
        let allDatas = aGrid.dataSource.data();

        duplicateValidateLoop:
        for (let i=0; i < allDatas.length; i++) {
        	for(let j=0; j < data.length; j++) {
        		if(allDatas[i].holidayDate == data[j]){
                    fnKendoMessage({message : '이미 선택한 항목 입니다.'});
                    return;
                }
        	}
        }

		for(let i=inputDate.length-1; i >= 0 ; i--){
			jsonObject['holidayDate'] = inputDate[i];
			targetGrid.dataSource.insert(0, jsonObject).set("dirtyFields",{AreaID:true});
		}
	}

	function fnGridDel() {
		var selectRows 	= aGrid.tbody.find('input[class=cGridCheckbox]:checked').closest('tr');
		if(selectRows){
			for(var i =0; i< selectRows.length;i++){
				aGrid.removeRow(selectRows[i]);
			}
		}
	}

	function isValidDate(dateString)
	{
	    // First check for the pattern
	    if(!/^\d{4}\-\d{2}\-\d{2}$/.test(dateString)){
	    	return false;
	    }

	    // Parse the date parts to integers
	    let parts = dateString.split("-");

	    let year = parseInt(parts[0], 10);
	    let month = parseInt(parts[1], 10);
	    let day = parseInt(parts[2], 10);

	    // Check the ranges of month and year
	    if(year < 2000 || year > 2999 || month == 0 || month > 12)
	        return false;

	    let monthLength = [ 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 ];

	    // Adjust for leap years
	    if(year % 400 == 0 || (year % 100 != 0 && year % 4 == 0))
	        monthLength[1] = 29;

	    // Check the range of the day
	    return day > 0 && day <= monthLength[month - 1];
	};
	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : "/admin/policy/holiday/getHolidayList"
		});
		aGridOpt = {
			dataSource: aGridDs,
			height: 397,
			scrollable: true,
			columns   : [
				{ field:'CHK'			,title : { key : '4346'		, nullMsg :'체크박스'}		, width:'40px',attributes:{ style:'text-align:center' }
				,template : "<input type='checkbox' class='cGridCheckbox' name='CGRID'/>"
				,headerTemplate : "<input type='checkbox' id='checkBoxAll' />"
				, filterable: false},
				{ field: 'holidayDate', title: '공통휴일저장목록'	, width: '300px', attributes:{ style:'text-align:center' }}
			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		aGrid.bind("dataBound", function(){
			if(aGrid.dataSource && aGrid.dataSource._view.length > 0){
				fnSetAllCheckbox('cGridCheckbox','checkBoxAll');
				$('input[name=CGRID]').on("change", function (){
					fnSetAllCheckbox('cGridCheckbox','checkBoxAll');
				});
			}
		});

		$(aGrid.tbody).on("click", "td", function (e) {
			var row = $(this).closest("tr");
			var rowIdx = $("tr", aGrid.tbody).index(row);
			var colIdx = $("td", row).index(this);
			if(colIdx>0){
				fnAGridClick($(e.target).closest('tr'));
			}
		});
	}
	function fnAGridClick(param){
		var clickRowCheckBox = param.find('input[type=checkbox]');
		if(clickRowCheckBox.prop('checked')){
			clickRowCheckBox.prop('checked', false);
		}else{
			clickRowCheckBox.prop('checked', true);
		}
		fnSetAllCheckbox('cGridCheckbox','checkBoxAll');
	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});
	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------
	function inputFocus(){
		$('#input1').focus();
	};

	/**
	 * 콜백합수
	 */
	function fnBizCallback( id, data ){
		switch(id){
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

	$scope.fnBack = function(){	 fnBack();};

	$scope.fnGridAdd = function(){	 fnGridAdd();};

	$scope.fnGridDel = function(){	 fnGridDel();};

	$scope.fnKendoPopup = function(){ fnKendoPopup();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
