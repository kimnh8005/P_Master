/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 상품관리 > 상세이미지 다운로드 팝업
 * @
 * @ 수정일        수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2020.01.22   김승우        최초 생성
 * @
 ******************************************************************************/
'use strict';

$(document).ready(function () {
//    const pageParam = parent.POP_PARAM;
//    const popupContent = parent.LAYER_POPUP_OBJECT;
//    const kendoWindow = popupContent.data('kendoWindow');
	const pageParam = {
		parameter: fnGetPageParam(),
	};
    const template = $('#template').html();
    const $screenContent = $('#screenContent');
    const $fileName = $('#fileName');

    var downloadPopup = null;

    fnInitialize();

    function fnInitialize() {
        $scope.$emit('fnIsMenu', { flag : false });
        
        fnPageInfo({
            PG_ID: 'goodsDetailImagePopup',
            callback: fnUI,
        });
    }

    function fnUI() {
        // 팝업창 내부 사이즈 조정
        initStyle();

        // 이벤트 초기화
        //fnInitEvent();

        // 다운로드 팝업 초기화
        //initDownloadPopup();

        try {
            // 데이터 요청
            fetchData();
        } catch (err) {
            console.warn(err);
            fnKendoMessage({
                message: err,
                ok: function () {
                    kendoWindow.close();
                },
            });
        }
    }

    // 이벤트 초기화
    function fnInitEvent() {
        // 파일저장 버튼 클릭 이벤트
        $('#downloadImage').on('click', function (e) {
            openDownloadPopup();
        });

        $('#closePopup').on('click', function (e) {
            closePopup();
        });

        // 다운로드 팝업 저장 버튼 이벤트
        $('#fnSaveFile').on('click', function (e) {
            console.log('저장');
            downloadImage();
        });

        // 다운로드 팝업 취소 버튼 이벤트
        $('#fnCancelFile').on('click', function (e) {
            console.log('취소');
            closeDownloadPopup();
        });
    }

    // 팝업창 닫기
    function closePopup() {
        kendoWindow.close();
    }

    // 템플릿 렌더링
    function renderTemplate(data) {
        const _template = $.template(template);

        $screenContent.empty();
        $.tmpl(_template, data).appendTo($screenContent);
        // changeImageAttribute();
        ignoreVideo();
    }

    // 파일저장
    function downloadImage() {
        var fileName = $fileName.val().trim() || getDefaultFileName();

        ignoreVideo();
        printDiv($screenContent[0], fileName);
    }

    // 저장할 html 내에 img태그 속성 변경
    function changeImageAttribute() {
        const $images = $screenContent.find('img');

        $images.each(function () {
            // setImageCors(this);
            // setImgProxy(this);
        });
    }

    // 이미지 src 속성에 프록시 url 추가 : https://github.com/Rob--W/cors-anywhere/blob/440d2de180737ef6bb16d1b38aaa6b5ce31363a8/demo.html#L67-L70
    function setImgProxy(image) {
        const proxyUrl = 'https://cors-anywhere.herokuapp.com/';
        const imgSrc = image.src;
        image.crossOrigin = 'anonymous';
        image.src = proxyUrl + imgSrc;
    }

    // 이미지 태그 crossorigin="anonymous" 처리 : https://github.com/niklasvh/html2canvas/issues/592
    // canvas cors : https://developer.mozilla.org/ko/docs/Web/HTML/CORS_enabled_image
    function setImageCors(image) {
        var imageSrc = image.src;
        const timestamp = new Date().getTime();
        const imageWithTimestamp = imageSrc.includes('?')
            ? `${imageSrc}&v=${timestamp}`
            : `${imageSrc}?v=${timestamp}`;
        image.src = imageWithTimestamp;
        image.crossOrigin = 'anonymous';
    }

    // screen 영역 내에 비디오 태그 삭제
    function ignoreVideo() {
        $screenContent.find('video').each(function () {
            //$(this).attr('data-html2canvas-ignore', true);
            $(this).css({
                display: 'none',
            });
        });
    }

    // screen에 html template 표시
    function setScreen(data) {
        const $screen = $('#screen');
        $screen.removeClass('hide');
        renderTemplate(data);
    }

    // api 요청
    function fetchData() {
        const ilGoodsId = pageParam.parameter
            ? pageParam.parameter.ilGoodsId
            : '';

        if (ilGoodsId !== '') {
            fbLoading(true);

            fnAjax({
                url: '/admin/goods/regist/getMallGoodsDetail',
                params: {
                    ilGoodsId: ilGoodsId,
                },
                success: function (data, result) {
                    //데이터 받아서 화면에 표시
                    setScreen(data);
                    fbLoading(false);
                },
                error: function (xhr, status, strError) {
                    console.log(arguments);
                    fbLoading(false);
                },
                isAction: 'select',
            });
        } else {
            throw new Error(
                '파라미터가 없습니다. ilGoodsId : ' + JSON.stringify(ilGoodsId)
            );
        }
    }

    // 스타일 초기화// 팝업창 초기화
    function initDownloadPopup() {
        var $popup = $('#downloadPopup');
        $popup.kendoWindow({
            visible: false,
            modal: true,
        });

        downloadPopup = $popup.data('kendoWindow');
    }

    // 다른이름으로 다운로드 팝업창 열기
    function openDownloadPopup() {
        var title = '파일 저장';
        var opt = {
            id: 'downloadPopup',
            width: 500,
            height: 'auto',
            title: { nullMsg: title },
        };

        const fileName = getDefaultFileName();
        setFileName(fileName);
        $fileName.focus();

        fnKendoInputPoup(opt);
    }
    // 다른이름으로 다운로드 팝업창 닫기
    function closeDownloadPopup() {
        if (downloadPopup) {
            downloadPopup.close();
            clearFileName();
        }
    }

    // 기본 파일명 => 상품 코드 + 상품 명 가져오기
    function getDefaultFileName() {
        const goodsName = pageParam.parameter.goodsName;
        const goodsCode = pageParam.parameter.ilGoodsId;
        const fileName = goodsCode + '_' + goodsName + '.jpg';
        return fileName;
    }

    // 파일명 입력 인풋 placeholder 설정
    function setFileName(fileName) {
        $fileName.attr('placeholder', fileName);
    }

    // 파일명 입력 인풋 초기화
    function clearFileName() {
        $fileName.attr('placeholder', '');
        $fileName.val('');
    }

    // 팝업 페이지 CSS 초기화
    function initStyle() {
        const $document = $('#document.popup-document');
        const $container = $document.find('#container');
        const $contents = $document.find('#contents');
        const $ngView = $document.find('#ng-view');

        const commonCss = {
            display: 'block',
            height: '100%',
        };

        $container.css(commonCss);
        $contents.css(commonCss);
        $ngView.css(commonCss);

        initPageStyle();
    }

    function initPageStyle() {
        const $html = document.getElementsByTagName('html')[0];
        $html.style.height = 'auto';

        document.getElementById('ng-view').style.minWidth = 'auto';
        document.getElementById('ng-view').style.padding = 0 + 'px';
    }

    // 묶음 상품 영양정보 표시 여부 확인
    function isNutritionDispYn(goods) {
        return -1 === goods.find(function(p) {
            return 'N' === p.nutritionDispYn;
        })
    }

    window.isNutritionDispYn = isNutritionDispYn;
});
