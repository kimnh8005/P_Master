/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 클레임 상세팝업 > 팝업
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.13		최윤지          최초생성
 * **/

var claimMgmPopupUtil = {
  // fnkendoinputpoup options
  options: {
	  nonCardBankBookClaimPopup: {
			id     : 'nonCardBankBookClaimPopup',
			title  : '비인증신용카드결제',
			src    : '#/nonCardBankBookClaimPopup',
			param  : null,
			width  : '500px',
			height : '500px',
			scrollable : "no",
			success: function(id, data){
				console.log(id, data);
			}
	  	}
  	},
	  open: function(id, param, success) {
	    const opt =  $.extend({}, this.options[id]);
	    if( param ) {
	      opt.param = param;
	    }
	    opt.success = success;
	    fnKendoPopup(opt);
	  }
}