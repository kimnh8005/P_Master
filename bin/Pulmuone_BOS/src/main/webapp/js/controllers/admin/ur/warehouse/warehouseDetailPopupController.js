/**-----------------------------------------------------------------------------
 * description 		 : 출고처 상세 팝업
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.25		안치열          최초생성
 * @
 * **/
'use strict';


var aGridDs, aGridOpt, aGrid;
var selectedSupplierId = [];
var deletedSupplierId = [];

var paramData = parent.POP_PARAM["parameter"]; // 파라미터


$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false});

		fnPageInfo({
			PG_ID  : 'warehouseDetailPopup',
			callback : fnUI
		});


	}


	$("#storeYn").click(function(){

		if($("#storeYn").prop("checked")==false){
			$("#btnReceiverZipCodeSearch").attr("disabled", false);
			$('#addressView').show();
			$("#storeYn").val('Y');
		}else{
			$("#btnReceiverZipCodeSearch").attr("disabled", true);
			$('#addressView').hide();
			$("#storeYn").val('N');
		}
	});




	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		chkInit();

		fnStoreInit();

		initUI();

	}


	function chkInit(){

		$('input[name^=warehouseGroup]').prop("checked",true);
	}

	function fnStoreInit(){

		$("#btnReceiverZipCodeSearch").attr("disabled", false);
		$('#addressView').show();
		$("#storeYn").val('N');
		$('#warehouseMailDiv').hide();
	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew, #fnSave, #fnClear, #fnClose, #fnDeliveryPatternPopupButton, #fnDawnDeliveryPatternPopupButton').kendoButton();
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

			if($('#dawnHour').val() == null){
				$("#dawnHour").val('00');
			}
			if($('#dawnMinute').val() == null){
				$("#dawnMinute").val('00');
			}

		}else{


			if( $("#dawnHour").val() == '--'){
				$("#dawnHour").val(null);
			}

			if( $("#dawnMinute").val() == '--'){
				$("#dawnMinute").val(null);
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
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------

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

		// 새벽배송 가능여부
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
				}else{
					$('#dawnDlvryWeekOption').show();
					$('#dawnDlvryOption').show();
					$('#dawnDlvryHolidayCutoffTime').show();
					$('#dawnDeliveryPatternDiv').show();
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
		fnTagMkRadio({
			id    :  'orderStatusAlamYn',
			tagId : 'orderStatusAlamYn',
			chkVal: 'Y',
			data  : [
						{ "CODE" : "N"	, "NAME":'아니오' },
						{ "CODE" : "Y"	, "NAME":'예' }
					],
			style : {},
			change : function(e){
				if($('#orderStatusAlamYn').getRadioVal() == "N"){
					$('#warehouseMailDiv').hide();
				}else{
					$('#warehouseMailDiv').show();
				}
            }
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


	//출고처 상세 팝업
	function initUI(){
		var url  = '/admin/ur/warehouse/getWarehouse';
		var data = {
				urWarehouseId : paramData.urWarehouseId
				};
		fnAjax({
				url     : url,
				params  : data,
				success :
				function(result) {

					$('#inputForm').bindingForm(result, "rows", true);

					$(".supplierCompany-box__list__item").remove();
	            	selectedSupplierId = [];
	            	deletedSupplierId = [];

					$('#createInfoDiv').show();

					let urSupplierId;
					let supplierName;
					let urSupplierWarehouseId;
					for(let i = 0; i < result.rows.supplierList.length; i++) {
	        			if(result.rows.supplierList[i].urSupplierId) {
	        				urSupplierId = result.rows.supplierList[i].urSupplierId.split(' ');
	        				supplierName = result.rows.supplierList[i].supplierName.split(' ');
	        				urSupplierWarehouseId = result.rows.supplierList[i].urSupplierWarehouseId.split(' ');

	        				selectedSupplierId[i] = urSupplierId[0];

	    					const li = $('<li class="supplierCompany-box__list__item"></li>');

	    					li.append(`<input type="hidden" name="selectedCompany" value="${supplierName[0]}" data-suppliername="${supplierName[0]}" data-urSupplierId="${urSupplierId[0]}" readonly  style="display:none">`);
	    					li.append(`<input type="hidden" name="urSupplierId" value="${urSupplierId[0]}">`);
	    					li.append(`<input type="hidden" name="urSupplierWarehouseId" value="${urSupplierWarehouseId[0]}">`);
	    					li.append(`<input type="text" name="supplierName" value="${supplierName[0]}" readonly>`);
	    					if(result.rows.supplierList[i].supplierYn == 'N'){
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


					if(result.rows.dawnDlvryYn == 'Y'){
						$('input:checkbox[name="dawnDlvryYn_0"]').prop("checked", true);
						$('#dawnDlvryWeekOption').show();
						$('#dawnDlvryOption').show();
						$('#dawnDlvryHolidayCutoffTime').show();
						$('#dawnDeliveryPatternDiv').show();
					}else{
						$('input:checkbox[name="dawnDlvryYn_1"]').prop("checked", true);
						$('#dawnDlvryWeekOption').hide();
						$('#dawnDlvryOption').hide();
						$('#dawnDlvryHolidayCutoffTime').hide();
						$('#dawnDeliveryPatternDiv').hide();
					}

					if(result.rows.storeYn == 'N'){
						$('input:checkbox[name="storeYn"]').prop("checked", false);
						$('#addressView').show();
					}else{
						$('input:checkbox[name="storeYn"]').prop("checked", true);
						$('#addressView').hide();
					}


					if(result.rows.orderStatusAlamYn == 'N'){
						$('input:checkbox[name="warehouseMailDiv_0"]').prop("checked", true);
						$('#warehouseMailDiv').hide();
					}else{
						$('input:checkbox[name="warehouseMailDiv_1"]').prop("checked", true);
						$('#warehouseMailDiv').show();
					}
				},

				isAction : 'select'
			});
	}

	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'insert':
			case 'update':
				fnKendoMessage({
						message:"저장되었습니다.",
						ok:function(){
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
	function fnDeliveryPatternPopupButton(){


		fnKendoPopup({
			id     : 'deliveryPatternMgm',
			title  : '배송패턴 조회',
			src    : '#/deliveryPatternMgm',
			width  : '1400px',
			height : '1200px',
			param  : { "psShippingPatternId" : $('#deliveryPatternId').val()},
			success: function( id, data ){
				if(data[0] != undefined){
					$('#deliveryPatternId').val(data[0]);
					$('#deliveryPatternName').val(data[1]);
				}
			}
		});
	}


	function fnDawnDeliveryPatternPopupButton(){

		fnKendoPopup({
			id     : 'deliveryPatternMgm',
			title  : '배송패턴 조회',
			src    : '#/deliveryPatternMgm',
			width  : '1400px',
			height : '1200px',
			param  : { "psShippingPatternId" : $('#dawnDeliveryPatternId').val()},
			success: function( id, data ){
				if(data[0] != undefined){
					$('#dawnDeliveryPatternId').val(data[0]);
					$('#dawnDeliveryPatternName').val(data[1]);
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

	$scope.fnDeliveryPatternPopupButton = function( ){  fnDeliveryPatternPopupButton();};

	$scope.fnDawnDeliveryPatternPopupButton = function( ){  fnDawnDeliveryPatternPopupButton();};




	//마스터코드값 입력제한 - 숫자 & -
	fnInputValidationByRegexp("accountTelephone1", /[^0-9]/g);
	fnInputValidationByRegexp("accountTelephone2", /[^0-9]/g);
	fnInputValidationByRegexp("accountTelephone3", /[^0-9]/g);
	fnInputValidationByRegexp("receiverZipCode", /[^0-9]/g);
	fnInputValidationByRegexp("limitCount", /[^0-9]/g);
	fnInputValidationByRegexp("warehouseTelephone1", /[^0-9]/g);
	fnInputValidationByRegexp("warehouseTelephone2", /[^0-9]/g);
	fnInputValidationByRegexp("warehouseTelephone3", /[^0-9]/g);


	// 입력가능 : 한글 & 영어(대,소) & 숫자 & 특수문자  []~!@#$%^&*()_+|<>?:{}
	fnInputValidationByRegexp("inputWarehouseName", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);


	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
