package BLL;
import javax.swing.JOptionPane;

public enum CategoriasProd {

	remeras,
	shorts,
	polleras,
	buzos,
	pantalones;
	
	public static CategoriasProd ElegirCategoria() {	
		CategoriasProd opcion = (CategoriasProd)JOptionPane.showInputDialog(null,"Categorias", null,  0, null, CategoriasProd.values(),CategoriasProd.values()[0] );
			return opcion;
		}

	public static CategoriasProd ElegirCategoria(CategoriasProd actual) {
		CategoriasProd opcion = (CategoriasProd)JOptionPane.showInputDialog(null,"Categorias", null,  0, null, CategoriasProd.values(), actual);
		return opcion;
	}
	
}
