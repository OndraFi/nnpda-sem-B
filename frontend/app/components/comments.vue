<template>
  <div class="space-y-4">
    <div class="flex gap-2">
      <UInput v-model="text" placeholder="Přidat komentář…" class="flex-1" @keyup.enter="emitAdd" />
      <UButton :disabled="!text" @click="emitAdd">Přidat</UButton>
    </div>
    <div class="space-y-3">
      <div v-for="c in items" :key="c.id" class="p-3 rounded-xl border">
        <div class="text-sm text-gray-500 flex items-center gap-2">
          <strong>{{ c.authorEmail || 'Uživatel' }}</strong>
          <span>·</span>
          <span>{{ formatDate(c.createdAt) }}</span>
        </div>
        <div class="mt-1 whitespace-pre-wrap">{{ c.comment }}</div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'Comments',
  props: { items: { type: Array, required: true } },
  data() { return { text: '' as string } },
  emits: ['add'],
  methods: {
    emitAdd() { if (!this.text) return; this.$emit('add', this.text); this.text = '' },
    formatDate(v?: string) { return v ? new Date(v).toLocaleString() : '' },
  },
})
</script>