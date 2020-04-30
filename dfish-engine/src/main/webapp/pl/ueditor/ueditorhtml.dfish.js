/* UEditorHtml
 * 展示UEditor编辑的内容
 */
require( './third-party/SyntaxHighlighter/shCoreDefault.css' );

var SyntaxHighlighter = require( './third-party/SyntaxHighlighter/shCore.js' ).SyntaxHighlighter;

var Html = require( 'Html' );

define.widget( 'UEditorHtml', {
	Const: function( x ) {
		if ( x.text )
			x.text = x.text.replace( /(?:<p>(<br\/>|&nbsp;)<\/p>)+$/, '' );
		Html.apply( this, arguments );
	},
	Extend: Html,
	Default: { thumbWidth: '*' },
	Listener: {
		body: {
			ready: function() {
				SyntaxHighlighter.highlight();
			}
		}
	}
} );

