/**-----------------------------------------------------------------------------
 * description : 기획전 승인목록 처리
 * @
 * @ 아래 기획전리스트 공통 처리
 * @  - 골라담기(균일가) 승인요청 내역 : approvalExhibitSelectRequestController.js, approvalExhibitSelectRequest.html
 * @  - 증정행사 승인요청 내역 : approvalExhibitGiftRequestController.js  , approvalExhibitGiftRequest.html
 * @  - 골라담기(균일가) 승인관리 : approvalExhibitSelectAcceptController.js, approvalExhibitSelectAccept.html
 * @  - 증정행사 승인관리 : approvalExhibitGiftAcceptController.js  , approvalExhibitGiftAccept.html
 * @
 * @ 수정일			수정자		수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.02.08		박승현		최초생성
 * @
 * **/
'use strict';
var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var viewModel = new kendo.data.ObservableObject({});
var SELECT_TYPE = {'BUTTON':'SELECT_TYPE.BUTTON', 'CHECKBOX':'SELECT_TYPE.CHECKBOX'}

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID    : pageId,
			callback : fnUI
        });

		// ------------------------------------------------------------------------
	    // 페이지파라미터
	    // ------------------------------------------------------------------------

	    // ------------------------------------------------------------------------
	    // 기획전 승인 요청 목록, 승인 대상 목록 Set
	    //   - approvalExhibitSelectRequestController.js, approvalExhibitGiftRequestController.js 에서 설정 됨
		//   - approvalExhibitSelectAcceptController.js, approvalExhibitGiftAcceptController.js 에서 설정 됨
	    // ------------------------------------------------------------------------
	    console.log('# gbExhibitTp :: ', gbExhibitTp);
	    console.log('# pageId :: ', pageId);
	    $('#exhibitTp').val(gbExhibitTp);

	    // ------------------------------------------------------------------------
	    // 상위타이틀
	    // ------------------------------------------------------------------------
	    if(!fnIsEmpty(pageId)) {
	    	if (pageId == 'approvalExhibitSelectRequest') {
	    		$('#pageTitleSpan').text('골라담기(균일가) 승인요청 내역');
	    	}else if (pageId == 'approvalExhibitGiftRequest') {
	    		$('#pageTitleSpan').text('증정행사 승인요청 내역');
	    	}else if (pageId == 'approvalExhibitSelectAccept') {
	    		$('#pageTitleSpan').text('골라담기(균일가) 승인관리');
	    	}else if (pageId == 'approvalExhibitGiftAccept') {
	    		$('#pageTitleSpan').text('증정행사 승인관리');
	    	}
	    }

    }

	function fnUI(){

		fnTranslate();			// comm.lang.js 안에 있는 공통함수 다국어 변환--------------------------------------------

		fnInitButton();			//Initialize Button  ---------------------------------

		fnInitGrid();			//Initialize Grid ------------------------------------

		fnInitOptionBox();		//Initialize Option Box ------------------------------------

		fnDefaultSet();			// 기본설정 -------------------------------------

		fnSearch();				// 조회 -----------------------------------------
	}

	//--------------------------------- Button Start---------------------------------
	function fnInitButton() {
		$('#fnSearch, #fnClear, #fnRejectReasonSave, #fnSelectApproval, #fnSelectCancelRequest').kendoButton();
		if(pageId == 'approvalExhibitSelectRequest' || pageId == 'approvalExhibitGiftRequest'){
			$('#fnSelectCancelRequest').show();
			$('#fnProcessApproved').hide();
			$('#fnProcessDenied').hide();
		}else if(pageId == 'approvalExhibitSelectAccept' || pageId == 'approvalExhibitGiftAccept'){
			$('#fnSelectCancelRequest').hide();
			$('#fnProcessApproved').show();
			$('#fnProcessDenied').show();
		}
	}

	function fnClear() {
		$('#searchForm').formClear(true);
		fnDefaultSet();
	}
	// 기본 설정
	function fnDefaultSet(){
		$('input[name=searchApprovalStatus]').each(function(idx, element) {
			if(element.value == "ALL" || element.value == "APPR_STAT.APPROVED" || element.value == "APPR_STAT.APPROVED_BY_SYSTEM"){
				$(element).prop('checked', false);
			}
			else{
				$(element).prop('checked', true);
			}
		});
		if(!(PG_SESSION == null || PG_SESSION == '' || PG_SESSION == undefined || PG_SESSION == 'undefined')) {
			if(pageId == 'approvalExhibitSelectRequest' || pageId == 'approvalExhibitGiftRequest')
				$("#searchApprReqUser").val(PG_SESSION.loginId);
			if(pageId == 'approvalExhibitSelectAccept' || pageId == 'approvalExhibitGiftAccept')
				$("#searchApprChgUser").val(PG_SESSION.loginId);
		}
		//전체 초기화 버튼 클릭시 필요한 초기화 요소 추가 2021-05-20 임상건 [HGRM-8162] [승인요청내역공통] 초기화 버튼 기능 수정 요청
		$('[data-id="fnDateBtnC"]').mousedown();
		//[HGRM-8240] [증정행사승인] 초기화 후 데이터 조회시 데이터 노출하지 않음, 2021-05-24 임상건
		$('#exhibitTp').val(gbExhibitTp);
	};

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		$('#evExhibitId').val("");
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

    	fnKendoDropDownList({
	        id      : 'searchKeywordType'
	      , tagId   : 'searchKeywordType'
	      , data    : [
	                    {'CODE':'TITLE','NAME':'기획전명'}
	                  , {'CODE':'ID'  ,'NAME':'기획전ID'}
	                  ]
	      , chkVal  : 'TITLE'
	    });

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
		$('input:checkbox[name="searchApprovalStatus"]:input[value="APPR_STAT.APPROVED_BY_SYSTEM"]').parent().remove();
	    $('input:checkbox[name="searchApprovalStatus"]:input[value="APPR_STAT.DISPOSAL"]').parent().remove();
	    if(pageId == 'approvalExhibitSelectAccept' || pageId == 'approvalExhibitGiftAccept')
	    	$('input:checkbox[name="searchApprovalStatus"]:input[value="APPR_STAT.CANCEL"]').parent().remove();

	    $('#searchApprovalStatus').bind("change", onCheckboxWithTotalChange);

	    fnKendoDropDownList({
    		id : "searchApprReqUserType",
    		data : [ {"CODE" : "ID", "NAME" : "아이디"},
    				{"CODE" : "NAME", "NAME" : "이름"}
    			],
    		value: "ID",
    		tagId : "searchApprReqUserType"
        });

		fnKendoDropDownList({
			id : "searchApprChgUserType",
			data : [ {"CODE" : "ID", "NAME" : "아이디"},
				{"CODE" : "NAME", "NAME" : "이름"}
			],
			value: "ID",
			tagId : "searchApprChgUserType"
		});

		fnKendoDatePicker({
			id    : 'approvalReqStartDate',
			format: 'yyyy-MM-dd',
			btnStartId : 'approvalReqStartDate',
			btnEndId : 'approvalReqEndDate',
			change: function() {
				$("#approvalReqEndDate").data("kendoDatePicker").min($("#approvalReqStartDate").val());
			}
		});
		fnKendoDatePicker({
			id    : 'approvalReqEndDate',
			format: 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'approvalReqStartDate',
			btnEndId : 'approvalReqEndDate',
			change: function() {
				$("#approvalReqStartDate").data("kendoDatePicker").max($("#approvalReqEndDate").val());
			}
		});
		fnKendoDatePicker({
			id    : 'approvalChgStartDate',
			format: 'yyyy-MM-dd',
			btnStartId : 'approvalChgStartDate',
			btnEndId : 'approvalChgEndDate',
			change: function() {
				$("#approvalChgEndDate").data("kendoDatePicker").min($("#approvalChgStartDate").val());
			}
		});
		fnKendoDatePicker({
			id    : 'approvalChgEndDate',
			format: 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'approvalChgStartDate',
			btnEndId : 'approvalChgEndDate',
			change: function() {
				$("#approvalChgStartDate").data("kendoDatePicker").max($("#approvalChgEndDate").val());
			}
		});

    }
    //--- 검색  -----------------
	function fnSearch() {
		if($('#searchKeywordType').val() == 'TITLE') {
			var keyword = $('#searchKeyword').val();
			if(!fnIsEmpty(keyword) && keyword != 'null') {
				keyword = $.trim(keyword);
				if(keyword.length < 2) {
					fnKendoMessage({ message : "기획전명을 두글자 이상 입력하세요." });
					return false;
				}
			}
		}
		var data;
		data = $('#searchForm').formSerialize(true);
		var query = {
					page			: 1
					, pageSize      : PAGE_SIZE
					, filterLength  : fnSearchData(data).length
					, filter        : {
						filters : fnSearchData(data)
					}
		};
		aGridDs.query(query);
	}

	//--------------------------------------------------------------------------
	//------------------------------- Grid Start -------------------------------
	//--------------------------------------------------------------------------
    function fnInitGrid() {

		aGridDs = fnGetPagingDataSource({
			url      : '/admin/approval/exhibit/getApprovalExhibitList'
			, pageSize : PAGE_SIZE
			, requestEnd: function(e) {
                //$('#countTotalSpan').text(kendo.toString(e.response.total, "n0"));
            }
		});

		aGridOpt = {
	              dataSource: aGridDs
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
								if (pageId == 'approvalExhibitSelectAccept' || pageId == 'approvalExhibitGiftAccept') {
									if (dataItem.apprStat == 'APPR_STAT.REQUEST') {
										if (!fnIsEmpty(dataItem.approvalSubUserId)) { // 1차 승인자 설정시, 1차 승인자(권한대행 포함)에게만 노출
											if (
												PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
												&& (PG_SESSION.loginId == dataItem.approvalSubUserId || PG_SESSION.loginId == dataItem.approvalSubGrantUserId)
											)
					            				returnValue = '<input type="checkbox" name="itemGridChk" class="itemGridChk" value="' + dataItem.evExhibitId + '" />';
										}
										else { // 1차 승인자 미설정시, 2차 승인자(권한대행 포함)에게만 노출
											if (
												PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
												&& (PG_SESSION.loginId == dataItem.approvalUserId || PG_SESSION.loginId == dataItem.approvalGrantUserId)
											)
					            				returnValue = '<input type="checkbox" name="itemGridChk" class="itemGridChk" value="' + dataItem.evExhibitId + '" />';
										}
									}
									else if (dataItem.apprStat == 'APPR_STAT.SUB_APPROVED') {
										if (
											PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
											&& (PG_SESSION.loginId == dataItem.approvalUserId || PG_SESSION.loginId == dataItem.approvalGrantUserId)
										)
				            				returnValue = '<input type="checkbox" name="itemGridChk" class="itemGridChk" value="' + dataItem.evExhibitId + '" />';
									}
								}
								else if (pageId == 'approvalExhibitSelectRequest' || pageId == 'approvalExhibitGiftRequest') {
									if (dataItem.apprStat == 'APPR_STAT.REQUEST') {
										if (
												PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
												&& PG_SESSION.loginId == dataItem.approvalRequestUserId
											)
				            				returnValue = '<input type="checkbox" name="itemGridChk" class="itemGridChk" value="' + dataItem.evExhibitId + '" />';
									}
								}

								return returnValue;
		            		}
	            		}
	                    , {title : 'No'   , width : '60px' , attributes : {style : 'text-align:center'}, template:"<span class='row-number'></span>", type:"number", format: "{0:n0}"}
	                    , {field : 'evExhibitId' , title : '기획전ID' , width : '120px' , attributes : {style : 'text-align:center'}}
	                    , {field : 'mallDivName' , title : '몰구분' , width : '200px' , attributes : {style : 'text-align:center'}}
	                    , {field : 'title' , title : '기획전 제목' , width : '200px' , attributes : {style : 'text-align:center;text-decoration: underline;color:blue;'}}
	                    , {field : 'startDt' , title : '시작일자' , width : '100px' , attributes : {style : 'text-align:center'}}
	                    , {field : 'endDt'	, title : '종료일자'	, width:'100px' , attributes:{ style:'text-align:center' }}
	                    , {field : 'approvalRequestUserName', title : '승인요청자', width : '150px', attributes : {style : 'text-align:center'},
								template : function(dataItem) {
									if (fnIsEmpty(dataItem.approvalRequestUserName))
		        	                    return '';
									else
										return dataItem.approvalRequestUserName + "(" + dataItem.approvalRequestUserId + ")";
		                    	}
							}
	                    , {field : 'approvalRequestDt'	,title : '승인요청일'	, width:'100px',attributes:{ style:'text-align:center' }}
						, {field : 'approvalChangeDt'	,title : '승인처리일<BR>(1차/최종)'	, width:'100px',attributes:{ style:'text-align:center' },
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
						, {field : 'approvalSubUserName', title : '1차 승인관리자<BR>/권한위임자', width : '150px', attributes : {"class" : "#=(!fnIsEmpty(approvalSubChangeDt))?'bgColorGreen':''#",style : 'text-align:center'},
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
						, {field : 'approvalUserName', title : '최종승인관리자<BR>/권한위임자', width : '150px', attributes : {"class" : "#=(!fnIsEmpty(approvalChangeDt))?'bgColorGreen':''#", style : 'text-align:center'},
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
															            fnCancelRequestOne(data.evExhibitId);}
												,visible: function(dataItem) {
													var hasAuth = false;

													if (pageId != 'approvalExhibitSelectRequest' && pageId != 'approvalExhibitGiftRequest')
														return false;

													if (dataItem.apprStat != 'APPR_STAT.REQUEST')
														return false;

													if (!fnIsProgramAuth("CANCEL_LIST_REQ"))
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
													fnDisposal(data.evExhibitId);
												}
												, visible: function(dataItem) {
													return ( (pageId == 'approvalExhibitSelectRequest' || pageId == 'approvalExhibitGiftRequest')
																&& (dataItem.apprStat =="APPR_STAT.DENIED" || dataItem.apprStat =="APPR_STAT.CANCEL" )
															)
												}
						                    }
											, { text: '승인완료'		, imageClass: "k-i-add", className: "f-grid-add k-margin5", iconClass: "k-icon",
												click: function(e) {  e.preventDefault();
																		var tr = $(e.target).closest("tr");
																		var data = this.dataItem(tr);
																		fnConfirmApproval(SELECT_TYPE.BUTTON, data.evExhibitId);}
												,visible: function(dataItem) {
													var hasAuth = false;

													if (pageId != 'approvalExhibitSelectAccept' && pageId != 'approvalExhibitGiftAccept') // 승인관리 페이지에서만
														return false;

													if (!fnIsProgramAuth("CONFIRM_LIST_APPRL"))
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
																		fnDenieApprovalPopup(data.evExhibitId);
													            	}
												, visible: function(dataItem) {
													var hasAuth = false;

													if (pageId != 'approvalExhibitSelectAccept' && pageId != 'approvalExhibitGiftAccept') // 승인관리 페이지에서만
														return false;

													if (!fnIsProgramAuth("DENY_LIST_APPRL"))
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

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

        aGrid.bind("dataBound", function(){
        	var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
        	$("#aGrid tbody > tr .row-number").each(function(index){
        		$(this).html(row_num);
        		row_num--;
        	});
        	$('#countTotalSpan').text(aGridDs._total);
        });

        $("#checkBoxAll").prop("checked", false);
	    //그리드 전체선택 클릭
        $("#checkBoxAll").on("click", function(index){
        	if( $("#checkBoxAll").prop("checked") ){
        		$("input[name=itemGridChk]").prop("checked", true);
        	}else{
        		$("input[name=itemGridChk]").prop("checked", false);
        	}
        });

        // 그리드 체크박스 클릭
        aGrid.element.on("click", "[name=itemGridChk]" , function(e){
        	if( e.target.checked ){
        		if( $("[name=itemGridChk]").length == $("[name=itemGridChk]:checked").length ){
        			$("#checkBoxAll").prop("checked", true);
        		}
            }else{
                $("#checkBoxAll").prop("checked", false);
            }
        });

        $(aGrid.tbody).on("click", "td", function (e) {
			var row = $(this).closest("tr");
			var rowIdx = $("tr", aGrid.tbody).index(row);
			var colIdx = $("td", row).index(this);
			let dataItem = aGrid.dataItem($(e.currentTarget).closest('tr'));

			if(colIdx == 4){
				//fnGridClick(e);
				fnExhibitEdit(dataItem);
			}
		});
    }
    // ------------------------------- Grid End ---------------------------------
    function fnExhibitEdit(dataItem) {

        // ------------------------------------------------------------------------
        // 수정 화면으로 이동
        // ------------------------------------------------------------------------
        // 링크정보
        let option = {};

        console.log("dataItem : ", dataItem);

        option.url    = '#/exhibitMgm';
        option.menuId = 959;;
        option.target = '_blank';
        option.data = { exhibitTp : gbExhibitTp, evExhibitId : dataItem.evExhibitId, mode : 'update'};
        // 화면이동
        fnGoNewPage(option);  // 새페이지(탭)
      }

    function onCheckboxWithTotalChange(e) {   // 체크박스 change event
    	//첫번째 체크박스가 '전체' 체크박스라 가정함
    	var totalCheckedValue = $("input[name=" + e.target.name + "]:eq(0)").attr('value');
    	if( e.target.value == totalCheckedValue ) {  // '전체' 체크 or 체크 해제시
    		if( $("input[name=" + e.target.name + "]:eq(0)").is(":checked") ) {  // '전체' 체크시
    			$("input[name=" + e.target.name + "]:gt(0)").each(function(idx, element){
    				$(element).prop('checked', true);  // 나머지 모두 체크
    			});
    		}else { // '전체' 체크 해제시
    			$("input[name=" + e.target.name + "]:gt(0)").each(function(idx, element){
    				$(element).prop('checked', false);  // 나머지 모두 체크 해제
    			});
    		}
    	}else { // 나머지 체크 박스 중 하나를 체크 or 체크 해재시
    		var allChecked = true; // 나머지 모두 체크 상태인지 flag
    		$("input[name=" + e.target.name + "]:gt(0)").each(function(idx, element) {
    			if( $(element).prop('checked') == false ) {
    				allChecked = false;  // 하나라도 체크 해제된 상태가 있는 경우 flag 값 false
    			}
    		});
    		if( allChecked ) { // 나머지 모두 체크 상태인 경우
    			$("input[name=" + e.target.name + "]:eq(0)").prop('checked', true);  // 나머지 모두 '전체' 체크
    		}else {
    			$("input[name=" + e.target.name + "]:eq(0)").prop('checked', false);  // 나머지 모두 '전체' 체크 해제
    		}
    	}
    }

    function fnGetSelectType(evExhibitId) {
		if (evExhibitId == undefined) {
			viewModel.popupSelectType = SELECT_TYPE.CHECKBOX;
		} else {
			viewModel.evExhibitId = evExhibitId;
			viewModel.popupSelectType = SELECT_TYPE.BUTTON;
		}

		return viewModel.popupSelectType;
	}

	//승인 반려사유 팝업
	function fnDenieApprovalPopup(evExhibitId){
		fnKendoInputPoup({height:"400px" ,width:"500px", title:{ nullMsg :'승인 반려 사유' } });
		$('#evExhibitId').val(evExhibitId);
		fnGetSelectType(evExhibitId);
	}

	//승인처리
	function fnConfirmApproval(SELECT_TYPE, evExhibitId) {
		let params = {};
		let selectType = fnGetSelectType(evExhibitId);

		params.evExhibitIdList = [];
		params.apprStat = "APPR_STAT.APPROVED";

		if (selectType == "SELECT_TYPE.BUTTON") {
			params.evExhibitIdList[0] = evExhibitId;
		}

		if (selectType == "SELECT_TYPE.CHECKBOX") {
			let selectRows  = $("#aGrid").find("input[name=itemGridChk]:checked").closest("tr");

			for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){
				let dataItem = aGrid.dataItem($(selectRows[i]));
				params.evExhibitIdList[i] = dataItem.evExhibitId;
			}
		}

		if( params.evExhibitIdList.length == 0 ){
			fnKendoMessage({ message : "선택된 기획전이 없습니다." });
			return;
		}

		/*console.log("params : ", params);
		return;*/

		fnApprovalProcess(params);
	}

	//승인반려
	function fnDenieApproval() {
		let params = {};
		let selectType = viewModel.popupSelectType;

		params.evExhibitIdList = [];
		params.apprStat = "APPR_STAT.DENIED";
		params.statusComment = $('#statusComment').val();

		if( fnIsEmpty(params.statusComment)){
			fnKendoMessage({ message : "승인 반려 사유를 입력 해주세요." });
			return;
		}

		if (selectType == "SELECT_TYPE.BUTTON") {
			params.evExhibitIdList[0] = $('#evExhibitId').val();
		}

		if (selectType == "SELECT_TYPE.CHECKBOX") {
			let selectRows  = $("#aGrid").find("input[name=itemGridChk]:checked").closest("tr");

			for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){
				let dataItem = aGrid.dataItem($(selectRows[i]));
				params.evExhibitIdList[i] = dataItem.evExhibitId;
			}
		}

		if( params.evExhibitIdList.length == 0 ){
			fnKendoMessage({ message : "선택된 기획전이 없습니다." });
			return;
		}

		fnApprovalProcess(params);
	}

    function fnCancelRequestOne(evExhibitId){
		if( evExhibitId.length == 0 ){
			fnKendoMessage({ message : "선택된 기획전이 없습니다." });
			return;
		}
		let params = {};
		params.evExhibitIdList = [];
		params.evExhibitIdList[0] = evExhibitId;
		fnCancelRequest(params);
	}

	//선택 요청철회
	function fnSelectCancelRequest(){
		let selectRows  = $("#aGrid").find("input[name=itemGridChk]:checked").closest("tr");
		let params = {};
		params.evExhibitIdList = [];
		if( selectRows.length == 0 ){
			fnKendoMessage({ message : "선택된 기획전이 없습니다." });
			return;
		}
		for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){
			let dataItem = aGrid.dataItem($(selectRows[i]));
			params.evExhibitIdList[i] = dataItem.evExhibitId;
		}
		fnCancelRequest(params);
	};

	//폐기
	function fnDisposal(evExhibitId){
    	if( evExhibitId.length == 0 ){
    		fnKendoMessage({ message : "선택된 기획전이 없습니다." });
    		return;
    	}
    	let params = {};
    	params.evExhibitIdList = [];
    	params.evExhibitIdList[0] = evExhibitId;
		params.exhibitTp = gbExhibitTp;

    	fnKendoMessage({
			type    : "confirm",
			message : "삭제 하시겠습니까?",
			ok      : function(){
				fnAjax({
		    		url     : "/admin/approval/exhibit/putDisposalApprovalExhibit",
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
	// 승인처리
	function fnApprovalProcess(params){
		if( fnIsEmpty(params) || fnIsEmpty(params.evExhibitIdList) || params.evExhibitIdList.length < 1 || fnIsEmpty(params.apprStat)){
			fnKendoMessage({ message : "선택된 기획전이 없습니다." });
			return;
		}
		params.exhibitTp = gbExhibitTp;

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
					url     : "/admin/approval/exhibit/putApprovalProcessExhibit",
					params  : params,
					contentType : "application/json",
					success : function( data ){
						fnKendoMessage({  message : "저장이 완료되었습니다."
							, ok : function(){
								if(params.apprStat == "APPR_STAT.DENIED") fnClose();
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

	//요청철회
	function fnCancelRequest(params){
		if( fnIsEmpty(params) || fnIsEmpty(params.evExhibitIdList) || params.evExhibitIdList.length < 1){
			fnKendoMessage({ message : "선택된 기획전이 없습니다." });
			return;
		}
		params.exhibitTp = gbExhibitTp;

    	fnKendoMessage({
			type    : "confirm",
			message : "요청철회 하시겠습니까?",
			ok      : function(){
				fnAjax({
					url     : "/admin/approval/exhibit/putCancelRequestApprovalExhibit",
					params  : params,
					contentType : "application/json",
					success : function( data ){
						fnKendoMessage({  message : "저장이 완료되었습니다."
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
	//------------------------------- Html 버튼 바인딩  Start -------------------------------
    //==================================================================================
    /** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};

	$scope.fnDenieApprovalPopup = function() { fnDenieApprovalPopup(); };
	$scope.fnDenieApproval = function() { fnDenieApproval(); };
	$scope.fnConfirmApprovalChkbox = function() { fnConfirmApproval(); };
	$scope.fnSelectCancelRequest = function( ){  fnSelectCancelRequest();};
	$scope.fnOpenApprDinedResonPopup = function(data){ fnOpenApprDinedResonPopup(data); };
	$scope.fnCloseApprDinedResonPopup = function(){ fnCloseApprDinedResonPopup(); };

}); // document ready - END
