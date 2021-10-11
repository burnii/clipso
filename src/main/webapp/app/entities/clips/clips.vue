<template>
  <div>
    <h2 id="page-heading" data-cy="ClipsHeading">
      <span id="clips-heading">Clips</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'ClipsCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-clips"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Clips </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && clips && clips.length === 0">
      <span>No clips found</span>
    </div>
    <div class="table-responsive" v-if="clips && clips.length > 0">
      <table class="table table-striped" aria-describedby="clips">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>User Id</span></th>
            <th scope="row"><span>Name</span></th>
            <th scope="row"><span>Content</span></th>
            <th scope="row"><span>Positive Count</span></th>
            <th scope="row"><span>Negative Count</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="clips in clips" :key="clips.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ClipsView', params: { clipsId: clips.id } }">{{ clips.id }}</router-link>
            </td>
            <td>{{ clips.userId }}</td>
            <td>{{ clips.name }}</td>
            <td>
              <a v-if="clips.content" v-on:click="openFile(clips.contentContentType, clips.content)">
                <img
                  v-bind:src="'data:' + clips.contentContentType + ';base64,' + clips.content"
                  style="max-height: 30px"
                  alt="clips image"
                />
              </a>
              <span v-if="clips.content">{{ clips.contentContentType }}, {{ byteSize(clips.content) }}</span>
            </td>
            <td>{{ clips.positiveCount }}</td>
            <td>{{ clips.negativeCount }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ClipsView', params: { clipsId: clips.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ClipsEdit', params: { clipsId: clips.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(clips)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="clipsApp.clips.delete.question" data-cy="clipsDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-clips-heading">Are you sure you want to delete this Clips?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-clips"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeClips()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./clips.component.ts"></script>
