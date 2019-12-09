window.sys = {
    login: require( './login/login.js'),
    index: {
        clickMenu: function (target) {
            var menuAction = target.data('menuAction');
            if (!menuAction) {
                return;
            }
            if (menuAction.indexOf("javascript:") == 0) {
                new Function(menuAction.substr(11))();
            } else {
                var main = target.rootNode.next();
                main.data("mainTitle", target.data('menuName'));
                // 这里的menuAction就是模板
                main.template(menuAction);
            }
        }
    },
    menu: {
        reloadDisplay: function (target, menuId, welcome) {
            var vm = VM(target);
            var mainDisplay = vm.find('mainDisplay');
            var template = "";
            if (welcome) {
                template = "sys/menu/welcome";
            } else {
                if (!menuId) {
                    mainDisplay.data("menuParent", mainDisplay.data("menuId"));
                }
                mainDisplay.data("menuId", menuId);
                template = "sys/menu/display";
            }

            vm.find('saveBtn').status(welcome ? "disabled" : "normal");
            vm.find('delBtn').status((welcome || !menuId) ? "disabled" : "normal");
            mainDisplay.template(template);
        },
        saved: function (target, response) {
            var menuId = response.data.menuId;
            var menuPath = response.data.menuPath;
            var menuParent = response.data.menuParent;

            var vm = VM(target);

            var targetLeaf = vm.find(menuId);
            var parentLeaf = vm.find(menuParent);
            if (!targetLeaf) {
                parentLeaf.append({
                    type: 'leaf',
                    id: menuId,
                    text: response.data.menuName,
                    data: response.data,
                    cls: response.data.menuStatus != '1' ? 'z-ds' : null
                });
            } else {
                // 变更菜单名称
                targetLeaf.attr('text', response.data.menuName);
                // 菜单状态变更
                if (response.data.menuStatus == '0') {
                    targetLeaf.addClass('z-ds');
                } else {
                    targetLeaf.removeClass('z-ds');
                }

                // 新菜单序号
                var targetOrder = parseInt(response.data.menuOrder);
                var oldOrder = parseInt(targetLeaf.data('menuOrder'));
                // 上级菜单或菜单序号变动时需要排序
                if (targetLeaf.parentNode != parentLeaf || targetOrder != oldOrder) {
                    var haveInsert = false;
                    for (var i = 0; i < parentLeaf.length; i++) {
                        var tempLeaf = parentLeaf[i];
                        var tempOrder = parseInt(tempLeaf.data('menuOrder'));
                        if (targetOrder < tempOrder) {
                            // 比当前序号小
                            haveInsert = true;
                            tempLeaf.before(targetLeaf);
                            break;
                        } else if (targetOrder == tempOrder) {
                            if (response.data.menuId < tempLeaf.data('menuId')) {
                                haveInsert = true;
                                tempLeaf.before(targetLeaf);
                                break;
                            }
                        }
                    }
                    // 以上均不满足则拼接在尾部
                    if (!haveInsert) {
                        parentLeaf.append(targetLeaf);
                    }
                    // 变更菜单序号
                    targetLeaf.data('menuOrder', targetOrder);
                }
            }
            parentLeaf.toggle(true);
            // FIXME 上级菜单中的候选项还需要刷新
            vm.fv('menuId', menuId);
            vm.fv('menuPath', menuPath);
            vm.find('delBtn').status("normal");
            app.alertInfo('保存成功');
        },
        deleted: function (target, response) {
            if (!response) {
                return;
            }
            var vm = VM(target);
            var menuId = response.data;
            var targetLeaf = vm.find(menuId);
            if (targetLeaf) {
                targetLeaf.remove();
            }
            app.alertInfo('删除成功');
            this.reloadDisplay(target, '', true);
        }
    },

    org: {
        reloadDisplay: function (target, orgId, welcome) {
            var vm = VM(target);
            var mainDisplay = vm.find('mainDisplay');
            var template = "";
            if (welcome) {
                template = "sys/org/welcome";
            } else {
                if (!orgId) {
                    mainDisplay.data("orgParent", mainDisplay.data("orgId"));
                }
                mainDisplay.data("orgId", orgId);
                template = "sys/org/display";
            }

            vm.find('saveBtn').status(welcome ? "disabled" : "normal");
            vm.find('delBtn').status((welcome || !orgId) ? "disabled" : "normal");
            mainDisplay.template(template);
        },
        saved: function (target, response) {
            var orgId = response.data.orgId;
            var orgPath = response.data.orgPath;
            var orgParent = response.data.orgParent;

            var vm = VM(target);
            // FIXME 机构刷新未处理
            vm.fv('orgId', orgId);
            vm.fv('orgPath', orgPath);
            vm.find('delBtn').status("normal");
            app.alertInfo('保存成功');
        },
        deleted: function (target, response) {
            if (!response) {
                return;
            }
            var vm = VM(target);
            var orgId = response.data;
            var targetLeaf = vm.find(orgId);
            if (targetLeaf) {
                targetLeaf.remove();
            }
            app.alertInfo('删除成功');
            this.reloadDisplay(target, '', true);
        },
        picker: {
            select: function (target) {
                var selected = VM(target).find('selected');
                var multiple = $.dialog(target).commander.attr('multiple');
                var orgId = target.attr('id');
                var orgName = target.attr('text');
                var row = selected.row({orgId: orgId});
                if (!row) {
                    if (!multiple) {
                        // 单选,清空选项
                        selected.deleteAllRows();
                    }
                }
                if (!row) {
                    selected.insertRow({orgId: orgId, orgName: orgName});
                }
            },
            yes: function (target) {
                var rowsData = VM(target).find('selected').rowsData();
                var selValue = '';
                if (rowsData) {
                    for (var i = 0; i < rowsData.length; i++) {
                        if (selValue) {
                            selValue += ',';
                        }
                        selValue += rowsData[i].data.orgId;
                    }
                }
                $.dialog(target).commander.val(selValue);
                $.close(target);
            }
        }

    },

    user: {
        picker: {
            click: function (target) {
                var selected = VM(target).find('selected');
                var userId = target.data('userId');
                var removed = false;
                for (var i = 0; i < selected.length; i++) {
                    var option = selected[i];
                    if (userId == option.data('userId')) {
                        option.remove();
                        removed = true;
                        target.focus(false);
                        break;
                    }
                }
                if (!removed) {
                    if (!$.dialog(target).commander.attr('multiple')) {
                        selected.empty();
                    }
                    selected.append({text: target.attr('text'), data: {"userId": userId}});
                }
            },
            yes: function (target) {
                var selected = VM(target).find('selected');
                var selValue = '';
                for (var i = 0; i < selected.length; i++) {
                    if (selValue) {
                        selValue += ',';
                    }
                    var option = selected[i];
                    selValue += option.data('userId');
                }
                $.dialog(target).commander.val(selValue);
                $.close(target);
            },
            closed: function (target) {
                var options = VM(target).find('options');
                var userId = target.data('userId');
                for (var i = 0; i < options.length; i++) {
                    var option = options[i];
                    if (userId == option.data('userId')) {
                        option.focus(false);
                        break;
                    }
                }
            }
        }
    },

    role: {
        reloadDisplay: function (target, roleCode, welcome) {
            var vm = VM(target);
            var mainDisplay = vm.find('mainDisplay');
            var template = "";
            if (welcome) {
                template = "sys/role/welcome";
            } else {
                mainDisplay.data("roleCode", roleCode);
                template = "sys/role/display";
            }

            vm.find('saveBtn').status(welcome ? "disabled" : "normal");
            vm.find('delBtn').status((welcome || !roleCode) ? "disabled" : "normal");
            mainDisplay.template(template);
        },
        saved: function (target, response) {
            var vm = VM(target);
            vm.fv('isNew', '0');
            // FIXME 角色刷新未实现
            vm.find('delBtn').status("normal");
            app.alertInfo('保存成功');
        },
        deleted: function (target, response) {
            if (!response) {
                return;
            }
            var vm = VM(target);
            var roleCode = response.data;
            var targetLeaf = vm.find(roleCode);
            if (targetLeaf) {
                targetLeaf.remove();
            }
            app.alertInfo('删除成功');
            this.reloadDisplay(target, '', true);
        }
    },

    // 数据字典
    code: {
        type: {
            edit: function (target, typeId) {
                sys.code.reloadDisplay(target, typeId);
            },
            locate: function (target, typeId) {
                target.cmd({
                    type: "ajax",
                    src: "sys/code/type/locate?locate=" + typeId,
                    success: "sys.code.type.reloadList(this, $response);VM(this).find('sysCodeGrid').focusRow({typeId: '" + typeId + "'})"
                });
            },
            reloadList: function (target, response) {
                VM(target).find('typeList').reload(response);
            },
            saved: function (target, response) {
                if (!response) {
                    return;
                }
                var vm = VM(target);
                var typeId = response.data.typeId;
                var grid = vm.find("sysCodeGrid");
                var targetRow = grid.row({typeId: typeId});
                if (targetRow) {
                    grid.updateRow({
                        typeId: typeId,
                        typeName: response.data.typeName
                    }, targetRow.nodeIndex);
                } else {
                    vm.fv('isNew', "0");
                    vm.find('typeId').status("readonly");
                    vm.find("codeGrid").data("typeId", typeId);
                    vm.find("typeList").reload();
                }
                // 开启新建代码权限
                vm.find('newCodeBtn').disable(false);
                var codeBtn = vm.find('codeBtn');
                codeBtn.disable(false);
                codeBtn.focus();
                app.alertInfo('保存成功');
            },
            deleted: function (target, response) {
                if (!response.data) {
                    return;
                }
                app.alertInfo('删除成功');
                VM(target).reload();
            }
        },

        reloadDisplay: function (target, typeId, welcome) {
            var vm = VM(target);
            var template = welcome ? "sys/code/welcome" : "sys/code/display";
            vm.find('saveBtn').status(welcome ? "disabled" : "normal");
            vm.find('delBtn').status((welcome || !typeId) ? "disabled" : "normal");
            vm.find('newCodeBtn').status(!typeId ? "disabled" : "normal");
            var codeDisplay = vm.find('codeDisplay');
            codeDisplay.data("typeId", typeId);
            codeDisplay.template(template);
        },

        formatOper: function (target, typeId, codeValue) {
            var html = "<div class='x-grid-oper'>";
            html += "<i onclick=\"VM(this).cmd('edit','" + typeId + "','" + codeValue + "')\" title='编辑' class='i-oper-edit'></i>";
            html += "&nbsp;&nbsp;<i onclick=\"VM(this).cmd('delete','" + typeId + "','" + codeValue + "')\" title='删除' class='i-oper-del'></i>";
            if (target.nodeIndex > 0) {
                html += "&nbsp;&nbsp;<i onclick=\"VM(this).cmd('move','" + typeId + "','" + codeValue + "','T')\" title='置顶' class='i-oper-top'></i>";
                html += "&nbsp;&nbsp;<i onclick=\"VM(this).cmd('move','" + typeId + "','" + codeValue + "','U')\" title='上移' class='i-oper-up'></i>";
            } else {
                html += "&nbsp;&nbsp;<i title='置顶' class='i-oper-top z-ds'></i>";
                html += "&nbsp;&nbsp;<i title='上移' class='i-oper-up z-ds'></i>";
            }
            if (target.nodeIndex < target.parentNode.length - 1) {
                html += "&nbsp;&nbsp;<i onclick=\"VM(this).cmd('move','" + typeId + "','" + codeValue + "','D')\" title='下移' class='i-oper-down'></i>";
                html += "&nbsp;&nbsp;<i onclick=\"VM(this).cmd('move','" + typeId + "','" + codeValue + "','B')\" title='置底' class='i-oper-bottom'></i>";
            } else {
                html += "&nbsp;&nbsp;<i title='下移' class='i-oper-down z-ds'></i>";
                html += "&nbsp;&nbsp;<i title='置底' class='i-oper-bottom z-ds'></i>";
            }
            html += "</div>";
            return html;
        },

        saved: function (target, response) {
            if (!response.data) {
                return;
            }
            app.alertInfo('保存成功');
            dfish.close(target);
            VM(target).parent.find("codeGrid").reload();
        },
        deleted: function (target, response) {
            if (!response.data) {
                return;
            }
            app.alertInfo('删除成功');
            VM(target).find("codeGrid").reload();
        },
        moved: function (target, response) {
            if (!response.data) {
                return;
            }
            var msg = '';
            var moveType = response.data;
            if (moveType == "T") {
                msg = "置顶";
            } else if (moveType == "U") {
                msg = "上移";
            } else if (moveType == "D") {
                msg = "下移";
            } else if (moveType == "B") {
                msg = "置底";
            }
            app.alertInfo(msg + '操作成功');
            VM(target).find("codeGrid").reload();
        }
    }

};