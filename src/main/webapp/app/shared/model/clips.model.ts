export interface IClips {
  id?: number;
  name?: string;
  contentContentType?: string;
  content?: string;
  positiveCount?: number | null;
  negativeCount?: number | null;
}

export class Clips implements IClips {
  constructor(
    public id?: number,
    public name?: string,
    public contentContentType?: string,
    public content?: string,
    public positiveCount?: number | null,
    public negativeCount?: number | null
  ) {}
}
