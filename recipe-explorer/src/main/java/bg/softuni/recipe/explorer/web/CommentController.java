package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.model.dto.CommentPutDTO;
import bg.softuni.recipe.explorer.model.dto.CommentRestDTO;
import bg.softuni.recipe.explorer.model.user.AppUserDetails;
import bg.softuni.recipe.explorer.service.CommentService;
import bg.softuni.recipe.explorer.utils.RedirectUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
            @Valid CommentPutDTO bindingModel,
            BindingResult bindingResult,
            RedirectAttributes rAttrs,
            HttpServletRequest request,
            @AuthenticationPrincipal AppUserDetails appUserDetails
    ) {

        if (bindingResult.hasErrors()) {
            RedirectUtil.setRedirectAttrs(rAttrs, bindingModel, bindingResult, "commentPutData");

            return "redirect:" + request.getHeader("Referer");
        }

        this.commentService.post(bindingModel.getMessage(), recipeId, appUserDetails.getId());

        return "redirect:" + request.getHeader("Referer");
    }

    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable Long id,
            HttpServletRequest request,
            @AuthenticationPrincipal AppUserDetails appUserDetails
    ) {
        this.commentService.delete(id, appUserDetails);

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
            RedirectAttributes rAttr,
            @AuthenticationPrincipal AppUserDetails appUserDetails
    ) {

        CommentPutDTO commentPutDTO = this.commentService.get(id, appUserDetails);
        rAttr.addFlashAttribute("commentPutData", commentPutDTO);

        return "redirect:" + request.getHeader("Referer");
    }

    @PutMapping("/{id}")
    public String putEdit(
            @PathVariable Long id,
            @Valid CommentPutDTO bindingModel,
            BindingResult bindingResult,
            RedirectAttributes rAttrs,
            HttpServletRequest request,
            @AuthenticationPrincipal AppUserDetails appUserDetails
    ) {
        if (bindingResult.hasErrors()) {
            RedirectUtil.setRedirectAttrs(rAttrs, bindingModel, bindingResult, "commentPutData");

            return "redirect:" + request.getHeader("Referer");
        }

        this.commentService.edit(bindingModel, appUserDetails);

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
