/**-----------------------------------------------------------------------------
 * description 		 : 통계관리 > 프로모션통계 > 외부광고코드별 매출현황 통계
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.07.23		안치열          최초생성
 * @
 * **/
'use strict';

var aGridDs, aGridOpt, aGrid, aGridDsGoods, aGridOptGoods, aGridGoods;

$(document).ready(function() {
    importScript("/js/service/ca/pov/calPovSearch.js", function (){
        fnInitialize();
    });

	// Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'staticsAdvertising',
			callback : fnUI
		});
	}

	function fnUI(){
		fnInitButton();	// Initialize Button ---------------------------------
		fnInitGrid();	// Initialize Grid ------------------------------------
        fnInitGridGoods();
		fnInitOptionBox();// Initialize Option Box
	}

	// --------------------------------- Button
	// Start---------------------------------
	function fnInitButton(){
		$('#fnSearch', '#fnClear', '#fnExcelDownload', '#fnExcelDownloadGoods').kendoButton();
	}

	function fnSearch(){
		var data = $('#searchForm').formSerialize(true);

		if(data.searchDateStart == "" || data.searchDateEnd == ""){
		    fnKendoMessage({message : '기간검색일을 입력해주세요.'});
		    return;
		}

		aGridDs.read(data);

		//if(data.source == '' || data.source == '샵라이브') {
            aGridDsGoods.read(data);
        //}
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
          , defVal: fnGetDayMinus(fnGetToday(),1)
        });
        fnKendoDatePicker({
            id          : 'searchDateEnd'
          , format      : 'yyyy-MM-dd'
          , btnStyle    : false     //버튼 숨김
          , btnStartId  : 'searchDateStart'
          , btnEndId    : 'searchDateEnd'
          , change      : fnOnChangeEndDt
          , defVal: fnGetToday()
        });
        function fnOnChangeStartDt(e) {
          fnOnChangeDatePicker(e, 'start', 'searchDateStart', 'searchDateEnd');
        }
        function fnOnChangeEndDt(e) {
          fnOnChangeDatePicker(e, 'end', 'searchDateStart', 'searchDateEnd');
        }

        // 분류값 선택 - 매체
        fnKendoDropDownList({
            id : "source",
            url : "/admin/promotion/advertising/getAdvertisingType",
            tagId : "source",
            params : { "searchType" : "SOURCE" },
            valueField: 'CODE',
            textField: 'NAME',
            blank : "전체",
            async : false
        });

        // 분류값 선택 - 구좌
        fnKendoDropDownList({
            id : "medium",
            url : "/admin/promotion/advertising/getAdvertisingType",
            tagId : "medium",
            params : { "searchType" : "MEDIUM" },
            chkVal: "",
            blank : "전체",
            async : false,
            cscdId : "source",
            cscdField : "source"
        });

        // 분류값 선택 - 캠페인
        fnKendoDropDownList({
            id : "campaign",
            url : "/admin/promotion/advertising/getAdvertisingType",
            tagId : "campaign",
            params : { "searchType" : "CAMPAIGN"},
            chkVal: "",
            blank : "전체",
            async : false,
            cscdId : "medium",
            cscdField : "medium"
        });

        // 분류값 선택 - 콘텐츠
        fnKendoDropDownList({
            id : "content",
            url : "/admin/promotion/advertising/getAdvertisingType",
            tagId : "content",
            params : { "searchType" : "CONTENT"},
            chkVal: "",
            blank : "전체",
            async : false,
            cscdId : "campaign",
            cscdField : "campaign"
        });
	};

  // ==========================================================================
  // # 조회조건 정보 문자열 생성
  // ==========================================================================
  function fnGenConditionInfo() {

        let infoStr = '';
        let tmpStr  = '';

         // ------------------------------------------------------------------------
         // 외부광고코드
         // ------------------------------------------------------------------------
         tmpStr = '';
         tmpStr = $('#searchPmAdExternalCd').val();
         if (fnIsEmpty(tmpStr) == true) {
           infoStr += ' 검색기준: ' + tmpStr;
         }
         else {
           infoStr += ' 검색기준: 외부광고코드: ' + tmpStr;
         }

         // ------------------------------------------------------------------------
         // 외부광고 구분
         // ------------------------------------------------------------------------
         tmpStr = '';
         tmpStr = '외부광고 구분: ' + $('#source').data('kendoDropDownList').text() +' > '+ $('#medium').data('kendoDropDownList').text()
                    +' > '+ $('#campaign').data('kendoDropDownList').text()+' > '+ $('#content').data('kendoDropDownList').text();
         if (fnIsEmpty(tmpStr) == true) {
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
        // 자료갱신일
        // ------------------------------------------------------------------------
        let date = new Date();
        infoStr += '자료 갱신일: ' + date.oFormat('yyyy-MM-dd');

        //console.log('# 검색조건 :: ', infoStr);
        // hidden에 Set
        $('#searchInfo').val(infoStr);
      }


    // --------------------------------- Button
	// End---------------------------------

	// ------------------------------- Grid Start
	// -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetDataSource({
			url      : "/admin/statics/pm/getStaticsAdvertisingList"
		});
		aGridOpt = {
			dataSource: aGridDs
			, noRecordMsg : '검색된 목록이 없습니다.'
            , navigatable : true
            , scrollable  : true
			,columns   : [
			    { field:'source'         , title: '대분류(매체)'    , width: 150 , attributes:{ style:'text-align:center' }}
			  , { field:'medium'         , title: '중분류(구좌)'    , width: 150 , attributes:{ style:'text-align:center' }}
			  , { field:'campaign'       , title: '소분류(캠페인)'  , width: 100 , attributes:{ style:'text-align:center' }}
			  , { field:'content'        , title: '세분류(콘텐츠)'  , width: 100 , attributes:{ style:'text-align:center' }}
			  , { field:'paidPrice'      , title: '총매출'       , width: 100 , attributes:{ style:'text-align:center' }, format: '{0:\#\#,\#}'}
			  , { field:'orderCnt'       , title: '주문건수'       , width: 100 , attributes:{ style:'text-align:center' }}
			  , { field:'orderUnitPrice' , title: '주문단가'       , width: 100 , attributes:{ style:'text-align:center' }, format: '{0:\#\#,\#}'}
			  , { field:'userCnt'        , title: '구매고객수'      , width: 100 , attributes:{ style:'text-align:center' }}
			  , { field:'userUnitPrice'  , title: '인단가'         , width: 100 , attributes:{ style:'text-align:center' }, format: '{0:\#\#,\#}'}
            ]
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		aGrid.bind("dataBound", function() {
			var row_num =  aGridDs._data.length;
			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(row_num);
				row_num--;
			});

			if(aGridDs._data.length > 0) {
			    $('#totalPaidPrice').text(aGridDs._data[aGridDs._data.length - 1].paidPrice);
			    $('#totalOrderCnt').text(aGridDs._data[aGridDs._data.length - 1].orderCnt);
			    $('#totalUserCnt').text(aGridDs._data[aGridDs._data.length - 1].userCnt);
            }

        });
	}

    function fnInitGridGoods(){
        aGridDsGoods = fnGetDataSource({
            url      : "/admin/statics/pm/getStaticsAdvertisingGoodsList"
        });
        aGridOptGoods = {
            dataSource: aGridDsGoods
            , noRecordMsg : '검색된 목록이 없습니다.'
            , navigatable : true
            , scrollable  : true
            ,columns   : [
                { field:'source'         , title: '대분류(매체)'    , width: 150 , attributes:{ style:'text-align:center' }}
                , { field:'medium'         , title: '중분류(구좌)'    , width: 150 , attributes:{ style:'text-align:center' }}
                , { field:'campaign'       , title: '소분류(캠페인)'  , width: 100 , attributes:{ style:'text-align:center' }}
                , { field:'content'        , title: '세분류(콘텐츠)'  , width: 100 , attributes:{ style:'text-align:center' }}
                , { field:'pakageGoodsId'  , title: '묶음상품아이디'     , width: 120 , attributes:{ style:'text-align:center' }}
                , { field:'pakageGoodsNm'  , title: '묶음상품명'         , width: 200 , attributes:{ style:'text-align:left' }}
                , { field:'ilGoodsId'      , title: '상품아이디'     , width: 100 , attributes:{ style:'text-align:center' }}
                , { field:'goodsNm'        , title: '상품명'         , width: 200 , attributes:{ style:'text-align:left' }}
                , { field:'paidPrice'      , title: '총매출'       , width: 100 , attributes:{ style:'text-align:center' }, format: '{0:\#\#,\#}'}
                , { field:'orderCnt'       , title: '주문건수'       , width: 100 , attributes:{ style:'text-align:center' }}
                , { field:'orderUnitPrice' , title: '주문단가'       , width: 100 , attributes:{ style:'text-align:center' }, format: '{0:\#\#,\#}'}
                , { field:'userCnt'        , title: '구매고객수'      , width: 100 , attributes:{ style:'text-align:center' }}
                , { field:'userUnitPrice'  , title: '인단가'         , width: 100 , attributes:{ style:'text-align:center' }, format: '{0:\#\#,\#}'}
            ]
        };
        aGridGoods = $('#aGridGoods').initializeKendoGrid( aGridOptGoods ).cKendoGrid();
        aGridGoods.bind("dataBound", function() {
            var row_num =  aGridDsGoods._data.length;
            $("#aGridGoods tbody > tr .row-number").each(function(index){
                $(this).html(row_num);
                row_num--;
            });
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

        fnExcelDownload('/admin/statics/pm/getExportExcelStaticsAdvertisingList', data);
    };

    /** Button fnExcelDownloadGoods */
    $scope.fnExcelDownloadGoods = function() {

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

        fnExcelDownload('/admin/statics/pm/getExportExcelStaticsAdvertisingGoodsList', data);
    };

	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END
