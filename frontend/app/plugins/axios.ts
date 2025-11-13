// plugins/axios.ts
import type { AxiosInstance } from 'axios'
import axios from 'axios'
import { useAuthStore } from '~/stores/auth'


export default defineNuxtPlugin((nuxtApp) => {
    const config = useRuntimeConfig()


    const api: AxiosInstance = axios.create({
        baseURL: config.public.apiBase,
    })


    api.interceptors.request.use((req) => {
        const auth = useAuthStore()
        if (auth.token) {
            req.headers = req.headers || {}
            req.headers.Authorization = `Bearer ${auth.token}`
        }
        return req
    })


    api.interceptors.response.use(
        (res) => res,
        async (err) => {
            const status = err?.response?.status
            if (status === 401) {
                const auth = useAuthStore()
                auth.clear()
                if (process.client && !window.location.pathname.startsWith('/login')) {
                    await navigateTo('/login')
                }
            }
            return Promise.reject(err)
        }
    )


    return { provide: { api } }
})