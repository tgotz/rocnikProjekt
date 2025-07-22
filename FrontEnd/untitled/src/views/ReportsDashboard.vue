<template>
  <div class="container mt-5 bg-main pt-3 pb-5">
    <h2 class="mb-4">Přehled nahlášených položek</h2>

    <div v-if="isLoading" class="text-center">
      <span>Načítání dat...</span>
    </div>

    <div v-else>
      <div
          v-for="(group, index) in combinedReports"
          :key="index"
          class="card mb-3"
      >
        <div class="card-header d-flex justify-content-between align-items-center">
          <div>
            <strong v-if="group.type === 'character'">Postava:</strong>
            <strong v-else>Recenze:</strong>

            <template v-if="group.type === 'character'">
              <a
                  :href="`/detail/${group.id}`"
                  target="_blank"
                  class="text-decoration-none text-dark"
              >
                {{ group.name }}
              </a>
            </template>

            <template v-else>
              {{ group.name }}
              <span class="text-muted">({{ group.username }})</span>
            </template>

            <br />
            <small>
              Počet hlášení: {{ group.count }} |
              Poslední:
              {{
                group.latest
                    ? formatDate(group.latest)
                    : "—"
              }}
            </small>
          </div>

          <div>
            <button
                class="btn btn-sm btn-outline-secondary"
                @click="toggleDetails(index)"
            >
              {{ group.showDetails ? 'Skrýt' : 'Zobrazit' }} detail
            </button>

            <button
                class="btn btn-sm btn-success ms-2"
                @click="resolveAll(group)"
            >
              Vyřešit vše
            </button>
          </div>
        </div>
        <div v-if="group.showDetails" class="card-body">
          <div v-if="group.type === 'review'">
          <p>Text recenze: {{group.reports[0].review.reviewText}}</p>
          <p>Recente je u postavy:
            <a
                :href="`/detail/${group.reports[0].review.character.id}`"
                target="_blank"
                class="text-decoration-none text-dark"
            >
              {{ group.reports[0].review.character.name}}
            </a>
          </p>
          </div>
          <ul>
            <li  class="pb-1" v-for="(report, rIndex) in group.reports" :key="rIndex">
              {{ formatDate(report.dateReported) }} — {{ report.reportedBy.username }}:
              <em>{{ report.description }}</em>
              <button
                  class="btn btn-sm btn-outline-success ms-2"
                  @click="resolveSingle(report.id)"
              >
                Vyřešit
              </button>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      characterReports: [],
      reviewReports: [],
      isLoading: true,
      combinedReports: [],
    };
  },
  methods: {
    async fetchReports() {
      try {
        const [charRes, revRes] = await Promise.all([
          axios.get(`${import.meta.env.VITE_API_URL}/api/reports/characters`, {
            withCredentials: true,
          }),
          axios.get(`${import.meta.env.VITE_API_URL}/api/reports/reviews`, {
            withCredentials: true,
          }),
        ]);

        const characters = await Promise.all(
            charRes.data.map(async ([id, name, count, latest]) => {
              const detail = await axios.get(
                  `${import.meta.env.VITE_API_URL}/api/reports/character/${id}`,
                  { withCredentials: true }
              );
              return {
                id,
                name,
                count,
                latest,
                type: "character",
                reports: detail.data,
                showDetails: false,
              };
            })
        );

        const reviews = await Promise.all(
            revRes.data.map(async ([id, username, count, latest]) => {
              const detail = await axios.get(
                  `${import.meta.env.VITE_API_URL}/api/reports/review/${id}`,
                  { withCredentials: true }
              );
              return {
                id,
                name: `Recenze #${id}`,
                username,
                count,
                latest,
                type: "review",
                reports: detail.data,
                showDetails: false,
              };
            })
        );

        this.combinedReports = [...characters, ...reviews].sort(
            (a, b) => b.count - a.count
        );
      } catch (err) {
        console.error("Chyba při načítání reportů:", err);
      } finally {
        this.isLoading = false;
      }
    },
    async resolveAll(group) {
      const endpoint =
          group.type === "character"
              ? `/api/reports/resolve/character/${group.id}`
              : `/api/reports/resolve/review/${group.id}`;

      try {
        await axios.post(`${import.meta.env.VITE_API_URL}${endpoint}`, {}, { withCredentials: true });
        alert("Reporty byly označeny jako vyřešené.");
        this.fetchReports(); // Refresh
      } catch (err) {
        console.error("Chyba při řešení reportů:", err);
      }
    },
    async resolveSingle(reportId) {
      try {
        await axios.put(`${import.meta.env.VITE_API_URL}/api/reports/${reportId}/resolve?solvedById=1`, {}, {
          withCredentials: true,
        });
        alert("Report označen jako vyřešený.");
        this.fetchReports(); // Refresh
      } catch (err) {
        console.error("Chyba při řešení reportu:", err);
      }
    },
    toggleDetails(index) {
      this.combinedReports[index].showDetails = !this.combinedReports[index].showDetails;
    },
    formatDate(dateString) {
      const date = new Date(dateString);
      return date.toLocaleString("cs-CZ", {
        dateStyle: "short",
        timeStyle: "short",
      });
    },
  },
  mounted() {
    this.fetchReports();
  },
};
</script>

<style scoped>
.card-header {
  background-color: #f8f9fa;
}
ul {
  padding-left: 1.2rem;
}
</style>
