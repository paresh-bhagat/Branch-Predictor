
public class Predictor2400 extends Predictor {
	
	Table gshare = new Table(1024,2);
	Register access_register = new Register(10);
	Register ghr =new Register(10);
	public Predictor2400() {
	
	for(int i=0;i<=9;i++)
	{
		access_register.setBitAtIndex(i,false);
	}
	for(int i=0;i<=9;i++)
	{
		ghr.setBitAtIndex(i,false);
	}
	for(int i=0;i<=1023;i++)
	{
		for(int j=0;j<=1;j++)
		{
			gshare.setBit(i,j,false);
		}
	}
		
	}


	public void Train(long address, boolean outcome, boolean predict) {
		
		int k=9;
		boolean value1;
		boolean value2;
		int address_value=0;
		boolean zero_bit;
		boolean first_bit;
		for(int i=0;i<=9;i++)
		{
			value1=ghr.getBitAtIndex(9-i);
			if((address & (1<<i))!=0)  
				value2=true;		
			else                       
				value2=false;
			if(value1!=value2)
				access_register.setBitAtIndex(k,true);
			else
				access_register.setBitAtIndex(k,false);
			k--;
		}
		for(int i=9;i>=0;i--)
		{
			value1=access_register.getBitAtIndex(i);
			if(value1==true)
				address_value=address_value+(int)(1*Math.pow(2,9-i));
		}
		zero_bit=gshare.getBit(address_value,1);
		first_bit=gshare.getBit(address_value,0);
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
		for(int i=8;i>=0;i--)
		{
			value1=ghr.getBitAtIndex(i);
			ghr.setBitAtIndex(i+1,value1);
		}
		if(outcome==true)
			ghr.setBitAtIndex(0,true);	
		else
			ghr.setBitAtIndex(0,false);
	}


	public boolean predict(long address){

		int k=9;
		boolean value1;
		boolean value2;
		int address_value=0;
		boolean zero_bit;
		boolean first_bit;
		for(int i=0;i<=9;i++)
		{
			value1=ghr.getBitAtIndex(9-i);
			if((address & (1<<i))!=0)  
				value2=true;		
			else                       
				value2=false;
			if(value1!=value2)
				access_register.setBitAtIndex(k,true);
			else
				access_register.setBitAtIndex(k,false);
			k--;
		}
		for(int i=9;i>=0;i--)
		{
			value1=access_register.getBitAtIndex(i);
			if(value1==true)
				address_value=address_value+(int)(1*Math.pow(2,9-i));
		}
		zero_bit=gshare.getBit(address_value,1);
		first_bit=gshare.getBit(address_value,0);
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
