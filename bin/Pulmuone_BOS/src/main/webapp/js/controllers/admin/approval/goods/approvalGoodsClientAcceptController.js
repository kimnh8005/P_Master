/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 거래처 상품수정 승인관리리스트
 *
 * @ 실제 처리는 approvalGoodsClientController.js, approvalGoodsClient.html
 *
 * @ 수정일			수정자		수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.04.09		최윤석		최초생성
 * @
 *
 ******************************************************************************/

'use strict';

	// --------------------------------------------------------------------------
	// 변수 선언 - approvalGoodsRegistController.js에서 사용할 전역변수 정의(중복 주의)
	// --------------------------------------------------------------------------
	var pageId      = 'approvalGoodsClientAccept';

	// --------------------------------------------------------------------------
	//html 컨텐츠 가지고 오기
	getContentsHtmlByPath("/contents/views/admin/approval/goods/approvalGoodsClient.html", function (){
		//html 컨텐츠 가지고 온 이후 callback
		//javascript import
		importScript("/js/controllers/admin/approval/goods/approvalGoodsClientController.js");
	});