import request from '@/utils/request'
 
/**
 * 获取热门工具列表
 */
export function getHotToolsApi(params) {
  return request({
    url: '/api/home/tools/hot',
    method: 'get',
    params
  })
}
 
/**
 * 获取工具分类
 */
export function getToolCategoriesApi() {
  return request({
    url: '/api/home/tools/categories',
    method: 'get'
  })
}
 
/**
 * 获取工具详情
 */
export function getToolDetailApi(id) {
  return request({
    url: `/api/home/tools/${id}`,
    method: 'get'
  })
}