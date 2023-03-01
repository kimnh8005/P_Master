/**-----------------------------------------------------------------------------
 * description 		 : 로그관리 - 메뉴사용이력조회
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.11		강윤경          최초생성
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
			PG_ID  : 'privacyMenuOperHist',
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
		$('#fnSearch, #fnClear').kendoButton();
	}
	function fnSearch(){
		var data;
		data = $('#searchForm').formSerialize(true);

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
	function fnClear(){
		$('#searchForm').formClear(true);
	}
	function fnNew(){
	}
	function fnSave(){
	}
	function fnDel(){
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : '/admin/st/log/getPrivacyMenuOperLogList',
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 5
			}
			,navigatable: true
			,columns   : [
				 { field:'No'					,title : 'No'				, width:'50px'		,attributes:{ style:"text-align:center"}, template : "<span class='row-number'></span>" }
				,{ field:'loginId'	    		,title : '로그인 아이디'			, width:'70px'		,attributes:{ style:'text-align:center' }}
				,{ field:'loginName'			,title : '관리자명' 			, width:'70px'		,attributes:{ style:'text-align:center' }}
				,{ field:'paramValue'			,title : 'Parent data'		, width:'250px' 	,attributes:{ style:'text-align:center' }}
				,{ field:'url' 					,title : '메뉴정보(URL)'		, width:'150px'		,attributes:{ style:'text-align:center' }
					, template: function(data) {
						return data.urlName + '/<br>' + data.url;
					}}
				,{ field:'crudType'				,title : '수행업무'			, width:'70px'		,attributes:{ style:'text-align:center' }}
				,{ field:'ip'					,title : '아이피'				, width:'70px'		,attributes:{ style:'text-align:center' }}
				,{ field:'createDate'			,title : '등록일자'			, width:'70px'		,attributes:{ style:'text-align:center' }}
			],
			change : fnGridClick
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		aGrid.bind("dataBound", function() {
			//total count
            $('#countTotalSpan').text(aGridDs._total);

            let rowNum = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);

 		   $("#aGrid tbody > tr .row-number").each(function(index){
 		      $(this).html(rowNum);
 		      rowNum--;
 		   });
        });
	}

	function fnGridClick(){
		var map = aGrid.dataItem(aGrid.select());
	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		fnKendoDropDownList({
			id    : 'searchType',
			data  : [
				{"CODE":"loginId"	, "NAME":"로그인 아이디"},
				{"CODE":"loginName"	, "NAME":"관리자명"},
				{"CODE":"url"		, "NAME":"메뉴정보"},
				{"CODE":"ip"		, "NAME":"아이피"}
			],
			textField :"NAME",
			valueField : "CODE"
		});
		fnKendoDatePicker({
			id    : 'startCreateDate',
			format: 'yyyy-MM-dd',
			defVal: fnGetDayMinus(fnGetToday(),6),
			defType : 'oneWeek'
		});
		fnKendoDatePicker({
			id    : 'endCreateDate',
			format: 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startCreateDate',
			btnEndId : 'endCreateDate',
			defVal: fnGetToday(),
			defType : 'oneWeek'
		});

		fnTagMkChkBox({
            id : "crudType",
            url : "/admin/comn/getCodeList",
            tagId : "crudType",
            style : {},
            async : false,
            params : {"stCommonCodeMasterCode" : "CRUD_TP", "useYn" :"Y", "attr1" : "Y"}
//            , change : function(e) {
//                if($("input[name=clientType]:eq(0)").is(":checked") == true){
//                    $("input[name=clientType]").each(function(idx) {
//                        if(idx != 0){
//                            $(this).prop("checked", true);
//                            $(this).prop("disabled", true);
//                        }
//                    });
//                } else {
//                    $("input[name=clientType]").each(function(idx) {
//                        if(idx != 0){
//                            $(this).prop("disabled", false);
//                        }
//                    });
//                }
//            }
        });
	}

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	function inputFocus(){
	};
	function condiFocus(){
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
				fnInitOptionBox();
				break;
			case 'save':
				fnKendoMessage({message : fnGetLangData({key :"368",nullMsg :'저장되었습니다.' })});
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
	$scope.fnSearch = function( ) {	fnSearch();};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
