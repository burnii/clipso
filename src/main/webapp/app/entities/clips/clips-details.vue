<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="clips">
        <h2 class="jh-entity-heading" data-cy="clipsDetailsHeading"><span>Clips</span> {{ clips.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Name</span>
          </dt>
          <dd>
            <span>{{ clips.name }}</span>
          </dd>
          <dt>
            <span>Content</span>
          </dt>
          <dd>
            <div v-if="clips.content">
              <a v-on:click="openFile(clips.contentContentType, clips.content)">
                <img
                  v-bind:src="'data:' + clips.contentContentType + ';base64,' + clips.content"
                  style="max-width: 100%"
                  alt="clips image"
                />
              </a>
              {{ clips.contentContentType }}, {{ byteSize(clips.content) }}
            </div>
          </dd>
          <dt>
            <span>Positive Count</span>
          </dt>
          <dd>
            <span>{{ clips.positiveCount }}</span>
          </dd>
          <dt>
            <span>Negative Count</span>
          </dt>
          <dd>
            <span>{{ clips.negativeCount }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link v-if="clips.id" :to="{ name: 'ClipsEdit', params: { clipsId: clips.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./clips-details.component.ts"></script>
