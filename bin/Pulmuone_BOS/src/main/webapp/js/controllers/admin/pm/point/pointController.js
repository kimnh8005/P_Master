﻿'use strict';

var pageParam = fnGetPageParam();

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var rejectPmPointId;
var rejectPointType;

$(document).ready(function() {

    fnInitialize();

    function fnInitialize(){
        $scope.$emit('fnIsMenu', { flag : 'true' });

        fnPageInfo({
              PG_ID    : 'point'
            , callback : fnUI
        });
    }

	function fnUI(){
		fnTranslate();	// comm.lang.js 안에 있는 공통함수 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		setTiekctDiv();

		fnSearch();

	}


	//--------------------------------- Button Start---------------------------------
	function fnInitButton() {
		$('#fnSearch, #fnSave, #fnSelectCancel, #fnRejectReasonSave, #fnExcel, #fnNew').kendoButton();
	}

    //--- 검색  -----------------
    function fnSearch() {
        $('#inputForm').formClear(false);
        var data = $('#searchForm').formSerialize(true);

        var query = {
                       page         : 1
                     , pageSize     : PAGE_SIZE
                     , filterLength : fnSearchData(data).length
                     , filter :  {
                           filters : fnSearchData(data)
                       }
        };
        aGridDs.query( query );
    }

    function setTiekctDiv(){
		$('#searchIssueTicketTypeName').hide();
		$('#searchIssueTicketTypeDiv').hide();
	}

    //-- 초기화 버튼 -----------------
	function fnClear() {
		$('#searchForm').formClear(true);

		$(".date-controller button").each(function() {
			$(this).attr("fb-btn-active", false);
		})
	}

	function fnSelectCancel() {
		var selectRows 	= aGrid.tbody.find('input[name=itemGridChk]:checked').closest('tr');
		var cancelArray = new Array();

		if(selectRows.length == 0) {
			fnKendoMessage({message:'이용권을 선택해주세요.'});
			return false;
		}

		for(var i =0; i< selectRows.length;i++){
			var dataRow = aGrid.dataItem($(selectRows[i]));
			cancelArray.push(dataRow.pmSerialNumberId);
		}
		fnKendoMessage({message:'선택하신 이용권을 중지 처리 하시겠습니까?', type : "confirm" ,ok : function(){ fnSelectCancelApply(cancelArray) }  });
	}

	function fnSelectCancelApply(cancelArray){
		var url  = '/admin/promotion/serialNumber/putSerialNumberCancel';
		var cbId = 'delete';

		fnAjax({
			url     : url,
			params  : {inputPmSerialNumberIdList :cancelArray},
			success :
				function( data ){
					fnBizCallback("update", data);
				},
			isAction : 'update'
		});
	}

	function fnNew(){

		fnKendoPopup({
			id     : 'pointMgmAdd',
			title  : '적립금 설정 등록',
			src    : '#/pointMgmAdd',
			param  : { },
			width  : '1100px',
			height : '1100px',
			success: function( id, data ){
				if(data == 'SAVE'){
					fnSearch();
				}
			}
		});
	}

	//--------------------------------- Button End---------------------------------


	//--------------------------------------------------------------------------
	//------------------------------- Grid Start -------------------------------
	//--------------------------------------------------------------------------
    function fnInitGrid() {

        aGridDs = fnGetPagingDataSource({
             url      : "/admin/promotion/point/getPointSettingList"
             , pageSize : PAGE_SIZE
             , requestEnd: function(e) {
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
                      {field : 'no'        , title : 'No'   , width : '60px' , attributes : {style : 'text-align:center'}, template:"<span class='row-number'></span>", type:"number", format: "{0:n0}"}
                    , {field : 'pmPointId' , title : '적립금번호' , width : '120px' , attributes : {style : 'text-align:center'}}
                    , {field : 'pointTypeName' , title : '적립금설정' , width : '120px' , attributes : {style : 'text-align:center'}}
                    , {field : 'pointName' , title : '적립금명' , width : '200px' , attributes : {style : 'text-align:center;text-decoration: underline;color:blue;'}}
                    , {field : 'issueDate' , title : '기간' , width : '200px' , attributes : {style : 'text-align:center'}}
                    , {field : 'organizationName' , title : '분담조직' , width : '100px' , attributes : {style : 'text-align:center'}}
                    , {field : 'sumIssueVal' , title : '적립금' , width : '130px' , attributes : {style : 'text-align:center'},
                       template : function(dataItem){
                    	     let returnValue;
                             if(dataItem.pointType == "POINT_TYPE.FEEDBACK"){
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
                            if(dataItem.pointType == "POINT_TYPE.FEEDBACK"){
                           	 //returnValue = dataItem.normalValidityDay + "<br>" + dataItem.photoValidityDay + "<br>" + dataItem.premiumValidityDay;
                           	 returnValue = '지급일로부터 ' + dataItem.validityDay + ' 일';
                            }else{
                            	if(dataItem.validityType == 'VALIDITY_TYPE.PERIOD'){
                            		if(dataItem.validityEndDate != null){
                            			returnValue = dataItem.validityEndDate.substring(0,10) + ' 까지';
                            		}
                            	}else{
                            		if(dataItem.validityDay == '' || dataItem.validityDay == null || dataItem.validityDay == '-'){
                                		returnValue = '-';
                                	}else{
                                		if(dataItem.validityDay == 0){
                                			returnValue = '지급 당일';
                                		}else{
                                			returnValue = '등록일로부터 ' + dataItem.validityDay + ' 일';
                                		}
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
                           	 //returnValue = dataItem.comment + ": " + kendo.format("{0:n0}", dataItem.issueBudget) + "원";
                           	 returnValue = dataItem.comment + ": " + "제한없음";
                            }else{
                            	if(dataItem.comment == null){
                            		returnValue = '';
                            	}else{
                            		returnValue = dataItem.comment
                            	}
                            }
                            return returnValue;
				          }
                    }
                    , {field : 'statusName' , title : '설정상태' , width : '150px' , attributes : {style : 'text-align:center'},
                    	template : function(dataItem){
                    	    let returnValue;
                    	    if(dataItem.ticketCollectYn == 'N' && dataItem.apprStat == 'APPR_STAT.APPROVED' && dataItem.pointType == 'POINT_TYPE.SERIAL_NUMBER'){
                    	    	returnValue = dataItem.statusName + '(미수금)';
                    	    }else if(dataItem.ticketCollectYn == 'Y' && dataItem.apprStat == 'APPR_STAT.APPROVED' && dataItem.pointType == 'POINT_TYPE.SERIAL_NUMBER'){
                    	    	returnValue = dataItem.statusName + '(수금 완료)';
                    	    }else{
                    	    	returnValue = dataItem.statusName;
                    	    }
                            return returnValue;
                    	}
                    }
                    , {field : "button"    , title : "설정상태 관리"       , width : "200px"  , attributes : { style : "text-align:center" }
	                    ,command: [
	    					{ text: '승인요청' , className: "btn-point btn-s"
	    						, click: function(e) {
	    						e.preventDefault();
	    						var tr = $(e.target).closest("tr");
	    						var data = this.dataItem(tr);
	    						fnApprAdmin(data.pmPointId, data.pointType, data.grPmPointId, data.payMethodType);
	    						}
	    						,visible: function(dataItem) { return (dataItem.apprStat =="APPR_STAT.NONE" || dataItem.apprStat =="APPR_STAT.CANCEL" || dataItem.apprStat =="APPR_STAT.DENIED") }}
	    					,{ text: '요청철회'		, className: "btn-gray btn-s"
	    						, click: function(e) {  e.preventDefault();
	    						var tr = $(e.target).closest("tr");
	    						var data = this.dataItem(tr);
	    						fnCancelRequest(data.pmPointId,data.groupPmPointIdList);}
	    						, visible: function(dataItem) { return dataItem.apprStat =="APPR_STAT.REQUEST"}}
	    					,{ text: '지급' , imageClass: "k-i-search", className: "f-grid-search k-margin5", iconClass: "k-icon"
	    						, click: function(e) {  e.preventDefault();
	    						var tr = $(e.target).closest("tr");
	    						var data = this.dataItem(tr);
	    						fnIssueReserve(data.pmPointId, data.pointType, data.grPmPointId, data.payMethodType);}
	    						, visible: function(dataItem) { return dataItem.pointMasterStat =="POINT_MASTER_STAT.STOP" }}
	    					,{ text: '지급중지' , imageClass: "k-i-search", className: "f-grid-search k-margin5", iconClass: "k-icon"
	    						, click: function(e) {  e.preventDefault();
	    						var tr = $(e.target).closest("tr");
	    						var data = this.dataItem(tr);
	    						fnIssueReserveStop(data.pmPointId, data.pointType, data.grPmPointId, data.payMethodType);}
	    						, visible: function(dataItem) { return dataItem.apprStat =="APPR_STAT.APPROVED" &&  dataItem.pointMasterStat !="POINT_MASTER_STAT.STOP" && dataItem.statusName != '지급기간만료'  }}
	    					, { text: '이용권 수금' , imageClass: "k-i-search", className: "f-grid-search k-margin5", iconClass: "k-icon"
                                  , click   : function(e) {
                                                e.preventDefault();
                                                var tr = $(e.target).closest("tr");
                                                var data = this.dataItem(tr);
                                                fnTicketCollect(data.pmPointId);
                                              }
                                  , visible : function(dataItem) {
                                                return dataItem.apprStat =="APPR_STAT.APPROVED" && dataItem.ticketCollectYn == 'N' && dataItem.pointType == 'POINT_TYPE.SERIAL_NUMBER' && fnIsProgramAuth("COLLECT_TICKET");
                                              }
                                  }
	    				] }
	                    ,{ title:'관리', width: "200px", attributes:{ style:'text-align:left;', class:'forbiz-cell-readonly' }
							,command: [ { text: '복사',  className: "btn-gray btn-s",
										   click: function(e) {  e.preventDefault();
														            var tr = $(e.target).closest("tr");
														            var data = this.dataItem(tr);
														            fnPointCopy(data.pmPointId);}
										}
										,{ text: '삭제' , className: "btn-red btn-s",
											click: function(e) {  e.preventDefault();
														            var tr = $(e.target).closest("tr");
														            var data = this.dataItem(tr);
														            fnRemovePoint(data.grPmPointId);}
											, visible:  function(dataItem) { return (dataItem.apprStat=='APPR_STAT.NONE' || dataItem.apprStat=='APPR_STAT.DENIED' || dataItem.apprStat=='APPR_STAT.CANCEL') && fnIsProgramAuth("DELETE")} }
										,{ text: '이용권내역' , className: "btn-lightgray btn-s",
											click: function(e) {  e.preventDefault();
														            var tr = $(e.target).closest("tr");
														            var data = this.dataItem(tr);
														            fnSerialNumber(data.pmPointId, "SERIAL_NUMBER_USE_TYPE.POINT", data.pointName, data.serialNumberTp);}
											, visible: function(dataItem) { return dataItem.pointType =="POINT_TYPE.SERIAL_NUMBER" && dataItem.apprStat=='APPR_STAT.APPROVED'} }
										,{ text: '이용권내역다운로드' , className: "btn-lightgray btn-s",
											click: function(e) {  e.preventDefault();
														            var tr = $(e.target).closest("tr");
														            var data = this.dataItem(tr);
														            fnTicketDownloadPopup(data.pmPointId, "SERIAL_NUMBER_USE_TYPE.POINT", data.pointName);}
											, visible: function(dataItem) { return dataItem.pointType =="POINT_TYPE.SERIAL_NUMBER" && dataItem.apprStat=='APPR_STAT.APPROVED' && dataItem.fixSerialNumber == null && fnIsProgramAuth("EXCELDOWN")} }
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

        $(aGrid.tbody).on("click", "td", function (e) {
			var row = $(this).closest("tr");
			var rowIdx = $("tr", aGrid.tbody).index(row);
			var colIdx = $("td", row).index(this);
			if(colIdx==3){
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
				if(data == 'SAVE'){
					fnSearch();
				}
			}
		});
    }

    //==================================================================================
    //--------------- Initialize Option Box Start --------------------------------------
    //==================================================================================
    function fnInitOptionBox() {

    	$('#kendoPopup').kendoWindow({
			visible: false
			, modal: true
		});

    	//================ 적립금 설정구분 ===============
    	fnKendoDropDownList({
    			id  : 'searchPointType',
    	        url : "/admin/comn/getCodeList",
    	        params : {"stCommonCodeMasterCode" : "POINT_TYPE", "useYn" :"Y"},
    	        textField :"NAME",
    			valueField : "CODE",
    			blank : "전체"
    	});

    	$('#searchPointType').unbind('change').on('change', function(){

			var pointTypeList =$('#searchPointType').data('kendoDropDownList');
			if(pointTypeList._old == 'POINT_TYPE.SERIAL_NUMBER'){
				$('#searchIssueTicketTypeName').show();
				$('#searchIssueTicketTypeDiv').show();
			}else{
				$('#searchIssueTicketTypeName').hide();
				$('#searchIssueTicketTypeDiv').hide();
				$('#searchIssueTicketType').val('');
			}

		});

    	//================ 적립금 설정상태 ===============
//    	fnKendoDropDownList({
//    			id  : 'searchPointStatus',
//    	        url : "/admin/comn/getCodeList",
//    	        params : {"stCommonCodeMasterCode" : "APPR_STAT", "useYn" :"Y"},
//    	        textField :"NAME",
//    			valueField : "CODE",
//    			blank : "전체"
//    	});

    	fnKendoDropDownList({
			id  : 'searchPointStatus',
	        url : "/admin/comn/getPointSearchStatus",
	        params : {"useYn" :"Y"},
	        textField :"NAME",
			valueField : "CODE",
			blank : "전체"
    	});

		fnKendoDropDownList({
			id    : 'searchPointDivision',
			data  : [
				{"CODE":"POINT_ID","NAME":"적립금번호"}
				,{"CODE":"POINT_NM","NAME":"적립금명"}
			],
			chkVal: 'POINT_NM'
		});

		//================데이트 피커===============
        fnKendoDatePicker({
			id    : 'startCreateDate',
			format: 'yyyy-MM-dd'
		});
		fnKendoDatePicker({
			id    : 'endCreateDate',
			format: 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startCreateDate',
			btnEndId : 'endCreateDate'
		});


		// 이용권 수금 상태
		fnTagMkRadio({
			id: "searchIssueTicketType" ,
			tagId : 'searchIssueTicketType',
			url : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "TICKET_COLLECT_TYPE", "useYn" :"Y"},
			async : false,
			beforeData : [
							{"CODE":"", "NAME":"전체"},
						],
			chkVal: '',
			style : {}
		});



	}
    //==================================================================================
	//---------------Initialize Option Box End ------------------------------------------------
    //==================================================================================



    //==================================================================================
    //-------------------------------  Common Function start -------------------------------
    //==================================================================================
    function inputFocus() {
        $('#brandName').focus();
    };

	//승인요청
	function fnApprovalRequest(pmPointId, pointType, apprSubUserId, apprUserId, payMethodType, grPmPointId){

		var url = '/admin/promotion/point/putPointStatus'
		var status = 'APPR_STAT.REQUEST';

		fnAjax({
			url     : url,
			params  : {pmPointId : pmPointId, apprStat : status, pointMasterStat : 'POINT_MASTER_STAT.SAVE' ,pointType:pointType, apprSubUserId : apprSubUserId, apprUserId : apprUserId, payMethodType : payMethodType, grPmPointId : grPmPointId},
			success :
				function( data ){
					fnSearch();
				},
				isAction : 'batch'
		});

	}

	// 승인
	function fnReserveApproval(pmPointId, pointType){

		var url = '/admin/promotion/point/putPointStatus'
		var status = 'APPR_STAT.APPROVED';

		fnAjax({
			url     : url,
			params  : {pmPointId : pmPointId, apprStat : status, pointType:pointType},
			success :
				function( data ){
					fnSearch();
				},
				isAction : 'batch'
		});

	}

	// 요청철회
	function fnCancelRequest(pmPointId,groupPmPointIdList){

		let params = {};
		params.pmPointIdList = [];
		let pmPointList = groupPmPointIdList.split(",");
		// 엑셀 대량 지급
		if(pmPointList.length > 1) {
			for(var i = 0; i < pmPointList.length; i++) {
				params.pmPointIdList[i] = pmPointList[i];
			}
		} else {
			params.pmPointIdList[0] = pmPointId;
		}


		var url = "/admin/approval/point/putCancelRequestApprovalPoint";

		fnAjax({
			url     : url,
			params  : params,
			contentType : "application/json",
			success :
				function( data ){
					fnSearch();
				},
				isAction : 'batch'
		});

	}

	// 승인반려
	function fnRejectApproval(pmPointId, pointType){
		fnKendoInputPoup({height:"400px" ,width:"500px", title:{ nullMsg :'승인 반려 사유' } });
		rejectPmPointId = pmPointId;
		rejectPointType = pointType;
	}

	function fnRejectReasonSave(){
		var url = '/admin/promotion/point/putPointStatus'
		var status = 'APPR_STAT.DENIED';
		var statusComment = $('#statusComment').val();

		fnAjax({
			url     : url,
			params  : {pmPointId : rejectPmPointId, pointType:rejectPointType , apprStat : status, statusComment:statusComment},
			success :
				function( data ){
				fnClose();
				fnSearch();
			},
			isAction : 'batch'
		});

	}

	// 지급
	function fnIssueReserve(pmPointId, pointType, grPmPointId, payMethodType){
		var url = '/admin/promotion/point/putPointStatus'
		var status = 'APPR_STAT.APPROVED';
		var masterStatus = 'POINT_MASTER_STAT.APPROVED';
		var payMethodTp;
		// 엑셀대량지급
		if(payMethodType == "POINT_PAY_METHOD_TP.EXCEL_LARGE_PAY") {
			payMethodTp = payMethodType;
		}

		fnAjax({
			url     : url,
			params  : {pmPointId : pmPointId, apprStat : status, pointMasterStat : masterStatus, pointType:pointType,  payMethodType : payMethodTp, grPmPointId : grPmPointId},
			success :
				function( data ){
					fnSearch();
				},
				isAction : 'batch'
		});

	}

	// 지급중지
	function fnIssueReserveStop(pmPointId, pointType, grPmPointId, payMethodType){
		var url = '/admin/promotion/point/putPointStatus'
		var status = 'APPR_STAT.APPROVED';
		var masterStatus = 'POINT_MASTER_STAT.STOP';
		var payMethodType;
		// 엑셀대량지급
		if(payMethodType == "POINT_PAY_METHOD_TP.EXCEL_LARGE_PAY") {
			payMethodTp = payMethodType;
		}

		fnAjax({
			url     : url,
			params  : {pmPointId : pmPointId, apprStat : status, pointMasterStat : masterStatus, pointType:pointType, payMethodType : payMethodTp, grPmPointId : grPmPointId},
			success :
				function( data ){
					fnSearch();
				},
				isAction : 'batch'
		});

	}

	//적립금 설정 복사
	function fnPointCopy(pmPointId){
		var pointCopyYn = 'Y';
		var dataItem = aGrid.dataItem($(event.target).closest('tr'));
		var paramData;

		// 기존 일반
		if(dataItem.payMethodType != "POINT_PAY_METHOD_TP.EXCEL_LARGE_PAY") {
            paramData = {pmPointId : pmPointId, pointCopyYn : pointCopyYn};
		} else {
            // 엑셀 대량 지급 건
            dataItem['pointCopyYn'] = pointCopyYn;
            paramData = dataItem;
		}

		fnKendoPopup({
			id     : 'pointMgmPut',
			title  : '적립금 등록/수정',
			src    : '#/pointMgmPut',
			param  : paramData,
			width  : '1100px',
			height : '1100px',
			success: function( id, data ){
				if(data == 'SAVE'){
					fnSearch();
				}
			}
		});

	}


    // 이용권내역 호출 팝업
    function fnSerialNumber(pmPointId, useType, pointName, serialNumberType){

//		var map = aGrid.dataItem(aGrid.select());
//		var sData = $('#searchForm').formSerialize(true);
//		var option = new Object();
//
//		option.url = "#/serialNumberList";
//		option.menuId = 807;
//		option.data = {
//				parentId : pmPointId
//
//				, useType : "SERIAL_NUMBER_USE_TYPE.POINT"
//				, displayName : pointName
//				};
//
//		$scope.goPage(option);
//
		fnKendoPopup({
			id     : 'serialNumberList',
			title  : '이용권',
			src    : '#/serialNumberList',
			param  : {parentId : pmPointId		, useType : "SERIAL_NUMBER_USE_TYPE.POINT"		, displayName : pointName, serialNumberTp : serialNumberType},
			width  : '1100px',
			height : '1100px',
			success: function( id, data ){
				if(data == 'SAVE'){
					fnSearch();
				}
			}
		});

    }

    // 이용권 내역 다운로드
    function fnTicketDownloadPopup(pmPointId, useType, pointName){
		$('#displayName').val(pointName);
		$('#parentId').val(pmPointId);
		$('#useType').val('SERIAL_NUMBER_USE_TYPE.POINT');
		var data = $('#searchForm').formSerialize(true);
		fnExcelDownload('/admin/promotion/serialNumber/ticketNumberExportExcel', data);

	}

    // 삭제 확인 Confimrm
	function fnRemovePoint(pmPointId){
		fnKendoMessage({message:'삭제 하시겠습니까?', type : "confirm" ,ok : function(){ fnRemovePointConfirm(pmPointId) } });
	}

    // 적립금 설정 정보 삭제
    function fnRemovePointConfirm(pmPointId){
    	var url = '/admin/promotion/point/removePoint'
			fnAjax({
				url     : url,
				params  : {pmPointId : pmPointId},
				success :
					function( data ){
						fnBizCallback("delete", data);
					},
					isAction : 'delete'
			});

    }

    function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

    //-------------------------------  콜백합수 -----------------------------
    function fnBizCallback (id, data) {

        switch (id) {
            case 'select':
            	/* popup 오픈 시 데이터 초기화 */
                break;
			case 'insert':
			case 'update':
				fnKendoMessage({
                    message:"저장되었습니다."
                    , ok : function() {
                                  fnSearch();
                                  fnClose();
                           }
				});
			    break;

            case 'delete':
                fnKendoMessage({  message : '삭제되었습니다.'
                	, ok      : function(){
                		fnSearch();
                	}
                });
                break;
        }
    }

    //-- Alert 메세지
	function fnAlertMessage(msg, id) {
		fnKendoMessage(
			{  message : msg
				, ok      : function focusValue() { $("#" + id).focus(); }
			}
		);
        return false;
    }

	// 승인요청
	function fnApprAdmin(pmPointId, pointType, grPmPointId, payMethodType){
		var param  = {'taskCode' : 'APPR_KIND_TP.POINT' };
		fnKendoPopup({
			id     : 'approvalManagerSearchPopup',
			title  : '승인관리자 선택',
			src    : '#/approvalManagerSearchPopup',
			param  : param,
			width  : '1600px',
			height : '800px',
			scrollable : "yes",
			success: function( stMenuId, data ){

				if (data.isCompleteProcess != true) {
					return;
				}

				var apprSubUserId;
				var apprUserId;
				var payMethodTp;

				if(data && !fnIsEmpty(data) && data.authManager2nd){
					var authManager1 = data.authManager1st;
					apprSubUserId = authManager1.apprUserId;
					var authManager2 = data.authManager2nd;
					apprUserId = authManager2.apprUserId;
				}
				var payMethodTp;

				// 엑셀 대량
				if(payMethodType == "POINT_PAY_METHOD_TP.EXCEL_LARGE_PAY") {
					payMethodTp = "POINT_PAY_METHOD_TP.EXCEL_LARGE_PAY";
				}

				fnApprovalRequest(pmPointId, pointType, apprSubUserId, apprUserId, payMethodTp, grPmPointId);
			}
		});
	}


	// 이용권 수금 완료 여부 확인
	function fnTicketCollect(pmPointId){
		fnKendoMessage({message:'수금완료로 변경하시겠습니까?', type : "confirm" ,ok : function(){ fnTicketCollectConfirm(pmPointId) } });

	}

	// 이용권 수금 완료 변경 처리
	function fnTicketCollectConfirm(pmPointId){
		var url = '/admin/pm/point/putTicketCollectStatus'
		var ticketCollectYn = 'Y';
		fnAjax({
			url     : url,
			params  : {pmPointId : pmPointId, ticketCollectYn : ticketCollectYn},
			success :
				function( data ){
					fnSearch();
				},
				isAction : 'batch'
		});
	}

    //==================================================================================
    //-------------------------------  Common Function end -----------------------------
    //==================================================================================


    //==================================================================================
	//------------------------------- Html 버튼 바인딩  Start -------------------------------
    //==================================================================================
    $scope.fnSearch     = function () { fnSearch(); };
    $scope.fnClear      = function () { fnClear() ; };
    $scope.fnNew        = function () { fnNew()   ; };
    $scope.fnSave       = function () { fnSave()  ; };
    $scope.fnClose      = function () { fnClose() ; };
    $scope.fnRejectReasonSave      = function () { fnRejectReasonSave() ; };

    $scope.fnSelectCancel       = function () { fnSelectCancel()  ; };
    $scope.fnExcel      = function () { fnExcel() ; };
    //==================================================================================
	//------------------------------- Html 버튼 바인딩  End ----------------------------
    //==================================================================================

}); // document ready - END
