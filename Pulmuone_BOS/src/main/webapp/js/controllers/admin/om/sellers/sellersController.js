/**-----------------------------------------------------------------------------
 * description 		 : 외부몰관리 > 외부몰관리 > 외부몰 리스트
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.12ㅌ6		이명수          최초생성
 * @
 * **/
"use strict";

	var PAGE_SIZE = 20;
	var viewModel, sellersGridDs, sellersGridOpt, sellersGrid;
	var sellerSuppliersGridDs, sellerSuppliersGridOpt, sellerSuppliersGrid;
	var pageParam = fnGetPageParam();
	$(document).ready(function() {
		importScript("/js/service/om/sellers/sellersFunction.js", function (){
			importScript("/js/service/om/sellers/sellersGridColumns.js", function (){
				importScript("/js/service/om/sellers/sellersSearch.js", function (){
					importScript("/js/service/od/order/orderCommSearch.js", function (){
						fnInitialize();
					});
				});
			});
		});

		//Initialize PageR
		function fnInitialize(){
			$scope.$emit("fnIsMenu", { flag : true });
			fnPageInfo({
				PG_ID  : "sellers",
				callback : fnUI
			});
	};

	// 화면 UI 초기화
	function fnUI(){
		fnInitButton();
		fnInitGrid();
		fnInitOptionBox();
		fnViewModelInit();
		fnDefaultSet();
		sellersSubmitUtil.search();
	};

	//--------------------------------- Button Start---------------------------------

	// 버튼 초기화
	function fnInitButton(){
		$("#fnSearch, #fnClear #fnNew").kendoButton();
		$("#fnFindSellerInfo").kendoButton();
	};

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid(){
		sellersGridDs = fnGetPagingDataSource({
			url      : "/admin/outmall/sellers/getSellersList",
			pageSize : PAGE_SIZE
		});

		sellersGridOpt = {
			dataSource: sellersGridDs,
			pageable  : { pageSizes: [20, 30, 50], buttonCount : 10 },
			navigatable : true,
			height : 755,
			scrollable : false,
			columns : sellersGridUtil.sellersList()
		};

		sellersGrid = $('#sellersGrid').initializeKendoGrid( sellersGridOpt ).cKendoGrid();
		$("#sellersGrid").on("click", "tbody>tr>td", function () {
			var index = $(this).index();

			//외부몰 상세 정보
			if($(this).closest('table').find('th').eq(index).text() == '판매처명'){
				sellersEventUtil.gridRowClick();
			}

			//공급업체 상세 정보
			if($(this).closest('table').find('th').eq(index).text() == '공급업체'){
				sellersEventUtil.sellerSupplierClick();
			}
		});
		sellersGrid.bind("dataBound", function() {
			$('#totalCnt').text(sellersGridDs._total);
		});


		//공급업체 상세 정보
		sellerSuppliersGridDs = new kendo.data.DataSource({
	        transport: {
	        	read : {
	                dataType : 'json',
	                type     : 'POST',
	                url: "/admin/outmall/sellers/getSellers",
                    beforeSend: function(req) {
                        req.setRequestHeader('authMenuID', CLICKED_MENU_ID);
                    }
	            }
	        },
	        schema: {
	        	data  : function(response) {
        			return response.data.rows.sellersSupplierList
	            }
	        }
		});
		sellerSuppliersGridOpt = {
			dataSource: sellerSuppliersGridDs
			,navigatable: true
			,height:300
			,scrollable: true
			,columns : sellersGridUtil.sellerSuppliersList()
		};
		sellerSuppliersGrid = $('#supplierGrid').initializeKendoGrid( sellerSuppliersGridOpt ).cKendoGrid();
	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------처------------------------

	// 옵션 초기화
	function fnInitOptionBox(){
		/* 검색 */
		sellersSearchUtil.searchTypeKeyword(); // 검색어
		searchCommonUtil.getDropDownCommCd("findSellersGroupCd", "NAME", "CODE", "전체", "SELLERS_GROUP", "Y", "", "", ""); // 판매처그룹
		searchCommonUtil.getDropDownCommCd("sellersoupCode", "NAME", "CODE", "선택해주세요.", "SELLERS_GROUP", "Y", "OUTMALL", "", ""); // 판매처그룹
		searchCommonUtil.getRadioLocalData("findInterfaceYn", "findInterfaceYn", "", sellersSearchUtil.searchInterfaceData(), null); // 연동여부
		searchCommonUtil.getRadioLocalData("findUseYn", "findUseYn", "", sellersSearchUtil.searchUseData(), null); // 사용여부

		searchCommonUtil.getDropDownCommCd("sellersGroupCode", "NAME", "CODE", "전체", "SELLERS_GROUP", "Y", "", "", ""); // 판매처그룹

		$("input[name=findSellersGroupCd]").each(function() {
			$(this).bind("change", onPurchaseTargetType2);
		});


		/* 데이터 */
		searchCommonUtil.getRadioLocalData("spanCalcType", "calcType", "S", sellersSearchUtil.searchCalcTypeData(), null); // 정산방식
		searchCommonUtil.getRadioLocalData("spanInterfaceYn", "interfaceYn", "Y", sellersSearchUtil.searchInterfaceYnData(), function(){
			$('#outmallCode').val('');
	    	$('#outmallName').val('');
	    	$('#outmallInfoView').val('');
		}); // 연동여부
		searchCommonUtil.getRadioLocalData("spanErpInterfaceYn", "erpInterfaceYn", "Y", sellersSearchUtil.searchInterfaceYnData(), null);	// 물류I/F 연동여
		searchCommonUtil.getRadioLocalData("spanUseYn", "useYn", "Y", sellersSearchUtil.searchUseYnData(), null); // 사용여부

		// 공급처
		searchCommonUtil.getSellersSupplier("supplierId", "supplierId", "선택해주세요.");


		// 팝업 초기화
		sellersPopupUtil.popupInit();

		// 정산 방식
		fnKendoDropDownList({ id : 'addCalcType', data : sellersSearchUtil.searchCalcTypeData(), textField :"NAME", valueField : "CODE", blank : "정산방식" });

		// 물류IF 연동구문
		fnKendoDropDownList({
			id : "gridErpInterfaceStatus",
			data : [
				{"CODE" : "Y", "NAME" : "정상I/F"},
				{"CODE" : "N", "NAME" : "I/F중지"}
			],
			valueField: "CODE",
			textField: "NAME",
			blank :"물류I/F 연동여부"
		});
	};

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

    // viewModel 초기화
    function fnViewModelInit(){

    };

    // 기본값 설정
    function fnDefaultSet(){

    };

	//-------------------------------  Common Function end -------------------------------
//------------------------------- Html 버튼 바인딩  Start -------------------------------
		/** Common Search*/
		$scope.fnSearch = function( ) {	sellersSubmitUtil.search();	};
		/** Common Clear*/
		$scope.fnClear =function(){	 sellersSubmitUtil.searchClear();	};
		/** Common New*/
		$scope.fnNew = function( ){	sellersSubmitUtil.new(); };
		/** Common Save*/
		$scope.fnSave = function(){	 sellersSubmitUtil.save(); };
		/** Common Close*/
		$scope.fnClose = function( ){  sellersEventUtil.close(); };
		/** 물류I/F change UPDATE*/
		$scope.fnSelectModification = function( ){  sellersEventUtil.selectModification(); };

		$scope.excelDownload = function( ){
			var data = $('#searchForm').formSerialize(true);

			fnExcelDownload('/admin/outmall/sellers/getSellersExcelList', data);

		};

		$scope.fnDeliveryPatternPopupButton = function( ){  fnDeliveryPatternPopupButton();};

		$scope.fnDawnDeliveryPatternPopupButton = function( ){  fnDawnDeliveryPatternPopupButton();};

		$scope.fnFindOutmallInfo = function(){	sellersEZAdminPopupUtil.fnFindOutmallInfo();};


		//마스터코드값 입력제한 - 숫자 & -
		fnInputValidationByRegexp("accountTelephone1", /[^0-9]/g);
		fnInputValidationByRegexp("accountTelephone2", /[^0-9]/g);
		fnInputValidationByRegexp("accountTelephone3", /[^0-9]/g);
		fnInputValidationByRegexp("receiverZipCode", /[^0-9]/g);
		fnInputValidationByRegexp("limitCount", /[^0-9]/g);
		//fnInputValidationLimitSpecialCharacter("inputWarehouseName");
		fnInputValidationByRegexp("warehouseTelephone1", /[^0-9]/g);
		fnInputValidationByRegexp("warehouseTelephone2", /[^0-9]/g);
		fnInputValidationByRegexp("warehouseTelephone3", /[^0-9]/g);
		fnInputValidationByRegexp("addFee", /[^0-9]/g);


		// 입력가능 : 한글 & 영어(대,소) & 숫자 & 특수문자  []~!@#$%^&*()_+|<>?:{}
		fnInputValidationByRegexp("inputWarehouseName", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);


		//------------------------------- Html 버튼 바인딩  End -------------------------------
}); // document ready - END
