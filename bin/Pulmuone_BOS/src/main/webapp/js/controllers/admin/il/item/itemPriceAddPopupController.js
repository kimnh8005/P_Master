/**-----------------------------------------------------------------------------
 * system            : 우선할인 & 즉사할인 등록 팝업
 * @
 * @ 수정일           수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.08.28       손진구          최초생성
 * @
 * **/
"use strict";

var paramData = parent.POP_PARAM["parameter"]; // 파라미터
var viewModel; // viewModel

var apprAdminGridDs, apprAdminGridOpt, apprAdminGrid;
var apprKindTp;
var disableStartDates = [];

$(document).ready(function() {
	kendo.data.binders.widget.disableDates = kendo.data.Binder.extend({
        init: function(widget, bindings, options) {
            //call the base constructor
            kendo.data.Binder.fn.init.call(this, widget.element[0], bindings, options);
        }
        , refresh: function() {
            var that = this,
            value = that.bindings["disableDates"].get(); //get the value from the View-Model
            if (value != undefined && value != null) {
                $(that.element).data("kendoDatePicker").setOptions({
    				disableDates: value.slice(),
                }); //update the widget
            }
        }
    });

	fnInitialize();	//Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : false });

        fnPageInfo({
            PG_ID  : "itemPriceAddPopup",
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){

        fnInitButton();
        fnViewModelInit();
        fnInitOptionBox();
        fnApprAdminInit();

        setTimeout( function(){
            fnDefaultValue();
        }, 100);
    };

    //--------------------------------- Button Start---------------------------------

    // 버튼 초기화
    function fnInitButton(){
        $("#fnSave, #fnAddPrice").kendoButton();
    };

    //--------------------------------- Button End---------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    // 옵션 초기화
    function fnInitOptionBox(){
    	viewModel.fnGetApprovalStatus(); // 승인상태 공통코드 조회
    };

    //---------------Initialize Option Box End ------------------------------------------------


    //-------------------------------  Common Function start -------------------------------

    // viewModel 초기화
    function fnViewModelInit(){
        viewModel = new kendo.data.ObservableObject({
            isAddDiscountEnabled 		 : true, // 할인추가 버튼 Enabled
            isAddApprovalVisiable		 : false, // 승인정보 Visiable
            isAddPriceEnabled			 : true,
            isApprovalBtn				 : false,
            itemInfo : {
            	ilItemCd : '',
            	isErpItemLink : '',
            	erpLegalTypeCode : '',
            	productType : '',
            	taxYn : '',
            	newInit : '',
            	standardPriceOrig : '',
            	recommendedPriceOrig : '',
            	urSupplierId : '',
	    		// 원가, 정상가 수정 가능 여부(A:원가/정상가, R:정상가) S
            	priceManageTp : '',
	    		// 원가, 정상가 수정 가능 여부(A:원가/정상가, R:정상가) E
            },
            orginPriceList : new kendo.data.DataSource({ // 할인 리스트
                schema: {
                    model: {
                        id: "goodsDiscountId",
                        fields: {
                        	updateTypeName : { type: "string"}, // 구분
                        	systemUpdateYn : { type: "string"}, // 시스템에 의한 업데이트 유무(Y: 시스템)
                        	managerUpdateYn : { type: "string"}, // 관리자에 의한 업데이트 유무(Y: 관리자)
                        	approvalStatusCode : { type: "string"}, // 품목가격 승인상태코드
                        	approvalStatusCodeName : { type: "string" }, // 품목가격 승인상태코드
                        	priceApplyStartDate : { type: "string", validation: { required: true } }, // 품목가격 시작일자
                        	priceApplyEndDate : { type: "string", validation: { required: true } }, // 품목가격 종료일자
                        	standardPrice : { type: "number" }, // 품목 원가
                        	recommendedPrice : { type: "number" }, // 품목 정상가
                        	standardPriceOrig : { type: "number" }, // 품목 원가
                        	recommendedPriceOrig : { type: "number" }, // 품목 정상가
                        	priceRatio : { type: "number", }, // 품목 마진율
                        	approval1st : { type: "string", }, // 승인요청자
                        	approvalConfirm : { type: "string" }, // 승인관리자(1차/최종)
                        }
                    }
                }
            }),

            approvalStatusDropdownData : [], // 승인상태 Dropdown 데이터
            fnGetApprovalStatus : function(){ // 승인상태 공통코드 조회
                fnAjax({
                    url     : "/admin/comn/getCodeList",
                    method : "GET",
                    async : false,
                    params  : {"stCommonCodeMasterCode" : "APPR_STAT", "useYn" : "Y"},
                    success : function( data ){
                        viewModel.set("approvalStatusDropdownData", data.rows);
                    },
                    error : function(xhr, status, strError){
                        fnKendoMessage({ message : xhr.responseText });
                    },
                    isAction : "select"
                });
            },
            fnGetCodeName : function(code, dataName){ // 코드명 가져오기
                let dataList = viewModel.get(dataName);
                let codeName = "";

                for (let i = 0, dataListCount = dataList.length; i < dataListCount; i++) {
                    if( dataList[i].CODE == code ) {
                        codeName =  dataList[i].NAME;
                        break;
                    }
                }

                return codeName;
            },
            fnGetStartDate : function(){ // 시작일자 가져오기
            	let nowDateTime = kendo.parseDate( fnGetToday("yyyy-MM-dd"), "yyyy-MM-dd");
                nowDateTime.setDate(nowDateTime.getDate() + 1);

                return nowDateTime;
            },
            fnStartDateChange : function(e){ // 시작일 변경
                e.preventDefault();

                let rowIndex = viewModel.orginPriceList.indexOf( e.data );
                let dataItem = viewModel.orginPriceList.at(rowIndex);

                dataItem.priceApplyStartDate = kendo.toString(kendo.parseDate(dataItem.priceApplyStartDate), "yyyy-MM-dd");
            },
            fnStartDateOpen : function(e){ // 시작일 datepicker open 시 호출
            },
            fnGetStandPrice : function () {
            	let rowIndex = viewModel.orginPriceList.data().length;
            	let priceList = viewModel.orginPriceList.data();
            	let nowDateTime = fnGetToday("yyyyMMdd");
            	var standPrice = 0;

            	for(var i = 0; i < priceList.length; i++) {
            		var startDate = parseInt(priceList[i].priceApplyStartDate.replaceAll('-',''));
            		var endDate = parseInt(priceList[i].priceApplyEndDate.replaceAll('-',''));

            		if(nowDateTime >= startDate && nowDateTime <= endDate) {
            			standPrice = parseInt(priceList[i].standardPrice);
            			break;
            		}
            	}

            	return standPrice;
            },
            fnGetRecommendedPrice : function () {
                let rowIndex = viewModel.orginPriceList.data().length;
                let priceList = viewModel.orginPriceList.data();
                let nowDateTime = fnGetToday("yyyyMMdd");
                var recommendedPrice = 0;

                for(var i = 0; i < priceList.length; i++) {
                    var startDate = parseInt(priceList[i].priceApplyStartDate.replaceAll('-',''));
                    var endDate = parseInt(priceList[i].priceApplyEndDate.replaceAll('-',''));

                    if(nowDateTime >= startDate && nowDateTime <= endDate) {
                        recommendedPrice = parseInt(priceList[i].recommendedPrice);
                        break;
                    }
                }

                return recommendedPrice;
            },
            fnNewStandPriceChange : function(e) {

            	e.preventDefault();
            	let rowIndex = viewModel.orginPriceList.data().length;

            	var priceList = viewModel.orginPriceList.data();
            	var newStandardPrice = e.data.standardPrice;
            	var newRecommendedPrice = e.data.recommendedPrice;

            	if(newStandardPrice != undefined && newStandardPrice != "" && newRecommendedPrice != undefined && newRecommendedPrice != "") {
            		if( parseInt( newStandardPrice) >= parseInt(newRecommendedPrice) ) {
            			setTimeout(function() {
            				fnKendoMessage({
                                message : '원가는 정상가와 동일하거나 높을 수 없습니다.',
                                ok : function focusValue() {
                                	priceList[rowIndex-1].standardPrice = 0;
                                	priceList[rowIndex-1].recommendedPrice = 0;
                                	viewModel.orginPriceList.sync();
                                	return;
                                }
                            });
            			});

            		}else {
            			// 마진율 계산
            			this.fnRatioCalculation(e.data);
            		}
            	}
            },
            fnNewRecommendedPriceChange : function(e) {

            	e.preventDefault();

            	let rowIndex = viewModel.orginPriceList.data().length;
            	var priceList = viewModel.orginPriceList.data();
            	var newStandardPrice = e.data.standardPrice;
            	var newRecommendedPrice = e.data.recommendedPrice;

            	if( parseInt(newRecommendedPrice) <= 0 ) {

        			setTimeout(function() {
            			fnKendoMessage({
                            message : '정상가는 0원 이하로 입력하실수 없습니다. <br>가격정보를 확인해주세요',
                            ok : function focusValue() {
                            	//priceList[rowIndex-1].standardPrice = 0;
                            	priceList[rowIndex-1].recommendedPrice = '';
                            	priceList[rowIndex-1].priceRatio = 0;
                            	viewModel.orginPriceList.sync();
                            	return;
                            }
                        });
        			});
        		}

            	if(newStandardPrice != undefined && newStandardPrice != "" && newRecommendedPrice != undefined && newRecommendedPrice != "") {
            		if( parseInt( newStandardPrice) >= parseInt(newRecommendedPrice) ) {

            			setTimeout(function() {
	            			fnKendoMessage({
	                            message : '정상가는 원가보다 낮게 입력할 수 없습니다.<br>가격정보를 확인해주세요',
	                            ok : function focusValue() {
	                            	//priceList[rowIndex-1].standardPrice = 0;
	                            	priceList[rowIndex-1].recommendedPrice = '';
	                            	priceList[rowIndex-1].priceRatio = 0;
	                            	viewModel.orginPriceList.sync();
	                            	return;
	                            }
	                        });
            			});
            		} else {
            			// 마진율 계산
            			this.fnRatioCalculation(e.data);
            		}
            	}


            },
            fnRatioCalculation : function(dataItem) {
//            	let rowIndex = viewModel.orginPriceList.data().length;
//            	var priceList = viewModel.orginPriceList.data();
            	var newStandardPrice = parseInt(dataItem.standardPrice);
            	var newRecommendedPrice = parseInt(dataItem.recommendedPrice);

            	let rowIndex = viewModel.orginPriceList.indexOf( dataItem );
                let priceList = viewModel.orginPriceList.at(rowIndex);


            	var ratioCal = "";

            	if(viewModel.itemInfo.get("taxYn") == true) {
            		ratioCal = 1.1;
            	}else {
            		ratioCal = 1.0;
            	}

            	var ratio = Math.floor( (newRecommendedPrice - (newStandardPrice * ratioCal)) / newRecommendedPrice * 100);
            	priceList.priceRatio = ratio;

//            	priceList[rowIndex-1].priceRatio = ratio;
            	viewModel.orginPriceList.sync();
            },
            fnAddPrice : function(e){ // 추가
                e.preventDefault();

                var startDate = viewModel.fnGetStartDate();
                var rowIndex = viewModel.orginPriceList.data().length;
            	let priceList = viewModel.orginPriceList.data();

                if(viewModel.itemInfo.get("newInit") == true) {
        			viewModel.set("isAddApprovalVisiable", false);
        			viewModel.set("isAddPriceEnabled", false);
                    $('#saveButton').show();   //저장버튼
        		}else {
        			viewModel.set("isAddApprovalVisiable", true);
                    viewModel.set("isAddPriceEnabled", false);

                    $('#apprDiv').show();   //승인
                    $('#saveButton').show();   //저장버튼

// 저장된 마지막 날짜 이후 가격 시작일 설정 S - 승인변경으로 코멘트 처리
//                    rowIndex = rowIndex -1;
//
//                    var lastDate = fnGetToday();
//                    for (var i = rowIndex; i >= 0; i--) {
//                    	if (priceList[i].managerUpdateYn == 'Y') {
//                    		rowIndex = i;
//                    		lastDate = fnFormatDate(priceList[rowIndex].priceApplyStartDate, 'yyyy-MM-dd');
//                    		break;
//                    	}
//                    	else
//                    		continue;
//                    }
//
//                    var today = fnGetToday();
//                    if (lastDate > today) {
//                    	startDate = lastDate;
//                    }
//                    else {
//                    	startDate = today;
//                    }
//                    startDate = kendo.parseDate( startDate, "yyyy-MM-dd");
//                    startDate.setDate(startDate.getDate() + 1);
// 저장된 마지막 날짜 이후 가격 시작일 설정 E - 승인변경으로 코멘트 처리
        		}

                let endDate = "2999-12-31";

                var foodmerceLegalTypeCode = "FDM";
                var lohasLegalTypeCode = "PGS";      // 건강생활 법인코드

				var newStandardPrice = '';
				var newRecommendedPrice = '';
				var ratio = 0;

//				if(viewModel.itemInfo.get("isErpItemLink") == true) {
//					if(viewModel.itemInfo.get('erpLegalTypeCode') == foodmerceLegalTypeCode ) {
//						newStandardPrice = this.fnGetStandPrice();
//					}else if(viewModel.itemInfo.get('erpLegalTypeCode') == lohasLegalTypeCode) {
//						if(viewModel.itemInfo.get('productType') == '상품')  {
//							newStandardPrice = this.fnGetStandPrice();
//						}
//					}else {
//						newStandardPrice = this.fnGetStandPrice();
//					}
//				}

	    		// 원가, 정상가 수정 가능 여부(A:원가/정상가, R:정상가) S
//				if (viewModel.itemInfo.get("priceManageTp") == 'R')
//					newStandardPrice = this.fnGetStandPrice();
	    		// 원가, 정상가 수정 가능 여부(A:원가/정상가, R:정상가) E

				// 신규 등록시 삭제 후 -> 추가시 기존값 다시 셋팅
				if(viewModel.itemInfo.get("newInit") == true) {
					newStandardPrice = viewModel.itemInfo.get("standardPriceOrig");
            		newRecommendedPrice = viewModel.itemInfo.get("recommendedPriceOrig");

            		var ratioCal = 0;
            		if(viewModel.itemInfo.get("taxYn") == true) {
                		ratioCal = 1.1;
                	}else {
                		ratioCal = 1.0;
                	}

            		if(newStandardPrice != "" && newRecommendedPrice != "") {
            			ratio = Math.floor( (newRecommendedPrice - (newStandardPrice * ratioCal)) / newRecommendedPrice * 100);
            		}
            	} else {
            	    // 수정 추가시 원가, 정상가 설정
            	    newStandardPrice = this.fnGetStandPrice();
            	    newRecommendedPrice = this.fnGetRecommendedPrice();
            	}

                viewModel.orginPriceList.add(
                {
                  ilItemCode : viewModel.itemInfo.ilItemCd,
                  updateTypeName : "관리자",
				  systemUpdateYn : "N",
				  managerUpdateYn : "Y",
                  approvalStatusCode : "APPR_STAT.NONE",
                  approvalStatusCodeName : "승인대기",
                  priceApplyStartDate : kendo.toString(startDate, "yyyy-MM-dd"),
                  disableStartDates : disableStartDates,
                  priceApplyEndDate : endDate,
                  standardPrice : newStandardPrice,
                  standardPriceChange : newStandardPrice,
                  recommendedPrice : newRecommendedPrice,
                  recommendedPriceChange : newRecommendedPrice,
                  priceRatio : ratio,
                  approval1st : "",
                  approvalConfirm : "",
                });
//                $("#priceApplyStartDate").attr("readonly", true);

//                let rowIndex = viewModel.orginPriceList.total() - 1;
//
//                viewModel.fnDiscountCalculation(rowIndex);
//                viewModel.fnAddDiscountButtonEnableSetting();
            },
            fnPriceDeleteRow : function(e) {
            	e.preventDefault();

                let message = "삭제하시겠습니까?";
				if(e.data.approvalStatusCode == "APPR_STAT.APPROVED") {
					message = "승인완료된 정보가 자동폐기 됩니다. 삭제하시겠습니까?";
				}else if(e.data.approvalStatusCode == "APPR_STAT.REQUEST"){
				    message = "승인요청하신 목록이 요청철회 됩니다. 삭제하시겠습니까?";
				}else if(e.data.approvalStatusCode == "APPR_STAT.NONE"){
                    message = "삭제하시겠습니까?";
				}else{ // 1차 승인완료(APPR_STAT.SUB_APPROVED)인 경우 삭제할 수 없음
				    return;
				}

                fnKendoMessage({
                    type    : "confirm",
                    message : message,
                    ok      : function(){
                        if( e.data.approvalStatusCode == "APPR_STAT.APPROVED" ){
                            viewModel.fnPriceDelete(e.data);
                        }else if(e.data.approvalStatusCode == "APPR_STAT.REQUEST"){
                            viewModel.fnPriceRequestCancel(e.data);
                        }else{
                            $('#apprDiv').hide();   //승인
                            $('#saveButton').hide();   //저장버튼
                            viewModel.fnPriceDatasourceRemove(e.data);
                        }
                    },
                    cancel  : function(){

                    }
                });
            },
            fnPriceRequestCancel : function(dataItem) {
                let params = {};
                params.ilItemPriceApprIdList = [];
                params.ilItemPriceApprIdList[0] = dataItem.ilItemPriceApprId;

            	fnAjax({
                    url	 : "/admin/approval/item/putCancelRequestApprovalItemPrice",
                    params  : params,
                    contentType : "application/json",
                    success : function( data ){
                    	if (data.failCount == '0') {
                            fnKendoMessage({ message : "요청철회 되었습니다." });
                    		viewModel.fnPriceDatasourceRemove(dataItem);					//삭제 처리한 행 화면에서 삭제
                    	}
                    	else
                            fnKendoMessage({ message : "요청철회에 실패하였습니다." });
                    },
                    error : function(xhr, status, strError){
                        fnKendoMessage({ message : xhr.responseText });
                    },
                    isAction : "update"
                });
            },
            fnPriceDelete : function( dataItem ){ // 금액 삭제
				fnAjax({
					url	 : "/admin/approval/item/delItemPriceOrig",
					params  : { ilItemPriceOrigId : dataItem.ilItemPriceOrigId, ilItemCd : viewModel.itemInfo.ilItemCd, ilItemPriceApprId : dataItem.ilItemPriceApprId },
					method : "POST",
					contentType : "application/json;charset=UTF-8",
					success : function( data ){
                        fnKendoMessage({ message : "삭제 하였습니다." });
						viewModel.fnPriceDatasourceRemove(dataItem);					//삭제 처리한 행 화면에서 삭제
					},
					error : function(xhr, status, strError){
						fnKendoMessage({ message : xhr.responseText });
					},
					isAction : "update"
				});
			},
			fnPriceDatasourceRemove : function( dataItem ){ // datasource 삭제 제어
//                viewModel.goodsDiscountList.remove(goodsDiscountInfo);
//                viewModel.fnAddDiscountButtonEnableSetting();
//
//                $("#dGrid").empty();								//가격계산 Grid Delete
//                viewModel.set("visibleGoodsPackageCalcBtn", false)	//가격계산, 초기화 Button Visible
//                viewModel.set("visibleGoodsPackageSaveBtn", false);	//저장 Button Visible
//                viewModel.set("visibleDGridTitle", false);
//                viewModel.set("visibleDGrid", false);

//				viewModel.orginPriceList.remove(dataItem);
                fnGetItemPriceList(dataItem);

                fnSetDisableStartDates(); // 설정 불가한 품목 가격 시작일 설정

                viewModel.set("isAddPriceEnabled", true);
                viewModel.set("isAddApprovalVisiable", false);
                fnKendoMessage({ message : "삭제 하였습니다." });
            },
			fnSave : function(e) {
				e.preventDefault();

	    		// 원가, 정상가 수정 가능 여부(A:원가/정상가, R:정상가) S
				if (!(viewModel.itemInfo.get("priceManageTp") == 'A' || viewModel.itemInfo.get("priceManageTp") == 'R')) {
                    fnKendoMessage({message : '해당 품목은 품목 가격을 수정할 수 없습니다.'});
                    return;
				}
	    		// 원가, 정상가 수정 가능 여부(A:원가/정상가, R:정상가) E

				// 가격 validation S
				var priceData;
				if(viewModel.itemInfo.get("newInit") == true) {
					priceData = viewModel.orginPriceList.data().at(0);
                }
                else {
                    var rowIndex = viewModel.orginPriceList.data().length;
                    priceData = viewModel.orginPriceList.data().at(rowIndex - 1);
                }

				var newStandardPrice = priceData.standardPrice;
            	var newRecommendedPrice = priceData.recommendedPrice;
            	if (fnIsEmpty(newStandardPrice)) {
    				fnKendoMessage({
                        message : '원가는 필수 항목입니다.<br>가격정보를 확인해주세요.',
                    });
    				return;
            	}
            	else if (fnIsEmpty(newRecommendedPrice)) {
    				fnKendoMessage({
                        message : '정상가는 필수 항목입니다.<br>가격정보를 확인해주세요.',
                    });
    				return;
            	}
            	else if (parseInt(newRecommendedPrice) <= 0) {
    				fnKendoMessage({
                        message : '정상가는 0원 이하로 입력하실 수 없습니다.<br>가격정보를 확인해주세요.',
                    });
    				return;
            	}
            	else if (parseInt( newStandardPrice) >= parseInt(newRecommendedPrice)) {
    				fnKendoMessage({
                        message : '정상가는 원가보다 낮게 입력할 수 없습니다.<br>가격정보를 확인해주세요.',
                    });
    				return;
            	}
				// 가격 validation E

				// 품목 가격 승인, 상품 할인 승인 중복 체크
                fnAjax({
                    url     : "/admin/approval/auth/checkDuplicatePriceApproval",
                    method : "GET",
                    async : false,
                    params  : {"taskCode" : "APPR_KIND_TP.ITEM_PRICE", "itemCode" : viewModel.itemInfo.ilItemCd},
                    success : function(data){
                        if( viewModel.fnSaveValid() ){

                            fnKendoMessage({
                                type : "confirm",
                                message : "저장하시겠습니까?",
                                ok : function() {

                                    if(viewModel.itemInfo.get("newInit") == true) {
                                        // 신규등록시
                                        parent.POP_PARAM = viewModel.orginPriceList.data();
                                        parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
                                    }else {
                                        // 수정시
//			            		fnAjax({
//									url	 : "/admin/item/master/register/addItemPriceOrig",
//									params  : {
//												priceList : viewModel.orginPriceList.data(),
//											},
//									isAction : "insert",
//									contentType : 'application/json',
//									success : function( data ){
//
//										fnKendoMessage({
//						                    message : "가격정보가 저장되었습니다.",
//						                    ok : function focusValue() {
//						                    	parent.POP_PARAM = viewModel.orginPriceList.data();
//						                    	parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
//						                    }
//						                });
//									},
//									error : function(xhr, status, strError){
//										fnKendoMessage({ message : xhr.responseText });
//									},
//
//								});

                                        // 요청값 설정
                                        let rowIndex = viewModel.orginPriceList.data().length;
                                        let params = viewModel.orginPriceList.data().at(rowIndex - 1);

                        	    		// 원가, 정상가 수정 가능 여부(A:원가/정상가, R:정상가) S
                                        params.priceManageTp = viewModel.itemInfo.get("priceManageTp");
                        	    		// 원가, 정상가 수정 가능 여부(A:원가/정상가, R:정상가) E

                                        // 승인요청처리 체크
                                        let targetGridArr = $("#apprGrid").data("kendoGrid").dataSource.data()
                                        if (targetGridArr == undefined || targetGridArr == null || targetGridArr.length < 1) {
                                            fnKendoMessage({message : '승인받으실 관리자를 지정해주세요.'});
                                            return false;
                                        }

                                        // 승인자정보 Set
                                        if (targetGridArr.length == 1) {
                                            // 2차승인자만 있는 경우
                                            if (fnIsEmpty(targetGridArr[0].apprUserId) == false) {
                                                params.approvalUserId = targetGridArr[0].apprUserId;
                                            }
                                        }
                                        else if (targetGridArr.length == 2) {
                                            // 1차승인자/2차승인자 모두있는 경우
                                            // 1차승인자
                                            if (fnIsEmpty(targetGridArr[0].apprUserId) == false) {
                                                params.approvalSubUserId = targetGridArr[0].apprUserId;
                                            }
                                            // 2차승인자
                                            if (fnIsEmpty(targetGridArr[1].apprUserId) == false) {
                                                params.approvalUserId = targetGridArr[1].apprUserId;
                                            }
                                        }

                                        fnAjax({
                                            url	 : "/admin/approval/item/addApprovalItemPrice",
                                            params  : params,
                                            isAction : "insert",
                                            contentType : 'application/json',
                                            success : function( data ){

                                                fnKendoMessage({
                                                    message : "가격정보가 저장되었습니다.",
                                                    ok : function focusValue() {
                                                        parent.POP_PARAM = viewModel.orginPriceList.data();
                                                        parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
                                                    }
                                                });
                                            },
                                            error : function(xhr, status, strError){
                                                fnKendoMessage({ message : xhr.responseText });
                                            },

                                        });
                                    }

                                },
                                cancel : function() {
                                    return;
                                }
                            });

                        }
                    },
                    isAction : "select"
                });

			},
			fnSaveValid : function(){ // 데이터 검증
                let priceList = viewModel.orginPriceList.data();

                if( priceList.length == 0 ){
                    fnKendoMessage({ message : "저장할 값이 없습니다." });
                    return false;
                }

                var priceWaitCount = 0;

                for(var i = 0 ; i < priceList.length; i++){

                	if(priceList[i].approvalStatusCode == "APPR_STAT.NONE") {
                		priceWaitCount++;
                	}
                }
                if(priceWaitCount == 0) {
                	fnKendoMessage({ message : "저장할 값이 없습니다." });
                    return false;
                }


                for(var i = 0 ; i < priceList.length; i++){
                	if(priceList[i].approvalStatusCode == "APPR_STAT.NONE") {

                		if(priceList[i].standardPrice == undefined) {
                			fnKendoMessage({ message : "원가 정보가 없습니다." });
                			return false;
                		}

                		if(parseInt(priceList[i].recommendedPrice) == 0 || priceList[i].recommendedPrice == undefined) {
                			fnKendoMessage({ message : "정상가 정보가 없습니다." });
                          return false;
                		}
                	}
                }

                return true;
            }

        });

        kendo.bind($("#inputForm"), viewModel);
    };

    // 기본값 셋팅
    function fnDefaultValue(){
    	if( paramData == undefined || paramData.itemInfo == undefined ){
            fnKendoMessage({ message : "품목 가격정보를 위한 필수값이 없습니다.", ok : fnClose });
    	}
    	else {
    		viewModel.itemInfo.set("ilItemCd", paramData.itemInfo.ilItemCd);
    		viewModel.itemInfo.set("isErpItemLink", paramData.itemInfo.isErpItemLink);
    		viewModel.itemInfo.set("erpLegalTypeCode", paramData.itemInfo.erpLegalTypeCode);
    		viewModel.itemInfo.set("productType", paramData.itemInfo.productType);
    		viewModel.itemInfo.set("taxYn", paramData.itemInfo.taxYn);
    		viewModel.itemInfo.set("newInit", paramData.itemInfo.newInit);
    		viewModel.itemInfo.set("urSupplierId", paramData.itemInfo.urSupplierId);

    		if(viewModel.itemInfo.get("newInit") == true) {
    			viewModel.set("isAddApprovalVisiable", false);
    			viewModel.set("isAddPriceEnabled", false);
                $('#saveButton').show();   //저장버튼
    			viewModel.itemInfo.set("standardPriceOrig", paramData.itemInfo.standardPriceOrig);
    			viewModel.itemInfo.set("recommendedPriceOrig", paramData.itemInfo.recommendedPriceOrig);
    		}

    		// 원가, 정상가 수정 가능 여부(A:원가/정상가, R:정상가) S
            var foodmerceLegalTypeCode = "FDM"; // 푸드머스 법인코드
            var lohasLegalTypeCode = "PGS";      // 건강생활 법인코드
            var greenJuiceSupplierId = "4";      // 녹즙 공급업체 ID
    		viewModel.itemInfo.set("priceManageTp", "");
    		if (paramData.itemInfo.isErpItemLink == true) {
				if (paramData.itemInfo.erpLegalTypeCode == foodmerceLegalTypeCode) { // 푸드머스
		    		viewModel.itemInfo.set("priceManageTp", "R");
				}
				else if (paramData.itemInfo.erpLegalTypeCode == lohasLegalTypeCode) { // 건강생활
					if (paramData.itemInfo.productType == '상품') {
			    		viewModel.itemInfo.set("priceManageTp", "R");
					}
					else if (paramData.itemInfo.productType == '제품') {
						if (paramData.itemInfo.urSupplierId == greenJuiceSupplierId) {
				    		viewModel.itemInfo.set("priceManageTp", "R");
						}
						else {
				    		viewModel.itemInfo.set("priceManageTp", "A");
						}
					}
				}
			}
    		else {
	    		viewModel.itemInfo.set("priceManageTp", "A");
    		}
    		// 원가, 정상가 수정 가능 여부(A:원가/정상가, R:정상가) E

    		viewModel.orginPriceList.data(paramData.itemInfo.itemPriceList);

    		if (paramData.itemInfo.itemPriceList != null) {
            	for (var i = 0; i < paramData.itemInfo.itemPriceList.length; i++) {
                    if( paramData.itemInfo.itemPriceList[i].approvalStatusCode != "APPR_STAT.APPROVED" ){
            			viewModel.set("isAddPriceEnabled", false);
                        break;
                    }
    			}

                fnSetDisableStartDates(); // 설정 불가한 품목 가격 시작일 설정
    		}

    		var tooltip = $(".standPriceToolTip").kendoTooltip({ // 도움말 toolTip
                filter : "span",
                width : 300,
                position : "center",
                content: kendo.template($("#standPriceToolTip-template").html()),
                animation : {
                    open : {
                        effects : "zoom",
                        duration : 150
                    }
                }
            }).data("kendoTooltip");

        	var tooltip = $(".recommendedPriceToolTip").kendoTooltip({ // 도움말 toolTip
                filter : "span",
                width : 300,
                position : "center",
                content: kendo.template($("#recommendedPriceToolTip-template").html()),
                animation : {
                    open : {
                        effects : "zoom",
                        duration : 150
                    }
                }
            }).data("kendoTooltip");
        }
    };

    // 팝업 닫기
    function fnClose(){
    	parent.POP_PARAM = viewModel.orginPriceList.data();
        parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
    };

    //-------------------------------  Common Function end -------------------------------

    //-------------------------------  승인관리자 start -------------------------------
    //승인관리자 정보 Grid
    function fnApprAdminInit(){
        apprAdminGridDs =  new kendo.data.DataSource();

        apprAdminGridOpt = {
            dataSource : apprAdminGridDs,
            editable : false,
            noRecordMsg: '승인관리자를 선택해 주세요.',
            columns : [{
                field : 'apprAdminInfo',
                title : '승인관리자정보',
                width : '100px',
                attributes : {style : 'text-align:center'}
                },{
                field : 'adminTypeName',
                title : '계정유형',
                width : '100px',
                attributes : {style : 'text-align:center'}
                },{
                    field : 'apprUserName',
                    title : '관리자이름/아이디',
                    width : '100px',
                    attributes : {style : 'text-align:center'},
                    template : function(dataItem){
                        let returnValue;
                        returnValue = dataItem.apprUserName + '/' + dataItem.apprLoginId;
                        return returnValue;
                    }
                },{
                    field : 'organizationName',
                    title : '조직/거래처 정보',
                    width : '100px',
                    attributes : {style : 'text-align:center'}
                },{
                    field : 'teamLeaderYn',
                    title : '조직장여부',
                    width : '80px',
                    attributes : {style : 'text-align:center'}
                },{
                    field : 'userStatusName',
                    title : 'BOS 계정상태',
                    width : '80px',
                    attributes : {style : 'text-align:center'}
                },{
                    field : 'grantUserName',
                    title : '권한위임정보<BR/>(이름/ID)',
                    width : '100px',
                    attributes : {style : 'text-align:center'},
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
                    attributes : {style : 'text-align:left'},
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
                    title : '권한위임자<BR/>BOS 계정상태',
                    width : '100px',
                    attributes : {style : 'text-align:left'},
                    template : function(dataItem){
                        let returnValue;
                        if(dataItem.grantAuthYn == 'Y'){
                            returnValue = dataItem.grantUserStatusName;
                        }else{
                            returnValue = '';
                        }
                        return returnValue;
                    }
                },{
                    field:'addCoverageId', hidden:true
                },{
                    field:'includeYn', hidden:true
                }
            ]
        };

        apprAdminGrid = $('#apprGrid').initializeKendoGrid(apprAdminGridOpt).cKendoGrid();
        apprKindTp = "APPR_KIND_TP.ITEM_PRICE";
    }

    // 승인관리자 선택 팝업 호출
    function fnApprAdmin(){
//        console.log("### apprKindTp  ===>"+ apprKindTp);
        var param = {'taskCode' : apprKindTp };

//        console.log("#### param --->"+ JSON.stringify(param));
        fnKendoPopup({
            id		: 'approvalManagerSearchPopup',
            title	: '승인관리자 선택',
            src		: '#/approvalManagerSearchPopup',
            param	: param,
            width	: '1300px',
            heigh	: '800px',
            scrollable : "yes",
            success: function( stMenuId, data ){

                if(data && !fnIsEmpty(data) && data.authManager2nd){
                    $('#apprGrid').gridClear(true);

                    var authManager1 = data.authManager1st;
                    var authManager2 = data.authManager2nd;

                    if(authManager1.apprUserId != undefined){							//1차 승인관리자가 미지정이라면
                        var objManager1 = new Object();

                        objManager1["apprAdminInfo"] = "1차 승인관리자";
                        objManager1["adminTypeName"] = authManager1.adminTypeName;
                        objManager1["apprUserName"] = authManager1.apprUserName;
                        objManager1["apprKindTp"] = authManager1.apprKindType;
                        objManager1["apprManagerTp"] = authManager1.apprManagerType
                        objManager1["apprUserId"] = authManager1.apprUserId;
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
                    }

                    var objManager2 = new Object();

                    objManager2["apprAdminInfo"] = "2차 승인관리자";
                    objManager2["adminTypeName"] = authManager2.adminTypeName;
                    objManager2["apprUserName"] = authManager2.apprUserName;
                    objManager2["apprKindTp"] = authManager2.apprKindType;
                    objManager2["apprManagerTp"] = authManager2.apprManagerType
                    objManager2["apprUserId"] = authManager2.apprUserId;
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
                }
            }
        });
    }

    function fnGetItemPriceList(dataItem) { // 해당 품목코드로 가격 정보 조회
        if (dataItem.approvalStatusCode == "APPR_STAT.NONE") { // 화면에서 추가 후 삭제시는 해당 row만 삭제
			viewModel.orginPriceList.remove(dataItem);
        	return;
        }

        fnAjax({
            url : "/admin/item/master/modify/getItemPriceSchedule",
            method : 'GET',
            params : {
                ilItemCode : viewModel.itemInfo.ilItemCd,  //품목 코드
            },
            isAction : 'select',
            success : function(data, status, xhr) {

                var rowIndex = viewModel.orginPriceList.data().length;
                var priceData = viewModel.orginPriceList.data().at(rowIndex - 1); // 화면에서 추가한 내역은 마지막 row에 쌓이기 때문에 마지막 row는 향후 처리를 위해 임시로 저장

            	if( data['rowsPopup'] && data['rowsPopup'].length > 0 ) {
            		viewModel.orginPriceList.data(data['rowsPopup']);
                }

            	if(priceData.approvalStatusCode == "APPR_STAT.NONE") { // 화면에서 추가한 내역이 있으면 해당  row 추가
                    viewModel.orginPriceList.add(priceData);
            	}
            	viewModel.orginPriceList.sync();
            }
        });
    }

    // 설정 불가한 품목 가격 시작일 설정
    function fnSetDisableStartDates() {
    	disableStartDates = [];
    	var priceList = viewModel.orginPriceList.data();
        for(var i = 0 ; i < priceList.length; i++){
        	if (priceList[i].managerUpdateYn == 'Y') {
        		disableStartDates.push(new Date(priceList[i].priceApplyStartDate));
        	}
        }
    }

    // 승인관리자 그리드 초기화
    function fnApprClear(){
        $('#apprGrid').gridClear(true);
        $('#apprSubUserId').val('');
        $('#apprUserId').val('');
    }

    function fnApprDetailPopup(){
		if( viewModel.ilItemApprId == null && viewModel.ilItemApprId == undefined){
			fnKendoMessage({ message : "승인 내역에 문제가 있습니다. 관리자에게 문의하세요." });
			return;
		}

		fnKendoPopup({
			id		: 'itemApprovalDetailPopup',
			title	: "품목등록 승인내역",
			src		: '#/approvalHistoryPopup',
			param	: { "taskCode" : "APPR_KIND_TP.ITEM_REGIST", "taskPk" : viewModel.ilItemApprId},
			width	: '680px',
			height	: '585px',
			success	: function( id, data ){
			}
		});
	}

    $scope.fnApprAdmin = function(){fnApprAdmin();};				//승인관리자 지증
    $scope.fnApprClear = function(){fnApprClear();};				//승인관리자 초기화
    $scope.fnApprDetailPopup = function() {fnApprDetailPopup();};	//승인내역 상세보기 팝업

    //-------------------------------  승인관리자 end -------------------------------
});
