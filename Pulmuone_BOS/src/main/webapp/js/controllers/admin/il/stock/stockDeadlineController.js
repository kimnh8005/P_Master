﻿﻿/**-----------------------------------------------------------------------------
 * description 		 : 출고 기준 관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.09		박영후          최초생성
 * @ 2020.08.10		강윤경          수정
 * **/
'use strict';


var PAGE_SIZE = 200;
var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
	fnInitialize();	// Initialize Page Call ---------------------------------

	// 기준 변경시
	$("input[name='stdType']").change(function(){
		$(".stdTypeName").text(this.value == 'percent' ? '%' : this.value);
		$("#imminent, #delivery").val('');
	});

	// Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'stockDeadline',
			callback : fnUI
		});

	}

	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	// Initialize Button ---------------------------------

		fnInitGrid();	// Initialize Grid ------------------------------------

		fnInitMaskTextBox();  // Initialize Input Mask
								// ------------------------------------

		fnInitOptionBox();// Initialize Option Box
							// ------------------------------------

	}

	// --------------------------------- Button
	// Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew,#fnSave,  #fnClear, #fnHistory').kendoButton();
	}
	// 검색
	function fnSearch(){

		var data = $('#searchForm').formSerialize(true);
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
	function fnClear(){
		$('#searchForm').formClear(true);

//		var warehouseDropdown = $("#searchUrWarehouseId").data("kendoDropDownList");
//		warehouseDropdown.dataSource.data([]);
//		warehouseDropdown.enable(false);

	}
	// 등록수정시 입력폼 초기화
	function fnInputClear(){
		$('#inputForm').formClear(true);
		$(".stdTypeName").text('일');
		$("#distributionPeriod").prop({"disabled":false, "required":true});
	}

	// 신규
	function fnNew(){
		fnInputClear();

		$(".inputClass").css("display", "");
		$(".updateClass").css("display", "none");
		var warehouseDropdown = $("#urWarehouseId").data("kendoDropDownList");
		warehouseDropdown.dataSource.data([]);
		warehouseDropdown.enable(false);

		$("#urSupplierId", "#urWarehouseId").attr("readonly", true);

        $("#popBasicYn input").prop("disabled", false);
		fnKendoInputPoup({height:"500px" ,width:"700px", title:{ nullMsg :'출고 기준 설정 등록' } });
	}
	// 저장
	function fnSave(){
		var data = $('#inputForm').formSerialize(true);

		var url = '/admin/il/stock/addStockDeadline';
		var cbId = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/il/stock/putStockDeadline';
			cbId= 'update';
		} else {
			data.ilStockDeadlineId = 0;
		}

		if( data.rtnValid ){
			// 유통기간 0 여부 체크
			if(parseInt($('#distributionPeriod').val()) == 0) {
				fnKendoMessage({message : '유통기간에 0을 입력할 수 없습니다.'});
				return;
			}

			/*
			// 기준이 %일 경우
			if($("#stdType").val() == 'percent') {
				if(parseInt($("#imminent").val()) > 100) {
					fnKendoMessage({message : '임박 기준이 100% 보다 클 수 없습니다.'});
					return;
				}
				if(parseInt($("#delivery").val()) > 100) {
					fnKendoMessage({message : '출고 기준이 100% 보다 클 수 없습니다.'});
					return;
				}
			}
			// 목표재고
			if(parseInt($("#targetStockRatio").val()) > 100) {
				fnKendoMessage({message : '목표재고가 100% 보다 클 수 없습니다.'});
				return;
			}
			*/

			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback(cbId, data);
					},
					isAction : 'batch'
			});
		}
	}

	// 닫기
	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}
	// 이력관리
	function fnHistory() {
		fnKendoPopup({
            id: 'stockDeadlineHist',
            title: "이력관리",
            // param: {},
            src: '#/stockDeadlineHist',
            width: '1200px',
            height: '600px',
            success: function(id, data) {
                // fnSearch();
            }
        });
	}



	// --------------------------------- Button
	// End---------------------------------


	// ------------------------------- Grid Start
	// -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : '/admin/il/stock/getStockDeadlineList',
			pageSize : PAGE_SIZE
		});


		aGridOpt = {
				dataSource: aGridDs
			,  	pageable  : {
				pageSizes: [200, 300, 500],
				buttonCount : 10
			}
			,	navigatable: true
// ,height:550
			,	columns   : [
				 { field:'no'				,title : 'No.'				, width:'50px'	,attributes:{ style:'text-align:center' }}
				,{ field:'compName'			,title : '공급업체'			, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'warehouseName'	,title : '출고처'				, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'distributionPeriod'	,title : '유통기간'		, width:'60px'	,attributes:{ style:'text-align:center' }}
				,{ field:'imminent'			,title : '임박기준'			, width:'100px'	,attributes:{ style:'text-align:center' }
					,template:  function(dataItem){
	                    return dataItem.imminent + "(" + dataItem.imminentPer + ")" ;
					}
				}
				,{ field:'delivery'			,title : '출고기준'			, width:'100px'	,attributes:{ style:'text-align:center' }
					,template:  function(dataItem){
	                    return dataItem.delivery + "(" + dataItem.deliveryPer + ")" ;
					}
				}
				,{ field:'targetStockRatio'	,title : '목표재고'			, width:'60px'	,attributes:{ style:'text-align:center' }
					,template:  function(dataItem){
	                    return dataItem.targetStockRatio + "%" ;
					}
				}
				,{ field:'basicYn'			,title : '기본설정'			, width:'60px'	,attributes:{ style:'text-align:center' }}
				,{ command: [{ name: "수정", click: fnPut
						, template: function(dataItem) {
							let btn = "";
							if(fnIsProgramAuth("SAVE")) {
								btn = "<a role=\"button\" class=\"k-button k-button-icontext k-grid-수정\">수정</a>"
							}

							return btn;
						}
					}]
					, title : '관리', width:'80px' 	,attributes:{ style:'text-align:center', class:'forbiz-cell-readonly' }
				}
			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
        aGrid.bind("dataBound", function() {
			//total count
            $('#countTotalSpan').text(aGridDs._total);
        });
	}


	// 수정
	function fnPut(e){
		fnInputClear();
		$(".inputClass").css("display", "none");
		$(".updateClass").css("display", "");

		var dataItem = this.dataItem($(e.currentTarget).closest("tr"));

		fnAjax({
			url     : '/admin/il/stock/getStockDeadline',
			params  : {ilStockDeadlineId : dataItem.ilStockDeadlineId},
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'select'
		});
	};
	// ------------------------------- Grid End -------------------------------

	// ---------------Initialize Input Mask Start
	// ------------------------------------------------
	function fnInitMaskTextBox() {
		$('#searchDistributionPeriod, #searchDistributionPeriodStart, #searchDistributionPeriodEnd, #searchImminent, #searchDelivery, #searchTargetStockRatio, #imminent, #delivery, #targetStockRatio, #distributionPeriod')
		.forbizMaskTextBox({fn:'onlyNum'});
	}
	// ---------------Initialize Input Mask End
	// ------------------------------------------------

	// ---------------Initialize Option Box Start
	// ------------------------------------------------
	function fnInitOptionBox(){
		// 공급업체
		fnKendoDropDownList({
			id    : 'searchUrSupplierId',
			url : "/admin/comn/getDropDownSupplierList",
			tagId : 'searchUrSupplierId',
			textField :"supplierName",
			valueField : "supplierId",
			blank : "전체"
		})
		.bind("change", onSupplierCompany)
			.dataSource.bind("requestEnd", function() {  // 팝업 공급업체 dropdown
				$("#urSupplierId").data("kendoDropDownList").setDataSource(this);// 데이타 설정
		});



		// 출고처
		fnKendoDropDownList({
			id    : 'searchUrWarehouseId',
			url : "/admin/comn/getDropDownWarehouseList",
			tagId : 'searchUrWarehouseId',
			chkVal: '',
			params : {'stockOrderYn' : 'Y'},
			blank : "전체",
			textField :"warehouseName",
			valueField : "warehouseId"
			/*
			, autoBind : false
			, cscdId     : 'searchUrSupplierId'
			, cscdField  : 'supplierId'
			*/
			, urlCallback : function(response) {
				response.data.rows.push({
					warehouseName: "미지정"
					, warehouseId: "0"
				});
			}
		});

		// 기본설정
		fnTagMkRadio({
			id    : 'basicYn',
			tagId : 'basicYn',
			chkVal: '',
			data  : [   { "CODE" : ""  , "NAME":'전체'  },
						{ "CODE" : "Y" , "NAME":'Y' },
						{ "CODE" : "N" , "NAME":'N'},
					],
			style : {}
		});

		// 팝업 - 기본설정
		fnTagMkRadio({
			id    : 'popBasicYn',
			tagId : 'popBasicYn',
			chkVal: '',
			data  : [
						{ "CODE" : "Y" , "NAME":'Y' },
						{ "CODE" : "N" , "NAME":'N'}
					],
			style : {}
		});


		/*
		// 공급업체
		fnKendoDropDownList({
			id    : 'searchUrSupplierId',
			url : "/admin/ur/urCompany/getSupplierCompanyList",
			tagId : 'searchApplyCompany',
			textField :"supplierName",
			valueField : "urSupplierId",
			blank : "전체"
		}).bind("change", onSupplierCompany)
			.dataSource.bind("requestEnd", function() {  // 팝업 공급업체 dropdown
															// 데이타 설정
				$("#urSupplierId").data("kendoDropDownList").setDataSource(this);
		});

		// 출고처
		fnKendoDropDownList({
			id    : 'searchUrWarehouseId',
			url : "/admin/ur/urCompany/getWarehouseList",
			tagId : 'searchUrWarehouseId',
			chkVal: '',
			style : {},
			blank : "전체",
			textField :"warehouseName",
			valueField : "urSupplierWarehouseId",
			autoBind : false,
			cscdId     : 'searchUrSupplierId',
			cscdField  : 'supplierCode'
		});*/

		// 팝업 공급업체 리스트 조회
		fnKendoDropDownList({
			id    : 'urSupplierId',
			tagId : 'urSupplierId',
			autoBind : false,
			textField :"supplierName",
			valueField : "supplierId",
			blank : "전체"
		});


		// 팝업 출고처
		fnKendoDropDownList({
			id    : 'urWarehouseId',
			url : "/admin/comn/getDropDownWarehouseList",
			tagId : 'urWarehouseId',
			chkVal: '',
			style : {},
			blank : "전체",
			textField :"warehouseName",
			valueField : "warehouseId",
			cscdId     : 'urSupplierId',
			cscdField  : 'supplierId',
			autoBind : false
		}).enable(false);

		/*
		// 팝업 출고처
		fnKendoDropDownList({
			id    : 'urWarehouseId',
			url : "/admin/ur/urCompany/getWarehouseList",
			tagId : 'urWarehouseId',
			chkVal: '',
			style : {},
			blank : "전체",
			textField :"warehouseName",
			valueField : "urSupplierWarehouseId",
			cscdId     : 'urSupplierId',
			cscdField  : 'supplierCode',
			autoBind : false
		}).enable(false);
*/

		// 임박기준 범위
		fnKendoDropDownList({
			id    : 'searchImminentBelow',
			data  : [
				{"CODE":"BELOW"	,"NAME":"이하"},
				{"CODE":"OVER"	,"NAME":"이상"}
			],
			textField :"NAME",
			valueField : "CODE",
			value : ""
		});


		// 출고기준 범위
		fnKendoDropDownList({
			id    : 'searchDeliveryBelow',
			data  : [
				{"CODE":"BELOW"	,"NAME":"이하"},
				{"CODE":"OVER"	,"NAME":"이상"}
			],
			textField :"NAME",
			valueField : "CODE",
			value : "ALL"
		});

		// 목표재고 범위
		fnKendoDropDownList({
			id    : 'searchTargetStockRatioBelow',
			data  : [
				{"CODE":"BELOW"	,"NAME":"이하"},
				{"CODE":"OVER"	,"NAME":"이상"}
			],
			textField :"NAME",
			valueField : "CODE",
			value : "ALL"
		});

		// 기준
		fnKendoDropDownList({
			id    : 'stdType',
			data  : [
				{"CODE":"일"			,"NAME":"일"},
				{"CODE":"percent"	,"NAME":"%"}
			],
			textField :"NAME",
			valueField : "CODE",
			value : "ALL"
		});


		// 조회용 유통기간
		$("#searchForm input[type=radio][name=searchExpireDateRadio]:eq(0)").on("click", function() {
			$("#searchDistributionPeriod").prop("disabled", false);
			$("#searchDistributionPeriodStart, #searchDistributionPeriodEnd").prop("disabled", true);
			$("#searchDistributionPeriodStart, #searchDistributionPeriodEnd").val("");
		});
		$("#searchForm input[type=radio][name=searchExpireDateRadio]:eq(1)").on("click", function() {
			$("#searchDistributionPeriod").prop("disabled", true);
			$("#searchDistributionPeriodStart, #searchDistributionPeriodEnd").prop("disabled", false);
			$("#searchDistributionPeriod").val("");
		});

		// 공급업체
		function onSupplierCompany(e) {
			var targetId = "";
			var sourceId = $(e.sender.element).attr("id");
			// 등록/수정일경우
			targetId = "urWarehouseId";
			/*
			if (sourceId == "urSupplierId")
				targetId = "urWarehouseId";
			else
				targetId = "searchUrWarehouseId";
*/
			var warehouseDropDown = $("#" + targetId).data("kendoDropDownList");

			if (e.sender.value() != "") {
				warehouseDropDown.enable(true);

	        	var dropDownUrl = warehouseDropDown.dataSource.options.transport.read.url;

	        	fnAjax({
	        		url     : dropDownUrl,
	        		method	: 'GET',
	        		params  : {"supplierId": e.sender.value()},
	        		success :
	        			function( data ){
	        			warehouseDropDown.dataSource.data(data.rows);
	        			},
	        		isAction : 'batch'
	        	});
	        }
	        else {
	        	warehouseDropDown.dataSource.data([]);
	        	warehouseDropDown.enable(false);
	        }
		}


		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});
	}
	// ---------------Initialize Option Box End
	// ------------------------------------------------
	// ------------------------------- Common Function start
	// -------------------------------

	/**
	 * 콜백합수
	 */
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				// form data binding
				$('#inputForm').bindingForm(data, 'stockDeadlineResultVo', true);

				if (data.stockDeadlineResultVo.popBasicYn == 'Y') {
		            $("#popBasicYn input").prop("disabled", true);
				}
				else {
		            $("#popBasicYn input").prop("disabled", false);
				}

				fnKendoInputPoup({height:"500px" ,width:"700px",title:{nullMsg :'출고 기준 설정 업데이트'} });
				break;
			case 'insert':
				fnSearch();
				$('#kendoPopup').data('kendoWindow').close();
				fnKendoMessage({message : '저장되었습니다.'});
				break;
			case 'update':
				aGridDs.query();
				fnKendoMessage({message : '수정되었습니다.'});
				fnClose();
				break;
		}
	}


	/*
	 * ? function valueCheck(key, nullMsg, ID){ fnKendoMessage({ message :
	 * fnGetLangData({ key : key, nullMsg : nullMsg}), ok : function
	 * focusValue(){ $('#'+ID).focus(); }}); return false; }
	 *
	 *
	 */
	// ------------------------------- Common Function end
	// -------------------------------


	// ------------------------------- Html 버튼 바인딩 Start
	// -------------------------------
	/** Common Search */
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear */
	$scope.fnClear =function(){	 fnClear();	};
	/** Common New */
	$scope.fnNew = function( ){	fnNew();};
	/** Common Save */
	$scope.fnSave = function(){	 fnSave();};
	/** Common Close */
	$scope.fnClose = function( ){  fnClose();};
	/** 이력관리 */
	$scope.fnHistory = function( ){  fnHistory();};
	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END
