/*******************************************************************************************************************************************************************************************************
 * -------------------------------------------------------- description : 마스터 품목 관리 - 마스터 품목 리스트 * @ @ 수정일 수정자 수정내용 @ ------------------------------------------------------ * @ 2017.02.13 신혁 최초생성 @
 ******************************************************************************************************************************************************************************************************/
'use strict';
var pageParam = fnGetPageParam(); // GET 방식으로 전달된 parameter 객체

var PAGE_SIZE = 20;
var viewModel, itemGridOpt, itemGrid, itemGridDs;

$(document).ready(function() {

	var goodsId = parent.POP_PARAM["parameter"].goodsId;

	// Initialize Page Call
	fnInitialize();

	// Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', {
			flag : false
		});

		fnPageInfo({
			PG_ID : 'goodsPackageDetailListPopup',
			callback : fnUI
		});
	};

	//전체화면 구성
	function fnUI() {
		$("#goodsId").val(goodsId);

		fnTranslate();			// comm.lang.js 안에 있는 공통함수 다국어
		fnItemGrid();			// 묶음 상품 상세 내역 Grid
	}

	 //묶음 상품 리스트 Grid
	 function fnItemGrid() {

		 itemGridDs = fnGetEditPagingDataSource({
			url : '/admin/goods/list/getGoodsPackageDetailList',
			pageSize : PAGE_SIZE,
			requestEnd : function(e){}
		 });

		 itemGridOpt = {
				dataSource : itemGridDs,

				editable : false,
				navigatable: false,
				columns : [
					{ field : 'goodsPackageTp',  title : '상품구분', width : '80px', attributes : { style : 'text-align:center' },
				          template : function(dataItem){
				        	  var packageTpNm = "";
				        	  if( dataItem.goodsPackageTp == "GOODS_TYPE.NORMAL" || dataItem.goodsPackageTp == "GOODS_TYPE.DISPOSAL") {
				        		  packageTpNm = "묶음상품"
				        	  }else if(dataItem.goodsPackageTp == "GOODS_TYPE.GIFT" || dataItem.goodsPackageTp == 'GOODS_TYPE.GIFT_FOOD_MARKETING') {
				        		  packageTpNm = "증정상품"
				        	  }else if(dataItem.goodsPackageTp == "GOODS_TYPE.ADDITIONAL") {
				        		  packageTpNm = "추가상품"
				        	  }


		                      return packageTpNm;
				          },
		                }
				,	{ field : 'itemCd', title : '품목코드<BR>품목바코드', width : '120px', attributes : { style : 'text-align:center' },
			          template : function(dataItem){
	                      return dataItem.itemCd + "<BR>" + dataItem.itemBarcode;
			          },
	                }
	            ,   { field : 'targetGoodsId', title : '상품코드', width : '80px', attributes : { style : 'text-align:center' }}
	            ,   { field : 'goodsTpNm', title : '상품유형', width : '80px', attributes : { style : 'text-align:center' } }
	            ,	{ field : 'goodsNm', title : '상품명', width : '150px', attributes : { style : 'text-align:left' } }
				,	{ field : 'taxYn', title : '과세구분', width : '80px', attributes : { style : 'text-align:center' },
						template : function(dataItem){
							return dataItem.taxYn == "Y" ? "과세" : "면세";
						}
              		}
				,	{ field : 'goodsQty', title : '구성수량', width : '80px', attributes : { style : 'text-align:center' } }

	            ]
			};

			itemGrid = $('#itemGrid').initializeKendoGrid(itemGridOpt).cKendoGrid();

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

			itemGrid.bind("dataBound", function() {
				// rowspan 로직 Start
	            mergeGridRows(
	                'itemGrid' // div 로 지정한 그리드 ID
	               , ['goodsPackageTp'] // 그리드에서 셀 머지할 컬럼들의 data-field 목록
	               , ['goodsPackageTp'] // group by 할 컬럼들의 data-field 목록
	            );
			});


	 };

	 /*
	     * Kendo Grid 전용 rowSpan 메서드
	     *
	     * @param gridId : div 로 지정한 그리드 ID, 해당 div 내 table 태그를 찾아감
	     * @param mergeColumns : 그리드에서 셀 머지할 컬럼들의 data-field 목록
	     * @param groupByColumns : group by 할 컬럼들의 data-field 목록, 해당 group 내에서만 셀 머지가 일어남
	     *
	     */
	    function mergeGridRows(gridId, mergeColumns, groupByColumns) {

	        if( $('#' + gridId + ' > table > tbody > tr').length <= 1 ) { // 데이터 1건 이하인 경우 : rowSpan 불필요하므로 return
	            return;
	        }

	        var groupByColumnIndexArray = [];  // group by 할 컬럼들의 th 헤더 내 column index 목록
	        var tdArray = [];  // 해당 컬럼의 모든 td 배열, 개수 / 순서는 그리드 내 tr 개수 / 순서와 같음
	        var groupNoArray = [];  // 파라미터로 전달된 groupByColumns 에 따라 계산된 그룹번호 배열, 같은 그룹인 경우 그룹번호 같음, 개수 / 순서는 tdArray 와 같음

	        var groupNo;  // 각 tr 별 그룹번호, 같은 그룹인 경우 그룹번호 같음
	        var beforeTr = null; // 이전 tr
	        var beforeTd = null; // 이전 td
	        var rowspan = null; // rowspan 할 개수, 1 인경우 rowspan 하지 않음

	        var thRow = $('#' + gridId + ' > table > thead > tr')[0];  // 해당 그리드의 th 헤더 row

	        // 셀 머지시 group by 할 컬럼들의 data-field 목록이 Array 형태의 파라미터로 전달시
	        if( groupByColumns && Array.isArray(groupByColumns) && groupByColumns.length > 0 ) {

	            $(thRow).children('th').each(function (thIndex, th) {  // thead 의 th 반복문 시작 : table 내 각 컬럼별로 반복

	                // groupByColumns => groupByColumnIndexArray 로 변환
	                if( groupByColumns.includes( $(th).attr('data-field') ) ) {
	                    groupByColumnIndexArray.push(thIndex);
	                }

	            });

	        } // if 문 끝

	        $('#' + gridId + ' > table > tbody > tr').each(function() {  // tbody 내 tr 반복문 시작
	            beforeTr = $(this).prev();  // 이전 tr

	            if( beforeTr.length == 0 ) {  // 첫번째 tr 인 경우 : 이전 tr 없음

	                groupNo = 0;  // 그룹번호는 0 부터 시작
	                groupNoArray.push(groupNo); // 첫번째 tr 의 그룹번호 push

	            } else {

	                var sameGroupFlag = true;  // 이전 tr 과 비교하여 같은 그룹인지 여부 flag, 기본값 true

	                for( var i in groupByColumnIndexArray ) {

	                    var groupByColumnIndex = groupByColumnIndexArray[i];  // groupByColumns 로 전달된 각 column 의 index

	                    // 이전 tr 과 현재 tr 비교하여 group by 기준 컬럼의 html 값이 하나라도 다른 경우 flag 값 false ( 같은 그룹 아님 )
	                    if( $(this).children().eq(groupByColumnIndex).html() != $(beforeTr).children().eq(groupByColumnIndex).html() ) {
	                        sameGroupFlag = false;
	                    }

	                }

	                if( ! sameGroupFlag ) {  // 이전 tr 의 값과 비교하여 같은 그룹이 아닌 경우 : groupNo 1 증가시킴
	                    groupNo++;
	                }

	                groupNoArray.push(groupNo); // 해당 tr 의 그룹번호 push

	            }

	        });  // tbody 내 tr 반복문 끝

	        $(thRow).children('th').each(function (thIndex, th) {  // thead 의 th 반복문 시작 : table 내 각 컬럼별로 반복

	            if( ! mergeColumns.includes( $(th).attr('data-field') ) ) {
	                return true;   // mergeColumns 에 포함되지 않은 컬럼인 경우 continue
	            }

	            tdArray = [];  // 값 초기화
	            beforeTd = null;
	            rowspan = null;

	            var colIdx = $("th", thRow).index(this);  // 해당 컬럼 index

	            $('#' + gridId + ' > table > tbody > tr').each(function() {  // tbody 내 tr 반복문 시작

	                var td = $(this).children().eq(colIdx);
	                tdArray.push(td);

	            });  // tbody 내 tr 반복문 끝

	            for( var i in tdArray ) {  // 해당 컬럼의 td 배열 반복문 시작

	                var td = tdArray[i];

	                if ( i > 0 && groupNoArray[i-1] == groupNoArray[i] && $(td).html() == $(beforeTd).html() ) {

	                    rowspan = $(beforeTd).attr("rowSpan");

	                    if ( rowspan == null || rowspan == undefined ) {
	                        $(beforeTd).attr("rowSpan", 1);
	                        rowspan = $(beforeTd).attr("rowSpan");
	                    }

	                    rowspan = Number(rowspan) + 1;

	                    $(beforeTd).attr("rowSpan",rowspan);
	                    $(td).hide(); // .remove(); // do your action for the old cell here

	                } else {

	                    beforeTd = td;

	                }

	                beforeTd = ( beforeTd == null || beforeTd == undefined ) ? td : beforeTd; // set the that if not already set

	            }  // 해당 컬럼의 td 배열 반복문 끝

	        });  // thead 의 th 반복문 끝

	    }

});