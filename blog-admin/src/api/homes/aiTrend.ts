import request from '@/utils/request'

/**
 * 获取AI趋势表列表
 */
export function getAiTrendListApi(params?: any) {
    return request({
        url: '/sys/sysAiTrend/list',
        method: 'get',
        params
    })
}

/**
 * 获取AI趋势表详情
 */
export function getAiTrendDetailApi(id: any) {
    return request({
        url: '/sys/sysAiTrend/' + id,
        method: 'get'
    })
}

/**
 * 添加AI趋势表
 */
export function addAiTrendApi(data: any) {
    return request({
        url: '/sys/sysAiTrend/add',
        method: 'post',
        data
    })
}
export function exportAiTrendApi(data: any) {
    return request({
        url: '/sys/sysAiTrend/add',
        method: 'post',
        data
    })
}
/**
 * 修改AI趋势表
 */
export function updateAiTrendApi(data: any) {
    return request({
        url: `/sys/sysAiTrend/update`,
        method: 'put',
        data
    })
}


/**
 * 删除AI趋势表
 */
export function deleteAiTrendApi(ids: number[] | number) {
    return request({
        url: `/sys/sysAiTrend/delete/` + ids,
        method: 'delete'
    })
}

/**
 *批量删除AI趋势表
 */
export function batchDeleteAiTrendApi(ids: number[] | number) {
    return request({
        url: `/sys/sysAiTrend/delete/` + ids,
        method: 'delete'
    })
}

