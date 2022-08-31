// 分页查询套餐信息
const getComboPage = (params) => {
  return $axios({
    url: '/combo/page',
    method: 'get',
    params
  })
}

// 删除套餐
const deleteCombo = (data) => {
  return $axios({
    url: '/combo',
    method: 'delete',
    data
  })
}

// 修改套餐
const editCombo = (data) => {
  return $axios({
    url: '/combo',
    method: 'put',
    data
  })
}

// 新增套餐
const addCombo = (data) => {
  return $axios({
    url: '/combo',
    method: 'post',
    data
  })
}

// 根据id查询套餐信息
const getComboById = (id) => {
  return $axios({
    url: `/combo/${id}`,
    method: 'get'
  })
}

// 修改套餐状态
const updateComboStatus = (params) => {
  return $axios({
    url: `/combo/${params.status}`,
    method: 'put',
    data: params.ids
  })
}
