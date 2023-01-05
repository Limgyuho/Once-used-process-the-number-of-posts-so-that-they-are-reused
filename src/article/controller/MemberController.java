package article.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import article.container.Container;
import creatboard17.dto.Article;
import creatboard17.dto.Member;
import creatboard17.util.Util;

public class MemberController extends Controller{
	
	private List<Member> members;
	private Scanner sc;
	private String cmd;
	
	


	public MemberController( Scanner sc) {
		this.members = Container.memberDao.members;
		this.sc =sc;	
		}

	@Override
	public void doAction(String actionMethName, String cmd) {
		this.cmd = cmd;
		
		switch(actionMethName) {
		case "join":
			doJoin();
			break;
		case "login":
			doLogin();
			break;
		case "logout":
			doLogout();
			break;
		default:
				System.out.println("존재하지 않는 명령어 입니다");
				break;
		}
		
	}
	
 
	
	private void doLogout() {
		if(isLogined() ==false) {
			System.out.println("로그인 상태가 아닙니다");
			return;
		}
		loginedMember = null;
		System.out.println("로그아웃 되었습니다");
	}

	
	private void doLogin() {
		
		
		System.out.println("아이디 : ");
		String loginId = sc.nextLine();
		System.out.println("비밀번호 : ");
		String loginPw = sc.nextLine();
		
		System.out.printf("입력한 아이디 : %s,입력한 비밀번호 : %s\n",loginId,loginPw);
	
		Member member =getMemberByLoginId(loginId);
		if(member == null) {
			System.out.println("해당 회원은 존재하지 않습니다");
			return;
		}
		if(member.loginPw.equals(loginPw) == false) {
			System.out.println("비밀번호를 확인해주세요");
			return;
		}
		
		loginedMember = member;
		
		System.out.printf("로그인 성공!,%s님 환영합니다\n",loginedMember.name);
	}



	private void doJoin() {
		

		String regDate = Util.getNowDateStr();
		int id = Container.articleDao.getNewId();
		
		String loginId; 
		String loginPw;
		String loginPwChk;
		
		while(true) {
			System.out.println("아이디 : ");
			loginId = sc.nextLine();
			
			if(isJoinableLoginId(loginId) == false) {
				System.out.printf("%s는 이미 사용중인 아이디 입니다\n",loginId);
				continue;
			}
			
			break;
		}
		
		while(true) {
			
			System.out.println("비밀번호 : ");
			loginPw = sc.nextLine();
			System.out.println("비밀번호 확인: ");
			loginPwChk = sc.nextLine();
			
			if(!(loginPw.equals(loginPwChk))) {
				System.out.println("비밀번호를 다시 입력해주세요");
				continue;
			}
			break;
		}
		System.out.println("이름 : ");
		String name = sc.nextLine();
		
		Member member = new Member(id, regDate, loginId, loginPw, name);
		Container.memberDao.add(member);

		System.out.printf("%d번 회원이 생성되었습니다\n", id);

	}
	
	private Member getMemberByLoginId(String loginId) {
		
		int index = getMemberIndexByloginId(loginId);
		
		if(index == -1) {
			return null; 
		}
		return members.get(index);
	}
	private int getMemberIndexByloginId(String loginId) {
		int i = 0;
		
		for (Member member : members) {
			// 향상된 for문으로 사용 하여 처음부터 끝까지 확인 가능하다
			
			if (member.loginId.equals(loginId)) {
				return i;
			}
			i++;
		}
		return -1;
	}

	private boolean isJoinableLoginId(String loginId) {

		int index = getMemberIndexByloginId(loginId);

		if (index != -1) {
			return false;
		}

		return true;
	}
	public void makeTestDate() {

		System.out.println("회원 데이터를 생성합니다");
		Container.memberDao.add(new Member(Container.memberDao.getNewId(), Util.getNowDateStr(), "admin", "admin", "관리자"));
		Container.memberDao.add(new Member(Container.memberDao.getNewId(), Util.getNowDateStr(), "tset1", "test1", "김철수"));
		Container.memberDao.add(new Member(Container.memberDao.getNewId(), Util.getNowDateStr(), "test2", "test2", "김영희"));
	}

}
