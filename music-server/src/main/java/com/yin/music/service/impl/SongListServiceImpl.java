package com.yin.music.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yin.music.model.R;
import com.yin.music.config.MediaProperties;
import com.yin.music.mapper.SongListMapper;
import com.yin.music.mapper.SongListStyleMapper;
import com.yin.music.mapper.SongListStyleRelMapper;
import com.yin.music.model.domain.SongList;
import com.yin.music.model.domain.SongListStyle;
import com.yin.music.model.domain.SongListStyleRel;
import com.yin.music.model.request.SongListRequest;
import com.yin.music.model.response.PageResponse;
import com.yin.music.model.response.SongListResponse;
import com.yin.music.model.response.SongListStyleLink;
import com.yin.music.model.response.StyleOptionResponse;
import com.yin.music.model.response.UploadResponse;
import com.yin.music.service.SongListService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SongListServiceImpl extends ServiceImpl<SongListMapper, SongList> implements SongListService {

    private final SongListMapper songListMapper;

    private final SongListStyleMapper songListStyleMapper;

    private final SongListStyleRelMapper songListStyleRelMapper;

    private final MediaProperties mediaProperties;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> updateSongListMsg(SongListRequest updateSongListRequest) {
        if (updateSongListRequest.getId() == null) {
            return R.error("歌单 ID 不能为空");
        }
        if (songListMapper.selectById(updateSongListRequest.getId()) == null) {
            return R.error("歌单不存在");
        }

        List<SongListStyle> styles = loadStylesByIds(updateSongListRequest.getStyleIds());
        if (styles.isEmpty()) {
            return R.error("请至少选择一个风格标签");
        }

        SongList songList = new SongList();
        songList.setId(updateSongListRequest.getId());
        songList.setTitle(updateSongListRequest.getTitle());
        songList.setIntroduction(updateSongListRequest.getIntroduction());
        songList.setStyle(joinStyleNames(styles));
        if (songListMapper.updateById(songList) > 0) {
            replaceSongListStyles(songList.getId(), styles);
            return R.success("修改成功");
        }
        return R.error("修改失败");
    }

    @Override
    public R<?> deleteSongList(Integer id) {
        if (songListMapper.deleteById(id) > 0) {
            return R.success("删除成功");
        }
        return R.error("删除失败");
    }

    @Override
    public R<?> allSongList() {
        return R.success(null, attachStyles(SongListResponse.fromList(songListMapper.selectList(null))));
    }

    @Override
    public R<?> pageSongList(Integer page, Integer size) {
        Page<SongList> pageInfo = new Page<>(page, size);
        songListMapper.selectPage(pageInfo, null);
        return R.success(null, buildPagedData(pageInfo, page, size));
    }

    @Override
    public R<?> songListOfId(Integer id) {
        SongList songList = songListMapper.selectById(id);
        if (songList == null) {
            return R.error("歌单不存在");
        }
        List<SongListResponse> list = attachStyles(Collections.singletonList(SongListResponse.from(songList)));
        return R.success(null, list.isEmpty() ? null : list.get(0));
    }

    @Override
    public R<?> likeTitle(String title) {
        QueryWrapper<SongList> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", title);
        return R.success(null, attachStyles(SongListResponse.fromList(songListMapper.selectList(queryWrapper))));
    }

    @Override
    public R<?> listByStyleId(Integer styleId) {
        if (styleId == null || styleId <= 0) {
            return allSongList();
        }
        return R.success(null, attachStyles(SongListResponse.fromList(songListMapper.selectByStyleId(styleId))));
    }

    @Override
    public R<?> pageByStyleId(Integer styleId, Integer page, Integer size) {
        if (styleId == null || styleId <= 0) {
            return pageSongList(page, size);
        }
        Page<SongList> pageInfo = new Page<>(page, size);
        songListMapper.selectPageByStyleId(pageInfo, styleId);
        return R.success(null, buildPagedData(pageInfo, page, size));
    }

    @Override
    public R<?> allSongListStyle() {
        List<StyleOptionResponse> styleList = new ArrayList<>();
        styleList.add(StyleOptionResponse.of(0, "全部歌单"));

        List<SongListStyle> dbStyles = songListStyleMapper.selectList(
                new QueryWrapper<SongListStyle>().orderByAsc("id"));
        for (SongListStyle item : dbStyles) {
            if (item == null || item.getName() == null || item.getName().trim().isEmpty()) {
                continue;
            }
            styleList.add(StyleOptionResponse.of(item.getId(), item.getName().trim()));
        }
        return R.success(null, styleList);
    }

    @Override
    public R<?> recommendSongList(Integer limit) {
        int safeLimit = (limit == null || limit <= 0) ? 10 : Math.min(limit, 50);
        return R.success(null, attachStyles(
                SongListResponse.fromList(songListMapper.selectRecommendedSongLists(safeLimit))));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> addSongList(SongListRequest addSongListRequest) {
        List<SongListStyle> styles = loadStylesByIds(addSongListRequest.getStyleIds());
        if (styles.isEmpty()) {
            return R.error("请至少选择一个风格标签");
        }

        SongList songList = new SongList();
        songList.setTitle(addSongListRequest.getTitle());
        songList.setIntroduction(addSongListRequest.getIntroduction());
        songList.setStyle(joinStyleNames(styles));
        songList.setPic("/img/songListPic/123.jpg");
        if (songListMapper.insert(songList) > 0) {
            replaceSongListStyles(songList.getId(), styles);
            return R.success("添加成功", Collections.singletonMap("id", songList.getId()));
        }
        return R.error("添加失败");
    }

    @Override
    public R<?> updateSongListImg(MultipartFile avatorFile, int id) {
        // Song list covers are /img/songListPic/** (local media), same as seed data & WebMvcConfig.
        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        Path folder = mediaProperties.imgDir("songListPic");
        try {
            Files.createDirectories(folder);
            avatorFile.transferTo(folder.resolve(fileName));
        } catch (IOException e) {
            return R.fatal("上传失败");
        }
        String imgPath = "/img/songListPic/" + fileName;
        SongList songList = new SongList();
        songList.setId(id);
        songList.setPic(imgPath);
        if (songListMapper.updateById(songList) > 0) {
            return R.success("上传成功", UploadResponse.of(imgPath));
        }
        return R.error("上传失败");
    }

    private PageResponse<SongListResponse> buildPagedData(Page<SongList> pageInfo, Integer page, Integer size) {
        return PageResponse.of(
                attachStyles(SongListResponse.fromList(pageInfo.getRecords())),
                pageInfo.getTotal(),
                page,
                size
        );
    }

    private List<SongListStyle> loadStylesByIds(List<Integer> styleIds) {
        if (styleIds == null || styleIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Integer> ids = styleIds.stream()
                .filter(Objects::nonNull)
                .filter(id -> id > 0)
                .distinct()
                .collect(Collectors.toList());
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<SongListStyle> styles = songListStyleMapper.selectBatchIds(ids);
        if (styles == null || styles.size() != ids.size()) {
            return Collections.emptyList();
        }
        styles.sort(Comparator.comparing(SongListStyle::getId, Comparator.nullsLast(Integer::compareTo)));
        return styles;
    }

    private void replaceSongListStyles(Integer songListId, List<SongListStyle> styles) {
        songListStyleRelMapper.deleteBySongListId(songListId);
        if (styles == null || styles.isEmpty()) {
            return;
        }
        List<SongListStyleRel> rows = new ArrayList<>(styles.size());
        for (SongListStyle style : styles) {
            if (style == null || style.getId() == null) {
                continue;
            }
            SongListStyleRel rel = new SongListStyleRel();
            rel.setSongListId(songListId);
            rel.setStyleId(style.getId());
            rows.add(rel);
        }
        if (!rows.isEmpty()) {
            songListStyleRelMapper.insertBatch(rows);
        }
    }

    private static String joinStyleNames(List<SongListStyle> styles) {
        if (styles == null || styles.isEmpty()) {
            return "";
        }
        return styles.stream()
                .map(SongListStyle::getName)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining("-"));
    }

    private List<SongListResponse> attachStyles(List<SongListResponse> responses) {
        if (responses == null || responses.isEmpty()) {
            return responses == null ? Collections.emptyList() : responses;
        }
        List<Integer> ids = responses.stream()
                .map(SongListResponse::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (ids.isEmpty()) {
            return responses;
        }
        List<SongListStyleLink> rows = songListStyleRelMapper.selectStylesBySongListIds(ids);
        Map<Integer, List<SongListStyleLink>> grouped = rows == null
                ? Collections.emptyMap()
                : rows.stream().collect(Collectors.groupingBy(SongListStyleLink::getSongListId));

        for (SongListResponse item : responses) {
            List<SongListStyleLink> styleRows =
                    grouped.getOrDefault(item.getId(), Collections.emptyList());
            List<StyleOptionResponse> styles = styleRows.stream()
                    .map(link -> StyleOptionResponse.of(link.getId(), link.getName()))
                    .collect(Collectors.toList());
            item.setStyles(styles);
            if (!styles.isEmpty()) {
                item.setStyle(styles.stream()
                        .map(StyleOptionResponse::getName)
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.joining("-")));
            }
        }
        return responses;
    }
}
