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
            if (entrada == null) return null; // Cancelar

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
                }
            }
            if (!valido) {
                JOptionPane.showMessageDialog(null, "Ingresá solo letras.");
            }
        } while (!valido);
        return entrada;  }

    // Solo números (CUIT, teléfono)
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
                JOptionPane.showMessageDialog(null, "Ingresá solo números.");
            }
        } while (!valido);
        return entrada; }
    
    
    public static String validarEmail(String mensaje) {
        while (true) {
            String email = JOptionPane.showInputDialog(mensaje);
            if (email == null) {
                return null; }

            email = email.trim().toLowerCase();

            if (email.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
                return email; }

            JOptionPane.showMessageDialog(null, "Ingrese un email válido (ej: nombre@dominio.com)."); }
                      }
    
    
}