package com.recipemanagement.serviceImpl;

import com.recipemanagement.dto.CommentDto;
import com.recipemanagement.entity.CommentEntity;
import com.recipemanagement.exception.CommentNotFoundException;
import com.recipemanagement.repository.CommentRepository;
import com.recipemanagement.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CommentDto> getComments() {
        List<CommentEntity> comments = commentRepository.findAll();
        return comments.stream().map(commentEntity -> modelMapper.map(commentEntity, CommentDto.class)).collect(Collectors.toList());
    }

    @Override
    public Optional<CommentDto> getComment(String id) throws CommentNotFoundException {
        Optional<CommentEntity> commentEntity = commentRepository.findById(id);
        if (commentEntity.isEmpty()) {
            throw new CommentNotFoundException("Comment not found");
        } else {
            CommentDto commentDto = modelMapper.map(commentEntity.get(), CommentDto.class);
            return Optional.of(commentDto);
        }
    }

    @Override
    public CommentDto addComment(CommentDto commentDto) {
        CommentEntity commentEntity = modelMapper.map(commentDto, CommentEntity.class);
        CommentEntity savedCommentEntity = commentRepository.save(commentEntity);
        return modelMapper.map(savedCommentEntity, CommentDto.class);
    }

    @Override
    public Optional<CommentDto> updateComment(String id, CommentDto updatedCommentDto) throws CommentNotFoundException {
        Optional<CommentEntity> existingCommentEntity = commentRepository.findById(id);
        if (existingCommentEntity.isEmpty()) throw new CommentNotFoundException("Comment not found");

        CommentEntity updatedCommentEntity = modelMapper.map(updatedCommentDto, CommentEntity.class);
        updatedCommentEntity.setCommentId(id);
        commentRepository.save(updatedCommentEntity);

        return Optional.of(modelMapper.map(updatedCommentEntity, CommentDto.class));
    }

    @Override
    public void deleteComment(String id) throws CommentNotFoundException {
        Optional<CommentEntity> existingCommentEntity = commentRepository.findById(id);

        if (existingCommentEntity.isEmpty()) throw new CommentNotFoundException("Comment not found");

        commentRepository.deleteById(id);
    }

    @Override
    public Optional<CommentDto> updateCommentStatus(String id, CommentDto updatedCommentDto) {
        Optional<CommentEntity> existingCommentEntity = commentRepository.findById(id);
        if (existingCommentEntity.isEmpty()) throw new CommentNotFoundException("Comment not found");

        existingCommentEntity.get().setStatus(updatedCommentDto.getStatus());
        commentRepository.save(existingCommentEntity.get());

        return Optional.of(modelMapper.map(existingCommentEntity.get(), CommentDto.class));
    }
}
