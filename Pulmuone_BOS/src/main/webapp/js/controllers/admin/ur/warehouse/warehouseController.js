/**-----------------------------------------------------------------------------
 * description 		 : 프로그램관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.08.01		안치열          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var selectedSupplierId = [];
var deletedSupplierId = [];

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'warehouse',
			callback : fnUI
		});


	}


	$("#storeYn").click(function(){

		if($("#storeYn").prop("checked")==false){
			$("#btnReceiverZipCodeSearch").attr("disabled", false);
			$('#addressView').show();
			$("#storeYn").val('Y');

			$('#storeLimitCntDiv').hide();
			$('#storePatternDiv').hide();
			$('#storeCutOffDiv').hide();
		}else{

			if($("#dawnDlvryYn_0").prop("checked") == true){
				fnKendoMessage({message:'매장(가맹점) 설정 인 경우, 새벽배송 가능으로 사용할 수 없습니다.'});
				$("#storeYn").prop("checked",false);
				return;
			}


			$("#btnReceiverZipCodeSearch").attr("disabled", true);
			$('#addressView').hide();
			$("#storeYn").val('N');

			$('#storeLimitCntDiv').show();
			$('#storePatternDiv').show();
			$('#storeCutOffDiv').show();
		}
	});




	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		chkInit();

		fnStoreInit();

		fnSearch();

	}


	function chkInit(){

		$('input[name^=warehouseGroup]').prop("checked",true);
	}

	function fnStoreInit(){

		$("#btnReceiverZipCodeSearch").attr("disabled", false);
		$('#addressView').show();
		$("#storeYn").val('N');
//		$('#warehouseMailDiv').hide();

        $('#storeLimitCntDiv').hide();
		$('#storePatternDiv').hide();
		$('#storeCutOffDiv').hide();
	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew, #fnSave, #fnClear, #fnClose, #fnDeliveryPatternPopupButton').kendoButton();
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
		$("span#tpCode input:radio").eq(0).click();
		chkInit();
	}
	function fnNew(){

		document.documentElement.scrollTop = 0;
		document.documentElement.scrollTop = 0;
		$('#inputForm').formClear(true);
		fnStoreInit();

		$('input:radio[name="stockOrderYn"]:input[value="N"]').prop("checked", true);
		$('input:radio[name="stlmnYn"]:input[value="N"]').prop("checked", true);
		$('input:radio[name="holidayGroupYn"]:input[value="Y"]').prop("checked", true);
		$('input:radio[name="dawnDlvryYn"]:input[value="N"]').prop("checked", true);
        $('input[name=undeliverableAreaTp]').prop("checked",false);

		$('#dawnDlvryHolidayCutoffTime').hide();
		$('#dawnDeliveryPatternDiv').hide();
		$('#dawnLimitCntDiv').hide();
		$('#dawnDlvryHolidayGroupOption').hide();
		$('#dawnUndeliverableAreaDiv').hide();

		$('#createInfoDiv').hide();

		//$('#addressView').show();
		//$('#addressNoView').hide();

		$(".supplierCompany-box__list__item").remove();

		selectedSupplierId = [];
		deletedSupplierId = [];

		fnKendoInputPoup({height:"auto" ,width:"600px", title:{ nullMsg :'출고처 등록' } } );
	}

	function fnSave(){

		var url  = '/admin/ur/warehouse/addWarehouse';
		var cbId = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/ur/warehouse/putWarehouse';
			cbId= 'update';
		}

		if($('#holidayGroupYn').getRadioVal() == "Y"){
			$('#holiday').val('_');
		}else{
			$('#holidayGroup').val('_');
		}


		if($('#dawnDlvryYn').getRadioVal() == "N"){

			$("#dawnHour").val('00');
			$("#dawnMinute").val('00');
			$("#dawnLimitCnt").val('');

			if(Number($('#storeLimitCnt').val()) > Number($('#limitCount').val())){
				fnKendoMessage({message:'매장(가맹점)택배 출고한도가 초과 되었습니다.'});
				return;
			}

		}else{


			if( $("#dawnHour").val() == '--'){
				$("#dawnHour").val(null);
			}

			if( $("#dawnMinute").val() == '--'){
				$("#dawnMinute").val(null);
			}

			if(Number($('#dawnLimitCnt').val()) > Number($('#limitCount').val())){
				fnKendoMessage({message:'새벽 일별출고한도가 초과 되었습니다.'});
				return;
			}

		}

		if($("#storeYn").prop("checked")==true){
			$('#receiverZipCode').val('_');
			$('#receiverAddress1').val('_');
			$('#receiverAddress2').val('_');
		}


		if(selectedSupplierId.length > 0){
			$('#supplierCompany').val('_');
		}


		if( $("#hour").val() == '--'){
			$("#hour").val(null);
		}

		if( $("#minute").val() == '--'){
			$("#minute").val(null);
		}


		var data = $('#inputForm').formSerialize(true);

		if(selectedSupplierId.length > 0){
			$('#supplierCompany').val('');
			data.supplierCompany = '';
		}

		for(let i = 0; i < selectedSupplierId.length; i++) {
			if(data.supplierCompany == ''){
				data.supplierCompany = selectedSupplierId[i];
			}else{
				data.supplierCompany = data.supplierCompany + '∀ ' + selectedSupplierId[i];
			}
		}


		for(let i = 0; i < deletedSupplierId.length; i++) {
			if(data.deleteSupplierCompany == ''){
				data.deleteSupplierCompany = deletedSupplierId[i];
			}else{
				data.deleteSupplierCompany = data.deleteSupplierCompany + '∀ ' + deletedSupplierId[i];
			}
		}


		if(data.rtnValid == false && data.receiverZipCode == '_'){
			$('#receiverZipCode').val(null);
			$('#receiverAddress1').val(null);
			$('#receiverAddress2').val(null);
			data.receiverZipCode = null;
			data.receiverAddress1 = null;
			data.receiverAddress2 = null;
		}

		if($("#hour").val() == null || $("#hour").val() == '--'){
			$("#hour").append("<option value='--'>  </option>")
			$("#hour").val("--").prop("selected", true);
		}

		if($("#minute").val() == null || $("#minute").val() == '--'){
			$("#minute").append("<option value='--'>  </option>")
			$("#minute").val("--").prop("selected", true);
		}

		if($("#dawnHour").val() == null || $("#dawnHour").val() == '--'){
			$("#dawnHour").append("<option value='--'>  </option>")
			$("#dawnHour").val("--").prop("selected", true);
		}

		if($("#dawnMinute").val() == null || $("#dawnMinute").val() == '--'){
			$("#dawnMinute").append("<option value='--'>  </option>")
			$("#dawnMinute").val("--").prop("selected", true);
		}


		if($("#storeYn").prop("checked")==true){
			$('#receiverZipCode').val(null);
			$('#receiverAddress1').val(null);
			$('#receiverAddress2').val(null);
			data.receiverZipCode = '';
			data.receiverAddress1 = '';
			data.receiverAddress2 = '';
			data.storeYn = 'Y';
		}else{
			data.storeYn = 'N';
		}

		if (data.dawnDlvryYn != 'Y') {
			if(data.dawnHour == '00'){
				data.dawnHour = null;
			}

			if(data.dawnMinute == '00'){
				data.dawnMinute = null;
			}
			data.dawnUndeliverableAreaTpGrp = null;
		}

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
		}else{

		}
	}

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : "/admin/ur/warehouse/getWarehouseList",
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 5
			}
			,navigatable: true
			,columns   : [
				 { field:'no'	,title : 'No'	, width:'100px',attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
				,{ field:'warehouseGroupName'	,title : '출고처그룹'	, width:'100px',attributes:{ style:'text-align:center' }}
				,{ field:'warehouseName'	,title : '출고처명'		, width:'140px',attributes:{ style:'text-align:center' }}
				,{ field:'companyName'		,title : '업체명(약관)'		, width:'140px',attributes:{ style:'text-align:center' }}
				,{ field:'storeYn'	 		,title : '매장(가맹점)여부'	, width:'100px',attributes:{ style:'text-align:center' }
				,	template : "#=(storeYn=='Y')?'매장(가맹점)':''#"}
				,{ field:'supplierCompany'		,title : '공급업체'		, width:'120px',attributes:{ style:'text-align:center' }}
				,{ field:'cutoffTime'		,title : '주문마감시간'		, width:'100px',attributes:{ style:'text-align:center' }}
				,{ field:'deliveryPatternName'		,title : '배송패턴명'		, width:'120px',attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		,title : '등록일자'		, width:'120px',attributes:{ style:'text-align:center' }}
				,{ field:'psShippingPatternId', hidden:true}
				,   { command: [
								{ text: '배송정책 생성' , className: "f-grid-add k-margin5",
									   click: function(e) {  e.preventDefault();
							            var tr = $(e.target).closest("tr");
							            var data = this.dataItem(tr);
							            fnDeliveryNew();}
								, visible: function(dataItem) { return dataItem.shippingTemplateCnt == 0} }
								,{ text: '배송정책 목록' , imageClass: "k-i-search", className: "f-grid-search k-margin5", iconClass: "k-icon",
								click: function(e) {  e.preventDefault();
										            	var tr = $(e.target).closest("tr");
										            	var data = this.dataItem(tr);
										            	fnDeliveryList(data.urWarehouseId);}
								, visible: function(dataItem) { return dataItem.shippingTemplateCnt > 0} }]
					, title: '배송정책관리', width: "100px", attributes:{ style:'text-align:center'  , class:'forbiz-cell-readonly #:#' }}   // EXECUTE_TYPE 별 버튼 제어 처리 확인 필요

			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
			$("#aGrid").on("click", "tbody>tr>td", function () {

				var index = $(this).index();

				if(index < 7){
					fnGridClick();
				}
		});

		aGrid.bind("dataBound", function() {
			//row number
			var row_number = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(){
				$(this).html(row_number);
				row_number--;
			});

			//total count
            $('#totalCnt').text(aGridDs._total);
        });

	}

	function fnDeliveryNew(){
		fnGoPage({url:'#/shppgTmplt',menuId : 123,menuGrpName : '통합몰 설정',menuGrpId : 1});

	}


	function fnDeliveryList(params){

        fnKendoPopup({
            id         : "deliveryPatternViewPopup",
            title      : "출고처 보기",
            width      : "800px",
            height     : "700px",
            scrollable : "yes",
            src        : "#/deliveryPatternViewPopup",
            param      : {urWarehouseId : params},
            success    : function( id, data ){
            }
        });
	}

	function fnGridClick(){
		var aMap = aGrid.dataItem(aGrid.select());

		// 팝업호출시 체크박스 해제
		$('input:checkbox[name="undeliverableAreaTpGrp"]').prop("checked", false);
		$('input:checkbox[name="dawnUndeliverableAreaTpGrp"]').prop("checked", false);

		fnAjax({
			url     : '/admin/ur/warehouse/getWarehouse',
			params  : {urWarehouseId : aMap.urWarehouseId},
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'select'
		});
	};

	function fnPutSupplier(urClientId){


		fnAjax({
			url     : '/admin/ur/urCompany/getClient',
			params  : {urClientId : urClientId},
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'select'
		});

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

		//물류비 정산 여부
		fnTagMkRadio({
			id    :  'stlmnYn',
			tagId : 'stlmnYn',
			chkVal: 'Y',
			data  : [
						{ "CODE" : "Y"	, "NAME":'예' },
						{ "CODE" : "N"	, "NAME":'아니오' }
					],
			style : {}
		});

		//재고발주 여부
		fnTagMkRadio({
			id    :  'stockOrderYn',
			tagId : 'stockOrderYn',
			chkVal: 'Y',
			data  : [
						{ "CODE" : "Y"	, "NAME":'예' },
						{ "CODE" : "N"	, "NAME":'아니오' }
					],
			style : {}
		});

		// 새별배송 가능여부
		fnTagMkRadio({
			id    :  'dawnDlvryYn',
			tagId : 'dawnDlvryYn',
			chkVal: 'Y',
			data  : [
						{ "CODE" : "Y"	, "NAME":'가능' },
						{ "CODE" : "N"	, "NAME":'불가' }
					],
			style : {},
			change : function(e){
				if($('#dawnDlvryYn').getRadioVal() == "N"){
					$('#dawnDlvryWeekOption').hide();
					$('#dawnDlvryOption').hide();
					$('#dawnDlvryHolidayCutoffTime').hide();
					$('#dawnDeliveryPatternDiv').hide();
					$('#dawnLimitCntDiv').hide();
					$('#dawnUndeliverableAreaDiv').hide();
				}else{

					if($("#storeYn").prop("checked") == true){
						$("#dawnDlvryYn_1").prop("checked",true);
						fnKendoMessage({message:'매장(가맹점) 설정 인 경우, 새벽배송 가능으로 사용할 수 없습니다.'});
						return;
					}

					$('#dawnDlvryWeekOption').show();
					$('#dawnDlvryOption').show();
					$('#dawnDlvryHolidayCutoffTime').show();
					$('#dawnDeliveryPatternDiv').show();
					$('#dawnLimitCntDiv').show();
					$('#dawnUndeliverableAreaDiv').show();


				}
            }
		});


		// 출고처 그룹
		fnKendoDropDownList({
			id    : "warehouseGroup",
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "WAREHOUSE_GROUP", "useYn" :"Y"},
			tagId : 'warehouseGroupCode',
			blank : "선택해주세요"

		});


		// 출고처 그룹
		fnKendoDropDownList({
			id    : "warehouseGroupCode",
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "WAREHOUSE_GROUP", "useYn" :"Y"},
			tagId : 'warehouseGroupCode',
			blank : "선택해주세요"

		});

		// 공급업체 리스트 조회(검색)
		fnKendoDropDownList({
			id    : 'supplier',
			url : "/admin/ur/urCompany/getSupplierCompanyList",
			params : {},
			textField :"supplierName",
			valueField : "urSupplierId",
			blank : "선택해주세요"
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

		// 팝업 공급업체 리스트 조회 Change 이벤트 바인딩
		$('#supplierCompany').data('kendoDropDownList').bind('change', onChangeSupplierCompany)


		// 주문 변경여부
		fnTagMkRadio({
			id    : 'orderChangeType',
			tagId : 'orderChangeType',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "ORDER_CHANGE_TP", "useYn" :"Y"},
			async : false,
			chkVal: 'ORDER_CHANGE_TP.NOT_USE'
		});

		// 주문 알림 수신여부
//		fnTagMkRadio({
//			id    :  'orderStatusAlamYn',
//			tagId : 'orderStatusAlamYn',
//			chkVal: 'Y',
//			data  : [
//						{ "CODE" : "N"	, "NAME":'아니오' },
//						{ "CODE" : "Y"	, "NAME":'예' }
//					],
//			style : {},
//			change : function(e){
//				if($('#orderStatusAlamYn').getRadioVal() == "N"){
//					$('#warehouseMailDiv').hide();
//				}else{
//					$('#warehouseMailDiv').show();
//				}
//            }
//		});


		// 출고처 배송불가지역
		// fnKendoDropDownList({
		//     id: 'undeliverableAreaTp',
		//     url: "/admin/comn/getCodeList",
		//     params: {"stCommonCodeMasterCode": "UNDELIVERABLE_TP", "useYn": "Y"},
		//     tagId: 'undeliverableAreaTp',
		//     blank: "사용안함"
		// });

		// 출고처 배송불가지역
		fnTagMkChkBox({
			id         : 'undeliverableAreaTpGrp',
			url         : "/admin/comn/getCodeList",
			tagId      : 'undeliverableAreaTpGrp',
			autoBind   : true,
			async      : false,
			valueField   : 'CODE',
			textField    : 'NAME',
			chkVal      : '',
			style      : {},
			params: {"stCommonCodeMasterCode": "UNDELIVERABLE_TP", "useYn": "Y"},
		});

		// 새벽 배송불가지역
		// fnKendoDropDownList({
		//     id: 'dawnUndeliverableAreaTp',
		//     url: "/admin/comn/getCodeList",
		//     params: {"stCommonCodeMasterCode": "UNDELIVERABLE_TP", "useYn": "Y"},
		//     tagId: 'dawnUndeliverableAreaTp',
		//     blank: "사용안함"
		// });

		// 새벽 배송불가지역
		fnTagMkChkBox({
			id         : 'dawnUndeliverableAreaTpGrp',
			url         : "/admin/comn/getCodeList",
			tagId      : 'dawnUndeliverableAreaTpGrp',
			autoBind   : true,
			async      : false,
			valueField   : 'CODE',
			textField    : 'NAME',
			chkVal      : '',
			style      : {},
			params: {"stCommonCodeMasterCode": "UNDELIVERABLE_TP", "useYn": "Y"},
		});

	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------


	function fnShop(param){


		if(param.length == 0){
			fnKendoMessage({message:'공급업체가 선택되지 않았습니다.'});
			return;
		}

		fnKendoDropDownList({
			id    : 'store',
			url : "/admin/ur/urCompany/getStoreList",
			params : {},
			textField :"storeName",
			valueField : "storeId",
			blank : "선택해주세요"
		});
	}

	/**
	* 콜백함수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				$('#inputForm').bindingForm(data, "rows", true);

				$(".supplierCompany-box__list__item").remove();
            	selectedSupplierId = [];
            	deletedSupplierId = [];

				fnKendoInputPoup({height:"1300px" ,width:"600px",title:{key :"5876",nullMsg :'출고처 수정' } });

				$('#createInfoDiv').show();

				let urSupplierId;
				let supplierName;
				let urSupplierWarehouseId;
				// 공급업체 설정
				for(let i = 0; i < data.rows.supplierList.length; i++) {
        			if(data.rows.supplierList[i].urSupplierId) {
        				urSupplierId = data.rows.supplierList[i].urSupplierId.split(' ');
        				supplierName = data.rows.supplierList[i].supplierName.split(' ');
        				urSupplierWarehouseId = data.rows.supplierList[i].urSupplierWarehouseId.split(' ');

        				selectedSupplierId[i] = urSupplierId[0];

    					const li = $('<li class="supplierCompany-box__list__item"></li>');

    					li.append(`<input type="hidden" name="selectedCompany" value="${supplierName[0]}" data-suppliername="${supplierName[0]}" data-urSupplierId="${urSupplierId[0]}" readonly  style="display:none">`);
    					li.append(`<input type="hidden" name="urSupplierId" value="${urSupplierId[0]}">`);
    					li.append(`<input type="hidden" name="urSupplierWarehouseId" value="${urSupplierWarehouseId[0]}">`);
    					li.append(`<input type="text" name="supplierName" value="${supplierName[0]}" readonly>`);
    					if(data.rows.supplierList[i].supplierYn == 'N'){
    						li.append(`<button class="btn-removeDateBoxItem" type="button">x</button>`);
    					}

    			        const target = $('.supplierCompany-box__list');

    			        target.append(li);

    			        //remove 버튼 이벤트 추가
    			        li.on('click','.btn-removeDateBoxItem',function(e){

    			        	const selectedId = $(e.currentTarget).closest(".supplierCompany-box__list__item").find("[name=urSupplierWarehouseId]").val();
    			        	deletedSupplierId = deletedSupplierId.filter(function(e) { return e !== selectedId });
    	        			deletedSupplierId.push(selectedId);
    			        	$(this).parent().remove();
    			        });
        			}
        		}

				// 배송불가지역
				let undeliverableAreaTpArr;
				undeliverableAreaTpArr = data.rows.undeliverableAreaTpGrp.split('∀');
				for(let j = 0 ; j < undeliverableAreaTpArr.length ; j++) {
					$('input:checkbox[name="undeliverableAreaTpGrp"]').filter('[value="'+undeliverableAreaTpArr[j]+'"]').prop("checked", true);
				}

				// 새벽배송불가지역
				let dawnUndeliverableAreaTpArr;
				dawnUndeliverableAreaTpArr = data.rows.dawnUndeliverableAreaTpGrp.split('∀');
				for(let k = 0 ; k < dawnUndeliverableAreaTpArr.length ; k++) {
					$('input:checkbox[name="dawnUndeliverableAreaTpGrp"]').filter('[value="' + dawnUndeliverableAreaTpArr[k] + '"]').prop("checked", true);
				}

				// 새벽배송 가능여부 - 가능
				if(data.rows.dawnDlvryYn == 'Y'){
					$('input:checkbox[name="dawnDlvryYn_0"]').prop("checked", true);
					$('#dawnDlvryWeekOption').show();
					$('#dawnDlvryOption').show();
					$('#dawnDlvryHolidayCutoffTime').show();
					$('#dawnDeliveryPatternDiv').show();
					$('#dawnLimitCntDiv').show();
					$('#dawnUndeliverableAreaDiv').show();
				}else{
					$('input:checkbox[name="dawnDlvryYn_1"]').prop("checked", true);
					$('#dawnDlvryWeekOption').hide();
					$('#dawnDlvryOption').hide();
					$('#dawnDlvryHolidayCutoffTime').hide();
					$('#dawnDeliveryPatternDiv').hide();
					$('#dawnLimitCntDiv').hide();
					$('#dawnUndeliverableAreaDiv').hide();

					// 매장(가맹점)인 경우
					if(data.rows.storeYn == 'Y'){
						$('input:checkbox[name="storeYn"]').prop("checked", true);
						$('#addressView').hide();

				        $('#storeLimitCntDiv').show();
						$('#storePatternDiv').show();
						$('#storeCutOffDiv').show();
					}
				}

				// 매장(가맹점)이 아닌 경우
				if(data.rows.storeYn == 'N'){
					$('input:checkbox[name="storeYn"]').prop("checked", false);
					$('#addressView').show();

					$('#storeLimitCntDiv').hide();
					$('#storePatternDiv').hide();
					$('#storeCutOffDiv').hide();
				}


//				if(data.rows.orderStatusAlamYn == 'N'){
//					$('input:checkbox[name="warehouseMailDiv_0"]').prop("checked", true);
//					$('#warehouseMailDiv').hide();
//				}else{
//					$('input:checkbox[name="warehouseMailDiv_1"]').prop("checked", true);
//					$('#warehouseMailDiv').show();
//				}

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


	//================공급업체 드롭다운 리스트 Change 함수===============
	function onChangeSupplierCompany(e){
		let checkCompanyExist = true;
		const li = $('<li class="supplierCompany-box__list__item"></li>')

		const idValue = this.value();
		const textValue = this.text();
		const target = $('.supplierCompany-box__list');

		$('input[name="selectedCompany"]').each(function(){
			if(this.dataset.suppliername === textValue){
				checkCompanyExist = false;
			}
		})
		//선택해주세요가 선택 될경우 idvalue.length === 0이므로 추가되지 않는다.
		//업체가 중복되지 않고, '기본값'이 아닌 업체가 선택된 경우
		if(checkCompanyExist && idValue.length){


			selectedSupplierId.push(idValue);

			li.append(`<input type="hidden" name="selectedCompany" value="${idValue}" data-suppliername="${textValue}" data-urSupplierId="${idValue}" readonly  style="display:none">`);
			li.append(`<span style="display:block">${textValue}</span>`);
			li.append(`<button class="btn-removeCompanyBoxItem" type="button">x</button>`);
			const target = $('.supplierCompany-box__list')
			target.append(li);
			li.on('click','button',function(e){
				if(confirm("삭제하시겠습니까?")){
					$(this).parent().remove();
				}
			})
			this.select(0);
		}
		//중복된 값이 선택된 경우
		else if(!checkCompanyExist){
			fnKendoMessage({message:'이미 선택된 업체입니다.'});
			this.select(0);
		}else{
			return;
		}
	}

	//배송패턴 검색 팝업
    function fnDeliveryPatternPopupButton(type){

	    var patternId;
	    if(type == 'delivery'){
	        patternId = $('#deliveryPatternId').val();
	    }else if(type == 'dawn'){
	        patternId = $('#dawnDeliveryPatternId').val();
	    }else if(type == 'store'){
	        patternId = $('#storeShippingPatternId').val();
	    }

            fnKendoPopup({
                id     : 'deliveryPatternMgm',
                title  : '배송패턴 조회',
                src    : '#/deliveryPatternMgm',
                width  : '1400px',
                height : '1200px',
                param  : { "psShippingPatternId" : $('#storeShippingPatternId').val()},
                success: function( id, data ){
                    if(data[0] != undefined){

                    	if(type == 'delivery'){
                    		$('#deliveryPatternId').val(data[0]);
        					$('#deliveryPatternName').val(data[1]);
                        }else if(type == 'dawn'){
                            $('#dawnDeliveryPatternId').val(data[0]);
                            $('#dawnDeliveryPatternName').val(data[1]);
                        }else if(type == 'store'){
                            $('#storeShippingPatternId').val(data[0]);
                            $('#storePatternName').val(data[1]);
                        }

                    }
                }
            });
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

	$scope.fnDeliveryPatternPopupButton = function(type ){  fnDeliveryPatternPopupButton(type);};

	// Excel Download
	$scope.fnExcelDownload = function() {
		var data = $('#searchForm').formSerialize(true);
		fnExcelDownload('/admin/ur/warehouse/getWarehouseExcelDownload', data);
	};


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
	fnInputValidationByRegexp("dawnLimitCnt", /[^0-9]/g);
	fnInputValidationByRegexp("storeLimitCnt", /[^0-9]/g);


	// 입력가능 : 한글 & 영어(대,소) & 숫자 & 특수문자  []~!@#$%^&*()_+|<>?:{}
	fnInputValidationByRegexp("inputWarehouseName", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);
	fnInputValidationByRegexp("inputCompanyName", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);


	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
