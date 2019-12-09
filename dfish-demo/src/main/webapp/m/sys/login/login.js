var login = {
    error: function (target, response) {
        if (!response.error) {
            return response;
        }
        $.alert(response.error.msg);
        var vm = VM(target);
        vm.fv('pwd', '');
        vm.find('checkCode').reset();
    },
    login: function (target) {
        var vm = VM(target);
        var redirect = vm.fv('redirect');
        if (!redirect) {
            redirect = "./sys.jsp";
        }
        // 登录后首页
        window.location.href = redirect;
    }
};
module.exports = login;