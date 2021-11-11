<template>

  <div id="register" v-title data-title="注册">
    <div class="background">
      <img :src="imgSrc" width="100%" height="100%" alt=""/>
    </div>

    <div class="me-login-box me-login-box-radius">
      <h1>注册</h1>

      <el-form ref="userForm" :model="userForm" :rules="registerRules">
        <el-form-item prop="account">
          <el-input placeholder="账号(请使用手机号或者邮箱)" v-model="userForm.account"></el-input>
        </el-form-item>
        <el-form-item prop="nickname">
          <el-input placeholder="昵称" v-model="userForm.nickname"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="密码" type="password" v-model="userForm.password"></el-input>
        </el-form-item>
        <el-form-item prop="surePassword">
          <el-input placeholder="确认密码" type="password" v-model="userForm.surePassword"></el-input>
        </el-form-item>
        <el-form-item size="small" class="me-login-button">
          <el-button type="primary" @click.native.prevent="register('userForm')">注册</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import {checkComment} from "../api/comment";

export default {
  name: 'Register',
  data() {
    //自定义注册验证
    var validateAccount = (rule, value, callback) => {
      const mailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/
      const phoneReg = /^1[3|4|5|7|8][0-9]{9}$/
      if (!value) {
        return callback(new Error('账号不能为空'))
      }
      setTimeout(() => {
        // Number.isInteger是es6验证数字是否为整数的方法,但是我实际用的时候输入的数字总是识别成字符串
        // 所以我就在前面加了一个+实现隐式转换
        if (phoneReg.test(value) || mailReg.test(value)) {
          callback()
        } else {
          callback(new Error('电话号码或者邮箱格式不正确'))
        }
      }, 100)
    };
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('密码不能为空'));
      } else if (value.length > 20) {
        callback(new Error('密码不能超过20位哦'));
      } else {
        if (this.userForm.surePassword !== '') {
          this.$refs.userForm.validateField('surePassword');
        }
        callback();
      }
    };
    var validateSurePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入再次输入密码'));
      } else if (value !== this.userForm.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      imgSrc: require('../assets/img/bg.jpg'),
      userForm: {
        account: '',
        nickname: '',
        password: '',
        surePassword: ''
      },
      registerRules: { //注册校验规则
        account: [
          {validator: validateAccount, trigger: 'blur'},
        ],
        nickname: [
          {required: true, message: '请输入昵称', trigger: 'blur'},
          {max: 10, message: '不能大于10个字符', trigger: 'blur'}
        ],
        password: [
          {validator: validatePass, trigger: 'blur'},

        ],
        surePassword: [
          {validator: validateSurePass, trigger: 'blur'},
        ]
      },
    }
  },
  methods: {
    register(formName) {
      let that = this
      this.$refs[formName].validate((valid) => {
        //信息完整
        if (valid) {
          //审核昵称
          var text = that.userForm.nickname
          checkComment(text).then(data => {
            if (data.success) {
              //合格
              if (data.data.conclusionType == 1) {

                that.$message({type: 'success', message: '审核通过', showClose: true})
                //调用store/index.js中register方法进行注册
                that.$store.dispatch('register', that.userForm).then(() => {
                  that.$message({message: '注册成功可以登录评论啦', type: 'success', showClose: true});
                  that.$router.push({path: '/'})
                }).catch((error) => {
                  if (error !== 'error') {
                    that.$message({message: error, type: 'error', showClose: true});
                  }
                })
              }
              //不合格
              else {
                that.$message({type: 'error', message: "昵称不合规", showClose: true})
                return;
              }
            } else {
              that.$message({type: 'error', message: data.msg, showClose: true})
              return;
            }
          })
        }
        //信息不完整
        else {
          that.$message.warning("请检查信息是否正确")
          return false;
        }
      });

    }

  }
}
</script>

<style scoped>
.background {
  width: 100%;
  height: 100%; /**宽高100%是为了图片铺满屏幕 */
  z-index: -1;
  position: absolute;
}

#login {
  min-width: 100%;
  min-height: 100%;
}

.me-video-player {
  background-color: transparent;
  width: 100%;
  height: 100%;
  object-fit: fill;
  display: block;
  position: absolute;
  left: 0;
  z-index: 0;
  top: 0;
}

.me-login-box {
  z-index: 1;
  position: absolute;
  width: 300px;
  height: 320px;
  background-color: white;
  margin-top: 150px;
  margin-left: -180px;
  left: 50%;
  padding: 30px;
}

.me-login-box-radius {
  border-radius: 10px;
  box-shadow: 0px 0px 1px 1px rgba(161, 159, 159, 0.1);
}

.me-login-box h1 {
  text-align: center;
  font-size: 24px;
  margin-bottom: 20px;
  vertical-align: middle;
}

.me-login-design {
  text-align: center;
  font-family: 'Open Sans', sans-serif;
  font-size: 18px;
}

.me-login-design-color {
  color: #5FB878 !important;
}

.me-login-button {
  text-align: center;
}

.me-login-button button {
  width: 100%;
}

</style>
