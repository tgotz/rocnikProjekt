<template>
  <div>
    <ul class="list-group">
      <li class="list-group-item my-3" v-for="(review, index) in reviews" :key="index">
        <div class="d-flex justify-content-between">
          <strong>{{ review.user.username }} říká:</strong><br />
          <button v-if="user.role >= 3 || user.userId === review.user.id" @click="onDelete(review, user)" class="btn btn-danger btn-sm">
            Smazat

          </button>

        </div>
        <strong>Celkové hodnocení: {{ review.overallRating }}/10</strong><br />
        <strong>Hodnocení atraktivity: {{ review.attractivenessRating }}/10</strong><br />
        <strong>{{ review.reviewText }}</strong>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  props: {
    reviews: {
      type: Array,
      required: true,
    },
    user: Object
  },
  methods: {
    onDelete(review, user) {
      console.log(review.user.id)
      console.log(user.userId)
      console.log(user.role)
      if(review.user.id === user.userId || user.role >= 3){
        this.$emit("delete-review", review.id);
      }else{
        console.log("Nedostatečné oprávnění ke smazání recenze.")
      }
    }
}

  };
</script>
