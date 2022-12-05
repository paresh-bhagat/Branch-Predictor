
public class Predictor32000 extends Predictor {

	Table selector_array = new Table(2048,2);    //selector array
	Register access_register = new Register(11);    
	
	//for pap

	Table pap = new Table(8192,2);
	Table ghr_pap = new Table(512,6);
	Register access_register_pap = new Register(13);

	//for ghare
	
	Table gshare = new Table(4096,2);
	Register access_register_gshare = new Register(12);
	Register ghr_gshare =new Register(12);
	
	public Predictor32000() {
	
	//for selector

	for(int i=0;i<=2043;i++)
	{
		for(int j=0;j<=1;j++)
		{
			selector_array.setBit(i,j,false);
		}
	}

	for(int i=0;i<=10;i++)
	{
		access_register.setBitAtIndex(i,false);
	}


	//for pap

	for(int i=0;i<=12;i++)
	{
		access_register_pap.setBitAtIndex(i,false);
	}
	for(int i=0;i<=8191;i++)
	{
		for(int j=0;j<=1;j++)
		{
			pap.setBit(i,j,false);
		}
	}
	for(int i=0;i<=511;i++)
	{
		for(int j=0;j<=5;j++)
		{
			ghr_pap.setBit(i,j,false);
		}
	}

	//for gshare

	for(int i=0;i<=11;i++)
	{
		access_register_gshare.setBitAtIndex(i,false);
	}
	for(int i=0;i<=11;i++)
	{
		ghr_gshare.setBitAtIndex(i,false);
	}
	for(int i=0;i<=4095;i++)
	{
		for(int j=0;j<=1;j++)
		{
			gshare.setBit(i,j,false);
		}
	}


	}


	public void Train(long address, boolean outcome, boolean predict) {

		//train pap

		int k=6;
		boolean value1;
		int address_value=0;
		int access_ghr=0;
		boolean zero_bit;
		boolean first_bit;
		boolean output_pap=false;
		for(int i=0;i<=6;i++)
		{
			if((address & (1<<i))!=0)
				access_register_pap.setBitAtIndex(k,true);
			else
				access_register_pap.setBitAtIndex(k,false);
			k--;
		}
		
		for(int i=12;i<=20;i++)
		{		
			if((address & (1<<i))!=0)
				access_ghr=access_ghr+(int)(1*Math.pow(2,12-i));
		}

		k=7;
		for(int i=0;i<=5;i++)
		{
			value1=ghr_pap.getBit(access_ghr,i);
			access_register_pap.setBitAtIndex(k,value1);
			k++;
		}

		for(int i=12;i>=0;i--)
		{
			value1=access_register_pap.getBitAtIndex(i);
			if(value1==true)
				address_value=address_value+(int)(1*Math.pow(2,12-i));
		}

		zero_bit=pap.getBit(address_value,1);
		first_bit=pap.getBit(address_value,0);
		if(zero_bit==false && first_bit==false)
			output_pap = false;
		else if(first_bit==false && zero_bit==true)
			output_pap = false;
		else if(first_bit==true && zero_bit==false)
			output_pap = true;
		else if(first_bit==true && zero_bit==true)
			output_pap = true;
		if(zero_bit==false && first_bit==false)
		{
			if(outcome==true)
			{
				pap.setBit(address_value,1,true);
			}				

		}
		else if(first_bit==false && zero_bit==true)
		{
			if(outcome==true)
			{
				pap.setBit(address_value,1,false);
				pap.setBit(address_value,0,true);
			}
			else
			{
				pap.setBit(address_value,1,false);
			}
		}
		else if(first_bit==true && zero_bit==false)
		{
     			if(outcome==true)
			{
				pap.setBit(address_value,1,true);
			}
			else
			{
				pap.setBit(address_value,1,true);
				pap.setBit(address_value,0,false);
			}
		}
		else if(first_bit==true && zero_bit==true)
		{
       			if(outcome==false)
			{
				pap.setBit(address_value,1,false);
			}
				
		}

		for(int i=4;i>=0;i--)
		{
			value1=ghr_pap.getBit(access_ghr,i);
			ghr_pap.setBit(access_ghr,i+1,value1);
		}
		if(outcome==true)
			ghr_pap.setBit(access_ghr,0,true);	
		else
			ghr_pap.setBit(access_ghr,0,false);


		//train gshare

		k=11;
		boolean value2;
		address_value=0;
		boolean output_gshare=false;
		for(int i=0;i<=11;i++)
		{
			value1=ghr_gshare.getBitAtIndex(11-i);
			if((address & (1<<i))!=0)  
				value2=true;		
			else                       
				value2=false;
			if(value1!=value2)
				access_register_gshare.setBitAtIndex(k,true);
			else
				access_register_gshare.setBitAtIndex(k,false);
			k--;
		}
		for(int i=11;i>=0;i--)
		{
			value1=access_register_gshare.getBitAtIndex(i);
			if(value1==true)
				address_value=address_value+(int)(1*Math.pow(2,11-i));
		}
		zero_bit=gshare.getBit(address_value,1);
		first_bit=gshare.getBit(address_value,0);
		if(zero_bit==false && first_bit==false)
			output_gshare = false;
		else if(first_bit==false && zero_bit==true)
			output_gshare = false;
		else if(first_bit==true && zero_bit==false)
			output_gshare = true;
		else if(first_bit==true && zero_bit==true)
			output_gshare = true;
		if(zero_bit==false && first_bit==false)
		{
			if(outcome==true)
			{
				gshare.setBit(address_value,1,true);
			}				

		}
		else if(first_bit==false && zero_bit==true)
		{
			if(outcome==true)
			{
				gshare.setBit(address_value,1,false);
				gshare.setBit(address_value,0,true);
			}
			else
			{
				gshare.setBit(address_value,1,false);
			}
		}
		else if(first_bit==true && zero_bit==false)
		{
     			if(outcome==true)
			{
				gshare.setBit(address_value,1,true);
			}
			else
			{
				gshare.setBit(address_value,1,true);
				gshare.setBit(address_value,0,false);
			}
		}
		else if(first_bit==true && zero_bit==true)
		{
       			if(outcome==false)
			{
				gshare.setBit(address_value,1,false);
			}
				
		}
		for(int i=10;i>=0;i--)
		{
			value1=ghr_gshare.getBitAtIndex(i);
			ghr_gshare.setBitAtIndex(i+1,value1);
		}
		if(outcome==true)
			ghr_gshare.setBitAtIndex(0,true);	
		else
			ghr_gshare.setBitAtIndex(0,false);

		//train selector
		
		if(output_gshare!=output_pap)
		{
			k = 10;
			address_value=0;
			for(int i=0;i<=10;i++)
			{
				if((address & (1<<i))!=0)
					access_register.setBitAtIndex(k,true);
				else
					access_register.setBitAtIndex(k,false);
				k--;
			}
			for(int i=10;i>=0;i--)
			{
				value1=access_register.getBitAtIndex(i);
				if(value1==true)
					address_value=address_value+(int)(1*Math.pow(2,10-i));
			}
			zero_bit=selector_array.getBit(address_value,1);
			first_bit=selector_array.getBit(address_value,0);

			if(zero_bit==false && first_bit==false)
			{
				if(outcome==output_gshare)
				{
					selector_array.setBit(address_value,1,true);
				}				

			}
			else if(first_bit==false && zero_bit==true)
			{
				if(outcome==output_gshare)
				{
					selector_array.setBit(address_value,1,false);
					selector_array.setBit(address_value,0,true);
				}
				else if(outcome==output_pap)
				{
					selector_array.setBit(address_value,1,false);
				}
			}
			else if(first_bit==true && zero_bit==false)
			{
     				if(outcome==output_gshare)
				{
					selector_array.setBit(address_value,1,true);
				}
				else if(outcome==output_pap)
				{
					selector_array.setBit(address_value,1,true);
					selector_array.setBit(address_value,0,false);
				}
			}
			else if(first_bit==true && zero_bit==true)
			{
       				if(outcome==output_pap)
				{
					selector_array.setBit(address_value,1,false);
				}
				
			}
			
		}
		
	}


	public boolean predict(long address){
		
		//pap
		
		int k=6;
		boolean value1;
		int address_value=0;
		int access_ghr=0;
		boolean zero_bit;
		boolean first_bit;
		boolean output_pap=false;
		for(int i=0;i<=6;i++)
		{
			if((address & (1<<i))!=0)
				access_register_pap.setBitAtIndex(k,true);
			else
				access_register_pap.setBitAtIndex(k,false);
			k--;
		}
		
		for(int i=12;i<=20;i++)
		{		
			if((address & (1<<i))!=0)
				access_ghr=access_ghr+(int)(1*Math.pow(2,12-i));
		}

		k=7;
		for(int i=0;i<=5;i++)
		{
			value1=ghr_pap.getBit(access_ghr,i);
			access_register_pap.setBitAtIndex(k,value1);
			k++;
		}

		for(int i=12;i>=0;i--)
		{
			value1=access_register_pap.getBitAtIndex(i);
			if(value1==true)
				address_value=address_value+(int)(1*Math.pow(2,12-i));
		}


		zero_bit=pap.getBit(address_value,1);
		first_bit=pap.getBit(address_value,0);
		if(zero_bit==false && first_bit==false)
			output_pap = false;
		else if(first_bit==false && zero_bit==true)
			output_pap = false;
		else if(first_bit==true && zero_bit==false)
			output_pap = true;
		else if(first_bit==true && zero_bit==true)
			output_pap = true;

		//gshare

		k=11;
		boolean value2;
		address_value=0;
		boolean output_gshare=false;
		for(int i=0;i<=11;i++)
		{
			value1=ghr_gshare.getBitAtIndex(11-i);
			if((address & (1<<i))!=0)  
				value2=true;		
			else                       
				value2=false;
			if(value1!=value2)
				access_register_gshare.setBitAtIndex(k,true);
			else
				access_register_gshare.setBitAtIndex(k,false);
			k--;
		}
		for(int i=11;i>=0;i--)
		{
			value1=access_register_gshare.getBitAtIndex(i);
			if(value1==true)
				address_value=address_value+(int)(1*Math.pow(2,11-i));
		}
		zero_bit=gshare.getBit(address_value,1);
		first_bit=gshare.getBit(address_value,0);
		if(zero_bit==false && first_bit==false)
			output_gshare = false;
		else if(first_bit==false && zero_bit==true)
			output_gshare = false;
		else if(first_bit==true && zero_bit==false)
			output_gshare = true;
		else if(first_bit==true && zero_bit==true)
			output_gshare = true;
		
		
		//selector array

		k = 10;
		address_value=0;
		for(int i=0;i<=10;i++)
		{
			if((address & (1<<i))!=0)
				access_register.setBitAtIndex(k,true);
			else
				access_register.setBitAtIndex(k,false);
			k--;
		}
		for(int i=10;i>=0;i--)
		{
			value1=access_register.getBitAtIndex(i);
			if(value1==true)
				address_value=address_value+(int)(1*Math.pow(2,10-i));
		}
		zero_bit=selector_array.getBit(address_value,1);
		first_bit=selector_array.getBit(address_value,0);
		if(zero_bit==false && first_bit==false)
			return output_pap;
		else if(first_bit==false && zero_bit==true)
			return output_pap;
		else if(first_bit==true && zero_bit==false)
			return output_gshare;
		else if(first_bit==true && zero_bit==true)
			return output_gshare;
		return false;
	}

}
