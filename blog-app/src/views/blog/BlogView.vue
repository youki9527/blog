<template>
  <div class="me-view-body  animate__animated  animate__backInDown" v-title :data-title="title">
    <el-container class="me-view-container">
      <el-main>
        <div class="me-view-card">
          <h1 class="me-view-title">{{ article.title }}</h1>
          <div class="me-view-author">
            <a class="">
              <img class="me-view-picture" :src="article.author.avatar"></img>
            </a>
            <div class="me-view-info">
              <span>{{ article.author.nickname }}</span>
              <div class="me-view-meta">
                <span>{{ article.createDate | format }}</span>
                <span>阅读   {{ article.viewCounts }}</span>
                <span>评论   {{ article.commentCounts }}</span>
              </div>
            </div>
          </div>
          <!--<div class="me-view-content">-->
          <!--  <markdown-editor :editor=article.editor></markdown-editor>-->
          <!--</div>-->
          <div >
            <markedown-view :blogcontent=article.editor.value></markedown-view>
          </div>
          <div class="me-view-end">
            <el-alert
              title="文章End..."
              type="success"
              center
              :closable="false">
            </el-alert>
          </div>
          <div class="me-view-tag">
            标签：
            <el-button @click="tagOrCategory('tag', t.id)" size="mini" type="primary" v-for="t in article.tags"
                       :key="t.id" round plain>{{ t.tagName }}
            </el-button>
          </div>
          <div class="me-view-tag">
            文章分类：
            <el-button @click="tagOrCategory('category', article.category.id)" size="mini" type="primary" round plain>
              {{ article.category.categoryName }}
            </el-button>
          </div>
          <div class="me-view-comment">
            <div class="me-view-comment-write">
              <el-row :gutter="20">
                <el-col :span="2">
                  <a class="">
                    <img class="me-view-picture" :src="avatar"></img>
                  </a>
                </el-col>
                <el-col :span="22">
                  <el-input
                    type="textarea"
                    :autosize="{ minRows: 2}"
                    placeholder="你的评论..."
                    class="me-view-comment-text"
                    v-model="comment.content"
                    resize="none">
                  </el-input>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="2" :offset="22">
                  <el-button type="text" @click="publishComment()">评论</el-button>
                </el-col>
              </el-row>
            </div>
            <div class="me-view-comment-title">
              <span>{{ article.commentCounts }} 条评论</span>
            </div>
            <commment-item
              v-for="(c,index) in comments"
              :comment="c"
              :articleId="article.id"
              :index="index"
              :rootCommentCounts="comments.length"
              @commentCountsIncrement="commentCountsIncrement"
              :key="c.id">
            </commment-item>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import MarkdownEditor from '@/components/markdown/MarkdownEditor'
import CommmentItem from '@/components/comment/CommentItem'
import {viewArticle} from '@/api/article'
import {getCommentsByArticle,publishComment} from '@/api/comment'
import default_avatar from '@/assets/img/default_avatar.png'
import {checkComment} from "../../api/comment";
import MarkedownView from "../../components/markdown/MarkdownView";
export default {
  name: 'BlogView',
  created() {
    this.getArticle()
  },
  watch: {
    '$route': 'getArticle'
  },
  data() {
    return {
      article: {
        id: '',
        title: '',
        commentCounts: 0,
        viewCounts: 0,
        summary: '',
        author: {},
        tags: [],
        category: {},
        createDate: '',
        editor: {
          value: '',
          toolbarsFlag: false,
          subfield: false,
          defaultOpen: 'preview'
        }
      },
      comments: [],
      //记录发布评论
      comment: {
        article: {},
        content: ''
      },
    }
  },
  computed: {
    title() {
      return `${this.article.title} - 文章 `
    }
  },
  methods: {
    //跳转到具体分类或者标签页面文章列表
    tagOrCategory(type, id) {
      this.$router.push({path: `/${type}/${id}`})
    },
    //获取文章
    getArticle() {
      let that = this
      viewArticle(that.$route.params.id).then(data => {
        Object.assign(that.article, data.data)
        that.article.editor.value = data.data.body.content
        //获取评论
        that.getCommentsByArticle()
      }).catch(error => {
        if (error !== 'error') {
          that.$message({type: 'error', message: '文章加载失败', showClose: true})
        }
      })
    },
    //发布评论
    publishComment() {
      let that = this
      if (!this.$store.state.token){
        that.$message({type: 'error', message: '未登录token过期', showClose: true})
        return;
      }
      if (!that.comment.content) {
        that.$message({type: 'error', message: '评论内容不能为空', showClose: true})
        return;
      }
      // 审核评论
      var text=that.comment.content
      checkComment(text).then(data => {
        if (data.success) {
          console.log()
          //合格
          if(data.data.conclusionType==1){
            that.$message({type: 'success', message: '评论审核通过', showClose: true})
            that.comment.article.id = that.article.id
            //设置请求参数
            let parms = {articleId: that.article.id, content: that.comment.content}
            publishComment(parms, this.$store.state.token).then(data => {
              if (data.success) {
                that.$message({type: 'success', message: '评论成功', showClose: true})
                that.comment.content = ''
                that.comments.unshift(data.data)
                that.commentCountsIncrement()
              } else {
                that.$message({type: 'error', message: data.msg, showClose: true})
              }
            }).catch(error => {
              if (error !== 'error') {
                that.$message({type: 'error', message: '评论失败', showClose: true})
              }
            })
          } else {
            that.$message({type: 'success', message: "评论不合规", showClose: true})

          }
        } else {
          that.$message({type: 'success', message: data.msg, showClose: true})
        }
      })

    },
    //获取评论信息
    getCommentsByArticle() {
      let that = this
      getCommentsByArticle(that.article.id).then(data => {
        if (data.success) {
          that.comments = data.data
        } else {
          that.$message({type: 'error', message: '评论加载失败', showClose: true})
        }
      }).catch(error => {
        if (error !== 'error') {
          that.$message({type: 'error', message: '评论加载失败', showClose: true})
        }
      })
    },
    commentCountsIncrement() {
      this.article.commentCounts += 1
    }
  },
  components: {
    'markedown-view':MarkedownView,
    'markdown-editor': MarkdownEditor,
    CommmentItem
  },
  //组件内的守卫 调整body的背景色
  beforeRouteEnter(to, from, next) {
    window.document.body.style.backgroundColor = '#fff';
    next();
  },
  beforeRouteLeave(to, from, next) {
    window.document.body.style.backgroundColor = '#f5f5f5';
    next();
  }
}
</script>

<style>
body {
  /*background: url("./assets/img/bg.jpg");*/
  background-color: #f5f5f5 !important;
  font-weight: 400;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  line-height: 1.5;
}

.me-view-body {
  width: 65%;
  background:#f5f5f5!important;
  margin: 100px auto 140px;
}

.me-view-container {
  margin: auto;
  width:90%;
}

.el-main {
  overflow: hidden;
}

.me-view-title {
  font-size: 34px;
  font-weight: 800;
  line-height: 1.3;
}

.me-view-author {
  /*margin: 30px 0;*/
  margin-top: 30px;
  vertical-align: middle;
}

.me-view-picture {
  width: 40px;
  height: 40px;
  border: 1px solid #ddd;
  border-radius: 50%;
  vertical-align: middle;
  background-color: #5fb878;
}

.me-view-info {
  display: inline-block;
  vertical-align: middle;
  margin-left: 8px;
}

.me-view-meta {
  font-size: 12px;
  color: #969696;
}

.me-view-end {
  margin-top: 20px;
}

.me-view-tag {
  margin-top: 20px;
  padding-left: 6px;
  border-left: 4px solid #c5cac3;
}

.me-view-tag-item {
  margin: 0 4px;
}

.me-view-comment {
  margin-top: 60px;
}

.me-view-comment-title {
  font-weight: 600;
  border-bottom: 1px solid #f0f0f0;
  padding-bottom: 20px;
}

.me-view-comment-write {
  margin-top: 20px;
}

.me-view-comment-text {
  font-size: 16px;
}

.v-show-content {
  padding: 8px 25px 15px 30px !important;
}

.v-note-wrapper .v-note-panel {
  box-shadow: none !important;
}

.v-note-wrapper .v-note-panel .v-note-show .v-show-content, .v-note-wrapper .v-note-panel .v-note-show .v-show-content-html {
  background: #fff !important;
}


</style>
