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
	// 전역 변수
	// 체크된 그리드 행의 아이디 값을 담을 배열
	let selectedPfRow = [];
	let selectedOgRow = [];

	fnInitialize();	// Initialize Page Call ---------------------------------

	// Initialize Page
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'poList',
			callback : fnUI
		});

	}

	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	// Initialize Button ---------------------------------

		fnInitGrid();	// Initialize Grid ------------------------------------

		fnInitMaskTextBox();// Initialize Input Mask

		fnInitOptionBox();// Initialize Option Box

		fnInitEvents();
	}

	// --------------------------------- Button
	// Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnSave, #fnClear, #fnExcelDown').kendoButton();
	}
	// 검색
	function fnSearch(){
		// 발주조회시 조회불가 조건 추가
		if(fnSearchCondition()){
			urSupplierId  = $("#searchUrSupplierId").val();
			urWarehouseId = $("#searchUrWarehouseId").val();

			// pfGrid, ogGrid 데이터 요청
			const requestGridData = function (callback) {
				fbLoading(true);
				const formData = $('#searchForm').formSerialize(true);
				const filters = fnSearchData(formData);

				const option = {
					type     : 'POST',
					url      : '/admin/item/po/getPoList',
					params: {
						filterLength : filters.length,
						filter: {
							filters: filters,
						},
					},
					success: function(data, resultCode) {
						if(typeof callback === 'function') {
							callback(data.rows);
						}

						fbLoading(false);
					},
					error: function() {
						fbLoading(false);
					},
				};

				fnAjax(option);
			}

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
				//   pfGridDs.query( query );

				// 검색 시 체크된 행 초기화
				selectedPfRow = [];

				requestGridData(function(data) {
					pfGridDs.data(data);
				});
			} else if(urSupplierId == ogSupplierCode && urWarehouseId != '') {	//올가홀푸드
				$("#ogGrid").show();
				$("#pfGrid").hide();
				fnGetPoInfoList();	// 발주정보 조회
				//   ogGridDs.query( query );

				selectedOgRow = [];

				requestGridData(function(data) {
					ogGridDs.data(data);
				});
			} else {
				$("#pfGrid").show();
				$("#ogGrid").hide();
				$('#poInfoTable > tbody > tr > td > pre > pre').remove();	// UI초기화
				$('#totalPiecePoQty').text("0");	// 낱개 발주수량 합계 SET
				$('#totalBoxPoQty').text("0");	// BOX 발주수량 합계 SET
				$('#userSavedInfo').text("");		// 저장시간/관리자 SET

				selectedOgRow = [];
				selectedPfRow = [];

				pfGridDs.data([]);
			}
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

		// 외부몰 판매상태 '판매중' 체크
		$('input[name=goodsOutMallSaleStatus]').each(function(idx, element) {
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

		// ERP카테고리 '전체' 체크
		$('input[name=searchErpCtgryGubun]').each(function(idx, element) {
			$(element).prop('checked', true);
		});

		// ERP카테고리 '전체' disabled 처리
		$('input[name=searchErpCtgry]').each(function(idx, element) {
			$(element).prop('disabled', true);
		});
	}

	// 발주마감 시간이 지났는지 비교하는 함수
	function fnExpiredPoDeadline(baseDt, strPoExpireTime) {
		var currentDate = new Date();
		var deadlineDate = new Date();
		var splitBaseDt = baseDt.split('-');
		var splitPoTpDeadline = strPoExpireTime.split(':');

		deadlineDate.setYear(parseInt(splitBaseDt[0], 10));
		deadlineDate.setMonth(parseInt(splitBaseDt[1], 10) - 1);
		deadlineDate.setDate(parseInt(splitBaseDt[2], 10));
		deadlineDate.setHours(parseInt(splitPoTpDeadline[0], 10), parseInt(splitPoTpDeadline[1], 10), 0);
		if (currentDate >= deadlineDate) {
			return true;
		} else {
			return false;
		}
	}

	// 00:00부터 00:45까지 조회불가 처리.
	function fnSearchCondition(){
		const currDate = new Date().oFormat("HH:mm:ss");

		if(currDate >= '00:00:00' && currDate < '00:45:00' ){
			fnKendoMessage({ message : "발주 통계작업 완료 이후 발주리스트 이용이 가능합니다. <br/>* 이용 불가시간 00:00~00:45" });
			return false;
		} else {
			return true;
		}
	}

	// 저장
	function fnSave(){
		let selectRows = '';
		let grid = '';
		let gridDs = '';

		if(urSupplierId == pfSupplierCode){
			selectRows = $("#pfGrid").find("input[name=rowPfCheckbox]:checked").closest("tr");
			grid = pfGrid;
			gridDs = pfGridDs;
		}else if(urSupplierId == ogSupplierCode){
			selectRows = $("#ogGrid").find("input[name=rowOgCheckbox]:checked").closest("tr");
			grid = ogGrid;
			gridDs = ogGridDs;
		}else{
			fnKendoMessage({ message : "풀무원 식품과 올가만 저장이 가능합니다." });
			return false;
		}

		let rowCnt = selectRows.length;
		if( rowCnt == 0 ){
			fnKendoMessage({ message : "선택된 목록이 없습니다." });
			return false;
		}

		var includeExpiredData = false;
		for (let i = 0; i < rowCnt; i++){
			let dataItem = grid.dataItem($(selectRows[i]));

			if (fnExpiredPoDeadline(dataItem.baseDt, dataItem.poTpDeadline)) { // 발주마감 시간이 지났는지 확인
				includeExpiredData = true;
				break;
			}
		}

		if (includeExpiredData) {
			fnKendoMessage({ message : "발주마감시간이 지난 항목이 포함되어 있습니다." });

			for (let i = 0; i < gridDs._total; i++) {
				let dataItem = gridDs.data()[i]

				if (fnExpiredPoDeadline(dataItem.baseDt, dataItem.poTpDeadline)) { // 발주마감 시간이 지났는지 확인하여 해당 row disabled 처리
					var $checkbox = '';
					var $piecePoQt = '';
					var $memo = '';

					if (urSupplierId == pfSupplierCode) {
						$checkbox = $("input[name=rowPfCheckbox]").eq(i);
						$piecePoQt = $("input[name=rowPfPiecePoQty]").eq(i);
						$memo = $("input[name=rowPfMemo]").eq(i);
					} else if (urSupplierId == ogSupplierCode) {
						$checkbox = $("input[name=rowOgCheckbox]").eq(i);
						$piecePoQt = $("input[name=rowOgPiecePoQty]").eq(i);
						$memo = $("input[name=rowOgMemo]").eq(i);
					}
					// check box disable
					$checkbox.prop("checked", false).trigger("change");
					$checkbox.prop("disabled", true);

					// input box disable
					$piecePoQt.prop("disabled", true);
					$memo.prop("disabled", true);
				}
			}

			return false;
		}

		let confirmMessage = "일괄 변경하시겠습니까?";
		let params = {};
		params.poList = [];

		fnKendoMessage({
			type    : "confirm",
			message : confirmMessage,
			ok      : function(){

				for(let i = 0; i < rowCnt; i++){
					let dataItem = '';

					if(urSupplierId == pfSupplierCode){//풀무원식품
						dataItem = pfGrid.dataItem($(selectRows[i]));
					}else{//올가
						dataItem = ogGrid.dataItem($(selectRows[i]));
					}

					if(dataItem.memo == undefined || dataItem.memo == ''){
						dataItem.memo = ' ';
					}
					params.poList[i] = dataItem.ilPoId+"-"+dataItem.piecePoQty+"-"+dataItem.memo;
				}

				fnAjax({
					url     : "/admin/item/po/putItemPo",
					params  : params,
					contentType : "application/json",
					success : function( data ){
						fnKendoMessage({
							message : "저장이 완료되었습니다."
							, ok : function(e) {
								fnSearch()
							}
						});
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
	}

	//엑셀다운로드
	function fnExcelDown(){
		var data = $('#searchForm').formSerialize(true);
		fnExcelDownload('/admin/item/po/getPoListExportExcel', data);
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
		// pfGridDs = fnGetPagingDataSource({
		// 	url      : '/admin/item/po/getPoList'
		// 	, model_id     : 'ilPoId'  //model_id 는 pk역할을 해서 수정모드에서 활성화가 안된다.
		// });

		pfGridDs = new kendo.data.DataSource({
			data: [],
		});

		pfGridOpt = {
			dataSource: pfGridDs
			, pageable  : false
			, navigatable : true
			, scrollable : true
			, editable : true
			, sortable : {
				mode : "multiple",
				allowUnsort : true,
				showIndexes : true,
				initialDirection : "desc"
			}
			// , height : 755
			, columns : [
				{ title : '품목/상품정보'
					, headerAttributes :{
						"class" : 'row_line'
					}
					, columns: [
						{ field : "chk"
							, headerTemplate : '<input type="checkbox" id="checkBoxPfAll" />'
							, template: function(dataItem) {
								var html;
								if (dataItem.isPoExpired == 'Y' || dataItem.isPoPossible == 'N') { // 발주불가 또는 발주마감 시간이 지났으면 disabled 처리한다.
									html = '<input type="checkbox" name="rowPfCheckbox" value="' + dataItem.poTpDeadline + '" class="k-checkbox" disabled />';
								} else {
									html = '<input type="checkbox" name="rowPfCheckbox" value="' + dataItem.poTpDeadline + '" class="k-checkbox" />';
								}
								return html;
							}
							, width : "32px", attributes : {style : "text-align:center;"}, sortable:false, editable : function() { return false; }
						}
						, { title:'No.', width:'5%', attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>", sortable:false}
						, { field:'ilPoId', title : '발주SEQ', width:'80px', attributes:{ style:'text-align:center' }, sortable:false, hidden:true, editable : function() { return false; }}
						, { field:'poTpNm', headerTemplate : '<span title="발주유형">발주유형<br>-</span>', width:'46px', attributes:{ style:'text-align:center' }
							, template: function(dataItem) {
								//return dataItem.poTpNm + '/<br/>' + dataItem.poTpTemplateNm;
								return dataItem.poTpTemplateNm;
							}
							, editable : function() { return false; }
						}
						, { field:'ilItemCd', headerTemplate : '<span title="품목코드/품목바코드">품목코드/<br>품목바코드</span>', width:'110px', attributes:{ style:'text-align:center' }, sortable:false
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
						, { field:'itemNm', headerTemplate : '<span title="마스터품목명">마스터품목명</span>', width:'150px', attributes:{ style:'text-align:center' }, sortable:false
							, template: function(dataItem) {
								return "<a class='itemDetlInfo' kind='itemDetlInfo' style='color:blue; cursor:pointer' onclick='$scope.fnOpenCard(event, " + JSON.stringify(dataItem) + ");'>" + kendo.htmlEncode(dataItem.itemNm) + "</a>";
							}
							, editable : function() { return false; }
						}
						, { field:'storageMethodNm', headerTemplate : '<span title="보관방법">보관방법</span>', width:'6%', attributes:{ style:'text-align:center' }, sortable:false, editable : function() { return false; }}
						, { field:'distributionPeriod', headerTemplate : '<span title="유통기간">유통기간<br>-</span>', width:'7%', attributes:{ style:'text-align:center' }, editable : function() { return false; }}
						, { field:'pcsPerBox', headerTemplate : '<span title="박스 입수량">박스<br>입수량</span>', width:'5%', attributes:{ style:'text-align:center' }, sortable:false, editable : function() { return false; }}
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
						, { field:'saleStatusNm', headerTemplate : '<span title="상품 판매상태">상품<br>판매상태<br>-</span>', width:'65px', attributes:{style:'text-align:center' }
							, editable : function() { return false; }
						}
						, { field:'goodsOutmallSaleStatNm', headerTemplate : '<span title="외부몰 판매상태">외부몰<br>판매상태<br>-</span>', width:'65px', attributes:{"class" : 'row_line', style:'text-align:center' }
							, headerAttributes :{
								"class" : 'row_line'
							}
							, editable : function() { return false; }
						}
//						, { field:'ilGoodsId' ,title : '상품코드', width:'60px', attributes:{ style:'text-align:center' }, sortable:false, editable : function() { return false; }}
//						, { field:'erpCtgryLv1Id', title : 'ERP<br>카테고리', width:'80px', attributes:{ style:'text-align:center' }, editable : function (){ return false; }}
					]
				}
				, { title : '발주정보'
					, headerAttributes :{
						"class" : 'row_line'
					}
					, columns: [
						{ field:'targetStock', headerTemplate : '<span title="안전재고(목표재고)">안전재고<br>(목표재고)</span>', width:'5%', attributes:{ style:'text-align:center' }, sortable:false, editable : function() { return false; }}
						, { field:'recommendPoQty', headerTemplate : '<span title="권고수량">권고수량<br>-</span>', width:'56px', attributes:{ style:'text-align:center' }, editable : function() { return false; }}
						, { field:'piecePoQty'        ,headerTemplate : '<span title="낱개 발주수량">낱개<br>발주수량<br>-</span>' , width:'70px'	,attributes:{ style:'text-align:center'},
							editor: function(container, options) {
								var input = $("<input class='comm-input' type='number' style='width: 100%; text-align:center;' min='0' max='99999' maxlength='5' />");
								var rowData = options.model;

								input.on('focus', function(e) { // 낱개 발주수량 변경 시 event
									if (rowData.isPoExpired == 'Y' || rowData.isPoPossible == 'N' || fnExpiredPoDeadline(rowData.baseDt, rowData.poTpDeadline)) {
										$(e.target).prop("disabled", true);
									}
								});

								input.on('keyup', function(e) { // 숫자만입력
									this.value=this.value.replace(/[^(0-9)^(,)]/g, '');

									if (this.value.length > this.maxLength) { // 최대 입력 자리수 제한
										this.value = this.value.slice(0, this.maxLength);
									}
								});

								input.on('keydown', function(e) {
									const TAB_KEY = kendo.keys.TAB;
									const $target = e.target;
									if (e.keyCode === TAB_KEY && $($($target).closest('.k-edit-cell'))[0]) { // TAB 키를 누를 경우 다음 낱개발주수량 항목으로 이동
										e.preventDefault();
										//$($target).trigger('change');

										// 현재 행 개수
										var currentNumberOfItems = pfGrid.dataSource.view().length;

										// 현재 행
										var $tr = $($target).closest('tr');

										// 현재 행 번호
										var row = $tr.index();

										// 현재 열 번호
										var col = pfGrid.cellIndex($($target).closest('td'));

										// 값 세팅
										var dataItem = pfGrid.dataItem($tr);
										var field = pfGrid.thead.find('tr:eq(1)').find('th:eq(' + col +')').data('field');
										var value = $($target).val();
										dataItem.set(field, value);

										// 전체 열 개수
										var columnLength = pfGrid.thead.find('tr:eq(1)').find('th').length;

										if (row >= 0 && row < currentNumberOfItems && col >= 0 && col < columnLength) {
											var direction = null;
											var nextCellRow = row;
											var nextCellCol = col;

											if (e.shiftKey) {
												direction = "up";

												if (nextCellRow - 1 < 0) {
													nextCellRow = currentNumberOfItems - 1;
													// nextCellCol--;
												} else {
													nextCellRow--;
												}
											} else {
												direction = "down";

												if (nextCellRow + 1 >= currentNumberOfItems) {
													nextCellRow = 0;
													// nextCellCol++;
												} else {
													nextCellRow++;
												}
											}

											var $tr = pfGrid.tbody.find("tr:eq(" + nextCellRow + ")");

											if( !$tr.length ) return;

											var $rowPfCheckbox = $tr.find("input[name='rowPfCheckbox']");
											var isDisabled = $rowPfCheckbox.is(":disabled");

											if( !isDisabled ) {
												setTimeout(function() {
													pfGrid.editCell(pfGrid.tbody.find("tr:eq(" + nextCellRow + ") td:eq(" + nextCellCol + ")"));
												});
											} else {
												// 발주 가능한 상품을 찾을때까지 반복
												while(true) {
													if( direction === "down" ) {
														if( nextCellRow + 1 >= currentNumberOfItems ) {
															nextCellRow = 0;
														} else {
															nextCellRow += 1;
														}

													} else {
														if( nextCellRow - 1 < 0 ) {
															nextCellRow = currentNumberOfItems - 1;
														} else {
															nextCellRow -= 1;
														}
													}

													if( nextCellRow === row ) {
														return;
													}

													var $nextTr = pfGrid.tbody.find("tr:eq(" + nextCellRow + ")");
													if( !$nextTr.length ) return;

													var $nextCheckbox = $nextTr.find("input[name='rowPfCheckbox']");
													var isCheckboxDisabled = $nextCheckbox.is(":disabled");

													if( !isCheckboxDisabled ) {
														break;
													}
												}

												setTimeout(function() {
													pfGrid.editCell(pfGrid.tbody.find("tr:eq(" + nextCellRow + ") td:eq(" + col + ")"));
												});
											}


											// if (nextCellCol >= columnLength || nextCellCol < 0) {
											//     return;
											// }
											setTimeout(function() {
												pfGrid.editCell(pfGrid.tbody.find("tr:eq(" + nextCellRow + ") td:eq(" + nextCellCol + ")"));
											});
										}
									}
								});

								input.on('change', function(e) { // 낱개 발주수량 변경 시 event
									if ($(e.target).val() == '') { // 숫자이외의 값이 입력되었을 경우 0으로 셋팅
										$(e.target).val(0)
									}
									// 낱개 수량 변경시 체크박스 설정
									var $tr = container.closest('tr');
									var $checkbox = $tr.find("input[name=rowPfCheckbox]");
									$checkbox.prop("checked", true).trigger("change");

									// // 전체 checked 확인
									// var $checkboxes = $("input[name=rowPfCheckbox]");
									// var $checkedBox = $("input[name=rowPfCheckbox]:checked");
									// if( $checkboxes.not(":disabled").length == $checkedBox.length ) {
									// 	$("#checkBoxPfAll").prop("checked", true);
									// }

									// BOX 발주수량 변경
									var piecePoQty = parseInt($(e.target).val())
									rowData.set('boxPoQty', (piecePoQty/rowData.pcsPerBox).toFixed(1));
								});

								input.attr("name", options.field);
								input.appendTo(container);
							}
							, template: function(dataItem) {
								var html;
								if (dataItem.isPoExpired == 'Y' || dataItem.isPoPossible == 'N') { // 발주불가 또는 발주마감 시간이 지났으면 disabled 처리한다.
									html = "<input type='text' name='rowPfPiecePoQty' class='comm-input' value='" + dataItem.piecePoQty + "' style='width: 100%; text-align:center;' disabled>";
								} else {
									html = "<input type='text' name='rowPfPiecePoQty' class='comm-input' value='" + dataItem.piecePoQty + "' style='width: 100%; text-align:center;' readonly>";
								}
								return html;
							}
						}
			             , { field:'boxPoQty'          ,headerTemplate : '<span title="BOX 발주수량">BOX<br>발주수량</span>'	   , width:'5%'	,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function() { return false; }
						}
			             , { field:'eventPoQty'		  ,headerTemplate : '<span title="행사 발주수량">행사<br>발주수량<br>-</span>'	   , width:'5%'	,attributes:{ "class" : 'row_line', style:'text-align:center' }
							, headerAttributes :{
								"class" : 'row_line'
							}
							, editable : function() { return false; }
						}
					]
				}
				,{ title : '재고정보'
					, headerAttributes :{
						"class" : 'row_line'
					}
					, columns: [
						{ field:'stockClosed'	    ,headerTemplate : '<span title="전일 마감재고">전일<br>마감재고<br>-</span>'   , width:'56px'	,attributes:{ style:'text-align:center' }
							, editable : function() { return false; }
						}
						, { field:'stockDiscardD0'		,headerTemplate : '<span title="폐기 예정수량">폐기<br>예정수량</span>'   , width:'5%'	,attributes:{ style:'text-align:center' }, sortable:false
							, template: function(dataItem) {
								return dataItem.stockDiscardD0 + dataItem.stockDiscardD1;
							}
							, editable : function() { return false; }
						}
						, { field:'stockConfirmed'    ,headerTemplate : '<span title="입고확정 (입고예정)">입고<br>확정<br>(입고<br>예정)<br>-</span>', width:'5%'	    ,attributes:{ style:'text-align:center' }
							, template: function(dataItem) {
								return dataItem.stockConfirmed + '<br>(' + dataItem.stockScheduledD0 + ')';
							}
							, editable : function() { return false; }
						}
						, { field:'stockScheduledD1More',headerTemplate : '<span title="입고 대기수량">입고<br>대기<br>수량<br>-</span>'   , width:'5%'	,attributes:{ style:'text-align:center' }
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
						, { field:'expectedResidualQty' ,headerTemplate : '<span title="예상 잔여수량">예상<br>잔여수량<br>-</span>'   , width:'56px'	,attributes:{ "class" : 'row_line', style:'text-align:center' }
							, headerAttributes :{
								"class" : 'row_line'
							}
							, editable : function() { return false; }
						}
					]
				}
				,{ title : '주문/출고정보'
					,columns: [
						{field:'outbound0'   	  ,title : 'D-0'             , width:'5%'	,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function() { return false; }
						}
					 	,{ field:'outbound1'		  ,title : 'D+1'             , width:'5%'	,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function() { return false; }
						}
						,{ field:'outbound2'	      ,title : 'D+2'             , width:'5%'	,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function() { return false; }
						}
						,{ field:'outbound3More'     ,title : 'D+3<br>이상'      , width:'5%' ,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function() { return false; }
						}
						, { field:'outboundDayAvg'    ,headerTemplate : '<span title="일평균 출고량">일평균<br>출고량<br>-</span>'      , width:'5%' ,attributes:{ style:'text-align:center' }
							, editable : function (){ return false; }
						}
						, { field:'missedOutbound'	  ,headerTemplate : '<span title="결품 예상일">결품<br>예상일</span>'         , width:'60px'	,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function() { return false; }
						}
						, { field:'memo'		      ,headerTemplate : '<span title="발주메모">발주메모</span>'          , width:'120px'	,attributes:{ style:'text-align:center' }, sortable:false
							, editor: function(container, options) {
								var input = $("<input class='comm-input' type='text' style='width: 100%; text-align:center;'/>");
								var rowData = options.model;

								input.on('focus', function(e) { // 낱개 발주수량 변경 시 event
									if (rowData.isPoExpired == 'Y' || rowData.isPoPossible == 'N' || fnExpiredPoDeadline(rowData.baseDt, rowData.poTpDeadline)) {
										$(e.target).prop("disabled", true);
									}
								});

								input.attr("name", options.field);
								input.appendTo(container);
							}
							, template: function(dataItem) {
								var html;
								if (dataItem.isPoExpired == 'Y' || dataItem.isPoPossible == 'N') { // 발주불가 또는 발주마감 시간이 지났으면 disabled 처리한다.
									html = "<input type='text' name='rowPfMemo' class='comm-input' value='" + dataItem.memo + "' style='width: 100%; text-align:center;' disabled>";
								} else {
									html = "<input type='text' name='rowPfMemo' class='comm-input' value='" + dataItem.memo + "' style='width: 100%; text-align:center;' readonly>";
								}
								return html;
							}
						}
						, { field:'outbound3weekTotal'  ,headerTemplate : '<span title="직전 3주 출고수량">직전 3주<br>출고수량</span>' , width:'5%'	,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function() { return false; }
						}
						, { field:'outbound2weekTotal'  ,headerTemplate : '<span title="직전 2주 출고수량">직전 2주<br>출고수량</span>' , width:'5%'	,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function() { return false; }
						}
						, { field:'outbound1weekTotal'  ,headerTemplate : '<span title="직전 1주 출고수량">직전 1주<br>출고수량</span>' , width:'5%'	,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function() { return false; }
						}
						, { field:'manager'		  ,headerTemplate : '<span title="관리자">관리자</span>'           , width:'6%'	,attributes:{ "class" : 'admin_name', style:'text-align:center' }, sortable:false
							, template: function(dataItem) {
								var html = "";
								if (dataItem.userSavedDt != undefined && dataItem.userSavedDt != null) {
										html = '<span>' + dataItem.userNm + ' / ' + dataItem.loginId + '<br/>' + dataItem.userSavedDt + '</span>';
								}
								return html;
							}
							, editable : function() { return false; }
						}
					]
				}
			]
		};
		pfGrid = $('#pfGrid').initializeKendoGrid( pfGridOpt ).cKendoGrid();
		$('#pfGrid').addClass('order_table');
		pfGrid.bind("dataBound", function(e) {
			const self = this;

//			let rowNum = pfGridDs._total;
			$("#pfGrid tbody > tr .row-number").each(function(index){
//				$(this).html(rowNum);
//				rowNum--;
				$(this).html(index+1);
			});

			//total count
			$('#countTotalSpan').text(pfGridDs._total);

			// 체크박스 다시 체크
			if(selectedPfRow.length) {
				const $rows = self.element.find('.k-grid-content tr');

				$rows.each(function() {
					const _self = $(this);
					const rowData = self.dataItem(_self);

					const ilPoId = rowData ? rowData.ilPoId : null;

					// 해당 행이 기존에 체크되어 있을 경우
					if(selectedPfRow.includes(ilPoId)) {
						_self.find('input[name="rowPfCheckbox"]').prop("checked", true);
					}
				});
			}

			resizeGrid(self.element);
		});

		pfGrid.bind("sort", handleSort);

		// 그리드 전체선택 클릭
		$("#checkBoxPfAll").on("change", function(index){
			selectedPfRow = [];

			if( $("#checkBoxPfAll").prop("checked") ){
				$("input[name=rowPfCheckbox]").not(":disabled").prop("checked", true);

				const _data = pfGridDs.data().slice();

				for(let i = 0, maxCnt = _data.length; i < maxCnt; i++) {
					const isDisabled = checkIsDisabled(_data[i]);

					if(!isDisabled) {
						selectedPfRow.push(_data[i].ilPoId);
					}
				}
			}else{
				$("input[name=rowPfCheckbox]").prop("checked", false);
			}
		});

		// 그리드 체크박스 클릭
		pfGrid.element.on("change", "[name=rowPfCheckbox]" , function(e){
			const $row = $(e.target).closest("tr");
			const dataItem = pfGrid.dataItem($row);
			const ilPoId = dataItem && dataItem.ilPoId;

			if( e.target.checked ){
				if( $("[name=rowPfCheckbox]").not(":disabled").length == $("[name=rowPfCheckbox]:checked").length ){
					$("#checkBoxPfAll").prop("checked", true);
				}

				selectedPfRow.push(ilPoId);
			}else{
				$("#checkBoxPfAll").prop("checked", false);

				const _index = selectedPfRow.findIndex(function(item) {
					return item === ilPoId;
				});

				if(_index > -1) {
					selectedPfRow.splice(_index, 1);
				}
			}
		});
		// !SECTION
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		// ogGridDs = fnGetPagingDataSource({
		// 	url      : '/admin/item/po/getPoList'
		// 	, model_id     : 'ilPoId'  //model_id 는 pk역할을 해서 수정모드에서 활성화가 안된다.
		// });

		ogGridDs = new kendo.data.DataSource({
			data: [],
		});

		ogGridOpt = {
			dataSource: ogGridDs
			,  	pageable  : false
			,	navigatable : true
			,   scrollable : true
			,   sortable : {
				mode : "multiple",
				allowUnsort : true,
				showIndexes : true,
				initialDirection : "desc"
			}
			,   editable : true
			// ,   height : 755
			,	columns   : [
				{ title : '품목/상품정보'
					 , headerAttributes :{
						 "class" : 'row_line'
					 }
					,columns: [
						{ field : "chk", headerTemplate : '<input type="checkbox" id="checkOgBoxAll" />'
							, template: function(dataItem) {
								var html;
								if (dataItem.isPoExpired == 'Y' || dataItem.isPoPossible == 'N') { // 발주불가 또는 발주마감 시간이 지났으면 disabled 처리한다.
									html = '<input type="checkbox" name="rowOgCheckbox" value="' + dataItem.poTpDeadline + '" class="k-checkbox" disabled />';
								} else {
									html = '<input type="checkbox" name="rowOgCheckbox" value="' + dataItem.poTpDeadline + '" class="k-checkbox" />';
								}
								return html;
							}
							, width : "32px", attributes : {style : "text-align:center;"}, sortable:false
							, editable : function () { return false; }
						}
						, { title:'No.'		          ,width:'4%'	   , attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>", sortable:false }
						, { field:'ilPoId'	   ,title : '발주SEQ'	   , width:'80px'	 ,attributes:{ style:'text-align:center' }, sortable:false, hidden:true
							, editable : function (){ return false; }
						}
						, { field:'poTpNm'		      ,headerTemplate : '<span title="발주유형">발주유형<br>-</span>'			     , width:'46px'	 ,attributes:{ style:'text-align:center' }
							, template: function(dataItem) {
								var poTpNm = dataItem.poTpTemplateNm;
								var result = poTpNm.replace('품목별상이', 'R2발주');			// 품목별상이 -> R2발주로 변경

								return result;
							}
							, editable : function (){ return false; }
						}
						, { field:'erpCtgryLv1Id'      ,headerTemplate : '<span title="ERP카테고리(대분류)">ERP카테고리<br>(대분류)</span>' 	, width:'8%'	 ,attributes:{ style:'text-align:center' }
							, editable : function (){ return false; }
						}
						, { field:'erpEvent'		  ,headerTemplate : '<span title="행사정보 (올가전용)">행사정보<br>(올가전용)</span>'    , width:'86px'	 ,attributes:{ style:'text-align:center' }
							, editable : function (){ return false; }
						}
						, { field:'ilItemCd'	  ,headerTemplate : '<span title="품목코드/품목바코드">품목코드/<br>품목바코드</span>', width:'110px' ,attributes:{ style:'text-align:center' }, sortable:false
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
						, { field:'itemNm'		      ,headerTemplate : '<span title="마스터품목명">마스터품목명</span>'			     , width:'150px' ,attributes:{ style:'text-align:center' }, sortable:false
							, template: function(dataItem) {
								return "<a class='itemDetlInfo' kind='itemDetlInfo' style='color:blue; cursor:pointer' onclick='$scope.fnOpenCard(event, " + JSON.stringify(dataItem) + ");'>" + kendo.htmlEncode(dataItem.itemNm) + "</a>";
							}
							, editable : function (){ return false; }
						}
						, { field:'dispYn'	         ,headerTemplate : '<span title="상품 전시상태">상품<br>전시상태</span>'	         , width:'30px' ,attributes:{ style:'text-align:center' }, sortable:false
							, template: function(dataItem) {
								var html = "전시";
								if(dataItem.dispYn != "Y"){
									html = "미전시";
								}
								return html;
							}
							, editable : function (){ return false; }
						}
						, { field:'saleStatusNm', headerTemplate : '<span title="상품 판매상태">상품<br>판매상태<br>-</span>', width:'70px', attributes:{ style:'text-align:center' }
							, editable : function (){ return false; }
						}
						, { field:'goodsOutmallSaleStatNm', headerTemplate : '<span title="외부몰 판매상태">외부몰<br>판매상태<br>-</span>', width:'70px', attributes:{ "class" : 'row_line', style:'text-align:center' }
							, headerAttributes :{
								"class" : 'row_line'
							}
							, editable : function (){ return false; }
						}
						// ,{ field:'ctgryStdNm'		  ,title : '표준카테고리<br>(대분류)'      , width:'80px'	 ,attributes:{ style:'text-align:center' }, sortable:false
						//   	, editable : function (){ return false; }
						// }
					]
				}
				,{ title : '발주정보'
					, headerAttributes :{
						"class" : 'row_line'
					}
					,columns: [
						{ field:'poProRea'		  ,headerTemplate : '<span title="off발주상태 (올가전용)">off발주상태<br>(올가전용)<br>-</span>', width:'56px'	 ,attributes:{ style:'text-align:center' }
							, editable : function (){ return false; }
						}
						//, { field:'targetStock'	,title : '안전재고<br>(목표재고)'   , width:'60px'	,attributes:{ style:'text-align:center' }, sortable:false
						//	, editable : function (){ return false; }
						//}
						, { field:'recommendPoQty'	 ,headerTemplate : '<span title="권고수량">권고수량<br>-</span>'		     , width:'54px'	,attributes:{ style:'text-align:center' }
							, editable : function (){ return false; }
						}
			            , { field:'piecePoQty'       ,headerTemplate : '<span title="낱개 발주수량">낱개<br>발주수량<br>-</span>'   , width:'66px'	,attributes:{ style:'text-align:center' }
							, editor: function(container, options) {
								var input = $("<input class='comm-input' type='number' style='width: 100%; text-align:center;' min='0' max='99999' maxlength='5' />");
								var rowData = options.model;

								input.on('focus', function(e) { // 낱개 발주수량 변경 시 event
									if (rowData.isPoExpired == 'Y' || rowData.isPoPossible == 'N' || fnExpiredPoDeadline(rowData.baseDt, rowData.poTpDeadline)) {
										$(e.target).prop("disabled", true);
									}
								});

								input.on('keyup', function(e) { // 숫자만입력
									this.value=this.value.replace(/[^(0-9)^(,)]/g, '');

									if (this.value.length > this.maxLength) { // 최대 입력 자리수 제한
										this.value = this.value.slice(0, this.maxLength);
									}
								});

								input.on('keydown', function(e) {
									const TAB_KEY = kendo.keys.TAB;
									const $target = e.target;
									if (e.keyCode === TAB_KEY && $($($target).closest('.k-edit-cell'))[0]) { // TAB 키를 누를 경우 다음 낱개발주수량 항목으로 이동
										e.preventDefault();
										//$($target).trigger('change');
										var currentNumberOfItems = ogGrid.dataSource.view().length;
										var $tr = $($target).closest('tr');

										// 현재 행 번호
										var row = $tr.index();

										// 현재 열 번호
										var col = ogGrid.cellIndex($($target).closest('td'));

										var dataItem = ogGrid.dataItem($tr);
										var field = ogGrid.thead.find('tr:eq(1)').find('th:eq(' + col +')').data('field');
										var value = $($target).val();
										dataItem.set(field, value);

										var columnLength = ogGrid.thead.find('tr:eq(1)').find('th').length;

										if (row >= 0 && row < currentNumberOfItems && col >= 0 && col < columnLength) {
											var direction = null;
											var nextCellRow = row;
											var nextCellCol = col;

											if (e.shiftKey) {
												direction = "up";

												if (nextCellRow - 1 < 0) {
													nextCellRow = currentNumberOfItems - 1;
													// nextCellCol--;
												} else {
													nextCellRow--;
												}
											} else {
												direction = "down";

												if (nextCellRow + 1 >= currentNumberOfItems) {
													nextCellRow = 0;
													// nextCellCol++;
												} else {
													nextCellRow++;
												}
											}

											var $tr = ogGrid.tbody.find("tr:eq(" + nextCellRow + ")");

											if( !$tr.length ) return;

											var $rowOgCheckbox = $tr.find("input[name='rowOgCheckbox']");
											var isDisabled = $rowOgCheckbox.is(":disabled");

											if( !isDisabled ) {
												setTimeout(function() {
													ogGrid.editCell(ogGrid.tbody.find("tr:eq(" + nextCellRow + ") td:eq(" + nextCellCol + ")"));
												});
											} else {
												// 발주 가능한 상품을 찾을때까지 반복
												while(true) {
													if( direction === "down" ) {
														if( nextCellRow + 1 >= currentNumberOfItems ) {
															nextCellRow = 0;
														} else {
															nextCellRow += 1;
														}

													} else {
														if( nextCellRow - 1 < 0 ) {
															nextCellRow = currentNumberOfItems - 1;
														} else {
															nextCellRow -= 1;
														}
													}

													if( nextCellRow === row ) {
														return;
													}

													var $nextTr = ogGrid.tbody.find("tr:eq(" + nextCellRow + ")");
													if( !$nextTr.length ) return;

													var $nextCheckbox = $nextTr.find("input[name='rowOgCheckbox']");
													var isCheckboxDisabled = $nextCheckbox.is(":disabled");

													if( !isCheckboxDisabled ) {
														break;
													}
												}

												setTimeout(function() {
													ogGrid.editCell(ogGrid.tbody.find("tr:eq(" + nextCellRow + ") td:eq(" + col + ")"));
												});
											}

											// if (nextCellCol >= columnLength || nextCellCol < 0) {
											//     return;
											// }
											// setTimeout(function() {
											// 	ogGrid.editCell(ogGrid.tbody.find("tr:eq(" + nextCellRow + ") td:eq(" + nextCellCol + ")"));
											// });
										}
									}
								});

								input.on('change', function(e) { // 낱개 발주수량 변경 시 event
									if ($(e.target).val() == '') { // 숫자이외의 값이 입력되었을 경우 0으로 셋팅
										$(e.target).val(0)
									}

									// 낱개 수량 변경시 체크박스 설정
									var $tr = container.closest('tr');
									var $checkbox = $tr.find("input[name=rowOgCheckbox]");
									$checkbox.prop("checked", true).trigger("change");

									// // 전체 checked 확인
									// var $checkboxes = $("input[name=rowOgCheckbox]");
									// var $checkedBox = $("input[name=rowOgCheckbox]:checked");
									// if( $checkboxes.not(":disabled").length == $checkedBox.length ) {
									// 	$("#checkOgBoxAll").prop("checked", true);
									// }

									// BOX 발주수량 변경
									var piecePoQty = parseInt($(e.target).val())
									rowData.set('boxPoQty', (piecePoQty/rowData.pcsPerBox).toFixed(1));
								});

								input.attr("name", options.field);
								input.appendTo(container);
							},
							template: function(dataItem) {
								var html;
								if (dataItem.isPoExpired == 'Y' || dataItem.isPoPossible == 'N') { // 발주불가 또는 발주마감 시간이 지났으면 disabled 처리한다.
									html = "<input type='text' name='rowOfPiecePoQty' class='comm-input' value='" + dataItem.piecePoQty + "' style='width: 100%; text-align:center;' disabled>";
								} else {
									html = "<input type='text' name='rowOfPiecePoQty' class='comm-input' value='" + dataItem.piecePoQty + "' style='width: 100%; text-align:center;'>";
								}
								return html;
							}
						}
//				             ,{ field:'boxPoQty'          ,title : 'BOX<br>발주수량'	     , width:'50px'	 ,attributes:{ style:'text-align:center' }, sortable:false
//							   	, editable : function (){ return false; }
//				             }
						, { field:'eventPoQty'		  ,headerTemplate : '<span title="행사 발주수량">행사<br>발주수량<br>-</span>'	     , width:'5%' ,attributes:{ style:'text-align:center' }
							, editable : function (){ return false; }
						}
						, { field:'stockScheduledDt'  ,headerTemplate : '<span title="입고 예정일자">입고<br>예정일자</span>'	     , width:'76px' ,attributes:{ "class" : 'row_line', style:'text-align:center' }, sortable:false
							, headerAttributes :{
								"class" : 'row_line'
							}
							, template: function(dataItem) {
								var html;
								if (dataItem.isPoPossible == 'N') { // 발주불가의 경우 예외처리.
									html = '발주불가일';
								} else {
									html = dataItem.stockScheduledDt;
								}
								return html;
							}
							, editable : function (){ return false; }
						}
					]
				}
				,{ title : '재고정보'
					, headerAttributes :{
						"class" : 'row_line'
					}
					,columns: [
						{ field:'stockClosed'	    ,headerTemplate : '<span title="전일 마감재고">전일<br>마감재고<br>-</span>'        , width:'54px'	,attributes:{ style:'text-align:center' }
							, editable : function (){ return false; }
						}
						, { field:'stockConfirmed'    ,headerTemplate : '<span title="입고확정 (입고예정)">입고<br>확정<br>(입고<br>예정)<br>-</span>', width:'6%'	    ,attributes:{ style:'text-align:center' }
							, template: function(dataItem) {
								return dataItem.stockConfirmed + '<br>(' + dataItem.stockScheduledD0 + ')';
							}
							, editable : function (){ return false; }
						}
						, { field:'stockScheduledD1More',headerTemplate : '<span title="입고 대기수량">입고<br>대기<br>수량<br>-</span>'        , width:'5%'	,attributes:{ style:'text-align:center' }
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
						, { field:'expectedResidualQty',headerTemplate : '<span title="예상 잔여수량">예상<br>잔여수량<br>-</span>'        , width:'54px'	,attributes:{ style:'text-align:center' }
							, editable : function (){ return false; }
						}
						, { field:'offStock'		    ,headerTemplate : '<span title="off재고 (올가전용)">off재고<br>(올가전용)<br>-</span>'   , width:'54px',attributes:{ "class" : 'row_line', style:'text-align:center' }
							, headerAttributes :{
								"class" : 'row_line'
							}
							, editable : function (){ return false; }
						}
					]
				}
				,{ title : '주문/출고정보'
					, columns: [
						{ field:'outbound0'		  ,headerTemplate : '<span title="D-0">D-0</span>'             , width:'5%'	,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function (){ return false; }
						}
						, { field:'outbound1'		  ,headerTemplate : '<span title="D+1">D+1</span>'             , width:'5%'	,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function (){ return false; }
						}
						, { field:'outbound2'	      ,headerTemplate : '<span title="D+2">D+2</span>'             , width:'5%'	,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function (){ return false; }
						}
						, { field:'outbound3More'    ,headerTemplate : '<span title="D+3이상">D+3<br>이상</span>'      , width:'5%' ,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function (){ return false; }
						}
						, { field:'memo'		      ,headerTemplate : '<span title="발주메모">발주메모</span>'          , width:'100px'	,attributes:{ style:'text-align:center' }, sortable:false
							, editor: function(container, options) {
								var input = $("<input class='comm-input' type='text' style='width: 100%; text-align:center;'/>");
								var rowData = options.model;

								input.on('focus', function(e) { // 낱개 발주수량 변경 시 event
									if (rowData.isPoExpired == 'Y' || rowData.isPoPossible == 'N' || fnExpiredPoDeadline(rowData.baseDt, rowData.poTpDeadline)) {
										$(e.target).prop("disabled", true);
									}
								});

								input.attr("name", options.field);
								input.appendTo(container);
							}
							, template: function(dataItem) {
								var html;
								if (dataItem.isPoExpired == 'Y') { // 발주마감 시간이 지났으면 disabled 처리한다.
									html = "<input type='text' name='rowOgMemo' class='comm-input' value='" + dataItem.memo + "' style='width: 100%; text-align:center;' disabled>";
								} else {
									html = "<input type='text' name='rowOgMemo' class='comm-input' value='" + dataItem.memo + "' style='width: 100%; text-align:center;'>";
								}
								return html;
							}
						}
						, {field:'erpEventOrderAvg',headerTemplate : '<span title="프로모션 판매평균">프로모션<br>판매평균</span>'   , width:'5%'	,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function (){ return false; }
						}
						, {field:'nonErpEventOrderAvg',headerTemplate : '<span title="일반 판매평균">일반<br>판매평균</span>'   , width:'5%'	,attributes:{ style:'text-align:center' }, sortable:false
							, editable : function (){ return false; }
						}
						, { field:'manager'		  ,headerTemplate : '<span title="관리자">관리자</span>'           , width:'5%'	,attributes:{ "class" : 'admin_name', style:'text-align:center' }, sortable:false
							, template: function(dataItem) {
								var html = "";
								if (dataItem.userSavedDt != undefined && dataItem.userSavedDt != null) {
									html = '<span>'+ dataItem.userNm + ' / ' + dataItem.loginId + '<br/>' + dataItem.userSavedDt + '</span>';

								}
								return html;
							}
							, editable : function (){ return false; }
						}
					]
				}
			]
		};
		ogGrid = $('#ogGrid').initializeKendoGrid( ogGridOpt ).cKendoGrid();
		$('#ogGrid').addClass('order_table');
		ogGrid.bind("dataBound", function(e) {
			const self = this;

//			let rowNum = ogGridDs._total;
			$("#ogGrid tbody > tr .row-number").each(function(index){
//				$(this).html(rowNum);
//				rowNum--;
				$(this).html(index+1);
			});

			//total count
			$('#countTotalSpan').text(ogGridDs._total);

			// 체크박스 다시 체크
			if(selectedOgRow.length) {
				const $rows = self.element.find('.k-grid-content tr');

				$rows.each(function() {
					const _self = $(this);
					const rowData = self.dataItem(_self);

					const ilPoId = rowData ? rowData.ilPoId : null;

					if(selectedOgRow.includes(ilPoId)) {
						_self.find('input[name="rowOgCheckbox"]').prop("checked", true);
					}
				});
			}

			resizeGrid(self.element);
		});

		ogGrid.bind("sort", handleSort);

		// 그리드 전체선택 클릭
		$("#checkOgBoxAll").on("change", function(index){
			selectedOgRow = [];

			if( $("#checkOgBoxAll").prop("checked") ){
				// $("input[name=rowOgCheckbox]").prop("checked", true);
				$("input[name=rowOgCheckbox]").not(":disabled").prop("checked", true);

				const _data = ogGridDs.data().slice();

				// disabled되지 않은 모든 행의 ilPoId를 배열에 넣음
				for(let i = 0, maxCnt = _data.length; i < maxCnt; i++) {
					const isDisabled = checkIsDisabled(_data[i]);

					if(!isDisabled) {
						selectedOgRow.push(_data[i].ilPoId);
					}
				}
			}else{
				$("input[name=rowOgCheckbox]").prop("checked", false);
			}
		});

		// 그리드 체크박스 클릭
		ogGrid.element.on("change", "[name=rowOgCheckbox]" , function(e){
			const $row = $(e.target).closest("tr");
			const dataItem = ogGrid.dataItem($row);
			const ilPoId = dataItem && dataItem.ilPoId;

			if( e.target.checked ){
				if( $("[name=rowOgCheckbox]").not(":disabled").length == $("[name=rowOgCheckbox]:checked").length ){
					$("#checkOgBoxAll").prop("checked", true);
				}

				selectedOgRow.push(ilPoId);
			}else{
				$("#checkOgBoxAll").prop("checked", false);

				const _index = selectedOgRow.findIndex(function(item) {
					return item === ilPoId;
				});

				if(_index > -1) {
					selectedOgRow.splice(_index, 1);
				}
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

		// ERP 카테고리 (대분류)
		fnTagMkChkBox({
			id    : 'searchErpCtgry',
			tagId : 'searchErpCtgry',
			url : "/admin/item/po/getErpCtgryList",
			params : {"stCommonCodeMasterCode" : "ERP_PO_TP", "useYn" :"Y"},
			beforeData : [
				{"CODE":"ALL", "NAME":"전체"},
			],
			chkVal: "",
			async : false,
			style : {}
			// textField :"NAME",
			// valueField : "CODE",
			// blank : "전체",
			// autoBind : false
		});
		$('#searchErpCtgry').bind("change", onCheckboxWithTotalChange);
		// ERP 카테고리 (대분류) '전체' 체크
		$('input[name=searchErpCtgry]').each(function(idx, element) {
			$(element).prop('disabled', true);
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
			min : fnGetToday(),
			max : fnGetDayAdd(fnGetToday(), 13, 'yyyy-MM-dd')
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

		// 외부몰 판매상태
		fnTagMkChkBox({
			id: "goodsOutMallSaleStatus",
			tagId: "goodsOutMallSaleStatus",
			url: "/admin/comn/getCodeList",
			params: { "stCommonCodeMasterCode": "GOODS_OUTMALL_SALE_STAT", "useYn": "Y"},
			beforeData: [{ "CODE": "ALL", "NAME": "전체" }],
			async: false
		});

		$('#goodsOutMallSaleStatus').bind("change", onCheckboxWithTotalChange);

		// 외부몰 판매상태 '전체' 체크
		$('input[name=goodsOutMallSaleStatus]').each(function(idx, element) {
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
			// var sourceId = $(e.sender.element).attr("id");

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
			const searchUrSupplierId = $("#searchUrSupplierId").val();
			onErpCtgryList(searchUrSupplierId);
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
			// var targetId = "searchErpCtgry";
			var searchUrWarehouseId = $("#searchUrWarehouseId").val();
			// var searchErpCtgryDropDown = $("#" + targetId).data("kendoDropDownList");

			// searchErpCtgryDropDown.dataSource.data([]);
			$("input[name=searchErpCtgry]").each(function(idx, element){
//               $(element).prop('checked', false);//전체해제
//               $(element).prop('disabled', true);
				$(element).closest('label').remove();
			});

			if (searchUrSupplierId != "") {
				// searchErpCtgryDropDown.enable(true);

				var dropDownUrl = '/admin/item/po/getErpCtgryList';

				fnTagMkChkBox({
					id    : 'searchErpCtgry',
					tagId : 'searchErpCtgry',
					url     : dropDownUrl,
					method	: 'GET',
					params  : {"searchUrWarehouseId" : searchUrWarehouseId, "searchUrSupplierId": searchUrSupplierId},
					beforeData : [
						{"CODE":"ALL", "NAME":"전체"},
					],
					chkVal: "",
					async : false,
					style : {},
					success :
						function( data ){
							$("input[name=searchErpCtgry]").each(function(idx, element) {
								$(element).prop('checked', true);
								$(element).prop('disabled', false);
							});
						}
						// function( data ){
						// 	searchErpCtgryDropDown.dataSource.data(data.rows);
						// },
					// isAction : 'select'
				});
			}
			// else {
			// 	searchErpCtgryDropDown.dataSource.data([]);
			// 	searchErpCtgryDropDown.enable(false);
			// }
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

	// pfGrid, ogGrid > sort 이벤트 핸들러
	function handleSort(e) {
		const dataSource = e.sender.dataSource;
		const total = dataSource.total();

		if(!total || total <= 0) {
			e.preventDefault();
		}
	}

	// 발주리스트 행 disabled 여부 체크
	function checkIsDisabled(item) {
		if(!item) return false;

		return item.isPoExpired == 'Y' || item.isPoPossible == 'N';
	}

	function fnInitEvents() {
		// HGRM-9908
		$('#pfArea').on('select', '.comm-input', function(e) {
			const $td = $(e.target).closest('td');
			const $grid = $(this).closest('.k-grid');

			if(!$grid || !$grid.length) return false;

			$grid.data('kendoGrid').editCell($td);
		});
	}

	// ------------------------------- Common Function end
	// -------------------------------


	// ------------------------------- Html 버튼 바인딩 Start
	// -------------------------------
	/** Common Search */
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear */
	$scope.fnClear =function(){	 fnClear();	};
	/** Common Save */
	$scope.fnSave = function(){	 fnSave();};
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
