/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import ClipsUpdateComponent from '@/entities/clips/clips-update.vue';
import ClipsClass from '@/entities/clips/clips-update.component';
import ClipsService from '@/entities/clips/clips.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Clips Management Update Component', () => {
    let wrapper: Wrapper<ClipsClass>;
    let comp: ClipsClass;
    let clipsServiceStub: SinonStubbedInstance<ClipsService>;

    beforeEach(() => {
      clipsServiceStub = sinon.createStubInstance<ClipsService>(ClipsService);

      wrapper = shallowMount<ClipsClass>(ClipsUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          clipsService: () => clipsServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.clips = entity;
        clipsServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(clipsServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.clips = entity;
        clipsServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(clipsServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundClips = { id: 123 };
        clipsServiceStub.find.resolves(foundClips);
        clipsServiceStub.retrieve.resolves([foundClips]);

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
