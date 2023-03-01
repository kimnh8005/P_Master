/**-----------------------------------------------------------------------------
 * description : 상품할인 승인목록 처리
 * @
 * @ 아래 상품할인 공통 처리
 * @  - 상품할인 승인요청 내역 : approvalGoodsDiscountRequestController.js, approvalGoodsDiscountRequest.html
 * @  - 상품할인 승인관리 : approvalGoodsDiscountAcceptController.js  , approvalGoodsDiscountAccept.html
 * @
 * @ 수정일			수정자		수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.02.17		박승현		최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 50;
var viewModel, aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
    fnInitialize();	//Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : true });

        fnPageInfo({
            PG_ID  : 'approvalGoodsDiscountRequest',
            callback : fnUI
        });

        // ------------------------------------------------------------------------
	    // 상위타이틀
	    // ------------------------------------------------------------------------
	    if(!fnIsEmpty(pageId)) {
	    	if (pageId == 'approvalGoodsDiscountRequest') {
	    		$('#pageTitleSpan').text('상품할인 승인요청 내역');
	    	}else if (pageId == 'approvalGoodsDiscountAccept') {
	    		$('#pageTitleSpan').text('상품할인 승인 관리');
	    	}
	    }

        // ------------------------------------------------------------------------
        // 페이지파라미터
        // ------------------------------------------------------------------------

        // ------------------------------------------------------------------------
        // 상품할인 승인 요청 목록, 승인 대상 목록 Set
        //   - approvalGoodsDiscountRequestController.js 에서 설정 됨
        //   - approvalGoodsDiscountAcceptController.js 에서 설정 됨
        // ------------------------------------------------------------------------

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

    };

    // 버튼 초기화
    function fnInitButton(){
        $('#fnSearch, #fnClear, #fnRejectReasonSave, #fnSelectApproval, #fnSelectCancelRequest').kendoButton();
        if(pageId == 'approvalGoodsDiscountRequest'){
            $('#fnSelectCancelRequest').show();
            $('#fnProcessApproved').hide();
            $('#fnProcessDenied').hide();
        }else if(pageId == 'approvalGoodsDiscountAccept'){
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

        if(!(PG_SESSION == null || PG_SESSION == '' || PG_SESSION == undefined || PG_SESSION == 'undefined')) {
            if(pageId == 'approvalGoodsDiscountRequest') {
                $("#searchApprReqUser").val(PG_SESSION.loginId);
                $("#searchApprChgUser").val("");
            }
            if(pageId == 'approvalGoodsDiscountAccept') {
            	$("#searchApprReqUser").val("");
                $("#searchApprChgUser").val(PG_SESSION.loginId);
            }
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
                apprKindType : "APPR_KIND_TP.GOODS_DISCOUNT", 		// 승인코드

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
                    url     : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
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
            url : "/admin/comn/getDropDownSupplierList",
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
            url : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
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
        $('input:checkbox[name="goodsType"]:input[value="GOODS_TYPE.ADDITIONAL"]').parent().remove();

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
        $('input:checkbox[name="searchApprovalStatus"]:input[value="APPR_STAT.DISPOSAL"]').parent().remove();
        $('input:checkbox[name="searchApprovalStatus"]:input[value="APPR_STAT.APPROVED_BY_SYSTEM"]').parent().remove();

        if(pageId == 'approvalGoodsDiscountAccept')
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
            if(pageId == 'approvalGoodsDiscountRequest')
                data['searchApprovalStatus'] = 'APPR_STAT.REQUEST∀APPR_STAT.CANCEL∀APPR_STAT.DENIED∀APPR_STAT.SUB_APPROVED∀APPR_STAT.APPROVED';
            else if(pageId == 'approvalGoodsDiscountAccept')
                data['searchApprovalStatus'] = 'APPR_STAT.REQUEST∀APPR_STAT.DENIED∀APPR_STAT.SUB_APPROVED∀APPR_STAT.APPROVED';

        }

        if(!itemNameLengthChk) {
            const _pageSize = aGrid && aGrid.dataSource ? aGrid.dataSource.pageSize() : PAGE_SIZE;

            let searchData = fnSearchData(data);
            let query = {
            	page : 1,
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
            url      : "/admin/approval/goods/getApprovalGoodsDiscountList",
            pageSize : PAGE_SIZE
        });

        aGridOpt = {
            dataSource: aGridDs,
            pageable  : { pageSizes: [20, 30, 50, 100], buttonCount : 10 },
            navigatable : true,
            scrollable : true,
            columns : [
                { field : 'goodsGridChk', headerTemplate : "<input type='checkbox' id='checkBoxAll' />", width:'50px', attributes : {style : "text-align:center;"}
                    , template : function(dataItem){
                        let returnValue = '';
						if (pageId == 'approvalGoodsDiscountAccept') {
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
						else if (pageId == 'approvalGoodsDiscountRequest') {
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
                , {field : "goodsId", title : "상품코드", width : "70px", attributes : { style : "text-align:center" }}
                , {field : "goodsTypeName", title : "상품유형", width : "70px", attributes : { style : "text-align:center" } }
                , {field : "goodsName", title : "상품명", width : "290px", attributes : { style : "text-align:center" }
                    , template : function(dataItem){
                        let extinction = "";
                        if(dataItem.extinctionYn == "Y" && dataItem.saleStatusCode == "SALE_STATUS.STOP_SALE") {
                            extinction = "(판매불가)";
                        }
                        return extinction + dataItem.goodsName;
                    }
                }
                , {field : "warehouseName", title : "출고처 정보", width: "130px", attributes : {style : "text-align:center;"} }
                , {field : "supplierName", title : "공급업체", width: "150px", attributes : {style : "text-align:center;"} }

                , {field : "discountStartDt", title : "할인시작일자", width : "80px", attributes : { style : "text-align:center" }, format: "{0:n0}"}
                , {field : "discountEndDt", title : "할인종료일자", width : "80px", attributes : { style : "text-align:center" }, format: "{0:n0}"}
                , {field : "standardPrice", title : "원가", width : "80px", attributes : { style : "text-align:center" }
                    , template : function(dataItem) {
                        if (dataItem.standardPrice == dataItem.standardPriceChg) {
                            return "<div class='inlineBlock textCenter' style='margin-bottom:3px;'>" + kendo.format("{0:n0}", dataItem.standardPrice) + "</div>";
                        } else {
                            return "<div class='inlineBlock textCenter' style='margin-bottom:3px;'>" + kendo.format("{0:n0}", dataItem.standardPrice) + "</div>"
                                + "<BR>" + "<div class='inlineBlock textCenter' style='color:red;margin-bottom:3px;'>" + kendo.format("{0:n0}", dataItem.standardPriceChg) + "</div>";
                        }
                    }
                }
                , {field : "recommendedPrice", title : "정상가", width : "80px", attributes : { style : "text-align:center" }
                    , template : function(dataItem) {
                        if (dataItem.recommendedPrice == dataItem.recommendedPriceChg) {
                            return "<div class='inlineBlock textCenter' style='margin-bottom:3px;'>" + kendo.format("{0:n0}", dataItem.recommendedPrice) + "</div>";
                        } else {
                            return "<div class='inlineBlock textCenter' style='margin-bottom:3px;'>" + kendo.format("{0:n0}", dataItem.recommendedPrice) + "</div>"
                                + "<BR>" + "<div class='inlineBlock textCenter' style='color:red;margin-bottom:3px;'>" + kendo.format("{0:n0}", dataItem.recommendedPriceChg) + "</div>";
                        }
                    }
                }
                , {field : "discountTp", title : "할인구분", width : "80px", attributes : { style : "text-align:center" }
                    , template : function(dataItem) {
                        return dataItem.discountTypeName;
                    }
                }
                , {field : "discountMethodTp", title : "할인유형", width : "80px", attributes : { style : "text-align:center" }
                    , template : function(dataItem) {
                        return dataItem.discountMethodType;
                    }
                }
                , {field : "discountRatio", title : "할인율", width : "80px", attributes : { style : "text-align:center" }
                    , template : function(dataItem) {
                        if (dataItem.discountRatio == dataItem.discountRatioChg) {
                            return "<div class='inlineBlock textCenter' style='margin-bottom:3px;'>" + kendo.format("{0:n0}", dataItem.discountRatio) + "%</div>";
                        } else {
                            return "<div class='inlineBlock textCenter' style='margin-bottom:3px;'>" + kendo.format("{0:n0}", dataItem.discountRatio) + "%</div>"
                                + "<BR>" + "<div class='inlineBlock textCenter' style='color:red;margin-bottom:3px;'>" + kendo.format("{0:n0}", dataItem.discountRatioChg) + "%</div>";
                        }
                    }
                }
                , {field : "salePrice", title : "판매가", width : "80px", attributes : { style : "text-align:center" }
                    , template : function(dataItem) {
                        if (dataItem.discountSalePrice == dataItem.discountSalePriceChg) {
                            return "<div class='inlineBlock textCenter' style='margin-bottom:3px;'>" + kendo.format("{0:n0}", dataItem.discountSalePrice) + "</div>";
                        } else {
                            return "<div class='inlineBlock textCenter' style='margin-bottom:3px;'>" + kendo.format("{0:n0}", dataItem.discountSalePrice) + "</div>"
                                + "<BR>" + "<div class='inlineBlock textCenter' style='color:red;margin-bottom:3px;'>" + kendo.format("{0:n0}", dataItem.discountSalePriceChg) + "</div>";
                        }
                    }
                }
                , {field : "marginRate", title : "마진율", width : "80px", attributes : { style : "text-align:center" }
                    , template : function(dataItem) {
                        if (dataItem.marginRate == dataItem.marginRateChg) {
                            return "<div class='inlineBlock textCenter' style='margin-bottom:3px;'>" + kendo.format("{0:n0}", dataItem.marginRate) + "%</div>";
                        } else {
                            return "<div class='inlineBlock textCenter' style='margin-bottom:3px;'>" + kendo.format("{0:n0}", dataItem.marginRate) + "%</div>"
                                + "<BR>" + "<div class='inlineBlock textCenter' style='color:red;margin-bottom:3px;'>" + kendo.format("{0:n0}", dataItem.marginRateChg) + "%</div>";
                        }
                    }
                }


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
                    ,command: [
                    	{ text: '요청철회', imageClass: "k-i-add", className: "f-grid-add k-margin5", iconClass: "k-icon",
	                        click: function(e) {  e.preventDefault();
	                            var tr = $(e.target).closest("tr");
	                            var data = this.dataItem(tr);
	                            fnCancelRequestOne(data.ilGoodsApprId);}
	                        ,visible: function(dataItem) {
								var hasAuth = false;

								if (pageId != 'approvalGoodsDiscountRequest')
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
								return ( pageId == 'approvalGoodsDiscountRequest'
											&& (dataItem.apprStat =="APPR_STAT.DENIED" || dataItem.apprStat =="APPR_STAT.CANCEL" )
											&& (PG_SESSION != undefined && PG_SESSION != null && PG_SESSION.loginId != null && PG_SESSION.loginId == dataItem.approvalRequestUserId)
										)
							}
	                    }
	                    , { text: '승인완료'		, imageClass: "k-i-add", className: "f-grid-add k-margin5", iconClass: "k-icon",
	                        click: function(e) {  e.preventDefault();
	                            var tr = $(e.target).closest("tr");
	                            var data = this.dataItem(tr);
	                            fnApprovalProcessOne(data.ilGoodsApprId, data.discountType, "APPR_STAT.APPROVED");}
	                        ,visible: function(dataItem) {
								var hasAuth = false;

								if (pageId != 'approvalGoodsDiscountAccept') // 승인관리 페이지에서만
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
	                            fnRejectApproval(data.ilGoodsApprId);
	                        }
	                        , visible: function(dataItem) {
								var hasAuth = false;

								if (pageId != 'approvalGoodsDiscountAccept') // 승인관리 페이지에서만
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
    };

    function fnRejectApproval(ilGoodsApprId){
        fnKendoInputPoup({height:"400px" ,width:"500px", title:{ nullMsg :'승인 반려 사유' } });
        $('#ilGoodsApprId').val(ilGoodsApprId);
    }
    function fnRejectReasonSave(){
        let params = {};
        params.ilGoodsApprIdList = [];
        params.ilGoodsApprIdList[0] = $('#ilGoodsApprId').val();
        params.apprStat = "APPR_STAT.DENIED";
        params.statusComment = $('#statusComment').val();
        if( fnIsEmpty(params.statusComment)){
            fnKendoMessage({ message : "승인 반려 사유를 입력 해주세요." });
            return;
        }

        fnApprovalProcess(params);
    }
    function fnApprovalProcessOne(ilGoodsApprId, discountType, apprStatus){
        if( ilGoodsApprId.length == 0 ){
            fnKendoMessage({ message : "선택된 승인건이 없습니다." });
            return;
        }
        let params = {};
        params.ilGoodsApprIdList = [];
        params.ilGoodsApprIdList[0] = ilGoodsApprId;
        params.apprStat = apprStatus;

        fnApprovalProcess(params);
    }
    // 선택처리(승인, 반려)
    function fnSelectApproval(apprStatus){
        let selectRows  = $("#aGrid").find("input[name=goodsGridChk]:checked").closest("tr");
        let params = {};
        params.ilGoodsApprIdList = [];
        params.apprStat = apprStatus;
        if( selectRows.length == 0 ){
            fnKendoMessage({ message : "선택된 승인건이 없습니다." });
            return;
        }
        for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){
            let dataItem = aGrid.dataItem($(selectRows[i]));
            params.ilGoodsApprIdList[i] = dataItem.ilGoodsApprId;
        }
        fnApprovalProcess(params);
    };

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
		            url     : "/admin/approval/goods/putApprovalProcessGoodsDiscount",
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
		            url     : "/admin/approval/goods/putCancelRequestApprovalGoodsDiscount",
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
		    		url     : "/admin/approval/goods/putDisposalApprovalGoodsDiscount",
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

    $scope.fnSelectApproval = function(apprStatus){  fnSelectApproval(apprStatus);};
    $scope.fnRejectReasonSave = function( ){  fnRejectReasonSave();};
    $scope.fnSelectCancelRequest = function( ){  fnSelectCancelRequest();};
	$scope.fnOpenApprDinedResonPopup = function(data){ fnOpenApprDinedResonPopup(data); };
	$scope.fnCloseApprDinedResonPopup = function(){ fnCloseApprDinedResonPopup(); };

    // ------------------------------- Html 버튼 바인딩 End

}); // document ready - END
