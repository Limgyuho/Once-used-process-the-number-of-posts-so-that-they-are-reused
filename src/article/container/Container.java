package article.container;

import article.dao.ArticleDao;
import article.dao.MemberDao;
import creatboard17.dto.Article;

public class Container {

	 public static ArticleDao articleDao;
	 public static MemberDao memberDao;
	 
	 static {
		 articleDao = new ArticleDao();
		 memberDao = new MemberDao();
	 }


}
