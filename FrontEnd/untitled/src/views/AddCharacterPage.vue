<template>
  <div class="container mt-5 bg-main pt-2">
    <h2 class="mb-5 outline-heading">Přidejte novou postavu</h2>
    <div v-if="outcome" class="alert" :class="{'alert-success': success, 'alert-danger': !success}">
      {{ outcome }}
    </div>
    <form @submit.prevent="submitForm" enctype="multipart/form-data">
      <div class="row">
        <div class="col-md-6">
          <div class="form-group mb-2">
            <label for="characterName">Jméno postavy *</label>
            <input
                v-model="formData.name"
                name="name"
                type="text"
                class="form-control"
                id="characterName"
                placeholder="Zadejte jméno postavy"
                required
            />
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group mb-2">
            <label for="characterType">Typ *</label>
            <select
                v-model="formData.type"
                name="type"
                class="form-control"
                id="characterType"
                required
            >
              <option value="Animovaná">Animovaná</option>
              <option value="Hraná">Hraná</option>
            </select>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-md-6">
          <div class="form-group mb-2">
            <label for="characterGender">Pohlaví *</label>
            <select
                v-model="formData.gender"
                name="gender"
                class="form-control"
                id="characterGender"
                required
            >
              <option value="Muž">Muž</option>
              <option value="Žena">Žena</option>
              <option value="Jiné">Jiné</option>
            </select>
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group mb-2">
            <label for="filmShow">Film/seriál *</label>
            <input
                v-model="formData.film"
                name="film"
                type="text"
                class="form-control"
                id="filmShow"
                placeholder="Zadejte název filmu/seriálu"
                required
            />
          </div>
        </div>
      </div>
      <div class="form-group mb-2">
        <label for="characterDescription">Popis *</label>
        <textarea
            v-model="formData.desc"
            name="desc"
            class="form-control"
            id="characterDescription"
            rows="3"
            placeholder="Zadejte popis postavy"
            required
        ></textarea>
      </div>
      <div class="row">
        <div class="col-md-6">
          <div class="form-group mb-2">
            <label for="characterAge">Věk</label>
            <input
                v-model="formData.age"
                name="age"
                type="number"
                class="form-control"
                id="characterAge"
                placeholder="Zadejte věk postavy"
            />
          </div>
        </div>
        <div class="col-md-6 d-flex align-items-center">
          <div class="form-group mb-2">
            <label for="characterImage">Obrázek *</label>
            <input
                @change="onFileChange"
                name="picture"
                type="file"
                class="form-control-file"
                id="characterImage"
                required
                ref="fileInput"
            />
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-md-6">
          <div class="form-group mb-2">
            <label for="characterActor">Herec/herečka(dabér, dabérka) *</label>
            <input
                v-model="formData.actor"
                name="actor"
                type="text"
                class="form-control"
                id="characterActor"
                placeholder="Zadejte jméno herce/herečky"
                required
            />
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group mb-2">
            <label for="characterNickname">Přezdívka</label>
            <input
                v-model="formData.nickname"
                name="nickname"
                type="text"
                class="form-control"
                id="characterNickname"
                placeholder="Zadejte přezdívku postavy"
            />
          </div>
        </div>
      </div>
      <div class="form-group mb-2">
        <label for="characterQuotes">Hlášky</label>
        <textarea
            v-model="formData.quotes"
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

---

### 2. **Přidejte JavaScript pro zpracování dat**

```javascript
<script>
import axios from "axios";

export default {

  data() {
    return {
      formData: {
        name: "",
        type: "Animovaná",
        gender: "Muž",
        film: "",
        desc: "",
        age: "",
        actor: "",
        nickname: "",
        quotes: "",
        picture: null,
      },
      outcome: null,
      success: false,
    };
  },
  methods: {
    async submitForm() {
      try {
        const form = new FormData();
        Object.keys(this.formData).forEach((key) => {
          form.append(key, this.formData[key]);
        });

        const response = await axios.post(
            "http://localhost:8080/AddCharacterServlet",
            form,
            {
              headers: {
                "Content-Type": "multipart/form-data",
              },
            }
        );

        this.outcome = response.data.message;
        this.success = true;
        this.resetForm();
      } catch (error) {
        console.error(error);
        this.outcome = "Chyba při přidávání postavy.";
        this.success = false;
      }
    },
    resetForm() {
      this.formData = {
        name: "",
        type: "Animovaná",
        gender: "Muž",
        film: "",
        desc: "",
        age: "",
        actor: "",
        nickname: "",
        quotes: "",
        picture: null,
      };

      const fileInput = this.$refs.fileInput;
      if (fileInput) {
        fileInput.value = '';
      }
    },
    onFileChange(event) {
      const file = event.target.files[0];
      this.formData.picture = file;
      console.log("Selected file:", file);
    },
  },
};
</script>
