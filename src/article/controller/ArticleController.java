package article.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import article.container.Container;
import creatboard17.dto.Article;
import creatboard17.dto.Member;
import creatboard17.util.Util;

public class ArticleController extends Controller {

	private List<Article> articles;
	private Scanner sc;
	private int id;
	private int lastArticleId;
	private String cmd;

	public ArticleController(Scanner sc) {
		this.articles = Container.articleDao.articles;
		this.sc = sc;
	}

	
	@Override
	public void doAction(String actionMethName, String cmd) {

		this.cmd = cmd;

		switch (actionMethName) { 
		case "write":
			doWrite();
			break;
		case "list":
			showList();
			break;
		case "detail":
			showdetail();
			break;
		case "modify":
			domodify();
			break;
		case "delete":
			dodelete();
			break;
		default:
			System.out.println("존재하지 않는 명령어 입니다");
			break;
		}
	}

	private void doWrite() {

		int id = Container.articleDao.getNewId();
		String regDate = Util.getNowDateStr();
		System.out.println("제목) ");
		String title = sc.nextLine();
		System.out.println("내용) ");
		String body = sc.nextLine();

		Article article = new Article(id, regDate,loginedMember.id, title, body);
		// 제목과 내용을 쓰기 위해 article write 기능의 조건문에 있어야 한다
		Container.articleDao.add(article);
		// ArrayList의 요소를 추가 하기 위함이디

		System.out.printf("%d번글이 생성되었습니다\n", id);

	}

	private void showList() {
		if (articles.size() == 0) {

			System.out.println("게시글이 없습니다");
			return;
		} else {
			System.out.println("게시물이 있습니다");
		}

		String searchKeyword = cmd.substring("article list".length()).trim();

		List<Article> forPrintArticles = articles;

		if (searchKeyword.length() > 0) {
			// searchkeyword가 존재 항때
			forPrintArticles = new ArrayList<>();
			// forPrintArticles 에 ArrayList라는 객체를 만들어 넣겠다
			for (Article article : articles) {
				// 처음부터 끝까지 돌린다
				if (article.title.contains(searchKeyword)) {
					// contains는 문자열이 들어있는지 유무 확인 하는것인데
					// searchKeyword가 title이라는 변수에 문자열이 있으면 이라는 조건문
					forPrintArticles.add(article);
					// forPrintfArticles라는 변수에 article을 추가한다
				}
			}

			if (forPrintArticles.size() == 0) {
				System.out.println("검색결과가 없습니다");
			}
		}

		System.out.println("번호	|	제목	|	작성자	|	조회");
		for (int i = forPrintArticles.size() - 1; i >= 0; i--) {
			// 게시물이 저장되고 보여줄때 1번 부터 보여주게 하기 위함
			Article article = forPrintArticles.get(i);

			String writeName = null;
			
			List<Member> members = Container.memberDao.members;
			for(Member member : members) {
				if(article.memberId == member.id) {
					writeName = member.name;
					break;
				}
			}
		
			
			System.out.printf("%d	|	%s	|	%s	|	%d\n", article.id, article.title,writeName, article.hit);
		}

	}

	private void showdetail() {
		// 앞에있는 cmd가 뒤에있는 문자열로 시작할경우

		String[] cmdBit = cmd.split(" ");
		int id = Integer.parseInt(cmdBit[2]);

		Article foundArticle = getArticleById(id);

		if (foundArticle == null) {
			// 게시물이 없는 경우
			System.out.printf("%d번 게시물이 존재하지 않습니다\n", id);
			return;
		}

		foundArticle.increaseHit();
		// 증가 하는 것이 게시물이 만들어 질때 증가 해야 하므로 여기에 있어야 한다

//		System.out.printf("%d번 게시물은 존재 합니다\n",id);
		System.out.printf("번호 : %d\n", foundArticle.id);
		System.out.printf("날짜 : %s\n", foundArticle.regDate);
		System.out.printf("작성자 : %s\n", foundArticle.memberId);
		System.out.printf("제목 : %s\n", foundArticle.title);
		System.out.printf("내용 : %s\n", foundArticle.body);
		System.out.printf("조회 : %d\n", foundArticle.hit);

	}

	private void domodify() {
		// 앞에있는 cmd가 뒤에있는 문자열로 시작할경우

		String[] cmdBit = cmd.split(" ");
		// split은 안에있는 인자를 기준으로 나눈다
		int id = Integer.parseInt(cmdBit[2]);

		Article foundArticle = getArticleById(id);

		if (foundArticle == null) {
			// 게시물이 없는 경우
			System.out.printf("%d번 게시물이 존재하지 않습니다\n", id);
			return;
		}
		
		if(foundArticle.memberId != loginedMember.id) {
			System.out.println("권한이 없습니다");
			return;
		}
		
		
		System.out.println("수정할 제목) ");
		String title = sc.nextLine();
		System.out.println("수정할 내용) ");
		String body = sc.nextLine();

		foundArticle.title = title;
		foundArticle.title = body;

		System.out.printf("게시물이 수정되었습니다\n", id);
	}

	private void dodelete() {
		// 앞에있는 cmd가 뒤에있는 문자열로 시작할경우

		String[] cmdBit = cmd.split(" ");
		int id = Integer.parseInt(cmdBit[2]);

		Article foundArticle = getArticleById(id);

		if (foundArticle == null) {
			// 게시물이 없는 경우
			System.out.printf("%d번 게시물이 존재하지 않습니다\n", id);
			return;
		}

		if(foundArticle.memberId != loginedMember.id) {
			System.out.println("권한이 없습니다");
			return;
		}
		articles.remove(foundArticle);

		System.out.printf("%d번 게시물이 삭제 되었습니다\n", id);

	}
	

	private int getArticleIndexById(int id) {
		int i = 0;

		for (Article article : articles) {
			// 향상된 for문으로 사용 하여 처음부터 끝까지 확인 가능하다

			if (article.id == id) {
				return i;
			}
			i++;
		}
		return -1;

	}

	private Article getArticleById(int id) {

		int index = getArticleIndexById(id);

		if (index != -1) {
			return articles.get(index);
		}

		return null;

	}

	public void makeTestDate() {

		System.out.println("테스트 데이터를 생성합니다");
		Container.articleDao.add(new Article(Container.articleDao.getNewId(), Util.getNowDateStr(),1, "테스트1", "테스트1", 10));
		Container.articleDao.add(new Article(Container.articleDao.getNewId(), Util.getNowDateStr(),2, "테스트2", "테스트2", 20));
		Container.articleDao.add(new Article(Container.articleDao.getNewId(), Util.getNowDateStr(),2, "테스트3", "테스트3", 30));
	}

}
