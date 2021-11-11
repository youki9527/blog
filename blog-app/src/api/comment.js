import request from '@/request'

//获取评论列表
export function getCommentsByArticle(id) {
  return request({
    url: `/comments/article/${id}`,
    method: 'get'
  })
}

// 评论审核
export function checkComment(text) {
  return request({
    url: '/check/text',
    method: 'post',
    data: text
  })
}

//发布评论
export function publishComment(comment, token) {
  return request({
    headers: {'Authorization': token},
    url: '/comments/create/change',
    method: 'post',
    data: comment
  })
}
