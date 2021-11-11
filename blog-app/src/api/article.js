import request from '@/request'
//获取所有文章列表数据-
export function getArticles(query, page) {
  return request({
    url: '/articles',
    method: 'post',
    data: {
      currentPage: page.currentPage,
      pageSize: page.pageSize,
      // name: page.name,
      // sort: page.sort,
      year: query.year,
      month: query.month,
      tagId: query.tagId,
      categoryId: query.categoryId
    }
  })
}
//获取最热文章
export function getHotArtices() {
  return request({
    url: '/articles/hot',
    method: 'post'
  })
}
//获取最新文章
export function getNewArtices() {
  return request({
    url: '/articles/new',
    method: 'post'
  })
}
//获取文章详细信息
export function viewArticle(id) {
  return request({
    url: `/articles/view/${id}`,
    method: 'post'
  })
}

export function getArticlesByCategory(id) {
  return request({
    url: `/articles/category/${id}`,
    method: 'post'
  })
}

export function getArticlesByTag(id) {
  return request({
    url: `/articles/tag/${id}`,
    method: 'post'
  })
}


export function publishArticle(article,token) {
  return request({
    headers: {'Authorization': token},
    url: '/articles/publish',
    method: 'post',
    data: article
  })
}

export function listArchives() {
  return request({
    url: '/articles/listArchives',
    method: 'post'
  })
}

export function getArticleById(id) {
  return request({
    url: `/articles/${id}`,
    method: 'post'
  })
}
