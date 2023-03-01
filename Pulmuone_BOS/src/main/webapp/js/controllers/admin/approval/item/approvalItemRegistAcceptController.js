/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 품목등록 승인관리리스트
 *
 * @ 실제 처리는 approvalItemRegistController.js, approvalItemRegist.html
 *
 * @ 수정일			수정자		수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.02.17		박승현		최초생성
 * @
 *
 ******************************************************************************/

'use strict';

	// --------------------------------------------------------------------------
	// 변수 선언 - approvalItemRegistController.js에서 사용할 전역변수 정의(중복 주의)
	// --------------------------------------------------------------------------
	var pageId      = 'approvalItemRegistAccept';

	// --------------------------------------------------------------------------
	//html 컨텐츠 가지고 오기
	getContentsHtmlByPath("/contents/views/admin/approval/item/approvalItemRegist.html", function (){
		//html 컨텐츠 가지고 온 이후 callback
		//javascript import
		importScript("/js/controllers/admin/approval/item/approvalItemRegistController.js");
	});