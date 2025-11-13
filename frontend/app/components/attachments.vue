<template>
  <div class="space-y-3">
    <div class="flex items-center gap-2">
      <input type="file" @change="onPick" />
      <UButton v-if="busy" :loading="busy">Nahrávám…</UButton>
    </div>
    <UTable :rows="items" :columns="cols" />
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'Attachments',
  props: { items: { type: Array, required: true } },
  emits: ['upload'],
  data() {
    return {
      cols: [
        { accessorKey: 'name', header: 'Soubor' },
        { accessorKey: 'type', header: 'Typ' },
        { accessorKey: 'size', header: 'Velikost' },
        { accessorKey: 'createdAt', header: 'Nahráno' },
        { accessorKey: 'authorEmail', header: 'Autor' },
      ],
      busy: false,
    }
  },
  methods: {
    async onPick(e: Event) {
      const input = e.target as HTMLInputElement
      const file = input.files?.[0]
      if (!file) return
      this.busy = true
      try { this.$emit('upload', file) } finally { this.busy = false }
    },
  },
})
</script>