/**-----------------------------------------------------------------------------
 * description 		 : 발주리스트
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.26		이성준          최초생성
 * **/
'use strict';

var pfGridDs, pfGridOpt, pfGrid, ogGridDs, ogGridOpt, ogGrid;
var urSupplierId;
var urWarehouseId;
var anchoredTag = null;
var pfSupplierCode = 1; // 공급업체 - 풀무원식품 의 코드값
var ogSupplierCode = 2; // 공급업체 - 올가홀푸드 의 코드값


$(document).ready(function() {

	fnInitialize();	// Initialize Page Call ---------------------------------

	// Initialize Page
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'poResultList',
			callback : fnUI
		});

	}

	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	// Initialize Button ---------------------------------

		fnInitGrid();	// Initialize Grid ------------------------------------

		fnInitMaskTextBox();// Initialize Input Mask

		fnInitOptionBox();// Initialize Option Box

	}

	// --------------------------------- Button
	// Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnClear, #fnExcelDown').kendoButton();
	}
	// 검색
	function fnSearch(){

		urSupplierId  = $("#searchUrSupplierId").val();
		urWarehouseId = $("#searchUrWarehouseId").val();

		var data = $('#searchForm').formSerialize(true);
		var query = {
					filterLength : fnSearchData(data).length,
					filter :  {
						filters : fnSearchData(data)
					}
		};

		if(urSupplierId == pfSupplierCode && urWarehouseId != ''){		//풀무원식품
		  $("#pfGrid").show();
		  $("#ogGrid").hide();
		  fnGetPoInfoList();	// 발주정보 조회
		  pfGridDs.query( query );
		}else if(urSupplierId == ogSupplierCode && urWarehouseId != ''){	//올가홀푸드
		  $("#ogGrid").show();
		  $("#pfGrid").hide();
		  fnGetPoInfoList();	// 발주정보 조회
		  ogGridDs.query( query );
		}else{
		  $("#pfGrid").show();
		  $("#ogGrid").hide();
		  $('#poInfoTable > tbody > tr > td > pre > pre').remove();	// UI초기화
		  $('#totalPiecePoQty').text("0");	// 낱개 발주수량 합계 SET
		  $('#totalBoxPoQty').text("0");	// BOX 발주수량 합계 SET
		  $('#userSavedInfo').text("");		// 저장시간/관리자 SET
		  pfGridDs.data([]);
		}
	}

	//발주정보 조회
	function fnGetPoInfoList(){

		var data = $('#searchForm').formSerialize(true);

		fnAjax({
			url     : '/admin/item/po/getPoInfoList',
			method : 'GET',
			params  : data,
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'select'
		});
	}

	//그리드 초기값 설정
	function fnGridDefault(){
		$("#pfGrid").show();
		$("#ogGrid").hide();
	}

	// 초기화
	function fnClear(){
		$('#searchForm').formClear(true);

		fnGridDefault();

		$('#searchBaseDt').val(fnGetToday());

		// 발주유형 구분 '전체' 체크
        $('input[name=searchPoTypeGubun]').each(function(idx, element) {
            $(element).prop('checked', true);
        });

        // 보관방법 '전체' 체크
		$('input[name=searchStorageMethod]').each(function(idx, element) {
		    $(element).prop('checked', true);
		});

        // 상품 판매상태 '전체' 체크
        $('input[name=searchSaleStatus]').each(function(idx, element) {
            $(element).prop('checked', true);
        });

        // 상품 전시상태 '전체' 체크
        $('input[name=searchGoodsDisplayStatus]').each(function(idx, element) {
            $(element).prop('checked', true);
        });

		// 발주유형 '전체' disabled 처리
		$('input[name=searchPoType]').each(function(idx, element) {
            $(element).prop('disabled', true);
		});

	}

	//엑셀다운로드
    function fnExcelDown(){
    	var data = $('#searchForm').formSerialize(true);
		fnExcelDownload('/admin/item/po/getPoResultListExportExcel', data);
    }

	// 닫기
	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	// --------------------------------- Button
	// End---------------------------------


	// ------------------------------- Grid Start
	// -------------------------------
	function fnInitGrid(){
		// 풀무원식품 조회시.
		pfGridDs = fnGetPagingDataSource({
			url      : '/admin/item/po/getPoResultList'
			, model_id     : 'ilPoId'  //model_id 는 pk역할을 해서 수정모드에서 활성화가 안된다.
		});

		pfGridOpt = {
			dataSource: pfGridDs
			, pageable  : false
			, navigatable : true
			, scrollable : true
			,   editable : false
			, sortable : true
			// , height : 755
			, columns : [
				{ title : '품목/상품정보'
					, columns: [
						{ title:'No.', width:'40px', attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>", sortable:false}
						, { field:'ilPoId', title : '발주SEQ', width:'80px', attributes:{ style:'text-align:center' }, sortable:false, hidden:true, editable : function() { return false; }}
						, { field:'poTpNm', title : '발주유형<br>-', width:'80px', attributes:{ style:'text-align:center' }
							, template: function(dataItem) {
								//return dataItem.poTpNm + '/<br/>' + dataItem.poTpTemplateNm;
								return dataItem.poTpTemplateNm;
							}
							, editable : function() { return false; }
						}
						, { field:'ilItemCd', title : '품목코드/<br>품목바코드', width:'130px', attributes:{ style:'text-align:center' }, sortable:false
							, template: function(dataItem) {
								if(dataItem.barcode != ""){
									var html = kendo.htmlEncode(dataItem.ilItemCd) + "/<br/>" + kendo.htmlEncode(dataItem.barcode);
								} else {
									var html = kendo.htmlEncode(dataItem.ilItemCd) + "/-";
								}
								return html;
							}
							, editable : function() { return false; }
						}
						, { field:'itemNm', title : '마스터품목명', width:'110px', attributes:{ style:'text-align:center' }, sortable:false
							, template: function(dataItem) {
								return "<a class='itemDetlInfo' kind='itemDetlInfo' style='color:blue; cursor:pointer' onclick='$scope.fnOpenCard(event, " + JSON.stringify(dataItem) + ");'>" + kendo.htmlEncode(dataItem.itemNm) + "</a>";
							}
							, editable : function() { return false; }
						}
						, { field:'storageMethodNm', title : '보관방법', width:'60px', attributes:{ style:'text-align:center' }, sortable:false, editable : function() { return false; }}
						, { field:'distributionPeriod', title : '유통기간<br>-', width:'50px', attributes:{ style:'text-align:center' }, editable : function() { return false; }}
						, { field:'pcsPerBox', title : '박스<br>입수량', width:'50px', attributes:{ style:'text-align:center' }, sortable:false, editable : function() { return false; }}
						//, { field:'dispYn'	         ,title : '상품<br>전시상태'	         , width:'60px' ,attributes:{ style:'text-align:center' }, sortable:false
						//	, template: function(dataItem) {
						//		var html = "전시";
						//		if(dataItem.dispYn != "Y"){
						//		html = "미전시";
						//		}
						//		return html;
						//	}
						//	, editable : function (){ return false; }
						//}
						, { field:'saleStatusNm', title : '상품<br>판매상태<br>(외부몰<br>판매상태)<br>-', width:'80px', attributes:{ style:'text-align:center' }
							, template: function(dataItem) {
								var html = kendo.htmlEncode(dataItem.saleStatusNm) + "<br/>(" + kendo.htmlEncode(dataItem.goodsOutmallSaleStatNm) + ")";
								return html;
							}
							, editable : function() { return false; }
						}
//						, { field:'ilGoodsId' ,title : '상품코드', width:'60px', attributes:{ style:'text-align:center' }, sortable:false, editable : function() { return false; }}
//						, { field:'erpCtgryLv1Id', title : 'ERP<br>카테고리', width:'80px', attributes:{ style:'text-align:center' }, editable : function (){ return false; }}
					]
				}
				, { title : '발주정보'
					, columns: [
						{ field:'targetStock', title : '안전재고<br>(목표재고)', width:'60px', attributes:{ style:'text-align:center' }, sortable:false, editable : function() { return false; }}
						, { field:'recommendPoQty', title : '권고수량<br>-', width:'60px', attributes:{ style:'text-align:center' }, editable : function() { return false; }}
						, { field:'piecePoQty'        ,title : '낱개<br>발주수량<br>-' , width:'80px'	,attributes:{ style:'text-align:center'}
							, editable : function() { return false; }
				        }
						, { field:'poSystemQty'		  ,title : '추가<br>발주수량'	   , width:'100px'	,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function() { return false; }
						}
			             , { field:'boxPoQty'          ,title : 'BOX<br>발주수량'	   , width:'50px'	,attributes:{ style:'text-align:center' }, sortable:false
					   		, editable : function() { return false; }
			             }
			             , { field:'eventPoQty'		  ,title : '행사<br>발주수량<br>-'	   , width:'70px'	,attributes:{ style:'text-align:center' }
					   		, editable : function() { return false; }
			             }
						 , { field:'poIfYn'		  ,title : '발주여부'	   , width:'50px'	,attributes:{ style:'text-align:center' }
							, editable : function() { return false; }
						 }
			             ,{ field:'manager'		  ,title : '관리자'           , width:'100px'	,attributes:{ style:'text-align:center' }, sortable:false
							, template: function(dataItem) {
								var html = "";
								if (dataItem.userSavedDt != undefined && dataItem.userSavedDt != null) {
									html = dataItem.userNm + ' / ' + dataItem.loginId + '<br/>' + dataItem.userSavedDt;
								}
								return html;
							}
							, editable : function() { return false; }
			             }
					 ]}
				,{ title : '재고정보'
					, columns: [
						{ field:'stockClosed'	    ,title : '전일<br>마감재고<br>-'   , width:'60px'	,attributes:{ style:'text-align:center' }
							, editable : function() { return false; }
						}
						, { field:'stockDiscardD0'		,title : '폐기<br>예정수량'   , width:'60px'	,attributes:{ style:'text-align:center' }, sortable:false
							, template: function(dataItem) {
								return dataItem.stockDiscardD0 + dataItem.stockDiscardD1;
							}
							, editable : function() { return false; }
						}
						, { field:'stockConfirmed'    ,title : '입고확정<br>(입고예정)<br>-', width:'60px'	    ,attributes:{ style:'text-align:center' }
							, template: function(dataItem) {
								return dataItem.stockConfirmed + '<br>(' + dataItem.stockScheduledD0 + ')';
							}
							, editable : function() { return false; }
						}
						, { field:'stockScheduledD1More',title : '입고<br>대기수량<br>-'   , width:'60px'	,attributes:{ style:'text-align:center' }
							, template: function(dataItem) {
								var html = dataItem.stockScheduledD1More;

								if (dataItem.stockScheduledD1 > 0) {
									html += '<br>(' + fnGetDayAdd(dataItem.baseDt, 1) + ' : ' + dataItem.stockScheduledD1 + ')';
								}
								if (dataItem.stockScheduledD2 > 0) {
									html += '<br>(' + fnGetDayAdd(dataItem.baseDt, 2) + ' : ' + dataItem.stockScheduledD2 + ')';
								}
								if (dataItem.stockScheduledD3 > 0) {
									html += '<br>(' + fnGetDayAdd(dataItem.baseDt, 3) + ' : ' + dataItem.stockScheduledD3 + ')';
								}
								if (dataItem.stockScheduledD4 > 0) {
									html += '<br>(' + fnGetDayAdd(dataItem.baseDt, 4) + ' : ' + dataItem.stockScheduledD4 + ')';
								}
								if (dataItem.stockScheduledD5 > 0) {
									html += '<br>(' + fnGetDayAdd(dataItem.baseDt, 5) + ' : ' + dataItem.stockScheduledD5 + ')';
								}
								if (dataItem.stockScheduledD6 > 0) {
									html += '<br>(' + fnGetDayAdd(dataItem.baseDt, 6) + ' : ' + dataItem.stockScheduledD6 + ')';
								}
								if (dataItem.stockScheduledD7 > 0) {
									html += '<br>(' + fnGetDayAdd(dataItem.baseDt, 7) + ' : ' + dataItem.stockScheduledD7 + ')';
								}
								if (dataItem.stockScheduledD8More > 0) {
									html += '<br>(' + fnGetDayAdd(dataItem.baseDt, 8) + '~ : ' + dataItem.stockScheduledD8More + ')';
								}
								return html;
							}
							, editable : function() { return false; }
						}
						, { field:'expectedResidualQty' ,title : '예상<br>잔여수량<br>-'   , width:'60px'	,attributes:{ style:'text-align:center' }
							, editable : function() { return false; }
						}
		            ]
				}
				,{ title : '주문/출고정보'
					,columns: [{field:'outbound0'   	  ,title : 'D-0'             , width:'50px'	,attributes:{ style:'text-align:center' }, sortable:false
								, editable : function() { return false; }
				             }
				             ,{ field:'outbound1'		  ,title : 'D+1'             , width:'50px'	,attributes:{ style:'text-align:center' }, sortable:false
						   		, editable : function() { return false; }
				             }
				             ,{ field:'outbound2'	      ,title : 'D+2'             , width:'50px'	,attributes:{ style:'text-align:center' }, sortable:false
						   		, editable : function() { return false; }
				             }
				             ,{ field:'outbound3More'     ,title : 'D+3<br>이상'      , width:'50px' ,attributes:{ style:'text-align:center' }, sortable:false
						   		, editable : function() { return false; }
				             }
				             , { field:'outboundDayAvg'    ,title : '일평균<br>출고량<br>-'      , width:'50px' ,attributes:{ style:'text-align:center' }
									, editable : function (){ return false; }
					         }
				             ,{ field:'missedOutbound'	  ,title : '결품<br>예상일'         , width:'50px'	,attributes:{ style:'text-align:center' }, sortable:false
							   		, editable : function() { return false; }
				             }
				             ,{ field:'outbound3weekTotal'  ,title : '직전 3주<br>출고수량' , width:'50px'	,attributes:{ style:'text-align:center' }, sortable:false
						   		, editable : function() { return false; }
				             }
				             ,{ field:'outbound2weekTotal'  ,title : '직전 2주<br>출고수량' , width:'50px'	,attributes:{ style:'text-align:center' }, sortable:false
						   		, editable : function() { return false; }
				             }
				             ,{ field:'outbound1weekTotal'  ,title : '직전 1주<br>출고수량' , width:'50px'	,attributes:{ style:'text-align:center' }, sortable:false
						   		, editable : function() { return false; }
				             }
				             ,{ field:'memo'		      ,title : '발주메모'          , width:'100px'	,attributes:{ style:'text-align:center' }, sortable:false}
						]}
			]
		};
		pfGrid = $('#pfGrid').initializeKendoGrid( pfGridOpt ).cKendoGrid();
		pfGrid.bind("dataBound", function(e) {
//			let rowNum = pfGridDs._total;
			$("#pfGrid tbody > tr .row-number").each(function(index){
//				$(this).html(rowNum);
//				rowNum--;
				$(this).html(index+1);
			});

			//total count
            $('#countTotalSpan').text(pfGridDs._total);
			var gridElement = $("#pfGrid");
			resizeGrid(gridElement);
		});

		pfGrid.bind("sort", function(e) {
			if (pfGridDs._total == undefined || pfGridDs._total == null || pfGridDs._total == 0) {
				e.preventDefault();
				return false;
			}
		});

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ogGridDs = fnGetPagingDataSource({
			url      : '/admin/item/po/getPoResultList'
			, model_id     : 'ilPoId'  //model_id 는 pk역할을 해서 수정모드에서 활성화가 안된다.
		});

		ogGridOpt = {
				dataSource: ogGridDs
			,  	pageable  : false
			,	navigatable : true
			,   scrollable : true
			,   sortable : true
			,   editable : false
			// ,   height : 755
			,	columns   : [
				 { title : '품목/상품정보'
				   ,columns: [
				             { title:'No.'		          ,width:'40px'	   , attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>", sortable:false }
				             ,{ field:'ilPoId'	   ,title : '발주SEQ'	   , width:'80px'	 ,attributes:{ style:'text-align:center' }, sortable:false, hidden:true
						   		, editable : function (){ return false; }
				              }
				             ,{ field:'poTpNm'		      ,title : '발주유형<br>-'			     , width:'80px'	 ,attributes:{ style:'text-align:center' }
			            	    , template: function(dataItem) {
				             		var poTpNm = dataItem.poTpTemplateNm;
				             		var result = poTpNm.replace('품목별상이', 'R2발주');			// 품목별상이 -> R2발주로 변경

								 	return result;
			            	    }
						   		, editable : function (){ return false; }
				             }
							 ,{ field:'erpCtgryLv1Id'      ,title : 'ERP카테고리<br>(대분류)' 	, width:'80px'	 ,attributes:{ style:'text-align:center' }
							 	, editable : function (){ return false; }
							 }
							 ,{ field:'erpEvent'		  ,title : '행사정보<br>(올가전용)'    , width:'80px'	 ,attributes:{ style:'text-align:center' }
							   	, editable : function (){ return false; }
							 }
				             ,{ field:'ilItemCd'	  ,title : '품목코드/<br>품목바코드', width:'110px' ,attributes:{ style:'text-align:center' }, sortable:false
				            	, template: function(dataItem) {
										if(dataItem.barcode != ""){
										   var html = kendo.htmlEncode(dataItem.ilItemCd) + "/<br/>" +
													  kendo.htmlEncode(dataItem.barcode);
										}else{
										   var html = kendo.htmlEncode(dataItem.ilItemCd) + "/-";
										}

										return html;
								  }
						   		, editable : function (){ return false; }
				              }
//				             ,{ field:'ilGoodsId'    ,title : '상품코드'		             , width:'60px'	 ,attributes:{ style:'text-align:center' }, sortable:false
//							   	, editable : function (){ return false; }
//				             }
				             ,{ field:'itemNm'		      ,title : '마스터품목명'			     , width:'100px' ,attributes:{ style:'text-align:center' }, sortable:false
			            	    , template: function(dataItem) {
									return "<a class='itemDetlInfo' kind='itemDetlInfo' style='color:blue; cursor:pointer' onclick='$scope.fnOpenCard(event, " + JSON.stringify(dataItem) + ");'>" + kendo.htmlEncode(dataItem.itemNm) + "</a>";
				            	}
							   	, editable : function (){ return false; }
						     }
				             ,{ field:'dispYn'	         ,title : '상품<br>전시상태'	         , width:'60px' ,attributes:{ style:'text-align:center' }, sortable:false
				            	 , template: function(dataItem) {
				            		 var html = "전시";
				            		 if(dataItem.dispYn != "Y"){
										html = "미전시";
									 }
				            		 return html;
							     }
							   	, editable : function (){ return false; }
				             }
							, { field:'saleStatusNm', title : '상품<br>판매상태<br>(외부몰<br>판매상태)<br>-', width:'80px', attributes:{ style:'text-align:center' }
								, template: function(dataItem) {
									var html = kendo.htmlEncode(dataItem.saleStatusNm) + "<br/>(" + kendo.htmlEncode(dataItem.goodsOutmallSaleStatNm) + ")";
									return html;
								}
							   	, editable : function (){ return false; }
				             }
							 // ,{ field:'ctgryStdNm'		  ,title : '표준카테고리<br>(대분류)'      , width:'80px'	 ,attributes:{ style:'text-align:center' }, sortable:false
							 //   	, editable : function (){ return false; }
							 // }
							 ]}
				,{ title : '발주정보'
					,columns: [
						{ field:'poProRea'		  ,title : 'off발주상태<br>(올가전용)<br>-', width:'90px'	 ,attributes:{ style:'text-align:center' }
							, editable : function (){ return false; }
						}
						//, { field:'targetStock'	,title : '안전재고<br>(목표재고)'   , width:'60px'	,attributes:{ style:'text-align:center' }, sortable:false
						//	, editable : function (){ return false; }
						//}
						, { field:'recommendPoQty'	 ,title : '권고수량<br>-'		     , width:'60px'	,attributes:{ style:'text-align:center' }
							, editable : function (){ return false; }
						}
						, { field:'piecePoQty'       ,title : '낱개<br>발주수량<br>-'   , width:'80px'	,attributes:{ style:'text-align:center' }
						, editable : function() { return false; }
						}
						, { field:'poSystemQty'		  ,title : '추가<br>발주수량'	   , width:'100px'	,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function() { return false; }
						}
//				             ,{ field:'boxPoQty'          ,title : 'BOX<br>발주수량'	     , width:'50px'	 ,attributes:{ style:'text-align:center' }, sortable:false
//							   	, editable : function (){ return false; }
//				             }
						, { field:'eventPoQty'		  ,title : '행사<br>발주수량<br>-'	     , width:'70px' ,attributes:{ style:'text-align:center' }
							, editable : function (){ return false; }
						}
						, { field:'stockScheduledDt'  ,title : '입고<br>예정일자'	     , width:'90px' ,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function (){ return false; }
						}
						, { field:'poIfYn'		  ,title : '발주여부'	   , width:'50px'	,attributes:{ style:'text-align:center' }
							, editable : function() { return false; }
						}
						, { field:'manager'		  ,title : '관리자'           , width:'140px'	,attributes:{ style:'text-align:center' }, sortable:false
							, template: function(dataItem) {
								var html = "";
								if (dataItem.userSavedDt != undefined && dataItem.userSavedDt != null) {
									html = dataItem.userNm + ' / ' + dataItem.loginId + '<br/>' + dataItem.userSavedDt;
								}
								return html;
							}
							, editable : function (){ return false; }
						}
					]
				}
				,{ title : '재고정보'
					,columns: [
						{ field:'stockClosed'	    ,title : '전일<br>마감재고<br>-'        , width:'60px'	,attributes:{ style:'text-align:center' }
							, editable : function (){ return false; }
						}
						, { field:'stockConfirmed'    ,title : '입고확정<br>(입고예정)<br>-', width:'60px'	    ,attributes:{ style:'text-align:center' }
							, template: function(dataItem) {
								return dataItem.stockConfirmed + '<br>(' + dataItem.stockScheduledD0 + ')';
							}
							, editable : function (){ return false; }
						}
						, { field:'stockScheduledD1More',title : '입고<br>대기수량<br>-'        , width:'60px'	,attributes:{ style:'text-align:center' }
							, template: function(dataItem) {
								var html = dataItem.stockScheduledD1More;

								if (dataItem.stockScheduledD1 > 0) {
									html += '<br>(' + fnGetDayAdd(dataItem.baseDt, 1) + ' : ' + dataItem.stockScheduledD1 + ')';
								}
								if (dataItem.stockScheduledD2 > 0) {
									html += '<br>(' + fnGetDayAdd(dataItem.baseDt, 2) + ' : ' + dataItem.stockScheduledD2 + ')';
								}
								if (dataItem.stockScheduledD3 > 0) {
									html += '<br>(' + fnGetDayAdd(dataItem.baseDt, 3) + ' : ' + dataItem.stockScheduledD3 + ')';
								}
								if (dataItem.stockScheduledD4 > 0) {
									html += '<br>(' + fnGetDayAdd(dataItem.baseDt, 4) + ' : ' + dataItem.stockScheduledD4 + ')';
								}
								if (dataItem.stockScheduledD5 > 0) {
									html += '<br>(' + fnGetDayAdd(dataItem.baseDt, 5) + ' : ' + dataItem.stockScheduledD5 + ')';
								}
								if (dataItem.stockScheduledD6 > 0) {
									html += '<br>(' + fnGetDayAdd(dataItem.baseDt, 6) + ' : ' + dataItem.stockScheduledD6 + ')';
								}
								if (dataItem.stockScheduledD7 > 0) {
									html += '<br>(' + fnGetDayAdd(dataItem.baseDt, 7) + ' : ' + dataItem.stockScheduledD7 + ')';
								}
								if (dataItem.stockScheduledD8More > 0) {
									html += '<br>(' + fnGetDayAdd(dataItem.baseDt, 8) + '~ : ' + dataItem.stockScheduledD8More + ')';
								}
								return html;
							}
							, editable : function (){ return false; }
						}
						, { field:'expectedResidualQty',title : '예상<br>잔여수량<br>-'        , width:'60px'	,attributes:{ style:'text-align:center' }
							, editable : function (){ return false; }
						}
						, { field:'offStock'		    ,title : 'off재고<br>(올가전용)<br>-'   , width:'60px',attributes:{ style:'text-align:center' }
							, editable : function (){ return false; }
						}
					]
				}
				,{ title : '주문/출고정보'
					, columns: [
						{ field:'outbound0'		  ,title : 'D-0'             , width:'50px'	,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function (){ return false; }
						}
						, { field:'outbound1'		  ,title : 'D+1'             , width:'50px'	,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function (){ return false; }
						}
						, { field:'outbound2'	      ,title : 'D+2'             , width:'50px'	,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function (){ return false; }
						}
						, { field:'outbound3More'    ,title : 'D+3<br>이상'      , width:'50px' ,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function (){ return false; }
						}
						, {field:'erpEventOrderAvg',title : '프로모션<br>판매평균'   , width:'80px'	,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function (){ return false; }
						}
						, {field:'nonErpEventOrderAvg',title : '일반<br>판매평균'   , width:'60px'	,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function (){ return false; }
						}
						, { field:'memo'		     ,title : '발주메모'          , width:'100px'	,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function (){ return false; }
						}
					]
				}
			]
		};
		ogGrid = $('#ogGrid').initializeKendoGrid( ogGridOpt ).cKendoGrid();
		ogGrid.bind("dataBound", function(e) {
//			let rowNum = ogGridDs._total;
			$("#ogGrid tbody > tr .row-number").each(function(index){
//				$(this).html(rowNum);
//				rowNum--;
				$(this).html(index+1);
			});

			//total count
            $('#countTotalSpan').text(ogGridDs._total);

			var gridElement = $("#ogGrid");
			resizeGrid(gridElement);
		});

		ogGrid.bind("sort", function(e) {
			if (ogGridDs._total == undefined || ogGridDs._total == null || ogGridDs._total == 0) {
				e.preventDefault();
				return false;
			}
		});

		fnGridDefault();
	}

	// ------------------------------- Grid End -------------------------------

	// ---------------Initialize Input Mask Start
	// ------------------------------------------------
	function fnInitMaskTextBox() {

	}
	// ---------------Initialize Input Mask End
	// ------------------------------------------------

	// ---------------Initialize Option Box Start
	// ------------------------------------------------
	function fnInitOptionBox(){
		// 출고처
		fnKendoDropDownList({
			id    : 'searchUrWarehouseId',
//			url : "/admin/comn/getDropDownWarehouseList",
            url : "/admin/comn/getDropDownAuthWarehouseList",
			tagId : 'searchUrWarehouseId',
			params : {'stockOrderYn' : 'Y'},
			chkVal: '',
			style : {},
			blank : "선택",
			textField :"warehouseName",
			valueField : "warehouseId"
		}).bind("change", onSupplierCompany);

		// 공급업체
		fnKendoDropDownList({
			id    : 'searchUrSupplierId',
//			url : "/admin/comn/getDropDownSupplierList",
			url : "/admin/comn/getDropDownAuthSupplierList",
			tagId : 'searchUrSupplierId',
			textField :"supplierName",
			valueField : "supplierId",
			blank : "선택",
			autoBind : false
		}).bind("change", onPoTpList);

		// 발주유형
		fnTagMkChkBox({
        	id    : 'searchPoType',
        	tagId : 'searchPoType',
        	url : "/admin/item/po/getPoTpList",
        	params : {},
        	beforeData : [
				{"CODE":"ALL", "NAME":"전체"},
			],
			chkVal: "",
			async : false,
			style : {}
        });

		$('#searchPoType').bind("change", onCheckboxWithTotalChange);

		// 발주유형 '전체' 체크
		$('input[name=searchPoType]').each(function(idx, element) {
            $(element).prop('disabled', true);
		});

		// 발주여부
		fnKendoDropDownList({
			id : "searchPoIfYn",
			tagId : "searchPoIfYn",
			data: [
				{
					CODE: "ALL",
					NAME: "전체"
				},
				{
					CODE: "Y",
					NAME: "Y"
				},
				{
					CODE: "N",
					NAME: "N"
				}
			],
		});

		// ERP 카테고리 (대분류)
		fnKendoDropDownList({
			id    : 'searchErpCtgry',
			url : "/admin/item/po/getErpCtgryList",
			params : {"stCommonCodeMasterCode" : "ERP_PO_TP", "useYn" :"Y"},
			textField :"NAME",
			valueField : "CODE",
			blank : "전체",
			autoBind : false
		});

		// 검색 - 발주유형구분
		fnTagMkChkBox({
			id    :  'searchPoTypeGubun',
			tagId : 'searchPoTypeGubun',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "PO_TYPE", "useYn" :"Y"},
			beforeData : [
				{"CODE":"ALL", "NAME":"전체"},
			],
			chkVal: "",
			async : false,
			style : {}
		});

		$('#searchPoTypeGubun').bind("change", onCheckboxWithTotalChange);

		// 발주유형 구분 '전체' 체크
		$('input[name=searchPoTypeGubun]').each(function(idx, element) {
		    $(element).prop('checked', true);
		});

		//기준일 단일조건
		fnKendoDatePicker({
			id     : 'searchBaseDt',
			format : 'yyyy-MM-dd',
			defVal : fnGetToday(),
			max : fnGetToday()
		});

		// 표준카테고리 대분류
        fnKendoDropDownList({
            id : "searchBigCategory",
            tagId : "searchBigCategory",
            url : "/admin/comn/getDropDownCategoryStandardList",
            params : { "depth" : "1" },
            textField : "categoryName",
            valueField : "categoryId",
            blank : "전체",
            async : false
        });

        // 보관방법 리스트 조회
/*
        fnKendoDropDownList({
        	id    : 'searchStorageMethod',
        	tagId : 'searchStorageMethod',
        	url : "/admin/comn/getCodeList",
        	params : {"stCommonCodeMasterCode" : "ERP_STORAGE_TYPE", "useYn" : "Y"},
        	textField :"NAME",
        	valueField : "CODE",
        	blank : "전체",
            async : false
        });
*/
        fnTagMkChkBox({
            id: "searchStorageMethod",
            tagId: "searchStorageMethod",
            url: "/admin/comn/getCodeList",
        	params : {"stCommonCodeMasterCode" : "ERP_STORAGE_TYPE", "useYn" : "Y"},
            beforeData: [{ "CODE": "ALL", "NAME": "전체" }],
            async: false
        });
		$('#searchStorageMethod').bind("change", onCheckboxWithTotalChange);
        // 보관방법 '전체' 체크
		$('input[name=searchStorageMethod]').each(function(idx, element) {
		    $(element).prop('checked', true);
		});

        // 판매상태
        fnTagMkChkBox({
            id: "searchSaleStatus",
            tagId: "searchSaleStatus",
            url: "/admin/comn/getCodeList",
            params: { "stCommonCodeMasterCode": "SALE_STATUS", "useYn": "Y", "attr1": "PO"},
            beforeData: [{ "CODE": "ALL", "NAME": "전체" }],
            async: false
        });

        $('#searchSaleStatus').bind("change", onCheckboxWithTotalChange);

        // 상품 판매상태 '전체' 체크
		$('input[name=searchSaleStatus]').each(function(idx, element) {
		    $(element).prop('checked', true);
		});

		//상품 전시상태
		fnTagMkChkBox({
			id    : 'searchGoodsDisplayStatus',
			data  : [
				{ "CODE" : 'ALL' , "NAME":"전체" },
				{ "CODE" : 'Y' , "NAME":"전시" },
				{ "CODE" : 'N' , "NAME":"미전시" }
			],
			tagId : 'searchGoodsDisplayStatus',
			chkVal: '',
			style : {}
		});

		$('#searchGoodsDisplayStatus').bind("change", onCheckboxWithTotalChange);

		// 상품 전시상태 '전체' 체크
		$('input[name=searchGoodsDisplayStatus]').each(function(idx, element) {
		    $(element).prop('checked', true);
		});


		// 공급업체
		function onSupplierCompany(e) {
			var targetId = "searchUrSupplierId";
			var sourceId = $(e.sender.element).attr("id");

			var searchUrSupplierIdDropDown = $("#" + targetId).data("kendoDropDownList");

			if (e.sender.value() != "") {
				searchUrSupplierIdDropDown.enable(true);

//	        	var dropDownUrl = '/admin/comn/getDropDownSupplierByWarehouseList';
	        	var dropDownUrl = '/admin/comn/getDropDownAuthSupplierByWarehouseList';

	        	fnAjax({
	        		url     : dropDownUrl,
	        		method	: 'GET',
	        		params  : {"warehouseId": e.sender.value()},
	        		success :
	        			function( data ){
	        			    searchUrSupplierIdDropDown.dataSource.data(data.rows);
	        			},
	        		isAction : 'select'
	        	});
	        }
	        else {
	        	searchUrSupplierIdDropDown.dataSource.data([]);
	        	searchUrSupplierIdDropDown.enable(false);
	        }
		}

		//발주유형
		function onPoTpList(e) {
			var searchUrSupplierId = e.sender.value();

			$("input[name=searchPoType]").each(function(idx, element){
//               $(element).prop('checked', false);//전체해제
//               $(element).prop('disabled', true);
               $(element).closest('label').remove();
            });

			if (searchUrSupplierId != "") {

	        	var sendUrl = '/admin/item/po/getOnChangePoTpList';

/*
	        	fnAjax({
	        		url     : sendUrl,
	        		method	: 'GET',
	        		params  : {"searchUrSupplierId": searchUrSupplierId},
	        		success :
	        			function( data ){
	        			let size = data.rows.length;

	        			$("input[name=searchPoType]").each(function(idx, element){
	        				if (idx == 0) { // 전체 체크
	        				   $(element).prop('checked', true);
	        	               $(element).prop('disabled', false);
	        				}

	        				for(let i = 0; i < size; i++){

	        					let code = data.rows[i].CODE;

		        				if(code == element.value){
		        				   $(element).prop('checked', true);//같으면 체크
		        	               $(element).prop('disabled', false);
		        	               break;
		        				}
	        				}
	                     });
	        			},
	        		isAction : 'select'
	        	});
*/
	    		fnTagMkChkBox({
	            	id    : 'searchPoType',
	            	tagId : 'searchPoType',
	            	url : "/admin/item/po/getOnChangePoTpList",
	            	params : {"searchUrSupplierId": searchUrSupplierId},
	            	beforeData : [
	    				{"CODE":"ALL", "NAME":"전체"},
	    			],
	    			chkVal: "",
	    			async : false,
	    			style : {}
	        		, success :
	        			function( data ){
		        			$("input[name=searchPoType]").each(function(idx, element) {
								$(element).prop('checked', true);
								$(element).prop('disabled', false);
		                    });
        				}
	            });
			}

			//ERP 카테고리 (대분류) Call
			onErpCtgryList(searchUrSupplierId);
		}

		//ERP 카테고리 (대분류) 리스트
		function onErpCtgryList(searchUrSupplierId) {
			var targetId = "searchErpCtgry";
			var searchUrWarehouseId = $("#searchUrWarehouseId").val();
			var searchErpCtgryDropDown = $("#" + targetId).data("kendoDropDownList");

			searchErpCtgryDropDown.dataSource.data([]);

			if (searchUrSupplierId != "") {
				searchErpCtgryDropDown.enable(true);

	        	var dropDownUrl = '/admin/item/po/getErpCtgryList';

	        	fnAjax({
	        		url     : dropDownUrl,
	        		method	: 'GET',
	        		params  : {"searchUrWarehouseId" : searchUrWarehouseId, "searchUrSupplierId": searchUrSupplierId},
	        		success :
	        			function( data ){
	        			searchErpCtgryDropDown.dataSource.data(data.rows);
	        			},
	        		isAction : 'select'
	        	});
	        }
	        else {
	        	searchErpCtgryDropDown.dataSource.data([]);
	        	searchErpCtgryDropDown.enable(false);
	        }
		}


		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});
	}
	// ---------------Initialize Option Box End

	// ------------------------------- Common Function start
	function onCheckboxWithTotalChange(e) {   // 체크박스 change event

        // 첫번째 체크박스가 '전체' 체크박스라 가정함
        var totalCheckedValue = $("input[name=" + e.target.name + "]:eq(0)").attr('value');

        if( e.target.value == totalCheckedValue ) {  // '전체' 체크 or 체크 해제시

            if( $("input[name=" + e.target.name + "]:eq(0)").is(":checked") ) {  // '전체' 체크시

                $("input[name=" + e.target.name + "]:gt(0)").each(function(idx, element){
                    $(element).prop('checked', true);  // 나머지 모두 체크
                });

            } else { // '전체' 체크 해제시

                $("input[name=" + e.target.name + "]:gt(0)").each(function(idx, element){
                    $(element).prop('checked', false);  // 나머지 모두 체크 해제
                });

            }

        } else { // 나머지 체크 박스 중 하나를 체크 or 체크 해재시

            var allChecked = true; // 나머지 모두 체크 상태인지 flag

            $("input[name=" + e.target.name + "]:gt(0)").each(function(idx, element) {
                if( $(element).prop('checked') == false ) {
                    allChecked = false;  // 하나라도 체크 해제된 상태가 있는 경우 flag 값 false
                }
            });

            if( allChecked ) { // 나머지 모두 체크 상태인 경우
                $("input[name=" + e.target.name + "]:eq(0)").prop('checked', true);  // 나머지 모두 '전체' 체크
            } else {
                $("input[name=" + e.target.name + "]:eq(0)").prop('checked', false);  // 나머지 모두 '전체' 체크 해제
            }

        }
    }

	// -------------------------------

	/**
	 * 콜백합수
	 */
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
				$('#inputForm').bindingForm(data, 'rows', true);

				let detailData = data.rows;

				let size = detailData.length;

				if(size > 0){
				   $('#poInfoTable > tbody > tr > td > pre > pre').remove();//UI초기화

				   for(let i = 0; i < size-1; i++){
					   $('#poInfoTable > tbody > tr > td > pre').append('<pre>'+detailData[i].strPoInfo+'</pre>');//발주유형/발주 마감시간 SET
				   }

				   $('#totalPiecePoQty').text(detailData[size-1].sumPoUserQty);//낱개 발주수량 합계 SET
				   $('#totalBoxPoQty').text(detailData[size-1].sumBoxPoQty);//BOX 발주수량 합계 SET
				   $('#userSavedInfo').text(detailData[size-1].strManager);//저장시간/관리자 SET
				}

				break;
		}
	}


	// 카드 팝업 열기
	function fnOpenCard(event, data) {
		event.stopPropagation();
		const $target = $(event.target);

		if( anchoredTag ) return;

		requestCardData($target, data);
	}

	// 키보드 입력 함수
	function onKeyup(event) {
		console.log('keyup event');
		var keyCode = event.keyCode;
		var KEY_CODE_ESC = 27;

		if( keyCode === KEY_CODE_ESC ) {
			showHoverCard('', false);
			anchoredTag = null;
		}
	}

	// 카드 팝업 닫기
	function fnCloseCard(event) {
		event.stopPropagation();

		const $target = $(event.target);

		if( anchoredTag && anchoredTag === $target[0] ) {
			showHoverCard($target, false);
			anchoredTag = null;
		}
	}


    // 팝업 토글
    function showHoverCard(target = null, toggle = true) {
        const OPEN_CN = 'open';
        let _position = {};

        const $hoverCard = $('#hover-card');

        if( $hoverCard.length === 0 ) return;

        if(target && target.length > 0) {
            _position = getPosition(target);
        }


        if( toggle ) {
			$('#ng-view').on('keyup', onKeyup);
            $hoverCard.addClass(OPEN_CN);
        } else {
			$(document).off('keyup', onKeyup);
            $hoverCard.removeClass(OPEN_CN);
        }
    }

    // 포지션 계산
    function getPosition(target) {
        if( !target || !target.length ) return null;

        return {
            top: '',
            left: '',
        };
    }

    // 데이터 요청
    function addInfoCell(obj, title, value) {
        const $tr = $('<tr></tr>');
        const $th = $('<th>' + title + '</th>');
        const $td = $('<td>' + value + '</td>');

        $tr.append($th);
        $tr.append($td);

        obj.append($tr);
    }

    function requestCardData(target, data) {
        const $moreInfoBody = $('.moreInfoBody');

		$('.itemCode').text(data.ilItemCd);
		$('.itemBarCode').text(data.barcode);
		$('.masterItemName').text(data.itemNm);
		$('.supplierName').text(data.supplierName);

        $moreInfoBody.empty();

        if (data.supplierCd == 'PF') { // 풀무원 식품용 부가 정보 설정
        	addInfoCell($moreInfoBody, '표준카테고리(대분류)', data.ctgryStdNm);
			addInfoCell($moreInfoBody, 'ERP카테고리(대분류)', data.erpCtgryLv1Id);
        	addInfoCell($moreInfoBody, '상품코드', data.ilGoodsId);
        	addInfoCell($moreInfoBody, '상품 전시상태', data.dispYn != 'Y' ? '미전시' : '전시');
        	if (data.isPoPossible == 'N') {
            	addInfoCell($moreInfoBody, '입고 예정일자', '발주불가일');
        	} else {
            	addInfoCell($moreInfoBody, '입고 예정일자', data.stockScheduledDt);
        	}
			//addInfoCell($moreInfoBody, '프로모션 판매평균', data.erpEventOrderAvg);
			//addInfoCell($moreInfoBody, '일반 판매평균', data.nonErpEventOrderAvg);
        }
        else if (data.supplierCd == 'OG') { // 올가용 부가 정보 설정
			addInfoCell($moreInfoBody, '표준카테고리(대분류)', data.ctgryStdNm);
        	addInfoCell($moreInfoBody, '상품코드', data.ilGoodsId);
        	addInfoCell($moreInfoBody, '보관방법', data.storageMethodNm);
        	addInfoCell($moreInfoBody, '유통기간', data.distributionPeriod);
        	addInfoCell($moreInfoBody, '박스입수량', data.pcsPerBox);
//        	if (data.isPoPossible == 'N') {
//            	addInfoCell($moreInfoBody, '입고 예정일자', '발주불가일');
//        	} else {
//            	addInfoCell($moreInfoBody, '입고 예정일자', data.stockScheduledDt);
//        	}
        	addInfoCell($moreInfoBody, '안전재고(목표재고)', data.targetStock);
        	addInfoCell($moreInfoBody, '폐기예정수량', data.stockDiscardD0 + data.stockDiscardD1);
//        	addInfoCell($moreInfoBody, '예상잔여수량', data.expectedResidualQty);
        	addInfoCell($moreInfoBody, '일평균 출고량', data.outboundDayAvg);
        	addInfoCell($moreInfoBody, '결품 예상일', data.missedOutbound);
        	addInfoCell($moreInfoBody, '직전 3주 출고수량', data.outbound3weekTotal);
        	addInfoCell($moreInfoBody, '직전 2주 출고수량', data.outbound2weekTotal);
        	addInfoCell($moreInfoBody, '직전 1주 출고수량', data.outbound1weekTotal);
        }

        setTimeout(function() {
            showHoverCard(target, true);

			anchoredTag = target[0];
        }, 500);
    }

	$('.hover-card__close').on('click', function(e) {
		showHoverCard(null, false);

		anchoredTag = null;
	})

	$('#ng-view').on('mouseleave', function(e) {
		const $target = $(e.target);
		if( $target.closest('#pfGrid').length > 0 || $target.closest('.hover-card').length > 0 ) {
			return;
		}
		showHoverCard('', false);
		anchoredTag = null;
	});


	// 윈도우 리사이징 이벤트
	function onResize(event) {
		var gridId = urSupplierId && urSupplierId == ogSupplierCode ? "ogGrid" : "pfGrid";

		var gridElement = $("#" + gridId);

		resizeGrid(gridElement);
	};

	// 그리드 높이 변경
	function resizeGrid(gridElement) {
		// 그리드 최대 높이
		var maxGridHeight = 580;

		// 그리도 최소 높이
		var minGridHeight = 70;

		// 윈도우 내부 높이
		var windowSize = window.innerHeight;

		// 헤더 높이
		var headerHeight = $("#header").height();

		var gridId = urSupplierId && urSupplierId == ogSupplierCode ? "ogGrid" : "pfGrid";

		gridElement = gridElement || $("#" + gridId);

		var $grid = gridElement.data('kendoGrid');

		if( !$grid ) return;

		if( !$grid.dataSource.view().length ) return;

		var gridHeaderHeight = 0;

		var $gridHeader = gridElement.find('.k-grid-header');

		if( $gridHeader.length ) {
			gridHeaderHeight = gridElement.find('.k-grid-header').height();
		}

		var newContentsGridHeight = windowSize - headerHeight - gridHeaderHeight - 100;

		// 변경할 컨텐츠 높이
		var gridContentsHeight = 0;

		if( newContentsGridHeight > maxGridHeight ) {
			gridContentsHeight = maxGridHeight;
		} else if( newContentsGridHeight < minGridHeight ) {
			gridContentsHeight = minGridHeight;
		} else {
			gridContentsHeight = newContentsGridHeight;
		}

		var $gridContents = gridElement.find('.k-grid-content');

		if( $gridContents.length ) {
			$gridContents.height(gridContentsHeight);
		}
	};

	// 브라우저 해시 변경 이벤트 핸들러
	function onHashChange(event) {
		if( location.hash === "#/poList" ) return;

		// 다른 페이지로 이동 시 resize, hashChange 이벤트 제거
		$(window).off("resize", onResize);
		$(window).off("hashchange", onHashChange);
	};

	$(window).on('resize', onResize);
	$(window).on("hashchange", onHashChange);
	// ------------------------------- Common Function end
	// -------------------------------


	// ------------------------------- Html 버튼 바인딩 Start
	// -------------------------------
	/** Common Search */
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear */
	$scope.fnClear =function(){	 fnClear();	};
	/** Common ExcelDown*/
	$scope.fnExcelDown = function(){ fnExcelDown();};
	/** Common Close */
	$scope.fnClose = function( ){  fnClose();};

	//
	$scope.fnOpenCard = function(event, data) { fnOpenCard(event, data); }

	//
	$scope.fnCloseCard = function(event) { fnCloseCard(event); }
	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END
