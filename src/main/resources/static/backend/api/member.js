// 分页查询员工信息
function getMemberPage(params) {
  return $axios({
    url: '/employee/page',
    method: 'get',
    params
  })
}

// 新增员工
function addEmployee(data) {
  return $axios({
    url: '/employee',
    method: 'post',
    data
  })
}

// 修改员工
function editEmployee(data) {
  return $axios({
    url: '/employee',
    method: 'put',
    data
  })
}

// 根据id查询员工信息
function getEmployeeById(id) {
  return $axios({
    url: `/employee/${id}`,
    method: 'get'
  })
}