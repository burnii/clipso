import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { required } from 'vuelidate/lib/validators';

import { IClips, Clips } from '@/shared/model/clips.model';
import ClipsService from './clips.service';

const validations: any = {
  clips: {
    name: {
      required,
    },
    content: {
      required,
    },
    positiveCount: {},
    negativeCount: {},
  },
};

@Component({
  validations,
})
export default class ClipsUpdate extends mixins(JhiDataUtils) {
  @Inject('clipsService') private clipsService: () => ClipsService;
  public clips: IClips = new Clips();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.clipsId) {
        vm.retrieveClips(to.params.clipsId);
      }
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.clips.id) {
      this.clipsService()
        .update(this.clips)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Clips is updated with identifier ' + param.id;
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.clipsService()
        .create(this.clips)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Clips is created with identifier ' + param.id;
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    }
  }

  public retrieveClips(clipsId): void {
    this.clipsService()
      .find(clipsId)
      .then(res => {
        this.clips = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public clearInputImage(field, fieldContentType, idInput): void {
    if (this.clips && field && fieldContentType) {
      if (Object.prototype.hasOwnProperty.call(this.clips, field)) {
        this.clips[field] = null;
      }
      if (Object.prototype.hasOwnProperty.call(this.clips, fieldContentType)) {
        this.clips[fieldContentType] = null;
      }
      if (idInput) {
        (<any>this).$refs[idInput] = null;
      }
    }
  }

  public initRelationships(): void {}
}
