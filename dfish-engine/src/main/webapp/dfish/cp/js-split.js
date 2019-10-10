/*!
 * js.js v3.2
 * (c) 2015-2018 Mingyuan Chen
 * Released under the MIT License.
 */

(function(undefined) {

var toString = Object.prototype.toString,
	hasOwnProperty = Object.hasOwnProperty,
	push = Array.prototype.push;

// 修复ie8的split方法兼容性问题
if ( 'a'.split( /a/ ).length === 0 ) {
	var nativeSplit = String.prototype.split,
	    compliantExecNpcg = /()??/.exec("")[1] === undefined, 
	    self;
	self = function (str, separator, limit) {
	    if (toString.call(separator) !== "[object RegExp]") {
	        return nativeSplit.call(str, separator, limit);
	    }
	    var output = [],
	        flags = (separator.ignoreCase ? "i" : "") +
	                (separator.multiline ? "m" : "") +
	                (separator.extended ? "x" : "") + // Proposed for ES6
	                (separator.sticky ? "y" : ""), // Firefox 3+
	        lastLastIndex = 0,
	        separator = new RegExp(separator.source, flags + "g"),
	        separator2, match, lastIndex, lastLength;
	    str += ""; 
	    if (!compliantExecNpcg) {
	        separator2 = new RegExp("^" + separator.source + "$(?!\\s)", flags);
	    }
	    limit = limit === undefined ?
	        -1 >>> 0 : // Math.pow(2, 32) - 1
	        limit >>> 0; // ToUint32(limit)
	    while (match = separator.exec(str)) {
	        lastIndex = match.index + match[0].length;
	        if (lastIndex > lastLastIndex) {
	            output.push(str.slice(lastLastIndex, match.index));
	            if (!compliantExecNpcg && match.length > 1) {
	                match[0].replace(separator2, function () {
	                    for (var i = 1; i < arguments.length - 2; i++) {
	                        if (arguments[i] === undefined) {
	                            match[i] = undefined;
	                        }
	                    }
	                });
	            }
	            if (match.length > 1 && match.index < str.length) {
	                push.apply(output, match.slice(1));
	            }
	            lastLength = match[0].length;
	            lastLastIndex = lastIndex;
	            if (output.length >= limit) {
	                break;
	            }
	        }
	        if (separator.lastIndex === match.index) {
	            separator.lastIndex++;
	        }
	    }
	    if (lastLastIndex === str.length) {
	        if (lastLength || !separator.test("")) {
	            output.push("");
	        }
	    } else {
	        output.push(str.slice(lastLastIndex));
	    }
	    return output.length > limit ? output.slice(0, limit) : output;
	};
	String.prototype.split = function (separator, limit) {
	    return self(this, separator, limit);
	};
}


})();
