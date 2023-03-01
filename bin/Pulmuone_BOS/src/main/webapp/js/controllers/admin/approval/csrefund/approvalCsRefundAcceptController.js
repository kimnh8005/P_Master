'use strict';

  // --------------------------------------------------------------------------
  // 변수 선언 - approvalCsRefundController.js에서 사용할 전역변수 정의(중복 주의)
  // --------------------------------------------------------------------------
  var pageId      = 'approvalCsRefundAccept';

  // --------------------------------------------------------------------------
  //html 컨텐츠 가지고 오기
  getContentsHtmlByPath("/contents/views/admin/approval/csrefund/approvalCsRefund.html", function (){
    // html 컨텐츠 가지고 온 이후 callback
    // javascript import
    importScript("/js/controllers/admin/approval/csrefund/approvalCsRefundController.js");
  });