// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2025-07-15',
  devtools: { enabled: true },
  modules: ['@nuxt/ui','@pinia/nuxt'],
  css: ['assets/css/main.css'],
  runtimeConfig: {
    public: {
      apiBase: 'http://localhost:9000',
    },
  },
  app: {
    head: {
      title: 'Ticketing',
      meta: [{ name: 'viewport', content: 'width=device-width, initial-scale=1' }],
    },
  },
})
