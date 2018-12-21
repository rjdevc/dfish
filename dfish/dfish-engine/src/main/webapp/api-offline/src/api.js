/*
 * api.js
 */

var mod = dfish.x.data.module;

dfish.init( {
	view: {
		id: 'index',
		src: 'api/' + mod + '-view.json'
	},
	debug: true
} );

require.css( './api.css' );

var data = require( '../' + mod + '-data' ),
	path = module.path,
	pk = 'Config,Event,Properties,Methods,Classes'.split( ',' ),
	pn = '配置,事件,属性,方法,样式'.split( ',' );

//helper
function vm() {
	return VM( '/index' );
}
function find( a ) {
	return vm().find( a );
}

function apiContentWidget( id ) {
	var a = data[ id ], b = [ { type: 'html', style: 'font-size:14px;padding-bottom:5px;', text: '<p>' + a.remark + '</p>' } ], s = [], t, i = 0;
	for ( ; i < pk.length; i ++ )
		(t = memberWidget( id, a, pk[ i ], pn[ i ], ! t )) && (b = b.concat( t ), s.push( { type: 'button', text: pn[ i ], id: id + '-btn-' + pk[ i ], data: { mod: id, chap: pk[ i ] }, focus: i === 0, on: { click: 'api.anchor(this)' } } ));
	return { type: 'vert', id: id + '-con', nodes: [
		{ type: 'horz', height: 55, cls: 'main-header bd-nav bd-onlybottom', nodes: [
		  { type: 'html', text: '<h1>' + a.title + '</h1>' },
		  { type: 'buttonbar', pub: { cls: 'hd-tab', focusable: true }, space: 1, nodes: s }
		] },
		{ type: 'vert', id: id + '-scr', scroll: true, style: 'padding:0 15px', height: '*', nodes: b, on: { scroll: 'api.onscroll(this)' } }
	] };
}
//排除继承数据中重复的部分
function uniq( a, b ) {
	if ( a && b ) {
		var i = b.length, j;
		while ( i -- ) {
			j = a.length;
			while ( j -- ) {
				if ( b[ i ].name === a[ j ].name || b[ i ].name.indexOf( a[ j ].name + '(' ) === 0 ) { b.splice( i, 1 ); break; }
			}
		}
		b = a.concat( b );
	}
	return b || a;
}
// 获取继承的数据
function getExtend( a, k ) {
	var b = a[ k ],
		d = data[ a.extend ],
		e = a.extend && d[ k ] && d[ k ].concat(),
		t = a.sort !== false,
		s = function( m, n ) { return m.name < n.name ? -1 : 1 };
	b && t && b.sort( s );
	b = uniq( b, e );
	a.extend != 'widget' && b && t && b.sort( s );
	if ( d && d.extend ) {
		b = uniq( b, getExtend( d, k ) );
	}
	if ( b ) {
		var f = $.arrSelect( b, 'v.common' ), g = $.arrSelect( b, '!v.common' );
		t && (f.sort( s ), g.sort( s ));
		b = g.concat( f );
	}
	return b && b.concat();
}
function memberWidget( id, a, k, n, m ) {
	b = getExtend( a, k );
	if ( b ) {
		if ( a.deprecate ) { // 排除不用的方法
			var c = a.deprecate.split( ',' ), i = c.length, j;
			while ( i -- ) {
				j = b.length;
				while ( j -- ) {
					if ( c[ i ] === b[ j ].name || b[ j ].name.indexOf( c[ i ] + '(' ) === 0 ) { b.splice( j, 1 ); break; }
				}
			}
		}
		return [ { type: 'html', text: '<div id=' + id + '-' + k + ' class="members-title" onclick="api.expand(this)"><h3>' + $.arrow( 'b2' ) + ' &nbsp;' + n + '</h3>' + (m ? '<div style="float:right;font-size:12px;color:#888;line-height:40px"><font color=#ff7921>*</font> 注: 蓝色字体是当前widget专有的属性事件方法，黑色字体是所有widget共有的属性事件方法。用法无区别，只是为了方便查阅而加以颜色区分。</div>' : '') + '</div>' },
			{ type: 'grid', id: id + '-' + k + '-grid', face: 'cell', cls: 'detail-grid', "columns":[ {"field":"name", "width":"*", "format":"javascript:return api.format(this)"} ], tbody: { rows: b } } ];
	}
}

function format( row ) {
	var d = row.x.data, e,
		s = '<h4 id="' + ( d.id || $.strTo( d.name, '(' ) || d.name ) + '"><span class="name' + (d.common ? '' : ' ownprop') + '">' + d.name + '</span>' + (d.ver ? '<sup>' + d.ver + '</sup>&nbsp;' : '') + (d.type ? ' <span class=typeof>: ' + d.type + '</span>' : '') + (d.mobile ? ' <i class="f-i icon icon-mobile" title="移动端专用"></i> ': '') + '</h4><p class=remark>' + d.remark + '</p>';
	if ( d.param ) {
		s += formatParam( d.param, d.common );
	}
	if ( e = d.example )
		s += exampleContent( e );
	return { type: 'html', text: s, cls: 'title' };
}

function formatParam( p, c ) {
	var s = '<ul>';
	for ( var i = 0; i < p.length; i ++ ){
		s += '<li class="param' + (c ? ' z-common' : '') + '"><div><var>' + p[ i ].name + '</var> ' + (p[ i ].ver ? '<sup>' + p[ i ].ver + '</sup>' : '') + ': <span class=typeof>' + p[ i ].type + '</span>' + (p[ i ].optional ? ' &nbsp;<i>[Optional]</i>' : '') + (p[ i ].mobile ? ' <i class="f-i icon icon-mobile" title="移动端专用"></i>' : '') + '</div><div class=remark>' + p[ i ].remark  + '</div>';
		if ( p[ i ].param )
			s += formatParam( p[ i ].param, c );
	}
	return s + '</ul>';
}

function exampleContent( e ) {
	for ( var j = 0, f, h, s = '', l = e.length; j < l; j ++ ) {
		f = e[ j ].toString().slice( 14, -1 ).replace( /\s+$/, '' ).split( '\r\n' );
		if ( f[ 0 ].indexOf( '//' ) > -1 ) {
			s += '<div class="example-title">// ' + (f[ 0 ].indexOf( '///' ) > -1 ? '' : ('范例' + (l > 1 ? (j + 1) : '')) + ': ') + $.strTrim( f[ 0 ] ).replace( /^\/+/g, '' ) + '</div>';
			f.splice( 0, 1 );
		}
		h = f.join( '\n' ).replace( /</g, '&lt;' ).replace( />/g, '&gt;' )
			.replace( /\/\/.+/g, function( $0 ){ return '<em class=code-ann>' + $0 + '</em>' } )
			.replace( /return~/g, '' );
		if ( h.indexOf( "return''" ) > -1 )
			h = h.replace( /return''/g, '' ).replace( /(?=\s*)'|'(?=\n)/g, '' );
		h = h.replace( /".+?"/g, function( $0 ) { return '<em class=code-str>' + $0 + '</em>' } )
			.replace( /'.+?'/g, function( $0 ) { return '<em class=code-str>' + $0 + '</em>' } )
			.replace(/\b(?:true|false)\b/g, function( $0 ) { return '<em class=code-lit>' + $0 + '</em>' } )
			.replace(/\b(?:function)\b/g, function( $0 ) { return '<em class=code-fun>' + $0 + '</em>' } )
			.replace(/\b(?:this|var|new|return|if|for)\b/g, function( $0 ) { return '<em class=code-key>' + $0 + '</em>' } );
		s += '<pre class=example-content><code>\t    ' + $.strTrim( h ) + '</code></pre>';
	}
	return s;
}

var api = {
	vm: vm,
	find: find,
	init: function() {
		find( 'guide' ).append( { type: 'html', text: '<a href="javascript:" onclick="$.widget(this).text(123)">click</a>' } );
	},
	tab: function( btn, con ) {
		var b = vm().find( 'index-tabbar' ).append( btn ),
			c = vm().find( 'index-frame' ).append( con );
		b.focus();
	},
	tabClick: function( btn ) {
		find( btn.x.id.replace( '-btn', '' ) ).focus();
	},
	treeClick: function( tree ) {
		if ( typeof tree === 'string' )
			tree = vm().find( tree );
		var d = tree.x.datasrc || tree.x.id;
		if ( d ) {
			var b = vm().find( d + '-btn' ), t = tree.x.datasrc;
			if ( b ) {
				b.focus();
			} else {
				this.tab( { type: 'button', id: d + '-btn', text: ( t ? find( t ) : tree ).x.text, target: d + '-con' }, apiContentWidget( d ) );
			}
			if ( t ) {
				setTimeout( function() {
					find( d + '-scr' ).scrollTop( $( tree.x.id ), 'top', true );
				}, 100 );
			}
		}
	},
	open:function( a ) {
		api.treeClick( a );
		api.find( a ).focus();
		api.find( a ).scrollIntoView();
	},
	expand: function( a, b ) {
		var t = $.get( '.f-arw-b2', a );
		b == null && (b = ! t);
		$.classAdd( $.w( a ).next().$(), 'f-none', ! b );
		$.arrow( t || $.get( '.f-arw-t2', a ), b ? 'b2' : 't2' );
	},
	anchor: function( a ) {
		var d = a.x.data;
		find( d.mod + '-scr' ).scrollTo( $( d.mod + '-' + d.chap ), 'top', null, 'normal', null, function() { find(d.mod + '-btn-' + d.chap).focus() } );
		api.expand( $( d.mod + '-' + d.chap ), true );
	},
	onscroll: function( a ) {
		for ( var i = 0, b = a.x.id.replace( '-scr', '' ), c = $.bcr( a.$() ), l = pk.length; i < l; i ++ ) {
			var d = $( b + '-' + pk[ i ] );
			if ( d && (d = $.bcr( d )) && (d.top > c.top || i === l - 1) ) {
				find( b + '-btn-' + pk[ d.top > (c.top + c.bottom / 2) ? i - 1 : i ] ).focus();
				break;
			}
		}
	},
	trCmd: function( target, cmdArgs ) {
		var o = $.widget( target ).closest( 'tr' ),
			a = cmdArgs.split(','), b = [];
		for ( var i = 1; i < a.length; i ++ ) {
			b.push( o.x.data[ a[ i ] ] );
		}
		o.exec( a[ 0 ], b );			
	},
	format: format
	
};

window.api = api;
//$.ajaxCache( 'webapp/demo.json.php?act=combo' );

