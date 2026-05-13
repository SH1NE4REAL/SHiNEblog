<script setup lang="ts">
import { computed, nextTick, ref, watch } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const videoRef = ref<HTMLVideoElement | null>(null)

const backdrop = computed(() => {
  if (route.path === '/') {
    return null
  }
  if (route.path.startsWith('/articles')) {
    return {
      source: '/uploads/backgrounds/article-dive.mp4',
      className: 'is-article-dive',
      start: 1.6,
      end: 4.2
    }
  }
  if (route.path.startsWith('/music')) {
    return {
      source: '/uploads/backgrounds/ui-music.mp4',
      className: 'is-music-ui',
      start: 0,
      end: 0
    }
  }
  return {
    source: '/uploads/backgrounds/ui-articles.mp4',
    className: 'is-project-ui',
    start: 0,
    end: 0
  }
})

watch(
  () => backdrop.value?.source,
  async () => {
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
  <div v-if="backdrop" class="route-video-backdrop" :class="backdrop.className" aria-hidden="true">
    <video
      ref="videoRef"
      :key="backdrop.source"
      :src="backdrop.source"
      autoplay
      muted
      loop
      playsinline
      preload="metadata"
      @loadedmetadata="handleLoaded"
      @timeupdate="loopSegment"
    ></video>
  </div>
</template>
