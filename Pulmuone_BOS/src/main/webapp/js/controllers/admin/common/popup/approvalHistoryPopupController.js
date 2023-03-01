﻿/**-----------------------------------------------------------------------------
 * system            : 승인내역 히스로리 목록 조회 팝업
 * @
 * @ 수정일           수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.02.24     박승현          최초생성
 * @
 * **/
'use strict';

var paramData = parent.POP_PARAM["parameter"]; // 파라미터
var aGridDs, aGridOpt, aGrid;
var paramData ;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'approvalHistoryPopup',
			callback : fnUI
		});

	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnSearch();
	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnClose').kendoButton();
	}

    function fnSearch(){
        let data = {};
        if( !paramData.taskCode || !paramData.taskPk){
        	fnKendoMessage({  message : "요청값이 없습니다."
        		, ok : function(){
        			parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
        		}
        	});
        }
        data['taskCode'] = paramData.taskCode;
        data['taskPk'] = paramData.taskPk;
        aGridDs.read(data);
    };
	function fnClose(params){
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){

		aGridDs = fnGetDataSource({
            url : "/admin/approval/auth/getApprovalHistory"
        });

		aGridOpt = {
			dataSource: aGridDs
			,navigatable: true
			,height:380
			,columns   : [
				{ title : 'No'		, width:'30px',attributes:{ style:'text-align:center' },template:"<span class='row-number'></span>"}
				,{ field:'prevApprStatName'		,title : '승인상태'	, width:'90px',attributes:{ style:'text-align:center' }}
				,{ field:'approvalRequestUserName'	,title : '요청자'		, width:'70px'	,attributes:{ style:'text-align:center' }
					, template : function(dataItem) {
						var templateString = dataItem.approvalRequestUserName + "/<BR>" + dataItem.approvalRequestUserId;
						return templateString;
					}
				}
				,{ field:'apprStatName'		,title : '변경상태'	, width:'90px',attributes:{ style:'text-align:center' }}
				,{ field:'createName'	,title : '처리자'		, width:'70px'	,attributes:{ style:'text-align:center' }
					, template : function(dataItem) {
						var templateString = dataItem.createName + "/<BR>" + dataItem.createLoginId;
						return templateString;
					}
				}
				,{ field:'createDate'	,title : '처리일자'		, width:'70px'	,attributes:{ style:'text-align:center' }}
			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
		aGrid.bind("dataBound", function() {
			var row_num = aGridDs._data.length;
    		$("#aGrid tbody > tr .row-number").each(function(index){
    			$(this).html(row_num);
    			row_num--;
    		});
        });
	}
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END

