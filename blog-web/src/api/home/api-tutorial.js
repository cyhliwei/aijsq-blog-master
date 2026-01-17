import request from '@/utils/request'

/**
 * 获取教程列表
 */
export function getTutorialsApi(params) {
  return request({
    url: '/api/tutorial/list',
    method: 'get',
    params: params
  })
}

/**
 * 获取热门教程
 */
export function getPopularTutorialsApi() {
  return request({
    url: '/api/home/tutorial/popular',
    method: 'get'
  })
}

/**
 * 获取教程分类
 */
export function getTutorialCategoriesApi() {
  return request({
    url: '/api/home/tutorial/categories',
    method: 'get'
  })
}

/**
 * 获取教程详情
 */
export function getTutorialDetailApi(id) {
  return request({
    url: `/api/tutorial/detail/${id}`,
    method: 'get'
  })
}