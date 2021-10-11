/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import ClipsComponent from '@/entities/clips/clips.vue';
import ClipsClass from '@/entities/clips/clips.component';
import ClipsService from '@/entities/clips/clips.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Clips Management Component', () => {
    let wrapper: Wrapper<ClipsClass>;
    let comp: ClipsClass;
    let clipsServiceStub: SinonStubbedInstance<ClipsService>;

    beforeEach(() => {
      clipsServiceStub = sinon.createStubInstance<ClipsService>(ClipsService);
      clipsServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ClipsClass>(ClipsComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          clipsService: () => clipsServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      clipsServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllClipss();
      await comp.$nextTick();

      // THEN
      expect(clipsServiceStub.retrieve.called).toBeTruthy();
      expect(comp.clips[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      clipsServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeClips();
      await comp.$nextTick();

      // THEN
      expect(clipsServiceStub.delete.called).toBeTruthy();
      expect(clipsServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
