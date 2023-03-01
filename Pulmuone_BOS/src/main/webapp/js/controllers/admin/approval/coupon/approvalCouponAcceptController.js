/**-----------------------------------------------------------------------------
 * description 		 : 쿠폰 승인 처리 조회
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.05		박승현          최초생성
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
			PG_ID  : 'approvalCouponAccept',
			callback : fnUI
		});

	}

	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnDefaultSet();

		fnSearch();

	}

	//--------------------------------- Button Start---------------------------------
	function fnInitButton(){
		$('#fnSearch, #fnClear, #fnRejectReasonSave, #fnSelectApproval').kendoButton();
	}
	function fnSearch(){
		var data;
		if($("input[name=searchApprovalStatus]:checked").length < 1){
			fnKendoMessage({ message : "승인상태를 선택하여 조회하십시오." });
			return;
		}
		if( $("#searchDisplayCouponName").val().length == 1 ){
    		fnKendoMessage({ message : "1글자 이상 조회하십시오.",
    			ok : function(e) { $("#searchDisplayCouponName").focus(); }
    		});
    		return;
    	}
		if( $("#searchBosCouponName").val().length == 1 ){
			fnKendoMessage({ message : "1글자 이상 조회하십시오.",
				ok : function(e) { $("#searchBosCouponName").focus(); }
			});
			return;
		}
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
	// 초기화
	function fnClear() {
		$("#searchForm").formClear(true);
		fnDefaultSet();
	};
	// 기본 설정
	function fnDefaultSet(){
		$('input[name=searchApprovalStatus]').each(function(idx, element) {
			if(idx == 1 || idx == 2 || idx == 3) $(element).prop('checked', true);
			else $(element).prop('checked', false);
		});
		if(!(PG_SESSION == null || PG_SESSION == '' || PG_SESSION == undefined || PG_SESSION == 'undefined')) {
			$("#searchApprChgUser").val(PG_SESSION.loginId);
		}

		//전체 초기화 버튼 클릭시 필요한 초기화 요소 추가 2021-05-20 임상건 [HGRM-8162] [승인요청내역공통] 초기화 버튼 기능 수정 요청
		$('[data-id="fnDateBtnC"]').mousedown();
	};
	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		$('#pmCouponId').val("");
		$('#statusComment').val("");
		kendoWindow.close();
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){

		aGridDs = fnGetPagingDataSource({
			url      : '/admin/approval/coupon/getApprovalCouponList',
			pageSize : PAGE_SIZE
		});

		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 10
			}
			,navigatable: true
			,scrollable: true
			,columns   : [
				{ field : 'chk', headerTemplate : "<input type='checkbox' id='checkBoxAll' />", width:'50px', attributes : {style : "text-align:center;"}
	        		, template : function(dataItem){
	        			let returnValue = '';
						if (dataItem.apprStat == 'APPR_STAT.REQUEST') {
							if (!fnIsEmpty(dataItem.approvalSubUserId)) { // 1차 승인자 설정시, 1차 승인자(권한대행 포함)에게만 노출
								if (
									PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
									&& (PG_SESSION.loginId == dataItem.approvalSubUserId || PG_SESSION.loginId == dataItem.approvalSubGrantUserId)
								)
			        				returnValue = '<input type="checkbox" name="itemGridChk" class="itemGridChk" value="' + dataItem.pmCouponId + '" />';
							}
							else { // 1차 승인자 미설정시, 2차 승인자(권한대행 포함)에게만 노출
								if (
									PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
									&& (PG_SESSION.loginId == dataItem.approvalUserId || PG_SESSION.loginId == dataItem.approvalGrantUserId)
								)
			        				returnValue = '<input type="checkbox" name="itemGridChk" class="itemGridChk" value="' + dataItem.pmCouponId + '" />';
							}
						}
						else if (dataItem.apprStat == 'APPR_STAT.SUB_APPROVED') {
							if (
								PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
								&& (PG_SESSION.loginId == dataItem.approvalUserId || PG_SESSION.loginId == dataItem.approvalGrantUserId)
							)
		        				returnValue = '<input type="checkbox" name="itemGridChk" class="itemGridChk" value="' + dataItem.pmCouponId + '" />';
						}

	        			return returnValue;
	        		}
	    		}
				,{ title : 'No'		, width:'70px',attributes:{ style:'text-align:center' },template:"<span class='row-number'></span>"}
				,{ field:'couponTypeName'		,title : '종류(발급방법)'		, width:'130px',attributes:{ style:'text-align:center' }}
				,{ field:'displayCouponName'			,title : '전시쿠폰명'		, width:'200px',attributes:{ style:'text-align:left;text-decoration: underline;color:blue;'  }}
				,{ field:'bosCouponName'	,title : '관리자 쿠폰명'	, width:'200px',attributes:{ style:'text-align:left;text-decoration: underline;color:blue;'}}
				,{ field:'issueDate'	,title : '발급기간'	, width:'220px',attributes:{ style:'text-align:center' }}
				,{ field:'validityDate'	,title : '유효기간'	, width:'220px',attributes:{ style:'text-align:center' }}
				,{ field:'discountTypeName'	,title : '할인방식'	, width:'100px',attributes:{ style:'text-align:center' }}
				,{ field:'approvalRequestUserName', title : '승인요청자', width : '150px', attributes : {style : 'text-align:center'},
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
				,{ field:'apprStatName', title : '승인상태', width : '150px', attributes : {style : 'text-align:center'},
					template : function(dataItem) {
						if (dataItem.apprStat == "APPR_STAT.DENIED") {
							return "<a class='apprDenied' kind='apprDenied' style='color:blue; cursor:pointer' onclick='$scope.fnOpenApprDinedResonPopup(" + JSON.stringify(dataItem) + ");'>" + kendo.htmlEncode(dataItem.apprStatName) + "</a>";
						}
						else {
							return dataItem.apprStatName;
						}
					}
				}
				,{ title:'관리', width: "150px", attributes:{ style:'text-align:left;'  , class:'forbiz-cell-readonly' }
					,command: [ { text: '승인완료'		, imageClass: "k-i-add", className: "f-grid-add k-margin5", iconClass: "k-icon",
								   click: function(e) {  e.preventDefault();
												            var tr = $(e.target).closest("tr");
												            var data = this.dataItem(tr);
									   						fnConfirmApproval(SELECT_TYPE.BUTTON, data.pmCouponId);}
									,visible: function(dataItem) {
										var hasAuth = false;

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
								,{ text: '승인반려' , imageClass: "k-i-add", className: "f-grid-search k-margin5", iconClass: "k-icon",
									click: function(e) {  e.preventDefault();
											            	var tr = $(e.target).closest("tr");
											            	var data = this.dataItem(tr);
															fnDenieApprovalPopup(data.pmCouponId);
											            	}
									, visible: function(dataItem) {
										var hasAuth = false;

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
				,{ field:'pmCouponId'		, hidden:'true'}
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
	    // 그리드 전체선택 클릭
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
			if(colIdx == 3 || colIdx == 4){
				fnGridClick(e);
			}
		});

	}

	function fnGridClick(e){
		var dataItem = aGrid.dataItem($(e.target).closest('tr'));
		var sData = $('#searchForm').formSerialize(true);
		fnKendoPopup({
			id     : 'cpnMgmPut',
			title  : '쿠폰등록/수정',
			src    : '#/cpnMgmPut',
			param  : {pmCouponId :dataItem.pmCouponId },
			width  : '1300px',
			height : '1400px',
			success: function( id, data ){
				fnSearch();
			}
		});
	}

	function fnGetSelectType(pmCouponId) {
		if (pmCouponId == undefined) {
			viewModel.popupSelectType = SELECT_TYPE.CHECKBOX;
		} else {
			viewModel.pmCouponId = pmCouponId;
			viewModel.popupSelectType = SELECT_TYPE.BUTTON;
		}

		return viewModel.popupSelectType;
	}

	//승인 반려사유 팝업
	function fnDenieApprovalPopup(pmCouponId){
		fnKendoInputPoup({height:"400px" ,width:"500px", title:{ nullMsg :'승인 반려 사유' } });
		$('#pmCouponId').val(pmCouponId);
		fnGetSelectType(pmCouponId);
	}

	//승인처리
	function fnConfirmApproval(SELECT_TYPE, pmCouponId) {
		let params = {};
		let selectType = fnGetSelectType(pmCouponId);

		params.pmCouponIdList = [];
		params.apprStat = "APPR_STAT.APPROVED";

		if (selectType == "SELECT_TYPE.BUTTON") {
			params.pmCouponIdList[0] = pmCouponId;
		}

		if (selectType == "SELECT_TYPE.CHECKBOX") {
			let selectRows  = $("#aGrid").find("input[name=itemGridChk]:checked").closest("tr");

			for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){
				let dataItem = aGrid.dataItem($(selectRows[i]));
				params.pmCouponIdList[i] = dataItem.pmCouponId;
			}
		}

		if( params.pmCouponIdList.length == 0 ){
			fnKendoMessage({ message : "선택된 쿠폰이 없습니다." });
			return;
		}

		fnApprovalProcess(params);
	}

	//승인반려
	function fnDenieApproval() {
		let params = {};
		let selectType = viewModel.popupSelectType;

		params.pmCouponIdList = [];
		params.apprStat = "APPR_STAT.DENIED";
		params.statusComment = $('#statusComment').val();

		if( fnIsEmpty(params.statusComment)){
			fnKendoMessage({ message : "승인 반려 사유를 입력 해주세요." });
			return;
		}

		if (selectType == "SELECT_TYPE.BUTTON") {
			params.pmCouponIdList[0] = $('#pmCouponId').val();
		}

		if (selectType == "SELECT_TYPE.CHECKBOX") {
			let selectRows  = $("#aGrid").find("input[name=itemGridChk]:checked").closest("tr");

			for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){
				let dataItem = aGrid.dataItem($(selectRows[i]));
				params.pmCouponIdList[i] = dataItem.pmCouponId;
			}
		}

		if( params.pmCouponIdList.length == 0 ){
			fnKendoMessage({ message : "선택된 쿠폰이 없습니다." });
			return;
		}

		fnApprovalProcess(params);
	}

    // 승인처리
    function fnApprovalProcess(params){
    	if( fnIsEmpty(params) || fnIsEmpty(params.pmCouponIdList) || params.pmCouponIdList.length < 1 || fnIsEmpty(params.apprStat)){
    		fnKendoMessage({ message : "선택된 쿠폰이 없습니다." });
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
		    		url     : "/admin/approval/coupon/putApprovalProcessCoupon",
		    		params  : params,
		    		contentType : "application/json",
		    		success : function( data ){
		    			fnKendoMessage({  message : "승인 처리가 완료되었습니다."
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
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

    	$('#kendoPopupDenyInfo').kendoWindow({
			visible: false,
			modal: true
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
	    $('input:checkbox[name="searchApprovalStatus"]:input[value="APPR_STAT.DISPOSAL"]').parent().remove();
	    $('input:checkbox[name="searchApprovalStatus"]:input[value="APPR_STAT.CANCEL"]').parent().remove();

	    $('input:checkbox[name="searchApprovalStatus"]:input[value="APPR_STAT.APPROVED"]').next().text("승인완료(관리자)");

	    //fbCheckboxChange();
	    $('#searchApprovalStatus').bind("change", onCheckboxWithTotalChange);

		fnTagMkRadio({
			id: "searchCouponType" ,
			tagId : 'searchCouponType',
			url : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "COUPON_TYPE", "useYn" :"Y"},
			async : false,
			beforeData : [
							{"CODE":"", "NAME":"전체"},
						],
			chkVal: '',
			style : {}
		});
		/* 발급방법 */
		fnKendoDropDownList({
				id  : 'searchIssueType',
		        url : "/admin/comn/getCodeList",
		        params : {"stCommonCodeMasterCode" : "PAYMENT_TYPE", "useYn" :"Y"},
		        textField :"NAME",
				valueField : "CODE",
				blank : "전체"
		});

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
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------
	function onCheckboxWithTotalChange(e) {   // 체크박스 change event

        // 첫번째 체크박스가 '전체' 체크박스라 가정함
        var totalCheckedValue = $("input[name=" + e.target.name + "]:eq(0)").attr('value');

        if( e.target.value == totalCheckedValue ) {  // '전체' 체크 or 체크 해제시

            if( $("input[name=" + e.target.name + "]:eq(0)").is(":checked") ) {  // '전체' 체크시

                $("input[name=" + e.target.name + "]:gt(0)").each(function(idx, element){
                    $(element).prop('checked', true);  // 나머지 모두 체크
                });

            } else { // '전체' 체크 해제시

                $("input[name=" + e.target.name + "]:gt(0)").each(function(idx, element){
                    $(element).prop('checked', false);  // 나머지 모두 체크 해제
                });

            }

        } else { // 나머지 체크 박스 중 하나를 체크 or 체크 해재시

            var allChecked = true; // 나머지 모두 체크 상태인지 flag

            $("input[name=" + e.target.name + "]:gt(0)").each(function(idx, element) {
                if( $(element).prop('checked') == false ) {
                    allChecked = false;  // 하나라도 체크 해제된 상태가 있는 경우 flag 값 false
                }
            });

            if( allChecked ) { // 나머지 모두 체크 상태인 경우
                $("input[name=" + e.target.name + "]:eq(0)").prop('checked', true);  // 나머지 모두 '전체' 체크
            } else {
                $("input[name=" + e.target.name + "]:eq(0)").prop('checked', false);  // 나머지 모두 '전체' 체크 해제
            }

        }

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

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};

	$scope.fnDenieApprovalPopup = function() { fnDenieApprovalPopup() };
	$scope.fnDenieApproval = function() { fnDenieApproval(); };
	$scope.fnConfirmApprovalChkbox = function() { fnConfirmApproval() };
	$scope.fnOpenApprDinedResonPopup = function(data){ fnOpenApprDinedResonPopup(data); };
	$scope.fnCloseApprDinedResonPopup = function(){ fnCloseApprDinedResonPopup(); };

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
