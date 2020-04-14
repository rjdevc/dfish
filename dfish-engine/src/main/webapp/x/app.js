window.app = {
    id: {
        root: '00000000000000000000000000000000'
    },
    response: {
        error: function (response) {
            var error = response.error;
            var errorMsg = '';
            if (error.code) {
                errorMsg = '[' + error.code + ']';
            }
            if (error.msg) {
                errorMsg += error.msg;
            } else {
                errorMsg += '操作失败';
            }
            app.alert.warn(errorMsg);
        }
    },
    alert: {
        info: function (msg) {
            // $.alert(msg, 'br', 5000);
            VM().cmd({type: 'Alert', cls: 'x-alert-info', text: msg});
        },
        warn: function (msg) {
            // $.alert(msg);
            VM().cmd({type: 'Alert', cls: 'x-alert-warn', text: msg});
        }
    },
    dialog: {
        width: {
            'max': '*',
            'large': 980,
            'medium': 680,
            'small': 440
        },
        height: {
            'max': '*',
            'large': 550,
            'medium': 410,
            'small': 250
        }
        // max: function (target) {
        //     var d = dfish.dialog(target);
        //     var isMax = d.isMax();
        //     target.attr('tip', isMax ? '最大化' : '还原');
        //     // 最大化/还原
        //     d.max();
        // }
    },
    // page: {
    //     getSumPage: function (header) {
    //         if (!header) {
    //             return 1;
    //         }
    //         var size = header.size || 0;
    //         var limit = header.limit || 1;
    //         var sumPage = size / limit;
    //         // 这里不做任何处理,因为框架默认有小数代表超过当前页,总页数会向上取整
    //         return sumPage;
    //     },
    //     getCurrentPage: function (header) {
    //         if (!header) {
    //             return 1;
    //         }
    //         var offset = header.offset || 0;
    //         var limit = header.limit || 1;
    //         var currentPage = (offset / limit) + 1;
    //         return currentPage;
    //     }
    // },
    format: {
        pattern: {
            year: 'yyyy',
            month: 'yyyy-mm',
            date: 'yyyy-mm-dd',
            dateTimeMinute: 'yyyy-mm-dd hh:ii',
            dateTimeSecond: 'yyyy-mm-dd hh:ii:ss'
        },
        dateTime: function(dateTime) {
            if (dateTime) {
                // 格式是yyyy-mm-dd hh:ii
                return dateTime.substr(0, 16);
            }
            return dateTime;
        },
        date: function(dateTime) {
            if (dateTime) {
                // 格式是yyyy-mm-dd
                return dateTime.substr(0, 10);
            }
            return dateTime;
        }
    },
    options: {
        status: [{'value': '1', 'text': '启用'}, {'value': '0', 'text': '禁用'}],
        boolean: [{'value': '1', 'text': '是'}, {'value': '0', 'text': '否'}]
    }
};