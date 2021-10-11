<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="clipsApp.clips.home.createOrEditLabel" data-cy="ClipsCreateUpdateHeading">Create or edit a Clips</h2>
        <div>
          <div class="form-group" v-if="clips.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="clips.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="clips-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="clips-name"
              data-cy="name"
              :class="{ valid: !$v.clips.name.$invalid, invalid: $v.clips.name.$invalid }"
              v-model="$v.clips.name.$model"
              required
            />
            <div v-if="$v.clips.name.$anyDirty && $v.clips.name.$invalid">
              <small class="form-text text-danger" v-if="!$v.clips.name.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="clips-content">Content</label>
            <div>
              <img
                v-bind:src="'data:' + clips.contentContentType + ';base64,' + clips.content"
                style="max-height: 100px"
                v-if="clips.content"
                alt="clips image"
              />
              <div v-if="clips.content" class="form-text text-danger clearfix">
                <span class="pull-left">{{ clips.contentContentType }}, {{ byteSize(clips.content) }}</span>
                <button
                  type="button"
                  v-on:click="clearInputImage('content', 'contentContentType', 'file_content')"
                  class="btn btn-secondary btn-xs pull-right"
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                </button>
              </div>
              <input
                type="file"
                ref="file_content"
                id="file_content"
                data-cy="content"
                v-on:change="setFileData($event, clips, 'content', true)"
                accept="image/*"
              />
            </div>
            <input
              type="hidden"
              class="form-control"
              name="content"
              id="clips-content"
              data-cy="content"
              :class="{ valid: !$v.clips.content.$invalid, invalid: $v.clips.content.$invalid }"
              v-model="$v.clips.content.$model"
              required
            />
            <input
              type="hidden"
              class="form-control"
              name="contentContentType"
              id="clips-contentContentType"
              v-model="clips.contentContentType"
            />
            <div v-if="$v.clips.content.$anyDirty && $v.clips.content.$invalid">
              <small class="form-text text-danger" v-if="!$v.clips.content.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="clips-positiveCount">Positive Count</label>
            <input
              type="number"
              class="form-control"
              name="positiveCount"
              id="clips-positiveCount"
              data-cy="positiveCount"
              :class="{ valid: !$v.clips.positiveCount.$invalid, invalid: $v.clips.positiveCount.$invalid }"
              v-model.number="$v.clips.positiveCount.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="clips-negativeCount">Negative Count</label>
            <input
              type="number"
              class="form-control"
              name="negativeCount"
              id="clips-negativeCount"
              data-cy="negativeCount"
              :class="{ valid: !$v.clips.negativeCount.$invalid, invalid: $v.clips.negativeCount.$invalid }"
              v-model.number="$v.clips.negativeCount.$model"
            />
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
            :disabled="$v.clips.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./clips-update.component.ts"></script>
