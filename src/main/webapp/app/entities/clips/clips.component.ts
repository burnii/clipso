import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IClips, Clips as Clip } from '@/shared/model/clips.model';

import JhiDataUtils from '@/shared/data/data-utils.service';

import ClipsService from './clips.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Clips extends mixins(JhiDataUtils) {
  @Inject('clipsService') private clipsService: () => ClipsService;
  private removeId: number = null;

  public clips: IClips[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllClipss();
  }

  public clear(): void {
    this.retrieveAllClipss();
  }

  public takeScreenshot(): void {
    const takeScreenshot2 = async () => {
      const stream = await navigator.mediaDevices.getDisplayMedia();

      const track = stream.getVideoTracks()[0];

      const image = new ImageCapture(track);

      const bitmap = await image.grabFrame();

      track.stop();

      const canvas = <HTMLCanvasElement>document.createElement('CANVAS');
      const ctx = canvas.getContext('2d');
      canvas.height = bitmap.height;
      canvas.width = bitmap.width;
      ctx.drawImage(bitmap, 0, 0);
      const dataURL = canvas.toDataURL();
      const base64string = dataURL.split(',')[1];
      console.log(dataURL);
      console.log(base64string);
      const clip: IClips = new Clip();
      clip.name = 'test';
      clip.negativeCount = 12;
      clip.positiveCount = 13;
      clip.content = base64string;
      clip.contentContentType = 'image/png';

      new ClipsService()
        .create(clip)
        .then(res => {
          console.log(res);
        })
        .catch(err => {
          console.log(err);
        });
    };

    takeScreenshot2();
  }

  public retrieveAllClipss(): void {
    this.isFetching = true;

    this.clipsService()
      .retrieve()
      .then(
        res => {
          this.clips = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IClips): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeClips(): void {
    this.clipsService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A Clips is deleted with identifier ' + this.removeId;
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllClipss();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
