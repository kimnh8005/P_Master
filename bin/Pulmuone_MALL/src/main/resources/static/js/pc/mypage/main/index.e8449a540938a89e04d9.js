(function(e){function t(t){for(var i,n,o=t[0],r=t[1],l=t[2],d=0,f=[];d<o.length;d++)n=o[d],Object.prototype.hasOwnProperty.call(a,n)&&a[n]&&f.push(a[n][0]),a[n]=0;for(i in r)Object.prototype.hasOwnProperty.call(r,i)&&(e[i]=r[i]);c&&c(t);while(f.length)f.shift()();return _.push.apply(_,l||[]),s()}function s(){for(var e,t=0;t<_.length;t++){for(var s=_[t],i=!0,o=1;o<s.length;o++){var r=s[o];0!==a[r]&&(i=!1)}i&&(_.splice(t--,1),e=n(n.s=s[0]))}return e}var i={},a={"pc/mypage/main/index":0},_=[];function n(t){if(i[t])return i[t].exports;var s=i[t]={i:t,l:!1,exports:{}};return e[t].call(s.exports,s,s.exports,n),s.l=!0,s.exports}n.m=e,n.c=i,n.d=function(e,t,s){n.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:s})},n.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},n.t=function(e,t){if(1&t&&(e=n(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var s=Object.create(null);if(n.r(s),Object.defineProperty(s,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var i in e)n.d(s,i,function(t){return e[t]}.bind(null,i));return s},n.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return n.d(t,"a",t),t},n.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},n.p="/";var o=window["webpackJsonp"]=window["webpackJsonp"]||[],r=o.push.bind(o);o.push=t,o=o.slice();for(var l=0;l<o.length;l++)t(o[l]);var c=r;_.push([140,"chunk-commons"]),s()})({140:function(e,t,s){e.exports=s("a07f")},"2cc1":function(e,t,s){},"40d1":function(e,t,s){"use strict";var i=s("2cc1"),a=s.n(i);a.a},a07f:function(e,t,s){"use strict";s.r(t);var i=s("5530"),a=(s("e260"),s("e6cf"),s("cca6"),s("a79d"),function(){var e=this,t=e.$createElement,s=e._self._c||t;return e.userSession.isLogin?s("main",{staticClass:"fb__mypage fb__mypage__index"},[s("fb-header",{attrs:{type:"sub"}}),s("div",{staticClass:"index"},[s("div",{staticClass:"product-layout__body"},[s("div",{staticClass:"product-layout__body__left"},[s("fb-mypage-menu")],1),e.loading||e.isOrderMember?s("div",{staticClass:"product-layout__body__right"},[e.isOrderMember?e._e():s("section",{staticClass:"index__top"},[s("header",{staticClass:"index__header"},[s("h2",{staticClass:"index__title"},[s("b",[e._v(e._s(e.userSession.name))]),e._v(" 님"),s("br"),e._v(" 건강한 하루 보내세요! ")]),s("div",{staticClass:"index__member"},[s("figure",{staticClass:"index__member__icon"},[s("img",{directives:[{name:"lazy",rawName:"v-lazy",value:e.mergeImageHost(e.info.topImagePath),expression:"mergeImageHost(info.topImagePath)"}],attrs:{alt:""}})])])]),s("div",{staticClass:"index__info"},[s("ul",{staticClass:"index__info__box"},[s("li",{staticClass:"index__info__reserves"},[s("a",{staticClass:"index__info__link",attrs:{href:e.$APP_CONFIG.PAGE_URL.MYPAGE_RESERVES}},[e._v("적립금")]),s("span",[e._v(e._s(e._f("price")(e.info.pointUsable)))])]),s("li",{staticClass:"index__info__coupon"},[s("a",{staticClass:"index__info__link",attrs:{href:e.$APP_CONFIG.PAGE_URL.MYPAGE_COUPON}},[e._v("쿠폰")]),s("span",{staticClass:"coupon__count"},[s("em",[e._v(e._s(e.info.couponCount))]),e._v(" "),s("span",{staticClass:"unit"},[e._v("장")])])]),s("li",{staticClass:"index__info__add-coupon"},[s("span",[e._v("이용권/쿠폰")]),s("a",{attrs:{href:e.$APP_CONFIG.PAGE_URL.MYPAGE_TICKET}},[e._v("등록하기")])])])])]),s("section",{staticClass:"index__cont"},[s("header",{staticClass:"index__cont__header"},[e._m(0),s("div",{staticClass:"index__cont__old-site imsi"},[s("a",{attrs:{href:e.$APP_CONFIG.OLD_MALL.PULMUONE_ORDER_LIST,target:"_blank"}},[e._v("구)풀무원샵 주문 조회")]),s("a",{attrs:{href:e.$APP_CONFIG.OLD_MALL.ORGA_ORDER_LIST_PC,target:"_blank"}},[e._v("구)올가 주문 조회")])])]),s("div",{staticClass:"index__order"},[s("div",{staticClass:"index__order__wrap"},[s("ul",{staticClass:"index__order__stepBox"},[s("li",{staticClass:"index__order__step"},[s("a",{attrs:{href:this.$APP_CONFIG.PAGE_URL.MYPAGE_ORDER_HISTORY+"?div=order"}},[s("b",[e._v(e._s(e.info.depositReadyCount))]),s("span",[e._v("입금대기중")])])]),s("li",{staticClass:"index__order__step"},[s("a",{attrs:{href:this.$APP_CONFIG.PAGE_URL.MYPAGE_ORDER_HISTORY+"?div=order"}},[s("b",[e._v(e._s(e.info.depositCompleteCount))]),s("span",[e._v("결제완료")])])]),s("li",{staticClass:"index__order__step"},[s("a",{attrs:{href:this.$APP_CONFIG.PAGE_URL.MYPAGE_ORDER_HISTORY+"?div=order"}},[s("b",[e._v(e._s(e.info.deliveryReadyCount))]),s("span",[e._v("배송준비중")])])]),s("li",{staticClass:"index__order__step"},[s("a",{attrs:{href:this.$APP_CONFIG.PAGE_URL.MYPAGE_ORDER_HISTORY+"?div=order"}},[s("b",[e._v(e._s(e.info.deliveryDoingCount))]),s("span",[e._v("배송중")])])]),s("li",{staticClass:"index__order__step"},[s("a",{attrs:{href:this.$APP_CONFIG.PAGE_URL.MYPAGE_ORDER_HISTORY+"?div=order"}},[s("b",[e._v(e._s(e.info.deliveryCompleteCount))]),s("span",[e._v("배송완료")])])])]),s("div",{staticClass:"index__order__cancel"},[s("a",{staticClass:"cancel__link",attrs:{href:this.$APP_CONFIG.PAGE_URL.MYPAGE_ORDER_HISTORY+"?div=cancel"}},[s("p",{staticClass:"cancel__type"},[e._v(" 취소 "),s("b",{staticClass:"cancel__count"},[e._v(e._s(e.info.orderCancelCount))])])]),s("a",{staticClass:"cancel__link",attrs:{href:this.$APP_CONFIG.PAGE_URL.MYPAGE_ORDER_HISTORY+"?div=cancel"}},[s("p",{staticClass:"cancel__type"},[e._v(" 반품/환불 "),s("b",{staticClass:"cancel__count"},[e._v(e._s(e.info.orderReturnRefundCount))])])])])]),e.isOrderMember?e._e():s("ul",{staticClass:"index__order__info"},[s("li",{staticClass:"info__box info__box--common"},[s("h4",{staticClass:"info__title"},[e._v("일반배송")]),e.info.normalDeliveryCount>0?s("span",{staticClass:"info__count"},[e._v(e._s(e.info.normalDeliveryCount))]):e._e(),e.info.normalDeliveryCount>0?s("p",{staticClass:"info__desc"},[s("em",[e._v(e._s(e.info.normalDeliveryCount)+"개")]),e._v("의 상품이 "),s("br"),e._v("곧 배송 될 예정 입니다.")]):s("p",{staticClass:"info__desc info__desc--not"},[e._v("오늘 배송 예정인 "),s("br"),e._v("상품이 없습니다.")]),s("a",{staticClass:"info__link",attrs:{href:this.$APP_CONFIG.PAGE_URL.MYPAGE_ORDER_HISTORY+"?div=order"}},[e._v("상세보기")])]),s("li",{staticClass:"info__box info__box--daily"},[s("h4",{staticClass:"info__title"},[e._v("일일배송")]),e.info.dailyDeliveryCount>0?s("span",{staticClass:"info__count"},[e._v(e._s(e.info.dailyDeliveryCount))]):e._e(),e.info.dailyDeliveryCount>0?s("p",{staticClass:"info__desc"},[e._v("진행중인 일일배송이 "),s("br"),s("em",[e._v(e._s(e.info.dailyDeliveryCount)+"건")]),e._v(" 있습니다.")]):s("p",{staticClass:"info__desc info__desc--not"},[e._v("진행중인 일일배송이 "),s("br"),e._v("없습니다.")]),s("a",{staticClass:"info__link",attrs:{href:""+this.$APP_CONFIG.PAGE_URL.MYPAGE_DAILY_MANAGE}},[e._v("상세보기")])]),e.isOperatingHostname?e._e():s("li",{staticClass:"info__box info__box--regular"},[s("h4",{staticClass:"info__title"},[e._v("정기배송")]),e.info.regularDeliveryCount>0?s("span",{staticClass:"info__count"},[e._v(e._s(e.info.regularDeliveryCount))]):e._e(),e.info.regularDeliveryCount>0?s("p",{staticClass:"info__desc"},[e._v("진행중인 정기배송이 "),s("br"),s("em",[e._v(e._s(e.info.regularDeliveryCount)+"건")]),e._v(" 있습니다.")]):s("p",{staticClass:"info__desc info__desc--not"},[e._v("진행중인 정기배송이 "),s("br"),e._v("없습니다.")]),s("a",{staticClass:"info__link",attrs:{href:""+this.$APP_CONFIG.PAGE_URL.MYPAGE_REGULAR_MANAGE}},[e._v("상세보기")])]),s("li",{staticClass:"info__box info__box--store"},[s("h4",{staticClass:"info__title"},[e._v("매장배송")]),e.info.storeDeliveryCount>0?s("span",{staticClass:"info__count"},[e._v(e._s(e.info.storeDeliveryCount))]):e._e(),e.info.storeDeliveryCount>0?s("p",{staticClass:"info__desc"},[e._v("진행중인 매장배송이 "),s("em",[e._v(e._s(e.info.storeDeliveryCount)+"건")]),e._v(" 있습니다.")]):s("p",{staticClass:"info__desc info__desc--not"},[e._v("진행중인 매장배송이 "),s("br"),e._v("없습니다.")]),s("a",{staticClass:"info__link",attrs:{href:this.$APP_CONFIG.PAGE_URL.MYPAGE_ORDER_HISTORY+"?div=order"}},[e._v("상세보기")])])])])]),e.isOrderMember?e._e():s("section",{staticClass:"index__cont"},[e._m(1),s("div",{staticClass:"index__activity__review",class:{empty:!e.info.feedback.length}},[s("a",{staticClass:"review__link",attrs:{href:e.$APP_CONFIG.PAGE_URL.MYPAGE_REVIEW+"?available=1"}},[s("strong",{staticClass:"review__link__title"},[e._v("작성 가능 후기")]),s("p",{staticClass:"review__link__desc"},[e._v("후기 작성하고 적립금 받으세요!")])]),e.info.feedback.length?s("ul",{staticClass:"review__list"},e._l(e.info.feedback,(function(t,i){return s("li",{key:i+"feedback",staticClass:"review__list__box"},[s("a",{attrs:{href:e.goodsLink(t)}},[s("figure",{staticClass:"review__list__thumb",class:{goodsThumbnailDimmed:e.goodsThumbnailDimmed}},["GREENJUICE"==t.packType?s("img",{directives:[{name:"lazy",rawName:"v-lazy",value:e.$APP_CONFIG.IMAGES.GREEN_JUICE,expression:"$APP_CONFIG.IMAGES.GREEN_JUICE"}],attrs:{alt:""}}):"EXHIBIT"==t.packType?s("img",{directives:[{name:"lazy",rawName:"v-lazy",value:e.$APP_CONFIG.IMAGES.FLAT_PRICE,expression:"$APP_CONFIG.IMAGES.FLAT_PRICE"}],attrs:{alt:""}}):s("img",{directives:[{name:"lazy",rawName:"v-lazy",value:e.mergeImageHost(t.thumbnailPath),expression:"mergeImageHost(feedbacks.thumbnailPath)"}],attrs:{alt:""}})])]),s("p",{staticClass:"review__list__title"},[e._v(e._s("NORMAL"!=t.packType?t.packTitle:t.goodsName))]),s("p",{staticClass:"review__list__sub"},[e._v(e._s("NORMAL"!=t.packType?t.goodsName:""))]),s("dl",{staticClass:"review__list__d-day"},[s("dt",{staticClass:"d-dday__title"},[e._v("작성 가능일")]),s("dd",{staticClass:"d-day__date"},["Y"!=t.experienceYn?[e._v("D-"+e._s(t.dday>0?t.dday:"DAY"))]:e._e()],2)]),s("button",{staticClass:"review__list__btn fb__btn__medium--white",attrs:{type:"button"},on:{click:function(s){return e.openReviewWhite(t)}}},["Y"==t.experienceYn?[e._v("체험단 ")]:e._e(),e._v(" 상품후기 작성 ")],2)])})),0):s("div",{staticClass:"review__empty"},[s("p",[e._v("작성 가능한 상품후기가 없습니다.")])])]),s("div",{staticClass:"index__activity__inner"},[s("div",{staticClass:"index__activity__qa"},[s("a",{staticClass:"qa__title",attrs:{href:e.$APP_CONFIG.PAGE_URL.MYPAGE_QNA}},[e._v("1:1 문의 현황")]),e.info.onetoone.length?s("ul",{staticClass:"qa__personal"},e._l(e.info.onetoone,(function(t,i){return s("li",{key:i+"onetoones",staticClass:"qa__personal__box"},[s("a",{staticClass:"qa__personal__link",attrs:{href:e.$APP_CONFIG.PAGE_URL.MYPAGE_QNA+"?q="+t.csQnaId}},[s("span",{staticClass:"qa__personal__type"},[e._v(e._s(t.onetooneTypeName))]),s("p",{staticClass:"qa__personal__title"},[e._v(e._s(t.title))]),s("span",{staticClass:"qa__personal__status",class:{answer:"답변완료"==t.statusName}},[e._v(e._s(t.statusName))])])])})),0):s("div",{staticClass:"qa__empty"},[s("p",[e._v("등록된 문의 내역이 없습니다.")])])]),s("div",{staticClass:"index__activity__qa"},[s("a",{staticClass:"qa__title",attrs:{href:e.$APP_CONFIG.PAGE_URL.MYPAGE_INQUIRY}},[e._v("상품 문의 현황")]),e.info.product.length?s("ul",{staticClass:"qa__goods"},e._l(e.info.product,(function(t,i){return s("li",{key:i+"products",staticClass:"qa__goods__box"},[s("a",{attrs:{href:e.$APP_CONFIG.PAGE_URL.MYPAGE_INQUIRY+"?id="+t.csQnaId}},[s("div",{staticClass:"qa__goods__info"},[s("figure",{staticClass:"info__thumb",class:{goodsThumbnailDimmed:e.goodsThumbnailDimmed}},[s("img",{directives:[{name:"lazy",rawName:"v-lazy",value:e.mergeImageHost(t.thumbnailPath),expression:"mergeImageHost(products.thumbnailPath)"}],attrs:{alt:t.goodsName}})]),s("div",{staticClass:"info__detail"},[s("p",{staticClass:"info__title"},[e._v(e._s(t.goodsName))])])]),s("div",{staticClass:"qa__goods__inquiry"},[s("p",{staticClass:"qa__goods__title"},[e._v(e._s(t.title))]),s("span",{staticClass:"qa__goods__status",class:{answer:"답변완료"==t.statusName}},[e._v(e._s(t.statusName))])])])])})),0):s("div",{staticClass:"qa__empty"},[s("p",[e._v("등록된 문의 내역이 없습니다.")])])])])])]):s("div",{staticClass:"fb__mypage__loading"},[s("div",{staticClass:"fb__fetching"})])])]),s("fb-footer"),s("fb-modal",{attrs:{open:e.reviewLayer.open,isCloseButton:!0,classes:"fb__modal__Write",isMaskBackground:!0,isBackgroundClose:!1},on:{"close-modal":function(t){return e.closeReviewWhite({e:t,response:"close"})}}},[s("review-write",{attrs:{item:e.reviewLayer.item},on:{"close-modal":e.closeReviewWhite}})],1),s("fb-alert",{attrs:{open:e.alert.open,message:e.alert.message},on:{"close-alert":function(t){return e.closeAlert(t)}}}),s("fb-confirm",{attrs:{open:e.confirm.open,width:e.confirm.width,height:e.confirm.height,title:e.confirm.title,message:e.confirm.message,ok:e.confirm.ok,cancel:e.confirm.cancel,"is-background":e.confirm.isBackground,"is-background-close":e.confirm.isBackgroundClose},on:{"close-confirm":function(t){return e.closeConfirm(t)}}})],1):e._e()}),_=[function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("h3",{staticClass:"index__cont__title"},[s("span",[e._v("주문배송 현황")]),s("span",{staticClass:"index__cont__title--noti"},[e._v("최근 3개월내 주문 배송 현황입니다.")])])},function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("header",{staticClass:"index__cont__header"},[s("h3",{staticClass:"index__cont__title"},[e._v(" 나의 활동 ")])])}],n=(s("a434"),s("d3b7"),s("ac1f"),s("3ca3"),s("841c"),s("ddb0"),s("2b3d"),s("96cf"),s("1da1")),o=s("dcbe"),r=s("4402"),l=s("7fcc"),c=s("8fc3"),d=s("9d0b"),f=s("aa0e"),u=s("863d"),v=s("0bf4"),p={name:"mypage-main",mixins:[v["a"]],components:{fbHeader:o["a"],fbFooter:r["a"],fbMypageMenu:l["a"],reviewWrite:c["a"],fbAlert:d["a"],fbConfirm:f["a"],fbModal:u["a"]},data:function(){return{resultData:{},info:null,loading:!1,reviewOb:null,goodsReviewList:[],reviewLayer:{item:{},open:!1}}},created:function(){if(this.userSession.isLogin){this.getQueryString();this.getMypageMainInfo()}},mounted:function(){},watch:{},computed:{isOrderMember:function(){return!this.userSession.isLogin}},methods:{getQueryString:function(){return new URLSearchParams(window.location.search)},closeReviewWhite:function(e){var t;(null===e||void 0===e||null===(t=e.response)||void 0===t?void 0:t.code)==this.$FB_CODES.API.SUCCESS?(this.reviewLayer.item={},this.reviewLayer.open=!1,location.reload()):"close"==(null===e||void 0===e?void 0:e.response)&&this.openConfirm({type:"cancel",message:this.$FB_MESSAGES.SYSTEM.CONFIRM_07})},openReviewWhite:function(e){console.log(e),e?(this.reviewLayer.item=e,this.reviewLayer.open=!0):this.openAlert(this.$FB_MESSAGES.ERROR.DEFAULT)},closeConfirm:function(e){var t=e.value;if(t)switch(this.confirm.type){case"cancel":this.reviewLayer.item={},this.reviewLayer.open=!1,this.resetConfirm()}this.resetConfirm()},goodsLink:function(e){var t="";return t="GREENJUICE"==e.packType?this.$APP_CONFIG.PAGE_URL.PROMOTION_FREE_ORDER:"EXHIBIT"==e.packType?this.$APP_CONFIG.PAGE_URL.PROMOTION_PICKING_OUT_VIEW+"?select="+e.evEventId:"PACKAGE"==e.packType?this.$APP_CONFIG.PAGE_URL.GOODS_VIEW+"?goods="+e.packGoodsId:this.$APP_CONFIG.PAGE_URL.GOODS_VIEW+"?goods="+e.ilGoodsId,t},getMypageMainInfo:function(){var e=this;return Object(n["a"])(regeneratorRuntime.mark((function t(){var s;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.prev=0,t.next=3,e.$store.dispatch("network/request",{method:"get",url:"/user/buyer/getMypageMainInfo"});case 3:s=t.sent,s.code==e.$FB_CODES.API.SUCCESS&&(e.info=s.data,e.info.product&&e.info.product.length>2&&(e.info.product=e.info.product.splice(0,2)),e.loading=!0),t.next=10;break;case 7:t.prev=7,t.t0=t["catch"](0),console.error("getMypageMainInfo error...",t.t0.message);case 10:case"end":return t.stop()}}),t,null,[[0,7]])})))()}}},C=p,m=(s("40d1"),s("2877")),h=Object(m["a"])(C,a,_,!1,null,null,null),P=h.exports,b=s("8f7d"),g=s("caf9");b["a"].use(g["a"],{error:b["b"].IMAGES.NOT_FOUND,loading:b["b"].IMAGES.IMG_LOADING}),b["c"].components=Object(i["a"])({App:P},b["c"].components),b["d"].dispatch("init").then((function(){return new b["a"](b["c"])}))}});