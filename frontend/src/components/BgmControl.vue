<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref } from 'vue'
import { Music2, Pause, Play } from 'lucide-vue-next'

const audioRef = ref<HTMLAudioElement | null>(null)
const playing = ref(false)
const ready = ref(false)
const error = ref('')
const audioSrc = ref('')
const bgmSrc = '/uploads/bgm/bgm.web.mp3'
const fallbackBgmSrc = '/uploads/bgm/bgm.mp3'
let shouldResumeAfterFallback = false

async function playBgm(showError = true) {
  if (!audioSrc.value) {
    audioSrc.value = bgmSrc
  }
  const audio = audioRef.value
  if (!audio) {
    return false
  }

  try {
    audio.volume = 0.32
    await audio.play()
    error.value = ''
    return true
  } catch {
    if (showError) {
      error.value = '点击播放'
    }
    return false
  }
}

async function toggleBgm() {
  if (playing.value) {
    audioRef.value?.pause()
    return
  }
  await playBgm()
}

function pauseBgm() {
  audioRef.value?.pause()
}

function handlePlay() {
  playing.value = true
}

function handlePause() {
  playing.value = false
}

function handleReady() {
  ready.value = true
}

function handleAudioError() {
  if (audioSrc.value !== bgmSrc) {
    error.value = '加载失败'
    return
  }
  shouldResumeAfterFallback = playing.value
  audioSrc.value = fallbackBgmSrc
  ready.value = false
}

async function handleLoadedMetadata() {
  if (!shouldResumeAfterFallback) {
    return
  }
  shouldResumeAfterFallback = false
  await playBgm(false)
}

onMounted(() => {
  window.addEventListener('shine:foreground-audio-play', pauseBgm)
})

onBeforeUnmount(() => {
  window.removeEventListener('shine:foreground-audio-play', pauseBgm)
})
</script>

<template>
  <div class="bgm-control">
    <audio
      ref="audioRef"
      :src="audioSrc"
      loop
      preload="none"
      @canplay="handleReady"
      @loadedmetadata="handleLoadedMetadata"
      @error="handleAudioError"
      @play="handlePlay"
      @pause="handlePause"
    ></audio>
    <button
      class="bgm-button"
      type="button"
      :class="{ playing }"
      :title="playing ? '暂停背景音乐' : '播放背景音乐'"
      @click="toggleBgm"
    >
      <Music2 class="bgm-disc" :size="18" />
      <Pause v-if="playing" :size="15" />
      <Play v-else :size="15" />
    </button>
    <span class="bgm-status">{{ error || (ready ? 'BGM' : 'LOAD') }}</span>
  </div>
</template>
