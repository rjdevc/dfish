define.template({
    type: 'View',
    commands: {
        'dialog': {
            type: 'Dialog',
            preload: 'g/form',
            node: {
                type: 'View',
                commands: {
                    // 这里为了演示方便直接用JS命令,一般使用Submit
                    'yes': {
                        type: 'JS',
                        text: '$.alert("您输入的姓名是:"+VM(this).fv("userName"));'
                    },
                    'no': {type: 'JS', text: '$.close(this);'}
                },
                // 这里的页面是静态页面,所以看不出对话框预加载的效果
                node: {
                    type: 'Vertical',
                    // 用于同一id组件替换时,这里高度要写,不写将按照默认高度替换(-1)
                    // height: '*',
                    nodes: [
                        {
                            type: 'Form',
                            id: 'dlg_body',
                            height: '*',
                            nodes: [
                                {type: 'Text', name: 'userName', label: {text: '姓名'}},
                                {type: 'DatePicker', name: 'birthday', label: {text: '生日'}}
                            ]
                        },
                        {type: 'Html', id: 'dlg_foot_info', width: '*', vAlign: 'middle', text: '这里可以写一些备注说明'},
                        // 这里补充业务定制的按钮栏
                        {
                            type: 'ButtonBar', id: 'dlg_foot_operation', align: 'right', height: 50,
                            nodes: [
                                {text: '重置密码'},
                                {type: 'SubmitButton', text: '保存'},
                                {text: '关闭'}
                            ]
                        }
                    ]
                }
            }
        }
    },
    node: {
        type: 'ButtonBar',
        height: -1,
        nodes: [
            {text: '打开演示对话框', on: {click: 'this.cmd("dialog");'}}
        ]
    }
});
//
// minus的使用
// {
//     type: 'View',
//         node: {
//     type: 'Vertical',
//         nodes: [
//         {
//             type: 'Html',
//             text: '演示widthMinus和heightMinus区域',
//             cls: 'minus',
//             // style: 'margin:10px;padding:20px;border:1px solid #ddd;',
//             widthMinus: 0,
//             heightMinus: 0,
//             width: 400,
//             height: 200
//         }
//     ]
// }
// }

// 进度调样式
// {
//     type: 'View',
//         node: {
//     type: 'ProgressLoader',
//         height: -1,
//         // 多了range设置
//         pub: {range: '25,50,75,100'},
//     nodes: [
//         {percent: 10},
//         {percent: 30},
//         {percent: 50},
//         {percent: 70},
//         {percent: 90},
//         {percent: 100}
//     ]
// }
// }

// 全局配置演示
// {
//     type: 'View',
//         commands: {
//     'default': {
//         type: 'Dialog', node: {type: 'View', node: {type: 'Html', text: '默认'}}
//     },
//     'large': {
//         // 大窗口
//         type: 'Dialog', cls: 'dlg-large', node: {type: 'View', node: {type: 'Html', text: 'large'}}
//     },
//     'picker': {
//         // 选择窗口(底部按钮不同)
//         type: 'Dialog', cls: 'dlg-picker', node: {type: 'View', node: {type: 'Html', text: 'picker'}}
//     },
//     'small_picker': {
//         type: 'Dialog',
//             // 小窗口+选择窗口
//             cls: 'dlg-small dlg-picker',
//             node: {type: 'View', node: {type: 'Html', text: 'small_picker'}}
//     }
// },
//     node: {
//         type: 'ButtonBar',
//             height: -1,
//             nodes: [
//             {text: '默认Dialog', on: {click: 'this.cmd("default")'}},
//             {text: 'large', on: {click: 'this.cmd("large")'}},
//             {text: 'picker', on: {click: 'this.cmd("picker")'}},
//             {text: 'small_picker', on: {click: 'this.cmd("small_picker")'}}
//         ]
//     }
// }


/*
{
    type: 'View',
    commands: {
        'default': {
            type: 'Dialog', node: {type: 'View', node: {type: 'Html', text: '默认'}}
        },
        'large': {
            // 大窗口
            type: 'Dialog', cls: 'dlg-large', node: {type: 'View', node: {type: 'Html', text: 'large'}}
        },
        'picker': {
            // 选择窗口(底部按钮不同)
            type: 'Dialog', cls: 'dlg-picker', node: {type: 'View', node: {type: 'Html', text: 'picker'}}
        },
        'small_picker': {
            type: 'Dialog',
            // 小窗口+选择窗口
            cls: 'dlg-small dlg-picker',
            node: {type: 'View', node: {type: 'Html', text: 'small_picker'}}
        }
    },
    node: {
        type: 'Table',
        columns: [
            {field: 'A'},
            {field: 'B'},
            {field: 'C'},
            {
                field: 'D',
                width: 90,
                align: 'center',
                format: '<a href="javascript:;" onclick="$.alert(\'$B\');">提示班级</a>'
            }
        ],
        pub: {on: {click: '$.alert($A);'}},
        tHead: {
            nodes: [
                {'A': '姓名', 'B': '班级', 'C': '成绩', 'D': '操作'}
            ]
        },
        tBody: {
            nodes: [
                {'A': '张三', 'B': '一班', 'C': 80},
                {'A': '李四', 'B': '一班', 'C': 100},
                {'A': '王五', 'B': '二班', 'C': 90}
            ]
        }
    }
}
* */

// $.stop()的使用
// {
//     type: 'View',
//         node: {
//     type: 'Tree',
//         pub: {
//         expanded: true,
//             format: '$text<i class="i-operation-edit x-operation" onclick="console.log(\'text:$text\');$.stop();"></i>',
//             on:{click: 'console.log("id:"+$id);'}
//     },
//     nodes: [
//         {
//             text: '根节点',
//             nodes: [
//                 {id: '0101', text: '一级节点1'},
//                 {id: '0102', text: '一级节点2'},
//                 {id: '0103', text: '一级节点3'}
//             ]
//         }
//     ]
// }
// }

// dialog装载
