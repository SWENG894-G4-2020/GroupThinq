<template>
  <div class="q-pa-md">
    <q-table
      title="Results"
      dense
      :data="tabulatedResults"
      :columns="columns"
      color="primary"
      row-key="title"
      no-data-label="No results yet."
      :pagination="initialPagination"
    >
      <template v-slot:body-cell-name="props">
        <q-td :props="props">
          <div>
            {{props.value}}
          </div>
          <div class="option-desc">
            {{ props.row.description }}
          </div>
        </q-td>
      </template>
      <template v-slot:body-cell-winner="props">
        <q-td :props="props">
          <div v-if="props.row.winner">
            <q-icon name="done" class="text-green"/>
          </div>
        </q-td>
      </template>
    </q-table>
  </div>
</template>

<script>
export default {
  name: 'ResultsTable',
  data () {
    return {
      initialPagination: {
        sortBy: 'desc',
        descending: false,
        page: 1,
        rowsPerPage: 10
      },

      columns: [
        {
          name: 'name',
          required: true,
          label: 'Option',
          align: 'left',
          field: row => row.name,
          format: val => `${val}`,
          sortable: true
        },
        { name: 'winner', align: 'center', label: 'Winner', field: 'winner', sortable: true },
        { name: 'votes', align: 'center', label: '# of Votes', field: 'votes', sortable: true }
      ]
    }
  },

  props: {
    tabulatedResults: {
      type: Array,
      required: true
    }
  }
}
</script>

<style>
.option-desc {
  font-size: 0.85em;
  font-style: italic;
  max-width: 200px;
  white-space: normal;
  color: #555;
  margin-top: 4px;
}

</style>
