/**-----------------------------------------------------------------------------
 * description 		 : 통계관리 > 프로모션통계 > 회원등급 쿠폰현황 통계
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.07.23		안치열          최초생성
 * @
 * **/
'use strict';

var gridDs, gridOpt, grid;

$(document).ready(function() {
    importScript("/js/service/ca/pov/calPovSearch.js", function (){
        fnInitialize();
    });

	// Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'staticsUserGroupCouponStatus',
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

		if(data.findYear == "" || data.findMonth == ""){
		    fnKendoMessage({message : '발급기간을 입력해주세요.'});
		    return;
		}

        gridDs.read(data);

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

         // 기준기간
        calPovSearchUtil.getCalculateMonth();

	};


    // --------------------------------- Button
	// End---------------------------------

	// ------------------------------- Grid Start
	// -------------------------------
	function fnInitGrid(){
		gridDs = fnGetDataSource({
			url      : "/admin/statics/pm/getStaticsUserGroupCouponStatusList"
		});
		gridOpt = {
			dataSource: gridDs
			, noRecordMsg : '검색된 목록이 없습니다.'
            , navigatable : true
            , scrollable  : true
			,columns   : [
                 { field:'groupMasterNm'    ,title : '회원등급'	    ,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'issueCnt'         ,title : '발급수량'	    ,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'issuePrice'       ,title : '발급금액'	    ,width:'90px' ,attributes:{ style:'text-align:center' }, format: '{0:\#\#,\#}'}
                ,{ field:'useCnt'           ,title : '사용수량'	    ,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'usePrice'         ,title : '사용금액'	    ,width:'90px' ,attributes:{ style:'text-align:center' }, format: '{0:\#\#,\#}'}
                ,{ field:'expirationCnt'    ,title : '소멸수량'	    ,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'expirationPrice'  ,title : '소멸금액'	    ,width:'90px' ,attributes:{ style:'text-align:center' }, format: '{0:\#\#,\#}'}
            ]
		};

		grid = $('#grid').initializeKendoGrid( gridOpt ).cKendoGrid();

		grid.bind("dataBound", function() {
			var row_num =  gridDs._data.length;
			$("#grid tbody > tr .row-number").each(function(index){
				$(this).html(row_num);
				row_num--;
			});

			if(gridDs._data.length > 0) {
			    $('#totalIssuePrice').text(gridDs._data[gridDs._data.length - 1].issuePrice);
            }

            // rowspan
            mergeGridRows('grid', ['groupMasterNm'], ['groupMasterNm']);
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
      // 발급기간
      // ----------------------------------------------------------------------
      tmpStr = '';
      tmpStr = $('#findYear').data('kendoDropDownList').text() + $('#findMonth').data('kendoDropDownList').text();
      if (fnIsEmpty(tmpStr) == true) {
        infoStr += tmpStr;
      }
      else {
        infoStr += '검색기준: 발급기간 : '  + tmpStr;
      }


      // ------------------------------------------------------------------------
      // 정보갱신일
      // ------------------------------------------------------------------------
      let date = new Date();
      infoStr += ' / 정보 갱신일: ' + date.oFormat('yyyy-MM-dd');

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

        let findYear         = $('#findYear').val();
        let findMonth        = $('#findMonth').val();

        if (fnIsEmpty(findYear)) {
          fnMessage('', '<font color="#FF1A1A">[발급기간 년도]</font> 필수 입력입니다.', 'startDe');
          return;
        }
        if (fnIsEmpty(findMonth)) {
          fnMessage('', '<font color="#FF1A1A">[발급기간 월]</font> 필수 입력입니다.', 'endDe');
          return;
        }

        fnGenConditionInfo();

        var data = $('#searchForm').formSerialize(true);

        fnExcelDownload('/admin/statics/pm/getExportExcelStaticsUserGroupCouponStatusList', data);
    };

	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END
