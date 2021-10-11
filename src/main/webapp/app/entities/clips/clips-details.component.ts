import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { IClips } from '@/shared/model/clips.model';
import ClipsService from './clips.service';

@Component
export default class ClipsDetails extends mixins(JhiDataUtils) {
  @Inject('clipsService') private clipsService: () => ClipsService;
  public clips: IClips = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.clipsId) {
        vm.retrieveClips(to.params.clipsId);
      }
    });
  }

  public retrieveClips(clipsId) {
    this.clipsService()
      .find(clipsId)
      .then(res => {
        this.clips = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
