<!-- pages/login.vue -->
<template>
  <div class="max-w-md mx-auto mt-20 p-6 border rounded bg-white">
    <h1 class="text-xl font-semibold mb-6">Přihlášení</h1>

    <form @submit.prevent="submit" class="space-y-4 text-sm">
      <div>
        <label class="block mb-1">E‑mail</label>
        <input v-model="form.email" type="email" required class="w-full border rounded px-3 py-2" />
      </div>
      <div>
        <label class="block mb-1">Heslo</label>
        <input v-model="form.password" type="password" required class="w-full border rounded px-3 py-2" />
      </div>
      <button type="submit" class="w-full px-4 py-2 bg-black text-white rounded disabled:opacity-50" :disabled="loading">Přihlásit</button>
    </form>

    <div class="text-sm mt-4">Nemáš účet?
      <NuxtLink to="/register" class="underline">Registrovat</NuxtLink>
    </div>
  </div>
</template>

<script lang="ts">
export default {
  data() {
    return { form: { email: '', password: '' }, loading: false, error: '' }
  },
  methods: {
    async submit() {
      this.error = ''
      try {
        this.loading = true
        const { data } = await this.$api.post('/api/v1/auth/login', this.form)
        useAuthStore().setAuth(data.token)
        this.$router.push('/dashboard')
      } catch (e) {
        this.error = 'Chyba přihlášení'
      } finally { this.loading = false }
    },
  },
}
</script>