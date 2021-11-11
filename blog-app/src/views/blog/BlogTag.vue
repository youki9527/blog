<template>
  <div class="me-allct-body animate__animated  animate__slideInDown" v-title :data-title="TagTitle" >
    <el-container class="me-allct-container">
      <el-main>
            <ul class="me-allct-items">
              <li v-for="t in tags" @click="view(t.id)" :key="t.id" class="me-allct-item">
                <div class="me-allct-content">
                  <a class="me-allct-info">
                    <img class="me-allct-img" :src="t.avatar?t.avatar:defaultAvatar"/>
                    <h4 class="me-allct-name">{{t.tagName}}</h4>
                  </a>
                  <div class="me-allct-meta">
                    <span>{{t.articles}}篇文章</span>
                  </div>
                </div>
              </li>
            </ul>
      </el-main>
    </el-container>
  </div>
</template>

<script>
  import defaultAvatar from '@/assets/img/logo.jpg'
  import {getAllTagsDetail} from '@/api/tag'

  export default {
    name: 'BlogTag',
    created() {
      this.getTags()
    },
    data() {
      return {
        defaultAvatar:defaultAvatar,
        tags: [],
        currentActiveName: 'tag'
      }
    },
    computed: {
      TagTitle (){
        return '标签'
      }
    },
    methods: {
      //获取某个标签的文章列表
      view(id) {
        this.$router.push({path: `/${this.currentActiveName}/${id}`})
      },
      //获取所有标签的详细和对应文章数量
      getTags() {
        let that = this
        getAllTagsDetail().then(data => {
          that.tags = data.data
        }).catch(error => {
          if (error !== 'error') {
            that.$message({type: 'error', message: '标签加载失败', showClose: true})
          }
        })
      }
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
  .me-allct-body {
    margin: 60px auto 140px;
  }

  .me-allct-container {
    width: 1000px;
  }

  .me-allct-items {
    padding-top: 2rem;
  }

  .me-allct-item {
    width: 25%;
    display: inline-block;
    margin-bottom: 2.4rem;
    padding: 0 .7rem;
    box-sizing: border-box;
  }

  .me-allct-content {
    display: inline-block;
    width: 100%;
    /*background-color: #fff;*/
    /*border: 1px solid #f1f1f1;*/
    transition: border-color .3s;
    text-align: center;
    padding: 1.5rem 0;
  }

  .me-allct-info {
    cursor: pointer;
  }

  .me-allct-img {
    margin: -40px 0 10px;
    width: 60px;
    height: 60px;
    vertical-align: middle;

  }

  .me-allct-name {
    font-size: 21px;
    font-weight: 150;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    margin-top: 4px;
  }

  .me-allct-description {
    min-height: 50px;
    font-size: 13px;
    line-height: 25px;
  }

  .me-allct-meta {
    font-size: 12px;
    color: #969696;
  }
</style>
