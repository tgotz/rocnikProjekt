<template>
  <div class="container bg-main pt-2">
    <h2 class="mt-5 mb-5 outline-heading">Upravte postavu</h2>
    <form @submit.prevent="submitForm" enctype="multipart/form-data">
      <div class="row">
        <div class="col-md-6">
          <input type="hidden" v-model="character.id" />
          <div class="form-group mb-2">
            <label for="characterName">Jméno postavy</label>
            <input
                v-model="character.name"
                name="name"
                type="text"
                class="form-control"
                id="characterName"
                placeholder="Zadej jméno postavy"
                required
            />
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group mb-2">
            <label for="characterType">Typ</label>
            <select v-model="character.type" name="type" class="form-control" id="characterType" required>
              <option value="Animovaná">Animovaná</option>
              <option value="Hraná">Hraná</option>
            </select>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-md-6">
          <div class="form-group mb-2">
            <label for="characterGender">Pohlaví</label>
            <select v-model="character.gender" name="gender" class="form-control" id="characterGender" required>
              <option value="Muž">Muž</option>
              <option value="Žena">Žena</option>
              <option value="Jiné">Jiné</option>
            </select>
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group mb-2">
            <label for="filmShow">Film/seriál</label>
            <input
                v-model="character.filmName"
                name="film"
                type="text"
                class="form-control"
                id="filmShow"
                placeholder="Zadej jméno filmu/seriálu"
                @keyup="fetchFilms"
                required
            />
            <div class="suggestions" v-if="filmSuggestions.length">
              <ul>
                <li v-for="film in filmSuggestions" :key="film" @click="selectFilm(film)">
                  {{ film }}
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      <div class="form-group mb-2">
        <label for="characterDescription">Popis</label>
        <textarea
            v-model="character.desc"
            name="desc"
            class="form-control"
            id="characterDescription"
            rows="3"
            placeholder="Zadej popis postavy"
            required
        ></textarea>
      </div>
      <div class="row">
        <div class="col-md-6">
          <div class="form-group mb-2">
            <label for="characterAge">Věk</label>
            <input
                v-model="character.age"
                name="age"
                type="number"
                class="form-control"
                id="characterAge"
                placeholder="Zadej věk postavy"
            />
          </div>
        </div>
        <div class="col-md-6 d-flex align-items-center justify-content-between">
          <div class="form-group mb-2 col-3">
            <label for="characterImage">Obrázek</label>
            <input @change="onFileChange" name="picture" type="file" class="form-control-file" id="characterImage" />
          </div>
          <img
              :src="'data:image/jpeg;base64,' + character.image"
              class="img-preview"
              alt="Character Image"
          />
        </div>
      </div>
      <div class="row">
        <div class="col-md-6">
          <div class="form-group mb-2">
            <label for="characterActor">Herec/herečka (dabér, dabérka)</label>
            <input
                v-model="character.actorName"
                name="actor"
                type="text"
                class="form-control"
                id="characterActor"
                placeholder="Zadej jméno herce/herečky"
                required
            />
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group mb-2">
            <label for="characterNickname">Přezdívka</label>
            <input
                v-model="character.nickname"
                name="nickname"
                type="text"
                class="form-control"
                id="characterNickname"
                placeholder="Zadej přezdívku postavy"
            />
          </div>
        </div>
      </div>
      <button type="submit" class="btn btn-primary">Odeslat</button>
    </form>
  </div>
</template>

<script>
import axios from "axios";
import { useUserStore } from "../stores/userStore";
export default {
  data() {
    return {
      character: {
        id: null,
        name: "",
        type: "",
        gender: "",
        filmName: "",
        desc: "",
        age: "",
        actorName: "",
        nickname: "",
      },
      imageFile: null,
      imagePreview: "",
      filmSuggestions: [],
    };
  },
  methods: {
    async fetchCharacter(id) {
      try {
        const response = await axios.get(`http://localhost:8080/character`, {
          params: { id: id }, // Posíláme správné ID postavy
        });
        // Nastavení dat do `character` objektu
        this.character = response.data.character;
        this.quotes = response.data.quotes;
        // Nastavení obrázku
        if (response.data.image) {
          this.imagePreview = `data:image/jpeg;base64,${response.data.image}`;
        }
      } catch (error) {
        console.error("Error fetching character data:", error);
      }
    },

    async fetchFilms(query) {
      // Fetch film suggestions (mocked or API endpoint)
      this.filmSuggestions = ["Film 1", "Film 2", "Film 3"].filter((film) =>
          film.toLowerCase().includes(query.toLowerCase())
      );
    },
    selectFilm(film) {
      this.character.filmName = film;
      this.filmSuggestions = [];
    },
    onFileChange(event) {
      this.imageFile = event.target.files[0];
      const reader = new FileReader();
      reader.onload = (e) => {
        this.imagePreview = e.target.result;
      };
      reader.readAsDataURL(this.imageFile);
    },
    async submitForm() {
      try {
        const userStore = useUserStore();
        const userId =         userStore.userId;

        if (!userId) {
          alert("Uživatel není přihlášen.");
          return;
        }

        // Vytvoření FormData objektu pro odeslání dat
        const formData = new FormData();
        formData.append("id", this.character.id);
        formData.append("name", this.character.name);
        formData.append("type", this.character.type);
        formData.append("gender", this.character.gender);
        formData.append("film", this.character.filmName);
        formData.append("desc", this.character.desc);
        formData.append("age", this.character.age);
        formData.append("actor", this.character.actorName);
        formData.append("nickname", this.character.nickname);

        // Přidání userId
        formData.append("userId", userId);

        // Přidání obrázku, pokud byl nahrán
        if (this.imageFile) {
          formData.append("picture", this.imageFile);
        }

        // Odeslání požadavku na server
        await axios.post("http://localhost:8080/api/update-character", formData, {
          withCredentials: true, // Zahrnout cookies
          headers: {
            "Content-Type": "multipart/form-data",
          },
        });

        // Po úspěšném odeslání
        alert("Postava byla úspěšně upravena!");
        this.$router.push("/dashboard"); // Přesměrování na dashboard
      } catch (error) {
        console.error("Chyba při aktualizaci postavy:", error);
        alert("Při aktualizaci postavy došlo k chybě.");
      }
    }

  },
  mounted() {
    const characterId = this.$route.params.id; // ID získané z URL
    if (characterId) {
      this.fetchCharacter(characterId); // Načtení dat postavy
    } else {
      console.error("Character ID not found in route parameters");
    }
    }
};
</script>
<style>
.img-preview{
  max-width: 50% !important;
}
</style>
