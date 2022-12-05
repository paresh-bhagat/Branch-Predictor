
public class Predictor9999 extends Predictor {
	Table pap = new Table(4096,2);
	Table ghr = new Table(256,5);
	Register access_register = new Register(12);
	
	public Predictor9999() {
	for(int i=0;i<=11;i++)
	{
		access_register.setBitAtIndex(i,false);
	}
	for(int i=0;i<=4095;i++)
	{
		for(int j=0;j<=1;j++)
		{
			pap.setBit(i,j,false);
		}
	}
	for(int i=0;i<=255;i++)
	{
		for(int j=0;j<=4;j++)
		{
			ghr.setBit(i,j,false);
		}
	}

	}


	public void Train(long address, boolean outcome, boolean predict) {

		int k=6;
		boolean value1;
		int address_value=0;
		int access_ghr=0;
		boolean zero_bit;
		boolean first_bit;
		for(int i=0;i<=6;i++)
		{
			if((address & (1<<i))!=0)
				access_register.setBitAtIndex(k,true);
			else
				access_register.setBitAtIndex(k,false);
			k--;
		}
		
		for(int i=12;i<=19;i++)
		{		
			if((address & (1<<i))!=0)
				access_ghr=access_ghr+(int)(1*Math.pow(2,12-i));
		}

		k=7;
		for(int i=0;i<=4;i++)
		{
			value1=ghr.getBit(access_ghr,i);
			access_register.setBitAtIndex(k,value1);
			k++;
		}

		for(int i=11;i>=0;i--)
		{
			value1=access_register.getBitAtIndex(i);
			if(value1==true)
				address_value=address_value+(int)(1*Math.pow(2,11-i));
		}


		zero_bit=pap.getBit(address_value,1);
		first_bit=pap.getBit(address_value,0);
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

		for(int i=3;i>=0;i--)
		{
			value1=ghr.getBit(access_ghr,i);
			ghr.setBit(access_ghr,i+1,value1);
		}
		if(outcome==true)
			ghr.setBit(access_ghr,0,true);	
		else
			ghr.setBit(access_ghr,0,false);

	}


	public boolean predict(long address){

		int k=6;
		boolean value1;
		int address_value=0;
		int access_ghr=0;
		boolean zero_bit;
		boolean first_bit;
		for(int i=0;i<=6;i++)
		{
			if((address & (1<<i))!=0)
				access_register.setBitAtIndex(k,true);
			else
				access_register.setBitAtIndex(k,false);
			k--;
		}
		
		for(int i=12;i<=19;i++)
		{		
			if((address & (1<<i))!=0)
				access_ghr=access_ghr+(int)(1*Math.pow(2,12-i));
		}

		k=7;
		for(int i=0;i<=4;i++)
		{
			value1=ghr.getBit(access_ghr,i);
			access_register.setBitAtIndex(k,value1);
			k++;
		}

		for(int i=11;i>=0;i--)
		{
			value1=access_register.getBitAtIndex(i);
			if(value1==true)
				address_value=address_value+(int)(1*Math.pow(2,11-i));
		}


		zero_bit=pap.getBit(address_value,1);
		first_bit=pap.getBit(address_value,0);
		if(zero_bit==false && first_bit==false)
			return false;
		else if(first_bit==false && zero_bit==true)
			return false;
		else if(first_bit==true && zero_bit==false)
			return true;
		else if(first_bit==true && zero_bit==true)
			return true;
		return false;
		
	}

}
