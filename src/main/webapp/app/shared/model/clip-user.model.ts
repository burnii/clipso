import { IUser } from '@/shared/model/user.model';
import { IClips } from '@/shared/model/clips.model';

export interface IClipUser {
  id?: number;
  internalUser?: IUser | null;
  clips?: IClips | null;
}

export class ClipUser implements IClipUser {
  constructor(public id?: number, public internalUser?: IUser | null, public clips?: IClips | null) {}
}
