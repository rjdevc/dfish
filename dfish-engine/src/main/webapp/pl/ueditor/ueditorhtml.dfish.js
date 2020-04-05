/* UEditorHtml
 * 展示UEditor编辑的内容
 */
require( './third-party/SyntaxHighlighter/shCoreDefault.css' );

var SyntaxHighlighter = require( './third-party/SyntaxHighlighter/shCore.js' ).SyntaxHighlighter;

define.widget( 'UEditorHtml', {
	Extend: 'Html',
	Default: { thumbWidth: '*' },
	Listener: {
		body: {
			ready: function() {
				SyntaxHighlighter.highlight();
			}
		}
	}
} );

