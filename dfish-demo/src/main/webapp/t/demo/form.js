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
                text: 'Text',
                target: {
                    type: 'Form',
                    nodes: [
                        {type: 'Text', name: 'text1', value: '这是Text的值', label: {text: '常用'}},
                        {
                            type: 'Text',
                            name: 'text2',
                            label: {text: '必填'},
                            validate: {required: true}
                        },
                        {type: 'Text', name: 'text3', placeholder: '这里可以定义未输入文字提示语', label: {text: '未输入提示语'}},
                        {type: 'Text', name: 'text4', value: '不显示标签', label: '不显示标签'},
                        {
                            type: 'Text',
                            name: 'text5',
                            value: '文字删除可以看到无label的效果(关注placeholder提示语)'
                        }
                    ]
                }
            },
            {
                text: 'Textarea',
                target: {
                    type: 'Form',
                    nodes: [
                        {type: 'Textarea', name: 'textarea1', value: '这是Textarea的值', label: {text: '常用'}},
                        {
                            type: 'Textarea',
                            name: 'textarea2',
                            label: {text: '必填'},
                            validate: {required: true}
                        },
                        {
                            type: 'Textarea',
                            name: 'textarea3',
                            label: {text: '自定义高度'},
                            height: 200,
                        }
                    ]
                }
            },
            {
                text: 'Password',
                target: {
                    type: 'Form',
                    nodes: [
                        {type: 'Password', name: 'password1', label: {text: '常用'}},
                        {
                            type: 'Password',
                            name: 'password2',
                            label: {text: '必填'},
                            validate: {required: true}
                        }
                    ]
                }
            },
            {
                text: 'Hidden',
                target: {
                    type: 'Form',
                    hiddens: [
                        {name: 'hidden1', value: 'Hidden1'}
                    ],
                    pub: {colSpan: 4},
                    nodes: [
                        {type: 'Hidden', name: 'hidden2', value: 'Hidden2'},
                        {type: 'Text', name: 'text', value: '通常Hidden放到hiddens下;左侧的Hidden放到可见区域,当做普通的widget显示'}
                    ]
                }
            },
            {
                text: 'Radio',
                target: {
                    type: 'Form',
                    pub: {colSpan: -1},
                    nodes: [
                        {
                            type: 'FormGroup', label: {text: '单选框(一般用RadioGroup)'}, nodes: [
                                {type: 'Radio', name: 'radio', value: '1', text: '男', checked: true},
                                {type: 'Radio', name: 'radio', value: '2', text: '女'}
                            ]
                        }
                    ]
                }
            },
            {
                text: 'RadioGroup',
                target: {
                    type: 'Form',
                    pub: {colSpan: -1},
                    nodes: [
                        {
                            type: 'RadioGroup', label: {text: '常用'}, nodes: [
                                {name: 'radio1', text: '男', value: '1', checked: true},
                                {name: 'radio1', text: '女', value: '2'}
                            ]
                        },
                        {
                            type: 'RadioGroup', label: {text: '必填'},
                            pub: {validate: {required: true}},
                            nodes: [
                                {name: 'radio2', text: '男', value: '1', checked: true},
                                {name: 'radio2', text: '女', value: '2'}
                            ]
                        },
                        {
                            type: 'RadioGroup', label: {text: '指定目标'},
                            pub: {name:'radio3'},
                            nodes: [
                                {text: 'String', value: 'S', checked: true, target: {type: 'Text', label: '字符'}},
                                {text: 'Boolean', value: 'B', target: {type: 'Textarea', label: '布尔'}}
                            ]
                        }
                    ]
                }
            },
            {
                text: 'CheckBox',
                target: {
                    type: 'Form',
                    pub: {colSpan: -1},
                    nodes: [
                        {
                            type: 'FormGroup', label: {text: '复选框(一般用CheckBoxGroup)'}, nodes: [
                                {type: 'CheckBox', name: 'checkBox', value: '1', text: '琴', checked: true},
                                {type: 'CheckBox', name: 'checkBox', value: '2', text: '棋', checked: true},
                                {type: 'CheckBox', name: 'checkBox', value: '3', text: '书'},
                                {type: 'CheckBox', name: 'checkBox', value: '4', text: '画', checked: true}
                            ]
                        }
                    ]
                }
            },
            {
                text: 'CheckBoxGroup',
                target: {
                    type: 'Form',
                    pub: {colSpan: -1},
                    nodes: [
                        {
                            type: 'CheckBoxGroup', label: {text: '常用'}, nodes: [
                                {name: 'checkBox1', value: '1', text: '琴', checked: true},
                                {name: 'checkBox1', value: '2', text: '棋', checked: true},
                                {name: 'checkBox1', value: '3', text: '书'},
                                {name: 'checkBox1', value: '4', text: '画', checked: true}
                            ]
                        },
                        {
                            type: 'CheckBoxGroup', label: {text: '必填'},
                            pub: {validate: {required: true}},
                            nodes: [
                                {name: 'checkBox2', value: '1', text: '琴'},
                                {name: 'checkBox2', value: '2', text: '棋'},
                                {name: 'checkBox2', value: '3', text: '书'},
                                {name: 'checkBox2', value: '4', text: '画'}
                            ]
                        },
                        {
                            type: 'RadioGroup', label: {text: '指定目标'},
                            pub: {name:'checkBox3'},
                            nodes: [
                                {text: 'String', value: 'S', checked: true, target: {type: 'Text', label: '字符'}},
                                {text: 'Boolean', value: 'B', target: {type: 'Textarea', label: '布尔'}}
                            ]
                        }
                    ]
                }
            },
            {
                text: 'DatePicker',
                target: {
                    type: 'Form',
                    nodes: [
                        {type: 'DatePicker', name: 'datePicker1', label: {text: '日期'}},
                        {type: 'DatePicker', name: 'datePicker2', label: {text: '月'}, format: 'yyyy-mm'},
                        {type: 'DatePicker', name: 'datePicker3', label: {text: '年'}, format: 'yyyy'},
                        {type: 'DatePicker', name: 'datePicker4', label: {text: '时间'}, format: 'yyyy-mm-dd hh:ii:ss'}
                    ]
                }
            },
            {
                text: 'Select',
                target: {
                    type: 'Form',
                    nodes: [
                        {
                            type: 'Select', name: 'select', label: {text: '原生Select(比较少用)'},
                            value: 2,
                            nodes: [
                                {value: 1, text: 'SUNDAY'},
                                {value: 2, text: 'MONDAY'},
                                {value: 3, text: 'TUESDAY'},
                                {value: 4, text: 'WEDNESDAY'},
                                {value: 5, text: 'THURSDAY'},
                                {value: 6, text: 'FRIDAY'},
                                {value: 7, text: 'SATURDAY'}
                            ]
                        }
                    ]
                }
            },
            {
                text: 'DropBox',
                target: {
                    type: 'Form',
                    nodes: [
                        {
                            type: 'DropBox', name: 'dropBox', label: {text: '常用'},
                            value: 2,
                            nodes: [
                                {value: 1, text: 'SUNDAY'},
                                {value: 2, text: 'MONDAY'},
                                {value: 3, text: 'TUESDAY'},
                                {value: 4, text: 'WEDNESDAY'},
                                {value: 5, text: 'THURSDAY'},
                                {value: 6, text: 'FRIDAY'},
                                {value: 7, text: 'SATURDAY'}
                            ]
                        },
                        {
                            type: 'DropBox', name: 'dropBox', label: {text: '单选可取消'},
                            value: 2,
                            cancelable: true,
                            nodes: [
                                {value: 1, text: 'SUNDAY'},
                                {value: 2, text: 'MONDAY'},
                                {value: 3, text: 'TUESDAY'},
                                {value: 4, text: 'WEDNESDAY'},
                                {value: 5, text: 'THURSDAY'},
                                {value: 6, text: 'FRIDAY'},
                                {value: 7, text: 'SATURDAY'}
                            ]
                        },
                        {
                            type: 'DropBox', name: 'dropBox', label: {text: '多选'},
                            multiple: true,
                            nodes: [
                                {value: 1, text: 'SUNDAY'},
                                {value: 2, text: 'MONDAY'},
                                {value: 3, text: 'TUESDAY'},
                                {value: 4, text: 'WEDNESDAY'},
                                {value: 5, text: 'THURSDAY'},
                                {value: 6, text: 'FRIDAY'},
                                {value: 7, text: 'SATURDAY'}
                            ]
                        },
                        {
                            type: 'DropBox', name: 'dropBox', label: {text: '选项带图标'},
                            nodes: [
                                {value: 1, text: 'SUNDAY', icon: '.i-folder'},
                                {value: 2, text: 'MONDAY', icon: '.i-folder'},
                                {value: 3, text: 'TUESDAY', icon: '.i-folder'},
                                {value: 4, text: 'WEDNESDAY', icon: '.i-folder'},
                                {value: 5, text: 'THURSDAY', icon: '.i-folder'},
                                {value: 6, text: 'FRIDAY', icon: '.i-folder'},
                                {value: 7, text: 'SATURDAY', icon: '.i-folder'}
                            ]
                        }
                    ]
                }
            },
            {
                text: 'Spinner',
                target: {
                    type: 'Form',
                    nodes: [
                        {type: 'Spinner', name: 'spinner1', label: {text: '常用'}},
                        {type: 'Spinner', name: 'spinner2', label: {text: '保留2位小数'}, decimal: 2},
                        {
                            type: 'Spinner',
                            name: 'spinner3',
                            label: {text: '金额'},
                            width: '*',
                            format: {length: 3},
                            noButton: true
                        },
                        {
                            type: 'Spinner',
                            name: 'spinner4',
                            label: {text: '银行卡号'},
                            width: '*',
                            format: {length: 4, separator: ' ', rightward: true},
                            noButton: true
                        }
                    ]
                }
            },
            {
                text: 'Switch',
                target: {
                    type: 'Form',
                    nodes: [
                        {type: 'Switch', name: 'switch1', label: {text: '默认'}, value: 1},
                        {
                            type: 'Switch',
                            name: 'switch2',
                            label: {text: '更改文本'},
                            value: 1,
                            checked: true,
                            checkedText: '是',
                            uncheckedText: '否'
                        }
                    ]
                }
            },
            {
                text: 'Slider',
                target: {
                    type: 'Form',
                    nodes: [
                        {type: 'Slider', name: 'slider1', label: {text: '默认'}},
                        {type: 'Slider', name: 'slider2', label: {text: '更改数值'}, validate: {maxValue: 200}}
                    ]
                }
            },
            {
                text: 'Range',
                target: {
                    type: 'Form',
                    nodes: [
                        {
                            type: 'Range',
                            label: {text: '时间范围'},
                            begin: {type: 'DatePicker', name: 'beginTime', label: '开始时间'},
                            end: {type: 'DatePicker', name: 'endTime', label: '结束时间'}
                        },
                        {
                            type: 'Range',
                            label: {text: '数值范围'},
                            begin: {type: 'Spinner', name: 'beginNum', label: '开始数值'},
                            end: {type: 'Spinner', name: 'endNum', label: '结束数值'}
                        },
                        {
                            type: 'Range',
                            label: {text: '设置中间连接字'},
                            begin: {type: 'DatePicker', name: 'beginTime2', label: '开始时间2'},
                            end: {type: 'DatePicker', name: 'endTime2', label: '结束时间2'},
                            to: '到'
                        }
                    ]
                }
            },
            {
                text: 'FormGroup',
                target: {
                    type: 'Form',
                    nodes: [
                        {
                            type: 'FormGroup',
                            label: {text: '表单组合'},
                            nodes: [
                                {type: 'Html', text: '用Html的效果同FormLabel'}
                            ]
                        }
                    ]
                }
            },
            {
                text: 'FormLabel',
                target: {
                    type: 'Form',
                    nodes: [
                        {type: 'FormLabel', text: '保密', label: {text: '性别'}}
                    ]
                }
            },
            {
                text: 'ImgBox',
                target: {
                    type: 'Form',
                    nodes: [
                        {
                            type: 'ImgBox', label: {text: '图片选择'}, nodes: [
                                {value: 'T', icon: '.i-operation-top'},
                                {value: 'U', icon: '.i-operation-up'},
                                {value: 'D', icon: '.i-operation-down'},
                                {value: 'B', icon: '.i-operation-bottom'}
                            ]
                        },
                        {
                            type: 'ImgBox', label: {text: '带文本'}, nodes: [
                                {value: 'T', icon: '.i-operation-top', text: '置顶'},
                                {value: 'U', icon: '.i-operation-up', text: '上移'},
                                {value: 'D', icon: '.i-operation-down', text: '下移'},
                                {value: 'B', icon: '.i-operation-bottom', text: '置底'}
                            ]
                        }
                    ]
                }
            },
            {
                text: 'FileUpload',
                target: {
                    type: 'Form',
                    nodes: [
                        {
                            type: 'FileUpload',
                            name: 'fileUpload1',
                            label: {text: '常用'}
                        },
                        {
                            type: 'FileUpload',
                            name: 'fileUpload1',
                            label: {text: '定制效果'},
                            uploadButtons: [{type: 'UploadButton', text: '这里可以修改文字'}],
                            valueButtons: [
                                {text: '预览', on: {click: '$.alert("这是预览");'}},
                                {text: '下载', on: {click: '$.alert("这是下载");'}}
                            ]
                        }
                    ]
                }
            },
            {
                text: 'ImageUpload',
                target: {
                    type: 'Form',
                    nodes: [
                        {
                            type: 'ImageUpload',
                            name: 'imageUpload1',
                            label: {text: '常用'}
                        },
                        {
                            type: 'ImageUpload',
                            name: 'imageUpload1',
                            label: {text: '定制效果'},
                            uploadButtons: [{type: 'UploadButton', text: '选择图片', width: 100, height: 100}],
                            valueButtons: [
                                {text: '下载', on: {click: '$.alert("这是下载");'}},
                                {text: '预览', on: {click: '$.alert("这是预览");'}}
                            ]
                        }
                    ]
                }
            },
            {
                text: 'Combobox',
                target: {
                    type: 'Form',
                    nodes: [
                        {
                            type: 'ComboBox',
                            name: 'comboBox1',
                            label: {text: '仅搜索'},
                            placeholder: '请输入文字,如:"鼓楼"',
                            bind: {
                                field: {
                                    value: 'v',
                                    text: 't'
                                }
                            },
                            suggest: {
                                type: 'Dialog',
                                node: {
                                    type: 'View',
                                    node: {
                                        type: 'Vertical',
                                        nodes: [
                                            {
                                                type: 'Table',
                                                height: '*',
                                                columns: [
                                                    {field: 't', width: '*', tip: true, highlight: true}
                                                ],
                                                tBody: {
                                                    nodes: [
                                                        {'v': '350101000000', 't': '市辖区'},
                                                        {'v': '350102000000', 't': '鼓楼区'},
                                                        {'v': '350103000000', 't': '台江区'},
                                                        {'v': '350104000000', 't': '仓山区'},
                                                        {'v': '350105000000', 't': '马尾区'},
                                                        {'v': '350111000000', 't': '晋安区'},
                                                        {'v': '350112000000', 't': '长乐区'},
                                                        {'v': '350121000000', 't': '闽侯县'},
                                                        {'v': '350122000000', 't': '连江县'},
                                                        {'v': '350123000000', 't': '罗源县'},
                                                        {'v': '350124000000', 't': '闽清县'},
                                                        {'v': '350125000000', 't': '永泰县'},
                                                        {'v': '350128000000', 't': '平潭县'},
                                                        {'v': '350181000000', 't': '福清市'}
                                                    ]
                                                }
                                            }
                                        ]
                                    }
                                }
                            }
                        },
                        {
                            type: 'ComboBox',
                            name: 'comboBox2',
                            label: {text: '下拉选项'},
                            placeholder: '输入文字搜索或点击右侧按钮',
                            bind: {
                                field: {
                                    value: 'v',
                                    text: 't'
                                }
                            },
                            drop: {
                                type: 'Dialog',
                                node: {
                                    type: 'View',
                                    node: {
                                        type: 'Vertical',
                                        nodes: [
                                            {
                                                type: 'Table',
                                                height: '*',
                                                columns: [
                                                    {field: 't', width: '*', tip: true, highlight: true}
                                                ],
                                                tBody: {
                                                    nodes: [
                                                        {'v': '350101000000', 't': '市辖区'},
                                                        {'v': '350102000000', 't': '鼓楼区'},
                                                        {'v': '350103000000', 't': '台江区'},
                                                        {'v': '350104000000', 't': '仓山区'},
                                                        {'v': '350105000000', 't': '马尾区'},
                                                        {'v': '350111000000', 't': '晋安区'},
                                                        {'v': '350112000000', 't': '长乐区'},
                                                        {'v': '350121000000', 't': '闽侯县'},
                                                        {'v': '350122000000', 't': '连江县'},
                                                        {'v': '350123000000', 't': '罗源县'},
                                                        {'v': '350124000000', 't': '闽清县'},
                                                        {'v': '350125000000', 't': '永泰县'},
                                                        {'v': '350128000000', 't': '平潭县'},
                                                        {'v': '350181000000', 't': '福清市'}
                                                    ]
                                                }
                                            }
                                        ]
                                    }
                                }
                            }
                        },
                        {
                            type: 'ComboBox',
                            name: 'comboBox3',
                            label: {text: '弹窗选择'},
                            placeholder: '输入文字搜索或点击右侧按钮',
                            bind: {
                                field: {
                                    value: 'v',
                                    text: 't'
                                }
                            },
                            suggest: {
                                type: 'Dialog',
                                node: {
                                    type: 'View',
                                    node: {
                                        type: 'Vertical',
                                        nodes: [
                                            {
                                                type: 'Table',
                                                height: '*',
                                                columns: [
                                                    {field: 't', width: '*', tip: true, highlight: true}
                                                ],
                                                tBody: {
                                                    nodes: [
                                                        {'v': '350101000000', 't': '市辖区'},
                                                        {'v': '350102000000', 't': '鼓楼区'},
                                                        {'v': '350103000000', 't': '台江区'},
                                                        {'v': '350104000000', 't': '仓山区'},
                                                        {'v': '350105000000', 't': '马尾区'},
                                                        {'v': '350111000000', 't': '晋安区'},
                                                        {'v': '350112000000', 't': '长乐区'},
                                                        {'v': '350121000000', 't': '闽侯县'},
                                                        {'v': '350122000000', 't': '连江县'},
                                                        {'v': '350123000000', 't': '罗源县'},
                                                        {'v': '350124000000', 't': '闽清县'},
                                                        {'v': '350125000000', 't': '永泰县'},
                                                        {'v': '350128000000', 't': '平潭县'},
                                                        {'v': '350181000000', 't': '福清市'}
                                                    ]
                                                }
                                            }
                                        ]
                                    }
                                }
                            },
                            picker: {
                                type: 'Dialog',
                                cls: 'dlg-picker',
                                node: {
                                    type: 'View',
                                    commands: {
                                        'yes': {
                                            type: 'JS',
                                            text: '$.dialog(this).commander.complete(VM(this).find("grid").getFocus());$.close(this);'
                                        }
                                    },
                                    node: {
                                        type: 'Vertical',
                                        nodes: [
                                            {
                                                type: 'Table',
                                                id: 'grid',
                                                height: '*',
                                                columns: [
                                                    {field: 't', width: '*', tip: true, highlight: true}
                                                ],
                                                pub: {
                                                    focusable: true,
                                                    on: {dblClick: '$.dialog(this).commander.complete(this);$.close(this);'}
                                                },
                                                tBody: {
                                                    nodes: [
                                                        {'v': '350101000000', 't': '市辖区'},
                                                        {'v': '350102000000', 't': '鼓楼区'},
                                                        {'v': '350103000000', 't': '台江区'},
                                                        {'v': '350104000000', 't': '仓山区'},
                                                        {'v': '350105000000', 't': '马尾区'},
                                                        {'v': '350111000000', 't': '晋安区'},
                                                        {'v': '350112000000', 't': '长乐区'},
                                                        {'v': '350121000000', 't': '闽侯县'},
                                                        {'v': '350122000000', 't': '连江县'},
                                                        {'v': '350123000000', 't': '罗源县'},
                                                        {'v': '350124000000', 't': '闽清县'},
                                                        {'v': '350125000000', 't': '永泰县'},
                                                        {'v': '350128000000', 't': '平潭县'},
                                                        {'v': '350181000000', 't': '福清市'}
                                                    ]
                                                }
                                            }
                                        ]
                                    }
                                }
                            }
                        },
                        {
                            type: 'ComboBox',
                            name: 'comboBox4',
                            label: {text: '下拉分页'},
                            placeholder: '输入文字搜索或点击右侧按钮',
                            bind: {
                                field: {
                                    value: 'v',
                                    text: 't'
                                }
                            },
                            drop: {
                                type: 'Dialog',
                                node: {
                                    type: 'View',
                                    node: {
                                        type: 'Vertical',
                                        nodes: [
                                            {
                                                type: 'Table',
                                                height: '*',
                                                id: 'grid',
                                                // limit: 5,
                                                columns: [
                                                    {field: 't', width: '*', tip: true, highlight: true}
                                                ],
                                                tBody: {
                                                    nodes: [
                                                        {'v': '350101000000', 't': '市辖区'},
                                                        {'v': '350102000000', 't': '鼓楼区'},
                                                        {'v': '350103000000', 't': '台江区'},
                                                        {'v': '350104000000', 't': '仓山区'},
                                                        {'v': '350105000000', 't': '马尾区'},
                                                        {'v': '350111000000', 't': '晋安区'},
                                                        {'v': '350112000000', 't': '长乐区'},
                                                        {'v': '350121000000', 't': '闽侯县'},
                                                        {'v': '350122000000', 't': '连江县'},
                                                        {'v': '350123000000', 't': '罗源县'},
                                                        {'v': '350124000000', 't': '闽清县'},
                                                        {'v': '350125000000', 't': '永泰县'},
                                                        {'v': '350128000000', 't': '平潭县'},
                                                        {'v': '350181000000', 't': '福清市'}
                                                    ]
                                                }
                                            },
                                            {
                                                type: 'PageBar',
                                                face: 'mini',
                                                target: 'grid',
                                                buttonCount: 5,
                                                pageSize: 3
                                            }
                                        ]
                                    }
                                }
                            }
                        },
                        {
                            type: 'ComboBox',
                            name: 'comboBox5',
                            label: {text: '树形数据'},
                            placeholder: '输入文字搜索或点击右侧按钮',
                            bind: {
                                field: {
                                    value: 'id',
                                    text: 'text'
                                },
                                fullPath: true
                            },
                            drop: {
                                type: 'Dialog',
                                node: {
                                    type: 'View',
                                    node: {
                                        type: 'Vertical',
                                        nodes: [
                                            {
                                                type: 'Tree',
                                                height: '*',
                                                highlight: true,
                                                pub: {expanded: true},
                                                nodes: [
                                                    {
                                                        id: '350000000000',
                                                        text: '福建省',
                                                        nodes: [
                                                            {
                                                                id: '350100000000',
                                                                text: '福州市',
                                                                nodes: [
                                                                    {id: '350101000000', text: '市辖区'},
                                                                    {id: '350102000000', text: '鼓楼区'},
                                                                    {id: '350103000000', text: '台江区'},
                                                                    {id: '350104000000', text: '仓山区'},
                                                                    {id: '350105000000', text: '马尾区'},
                                                                    {id: '350111000000', text: '晋安区'},
                                                                    {id: '350112000000', text: '长乐区'},
                                                                    {id: '350121000000', text: '闽侯县'},
                                                                    {id: '350122000000', text: '连江县'},
                                                                    {id: '350123000000', text: '罗源县'},
                                                                    {id: '350124000000', text: '闽清县'},
                                                                    {id: '350125000000', text: '永泰县'},
                                                                    {id: '350128000000', text: '平潭县'},
                                                                    {id: '350181000000', text: '福清市'}
                                                                ]
                                                            },
                                                            {
                                                                id: '350200000000',
                                                                text: '厦门市',
                                                                nodes: [
                                                                    {id: '350201000000', text: '市辖区'},
                                                                    {id: '350203000000', text: '思明区'},
                                                                    {id: '350205000000', text: '海沧区'},
                                                                    {id: '350206000000', text: '湖里区'},
                                                                    {id: '350211000000', text: '集美区'},
                                                                    {id: '350212000000', text: '同安区'},
                                                                    {id: '350213000000', text: '翔安区'}
                                                                ]
                                                            }
                                                        ]
                                                    }
                                                ],
                                            }
                                        ]
                                    }
                                }
                            }
                        }
                    ]
                }
            },
            {
                text: 'LinkBox',
                target: {
                    type: 'Form',
                    nodes: [
                        {
                            type: 'LinkBox',
                            name: 'linkBox1',
                            label: {text: '仅搜索'},
                            placeholder: '请输入文字,如:"鼓楼"',
                            bind: {
                                field: {
                                    value: 'v',
                                    text: 't'
                                }
                            },
                            suggest: {
                                type: 'Dialog',
                                node: {
                                    type: 'View',
                                    node: {
                                        type: 'Vertical',
                                        nodes: [
                                            {
                                                type: 'Table',
                                                height: '*',
                                                columns: [
                                                    {field: 't', width: '*', tip: true, highlight: true}
                                                ],
                                                tBody: {
                                                    nodes: [
                                                        {'v': '350101000000', 't': '市辖区'},
                                                        {'v': '350102000000', 't': '鼓楼区'},
                                                        {'v': '350103000000', 't': '台江区'},
                                                        {'v': '350104000000', 't': '仓山区'},
                                                        {'v': '350105000000', 't': '马尾区'},
                                                        {'v': '350111000000', 't': '晋安区'},
                                                        {'v': '350112000000', 't': '长乐区'},
                                                        {'v': '350121000000', 't': '闽侯县'},
                                                        {'v': '350122000000', 't': '连江县'},
                                                        {'v': '350123000000', 't': '罗源县'},
                                                        {'v': '350124000000', 't': '闽清县'},
                                                        {'v': '350125000000', 't': '永泰县'},
                                                        {'v': '350128000000', 't': '平潭县'},
                                                        {'v': '350181000000', 't': '福清市'}
                                                    ]
                                                }
                                            }
                                        ]
                                    }
                                }
                            }
                        }
                    ]
                }
            },
            {
                text: 'OnlineBox',
                target: {
                    type: 'Form',
                    nodes: [
                        {
                            type: 'OnlineBox',
                            name: 'onlineBox1',
                            label: {text: '关键字搜索'},
                            placeholder: '请输入文字,由于该控件一般和后端配合使用,这里范例给的是写死数据,每次搜索结果都一样',
                            bind: {
                                field: {
                                    text: 't'
                                }
                            },
                            suggest: {
                                type: 'Dialog',
                                node: {
                                    type: 'View',
                                    node: {
                                        type: 'Vertical',
                                        nodes: [
                                            {
                                                type: 'Table',
                                                height: '*',
                                                columns: [
                                                    {field: 't', width: '*', tip: true, highlight: true}
                                                ],
                                                tBody: {
                                                    nodes: [
                                                        {'t': '市辖区'},
                                                        {'t': '鼓楼区'},
                                                        {'t': '台江区'},
                                                        {'t': '仓山区'},
                                                        {'t': '马尾区'},
                                                        {'t': '晋安区'},
                                                        {'t': '长乐区'},
                                                        {'t': '闽侯县'},
                                                        {'t': '连江县'},
                                                        {'t': '罗源县'},
                                                        {'t': '闽清县'},
                                                        {'t': '永泰县'},
                                                        {'t': '平潭县'},
                                                        {'t': '福清市'}
                                                    ]
                                                }
                                            }
                                        ]
                                    }
                                }
                            }
                        }
                    ]
                }
            },
            {
                text: 'PickBox',
                target: {
                    type: 'Form',
                    nodes: [
                        {
                            type: 'PickBox',
                            name: 'pickBox1',
                            label: {text: '选择框'},
                            picker: {
                                type: 'Dialog',
                                cls: 'dlg-picker',
                                node: {
                                    type: 'View',
                                    commands: {
                                        'yes': {
                                            type: 'JS',
                                            text: 'var focus=VM(this).find("tree").getFocus();var v,t;if(focus){v=focus.attr("id");t=focus.attr("text");}$.dialog(this).commander.val(v,t);$.close(this);'
                                        }
                                    },
                                    node: {
                                        type: 'Vertical',
                                        nodes: [
                                            {
                                                type: 'Tree',
                                                height: '*',
                                                id: 'tree',
                                                highlight: true,
                                                pub: {
                                                    expanded: true,
                                                    on: {dblClick: '$.dialog(this).commander.val($id,$text);$.close(this);'}
                                                },
                                                nodes: [
                                                    {
                                                        id: '350000000000',
                                                        text: '福建省',
                                                        nodes: [
                                                            {
                                                                id: '350100000000',
                                                                text: '福州市',
                                                                nodes: [
                                                                    {id: '350101000000', text: '市辖区'},
                                                                    {id: '350102000000', text: '鼓楼区'},
                                                                    {id: '350103000000', text: '台江区'},
                                                                    {id: '350104000000', text: '仓山区'},
                                                                    {id: '350105000000', text: '马尾区'},
                                                                    {id: '350111000000', text: '晋安区'},
                                                                    {id: '350112000000', text: '长乐区'},
                                                                    {id: '350121000000', text: '闽侯县'},
                                                                    {id: '350122000000', text: '连江县'},
                                                                    {id: '350123000000', text: '罗源县'},
                                                                    {id: '350124000000', text: '闽清县'},
                                                                    {id: '350125000000', text: '永泰县'},
                                                                    {id: '350128000000', text: '平潭县'},
                                                                    {id: '350181000000', text: '福清市'}
                                                                ]
                                                            },
                                                            {
                                                                id: '350200000000',
                                                                text: '厦门市',
                                                                nodes: [
                                                                    {id: '350201000000', text: '市辖区'},
                                                                    {id: '350203000000', text: '思明区'},
                                                                    {id: '350205000000', text: '海沧区'},
                                                                    {id: '350206000000', text: '湖里区'},
                                                                    {id: '350211000000', text: '集美区'},
                                                                    {id: '350212000000', text: '同安区'},
                                                                    {id: '350213000000', text: '翔安区'}
                                                                ]
                                                            }
                                                        ]
                                                    }
                                                ],
                                            }
                                        ]
                                    }
                                }
                            }
                        }
                    ]
                }
            },
            {
                text: 'Jigsaw',
                target: {
                    type: 'Section',
                    node: {
                        type: 'Form',
                        nodes: [
                            {
                                type: 'Jigsaw',
                                name: 'jigsaw',
                                label: {text: '滑动验证码'},
                                width: 400
                            }
                        ]
                    }
                }
            },
            {
                text: '简单布局',
                target: {
                    // 表单容器
                    type: 'Form',
                    // 一整行显示,可以设置列数(默认一行12列)
                    pub: {colSpan: -1, labelWidth: 120},
                    // 隐藏值
                    hiddens: [
                        {name: 'userId', value: '1001'}
                    ],
                    nodes: [
                        {
                            type: 'Text',
                            name: 'userName',
                            label: {text: '姓名'},
                            value: '张三',
                            validate: {required: true}
                        },
                        {
                            type: 'RadioGroup',
                            name: 'gender',
                            label: {text: '性别'},
                            value: '2',
                            nodes: [
                                {value: '1', text: '男'},
                                {value: '2', text: '女'}
                            ]
                        },
                        {
                            type: 'DatePicker',
                            name: 'birthday',
                            label: {text: '生日'},
                            value: '1970-01-01',
                            format: 'yyyy-mm-dd'
                        },
                        {
                            type: 'CheckBoxGroup',
                            name: 'hobbies',
                            label: {text: '兴趣爱好'},
                            value: '1,2,4',
                            nodes: [
                                {value: '1', text: '琴'},
                                {value: '2', text: '棋'},
                                {value: '3', text: '书'},
                                {value: '4', text: '画'}
                            ]
                        },
                        {type: 'Textarea', name: 'intro', label: {text: '简介'}, value: '不告诉你'},
                        {type: 'Spinner', name: 'position', label: {text: '座号'}, value: 3},
                        {type: 'Switch', name: 'isValid', label: {text: '是否启用'}, checked: true}
                    ]
                }
            },
            {
                text: '复杂布局',
                target: {
                    // 表单容器
                    type: 'Form',
                    // 一整行显示,子元素默认占4列(默认总列数为12列)
                    pub: {labelWidth: 120, colSpan: 4},
                    // 隐藏值
                    hiddens: [
                        {name: 'userId', value: '1001'}
                    ],
                    nodes: [
                        // 简易兼容写法
                        {type: 'Text', name: 'userName', label: {text: '姓名'}},
                        {type: 'Text', name: 'formerName', label: {text: '曾用名'}},

                        // 完整格式,form添加的子节点是td
                        {
                            rowSpan: 3, node: {type: 'Img', src: 'skin/img/icon_user.png'}
                        },

                        {type: 'Text', name: 'idCard', label: {text: '身份证'}},
                        {type: 'Text', name: 'nation', label: {text: '民族'}},

                        {type: 'Text', name: 'gender', label: {text: '性别'}},
                        {type: 'Text', name: 'birthday', label: {text: '出生日期'}},

                        {colSpan: -1, node: {type: 'Text', name: 'address', label: {text: '家庭住址'}}},

                        {colSpan: 6, node: {type: 'Text', name: 'postcode', label: {text: '住宅邮编'}}},
                        {colSpan: 6, node: {type: 'Text', name: 'maritalStatus', label: {text: '婚姻状况'}}},

                        {colSpan: 6, node: {type: 'Text', name: 'nativePlace', label: {text: '籍贯'}}},
                        {colSpan: 6, node: {type: 'Text', name: 'birthplace', label: {text: '出生地'}}}
                    ]
                }
            }
        ]
    }
});