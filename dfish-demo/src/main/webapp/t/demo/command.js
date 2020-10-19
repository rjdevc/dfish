/**
 *@author lamontYu
 *
 *@since DFish5.0
 */
define.template({
    type: 'View',
    commands: {
        'alert': {
            type: 'Alert',
            text: '这是alert',
            position: 'br'
        },
        'dialog': {
            type: 'Dialog',
            position: 'br',
            node: {
                type: 'Html',
                text: '弹窗位置'
            }
        },
        'afterSingle': {
            type: 'After',
            target: 'afterSingle',
            node: {
                type: 'Button',
                text: 'Single'
            }
        }
    },
    node: {
        type: 'Vertical',
        nodes: [
            {
                type: 'ButtonBar',
                nodes: [
                    {text: 'Ajax', on: {click: '$.alert("数据提交命令,需要和后台配合,这里暂不演示");'}},
                    {text: 'Submit', on: {click: '$.alert("数据提交命令,需要和后台配合,这里暂不演示");'}},
                    {text: 'JS', on: {click: '$.alert("这里调用的是JS方法,5秒后窗口将自动关闭",null,5000);'}},
                    {text: 'Dialog', on: {click: 'this.cmd("dialog");'}},
                    {text: 'Alert', on: {click: 'this.cmd("alert");'}},
                    {text: 'Alert2', on: {click: '$.alert("JS函数调用alert方法","br",5000);'}},
                    {text: 'Alert3', on: {click: 'this.cmd({type:"Alert",text:"支持非声明式命令调用"});'}},
                    {
                        text: 'Confirm', on: {
                            click: 'this.cmd({type:"Confirm",text:"这是确认命令,请点击[确定]和[取消]试试效果",' +
                                'yes:{type:"Alert",text:"您点击的是[确定]",timeout:5000},' +
                                'no:{type:"Alert",text:"您点击的是[取消]",timeout:5000}});'
                        }
                    },
                    {
                        text: 'Loading',
                        on: {click: 'this.cmd({type:"Loading",timeout:3000,node:{type:"ProgressLoader",nodes:[{text:"典型的场景就是进度条加载",percent:50}]}});'}
                    },
                    {text: 'Menu', on: {click: 'this.cmd({type:"Menu",nodes:[{text:"按钮1"},{text:"按钮2"}]});'}},
                    {text: 'Tip', on: {click: 'this.cmd({type:"Tip",text:"这是Tip"});'}}
                ]
            },
            {
                type: 'ButtonBar',
                nodes: [
                    {text: 'After:'},
                    {
                        text: 'After Single',
                        id: 'afterSingle',
                        on: {click: 'var count=this.data("count");if(!count){count=1;};this.cmd({type:"After",target:"afterSingle",node:{type:"Button",text:"Single"+(count++)}});this.data("count",count);'}
                    },
                    {
                        text: 'After Multiple',
                        id: 'afterMultiple',
                        on: {click: 'var count=this.data("count");if(!count){count=1;}this.cmd({type:"After",target:"afterMultiple",nodes:[{type:"Button",text:"Multiple"+(count++)},{type:"Button",text:"Multiple"+(count++)}]});this.data("count",count);'}
                    }
                ]
            },
            {
                type: 'ButtonBar',
                nodes: [
                    {text: 'Append:'},
                    {
                        text: 'Append Single',
                        id: 'appendSingle',
                        on: {click: 'var count=this.data("count");if(!count){count=1;}this.cmd({type:"Append",target:"appendSingle",node:{type:"Button",text:"Single"+(count++)}});this.data("count",count);this.data("count",count);'}
                    },
                    {
                        text: 'Append Multiple',
                        id: 'appendMultiple',
                        on: {click: 'var count=this.data("count");if(!count){count=1;}this.cmd({type:"Append",target:"appendMultiple",nodes:[{type:"Button",text:"Multiple"+(count++)},{type:"Button",text:"Multiple"+(count++)}]});this.data("count",count);'}
                    }
                ]
            },
            {
                type: 'ButtonBar',
                nodes: [
                    {text: 'Before:'},
                    {
                        text: 'Before Single',
                        id: 'beforeSingle',
                        on: {click: 'var count=this.data("count");if(!count){count=1;}this.cmd({type:"Before",target:"beforeSingle",node:{type:"Button",text:"Single"+(count++)}});this.data("count",count);'}
                    },
                    {
                        text: 'Before Multiple',
                        id: 'beforeMultiple',
                        on: {click: 'var count=this.data("count");if(!count){count=1;}this.cmd({type:"Before",target:"beforeMultiple",nodes:[{type:"Button",text:"Multiple"+(count++)},{type:"Button",text:"Multiple"+(count++)}]});this.data("count",count);'}
                    }
                ]
            },
            {
                type: 'ButtonBar',
                nodes: [
                    {text: 'Prepend:'},
                    {
                        text: 'Prepend Single',
                        id: 'prependSingle',
                        on: {click: 'var count=this.data("count");if(!count){count=1;}this.cmd({type:"Prepend",target:"prependSingle",node:{type:"Button",text:"Single"+(count++)}});this.data("count",count);'}
                    },
                    {
                        text: 'Prepend Multiple',
                        id: 'prependMultiple',
                        on: {click: 'var count=this.data("count");if(!count){count=1;}this.cmd({type:"Prepend",target:"prependMultiple",nodes:[{type:"Button",text:"Multiple"+(count++)},{type:"Button",text:"Multiple"+(count++)}]});this.data("count",count);'}
                    }
                ]
            },
            {
                type: 'ButtonBar',
                nodes: [
                    {text: 'Replace:'},
                    {text: '目标按钮', id: 'replace', tip: '点击右边按钮展示效果'},
                    {
                        text: 'Replace1',
                        on: {click: 'this.cmd({type:"Replace",target:"replace",node:{type:"Button",text:"点击右边按钮展示效果1",id:"replace"}});'}
                    },
                    {
                        text: 'Replace2',
                        on: {click: 'this.cmd({type:"Replace",target:"replace",node:{type:"Button",text:"点击右边按钮展示效果2",id:"replace"}});'}
                    },
                    {
                        text: 'Replace3',
                        on: {click: 'this.cmd({type:"Replace",target:"replace",node:{type:"Button",text:"点击右边按钮展示效果3",id:"replace"}});'}
                    }
                ]
            },
            {
                type: 'ButtonBar',
                nodes: [
                    {text: 'Remove:'},
                    {text: '指定Remove1消失', on: {click: 'this.cmd({type:"Remove",target:"remove1"});'}},
                    {text: 'Remove1', id: 'remove1'},
                    {text: '点击自我消失', on: {click: 'this.remove();'}},
                    {text: '后面按钮消失', on: {click: 'if(this.next()){this.next().remove();}'}},
                    {text: 'Remove3'},
                    {text: 'Remove4'},
                    {text: 'Remove5'},
                    {text: 'Remove6'},
                    {text: 'Remove7'},
                    {text: 'Remove8'}
                ]
            }
        ]
    }
});