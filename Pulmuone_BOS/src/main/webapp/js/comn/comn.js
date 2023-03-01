/**-----------------------------------------------------------------------------
 * system 			 : 사용자관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2017.01.10		추상구          최초생성
 * @
 * **/
var UPLOAD_IMAGE_SIZE = 5 * 1024 * 1024;


// 브라우저 및 버전을 구하기 위한 변수들.
var USER_AGENT    = navigator.userAgent.toLowerCase(),
    USER_APP_NAME = navigator.appName,
    USER_BROWSER;

var MY_STORE_URL = '';  // comm.layout.min.js 의 fnInitLayout 에서 세팅

var ALERT_MESSAGES = {
	'ONLY_NUM': '숫자만 입력 가능합니다.',
	'NOT_NUM': '올바른 숫자가 아닙니다.',
};

/**
 *  좌측메뉴를 보여줄지 결정하는 메뉴
 *  @param argv1 : location.hash
 *  @param argv2 : only url
 */
function fnIsLeftMenu( argv1, argv2 ){
	var flag = true;

	if( (argv1 == 'dashboardTot' || argv2 == 'dashboardTot')
			|| (argv1 == 'ecprocList'    || argv2 == 'ecprocList')
			|| (argv1 == 'dashboardComp' || argv2 == 'dashboardComp') ){
		flag = false;
	}

	return flag;
}

/**
 * 허용 Tag 처리
 * @param str
 *
 */
function fnTagConvert( str ){

	try{

		str = str.replace(/&amp;/g, '&')
			     .replace(/&gt;/g, '>')
			     .replace(/&lt;/g, '<')
			     .replace(/&quot;/g, '"')
			     .replace(/&#39;/g, '\'')
			     .replace(/(\n|\r\n)/g, '<br>');

		//--------------- XSS 방지 스크립트 처리 ----------------------------------------------
		//str.toLowerCase()
		str = str.replace(/javascript/g	, 'x-javascript')
		         .replace(/script/g		, 'x-script')
		         // .replace(/iframe/g		, 'x-iframe') // 유투브 퍼가기 소스 적용
		         .replace(/document/g	, 'x-document')
		         .replace(/vbscript/g	, 'x-vbscript')
		         .replace(/applet/g		, 'x-applet')
		         // .replace(/embed/g		, 'x-embed') // 유투브 퍼가기 소스 적용
		         .replace(/object/g		, 'x-object')
		         // .replace(/frame/g		, 'x-frame') // 유투브 퍼가기 소스 적용
		         .replace(/grameset/g	, 'x-grameset')
		         .replace(/layer/g		, 'x-layer')
		         .replace(/bgsound/g	, 'x-bgsound')
		         .replace(/alert/g		, 'x-alert')
		         .replace(/onblur/g		, 'x-onblur')
		         .replace(/onchange/g	, 'x-onchange')
		         .replace(/onclick/g	, 'x-onclick')
		         .replace(/ondblclick/g	, 'x-ondblclick')
		         .replace(/enerror/g	, 'x-enerror')
		         .replace(/onfocus/g	, 'x-onfocus')
		         .replace(/onload/g		, 'x-onload')
		         .replace(/onmouse/g	, 'x-onmouse')
		         .replace(/onscroll/g	, 'x-onscroll')
		         .replace(/onsubmit/g	, 'x-onsubmit')
		         .replace(/onunload/g	, 'x-onunload');
		//--------------- XSS 방지 스크립트 처리 ----------------------------------------------
	}catch(e){
		//console.log(e);
	}finally{
		return str;
	}
}

/**
 * 정규식 공통으로 사용하기 위한 변수 선언
 * ex) validateFormat.onlyNum.test(inputValue);
 *
 */
	var validateFormat = {
		typeKorean : /[ㄱ-ㅎㅏ-ㅣ가-힣]/
		, typeNumber : /[0-9]/
		, typeUpperEnglish : /[A-Z]/
		, typeLowerEnglish : /[a-z]/
		, typeEnglish : /[a-zA-Z]/
		, typeEnglishUnderBar : /[a-zA-Z_]/
		, typeLetter : /[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z]/
		, typeLetterUnderBar : /[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z_]/
		, typeLetterNumber : /[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]/
		, typeNotLetterUnderBar : /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z_]/
		, typeNotKorean : /[^ㄱ-ㅎㅏ-ㅣ가-힣]/
		, typeNotNumber : /[^0-9]/
		, typeNotUpperEnglish : /[^A-Z]/
		, typeNotUpperEnglishUnderBar : /[^A-Z_]/
		, typeNotLowerEnglish : /[^a-z]/
		, typeNotLowerEnglishUnderBar : /[^a-z_]/
		, typeNotEnglish : /[^a-zA-Z]/
		, typeNotEnglishUnderBar : /[^a-zA-Z_]/
		, typeNotUpperEnglishNumberUnderBar : /[^A-Z0-9_]/
		, typeNotLetter : /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z]/
		, typeNotLetterNumber : /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]/
        , typeNotLetterSpaceNumber : /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\s]/
    }

/**
 * 숫자만 입력 가능 설정
 * @author : 김아란
 * @date : 2017.05.25
 * @returns {void}
 */
function fnValidateNum(obj){
	var $obj = $("#"+obj);
	$obj.keyup(function(){
	    if(!checkNum($obj)){
	    	$obj.val("");
	    	return;
	    }
	});
	$obj.blur(function(){
		if(!checkNum($obj)){
			$obj.val("");
			return;
		 }
	});
}

/**
 * 숫자 확인
 * @author : 김아란
 * @date : 2017.05.25
 * @returns {void}
 */
function checkNum($obj){
	if($obj != null && $obj.val() != ""){
		if(isNaN($obj.val().replace(/[,-]/gi, ""))){
			$obj.focus();
			$obj.val("");
			return false;
		}
	}
	return true;
}

/**
 * 전화번호 및 팩스번호 체크
 * @param value, objNm, target
 * @returns{boolean}
 */
function fnValidateTel(obj){
	var $obj = $("#"+obj);
	var filter = /^\d{2,3}-\d{3,4}-\d{4}$/;
	if (!filter.test($obj.val())){
		return true;
	}else{
		return false;
	}
}

/**
 * 시간형식 hh:mm 체크
 * @param obj Id
 * @returns{boolean}
 */
function fnValidateHourMinute(obj){
    var $obj = $("#"+obj);
    var filter = /^([01][0-9]|2[0-3]):([0-5][0-9])$/;
    if (filter.test($obj.val())){
        return true;
    }else{
        return false;
    }
}

/**
 *  이메일 체크하기
 *
 *  param  : email (ex:sealove3904@gmail.com)
 *  return : true | false
 */
function fnValidateEmail(email) {
	var re = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
	return re.test(email);
}
/**
 *  휴대폰 번호 유효성 체크하기
 *
 *  휴대전화의 경우는 010은 중간자리가 항상 4자리이고
 *  011, 016, 017, 018, 019의 경우 3자리 또는 4자리이므로 위와 같이 정해주게 되면 010일때는 중간번호를 항상 4자리를 받고 그 외의 경우는 3자리나 4자리를 받게 된다.
 *
 *  param  : email (ex:01049053754 | 010-4905-3754)
 *  return : true | false
 */
function fnValidatePhone(phone){
	console.log(phone);
	var trans_num = phone.replace(/-/gi,'');
	var flag = true;
	var re1 =/^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})([0-9]{3,4})([0-9]{4})$/;
	//var re1 = /^(?:(010\d{3})|(01[1|6|7|8|9]\d{3,4}))(\d{4})$/;
	if( !re1.test(trans_num) ){
		//var re2 = /^(?:(010-\d{3})|(01[1|6|7|8|9]-\d{3,4}))-(\d{4})$/;
		//flag = re2.test(phone);
		flag = false;
	}
	return flag;
}

/**
 * 3자리 숫자마다 콤마 추가
 * @author : 김아란
 * @date : 2017.06.19
 * @param target
 * @returns {void}
 */
function fnNumberWithCommas(target) {
	//console.log(target);
    return target.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}







/**
 * fnKendoDatePicker > change 함수: 기간 선택 후 날짜 유효성 체크
 * 김영준
 * fromID : 시작날짜
 * toID   : 종료날짜
 * msg    : Label 항목명
 * ex     : dateTimeChk(fromID,toID,msg);
 */
function dateTimeChk(fromID,toID,msg){

	//$('.k-input').attr('readonly', true);
	var _from = $("#"+fromID).val();
	var _to = $("#"+toID).val();
	if(_from != "" && _to != ""){
        if(_from > _to){
        	  $("#"+fromID).val('');
    	      $("#"+toID).val('');
    	      $("#"+fromID).focus();
    	  	  fnKendoMessage({message : fnGetLangData({key :"6666",nullMsg :msg+' 종료일이 시작일보다 빠릅니다.' })});
		          return;
		    }
    }
}


/**
 * 김영준
 * fnSearch : 조건 기간검색 중 시작일/종료일 공백 유효성 체크
 * ex       : if(dateBlankChk() == false) return;
 */
function dateBlankChk(){

	var _label = $(".k-datepicker").parents("tr").find("label").text();
	var _dateCnt = $(".k-datepicker").find($(".k-input")).length;
	var _dateClass = $(".k-datepicker").find($(".k-input"));

	var _dateS1 = "";
	var _dateE1 = "";

	for(var i=0; i<_dateCnt; i++){
		if(i == 0){
			_dateS1 = _dateClass[i].value;
		}else if(i == 1){
			_dateE1 = _dateClass[i].value;
		}
		// 검색조건에 기간검색이 두개 일 경우 하단에 추가 작업 필요
	}

	if((_dateS1 == "" && _dateE1 != "") || (_dateS1 != "" && _dateE1 == "")){
		if(_dateS1 == ""){
		    fnKendoMessage({message : fnGetLangData({key :"7777",nullMsg : _label + ' 시작일을 입력해주세요.' })});
		}else if(_dateE1 == ""){
		 	fnKendoMessage({message : fnGetLangData({key :"8888",nullMsg : _label + ' 종료일을 입력해주세요.' })});
		}else{
			// 검색조건에 기간검색이 두개 일 경우 하단에 추가 작업 필요
		}
		return false;
	}

	return true;
}


/**
 *  브라우저 URL을 이용하여 파라미터를 생성한다.
 */
function fnUrlToParam(){
	var vars = [], hash;
	var obj = new Object();
	var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
	for(var i = 0; i < hashes.length; i++)
	{
		hash = hashes[i].split('=');
		obj[hash[0]] = decodeURIComponent(hash[1]);
	}
	return obj;
}


/**
 * get 방식 url 파라미터에 대하여 값을 구한다.
 */
function fnGetPageParam(){
	var vars = [], hash;
	var obj = new Object();
	var hashes = window.location.href.slice(window.location.href.lastIndexOf('?') + 1).split('&');

	for(var i = 0; i < hashes.length; i++){

		hash = hashes[i].split('=');
		obj[hash[0]] = decodeURIComponent(hash[1]);
	}

	return obj;
}

/**
 * 폼생성 후 서브밋 하는 함수
 * @param {Object} param
 * @param {Object} url
 */
function fnDynFrmSubmit( params, url ){

	var form = document.createElement('form');
	form.setAttribute('method', 'post');
	form.setAttribute('action', url);
	$.each(params, function(k,v){
		var hiddenField = document.createElement('input');
		hiddenField.setAttribute('type', 'hidden');
		hiddenField.setAttribute('name', k);
		hiddenField.setAttribute('value', v);
		form.appendChild(hiddenField);
	});

	document.body.appendChild(form);
	form.submit();
}

/**
 * xls 파라미터 설정
 * @param {Object} params
 * @param {Object} grid option
 */
function fnXlsParam( params, opt ){

	params['COLUMNS']        = kendo.stringify(opt.columns);
	params['HEADER_LINE']    = kendo.stringify(opt.headerLine);
	params['XLS_SHEET_NAME'] = kendo.stringify(opt.xlsSName);
	params['XLS_FILE_NAME']  = kendo.stringify(opt.xlsFName);



	return params;
}

/**
 * Edit Grid Validation Check - 상품등록 상품고시 소스 참고!!
 * @param {Object} params
 */
function fnEditGridValidation( params ){

}

/**
 * Object 유효성 체크
 * @param {Object} argv1
 */
function fnNvl( argv1 ){
	if( argv1 != null && argv1 != "" && argv1 != undefined ){
		return argv1;
	}else{
		return '';
	}
}

/**
 * 문자열이 빈 문자열인지 체크하여 결과값을 리턴
 * @param str : 체크할 문자열
 * @returns boolean
 */
function fnIsEmpty( str ){
    if( typeof str == "undefined" || str == null || str === "" ){
        return true;
    }else{
        return false;
    }
}

/**
 * Input Text 입력값을 숫자(0~9)로 제한
 * @param objectId
 * @returns
 */
function fnInputValidationForNumber(objectId) {
    var message = "숫자만 입력 가능합니다.";
	fnInputValidationByRegexp(objectId, /[^0-9]/g, message);
}

/*
 * 소수점 자리수 제한 Validation 체크 : 한글 입력은 해당 input tag 에서 onkeyup="this.value=this.value.replace(/[^.0-9]/g,'');" 로 별도로 막아야 함
 *
 * @param limitCountUpperDecimalPoint : 소수점 이상 허용 자리수
 * @param limitCountUnderDecimalPoint : 소수점 이하 허용 자리수
 *
 */
function fnDecimalValidationByNumberOfDigits(event, limitCountUpperDecimalPoint, limitCountUnderDecimalPoint) {

    var event = event || window.event;
    var charCode = (event.which) ? event.which : event.keyCode;
    var _value = event.target.value; // 기존 input 의 값 : 마지막에 입력한 event.key 는 미포함

    if ( ! charCode || ( ! ( charCode >= 48 && charCode <= 57 ) && charCode != 46 ) ) { // 숫자, 마침표만 입력 허용
        event.preventDefault();
        return false;
    }

    if( _value.length == 0 && ( event.key == 0 || event.key == '.' ) ) { // 최초 자리수는 0 또는 소수점 불가
        event.preventDefault();
        return false;
    }

    if( (_value + event.key).indexOf('.') >= 0 ) {  // ( 이전 입력값 + 현재 입력값 ) 에 소수점 포함되어 있는 경우

        if( _value.indexOf('.') >= 0 && charCode == 46 ) { // 소수점 2번 입력 방지
            event.preventDefault();
            return false;
        }

        var decimalSplit = _value.split('.');

        if( decimalSplit[1] != undefined && decimalSplit[1].length >= limitCountUnderDecimalPoint ) {
            event.preventDefault();
            return false;

        }

    } else { // ( 이전 입력값 + 현재 입력값 ) 에 소수점 미포함

        if( _value.length >= limitCountUpperDecimalPoint && charCode != 46 ) { // limitCountUpperDecimalPoint 번째 자리 이후에는 소수점만 입력 가능
            event.preventDefault();
            return false;
        }

    }

}

/**
 * Input Text 입력값을 숫자(0~9)로 제한
 * @param inputDom
 * @returns
 */
function fnInputDomValidationForNumber(inputDom) {
	fnInputDomValidationByRegexp(inputDom, /[^0-9]/g);
}


/**
 * Input Text 입력값을 영문대소문자로 제한
 * @param objectId
 * @returns
 */
function fnInputValidationForAlphabet(objectId) {
    var message = "영어 대소문자만 입력 가능합니다.";
	fnInputValidationByRegexp(objectId, /[^a-z^A-Z]/g, message);
}

/**
 * Input Text 입력값을 영문대소문자 + 숫자 + 언더바(_) 로 제한
 * @param objectId
 * @returns
 */
function fnInputValidationForAlphabetNumberUnderbar(objectId) {
    var message = "영어 대소문자, 숫자, '_'만 입력 가능합니다.";
	fnInputValidationByRegexp(objectId, /[^a-z^A-Z^0-9^_]/g, message);
}

/**
 * Input Text 입력값을 영문대소문자 + 숫자 + 하이픈(-) 로 제한
 * @param objectId
 * @returns
 */
function fnInputValidationForAlphabetNumberHyphen(objectId) {
    var message = "영어 대소문자, 숫자, '-'만 입력 가능합니다.";
	fnInputValidationByRegexp(objectId, /[^a-z^A-Z^0-9^\-]/g, message);
}

/**
 * Input Text 입력값에 특수문자 제외
 * @param objectId
 * @returns
 */
function fnInputValidationLimitSpecialCharacter(objectId) {
    var message = "특수 문자는 입력하실 수 없습니다.";
	fnInputValidationByRegexp(objectId, /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi, message);
}

/**
 * Input Text 입력값을 영문대소문자 + 한글 로 제한
 * @param objectId
 * @returns
 */
function fnInputValidationForAlphabetHangul(objectId) {
    var message = "영어 대소문자, 한글만 입력 가능합니다.";
    fnInputValidationByRegexp(objectId, /[^a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]/g, message);
}

/**
 * Input Text 입력값을 숫자 + 바(-) 로 제한
 * @param objectId
 * @returns
 */
function fnInputValidationForNumberBar(objectId) {
    var message = "숫자 '-'만 입력 가능합니다.";
    fnInputValidationByRegexp(objectId, /[^0-9-]/g, message);
}

/**
 * Input Text 입력값을 한글 + 숫자 + 바(-) 로 제한
 * @param objectId
 * @returns
 */
function fnInputValidationForHangulNumberBar(objectId) {
    var message = "한글, 숫자, '-'만 입력 가능합니다.";
    fnInputValidationByRegexp(objectId, /[^ㄱ-ㅎㅏ-ㅣ가-힣0-9-]/g, message);
}

/**
 * Input Text 입력값을 영문대소문자 + 특수문자 로 제한
 * @param objectId
 * @returns
 */
function fnInputValidationForAlphabetSpecialCharacters(objectId) {
    var message = "영어 대소문자, 특수 문자만 입력 가능합니다.";
    fnInputValidationByRegexp(objectId, /[^a-zA-Z\{\}\[\]\/?.,;:|\)*~`!^\-_+┼<>@\#$%&\'\"\\\(\=]/g, message);
}

/**
 * Input Text 입력값을 숫자 + 특수문자 로 제한
 * @param objectId
 * @returns
 */
function fnInputValidationForNumberSpecialCharacters(objectId) {
    var message = "특수 문자, 숫자만 입력 가능합니다.";
    fnInputValidationByRegexp(objectId, /[^0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+┼<>@\#$%&\'\"\\\(\=]/g, message);
}

/**
 * Input Text 입력값을 영문대소문자 + 숫자 + 특수문자 로 제한
 * @param objectId
 * @returns
 */
function fnInputValidationForAlphabetNumberSpecialCharacters(objectId) {
    var message = "영어 대소문자, 특수 문자, 숫자만 입력 가능합니다.";
    fnInputValidationByRegexp(objectId, /[^a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+┼<>@\#$%&\'\"\\\(\=]/g, message);
}

/**
 * Input Text 입력값을 한글 + 영문대소문자 + 숫자 로 제한
 * @param objectId
 * @returns
 */
function fnInputValidationForHangulAlphabetNumber(objectId) {
    var message = "영어 대소문자, 한글, 숫자만 입력 가능합니다.";
    fnInputValidationByRegexp(objectId, /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]/g, message);
}

/**
 * Input Text 입력값을 영문대소문자 + 숫자 + 특수문자(.) 로 제한
 * @param objectId
 * @returns
 */
function fnInputValidationForAlphabetNumberPoint(objectId) {
    var message = "영어 대소문자, 숫자 '.'만 입력 가능합니다";
    fnInputValidationByRegexp(objectId, /[^a-zA-Z0-9.]/g, message);
}

/**
 * Input Text 입력값을 영문소문자 + 숫자 + 특수문자(.) 로 제한
 * @param objectId
 * @returns
 */
function fnInputValidationForLowAlphabetNumberPoint(objectId) {
    var message = "영어 소문자, 숫자, '.'만 입력 가능합니다.";
    fnInputValidationByRegexp(objectId, /[^a-z0-9.]/g, message);
}

/**
 * Input Text 입력값을 영문대소문자 + 숫자 로 제한
 * @param objectId
 * @returns
 */
function fnInputValidationForAlphabetNumber(objectId) {
    var message = "영어 대소문자, 숫자만 입력 가능합니다.";
    fnInputValidationByRegexp(objectId, /[^a-zA-Z0-9]/g, message);
}

/**
 * Input Text 입력값을 영문대소문자 + 숫자 + 엔터 + , 로 제한
 * @param objectId
 * @returns
 */
function fnInputValidationForAlphabetNumberLineBreakComma(objectId) {
    var message = "영어 대/소문자, 숫자, 엔터, ','만 입력 가능합니다.";
    fnInputValidationByRegexp(objectId, /[^a-zA-Z0-9,\n]/g, message);
}

/**
 * Input Text 입력값을 한글 + 특수문자( ) 로 제한
 * @param objectId
 * @returns
 */
function fnInputValidationForHangulSpace(objectId) {
    var message = "한글, 특수 문자만 입력 가능합니다.";
    fnInputValidationByRegexp(objectId, /[^ㄱ-ㅎㅏ-ㅣ가-힣 ]/g, message);
}

/**
 * Input Text 입력값을 영문대소문자 + 한글 + 공백 으로 제한
 * @param objectId
 * @returns
 */
function fnInputValidationForAlphabetHangulSpace(objectId) {
    var message = "영어 대소문자, 한글, 공백만 입력 가능합니다.";
    fnInputValidationByRegexp(objectId, /[^a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣\s]/g, message);
}
/**
 * Input Text 입력값을 영문대소문자 + 한글 + 숫자 + 공백 으로 제한
 * @param objectId
 * @returns
 */
function fnInputValidationForAlphabetHangulNumberSpace(objectId) {
  var message = "영어 대소문자, 한글, 숫자, 공백만 입력 가능합니다.";
  fnInputValidationByRegexp(objectId, /[^a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣\s]/g, message);
}

/**
 * Input Text 입력값을 한글 + 영문대소문자 + 숫자 + 특수문자( ) + 공백 으로 제한
 * @param objectId
 * @returns
 */
function fnInputValidationForHangulAlphabetNumberSpace(objectId) {
    var message = "영어 대소문자, 한글, 숫자, 특수 문자만 입력 가능합니다.";
    fnInputValidationByRegexp(objectId, /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\s\{\}\[\]\/?.,;:|\)*~`!^\-_+┼<>@\#$%&\'\"\\\(\=]/g, message);
}

/**
 * Input Text 입력값을 한글 + 영문대소문자 + 특수문자( )로 제한
 * @param objectId
 * @returns
 */
function fnInputValidationForHangulAlphabetSpace(objectId) {
    var message = "영어 대소문자, 한글, 특수 문자만 입력 가능합니다.";
    fnInputValidationByRegexp(objectId, /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z\{\}\[\]\/?.,;:|\)*~`!^\-_+┼<>@\#$%&\'\"\\\(\=]/g, message);
}

/**
 * Input Text 에 입력된 값이 filter에 적합한지 체크후 입력제한 및 삭제
 * @param objectId
 * @param regexpFilter
 * @returns
 */
function fnInputValidationByRegexp(objectId, regexpFilter, message) {

	var $targetObject = $("#"+objectId);

	$targetObject.keyup(function(){
	    var tempValue = $(this).val();

	    if(tempValue.length > 0) {
	    	if(tempValue.match(regexpFilter)) {
                $(this).val(tempValue.replace(regexpFilter, ""));

                fnShowValidateMessage($targetObject, message);
	    	}
	    }
	});

	// 포커스아웃 이벤트의 경우에도 동일하게 적용
	$targetObject.focusout(function(){
		var tempValue = $(this).val();

	    if(tempValue.length > 0) {
	    	if(tempValue.match(regexpFilter)) {
                $(this).val(tempValue.replace(regexpFilter, ""));

                fnShowValidateMessage($targetObject, message);
	    	}
	    }
	});

	// $targetObject.keypress(function(event){
	// 	return regexpFilter.test(event.key) ? false : true;
	// });
}

/**
 * Input Text 에 입력된 값이 filter에 적합한지 체크후 입력제한 및 삭제, 최대 길이 체크
 * @param objectId
 * @param regexpFilter
 * @returns
 */
function fnInputValidationAndLengthByRegexp(objectId, regexpFilter, message, maxLength) {
    var $targetObject = $("#" + objectId);
    var self = $(this);

	$targetObject.keyup(function(){
        var tempValue = self.val();
        var valueLength = tempValue.length;

        if( maxLength &&  valueLength > maxLength ) {
            tempValue = tempValue.slice(0, maxLength);
            self.val(tempValue);
        }

	    if(tempValue.length > 0) {
	    	if(tempValue.match(regexpFilter)) {
                self.val(tempValue.replace(regexpFilter, ""));
                fnShowValidateMessage($targetObject, message);
	    	}
	    }
	});

	// 포커스아웃 이벤트의 경우에도 동일하게 적용
	$targetObject.focusout(function(){
		var tempValue = self.val();

	    if(tempValue.length > 0) {
	    	if(tempValue.match(regexpFilter)) {
                self.val(tempValue.replace(regexpFilter, ""));

                fnShowValidateMessage($targetObject, message);
	    	}
	    }
	});
}

/**
 * Input Text 에 입력된 값이 filter에 적합한지 체크후 입력제한 및 삭제
 * @param inputDom
 * @param regexpFilter
 * @returns
 */
function fnInputDomValidationByRegexp(inputDom, regexpFilter) {
	var $targetObject = $(inputDom);

	$targetObject.keyup(function(){
	    var tempValue = $(this).val();

	    if(tempValue.length > 0) {
	    	if(tempValue.match(regexpFilter)) {
	    		$(this).val(tempValue.replace(regexpFilter, ""));
	    	}
	    }
	});
	$targetObject.keypress(function(event){
		return regexpFilter.test(event.key) ? false : true;
	});
}

/**
 * 휴대폰번호,전화번호 하이픈("-")입력
 * @param objectId
 * @returns
 */
function fnPhoneNumberHyphen(target){
	return target.toString().replace(/[^0-9*]/g, "").replace(/(^02.{0}|^01.{1}|[0-9]{3})([0-9*]+)([0-9]{4})/,"$1-$2-$3").replace("--", "-") ;
}



    // MS 계열 브라우저를 구분하기 위함.
    if(USER_APP_NAME === 'Microsoft Internet Explorer' || USER_AGENT.indexOf('trident') > -1 || USER_AGENT.indexOf('edge/') > -1) {
        USER_BROWSER = 'ie';
        if(USER_APP_NAME === 'Microsoft Internet Explorer') { // IE old version (IE 10 or Lower)
            USER_AGENT = /msie ([0-9]{1,}[\.0-9]{0,})/.exec(USER_AGENT);
            USER_BROWSER += parseInt(USER_AGENT[1]);
        } else { // IE 11+
            if(USER_AGENT.indexOf('trident') > -1) { // IE 11
                USER_BROWSER += 11;
            } else if(USER_AGENT.indexOf('edge/') > -1) { // Edge
                USER_BROWSER = 'edge';
            }
        }
    } else if(USER_AGENT.indexOf('safari') > -1) { // Chrome or Safari
        if(USER_AGENT.indexOf('opr') > -1) { // Opera
            USER_BROWSER = 'opera';
        } else if(USER_AGENT.indexOf('chrome') > -1) { // Chrome
            USER_BROWSER = 'chrome';
        } else { // Safari
            USER_BROWSER = 'safari';
        }
    } else if(USER_AGENT.indexOf('firefox') > -1) { // Firefox
        USER_BROWSER = 'firefox';
    }

    // IE: ie7~ie11, Edge: edge, Chrome: chrome, Firefox: firefox, Safari: safari, Opera: opera
    //document.getElementsByTagName('html')[0].className = w;

//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////
var $scope = (function() {
	var actions = {
		fnIsMenu: function(args) {
            if( args.flag ){
                $('#document').removeClass('ifr_popup');

                if($('#lnb').css('display') == 'none'){
                    $('#lnb').css({"display":""});
                }

                if($('#lnb-closeBtn').css('display') == 'none') {
                    $('#lnb-closeBtn').css({'display': ''});
                }

                if($('#wrapHeaderMain').css('display') == 'none'){
                    $('#wrapHeaderMain').css({"display":""});
                }
                if($('#footer').css('display') == 'none'){
                    $('#footer').css({"display":""});
                }
                if($('.location').css('display') == 'none'){
                    $('.location').css({"display":""});
                }
            }else{
                //팝업일 경우
                $('#document').addClass('ifr_popup');

                $('#lnb').css('display','none');
                $('#lnb-closeBtn').css('display', 'none');

                $('#wrapHeaderMain').css('display','none');
                $('#footer').css('display','none');

                $('h1.title').css('display','none');
                $('.location').css('display','none');
                //팝업 contents 우측 짤림 처리
                $('div#document').css('min-width', '0');
                //팝업 height resize 처리
                $('article#container').css('height', '100%');
                $('div#contents').css('padding-bottom', '0px');

                $('div#ng-view').css('width', '100%');

                $('#container').css('padding-left','0px');
            }
        },

        fnLogOut: function(){

            $.ajax({
                url     : "/comn/ur/login/logOut",
                success : function( data ){
                    location.href = '/admVerify.html';
                }
            });
        },

        fnGoMyStore: function() {
            var openNewWindow = window.open('about:blank');
                openNewWindow.location.href = MY_STORE_URL;
        },

        fnGoMain: function(){

            $('#navigation').find('.active').removeClass('active');

            if(PG_SESSION !=null){
                /*본사권한 관리자 로그인시 : 본사 대시보드로 링크
                입점사권한 관리자 로그인시 : 입점사 대시보드로 링크
                공급사 관리자 로그인시 : 사입상품 관리 > 사입상품 리스트로 링크*/
                var option = new Object();
                var random = Math.floor(Math.random() * 1000) + 1;
                var param = {};
                    param['ngViewVer'] = random;
                    param['menuId'] = 362;
/*
                if(PG_SESSION.companyType == 1){ //입점사
                    // 매장점주 36
                    if( PG_SESSION.roleId == '36' ){
                        $location.path( '/dashboardShop' ).search( param );
                    }else{
                    	$location.path( '/dashboardComp' ).search( param );
                    }
                }else if(PG_SESSION.companyType == 2){ //공급사
                    $location.path( '/dashboardComp' ).search( param );
                }else if(PG_SESSION.companyType == 3){ //본사
                    $location.path( '/dashboardTot' ).search( param );
                }
*/
                $scope.$apply();
            }

            else {
    	    	alert("PG_SESSION is null ");
            }

        },

        fnBuyerPop: function(){
            fnKendoPopup({
                id     : 'buyer002',
                title  : fnGetLangData({key :"5210",nullMsg :'회원찾기' }),
                src    : '#/buyer002',
                width  : '750px',
                height : '605px',
                success: function( id, data ){
                    //console.log(data);
                    if(data.UR_USER_ID){
                        $('#crmInfo').html(data.USERNAME+" ("+data.GENDER+") "+ data.LOGIN_ID);
                        $('#crmInfo2').html("일반회원");
                        $('#crmInfo3').html(data.GRP_NAME+" 등급");
                        $('#crmInfo4').html(data.MOBILE+" / "+data.BDAY);
                        $('#nonMemTxt').css('display','none');
                        $('#memTxt').css('display','');

                        $('#CRM_USER_ID').val(data.UR_USER_ID);
                        $('#CRM_LOGIN_ID').val(data.LOGIN_ID);
                        $('#CRM_USER_NAME').val(data.USERNAME);

                        $('#fnCrmSetting').trigger('click');
                    }
                }
            });
        },

        goPage: function( opt ){
            var url = opt.url.replace('#', '');

            try{

                //console.log( 'app.js goPage ::: $location.$$url, url :::' + $location.$$url + ' = ' + url );
                /*
                        if( $location.$$url == url && getItem('LAST_KEY_CODE') != 116 ){
                            setItem('LAST_KEY_CODE', 116);
                            $window.location.reload();
                            return;
                        }
                */

                /**
                 *   뷰를 업데이트를 하기 위해서는 Angular에 바인딩된 값이 변경되었는지 확인을 한다.
                 *   그래서 난수를 추가해서 페이지를 호출하면 바인딩 된 객체의 변화를 감지하여 Angular가 뷰를 업데이트 한다.
                 */
                var random = Math.floor(Math.random() * 1000) + 1;

                if( opt.menuId == '' || opt.menuId == undefined ){
                    alert('메뉴설정을 해주세요.');
                }else{
                    //alert( opt.menuId + ' / ' + CLICKED_MENU_ID);
                    CLICKED_MENU_ID = opt.menuId;
                }

                if(opt.data !== undefined){

                    $.extend(opt, { ngViewVer : random });
                    console.log('11: ', url, opt);
                    fnGoPage(opt);
                    // $location.path( url ).search(opt.data);
                }else{

                    var param = {};
                    var hashes = [];
                    var hash = [];
                    var urlInfo = url.split('?');

                    if( urlInfo.length > 1 ){
                        hashes = urlInfo[1].split('&');
                        url = urlInfo[0];
                    }

                    // 파라미터가 있는 경우
                    if( hashes.length > 0 ){
                        for(var i = 0; i < hashes.length; i++){
                            hash = hashes[i].split('=');
                            param[hash[0]] = decodeURIComponent(hash[1]);
                        }
                    }

                    param['ngViewVer'] = random;
                    console.log('22: ', url, param );
                    fnGoPage(param);
                    // $location.path( url ).search( param );
                }

                // $scope.$apply();

                //console.log('2');

            }catch(e){
                var errMsg  = '----------------------------- \n';
                    errMsg += '에러메세지 \n';
                    errMsg += '----------------------------- \n';
                    errMsg += e + '\n\n';
                    errMsg += '----------------------------- \n';
                    errMsg += '변경해야할 소스 ';
                    errMsg += '----------------------------- \n';
                    errMsg += 'fnGoPage 수정 예상 \n';
                    errMsg += '$scope.$emit(\'goPage\', option); \n';

                alert(errMsg);
            }
        },
	}

	return {
		$emit: function(id, payload) {
			if(actions[id]) actions[id](payload);
		},

		//
		goPage: actions.goPage,
	}
}());

//Rowspan
/*
 * 같은 값이 있는 열을 병합함
 * td 안에 내용은 삭제 함
 * 사용법 : $('#테이블 ID').rowspan(0);
 */
$.fn.rowspan = function(colIdx, isStats) {
	return this.each(function(){
		var that;
		$("tr", this).each(function(row) {
			$("td:eq("+colIdx+")", this).each(function(col) {

				if ($.trim($(this).html()) == $.trim($(that).html())
					&& (!isStats
							|| isStats && $.trim($(this).prev().html()) == $.trim($(that).prev().html())
							)
					) {
					rowspan = $(that).attr("rowspan") || 1;
					rowspan = Number(rowspan)+1;
					console.log("1")
					$(that).attr("rowspan",rowspan);

					$(this).empty().hide();
					//$(this).hide();
				} else {
					console.log(this.html())
					that = this;
				}

				that = (that == null) ? this : that;
			});
		});
	});
};


/*
 * 같은 값이 있는 열을 병합함
 * td 안에 내용은 그대로 둠
 * 사용법 : $('#테이블 ID').rowspanNotEmpty(0);
 */
$.fn.rowspanNotEmpty = function(colIdx, isStats) {
	return this.each(function(){
		var that;
		$("tr", this).each(function(row) {
			$("td:eq("+colIdx+")", this).each(function(col) {

				if ($.trim($(this).html()) == $.trim($(that).html())
					&& (!isStats
							|| isStats && $.trim($(this).prev().html()) == $.trim($(that).prev().html())
							)
					) {
					rowspan = $(that).attr("rowspan") || 1;
					rowspan = Number(rowspan)+1;

					$(that).attr("rowspan",rowspan);

					$(this).hide();
					//$(this).hide();
				} else {
					that = this;
				}

				that = (that == null) ? this : that;
			});
		});
	});
};

/*
 * 같은 값이 있는 열을 병합함
 * th만 적용 됨
 * 사용법 : $('#테이블 ID').rowspanTh(0);
 */

$.fn.rowspanTh = function(colIdx, isStats) {
	return this.each(function(){
		var that;
		$("tr", this).each(function(row) {
            if($(this).hasClass("hidden") == false){
			$("th:eq("+colIdx+")", this).each(function(col) {
				if ($.trim($(this).html()) == $.trim($(that).html())
					&& (!isStats
							|| isStats && $.trim($(this).prev().html()) == $.trim($(that).prev().html())
							)
					) {
					rowspan = $(that).attr("rowspan") || 1;
					rowspan = Number(rowspan)+1;

					$(that).attr("rowspan",rowspan);

					$(this).empty().hide();
					//$(this).hide();
				} else {
					that = this;
				}

				that = (that == null) ? this : that;
			});
            }
		});
	});
};




/*
 *
 * 같은 값이 있는 행을 병합함
 *
 * 사용법 : $('#테이블 ID').colspan (0);
 *
 */
$.fn.colspan = function(rowIdx) {
    return this.each(function(){

        var that;
	var colspan;
        $('tr', this).filter(":eq("+rowIdx+")").each(function(row) {
            $(this).find('th').filter(':visible').each(function(col) {
                if ($(this).html() == $(that).html()) {
                    colspan = $(that).attr("colSpan") || 1;
                    colspan = Number(colspan)+1;

                    $(that).attr("colSpan",colspan);
                    $(this).hide(); // .remove();
                } else {
                    that = this;
                }

                // set the that if not already set
                that = (that == null) ? this : that;

            });
        });
    });
}



$(document).ready(function() {

	$scope.fnLogOut = function() { fnLogOut(); };

    function fnLogOut(){
    	if(confirm("로그아웃 하시겠습니까?")) {

	    	$.ajax({
	             url     : "/admin/login/delLoginData",
	 			 type    : 'POST',
	             success : function( data ){
	                 location.href = '/admVerify.html';
	             }
	        });
    	}

    }
});

/**
 * Input 태그 최대 입력 길이 지정
 * @param {string}} id : 태그 아이디
 * @param {number} maxLength
 */
function fnCheckLength( id, maxLength ) {
    var $targetObject = $("#" + id);

    $targetObject.keyup(function() {
        var self = $(this);
        var value = self.val();

        if( value.length > maxLength ) {
            value = value.slice(0, maxLength);
            self.val(value);
        }
    })
}

/**
 * 소수점 입력 validation
 * @param objId
 * @param regexpFilter
 * @returns
 */
function fnInputDecimalValidationByRegexp(objId, regexpFilter){
	let $targetObj = $("#"+objId);

	$targetObj.keyup(function(){
		let targetVal = $(this).val();

		if(targetVal.length > 0){

			// 숫자, 소수점만 입력가능
			if(targetVal.match(regexpFilter)) {
				$(this).val(targetVal.replace(regexpFilter, ""));
				fnShowValidateMessage($targetObj);
			}

			// 처음 입력값 소수점 불가
			if(targetVal.length == 1 && (targetVal == '.')){
				$(this).val('');
				fnShowValidateMessage($targetObj);
			}

			// 소수점 2번 입력 방지
			if(targetVal.indexOf('.') >= 0 ){
				let arr = targetVal.split('.');
				if(arr.length > 2){
					$(this).val(arr[0]+'.'+arr[1]);
					fnShowValidateMessage($targetObj);
				}
			}
		}
	});
}

function fnShowValidateMessage(target, message = "지원하지 않는 형식의 입력이 있습니다.") {
    if( !target || !target.length) {
        return;
    }

    fnKendoMessage({
        message: message,
        ok: function() {
            target.focus();
            target.val(target.val());
        }
    })
}


/*
* ClassName : stringUtil
* Description : 문자열 관련 Class
*/
var stringUtil = {
	/* 문자열확인 후 문자열 또는 기본값 리턴
    str:체크 문자열 def:기본값 */
	getString: function (str, def) {
		if (str != undefined && str && str != "" && str != "null" && str != null) {
			return $.trim(str);
		} else {
			return $.trim(def);
		}
	},
	/* 정수형 확인 후 정수형 또는 기본값 리턴
    num:체크 정수형 / def:기본값 */
	getInt: function(num, def){
		var val = parseInt(num, 10);

		if (isNaN(val)){
			return def;
		} else {
			return val;
		}
	},
}