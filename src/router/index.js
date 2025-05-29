import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/HomeView.vue'),
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue')
    },
    {
      path: '/control',
      name: 'control',
      children: [
        {
          path: '',
          redirect: 'pipline_table'
        },
        // { 
        //   path: '/area',
        //   name: 'area',
        //   component: () => import('../components/control/AreeManagement.vue') 
        // },
        {
          path: '/route',
          name: 'route',
          component: () => import('../components/control/route/RouteManagement.vue')
        },
        {
          path: '/pipline_table',
          name: 'pipline_table',
          component: () => import('../components/control/pipeline/PipelineTable.vue')
        },
        { 
          path: '/users', 
          name: 'users',
          component: () => import('../components/control/UserManagement.vue') 
        },
        {
          path: '/settings',
          name: 'settings',
          component: () => import('../components/control/Setting.vue'),
        }
      ],
      component: () => import('../views/ControlView.vue')
    },
  ],
})

export default router
