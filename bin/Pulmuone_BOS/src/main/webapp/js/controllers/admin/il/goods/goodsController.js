/**-----------------------------------------------------------------------------
 * description 		 : 상품리스트
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.08.04		박영후          최초생성
 * @ 2020.08.20     손진구          퍼블수정 및 기능개발
 * @
 * **/
"use strict";


var PAGE_SIZE = 50;
var viewModel, goodsGridDs, goodsGridOpt, goodsGrid;
var pageParam = fnGetPageParam();													//GET Parameter 받기
var paramGoodsId = pageParam.paramIlGoodsId;
var paramDataSort = pageParam.paramDataSort;

var initSearch = true;

$(document).ready(function() {

	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : true });

		fnPageInfo({
			PG_ID  : "goods",
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

		//탭 변경 이벤트
        fbTabChange();

        $("tr[name=viewHideArea]").hide();
        $("#fnSearchToggle span").html("▼");

//        viewModel.fnGridDataSort();
        if(paramGoodsId){ //상품 저장에서 넘어온 상태라면
    		viewModel.searchInfo.set("searchCondition", "GOODS_CODE");
    		if(paramGoodsId != undefined) {
    			viewModel.searchInfo.set("findKeyword", paramGoodsId);
    		}
    		if(paramDataSort != undefined) {
    			viewModel.searchInfo.set("gridDataSort", paramDataSort);
    		}
    		viewModel.searchInfo.saleStatus.push("SALE_STATUS.SAVE");
    		var renewURL = window.location.href.slice(0, window.location.href.indexOf('?'));		//URL 파라미터 초기화(삭제)
	        history.pushState(null, null, renewURL);

	        $("#fnSearch").trigger("click");

	        paramGoodsId = null; //상품 저장에서 넘어온 처음만 검색되도록 paramGoodsId를 초기화 한다.
    	}

	};

	//--------------------------------- Button Start---------------------------------

	// 버튼 초기화
	function fnInitButton(){
		$("#fnSearch,  #fnClear").kendoButton();
	};

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid(){

	    goodsGridDs = fnGetPagingDataSource({
			url      : "/admin/goods/list/getGoodsList",
			pageSize : PAGE_SIZE
		});

	    goodsGridOpt = {
			dataSource: goodsGridDs,
			pageable  : { pageSizes: [20, 30, 50, 100], buttonCount : 10 },
			navigatable : true,
            height : 755,
			scrollable : true,
			columns : [
                        { field : "chk", headerTemplate : '<input type="checkbox" id="checkBoxAll" />',
                          template : '<input type="checkbox" name="rowCheckbox" class="k-checkbox" />',
                          width : "32px", attributes : {style : "text-align:center;"}, locked: true, lockable: false
                        }
                      , { field : "itemCode", title : "품목코드/<BR>품목바코드", width : "140px", attributes : {style : "text-align:center;"},
                          template : function(dataItem){
                              let html = "<a kind='itemDetlInfo' style='cursor:pointer' >";
                              let itemCodeView = dataItem.itemCode;

                              if( dataItem.itemBarcode != "" ){
                                    itemCodeView += "/<br>" + dataItem.itemBarcode;
                              } else {
                                    itemCodeView += "/ - ";
                              }

                              html += itemCodeView + '</a>'
                              return html;
                          }, locked: true
                        }
                      , { field : "goodsId", title : "상품코드", width : "70px", attributes : { style : "text-align:center" }, locked: true }
				      , { field : "goodsName", title : "프로모션명<BR>상품명", width : "290px", attributes : { style : "text-align:center" },
				          template : function(dataItem){
                              let imageUrl = dataItem.goodsImagePath ? viewModel.publicStorageUrl + dataItem.goodsImagePath : '/contents/images/noimg.png';
                              let extinction = "";
                              if(dataItem.extinctionYn == "Y" && dataItem.saleStatusCode == "SALE_STATUS.STOP_SALE") {
                            	  extinction = "(판매불가)";
                              }
                              return "<img src='" + imageUrl + "' width='50' height='50' align='left' />" + dataItem.promotionName + "<BR>" + extinction + dataItem.goodsName;
				          }, locked: true
                        }
				      , { command : [ { name : "수정",
				                        click : function(e) {
				                            e.preventDefault();
                                            let row = $(e.target).closest("tr");
				                            let rowData = this.dataItem(row);

				                            viewModel.fnGoodsInfo(rowData, e.ctrlKey);
				                        }, template: "<a role=\"button\" class=\"k-button k-button-icontext k-grid-수정\">수정</a>"
				                        , visible: function() { return fnIsProgramAuth("SAVE")}
				                      }
				                    ], title : "관리", width : "80px", attributes : { style : "text-align:center" }, locked: true
				        }
				      , { field : "standardPrice", title : "원가", width : "80px", attributes : { style : "text-align:center" }, format: "{0:n0}", locked: true }
				      , { field : "recommendedPrice", title : "정상가", width : "80px", attributes : { style : "text-align:center" }, format: "{0:n0}", locked: true }
                      , { title : "판매가<BR>(행사여부 표시)", attributes : { style : "text-align:center" }, width : "100px",
                          template : function(dataItem){
                              if( dataItem.discountTypeCode == "GOODS_DISCOUNT_TP.NOT_APPLICABLE" ){
                                return "<div class='inlineBlock textCenter' style='color:red;margin-bottom:3px;'>" + kendo.format("{0:n0}", dataItem.salePrice) + "</div><div class='textCenter'>(" + dataItem.discountTypeName + ")</div>";
                              }else if( dataItem.discountTypeCode == "GOODS_DISCOUNT_TP.NONE" ){
                            	  return "<div class='inlineBlock textCenter' style='margin-bottom:3px;'>" + kendo.format("{0:n0}", dataItem.salePrice) + "</div>";
                              }else{
                                return "<div class='inlineBlock textCenter' style='margin-bottom:3px;'>" + kendo.format("{0:n0}", dataItem.salePrice) + "</div><div class='textCenter'>(" + dataItem.discountTypeName + ")</div>";
                              }
                          }, locked: true
                        }
                      , { field : "goodsCreateDate", title : "상품등록일<BR>최근수정일", width : "170px", attributes : {style : "text-align:center;"},
                          template : function(dataItem){
                              return dataItem.goodsCreateDate + "<BR>" + fnNvl(dataItem.goodsModifyDate);
                          }
                        }
				      , { field : "goodsTypeName", title : "상품유형", width : "70px", attributes : { style : "text-align:center" } }
				      , { field : "purchaseYn", title : "구매유형", width : "120px", attributes : { style : "text-align:center" },
                          template : function(dataItem){
                              let purchaseYnName = "";

                              if( dataItem.purchaseMemberYn == "Y" && dataItem.purchaseEmployeeYn == "Y" && dataItem.purchaseNonmemberYn == "Y" ){

                                  purchaseYnName = "전체";
                              }else{

                                  let purchaseYnNameArr = [];

                                  if( dataItem.purchaseMemberYn == "Y" ){
                                      purchaseYnNameArr.push("일반회원");
                                  }

                                  if( dataItem.purchaseEmployeeYn == "Y" ){
                                      purchaseYnNameArr.push("임직원회원");
                                  }

                                  if( dataItem.purchaseNonmemberYn == "Y" ){
                                      purchaseYnNameArr.push("비회원");
                                  }

                                  purchaseYnName = purchaseYnNameArr.join(", ");
                              }

                              return purchaseYnName;
                          }
				        }
                      , { field : "displayWeb", title : "판매허용범위<BR>(PC/Mobile/App)", width : "120px", attributes : {style : "text-align:center;"},
                          template : function(dataItem){
                              let displayWebName = "";

                              if( dataItem.displayWebPcYn == "Y" && dataItem.displayWebMobileYn == "Y" && dataItem.displayAppYn == "Y" ){

                                  displayWebName = "전체";
                              }else{

                                  let displayWebNameArr = [];

                                  if( dataItem.displayWebPcYn == "Y" ){
                                      displayWebNameArr.push("PC");
                                  }

                                  if( dataItem.displayWebMobileYn == "Y" ){
                                      displayWebNameArr.push("Mobile");
                                  }

                                  if( dataItem.displayAppYn == "Y" ){
                                      displayWebNameArr.push("APP");
                                  }

                                  displayWebName = displayWebNameArr.join(", ");
                              }

                              return displayWebName;
                          }
                        }
                      , { field : "saleTypeName", title : "판매유형", width : "70px", attributes : { style : "text-align:center" } }
                      , { field : "presentYn", title : "선물하기<BR>허용", width: "60px", attributes : {style : "text-align:center;"}
	                      , template : function(dataItem){
	                    	  var returnVal = '';
	                    	  if (dataItem.presentYn == 'Y')
	                    		  returnVal = '허용';
	                    	  else if (dataItem.presentYn == 'N')
	                    		  returnVal = '불가';
	                    	  else if (dataItem.presentYn == 'NA')
	                    		  returnVal = '미대상';
	                    	  return returnVal;
	                      }
                      }
                      , { field : "approvalStatusName", title : "승인상태", width : "150px", attributes : { style : "text-align:center" },
		  					template: function(dataItem) {
								var html =	"";
								if (
									!fnIsEmpty(dataItem.goodsRegistApprStat)
									&& (dataItem.goodsRegistApprStat == 'APPR_STAT.REQUEST' || dataItem.goodsRegistApprStat == 'APPR_STAT.SUB_APPROVED')
								) {
									html = '상품등록<BR>/' + dataItem.goodsRegistApprStatNm;
								}
								else if (
									!fnIsEmpty(dataItem.itemClientApprStat)
									&& (dataItem.itemClientApprStat == 'APPR_STAT.REQUEST' || dataItem.itemClientApprStat == 'APPR_STAT.SUB_APPROVED')
								) {
									html = '거래처 품목수정<BR>/' + dataItem.itemClientApprStatNm;
								}
								else if (
									!fnIsEmpty(dataItem.goodsClientApprStat)
									&& (dataItem.goodsClientApprStat == 'APPR_STAT.REQUEST' || dataItem.goodsClientApprStat == 'APPR_STAT.SUB_APPROVED')
								) {
									html = '거래처 상품수정<BR>/' + dataItem.goodsClientApprStatNm;
								}
								else {
									if (!fnIsEmpty(dataItem.goodsRegistApprStat)) {
										html = '상품등록<BR>/' + dataItem.goodsRegistApprStatNm;
									}
								}

								if(fnIsProgramAuth("SAVE")) {
									if (
										!fnIsEmpty(dataItem.goodsRegistApprStat)
										&& (dataItem.goodsRegistApprStat == 'APPR_STAT.REQUEST' || dataItem.goodsRegistApprStat == 'APPR_STAT.SUB_APPROVED')
									) {
										html +=	"<br/><a role='button' class='k-button k-button-icontext' kind='goodsRegistApprovalInfo' >승인내역</a>";
									}
									else if (
										!fnIsEmpty(dataItem.itemClientApprStat)
										&& (dataItem.itemClientApprStat == 'APPR_STAT.REQUEST' || dataItem.itemClientApprStat == 'APPR_STAT.SUB_APPROVED')
									) {
										html +=	"<br/><a role='button' class='k-button k-button-icontext' kind='itemClientApprovalInfo' >승인내역</a>";
									}
									else if (
										!fnIsEmpty(dataItem.goodsClientApprStat)
										&& (dataItem.goodsClientApprStat == 'APPR_STAT.REQUEST' || dataItem.goodsClientApprStat == 'APPR_STAT.SUB_APPROVED')
									) {
										html +=	"<br/><a role='button' class='k-button k-button-icontext' kind='goodsClientApprovalInfo' >승인내역</a>";
									}
									else if (
										!fnIsEmpty(dataItem.goodsRegistApprStat)
										&& dataItem.goodsRegistApprStat != 'APPR_STAT.APPROVED'
									) {
										html +=	"<br/><a role='button' class='k-button k-button-icontext' kind='goodsRegistApprovalInfo' >승인내역</a>";
									}
		        				}
								return html;
							}
                      }
                      , { field : "saleStatusName", title : "판매상태", width : "100px", attributes : { style : "text-align:center" } }
                      , { field : "displayYn", title : "전시상태", width : "80px", attributes : { style : "text-align:center" },
                          template : function(dataItem){
                              return dataItem.displayYn == "Y" ? "전시" : "미전시";
                          }
                        }
                      , { field : "goodsOutMallSaleStatName", title : "외부몰 <BR> 판매상태", width : "100px", attributes : { style : "text-align:center" } }
                      , { field : "saleDate", title : "판매기간", width : "170px", attributes : {style : "text-align:center;"},
                          template : function(dataItem){
                              return dataItem.saleStartDate + "~<BR>" + dataItem.saleEndDate;
                          }
                        }
                      , { field : "categoryStandardDepthName", title : "표준 카테고리<BR>전시 카테고리", width: "400px", attributes : {style : "text-align:center;"},
                          template : function(dataItem){
                              return dataItem.categoryStandardDepthName + "<BR>" + dataItem.categoryDepthName;
                          }
                        }
                      , { field : "warehouseName", title : "출고처 정보", width: "130px", attributes : {style : "text-align:center;"} }
                      , { field : "supplierName", title : "공급업체", width: "150px", attributes : {style : "text-align:center;"} }
                      , { field : "brandName", title : "표준브랜드", width: "180px", attributes : {style : "text-align:center;"} }
                      , { field : "dpBrandName", title : "전시브랜드", width: "180px", attributes : {style : "text-align:center;"} }
                      , { field : "storageMethodTypeName", title : "보관방법", width: "80px", attributes : {style : "text-align:center;"} }
                      , { field : "stockOrderYn", title : "재고연동", width: "60px", attributes : {style : "text-align:center;"} }
                      ,	{ field:'preOrderTpNm'		  ,title : '재고<br>운영형태', width:'90px'	,attributes:{ style:'text-align:center' },
                    	  	template: function(dataItem) {
								var title = dataItem.preOrderNm;
								var preOrderYn = dataItem.preOrderYn;
								var html = '';

								if (title == null) {
									title = '';
								}

								html = "<strong>" + kendo.htmlEncode(title) + "</strong>"
								if (fnIsProgramAuth("EDIT_STOCK")) {
									if (dataItem.stockOrderYn == 'N') {
										html = html + "<br>" + "<a role='button' class='k-button k-button-icontext' kind='itemStockEditBtn' href='#'>변경</a>";

									} else if (dataItem.stockOrderYn == 'Y' && preOrderYn == 'Y') {
										html = html + "<br>" + "<a role='button' class='k-button k-button-icontext' kind='itemInfoEditBtn' href='#'>변경</a>";

									}
								}

								return html;
                    	  	}
                      	 }
						, { field:'stockClosingCount'  ,title : '전일마감재고/<br>재고미연동상품재고'					, width:'120px'	,attributes:{ style:'text-align:center' }}
						, { field:'stockConfirmedCount',title : '당일입고확정/<br>예정<br>(올가 off 재고)'	, width:'90px'	,attributes:{ style:'text-align:center' }
								  ,template: function(dataItem) {
									  var html = "";
							    if(dataItem.stockOrderYn == "Y") {
							    	html =	kendo.htmlEncode(dataItem.stockConfirmedCount) + "/" + kendo.htmlEncode(dataItem.stockScheduledCount) +
						            "<br>" + "(" + kendo.htmlEncode(dataItem.stockOfflineCount) + ")";
							    }else {
							    	html = "";
							    }

								return html;
								  }
							}
						, { title : '재고상세' , width:'85px'	,attributes:{ style:'text-align:center' }
							,template: function(dataItem) {
								var html =	"";
								if (fnIsProgramAuth("EDIT_STOCK")) {
									if(dataItem.stockOrderYn == "Y") {
										html =	"<a role='button' class='k-button k-button-icontext' kind='detailInfoBtn' href='#'>상세<BR>보기</a>";
									}else {
										html = "";
									}
								}
								return html;
							}
						}
                      , { field : "stockCount", title : "재고<BR>(정상/임박/폐기임박)", width: "120px", attributes : {style : "text-align:center;"}, hidden:true,
                          template : function(dataItem){
                              return dataItem.normalStockCount + "/" + dataItem.imminentStockCount + "/" + dataItem.disposalStockCount;
                          }
                        }
                      , { field : "additionalGoodsYn", title : "추가상품<BR>유무", width: "60px", attributes : {style : "text-align:center;"} }
                      , { field : "couponUseYn", title : "쿠폰사용", width: "60px", attributes : {style : "text-align:center;"} }
                      , { field : "mdRecommendYn", title : "MD추천", width: "60px", attributes : {style : "text-align:center;"}, lockable: false }
			]
		};

	    goodsGrid = $('#goodsGrid').initializeKendoGrid( goodsGridOpt ).cKendoGrid();

	    goodsGrid.bind("dataBound", function() {
            $('#countTotalSpan').text( kendo.toString(goodsGridDs._total, "n0") );
        });

	    // 그리드 전체선택 클릭
        $("#checkBoxAll").on("click", function(index){

            if( $("#checkBoxAll").prop("checked") ){

                $("input[name=rowCheckbox]").prop("checked", true);
            }else{

                $("input[name=rowCheckbox]").prop("checked", false);
            }
        });

        // 그리드 체크박스 클릭
        goodsGrid.element.on("click", "[name=rowCheckbox]" , function(e){

            if( e.target.checked ){
                if( $("[name=rowCheckbox]").length == $("[name=rowCheckbox]:checked").length ){
                    $("#checkBoxAll").prop("checked", true);
                }
            }else{
                $("#checkBoxAll").prop("checked", false);
            }
        });


        $('#goodsGrid').on("click", "a[kind=itemInfoEditBtn]", function(e) {
        	e.preventDefault();
			var dataItem = goodsGrid.dataItem($(e.currentTarget).closest("tr"));

			let params = {};

			params.ilItemWarehouseId	= dataItem.ilItemWarehouseId;

			fnKendoPopup({
				id			: "itemStockInfoEditPopup",
				title		: "선주문 여부",  // 해당되는 Title 명 작성
				width		: "525px",
				height		: "200px",
				scrollable	: "yes",
				src			: "#/itemStockInfoEditPopup",
				param		: params,
				success		: function( id, data ){
					if(data.parameter == undefined){
						$("#fnSearch").trigger("click");
					}
				}
			});
        });

        $('#goodsGrid').on("click", "a[kind=itemStockEditBtn]", function(e) {
        	e.preventDefault();
			var dataItem = goodsGrid.dataItem($(e.currentTarget).closest("tr"));

			let params = {};

	    	params.ilItemCd = dataItem.itemCode;
	    	params.itemNm = dataItem.itemNm;
	    	params.ilItemWarehouseId = dataItem.ilItemWarehouseId;
	    	params.unlimitStockYn = dataItem.unlimitStockYn;
	    	params.notIfStockCnt = dataItem.notIfStockCnt;

	    	fnKendoPopup({
				id			: "itemNonInterfacedStockUpdatePopup",
				title		: "재고수정",  // 해당되는 Title 명 작성
				width		: "700px",
				height		: "250px",
				scrollable	: "yes",
				src			: "#/itemNonInterfacedStockUpdatePopup",
				param		: params,
				success		: function( id, data ){
					if(data.parameter == undefined){
						$("#fnSearch").trigger("click");
					}
				}
			});
        });



      //품목 상세보기 팝업
		$('#goodsGrid').on("click", "a[kind=detailInfoBtn]", function(e) {
			e.preventDefault();
			var dataItem = goodsGrid.dataItem($(e.currentTarget).closest("tr"));
			var nowDate = fnGetToday();
			var popupOption;
			if('GOODS_TYPE.PACKAGE' == dataItem.goodsTypeCode) {
                popupOption = {
                    id: 'packageGoodsStockMgm',
                    title: '묶음상품 재고 상세 정보',
                    param: { "goodsId": dataItem.goodsId },
                    src: '#/packageGoodsStockMgm',
                    width: '1100px',
                    height: '800px',
                    success: function(id, data) {
                    }
                }
            } else {
                popupOption = {
                    id: 'goodsStockMgm',
                    title: '재고 상세 정보',
                    param: { "ilItemCd":dataItem.itemCd, "itemBarcode":dataItem.itemBarcode, "itemNm":dataItem.itemNm,
                        "preOrderTpNm":dataItem.preOrderTpNm, "ilItemStockId":dataItem.ilItemStockId,
                        "stockScheduledCount":dataItem.stockScheduledCount,"ilItemWarehouseId":dataItem.ilItemWarehouseId,"baseDt":nowDate
                    },
                    src: '#/goodsStockMgm',
                    width: '1100px',
                    height: '800px',
                    success: function(id, data) {
                        //fnSearch();
                    }
                }
            }
            fnKendoPopup(popupOption)

		});

		$("#goodsGrid").on("click", "a[kind=itemDetlInfo]", function (e) {
        	e.preventDefault();

			var aMap = goodsGrid.dataItem($(e.currentTarget).closest("tr"));

			var option = new Object();
    	    option.url = "#/itemMgmModify";  // 마스터 품목 수정 화면 URL
    	    option.target = "itemMgmModify";
    	    option.data = {                  // 마스터 품목 수정 화면으로 전달할 Data
    	            ilItemCode : aMap.itemCode,
    	            isErpItemLink : aMap.erpIfYn,
    	            masterItemType : aMap.itemTp
    	    };

    	    fnGoNewPage(option);
		});

        $('#goodsGrid').on("click", "a[kind=goodsRegistApprovalInfo]", function(e) {
        	e.preventDefault();
			var dataItem = goodsGrid.dataItem($(e.currentTarget).closest("tr"));

    	    var option = new Object();
    	    option.url = "";
    	    option.target = "_blank";

			if (dataItem.goodsRegistApprReqUserId == PG_SESSION.userId) // 상품등록 승인 요청자의 경우
				option.url = "#/approvalGoodsRegistRequest"; // 상품등록 승인요청 URL
			else
				option.url = "#/approvalGoodsRegistAccept"; // 상품등록 승인관리 URL

    	    option.data = {                  // 승인 화면으로 전달할 Data
	    		ilGoodsId : dataItem.goodsId,
    	    };

    	    if (option.url != "")
    	    	fnGoNewPage(option);
        });

        $('#goodsGrid').on("click", "a[kind=itemClientApprovalInfo]", function(e) {
        	e.preventDefault();
			var dataItem = goodsGrid.dataItem($(e.currentTarget).closest("tr"));

    	    var option = new Object();
    	    option.url = "";
    	    option.target = "_blank";

			if (PG_SESSION.companyType == "COMPANY_TYPE.HEADQUARTERS")
				option.url = "#/approvalItemClientAccept"; // 거래처 품목 승인관리 URL
			else
				option.url = "#/approvalItemClientRequest"; // 거래처 품목 승인요청 URL

			option.data = {                  // 승인 화면으로 전달할 Data
				ilItemCode : dataItem.itemCode,
    	    };

    	    if (option.url != "")
    	    	fnGoNewPage(option);
        });

        $('#goodsGrid').on("click", "a[kind=goodsClientApprovalInfo]", function(e) {
        	e.preventDefault();
			var dataItem = goodsGrid.dataItem($(e.currentTarget).closest("tr"));

    	    var option = new Object();
    	    option.url = "";
    	    option.target = "_blank";

			if (PG_SESSION.companyType == "COMPANY_TYPE.HEADQUARTERS")
				option.url = "#/approvalGoodsClientAccept"; // 거래처 상품 승인관리 URL
			else
				option.url = "#/approvalGoodsClientRequest"; // 거래처 상품 승인요청 URL

    	    option.data = {                  // 승인 화면으로 전달할 Data
	    		ilGoodsId : dataItem.goodsId,
    	    };

    	    if (option.url != "")
    	    	fnGoNewPage(option);
        });

	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){

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
           }
		});

        //상품 코드 검색
        fnKendoDropDownList({
       		id: "searchCondition",
            data: [
                { "CODE" : "GOODS_CODE", "NAME" : "상품코드" }
                , { "CODE" : "ITEM_CODE", "NAME" : "품목코드" }
                , { "CODE" : "ITEM_BARCODE", "NAME" : "품목 바코드" }
            ],
            valueField: "CODE",
            textField: "NAME",
        });

        // 업체기준
        fnTagMkRadio({
            id : "companyStandardType",
            tagId : "companyStandardType",
            data  : [ { "CODE" : "SUPPLIER"  , "NAME" : "공급업체 기준" },
                      { "CODE" : "WAREHOUSE"  , "NAME" : "출고처 그룹 기준(자사)" }
                    ],
            style : {}
        });

        $("input[name=companyStandardType]").attr("data-bind", "checked: searchInfo.companyStandardType, events: {change: fnCompanyStandardTypeChange}");

        // 공급업체
        fnKendoDropDownList({
            id : "supplierId",
            tagId : "supplierId",
//            url : "/admin/comn/getDropDownSupplierList",
            url : "/admin/comn/getDropDownAuthSupplierList",
            textField :"supplierName",
            valueField : "supplierId",
            blank : "공급업체 전체",
            async : false
        });

        // 공급업체 별 출고처
        fnKendoDropDownList({
            id : "supplierByWarehouseId",
            tagId : "supplierByWarehouseId",
            url : "/admin/comn/getDropDownSupplierByWarehouseList",
            textField :"warehouseName",
            valueField : "warehouseId",
            blank : "출고처 전체",
            async : false,
            cscdId     : "supplierId",
            cscdField  : "supplierId"
        });

        // 출고처그룹
        fnKendoDropDownList({
            id : "warehouseGroup",
            tagId : "warehouseGroup",
            url : "/admin/comn/getCodeList",
            params : { "stCommonCodeMasterCode" : "WAREHOUSE_GROUP", "useYn" : "Y" },
            blank : "출고처 그룹 전체",
            async : false
        });

        // 출고처그룹 별 출고처
        fnKendoDropDownList({
            id : "warehouseId",
            tagId : "warehouseId",
//            url : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
            url : "/admin/comn/getDropDownAuthWarehouseGroupByWarehouseList",
            textField :"warehouseName",
            valueField : "warehouseId",
            blank : "출고처 선택",
            async : false,
            params : { "warehouseGroupCode" : "" },
//            autoBind : true,
//            cscdId     : "warehouseGroup",
//            cscdField  : "warehouseGroupCode"
        });

        // 브랜드
        fnKendoDropDownList({
            id : "brandId",
            tagId : "brandId",
            url : "/admin/ur/brand/searchBrandList",
            params : {"searchUseYn": "Y"},
            textField :"brandName",
            valueField : "urBrandId",
            blank : "전체",
            async : false
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
        });

        // 카테고리 구분
        fnKendoDropDownList({
            id : "categoryType",
            tagId : "categoryType",
            data : [ {"CODE" : "CATEGORY_STANDARD", "NAME" : "표준카테고리"},
                     {"CODE" : "CATEGORY_PULMUONE", "NAME" : "전시카테고리"},
                     {"CODE" : "CATEGORY_ORGA", "NAME" : "몰인몰(올가)"},
                     {"CODE" : "CATEGORY_BABYMEAL", "NAME" : "몰인몰(베이비밀)"},
                     {"CODE" : "CATEGORY_EATSLIM", "NAME" : "몰인몰(잇슬림)"}
                   ]
        });

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

        // 전시카테고리 대분류
        fnKendoDropDownList({
            id : "categoryDepth1",
            tagId : "categoryDepth1",
            url : "/admin/comn/getDropDownCategoryList",
            params : { "depth" : "1", "mallDiv" : "MALL_DIV.PULMUONE" },
            textField : "categoryName",
            valueField : "categoryId",
            blank : "대분류",
            async : false
        });

        // 전시카테고리 중분류
        fnKendoDropDownList({
            id : "categoryDepth2",
            tagId : "categoryDepth2",
            url : "/admin/comn/getDropDownCategoryList",
            textField : "categoryName",
            valueField : "categoryId",
            blank : "중분류",
            async : false,
            cscdId : "categoryDepth1",
            cscdField : "categoryId"
        });

        // 전시카테고리 소분류
        fnKendoDropDownList({
            id : "categoryDepth3",
            tagId : "categoryDepth3",
            url : "/admin/comn/getDropDownCategoryList",
            textField : "categoryName",
            valueField : "categoryId",
            blank : "소분류",
            async : false,
            cscdId : "categoryDepth2",
            cscdField : "categoryId"
        });

        // 전시카테고리 세분류
        fnKendoDropDownList({
            id : "categoryDepth4",
            tagId : "categoryDepth4",
            url : "/admin/comn/getDropDownCategoryList",
            textField : "categoryName",
            valueField : "categoryId",
            blank : "세분류",
            async : false,
            cscdId : "categoryDepth3",
            cscdField : "categoryId"
        });

        // 검색기간
        fnKendoDropDownList({
            id : "dateSearchType",
            tagId : "dateSearchType",
            data : [ { "CODE" : "CREATE_DATE", "NAME" : "등록일" },
                     { "CODE" : "MODIFY_DATE", "NAME" : "최근수정일" },
                     { "CODE" : "SALE_DATE", "NAME" : "판매기간" }
                   ]
        });

        // 등록(가입)일 시작
        fnKendoDatePicker({
            id: "dateSearchStart",
            format: "yyyy-MM-dd",
            btnStartId: "dateSearchStart",
            btnEndId: "dateSearchEnd",
            defVal : fnGetDayMinus(fnGetToday(),6),
			defType : 'oneWeek',
            change : function(e) {
                fnStartCalChange("dateSearchStart", "dateSearchEnd");
            }
        });

        // 등록(가입)일 종료
        fnKendoDatePicker({
            id: "dateSearchEnd",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "dateSearchStart",
            btnEndId: "dateSearchEnd",
            defVal : fnGetToday(),
			defType : 'oneWeek',
            change : function(e) {
                fnEndCalChange("dateSearchStart", "dateSearchEnd");
            }
        });

        // 상품유형
        fnTagMkChkBox({
            id : "goodsType",
            tagId : "goodsType",
            url : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "GOODS_TYPE", "useYn" : "Y"},
            beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }],
            async : false
        });

		if (PG_SESSION != undefined && PG_SESSION != null
				&& PG_SESSION.companyType != undefined && PG_SESSION.companyType != null && PG_SESSION.companyType == 'COMPANY_TYPE.CLIENT') {
	    	$('input:checkbox[name="goodsType"]:input[value="GOODS_TYPE.PACKAGE"]').parent().remove(); // 거래처 회원에게는 묶음상품 미노출
		}

		$("input[name=goodsType]").attr("data-bind", "checked: searchInfo.goodsType, events: {change: fnCheckboxChange}");

        // 판매유형
        fnTagMkChkBox({
            id : "saleType",
            tagId : "saleType",
            url : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "SALE_TYPE", "useYn" : "Y"},
            beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }],
            async : false
        });

        $("input[name=saleType]").attr("data-bind", "checked: searchInfo.saleType, events: {change: fnCheckboxChange}");

        // 배송유형
        fnTagMkChkBox({
            id : "goodsDeliveryType",
            tagId : "goodsDeliveryType",
            url : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "GOODS_DELIVERY_TYPE", "useYn" : "Y"},
            beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }],
            async : false
        });

        $("input[name=goodsDeliveryType]").attr("data-bind", "checked: searchInfo.goodsDeliveryType, events: {change: fnCheckboxChange}");

        // 판매상태
        fnTagMkChkBox({
            id : "saleStatus",
            tagId : "saleStatus",
            url : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "SALE_STATUS", "useYn" : "Y"},
            beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }],
            async : false
        });

		if (PG_SESSION != undefined && PG_SESSION != null
				&& PG_SESSION.companyType != undefined && PG_SESSION.companyType != null && PG_SESSION.companyType == 'COMPANY_TYPE.CLIENT') {
	    	$('input:checkbox[name="saleStatus"]:input[value="SALE_STATUS.SAVE"]').parent().remove(); // 거래처 회원에게는 저장상태 미노출
		}

		$("input[name=saleStatus]").attr("data-bind", "checked: searchInfo.saleStatus, events: {change: fnCheckboxChange}");

        // 전시여부
        fnTagMkRadio({
            id : "displayYn",
            tagId : "displayYn",
            data  : [ { "CODE" : ""   , "NAME" : "전체" },
                      { "CODE" : "Y"  , "NAME" : "전시" },
                      { "CODE" : "N"  , "NAME" : "미전시" }
                    ]
        });

        $("input[name=displayYn]").attr("data-bind", "checked: searchInfo.displayYn");

        // ERP 연동여부
        fnTagMkRadio({
            id : "erpIfYn",
            tagId : "erpIfYn",
            data  : [ { "CODE" : ""   , "NAME" : "전체" },
                      { "CODE" : "Y"  , "NAME" : "예" },
                      { "CODE" : "N"  , "NAME" : "아니오" }
                    ]
        });

        $("input[name=erpIfYn]").attr("data-bind", "checked: searchInfo.erpIfYn");

        // 구매허용범위 조회조건
        fnTagMkRadio({
            id : "purchaseTargetAllYn",
            tagId : "purchaseTargetAllYn",
            data  : [ { "CODE" : "Y"   , "NAME" : "전체" },
                      { "CODE" : "N"  , "NAME" : "조건검색" }
                    ]
        });

        $("input[name=purchaseTargetAllYn]").attr("data-bind", "checked: searchInfo.purchaseTargetAllYn, events: {change: fnPurchaseTargetAllYnChange}");

        // 구매허용범위 : 일반회원
        fnTagMkChkBox({
            id : "purchaseMemberYn",
            tagId : "purchaseMemberYn",
            data : [ { "CODE" : "Y", "NAME" : "일반회원" } ]
        });

        $("input[name=purchaseMemberYn]").attr("data-bind", "checked: searchInfo.purchaseMemberYn");

        // 구매허용범위 : 임직원회원
        fnTagMkChkBox({
            id : "purchaseEmployeeYn",
            tagId : "purchaseEmployeeYn",
            data : [ { "CODE" : "Y", "NAME" : "임직원회원" } ]
        });

        $("input[name=purchaseEmployeeYn]").attr("data-bind", "checked: searchInfo.purchaseEmployeeYn");

        // 구매허용범위 : 비회원
        fnTagMkChkBox({
            id : "purchaseNonmemberYn",
            tagId : "purchaseNonmemberYn",
            data : [ { "CODE" : "Y", "NAME" : "비회원" } ]
        });

        $("input[name=purchaseNonmemberYn]").attr("data-bind", "checked: searchInfo.purchaseNonmemberYn");

        // 판매허용범위 조회조건
        fnTagMkRadio({
            id : "salesAllowanceAllYn",
            tagId : "salesAllowanceAllYn",
            data  : [ { "CODE" : "Y"   , "NAME" : "전체" },
                      { "CODE" : "N"  , "NAME" : "조건검색" }
                    ]
        });

        $("input[name=salesAllowanceAllYn]").attr("data-bind", "checked: searchInfo.salesAllowanceAllYn, events: {change: fnSalesAllowanceAllYnChange}");

        // 판매허용범위 : PC Web
        fnTagMkChkBox({
            id : "displayWebPcYn",
            tagId : "displayWebPcYn",
            data : [ { "CODE" : "Y", "NAME" : "PC Web" } ]
        });

        $("input[name=displayWebPcYn]").attr("data-bind", "checked: searchInfo.displayWebPcYn");

        // 판매허용범위 : M Web
        fnTagMkChkBox({
            id : "displayWebMobileYn",
            tagId : "displayWebMobileYn",
            data : [ { "CODE" : "Y", "NAME" : "M Web" } ]
        });

        $("input[name=displayWebMobileYn]").attr("data-bind", "checked: searchInfo.displayWebMobileYn");

        // 판매허용범위 : App
        fnTagMkChkBox({
            id : "displayAppYn",
            tagId : "displayAppYn",
            data : [ { "CODE" : "Y", "NAME" : "App" } ]
        });

        $("input[name=displayAppYn]").attr("data-bind", "checked: searchInfo.displayAppYn");

        // 보관방법
        fnTagMkChkBox({
            id : "storageMethodType",
            tagId : "storageMethodType",
            url : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "ERP_STORAGE_TYPE", "useYn" : "Y"},
            beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }],
            async : false
        });

        $("input[name=storageMethodType]").attr("data-bind", "checked: searchInfo.storageMethodType, events: {change: fnCheckboxChange}");

		// 외부몰 판매상태
		fnTagMkChkBox({
			id : "goodsOutMallSaleStat",
			tagId : "goodsOutMallSaleStat",
			url : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "GOODS_OUTMALL_SALE_STAT", "useYn" : "Y"},
			beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }],
			async : false
		});
		$("input[name=goodsOutMallSaleStat]").attr("data-bind", "checked: searchInfo.goodsOutMallSaleStat, events: {change: fnCheckboxChange}");

        // 쿠폰허용여부
        fnKendoDropDownList({
            id : "couponUseYn",
            tagId : "couponUseYn",
            data : [ { "CODE" : "", "NAME" : "쿠폰허용 여부" },
                     { "CODE" : "Y", "NAME" : "예" },
                     { "CODE" : "N", "NAME" : "아니오" }
                   ]
        });

        // 추가상품유무
        fnKendoDropDownList({
            id : "additionalGoodsYn",
            tagId : "additionalGoodsYn",
            data : [ { "CODE" : "", "NAME" : "추가상품 유무" },
                     { "CODE" : "Y", "NAME" : "예" },
                     { "CODE" : "N", "NAME" : "아니오" }
                   ]
        });

        // 재고연동 유무
        fnKendoDropDownList({
            id : "stockIfYn",
            tagId : "stockIfYn",
            data : [ { "CODE" : "", "NAME" : "재고연동 유무" },
                     { "CODE" : "Y", "NAME" : "예" },
                     { "CODE" : "N", "NAME" : "아니오" }
                   ]
        });

        // MD 추천유무
        fnKendoDropDownList({
            id : "mdRecommendYn",
            tagId : "mdRecommendYn",
            data : [ { "CODE" : "", "NAME" : "MD 추천여부" },
                     { "CODE" : "Y", "NAME" : "노출" },
                     { "CODE" : "N", "NAME" : "노출 안함" }
                   ]
        });

        // 매장전용상품유형
        fnKendoDropDownList({
            id : "erpProductType",
            tagId : "erpProductType",
            data : [ { "CODE" : "", "NAME" : "매장전용 상품유형" },
                     { "CODE" : "일반", "NAME" : "일반" },
                     { "CODE" : "사간거래", "NAME" : "사간거래" },
                     { "CODE" : "손질", "NAME" : "손질" },
                     { "CODE" : "제조", "NAME" : "제조" }
                   ]
        });

        // 할인적용 유무
        fnKendoDropDownList({
            id : "discountType",
            tagId : "discountType",
            data : [
            	{ "CODE" : "GOODS_DISCOUNT_TP.PRIORITY", "NAME" : "우선할인" },
            	{ "CODE" : "GOODS_DISCOUNT_TP.ERP_EVENT", "NAME" : "올가행사" },
            	{ "CODE" : "GOODS_DISCOUNT_TP.IMMEDIATE", "NAME" : "즉시할인" },
            	{ "CODE" : "GOODS_DISCOUNT_TP.PACKAGE", "NAME" : "기본할인(묶음상품)" },
            	{ "CODE" : "GOODS_DISCOUNT_TP.NONE", "NAME" : "할인정보 없음" },
            	{ "CODE" : "GOODS_DISCOUNT_TP.NOT_APPLICABLE", "NAME" : "적용불가" },

              ],

            blank : "할인적용 유무",
            async : false
        });

        // 상품정렬 방법
        fnKendoDropDownList({
            id : "gridDataSort",
            tagId : "gridDataSort",
            data : [ { "CODE" : "CREATE_DATE", "NAME" : "상품등록일 순" },
                     { "CODE" : "MODIFY_DATE", "NAME" : "상품수정일 순" }
                   ]
        });

		// 엑셀 양식 유형 - 엑셀다운로드 양식을 위한 공통
        fnKendoDropDownList({
        	id : "psExcelTemplateId",
        	url : "/admin/policy/excel/getPolicyExcelTmpltList",
        	tagId : "psExcelTemplateId",
        	params : { "excelTemplateTp" : "EXCEL_TEMPLATE_TP.GOODS", "excelTemplateUseTp" : "DOWNLOAD"},
        	textField : "templateNm",
            valueField : "psExcelTemplateId",
        	blank : "선택",
        	async : false
        });

        // 그리드 판매상태
        fnKendoDropDownList({
            id : "gridSaleStatus",
            tagId : "gridSaleStatus",
            data : [
            	{ "CODE" : "SALE_STATUS.WAIT", "NAME" : "판매대기" },
            	{ "CODE" : "SALE_STATUS.ON_SALE", "NAME" : "판매중" },
                { "CODE" : "SALE_STATUS.STOP_SALE", "NAME" : "판매중지" },
            	{ "CODE" : "SALE_STATUS.OUT_OF_STOCK_BY_MANAGER", "NAME" : "품절(관리자)" }
              ],

            blank : "판매상태",
            async : false
        });

        fnTagMkChkBox({
            id : "stockStatus",
            tagId : "stockStatus",
            data : [
            		{ "CODE" : "ALL", "NAME" : "전체" }
            		, { "CODE" : "STK_IF.Y.PRE_ORD.Y.UNLIMITED", "NAME" : "선주문" }
            		, { "CODE" : "STK_IF.Y.PRE_ORD.Y.LIMITED", "NAME" : "선주문(한정재고)" }
            		, { "CODE" : "STK_IF.Y.PRE_ORD.N", "NAME" : "한정재고" }
            		, { "CODE" : "STK_IF.N.LIMITED", "NAME" : "재고미연동(한정재고)" }
            		, { "CODE" : "STK_IF.N.UNLIMITED", "NAME" : "재고미연동(무제한)" }
            	]
        });


        // 할인적용 유무
        fnKendoDropDownList({
            id : "saleDateType",
            tagId : "saleDateType",
            data : [
            	{ "CODE" : "", "NAME" : "판매기간 구분" },
            	{ "CODE" : "WAIT", "NAME" : "판매중 대기목록" },
                { "CODE" : "END", "NAME" : "판매기간 만료" }

              ]
        });

		$("input[name=stockStatus]").attr("data-bind", "checked: searchInfo.stockStatus, events: {change: fnCheckboxChange}");

        fnKendoDropDownList({
			id : "presentYn",
			tagId : "presentYn",
			data : [
				{ "CODE" : "", "NAME" : "선물하기 허용여부" },
				{ "CODE" : "Y", "NAME" : "허용" },
				{ "CODE" : "N", "NAME" : "불가" },
				{ "CODE" : "NA", "NAME" : "미대상" }
			]
		});



        // 검색기간 초기화 버튼 클릭
        /*
        $('[data-id="fnDateBtnC"]').on("click", function(){
            $('[data-id="fnDateBtn3"]').mousedown();
        });
        */

//        fnInputValidationForAlphabetNumberLineBreakComma("findKeyword"); // 검색어
//        fnInputValidationForHangulAlphabetNumber("goodsName"); // 상품명
	};

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

    // viewModel 초기화
    function fnViewModelInit(){
        viewModel = new kendo.data.ObservableObject({
            searchInfo : { // 조회조건
                searchCondition : "", // 검색조건
                findKeyword : "", // 검색어 (숫자,영문대소문자)
                findKeywordOnMulti : "", // 검색어 (숫자,영문대소문자)
                goodsName : "", // 상품명 (한글,영문대소문자, 숫자)
                companyStandardType : "", // 업체 기준 (공급처 or 출고처)
                supplierId : "", // 공급업체
                supplierByWarehouseId : "", // 공급업체 기준 출고처
                warehouseGroup : "", // 출고처 그룹
                warehouseId : "", // 출고처 그룹 기준 출고처
                brandId : "", // 브랜드
                dpBrandId : "",
                categoryType : "", // 카테고리 구분
                categoryStandardDepth1 : "", // 표준카테고리 대분류
                categoryStandardDepth2 : "", // 표준카테고리 중분류
                categoryStandardDepth3 : "", // 표준카테고리 소분류
                categoryStandardDepth4 : "", // 표준카테고리 세분류
                categoryDepth1 : "", // 전시카테고리 대분류
                categoryDepth2 : "", // 전시카테고리 중분류
                categoryDepth3 : "", // 전시카테고리 소분류
                categoryDepth4 : "", // 전시카테고리 세분류
                dateSearchType : "", // 기간검색유형
                dateSearchStart : "", // 기간검색 시작일자
                dateSearchEnd : "", // 기간검색 종료일자
                goodsType : [], // 상품유형 목록
                saleType : [], // 판매유형
                goodsDeliveryType : [], // 배송유형
                saleStatus : [], // 판매상태
                displayYn : "", // 전시여부
                erpIfYn : "", // ERP 연동여부
                purchaseTargetAllYn : "", // 구매허용범위 전체조회 유무
                purchaseMemberYn : "", // 구매허용범위 일반회원
                purchaseEmployeeYn : "", // 구매허용범위 임직원회원
                purchaseNonmemberYn : "", // 구매허용범위 비회원
                salesAllowanceAllYn : "", // 판매허용범위 전체조회 유무
                displayWebPcYn : "", // 판매허용범위 WEB PC
                displayWebMobileYn : "", // 판매허용범위 WEB MOBILE
                displayAppYn : "", // 판매허용범위 APP
                storageMethodType : [], // 보관방법
                goodsOutMallSaleStat : [], // 외부몰 판먜상태
                couponUseYn : "", // 쿠폰허용여부
                additionalGoodsYn : "", // 추가상품유무
                stockIfYn : "", // 재고연동 유무
                mdRecommendYn : "", // md추천유무
                erpProductType : "", // 매장전용상품유형
                discountType : "", // 할인적용 유무
                gridDataSort : "", // 상품목록 정렬방법
                stockStatus : [], // 재고운영형태
                saleDateType : "", // 판매기간구분
                presentYn : "" // 선물하기 유형
            },
            publicStorageUrl : null, // 저장소 URL
            findKeywordDisabled : false, // 검색어 Disabled
            supplierStandardVisible : true, // 공급처기준 Visible
            warehouseStandardVisible : false, // 출고처기준 Visible
            categoryStandardVisible : true, // 표준카테고리 Visible
            categoryVisible : false, // 전시카테고리 Visible
            purchaseTargetTypeVisible : false, // 구매허용범위 Visible
            salesAllowanceVisible : false, // 판매허용범위 Visible
            gridSaleStatus : "", // 그리드 판매상태
            gridSelectionForm : "", // 그리드 선택양식
            fnSearch : function(e){ // 조회
                e.preventDefault();

                if($("input[name=selectConditionType]:checked").val() == "multiSection") {
                	viewModel.searchInfo.set("findKeyword", "");
        		}
                else {
                	viewModel.searchInfo.set("findKeywordOnMulti", "");
                }

                let data = $("#searchForm").formSerialize(true);

                var itemNameLengthChk = false;


                if($("input[name=selectConditionType]:checked").val() == "singleSection") {
                   	if($.trim($('#findKeyword').val()).length >= 0 && $.trim($('#findKeyword').val()).length < 2 ) {
                		itemNameLengthChk = true;
                			fnKendoMessage({
                				message : '단일조건 코드 검색은 2글자 이상 입력해야 합니다.',
                				ok : function() {
                					return;
                				}
                			});
                   	} else {
                              data['findKeyword'] = $.trim($('#findKeyword').val());
                   	}
                }else {
                	if( data.findKeyword == "" ){
                		if( data.dateSearchStart == "" && data.dateSearchEnd != "" ){
                			fnKendoMessage({ message : "시작일을 선택해주세요." });
                			return;
                		}

                		if( data.dateSearchStart != "" && data.dateSearchEnd == "" ){
                			fnKendoMessage({ message : "종료일을 선택해주세요." });
                				return;
                		}

                		if($.trim($('#findKeywordOnMulti').val()).length >= 0) {
                            data['findKeywordOnMulti'] = $.trim($('#findKeywordOnMulti').val());
                		}

                	}
                }

                if(!itemNameLengthChk) {
                	const _pageSize = goodsGrid && goodsGrid.dataSource ? goodsGrid.dataSource.pageSize() : PAGE_SIZE;


                    let searchData = fnSearchData(data);
                    let query = { page : 1,
                                  pageSize : _pageSize,
                                  filterLength : searchData.length,
                                  filter : { filters : searchData }
                    };

                    goodsGridDs.query(query);
                    initSearch = false;
                }
            },
            fnClear : function(e){ // 초기화

                e.preventDefault();
                fnDefaultSet();
            },
            fnSearchConditionChange : function(){ // 검색유형 변경
                /*
                if( viewModel.searchInfo.get("searchCondition") == "ALL" ){
                    viewModel.set("findKeywordDisabled", true);
                }else{
                    viewModel.set("findKeywordDisabled", false);
                }

                viewModel.searchInfo.set("findKeyword", "");
                */
            },
            fnCompanyStandardTypeChange : function(){ // 업체기준 변경

                if( viewModel.searchInfo.companyStandardType == "SUPPLIER" ){

                    viewModel.set("supplierStandardVisible", true);
                    viewModel.set("warehouseStandardVisible", false);
                }else{

                    viewModel.set("supplierStandardVisible", false);
                    viewModel.set("warehouseStandardVisible", true);
                }

                viewModel.searchInfo.set("supplierId", "");
                viewModel.searchInfo.set("supplierByWarehouseId", "");
                viewModel.searchInfo.set("warehouseGroup", "");
                viewModel.searchInfo.set("warehouseId", "");
            },
            fnWareHouseGroupChange : function() {
            	fnAjax({
//                    url     : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
                    url : "/admin/comn/getDropDownAuthWarehouseGroupByWarehouseList",
                    method : "GET",
                    params : { "warehouseGroupCode" : viewModel.searchInfo.get("warehouseGroup") },

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
            fnCategoryTypeChange : function(){ // 카테고리 타입 변경

                let categoryType = viewModel.searchInfo.get("categoryType");

                if( categoryType == "CATEGORY_STANDARD" ){
                    viewModel.set("categoryStandardVisible", true);
                    viewModel.set("categoryVisible", false);
                }else{
                    viewModel.set("categoryStandardVisible", false);
                    viewModel.set("categoryVisible", true);

                    let mallDiv = "";

                    switch(categoryType){
                        case "CATEGORY_PULMUONE" : // 전시카테고리
                            mallDiv = "MALL_DIV.PULMUONE";
                            break;
                        case "CATEGORY_ORGA" : // 몰인몰(올가)
                            mallDiv = "MALL_DIV.ORGA";
                            break;
                        case "CATEGORY_BABYMEAL" : // 몰인몰(베이비밀)
                            mallDiv = "MALL_DIV.BABYMEAL";
                            break;
                        case "CATEGORY_EATSLIM" : // 몰인몰(잇슬림)
                            mallDiv = "MALL_DIV.EATSLIM";
                            break;
                    };

                    viewModel.fnGetDropDownCategoryList(mallDiv);
                }

                viewModel.searchInfo.set("categoryStandardDepth1", "");
                viewModel.searchInfo.set("categoryStandardDepth2", "");
                viewModel.searchInfo.set("categoryStandardDepth3", "");
                viewModel.searchInfo.set("categoryStandardDepth4", "");
                viewModel.searchInfo.set("categoryDepth1", "");
                viewModel.searchInfo.set("categoryDepth2", "");
                viewModel.searchInfo.set("categoryDepth3", "");
                viewModel.searchInfo.set("categoryDepth4", "");
            },
            fnGetDropDownCategoryList : function(mallDiv){ // 전시카테고리 정보 조회

                fnAjax({
                    url     : "/admin/comn/getDropDownCategoryList",
                    method : "GET",
                    params  : {"depth" : "1", "mallDiv" : mallDiv},
                    success : function( data ){
                        let categoryDepth1 = $("#categoryDepth1").data("kendoDropDownList");
                        categoryDepth1.setDataSource(data.rows);
                    },
                    error : function(xhr, status, strError){
                        fnKendoMessage({ message : xhr.responseText });
                    },
                    isAction : "select"
                });
            },
            fnPurchaseTargetAllYnChange : function(){ // 구매허용범위 전체유무 변경

                if( viewModel.searchInfo.get("purchaseTargetAllYn") == "Y" ){
                    viewModel.set("purchaseTargetTypeVisible", false);
                }else{
                    viewModel.set("purchaseTargetTypeVisible", true);
                }
            },
            fnSalesAllowanceAllYnChange : function(){ // 판매허용범위 전체유무 변경

                if( viewModel.searchInfo.get("salesAllowanceAllYn") == "Y" ){
                    viewModel.set("salesAllowanceVisible", false);
                }else{
                    viewModel.set("salesAllowanceVisible", true);
                }
            },
            fnCheckboxChange : function(e){ // 체크박스 변경
                e.preventDefault();

                if( e.target.value == "ALL" ){
                    if( $("input[name=" + e.target.name + "]:eq(0)").is(":checked") ){

                        $("input[name=" + e.target.name + "]:gt(0)").each(function(idx){
                            if( viewModel.searchInfo.get(e.target.name).indexOf($(this).val()) < 0 ){
                                viewModel.searchInfo.get(e.target.name).push($(this).val());
                            }
                        });
                    }else{

                        $("input[name=" + e.target.name + "]:gt(0)").each(function(idx){
                            viewModel.searchInfo.get(e.target.name).remove($(this).val());
                        });
                    }
                }else{

                    if( !$("#" + e.target.id).is(":checked") && $("input[name=" + e.target.name + "]:eq(0)").is(":checked") ){
                        viewModel.searchInfo.get(e.target.name).remove($("input[name=" + e.target.name + "]:eq(0)").val());
                    }
                    else if( $("#" + e.target.id).is(":checked")
                                && ($("input[name=" + e.target.name + "]").length - 1) == viewModel.searchInfo.get(e.target.name).length )
                    {
                        viewModel.searchInfo.get(e.target.name).push($("input[name=" + e.target.name + "]:eq(0)").val());
                    }
                }
            },
            fnPackageGoodsCreate : function(e){ // 묶음상품생성
            	e.preventDefault();

            	if (!fnIsProgramAuth("CREATE_PACKAGE")) {
                    fnKendoMessage({ message : "권한이 없습니다." });
            		return;
            	}

            	let option = {};
                option.url = "#/goodsPackage";
                option.menuId = 768;
                option.target = "goodsPackage";

                fnGoNewPage(option);
            },
            fnExcelDownload : function(e){ // 엑셀다운로드
                e.preventDefault();

            	if (!fnIsProgramAuth("EXCELDOWN")) {
                    fnKendoMessage({ message : "권한이 없습니다." });
            		return;
            	}

                var data = $('#searchForm').formSerialize(true);

                //엑셀다운로드 양식을 위한 공통
        		if( data["psExcelTemplateId"].length < 1 || data["psExcelTemplateId"] == "[]"){
            		fnKendoMessage({ message : "다운로드 양식을 선택해주세요."});
            		return false;
            	}

        		fnExcelDownload('/admin/goods/list/goodsExportExcel', data);
            },
            fnSelectModification : function(e){ // 선택수정
            	e.preventDefault();

            	if (!fnIsProgramAuth("EDIT_SELECTED")) {
                    fnKendoMessage({ message : "권한이 없습니다." });
            		return;
            	}

            	let selectRows  = $("#goodsGrid").find("input[name=rowCheckbox]:checked").closest("tr");
//                let confirmMessage = $("#gridSaleStatus").data("kendoDropDownList").text() + " 으로 일괄 변경하시겠습니까?";
                let confirmMessage = "승인완료된 상품 및 판매상태 수정이 가능한 상품에 한해 일괄수정 처리됩니다. 진행하시겠습니까?";
                let params = {};
                params.gridSaleStatus = viewModel.get("gridSaleStatus");
                params.goodsIdList = [];

                if( selectRows.length == 0 ){
                    fnKendoMessage({ message : "선택된 상품이 없습니다." });
                    return;
                }

                if( !params.gridSaleStatus ){
                    fnKendoMessage({ message : "선택된 판매상태가 없습니다." });
                    return;
                }

                fnKendoMessage({
                    type    : "confirm",
                    message : confirmMessage,
                    ok      : function(){

                        for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){
                            let dataItem = goodsGrid.dataItem($(selectRows[i]));
                            params.goodsIdList[i] = dataItem.goodsId;
                        }

                        fnAjax({
                            url     : "/admin/goods/list/putGoodsListSaleStatusChange",
                            params  : params,
                            contentType : "application/json",
                            success : function( data ){
                            	if (data.total > 0) {
                            		var returnMsg = '아래 상품을 제외한 상품의 판매상태가 일괄변경되었습니다.';
                            		for (var i=0; i<data.total; i++) {
                            			returnMsg += '<br>' + data.rows[i].goodsId;
                            		}
                                	fnKendoMessage({ message : returnMsg, ok : viewModel.fnGridDataSort });
                            	}
                            	else {
                                	fnKendoMessage({ message : "저장이 완료되었습니다.", ok : viewModel.fnGridDataSort });
                            	}
                            },
                            error : function(xhr, status, strError){
                                fnKendoMessage({ message : xhr.responseText });
                            },
                            isAction : "update"
                        });
                    },
                    cancel  : function(){

                    }
                });
            },
            fnViewHideAreaChange : function(e){ // 상세검색 표시제어

                // const formOffset = $("#searchForm").offset();
                const _scrollTop = $("#document").scrollTop();

                if( $("tr[name=viewHideArea]").is(":visible") ){
                    $("tr[name=viewHideArea]").hide();
                    $("#fnSearchToggle span").html("▼");
                }else{
                    $("tr[name=viewHideArea]").show();
                    $("#fnSearchToggle span").html("▲");

                    // formOffset.top < 0 ? $('#document').animate({scrollTop : formOffset.top - 30 }, 100) : "";
                    _scrollTop >= 60 ? $('#document').animate({scrollTop : 0 }, 100) : "";

            		if (PG_SESSION != undefined && PG_SESSION != null
            				&& PG_SESSION.companyType != undefined && PG_SESSION.companyType != null && PG_SESSION.companyType == 'COMPANY_TYPE.CLIENT') {
            	        $('.headquaterView').hide();
            		}
                }
            },
            fnGridDataSort : function(){ // 그리드 정렬 변경

            	$("#fnSearch").trigger("click");
                //paramGoodsId = null; //상품 저장에서 넘어온 처음만 검색되도록 paramGoodsId를 초기화 한다.
            },
            fnGoodsInfo : function(rowData, ctrlKey){ // 상품 수정

                // 상품유형에 따라 상품상세화면 이동
                let option = {};
                let goodsTypeCode = rowData.goodsTypeCode;

                option.data = { ilGoodsId : rowData.goodsId };

                switch(goodsTypeCode){
                    case "GOODS_TYPE.ADDITIONAL" : // 추가
                        option.url = "#/goodsAdditional";
                        option.menuId = 865;
                        break;
                    case "GOODS_TYPE.DAILY" : // 일일
                        option.url = "#/goodsDaily";
                        option.menuId = 1;
                        break;
                    case "GOODS_TYPE.DISPOSAL" : // 폐기임박
                        option.url = "#/goodsDisposal";
                        option.menuId = 921;
                        break;
                    case "GOODS_TYPE.GIFT" : // 증정
                        option.url = "#/goodsAdditional";
                        option.menuId = 865;
                        break;
                    case "GOODS_TYPE.GIFT_FOOD_MARKETING" : // 식품마케팅증정
                        option.url = "#/goodsAdditional";
                        option.menuId = 865;
                        break;
                    case "GOODS_TYPE.INCORPOREITY" : // 무형
                        option.url = "#/goodsIncorporeal";
                        break;
                    case "GOODS_TYPE.NORMAL" : // 일반
                        option.url = "#/goodsMgm";
                        option.menuId = 98;
                        break;
                    case "GOODS_TYPE.PACKAGE" : // 묶음
                        option.url = "#/goodsPackage";
                        option.menuId = 768;
                        break;
                    case "GOODS_TYPE.RENTAL" : // 렌탈
                        option.url = "#/goodsRental";
                        option.menuId = 1286;
                        break;
                    case "GOODS_TYPE.SHOP_ONLY" : // 매장전용
                        option.url = "#/goodsShopOnly";
                        option.menuId = 1176;
                        break;
                };

/*
                if(ctrlKey){
                	option.target = '_blank';
                	fnGoNewPage(option);
                } else {
                	fnGoPage(option);
                }
*/
            	option.target = '_blank';
            	fnGoNewPage(option);

            }

        });

        viewModel.publicStorageUrl = fnGetPublicStorageUrl();

        kendo.bind($("#searchForm"), viewModel);
    };

    // 기본값 설정
    function fnDefaultSet(){

        // 데이터 초기화
        viewModel.searchInfo.set("searchCondition", "ALL");
        viewModel.searchInfo.set("findKeyword", "");
        viewModel.searchInfo.set("findKeywordOnMulti", "");
        viewModel.searchInfo.set("goodsName", "");
        viewModel.searchInfo.set("companyStandardType", "SUPPLIER");
        viewModel.searchInfo.set("supplierId", "");
        viewModel.searchInfo.set("supplierByWarehouseId", "");
        viewModel.searchInfo.set("warehouseGroup", "");
        viewModel.searchInfo.set("warehouseId", "");
        viewModel.searchInfo.set("brandId", "");
        viewModel.searchInfo.set("dpBrandId", "");

        viewModel.searchInfo.set("categoryType", "CATEGORY_STANDARD");
        viewModel.searchInfo.set("dateSearchType", "CREATE_DATE");
        viewModel.searchInfo.set("displayYn", "");
        viewModel.searchInfo.set("erpIfYn", "");
        viewModel.searchInfo.set("couponUseYn", "");
        viewModel.searchInfo.set("additionalGoodsYn", "");
        viewModel.searchInfo.set("stockIfYn", "");
        viewModel.searchInfo.set("mdRecommendYn", "");
        viewModel.searchInfo.set("erpProductType", "");
        viewModel.searchInfo.set("discountType", "");
        viewModel.searchInfo.set("gridDataSort", "CREATE_DATE");
        viewModel.searchInfo.set("goodsType", []);
        viewModel.searchInfo.set("saleType", []);
        viewModel.searchInfo.set("goodsDeliveryType", []);
        viewModel.searchInfo.set("saleStatus", []);
        viewModel.searchInfo.set("stockStatus", []);
        //viewModel.searchInfo.saleStatus.push("SALE_STATUS.ON_SALE");
        viewModel.searchInfo.set("purchaseTargetAllYn", "Y");
        viewModel.searchInfo.set("purchaseMemberYn", "Y");
        viewModel.searchInfo.set("purchaseEmployeeYn", "Y");
        viewModel.searchInfo.set("purchaseNonmemberYn", "Y");
        viewModel.searchInfo.set("salesAllowanceAllYn", "Y");
        viewModel.searchInfo.set("displayWebPcYn", "Y");
        viewModel.searchInfo.set("displayWebMobileYn", "Y");
        viewModel.searchInfo.set("displayAppYn", "Y");
        viewModel.searchInfo.set("saleDateType", "");
		viewModel.searchInfo.set("presentYn", "");


        viewModel.searchInfo.set("storageMethodType", []);
        viewModel.searchInfo.set("goodsOutMallSaleStat", []);
        viewModel.set("gridSaleStatus", "");
        viewModel.set("gridSelectionForm", "");
        $('[data-id="fnDateBtnC"]').mousedown();

        // 화면제어
        viewModel.fnSearchConditionChange();
        viewModel.fnWareHouseGroupChange();
        viewModel.fnCategoryTypeChange();
        viewModel.fnCompanyStandardTypeChange();
        viewModel.fnPurchaseTargetAllYnChange();
        viewModel.fnSalesAllowanceAllYnChange();

        if($("input[name=selectConditionType]:checked").val() == "singleSection") {
        	$("tr[name=viewHideArea]").hide();
        }else {
        	$("tr[name=viewHideArea]").show();
        	viewModel.fnViewHideAreaChange();
        }



        $("input[name=goodsType]:eq(0)").prop("checked", true).trigger("change");
        $("input[name=saleType]:eq(0)").prop("checked", true).trigger("change");
        $("input[name=goodsDeliveryType]:eq(0)").prop("checked", true).trigger("change");
        $("input[name=saleStatus]:eq(0)").prop("checked", true).trigger("change");
//        $('input[name=saleStatus][value="SALE_STATUS.STOP_PERMANENT_SALE"]').prop("checked", false).trigger("change");
        $("input[name=stockStatus]:eq(0)").prop("checked", true).trigger("change");
        $("input[name=storageMethodType]:eq(0)").prop("checked", true).trigger("change");
        $("input[name=goodsOutMallSaleStat]:eq(0)").prop("checked", true).trigger("change");
    };

    function fnClear() {

    };
	//-------------------------------  Common Function end -------------------------------
    /** Common Clear */
	$scope.fnClear = function() {
		fnClear();
	};

}); // document ready - END
