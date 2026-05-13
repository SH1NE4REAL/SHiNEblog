package com.shineblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shineblog.entity.MusicTrack;
import com.shineblog.mapper.MusicTrackMapper;
import com.shineblog.service.MusicTrackService;
import org.springframework.stereotype.Service;

@Service
public class MusicTrackServiceImpl extends ServiceImpl<MusicTrackMapper, MusicTrack> implements MusicTrackService {
}

