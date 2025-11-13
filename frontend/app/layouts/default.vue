<!-- app.vue -->
<template>
  <div class="min-h-screen bg-gray-50 text-gray-900">
    <header class="border-b bg-white">
      <nav class="mx-auto max-w-6xl px-4 py-3 flex justify-between items-center">
        <NuxtLink to="/dashboard" class="font-semibold">Ticketing</NuxtLink>
        <div class="flex gap-4 text-sm">
          <NuxtLink to="/dashboard" class="hover:underline">Dashboard</NuxtLink>
          <button v-if="isAuthenticated" @click="logout" class="hover:underline">Logout</button>
          <NuxtLink v-else to="/login" class="hover:underline">Login</NuxtLink>
        </div>
      </nav>
    </header>
    <main class="mx-auto max-w-6xl px-4 py-6">
      <NuxtPage />
    </main>
  </div>
</template>

<script lang="ts">
export default {
  computed: {
    isAuthenticated() { return useAuthStore().isAuthenticated },
  },
  methods: {
    logout() { const a = useAuthStore(); a.clear(); this.$router.push('/login') },
  },
}
</script>