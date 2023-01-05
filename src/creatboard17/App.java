package creatboard17;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import article.controller.ArticleController;
import article.controller.Controller;
import article.controller.MemberController;
import creatboard17.dto.Article;
import creatboard17.dto.Member;

public class App {
//	private List<Article> articles;
//	private List<Member> members;
//
//	App() {
//		articles = new ArrayList<>();
//		members = new ArrayList<>();
//	}

	public void run() {
		System.out.println("==프로그램 시작==");

		// 스캐너 타입의 sc라는 변수를 만들고 스캐너객체를 만들어서 연결한다
		Scanner sc = new Scanner(System.in);

		MemberController memberController = new MemberController( sc);
		ArticleController articleController = new ArticleController( sc);
		articleController.makeTestDate();
		memberController.makeTestDate();

		int lastArticleId = 3;
		// while문 밖에 있는 이유는 매번 0이 되면 안되기때문이다

		while (true) {
			// 무한루프
			System.out.println("명령어) ");
			String cmd = sc.nextLine();
			System.out.println("입력된명령어 > " + cmd);

			// 이 안에 있는 명령어가 계속 실행된다

			if (cmd.equals("exit")) {
				// 조건문 cmd라는변수의 값이 exit라는 문자와 같다면
				break;
				// 탈출해라 break를 하지 않으면 무한반복문으로 실행된다
			}

			String[] cmdBit = cmd.split(" ");

			if(cmdBit.length ==1) {
				System.out.println("존재하지 않는 명령어입니다");
				continue;
			}
			
			String controllerName = cmdBit[0];
			String actionMethName = cmdBit[1];

			Controller controller = null;

			if (controllerName.equals("article")) {
				controller = articleController;
			} else if (controllerName.equals("member")) {
				controller = memberController;
			} else {
				System.out.println("존재하지 않는 명령어 입니다");
				continue;
			}
			
			switch(actionMethName) {
			case "write":
			case "modify":
			case "delete":
			case "logout":
				if(Controller.isLogined() == false) {
					System.out.println("로그인 후 이용해주세요");
					continue;
				}
				break;
			}
			switch(actionMethName) {
			case "login":
			case "join":
				if(Controller.isLogined()) {
					System.out.println("로그아웃 후 이용해주세요");
					continue;
				}
				break;
			}
			controller.doAction(actionMethName, cmd);

		}

		sc.close(); // 스캐너기능을 하용하면 반드시 이 실행문을 추가햐야한다

		System.out.println("==프로그램 종료==");

	}

	

}
