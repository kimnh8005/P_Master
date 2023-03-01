﻿/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 프로모션 체험단 이벤트리스트
 *
 * @ 실제 처리는 eventListController.js, eventList.html
 *
 * @ 수정일        수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.01.15    dgyoun        최초생성
 * @
 ******************************************************************************/
'use strict';

  // --------------------------------------------------------------------------
  // 변수 선언 - eventListController.js에서 사용할 전역변수 정의(중복 주의)
  // --------------------------------------------------------------------------
  var gbEventTp = 'EVENT_TP.EXPERIENCE';
  var pageId = 'eventExprncList';

  // --------------------------------------------------------------------------
  //html 컨텐츠 가지고 오기
  getContentsHtmlByPath('/contents/views/admin/pm/event/eventList.html', function (){
    // html 컨텐츠 가지고 온 이후 callback
    // javascript import
    importScript('/js/controllers/admin/pm/event/eventListController.js');
  });