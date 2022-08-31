// 用户登录
function loginApi(data) {
  return $axios({
    url: '/employee/login',
    method: 'post',
    data
  })
}

// 用户登出
function logoutApi(){
  return $axios({
    url: '/employee/logout',
    method: 'post',
  })
}

// 用户修改信息
function editUser(data) {
  return $axios({
    url: '/employee/edit',
    method: 'put',
    data
  })
}
