
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RecordedSimulation extends Simulation {

	val httpProtocol = http
		.baseURL("https://maps.googleapis.com")
		.inferHtmlResources()

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate, sdch",
		"Accept-Language" -> "en,pt;q=0.8,es;q=0.6",
		"Cache-Control" -> "max-age=0",
		"Connection" -> "keep-alive",
		"If-Modified-Since" -> "Wed, 18 Jan 2017 16:47:40 GMT",
		"Upgrade-Insecure-Requests" -> "1",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")

	val headers_1 = Map("User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")

	val headers_11 = Map(
		"Accept" -> "application/json, text/plain, */*",
		"Accept-Encoding" -> "gzip, deflate, sdch",
		"Accept-Language" -> "en,pt;q=0.8,es;q=0.6",
		"Connection" -> "keep-alive",
		"Origin" -> "http://alpha.bubbl.tech",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")

	val headers_12 = Map(
		"Accept" -> "application/json, text/plain, */*",
		"Accept-Encoding" -> "gzip, deflate, sdch",
		"Accept-Language" -> "en,pt;q=0.8,es;q=0.6",
		"Connection" -> "keep-alive",
		"If-Modified-Since" -> "Wed, 18 Jan 2017 16:47:41 GMT",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")

	val headers_13 = Map(
		"Accept" -> "text/html",
		"Accept-Encoding" -> "gzip, deflate, sdch",
		"Accept-Language" -> "en,pt;q=0.8,es;q=0.6",
		"Connection" -> "keep-alive",
		"If-Modified-Since" -> "Wed, 18 Jan 2017 16:47:42 GMT",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")

	val headers_14 = Map(
		"Accept" -> "application/json, text/plain, */*",
		"Accept-Encoding" -> "gzip, deflate, sdch",
		"Accept-Language" -> "en,pt;q=0.8,es;q=0.6",
		"Connection" -> "keep-alive",
		"If-Modified-Since" -> "Wed, 18 Jan 2017 16:47:40 GMT",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")

	val headers_24 = Map(
		"Accept" -> "application/json, text/plain, */*",
		"Accept-Encoding" -> "gzip, deflate, sdch",
		"Accept-Language" -> "en,pt;q=0.8,es;q=0.6",
		"Connection" -> "keep-alive",
		"If-Modified-Since" -> "Wed, 18 Jan 2017 16:47:43 GMT",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")

	val headers_99 = Map(
		"Accept" -> "application/json, text/plain, */*",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "en,pt;q=0.8,es;q=0.6",
		"Connection" -> "keep-alive",
		"Content-Type" -> "application/json;charset=UTF-8",
		"Origin" -> "http://alpha.bubbl.tech",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")

	val headers_100 = Map(
		"Accept" -> "text/html",
		"Accept-Encoding" -> "gzip, deflate, sdch",
		"Accept-Language" -> "en,pt;q=0.8,es;q=0.6",
		"Authorization" -> "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkB0aGlua2lubm92YXRlLmNvLnVrIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VfQ09NUEFOWSIsIlJPTEVfQURNSU4iXSwiaWF0IjoxNDg1MTc1MzUzfQ.DfEgRVKz9oHwddOig79RXJxR6fL0s_Z7ScZVsMvPh7U",
		"Connection" -> "keep-alive",
		"If-Modified-Since" -> "Wed, 18 Jan 2017 16:47:41 GMT",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")

	val headers_101 = Map(
		"Accept" -> "application/json, text/plain, */*",
		"Accept-Encoding" -> "gzip, deflate, sdch",
		"Accept-Language" -> "en,pt;q=0.8,es;q=0.6",
		"Authorization" -> "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkB0aGlua2lubm92YXRlLmNvLnVrIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VfQ09NUEFOWSIsIlJPTEVfQURNSU4iXSwiaWF0IjoxNDg1MTc1MzUzfQ.DfEgRVKz9oHwddOig79RXJxR6fL0s_Z7ScZVsMvPh7U",
		"Connection" -> "keep-alive",
		"If-Modified-Since" -> "Wed, 18 Jan 2017 16:47:40 GMT",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")

	val headers_102 = Map(
		"Accept" -> "application/json, text/plain, */*",
		"Accept-Encoding" -> "gzip, deflate, sdch",
		"Accept-Language" -> "en,pt;q=0.8,es;q=0.6",
		"Authorization" -> "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkB0aGlua2lubm92YXRlLmNvLnVrIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VfQ09NUEFOWSIsIlJPTEVfQURNSU4iXSwiaWF0IjoxNDg1MTc1MzUzfQ.DfEgRVKz9oHwddOig79RXJxR6fL0s_Z7ScZVsMvPh7U",
		"Connection" -> "keep-alive",
		"Origin" -> "http://alpha.bubbl.tech",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")

	val headers_106 = Map(
		"Accept" -> "*/*",
		"Accept-Encoding" -> "gzip, deflate, sdch",
		"Accept-Language" -> "en,pt;q=0.8,es;q=0.6",
		"Connection" -> "keep-alive",
		"Origin" -> "http://alpha.bubbl.tech",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")

	val headers_111 = Map(
		"Accept" -> "*/*",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "en,pt;q=0.8,es;q=0.6",
		"Connection" -> "keep-alive",
		"Content-Type" -> "text/plain;charset=UTF-8",
		"Origin" -> "http://alpha.bubbl.tech",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")

	val headers_152 = Map(
		"Accept" -> "*/*",
		"Accept-Encoding" -> "gzip, deflate, sdch",
		"Accept-Language" -> "en,pt;q=0.8,es;q=0.6",
		"Access-Control-Request-Headers" -> "authorization",
		"Access-Control-Request-Method" -> "GET",
		"Connection" -> "keep-alive",
		"Origin" -> "http://alpha.bubbl.tech",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")

	val headers_156 = Map(
		"Accept" -> "image/webp,image/*,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate, sdch",
		"Accept-Language" -> "en,pt;q=0.8,es;q=0.6",
		"Connection" -> "keep-alive",
		"If-Modified-Since" -> "Wed, 18 Jan 2017 16:47:40 GMT",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")

    val uri01 = "maps.googleapis.com"
    val uri02 = "https://www.google-analytics.com"
    val uri03 = "https://csi.gstatic.com/csi"
    val uri04 = "https://www.youtube.com/iframe_api"
    val uri05 = "http://localhost:35759/livereload.js"
    val uri06 = "http://api-alpha.bubbl.tech"
    val uri07 = "https://maps.gstatic.com/mapfiles"
    val uri08 = "https://cdn.jsdelivr.net/alasql/0.3.1/alasql.min.js"
    val uri09 = "https://fonts.gstatic.com/s"
    val uri10 = "https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css"
    val uri11 = "http://canvg.github.io/canvg"
    val uri12 = "https://s.ytimg.com/yts/jsbin/www-widgetapi-vflibYk8g/www-widgetapi.js"
    val uri13 = "http://alpha.bubbl.tech"
    val uri14 = "https://fonts.googleapis.com"

	val scn = scenario("RecordedSimulation")
		.exec(http("request_0")
			.get(uri13 + "/")
			.headers(headers_0)
			.resources(http("request_1")
			.get(uri10 + "")
			.headers(headers_1),
            http("request_2")
			.get(uri02 + "/analytics.js")
			.headers(headers_1),
            http("request_3")
			.get(uri09 + "/roboto/v15/2tsd397wLxj96qwHyNIkxPesZW2xOQ-xsNqO47m55DA.woff2"),
            http("request_4")
			.get(uri09 + "/roboto/v15/Hgo13k-tfSpn0qi1SFdUfVtXRa8TVwTICgirnJhmVJw.woff2"),
            http("request_5")
			.get(uri09 + "/roboto/v15/CWB0XYA8bzo0kSThX0UTuA.woff2"),
            http("request_6")
			.get(uri09 + "/roboto/v15/RxZJdnzeo3R5zSexge8UUVtXRa8TVwTICgirnJhmVJw.woff2"),
            http("request_7")
			.get(uri09 + "/roboto/v15/d-6IYplOFocCacKzxwXSOFtXRa8TVwTICgirnJhmVJw.woff2"),
            http("request_8")
			.get(uri09 + "/materialicons/v20/2fcrYFNaTjcS6g4U3t-Y5ZjZjT5FdEJ140U2DJYC3mY.woff2"),
            http("request_9")
			.get(uri03 + "?v=2&s=mapsapi3&v3v=27.9&action=apiboot2&libraries=drawing%2Cplaces%2Cvisualization&e=10_1_0,10_2_0&rt=main.65")
			.headers(headers_1),
            http("request_10")
			.get(uri12 + "")
			.headers(headers_1),
            http("request_11")
			.get(uri06 + "/health")
			.headers(headers_11),
            http("request_12")
			.get(uri13 + "/js/pages/app/app.html")
			.headers(headers_12),
            http("request_13")
			.get(uri13 + "/js/pages/user/login/login.html")
			.headers(headers_13),
            http("request_14")
			.get(uri13 + "/js/components/directives/snackbar/snackbar.html")
			.headers(headers_14),
            http("request_15")
			.get(uri13 + "/js/components/directives/header/header.html")
			.headers(headers_14),
            http("request_16")
			.get(uri13 + "/js/components/directives/sidebar/sidebar.html")
			.headers(headers_14),
            http("request_17")
			.get(uri13 + "/img/logos/bubbl_websafe_small.png"),
            http("request_18")
			.get("/maps-api-v3/api/js/27/9/common.js")
			.headers(headers_1),
            http("request_19")
			.get("/maps-api-v3/api/js/27/9/map.js")
			.headers(headers_1),
            http("request_20")
			.get(uri02 + "/collect?v=1&_v=j47&a=803339347&t=pageview&_s=1&dl=http%3A%2F%2Falpha.bubbl.tech%2F&dp=%2Fuser%2Flogin&ul=en-us&de=UTF-8&dt=Bubbl&sd=24-bit&sr=1920x1080&vp=1919x466&je=0&fl=24.0%20r0&_utma=115324308.1369704918.1481647806.1484754235.1484756403.8&_utmz=115324308.1481647806.1.1.utmcsr%3D(direct)%7Cutmccn%3D(direct)%7Cutmcmd%3D(none)&_utmht=1485175329703&_u=CCECCMABJ~&jid=&cid=1369704918.1481647806&tid=UA-66420404-2&z=1917149827")
			.headers(headers_1),
            http("request_21")
			.get(uri03 + "?v=2&s=mapsapi3&v3v=27.9&action=apiboot2&libraries=drawing%2Cplaces%2Cvisualization&e=10_1_0,10_2_0&rt=firstmap.1600")
			.headers(headers_1),
            http("request_22")
			.get(uri13 + "/js/components/directives/confirmdialog/confirmdialogelement.html")
			.headers(headers_14),
            http("request_23")
			.get(uri13 + "/img/icons/ballicons/workspace.svg")
			.headers(headers_1),
            http("request_24")
			.get(uri13 + "/tpl/partials/menu-item.html")
			.headers(headers_24),
            http("request_25")
			.get(uri13 + "/tpl/partials/menu-group.html")
			.headers(headers_24),
            http("request_26")
			.get("/maps-api-v3/api/js/27/9/util.js")
			.headers(headers_1),
            http("request_27")
			.get("/maps-api-v3/api/js/27/9/onion.js")
			.headers(headers_1),
            http("request_28")
			.get(uri07 + "/openhand_8_8.cur"),
            http("request_29")
			.get("/maps/api/js/ViewportInfoService.GetViewportInfo?1m6&1m2&1d53.98529289842936&2d-2.7950582737310015&2m2&1d54.02172973883544&2d-2.3002224733775165&2u14&4sen&5e4&6sr%40372000000&7b0&8e0&callback=_xdc_._9cxcoh&token=70708")
			.headers(headers_1),
            http("request_30")
			.get("/maps-api-v3/api/js/27/9/stats.js")
			.headers(headers_1),
            http("request_31")
			.get(uri03 + "?v=2&s=mapsapi3&v3v=27.9&action=map2&firstmap=true&hdpi=false&mob=false&staticmap=false&size=1919x233&hadviewport=true&libraries=drawing%2Cplaces%2Cvisualization&e=10_1_0,10_2_0&rt=visreq.493")
			.headers(headers_1),
            http("request_32")
			.get("/maps-api-v3/api/js/27/9/controls.js")
			.headers(headers_1),
            http("request_33")
			.get("/maps/gen_204?target=api&ev=api_viewport&cad=host:alpha.bubbl.tech,v:27,r:1,mt:r,c:54.00366%2C-2.547855,sp:0.01175x0.16471,size:1919x233,relsize:0.50,token:6u7c76kcre,src:apiv3,ts:a31n67")
			.headers(headers_1),
            http("request_34")
			.get(uri14 + "/css?family=Roboto:300,400,500,700")
			.headers(headers_1),
            http("request_35")
			.get(uri07 + "/transparent.png")
			.headers(headers_1),
            http("request_36")
			.get(uri03 + "?v=2&s=mapsapi3&v3v=27.9&action=map2&firstmap=true&hdpi=false&mob=false&staticmap=false&size=1919x233&hadviewport=true&libraries=drawing%2Cplaces%2Cvisualization&e=10_1_0,10_2_0&rt=visres.721")
			.headers(headers_1),
            http("request_37")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8075!3i5260!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=18154")
			.headers(headers_1),
            http("request_38")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8076!3i5259!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=78155")
			.headers(headers_1),
            http("request_39")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8076!3i5260!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=30556")
			.headers(headers_1),
            http("request_40")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8075!3i5259!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=65753")
			.headers(headers_1),
            http("request_41")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8074!3i5260!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=5752")
			.headers(headers_1),
            http("request_42")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8074!3i5259!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=53351")
			.headers(headers_1),
            http("request_43")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8077!3i5259!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=90557")
			.headers(headers_1),
            http("request_44")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8077!3i5260!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=42958")
			.headers(headers_1),
            http("request_45")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8073!3i5259!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=40949")
			.headers(headers_1),
            http("request_46")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8073!3i5260!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=124421")
			.headers(headers_1),
            http("request_47")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8078!3i5259!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=102959")
			.headers(headers_1),
            http("request_48")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8078!3i5260!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=55360")
			.headers(headers_1),
            http("request_49")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8072!3i5260!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=112019")
			.headers(headers_1),
            http("request_50")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8072!3i5259!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=28547")
			.headers(headers_1),
            http("request_51")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8079!3i5259!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=115361")
			.headers(headers_1),
            http("request_52")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8079!3i5260!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=67762")
			.headers(headers_1),
            http("request_53")
			.get(uri07 + "/api-3/images/google4.png"),
            http("request_54")
			.get(uri07 + "/api-3/images/mapcnt6.png"),
            http("request_55")
			.get(uri07 + "/api-3/images/sv9.png"),
            http("request_56")
			.get("/maps/api/js/AuthenticationService.Authenticate?1shttp%3A%2F%2Falpha.bubbl.tech%2F%23%2Fuser%2Flogin&callback=_xdc_._eblwck&token=104235")
			.headers(headers_1),
            http("request_57")
			.get(uri03 + "?v=2&s=mapsapi3&v3v=27.9&action=map2&firstmap=true&hdpi=false&mob=false&staticmap=false&size=1919x233&hadviewport=true&libraries=drawing%2Cplaces%2Cvisualization&e=10_1_0,10_2_0&rt=firsttile.890,firstpixel.890")
			.headers(headers_1),
            http("request_58")
			.get(uri03 + "?v=2&s=mapsapi3&v3v=27.9&action=map2&firstmap=true&hdpi=false&mob=false&staticmap=false&size=1919x233&hadviewport=true&libraries=drawing%2Cplaces%2Cvisualization&e=10_1_0,10_2_0&rt=tilesloaded.891,allpixels.891")
			.headers(headers_1),
            http("request_59")
			.get("/maps/vt?pb=!1m4!1m3!1i14!2i8072!3i5259!1m4!1m3!1i14!2i8073!3i5259!1m4!1m3!1i14!2i8074!3i5259!1m4!1m3!1i14!2i8075!3i5259!1m4!1m3!1i14!2i8072!3i5260!1m4!1m3!1i14!2i8073!3i5260!1m4!1m3!1i14!2i8074!3i5260!1m4!1m3!1i14!2i8075!3i5260!1m4!1m3!1i14!2i8076!3i5259!1m4!1m3!1i14!2i8077!3i5259!1m4!1m3!1i14!2i8078!3i5259!1m4!1m3!1i14!2i8079!3i5259!1m4!1m3!1i14!2i8076!3i5260!1m4!1m3!1i14!2i8077!3i5260!1m4!1m3!1i14!2i8078!3i5260!1m4!1m3!1i14!2i8079!3i5260!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053850!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e3!12m1!5b1&callback=_xdc_._ss5vle&token=87369")
			.headers(headers_1)))
		.pause(2)
		.exec(http("request_60")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8080!3i5259!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=82228")
			.headers(headers_1)
			.resources(http("request_61")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8080!3i5260!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=34629")
			.headers(headers_1),
            http("request_62")
			.get("/maps/vt?pb=!1m4!1m3!1i14!2i8080!3i5259!1m4!1m3!1i14!2i8080!3i5260!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053850!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e3!12m1!5b1&callback=_xdc_._ok5is3&token=17686")
			.headers(headers_1)))
		.pause(2)
		.exec(http("request_63")
			.get("/maps/api/js/QuotaService.RecordEvent?1shttp%3A%2F%2Falpha.bubbl.tech%2F%23%2Fuser%2Flogin&5e0&6u1&7sa31r9r&callback=_xdc_._30s0r9&token=9633")
			.headers(headers_1)
			.resources(http("request_64")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8076!3i5261!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=28166")
			.headers(headers_1),
            http("request_65")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8075!3i5261!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=15764")
			.headers(headers_1),
            http("request_66")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8077!3i5261!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=40568")
			.headers(headers_1),
            http("request_67")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8074!3i5261!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=3362")
			.headers(headers_1),
            http("request_68")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8078!3i5261!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=52970")
			.headers(headers_1),
            http("request_69")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8073!3i5261!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=122031")
			.headers(headers_1),
            http("request_70")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8079!3i5261!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=65372")
			.headers(headers_1),
            http("request_71")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8072!3i5261!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=109629")
			.headers(headers_1),
            http("request_72")
			.get("/maps/vt?pb=!1m5!1m4!1i14!2i8080!3i5261!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=32239")
			.headers(headers_1),
            http("request_73")
			.get("/maps/vt?pb=!1m4!1m3!1i14!2i8072!3i5261!1m4!1m3!1i14!2i8073!3i5261!1m4!1m3!1i14!2i8074!3i5261!1m4!1m3!1i14!2i8075!3i5261!1m4!1m3!1i14!2i8076!3i5261!1m4!1m3!1i14!2i8077!3i5261!1m4!1m3!1i14!2i8078!3i5261!1m4!1m3!1i14!2i8079!3i5261!1m4!1m3!1i14!2i8080!3i5261!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053850!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e3!12m1!5b1&callback=_xdc_._lo3lgt&token=27150")
			.headers(headers_1)))
		.pause(1)
		.exec(http("request_74")
			.get("/maps/api/js/ViewportInfoService.GetViewportInfo?1m6&1m2&1d53.856002357152946&2d-4.513860857592704&2m2&1d54.147522537539146&2d-0.554828730412396&2u11&4sen&5e4&6sr%40372000000&7b0&8e0&callback=_xdc_._7c6plg&token=63530")
			.headers(headers_1)
			.resources(http("request_75")
			.get("/maps/gen_204?target=api&ev=api_viewport&cad=host:alpha.bubbl.tech,v:27,r:1,mt:r,c:54.000002%2C-2.535667,sp:0.09404x1.31767,size:1919x233,relsize:0.50,token:6u7c76kcre,src:apiv3,ts:a31suh")
			.headers(headers_1),
            http("request_76")
			.get("/maps/vt?pb=!1m4!1m3!1i11!2i1005!3i657!1m4!1m3!1i11!2i1005!3i658!1m4!1m3!1i11!2i1006!3i657!1m4!1m3!1i11!2i1007!3i657!1m4!1m3!1i11!2i1006!3i658!1m4!1m3!1i11!2i1007!3i658!1m4!1m3!1i11!2i1008!3i657!1m4!1m3!1i11!2i1009!3i657!1m4!1m3!1i11!2i1008!3i658!1m4!1m3!1i11!2i1009!3i658!1m4!1m3!1i11!2i1010!3i657!1m4!1m3!1i11!2i1011!3i657!1m4!1m3!1i11!2i1010!3i658!1m4!1m3!1i11!2i1011!3i658!1m4!1m3!1i11!2i1012!3i657!1m4!1m3!1i11!2i1013!3i657!1m4!1m3!1i11!2i1012!3i658!1m4!1m3!1i11!2i1013!3i658!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053850!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e3!12m1!5b1&callback=_xdc_._2ydwb0&token=123087")
			.headers(headers_1),
            http("request_77")
			.get("/maps/vt?pb=!1m5!1m4!1i11!2i1009!3i657!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053718!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=1278")
			.headers(headers_1),
            http("request_78")
			.get("/maps/vt?pb=!1m5!1m4!1i11!2i1009!3i658!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053934!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=111394")
			.headers(headers_1),
            http("request_79")
			.get("/maps/vt?pb=!1m5!1m4!1i11!2i1010!3i657!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=111572")
			.headers(headers_1),
            http("request_80")
			.get("/maps/vt?pb=!1m5!1m4!1i11!2i1008!3i657!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053718!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=87009")
			.headers(headers_1),
            http("request_81")
			.get("/maps/vt?pb=!1m5!1m4!1i11!2i1008!3i658!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053718!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=84619")
			.headers(headers_1),
            http("request_82")
			.get("/maps/vt?pb=!1m5!1m4!1i11!2i1010!3i658!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053934!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=108949")
			.headers(headers_1),
            http("request_83")
			.get("/maps/vt?pb=!1m5!1m4!1i11!2i1007!3i658!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053718!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=39279")
			.headers(headers_1),
            http("request_84")
			.get("/maps/vt?pb=!1m5!1m4!1i11!2i1007!3i657!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053718!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=41669")
			.headers(headers_1),
            http("request_85")
			.get("/maps/vt?pb=!1m5!1m4!1i11!2i1011!3i657!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=25841")
			.headers(headers_1),
            http("request_86")
			.get("/maps/vt?pb=!1m5!1m4!1i11!2i1011!3i658!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053934!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=23218")
			.headers(headers_1),
            http("request_87")
			.get("/maps/vt?pb=!1m5!1m4!1i11!2i1006!3i658!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053430!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=14113")
			.headers(headers_1),
            http("request_88")
			.get("/maps/vt?pb=!1m5!1m4!1i11!2i1006!3i657!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053430!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=16503")
			.headers(headers_1),
            http("request_89")
			.get("/maps/vt?pb=!1m5!1m4!1i11!2i1012!3i657!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=71181")
			.headers(headers_1),
            http("request_90")
			.get("/maps/vt?pb=!1m5!1m4!1i11!2i1012!3i658!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053550!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=32284")
			.headers(headers_1),
            http("request_91")
			.get("/maps/vt?pb=!1m5!1m4!1i11!2i1005!3i657!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053430!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=102234")
			.headers(headers_1),
            http("request_92")
			.get("/maps/vt?pb=!1m5!1m4!1i11!2i1005!3i658!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053430!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=99844")
			.headers(headers_1),
            http("request_93")
			.get("/maps/vt?pb=!1m5!1m4!1i11!2i1013!3i657!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053466!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=23371")
			.headers(headers_1),
            http("request_94")
			.get("/maps/vt?pb=!1m5!1m4!1i11!2i1013!3i658!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053598!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=76844")
			.headers(headers_1),
            http("request_95")
			.get("/maps/vt?pb=!1m4!1m3!1i11!2i1005!3i657!1m4!1m3!1i11!2i1005!3i658!1m4!1m3!1i11!2i1006!3i657!1m4!1m3!1i11!2i1007!3i657!1m4!1m3!1i11!2i1006!3i658!1m4!1m3!1i11!2i1007!3i658!1m4!1m3!1i11!2i1008!3i657!1m4!1m3!1i11!2i1009!3i657!1m4!1m3!1i11!2i1008!3i658!1m4!1m3!1i11!2i1009!3i658!1m4!1m3!1i11!2i1010!3i657!1m4!1m3!1i11!2i1011!3i657!1m4!1m3!1i11!2i1010!3i658!1m4!1m3!1i11!2i1011!3i658!1m4!1m3!1i11!2i1012!3i657!1m4!1m3!1i11!2i1013!3i657!1m4!1m3!1i11!2i1012!3i658!1m4!1m3!1i11!2i1013!3i658!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053934!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e3!12m1!5b1&callback=_xdc_._3o4hvn&token=125052")
			.headers(headers_1)))
		.pause(8)
		.exec(http("request_96")
			.get("/maps/vt?pb=!1m5!1m4!1i11!2i1014!3i657!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053466!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=68711")
			.headers(headers_1)
			.resources(http("request_97")
			.get("/maps/vt?pb=!1m5!1m4!1i11!2i1014!3i658!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053922!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=28362")
			.headers(headers_1),
            http("request_98")
			.get("/maps/vt?pb=!1m4!1m3!1i11!2i1014!3i657!1m4!1m3!1i11!2i1014!3i658!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053934!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e3!12m1!5b1&callback=_xdc_._uakaee&token=65837")
			.headers(headers_1)))
		.pause(2)
		.exec(http("request_99")
			.post(uri06 + "/user/login")
			.headers(headers_99)
			.body(RawFileBody("RecordedSimulation_0099_request.txt"))
			.resources(http("request_100")
			.get(uri13 + "/js/pages/dashboard/dashboard.html")
			.headers(headers_100),
            http("request_101")
			.get(uri13 + "/js/components/directives/loader/mdlloader.html")
			.headers(headers_101),
            http("request_102")
			.get(uri06 + "/event/dashboard")
			.headers(headers_102),
            http("request_103")
			.get(uri06 + "/venue")
			.headers(headers_102),
            http("request_104")
			.get(uri06 + "/view/dashboard")
			.headers(headers_102),
            http("request_105")
			.get(uri02 + "/collect?v=1&_v=j47&a=803339347&t=pageview&_s=2&dl=http%3A%2F%2Falpha.bubbl.tech%2F&dp=%2Fdashboard&ul=en-us&de=UTF-8&dt=Bubbl&sd=24-bit&sr=1920x1080&vp=1919x466&je=0&fl=24.0%20r0&_utma=115324308.1369704918.1481647806.1484754235.1484756403.8&_utmz=115324308.1481647806.1.1.utmcsr%3D(direct)%7Cutmccn%3D(direct)%7Cutmcmd%3D(none)&_utmht=1485175349870&_u=CCECCMABJ~&jid=&cid=1369704918.1481647806&tid=UA-66420404-2&z=28701984")
			.headers(headers_1),
            http("request_106")
			.get(uri06 + "/live/info")
			.headers(headers_106),
            http("request_107")
			.get(uri06 + "/event")
			.headers(headers_102),
            http("request_108")
			.get(uri06 + "/chart/platforms")
			.headers(headers_102),
            http("request_109")
			.get(uri06 + "/event")
			.headers(headers_102),
            http("request_110")
			.get(uri06 + "/chart/platforms")
			.headers(headers_102),
            http("request_111")
			.post(uri06 + "/live/536/yiheq4xa/xhr_send")
			.headers(headers_111)
			.body(RawFileBody("RecordedSimulation_0111_request.txt")),
            http("request_112")
			.get("/maps-api-v3/api/js/27/9/geometry.js")
			.headers(headers_1),
            http("request_113")
			.get("/maps-api-v3/api/js/27/9/poly.js")
			.headers(headers_1),
            http("request_114")
			.get("/maps-api-v3/api/js/27/9/marker.js")
			.headers(headers_1),
            http("request_115")
			.get("/maps/api/js/ViewportInfoService.GetViewportInfo?1m6&1m2&1d-90&2d-180&2m2&1d90&2d180&2u0&4sen&5e4&6sr%40372000000&7b0&8e0&callback=_xdc_._8whkwi&token=105264")
			.headers(headers_1),
            http("request_116")
			.get("/maps/gen_204?target=api&ev=api_viewport&cad=host:alpha.bubbl.tech,v:27,r:1,mt:r,c:8.084561%2C0,sp:159.20546x360.00000,size:1919x233,relsize:0.50,token:6u7c76kcre,src:apiv3,ts:a322t2")
			.headers(headers_1),
            http("request_117")
			.get("/maps/vt?pb=!1m4!1m3!1i0!2i0!3i0!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053934!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e3!12m1!5b1&callback=_xdc_._3r226f&token=34793")
			.headers(headers_1),
            http("request_118")
			.get(uri07 + "/transparent.png")
			.headers(headers_1),
            http("request_119")
			.post(uri06 + "/live/536/yiheq4xa/xhr_send")
			.headers(headers_111)
			.body(RawFileBody("RecordedSimulation_0119_request.txt")),
            http("request_120")
			.get(uri07 + "/undo_poly.png")
			.headers(headers_1),
            http("request_121")
			.get("/maps/vt?pb=!1m5!1m4!1i0!2i0!3i0!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053718!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=68953")
			.headers(headers_1),
            http("request_122")
			.get(uri07 + "/api-3/images/spotlight-poi.png")
			.headers(headers_1),
            http("request_123")
			.get("/maps/vt?pb=!1m4!1m3!1i0!2i0!3i0!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053718!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e3!12m1!5b1&callback=_xdc_._ny5rfp&token=75296")
			.headers(headers_1),
            http("request_124")
			.get("/maps/api/js/ViewportInfoService.GetViewportInfo?1m6&1m2&1d53.64991332903507&2d-3.6621897359384548&2m2&1d54.37302085651337&2d-1.4462150549566104&2u10&4sen&5e4&6sr%40372000000&7b0&8e0&callback=_xdc_._s5wzbb&token=34692")
			.headers(headers_1),
            http("request_125")
			.get(uri03 + "?v=2&s=mapsapi3&v3v=27.9&action=map2&firstmap=false&hdpi=false&mob=false&staticmap=true&size=530x289&hadviewport=true&libraries=drawing%2Cplaces%2Cvisualization&e=10_1_0,10_2_0&rt=visreq.75")
			.headers(headers_1),
            http("request_126")
			.get("/maps/gen_204?target=api&ev=api_viewport&cad=host:alpha.bubbl.tech,v:27,r:1,mt:r,c:54.003497%2C-2.547855,sp:0.23326x0.72784,size:530x289,relsize:0.17,token:6u7c76kcre,src:apiv3,ts:a3231g")
			.headers(headers_1),
            http("request_127")
			.get(uri07 + "/api-3/images/google_white5.png")
			.headers(headers_1),
            http("request_128")
			.get(uri03 + "?v=2&s=mapsapi3&v3v=27.9&action=map2&firstmap=false&hdpi=false&mob=false&staticmap=true&size=530x289&hadviewport=true&libraries=drawing%2Cplaces%2Cvisualization&e=10_1_0,10_2_0&rt=visres.211")
			.headers(headers_1),
            http("request_129")
			.get("/maps/vt?pb=!1m5!1m4!1i10!2i504!3i328!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=77485")
			.headers(headers_1),
            http("request_130")
			.get("/maps/vt?pb=!1m5!1m4!1i10!2i504!3i329!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053934!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=74862")
			.headers(headers_1),
            http("request_131")
			.get("/maps/vt?pb=!1m5!1m4!1i10!2i503!3i328!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=32145")
			.headers(headers_1),
            http("request_132")
			.get("/maps/vt?pb=!1m5!1m4!1i10!2i503!3i329!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053538!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=29755")
			.headers(headers_1),
            http("request_133")
			.get("/maps/vt?pb=!1m5!1m4!1i10!2i505!3i328!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053550!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=86318")
			.headers(headers_1),
            http("request_134")
			.get("/maps/vt?pb=!1m5!1m4!1i10!2i505!3i329!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053934!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=120202")
			.headers(headers_1),
            http("request_135")
			.get("/maps/vt?pb=!1m4!1m3!1i10!2i503!3i328!1m4!1m3!1i10!2i503!3i329!1m4!1m3!1i10!2i504!3i328!1m4!1m3!1i10!2i504!3i329!1m4!1m3!1i10!2i505!3i328!1m4!1m3!1i10!2i505!3i329!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053934!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e3!12m1!5b1&callback=_xdc_._bkpo8y&token=127387")
			.headers(headers_1),
            http("request_136")
			.get(uri03 + "?v=2&s=mapsapi3&v3v=27.9&action=map2&firstmap=false&hdpi=false&mob=false&staticmap=true&size=530x289&hadviewport=true&libraries=drawing%2Cplaces%2Cvisualization&e=10_1_0,10_2_0&rt=firsttile.272,firstpixel.272")
			.headers(headers_1),
            http("request_137")
			.get(uri03 + "?v=2&s=mapsapi3&v3v=27.9&action=map2&firstmap=false&hdpi=false&mob=false&staticmap=true&size=530x289&hadviewport=true&libraries=drawing%2Cplaces%2Cvisualization&e=10_1_0,10_2_0&rt=tilesloaded.273,allpixels.273")
			.headers(headers_1),
            http("request_138")
			.get("/maps/api/js/ViewportInfoService.GetViewportInfo?1m6&1m2&1d6.026164124552795&2d-144.62793898926708&2m2&1d90&2d138.60177486471434&2u3&4sen&5e4&6sr%40372000000&7b0&8e0&callback=_xdc_._cbnhmi&token=2754")
			.headers(headers_1),
            http("request_139")
			.get("/maps/gen_204?target=api&ev=api_viewport&cad=host:alpha.bubbl.tech,v:27,r:1,mt:r,c:51.374902%2C-2.547855,sp:30.13082x93.16406,size:530x289,relsize:0.17,token:6u7c76kcre,src:apiv3,ts:a323ck")
			.headers(headers_1),
            http("request_140")
			.get("/maps/vt?pb=!1m4!1m3!1i3!2i2!3i2!1m4!1m3!1i3!2i2!3i3!1m4!1m3!1i3!2i3!3i2!1m4!1m3!1i3!2i3!3i3!1m4!1m3!1i3!2i4!3i2!1m4!1m3!1i3!2i4!3i3!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053934!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e3!12m1!5b1&callback=_xdc_._k4exgt&token=61717")
			.headers(headers_1),
            http("request_141")
			.get("/maps/vt?pb=!1m4!1m3!1i3!2i5!3i2!1m4!1m3!1i3!2i5!3i3!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053934!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e3!12m1!5b1&callback=_xdc_._crtrlp&token=79238")
			.headers(headers_1),
            http("request_142")
			.get("/maps/vt?pb=!1m5!1m4!1i3!2i3!3i2!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053634!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=55014")
			.headers(headers_1),
            http("request_143")
			.get("/maps/vt?pb=!1m5!1m4!1i3!2i3!3i3!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053718!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=14508")
			.headers(headers_1),
            http("request_144")
			.get("/maps/vt?pb=!1m5!1m4!1i3!2i4!3i2!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053874!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=116577")
			.headers(headers_1),
            http("request_145")
			.get("/maps/vt?pb=!1m5!1m4!1i3!2i4!3i3!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053874!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=114187")
			.headers(headers_1),
            http("request_146")
			.get("/maps/vt?pb=!1m5!1m4!1i3!2i5!3i2!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053874!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=66024")
			.headers(headers_1),
            http("request_147")
			.get("/maps/vt?pb=!1m5!1m4!1i3!2i5!3i3!4i256!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053874!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e0&token=63634")
			.headers(headers_1),
            http("request_148")
			.get("/maps/vt?pb=!1m4!1m3!1i3!2i3!3i2!1m4!1m3!1i3!2i3!3i3!1m4!1m3!1i3!2i4!3i2!1m4!1m3!1i3!2i4!3i3!1m4!1m3!1i3!2i5!3i2!1m4!1m3!1i3!2i5!3i3!2m3!1e4!2st!3i372!2m3!1e0!2sr!3i372053874!3m9!2sen!3sUS!5e18!12m1!1e63!12m3!1e37!2m1!1ssmartmaps!4e3!12m1!5b1&callback=_xdc_._2vmguu&token=85819")
			.headers(headers_1)))
		.pause(4)
		.exec(http("request_149")
			.get("/maps/api/js/QuotaService.RecordEvent?1shttp%3A%2F%2Falpha.bubbl.tech%2F%23%2Fuser%2Flogin&5e0&6u1&7sa3271u&callback=_xdc_._ejzeqg&token=12595")
			.headers(headers_1)
			.resources(http("request_150")
			.get(uri13 + "/js/pages/event/single/event.html")
			.headers(headers_100),
            http("request_151")
			.get(uri06 + "/venue")
			.headers(headers_102),
            http("request_152")
			.options(uri06 + "/view/event/692")
			.headers(headers_152),
            http("request_153")
			.options(uri06 + "/event/692")
			.headers(headers_152),
            http("request_154")
			.options(uri06 + "/event/692/surveys")
			.headers(headers_152),
            http("request_155")
			.options(uri06 + "/event/692/payloads")
			.headers(headers_152),
            http("request_156")
			.get(uri13 + "/")
			.headers(headers_156),
            http("request_157")
			.get(uri02 + "/collect?v=1&_v=j47&a=803339347&t=pageview&_s=3&dl=http%3A%2F%2Falpha.bubbl.tech%2F&dp=%2Fevents%2F692&ul=en-us&de=UTF-8&dt=Bubbl&sd=24-bit&sr=1920x1080&vp=1919x466&je=0&fl=24.0%20r0&_utma=115324308.1369704918.1481647806.1484754235.1484756403.8&_utmz=115324308.1481647806.1.1.utmcsr%3D(direct)%7Cutmccn%3D(direct)%7Cutmcmd%3D(none)&_utmht=1485175356503&_u=CCECCMABJ~&jid=&cid=1369704918.1481647806&tid=UA-66420404-2&z=198617564")
			.headers(headers_1),
            http("request_158")
			.get(uri06 + "/view/event/692")
			.headers(headers_102),
            http("request_159")
			.get(uri06 + "/event/692/payloads")
			.headers(headers_102),
            http("request_160")
			.get(uri06 + "/event/692/surveys")
			.headers(headers_102),
            http("request_161")
			.get(uri06 + "/event/692")
			.headers(headers_102),
            http("request_162")
			.get("http://" + uri01 + "/maps/api/staticmap?centre=%5Bobject%20Object%5D&zoom=17&key=AIzaSyCr3ZtMml1JIFb1x4Z5mYLlfriotaumJ84&maptype=terrain&size=450x400&sensor=false&format=png&path=color%3A0x3f51b5ff%7Cweight%3A1%7Cfillcolor%3A0x00BFA510%7C40.156310883032674%2C44.49884533882141%7C40.15612228460059%2C44.49871122837067%7C40.156171484242094%2C44.498611986637115%7C40.156046435083475%2C44.498518109321594%7C40.15606488497382%2C44.49847251176834%7C40.15601363526606%2C44.49844300746918%7C40.156021835221914%2C44.49842154979706%7C40.15598288542285%2C44.498392045497894%7C40.15599313537212%2C44.49836790561676%7C40.155892685802385%2C44.49828743934631%7C40.15592753566993%2C44.49819624423981%7C40.15548678602769%2C44.497871696949005%7C40.154713929748986%2C44.499604403972626%7C40.155593386203435%2C44.50027495622635%7C40.155607736214314%2C44.50035274028778%7C40.15569998621186%2C44.50031518936157%7C40.155681536222374%2C44.500250816345215%7C40.15596853549125%2C44.49959635734558%7C40.155989035392594%2C44.499625861644745%7C40.15604233510718%2C44.499491751194%7C40.15601773524411%2C44.499494433403015%7C40.156310883032674%2C44.49884533882141")
			.headers(headers_1),
            http("request_163")
			.get(uri13 + "/js/components/directives/confirmdialog/confirmdialog.html")
			.headers(headers_101),
            http("request_164")
			.get(uri06 + "/event")
			.headers(headers_102),
            http("request_165")
			.get(uri06 + "/chart/platforms")
			.headers(headers_102),
            http("request_166")
			.get(uri06 + "/event")
			.headers(headers_102),
            http("request_167")
			.get(uri06 + "/chart/platforms")
			.headers(headers_102)))

	setUp(scn.inject(atOnceUsers(100))).protocols(httpProtocol)
}
