/*!
 * js-json.js v3.2
 * (c) 2015-2018 Mingyuan Chen
 * Released under the MIT License.
 */

(function() {

var toString = Object.prototype.toString,
	hasOwnProperty = Object.hasOwnProperty;
	
// 实现JSON
if ( ! window.JSON ) {
window.JSON = {
	stringify: (function() {
		var cx = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
		    es = /[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
		    meta = { '\b': '\\b', '\t': '\\t', '\n': '\\n', '\f': '\\f', '\r': '\\r', '"' : '\\"', '\\': '\\\\' },
			gap, idn, rep;
		function quote( s ) {
		    es.lastIndex = 0;
		    return es.test( s ) ? '"' + s.replace( es, function( a ) {
		            var c = meta[a];
		            return typeof c === 'string' ? c : '\\u' + ('0000' + a.charCodeAt( 0 ).toString( 16 )).slice( -4 );
		        }) + '"' : '"' + s + '"';
		}
		function str( key, h ) {
		    var i, k, v, length, mind = gap, _partial, u = h[ key ];
		    if ( typeof rep === 'function' )
		        u = rep.call(h, key, u);
		    switch ( typeof u ) {
		    case 'string':
		        return quote( u );
		    case 'number':
		        return isFinite( u ) ? u + '' : 'null';
		    case 'boolean':
		    case 'null':
		        return u + '';
		    case 'object':
		        if ( ! u )
		            return 'null';
		        gap += idn;
		        _partial = [];
		       if ( toString.call( u ) === '[object Array]' ) {
		            length = u.length;
		            for ( i = 0; i < length; i ++ ) {
		                _partial[ i ] = str( i, u ) || 'null';
		            }
		            v = _partial.length === 0 ? '[]' : gap ? '[\n' + gap + _partial.join(',\n' + gap) + '\n' + mind + ']' : '[' + _partial.join(',') + ']';
		            gap = mind;
		            return v;
		        }
		        if ( rep && typeof rep === 'object' ) {
		            length = rep.length;
		            for ( i = 0; i < length; i ++ ) {
		                k = rep[ i ];
		                if ( typeof k === 'string' && (v = str(k, u)) ) {
		                	_partial.push(quote( k ) + (gap ? ': ' : ':') + v);
		                }
		            }
		        } else {
		            for ( k in u ) {
		                if ( hasOwnProperty.call( u, k ) ) {
		                    if ( v = str( k, u ) )
		                        _partial.push( quote( k ) + ( gap ? ': ' : ':' ) + v );
		                }
		            }
		        }
		        v = _partial.length === 0 ? '{}' : gap ? '{\n' + gap + _partial.join( ',\n' + gap ) + '\n' + mind + '}' : '{' + _partial.join( ',' ) + '}';
		        gap = mind;
		        return v;
		    }
		}
		return function( u, r, s ) {
		    var i;
		    gap = '', idn = '';
		    if ( typeof s === 'number' ) {
		        for ( i = 0; i < s; i ++ )
		            idn += ' ';
		    } else if ( typeof s === 'string' ) {
		        idn = s;
		    }
		    rep = r;
		    if ( r && typeof r !== 'function' && (typeof r !== 'object' || typeof r.length !== 'number' ) ) {
		        throw new Error( 'JSON.stringify' );
		    }
		    return str( '', { '' : u } );
		};
	})(),
	parse: function( a ) {
		return a == null ? null : (Function('return ' + a))();
	}
}}
	
})();
