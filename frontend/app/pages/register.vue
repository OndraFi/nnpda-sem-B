<!-- pages/register.vue -->
<template>
  <UContainer class="max-w-md mx-auto mt-16">
    <UCard>
      <template #header>
        <h1 class="text-xl font-semibold">Registrace</h1>
      </template>


      <form @submit.prevent="submit" class="space-y-3">
        <UFormGroup label="Uživatelské jméno" name="username">
          <UInput v-model="form.username" required />
        </UFormGroup>
        <UFormGroup label="E‑mail" name="email">
          <UInput v-model="form.email" type="email" required />
        </UFormGroup>
        <UFormGroup label="Heslo" name="password">
          <UInput v-model="form.password" type="password" required />
        </UFormGroup>
        <UButton type="submit" :loading="loading" block>Vytvořit účet</UButton>
      </form>


      <template #footer>
        <div class="text-sm">Už máš účet? <NuxtLink to="/login" class="underline">Přihlásit se</NuxtLink></div>
      </template>
    </UCard>
  </UContainer>
</template>


<script lang="ts">
import { defineComponent } from 'vue'


export default defineComponent({
  name: 'RegisterPage',
  data() {
    return {
      form: { username: '', email: '', password: '' },
      loading: false,
    }
  },
  setup() {
    const toast = useToast()
    const { $api } = useNuxtApp()
    return { toast, $api }
  },
  methods: {
    async submit() {
      try {
        this.loading = true
// POST /api/v1/auth/register expects { username, email, password }
        await this.$api.post('/api/v1/auth/register', this.form)
        this.toast.add({ title: 'Účet vytvořen', description: 'Teď se přihlas', color: 'green' })
        this.$router.push('/login')
      } catch (e: any) {
        this.toast.add({ title: 'Registrace selhala', description: e?.response?.data?.message || 'Zkus to znovu', color: 'red' })
      } finally {
        this.loading = false
      }
    },
  },
})
</script>