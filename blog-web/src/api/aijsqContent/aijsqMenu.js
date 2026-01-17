import request from '@/utils/request'

export function getMenuListApi(params) {
  return request({
    url: '/sys/aijsqMenu/tree',
    method: 'get',
    params: params
  })
}
// 获取文章详情
export function getDetailByMenuIdApi(menuId) {
  return request({
    url: `/api/article/detailByMenuId/${menuId}`,
    method: 'get',
  })
}