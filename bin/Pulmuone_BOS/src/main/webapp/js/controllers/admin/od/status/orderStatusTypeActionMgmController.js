/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 상태관리 > 주문유형별 상태실행관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.21		최윤지          최초생성
 * **/
'use strict';

var aGridDs, aGridOpt, aGrid;
$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------
	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'orderStatusTypeActionMgm',
			callback : fnUI
		});

	}
	// 화면 UI 초기화
	function fnUI(){
		fnInitButton();	//Initialize Button  ---------------------------------
		fnInitGrid();	//Initialize Grid ------------------------------------
		fnInitOptionBox();//Initialize Option Box ------------------------------------
		fnSearch();
	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch,  #fnClear, #fnSaveAdd, #fnSaveUpdate').kendoButton();
	}

	//리스트 조회
	function fnSearch(){
        var data = $('#searchForm').formSerialize(true);

        var query = {
                filterLength : fnSearchData(data).length,
                filter :  {
                    filters : fnSearchData(data)
                }
        };
		aGridDs.query(query);
	}

    // 초기화
    function fnClear(){
        $('#searchForm').formClear(true);
    }

	//실행 저장
	function fnSaveAdd(){
		var url  = '/admin/order/statusMgr/addOrderStatusTypeAction'; // 등록
        var cbId = 'insert';

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
	//노출상태 저장
	function fnSaveUpdate(){

		var url  = '/admin/order/statusMgr/putStatusNmOrderStatusDisplay'; // 업데이트
		var cbId = 'update';

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

	//닫기
	function fnClose(){
		 var kendoWindow =$('#kendoPopup').data('kendoWindow');
	     kendoWindow.close();
	}

	//--------------------------------- Button End---------------------------------

	//------------------------------- Grid Start -------------------------------
	//그리드 초기화
	function fnInitGrid(){

		aGridDs = fnGetPagingDataSource({
			url      : '/admin/order/statusMgr/getOrderStatusTypeActionList', //조회URL
		});

		aGridOpt = {
			dataSource: aGridDs
			,navigatable: true
			,columns   : [
				 { field: 'statusCd'	,title : '주문상태ID'	, width:'50px',attributes:{ style:'text-align:center'}, rowspan:true, hidden:true}
				,{ field:'statusNm'	,title : '주문상태명'	, width:'100px',attributes:{ style:'text-align:center' }, rowspan:true, hidden:true}
				,{ field:'statusExplain'	,title : '주문상태'	, width:'200px',attributes:{ style:'text-align:center' }, rowspan:true}
				,{ field:'useType'	,title : '노출영역'	, width:'200px',attributes:{ style:'text-align:center' }, rowspan:true,
					template:
						function(dataItem){
							return "<div style='display:none'>"+dataItem.statusCd+"</div>"+
							"<div>"+dataItem.useType+"</div>";
					}
				}
				,{ field:'typeCd'	,title : '상품유형ID'	, width:'200px',attributes:{ style:'text-align:center' }, rowspan:true,
					template:
						function(dataItem){
							return "<div style='display:none'>"+dataItem.statusCd+"</div>"+
							"<div style='display:none'>"+dataItem.useType+"</div>"+
							"<div>"+dataItem.typeCd+"</div>";
					}
				}
				,{ field:'actionStatusNm'	,title : '노출상태명'	, width:'300px',attributes:{ style:'text-align:center' }, rowspan:true,
					template: function(dataItem){
									let returnVal;
				            	    if(dataItem.actionStatusNm == ''){
				            	    	returnVal = "<div style='display:none'>"+dataItem.statusCd+"</div>"+"<button class='update btn-s'>노출상태명관리</button>";
				            	    }else{
				            	    	returnVal = "<div style='display:none'>"+dataItem.statusCd+"</div>"+dataItem.actionStatusNm+"<button class='update btn-s'>노출상태명관리</button> <button class='insert btn-s'>실행관리</button>";
				            	    }
				            	    return returnVal;
						          }
				}
				,{ field:'actionNm'	,title : '노출버튼명'	, width:'200px',attributes:{ style:'text-align:center'}}
				,{ field:'actionId'	,title : '실행ID'	, width:'50px',attributes:{ style:'text-align:center' }, hidden:true}
				,{ field:'actionSeq'	,title : '실행SEQ'	, width:'50px',attributes:{ style:'text-align:center' }, hidden:true}
				,{ title : '관리', width:'120px', attributes:{ style:'text-align:center', class:'forbiz-cell-readonly' },
					template : function(dataItem){
									let returnVal;
				            	    if(dataItem.actionSeq == null){
				            	    	returnVal = "";
				            	    }else{
				            	    	returnVal = "<button class='delete btn-red btn-s'>삭제</button>";
				            	    }
				            	    return returnVal;
						          }
				}
			]
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
		aGrid.bind('dataBound', function(e) {
			mergeGridRows('aGrid',['statusExplain', 'useType','typeCd', 'actionStatusNm'],['statusExplain', 'useType', 'typeCd','actionStatusNm'] );

		});
	}

	//mergeGridRows('aGrid',['useType'],['useType'] );


	// ==========================================================================
	// # Kendo Grid 전용 rowSpan 메서드
	//  - @param gridId : div 로 지정한 그리드 ID, 해당 div 내 table 태그를 찾아감
	//  - @param mergeColumns : 그리드에서 셀 머지할 컬럼들의 data-field 목록
	//  - @param groupByColumns : group by 할 컬럼들의 data-field 목록, 해당 group 내에서만 셀 머지가 일어남
	// ==========================================================================
	function mergeGridRows(gridId, mergeColumns, groupByColumns) {
		// 데이터 1건 이하인 경우 : rowSpan 불필요하므로 return
		if( $('#' + gridId + ' > table > tbody > tr').length <= 1 ) {
			return;
		}


		var groupByColumnIndexArray = [];   // group by 할 컬럼들의 th 헤더 내 column index 목록
		var tdArray = [];                   // 해당 컬럼의 모든 td 배열, 개수 / 순서는 그리드 내 tr 개수 / 순서와 같음
		var groupNoArray = [];              // 파라미터로 전달된 groupByColumns 에 따라 계산된 그룹번호 배열, 같은 그룹인 경우 그룹번호 같음, 개수 / 순서는 tdArray 와 같음

		var groupNo;            // 각 tr 별 그룹번호, 같은 그룹인 경우 그룹번호 같음
		var beforeTr = null;    // 이전 tr
		var beforeTd = null;    // 이전 td
		var rowspan = null;     // rowspan 할 개수, 1 인경우 rowspan 하지 않음

		// 해당 그리드의 th 헤더 row
		var thRow = $('#' + gridId + ' > table > thead > tr')[0];

		// 셀 머지시 group by 할 컬럼들의 data-field 목록이 Array 형태의 파라미터로 전달시
		if( groupByColumns && Array.isArray(groupByColumns) && groupByColumns.length > 0 ) {

			$(thRow).children('th').each(function (thIndex, th) {  // thead 의 th 반복문 시작 : table 내 각 컬럼별로 반복
				// groupByColumns => groupByColumnIndexArray 로 변환
				if( groupByColumns.includes( $(th).attr('data-field') ) ) {
					groupByColumnIndexArray.push(thIndex);
				}

			});
		} // if 문 끝

		// ------------------------------------------------------------------------
		// tbody 내 tr 반복문 시작
		// ------------------------------------------------------------------------
		$('#' + gridId + ' > table > tbody > tr').each(function() {
			beforeTr = $(this).prev();        // 이전 tr
			// 첫번째 tr 인 경우 : 이전 tr 없음
			if( beforeTr.length == 0 ) {
				groupNo = 0;                    // 그룹번호는 0 부터 시작
				groupNoArray.push(groupNo);     // 첫번째 tr 의 그룹번호 push
			}
			else {
				var sameGroupFlag = true;       // 이전 tr 과 비교하여 같은 그룹인지 여부 flag, 기본값 true

				for( var i in groupByColumnIndexArray ) {
					var groupByColumnIndex = groupByColumnIndexArray[i];  // groupByColumns 로 전달된 각 column 의 index
					// 이전 tr 과 현재 tr 비교하여 group by 기준 컬럼의 html 값이 하나라도 다른 경우 flag 값 false ( 같은 그룹 아님 )
					if( $(this).children().eq(groupByColumnIndex).html() != $(beforeTr).children().eq(groupByColumnIndex).html() ) {
						sameGroupFlag = false;
					}
				}

				// 이전 tr 의 값과 비교하여 같은 그룹이 아닌 경우 : groupNo 1 증가시킴
				if( ! sameGroupFlag ) {
					groupNo++;
				}

				groupNoArray.push(groupNo); // 해당 tr 의 그룹번호 push
			}

		});
		// tbody 내 tr 반복문 끝
		// ------------------------------------------------------------------------

		// ------------------------------------------------------------------------
		// thead 의 th 반복문 시작 : table 내 각 컬럼별로 반복
		// ------------------------------------------------------------------------
		$(thRow).children('th').each(function (thIndex, th) {
			if( ! mergeColumns.includes( $(th).attr('data-field') ) ) {
				return true;    // mergeColumns 에 포함되지 않은 컬럼인 경우 continue
			}

			tdArray = [];  // 값 초기화
			beforeTd = null;
			rowspan = null;

			var colIdx = $("th", thRow).index(this);  // 해당 컬럼 index

			$('#' + gridId + ' > table > tbody > tr').each(function() {  // tbody 내 tr 반복문 시작
				var td = $(this).children().eq(colIdx);
				tdArray.push(td);
			});  // tbody 내 tr 반복문 끝

			// ----------------------------------------------------------------------
			// 해당 컬럼의 td 배열 반복문 시작
			// ----------------------------------------------------------------------
			for( var i in tdArray ) {
				var td = tdArray[i];

				if ( i > 0 && groupNoArray[i-1] == groupNoArray[i] && $(td).html() == $(beforeTd).html() ) {
					rowspan = $(beforeTd).attr("rowSpan");

					if ( rowspan == null || rowspan == undefined ) {
						$(beforeTd).attr("rowSpan", 1);
						rowspan = $(beforeTd).attr("rowSpan");
					}

					rowspan = Number(rowspan) + 1;

					$(beforeTd).attr("rowSpan",rowspan);
					$(td).hide(); // .remove(); // do your action for the old cell here
				}
				else {
					beforeTd = td;
				}

				beforeTd = ( beforeTd == null || beforeTd == undefined ) ? td : beforeTd; // set the that if not already set
			}
			// 해당 컬럼의 td 배열 반복문 끝
			// ----------------------------------------------------------------------
		});
		// thead 의 th 반복문 끝
		// ------------------------------------------------------------------------

	}

	//노출상태명 관리 (업데이트)
	$("#aGrid").on("click", ".update", function(){
		var map = aGrid.dataItem($(this).closest('tr'));
		$("#statusCd").val(map.statusCd).attr("disabled",true);
		$("#useType").val(map.useType).attr("disabled",true);
		$("#typeCd").val(map.typeCd).attr("disabled",true);
		$("#actionStatusNm").val(map.actionStatusNm).attr("disabled",false);
		$("#actionNmDiv").hide();
		$("#actionIdDiv").hide();
		$("#fnSaveAdd").hide();
		$("#fnSaveUpdate").show();
		fnKendoInputPoup({height:"250px" ,width:"500px",title:{nullMsg :'노출상태명 관리'} });

      });

	//실행 관리 (추가)
	$("#aGrid").on("click", ".insert", function(){
		var map = aGrid.dataItem($(this).closest('tr'));
		$("#statusCd").val(map.statusCd).attr("disabled",true);
		$("#useType").val(map.useType).attr("disabled",true);
		$("#typeCd").val(map.typeCd).attr("disabled",true);
		$("#actionStatusNm").val(map.actionStatusNm).attr("disabled",true);
		$("#actionId").data("kendoDropDownList").enable( true );
		$("#actionNmDiv").show();
		$("#actionIdDiv").show();
		$("#actionNm").val('');
		$("#fnSaveAdd").show();
		$("#fnSaveUpdate").hide();
		fnKendoInputPoup({height:"350px" ,width:"500px",title:{nullMsg :'실행관리'} });

	});

	//삭제
	$("#aGrid").on("click", ".delete", function(){

        var dataItem = aGrid.dataItem($(this).closest("tr"));

        fnKendoMessage({message : '삭제하시겠습니까?', type : "confirm", ok : function(){
            fnAjax({
                url     : '/admin/order/statusMgr/delOrderStatusTypeAction',
                params  : {actionSeq : dataItem.actionSeq, useType : dataItem.useType},
                success :
                    function( data ){
                        fnBizCallback("delete",data);
                    },
                isAction : 'delete'
            });
        }});

	});




	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

		// 주문상태ID 리스트 조회
		fnKendoDropDownList({
			id    : 'statusCdSelect',
			url : "/admin/order/statusMgr/getOrderStatusStatusCdList",
			params : {},
			blank : "선택하세요"
		});

		// 노출영역 리스트 조회
		fnKendoDropDownList({
			id    : 'useTypeSelect',
			url : "/admin/order/statusMgr/getOrderStatusGoodsTypeUseTypeList",
			params : {},
			blank : "선택하세요"
		});

		// 상품유형ID 리스트 조회
		fnKendoDropDownList({
			id    : 'typeCdSelect',
			url : "/admin/order/statusMgr/getOrderStatusGoodsTypeTypeCdList",
			params : {},
			blank : "선택하세요"
		});

		// 실행ID 리스트 조회
		fnKendoDropDownList({
			id    : 'actionId',
			url : "/admin/order/statusMgr/getOrderStatusActionIdList",
			params : {},
			blank : "0_사용안함"
		});

	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	/**
	 * 콜백합수
	 */
	function fnBizCallback( id, data ){
		switch(id){
			case 'insert':
				fnSearch();
				$('#kendoPopup').data('kendoWindow').close();
				fnKendoMessage({message : '추가되었습니다.'});
	            fnClose();
				break;
			case 'update':
				fnSearch();
				$('#kendoPopup').data('kendoWindow').close();
	            fnKendoMessage({message : '수정되었습니다.'});
	            fnClose();
	            break;
		    case 'delete':
	            aGridDs.query();
				$('#kendoPopup').data('kendoWindow').close();
	            fnKendoMessage({message : '삭제되었습니다.'});
	            fnClose();
	            break;
		}
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
    $scope.fnSearch = function( ) { fnSearch(); }; // 조회
    /** Common Clear*/
    $scope.fnClear =function(){  fnClear(); }; // 초기화
	/** Common Save*/
	$scope.fnSaveAdd = function(){	 fnSaveAdd();};
	$scope.fnSaveUpdate = function(){	 fnSaveUpdate();};
	/** Common Delete*/
	$scope.fnDel = function(){	 fnDel();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
