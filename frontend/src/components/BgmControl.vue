<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref } from 'vue'
import { Music2, Pause, Play } from 'lucide-vue-next'

const audioRef = ref<HTMLAudioElement | null>(null)
const playing = ref(false)
const ready = ref(false)
const error = ref('')
let triedResumeOnGesture = false

async function playBgm(showError = true) {
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
  playBgm(false)
}

async function resumeAfterGesture() {
  if (triedResumeOnGesture || playing.value) {
    return
  }
  triedResumeOnGesture = true
  await playBgm(false)
}

onMounted(() => {
  window.addEventListener('shine:foreground-audio-play', pauseBgm)
  window.addEventListener('pointerdown', resumeAfterGesture, { once: true })
  window.addEventListener('keydown', resumeAfterGesture, { once: true })
})

onBeforeUnmount(() => {
  window.removeEventListener('shine:foreground-audio-play', pauseBgm)
  window.removeEventListener('pointerdown', resumeAfterGesture)
  window.removeEventListener('keydown', resumeAfterGesture)
})
</script>

<template>
  <div class="bgm-control">
    <audio
      ref="audioRef"
      src="/uploads/bgm/bgm.mp3"
      loop
      preload="metadata"
      @canplay="handleReady"
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
