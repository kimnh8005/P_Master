/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 클레임 관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.07		김승우          최초생성
 * **/
"use strict";

var claimSubmitUtil = {
  search: function() {
      var grid = $("#grid").data("kendoGrid");
      var gridDs = grid.dataSource;
      var form = $("#searchForm");
      var data = form.formSerialize(true);
      var code = claimFncs.getCode("codeSearch");

      data.code = code;

      console.log("원래 data:", data);
      data = {"categoryType":""}; //url 호출 위해 임시

      var query = {
          page: 1,
          pageSize: PAGE_SIZE,
          filterLength: fnSearchData(data).length,
          filter: {
              filters: fnSearchData(data)
          }
      };

      gridDs.query(query);
  },
  clear: function() {
      var form = $("#searchForm");
      form.formClear(true);

      $(".date-controller button").attr("fb-btn-active", false);
  }
}

var claimSearchUtil = {
  // 기간 검색
  dateSearch: function() {
      fnKendoDatePicker({
        id: 'startDate',
        format: 'yyyy-MM-dd',

      });

      fnKendoDatePicker({
          id: 'endDate',
          format: 'yyyy-MM-dd',
          btnStyle: true,
          btnStartId: 'startDate',
          btnEndId: 'endDate',
          nextDate: true,
          minusCheck: true,
      });
  },
  // 단일조건 조건
  searchCondition: function() {
    fnKendoDropDownList(
      {
          id : "searchType",
          data : [
                  {"CODE" : "orderNo", "NAME" : "주문번호"},
                  {"CODE" : "orderName", "NAME" : "주문자명"},
                  {"CODE" : "orderId", "NAME" : "주문자ID"},
                  {"CODE" : "", "NAME" : "상품코드"},
                  {"CODE" : "", "NAME" : "품목코드"},
                  {"CODE" : "", "NAME" : "품목바코드"},
          ],
          valueField : "CODE",
          textField : "NAME",
          blank : "선택해주세요."
      });
  },
  // 복수조건/단일조건 탭 선택
  tabSelector: function() {
    fnTagMkRadio({
      id: 'selectConditionType',
      tagId: 'selectConditionType',
      chkVal: 'multiSection',
      data: [{
          CODE: "multiSection",
          NAME: "복수조건 검색",
      },{
          CODE: "singleSection",
          NAME: "단일조건 검색",
      }],
      change: function(e) {
        const $tabs = $("tr[data-tab]");
        const tab = $(e.target).val();

        $tabs.hide();
        $("[data-tab="+ tab +"]").show();
      }
    });
  }
}

var claimFncs = {
  getCode: function(id) {
    let input = $("#" + id).val();
    let query = "";

    if( input && input.length ) {

      input = input.split(",");

      query = input.filter(function(i){ 
        return i && i.length;
      }).join(",");
    }

    return query;
  },
}