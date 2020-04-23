/**
 *@author lamontYu
 *
 *@since
 */
define.template({
    type: 'View',
    node: {
        type: 'Horizontal',
        nodes: [
            {
                type: 'Tabs',
                height: '*',
                width: '*',
                position: 'l',
                nodes: [
                    {
                        text: 'View',
                        target: {
                            type: 'View',
                            node: {
                                type: 'Html',
                                text: '这是View里的内容'
                            }
                        }
                    },
                    {
                        text: 'Section',
                        target: {
                            type: 'Section',
                            node: {
                                type: 'Html',
                                text: '这是Section里的内容'
                            }
                        }
                    },
                    {
                        text: 'Vertical',
                        target: {
                            type: 'Vertical',
                            // 多个子节点
                            nodes: [
                                {type: 'Html', height: 100, text: '第1块内容固定高度', style: 'background:#ffb527'},
                                {type: 'Html', height: '*', text: '第2块内容显示剩余高度', style: 'background:#fc942d'},
                                {type: 'Html', height: '20%', text: '第3块内容百分比高度', style: 'background:#579bfb'},
                                {type: 'Html', height: -1, text: '第4块内容自适应高度', style: 'background:#72b636'}
                            ]
                        }
                    },
                    {
                        text: 'Horizontal',
                        target: {
                            type: 'Horizontal',
                            // 多个子节点
                            nodes: [
                                {type: 'Html', width: 100, text: '第1块内容固定宽度', style: 'background:#ffb527'},
                                {type: 'Html', width: '*', text: '第2块内容显示剩余宽度', style: 'background:#fc942d'},
                                {type: 'Html', width: '20%', text: '第3块内容百分比宽度', style: 'background:#579bfb'},
                                {type: 'Html', width: -1, text: '第4块内容自适应宽度', style: 'background:#72b636'}
                            ]
                        }
                    },
                    {
                        text: 'Frame',
                        target: {
                            type: 'Vertical',
                            nodes: [
                                {
                                    type: 'ButtonBar',
                                    pub: {focusable: true},
                                    nodes: [
                                        {text: '显示第1块', target: 'frame_html1'},
                                        {text: '显示第2块', target: 'frame_html2'},
                                        {text: '显示第3块', target: 'frame_html3'},
                                        {text: '显示第4块', target: 'frame_html4'}
                                    ]
                                },
                                {
                                    type: 'Frame',
                                    height: '*',
                                    // style: 'background:#e9e9e9;',
                                    // 多个子节点
                                    nodes: [
                                        {type: 'Html', id: 'frame_html1', text: '这是第1块内容(Frame下的组件一般需要id)'},
                                        {type: 'Html', id: 'frame_html2', text: '这是第2块内容(Frame下的组件一般需要id)'},
                                        {type: 'Html', id: 'frame_html3', text: '这是第3块内容(Frame下的组件一般需要id)'},
                                        {type: 'Html', id: 'frame_html4', text: '这是第4块内容(Frame下的组件一般需要id)'}
                                    ]
                                }

                            ]
                        }
                    },
                    {
                        text: 'Tabs',
                        target: {
                            type: 'Tabs',
                            nodes: [
                                {
                                    text: 'tab1', target: {
                                        type: 'Html',
                                        text: '这是tab1的内容'
                                    }
                                },
                                {
                                    text: 'tab2', target: {
                                        type: 'Html',
                                        text: '这是tab2的内容'
                                    }
                                },
                                {
                                    text: 'tab3', target: {
                                        type: 'Html',
                                        text: '这是tab3的内容'
                                    }
                                }
                            ]
                        }
                    },
                    {
                        text: 'Table',
                        target: {
                            type: 'Table',
                            columns: [
                                {field: 'A', width: 40},
                                {field: 'B', width: '*'},
                                {field: 'C', width: 200}
                            ],
                            tHead: {
                                nodes: [
                                    {'A': '序号', 'B': '姓名', 'C': '性别'}
                                ]
                            },
                            tBody: {
                                nodes: [
                                    {'A': 1, 'B': '张三', 'C': '男'},
                                    {'A': 2, 'B': '李四', 'C': '女'},
                                    {'A': 3, 'B': '王五', 'C': '男'}
                                ]
                            }
                        }
                    },
                    {
                        text: 'Form',
                        target: {
                            type: 'Form',
                            nodes: [
                                {
                                    type: 'Text',
                                    name: 'username',
                                    label: {text: '用户名'}
                                },
                                {
                                    type: 'Password',
                                    name: 'password',
                                    label: {text: '密码'}
                                }
                            ]
                        }
                    },
                    {
                        text: 'Collapse',
                        target: {
                            type: 'Collapse',
                            focusMultiple: true,
                            nodes: [
                                {text: '标题1', target: {type: 'Html', text: '内容1'}},
                                {text: '标题2', target: {type: 'Html', text: '内容2'}}
                            ]
                        }
                    },
                    {
                        text: 'FieldSet',
                        target: {
                            type: 'Vertical',
                            style: 'padding:20px;',
                            widthMinus: 40,
                            heightMinus: 40,
                            nodes: [
                                {type: 'Radio', name: 'ipAddr', text: '自动获取ip地址', checked: true},
                                {
                                    type: 'FieldSet',
                                    box: {type: 'Radio', name: 'ipAddr', text: '使用下面的ip地址'},
                                    legend: '[这是legend内容]',
                                    nodes: [
                                        {type: 'Text', label: {text: 'ip地址'}},
                                        {type: 'Text', label: {text: '子网掩码'}},
                                        {type: 'Text', label: {text: '默认网关'}},
                                    ]
                                }
                            ]
                        }
                    }
                ]
            }
        ]
    }
});