<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="clipsApp.clipUser.home.createOrEditLabel" data-cy="ClipUserCreateUpdateHeading">Create or edit a ClipUser</h2>
        <div>
          <div class="form-group" v-if="clipUser.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="clipUser.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="clip-user-internalUser">Internal User</label>
            <select
              class="form-control"
              id="clip-user-internalUser"
              data-cy="internalUser"
              name="internalUser"
              v-model="clipUser.internalUser"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="clipUser.internalUser && userOption.id === clipUser.internalUser.id ? clipUser.internalUser : userOption"
                v-for="userOption in users"
                :key="userOption.id"
              >
                {{ userOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="clip-user-clips">Clips</label>
            <select class="form-control" id="clip-user-clips" data-cy="clips" name="clips" v-model="clipUser.clips">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="clipUser.clips && clipsOption.id === clipUser.clips.id ? clipUser.clips : clipsOption"
                v-for="clipsOption in clips"
                :key="clipsOption.id"
              >
                {{ clipsOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.clipUser.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./clip-user-update.component.ts"></script>
