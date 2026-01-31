/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package power;

import java.util.List;
import java.util.ArrayList;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.power.PowerDatacenter;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerVm;

/**
 *
 * @author admin
 */
public class Details 
{
    static PowerDatacenter dc1;
    static String vms[][];
    static String host[][];
    
    static ArrayList Vt=new ArrayList();
    static ArrayList Ht=new ArrayList();
    
    //static List<Vm> vmlist=new ArrayList<Vm>();
    static List<PowerVm> vmlist=new ArrayList<PowerVm>();
    //static List<Host> hostList=new ArrayList<Host>(); // 
     static List<PowerHost> hostList=new ArrayList<PowerHost>(); // PM
    
    static double Velocity[][];
    static double Position[][];
     static ArrayList request=new ArrayList();
    static ArrayList population=new ArrayList();
    static ArrayList initialpop=new ArrayList();
    static int pop=8;
    static double pbest[]=new double[pop];
    static double gbest=200;
    static String psobest;
    static List<PowerVm> allVM=new ArrayList<PowerVm>();
    static ArrayList newList=new ArrayList();
    
    static int maxIter=10;
    
    static long ptime=0;
    static long atime=0;
    
    static double ppow=0;
    static double apow=0;
}
