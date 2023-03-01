/**-----------------------------------------------------------------------------
 * description 		 : CS환불 승인요청 내역 조회
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.05.14		홍진영          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var orderGridDs, orderGridOpt, orderGrid;
var viewModel = new kendo.data.ObservableObject({});
var SELECT_TYPE = {'BUTTON':'SELECT_TYPE.BUTTON', 'CHECKBOX':'SELECT_TYPE.CHECKBOX'}
//머지할 필드 목록
var orderMergeFieldArr = ['odCsId', 'refundType', 'refundPrice', 'csRefundTp', 'csRefundApproveNm', 'management'];
// 그룹할 필드 목록
var orderMergeGroupFieldArr = ['odCsId'];

function isRequest(){
	return pageId == 'approvalCsRefundRequest';
}

function isAccept(){
	return pageId == 'approvalCsRefundAccept';
}

$(document).ready(function() {

	importScript("/js/service/od/order/orderCommDetailGridColumns.js", function (){
		importScript("/js/service/od/order/orderMgmFunction.js", function (){
			importScript("/js/service/od/order/orderCommSearch.js", function (){
				importScript("/js/service/od/order/orderMgmPopups.js", function (){
					fnInitialize();	//Initialize Page Call ---------------------------------
				})
			});
		});
	});

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID    : pageId,
			callback : fnUI
        });
    }

	function fnUI(){
		fnTranslate();	// comm.lang.js 안에 있는 공통함수 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnDefaultSet();

//		fnSearch();
	}

	//--------------------------------- Button Start---------------------------------
	function fnInitButton() {
		$('#fnSearch, #fnClear, #fnSelectCancelRequest').kendoButton();

		if(isRequest()){
			$('#fnSelectCancelRequest').show();
			$('#fnProcessApproved').hide();
			$('#fnProcessDenied').hide();
		} else if (isAccept()) {
			$('#fnSelectCancelRequest').hide();
			$('#fnProcessApproved').show();
			$('#fnProcessDenied').show();
		}
	}

    //--- 검색  -----------------
	function fnSearch(){
		if($("input[name=searchApprovalStatus]:checked").length < 1){
			fnKendoMessage({ message : "승인상태를 선택하여 조회하십시오." });
			return;
		}
		orderSubmitUtil.search();
	}

    //-- 초기화 버튼 -----------------
	function fnClear() {
		$('#searchForm').formClear(true);
		fnDefaultSet();
	}
	// 기본 설정
	function fnDefaultSet(){
		$('input[name=searchApprovalStatus]').each(function(idx, element) {
			if(idx == 1 || idx == 2 || idx == 3 || idx == 4) $(element).prop('checked', true);
			else $(element).prop('checked', false);
		});
		if(!(PG_SESSION == null || PG_SESSION == '' || PG_SESSION == undefined || PG_SESSION == 'undefined')) {
			if(isRequest()){
				$("#searchApprReqUser").val(PG_SESSION.loginId);
			} else if (isAccept()) {
				$("#searchApprChgUser").val(PG_SESSION.loginId);
			}
		}

		$('input[name=csRefundTp]').each(function(idx, element) {
			if(idx == 0 || idx == 1 || idx == 2) $(element).prop('checked', true);
			else $(element).prop('checked', false);
		});
		//전체 초기화 버튼 클릭시 필요한 초기화 요소 추가 2021-05-20 임상건 [HGRM-8162] [승인요청내역공통] 초기화 버튼 기능 수정 요청
		$('[data-id="fnDateBtnC"]').mousedown();

		$("#dateSearchStart").data("kendoDatePicker").value(fnGetMonthAdd(fnGetToday(), -1,'yyyy-MM-dd'));
        $("#dateSearchEnd").data("kendoDatePicker").value(fnGetToday());
	};

	//--------------------------------- Button End---------------------------------


	//--------------------------------------------------------------------------
	//------------------------------- Grid Start -------------------------------
	//--------------------------------------------------------------------------
    function fnInitGrid() {

		orderGridDs = fnGetPagingDataSource({
             url      : "/admin/approval/csrefund/getApprovalCsRefundList"
             , pageSize : PAGE_SIZE
             , requestEnd: function(e) {
                 //$('#countTotalSpan').text(kendo.toString(e.response.total, "n0"));
             }
        });

		orderGridOpt = {
              dataSource: orderGridDs
            , pageable  : {
                pageSizes   : [20, 30, 50],
                buttonCount : 10
              }
            , navigatable: true
            , scrollable : true
            , columns : [
	            	{ field : 'chk', headerTemplate : "<input type='checkbox' id='checkBoxAll' />", width:'50px', attributes : {style : "text-align:center;"}
		        		, template : function(dataItem){
		        			let returnValue = '';
							if (isAccept()) {
								if (dataItem.apprStat == 'APPR_STAT.REQUEST') {
									if (!fnIsEmpty(dataItem.approvalSubUserId)) { // 1차 승인자 설정시, 1차 승인자(권한대행 포함)에게만 노출
										if (
											PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
											&& (PG_SESSION.loginId == dataItem.approvalSubUserId || PG_SESSION.loginId == dataItem.approvalSubGrantUserId)
										)
					        				returnValue = '<input type="checkbox" name="rowCheckbox" class="rowCheckbox" value="' + dataItem.odCsId + '" />';
									}
									else { // 1차 승인자 미설정시, 2차 승인자(권한대행 포함)에게만 노출
										if (
											PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
											&& (PG_SESSION.loginId == dataItem.approvalUserId || PG_SESSION.loginId == dataItem.approvalGrantUserId)
										)
					        				returnValue = '<input type="checkbox" name="rowCheckbox" class="rowCheckbox" value="' + dataItem.odCsId + '" />';
									}
								}
								else if (dataItem.apprStat == 'APPR_STAT.SUB_APPROVED') {
									if (
										PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
										&& (PG_SESSION.loginId == dataItem.approvalUserId || PG_SESSION.loginId == dataItem.approvalGrantUserId)
									)
				        				returnValue = '<input type="checkbox" name="rowCheckbox" class="rowCheckbox" value="' + dataItem.odCsId + '" />';
								}
							}
							else if (isRequest()) {
								if (dataItem.apprStat == 'APPR_STAT.REQUEST') {
									if (
										PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
										&& PG_SESSION.loginId == dataItem.approvalRequestUserId
									)
				        				returnValue = '<input type="checkbox" name="rowCheckbox" class="rowCheckbox" value="' + dataItem.odCsId + '" />';
								}
							}

		        			return returnValue;
		        		}
		    		},
            		orderDetailGridColUtil.no(this),					// 순번
            		orderDetailGridColUtil.odid(this),					// 주문번호
            		orderDetailGridColUtil.refundPrice(this),			// 환불금액
	        		{ field:'csRefundTp'	,title : 'CS 환불구분'	, width:'100px',attributes:{ style:'text-align:center' }},
                    { field:'approvalRequestUserName', title : '승인요청자', width : '150px', attributes : {style : 'text-align:center'},
							template : function(dataItem) {
								if (fnIsEmpty(dataItem.approvalRequestUserName))
	        	                    return '';
								else
									return dataItem.approvalRequestUserName + "(" + dataItem.approvalRequestUserId + ")";
	                    	}
						}
					,{ field:'approvalRequestDt'	,title : '승인요청일'	, width:'100px',attributes:{ style:'text-align:center' }}
					,{ field:'approvalChangeDt'	,title : '승인처리일<BR>(1차/최종)'	, width:'100px',attributes:{ style:'text-align:center' },
						template : function(dataItem) {
							var templateString = "";
							if (fnIsEmpty(dataItem.approvalSubChangeDt))
								templateString += '-<BR>';
							else
								templateString += dataItem.approvalSubChangeDt + '<BR>';

							if (fnIsEmpty(dataItem.approvalChangeDt))
								templateString += '-';
							else
								templateString += dataItem.approvalChangeDt;

							return templateString;
						}
					}
					,{ field:'approvalSubUserName', title : '1차 승인관리자<BR>/권한위임자', width : '150px', attributes : {"class" : "#=(!fnIsEmpty(approvalSubChangeDt))?'bgColorGreen':''#",style : 'text-align:center'},
						template : function(dataItem) {
							var templateString = "";
							if (!fnIsEmpty(dataItem.approvalSubUserId)) {
								if (!fnIsEmpty(dataItem.approvalSubChangeUserId)) { // 1차 승인 이력이 있을 경우
									if (dataItem.approvalSubChangeUserId == dataItem.approvalSubUserId) {
										templateString += "<STRONG>" + dataItem.approvalSubUserName + "/" + dataItem.approvalSubUserId + "</STRONG>";
									}
									else {
										templateString += dataItem.approvalSubUserName + "/" + dataItem.approvalSubUserId;
										templateString += "<BR><STRONG>" + dataItem.approvalSubChangeUserName + "/" + dataItem.approvalSubChangeUserId + "</STRONG>";
									}
								}
								else {
									templateString += dataItem.approvalSubUserName + "/" + dataItem.approvalSubUserId;
									if (!fnIsEmpty(dataItem.approvalSubGrantUserId)) {
										templateString += "<BR>" + dataItem.approvalSubGrantUserName + "/" + dataItem.approvalSubGrantUserId;
									}
								}
							}
							return templateString;
						}
					}
					,{ field:'approvalUserName', title : '최종승인관리자<BR>/권한위임자', width : '150px', attributes : {"class" : "#=(!fnIsEmpty(approvalChangeDt))?'bgColorGreen':''#", style : 'text-align:center'},
						template : function(dataItem) {
							var templateString = "";
							if (!fnIsEmpty(dataItem.approvalUserId)) {
								if (!fnIsEmpty(dataItem.approvalChangeUserId)) { // 2차 승인 이력이 있을 경우
									if (dataItem.approvalChangeUserId == dataItem.approvalUserId) {
										templateString += "<STRONG>" + dataItem.approvalUserName + "/" + dataItem.approvalUserId + "</STRONG>";
									}
									else {
										templateString += dataItem.approvalUserName + "/" + dataItem.approvalUserId;
										templateString += "<BR><STRONG>" + dataItem.approvalChangeUserName + "/" + dataItem.approvalChangeUserId + "</STRONG>";
									}
								}
								else {
									templateString += dataItem.approvalUserName + "/" + dataItem.approvalUserId;
									if (!fnIsEmpty(dataItem.approvalGrantUserId)) {
										templateString += "<BR>" + dataItem.approvalGrantUserName + "/" + dataItem.approvalGrantUserId;
									}
								}
							}
							return templateString;
	                    }
					}
					, {field : 'apprStatName', title : '승인상태', width : '150px', attributes : {style : 'text-align:center'},
						template : function(dataItem) {
							if (dataItem.apprStat == "APPR_STAT.DENIED") {
								return "<a class='apprDenied' kind='apprDenied' style='color:blue; cursor:pointer' onclick='$scope.fnOpenApprDinedResonPopup(" + JSON.stringify(dataItem) + ");'>" + kendo.htmlEncode(dataItem.apprStatName) + "</a>";
							}
							else {
								return dataItem.apprStatName;
							}
						}
    				}
    				, {title : '관리', width: "150px", attributes:{ style:'text-align:left;', class:'forbiz-cell-readonly' }
						,command: [ { text: '요청철회', imageClass: "k-i-add", className: "f-grid-add k-margin5", iconClass: "k-icon",
										 click: function(e) {  e.preventDefault();
													            var tr = $(e.target).closest("tr");
													            var data = this.dataItem(tr);
													            fnCancelRequestOne(data.odCsId);}
										,visible: function(dataItem) {
											var hasAuth = false;

											if (!isRequest())
												return false;

											if (dataItem.apprStat != 'APPR_STAT.REQUEST')
												return false;

											if (
												PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
												&& PG_SESSION.loginId == dataItem.approvalRequestUserId
											)
												hasAuth = true;

											return hasAuth;
										}
									}
									, { text: '삭제', imageClass: "k-i-add", className: "f-grid-add k-margin5", iconClass: "k-icon"
										, click: function(e) {
											e.preventDefault();
											var tr = $(e.target).closest("tr");
											var data = this.dataItem(tr);
											fnDisposal(data.odCsId);
										}
										, visible: function(dataItem) {
											return ( (pageId == 'approvalCsRefundRequest')
														&& (dataItem.apprStat =="APPR_STAT.DENIED" || dataItem.apprStat =="APPR_STAT.CANCEL" )
													)
										}
				                    }
									, { text: '승인완료'		, imageClass: "k-i-add", className: "f-grid-add k-margin5", iconClass: "k-icon",
										click: function(e) {  e.preventDefault();
																var tr = $(e.target).closest("tr");
																var data = this.dataItem(tr);
																fnConfirmApproval(data.odCsId);}
										,visible: function(dataItem) {
											var hasAuth = false;

											if (!isAccept()) // 승인관리 페이지에서만
												return false;

											if (dataItem.apprStat == 'APPR_STAT.REQUEST') {
												if (!fnIsEmpty(dataItem.approvalSubUserId)) { // 1차 승인자 설정시, 1차 승인자(권한대행 포함)에게만 노출
													if (
														PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
														&& (PG_SESSION.loginId == dataItem.approvalSubUserId || PG_SESSION.loginId == dataItem.approvalSubGrantUserId)
													)
														hasAuth = true;
												}
												else { // 1차 승인자 미설정시, 2차 승인자(권한대행 포함)에게만 노출
													if (
														PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
														&& (PG_SESSION.loginId == dataItem.approvalUserId || PG_SESSION.loginId == dataItem.approvalGrantUserId)
													)
														hasAuth = true;
												}
											}
											else if (dataItem.apprStat == 'APPR_STAT.SUB_APPROVED') {
												if (
													PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
													&& (PG_SESSION.loginId == dataItem.approvalUserId || PG_SESSION.loginId == dataItem.approvalGrantUserId)
												)
													hasAuth = true;
											}

											return hasAuth;
										}
									}
									, { text: '승인반려' , imageClass: "k-i-add", className: "f-grid-search k-margin5", iconClass: "k-icon",
										click: function(e) {  e.preventDefault();
																var tr = $(e.target).closest("tr");
																var data = this.dataItem(tr);
																fnDeniedApprovalPopup(data.odCsId);
											            	}
										, visible: function(dataItem) {
											var hasAuth = false;

											if (!isAccept()) // 승인관리 페이지에서만
												return false;

											if (dataItem.apprStat == 'APPR_STAT.REQUEST') {
												if (!fnIsEmpty(dataItem.approvalSubUserId)) { // 1차 승인자 설정시, 1차 승인자(권한대행 포함)에게만 노출
													if (
														PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
														&& (PG_SESSION.loginId == dataItem.approvalSubUserId || PG_SESSION.loginId == dataItem.approvalSubGrantUserId)
													)
														hasAuth = true;
												}
												else { // 1차 승인자 미설정시, 2차 승인자(권한대행 포함)에게만 노출
													if (
														PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
														&& (PG_SESSION.loginId == dataItem.approvalUserId || PG_SESSION.loginId == dataItem.approvalGrantUserId)
													)
														hasAuth = true;
												}
											}
											else if (dataItem.apprStat == 'APPR_STAT.SUB_APPROVED') {
												if (
													PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
													&& (PG_SESSION.loginId == dataItem.approvalUserId || PG_SESSION.loginId == dataItem.approvalGrantUserId)
												)
													hasAuth = true;
											}

											return hasAuth;
										}

									}
						]
    				}
              ]
        };

		orderGrid = $('#orderGrid').initializeKendoGrid( orderGridOpt ).cKendoGrid();

    	orderGridEventUtil.noView(function () {fnMergeGridRows('orderGrid', orderMergeFieldArr, orderMergeGroupFieldArr);});	// 그리드 전체 건수 설정
		orderGridEventUtil.ckeckBoxAllClick();		// 그리드 체크박스 전체 클릭 설정
		orderGridEventUtil.checkBoxClick();			// 그리드 체크박스 클릭 설정

		$("#orderGrid").on("click", "tbody > tr > td", function(e) {
            let filedId = e.currentTarget.dataset.field;	//컬럼 정보
            let dataItems = orderGrid.dataItem(orderGrid.select()); //선택데이터 정보

            if(filedId === "odid") {
                let odid = dataItems.odid
                var orderMenuId = $("#lnbMenuList li").find("a[data-menu_depth='3'].active").attr("id");
                window.open("#/orderMgm?orderMenuId="+ orderMenuId +"&odid="+odid ,"popForm_"+odid);
            }
        });
	}

    function fnCancelRequestOne(odCsId){
		if( odCsId.length == 0 ){
			fnKendoMessage({ message : "선택된 클래임이 없습니다." });
			return;
		}
		let params = {};
		params.odCsIdList = [];
		params.odCsIdList[0] = odCsId;
		fnCancelRequest(params);
	}

	//선택 요청철회
	function fnSelectCancelRequest(){
		let selectRows  = $("#orderGrid").find("input[name=rowCheckbox]:checked").closest("tr");
		let params = {};
		params.odCsIdList = [];
		if( selectRows.length == 0 ){
			fnKendoMessage({ message : "선택된 클래임이 없습니다." });
			return;
		}
		for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){
			let dataItem = orderGrid.dataItem($(selectRows[i]));
			params.odCsIdList[i] = dataItem.odCsId;
		}
		fnCancelRequest(params);
	};

	//폐기
	function fnDisposal(odCsId){
    	if( odCsId.length == 0 ){
    		fnKendoMessage({ message : "선택된 클래임이 없습니다." });
    		return;
    	}
    	let params = {};
    	params.odCsIdList = [];
    	params.odCsIdList[0] = odCsId;

    	fnKendoMessage({
			type    : "confirm",
			message : "삭제 하시겠습니까?",
			ok      : function(){
		    	fnAjax({
		    		url     : "/admin/approval/csrefund/putDisposalApprovalCsRefund",
					params  : params,
					contentType : "application/json",
					success : function( data ){
						fnKendoMessage({  message : "변경이 완료되었습니다."
							, ok : function(){
								fnSearch();
							}
						});
					},
					error : function(xhr, status, strError){
						fnKendoMessage({ message : xhr.responseText });
					},
					isAction : "update"
				});
			},
			cancel  : function(){
			}
		});

    }

	//요청철회
	function fnCancelRequest(params){
		if( fnIsEmpty(params) || fnIsEmpty(params.odCsIdList) || params.odCsIdList.length < 1){
			fnKendoMessage({ message : "선택된 클래임이 없습니다." });
			return;
		}

    	fnKendoMessage({
			type    : "confirm",
			message : "요청철회 하시겠습니까?",
			ok      : function(){
				fnAjax({
					url     : "/admin/approval/csrefund/putCancelRequestApprovalCsRefund",
					params  : params,
					contentType : "application/json",
					success : function( data ){
						fnKendoMessage({  message : "승인요청 철회 되었습니다."
							, ok : function(){
								fnSearch();
							}
						});
					},
					error : function(xhr, status, strError){
						fnKendoMessage({ message : xhr.responseText });
					},
					isAction : "update"
				});
			},
			cancel  : function(){
			}
		});

	};

	function fnGetSelectType(odCsId) {
		if (odCsId == undefined) {
			viewModel.popupSelectType = SELECT_TYPE.CHECKBOX;
		} else {
			$('#odCsId').val(odCsId);
			viewModel.popupSelectType = SELECT_TYPE.BUTTON;
		}

		return viewModel.popupSelectType;
	}

	//승인 반려사유 팝업
	function fnDeniedApprovalPopup(odCsId){
		fnKendoInputPoup({height:"400px" ,width:"500px", title:{ nullMsg :'승인 반려 사유' } });
		fnGetSelectType(odCsId);
	}

	//승인처리
	function fnConfirmApproval(odCsId) {
		let params = {};
		let selectType = fnGetSelectType(odCsId);

		params.odCsIdList = [];
		params.apprStat = "APPR_STAT.APPROVED";

		if (SELECT_TYPE.BUTTON == selectType) {
			params.odCsIdList[0] = odCsId;
		}

		if (SELECT_TYPE.CHECKBOX == selectType) {
			let selectRows  = $("#orderGrid").find("input[name=rowCheckbox]:checked").closest("tr");

			for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){
				let dataItem = orderGrid.dataItem($(selectRows[i]));
				params.odCsIdList[i] = dataItem.odCsId;
			}
		}

		if( params.odCsIdList.length == 0 ){
			fnKendoMessage({ message : "선택된 클래임이 없습니다." });
			return;
		}

		fnApprovalProcess(params);
	}

	//승인반려
	function fnDeniedApproval() {
		let params = {};
		let selectType = viewModel.popupSelectType;

		params.odCsIdList = [];
		params.apprStat = "APPR_STAT.DENIED";
		params.statusComment = $('#statusComment').val();

		if( fnIsEmpty(params.statusComment)){
			fnKendoMessage({ message : "승인 반려 사유를 입력 해주세요." });
			return;
		}

		if (SELECT_TYPE.BUTTON == selectType) {
			params.odCsIdList[0] = $('#odCsId').val();
		}

		if (SELECT_TYPE.CHECKBOX == selectType) {
			let selectRows  = $("#orderGrid").find("input[name=rowCheckbox]:checked").closest("tr");

			for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){
				let dataItem = orderGrid.dataItem($(selectRows[i]));
				params.odCsIdList[i] = dataItem.odCsId;
			}
		}

		if( params.odCsIdList.length == 0 ){
			fnKendoMessage({ message : "선택된 클래임이 없습니다." });
			return;
		}

		fnApprovalProcess(params);
	}

	// 승인처리
	function fnApprovalProcess(params){
		if( fnIsEmpty(params) || fnIsEmpty(params.odCsIdList) || params.odCsIdList.length < 1 || fnIsEmpty(params.apprStat)){
			fnKendoMessage({ message : "선택된 클래임이 없습니다." });
			return;
		}

    	var confirmMessage = '';
    	if (params.apprStat == 'APPR_STAT.APPROVED')
    		confirmMessage = '승인처리 하시겠습니까?';
    	else if (params.apprStat == 'APPR_STAT.DENIED')
    		confirmMessage = '승인반려 처리하시겠습니까?';
    	else
    		return;

    	fnKendoMessage({
			type    : "confirm",
			message : confirmMessage,
			ok      : function(){
				fnAjax({
					url     : "/admin/approval/csrefund/putApprovalProcessCsRefund",
					params  : params,
					contentType : "application/json",
					success : function( data ){
					    if(params.apprStat == "APPR_STAT.APPROVED") {
					        fnKendoMessage({  message : "승인처리가 완료 되었습니다.", ok : function(){fnSearch();}});
					    }
					    if(params.apprStat == "APPR_STAT.DENIED") {
					        fnKendoMessage({  message : "승인반려 상태로 변경하였습니다.", ok : function(){fnClose(); fnSearch();}});
					    }
					},
					error : function(xhr, status, strError){
						fnKendoMessage({ message : xhr.responseText });
					},
					isAction : "update"
				});
			},
			cancel  : function(){
			}
		});

	};

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		$('#odCsId').val("");
		$('#statusComment').val("");
		kendoWindow.close();
	}

    //==================================================================================
    //--------------- Initialize Option Box Start --------------------------------------
    //==================================================================================
    function fnInitOptionBox() {

    	$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

    	$('#kendoPopupDenyInfo').kendoWindow({
			visible: false,
			modal: true
		});

    	var searchData = orderSearchUtil.searchDataMutil();
		var searchDataSingle = orderSearchUtil.searchDataSingle();

		// 기간검색 데이터
		var dateSearchData = [
            { "CODE" : "CLAIM_REQUEST_DATE"	, "NAME" : "클레임요청일자" },
            { "CODE" : "CREATE_DATE"		, "NAME" : "주문일자" },
            { "CODE" : "PAY_DATE"			, "NAME" : "결제일자" }
        ];

		var ApprUserSearchType = [
			{"CODE" : "ID", "NAME" : "아이디"},
			{"CODE" : "NAME", "NAME" : "이름"}
		];

		// 단일조건,복수조건 radio
		mutilSearchCommonUtil.default();
		// 기간검색 DropDown
		//orderSearchUtil.dateSearch(dateSearchData);

		//전체 초기화 버튼 클릭시 필요한 초기화 요소 추가 2021-05-20 임상건 [HGRM-8162] [승인요청내역공통] 기간 검색 초기화
		fnKendoDropDownList({
			id : "dateSearchType",
			tagId : "dateSearchType",
			data : dateSearchData
		});
		// 시작일
		fnKendoDatePicker({
			id: "dateSearchStart",
			format: "yyyy-MM-dd",
			btnStartId: "dateSearchStart",
			btnEndId: "dateSearchEnd",
			defVal: fnGetToday(),
            defType : 'oneMonth',
			change : function(e) {
				fnDateValidation(e, "start", "dateSearchStart", "dateSearchEnd", minusDay);
				 fnValidateCal(e)
			}
		});
		// 종료일
		fnKendoDatePicker({
			id: "dateSearchEnd",
			format: "yyyy-MM-dd",
			btnStyle: true,
			btnStartId: "dateSearchStart",
			btnEndId: "dateSearchEnd",
			defVal: fnGetToday(),
            defType : 'oneMonth',
			minusCheck: true,
			nextDate: false,
			change : function(e) {
				fnDateValidation(e, "end", "dateSearchStart", "dateSearchEnd");
				fnValidateCal(e)
			}
		});

		$("#dateSearchStart, #dateSearchEnd").off().on('keydown', function(e) {
			if (e.keyCode == 13) {
				return false;
			}
		});
		//전체 초기화 버튼 클릭시 필요한 초기화 요소 추가 2021-05-20 임상건 [HGRM-8162] [승인요청내역공통] 기간 검색 초기화

		// 승인 상태
    	fnTagMkChkBox({
	        id    : "searchApprovalStatus",
	        url : "/admin/comn/getCodeList",
	        params : {"stCommonCodeMasterCode" : "APPR_STAT", "useYn" :"Y"},
	        tagId : 'searchApprovalStatus',
	        async : false,
	        chkVal: '',
	        style : {},
	        textField :"NAME",
			valueField : "CODE",
	        beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }]
	    });
		$('input:checkbox[name="searchApprovalStatus"]:input[value="APPR_STAT.SAVE"]').parent().remove();
	    $('input:checkbox[name="searchApprovalStatus"]:input[value="APPR_STAT.NONE"]').parent().remove();
	    $('input:checkbox[name="searchApprovalStatus"]:input[value="APPR_STAT.DISPOSAL"]').parent().remove();
	    $('input:checkbox[name="searchApprovalStatus"]:input[value="APPR_STAT.APPROVED"]').next().text("승인완료(관리자)");

	    fbCheckboxChange();

	    fnKendoDropDownList({
    		id : "searchApprReqUserType",
    		data : ApprUserSearchType,
    		value: "ID",
    		tagId : "searchApprReqUserType"
        });

		fnKendoDropDownList({
			id : "searchApprChgUserType",
			data : ApprUserSearchType,
			value: "ID",
			tagId : "searchApprChgUserType"
		});

	    // CS환불구분(결제금액 환불, 적립금 환불) CheckBox
	    orderSearchUtil.getCheckBoxLocalData(orderOptionUtil.csRefundTp);

	    // 검색어(복수, 단일) DropDown
		orderSearchUtil.searchTypeKeyword(searchData,searchDataSingle);

	}

	// 승인반려 사유정보 팝업
	function fnOpenApprDinedResonPopup(data) {
		$('#apprDeniedReasonInfo').val(data.apprStatCmnt);
		fnKendoInputPoup({id: "kendoPopupDenyInfo", height:"370px" ,width:"520px",title:{nullMsg :'반려사유'} });
	}

	// 승인반려 사유정보 팝업 닫기
	function fnCloseApprDinedResonPopup() {
		var kendoWindow =$('#apprDeniedReasonInfo').data('kendoWindow');
		$('#apprDeniedReasonInfo').val("");
		kendoWindow.close();
	}

    //==================================================================================
	//---------------Initialize Option Box End ------------------------------------------------
    //==================================================================================

    //==================================================================================
	//------------------------------- Html 버튼 바인딩  Start -------------------------------
    //==================================================================================
    $scope.fnSearch     = function () { fnSearch(); };
    $scope.fnClear      = function () { fnClear() ; };
    $scope.fnClose 		= function( ){  fnClose();};

    $scope.fnDeniedApproval = function() { fnDeniedApproval(); };
	$scope.fnDeniedApprovalPopup = function() { fnDeniedApprovalPopup(); };
	$scope.fnConfirmApprovalChkbox = function() { fnConfirmApproval(); };
	$scope.fnSelectCancelRequest = function( ){  fnSelectCancelRequest();}
	$scope.fnOpenApprDinedResonPopup = function(data){ fnOpenApprDinedResonPopup(data); };
	$scope.fnCloseApprDinedResonPopup = function(){ fnCloseApprDinedResonPopup(); };
    //==================================================================================
	//------------------------------- Html 버튼 바인딩  End ----------------------------
    //==================================================================================

}); // document ready - END
