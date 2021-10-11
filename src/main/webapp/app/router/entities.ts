import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore

// prettier-ignore
const Clips = () => import('@/entities/clips/clips.vue');
// prettier-ignore
const ClipsUpdate = () => import('@/entities/clips/clips-update.vue');
// prettier-ignore
const ClipsDetails = () => import('@/entities/clips/clips-details.vue');
// prettier-ignore
const ClipUser = () => import('@/entities/clip-user/clip-user.vue');
// prettier-ignore
const ClipUserUpdate = () => import('@/entities/clip-user/clip-user-update.vue');
// prettier-ignore
const ClipUserDetails = () => import('@/entities/clip-user/clip-user-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default [
  {
    path: '/clips',
    name: 'Clips',
    component: Clips,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/clips/new',
    name: 'ClipsCreate',
    component: ClipsUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/clips/:clipsId/edit',
    name: 'ClipsEdit',
    component: ClipsUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/clips/:clipsId/view',
    name: 'ClipsView',
    component: ClipsDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/clip-user',
    name: 'ClipUser',
    component: ClipUser,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/clip-user/new',
    name: 'ClipUserCreate',
    component: ClipUserUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/clip-user/:clipUserId/edit',
    name: 'ClipUserEdit',
    component: ClipUserUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/clip-user/:clipUserId/view',
    name: 'ClipUserView',
    component: ClipUserDetails,
    meta: { authorities: [Authority.USER] },
  },
  // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
];
