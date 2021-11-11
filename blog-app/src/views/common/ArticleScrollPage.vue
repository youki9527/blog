<template>
  <scroll-page :loading="loading" :offset="offset" :no-data="noData" v-on:load="load">
    <article-item v-for="a in articles" :key="a.id" v-bind="a"></article-item>
    <div class="pagination-container">
      <el-pagination
        class="pagination"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.currentPage"
        :page-sizes="[1,2,3,4,5,6,7,8,9,10]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total">
      </el-pagination>
    </div>
  </scroll-page>
</template>

<script>
import ArticleItem from '@/components/article/ArticleItem'
import ScrollPage from '@/components/scrollpage/Scrollpage'
import {getArticles} from '@/api/article'

export default {
  name: "ArticleScrollPage",
  props: {
    offset: {
      type: Number,
      default: 100
    },
    //分页参数
    page: {
      type: Object,
      default() {
        return {}
      }
    },
    //查询条件
    query: {
      type: Object,
      default() {
        return {}
      }
    }
  },
  watch: {
    'query': {
      handler() {
        this.noData = false
        this.articles = []
        this.pagination.currentPage = 1
        this.getArticles()
      },
      deep: true
    },
    // 'page': {
    //   handler() {
    //     this.noData = false
    //     this.articles = []
    //     this.pagination = this.page
    //     this.getArticles()
    //   },
    //   deep: true
    // }
  },
  created() {
    this.getArticles()
  },
  data() {
    return {
      loading: false,
      noData: false,
      pagination: {
        currentPage: 1,//当前页码
        pageSize: 5,//每页显示的记录数
        total: 0,
      },

      articles: []
    }
  },
  methods: {
    load() {
      this.getArticles()
    },
    //跳转到详细页面
    view(id) {
      this.$router.push({path: `/view/${id}`})
    },
    //切换页面大小
    handleSizeChange(pageSize) {
      this.pagination.pageSize = pageSize;
      this.pagination.currentPage = 1;
      this.getArticles()
    },
    //切换页码
    handleCurrentChange(currentPage) {
      this.pagination.currentPage = currentPage;
      this.getArticles()
    },
    //获取文章列表
    getArticles() {
      let that = this
      that.loading = true
      getArticles(that.query, that.pagination).then(data => {
        let newArticles = data.data.list
        if (newArticles && newArticles.length > 0) {
          // that.innerPage.pageNumber += 1
          // that.articles = that.articles.concat(newArticles)
          that.articles = newArticles;
          that.pagination.total=data.data.total;

        } else {
          that.noData = true
        }

      }).catch(error => {
        if (error !== 'error') {
          that.$message({type: 'error', message: '文章加载失败!', showClose: true})
        }
      }).finally(() => {
        that.loading = false
      })

    }
  },
  components: {
    'article-item': ArticleItem,
    'scroll-page': ScrollPage
  }

}
</script>

<style scoped>
/*.el-card {*/
/*  border-radius: 0;*/
/*}*/

/*.el-card:not(:first-child) {*/
/*  margin-top: 10px;*/

/*}*/
.pagination-container{
  margin-top: 15px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

/deep/ .pagination .el-pagination__sizes {
  float: left;
}

/deep/ .pagination .el-pagination__total {
  color:#9ec8a6;
  float: left;
}

/deep/ .pagination .el-pagination__jump {
  float: left;
}

</style>
