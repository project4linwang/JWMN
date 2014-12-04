package jaist.jwns.entity;

/***
 * check whether the two nodes are linked
 * @author lin
 *
 */
public class LinkChecker {
    public  boolean checker(Node tx, Node rx){
    	boolean linked=false;
    	linked= CheckLinked(tx,rx);

    	return linked;
    }
    /**
     * Check whether the two nodes are linked by the physical coordinate
     * @param tx is the start node
     * @param rx is the end node
     * @return  true of false
     */
    private boolean CheckLinked(Node tx, Node rx){
    	boolean linked=false;
    	double distance=Math.sqrt(Math.pow((double)(tx.getX()-rx.getX()), 2)+Math.pow((double)(tx.getY()-rx.getY()), 2));
    	if(distance!=0){
    		if(distance<=(tx.getRange()+rx.getRange())){
        		linked=true;
        	}
    	}
    	
    	return linked;
    }
}
