dfish.use('skin/dfish.config.js');
dfish.use('skin/skin.config.js');
/**
 * app定义
 */
window.app = {
    idRoot: "00000000000000000000000000000000",
    error: function (response) {
        var error = response.error;
        var errorMsg = '';
        if (error.code) {
            if (error.code == 'LOGIN-00001') {
                var redirect = response.data;
                var url = "./login.jsp";
                if (redirect) {
                    url += "?redirect=" + redirect;
                }
                window.location.href = url;
                return;
            }
            errorMsg = '[' + error.code + ']';
        }
        if (error.msg) {
            errorMsg += error.msg;
        } else {
            errorMsg += "操作失败";
        }
        this.alertWarn(errorMsg);
    },
    alertWarn: function (msg) {
        $.alert(msg);
    },
    alertInfo: function (msg) {
        $.alert(msg, 5, 5000);
    },
    principal: {
        isSame: function(header, userId) {
            // userId不存在都当做是不同
            if (!userId || !header || !header.principal) {
                return false;
            }
            return userId == header.principal.name;
        }
    },
    dialog: {
        width: {
            "max": "*",
            "large": 980,
            "medium": 680,
            "small": 440
        },
        height: {
            "max": "*",
            "large": 550,
            "medium": 410,
            "small": 250
        },
        max: function (target) {
            var d = dfish.dialog(target);
            var isMax = d.isMax();
            target.attr('tip', isMax ? '最大化' : "还原");
            // 最大化/还原
            d.max();
        }
    },
    page: {
        getSumPage: function (header) {
            if (!header) {
                return 1;
            }
            var size = header.size || 0;
            var limit = header.limit || 1;
            var sumPage = size / limit;
            // 这里不做任何处理,因为框架默认有小数代表超过当前页,总页数会向上取整
            return sumPage;
        },
        getCurrentPage: function (header) {
            if (!header) {
                return 1;
            }
            var offset = header.offset || 0;
            var limit = header.limit || 1;
            var currentPage = (offset / limit) + 1;
            return currentPage;
        }
    },
    format: {
        pattern: {
            year: 'yyyy',
            month: 'yyyy-mm',
            date: 'yyyy-mm-dd',
            dateTimeMinute: 'yyyy-mm-dd hh:ii',
            dateTimeSecond: 'yyyy-mm-dd hh:ii:ss'
        },
        datetime: function (datetime) {
            if (datetime) {
                // 格式是yyyy-mm-dd hh:ii
                return datetime.substr(0, 16);
            }
            return datetime;
        },
        date: function (datetime) {
            if (datetime) {
                // 格式是yyyy-mm-dd
                return datetime.substr(0, 10);
            }
            return datetime;
        }
    },
    // 验证
    validate: {
        pattern: {
            email: "/^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$/",
            pwd: "/^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\x21-\x2e\x3a-\x40\x5b-\x60\x7b-\x7e])[\x21-\x7e]{8,20}$/",
            phone: "/^\\d{11}$/"
        },
        regExps: {},
        test: function (text, pattern) {
            var regExp = this.regExps.pattern;
            if (!regExp) {
                // 正则验证
                regExp = new RegExp(pattern);
                this.regExps[pattern] = regExp;
            }
            return regExp.test(text);
        }
    },
    options: {
        status: [{"value": "1", "text": "启用"}, {"value": "0", "text": "禁用"}],
        boolean: [{"value": "1", "text": "是"}, {"value": "0", "text": "否"}],
        getOptionText: function (options, value) {
            if (options && value) {
                for (var i = 0; i < options.length; i++) {
                    var option = options[i];
                    if (value == option.value) {
                        return option.text;
                    }
                }
            }
            return "";
        }
    }
};