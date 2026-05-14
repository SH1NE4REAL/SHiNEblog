<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { Music2 } from 'lucide-vue-next'
import { fetchMusicTracks } from '../api/articles'
import type { MusicTrack } from '../types'

const tracks = ref<MusicTrack[]>([])
const loading = ref(true)
const error = ref('')

function pauseBgmForTrack() {
  window.dispatchEvent(new CustomEvent('shine:foreground-audio-play'))
}

function optimizedAudioUrl(url: string) {
  return url.replace(/\.mp3($|\?)/i, '.web.mp3$1')
}

onMounted(async () => {
  try {
    const page = await fetchMusicTracks()
    tracks.value = page.records
  } catch {
    error.value = '暂时无法加载音乐'
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div class="view-page">
    <section class="page-head">
      <p class="eyebrow">Music</p>
      <h1>音乐</h1>
      <p>我把自己做的曲子放在这里，像一张张声音卡片，记录当时的情绪、灵感和一点点没说出口的话。</p>
    </section>

    <section class="content-band">
      <div v-if="loading" class="state">加载中...</div>
      <div v-else-if="error" class="state error">{{ error }}</div>
      <div v-else-if="tracks.length === 0" class="state">还没有公开音乐</div>
      <div v-else class="music-list">
        <article v-for="track in tracks" :key="track.id" class="music-card">
          <div class="music-cover">
            <img v-if="track.coverUrl" :src="track.coverUrl" :alt="track.title" />
            <Music2 v-else :size="34" />
          </div>
          <div class="music-info">
            <p class="date">{{ track.artist || 'SHiNE' }}</p>
            <h2>{{ track.title }}</h2>
            <p>{{ track.description || '这首作品暂时没有说明。' }}</p>
            <div v-if="track.tags" class="tag-row">
              <span v-for="tag in track.tags.split(',')" :key="tag">{{ tag.trim() }}</span>
            </div>
            <audio controls preload="none" @play="pauseBgmForTrack">
              <source :src="optimizedAudioUrl(track.audioUrl)" type="audio/mpeg" />
              <source :src="track.audioUrl" type="audio/mpeg" />
            </audio>
          </div>
        </article>
      </div>
    </section>
  </div>
</template>
