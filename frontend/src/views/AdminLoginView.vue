<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { LogIn } from 'lucide-vue-next'
import axios from 'axios'
import { login } from '../api/articles'

const router = useRouter()
const username = ref('admin')
const password = ref('')
const error = ref('')
const loading = ref(false)

async function submit() {
  loading.value = true
  error.value = ''
  try {
    const result = await login(username.value.trim(), password.value.trim())
    if (!result.success) {
      error.value = result.message
      return
    }
    localStorage.setItem('shine_admin_token', result.data.token)
    await router.push('/studio')
  } catch (err) {
    if (axios.isAxiosError(err)) {
      if (err.code === 'ECONNABORTED') {
        error.value = '网络超时，请再点一次登录'
        return
      }
      const message = err.response?.data?.message
      error.value = message || '登录失败，请检查网络后重试'
      return
    }
    error.value = '登录失败，请检查网络后重试'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <section class="auth-page">
    <form class="panel auth-panel" @submit.prevent="submit">
      <h1>后台登录</h1>
      <label>
        用户名
        <input v-model="username" autocomplete="username" />
      </label>
      <label>
        密码
        <input v-model="password" type="password" autocomplete="current-password" />
      </label>
      <p v-if="error" class="form-error">{{ error }}</p>
      <button class="primary-button" type="submit" :disabled="loading">
        <LogIn :size="18" />
        {{ loading ? '登录中' : '登录' }}
      </button>
    </form>
  </section>
</template>
