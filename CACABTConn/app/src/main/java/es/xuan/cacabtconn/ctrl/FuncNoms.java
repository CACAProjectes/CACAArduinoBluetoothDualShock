package es.xuan.cacabtconn.ctrl;

public enum FuncNoms {
	LINE("linea"), RECT("rectangulo"), DIAM("rombo");
	
    private String action;
    
    public String getAction(){
        return this.action;
    }
    private FuncNoms(String action){
        this.action = action;
    }
}
