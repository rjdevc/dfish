define.template({
    type: 'jigsaw/auth',
    src: 'jigsaw/auth?offset=$value',
    result: {
        '@success': '$data.success',
        '@msg': '$data.success?"验证通过":null'
    }
});