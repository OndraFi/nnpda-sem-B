import { useAuthStore } from '~/stores/auth'


export default defineNuxtRouteMiddleware((to) => {
    const auth = useAuthStore()
    const publicPages = new Set(['/login', '/register'])
    if (!auth.isAuthenticated && !publicPages.has(to.path)) {
        return navigateTo('/login')
    }
})