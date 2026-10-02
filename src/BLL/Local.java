package BLL;

import java.util.LinkedList;

import javax.swing.JOptionPane;

import DDL.ControllerLocal;

public class Local {

    private int IdLocal;
    private String direccion;

    public Local(int idLocal, String direccion) {
        super();
        IdLocal = idLocal;
        this.direccion = direccion;
    }

    public int getIdLocal() {
        return IdLocal;
    }

    public void setIdLocal(int idLocal) {
        IdLocal = idLocal;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "Local [IdLocal=" + IdLocal + ", direccion=" + direccion + "]";
    }

    public static void crearLocal() {
        String direccion = JOptionPane.showInputDialog("Ingrese la dirección del local:");
        if (direccion == null || direccion.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Operación cancelada.");
            return;
        }
        boolean exito = ControllerLocal.crearLocal(direccion);
        if (exito) {
            JOptionPane.showMessageDialog(null, "Local creado correctamente.");
        } else {
            JOptionPane.showMessageDialog(null, "Error al crear el local.");
        }
    }

    public static void buscarLocal() {
        String idTexto = JOptionPane.showInputDialog("Ingrese el ID del local a buscar:");
        if (idTexto == null || idTexto.isEmpty()) {
            return;
        }
        int idLocal = Integer.parseInt(idTexto);
        Local local = ControllerLocal.buscarLocalPorId(idLocal);
        if (local != null) {
            String info = "ID: " + local.getIdLocal() + "\n";
            info += "Dirección: " + local.getDireccion();
            JOptionPane.showMessageDialog(null, info);
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró el local.");
        }
    }

    public static void editarLocal() {
        String idTexto = JOptionPane.showInputDialog("Ingrese el ID del local a editar:");
        if (idTexto == null || idTexto.isEmpty()) {
            return;
        }
        int idLocal = Integer.parseInt(idTexto);
        Local local = ControllerLocal.buscarLocalPorId(idLocal);
        if (local == null) {
            JOptionPane.showMessageDialog(null, "No se encontró el local.");
            return;
        }
        String nuevaDireccion = JOptionPane.showInputDialog("Nueva dirección:", local.getDireccion());
        if (nuevaDireccion == null || nuevaDireccion.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Operación cancelada.");
            return;
        }
        local.setDireccion(nuevaDireccion);
        boolean exito = ControllerLocal.editarLocal(local);
        if (exito) {
            JOptionPane.showMessageDialog(null, "Local editado correctamente.");
        } else {
            JOptionPane.showMessageDialog(null, "Error al editar el local.");
        }
    }

    public static void eliminarLocal() {
        String idTexto = JOptionPane.showInputDialog("Ingrese el ID del local a eliminar:");
        if (idTexto == null || idTexto.isEmpty()) {
            return;
        }
        int idLocal = Integer.parseInt(idTexto);
        boolean exito = ControllerLocal.eliminarLocal(idLocal);
        if (exito) {
            JOptionPane.showMessageDialog(null, "Local eliminado correctamente.");
        } else {
            JOptionPane.showMessageDialog(null, "Error al eliminar el local.");
        }
    }

    public static void verLocales() {
        LinkedList<Local> locales = ControllerLocal.obtenerLocales();
        if (locales.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay locales cargados.");
            return;
        }
        String texto = "";
        for (Local l : locales) {
            texto += "ID: " + l.getIdLocal() + " - Dirección: " + l.getDireccion() + "\n";
        }
        JOptionPane.showMessageDialog(null, texto);
    }
	
}
