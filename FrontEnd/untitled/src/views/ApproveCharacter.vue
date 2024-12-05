<template>
  <div class="container mt-5 bg-main pt-2">
    <div class="d-flex justify-content-between align-items-center">
      <button
          :disabled="currentIndex === 0"
          @click="prevCharacter"
          class="btn btn-link"
      >
        Předchozí
      </button>
      <h2>Žádost o přidání</h2>
      <button
          :disabled="currentIndex === characters.length - 1"
          @click="nextCharacter"
          class="btn btn-link"
      >
        Další
      </button>
    </div>
    <hr />

    <!-- Zobrazení postavy nebo zpráva -->
    <div v-if="currentCharacter" class="row">
      <div class="col-md-8">
        <h3>Jméno: {{ currentCharacter.name }}</h3>
        <p v-if="currentCharacter.nickname">
          Přezdívka: {{ currentCharacter.nickname }}
        </p>
        <p v-if="currentCharacter.age !== 999">Věk: {{ currentCharacter.age }}</p>
        <p>Pohlaví: {{ currentCharacter.gender }}</p>
        <p>Typ: {{ currentCharacter.type }}</p>
        <p>Herec/dabér: {{ currentCharacter.actorName }}</p>
        <p>Název filmu: {{ currentCharacter.filmName }}</p>
        <p>{{ currentCharacter.description }}</p>
      </div>
      <div class="col-md-4">
        <img
            :src="'data:image/jpeg;base64,' + currentCharacter.image"
            class="img-fluid"
            alt="Character Image"
        />
      </div>
      <div v-if="currentCharacter.quotes.length">
        <h3>Hlášky</h3>
        <ul>
          <li v-for="(quote, index) in currentCharacter.quotes" :key="index">
            {{ quote }}
          </li>
        </ul>
      </div>
    </div>
    <p v-else class="text-center mt-4">
      Aktuálně žádné postavy nečekají na schválení.
    </p>

    <!-- Schválení/Odmítnutí -->
    <hr />
    <div class="d-flex justify-content-between">
      <button
          class="btn btn-danger fs-4"
          @click="rejectCharacter(currentCharacter.id)"
          v-if="currentCharacter"
      >
        &#10008; Odmítnout
      </button>
      <button
          class="btn btn-success fs-4"
          @click="approveCharacter(currentCharacter.id)"
          v-if="currentCharacter"
      >
        &#10004; Schválit
      </button>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { useUserStore } from "@/stores/userStore";
import { ref, computed, onMounted } from "vue";

export default {
  setup() {
    const characters = ref([]);
    const currentIndex = ref(0);
    const userStore = useUserStore();

    // Načtení všech postav
    const fetchCharacters = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/approve", {
          withCredentials: true,
        });
        characters.value = response.data;
      } catch (error) {
        console.error("Chyba při načítání postav:", error);
      }
    };

    // Aktuální postava
    const currentCharacter = computed(() => characters.value[currentIndex.value]);

    // Přepnutí na další postavu
    const nextCharacter = () => {
      if (currentIndex.value < characters.value.length - 1) {
        currentIndex.value++;
      }
    };

    // Přepnutí na předchozí postavu
    const prevCharacter = () => {
      if (currentIndex.value > 0) {
        currentIndex.value--;
      }
    };

    // Schválení postavy
    const approveCharacter = async (id) => {
      console.log(id);
      console.log(userStore.userId);
      try {
        // Odeslání POST požadavku na schválení postavy
        const response = await axios.post(
            "http://localhost:8080/api/approve-character",
            {
              id: id, // ID postavy
              userId: userStore.userId // ID uživatele (přihlašovací ID)
            },
            {
              withCredentials: true
            }
        );

        // Kontrola úspěšné odpovědi
        if (response.status === 200) {
          // Odebrání schválené postavy ze seznamu
          characters.value.splice(currentIndex.value, 1);

          // Posun na další postavu, pokud existuje
          if (currentIndex.value >= characters.value.length) {
            currentIndex.value = characters.value.length - 1;
          }
        }
      } catch (error) {
        console.error("Chyba při schvalování postavy:", error);
      }
    };

    // Odmítnutí postavy
    const rejectCharacter = async (id) => {
      console.log(id)
      try {
        await axios.post(
            `http://localhost:8080/api/delete-character?id=${id}`,
            {},
            { withCredentials: true }
        );
        characters.value.splice(currentIndex.value, 1);
        if (currentIndex.value >= characters.value.length) {
          currentIndex.value = characters.value.length - 1;
        }
      } catch (error) {
        console.error("Chyba při odmítání postavy:", error);
      }
    };

    onMounted(fetchCharacters);

    return {
      characters,
      currentIndex,
      currentCharacter,
      nextCharacter,
      prevCharacter,
      approveCharacter,
      rejectCharacter,
    };
  },
};
</script>
