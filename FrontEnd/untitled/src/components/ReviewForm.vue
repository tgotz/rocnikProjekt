<template>
  <div>
    <div class="reviews text-center">
      <h3>Přidat recenzi</h3>
      <p></p>
      <!-- Šipka dolů -->
      <i
          class="arrowDown animate__animated"
          :class="showForm ? 'animate__fadeOut' : 'animate__fadeIn'"
          v-if="!showForm && isHidden"
          @click="openForm"
      ></i>

      <!-- Formulář recenze -->
      <div
          id="reviewForm"
          :class="[
          'review-form',
          'animate__animated',
          showForm ? 'animate__fadeIn' : 'animate__fadeOut',
          isHidden ? 'd-none' : ''
        ]"
      >
        <form @submit.prevent="submitReview">
          <div class="form-group">
            <label for="name">Vaše přezdívka (bude zobrazena u recenze):</label>
            <input
                v-model="formData.name"
                type="text"
                class="form-control"
                id="name"
                name="name"
                required
            />
          </div>
          <div class="form-group">
            <label for="overallRating">Celkové hodnocení:</label>
            <input
                v-model="formData.overallRating"
                min="0"
                max="10"
                step="1"
                type="range"
                class="form-range"
                id="overallRating"
            />
          </div>
          <div class="form-group">
            <label for="attractivenessRating">Hodnocení atraktivity postavy:</label>
            <input
                v-model="formData.attractivenessRating"
                min="0"
                max="10"
                step="1"
                type="range"
                class="form-range"
                id="attractivenessRating"
            />
          </div>
          <div class="form-group">
            <label for="reviewText">Text recenze:</label>
            <textarea
                v-model="formData.reviewText"
                class="form-control"
                id="reviewText"
                name="reviewText"
                rows="3"
                required
            ></textarea>
          </div>
          <button type="submit" class="btn btn-primary mt-3">Odeslat</button>
        </form>
        <!-- Šipka nahoru -->
        <i
            class="arrowUp animate__animated mt-4"
            :class="showForm ? 'animate__fadeIn' : 'animate__fadeOut'"
            v-if="showForm"
            @click="closeForm"
        ></i>
      </div>
    </div>
  </div>
</template>

---

### Logika ve scriptu

```javascript
<script>
export default {
  data() {
    return {
      showForm: false, // Řídí viditelnost formuláře (pro animace)
      isHidden: true, // Řídí úplné skrytí formuláře po animaci
      formData: {
        name: '',
        overallRating: 5,
        attractivenessRating: 5,
        reviewText: '',
      },
    };
  },
  methods: {
    openForm() {
      this.isHidden = false; // Zviditelni formulář
      this.showForm = true; // Spusť animaci "fadeIn"
    },
    closeForm() {
      this.showForm = false; // Spusť animaci "fadeOut"
      setTimeout(() => {
        this.isHidden = true; // Úplně skryj formulář po animaci
      }, 700); // Doba trvání animace
    },
    submitReview() {
      // Logika pro odeslání recenze
      console.log('Odesílám recenzi:', this.formData);
      // Tady by se volalo API pro odeslání recenze
      this.closeForm(); // Skryj formulář po odeslání
    },
  },
};
</script>
