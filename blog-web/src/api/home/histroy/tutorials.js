import request from '@/utils/request'
 
/**
 * 获取推荐教程列表
 */
export function getFeaturedTutorialsApi(params) {
  return request({
    url: '/api/home/tutorials/featured',
    method: 'get',
    params
  })
}
 
/**
 * 获取教程分类
 */
export function getTutorialCategoriesApi() {
  return request({
    url: '/api/home/tutorials/categories',
    method: 'get'
  })
}
 
/**
 * 获取教程详情
 */
export function getTutorialDetailApi(id) {
  return request({
    url: `/api/home/tutorials/${id}`,
    method: 'get'
  })
}