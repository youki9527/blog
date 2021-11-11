<template>

  <div v-title data-title="关于我">
    <el-container class="about-me-con">
      <h2 class="info">Youki</h2>
      <h4 class="info animate__animated  animate__lightSpeedInRight">即将毕业的软件工程程学生&Java后端开发</h4>
    </el-container>
  </div>

</template>

<script>
export default {
  name: "aboutmeinfo"
}
</script>

<style scoped>
.about-me-con{
flex-direction: column;
}
.info{
  display: block;
  color: #16a7ad;
}
</style>
