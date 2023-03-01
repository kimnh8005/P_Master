/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 증빙문서 관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.07		김승우          최초생성
 * **/
"use strict";

var pgSubmitUtil = {
  search: function() {
      var grid = $("#grid").data("kendoGrid");
      var gridDs = grid.dataSource;
      var form = $("#searchForm");
      var data = form.formSerialize(true);
      var code = pgFncs.getCode("codeSearch");

      //data.code = code;

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
      $('button[data-id="fnDateBtn3"]').attr("fb-btn-active", true);
  },
}

var pgSearchUtil = {
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

  searchCondition: function() {
    fnKendoDropDownList(
      {
          id : "searchType",
          data : [
                  {"CODE" : "odid"              , "NAME" : "주문번호"},
                  {"CODE" : "cashReceiptNo"     , "NAME" : "증빙번호"},
                  {"CODE" : "cashReceiptAuthNo" , "NAME" : "승인번호"},
          ],
          valueField : "CODE",
          textField : "NAME"
      });
      fnKendoDropDownList(
          {
              id : "dateSearch",
              data : [
                  {"CODE" : "orderDate"     , "NAME" : "주문일자"},
                  {"CODE" : "paymentDate"   , "NAME" : "결제일자"},
              ],
              valueField : "CODE",
              textField : "NAME"
          });
      fnKendoDropDownList(
          {
              id : "cashReceiptType",
              data : [
                  {"CODE" : "notIssued" , "NAME" : "미발급"},
                  {"CODE" : "proof"     , "NAME" : "지출증빙"},
                  {"CODE" : "deduction" , "NAME" : "소득공제"},
                  {"CODE" : "user"      , "NAME" : "사용자발급"},
              ],
              valueField : "CODE",
              textField : "NAME",
              blank : "전체"
          });
      fnKendoDropDownList(
          {
              id : "pgType",
              data : [
                  {"CODE" : "kcp"       , "NAME" : "KCP"},
                  {"CODE" : "inicis"    , "NAME" : "이니시스"},
              ],
              valueField : "CODE",
              textField : "NAME",
              blank : "전체"
          });
      fnKendoDropDownList(
          {
              id : "issueType",
              data : [
                  {"CODE" : "notIssued"     , "NAME" : "미발급"},
                  {"CODE" : "issueCompleted", "NAME" : "발급완료"},
              ],
              valueField : "CODE",
              textField : "NAME",
              blank : "전체"
          });
      fnKendoDropDownList(
          {
              id : "payType",
              data : [
                  {"CODE" : "virtualBank"   , "NAME" : "무통장입금(가상계좌)"},
                  {"CODE" : "bank"          , "NAME" : "실시간계좌이체"},
              ],
              valueField : "CODE",
              textField : "NAME",
              blank : "전체"
          });
  },
}

function pgPopup(param, onSuccess) {
    fnKendoPopup({
      id: 'cashReceiptIssuedPopup',
      title: '현금영수증 발급',
      src: '#/cashReceiptIssuedPopup',
      param: param ? param : {},
      width: '500px',
      height: '220px',
      success: onSuccess ? onSuccess : null,
    });
}

var pgFncs = {
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
  }
}