import request from '@/utils/request'
 
// 获取新闻分类
export function getNewsCategoriesApi() {
  return request({
    url: '/api/home/news/categories',
    method: 'get'
  })
}
 
// 获取新闻列表
export function getNewsListApi(params) {
  return request({
    url: '/api/home/news/list',
    method: 'get',
    params
  })
}
 
// 获取热门话题
export function getTrendingTopicsApi() {
  return request({
    url: '/api/home/news/trending-topics',
    method: 'get'
  })
}