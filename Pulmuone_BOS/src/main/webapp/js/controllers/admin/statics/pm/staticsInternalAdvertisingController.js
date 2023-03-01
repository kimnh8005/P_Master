/**-----------------------------------------------------------------------------
 * description 		 : 통계관리 > 프로모션통계 > 내부광고코드별 매출현황 통계
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
			PG_ID  : 'staticsInternalAdvertising',
			callback : fnUI
		});
	}

	function fnUI(){
		fnInitButton();	// Initialize Button ---------------------------------
		fnInitGrid();	// Initialize Grid ------------------------------------
		fnInitOptionBox();// Initialize Option Box

		fnDefault();
	}

	// --------------------------------- Button
	// Start---------------------------------
	function fnInitButton(){
		$('#fnSearch', '#fnClear', '#fnExcelDownload').kendoButton();
	}

	function fnSearch(){
		var data = $('#searchForm').formSerialize(true);

		if(data.searchDateStart == "" || data.searchDateEnd == ""){
		    fnKendoMessage({message : '기간검색일을 입력해주세요.'});
		    return;
		}
		gridDs.read(data);
	}

	function fnDefault(){
	    $("#searchContentNm").hide();
	}

	function fnClear(){
		$('#searchForm').formClear(true);
	}

    // 옵션 초기화
	function fnInitOptionBox(){
	    // ------------------------------------------------------------------------
        // 검색기준유형 [체크] : 주문일/결제일/매출일
        // ------------------------------------------------------------------------
        fnTagMkRadio({
            id      : 'searchTp'
          , tagId   : 'searchTp'
          // , chkVal  : 'singleSection'
          , tab     : true
          , data    : [ {CODE : 'ODR' , NAME  : '주문일'}
                      , {CODE : 'PAY' , NAME  : '결제일'}
                      , {CODE : 'SAL' , NAME  : '매출일'}
                      ]
          , chkVal  : 'ODR'
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

        // 분류값 선택 - Lv1(페이지)
        fnKendoDropDownList({
            id : "pageCd",
            url : "/admin/statics/pm/getAdvertisingType",
            tagId : "pageCd",
            params : { "searchType" : "PAGE" },
            valueField: 'CODE',
            textField: 'NAME',
            blank : "전체",
            async : false
        });

        // 분류값 선택 - Lv2(구좌/카테고리)
        fnKendoDropDownList({
            id : "contentCd",
            url : "/admin/statics/pm/getAdvertisingType",
            tagId : "contentCd",
            params : { "searchType" : "CONTENT" },
            chkVal: "",
            blank : "전체",
            async : false,
            cscdId : "pageCd",
            cscdField : "pageCd"
        });

        $('#pageCd').unbind('change').on('change', function(){
            var dropDownList =$('#pageCd').data('kendoDropDownList');
            var data = dropDownList.value();
            var key;

            if(data.includes('SerKwd')||data.includes('Cate')||data.includes('GNB1')||
                data.includes('GNB2')||data.includes('GNB5')||data.includes('bestItem')||
                data.includes('keywordItem')){
                key = 'KEYWORD';
            }else{
                key = 'NOTKEYWORD';
            }

            switch(key){
            case "NOTKEYWORD" :

                $("#searchContentNm").hide();
                $("#contentCd").closest(".k-widget").show();

                break;

            case "KEYWORD" :

                $("#searchContentNm").show();
                $("#contentCd").closest(".k-widget").hide();
                $("#contentCd").val('');

                break;
            }
        });

	};

    // --------------------------------- Button
	// End---------------------------------

	// ------------------------------- Grid Start
	// -------------------------------
	function fnInitGrid(){
		gridDs = fnGetDataSource({
			url      : "/admin/statics/pm/getStaticsInternalAdvertisingList"
		});
		gridOpt = {
			dataSource: gridDs
			, noRecordMsg : '검색된 목록이 없습니다.'
            , navigatable : true
            , scrollable  : true
            , height      : 755
			,columns   : [
			    { field:'pageNm'            , title: 'LEVEL1_페이지'      , width:120 , attributes:{ style:'text-align:center' }}
			  , { field:'contentNm'         , title: 'LEVEL2_영역/키워드'  , width:120 , attributes:{ style:'text-align:center' }}
              , { field:'paidPrice'         , title: '고객매출'            , width: 80 , attributes:{ style:'text-align:right' }, format: '{0:\#\#,\#}'}
              , { field:'orderCnt'          , title: '주문건수'            , width: 60 , attributes:{ style:'text-align:right' }}
              , { field:'orderUnitPrice'    , title: '주문단가'            , width: 80 , attributes:{ style:'text-align:right' }, format: '{0:\#\#,\#}'}
              , { field:'userCnt'           , title: '구매고객수'          , width: 60 , attributes:{ style:'text-align:right' }}
              , { field:'userUnitPrice'     , title: '인단가'             , width: 80 , attributes:{ style:'text-align:right' }, format: '{0:\#\#,\#}'}
            ]
		};

		grid = $('#grid').initializeKendoGrid( gridOpt ).cKendoGrid();

		grid.bind("dataBound", function() {
			var row_num = gridDs._total - ((gridDs._page - 1) * gridDs._pageSize);
			$("#grid tbody > tr .row-number").each(function(index){
				$(this).html(row_num);
				row_num--;
			});

			if(gridDs._data.length > 0) {
			    $('#totalPaidPrice').text(gridDs._data[gridDs._data.length - 1].paidPrice);
			    $('#totalOrderCnt').text(gridDs._data[gridDs._data.length - 1].orderCnt);
			    $('#totalUserCnt').text(gridDs._data[gridDs._data.length - 1].userCnt);
            }

        });
	}


  // ==========================================================================
  // # 조회조건 정보 문자열 생성
  // ==========================================================================
  function fnGenConditionInfo() {

    let infoStr = '';
    let tmpStr  = '';


     // ------------------------------------------------------------------------
     // 내부광고코드
     // ------------------------------------------------------------------------
     tmpStr = '';
     tmpStr = $('#searchPmAdInternalPageCd').val();
     if (fnIsEmpty(tmpStr) == true) {
       infoStr += ' 검색기준: ' + tmpStr;
     }
     else {
       infoStr += ' 검색기준: 내부광고코드 :' + tmpStr;
     }

     // ------------------------------------------------------------------------
     // 내부광고 구분
     // ------------------------------------------------------------------------
     tmpStr = '';
     tmpStr =  $('#pageCd').data('kendoDropDownList').text();// +' > '+ $('#contentCd').data('kendoDropDownList').text();

     if (fnIsEmpty(tmpStr) == true) {
        infoStr += tmpStr;
     }else{
        var category ='';
        category =  $('#searchContentNm').val();
        if (!fnIsEmpty(category)) {
           tmpStr += ' > ' + category;
        }else{
            category =  $('#contentCd').data('kendoDropDownList').text();
            if (!fnIsEmpty(category)) {
               tmpStr += ' > ' + category;
            }
        }

        infoStr += ' 내부광고 구분 : ' + tmpStr;

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
      tmpStr = '기간: ' + tmpStr;
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
    // 정보갱신일
    // ------------------------------------------------------------------------
    let date = new Date();
    infoStr += ' / 정보 갱신일: ' + date.oFormat('yyyy-MM-dd');

    //console.log('# 검색조건 :: ', infoStr);
    // hidden에 Set
    $('#searchInfo').val(infoStr);
  }

    // ==========================================================================
    // # 오류메시지처리
    // ==========================================================================
    function fnMessage(key, nullMsg, ID) {
      fnKendoMessage({
          message : fnGetLangData({ key : key, nullMsg : nullMsg})
        , ok      : function() {
                      if (ID != null && ID != '') {
                        $('#'+ID).focus();
                      }
                    }
      });
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

        fnGenConditionInfo();
        var data = $('#searchForm').formSerialize(true);

        fnExcelDownload('/admin/statics/pm/getExportExcelStaticsInternalAdvertisingList', data);
    };

	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END
