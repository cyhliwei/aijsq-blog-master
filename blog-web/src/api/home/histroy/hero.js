import request from '@/utils/request'
 
// 获取轮播图
export function getHeroCarouselApi() {
  return request({
    url: '/api/home/carousel',
    method: 'get'
  })
}
 
// 获取热门标签
export function getTrendingTagsApi() {
  return request({
    url: '/api/home/trending-tags',
    method: 'get'
  })
}
 
// 获取统计数据
export function getHomeStatsApi() {
  return request({
    url: '/api/home/stats',
    method: 'get'
  })
}