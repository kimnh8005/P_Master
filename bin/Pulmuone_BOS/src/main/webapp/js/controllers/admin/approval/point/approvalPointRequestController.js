﻿/**-----------------------------------------------------------------------------
 * description 		 : 포인트 승인요청 내역 조회
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.05		박승현          최초생성
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
			PG_ID    : 'approvalPointRequest',
			callback : fnUI
        });
    }

	function fnUI(){
		fnTranslate();	// comm.lang.js 안에 있는 공통함수 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnDefaultSet();

		fnSearch();

	}


	//--------------------------------- Button Start---------------------------------
	function fnInitButton() {
		$('#fnSearch, #fnClear, #fnSelectCancelRequest').kendoButton();
	}

    //--- 검색  -----------------
	function fnSearch(){
		var data;
		if($("input[name=searchApprovalStatus]:checked").length < 1){
			fnKendoMessage({ message : "승인상태를 선택하여 조회하십시오." });
			return;
		}
		if( $("#searchPointName").val().length == 1 ){
    		fnKendoMessage({ message : "1글자 이상 조회하십시오.",
    			ok : function(e) { $("#searchPointName").focus(); }
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
			$("#searchApprReqUser").val(PG_SESSION.loginId);
		}
		//전체 초기화 버튼 클릭시 필요한 초기화 요소 추가 2021-05-20 임상건 [HGRM-8162] [승인요청내역공통] 초기화 버튼 기능 수정 요청
		$('[data-id="fnDateBtnC"]').mousedown();
	};

	//--------------------------------- Button End---------------------------------


	//--------------------------------------------------------------------------
	//------------------------------- Grid Start -------------------------------
	//--------------------------------------------------------------------------
    function fnInitGrid() {

        aGridDs = fnGetPagingDataSource({
             url      : "/admin/approval/point/getApprovalPointList"
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
							if (dataItem.apprStat == 'APPR_STAT.REQUEST') {
								if (
									PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
									&& PG_SESSION.loginId == dataItem.approvalRequestUserId
								)
			        				returnValue = '<input type="checkbox" name="itemGridChk" class="itemGridChk" value="' + dataItem.pmPointId + '" />';
							}

		        			return returnValue;
		        		}
		    		}
            		, {title : 'No'   , width : '60px' , attributes : {style : 'text-align:center'}, template:"<span class='row-number'></span>", type:"number", format: "{0:n0}"}
                    , {field : 'pointTypeName' , title : '적립금설정' , width : '120px' , attributes : {style : 'text-align:center'}}
                    , {field : 'pointName' , title : '적립금명' , width : '200px' , attributes : {style : 'text-align:center;text-decoration: underline;color:blue;'}}
                    , {field : 'issueDate' , title : '기간' , width : '200px' , attributes : {style : 'text-align:center'}}
                    , {field : 'organizationName' , title : '분담조직' , width : '100px' , attributes : {style : 'text-align:center'}}
                    , {field : 'sumIssueVal' , title : '적립금' , width : '130px' , attributes : {style : 'text-align:center'},
                       template : function(dataItem){
                    	     let returnValue;
                             if(dataItem.pointType == "POINT_TYPE.REVIEW"){
                            	 returnValue = "일반:" + kendo.format("{0:n0}", dataItem.normalAmount) + "원"+ "<br>" + "포토: " +kendo.format("{0:n0}", dataItem.photoAmount) + "원" + "<br>" + "프리미엄: " + kendo.format("{0:n0}", dataItem.premiumAmount) + "원";
                             }else{
                            	 returnValue = kendo.format("{0:n0}", dataItem.sumIssueVal) + "원";
                             }
                             return returnValue;
				          }
                    }
                    , {field : 'validityDay' , title : '유효기간' , width : '180px' , attributes : {style : 'text-align:center'},
                       template : function(dataItem){
                    	    let returnValue;
                            if(dataItem.pointType == "POINT_TYPE.REVIEW"){
                           	 //returnValue = dataItem.normalValidityDay + "<br>" + dataItem.photoValidityDay + "<br>" + dataItem.premiumValidityDay;
                           	 returnValue = '지급일로부터 ' + dataItem.validityDay + ' 일';
                            }else{
                            	if(dataItem.validityType == 'VALIDITY_TYPE.PERIOD'){
                            		returnValue = dataItem.validityEndDate.substring(0,10) + ' 까지';
                            	}else{
                            		if(dataItem.validityDay == '' || dataItem.validityDay == null || dataItem.validityDay == '-'){
                                		returnValue = '-';
                                	}else{
                                		returnValue = '등록일로부터 ' + dataItem.validityDay + ' 일';
                                	}
                            	}
                            }
                            return returnValue;
				          }
                    }
                    , {field : 'comment' , title : '비고' , width : '300px' , attributes : {style : 'text-align:left'},
                    	template : function(dataItem){
                    	    let returnValue;
                            if(dataItem.pointType == "POINT_TYPE.AUTO"){
                           	 returnValue = dataItem.comment + ": " + kendo.format("{0:n0}", dataItem.issueBudget) + "원";
                            }else{
                            	returnValue = dataItem.comment
                            }
                            return returnValue;
				          }
                    }
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
	                ,{ title:'관리', width: "150px", attributes:{ style:'text-align:left;', class:'forbiz-cell-readonly' }
							,command: [ { text: '요청철회', imageClass: "k-i-add", className: "f-grid-add k-margin5", iconClass: "k-icon",
											 click: function(e) {  e.preventDefault();
														            var tr = $(e.target).closest("tr");
														            var data = this.dataItem(tr);
														            fnCancelRequestOne(data.pmPointId,data.groupPmPointIdList);}
											,visible: function(dataItem) {
												var hasAuth = false;

												if (!fnIsProgramAuth("CANCEL_LIST_REQ"))
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
												fnDisposal(data.pmPointId,data.groupPmPointIdList);
											}
											, visible: function(dataItem) {
												return (dataItem.apprStat =="APPR_STAT.DENIED" || dataItem.apprStat =="APPR_STAT.CANCEL" )
											}
					                    }
							]
	                }
					,{ field:'pmPointId', hidden:'true'}
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
			if(colIdx == 3 || colIdx == 4){
				fnGridClick(e);
			}
		});
	}

    function fnGridClick(e){

    	var dataItem = aGrid.dataItem($(e.target).closest('tr'));

		fnKendoPopup({
			id     : 'pointMgmPut',
			title  : '적립금 설정 수정',
			src    : '#/pointMgmPut',
			param  : dataItem,
			width  : '1000px',
			height : '1100px',
			success: function( id, data ){
				fnSearch();
			}
		});
    }

    function fnCancelRequestOne(pmPointId,groupPmPointIdList){
		if( pmPointId.length == 0 ) {
			fnKendoMessage({message: "선택된 적립금이 없습니다."});
			return;
		}
		let params = {};
		let pmPointList = groupPmPointIdList.split(",");
		params.pmPointIdList = [];
		if(pmPointList.length > 1) {
			for (var i = 0; i < pmPointList.length; i++) {
				params.pmPointIdList[i] = pmPointList[i];
			}
		} else {
			params.pmPointIdList[0] = pmPointId;
		}

		fnCancelRequest(params);
	}

	//선택 요청철회
	function fnSelectCancelRequest(){
		let selectRows  = $("#aGrid").find("input[name=itemGridChk]:checked").closest("tr");
		let params = {};
		params.pmPointIdList = [];
		let groupItemList = [];

		if( selectRows.length == 0 ){
			fnKendoMessage({ message : "선택된 적립금이 없습니다." });
			return;
		}

		for (let i = 0, selectCount = selectRows.length; i < selectCount; i++) {
			let dataItem = aGrid.dataItem($(selectRows[i]));
			if (dataItem.pmPointId == dataItem.groupPmPointIdList) {
				params.pmPointIdList[i] = dataItem.pmPointId;
			} else {
				let pmPointList = dataItem.groupPmPointIdList.split(",");
				for (let j = 0; j < pmPointList.length; j++) {
					groupItemList.push(pmPointList[j]);
				}

				if (groupItemList.length > 0) {
					for (let i = 0; i < groupItemList.length; i++) {
						params.pmPointIdList[selectRows.length + i] = groupItemList[i];
					}
				}
				params.pmPointIdList = params.pmPointIdList.filter(((item, index) => {
					return item !== null && item !== undefined && item !== '' && params.pmPointIdList.indexOf(item) === index;
				}));
			}
		}

		fnCancelRequest(params);
	};

	// 삭제(폐기)
	function fnDisposal(pmPointId, groupPmPointIdList){
		if( pmPointId.length == 0 ){
			fnKendoMessage({ message : "선택된 쿠폰이 없습니다." });
			return;
		}

    	let params = {};
		let pmPointList = groupPmPointIdList.split(",");
    	params.pmPointIdList = [];

		if(pmPointList.length > 1) {
			for (var i = 0; i < pmPointList.length; i++) {
				params.pmPointIdList[i] = pmPointList[i];
			}
		} else {
			params.pmPointIdList[0] = pmPointId;
		}

    	fnKendoMessage({
			type    : "confirm",
			message : "삭제 하시겠습니까?",
			ok      : function(){
		    	fnAjax({
		    		url     : "/admin/approval/point/putDisposalApprovalPoint",
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

		//		var url = '/admin/promotion/point/removePoint'
//		fnAjax({
//			url     : url,
//			params  : {pmPointId : pmPointId},
//			success :
//				function( data ){
//					fnSearch();
//				},
//				isAction : 'delete'
//		});
	}

	//요청철회
	function fnCancelRequest(params){
		if( fnIsEmpty(params) || fnIsEmpty(params.pmPointIdList) || params.pmPointIdList.length < 1){
			fnKendoMessage({ message : "선택된 적립금이 없습니다." });
			return;
		}

    	fnKendoMessage({
			type    : "confirm",
			message : "요청철회 하시겠습니까?",
			ok      : function(){
				fnAjax({
					url     : "/admin/approval/point/putCancelRequestApprovalPoint",
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

    //==================================================================================
    //--------------- Initialize Option Box Start --------------------------------------
    //==================================================================================
    function fnInitOptionBox() {

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

	    $('input:checkbox[name="searchApprovalStatus"]:input[value="APPR_STAT.APPROVED"]').next().text("승인완료(관리자)");

	    //fbCheckboxChange();
	    $('#searchApprovalStatus').bind("change", onCheckboxWithTotalChange);

	    //================ 적립금 설정구분 ===============
    	fnKendoDropDownList({
    			id  : 'searchPointType',
    	        url : "/admin/comn/getCodeList",
    	        params : {"stCommonCodeMasterCode" : "POINT_TYPE", "useYn" :"Y"},
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
    //==================================================================================
	//---------------Initialize Option Box End ------------------------------------------------
    //==================================================================================


    //==================================================================================
    //-------------------------------  Common Function start -------------------------------
    //==================================================================================

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

    //==================================================================================
    //-------------------------------  Common Function end -----------------------------
    //==================================================================================


    //==================================================================================
	//------------------------------- Html 버튼 바인딩  Start -------------------------------
    //==================================================================================
    $scope.fnSearch     = function () { fnSearch(); };
    $scope.fnClear      = function () { fnClear() ; };
	$scope.fnSelectCancelRequest = function( ){  fnSelectCancelRequest();};
	$scope.fnOpenApprDinedResonPopup = function(data){ fnOpenApprDinedResonPopup(data); };
	$scope.fnCloseApprDinedResonPopup = function(){ fnCloseApprDinedResonPopup(); };
    //==================================================================================
	//------------------------------- Html 버튼 바인딩  End ----------------------------
    //==================================================================================

}); // document ready - END
