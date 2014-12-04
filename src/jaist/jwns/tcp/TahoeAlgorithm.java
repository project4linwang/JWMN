package jaist.jwns.tcp;

import jaist.jwns.util.Configuration;

public class TahoeAlgorithm {

	private int wnd;
	private boolean apply_alg;
	Configuration config=Configuration.getConfig();
	public TahoeAlgorithm(boolean apply){
		this.apply_alg=apply;
		if(apply){
			this.wnd=config.getWNDInitial(); 
		}
		else{
			this.wnd=config.getWNDMax(); 
		}
		
	}

	public int getWnd() {
		return wnd;
	}
	public int process(){
		if(apply_alg){
			return processWithAlgorithm();
		}
		else{
			return processWithoutAlgorithm();
		}
	}
	/**
	 * Tahoe algorithm
	 * @return
	 */
	private int processWithAlgorithm(){
		int t_wnd=0;
		if(wnd<=config.getWNDThreshold() ){
			 t_wnd=2*wnd;
			 		
		}
		else{
			t_wnd=wnd+5;
		}
		if(t_wnd<=config.getWNDMax()){
			wnd=t_wnd;
		}
		return wnd;
	}
	/**
	 * Fixed algorithm
	 * @return
	 */
	private int processWithoutAlgorithm(){
		return config.getWNDMax(); 
	}
}
