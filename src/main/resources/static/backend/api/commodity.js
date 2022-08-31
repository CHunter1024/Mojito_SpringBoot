// 分页查询商品信息
const getCommodityPage = (params) => {
  return $axios({
    url: '/commodity/page',
    method: 'get',
    params
  })
}

// 删除商品
const deleteCommodity = (data) => {
  return $axios({
    url: '/commodity',
    method: 'delete',
    data
  })
}

// 修改商品
const editCommodity = (data) => {
  return $axios({
    url: '/commodity',
    method: 'put',
    data
  })
}

// 新增商品
const addCommodity = (data) => {
  return $axios({
    url: '/commodity',
    method: 'post',
    data
  })
}

// 根据id查询商品信息
const getCommodityById = (id) => {
  return $axios({
    url: `/commodity/${id}`,
    method: 'get'
  })
}

// 获取分类信息
const getCategoryList = (params) => {
  return $axios({
    url: '/category/list',
    method: 'get',
    params
  })
}

// 获取商品信息
const getCommodityList = (params) => {
  return $axios({
    url: '/commodity/list',
    method: 'get',
    params
  })
}

// 修改商品状态
const updateCommodityStatus = (params) => {
  return $axios({
    url: `/commodity/${params.status}`,
    method: 'put',
    data: params.ids
  })
}