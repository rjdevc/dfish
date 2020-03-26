/**
 *@author lamontYu
 *@date 2019-12-27
 *@since
 */
define.template({
    type: 'View',
    node: {
        type: 'Tabs',
        position: 'l',
        nodes: [
            {
                text: '简单表格', target: {
                    type: 'Table',
                    face: 'cell',
                    // 需要显示的列
                    columns: [
                        // 序号
                        {
                            type: 'column', // 这里可省略不写
                            field: 'rn',
                            width: 40,
                            align: 'center',
                            format: 'javascript:return {type: "TableRowNum"}'
                        },
                        {
                            type: 'column', // 这里可省略不写
                            field: 'userName', width: '*', tip: true
                        },
                        {field: 'gender', width: 80, align: 'center'},
                        {field: 'nation', width: 100, tip: true},
                        {field: 'score', width: 100, align: 'right'}
                    ],
                    // 表头,不写即无表头
                    tHead: {
                        // 行数,支持多行
                        nodes: [
                            // 属性名和列中的field名字一一对应,这样才能显示
                            {'rn': '序号', 'userName': '姓名', 'gender': '性别', 'nation': '民族', 'score': '得分'}
                        ]
                    },
                    // 表体,需要显示的数据项
                    tBody: {
                        // 行数,支持多行
                        nodes: [
                            // 属性名和列中的field名字一一对应,这样才能显示;允许有额外的属性用于隐藏值使用
                            {'userId': '03', 'userName': '张三', 'gender': '男', 'nation': '汉族', 'score': 30},
                            {'userId': '04', 'userName': '李四', 'gender': '女', 'nation': '满族', 'score': 40},
                            {'userId': '05', 'userName': '王五', 'gender': '男', 'nation': '回族', 'score': 50},
                            {'userId': '06', 'userName': '赵六', 'gender': '男', 'nation': '维吾尔族', 'score': 60}
                        ]
                    }
                }
            },
            {
                text: '复杂表格', target: {
                    type: 'Table',
                    face: 'cell',
                    // 需要显示的列
                    columns: [
                        {
                            field: 'rn',
                            width: 40,
                            align: 'center',
                            format: 'javascript:return {type: "TableRowNum"}'
                        },
                        {field: 'userName', width: 200, tip: true},
                        {field: 'subject', width: 100, align: 'center'},
                        {field: 'score', width: 100, align: 'right'}
                    ],
                    // 表头,不写即无表头
                    tHead: {
                        // 行数,支持多行
                        nodes: [
                            // 属性名和列中的field名字一一对应,这样才能显示
                            {'rn': '序号', 'userName': '姓名', 'subject': '学科', 'score': '得分'}
                        ]
                    },
                    // 表体,需要显示的数据项
                    tBody: {
                        // 行数,支持多行
                        nodes: [
                            // 属性名和列中的field名字一一对应,这样才能显示;允许有额外的属性用于隐藏值使用
                            {
                                type: 'TR', // 这里可省略不写
                                data: { // data是完整格式,可简写
                                    'userName': {
                                        type: 'TD', // 这里可省略不写
                                        rowSpan: 3, node: {type: 'Html', text: '张三'} // 可以是任意组件
                                    },
                                    'subject': '语文',
                                    'score': 31
                                }
                            },
                            {data: {'subject': '数学', 'score': 32}},
                            {data: {'subject': '英语', 'score': 33}},
                            {
                                'userName': {rowSpan: 3, text: '李四'},
                                'subject': '语文',
                                'score': 41
                            },
                            {'subject': '数学', 'score': 42},
                            {'subject': '英语', 'score': 43},
                            {
                                'userName': {rowSpan: 3, text: '王五'},
                                'subject': '语文',
                                'score': 51
                            },
                            {'subject': '数学', 'score': 52},
                            {'subject': '英语', 'score': 53},
                            {
                                'userName': {rowSpan: 3, text: '赵六'},
                                'subject': '语文',
                                'score': 61
                            },
                            {'subject': '数学', 'score': 62},
                            {'subject': '英语', 'score': 63}
                        ]
                    }
                }
            },
            {
                text: '分组表格',
                target: {
                    type: 'Table',
                    face: 'cell',
                    // 需要显示的列
                    columns: [
                        {field: 'userName', width: '*', tip: true},
                        {field: 'subject', width: 100, align: 'center'},
                        {field: 'score', width: 100, align: 'right'}
                    ],
                    // 表头,不写即无表头
                    tHead: {
                        // 行数,支持多行
                        nodes: [
                            // 属性名和列中的field名字一一对应,这样才能显示
                            {'userName': '姓名', 'subject': '学科', 'score': '得分'}
                        ]
                    },
                    // 表体,需要显示的数据项
                    tBody: {
                        // 行数,支持多行
                        nodes: [
                            {
                                data: {
                                    'userName': {type: 'TD', node: {type: 'TableLeaf', text: '1班'}}
                                },
                                nodes: [
                                    {
                                        'userName': {
                                            rowSpan: 3, node: {type: 'TableLeaf', text: '张三'}
                                        },
                                        'subject': '语文',
                                        'score': 31
                                    },
                                    {'subject': '数学', 'score': 32},
                                    {'subject': '英语', 'score': 33},
                                    {
                                        'userName': {rowSpan: 3, node: {type: 'TableLeaf', text: '李四'}},
                                        'subject': '语文',
                                        'score': 41
                                    },
                                    {'subject': '数学', 'score': 42},
                                    {'subject': '英语', 'score': 43},
                                    {
                                        'userName': {rowSpan: 3, node: {type: 'TableLeaf', text: '王五'}},
                                        'subject': '语文',
                                        'score': 51
                                    },
                                    {'subject': '数学', 'score': 52},
                                    {'subject': '英语', 'score': 53}
                                ]
                            },
                            {
                                data: {
                                    'userName': {type: 'TD', node: {type: 'TableLeaf', text: '2班'}}
                                },
                                nodes: [
                                    {
                                        'userName': {rowSpan: 3, node: {type: 'TableLeaf', text: '赵六'}},
                                        'subject': '语文',
                                        'score': 61
                                    },
                                    {'subject': '数学', 'score': 62},
                                    {'subject': '英语', 'score': 63}
                                ]
                            }
                        ]
                    }
                }
            },
            {
                text: '格式化表格',
                target: {
                    type: 'Table',
                    face: 'cell',
                    // 需要显示的列
                    columns: [
                        {
                            field: 'rn',
                            width: 40,
                            align: 'center',
                            format: 'javascript:return {type: "TableRowNum"}'
                        },
                        {field: 'userName', width: '*', tip: true},
                        {field: 'gender', width: 80, align: 'center'},
                        {field: 'nation', width: 100, tip: true},
                        {field: 'score', width: 100, align: 'right'},
                        {
                            field: 'operation',
                            width: 90,
                            align: 'center',
                            format: '<a href=javascript:; onclick=\'$.alert("您要查看的记录编号是:$userId;姓名是:$userName");\'>查看</a>' +
                                '&nbsp;&nbsp;<a href=javascript:; onclick=\'$.widget(this).closest("TR").remove();\'>删除</a>'
                        }
                    ],
                    // 表头,不写即无表头
                    tHead: {
                        // 行数,支持多行
                        nodes: [
                            // 属性名和列中的field名字一一对应,这样才能显示
                            {
                                'rn': '序号',
                                'userName': '姓名',
                                'gender': '性别',
                                'nation': '民族',
                                'score': '得分',
                                'operation': '操作'
                            }
                        ]
                    },
                    // 表体,需要显示的数据项
                    tBody: {
                        // 行数,支持多行
                        nodes: [
                            // 属性名和列中的field名字一一对应,这样才能显示;允许有额外的属性用于隐藏值使用
                            {'userId': '03', 'userName': '张三', 'gender': '男', 'nation': '汉族', 'score': 30},
                            {'userId': '04', 'userName': '李四', 'gender': '女', 'nation': '满族', 'score': 40},
                            {'userId': '05', 'userName': '王五', 'gender': '男', 'nation': '回族', 'score': 50},
                            {'userId': '06', 'userName': '赵六', 'gender': '男', 'nation': '维吾尔族', 'score': 60}
                        ]
                    }
                }
            },
            {
                text: '固定列表格',
                target: {
                    type: 'Table',
                    face: 'cell',
                    // 需要显示的列
                    columns: [
                        {
                            field: 'rn',
                            width: 40,
                            align: 'center',
                            format: 'javascript:return {type: "TableRowNum"}',
                            fixed: 'left'
                        },
                        {field: 'userName', width: 120, fixed: 'left'},
                        {field: 'address', width: 1600, tip: true},
                        {field: 'gender', width: 80, align: 'center'},
                        {field: 'nation', width: 100, tip: true},
                        {field: 'score', width: 100, align: 'right'},
                        {
                            field: 'operation',
                            width: 90,
                            align: 'center',
                            fixed: 'right',
                            format: '<a href=javascript:; onclick=\'$.alert("您要查看的记录编号是:$userId;姓名是:$userName");\'>查看</a>' +
                                '&nbsp;&nbsp;<a href=javascript:; onclick=\'$.widget(this).closest("TR").remove();\'>删除</a>'
                        }
                    ],
                    // 表头,不写即无表头
                    tHead: {
                        // 行数,支持多行
                        nodes: [
                            // 属性名和列中的field名字一一对应,这样才能显示
                            {
                                'rn': '序号',
                                'userName': '姓名',
                                'address': '公司住址',
                                'gender': '性别',
                                'nation': '民族',
                                'score': '得分',
                                'operation': '操作'
                            }
                        ]
                    },
                    // 表体,需要显示的数据项
                    tBody: {
                        // 行数,支持多行
                        nodes: [
                            // 属性名和列中的field名字一一对应,这样才能显示;允许有额外的属性用于隐藏值使用
                            {
                                'userId': '03',
                                'userName': '张三',
                                'address': '福建省福州市鼓楼区软件园A区15号,听说要看效果,列的总宽度要设置很长很长很长很长很长很长很长很长很长很长',
                                'gender': '男',
                                'nation': '汉族',
                                'score': 30
                            },
                            {
                                'userId': '04',
                                'userName': '李四',
                                'address': '福建省福州市鼓楼区软件园A区15号,听说要看效果,列的总宽度要设置很长很长很长很长很长很长很长很长很长很长',
                                'gender': '女',
                                'nation': '满族',
                                'score': 40
                            },
                            {
                                'userId': '05',
                                'userName': '王五',
                                'address': '福建省福州市鼓楼区软件园A区15号,听说要看效果,列的总宽度要设置很长很长很长很长很长很长很长很长很长很长',
                                'gender': '男',
                                'nation': '回族',
                                'score': 50
                            },
                            {
                                'userId': '06',
                                'userName': '赵六',
                                'address': '福建省福州市鼓楼区软件园A区15号,听说要看效果,列的总宽度要设置很长很长很长很长很长很长很长很长很长很长',
                                'gender': '男',
                                'nation': '维吾尔族',
                                'score': 60
                            }
                        ]
                    }
                }
            },
            {
                text: '设置表尾', target: {
                    type: 'Table',
                    face: 'cell',
                    // 需要显示的列
                    columns: [
                        {
                            field: 'rn',
                            width: 40,
                            align: 'center',
                            format: 'javascript:return {type: "TableRowNum"}'
                        },
                        {field: 'city', width: 100, tip: true, align: 'right'},
                        {field: 'gdp', width: '*', tip: true}
                    ],
                    // 表头,不写即无表头
                    tHead: {
                        // 行数,支持多行
                        nodes: [
                            // 属性名和列中的field名字一一对应,这样才能显示
                            {'rn': '序号', 'city': '城市', 'gdp': '2018年GDP(亿元)'}
                        ]
                    },
                    // 表体,需要显示的数据项
                    tBody: {
                        // 行数,支持多行
                        nodes: [
                            {'city': '泉州', 'gdp': 8467.98},
                            {'city': '福州', 'gdp': 7856.81},
                            {'city': '厦门', 'gdp': 4791.41},
                            {'city': '漳州', 'gdp': 3947.63},
                            {'city': '龙岩', 'gdp': 2393.30},
                            {'city': '三明', 'gdp': 2353.72},
                            {'city': '莆田', 'gdp': 2242.41},
                            {'city': '宁德', 'gdp': 1942.80},
                            {'city': '南平', 'gdp': 1792.51}
                        ]
                    },
                    tFoot: {
                        nodes: [
                            {
                                'rn': {
                                    colSpan: 2,
                                    align: 'right',
                                    text: '合计'
                                },
                                'gdp': 35804.04
                            }
                        ]
                    }
                }
            },
            {
                text: '分页',
                target: {
                    type: 'Vertical',
                    nodes: [
                        {
                            type: 'PageBar',
                            face: 'normal',
                            buttonCount: 5,
                            currentPage: 2,
                            jump: true,
                            sumPage: 10,
                            keyJump: true,
                            src: 'javascript:$.alert($0);',
                            prependContent: '默认样式:&nbsp;&nbsp;'
                        },
                        {
                            type: 'PageBar',
                            face: 'mini',
                            buttonCount: 5,
                            currentPage: 6,
                            jump: true,
                            sumPage: 10,
                            prependContent: '迷你样式:&nbsp;&nbsp;'
                        },
                        {
                            type: 'PageBar',
                            face: 'simple',
                            buttonCount: 5,
                            currentPage: 6,
                            jump: true,
                            sumPage: 10,
                            prependContent: '简单样式:&nbsp;&nbsp;'
                        },
                        {
                            type: 'PageBar',
                            face: 'none',
                            buttonCount: 5,
                            currentPage: 6,
                            jump: true,
                            sumPage: 10,
                            prependContent: '无边框样式:&nbsp;&nbsp;'
                        }
                    ]
                }
            }
        ]
    }
});