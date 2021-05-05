/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scriptlapseexe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jorge
 */
public class ScriptLapseExe {

    private static final boolean EJECUTAR = true;
    private final static Logger LOG = Logger.getLogger(ScriptLapseExe.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        LOG.log(Level.INFO, "Arrancando hilo de ejecución");
        long minuto = 60 * 1000;
        long nMinutos = 90;
        if(args!=null && args.length>0){
            nMinutos = (long)Integer.parseInt(args[0]);
        }
        long tEspera = 0;

        long time = 0;
        int numEjecucion = 1;

        while (EJECUTAR) {
            while (!((System.currentTimeMillis() - time) < tEspera)) {
                try {
                    
                    LOG.log(Level.INFO, "Iniciando ejecución número " + numEjecucion);
                    
                    ejecuta();
                    
                    if (tEspera == 0) {
                        tEspera = minuto * nMinutos;
                    }
                    LOG.log(Level.INFO, "Ejecución número " + numEjecucion + " finalizada");

                    numEjecucion++;
                    time = System.currentTimeMillis();
                    Timestamp ts = new Timestamp(time + tEspera);
                    String t = ts.toString().substring(0, 19);
                    LOG.log(Level.INFO, "Inicio de la ejecución número " + numEjecucion + ": " + t);

                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "Se ha producido un error. Revise la acción.", e);
                    break;
                }
            }
        }
    }

    public static void ejecuta() {
        Process process;
        try {
            Runtime runtime = Runtime.getRuntime();

            process = runtime.exec("./tarea.sh");
            
            LOG.log(Level.INFO, "Ejecutando script 'tarea.sh'... ");

            while (process.isAlive()) {
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                String t = ts.toString().substring(0, 19);
                imprimirOutputProcess(process);
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public static void imprimirOutputProcess(Process p) {
        try {
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            String s = null;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
            int exitVal = p.waitFor();
            System.out.println("Process exitValue: " + exitVal);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
}
