/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package power;

/**
 *
 * @author admin
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        long tm1=System.currentTimeMillis();
        VMAllocation vm=new VMAllocation();
        vm.readVM();
        vm.readHost();
        vm.createHost();
        vm.createVM();
        
        vm.optimiseVmAllocation();
        
        long tm2=System.currentTimeMillis();
        long tim=tm2-tm1;
        System.out.println(tim);
        
        long atm1=System.currentTimeMillis();
       System.out.println("ALO");
       ALO al=new ALO();
       al.applyALO();
       long atm2=System.currentTimeMillis();
       long atim=atm2-atm1;
       
       Graph1 gr=new Graph1();
        gr.display1(tim,atim);
        gr.display2();
    }
    
}
