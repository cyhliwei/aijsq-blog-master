import request from '@/utils/request'

/**
 * 获取AI工具列表
 */
export function getAIToolsApi(params) {
  return request({
    url: '/api/ai-tool/list',
    method: 'get',
    params: params
  })
}

/**
 * 获取热门AI工具
 */
export function getPopularAIToolsApi() {
  return request({
    url: '/api/ai-tool/popular',
    method: 'get'
  })
}

/**
 * 获取AI工具详情
 */
export function getAIToolDetailApi(id) {
  return request({
    url: `/api/ai-tool/detail/${id}`,
    method: 'get'
  })
}

/**
 * 获取工具教程
 */
export function getAIToolTutorialsApi(toolId) {
  return request({
    url: `/api/ai-tool/${toolId}/tutorials`,
    method: 'get'
  })
}