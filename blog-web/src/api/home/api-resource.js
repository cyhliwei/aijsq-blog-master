import request from '@/utils/request'

/**
 * 获取资源列表
 */
export function getResourcesApi(params) {
  return request({
    url: '/api/resource/list',
    method: 'get',
    params: params
  })
}

/**
 * 获取热门资源
 */
export function getPopularResourcesApi() {
  return request({
    url: '/api/resource/popular',
    method: 'get'
  })
}

/**
 * 下载资源
 */
export function downloadResourceApi(id) {
  return request({
    url: `/api/resource/download/${id}`,
    method: 'get'
  })
}