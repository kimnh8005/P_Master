/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 프로모션 일반 기획전리스트
 *
 * @ 실제 처리는 exhibitListController.js, exhibitList.html
 *
 * @ 수정일        수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2020.12.15    dgyoun        최초생성
 * @
 ******************************************************************************/
'use strict';

  // --------------------------------------------------------------------------
  // 변수 선언 - exhibitListController.js에서 사용할 전역변수 정의(중복 주의)
  // --------------------------------------------------------------------------
  var gbExhibitTp = "EXHIBIT_TP.NORMAL";
  var pageId      = 'exhibitNormalList';

  // --------------------------------------------------------------------------
  //html 컨텐츠 가지고 오기
  getContentsHtmlByPath("/contents/views/admin/pm/exhibit/exhibitList.html", function (){
    // html 컨텐츠 가지고 온 이후 callback
    // javascript import
    importScript("/js/controllers/admin/pm/exhibit/exhibitListController.js");
  });