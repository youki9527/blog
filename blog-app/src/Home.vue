<template>

  <div id="home">
    <!--背景图片-->
    <div class="background">
      <img :src="imgSrc" width="100%" height="100%" alt=""/>
    </div>
    <el-container class="me-home-container">
      <base-header :activeIndex="activeIndex"></base-header>
      <router-view class="me-container"/>
      <base-footer v-show="footerShow"></base-footer>
    </el-container>
  </div>
</template>

<script>
import BaseFooter from '@/views/BaseFooter'
import BaseHeader from '@/views/BaseHeader'

export default {
  name: 'Home',
  data() {
    return {
      imgSrc: require('./assets/img/bg.jpg'),
      activeIndex: '/',
      footerShow:true
    }
  },
  components: {
    'base-header': BaseHeader,
    'base-footer': BaseFooter
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.activeIndex = to.path
    })
  },
  beforeRouteUpdate(to, from, next) {
    // if (to.path == '/aboutme') {
    //   this.footerShow = false
    // } else {
    //   this.footerShow = true
    // }
    this.activeIndex = to.path
    next()
  }
}
</script>

<style>

#home {
  display: flex;
  min-height: 100vh;
  flex-direction: column;
}

.open-info{
  height: 100%;
  width: 100%;
}
/**
全局container
*/
.me-home-container{
  min-height: 100%;
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
  align-content: flex-end;

}

/**
背景图片样式
**/
.background {
  width: 100%;
  height: 100%; /**宽高100%是为了图片铺满屏幕 */
  z-index: -1;
  position: absolute;
}

/**
中间内容样式
**/
.me-container {
  width: 65%;
  display: block;
  margin: 100px auto 100px;
}


</style>
