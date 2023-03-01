/*******************************************************************************************************************************************************************************************************
 * --------------- description : 마스터 품목 관리 - 마스터 품목 검색 팝업 @ ----------------------------
 ******************************************************************************************************************************************************************************************************/
'use strict';

var PAGE_SIZE = 20;
var result = new Array;
var chkArr = new Array;
var itemGridOpt, itemGrid, itemGridDs;

var popupParameter = parent.POP_PARAM["parameter"]; // 팝업 파라미터 전역변수
var masterItemType = popupParameter['masterItemType']; // 모화면 ( 마스터 품목 등록 ) 의 마스터 품목 유형
var storageMethodCode = popupParameter['bosStorageMethod']; // 모화면 ( 마스터 품목 등록 ) 의 보관방법

$(document).ready(function() {
	// Initialize Page Call
	fnInitialize();

	// Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', {
			flag : false
		});

		fnPageInfo({
			PG_ID : 'item',
			callback : fnUI
		});
	};

	//전체화면 구성
	function fnUI() {
		fnTranslate();			// comm.lang.js 안에 있는 공통함수 다국어
		fnInitButton();			// Initialize Button\
		fnInitItemCtgry();		// 마스터 품목 검색조건 Initialize
		fnItemGrid();			// 품목 Grid

		//탭 변경 이벤트
        fbTabChange();

	    fnToggleSearchOption();   // 상세검색 옵션 toggle : 최초 조회시 상세검색 닫힘

	};

	function fnInitButton() {
		$('#fnSearch , #fnClear , #fnClose').kendoButton();
	};

	//검색
	function fnSearch() {

		if($("input[name=selectConditionType]:checked").val() == "multiSection") {
			if($('#ilItemCode').val() != "") {
				$("#ilItemCode").val("");
			}
		}

		// 기간 조건검색 시 공백 유효성 검사
		if (dateBlankChk() == false)
			return;

		var data = $('#searchForm').formSerialize(true);

		// 마스터 품목 검색 팝업에서는 정상등록 품목만 검색
		data['itemStatusTp'] = 'ITEM_STATUS_TP.REGISTER';

		// 표준 카테고리 : 현재 선택된 표준 카테고리 Select 값 중 가장 마지막 값을 조회조건에 추가
		data['ilCategoryStandardId'] = checkCategoryStandardId();

        if( $("input[name=storageMethod]:eq(0)").is(":checked") ) {  // 보관방법 '전체' 체크시 : 해당 파라미터 전송하지 않음
            data['storageMethod'] = '';
        }

		var query = {
			page : 1,
			pageSize : PAGE_SIZE,
			filterLength : fnSearchData(data).length,
			filter : {
				filters : fnSearchData(data)
			}
		};

		itemGridDs.query(query);
	};

	//검색조건 초기화
	function fnClear() {
		$('#searchForm').formClear(true);

        // 모화면 ( 마스터 품목 등록 ) 에서 선택한 품목 유형만 노출 및 선택상태 고정
        $('input[name=masterItemType]').each(function(idx) {

            if( $(this).val() == masterItemType ) {
                $(this).prop('checked', true);
            }

            $(this).prop('disabled', true); // 모두 비활성화

        });

        // 최초 조회시에는 보관방법 '전체' 체크
        $('input[name=storageMethod]').each(function(idx, element) {
            $(element).prop('checked', true);
        });

        $('[data-id="fnDateBtnC"]').mousedown();

	};

	//검색조건 관련 radio, dropdown, checkbox 구성
	function fnInitItemCtgry() {

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
	          }]
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
			format : 'yyyy-MM-dd',
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
            url : "/admin/comn/getDropDownSupplierList",
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
            blank : "출고처 그룹 선택",
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
            cscdId     : "warehouseGroup",
            cscdField  : "warehouseGroupCode"
        });

        $('#urBrandId').attr("disabled", true); // 브랜드 목록 비활성화

        fnKendoDropDownList({
            id : 'urBrandId', // 최초 조회시 브랜드 목록 없음 : 공급업체 선택시 해당 공급업체 ID 로 조회
            data : {},
            valueField : 'code',
            textField : 'name',
            blank : '전체'
        });

        fnKendoDropDownList({
            id : 'dpBrandId', 	// 전시 브랜드
            url : "/admin/ur/brand/searchDisplayBrandList",
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

		// 마스터 품목 유형
		fnTagMkChkBox({
			id			: 'masterItemType',
			url			: "/admin/comn/getCodeList",
			tagId		: 'masterItemType',
			autoBind	: true,
			async		: false,
			valueField	: 'CODE',
			textField 	: 'NAME',
			style		: {},
			params		: {"stCommonCodeMasterCode" : "MASTER_ITEM_TYPE", "useYn" :"Y"},
		});

        // 모화면 ( 마스터 품목 등록 ) 에서 선택한 품목 유형만 노출 및 선택상태 고정
        $('input[name=masterItemType]').each(function(idx) {

            if( $(this).val() == masterItemType ) {
                $(this).prop('checked', true);
            }

            $(this).prop('disabled', true); // 모두 비활성화

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

                storageMethodCode = storageMethodCode ? storageMethodCode : '';

                fnTagMkChkBox({
                    id    : 'storageMethod',
                    data  : data['rows'],
                    tagId : 'storageMethod',
                    chkVal: storageMethodCode,
                    style : {}
                });

                $('#storageMethod').bind("change", onCheckboxWithTotalChange)

                // 최초 조회시에는 보관방법 '전체' 체크
                $('input[name=storageMethod]').each(function(idx, element) {
                    $(element).prop('checked', true);
                });

            },

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

	//공급업체 변경시 브랜드 Dropdown 갱신
	function onUrSupplierIdChange(e) {

		if ( e.sender.value() ) {

            $('#urBrandId').attr("disabled", false); // 비활성화 해제

            /* 변경한 공급업체 ID 로 브랜드 리스트 호출 */
            fnKendoDropDownList({
                id : 'urBrandId',
                url : "/admin/ur/brand/searchBrandList",
                params : {
                    searchUrSupplierId : e.sender.value() // 변경한 공급업체 ID 값
                },
                textField : "brandName",
                valueField : "urBrandId",
                blank : '전체'
            });

		} else {

            $('#urBrandId').attr("disabled", true); // 비활성화

	        fnKendoDropDownList({
	            id : 'urBrandId', // 최초 조회시 브랜드 목록 없음 : 공급업체 선택시 해당 공급업체 ID 로 조회
	            data : {},
	            valueField : 'code',
	            textField : 'name',
	            blank : '전체'
	        });


		}
	}

	//마스터 품목 리스트 Grid
	function fnItemGrid() {

		itemGridDs = fnGetEditPagingDataSource({
			url : '/admin/item/master/getItemList',
			pageSize : PAGE_SIZE
		});

		itemGridOpt = {
			dataSource : itemGridDs,
			pageable : {
				pageSizes : [ 20, 30, 50 ],
				buttonCount : 5
			},
            editable : false,
            navigatable: true,
			height : 500,
			scrollable : true,
			columns : [
				{ title : 'No', width:'70px', attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>", locked: false, lockable: false }
			,	{ command: [{
						className : "k-grid-customCommand"
					,	name : "masterSearchBtn"
					,	text: "선택"
					,	click: function(e){
							e.preventDefault(); // prevent page scroll position change
							var tr = $(e.target).closest("tr"); // get the current table row (tr)
							var data = this.dataItem(tr); // get the data bound to the current table row

							fnCopyMaster(data);
						}
					}], title: '관리', width: "90px", attributes:{ style:'text-align:center'  , class:'forbiz-cell-readonly' }, locked: false}
			,   { field : 'ilItemCodeBarcode',  title : '품목코드/<br>품목바코드', width : '175px', attributes : { style : 'text-align:center' }, locked: false,
                  template: function(dataItem) {
                      return dataItem['ilItemCode'] + "<br>" + dataItem['itemBarcode'];
                  }
              }
			,	{ field : 'ilItemCode', width: "0px", title : '품목코드', attributes : { style : 'text-align:center' }, hidden:true, locked: false }
			,	{ field : 'itemName', title : 'ERP 품목명/<br>마스터 품목명', width: "250px", attributes : { style : 'text-align:left' }, locked: false, lockable: false }
			,	{ field : 'supplierName', title : '공급업체', width: "135px",  attributes : { style : 'text-align:center' } }
			,	{ field : 'warehouseName', title : '출고지', width : '225px', attributes : { style : 'text-align:center' },
                  template: function(dataItem) {
                      return ( dataItem['warehouseAddress'] ? dataItem['warehouseAddress'] + ( dataItem['warehouseName'] ? '<br>' + dataItem['warehouseName'] : '' ) : '' );
                  }
			    }
			,	{ field : 'poTypeName', title : '발주구분', width : '100px', attributes : { style : 'text-align:center' } }
			,	{ field : '', title : '재고', width : '100px', attributes : { style : 'text-align:center' } }
			,	{ field : 'brandName', title : '표준 브랜드', width : '150px', attributes : { style : 'text-align:center' } }
			,	{ field : 'dpBrandName', title : '전시 브랜드', width : '150px', attributes : { style : 'text-align:center' } }
			,	{ field : 'storageMethodName', title : '보관<br>방법', width : '80px', attributes : { style : 'text-align:center' } }
			,	{ field : 'standardCategoryFullName', title : '표준카테고리', width : '250px', attributes : { style : 'text-align:left' },
                  template: function(dataItem) {
                      return dataItem['standardCategoryFullName'];
                  }
			    }
			]
		};

		itemGrid = $('#itemGrid').initializeKendoGrid(itemGridOpt).cKendoGrid();

        itemGrid.bind("dataBound", function() {

            var row_num = itemGridDs._total - ( ( itemGridDs._page - 1 ) * itemGridDs._pageSize );

            $("#itemGrid tbody > tr .row-number").each(function(index){
                $(this).html(row_num);
                row_num--;
            });

            $("#pageTotalText").text( kendo.toString( itemGridDs._total, "n0") );

        });

		$("#itemGrid .k-grid-content").css({
		    "overflow-y": "auto"
		});

	};

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

	function fnCopyMaster(params){
	    if (params) {
			parent.POP_PARAM = params;
		}
        parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	}

	function fnToggleSearchOption() {  // 상세검색 옵션 toggle
			$('tr[name=detailSearchRow]').toggle();

			if( $('tr[name=detailSearchRow]').is(':visible') ) {
					$('#fnToggleSearchOption').html('상세검색 ▲');
					setGridHeightBigger(false);
			} else {
					$('#fnToggleSearchOption').html('상세검색 ▼');
					setGridHeightBigger(true);
			}
	}

	function setGridHeightBigger(toggle) {
		const $grid = $("#itemGrid");
		const $gridContent = $("#itemGrid > .k-grid-content");

		const gridHeight = $grid.height();
		const contentHeight = $gridContent.height();

		//상세검색 접힐떄
		if(toggle) {
			$grid.height(500);
			$gridContent.height(385);
		} else {
			$grid.height(gridHeight - 100);
			$gridContent.height(contentHeight - 100);
		}
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
	$scope.fnClose = function() {
		fnClose();
	};
	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

    $scope.fnToggleSearchOption = function() {
        fnToggleSearchOption();
    };

}); // document ready - END
