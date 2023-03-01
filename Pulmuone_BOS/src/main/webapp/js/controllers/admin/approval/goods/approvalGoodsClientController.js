/**-----------------------------------------------------------------------------
 * description : 상품 수정 승인목록 처리
 * @
 * @ 아래 상품 수정 공통 처리
 * @  - 상품수정 승인요청 내역 : approvalGoodsClientRequestController.js, approvalGoodsClientRequest.html
 * @  - 상품수정 승인관리 : approvalGoodsClientAcceptController.js  , approvalGoodsClientAccept.html
 * @
 * @ 수정일			수정자		수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.04.09		최윤석		최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 50;
var pageParam = fnGetPageParam();
var viewModel, aGridDs, aGridOpt, aGrid;
var SELECT_TYPE = {'BUTTON':'SELECT_TYPE.BUTTON', 'CHECKBOX':'SELECT_TYPE.CHECKBOX'}

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : true });

		fnPageInfo({
			PG_ID  : pageId,
			callback : fnUI
		});

		// ------------------------------------------------------------------------
	    // 페이지파라미터
	    // ------------------------------------------------------------------------

	    // ------------------------------------------------------------------------
	    // 상품 수정 승인 요청 목록, 승인 대상 목록 Set
	    //   - approvalGoodsClientRequestController.js 에서 설정 됨
		//   - approvalGoodsClientAcceptController.js 에서 설정 됨
	    // ------------------------------------------------------------------------

	    // ------------------------------------------------------------------------
	    // 상위타이틀
	    // ------------------------------------------------------------------------
	    if(!fnIsEmpty(pageId)) {
	    	if (pageId == 'approvalGoodsClientRequest') {
	    		$('#pageTitleSpan').text('상품수정 승인요청 내역');
	    	}else if (pageId == 'approvalGoodsClientAccept') {
	    		$('#pageTitleSpan').text('거래처 상품 승인관리');
	    	}
	    }
	};

	// 화면 UI 초기화
	function fnUI(){

		fnTranslate();			// comm.lang.js 안에 있는 공통함수 다국어 변환--------------------------------------------

		fnInitButton();			//Initialize Button  ---------------------------------

		fnViewModelInit();

		fnInitOptionBox();		// 마스터 품목 검색조건 Initialize

		fnInitGrid();			//Initialize Grid ------------------------------------

		fnDefaultSet();			// 기본설정 -------------------------------------

		fnSearch();

	    //탭 변경 이벤트
	    fbTabChange();

	    fnInitPage();

	};

	// goNewPage 왔을 경우 조회 함수
	function fnInitPage() {
		if (pageParam != null) {
			if (pageParam.ilGoodsId != null) {
				$("input[name=selectConditionType]:eq(0)").prop("checked", true).trigger("change"); // 단일조건 검색 설정
//	            viewModel.searchInfo.set("searchCondition", "GOODS_CODE"); // 상품코드 설정
				$("#searchCondition").data("kendoDropDownList").select(0); // 상품코드(GOODS_CODE) 설정
	            viewModel.searchInfo.set("findKeyword", pageParam.ilGoodsId); // 상품코드값 설정
	    		var renewURL = window.location.href.slice(0, window.location.href.indexOf('?')); //URL 파라미터 초기화(삭제)
		        history.pushState(null, null, renewURL);

		        $("#fnSearch01").trigger("click"); // 조회 버튼 클릭

		        pageParam.ilGoodsId = null; //처음만 검색되도록pageParam.ilGoodsId를 초기화 한다.
			}
			else if (pageParam.ilItemCode != null) {
				$("input[name=selectConditionType]:eq(0)").prop("checked", true).trigger("change"); // 단일조건 검색 설정
//	            viewModel.searchInfo.set("searchCondition", "ITEM_CODE"); // 품목코드 설정
				$("#searchCondition").data("kendoDropDownList").select(1); // 품목코드(ITEM_CODE) 설정
	            viewModel.searchInfo.set("findKeyword", pageParam.ilItemCode); // 품목코드값 설정
	    		var renewURL = window.location.href.slice(0, window.location.href.indexOf('?')); //URL 파라미터 초기화(삭제)
		        history.pushState(null, null, renewURL);

		        $("#fnSearch01").trigger("click"); // 조회 버튼 클릭

		        pageParam.ilItemCode = null; //처음만 검색되도록 pageParam.ilItemCode를 초기화 한다.
			}
		}
	}

	// 버튼 초기화
	function fnInitButton(){
		$('#fnSearch, #fnClear, #fnRejectReasonSave, #fnSelectApproval, #fnSelectCancelRequest').kendoButton();
		if(pageId == 'approvalGoodsClientRequest'){
			$('#fnSelectCancelRequest').show();
			$('#fnProcessApproved').hide();
			$('#fnProcessDenied').hide();
		}else if(pageId == 'approvalGoodsClientAccept'){
			$('#fnSelectCancelRequest').hide();
			$('#fnProcessApproved').show();
			$('#fnProcessDenied').show();
		}
	};

	function fnClear() {
//		$('#searchForm').formClear(true);
		fnDefaultSet();
	}

	// 기본값 설정
    function fnDefaultSet(){

		// 데이터 초기화
		viewModel.searchInfo.set("searchCondition", "ALL");
		viewModel.searchInfo.set("findKeyword", "");
		viewModel.searchInfo.set("goodsName", "");
		viewModel.searchInfo.set("supplierId", "");
		viewModel.searchInfo.set("warehouseGroup", "");
		viewModel.searchInfo.set("warehouseId", "");

		viewModel.searchInfo.set("goodsType", []);

		// 화면제어
		viewModel.fnWareHouseGroupChange();

        $('input[name=searchApprovalStatus]').each(function(idx, element) {
        	if(element.value == "ALL" || element.value == "APPR_STAT.APPROVED" || element.value == "APPR_STAT.APPROVED_BY_SYSTEM"){
        		$(element).prop('checked', false);
        	}
        	else{
        		$(element).prop('checked', true);
        	}
		});

		$('input[name=goodsType]').each(function(idx, element) {
            $(element).prop('checked', true);
        });

		$("#searchApprReqUserType").data("kendoDropDownList").enable(true);
		$("#searchApprReqUser").prop('disabled', false);
        if(!(PG_SESSION == null || PG_SESSION == '' || PG_SESSION == undefined || PG_SESSION == 'undefined')) {
			if(pageId == 'approvalGoodsClientRequest') {
				$("#searchApprReqUser").val(PG_SESSION.loginId);
				if (PG_SESSION.companyType != 'COMPANY_TYPE.HEADQUARTERS') { // 거래처의 경우 승인요청자 변경 불가
					$("#searchApprReqUserType").data("kendoDropDownList").enable(false);
					$("#searchApprReqUser").prop('disabled', true);
				}
			}
			if(pageId == 'approvalGoodsClientAccept')
				$("#searchApprChgUser").val(PG_SESSION.loginId);
		}

		//전체 초기화 버튼 클릭시 필요한 초기화 요소 추가 2021-05-20 임상건 [HGRM-8162] [승인요청내역공통] 초기화 버튼 기능 수정 요청
		$("#searchApprChgUserType").data("kendoDropDownList").select(0);
		$('[data-id="fnDateBtnC"]').mousedown();
    };

    function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		$('#ilGoodsApprId').val("");
		$('#statusComment').val("");
		kendoWindow.close();
	}

 // viewModel 초기화
    function fnViewModelInit(){
        viewModel = new kendo.data.ObservableObject({
            searchInfo : { // 조회조건
                searchCondition : "", // 검색조건
                findKeyword : "", // 검색어 (숫자,영문대소문자)
                goodsName : "", // 상품명 (한글,영문대소문자, 숫자)
            	apprKindType : "APPR_KIND_TP.GOODS_CLIENT", 		// 승인코드

                goodsType : [], // 상품유형 목록

            	searchApprReqUserType : "ID", // 승인요청자 아이디/회원 타입
            	searchApprReqUser : "", // 승인요청자 아이디/회원 조회값
            	approvalReqStartDate : "", // 승인요청일자 검색 시작일
            	approvalReqEndDate : "", // 승인요청일자 검색 종료일

                searchApprChgUserType : "ID", // 승인처리자 아이디/회원 타입
                searchApprChgUser : "", // 승인처리자 아이디/회원 조회값
                approvalChgStartDate : "", // 승인처리일자 검색 시작일
                approvalChgEndDate : "", // 승인처리일자 검색 종료일

                searchApprovalStatus : [], 		// 승인유형

                supplierId : "", // 공급업체
                warehouseGroup : "", // 출고처 그룹
                warehouseId : "" // 출고처 그룹 기준 출고처
            },
            publicStorageUrl : null, // 저장소 URL
            findKeywordDisabled : false, // 검색어 Disabled
            supplierStandardVisible : true, // 공급처기준 Visible
            warehouseStandardVisible : false, // 출고처기준 Visible

            fnWareHouseGroupChange : function() {
            	fnAjax({
//                    url     : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
                    url : "/admin/comn/getDropDownAuthWarehouseGroupByWarehouseList",
                    method : "GET",
                    params : { "warehouseGroupCode" : viewModel.searchInfo.get("warehouseGroup") },

                    success : function( data ){
                        let warehouseId = $("#warehouseId").data("kendoDropDownList");
                        warehouseId.setDataSource(data.rows);
                    },
                    error : function(xhr, status, strError){
                        fnKendoMessage({ message : xhr.responseText });
                    },
                    isAction : "select"
                });
            }
        });

        viewModel.publicStorageUrl = fnGetPublicStorageUrl();

        kendo.bind($("#searchForm"), viewModel);
    };

	//==================================================================================
    //--------------- Initialize Option Box Start --------------------------------------
    //==================================================================================
    function fnInitOptionBox(){

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
           }
		});

        //상품 코드 검색
        fnKendoDropDownList({
       		id: "searchCondition",
            data: [
                { "CODE" : "GOODS_CODE", "NAME" : "상품코드" }
                , { "CODE" : "ITEM_CODE", "NAME" : "품목코드" }
                , { "CODE" : "ITEM_BARCODE", "NAME" : "품목 바코드" }
            ],
            valueField: "CODE",
            textField: "NAME",
        });

        // 공급업체
        fnKendoDropDownList({
            id : "supplierId",
            tagId : "supplierId",
//            url : "/admin/comn/getDropDownSupplierList",
            url : "/admin/comn/getDropDownAuthSupplierList",
            textField :"supplierName",
            valueField : "supplierId",
            blank : "공급업체 전체",
            async : false
        });

        // 출고처그룹
        fnKendoDropDownList({
            id : "warehouseGroup",
            tagId : "warehouseGroup",
            url : "/admin/comn/getCodeList",
            params : { "stCommonCodeMasterCode" : "WAREHOUSE_GROUP", "useYn" : "Y" },
            blank : "출고처 그룹 전체",
            async : false
        });

        // 출고처그룹 별 출고처
        fnKendoDropDownList({
            id : "warehouseId",
            tagId : "warehouseId",
//            url : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
            url : "/admin/comn/getDropDownAuthWarehouseGroupByWarehouseList",
            textField :"warehouseName",
            valueField : "warehouseId",
            blank : "출고처 선택",
            async : false,
            params : { "warehouseGroupCode" : "" },
//            autoBind : true,
//            cscdId     : "warehouseGroup",
//            cscdField  : "warehouseGroupCode"
        });

        // 상품유형
        fnTagMkChkBox({
            id : "goodsType",
            tagId : "goodsType",
            url : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "GOODS_TYPE", "useYn" : "Y"},
            beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }],
            async : false
        });

        $('#goodsType').bind("change", onCheckboxWithTotalChange);

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

	    if(pageId == 'approvalGoodsClientAccept')
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

//		if(pageParam != null && pageParam.ilGoodsId != null) {
//			viewModel.searchInfo.set("findKeyword", pageParam.ilGoodsId);
//		}

		if($("input[name=selectConditionType]:checked").val() == "multiSection") {
        	viewModel.searchInfo.set("findKeyword", "");
		}

        let data = $("#searchForm").formSerialize(true);

        var itemNameLengthChk = false;

        if($("input[name=selectConditionType]:checked").val() == "singleSection") {
           	if($.trim($('#findKeyword').val()).length >= 0 && $.trim($('#findKeyword').val()).length < 2 ) {
        		itemNameLengthChk = true;
        			fnKendoMessage({
        				message : '단일조건 코드 검색은 2글자 이상 입력해야 합니다.',
        				ok : function() {
        					return;
        				}
        			});
           	} else {
                      data['findKeyword'] = $.trim($('#findKeyword').val());
           	}
           	if(pageId == 'approvalGoodsClientRequest')
				data['searchApprovalStatus'] = 'APPR_STAT.REQUEST∀APPR_STAT.CANCEL∀APPR_STAT.DENIED∀APPR_STAT.SUB_APPROVED∀APPR_STAT.APPROVED';
			else if(pageId == 'approvalGoodsClientAccept')
				data['searchApprovalStatus'] = 'APPR_STAT.REQUEST∀APPR_STAT.DENIED∀APPR_STAT.SUB_APPROVED∀APPR_STAT.APPROVED';
        }

        if( $("input[name=searchApprovalStatus]:eq(0)").is(":checked") ) {  // '전체' 체크시 : 해당 파라미터 전송하지 않음
			data['searchApprovalStatus'] = '';
		}
		if( $("input[name=goodsType]:eq(0)").is(":checked") ) {  // '전체' 체크시 : 해당 파라미터 전송하지 않음
			data['goodsType'] = '';
		}

        if(!itemNameLengthChk) {
        	const _pageSize = aGrid && aGrid.dataSource ? aGrid.dataSource.pageSize() : PAGE_SIZE;

        	let searchData = fnSearchData(data);
            let query = { page : 1,
                          pageSize : _pageSize,
                          filterLength : searchData.length,
                          filter : { filters : searchData }
            };
            aGridDs.query(query);
        }
	};

	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid(){

		aGridDs = fnGetPagingDataSource({
			url      : "/admin/approval/goods/getApprovalGoodsClientList",
			pageSize : PAGE_SIZE
		});

		aGridOpt = {
			dataSource: aGridDs,
			pageable  : { pageSizes: [20, 30, 50, 100], buttonCount : 10 },
			navigatable : true,
			scrollable : true,
			columns : [
						{ field : 'chk', headerTemplate : "<input type='checkbox' id='checkBoxAll' />", width:'50px', attributes : {style : "text-align:center;"}
							, template : function(dataItem){
								let returnValue = '';
								if (pageId == 'approvalGoodsClientAccept') {
									if (dataItem.apprStat == 'APPR_STAT.REQUEST') {
										if (!fnIsEmpty(dataItem.approvalSubUserId)) { // 1차 승인자 설정시, 1차 승인자(권한대행 포함)에게만 노출
											if (
												PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
												&& (PG_SESSION.loginId == dataItem.approvalSubUserId || PG_SESSION.loginId == dataItem.approvalSubGrantUserId)
											)
												returnValue = '<input type="checkbox" name="goodsGridChk" class="goodsGridChk" value="' + dataItem.ilGoodsApprId + '" />';
										}
										else { // 1차 승인자 미설정시, 2차 승인자(권한대행 포함)에게만 노출
											if (
												PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
												&& (PG_SESSION.loginId == dataItem.approvalUserId || PG_SESSION.loginId == dataItem.approvalGrantUserId)
											)
												returnValue = '<input type="checkbox" name="goodsGridChk" class="goodsGridChk" value="' + dataItem.ilGoodsApprId + '" />';
										}
									}
									else if (dataItem.apprStat == 'APPR_STAT.SUB_APPROVED') {
										if (
											PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
											&& (PG_SESSION.loginId == dataItem.approvalUserId || PG_SESSION.loginId == dataItem.approvalGrantUserId)
										)
											returnValue = '<input type="checkbox" name="goodsGridChk" class="goodsGridChk" value="' + dataItem.ilGoodsApprId + '" />';
									}
								}
								else if (pageId == 'approvalGoodsClientRequest') {
									if (dataItem.apprStat == 'APPR_STAT.REQUEST') {
										if (
											PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != undefined && PG_SESSION.loginId != null
											&& PG_SESSION.loginId == dataItem.approvalRequestUserId
										)
											returnValue = '<input type="checkbox" name="goodsGridChk" class="goodsGridChk" value="' + dataItem.ilGoodsApprId + '" />';
									}
								}

								return returnValue;
							}
						}
						, {title : 'No'   , width : '50px' , attributes : {style : 'text-align:center'}, template:"<span class='row-number'></span>", type:"number", format: "{0:n0}"}
						, {field : "itemCode", title : "품목코드/<BR>품목바코드", width : "140px", attributes : {style : "text-align:center;"}
							, template : function(dataItem){
								let itemCodeView = dataItem.itemCode;
								if(dataItem.itemBarcode != "" ){
									itemCodeView += "/<br>" + dataItem.itemBarcode;
								}else{
									itemCodeView += "/ - ";
								}
								return itemCodeView;
							}
						}
						, {field : "goodsId", title : "상품코드", width : "70px", attributes : { style : "text-align:center;color:black;" },
						    template : function(dataItem) {
                                if (dataItem.apprStat == "APPR_STAT.APPROVED" || dataItem.apprStat == "APPR_STAT.CANCEL") {
                                    return dataItem.goodsId;
                                }else{
                                    return "<a class='apprDenied' kind='apprDenied' style='color:blue;' >" + kendo.htmlEncode(dataItem.goodsId) + "</a>";
                                }
                            }
						}
						, {field : "goodsTypeName", title : "상품유형", width : "70px", attributes : { style : "text-align:center" } }
						, {field : "goodsName", title : "상품명", width : "290px", attributes : { style : "text-align:center;color:black;" }
							, template : function(dataItem){
								let imageUrl = dataItem.goodsImagePath ? viewModel.publicStorageUrl + dataItem.goodsImagePath : '/contents/images/noimg.png';
								let extinction = "";
								if(dataItem.extinctionYn == "Y" && dataItem.saleStatusCode == "SALE_STATUS.STOP_SALE") {
									extinction = "(판매불가)";
								}
								if (dataItem.apprStat == "APPR_STAT.APPROVED" || dataItem.apprStat == "APPR_STAT.CANCEL") {
								    return "<img src='" + imageUrl + "' width='50' height='50' align='left' />" + "<BR>" + extinction + dataItem.goodsName;
                                }else{
                                    return "<img src='" + imageUrl + "' width='50' height='50' align='left' />" + "<BR>" + extinction + "<a class='apprDenied' kind='apprDenied' style='color:blue;' >" + kendo.htmlEncode(dataItem.goodsName) + "</a>";
                                }
							}
						}
						, {field : "warehouseName", title : "출고처 정보", width: "130px", attributes : {style : "text-align:center;"} }
						, {field : "supplierName", title : "공급업체", width: "150px", attributes : {style : "text-align:center;"} }
						, {field : "standardPrice", title : "원가", width : "80px", attributes : { style : "text-align:center" }, format: "{0:n0}"}
						, {field : "recommendedPrice", title : "정상가", width : "80px", attributes : { style : "text-align:center" }, format: "{0:n0}"}
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
														            fnCancelRequestOne(data.ilGoodsApprId);}
											,visible: function(dataItem) {
												var hasAuth = false;

												if (pageId != 'approvalGoodsClientRequest')
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
												fnDisposal(data.ilGoodsApprId);
											}
											, visible: function(dataItem) {
												return ( pageId == 'approvalGoodsClientRequest'
															&& (dataItem.apprStat =="APPR_STAT.DENIED" || dataItem.apprStat =="APPR_STAT.CANCEL" )
															&& (PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != null && PG_SESSION.loginId == dataItem.approvalRequestUserId)
														)
											}
					                    }
										, { text: '승인완료'		, imageClass: "k-i-add", className: "f-grid-add k-margin5", iconClass: "k-icon",
											click: function(e) {  e.preventDefault();
																	var tr = $(e.target).closest("tr");
																	var data = this.dataItem(tr);
																	fnConfirmApproval(data.ilGoodsApprId);}
											,visible: function(dataItem) {
												var hasAuth = false;

												if (pageId != 'approvalGoodsClientAccept') // 승인관리 페이지에서만
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
																	fnDenieApprovalPopup(data.ilGoodsApprId);
												            	}
											, visible: function(dataItem) {
												var hasAuth = false;

												if (pageId != 'approvalGoodsClientAccept') // 승인관리 페이지에서만
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
						, {field : 'ilGoodsApprId' , title : '상픔승인 PK' , attributes : {style : 'text-align:center'}, hidden:true}
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
				$("input[name=goodsGridChk]").prop("checked", true);
			}else{
				$("input[name=goodsGridChk]").prop("checked", false);
			}
		});

		//그리드 체크박스 클릭
		aGrid.element.on("click", "[name=goodsGridChk]" , function(e){
			if( e.target.checked ){
				if( $("[name=goodsGridChk]").length == $("[name=goodsGridChk]:checked").length ){
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
                if(colIdx == 3 || colIdx == 5){
                    fnGridClick(e);
                }
            }

		});
	};

	//상품 상세화면
	function fnGridClick(e){
		e.preventDefault();

		let dataItem = aGrid.dataItem($(e.currentTarget).closest('tr'));
		let option = new Object();

		option.data = {                  // 상품 수정 화면으로 전달할 Data
			ilGoodsId : dataItem.goodsId,
			ilGoodsApprId : dataItem.ilGoodsApprId,
			apprKindType : viewModel.searchInfo.apprKindType
		};

		switch(dataItem.goodsTypeCode){
			case "GOODS_TYPE.ADDITIONAL" : // 추가
				option.url = "#/goodsAdditional";
				break;
			case "GOODS_TYPE.DAILY" : // 일일
				option.url = "#/goodsDaily";
				break;
			case "GOODS_TYPE.DISPOSAL" : // 폐기임박
				option.url = "#/goodsDisposal";
				break;
			case "GOODS_TYPE.GIFT" : // 증정
				option.url = "#/goodsAdditional";
				break;
			case "GOODS_TYPE.GIFT_FOOD_MARKETING" : // 식품마케팅증정
				option.url = "#/goodsAdditional";
				break;
			case "GOODS_TYPE.INCORPOREITY" : // 무형
	            option.url = "#/goodsIncorporeal";
				break;
			case "GOODS_TYPE.NORMAL" : // 일반
				option.url = "#/goodsMgm";
				break;
			case "GOODS_TYPE.PACKAGE" : // 묶음
				option.url = "#/goodsPackage";
				break;
			case "GOODS_TYPE.RENTAL" : // 렌탈
				option.url = "#/goodsRental";
				break;
			case "GOODS_TYPE.SHOP_ONLY" : // 매장전용
				option.url = "#/goodsShopOnly";
				break;
		};

		option.target = '_blank';
		fnGoNewPage(option);
	}

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

    function fnGetSelectType(ilGoodsApprId) {
		if (ilGoodsApprId == undefined) {
			viewModel.popupSelectType = SELECT_TYPE.CHECKBOX;
		} else {
			viewModel.ilGoodsApprId = ilGoodsApprId;
			viewModel.popupSelectType = SELECT_TYPE.BUTTON;
		}

		return viewModel.popupSelectType;
	}

    //승인 반려사유 팝업
	function fnDenieApprovalPopup(ilGoodsApprId){
		fnKendoInputPoup({height:"400px" ,width:"500px", title:{ nullMsg :'승인 반려 사유' } });
		fnGetSelectType(ilGoodsApprId);
	}

	//승인처리
	function fnConfirmApproval(ilGoodsApprId) {
		let params = {};
		let selectType = fnGetSelectType(ilGoodsApprId);

		params.ilGoodsApprIdList = [];
		params.apprStat = "APPR_STAT.APPROVED";


		if (SELECT_TYPE.BUTTON == selectType) {
			params.ilGoodsApprIdList[0] = ilGoodsApprId;
		}

		if (SELECT_TYPE.CHECKBOX == selectType) {
			let selectRows  = $("#aGrid").find("input[name=goodsGridChk]:checked").closest("tr");

			for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){
				let dataItem = aGrid.dataItem($(selectRows[i]));
				params.ilGoodsApprIdList[i] = dataItem.ilGoodsApprId;
			}
		}

		if( params.ilGoodsApprIdList.length == 0 ){
			fnKendoMessage({ message : "선택된 품목이 없습니다." });
			return;
		}

		fnApprovalProcess(params);
	}

	//승인반려
	function fnDenieApproval() {
		let params = {};
		let selectType = viewModel.popupSelectType;

		params.ilGoodsApprIdList = [];
		params.apprStat = "APPR_STAT.DENIED";
		params.statusComment = $('#statusComment').val();

		if( fnIsEmpty(params.statusComment)){
			fnKendoMessage({ message : "승인 반려 사유를 입력 해주세요." });
			return;
		}

		if (SELECT_TYPE.BUTTON == selectType) {
			params.ilGoodsApprIdList[0] = viewModel.ilGoodsApprId;
		}

		if (SELECT_TYPE.CHECKBOX == selectType) {
			let selectRows  = $("#aGrid").find("input[name=goodsGridChk]:checked").closest("tr");

			for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){
				let dataItem = aGrid.dataItem($(selectRows[i]));
				params.ilGoodsApprIdList[i] = dataItem.ilGoodsApprId;
			}
		}

		if( params.ilGoodsApprIdList.length == 0 ){
			fnKendoMessage({ message : "선택된 품목이 없습니다." });
			return;
		}

		fnApprovalProcess(params);
	}

    // 승인처리
    function fnApprovalProcess(params){
    	if( fnIsEmpty(params) || fnIsEmpty(params.ilGoodsApprIdList) || params.ilGoodsApprIdList.length < 1 || fnIsEmpty(params.apprStat)){
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
		    		url     : "/admin/approval/goods/putApprovalProcessGoodsClient",
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

    function fnCancelRequestOne(ilGoodsApprId){
		if( ilGoodsApprId.length == 0 ){
			fnKendoMessage({ message : "선택된 승인건이 없습니다." });
			return;
		}
		let params = {};
		params.ilGoodsApprIdList = [];
		params.ilGoodsApprIdList[0] = ilGoodsApprId;
		fnCancelRequest(params);
	}

    //선택 요청철회
	function fnSelectCancelRequest(){
		let selectRows  = $("#aGrid").find("input[name=goodsGridChk]:checked").closest("tr");
		let params = {};
		params.ilGoodsApprIdList = [];
		if( selectRows.length == 0 ){
			fnKendoMessage({ message : "선택된 승인건이 없습니다." });
			return;
		}
		for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){
			let dataItem = aGrid.dataItem($(selectRows[i]));
			params.ilGoodsApprIdList[i] = dataItem.ilGoodsApprId;
		}
		fnCancelRequest(params);
	};

	//요청철회
	function fnCancelRequest(params){
		if( fnIsEmpty(params) || fnIsEmpty(params.ilGoodsApprIdList) || params.ilGoodsApprIdList.length < 1){
			fnKendoMessage({ message : "선택된 승인건이 없습니다." });
			return;
		}

    	fnKendoMessage({
			type    : "confirm",
			message : "요청철회 하시겠습니까?",
			ok      : function(){
				fnAjax({
					url     : "/admin/approval/goods/putCancelRequestApprovalGoodsClient",
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
	function fnDisposal(ilGoodsApprId){
    	if( ilGoodsApprId.length == 0 ){
    		fnKendoMessage({ message : "선택된 승인건이 없습니다." });
    		return;
    	}
    	let params = {};
    	params.ilGoodsApprIdList = [];
    	params.ilGoodsApprIdList[0] = ilGoodsApprId;

    	fnKendoMessage({
			type    : "confirm",
			message : "삭제 하시겠습니까?",
			ok      : function(){
		    	fnAjax({
		    		url     : "/admin/approval/goods/putDisposalApprovalGoodsClient",
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

	//-------------------------------  Common Function start -------------------------------
	//-------------------------------  Common Function end -------------------------------
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

}); // document ready - END
