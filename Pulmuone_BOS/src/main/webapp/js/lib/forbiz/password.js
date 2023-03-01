/**
 * 비밀번호 검증 
 * @param {Object} obj   - input Tag id
 * @param {Object} objNm - input Tag Title
 */
function fnPwValid(obj, objNm){
	
	var $obj = $('#'+obj);
	
    if($obj.val() == ''){
    	return objNm + ' ' + '입력해주세요.';
    }else{
    	try{
    		//어드민 로그인 페이지
    		$obj.closest('div').removeClass('area-error');	
    	}catch(e){
    		alert('error : ' + e);
    	}
    	
    	var checkPass = fnCheckPasswd($obj.val());
    	if(checkPass != '0'){
    		return objNm + ' ' + checkPass;
    	}
    	
    	if(obj == 'nPwChk2'){
    		if( $('#nPw2').val() != $obj.val()){
    			return '비밀번호가 일치하지 않습니다. 다시 입력해 주세요.';
        	}
    	}
    	
    	if(obj == 'nPwChk3'){
    		if( $('#nPw3').val() != $obj.val()){
    			return '비밀번호가 일치하지 않습니다. 다시 입력해 주세요.';
        	}
    	}
    }
    return 'success';
}

function fnCheckPasswd(str){
	
	var pw = str;
	//
	if(!fnCheckPasswordLength(pw)){
		return "8자 이상 입력해주세요.";
	}
	//비밀번호 공백체크
	if(fnCheckPasswordSpace(pw)){
		return "비밀번호에 공백이 있습니다.";
	}
	//영문,숫자,특수문자를 혼합체크
	if(!fnCheckMixPwdChar(pw)){
		return "영문, 숫자, 특수문자 3가지를 조합해주세요.";
	}
	
	return '0';
}

//8자리 ~ 20자리 
function fnCheckPasswordLength(pw){
	if(pw.length < 8 || pw.length > 20){
		return false;
	}
	return true;
}
	
//비밀번호 공백체크
function fnCheckPasswordSpace(pw){
	var spacePattern = /[\s]/g;
	var str = $.trim(pw);
	
	console.log( '공백체크 : ' + spacePattern.test(str) );
	return spacePattern.test(str);
	
	/*
	if(spacePattern.test(str) != -1){
		return false;
	}
	return true;
	*/
}
	
//영문,숫자, 특수문자를 혼합체크
function fnCheckMixPwdChar(pw){
	var num = pw.search(/[0-9]/g);
	var eng = pw.search(/[a-z]/ig);
	var spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);
	
	if(num < 0 || eng < 0 || spe < 0 ){
		return false;
	}
	return true;
}
	
//연속된 3개의 문자 사용불가
function fnCheckContinuous3Character(str) {
	if(typeof str !="string"){
		return false;
	}
	
	var bytes = []; // char codes
	for (var i = 0; i < str.length; ++i) {
		var code = str.charCodeAt(i);
		bytes = bytes.concat([code]);
	}
	
	var b = bytes;
	var p = str.length;
	// 연속된 3개의 문자 사용불가 (오름차순)
	for (var i = 0; i < ((p * 2) / 3); i++) {
		var b1 = b[i] + 1;
		var b2 = b[i + 1];
		var b3 = b[i + 1] + 1;
		var b4 = b[i + 2];
	
		if ((b1 == b2) && (b3 == b4)) {
			return false;
		}else {
			continue;
		}
	}
	// 연속된 3개의 문자 사용불가 (내림차순)
	for (var i = 0; i < ((p * 2) / 3); i++) {
		var b1 = b[i + 1] + 1;
		var b2 = b[i + 2] + 2;

		if ((b[i] == b1) && (b[i] == b2)) {
			return false;
		} else {
			continue;
		}
	}
	return true;
}
	
//일련숫자 또는 알파벳 순서대로 3자이상 사용하는 비밀번호는 사용불가
function  fnCheckDuplicate3Character(str) {
	var p = d.length();
	if(typeof str !="string"){
		return false;
	}
	
	var bytes = []; // char codes
	for (var i = 0; i < str.length; ++i) {
		var code = str.charCodeAt(i);
		bytes = bytes.concat([code]);
	}
	
	var b = bytes;
	for (var i = 0; i < ((p * 2) / 3); i++) {
		var b1 = b[i + 1];
		var b2 = b[i + 2];

		if ((b[i] == b1) && (b[i] == b2)) {
			return true;
		} else {
			continue;
		}
	}
	return false;
}