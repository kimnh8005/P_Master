/**-----------------------------------------------------------------------------
 * system 			 : 사용자관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2017.01.10		추상구          최초생성
 * @
 * **/


/**
 * 
 * 연동코드를 입력하여 코드에 대한 다국어를 리턴한다.
 *  
 * @param {Object} extCode 
 */
function fnExterCodeValue( extCode ){
	var codeName = '';
	
	if( extCode == 'SS01'){ 
		codeName = fnGetLangData({key :"6613",nullMsg :'매장정산 정보' });
	}else if( extCode == 'OD10'){ 
		codeName = fnGetLangData({key :"6614",nullMsg :'입점사 판매자료' });
	}else if( extCode == 'RW01'){ 
		codeName = fnGetLangData({key :"6606",nullMsg :'맴버스포인트 잔액 정보' });
	}else if( extCode == 'RW02'){ 
		codeName = fnGetLangData({key :"6607",nullMsg :'맴버스포인트 사용 요청 정보' });
	}else if( extCode == 'RW03'){ 
		codeName = fnGetLangData({key :"6608",nullMsg :'맴버스포인트 적립/사용 요청 정보' });
	}else if( extCode == 'RW04'){ 
		codeName = fnGetLangData({key :"6609",nullMsg :'맴버스포인트 사용취소 요청 정보' });
	}else if( extCode == 'RW05'){ 
		codeName = fnGetLangData({key :"6610",nullMsg :'맴버스포인트 적립/사용 이력 정보' });
	}else if( extCode == 'OD01'){ 
		codeName = fnGetLangData({key :"6611",nullMsg :'매도 정보' });
	}else if( extCode == 'OD02'){ 
		codeName = fnGetLangData({key :"6612",nullMsg :'매출 정보' });
	}else if( extCode == 'OD03'){ 
		codeName = fnGetLangData({key :"6618",nullMsg :'매장픽업 주문 정보' });
	}else if( extCode == 'OD05'){ 
		codeName = fnGetLangData({key :"6620",nullMsg :'매장픽업 주문상품 준비완료 및 픽업완료 정보' });
	}else if( extCode == 'OD06'){ 
		codeName = fnGetLangData({key :"6619",nullMsg :'매장픽업 주문 취소' });
	}else if( extCode == 'OD07'){ 
		codeName = fnGetLangData({key :"6621",nullMsg :'매장픽업 주문상품 취소(반품) 정보' });
	}else if( extCode == 'OD08'){ 
		codeName = fnGetLangData({key :"6748",nullMsg :'매장픽업 구매확정일자' });
	}else if( extCode == 'OD11'){ 
		codeName = fnGetLangData({key :"6615",nullMsg :'입점사 미수금 (수수료) 자료' }); 
	}else if( extCode == 'OD12'){ 
		codeName = fnGetLangData({key :"6616",nullMsg :'입점업체 코드 기준자료' });
	}else if( extCode == 'OD13'){ 
		codeName = fnGetLangData({key :"6617",nullMsg :'입점사별 매입정보 (세금계산서역발행)' });
	}else if( extCode == 'OD14'){ 
		codeName = fnGetLangData({key :"6617",nullMsg :'매출거래처 등록' });
	}else if( extCode == 'UR01'){ 
		codeName = fnGetLangData({key :"6602",nullMsg :'종합몰 회원가입 정보' }); 
	}else if( extCode == 'UR02'){ 
		codeName = fnGetLangData({key :"6603",nullMsg :'종합몰 회원정보변경 정보' });
	}else if( extCode == 'UR03'){ 
		codeName = fnGetLangData({key :"6604",nullMsg :'매장 회원정보변경 정보' });
	}else if( extCode == 'UR04'){ 
		codeName = fnGetLangData({key :"6605",nullMsg :'매장 회원가입 정보' });
	}else if( extCode == 'UR05'){ 
		codeName = fnGetLangData({key :"6622",nullMsg :'매장 정보' });
	}else if( extCode == 'UR06'){ 
		codeName = fnGetLangData({key :"",nullMsg :'자동로그인' });
	}else if( extCode == 'UR07'){ 
		codeName = fnGetLangData({key :"6624",nullMsg :'특정매장 상품보유 리스트 정보' });
	}else if( extCode == 'UR08'){ 
		codeName = fnGetLangData({key :"",nullMsg :'POS 변경된 회원 관리매장 연동' });
	}else if( extCode == 'IL01'){ 
		codeName = fnGetLangData({key :"6625",nullMsg :'개별 상품 정보' });
	}else if( extCode == 'IL02'){ 
		codeName = fnGetLangData({key :"6626",nullMsg :'상품 재고/유통기한 정보' });
	}else if( extCode == 'CS01'){ 
		codeName = fnGetLangData({key :"6743",nullMsg :'QNA 답변' });

		
	}else if( extCode == 'OD15'){ 
		codeName = fnGetLangData({key :"XXXX",nullMsg :'매장배송 구매확정 주문정보 - 택배' });
	}else if( extCode == 'OD16'){ 
		codeName = fnGetLangData({key :"XXXX",nullMsg :'매장택배 구매확정 후 반품정보 - 택배' });
		
	}else if( extCode == 'OD17'){ 
		codeName = fnGetLangData({key :"XXXX",nullMsg :'매장배송 구매확정 주문정보  - 직배' });		
	}else if( extCode == 'OD18'){ 
		codeName = fnGetLangData({key :"XXXX",nullMsg :'매장택배 구매확정 후 반품정보 - 직배' });		
		
	}else if( extCode == 'OD19'){ 
		codeName = fnGetLangData({key :"XXXX",nullMsg :'매장픽업 구매확정 주문정보' });
	}else if( extCode == 'OD20'){ 
		codeName = fnGetLangData({key :"XXXX",nullMsg :'매장픽업 구매확정 후 반품정보' });				
	}else if( extCode == 'OD21'){ 
		codeName = fnGetLangData({key :"XXXX",nullMsg :'매장배송 구매확정 주문정보 - 자체택배' });
	}else if( extCode == 'OD22'){ 
		codeName = fnGetLangData({key :"XXXX",nullMsg :'매장배송 구매확정 후 반품정보 - 자체택배' });				
		
		
		
	}else{
		codeName = 'Add Code';
	} 						
	
	return codeName;
}    

