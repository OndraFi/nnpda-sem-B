// stores/auth.ts
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { useLocalStorage } from '@vueuse/core'


export interface UserDto {
    id: string
    email: string
    name?: string
}


export const useAuthStore = defineStore('auth', () => {
    const token = useLocalStorage<string | null>('auth.token', null)
    const user = useLocalStorage<UserDto | null>('auth.user', null)


    const isAuthenticated = computed(() => !!token.value)


    function setAuth(_token: string, _user?: UserDto) {
        token.value = _token
        if (_user) user.value = _user
    }
    function clear() {
        token.value = null
        user.value = null
    }


    return { token, user, isAuthenticated, setAuth, clear }
},{persist: true})