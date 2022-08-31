// 获取验证码
function getCodeApi(email) {
    return $axios({
        url: `/user/getCode/${email}`,
        method: 'get',
    })
}

// 登录
function loginApi(data) {
    return $axios({
        url: '/user/login',
        method: 'post',
        data
    })
}

  