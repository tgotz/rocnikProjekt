<template>
  <div>
    <!-- Vykřičník -->
    <button @click="showPopup = true" class="btn fs-5 btn-sm">
      ❗
    </button>

    <!-- Popup -->
    <div v-if="showPopup" class="modal-backdrop">
      <div class="modal-content bg-dark">
        <h5 class="text-white">Nahlásit {{ type === 'character' ? 'postavu' : 'recenzi' }}</h5>
        <textarea v-model="description" required class="form-control" rows="4" placeholder="Popiš důvod nahlášení..."></textarea>

        <div class="mt-3 d-flex justify-content-between">
          <button class="btn btn-secondary" @click="showPopup = false">Zrušit</button>
          <button class="btn btn-primary" @click="submitReport">Odeslat</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import axios from "axios";

// Props pro ID a typ (character/review)
const props = defineProps({
  type: String, // "character" nebo "review"
  targetId: Number,
  userId: Number
});

const showPopup = ref(false);
const description = ref("");

const submitReport = async () => {
  try {
    const payload = {
      description: description.value,
      id_character: props.type === "character" ? props.targetId : null,
      id_review: props.type === "review" ? props.targetId : null,
    };
    if(!description.value.trim()){
      alert("Popis důvodu nahlášení je povinný!");
      return;
    }
    console.log(payload)
    await axios.post(`http://localhost:8080/api/reports/add?userId=${props.userId}`, payload, {
      withCredentials: true
    });

    alert("Nahlášení odesláno.");
    showPopup.value = false;
    description.value = "";
  } catch (err) {
    console.error("Chyba při odesílání reportu:", err);
    alert("Nepodařilo se odeslat nahlášení.");
  }
};
</script>

<style scoped>
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  padding: 1.5rem;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
}
</style>
