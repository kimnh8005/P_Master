﻿﻿/**-----------------------------------------------------------------------------
 * description 		 : 쿠폰 등록
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.08.28		안치열          최초생성
 * @
 */
'use strict';
var ctgryGridDs, ctgryGridOpt, ctgryGrid;
var cartCoverageGridDs, cartCoverageGridOpt, cartCoverageGrid;

var accountGridDs, accountGridOpt, accountGrid;
var ticketGridDs, ticketGridOpt, ticketGrid;
var appintCoverageGridDs, appintCoverageGridOpt, appintCoverageGrid;

var apprAdminGridDs, apprAdminGridOpt, apprAdminGrid;

var ilCtgryStd1;
var ilCtgryStd2;
var ilCtgryStd3;
var ilCtgryStd4;

var pgPromotionPayConfigId;
var pgPromotionPayGroupId;
var pgPromotionPayId;

var accountUploadData;


$(document).ready(function() {

	fnInitialize();	//Initialize Page Call ---------------------------------

    // sheetJs 스크립트 추가
    let myScript = document.createElement("script");
    myScript.setAttribute("src", "js/lib/sheetJs/xlsx.full.min.js");
    document.body.appendChild(myScript);


	//유효기간 라디오버튼 제어
	$("input[name=validityType]").change(function() {
		var chkValue = $(this).val();
		if(chkValue == 'VALIDITY_TYPE.PERIOD'){
			$("#validityStartDate").data("kendoDatePicker").enable( true );
	        $("#validityEndDate").data("kendoDatePicker").enable( true );

			$("#validityDay").attr("disabled", true);
			$('#validityDay').val();
		}else{
			$("#validityStartDate").data("kendoDatePicker").enable( false );
	        $("#validityEndDate").data("kendoDatePicker").enable( false );
			$("#validityDay").attr("disabled", false);
			$('#validityStartDate').val();
			$('#validityEndDate').val();
			$("#validityDay").focus();
		}
	});



	$("input[name=ticketType]").change(function() {
		var chkValue = $(this).val();
		if(chkValue == 'SERIAL_NUMBER_TYPE.AUTO'){
			$("#ticketIssueType").data("kendoDropDownList").enable(true);
			$("#serialNumber").attr("disabled", true);
			$("#uploadTicketViewControl").hide();
			$("#ticketUploadFileName").hide();
			$("#serialNumber").val('')
		}else{
			$("#ticketIssueType").data("kendoDropDownList").enable(false);
			var dropDownList =$('#ticketIssueType').data('kendoDropDownList');
			dropDownList.value("SERIAL_NUMBER_TYPE.AUTO_CREATE");
			$("#fnTicketExcelUpload").hide();
			$("#fnTicketSamepleFormDownload").hide();
			$("#serialNumber").attr("disabled", false);
			$("#uploadTicketViewControl").hide();
			$("#ticketUploadFileName").hide();
			$("#issueQty").attr("disabled", false);
		}
	});


	//할인방식 라디오버튼 제어
	$("input[name=discountType]").change(function() {
		var chkValue = $(this).val();
		if(chkValue == 'COUPON_DISCOUNT_STATUS.PERCENTAGE_DISCOUNT'){
			$("#discountValueFixed").attr("disabled", true);
			$("#discountValuePercent").attr("disabled", false);
			$("#percentageMaxDiscountAmount").attr("disabled", false);
			$("#discountValueFixed").val('');
		}else{
			$("#discountValuePercent").val('');
			$("#percentageMaxDiscountAmount").val('');
			$("#discountValueFixed").attr("disabled", false);
			$("#discountValuePercent").attr("disabled", true);
			$("#percentageMaxDiscountAmount").attr("disabled", true);
		}
	});

	$("#allUse").click(function(){
		if($("#allUse_0").prop("checked") == true){
			$('input[name^=use]').prop("checked",true);
		}else{
			$('input[name^=use]').prop("checked",false);
		}
	});

	$("#usePcYn").click(function(){
		if($("input[name^='use']:checked").length == 3){
			$("#allUse_0").prop("checked",true);
		}else{
			$("#allUse_0").prop("checked",false);
		}
	});

	$("#useMobileWebYn").click(function(){
		if($("input[name^='use']:checked").length == 3){
			$("#allUse_0").prop("checked",true);
		}else{
			$("#allUse_0").prop("checked",false);
		}
	});

	$("#useMobileAppYn").click(function(){
		if($("input[name^='use']:checked").length == 3){
			$("#allUse_0").prop("checked",true);
		}else{
			$("#allUse_0").prop("checked",false);
		}
	});

	$("input[name=issueQtyType]").change(function() {
		if($("#issueQtyType_0").prop("checked") == true){
			$("#issueQtyType_0").prop("checked",true);
			$("#issueQty").attr("disabled", false);
		}else{
			$("#issueQtyType_1").prop("checked",true);
			$("#issueQty").attr("disabled", true);
		}
	});

	//승인 요청처리 여부
	$("#approvalCheckbox").click(function(){
		if($("#approvalCheckbox").prop("checked") == true){
			$('#approvalCheckbox').prop("checked",true);
			$('#apprDiv').show();
		}else{
			$('#approvalCheckbox').prop("checked",false);
			$('#apprDiv').hide();
		}
	});


	$("input[name=percentageMaxDiscountAmount]").change(function() {
		var issueQty;
		var percentageMaxDiscountAmount;
		var discountType;
		var person;
		var budget;

		issueQty = Number($('#issueQty').val());
		percentageMaxDiscountAmount = Number($('#percentageMaxDiscountAmount').val());
		discountType = $('#discountType').val();
		person = $("#accountGrid").data("kendoGrid").dataSource.total();

		var couponType = $('#couponType').getRadioVal();
		var paymentTypeShippingPrice;
		var paymentTypeCart;
		var paymentType;

		if(couponType == 'COUPON_TYPE.GOODS'){
			paymentType = $('#paymentType').getRadioVal();
		}else if(couponType == 'COUPON_TYPE.CART'){
			paymentType = $('#paymentTypeCart').getRadioVal();
		}else if(couponType = 'COUPON_TYPE.SHIPPING_PRICE'){
			paymentType = $('#paymentTypeShippingPrice').getRadioVal();
		}


		if(	paymentType  == 'PAYMENT_TYPE.CHECK_PAYMENT'){

			if( issueQty >0 && percentageMaxDiscountAmount>0 && person >0){
				//계정발급
				budget = issueQty*percentageMaxDiscountAmount*person
			}
		}else{
			if( issueQty >0 && percentageMaxDiscountAmount>0){
				//정률할인
				budget = issueQty*percentageMaxDiscountAmount
			}
		}

		if($("#issueQtyType_0").prop("checked") == true){
			$('#budget').text(budget.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + '원');
		}else{
			$('#budget').text('제한없음');
		}
	});


	$("input[name=discountValueFixed]").change(function() {
		var issueQty;
		var discountValueFixed;
		var discountType;
		var person;
		var budget='';

		issueQty = Number($('#issueQty').val());
		discountValueFixed = Number($('#discountValueFixed').val());
		discountType = $('#discountType').val();
		person = $("#accountGrid").data("kendoGrid").dataSource.total();

		var couponType = $('#couponType').getRadioVal();
		var paymentTypeShippingPrice;
		var paymentTypeCart;
		var paymentType;

		if(couponType == 'COUPON_TYPE.GOODS'){
			paymentType = $('#paymentType').getRadioVal();
		}else if(couponType == 'COUPON_TYPE.CART'){
			paymentType = $('#paymentTypeCart').getRadioVal();
		}else if(couponType = 'COUPON_TYPE.SHIPPING_PRICE'){
			paymentType = $('#paymentTypeShippingPrice').getRadioVal();
		}

		if(	paymentType  == 'PAYMENT_TYPE.CHECK_PAYMENT'){

			if(issueQty >0 && discountValueFixed>0 && person >0){
				//계정발급
				budget = issueQty*discountValueFixed*person;
			}
		}else{
			if(issueQty >0 && discountValueFixed>0){
				//정액할인
				budget = issueQty*discountValueFixed;
			}
		}

		if($("#issueQtyType_0").prop("checked") == true){
			$('#budget').text(budget.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + '원');
		}else{
			$('#budget').text('제한없음');
		}
	});


	$("input[name=issueQty]").change(function() {
		var qty;
		var issueQty;
		var percentageMaxDiscountAmount;
		var discountValueFixed;
		var discountType;
		var person;
		var budget= '';

		issueQty = Number($('#issueQty').val());
		percentageMaxDiscountAmount = Number($('#percentageMaxDiscountAmount').val());
		discountType = $('#discountType').val();
		person = $("#accountGrid").data("kendoGrid").dataSource.total();
		discountValueFixed = Number($('#discountValueFixed').val());

		var couponType = $('#couponType').getRadioVal();
		var paymentTypeShippingPrice;
		var paymentTypeCart;
		var paymentType;

		if(couponType == 'COUPON_TYPE.GOODS'){
			paymentType = $('#paymentType').getRadioVal();
		}else if(couponType == 'COUPON_TYPE.CART'){
			paymentType = $('#paymentTypeCart').getRadioVal();
		}else if(couponType = 'COUPON_TYPE.SHIPPING_PRICE'){
			paymentType = $('#paymentTypeShippingPrice').getRadioVal();
		}


		if(	paymentType  == 'PAYMENT_TYPE.CHECK_PAYMENT'){
			if($('#discountType_0').prop("checked") == true){
				if(issueQty >0 && percentageMaxDiscountAmount>0 && person >0){
					//계정발급
					budget = issueQty*percentageMaxDiscountAmount*person;
				}
			}else{
				if(issueQty >0 && discountValueFixed>0 && person >0){
					budget = issueQty*discountValueFixed*person;
				}
			}
		}else{
			if($('#discountType_0').prop("checked") == true){
				if(issueQty >0 && percentageMaxDiscountAmount>0){
					//정률할인
					budget = issueQty*percentageMaxDiscountAmount;
				}
			}else {
				if(issueQty >0 && discountValueFixed>0){
					//정률할인
					budget = issueQty*discountValueFixed;
				}
			}
		}

		if($("#issueQtyType_0").prop("checked") == true){
			$('#budget').text(budget.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + '원');
		}else{
			$('#budget').text('제한없음');
		}
	});



	$("input[name=issueQtyType]").change(function() {

		if($("#issueQtyType_1").prop("checked") == true){
			$('#budget').text('제한없음');
		}else{
			var qty;
			var issueQty;
			var percentageMaxDiscountAmount;
			var discountValueFixed;
			var discountType;
			var person;
			var budget= '';

			issueQty = Number($('#issueQty').val());
			percentageMaxDiscountAmount = Number($('#percentageMaxDiscountAmount').val());
			discountType = $('#discountType').val();
			person = $("#accountGrid").data("kendoGrid").dataSource.total();
			discountValueFixed = Number($('#discountValueFixed').val());

			var couponType = $('#couponType').getRadioVal();
			var paymentTypeShippingPrice;
			var paymentTypeCart;
			var paymentType;

			if(couponType == 'COUPON_TYPE.GOODS'){
				paymentType = $('#paymentType').getRadioVal();
			}else if(couponType == 'COUPON_TYPE.CART'){
				paymentType = $('#paymentTypeCart').getRadioVal();
			}else if(couponType = 'COUPON_TYPE.SHIPPING_PRICE'){
				paymentType = $('#paymentTypeShippingPrice').getRadioVal();
			}


			if(	paymentType  == 'PAYMENT_TYPE.CHECK_PAYMENT'){
				if($('#discountType_0').prop("checked") == true){
					if(issueQty >0 && percentageMaxDiscountAmount>0 && person >0){
						//계정발급
						budget = issueQty*percentageMaxDiscountAmount*person;
					}
				}else{
					if(issueQty >0 && discountValueFixed>0 && person >0){
						budget = issueQty*discountValueFixed*person;
					}
				}
			}else{
				if($('#discountType_0').prop("checked") == true){
					if(issueQty >0 && percentageMaxDiscountAmount>0){
						//정률할인
						budget = issueQty*percentageMaxDiscountAmount;
					}
				}else {
					if(issueQty >0 && discountValueFixed>0){
						//정률할인
						budget = issueQty*discountValueFixed;
					}
				}

				if(couponType == 'COUPON_TYPE.SHIPPING_PRICE'){
					var discountValueCart = Number($('#discountValueCart').val());
					budget = issueQty*discountValueCart;
				}

				if(couponType == 'COUPON_TYPE.SALEPRICE_APPPOINT'){
					var discountValueSalePrice = Number($('#discountValueSalePrice').val());
					budget = issueQty*discountValueSalePrice;
				}
			}

			$('#budget').text(budget.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + '원');
		}
	});


	$("input[name=discountType]").change(function() {

		var qty;
		var issueQty;
		var percentageMaxDiscountAmount;
		var discountValueFixed;
		var discountType;
		var person;
		var budget= '';

		issueQty = Number($('#issueQty').val());
		percentageMaxDiscountAmount = Number($('#percentageMaxDiscountAmount').val());
		discountType = $('#discountType').val();
		person = $("#accountGrid").data("kendoGrid").dataSource.total();
		discountValueFixed = Number($('#discountValueFixed').val());

		var couponType = $('#couponType').getRadioVal();
		var paymentTypeShippingPrice;
		var paymentTypeCart;
		var paymentType;

		if(couponType == 'COUPON_TYPE.GOODS'){
			paymentType = $('#paymentType').getRadioVal();
		}else if(couponType == 'COUPON_TYPE.CART'){
			paymentType = $('#paymentTypeCart').getRadioVal();
		}else if(couponType = 'COUPON_TYPE.SHIPPING_PRICE'){
			paymentType = $('#paymentTypeShippingPrice').getRadioVal();
		}


		if(	paymentType  == 'PAYMENT_TYPE.CHECK_PAYMENT'){
			if($('#discountType_0').prop("checked") == true){
				if(issueQty >0 && percentageMaxDiscountAmount>0 && person >0){
					//계정발급
					budget = issueQty*percentageMaxDiscountAmount*person;
				}
			}else{
				if(issueQty >0 && discountValueFixed>0 && person >0){
					budget = issueQty*discountValueFixed*person;
				}
			}
		}else{
			if($('#discountType_0').prop("checked") == true){
				if(issueQty >0 && percentageMaxDiscountAmount>0){
					//정률할인
					budget = issueQty*percentageMaxDiscountAmount;
				}
			}else {
				if(issueQty >0 && discountValueFixed>0){
					//정률할인
					budget = issueQty*discountValueFixed;
				}
			}
		}

		if($("#issueQtyType_0").prop("checked") == true){
			$('#budget').text(budget.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + '원');
		}else{
			$('#budget').text('제한없음');
		}
	});

	$("input[name=couponType]").change(function() {
		var couponType = $('#couponType').getRadioVal();
		var discountValueCart = Number($('#discountValueCart').val());
		var issueQty = Number($('#issueQty').val());
		var discountValueSalePrice = Number($('#discountValueSalePrice').val());
		var budget = '';
		if(couponTYpe =='COUPON_TYPE.SHIPPING_PRICE'){
			budget =  discountValueCart*issueQty;
			$('#budget').text(budget.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + '원');
		}else if(couponTYpe =='COUPON_TYPE.SALEPRICE_APPPOINT'){
			$('#totalBudget').hide();
		}

	});

	$("input[name=discountValueCart]").change(function() {
		var discountValueCart = Number($('#discountValueCart').val());
		var issueQty = Number($('#issueQty').val());
		var budget = '';

		budget =  discountValueCart*issueQty;
		if(discountValueCart == 0){
			$('#totalBudget').hide();
			$('#budget').hide();
		}else if(budget > 0){
			$('#totalBudget').show();
			$('#budget').show();
			$('#budget').text(budget.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + '원');
		}
	});

	$("input[name=discountValueSalePrice]").change(function() {
		var discountValueSalePrice = Number($('#discountValueSalePrice').val());
		var issueQty = Number($('#issueQty').val());
		var budget = '';

		budget =  discountValueSalePrice*issueQty;
		$('#budget').text(budget.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + '원');
	});


	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'cpnMgmAdd',
			callback : fnUI
		});

	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitMGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnInitMGridAfter();

		fnCoverageInit();

		fnAppintCoverageInit();

		fnAccountUploadInit();

		fnTicketUploadInit();

		fnAprpAdminInit();

		fnInitKendoUserUpload();

		fnInitKendoTicketUpload();

		fnOrgSetInit();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnClear, #fnSave, #fnList, #fnDeptPopupButton, #fnAddCoverage, #fnSamepleFormDownload, #fnTicketSamepleFormDownload').kendoButton();

		$('#fnCheckRequestApproval, #fnWarehousePopupButton, #fnSelectUser, #fnTicketExcelUpload, #fnUseExcelUpload, #fnAppintAddCoverage').kendoButton();

		$('#fnApprAdmin, #fnApprClear').kendoButton();
	}

	// 초기화
	function fnClear(){
		$('#inputForm').formClear(true);

	}

	// 목록화면 이동
	function fnList(){
		var option = new Object();
		option.url = "#/cpnMgm";
		option.menuId = 165;
		$scope.$emit('goPage', option);
	}

	$("#RATE_SERVICE").focusout(function(){
		if($("#RATE_TYPE").getRadioVal()=="1"){
			var s = parseInt($("#RATE_SERVICE").val());
			if(s > 50){
				fnKendoMessage({message : fnGetLangData({key :"6279",nullMsg :'분담금 설정의 할인율이 50%를 초과했습니다. <br>유의하여 작업해주세요.'})});
			}
		}
	});

	$("#RATE_SELLER").focusout(function(){
		if($("#RATE_TYPE").getRadioVal()=="1"){
			var s = parseInt($("#RATE_SELLER").val());
			if(s > 50){
				fnKendoMessage({message : fnGetLangData({key :"6279",nullMsg :'분담금 설정의 할인율이 50%를 초과했습니다. <br>유의하여 작업해주세요.'})});
			}
		}
	});

	// 계정발급 - 회원선택
	function fnSelectUser(pmCouponId){

		fnKendoPopup({
			id     : 'cpnMgmIssueList',
			title  : '쿠폰발급',
			src    : '#/cpnMgmIssueList',
			width  : '1100px',
			height : '900px',
			param  : { "pmCouponId" : pmCouponId},
			success: function(id, data ){
				accountUploadData = data;
				$("#accountGrid").data("kendoGrid").dataSource.data( data.rows );

				$("#uploadAccountViewControl").show();
                $("#uploadAccountLink").text('총 발급 회원 수 : ' + $("#accountGrid").data("kendoGrid").dataSource.total() + '명');
                $('#accountGridCountTotalSpan').text($("#accountGrid").data("kendoGrid").dataSource.total()  );
			}
		});
	}


	var fileTagId;
    var file;

    // 회원정보 업로드 Start
    function fnInitKendoUserUpload() {

      var uploadFileTagIdList = ['uploadUserFile'];

      var selectFunction = function(e) {
        if (e.files && e.files[0]) {
          // 엑셀 파일 선택시
          $("#fileInfoUserDiv").text(e.files[0].name);
          // --------------------------------------------------------------------
          // 확장자 2중 체크 위치
          // --------------------------------------------------------------------
          //  켄도 이미지 업로드 확장자 검사
          if(!validateExtension(e)) {
            fnKendoMessage({
              message : '허용되지 않는 확장자 입니다.',
              ok : function(e) {}
            });
            return;
          }

          fileTagId = e.sender.element[0].id;
          let reader = new FileReader();

          reader.onload = function(ele) {
              var itemImageScr = ele.target.result; // FileReader 로 생성한 상품 이미지 url
              file = e.files[0].rawFile; // kendoUpload 로 가져온 상품 이미지 file 객체

              fnExcelUserUpload();
          };

          reader.readAsDataURL(e.files[0].rawFile);

        } // End of if (e.files && e.files[0])
      } // End of var selectFunction = function(e)

      for (var i = 0; i < uploadFileTagIdList.length; i++) {
        fnKendoUpload({
            id : uploadFileTagIdList[i]
          , select : selectFunction
        });
      } // End of for (var i = 0; i < uploadFileTagIdList.length; i++)
    }


    // 이용권 번호 파일 업로드 Start
    function fnInitKendoTicketUpload() {

      var uploadFileTagIdList = ['uploadTicketFile'];

      var selectFunction = function(e) {
        if (e.files && e.files[0]) {
          // 엑셀 파일 선택시
          $("#fileInfoTicketDiv").text(e.files[0].name);
          // --------------------------------------------------------------------
          // 확장자 2중 체크 위치
          // --------------------------------------------------------------------
          //  켄도 이미지 업로드 확장자 검사
          if(!validateExtension(e)) {
            fnKendoMessage({
              message : '허용되지 않는 확장자 입니다.',
              ok : function(e) {}
            });
            return;
          }

          fileTagId = e.sender.element[0].id;
          let reader = new FileReader();

          reader.onload = function(ele) {
              var itemImageScr = ele.target.result; // FileReader 로 생성한 상품 이미지 url
              file = e.files[0].rawFile; // kendoUpload 로 가져온 상품 이미지 file 객체

              fnExcelTicketUpload();
          };

          reader.readAsDataURL(e.files[0].rawFile);

        } // End of if (e.files && e.files[0])
      } // End of var selectFunction = function(e)

      for (var i = 0; i < uploadFileTagIdList.length; i++) {
        fnKendoUpload({
            id : uploadFileTagIdList[i]
          , select : selectFunction
        });
      } // End of for (var i = 0; i < uploadFileTagIdList.length; i++)
    }


    // 분담조직정보 설정
    function fnOrgSetInit(){

    	fnAjax({
			url     : '/admiun/pm/cpnMgm/getOrgInfo',
			params : {"stCommonCodeMasterCode" : "PROMOTION_ORG_VALUE", "useYn" :"Y"},
			success :
				function( data ){

					$('#erpOrganizationName').val(data.rows[0].NAME);
					$('#erpOrganizationCode').val(data.rows[1].NAME);
					$('#erpRegalName').val(data.rows[2].NAME);
					$('#erpRegalCode').val(data.rows[4].NAME);
				},
				isAction : 'batch'
		});
    }


    // ==========================================================================
    // # 파일업로드-validateExtension
    // ==========================================================================
    function validateExtension(e) {

        var allowedExt = '';
        var ext = e.files[0].extension;
        var $el = e.sender.element;

        if( !$el.length ) return;

        if( $el[0].accept && $el[0].accept.length ) {
          // 공백 제거
          allowedExt = $el[0].accept.replace(/\s/g, '');
          allowedExt = allowedExt.split(',');
        } else {
          allowedExt = allowedImageExtensionList;
        }

        return allowedExt.includes(ext);
    };


    // 회원정보 파일 업로드 이벤트
    function fnExcelUserUpload(event) {

    	if(file == undefined || file == ""){
            fnKendoMessage({
                message : "파일을 선택해주세요.",
                ok : function(e) {
                }
            });
            return;
        }

        var formData = new FormData();
        formData.append('bannerImage', file);

        $.ajax({
            url         : '/admin/pm/cpnMgm/addUserExcelUpload'
            , data        : formData
            , type        : 'POST'
            , contentType : false
            , processData : false
            , async       : false
			, beforeSend : function(xhr) {
				xhr.setRequestHeader('authMenuID', CLICKED_MENU_ID);
			}
            , success     : function(id, data) {
                    let localMessage = "";
                    if(data.code == 'FILE_ERROR' || data.code == 'EXCEL_TRANSFORM_FAIL' || data.code == 'EXCEL_UPLOAD_NONE'){
                        localMessage = data.message;
                    }else{
                    	var fileName = file.name;
                        localMessage = "[결과] : " + data.message + "<BR>"
        				$("#accountGrid").data("kendoGrid").dataSource.data( id.data.rows );
        				$("#uploadAccountViewControl").show();
        				$("#userUploadFileName").show();
                        $("#uploadAccountLink").text('총 발급 회원 수 : ' + $("#accountGrid").data("kendoGrid").dataSource.total() + '명');
                        $('#accountGridCountTotalSpan').text($("#accountGrid").data("kendoGrid").dataSource.total()  );
                        $("#userUploadFileName").text('파일명 :' + fileName);
                    }
                  }
        });
    }

    // 이용권번호 파일 업로드 이벤트
    function fnExcelTicketUpload(event) {

    	if(file == undefined || file == ""){
            fnKendoMessage({
                message : "파일을 선택해주세요.",
                ok : function(e) {
                }
            });
            return;
        }

        var formData = new FormData();
        formData.append('bannerImage', file);

        $.ajax({
            url         : '/admin/pm/cpnMgm/addTicketExcelUpload'
            , data        : formData
            , type        : 'POST'
            , contentType : false
            , processData : false
            , async       : false
            , beforeSend : function(xhr) {
                        				xhr.setRequestHeader('authMenuID', CLICKED_MENU_ID);
                        			}
            , success     : function(id, data) {
                    let localMessage = "";
                    if(data.code == 'FILE_ERROR' || data.code == 'EXCEL_TRANSFORM_FAIL' || data.code == 'EXCEL_UPLOAD_NONE'){
                        localMessage = data.message;
                    }else{

                    	var fileName = file.name;
                    	$("#ticketGrid").data("kendoGrid").dataSource.data(id.data.rows);
                        $("#uploadTicketViewControl").show();
                        $("#ticketUploadFileName").show();
                        $("#uploadTicketLink").text('이용권 생성 개수 : ' + $("#ticketGrid").data("kendoGrid").dataSource.total() + '건');
                        $('#ticketGridCountTotalSpan').text($("#ticketGrid").data("kendoGrid").dataSource.total()  );
                        $("#ticketUploadFileName").text('파일명 :' + fileName);

                        $('#issueQty').val($("#ticketGrid").data("kendoGrid").dataSource.total());
                    }

                  }
        });
    }
    // # 파일업로드 End
    // ##########################################################################

    // 업로드 회원등록 버튼 클릭
    function fnUseExcelUpload(){
        $("#uploadUserFile").trigger("click");
    }

    // 이용권 파일업로드 (난수)
    function fnTicketExcelUpload(){
    	$("#uploadTicketFile").trigger("click");
    }


    // 회원 엑셀 업로드 (SheetJs)
    function excelExport(event) {

        // Excel Data => Javascript Object 로 변환
        var input = event.target;
        var reader = new FileReader();

        var fileName = event.target.files[0].name;

        reader.onload = function() {
            var fileData = reader.result;
            var wb = XLSX.read(fileData, {
                type : 'binary'
            });

            wb.SheetNames.forEach(function(sheetName) {
                var excelData = XLSX.utils.sheet_to_json(wb.Sheets[sheetName]);

                $("#accountGrid").data("kendoGrid").dataSource.data(excelData);
                $("#uploadAccountViewControl").show();
                $("#userUploadFileName").show();
                $("#uploadAccountLink").text('총 발급 회원 수 : ' + $("#accountGrid").data("kendoGrid").dataSource.total() + '명');
                $('#accountGridCountTotalSpan').text($("#accountGrid").data("kendoGrid").dataSource.total()  );
                $("#userUploadFileName").text('파일명 :' + fileName);
            })
        };

        reader.readAsBinaryString(input.files[0]);
    }



    // 이용권 엑셀 업로드 (SheetJs)
    function excelTicketExport(event) {

        // Excel Data => Javascript Object 로 변환
        var input = event.target;
        var reader = new FileReader();

        var fileName = event.target.files[0].name;

        reader.onload = function() {
            var fileData = reader.result;
            var wb = XLSX.read(fileData, {
                type : 'binary'
            });

            wb.SheetNames.forEach(function(sheetName) {
                var excelData = XLSX.utils.sheet_to_json(wb.Sheets[sheetName]);

                $("#ticketGrid").data("kendoGrid").dataSource.data(excelData);
                $("#uploadTicketViewControl").show();
                $("#ticketUploadFileName").show();
                $("#uploadTicketLink").text('이용권 생성 개수 : ' + $("#ticketGrid").data("kendoGrid").dataSource.total() + '건');
                $('#ticketGridCountTotalSpan').text($("#ticketGrid").data("kendoGrid").dataSource.total()  );
                $("#ticketUploadFileName").text('파일명 :' + fileName);
            })
        };


        reader.readAsBinaryString(input.files[0]);
    }

	// 저장
	function fnSave(){

		var url  = '/admin/pm/cpnMgm/addCoupon';
		var cbId = 'insert';
		var couponType;

		if($("#couponType_0").prop("checked")){
			couponType = 'COUPON_TYPE.GOODS';
		}else if($("#couponType_1").prop("checked")){
			couponType = 'COUPON_TYPE.CART';
		}else if($("#couponType_2").prop("checked")){
			couponType = 'COUPON_TYPE.SHIPPING_PRICE';
		}else if($("#couponType_3").prop("checked")){
			couponType = 'COUPON_TYPE.SALEPRICE_APPPOINT';
		}


		var issueQty = Number($('#issueQty').val());


		var paymentType;
		if(couponType == 'COUPON_TYPE.GOODS'){
			if($("#paymentType_0").prop("checked")){
				paymentType = 'PAYMENT_TYPE.GOODS_DETAIL';
			}else if($("#paymentType_1").prop("checked")){
				paymentType = 'PAYMENT_TYPE.DOWNLOAD';
			}else if($("#paymentType_2").prop("checked")){
				paymentType = 'PAYMENT_TYPE.AUTO_PAYMENT';
			}else if($("#paymentType_3").prop("checked")){
				paymentType = 'PAYMENT_TYPE.CHECK_PAYMENT';
			}else if($("#paymentType_4").prop("checked")){
				paymentType = 'PAYMENT_TYPE.TICKET';
			}
		}else if(couponType == 'COUPON_TYPE.SHIPPING_PRICE'){
			if($("#paymentTypeShippingPrice_0").prop("checked")){
				paymentType = 'PAYMENT_TYPE.DOWNLOAD';
			}else if($("#paymentTypeShippingPrice_1").prop("checked")){
				paymentType = 'PAYMENT_TYPE.AUTO_PAYMENT';
			}else if($("#paymentTypeShippingPrice_2").prop("checked")){
				paymentType = 'PAYMENT_TYPE.CHECK_PAYMENT';
			}
		}else if(couponType == 'COUPON_TYPE.CART'){
			if($("#paymentTypeCart_0").prop("checked")){
				paymentType = 'PAYMENT_TYPE.DOWNLOAD';
			}else if($("#paymentTypeCart_1").prop("checked")){
				paymentType = 'PAYMENT_TYPE.AUTO_PAYMENT';
			}else if($("#paymentTypeCart_2").prop("checked")){
				paymentType = 'PAYMENT_TYPE.CHECK_PAYMENT';
			}else if($("#paymentTypeCart_3").prop("checked")){
				paymentType = 'PAYMENT_TYPE.TICKET';
			}
		}else{
			paymentType = 'PAYMENT_TYPE.AUTO_PAYMENT';
		}



		var ticketType;
		if($("#ticketType_0").prop("checked")){
			ticketType = 'SERIAL_NUMBER_TYPE.AUTO';
		}else{
			ticketType = 'SERIAL_NUMBER_TYPE.FIXED_VALUE';
		}
		if(paymentType =='PAYMENT_TYPE.TICKET'){				// 발급방법 : 이용권
			if(ticketType == 'SERIAL_NUMBER_TYPE.AUTO'){
				var ticketIssueType = $('#ticketIssueType').val();
				$('#serialNumberType').val(ticketIssueType);

				// 단일코드
				if($('#serialNumber').val() == ''){
					$('#serialNumber').val('_');
				}

			}else{												// 단일코드 입력
				$('#serialNumberType').val(ticketType);
			}
		}else{
			// 단일코드
			if($('#serialNumber').val() == ''){
				$('#serialNumber').val('_');
			}
		}

		if(couponType == 'COUPON_TYPE.SHIPPING_PRICE' || couponType == 'COUPON_TYPE.SALEPRICE_APPPOINT'){
			ticketType = '';
			$('#serialNumber').val('_');
		}

		var uploadUser = kendo.stringify( accountGrid._data );

		$('#uploadUser').val(uploadUser);

		var uploadTicket = kendo.stringify( ticketGrid._data );

		$('#uploadTicket').val(uploadTicket);



		if($("#validityType_0").prop("checked")){
			$('#validityDay').val('_');
		}else{
			$('#validityStartDate').val('_');
			$('#validityEndDate').val('_');
		}

		if($("#discountType_0").prop("checked")){
			$('#discountValueFixed').val('_');
		}else{
			$('#discountValuePercent').val('_');
			$('#percentageMaxDiscountAmount').val('_');
		}

		if(couponType == 'COUPON_TYPE.SALEPRICE_APPPOINT' ){
			$('#discountValueFixed').val('_');
			$('#discountValuePercent').val('_');
			$('#percentageMaxDiscountAmount').val('_');
			$('#minPaymentAmount').val('_');
			$("#discountType_1").prop("checked",true);

		}else if(couponType == 'COUPON_TYPE.SHIPPING_PRICE'){
			$('#discountType').val('_');
			$('#discountValueFixed').val('_');
			$('#discountValuePercent').val('_');
			$('#percentageMaxDiscountAmount').val('_');
			$('#discountValueSalePrice').val('_');
		}else if(couponType == 'COUPON_TYPE.GOODS'){
			$('#discountValueSalePrice').val('_');
		}else if(couponType == 'COUPON_TYPE.CART'){
			$('#discountValueSalePrice').val('_');
		}

		if(couponType != 'COUPON_TYPE.SHIPPING_PRICE'){
			$('#discountValueCart').val('_');
		}


		if($("#issueQtyType_1").prop("checked") == true){
			$("#issueQty").val('N');
		}

		// 자동발급 타입 '이벤트'인 경우 쿠폰 제한 선택 안함
		if($("#autoIssueType").val() == 'AUTO_ISSUE_TYPE.EVENT_AWARD'){
			$("#issueQtyLimit").val(0);
			$("#autoIssueQtyLimit").val(0);
		}

		// 자동발급 쿠폰 발급 수량 체크
		if(paymentType == 'PAYMENT_TYPE.AUTO_PAYMENT'){
			$("#issueQtyLimit").val('∀');
		}else{
			$("#autoIssueQtyLimit").val('∀');
		}

		// 자동발급 케이스
		if(paymentType != 'PAYMENT_TYPE.AUTO_PAYMENT'){
			$('#autoIssueType').val('N');
		}


		var insertData;
		if(couponType =="COUPON_TYPE.SALEPRICE_APPPOINT"){			// 판매가 지정
			insertData = kendo.stringify(appintCoverageGrid._data);
			if(insertData == undefined || insertData == '[]'){
				$('#appintCategoryType').val('');
			}else{
				$('#appintCategoryType').val('N');
			}
			$('#goodsCategoryType').val('N');
			$('#shippingCategoryType').val('N');
		}else if(couponType =="COUPON_TYPE.SHIPPING_PRICE"){		// 배송비
			insertData = kendo.stringify(cartCoverageGrid._data);
			if(insertData == undefined){
				 $('#shippingCategoryType').val('');
			}else{
				$('#shippingCategoryType').val('N');
			}
			$('#goodsCategoryType').val('N');
			$('#appintCategoryType').val('N');
		}else{		// 상품, 장바구니
			insertData = kendo.stringify(ctgryGrid._data);

			if(couponType == 'COUPON_TYPE.GOODS'  && paymentType == 'PAYMENT_TYPE.GOODS_DETAIL'){		// 쿠폰종류:상품 , 발급방법:상품상세발급
				if(insertData == undefined || insertData == '[]'){
					 $('#goodsCategoryType').val('');
				}else{
					$('#goodsCategoryType').val('N');
				}
				$('#shippingCategoryType').val('N');
				$('#appintCategoryType').val('N');
			}else{
				$('#goodsCategoryType').val('N');
				$('#shippingCategoryType').val('N');
				$('#appintCategoryType').val('N');
			}
		}


		// 발급방법 : 계정발급
		if(	paymentType  != 'PAYMENT_TYPE.CHECK_PAYMENT'){
			if($('#issueReason').val() == ''){
				$('#issueReason').val('_');
			}
		}

		var usePcYn = $('#usePcYn_0').prop("checked");
		var useMobileWebYn = $('#useMobileWebYn_0').prop("checked");
		var useMobileAppYn = $('#useMobileAppYn_0').prop("checked");

		if(usePcYn==false && useMobileWebYn==false &&useMobileAppYn == false){
			$('#pcMobileCheck').val('');
		}else{
			$('#pcMobileCheck').val('N');
		}


// ********************************************************************************************

		var data = $('#inputForm').formSerialize(true);

// ********************************************************************************************

		if($('#issueQty').val() == 'N'){
			$('#issueQty').val('');
		}


		if($('#validityDay').val() == '_'){
			$('#validityDay').val('');
			data.validityDay = '';
		}


		if($('#validityStartDate').val() == '_'){
			$('#validityStartDate').val('');
			data.validityStartDate = '';
		}

		if($('#validityEndDate').val() == '_'){
			$('#validityEndDate').val('');
			data.validityEndDate = '';
		}


		if($('#discountValueFixed').val() == '_'){
			$('#discountValueFixed').val('');
			data.discountValueFixed = '';
		}

		if($('#discountValuePercent').val() == '_'){
			$('#discountValuePercent').val('');
			data.discountValuePercent = '';
		}

		if($('#percentageMaxDiscountAmount').val() == '_'){
			$('#percentageMaxDiscountAmount').val('');
			data.percentageMaxDiscountAmount = '';
		}

		if($('#issueReason').val() == '_'){
			$('#issueReason').val('');
			data.issueReason = '';
		}

		if($('#serialNumber').val() == '_'){
			$('#serialNumber').val('');
			data.serialNumber = '';
		}

		if($('#discountValueFixed').val() == '_'){
			$('#discountValueFixed').val('');
			data.discountValueFixed = '';
		}

		if($('#percentageMaxDiscountAmount').val() == '_'){
			$('#percentageMaxDiscountAmount').val('');
			data.percentageMaxDiscountAmount = '';
		}

		if($('#discountValuePercent').val() == '_'){
			$('#discountValuePercent').val('');
			data.discountValuePercent = '';
		}

		if($('#discountValueCart').val() == '_'){
			$('#discountValueCart').val('');
			data.discountValueCart = '';
		}

		if($('#discountType').val() == '_'){
			$('#discountType').val('');
			data.discountType = '';
		}

		if($('#discountValueSalePrice').val() == '_'){
			$('#discountValueSalePrice').val('');
			data.discountValueSalePrice = '';
		}

        if(Number(data.discountValueSalePrice) > 0 ){
            data.discountVal = data.discountValueSalePrice;
            data.discountType ='COUPON_DISCOUNT_STATUS.PERCENTAGE_DISCOUNT';  // 판매가지정 인 경우 쿠폰할인방식 타입 을 정률값으로 설정 함
        }

		if(couponType != 'COUPON_TYPE.CART'){
			data.pgPromotionYn= '';
		}


		if($('#minPaymentAmount').val() == '_'){
			$('#minPaymentAmount').val('');
			data.minPaymentAmount = 0;
		}

		data.issueQtyLimit = $('#issueQtyLimit').val();

		if(paymentType == 'PAYMENT_TYPE.AUTO_PAYMENT'){
			data.issueQtyLimit = $('#autoIssueQtyLimit').val();
			if($('#issueQtyLimit').val() == '∀'){
				$('#issueQtyLimit').val('');
			}
		}else{
			data.issueQtyLimit = $('#issueQtyLimit').val();
			if($('#autoIssueQtyLimit').val() == '∀'){
				$('#autoIssueQtyLimit').val('');
			}
		}


		if(issueQty == 0){
			data.issueQty = 0;
		}

		if($("#issueQtyType_1").prop("checked") == true){
			$("#issueQty").val('');
			data.issueQty = 0;
		}


		if($('#autoIssueType').val() == 'N'){
			$('#autoIssueType').val('');
			data.autoIssueType = '';
		}

		if($('#goodsCategoryType').val() == 'N'){
			$('#goodsCategoryType').val('');
			data.goodsCategoryType = '';
		}

		if($('#shippingCategoryType').val() == 'N'){
			$('#shippingCategoryType').val('');
			data.shippingCategoryType = '';
		}

		if($('#appintCategoryType').val() == 'N'){
			$('#appintCategoryType').val('');
			data.appintCategoryType = '';
		}

		if($('#pcMobileCheck').val() == 'N'){
			$('#pcMobileCheck').val('');
			data.pcMobileCheck = '';
		}


		// 21-03-26 : 발급예산 0원 처리
//		if(couponType == 'COUPON_TYPE.SALEPRICE_APPPOINT'){
//			data.issueBudget = 0;
//		}
		data.issueBudget = 0;


		if(!data.rtnValid){
			return;
		}

		if(couponType != 'COUPON_TYPE.SALEPRICE_APPPOINT'){
//			var issueBudget = Number($('#issueBudget').val());
			var percentageMaxDiscountAmount = Number($('#percentageMaxDiscountAmount').val());

			var discountValueFixed = Number($('#discountValueFixed').val());
			var discountType;
			if($("#discountType_0").prop("checked")){
				discountType = 'COUPON_DISCOUNT_STATUS.PERCENTAGE_DISCOUNT';
			}else{
				discountType = 'COUPON_DISCOUNT_STATUS.FIXED_DISCOUNT';
			}
			var value;
			if(discountType == 'COUPON_DISCOUNT_STATUS.PERCENTAGE_DISCOUNT'){		//할인방식 : 정률

				if(couponType != 'COUPON_TYPE.SHIPPING_PRICE'){
					if(percentageMaxDiscountAmount == 0){
						fnKendoMessage({message:'최대할인금액이 입력되지 않았습니다.'});
						return;
					}
				}
			}else{																// 할인방식 : 정액
				var minPaymentAmount = Number($('#minPaymentAmount').val());
			}
		}



//		if(paymentType == 'PAYMENT_TYPE.AUTO_PAYMENT'){
//			if(Number($('#issueQtyLimit').val()) < 1){
//				fnKendoMessage({message:'쿠폰발급수량 제한은 1건 이상 입력해야 합니다.'});
//				return;
//			}
//		}


		$('#insertData').val(insertData);
		data.insertData = insertData;

		if($("#issueQtyType_1").prop("checked") == false){		// 발급수량 : 제한

			var limit = $('#issueQtyLimit').val().split('.');

			var issueCnt = $("#accountGrid").data("kendoGrid").dataSource.total();

			if(	paymentType  != 'PAYMENT_TYPE.CHECK_PAYMENT'){
				// 발급수량 유효성 체크 (발급수량: 제한)
				if($('#autoIssueType').val() != 'AUTO_ISSUE_TYPE.EVENT_AWARD' && Number($('#issueQty').val()) < Number(limit[1])){
					fnKendoMessage({message:'발급 수량을 확인해주세요.<br> (쿠폰발급수량제한 이상 입력해주셔야 합니다.)'});
					return
				}
			}

		}else{													// 발급수량 : 무제한

			if(	paymentType  == 'PAYMENT_TYPE.CHECK_PAYMENT'){

				if($("#accountGrid").data("kendoGrid").dataSource.total() < 1){
					fnKendoMessage({message:'계정발급 대상을 입력해주세요'});
					return
				}

				if(paymentType == 'PAYMENT_TYPE.CHECK_PAYMENT' && $('#issueReason').val() == ''){
					fnKendoMessage({message:'계정발급 사유를 입력해주세요.'});
					return;
				}
			}
		}


		//승인요청 처리 선택 구분
		if($("#approvalCheckbox").prop("checked")){
			data.approvalCheck = 'Y';
			if($('#apprUserId').val() == ''){
				fnKendoMessage({message:'승인관리자가 지정되지 않았습니다.'});
				return;
			}
		}else{
			data.approvalCheck = 'N';
		}


		if( data.rtnValid ){

			let issueStartDt = Number($('#issueStartDate').val().replaceAll('-',''));

			let todatyDt = Number(fnGetToday().replaceAll('-',''));

			// 발급기간 시작일 체크
			if(issueStartDt < todatyDt || issueStartDt > '29991231'){
				fnKendoMessage({message:'발급기간 시작일이 유효하지 않습니다.'});
				return;
			}

			let issueEndDt = Number($('#issueEndDate').val().replaceAll('-',''));

			// 발급기간 종료일 체크
			if(issueEndDt < todatyDt || issueEndDt > '29991231'){
				fnKendoMessage({message:'발급기간 종료일이 유효하지 않습니다.'});
				return;
			}


			if($("#validityType_0").prop("checked")){

				let validityStartDt = Number($('#validityStartDate').val().replaceAll('-',''));

				// 유효기간 시작일 체크
				if(validityStartDt < '19000101' || validityStartDt > '29991231'){
					fnKendoMessage({message:'유효기간 시작일이 유효하지 않습니다.'});
					return;
				}

				let validityEndDt = Number($('#validityEndDate').val().replaceAll('-',''));

				// 유효기간 종료일 체크
				if(validityEndDt < '19000101' || validityEndDt > '29991231'){
					fnKendoMessage({message:'유효기간 종료일이 유효하지 않습니다.'});
					return;
				}
			}


			// 단일코드 체크
			if( ticketType == 'SERIAL_NUMBER_TYPE.FIXED_VALUE' && data.serialNumber.length <5 ){
				fnKendoMessage({message:'단일코드는 최소5자리 이상 입력해야 합니다.'});
				return;
			}

			// 발급기간 시작일 체크
			if(data.validityType == 'VALIDITY_TYPE.PERIOD' && (data.issueStartDate > data.validityStartDate)){
				fnKendoMessage({message:'발급일자보다 유효기간 과거 입니다. 다시 설정해주세요.'});
				return;
			}

			// 발급기간 종료일 체크
			if(data.validityType == 'VALIDITY_TYPE.PERIOD' && (data.issueEndDate > data.validityEndDate)){
				fnKendoMessage({message:'유효기간은 발급기간보다 길어야 합니다. 유효기간을 수정 해 주세요.'});
				return;
			}

			if(data.validityType == 'VALIDITY_TYPE.VALIDITY' && $('#validityDay').val() == ''){
				fnKendoMessage({message:'유효기간을 확인해주세요.'});
				return;
			}

			// 최소 결제 금액 체크
			if(data.discountType == 'COUPON_DISCOUNT_STATUS.FIXED_DISCOUNT'){
				if(data.minPaymentAmount != 0 && Number(data.discountValueFixed) > Number(data.minPaymentAmount)){
					fnKendoMessage({message:'정액 할인 금액이 최소 결제금액보다 높습니다.'});
					return;
				}
			}


			// 발급방법 : 이용권 && 엑셀 업로드
			// 이용권 엑셀 업로드 체크
			if(paymentType == 'PAYMENT_TYPE.TICKET' && data.ticketIssueType == 'SERIAL_NUMBER_TYPE.EXCEL_UPLOAD'){
				if( $("#ticketGrid").data("kendoGrid").dataSource.total() == 0){
					fnKendoMessage({message : '엑셀업로드를 진행해주세요.'});
					return;
				}
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



	// 승인요청 채크
	function fnCheckRequestApproval(){

	}


	// 조직검색 팝업 호출
	function fnDeptPopupButton(){

		fnKendoPopup({
			id     : 'deptPopup',
			title  : '조직검색',
			src    : '#/deptPopup',
			width  : '900px',
			height : '800px',
			param  : { },
			success: function( stMenuId, data ){
			    if(data.erpOrganizationCode != undefined){
					$('#erpOrganizationName').val(data.erpOrganizationName);
					$('#erpOrganizationCode').val(data.erpOrganizationCode);
					$('#erpRegalCode').val(data.finOrganizationCode);
					$('#erpRegalName').val(data.erpRegalName);
                }
			}
		});
	}

	//발급수량 제한 표시처리
	function fnIssueQtyLimit(){
		$('#issueQtyType_1').attr("disabled", true);
		$('#issueQtyType_0').attr("disabled", false);
		$('#issueQtyType_0').prop("checked", true);
		$("#issueQty").attr("disabled", false);
	}

	//발급수량 무제한 처리 (발급방법:자동발급[이벤트])
	function fnIssueQtyUnLimit(){
		$('#issueQtyType_0').attr("disabled", true);
		$('#issueQtyType_1').attr("disabled", false);
		$('#issueQtyType_1').prop("checked", true);
		$("#issueQty").attr("disabled", true);
	}

	//발급수량 제한/무제한 표시처리
	function fnIssueQtyNoneLimit(){
		$('#issueQtyType_1').attr("disabled", false);
		$('#issueQtyType_0').attr("disabled", false);
		$('#issueQtyType_0').prop("checked", true);
		$("#issueQty").attr("disabled", false);
	}




	//쿠폰발급수량제한 (발급방법:자동발급)
	function fnAutoIssueQtyLimit(){
		$("#issueQtyLimitView").hide();
		$("#eventView").hide();
		$('#autoIssueQtyLimitDiv').show();
		$("#autoIssueType").data('kendoDropDownList').value("");
	}

	//쿠폰발급수량제한 (발급방법:자동발급 외)
	function fnAutoIssueQtyUnLimit(){
		$("#issueQtyLimitView").show();
		$("#eventView").hide();
		$('#autoIssueQtyLimitDiv').hide();
		$("#autoIssueType").data('kendoDropDownList').value("");
		$("#issueQtyLimit").data('kendoDropDownList').value("");

	}


	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------

	function fnInitMGrid(){


		//유효기간 버튼제어
		$("#validityDay").attr("disabled", true);
		//할인방식 버튼제어
		$("#discountValueFixed").attr("disabled", true);

		$("#goodsPaymentTypeDiv").show();
		$("#cartPaymentTypeDiv").hide();
		$("#shippingPricePaymentTypeDiv").hide();
		$("#salepricePaymentTypeDiv").hide();
		$("#autoIssueTypeDiv").hide();
		$("#checkPaymentDiv").hide();
		$("#ticketDiv").hide();
		$("#organizationRemoveDiv").hide();
		$("#brandCoverageTypeDiv").hide();
		$("#categoryCoverageTypeDiv").hide();
		$('#shippingCoverageDiv').hide();
		$('#appintCoverageDiv').hide();
		$("#shippingCoverageTypeDiv").hide();

		//할인방식 (배송비, 판매가지정) hide
		$("#discountTypeShipping").hide();
		$("#discountTypeSalePrice").hide();
		$('#discountTypeSalePriceCoupon').hide();

		//제휴구분
		$('#paymentCartView').hide();
		$('#organizationDiv').hide();

		//승인관리자 정보
		$('#approvalCheckbox').prop("checked",true);
		$('#apprDiv').show();


	}


	function fnInitMGridAfter(){

		// 쿠폰종류: 상품, 발급방법:상품상세발급 화면제어
		$("#goodsCoverageType").data('kendoDropDownList').value("APPLYCOVERAGE.GOODS");
		$("#goodsCoverageType").data("kendoDropDownList").enable(false);

		$("#goodsIncludeYn").data('kendoDropDownList').value("Y");
		$("#goodsIncludeYn").data("kendoDropDownList").enable(false);

		//제휴구분 버튼제어
		$("#pgPromotionPayConfigId").data("kendoDropDownList").enable( false );
		$("#pgPromotionPayGroupId").data("kendoDropDownList").enable( false );
		$("#pgPromotionPayId").data("kendoDropDownList").enable( false );

		// 분담조직 제어
		$("#erpOrganizationName").attr("disabled", true);

		// 발급수량
		$("#issueQtyType_0").prop("checked",true);
		$("#issueQty").attr("disabled", false);

		$('#goodsIncludeYn').hide();

		$("#goodsIncludeYn").closest(".k-widget").hide();

		$("#eventView").hide();

		$('#autoIssueQtyLimitDiv').hide();

	}


	function fnClose(){
		parent.POP_PARAM = 'SAVE';
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	}



	// 승인관리자 선택 팝업 호출
	function fnApprAdmin(){
		var param  = {'taskCode' : 'APPR_KIND_TP.COUPON' };
		fnKendoPopup({
			id     : 'approvalManagerSearchPopup',
			title  : '승인관리자 선택',
			src    : '#/approvalManagerSearchPopup',
			param  : param,
			width  : '1300px',
			height : '800px',
			scrollable : "yes",
			success: function( stMenuId, data ){

				if(data && !fnIsEmpty(data) && data.authManager2nd){
					$('#apprGrid').gridClear(true);
					var authManager1 = data.authManager1st;
					var authManager2 = data.authManager2nd;

					if(authManager1.adminTypeName != undefined){
						var objManager1 = new Object();

						objManager1["apprAdminInfo"] = '1차 승인관리자';
						objManager1["adminTypeName"] = authManager1.adminTypeName;
						objManager1["apprUserName"] = authManager1.apprUserName;
						objManager1["apprLoginId"] = authManager1.apprLoginId;
						objManager1["organizationName"] = authManager1.organizationName;
						objManager1["userStatusName"] = authManager1.userStatusName;
						objManager1["teamLeaderYn"] = authManager1.teamLeaderYn;
						objManager1["grantAuthYn"] = authManager1.grantAuthYn;
						objManager1["grantUserName"] = authManager1.grantUserName;
						objManager1["grantLoginId"] = authManager1.grantLoginId;
						objManager1["grantAuthStartDt"] = authManager1.grantAuthStartDt;
						objManager1["grantAuthEndDt"] = authManager1.grantAuthEndDt;
						objManager1["grantUserStatusName"] = authManager1.grantUserStatusName;

						apprAdminGridDs.add(objManager1);
						$('#apprSubUserId').val(authManager1.apprUserId);
					}

					if(authManager2 != undefined){

						var objManager2 = new Object();

						objManager2["apprAdminInfo"] = '최종 승인관리자';
						objManager2["adminTypeName"] = authManager2.adminTypeName;
						objManager2["apprUserName"] = authManager2.apprUserName;
						objManager2["apprLoginId"] = authManager2.apprLoginId;
						objManager2["organizationName"] = authManager2.organizationName;
						objManager2["userStatusName"] = authManager2.userStatusName;
						objManager2["teamLeaderYn"] = authManager2.teamLeaderYn;
						objManager2["grantAuthYn"] = authManager2.grantAuthYn;
						objManager2["grantUserName"] = authManager2.grantUserName;
						objManager2["grantLoginId"] = authManager2.grantLoginId;
						objManager2["grantAuthStartDt"] = authManager2.grantAuthStartDt;
						objManager2["grantAuthEndDt"] = authManager2.grantAuthEndDt;
						objManager2["grantUserStatusName"] = authManager2.grantUserStatusName;

						apprAdminGridDs.add(objManager2);
						$('#apprUserId').val(authManager2.apprUserId);
					}
				}


			}
		});
	}

	// 승인관리자 그리드 초기화
	function fnApprClear(){
		$('#apprGrid').gridClear(true);
		$('#apprSubUserId').val('');
		$('#apprUserId').val('');
	}


	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	function fnInitOptionBox(){

		//쿠폰종류 선택 화면 제어
		fnTagMkRadio({
			id    : 'couponType',
			tagId : 'couponType',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "COUPON_TYPE", "useYn" :"Y"},
			async : false,
			chkVal: 'COUPON_TYPE.GOODS',
			style : {},
			change : function(e){
				if($('#couponType').getRadioVal() == "COUPON_TYPE.GOODS"){		//쿠폰종류 - 상품

					$("#goodsPaymentTypeDiv").show();
					$("#cartPaymentTypeDiv").hide();
					$("#shippingPricePaymentTypeDiv").hide();
					$("#salepricePaymentTypeDiv").hide();
					$('#goodsCoverageDiv').show();
					$('#shippingCoverageDiv').hide();
					$('#appintCoverageDiv').hide();
					$('#discountTypeGoods').show();
					$('#discountTypeShipping').hide();
					$('#discountTypeSalePrice').hide();
					$('#discountTypeSalePriceCoupon').hide();
					$("#minPaymentDiv").show();
					$('#paymentCartView').hide();

					fnInitMGridAfter();

					$("#paymentType_0").prop("checked",true);
					$('#ticketDiv').hide();
					$('#autoIssueTypeDiv').hide();
					fnRemoveEvntType();
					$('#checkPaymentDiv').hide();
					$("#fnAddCoverageGoods").show();
					$('#ctgryGrid').gridClear(true);
					$('#appintCoverageGrid').gridClear(true);
					$("#brandCoverageTypeDiv").hide();
					$('#categoryCoverageTypeDiv').hide();

					$('#totalBudget').show();
					$('#budget').show();

					//발급수량제한[자동발급 외 ] 표시
					fnAutoIssueQtyUnLimit();

					//발급수량 제한/무제한
					fnIssueQtyNoneLimit();

				}else if($('#couponType').getRadioVal() == "COUPON_TYPE.CART"){		//쿠폰종류 - 장바구니

					$("#goodsPaymentTypeDiv").hide();
					$("#cartPaymentTypeDiv").show();
					$("#shippingPricePaymentTypeDiv").hide();
					$("#salepricePaymentTypeDiv").hide();
					$('#goodsCoverageDiv').show();
					$('#shippingCoverageDiv').hide();
					$('#appintCoverageDiv').hide();
					$('#discountTypeGoods').show();
					$('#discountTypeShipping').hide();
					$('#discountTypeSalePrice').hide();
					$('#discountTypeSalePriceCoupon').hide();
					$("#minPaymentDiv").show();
					$('#paymentCartView').show();
					$("#paymentTypeCart_0").prop("checked",true);
					$('#ticketDiv').hide();
					$('#autoIssueTypeDiv').hide();
					fnRemoveEvntType();
					$('#checkPaymentDiv').hide();
					$("#goodsCoverageType").data('kendoDropDownList').value("APPLYCOVERAGE.ALL");
					$("#goodsCoverageType").data("kendoDropDownList").enable(true);
					$("#goodsIncludeYn").closest(".k-widget").show();
					$("#goodsIncludeYn").data('kendoDropDownList').value("Y");
					$("#goodsIncludeYn").data("kendoDropDownList").enable(false);
					$('#ctgryGrid').gridClear(true);
					$('#appintCoverageGrid').gridClear(true);
					$('#categoryCoverageTypeDiv').hide();

					$('#totalBudget').show();
					$('#budget').show();

					//발급수량제한[자동발급 외 ] 표시
					fnAutoIssueQtyUnLimit();

					//발급수량 제한/무제한
					fnIssueQtyNoneLimit();

				}else if($('#couponType').getRadioVal() == "COUPON_TYPE.SHIPPING_PRICE"){		//쿠폰종류 - 배송비

					$("#goodsPaymentTypeDiv").hide();
					$("#cartPaymentTypeDiv").hide();
					$("#shippingPricePaymentTypeDiv").show();
					$("#salepricePaymentTypeDiv").hide();
					$('#goodsCoverageDiv').hide();
					$('#shippingCoverageDiv').show();
					$('#appintCoverageDiv').hide();
					$('#discountTypeGoods').hide();
					$('#discountTypeShipping').show();
					$('#discountTypeSalePrice').hide();
					$('#discountTypeSalePriceCoupon').hide();
					$("#minPaymentDiv").show();
					$('#paymentCartView').hide();
					$("#paymentTypeShippingPrice_0").prop("checked",true);
					$('#ticketDiv').hide();
					$('#autoIssueTypeDiv').hide();
					fnRemoveEvntType();
					$('#checkPaymentDiv').hide();
					$("#shippingCoverageTypeDiv").show();
					$("#shippingCoverageType").data("kendoDropDownList").enable(false);
					$("#shippingIncludeYn").data("kendoDropDownList").enable(false);
					fnCartCoverageInit();
					$('#ctgryGrid').gridClear(true);
					$('#appintCoverageGrid').gridClear(true);
					$("#brandCoverageTypeDiv").hide();
					$('#categoryCoverageTypeDiv').hide();

					$('#totalBudget').show();
					$('#budget').show();

					//발급수량제한[자동발급 외 ] 표시
					fnAutoIssueQtyUnLimit();

					//발급수량 제한/무제한
					fnIssueQtyNoneLimit();
				}else if($('#couponType').getRadioVal() == "COUPON_TYPE.SALEPRICE_APPPOINT"){		//쿠폰종류 - 판매가지정

					$("#goodsPaymentTypeDiv").hide();
					$("#cartPaymentTypeDiv").hide();
					$("#shippingPricePaymentTypeDiv").hide();
					$("#salepricePaymentTypeDiv").show();
					$("#paymentTypeSaleprice_0").prop("checked",true);
					$("#autoIssueTypeDiv").show();
					fnAddEvntType();
					$('#checkPaymentDiv').hide();
					$('#goodsCoverageDiv').hide();
					$('#shippingCoverageDiv').hide();
					$('#appintCoverageDiv').show();
					$('#discountTypeGoods').hide();
					$('#discountTypeShipping').hide();
					$('#discountTypeSalePrice').show();
					$('#discountTypeSalePriceCoupon').show();
					$("#minPaymentDiv").hide();
					$('#paymentCartView').hide();
					$("#appintCoverageType").data("kendoDropDownList").enable(false);
					$('#ctgryGrid').gridClear(true);
					$('#appintCoverageGrid').gridClear(true);
					$("#brandCoverageTypeDiv").hide();
					$('#categoryCoverageTypeDiv').hide();

					$('#totalBudget').hide();
					$('#budget').hide();
					$('#ticketDiv').hide();


					//발급수량제한[자동발급 외 ] 표시
					fnAutoIssueQtyUnLimit();

					//발급수량 무제한 표시처리
					fnIssueQtyUnLimit();

					//쿠폰발급수량제한 (발급방법:자동발급)
					fnAutoIssueQtyLimit();
					$("#issueQty").val('');
				}

            }

		});



		fnTagMkRadio({
			id: "orgBaseValue" ,
			tagId : 'orgBaseValue',
			url : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "PROMOTION_ORG_VALUE", "useYn" :"Y"},
			beforeData : [
				{"CODE":"", "NAME":"전체"},
						],
			chkVal: '',
			style : {}
		});



		//자동발급 구분
		fnKendoDropDownList({
			id  : 'autoIssueType',
			tagId : 'autoIssueType',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "AUTO_ISSUE_TYPE", "useYn" :"Y"},
			textField :"NAME",
			valueField : "CODE",
			value : "NAME",
			blank : "선택해주세요"
		});

		$('#autoIssueType').unbind('change').on('change', function(){
			var dropDownList =$('#autoIssueType').data('kendoDropDownList');
			var data = dropDownList.value();
			switch(data){

			case "AUTO_ISSUE_TYPE.USER_JOIN" :
				$("#eventView").hide();
				$('#autoIssueQtyLimitDiv').show();

				break;

			case "AUTO_ISSUE_TYPE.EVENT_AWARD" :
				$("#eventView").show();
				$('#autoIssueQtyLimitDiv').hide();
				//발급수량 무제한 처리 (발급방법:자동발급[이벤트])
				fnIssueQtyUnLimit();
				break;

			case "AUTO_ISSUE_TYPE.USER_GRADE" :
				$("#eventView").hide();
				$('#autoIssueQtyLimitDiv').show();

				break;

			case "AUTO_ISSUE_TYPE.RECOMMENDER" :
				$("#eventView").hide();
				$('#autoIssueQtyLimitDiv').show();

				break;

			case "AUTO_ISSUE_TYPE.TESTER_EVENT" :
				$("#eventView").hide();
				$('#autoIssueQtyLimitDiv').show();

				break;
			}
		});


		// 발급기간
		fnKendoDatePicker({
			id          : 'issueStartDate',
			format		: 'yyyy-MM-dd',
			min 		: fnGetToday(),
			change      : function(e){
				dateTimeChk('issueStartDate','issueEndDate','발급기간');
			}
		});
		fnKendoDatePicker({
			id          : 'issueEndDate',
			format		: 'yyyy-MM-dd',
			min 		: fnGetToday(),
			change      : function(e){
				dateTimeChk('issueStartDate','issueEndDate','발급기간');
			}
		});


		// 유효기간
		fnKendoDatePicker({
			id          : 'validityStartDate',
			format		: 'yyyy-MM-dd',
			min 		: fnGetToday(),
			change      : function(e){
				dateTimeChk('validityStartDate','validityEndDate','유효기간');
			}
		});
		fnKendoDatePicker({
			id          : 'validityEndDate',
			format		: 'yyyy-MM-dd',
			min 		: fnGetToday(),
			change      : function(e){
				dateTimeChk('validityStartDate','validityEndDate','유효기간');
			}
		});

		//사용 전체
		fnTagMkChkBox({
			id    : 'allUse',
			data  : [	{ "CODE" : "Y" , "NAME":'전체'}
					],
			tagId : 'allUse',
			chkVal: 'Y',
			style : {}
		});
		//사용 PC
		fnTagMkChkBox({
			id    : 'usePcYn',
			data  : [	{ "CODE" : "Y" , "NAME":'PC'}
					],
			tagId : 'usePcYn',
			chkVal: 'Y',
			style : {}
		});
		//사용 모바일
		fnTagMkChkBox({
			id    : 'useMobileWebYn',
			data  : [	{ "CODE" : "Y" , "NAME":'Mobile'}
					],
			tagId : 'useMobileWebYn',
			chkVal: 'Y',
			style : {}
		});
		//사용 App
		fnTagMkChkBox({
			id    : 'useMobileAppYn',
			data  : [	{ "CODE" : "Y" , "NAME":'App'}
					],
			tagId : 'useMobileAppYn',
			chkVal: 'Y',
			style : {}
		});


		// 상품 적용범위 리스트
		fnKendoDropDownList({
			id    : 'goodsCoverageType',
			data  : [{"CODE":"APPLYCOVERAGE.ALL","NAME":'전체'}
					,{"CODE":"APPLYCOVERAGE.GOODS","NAME":'상품'}
					,{"CODE":"APPLYCOVERAGE.BRAND","NAME":'전시브랜드'}
					,{"CODE":"APPLYCOVERAGE.DISPLAY_CATEGORY","NAME":'전시카테고리'}
					]
		});

		$('#goodsCoverageType').unbind('change').on('change', function(){
			var dropDownList =$('#goodsCoverageType').data('kendoDropDownList');
			var data = dropDownList.value();
			switch(data){

			case "APPLYCOVERAGE.ALL" :
				$("#goodsIncludeYn").data("kendoDropDownList").enable(false);
				$("#brandCoverageTypeDiv").hide();
				$("#categoryCoverageTypeDiv").hide();
				$("#fnAddCoverageGoods").hide();
				$("#fnAddCoverageBrand").hide();
				break;

			case "APPLYCOVERAGE.GOODS" :
				$("#goodsIncludeYn").data("kendoDropDownList").enable(true);
				$("#brandCoverageTypeDiv").hide();
				$("#categoryCoverageTypeDiv").hide();
				$("#fnAddCoverageGoods").show();
				$("#fnAddCoverageBrand").hide();
				break;

			case "APPLYCOVERAGE.BRAND" :
				$("#goodsIncludeYn").data("kendoDropDownList").enable(true);
				$("#brandCoverageTypeDiv").show();
				$("#categoryCoverageTypeDiv").hide();
				document.getElementById('goodsIncludeYn').style.visibility='visible';
				$("#fnAddCoverageGoods").hide();
				$("#fnAddCoverageBrand").show();
				break;

			case "APPLYCOVERAGE.DISPLAY_CATEGORY" :
				$("#goodsIncludeYn").data("kendoDropDownList").enable(true);
				$("#brandCoverageTypeDiv").hide();
				$("#categoryCoverageTypeDiv").show();
				$("#fnAddCoverageGoods").hide();
				$("#fnAddCoverageBrand").hide();

				$("#ilCtgryStd1").data('kendoDropDownList').value("");
				$("#ilCtgryStd2").data('kendoDropDownList').value("");
				$("#ilCtgryStd3").data('kendoDropDownList').value("");
				$("#ilCtgryStd4").data('kendoDropDownList').value("");
				break;
			}

		});


		// 조회된 공급업체 ID 로 브랜드 리스트 호출
		fnKendoDropDownList({
            id : 'selectBrandCoverage',
            url : "/admin/ur/brand/searchDisplayBrandList",
            params : {
            },
            textField : "dpBrandName",
            valueField : "dpBrandId",
            blank : "선택해주세요"

        });

		$('#selectBrandCoverage').unbind('change').on('change', function(){
			var shopDropDownList =$('#selectBrandCoverage').data('kendoDropDownList');
			$('#brandName').val(shopDropDownList._oldText);
		});



		// 장바구니 적용범위 리스트
		fnKendoDropDownList({
			id    : 'cartCoverageType',
			data  : [{"CODE":"APPLYCOVERAGE.ALL","NAME":'전체'}
					,{"CODE":"APPLYCOVERAGE.GOODS","NAME":'상품'}
					,{"CODE":"APPLYCOVERAGE.BRAND","NAME":'전시브랜드'}
					,{"CODE":"APPLYCOVERAGE.DISPLAY_CATEGORY","NAME":'전시카테고리'}
					]
		});


		// 배송비 적용범위 리스트
		fnKendoDropDownList({
			id    : 'shippingCoverageType',
			data  : [{"CODE":"APPLYCOVERAGE.WAREHOUSE","NAME":'출고처'}]
		});

		// 판매가지정 적용범위 리스트
		fnKendoDropDownList({
			id    : 'appintCoverageType',
			data  : [{"CODE":"APPLYCOVERAGE.GOODS","NAME":'상품'}]
		});

		// 적용범위 포함/제외 리스트
		fnKendoDropDownList({
			id    : 'goodsIncludeYn',
			data  : [{"CODE":"Y","NAME":'포함'}
					,{"CODE":"N","NAME":'제외'}
					]
		});

		//적용범위 포함/제외 리스트
		fnKendoDropDownList({
			id    : 'cartIncludeYn',
			data  : [{"CODE":"Y","NAME":'포함'}
					,{"CODE":"N","NAME":'제외'}
					]
		});


		// 적용범위 포함/제외 리스트
		fnKendoDropDownList({
			id    : 'brandIncludeYn',
			data  : [{"CODE":"Y","NAME":'포함'}
					,{"CODE":"N","NAME":'제외'}
					]
		});

		// 적용범위 포함/제외 리스트
		fnKendoDropDownList({
			id    : 'shippingIncludeYn',
			data  : [{"CODE":"Y","NAME":'포함'}
					,{"CODE":"N","NAME":'제외'}
					]
		});


		// 쿠폰제한수량
		fnKendoDropDownList({
			id  : 'issueQtyLimit',
			tagId : 'issueQtyLimit',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "COUPON_LIMIT", "useYn" :"Y"},
			textField :"NAME",
			valueField : "CODE",
			value : "NAME",
			blank : "선택해주세요"
		});


		// 발급목적
		fnKendoDropDownList({
			id  : 'issuePurposeType',
			tagId : 'issuePurposeType',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "ISSUE_PURPOSE_TYPE", "useYn" :"Y"},
			textField :"NAME",
			valueField : "CODE",
			value : "NAME",
			blank : "선택해주세요"
		});


		// 전시카테고리 시작
		// 전시카테고리 대분류
        fnKendoDropDownList({
        	id : "ilCtgryStd1",
        	tagId : "ilCtgryStd1",
        	url : "/admin/comn/getDropDownCategoryList",
        	params : { "depth" : "1", "mallDiv" : "MALL_DIV.PULMUONE","categoryId" : "" },
        	textField : "categoryName",
        	valueField : "categoryId",
        	blank : "대분류",
        	async : false
        });

        // 전시카테고리 중분류
        fnKendoDropDownList({
            id : "ilCtgryStd2",
            tagId : "ilCtgryStd2",
            url : "/admin/comn/getDropDownCategoryList",
            textField : "categoryName",
            valueField : "categoryId",
            blank : "중분류",
            async : false,
            cscdId : "ilCtgryStd1",
            cscdField : "categoryId"
        });

        $('#ilCtgryStd1').unbind('change').on('change', function(){
			var ilCtgryStd1DownList =$('#ilCtgryStd1').data('kendoDropDownList');
			ilCtgryStd1 = ilCtgryStd1DownList._oldText;
		});

        $('#ilCtgryStd2').unbind('change').on('change', function(){
			var ilCtgryStd1DownList =$('#ilCtgryStd2').data('kendoDropDownList');
			ilCtgryStd2 = ilCtgryStd1DownList._oldText;
		});

        // 전시카테고리 소분류
        fnKendoDropDownList({
            id : "ilCtgryStd3",
            tagId : "ilCtgryStd3",
            url : "/admin/comn/getDropDownCategoryList",
            textField : "categoryName",
            valueField : "categoryId",
            blank : "소분류",
            async : false,
            cscdId : "ilCtgryStd2",
            cscdField : "categoryId"
        });

        $('#ilCtgryStd3').unbind('change').on('change', function(){
			var ilCtgryStd1DownList =$('#ilCtgryStd3').data('kendoDropDownList');
			ilCtgryStd3 = ilCtgryStd1DownList._oldText;
		});

        // 전시카테고리 세분류
        fnKendoDropDownList({
            id : "ilCtgryStd4",
            tagId : "ilCtgryStd4",
            url : "/admin/comn/getDropDownCategoryList",
            textField : "categoryName",
            valueField : "categoryId",
            blank : "세분류",
            async : false,
            cscdId : "ilCtgryStd3",
            cscdField : "categoryId"
        });

        $('#ilCtgryStd4').unbind('change').on('change', function(){
			var ilCtgryStd1DownList =$('#ilCtgryStd4').data('kendoDropDownList');
			ilCtgryStd4 = ilCtgryStd1DownList._oldText;
		});
		//전시카테고리 끝

		fnTagMkRadio({
			id    :  "validityType",
			data  : [	{ "CODE" : "VALIDITY_TYPE.PERIOD" , "NAME":'기간설정'}
					, 	{ "CODE" : "VALIDITY_TYPE.VALIDITY" , "NAME":'유효일'}
					],
			tagId : "validityType",
			chkVal: 'VALIDITY_TYPE.PERIOD',
			style : {},
			change : function(e){
					var data = $("#validityType").getRadioVal();
					switch(data){
					case "VALIDITY_TYPE.PERIOD" :
						break;

					case "VALIDITY_TYPE.VALIDITY" :
						break;

					}
				}
			});

		fnTagMkRadio({
			id    :  "paymentType",
			data  : [	{ "CODE" : "PAYMENT_TYPE.GOODS_DETAIL" , "NAME":'상품상세발급'}
					, 	{ "CODE" : "PAYMENT_TYPE.DOWNLOAD" , "NAME":'다운로드'}
					, 	{ "CODE" : "PAYMENT_TYPE.AUTO_PAYMENT" , "NAME":'자동발급'}
					, 	{ "CODE" : "PAYMENT_TYPE.CHECK_PAYMENT" , "NAME":'계정발급'}
					, 	{ "CODE" : "PAYMENT_TYPE.TICKET" , "NAME":'이용권'}
					],
			tagId : "paymentType",
			chkVal: 'PAYMENT_TYPE.GOODS_DETAIL',
			style : {},
			change : function(e){
					var data = $("#paymentType").getRadioVal();
					switch(data){
					case "PAYMENT_TYPE.GOODS_DETAIL" :
						$("#autoIssueTypeDiv").hide();
						fnRemoveEvntType();
						$("#checkPaymentDiv").hide();
						$("#ticketDiv").hide();

						$("#goodsCoverageType").data('kendoDropDownList').value("APPLYCOVERAGE.GOODS");
						$("#goodsCoverageType").data("kendoDropDownList").enable(false);

						$("#goodsIncludeYn").closest(".k-widget").hide();
						$("#fnAddCoverageGoods").show();

						$('#goodsCoverageDiv').show();
						$("#categoryCoverageTypeDiv").hide();
						$("#brandCoverageTypeDiv").hide();


						//발급수량 제한/무제한
						fnIssueQtyNoneLimit();

						//쿠폰발급수량 제한 [자동발급 외]
						fnAutoIssueQtyUnLimit();

						break;

					case "PAYMENT_TYPE.DOWNLOAD" :
						$("#autoIssueTypeDiv").hide();
						fnRemoveEvntType();
						$("#checkPaymentDiv").hide();
						$("#ticketDiv").hide();

						$("#goodsCoverageType").data('kendoDropDownList').value("APPLYCOVERAGE.ALL");
						$("#goodsCoverageType").data("kendoDropDownList").enable(true);

						$("#goodsIncludeYn").closest(".k-widget").show();
						$("#goodsIncludeYn").data('kendoDropDownList').value("Y");
						$("#goodsIncludeYn").data("kendoDropDownList").enable(false);

						$('#goodsCoverageDiv').show();

						//발급수량 제한/무제한
						fnIssueQtyNoneLimit();
						//쿠폰발급수량 제한 [자동발급 외]
						fnAutoIssueQtyUnLimit();

						break;

					case "PAYMENT_TYPE.AUTO_PAYMENT" :
						$("#autoIssueTypeDiv").show();
						fnRemoveEvntType();
						$("#checkPaymentDiv").hide();
						$("#ticketDiv").hide();

						$("#goodsCoverageType").data('kendoDropDownList').value("APPLYCOVERAGE.ALL");
						$("#goodsCoverageType").data("kendoDropDownList").enable(true);

						$("#goodsIncludeYn").closest(".k-widget").show();
						$("#goodsIncludeYn").data('kendoDropDownList').value("Y");
						$("#goodsIncludeYn").data("kendoDropDownList").enable(true);
						$('#goodsCoverageDiv').show();

						//발급수량 무제한 처리
						fnIssueQtyUnLimit();

						//쿠폰발급수량 제한 [자동발급]
						fnAutoIssueQtyLimit();
						$("#issueQty").val('');
						break;

					case "PAYMENT_TYPE.CHECK_PAYMENT" :
						$("#autoIssueTypeDiv").hide();
						fnRemoveEvntType();
						$("#checkPaymentDiv").show();
						$("#ticketDiv").hide();

						$("#goodsCoverageType").data('kendoDropDownList').value("APPLYCOVERAGE.ALL");
						$("#goodsCoverageType").data("kendoDropDownList").enable(true);

						$("#goodsIncludeYn").closest(".k-widget").show();
						$("#goodsIncludeYn").data('kendoDropDownList').value("Y");
						$("#goodsIncludeYn").data("kendoDropDownList").enable(true);
						$('#goodsCoverageDiv').show();
						if($('#userUploadFileName').text().length == 0){
							$('#userUploadFileName').hide();
						}else{
							$('#userUploadFileName').show();
						}

						//발급수량 무제한 표시처리
						fnIssueQtyUnLimit();
						//쿠폰발급수량 제한 [자동발급 외]
						fnAutoIssueQtyUnLimit();
						$("#issueQty").val('');
						break;

					case "PAYMENT_TYPE.TICKET" :
						$("#autoIssueTypeDiv").hide();
						fnRemoveEvntType();
						$("#checkPaymentDiv").hide();
						$("#ticketDiv").show();
						$("#ticketIssueType").data('kendoDropDownList').value("SERIAL_NUMBER_TYPE.AUTO_CREATE");
						$("#fnTicketExcelUpload").hide();
						$("#fnTicketSamepleFormDownload").hide();
						$("#serialNumber").attr("disabled", true);

						$("#goodsCoverageType").data('kendoDropDownList').value("APPLYCOVERAGE.ALL");
						$("#goodsCoverageType").data("kendoDropDownList").enable(true);

						$("#goodsIncludeYn").closest(".k-widget").show();
						$("#goodsIncludeYn").data('kendoDropDownList').value("Y");
						$("#goodsIncludeYn").data("kendoDropDownList").enable(true);

						$('#goodsCoverageDiv').show();
						if($('#ticketUploadFileName').text().length == 0){
							$('#ticketUploadFileName').hide();
							$("#fnTicketExcelUpload").hide();
							$("#fnTicketSamepleFormDownload").hide();
						}else{
							$('#ticketUploadFileName').show();
							$("#fnTicketExcelUpload").show();
							$("#fnTicketSamepleFormDownload").show();
						}

						$('#brandCoverageTypeDiv').hide();
						$('#categoryCoverageTypeDiv').hide();

						// 발급수량 제한 표시처리
						fnIssueQtyLimit();

						// 쿠폰발급수량 제한 [자동발급 외]
						fnAutoIssueQtyUnLimit();
						$("#issueQty").val('');
						break;

					}
				}
			});

		fnTagMkRadio({
			id    :  "paymentTypeCart",
			data  : [	{ "CODE" : "PAYMENT_TYPE.DOWNLOAD" , "NAME":'다운로드'}
					, 	{ "CODE" : "PAYMENT_TYPE.AUTO_PAYMENT" , "NAME":'자동발급'}
					, 	{ "CODE" : "PAYMENT_TYPE.CHECK_PAYMENT" , "NAME":'계정발급'}
					, 	{ "CODE" : "PAYMENT_TYPE.TICKET" , "NAME":'이용권'}
					],
			tagId : "paymentTypeCart",
			chkVal: 'PAYMENT_TYPE.DOWNLOAD',
			style : {},
			change : function(e){
					var data = $("#paymentTypeCart").getRadioVal();
					switch(data){
					case "PAYMENT_TYPE.DOWNLOAD" :
						$("#autoIssueTypeDiv").hide();
						fnRemoveEvntType();
						$("#checkPaymentDiv").hide();
						$("#ticketDiv").hide();

						//발급수량 제한/무제한
						fnIssueQtyNoneLimit();
						//쿠폰발급수량 제한 [자동발급 외]
						fnAutoIssueQtyUnLimit();

						break;

					case "PAYMENT_TYPE.AUTO_PAYMENT" :
						$("#autoIssueTypeDiv").show();
						fnRemoveEvntType();
						$("#checkPaymentDiv").hide();
						$("#ticketDiv").hide();

						//발급수량 무제한 처리
						fnIssueQtyUnLimit();
						//쿠폰발급수량 제한 [자동발급]
						fnAutoIssueQtyLimit();
						$("#issueQty").val('');
						break;

					case "PAYMENT_TYPE.CHECK_PAYMENT" :
						$("#autoIssueTypeDiv").hide();
						fnRemoveEvntType();
						$("#checkPaymentDiv").show();
						$("#ticketDiv").hide();

						//발급수량 무제한 처리
						fnIssueQtyUnLimit();
						//쿠폰발급수량 제한 [자동발급 외]
						fnAutoIssueQtyUnLimit();
						$("#issueQty").val('');
						break;

					case "PAYMENT_TYPE.TICKET" :
						$("#autoIssueTypeDiv").hide();
						fnRemoveEvntType();
						$("#checkPaymentDiv").hide();
						$("#ticketDiv").show();
						$("#ticketIssueType").data('kendoDropDownList').value("SERIAL_NUMBER_TYPE.AUTO_CREATE");
						$("#fnTicketExcelUpload").hide();
                        $("#fnTicketSamepleFormDownload").hide();
                        $("#serialNumber").attr("disabled", true);

						//발급수량 제한 표시처리
						fnIssueQtyLimit();
						//쿠폰발급수량 제한 [자동발급 외]
						fnAutoIssueQtyUnLimit();
						$("#issueQty").val('');
						break;

					}
				}
			});

		fnTagMkRadio({
			id    :  "paymentTypeShippingPrice",
			data  : [	{ "CODE" : "PAYMENT_TYPE.DOWNLOAD" , "NAME":'다운로드'}
					, 	{ "CODE" : "PAYMENT_TYPE.AUTO_PAYMENT" , "NAME":'자동발급'}
					, 	{ "CODE" : "PAYMENT_TYPE.CHECK_PAYMENT" , "NAME":'계정발급'}
					],
			tagId : "paymentTypeShippingPrice",
			chkVal: 'PAYMENT_TYPE.DOWNLOAD',
			style : {},
			change : function(e){
					var data = $("#paymentTypeShippingPrice").getRadioVal();
					switch(data){
					case "PAYMENT_TYPE.DOWNLOAD" :
						$("#autoIssueTypeDiv").hide();
						fnRemoveEvntType();
						$("#checkPaymentDiv").hide();
						$("#ticketDiv").hide();
						//발급수량 제한/무제한
						fnIssueQtyNoneLimit();
						//쿠폰발급수량 제한 [자동발급 외]
						fnAutoIssueQtyUnLimit();
						break;

					case "PAYMENT_TYPE.AUTO_PAYMENT" :
						$("#autoIssueTypeDiv").show();
						fnRemoveEvntType();
						$("#checkPaymentDiv").hide();
						$("#ticketDiv").hide();

						//발급수량 무제한 처리
						fnIssueQtyUnLimit();
						//쿠폰발급수량 제한 [자동발급]
						fnAutoIssueQtyLimit();
						break;

					case "PAYMENT_TYPE.CHECK_PAYMENT" :
						$("#autoIssueTypeDiv").hide();
						fnRemoveEvntType();
						$("#checkPaymentDiv").show();
						$("#ticketDiv").hide();

						//발급수량 무제한 처리
						fnIssueQtyUnLimit();
						//쿠폰발급수량 제한 [자동발급 외]
						fnAutoIssueQtyUnLimit();
						break;


					}
				}
			});

		fnTagMkRadio({
			id    :  "paymentTypeSaleprice",
			data  : [	{ "CODE" : "PAYMENT_TYPE.AUTO_PAYMENT" , "NAME":'자동발급'}],
			tagId : "paymentTypeSaleprice",
			chkVal: '전체',
			style : {},
			change : function(e){
					var data = $("#paymentTypeSaleprice").getRadioVal();
					switch(data){
					case "PAYMENT_TYPE.AUTO_PAYMENT" :
						$("#autoIssueTypeDiv").hide();
						fnAddEvntType();
						$("#issueQtyLimitView").show();
						$("#eventView").hide();
						$("#issueQty").val('');
						break;
					}
				}
			});

		// 결제수단 이니시스/KCP 구분
        fnKendoDropDownList({
            id : "pgPromotionPayConfigId",
            tagId : "pgPromotionPayConfigId",
            url : "/admin/pm/cpnMgm/getPaymentList",
            params : {},
            textField : "psPayCodeName",
            valueField : "psPayCode",
            blank : "PG전체",
            async : false
        });

        // 제휴구분 PG
        fnKendoDropDownList({
            id : "pgPromotionPayGroupId",
            tagId : "pgPromotionPayGroupId",
            url : "/admin/pm/cpnMgm/getPaymentUseList",
            textField : "psPayCodeName",
            valueField : "psPayCode",
            blank : "선택해주세요",
            async : false,
            cscdId : "pgPromotionPayConfigId",
            cscdField : "psPayCode"
        });

        $('#pgPromotionPayConfigId').unbind('change').on('change', function(){

			var pgPromotionPayConfigIdDownList =$('#pgPromotionPayConfigId').data('kendoDropDownList');
        	pgPromotionPayConfigId = pgPromotionPayConfigIdDownList._old;
			$("#pgPromotionPayGroupId").data("kendoDropDownList").enable( true );

		});

        // 제휴구분 결제수단
        fnKendoDropDownList({
			id : "pgPromotionPayId",
			tagId : "pgPromotionPayId",
			url : "/admin/pm/cpnMgm/getPayCardList",
			textField : "psPayCodeName",
			valueField : "psPayCode",
			blank : "선택해주세요",
			async : true
		});

        $('#pgPromotionPayGroupId').unbind('change').on('change', function(){
			var pgPromotionPayConfigIdDownList =$('#pgPromotionPayGroupId').data('kendoDropDownList');
			pgPromotionPayGroupId = pgPromotionPayConfigIdDownList._old;

			$("#pgPromotionPayId").data("kendoDropDownList").enable( true );

			//제휴구분 결제수단 상세
			fnKendoDropDownList({
				id : "pgPromotionPayId",
				tagId : "pgPromotionPayId",
				url : "/admin/pm/cpnMgm/getPayCardList",
				params : {"psCode" : pgPromotionPayConfigId, "psPgCode" : pgPromotionPayGroupId},
				textField : "psPayCodeName",
				valueField : "psPayCode",
				blank : "선택해주세요",
				async : true
			});

			$('#pgPromotionPayId').unbind('change').on('change', function(){
				var pgPromotionPayConfigIdDownList =$('#pgPromotionPayId').data('kendoDropDownList');
				pgPromotionPayId = pgPromotionPayConfigIdDownList._oldText;
			});

			if($('#pgPromotionPayGroupId').data('kendoDropDownList').value() == 'PAY_TP.CARD' ||
					$('#pgPromotionPayGroupId').data('kendoDropDownList').value() == 'PAY_TP.VIRTUAL_BANK'	){
				$("#pgPromotionPayId").data("kendoDropDownList").enable( true );
			}else{
				$("#pgPromotionPayId").data("kendoDropDownList").enable( false );
			}
		});

		fnKendoDropDownList({
			id    : 'ticketIssueType',
			data  : [
					{"CODE":"SERIAL_NUMBER_TYPE.AUTO_CREATE","NAME":"자동생성"}
					,{"CODE":"SERIAL_NUMBER_TYPE.EXCEL_UPLOAD","NAME":"엑셀 업로드"}
					],
			chkVal: 'SERIAL_NUMBER_TYPE.AUTO_CREATE'
		});

		$('#ticketIssueType').unbind('change').on('change', function(){
			var dropDownList =$('#ticketIssueType').data('kendoDropDownList');
			var data = dropDownList.value();
			switch(data){
			case "SERIAL_NUMBER_TYPE.AUTO_CREATE" :
				$("#fnTicketExcelUpload").hide();
				$("#fnTicketSamepleFormDownload").hide();
				$("#serialNumber").attr("disabled", true);
				$("#issueQty").attr("disabled", false);

				$("#uploadTicketViewControl").hide();
                $("#ticketUploadFileName").hide();

				break;

			case "SERIAL_NUMBER_TYPE.EXCEL_UPLOAD" :
				$("#fnTicketExcelUpload").show();
				$("#fnTicketSamepleFormDownload").show();
				$("#serialNumber").attr("disabled", true);
				$("#issueQty").attr("disabled", true);

				break;
			}
		});



		$("input[name=ticketType]").change(function() {
			var chkValue = $(this).val();
			if(chkValue = 'ticketType_auto'){
				document.getElementById("ticketIssueType").disabled = false;
			}else{
				document.getElementById("ticketIssueType").disabled = true;
				var dropDownList =$('#ticketIssueType').data('kendoDropDownList');
				dropDownList.value("AUTO");
				$("#fnTicketExcelUpload").hide();
				$("#fnTicketSamepleFormDownload").hide();
			}
		});


		// 제휴구분
		fnTagMkRadio({
			id    :  "pgPromotionYn",
			data  : [	{ "CODE" : "N" , "NAME":"미적용"}
					, 	{ "CODE" : "Y" , "NAME":"적용, 결제수단"}],
			tagId : "pgPromotionYn",
			chkVal: 'N',
			style : {},
			change : function(e){
					var data = $("#pgPromotionYn").getRadioVal();
					switch(data){
					case "N" :
						$("#pgPromotionPayConfigId").data("kendoDropDownList").enable( false );
						$("#pgPromotionPayGroupId").data("kendoDropDownList").enable( false );
						$("#pgPromotionPayId").data("kendoDropDownList").enable( false );
						$("#pgPromotionPayConfigId").data('kendoDropDownList').value("");
						$("#pgPromotionPayGroupId").data('kendoDropDownList').value("");
						$("#pgPromotionPayId").data('kendoDropDownList').value("");
						break;

					case "Y" :
						$("#pgPromotionPayConfigId").data("kendoDropDownList").enable( true );

						break;
					}
				}
		});

        // 업로드 회원등록 링크 클릭
        $("#uploadAccountLink").on("click", function(e){
            $("#uploadUserPopup").data("kendoWindow").center().open();
        });


        // 업로드 이용권 링크 클릭
        $("#uploadTicketLink").on("click", function(e){
            $("#uploadTicketPopup").data("kendoWindow").center().open();
        });


        // 장바구니쿠폰 적용여부
		fnTagMkRadio({
			id    :  "cartCouponApplyYn",
			data  : [	{ "CODE" : "N" , "NAME":"적용 불가"}
					, 	{ "CODE" : "Y" , "NAME":"적용 가능"}],
			tagId : "cartCouponApplyYn",
			chkVal: 'N',
			style : {}
		});


	}


	 $("#minPaymentAmount").kendoNumericTextBox({
		  format: "n0"
       , spinners: false
	 });


//	 $("#percentageMaxDiscountAmount").kendoNumericTextBox({
//		  format: "n0"
//      , spinners: false
//	 });

	 $("#discountValueCart").kendoNumericTextBox({
		  format: "n0"
      , spinners: false
	 });

	 $("#discountValueSalePrice").kendoNumericTextBox({
		  format: "n0"
     , spinners: false
	 });



	 $("#discountValueFixed").kendoNumericTextBox({
		  format: "n0"
    , spinners: false
	 });


	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'insert':

			fnKendoMessage({
					message : '저장되었습니다.',
					ok      : function(){ fnClose();}
				});

				break;


		}
	}

	function fnCoverageInit(){
		ctgryGridDs =  new kendo.data.DataSource();

		ctgryGridOpt = {
	            dataSource : ctgryGridDs,
	            editable : false,
	            filterble : true,
	            columns : [
	            	{
		                field : 'categoryType',
		                title : '구분',
		                width : '15%',
		                attributes : {
		                    style : 'text-align:center'
		                }
		            },
	            	{
		                field : 'coverageName',
		                encoded: false,
		                title : '항목',
		                width : '45%',
		                attributes : {
		                    style : 'text-align:left'
		                }
		            }, {
		                field : 'includeYnName',
		                title : '포함/제외',
		                width : '20%',
		                attributes : {
		                    style : 'text-align:center'
		                }
		            }, {
		                command : {text: '해제',click:fnDelCoverage} ,
		                title : '관리',
		                width : '20%',
		                attributes : {
		                    style : 'text-align:center',
		                    class : 'forbiz-cell-readonly'
		                }
		            },
		            { field:'addCoverageId', hidden:true},
		            { field:'includeYn', hidden:true}
	            ]
	        };

	        ctgryGrid = $('#ctgryGrid').initializeKendoGrid(ctgryGridOpt).cKendoGrid();


	}


	function fnAppintCoverageInit(){
		appintCoverageGridDs =  new kendo.data.DataSource();

		appintCoverageGridOpt = {
	            dataSource : appintCoverageGridDs,
	            editable : false,
	            columns : [
	            	{
		                field : 'categoryType',
		                title : '구분',
		                width : '20%',
		                attributes : {
		                    style : 'text-align:center'
		                }
		            },
	            	{
		                field : 'coverageName',
		                encoded: false,
		                title : '항목',
		                width : '50%',
		                attributes : {
		                    style : 'text-align:left'
	                }
	            	}, {
		                command : {text: '해제',click:fnDelAppintCoverage} ,
		                title : '관리',
		                width : '30%',
		                attributes : {
		                    style : 'text-align:center',
		                    class : 'forbiz-cell-readonly'
		                }
		            },
		            { field:'addCoverageId', hidden:true},
		            { field:'includeYn', hidden:true}
	            ]
	        };

		appintCoverageGrid = $('#appintCoverageGrid').initializeKendoGrid(appintCoverageGridOpt).cKendoGrid();


	}


	//승인관리자 정보 Grid
	function fnAprpAdminInit(){
		apprAdminGridDs =  new kendo.data.DataSource();

		apprAdminGridOpt = {
	            dataSource : apprAdminGridDs,
	            editable : false,
	            noRecordMsg: '승인관리자를 선택해 주세요.',
	            columns : [{
			                field : 'apprAdminInfo',
			                title : '승인관리자 정보',
			                width : '100px',
			                attributes : {
			                    style : 'text-align:center'}
		                },{
		                field : 'adminTypeName',
		                title : '계정유형',
		                width : '100px',
		                attributes : {
		                    style : 'text-align:center'}
		                },{
			                field : 'apprUserName',
			                title : '관리자이름/아이디',
			                width : '100px',
			                attributes : {
			                    style : 'text-align:center'
			                },
			                template : function(dataItem){
								let returnValue;
								returnValue = dataItem.apprUserName + '/' + dataItem.apprLoginId;
								return returnValue;
							}
			            },{
			                field : 'organizationName',
			                title : '조직/거래처 정보',
			                width : '100px',
			                attributes : {
			                    style : 'text-align:center'}
		                },{
			                field : 'teamLeaderYn',
			                title : '조직장여부',
			                width : '80px',
			                attributes : {
			                    style : 'text-align:center'}
		                },{
			                field : 'userStatusName',
			                title : 'BOS 계정상태',
			                width : '80px',
			                attributes : {
			                    style : 'text-align:center'}
		                },{
			                field : 'grantUserName',
			                title : '권한위임정보',
			                width : '100px',
			                attributes : {
			                    style : 'text-align:center'},
			                template : function(dataItem){
									let returnValue;
									if(dataItem.grantAuthYn == 'Y'){
										returnValue = dataItem.grantUserName + '/' + dataItem.grantLoginId;
									}else{
										returnValue = '';
									}
									return returnValue;
							}
		                },{
			                field : 'userStatusName',
			                title : '권한위임기간',
			                width : '150px',
			                attributes : {
			                    style : 'text-align:left'},
			                template : function(dataItem){
									let returnValue;
									if(dataItem.grantAuthYn == 'Y'){
										returnValue = dataItem.grantAuthStartDt + '~' + dataItem.grantAuthEndDt;
									}else{
										returnValue = '';
									}
									return returnValue;
							}
		                },{
			                field : 'grantUserStatusName',
			                title : '권한위임자 BOS 계정상태',
			                width : '100px',
			                attributes : {
			                    style : 'text-align:left'},
			                template : function(dataItem){
									let returnValue;
									if(dataItem.grantAuthYn == 'Y'){
										returnValue = dataItem.grantUserStatusName;
									}else{
										returnValue = '';
									}
									return returnValue;
							}
		                },
			            { field:'addCoverageId', hidden:true},
			            { field:'includeYn', hidden:true}
	            ]
	        };

		apprAdminGrid = $('#apprGrid').initializeKendoGrid(apprAdminGridOpt).cKendoGrid();


	}


	function fnAccountUploadInit(){
		 // 업로드 회원 팝업
	    $("#uploadUserPopup").kendoWindow({
            width : 450,
            height : 500,
            title : "계정 발급 내역",
            visible : false,
            modal : true
        });

		accountGridDs =  new kendo.data.DataSource();

		accountGridOpt = {
	            dataSource : accountGridDs,
	            editable : false,
	            columns : [ {field : 'loginId'	, title : '회원ID'	, width : '60px', attributes : {style : 'text-align:center'}}     ]
	        };

	        accountGrid = $('#accountGrid').initializeKendoGrid(accountGridOpt).cKendoGrid();
	}



	function fnTicketUploadInit(){
		 // 업로드 회원 팝업
	    $("#uploadTicketPopup").kendoWindow({
            width : 450,
            height : 500,
            title : "업로드 이용권 난수",
            visible : false,
            modal : true
        });

		ticketGridDs =  new kendo.data.DataSource();

		ticketGridOpt = {
	            dataSource : ticketGridDs,
	            editable : false,
	            columns : [ {field : 'serialNumber'	, title : '이용권 번호'	, width : '60px', attributes : {style : 'text-align:center'}}     ]
	        };

	        ticketGrid = $('#ticketGrid').initializeKendoGrid(ticketGridOpt).cKendoGrid();
	}

	function fnCartCoverageInit(){
		cartCoverageGridDs =  new kendo.data.DataSource();

		cartCoverageGridOpt = {
	            dataSource : cartCoverageGridDs,
	            editable : false,
	            columns : [
	            	{
		                field : 'categoryType',
		                title : '구분',
		                width : '15%',
		                attributes : {
		                    style : 'text-align:center'
		                }
		            },
	            	{
		                field : 'coverageName',
		                encoded: false,
		                title : '항목',
		                width : '45%',
		                attributes : {
		                    style : 'text-align:left'
		                }
		            }, {
		                field : 'includeYnName',
		                title : '포함/제외',
		                width : '20%',
		                attributes : {
		                    style : 'text-align:center'
		                }
		            }, {
		                command : {text: '해제',click:fnDelCartCoverage} ,
		                title : '관리',
		                width : '20%',
		                attributes : {
		                    style : 'text-align:center',
		                    class : 'forbiz-cell-readonly'
		                }
		            },
		            { field:'addCoverageId', hidden:true},
		            { field:'includeYn', hidden:true}
	            ]
	        };

		cartCoverageGrid = $('#cartCoverageGrid').initializeKendoGrid(cartCoverageGridOpt).cKendoGrid();


	}



	// 판매가지정 상품 추가
	 function fnAppintAddCoverage() {

		var obj = new Object();
		var params = {};
		params.goodsType = "";
		params.selectType = "multi";
		params.searchChoiceBtn = "Y";
		params.columnNameHidden = false;
		params.columnAreaShippingDeliveryYnHidden = false;
		params.columnDpBrandNameHidden = false;
		params.columnStardardPriceHidden = false;
		params.columnRecommendedPriceHidden = false;
		params.columnSalePriceHidden = false;
		params.columnSaleStatusCodeNameHidden = false;
		params.columnGoodsDisplyYnHidden = false;

        fnKendoPopup({
            id         : "goodsSearchPopup",
            title      : "상품조회",  // 해당되는 Title 명 작성
            width      : "1900px",
            height     : "800px",
            scrollable : "yes",
            src        : "#/goodsSearchPopup",
            param      : params,
            success    : function( id, data ){

            	if(data[0] != undefined){

	            	var dataCheck = $("#appintCoverageGrid").data("kendoGrid").dataSource._data;
	            	var dataCheckSize = dataCheck.length;
	            	if(dataCheck.length > 0 ){

	            	var dataCheckAarray =[];
	            	for(var i=0;i<dataCheck.length ;i++){
	            		dataCheckAarray.push(dataCheck[i].coverageId);
	            	}

	            	var goodsDataCheckArray =[];
	            	for(var j=0 ; j<data.length; j++){
	            		goodsDataCheckArray.push(data[j].goodsId);
	            	}
	            	var sum = dataCheckAarray.concat(goodsDataCheckArray);
	            	var intersec = sum.filter((item, index) => sum.indexOf(item) !== index);

	            	for(var k=0 ; k<data.length; k++){
            				var item = data[k].goodsId;
            				if(!intersec.includes(item)){
            					var goodsDisplayType;
		            			if( data[k].goodsDisplayYn == 'Y'){
		            				goodsDisplayType = '가능';
		            			}else{
		            				goodsDisplayType = '불가능';
		            			}
		            			obj["categoryType"] = '상품';
            					obj["coverageName"] = '상품코드 : ' + data[k].goodsId + "</br>" + '판매상태 : ' + data[k].saleStatusCodeName + ' / 전시상태 : ' + goodsDisplayType  + "</br>" + '상품명 : ' + data[k].goodsName;
            					obj["includeYn"] = 'Y';   //포함
            					obj["coverageId"] = data[k].goodsId;
            					obj["coverageType"] = 'APPLYCOVERAGE.GOODS';
            					appintCoverageGridDs.add(obj);
            				}
            			}
	            	}else{
	            		for(var k=0;k<data.length;k++){
	            			var goodsDisplayType;
	            			if( data[k].goodsDisplayYn == 'Y'){
	            				goodsDisplayType = '가능';
	            			}else{
	            				goodsDisplayType = '불가능';
	            			}
	            			obj["categoryType"] = '상품';
        					obj["coverageName"] = '상품코드 : ' + data[k].goodsId + "</br>" + '판매상태 : ' + data[k].saleStatusCodeName + ' / 전시상태 : ' + goodsDisplayType  + "</br>" + '상품명 : ' + data[k].goodsName;
							obj["includeYnName"] = $('#goodsIncludeYn').val() == 'Y' ? '포함' : '제외';
							obj["includeYn"] = $('#goodsIncludeYn').val();
							obj["coverageId"] = data[k].goodsId;
							obj["coverageType"] = 'APPLYCOVERAGE.GOODS';
							appintCoverageGridDs.add(obj);
		            	}
	            	}

            	}
            }
        });

	 }



	function fnWarehousePopupButton(){

		fnKendoPopup({
			id     : 'warehousePopup',
			title  : '출고처 검색',
			src    : '#/warehousePopup',
			param  : { },
			width  : '760px',
			height : '585px',
			success: function( id, data ){

				var dataCheck =$("#cartCoverageGrid").data("kendoGrid").dataSource._data;
				for(var i=0; i<dataCheck.length; i++){
					if(dataCheck[i].coverageId == data.urWarehouseId){
						fnKendoMessage({message : '이미 존재하는 출고처 입니다.'});
						return;
					}

				}

				if(data.urWarehouseId){
					var obj = new Object();
					obj["categoryType"] = '출고처';
					obj["coverageName"] = '출고처 코드 : ' + data.urWarehouseId + '</br>' + '출고처 명 : ' + data.warehouseName;
					obj["includeYnName"] = $('#shippingIncludeYn').val() == 'Y' ? '포함' : '제외';
					obj["includeYn"] = $('#shippingIncludeYn').val();
					obj["coverageId"] = data.urWarehouseId;
					obj["coverageType"] = 'APPLYCOVERAGE.WAREHOUSE';

					cartCoverageGridDs.add(obj);

				}
			}
		});
	}


	function fnDelCoverage(e){
		e.preventDefault();
		var dataItem = ctgryGrid.dataItem($(e.currentTarget).closest('tr'));
		ctgryGridDs.remove(dataItem);
		return;
	}

	function fnDelCartCoverage(e){
		e.preventDefault();
		var dataItem = cartCoverageGrid.dataItem($(e.currentTarget).closest('tr'));
		cartCoverageGridDs.remove(dataItem);
		return;
	}

	function fnDelAppintCoverage(e){
		e.preventDefault();
		var dataItem = appintCoverageGrid.dataItem($(e.currentTarget).closest('tr'));
		appintCoverageGridDs.remove(dataItem);
		return;
	}

	function getUniqueObjectArray(array, key) {
		  var tempArray = [];
		  var resultArray = [];
		  for(var i = 0; i < array.length; i++) {
		    var item = array[i]
		    if(temArray.include(item[key])) {
		      continue;
		    } else {
		      resultArray.push(item);
		      tempArray.push(item[key]);
		    }
		  }
		  return resultArray;
		}

	 // 상품 추가 - 상품검색 팝업
    function fnAddCoverage(  ){
    	var obj = new Object();

    	var couponTypeValue = $('#couponType').getRadioVal();
		var goodsCoverageType = $('#goodsCoverageType').val();
		var paymentTYpeValue = $("#paymentType").getRadioVal();

		if(goodsCoverageType == 'APPLYCOVERAGE.GOODS'){  //쿠폰종류 : 상품, 장바구니

			var params = {};
			params.goodsType = "GOODS_TYPE.NORMAL,GOODS_TYPE.DISPOSAL";	// 상품유형(복수 검색시 , 로 구분)

//			if(couponTypeValue == "COUPON_TYPE.GOODS" && paymentTYpeValue == "PAYMENT_TYPE.GOODS_DETAIL"){
//				params.selectType = "single";
//			}else{
				params.selectType = "multi";
				params.searchChoiceBtn = "Y";
//			}
			params.columnNameHidden = false;
			params.columnAreaShippingDeliveryYnHidden = false;
			params.columnDpBrandNameHidden = false;
			params.columnStardardPriceHidden = false;
			params.columnRecommendedPriceHidden = false;
			params.columnSalePriceHidden = false;
			params.columnSaleStatusCodeNameHidden = false;
			params.columnGoodsDisplyYnHidden = false;



	        fnKendoPopup({
	            id         : "goodsSearchPopup",
	            title      : "상품조회",  // 해당되는 Title 명 작성
	            width      : "1900px",
	            height     : "800px",
	            scrollable : "yes",
	            src        : "#/goodsSearchPopup",
	            param      : params,
	            success    : function( id, data ){

	            	if(data[0] != undefined){

		            	var dataCheck = $("#ctgryGrid").data("kendoGrid").dataSource._data;
		            	var dataCheckSize = dataCheck.length;
		            	if(dataCheck.length > 0 ){

		            		var dataCheckAarray =[];
			            	for(var i=0;i<dataCheck.length ;i++){
			            		dataCheckAarray.push(dataCheck[i].coverageId);
			            	}

			            	var goodsDataCheckArray =[];
			            	for(var j=0 ; j<data.length; j++){
			            		goodsDataCheckArray.push(data[j].goodsId);
			            	}
			            	var sum = dataCheckAarray.concat(goodsDataCheckArray);
			            	var intersec = sum.filter((item, index) => sum.indexOf(item) !== index);

		            		for(var k=0 ; k<data.length; k++){
	            				var item = data[k].goodsId;

	            				if(!intersec.includes(item)){
	            					var goodsDisplayType;
			            			if( data[k].goodsDisplayYn == 'Y'){
			            				goodsDisplayType = '가능';
			            			}else{
			            				goodsDisplayType = '불가능';
			            			}
	            					obj["categoryType"] = '상품';
	            					obj["coverageName"] = '상품코드 : ' + data[k].goodsId + "</br>" + '판매상태 : ' + data[k].saleStatusCodeName + ' / 전시상태 : ' + goodsDisplayType  + "</br>" + '상품명 : ' + data[k].goodsName;
	            					obj["includeYnName"] = $('#goodsIncludeYn').val() == 'Y' ? '포함' : '제외';
	            					obj["includeYn"] = $('#goodsIncludeYn').val();
	            					obj["coverageId"] = data[k].goodsId;
	            					obj["coverageType"] = 'APPLYCOVERAGE.GOODS';
	            					ctgryGridDs.add(obj);
	            				}
	            			}

		            	}else{
		            		for(var k=0;k<data.length;k++){
		            			var goodsDisplayType;
		            			if( data[k].goodsDisplayYn == 'Y'){
		            				goodsDisplayType = '가능';
		            			}else{
		            				goodsDisplayType = '불가능';
		            			}
		            			obj["categoryType"] = '상품';
								obj["coverageName"] = '상품코드 : ' + data[k].goodsId + "</br>" + '판매상태 : ' + data[k].saleStatusCodeName + ' / 전시상태 : ' + goodsDisplayType  +"</br>" + '상품명 : ' + data[k].goodsName;
								obj["includeYnName"] = $('#goodsIncludeYn').val() == 'Y' ? '포함' : '제외';
								obj["includeYn"] = $('#goodsIncludeYn').val();
								obj["coverageId"] = data[k].goodsId;
								obj["coverageType"] = 'APPLYCOVERAGE.GOODS';
								ctgryGridDs.add(obj);
			            	}
		            	}

	            	}
	            }
	        });

		}else if(goodsCoverageType == 'APPLYCOVERAGE.BRAND'){


			var dataCheck =$("#ctgryGrid").data("kendoGrid").dataSource._data;

			if($('#selectBrandCoverage').val() == ''){
				fnKendoMessage({message : '전시브랜드가 선택되지 않았습니다.'});
    			return;
			}

        	for(var i=0; i<dataCheck.length; i++){
        		if(dataCheck[i].coverageId == $('#selectBrandCoverage').val()){
        			fnKendoMessage({message : '이미 존재하는 브랜드입니다.'});
        			return;
        		}

        	}

			//쿠폰종류 : 상품
        	obj["categoryType"] = '전시브랜드';
			obj["coverageName"] = '전시브랜드 코드 : ' + $('#selectBrandCoverage').val() + "</br>" + '전시브랜드명 : ' + $('#brandName').val();
			obj["includeYnName"] = $('#goodsIncludeYn').val() == 'Y' ? '포함' : '제외';
			obj["includeYn"] = $('#goodsIncludeYn').val();
			obj["coverageId"] = $('#selectBrandCoverage').val();
			obj["coverageType"] = 'APPLYCOVERAGE.BRAND';
			ctgryGridDs.add(obj);

			// 전시 브랜드 선택 초기화
			$("#selectBrandCoverage").data('kendoDropDownList').value("");

		}else{
			var coverageName;
			var coverageId;
			if(ilCtgryStd4 != null){
				coverageName = '대분류 : ' + ilCtgryStd1 + ' / 중분류 :' + ilCtgryStd2 + ' / 소분류 : ' + ilCtgryStd3 + ' / </br>' + ' 세분류 :' +ilCtgryStd4;
				coverageId = $('#ilCtgryStd4').val();
			}else if(ilCtgryStd3 != null){
				coverageName =  '대분류 : ' + ilCtgryStd1 + ' / 중분류 :' + ilCtgryStd2 + ' / 소분류 : ' + ilCtgryStd3;
				coverageId = $('#ilCtgryStd3').val();
			}else if(ilCtgryStd2 != null){
				coverageName = '대분류 : ' + ilCtgryStd1 + ' / 중분류 :' + ilCtgryStd2 ;
				coverageId = $('#ilCtgryStd2').val();
			}else{
				coverageName = '대분류 : ' + ilCtgryStd1;
				coverageId = $('#ilCtgryStd1').val();
			}

			if(coverageId == ''){
				fnKendoMessage({message : '분류선택 후 추가해주세요.'});
    			return;
			}

			var dataCheck =$("#ctgryGrid").data("kendoGrid").dataSource._data;
        	for(var i=0; i<dataCheck.length; i++){
        		if(dataCheck[i].coverageId == coverageId){
        			fnKendoMessage({message : '이미 존재하는 카테고리입니다.'});
        			return;
        		}

        	}

			//쿠폰종류 : 상품
        	obj["categoryType"] = '전시카테고리';
			obj["coverageName"] = '전시카테고리 코드 : ' + coverageId + "</br>"+ coverageName;
			obj["includeYnName"] = $('#goodsIncludeYn').val() == 'Y' ? '포함' : '제외';
			obj["includeYn"] = $('#goodsIncludeYn').val();
			obj["coverageId"] = coverageId;
			obj["coverageType"] = 'APPLYCOVERAGE.DISPLAY_CATEGORY';
			ctgryGridDs.add(obj);

			ilCtgryStd1 = null;
			ilCtgryStd2 = null;
			ilCtgryStd3 = null;
			ilCtgryStd4 = null;

			$("#ilCtgryStd1").data('kendoDropDownList').value("");
			$("#ilCtgryStd2").data('kendoDropDownList').value("");
			$("#ilCtgryStd3").data('kendoDropDownList').value("");
			$("#ilCtgryStd4").data('kendoDropDownList').value("");

		}


    }


	// 샘플다운로드 버튼 클릭
	function fnSamepleFormDownload(){
		document.location.href = "/contents/excelsample/coupon/쿠폰_계정발급_샘플.xlsx"
	};


	//샘플다운로드 버튼 클릭
	function fnTicketSamepleFormDownload(){
		document.location.href = "/contents/excelsample/coupon/쿠폰_이용권발급_샘플.xlsx"
	};


	function fnAddEvntType(){
		$("#autoIssueType").data("kendoDropDownList").dataSource.add({"CODE": "AUTO_ISSUE_TYPE.TESTER_EVENT", "NAME": "체험단이벤트"});
	}

	function fnRemoveEvntType(){
		if($("#autoIssueType").data("kendoDropDownList").dataSource._data.length>4){
			$("#autoIssueType").data("kendoDropDownList").dataSource.remove($("#autoIssueType").data("kendoDropDownList").dataSource.data()[4]);
		}
	}



	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};

	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};

	/** Common List*/
	$scope.fnList = function( ){  fnList();};

	$scope.fnDeptPopupButton = function(param){fnDeptPopupButton(param);};

	$scope.fnAddCoverage = function(){fnAddCoverage();};

	$scope.fnAppintAddCoverage = function(){fnAppintAddCoverage();};

	$scope.fnCheckRequestApproval = function(){fnCheckRequestApproval();};

	$scope.fnWarehousePopupButton = function(){fnWarehousePopupButton();};

	$scope.fnSelectUser = function(){fnSelectUser();};

	$scope.fnUseExcelUpload = function(){fnUseExcelUpload();};

	$scope.fnTicketExcelUpload = function(){fnTicketExcelUpload();};

	$scope.fnExcelUpload = function(event) { excelExport(event);} // 엑셀 업로드 버튼

	$scope.fnExcelTicketUpload = function(event) { excelTicketExport(event);} // 엑셀 이용권 난수 업로드 버튼

	$scope.fnSamepleFormDownload = function(event) { fnSamepleFormDownload();} // 엑셀 이용권 난수 업로드 버튼

	$scope.fnTicketSamepleFormDownload = function(){fnTicketSamepleFormDownload();};

	$scope.fnApprAdmin = function(){fnApprAdmin();};		//승인관리자 지증

	$scope.fnApprClear = function(){fnApprClear();};		//승인관리자 초기화

	//마스터코드값 입력제한 - 숫자 & -
	fnInputValidationByRegexp("validityDay", /[^0-9]/g);
	fnInputValidationByRegexp("issueQty", /[^0-9]/g);
	fnInputValidationByRegexp("discountValuePercent", /[^0-9]/g);
	fnInputValidationByRegexp("percentageMaxDiscountAmount", /[^0-9]/g);
	fnInputValidationByRegexp("discountValueFixed", /[^0-9]/g);
	fnInputValidationByRegexp("minPaymentAmount", /[^0-9]/g);
	fnInputValidationByRegexp("issueQtyCount", /[^0-9]/g);


	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
