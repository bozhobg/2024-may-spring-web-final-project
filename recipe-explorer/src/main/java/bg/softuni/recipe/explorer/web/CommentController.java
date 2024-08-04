package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.model.dto.CommentRestDTO;
import bg.softuni.recipe.explorer.model.user.AppUserDetails;
import bg.softuni.recipe.explorer.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

//    TODO: post, delete, approve, edit
//    TODO: verify header "Referer" not null and handle

    @PostMapping("/recipe/{recipeId}")
    public String post(
            @PathVariable Long recipeId,
            @RequestParam String message,
            HttpServletRequest request,
            @AuthenticationPrincipal AppUserDetails appUserDetails
    ) {
        this.commentService.post(message, recipeId, appUserDetails.getId());

        return "redirect:" + request.getHeader("Referer");
    }

    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        this.commentService.delete(id);

//        redirect:/ on host path, redirect: to address
        return "redirect:" + request.getHeader("Referer");
    }

    @PatchMapping("/{id}/approve")
    public String approve(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        this.commentService.approve(id);

        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/{id}")
    public String loadEdit(
            @PathVariable Long id,
            HttpServletRequest request,
            RedirectAttributes rAttr
    ) {

        CommentRestDTO commentRestDTO = this.commentService.get(id);
        rAttr.addFlashAttribute("editComment", commentRestDTO);

        return "redirect:" + request.getHeader("Referer");
    }

    @PutMapping("/{id}")
    public String loadEdit(
            @PathVariable Long id,
            HttpServletRequest request,
            @RequestParam String message,
            @AuthenticationPrincipal AppUserDetails appUserDetails
    ) {

        this.commentService.edit(id, message, appUserDetails.getId());

        return "redirect:" + request.getHeader("Referer");
    }

    @ExceptionHandler(RestClientException.class)
    public String handleRestException(HttpServletRequest request) {

//        TODO: test without csfr token

        String referer = request.getHeader("Referer");
        if (referer == null) return "redirect:/404";
        return "redirect:" + referer;
    }
}
