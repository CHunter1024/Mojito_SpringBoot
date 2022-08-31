// 帐号校验规则
function isAccount(account) {
  return /^[a-zA-Z][a-zA-Z0-9_]{4,15}$/.test(account)
}
// 手机号检验规则
function isPhoneNumber(phoneNumber) {
  return /^1(3|4|5|6|7|8)\d{9}$/.test(phoneNumber)
}
// 密码校验规则
function isPassword(password) {
  return /^(?=.*?[a-zA-Z])(?=.*?[0-9]).{6,18}$/.test(password)
}
// 姓名校验规则
function isUsername(username) {
  return /^.{1,12}$/.test(username)
}

// 校验帐号
function checkAccount(rule, value, callback) {
  if (value.length < 1) {
    callback(new Error('请输入帐号'))
  } else if (!isAccount(value)) {
    callback(new Error('帐号需字母开头，长度5-16，允许字母数字下划线'))
  } else {
    exist('account', value).then(res => {
      if (res.code === 1) {
        if (res.data) {
          callback(new Error('帐号已存在'))
        } else {
          callback()
        }
      }
    })
  }
}
// 校验密码
function checkPassword(rule, value, callback) {
  if (value.length < 1) {
    callback(new Error('请输入密码'))
  } else if (!isPassword(value)) {
    callback(new Error('密码需包含数字和字母,且长度6-18'))
  } else {
    callback()
  }
}
// 校验手机号码
function checkPhoneNumber(rule, value, callback) {
  if (value.length < 1) {
    callback(new Error('请输入手机号码'))
  } else if (!isPhoneNumber(value)) {
    callback(new Error('请输入正确的手机号码'))
  } else {
    exist('phone_number', value).then(res => {
      if (res.code === 1) {
        if (res.data) {
          callback(new Error('该手机号码已被注册'))
        } else {
          callback()
        }
      }
    })
  }
}
// 校验姓名
function checkUsername(rule, value, callback) {
  if (value.length < 1) {
    callback(new Error('请输入员工姓名'))
  } else if (!isUsername(value)) {
    callback(new Error('姓名长度应是1-12'))
  } else {
    callback()
  }
}


// 查询指定属性值是否已存在-接口
function exist(field, value) {
  return $axios({
    url: '/employee/exist',
    method: 'get',
    params: { field, value }
  })
}