type CacheRecord<T> = {
  savedAt: number
  value: T
}

const PUBLIC_CACHE_PREFIX = 'shine_public_cache:'
const MAX_STALE_MS = 24 * 60 * 60 * 1000

function cacheKey(key: string) {
  return `${PUBLIC_CACHE_PREFIX}${key}`
}

export function readPublicCache<T>(key: string) {
  try {
    const raw = window.localStorage.getItem(cacheKey(key))
    if (!raw) {
      return null
    }
    const record = JSON.parse(raw) as CacheRecord<T>
    if (!record.savedAt || Date.now() - record.savedAt > MAX_STALE_MS) {
      window.localStorage.removeItem(cacheKey(key))
      return null
    }
    return record.value
  } catch {
    return null
  }
}

export function writePublicCache<T>(key: string, value: T) {
  try {
    const record: CacheRecord<T> = {
      savedAt: Date.now(),
      value
    }
    window.localStorage.setItem(cacheKey(key), JSON.stringify(record))
  } catch {
    // Browser storage can be disabled or full. Fresh network data still works.
  }
}

export async function cachedPublicRequest<T>(key: string, loader: () => Promise<T>) {
  const cached = readPublicCache<T>(key)
  if (cached) {
    loader()
      .then((fresh) => writePublicCache(key, fresh))
      .catch(() => undefined)
    return cached
  }

  const fresh = await loader()
  writePublicCache(key, fresh)
  return fresh
}

export function prefetchPublicRequest<T>(key: string, loader: () => Promise<T>) {
  if (readPublicCache<T>(key)) {
    return
  }
  loader()
    .then((fresh) => writePublicCache(key, fresh))
    .catch(() => undefined)
}
