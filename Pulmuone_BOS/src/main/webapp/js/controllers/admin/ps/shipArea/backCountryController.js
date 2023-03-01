/**-----------------------------------------------------------------------------
 * description 		 : 도서산관 권역 설정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.02		박영후          최초생성
 * @ 2020.11.30		강윤경          현행화
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
			PG_ID  : 'backCountry',
			callback : fnUI
		});

	}

	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitMaskTextBox();  // Initialize Input Mask ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnSearch();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew,#fnSave,  #fnClear').kendoButton();
	}
	function fnSearch(){

//		if( $('#condiValue').val() == ""){
//			return valueCheck("", "필수값을 입력 후 조회하시기 바랍니다.", 'condiValue');
//		}

		$("#checkBoxAll").removeAttr("checked");

		var data = $('#searchForm').formSerialize(true);
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
	function valueCheck(key, nullMsg, ID){
		fnKendoMessage({ message : fnGetLangData({ key : key, nullMsg : nullMsg}), ok : function focusValue(){
			$('#'+ID).focus();
		}});
		return false;
	}
	function fnClear(){
		$('#searchForm').formClear(true);
	}
	function fnNew(){
		$("#zipCode").attr("disabled", false);

		$("#zipAddArea").show();
		$("#fileuploadAddArea").hide();

	//	$('#inputForm').formClear(true);
		$('#inputForm')[0].reset();
		fnKendoInputPoup({height:"220px" ,width:"600px", title:{ nullMsg :'그룹추가' } });
	}
	function fnSave(){
		var url  = '/admin/policy/shipArea/addBackCountry';
		var cbId = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/policy/shipArea/putBackCountry';
			cbId= 'update';
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
		}
	}
	function fnDelRow(e){
		e.preventDefault();

		var dataItem = this.dataItem($(e.currentTarget).closest("tr"));

		fnKendoMessage({message : '삭제하시겠습니까?', type : "confirm", ok : function(){
			fnAjax({
				url     : '/admin/policy/shipArea/delBackCountry',
				params  : {zipCodeCsv : dataItem.zipCode},
				success :
					function( data ){
						fnBizCallback("delete",data);
					},
				isAction : 'delete'
			});
		}});
	}
	function fnMultiDel(){
		var selZipCodeCsv = $("input[type=checkbox][name=itemGridChk]:checked").map(function() {
			return $(this).val();
		}).toArray().join(",");

		if (!selZipCodeCsv) {
			fnKendoMessage({message : '삭제할 우편번호를 선택해주세요.'});
			return;
		}

		fnKendoMessage({message : '삭제하시겠습니까?', type : "confirm", ok : function(){
			fnAjax({
				url     : '/admin/policy/shipArea/delBackCountry',
				params  : {zipCodeCsv : selZipCodeCsv},
				success :
					function( data ){
						fnBizCallback("delete",data);
					},
				isAction : 'select'
			});
		}});
	}

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : '/admin/policy/shipArea/getBackCountryList',
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50, 100],
				buttonCount : 10
			}
			,navigatable: true
//			        ,height:550
			,columns   : [
				{ field : 'chk', headerTemplate : "<input type='checkbox' id='checkBoxAll' />", template : '<input type="checkbox" name="itemGridChk" value="#: zipCode #" class="itemGridChk" />', width:'50px', attributes : {style : "text-align:center;"}}
				,{ field:'zipCode'			,title : '우편번호'				, width:'300px'	,attributes:{ style:'text-align:center' }}
				,{ field:'islandYn'			,title : '도서산간 (1 권역)'			, width:'200px'	,attributes:{ style:'text-align:center' }}
				,{ field:'jejuYn'			,title : '제주 (2 권역)'			, width:'200px'	,attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		,title : '등록일자'				, width:'400px'	,attributes:{ style:'text-align:center' }}
				,{ command: [{
						click: fnDelRow
						, text: '삭제'
						, className: "k-button k-button-icontext btn-red btn-s k-grid-delete"
						, visible: function() { return fnIsProgramAuth("DELETE") }
						, title : '관리'
						, width:'100px'
						, attributes:{ style:'text-align:center', class:'forbiz-cell-readonly' }
					}]
				}
			]
			,change : fnGridClick
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
        aGrid.bind("dataBound", function() {
			//total count
            $('#countTotalSpan').text(aGridDs._total);
        });

		$("#checkBoxAll").on("click",function(index){
			if($("#checkBoxAll").prop("checked")==true)
				$('INPUT[name=itemGridChk]').prop("checked",true);
			else
				$('INPUT[name=itemGridChk]').prop("checked",false);
		});
	}

	function fnGridClick(){
		$("#zipCode").attr("disabled", true);

		var aMap = aGrid.dataItem(aGrid.select());

		fnAjax({
			url     : '/admin/policy/shipArea/getBackCountry',
			params  : {zipCode : aMap.zipCode},
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'select'
		});
	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitMaskTextBox() {
		//$('#zipCode').forbizMaskTextBox({fn:'onlyNum'});
		fnInputValidationForNumber("zipCode");
	}

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		fnKendoDropDownList({
			id    : 'searchAreaType',
			data  : [
				{"CODE":"ALL"		,"NAME":"전체"},
				{"CODE":"ISLAND"	,"NAME":"도서산간 (1 권역)"},
				{"CODE":"JEJU"		,"NAME":"제주 (2 권역)"}
			],
			textField :"NAME",
			valueField : "CODE",
			value : "ALL"
		});

		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

//		fnTagMkRadio({
//			id    :  'regFormRadio',
//			tagId : 'regFormRadio',
//			chkVal: 'D',
//			data  : [   { "CODE" : "D"	, "NAME":'직접등록' },
//						{ "CODE" : "F"	, "NAME":'파일등록' }
//					],
//			style : {}
//		});

		$("#regFormRadio_0").on("click", function() {  // 그룹추가시 직접등록 라디오 클릭시
			$("#zipAddArea").show();
			$("#fileuploadAddArea").hide();
		});
		$("#regFormRadio_1").on("click", function() {  // 그룹추가시 파일등록 라디오 클릭시
			$("#zipAddArea").hide();
			$("#fileuploadAddArea").show();
		});

		fnTagMkRadio({
			id    : 'areaType',
			data  : [
				{"CODE":"ISLAND"	,"NAME":"1 권역 (도서산간)"},
				{"CODE":"JEJU"		,"NAME":"2 권역 (제주)"}
			],
			tagId : 'areaType',
			chkVal: '',
			style : {}
		});
	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
				//$('#inputForm').bindingForm(data, 'rows', true);

				OPER_TP_CODE = 'U';

				var detailData = data.rows;

				$("#zipCode").val(detailData.zipCode);

				if (detailData.islandYn == "Y")
					$("#areaType_0").prop("checked", "checked");
				else
					$("#areaType_0").removeAttr("checked");
				if (detailData.jejuYn == "Y")
					$("#areaType_1").prop("checked", "checked");
				else
					$("#areaType_1").removeAttr("checked");

				//$("#regFormRadio_0").click();
				fnKendoInputPoup({height:"220px" ,width:"600px",title:{nullMsg :'그룹추가'} });
				break;
			case 'insert':
				fnSearch();
				$('#kendoPopup').data('kendoWindow').close();
				fnKendoMessage({message : '저장되었습니다.'});
				break;
//			case 'save':
//				fnKendoMessage({message : '저장되었습니다.'});
//				break;
			case 'update':
				aGridDs.query();
				fnKendoMessage({message : '수정되었습니다.'});
				fnClose();
				break;
			case 'delete':
				aGridDs.query();
				fnClose();
				fnKendoMessage({message : '삭제되었습니다.'});
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
	$scope.fnMultiDel = function(){	 fnMultiDel();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	/** Excel Down*/
	$scope.fnDown = function( ){  fnDown();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
