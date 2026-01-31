/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package power;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.VmSchedulerTimeSharedOverSubscription;
import org.cloudbus.cloudsim.power.PowerDatacenterBroker;
import org.cloudbus.cloudsim.power.PowerDatacenter;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerHostUtilizationHistory;
import org.cloudbus.cloudsim.power.PowerVm;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationAbstract;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationInterQuartileRange;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationStaticThreshold;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicy;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicyMaximumCorrelation;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicyMinimumMigrationTime;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G4Xeon3040;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;


/**
 *
 * @author 91790
 */
public class ALO 
{
    Details dt=new Details();
    ALO()
    {
        
    }
    
    public void applyALO()
    {
        try
        {
            Random rn=new Random();
            
            double X[][]=new double[dt.vmlist.size()][dt.hostList.size()];
            
            double Mant[][]=new double[dt.vmlist.size()][dt.hostList.size()];
            double Mantlion[][]=new double[dt.vmlist.size()][dt.hostList.size()];
            
            
            double amin=Double.MAX_VALUE;
            double bmax=0;
            for(int i=0;i<dt.vmlist.size();i++)
            {
                X[i][0]=0;
                for(int j=1;j<dt.hostList.size();j++)
                {
                    double e1=rn.nextDouble();
                    double r=0;
                    if(e1>0.5)
                    {
                        r=1;
                    }
                    
                    for(int k=0;k<j;k++)
                        r=r+X[i][k];
                    
                    X[i][j]=r;
                }
                for(int j=0;j<dt.hostList.size();j++)
                {
                    amin=Math.min(amin, X[i][j]);
                    bmax=Math.max(bmax, X[i][j]);
                }
            }
            
            for(int i=0;i<dt.vmlist.size();i++)
            {
                for(int j=0;j<dt.hostList.size();j++)
                {
                    System.out.print(X[i][j]+"\t");
                }
                System.out.println();
            }
            
            System.out.println(amin+" : "+bmax);
            double Y[][]=new double[dt.vmlist.size()][dt.hostList.size()];
            
            for(int i=0;i<dt.vmlist.size();i++)
            {
                double cmin=Double.MAX_VALUE;
                double dmax=0;
                for(int j=0;j<dt.hostList.size();j++)
                {
                    cmin=Math.min(cmin, X[i][j]);
                    dmax=Math.max(dmax, X[i][j]);
                }
                System.out.println(cmin+" = "+dmax);
                for(int j=0;j<dt.maxIter;j++)
                {
                    Y[i][j]=((X[i][j]-amin)/(bmax-amin))*(dmax-cmin)+cmin;
                }
            }
            
            for(int i=0;i<dt.vmlist.size();i++)
            {
                for(int j=0;j<dt.hostList.size();j++)
                {
                    System.out.print(Y[i][j]+"\t");
                }
                System.out.println();
            }
            
            for(int i=0;i<dt.vmlist.size();i++)
            {
                for(int j=0;j<dt.hostList.size();j++)
                {
                    Mant[i][j]=(bmax-amin)*rn.nextDouble()+amin;
                    Mantlion[i][j]=(bmax-amin)*rn.nextDouble()+amin;
                }
            }
            
            double alfit[]=new double[dt.vmlist.size()];
            double afit[]=new double[dt.vmlist.size()];
            for(int i=0;i<dt.vmlist.size();i++)
            {
                PowerVm v1=dt.vmlist.get(i);
                double e1=0;
                double e2=0;
                for(int j=0;j<dt.hostList.size();j++)
                {
                    PowerHost ph1=dt.hostList.get(j);
                    if(ph1.isSuitableForVm(v1))
                    {
                        e1=e1+(Mant[i][j]);
                        e2=e2+(Mantlion[i][j]);
                    }
                }
                afit[i]=e1/(double)dt.hostList.size();
                alfit[i]=e2/(double)dt.hostList.size();
            }
            int bestaInd=0;
            int bestalInd=0;
            double maxafit=0;
            double maxalfit=0;
            for(int i=0;i<dt.vmlist.size();i++)
            {
                System.out.println(afit[i]+" : "+alfit[i]);
                int antlt=roulette(alfit);
               if(maxafit<afit[i])
               {
                   maxafit=afit[i];
                   bestaInd=i;
               }
               if(maxalfit<alfit[i])
               {
                   maxalfit=alfit[i];
                   bestalInd=i;
               }
                   
            }
            
            System.out.println(maxafit+" = "+bestaInd);
            System.out.println(maxalfit+" = "+bestalInd);           
                                      
            
            allocate();
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void allocate()
    {
        try
        {
            
            List<PowerVm> vmlt = new ArrayList<PowerVm>();
            for(int i=0;i<dt.vms.length;i++)
            {                
                vmlt.add(new PowerVm(i,1,Double.parseDouble(dt.vms[i][1]),
                                           1,
                                           Integer.parseInt(dt.vms[i][2]),
                                           Integer.parseInt(dt.vms[i][3]),
                                           10000,
                                           1,
                                           "Xen",
                                            new CloudletSchedulerDynamicWorkload(Double.parseDouble(dt.vms[i][1]), 1),
                                            50));
            }
            
            List<PowerHost> hostlt = new ArrayList<PowerHost>();
            for (int i = 0; i < dt.host.length; i++) 
            {
                List<Pe> peList = new ArrayList<Pe>();
                for (int j = 0; j <1; j++) 
                {
        		peList.add(new Pe(j, new PeProvisionerSimple(Integer.parseInt(dt.host[i][1]))));
                }

            hostlt.add(new PowerHostUtilizationHistory(
			i,
			new RamProvisionerSimple(Integer.parseInt(dt.host[i][2])),
			new BwProvisionerSimple(Integer.parseInt(dt.host[i][3])),
			1000000,
			peList,
			new VmSchedulerTimeSharedOverSubscription(peList),
			new PowerModelSpecPowerHpProLiantMl110G4Xeon3040()));
        }
            
            PowerVmSelectionPolicy vmSelPolicy =  new PowerVmSelectionPolicyMaximumCorrelation(new PowerVmSelectionPolicyMinimumMigrationTime());
            
            PowerVmAllocationPolicyMigrationAbstract fallbackVmSelectionPolicy = new PowerVmAllocationPolicyMigrationStaticThreshold(
					hostlt,
					vmSelPolicy,
					0.7);
            
            VmAllocationPolicy vp = new PowerVmAllocationPolicyMigrationInterQuartileRange(
					hostlt,
					vmSelPolicy,					
                                        0.8,
					fallbackVmSelectionPolicy);
            double e1=0;
            for(int i=0;i<vmlt.size();i++)
            {               
                PowerVm vm=vmlt.get(i);
                boolean bo=vp.allocateHostForVm(vm);
                if(bo)
                {    
                    PowerHost ht=(PowerHost)vp.getHost(vm);                    
                    double uti=ht.getUtilizationOfRam()+ht.getUtilizationOfBw()+ht.getUtilizationOfCpuMips();                    
                    e1=e1+uti;
                }
            }
            dt.apow=(e1/(double)dt.hostList.size());
            System.out.println("power = "+dt.apow);
        }
        catch(Exception e)       
        {
            e.printStackTrace();
        }
    }
    public int roulette(double weight[])
    {
        int ind=0;
        try
        {
            Random rn=new Random();
            double weight_sum = 0;
            for(int i=0; i<weight.length; i++) 
            {
		weight_sum += weight[i];
            }
            double value = rn.nextDouble() * weight_sum;		
            		
            for(int i=0; i<weight.length; i++) 
            {		
            	value -= weight[i];		
            	if(value <= 0) 
		{
                    ind=i;
                    break;
		}
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return ind;
    }
}
