/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 상품등록 승인관리리스트
 *
 * @ 실제 처리는 approvalGoodsRegistController.js, approvalGoodsRegist.html
 *
 * @ 수정일			수정자		수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.02.17		박승현		최초생성
 * @
 *
 ******************************************************************************/

'use strict';

	// --------------------------------------------------------------------------
	// 변수 선언 - approvalGoodsRegistController.js에서 사용할 전역변수 정의(중복 주의)
	// --------------------------------------------------------------------------
	var pageId      = 'approvalGoodsRegistAccept';

	// --------------------------------------------------------------------------
	//html 컨텐츠 가지고 오기
	getContentsHtmlByPath("/contents/views/admin/approval/goods/approvalGoodsRegist.html", function (){
		//html 컨텐츠 가지고 온 이후 callback
		//javascript import
		importScript("/js/controllers/admin/approval/goods/approvalGoodsRegistController.js");
	});