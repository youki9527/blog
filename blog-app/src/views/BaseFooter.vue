<template>
  <footer class="me-footer">
    <div class="info-blog">
      <el-row :gutter="20" class="me-el-row">
        <el-col :span="6" class="me-el-col">
          <div class="grid-content">
            <div class="img">
              <img src="../assets/img/footer.png" class="" alt="" style="width: 120px">
            </div>
          </div>
          <el-divider direction="vertical"></el-divider>
        </el-col>

        <el-col :span="6" class="me-el-col">
          <h4 class="grid-title">最新博客</h4>
          <div class="grid-content">
            <a v-for="a in  newArticles" @click="view(a.id)" :style="itemStyle" :key="a.id"
               class="nav-item">{{ a.title }}</a>
          </div>
          <el-divider direction="vertical"></el-divider>
        </el-col>

        <el-col :span="6" class="me-el-col">
          <h4 class="grid-title">联系我</h4>
          <div class="grid-content">
            <p class="grid-item">qq:291821441</p>
            <p class="grid-item">邮箱:291821441@qq.xom</p>
          </div>
        </el-col>

        <el-col :span="6">
          <h4 class="grid-title">Blog</h4>
          <div class="grid-content">
            <p class="grid-item">
              知道的少了,也就知道的多了
            </p>
            <p class="grid-item">
              知道的多了,也就知道的少了
            </p>
          </div>
        </el-col>
      </el-row>
    </div>

    <div class="info-beian">
      <a href="https://beian.miit.gov.cn/#/Integrated/index" class="beian" style="hover:#2b2b2b">陕ICP备2021012054号</a>
    </div>
  </footer>

</template>

<script>
import {getNewArtices} from '@/api/article'

export default {
  name: 'BaseFooter',
  data() {
    return {
      newArticles: {}
    }
  },
  methods: {
    getNewArtices() {
      let that = this
      getNewArtices().then(data => {
        that.newArticles = data.data
      }).catch(error => {
        if (error !== 'error') {
          that.$message({type: 'error', message: '最新文章加载失败!', showClose: true})
        }

      })
    },
    view(id) {
      this.$router.push({path: `/view/${id}`})
    }
  },
  mounted() {
    this.getNewArtices();
  }
}
</script>

<style>

.me-footer {

  margin-top: auto;
  width: 100%;
  background: #222222;
  height: 200px;
  text-align: center;
  min-height: 20px;
  max-width: 100%;
  padding: 0 15px;
  bottom: 0;
  opacity: 0.9;
}

.me-login-design-color {
  color: #5FB878 !important;
}

.info-blog {
  display: block;
}

.me-el-row {
  height: 120px;
  margin-top: 15px;
  margin-bottom: 20px;

:last-child {
  margin-bottom: 0;
}

}

.me-el-col {
  border-right: 1px solid gray;
}

.grid-title {
  margin-top: 18px;
  color: #ffffff;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
  display: flex;
  flex-direction: column;
  justify-content: center; /*主轴上居中*/
  align-items: center; /*侧轴上居中*/
}

.img {
  margin-top: 25px;
}

.grid-item {
  margin-top: 5px;
  color: #a29892;
  display: block;
}

.nav-item {
  margin-top: 5px;
  color: #a29892;
  display: block;
  hover: #a29892 !important;
}

.info-beian {
  margin-top: 40px;
  display: block;
}

.beian {
  color: gray;
}
</style>
