/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 승인관리 골라담기(균일가) 승인요청내역리스트
 *
 * @ 실제 처리는 approvalExhibitController.js, approvalExhibit.html
 *
 * @ 수정일			수정자		수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.02.08		박승현		최초생성
 * @
 *
 ******************************************************************************/
'use strict';

  // --------------------------------------------------------------------------
  // 변수 선언 - approvalExhibitController.js에서 사용할 전역변수 정의(중복 주의)
  // --------------------------------------------------------------------------
  var gbExhibitTp = "EXHIBIT_TP.SELECT";
  var pageId      = 'approvalExhibitSelectRequest';

  // --------------------------------------------------------------------------
  //html 컨텐츠 가지고 오기
  getContentsHtmlByPath("/contents/views/admin/approval/exhibit/approvalExhibit.html", function (){
    // html 컨텐츠 가지고 온 이후 callback
    // javascript import
    importScript("/js/controllers/admin/approval/exhibit/approvalExhibitController.js");
  });