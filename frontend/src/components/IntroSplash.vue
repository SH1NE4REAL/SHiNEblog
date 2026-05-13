<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref } from 'vue'

const visible = ref(false)
const closing = ref(false)
const introVideoSrc = '/uploads/intro/splash.mp4'
let closeTimer: number | undefined
let removeTimer: number | undefined

function closeIntro() {
  if (!visible.value || closing.value) {
    return
  }
  closing.value = true
  window.sessionStorage.setItem('shine_intro_seen', '1')
  removeTimer = window.setTimeout(() => {
    visible.value = false
  }, 920)
}

onMounted(() => {
  if (window.sessionStorage.getItem('shine_intro_seen') === '1') {
    return
  }
  visible.value = true
  closeTimer = window.setTimeout(closeIntro, 5200)
})

function cleanup() {
  window.clearTimeout(closeTimer)
  window.clearTimeout(removeTimer)
}

onBeforeUnmount(cleanup)
</script>

<template>
  <Transition name="intro-shell" @after-leave="cleanup">
    <div v-if="visible" class="intro-splash" :class="{ closing }">
      <video
        class="intro-video"
        :src="introVideoSrc"
        autoplay
        muted
        playsinline
        preload="auto"
        @ended="closeIntro"
      ></video>
      <div class="intro-flash" aria-hidden="true"></div>
      <div class="intro-shards" aria-hidden="true">
        <span v-for="index in 16" :key="index"></span>
      </div>
      <div class="intro-copy">
        <p>SHiNE BLOG</p>
        <strong>DIVE IN</strong>
      </div>
      <button type="button" class="intro-skip" @click="closeIntro">SKIP</button>
    </div>
  </Transition>
</template>
