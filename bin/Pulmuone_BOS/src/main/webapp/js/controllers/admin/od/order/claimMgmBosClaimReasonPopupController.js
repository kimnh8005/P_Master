/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > BOS클레임사유 변경 팝업
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.05.04		최윤지          최초생성
 * **/
"use strict";
var viewModel, gridDs, gridOpt, grid;
var claimMgmPopupOrderGoodsGridDs, claimMgmPopupOrderGoodsGridOpt, claimMgmPopupOrderGoodsGrid;
var pageParam = fnGetPageParam();
var paramData = parent.POP_PARAM['parameter'];
var claimReasonData;
var LClaimCtgryDropDownList = [];
var MClaimCtgryDropDownList = [];
var SClaimCtgryDropDownList = [];
var sClaimCtgryDropDownListData = [];

$(document).ready(function() {
	importScript("/js/service/od/claim/claimMgmFunction.js");
	importScript("/js/service/od/order/orderMgmGridColumns.js");
	importScript("/js/controllers/admin/ps/claim/searchCtgryComm.js");

	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({
			PG_ID  : "claimMgmBosClaimReasonPopup",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){
		setTimeout(function() {
			fnInitButton();
			fnInitGrid();
			initOrderGoodList();
			fnInitOptionBox();
		}, 200);
	};

	//--------------------------------- Button Start---------------------------------
	// 버튼 초기화
	function fnInitButton(){
		$("#fnSave").kendoButton();
	};

	//--------------------------------- Button End-----------------------------------

	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid(){

		let form = {};
		form.odClaimId = paramData.odClaimId; //주문pk
		form.odOrderId = paramData.odOrderId; //주문클레임pk
		form.orderStatusCd = paramData.orderStatusCd; //주문클레임pk
		form.goodSearchList = paramData.goodSearchList; //주문상세pk
		form.claimStatusCd = paramData.putOrderStatusCd; // 변경 클레임 주문 상태

		fnAjax({
	       	url     : "/admin/order/claim/getOrderClaimBosClaimReasonView",
	       	params  : form,
	       	contentType: "application/json",
	 		success : function( data ){
	 				//상품정보 그리드
	 				claimMgmPopupOrderGoodsGrid.dataSource.data(data.orderGoodList);

	 				setTimeout(function() {
						//클레임정보
						initClaimInfo(data.orderGoodList);
					}, 200);
			},
	 		isAction : "select"
 		});
	};

	//클레임 상세 > 클레임 정보
	function initClaimInfo(orderGoodList) {
		claimMgmFunctionUtil.initClaimInfoForBosClaimReason(orderGoodList, paramData);

		//bos클레임사유
		for(let i=0; i<orderGoodList.length; i++){
			fnDropDownListInit(i);
		}

		for(let i=0; i<orderGoodList.length; i++){
			setTimeout(function(){
				MClaimCtgryDropDownList[i].enable(false);
				SClaimCtgryDropDownList[i].enable(false);

				LClaimCtgryDropDownList[i].bind('change', function(e) {
					if(0 === this.selectedIndex) {
						MClaimCtgryDropDownList[i].enable(false);
						SClaimCtgryDropDownList[i].enable(false);
					}
					else {
						MClaimCtgryDropDownList[i].enable(true);
					}

					$("#targetType_"+i).val("");
				});

				MClaimCtgryDropDownList[i].bind('change', function(e) {
					if(0 === this.selectedIndex) SClaimCtgryDropDownList[i].enable(false);
					else SClaimCtgryDropDownList[i].enable(true);

					$("#targetType_"+i).val("");
				});

				SClaimCtgryDropDownList[i].unbind("change").bind('change', function(e){
					var psClaimCtgryId = this.value();
					var claimName = this.text();
					var iid = $(this)[0].element[0].id.replace("sclaimCtgryId_", ""); // id ="abcd_"+i 값
					var sClaimData = sClaimCtgryDropDownListData[i];

					if ($("#eachGoodsReasonSelect").is(":checked") == false){
						iid = 0;
					}
					if (sClaimData != undefined) {
						for (var j = 0; j < sClaimData.length; j++) {
							if (sClaimData[j].psClaimCtgryId == psClaimCtgryId) {
								$('.bosClaimResultView').show();

								$("#psClaimBosId_" + iid).text(sClaimData[j].psClaimBosId); //psClaimBosId
								$("#psClaimBosSupplyId_" + iid).text(sClaimData[j].psClaimBosSupplyId); //psClaimBosSupplyId
								$("#targetType_" + iid).text(sClaimData[j].targetTypeName);
								if (orderGoodList[i].returnsYn == 'N') {
									if ($.trim(sClaimData[j].nclaimName) != "") {
										$("#supplierName_" + iid).text(sClaimData[j].supplierName + ' : ' + sClaimData[j].nclaimName);
									}
								} else {
									if ($.trim(sClaimData[j].yclaimName) != "") {
										$("#supplierName_" + iid).text(sClaimData[j].supplierName + ' : ' + sClaimData[j].yclaimName);
									}
								}

								if (paramData.putOrderStatusCd == 'RC') {
									var targetTp = "S";
									if (sClaimData[j].targetTypeName == "구매자 귀책") {
										targetTp = "B";
									}

									// let form = {};
									// form.odClaimId = paramData.odClaimId; //주문pk
									// form.odOrderId = paramData.odOrderId; //주문클레임pk
									// form.orderStatusCd = paramData.orderStatusCd; //주문클레임pk
									// form.claimStatusCd = paramData.putOrderStatusCd; // 변경 클레임 주문 상태
									// //form.goodSearchList = paramData.goodSearchList; //주문상세pk
									// setGridGoodsData(form);
									// form.targetTp = targetTp;
									// form.recvZipCd = $("#zipCode").val();
									// form.returnsYn = $("input[name=withDraw]:checked").val();
									// //form.
									//
									// refreshGoodsGrid(form);
								}
							}
						}
					}
				});
				let bosClaimLargeId		= orderGoodList[i].bosClaimLargeId;
				let bosClaimMiddleId	= orderGoodList[i].bosClaimMiddleId;
				let bosClaimSmallId		= orderGoodList[i].bosClaimSmallId;

				var lCtgryList = $(LClaimCtgryDropDownList[i].element).data("kendoDropDownList");
				var mCtgryList = $(MClaimCtgryDropDownList[i].element).data("kendoDropDownList");
				var sCtgryList = $(SClaimCtgryDropDownList[i].element).data("kendoDropDownList");

				lCtgryList.value(bosClaimLargeId);
				lCtgryList.trigger("change");

				mCtgryList.value(bosClaimMiddleId);
				mCtgryList.trigger("change");

				sCtgryList.value(bosClaimSmallId);

				setTimeout(function () {
					sCtgryList.trigger("change");
				}, 1000)


			}, 500)
		}


	};

	//클레임 상세 > 상품정보
	function initOrderGoodList(){
		claimMgmPopupOrderGoodsGridOpt = {
				navigatable : true,
				scrollable : true,
				editable : true,
				columns : orderMgmGridUtil.claimMgmPopupOrderGoodsList()
			};

		claimMgmPopupOrderGoodsGrid = $('#claimMgmPopupOrderGoodsGrid').initializeKendoGrid( claimMgmPopupOrderGoodsGridOpt ).cKendoGrid();
	};

	//-------------------------------  Grid End  -------------------------------
	//-------------------------------- fn Start --------------------------------------
	function validClaimReason() {
		let validFlag = true;
		// BOS 클레임 사유 > 상품별 사유 버튼 클릭 했을 경우
		let liArea = $("li.bosClaimReasonList__listItem");
		if($("#eachGoodsReasonSelect").is(":checked")) {
			liArea.each(function() {
				if(	$(this).find("input[name=lclaimCtgryId]").val() == "" ||
					$(this).find("input[name=mclaimCtgryId]").val() == "" ||
					$(this).find("input[name=sclaimCtgryId]").val() == "") {
					fnKendoMessage({message:$(this).find("span.claimMgm__goodsNm").text() + "<br/>BOS 클레임 사유를 선택 해주세요."});
					validFlag = false;
					return false;
				}
			});
		}
		// BOS 클레임 사유 > 상품별 사유 버튼 클릭 하지 않았을 경우
		else {
			if(	liArea.eq(0).find("input[name=lclaimCtgryId]").val() == "" ||
				liArea.eq(0).find("input[name=mclaimCtgryId]").val() == "" ||
				liArea.eq(0).find("input[name=sclaimCtgryId]").val() == "") {
				fnKendoMessage({message:"BOS 클레임 사유를 선택 해주세요."});
				validFlag = false;
			}
		}

		return validFlag;
	}

	//변경(저장)
    function fnSave() {
    	var data = $('#inputForm').formSerialize(true);
			if( data.rtnValid ){
				var url = "/admin/order/claim/putOrderClaimDetlBosClaimReason";
				//--> 상품정보 [OD_CLAIM_DETL]
				data.goodsInfoList = claimInfoRegisterUtil.goodsInfoListForBosClaimReason(data, paramData);
				//--> BOS클레임 사유 SET
				//claimInfoRegisterUtil.odClaimCtgrySetting(data, paramData);

				fnAjax({
	                url     : url,
	    	       	params  : data,
	    	       	contentType: "application/json",
	                success :
	                    function( data ){
							fnKendoMessage({
								message:"변경되었습니다.",
								ok:function(){
									paramData.successDo = true;
									fnClose();
								}
							});
                    },
                    error :
                    	function (xhr, status, strError) {
                    	let resultData = xhr.responseJSON;
                    	fnKendoMessage({
	    					message: resultData.message
	    				});
                    },
                    isAction : 'batch'
	            });
			}
	};

	//닫기
    function fnClose(){
        parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
    };

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){
		//사유
		claimMgmEventUtil.psClaimMallIdForBosClaimReason(paramData, claimReasonData);
	};
	//---------------Initialize Option Box End ------------------------------------------------
	// 상품 그리드 새로고침
	function refreshGoodsGrid(param) {
		fnAjax({
	       	url     : "/admin/order/claim/getOrderClaimView",
	       	params  : param,
	       	contentType: "application/json",
	 		success : function( data ){
 				//환불정보 폼
 				initRefundInfo(data.priceInfo, data.goodsCouponList, data.cartCouponList);
 				// 재배송일 경우
 				if(param.putOrderStatusCd == 'EC') {
					//출고정보 리스트
					initOrderClaimReleaseList(data.deliveryInfoList);
				}
	 		},
	 		isAction : "select"
 		});
	}

	//BOS 클레임사유 드롭다운리스트
	function fnDropDownListInit(i){

    	LClaimCtgryDropDownList[i] = searchCtgryCommonUtil.LClaimCtgryDropDownListById(i);
    	MClaimCtgryDropDownList[i] = searchCtgryCommonUtil.MClaimCtgryDropDownListById(i);
		SClaimCtgryDropDownList[i] = searchCtgryCommonUtil.SClaimCtgryDropDownListById(i);

    }

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	$scope.fnSave = function() { fnSave();	}; //변경(저장)
	$scope.fnClose = function(){ fnClose(); };
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
