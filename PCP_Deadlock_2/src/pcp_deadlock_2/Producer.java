/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pcp_deadlock_2;

import static pcp_deadlock_2.PCP_Deadlock_2.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pfleger C.
 */
public class Producer extends Thread {
    
    static Boolean hasS = false;
    
    @Override
    public void run(){
        while (true) {
            try {
                s.acquire();
                hasS = true;
                if (count >= MAX) {
                    try {
                        hasS = false;
                        s.release();
                        synchronized(this){ wait(); }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if(hasS){
                    s.release();
                    hasS = false;
                }                
                s.acquire();
                insert_item();
                s.release();
                s.acquire();
                hasS = true;
                if (count >= 1) {
                    hasS = false;
                    s.release();
                    synchronized(cons){ cons.notify();}
                }
                if(hasS){
                    s.release();
                    hasS = false;
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
