<!-- pages/projects/[projectId]/tickets/[ticketId].vue -->
<template>
  <div class="space-y-8">
    <section class="border rounded bg-white p-4">
      <div class="flex items-center justify-between">
        <h1 class="text-xl font-semibold">Ticket #{{ ticket?.id }} – {{ ticket?.title }}</h1>
        <button class="text-sm px-3 py-1 border rounded" :disabled="loading" @click="fetchAll">Obnovit</button>
        <button
            class="text-sm px-3 py-1 border rounded bg-red-600 text-white disabled:opacity-50"
            :disabled="deleting"
            @click="deleteTicket"
        >
          {{ deleting ? 'Mazání…' : 'Smazat' }}
        </button>
      </div>
      <div class="grid md:grid-cols-3 gap-4 mt-3">
        <div class="space-y-1 text-sm">
          <div>Typ: <span class="px-2 py-0.5 rounded bg-gray-100">{{ ticket?.type }}</span></div>
          <div>Priorita: <span class="px-2 py-0.5 rounded bg-gray-100">{{ ticket?.priority }}</span></div>
          <div>Stav: <span class="px-2 py-0.5 rounded bg-gray-100">{{ ticket?.state }}</span></div>
          <div>Uživatel: <span class="px-2 py-0.5 rounded bg-gray-100">{{ ticket?.assignedUser?.email }}</span></div>
        </div>
        <div class="md:col-span-2">
          <h3 class="font-medium mb-2">Verze</h3>
          <div class="overflow-x-auto border rounded">
            <table class="w-full text-sm">
              <thead class="bg-gray-100 text-left">
              <tr><th class="p-2">Titulek</th><th class="p-2">Typ</th><th class="p-2">Priorita</th><th class="p-2">Stav</th><th class="p-2">Vytvořeno</th></tr>
              </thead>
              <tbody>
              <tr v-for="v in versions" :key="v.id" class="border-t">
                <td class="p-2">{{ v.title }}</td>
                <td class="p-2">{{ v.type }}</td>
                <td class="p-2">{{ v.priority }}</td>
                <td class="p-2">{{ v.state }}</td>
                <td class="p-2">{{ formatDate(v.createdAt) }}</td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </section>

    <!-- Upravit ticket (jednoduchý POST) -->
    <section class="border rounded bg-white p-4">
      <h2 class="font-semibold mb-2">Upravit ticket</h2>

      <form @submit.prevent="updateTicket" class="grid md:grid-cols-2 gap-4 max-w-3xl">
        <div class="md:col-span-2">
          <label class="block text-sm mb-1">Titulek (1–160)</label>
          <input v-model.trim="edit.title" required :maxlength="160"
                 class="w-full border rounded px-3 py-2" placeholder="Titulek" />
        </div>

        <div>
          <label class="block text-sm mb-1">Typ</label>
          <input v-model.trim="edit.type" required class="w-full border rounded px-3 py-2"
                 :placeholder="ticket?.type || 'např. BUG/FEATURE…'" />
        </div>

        <div>
          <label class="block text-sm mb-1">Priorita</label>
          <input v-model.trim="edit.priority" required class="w-full border rounded px-3 py-2"
                 :placeholder="ticket?.priority || 'např. LOW/MEDIUM/HIGH…'" />
        </div>

        <div>
          <label class="block text-sm mb-1">Stav</label>
          <input v-model.trim="edit.state" required class="w-full border rounded px-3 py-2"
                 :placeholder="ticket?.state || 'např. OPEN/IN_PROGRESS…'" />
        </div>

        <div>
          <label class="block text-sm mb-1">Přiřazený uživatel (ID)</label>
          <input v-model.number="edit.assignedUserId" type="number" min="1"
                 class="w-full border rounded px-3 py-2"
                 :placeholder="ticket?.assignedUser?.id || '-- nevyplněno --'" />
        </div>

        <div class="md:col-span-2 flex items-center gap-2">
          <button type="submit" class="px-4 py-2 bg-black text-white rounded disabled:opacity-50"
                  :disabled="updating">
            Uložit
          </button>
          <button type="button" class="px-4 py-2 border rounded" :disabled="updating" @click="resetEdit">
            Reset
          </button>
          <span v-if="updateOk" class="text-sm text-green-700">Uloženo.</span>
          <span v-if="updateError" class="text-sm text-red-600">{{ updateError }}</span>
        </div>
      </form>
    </section>


    <!-- Přiřazení uživatele -->
    <section class="border rounded bg-white p-4">
      <h2 class="font-semibold mb-2">Přiřadit uživatele</h2>
      <form @submit.prevent="assignUser" class="flex flex-wrap items-end gap-3 max-w-xl">
        <div class="flex-1 min-w-[220px]">
          <label class="block text-sm mb-1">Uživatel</label>
          <select v-model.number="selectedUserId" class="w-full border rounded px-3 py-2">
            <option :value="undefined">-- nevybráno --</option>
            <option v-for="u in users" :key="u.id" :value="u.id">{{ u.username }} ({{ u.email }})</option>
          </select>
        </div>
        <button type="submit" class="px-4 py-2 bg-black text-white rounded disabled:opacity-50" :disabled="assigning">Uložit</button>
        <span v-if="assignError" class="text-sm text-red-600">{{ assignError }}</span>
      </form>
    </section>

    <section class="border rounded bg-white p-4">
      <h2 class="font-semibold mb-2">Komentáře</h2>
      <form @submit.prevent="addComment" class="flex gap-2 mb-3">
        <input v-model="newComment" placeholder="Přidat komentář…" class="flex-1 border rounded px-3 py-2" />
        <button class="px-4 py-2 bg-black text-white rounded">Přidat</button>
      </form>
      <div class="space-y-3">
        <div v-for="c in comments" :key="c.id" class="p-3 rounded border">
          <div class="text-sm text-gray-500 flex items-center gap-2">
            <strong>{{ c.authorEmail || 'Uživatel' }}</strong>
            <span>·</span>
            <span>{{ formatDate(c.createdAt) }}</span>
          </div>
          <div class="mt-1 whitespace-pre-wrap">{{ c.comment }}</div>
        </div>
      </div>
    </section>

    <section class="border rounded bg-white p-4">
      <h2 class="font-semibold mb-2">Přílohy</h2>
      <input type="file" @change="uploadAttachment" class="mb-3" />
      <div class="overflow-x-auto">
        <table class="w-full text-sm">
          <thead class="bg-gray-100 text-left">
          <tr><th class="p-2">Soubor</th><th class="p-2">Typ</th><th class="p-2">Velikost</th><th class="p-2">Nahráno</th><th class="p-2">Autor</th></tr>
          </thead>
          <tbody>
          <tr v-for="a in attachments" :key="a.id || a.name" class="border-t">
            <td class="p-2">{{ a.name }}</td>
            <td class="p-2">{{ a.type }}</td>
            <td class="p-2">{{ a.size }}</td>
            <td class="p-2">{{ formatDate(a.createdAt) }}</td>
            <td class="p-2">{{ a.authorEmail }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </section>
  </div>
</template>

<script lang="ts">
export default {
  data() {
    return {
      projectId: Number(this.$route.params.projectId),
      ticketId: Number(this.$route.params.ticketId),
      ticket: null as any,
      versions: [] as any[],
      comments: [] as any[],
      attachments: [] as any[],
      newComment: '',
      loading: false,
      // přiřazení
      users: [] as any[],
      selectedUserId: undefined as number | undefined,
      assigning: false,
      assignError: '',
      // jednoduchý update form
      edit: {
        title: '',
        type: '',
        priority: '',
        state: '',
        assignedUserId: undefined as number | undefined,
      },
      updating: false,
      updateError: '',
      updateOk: false,
      deleting: false,
      deleteError: '',
    }
  },
  mounted() { this.fetchAll(); this.fetchUsers() },
  methods: {
    formatDate(v?: string) { return v ? new Date(v).toLocaleString() : '' },
    async fetchAll() {
      this.loading = true
      try {
        const [{ data: t }, { data: cs }, { data: as }] = await Promise.all([
          this.$api.get(`/api/v1/projects/${this.projectId}/tickets/${this.ticketId}`),
          this.$api.get(`/api/v1/projects/${this.projectId}/tickets/${this.ticketId}/comments`),
          this.$api.get(`/api/v1/projects/${this.projectId}/tickets/${this.ticketId}/attachments`),
        ])
        this.ticket = t
        this.versions = Array.isArray(t?.versions) ? t.versions : []
        this.comments = cs
        this.attachments = as
        this.initEditFromTicket()
      } finally { this.loading = false }
    },
    async fetchUsers() { const { data } = await this.$api.get('/api/v1/users/all'); this.users = data },
    async assignUser() {
      this.assignError = ''
      try {
        this.assigning = true
        // PUT requires: title, type, priority, state; plus assignedUserId
        const body = {
          title: this.ticket.title,
          type: this.ticket.type,
          priority: this.ticket.priority,
          state: this.ticket.state,
          assignedUserId: this.selectedUserId,
        }
        const { data } = await this.$api.put(`/api/v1/projects/${this.projectId}/tickets/${this.ticketId}`, body)
        this.ticket = data
      } catch (e:any) {
        this.assignError = e?.response?.data?.message || 'Přiřazení se nepodařilo'
      } finally { this.assigning = false }
    },
    async addComment() { if (!this.newComment) return; const { data } = await this.$api.post(`/api/v1/projects/${this.projectId}/tickets/${this.ticketId}/comments`, { comment: this.newComment }); this.comments.unshift(data); this.newComment = '' },
    async uploadAttachment(e: Event) { const input = e.target as HTMLInputElement; const file = input.files?.[0]; if (!file) return; const fd = new FormData(); fd.append('file', file); const { data } = await this.$api.post(`/api/v1/projects/${this.projectId}/tickets/${this.ticketId}/attachments`, fd, { headers: { 'Content-Type': 'multipart/form-data' } }); this.attachments.unshift(data) },
    initEditFromTicket() {
      if (!this.ticket) return
      this.edit.title = this.ticket.title || ''
      this.edit.type = this.ticket.type || ''
      this.edit.priority = this.ticket.priority || ''
      this.edit.state = this.ticket.state || ''
      this.edit.assignedUserId = this.ticket.assignedUser?.id ?? undefined
    },

    resetEdit() {
      this.updateError = ''
      this.updateOk = false
      this.initEditFromTicket()
    },

    async updateTicket() {
      this.updateError = ''
      this.updateOk = false
      // minimální FE validace titulku
      const t = (this.edit.title || '').trim()
      if (t.length < 1 || t.length > 160) {
        this.updateError = 'Titulek musí mít 1–160 znaků.'
        return
      }
      try {
        this.updating = true
        const body = {
          title: this.edit.title,
          type: this.edit.type,
          priority: this.edit.priority,
          state: this.edit.state,
          assignedUserId: this.edit.assignedUserId,
        }
        // POST na stejnou URL jako GET ticketu
        const { data } = await this.$api.put(
            `/api/v1/projects/${this.projectId}/tickets/${this.ticketId}`,
            body
        )
        this.ticket = data
        this.initEditFromTicket()
        this.updateOk = true
      } catch (e:any) {
        this.updateError = e?.response?.data?.message || 'Uložení se nepodařilo'
      } finally {
        this.updating = false
      }
    },
    async deleteTicket() {
      this.deleteError = ''

      const ok = window.confirm('Opravdu smazat tento ticket? Tato akce je nevratná.')
      if (!ok) return

      try {
        this.deleting = true
        await this.$api.delete(
            `/api/v1/projects/${this.projectId}/tickets/${this.ticketId}`
        )

        // po úspěšném smazání redirect zpět na detail projektu
        this.$router.push(`/projects/${this.projectId}`)
      } catch (e: any) {
        this.deleteError = e?.response?.data?.message || 'Smazání ticketu selhalo'
      } finally {
        this.deleting = false
      }
    },
  },
}
</script>