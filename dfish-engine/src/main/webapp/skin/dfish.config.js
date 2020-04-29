dfish.config({
    // 调试模式
    debug: true,
    // 模板目录
    templateDir: 't/',
    // 预装载目录
    preloadDir: 'p/',
    // 皮肤
    skin: {
        // 样式目录
        dir: 'skin/',
        // 主题风格
        theme: 'classic',
        // 色调
        color: 'blue'
    },
    //表单验证效果 可选值: red,tip,alert
    validate: {
        effect: 'red,tip'
    },
    // 所有表单提示信息按照系统规则显示,比如:请输入XXX
    autoPlaceholder: true,
    // 每个 widget 类都可以定义默认属性，以 widget type 作为 key
    defaultOptions: {
        'Alert': {preload: 'g/alert'},
        'Alert.x-alert-info': {position: 'br', timeout: 3000},
        'Alert.x-alert-warn': {icon: '.f-i-warning'},
        'Confirm': {preload: 'g/alert'},
        'Dialog': {preload: 'g/std', width: 680, height: 410, error: "this.close();"},
        'Dialog.dlg-large': {width: 980, height: 550},
        'Dialog.dlg-medium': {width: 680, height: 410},
        'Dialog.dlg-small': {width: 440, height: 250},
        'Dialog.dlg-alert': {preload: 'g/alert'},
        'Dialog.dlg-std': {preload: 'g/std'},
        'Dialog.dlg-fixed': {preload: 'g/fixed'},
        'Dialog.dlg-form': {preload: 'g/form'},
        'Dialog.dlg-picker': {preload: 'g/picker'},
        'Dialog.dlg-none': {preload: 'g/none'},

        'Tree': {ellipsis: true},
        'Leaf': {tip: true, style: 'padding-right:10px;', widthMinus: 10},
        'Blank': {width: '*', height: '*'},

        'ButtonBar': {space: 10 },
        'ButtonBar.dlg-head-operation': {space: 0},
        'ButtonBar.x-breadcrumb': {space: 0},
        'ButtonBar.x-face-text': {space: 0},
        'Button': {tip: true, widthMinus: 2, heightMinus: 2},
        'SubmitButton': {tip: true, widthMinus: 2, heightMinus: 2},
        // 'Tabs': {widthMinus: 2, heightMinus: 2},
        // 'TabBar': {widthMinus: 20},
        // 'TabBar.z-position-left': {widthMinus: 0, heightMinus: 20},
        // 'TabBar.z-position-right': {widthMinus: 0, heightMinus: 20},

        // 'ButtonBar.w-tabbar': {space: 0, widthMinus: 20},
        // 'ButtonBar.w-tabbar.z-position-left': {widthMinus: 0, heightMinus: 20},
        // 'ButtonBar.w-tabbar.z-position-right': {widthMinus: 0, heightMinus: 20},

        'Table': {face: "none"},
        'TBody': {br: false, pub: {focusable: true}},
        'Column': {tip: true},
        'PageBar': {align: 'right', buttonCount: 5},
        // 'PageBar.z-normal': {align: 'right', buttonCount: 5, widthMinus: 40, name: 'currentPage'},
        // 'PageBar.z-mini': {align: 'right', buttonCount: 5, widthMinus: 40, name: 'currentPage'},
        // 'PageBar.z-simple': {align: 'right', buttonCount: 5, widthMinus: 40, name: 'currentPage'},

        'Form': {pub: {colSpan: -1}},
        'Form.x-form1': {pub: {colSpan: -1}},
        'Form.x-form2': {pub: {colSpan: 6}},
        'Form.x-form3': {pub: {colSpan: 4}},
        'Form.x-form4': {pub: {colSpan: 3}},

        'Label': {width: 120},
        'Label.z-type-password': {vAlign: 'top'},
        'Label.z-type-textarea': {vAlign: 'top'},
        'Label.z-type-fileupload': {vAlign: 'top'},

        'Textarea': {height: 100},
        'DatePicker': {format: 'yyyy-MM-dd'},
        'Spinner': {step: 10, decimal: 0, validate: {minValue: 1, maxValue: 9999999999999999}},
        // 'Combo': {keepShow: true},
        'Slider': {tip: true},
        'Jigsaw': {
            img: {type: 'JigsawImg', template: 'g/jigsaw/img'},
            auth: {type: 'JigsawAuth', template: 'g/jigsaw/auth'}
        },
        'FileUpload': {
            minFileSize: '1B',
            maxFileSize: '10M',
            data: {'scheme': 'DEFAULT'},
            post: {type: 'UploadPost', template: 'g/upload/post'},
            download: 'file/download?fileId=$id&scheme=$scheme',
            fileTypes: '*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.jpg;*.gif;*.png;*.vsd;*.txt;*.rtf;*.pdf;*.wps;',
            uploadButtons: [{type: 'UploadButton', text: '选择文件', icon: '.f-i-upload'}]
        },
        'ImageUpload': {
            minFileSize: '1B',
            maxFileSize: '10M',
            data: {'scheme': 'DEFAULT'},
            post: {type: 'UploadPost', src: 'file/upload/image?scheme=$scheme', template: 'g/upload/post'},
            download: 'file/download?fileId=$id&scheme=$scheme',
            thumbnail: 'file/thumbnail?fileId=$id&scheme=$scheme',
            preview: {type: 'JS', text: '$.previewImage("file/thumbnail?fileId="+$id+"&scheme="+$scheme);'},
            fileTypes: '*.png;*.jpg;*.jpeg;',
            uploadButtons: [{type: 'UploadButton', icon: '.f-i-upload-image'}]
        },

        'Toggle': {
            height: 40,
            collapsedIcon: '.f-i-plus',
            expandedIcon: '.f-i-minus',
            cls: 'bd-split bd-onlybottom',
            heightMinus: 1
        },
        'Toggle.z-hr': {
            cls: null,
            heightMinus: 0
        },
        'TableToggle.z-form': {
            height: 40,
            collapsedIcon: '.f-i-plus',
            expandedIcon: '.f-i-minus',
            cls: 'bd-split bd-onlybottom',
            heightMinus: 1
        },
        'TableToggle.z-form.z-hr': {
            cls: null,
            heightMinus: 0
        },
        'UEditor': {
            height: 300
        }
    },
    // 自定义widget
    alias: {
        'AMap': 'pl/amap/amap.dfish.js',
        'AMapPicker': 'pl/amap/amap.dfish.js',
        'ECharts': 'pl/echarts/echarts.dfish.js',
        'UEditor': 'pl/ueditor/ueditor.dfish.js',
        'UEditorHtml': 'pl/ueditor/ueditorhtml.dfish.js',
        'FlowPlayer': 'pl/flowplayer/flowplayer.dfish.js',
        'Carousel': 'pl/carousel/carousel.dfish.js'
    },
    ajaxFilter: function (response) {
        var hasError = response.error;
        if (hasError) {
            app.response.error(response);
        } else {
            if (!response.data) {
                response.data = {};
            }
        }
        return !hasError;
    },
    // 一个汉字算3个字节
    cnBytes: 3
});