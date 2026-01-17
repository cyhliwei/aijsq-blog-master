import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '@/views/home/index.vue'
import Layout from '@/layout/index.vue'
import NotFound from '@/views/404/404.vue'
import Article from '@/views/article/index.vue'
import Archive from '@/views/archives/index.vue'
import Categories from '@/views/categories/index.vue'
import Tags from '@/views/tags/index.vue'
import Messages from '@/views/messages/index.vue'
import About from '@/views/about/index.vue'
import Photos from '@/views/photos/index.vue'
import aiNavHome from '@/views/aiNavHome/aiNavHome.vue'
import waytoAGI from '@/views/waytoagi/index.vue'
import store from '@/store';

Vue.use(VueRouter)

const routes = [
  {
    path: "/",
    component: Layout,
    meta: {
        title: "AI技术圈-一个专注于技术分享的博客平台",
        loading: true
    },
    children: [
        {
          path: '/',
          name: 'Home',
          component: Home,
          meta: {
              title: '首页',
              transition: 'fade',
              icon: 'fas fa-home',
              loading: true
            }
        },
        // {
        //   path: '/archive',
        //   name: 'Archive',
        //   component: Archive,
        //   meta: { 
        //     transition: 'fade',
        //     title: '归档 - AI技术圈',
        //     icon: 'fas fa-archive'
        //   }
        // },
        {
          path: '/categories',
          name: 'Categories',
          component: Categories,
          meta: {
              transition: 'fade',
              title: "分类 - AI技术圈",
              icon: 'fas fa-folder'
            }
        },
        {
          path: '/tags',
          name: 'Tags',
          component: Tags,
          meta: {
              transition: 'fade',
              title: '标签 - AI技术圈',
              icon: 'fas fa-tags'
          }
        },
        // {
        //   path: '/moments',
        //   name: 'Moments',
        //   component: () => import('@/views/moments/index.vue'),
        //   meta: {
        //     title: '说说 - AI技术圈',
        //     icon: 'fas fa-comment-dots'
        //   }
        // },
        // {
        //   path: '/photos',
        //   name: 'Photos',
        //   component: Photos,
        //   meta: {
        //       transition: 'fade',
        //       title: '相册 - AI技术圈',
        //       icon: 'fas fa-images'
        //   }
        // },
        // {
        //   path: '/photos/:id',
        //   name: 'PhotoDetail',
        //   component: () => import('@/views/photos/detail.vue'),
        //   meta: {
        //       transition: 'fade',
        //       title: '相册详情 - AI技术圈',
        //       icon: 'fas fa-images',
        //       hidden: true
        //   }
        // },
        {
          path: '/hotSearch',
          name: 'HotSearch',
          component: () => import(/* webpackPrefetch: true */ '@/views/hotSearch/index.vue'),
          meta: { 
            transition: 'fade',
            title: '热搜 - AI技术圈',
            icon: 'fas fa-fire'
          }
        },
        {
          path: '/resources',
          name: 'Resources',
          component: () => import('@/views/resources/index.vue'),
          meta: {
            title: '资源',
            icon: 'fas fa-cloud-download-alt'

          }
        },
        // {
        //   path: '/messages',
        //   name: 'Messages',
        //   component: Messages,
        //   meta: { 
        //     transition: 'fade',
        //     title: '留言板 - AI技术圈',
        //     icon: 'fas fa-comments'
        //   }
        // },
        {
          path: '/friends',
          name: 'Friends',
          component: () => import(/* webpackPrefetch: true */ '@/views/friends/index.vue'),
          meta: { 
            transition: 'fade',
            title: '友情链接 - AI技术圈',
            icon: 'fas fa-user-friends'
          }
        },
        // {
        //   path: '/about',
        //   name: 'About',
        //   component: About,
        //   meta: { 
        //     transition: 'fade',
        //     title: '关于本站 - AI技术圈',
        //     icon: 'fas fa-info-circle'
        //   }
        // },
        {
          path: '/post/:id',
          name: 'Post',
          component: Article,
          props: true,
          meta: {
            hidden: true
          }
        },
        {
          path: '/user/profile',
          name: 'Profile',
          component: () => import(/* webpackPrefetch: true */ '@/views/profile/index.vue'),
          meta: {
            title: '个人主页 - AI技术圈',
            icon: 'fas fa-user',
            hidden: true
          }
        },
        {
          path: '/editor',
          name: 'Editor',
          component: () => import(/* webpackPrefetch: true */ '@/views/editor/index.vue'),
          meta: {
            title: '写文章 - AI技术圈',
            icon: 'fas fa-edit',
            requireAuth: true,
            hidden: true
          }
        },
        {
          path: '/aiNavHome/:cateId',
          name: 'AiNavHome',
          component: aiNavHome,
          meta: {
            title: '最新AI动态和资讯 - AI技术圈',
            icon: 'fas fa-edit',
            requireAuth: true,
            hidden: true
          }
        },
        {
          path: '/chat',
          name: 'Chat',
          component: () => import(/* webpackPrefetch: true */ '@/views/chat/index.vue'),
          meta: {
            title: '聊天 - AI技术圈',
            icon: 'fas fa-comments',
            hidden: true
          }
        },
        {
          path: '/login',
          name: 'Login',
          component: () => import('@/views/login/index.vue'),
          meta: {
            title: '登录',
            hidden: true,
            fullscreen: true
          }
        },
        {
          path: '/notifications',
          name: 'Notifications',
          component: () => import('@/views/notifications/index.vue'),
          meta: {
            title: '消息通知',
            requiresAuth: true,
            hidden: true
          }
        },
        {
          path: '/waytoagi/:menuId',
          name: 'waytoagi',
          component: waytoAGI,
          props: true,
          meta: {
            title: '通往AGI之路 - AI技术圈',
            hidden: true
          }
        },
        {
          path: '/waytoagi_editor/:type',
          name: 'waytoagi_editor',
          component: () => import('@/views/waytoagi/waytoagi_editor.vue'),
          meta: {
            title: '通往AGI之路 - AI技术圈',
            icon: 'fas fa-edit',
            requireAuth: true,
            hidden: true
          }
        },
        {
          path: '/shejishi_paths',
          name: 'shejishi_paths',
          component: () => import('@/views/shejishi_paths/shejishi.vue'),
          meta: {
            title: '设计师AI路径 - AI技术圈',
            icon: 'fas fa-edit',
            requireAuth: true,
            hidden: true
          }
        },
        {
          path: '/shejishi_paths/:type',
          name: 'shejishi_paths_type',
          component: () => import('@/views/shejishi_paths/shejishi_paths_editor.vue'),
          meta: {
            title: '设计师AI路径 - AI技术圈',
            icon: 'fas fa-edit',
            requireAuth: true,
            hidden: true
          }
        },
        {
          path: '/chenxuyuan_paths',
          name: 'chenxuyuan_paths',
          component: () => import('@/views/chenxuyuan_paths/index.vue'),
          meta: {
            title: '程序员AI路径 - AI技术圈',
            icon: 'fas fa-edit',
            requireAuth: true,
            hidden: true
          }
        },
        {
          path: '/chenxuyuan_paths/:type',
          name: 'chenxuyuan_paths_type',
          component: () => import('@/views/chenxuyuan_paths/chenxuyuan_editor.vue'),
          meta: {
            title: '程序员AI路径 - AI技术圈',
            icon: 'fas fa-edit',
            requireAuth: true,
            hidden: true
          }
        }
        ,
        {
          path: '/yunyin_paths',
          name: 'yunyin_paths',
          component: () => import('@/views/yunyin_paths/index.vue'),
          meta: {
            title: '运营人员AI路径 - AI技术圈',
            icon: 'fas fa-edit',
            requireAuth: true,
            hidden: true
          }
        },
        {
          path: '/yunyin_paths/:type',
          name: 'yunyin_paths_type',
          component: () => import('@/views/yunyin_paths/yunyin_paths_editor.vue'),
          meta: {
            title: '运营人员AI路径 - AI技术圈',
            icon: 'fas fa-edit',
            requireAuth: true,
            hidden: true
          }
        },
        {
          path: '/xuesheng_paths',
          name: 'xuesheng_paths',
          component: () => import('@/views/xuesheng_paths/index.vue'),
          meta: {
            title: '学生AI路径 - AI技术圈',
            icon: 'fas fa-edit',
            requireAuth: true,
            hidden: true
          }
        },
        {
          path: '/xuesheng_paths/:type',
          name: 'xuesheng_paths_type',
          component: () => import('@/views/xuesheng_paths/xuesheng_paths_editor.vue'),
          meta: {
            title: '学生AI路径 - AI技术圈',
            icon: 'fas fa-edit',
            requireAuth: true,
            hidden: true
          }
        },
        {
          path: '/guanli_paths',
          name: 'guanli_paths',
          component: () => import('@/views/guanli_paths/index.vue'),
          meta: {
            title: '管理者AI路径 - AI技术圈',
            icon: 'fas fa-edit',
            requireAuth: true,
            hidden: true
          }
        },
        {
          path: '/guanli_paths/:type',
          name: 'guanli_paths_type',
          component: () => import('@/views/guanli_paths/guanli_editor.vue'),
          meta: {
            title: '管理者AI路径 - AI技术圈',
            icon: 'fas fa-edit',
            requireAuth: true,
            hidden: true
          }
        },
        {
          path: '/job_paths',
          name: 'job_paths',
          component: () => import('@/views/job_paths/index.vue'),
          meta: {
            title: '不同岗位AI学习路线 - AI技术圈',
            icon: 'fas fa-edit',
            requireAuth: true,
            hidden: true
          }
        },
        {
          path: '/job_paths/:type',
          name: 'job_paths_type',
          component: () => import('@/views/job_paths/job_editor.vue'),
          meta: {
            title: '不同岗位AI学习路线 - AI技术圈',
            icon: 'fas fa-edit',
            requireAuth: true,
            hidden: true
          }
        }
        ,
        {
          path: '/:pathMatch(.*)*',
          name: 'NotFound',
          component: NotFound,
          meta: {
            hidden: true
          }
        }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: '/',
  routes,
  scrollBehavior(to, from, savedPosition) {
    return { x: 0, y: 0 }
  }
})


// 解决重复点击导航时，控制台出现报错
const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (to) {
  return VueRouterPush.call(this, to).catch(err => err)
}


router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title
  }
  //关闭搜索框
  store.commit('SET_SEARCH_VISIBLE', false)
  next()
})

export default router 