/**-----------------------------------------------------------------------------
 * description 		 : 통계관리 > 출고통계 > 출고처/판매처 별 출고통계
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.05.11		이원호          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
    importScript("/js/service/ca/pov/calPovSearch.js", function (){
        fnInitialize();
    });

	// Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'staticsOutbound',
			callback : fnUI
		});
	}

	function fnUI(){
		fnInitButton();	// Initialize Button ---------------------------------
		fnInitGrid();	// Initialize Grid ------------------------------------
		fnInitOptionBox();// Initialize Option Box
		fnDefaultSet();
	}

	// --------------------------------- Button
	// Start---------------------------------
	function fnInitButton(){
		$('#fnSearch', '#fnClear', '#fnExcelDownload').kendoButton();
	}

	function fnSearch(){
		var data = $('#searchForm').formSerialize(true);

		if(data.findYear == "" || data.findMonth == ""){
		    fnKendoMessage({message : '기준기간을 입력해주세요.'});
		    return;
		}

		var query = {
            page         : 1,
            pageSize     : PAGE_SIZE,
            filterLength : fnSearchData(data).length,
            filter :  {
                filters : fnSearchData(data)
            }
		};
		aGridDs.query( query );

		//Set Grid Column header
		let week = new Array('일', '월', '화', '수', '목', '금', '토');
		let month = Number($('#findMonth').val());
        let lastDate = new Date($('#findYear').val(), month, 0);

        for (let i = 1; i <= lastDate.getDate(); i++){
        	let weekDate = new Date($('#findYear').val(), month-1, i);
            let title = month + "/" + i + "(" + week[weekDate.getDay()] + ")";
		    aGrid.thead.find("[data-field~='day" + i + "Cnt']").html(title);
		    aGrid.showColumn("day" + i + "Cnt");
        }

        for (let i = lastDate.getDate() + 1; i <= 31; i++){
            aGrid.hideColumn("day" + i + "Cnt");
        }

        if(data.searchType == 'WAREHOUSE'){
            aGrid.thead.find("[data-field~='div1']").html("출고처");
            aGrid.thead.find("[data-field~='div2']").html("배송유형");
        }else{
            aGrid.thead.find("[data-field~='div1']").html("판매처그룹");
            aGrid.thead.find("[data-field~='div2']").html("판매처");
        }
	}

    function fnGetCheckBoxText(id){
	    var value = "";
        $('form[id=searchForm] :checkbox[name='+ id +']:checked').each(function() {
            value += $(this).closest('label').find('span').text() + ","
        });
        if (value == null)
            value = "";
        value = value.substring(0, value.length - 1);
        return value;
	}

	function fnClear(){
		$('#searchForm').formClear(true);
		fnDefaultSet();
	}

    // 옵션 초기화
	function fnInitOptionBox(){

	    // 검색기준
        fnTagMkRadio({
            id: "searchType",
            tagId: "searchType",
            data: [{ "CODE": "WAREHOUSE", "NAME": '출고처기준' },
                   { "CODE": "SELLERS", "NAME": '판매처기준' }],
            chkVal: "WAREHOUSE"
        });

        // 기준기간 - 조회구분
        fnKendoDropDownList({
            id    : 'searchDateType',
            data  : [
                {"CODE":"ORDER_DT"	,"NAME":"주문일자"},
                {"CODE":"IC_DT"	    ,"NAME":"결제완료일자"},
                {"CODE":"IF_DT"	    ,"NAME":"주문I/F일자"},
                {"CODE":"DI_DT"	    ,"NAME":"배송중일자"}
            ],
            textField :"NAME",
            valueField : "CODE"
        });

        // 기준기간
        calPovSearchUtil.getCalculateAllMonth();

        fnTagMkChkBox({
            id        : 'exceptClaimOrderYn'
          , tagId     : 'exceptClaimOrderYn'
          , data      : [
                          { 'CODE' : 'Y'    , 'NAME' : '취소주문 제외'    }
                        ]
          , chkVal    : 'Y'
          , style     : {}
        });

        // 공급업체
        fnTagMkChkBox({
            id          : 'supplierFilter'
          , tagId       : 'supplierFilter'
          , url         : '/admin/comn/getDropDownSupplierList'
          , async       : false
          , isDupUrl    : 'Y'
          , style       : {}
          , beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
          , textField   : "supplierName"
          , valueField  : "supplierId"
        });

        // 보관방법
        fnTagMkChkBox({
            id          : 'storageMethodFilter'
          , tagId       : 'storageMethodFilter'
          , url         : '/admin/comn/getCodeList'
          , params : {
                  "stCommonCodeMasterCode" : "ERP_STORAGE_TYPE",
                  "useYn" : "Y"
              }
          , async       : false
          , isDupUrl    : 'Y'
          , style       : {}
          , beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
        });


        // 출고처그룹
        searchCommonUtil.getDropDownCommCd("urWarehouseGrpCd", "NAME", "CODE", "출고처 그룹 전체", "WAREHOUSE_GROUP", "Y");

        const $warehouseGroup = $("#urWarehouseGrpCd").data("kendoDropDownList");

        if( $warehouseGroup ) {
            $warehouseGroup.bind("change", function (e) {
                const warehouseGroupCode = this.value();

                fnAjax({
                    url     : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
                    method : "GET",
                    params : { "warehouseGroupCode" : warehouseGroupCode },
                    success : function( data ){
                        let warehouseId = $("#urWarehouseId").data("kendoDropDownList");
                        warehouseId.setDataSource(data.rows);
                    },
                    error : function(xhr, status, strError){
                        fnKendoMessage({ message : xhr.responseText });
                    },
                    isAction : "select"
                });
            });
        }

        // 출고처그룹 별 출고처
        const WAREHOSE_ID = "urWarehouseId";
        searchCommonUtil.getDropDownUrl(WAREHOSE_ID, WAREHOSE_ID, "/admin/comn/getDropDownWarehouseGroupByWarehouseList", "warehouseName", "warehouseId", "출고처 선택", "", "");

        // 검색 판매처그룹
        searchCommonUtil.getDropDownCommCd("sellersGroupCd", "NAME", "CODE", "판매처 그룹 전체", "SELLERS_GROUP", "Y", "", "", "");

        // 검색 판매처그룹 별 외부몰
        const SCH_OM_SELLERS_ID = "omSellersId";

        searchCommonUtil.getDropDownUrl(SCH_OM_SELLERS_ID, SCH_OM_SELLERS_ID, "/admin/comn/getDropDownSellersGroupBySellersList", "sellersNm", "omSellersId", "판매처 전체", "", "");

        const searchSellersGroup = $("#sellersGroupCd").data("kendoDropDownList");

        if( searchSellersGroup ) {
            searchSellersGroup.bind("change", function (e) {
                const searchSellersGroup = this.value();

                fnAjax({
                    url     : "/admin/comn/getDropDownSellersGroupBySellersList",
                    method : "GET",
                    params : { "sellersGroupCd" : searchSellersGroup },
                    success : function( data ){
                        let searchOmSellersId = $("#omSellersId").data("kendoDropDownList");
                        searchOmSellersId.setDataSource(data.rows);
                    },
                    error : function(xhr, status, strError){
                        fnKendoMessage({ message : xhr.responseText });
                    },
                    isAction : "select"
                });
            });
        }

        fbCheckboxChange(); //[공통] checkBox
	};

	// 기본 설정
	function fnDefaultSet(){
	    $("input[name=supplierFilter]").eq(0).prop("checked", true).trigger("change");
	    $("input[name=storageMethodFilter]").eq(0).prop("checked", true).trigger("change");
        $("input[name=exceptClaimOrderYn]").eq(0).prop("checked", true).trigger("change");
	};

    // --------------------------------- Button
	// End---------------------------------

	// ------------------------------- Grid Start
	// -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : "/admin/statics/outbound/getOutboundStaticsList",
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			, noRecordMsg : '검색된 목록이 없습니다.'
            //, pageable    : {
            //                  pageSizes   : [20, 30, 50]
            //                , buttonCount : 10
            //                }
            , navigatable : true
            , scrollable  : true
            , height      : 755
            , selectable  : true
            , editable    : false
            , resizable   : true
            , autobind    : false
			,columns   : [
			    { title: '구분' , field:'div', locked: true,
			        columns: [
			            { field:'div1' ,title : '출고처'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                        ,{ field:'div2' ,title : '배송유형'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                        ]}
                ,{ field:'day1Cnt' ,title : '1'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day2Cnt' ,title : '2'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day3Cnt' ,title : '3'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day4Cnt' ,title : '4'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day5Cnt' ,title : '5'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day6Cnt' ,title : '6'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day7Cnt' ,title : '7'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day8Cnt' ,title : '8'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day9Cnt' ,title : '9'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day10Cnt' ,title : '10'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day11Cnt' ,title : '11'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day12Cnt' ,title : '12'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day13Cnt' ,title : '13'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day14Cnt' ,title : '14'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day15Cnt' ,title : '15'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day16Cnt' ,title : '16'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day17Cnt' ,title : '17'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day18Cnt' ,title : '18'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day19Cnt' ,title : '19'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day20Cnt' ,title : '20'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day21Cnt' ,title : '21'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day22Cnt' ,title : '22'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day23Cnt' ,title : '23'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day24Cnt' ,title : '24'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day25Cnt' ,title : '25'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day26Cnt' ,title : '26'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day27Cnt' ,title : '27'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day28Cnt' ,title : '28'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day29Cnt' ,title : '29'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day30Cnt' ,title : '30'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'day31Cnt' ,title : '31'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'sumCnt' ,title : '합계'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'avgCnt' ,title : '평균'	,width:'90px' ,attributes:{ style:'text-align:center' }}
            ]
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		aGrid.bind("dataBound", function() {
			var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(row_num);
				row_num--;
			});
			
			if(aGridDs._data.length > 0) {
			    $('#totalCnt').text(aGridDs._data[aGridDs._data.length - 1].sumCnt);
            }

            // rowspan
            mergeGridRows('aGrid', ['div'], ['div1']);
        });
	}

  // ==========================================================================
  // # Kendo Grid 전용 rowSpan 메서드
  //  - @param gridId : div 로 지정한 그리드 ID, 해당 div 내 table 태그를 찾아감
  //  - @param mergeColumns : 그리드에서 셀 머지할 컬럼들의 data-field 목록
  //  - @param groupByColumns : group by 할 컬럼들의 data-field 목록, 해당 group 내에서만 셀 머지가 일어남
  // ==========================================================================
  function mergeGridRows(gridId, mergeColumns, groupByColumns) {
    // 데이터 1건 이하인 경우 : rowSpan 불필요하므로 return
    if( $('#' + gridId + ' > table > tbody > tr').length <= 1 ) {
      return;
    }


    var groupByColumnIndexArray = [];   // group by 할 컬럼들의 th 헤더 내 column index 목록
    var tdArray = [];                   // 해당 컬럼의 모든 td 배열, 개수 / 순서는 그리드 내 tr 개수 / 순서와 같음
    var groupNoArray = [];              // 파라미터로 전달된 groupByColumns 에 따라 계산된 그룹번호 배열, 같은 그룹인 경우 그룹번호 같음, 개수 / 순서는 tdArray 와 같음

    var groupNo;            // 각 tr 별 그룹번호, 같은 그룹인 경우 그룹번호 같음
    var beforeTr = null;    // 이전 tr
    var beforeTd = null;    // 이전 td
    var rowspan = null;     // rowspan 할 개수, 1 인경우 rowspan 하지 않음

    // 해당 그리드의 th 헤더 row
    var thRow = $('#' + gridId + ' > table > thead > tr')[0];

    // 셀 머지시 group by 할 컬럼들의 data-field 목록이 Array 형태의 파라미터로 전달시
    if( groupByColumns && Array.isArray(groupByColumns) && groupByColumns.length > 0 ) {

      $(thRow).children('th').each(function (thIndex, th) {  // thead 의 th 반복문 시작 : table 내 각 컬럼별로 반복
        // groupByColumns => groupByColumnIndexArray 로 변환
        if( groupByColumns.includes( $(th).attr('data-field') ) ) {
          groupByColumnIndexArray.push(thIndex);
        }

      });
    } // if 문 끝

    // ------------------------------------------------------------------------
    // tbody 내 tr 반복문 시작
    // ------------------------------------------------------------------------
    $('#' + gridId + ' > table > tbody > tr').each(function() {
      beforeTr = $(this).prev();        // 이전 tr
      // 첫번째 tr 인 경우 : 이전 tr 없음
      if( beforeTr.length == 0 ) {
        groupNo = 0;                    // 그룹번호는 0 부터 시작
        groupNoArray.push(groupNo);     // 첫번째 tr 의 그룹번호 push
      }
      else {
        var sameGroupFlag = true;       // 이전 tr 과 비교하여 같은 그룹인지 여부 flag, 기본값 true

        for( var i in groupByColumnIndexArray ) {
          var groupByColumnIndex = groupByColumnIndexArray[i];  // groupByColumns 로 전달된 각 column 의 index
          // 이전 tr 과 현재 tr 비교하여 group by 기준 컬럼의 html 값이 하나라도 다른 경우 flag 값 false ( 같은 그룹 아님 )
          if( $(this).children().eq(groupByColumnIndex).html() != $(beforeTr).children().eq(groupByColumnIndex).html() ) {
            sameGroupFlag = false;
          }
        }

        // 이전 tr 의 값과 비교하여 같은 그룹이 아닌 경우 : groupNo 1 증가시킴
        if( ! sameGroupFlag ) {
          groupNo++;
        }

        groupNoArray.push(groupNo); // 해당 tr 의 그룹번호 push
      }

    });
    // tbody 내 tr 반복문 끝
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // thead 의 th 반복문 시작 : table 내 각 컬럼별로 반복
    // ------------------------------------------------------------------------
    $(thRow).children('th').each(function (thIndex, th) {
      if( ! mergeColumns.includes( $(th).attr('data-field') ) ) {
        return true;    // mergeColumns 에 포함되지 않은 컬럼인 경우 continue
      }

      tdArray = [];  // 값 초기화
      beforeTd = null;
      rowspan = null;

      var colIdx = $("th", thRow).index(this);  // 해당 컬럼 index

      $('#' + gridId + ' > table > tbody > tr').each(function() {  // tbody 내 tr 반복문 시작
        var td = $(this).children().eq(colIdx);
        tdArray.push(td);
      });  // tbody 내 tr 반복문 끝

      // ----------------------------------------------------------------------
      // 해당 컬럼의 td 배열 반복문 시작
      // ----------------------------------------------------------------------
      for( var i in tdArray ) {
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
        }
        else {
          beforeTd = td;
        }

        beforeTd = ( beforeTd == null || beforeTd == undefined ) ? td : beforeTd; // set the that if not already set
      }
      // 해당 컬럼의 td 배열 반복문 끝
      // ----------------------------------------------------------------------
    });
    // thead 의 th 반복문 끝
    // ------------------------------------------------------------------------

  }

	// ------------------------------- Grid End -------------------------------

	// ------------------------------- Html 버튼 바인딩 Start
	// -------------------------------

	/** Common Search */
	$scope.fnSearch = function(){	fnSearch();	};
	/** Common Clear */
	$scope.fnClear = function(){	 fnClear();	};

	/** Button fnExcelDownload */
    $scope.fnExcelDownload = function() {
        var data = $('#searchForm').formSerialize(true);

        data.supplierFilterName = fnGetCheckBoxText('supplierFilter');
        data.storageMethodFilterName = fnGetCheckBoxText('storageMethodFilter');

        data.urWarehouseGrpCdName = $('#urWarehouseGrpCd').data('kendoDropDownList').text();
        data.urWarehouseIdName = $('#urWarehouseId').data('kendoDropDownList').text();
        data.sellersGroupCdName = $('#sellersGroupCd').data('kendoDropDownList').text();
        data.omSellersIdName = $('#omSellersId').data('kendoDropDownList').text();

        fnExcelDownload('/admin/statics/outbound/getExportExcelOutboundStaticsList', data);
    };

	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END
