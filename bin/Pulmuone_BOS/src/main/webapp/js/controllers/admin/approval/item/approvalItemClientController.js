/**-----------------------------------------------------------------------------
 * description : 품목 수정 승인목록 처리
 * @
 * @ 아래 품목 수정 공통 처리
 * @  - 품목 수정 승인요청 내역 : approvalItemClientRequestController.js, approvalItemClientRequest.html
 * @  - 품목 수정 승인관리 : approvalItemClientAcceptController.js  , approvalItemClientAccept.html
 * @
 * @ 수정일			수정자		수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.02.17		박승현		최초생성
 * @
 * **/
'use strict';
var PAGE_SIZE = 20;
var aGridOpt, aGrid, aGridDs;
var viewModel;
var SELECT_TYPE = {'BUTTON':'SELECT_TYPE.BUTTON', 'CHECKBOX':'SELECT_TYPE.CHECKBOX'}

var pageParam = fnGetPageParam(); // GET 방식으로 전달된 parameter 객체
var paramItemCd = pageParam.ilItemCode;

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
	    // 품목 수정 승인 요청 목록, 승인 대상 목록 Set
	    //   - approvalItemClientRequestController.js 에서 설정 됨
		//   - approvalItemClientAcceptController.js 에서 설정 됨
	    // ------------------------------------------------------------------------

	    // ------------------------------------------------------------------------
	    // 상위타이틀
	    // ------------------------------------------------------------------------
	    if(!fnIsEmpty(pageId)) {
	    	if (pageId == 'approvalItemClientRequest') {
	    		$('#pageTitleSpan').text('품목수정 승인요청 내역');
	    	}else if (pageId == 'approvalItemClientAccept') {
	    		$('#pageTitleSpan').text('거래처 품목 승인관리');
	    	}
	    }

    }

	//전체화면 구성
	function fnUI(){

		fnTranslate();			// comm.lang.js 안에 있는 공통함수 다국어 변환--------------------------------------------

		fnInitButton();			//Initialize Button  ---------------------------------

		fnViewModelInit();

		fnInitOptionBox();		// 마스터 품목 검색조건 Initialize

		fnInitGrid();			//Initialize Grid ------------------------------------

		fnDefaultSet();			// 기본설정 -------------------------------------

	    //탭 변경 이벤트
	    fbTabChange();

        if(paramItemCd){ //다른페이지에서 넘어온 상태라면
        	$("input[name=selectConditionType]:eq(0)").prop("checked", true).trigger("change"); // 단일조건 검색 설정
            viewModel.searchInfo.set("ilItemCodeKind", "1"); // ERP품목코드 설정
    		if(paramItemCd != undefined) {
                viewModel.searchInfo.set("ilItemCode", paramItemCd); // 품목코드값 설정
    		}
    		var renewURL = window.location.href.slice(0, window.location.href.indexOf('?')); //URL 파라미터 초기화(삭제)
	        history.pushState(null, null, renewURL);

	        $("#fnSearch01").trigger("click"); // 조회 버튼 클릭

	        paramItemCd = null; //처음만 검색되도록 paramItemCd를 초기화 한다.
    	}
        else {
    		fnSearch();				// 조회 -----------------------------------------
        }

	};

	function fnInitButton() {
		$('#fnSearch, #fnClear, #fnRejectReasonSave, #fnSelectApproval, #fnSelectCancelRequest').kendoButton();
		if(pageId == 'approvalItemClientRequest'){
			$('#fnSelectCancelRequest').show();
			$('#fnProcessApproved').hide();
			$('#fnProcessDenied').hide();
		}else if(pageId == 'approvalItemClientAccept'){
			$('#fnSelectCancelRequest').hide();
			$('#fnProcessApproved').show();
			$('#fnProcessDenied').show();
		}
	};

	function fnClear() {
//		$('#searchForm').formClear(true);
		fnDefaultSet();
	}
	// 기본 설정
	function fnDefaultSet(){
		// 데이터 초기화
        viewModel.searchInfo.set("ilItemCodeKind", "1");
        viewModel.searchInfo.set("ilItemCode", "");
        viewModel.searchInfo.set("itemName", "");
        viewModel.searchInfo.set("masterItemType", []);
        viewModel.searchInfo.set("erpLinkIfYn", "");

        viewModel.searchInfo.set("urSupplierId", "");
        viewModel.searchInfo.set("warehouseGroup", "");
        viewModel.searchInfo.set("warehouseId", "");

        $("input[name=masterItemType]:eq(0)").prop("checked", true).trigger("change");
        $("input[name=erpLinkIfYn]:eq(0)").prop("checked", true).trigger("change");

		$('input[name=searchApprovalStatus]').each(function(idx, element) {
			if(element.value == "ALL" || element.value == "APPR_STAT.APPROVED" || element.value == "APPR_STAT.APPROVED_BY_SYSTEM"){
				$(element).prop('checked', false);
			}
			else{
				$(element).prop('checked', true);
			}
		});
        $('input[name=masterItemType]').each(function(idx, element) {
            $(element).prop('checked', true);
        });

		$("#searchApprReqUserType").data("kendoDropDownList").enable(true);
		$("#searchApprReqUser").prop('disabled', false);
        if(!(PG_SESSION == null || PG_SESSION == '' || PG_SESSION == undefined || PG_SESSION == 'undefined')) {
			if(pageId == 'approvalItemClientRequest') {
				$("#searchApprReqUser").val(PG_SESSION.loginId);
				if (PG_SESSION.companyType != 'COMPANY_TYPE.HEADQUARTERS') { // 거래처의 경우 승인요청자 변경 불가
					$("#searchApprReqUserType").data("kendoDropDownList").enable(false);
					$("#searchApprReqUser").prop('disabled', true);
				}
			}
			if(pageId == 'approvalItemClientAccept')
				$("#searchApprChgUser").val(PG_SESSION.loginId);
		}

		//전체 초기화 버튼 클릭시 필요한 초기화 요소 추가 2021-05-20 임상건 [HGRM-8162] [승인요청내역공통] 초기화 버튼 기능 수정 요청
		$("#searchApprChgUserType").data("kendoDropDownList").select(0);
		$('[data-id="fnDateBtnC"]').mousedown();
	};

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		$('#ilItemApprId').val("");
		$('#statusComment').val("");
		kendoWindow.close();
	}

	function fnViewModelInit() {
		viewModel = new kendo.data.ObservableObject({
            searchInfo : { // 조회조건
            	ilItemCodeKind : "1", 	// 검색조건
            	ilItemCode : "", 		// 검색어 (숫자,영문대소문자)
            	itemName : "", 			// 품목명
            	apprKindType : "APPR_KIND_TP.ITEM_CLIENT", 		// 승인코드

            	masterItemType : [], 		// 마스터품목유형
            	erpLinkIfYn : "", 		// ERP 연동여부

            	searchApprReqUserType : "ID", // 승인요청자 아이디/회원 타입
            	searchApprReqUser : "", // 승인요청자 아이디/회원 조회값
            	approvalReqStartDate : "", // 승인요청일자 검색 시작일
            	approvalReqEndDate : "", // 승인요청일자 검색 종료일

                searchApprChgUserType : "ID", // 승인처리자 아이디/회원 타입
                searchApprChgUser : "", // 승인처리자 아이디/회원 조회값
                approvalChgStartDate : "", // 승인처리일자 검색 시작일
                approvalChgEndDate : "", // 승인처리일자 검색 종료일

                searchApprovalStatus : [], 		// 승인유형
                urSupplierId : "", // 공급업체
                warehouseGroup : "", // 출고처 그룹
                warehouseId : "" // 출고처 그룹 기준 출고처
            },
        });

        viewModel.publicStorageUrl = fnGetPublicStorageUrl();

        kendo.bind($("#searchForm"), viewModel);
	};

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

    	var tooltip = $("#tooltipDiv").kendoTooltip({ // 도움말 toolTip
            filter : "span",
            width : 600,
            position : "center",
            content: kendo.template($("#tooltip-template").html()),
            animation : {
                open : {
                    effects : "zoom",
                    duration : 150
                }
            }
        }).data("kendoTooltip");

        fnTagMkRadio({
	          id: 'selectConditionType',
	          tagId: 'selectConditionType',
	          chkVal: 'multiSection',
	          tab: true,
	          data: [{
	              CODE: "singleSection",
	              NAME: "단일조건 검색",
	              TAB_CONTENT_NAME: "singleSection"
	          }, {
	              CODE: "multiSection",
	              NAME: "복수조건 검색",
	              TAB_CONTENT_NAME: "multiSection"
	          }],
            change: function (e) {
                const mode = e.target.value.trim();
                toggleElement(mode);
            }
	      });

        //상품 코드 검색
        fnKendoDropDownList({
       		id: "ilItemCodeKind",
            data: [
            	{
            		CODE: "1",
            		NAME: "ERP품목코드(마스터품목코드)",
            	},
            	{
            		CODE: "2",
            		NAME: "품목바코드",
            	}
            ],
            valueField: "CODE",
            textField: "NAME",
        });

        // 마스터 품목 유형
		fnTagMkChkBox({
			id			: 'masterItemType',
			url			: "/admin/comn/getCodeList",
			tagId		: 'masterItemType',
			autoBind	: true,
			async		: false,
			valueField	: 'CODE',
			textField 	: 'NAME',
			beforeData	: [{"CODE":"", "NAME":"전체"}],
			chkVal		: '',
			style		: {},
			params		: {"stCommonCodeMasterCode" : "MASTER_ITEM_TYPE", "useYn" :"Y"},
		});

		// ERP 연동여부
		fnTagMkRadio({
			id : 'erpLinkIfYn',
			data : [ {
				"CODE" : "",
				"NAME" : "전체"
			}, {
				"CODE" : "Y",
				"NAME" : "연동 품목"
			}, {
				"CODE" : "N",
				"NAME" : "미연동 품목"
			} ],
			tagId : 'erpLinkIfYn',
			chkVal : ''
		});

        // 공급업체
        fnKendoDropDownList({
            id : "urSupplierId",
//            url : "/admin/comn/getDropDownSupplierList",
            url : "/admin/comn/getDropDownAuthSupplierList",
            tagId : "urSupplierId",
            textField :"supplierName",
            valueField : "supplierId",
            chkVal : "",
            blank : "전체",
            async : false
        })

        // 출고처그룹
        fnKendoDropDownList({
            id : "warehouseGroup",
            tagId : "warehouseGroup",
            url : "/admin/comn/getCodeList",
            params : { "stCommonCodeMasterCode" : "WAREHOUSE_GROUP", "useYn" : "Y" },
            blank : "출고처 그룹 전체",
            async : false
        }).bind("change", fnWareHouseGroupChange);

        // 출고처그룹 별 출고처
        fnKendoDropDownList({
            id : "warehouseId",
            tagId : "warehouseId",
//            url : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
            url : "/admin/comn/getDropDownAuthWarehouseGroupByWarehouseList",
            textField :"warehouseName",
            valueField : "warehouseId",
            blank : "출고처 선택",
            params : { "warehouseGroupCode" : "" },
//            async : false,
//            cscdId     : "warehouseGroup",
//            cscdField  : "warehouseGroupCode"
        });

        $('#masterItemType').bind("change", onCheckboxWithTotalChange);

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
	    $('input:checkbox[name="searchApprovalStatus"]:input[value="APPR_STAT.NONE"]').parent().remove();
	    $('input:checkbox[name="searchApprovalStatus"]:input[value="APPR_STAT.SAVE"]').parent().remove();
	    $('input:checkbox[name="searchApprovalStatus"]:input[value="APPR_STAT.APPROVED_BY_SYSTEM"]').parent().remove();
	    $('input:checkbox[name="searchApprovalStatus"]:input[value="APPR_STAT.DISPOSAL"]').parent().remove();
	    if(pageId == 'approvalItemClientAccept')
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
	};

	//검색
	function fnSearch() {

		if($("input[name=selectConditionType]:checked").val() == "multiSection") {
			if($('#ilItemCode').val() != "") {
				$("#ilItemCode").val("");
			}
		}

		var itemNameLengthChk = false;

		// 기간 조건검색 시 공백 유효성 검사
		if (dateBlankChk() == false)
			return;

		var data = $('#searchForm').formSerialize(true);

		if($("input[name=selectConditionType]:checked").val() == "singleSection") {
			if( $.trim($('#ilItemCode').val()).length >= 0 && $.trim($('#ilItemCode').val()).length < 2 ) {
				itemNameLengthChk = true;
				fnKendoMessage({
					message : '단일조건 코드 검색은 2글자 이상 입력해야 합니다.',
					ok : function() {
						return;
					}
				});
			}else {
				data['ilItemCode'] = $.trim($('#ilItemCode').val());
			}
			if(pageId == 'approvalItemClientRequest')
				data['searchApprovalStatus'] = 'APPR_STAT.REQUEST∀APPR_STAT.CANCEL∀APPR_STAT.DENIED∀APPR_STAT.SUB_APPROVED∀APPR_STAT.APPROVED';
			else if(pageId == 'approvalItemClientAccept')
				data['searchApprovalStatus'] = 'APPR_STAT.REQUEST∀APPR_STAT.DENIED∀APPR_STAT.SUB_APPROVED∀APPR_STAT.APPROVED';
		}else {
			// 마스터 품목명 : trim 으로 공백 제거, 2글자 이상 입력해야 함
			if( $.trim($('#itemName').val()).length == 0 ) {
				data['itemName'] = '';
			} else if( $.trim($('#itemName').val()).length == 1 ) {
				itemNameLengthChk = true;
				fnKendoMessage({
					message : '마스터 품목명 조회시 2글자 이상 입력해야 합니다.',
					ok : function() {
						return;
					}
				});
			}else if( $.trim($('#itemName').val()).length > 30 ) {
				itemNameLengthChk = true;
				fnKendoMessage({
					message : '마스터 품목명 조회시 30글자 이상 입력이 안됩니다. ',
					ok : function() {
						return;
					}
				});
			} else {
				data['itemName'] = $.trim($('#itemName').val());
			}
		}

		if( $("input[name=searchApprovalStatus]:eq(0)").is(":checked") ) {  // 마스터 품목유형 '전체' 체크시 : 해당 파라미터 전송하지 않음
			data['searchApprovalStatus'] = '';
		}
		if( $("input[name=masterItemType]:eq(0)").is(":checked") ) {  // 마스터 품목유형 '전체' 체크시 : 해당 파라미터 전송하지 않음
			data['masterItemType'] = '';
		}

		const _pageSize = aGrid && aGrid.dataSource ? aGrid.dataSource.pageSize() : PAGE_SIZE;

		if(!itemNameLengthChk) {
			var query = {
						page : 1,
						pageSize : _pageSize,
						filterLength : fnSearchData(data).length,
						filter : {
							filters : fnSearchData(data)
						}
			};
			aGridDs.query(query);
		}
	};

	//Grid
	function fnInitGrid() {

		aGridDs = fnGetPagingDataSource({
			url : '/admin/approval/item/getApprovalItemClientList',
			pageSize : PAGE_SIZE,
            requestEnd : function(e){
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
								if (pageId == 'approvalItemClientAccept') {
									if (dataItem.apprStat == 'APPR_STAT.REQUEST') {
										if (!fnIsEmpty(dataItem.approvalSubUserId)) { // 1차 승인자 설정시, 1차 승인자(권한대행 포함)에게만 노출
											if (
												PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
												&& (PG_SESSION.loginId == dataItem.approvalSubUserId || PG_SESSION.loginId == dataItem.approvalSubGrantUserId)
											)
												returnValue = '<input type="checkbox" name="itemGridChk" class="itemGridChk" value="' + dataItem.ilItemApprId + '" />';
										}
										else { // 1차 승인자 미설정시, 2차 승인자(권한대행 포함)에게만 노출
											if (
												PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
												&& (PG_SESSION.loginId == dataItem.approvalUserId || PG_SESSION.loginId == dataItem.approvalGrantUserId)
											)
												returnValue = '<input type="checkbox" name="itemGridChk" class="itemGridChk" value="' + dataItem.ilItemApprId + '" />';
										}
									}
									else if (dataItem.apprStat == 'APPR_STAT.SUB_APPROVED') {
										if (
											PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
											&& (PG_SESSION.loginId == dataItem.approvalUserId || PG_SESSION.loginId == dataItem.approvalGrantUserId)
										)
											returnValue = '<input type="checkbox" name="itemGridChk" class="itemGridChk" value="' + dataItem.ilItemApprId + '" />';
									}
								}
								else if (pageId == 'approvalItemClientRequest') {
									if (dataItem.apprStat == 'APPR_STAT.REQUEST') {
										if (
											PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
											&& PG_SESSION.loginId == dataItem.approvalRequestUserId
										)
											returnValue = '<input type="checkbox" name="itemGridChk" class="itemGridChk" value="' + dataItem.ilItemApprId + '" />';
									}
								}

								return returnValue;
							}
						}
						, {title : 'No'   , width : '50px' , attributes : {style : 'text-align:center'}, template:"<span class='row-number'></span>", type:"number", format: "{0:n0}"}
						, {field : 'ilItemBarcode',  title : '품목코드/<br>품목바코드', width : '140px', attributes : { style : 'text-align:center;color:black;' }
							, template: function(dataItem) {
							    if (dataItem.apprStat == "APPR_STAT.APPROVED" || dataItem.apprStat == "APPR_STAT.CANCEL") {
                                   if(dataItem['ilItemCode'] != "" && dataItem['ilItemBarcode'] != "") {
                                       return dataItem['ilItemCode'] + "/<br>" + dataItem['ilItemBarcode'];
                                   }else if(dataItem['ilItemBarcode'] == "" && dataItem['ilItemCode'] != "") {
                                       return dataItem['ilItemCode'] + "/- ";
                                   }else if(dataItem['ilItemCode'] == "" && dataItem['ilItemBarcode'] != "") {
                                       return  "- /" + dataItem['ilItemBarcode'];
                                   }else if( dataItem['ilItemCode'] == "" && dataItem['ilItemBarcode'] == "" ) {
                                       return "- / -";
                                   }
                                }else{
                                   if(dataItem['ilItemCode'] != "" && dataItem['ilItemBarcode'] != "") {
                                       return "<a class='apprDenied' kind='apprDenied' style='color:blue;' >" + kendo.htmlEncode(dataItem['ilItemCode'] ) + "/<br>"+ kendo.htmlEncode(dataItem['ilItemBarcode'] )+ "</a>"
                                   }else if(dataItem['ilItemBarcode'] == "" && dataItem['ilItemCode'] != "") {
                                       return "<a class='apprDenied' kind='apprDenied' style='color:blue;' >" + kendo.htmlEncode(dataItem['ilItemCode'] ) + "/- "+ "</a>"
                                   }else if(dataItem['ilItemCode'] == "" && dataItem['ilItemBarcode'] != "") {
                                       return "<a class='apprDenied' kind='apprDenied' style='color:blue;' >" + "- /" + kendo.htmlEncode(dataItem['ilItemBarcode'] )+ "</a>"
                                   }else if( dataItem['ilItemCode'] == "" && dataItem['ilItemBarcode'] == "" ) {
                                       return "<a class='apprDenied' kind='apprDenied' style='color:blue;' >" + "- / -" + "</a>"
                                   }
                                }
							}
						}
						, {field : 'itemTypeName', title : '마스터 품목유형', width : '80px', attributes : { style : 'text-align:center' } }
						, {field : 'erpIfYn', title : 'ERP 연동여부', width : '80px', attributes : { style : 'text-align:center' }
							, template: function(dataItem) {
								return dataItem['erpIfYn'] == 'Y' ? '연동' : '미연동';
							}
						}
						, {field : 'itemName', title : '마스터 품목명', width : '250px', attributes : { style : 'text-align:center;color:black;' },
                            template : function(dataItem) {
                                if (dataItem.apprStat == "APPR_STAT.APPROVED" || dataItem.apprStat == "APPR_STAT.CANCEL") {
                                    return dataItem.itemName;
                                }else{
                                    return "<a class='apprDenied' kind='apprDenied' style='color:blue;' >" + kendo.htmlEncode(dataItem.itemName) + "</a>";
                                }

                            }
						 }
						, {field : 'warehouseName', title : '출고지', width : '150px', attributes : { style : 'text-align:center' }
							, template: function(dataItem) {
								return ( dataItem['warehouseName'] ? dataItem['warehouseName'] + ( dataItem['preOrderYn']  == 'Y' ? '<br>선주문가능' : '' ) : '' );
							}
						}
						, {field : 'supplierName', title : '공급업체', width : '100px', attributes : { style : 'text-align:center' } }
						, {field : 'standardPrice', title : '원가', width : '80px', attributes : { style : 'text-align:center' }, format: "{0:n0} 원"}
						, {field : 'recommendedPrice', title : '정상가', width : '80px', attributes : { style : 'text-align:center' }, format: "{0:n0} 원" }
						, {field : 'marginRate', title : '마진율', width : '70px', attributes : { style : 'text-align:center' }, format: "{0:n0} %" }
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
														            fnCancelRequestOne(data.ilItemApprId);}
											,visible: function(dataItem) {
												var hasAuth = false;

												if (pageId != 'approvalItemClientRequest')
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
												fnDisposal(data.ilItemApprId);
											}
											, visible: function(dataItem) {
												return ( pageId == 'approvalItemClientRequest'
															&& (dataItem.apprStat =="APPR_STAT.DENIED" || dataItem.apprStat =="APPR_STAT.CANCEL" )
															&& (PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != null && PG_SESSION.loginId == dataItem.approvalRequestUserId)
														)
											}
					                    }
										, { text: '승인완료'		, imageClass: "k-i-add", className: "f-grid-add k-margin5", iconClass: "k-icon",
											click: function(e) {  e.preventDefault();
																	var tr = $(e.target).closest("tr");
																	var data = this.dataItem(tr);
																	fnConfirmApproval(data.ilItemApprId, data.ilItemCode);
																}
											,visible: function(dataItem) {
												var hasAuth = false;

												if (pageId != 'approvalItemClientAccept') // 승인관리 페이지에서만
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
																	fnDenieApprovalPopup(data.ilItemApprId, data.ilItemCode);
												            	}
											, visible: function(dataItem) {
												var hasAuth = false;

												if (pageId != 'approvalItemClientAccept') // 승인관리 페이지에서만
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
						, {field : 'ilItemApprId' , title : '품목승인 PK' , attributes : {style : 'text-align:center'}, hidden:true}
					]
		};

		aGrid = $('#aGrid').initializeKendoGrid(aGridOpt).cKendoGrid();

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
		//그리드 체크박스 클릭
		aGrid.element.on("click", "[name=itemGridChk]" , function(e){
			if( e.target.checked ){
				if( $("[name=itemGridChk]").length == $("[name=itemGridChk]:checked").length ){
					$("#checkBoxAll").prop("checked", true);
				}
			}else{
				$("#checkBoxAll").prop("checked", false);
			}
		});

		//그리드 td 클릭
		$(aGrid.tbody).on("click", "td", function (e) {
			var row = $(this).closest("tr");
			var rowIdx = $("tr", aGrid.tbody).index(row);
			var colIdx = $("td", row).index(this);

            var apprStat = aGrid.dataItem($(e.currentTarget).closest('tr')).apprStat;
            if(apprStat != 'APPR_STAT.CANCEL' && apprStat != 'APPR_STAT.APPROVED'){
                if(colIdx == 2 || colIdx == 5){
                    fnGridClick(e);
                }
            }
		});
	};

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

    function fnWareHouseGroupChange() {
    	fnAjax({
//            url     : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
            url : "/admin/comn/getDropDownAuthWarehouseGroupByWarehouseList",
            method : "GET",
            params : { "warehouseGroupCode" : $("#warehouseGroup").val() },

            success : function( data ){
                let warehouseId = $("#warehouseId").data("kendoDropDownList");
                warehouseId.setDataSource(data.rows);
            },
            error : function(xhr, status, strError){
                fnKendoMessage({ message : xhr.responseText });
            },
            isAction : "select"
        });
    };

    //품목 상세화면
	function fnGridClick(e){
		e.preventDefault();

		var dataItem = aGrid.dataItem($(e.currentTarget).closest('tr'));
		var option = new Object();

		option.url = "#/itemMgmModify";  // 마스터 품목 수정 화면 URL
		option.target = "_blank";

		option.data = {                  // 마스터 품목 수정 화면으로 전달할 Data
			ilItemCode : dataItem.ilItemCode,
			masterItemType : dataItem.itemType,
			isErpItemLink : dataItem.erpIfYn == 'Y' ? true : false,
			ilItemApprId : dataItem.ilItemApprId,
			apprKindType : viewModel.searchInfo.apprKindType
		};

		fnGoNewPage(option);
	}

	function toggleElement(mode) {
        const $targetEl = $('[data-include="' + mode + '"]');

        $("[data-include]").toggleDisableInTag(true);
        $targetEl.toggleDisableInTag(false);
    }

    function fnGetSelectType(ilItemApprId, ilItemCode) {
		if (ilItemApprId == undefined) {
			viewModel.popupSelectType = SELECT_TYPE.CHECKBOX;
		} else {
			viewModel.ilItemApprId = ilItemApprId;
			viewModel.ilItemCode = ilItemCode;
			viewModel.popupSelectType = SELECT_TYPE.BUTTON;
		}

		return viewModel.popupSelectType;
	}

    //승인 반려사유 팝업
	function fnDenieApprovalPopup(ilItemApprId, ilItemCode){
		fnKendoInputPoup({height:"400px" ,width:"500px", title:{ nullMsg :'승인 반려 사유' } });
		fnGetSelectType(ilItemApprId, ilItemCode);
	}

	//승인처리
	function fnConfirmApproval(ilItemApprId, ilItemCode) {
		let params = {};
		let selectType = fnGetSelectType(ilItemApprId, ilItemCode);

		params.ilItemApprIdList = [];
		params.apprStat = "APPR_STAT.APPROVED";


		if (SELECT_TYPE.BUTTON == selectType) {
			params.ilItemApprIdList[0] = ilItemApprId;
		}

		if (SELECT_TYPE.CHECKBOX == selectType) {
			let selectRows  = $("#aGrid").find("input[name=itemGridChk]:checked").closest("tr");

			for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){
				let dataItem = aGrid.dataItem($(selectRows[i]));
				params.ilItemApprIdList[i] = dataItem.ilItemApprId;
			}
		}

		if( params.ilItemApprIdList.length == 0 ){
			fnKendoMessage({ message : "선택된 품목이 없습니다." });
			return;
		}

		fnApprovalProcess(params);
	}

	//승인반려
	function fnDenieApproval() {
		let params = {};
		let selectType = viewModel.popupSelectType;

		params.ilItemApprIdList = [];
		params.apprStat = "APPR_STAT.DENIED";
		params.statusComment = $('#statusComment').val();

		if( fnIsEmpty(params.statusComment)){
			fnKendoMessage({ message : "승인 반려 사유를 입력 해주세요." });
			return;
		}

		if (SELECT_TYPE.BUTTON == selectType) {
			params.ilItemApprIdList[0] = viewModel.ilItemApprId;
		}

		if (SELECT_TYPE.CHECKBOX == selectType) {
			let selectRows  = $("#aGrid").find("input[name=itemGridChk]:checked").closest("tr");

			for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){
				let dataItem = aGrid.dataItem($(selectRows[i]));
				params.ilItemApprIdList[i] = dataItem.ilItemApprId;
			}
		}

		if( params.ilItemApprIdList.length == 0 ){
			fnKendoMessage({ message : "선택된 품목이 없습니다." });
			return;
		}

		fnApprovalProcess(params);
	}

    // 승인처리
    function fnApprovalProcess(params){
    	if( fnIsEmpty(params) || fnIsEmpty(params.ilItemApprIdList) || params.ilItemApprIdList.length < 1 || fnIsEmpty(params.apprStat)){
    		fnKendoMessage({ message : "선택된 승인건이 없습니다." });
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
		    		url     : "/admin/approval/item/putApprovalProcessItemClient",
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

    function fnCancelRequestOne(ilItemApprId){
		if( ilItemApprId.length == 0 ){
			fnKendoMessage({ message : "선택된 승인건이 없습니다." });
			return;
		}
		let params = {};
		params.ilItemApprIdList = [];
		params.ilItemApprIdList[0] = ilItemApprId;
		fnCancelRequest(params);
	}
    //선택 요청철회
	function fnSelectCancelRequest(){
		let selectRows  = $("#aGrid").find("input[name=itemGridChk]:checked").closest("tr");
		let params = {};
		params.ilItemApprIdList = [];
		if( selectRows.length == 0 ){
			fnKendoMessage({ message : "선택된 승인건이 없습니다." });
			return;
		}
		for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){
			let dataItem = aGrid.dataItem($(selectRows[i]));
			params.ilItemApprIdList[i] = dataItem.ilItemApprId;
		}
		fnCancelRequest(params);
	};

	//요청철회
	function fnCancelRequest(params){
		if( fnIsEmpty(params) || fnIsEmpty(params.ilItemApprIdList) || params.ilItemApprIdList.length < 1){
			fnKendoMessage({ message : "선택된 승인건이 없습니다." });
			return;
		}

    	fnKendoMessage({
			type    : "confirm",
			message : "요청철회 하시겠습니까?",
			ok      : function(){
				fnAjax({
					url     : "/admin/approval/item/putCancelRequestApprovalItemClient",
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

	//폐기
	function fnDisposal(ilItemApprId){
    	if( ilItemApprId.length == 0 ){
    		fnKendoMessage({ message : "선택된 승인건이 없습니다." });
    		return;
    	}
    	let params = {};
    	params.ilItemApprIdList = [];
    	params.ilItemApprIdList[0] = ilItemApprId;

    	fnKendoMessage({
			type    : "confirm",
			message : "삭제 하시겠습니까?",
			ok      : function(){
		    	fnAjax({
		    		url     : "/admin/approval/item/putDisposalApprovalItemClient",
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

	// ------------------------------- Html 버튼 바인딩 Start
	// -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};

	$scope.fnSelectCancelRequest = function( ){  fnSelectCancelRequest();};
	$scope.fnDenieApprovalPopup = function() { fnDenieApprovalPopup() };
	$scope.fnDenieApproval = function() { fnDenieApproval(); };
	$scope.fnConfirmApprovalChkbox = function() { fnConfirmApproval() };
	$scope.fnOpenApprDinedResonPopup = function(data){ fnOpenApprDinedResonPopup(data); };
	$scope.fnCloseApprDinedResonPopup = function(){ fnCloseApprDinedResonPopup(); };

	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END