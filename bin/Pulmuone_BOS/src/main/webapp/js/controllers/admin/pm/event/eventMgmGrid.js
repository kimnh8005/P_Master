/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 프로모션 이벤트 그리드 - 소스분리 - Grid
 * @
 * @ 수정일        수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.01.29    dgyoun        최초 생성
 * @
 ******************************************************************************/

// ----------------------------------------------------------------------------
// 그리드
// ----------------------------------------------------------------------------
// 골라담기(균일가)-대표상품
var benefitCouponGridDs, benefitCouponGridOpt, benefitCouponGrid;
var benefitPointGridDs , benefitPointGridOpt , benefitPointGrid;
var experienceGridDs   , experienceGridOpt   , experienceGrid;

  // ==========================================================================
  // # 당첨자 혜택 - 쿠폰그리드 : [일반][설문][체험단]
  // ==========================================================================
  function fnInitBenefitCouponGrid(eventTypeFlag) {
    //console.log('# fnInitBenefitCouponGrid Start');

    var callUrl               = '';

    if(eventTypeFlag == undefined){
        eventTypeFlag = false;
    }

    // 페이징없는 그리드

    benefitCouponGridDs = fnGetDataSource({
        url      : '/admin/pm/event/selectfEventBenefitCouponList?evEventId='+gbEvEventId
    });

    var bEditableGrid;
    bEditableGrid = false;

    benefitCouponGridOpt = {
          dataSource  : benefitCouponGridDs
        , noRecordMsg : '검색된 목록이 없습니다.'
        , navigatable : true
        , scrollable  : false
        , height      : 'auto' // '120'
        , selectable  : true
        , editable    : bEditableGrid
        , resizable   : true
        , autobind    : false
        // 임시 툴바 Start
        //,toolbar      : [
        //                  { name: 'create', text: '신규', className: "btn-point btn-s"}
        //                , { name: 'cSave',  text: '저장', imageClass: "k-i-update", className: 'k-custom-save btn-point btn-s'}
        //                , { name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }
        //                ]
        // 임시 툴바 End
        , columns     : [
                        //  {                                 title: 'No'             , width:  '70px', attributes:{ style:'text-align:center' }
                        //                                                        , template  : '<span class="row-number"></span>'
                        //  }
                        //,
                          { field : 'pmCouponId'          , title : '쿠폰PK'        , width:  '10px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}, hidden : true}
                        , { field : 'evEventCouponId'     , title : '이벤트 쿠폰PK'        , width:  '10px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}, hidden : true}
                        , { field : 'displayCouponNm'     , title : '전시쿠폰명'    , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                    , template : function(dataItem) {
                                                                                        // 기간설정
                                                                                        if(dataItem.validityType == "VALIDITY_TYPE.PERIOD") {
                                                                                            return dataItem.displayCouponNm;
                                                                                            // 유효일
                                                                                        } else {
                                                                                            return dataItem.displayCouponNm + "<br>(" + dataItem.issueDt +")";
                                                                                        }
                                                                                    }
                          }
                        , { field : 'discountTpNm'        , title : '할인방식'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'discountDetl'        , title : '할인상세'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                          }
                        , { field : 'validityConts'       , title : '유효기간'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'urErpOrganizationNm' , title : '분담조직'      , width: '100px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'couponCnt'           , title : '계정당 지급 수량'      , width:  '40px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                    , template  : function(dataItem) {
                                                                                                    var couponCntVal = '';
                                                                                                    if (fnIsEmpty(dataItem.couponCnt) == true) {
                                                                                                      couponCntVal = '';
                                                                                                    }
                                                                                                    else {
                                                                                                      couponCntVal = dataItem.couponCnt + '';
                                                                                                    }
                                                                                                    return '<input type="text" name="couponCount" class="comm-input marginR10" maxLength="2" size="5" style="text-align:right;" value='+ couponCntVal +' />';
                                                                                                  }
                          }
                        , { field : 'couponTotalCnt'           , title : '총 당첨 수량'      , width:  '40px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}, hidden : eventTypeFlag
                                                                                    , template  : function(dataItem) {
                                                                                                    var couponTotalCntVal = '';
                                                                                                    if (fnIsEmpty(dataItem.couponTotalCnt) == true) {
                                                                                                      couponTotalCntVal = '';
                                                                                                    }
                                                                                                    else {
                                                                                                      couponTotalCntVal = dataItem.couponTotalCntVal + '';
                                                                                                    }
                                                                                                    return '<input type="text" name="couponTotalCount" class="comm-input marginR10" size="5" style="text-align:right;" value='+ couponTotalCntVal +' >';
                                                                                                  }
                          }
                        , { field : 'management'          , title : '관리'          , width:  '40px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                    , template  : function(dataItem) {
                                                                                                    return  '<div class="textCenter">'
                                                                                                          + '<button type="button" class="btn-red btn-s btnCouponDel" kind="btnBenefitCouponDel">삭제</button>'
                                                                                                          + '</div>';
                                                                                                  }
                          }
                        ]
        //, rowTemplate : kendo.template($('#rowTemplate').html())
    };

    benefitCouponGrid = $('#benefitCouponGrid').initializeKendoGrid( benefitCouponGridOpt ).data('kendoGrid');
    //$('#benefitCouponGrid').initializeKendoGrid( benefitCouponGridOpt ).cKendoGrid();
    // ------------------------------------------------------------------------
    // NO 항목 및 전체건수 Set
    // ------------------------------------------------------------------------
    benefitCouponGrid.bind('dataBound', function() {
      // 페이징 없음
      //var row_num = benefitCouponGridDs._total;
      var row_num = benefitCouponGridDs.data().length;
      $('#benefitCouponGrid tbody > tr .row-number').each(function(index){
        $(this).html(row_num);
        row_num--;
      });
      // ----------------------------------------------------------------------
      // 전체건수 Set
      // ----------------------------------------------------------------------
      //$('#totalCnt').text(exhibitGridDs._total);

      // ----------------------------------------------------------------------
      // 지급수량 숫자만 입력 설정
      // ----------------------------------------------------------------------
      $('#benefitCouponGrid tbody > tr').each(function(index){
        var inputDom = $(this).find("input[name=couponCount]");
        fnInputDomValidationForNumber(inputDom);
      });
      //var eventCouponList = $('#benefitCouponGrid').data('kendoGrid').dataSource.data();
      //console.log('# eventCouponList :: ', eventCouponList);
      //for (var i = 0; i < eventCouponList.length; i++) {
      //  $('#benefitCouponGrid input[name="kkk"]')[i].value = eventCouponList[i].couponCnt+'';
      //}
      // ----------------------------------------------------------------------
      // 총 당첨 수량 숫자만 입력 설정
      // ----------------------------------------------------------------------
      $('#benefitCouponGrid tbody > tr').each(function(index){
        var inputDom = $(this).find("input[name=couponTotalCount]");
        fnInputDomValidationForNumber(inputDom);
      });
    });

    // ------------------------------------------------------------------------
    // 삭제 버튼 클릭
    // ------------------------------------------------------------------------
    $('#benefitCouponGrid').on("click", "button[kind=btnBenefitCouponDel]", function(e) {
      e.preventDefault();
      // 골라담기.대표상품 삭제 : 그리드에서만 삭제한다.
      fnKendoMessage( {
          message : fnGetLangData({key :'', nullMsg : '<div>삭제하시겠습니까?</div>' })
        , type    : 'confirm'
        , ok      : function(){
                      let dataItem = benefitCouponGrid.dataItem($(e.currentTarget).closest('tr'));
                      benefitCouponGridDs.remove(dataItem);
                      //fnMessage('', '제거되었습니다.', '');
                    }
      });
    });
  }


  // ==========================================================================
  // # 당첨자 혜택 - 동적 쿠폰그리드 : [스탬프(출석)][스탬프(미션)][스탬프(구매)][룰렛]
  // ==========================================================================
  function fnInitBenefitCouponDynamicGrid(gridId, gridIndex, evEventId, gbIsPopupHiddenYn) {
    //console.log('# fnInitBenefitCouponDynamicGrid Start [', gridId, '][', gridIndex, '][', evEventId, ']');

    var callUrl               = '';

    // 페이징없는 그리드
    var benefitCouponDynamicGridDs = fnGetDataSource({
      url      : '/admin/pm/event/selectfEventBenefitStampCouponList?evEventId='+gbEvEventId
    });

    var bEditableGrid;
    bEditableGrid = false;
//    gbIsPopupHiddenYn = false;

    var benefitCouponDynamicGridOpt = {
          dataSource  : benefitCouponDynamicGridDs
        , noRecordMsg : '검색된 목록이 없습니다.'
        , navigatable : true
        , scrollable  : false
          //, height      : 'auto' // '120'
        , selectable  : true
        , editable    : bEditableGrid
        , resizable   : true
        , autobind    : false
        // 임시 툴바 Start
        //,toolbar      : [
        //                  { name: 'create', text: '신규', className: "btn-point btn-s"}
        //                , { name: 'cSave',  text: '저장', imageClass: "k-i-update", className: 'k-custom-save btn-point btn-s'}
        //                , { name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }
        //                ]
        // 임시 툴바 End
        , columns     : [
                        //  {                                 title: 'No'             , width:  '70px', attributes:{ style:'text-align:center' }
                        //                                                        , template  : '<span class="row-number"></span>'
                        //  }
                        //,
                          { field : 'pmCouponId'          , title : '쿠폰PK'        , width:  '10px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}, hidden : true}
                        , { field : 'evEventCouponId'     , title : '이벤트 쿠폰PK'  , width:  '10px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}, hidden : true}
                        , { field : 'displayCouponNm'     , title : '전시쿠폰명'    , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                    , template : function(dataItem) {
                                                                                        // 기간설정
                                                                                        if (dataItem.validityTp == "VALIDITY_TYPE.PERIOD") {
                                                                                            return dataItem.displayCouponNm;
                                                                                            // 유효일
                                                                                        } else {
                                                                                            return dataItem.displayCouponNm + "<br>" + dataItem.issueDt;
                                                                                        }
                                                                                    }
                          }
                        , { field : 'discountTpNm'        , title : '할인방식'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'discountDetl'        , title : '할인상세'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'validityConts'       , title : '유효기간'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'urErpOrganizationNm' , title : '분담조직'      , width: '100px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'couponCnt'           , title : '계정당 지급 수량'      , width:  '40px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                    , template  : function(dataItem) {
                                                                                                    var couponCntVal;
                                                                                                    if (fnIsEmpty(dataItem.couponCnt) == true) {
                                                                                                      couponCntVal = '';
                                                                                                    }
                                                                                                    else {
                                                                                                      couponCntVal = dataItem.couponCnt + '';
                                                                                                    }
                                                                                                    return '<input type="text" name="couponCount" class="comm-input marginR10" maxLength="2" size="5" style="text-align:right;" value="'+couponCntVal+'" />';
                                                                                                  }
                          }
                          , { field : 'couponTotalCnt'           , title : '총 당첨 수량'      , width:  '40px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                  , template  : function(dataItem) {
                                                                                                  var couponTotalCntVal = '';
                                                                                                  if (fnIsEmpty(dataItem.couponTotalCnt) == true) {
                                                                                                    couponTotalCntVal = '';
                                                                                                  }
                                                                                                  else {
                                                                                                    couponTotalCntVal = dataItem.couponTotalCnt + '';
                                                                                                  }
                                                                                                  return '<input type="text" name="couponTotalCount" class="comm-input marginR10" size="5" style="text-align:right;" value='+ couponTotalCntVal +' >';
                                                                                                }
                                                                                                , hidden : gbIsPopupHiddenYn
                                                    }
                        , { field : 'management'          , title : '관리'          , width:  '40px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                    , template  : function(dataItem) {
                                                                                                    return  '<div class="textCenter">'
                                                                                                          + '<button type="button" class="btn-red btn-s btnCouponDel" kind="btnBenefitCouponDel">삭제</button>'
                                                                                                          + '</div>';
                                                                                                  }
                          }
                        ]
        //, rowTemplate : kendo.template($('#rowTemplate').html())
    };

    // Grid
    $('#'+gridId).initializeKendoGrid(benefitCouponDynamicGridOpt).data('kendoGrid');
    //benefitCouponDynamicGrid = $('#benefitCouponDynamicGrid').initializeKendoGrid( benefitCouponDynamicGridOpt ).data('kendoGrid');
    //$('#benefitCouponDynamicGrid').initializeKendoGrid( benefitCouponDynamicGridOpt ).cKendoGrid();
    var benefitCouponDynamicGrid = $('#'+gridId).initializeKendoGrid(benefitCouponDynamicGridOpt).cKendoGrid();
    // ------------------------------------------------------------------------
    // NO 항목 및 전체건수 Set
    // ------------------------------------------------------------------------
    benefitCouponDynamicGrid.bind('dataBound', function() {
      // 페이징 없음
      //var row_num = benefitCouponDynamicGridDs._total;
      var row_num = benefitCouponDynamicGridDs.data().length;
      $('#'+gridId+' tbody > tr .row-number').each(function(index){
        $(this).html(row_num);
        row_num--;
      });
      // ----------------------------------------------------------------------
      // 전체건수 Set
      // ----------------------------------------------------------------------
      //$('#totalCnt').text(exhibitGridDs._total);

      // ----------------------------------------------------------------------
      // 지급수량 숫자만 입력 설정
      // ----------------------------------------------------------------------
      $('#'+gridId + ' tbody > tr').each(function(index){
        var inputDom = $(this).find("input[name=couponCount]");
        fnInputDomValidationForNumber(inputDom);
      });

      // ----------------------------------------------------------------------
      // 총 당첨 수량 숫자만 입력 설정
      // ----------------------------------------------------------------------
      $('#'+gridId + ' tbody > tr').each(function(index){
        var inputDom = $(this).find("input[name=couponTotalCount]");
        fnInputDomValidationForNumber(inputDom);
      });
    });

    // ------------------------------------------------------------------------
    // 삭제 버튼 클릭
    // ------------------------------------------------------------------------
    $('#'+gridId).on("click", "button[kind=btnBenefitCouponDel]", function(e) {
      e.preventDefault();
      // 골라담기.대표상품 삭제 : 그리드에서만 삭제한다.
      fnKendoMessage( {
          message : fnGetLangData({key :'', nullMsg : '<div>삭제하시겠습니까?</div>' })
        , type    : 'confirm'
        , ok      : function(){
                      let dataItem = benefitCouponDynamicGrid.dataItem($(e.currentTarget).closest('tr'));
                      benefitCouponDynamicGridDs.remove(dataItem);
                      //fnMessage('', '제거되었습니다.', '');
                    }
      });
    });
  }

  // ==========================================================================
  // # 당첨자 혜택 - 적립금그리드 : [일반][설문][체험단]
  // ==========================================================================
  function fnInitBenefitPointGrid(surveyYn) {
    //console.log('# fnInitBenefitPointGrid Start');

    if(surveyYn == undefined){
        surveyYn = false;
    }
    var callUrl               = '';

    // 페이징없는 그리드

    benefitPointGridDs = fnGetDataSource({
      url      : '/admin/pm/event/selectfEventBenefitPointList?evEventId='+gbEvEventId
    });

    var bEditableGrid;
    bEditableGrid = false;

    benefitPointGridOpt = {
          dataSource  : benefitPointGridDs
        , noRecordMsg : '검색된 목록이 없습니다.'
        , navigatable : true
        , scrollable  : false
        //, height      : 'auto'
        , height      : '120'
        , selectable  : true
        , editable    : bEditableGrid
        , resizable   : true
        , autobind    : false
        // 임시 툴바 Start
        //,toolbar      : [
        //                  { name: 'create', text: '신규', className: "btn-point btn-s"}
        //                , { name: 'cSave',  text: '저장', imageClass: "k-i-update", className: 'k-custom-save btn-point btn-s'}
        //                , { name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }
        //                ]
        // 임시 툴바 End
        , columns     : [
                        //  {                                 title: 'No'             , width:  '70px', attributes:{ style:'text-align:center' }
                        //                                                        , template  : '<span class="row-number"></span>'
                        //  }
                        //,
                          { field : 'pmPointId'           , title : '적립금PK'      , width:  '10px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}, hidden : true}
                        , { field : 'displayPointNm'      , title : '적립금명'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'discountTpNm'        , title : '적립구분'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'discountDetl'        , title : '적립금액'        , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                    , template  : function(dataItem) {
                                                                                                    if (fnIsEmpty(dataItem.discountDetl) == false) {
                                                                                                      return fnNumberWithCommas(dataItem.discountDetl) + ' 원';
                                                                                                    }
                                                                                                    else {
                                                                                                      return '';
                                                                                                    }
                                                                                                  }
                          }
                        , { field : 'totalWinCnt'         , title : '총 당첨 수량'      , width:  '40px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}, hidden : surveyYn
                                                                                    , template  : function(dataItem) {
                                                                                                    var totalWinCntVal = '';
                                                                                                    if (fnIsEmpty(dataItem.totalWinCnt) == true) {
                                                                                                      totalWinCntVal = '';
                                                                                                    }
                                                                                                    else {
                                                                                                      totalWinCntVal = dataItem.totalWinCnt + '';
                                                                                                    }
                                                                                                    return '<input type="text" name="totalWinCount" class="comm-input marginR10" maxLength="5" size="10" style="text-align:right;" value='+ totalWinCntVal +' >';
                                                                                                  }
                         }
                        , { field : 'urErpOrganizationNm' , title : '분담조직'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                          }
                        //, { field : 'management'          , title : '관리'          , width:  '60px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                        //                                                            , template  : function(dataItem) {
                        //                                                                            return  '<div class="btn-area textCenter">'
                        //                                                                                  + '<button type="button" class="btn-red btn-s btnPointDel" kind="btnBenefitPointDel">삭제</button>'
                        //                                                                                  + '</div>';
                        //                                                                          }
                        //  }
                        ]
        //, rowTemplate : kendo.template($('#rowTemplate').html())
    };

    benefitPointGrid = $('#benefitPointGrid').initializeKendoGrid( benefitPointGridOpt ).data('kendoGrid');
    //$('#benefitPointGrid').initializeKendoGrid( benefitPointGridOpt ).cKendoGrid();
    // ------------------------------------------------------------------------
    // NO 항목 및 전체건수 Set
    // ------------------------------------------------------------------------
    benefitPointGrid.bind('dataBound', function() {
      // 페이징 없음
      //var row_num = benefitPointGridDs._total;
      var row_num = benefitPointGridDs.data().length;
      $('#benefitPointGrid tbody > tr .row-number').each(function(index){
        $(this).html(row_num);
        row_num--;
      });
      // ----------------------------------------------------------------------
      // 전체건수 Set
      // ----------------------------------------------------------------------
      //$('#totalCnt').text(exhibitGridDs._total);
      // ----------------------------------------------------------------------
      // 총 당첨 수량 숫자만 입력 설정
      // ----------------------------------------------------------------------
      $('#benefitPointGrid tbody > tr').each(function(index){
        var inputDom = $(this).find("input[name=totalWinCount]");
        fnInputDomValidationForNumber(inputDom);
      });
    });

    // ------------------------------------------------------------------------
    // 삭제 버튼 클릭
    // ------------------------------------------------------------------------
    $('#benefitPointGrid').on("click", "button[kind=btnBenefitPointDel]", function(e) {
      e.preventDefault();
      // 그리드에서만 삭제한다.
      fnKendoMessage({
          message : fnGetLangData({key :'', nullMsg : '<div>삭제하시겠습니까?</div>' })
        , type    : 'confirm'
        , ok      : function(){
                      let dataItem = benefitPointGrid.dataItem($(e.currentTarget).closest('tr'));
                      benefitPointGridDs.remove(dataItem);
                      //fnMessage('', '제거되었습니다.', '');
                    }
      });
    });
  }


  // ==========================================================================
  // # 당첨자 혜택 - 동적 적립금그리드 : [일반][설문][체험단]
  // ==========================================================================
  function fnInitBenefitPointDynamicGrid(gridId, gridIndex, evEventId) {
    //console.log('# fnInitBenefitPointDynamicGrid Start [', gridIndex, '][', evEventId, ']');

    var callUrl               = '';

    // 페이징없는 그리드

    var benefitPointDynamicGridDs = fnGetDataSource({
      url      : '/admin/pm/event/selectfEventBenefitPointList?evEventId='+gbEvEventId
    });

    var bEditableGrid;
    bEditableGrid = false;

    var benefitPointDynamicGridOpt = {
          dataSource  : benefitPointDynamicGridDs
        , noRecordMsg : '검색된 목록이 없습니다.'
        , navigatable : true
        , scrollable  : false
        //, height      : 'auto'
        , height      : '120'
        , selectable  : true
        , editable    : bEditableGrid
        , resizable   : true
        , autobind    : false
        // 임시 툴바 Start
        //,toolbar      : [
        //                  { name: 'create', text: '신규', className: "btn-point btn-s"}
        //                , { name: 'cSave',  text: '저장', imageClass: "k-i-update", className: 'k-custom-save btn-point btn-s'}
        //                , { name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }
        //                ]
        // 임시 툴바 End
        , columns     : [
                          { field : 'pmPointId'           , title : '적립금PK'      , width:  '10px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}, hidden : true}
                        , { field : 'displayPointNm'      , title : '적립금명'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'discountTpNm'        , title : '적립구분'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}}
                        , { field : 'discountDetl'        , title : '적립금액'        , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                    , template  : function(dataItem) {
                                                                                                    if (fnIsEmpty(dataItem.discountDetl) == false) {
                                                                                                      return fnNumberWithCommas(dataItem.discountDetl) + ' 원';
                                                                                                    }
                                                                                                    else {
                                                                                                      return '';
                                                                                                    }
                                                                                                  }
                          }
//                        , { field : 'totalWinCnt'         , title : '총 당첨 수량'      , width:  '40px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
//                                                                                    , template  : function(dataItem) {
//                                                                                                    var totalWinCntVal = '';
//                                                                                                    if (fnIsEmpty(dataItem.totalWinCnt) == true) {
//                                                                                                      totalWinCntVal = '';
//                                                                                                    }
//                                                                                                    else {
//                                                                                                      totalWinCntVal = dataItem.totalWinCnt + '';
//                                                                                                    }
//                                                                                                    return '<input type="text" name="totalWinCount" class="comm-input marginR10" maxLength="2" size="5" style="text-align:right;" value='+ totalWinCntVal +' >';
//                                                                                                  }
//                         }
                        , { field : 'urErpOrganizationNm' , title : '분담조직'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                          }
                        //, { field : 'management'          , title : '관리'          , width:  '60px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                        //                                                            , template  : function(dataItem) {
                        //                                                                            return  '<div class="btn-area textCenter">'
                        //                                                                                  + '<button type="button" class="btn-red btn-s btnPointDel" kind="btnBenefitPointDel">삭제</button>'
                        //                                                                                  + '</div>';
                        //                                                                          }
                        //  }
                        ]
        //, rowTemplate : kendo.template($('#rowTemplate').html())
    };

    // Grid
    $('#'+gridId).initializeKendoGrid(benefitPointDynamicGridOpt).data('kendoGrid');
    //benefitPointDynamicGrid = $('#benefitPointDynamicGrid').initializeKendoGrid( benefitPointDynamicGridOpt ).data('kendoGrid');
    //$('#benefitPointDynamicGrid').initializeKendoGrid( benefitPointDynamicGridOpt ).cKendoGrid();
    var benefitPointDynamicGrid = $('#'+gridId).initializeKendoGrid(benefitPointDynamicGridOpt).cKendoGrid();

    //$('#benefitPointDynamicGrid').initializeKendoGrid( benefitPointDynamicGridOpt ).cKendoGrid();
    // ------------------------------------------------------------------------
    // NO 항목 및 전체건수 Set
    // ------------------------------------------------------------------------
    benefitPointDynamicGrid.bind('dataBound', function() {
      // 페이징 없음
      //var row_num = benefitPointDynamicGridDs._total;
      var row_num = benefitPointDynamicGridDs.data().length;
      $('#benefitPointDynamicGrid tbody > tr .row-number').each(function(index){
        $(this).html(row_num);
        row_num--;
      });
      // ----------------------------------------------------------------------
      // 전체건수 Set
      // ----------------------------------------------------------------------
      //$('#totalCnt').text(exhibitGridDs._total);

      // ----------------------------------------------------------------------
      // 총 당첨 수량 숫자만 입력 설정
      // ----------------------------------------------------------------------
      $('#'+gridId + ' tbody > tr').each(function(index){
        var inputDom = $(this).find("input[name=totalWinCount]");
        fnInputDomValidationForNumber(inputDom);
      });

    });

    // ------------------------------------------------------------------------
    // 삭제 버튼 클릭
    // ------------------------------------------------------------------------
    $('#'+gridId).on("click", "button[kind=btnBenefitPointDel]", function(e) {
      e.preventDefault();
      // 그리드에서만 삭제한다.
      fnKendoMessage( {
          message : fnGetLangData({key :'', nullMsg : '<div>삭제하시겠습니까?</div>' })
        , type    : 'confirm'
        , ok      : function(){
                      let dataItem = benefitPointDynamicGrid.dataItem($(e.currentTarget).closest('tr'));
                      benefitPointDynamicGridDs.remove(dataItem);
                      //fnMessage('', '제거되었습니다.', '');
                    }
      });
    });
  }

  // ==========================================================================
  // # 체험단 - 체험단상품그리드 (단건만 존재) : [체험단]
  // ==========================================================================
  function fnInitExperienceGrid() {
    //console.log('# fnInitExperienceGrid Start');

    var callUrl               = '';

    // 페이징없는 그리드
    experienceGridDs = fnGetDataSource({
      url      : ''   // 조회없음
    });

    var bEditableGrid;
    bEditableGrid = false;

    experienceGridOpt = {
          dataSource  : experienceGridDs
        , noRecordMsg : '검색된 목록이 없습니다.'
        , navigatable : true
        , scrollable  : false
        //, height      : 'auto'
        , height      : '120'
        , selectable  : true
        , editable    : bEditableGrid
        , resizable   : true
        , autobind    : false
        // 임시 툴바 Start
        //,toolbar      : [
        //                  { name: 'create', text: '신규', className: "btn-point btn-s"}
        //                , { name: 'cSave',  text: '저장', imageClass: "k-i-update", className: 'k-custom-save btn-point btn-s'}
        //                , { name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }
        //                ]
        // 임시 툴바 End
        , columns     : [
                          { field : 'goodsTpNm'           , title : '상품유형'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}}
                        , { field : 'ilGoodsId'           , title : '상품코드'      , width:  '80px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}}
                        , { field : 'goodsNm'             , title : '상품명'        , width: '300px', attributes : {style : 'text-align:left;'  }, editable:function (dataItem) {return bEditableGrid;}
                                                                                    , template  : function(dataItem) {
                                                                                                    let imageUrl = dataItem.goodsImagePath ? publicStorageUrl + dataItem.goodsImagePath : '/contents/images/noimg.png';
                                                                                                    return '<img src="' + imageUrl + '" width="50" height="50" align="left" /><BR>&nbsp;&nbsp;' + dataItem.goodsNm ;
                                                                                                  }
                          }
                        , { field : 'warehouseNm'         , title : '출고처명'      , width: '200px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}}
                        , { field : 'shippingTemplateName', title : '배송정책'      , width: '200px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}}
                        , { field : 'standardPrice'       , title : '원가'          , width:  '70px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}, format: '{0:\#\#,\#}'
                                                                                    , template  : function(dataItem) {
                                                                                                    return fnNumberWithCommas(dataItem.standardPrice);
                                                                                                  }
                          }
                        , { field : 'recommendedPrice'    , title : '정상가'        , width:  '70px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return bEditableGrid;}, format: '{0:\#\#,\#}'
                                                                                    , template  : function(dataItem) {
                                                                                                    return fnNumberWithCommas(dataItem.recommendedPrice);
                                                                                                  }
                          }
                        , { field : 'management'          , title : '관리'          , width:  '70px', attributes : {style : 'text-align:center;'}, editable:function (dataItem) {return false;}
                                                                                    , template  : function(dataItem) {
                                                                                                    return  '<div class="btn-area textCenter">'
                                                                                                          + '<button type="button" name="btnExperienceGoodsDel" class="btn-red btn-s" kind="btnExperienceGoodsDel">삭제</button>'
                                                                                                          + '</div>';
                                                                                                  }
                          }
                        ]
        //, rowTemplate : kendo.template($('#rowTemplate').html())
    };

    experienceGrid = $('#experienceGrid').initializeKendoGrid( experienceGridOpt ).data('kendoGrid');
    //$('#experienceGrid').initializeKendoGrid( experienceGridOpt ).cKendoGrid();
    // ------------------------------------------------------------------------
    // NO 항목 및 전체건수 Set
    // ------------------------------------------------------------------------
    experienceGrid.bind('dataBound', function() {
      // 페이징 없음
      //var row_num = experienceGridDs._total;
      var row_num = experienceGridDs.data().length;
      $('#experienceGrid tbody > tr .row-number').each(function(index){
        $(this).html(row_num);
        row_num--;
      });
      // ----------------------------------------------------------------------
      // 전체건수 Set
      // ----------------------------------------------------------------------
      //$('#totalCnt').text(exhibitGridDs._total);
    });

    // ------------------------------------------------------------------------
    // 삭제 버튼 클릭
    // ------------------------------------------------------------------------
    $('#experienceGrid').on("click", "button[kind=btnExperienceGoodsDel]", function(e) {
      e.preventDefault();
      // 골라담기.대표상품 삭제 : 그리드에서만 삭제한다.
      fnKendoMessage({
          message : fnGetLangData({key :'', nullMsg : '<div>삭제하시겠습니까?</div>' })
        , type    : 'confirm'
        , ok      : function(){
                      let dataItem = experienceGrid.dataItem($(e.currentTarget).closest('tr'));
                      experienceGridDs.remove(dataItem);
                      //fnMessage('', '제거되었습니다.', '');
                    }
      });
    });
  }



  // ==========================================================================
  // # 다건 - 당첨자 혜택 - 적립금그리드 : [?][?][?]
  // ==========================================================================
  function fnMakeBenefitCouponGrid(id) {
    //console.log('전시 쿠폰 그리드 생성', id);

  }

  // ==========================================================================
  // # 다건 - 당첨자 혜택 - 적립금그리드 : [?][?][?]
  // ==========================================================================
  function fnMakeBenefitPointGrid(id) {
    //console.log('전시 적립금 그리드 생성', id);

  }






