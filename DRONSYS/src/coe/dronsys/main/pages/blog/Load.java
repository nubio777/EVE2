package coe.dronsys.main.pages.blog;

import coe.dronsys.main.pages.blog.model.Author;
import coe.dronsys.main.pages.blog.model.Comment;
import coe.dronsys.main.pages.blog.model.Entry;
import coe.wuti.jpa.DAO;
import coe.wuti.jpa.Insert;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jesus on 3/15/16.
 */
public class Load {
    public static void main(String[] args) throws Exception {
        Author author1 = new Author("Ennis del Mar", "At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga");
        Author author2 = new Author("Eddard Stark", "Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus");
        Author author3 = new Author("Cersei L.", "Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae");

        Entry entry1 = new Entry("The mountain trip", "Once in a lifetime", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                " Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
                "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum", author3);

        DAO dao = DAO.get("DRONSYS-DB");
        for (Author a : Arrays.asList(author1, author2, author3))
            dao.execute(new Insert(a));

        for (Entry e : Arrays.asList(entry1))
            dao.execute(new Insert(e));

        List<Comment> cs = dao.getList(Comment.class) ;
        System.out.println("cs = " + cs);
    }
}
