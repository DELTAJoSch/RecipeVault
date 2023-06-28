package at.ac.tuwien.sepm.groupphase.backend.service.impl;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CommentDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Comment;
import at.ac.tuwien.sepm.groupphase.backend.entity.CommentPrimaryKey;
import at.ac.tuwien.sepm.groupphase.backend.exception.InternalServerException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CommentRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CommentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DefaultCommentService implements CommentService {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RecipeRepository recipeRepository;

    public List<Comment> getComments(Long recipeId)
        throws NotFoundException {

        var recipeEntity = this.recipeRepository.findById(recipeId);
        if (recipeEntity.isEmpty()) {
            throw new NotFoundException("Rezept nicht gefunden");
        }

        return this.commentRepository.findByRecipe(recipeEntity.get());
    }

    @Transactional
    public Comment createComment(CommentDto commentDto, String issuerEmail)throws ValidationException {

        ApplicationUser user = this.userRepository.findByEmail(issuerEmail);
        if (user == null) {
            throw new NotFoundException("Benutzer mit E-Mail " + issuerEmail + " konnte nicht gefunden werden");
        }

        commentDto.setCreatorId(user.getId());

        var recipeOpt = this.recipeRepository.findById(commentDto.getRecipeId());
        if (recipeOpt.isEmpty()) {
            throw new NotFoundException("Rezept existiert nicht");
        }
        var recipe = recipeOpt.get();

        LocalDateTime date = commentDto.getDateTime();

        if (commentDto.getContent() == null || commentDto.getContent().isBlank() || commentDto.getContent().length() > 2047) {
            throw new ValidationException("Ungültige Länge des Kommentarinhalts");
        }

        Comment entity;

        entity = new Comment(user, recipe, date, commentDto.getContent());
        commentRepository.saveAndFlush(entity);
        return entity;

    }

    @Transactional
    public void deleteComment(CommentDto commentDto, String issuerEmail)
        throws NotFoundException, UserPermissionException {

        var issuer = this.userRepository.findByEmail(issuerEmail);
        if (issuer == null) {
            throw new InternalServerException("Der ausstellende Benutzer konnte in der Datenbank nicht gefunden werden.");
        }
        if (!issuer.getAdmin()) {
            throw new UserPermissionException("Benutzer darf den Kommentar nicht löschen");
        }
        var userOpt = this.userRepository.findById(commentDto.getCreatorId());
        if (userOpt.isEmpty()) {
            throw new NotFoundException("Benutzer nicht gefunden");
        }
        var user = userOpt.get();

        var recipeOpt = this.recipeRepository.findById(commentDto.getRecipeId());
        if (recipeOpt.isEmpty()) {
            throw new NotFoundException("Rezept nicht gefunden");
        }
        var recipe = recipeOpt.get();

        LocalDateTime date = commentDto.getDateTime();

        if (!issuer.getAdmin() && !issuer.getId().equals(commentDto.getCreatorId())) {
            throw new UserPermissionException("Sie können den Kommentar nicht löschen");
        }
        CommentPrimaryKey commentPrimaryKey = new CommentPrimaryKey(user, recipe, date);

        if (!this.commentRepository.existsById(commentPrimaryKey)) {
            throw new NotFoundException("Kommentar nicht gefunden");
        }
        this.commentRepository.deleteById(commentPrimaryKey);

    }
}
