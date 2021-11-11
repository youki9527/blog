<template>
  <div id="app">
    <!--&lt;!&ndash;背景图片&ndash;&gt;-->
    <!--<div class="background">-->
    <!--  <img :src="imgSrc" width="100%" height="100%" alt=""/>-->
    <!--</div>-->
    <router-view/>
    <go-top></go-top>
  </div>
</template>

<script>
import GoTop from '@/components/gotop/GoTop'

export default {
  name: 'App',
  components: {GoTop},
  data() {
    return {
      imgSrc: require('./assets/img/bg.jpg')
    }
  }
}
</script>

<style>

#app {
  min-height: 100%;
  width: 100%;
}


* {
  margin: 0;
  padding: 0;
}

body {
  background-color: #f5f5f5;
  font-weight: 400;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  line-height: 1.5;
}

body {
  min-width: 100%;
  min-height: 100%;
}

body {
  position: relative;
}
html {
  height: 100%;
}

a {
  cursor: pointer;
  text-decoration: none;
  transition: none 86ms ease-out;
}

a:hover {
  color: #5FB878;
}

.me-area {
  background-color: #fff;
}

.me-pull-right {
  float: right;
}


</style>
