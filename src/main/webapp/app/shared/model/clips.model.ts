import { IClipUser } from '@/shared/model/clip-user.model';

export interface IClips {
  id?: number;
  userId?: number;
  name?: string;
  contentContentType?: string;
  content?: string;
  positiveCount?: number | null;
  negativeCount?: number | null;
  clipUsers?: IClipUser[] | null;
}

export class Clips implements IClips {
  constructor(
    public id?: number,
    public userId?: number,
    public name?: string,
    public contentContentType?: string,
    public content?: string,
    public positiveCount?: number | null,
    public negativeCount?: number | null,
    public clipUsers?: IClipUser[] | null
  ) {}
}
