package corp.fredkiss.smart_commerce;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import corp.fredkiss.smart_commerce.model.Article;
import corp.fredkiss.smart_commerce.model.NotAvailableException;
import corp.fredkiss.smart_commerce.model.NotEnoughStockException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    List<Article> articleList = new ArrayList<>();

    @Test
    public void test2() {

        articleList.add(new Article());
        articleList.add(new Article());
        articleList.add(new Article());

        // récupérer l'article
        Article article = articleList.get(0);

        // modifier l'article
        article.setStock(15);
        article.setPrixU(2);
        int sold = 0;
        try {
            sold = article.sell(5, 0);
        } catch (NotAvailableException e) {
            e.printStackTrace();
        } catch (NotEnoughStockException e) {
            e.printStackTrace();
        }

        // vérifier l'article
        assertEquals(2, articleList.get(0).getCost());
        assertEquals(10, articleList.get(0).getStock());
        assertEquals(10, sold);


    }
}
