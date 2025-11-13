<!-- pages/projects/[projectId].vue -->
<template>
  <div class="space-y-8">
    <!-- Header -->
    <section class="border rounded bg-white p-4">
      <div class="flex items-center justify-between">
        <h1 class="text-xl font-semibold">{{ project?.name || 'Projekt' }}</h1>
        <button class="text-sm px-3 py-1 border rounded" :disabled="loadingProject" @click="fetchAll">Obnovit</button>
        <button
            class="text-sm px-3 py-1 border rounded bg-red-600 text-white disabled:opacity-50"
            :disabled="deletingProject"
            @click="deleteProject"
        >
          {{ deletingProject ? 'Mazání…' : 'Smazat' }}
        </button>
      </div>
      <p class="text-sm text-gray-600 mt-2">{{ project?.description }}</p>
      <p class="text-sm mt-2">Stav: <span class="px-2 py-0.5 rounded bg-gray-100">{{ project?.state }}</span></p>
    </section>

    <section class="border rounded bg-white p-4">
      <h2 class="font-semibold mb-2">Upravit projekt</h2>

      <form @submit.prevent="updateProject" class="grid gap-3">
        <div>
          <label class="block text-sm mb-1">Název</label>
          <input
              v-model="updateProjectData.name"
              required
              class="w-full border rounded px-3 py-2"
          />
        </div>

        <div>
          <label class="block text-sm mb-1">Popis</label>
          <textarea
              v-model="updateProjectData.description"
              required
              class="w-full border rounded px-3 py-2"
          ></textarea>
        </div>

        <div>
          <label class="block text-sm mb-1">Stav</label>
          <select
              v-model="updateProjectData.state"
              required
              class="w-full border rounded px-3 py-2"
          >
            <option value="ACTIVE">ACTIVE</option>
            <option value="ARCHIVED">ARCHIVED</option>
          </select>
        </div>

        <div class="flex items-center gap-3">
          <button
              type="submit"
              class="px-4 py-2 bg-black text-white rounded disabled:opacity-50"
              :disabled="updatingProject"
          >
            {{ updatingProject ? 'Ukládám…' : 'Uložit změny' }}
          </button>

          <span v-if="updateError" class="text-sm text-red-600">
        {{ updateError }}
      </span>
        </div>
      </form>
    </section>

    <!-- Tickety projektu -->
    <section class="border rounded bg-white p-4">
      <div class="flex items-center justify-between mb-2">
        <h2 class="font-semibold">Tickety projektu</h2>
      </div>
      <div class="overflow-x-auto">
        <table class="w-full text-sm">
          <thead class="bg-gray-100 text-left">
          <tr>
            <th class="p-2">Titulek</th>
            <th class="p-2">Typ</th>
            <th class="p-2">Priorita</th>
            <th class="p-2">Stav</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="t in tickets" :key="t.id" class="border-t hover:bg-gray-50 cursor-pointer" @click="openTicket(t.id)">
            <td class="p-2"><NuxtLink :to="`/projects/${projectId}/tickets/${t.id}`" class="underline">{{ t.title }}</NuxtLink></td>
            <td class="p-2">{{ t.type }}</td>
            <td class="p-2">{{ t.priority }}</td>
            <td class="p-2">{{ t.state }}</td>
          </tr>
          <tr v-if="!loadingTickets && tickets.length===0">
            <td colspan="4" class="p-3 text-gray-500">Žádné tickety.</td>
          </tr>
          </tbody>
        </table>
      </div>
    </section>

    <!-- Nový ticket -->
    <section class="border rounded bg-white p-4">
      <h2 class="font-semibold mb-2">Přidat nový ticket</h2>
      <form @submit.prevent="createTicket" class="grid gap-3 md:grid-cols-4">
        <div class="md:col-span-2">
          <label class="block text-sm mb-1">Titulek</label>
          <input v-model="newTicket.title" required class="w-full border rounded px-3 py-2" />
        </div>
        <div>
          <label class="block text-sm mb-1">Typ</label>
          <select v-model="newTicket.type" class="w-full border rounded px-3 py-2">
            <option value="BUG">BUG</option>
            <option value="FEATURE">FEATURE</option>
            <option value="TASK">TASK</option>
          </select>
        </div>
        <div>
          <label class="block text-sm mb-1">Priorita</label>
          <select v-model="newTicket.priority" class="w-full border rounded px-3 py-2">
            <option value="LOW">LOW</option>
            <option value="MEDIUM">MEDIUM</option>
            <option value="HIGH">HIGH</option>
          </select>
        </div>
        <div class="md:col-span-4 flex items-center gap-3">
          <button type="submit" class="px-4 py-2 bg-black text-white rounded disabled:opacity-50" :disabled="creatingTicket">Vytvořit ticket</button>
          <span v-if="ticketError" class="text-sm text-red-600">{{ ticketError }}</span>
        </div>
      </form>
    </section>

    <!-- Generátor ticketů -->
    <section class="border rounded bg-white p-4">
      <div class="flex items-center justify-between">
        <h2 class="font-semibold">Generátor ticketů</h2>
        <TicketGenerator :project-id="projectId" @created="onGenerated" />
      </div>
      <p class="text-xs text-gray-500 mt-2">Při zapnutí vytváří každých 10&nbsp;s náhodný ticket (title/priority/type).</p>
    </section>

    <!-- Komentáře -->
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

    <!-- Přílohy -->
    <section class="border rounded bg-white p-4">
      <h2 class="font-semibold mb-2">Přílohy</h2>
      <input type="file" @change="uploadAttachment" class="mb-3" />
      <div class="overflow-x-auto">
        <table class="w-full text-sm">
          <thead class="bg-gray-100 text-left">
          <tr>
            <th class="p-2">Soubor</th><th class="p-2">Typ</th><th class="p-2">Velikost</th><th class="p-2">Nahráno</th><th class="p-2">Autor</th>
          </tr>
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
import TicketGenerator from '~/components/TicketGenerator.vue'
export default {
  components: { TicketGenerator },
  data() {
    return {
      projectId: Number(this.$route.params.projectId),
      project: null as any,
      tickets: [] as any[],
      comments: [] as any[],
      attachments: [] as any[],
      newComment: '',
      loadingProject: false,
      loadingTickets: false,
      // nový ticket
      newTicket: { title: '', type: 'TASK', priority: 'MEDIUM' },
      creatingTicket: false,
      ticketError: '',
      // ↓ přidáno pro delete
      deletingProject: false,
      deleteError: '',
      updateProjectData: {
        name: '',
        description: '',
        state: 'ACTIVE', // nebo cokoliv z enumu, doplň si jak chceš
      },
      updatingProject: false,
      updateError: '',
    }
  },
  watch: {
    project(p) {
      if (p) {
        this.updateProjectData.name = p.name
        this.updateProjectData.description = p.description
        this.updateProjectData.state = p.state
      }
    }
  },
  mounted() { this.fetchAll() },
  methods: {
    formatDate(v?: string) { return v ? new Date(v).toLocaleString() : '' },
    async fetchAll() { await Promise.all([this.fetchProject(), this.fetchTickets(), this.fetchComments(), this.fetchAttachments()]) },
    async fetchProject() { try { this.loadingProject = true; const { data } = await this.$api.get(`/api/v1/projects/${this.projectId}`); this.project = data } finally { this.loadingProject = false } },
    async fetchTickets() { try { this.loadingTickets = true; const { data } = await this.$api.get(`/api/v1/projects/${this.projectId}/tickets`); this.tickets = data } finally { this.loadingTickets = false } },
    async fetchComments() { const { data } = await this.$api.get(`/api/v1/projects/${this.projectId}/comments`); this.comments = data },
    async fetchAttachments() { const { data } = await this.$api.get(`/api/v1/projects/${this.projectId}/attachments`); this.attachments = data },
    openTicket(ticketId:number) { this.$router.push(`/projects/${this.projectId}/tickets/${ticketId}`) },
    async addComment() { if (!this.newComment) return; const { data } = await this.$api.post(`/api/v1/projects/${this.projectId}/comments`, { comment: this.newComment }); this.comments.unshift(data); this.newComment = '' },
    async uploadAttachment(e: Event) { const input = e.target as HTMLInputElement; const file = input.files?.[0]; if (!file) return; const fd = new FormData(); fd.append('file', file); const { data } = await this.$api.post(`/api/v1/projects/${this.projectId}/attachments`, fd, { headers: { 'Content-Type': 'multipart/form-data' } }); this.attachments.unshift(data) },
    async createTicket() {
      this.ticketError = ''
      try {
        this.creatingTicket = true
        const body = { title: this.newTicket.title, type: this.newTicket.type, priority: this.newTicket.priority }
        const { data } = await this.$api.post(`/api/v1/projects/${this.projectId}/tickets`, body)
        this.tickets.unshift(data)
        this.newTicket.title = ''
        this.newTicket.type = 'TASK'
        this.newTicket.priority = 'MEDIUM'
      } catch (e:any) {
        this.ticketError = e?.response?.data?.message || 'Vytvoření ticketu selhalo'
      } finally { this.creatingTicket = false }
    },
    onGenerated(ticket:any){
      // Když komponenta vygeneruje ticket, rovnou ho zobrazíme nahoře
      this.tickets.unshift(ticket)
    },
    async deleteProject() {
      this.deleteError = ''
      const ok = window.confirm('Opravdu smazat tento projekt? Tato akce je nevratná.')
      if (!ok) return

      try {
        this.deletingProject = true
        await this.$api.delete(`/api/v1/projects/${this.projectId}`)
        // po úspěchu redirect
        this.$router.push('/dashboard')
      } catch (e: any) {
        this.deleteError = e?.response?.data?.message || 'Smazání projektu selhalo'
      } finally {
        this.deletingProject = false
      }
    },
    async updateProject() {
      this.updateError = ''

      try {
        this.updatingProject = true

        const body = {
          name: this.updateProjectData.name,
          description: this.updateProjectData.description,
          state: this.updateProjectData.state,
        }

        const { data } = await this.$api.put(
            `/api/v1/projects/${this.projectId}`,
            body
        )

        // aktualizuj lokálně
        this.project = data

      } catch (e: any) {
        this.updateError = e?.response?.data?.message || 'Update selhal'
      } finally {
        this.updatingProject = false
      }
    },
  },
}
</script>