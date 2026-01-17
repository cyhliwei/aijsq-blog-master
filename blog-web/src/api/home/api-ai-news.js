import request from '@/utils/request'

/**
 * 获取AI热点新闻列表
 */
export function getAINewsListApi(params) {
  return request({
    url: '/api/home/ai-news',
    method: 'get',
    params: params
  })
}

/**
 * 获取新闻分类
 */
export function getAINewsCategoriesApi() {
  return request({
    url: '/api/home/aiNewsCategories',
    method: 'get'
  })
}

/**
 * 获取热门话题
 */
export function getTrendingTopicsApi() {
  return request({
    url: '/api/home/aiNewsTopics',
    method: 'get'
  })
}

/**
 * 获取AI热点详情
 */
export function getAINewsDetailApi(id) {
  return request({
    url: `/api/ai-news/detail/${id}`,
    method: 'get'
  })
}