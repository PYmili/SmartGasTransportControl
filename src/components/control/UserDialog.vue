<template>
  <el-dialog 
    v-model="dialogVisible" 
    :title="dialogType === 'add' ? '添加用户' : '编辑用户'"
    width="500px"
  >
    <el-form :model="form" :rules="rules" ref="formRef">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" />
      </el-form-item>

      <!-- 密码输入框 -->
      <el-form-item :label="dialogType === 'add' ? '密码' : '新密码'" prop="password">
        <el-input v-model="form.password" type="password" show-password />
      </el-form-item>

      <!-- 选择是否可用 -->
      <el-form-item label="是否可用" prop="action">
        <el-radio-group v-model="form.action">
          <el-radio :label="true">可用</el-radio>
          <el-radio :label="false">不可用</el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- 编辑模式下显示邮箱 -->
      <el-form-item v-if="dialogType === 'edit'" label="邮箱" prop="email">
        <el-input v-model="form.email" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitForm">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch, reactive } from 'vue'

const props = defineProps({
  dialogType: String,
  currentUser: Object,
  modelValue: Boolean
})

const emit = defineEmits(['update:modelValue', 'submit'])

const formRef = ref(null)
const dialogVisible = ref(props.modelValue)

const form = reactive({
  id: null,
  username: '',
  password: '',
  email: '',
  action: true
})

// 动态表单验证规则
const rules = reactive({
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [
    { 
      required: props.dialogType === 'add', 
      message: '请输入密码', 
      trigger: 'blur' 
    },
    {
      min: 5,
      message: '密码长度至少为5个字符',
      trigger: 'blur'
    }
  ],
  email: [
    { required: false, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
})

// 监听props变化
watch(() => props.modelValue, (val) => {
  dialogVisible.value = val
})

watch(dialogVisible, (val) => {
  emit('update:modelValue', val)
  if (!val) {
    formRef.value?.resetFields()
  }
})

// 当前用户变化时更新表单
watch(() => props.currentUser, (user) => {
  if (user) {
    Object.assign(form, {
      id: user.id,
      username: user.username,
      password: '', // 密码不显示原始值
      email: user.email || '',
      action: user.action || true
    })
  } else {
    formRef.value?.resetFields()
  }
})

const submitForm = async () => {
  try {
    await formRef.value.validate()
    emit('submit', { ...form })
  } catch (error) {
    console.log('表单验证失败')
  }
}
</script>