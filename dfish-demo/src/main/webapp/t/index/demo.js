define.template({
    type: 'view',
    commands: {
        'submit': {
            type: 'submit',
            src: 'demo/submit',
            success: '$.alert("提交成功");'
        },
        'loading': {
            type: 'loading',
            node: {
                type: 'progress',
                guide: 'demo/loading',
                template: 'progress/multiple',
                success: 'if($response.data.finish){$.close(this);$.alert("完成了!");}'
            }
        }
    },
    node: {
        type: 'vertical', nodes: [
            {
                type: 'horizontal',
                height: '*',
                nodes: [
                    {type: 'html', width: '*'},
                    {
                        type: 'form',
                        width: 1200,
                        pub: {colspan: -1},
                        nodes: [
                            {
                                type: 'buttonbar',
                                nodes: [
                                    {type: 'submitbutton', text: '提交', on: {click: 'this.cmd("submit")'}},
                                    {text: '进度条', on: {click: 'this.cmd("loading");'}}
                                ]
                            },
                            {
                                type: 'upload/file',
                                name: 'fileJson',
                                label: {text: '附件上传'}
                            },
                            {
                                type: 'upload/image',
                                name: 'imageJson',
                                data: {'scheme': 'USER_ICON'},
                                label: {text: '图片上传'}
                            },
                            {
                                type: 'ueditor',
                                name: 'lobContent',
                                label: {text: '测试文本'}
                            },
                            {
                                colspan: -1, node: {
                                    type: 'jigsaw',
                                    label: {text: '验证码'}
                                }
                            }
                        ]
                    },
                    {type: 'html', width: '*'}
                ]
            },
            {
                type: 'horizontal',
                height: 50,
                style: 'margin-top:10px;',
                cls: 'bg-white',
                hmin: 10,
                nodes: [
                    {type: 'html', width: '*'},
                    {
                        type: 'html',
                        width: 1200,
                        align: 'center',
                        valign: 'middle',
                        text: '版权所有 福建榕基软件股份有限公司 ©2019'
                    },
                    {type: 'html', width: '*'}
                ]
            }
        ]
    }
});