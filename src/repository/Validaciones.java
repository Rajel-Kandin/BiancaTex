package repository;

import javax.swing.JOptionPane;

public class Validaciones {

    // Entero mayor a 0
    public static Integer validarEntero(String mensaje) {
        String entrada;
        int numero = 0;
        boolean valido = false;
        do {
            entrada = JOptionPane.showInputDialog(mensaje);
            if (entrada == null) return null; 
            try {
                numero = Integer.parseInt(entrada.trim());
                if (numero > 0) {
                    valido = true;
                } else {
                    JOptionPane.showMessageDialog(null, "El número tiene que ser mayor a 0.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingresá solo números enteros.");
            }
        } while (!valido);
        return numero; }
    
    

    // Número con decimales mayor a 0
    public static Double validarDouble(String mensaje) {
        return validarDouble(mensaje, "");}

    // igual, pero con un valor inicial cargado 
    public static Double validarDouble(String mensaje, String valorActual) {
        String entrada;
        double numero = 0;
        boolean valido = false;
        do {
            entrada = JOptionPane.showInputDialog(mensaje, valorActual);
            if (entrada == null) return null; 
            try {
                numero = Double.parseDouble(entrada.trim().replace(",", "."));
                if (numero > 0) {
                    valido = true;
                } else {
                    JOptionPane.showMessageDialog(null, "El número tiene que ser mayor a 0.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingresá un número válido.");  }
        } while (!valido);
        return numero; }

    
    // Solo letras
    public static String validarLetras(String mensaje) {
        return validarLetras(mensaje, "");  }

    // igual, pero con un valor inicial cargado 
    public static String validarLetras(String mensaje, String valorActual) {
        String entrada;
        boolean valido = false;
        do {
            entrada = JOptionPane.showInputDialog(mensaje, valorActual);
            if (entrada == null) return null; 

            entrada = entrada.trim();
            valido = !entrada.isEmpty();
            for (int i = 0; i < entrada.length(); i++) {
                char c = entrada.charAt(i);
                if (!Character.isLetter(c) && c != ' ') {
                    valido = false;
                }}
            if (!valido) {
                JOptionPane.showMessageDialog(null, "Ingresá solo letras.");}
        } while (!valido);
        return entrada;  }

    
    // Solo números (Cuit, teléfono)
    public static String validarNumeros(String mensaje) {
        return validarNumeros(mensaje, "");}

    // igual, pero con un valor inicial cargado 
    public static String validarNumeros(String mensaje, String valorActual) {
        String entrada;
        boolean valido = false;
        do {
            entrada = JOptionPane.showInputDialog(mensaje, valorActual);
            if (entrada == null) return null; 

            entrada = entrada.trim();
            valido = !entrada.isEmpty();
            for (int i = 0; i < entrada.length(); i++) {
                char c = entrada.charAt(i);
                if (!Character.isDigit(c)) {
                    valido = false;  }}
            if (!valido) {
                JOptionPane.showMessageDialog(null, "Ingresá solo números.");}
        } while (!valido);
        return entrada; }
    
    
    public static String validarEmail(String mensaje) {
        return validarEmail(mensaje, ""); }

    public static String validarEmail(String mensaje, String valorInicial) {
        while (true) {
            String email = JOptionPane.showInputDialog(mensaje, valorInicial);
            if (email == null) {
                return null; }
            email = email.trim().toLowerCase();
            if (email.matches("^[\\w+-]+(\\.[\\w+-]+)*@biancatex\\.com$")) {
                return email; }
            JOptionPane.showMessageDialog(null, "El email debe ser de la empresa .");
            valorInicial = email;} }
    
    
	// entero mayor o igual a 0 (para stock)
	public static Integer validarEnteroCero(String mensaje) {
		return validarEnteroCero(mensaje, "");}

	
	// igual pero con valor inicial cargado (para editar)
	public static Integer validarEnteroCero(String mensaje, String valorInicial) {
		Integer numero = null;
		boolean valido = false;
		do {
			String texto = JOptionPane.showInputDialog(mensaje, valorInicial);
			if (texto == null) return null;
			texto = texto.trim();
			if (texto.isEmpty() || !texto.matches("[0-9]+")) {
				JOptionPane.showMessageDialog(null, "Ingrese solo números (0 o mayor)");
			} else if (texto.length() > 9) {
				JOptionPane.showMessageDialog(null, "El número es demasiado grande");
			} else {
				numero = Integer.parseInt(texto);
				valido = true;}
		} while (!valido);
		return numero;}
	
	
	
	
	//revisa q sean formatos correctos
	public static boolean esDniValido(String dni) {
		return dni.matches("[0-9]{7,8}");}

	public static boolean esCuitValido(String cuit) {
		return cuit.matches("(20|23|24|25|26|27|30|33|34)[0-9]{9}");}

	// valida DNI (crear)
	public static String validarDni(String mensaje) {
		return validarDni(mensaje, "");}

	// valida DNI con valor inicial (editar)
	public static String validarDni(String mensaje, String valorInicial) {
		String dni;
		boolean valido = false;
		do {
			dni = JOptionPane.showInputDialog(mensaje, valorInicial);
			if (dni == null) return null; 
			dni = dni.trim();
			if (esDniValido(dni)) {
				valido = true;
			} else {
				JOptionPane.showMessageDialog(null, "El DNI debe tener 7 u 8 números, sin puntos");
			}} while (!valido);
		return dni;}

	
	// valida CUIT (crear)
	public static String validarCuit(String mensaje) {
		return validarCuit(mensaje, "");}

	// valida CUIT con valor inicial (editar)
	public static String validarCuit(String mensaje, String valorInicial) {
		String cuit;
		boolean valido = false;
		do {
			cuit = JOptionPane.showInputDialog(mensaje, valorInicial);
			if (cuit == null) return null; // cancelar
			cuit = cuit.trim();
			if (esCuitValido(cuit)) {
				valido = true;
			} else {
				JOptionPane.showMessageDialog(null, "El CUIT debe tener 11 números, sin guiones, y empezar con 20, 23, 24, 25, 26, 27, 30, 33 o 34");
			}} while (!valido);
		return cuit;}
	
	
	
	
}