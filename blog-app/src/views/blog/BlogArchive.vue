<template>
  <div v-title :data-title="title">
    <el-container>
      <!--左侧：所有归档数据年 月 文章数-->
      <el-aside class="me-area  animate__animated animate__slideInLeft">
        <ul class="me-month-list">
          <li v-for="a in archives" :key="a.year + a.month" class="me-month-item">
            <el-badge :value="a.count">
              <el-button @click="changeArchive(a.year, a.month)" size="small">{{a.year +'年' + a.month + '月'}}
              </el-button>
            </el-badge>
          </li>
        </ul>
      </el-aside>
      <!--右侧 文章列表-->
      <el-main class="me-articles animate__animated animate__slideInRight">
        <div class="me-month-title">{{currentArchive}}</div>
        <!--传递查询条件-->
        <article-scroll-page v-bind="article"></article-scroll-page>
      </el-main>
    </el-container>
  </div>
</template>

<script>
  import ArticleScrollPage from '@/views/common/ArticleScrollPage'
  import {listArchives} from '@/api/article'
  export default {
    name: "BlogArchive",
    components: {
      ArticleScrollPage
    },
    created() {
      //vue实例创建的时候就获取 所有归档数据 年 月 文章数
      this.listArchives()
    },
    watch: {
      //监听路由发生变化 将查询条件进行变化
      '$route'() {
        if (this.$route.params.year && this.$route.params.month) {
          this.article.query.year = this.$route.params.year
          this.article.query.month = this.$route.params.month
        }
      }
    },
    data() {
      return {
        //查询条件
        article: {
          query: {
            month: this.$route.params.month,
            year: this.$route.params.year
          }
        },
        //归档文章数据数组 年 月 文章数
        archives: []
      }
    },
    computed: {
      //网站标题
      title (){
        return this.currentArchive + ' - 归档'
      },
      //当前所在具体归档 年 月还是全部
      currentArchive (){
        if(this.article.query.year && this.article.query.month){
          return `${this.article.query.year}年${this.article.query.month}月`
        }
        return '全部'
      }
    },
    methods: {
      //获取具体归档数据 改变路由
      changeArchive(year, month) {
        // this.currentArchive = `${year}年${month}月`
        // this.article.query.year = year
        // this.article.query.month = month
        this.$router.push({path: `/archives/${year}/${month}`})
      },
      //获取所有归档信息 年 月 文章数
      listArchives() {
        listArchives().then((data => {
          this.archives = data.data
        })).catch(error => {
          that.$message({type: 'error', message: '文章归档加载失败!', showClose: true})
        })
      }
    }
  }
</script>

<style scoped>

  .el-container {
    width: 80%;
  }

  .el-aside {
    /*position: fixed;*/
    left: 200px;
    margin-right: 50px;
    width: 150px !important;
  }

  .el-main {
    padding: 0px;
    line-height: 16px;
  }

  .me-month-list {
    margin-top: 10px;
    margin-bottom: 10px;
    text-align: center;
    list-style-type: none;
  }

  .me-month-item {
    margin-top: 18px;
    padding: 4px;
    font-size: 18px;
    color: #5FB878;
  }

  .me-order-list {
    float: right;
  }

  .me-month-title {
    margin-left: 4px;
    margin-bottom: 12px;
  }
</style>
