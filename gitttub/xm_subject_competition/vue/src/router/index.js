import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/login' },
    {
      path: '/manager',
      component: () => import('@/views/Manager.vue'),
      children: [
        { path: 'home', meta: { name: '系统首页' }, component: () => import('@/views/manager/Home.vue'),  },
        { path: 'admin', meta: { name: '管理员信息' }, component: () => import('@/views/manager/Admin.vue'), },
        { path: 'notice', meta: { name: '系统公告' }, component: () => import('@/views/manager/Notice.vue'), },
        { path: 'person', meta: { name: '个人资料' }, component: () => import('@/views/manager/Person.vue'), },
        { path: 'password', meta: { name: '修改密码' }, component: () => import('@/views/manager/Password.vue'), },
        { path: 'award', meta: { name: '赛事奖项' }, component: () => import('@/views/manager/Award.vue'), },
        { path: 'collect', meta: { name: '收藏信息' }, component: () => import('@/views/manager/Collect.vue'), },
        { path: 'drawCash', meta: { name: '提现记录' }, component: () => import('@/views/manager/DrawCash.vue'), },
        { path: 'event', meta: { name: '赛事信息' }, component: () => import('@/views/manager/Event.vue'), },
        { path: 'eventResult', meta: { name: '赛事结果' }, component: () => import('@/views/manager/EventResult.vue'), },
        { path: 'host', meta: { name: '主办方信息' }, component: () => import('@/views/manager/Host.vue'), },
        { path: 'news', meta: { name: '赛事资讯' }, component: () => import('@/views/manager/News.vue'), },
        { path: 'sign', meta: { name: '赛事报名' }, component: () => import('@/views/manager/Sign.vue'), },
        { path: 'type', meta: { name: '赛事类型' }, component: () => import('@/views/manager/Type.vue'), },
        { path: 'user', meta: { name: '用户信息' }, component: () => import('@/views/manager/User.vue'), },
        { path: 'carousel', meta: { name: '轮播图信息' }, component: () => import('@/views/manager/Carousel.vue'), },
        { path: 'dashboard', meta: { name: '数据统计' }, component: () => import('@/views/manager/Dashboard.vue'), },
      ]
    },
    {
      path: '/front',
      component: () => import('@/views/Front.vue'),
      children: [
        { path: 'home', component: () => import('@/views/front/Home.vue'),  },
        { path: 'person', component: () => import('@/views/front/Person.vue'),  },
        { path: 'password', component: () => import('@/views/front/Password.vue'),  },
        { path: 'event', component: () => import('@/views/front/Event.vue'),  },
        { path: 'news', component: () => import('@/views/front/News.vue'),  },
        { path: 'notice', component: () => import('@/views/front/Notice.vue'),  },
        { path: 'userDrawCash', component: () => import('@/views/front/UserDrawCash.vue'),  },
        { path: 'newsDetail', component: () => import('@/views/front/NewsDetail.vue'),  },
        { path: 'eventDetail', component: () => import('@/views/front/EventDetail.vue'),  },
        { path: 'userSign', component: () => import('@/views/front/UserSign.vue'),  },
        { path: 'userEventResult', component: () => import('@/views/front/UserEventResult.vue'),  },
        { path: 'userCollect', component: () => import('@/views/front/UserCollect.vue'),  },
      ]
    },
    { path: '/login', component: () => import('@/views/Login.vue') },
    { path: '/register', component: () => import('@/views/Register.vue') },
    { path: '/404', component: () => import('@/views/404.vue') },
    { path: '/:pathMatch(.*)', redirect: '/404' }
  ]
})

router.afterEach(() => {
  setTimeout(() => {
    window.scroll({ top: 0, behavior: 'smooth' })
  }, 0)
})

export default router
