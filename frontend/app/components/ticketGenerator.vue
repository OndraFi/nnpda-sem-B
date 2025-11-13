<template>
  <label class="inline-flex items-center gap-2 cursor-pointer select-none">
    <input type="checkbox" class="sr-only" v-model="on">
    <span :class="['w-10 h-6 rounded-full transition-colors', on ? 'bg-green-500' : 'bg-gray-300']">
      <span :class="['block w-5 h-5 bg-white rounded-full transform transition-transform', on ? 'translate-x-5' : 'translate-x-0']"></span>
    </span>
    <span class="text-sm">{{ on ? 'Zapnuto' : 'Vypnuto' }}</span>
    <span class="text-xs text-gray-500">(každých 10s)</span>
  </label>
</template>

<script lang="ts">
export default {
  props: { projectId: { type: Number, required: true } },
  emits: ['created'],
  data(){
    return { on: false, timer: undefined as any }
  },
  watch: {
    on(val){ val ? this.start() : this.stop() }
  },
  beforeUnmount(){ this.stop() },
  methods: {
    start(){
      if (this.timer) return
      // okamžitě vytvoř první a pak každých 10 s
      this.createOnce()
      this.timer = setInterval(() => this.createOnce(), 10000)
    },
    stop(){ if (this.timer){ clearInterval(this.timer); this.timer = undefined } },
    async createOnce(){
      try {
        const title = `Auto ticket #${Math.floor(Math.random()*100000)}`
        const types = ['BUG','FEATURE','TASK'] as const
        const priorities = ['LOW','MEDIUM','HIGH'] as const
        const body = {
          title,
          type: types[Math.floor(Math.random()*types.length)],
          priority: priorities[Math.floor(Math.random()*priorities.length)],
        }
        const { data } = await this.$api.post(`/api/v1/projects/${this.projectId}/tickets`, body)
        this.$emit('created', data)
      } catch (e) {
        // ignoruj chyby, nebo sem dej toast/log dle potřeby
      }
    }
  }
}
</script>