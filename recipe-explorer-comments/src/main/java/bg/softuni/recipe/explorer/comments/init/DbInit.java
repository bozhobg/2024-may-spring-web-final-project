package bg.softuni.recipe.explorer.comments.init;

import bg.softuni.recipe.explorer.comments.service.CommentService;
import bg.softuni.recipe.explorer.comments.service.CommentServiceInitImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class DbInit implements CommandLineRunner {

    private final CommentServiceInitImpl commentServiceInit;

    @Autowired
    public DbInit(CommentServiceInitImpl commentServiceInit) {
        this.commentServiceInit = commentServiceInit;
    }

    @Override
    public void run(String... args) throws Exception {
        commentServiceInit.init();
    }
}
