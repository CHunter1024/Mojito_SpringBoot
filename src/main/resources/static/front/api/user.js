// 登出
function logoutApi() {
    return $axios({
        url: '/user/logout',
        method: 'post'
    })
}

// 文件上传
function upload(file) {
    // 文件上传需要以表单的形式发送
    const data = new FormData()
    data.append("file", file)  // 这里一定要双引号
    return $axios({
        url: '/common/upload',
        method: 'post',
        headers: {'Content-Type': 'multipart/form-data'},
        data
    })
}

// 修改用户信息
function editUserInfo(data) {
    return $axios({
        url: '/user',
        method: 'put',
        data
    })
}

// 更新用户邮箱
function UpdateUserEmail(data) {
    return $axios({
        url: '/user/email',
        method: 'put',
        data
    })
}