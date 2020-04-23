/**
 *@author lamontYu
 *
 *@since
 */
define.template({
    type: 'View',
    node: {
        type: 'Tabs',
        position: 'l',
        nodes: [
            {
                text: 'Button',
                target: {
                    type: 'ButtonBar',
                    height: -1,
                    space: 10,
                    nodes: [
                        {
                            type: 'Button',
                            text: '按钮1',
                            nodes: [
                                {text: '按钮1-1'},
                                {text: '按钮1-2'},
                                {text: '按钮1-3'}
                            ]
                        },
                        {type: 'SubmitButton', text: '表单提交按钮'},
                        {text: '按钮3'}
                    ]
                }
            },
            {
                text: 'Tree',
                target: {
                    type: 'Tree',
                    pub: {on: {click: '$.alert($text);'}},
                    nodes: [
                        {
                            type: 'Leaf', // 这里可省略不写
                            text: '北京市'
                        },
                        {text: '上海市'},
                        {text: '广东省'},
                        {
                            text: '福建省',
                            expanded: true,
                            nodes: [
                                {
                                    type: 'Leaf', // 这里可省略不写
                                    text: '福州市',
                                    expanded: true,
                                    nodes: [
                                        {text: '市辖区'},
                                        {text: '鼓楼区'},
                                        {text: '台江区'},
                                        {text: '仓山区'},
                                        {text: '马尾区'},
                                        {text: '晋安区'},
                                        {text: '闽侯县'},
                                        {text: '连江县'},
                                        {text: '罗源县'},
                                        {text: '闽清县'},
                                        {text: '永泰县'},
                                        {text: '平潭县'},
                                        {text: '福清市'},
                                        {text: '长乐区'}
                                    ]
                                },
                                {
                                    text: '厦门市',
                                    nodes: [
                                        {text: '市辖区'},
                                        {text: '思明区'},
                                        {text: '海沧区'},
                                        {text: '湖里区'},
                                        {text: '集美区'},
                                        {text: '同安区'},
                                        {text: '翔安区'}
                                    ]
                                },
                                {text: '莆田市'},
                                {text: '三明市'},
                                {text: '泉州市'},
                                {text: '漳州市'},
                                {text: '南平市'},
                                {text: '龙岩市'},
                                {text: '宁德市'}
                            ]
                        },
                    ]
                }
            },
            {
                text: 'Html',
                target: {
                    type: 'Html',
                    text: '这是Html内容,可以写html代码,<a href="https://www.baidu.com" target="_blank">点击这里前往百度</a>'
                }
            },
            {
                text: 'Blank',
                target: {
                    type: 'Blank'
                }
            },
            {
                text: 'Album',
                target: {
                    type: 'Album',
                    pub: {width: 100, height: 100},
                    nodes: [
                        {src: '.i-operation-edit', text: '编辑'},
                        {src: '.i-operation-del', text: '删除'},
                        {src: '.i-operation-more', text: '更多'},
                        {src: '.i-operation-top', text: '置顶'},
                        {src: '.i-operation-bottom', text: '置底'},
                        {src: '.i-operation-up', text: '上移'},
                        {src: '.i-operation-down', text: '下移'},
                        {src: '.i-operation-back', text: '回退'},
                        {src: '.i-operation-search', text: '查询'},
                        {src: '.i-operation-money', text: '费用'},
                        {src: '.i-operation-favorite', text: '收藏'},
                        {src: '.i-operation-setup', text: '设置'},
                        {src: '.i-operation-history', text: '历史'},
                        {src: '.i-operation-tip', text: '提示'},
                        {src: '.i-operation-refresh', text: '刷新'},
                        {src: '.i-operation-qrcode', text: '二维码'},
                        {src: '.i-operation-sign', text: '签到'},
                        {src: '.i-operation-view', text: '查看'},
                        {src: '.i-operation-pass', text: '同意'},
                        {src: '.i-operation-reject', text: '驳回'},
                        {src: '.i-operation-cancel', text: '取消'}
                    ]
                }
            },
            {
                text: 'Calendar',
                target: {
                    type: 'Vertical',
                    scroll: true,
                    nodes: [
                        {type: 'Toggle', text: '日期模式'},
                        {
                            type: 'Calendar',
                            face: 'date',
                            date: '2020-02-02',
                            focusDate: '2020-02-02'
                        },
                        {type: 'Toggle', text: '周模式'},
                        {
                            type: 'Calendar',
                            face: 'week',
                            date: '2020-02',
                            focusDate: '2020-02',
                            nodes: [
                                {
                                    type: 'CalendarItem',
                                    value: '2020-04',
                                    text: '这里可以修改显示文字',
                                    style: 'background:blue;color:#fff;'
                                }
                            ]
                        },
                        {type: 'Toggle', text: '月模式'},
                        {
                            type: 'Calendar',
                            face: 'month',
                            date: '2020-02',
                            focusDate: '2020-02'
                        },
                        {type: 'Toggle', text: '年模式'},
                        {
                            type: 'Calendar',
                            face: 'year',
                            date: '2020',
                            focusDate: '2020'
                        }
                    ]
                }
            },
            {
                text: 'Toggle',
                target: {
                    type: 'Vertical',
                    nodes: [
                        {type: 'Toggle', text: '这里不指定target,目标就是与下个toggle之间包含的内容'},
                        {type: 'Html', text: '第1个html', height: 50, style: 'background:#fc942d;'},
                        {type: 'Toggle', text: '这里指定第3个html', target: 'html3'},
                        {type: 'Html', text: '第2个html', height: 100, style: 'background:#579bfb;'},
                        {type: 'Html', id: 'html3', text: '第3个html', height: '*', style: 'background:#72b636;'},
                        {type: 'Toggle', text: '后面有条线', hr: true},
                        {
                            type: 'Form', nodes: [
                                {type: 'Toggle', text: 'Form下的toggle', hr: true},
                                {type: 'Html', text: '随便测试'}
                            ]
                        }
                    ]
                }
            },
            {
                text: 'Progress',
                target: {
                    type: 'Progress',
                    height: -1,
                    pub: {range: '25,50,75,100'},
                    nodes: [
                        {
                            percent: 20
                        },
                        {
                            percent: 40
                        },
                        {
                            percent: 60
                        },
                        {
                            percent: 80
                        },
                        {
                            percent: 100
                        }
                    ]
                }
            },
            {
                text: 'Timeline',
                target: {
                    type: 'Timeline',
                    align: 'center',
                    // pub: {icon: '.i-file'},
                    nodes: [
                        {
                            type: 'TimelineItem', // 可省略不写
                            align: 'left',
                            text: '2005.7 RJ-iTASKV3.0发布，开始开发网络文本会议，由其中的数据交换模式酝酿ALL-AJAX系统体系架构（即页面数据交换全AJAX模式），并着手设计RJ-iTASKV5.0系统框架。'
                        },
                        {
                            type: 'TimelineItem', // 可省略不写
                            align: 'left',
                            text: '2006~2007 设计并完成Xmltmp封装解析引擎。主要利用ITASKV5.0开发工作之外的时间完成XmltmpV1.0，并在部门内部发布XmltmpV1.0版本。'
                        },
                        {
                            text: '2007~2008 结合DFish相关项目产品研发与产品推广情况，研发团队发现xmltmpV1.0的二次开发缺陷；扩展不够灵活，讨论决定重构Xmltmp，并于2007年10月发布Xmltmp2.0。在此基础上完善了开发框架的Demo包、外部自适应性的相关接口，并定义了相关标准，于2008年1月正式命名为“深海鱼”，简称DFish。 '
                        },
                        {
                            text: '2008.3 正式发布DFishV1.0，开始组织相关培训及后续功能组件的完善维护，尝试性项目产品开发。'
                        },
                        {
                            align: 'left',
                            text: '2009~2010 经过多个项目产品的试用，完善相关功能细节、Bug，在完成“易检运维支撑平台”开发及“RJ-CMS 7X”产品开发后，正式推出DFishV2.0版本。'
                        },
                        {
                            align: 'left',
                            text: '2011~2012 持续支持、保障更多基于DFish框架的项目产品开发，持续完善框架组件功能，提供快捷的二次开发集成功能、布局面板，于2011年5月正式发布DFishV2.2。'
                        },
                        {
                            text: '2016.8 DFish3.0 重构引擎内核，以json作为内核结构，'
                        },
                        {
                            text: '2018.8 DFish3.1 按框架组件模块划分，工程支持maven依赖'
                        },
                        {
                            text: '2019.7 DFish3.2 前后端分离预研阶段，为前后端分离技术可行性进行充分验证。'
                        },
                        {
                            align: 'left',
                            text: '2020.2 DFish5.0 前后端分离：框架组件全面支持前后端分离技术；插件式加载：Java端组件可根据项目需要按需装载；多终端显示：一套可在web和移动端较友好的显示。'
                        }
                    ]
                }
            },
            {
                text: 'EmbedWindow',
                target: {
                    type: 'EmbedWindow',
                    src: 'https://www.baidu.com'
                }
            }
        ]
    }
});