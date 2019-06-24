/* carousel
 * 幻灯片

{ "type": "carousel", "id": "f_carousel0", "width": "470", height: 540, "thumbwidth": 80, "thumbheight": 60, "bigwidth": 470, "bigheight": 470,
  "value": [
    { "thumbnail": "http://tp1.sinaimg.cn/2483245844/180/5614510120/0",  "url": "http://tp1.sinaimg.cn/2483245844/180/5614510120/0",  "text": "CBA两老板入财富榜500强 新疆冠军巨鳄令姚明望尘莫及", "href": "javascript:alert(this.id)" },
    { "thumbnail": "http://tp2.sinaimg.cn/2214257545/180/5634974717/0",  "url": "http://tp2.sinaimg.cn/2214257545/180/5634974717/0",  "text": "有钱任性！新疆队老板送30辆进口豪车 西热兴奋晒图", "href": "#1" },
    { "thumbnail": "http://tp3.sinaimg.cn/2622821682/180/5662155841/1",  "url": "http://tp3.sinaimg.cn/2622821682/180/5662155841/1",  "text": "男篮双国家队集训名单：小丁与阿联各领衔红蓝队",    "href": "#2" },
    { "thumbnail": "http://tp4.sinaimg.cn/2495996991/180/5638239663/1",  "url": "http://tp4.sinaimg.cn/2495996991/180/5638239663/1",  "text": "上港被绝杀博阿斯最开心 疯狂想碰苏宁终得偿所愿",   "href": "#3" },
    { "thumbnail": "http://tp3.sinaimg.cn/1966380590/180/40021479088/0", "url": "http://tp3.sinaimg.cn/1966380590/180/40021479088/0", "text": "亚冠16强对阵：淘汰赛首迎中国德比 中日霸主大对决", "href": "#4" }
  ]
},
{ "type": "carousel", "id": "f_carousel1", "width": "470", height: 470, "bigwidth": 470, "bigheight": 470, "cls": "carousel-1",
  "value": [
    { "thumbnail": ".f-dot", "url": "http://tp1.sinaimg.cn/2483245844/180/5614510120/0" },
    { "thumbnail": ".f-dot", "url": "http://wx2.sinaimg.cn/large/679ca86egy1ffh9c5bw35j20sg0n7dkt" },
    { "thumbnail": ".f-dot", "url": "http://tp3.sinaimg.cn/1966380590/180/40021479088/0" }
  ]
},
{ "type": "carousel", "id": "f_carousel2", "width": "470", height: 470, "bigwidth": 470, "bigheight": 470, "cls": "carousel-2",
  "value": [
    { "thumbnail": ".f-dot", "url": "http://tp1.sinaimg.cn/2483245844/180/5614510120/0",  "href": "javascript:alert(this.id)" },
    { "thumbnail": ".f-dot", "url": "http://wx2.sinaimg.cn/large/679ca86egy1ffh9c5bw35j20sg0n7dkt",  "href": "#1" },
    { "thumbnail": ".f-dot", "url": "http://tp3.sinaimg.cn/1966380590/180/40021479088/0", "href": "#4" }
  ]
},
{ "type": "carousel", "id": "f_carousel3", "width": "470", height: 470, "bigwidth": 470, "bigheight": 470, "cls": "carousel-3",
  "value": [
    { "thumbnail": ".f-dot", "url": "http://tp1.sinaimg.cn/2483245844/180/5614510120/0", "text": "CBA两老板入财富榜500强 新疆冠军巨鳄令姚明望尘莫及", "href": "javascript:alert(this.id)" },
    { "thumbnail": ".f-dot", "url": "http://wx2.sinaimg.cn/large/679ca86egy1ffh9c5bw35j20sg0n7dkt", "text": "有钱任性！新疆队老板送30辆进口豪车 西热兴奋晒图", "href": "#1" },
    { "thumbnail": ".f-dot", "url": "http://tp3.sinaimg.cn/1966380590/180/40021479088/0", "text": "男篮双国家队集训名单：小丁与阿联各领衔红蓝队", "href": "#4" }
  ]
}

*/

require.css( './carousel.css' );

var $ = require( 'dfish' ), W = require( 'widget' );

define.widget( 'carousel', {
	Const: function( x, p ) {
		W.apply( this, arguments );
		if ( typeof x.value === 'string' )
			x.value = $.jsonParse( x.value );
		for ( var i = 0, t = [], f = [], g = $.abbr + '.all["' + this.id + '"]', s, v = x.value, l = v && v.length; i < l; i ++ ) {
			t.push( { icon: v[ i ].thumbnail || v[ i ].url, iconwidth: x.thumbwidth, iconheight: x.thumbheight, width: x.thumbwidth, height: x.thumbheight, target: this.id + 'i' + i, focus: i === 0, on: { mouseover: g + '.pause(' + i + ')' } } );
			s = '<img src=' + (v[ i ].url || v[ i ].thumbnail) + ' width=' + x.bigwidth + ' height=' + x.bigheight + '>';
			if ( v[ i ].text )
				s += '<b class=_b></b><span class=_t>' + v[ i ].text + '</span>';
			if ( v[ i ].href )
				s = '<a ' + (v[ i ].href.indexOf( 'javascript:' ) === 0 ? ' onclick=' + g + '.click(' + i + ')' : 'href=' + v[ i ].href + ' target=_blank') + '>' + s + '</a>';
			f.push( { type: 'html', cls: 'w-carousel-big', id: this.id + 'i' + i, width: '*', height: '*', text: s } );
		}
		this.tab = this.add( { type: 'buttonbar', cls: 'w-carousel-bbr', width: '*', height: '*', pub: { name: this.id + 'name', cls: 'w-carousel-btn', focusable: true }, nodes: t, on: { mouseout: g + '.play()' } } );
		this.fra = this.add( { type: 'frame', width: '*', height: x.bigheight, dft: this.id + 'i0', nodes: f, on: { mouseover: g + '.pause()', mouseout: g + '.play()' } } );
	},
	Extend: 'vert',
	Listener: {
		body: {
			ready: function() { this.play(); }
		}
	},
	Prototype: {
		className: 'w-carousel',
		index: 0,
		play: function() {
			var self = this;
			clearTimeout( this.timer );
			this.timer = setTimeout( function() {
				(self.tab[ self.index ++ ].next() || self.tab[ self.index = 0 ]).focus();
				self.play();
			}, 3000 );
		},
		pause: function( a ) {
			clearTimeout( this.timer );
			a != null && this.tab[ this.index = a ].focus();
		},
		click: function( a ) {
			$.fncall( this.x.value[ a ].href, this );
		},
		html_nodes: function() {
			return this.fra.html() + this.tab.html();
		}
	}
} );
