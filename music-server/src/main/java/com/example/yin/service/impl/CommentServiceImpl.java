package com.example.yin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yin.common.R;
import com.example.yin.mapper.CommentMapper;
import com.example.yin.mapper.AppUserMapper;
import com.example.yin.model.domain.AppUser;
import com.example.yin.model.domain.Comment;
import com.example.yin.model.request.CommentRequest;
import com.example.yin.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    private final CommentMapper commentMapper;

    private final AppUserMapper appUserMapper;

    @Override
    public R addComment(CommentRequest addCommentRequest) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(addCommentRequest, comment);
        comment.setType(addCommentRequest.getNowType());
        if (commentMapper.insert(comment) > 0) {
            return R.success("评论成功");
        } else {
            return R.error("评论失败");
        }
    }

    @Override
    public R updateCommentMsg(CommentRequest addCommentRequest) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(addCommentRequest, comment);
        if (commentMapper.updateById(comment) > 0) {
            return R.success("点赞成功");
        } else {
            return R.error("点赞失败");
        }
    }

    //    删除评论
    @Override
    public R deleteComment(Integer id) {
        if (commentMapper.deleteById(id) > 0) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @Override
    public R commentOfSongId(Integer songId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("song_id",songId);
        return R.success(null, buildCommentWithUser(commentMapper.selectList(queryWrapper)));
    }

    @Override
    public R commentOfSongListId(Integer songListId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("song_sheet_id", songListId);
        return R.success(null, buildCommentWithUser(commentMapper.selectList(queryWrapper)));
    }

    private List<Map<String, Object>> buildCommentWithUser(List<Comment> comments) {
        if (comments == null || comments.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Integer> userIds = comments.stream()
                .map(Comment::getUserId)
                .filter(id -> id != null && id > 0)
                .collect(Collectors.toCollection(HashSet::new));
        Map<Integer, AppUser> userMap = new LinkedHashMap<>();
        if (!userIds.isEmpty()) {
            QueryWrapper<AppUser> wrapper = new QueryWrapper<>();
            wrapper.in("id", userIds);
            userMap = appUserMapper.selectList(wrapper).stream()
                    .collect(Collectors.toMap(AppUser::getId, Function.identity(), (a, b) -> a, LinkedHashMap::new));
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Comment comment : comments) {
            AppUser appUser = userMap.get(comment.getUserId());
            Map<String, Object> commentMap = new LinkedHashMap<>();
            commentMap.put("id", comment.getId());
            commentMap.put("userId", comment.getUserId());
            commentMap.put("songId", comment.getSongId());
            commentMap.put("songListId", comment.getSongListId());
            commentMap.put("content", comment.getContent());
            commentMap.put("createTime", comment.getCreateTime());
            commentMap.put("type", comment.getType());
            commentMap.put("likeCount", comment.getLikeCount());
            commentMap.put("username", appUser != null ? appUser.getUsername() : "");
            commentMap.put("avatar", appUser != null ? appUser.getAvatar() : "");
            result.add(commentMap);
        }
        return result;
    }
}
