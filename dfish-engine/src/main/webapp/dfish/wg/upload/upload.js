/**
 * SWFUpload: http://www.swfupload.org, http://swfupload.googlecode.com
 *
 * mmSWFUpload 1.0: Flash upload dialog - http://profandesign.se/swfupload/,  http://www.vinterwebb.se/
 *
 * SWFUpload is (c) 2006-2007 Lars Huring, Olov Nilz閚 and Mammon Media and is released under the MIT License:
 * http://www.opensource.org/licenses/mit-license.php
 *
 * SWFUpload 2 is (c) 2007-2008 Jake Roberts and is released under the MIT License:
 * http://www.opensource.org/licenses/mit-license.php
 *
 * SWFObject v2.2 <http://code.google.com/p/swfobject/> 
 *	is released under the MIT License <http://www.opensource.org/licenses/mit-license.php>
 */



/* ******************* */
/* Constructor & Init  */
/* ******************* */

var SWFUpload = function(settings) {
	this.initSWFUpload(settings);
};

SWFUpload.prototype.initSWFUpload = function (userSettings) {
	try {
		this.customSettings = {};	// A container where developers can place their own settings associated with this instance.
		this.settings = {};
		this.eventQueue = [];
		this.movieName = "SWFUpload_" + SWFUpload.movieCount++;
		this.movieElement = null;


		// Setup global control tracking
		SWFUpload.instances[this.movieName] = this;

		// Load the settings.  Load the Flash movie.
		this.initSettings(userSettings);
		this.loadSupport();
		if (this.swfuploadPreload()) {
			this.loadFlash();
		}

		this.displayDebugInfo();
	} catch (ex) {
		delete SWFUpload.instances[this.movieName];
		throw ex;
	}
};

/* *************** */
/* Static Members  */
/* *************** */
SWFUpload.instances = {};
SWFUpload.movieCount = 0;
SWFUpload.version = "2.5.0 2010-01-15 Beta 2";
SWFUpload.QUEUE_ERROR = {
	QUEUE_LIMIT_EXCEEDED            : -100,
	FILE_EXCEEDS_SIZE_LIMIT         : -110,
	FILE_MIN_SIZE_LIMIT         	: -111,
	ZERO_BYTE_FILE                  : -120,
	INVALID_FILETYPE                : -130
};
SWFUpload.UPLOAD_ERROR = {
	HTTP_ERROR                      : -200,
	MISSING_UPLOAD_URL              : -210,
	IO_ERROR                        : -220,
	SECURITY_ERROR                  : -230,
	UPLOAD_LIMIT_EXCEEDED           : -240,
	UPLOAD_FAILED                   : -250,
	SPECIFIED_FILE_ID_NOT_FOUND     : -260,
	FILE_VALIDATION_FAILED          : -270,
	FILE_CANCELLED                  : -280,
	UPLOAD_STOPPED                  : -290,
	RESIZE                          : -300
};
SWFUpload.FILE_STATUS = {
	QUEUED       : -1,
	IN_PROGRESS  : -2,
	ERROR        : -3,
	COMPLETE     : -4,
	CANCELLED    : -5
};
SWFUpload.UPLOAD_TYPE = {
	NORMAL       : -1,
	RESIZED      : -2
};

SWFUpload.BUTTON_ACTION = {
	SELECT_FILE             : -100,
	SELECT_FILES            : -110,
	START_UPLOAD            : -120,
	JAVASCRIPT              : -130,	// DEPRECATED
	NONE                    : -130
};
SWFUpload.CURSOR = {
	ARROW : -1,
	HAND  : -2
};
SWFUpload.WINDOW_MODE = {
	WINDOW       : "window",
	TRANSPARENT  : "transparent",
	OPAQUE       : "opaque"
};

SWFUpload.RESIZE_ENCODING = {
	JPEG  : -1,
	PNG   : -2
};

// Private: takes a URL, determines if it is relative and converts to an absolute URL
// using the current site. Only processes the URL if it can, otherwise returns the URL untouched
SWFUpload.completeURL = function (url) {
	try {
		var path = "", indexSlash = -1;
		if (typeof(url) !== "string" || url.match(/^https?:\/\//i) || url.match(/^\//) || url === "") {
			return url;
		}
		
		indexSlash = window.location.pathname.lastIndexOf("/");
		if (indexSlash <= 0) {
			path = "/";
		} else {
			path = window.location.pathname.substr(0, indexSlash) + "/";
		}
		
		return path + url;
	} catch (ex) {
		return url;
	}
};

// Public: assign a new function to onload to use swfobject's domLoad functionality
SWFUpload.onload = function () {};


/* ******************** */
/* Instance Members  */
/* ******************** */

// Private: initSettings ensures that all the
// settings are set, getting a default value if one was not assigned.
SWFUpload.prototype.initSettings = function (userSettings) {
	this.ensureDefault = function (settingName, defaultValue) {
		var setting = userSettings[settingName];
		if (setting != undefined) {
			this.settings[settingName] = setting;
		} else {
			this.settings[settingName] = defaultValue;
		}
	};
	
	// Upload backend settings
	this.ensureDefault("upload_url", "");
	this.ensureDefault("preserve_relative_urls", false);
	this.ensureDefault("file_post_name", "Filedata");
	this.ensureDefault("post_params", {});
	this.ensureDefault("use_query_string", false);
	this.ensureDefault("requeue_on_error", false);
	this.ensureDefault("http_success", []);
	this.ensureDefault("assume_success_timeout", 0);
	
	// File Settings
	this.ensureDefault("file_types", "*.*");
	this.ensureDefault("file_types_description", "All Files");
	this.ensureDefault("file_size_limit", 0);	// Default zero means "unlimited"
	this.ensureDefault("file_upload_limit", 0);
	this.ensureDefault("file_queue_limit", 0);

	// Flash Settings
	this.ensureDefault("flash_url", "swfupload.swf");
	this.ensureDefault("flash9_url", "swfupload_fp9.swf");
	this.ensureDefault("prevent_swf_caching", true);
	
	// Button Settings
	this.ensureDefault("button_image_url", "");
	this.ensureDefault("button_width", 1);
	this.ensureDefault("button_height", 1);
	this.ensureDefault("button_text", "");
	this.ensureDefault("button_text_style", "color: #000000; font-size: 16pt;");
	this.ensureDefault("button_text_top_padding", 0);
	this.ensureDefault("button_text_left_padding", 0);
	this.ensureDefault("button_action", SWFUpload.BUTTON_ACTION.SELECT_FILES);
	this.ensureDefault("button_disabled", false);
	this.ensureDefault("button_placeholder_id", "");
	this.ensureDefault("button_placeholder", null);
	this.ensureDefault("button_cursor", SWFUpload.CURSOR.ARROW);
	this.ensureDefault("button_window_mode", SWFUpload.WINDOW_MODE.WINDOW);
	
	// Debug Settings
	this.ensureDefault("debug", false);
	this.settings.debug_enabled = this.settings.debug;	// Here to maintain v2 API
	
	// Event Handlers
	this.settings.return_upload_start_handler = this.returnUploadStart;
	this.ensureDefault("swfupload_preload_handler", null);
	this.ensureDefault("swfupload_load_failed_handler", null);
	this.ensureDefault("swfupload_loaded_handler", null);
	this.ensureDefault("file_dialog_start_handler", null);
	this.ensureDefault("file_queued_handler", null);
	this.ensureDefault("file_queue_error_handler", null);
	this.ensureDefault("file_dialog_complete_handler", null);
	
	this.ensureDefault("upload_resize_start_handler", null);
	this.ensureDefault("upload_start_handler", null);
	this.ensureDefault("upload_progress_handler", null);
	this.ensureDefault("upload_error_handler", null);
	this.ensureDefault("upload_success_handler", null);
	this.ensureDefault("upload_complete_handler", null);
	
	this.ensureDefault("mouse_click_handler", null);
	this.ensureDefault("mouse_out_handler", null);
	this.ensureDefault("mouse_over_handler", null);
	
	this.ensureDefault("debug_handler", this.debugMessage);

	this.ensureDefault("custom_settings", {});
	
//	this.ensureDefault("button_html", null);
//	this.ensureDefault("title_width", null);
//	this.ensureDefault("down_url", null);
//	this.ensureDefault("face", null);
	

	// Other settings
	this.customSettings = this.settings.custom_settings;
	
	// Update the flash url if needed
	if (!!this.settings.prevent_swf_caching) {
		this.settings.flash_url = this.settings.flash_url + (this.settings.flash_url.indexOf("?") < 0 ? "?" : "&") + "preventswfcaching=" + new Date().getTime();
		this.settings.flash9_url = this.settings.flash9_url + (this.settings.flash9_url.indexOf("?") < 0 ? "?" : "&") + "preventswfcaching=" + new Date().getTime();
	}
	
	if (!this.settings.preserve_relative_urls) {
		this.settings.upload_url = SWFUpload.completeURL(this.settings.upload_url);
		this.settings.button_image_url = SWFUpload.completeURL(this.settings.button_image_url);
	}
	
	delete this.ensureDefault;
};

// Initializes the supported functionality based the Flash Player version, state, and event that occur during initialization
SWFUpload.prototype.loadSupport = function () {
	this.support = {
		loading : swfobject.hasFlashPlayerVersion("9.0.28"),
		imageResize : swfobject.hasFlashPlayerVersion("10.0.0")
	};
	
};

// Private: loadFlash replaces the button_placeholder element with the flash movie.
SWFUpload.prototype.loadFlash = function () {
	var targetElement, tempParent, wrapperType, flashHTML, els;

	if (!this.support.loading) {
		this.queueEvent("swfupload_load_failed_handler", ["Flash Player doesn't support SWFUpload"]);
		return;
	}
	
	// Make sure an element with the ID we are going to use doesn't already exist
	if (document.getElementById(this.movieName) !== null) {
		this.support.loading = false;
		//this.queueEvent("swfupload_load_failed_handler", ["Element ID already in use"]);
		return;
	}

	// Get the element where we will be placing the flash movie
	targetElement = document.getElementById(this.settings.button_placeholder_id) || this.settings.button_placeholder;
	if (targetElement == undefined) {
		this.support.loading = false;
		this.queueEvent("swfupload_load_failed_handler", ["button place holder not found"]);
		return;
	}

	wrapperType = (targetElement.currentStyle && targetElement.currentStyle["display"] || window.getComputedStyle && document.defaultView.getComputedStyle(targetElement, null).getPropertyValue("display")) !== "block" ? "span" : "div";
	
	// Append the container and load the flash
	tempParent = document.createElement(wrapperType);

	flashHTML = this.getFlashHTML();

	try {
		tempParent.innerHTML = flashHTML;	// Using innerHTML is non-standard but the only sensible way to dynamically add Flash in IE (and maybe other browsers)
	} catch (ex) {
		this.support.loading = false;
		this.queueEvent("swfupload_load_failed_handler", ["Exception loading Flash HTML into placeholder"]);
		return;
	}

	// Try to get the movie element immediately
	els = tempParent.getElementsByTagName("object");
	if (!els || els.length > 1 || els.length === 0) {
		this.support.loading = false;
		this.queueEvent("swfupload_load_failed_handler", ["Unable to find movie after adding to DOM"]);
		return;
	} else if (els.length === 1) {
		this.movieElement = els[0];
	}
	
	targetElement.parentNode.replaceChild(tempParent.firstChild, targetElement);

	// Fix IE Flash/Form bug
	if (window[this.movieName] == undefined) {
		window[this.movieName] = this.getMovieElement();
	}
};
// fix: SWFupload在IE9中无法点击bug
SWFUpload.prototype.getFlashHTML = function (flashVersion) {
	// Flash Satay object syntax: http://www.alistapart.com/articles/flashsatay
    var obj = ['<object id="', this.movieName, '" type="application/x-shockwave-flash" data="', (this.support.imageResize ? this.settings.flash_url : this.settings.flash9_url), '" data-transparent="1" class="swfupload">'].join(""),
	params = [
		'<param name="wmode" value="', this.settings.button_window_mode , '" />',
		'<param name="movie" value="', (this.support.imageResize ? this.settings.flash_url : this.settings.flash9_url), '" />',
		'<param name="quality" value="high" />',
		'<param name="menu" value="false" />',
		'<param name="allowScriptAccess" value="always" />',
		'<param name="flashvars" value="', this.getFlashVars(), '" />'
	].join("");
	if (navigator.userAgent.search(/MSIE/) > -1){
		obj = ['<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" id="', this.movieName, '" data-transparent="1" class="swfupload">'].join("");
		params += '<param name="src" value="' + (this.support.imageResize ? this.settings.flash_url : this.settings.flash9_url) + '" />';
	}
	return [obj, params, '</object>'].join("");
};

// Private: getFlashVars builds the parameter string that will be passed
// to flash in the flashvars param.
SWFUpload.prototype.getFlashVars = function () {
	// Build a string from the post param object
	var httpSuccessString, paramString;
	
	paramString = this.buildParamString();
	httpSuccessString = this.settings.http_success.join(",");
	
	// Build the parameter string
	return ["movieName=", encodeURIComponent(this.movieName),
			"&amp;uploadURL=", encodeURIComponent(this.settings.upload_url),
			"&amp;useQueryString=", encodeURIComponent(this.settings.use_query_string),
			"&amp;requeueOnError=", encodeURIComponent(this.settings.requeue_on_error),
			"&amp;httpSuccess=", encodeURIComponent(httpSuccessString),
			"&amp;assumeSuccessTimeout=", encodeURIComponent(this.settings.assume_success_timeout),
			"&amp;params=", encodeURIComponent(paramString),
			"&amp;filePostName=", encodeURIComponent(this.settings.file_post_name),
			"&amp;fileTypes=", encodeURIComponent(this.settings.file_types),
			"&amp;fileTypesDescription=", encodeURIComponent(this.settings.file_types_description),
			"&amp;fileSizeLimit=", encodeURIComponent(this.settings.file_size_limit),
			"&amp;fileUploadLimit=", encodeURIComponent(this.settings.file_upload_limit),
			"&amp;fileQueueLimit=", encodeURIComponent(this.settings.file_queue_limit),
			"&amp;debugEnabled=", encodeURIComponent(this.settings.debug_enabled),
			"&amp;buttonImageURL=", encodeURIComponent(this.settings.button_image_url),
			"&amp;buttonWidth=", encodeURIComponent(this.settings.button_width),
			"&amp;buttonHeight=", encodeURIComponent(this.settings.button_height),
			"&amp;buttonText=", encodeURIComponent(this.settings.button_text),
			"&amp;buttonTextTopPadding=", encodeURIComponent(this.settings.button_text_top_padding),
			"&amp;buttonTextLeftPadding=", encodeURIComponent(this.settings.button_text_left_padding),
			"&amp;buttonTextStyle=", encodeURIComponent(this.settings.button_text_style),
			"&amp;buttonAction=", encodeURIComponent(this.settings.button_action),
			"&amp;buttonDisabled=", encodeURIComponent(this.settings.button_disabled),
			"&amp;buttonCursor=", encodeURIComponent(this.settings.button_cursor)
		].join("");
};

// Public: get retrieves the DOM reference to the Flash element added by SWFUpload
// The element is cached after the first lookup
SWFUpload.prototype.getMovieElement = function () {
	if (this.movieElement == undefined) {
		this.movieElement = document.getElementById(this.movieName);
	}

	/*if (this.movieElement === null) {
		throw "Could not find Flash element";
	}*/
	
	return this.movieElement;
};

// Private: buildParamString takes the name/value pairs in the post_params setting object
// and joins them up in to a string formatted "name=value&amp;name=value"
SWFUpload.prototype.buildParamString = function () {
	var name, postParams, paramStringPairs = [];
	
	postParams = this.settings.post_params; 

	if (typeof(postParams) === "object") {
		for (name in postParams) {
			if (postParams.hasOwnProperty(name)) {
				paramStringPairs.push(encodeURIComponent(name.toString()) + "=" + encodeURIComponent(postParams[name].toString()));
			}
		}
	}

	return paramStringPairs.join("&amp;");
};

// Public: Used to remove a SWFUpload instance from the page. This method strives to remove
// all references to the SWF, and other objects so memory is properly freed.
// Returns true if everything was destroyed. Returns a false if a failure occurs leaving SWFUpload in an inconsistant state.
// Credits: Major improvements provided by steffen
SWFUpload.prototype.destroy = function () {
	var movieElement;
	
	try {
		// Make sure Flash is done before we try to remove it
		this.cancelUpload(null, false);
		
		movieElement = this.cleanUp();

		// Remove the SWFUpload DOM nodes
		if (movieElement) {
			// Remove the Movie Element from the page
			try {
				movieElement.parentNode.removeChild(movieElement);
			} catch (ex) {}
		}

		// Remove IE form fix reference
		window[this.movieName] = null;

		// Destroy other references
		SWFUpload.instances[this.movieName] = null;
		delete SWFUpload.instances[this.movieName];

		this.movieElement = null;
		this.settings = null;
		this.customSettings = null;
		this.eventQueue = null;
		this.movieName = null;
		
		return true;
	} catch (ex2) {
		return false;
	}
};


// Public: displayDebugInfo prints out settings and configuration
// information about this SWFUpload instance.
// This function (and any references to it) can be deleted when placing
// SWFUpload in production.
SWFUpload.prototype.displayDebugInfo = function () {
	this.debug(
		[
			"---SWFUpload Instance Info---\n",
			"Version: ", SWFUpload.version, "\n",
			"Movie Name: ", this.movieName, "\n",
			"Settings:\n",
			"\t", "upload_url:               ", this.settings.upload_url, "\n",
			"\t", "flash_url:                ", this.settings.flash_url, "\n",
			"\t", "flash9_url:                ", this.settings.flash9_url, "\n",
			"\t", "use_query_string:         ", this.settings.use_query_string.toString(), "\n",
			"\t", "requeue_on_error:         ", this.settings.requeue_on_error.toString(), "\n",
			"\t", "http_success:             ", this.settings.http_success.join(", "), "\n",
			"\t", "assume_success_timeout:   ", this.settings.assume_success_timeout, "\n",
			"\t", "file_post_name:           ", this.settings.file_post_name, "\n",
			"\t", "post_params:              ", this.settings.post_params.toString(), "\n",
			"\t", "file_types:               ", this.settings.file_types, "\n",
			"\t", "file_types_description:   ", this.settings.file_types_description, "\n",
			"\t", "file_size_limit:          ", this.settings.file_size_limit, "\n",
			"\t", "file_upload_limit:        ", this.settings.file_upload_limit, "\n",
			"\t", "file_queue_limit:         ", this.settings.file_queue_limit, "\n",
			"\t", "debug:                    ", this.settings.debug.toString(), "\n",

			"\t", "prevent_swf_caching:      ", this.settings.prevent_swf_caching.toString(), "\n",

			"\t", "button_placeholder_id:    ", this.settings.button_placeholder_id.toString(), "\n",
			"\t", "button_placeholder:       ", (this.settings.button_placeholder ? "Set" : "Not Set"), "\n",
			"\t", "button_image_url:         ", this.settings.button_image_url.toString(), "\n",
			"\t", "button_width:             ", this.settings.button_width.toString(), "\n",
			"\t", "button_height:            ", this.settings.button_height.toString(), "\n",
			"\t", "button_text:              ", this.settings.button_text.toString(), "\n",
			"\t", "button_text_style:        ", this.settings.button_text_style.toString(), "\n",
			"\t", "button_text_top_padding:  ", this.settings.button_text_top_padding.toString(), "\n",
			"\t", "button_text_left_padding: ", this.settings.button_text_left_padding.toString(), "\n",
			"\t", "button_action:            ", this.settings.button_action.toString(), "\n",
			"\t", "button_cursor:            ", this.settings.button_cursor.toString(), "\n",
			"\t", "button_disabled:          ", this.settings.button_disabled.toString(), "\n",

			"\t", "custom_settings:          ", this.settings.custom_settings.toString(), "\n",
			"Event Handlers:\n",
			"\t", "swfupload_preload_handler assigned:  ", (typeof this.settings.swfupload_preload_handler === "function").toString(), "\n",
			"\t", "swfupload_load_failed_handler assigned:  ", (typeof this.settings.swfupload_load_failed_handler === "function").toString(), "\n",
			"\t", "swfupload_loaded_handler assigned:  ", (typeof this.settings.swfupload_loaded_handler === "function").toString(), "\n",
			"\t", "mouse_click_handler assigned:       ", (typeof this.settings.mouse_click_handler === "function").toString(), "\n",
			"\t", "mouse_over_handler assigned:        ", (typeof this.settings.mouse_over_handler === "function").toString(), "\n",
			"\t", "mouse_out_handler assigned:         ", (typeof this.settings.mouse_out_handler === "function").toString(), "\n",
			"\t", "file_dialog_start_handler assigned: ", (typeof this.settings.file_dialog_start_handler === "function").toString(), "\n",
			"\t", "file_queued_handler assigned:       ", (typeof this.settings.file_queued_handler === "function").toString(), "\n",
			"\t", "file_queue_error_handler assigned:  ", (typeof this.settings.file_queue_error_handler === "function").toString(), "\n",
			"\t", "upload_resize_start_handler assigned:      ", (typeof this.settings.upload_resize_start_handler === "function").toString(), "\n",
			"\t", "upload_start_handler assigned:      ", (typeof this.settings.upload_start_handler === "function").toString(), "\n",
			"\t", "upload_progress_handler assigned:   ", (typeof this.settings.upload_progress_handler === "function").toString(), "\n",
			"\t", "upload_error_handler assigned:      ", (typeof this.settings.upload_error_handler === "function").toString(), "\n",
			"\t", "upload_success_handler assigned:    ", (typeof this.settings.upload_success_handler === "function").toString(), "\n",
			"\t", "upload_complete_handler assigned:   ", (typeof this.settings.upload_complete_handler === "function").toString(), "\n",
			"\t", "debug_handler assigned:             ", (typeof this.settings.debug_handler === "function").toString(), "\n",

			"Support:\n",
			"\t", "Load:                     ", (this.support.loading ? "Yes" : "No"), "\n",
			"\t", "Image Resize:             ", (this.support.imageResize ? "Yes" : "No"), "\n"

		].join("")
	);
};

/* Note: addSetting and getSetting are no longer used by SWFUpload but are included
	the maintain v2 API compatibility
*/
// Public: (Deprecated) addSetting adds a setting value. If the value given is undefined or null then the default_value is used.
SWFUpload.prototype.addSetting = function (name, value, default_value) {
    if (value == undefined) {
        return (this.settings[name] = default_value);
   } else {
        return (this.settings[name] = value);
	}
};

// Public: (Deprecated) getSetting gets a setting. Returns an empty string if the setting was not found.
SWFUpload.prototype.getSetting = function (name) {
    if (this.settings[name] != undefined) {
        return this.settings[name];
	}
    return "";
};



// Private: callFlash handles function calls made to the Flash element.
// Calls are made with a setTimeout for some functions to work around
// bugs in the ExternalInterface library.
SWFUpload.prototype.callFlash = function (functionName, argumentArray) {
	var movieElement, returnValue, returnString;
	
	argumentArray = argumentArray || [];
	movieElement = this.getMovieElement();

	// Flash's method if calling ExternalInterface methods (code adapted from MooTools).
	try {
		if (movieElement != undefined) {
			returnString = movieElement.CallFunction('<invoke name="' + functionName + '" returntype="javascript">' + __flash__argumentsToXML(argumentArray, 0) + '</invoke>');
			returnValue = eval(returnString);
		} else {
			this.debug("Can't call flash because the movie wasn't found.");
		}
	} catch (ex) {
		this.debug("Exception calling flash function '" + functionName + "': " + ex.message);
	}
	
	// Unescape file post param values
	if (returnValue != undefined && typeof returnValue.post === "object") {
		returnValue = this.unescapeFilePostParams(returnValue);
	}

	return returnValue;
};

/* *****************************
	-- Flash control methods --
	Your UI should use these
	to operate SWFUpload
   ***************************** */

// WARNING: this function does not work in Flash Player 10
// Public: selectFile causes a File Selection Dialog window to appear.  This
// dialog only allows 1 file to be selected.
SWFUpload.prototype.selectFile = function () {
	this.callFlash("SelectFile");
};

// WARNING: this function does not work in Flash Player 10
// Public: selectFiles causes a File Selection Dialog window to appear/ This
// dialog allows the user to select any number of files
// Flash Bug Warning: Flash limits the number of selectable files based on the combined length of the file names.
// If the selection name length is too long the dialog will fail in an unpredictable manner.  There is no work-around
// for this bug.
SWFUpload.prototype.selectFiles = function () {
	this.callFlash("SelectFiles");
};


// Public: startUpload starts uploading the first file in the queue unless
// the optional parameter 'fileID' specifies the ID 
SWFUpload.prototype.startUpload = function (fileID) {
	this.callFlash("StartUpload", [fileID]);
};

// Public: startUpload starts uploading the first file in the queue unless
// the optional parameter 'fileID' specifies the ID 
SWFUpload.prototype.startResizedUpload = function (fileID, width, height, encoding, quality, allowEnlarging) {
	this.callFlash("StartUpload", [fileID, {"width": width, "height" : height, "encoding" : encoding, "quality" : quality, "allowEnlarging" : allowEnlarging}]);
};

// Public: cancelUpload cancels any queued file.  The fileID parameter may be the file ID or index.
// If you do not specify a fileID the current uploading file or first file in the queue is cancelled.
// If you do not want the uploadError event to trigger you can specify false for the triggerErrorEvent parameter.
SWFUpload.prototype.cancelUpload = function (fileID, triggerErrorEvent) {
	if (triggerErrorEvent !== false) {
		triggerErrorEvent = true;
	}
	this.callFlash("CancelUpload", [fileID, triggerErrorEvent]);
};

// Public: stopUpload stops the current upload and requeues the file at the beginning of the queue.
// If nothing is currently uploading then nothing happens.
SWFUpload.prototype.stopUpload = function () {
	this.callFlash("StopUpload");
};


// Public: requeueUpload requeues any file. If the file is requeued or already queued true is returned.
// If the file is not found or is currently uploading false is returned.  Requeuing a file bypasses the
// file size, queue size, upload limit and other queue checks.  Certain files can't be requeued (e.g, invalid or zero bytes files).
SWFUpload.prototype.requeueUpload = function (indexOrFileID) {
	return this.callFlash("RequeueUpload", [indexOrFileID]);
};


/* ************************
 * Settings methods
 *   These methods change the SWFUpload settings.
 *   SWFUpload settings should not be changed directly on the settings object
 *   since many of the settings need to be passed to Flash in order to take
 *   effect.
 * *********************** */

// Public: getStats gets the file statistics object.
SWFUpload.prototype.getStats = function () {
	return this.callFlash("GetStats");
};

// Public: setStats changes the SWFUpload statistics.  You shouldn't need to 
// change the statistics but you can.  Changing the statistics does not
// affect SWFUpload accept for the successful_uploads count which is used
// by the upload_limit setting to determine how many files the user may upload.
SWFUpload.prototype.setStats = function (statsObject) {
	this.callFlash("SetStats", [statsObject]);
};

// Public: getFile retrieves a File object by ID or Index.  If the file is
// not found then 'null' is returned.
SWFUpload.prototype.getFile = function (fileID) {
	if (typeof(fileID) === "number") {
		return this.callFlash("GetFileByIndex", [fileID]);
	} else {
		return this.callFlash("GetFile", [fileID]);
	}
};

// Public: getFileFromQueue retrieves a File object by ID or Index.  If the file is
// not found then 'null' is returned.
SWFUpload.prototype.getQueueFile = function (fileID) {
	if (typeof(fileID) === "number") {
		return this.callFlash("GetFileByQueueIndex", [fileID]);
	} else {
		return this.callFlash("GetFile", [fileID]);
	}
};


// Public: addFileParam sets a name/value pair that will be posted with the
// file specified by the Files ID.  If the name already exists then the
// exiting value will be overwritten.
SWFUpload.prototype.addFileParam = function (fileID, name, value) {
	return this.callFlash("AddFileParam", [fileID, name, value]);
};

// Public: removeFileParam removes a previously set (by addFileParam) name/value
// pair from the specified file.
SWFUpload.prototype.removeFileParam = function (fileID, name) {
	this.callFlash("RemoveFileParam", [fileID, name]);
};

// Public: setUploadUrl changes the upload_url setting.
SWFUpload.prototype.setUploadURL = function (url) {
	this.settings.upload_url = url.toString();
	this.callFlash("SetUploadURL", [url]);
};

// Public: setPostParams changes the post_params setting
SWFUpload.prototype.setPostParams = function (paramsObject) {
	this.settings.post_params = paramsObject;
	this.callFlash("SetPostParams", [paramsObject]);
};

// Public: addPostParam adds post name/value pair.  Each name can have only one value.
SWFUpload.prototype.addPostParam = function (name, value) {
	this.settings.post_params[name] = value;
	this.callFlash("SetPostParams", [this.settings.post_params]);
};

// Public: removePostParam deletes post name/value pair.
SWFUpload.prototype.removePostParam = function (name) {
	delete this.settings.post_params[name];
	this.callFlash("SetPostParams", [this.settings.post_params]);
};

// Public: setFileTypes changes the file_types setting and the file_types_description setting
SWFUpload.prototype.setFileTypes = function (types, description) {
	this.settings.file_types = types;
	this.settings.file_types_description = description;
	this.callFlash("SetFileTypes", [types, description]);
};

// Public: setFileSizeLimit changes the file_size_limit setting
SWFUpload.prototype.setFileSizeLimit = function (fileSizeLimit) {
	this.settings.file_size_limit = fileSizeLimit;
	this.callFlash("SetFileSizeLimit", [fileSizeLimit]);
};

// Public: setFileUploadLimit changes the file_upload_limit setting
SWFUpload.prototype.setFileUploadLimit = function (fileUploadLimit) {
	this.settings.file_upload_limit = fileUploadLimit;
	this.callFlash("SetFileUploadLimit", [fileUploadLimit]);
};

// Public: setFileQueueLimit changes the file_queue_limit setting
SWFUpload.prototype.setFileQueueLimit = function (fileQueueLimit) {
	this.settings.file_queue_limit = fileQueueLimit;
	this.callFlash("SetFileQueueLimit", [fileQueueLimit]);
};

// Public: setFilePostName changes the file_post_name setting
SWFUpload.prototype.setFilePostName = function (filePostName) {
	this.settings.file_post_name = filePostName;
	this.callFlash("SetFilePostName", [filePostName]);
};

// Public: setUseQueryString changes the use_query_string setting
SWFUpload.prototype.setUseQueryString = function (useQueryString) {
	this.settings.use_query_string = useQueryString;
	this.callFlash("SetUseQueryString", [useQueryString]);
};

// Public: setRequeueOnError changes the requeue_on_error setting
SWFUpload.prototype.setRequeueOnError = function (requeueOnError) {
	this.settings.requeue_on_error = requeueOnError;
	this.callFlash("SetRequeueOnError", [requeueOnError]);
};

// Public: setHTTPSuccess changes the http_success setting
SWFUpload.prototype.setHTTPSuccess = function (http_status_codes) {
	if (typeof http_status_codes === "string") {
		http_status_codes = http_status_codes.replace(" ", "").split(",");
	}
	
	this.settings.http_success = http_status_codes;
	this.callFlash("SetHTTPSuccess", [http_status_codes]);
};

// Public: setHTTPSuccess changes the http_success setting
SWFUpload.prototype.setAssumeSuccessTimeout = function (timeout_seconds) {
	this.settings.assume_success_timeout = timeout_seconds;
	this.callFlash("SetAssumeSuccessTimeout", [timeout_seconds]);
};

// Public: setDebugEnabled changes the debug_enabled setting
SWFUpload.prototype.setDebugEnabled = function (debugEnabled) {
	this.settings.debug_enabled = debugEnabled;
	this.callFlash("SetDebugEnabled", [debugEnabled]);
};

// Public: setButtonImageURL loads a button image sprite
SWFUpload.prototype.setButtonImageURL = function (buttonImageURL) {
	if (buttonImageURL == undefined) {
		buttonImageURL = "";
	}
	
	this.settings.button_image_url = buttonImageURL;
	this.callFlash("SetButtonImageURL", [buttonImageURL]);
};

// Public: setButtonDimensions resizes the Flash Movie and button
SWFUpload.prototype.setButtonDimensions = function (width, height) {
	this.settings.button_width = width;
	this.settings.button_height = height;
	
	var movie = this.getMovieElement();
	if (movie != undefined) {
		movie.style.width = width + "px";
		movie.style.height = height + "px";
	}
	
	this.callFlash("SetButtonDimensions", [width, height]);
};
// Public: setButtonText Changes the text overlaid on the button
SWFUpload.prototype.setButtonText = function (html) {
	this.settings.button_text = html;
	this.callFlash("SetButtonText", [html]);
};
// Public: setButtonTextPadding changes the top and left padding of the text overlay
SWFUpload.prototype.setButtonTextPadding = function (left, top) {
	this.settings.button_text_top_padding = top;
	this.settings.button_text_left_padding = left;
	this.callFlash("SetButtonTextPadding", [left, top]);
};

// Public: setButtonTextStyle changes the CSS used to style the HTML/Text overlaid on the button
SWFUpload.prototype.setButtonTextStyle = function (css) {
	this.settings.button_text_style = css;
	this.callFlash("SetButtonTextStyle", [css]);
};
// Public: setButtonDisabled disables/enables the button
SWFUpload.prototype.setButtonDisabled = function (isDisabled) {
	this.settings.button_disabled = isDisabled;
	this.callFlash("SetButtonDisabled", [isDisabled]);
};
// Public: setButtonAction sets the action that occurs when the button is clicked
SWFUpload.prototype.setButtonAction = function (buttonAction) {
	this.settings.button_action = buttonAction;
	this.callFlash("SetButtonAction", [buttonAction]);
};

// Public: setButtonCursor changes the mouse cursor displayed when hovering over the button
SWFUpload.prototype.setButtonCursor = function (cursor) {
	this.settings.button_cursor = cursor;
	this.callFlash("SetButtonCursor", [cursor]);
};

/* *******************************
	Flash Event Interfaces
	These functions are used by Flash to trigger the various
	events.
	
	All these functions a Private.
	
	Because the ExternalInterface library is buggy the event calls
	are added to a queue and the queue then executed by a setTimeout.
	This ensures that events are executed in a determinate order and that
	the ExternalInterface bugs are avoided.
******************************* */

SWFUpload.prototype.queueEvent = function (handlerName, argumentArray) {
	// Warning: Don't call this.debug inside here or you'll create an infinite loop
	var self = this,
		handler = this.settings[handlerName] || this[handlerName];
	
	if (argumentArray == undefined) {
		argumentArray = [];
	} else if (!(argumentArray instanceof Array)) {
		argumentArray = [argumentArray];
	}
	
	if (typeof handler === "function") {
		// Queue the event
		this.eventQueue.push(function () {
			handler.apply(this, argumentArray);
		});
		
		// Execute the next queued event
		setTimeout(function () {
			self.executeNextEvent();
		}, 0);
		
	} else if (handler !== null) {
		throw "Event handler " + handlerName + " is unknown or is not a function";
	}
};

// Private: Causes the next event in the queue to be executed.  Since events are queued using a setTimeout
// we must queue them in order to garentee that they are executed in order.
SWFUpload.prototype.executeNextEvent = function () {
	// Warning: Don't call this.debug inside here or you'll create an infinite loop

	var  f = this.eventQueue ? this.eventQueue.shift() : null;
	if (typeof(f) === "function") {
		f.apply(this);
	}
};

// Private: unescapeFileParams is part of a workaround for a flash bug where objects passed through ExternalInterface cannot have
// properties that contain characters that are not valid for JavaScript identifiers. To work around this
// the Flash Component escapes the parameter names and we must unescape again before passing them along.
SWFUpload.prototype.unescapeFilePostParams = function (file) {
	var reg = /[$]([0-9a-f]{4})/i, unescapedPost = {}, uk, k, match;

	if (file != undefined) {
		for (k in file.post) {
			if (file.post.hasOwnProperty(k)) {
				uk = k;
				while ((match = reg.exec(uk)) !== null) {
					uk = uk.replace(match[0], String.fromCharCode(parseInt("0x" + match[1], 16)));
				}
				unescapedPost[uk] = file.post[k];
			}
		}

		file.post = unescapedPost;
		if (! (window.File && file instanceof window.File))
			file.name = decodeURI(file.name);
	}

	return file;
};

// Private: This event is called by SWFUpload Init after we've determined what the user's Flash Player supports.
// Use the swfupload_preload_handler event setting to execute custom code when SWFUpload has loaded.
// Return false to prevent SWFUpload from loading and allow your script to do something else if your required feature is
// not supported
SWFUpload.prototype.swfuploadPreload = function () {
	var returnValue;
	if (typeof this.settings.swfupload_preload_handler === "function") {
		returnValue = this.settings.swfupload_preload_handler.call(this);
	} else if (this.settings.swfupload_preload_handler != undefined) {
		throw "upload_start_handler must be a function";
	}

	// Convert undefined to true so if nothing is returned from the upload_start_handler it is
	// interpretted as 'true'.
	if (returnValue === undefined) {
		returnValue = true;
	}
	
	return !!returnValue;
}

// Private: This event is called by Flash when it has finished loading. Don't modify this.
// Use the swfupload_loaded_handler event setting to execute custom code when SWFUpload has loaded.
SWFUpload.prototype.flashReady = function () {
	// Check that the movie element is loaded correctly with its ExternalInterface methods defined
	var movieElement = 	this.cleanUp();

	if (!movieElement) {
		this.debug("Flash called back ready but the flash movie can't be found.");
		return;
	}

	this.queueEvent("swfupload_loaded_handler");
};

// Private: removes Flash added fuctions to the DOM node to prevent memory leaks in IE.
// This function is called by Flash each time the ExternalInterface functions are created.
SWFUpload.prototype.cleanUp = function () {
	var key, movieElement = this.getMovieElement();
	
	// Pro-actively unhook all the Flash functions
	try {
		if (movieElement && typeof(movieElement.CallFunction) === "unknown") {// We only want to do this in IE
			this.debug("Removing Flash functions hooks (this should only run in IE and should prevent memory leaks)");
			for (key in movieElement) {
				try {
					if (typeof(movieElement[key]) === "function" && key.charAt(0) <= 'Z') {
						movieElement[key] = null;
					}
				} catch (ex) {
				}
			}
		}
	} catch (ex1) {
	
	}

	// Fix Flashes own cleanup code so if the SWF Movie was removed from the page
	// it doesn't display errors.
	window["__flash__removeCallback"] = function (instance, name) {
		try {
			if (instance) {
				instance[name] = null;
			}
		} catch (flashEx) {
		
		}
	};
	
	return movieElement;
};

/* When the button_action is set to None this event gets fired and executes the mouse_click_handler */
SWFUpload.prototype.mouseClick = function () {
	this.queueEvent("mouse_click_handler");
};
SWFUpload.prototype.mouseOver = function () {
	this.queueEvent("mouse_over_handler");
};
SWFUpload.prototype.mouseOut = function () {
	this.queueEvent("mouse_out_handler");
};

/* This is a chance to do something before the browse window opens */
SWFUpload.prototype.fileDialogStart = function () {
	this.queueEvent("file_dialog_start_handler");
};


/* Called when a file is successfully added to the queue. */
SWFUpload.prototype.fileQueued = function (file) {
	file = this.unescapeFilePostParams(file);
	this.queueEvent("file_queued_handler", file);
};


/* Handle errors that occur when an attempt to queue a file fails. */
SWFUpload.prototype.fileQueueError = function (file, errorCode, message) {
	file = this.unescapeFilePostParams(file);
	this.queueEvent("file_queue_error_handler", [file, errorCode, message]);
};

/* Called after the file dialog has closed and the selected files have been queued.
	You could call startUpload here if you want the queued files to begin uploading immediately. */
SWFUpload.prototype.fileDialogComplete = function (numFilesSelected, numFilesQueued, numFilesInQueue) {
	this.queueEvent("file_dialog_complete_handler", [numFilesSelected, numFilesQueued, numFilesInQueue]);
};

SWFUpload.prototype.uploadResizeStart = function (file, resizeSettings) {
	file = this.unescapeFilePostParams(file);
	this.queueEvent("upload_resize_start_handler", [file, resizeSettings.width, resizeSettings.height, resizeSettings.encoding, resizeSettings.quality]);
};

SWFUpload.prototype.uploadStart = function (file) {
	file = this.unescapeFilePostParams(file);
	this.queueEvent("return_upload_start_handler", file);
};

SWFUpload.prototype.returnUploadStart = function (file) {
	var returnValue;
	if (typeof this.settings.upload_start_handler === "function") {
		file = this.unescapeFilePostParams(file);
		returnValue = this.settings.upload_start_handler.call(this, file);
	} else if (this.settings.upload_start_handler != undefined) {
		throw "upload_start_handler must be a function";
	}

	// Convert undefined to true so if nothing is returned from the upload_start_handler it is
	// interpretted as 'true'.
	if (returnValue === undefined) {
		returnValue = true;
	}
	
	returnValue = !!returnValue;
	
	this.callFlash("ReturnUploadStart", [returnValue]);
};



SWFUpload.prototype.uploadProgress = function (file, bytesComplete, bytesTotal) {
	file = this.unescapeFilePostParams(file);
	this.queueEvent("upload_progress_handler", [file, bytesComplete, bytesTotal]);
};

SWFUpload.prototype.uploadError = function (file, errorCode, message) {
	file = this.unescapeFilePostParams(file);
	this.queueEvent("upload_error_handler", [file, errorCode, message]);
};

SWFUpload.prototype.uploadSuccess = function (file, serverData, responseReceived) {
	file = this.unescapeFilePostParams(file);
	this.queueEvent("upload_success_handler", [file, serverData, responseReceived]);
};

SWFUpload.prototype.uploadComplete = function (file) {
	file = this.unescapeFilePostParams(file);
	this.queueEvent("upload_complete_handler", file);
};

/* Called by SWFUpload JavaScript and Flash functions when debug is enabled. By default it writes messages to the
   internal debug console.  You can override this event and have messages written where you want. */
SWFUpload.prototype.debug = function (message) {
	this.queueEvent("debug_handler", message);
};


/* **********************************
	Debug Console
	The debug console is a self contained, in page location
	for debug message to be sent.  The Debug Console adds
	itself to the body if necessary.

	The console is automatically scrolled as messages appear.
	
	If you are using your own debug handler or when you deploy to production and
	have debug disabled you can remove these functions to reduce the file size
	and complexity.
********************************** */
   
// Private: debugMessage is the default debug_handler.  If you want to print debug messages
// call the debug() function.  When overriding the function your own function should
// check to see if the debug setting is true before outputting debug information.
SWFUpload.prototype.debugMessage = function (message) {
	var exceptionMessage, exceptionValues, key;

	if (this.settings.debug) {
		exceptionValues = [];

		// Check for an exception object and print it nicely
		if (typeof message === "object" && typeof message.name === "string" && typeof message.message === "string") {
			for (key in message) {
				if (message.hasOwnProperty(key)) {
					exceptionValues.push(key + ": " + message[key]);
				}
			}
			exceptionMessage = exceptionValues.join("\n") || "";
			exceptionValues = exceptionMessage.split("\n");
			exceptionMessage = "EXCEPTION: " + exceptionValues.join("\nEXCEPTION: ");
			SWFUpload.Console.writeLine(exceptionMessage);
		} else {
			SWFUpload.Console.writeLine(message);
		}
	}
};

SWFUpload.Console = {};
SWFUpload.Console.writeLine = function (message) {
	var console, documentForm;

	try {
		console = document.getElementById("SWFUpload_Console");

		if (!console) {
			documentForm = document.createElement("form");
			document.getElementsByTagName("body")[0].appendChild(documentForm);

			console = document.createElement("textarea");
			console.id = "SWFUpload_Console";
			console.style.fontFamily = "monospace";
			console.setAttribute("wrap", "off");
			console.wrap = "off";
			console.style.overflow = "auto";
			console.style.width = "700px";
			console.style.height = "350px";
			console.style.margin = "5px";
			documentForm.appendChild(console);
		}

		console.value += message + "\n";

		console.scrollTop = console.scrollHeight - console.clientHeight;
	} catch (ex) {
		alert("Exception: " + ex.name + " Message: " + ex.message);
	}
};

/*	SWFObject v2.2 <http://code.google.com/p/swfobject/> 
	is released under the MIT License <http://www.opensource.org/licenses/mit-license.php> 
*/
var swfobject = function(){var D="undefined",r="object",S="Shockwave Flash",W="ShockwaveFlash.ShockwaveFlash",q="application/x-shockwave-flash",R="SWFObjectExprInst",x="onreadystatechange",O=window,j=document,t=navigator,T=false,U=[h],o=[],N=[],I=[],l,Q,E,B,J=false,a=false,n,G,m=true,M=function(){var aa=typeof j.getElementById!=D&&typeof j.getElementsByTagName!=D&&typeof j.createElement!=D,ah=t.userAgent.toLowerCase(),Y=t.platform.toLowerCase(),ae=Y?/win/.test(Y):/win/.test(ah),ac=Y?/mac/.test(Y):/mac/.test(ah),af=/webkit/.test(ah)?parseFloat(ah.replace(/^.*webkit\/(\d+(\.\d+)?).*$/,"$1")):false,X=!+"\v1",ag=[0,0,0],ab=null;if(typeof t.plugins!=D&&typeof t.plugins[S]==r){ab=t.plugins[S].description;if(ab&&!(typeof t.mimeTypes!=D&&t.mimeTypes[q]&&!t.mimeTypes[q].enabledPlugin)){T=true;X=false;ab=ab.replace(/^.*\s+(\S+\s+\S+$)/,"$1");ag[0]=parseInt(ab.replace(/^(.*)\..*$/,"$1"),10);ag[1]=parseInt(ab.replace(/^.*\.(.*)\s.*$/,"$1"),10);ag[2]=/[a-zA-Z]/.test(ab)?parseInt(ab.replace(/^.*[a-zA-Z]+(.*)$/,"$1"),10):0}}else{if(typeof O.ActiveXObject!=D){try{var ad=new ActiveXObject(W);if(ad){ab=ad.GetVariable("$version");if(ab){X=true;ab=ab.split(" ")[1].split(",");ag=[parseInt(ab[0],10),parseInt(ab[1],10),parseInt(ab[2],10)]}}}catch(Z){}}}return{w3:aa,pv:ag,wk:af,ie:X,win:ae,mac:ac}}(),k=function(){if(!M.w3){return}if((typeof j.readyState!=D&&j.readyState=="complete")||(typeof j.readyState==D&&(j.getElementsByTagName("body")[0]||j.body))){f()}if(!J){if(typeof j.addEventListener!=D){j.addEventListener("DOMContentLoaded",f,false)}if(M.ie&&M.win){j.attachEvent(x,function(){if(j.readyState=="complete"){j.detachEvent(x,arguments.callee);f()}});if(O==top){(function(){if(J){return}try{j.documentElement.doScroll("left")}catch(X){setTimeout(arguments.callee,0);return}f()})()}}if(M.wk){(function(){if(J){return}if(!/loaded|complete/.test(j.readyState)){setTimeout(arguments.callee,0);return}f()})()}s(f)}}();function f(){if(J){return}try{var Z=j.getElementsByTagName("body")[0].appendChild(C("span"));Z.parentNode.removeChild(Z)}catch(aa){return}J=true;var X=U.length;for(var Y=0;Y<X;Y++){U[Y]()}}function K(X){if(J){X()}else{U[U.length]=X}}function s(Y){if(typeof O.addEventListener!=D){O.addEventListener("load",Y,false)}else{if(typeof j.addEventListener!=D){j.addEventListener("load",Y,false)}else{if(typeof O.attachEvent!=D){i(O,"onload",Y)}else{if(typeof O.onload=="function"){var X=O.onload;O.onload=function(){X();Y()}}else{O.onload=Y}}}}}function h(){if(T){V()}else{H()}}function V(){var X=j.getElementsByTagName("body")[0];var aa=C(r);aa.setAttribute("type",q);var Z=X.appendChild(aa);if(Z){var Y=0;(function(){if(typeof Z.GetVariable!=D){var ab=Z.GetVariable("$version");if(ab){ab=ab.split(" ")[1].split(",");M.pv=[parseInt(ab[0],10),parseInt(ab[1],10),parseInt(ab[2],10)]}}else{if(Y<10){Y++;setTimeout(arguments.callee,10);return}}X.removeChild(aa);Z=null;H()})()}else{H()}}function H(){var ag=o.length;if(ag>0){for(var af=0;af<ag;af++){var Y=o[af].id;var ab=o[af].callbackFn;var aa={success:false,id:Y};if(M.pv[0]>0){var ae=c(Y);if(ae){if(F(o[af].swfVersion)&&!(M.wk&&M.wk<312)){w(Y,true);if(ab){aa.success=true;aa.ref=z(Y);ab(aa)}}else{if(o[af].expressInstall&&A()){var ai={};ai.data=o[af].expressInstall;ai.width=ae.getAttribute("width")||"0";ai.height=ae.getAttribute("height")||"0";if(ae.getAttribute("class")){ai.styleclass=ae.getAttribute("class")}if(ae.getAttribute("align")){ai.align=ae.getAttribute("align")}var ah={};var X=ae.getElementsByTagName("param");var ac=X.length;for(var ad=0;ad<ac;ad++){if(X[ad].getAttribute("name").toLowerCase()!="movie"){ah[X[ad].getAttribute("name")]=X[ad].getAttribute("value")}}P(ai,ah,Y,ab)}else{p(ae);if(ab){ab(aa)}}}}}else{w(Y,true);if(ab){var Z=z(Y);if(Z&&typeof Z.SetVariable!=D){aa.success=true;aa.ref=Z}ab(aa)}}}}}function z(aa){var X=null;var Y=c(aa);if(Y&&Y.nodeName=="OBJECT"){if(typeof Y.SetVariable!=D){X=Y}else{var Z=Y.getElementsByTagName(r)[0];if(Z){X=Z}}}return X}function A(){return !a&&F("6.0.65")&&(M.win||M.mac)&&!(M.wk&&M.wk<312)}function P(aa,ab,X,Z){a=true;E=Z||null;B={success:false,id:X};var ae=c(X);if(ae){if(ae.nodeName=="OBJECT"){l=g(ae);Q=null}else{l=ae;Q=X}aa.id=R;if(typeof aa.width==D||(!/%$/.test(aa.width)&&parseInt(aa.width,10)<310)){aa.width="310"}if(typeof aa.height==D||(!/%$/.test(aa.height)&&parseInt(aa.height,10)<137)){aa.height="137"}j.title=j.title.slice(0,47)+" - Flash Player Installation";var ad=M.ie&&M.win?"ActiveX":"PlugIn",ac="MMredirectURL="+O.location.toString().replace(/&/g,"%26")+"&MMplayerType="+ad+"&MMdoctitle="+j.title;if(typeof ab.flashvars!=D){ab.flashvars+="&"+ac}else{ab.flashvars=ac}if(M.ie&&M.win&&ae.readyState!=4){var Y=C("div");X+="SWFObjectNew";Y.setAttribute("id",X);ae.parentNode.insertBefore(Y,ae);ae.style.display="none";(function(){if(ae.readyState==4){ae.parentNode.removeChild(ae)}else{setTimeout(arguments.callee,10)}})()}u(aa,ab,X)}}function p(Y){if(M.ie&&M.win&&Y.readyState!=4){var X=C("div");Y.parentNode.insertBefore(X,Y);X.parentNode.replaceChild(g(Y),X);Y.style.display="none";(function(){if(Y.readyState==4){Y.parentNode.removeChild(Y)}else{setTimeout(arguments.callee,10)}})()}else{Y.parentNode.replaceChild(g(Y),Y)}}function g(ab){var aa=C("div");if(M.win&&M.ie){aa.innerHTML=ab.innerHTML}else{var Y=ab.getElementsByTagName(r)[0];if(Y){var ad=Y.childNodes;if(ad){var X=ad.length;for(var Z=0;Z<X;Z++){if(!(ad[Z].nodeType==1&&ad[Z].nodeName=="PARAM")&&!(ad[Z].nodeType==8)){aa.appendChild(ad[Z].cloneNode(true))}}}}}return aa}function u(ai,ag,Y){var X,aa=c(Y);if(M.wk&&M.wk<312){return X}if(aa){if(typeof ai.id==D){ai.id=Y}if(M.ie&&M.win){var ah="";for(var ae in ai){if(ai[ae]!=Object.prototype[ae]){if(ae.toLowerCase()=="data"){ag.movie=ai[ae]}else{if(ae.toLowerCase()=="styleclass"){ah+=' class="'+ai[ae]+'"'}else{if(ae.toLowerCase()!="classid"){ah+=" "+ae+'="'+ai[ae]+'"'}}}}}var af="";for(var ad in ag){if(ag[ad]!=Object.prototype[ad]){af+='<param name="'+ad+'" value="'+ag[ad]+'" />'}}aa.outerHTML='<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"'+ah+">"+af+"</object>";N[N.length]=ai.id;X=c(ai.id)}else{var Z=C(r);Z.setAttribute("type",q);for(var ac in ai){if(ai[ac]!=Object.prototype[ac]){if(ac.toLowerCase()=="styleclass"){Z.setAttribute("class",ai[ac])}else{if(ac.toLowerCase()!="classid"){Z.setAttribute(ac,ai[ac])}}}}for(var ab in ag){if(ag[ab]!=Object.prototype[ab]&&ab.toLowerCase()!="movie"){e(Z,ab,ag[ab])}}aa.parentNode.replaceChild(Z,aa);X=Z}}return X}function e(Z,X,Y){var aa=C("param");aa.setAttribute("name",X);aa.setAttribute("value",Y);Z.appendChild(aa)}function y(Y){var X=c(Y);if(X&&X.nodeName=="OBJECT"){if(M.ie&&M.win){X.style.display="none";(function(){if(X.readyState==4){b(Y)}else{setTimeout(arguments.callee,10)}})()}else{X.parentNode.removeChild(X)}}}function b(Z){var Y=c(Z);if(Y){for(var X in Y){if(typeof Y[X]=="function"){Y[X]=null}}Y.parentNode.removeChild(Y)}}function c(Z){var X=null;try{X=j.getElementById(Z)}catch(Y){}return X}function C(X){return j.createElement(X)}function i(Z,X,Y){Z.attachEvent(X,Y);I[I.length]=[Z,X,Y]}function F(Z){var Y=M.pv,X=Z.split(".");X[0]=parseInt(X[0],10);X[1]=parseInt(X[1],10)||0;X[2]=parseInt(X[2],10)||0;return(Y[0]>X[0]||(Y[0]==X[0]&&Y[1]>X[1])||(Y[0]==X[0]&&Y[1]==X[1]&&Y[2]>=X[2]))?true:false}function v(ac,Y,ad,ab){if(M.ie&&M.mac){return}var aa=j.getElementsByTagName("head")[0];if(!aa){return}var X=(ad&&typeof ad=="string")?ad:"screen";if(ab){n=null;G=null}if(!n||G!=X){var Z=C("style");Z.setAttribute("type","text/css");Z.setAttribute("media",X);n=aa.appendChild(Z);if(M.ie&&M.win&&typeof j.styleSheets!=D&&j.styleSheets.length>0){n=j.styleSheets[j.styleSheets.length-1]}G=X}if(M.ie&&M.win){if(n&&typeof n.addRule==r){n.addRule(ac,Y)}}else{if(n&&typeof j.createTextNode!=D){n.appendChild(j.createTextNode(ac+" {"+Y+"}"))}}}function w(Z,X){if(!m){return}var Y=X?"visible":"hidden";if(J&&c(Z)){c(Z).style.visibility=Y}else{v("#"+Z,"visibility:"+Y)}}function L(Y){var Z=/[\\\"<>\.;]/;var X=Z.exec(Y)!=null;return X&&typeof encodeURIComponent!=D?encodeURIComponent(Y):Y}var d=function(){if(M.ie&&M.win){window.attachEvent("onunload",function(){var ac=I.length;for(var ab=0;ab<ac;ab++){I[ab][0].detachEvent(I[ab][1],I[ab][2])}var Z=N.length;for(var aa=0;aa<Z;aa++){y(N[aa])}for(var Y in M){M[Y]=null}M=null;for(var X in swfobject){swfobject[X]=null}swfobject=null})}}();return{registerObject:function(ab,X,aa,Z){if(M.w3&&ab&&X){var Y={};Y.id=ab;Y.swfVersion=X;Y.expressInstall=aa;Y.callbackFn=Z;o[o.length]=Y;w(ab,false)}else{if(Z){Z({success:false,id:ab})}}},getObjectById:function(X){if(M.w3){return z(X)}},embedSWF:function(ab,ah,ae,ag,Y,aa,Z,ad,af,ac){var X={success:false,id:ah};if(M.w3&&!(M.wk&&M.wk<312)&&ab&&ah&&ae&&ag&&Y){w(ah,false);K(function(){ae+="";ag+="";var aj={};if(af&&typeof af===r){for(var al in af){aj[al]=af[al]}}aj.data=ab;aj.width=ae;aj.height=ag;var am={};if(ad&&typeof ad===r){for(var ak in ad){am[ak]=ad[ak]}}if(Z&&typeof Z===r){for(var ai in Z){if(typeof am.flashvars!=D){am.flashvars+="&"+ai+"="+Z[ai]}else{am.flashvars=ai+"="+Z[ai]}}}if(F(Y)){var an=u(aj,am,ah);if(aj.id==ah){w(ah,true)}X.success=true;X.ref=an}else{if(aa&&A()){aj.data=aa;P(aj,am,ah,ac);return}else{w(ah,true)}}if(ac){ac(X)}})}else{if(ac){ac(X)}}},switchOffAutoHideShow:function(){m=false},ua:M,getFlashPlayerVersion:function(){return{major:M.pv[0],minor:M.pv[1],release:M.pv[2]}},hasFlashPlayerVersion:F,createSWF:function(Z,Y,X){if(M.w3){return u(Z,Y,X)}else{return undefined}},showExpressInstall:function(Z,aa,X,Y){if(M.w3&&A()){P(Z,aa,X,Y)}},removeSWF:function(X){if(M.w3){y(X)}},createCSS:function(aa,Z,Y,X){if(M.w3){v(aa,Z,Y,X)}},addDomLoadEvent:K,addLoadEvent:s,getQueryParamValue:function(aa){var Z=j.location.search||j.location.hash;if(Z){if(/\?/.test(Z)){Z=Z.split("?")[1]}if(aa==null){return L(Z)}var Y=Z.split("&");for(var X=0;X<Y.length;X++){if(Y[X].substring(0,Y[X].indexOf("="))==aa){return L(Y[X].substring((Y[X].indexOf("=")+1)))}}}return""},expressInstallCallback:function(){if(a){var X=c(R);if(X&&l){X.parentNode.replaceChild(l,X);if(Q){w(Q,true);if(M.ie&&M.win){l.style.display="block"}}if(E){E(B)}}a=false}}}}();
swfobject.addDomLoadEvent(function () {if (typeof(SWFUpload.onload) === "function") {SWFUpload.onload.call(window);}});

/*
   {type: 'upload/file', download: "xxx", preview: "xxx"}
   如果只配置了 download，点击文件都是下载。
   如果同时配置 download 和 preview，download 无效，preview 有效。
   如果只设置 preview，返回命令做处置。
   如果需要特殊处理，比如doc文件要预览，那么设置 pub: {on: {click: "myFile.doSomething($id,$name);return false;"}
 */

/* `upload` */
var U,
$ = require('dfish'),
Loc = require('loc'),
W = require('Widget'),
Horz = require('Horz'),
Button = require('Button'),
ButtonBar = require('ButtonBar'),
AbsForm = require('AbsForm'),

evw = $.abbr + '.w(this)',

_dftFileTypes = {
	'FileUpload': '*.*',
	'ImageUpload': '*.png;*.wpng;*.gif;*.svg;*.jpg;*.jpeg;*.tif;*.tiff;*.bmp;*.xbm;*.xpm;*.xwd;*.cal;*.cmx;*.cod;*.dcx;*.eri;*.fh4;*.fh5;*.fhc;',
	'VideoUpload': '*.mp4;*.avi;*.rm;*.rmvb;*.wmv;*.webm;*.mpa;*.mpe;*.mpeg;*.mpg;*.mpg4;*.mpv2;*.3gp;*.asf;*.asr;*.asx;*.fvi;*.lsf;*.lsx;*.m4u;*.m4v;*.mng;*.mov;*.movie;*.mp2;*.pvx;*.qt;*.rv;*.vdo;*.viv;*.vivo;*.wm;*.wmx;*.wv;*.wvx;'
},

UploadPost = define.widget('UploadPost', {
	Extend: 'AbsSection'
}),

AbsUpload = define.widget('AbsUpload', {
	Const: function(x) {
		AbsForm.apply(this, arguments);
		x.post && (this.post = new UploadPost(x.post, this, -1));
		this.x = $.merge({
			file_types: _dftFileTypes[this.type],
			file_types_description: 'All Files',
			file_upload_limit: 0,
			button_disabled: !!(x.status && x.status !== 'normal'),
			flash_url: $.SWF_PATH + 'swfupload.swf',
			flash9_url: $.SWF_PATH + 'swfupload_fp9.swf',
			upload_url: this.post && this.post.getSrc()
		}, swfOptions(x));
		var v = x.value || [];
		if (typeof v === 'string')
			v = v.charAt(0) == '[' ? $.jsonParse(v) : [];
		this._value    = v.concat();
		this.uploadbar = this.add({type: this.type + 'ButtonBar'});
		this.valuebar  = this.add({type: this.type + 'ValueBar', br: true, cls: '_vbar'});
		this._queues   = [];
		if (! x.uploadButtons)
			this.className += ' z-lmt';
		if (! this.valuebar.length)
			this.className += ' z-nofiles';
	},
	Default:{height: -1},
	Extend: AbsForm,
	Prototype: {
		isFormWidget: true,
		val: function(a) {
			if (a == null)
				return this.ipt().value;
			if (typeof a === 'string')
				a = $.jsonParse(a) || [];
			this._value = a;
			this.save();
			for (var i = this.valuebar.length - 1; i > -1; i --)
				this.valuebar[i].loaded && this.valuebar[i].remove();
			for (i = 0; i < a.length; i ++)
				this.valuebar.append({data: a[i]});
		},
		//@a -> orignal?
		isModified: function(a) {
			var v = a || this._modval == null ? (this.x.value || []) : this._modval;
			if (typeof v === 'string') {
				v = v.charAt(0) == '[' ? $.jsonParse(v) : [];
			}
			u = this._value || [];
			if (v.length !== u.length)
				return true;
			for (var i = 0; i < v.length; i ++) {
				if (v[i].id != u[i].id)
					return true;
			}
		},
		saveModified: function() {
			this._modval = this.val();
		},
		// @a -> default?
		reset: function(a) {
			this.isModified(a) && this.val(a || this._modval == null ? this.x.value : this._modval);
		},
		normal: function() {
			this.x.status = 'normal';
			$.classRemove(this.$(), 'z-err z-ds');
			this.setButtonDisabled(false);
			W.prototype.normal.apply(this, arguments);
			return this;
		},
		readonly: function(a) {
			a = a == null || a;
			$.classRemove(this.$(), 'z-err');
			this.setButtonDisabled(a);
			W.prototype.readonly.apply(this, arguments);
			return this;
		},
		validonly: function(a) {
			a = a == null || a;
			$.classRemove(this.$(), 'z-err');
			this.setButtonDisabled(a);
			W.prototype.validonly.apply(this, arguments);
			return this;
		},
		disable: function(a) {
			a = a == null || a;
			$.classRemove(this.$(), 'z-err');
			this.setButtonDisabled(a);
			W.prototype.disable.apply(this, arguments);
			return this;
		},
		ipt: function() {
			return this.$('v');
		},
		getLoaderByFile: function(a) {
			for (var i = 0, f; i < this.valuebar.length; i ++) {
				if ((f = this.valuebar[i].x.file) && (f.id === a.id))
					return this.valuebar[i];
			}
		},
		addValue: function(a) {
			this._value.push(a);
			this.save();
		},
		removeValue: function(a) {
			$.arrPop(this._value, a);
			this.save();
		},
		save: function() {
			var v = $.jsonString(this._value);
			this.ipt().value = v == '[]' ? '' : v;
		},
		getNewLoaders: function() {
			for (var i = 0, d = this.valuebar, r = []; i < d.length; i ++)
				! d[i].$() && r.push(d[i]);
			return r;
		},
		addNewLoaders: function(numFilesSelected, numFilesQueued, numFilesInQueue) {
			if (numFilesInQueue > 0) {
				for (var i = numFilesInQueue - numFilesQueued, f; i < numFilesInQueue; i ++) {
					(f = this.getQueueFile(i)) && this.valuebar.add({file: f});
				}
				//this.trigger('fileselect');
				for (var i = 0, d = this.getNewLoaders(), l = d.length, s = []; i < l; i ++)
					d[i].render();
				this.startUpload();
			}
		},
		file_queue_error_handler: function(file, errorCode, message) {
			var ro  = SWFUpload.QUEUE_ERROR,
				msg = message;
			if (errorCode == ro.QUEUE_LIMIT_EXCEEDED) {
				msg = '上传数量超过上限(最多' + this.x.file_upload_limit + '个)';
			} else if (errorCode == ro.INVALID_FILETYPE) {
				msg = '无效的文件类型';
			} else if (errorCode == ro.FILE_EXCEEDS_SIZE_LIMIT) {
				msg = '文件大小不能超过' + this.x.file_size_limit + '';
			} else if (errorCode == ro.FILE_MIN_SIZE_LIMIT) {
				msg = '文件大小不能小于' + this.x.minFileSize + '';
			}
			$.alert('上传失败：' + (file ? file.name : '') + '\n\n' + msg);
		},
		file_dialog_complete_handler: function(numFilesSelected, numFilesQueued, numFilesInQueue) {
			$.br.app ? $.wifiConfirm(function() {this.addNewLoaders(numFilesSelected, numFilesQueued, numFilesInQueue);}, this) : this.addNewLoaders(numFilesSelected, numFilesQueued, numFilesInQueue);
			$.classRemove(this.$(), 'z-err');
		},
		upload_progress_handler: function(file, bytesLoaded, bytesTotal) {
			var ldr = this.getLoaderByFile(file);
			if (ldr) {
				var percent = Math.ceil((bytesLoaded / bytesTotal) * 100);
				ldr.setProgress(percent);
			}
		},
		upload_success_handler: function(file, serverData) {
			var ldr = this.getLoaderByFile(file), r;
			try {
				eval('r=' + serverData);
			} catch(e) {}
			if (! r || W.isCmd(r) || r.error) {
				this.uploadError(file, SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED, (r && r.error) || serverData);	
			} else {
				this.post.srcData(r);
				ldr.setSuccess(this.post.getResult());
			}
		},
		upload_complete_handler: function(file) {
			if (this.getQueueFile(0))
				this.startUpload();
			else
				this.trigger('change');
		},
		upload_error_handler: function(file, errorCode, message) {
			var ldr = this.getLoaderByFile(file);
			if (ldr) {
				if (typeof message === 'string')
					$.alert(message);
				else if (message.text)
					$.alert(message.text);
				else if (W.isCmd(message))
					this.cmd(message);
				ldr.setError(errorCode, message);
			}
		}
	}
}),

Upload = define.widget('AjaxUpload', {
	Extend: AbsUpload,
	Listener: {
		body: {
			ready: $.rt()
		}
	},
	Prototype: {
		setButtonDisabled: function() {
			//implement
		},
		getQueueFile: function(i) {
			return this._queues[i];
		},
		fileQueueError: function() {
			return this.file_queue_error_handler.apply(this, arguments);
		},
		uploadError: function() {
			return this.upload_error_handler.apply(this, arguments);
		},
		startUpload: function() {
			for (var i = 0, ldr; i < this.valuebar.length; i ++) {
				ldr = this.valuebar[i];
				if (ldr.loading)
					return;
				if (! ldr.loading && ! ldr.loaded)
					break;
			}
			ldr && ldr.upload();
		}
	}
}),

FlashUpload = define.widget('FlashUpload', {
	Extend: [AbsUpload, SWFUpload],
	Listener: {
		body: {
			ready: function() {
				var s = this.x.file_size_limit;
				if (s && /[KMG]$/i.test(s))
					this.x.file_size_limit = s.toUpperCase() + 'B';
				this.settings = $.extend(this.x, {
					button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
					button_cursor: SWFUpload.CURSOR.HAND,
					button_placeholder_id: this.id + 'swf'
				});
				if ($.x.ajaxData)
					this.settings.post_params = $.extend(this.settings.post_params || {}, $.x.ajaxData);
				this.initSWFUpload(this.settings);
			}
		}
	},
	Prototype: {
		uploadStats: function(i) {
			var stats = this.getStats();
			if (stats) {
				if (i !== U) {
					stats.successful_uploads += i;
					this.setStats(stats);
				}
				return stats.successful_uploads;
			}
		},
		file_queued_handler: function(file) {
			var n = this.x.minFileSize;
			if(n != null && file.size < fileByte(n)) {
				this.cancelUpload(file.id);
				this.fileQueueError(file, SWFUpload.QUEUE_ERROR.FILE_MIN_SIZE_LIMIT);
			}
		},
		swfupload_load_failed_handler: function() {
			if (this.x.uploadButtons)
				this.cmd({type: 'Alert', id: 'upload_base_swf', text: '使用上传功能需要安装flash插件。 <a href=' + ($.x.support_url ? $.urlFormat($.x.support_url, ['flash']) : 'http://flash.cn') + ' target=_blank><b>点此下载>></b></a>'});
		},
		swfupload_loaded_handler: function() {
			this.uploadStats(this._value.length);
		}
	}
}),

isSWF = ! window.FormData,
uploadCount = 0, fileCount = 0;

if (isSWF) {
	Upload = FlashUpload;
	window.SWFUpload = SWFUpload;
}

/*! `upload/file` */
var 
FileUpload = define.widget('FileUpload', {
	Extend: Upload,
	Listener: {
		body: {
			ready: function() {
				Upload.Listener.body.ready.apply(this, arguments);
				this.fixLabelVAlign();
			},
			statuschange: function() {
				this.fixLabelVAlign();
			}
		}
	},
	Prototype: {
		className: 'w-upload',
		validHooks: {
			valid: function(b, v) {
				var b = this.valuebar, l = b.length, e;
				for (var i = 0; i < l; i ++) {
					if (b[i].loading) {e = Loc.form.upload_loading; break;}
					if (b[i].error) {e = b[i].error.text || Loc.form.upload_invalid_file; break;}
				}
				return e && {name: this.x.name, wid: this.id, code: 'upload', text: e};
			}
		},
		validTip: function(x) {
			return {type: 'Tip', face: 'warn', text: x.text, snap: {target: this.getTipLoader()}};
		},
		append: function(a) {
			if (typeof a === 'string')
				a = $.jsonParse(a);
			if ($.isArray(a)) {
				for (var i = 0; i < a.length; i ++)
					this.append(a[i]);
			} else if (! this.isLimit()) {
				this.addValue(a);
				this.valuebar.add({data: a}).render();
			}
		},
		text: function() {
			for (var i = 0, s = []; i < this._value.length; i ++) {
				s.push(this._value[i].name);
			}
			return s.join(', ');
		},
		fixLabelVAlign: function() {
			this.label && this.label.addClass('z-va', !!(this.hasClass('z-lmt z-ds') && this._value.length));
		},
		getTipLoader: function() {
			var b = this.valuebar, l = b.length;
			for (var i = 0; i < l; i ++) {
				if (b[i].loading || b[i].error) return b[i];
			}
		},
		isLimit: function() {
			return this.x.file_upload_limit > 0 && (this.valuebar || this._value).length >= this.x.file_upload_limit;
		},
		html_input: function() {
			return '<input type=hidden id=' + this.id + 'v name=' + this.x.name + ' value=\'' + (this._value.length ? $.jsonString(this._value) : '') + '\'>';
		},
		html_nodes: function() {
			return (this.uploadbar && this.uploadbar.length ? this.uploadbar.html() : '') + this.valuebar.html() + this.html_input();
		}
	}
}),

/*! ImageUpload */
ImageUpload = define.widget('ImageUpload', {
	Extend: FileUpload,
	Prototype: {
		html_nodes: function() {
			return this.valuebar.html() + this.html_input();
		}
	}
}),

/*! VideoUpload */
VideoUpload = define.widget( 'VideoUpload', {
	Extend: ImageUpload
} ),

FileUploadButtonBar = define.widget('FileUploadButtonBar', {
	Const: function(x, p) {
		this.u = p;
		ButtonBar.apply(this, arguments);
	},
	Extend: ButtonBar,
	Default: {space: 10, width: -1, height: -1},
	Prototype: {
		className: 'w-buttonbar _bbar',
		x_nodes: function(x) {
			return this.u.x.uploadButtons;
		},
		x_childtype: function(t) {
			return 'FileUpload' + t;
		}
	}
}),

ImageUploadButtonBar = define.widget('ImageUploadButtonBar', {
	Extend: FileUploadButtonBar,
	Prototype: {
		x_childtype: function(t) {
			return 'ImageUpload' + t;
		}
	}
}),

VideoUploadButtonBar = define.widget('VideoUploadButtonBar', {
	Extend: ImageUploadButtonBar,
	Prototype: {
		x_childtype: function(t) {
			return 'VideoUpload' + t;
		}
	}
}),

// 附件列表
FileUploadValueBar = define.widget('FileUploadValueBar', {
	Const: function(x, p) {
		this.u = p;
		x.width = x.height = -1;
		Horz.call(this, x, p);
		for (var i = 0, n = []; i < p._value.length; i ++)
			p._value[i] && this.add({data: p._value[i]});
		this.childCls = p.x.dir === 'v' ? 'f-left f-clear' : 'f-sub-horz';
	},
	Extend: 'Horz',
	Listener: {
		body: {
			ready: function() {
				this.trigger('nodeChange');
			},
			nodeChange: function() {
				var u = this.u;
				$.classAdd(u.$(), 'z-lmt', u.isLimit());
				this.length && this.$('nf') && $.remove(this.$('nf'));
				! this.length && ! this.$('nf') && Q(this.$()).prepend(this.html_nofiles());
				$.classAdd(u.$(), 'z-nofiles', !this.length);
				u.fixLabelVAlign();
			}
		}
	},
	Prototype: {
		x_childtype: $.rt('FileUploadValue'),
		html_nofiles: function() {
			return '<div id=' + this.id + 'nf class="_nofiles f-nv">' + Loc.form.no_files + '</div>';
		},
		html_nodes: function() {
			return (this.length ? '' : this.html_nofiles()) + Horz.prototype.html_nodes.call(this);
		}
	}
}),

ImageUploadValueBar = define.widget('ImageUploadValueBar', {
	Extend: FileUploadValueBar,
	Prototype: {
		x_childtype: $.rt('ImageUploadValue'),
		// @implement / a -> html|widget, b -> where(prepend|append|before|after)
		insertHTML: function(a, b) {
			if (! b || b === 'append') {
				$.before(this.parentNode.uploadbar.$(), a.isWidget ? a.$() : a);
			} else
				W.prototype.insertHTML.call(this, a, b);
		},
		html_nodes: function() {
			return FileUploadValueBar.prototype.html_nodes.call(this) + (this.parentNode.uploadbar ? this.parentNode.uploadbar.html() : '');
		}
	}
}),

VideoUploadValueBar = define.widget('VideoUploadValueBar', {
	Extend: ImageUploadValueBar,
	Prototype: {
		x_childtype: $.rt('VideoUploadValue')
	}
}),

FileUploadButton = define.widget('FileUploadButton', {
	Const: function(x, p) {
		this.u = p.parentNode;
		Button.apply(this, arguments);
	},
	Extend: Button,
	Prototype: {
		className: 'w-button w-uploadbutton'
	}
}),

ImageUploadButton = define.widget('ImageUploadButton', {
	Const: function(x, p) {
		FileUploadButton.apply(this, arguments);
		var b = this.u.x.pub || false, w = x.width || b.width || 80, h = x.height || b.height || 80, t = this.x.style || '';
		if (w || h) {
			t += 'width:' + (w - (this.attr('widthMinus') || 0)) + 'px;height:' + (h - (this.attr('heightMinus') || 0)) + 'px;';
			this.x.style = t;
		}
		this.u.x.dir === 'v' && $.classAdd(this.parentNode, 'f-left f-clear');
	},
	Extend: FileUploadButton
}),

VideoUploadButton = define.widget('VideoUploadButton', {
	Extend: ImageUploadButton
}),

// 选择本地文件的按钮
FileUploadUploadButton = define.widget('FileUploadUploadButton', {
	Const: function(x, p) {
		FileUploadButton.apply(this, arguments);
		this.fileID = this.id + 'u-' + uploadCount;
	},
	Extend: FileUploadButton,
	Listener: {
		body: {
			click: function() {
				if (this.u.isNormal()) {
					Button.Listener.body.click.apply(this, arguments);
					! this.x.nodes && this.selectFile();
				}
			}
		}
	},
	Prototype: {
		getAccept: function() {
			var t = this.u.x.file_types;
			return t && t !== '*.*' ? getFileType(t) : '';
		},
		getFileTypes: function() {
			var t = this.u.x.file_types;
			return t && t !== '*.*' ? t.replace(/\*/g, '').replace(/;/g, ',').toLowerCase() : '';
		},
		selectFile: function() {
			$(this.fileID).click();
		},
		fileSelected: function(o) {
			var u = this.u, b = o.files, d = b.length, t = this.getFileTypes();
			for (var i = 0, k = 0; i < d; i ++) {
				if (u.x.file_upload_limit > 1 && u.valuebar.length + (i + 1) > u.x.file_upload_limit) {
					u.fileQueueError(b[i], SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED);
					continue;
				} else if (u.x.file_size_limit != null && b[i].size > fileByte(u.x.file_size_limit)) {
					u.fileQueueError(b[i], SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT);
					continue;
				} else if (u.x.minFileSize != null && b[i].size < fileByte(u.x.minFileSize)) {
					u.fileQueueError(b[i], SWFUpload.QUEUE_ERROR.FILE_MIN_SIZE_LIMIT);
					continue;
				} else if (t && ! $.idsAny(t, '.' + $.strFrom(b[i].name, '.', true).toLowerCase())) {
					u.fileQueueError(b[i], SWFUpload.QUEUE_ERROR.INVALID_FILETYPE);
					continue;
				} else if (u.x.on && u.x.on.fileselect && (r = u.triggerHandler('fileselect', b[i]))) {
					u.fileQueueError(b[i], SWFUpload.QUEUE_ERROR.INVALID_FILENAME, r);
					continue;
				} else {
					b[i].id = this.id + 'f-' + (fileCount ++);
					u._queues.push(b[i]);
					k ++;
				}
			}
			u.file_dialog_complete_handler(o.files.length, k, u._queues.length);
			this.reset();
		},
		reset: function() {
			if (! isSWF) {
				var self = this;
				setTimeout(function() {
					uploadCount ++;
					$.append(self.$(), self.html_append());
					self.fileID = self.id + 'u-' + uploadCount;
				}, 0);
			}
		},
		html_append: function() {
			return isSWF ? '<div class=w-upload-swf id=' + this.u.id + 'swf></div>' : '<input type=file style="visibility:hidden;position:absolute;width:0;height:0;" id=' + this.id + 'u-' + uploadCount + ' onchange=' + $.abbr + '.w(this).fileSelected(this) onclick=' + $.abbr + '.cancel(event)' +
				(this.u.x.file_upload_limit !== 1 ? ' multiple' : '') + ' accept="' + this.getAccept() + '">';
		}
	}
}),

ImageUploadUploadButton = define.widget('ImageUploadUploadButton', {
	Const: function(x, p) {
		ImageUploadButton.apply(this, arguments);
		this.fileID = this.id + 'u-' + uploadCount;
	},
	Default: {widthMinus: 2, heightMinus: 2},
	Extend: FileUploadUploadButton
}),

VideoUploadUploadButton = define.widget('VideoUploadUploadButton', {
	Extend: ImageUploadUploadButton
}),

// 图片模式显示value
ImageUploadValue = define.widget('ImageUploadValue', {
	Const: function(x, p) {
		this.u = p.u;
		W.apply(this, arguments);
		this.loading = false;
		this.loaded  = !!x.data;
	},
	Extend: Horz,
	Listener: {
		body: {
			ready: function() {
				this.x.file && this.createThumbnail();
			},
			click:  function() {
				if (this.x.data) {
					if (this.u.x.preview) {
						this.cmd(this.u.x.preview);
					} else if (this.u.x.download) {
						this.download();
					}
				}
			}
		}
	},
	Default: {width: 80, height: 80, widthMinus: 2, heightMinus: 2},
	Prototype: {
		className: 'w-imageupload-value',
		pubParent: function() {return this.u},
		// 预览本地图片
		createThumbnail: function() {
			var r = new FileReader(), self = this;
			r.onload = function(e) {self.$('g').src = e.target.result};
			r.readAsDataURL(this.x.file);
		},
		upload: function() {
			var d = new FormData(), f = this.x.file, u = this.u, r = $.ajaxXHR();
			d.append('Filedata', f);
			r.upload.addEventListener('progress', function(e) {
				if (e.lengthComputable)
					u.upload_progress_handler(f, e.loaded, e.total);
			}, false);
			r.addEventListener('load', function(e) {
				u.upload_success_handler(f, e.target.responseText);
				u.upload_complete_handler(f);
			}, false);
			r.addEventListener('error', function(e) {
				u.uploadError(f, e.error, e.error);
			}, false);
			this.loading = true;
			this.xhr     = r;
			r.open('post', $.urlComplete(u.x.upload_url));
			r.send(d);
		},
		download: function() {
			var s = this.u.x.download;
			s && $.download(this.formatStr(s, null, true));
		},
		preview: function(a) {
			if (! this.x.data)
				return;
			if (! a && ! this.x.data.url && ! this.x.data.thumbnail)
				a = this.u.x.thumbnail;
			for (var i = 0, b = this.parentNode, l = b.length, c = [], d = a && ! /^\$\w+$/.test(a), e; i < l; i ++) {
				if (b[i].x.data) {
					var f = (b[i].data('url') || b[i].data('thumbnail'));
					if (! f)
						f = a && b[i].formatStr(a, null, d);
					if (f) {
						c.push(f);
						if (b[i] === this) e = c.length -1;
					}
				}
			}
			c.length && $.previewImage(c, {current: e});
		},
		setProgress: function(a) {
			if (a < 50) {
				this.$('p-r').style.transform = 'rotate(' + (180 * a / 50) + 'deg)';
			} else {
				this.$('p').classList.add('z-2');
				this.$('p-r').style.transform = '';
				this.$('p-l').style.transform = 'rotate(' + (180 * (a - 50) / 50) + 'deg)';
			}
		},
		//@serverData -> 成功返回: {id: 'ID', name: '名称', size: '字节数', url: '地址', thumbnail: '缩略图地址'}, 失败返回: {error: true, text: '失败原因'}
		setSuccess: function(serverData) {
			this.loading = false;
			this.loaded  = true;
			this.removeQueue();
			delete this.x.file;
			this.x.data = serverData;
			this.u.addValue(serverData);
			this.removeClass('z-loading');
			//Q('._loading,._progress', this.$()).remove();
			this.render();
		},
		setError: function(errorCode, message) {
			this.loading = false;
			this.loaded  = true;
			this.error   = message;
			this.removeElem('p');
			this.removeClass('z-loading');
			this.addClass('z-err');
			Q('._loading,._g', this.$()).remove();
			this.removeQueue();
		},
		removeQueue: function() {
			if (this.x.file) {
				$.arrPop(this.u._queues, this.x.file);
				delete this.x.file;
			}
		},
		moreNodes: function() {
			var b = $.jsonClone(this.u.x.valueButtons), v = this.x.data;
			(function(d) {
				for (var i = 0; i < d.length; i ++) {
					d[i].data = v;
					d[i].nodes && arguments.callee(d[i].nodes);
				}
			})(b);
			return b;
		},
		more: function(a, e) {
			this.cmd({type: 'Menu', snap: {target: a}, nodes: this.moreNodes()});
			e && $.stop(e);
		},
		close: function(a, e) {
			this.remove();
			e && $.stop(e);
			this.x.data && this.u.trigger('change');
		},
		html_cvr: function() {
			return '<div class=_cvr></div>';
		},
		html_loading: function() {
			return '<div id=' + this.id + 'p class=f-circle-progress><div id=' + this.id + 'p-l class=_l></div><div id=' + this.id + 'p-r class=_r></div></div>'
		},
		html_nodes: function() {
			var u = this.u, f = this.x.file, v = this.x.data, m = '', w = this.innerWidth(), h = this.innerHeight(), c = u.x.thumbnail,
				s = ' style="max-width:' + w + 'px;max-height:' + h + 'px"' + ($.br.css3 ? '' : ' width=' + w + ' height=' + h);
			if (! f) {
				m = v.thumbnail;
				! m && (m = this.formatStr(c, null, ! /^\$\w+$/.test(c)));
			}
			m = $.urlComplete(m);
			return (this.x.file ? '<i class=f-vi></i><img id=' + this.id + 'g class=_g' + s + '>' + this.html_loading() + '<div class="_name f-omit" title="' + this.x.file.name + '">' + this.x.file.name + '</div>' :
				'<i class=f-vi></i><img id=' + this.id + 'g class=_g src="' + m + '"' + s + '>') + (f ? '' : this.html_cvr()) + (!f && u.x.valueButtons ? '<div class=_more onclick=' + evw + '.more(this,event)>' + $.caret('d') + '</div>' : '') + '<div class=_close onclick=' + evw + '.close(this,event)>&times;</div>';
		},
		html: function() {
			this.x.file && this.addClass('z-loading');
			return Horz.prototype.html.call(this);
		},
		remove: function() {
			var u = this.u;
			if (u.x.removesrc)
				this.cmd({type: 'Ajax', src: u.x.removesrc, error: false});
			if (this.x.data)
				u.removeValue(this.x.data);
			if (isSWF) {
				if (this.x.file)
					u.cancelUpload(this.x.file.id);
				u.uploadStats(-1);
			} else {
				if (this.xhr) {
					this.xhr.abort();
					delete this.xhr;
				}
				this.removeQueue();
			}
			W.prototype.remove.call(this);
		}
	}
}),

VideoUploadValue = define.widget('VideoUploadValue', {
	Extend: ImageUploadValue,
	Prototype: {
		className: 'w-videoupload-value',
		// 本地视频生成预览图
		createThumbnail: function() {
			var video = document.createElement('video'), self = this;
			video.addEventListener('loadedmetadata', function() {
			    setTimeout(function() {
			    	if (!self.x.file) return;
			    	var canvas = document.createElement('canvas');
			    	canvas.width = video.videoWidth;
			    	canvas.height = video.videoHeight;
			    	var ctx = canvas.getContext('2d');
			    	ctx.drawImage(video, 0, 0);
			    	self.$('g').src = canvas.toDataURL('image/png');
					video.pause();
					Q(video).remove();
			    }, 100);
			});
			video.src = URL.createObjectURL(this.x.file);
			video.muted = true;
			video.play();
		},
		html_cvr: function() {
			return '<div class=_cvr>' + $.image('.f-i-play', {cls: '_play'}) + '</div>';
		}
	}
}),

FileUploadValue = define.widget('FileUploadValue', {
	Extend: ImageUploadValue,
	Listener: {
		body: {
			ready: $.rt()
		}
	},
	Default: {width: -1, height: -1},
	Prototype: {
		pubParent: function() {
			return this.u;
		},
		className: 'w-fileupload-value',
		setProgress: function(a) {
			this.$('p').style.width = a + '%';
		},
		setError: function(errorCode, message) {
			this.loading = false;
			this.loaded  = true;
			this.error   = message;
			this.removeElem('g');
			$.classAdd(this.$(), 'z-err');
			$.classRemove(this.$(), 'z-loading');
			this.removeQueue();
		},
		html_nodes: function() {
			var u = this.u, c = u.x.valueButtons, f = this.x.file, t = f ? f.name : this.x.data.name;
			return (this.x.file ? '<div class=_progress id=' + this.id + 'g><div id=' + this.id + 'p class=_percent></div></div>' : '') +
				'<i class="_icon f-i ' + getIco(t) + '"></i><div class=_main>' + (c && !f ? $.image('.f-i-more', {cls: '_more', click: evw + '.more(this,event)'}) : '') + $.image('.f-i-trash', {cls: '_close', click: evw + '.close(this,event)'}) +
				'<div class="_name f-omit" title="' + t + '"><i class=f-vi></i><span class=f-va>' + t + '</span></div></div>';
		},
		html: function() {
			var u = this.u, pw = u.formWidth(), vw = u.scaleWidth(this, u.x.pub && u.x.pub.width), nw = 130, xw = 330, tw;
			if (pw) {
				xw = Math.min(xw, pw);
				nw = Math.min(xw, nw);
				if (vw != null)
					tw = vw;
				else if ($.br.ie7)
					tw = Math.min(pw, 200);
			}
			this.cssText = 'min-width:' + nw + 'px;max-width:' + (xw) + 'px;' + (tw != null ? 'width:' + (tw) + 'px;' : '');
			this.x.file && this.addClass('z-loading');
			return Horz.prototype.html.call(this);
		}
	}
}),

UploadValueButtonBar = define.widget('UploadValueButtonBar', {
	Extend: 'ButtonBar',
	Prototype: {
		x_childtype: $.rt('UploadValueButton')
	}
}),

UploadValueButton = define.widget('UploadValueButton', {
	Extend: 'Button',
	Listener: {
		body: {
			close: function(e) {
				if (! this._disposed) {
					$.stop(e);
					this.parentNode.parentNode.remove();
				}
			}
		}
	},
	Prototype: {
		close: function() {
			this.trigger('close');
		}
	}
});

/* helper */
var suffix = (function() {
	var n = {
		'0' : 'js.css.pdf.psd.ai.txt.eps.txt',
		xls : 'xls.xlsx',
		doc : 'doc.docx.rtf',
		ppt : 'ppt.pptx',
		htm : 'htm.html',
		zip : 'rar.zip.tar.jar.z.7z.ace.lzh.arj.gzip.bz2.cab.uue',
		fla : 'fla.swf',
		fon : 'eot.otf.fon.font.ttf.ttc.woff.woff2',
		iso : 'iso.nrg.mds.mdf.bin.cue.img.fcd.lcd.dmg',
		vid : 'avi.asf.wmv.avs.flv.mkv.mov.3gp.mp4.mpg.mpeg.dat.dsm.ogm.vob.rm.rmvb.ts.tp.ifo.nsv',
		aud : 'mp3.aac.wav.wma.cda.flac.m4a.mid.mka.mp2.mpa.mpc.ape.ofr.ogg.ra.wv.tta.ac3.dts.wv.shn.vqf.spc.nsf.adx.psf.minipsf.psf2.minipsf2.rsn.zst',
		img : 'jpg.jpeg.gif.png.bmp.tif.tiff'
	}, r = {};
	for (var k in n) {
		for (var i = 0, a = n[k].split('.'); i < a.length; i ++)
			r[a[i]] = k == '0' ? a[i] : k;
	}
	return r;
})(),
swfTranslate = {
	uploadLimit: 'file_upload_limit', maxFileSize: 'file_size_limit', fileTypes: 'file_types'
};
function getSuffix(url) {
	var a = $.strFrom(url, '.', true).toLowerCase();
	return suffix[a] || 'file';
};
function swfOptions(x) {
	for (var i in swfTranslate) {
		if (x[i] !== undefined) x[swfTranslate[i]] = x[i];
	}
	return x;
};
// 根据文件后缀名获取图标样式
function getIco(url) {
	return 'f-i-file-' + getSuffix(url);
};
// 把 "1MB" 转为字节
function fileByte(a) {
	var b;
	if (a != null && ! isNaN(a))
		a += 'KB';
	a = $.number(a.replace(/[a-z]+$/i, function($0) {b = $0.charAt(0).toUpperCase(); return ''}));
	return a * (b === 'K' ? 1024 : b === 'M' ? 1024 * 1024 : b === 'G' ? 1024 * 1024 * 1024 : b === 'T' ? 1024 * 1024 * 1024 * 1024 : 1);
}
function getFileType(a) {
	a = a.replace(/;/g, ',').replace(/\*/g, '').toLowerCase();
	if ($.br.ms)
		return a;
	for (var i = 0, b = a.split(','), l = b.length, c = [], d; i < l; i ++) {
		b[i] && c.push($.mimeType(b[i]) || b[i]);
	}
	return c.join(',');
}

module.exports = Upload;

