package article.dao;

public class Dao {
	public int lastId;
	
	Dao(){
		lastId =0;
	}
	public int getNewId() {
		return lastId+1;
	}
	
}
