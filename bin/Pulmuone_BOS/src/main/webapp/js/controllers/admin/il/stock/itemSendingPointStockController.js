﻿/**-----------------------------------------------------------------------------
 * description 		 : 임박/폐기 예정 품목리스트
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.06		박영후          최초생성
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
			PG_ID  : 'itemSendingPointStock',
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

		$('#inputForm').formClear(true);
		fnKendoInputPoup({height:"200px" ,width:"600px", title:{ nullMsg :'그룹추가' } });
	}
	function fnSave(){
		if ($("input[name='regFormRadio']:checked").val() != "D")  // 파일 등록일 경우
			alert("직접등록만 가능합니다.");

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
				{ field:'rowNumber'			,title : 'No.'							, width:'50px'		,attributes:{ style:'text-align:center' }}
				,{ field:'zipCode'			,title : 'ERP품목코드<br>(재고코드)'			, width:'110px'	,attributes:{ style:'text-align:center' }}
				,{ field:'jejuYn'			,title : '마스터품목명'						, width:'200px'	,attributes:{ style:'text-align:left' }
											,template: function(dataItem) {
												var html =	"<strong>" + kendo.htmlEncode(dataItem.zipCode) + "</strong>" + "<br>" +
															"<a role='button' class='k-button k-button-icontext' kind='itemInfoEditBtn' href='#'>품목정보수정</a>";
												return html;
										    }}
				,{ field:'islandYn'			,title : '공급업체'						, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		,title : '브랜드'							, width:'60px'	,attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		,title : '표준카테고리<br>(대분류)'			, width:'100px'	,attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		,title : '보관<br>방법'					, width:'60px'	,attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		,title : '출고처'							, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'zipCode'			,headerTemplate : '재고<br>(정상/<span style="color:#0000ff">임박</span>/<span style="color:#ff0000">폐기예정</span>)'
											,template: function(dataItem) {
												var html =	"<strong>" + kendo.htmlEncode(dataItem.zipCode) + "</strong>" +
															"<a role='button' class='k-button k-button-icontext' kind='detailInfoBtn' href='#'>상세보기</a>";
												return html;
										    }
											, width:'120px'
											,attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		,title : '입고예정수량<br>(올가 off 재고)'		, width:'110px'	,attributes:{ style:'text-align:center' }}
			]
			,change : fnGridClick
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
        aGrid.bind("dataBound", function() {
			//total count
            $('#countTotalSpan').text(aGridDs._total);
        });
		$('#aGrid').on("click", "a[kind=itemInfoEditBtn]", function(e) {
			e.preventDefault();

			var dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));

			fnKendoPopup({
			    id: 'goodsStockMgm',
			    title: '재고 상세 정보',
			    param: { "zipCode":  dataItem.zipCode },
			    src: '#/goodsStockMgm',
			    width: '1100px',
			    height: '800px',
			    success: function(id, data) {
			        //fnSearch();
			    }
			});
		});

		$('#aGrid').on("click", "a[kind=detailInfoBtn]", function(e) {
			e.preventDefault();

			var dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));

			fnKendoPopup({
			    id: 'goodsStockMgm',
			    title: '재고 상세 정보',
			    param: { "zipCode":  dataItem.zipCode },
			    src: '#/goodsStockMgm',
			    width: '1100px',
			    height: '800px',
			    success: function(id, data) {
			        //fnSearch();
			    }
			});
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
		$('#zipCode').forbizMaskTextBox({fn:'onlyNum'});
	}

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

		fnTagMkRadio({
			id    :  'searchStockTypeRadio',
			tagId : 'searchStockTypeRadio',
			chkVal: 'ALL',
			data  : [   { "CODE" : "ALL"		, "NAME":'전체' },
						{ "CODE" : "C"			, "NAME":'임박' },
						{ "CODE" : "OPE"		, "NAME":'출고기한 초과' },
					],
			style : {}
		});


		fnTagMkChkBox({
			id    : 'searchKeepMethod',
			url : "/admin/comn/getCodeList",
			tagId : 'searchKeepMethod',
			chkVal: '',
			style : {},
			params : {"stCommonCodeMasterCode" : "ERP_STORAGE_TYPE", "useYn" :"Y"}
		});


		fnTagMkRadio({
			id    :  'searchInPlanQuantityRadio',
			tagId : 'searchInPlanQuantityRadio',
			chkVal: 'ALL',
			data  : [   { "CODE" : "ALL"		, "NAME":'전체' },
						{ "CODE" : "O0"			, "NAME":'0이상' },
						{ "CODE" : "0"			, "NAME":'0' }
					],
			style : {}
		});


		fnKendoDropDownList({
			id    : 'searchExpireDateDropDown',
			data  : [
				{"CODE":"BELOW"		,"NAME":"이하"},
				{"CODE":"OVER"	,"NAME":"이상"}
			],
			textField :"NAME",
			valueField : "CODE",
			value : "ALL"
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

				if (detailData.jejuYn == "Y")
					$("#areaType_0").prop("checked", "checked");
				else
					$("#areaType_0").removeAttr("checked");
				if (detailData.islandYn == "Y")
					$("#areaType_1").prop("checked", "checked");
				else
					$("#areaType_1").removeAttr("checked");

				$("#regFormRadio_0").click();
				fnKendoInputPoup({height:"200px" ,width:"600px",title:{nullMsg :'원산지 등록'} });
				break;
			case 'insert':
				if(data.rows =="DUP_DATA"){
					fnKendoMessage({message : '중복입니다.'});
				}else{
					fnSearch();
					$('#kendoPopup').data('kendoWindow').close();
					$('#searchForm').formClear(true);
					fnKendoMessage({message : '저장되었습니다.'});
				}
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
	$scope.fnDel = function(){	 fnDel();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
