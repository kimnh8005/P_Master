(function(e){function t(t){for(var n,s,o=t[0],l=t[1],c=t[2],_=0,u=[];_<o.length;_++)s=o[_],Object.prototype.hasOwnProperty.call(i,s)&&i[s]&&u.push(i[s][0]),i[s]=0;for(n in l)Object.prototype.hasOwnProperty.call(l,n)&&(e[n]=l[n]);d&&d(t);while(u.length)u.shift()();return r.push.apply(r,c||[]),a()}function a(){for(var e,t=0;t<r.length;t++){for(var a=r[t],n=!0,o=1;o<a.length;o++){var l=a[o];0!==i[l]&&(n=!1)}n&&(r.splice(t--,1),e=s(s.s=a[0]))}return e}var n={},i={"mobile/main/lohas/index":0},r=[];function s(t){if(n[t])return n[t].exports;var a=n[t]={i:t,l:!1,exports:{}};return e[t].call(a.exports,a,a.exports,s),a.l=!0,a.exports}s.m=e,s.c=n,s.d=function(e,t,a){s.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:a})},s.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},s.t=function(e,t){if(1&t&&(e=s(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var a=Object.create(null);if(s.r(a),Object.defineProperty(a,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var n in e)s.d(a,n,function(t){return e[t]}.bind(null,n));return a},s.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return s.d(t,"a",t),t},s.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},s.p="/";var o=window["webpackJsonp"]=window["webpackJsonp"]||[],l=o.push.bind(o);o.push=t,o=o.slice();for(var c=0;c<o.length;c++)t(o[c]);var d=l;r.push([42,"chunk-commons","chunk-vendors"]),a()})({42:function(e,t,a){e.exports=a("c9cb")},c42f:function(e,t,a){"use strict";var n=a("cd0b"),i=a.n(n);i.a},c9cb:function(e,t,a){"use strict";a.r(t);var n=a("5530"),i=(a("e260"),a("e6cf"),a("cca6"),a("a79d"),function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("main",{staticClass:"fb__lohas"},[a("fb-header",{attrs:{type:"main",brand:"MALL_DIV.PULMUONE"}}),e.isPageError&&!e.isReloading?[a("error-layout",{staticClass:"fb__error--main",attrs:{"error-type":"default"}},[[a("div",{staticClass:"fb__btn-wrap margin"},[a("button",{staticClass:"fb__btn-margin--white",attrs:{type:"button",tabindex:"0"},on:{click:e.reloadPage}},[e._v("새로고침")]),a("button",{staticClass:"fb__btn-margin--green",attrs:{type:"button",tabindex:"0"},on:{click:function(t){return e.goToRoute(e.$APP_CONFIG.PAGE_URL.ROOT)}}},[e._v("홈으로")])])]],2)]:e._e(),e.$FB_CODES.FETCHES.WAIT===e.fetches.pageInventory||e.isReloading?a("div",{staticClass:"fb__loading"},[a("div",{staticClass:"fb__fetching"})]):e.$FB_CODES.FETCHES.SUCCESS===e.fetches.pageInventory?[a("div",{staticClass:"lohas__inner"},[e.mainBanner&&e.mainBanner.length>0?a("section",{staticClass:"lohas__main-banner"},[e.mainBanner.length>1?[a("div",{staticClass:"main-banner__swiper"},[a("swiper",{ref:"mainBannerSwiper",attrs:{options:e.swipers.mainBanner}},e._l(e.mainBanner,(function(t,n){return a("swiper-slide",{key:n+"-billboardBanner",ref:"mainBannerList",refInFor:!0},[a("a",{staticClass:"ga_lohas_main_banner",attrs:{href:t.linkUrlMobile,"data-ga-banner-name":t.titleName}},[a("figure",{staticClass:"main-banner__image"},[a("img",{attrs:{src:e.mergeImageHost(t.imagePathMobile),alt:t.titleName}})])])])})),1),e._m(0),a("div",{staticClass:"control-area"},[a("div",{staticClass:"swiper__pagination",attrs:{slot:"pagination"},slot:"pagination"}),a("button",{staticClass:"btn__view-all",attrs:{type:"button"},on:{click:function(t){return t.preventDefault(),e.goToRoute("/lohasAll")}}},[e._v("전체보기")])])],1)]:1===e.mainBanner.length?[a("div",{staticClass:"main-banner__item"},[a("a",{staticClass:"ga_lohas_main_banner",attrs:{href:e.mainBanner[0].linkUrlPc,"data-ga-banner-name":e.mainBanner[0].titleName}},[a("figure",{staticClass:"main-banner__image"},[a("img",{directives:[{name:"lazy",rawName:"v-lazy",value:e.mergeImageHost(e.mainBanner[0].imagePathMobile),expression:"mergeImageHost(mainBanner[0].imagePathMobile)"}],attrs:{alt:e.mainBanner[0].titleName}})])])])]:e._e()],2):e._e(),a("section",{staticClass:"lohas__contents"},[e.middleBanner&&e.middleBanner.imagePathMobile?a("div",{staticClass:"lohas__middle-banner"},[a("a",{staticClass:"ga_lohas_middle_banner",attrs:{href:e.middleBanner.linkUrlMobile,"data-ga-banner-name":e.middleBanner.titleName}},[a("figure",{staticClass:"middle-banner__img"},[a("img",{directives:[{name:"lazy",rawName:"v-lazy",value:e.mergeImageHost(e.middleBanner.imagePathMobile),expression:"mergeImageHost(middleBanner.imagePathMobile)"}],attrs:{alt:e.middleBanner.titleName}})])])]):e._e(),e.isValidateArray(e.lohasCategories)?a("div",{ref:"category",staticClass:"lohas__category",class:{fixedCategory:e.isNavFixed}},[a("div",{ref:"nav",staticClass:"lohas__nav__wrap"},[a("nav",{staticClass:"lohas__nav fb__category-tab"},e._l(e.lohasCategories,(function(t,n){return a("label",{key:"lohasTab-"+n,ref:"navCategory",refInFor:!0,staticClass:"lohas__nav__list fb__category-tab__list",class:{active:n==e.activeIndex}},[a("input",{directives:[{name:"model",rawName:"v-model",value:e.activeIndex,expression:"activeIndex"}],staticClass:"ga_lohas_product_page_category",attrs:{type:"radio","data-ga-category":t.categoryName},domProps:{value:n,checked:e._q(e.activeIndex,n)},on:{click:function(t){return e.moveToProductTab(t,n)},change:function(t){e.activeIndex=n}}}),a("span",{staticClass:"lohas__nav__btn fb__category-tab__btn"},[e._v(e._s(t.categoryName))])])})),0)]),a("div",{staticClass:"lohas__products__wrap"},[e._l(e.lohasCategories,(function(t,n){return[e.isValidateArray(t.level2)?[a("div",{key:"lohasItem-"+n,ref:"goodsItem",refInFor:!0,staticClass:"lohas__products",attrs:{"data-idx":n}},[a("div",{staticClass:"lohas__products__content"},[a("div",{staticClass:"lohas__products__banner"},[a("a",{staticClass:"ga_lohas_product_banner",attrs:{href:t.level2[0].linkUrlMobile,"data-ga-banner-name":t.level2[0].titleName}},[a("figure",{staticClass:"banner__img",attrs:{title:t.level2[0].titleName}},[a("img",{directives:[{name:"lazy",rawName:"v-lazy",value:e.mergeImageHost(t.level2[0].imagePathMobile),expression:"mergeImageHost(item.level2[0].imagePathMobile)"}],attrs:{alt:t.level2[0].titleName}})])])]),e.isValidateArray(t.level2[0].level3)?[a("div",{staticClass:"lohas__products__item"},[a("fb-product",{attrs:{viewType:["wide"],contentType:"lohas","product-list":e.filterGoodsInfo(t.level2[0].level3).slice(0,4),PageCd:"P_MO_GNB1",ContentCd:String(t.dpCtgryId)}})],1)]:e._e()],2),a("a",{staticClass:"btn__view-more",attrs:{href:t.level2[0].linkUrlMobile,type:"button"}},[e._v("상품 더 보러가기")])])]:e._e()]}))],2)]):e._e()])])]:e._e(),a("fb-footer"),a("fb-dockbar")],2)}),r=[function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"swiper__nav__area"},[a("span",{staticClass:"swiper__nav swiper__nav-prev"}),a("span",{staticClass:"swiper__nav swiper__nav-next"})])}],s=(a("4160"),a("a630"),a("a9e3"),a("b680"),a("3ca3"),a("159b"),a("0bf4")),o=a("fc84"),l=a("1dfd"),c=a("8e32"),d=a("629b"),u=a("cbaa"),m=a("3ed5"),f=a("7212"),v=(a("a7a3"),{name:"lohas",extends:l["a"],mixins:[s["a"],o["a"]],components:{fbHeader:c["a"],fbFooter:d["a"],fbDockbar:u["a"],fbProduct:m["a"],Swiper:f["Swiper"],SwiperSlide:f["SwiperSlide"]},data:function(){return{inventoryList:null,mainBanner:null,middleBanner:{linkUrlMobile:null,imagePathMobile:null,titleName:null},goods:null,activeIndex:0,swipers:{mainBanner:{loop:!0,autoplay:{delay:3e3},pagination:{el:".swiper__pagination",type:"fraction"},navigation:{prevEl:".swiper__nav-prev",nextEl:".swiper__nav-next"}}},activeFixed:!1,isNavFixed:!1}},created:function(){},watch:{activeIndex:{handler:function(e,t){if(e){var a=this.$refs.navCategory[e];a&&a.scrollIntoView({inline:"end",block:"nearest"})}}}},methods:{onScroll:function(){var e=this,t=this.$refs["category"],a=this.$refs["nav"];if(t&&a){var n=a.getBoundingClientRect().height,i=t.getBoundingClientRect().top;i<=0?this.isNavFixed||(this.isNavFixed=!0):this.isNavFixed&&(this.isNavFixed=!1);var r=null,s=this.$refs["goodsItem"];if(s&&(Array.from(s).forEach((function(t){var a=t.getBoundingClientRect();if(e.toFixedNumber(n)>=e.toFixedNumber(a.top)+e.getProductPaddingTop()){if(r===t)return;r=t}})),r)){var o=r.dataset.idx;this.activeIndex=o}}},moveToProductTab:function(e,t){var a=this.$refs["goodsItem"][t];if(a){var n=pageYOffset||scrollY,i=a.getBoundingClientRect(),r=this.$refs.nav.offsetHeight,s=Math.ceil(i.top)+n-r;if(window.scrollTo(0,s+this.getProductPaddingTop()),this.isMobile){var o=e.target.closest(".fb__category-tab__list");o.scrollIntoView({inline:"end",block:"nearest"})}}},setScrollEvent:function(){document.addEventListener("scroll",_.throttle(this.onScroll,100))},moveCategoryList:function(e){var t=document.querySelector(".lohas__nav.fb__category-tab"),a=t.querySelectorAll(".lohas__nav__list");a[e]&&a[e].scrollIntoView()},getProductPaddingTop:function(){var e=Math.max(document.documentElement.clientWidth||0,window.innerWidth||0),t=e*(40/360);return this.toFixedNumber(t)},toFixedNumber:function(e,t){return Number(e.toFixed(t))}}}),g=v,b=(a("c42f"),a("2877")),p=Object(b["a"])(g,i,r,!1,null,null,null),h=p.exports,y=a("dd69"),C=a("caf9");y["a"].use(C["a"],{error:y["b"].IMAGES.NOT_FOUND,loading:y["b"].IMAGES.IMG_LOADING}),y["c"].components=Object(n["a"])({App:h},y["c"].components),y["d"].dispatch("init").then((function(){return new y["a"](y["c"])}))},cd0b:function(e,t,a){}});