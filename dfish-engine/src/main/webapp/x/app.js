window.app = {
    response: {
        error: function (response) {
            var error = response.error;
            var errorMsg = '';
            if (error.code) {
                errorMsg = '[' + error.code + ']';
            }
            if (error.message) {
                errorMsg += error.message;
            } else {
                errorMsg += '操作失败';
            }
            app.alert.warn(errorMsg);
        }
    },
    alert: {
        info: function (msg) {
            VM().cmd({type: 'Alert', cls: 'x-alert-info', text: msg});
        },
        warn: function (msg) {
            VM().cmd({type: 'Alert', cls: 'x-alert-warn', text: msg});
        }
    },
    url: {
        getLocationParamValue: function (key) {
            var params = this.getParams(location.search);
            return params[key];
        },
        getParams: function (locationSearch) {
            var params = {};
            if (locationSearch) {
                var splitIndex = locationSearch.indexOf('?');
                if (splitIndex >= 0) {
                    locationSearch = locationSearch.substring(splitIndex + 1);
                }
                var paramArray = locationSearch.split('&');
                for (var i = 0; i < paramArray.length; i++) {
                    var param = paramArray[i].split('=');
                    var key = param[0];
                    var value = param[1];
                    if (key == 'redirect') {
                        value = value.replace(/%3D/g, '=').replace(/%26/g, '&');
                    }
                    params[key] = value;
                }
            }
            return params;
        }
    }
};