import request from '@/utils/request'

/**
 * 获取学习路径列表
 */
export function getLearningPathsApi() {
  return request({
    url: '/api/learning-path/list',
    method: 'get'
  })
}

/**
 * 获取学习进度
 */
export function getLearningProgressApi(userId) {
  return request({
    url: '/api/learning-path/progress',
    method: 'get',
    params: { userId }
  })
}

/**
 * 开始技能评估
 */
export function startSkillAssessmentApi(data) {
  return request({
    url: '/api/learning-path/assessment/start',
    method: 'post',
    data
  })
}