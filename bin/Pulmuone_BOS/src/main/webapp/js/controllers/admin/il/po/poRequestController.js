/**-----------------------------------------------------------------------------
 * description 		 : 행사발주관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.19     정형진			퍼블수정 및 기능개발
 * @
 * **/

'use strict';
var pageParam = fnGetPageParam(); // GET 방식으로 전달된 parameter 객체
var PAGE_SIZE = 20;
var itemGridOpt, itemGrid, itemGridDs;
var viewModel, inputViewModel;
var paramIlPoTpId;

$(document).ready(function() {

	var todayDate = new Date();
	var tomorrow = new Date(todayDate);
	tomorrow.setDate(tomorrow.getDate()+1);

	// Initialize Page Call
	fnInitialize();

	// Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', {
			flag : 'true'
		});

		fnPageInfo({
			PG_ID : 'poRequest',
			callback : fnUI
		});
	};

	function fnUI() {
		fnTranslate();			// comm.lang.js 안에 있는 공통함수 다국어

		fnInitButton();			// Initialize Button

		fnViewModelInit();

		fnInitCompont();		// 묶음 상품 컴포넌트 Initialize

		//탭 변경 이벤트
        fbTabChange();

        fnItemGrid();			// 상품 할인 일괄업로드 상세 내역 Grid
	};


	function fnInitButton() {
		$('#fnSearch , #fnClear , #EXCEL_DN, #fnNew, #fnEmployeeSearchPopup').kendoButton();
	};

	function fnInitCompont() {
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
                  const mode = e.target.value.trim();
//                  toggleElement(mode);
              }
	      });

		fnKendoDatePicker({
			id : 'searchEventStartDt',
			format : 'yyyy-MM-dd',
			change: function(e){
				viewModel.fnDateChange(e, "searchEventStartDt");
			}
		});
		$("#searchEventStartDt").data("kendoDatePicker").unbind("blur");

		fnKendoDatePicker({
			id : 'searchEventEndDt',
			format : 'yyyy-MM-dd',
			change: function(e){
				viewModel.fnDateChange(e, "searchEventEndDt");
			}
		});

		fnKendoDropDownList({
            id : "sellersGroup",
            tagId : "sellersGroup",
            url : "/admin/comn/getCodeList",
            params : { "stCommonCodeMasterCode" : "SELLERS_GROUP", "useYn" : "Y" },
            blank : "판매처 그룹 전체",
            async : false
        }).bind("change", fnSellerGroupChange);


        fnKendoDropDownList({
            id : "sellersDetail",
            tagId : "sellersDetail",
            url : "/admin/comn/getDropDownSellersGroupBySellersList",
            textField :"sellersNm",
            valueField : "omSellersId",
            blank : "판매처 전체",
            params : { "sellersGroupCd" : "" },
        });

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

        fnKendoDropDownList({
            id : "warehouseId",
            tagId : "warehouseId",
//            url : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
            url : "/admin/comn/getDropDownAuthWarehouseGroupByWarehouseList",
            textField :"warehouseName",
            valueField : "warehouseId",
            blank : "출고처 선택",
            params : {"stockOrderYn": "Y", "warehouseGroupCode" : ""},
//            async : false,
//            cscdId     : "warehouseGroup",
//            cscdField  : "warehouseGroupCode"
        });

        fnKendoDropDownList({
            id : "searchNameType",
            tagId : "searchNameType",
            data: [
            	{
            		CODE: "productNm",
            		NAME: "상품명"
            	},
            	{
            		CODE: "itemNm",
            		NAME: "마스터 품목명"
            	},
            	{
            		CODE: "goodsId",
            		NAME: "상품코드"
            	},
            	{
            		CODE: "itemCd",
            		NAME: "마스터 품목코드"
            	},
            	{
            		CODE: "itemBarcode",
            		NAME: "품목바코드"
            	}
            ],
        });

        fnKendoDatePicker({
			id : 'eventStartDt',
			format : 'yyyy-MM-dd',
			change: function(e){
				inputViewModel.fnDateChange3(e, "eventStartDt");
			}
		});
		$("#eventStartDt").data("kendoDatePicker").unbind("blur");

		fnKendoDatePicker({
			id : 'eventEndDt',
			format : 'yyyy-MM-dd',
			change: function(e){
				inputViewModel.fnDateChange2(e, "eventEndDt");
			}
		});

		fnKendoDropDownList({
            id : "inputSellersGroup",
            tagId : "inputSellersGroup",
            url : "/admin/comn/getCodeList",
            params : { "stCommonCodeMasterCode" : "SELLERS_GROUP", "useYn" : "Y" },
            blank : "판매처 그룹 전체",
            async : false
        }).bind("change", fnSellerGroupChange2);


        fnKendoDropDownList({
            id : "inputSellersDetail",
            tagId : "inputSellersDetail",
            url : "/admin/comn/getDropDownSellersGroupBySellersList",
            textField :"sellersNm",
            valueField : "omSellersId",
            blank : "판매처 전체",
            params : { "sellersGroupCd" : "" },
        });

        fnKendoDropDownList({
            id : "searchRecevingType",
            tagId : "searchRecevingType",
            data: [
            	{
            		CODE: "equal",
            		NAME: "동일"
            	},
            	{
            		CODE: "over",
            		NAME: "이상"
            	},
            	{
            		CODE: "min",
            		NAME: "이하"
            	}
            ],
        });

        fnKendoDropDownList({
            id : "searchEventType",
            tagId : "searchEventType",
            data: [
            	{
            		CODE: "equal",
            		NAME: "동일"
            	},
            	{
            		CODE: "over",
            		NAME: "이상"
            	},
            	{
            		CODE: "min",
            		NAME: "이하"
            	}
            ],
        });



        $('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});
//        fnInputValidationForAlphabetNumberLineBreakComma("goodsCodes"); // 검색어
//        fnInputValidationForHangulAlphabetNumber("goodsNm"); // 상품명

	};

	function fnSellerGroupChange() {
		fnAjax({
            url     : "/admin/comn/getDropDownSellersGroupBySellersList",
            method : "GET",
            params : { "sellersGroupCd" : viewModel.searchInfo.get("searchSellersGroup") },

            success : function( data ){
                let sellerDetail = $("#sellersDetail").data("kendoDropDownList");
                sellerDetail.setDataSource(data.rows);
            },
            error : function(xhr, status, strError){
                fnKendoMessage({ message : xhr.responseText });
            },
            isAction : "select"
        });
	};

	function fnSellerGroupChange2() {
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


	function fnViewModelInit() {
		viewModel = new kendo.data.ObservableObject({
            searchInfo : { // 조회조건
            	searchItemCd				: "",
            	searchEventStartDt 			: "",
            	searchEventEndDt 			: "",
            	searchSellersGroup 			: "",
            	searchSellersDetail			: "",
            	searchSupplierId			: "",
            	searchWarehouseId			: "",
            	searchUserId				: "",
            	searchNameType	 			: "productNm",
            	searchName					: "",
            	searchRecevingType			: "equal",
            	searchEventType				: "equal",
            	inputRecevingReqDt   		: "",
            	inputEventEndDt				: "",
            	inputSellersGroup			: "",
            	inputSellersDetail			: "",
            },
            fnDateChange : function(e, id){
    			e.preventDefault();
    			var eventStartDt = new Date(viewModel.searchInfo.get("searchEventStartDt"));
    			var eventEndDt = new Date(viewModel.searchInfo.get("searchEventEndDt"));

    			if( eventStartDt.getTime() > eventEndDt.getTime() ) {
					fnKendoMessage({ message : "행사종료일이 행사시작일 보다 빠릅니다.", ok : function() {
						$("#"+id).focus();

						viewModel.searchInfo.set(id, "");
					}});
    			}

    		}

        });

		inputViewModel = new kendo.data.ObservableObject({
            inputInfo : { // 조회조건
            	ilPoEventId					: "",
            	goodsInfo					: "",
            	ilGoodsId					: "",
            	itemInfo					: "",
            	ilItemCd					: "",
            	itemNm						: "",
            	pcsPerBox					: "",
            	oms							: "",
            	warehouseName				: "",
            	poTpNm						: "",
            	poEventQty					: "",
            	recevingReqDt   			: "",
            	saveBtnVisiable				: true,
            	poScheduleDt				: "",
            	eventStartDt				: "",
            	eventEndDt					: "",
           		inputSellersGroup			: "",
           		inputSellersDetail			: "",
           		memo						: ""
            },
         	fnDateChange2 : function(e, id){
    			e.preventDefault();

				//====================================
    			if (inputViewModel.inputInfo.get("eventStartDt") == null || inputViewModel.inputInfo.get("eventStartDt") == undefined || inputViewModel.inputInfo.get("eventStartDt") == '') {
					fnKendoMessage({ message : "행사시작일을 먼저 선택해주세요.", ok : function() {
						$("#eventStartDt").focus();
						inputViewModel.inputInfo.set("eventEndDt", "");
					}});
					return;
    			} else {
        			var eventStartDt = new Date(inputViewModel.inputInfo.get("eventStartDt"));
        			var eventEndDt = new Date(inputViewModel.inputInfo.get("eventEndDt"));

        			if( eventStartDt.getTime() > eventEndDt.getTime() ) {
    					fnKendoMessage({ message : "행사시작일 이후를 선택해주세요.", ok : function() {
    						$("#eventEndDt").focus();
    						inputViewModel.inputInfo.set("eventEndDt", "");
    					}});
    					return;
        			}
    			}

    			//====================================

    	        var selectDayStr = $("#recevingReqDt").val();
    	        var selectDayOfWeek = selectDayStr.substring(0, 3).toLowerCase();
    	        var selectDay = new Date(selectDayStr);
    	        var poScheduleDay = null;

    	        if(Object.keys(checkWeekObj).length != 0) {
    	        	var sortData = [];
    				// 배열로 재구성
    				$.each(checkWeekObj, function(index, value){
    				        sortData.push({key: index, value: value});
    				});

    				// 배열로 구성된 데이터를 정렬
    				sortData.sort(function(a, b){
    					return(a.value < b.value) ? -1 : (a.value > b.value) ? 1 : 0;
    				});

    				$.each(sortData, function(index, item) {

    					var dayOfWeek = item.key;
    					var scheduleCnt = item.value;

    					var checkWeekIndex = weekArray.indexOf(dayOfWeek) + parseInt(scheduleCnt);
    				    if(checkWeekIndex >=7) {
    				    	checkWeekIndex = checkWeekIndex % 7;
    				    }

    				    if(checkWeekIndex == selectDay.getDay()) {
    				    	poScheduleDay = new Date(selectDay);
    				    	poScheduleDay.setDate(poScheduleDay.getDate() - scheduleCnt);

    				    	return false;
    				    }

    				});

    				var poScheduleDayStr = "";
    				poScheduleDayStr = poScheduleDay.getFullYear();
    				poScheduleDayStr += "-";
            		if(poScheduleDay.getMonth() < 10) {
            			poScheduleDayStr += "0";
            			poScheduleDayStr += poScheduleDay.getMonth()+1;
            		}else {
            			poScheduleDayStr += poScheduleDay.getMonth()+1;
            		}
            		poScheduleDayStr += "-";
            		if(poScheduleDay.getDate() < 10) {
            			poScheduleDayStr += "0";
            			poScheduleDayStr += poScheduleDay.getDate();
            		}else {
            			poScheduleDayStr += poScheduleDay.getDate();
            		}

            		inputViewModel.inputInfo.set("poScheduleDt", poScheduleDayStr);
            		$("#poScheduleLabel").text(poScheduleDayStr);
    	        }



    		},
    		fnDateChange3 : function(e, id){ // 행사시작일 변경시, 입고요청일 및 발주요청일을 얻어온다.
    			e.preventDefault();

    			if (paramIlPoTpId == null || paramIlPoTpId == undefined || paramIlPoTpId == '') {
    				fnKendoMessage({ message : "상품을 먼저 선택해 주세요.", ok : function() {
						$("#fnGoodsSearch").focus();
						inputViewModel.inputInfo.set("eventStartDt", '');
					}});
    				return;
    			}

    			var eventStartDtNumber = $("#eventStartDt").data("kendoDatePicker")._oldText;

    			fnAjax({
    				url     : '/admin/item/potype/getItemPoDay',
    				params  : {ilPoTpId : paramIlPoTpId, eventStartDtNumber : eventStartDtNumber},
    				success :
    					function( data ){
    						var poDayInfo = data.rows;

    	    				var now = new Date();
    				    	var poScheduleDay = new Date(poDayInfo.poScheduleDt);
    	    				if (now.format('yyyyMMdd') > poScheduleDay.format('yyyyMMdd')) {
    		    				fnKendoMessage({ message : "발주요청일(" + poDayInfo.poScheduleDt + ")이 지나 요청이 불가능합니다.", ok : function() {
    								$("#eventStartDt").focus();
    	    						$("#recevingReqLabel").text('');//입고요청일
    	    						$("#poScheduleLabel").text('');//발주요청일
    	    						inputViewModel.inputInfo.set("recevingReqDt", '');
    	    						inputViewModel.inputInfo.set("poScheduleDt", '');
    	    						inputViewModel.inputInfo.set("eventStartDt", '');
    	    						inputViewModel.inputInfo.set("eventEndDt", '');
    							}});
    		    				return;
    		    			} else if (now.format('yyyyMMdd') == poScheduleDay.format('yyyyMMdd') && now.format('HHmmss') > '120000') {
    	        				fnKendoMessage({ message : "발주요청일이 당일인 경우 12:00 전까지만 저장 가능합니다. 행사 시작일을 다시 선택해 주세요.", ok : function() {
    	    						$("#eventStartDt").focus();
    	    						$("#recevingReqLabel").text('');//입고요청일
    	    						$("#poScheduleLabel").text('');//발주요청일
    	    						inputViewModel.inputInfo.set("recevingReqDt", "");
    	    						inputViewModel.inputInfo.set("poScheduleDt", "");
    	    						inputViewModel.inputInfo.set("eventStartDt", "");
    	    						inputViewModel.inputInfo.set("eventEndDt", "");
    	    					}});
    	        				return;
    	    				}

    						$("#recevingReqLabel").text(poDayInfo.recevingReqDt);//입고요청일
    						$("#poScheduleLabel").text(poDayInfo.poScheduleDt);//발주요청일
    						inputViewModel.inputInfo.set("recevingReqDt", poDayInfo.recevingReqDt);
    						inputViewModel.inputInfo.set("poScheduleDt", poDayInfo.poScheduleDt);
    						inputViewModel.inputInfo.set("eventEndDt", '');
    					},
    				isAction : 'select'
    			});

    		},
    		fnSelectGetWeek : function (selectDay){
    			var value = [];
    			var formatDate = function(date){

    				var myMonth = date.getMonth()+1;
    				var myWeekDay = date.getDate();

    				var addZero = function(num){
    					if (num < 10){
    						num = "0"+num;
    					}
    					return num;
    				}

    				var md = addZero(myMonth)+addZero(myWeekDay);
    				return md;
    			}

    			var now = new Date(selectDay);

    			var nowDayOfWeek = now.getDay();
    			var nowDay = now.getDate();
    			var nowMonth = now.getMonth();
    			var nowYear = now.getYear();
    			nowYear += (nowYear < 2000) ? 1900 : 0;
    			var weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek);
    			var weekEndDate = new Date(nowYear, nowMonth, nowDay + (6 - nowDayOfWeek));
    			value.push(nowYear+formatDate(weekStartDate));
    			value.push(nowYear+formatDate(weekEndDate));
    			return value;
    		},
        });

		kendo.bind($("#searchForm"), viewModel);
        kendo.bind($("#inputForm"), inputViewModel);
	};



	//묶음 상품 리스트 Grid
	function fnItemGrid() {

		itemGridDs = fnGetEditPagingDataSource({
			url : '/admin/item/poRequest/getPoRequestList',
			pageSize : PAGE_SIZE,
	           requestEnd : function(e) {
	        }
		});

		itemGridOpt = {
			dataSource : itemGridDs,
			pageable : {
				pageSizes : [ 20, 30, 50 ],
				buttonCount : 5
			},
			editable : false,
			navigatable: true,
			columns : [
				{ field : 'rownum',  title : 'No', width : '40px', attributes : { style : 'text-align:center' }, template: "<span class='row-number'></span>"}
			,	{ field : 'itemCd', title : '마스터품목코드<BR>품목바코드', width : '140px', attributes : { style : 'text-align:center' } ,
		          template : function(dataItem){
                      return dataItem.ilItemCd + "<BR>" + dataItem.itemBarcode;
		          },
                }
			,   { field : 'goodsTpName', title : '상품유형<BR>상품코드', width : '120px', attributes : { style : 'text-align:center' } ,
		          template : function(dataItem){
                      return dataItem.goodsTpName + "<BR>" + dataItem.ilGoodsId;
		          },
                }
			,   { field : 'goodsNm', title : '상품명', width : '140px', attributes : { style : 'text-align:center' }}
			,   { field : 'warehouseNm', title : '출고처', width : '160px', attributes : { style : 'text-align:center' }}
			,   { field : 'poTpNm', title : '발주유형', width : '120px', attributes : { style : 'text-align:center' }}
			,   { field : 'poScheduleDt', title : '발주예정일', width : '80px', attributes : { style : 'text-align:center' }}
			,   { field : 'recevingReqDt', title : '입고요청일', width : '80px', attributes : { style : 'text-align:center' }}
			,   { field : 'eventStartDt', title : '행사시작일', width : '80px', attributes : { style : 'text-align:center' }}
			,   { field : 'eventEndDt', title : '행사종료일', width : '80px', attributes : { style : 'text-align:center' }}
			,   { field : 'sellersNm', title : '판매처', width : '70px', attributes : { style : 'text-align:center' }}
			,   { field : 'pcsPerBox', title : '박스입수량', width : '70px', attributes : { style : 'text-align:center' }}
			,   { field : 'poEventQty', title : '행사발주수량', width : '70px', attributes : { style : 'text-align:center' }}
			,   { field : 'orderCnt', title : '주문수량', width : '70px', attributes : { style : 'text-align:center' }}
			,   { field : 'diffCnt', title : '차이수량', width : '70px', attributes : { style : 'text-align:center' }
					, template : function(dataItem){
						return dataItem.poEventQty - dataItem.orderCnt;
					}
			}
			,   { field : 'orderAvgCnt', title : '일평균(30일)<br>판매량', width : '80px', attributes : { style : 'text-align:center' }}
			,   { field : 'createId', title : '관리자', width : '100px', attributes : { style : 'text-align:center;white-space: nowrap;' },
		          template : function(dataItem){
		        	  if (dataItem.modifyDt == undefined || dataItem.modifyDt == null || dataItem.modifyDt == '') {
	                      return dataItem.userNm + "(" + dataItem.loginId + ")";
		        	  } else {
			        	  return dataItem.userNm + "(" + dataItem.loginId + ") <BR>/" + dataItem.modifyUserNm + "(" + dataItem.modifyLoginId + ")";
		        	  }
		          },
                }
			,   { field : 'ilPoEventId', title : 'ilPoEventId', width : '100px', attributes : { style : 'text-align:center;' }, hidden:true}
			,   { field : 'createDt', title : '등록/수정일', width : '100px', attributes : { style : 'text-align:center' },
		          template : function(dataItem){
		        	  if (dataItem.modifyDt == undefined || dataItem.modifyDt == null || dataItem.modifyDt == '') {
			        	  return dataItem.createDt;
		        	  } else {
			        	  return dataItem.createDt + "<BR>/" + dataItem.modifyDt;
		        	  }
		          },
                }
			,   { field : 'ilPoEventId', title : '관리', width : '100px', attributes : { style : 'text-align:center' },
        		template : function(dataItem){
        			var poScheduleDate = new Date(dataItem.poScheduleDt);
        			poScheduleDate.setHours(12,0,0);

					var html = '';
        			if(dataItem.poScheduleDt == null) {
        				if (fnIsProgramAuth("SAVE")) {
        					html += '<button type="button" class="k-button k-button-icontext k-grid-상세보기" kind="btnModify">수정 </button>';
        				}
        				if (fnIsProgramAuth("DELETE")) {
        					html += '<button type="button" class="k-button k-button-icontext k-grid-삭제" kind="btnDelete">삭제 </button>';
        				}
        			} else {
        				if(todayDate > poScheduleDate) {
        					html = '';
            			}
        				else {
            				if (fnIsProgramAuth("SAVE")) {
            					html += '<button type="button" class="k-button k-button-icontext k-grid-상세보기" kind="btnModify">수정 </button>';
            				}
            				if (fnIsProgramAuth("DELETE")) {
            					html += '<button type="button" class="k-button k-button-icontext k-grid-삭제" kind="btnDelete">삭제 </button>';
            				}
            			}
        			}

        			return html;
        		}
      		}

            ]
		};

		itemGrid = $('#itemGrid').initializeKendoGrid(itemGridOpt).cKendoGrid();

		itemGrid.bind("dataBound", function() {
        	let rowNum = itemGridDs._total - ((itemGridDs._page - 1) * itemGridDs._pageSize);
			$("#itemGrid tbody > tr .row-number").each(function(index){
				$(this).html(rowNum);
				rowNum--;
			});

			$('#pageTotalText').text( kendo.toString(itemGridDs._total, "n0") );
		});

		 $("#itemGrid").kendoTooltip({
	          filter: "td:nth-child(14)", //this filter selects the second column's cells
	          position: "right",
	          content: function(e){
	            var dataItem = $("#itemGrid").data("kendoGrid").dataItem(e.target.closest("tr"));
	            var content = dataItem.memo;
	            return content;
	          }
	     }).data("kendoTooltip");

		 // 상세 내역 버튼 이벤트
	    $('#itemGrid').on("click", "button[kind=btnModify]", function(e) {
	    	e.preventDefault();
		    let dataItem = itemGrid.dataItem($(e.currentTarget).closest("tr"));

		    fnAjax({
				url     : '/admin/item/poRequest/getPoRequest',
				params  : {ilPoEventId : dataItem.ilPoEventId},
				success :
					function( data ){
						fnBizCallback("select",data);
					},
				isAction : 'select'
			});
	    });

	    $('#itemGrid').on("click", "button[kind=btnDelete]", function(e) {
	    	e.preventDefault();
		    let dataItem = itemGrid.dataItem($(e.currentTarget).closest("tr"));

		    fnKendoMessage({message:'행사발주내역을 삭제하시겠습니까?', type : "confirm" , ok : function(){
		    	fnAjax({
					url     : '/admin/item/poRequest/delPoRequest',
					params  : {ilPoEventId : dataItem.ilPoEventId},
					success :
						function( data ){
							fnBizCallback("delete",data);
						},
					isAction : 'delete'
				});
			}});


	    });

	};

	//검색
	function fnSearch() {

		var searchVaild = true;

		if($("input[name=selectConditionType]:checked").val() == "singleSection") {

//			if( $.trim($('#searchItemCd').val()).length < 2) {
//				searchVaild = false;
//	            fnKendoMessage({
//	                message : '코드 검색 조회시 2글자 이상 입력해야 합니다.',
//	                ok : function() {
//	                    return false;
//	                }
//	            });
//			};

		}else {
			// 상품명 : trim 으로 공백 제거, 2글자 이상 입력해야 함
			if($.trim($('#goodsNm').val()).length == 1) {
				searchVaild = false;
	            fnKendoMessage({
	                message : '상품명 조회시 2글자 이상 입력해야 합니다.',
	                ok : function() {
	                    return;
	                }
	            });
			};

		}

		if(searchVaild) {
			var data = $('#searchForm').formSerialize(true);

			const	_pageSize = itemGrid && itemGrid.dataSource ? itemGrid.dataSource.pageSize() : PAGE_SIZE;

			var query = {
				page : 1,
				pageSize : _pageSize,
				filterLength : fnSearchData(data).length,
				filter : {
					filters : fnSearchData(data)
				}
			};

			itemGridDs.query(query);
		}
	};

	function fnClear() {
		fnDefaultSet();
	};

	function fnDefaultSet() {
		viewModel.searchInfo.set("searchItemCd", "");
		viewModel.searchInfo.set("searchEventStartDt", "");
		viewModel.searchInfo.set("searchEventEndDt", "");
		viewModel.searchInfo.set("searchSellersGroup", "");
		viewModel.searchInfo.set("searchSellersDetail", "");
		viewModel.searchInfo.set("searchSupplierId", "");
		viewModel.searchInfo.set("searchWarehouseId", "");
		viewModel.searchInfo.set("searchUserId", "");
		viewModel.searchInfo.set("searchNameType", "productNm");
		viewModel.searchInfo.set("searchName", "");
		viewModel.searchInfo.set("searchRecevingType", "equal");
		viewModel.searchInfo.set("searchEventType", "equal");
		viewModel.searchInfo.set("searchSellerGroup", "");
		viewModel.searchInfo.set("searchSellerDetail", "");
	};

	function fnNew() {
		$("#inputForm tr[modArea]").hide();
		var datepicker = $("#eventStartDt").data("kendoDatePicker");
		datepicker.enable(true);
		$("#fnSave").show();

		fnInputViewInit();
		fnKendoInputPoup({height:"1000px" ,width:"700px",title:{nullMsg :'행사발주 요청'} });
	};

	function fnInputViewInit() {
		inputViewModel.inputInfo.set("goodsInfo", "");
        inputViewModel.inputInfo.set("ilGoodsId", "");

        inputViewModel.inputInfo.set("itemInfo", "");
        inputViewModel.inputInfo.set("ilItemCd", "");

        inputViewModel.inputInfo.set("itemNm", "");
        inputViewModel.inputInfo.set("pcsPerBox", "");

        inputViewModel.inputInfo.set("warehouseName", "");
        inputViewModel.inputInfo.set("poTpNm", "");

        inputViewModel.inputInfo.set("poEventQty", "");

        inputViewModel.inputInfo.set("eventStartDt", "");
        inputViewModel.inputInfo.set("recevingReqDt", "");
        inputViewModel.inputInfo.set("eventEndDt", "");

        inputViewModel.inputInfo.set("inputSellersGroup", "");
        inputViewModel.inputInfo.set("inputSellersDetail", "");

        inputViewModel.inputInfo.set("memo", "");
        inputViewModel.inputInfo.set("saveBtnVisiable", true);

        fnKendoDatePicker({
			id : 'eventStartDt'
			, format : 'yyyy-MM-dd'
			, min: tomorrow
			, change: function(e){
				inputViewModel.fnDateChange3(e, "eventStartDt");
			}
		});

        $("#recevingReqLabel").text("");
        $("#poScheduleLabel").text("");
        checkWeekObj = new Object();
        checkWeekAry = [];

	};

	function fnGoodsSearch() {
		let params = {};
		fnKendoPopup({
			id			: "poGoodsSearchPopup",
			title		: "상품 검색",  // 해당되는 Title 명 작성
			width		: "1700px",
			height		: "800px",
			scrollable	: "yes",
			src			: "#/poGoodsSearchPopup",
			param		: params,
			success		: function( id, data ){

				if(data.parameter == undefined){
					inputViewModel.inputInfo.set("goodsInfo", data[0].goodsId + "  /  " + data[0].goodsName);
					inputViewModel.inputInfo.set("ilGoodsId", data[0].goodsId);

					inputViewModel.inputInfo.set("itemInfo", data[0].itemCode + "  /  " + data[0].itemBarcode);
					inputViewModel.inputInfo.set("ilItemCd", data[0].itemCode);

					inputViewModel.inputInfo.set("itemNm", data[0].itemName);

					inputViewModel.inputInfo.set("pcsPerBox", data[0].pcsPerBox + " " + data[0].oms);
					inputViewModel.inputInfo.set("warehouseName", data[0].warehouseName);

					if(data[0].ilPoTpId != undefined) {
						inputViewModel.inputInfo.set("poTpNm", data[0].poTpNm);

						paramIlPoTpId = data[0].ilPoTpId;

						fnPoTypeInfo(data[0].ilPoTpId);
					}

					// 행사 시작일, 종료일, 입고요청일, 발주요청일 초기화
					$("#recevingReqLabel").text('');//입고요청일
					$("#poScheduleLabel").text('');//발주요청일
					inputViewModel.inputInfo.set("eventStartDt", '');
					inputViewModel.inputInfo.set("recevingReqDt", '');
					inputViewModel.inputInfo.set("poScheduleDt", '');
					inputViewModel.inputInfo.set("eventEndDt", '');
				}
			}
		});
	}

	var checkWeekObj = new Object();
	var checkWeekAry = [];
	var weekArray = ["sun", "mon", "tue", "wed", "thu", "fri", "sat"];

	function fnPoTypeInfo(ilPoTpId) {

		fnAjax({
			url     : '/admin/item/potype/getItemPoType',
			params  : {ilPoTpId : ilPoTpId},
			success :
				function( data ){
					var poTpInfo = data.rows;


					if(poTpInfo.checkSun == "Y") {
						checkWeekObj["sun"] = poTpInfo.scheduledSun;
						var checkWeekIndex = weekArray.indexOf("sun") + parseInt(poTpInfo.scheduledSun);
						if(checkWeekIndex >=7) {
							checkWeekIndex = checkWeekIndex % 7;
						}
						checkWeekAry.push(weekArray[checkWeekIndex]);
					}

					if(poTpInfo.checkMon == "Y") {
						checkWeekObj["mon"] = poTpInfo.scheduledMon;
						var checkWeekIndex = weekArray.indexOf("mon") + parseInt(poTpInfo.scheduledMon);
						if(checkWeekIndex >=7) {
							checkWeekIndex = checkWeekIndex % 7;
						}
						checkWeekAry.push(weekArray[checkWeekIndex]);
					}

					if(poTpInfo.checkTue == "Y") {
						checkWeekObj["tue"] = poTpInfo.scheduledTue;

						var checkWeekIndex = weekArray.indexOf("tue") + parseInt(poTpInfo.scheduledTue);
						if(checkWeekIndex >=7) {
							checkWeekIndex = checkWeekIndex % 7;
						}
						checkWeekAry.push(weekArray[checkWeekIndex]);
					}

					if(poTpInfo.checkWed == "Y") {
						checkWeekObj["wed"] = poTpInfo.scheduledWed;

						var checkWeekIndex = weekArray.indexOf("wed") + parseInt(poTpInfo.scheduledWed);
						if(checkWeekIndex >=7) {
							checkWeekIndex = checkWeekIndex % 7;
						}
						checkWeekAry.push(weekArray[checkWeekIndex]);
					}

					if(poTpInfo.checkThu == "Y") {
						checkWeekObj["thu"] = poTpInfo.scheduledThu;

						var checkWeekIndex = weekArray.indexOf("thu") + parseInt(poTpInfo.scheduledThu);
						if(checkWeekIndex >=7) {
							checkWeekIndex = checkWeekIndex % 7;
						}
						checkWeekAry.push(weekArray[checkWeekIndex]);

					}

					if(poTpInfo.checkFri == "Y") {
						checkWeekObj["fri"] = poTpInfo.scheduledFri;

						var checkWeekIndex = weekArray.indexOf("fri") + parseInt(poTpInfo.scheduledFri);
						if(checkWeekIndex >=7) {
							checkWeekIndex = checkWeekIndex % 7;
						}
						checkWeekAry.push(weekArray[checkWeekIndex]);
					}

					if(poTpInfo.checkSat == "Y") {
						checkWeekObj["sat"] = poTpInfo.scheduledSat;

						var checkWeekIndex = weekArray.indexOf("sat") + parseInt(poTpInfo.scheduledSat);
						if(checkWeekIndex >=7) {
							checkWeekIndex = checkWeekIndex % 7;
						}
						checkWeekAry.push(weekArray[checkWeekIndex]);
					}

					let disableDate = [];
					if(checkWeekAry.length > 0) {
						disableDate = weekArray.filter(x => !checkWeekAry.includes(x));
					}


					fnKendoDatePicker({
						id : 'eventStartDt'
						, format : 'yyyy-MM-dd'
						, min: tomorrow
						//, disableDates: disableDate
						, change: function(e){
							inputViewModel.fnDateChange3(e, "eventStartDt");
						}
					});
					$("#eventStartDt").data("kendoDatePicker").unbind("blur");
				},
			isAction : 'select'
		});

	};
	function fnSave() {

		var url  = '/admin/item/poRequest/addPoRequest';
		var cbId = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/item/poRequest/putPoRequest';
			cbId= 'update';
		}
		var data = $('#inputForm').formSerialize(true);

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

	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
	            inputViewModel.inputInfo.set("goodsInfo", data.detail.ilGoodsId + "  /  " + data.detail.goodsNm);
	            inputViewModel.inputInfo.set("ilGoodsId", data.detail.ilGoodsId);
	            inputViewModel.inputInfo.set("ilPoEventId", data.detail.ilPoEventId);

	            inputViewModel.inputInfo.set("itemInfo", data.detail.ilItemCd + "  /  " + data.detail.itemBarcode);
	            inputViewModel.inputInfo.set("ilItemCd", data.detail.ilItemCd);

	            inputViewModel.inputInfo.set("itemNm", data.detail.itemNm);
	            inputViewModel.inputInfo.set("pcsPerBox", data.detail.pcsPerBox+ " " + data.detail.oms);

	            inputViewModel.inputInfo.set("warehouseName", data.detail.warehouseNm);
	            inputViewModel.inputInfo.set("poTpNm", data.detail.poTpNm);

	            inputViewModel.inputInfo.set("poEventQty", data.detail.poEventQty);

	            inputViewModel.inputInfo.set("recevingReqDt", data.detail.recevingReqDt);
	            inputViewModel.inputInfo.set("poScheduleDt", data.detail.poScheduleDt);
	            inputViewModel.inputInfo.set("eventStartDt", data.detail.eventStartDt);
	            inputViewModel.inputInfo.set("eventEndDt", data.detail.eventEndDt);

	            inputViewModel.inputInfo.set("inputSellersGroup", data.detail.sellersGroupCd);
	            inputViewModel.inputInfo.set("inputSellersDetail", data.detail.omSellersId);

	            inputViewModel.inputInfo.set("memo", data.detail.memo);

	            if(data.detail.poScheduleDt != null) {
	            	var poScheduleDate = new Date(data.detail.poScheduleDt);
		            poScheduleDate.setHours(12,0,0);
		            var datepicker = $("#eventStartDt").data("kendoDatePicker");

		            if(todayDate > poScheduleDate) {
		            	datepicker.enable(false);
		            	inputViewModel.inputInfo.set("saveBtnVisiable", false);
		            	$("#fnSave").hide();
		            }else {
		            	datepicker.enable(true);
		            	$("#fnSave").show();
		            }

		            $("#recevingReqLabel").text(data.detail.recevingReqDt);
		            $("#poScheduleLabel").text(data.detail.poScheduleDt);
	            }


	            var createDt = data.detail.createDt;
	            if(data.detail.userNm != null) {
	            	createDt += "&nbsp;&nbsp;"+ data.detail.userNm + "(" + data.detail.loginId + ")";
	            }

	            var modifyDt = data.detail.modifyDt;
	            if(data.detail.modifyUserNm != null) {
	            	modifyDt += "&nbsp;&nbsp;"+ data.detail.modifyUserNm + "(" + data.detail.modifyLoginId + ")";
	            }

				$("#createDate").html(createDt);
				$("#modifyDate").html(modifyDt);

				$("#inputForm tr[modArea]").show();
				OPER_TP_CODE = "U";
				fnKendoInputPoup({height:"1000px" ,width:"700px", title:{ nullMsg :'행사발주 요청 상세' } });

				paramIlPoTpId = data.detail.ilPoTpId;

				fnPoTypeInfo(data.detail.ilPoTpId);

				break;
			case 'insert':
				fnSearch();
				$('#kendoPopup').data('kendoWindow').close();
				$('#searchForm').formClear(true);
				fnKendoMessage({message : '저장되었습니다.'});
				break;
			case 'update':
				fnSearch();
				fnKendoMessage({message : '수정되었습니다.'});
				fnClose();
				break;
			case 'delete':
				fnSearch();
				fnClose();
				fnKendoMessage({message : '삭제되었습니다.'});
				break;

		}  // switch(id){
	}  // function fnBizCallback( id, data ){

	// 닫기
	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	function fnEmployeeSearchPopup(apprManagerType){
		fnKendoPopup({
			id     : 'employeeSearchPopup',
			title  : 'BOS 계정 선택',
			src    : '#/employeeSearchPopup',
			width  : '1050px',
			height : '800px',
            scrollable : "yes",
			success: function( stMenuId, data ){
				if(!fnIsEmpty(data.loginId)){

					viewModel.searchInfo.set("searchUserId", data.loginId);
				}else{
//					fnKendoMessage({ message : "올바르지 않은 관리자입니다." });
				}
			}
		});
	};

	function fnExcelExport() {
		var data = $('#searchForm').formSerialize(true);
		fnExcelDownload('/admin/item/poRequest/createPoRequestList', data);
	};


	/** Common Search */
	$scope.fnSearch = function() {
		fnSearch();
	};
	$scope.fnExcelExport = function() {
		fnExcelExport();
	};


	/** Common Clear */
	$scope.fnClear = function() {
		fnClear();
	};

	$scope.fnNew = function() {
		fnNew();
	};

	$scope.fnGoodsSearch = function() {
		fnGoodsSearch();
	};

	$scope.fnSave = function() {
		fnSave();
	};

	$scope.fnEmployeeSearchPopup = function(apprManagerType){	 fnEmployeeSearchPopup(apprManagerType);};


});
