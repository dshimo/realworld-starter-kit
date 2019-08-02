package core.article;

import java.util.List;

public class ArticleDataList {
	
    private final List<Article> articleDatas;

    private final int count;

    public ArticleDataList(List<Article> articleDatas, int count) {

        this.articleDatas = articleDatas;
        this.count = count;
        
    }
}
