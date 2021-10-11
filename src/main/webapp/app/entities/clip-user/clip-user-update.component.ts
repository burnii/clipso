import { Component, Vue, Inject } from 'vue-property-decorator';

import UserService from '@/admin/user-management/user-management.service';

import ClipsService from '@/entities/clips/clips.service';
import { IClips } from '@/shared/model/clips.model';

import { IClipUser, ClipUser } from '@/shared/model/clip-user.model';
import ClipUserService from './clip-user.service';

const validations: any = {
  clipUser: {},
};

@Component({
  validations,
})
export default class ClipUserUpdate extends Vue {
  @Inject('clipUserService') private clipUserService: () => ClipUserService;
  public clipUser: IClipUser = new ClipUser();

  @Inject('userService') private userService: () => UserService;

  public users: Array<any> = [];

  @Inject('clipsService') private clipsService: () => ClipsService;

  public clips: IClips[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.clipUserId) {
        vm.retrieveClipUser(to.params.clipUserId);
      }
      vm.initRelationships();
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
    if (this.clipUser.id) {
      this.clipUserService()
        .update(this.clipUser)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A ClipUser is updated with identifier ' + param.id;
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.clipUserService()
        .create(this.clipUser)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A ClipUser is created with identifier ' + param.id;
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

  public retrieveClipUser(clipUserId): void {
    this.clipUserService()
      .find(clipUserId)
      .then(res => {
        this.clipUser = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.userService()
      .retrieve()
      .then(res => {
        this.users = res.data;
      });
    this.clipsService()
      .retrieve()
      .then(res => {
        this.clips = res.data;
      });
  }
}
