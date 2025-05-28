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

      <el-form-item label="角色" prop="role">
        <el-select v-model="form.role">
          <el-option label="管理员" value="admin" />
          <el-option label="操作员" value="operator" />
        </el-select>
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
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
  username: '',
  role: '',
  email: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

watch(() => props.modelValue, (val) => {
  dialogVisible.value = val
})

watch(dialogVisible, (val) => {
  emit('update:modelValue', val)
  if (!val) {
    formRef.value?.resetFields()
  }
})

watch(() => props.currentUser, (user) => {
  if (user) {
    Object.assign(form, user)
  } else {
    formRef.value?.resetFields()
  }
})

const submitForm = async () => {
  try {
    await formRef.value.validate()
    emit('submit', { ...form })
    dialogVisible.value = false
  } catch (error) {
    console.log('表单验证失败')
  }
}
</script>