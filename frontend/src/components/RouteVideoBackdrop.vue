<script setup lang="ts">
import { computed, nextTick, ref, watch } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const videoRef = ref<HTMLVideoElement | null>(null)
const videoReady = ref(false)

const backdrop = computed(() => {
  if (route.path === '/') {
    return null
  }
  if (route.path.startsWith('/articles')) {
    return {
      source: '/uploads/backgrounds/article-dive.web.mp4',
      poster: '/uploads/backgrounds/article-dive-poster.jpg',
      className: 'is-article-dive',
      start: 0,
      end: 3.1
    }
  }
  if (route.path.startsWith('/music')) {
    return {
      source: '/uploads/backgrounds/ui-music.web.mp4',
      poster: '/uploads/backgrounds/ui-music-poster.jpg',
      className: 'is-music-ui',
      start: 0,
      end: 0
    }
  }
  return {
    source: '/uploads/backgrounds/ui-articles.web.mp4',
    poster: '/uploads/backgrounds/ui-articles-poster.jpg',
    className: 'is-project-ui',
    start: 0,
    end: 0
  }
})

watch(
  () => backdrop.value?.source,
  async () => {
    videoReady.value = false
    await nextTick()
    const video = videoRef.value
    const current = backdrop.value
    if (!video || !current) {
      return
    }
    video.currentTime = current.start
    video.play().catch(() => undefined)
  }
)

function handleLoaded() {
  const video = videoRef.value
  const current = backdrop.value
  if (!video || !current) {
    return
  }
  video.currentTime = current.start
}

function handleReady() {
  videoReady.value = true
}

function loopSegment() {
  const video = videoRef.value
  const current = backdrop.value
  if (!video || !current || current.end <= current.start) {
    return
  }
  if (video.currentTime >= current.end) {
    video.currentTime = current.start
    video.play().catch(() => undefined)
  }
}
</script>

<template>
  <div
    v-if="backdrop"
    class="route-video-backdrop"
    :class="[backdrop.className, { 'is-video-ready': videoReady }]"
    :style="{ '--route-poster': `url(${backdrop.poster})` }"
    aria-hidden="true"
  >
    <video
      ref="videoRef"
      :key="backdrop.source"
      :src="backdrop.source"
      :poster="backdrop.poster"
      autoplay
      muted
      loop
      playsinline
      webkit-playsinline
      x5-playsinline
      x5-video-player-type="h5-page"
      x5-video-player-fullscreen="false"
      preload="metadata"
      disablepictureinpicture
      disableremoteplayback
      controlslist="nodownload nofullscreen noremoteplayback"
      @loadedmetadata="handleLoaded"
      @loadeddata="handleReady"
      @canplay="handleReady"
      @timeupdate="loopSegment"
    ></video>
  </div>
</template>
