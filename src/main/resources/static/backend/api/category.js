// 分页查询分类信息
const getCategoryPage = (params) => {
  return $axios({
    url: '/category/page',
    method: 'get',
    params
  })
}

// 删除分类
const deleteCategory = (id) => {
  return $axios({
    url: `/category/${id}`,
    method: 'delete',
  })
}

// 修改分类
const editCategory = (data) => {
  return $axios({
    url: '/category',
    method: 'put',
    data
  })
}

// 新增分类
const addCategory = (data) => {
  return $axios({
    url: '/category',
    method: 'post',
    data
  })
}