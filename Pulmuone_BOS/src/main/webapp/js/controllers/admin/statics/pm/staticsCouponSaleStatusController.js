/**-----------------------------------------------------------------------------
 * description 		 : 통계관리 > 프로모션통계 > 쿠폰 별 매출현황 통계
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.07.23		안치열          최초생성
 * @
 * **/
'use strict';

var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
    importScript("/js/service/ca/pov/calPovSearch.js", function (){
        fnInitialize();
    });

	// Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'staticsCouponSaleStatus',
			callback : fnUI
		});
	}

	function fnUI(){
		fnInitButton();	// Initialize Button ---------------------------------
		fnInitGrid();	// Initialize Grid ------------------------------------
		fnInitOptionBox();// Initialize Option Box
	}

	// --------------------------------- Button
	// Start---------------------------------
	function fnInitButton(){
		$('#fnSearch', '#fnClear', '#fnExcelDownload').kendoButton();
	}

	function fnSearch(){
		var data = $('#searchForm').formSerialize(true);

		if(data.searchDateStart == "" || data.searchDateEnd == ""){
		    fnKendoMessage({message : '조회기간을 입력해주세요.'});
		    return;
		}

        aGridDs.read(data);
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
	}

    // 옵션 초기화
	function fnInitOptionBox(){
	    // 쿠폰명
		fnKendoDropDownList({
    		id    : 'searchSelect',
    		data  : [{"CODE":"SEARCH_SELECT.DISPLAY"	,"NAME":'전시 쿠폰명'}
    				,{"CODE":"SEARCH_SELECT.BOS"		,"NAME":'관리자 쿠폰명'}
    			    ]
    	});

	    // 쿠폰종류
        fnKendoDropDownList({
            id  : 'searchCouponStatus',
            url : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "COUPON_TYPE", "useYn" :"Y"},
            async : false,
            textField :"NAME",
            valueField : "CODE",
            blank : "쿠폰종류 전체"
        });


        // 발급목적
        fnKendoDropDownList({
            id  : 'searchIssuedType',
            tagId : 'searchIssuedType',
            url   : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "ISSUE_PURPOSE_TYPE", "useYn" :"Y"},
            textField :"NAME",
            valueField : "CODE",
            value : "NAME",
            blank : "전체"
        });

        // 재발행 포함/제외
        fnKendoDropDownList({
            id    : 'searchReissue',
            data  : [
                {"CODE":"EXCEPT" 	    ,"NAME":"제외"},
                {"CODE":"INCLUDE"	    ,"NAME":"포함"}
            ],
            textField :"NAME",
            valueField : "CODE"
        });

	    // ------------------------------------------------------------------------
        // 검색기준유형 [체크] : 생성일/발급일/사용일
        // ------------------------------------------------------------------------
        fnTagMkRadio({
            id      : 'searchTp'
          , tagId   : 'searchTp'
          // , chkVal  : 'singleSection'
          , tab     : true
          , data    : [ {CODE : 'CRD' , NAME  : '생성일'}
                      , {CODE : 'ISD' , NAME  : '발급일'}
                      , {CODE : 'USD' , NAME  : '사용일'}
                      ]
          , chkVal  : 'CRD'
          , change  : function (e) {}
          , style   : {}
        });

        // ------------------------------------------------------------------------
        // 기준기간 시작일/종료일 [날짜]
        // ------------------------------------------------------------------------
        fnKendoDatePicker({
            id          : 'searchDateStart'
          , format      : 'yyyy-MM-dd'
          , btnStartId  : 'searchDateStart'
          , btnEndId    : 'searchDateEnd'
          , change      : fnOnChangeStartDt
        });
        fnKendoDatePicker({
            id          : 'searchDateEnd'
          , format      : 'yyyy-MM-dd'
          , btnStyle    : false     //버튼 숨김
          , btnStartId  : 'searchDateStart'
          , btnEndId    : 'searchDateEnd'
          , change      : fnOnChangeEndDt
        });
        function fnOnChangeStartDt(e) {
          fnOnChangeDatePicker(e, 'start', 'searchDateStart', 'searchDateEnd');
        }
        function fnOnChangeEndDt(e) {
          fnOnChangeDatePicker(e, 'end', 'searchDateStart', 'searchDateEnd');
        }


        var tooltip = $("#tooltipDiv").kendoTooltip({ // 도움말 toolTip
            filter : "span",
            width : 600,
            position : "center",
            content: kendo.template($("#tooltip-template").html()),
            animation : {
                open : {
                    effects : "zoom",
                    duration : 150
                }
            }
        }).data("kendoTooltip");
	};




    // --------------------------------- Button
	// End---------------------------------

	// ------------------------------- Grid Start
	// -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetDataSource({
			url      : "/admin/statics/pm/getStaticsCouponSaleStatusList"
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
//            , height      : 755
            , selectable  : true
            , editable    : false
            , resizable   : true
            , autobind    : false
			,columns   : [
			    { field:'pmCouponId'        ,title : '쿠폰번호'	        ,width:'90px' ,attributes:{ style:'text-align:center' }}
			    ,{ field:'couponTp'         ,title : '쿠폰종류'	        ,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'issuePurpose'     ,title : '발급목적'	        ,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'displayCouponNm'  ,title : '전시쿠폰명'	        ,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'bosCouponNm'      ,title : '관리자쿠폰명'	    ,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'usePcMobile'      ,title : '사용(PC/모바일)'	,width:'90px' ,attributes:{ style:'text-align:center' },
                 template : function(dataItem){
                     	    let returnValue = '';
                     	    if(dataItem.usePcYn == 'Y'){
                     	    	returnValue += 'PC,';
                     	    }
                     	    if(dataItem.useMoWebYn == 'Y'){
                                returnValue += 'Mobile,';
                            }else{
                                if(dataItem.usePcYn == 'N'){
                                    returnValue = returnValue.slice(0, -1);
                                }
                            }
                            if(dataItem.useMoAppYn == 'Y'){
                                returnValue += 'App';
                            }else{
                                returnValue = returnValue.slice(0, -1);
                            }
                             return returnValue;
                           }
                  }
                ,{ field:'issueQty'         ,title : '생성수량'	        ,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'issueCnt'         ,title : '발급수량'	        ,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'issuePrice'       ,title : '발급금액'	        ,width:'90px' ,attributes:{ style:'text-align:center' }, format: '{0:\#\#,\#}'}
                ,{ field:'orderCnt'         ,title : '주문건수'	        ,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'discountPrice'    ,title : '쿠폰할인<br>금액'	,width:'90px' ,attributes:{ style:'text-align:center' }, format: '{0:\#\#,\#}', headerTemplate: '<div id="tooltipDiv">  구매자수 <span class="k-icon k-i-question k-i-help" style="margin-left:10px;"> </span> </div>'}
                ,{ field:'paidPrice'        ,title : '매출기여'	        ,width:'90px' ,attributes:{ style:'text-align:center' }, format: '{0:\#\#,\#}'}
                ,{ field:'userCnt'          ,title : '구매자수'	        ,width:'120px',attributes:{ style:'text-align:center'}}
            ]
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		aGrid.bind("dataBound", function() {
			var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(row_num);
				row_num--;
			});

			// 금액 / 수량 / 구매자 수 setting
			let totalPaidPrice = 0;
			let totalIssueCnt = 0;
			let totalUserCnt = 0;
			var targetGridArr = aGridDs._data;

			if(targetGridArr.length > 0) {

			    for (var i = 0; i < targetGridArr.length; i++) {
                        totalPaidPrice     += targetGridArr[i].paidPrice;
                        totalIssueCnt  += targetGridArr[i].issueCnt;
                        totalUserCnt  += targetGridArr[i].userCnt;
			    }

			    $('#totalPaidPrice').text(totalPaidPrice);
			    $('#totalIssueCnt').text(totalIssueCnt);
			    $('#totalUserCnt').text(totalUserCnt);
            }

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

    // ==========================================================================
    // # 조회조건 정보 문자열 생성
    // ==========================================================================
    function fnGenConditionInfo() {

      let infoStr = '';
      let tmpStr  = '';


      // ----------------------------------------------------------------------
      // 쿠폰종류
      // ----------------------------------------------------------------------
      tmpStr = '';
      tmpStr = $('#searchCouponStatus').data('kendoDropDownList').text();
      if (fnIsEmpty(tmpStr) == true) {
        infoStr += ' 검색기준 : ' + tmpStr;
      }
      else {
        infoStr += ' 검색기준 : 쿠폰종류: ' + tmpStr;
      }

      // ----------------------------------------------------------------------
      // 발급목적
      // ----------------------------------------------------------------------
      tmpStr = '';
      tmpStr = '발급목적: ' + $('#searchIssuedType').data('kendoDropDownList').text();
      if (fnIsEmpty(infoStr) == true) {
        infoStr += tmpStr;
      }
      else {
        infoStr += ' / ' + tmpStr;
      }

      // ----------------------------------------------------------------------
      // 재발행 포함/제외
      // ----------------------------------------------------------------------
      tmpStr = '';
      tmpStr = '재발행 포함/제외: ' + $('#searchReissue').data('kendoDropDownList').text();
      if (fnIsEmpty(infoStr) == true) {
        infoStr += tmpStr;
      }
      else {
        infoStr += ' / ' + tmpStr;
      }


      // ------------------------------------------------------------------------
      // 검색기준 : 라디오
      // ------------------------------------------------------------------------
      $('input[name="searchTp"]:checked').each(function() {
        //var value = $(this).val();
        var text = $(this).closest('label').find('span').text();
        tmpStr = text;
      });
      if (fnIsEmpty(tmpStr) != true) {
        // 값이 존재하면
        tmpStr = '조회기간: ' + tmpStr;
      }
      infoStr += ' / ' + tmpStr;

      // ------------------------------------------------------------------------
      // 기준기간
      // ------------------------------------------------------------------------
      tmpStr = '';
      tmpStr = $('#searchDateStart').val() + '~' + $('#searchDateEnd').val();

      if (fnIsEmpty($('#searchDateStart').val()) != true || fnIsEmpty($('#searchDateEnd').val()) != true) {
        tmpStr = ' ' + tmpStr;
        if (fnIsEmpty(infoStr) == true) {
          infoStr += tmpStr;
        }
        else {
          infoStr += ' / ' + tmpStr;
        }
      }

      // ------------------------------------------------------------------------
      // 자료갱신일
      // ------------------------------------------------------------------------
      let date = new Date();
      infoStr += ' / 자료 갱신일: ' + date.oFormat('yyyy-MM-dd');

      //console.log('# 검색조건 :: ', infoStr);
      // hidden에 Set

      $('#searchInfo').val(infoStr);
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

        let startDe         = $('#searchDateStart').val();
        let endDe           = $('#searchDateEnd').val();

        if (fnIsEmpty(startDe)) {
          fnMessage('', '<font color="#FF1A1A">[기간검색 시작일자]</font> 필수 입력입니다.', 'startDe');
          return;
        }
        if (fnIsEmpty(endDe)) {
          fnMessage('', '<font color="#FF1A1A">[기간검색 종료일자]</font> 필수 입력입니다.', 'endDe');
          return;
        }

        fnGenConditionInfo()

        var data = $('#searchForm').formSerialize(true);

//        data.supplierFilterName = fnGetCheckBoxText('supplierFilter');
//        data.storageMethodFilterName = fnGetCheckBoxText('storageMethodFilter');
//
//        data.urWarehouseGrpCdName = $('#urWarehouseGrpCd').data('kendoDropDownList').text();
//        data.urWarehouseIdName = $('#urWarehouseId').data('kendoDropDownList').text();
//        data.sellersGroupCdName = $('#sellersGroupCd').data('kendoDropDownList').text();
//        data.omSellersIdName = $('#omSellersId').data('kendoDropDownList').text();
        fnExcelDownload('/admin/statics/pm/getExportExcelStaticsCouponSaleStatusList', data);
    };

	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END
