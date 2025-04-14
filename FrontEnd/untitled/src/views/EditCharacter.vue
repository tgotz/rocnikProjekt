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
      <div class="form-group mb-2">
        <label for="characterDescription">Popis</label>
        <textarea
            v-model="character.description"
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
              :src="'data:image/jpeg;base64,' + character.imageBytes"
              class="img-preview"
              alt="Character Image"
          />
        </div>
      </div>
      <div class="row">
        <div class="col-md-6">
          <div class="form-group mb-2">
            <label for="characterActor">Herec/herečka</label>
            <AutocompleteInput
                v-model="actorName"
                :search-url="'http://localhost:8080/api/actors/search'"
                placeholder="Zadejte jméno herce/herečky"
                :required="false"
            />
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group mb-2">
            <label for="characterActor">Daber/dabérka</label>
            <AutocompleteInput
                v-model="dabberName"
                :search-url="'http://localhost:8080/api/actors/search'"
                placeholder="Zadejte jméno dabéra/dabérky"
                :required="false"
            />
          </div>
        </div>
      </div>
      <div class="form-group mb-2">
        <div class="form-group mb-2">
          <AutocompleteMultiInput
              v-model="multiMovieInput"
              ref="multiMovieInputRef"
              name="movies"
          />
        </div>
      </div>
      <div class="form-group mb-2">
        <label for="characterQuotes">Hlášky</label>
        <textarea
            v-model="quotesInput"
            name="quotes"
            class="form-control"
            id="characterQuotes"
            rows="3"
            placeholder="Zadejte hlášky postavy oddělené středníkem (;)"
        ></textarea>
      </div>
      <button type="submit" class="btn btn-primary">Odeslat</button>
    </form>
  </div>
</template>

<script>
import axios from "axios";
import { useUserStore } from "../stores/userStore";
import AutocompleteInput from "@/components/AutocompleteInput.vue";
import AutocompleteMultiInput from "@/components/AutocompleteMultiInput.vue";

export default {
  components: {AutocompleteInput,
    AutocompleteMultiInput
  },
  computed: {
    dabberName: {
      get() {
        return this.character.dabber ? this.character.dabber.name : '';
      },
      set(value) {
        if (!this.character.dabber) {
          this.character.dabber = { name: value };
        } else {
          this.character.dabber.name = value;
        }
      }
    },
      actorName: {
        get() {
          return this.character.actor ? this.character.actor.name : '';
        },
        set(value) {
          if (!this.character.actor) {
            this.character.actor = { name: value };
          } else {
            this.character.actor.name = value;
          }
        }
      },

  },
  data() {
    return {
      character: {
        id: null,
        name: "",
        type: "",
        gender: "",
        movieList: "",
        desc: "",
        age: "",
        actorName: "",
        nickname: "",
      },
      multiMovieInput: "",
      imageFile: null,
      imagePreview: "",
      filmSuggestions: [],
      quotesInput: "",

    };
  },
  methods: {
    async fetchCharacter(id) {
      try {
        const response = await axios.get(`http://localhost:8080/api/character/${id}`);
        this.character = response.data.character;
        this.quotes = response.data.quotes;
        this.quotesInput = this.quotes.map(q => q.textQuote).join("\n");
        this.movies = response.data.character.movies;
        this.multiMovieInput = this.character.movies?.map(m => m.nameMovie).join("\n") || "";

        this.$nextTick(() => {
          if (this.$refs.multiMovieInputRef) {
            this.$refs.multiMovieInputRef.setText(this.multiMovieInput);
          }
        });
        if (response.data.image) {
          this.imagePreview = `data:image/jpeg;base64,${response.data.image}`;
        }
      } catch (error) {
        console.error("Error fetching character data:", error);
      }
    },
    prepareQuotesArray() {
      this.quotes = this.quotesInput
          .split(";")
          .map(q => q.trim())
          .filter(q => q.length > 0)
          .map(q => ({ textQuote: q }));
    },
    async fetchFilms(query) {
      // doesnt work yet
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
        this.prepareQuotesArray();

        const userStore = useUserStore();
        const userId = userStore.userId;

        if (!userId) {
          alert("Uživatel není přihlášen.");
          return;
        }

        // Ověření povinných polí
        if (!this.character.name || !this.character.type || !this.character.gender || !this.character.description) {
          alert("Vyplňte všechna povinná pole označená hvězdičkou.");
          return;
        }

        // ✅ Tady vytvořit FormData
        const formData = new FormData();

        // ✨ Převedení polí na správný formát
        const movieListString = this.multiMovieInput
            ? this.multiMovieInput.split("\n").map(item => item.trim()).filter(item => item.length > 0).join(";")
            : "";
        const quotesString = this.quotesInput
            ? this.quotesInput.split("\n").map(q => q.trim()).filter(q => q.length > 0).join(";")
            : "";

        // ✅ Naplnění dat
        formData.append("id", this.character.id);
        formData.append("name", this.character.name);
        formData.append("type", this.character.type);
        formData.append("gender", this.character.gender);
        formData.append("description", this.character.description);
        formData.append("age", this.character.age ?? "");
        formData.append("nickname", this.character.nickname ?? "");
        formData.append("actor", this.character.actor ? this.character.actor.name ?? "" : "");
        formData.append("dabber", this.character.dabber ? this.character.dabber.name ?? "" : "");
        formData.append("movies", movieListString);
        formData.append("quotes", quotesString);
        formData.append("userId", userId);
          console.log(formData)
        if (this.imageFile) {
          formData.append("picture", this.imageFile);
        }

        await axios.post("http://localhost:8080/api/character/update-character", formData, {
          withCredentials: true,
          headers: {
            "Content-Type": "multipart/form-data",
          },
        });

        alert("Postava byla úspěšně upravena!");
        this.$router.push("/dashboard");

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
