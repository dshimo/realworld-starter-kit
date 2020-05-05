package core.article;

import java.util.List;

import org.json.JSONObject;

public class Articles {
    private List<Article> articles;

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public int getArticlesCount() {
        return articles.size();
    }

    public JSONObject toJson() {
        return new JSONObject()
            .put("articles", articles)
            .put("articlesCount", getArticlesCount());
    }
}