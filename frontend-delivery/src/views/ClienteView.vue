<script>
    import CreateOrder from '@/components/CreateOrder.vue'; 

    export default {
        name: 'ClientView',
        components: {
        CreateOrder
        },
    }
</script>

<!-- Example: src/views/Home.vue -->
<template>
    <div class="client">
      <CreateOrder />
    </div>
  </template>
  
