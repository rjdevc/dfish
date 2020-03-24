define.template({
    type: 'View',
    commands: {
        'submit': {
            type: 'submit',
            src: 'demo/submit',
            success: '$.alert("提交成功",null,5000);'
        },
        'loading': {
            type: 'loading',
            node: {
                type: 'progress',
                guide: 'demo/loading',
                template: 'progress/multiple',
                success: 'if($response.data.finish){$.close(this);$.alert("完成了!",null,5000);}'
            }
        }
    },
    node: {
        type: 'Tabs',
        cls: 'bg-white',
        position: 'b',
        nodes: [
            {
                text: '布局类',
                target: {
                    type: 'View',
                    template: 'demo/layout'
                }
            },
            {
                text: '表格类',
                target: {
                    type: 'View',
                    template: 'demo/table'
                }
            },
            {
                text: '表单类',
                target: {
                    type: 'View',
                    template: 'demo/form'
                }
            },
            {
                text: '命令类',
                target: {
                    type: 'View',
                    template: 'demo/command'
                }
            },
            {
                text: '功能类',
                target: {
                    type: 'View',
                    template: 'demo/widget'
                }
            },
            {
                text: '高级功能',
                target: {
                    type: 'View',
                    template: 'demo/adv'
                }
            }
        ]
    }
});