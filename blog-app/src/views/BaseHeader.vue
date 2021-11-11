<template>
  <el-header class="me-header">
    <el-row >
      <!--网站logo-->
      <el-col  class="me-header-left" :span="4">
       <h2><a href="/" style="color: #00b5ad">Youki's Blog</a></h2>
      </el-col>
      <!--目录-->
      <el-col v-if="!simple" :span="16" >
        <el-menu :router=true
                 menu-trigger="click"
                 active-text-color="#ffffff"
                 :default-active="activeIndex"
                 background-color="#222222"
                 mode="horizontal">
          <el-menu-item index="/">首页 <i class="iconfont icon-shouye"></i></el-menu-item>
          <el-menu-item index="/category/all">分类 <i class="iconfont icon-fenlei"></i></el-menu-item>
          <el-menu-item index="/tag/all">标签 <i class="iconfont icon-biaoqian"></i></el-menu-item>
          <el-menu-item index="/archives">归档 <i class="iconfont icon-guidang"></i></el-menu-item>
          <el-menu-item index="/aboutme">关于我 <i class="el-icon-info"></i></el-menu-item>
        </el-menu>
      </el-col>
      <!--用户登录-->
      <el-col :span="4">
        <el-menu
          :router=true
          menu-trigger="click"
          mode="horizontal"
          active-text-color="#ffffff"
          background-color="#222222">
          <!--未登录状态-->
          <template v-if="!user.login">
            <el-menu-item index="/login">
              <el-button type="text" style="color: #909399">登录</el-button>
            </el-menu-item>
            <el-menu-item index="/register">
              <el-button type="text" style="color: #909399">注册</el-button>
            </el-menu-item>
          </template>
          <!--登录状态-->
          <template v-else>
            <el-submenu index>
              <template slot="title">
                <img class="me-header-picture" :src="user.avatar"/>
              </template>
              <el-menu-item @click="getUserInfo()"><i class="el-icon-info"></i>个人信息</el-menu-item>
              <el-menu-item @click="ShowUpdatePasswordView()"><i class="iconfont icon-xiugaimima"></i>修改密码</el-menu-item>
              <el-menu-item @click="deleteAccount()"><i class="el-icon-delete"></i>注销账号</el-menu-item>
              <el-menu-item @click="logout"><i class="el-icon-back"></i>退出</el-menu-item>
            </el-submenu>
          </template>
        </el-menu>

      </el-col>
    </el-row>
    <!-- 用户信息 -->
    <div class="add-form">
      <el-dialog title="用户信息" :visible.sync="dialogFormVisibleEdit"  append-to-body>
        <el-form ref="formData" :model="formData" :rules="userInfoRules" label-position="right" label-width="100px">
          <el-row>
            <el-col>
              <el-form-item label="用户头像" prop="avatar">
                <!--<el-input hidden disabled v-model="formData.avatar"/>-->
                <el-upload
                  class="avatar-uploader"
                  action=""
                  :http-request="uploadImg"
                  :show-file-list="false"
                  :before-upload="beforeAvatarUpload">
                  <img v-if="imageUrl" :src="imageUrl" class="avatar">
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col>
              <el-form-item label="账号" prop="account">
                <el-input disabled v-model="formData.account"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col>
              <el-form-item label="昵称" prop="nickname">
                <el-input v-model="formData.nickname"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col>
              <el-form-item label="注册时间" prop="createDate">
                <el-input disabled v-model="formData.createDate"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col>
              <el-form-item label="手机号码" prop="mobilePhoneNumber">
                <el-input v-model="formData.mobilePhoneNumber"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col>
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="formData.email"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col>
              <el-form-item label="状态" prop="status">
                <el-input disabled v-model="formData.status"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col>
              <el-form-item label="最近一次登录时间" prop="lastLogin">
                <el-input disabled v-model="formData.lastLogin"/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisibleEdit = false">取消</el-button>
          <el-button type="primary" @click="handleEdit('formData')">修改</el-button>
        </div>
      </el-dialog>
    </div>
    <!--  修改密码-->
    <el-dialog title="修改密码" :visible.sync="dialogUpdatePassVisible" width="30%" append-to-body>
      <el-form ref="formDataUpdatePassWord" :model="formDataUpdatePassWord" :rules="updateRules" label-position="right"
               label-width="100px">
        <el-row>
          <el-col>
            <el-form-item label="旧密码" prop="oldPassword">
              <el-input type="password" v-model="formDataUpdatePassWord.oldPassword"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item label="新密码" prop="newPassword">
              <el-input type="password" v-model="formDataUpdatePassWord.newPassword"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item label="确认新密码" prop="surePassword">
              <el-input type="password" v-model="formDataUpdatePassWord.surePassword"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
            <el-button @click="ShowUpdatePasswordView()">取 消</el-button>
            <el-button @click="resetForm('formDataUpdatePassWord')">重置</el-button>
            <el-button type="primary" @click="HandleUpdatePassword('formDataUpdatePassWord')">确 定</el-button>
          </span>
    </el-dialog>
  </el-header>
</template>

<script>
import {getUserDetail} from '../api/user.js'
import {updateUserDetail,updatePassword,checkImg} from '../api/user.js'
import {upload} from '@/api/upload'

export default {
  name: 'BaseHeader',
  props: {
    activeIndex: String,
    simple: {
      type: Boolean,
      default: false
    }
  },
  data() {
    //自定义修改密码验证
    var validateOldPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入旧密码'));
      }else if (value.length>20){
        callback(new Error('密码在20个字符以内哦'));
      }else {
        callback();
      }
    };
    var validateNewPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'));
      }else if(value.length>20){
        callback(new Error('密码在20个字符以内哦'));
      } else if (value==this.formDataUpdatePassWord.oldPassword){
        callback(new Error('不能和旧密码一致啊'));
      }
      else {
        if (this.formDataUpdatePassWord.surePassword!== '') {
          this.$refs.formDataUpdatePassWord.validateField('surePassword');
        }
        callback();
      }
    };
    var validateSurePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'));
      } else if(value.length>20){
        callback(new Error('密码在20个字符以内哦'));
      } else if (value !== this.formDataUpdatePassWord.newPassword) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    //自定义个人信息修改验证
    var checkPhone = (rule, value, callback) => {
      const phoneReg = /^1[3|4|5|7|8][0-9]{9}$/
      if (!value) {
        return callback(new Error('电话号码不能为空'))
      }
      setTimeout(() => {
        if (!Number.isInteger(+value)) {
          callback(new Error('请输入数字值'))
        } else {
          if (phoneReg.test(value)) {
            callback()
          } else {
            callback(new Error('电话号码格式不正确'))
          }
        }
      }, 100)
    }
    var checkEmail = (rule, value, callback) => {
      const mailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/
      if (!value) {
        return callback(new Error('邮箱不能为空'))
      }
      setTimeout(() => {
        if (mailReg.test(value)) {
          callback()
        } else {
          callback(new Error('请输入正确的邮箱格式'))
        }
      }, 100)
    }
    return {
      imageUrl: '',
      formData: {},//表单数据
      dialogFormVisibleEdit: false,//个人信息视图
      userParams: {
        nickName: '',
        email: '',
        mobilePhoneNumber: '',
        avatar: ''
      },
      userInfoRules:{
         mobilePhoneNumber:[
           { validator: checkPhone , trigger: 'blur' },
         ],
        email:[
          { validator: checkEmail , trigger: 'blur' },
        ],
        nickname: [
          {required: true, message: '请输入昵称', trigger: 'blur'},
          {max: 10, message: '不能大于10个字符', trigger: 'blur'}
        ],
      },
      //修改密码视图
      dialogUpdatePassVisible: false,
      //修改密码表单
      formDataUpdatePassWord: {
        oldPassword: '',
        newPassword: '',
        surePassword: ''
      },
      passwordParams:{
        oldPassword:'',
        newPassword:''
      },
      updateRules: { //修改密码校验规则
        oldPassword: [
          { validator: validateOldPass, trigger: 'blur' },
          ],
        newPassword: [
          { validator: validateNewPass, trigger: 'blur' },

        ],
        surePassword: [
          { validator: validateSurePass, trigger: 'blur' },
        ]
      }
    }
  },
  computed: {
    user() {
      //判断是否登录 调用store中存储的信息
      let login = this.$store.state.account.length != 0
      // 显示头像
      let avatar = this.$store.state.avatar
      return {
        login, avatar
      }
    }
  },
  methods: {
    //修改个人信息
    handleEdit(formName) {
      let that = this;
      that.$confirm('确定修改?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
          //验证数据合法性
          that.$refs[formName].validate((valid) => {
            //合法
           if (valid) {
              //设置请求参数
              that.userParams.nickname = that.formData.nickname;
              that.userParams.email = that.formData.email;
              that.userParams.mobilePhoneNumber = that.formData.mobilePhoneNumber;
              //发起请求
              updateUserDetail(this.$store.state.token, that.userParams).then((res) => {
                if (res.success) {
                  that.$message.success("修改成功");
                  // that.dialogFormVisibleEdit = false;
                  console.log('修改前')
                  console.log('修改前')
                  console.log(that.$store.state.name)
                  console.log(that.$store.state.avatar)
                  that.$store.state.name = that.userParams.nickname;
                  console.log('修改后')
                  console.log(that.$store.state.name)
                  console.log(that.$store.state.avatar)
                }
              })
            }else {//不合法
              that.$message.warning("修改数据不合法")
              return false;
            }
          })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消修改'
        });
      });
    },
    //修改头像图片
    uploadImg(file) {
      console.log(file.file);
      //封装请求体或请求参数
      let formdata = new FormData();
      formdata.append('image', file.file);
      var that = this;
      //审核图片
      checkImg(formdata).then(data=>{
        if (data.success) {
          //合规
          if(data.data.conclusionType==1){
            that.$message({type: 'success', message: '图片审核通过', showClose: true})
            //发起上传请求
            upload(formdata).then(data => {
              //上传成功
              if (data.success) {
                that.$message.success("上传图片成功修改中...")
                console.log(data.data)
                that.userParams.nickName='';
                that.userParams.email='';
                that.userParams.mobilePhoneNumber='';
                that.userParams.avatar=data.data;
                //发起修改头像请求
                updateUserDetail(that.$store.state.token, that.userParams).then((res) => {
                  if (res.success) {
                    that.$message.success("修改成功");
                    // that.dialogFormVisibleEdit = false;
                    console.log('修改前')
                    console.log(that.$store.state.avatar)
                    console.log('修改后')
                    that.$store.state.avatar= that.userParams.avatar;
                    that.imageUrl=that.userParams.avatar;
                    console.log(that.$store.state.avatar)
                  }else {
                    that.$message({message: "修改失败", type: 'error', showClose: true})
                    return;
                  }
                })
              }
              //上传失败
              else {
                that.$message({message: data.msg, type: 'error', showClose: true})
                return;
              }
            }).catch(err => {
              that.$message({message: err, type: 'error', showClose: true});
            })
          }
          //不合规
          else {
            that.$message({type: 'success', message: "图片不合规", showClose: true})
            return;
          }
        } else {
          that.$message({type: 'success', message: data.msg, showClose: true})
          return;
        }
      })


    },
    //上传限制
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isLt2M;
    },
    //登出
    logout() {
      let that = this
      //调用store/index.js中的登出方法
      this.$store.dispatch('logout').then(() => {
        this.$router.push({path: '/'})
      }).catch((error) => {
        if (error !== 'error') {
          that.$message({message: error, type: 'error', showClose: true});
        }
      })
    },
    //注销
    deleteAccount() {
      let that = this
      this.$confirm('此操作将注销该账号, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        //调用store/index.js中的登出方法
        that.$store.dispatch('deleteAccount').then(() => {
          this.$message({
            type: 'success',
            message: '注销成功!'
          });
          this.$router.push({path: '/'})
        }).catch((error) => {
          if (error !== 'error') {
            that.$message({message: error, type: 'error', showClose: true});
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消注销'
        });
      });
    },
    //修改密码视图
    ShowUpdatePasswordView() {
      this.dialogUpdatePassVisible=!this.dialogUpdatePassVisible
    },
    //修改密码
    HandleUpdatePassword(formName){
      let that=this;
      that.passwordParams.newPassword=that.formDataUpdatePassWord.newPassword
      that.passwordParams.oldPassword=that.formDataUpdatePassWord.oldPassword
      this.$refs[formName].validate((valid) => {
        if (valid) {
          //修改密码请求
          updatePassword(this.$store.state.token,that.passwordParams).then(data => {
            if (data.success) {
              that.$message.success("修改成功");
              that.dialogUpdatePassVisible=! that.dialogUpdatePassVisible;
            } else {
              that.$message({message: data.msg, type: 'error', showClose: true})
            }
          }).catch(err => {
            that.$message({message: err, type: 'error', showClose: true});
          })
          return true;
        } else {
         that.$message.warning("修改数据不合法")
          return false;
        }
      });
    },
    //重置
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    //获取用户详细信息
    getUserInfo() {
      let that = this;
      this.dialogFormVisibleEdit = true;
      getUserDetail(this.$store.state.token).then(data => {
        that.formData = data.data
        that.imageUrl = data.data.avatar;
      }).catch(error => {
        if (error !== 'error') {
          that.$message({type: 'error', message: '用户详细信息加载失败!', showClose: true})
        }
      })
    }
  }
}
</script>

<style scoped>

.avatar {
  width: 200px;
  height: 200px;
  border-style: solid;
  border-color: #00ff00;
}

.el-header {
  /*position: fixed;*/
  z-index: 1024;
  min-width: 100%;
  box-shadow: 0 2px 3px hsla(0, 0%, 7%, .1), 0 0 0 1px hsla(0, 0%, 7%, .1);
}

.me-header{
  background: #222222!important;
}

.me-title {
  margin-top: 10px;
  font-size: 24px;
}

.me-header-left {
  padding-left: 30px;
  margin-top: 10px;
}

.me-title img {
  max-height: 2.4rem;
  max-width: 100%;
}

.me-header-picture {
  width: 36px;
  height: 36px;
  border: 1px solid #ddd;
  border-radius: 50%;
  vertical-align: middle;
  background-color: #5fb878;
}
</style>
