/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ClipsDetailComponent from '@/entities/clips/clips-details.vue';
import ClipsClass from '@/entities/clips/clips-details.component';
import ClipsService from '@/entities/clips/clips.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Clips Management Detail Component', () => {
    let wrapper: Wrapper<ClipsClass>;
    let comp: ClipsClass;
    let clipsServiceStub: SinonStubbedInstance<ClipsService>;

    beforeEach(() => {
      clipsServiceStub = sinon.createStubInstance<ClipsService>(ClipsService);

      wrapper = shallowMount<ClipsClass>(ClipsDetailComponent, {
        store,
        localVue,
        router,
        provide: { clipsService: () => clipsServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundClips = { id: 123 };
        clipsServiceStub.find.resolves(foundClips);

        // WHEN
        comp.retrieveClips(123);
        await comp.$nextTick();

        // THEN
        expect(comp.clips).toBe(foundClips);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundClips = { id: 123 };
        clipsServiceStub.find.resolves(foundClips);

        // WHEN
        comp.beforeRouteEnter({ params: { clipsId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.clips).toBe(foundClips);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
