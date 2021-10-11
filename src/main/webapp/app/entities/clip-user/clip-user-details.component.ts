import { Component, Vue, Inject } from 'vue-property-decorator';

import { IClipUser } from '@/shared/model/clip-user.model';
import ClipUserService from './clip-user.service';

@Component
export default class ClipUserDetails extends Vue {
  @Inject('clipUserService') private clipUserService: () => ClipUserService;
  public clipUser: IClipUser = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.clipUserId) {
        vm.retrieveClipUser(to.params.clipUserId);
      }
    });
  }

  public retrieveClipUser(clipUserId) {
    this.clipUserService()
      .find(clipUserId)
      .then(res => {
        this.clipUser = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
