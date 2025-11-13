<!-- pages/dashboard.vue -->
<template>
  <div class="space-y-10">
    <!-- Projekty -->
    <section>
      <div class="flex items-center justify-between mb-3">
        <h2 class="text-lg font-semibold">Moje projekty</h2>
        <button class="text-sm px-3 py-1 border rounded hover:bg-gray-50" :disabled="loadingProjects" @click="fetchProjects">Obnovit</button>
      </div>

      <div class="overflow-x-auto border rounded bg-white">
        <table class="w-full text-sm">
          <thead class="bg-gray-100 text-left">
          <tr>
            <th class="p-2">Název</th>
            <th class="p-2">Popis</th>
            <th class="p-2">Stav</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="p in projects" :key="p.id" class="border-t hover:bg-gray-50 cursor-pointer" @click="openProject(p.id)">
            <td class="p-2"><NuxtLink :to="`/projects/${p.id}`" class="underline">{{ p.name }}</NuxtLink></td>
            <td class="p-2">{{ p.description }}</td>
            <td class="p-2"><span class="px-2 py-0.5 rounded bg-gray-100">{{ p.state }}</span></td>
          </tr>
          <tr v-if="!loadingProjects && projects.length===0">
            <td colspan="3" class="p-3 text-gray-500">Žádné projekty.</td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Nový projekt -->
      <div class="mt-6 max-w-xl">
        <h3 class="font-medium mb-2">Přidat projekt</h3>
        <form @submit.prevent="createProject" class="space-y-3">
          <div>
            <label class="block text-sm mb-1">Název</label>
            <input v-model="projectForm.name" required class="w-full border rounded px-3 py-2" />
          </div>
          <div>
            <label class="block text-sm mb-1">Popis</label>
            <textarea v-model="projectForm.description" required class="w-full border rounded px-3 py-2" rows="3"></textarea>
          </div>
          <button type="submit" class="px-4 py-2 bg-black text-white rounded disabled:opacity-50" :disabled="creatingProject">Vytvořit</button>
          <span v-if="projectError" class="text-sm text-red-600 ml-2">{{ projectError }}</span>
        </form>
      </div>
    </section>

    <!-- Tickety -->
    <section>
      <div class="flex items-center justify-between mb-3">
        <h2 class="text-lg font-semibold">Přidělené tickety</h2>
        <button class="text-sm px-3 py-1 border rounded hover:bg-gray-50" :disabled="loadingTickets" @click="fetchTickets">Obnovit</button>
      </div>

      <div class="overflow-x-auto border rounded bg-white">
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
          <tr v-for="t in tickets" :key="t.id" class="border-t hover:bg-gray-50">
            <td class="p-2"><nuxt-link :to="`/projects/${t.projectId}/tickets/${t.id}`">{{ t.title }}</nuxt-link></td>
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
  </div>
</template>

<script lang="ts">
export default {
  data() {
    return {
      projects: [] as any[],
      tickets: [] as any[],
      loadingProjects: false,
      loadingTickets: false,
      projectForm: { name: '', description: '' },
      creatingProject: false,
      projectError: '',
    }
  },
  mounted() {
    this.fetchProjects();
    this.fetchTickets();
  },
  methods: {
    async fetchProjects() {
      try { this.loadingProjects = true; const { data } = await this.$api.get('/api/v1/projects'); this.projects = data }
      catch { /* optional toast */ }
      finally { this.loadingProjects = false }
    },
    async fetchTickets() {
      try { this.loadingTickets = true; const { data } = await this.$api.get('/api/v1/users/tickets/assigned'); this.tickets = data }
      catch { /* optional toast */ }
      finally { this.loadingTickets = false }
    },
    openProject(id:number) { this.$router.push(`/projects/${id}`) },
    async createProject() {
      this.projectError = ''
      try {
        this.creatingProject = true
        const body = { name: this.projectForm.name, description: this.projectForm.description }
        const { data } = await this.$api.post('/api/v1/projects', body)
        this.projects.unshift(data)
        this.projectForm.name = ''
        this.projectForm.description = ''
      } catch (e:any) {
        this.projectError = e?.response?.data?.message || 'Vytvoření projektu selhalo'
      } finally { this.creatingProject = false }
    },
  },
}
</script>