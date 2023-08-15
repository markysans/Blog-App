package com.springboot.blog.service.ipml;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    private final ModelMapper modelMapper;

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        // Retrieve the post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId)
        );
        Comment comment = mapToEntity(commentDto);
        comment.setPost(post);
        Comment newComment = commentRepository.save(comment);
        CommentDto commentResponse = mapToDTO(newComment);
        return commentResponse;
    }

    @Override
    public List<CommentDto> getCommentsByPostID(long postId) {
        // Retrieve the comments by post id
        List<Comment> commentList = commentRepository.findByPostId(postId);

        return commentList.stream().map(this::mapToDTO).collect(Collectors.toList());

    }

    @Override
    public CommentDto getCommentsById(long postId, long commentId) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Comment", "id", commentId);
        });

        return mapToDTO(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long commentId, CommentDto commentDto) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId).orElseThrow(() -> {
            throw new BlogAPIException(HttpStatus.NOT_FOUND, "Comment not Found");
        });

        if(commentDto.getName() != null)
            comment.setName(commentDto.getName());

        if(commentDto.getEmail() != null)
            comment.setEmail(commentDto.getEmail());

        if(commentDto.getBody() != null)
            comment.setBody(commentDto.getBody());

        Comment updatedComment =  commentRepository.save(comment);

        return mapToDTO(updatedComment);

    }

    @Override
    public void deleteComment(long postId, long commentId) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId).orElseThrow(() -> {
            throw new BlogAPIException(HttpStatus.NOT_FOUND, "Comment not Found");
        });
        commentRepository.delete(comment);
    }

    private CommentDto mapToDTO(Comment comment) {
        CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
//        CommentDto commentDto = new CommentDto();
//        commentDto.setId(comment.getId());
//        commentDto.setName(comment.getName());
//        commentDto.setBody(comment.getBody());
//        commentDto.setEmail(comment.getEmail());
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto) {
        Comment comment = modelMapper.map(commentDto, Comment.class);
//        Comment comment = new Comment();
//        comment.setId(commentDto.getId());
//        comment.setName(commentDto.getName());
//        comment.setBody(commentDto.getBody());
//        comment.setEmail(commentDto.getEmail());
        return comment;
    }

}
