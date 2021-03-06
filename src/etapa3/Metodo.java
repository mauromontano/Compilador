package etapa3;


import java.util.HashMap;
import java.util.Map;
import Excepciones.ErrorSemantico;
import etapa1.Token;
import etapa4.Bloque;




public class Metodo {
	private String nombre;
	private String forma;
	private Tipo tipoRetorno;
	private Map<String, Parametro> params;
	private int linea;
	private Bloque bloque;
	
	
	public Metodo(Token t, String forma, Tipo tipoRetorno) {
		
		nombre = t.getLexema();
		linea = t.getNroLinea();
		params = new HashMap<String, Parametro>();
		this.forma = forma;
		this.tipoRetorno = tipoRetorno;
		this.bloque = new Bloque(t);
		
	}
	

	public String getNombre() {
		return nombre;
	}
	
	public int getLinea(){
		return linea;
	}
	
	public String getForma(){
		return forma;
	}

	public Tipo getTipoRetorno(){
		return tipoRetorno;
	}
	
	public Map<String, Parametro> getParametros() {
		return params;
	}
	
	public boolean isDynamic() {
		return forma.equals("dynamic");
	}

	public boolean isStatic() {
		return forma.equals("static");
	}

	public void setBloque(Bloque bloque){
		this.bloque = bloque;
	}
	
	public Bloque getBloque(){
		
		return bloque;
	}
	
	public Parametro getParametro(int indice){
		
		Parametro toReturn = null;
		
				for(Parametro p : params.values()){
					
					if(p.getIndice()==indice)
						toReturn = p;
				}
			
		
		return toReturn;
	}

	public void insertarParametros(Parametro p) throws ErrorSemantico{
		
		if(!params.containsKey(p.getNombre()))
			params.put(p.getNombre(), p);
		else
			throw new ErrorSemantico(p.getLinea()+" : ya existe un parametro con el mismo nombre \""+p.getNombre()+"\""
					+"\n\n[Error:"+p.getNombre()+"|"+
					p.getLinea()+"]");
	}

	
	
	public void controlDeclaracion() throws ErrorSemantico{
		

		// Si retorna un tipo clase, entonces dicha clase debe estar declarada.
		
		if (tipoRetorno instanceof TipoClase) {
			if (TablaDeSimbolos.getTablaDeSimbolos().getClases().get(tipoRetorno.getNombre())==null)
				throw new ErrorSemantico(tipoRetorno.getLinea()+" : la clase \""+tipoRetorno.getNombre()+"\" no fue declarada"
						+"\n\n[Error:"+tipoRetorno.getNombre()+"|"+
						tipoRetorno.getLinea()+"]");
		}
		
		// Verifica que para los argumentos de tipo clase, la misma este
				// declarada.
		for (Parametro param : params.values()) {
					
					if (param.getTipo() instanceof TipoClase) {
						
						if (TablaDeSimbolos.getTablaDeSimbolos().getClases().get(param.getTipo().getNombre())==null)
							
							throw new ErrorSemantico(param.getTipo().getLinea()+" : la clase \""+param.getTipo().getNombre()+"\" no fue declarada"
									+"\n\n[Error:"+param.getTipo().getNombre()+"|"+
									param.getTipo().getLinea()+"]");
					}
				}
		
		
	}
	
	
	
	public void puedeSobreEscribirse(Metodo m) throws ErrorSemantico{
		
		 
		
		if(!forma.equals(m.getForma()))
				throw new ErrorSemantico(m.getLinea()+" : el metodo \""+this.nombre+"\" no se puede redefinir porque su forma es distinta"
					+"\n\n[Error:"+this.nombre+"|"+
					m.getLinea()+"]");
		
		if(!tipoRetorno.getNombre().equals(m.getTipoRetorno().getNombre()))
			throw new ErrorSemantico(m.getLinea()+" : el metodo \""+this.nombre+"\" no se puede redefinir porque su tipo de retorno es distinto"
					+"\n\n[Error:"+this.nombre+"|"+
					m.getLinea()+"]");
		
		if(params.size()!=m.getParametros().size())
			
			throw new ErrorSemantico(m.getLinea()+" : el metodo \""+this.nombre+"\" no se puede redefinir porque su cantidad de parametros es distinta"
					+"\n\n[Error:"+this.nombre+"|"+
					m.getLinea()+"]");	
		
		
		for(int i=0;i<params.size();i++){
			
			if(!this.getParametro(i).getTipo().getNombre().equals(m.getParametro(i).getTipo().getNombre()))
				
				throw new ErrorSemantico(m.getLinea()+" :El metodo \""+nombre+"\" no se puede redefinir, el parametro numero "+(i+1)+" no matchea. Se esperaba un Parametro de tipo \""
						+this.getParametro(i).getTipo().getNombre()+"\" y encontro un parametro de tipo \""+m.getParametro(i).getTipo().getNombre()+"\""
						+"\n\n[Error:"+this.nombre+"|"+
						m.getLinea()+"]");
		}
		
	}
	
	
}
