/**-----------------------------------------------------------------------------
 * description 		 : 업무별 승인권한 관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.11		박승현          최초생성
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
			PG_ID  : 'approvalAuthByTask',
			callback : fnUI
		});

	}

	function fnUI(){

		fnTranslate();	// comm.lang.js 안에 있는 공통함수 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnSearch();
	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew, #fnClear').kendoButton();
	}
	function fnSearch(){
		aGridDs.read({"masterApprovalKindType" : "APPR_KIND_TP"});
	}
	function fnClear(){
		$('#searchForm').formClear(true);
	}
	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){

		aGridDs = fnGetDataSource({
			url : '/admin/approval/auth/getApprovalAuthByTaskList'
		});

		aGridOpt = {
			dataSource: aGridDs
			,autoBind : false
//				,height : 600
//				,scrollable: true
//				,selectable: "multiple, row"
			,columns   : [
				{ field:'taskName', title : '업무명', width:'150px', attributes:{ style:'text-align:center' }}
				,{ field:'taskCode', title : 'taskCode', width:'150px', attributes:{ style:'text-align:center' }}
				,{ field : "authManager1stList", title : "1차 승인관리자<BR>(이름/아이디)", width : '100px', attributes:{ style:'text-align:center' }
					, template : function(dataItem){
						if(dataItem.authManager1stList && Array.isArray(dataItem.authManager1stList.toJSON())) {
							var authManager = [];
							authManager = dataItem.authManager1stList.toJSON().slice();
							var templateString = "";
							if (authManager.length) {
								for (var i = 0; i < authManager.length; i++) {
									templateString += authManager[i].apprUserName + " / " + authManager[i].apprLoginId;
									if(i < (authManager.length + 1)) templateString += "<BR>";
									//apprUserId, apprLoginId, apprUserName
								}
							}
							return templateString;
						}else{
							return "";
						}
					}
				}
				,{ field : "authManager2ndList", title : "2차 승인관리자<BR>(이름/아이디)", width : '100px', attributes:{ style:'text-align:center' }
					, template : function(dataItem){
						if(dataItem.authManager2ndList && Array.isArray(dataItem.authManager2ndList.toJSON())) {
							var authManager = [];
							authManager = dataItem.authManager2ndList.toJSON().slice();
							var templateString = "";
							if (authManager.length) {
								for (var i = 0; i < authManager.length; i++) {
									templateString += authManager[i].apprUserName + " / " + authManager[i].apprLoginId;
									if(i < (authManager.length + 1)) templateString += "</br>";
									//apprUserId, apprLoginId, apprUserName
								}
							}
							return templateString;
						}else{
							return "";
						}
					}
				}
				,{ title:'관리', width: "150px", attributes:{ style:'text-align:left;'  , class:'forbiz-cell-readonly' }
					,command: [ { text: '수정'		, imageClass: "k-i-add", className: "f-grid-add k-margin5", iconClass: "k-icon",
								   click: function(e) {  e.preventDefault();
												            var tr = $(e.target).closest("tr");
												            var data = this.dataItem(tr);
												            fnSetManagerByTask(data.taskCode);},
									visible: function(dataItem){return fnIsProgramAuth("SAVE")}
								}
					]
				}
			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
	}
	//-------------------------------  Grid End  -------------------------------
	function fnSetManagerByTask( taskCode ){
		var param  = {'taskCode' : taskCode };
		fnKendoPopup({
			id     : 'approvalAuthByTaskMgm',
			title  : '업무별 승인관리자 정보',
			src    : '#/approvalAuthByTaskMgm',
            param  : param,
			width  : '1400px',
			height : '800px',
			scrollable : "yes",
			success: function( stMenuId, data ){
				fnSearch();
			}
		});
	}
	/*
	 * 공통 팝업 사용자들 가이드를 위한 Sample - 추후 삭제
	 */
	function fnSetManagerByTaskAppr(){
		var param  = {'taskCode' : 'APPR_KIND_TP.ITEM_REGIST' };
		fnKendoPopup({
			id     : 'approvalManagerSearchPopup',
			title  : '승인관리자 선택',
			src    : '#/approvalManagerSearchPopup',
			param  : param,
			width  : '1600px',
			height : '800px',
			scrollable : "yes",
			success: function( stMenuId, data ){

				if(data && !fnIsEmpty(data) && data.authManager2nd){
					var authManager1 = data.authManager1st;
					var authManager2 = data.authManager2nd;

					console.log("authManager1:"+authManager1);
					console.log("authManager2:"+authManager2);
				}
			}
		});
	}
	/*
	 * 공통 팝업 사용자들 가이드를 위한 Sample - 추후 삭제
	 */
	function fnGetApprovalHistoryPopup(){
		var param  = {'taskCode' : 'APPR_KIND_TP.COUPON' , 'taskPk' : '33'};
		fnKendoPopup({
			id     : 'approvalHistoryPopup',
			title  : '승인내역',
			src    : '#/approvalHistoryPopup',
			param  : param,
			width  : '800px',
			height : '400px',
			scrollable : "yes",
			success: function( stMenuId, data ){
			}
		});
	}

	//---------------Initialize Option Box Start ------------------------------------------------

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	$scope.fnSetManagerByTaskAppr = function( ){  fnSetManagerByTaskAppr();};
	$scope.fnGetApprovalHistoryPopup = function( ){  fnGetApprovalHistoryPopup();};

	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- validation Start -------------------------------
	//------------------------------- validation End -------------------------------

}); // document ready - END

