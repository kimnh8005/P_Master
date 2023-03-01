/*******************************************************************************************************************************************************************************************************
 * -------------------------------------------------------- description : 마스터 품목 관리 - 마스터 품목 리스트 * @ @ 수정일 수정자 수정내용 @ ------------------------------------------------------ * @ 2017.02.13 신혁 최초생성 @
 ******************************************************************************************************************************************************************************************************/
'use strict';
var pageParam = fnGetPageParam(); // GET 방식으로 전달된 parameter 객체

var PAGE_SIZE = 20;
var itemGridOpt, itemGrid, itemGridDs;
var viewModel;

$(document).ready(function() {

    // Initialize Page Call
	fnInitialize();

	// Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', {
			flag : 'true'
		});

		fnPageInfo({
			PG_ID : 'item',
			callback : fnUI
		});
	};

	//전체화면 구성
	function fnUI() {
		fnTranslate();			// comm.lang.js 안에 있는 공통함수 다국어
		fnInitButton();			// Initialize Button
		fnInitItemCtgry();		// 마스터 품목 검색조건 Initialize
		fnViewModelInit();
		fnItemGrid();			// 품목 Grid

//        fnToggleSearchOption();   // 상세검색 옵션 toggle : 최초 조회시 상세검색 닫힘

        $('tr[name=detailSearchRow]').hide();

	    // 마스터 품목 등록 or 수정 화면에서 품목 등록 or 수정 후 품목코드 전달시 : 해당 품목코드로 바로 조회
	    if( pageParam && pageParam['ilItemCode'] ) {

	        // 브라우저의 url 에서 GET 방식으로 전달된 ? 이하 주소는 삭제
	        var renewURL = window.location.href.slice(0, window.location.href.indexOf('?'));
	        history.pushState(null, null, renewURL);

	        // 전달 받은 품목 코드로 바로 조회
	        $('#ilItemCode').val(pageParam['ilItemCode']);
	        fnSearch();
	    }

	    //탭 변경 이벤트
	    fbTabChange();

	};

	function fnInitButton() {
		$('#fnSearch , #fnClear , #EXCEL_DN , #fnGoItemMgmPage').kendoButton();
	};

	function fnViewModelInit() {
		viewModel = new kendo.data.ObservableObject({
            searchInfo : { // 조회조건
            	ilItemCodeKind : "1", 	// 검색조건
            	ilItemCode : "", 		// 검색어 (숫자,영문대소문자)
            	itemName : "", 			// 품목명
            	masterItemType : [], 		// 마스터품목유형
            	erpLinkIfYn : "", 		// ERP 연동여부
            	categoryStandardDepth1 : "", // 표준카테고리 대분류
                categoryStandardDepth2 : "", // 표준카테고리 중분류
                categoryStandardDepth3 : "", // 표준카테고리 소분류
                categoryStandardDepth4 : "", // 표준카테고리 세분류
                dateType : "registerDate", // 기간검색유형
                startDate : "", // 기간검색 시작일자
                endDate : "", // 기간검색 종료일자
                urSupplierId : "", // 공급업체
                warehouseGroup : "", // 출고처 그룹
                warehouseId : "", // 출고처 그룹 기준 출고처
                urBrandId : "", // 브랜드
                dpBrandId : "",
                approvalDiv : ""
            },
        });

        viewModel.publicStorageUrl = fnGetPublicStorageUrl();

        kendo.bind($("#searchForm"), viewModel);
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

        // 표준 카테고리 : 현재 선택된 표준 카테고리 Select 값 중 가장 마지막 값을 조회조건에 추가
        data['ilCategoryStandardId'] = checkCategoryStandardId();

        if( $("input[name=masterItemType]:eq(0)").is(":checked") ) {  // 마스터 품목유형 '전체' 체크시 : 해당 파라미터 전송하지 않음
            data['masterItemType'] = '';
        }

        if( $("input[name=storageMethod]:eq(0)").is(":checked") ) {  // 보관방법 '전체' 체크시 : 해당 파라미터 전송하지 않음
            data['storageMethod'] = '';
        }

        const	_pageSize = itemGrid && itemGrid.dataSource ? itemGrid.dataSource.pageSize() : PAGE_SIZE;

        if(!itemNameLengthChk) {

        	var query = {
        			page : 1,
        			pageSize : _pageSize,
        			filterLength : fnSearchData(data).length,
        			filter : {
        				filters : fnSearchData(data)
        			}
        	};

        	itemGridDs.query(query);
        }

	};

	//검색조건 초기화
	function fnClear() {
//		$('#searchForm').formClear(true);
		fnDefaultSet();

        // 최초 조회시에는 마스터 품목 유형 '전체' 체크
//        $('input[name=masterItemType]').each(function(idx, element) {
//            $(element).prop('checked', true);
//        });

        // 최초 조회시에는 보관방법 '전체' 체크
//        $('input[name=storageMethod]').each(function(idx, element) {
//            $(element).prop('checked', true);
//        });

        $('[data-id="fnDateBtnC"]').mousedown();

	};

	function fnDefaultSet() {
		// 데이터 초기화
        viewModel.searchInfo.set("ilItemCodeKind", "1");
        viewModel.searchInfo.set("ilItemCode", "");
        viewModel.searchInfo.set("itemName", "");
        viewModel.searchInfo.set("masterItemType", []);
        viewModel.searchInfo.set("erpLinkIfYn", "");

        viewModel.searchInfo.set("categoryStandardDepth1", "");
        viewModel.searchInfo.set("categoryStandardDepth2", "");
        viewModel.searchInfo.set("categoryStandardDepth3", "");
        viewModel.searchInfo.set("categoryStandardDepth4", "");
        viewModel.searchInfo.set("dateType", "registerDate");
        viewModel.searchInfo.set("startDate", "");
        viewModel.searchInfo.set("endDate", "");
        viewModel.searchInfo.set("urSupplierId", "");
        viewModel.searchInfo.set("warehouseGroup", "");
        viewModel.searchInfo.set("warehouseId", "");
        viewModel.searchInfo.set("urBrandId", "");
        viewModel.searchInfo.set("dpBrandId", "");
        viewModel.searchInfo.set("approvalDiv", "");

        $("input[name=masterItemType]:eq(0)").prop("checked", true).trigger("change");
        $("input[name=storageMethod]:eq(0)").prop("checked", true).trigger("change");
        $("input[name=approvalStatus]:eq(0)").prop("checked", true).trigger("change");
        $("input[name=erpLinkIfYn]:eq(0)").prop("checked", true).trigger("change");
        $("input[name=approvalDiv]:eq(0)").prop("checked", true).trigger("change");
//        $("input[name=clientApprovalStatus]:eq(0)").prop("checked", true).trigger("change");
	};

	//검색조건 관련 radio, dropdown, checkbox 구성
	function fnInitItemCtgry() {

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
	          chkVal: 'singleSection',
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

        //표준카테고리 시작
		// 표준카테고리 대분류
        fnKendoDropDownList({
            id : "categoryStandardDepth1",
            tagId : "categoryStandardDepth1",
            url : "/admin/comn/getDropDownCategoryStandardList",
            params : { "depth" : "1" },
            textField : "categoryName",
            valueField : "categoryId",
            blank : "대분류",
            async : false
        });

        // 표준카테고리 중분류
        fnKendoDropDownList({
            id : "categoryStandardDepth2",
            tagId : "categoryStandardDepth2",
            url : "/admin/comn/getDropDownCategoryStandardList",
            textField : "categoryName",
            valueField : "categoryId",
            blank : "중분류",
            async : false,
            cscdId : "categoryStandardDepth1",
            cscdField : "categoryId"
        });

        // 표준카테고리 소분류
        fnKendoDropDownList({
            id : "categoryStandardDepth3",
            tagId : "categoryStandardDepth3",
            url : "/admin/comn/getDropDownCategoryStandardList",
            textField : "categoryName",
            valueField : "categoryId",
            blank : "소분류",
            async : false,
            cscdId : "categoryStandardDepth2",
            cscdField : "categoryId"
        });

        // 표준카테고리 세분류
        fnKendoDropDownList({
            id : "categoryStandardDepth4",
            tagId : "categoryStandardDepth4",
            url : "/admin/comn/getDropDownCategoryStandardList",
            textField : "categoryName",
            valueField : "categoryId",
            blank : "세분류",
            async : false,
            cscdId : "categoryStandardDepth3",
            cscdField : "categoryId"
        });
        //표준카테고리 끝

		// 등록일/수정일
		fnKendoDropDownList({
			id : 'dateType',
			data : [ {
				code : 'registerDate',
				name : '등록일'
			}, {
				code : 'modifiedDate',
				name : '수정일'
			} ],
			valueField : 'code',
			textField : 'name'
		});

		//등록일/수정일 시작날짜
		fnKendoDatePicker({
			id : 'startDate',
			format : 'yyyy-MM-dd'
		});

		//등록일/수정일 종료날짜
		fnKendoDatePicker({
			id : 'endDate',
			format : 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startDate',
			btnEndId : 'endDate',
			change : function(e) {
				fnStartCalChange('startDate', 'endDate');
			}
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
        }).bind("change", onUrSupplierIdChange);

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

        fnKendoDropDownList({
        	 id : 'urBrandId',
             url : "/admin/ur/brand/searchBrandList",
             params : {
                 //searchUrSupplierId : e.sender.value(), // 변경한 공급업체 ID 값
                 "searchUseYn": "Y"
             },
             textField : "brandName",
             valueField : "urBrandId",
             blank : '전체'
        });

        fnKendoDropDownList({
            id : 'dpBrandId', 	// 전시 브랜드
            url : "/admin/ur/brand/searchDisplayBrandList",
            params : {"searchUseYn": "Y"},
            tagId : 'dpBrandId',
            autoBind : true,
            valueField : 'dpBrandId',
            textField : 'dpBrandName',
            chkVal : '',
            style : {},
            blank : '선택',
            params      : {"useYn" :"Y"}
        });

		// 선주문가능여부
		fnTagMkRadio({
			id : 'preOrderAvailable',
			data : [ {
				"CODE" : "",
				"NAME" : "전체"
			}, {
				"CODE" : "Y",
				"NAME" : "선주문가능 품목"
			}, {
				"CODE" : "N",
				"NAME" : "한정재고 품목"
			} ],
			tagId : 'preOrderAvailable',
			chkVal : ''
		});

		//마스터 품목 유형
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

		// 엑셀 양식 유형 - 엑셀다운로드 양식을 위한 공통
        fnKendoDropDownList({
        	id : "psExcelTemplateId",
        	url : "/admin/policy/excel/getPolicyExcelTmpltList",
        	tagId : "psExcelTemplateId",
        	params : { "excelTemplateTp" : "EXCEL_TEMPLATE_TP.MASTER_ITEM", "excelTemplateUseTp" : "DOWNLOAD"},
        	textField : "templateNm",
            valueField : "psExcelTemplateId",
        	blank : "선택",
        	async : false
        });

        $('#masterItemType').bind("change", onCheckboxWithTotalChange);

		// 최초 조회시에는 마스터 품목 유형 '전체' 체크
		$('input[name=masterItemType]').each(function(idx, element) {
		    $(element).prop('checked', true);
		});

        fnAjax({
            url : "/admin/comn/getCodeList",  // 보관방법 코드 조회
            method : 'GET',
            isAction : 'select',
            params      : {"stCommonCodeMasterCode" : "ERP_STORAGE_TYPE", "useYn" :"Y"},
            async : false,
            isAction : 'select',
            success : function(data, status, xhr) {

                // '전체' 코드 추가
                data['rows'].unshift( { 'CODE' : 'ERP_STORAGE_TYPE.ALL' , 'NAME' : '전체' } );

                fnTagMkChkBox({
                    id    : 'storageMethod',
                    data  : data['rows'],
                    tagId : 'storageMethod',
                    chkVal: '',
                    style : {}
                });

                $('#storageMethod').bind("change", onCheckboxWithTotalChange);

                // 최초 조회시에는 보관방법 '전체' 체크
                $('input[name=storageMethod]').each(function(idx, element) {
                    $(element).prop('checked', true);
                });

            },

        });

        // 숭인상태 - 승인대상 , 미승인대상
		fnTagMkRadio({
			id : 'approvalDiv',
			data : [ {
				"CODE" : "",
				"NAME" : "전체"
			}, {
				"CODE" : "Y",
				"NAME" : "승인대상"
			}, {
				"CODE" : "N",
				"NAME" : "미승인대상"
			} ],
			tagId : 'approvalDiv',
			chkVal : ''
		});

		// 승인여부
		$('#approvalStatus').bind("change", onCheckboxWithTotalChange);

		//마스터 품목 유형
		fnTagMkChkBox({
	        id    : "approvalStatus",
	        url : "/admin/comn/getCodeList",
	        params : {"stCommonCodeMasterCode" : "APPR_STAT", "useYn" :"Y"},
	        tagId : 'approvalStatus',
	        async : false,
	        chkVal: '',
	        style : {},
	        textField :"NAME",
			valueField : "CODE",
	        beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }]
	    });
	    $('input:checkbox[name="approvalStatus"]:input[value="APPR_STAT.NONE"]').parent().remove();
		$('input:checkbox[name="approvalStatus"]:input[value="APPR_STAT.APPROVED_BY_SYSTEM"]').parent().remove();
	    $('input:checkbox[name="approvalStatus"]:input[value="APPR_STAT.DISPOSAL"]').parent().remove();

        // 최초 조회시에는 '전체' 체크
        $('input[name=approvalStatus]').each(function(idx, element) {
            $(element).prop('checked', true);
        });

        // [S] 승인상태(거래처 품목수정)
//        fnTagMkChkBox({
//	        id    : "clientApprovalStatus",
//	        url : "/admin/comn/getCodeList",
//	        params : {"stCommonCodeMasterCode" : "APPR_STAT", "useYn" :"Y"},
//	        tagId : 'clientApprovalStatus',
//	        async : false,
//	        chkVal: '',
//	        style : {},
//	        textField :"NAME",
//			valueField : "CODE",
//	        beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }]
//	    });
//	    $('input:checkbox[name="clientApprovalStatus"]:input[value="APPR_STAT.NONE"]').parent().remove();
//	    $('input:checkbox[name="clientApprovalStatus"]:input[value="APPR_STAT.SAVE"]').parent().remove();
//	    $('input:checkbox[name="clientApprovalStatus"]:input[value="APPR_STAT.APPROVED_BY_SYSTEM"]').parent().remove();
//	    $('input:checkbox[name="clientApprovalStatus"]:input[value="APPR_STAT.DISPOSAL"]').parent().remove();
//
//        $('input[name=clientApprovalStatus]').each(function(idx, element) {
//            $(element).prop('checked', true);
//        });
//
//        $('#clientApprovalStatus').bind("change", onCheckboxWithTotalChange);
        // [E] 승인상태(거래처 품목수정)

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

//    function onApprovalDivChange(e) {
//
//    	if(e.target.value == "Y") {
//    		$("#approvalStatus").show();
//    	}else {
//    		$("#approvalStatus").hide();
//    	}
//    };

    //공급업체 변경시 브랜드 Dropdown 갱신
    function onUrSupplierIdChange(e) {

    	if ( e.sender.value() ) {

//            $('#urBrandId').attr("disabled", false); // 비활성화 해제

            /* 변경한 공급업체 ID 로 브랜드 리스트 호출 */
//            fnKendoDropDownList({
//                id : 'urBrandId',
//                url : "/admin/ur/brand/searchBrandList",
//                params : {
//                    searchUrSupplierId : e.sender.value(), // 변경한 공급업체 ID 값
//                    "searchUseYn": "Y"
//                },
//                textField : "brandName",
//                valueField : "urBrandId",
//                blank : '전체'
//            });

        } else {

//            $('#urBrandId').attr("disabled", true); // 비활성화

//            fnKendoDropDownList({
//                id : 'urBrandId', // 최초 조회시 브랜드 목록 없음 : 공급업체 선택시 해당 공급업체 ID 로 조회
//                data : {},
//                valueField : 'code',
//                textField : 'name',
//                blank : '전체'
//            });


        }
    };

    function fnWareHouseGroupChange() {
    	fnAjax({
//            url     : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
            url     : "/admin/comn/getDropDownAuthWarehouseGroupByWarehouseList",
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

	//마스터 품목 리스트 Grid
	function fnItemGrid() {

		itemGridDs = fnGetEditPagingDataSource({
			url : '/admin/item/master/getItemList',
			pageSize : PAGE_SIZE,
            requestEnd : function(e){
            }
		});

		itemGridOpt = {
			dataSource : itemGridDs,
			pageable : {
				pageSizes : [ 20, 30, 50 ],
				buttonCount : 5
			},
			editable : false,
			navigatable: true,
			columns : [
				{ title : 'No', width:'50px', attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>" }
	        ,   { field : 'ilItemCodeBarcode',  title : '품목코드/<br>품목바코드', width : '140px', attributes : { style : 'text-align:center' },
                  template: function(dataItem) {

                        if(dataItem['ilItemCode'] != "" && dataItem['itemBarcode'] != "") {
                            return dataItem['ilItemCode'] + "/<br>" + dataItem['itemBarcode'];
                        } else if (dataItem['itemBarcode'] == "" && dataItem['ilItemCode'] != "") {
                            return dataItem['ilItemCode'] + "/- ";
                        } else if (dataItem['ilItemCode'] == "" && dataItem['itemBarcode'] != "") {
                            return  "- /" + dataItem['itemBarcode'];
                        } else if ( dataItem['ilItemCode'] == "" && dataItem['itemBarcode'] == "" ) {
                            return "- / -";
                        }

	              }
	            }
			,	{ field : 'ilItemCode', title : '품목코드', attributes : { style : 'text-align:center' }, hidden:true }
			,   { field : 'itemTypeName', title : '마스터 품목유형', width : '10%', attributes : { style : 'text-align:center' } }
            ,   { field : 'erpLinkIfYn', title : 'ERP 연동여부', width : '9%', attributes : { style : 'text-align:center' },
                	template: function(dataItem) {
                		return dataItem['erpLinkIfYn'] == true ? '연동' : '미연동';
                	}
			    }
            ,	{ field : 'itemName', title : '마스터 품목명', width : '25%', attributes : { style : 'text-align:center' },
                	template: function(dataItem) {
                		var titleStr = "(판매중지)";
                		if(dataItem.extinctionYn == "Y") {
                			return titleStr +" " + dataItem.itemName;
                		}else {
                			return dataItem['itemName'];
                		}

                	}
			    }
			,	{ field : 'supplierName', title : '공급업체', width : '8%', attributes : { style : 'text-align:center' } }
			,	{ field : 'brandName', title : '표준 브랜드', width : '10%', attributes : { style : 'text-align:center' } }
			,	{ field : 'dpBrandName', title : '전시 브랜드', width : '10%', attributes : { style : 'text-align:center' } }
			,	{ field : 'standardCategoryFullName', title : '표준카테고리', width : '15%', attributes : { style : 'text-align:left' },
                  template: function(dataItem) {
                      return dataItem['standardCategoryFullName'];
                  }
			    }
			,	{ field : 'distributionPeriod', title : '유통<br>기간', width : '40px', attributes : { style : 'text-align:center' }, format : "{0:n0}" }
			,	{ field : 'storageMethodName', title : '보관<br>방법', width : '60px', attributes : { style : 'text-align:center' } }
			,	{ field : 'poTypeName', title : '발주구분', width : '80px', attributes : { style : 'text-align:center' }, hidden : true }
			, 	{ field : 'itemModify', title : '품목관리' , width:'100px'	,attributes:{ style:'text-align:center' }
					,template: function(dataItem) {
						var html =	"";
        				if(fnIsProgramAuth("ITEM_UPDATE")) {
    						html =	"<a role='button' class='k-button k-button-icontext' kind='itemModify' >품목수정</a>";
        				}
						return html;
					}
				}
			, 	{ field : 'itemApprovalInfo', title : '승인상태' , width:'100px'	,attributes:{ style:'text-align:center' }
					,template: function(dataItem) {
						var html =	"";
						if (
							!fnIsEmpty(dataItem.itemRegistApprStat)
							&& (dataItem.itemRegistApprStat == 'APPR_STAT.REQUEST' || dataItem.itemRegistApprStat == 'APPR_STAT.SUB_APPROVED')
						) {
							html = '품목등록<BR>/' + dataItem.itemRegistApprStatNm;
						}
						else if (
							!fnIsEmpty(dataItem.itemClientApprStat)
							&& (dataItem.itemClientApprStat == 'APPR_STAT.REQUEST' || dataItem.itemClientApprStat == 'APPR_STAT.SUB_APPROVED')
						) {
							html = '거래처 품목수정<BR>/' + dataItem.itemClientApprStatNm;
						}
						else {
							if (!fnIsEmpty(dataItem.itemRegistApprStat)) {
								html = '품목등록<BR>/' + dataItem.itemRegistApprStatNm;
							}
						}

						if(fnIsProgramAuth("ITEM_UPDATE")) {
							if (
								!fnIsEmpty(dataItem.itemRegistApprStat)
								&& (dataItem.itemRegistApprStat == 'APPR_STAT.REQUEST' || dataItem.itemRegistApprStat == 'APPR_STAT.SUB_APPROVED')
							) {
								html +=	"<br/><a role='button' class='k-button k-button-icontext' kind='itemRegistApprovalInfo' >승인내역</a>";
							}
							else if (
								!fnIsEmpty(dataItem.itemClientApprStat)
								&& (dataItem.itemClientApprStat == 'APPR_STAT.REQUEST' || dataItem.itemClientApprStat == 'APPR_STAT.SUB_APPROVED')
							) {
								html +=	"<br/><a role='button' class='k-button k-button-icontext' kind='itemClientApprovalInfo' >승인내역</a>";
							}
							else if (
								!fnIsEmpty(dataItem.itemRegistApprStat)
								&& dataItem.itemRegistApprStat != 'APPR_STAT.APPROVED'
							) {
								html +=	"<br/><a role='button' class='k-button k-button-icontext' kind='itemRegistApprovalInfo' >승인내역</a>";
							}
						}
						return html;
					}
				}
			,	{ field : 'priceApprTargetYn', title : '품목가격<br>승인대상', width : '10%', attributes : { style : 'text-align:center' } }
			,	{ field : '', title : '재고', width : '70px', attributes : { style : 'text-align:center' }, hidden:true }
	        ,   { field : 'urWarehouseId', title : '출고처 PK', attributes : { style : 'text-align:center' }, hidden:true }
	        ,   { field : 'poTpNm', title : '발주구분', width : '80px', attributes : { style : 'text-align:center' } }
            ,   { field : 'warehouseName', title : '출고지', width : '150px', attributes : { style : 'text-align:center' },
                  template: function(dataItem) {
                      return ( dataItem['warehouseName'] ? dataItem['warehouseName'] + ( dataItem['preOrderYn']  == 'Y' ? '<br>선주문가능' : '' ) : '' );
                  }
                }
            ,   { field : 'stockOrderYn', title : '재고발주여부', attributes : { style : 'text-align:center' }, hidden:true }
            ,   { field : 'storeYn', title : '매장(가맹점)여부', width: '100px', attributes : { style : 'text-align:center' }, hidden:true }

            ,   { field : 'supplierCode', title : '공급업체코드', width: '100px', attributes : { style : 'text-align:center' }, hidden:true }
            ,   { field : 'mallDivId', title : 'MALL_DIV_ID', width: '100px', attributes : { style : 'text-align:center' }, hidden:true }

            ,	{ title: '관리', width: "130px", attributes:{ style:'text-align:center', class:'forbiz-cell-readonly' } ,
                  template : kendo.template($("#command-template").html())
		        }
            ]
		};

		itemGrid = $('#itemGrid').initializeKendoGrid(itemGridOpt).cKendoGrid();

        // 그리드 Command 클릭 이벤트 : 각 상품 화면으로 이동
        $($("#itemGrid").data("kendoGrid").tbody).on("click", "[kind]", function(e) {

            e.preventDefault();

            var grid = $("#itemGrid").data("kendoGrid");
            var row = $(this).closest("tr");
            var colIdx = $("td", row).index(this);
            var rowData = grid.dataItem(row);
			var companyType = !fnIsEmpty(PG_SESSION) && !fnIsEmpty(PG_SESSION.companyType) ? PG_SESSION.companyType : "" // 거래처 계정은 상품 수정만 가능(등록 불가능)

            switch (e.currentTarget.text) {

                case "일반상품" :

                    var option = new Object();

                    option.url = "#/goodsMgm";       // 일반상품 URL
                    option.target = "_blank";

                    option.data = {                  // 일반상품 화면으로 전달할 Data
                            ilItemCode : rowData.ilItemCode,
                            urWarehouseId : rowData.urWarehouseId,
                    };

                    fnAjax({
        				url		: '/admin/goods/regist/duplicateGoods',
        				params	: {ilItemCode : rowData.ilItemCode, urWarehouseId : rowData.urWarehouseId, goodsType : "GOODS_TYPE.NORMAL"},
        				async	: false,
        				contentType : 'application/json',
        				success	: function(data){
        					if(data.ilGoodsId){
        						if (data.saleStatus == 'SALE_STATUS.SAVE' && companyType != 'COMPANY_TYPE.HEADQUARTERS') { // 상품이 등록되지 않았으면 거래처 관리자는 상품수정이 불가능
        							fnKendoMessage({ message : "등록된 상품정보가 없습니다." });
        						}
        						else {
            						fnKendoMessage({ type : 'confirm', message : "이미 등록된 상품정보를 불러오시겠습니까?", ok : function (){
            							//location.href = "/layout.html#/goodsMgm?ilGoodsId="+data.ilGoodsId;
            							option.data = {
            									ilGoodsId : data.ilGoodsId
            							}
            							fnGoNewPage(option);
            						}});
        						}
        						return;
        					} else {
        						if (companyType == 'COMPANY_TYPE.HEADQUARTERS') {
        							if (
    									!fnIsEmpty(rowData.itemClientApprStat)
    									&& (rowData.itemClientApprStat == 'APPR_STAT.REQUEST' || rowData.itemClientApprStat == 'APPR_STAT.SUB_APPROVED')
    								)
										fnKendoMessage({ message : "거래처 품목 수정 승인 처리중입니다.<BR>해당 상태에서는 상품 등록이 불가합니다." });
        							else
        								fnGoNewPage(option);
        						}
        						else
        							fnKendoMessage({ message : "등록된 상품정보가 없습니다." });
        					}
        				}
        			});

                    break;

                case "증정품" :

                	var option = new Object();

                    option.url = "#/goodsAdditional";       // 추가/증정상품 URL
                    option.target = "_blank";
                    option.data = {                  // 추가상품 화면으로 전달할 Data
                            ilItemCode : rowData.ilItemCode,
                            urWarehouseId : rowData.urWarehouseId,
                            goodsType : "GOODS_TYPE.GIFT",
                            goodsTypeName : "증정"
                    };

                    fnAjax({
        				url		: '/admin/goods/regist/duplicateGoods',
        				params	: {ilItemCode : rowData.ilItemCode, urWarehouseId : rowData.urWarehouseId, goodsType : "GOODS_TYPE.GIFT"},
        				async	: false,
        				contentType : 'application/json',
        				success	: function(data){
        					if(data.ilGoodsId){
        						if (data.saleStatus == 'SALE_STATUS.SAVE' && companyType != 'COMPANY_TYPE.HEADQUARTERS') { // 상품이 등록되지 않았으면 거래처 관리자는 상품수정이 불가능
        							fnKendoMessage({ message : "등록된 상품정보가 없습니다." });
        						}
        						else {
	        						fnKendoMessage({ type : 'confirm', message : "이미 등록된 상품정보를 불러오시겠습니까?", ok : function (){
	        							//location.href = "/layout.html#/goodsAdditional?ilGoodsId="+data.ilGoodsId;
	        							option.data = {
	        									ilGoodsId : data.ilGoodsId
	        							}
	        							fnGoNewPage(option);
	        						}});
        						}
        						return;
        					} else {
								if(companyType == 'COMPANY_TYPE.HEADQUARTERS') {
        							if (
    									!fnIsEmpty(rowData.itemClientApprStat)
    									&& (rowData.itemClientApprStat == 'APPR_STAT.REQUEST' || rowData.itemClientApprStat == 'APPR_STAT.SUB_APPROVED')
    								)
										fnKendoMessage({ message : "거래처 품목 수정 승인 처리중입니다.<BR>해당 상태에서는 상품 등록이 불가합니다." });
        							else {
    									fnAjax({
    										url		: '/admin/goods/regist/duplicateGoods',
    										params	: {ilItemCode : rowData.ilItemCode, urWarehouseId : rowData.urWarehouseId, goodsType : "GOODS_TYPE.NORMAL"},
    										async	: false,
    										contentType : 'application/json',
    										success	: function(data){
    											if(data.ilGoodsId){
    												fnKendoMessage({ type : 'confirm', message : "등록된 일반상품이 있습니다. 상품 정보를 반영하시겠습니까?", ok : function (){
    														option.data.normalGoodsId = data.ilGoodsId;
    														fnGoNewPage(option);
    													}});
    												return;
    											} else {
    												fnGoNewPage(option);
    											}
    										}
    									});
        							}
								}
								else
									fnKendoMessage({ message : "등록된 상품정보가 없습니다." });
        					}
        				}
        			});

                    break;
                case "식품마케팅증정품" :

                    var option = new Object();

                    option.url = "#/goodsAdditional";       // 추가/증정상품 URL
                    option.target = "_blank";
                    option.data = {                  // 추가상품 화면으로 전달할 Data
                        ilItemCode : rowData.ilItemCode,
                        urWarehouseId : rowData.urWarehouseId,
                        goodsType : "GOODS_TYPE.GIFT_FOOD_MARKETING",
                        goodsTypeName : "식품마케팅증정"
                    };

                    fnAjax({
                        url		: '/admin/goods/regist/duplicateGoods',
                        params	: {ilItemCode : rowData.ilItemCode, urWarehouseId : rowData.urWarehouseId, goodsType : "GOODS_TYPE.GIFT_FOOD_MARKETING"},
                        async	: false,
                        contentType : 'application/json',
                        success	: function(data){
                            if(data.ilGoodsId){
        						if (data.saleStatus == 'SALE_STATUS.SAVE' && companyType != 'COMPANY_TYPE.HEADQUARTERS') { // 상품이 등록되지 않았으면 거래처 관리자는 상품수정이 불가능
        							fnKendoMessage({ message : "등록된 상품정보가 없습니다." });
        						}
        						else {
        							fnKendoMessage({ type : 'confirm', message : "이미 등록된 상품정보를 불러오시겠습니까?", ok : function (){
        								//location.href = "/layout.html#/goodsAdditional?ilGoodsId="+data.ilGoodsId;
                                        option.data = {
                                            ilGoodsId : data.ilGoodsId
                                        }
                                        fnGoNewPage(option);
                                    }});
        						}
                                return;
                            } else {
								if(companyType == 'COMPANY_TYPE.HEADQUARTERS') {
        							if (
    									!fnIsEmpty(rowData.itemClientApprStat)
    									&& (rowData.itemClientApprStat == 'APPR_STAT.REQUEST' || rowData.itemClientApprStat == 'APPR_STAT.SUB_APPROVED')
    								)
										fnKendoMessage({ message : "거래처 품목 수정 승인 처리중입니다.<BR>해당 상태에서는 상품 등록이 불가합니다." });
        							else {
    									fnAjax({
    										url		: '/admin/goods/regist/duplicateGoods',
    										params	: {ilItemCode : rowData.ilItemCode, urWarehouseId : rowData.urWarehouseId, goodsType : "GOODS_TYPE.NORMAL"},
    										async	: false,
    										contentType : 'application/json',
    										success	: function(data){
    											if(data.ilGoodsId){
    												fnKendoMessage({ type : 'confirm', message : "등록된 일반상품이 있습니다. 상품 정보를 반영하시겠습니까?", ok : function (){
    														option.data.normalGoodsId = data.ilGoodsId;
    														fnGoNewPage(option);
    													}});
    												return;
    											} else {
    												fnGoNewPage(option);
    											}
    										}
    									});
        							}
								}
								else
									fnKendoMessage({ message : "등록된 상품정보가 없습니다." });
                            }
                        }
                    });

                    break;

                case "추가상품" :

                	var option = new Object();

                    option.url = "#/goodsAdditional";       // 추가/증정상품 URL
                    option.target = "_blank";
                    option.data = {                  // 추가상품 화면으로 전달할 Data
                            ilItemCode : rowData.ilItemCode,
                            urWarehouseId : rowData.urWarehouseId,
                            goodsType : "GOODS_TYPE.ADDITIONAL",
                            goodsTypeName : "추가"
                    };

                    fnAjax({
        				url		: '/admin/goods/regist/duplicateGoods',
        				params	: {ilItemCode : rowData.ilItemCode, urWarehouseId : rowData.urWarehouseId, goodsType : "GOODS_TYPE.ADDITIONAL"},
        				async	: false,
        				contentType : 'application/json',
        				success	: function(data){
        					if(data.ilGoodsId){
        						if (data.saleStatus == 'SALE_STATUS.SAVE' && companyType != 'COMPANY_TYPE.HEADQUARTERS') { // 상품이 등록되지 않았으면 거래처 관리자는 상품수정이 불가능
        							fnKendoMessage({ message : "등록된 상품정보가 없습니다." });
        						}
        						else {
	        						fnKendoMessage({ type : 'confirm', message : "이미 등록된 상품정보를 불러오시겠습니까?", ok : function (){
	        							//location.href = "/layout.html#/goodsAdditional?ilGoodsId="+data.ilGoodsId;
	        							option.data = {
	        									ilGoodsId : data.ilGoodsId
	        							}
	        							fnGoNewPage(option);
	        						}});
        						}
        						return;
        					} else {
								if(companyType == 'COMPANY_TYPE.HEADQUARTERS') {
        							if (
    									!fnIsEmpty(rowData.itemClientApprStat)
    									&& (rowData.itemClientApprStat == 'APPR_STAT.REQUEST' || rowData.itemClientApprStat == 'APPR_STAT.SUB_APPROVED')
    								)
										fnKendoMessage({ message : "거래처 품목 수정 승인 처리중입니다.<BR>해당 상태에서는 상품 등록이 불가합니다." });
        							else {
    									fnAjax({
    										url		: '/admin/goods/regist/duplicateGoods',
    										params	: {ilItemCode : rowData.ilItemCode, urWarehouseId : rowData.urWarehouseId, goodsType : "GOODS_TYPE.NORMAL"},
    										async	: false,
    										contentType : 'application/json',
    										success	: function(data){
    											if(data.ilGoodsId){
    												fnKendoMessage({ type : 'confirm', message : "등록된 일반상품이 있습니다. 상품 정보를 반영하시겠습니까?", ok : function (){
    														option.data.normalGoodsId = data.ilGoodsId;
    														fnGoNewPage(option);
    													}});
    												return;
    											} else {
    												fnGoNewPage(option);
    											}
    										}
    									});
        							}
								}
								else
									fnKendoMessage({ message : "등록된 상품정보가 없습니다." });
        					}
        				}
        			});

                    break;

                case "폐기임박상품" :
                	var option = new Object();

                    option.url = "#/goodsDisposal";       // 폐기임박상품 URL
                    option.target = "_blank";
                    option.data = {                  // 폐기임박상품 화면으로 전달할 Data
                            ilItemCode : rowData.ilItemCode,
                            urWarehouseId : rowData.urWarehouseId,
                    };

                    fnAjax({
        				url		: '/admin/goods/regist/duplicateGoods',
        				params	: {ilItemCode : rowData.ilItemCode, urWarehouseId : rowData.urWarehouseId, goodsType : "GOODS_TYPE.DISPOSAL"},
        				async	: false,
        				contentType : 'application/json',
        				success	: function(data){
        					if(data.ilGoodsId){
        						if (data.saleStatus == 'SALE_STATUS.SAVE' && companyType != 'COMPANY_TYPE.HEADQUARTERS') { // 상품이 등록되지 않았으면 거래처 관리자는 상품수정이 불가능
        							fnKendoMessage({ message : "등록된 상품정보가 없습니다." });
        						}
        						else {
	        						fnKendoMessage({ type : 'confirm', message : "이미 등록된 상품정보를 불러오시겠습니까?", ok : function (){
	        							//location.href = "/layout.html#/goodsDisposal?ilGoodsId="+data.ilGoodsId;
	        							option.data = {
	        									ilGoodsId : data.ilGoodsId
	        							}
	        							fnGoNewPage(option);
	        						}});
        						}
        						return;
        					} else {
								if(companyType == 'COMPANY_TYPE.HEADQUARTERS') {
        							if (
    									!fnIsEmpty(rowData.itemClientApprStat)
    									&& (rowData.itemClientApprStat == 'APPR_STAT.REQUEST' || rowData.itemClientApprStat == 'APPR_STAT.SUB_APPROVED')
    								)
										fnKendoMessage({ message : "거래처 품목 수정 승인 처리중입니다.<BR>해당 상태에서는 상품 등록이 불가합니다." });
        							else {
    									fnAjax({
    										url		: '/admin/goods/regist/duplicateGoods',
    										params	: {ilItemCode : rowData.ilItemCode, urWarehouseId : rowData.urWarehouseId, goodsType : "GOODS_TYPE.NORMAL"},
    										async	: false,
    										contentType : 'application/json',
    										success	: function(data){
    											if(data.ilGoodsId){
    												fnKendoMessage({ type : 'confirm', message : "등록된 일반상품이 있습니다. 상품 정보를 반영하시겠습니까?", ok : function (){
    														option.data.normalGoodsId = data.ilGoodsId;
    														fnGoNewPage(option);
    													}});
    												return;
    											} else {
    												fnGoNewPage(option);
    											}
    										}
    									});
        							}
								}
								else
									fnKendoMessage({ message : "등록된 상품정보가 없습니다." });
        					}
        				}
        			});

                    break;
                case "묶음상품" :

                    fnKendoPopup({
                        id : "itemGoodsPackagePopup",
                        title : '연관 묶음상품 목록',
                        src : '#/itemGoodsPackagePopup',
                        width : '1000px',
                        height : '500px',
                        param : {
                            ilItemCode : rowData.ilItemCode ,
                        }
                    });

                    break;

                case "일일상품" :

                    var option = new Object();

                    option.url = "#/goodsDaily";      // 일일상품 URL
                    option.target = "_blank";
                    option.data = {                   // 일일상품 화면으로 전달할 Data
                            ilItemCode : rowData.ilItemCode,
                            urWarehouseId : rowData.urWarehouseId,
                    };

                    fnAjax({
        				url		: '/admin/goods/regist/duplicateGoods',
        				params	: {ilItemCode : rowData.ilItemCode, urWarehouseId : rowData.urWarehouseId, goodsType : "GOODS_TYPE.DAILY"},
        				async	: false,
        				contentType : 'application/json',
        				success	: function(data){
        					if(data.ilGoodsId){
        						if (data.saleStatus == 'SALE_STATUS.SAVE' && companyType != 'COMPANY_TYPE.HEADQUARTERS') { // 상품이 등록되지 않았으면 거래처 관리자는 상품수정이 불가능
        							fnKendoMessage({ message : "등록된 상품정보가 없습니다." });
        						}
        						else {
	        						fnKendoMessage({ type : 'confirm', message : "이미 등록된 상품정보를 불러오시겠습니까?", ok : function (){
	        							//location.href = "/layout.html#/goodsDaily?ilGoodsId="+data.ilGoodsId;
	        							option.data = {
	        									ilGoodsId : data.ilGoodsId
	        							}
	        							fnGoNewPage(option);
	        						}});
        						}
        						return;
        					} else {
								if(companyType == 'COMPANY_TYPE.HEADQUARTERS') {
        							if (
    									!fnIsEmpty(rowData.itemClientApprStat)
    									&& (rowData.itemClientApprStat == 'APPR_STAT.REQUEST' || rowData.itemClientApprStat == 'APPR_STAT.SUB_APPROVED')
    								)
										fnKendoMessage({ message : "거래처 품목 수정 승인 처리중입니다.<BR>해당 상태에서는 상품 등록이 불가합니다." });
           							else
           								fnGoNewPage(option);
								}
								else
									fnKendoMessage({ message : "등록된 상품정보가 없습니다." });
        					}
        				}
        			});

                    break;
                case "매장상품" :

                	var option = new Object();

                	option.url = "#/goodsShopOnly";      // 매장상품 URL
                	option.target = "_blank";
                	option.menuId = 1176;                // 매장상품 메뉴 ID
                	option.data = {                      // 매장상품 화면으로 전달할 Data
                			ilItemCode : rowData.ilItemCode,
                			urWarehouseId : rowData.urWarehouseId,
                	};

                	fnAjax({
                		url		: '/admin/goods/regist/duplicateGoods',
                		params	: {ilItemCode : rowData.ilItemCode, urWarehouseId : rowData.urWarehouseId, goodsType : "GOODS_TYPE.SHOP_ONLY"},
                		async	: false,
                		contentType : 'application/json',
                		success	: function(data){
                			if(data.ilGoodsId){
        						if (data.saleStatus == 'SALE_STATUS.SAVE' && companyType != 'COMPANY_TYPE.HEADQUARTERS') { // 상품이 등록되지 않았으면 거래처 관리자는 상품수정이 불가능
        							fnKendoMessage({ message : "등록된 상품정보가 없습니다." });
        						}
        						else {
	                				fnKendoMessage({ type : 'confirm', message : "이미 등록된 상품정보를 불러오시겠습니까?", ok : function (){
	                					//location.href = "/layout.html#/goodsShopOnly?ilGoodsId="+data.ilGoodsId;
	                					option.data = {
	        									ilGoodsId : data.ilGoodsId
	        							}
	        							fnGoNewPage(option);
	                				}});
        						}
                				return;
                			} else {
								if(companyType == 'COMPANY_TYPE.HEADQUARTERS') {
        							if (
    									!fnIsEmpty(rowData.itemClientApprStat)
    									&& (rowData.itemClientApprStat == 'APPR_STAT.REQUEST' || rowData.itemClientApprStat == 'APPR_STAT.SUB_APPROVED')
    								)
										fnKendoMessage({ message : "거래처 품목 수정 승인 처리중입니다.<BR>해당 상태에서는 상품 등록이 불가합니다." });
           							else
           								fnGoNewPage(option);
								}
								else
									fnKendoMessage({ message : "등록된 상품정보가 없습니다." });
                			}
                		}
                	});

                	break;

                case "렌탈상품" :

                	var option = new Object();

                	option.url = "#/goodsRental";      // 렌탈상품 URL
                	option.target = "_blank";
                	option.menuId = 1286;              // 렌탈상품 메뉴 ID
                	option.data = {                    // 렌탈상품 화면으로 전달할 Data
                			ilItemCode : rowData.ilItemCode,
                			urWarehouseId : rowData.urWarehouseId,
                	};

                	fnAjax({
                		url		: '/admin/goods/regist/duplicateGoods',
                		params	: {ilItemCode : rowData.ilItemCode, urWarehouseId : rowData.urWarehouseId, goodsType : "GOODS_TYPE.RENTAL"},
                		async	: false,
                		contentType : 'application/json',
                		success	: function(data){
                			if(data.ilGoodsId){
        						if (data.saleStatus == 'SALE_STATUS.SAVE' && companyType != 'COMPANY_TYPE.HEADQUARTERS') { // 상품이 등록되지 않았으면 거래처 관리자는 상품수정이 불가능
        							fnKendoMessage({ message : "등록된 상품정보가 없습니다." });
        						}
        						else {
	                				fnKendoMessage({ type : 'confirm', message : "이미 등록된 상품정보를 불러오시겠습니까?", ok : function (){
	                					//location.href = "/layout.html#/goodsRental?ilGoodsId="+data.ilGoodsId;
	                					option.data = {
	        									ilGoodsId : data.ilGoodsId
	        							}
	        							fnGoNewPage(option);
	                				}});
        						}
                				return;
        					} else {
								if(companyType == 'COMPANY_TYPE.HEADQUARTERS') {
        							if (
    									!fnIsEmpty(rowData.itemClientApprStat)
    									&& (rowData.itemClientApprStat == 'APPR_STAT.REQUEST' || rowData.itemClientApprStat == 'APPR_STAT.SUB_APPROVED')
    								)
										fnKendoMessage({ message : "거래처 품목 수정 승인 처리중입니다.<BR>해당 상태에서는 상품 등록이 불가합니다." });
           							else
           								fnGoNewPage(option);
								}
								else
									fnKendoMessage({ message : "등록된 상품정보가 없습니다." });
                			}
                		}
                	});

                	break;

                case "무형상품" :
                    var option = new Object();

                    option.url = "#/goodsIncorporeal";
                    option.target = "_blank";
                    option.data = {
                        ilItemCode : rowData.ilItemCode,
                        urWarehouseId : rowData.urWarehouseId,
                    };

                    fnAjax({
                        url		: '/admin/goods/regist/duplicateGoods',
                        params	: {ilItemCode : rowData.ilItemCode, urWarehouseId : rowData.urWarehouseId, goodsType : "GOODS_TYPE.INCORPOREITY"},
                        async	: false,
                        contentType : 'application/json',
                        success	: function(data){
                            if(data.ilGoodsId){
                                fnKendoMessage({ type : 'confirm', message : "이미 등록된 상품정보를 불러오시겠습니까?", ok : function (){
//                					location.href = "/layout.html#/goodsIncorporeal?ilGoodsId="+data.ilGoodsId;
                                        option.data = {
                                            ilGoodsId : data.ilGoodsId
                                        }
                                        fnGoNewPage(option);
                                    }});
                                return;
                            } else {
                                fnGoNewPage(option);
                            }
                        }
                    });

                    break;
                case "품목수정" :

            	    var option = new Object();
            	    option.url = "#/itemMgmModify";  // 마스터 품목 수정 화면 URL
            	    option.target = "_blank";
            	    option.data = {                  // 마스터 품목 수정 화면으로 전달할 Data
            	            ilItemCode : rowData.ilItemCode,
            	            isErpItemLink : rowData.erpLinkIfYn,
            	            masterItemType : rowData.itemType
            	    };

            	    fnGoNewPage(option);

                	break;

                case "승인내역" :
            	    var option = new Object();
            	    option.url = "";
            	    option.target = "_blank";

            	    var kindValue = e.currentTarget.attributes.kind.value;
            	    if (kindValue == 'itemRegistApprovalInfo') {
            			if (rowData.itemRegistApprReqUserId == PG_SESSION.userId) // 품목등록 승인 요청자의 경우
        					option.url = "#/approvalItemRegistRequest"; // 품목등록 승인요청 URL
        				else
        					option.url = "#/approvalItemRegistAccept"; // 품목등록 승인관리 URL
            	    }
            	    else if (kindValue == 'itemClientApprovalInfo') {
            			if (PG_SESSION.companyType == "COMPANY_TYPE.HEADQUARTERS") // 임직원 관리자의 경우
            				option.url = "#/approvalItemClientAccept"; // 거래처 품목 승인관리 URL
            			else // 거래처 관리자의 경우
            				option.url = "#/approvalItemClientRequest"; // 거래처 품목 승인요청 URL
            	    }

            	    option.data = {                  // 승인 화면으로 전달할 Data
        	    		ilItemCode : rowData.ilItemCode,
            	    };

            	    if (option.url != "")
            	    	fnGoNewPage(option);
                	break;

            };
        });

		itemGrid.bind("dataBound", function() {

		    var row_num = itemGridDs._total - ( ( itemGridDs._page - 1 ) * itemGridDs._pageSize );

		    $("#itemGrid tbody > tr .row-number").each(function(index){
	            $(this).html(row_num);
	            row_num--;
		    });

		    $("#pageTotalText").text(itemGridDs._total);

            // rowspan 로직 Start
            mergeGridRows(
                'itemGrid' // div 로 지정한 그리드 ID
               , ['ilItemCodeBarcode', 'itemTypeName', 'erpLinkIfYn', 'itemName', 'supplierName', 'brandName', 'dpBrandName', 'standardCategoryFullName' //
               , 'distributionPeriod', 'storageMethodName', 'poTypeName', 'itemModify', 'itemApprovalInfo', 'tempStatus'] // 그리드에서 셀 머지할 컬럼들의 data-field 목록
               , ['ilItemCodeBarcode'] // group by 할 컬럼들의 data-field 목록
            );
            // rowspan 로직 End

		});

	};

    /*
     * Kendo Grid 전용 rowSpan 메서드
     *
     * @param gridId : div 로 지정한 그리드 ID, 해당 div 내 table 태그를 찾아감
     * @param mergeColumns : 그리드에서 셀 머지할 컬럼들의 data-field 목록
     * @param groupByColumns : group by 할 컬럼들의 data-field 목록, 해당 group 내에서만 셀 머지가 일어남
     *
     */
    function mergeGridRows(gridId, mergeColumns, groupByColumns) {

        if( $('#' + gridId + ' > table > tbody > tr').length <= 1 ) { // 데이터 1건 이하인 경우 : rowSpan 불필요하므로 return
            return;
        }

        var groupByColumnIndexArray = [];  // group by 할 컬럼들의 th 헤더 내 column index 목록
        var tdArray = [];  // 해당 컬럼의 모든 td 배열, 개수 / 순서는 그리드 내 tr 개수 / 순서와 같음
        var groupNoArray = [];  // 파라미터로 전달된 groupByColumns 에 따라 계산된 그룹번호 배열, 같은 그룹인 경우 그룹번호 같음, 개수 / 순서는 tdArray 와 같음

        var groupNo;  // 각 tr 별 그룹번호, 같은 그룹인 경우 그룹번호 같음
        var beforeTr = null; // 이전 tr
        var beforeTd = null; // 이전 td
        var rowspan = null; // rowspan 할 개수, 1 인경우 rowspan 하지 않음

        var thRow = $('#' + gridId + ' > table > thead > tr')[0];  // 해당 그리드의 th 헤더 row

        // 셀 머지시 group by 할 컬럼들의 data-field 목록이 Array 형태의 파라미터로 전달시
        if( groupByColumns && Array.isArray(groupByColumns) && groupByColumns.length > 0 ) {

            $(thRow).children('th').each(function (thIndex, th) {  // thead 의 th 반복문 시작 : table 내 각 컬럼별로 반복

                // groupByColumns => groupByColumnIndexArray 로 변환
                if( groupByColumns.includes( $(th).attr('data-field') ) ) {
                    groupByColumnIndexArray.push(thIndex);
                }

            });

        } // if 문 끝

        $('#' + gridId + ' > table > tbody > tr').each(function() {  // tbody 내 tr 반복문 시작
            beforeTr = $(this).prev();  // 이전 tr

            if( beforeTr.length == 0 ) {  // 첫번째 tr 인 경우 : 이전 tr 없음

                groupNo = 0;  // 그룹번호는 0 부터 시작
                groupNoArray.push(groupNo); // 첫번째 tr 의 그룹번호 push

            } else {

                var sameGroupFlag = true;  // 이전 tr 과 비교하여 같은 그룹인지 여부 flag, 기본값 true

                for( var i in groupByColumnIndexArray ) {

                    var groupByColumnIndex = groupByColumnIndexArray[i];  // groupByColumns 로 전달된 각 column 의 index

                    // 이전 tr 과 현재 tr 비교하여 group by 기준 컬럼의 html 값이 하나라도 다른 경우 flag 값 false ( 같은 그룹 아님 )
                    if( $(this).children().eq(groupByColumnIndex).html() != $(beforeTr).children().eq(groupByColumnIndex).html() ) {
                        sameGroupFlag = false;
                    }

                }

                if( ! sameGroupFlag ) {  // 이전 tr 의 값과 비교하여 같은 그룹이 아닌 경우 : groupNo 1 증가시킴
                    groupNo++;
                }

                groupNoArray.push(groupNo); // 해당 tr 의 그룹번호 push

            }

        });  // tbody 내 tr 반복문 끝

        $(thRow).children('th').each(function (thIndex, th) {  // thead 의 th 반복문 시작 : table 내 각 컬럼별로 반복

            if( ! mergeColumns.includes( $(th).attr('data-field') ) ) {
                return true;   // mergeColumns 에 포함되지 않은 컬럼인 경우 continue
            }

            tdArray = [];  // 값 초기화
            beforeTd = null;
            rowspan = null;

            var colIdx = $("th", thRow).index(this);  // 해당 컬럼 index

            $('#' + gridId + ' > table > tbody > tr').each(function() {  // tbody 내 tr 반복문 시작

                var td = $(this).children().eq(colIdx);
                tdArray.push(td);

            });  // tbody 내 tr 반복문 끝

            for( var i in tdArray ) {  // 해당 컬럼의 td 배열 반복문 시작

                var td = tdArray[i];

                if ( i > 0 && groupNoArray[i-1] == groupNoArray[i] && $(td).html() == $(beforeTd).html() ) {

                    rowspan = $(beforeTd).attr("rowSpan");

                    if ( rowspan == null || rowspan == undefined ) {
                        $(beforeTd).attr("rowSpan", 1);
                        rowspan = $(beforeTd).attr("rowSpan");
                    }

                    rowspan = Number(rowspan) + 1;

                    $(beforeTd).attr("rowSpan",rowspan);
                    $(td).hide(); // .remove(); // do your action for the old cell here

                } else {

                    beforeTd = td;

                }

                beforeTd = ( beforeTd == null || beforeTd == undefined ) ? td : beforeTd; // set the that if not already set

            }  // 해당 컬럼의 td 배열 반복문 끝

        });  // thead 의 th 반복문 끝

    }

    // 현재 선택된 표준 카테고리 Select 값 중 가장 마지막 값을 반환
    function checkCategoryStandardId() {

        var categoryStandardIdArray = [ 'categoryStandardDepth1', 'categoryStandardDepth2', 'categoryStandardDepth3', 'categoryStandardDepth4' ];

        for (var i = categoryStandardIdArray.length - 1; i >= 0; i--) { // 카테고리 ID 배열을 역순으로 순환

            if( $('#' + categoryStandardIdArray[i]).val() && $('#' + categoryStandardIdArray[i]).val().trim() != '' ) {
                return $('#' + categoryStandardIdArray[i]).val();
            }

        }

        return '';

    }

	//엑셀 다운로드
	function fnExcelExport() {
		var data = $('#searchForm').formSerialize(true);

		if( $("input[name=masterItemType]:eq(0)").is(":checked") ) {  // 마스터 품목유형 '전체' 체크시 : 해당 파라미터 전송하지 않음
            data['masterItemType'] = '';
        }

        if( $("input[name=storageMethod]:eq(0)").is(":checked") ) {  // 보관방법 '전체' 체크시 : 해당 파라미터 전송하지 않음
            data['storageMethod'] = '';
        }

        // 표준 카테고리 : 현재 선택된 표준 카테고리 Select 값 중 가장 마지막 값을 조회조건에 추가
        data['ilCategoryStandardId'] = checkCategoryStandardId();

        //엑셀다운로드 양식을 위한 공통
		if( data["psExcelTemplateId"].length < 1 || data["psExcelTemplateId"] == "[]"){
    		fnKendoMessage({ message : "다운로드 양식을 선택해주세요."});
    		return false;
    	}

		fnExcelDownload('/admin/item/master/itemExportExcel', data);
	}

	function fnGoItemMgmPage(e) {  // 마스터 품목 등록 화면으로 이동
        var option = new Object();

        option.url = "#/itemMgm";   // 마스터 품목 등록 화면 URL
        option.menuId = 722;        // 마스터 품목 등록 화면 메뉴 ID

        fnGoPage(option);

	}

	function fnModify(e) {  // 마스터 품목 수정 화면으로 이동
		e.preventDefault();

		var dataItem = itemGrid.dataItem($(e.currentTarget).closest('tr'));
	    var option = new Object();

	    option.url = "#/itemMgmModify";  // 마스터 품목 수정 화면 URL
	    option.menuId = 789;             // 마스터 품목 수정 화면 메뉴 ID
	    option.data = {                  // 마스터 품목 수정 화면으로 전달할 Data
	            ilItemCode : dataItem.ilItemCode,
	            isErpItemLink : dataItem.erpLinkIfYn,
	            masterItemType : dataItem.itemType
	    };

	    fnGoPage(option);
	}

	function fnApprovalInfo(e) {
		e.preventDefault();

		fnKendoMessage({
            message : '개발예정입니다.',
            ok : function() {
                return;
            }
        });
	}

	function fnToggleSearchOption() {  // 상세검색 옵션 toggle

        // const formOffset = $("#searchForm").offset();
        const _scrollTop = $("#document").scrollTop();

	    if( $('tr[name=detailSearchRow]').is(':visible') ) {
            $('tr[name=detailSearchRow]').hide();
	        $('#fnToggleSearchOption').html('상세검색 ▼');

	    } else {
            $('#fnToggleSearchOption').html('상세검색 ▲');
            $('tr[name=detailSearchRow]').show();
            // formOffset.top < 0 ? $('#document').animate({scrollTop : formOffset.top - 30 }, 100) : "";
            _scrollTop >= 60 ? $('#document').animate({scrollTop : 0 }, 100) : "";

    		if (PG_SESSION != undefined && PG_SESSION != null
    				&& PG_SESSION.companyType != undefined && PG_SESSION.companyType != null && PG_SESSION.companyType == 'COMPANY_TYPE.CLIENT') {
    	        $('.headquaterView').hide();
    	        $('.clientView').show();
    		}
    		else {
    	        $('.headquaterView').show();
    	        $('.clientView').hide();
    		}
	    }

	}

	function toggleElement(mode) {
        const $targetEl = $('[data-include="' + mode + '"]');

        $("[data-include]").toggleDisableInTag(true);
        $targetEl.toggleDisableInTag(false);
    }


	// ------------------------------- Html 버튼 바인딩 Start
	// -------------------------------
	/** Common Search */
	$scope.fnSearch = function() {
		fnSearch();
	};

	/** Common Clear */
	$scope.fnClear = function() {
		fnClear();
	};

	$scope.fnSelect = function() {
		fnSelect();
	};

    $scope.fnGoItemMgmPage = function() {
        fnGoItemMgmPage();
    };

	$scope.fnExcelExport = function() {
		fnExcelExport();
	};

	$scope.fnToggleSearchOption = function() {
	    fnToggleSearchOption();
	};

	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END