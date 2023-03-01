/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 품목가격 승인요청내역리스트
 *
 * @ 실제 처리는 approvalItemPriceController.js, approvalItemPrice.html
 *
 * @ 수정일			수정자		수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.02.17		박승현		최초생성
 * @
 *
 ******************************************************************************/

'use strict';

	// --------------------------------------------------------------------------
	// 변수 선언 - approvalItemPriceController.js에서 사용할 전역변수 정의(중복 주의)
	// --------------------------------------------------------------------------
	var pageId      = 'approvalItemPriceRequest';

	// --------------------------------------------------------------------------
	//html 컨텐츠 가지고 오기
	getContentsHtmlByPath("/contents/views/admin/approval/item/approvalItemPrice.html", function (){
		//html 컨텐츠 가지고 온 이후 callback
		//javascript import
		importScript("/js/controllers/admin/approval/item/approvalItemPriceController.js");
	});