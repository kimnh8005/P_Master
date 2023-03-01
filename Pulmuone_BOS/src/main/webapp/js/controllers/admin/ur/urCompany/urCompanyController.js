/**-----------------------------------------------------------------------------
 * description 		 : 프로그램관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.7.01		안치열          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var selectedWarehouseId = [];
var deletedWarehouseId = [];

var selectedSellersId = [];
var deletedSellersId = [];

var viewModel, inputViewModel;
var supplierCompanyFlag = 'N';

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------


	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'urCompany',
			callback : fnUI
		});

	}


	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnViewModelInit();

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnWarehouseView();

		fnSearch();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew, #fnSave, #fnClear, #fnClose, #fnAddSeller, #fnAddWarehouse').kendoButton();
	}
	function fnSearch(){
		$('#inputForm').formClear(false);
		var data;
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
	function fnClear(){
		$('#searchForm').formClear(true);
		$("span#useYn input:radio").eq(0).click();
		$("span#tpCode input:radio").eq(0).click();
	}
	function fnNew(){
		$('#inputForm').formClear(true);
		$("input:radio[name='inputTpCode']").attr('disabled',false);
		$('input:radio[name="inputTpCode"]:input[value="CLIENT_TYPE.CLIENT"]').prop("checked", true);
		$('input:radio[name="inputUseYn"]:input[value="Y"]').prop("checked", true);
		$('#clientCompanyDiv').show();
		$('#clientWarehouseDiv').show();
		$('#shopDiv').hide();
//		$('#vendorChannelDiv').hide();
		$('#vendorErpDiv').hide();
		$('#telBaseView').show();
		$('#telShopView').hide();
		$("#supplierCompany").data("kendoDropDownList").enable( true );
		$("#warehousePopup").attr("disabled", false);
		$("#store").data("kendoDropDownList").enable( true );

		$('#createInfoDiv').hide();
		$('#modifyInfoDiv').hide();

		$(".addedWarehouse-conatiner__list__item").remove();
		$(".addedSeller-conatiner__list__item").remove();
		$('#addedWarehouseList').empty();
		$('#addedSellerList').empty();
		selectedWarehouseId = [];
		selectedSellersId = [];

		fnWarehouseView();

		fnKendoInputPoup({height:"900px" ,width:"800px", title:{ nullMsg :'거래처 등록' } });
	}

	function fnSave(){

		var url  = '/admin/ur/urCompany/addClient';
		var cbId = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/ur/urCompany/putClient';
			cbId= 'update';
		}

		if($('#inputTpCode').getRadioVal() == "CLIENT_TYPE.CLIENT"){
			$('#store').val('_');
			$('#channelCode').val('_');
			$('#erpCode').val('_');
			$('#shopTelephone1').val('_');
			$('#shopTelephone2').val('_');
			$('#shopTelephone3').val('_');
		}else if($('#inputTpCode').getRadioVal() == "CLIENT_TYPE.SHOP"){
			$('#warehouseName').val('_');
			$('#channelCode').val('_');
			$('#erpCode').val('_');


			var storeId = $('#store').val();
            var result = storeId.split("_");
            $('#store').val(result[0]);

		}else{
			$('#supplierCompany').val('_');
			$('#store').val('_');
			$('#warehouseName').val('_');
			$('#shopTelephone1').val('_');
			$('#shopTelephone2').val('_');
			$('#shopTelephone3').val('_');
		}

		if(selectedWarehouseId.length > 0){
			$('#warehouseName').val('_');
		}

		var data = $('#inputForm').formSerialize(true);

		if(selectedWarehouseId.length > 0){
			$('#warehouseName').val('');
			data.warehouseName = '';
			data.urSupplierWarehouse = '';
		}


		for(let i = 0; i < selectedWarehouseId.length; i++) {
			if(data.urSupplierWarehouse == ''){
				data.urSupplierWarehouse = selectedWarehouseId[i];
			}else{
				data.urSupplierWarehouse = data.urSupplierWarehouse + '∀' + selectedWarehouseId[i];
			}
		}


		if($('#inputTpCode').getRadioVal() == "CLIENT_TYPE.CLIENT" && selectedWarehouseId.length == 0){
			fnKendoMessage({message : '출고처를 선택해주세요'});
            return;
		}

		if($('#inputTpCode').getRadioVal() == "CLIENT_TYPE.VENDOR" && selectedSellersId.length == 0){
			fnKendoMessage({message : '외부몰을 선택해주세요'});
            return;
		}

		data.omSellersId = '';
		var sellerId;
		for(let j = 0; j< selectedSellersId.length ; j++){
			if(data.omSellersId == ''){
				data.omSellersId = selectedSellersId[j];
			}else{
				data.omSellersId = data.omSellersId + '∀' + selectedSellersId[j];
			}
		}


		var inputTypeCode = $('#inputTpCode').getRadioVal();
		if(data.rtnValid == false){

			if($('#store').val() == '_'){
				$('#store').val('');
			}
			if($('#channelCode').val() == '_'){
				$('#channelCode').val('');
			}
			if($('#erpCode').val() == '_'){
				$('#erpCode').val('');
			}
			if($('#shopTelephone1').val() == '_'){
				$('#shopTelephone1').val('');
			}
			if($('#shopTelephone2').val() == '_'){
				$('#shopTelephone2').val('');
			}
			if($('#shopTelephone3').val() == '_'){
				$('#shopTelephone3').val('');
			}
			if($('#warehouseName').val() == '_'){
				$('#warehouseName').val('');
			}
			if($('#channelCode').val() == '_'){
				$('#channelCode').val('');
			}
			if($('#erpCode').val() == '_'){
				$('#erpCode').val('');
			}
			if($('#supplierCompany').val() == '_'){
				$('#supplierCompany').val('');
			}
			if($('#store').val() == '_'){
				$('#store').val('');
			}
		}

		if($('#inputTpCode').getRadioVal() == "CLIENT_TYPE.CLIENT"){
			data.store = '';
			data.channelCode = '';
			data.erpCode = '';
			data.shopTelephone1 = '';
			data.shopTelephone2 = '';
			data.shopTelephone3 = '';
		}else if($('#inputTpCode').getRadioVal() == "CLIENT_TYPE.SHOP"){
			data.warehouseName = '';
			data.channelCode = '';
			data.erpCode = '';
		}else{
			data.supplierCompany = '';
			data.store = '';
			data.warehouseName ='';
			data.shopTelephone1 = '';
			data.shopTelephone2 = '';
			data.shopTelephone3 = '';
		}

//		if($("#deliveryStatChgYn").prop("checked")){
//			data.deliveryStatChgYn= 'Y';
//		}else{
//			data.deliveryStatChgYn= 'N';
//		}
		if( data.rtnValid ){
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

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	function fnWarehouseView(){
//		$("#warehouseGroup").attr("disabled", true);
		$("#warehouseGroup").data("kendoDropDownList").enable(false);
		$("#warehouseId").data("kendoDropDownList").enable(false);
		viewModel.searchInfo.set("warehouseGroup", "");
		viewModel.searchInfo.set("warehouseId", "");
		viewModel.fnWareHouseGroupChange();
		viewModel.fnSupplierCompanyChange();
		$("#fnAddWarehouse").attr("disabled", true);
	}


	function fnViewModelInit() {
		viewModel = new kendo.data.ObservableObject({
            searchInfo : { // 조회조건
            	inputSellersGroup			: "",
            	inputSellersDetail			: "",
            	warehouseGroup				: "",
            	warehouseId					: "",
            	supplierCompany				: ""
            },
            fnDateChange : function(e, id){
    			e.preventDefault();

    		},
    		fnWareHouseGroupChange : function() {
            	fnAjax({
//                    url     : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
                    url     : "/admin/comn/getWarehouseGroupByWarehouseList",
                    method : "GET",
                    params : { "warehouseGroupCode" : viewModel.searchInfo.get("warehouseGroup"), "supplierCompany" : viewModel.searchInfo.get("supplierCompany") },

                    success : function( data ){
                        let warehouseId = $("#warehouseId").data("kendoDropDownList");
                        warehouseId.setDataSource(data.rows);

                    },
                    error : function(xhr, status, strError){
                        fnKendoMessage({ message : xhr.responseText });
                    },
                    isAction : "select"
                });
            },
            fnSupplierCompanyChange : function() {
            	fnAjax({
                  url     : "/admin/comn/getWarehouseGroupByWarehouseList",
                  method : "GET",
                  params : { "supplierCompany" : viewModel.searchInfo.get("supplierCompany")},

                  success : function( data ){
                      let warehouseId = $("#warehouseId").data("kendoDropDownList");
                      warehouseId.setDataSource(data.rows);
                  },
                  error : function(xhr, status, strError){
                      fnKendoMessage({ message : xhr.responseText });
                  },
                  isAction : "select"
              });
            	fnAjax({
                    url     : "/admin/comn/getSupplierCompanyByWarehouseList",
                    method : "GET",
                    params : { "supplierCompany" : viewModel.searchInfo.get("supplierCompany")},

                    success : function( data ){
                        let warehouseGroup = $("#warehouseGroup").data("kendoDropDownList");
                        warehouseGroup.setDataSource(data.rows);

                        // 공급업체 변경 시 출고처 선택 항목 초기화
                        if(supplierCompanyFlag == 'N'){
                            $('#addedWarehouseList').empty();
                            selectedWarehouseId = [];
                        }
                    },
                    error : function(xhr, status, strError){
                        fnKendoMessage({ message : xhr.responseText });
                    },
                    isAction : "select"
                });
          }

        });


		kendo.bind($("#inputForm"), viewModel);

		inputViewModel = new kendo.data.ObservableObject({
            inputInfo : { // 조회조건
           		inputSellersGroup			: "",
           		inputSellersDetail			: ""
            },
         	fnDateChange2 : function(e, id){
    			e.preventDefault();
         	}

        });

//        kendo.bind($("#inputForm"), inputViewModel);
	};

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : "/admin/ur/urCompany/getCompanyList",
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 5
			}
			,navigatable: true
			// ,scrollable: true
			// ,height:550
			,columns   : [
				 { title : 'No'	, width:'50px',attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
				,{ field:'clientName'	,title : '거래처명' ,attributes:{ style:'text-align:center' }}
				,{ field:'clientTypeName'	,title : '타입'		, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'supplyCompName'			,title : '공급업체(외부몰)'		, width:'300px',attributes:{ style:'text-align:center' },
					template : function(dataItem){
						var temp ="";
						if(dataItem.clientTp == 'CLIENT_TYPE.VENDOR'){
							var supplyCompName =dataItem.supplyCompName;
							if(supplyCompName != null){
								var result = supplyCompName.split(",");
								for(var i=0; i<result.length; i++){
									temp += result[i]+"<br>";
								}
							}
						}else{
							temp = dataItem.supplyCompName;
						}
						return temp;
					}
				}
				,{ field:'useYn'		,title : '사용여부'		, width:'150px',attributes:{ style:'text-align:center' }
				,template : "#=(useYn=='Y')?fnGetLangData({nullMsg :'예' }):fnGetLangData({nullMsg :'아니오' })#"}
				,{ field:'createDate'		,title : '등록일자'		, width:'200px',attributes:{ style:'text-align:center' }}
				,{ field:'urClientId', hidden:true}
				,{ field:'urCompanyId', hidden:true}
				,{ field:'clientTp', hidden:true}
				,   { command: [{ text: '수정', click: fnPutSupplier, className:'btn-s btn-white',
									click: function(e) {

						            e.preventDefault();
						            var tr = $(e.target).closest("tr"); // get the current table row (tr)
						            var data = this.dataItem(tr);

						            fnPutSupplier(data.urClientId, data.clientTp, data.urCompanyId);
						        }}]
					, title: '관리', width: "150px", attributes:{ style:'text-align:center'  , class:'forbiz-cell-readonly #:#' }}   // EXECUTE_TYPE 별 버튼 제어 처리 확인 필요

			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
			$("#aGrid").on("click", "tbody>tr", function () {
				//fnGridClick();
		});

		aGrid.bind("dataBound", function() {
			//row number
			var row_number = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(){
				$(this).html(row_number);
				row_number--;
			});

			//total count
            $('#countTotalSpan').text(aGridDs._total);
        });
	}
	function fnGridClick(){
		var aMap = aGrid.dataItem(aGrid.select());
		fnAjax({
			url     : '/admin/ur/urCompany/getSupplierCompany',
			params  : {urClientId : aMap.urClientId},
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'select'
		});
	};

	function fnPutSupplier(urClientId, clientTp, urCompanyId){

		$('#inputForm').formClear(true);
		fnAjax({
			url     : '/admin/ur/urCompany/getClient',
			params  : {urClientId : urClientId, clientTp: clientTp, urCompanyId : urCompanyId},
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'select'
		});

	}

	// 외부몰 추가
	function fnAddSeller(){
		addSellerItem();
	}

	// 출고처 추가
	function fnAddWarehouse(){

		const warehouseId  = $('#warehouseId').data('kendoDropDownList').value();
		const warehouseName   = $('#warehouseId').data('kendoDropDownList').text();

		addWareHouseItem(warehouseId, warehouseName);
	}


	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

		fnTagMkRadio({
			id    :  'useYn',
			tagId : 'useYn',
			chkVal: '',
			data  : [   { "CODE" : ""	, "NAME":'전체' },
						{ "CODE" : "Y"	, "NAME":'예' },
						{ "CODE" : "N"	, "NAME":'아니오' }
					],
			style : {}
		});

		fnTagMkRadio({
			id    :  'inputUseYn',
			tagId : 'inputUseYn',
			chkVal: 'Y',
			data  : [
						{ "CODE" : "Y"	, "NAME":'예' },
						{ "CODE" : "N"	, "NAME":'아니오' }
					],
			style : {}
		});

		fnTagMkRadioYN({id: "intputActive" , tagId : "useYn",chkVal: 'Y'});


		fnTagMkRadio({
			id    : 'tpCode',
			tagId : 'tpCode',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "CLIENT_TYPE", "useYn" :"Y"},
			async : false,
			beforeData : [
							{"CODE":"", "NAME":"전체"},
						],
			chkVal: '',
			style : {}
		});


		fnTagMkRadio({
			id    : 'inputTpCode',
			tagId : 'inputTpCode',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "CLIENT_TYPE", "useYn" :"Y"},
			async : false,
			chkVal: 'CLIENT_TYPE.CLIENT',
			style : {},
			change : function(e){
				if($('#inputTpCode').getRadioVal() == "CLIENT_TYPE.CLIENT"){

					$("#supplierCompany").data('kendoDropDownList').value("");

					$('#clientCompanyDiv').show();
					$('#clientWarehouseDiv').show();
					$('#shopDiv').hide();
					$('#vendorErpDiv').hide();
					$('#telBaseView').show();
					$('#telShopView').hide();
					$('input:radio[name="inputUseYn"]:input[value="Y"]').prop("checked", true);

					$(".addedWarehouse-conatiner__list__item").remove();
					$(".addedSeller-conatiner__list__item").remove();
					fnWarehouseView();
					fnInputViewInit();

					kendo.bind($("#inputForm"), viewModel);

				}else if($('#inputTpCode').getRadioVal() == "CLIENT_TYPE.SHOP"){

					$("#supplierCompany").data('kendoDropDownList').value("");

					$('#clientCompanyDiv').show();
					$('#clientWarehouseDiv').hide();
					$('#shopDiv').show();
					$('#vendorErpDiv').hide();
					$("#store").kendoDropDownList({enable: true});
					$('#telBaseView').hide();
					$('#telShopView').show();
					$('input:radio[name="inputUseYn"]:input[value="Y"]').prop("checked", true);

				}else{

					$('#clientCompanyDiv').hide();
					$('#clientWarehouseDiv').hide();
					$('#shopDiv').hide();
					$('#vendorErpDiv').show();
					$('#telBaseView').show();
					$('#telShopView').hide();
					$('input:radio[name="inputUseYn"]:input[value="Y"]').prop("checked", true);

					$(".addedWarehouse-conatiner__list__item").remove();
					$(".addedSeller-conatiner__list__item").remove();

					fnViewInit();
					kendo.bind($("#inputForm"), inputViewModel);

				}

            }

		});



		fnKendoDropDownList({
			id    : 'companyType',
			tagId : 'companyType',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "ACCOUNT_SEARCH_TYPE", "useYn" :"Y"},
			textField :"NAME",
			valueField : "CODE",
			value : "NAME"
		});

		// 공급업체 리스트 조회
		fnKendoDropDownList({
			id    : 'supplierCompany',
			url : "/admin/ur/urCompany/getSupplierCompanyList",
			params : {},
			textField :"supplierName",
			valueField : "urSupplierId",
			blank : "선택해주세요"
		});

		$('#supplierCompany').unbind('change').on('change', function(){
			var shopDropDownList =$('#supplierCompany').data('kendoDropDownList');
			$('#warehouseId').val('');
			$('#warehouseName').val('');

			$("#warehouseGroup").data("kendoDropDownList").enable(true);
			$("#warehouseId").data("kendoDropDownList").enable(true);
			$("#fnAddWarehouse").attr("disabled", false);



			// 매장 리스트 조회
            fnKendoDropDownList({
                id    : 'store',
                url : "/admin/ur/urCompany/getStoreList",
                params : {"urSupplierId" : $('#supplierCompany').val()},
                textField :"storeName",
                valueField : "storeId",
                blank : "선택해주세요",
                enable: false

            });

		});


//        $('#store').unbind('change').on('change', function(){
//            let storeId = $('#storeId').val();
//
//            var result = storeId.split("_");
//            for(var i=0; i<result.length; i++){
//                if(i==0){
//                    $('#storeId').val(result.get[0]);
//                }
//            }
//        });

        $('#store').unbind('change').on('change', function(){

            var storeId = $('#store').val();
            var result = storeId.split("_");
            $('#store').val(result[0]);

            if(result[1]!=null){
                var telResult = result[1].split("-");

                $('#shopTelephone1').val(telResult[0]);
                $('#shopTelephone2').val(telResult[1]);
                $('#shopTelephone3').val(telResult[2]);
            }

        });


        // 매장 리스트 조회
        fnKendoDropDownList({
            id    : 'store',
            url : "/admin/ur/urCompany/getStoreList",
            textField :"storeName",
            valueField : "storeId",
            blank : "선택해주세요",
            enable: false

        });




		// 판매처 그룹
		fnKendoDropDownList({
            id : "inputSellersGroup",
            tagId : "inputSellersGroup",
            url : "/admin/comn/getCodeList",
            params : { "stCommonCodeMasterCode" : "SELLERS_GROUP", "useYn" : "Y", "attr1" : "OUTMALL" },
            blank : "외부몰그룹 전체",
            async : false
        }).bind("change", fnSellerGroupChange);

		// 판매처
        fnKendoDropDownList({
            id : "inputSellersDetail",
            tagId : "inputSellersDetail",
            url : "/admin/comn/getDropDownSellersGroupBySellersList",
            textField :"sellersNm",
            valueField : "omSellersId",
            blank : "외부몰 전체",
            params : { "sellersGroupCd" : "T" },
        });

        // 주문상태 알림 수신여부
        fnTagMkRadio({
			id    :  'orderAlarmRevYn',
			tagId : 'orderAlarmRevYn',
			chkVal: 'Y',
			data  : [
						{ "CODE" : "N"	, "NAME":'미수신' },
						{ "CODE" : "Y"	, "NAME":'수신' }
					],
			style : {}
		});


        // 출고처그룹
        fnKendoDropDownList({
            id : "warehouseGroup",
            tagId : "warehouseGroup",
            //url : "/admin/comn/getCodeList",
            url : "/admin/comn/getSupplierCompanyByWarehouseList",
            textField :"warehouseGroupName",
            valueField : "warehouseGrpCd",
//            params : { "stCommonCodeMasterCode" : "WAREHOUSE_GROUP", "useYn" : "Y" },
            params : { },
            blank : "출고처 그룹 선택",
            async : false
        });


        // 출고처그룹 별 출고처
        fnKendoDropDownList({
            id : "warehouseId",
            tagId : "warehouseId",
//            url : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
            url : "/admin/comn/getWarehouseGroupByWarehouseList",
            textField :"warehouseName",
            valueField : "supplierWarehouseId",
            blank : "출고처 선택",
            async : false,
            params : { "warehouseGroupCode" : "" }
        });



	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	function inputFocus(){
		$('#input1').focus();
	};

	function condiFocus(){
		$('#condition1').focus();
	};

	function fnShop(param){


		if(param.length == 0){
			fnKendoMessage({message:'공급업체가 선택되지 않았습니다.'});
			return;
		}

		fnKendoDropDownList({
			id    : 'store',
			url : "/admin/ur/urCompany/getStoreList",
			params : {urSupplierId : param},
			textField :"storeName",
			valueField : "storeId",
			blank : "선택해주세요"
		});



		$('#store').unbind('change').on('change', function(){

			var storeId = $('#store').val();
			var result = storeId.split("_");
			$('#store').val(result[0]);

			if(result[1]!=null){
				var telResult = result[1].split("-");

				$('#shopTelephone1').val(telResult[0]);
				$('#shopTelephone2').val(telResult[1]);
				$('#shopTelephone3').val(telResult[2]);
			}

		});
	}

	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				$('#inputForm').formClear(true);

				if(data != null && data.rows.inputTpCode == "CLIENT_TYPE.SHOP"){
					fnShop(data.rows.urSupplierId);

					data.rows.store = data.rows.storeId;
				}

				$(".addedWarehouse-conatiner__list__item").remove();
				$(".addedSeller-conatiner__list__item").remove();
				$('#addedWarehouseList').empty();
				$('#addedSellerList').empty();

				selectedWarehouseId = [];
            	deletedWarehouseId = [];

            	selectedSellersId = [];
            	deletedSellersId = [];

				fnKendoInputPoup({height:"900px" ,width:"800px",title:{key :"5876",nullMsg :'거래처 수정' } });
				$('#inputForm').bindingForm(data, "rows", true);
				$("#inputTypeCodeView").attr("disabled", true);
				$("input:radio[name='inputTpCode']").attr('disabled',true);
				$('#createInfoDiv').show();
				$('#modifyInfoDiv').show();

				if(data.rows.orderAlarmRevYn == 'N'){
					$("#orderAlarmRevYn_0").prop("checked",true);
				}else{
					$("#orderAlarmRevYn_1").prop("checked",true);
				}


				if(data.rows.inputTpCode == "CLIENT_TYPE.CLIENT"){

					$("#warehouseGroup").data("kendoDropDownList").enable(true);
					$("#warehouseId").data("kendoDropDownList").enable(true);
					$("#fnAddWarehouse").attr("disabled", false);

					$('#clientCompanyDiv').show();
					$('#clientWarehouseDiv').show();
					$('#shopDiv').hide();
					$('#vendorErpDiv').hide();
					$('#telBaseView').show();
					$('#telShopView').hide();
					$("#warehousePopup").attr("disabled", true);
					fnViewInit();
					kendo.bind($("#inputForm"), viewModel);



					let urSupplierWarehouseId;
					let warehouseName;


					for(let i = 0; i < data.rows.clientSupplierWarehouseList.length; i++) {
	        			if(data.rows.clientSupplierWarehouseList[i].urSupplierWarehouseId) {
	        				urSupplierWarehouseId = data.rows.clientSupplierWarehouseList[i].urSupplierWarehouseId.split(' ');
	        				warehouseName = data.rows.clientSupplierWarehouseList[i].warehouseName;

//	        				selectedWarehouseId[i] = urSupplierWarehouseId[0];

	        				var setId = parseInt(urSupplierWarehouseId);
	        				selectedWarehouseId[i] = setId;

	    					const li = $('<li class="addedWarehouse-conatiner__list__item"></li>');

//	    					li.append(`<input type="hidden" name="warehouseId" value="${warehouseName}" data-warehouseName="${warehouseName}" data-urSupplierWarehouseId="${urSupplierWarehouseId[0]}" readonly  style="display:none">`);
	    					li.append(`<input type="hidden" name="urSupplierWarehouseId" value="${urSupplierWarehouseId}">`);
	    					li.append(`<input type="text" name="warehouseName" value="${warehouseName}" readonly>`);
    						li.append(`<button class="btn-removeDateBoxItem1" type="button" value="${urSupplierWarehouseId}">x</button>`);

	    			        const target = $('.addedWarehouse-conatiner__list');

	    			        target.append(li);

	    			        //remove 버튼 이벤트 추가
	    			        li.on('click','.btn-removeDateBoxItem1',function(e){
	    			        	const selectedId = parseInt(this.value);
	    			        	const idx = selectedWarehouseId.indexOf(selectedId);
	    			        	selectedWarehouseId.splice(idx,1);
	    			        	$(this).parent().remove();
	    			        });
	        			}
	        		}


	        		viewModel.searchInfo.set("supplierCompany", data.rows.urSupplierId);
                    $("#supplierCompany").data('kendoDropDownList').value(data.rows.urSupplierId);
                    viewModel.fnWareHouseGroupChange();
                    supplierCompanyFlag = 'Y';
                    viewModel.fnSupplierCompanyChange();

				}else if(data.rows.inputTpCode == "CLIENT_TYPE.SHOP"){

					$('#clientCompanyDiv').show();
					$('#clientWarehouseDiv').hide();
					$('#shopDiv').show();
					$('#vendorErpDiv').hide();
					$('#telBaseView').hide();
					$('#telShopView').show();

					$("#shopTelephone1").attr('disabled',true);
					$("#shopTelephone2").attr('disabled',true);
					$("#shopTelephone3").attr('disabled',true);

					$("#supplierCompany").data("kendoDropDownList").enable( false );
					$("#store").data("kendoDropDownList").enable( false );
//					$("#store").data('kendoDropDownList').value(data.rows.store);

				}else{
					$('#clientCompanyDiv').hide();
					$('#clientWarehouseDiv').hide();
					$('#shopDiv').hide();
					$('#vendorErpDiv').show();
					$('#telBaseView').show();
					$('#telShopView').hide();
					fnInputViewInit();
					kendo.bind($("#inputForm"), inputViewModel);

					let omSellersId;
					let sellersName;


					for(let i = 0; i < data.rows.clientSellerList.length; i++) {
	        			if(data.rows.clientSellerList[i].omSellersId) {
	        				omSellersId = data.rows.clientSellerList[i].omSellersId.split(' ');
	        				sellersName = data.rows.clientSellerList[i].sellersNm;

	        				var setId = parseInt(omSellersId);
	        				selectedSellersId[i] = setId;

	    					const li = $('<li class="addedSeller-conatiner__list__item"></li>');

	    					li.append(`<input type="hidden" name="omSellersId" value="${omSellersId}"  readonly  style="display:none">`);
	    					li.append(`<input type="text" name="sellersName" value="${sellersName}" readonly>`);
    						li.append(`<button class="btn-removeSellerItem1" type="button" value="${omSellersId}">x</button>`);

	    			        const target = $('.addedSeller-conatiner__list');

	    			        target.append(li);

	    			        //remove 버튼 이벤트 추가
	    			        li.on('click','.btn-removeSellerItem1',function(e){
	    			        	const selectedId = parseInt(this.value);
	    			        	//selectedSellersId = selectedSellersId.filter(function(e) { return e !== selectedId });
	    			        	const idx = selectedSellersId.indexOf(selectedId);
	    			        	selectedSellersId.splice(idx,1);
	    			        	$(this).parent().remove();
	    			        });
	        			}
	        		}

				}
				break;
			case 'insert':
			case 'update':
				fnKendoMessage({
						message:"저장되었습니다.",
						ok:function(){
							fnSearch();
							fnClose();
						}
				});
			break;

		}
	}


	//========================  출고처 항목 추가 함수 ===========================//
	function addWareHouseItem(warehouseId, warehouseName){
		//아이템을 추가할 컨테이너
		const $target = $('.addedWarehouse-conatiner__list');

		//중복 체크 변수
		let checkItemExist = true;


		if(warehouseId == ''){
			fnKendoMessage({message:'출고처를 선택해주세요.'});
			return;
		}

		//중복 체크
		for(var i = 0 ; i<selectedWarehouseId.length ; ++i){
			if(selectedWarehouseId[i] == warehouseId){
				checkItemExist = false;
			}
		}


		//중복된 값이 없을 경우
		if(checkItemExist){

			var addWarehouseId = parseInt(warehouseId);

			//추가할 엘리먼트 생성
			selectedWarehouseId.push(addWarehouseId);

			const li = $(`<li class="addedWarehouse-conatiner__list__item"></li>`);
			li.append(`<input type="hidden" class="urWarehouseId" name="urWarehouseId" value="${warehouseId}" data-urSupplierWarehouseId=${warehouseId}">`);
			li.append(`<input type="text" class="warehouseName" name="warehouseName" value="${warehouseName}" data-warehouseName="${warehouseName}">`);
			li.append(`<button class="btn-removeWareHouseItem" type="button">x</button>`);
			$target.append(li);

			li.on('click','button',function(e){
				const selectedId = parseInt(this.defaultValue);
	        	const idx = selectedWarehouseId.indexOf(selectedId);
	        	selectedWarehouseId.splice(idx,1);
	        	$(this).parent().remove();


			//	if(confirm("삭제하시겠습니까?")){
//					$(this).parent().remove();
			//	}
			})
		}
		//중복된 값이 있을 경우
		else{
			fnKendoMessage({message:'이미 선택된 출고처입니다.'});
			return;
		}
	}


	//========================  판매처 항목 추가 함수 ===========================//
	function addSellerItem(data){
		//아이템을 추가할 컨테이너
		const $target = $('.addedSeller-conatiner__list');

		//중복 체크 변수
		let checkItemExist = true;

		const sellersId  = $('#inputSellersDetail').data('kendoDropDownList').value();
		const sellersName   = $('#inputSellersDetail').data('kendoDropDownList').text();


		if(sellersId == ''){
			fnKendoMessage({message:'외부몰을 선택해주세요.'});
			return;
		}

		//사용할 데이터
//		const { omSellersId, omSellersName} = data;

		//중복 체크
		for(var i = 0 ; i<selectedSellersId.length ; ++i){
			if(selectedSellersId[i] == sellersId){
				checkItemExist = false;
			}
		}

		//중복된 값이 없을 경우
		if(checkItemExist){
			//추가할 엘리먼트 생성

			var addId = parseInt(sellersId);

			selectedSellersId.push(addId);

			const li = $('<li class="addedSeller-conatiner__list__item"></li>');

			li.append(`<input type="hidden" class="omSellersId" name="omSellersId" value="${sellersId}">`);
			li.append(`<input type="text" class="sellersName" name="sellersName" value="${sellersName}" >`);
			li.append(`<button class="btn-removeSellerItem" type="button">x</button>`);

			const target = $('.addedSeller-conatiner__list');

			$target.append(li);

			 //remove 버튼 이벤트 추가
	        li.on('click','.btn-removeSellerItem',function(e){
	        	const selectedId = parseInt(this.defaultValue);
	        	//selectedSellersId = selectedSellersId.filter(function(e) { return e !== selectedId });
	        	const idx = selectedSellersId.indexOf(selectedId);
	        	selectedSellersId.splice(idx,1);
	        	$(this).parent().remove();
	        });
		}
		//중복된 값이 있을 경우
		else{
			fnKendoMessage({message:'이미 선택 된 외부몰입니다.'});
			return;
		}
	}


	function fnSellerGroupChange() {
		fnAjax({
            url     : "/admin/comn/getDropDownSellersGroupBySellersList",
            method : "GET",
            params : { "sellersGroupCd" : inputViewModel.inputInfo.get("inputSellersGroup") },

            success : function( data ){

                let sellerDetail = $("#inputSellersDetail").data("kendoDropDownList");
                sellerDetail.setDataSource(data.rows);
            },
            error : function(xhr, status, strError){
                fnKendoMessage({ message : xhr.responseText });
            },
            isAction : "select"
        });
	};

	function fnInputViewInit() {

        inputViewModel.inputInfo.set("inputSellersGroup", "");
        inputViewModel.inputInfo.set("inputSellersDetail", "");

	};

	function fnViewInit(){
		viewModel.searchInfo.set("warehouseGroup", "");
		viewModel.searchInfo.set("warehouseId", "");
	}


	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common New*/
	$scope.fnNew = function( ){	fnNew();};
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};

	$scope.fnAddSeller = function( ){  fnAddSeller();};

	$scope.fnAddWarehouse = function( ){  fnAddWarehouse();};


	$("#clear").click(function(){
	      $(".resultingarticles").empty();
	      $("#searchbox").val("");
    });

	//마스터코드값 입력제한 - 숫자 & -
	fnInputValidationByRegexp("accountTelephone1", /[^0-9]/g);
	fnInputValidationByRegexp("accountTelephone2", /[^0-9]/g);
	fnInputValidationByRegexp("accountTelephone3", /[^0-9]/g);
	fnInputValidationByRegexp("channelCode", /[^0-9]/g);
//	fnInputValidationForAlphabetSpecialCharacters("accountMail");
	fnInputValidationByRegexp("accountMail", /[^a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);



	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
