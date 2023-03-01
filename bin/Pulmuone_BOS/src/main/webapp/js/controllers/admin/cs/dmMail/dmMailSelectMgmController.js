/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 프로모션 골라담기 기획전리스트
 *
 * @ 실제 처리는 exhibitSelectMgmController.js, exhibitSelectMgm.html
 *
 * @ 수정일        수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.05.24    원종한        최초생성
 * @
 ******************************************************************************/
'use strict';

  // --------------------------------------------------------------------------
  // 변수 선언 - exhibitMgmController.js에서 사용할 전역변수 정의(중복 주의)
  // --------------------------------------------------------------------------
  var gbExhibitTp = "EXHIBIT_TP.SELECT";
  var pageId      = 'exhibitSelectMgm';

  // --------------------------------------------------------------------------
  //html 컨텐츠 가지고 오기
  getContentsHtmlByPath("/contents/views/admin/pm/exhibit/exhibitMgm.html", function (){
    // html 컨텐츠 가지고 온 이후 callback
    // javascript import
    importScript("/js/controllers/admin/pm/exhibit/exhibitMgmController.js");
  });