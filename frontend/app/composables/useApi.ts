// composables/useApi.ts
export function useApi() {
    const { $api } = useNuxtApp()
    return {
        get: $api.get,
        post: $api.post,
        put: $api.put,
        del: $api.delete,
    }
}