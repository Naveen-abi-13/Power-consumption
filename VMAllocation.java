/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package power;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.power.PowerDatacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.models.PowerModelCubic;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicyMinimumMigrationTime;
import  org.cloudbus.cloudsim.power.PowerVmSelectionPolicy;
import org.cloudbus.cloudsim.power.PowerVm;
/**
 *
 * @author admin
 */
public class VMAllocation 
{
    Details dt=new Details();
    //Datacenter dc1;
    
    DatacenterCharacteristics characteristics;
    
    public void readVM()
    {
        try
        {
            File fe=new File("vm1.txt");
            FileInputStream fis=new FileInputStream(fe);
            byte bt[]=new byte[fis.available()];
            fis.read(bt);
            fis.close();
            
            String g1=new String(bt);
            System.out.println("VM List");
            System.out.println("=========================");
            System.out.println(g1);
            String g2[]=g1.split("\n");
            for(int i=1;i<g2.length;i++)
            {
                dt.Vt.add(g2[i].trim());
            }
            
            dt.vms=new String[dt.Vt.size()][4];
             for(int i=0;i<dt.Vt.size();i++)
            {
                String a1[]=dt.Vt.get(i).toString().trim().split("\t");
                dt.vms[i][0]=a1[0];    // VM Id                    
                dt.vms[i][1]=a1[1];    // VM cpu
                dt.vms[i][2]=a1[2];    // VM ram
                dt.vms[i][3]=a1[3];    // VM bw
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
     public void readHost()
    {
        try
        {
            File fe=new File("host2.txt");
            FileInputStream fis=new FileInputStream(fe);
            byte bt[]=new byte[fis.available()];
            fis.read(bt);
            fis.close();
            
            String g1=new String(bt);
            System.out.println("Host List");
            System.out.println("=========================");
            System.out.println(g1);
            String g2[]=g1.split("\n"); 
                 
            for(int i=1;i<g2.length;i++)
            {
                dt.Ht.add(g2[i].trim());                
            }
            
            dt.host=new String[dt.Ht.size()][4];
               
            for(int i=0;i<dt.Ht.size();i++)
            {
                String a1[]=dt.Ht.get(i).toString().trim().split("\t");
                dt.host[i][0]=a1[0];    // Host Id
                dt.host[i][1]=a1[1];    // Host cpu
                dt.host[i][2]=a1[2];    // Host ram
                dt.host[i][3]=a1[3];    // Host bw                
            }  
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void createHost()
    {
        try
        {
            Log.printLine("Starting CloudSim");
            CloudSim cs=new CloudSim();
            Calendar calendar = Calendar.getInstance();
            cs.init(1, calendar,false);
                
            String name="DC1";
            
            for(int i=0;i<dt.Ht.size();i++)
            {
                String a1[]=dt.Ht.get(i).toString().split("\t");
                
                int id=Integer.parseInt(a1[0]);
                int cpu=Integer.parseInt(a1[1]);
                int ram1=Integer.parseInt(a1[2]);
                int bw2=Integer.parseInt(a1[3]);
                int storage=100000;
                List<Pe> peList1 = new ArrayList<Pe>();
                int mips1 = cpu;//1000000;
                
                for(int k=0;k<cpu;k++)
                    peList1.add(new Pe(0, new PeProvisionerSimple(mips1))); 
                //peList1.add(new Pe(0, new PeProvisionerSimple(mips1))); 
                
                //dt.hostList.add(new Host(id, new RamProvisionerSimple(ram1),new BwProvisionerSimple(bw2), storage, peList1,new VmSchedulerTimeShared(peList1))); 
                dt.hostList.add(new PowerHost(id, new RamProvisionerSimple(ram1),new BwProvisionerSimple(bw2), storage, peList1,new VmSchedulerTimeShared(peList1),new PowerModelCubic(1000,500))); 
            }
            
            String arch = "x86"; 
            String os = "Linux"; 
            String vmm1 = "Xen";
            double time_zone = 10.0; 
            double cost = 3.0; 
            double costPerMem = 0.05; 
            double costPerStorage = 0.2;
									
            double costPerBw = 0.1;
            LinkedList<Storage> storageList = new LinkedList<Storage>();
                    
            characteristics = new DatacenterCharacteristics(arch, os, vmm1, dt.hostList, time_zone, cost, costPerMem,costPerStorage, costPerBw);
                   
            //dc1 = new Datacenter(name, characteristics, new VmAllocationPolicySimple(dt.hostList), storageList, 0);
            dt.dc1=new PowerDatacenter(name, characteristics, new VmAllocationPolicySimple(dt.hostList), storageList, 0);
            System.out.println("Data Center Created with "+dt.Ht.size()+" Host");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
   
    public void createVM()
    {
        try
        {
            for(int i=0;i<dt.vms.length;i++)
            {
                int vmid = Integer.parseInt(dt.vms[i][0]);
                int cid=Integer.parseInt(dt.vms[i][0]);
                    
                int mips = 250;
                long size = 10000; //image size (MB)
                int ram = Integer.parseInt(dt.vms[i][2]); //vm memory (MB)
                long bw = Long.parseLong(dt.vms[i][3]);
                int pesNumber = Integer.parseInt(dt.vms[i][1]); //number of cpus
                String vmm = "Xen"; //VMM name
                        
                //Vm vm1 = new Vm(vmid,cid, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
                PowerVm vm1 = new PowerVm(vmid,cid, mips, pesNumber, ram, bw, size,1 ,vmm, new CloudletSchedulerTimeShared(),0.5);
                
                
                System.out.println("VM-"+vmid+" is Created...");
                dt.vmlist.add(vm1);
                        
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
     public void optimiseVmAllocation()
     {
        try
        {
            Random rn=new Random();
            for(int i=0;i<dt.pop;i++)
            {
                String pp1="";
                for(int j=0;j<dt.vmlist.size();j++)
                {
                    int s1=rn.nextInt(dt.hostList.size())+1;
                    pp1=pp1+s1+"#";
                }
                pp1=pp1.substring(0, pp1.lastIndexOf("#"));                
                dt.population.add(pp1);                
                System.out.println(pp1);
            }
			
            double e1=0;
            for(int j=0;j<dt.hostList.size();j++)
            {
            
                PowerHost ph=dt.hostList.get(j);                
                
                PSO ps=new PSO();
                ps.applyPSO();
                double uti=ps.fittnessFun(ph);
                e1=e1+uti;
                System.out.println("Utilization for Host - "+ph.getId()+" = "+uti);
            }                  
            
                
            dt.ppow=(e1/(double)dt.hostList.size());
            
            System.out.println("power = "+dt.ppow);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
     }    
     
}
