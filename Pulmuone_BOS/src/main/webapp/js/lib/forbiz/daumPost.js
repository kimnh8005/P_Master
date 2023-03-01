/*
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
 * 다음 주소검색 팝업
 * postcode : 주소검색 후 선택된 주소의 우편번호를 넣어줄 input id
 * address : 주소검색 후 선택한 주소를 넣어줄 input id
 * detailAddress : 주소검색 후 선택한 상세주소를 넣어줄 input id
 */

function fnDaumPostcode(postcode, address, detailAddress,buildingCode){
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //도로명 주소가 빈값일 경우 지번주소로 저장
            if (data.roadAddress !== '') {
                addr = data.roadAddress;
            } else {
                addr = data.jibunAddress;
            }

            // 도로명 주소가 빈값이 아닌 경우 참고항목을 조합
            if(data.roadAddress !== ''){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.코스트코코리아 양재점
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }

                // 주소변수에 참고항목 추가
                if(extraAddr !== ''){
                    addr += extraAddr;
                }
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById(postcode).value = data.zonecode;
            document.getElementById(address).value = addr;
            if (buildingCode ==null ||buildingCode ==undefined || buildingCode =="" ){
                buildingCode = "buildingCode";
            }
            else
            {
                document.getElementById(buildingCode).value = data.buildingCode;
            }



            // 커서를 상세주소 필드로 이동한다.
            document.getElementById(detailAddress).focus();
        }
    }).open();
}